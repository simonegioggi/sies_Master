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

public class ActAvviaDemoneConsultazionePagoPa extends ActionSiap implements ICostantiBatchPagoPa {
    private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

    public String processRequest() throws Exception {
        siesLogger.debug("ActAvviaDemoneConsultazionePagoPa...");
        
        // Recupero lo scheduler
        Scheduler scheduler = (Scheduler) getServletContext().getAttribute("quartzScheduler");

        JobKey keyCheckBollettini = new JobKey (ConsultaPagamentiJob.JOB_NAME,ConsultaPagamentiJob.JOB_GROUP);
        // scheduler.resumeJob(keyCheckBollettini);
        
        List<? extends Trigger> trigList =scheduler.getTriggersOfJob(keyCheckBollettini);
        Iterator<? extends Trigger> trigIter = trigList.iterator();
        while(trigIter.hasNext()) 
        {
            Trigger trigger = trigIter.next();
            scheduler.resumeTrigger(trigger.getKey());
            if (scheduler.getTriggerState(trigger.getKey()).equals(Trigger.TriggerState.ERROR)) {
                siesLogger.info("Resetto lo stato di errore...");
                scheduler.resetTriggerFromErrorState(trigger.getKey());
            }
       }
        
        String lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
                + "=siap.siep.pagoPaBatch.action.ActVisualizzaSchedulazioneBatchPagoPa";

        return lPage;
    }
}
