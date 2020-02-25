package siap.sius.statistiche.action;

import java.io.ByteArrayOutputStream;
import java.util.Date;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.web.IWebConstants;
import siap.sico.web.ActionSiap;
import siap.sius.statistiche.model.RicercaProcedimentoModel;
import siap.sius.statistiche.util.excel.ProcDlgs123_2018Excel;

public class ActRicercaProcDlgs123_2018Excel extends ActionSiap implements ICostantiStatistiche {

	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public String processRequest() throws Exception {

		ByteArrayOutputStream lFileOut = null;
		Date lDataIniziale = null, lDataFinale = null;
		RicercaProcedimentoModel lRicercaModel = null;

		lDataIniziale = getRequestDateParameter(CAMPO_ANNO_INIZIALE, CAMPO_MESE_INIZIALE,
				CAMPO_GIORNO_INIZIALE);
		lDataFinale = getRequestDateParameter(CAMPO_ANNO_FINALE, CAMPO_MESE_FINALE, CAMPO_GIORNO_FINALE);

		lRicercaModel = new RicercaProcedimentoModel();
		lRicercaModel.setDataIscrizioneInizio(lDataIniziale);
		lRicercaModel.setDataIscrizioneFine(lDataFinale);
		lRicercaModel.setUtenteConnesso(getUtenteConnesso());

		ProcDlgs123_2018Excel lExcel = new ProcDlgs123_2018Excel();
		lFileOut = lExcel.creaExcelProcProcDlgs123_2018(getUfficioUtenteConnesso(), lRicercaModel);

		setRequestAttribute("report", lFileOut);
		setRequestAttribute(IWebConstants.DISPOSITION_FIELD, IWebConstants.ATTACHMENT_DISPOSITION_FILE);

		siesLogger.debug(getClass().getName() + " .processRequest: fine ");

		return IWebConstants.PG_DOWNLOAD_DOCUMENT;
	}

}