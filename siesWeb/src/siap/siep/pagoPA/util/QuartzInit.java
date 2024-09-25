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
import siap.sico.dna.PulisciDNAJob;

public class QuartzInit extends HttpServlet {

	private static final long serialVersionUID = 5802375882783657607L;

	private static Logger pagoPaLogger = Logger.getLogger(LogF3B.PAGO_PA_LOG);
	
	// MEV_2024-DNA
	private static Logger dnaLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public void init() {

		String lCronPersistanceType = "MEM";

		try {
			lCronPersistanceType = F3BProperties.getProperty("CronPersistanceType");
			// info per il log
			pagoPaLogger.info("lCronPersistanceType = " + lCronPersistanceType);
		} catch (Exception e) {
			// info per il log
			pagoPaLogger.error("Errore in fase di inizializzazione di quartz: ", e);
		}

		if ("DB".equals(lCronPersistanceType))
			initDBPersistance();
		else
			initInMemory();
	}

	public void initInMemory() {

		// info per il log
		pagoPaLogger.info(getClass().getName() + ".initInMemory()");
		pagoPaLogger.info("");
		pagoPaLogger.info("===================================================");
		pagoPaLogger.info("Inizializzazione schedulazione batch pagoPA  MEM ");

		try {
			String lPagoPaSchedulerEnabled = F3BProperties.getProperty("PagoPaSchedulerEnabled");
			pagoPaLogger.info("PagoPaSchedulerEnabled = " + lPagoPaSchedulerEnabled);

			// Si trimma per sicurezza. Pare che su linux non trimmi in automatica le properties
			// Su windows legge invece fino al primo spazio (che trimma)
			if (lPagoPaSchedulerEnabled != null)
				lPagoPaSchedulerEnabled = lPagoPaSchedulerEnabled.trim();

			if ("true".equals(lPagoPaSchedulerEnabled)) {
				// info per il log
				pagoPaLogger.info("Procedo ad attivare la schedulazione" + lPagoPaSchedulerEnabled);

				SchedulerFactory sf = new StdSchedulerFactory();
				Scheduler scheduler = sf.getScheduler();

				JobKey keyCheckBollettini = new JobKey(ConsultaPagamentiJob.JOB_NAME,
						ConsultaPagamentiJob.JOB_GROUP);

				// verifico se il job è già stato schedulato
				boolean esisteCheckBollettini = scheduler.checkExists(keyCheckBollettini);
				if (!esisteCheckBollettini) {
					// info per il log
					pagoPaLogger.info("Demone delle chiamate a PagoPa non ancora creato, lo creo adesso");
					JobDataMap dataMap = new JobDataMap();
					dataMap.put("DescCol", ConsultaPagamentiJob.JOB_DESC_COL);
					// aggiungere al dataMap eventuali parametri di inizializzazione del JOB

					JobDetail job = JobBuilder.newJob(ConsultaPagamentiJob.class)
							.withIdentity(keyCheckBollettini).withDescription(ConsultaPagamentiJob.JOB_DESC)
							.usingJobData(dataMap).storeDurably().build();
					String lPagoPaCronExpression = F3BProperties.getProperty("PagoPaCronExpression");
					// info per il log
					pagoPaLogger.info("F3b.properties: PagoPaCronExpression = " + lPagoPaCronExpression);

					// Creo un nuovo trigger
					TriggerKey trigKey = new TriggerKey("Default_Trigger_Name", "Default_Trigger_Group");
					Trigger trigger = TriggerBuilder.newTrigger().withIdentity(trigKey)
							.withSchedule(CronScheduleBuilder.cronSchedule(lPagoPaCronExpression)).build();
					scheduler.scheduleJob(job, trigger);
				} else {
					// info per il log
					pagoPaLogger.info("JOB PagoPa già presente");
				}
				
				getServletContext().setAttribute("quartzScheduler", scheduler);
				
				// info per il log
				pagoPaLogger.info(" Avvio dello schedulatore...");
				scheduler.start();
				pagoPaLogger.info(" Schedulatore avviato");
				pagoPaLogger.info("");
				pagoPaLogger.info("===================================================");
			} else {
				// info per il log
				pagoPaLogger.warn("Schedulazione PagoPaSchedulerEnabled non attiva.");
				pagoPaLogger.info("===================================================");
			}
		} catch (Exception e) {
			// info per il log
			pagoPaLogger.error("Errore in fase di inizializzazione della memoria di quartz: ", e);
		}
		
    
    //  MEV_2024-DNA
    // ======================================================================================
    //  NUOVO JOB per la cancellazione delle attivita DNA		
		//
		// Per la schedulazione del job verificare il contenuto del file f3b.properties
		// # Nuovo JOB di pulizia DNA
		// DNASchedulerEnabled=true
		// DNACronExpression=0 0/1 * * * ?		
    // ======================================================================================
      try {
        String lDNASchedulerEnabled = F3BProperties.getProperty("DNASchedulerEnabled");
        dnaLogger.info("DNASchedulerEnabled = " + lDNASchedulerEnabled);

        // Si trimma per sicurezza. Pare che su linux non trimma in automatico le properties
        // Su windows legge invece fino al primo spazio (che trimma)
        if (lDNASchedulerEnabled != null)
          lDNASchedulerEnabled = lDNASchedulerEnabled.trim();

        if ("true".equals(lDNASchedulerEnabled)) {
          // info per il log 
          dnaLogger.info("Procedo ad attivare la schedulazione " + lDNASchedulerEnabled);

          SchedulerFactory sf = new StdSchedulerFactory();
          Scheduler scheduler = sf.getScheduler();  // ?????? Verificare se utilizzare quello di prima

          JobKey keyPulisciDNA = new JobKey (PulisciDNAJob.JOB_NAME, PulisciDNAJob.JOB_GROUP);
          
          // verifico se il job è già stato schedulato
          boolean esistePulisciDNA = scheduler.checkExists(keyPulisciDNA);
          if (!esistePulisciDNA) {
            // info per il log
            dnaLogger.info("Demone Pulisci DNA non ancora creato, lo creo adesso");
            JobDataMap dataMap = new JobDataMap();
            dataMap.put("DescCol", PulisciDNAJob.JOB_DESC_COL);
            // aggiungere al dataMap eventuali parametri di inizializzazione del JOB

            JobDetail job = JobBuilder.newJob(PulisciDNAJob.class)
                .withIdentity(keyPulisciDNA).withDescription(PulisciDNAJob.JOB_DESC)
                .usingJobData(dataMap).storeDurably().build();
                
            String lDNACronExpression = F3BProperties.getProperty("DNACronExpression");

            dnaLogger.info("F3b.properties: DNACronExpression = " + lDNACronExpression);

            // Creo un nuovo trigger
            TriggerKey trigKey = new TriggerKey("Trigger_DNA", "Trigger_Group_DNA");
            Trigger trigger = TriggerBuilder.newTrigger().withIdentity(trigKey)
                .withSchedule(CronScheduleBuilder.cronSchedule(lDNACronExpression)).build();
            scheduler.scheduleJob(job, trigger);
          } else {
            dnaLogger.info("JOB Pilusci DNA gia'' presente");
          }
          dnaLogger.info(" Avvio dello schedulatore DNA...");
          scheduler.start();
          dnaLogger.info(" Schedulatore DNA avviato");
          dnaLogger.info("");
          dnaLogger.info("===================================================");  
        } else {
          dnaLogger.warn("Schedulazione PuliscDNA non attiva.");
          dnaLogger.info("===================================================");
        }
      } catch (Exception e) {
        dnaLogger.error("Errore in fase di inizializzazione della memoria di quartz per il JOB DNA: ", e);
      }		
	}

