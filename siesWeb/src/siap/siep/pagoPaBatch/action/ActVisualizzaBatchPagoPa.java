package siap.siep.pagoPaBatch.action;

import java.math.BigDecimal;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.web.IWebConstants;
import siap.sico.web.ActionSiap;
import siap.siep.pagoPaBatch.controller.IBatchPagopa;
import siap.siep.pagoPaBatch.model.BatchPagopaModel;
import siap.siep.util.SIEPLookupRemote;

public class ActVisualizzaBatchPagoPa extends ActionSiap implements ICostantiBatchPagoPa {

	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public String processRequest() throws Exception {

		siesLogger.debug("ActVisualizzaBatchPagoPa...");

		// recupero i dati dell'esecuzione degli ultimi 10 lanci del batch (BATCH_PAGOPA)

		BatchPagopaModel lCriteriRicerca = new BatchPagopaModel();

		// Gestire i criteri di ricerca. Per ora non previsti. Il Batch gira una volta al giorn
		// e nella form vengono caricati 20 record ogni pagina per cui si ha la situazione degli ultimi 20
		// giorni

		// Inserire paginazione
		String lPagina = "1";
		if (!isRequestParameterNullObj(IWebConstants.NUM_PAGE))
			lPagina = getRequestStringParameter(IWebConstants.NUM_PAGE);

		IBatchPagopa lCtrlBatch = SIEPLookupRemote.getBatchPagopaPagopaRemote();

		// Recupero il numero totale di record
		BigDecimal lCountRisultati;
		if (isRequestParameterNullObj("CountRisultati")) {
			Vector<BatchPagopaModel> listaLanci = lCtrlBatch.ExRecuperaLancioBatchPagopa(lCriteriRicerca, 0);
			lCountRisultati = new BigDecimal(listaLanci.size());
		} else {
			lCountRisultati = getRequestBigDecimalParameter("CountRisultati");
		}

		Vector<BatchPagopaModel> listaLanci = lCtrlBatch.ExRecuperaLancioBatchPagopa(lCriteriRicerca,
				Integer.parseInt(lPagina));
		setRequestAttribute("listaLanciJob", listaLanci);

		// Passo i dati per gestire la paginazione
		setRequestAttribute("CountRisultati", lCountRisultati);
		setRequestAttribute(IWebConstants.NUM_PAGE, lPagina);
		setRequestAttribute(IWebConstants.REQUEST_FOR_PAGING, getCompleteRequestURL());

		/*
		 * BatchUtils batchUtils = new BatchUtils(); QuartzJobModel quartzModel =batchUtils.getJobInformation
		 * (getServletContext()); setRequestAttribute("ConsultaPagamentiJob", quartzModel);
		 */

		return PG_LOAD_DETTAGLIO_BATCH_PAGOPA;
	}

}