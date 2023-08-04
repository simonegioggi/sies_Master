package siap.siep.pagoPaBatch.action;

import java.math.BigDecimal;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.web.IWebConstants;
import siap.sico.web.ActionSiap;
import siap.siep.pagoPaBatch.controller.IBatchPagopa;
import siap.siep.pagoPaBatch.controller.IInvocazionePagopa;
import siap.siep.pagoPaBatch.model.BatchPagopaModel;
import siap.siep.util.SIEPLookupRemote;

public class ActLoadDettaglioEsecuzioneBatchPagoPa extends ActionSiap implements ICostantiBatchPagoPa 
{
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
	
	public String processRequest() throws Exception {
        siesLogger.debug("ActLoadDettaglioEsecuzioneBatchPagoPa...");
        
        BigDecimal lIdBatchPagoPa = getRequestBigDecimalParameter(ICostantiBatchPagoPa.CAMPO_ID_BATCH_PAGOPA);

        IBatchPagopa lCtrlBatch = SIEPLookupRemote.getBatchPagopaPagopaRemote();
        BatchPagopaModel lBatchModel = lCtrlBatch.ExRicercaBatchPagopaByKey(lIdBatchPagoPa);
        setRequestAttribute("BatchModel", lBatchModel);
        
        // Inserire paginazione
		String lPagina = "1";
		if (!isRequestParameterNullObj(IWebConstants.NUM_PAGE))
			lPagina = getRequestStringParameter(IWebConstants.NUM_PAGE);
		
		IInvocazionePagopa lCtrl = SIEPLookupRemote.getInvocazionePagopaRemote();
        // Recupero il numero totale di record
		BigDecimal lCountRisultati;
		if (isRequestParameterNullObj("CountRisultati")) {
			Vector lListaInvocazioni = lCtrl.ExRicercaInvocazioniByIdBatchPagopa (lIdBatchPagoPa, 0);
			lCountRisultati = new BigDecimal(lListaInvocazioni.size());
		} else {
			lCountRisultati = getRequestBigDecimalParameter("CountRisultati");
		}
        
        Vector lListaInvocazioni = lCtrl.ExRicercaInvocazioniByIdBatchPagopa (lIdBatchPagoPa, Integer.parseInt(lPagina));
        setRequestAttribute("ListaInvocazioni", lListaInvocazioni);

		// Passo i dati per gestire la paginazione
		setRequestAttribute("CountRisultati", lCountRisultati);
		setRequestAttribute(IWebConstants.NUM_PAGE, lPagina);
		setRequestAttribute(IWebConstants.REQUEST_FOR_PAGING, getCompleteRequestURL());
		
        
        return PG_LOAD_DETTAGLIO_ESECUZIONE_BATCH;
	}
}
