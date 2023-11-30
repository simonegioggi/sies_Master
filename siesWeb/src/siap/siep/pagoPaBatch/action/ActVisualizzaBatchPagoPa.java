package siap.siep.pagoPaBatch.action;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.web.IWebConstants;
import siap.sico.web.ActionSiap;
import siap.siep.pagoPaBatch.controller.IBatchPagopa;
import siap.siep.pagoPaBatch.model.BatchPagopaModel;
import siap.siep.pagoPaBatch.model.CriteriRicercaBatchPagopaModel;
import siap.siep.util.SIEPLookupRemote;

public class ActVisualizzaBatchPagoPa extends ActionSiap implements ICostantiBatchPagoPa {

	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public String processRequest() throws Exception {

		siesLogger.debug("ActVisualizzaBatchPagoPa...");

		// recupero i dati dell'esecuzione degli ultimi 10 lanci del batch (BATCH_PAGOPA)
		CriteriRicercaBatchPagopaModel lCriteriRicerca = new CriteriRicercaBatchPagopaModel();

		// Data iscrizione Iniziale
		if (!isRequestParameterNullObj(CAMPO_GIORNO_ESECUZIONE_INIZIALE)
				&& !isRequestParameterNullObj(CAMPO_MESE_ESECUZIONE_INIZIALE)
				&& !isRequestParameterNullObj(CAMPO_ANNO_ESECUZIONE_INIZIALE)) {
			lCriteriRicerca.setDataInizioEsecuzioneDal(getRequestDateParameter(CAMPO_ANNO_ESECUZIONE_INIZIALE,
					CAMPO_MESE_ESECUZIONE_INIZIALE, CAMPO_GIORNO_ESECUZIONE_INIZIALE));

		}

		// Data iscrizione Finale
		if (!isRequestParameterNullObj(CAMPO_GIORNO_ESECUZIONE_FINALE)
				&& !isRequestParameterNullObj(CAMPO_MESE_ESECUZIONE_FINALE)
				&& !isRequestParameterNullObj(CAMPO_ANNO_ESECUZIONE_FINALE)) {
			lCriteriRicerca.setDataInizioEsecuzioneAl(getRequestDateParameter(CAMPO_ANNO_ESECUZIONE_FINALE,
					CAMPO_MESE_ESECUZIONE_FINALE, CAMPO_GIORNO_ESECUZIONE_FINALE));
		}

		if (isRequestParameterNullObj(CAMPO_GIORNO_ESECUZIONE_INIZIALE)) {
			// Provengo dal menu. Imposto la data Inizio a 2 mesi
			Date dataDal = new Date();
			dataDal = DateUtils.moveDateTo(dataDal, Calendar.MONTH, -2);
			lCriteriRicerca.setDataInizioEsecuzioneDal(dataDal);
		}

		// Gestire i criteri di ricerca. Per ora non previsti. Il Batch gira una volta al giorn
		// e nella form vengono caricati 20 record ogni pagina per cui si ha la situazione degli ultimi 20
		// giorni

		// Inserire paginazione
		String lPagina = "1";
		if (!isRequestParameterNullObj(IWebConstants.NUM_PAGE))
			lPagina = getRequestStringParameter(IWebConstants.NUM_PAGE);

		IBatchPagopa lCtrlBatch = SIEPLookupRemote.getBatchPagopaPagopaRemote();

		// Recupero il numero totale di record
		BigDecimal lCountRisultati;
		if (isRequestParameterNullObj("CountRisultati")) {
			Vector<BatchPagopaModel> listaLanci = lCtrlBatch.ExRecuperaLancioBatchPagopa(lCriteriRicerca, 0);
			lCountRisultati = new BigDecimal(listaLanci.size());
		} else {
			lCountRisultati = getRequestBigDecimalParameter("CountRisultati");
		}

		Vector<BatchPagopaModel> listaLanci = lCtrlBatch.ExRecuperaLancioBatchPagopa(lCriteriRicerca,
				Integer.parseInt(lPagina));
		setRequestAttribute("listaLanciJob", listaLanci);

		setRequestAttribute("CriteriRicerca", lCriteriRicerca);

		// Passo i dati per gestire la paginazione
		setRequestAttribute("CountRisultati", lCountRisultati);
		setRequestAttribute(IWebConstants.NUM_PAGE, lPagina);
		setRequestAttribute(IWebConstants.REQUEST_FOR_PAGING, getCompleteRequestURL());

		return PG_LOAD_DETTAGLIO_BATCH_PAGOPA;
	}

}