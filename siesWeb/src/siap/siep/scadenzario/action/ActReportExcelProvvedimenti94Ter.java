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
				this.getCodUfficioUtenteConnesso(), buildRiepilogoFiltri()); // 20261102-MEV_2026-105_INLAY

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

	// 20261102-MEV_2026-105_INLAY: riepilogo dei filtri impostati nella form di ricerca
	private java.util.List<String[]> buildRiepilogoFiltri() throws F3BException {
		java.util.List<String[]> l = new java.util.ArrayList<String[]>();
		l.add(new String[] { "Procedimenti da (Anno/Numero)", coppia(CAMPO_CHIAVE_ANNO_INIZIALE, CAMPO_CHIAVE_PROGR_INIZIALE) });
		l.add(new String[] { "Procedimenti a (Anno/Numero)", coppia(CAMPO_CHIAVE_ANNO_FINALE, CAMPO_CHIAVE_PROGR_FINALE) });
		l.add(new String[] { "Data emissione iniziale", data("Giorno", "Mese", "Anno", "Iniziale") });
		l.add(new String[] { "Data emissione finale", data("Giorno", "Mese", "Anno", "Finale") });
		StringBuilder tipi = new StringBuilder();
		String[] lSel = getRequestStringParameters(CAMPO_TIPO_PROVVEDIMENTO);
		for (int i = 0; lSel != null && i < lSel.length; i++) {
			String d = "Tutti";
			if ("0760".equals(lSel[i])) d = "OE con sospensione Libero";
			else if ("0761".equals(lSel[i])) d = "OE con sospensione Custodia Cautelare Altra Causa AD";
			else if ("0762".equals(lSel[i])) d = "OE con sospensione Custodia Cautelare Questa Causa AD";
			tipi.append(tipi.length() > 0 ? "; " : "").append(d);
		}
		l.add(new String[] { "Tipologia provvedimento", tipi.length() > 0 ? tipi.toString() : "Tutti" });
		l.add(new String[] { "Estrazione anche procedimenti Definiti", getDefiniti() ? "Si" : "No" });
		return l;
	}

	private String coppia(String aAnno, String aNum) throws F3BException {
		String lA = getRequestStringParameter(aAnno);
		String lN = getRequestStringParameter(aNum);
		return (lA != null && lA.trim().length() > 0) ? lA.trim() + " / " + (lN == null ? "" : lN.trim()) : "-";
	}

	private String data(String aG, String aM, String aA, String aSfx) throws F3BException {
		String lG = getRequestStringParameter(aG + "Emissione" + aSfx);
		if (lG == null || lG.trim().length() == 0) return "-";
		return lG.trim() + "/" + getRequestStringParameter(aM + "Emissione" + aSfx) + "/"
				+ getRequestStringParameter(aA + "Emissione" + aSfx);
	}
}
