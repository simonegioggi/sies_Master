package siap.siep.pagoPaBatch.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.servlet.ServletContext;

import org.apache.log4j.Logger;
import org.quartz.CronTrigger;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.Trigger;
import org.quartz.Trigger.TriggerState;

import f3b.log.LogF3B;
import siap.siep.pagoPA.util.ConsultaPagamentiJob;
import siap.siep.pagoPaBatch.action.ICostantiBatchPagoPa;
import siap.siep.pagoPaBatch.model.QuartzJobModel;

public class BatchUtils {
    
    private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
    
    public QuartzJobModel getJobInformation(ServletContext servletContex) throws Exception {
        Scheduler scheduler = (Scheduler) servletContex.getAttribute("quartzScheduler");
        siesLogger.info("getSchedulerName = "+scheduler.getSchedulerName());
        
        JobKey keyCheckBollettini = new JobKey (ConsultaPagamentiJob.JOB_NAME,ConsultaPagamentiJob.JOB_GROUP);
        String jobName = keyCheckBollettini.getName();
        String jobGroup = keyCheckBollettini.getGroup();   

        JobDetail detail = scheduler.getJobDetail(keyCheckBollettini);     
        
        // Recupero i trigger associati al job (uno solo)
        List<? extends Trigger> triggers = (List<? extends Trigger>) scheduler.getTriggersOfJob(keyCheckBollettini);
        for (Trigger jobTrigger : triggers)
        {
            siesLogger.info("jobTrigger = "+jobTrigger.getKey());
        }
        
        String cronDescription="";
        String status="";
        Date startTime = null;
        Date prevFireTime = null;
        Date nextFireTime = null; 
        String startTimeString;
        String prevFireString;
        TriggerState state = null;

        if(triggers.size()>0) 
        {
          startTime    = triggers.get(0).getStartTime();
          prevFireTime = triggers.get(0).getPreviousFireTime();
          nextFireTime = triggers.get(0).getNextFireTime(); 
          state        = scheduler.getTriggerState(triggers.get(0).getKey());
          Trigger trig = triggers.get(0);
          
          siesLogger.debug("Trigger.startTime    = "+startTime); 
          siesLogger.debug("Trigger.prevFireTime = "+prevFireTime); 
          siesLogger.debug("Trigger.nextFireTime = "+nextFireTime); 
          siesLogger.debug("Trigger.state        = "+state);
          
          if(trig instanceof CronTrigger) 
          {
            CronTrigger cront = (CronTrigger) triggers.get(0);
            
            cronDescription = cront.getCronExpression();
            
            //Options options = new Options();
            //options.setTwentyFourHourTime(true);
            //net.redhogs.cronparser.Options
            //net.redhogs.cronparser.CronExpressionDescriptor
            //cronDescription = CronExpressionDescriptor.getDescription(cront.getCronExpression(),options, Locale.ITALIAN);
          }
          else 
          {    
              siesLogger.warn("Il trigger associato al job a' di tipo "+trig.getClass().getName());
          }
        }
        
        // Decodifico lo stato
        if(state!=null) 
        {
          if (state.equals(TriggerState.PAUSED)) status=ICostantiBatchPagoPa.STATO_INPAUSA; //"In Pausa";
          if (state.equals(TriggerState.ERROR))  status=ICostantiBatchPagoPa.STATO_ERRORE; //"Errore";
          if (state.equals(TriggerState.NORMAL)) status=ICostantiBatchPagoPa.STATO_SCHEDULATO; //"Schedulato";
        }        
        
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE dd MMMM yyyy - HH:mm",Locale.ITALIAN);
        if(nextFireTime!= null) startTimeString = sdf.format(nextFireTime);
        else startTimeString="";

        if(prevFireTime!=null) prevFireString = sdf.format(prevFireTime);
        else prevFireString = "Non presente";
        
        
        JobDataMap dataMap = detail.getJobDataMap();
        String esito = dataMap.getString("esito");
        // Misfired??
        
        QuartzJobModel quartzModel = new QuartzJobModel();
        quartzModel.setNomeDemone   (jobName);
        quartzModel.setGruppoDemone (jobGroup);
        quartzModel.setDescrizione  (detail.getDescription());
        
        quartzModel.setCronExpression(cronDescription);
        quartzModel.setLastExec(prevFireString);
        quartzModel.setNextSched(startTimeString);
        quartzModel.setStatus(status);
        quartzModel.setEsito(esito);
        
        return quartzModel;
    } 
}
