package siap.sius.statistiche.action;

import java.io.ByteArrayOutputStream;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.web.IWebConstants;
import siap.sius.ActionSius;
import siap.sius.statistiche.model.RicercaProcedimentoModel;
import siap.sius.statistiche.util.excel.ProcPerStatisticaMisureAlternativeExcel;

/**
 * Title: ActRicercaStatisticaMisureAlter678c1tercppExcel 
 * Description: Classe Action per la ricerca statistica per procedimenti MA Art.678c1tercpp
 *
 * @version 1.0
 */
// MEV_9: aggiunta classe per le statistiche
public class ActRicercaStatisticaMisureAlter678c1tercppExcel extends ActionSius
		implements ICostantiStatistiche {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public String processRequest() throws Exception {

		RicercaProcedimentoModel rpm = null;
		ByteArrayOutputStream baos = null;

		rpm = (RicercaProcedimentoModel) getSessionAttribute("ricercaProcedimenti");

		ProcPerStatisticaMisureAlternativeExcel excel = new ProcPerStatisticaMisureAlternativeExcel();
		baos = excel.creaFoglioProcPerStatisticaMisureAlternative(getUfficioUtenteConnesso(), rpm);

		setRequestAttribute("report", baos);
		setRequestAttribute(IWebConstants.DISPOSITION_FIELD, IWebConstants.ATTACHMENT_DISPOSITION_FILE);

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(getClass().getName() + " .processRequest: fine ");

		return IWebConstants.PG_DOWNLOAD_DOCUMENT;
	}

}