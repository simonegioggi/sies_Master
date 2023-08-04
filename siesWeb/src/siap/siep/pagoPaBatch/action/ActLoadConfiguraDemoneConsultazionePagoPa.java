package siap.siep.pagoPaBatch.action;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.F3BProperties;
import siap.sico.web.ActionSiap;
import siap.siep.pagoPaBatch.model.QuartzJobModel;
import siap.siep.pagoPaBatch.utils.BatchUtils;

public class ActLoadConfiguraDemoneConsultazionePagoPa extends ActionSiap implements ICostantiBatchPagoPa {
    private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
    
    public String processRequest() throws Exception {
        
        BatchUtils batchUtils = new BatchUtils();
        
        QuartzJobModel quartzModel =batchUtils.getJobInformation (getServletContext());
        setRequestAttribute("ConsultaPagamentiJob", quartzModel);
        
        String lPagoPaCronDebugEnabled = F3BProperties.getProperty("PagoPaCronDebugEnabled");
        setRequestAttribute("PagoPaCronDebugEnabled", lPagoPaCronDebugEnabled);
        
        return PG_LOAD_CONFIGURA_BATCH_PAGOPA;
    }
}
