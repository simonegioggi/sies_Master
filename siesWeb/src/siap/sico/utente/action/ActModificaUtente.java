package siap.sico.utente.action;

/**
* <p>Title: ActModificaUtente</p>
* <p>Description: Classe Action per la modifica di Utente</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.util.Vector;

import org.apache.log4j.Logger;

import siap.sico.profilo.controller.IProfilo;
import siap.sico.profilo.model.ProfiloModel;
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.utente.controller.IUtente;
import siap.sico.utente.model.UtenteModel;
import siap.sico.utente.model.UtenteViewModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import f3b.log.LogF3B;
import f3b.security.model.ProfileModel;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;

@SuppressWarnings("rawtypes")
public class ActModificaUtente extends ActionSiap implements ICostantiUtente {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	/**
	 * Azione di Modifica del Utente
	 * 
	 * @return Nome della pagina JSP da visualizzare al termine dell'elaborazione
	 * @throws F3BException
	 */
	public String processRequest() throws F3BException {

		Vector lUtenti = null;

		// Riempie il model dell'utente con i dati modificati/modificabili
		UtenteModel lUteMod = new UtenteModel();
		lUteMod.setUserId(getRequestStringParameter(CAMPO_COD_UTENTE));
		lUteMod.setCognome(getRequestStringParameter(CAMPO_COGNOME));
		lUteMod.setNome(getRequestStringParameter(CAMPO_NOME));
		lUteMod.setTelefono(getRequestStringParameter(CAMPO_TELEFONO));
		lUteMod.setFax(getRequestStringParameter(CAMPO_FAX));
		lUteMod.setEmail(getRequestStringParameter(CAMPO_E_MAIL));
		lUteMod.setDataFineValidita(getRequestDateParameter(CAMPO_ANNO_DATA_FINE_VALIDITA,
				CAMPO_MESE_DATA_FINE_VALIDITA, CAMPO_GIORNO_DATA_FINE_VALIDITA));
		lUteMod.setCodOperatoreAggiornamento(this.getCodUtenteConnesso());
		lUteMod.setDataAggiornamento(DateUtils.getSysDate());

		// Ufficio Utente (non modificabile)
		UfficioModel lUffMod = new UfficioModel();
		lUffMod.setCodUfficio(getRequestStringParameter("cod_uff"));
		lUteMod.setUfficioUtente(lUffMod);

		// Profilo Model (modificabile)
		ProfileModel lprf = new ProfileModel();
		lprf.setProfileId(getRequestBigDecimalParameter("cod_prf"));
		lUteMod.setUserProfile(lprf);

		// Verifica se esistono omonimi per l'utente nell'ufficio di appartenenza
		IUtente lCtrl = SICOLookupRemote.getUtenteRemote();
		if (getRequestStringParameter("flag").equals("no")) {
			lUtenti = lCtrl.ExRicercaUtentePerUfficioCognomeNome(getRequestStringParameter("cod_uff"),
					getRequestStringParameter(CAMPO_COGNOME), getRequestStringParameter(CAMPO_NOME));
		}

		// =======================================================================
		// Tolgo dalla lista degli omonimi l'utente corrente. Se non ho modificato
		// Cognome e/o Nome infatti l'utente corrente sicuramente figura tra gli
		// omonimi
		// =======================================================================
		if (lUtenti != null && lUtenti.size() > 0) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Ricerco utente corrente tra gli omonimi");
			for (int i = 0; i < lUtenti.size(); i++) {
				UtenteViewModel lUteModRic = (UtenteViewModel) lUtenti.elementAt(i);

				if (lUteModRic != null && lUteModRic.getUserId().equals(lUteMod.getUserId())) {
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger.debug("Utente corrente trovato lo elimino dalla lista");
					lUtenti.remove(i);
					break;
				}

			}
		}

		// Non esistono Omonimi o provengo dalla form di conferma dopo aver visualizzato
		// gli eventuali omonimi
		if (getRequestStringParameter("flag").equals("si") || lUtenti == null || lUtenti.size() == 0) {
			UtenteModel llUteModRet = lCtrl.ExModificaUtente(lUteMod);

			String lPage = "";
			lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
					+ "=siap.sico.utente.action.ActLoadDettaglioUtente&" + CAMPO_COD_UTENTE + "="
					+ llUteModRet.getUserId().toString();
			return lPage;
		} else {
			// Sono presenti omonimi, recupero il profilo
			IProfilo lProf = SICOLookupRemote.getProfiloRemote();
			ProfiloModel lProfMod = lProf.ExRicercaProfiloByKey(getRequestBigDecimalParameter("cod_prf"));

			setRequestAttribute("tit", "modificare");
			setRequestAttribute("titolo", "Modifica");
			setRequestAttribute("utente", lUteMod);
			setRequestAttribute("utenti", lUtenti);
			setRequestAttribute("profilo", lProfMod);
			return ROOT_DIR + "files/siap/sico/utente/ConfermaInserimentoUtente.jsp";
		}
	}

}