package siap.sico.utente.action;

/**
* <p>Title: ActInserisciUtente</p>
* <p>Description: Classe Action per l'inserimento di Utente</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/
import java.math.BigDecimal;
import java.util.Random;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.security.model.ProfileModel;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.util.Utils;
import f3b.web.IWebConstants;
import siap.sico.profilo.controller.IProfilo;
import siap.sico.profilo.model.ProfiloModel;
import siap.sico.security.action.ICostantiSecurity;
import siap.sico.ufficio.controller.IUfficio;
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.utente.controller.IUtente;
import siap.sico.utente.model.UtenteModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siep.parametro.controller.IParametro;
import siap.siep.parametro.model.ParametroModel;
import siap.siep.util.SIEPLookupRemote;

@SuppressWarnings("rawtypes")
public class ActInserisciUtente extends ActionSiap implements ICostantiUtente {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	/**
	 * Azione di Inserimento del Utente
	 * 
	 * @return Nome della pagina JSP da visualizzare al termine dell'elaborazione
	 * @throws F3BException
	 */
	public String processRequest() throws F3BException {

		String lUserid = "";
		Vector lUtente = null;
		boolean Proceed = true;
		// 13/03/2018 metodo introdotto per anomalia m_dg.DOG07.28-02-2018.0007015.U (parametro scadenziario
		// mancante)
		String tipoUfficio = "";

		// Carico i dati dell'utente da inserire
		UtenteModel lUteMod = new UtenteModel();
		UtenteModel lUtTMPMod;

		UfficioModel luff = new UfficioModel();
		luff.setCodUfficio(getRequestStringParameter("cod_uff"));
		lUteMod.setUfficioUtente(luff);

		IUtente lCtrl = SICOLookupRemote.getUtenteRemote();
		IUfficio IUff = SICOLookupRemote.getUfficioRemote();

		// Devo generare la Userid
		// Prendo la prima lettera di decodifica (Cg_ref_codes(ufficio_login), rv_abbreviation)
		if (getRequestStringParameter("flag").equals("no")) {
			lUserid = IUff.getPrefissoUtenteUfficio(luff.getCodUfficio());
			// Ora compongo un codice casuale di 5 cifre numeriche (loop per evitare chiavi duplicate)
			while (Proceed) {
				String tmp;
				Random rnd;
				for (int i = 0; i < 5; i++) {
					rnd = new Random();
					tmp = java.lang.StrictMath.abs(rnd.nextInt()) + "";
					if (tmp.length() > i) {
						lUserid += tmp.charAt(i);
					} else
						lUserid += tmp.charAt(tmp.length() - 1);
				}

				lUteMod.setUserId(lUserid);
				Proceed = false;
				lUtTMPMod = new UtenteModel();
				lUtTMPMod = lCtrl.ExRicercaUtenteByKey(lUserid);
				if (lUtTMPMod != null && !lUtTMPMod.getCognome().equals("")) {
					Proceed = true;
					lUserid = IUff.getPrefissoUtenteUfficio(luff.getCodUfficio());
				}
			}

			/*
			 * se il flag è uguale a "no" allora vuol dire che ancora non ho fatto il controllo sull'esistenza
			 * dell'utente omonimo nello stesso ufficio
			 */

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Il flag è no e quindi faccio i controlli");
			lUtente = lCtrl.ExRicercaUtentePerUfficioCognomeNome(getRequestStringParameter("cod_uff"),
					getRequestStringParameter(CAMPO_COGNOME), getRequestStringParameter(CAMPO_NOME));
		} else {
			lUserid = getRequestStringParameter(CAMPO_COD_UTENTE);
		}

		lUteMod.setUserId(lUserid);
		lUteMod.setCognome(getRequestStringParameter(CAMPO_COGNOME));
		lUteMod.setNome(getRequestStringParameter(CAMPO_NOME));
		lUteMod.setTelefono(getRequestStringParameter(CAMPO_TELEFONO));
		lUteMod.setFax(getRequestStringParameter(CAMPO_FAX));
		lUteMod.setEmail(getRequestStringParameter(CAMPO_E_MAIL));
		lUteMod.setPwd(Utils.cryptPassword(lUserid));

		if (!getRequestStringParameter(CAMPO_ANNO_DATA_FINE_VALIDITA).equals(""))
			lUteMod.setDataFineValidita(getRequestDateParameter(CAMPO_ANNO_DATA_FINE_VALIDITA,
					CAMPO_MESE_DATA_FINE_VALIDITA, CAMPO_GIORNO_DATA_FINE_VALIDITA));

		UtenteModel lUtenteMod = new UtenteModel(
				(UtenteModel) getSessionAttribute(ICostantiSecurity.SESSION_UTENTE_CONNESSO));
		lUteMod.setCodOperatoreInserimento(lUtenteMod.getUserId());

		ProfileModel lprf = new ProfileModel();
		lprf.setProfileId(getRequestBigDecimalParameter("cod_prf"));
		lUteMod.setUserProfile(lprf);

		// >>> 13/03/2018 INIZIO ANOMALIA PER SCADENZIARIO
		// recupero il tipo ufficio per controllare se è un ufficio UDS o UDSM
		tipoUfficio = IUff.getTipoUfficioUtente(luff.getCodUfficio());
		// PER uffici di tipo UDS e UDSM occorre inserire, se non presente, un record nella tabella PARAMETRO
		// PER NOME_PARAMETRO = 'TERMINE SOTTOSCRIZIONE VERBALE M.A.'
		if ("UDS".equals(tipoUfficio) || "UDSM".equals(tipoUfficio)) {
			// 1° EFFETTUO LA RICERCA SULLA TABELLA PARAMETRO
			ParametroModel lParMod = new ParametroModel();
			// Ricerca se esiste Periodo per quell'Ufficio
			lParMod.setNomeParametro("TERMINE SOTTOSCRIZIONE VERBALE M.A.");
			lParMod.setCodUfficioValidita(luff.getCodUfficio());
			IParametro lCtrlPar = SIEPLookupRemote.getParametroRemote();
			Vector lParVect = lCtrlPar.ExRicercaParametroUfficioConnesso(lParMod);
			// 2° SE NON ESISTE IL RECORD, LO DEVO INSERIRE
			if (lParVect == null || lParVect.size() == 0) {
				// Inserisci
				lParMod.setCodOperatoreInserimento(getCodUtenteConnesso());
				lParMod.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
				lParMod.setDataInserimento(DateUtils.getSysDate());
				// IL VALORE DI DEFAULT è DI 30 GIORNI
				lParMod.setGiorni(new BigDecimal(30));
				lParMod.setMesi(new BigDecimal(0));
				lParMod.setAnni(new BigDecimal(0));
				lParMod.setDataInizioValidita(DateUtils.getSysDate());
				lCtrlPar.ExInserisciParametro(lParMod);
			}
		}
		// >>> 13/03/2018 FINE ANOMALIA PER SCADENZIARIO 4

		// Se non esistono omonimi o provengo dalla maschera di conferma omonimi
		// inserisco l'utente
		if (getRequestStringParameter("flag").equals("si") || lUtente == null || lUtente.size() == 0) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Inserisco l'utente perchè non esistono omonimi");
			UtenteModel llUteModRet = lCtrl.ExInserisciUtente(lUteMod); // setta la risposta nella request
			setRequestAttribute("utente", llUteModRet);
			// Prepara la pagina di destinazione
			String lPage = "";
			lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
					+ "=siap.sico.utente.action.ActLoadDettaglioUtente&" + CAMPO_COD_UTENTE + "="
					+ lUteMod.getUserId().toString();
			return lPage;
		} else {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Non inserisco immediatamente l'utente perchè esite un omonimo");
			IProfilo lProf = SICOLookupRemote.getProfiloRemote();
			ProfiloModel lProfMod = lProf.ExRicercaProfiloByKey(getRequestBigDecimalParameter("cod_prf"));

			setRequestAttribute("tit", "inserire");
			setRequestAttribute("titolo", "Inserisci");
			setRequestAttribute("utente", lUteMod);
			setRequestAttribute("utenti", lUtente);
			setRequestAttribute("profilo", lProfMod);
			return ROOT_DIR + "files/siap/sico/utente/ConfermaInserimentoUtente.jsp";
		}
	}

}