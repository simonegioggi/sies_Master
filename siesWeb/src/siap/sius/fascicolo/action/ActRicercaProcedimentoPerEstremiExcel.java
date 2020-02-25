package siap.sius.fascicolo.action;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.web.IWebConstants;
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.web.ActionSiap;
import siap.sius.cancassfascsius.model.CancAssFascSiusModel;
import siap.sius.fascicolo.controller.IFascicoloSius;
import siap.sius.fascicolo.model.FascicoloSiusModel;
import siap.sius.util.SIUSLookupRemote;

public class ActRicercaProcedimentoPerEstremiExcel extends ActionSiap implements ICostantiFascicoloSius {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	@SuppressWarnings("unchecked")
	public String processRequest() throws Exception {

		HashMap<String, Object> lParams = (HashMap<String, Object>) getSessionAttribute("parametri_ricerca");

		UfficioModel lUfficio = getUfficioUtenteConnesso();

		String lFiltroCollaboratore = null;
		lFiltroCollaboratore = (String) lParams.get("filtroCollaboratore");

		// si leggono le condizioni di filtro dalla session.
		CancAssFascSiusModel lCancAssFasc = null;
		lCancAssFasc = (CancAssFascSiusModel) lParams.get("cancAssFasc");

		String lCodAtto = null;
		lCodAtto = (String) lParams.get("codAtto");

		// si recupera dalla session il fascicoloSiusModel valorizzato, non serve poichè è già in questo
		// model.
		FascicoloSiusModel lFSiusMod = null;
		if (!isSessionAttributeNullObj("fSmod"))
			lFSiusMod = (FascicoloSiusModel) getSessionAttribute("fSmod");

		// =====================================================================================================

		IFascicoloSius fSCtrl = SIUSLookupRemote.getFascicoloSiusRemote();
		ByteArrayOutputStream fileOut = fSCtrl.ExReportFascicoloSiusByEstremiXLS(lFSiusMod, lCodAtto,
				lCancAssFasc, lFiltroCollaboratore, lUfficio, lParams); // aggiungere lParams, necessario per
																		// recuperare i parametri di filtro

		setRequestAttribute("report", fileOut);
		setRequestAttribute(IWebConstants.DISPOSITION_FIELD, IWebConstants.ATTACHMENT_DISPOSITION_FILE);
		// setRequestAttribute(IWebConstants.DISPOSITION_FIELD, IWebConstants.INLINE_DISPOSITION_FILE );

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("" + getClass().getName() + " .processRequest: fine ");

		return IWebConstants.PG_DOWNLOAD_DOCUMENT;
	}

}