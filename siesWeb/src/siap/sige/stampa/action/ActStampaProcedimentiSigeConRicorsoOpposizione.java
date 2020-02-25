package siap.sige.stampa.action;

import java.io.ByteArrayOutputStream;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
import siap.sico.utente.model.UtenteModel;
import siap.sige.fascicolo.controller.IFascicoloSige;
import siap.sige.fascicolo.model.FascicoloSigeEstesoModel;
import siap.sige.fascicolo.model.RicercaFascicoloSigeModel;
import siap.sige.stampa.controller.IStampaSige;
import siap.sige.util.SIGELookupRemote;
import siap.sige.web.ActionSige;

/**
 * MEV_65: aggiunta classe Action per la visualizzazione della Stampa dei Procedimenti Sige Con Ricorso od
 * Opposizione.
 * 
 * @author Gioggi
 * @version 1.0
 */
public class ActStampaProcedimentiSigeConRicorsoOpposizione extends ActionSige {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public String processRequest() throws Exception {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("" + getClass().getName() + " .processRequest: inizio.");

		// Viene istanziato il controller per la ricerca
		IFascicoloSige ifs = SIGELookupRemote.getFascicoloSigeRemote();
		// recupero oggetti in sessione
		RicercaFascicoloSigeModel rfsm = (RicercaFascicoloSigeModel) getSessionAttribute("modelRO");
		// Ricerca Procedimenti Sige Con Ricorso/Opposizione
		Vector<FascicoloSigeEstesoModel> fascicoli = ifs.ExRicercaProcedimentiSigeConRicorsoOpposizione(rfsm,
				0);
		UtenteModel um = getUtenteConnesso();

		String s = IWebConstants.PG_DOWNLOAD;

		try {
			IStampaSige iss = SIGELookupRemote.getStampaRemote();
			ByteArrayOutputStream report = iss.ExStampaProcedimentiSigeConRicorsoOpposizione(fascicoli, rfsm,
					um);
			setRequestAttribute("report", report);
		} catch (F3BException ex) {
			siesLogger.debug(" * * * F3BException " + ex);
			s = IWebConstants.PG_MESSAGE;
			setRequestAttribute(IWebConstants.MESSAGE_TEXT,
					"Nessun procedimento nell'intervallo selezionato!");
		}

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(getClass().getName() + " .processRequest: fine.");

		// stampa elenco procedimenti
		return s;
	}

}