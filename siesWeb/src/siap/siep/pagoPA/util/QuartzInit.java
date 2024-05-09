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