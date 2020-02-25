package siap.sius.statistiche.action;

//import java.math.BigDecimal;
import java.io.ByteArrayOutputStream;
import java.util.Date;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.web.IWebConstants;
import siap.sico.web.ActionSiap;
//import siap.sius.statistiche.model.EveFasGepSogProvModel;
import siap.sius.statistiche.model.RicercaProcedimentoModel;
import siap.sius.statistiche.util.excel.ProcAggregatiPerIstitutoDetenzioneExcel;

public class ActRicercaProcAggregatiPerIstitutoDetenzioneExcel extends ActionSiap
		implements ICostantiStatistiche {
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public String processRequest() throws Exception {

		ByteArrayOutputStream lFileOut = null;
		Date lDataIniziale = null, lDataFinale = null;
		RicercaProcedimentoModel lRicercaModel = null;
		// IStatisticheSius lCtrl = null;
		// Collection<EveFasGepSogDetModel> lElenco;

		lDataIniziale = getRequestDateParameter(CAMPO_ANNO_INIZIALE, CAMPO_MESE_INIZIALE,
				CAMPO_GIORNO_INIZIALE);
		lDataFinale = getRequestDateParameter(CAMPO_ANNO_FINALE, CAMPO_MESE_FINALE, CAMPO_GIORNO_FINALE);

		lRicercaModel = new RicercaProcedimentoModel();
		lRicercaModel.setDataIscrizioneInizio(lDataIniziale);
		lRicercaModel.setDataIscrizioneFine(lDataFinale);
		lRicercaModel.setUtenteConnesso(getUtenteConnesso());

		// lCtrl = SIUSLookupRemote.getStatisticheSiusRemote();
		// lElenco = lCtrl.ExRicercaProcSoggettiIstitutiDetenzione(lRicercaModel);
		/*
		 * StatisController lStatis = new StatisController();
		 * 
		 * lFileOut = lStatis.creaExcelProcAggrIstitutiDetenzione(lRicercaModel);
		 */

		ProcAggregatiPerIstitutoDetenzioneExcel lExcel = new ProcAggregatiPerIstitutoDetenzioneExcel();
		lFileOut = lExcel.creaExcelProcAggrIstitutiDetenzione(lRicercaModel);

		setRequestAttribute("report", lFileOut);
		setRequestAttribute(IWebConstants.DISPOSITION_FIELD, IWebConstants.ATTACHMENT_DISPOSITION_FILE);

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(getClass().getName() + " .processRequest: fine ");

		return IWebConstants.PG_DOWNLOAD_DOCUMENT;
	}

}