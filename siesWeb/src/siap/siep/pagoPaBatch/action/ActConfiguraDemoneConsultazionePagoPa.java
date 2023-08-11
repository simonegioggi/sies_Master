package siap.siep.pagoPaBatch.action;

import java.util.List;

import org.apache.log4j.Logger;
import org.quartz.CronScheduleBuilder;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;

import f3b.log.LogF3B;
import f3b.util.F3BProperties;
import f3b.web.IWebConstants;
import siap.sico.web.ActionSiap;
import siap.siep.pagoPA.util.ConsultaPagamentiJob;

public class ActConfiguraDemoneConsultazionePagoPa extends ActionSiap implements ICostantiBatchPagoPa {

	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public String processRequest() throws Exception {

		siesLogger.debug("ActConfiguraDemoneConsultazionePagoPa...");

		// TODO - Implementare la configurazione del demone
		String secondi = "0";
		String minuti = "0";
		String orario = "0"; // this.getRequestStringParameter(ICostantiBatchPagoPa.CAMPO_ORA);
		String giorno = "*"; // this.getRequestStringParameter(ICostantiBatchPagoPa.CAMPO_GIORNO);
		String mese = "*"; // Per ora fisso
		String giornoSettimana = "?"; // this.getRequestStringParameter(ICostantiBatchPagoPa.GIORNO_SETTIMANA);

		if (!"".equals(getRequestStringParameter(ICostantiBatchPagoPa.CAMPO_MINUTI)))
			minuti = this.getRequestStringParameter(ICostantiBatchPagoPa.CAMPO_MINUTI);

		if (!"".equals(getRequestStringParameter(ICostantiBatchPagoPa.CAMPO_ORA)))
			orario = this.getRequestStringParameter(ICostantiBatchPagoPa.CAMPO_ORA);

		if (!"".equals(getRequestStringParameter(ICostantiBatchPagoPa.CAMPO_GIORNO)))
			giorno = this.getRequestStringParameter(ICostantiBatchPagoPa.CAMPO_GIORNO);

		if (!isRequestParameterNullEmptyObj(ICostantiBatchPagoPa.CAMPO_GG_SETT))
			giornoSettimana = this.getRequestStringParameter(ICostantiBatchPagoPa.CAMPO_GG_SETT);

		// ATTENZIONE - Non è possibile specificare sia il campo giorno del mese e giorno della settimana
		// uno dei 2 campi DEVE valere ? se l
		// giornoSettimana = this.getRequestStringParameter(ICostantiBatchPagoPa.GIORNO_SETTIMANA);
		if (!"?".equals(giornoSettimana))
			giorno = "?";
		// Il formato della schedulazione a 6 campi
		// secondi = fisso a 0 non modificabile
		// minuti =
		// ora =
		// giorno del mese = 1-31 oppure L = ultimo giorno del mese
		// Mese = fisso a *
		// Giorno della settimana

		String nuovaSchedulazione = secondi + " " + minuti + " " + orario + " " + giorno + " " + mese + " "
				+ giornoSettimana;
		siesLogger.debug("nuovaSchedulazione = " + nuovaSchedulazione);

		// In debug
		if (!isRequestParameterNullEmptyObj(ICostantiBatchPagoPa.CAMPO_CHECK_CRON_EXPR))
			nuovaSchedulazione = this.getRequestStringParameter(ICostantiBatchPagoPa.CAMPO_CRON_EXPR);

		// Testo prima la cron exp
		CronScheduleBuilder schedulerBuilder = null;
		try {
			schedulerBuilder = CronScheduleBuilder.cronSchedule(nuovaSchedulazione)
					.withMisfireHandlingInstructionDoNothing();
		} catch (Exception e) {
			siesLogger
					.error("Espressione non valida = " + e.getMessage() + " - " + e.getCause().getMessage());
			throw e;
		}

		// nuovaSchedulazione = "0 0/5 * * * ?";
		// =============================================
		Scheduler scheduler = (Scheduler) getServletContext().getAttribute("quartzScheduler");
		siesLogger.info("getSchedulerName = " + scheduler.getSchedulerName());

		JobKey keyCheckBollettini = new JobKey(ConsultaPagamentiJob.JOB_NAME, ConsultaPagamentiJob.JOB_GROUP);
		JobDetail myJob = scheduler.getJobDetail(keyCheckBollettini);

		List<? extends Trigger> triggers = scheduler.getTriggersOfJob(keyCheckBollettini);
		if (triggers.size() > 0) {
			// Devo deschedulare per poter ricreare il trigger con lo stesso nome (forse)
			scheduler.unscheduleJob(triggers.get(0).getKey());
		}
		scheduler.deleteJob(keyCheckBollettini);

		siesLogger.info("Creo il nuovo trigger...");
		TriggerKey trigKey = new TriggerKey("Default_Trigger_Name", "Default_Trigger_Group");
		Trigger newTrigger = TriggerBuilder.newTrigger().withIdentity(trigKey).withSchedule(schedulerBuilder)
				.build();

		if (scheduler.getTriggerState(trigKey).equals(Trigger.TriggerState.ERROR)) {
			siesLogger.info("Resetto lo stato di errore...");
			scheduler.resetTriggerFromErrorState(trigKey);
		}
		scheduler.scheduleJob(myJob, newTrigger);
		scheduler.start();

		String lPagoPaCronDebugEnabled = F3BProperties.getProperty("PagoPaCronDebugEnabled");
		setRequestAttribute("PagoPaCronDebugEnabled", lPagoPaCronDebugEnabled);

		String lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
				+ "=siap.siep.pagoPaBatch.action.ActVisualizzaSchedulazioneBatchPagoPa";

		return lPage;
	}

}