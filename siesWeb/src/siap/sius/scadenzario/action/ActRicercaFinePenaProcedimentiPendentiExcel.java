package siap.sius.scadenzario.action;

import java.io.ByteArrayOutputStream;
import java.util.List;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.web.IWebConstants;
import siap.sico.web.ActionSiap;

/**
 * ActRicercaFinePenaProcedimentiPendentiExcel - Classe Action per la stampa in Excel della ricerca fine pena
 * procedimenti pendenti
 *
 * @author sgioggi
 * @since MEV_2026-1
 * @ver
 */
public class ActRicercaFinePenaProcedimentiPendentiExcel extends ActionSiap
		implements ICostantiScadenzarioSius {

	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	@SuppressWarnings("unchecked")
	public String processRequest() throws Exception {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(getClass().getName() + ".processRequest : inizio");

		// recupero info dalla sessione
		List<Object> l = (List<Object>) getSessionAttribute("ricercaFinePenaProcedimentiPendenti");

		RicercaFinePenaProcedimentiPendentiExcel rfpppe = new RicercaFinePenaProcedimentiPendentiExcel();
		ByteArrayOutputStream baos = rfpppe.stampaExcel(getUfficioUtenteConnesso(), l);

		setRequestAttribute("report", baos);
		setRequestAttribute(IWebConstants.DISPOSITION_FIELD, IWebConstants.ATTACHMENT_DISPOSITION_FILE);

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(getClass().getName() + ".processRequest : fine");

		return IWebConstants.PG_DOWNLOAD_DOCUMENT;
	}

}