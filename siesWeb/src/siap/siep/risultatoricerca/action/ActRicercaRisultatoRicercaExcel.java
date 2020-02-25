package siap.siep.risultatoricerca.action;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.web.IWebConstants;
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.web.ActionSiap;
import siap.siep.risultatoricerca.controller.IRisultatoRicerca;
import siap.siep.util.SIEPLookupRemote;

public class ActRicercaRisultatoRicercaExcel extends ActionSiap implements ICostantiRisultatoRicerca {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	@SuppressWarnings("unchecked")
	public String processRequest() throws Exception {

		HashMap<String, Object> lParams = (HashMap<String, Object>) getSessionAttribute("parametri_ricerca");

		UfficioModel lUfficio = getUfficioUtenteConnesso();

		String lIdRisultatoRicerca = getRequestStringParameter("IdRisultatoRicerca");

		IRisultatoRicerca lCtrl = SIEPLookupRemote.getRisultatoRicercaRemote();
		ByteArrayOutputStream fileOut = lCtrl.ExReportRicercaByIdExcel(lIdRisultatoRicerca, lUfficio,
				lParams); // aggiungere lParams, necessario per recuperare i parametri di filtro

		setRequestAttribute("report", fileOut);
		setRequestAttribute(IWebConstants.DISPOSITION_FIELD, IWebConstants.ATTACHMENT_DISPOSITION_FILE);

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("" + getClass().getName() + " .processRequest: fine ");

		return IWebConstants.PG_DOWNLOAD_DOCUMENT;
	}

}