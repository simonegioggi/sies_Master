package siap.sico.utente.action;

import java.math.BigDecimal;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.F3BException;
import f3b.util.StringUtils;
import f3b.util.Utils;
import f3b.web.IWebConstants;
import siap.sico.security.action.ICostantiSecurity;
import siap.sico.utente.controller.IUtente;
import siap.sico.utente.model.UtenteModel;
import siap.sico.utenzaAdn.model.AssocUtenteSiesAdnModel;
import siap.sico.utenzaAdn.model.UtenzaAdnModel;
import siap.sico.utenzaAdn.util.UtenzaAdnUtils;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;

public class ActModificaPassword extends ActionSiap implements ICostantiUtente {

	// MEV INTEGRAZIONE SIES ADN: aggiunto log
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	/**
	 * Azione di Modifica del Utente
	 *
	 * @return Nome della pagina JSP da visualizzare al termine dell'elaborazione
	 * @throws F3BException
	 */
	public String processRequest() throws F3BException {

		if (((UtenteModel) getSessionAttribute("UtenteConnesso")).getPwd() != null) {
			if (!((UtenteModel) getSessionAttribute("UtenteConnesso")).getPwd()
					.equals(Utils.cryptPassword(getRequestStringParameter("oldPwd")))) {
				setRequestAttribute("msg", "<font color=red><b>Vecchia Password Errata</b></font>");
				return ICostantiSecurity.PG_CHANGE_PASSWORD;
			}
		}

		String userId = getRequestStringParameter(CAMPO_COD_UTENTE);

		UtenteModel um = new UtenteModel();
		um.setUserId(userId);
		um.setPwd(getRequestStringParameter("Pwd"));

		// MEV INTEGRAZIONE SIES ADN: aggiunto controllo
		// Verifico che la password sia diversa dalla username ...
		if (userId.equals(getRequestStringParameter("Pwd"))) {
			// Se si...chiamo la maschera di cambio password obbligatoria
			setRequestAttribute("msg",
					"<font color=red><b>Attenzione: Nuova Password uguale all'Utente!</b></font>");
			return ICostantiSecurity.PG_CHANGE_PASSWORD;
		}

		IUtente iu = SICOLookupRemote.getUtenteRemote();
		iu.ExModificaPassword(um);

		// MEV INTEGRAZIONE SIES ADN: modificata gestione ritorno a seconda della provenienza
		// pagina di login oppure home page
		String testProvenienza = "";
		if (!isRequestParameterNullObj("testProvenienza"))
			testProvenienza = getRequestStringParameter("testProvenienza");

		if (!testProvenienza.contains("obbligatoriamente")) {
			return IWebConstants.PAGE_OPEN_FRAMESET;
		} else {
			String usernameDB = getRequestStringParameter("usernameDB");
			List<AssocUtenteSiesAdnModel> listaUtenzeAdn = UtenzaAdnUtils
					.verificaAssociazioneSiesAdn(usernameDB);
			boolean giaAssociata = false;
			Iterator<AssocUtenteSiesAdnModel> i = listaUtenzeAdn.iterator();
			while (i.hasNext()) {
				AssocUtenteSiesAdnModel ausam = i.next();
				if (userId.equals(ausam.getUteCodUtente())) {
					giaAssociata = true;
					break;
				}
			}

			if (listaUtenzeAdn.isEmpty() || !giaAssociata) {
				// 2)
				BigDecimal bd = null;
				UtenzaAdnModel uam = UtenzaAdnUtils.verificaEsistenzaUtenzaAdn(usernameDB);
				AssocUtenteSiesAdnModel ausam = null;
				try {
					if (Utils.isNullObj(uam) || Utils.isNullObj(uam.getId())) {
						// inserisco in UTENZA_ADN
						bd = UtenzaAdnUtils.inserisciUtenzaAdn(usernameDB);
						siesLogger.info("Inserita Utenza ADN: " + "" + bd);
					} else
						bd = uam.getId();
					ausam = UtenzaAdnUtils.inserisciAssociazioneSiesAdn(userId, bd);
				} catch (Exception e) {
					siesLogger.error("ERRORE in ActAssocUtenteSiesAdn.processRequest: " + e.getMessage());
					setRequestAttribute(IWebConstants.MESSAGE_TEXT, e.getMessage());
					return IWebConstants.PG_MESSAGE_ADN;
				}
				if (Utils.isPresent(ausam)) {
					siesLogger.info("Inserita Associazione Utenza SIES-ADN: " + "" + ausam.getUteCodUtente());
					setRequestAttribute("msg", "Hai effettuato con successo l'associazione dell'utenza: "
							+ ausam.getUteCodUtente());
				}
			} else if (giaAssociata) {
				siesLogger.info(userId + ": Account già configurato!");
				setRequestAttribute(IWebConstants.MESSAGE_TEXT, "Attenzione! Account già configurato.");
				return IWebConstants.PG_MESSAGE_ADN;
			}

			String username = StringUtils.capitalize(usernameDB.substring(0, usernameDB.indexOf("."))) + " "
					+ StringUtils.capitalize(usernameDB.substring(usernameDB.indexOf(".") + 1));
			setRequestAttribute("username", username);
			siesLogger.debug("NOME UTENTE = " + username);
			setRequestAttribute("usernameDB", usernameDB);
			siesLogger.debug("NOME UTENTE DB = " + usernameDB);
			return PG_LOGIN_SIES;
		}
	}

}