	public void destroy() {

		// info per il log
		pagoPaLogger.debug(getClass().getName() + ".destroy()");
	}

	/**
	 * Versione dell'init per attivare la persistenza sul DB dei job Per utilizzare questo metodo dev essere
	 * presente il file quart.properties nella cartella config con i parametri per l'accesso al DB
	 */
	public void initDBPersistance() {

		// info per il log
		pagoPaLogger.info(getClass().getName() + ".initDBPersistance()");
		pagoPaLogger.info("");
		pagoPaLogger.info("===================================================");
		pagoPaLogger.info("Inizializzazione schedulazione batch pagoPA DB ");

		try {
			Properties qrtzProp = QuartzProperties.getInstance().getProps();

			// info per il log
			pagoPaLogger.info("qrtzProp = " + qrtzProp);
			pagoPaLogger.info("instanceName = " + qrtzProp.getProperty("org.quartz.scheduler.instanceName"));

			String lPagoPaSchedulerEnabled = F3BProperties.getProperty("PagoPaSchedulerEnabled");
			// info per il log
			pagoPaLogger.info("PagoPaSchedulerEnabled = " + lPagoPaSchedulerEnabled);

			if ("true".equals(lPagoPaSchedulerEnabled)) {
				// info per il log
				pagoPaLogger.info("Procedo ad attivare la schedulazione" + lPagoPaSchedulerEnabled);
				SchedulerFactory sf = new StdSchedulerFactory(qrtzProp);
				Scheduler scheduler = sf.getScheduler();
				Collection<Scheduler> listaScheduler = sf.getAllSchedulers();
				for (Scheduler sch : listaScheduler) {
					pagoPaLogger.info("getSchedulerName = " + sch.getSchedulerName());
				}

				// info per il log
				pagoPaLogger.info("getSchedulerName = " + scheduler.getSchedulerName());

				JobKey keyCheckBollettini = new JobKey(ConsultaPagamentiJob.JOB_NAME,
						ConsultaPagamentiJob.JOB_GROUP);

				// verifico se il job è già stato schedulato
				boolean esisteCheckBollettini = scheduler.checkExists(keyCheckBollettini);
				// info per il log
				pagoPaLogger.info("esisteCheckBollettini = " + esisteCheckBollettini);
				if (!esisteCheckBollettini) {
					// info per il log
					pagoPaLogger.info("Demone delle chiamate a PagoPa non ancora creato, lo creo adesso");
					JobDataMap dataMap = new JobDataMap();
					dataMap.put("DescCol", ConsultaPagamentiJob.JOB_DESC_COL);
					// aggiungere al dataMap eventuali parametri di inizializzazione del JOB

					JobDetail job = JobBuilder.newJob(ConsultaPagamentiJob.class)
							.withIdentity(keyCheckBollettini).withDescription(ConsultaPagamentiJob.JOB_DESC)
							.usingJobData(dataMap).storeDurably() // indica allo scheduler di non rimuover il
																	// job se non ha trigger associati
							.build();

					String lPagoPaCronExpression = F3BProperties.getProperty("PagoPaCronExpression");
					// info per il log
					pagoPaLogger.info("F3b.properties: PagoPaCronExpression = " + lPagoPaCronExpression);

					// Creo un nuovo trigger
					TriggerKey trigKey = new TriggerKey("Default_Trigger_Name", "Default_Trigger_Group");
					Trigger trigger = TriggerBuilder.newTrigger().withIdentity(trigKey)
							.withSchedule(CronScheduleBuilder.cronSchedule(lPagoPaCronExpression)
									.withMisfireHandlingInstructionDoNothing())
							.build();

					scheduler.scheduleJob(job, trigger);
				} else {
					// info per il log
					pagoPaLogger.info("JOB PagoPa già presente");
				}

				getServletContext().setAttribute("quartzScheduler", scheduler);

				// info per il log
				pagoPaLogger.info(" Avvio dello schedulatore...");
				scheduler.start();
				pagoPaLogger.info(" Schedulatore avviato");
				pagoPaLogger.info("");
				pagoPaLogger.info("===================================================");
			} else {
				// info per il log
				pagoPaLogger.warn("Schedulazione PagoPaSchedulerEnabled non attiva.");
				pagoPaLogger.info("===================================================");
			}
		} catch (Exception e) {
			// info per il log
			pagoPaLogger.error("Errore in fase di inizializzazione di quartz: ", e);
		}
	}

}