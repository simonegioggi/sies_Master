package siap.siep.pagoPaBatch.action;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import siap.sico.web.ActionSiap;
import siap.siep.pagoPaBatch.model.QuartzJobModel;
import siap.siep.pagoPaBatch.utils.BatchUtils;

public class ActVisualizzaSchedulazioneBatchPagoPa extends ActionSiap implements ICostantiBatchPagoPa{
    private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

    public String processRequest() throws Exception {
        siesLogger.debug("ActVisualizzaSchedulazioneBatchPagoPa...");
        
        BatchUtils batchUtils = new BatchUtils();
        QuartzJobModel quartzModel =batchUtils.getJobInformation (getServletContext());
        setRequestAttribute("ConsultaPagamentiJob", quartzModel);     
        
        
        return PG_LOAD_DETTAGLIO_SCHEDULAZIONE_BATCH_PAGOPA;        
    }
}
