package siap.siep.pagoPA.util;

import java.util.Collection;
import java.util.Properties;

import javax.servlet.http.HttpServlet;

import org.apache.log4j.Logger;
import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerFactory;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.quartz.impl.StdSchedulerFactory;

import f3b.log.LogF3B;
import f3b.util.F3BProperties;

public class QuartzInit extends HttpServlet {

	private static final long serialVersionUID = 5802375882783657607L;

	private static Logger pagoPaLogger = Logger.getLogger(LogF3B.PAGO_PA_LOG);

	public void init() {

		pagoPaLogger.info(getClass().getName() + ".init()");
		pagoPaLogger.info("");
		pagoPaLogger.info("===================================================");
		pagoPaLogger.info("Inizializzazione schedulazione batch pagoPA  MEM ");

		try {
			String lPagoPaSchedulerEnabled = F3BProperties.getProperty("PagoPaSchedulerEnabled");
			pagoPaLogger.info("PagoPaSchedulerEnabled = " + lPagoPaSchedulerEnabled);

			if ("true".equals(lPagoPaSchedulerEnabled)) {
				pagoPaLogger.info("Procedo ad attivare la schedulazione" + lPagoPaSchedulerEnabled);

				SchedulerFactory sf = new StdSchedulerFactory();
				Scheduler scheduler = sf.getScheduler();

				JobKey keyCheckBollettini = new JobKey(ConsultaPagamentiJob.JOB_NAME,
						ConsultaPagamentiJob.JOB_GROUP);

				// verifico se il job è già stato schedulato
				boolean esisteCheckBollettini = scheduler.checkExists(keyCheckBollettini);
				if (!esisteCheckBollettini) {
					pagoPaLogger.info("Demone delle chiamate a PagoPa non ancora creato, lo creo adesso");
					JobDataMap dataMap = new JobDataMap();
					dataMap.put("DescCol", ConsultaPagamentiJob.JOB_DESC_COL);
					// aggiungere al dataMap eventuali parametri di inizializzazione del JOB

					JobDetail job = JobBuilder.newJob(ConsultaPagamentiJob.class)
							.withIdentity(keyCheckBollettini).withDescription(ConsultaPagamentiJob.JOB_DESC)
							.usingJobData(dataMap).storeDurably().build();

					String lPagoPaCronExpression = F3BProperties.getProperty("PagoPaCronExpression");
					pagoPaLogger.info("F3b.properties: PagoPaCronExpression = " + lPagoPaCronExpression);

					// Creo un nuovo trigger
					TriggerKey trigKey = new TriggerKey("Default_Trigger_Name", "Default_Trigger_Group");
					Trigger trigger = TriggerBuilder.newTrigger().withIdentity(trigKey)
							.withSchedule(CronScheduleBuilder.cronSchedule(lPagoPaCronExpression)).build();

					scheduler.scheduleJob(job, trigger);

					// Blocco la scedulazione nel caso in cui al primo avvio la voglio configurare da
					// interfaccia grafica
					// scheduler.unscheduleJob(trigKey);
				} else {
					pagoPaLogger.info("JOB PagoPa già presente");
				}

				pagoPaLogger.info(" Avvio dello schedulatore...");
				scheduler.start();
				pagoPaLogger.info(" Schedulatore avviato");
				pagoPaLogger.info("");
				pagoPaLogger.info("===================================================");

			} else {
				pagoPaLogger.warn("Schedulazione PagoPaSchedulerEnabled non attiva.");
				pagoPaLogger.info("===================================================");
			}
		} catch (Exception e) {
			pagoPaLogger.error("Errore in fase di inizializzazione di quartz: ", e);
		}
	}

	public void destroy() {

		pagoPaLogger.debug(getClass().getName() + ".destroy()");
		// try {
		// SchedulerFactory sf = new StdSchedulerFactory();
		// Scheduler sched = sf.getScheduler();
		// } catch (Exception e) {
		// siesLogger.error("Errore in fase di inizializzazione di quartz: ",e);
		// }
	}
	
