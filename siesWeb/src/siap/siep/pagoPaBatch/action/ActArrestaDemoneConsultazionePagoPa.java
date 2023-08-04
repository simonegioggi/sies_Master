package siap.siep.pagoPaBatch.action;

import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.Trigger;

import f3b.log.LogF3B;
import f3b.web.IWebConstants;
import siap.sico.web.ActionSiap;
import siap.siep.pagoPA.util.ConsultaPagamentiJob;

public class ActArrestaDemoneConsultazionePagoPa extends ActionSiap implements ICostantiBatchPagoPa {
    private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

    public String processRequest() throws Exception {
        siesLogger.debug("ActArrestaDemoneConsultazionePagoPa...");
        
        // Recupero lo scheduler
        Scheduler scheduler = (Scheduler) getServletContext().getAttribute("quartzScheduler");
        siesLogger.info("getSchedulerName = "+scheduler.getSchedulerName());
        
        JobKey keyCheckBollettini = new JobKey (ConsultaPagamentiJob.JOB_NAME,ConsultaPagamentiJob.JOB_GROUP);
        
        // Posso mettere in pausa il job 
        //scheduler.pauseJob(keyCheckBollettini);
        
        // Oppure i trigger associati al job        
        List<? extends Trigger> trigList =scheduler.getTriggersOfJob(keyCheckBollettini);
        Iterator<? extends Trigger> trigIter = trigList.iterator();
        while(trigIter.hasNext()) 
        {
            scheduler.pauseTrigger(trigIter.next().getKey());
        }        
        
        String lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
                + "=siap.siep.pagoPaBatch.action.ActVisualizzaBatchPagoPa";

        return lPage;
    }
}
