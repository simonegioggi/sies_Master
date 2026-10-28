// 20261028-MEV_2026-105_INLAY - MEV Legge 140/2026: ricerca provvedimenti emessi ex art. 94 ter DPR 309/90
package siap.siep.scadenzario.action;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import siap.siep.scadenzario.model.ScadenzarioModel;
import siap.siep.statis.controller.StatisController;
import f3b.log.LogF3B;
import f3b.util.F3BException;
import f3b.web.IWebConstants;

/**
 * Export in formato xls dei provvedimenti emessi ex art. 94 ter DPR 309/90.
 */
public class ActReportExcelProvvedimenti94Ter extends ActProvvedimenti94TerBase {

	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public String processRequest() throws F3BException {
		ScadenzarioModel lScaMod = buildFiltro();
		Boolean lDefiniti = getDefiniti();

		HSSFWorkbook wb = new HSSFWorkbook();
		StatisController lStatisCtrl = new StatisController();
		lStatisCtrl.ExCreateReportProvvedimenti94Ter(lScaMod, getUfficioUtenteConnesso(), wb, lDefiniti,
				this.getCodUfficioUtenteConnesso());

		ByteArrayOutputStream fileOut = new ByteArrayOutputStream();
		try {
			wb.write(fileOut);
		} catch (IOException ioe) {
			siesLogger.error("IOException: " + ioe);
			throw new F3BException("siap.siep.scadenzario.action.ActReportExcelProvvedimenti94Ter.processRequest: "
					+ ioe);
		}

		setRequestAttribute("report", fileOut);
		setRequestAttribute(IWebConstants.DISPOSITION_FIELD, IWebConstants.ATTACHMENT_DISPOSITION_FILE);
		return IWebConstants.PG_DOWNLOAD_DOCUMENT;
	}
}