	/**
	 * Versione dell'init per attivare la persistenza sul DB dei job 
	 */
    //public void initQuartzPersistance() {
    public void initDB() {    
        pagoPaLogger.info(getClass().getName() + ".init()");
        pagoPaLogger.info("");
        pagoPaLogger.info("===================================================");
        pagoPaLogger.info("Inizializzazione schedulazione batch pagoPA DB ");
       
        
        try {
            Properties qrtzProp = QuartzProperties.getInstance().getProps();
            
            pagoPaLogger.info("qrtzProp = "+qrtzProp);
            pagoPaLogger.info("instanceName = "+qrtzProp.getProperty("org.quartz.scheduler.instanceName"));
            
            String lPagoPaSchedulerEnabled = F3BProperties.getProperty("PagoPaSchedulerEnabled");
            pagoPaLogger.info("PagoPaSchedulerEnabled = "+lPagoPaSchedulerEnabled); 
            
            if ("true".equals(lPagoPaSchedulerEnabled)) {
                pagoPaLogger.info("Procedo ad attivare la schedulazione"+lPagoPaSchedulerEnabled); 
            
                //SchedulerFactory sf = new StdSchedulerFactory();
                SchedulerFactory sf = new StdSchedulerFactory(qrtzProp);
                //SchedulerFactory sf = new StdSchedulerFactory("D:\\SIES_2020\\config\\quartzDF.properties");
                
                Scheduler scheduler = sf.getScheduler();
                //Scheduler scheduler = sf.getScheduler("SIESScheduler"); // NON FUNZIONE
                
                Collection <Scheduler> listaScheduler = sf.getAllSchedulers();
                for (Scheduler sch : listaScheduler) {
                    pagoPaLogger.info("getSchedulerName = "+sch.getSchedulerName());
                }
                
                pagoPaLogger.info("getSchedulerName = "+scheduler.getSchedulerName());
                
                JobKey keyCheckBollettini = new JobKey (ConsultaPagamentiJob.JOB_NAME,ConsultaPagamentiJob.JOB_GROUP);
                
                // verifico se il job è già stato schedulato 
                boolean esisteCheckBollettini = scheduler.checkExists(keyCheckBollettini);
                pagoPaLogger.info("esisteCheckBollettini = "+esisteCheckBollettini);
                if(!esisteCheckBollettini) 
                {
                    pagoPaLogger.info("Demone delle chiamate a PagoPa non ancora creato, lo creo adesso");
                    JobDataMap dataMap = new JobDataMap();
                    dataMap.put("DescCol", ConsultaPagamentiJob.JOB_DESC_COL);
                    // aggiungere al dataMap eventuali parametri di inizializzazione del JOB 
                    
                    JobDetail job = JobBuilder.newJob(ConsultaPagamentiJob.class)
                                              .withIdentity (keyCheckBollettini)
                                              .withDescription (ConsultaPagamentiJob.JOB_DESC)
                                              .usingJobData(dataMap)
                                              .storeDurably() // indica allo scheduler di non rimuover il job se non ha trigger associati
                                              .build();                
                    
                    String lPagoPaCronExpression = F3BProperties.getInstance().getProperty("PagoPaCronExpression");
                    pagoPaLogger.info("F3b.properties: PagoPaCronExpression = "+lPagoPaCronExpression);
    
                    // Creo un nuovo trigger
                    TriggerKey trigKey = new TriggerKey("Default_Trigger_Name","Default_Trigger_Group");
                    Trigger trigger = TriggerBuilder.newTrigger()
                                                    .withIdentity(trigKey)
                                                    .withSchedule(CronScheduleBuilder.cronSchedule(lPagoPaCronExpression)
                                                            .withMisfireHandlingInstructionDoNothing())
                                                    .build();
                    
                    scheduler.scheduleJob(job,trigger);
                    
                    //Blocco la schedulazione nel caso in cui al primo avvio la voglio configurare da interfaccia grafica
                    //scheduler.unscheduleJob(trigKey);
                }
                else {
                    pagoPaLogger.info("JOB PagoPa già presente");
                }   
                
                
                getServletContext().setAttribute("quartzScheduler",scheduler);
                
                pagoPaLogger.info(" Avvio dello schedulatore...");
                scheduler.start();
                pagoPaLogger.info(" Schedulatore avviato");
                pagoPaLogger.info("");
                pagoPaLogger.info("===================================================");
            
            }
            else {
                pagoPaLogger.warn("Schedulazione PagoPaSchedulerEnabled non attiva.");  
                pagoPaLogger.info("===================================================");
            }
        } catch (Exception e) {
            pagoPaLogger.error("Errore in fase di inizializzazione di quartz: ",e);
        }
    }    	

}