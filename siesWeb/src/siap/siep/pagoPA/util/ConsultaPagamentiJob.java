package siap.siep.pagoPA.util;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.apache.log4j.MDC;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.F3BProperties;
import it.giustizia.www.serviziTelematici.serviziGenerici.RisultatoRicerca;
import it.giustizia.www.serviziTelematici.serviziGenerici.ServiziConsultazionePagamentiTelematici;
import it.giustizia.www.serviziTelematici.serviziGenerici.ServiziConsultazionePagamentiTelematiciBeanServiceLocator;
import it.giustizia.www.serviziTelematici.serviziGenerici.StatoRichiestaPagamento;
import siap.sico.utente.model.UtenteModel;
import siap.siep.pagoPA.action.ICostantiPagoPA;
import siap.siep.pagoPA.controller.IBollettinoPagopa;
import siap.siep.pagoPA.model.BollettinoPagopaModel;
import siap.siep.pagoPaBatch.controller.IBatchPagopa;
import siap.siep.pagoPaBatch.controller.IInvocazionePagopa;
import siap.siep.pagoPaBatch.model.BatchPagopaModel;
import siap.siep.pagoPaBatch.model.BollettinoBatchPagopaModel;
import siap.siep.pagoPaBatch.model.InvocazionePagopaModel;
import siap.siep.util.SIEPLookupRemote;

/**
 * Il job preleva dalla tabella BOLLETTINO_PAGOPA i bollettini non ancora pagati (DATA_AVV_PAGAMENTO null o
 * STATO_PAGAMENTO = PN) e per ogni bollettini egffettua lì'interrogazione al servizio "elencopagamenti" di
 * pagoPA.
 *
 * Viene aggiornata BOLLETTINO_PAGOPA con la data ultimo controllo e la eventuale risposta XML
 *
 * @author d.fiorletta
 * @since MEV_2023-13
 * @version 1.0
 */
public class ConsultaPagamentiJob implements Job {

	public static final String JOB_NAME = "ConsultaPagamentiJob";
	public static final String JOB_GROUP = "PagoPAGroup";
	public static final String JOB_DESC = "Demone responsabile della consultazione dello stato dei pagamento su PagoPA";

	public static final String JOB_DESC_COL = "xxxxxxxxxxxxxxx";

	private static Logger pagoPaLogger = Logger.getLogger(LogF3B.PAGO_PA_LOG);

	// Lo scheduler ad ogni lancio crea una nuova istanza della classe invocamdo il costruttore senza
	// parametri
	// MEV_2023-33
	public ConsultaPagamentiJob() {
	}

	 public void execute(JobExecutionContext arg0) throws JobExecutionException {

	    MDC.put("utente", "BATCH_PAGOPA");
	    MDC.put("ufficio", "DISTRETTUALE");

	    pagoPaLogger.debug("===================================================");
	    pagoPaLogger.debug(" Avvio job di PagoPA - ConsultazionePagamenti      ");
	    pagoPaLogger.debug("===================================================");
	    pagoPaLogger.debug("");
	    
	    IBatchPagopa lCtrlBatch = null;

	    try {
	      UtenteModel lUtente = new UtenteModel();
	      lUtente.setUserId("BATCH");
	      lCtrlBatch = SIEPLookupRemote.getBatchPagopaPagopaRemote();
	      lCtrlBatch.ExLancioBatchPagopa(lUtente);
	    } catch (Exception e) {
	      pagoPaLogger.error("Eccezione in fase di lancio del job ",e);
	    } finally {
	      pagoPaLogger.debug("===================================================");
	      pagoPaLogger.debug(" job di PagoPA Terminato                           ");
	      pagoPaLogger.debug("===================================================");
	      pagoPaLogger.debug("");	      
	    }   
	 }
}