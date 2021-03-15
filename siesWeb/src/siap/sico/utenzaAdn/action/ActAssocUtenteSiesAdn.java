package siap.sico.utenzaAdn.action;

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
import siap.sico.utente.model.UtenteModel;
import siap.sico.utenzaAdn.model.AssocUtenteSiesAdnModel;
import siap.sico.utenzaAdn.model.UtenzaAdnModel;
import siap.sico.utenzaAdn.util.UtenzaAdnUtils;
import siap.sico.web.ActionSiap;

/**
 * MEV INTEGRAZIONE SIES ADN
 *
 * @author sgioggi
 *
 */
public class ActAssocUtenteSiesAdn extends ActionSiap implements ICostantiSecurity {

	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public String processRequest() throws F3BException {

		// prepara il model dell'utente per la login
		String userId = StringUtils.convertSqlString(getRequestStringParameter(CAMPO_USER_ID));
		String password = getRequestStringParameter(CAMPO_PASSWORD);
		UtenteModel um = new UtenteModel(userId, password);
		um.setIP(getRequest().getRemoteAddr());
		siesLogger.debug("USERID = " + userId + " # REMOTE IP ADDRESS = " + um.getIP());
		try {
			um = UtenzaAdnUtils.preLogin(um, true);
		} catch (Exception e) {
			setRequestAttribute(IWebConstants.MESSAGE_TEXT, e.getMessage());
			return IWebConstants.PG_MESSAGE_ADN;
		}
		// Verifico che la password sia diversa dalla username ...
		if (Utils.cryptPassword(um.getUserId()).equals(um.getPwd()) || um.getPwd() == null) {
			// Se si...chiamo la maschera di cambio password obbligatoria
			setRequestAttribute("msg", "Devi obbligatoriamente cambiare la password");
			siesLogger.info("Obbligo cambio password");
			return PG_CHANGE_PASSWORD;
		}

		// MEV INTEGRAZIONE SIES ADN:
		// Login sul sistema SIES con verifica della corrispondenza username e password inseriti;
		// Il sistema si interessa di effettuare la consueta procedura di login su SIES.
		// Non è prevista nessuna modifica al flusso implementativo a tale funzione.
		// 1- Verifica della presenza a sistema dell'associazione utenza SIES specificata ed utenza ADN;
		// Il sistema deve fare accesso alla tabella ASSOC_UTENTE_SIES_ADN e verificare che esista
		// un record per lo user SIEP (COD_UTENTE) specificato in fase di login.
		// Per record individuato, il sistema recupera i dettagli dell'utente e dell'ufficio e
		// mostra la pagina per l'accesso alla home page del sies
		// 2- In caso di NON associazione dell'utenza, il sistema procederà con l'accoppiamento delle
		// utenze scrivendo il dato in un'apposita tabella che sarà creata sul sistema SIES;
		// Nella casistica in cui, non esiste nessuna associazione tra utenza SIES ed utenza ADN,
		// il sistema deve occuparsi di 'registrare' i dati nella tabella ASSOC_UTENTE_SIES_ADN e
		// nella tabella UTENZA_ADN.
		// 3- In caso di associazione GIA' presente, il sistema mostrerà un messaggio di utente riconosciuto
		// per l'ufficio scelto;
		// 4- In ogni caso l'utente può procedere con la configurazione di eventuale ogni altro account di
		// cui dispone per l'accesso al SIES;
		// 1)
		String usernameDB = getRequestStringParameter("usernameDB");
		List<AssocUtenteSiesAdnModel> listaUtenzeAdn = UtenzaAdnUtils.verificaAssociazioneSiesAdn(usernameDB);
		boolean giaAssociata = false;
		Iterator<AssocUtenteSiesAdnModel> i = listaUtenzeAdn.iterator();
		while (i.hasNext()) {
			AssocUtenteSiesAdnModel ausam = i.next();
			if (usernameDB.equals(ausam.getUteCodUtente())) {
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
				if (e.getMessage().contains("00001"))
					setRequestAttribute(IWebConstants.MESSAGE_TEXT, "Attenzione! Account già configurato.");
				else
					setRequestAttribute(IWebConstants.MESSAGE_TEXT, e.getMessage());
				return IWebConstants.PG_MESSAGE_ADN;
			}
			if (Utils.isPresent(ausam)) {
				siesLogger.info("Inserita Associazione Utenza SIES-ADN: " + "" + ausam.getUteCodUtente());
				setRequestAttribute("msg",
						"Hai effettuato con successo l'associazione dell'utenza: " + ausam.getUteCodUtente());
			}
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