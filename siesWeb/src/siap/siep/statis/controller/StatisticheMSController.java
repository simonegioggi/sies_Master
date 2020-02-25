package siap.siep.statis.controller;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFPalette;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.CellReference;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.util.CellRangeAddress;

import f3b.controller.GenericController;
import f3b.dao.DAOException;
import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.util.StringUtils;
import siap.sico.ufficio.model.UfficioModel;
import siap.siep.statis.action.ICostantiStatis;
import siap.siep.statis.dao.IspProvvedimentiSqlDAO;
import siap.siep.statis.dao.StatistichMSStoreProcedureDAO;
import siap.siep.statis.dao.StatisticheMSSqlDAO;
import siap.siep.statis.dao.StatoFascicoloResDAO;
import siap.siep.statis.dao.StatoFascicoloResMSDAO;
import siap.siep.statis.model.IspProvvedimentiModel;
import siap.siep.statis.model.StatisticheMSModel;
import siap.siep.statis.model.StatoFascicoloResModel;

/**
 * MEV_39
 * <p>
 * Title: StatisticheMSController
 * </p>
 * <p>
 * Description: Controller per le statistiche MS sulle Classi IV e VII
 * </p>
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class StatisticheMSController extends GenericController {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public Vector<StatisticheMSModel> ricercaRiepilogoIscrizioniProcedimentiMisura(String dataIniziale,
			String dataFinale, String numeroTrimestreSemestre, String ufficioConnesso) throws Exception {

		Connection c = null;
		Vector<StatisticheMSModel> v = new Vector<>();
		StatisticheMSSqlDAO smsd = null;

		String range = calcolaRange(dataIniziale, dataFinale);

		try {
			c = getDBConnection();
			smsd = new StatisticheMSSqlDAO(c);
			smsd.ricercaRiepilogoIscrizioniProcedimentiMisura(dataIniziale, dataFinale,
					numeroTrimestreSemestre, ufficioConnesso, range);
			smsd.start();
			while (smsd.next())
				v.add((StatisticheMSModel) smsd.getModelIscrizioni(range));
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new DAOException(
					"StatisticheMSController.ricercaRiepilogoIscrizioniProcedimentiMisura: Non posso leggere : "
							+ daoEx);
		} finally {
			cleanup(smsd);
			cleanup(c);
		}

		// valore di ritorno
		return v;
	}

	public void creaRiepilogoIscrizioniProcedimentiMisura(Vector<StatisticheMSModel> v, HSSFWorkbook wb,
			UfficioModel ufficioUtenteConnesso, String dataIniziale, String dataFinale, String descrComune) {

		if (v != null && !v.isEmpty()) {
			HSSFCellStyle csNull = wb.createCellStyle();

			// stile per celle col bordo
			HSSFCellStyle cs = getBordo4Lati(wb);

			// stile per celle col bordo con carattere grassetto
			HSSFCellStyle csBold = getBordo4Lati(wb);
			// Create a new font and alter it.
			HSSFFont font = wb.createFont();
			font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
			csBold.setFont(font);

			HSSFCellStyle csBoldCenter = getBordo4Lati(wb);
			csBoldCenter.setFont(font);
			csBoldCenter.setAlignment(HSSFCellStyle.ALIGN_CENTER);

			// primo foglio; RIEPILOGO_ISCRIZIONI
			HSSFSheet sheet = wb.createSheet("Riepilogo Iscrizioni");
			sheet.setColumnWidth(0, (75 * 256));

			int nRow = 0;

			// Intestazione del foglio excel
			nRow = setIntestazione(sheet, ufficioUtenteConnesso, csNull, descrComune);

			nRow++;
			nRow++;

			HSSFRow row = sheet.createRow(nRow);
			setCell(row, 0,
					"Riepilogo Iscrizioni relativo al periodo dal " + dataIniziale + " al " + dataFinale,
					csNull);

			nRow++;
			nRow++;

			// Tipologia tempi
			String tipologia = "RIEPILOGO ISCRIZIONI MISURE DI SICUREZZA";
			int colonnaIniziale = 0;

			// creazione riga con altezza per wraptext
			row = sheet.getRow(nRow);
			if (row == null)
				row = sheet.createRow(nRow);
			csBoldCenter.setWrapText(true);
			setCell(row, colonnaIniziale, tipologia, csBoldCenter);
			for (int a = 0; a < v.get(0).getAnni().size(); a++) {
				colonnaIniziale++;
				setCell(row, colonnaIniziale, v.get(0).getAnni().get(a).intValue(), csBoldCenter);
			}

			int nRowAnno = nRow;
			int nColAnno = 0;

			// inizio ciclo di scrittura dei dati
			Iterator itx = v.iterator();
			while (itx.hasNext()) {
				StatisticheMSModel smsm = (StatisticheMSModel) itx.next();

				int jump = 0;
				nColAnno = 0;
				// ciclo per scrittura colonne
				for (int i = 0; i < smsm.getAnni().size(); i++) {
					nColAnno++;
					if (jump == 0)
						nRow++;
					// scrittura tipologia e valore
					row = sheet.getRow(nRow);
					if (row == null)
						row = sheet.createRow(nRow);
					setCell(row, 0, smsm.getTipoMS(), cs);
					setCell(row, nColAnno, smsm.getIscrittiParziali().get(i).doubleValue(), cs);
					jump += 1;
				}
			}

			nRow++;
			// scrittura totale anno
			row = sheet.getRow(nRow);
			if (row == null)
				row = sheet.createRow(nRow);
			setCell(row, 0, "Totali Iscritti nel Periodo", csBoldCenter);
			int colonnaFinale = 1;
			for (int a = 0; a < v.get(0).getAnni().size(); a++) {
				String formula = getStringaSomma(nRowAnno + 1, colonnaFinale, nRow - 1, colonnaFinale);
				setFormulaCell(row, colonnaFinale, formula, csBoldCenter);
				colonnaFinale++;
			}

			// fuori ciclo
			// Richiamo metodo per la scrittura delle formule contenenti
			// le somme parziali e generali
			nColAnno++;
			nRow = setTotaliTempi(sheet, csBold, csBoldCenter, nRow, nRowAnno, nColAnno);
			nRow++;
		}
	}

	private HSSFCellStyle getBordo4Lati(HSSFWorkbook wb) {

		HSSFCellStyle cs = wb.createCellStyle();
		cs.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		cs.setBorderTop(HSSFCellStyle.BORDER_THIN);
		cs.setBorderRight(HSSFCellStyle.BORDER_THIN);
		cs.setBorderLeft(HSSFCellStyle.BORDER_THIN);

		return cs;
	}

	private HSSFCell setCell(HSSFRow row, int nCol, double value, HSSFCellStyle cs) {

		HSSFCell cell = row.createCell(nCol);
		cell.setCellValue(value);
		cell.setCellStyle(cs);

		return cell;
	}

	private HSSFCell setCell(HSSFRow row, int nCol, String value, HSSFCellStyle cs) {

		HSSFCell cell = row.createCell(nCol);
		cell.setCellValue(value);
		cell.setCellStyle(cs);

		return cell;
	}

	private int setIntestazione(HSSFSheet sheet, UfficioModel ufficioUtenteConnesso, HSSFCellStyle csNull,
			String DescUffIntesta) {

		int nRow = 0;

		// Create a row and put some cells in it. Rows are 0 based.
		HSSFRow row = sheet.createRow(nRow);
		// Create a cell and put a value in it.
		String value = (ufficioUtenteConnesso.getDescrTipoUfficio().toUpperCase() + " DI "
				+ DescUffIntesta.toUpperCase());
		setCell(row, 0, value, csNull);

		nRow++;
		// Create a row and put some cells in it. Rows are 0 based.
		row = sheet.createRow(nRow);
		// Create a cell and put a value in it.

		value = ("Tel. " + ufficioUtenteConnesso.getTelefono() + " - Fax " + ufficioUtenteConnesso.getFax());
		setCell(row, 0, value, csNull);

		return nRow;
	}

	private String getStringaSomma(int nRow1, int nCol1, int nRow2, int nCol2) {

		CellReference cellRef1 = new CellReference(nRow1, nCol1);
		CellReference cellRef2 = new CellReference(nRow2, nCol2);
		String formula = "SUM(" + cellRef1.formatAsString() + ":" + cellRef2.formatAsString() + ")";

		return formula;
	}

	private HSSFCell setFormulaCell(HSSFRow row, int nCol, String value, HSSFCellStyle cs) {

		HSSFCell cell = row.createCell(nCol);
		// cell.setCellType(HSSFCell.CELL_TYPE_FORMULA);
		cell.setCellFormula(value);
		cell.setCellStyle(cs);

		return cell;
	}

	private int setTotaliTempi(HSSFSheet sheet, HSSFCellStyle csBold, HSSFCellStyle csBoldCenter, int nRow,
			int nRowAnno, int nColAnno) {

		HSSFRow row = null;
		HSSFCell cell = null;
		String somma = "";
		int forRow = 0;

		// Ciclo per scrivere il totale per tipologia per tutti gli anni
		for (forRow = nRowAnno; forRow < nRow + 1; forRow++) {
			// row = sheet.createRow(forRow);
			row = sheet.getRow(forRow);
			if (row == null)
				row = sheet.createRow(forRow);
			cell = row.createCell(nColAnno);
			cell.setCellStyle(csBold);
			if (forRow == nRowAnno) {
				setCell(row, nColAnno, "Totali", csBoldCenter);
			} else {
				somma = getStringaSomma(forRow, 1, forRow, nColAnno - 1);
				setFormulaCell(row, nColAnno, somma, csBoldCenter);
			}
		}

		// valore di ritorno
		return nRow;
	}

	public Vector<StatisticheMSModel> ricercaRiepilogoIscrizioniTipologiaMisura(String dataIniziale,
			String dataFinale, String numeroTrimestreSemestre, String ufficioConnesso) throws Exception {

		Connection c = null;
		Vector<StatisticheMSModel> v = new Vector<>();
		StatisticheMSSqlDAO smsd = null;

		String range = calcolaRange(dataIniziale, dataFinale);

		try {
			c = getDBConnection();
			smsd = new StatisticheMSSqlDAO(c);
			smsd.ricercaRiepilogoIscrizioniTipologiaMisura(dataIniziale, dataFinale, numeroTrimestreSemestre,
					ufficioConnesso, range);
			smsd.start();
			while (smsd.next())
				v.add((StatisticheMSModel) smsd.getModelIscrizioni(range));
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new DAOException(
					"StatisticheMSController.ricercaRiepilogoIscrizioniTipologiaMisura: Non posso leggere : "
							+ daoEx);
		} finally {
			cleanup(smsd);
			cleanup(c);
		}

		// valore di ritorno
		return v;
	}

	private String calcolaRange(String dataIniziale, String dataFinale) {

		String range = "";
		int annoIniziale = new Integer(dataIniziale.substring(6)).intValue();
		int annoFinale = new Integer(dataFinale.substring(6)).intValue();
		range += annoIniziale;
		if (annoIniziale != annoFinale) {
			for (int a = annoIniziale + 1; a <= annoFinale; a++)
				range += "," + a;
		}

		// valore di ritorno
		return range;
	}

	public void creaRiepilogoIscrizioniTipologiaMisura(Vector<StatisticheMSModel> v, HSSFWorkbook wb,
			UfficioModel ufficioUtenteConnesso, String dataIniziale, String dataFinale, String descrComune) {

		if (v != null && !v.isEmpty()) {
			HSSFCellStyle csNull = wb.createCellStyle();

			// stile per celle col bordo
			HSSFCellStyle cs = getBordo4Lati(wb);

			// stile per celle col bordo con carattere grassetto
			HSSFCellStyle csBold = getBordo4Lati(wb);
			// Create a new font and alter it.
			HSSFFont font = wb.createFont();
			font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
			csBold.setFont(font);

			HSSFCellStyle csBoldCenter = getBordo4Lati(wb);
			csBoldCenter.setFont(font);
			csBoldCenter.setAlignment(HSSFCellStyle.ALIGN_CENTER);

			// primo foglio; RIEPILOGO_ISCRIZIONI
			HSSFSheet sheet = wb.createSheet("Tipologia MS");
			sheet.setColumnWidth(0, (75 * 256));

			int nRow = 0;

			// Intestazione del foglio excel
			nRow = setIntestazione(sheet, ufficioUtenteConnesso, csNull, descrComune);

			nRow++;
			nRow++;

			HSSFRow row = sheet.createRow(nRow);
			setCell(row, 0,
					"Riepilogo Iscrizioni relativo al periodo dal " + dataIniziale + " al " + dataFinale,
					csNull);

			nRow++;
			nRow++;

			// Tipologia tempi
			String tipologia = "TIPO MISURA DI SICUREZZA";
			int colonnaIniziale = 0;

			// creazione riga con altezza per wraptext
			row = sheet.getRow(nRow);
			if (row == null)
				row = sheet.createRow(nRow);
			csBoldCenter.setWrapText(true);
			setCell(row, colonnaIniziale, tipologia, csBoldCenter);
			for (int a = 0; a < v.get(0).getAnni().size(); a++) {
				colonnaIniziale++;
				setCell(row, colonnaIniziale, v.get(0).getAnni().get(a).intValue(), csBoldCenter);
			}

			int nRowAnno = nRow;
			int nColAnno = 0;

			// inizio ciclo di scrittura dei dati
			Iterator itx = v.iterator();
			while (itx.hasNext()) {
				StatisticheMSModel smsm = (StatisticheMSModel) itx.next();

				int jump = 0;
				nColAnno = 0;
				// ciclo per scrittura colonne
				for (int i = 0; i < smsm.getAnni().size(); i++) {
					nColAnno++;
					if (jump == 0)
						nRow++;
					// scrittura tipologia e valore
					row = sheet.getRow(nRow);
					if (row == null)
						row = sheet.createRow(nRow);
					setCell(row, 0, smsm.getTipoMS(), cs);
					setCell(row, nColAnno, smsm.getIscrittiParziali().get(i).doubleValue(), cs);
					jump += 1;
				}
			}

			nRow++;
			// scrittura totale anno
			row = sheet.getRow(nRow);
			if (row == null)
				row = sheet.createRow(nRow);
			setCell(row, 0, "Totali Iscritti nel Periodo", csBoldCenter);
			int colonnaFinale = 1;
			for (int a = 0; a < v.get(0).getAnni().size(); a++) {
				String formula = getStringaSomma(nRowAnno + 1, colonnaFinale, nRow - 1, colonnaFinale);
				setFormulaCell(row, colonnaFinale, formula, csBoldCenter);
				colonnaFinale++;
			}

			// fuori ciclo
			// Richiamo metodo per la scrittura delle formule contenenti
			// le somme parziali e generali
			nColAnno++;
			nRow = setTotaliTempi(sheet, csBold, csBoldCenter, nRow, nRowAnno, nColAnno);
			nRow++;
		}
	}

	public Vector<StatisticheMSModel> ricercaDettaglioProcedimentiMSTipologiaIscrizione(String dataIniziale,
			String dataFinale, String numeroTrimestreSemestre, String ufficioConnesso) throws Exception {

		Connection c = null;
		Vector<StatisticheMSModel> v = new Vector<>();
		StatisticheMSSqlDAO smsd = null;

		String range = calcolaRange(dataIniziale, dataFinale);

		try {
			c = getDBConnection();
			smsd = new StatisticheMSSqlDAO(c);
			smsd.ricercaDettaglioProcedimentiMSTipologiaIscrizione(dataIniziale, dataFinale,
					numeroTrimestreSemestre, ufficioConnesso, range);
			smsd.start();
			while (smsd.next())
				v.add((StatisticheMSModel) smsd.getModelDettaglio());
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new DAOException(
					"StatisticheMSController.ricercaDettaglioProcedimentiMSTipologiaIscrizione: Non posso leggere : "
							+ daoEx);
		} finally {
			cleanup(smsd);
			cleanup(c);
		}

		// valore di ritorno
		return v;
	}

	public void creaDettaglioProcedimentiMSTipologiaIscrizione(Vector<StatisticheMSModel> v, HSSFWorkbook wb,
			UfficioModel ufficioUtenteConnesso, String dataIniziale, String dataFinale, String descrComune) {

		if (v != null && !v.isEmpty()) {
			// Stile della cella vuoto
			HSSFCellStyle csNull = wb.createCellStyle();
			// Stile della cella grassetto
			HSSFCellStyle csBold = wb.createCellStyle();
			// Create a new font and alter it
			HSSFFont fontBold = wb.createFont();
			fontBold.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
			csBold.setFont(fontBold);

			// Stile della cella con bordi ed allineamento a destra
			HSSFCellStyle csC = getBordo4Lati(wb);
			csC.setAlignment(HSSFCellStyle.ALIGN_CENTER);

			// stile per celle col bordo con carattere grassetto
			// ALLINEATO AL CENTRO
			HSSFCellStyle csBoldCenter = getBordo4Lati(wb);
			csBoldCenter.setFont(fontBold);
			csBoldCenter.setAlignment(HSSFCellStyle.ALIGN_CENTER);

			// Stile Titolo 1
			HSSFCellStyle csTitolo1 = getBordo4Lati(wb);

			csTitolo1.setAlignment(HSSFCellStyle.ALIGN_CENTER);
			csTitolo1.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
			csTitolo1.setFillForegroundColor(HSSFColor.GREEN.index);
			csTitolo1.setFont(fontBold);

			// Stile Titolo 2
			HSSFCellStyle csTitolo2 = getBordo4Lati(wb);

			csTitolo2.setAlignment(HSSFCellStyle.ALIGN_CENTER);
			csTitolo2.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
			csTitolo2.setFillForegroundColor(HSSFColor.YELLOW.index);
			csTitolo2.setFont(fontBold);

			// Utilizzo colore non standard (204,255,204)
			HSSFPalette palette = wb.getCustomPalette();
			palette.setColorAtIndex(HSSFColor.GREEN.index, (byte) 204, (byte) 255, (byte) 204);

			HSSFSheet sheet = wb.createSheet("Dettagli");
			sheet.setDefaultColumnWidth(12);

			int nRow = 0;
			if ("-".equals(descrComune))
				nRow = setIntestazione(sheet, ufficioUtenteConnesso, csNull);
			else
				nRow = setIntestazione(sheet, ufficioUtenteConnesso, csNull, descrComune);

			nRow++;
			nRow++;

			HSSFRow row = sheet.createRow(nRow);
			setCell(row, 0, "Intervallo Date Ricerca dal " + dataIniziale + " al " + dataFinale, csNull);

			nRow++;

			int nCell = 0;
			int maxCellxRow = 8;
			String tipoMS = "";

			Iterator itx = v.iterator();
			while (itx.hasNext()) {
				StatisticheMSModel smsm = (StatisticheMSModel) itx.next();

				if (!tipoMS.equals(smsm.getTipoMS())) {
					nRow++;
					nRow++;
					row = sheet.createRow(nRow);
					setCell(row, 0, smsm.getTipoMS(), csBold);
					nRow++;
					nRow++;
				} else
					nRow++;
				tipoMS = smsm.getTipoMS();

				row = sheet.createRow(nRow);
				if ("-".equals(descrComune))
					setCell(row, 0, "ANNO " + StringUtils.toStringJSP(smsm.getAnno()) + " - " + descrComune,
							csNull);
				else
					setCell(row, 0, "ANNO " + StringUtils.toStringJSP(smsm.getAnno()), csNull);

				nCell = 0;
				nRow++;

				row = sheet.getRow(nRow);
				if (row == null)
					row = sheet.createRow(nRow);

				// =======================================
				// Scrivo il Numero Fascicolo
				// =======================================
				for (int i = 0; i < smsm.getProgFasc().size(); i++) {
					// MEV 39: STATISTICHE: DEVE USCIRE ANNO/NUMERO
					String annoNumero = StringUtils.toStringJSP(smsm.getProgFasc().get(i)); // es: -201640012
																							// (se arriva un
																							// numero negativo
																							// indicano che la
																							// misura è
																							// provvisoria)
					// 24/11/2019 (INTERVENTO POST COLLAUDO 11.3) : GESTIONE MISURE PROVVISORIE
					if (annoNumero.startsWith("-")) {
						annoNumero = annoNumero.substring(1, 5) + "/"
								+ annoNumero.substring(5, annoNumero.length()) + " (Misura Provvisoria)";
					} else {
						annoNumero = annoNumero.substring(0, 4) + "/"
								+ annoNumero.substring(4, annoNumero.length());
					}
					setCell(row, nCell, annoNumero, csC);
					if (nCell == maxCellxRow) {
						nCell = 0;
						nRow++;
						row = sheet.createRow(nRow);
					} else {
						nCell++;
					}
				}

				nRow++;
				setRowTotaliProvvedimenti(sheet, nRow, "TOTALE ANNO", csBoldCenter, csNull,
						smsm.getTotFasc().intValue());
			} // end while
		}
	}

	private void setRowTotaliProvvedimenti(HSSFSheet sheet, int nRow, String tipoTot,
			HSSFCellStyle csBoldRight, HSSFCellStyle csNull, int formula) {

		HSSFRow row = sheet.createRow(nRow);
		setCell(row, 6, tipoTot, csNull);
		setCell(row, 8, formula, csBoldRight);
	}

	private int setIntestazione(HSSFSheet sheet, UfficioModel ufficioUtenteConnesso, HSSFCellStyle csNull) {

		int nRow = 0;

		// Create a row and put some cells in it. Rows are 0 based.
		HSSFRow row = sheet.createRow(nRow);
		// Create a cell and put a value in it.
		String value = (ufficioUtenteConnesso.getDescrTipoUfficio().toUpperCase() + " DI "
				+ ufficioUtenteConnesso.getDescrComune().toUpperCase());
		setCell(row, 0, value, csNull);

		nRow++;
		// Create a row and put some cells in it. Rows are 0 based.
		row = sheet.createRow(nRow);
		// Create a cell and put a value in it.

		value = ("Tel. " + ufficioUtenteConnesso.getTelefono() + " - Fax " + ufficioUtenteConnesso.getFax());
		setCell(row, 0, value, csNull);

		return nRow;
	}

	public Vector<StatisticheMSModel> ricercaRiepilogoProcedimentiPendentiPeriodo(String dataIniziale,
			String dataFinale, String ufficioConnesso, String codMagistrato) throws Exception {

		Connection c = null;
		Vector<StatisticheMSModel> v = new Vector<>();
		StatisticheMSSqlDAO smsd = null;

		String range = calcolaRange(dataIniziale, dataFinale);

		try {
			c = getDBConnection();
			smsd = new StatisticheMSSqlDAO(c);
			smsd.ricercaRiepilogoProcedimentiPendentiPeriodo(dataIniziale, dataFinale, ufficioConnesso, range,
					codMagistrato);
			smsd.start();
			while (smsd.next())
				v.add((StatisticheMSModel) smsd.getModelIscrizioni(range));
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new DAOException(
					"StatisticheMSController.ricercaRiepilogoProcedimentiPendentiPeriodo: Non posso leggere : "
							+ daoEx);
		} finally {
			cleanup(smsd);
			cleanup(c);
		}

		// valore di ritorno
		return v;
	}

	public Vector<StatisticheMSModel> ricercaRiepilogoPPPTipologiaMisura(String dataIniziale,
			String dataFinale, String ufficioConnesso, String codMagistrato) throws Exception {

		Connection c = null;
		Vector<StatisticheMSModel> v = new Vector<>();
		StatisticheMSSqlDAO smsd = null;

		try {
			c = getDBConnection();
			smsd = new StatisticheMSSqlDAO(c);
			smsd.ricercaRiepilogoPPPTipologiaMisura(dataIniziale, dataFinale, ufficioConnesso, codMagistrato);
			smsd.start();
			String periodi = "'Pendenti Inizio','Sopravvenuti','Esauriti','Riaperti','Pendenti Fine'";
			while (smsd.next())
				v.add(smsd.getModelRiepilogoPPPTipologiaMisura(periodi));
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new DAOException(
					"StatisticheMSController.ricercaRiepilogoPPPTipologiaMisura: Non posso leggere : "
							+ daoEx);
		} finally {
			cleanup(smsd);
			cleanup(c);
		}

		// valore di ritorno
		return v;
	}

	public Vector<StatisticheMSModel> ricercaDettaglioProcedimentiPendentiPeriodo(String dataIniziale,
			String dataFinale, String ufficioConnesso, String codMagistrato, String var) throws Exception {

		Connection c = null;
		Vector<StatisticheMSModel> v = new Vector<>();
		StatisticheMSSqlDAO smsd = null;

		String range = calcolaRange(dataIniziale, dataFinale);

		try {
			c = getDBConnection();
			smsd = new StatisticheMSSqlDAO(c);
			smsd.ricercaDettaglioProcedimentiPendentiPeriodo(dataIniziale, dataFinale, ufficioConnesso, range,
					codMagistrato, var);
			smsd.start();
			while (smsd.next())
				v.add((StatisticheMSModel) smsd.getModelDettaglio());
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new DAOException(
					"StatisticheMSController.ricercaDettaglioProcedimentiPendentiPeriodo: Non posso leggere : "
							+ daoEx);
		} finally {
			cleanup(smsd);
			cleanup(c);
		}

		// valore di ritorno
		return v;
	}

	public void creaRiepilogoProcedimentiPendentiPeriodo(Vector<StatisticheMSModel> v, HSSFWorkbook wb,
			UfficioModel ufficioUtenteConnesso, String dataIniziale, String dataFinale, String descrComune,
			String cognomeNomeMag) {

		if (v != null && !v.isEmpty()) {
			HSSFCellStyle csNull = wb.createCellStyle();

			// stile per celle col bordo
			HSSFCellStyle cs = getBordo4Lati(wb);

			// stile per celle col bordo con carattere grassetto
			HSSFCellStyle csBold = getBordo4Lati(wb);
			// Create a new font and alter it.
			HSSFFont font = wb.createFont();
			font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
			csBold.setFont(font);

			HSSFCellStyle csBoldCenter = getBordo4Lati(wb);
			csBoldCenter.setFont(font);
			csBoldCenter.setAlignment(HSSFCellStyle.ALIGN_CENTER);

			// primo foglio; RIEPILOGO_ISCRIZIONI
			HSSFSheet sheet;
			if ("".equals(cognomeNomeMag))
				sheet = wb.createSheet("Riepilogo Procedimenti Pendenti");
			else
				sheet = wb.createSheet("Riepilogo Proc Pend Mag");
			sheet.setColumnWidth(0, (75 * 256));

			int nRow = 0;

			// Intestazione del foglio excel
			nRow = setIntestazione(sheet, ufficioUtenteConnesso, csNull, descrComune);

			nRow++;
			nRow++;

			HSSFRow row = sheet.createRow(nRow);
			setCell(row, 0, "Riepilogo Procedimenti Pendenti relativo al periodo dal " + dataIniziale + " al "
					+ dataFinale, csNull);

			nRow++;
			nRow++;

			if (!"".equals(cognomeNomeMag)) {
				row = sheet.createRow(nRow);
				setCell(row, 0, "Statistiche relative al Magistrato: " + cognomeNomeMag, csNull);
				nRow++;
				nRow++;
			}

			// Tipologia tempi
			String tipologia = "RIEPILOGO PROCEDIMENTI PENDENTI NEL PERIODO";
			int colonnaIniziale = 0;

			// creazione riga con altezza per wraptext
			row = sheet.getRow(nRow);
			if (row == null)
				row = sheet.createRow(nRow);
			csBoldCenter.setWrapText(true);
			setCell(row, colonnaIniziale, tipologia, csBoldCenter);
			for (int a = 0; a < v.get(0).getAnni().size(); a++) {
				colonnaIniziale++;
				setCell(row, colonnaIniziale, v.get(0).getAnni().get(a).intValue(), csBoldCenter);
			}

			// int nRowAnno = nRow;
			int nColAnno = 0;

			// inizio ciclo di scrittura dei dati
			Iterator itx = v.iterator();
			while (itx.hasNext()) {
				StatisticheMSModel smsm = (StatisticheMSModel) itx.next();

				int jump = 0;
				nColAnno = 0;
				// ciclo per scrittura colonne
				for (int i = 0; i < smsm.getAnni().size(); i++) {
					nColAnno++;
					if (jump == 0)
						nRow++;
					// scrittura tipologia e valore
					row = sheet.getRow(nRow);
					if (row == null)
						row = sheet.createRow(nRow);
					setCell(row, 0, smsm.getTipoMS(), cs);
					setCell(row, nColAnno, smsm.getIscrittiParziali().get(i).doubleValue(), cs);
					jump += 1;
				}
			}

			// 20191122 [SG]: tolta riga "Totali Iscritti nel Periodo"
			// nRow++;
			// // scrittura totale anno
			// row = sheet.getRow(nRow);
			// if (row == null)
			// row = sheet.createRow(nRow);
			// setCell(row, 0, "Totali Iscritti nel Periodo", csBoldCenter);
			// int colonnaFinale = 1;
			// for (int a = 0; a < v.get(0).getAnni().size(); a++) {
			// String formula = getStringaSomma(nRowAnno + 1, colonnaFinale, nRow - 1, colonnaFinale);
			// setFormulaCell(row, colonnaFinale, formula, csBoldCenter);
			// colonnaFinale++;
			// }
			// 20191125 [SG]: tolta ulitma colonna "Totali"
			// fuori ciclo
			// Richiamo metodo per la scrittura delle formule contenenti
			// le somme parziali e generali
			// nColAnno++;
			// nRow = setTotaliTempi(sheet, csBold, csBoldCenter, nRow, nRowAnno, nColAnno);
			// nRow++;
		}
	}

	public void creaRiepilogoPPPTipologiaMisura(Vector<StatisticheMSModel> v, HSSFWorkbook wb,
			UfficioModel ufficioUtenteConnesso, String dataIniziale, String dataFinale, String descrComune,
			String cognomeNomeMag) {

		if (v != null && !v.isEmpty()) {
			HSSFCellStyle csNull = wb.createCellStyle();

			// stile per celle col bordo
			HSSFCellStyle cs = getBordo4Lati(wb);

			// stile per celle col bordo con carattere grassetto
			HSSFCellStyle csBold = getBordo4Lati(wb);
			// Create a new font and alter it.
			HSSFFont font = wb.createFont();
			font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
			csBold.setFont(font);

			HSSFCellStyle csBoldCenter = getBordo4Lati(wb);
			csBoldCenter.setFont(font);
			csBoldCenter.setAlignment(HSSFCellStyle.ALIGN_CENTER);

			// primo foglio; Tipologia MS
			HSSFSheet sheet;
			if ("".equals(cognomeNomeMag))
				sheet = wb.createSheet("Tipologia MS");
			else
				sheet = wb.createSheet("Tipologia MS Mag");
			sheet.setColumnWidth(0, (75 * 256));

			int nRow = 0;

			// Intestazione del foglio excel
			nRow = setIntestazione(sheet, ufficioUtenteConnesso, csNull, descrComune);

			nRow++;
			nRow++;

			HSSFRow row = sheet.createRow(nRow);
			setCell(row, 0,
					"Riepilogo Iscrizioni relativo al periodo dal " + dataIniziale + " al " + dataFinale,
					csNull);

			nRow++;
			nRow++;

			if (!"".equals(cognomeNomeMag)) {
				row = sheet.createRow(nRow);
				setCell(row, 0, "Statistiche relative al Magistrato: " + cognomeNomeMag, csNull);
				nRow++;
				nRow++;
			}

			// Tipologia tempi
			String tipologia = "TIPO MISURA DI SICUREZZA";
			int colonnaIniziale = 0;

			// creazione riga con altezza per wraptext
			row = sheet.getRow(nRow);
			if (row == null)
				row = sheet.createRow(nRow);
			csBoldCenter.setWrapText(true);
			setCell(row, colonnaIniziale, tipologia, csBoldCenter);
			for (int a = 0; a < v.get(0).getPeriodi().size(); a++) {
				colonnaIniziale++;
				sheet.setColumnWidth(colonnaIniziale, (15 * 256));
				String testo = v.get(0).getPeriodi().get(a).replace("'", "");
				setCell(row, colonnaIniziale, testo, csBoldCenter);
			}

			int nRowAnno = nRow;
			int nColAnno = 0;

			// inizio ciclo di scrittura dei dati
			Iterator itx = v.iterator();
			while (itx.hasNext()) {
				StatisticheMSModel smsm = (StatisticheMSModel) itx.next();

				int jump = 0;
				nColAnno = 0;
				// ciclo per scrittura colonne
				for (int i = 0; i < smsm.getPeriodi().size(); i++) {
					nColAnno++;
					if (jump == 0)
						nRow++;
					// scrittura tipologia e valore
					row = sheet.getRow(nRow);
					if (row == null)
						row = sheet.createRow(nRow);
					setCell(row, 0, smsm.getTipoMS(), cs);
					setCell(row, nColAnno, smsm.getIscrittiParziali().get(i).doubleValue(), cs);
					jump += 1;
				}
			}

			// 20191122 [SG]: tolta riga "Totali Iscritti nel Periodo"
			// nRow++;
			// // scrittura totale anno
			// row = sheet.getRow(nRow);
			// if (row == null)
			// row = sheet.createRow(nRow);
			// setCell(row, 0, "Totali Iscritti nel Periodo", csBoldCenter);
			// int colonnaFinale = 1;
			// for (int a = 0; a < v.get(0).getPeriodi().size(); a++) {
			// String formula = getStringaSomma(nRowAnno + 1, colonnaFinale, nRow - 1, colonnaFinale);
			// setFormulaCell(row, colonnaFinale, formula, csBoldCenter);
			// colonnaFinale++;
			// }

			// fuori ciclo
			// Richiamo metodo per la scrittura delle formule contenenti
			// le somme parziali e generali
			nColAnno++;
			nRow = setTotaliTempi(sheet, csBold, csBoldCenter, nRow, nRowAnno, nColAnno);
			nRow++;
		}
	}

	public void creaDettaglioProcedimentiPendentiPeriodo(Vector<StatisticheMSModel> v, HSSFWorkbook wb,
			UfficioModel ufficioUtenteConnesso, String dataIniziale, String dataFinale, String descrComune,
			String tipo, String cognomeNomeMag) {

		if (v != null && !v.isEmpty()) {
			// Stile della cella vuoto
			HSSFCellStyle csNull = wb.createCellStyle();
			// Stile della cella grassetto
			HSSFCellStyle csBold = wb.createCellStyle();
			// Create a new font and alter it
			HSSFFont fontBold = wb.createFont();
			fontBold.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
			csBold.setFont(fontBold);

			// Stile della cella con bordi ed allineamento a destra
			HSSFCellStyle csC = getBordo4Lati(wb);
			csC.setAlignment(HSSFCellStyle.ALIGN_CENTER);

			// stile per celle col bordo con carattere grassetto
			// ALLINEATO AL CENTRO
			HSSFCellStyle csBoldCenter = getBordo4Lati(wb);
			csBoldCenter.setFont(fontBold);
			csBoldCenter.setAlignment(HSSFCellStyle.ALIGN_CENTER);

			// Stile Titolo 1
			HSSFCellStyle csTitolo1 = getBordo4Lati(wb);

			csTitolo1.setAlignment(HSSFCellStyle.ALIGN_CENTER);
			csTitolo1.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
			csTitolo1.setFillForegroundColor(HSSFColor.GREEN.index);
			csTitolo1.setFont(fontBold);

			// Stile Titolo 2
			HSSFCellStyle csTitolo2 = getBordo4Lati(wb);

			csTitolo2.setAlignment(HSSFCellStyle.ALIGN_CENTER);
			csTitolo2.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
			csTitolo2.setFillForegroundColor(HSSFColor.YELLOW.index);
			csTitolo2.setFont(fontBold);

			// Utilizzo colore non standard (204,255,204)
			HSSFPalette palette = wb.getCustomPalette();
			palette.setColorAtIndex(HSSFColor.GREEN.index, (byte) 204, (byte) 255, (byte) 204);

			HSSFSheet sheet = wb.createSheet("Dettagli " + tipo);
			sheet.setDefaultColumnWidth(30);

			int nRow = 0;
			if ("-".equals(descrComune))
				nRow = setIntestazione(sheet, ufficioUtenteConnesso, csNull);
			else
				nRow = setIntestazione(sheet, ufficioUtenteConnesso, csNull, descrComune);

			nRow++;
			nRow++;

			HSSFRow row = sheet.createRow(nRow);
			setCell(row, 0, "Intervallo Date Ricerca dal " + dataIniziale + " al " + dataFinale, csNull);

			nRow++;
			nRow++;

			if (!"".equals(cognomeNomeMag)) {
				row = sheet.createRow(nRow);
				setCell(row, 0, "Statistiche relative al Magistrato: " + cognomeNomeMag, csNull);
			}

			int nCell = 0;
			int maxCellxRow = 8;
			String tipoMS = "";

			Iterator itx = v.iterator();
			while (itx.hasNext()) {
				StatisticheMSModel smsm = (StatisticheMSModel) itx.next();

				if (!tipoMS.equals(smsm.getTipoMS())) {
					nRow++;
					nRow++;
					row = sheet.createRow(nRow);
					setCell(row, 0, smsm.getTipoMS(), csBold);
					nRow++;
					nRow++;
				} else
					nRow++;
				tipoMS = smsm.getTipoMS();

				row = sheet.createRow(nRow);
				if ("-".equals(descrComune))
					setCell(row, 0, "ANNO " + StringUtils.toStringJSP(smsm.getAnno()) + " - " + descrComune,
							csNull);
				else
					setCell(row, 0, "ANNO " + StringUtils.toStringJSP(smsm.getAnno()), csNull);

				nCell = 0;
				nRow++;

				row = sheet.getRow(nRow);
				if (row == null)
					row = sheet.createRow(nRow);

				// =======================================
				// Scrivo il Numero Fascicolo
				// =======================================
				for (int i = 0; i < smsm.getProgFasc().size(); i++) {
					// MEV 39: STATISTICHE: DEVE USCIRE ANNO/NUMERO
					// 24/11/2019 (INTERVENTO POST COLLAUDO 11.3) : GESTIONE MISURE PROVVISORIE
					String annoNumero = StringUtils.toStringJSP(smsm.getProgFasc().get(i)); // es: -201640012
																							// (se arriva un
																							// numero negativo
																							// indicano che la
																							// misura è
																							// provvisoria)
					if (annoNumero.startsWith("-")) {
						annoNumero = annoNumero.substring(1, 5) + "/"
								+ annoNumero.substring(5, annoNumero.length()) + " (Misura Provvisoria)";
					} else {
						annoNumero = annoNumero.substring(0, 4) + "/"
								+ annoNumero.substring(4, annoNumero.length());
					}
					setCell(row, nCell, annoNumero, csC);
					if (nCell == maxCellxRow) {
						nCell = 0;
						nRow++;
						row = sheet.createRow(nRow);
					} else {
						nCell++;
					}
				}

				nRow++;
				setRowTotaliProvvedimenti(sheet, nRow, "TOTALE ANNO", csBoldCenter, csNull,
						smsm.getTotFasc().intValue());
			} // end while
		}
	}

	// public void ricercaRiepilogoMovimentoProcedimentiStoreProcedure(String dataIniziale, String dataFinale,
	// String ufficioConnesso) throws F3BException {
	//
	// Connection c = null;
	// StatistichMSStoreProcedureDAO smsspdao = null;
	//
	// try {
	// c = getDBConnection();
	// smsspdao = new StatistichMSStoreProcedureDAO(c);
	// smsspdao.setRiepilogoMovimentoProcedimentiStoreProcedure();
	// smsspdao.setDataInizio(dataIniziale);
	// smsspdao.setDataFine(dataFinale);
	// smsspdao.setCodUfficioInserimento(ufficioConnesso);
	// smsspdao.execute();
	// c.commit();
	// } catch (DAOException daoEx) {
	// throw new F3BException(
	// "StatisticheMSController.ricercaRiepilogoMovimentoProcedimentiStoreProcedure: " + daoEx);
	// } catch (SQLException sqe) {
	// throw new F3BException(
	// "StatisticheMSController.ricercaRiepilogoMovimentoProcedimentiStoreProcedure: " + sqe);
	// } finally {
	// cleanup(smsspdao);
	// cleanup(c);
	// }
	// }

	public void ricercaStatiFascicoloStoredProcedure(String ufficioConnesso, String accorpato1,
			String accorpato2, String accorpato3, String dataVerifica, String dataIniziale, String dataFinale)
			throws Exception {

		Connection c = null;
		StatistichMSStoreProcedureDAO smsdpdao = null;

		try {
			c = getDBConnection();

			smsdpdao = new StatistichMSStoreProcedureDAO(c);
			smsdpdao.setStatiFascicoloStoreProcedure();
			smsdpdao.setCodUfficioInserimento(ufficioConnesso);
			smsdpdao.setDataVerifica(dataVerifica);
			smsdpdao.setCodUfficioAccorpato_1(accorpato1);
			smsdpdao.setCodUfficioAccorpato_2(accorpato2);
			smsdpdao.setCodUfficioAccorpato_3(accorpato3);
			smsdpdao.setDataInizio(dataIniziale);
			smsdpdao.setDataFine(dataFinale);
			smsdpdao.execute();
			c.commit();
		} catch (DAOException daoEx) {
			throw new DAOException("StatisticheMSController.RicercaStatiFascicoloStoredProcedure: " + daoEx);
		} catch (SQLException sqe) {
			throw new SQLException("StatisticheMSController.RicercaStatiFascicoloStoredProcedure: " + sqe);
		} finally {
			cleanup(smsdpdao);
			cleanup(c);
		}
	}

	public Vector<StatoFascicoloResModel> ricercaStatiFascicoloRes() throws Exception {

		Connection c = null;
		Vector<StatoFascicoloResModel> v = new Vector<>();
		StatoFascicoloResDAO sfrdao = null;

		try {
			c = getDBConnection();
			sfrdao = new StatoFascicoloResDAO(c);
			sfrdao.setOrdinamento("ORDINAMENTO");
			sfrdao.start();
			while (sfrdao.next()) {
				StatoFascicoloResModel lStatoModel = (StatoFascicoloResModel) sfrdao.getModel();
				if (!ICostantiStatis.TIPO_CAMPO_TITOLO1.equals(lStatoModel.getTipoCampo())
						&& !ICostantiStatis.TIPO_CAMPO_TITOLO2.equals(lStatoModel.getTipoCampo())) {
					v.add(lStatoModel);
				}
			}
			if (v.size() == 0)
				throw new F3BException(F3BException.USER_MESSAGE, "Nessun Elemento trovato");
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new DAOException(
					"StatisticheMSController.RicercaStatiFascicoloRes: Non posso leggere : " + daoEx);
		} finally {
			cleanup(sfrdao);
			cleanup(c);
		}
		return v;
	}

	public Vector<String> getTitoliPerCodici(String[] codiciSelezionati, String tipo) throws Exception {

		Vector<String> elencoTitoli = new Vector<>();
		Connection c = null;
		IspProvvedimentiSqlDAO ipsdao = null;

		try {
			c = getDBConnection();
			ipsdao = new IspProvvedimentiSqlDAO(c);
			ipsdao.getTitoliPerCodiciPerTipoTitoloMS(codiciSelezionati, tipo);
			ipsdao.start();
			while (ipsdao.next())
				elencoTitoli.add(ipsdao.getString(tipo));
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("DAOException: ", daoEx);
			throw new DAOException("StatisticheMSController.getTitoliPerCodici. Non posso leggere: " + daoEx);
		} finally {
			cleanup(ipsdao);
			cleanup(c);
		}

		// valore di ritorno
		return elencoTitoli;
	}

	public Vector ricercaDettaglioProcedimenti(String[] codiciTitoli) throws Exception {

		Connection c = null;
		Vector provvedimenti = new Vector();
		IspProvvedimentiSqlDAO ipsdao = null;

		try {
			c = getDBConnection();
			ipsdao = new IspProvvedimentiSqlDAO(c);
			ipsdao.RicercaDettaglioProcedimentiMS(codiciTitoli);
			ipsdao.start();
			while (ipsdao.next())
				provvedimenti.add(ipsdao.getModelExtend()); // 24/11/2019 (INTERVENTO POST COLLAUDO 11.3) :
															// INVOCO IL UN NUOVO METODO getModelExtend
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new DAOException(
					"StatisticheMSController.ricercaDettaglioProcedimenti. Non posso leggere: " + daoEx);
		} finally {
			cleanup(ipsdao);
			cleanup(c);
		}

		// valore di ritorno
		return provvedimenti;
	}

	public void creaDettagliProcedimenti(Vector dettagli, HSSFWorkbook wb, UfficioModel ufficioUtenteConnesso,
			String nomeFoglio, String codiceUffAccorpato, String dataIniziale, String dataFinale) {

		// Stile della cella vuoto
		HSSFCellStyle csNull = wb.createCellStyle();
		// Stile della cella grassetto
		HSSFCellStyle csBold = wb.createCellStyle();
		// Create a new font and alter it.
		HSSFFont fontBold = wb.createFont();
		fontBold.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		csBold.setFont(fontBold);

		// Stile della cella con bordi ed allineamento a destra
		HSSFCellStyle csR = getBordo4Lati(wb);
		csR.setAlignment(HSSFCellStyle.ALIGN_RIGHT);

		// stile per celle col bordo con carattere grassetto ALLINEATO A DESTRA
		HSSFCellStyle csBoldRight = getBordo4Lati(wb);
		csBoldRight.setFont(fontBold);
		csBoldRight.setAlignment(HSSFCellStyle.ALIGN_RIGHT);

		// Stile Titolo 1
		HSSFCellStyle csTitolo1 = getBordo4Lati(wb);
		csTitolo1.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		csTitolo1.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		csTitolo1.setFillForegroundColor(HSSFColor.GREEN.index);
		csTitolo1.setFont(fontBold);

		// Stile Titolo 2
		HSSFCellStyle csTitolo2 = getBordo4Lati(wb);
		csTitolo2.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		csTitolo2.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		csTitolo2.setFillForegroundColor(HSSFColor.YELLOW.index);
		csTitolo2.setFont(fontBold);

		// Utilizzo colore non standard (204,255,204)
		HSSFPalette palette = wb.getCustomPalette();
		palette.setColorAtIndex(HSSFColor.GREEN.index, (byte) 204, (byte) 255, (byte) 204);

		HSSFSheet sheet = wb.createSheet(nomeFoglio);
		if ("Dettagli".equals(nomeFoglio))
			sheet.setDefaultColumnWidth(12);

		int nRow = 0;
		if ("-".equals(codiceUffAccorpato))
			nRow = setIntestazione(sheet, ufficioUtenteConnesso, csNull);
		else
			nRow = setIntestazione(sheet, ufficioUtenteConnesso, csNull, codiceUffAccorpato);

		nRow++;
		nRow++;

		HSSFRow row = sheet.createRow(nRow);
		setCell(row, 0, "Intervallo Date Ricerca dal " + dataIniziale + " al " + dataFinale, csNull);

		nRow++;
		nRow++;

		// Create a row and put some cells in it. Rows are 0 based.
		row = sheet.createRow(nRow);
		setCell(row, 0, "ELENCO   NUMERICO   DELLE   PROCEDURE  ESECUTIVE  PENDENTI  ALLA  DATA  DEL",
				csNull);

		nRow++;

		// Create a row and put some cells in it. Rows are 0 based.
		row = sheet.createRow(nRow);
		setCell(row, 0, DateUtils.getSysDate("dd-MM-yyyy")
				+ ",   DISTINTE   PER   ANNO   E  SECONDO  LO  STATO  DI  ESECUZIONE", csNull);

		String oldUfficio = "";
		boolean firstUfficio = true;

		int nCell = 0;
		Integer oldCodStato = new Integer(0);
		Integer oldAnno = new Integer(0);
		boolean firstCod = true;
		boolean firstAnno = true;
		int totGenNum = 0;
		int totAnnoNum = 0;
		int formula = 0;
		int maxCellxRow = 8;

		// boolean aa = true;
		boolean lLastRigaTitolo1 = false; // indica se l'ultima riga inserita era un Titolo1
		boolean lLastRigaTitolo2 = false; // indica se l'ultima riga inserita era un Titolo1

		nRow++;

		Iterator itx = dettagli.iterator();
		while (itx.hasNext()) {
			IspProvvedimentiModel ipm = (IspProvvedimentiModel) itx.next();
			if (!oldCodStato.equals(ipm.getCodStatoFascicoloRes())) {
				if (!firstCod && !lLastRigaTitolo1 && !lLastRigaTitolo2) {
					// ====================================================================
					// Cambio Stato_Fascicolo
					// - Va scritta la riga con il 'TOTALE ANNO'
					// - Va scritta una riga vuota
					// - Va scritta la riga con il 'TOTALE GENERALE'
					// ====================================================================
					if (nCell > 0)
						nRow++;
					formula = totAnnoNum;
					setRowTotaliProvvedimenti(sheet, nRow, "TOTALE ANNO", csBoldRight, csNull, formula);
					totGenNum += totAnnoNum;
					nRow++;
					nRow++;
					formula = totGenNum;
					setRowTotaliProvvedimenti(sheet, nRow, "TOTALE GENERALE", csBoldRight, csNull, formula);
					totGenNum = 0;
					nRow++;
				}

				if (ICostantiStatis.TIPO_CAMPO_TITOLO1.equals(ipm.getTipoCampo())
						|| ICostantiStatis.TIPO_CAMPO_TITOLO2.equals(ipm.getTipoCampo())) {
					// Il record corrente contine il TITOLO (1 o 2) da visualizare
					nRow++;
					if (!lLastRigaTitolo1)
						nRow++;

					HSSFCellStyle csRiga = null;
					if (ICostantiStatis.TIPO_CAMPO_TITOLO1.equals(ipm.getTipoCampo())) {
						csRiga = csTitolo1;
						lLastRigaTitolo1 = true;
						lLastRigaTitolo2 = false;
					} else {
						csRiga = csTitolo2;
						lLastRigaTitolo1 = false;
						lLastRigaTitolo2 = true;
					}
					row = sheet.createRow(nRow);
					setCell(row, 0, ipm.getDescrStatoFascicoloRes(), csRiga);
					for (int i = 1; i <= maxCellxRow; i++)
						setCell(row, i, "", csRiga);
					sheet.addMergedRegion(new CellRangeAddress(nRow, nRow, 0, maxCellxRow));
				} else {
					lLastRigaTitolo1 = false;
					lLastRigaTitolo2 = false;

					nRow++;
					nRow++;
					row = sheet.createRow(nRow);
					setCell(row, 0, ipm.getDescrStatoFascicoloRes(), csBold);
				} // end if T1,T2

				oldAnno = new Integer(0);

				if (!ICostantiStatis.TIPO_CAMPO_TITOLO1.equals(ipm.getTipoCampo())
						&& !ICostantiStatis.TIPO_CAMPO_TITOLO2.equals(ipm.getTipoCampo())) {
					firstCod = false;
				}
				firstAnno = true;
			}

			oldCodStato = ipm.getCodStatoFascicoloRes();

			if (ICostantiStatis.TIPO_CAMPO_TITOLO1.equals(ipm.getTipoCampo())
					|| ICostantiStatis.TIPO_CAMPO_TITOLO2.equals(ipm.getTipoCampo())) {
				// Nel caso in cui sto visualizzando un titolo (T1,T2) non procedo
				// con il test su cambio anno o cambio ufficio. Il test e' inutile e
				// andrebbe in errore non essendo definiti sul record del titolo ne
				// l'anno ne l'ufficio
				continue;
			}

			// ====================================================================
			// Se nuovo anno (o prima riga del nuovo Stato Fascicolo)
			// ANNO YYYY - Descrizione Ufficio
			if (!oldAnno.equals(ipm.getChiaveAnno())) {
				if (!firstAnno) {
					if (nCell > 0)
						nRow++;
					formula = totAnnoNum;
					setRowTotaliProvvedimenti(sheet, nRow, "TOTALE ANNO", csBoldRight, csNull, formula);
					totGenNum += totAnnoNum;
				}

				firstAnno = false;

				nRow++;
				nRow++;

				row = sheet.createRow(nRow);
				if (codiceUffAccorpato.equals("-"))
					setCell(row, 0, "ANNO " + StringUtils.toStringJSP(ipm.getChiaveAnno()) + " - "
							+ ipm.getDescUfficioInserimento(), csNull);
				else
					setCell(row, 0, "ANNO " + StringUtils.toStringJSP(ipm.getChiaveAnno()), csNull);

				firstUfficio = true;
				nCell = 0;
				nRow++;
				totAnnoNum = 0;
			} else
				firstUfficio = false;

			oldAnno = ipm.getChiaveAnno();
			row = sheet.getRow(nRow);
			if (row == null)
				row = sheet.createRow(nRow);
			if (!oldUfficio.equals(ipm.getCodUfficioInserimento())) {
				if (!firstUfficio) {
					nRow++;
					nRow++;
					row = sheet.createRow(nRow);
					if (codiceUffAccorpato.equals("-"))
						setCell(row, 0, "ANNO " + StringUtils.toStringJSP(ipm.getChiaveAnno()) + " - "
								+ ipm.getDescUfficioInserimento(), csNull);
					else
						setCell(row, 0, "ANNO " + StringUtils.toStringJSP(ipm.getChiaveAnno()), csNull);
					nCell = 0;
					nRow++;
					row = sheet.getRow(nRow);
					if (row == null)
						row = sheet.createRow(nRow);
					if (ipm.getChiaveProgrOrig() != null)
						setCell(row, nCell, StringUtils.toStringJSP(ipm.getChiaveProgrOrig()), csR);
					else {
						// 24/11/2019 (INTERVENTO POST COLLAUDO 11.3) : GESTIONE MISURE PROVVISORIE
						String annoNumero = StringUtils
								.toStringJSP(ipm.getChiaveAnno() + "/" + ipm.getChiaveProgr());
						if (ipm.getTipoMisura() != null) {
							annoNumero = annoNumero + " (Misura Provvisoria)";
						}
						setCell(row, nCell, annoNumero, csR);
					}
					// else
					// setCell(row, nCell, StringUtils.toStringJSP(ipm.getChiaveProgrOrig()), csR);
					oldUfficio = ipm.getCodUfficioInserimento();
				} else
					firstUfficio = false;
			}

			// =======================================
			// Scrivo il Numero Fascicolo
			// =======================================
			if (ipm.getChiaveProgrOrig() != null)
				setCell(row, nCell, StringUtils.toStringJSP(ipm.getChiaveProgrOrig()), csR);
			else {
				// 24/11/2019 (INTERVENTO POST COLLAUDO 11.3) : GESTIONE MISURE PROVVISORIE
				String annoNumero = StringUtils.toStringJSP(ipm.getChiaveAnno() + "/" + ipm.getChiaveProgr());
				if (ipm.getTipoMisura() != null) {
					annoNumero = annoNumero + " (Misura Provvisoria)";
				}
				setCell(row, nCell, annoNumero, csR);
			}

			oldUfficio = ipm.getCodUfficioInserimento();
			totAnnoNum++;

			if (nCell == maxCellxRow) {
				nCell = 0;
				nRow++;
			} else
				nCell++;
		} // end while

		nRow++;
		// 24/11/2019 (INTERVENTO POST COLLAUDO 11.3) : ELIMINO I TOTALI PER FOGLIO (SONO TUTTI ERRATI!!)
		formula = totAnnoNum;
		setRowTotaliProvvedimenti(sheet, nRow, "TOTALE ANNO", csBoldRight, csNull, formula);
		totGenNum += totAnnoNum;
		nRow++;
		nRow++;
		formula = totGenNum;
		setRowTotaliProvvedimenti(sheet, nRow, "TOTALE GENERALE", csBoldRight, csNull, formula);
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("Fine createDettagliProcedimenti");
	}

	public void elaboraStatisticaAttMagStoreProcedure(String dataIniziale, String dataFinale,
			String ufficioConnesso) throws Exception {

		Connection c = null;
		StatistichMSStoreProcedureDAO smsspdao = null;

		try {
			c = getDBConnection();
			smsspdao = new StatistichMSStoreProcedureDAO(c);
			smsspdao.setElaboraStatisticaAttMagStoreProcedure();
			smsspdao.setDataInizio(dataIniziale);
			smsspdao.setDataFine(dataFinale);
			smsspdao.setCodUfficioInserimento(ufficioConnesso);
			smsspdao.execute();
			c.commit();
		} catch (DAOException daoEx) {
			throw new DAOException("StatisticheMSController.elaboraStatisticaAttMagStoreProcedure: " + daoEx);
		} catch (SQLException sqe) {
			throw new SQLException("StatisticheMSController.elaboraStatisticaAttMagStoreProcedure: " + sqe);
		} finally {
			cleanup(smsspdao);
			cleanup(c);
		}
	}

	public Vector<StatisticheMSModel>[] ricercaAttivitaMagistratiRiepilogo(String dataIniziale,
			String dataFinale) throws Exception {

		Connection c = null;
		Vector<StatisticheMSModel> vTemp;
		StatisticheMSSqlDAO smssdao = null;
		String di = "", df = "";
		String range = calcolaRange(dataIniziale, dataFinale);
		String[] s = range.split(",");
		Vector[] v = new Vector[s.length];
		int cont = 0;

		try {
			c = getDBConnection();
			dataIniziale = dataIniziale.replaceAll("/", "");
			dataFinale = dataFinale.replaceAll("/", "");
			int annoIniziale = new Integer(dataIniziale.substring(4)).intValue();
			int annoFinale = new Integer(dataFinale.substring(4)).intValue();
			for (int anno = annoIniziale; anno <= annoFinale; anno++) {
				vTemp = new Vector<>();
				if (annoIniziale != annoFinale) {
					if (anno != annoIniziale)
						di = "0101" + anno;
					else
						di = dataIniziale;
					if (anno != annoFinale)
						df = "3112" + anno;
					else
						df = dataFinale;
				} else {
					di = dataIniziale;
					df = dataFinale;
				}
				smssdao = new StatisticheMSSqlDAO(c);
				smssdao.ricercaAttivitaMagistratiRiepilogo(anno, di, df);
				smssdao.start();
				while (smssdao.next())
					vTemp.add((StatisticheMSModel) smssdao.getModelAttivita());
				v[cont] = vTemp;
				cont++;
			}
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new DAOException(
					"StatisticheMSController.ricercaAttivitaMagistratiRiepilogo. Non posso leggere: "
							+ daoEx);
		} finally {
			cleanup(smssdao);
			cleanup(c);
		}

		// valore di ritorno
		return v;
	}

	public void creaStatisticaAttMagRiep(Vector<StatisticheMSModel>[] v, HSSFWorkbook wb,
			UfficioModel ufficioUtenteConnesso, String dataIniziale, String dataFinale, String descrComune,
			Vector<StatisticheMSModel>[] motiviAnno, String cognomeNomeMag) throws Exception {

		HSSFCellStyle csNull = wb.createCellStyle();

		// stile per celle col bordo
		HSSFCellStyle cs = getBordo4Lati(wb);

		// stile per celle col bordo con carattere grassetto
		HSSFCellStyle csBold = getBordo4Lati(wb);

		// Create a new font and alter it.
		HSSFFont font = wb.createFont();
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		csBold.setFont(font);

		// primo foglio -----------------------------------------RIEPILOGO
		HSSFSheet sheet = wb.createSheet("Riepilogo");
		sheet.setColumnWidth(0, (125 * 256));

		// Colore per TIPOLOGIA
		HSSFCellStyle style = wb.createCellStyle();
		style = getBordo4Lati(wb);
		HSSFFont fontGR = wb.createFont();
		fontGR.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		fontGR.setColor(HSSFColor.BLUE.index);
		style.setFont(fontGR);

		// Colore per TOTALI
		HSSFCellStyle stylered = wb.createCellStyle();
		stylered = getBordo4Lati(wb);
		HSSFFont fontR = wb.createFont();
		fontR.setColor(HSSFColor.RED.index);
		stylered.setFont(fontR);

		// Intestazione del foglio excel
		int nRow = setIntestazione(sheet, ufficioUtenteConnesso, csNull, descrComune);

		nRow++;
		nRow++;

		// Create a row and put some cells in it. Rows are 0 based.
		HSSFRow row = sheet.createRow(nRow);
		setCell(row, 0, "Statistica lavoro magistrati del " + DateUtils.getSysDate("dd/MM/yyyy"), csNull);

		nRow++;

		row = sheet.createRow(nRow);
		setCell(row, 0, "Periodo dal " + dataIniziale + " al " + dataFinale, csNull);

		int annoOld = 0;
		String magOld = "";
		int nColAnno = 0;
		int nRowAnno = nRow;
		int firstMag = 0;
		int colonne = 0;
		int k = 0;
		int numRighe = getNumTipologieAttivita();
		int[] righeBuone = new int[numRighe];
		if (v != null && v.length != 0) {
			Vector<StatisticheMSModel> vs = v[0];
			if (vs.size() > numRighe) {
				numRighe = vs.size();
				righeBuone = new int[numRighe];
			}
		}

		// inizio ciclo di scrittura dei dati
		for (int i = 0; i < v.length; i++) {
			Vector<StatisticheMSModel> vsmsm = v[i];
			Iterator itx = vsmsm.iterator();
			while (itx.hasNext()) {
				StatisticheMSModel smsm = (StatisticheMSModel) itx.next();
				// test per cambio magistrato
				if (!(magOld.equals(cognomeNomeMag))) {
					if (firstMag > 0) {
						nColAnno++;
						// Richiamo metodo per la scrittura delle formule contenenti
						// le somme parziali e generali
						nRow = setTotaliAttivitaMagistrati(sheet, csBold, nRow, nRowAnno, nColAnno,
								righeBuone, style, stylered);
						k = 0;
					}
					firstMag++;

					nRow++;
					nRow++;

					nRowAnno = nRow;
					nColAnno = 0;
					annoOld = 0;

					row = sheet.getRow(nRowAnno);
					if (row == null)
						row = sheet.createRow(nRowAnno);

					setCell(row, nColAnno, cognomeNomeMag, csBold);
				}
				magOld = cognomeNomeMag;
				// test per cambio anno
				if (annoOld != smsm.getAnno().intValue()) {
					nColAnno++;
					nRow = nRowAnno;
					// scrittura anno
					row = sheet.getRow(nRowAnno);
					if (row == null)
						row = sheet.createRow(nRowAnno);

					setCell(row, nColAnno, smsm.getAnno().doubleValue(), csBold);
				}
				annoOld = smsm.getAnno().intValue();

				nRow++;

				// scrittura tipologia e valore
				row = sheet.getRow(nRow);
				if (row == null)
					row = sheet.createRow(nRow);

				setCell(row, 0, smsm.getDescrMotivo(), style);
				setCell(row, nColAnno, smsm.getConta().doubleValue(), style);

				// serve per segnare le righe buone ai fini del conteggio finale
				colonne = nColAnno;
				if (colonne == 1) {
					righeBuone[k] = nRow;
					if (k < righeBuone.length)
						k++;
				}
				Vector motivi = cercaMotivi(smsm.getCodAttivita());
				nRow++;
				row = sheet.getRow(nRow);
				if (row == null)
					row = sheet.createRow(nRow);

				setCell(row, 0, "DI CUI", cs);

				Iterator itx1 = motivi.iterator();
				while (itx1.hasNext()) {
					StatisticheMSModel smsm1 = (StatisticheMSModel) itx1.next();
					nRow++;
					row = sheet.getRow(nRow);
					if (row == null)
						row = sheet.createRow(nRow);

					setCell(row, 0, "   " + smsm1.getDescrMotivo(), cs);
					int totaleMotivi = 0;
					if ("RIEPILOGO GENERALE".equals(cognomeNomeMag)) {
						Iterator itx2 = motiviAnno[i].iterator();
						while (itx2.hasNext()) {
							StatisticheMSModel smsm2 = (StatisticheMSModel) itx2.next();
							if (smsm2.getAnno().equals(smsm.getAnno())
									&& smsm2.getCodAttivita().equals(smsm.getCodAttivita())
									&& smsm2.getCodMotivo().equals(smsm1.getCodMotivo())) {
								if (smsm1.getTipoMS() != null) {
									if (smsm1.getTipoMS().equals(smsm2.getTipoMS()))
										totaleMotivi = smsm2.getConta();
								} else
									totaleMotivi = smsm2.getConta();
							}
						}
					}
					setCell(row, nColAnno, totaleMotivi, cs);
				}
			}
		}

		// fuori ciclo
		// Richiamo metodo per la scrittura delle formule contenenti le somme parziali e generali
		nColAnno++;

		nRow = setTotaliAttivitaMagistrati(sheet, csBold, nRow, nRowAnno, nColAnno, righeBuone, style,
				stylered);
	}

	private Vector cercaMotivi(String codAttivita) throws Exception {

		Connection c = null;
		Vector<StatisticheMSModel> v = new Vector<>();
		StatisticheMSSqlDAO smssdao = null;
		try {
			c = getDBConnection();
			smssdao = new StatisticheMSSqlDAO(c);
			smssdao.ricercaMotivoPerAttivita(codAttivita);
			smssdao.start();
			while (smssdao.next())
				v.add(smssdao.getModelMotivo());
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new DAOException("StatisticheMSController.cercaMotivi. Non posso leggere: " + daoEx);
		} finally {
			cleanup(c);
			cleanup(smssdao);
		}

		// valore di ritorno
		return v;
	}

	private int getNumTipologieAttivita() throws Exception {

		int numRecord = 0;
		Connection c = null;
		StatisticheMSSqlDAO smssdao = null;

		try {
			c = getDBConnection();
			smssdao = new StatisticheMSSqlDAO(c);
			smssdao.getNumTipologieAttivita();
			smssdao.start();
			smssdao.next();
			numRecord = smssdao.getInt("contaTipologie");
		} catch (DAOException sqe) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("SQLException: ", sqe);
			throw new DAOException(
					"StatisticheMSController.getNumTipologieAttivita. Non posso leggere: " + sqe);
		} finally {
			cleanup(smssdao);
			cleanup(c);
		}

		// valore di ritorno
		return numRecord;
	}

	public Vector<StatisticheMSModel>[] ricercaAttivitaMagistratiDettaglio(String dataIniziale,
			String dataFinale, String codMagistrato, String cognomeNomeMag) throws Exception {

		Connection c = null;
		Vector<StatisticheMSModel> vTemp;
		StatisticheMSSqlDAO smssdao = null;
		String di = "", df = "";
		String range = calcolaRange(dataIniziale, dataFinale);
		String[] s = range.split(",");
		Vector[] v = new Vector[s.length];
		int cont = 0;

		try {
			c = getDBConnection();
			dataIniziale = dataIniziale.replaceAll("/", "");
			dataFinale = dataFinale.replaceAll("/", "");
			int annoIniziale = new Integer(dataIniziale.substring(4)).intValue();
			int annoFinale = new Integer(dataFinale.substring(4)).intValue();
			for (int anno = annoIniziale; anno <= annoFinale; anno++) {
				vTemp = new Vector<>();
				if (annoIniziale != annoFinale) {
					if (anno != annoIniziale)
						di = "0101" + anno;
					else
						di = dataIniziale;
					if (anno != annoFinale)
						df = "3112" + anno;
					else
						df = dataFinale;
				} else {
					di = dataIniziale;
					df = dataFinale;
				}
				smssdao = new StatisticheMSSqlDAO(c);
				smssdao.ricercaAttivitaMagistratiDettaglio(anno, di, df, codMagistrato);
				smssdao.start();
				while (smssdao.next())
					vTemp.add((StatisticheMSModel) smssdao.getModelAttivita());
				v[cont] = vTemp;
				cont++;
			}
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new DAOException(
					"StatisticheMSController.ricercaAttivitaMagistratiDettaglio. Non posso leggere: "
							+ daoEx);
		} finally {
			cleanup(smssdao);
			cleanup(c);
		}

		// valore di ritorno
		return v;
	}

	public void creaStatisticaAttMagDett(/* Vector<StatisticheMSModel>[] v */Vector tuttiMagsDett,
			HSSFWorkbook wb, UfficioModel ufficioUtenteConnesso, String dataIniziale, String dataFinale,
			String descrComune, /* String cognomeNomeMag */Vector tuttiMagsCognomeNome,
			/* Vector<StatisticheMSModel>[] motiviMagAnno */Vector tuttiMagsAnno) throws Exception {

		HSSFCellStyle csNull = wb.createCellStyle();

		// stile per celle col bordo
		HSSFCellStyle cs = getBordo4Lati(wb);

		// stile per celle col bordo con carattere grassetto
		HSSFCellStyle csBold = getBordo4Lati(wb);

		// Create a new font and alter it.
		HSSFFont font = wb.createFont();
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		csBold.setFont(font);

		// primo foglio -----------------------------------------RIEPILOGO
		HSSFSheet sheet = wb.createSheet("Dettaglio");
		sheet.setColumnWidth(0, (125 * 256));

		// Colore per TIPOLOGIA
		HSSFCellStyle style = wb.createCellStyle();
		style = getBordo4Lati(wb);
		HSSFFont fontGR = wb.createFont();
		fontGR.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		fontGR.setColor(HSSFColor.BLUE.index);
		style.setFont(fontGR);

		// Colore per TOTALI
		HSSFCellStyle stylered = wb.createCellStyle();
		stylered = getBordo4Lati(wb);
		HSSFFont fontR = wb.createFont();
		fontR.setColor(HSSFColor.RED.index);
		stylered.setFont(fontR);

		// Intestazione del foglio excel
		int nRow = setIntestazione(sheet, ufficioUtenteConnesso, csNull, descrComune);

		nRow++;
		nRow++;

		// Create a row and put some cells in it. Rows are 0 based.
		HSSFRow row = sheet.createRow(nRow);
		setCell(row, 0, "Statistica lavoro magistrati del " + DateUtils.getSysDate("dd/MM/yyyy"), csNull);

		nRow++;

		row = sheet.createRow(nRow);
		setCell(row, 0, "Periodo dal " + dataIniziale + " al " + dataFinale, csNull);

		int annoOld = 0;
		String magOld = "";
		int nColAnno = 0;
		int nRowAnno = nRow;
		int firstMag = 0;
		int colonne = 0;
		int k = 0;
		int numRighe = getNumTipologieAttivita();
		int[] righeBuone = new int[numRighe];

		// ciclo esterno
		Iterator est = tuttiMagsDett.iterator();
		int contEst = 0;
		while (est.hasNext()) {
			Vector<StatisticheMSModel>[] v = (Vector<StatisticheMSModel>[]) est.next();
			// inizio ciclo di scrittura dei dati
			for (int i = 0; i < v.length; i++) {
				Vector<StatisticheMSModel> vsmsm = v[i];
				Iterator itx = vsmsm.iterator();
				while (itx.hasNext()) {
					StatisticheMSModel smsm = (StatisticheMSModel) itx.next();
					// test per cambio magistrato
					String cognomeNomeMag = StringUtils.toStringJSP(tuttiMagsCognomeNome.get(contEst));
					if (!(magOld.equals(cognomeNomeMag))) {
						if (firstMag > 0) {
							nColAnno++;
							// Richiamo metodo per la scrittura delle formule contenenti
							// le somme parziali e generali
							nRow = setTotaliAttivitaMagistrati(sheet, csBold, nRow, nRowAnno, nColAnno,
									righeBuone, style, stylered);
							k = 0;
						}
						firstMag++;

						nRow++;
						nRow++;

						nRowAnno = nRow;
						nColAnno = 0;
						annoOld = 0;

						row = sheet.getRow(nRowAnno);
						if (row == null)
							row = sheet.createRow(nRowAnno);

						setCell(row, nColAnno, cognomeNomeMag, csBold);
					}
					magOld = cognomeNomeMag;
					// test per cambio anno
					if (annoOld != smsm.getAnno().intValue()) {
						nColAnno++;
						nRow = nRowAnno;
						// scrittura anno
						row = sheet.getRow(nRowAnno);
						if (row == null)
							row = sheet.createRow(nRowAnno);

						setCell(row, nColAnno, smsm.getAnno().doubleValue(), csBold);
					}
					annoOld = smsm.getAnno().intValue();

					nRow++;

					// scrittura tipologia e valore
					row = sheet.getRow(nRow);
					if (row == null)
						row = sheet.createRow(nRow);

					setCell(row, 0, smsm.getDescrMotivo(), style);
					setCell(row, nColAnno, smsm.getConta().doubleValue(), style);

					// serve per segnare le righe buone ai fini del conteggio finale
					colonne = nColAnno;
					if (colonne == 1) {
						righeBuone[k] = nRow;
						if (k < righeBuone.length)
							k++;
					}
					Vector motivi = cercaMotivi(smsm.getCodAttivita());
					nRow++;
					row = sheet.getRow(nRow);
					if (row == null)
						row = sheet.createRow(nRow);

					setCell(row, 0, "DI CUI", cs);

					Iterator itx1 = motivi.iterator();
					while (itx1.hasNext()) {
						StatisticheMSModel smsm1 = (StatisticheMSModel) itx1.next();
						nRow++;
						row = sheet.getRow(nRow);
						if (row == null)
							row = sheet.createRow(nRow);

						setCell(row, 0, "   " + smsm1.getDescrMotivo(), cs);
						int totaleMotivi = 0;

						// ciclo esterno
						Iterator est2 = tuttiMagsAnno.iterator();
						while (est2.hasNext()) {
							Vector<StatisticheMSModel>[] motiviMagAnno = (Vector<StatisticheMSModel>[]) est2
									.next();
							Iterator itx2 = motiviMagAnno[i].iterator();
							while (itx2.hasNext()) {
								StatisticheMSModel smsm2 = (StatisticheMSModel) itx2.next();
								if ("MAGISTRATO NON ASSEGNATO".equals(cognomeNomeMag)) {
									if (smsm2.getAnno().equals(smsm.getAnno())
											&& smsm2.getCodAttivita().equals(smsm.getCodAttivita())
											&& smsm2.getCodMotivo().equals(smsm1.getCodMotivo())
											&& smsm2.getCodMagistrato() == null) {
										if (smsm1.getTipoMS() != null) {
											if (smsm1.getTipoMS().equals(smsm2.getTipoMS()))
												totaleMotivi = smsm2.getConta();
										} else
											totaleMotivi = smsm2.getConta();
									}
								} else {
									if (smsm2.getAnno().equals(smsm.getAnno())
											&& smsm2.getCodAttivita().equals(smsm.getCodAttivita())
											&& smsm2.getCodMotivo().equals(smsm1.getCodMotivo())) {
										if (smsm1.getTipoMS() != null) {
											if (smsm1.getTipoMS().equals(smsm2.getTipoMS()))
												totaleMotivi = smsm2.getConta();
										} else
											totaleMotivi = smsm2.getConta();
									}
								}
							}
						}
						setCell(row, nColAnno, totaleMotivi, cs);
					}
				}
			}
			contEst++;
		}

		// fuori ciclo
		// Richiamo metodo per la scrittura delle formule contenenti le somme parziali e generali
		nColAnno++;

		nRow = setTotaliAttivitaMagistrati(sheet, csBold, nRow, nRowAnno, nColAnno, righeBuone, style,
				stylered);
	}

	public Vector<StatisticheMSModel>[] contaMotiviAnno(String dataIniziale, String dataFinale)
			throws Exception {

		Connection c = null;
		Vector<StatisticheMSModel> vTemp;
		StatisticheMSSqlDAO smssdao = null;
		String range = calcolaRange(dataIniziale, dataFinale);
		String[] s = range.split(",");
		Vector[] v = new Vector[s.length];
		int cont = 0;

		try {
			c = getDBConnection();
			dataIniziale = dataIniziale.replaceAll("/", "");
			dataFinale = dataFinale.replaceAll("/", "");
			int annoIniziale = new Integer(dataIniziale.substring(4)).intValue();
			int annoFinale = new Integer(dataFinale.substring(4)).intValue();
			for (int anno = annoIniziale; anno <= annoFinale; anno++) {
				vTemp = new Vector<>();
				smssdao = new StatisticheMSSqlDAO(c);
				smssdao.ricercaTotaleMotivoPerAnno(anno);
				smssdao.start();
				while (smssdao.next())
					vTemp.add(smssdao.getModelTotaliPerAnno(""));
				v[cont] = vTemp;
				cont++;
			}
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new DAOException("StatisticheMSController.contaMotiviAnno. Non posso leggere: " + daoEx);
		} finally {
			cleanup(smssdao);
			cleanup(c);
		}

		// valore di ritorno
		return v;
	}

	public Vector<StatisticheMSModel>[] contaMotiviMagAnno(String dataIniziale, String dataFinale,
			String codMagistrato) throws Exception {

		Connection c = null;
		Vector<StatisticheMSModel> vTemp;
		StatisticheMSSqlDAO smssdao = null;
		String range = calcolaRange(dataIniziale, dataFinale);
		String[] s = range.split(",");
		Vector[] v = new Vector[s.length];
		int cont = 0;

		try {
			c = getDBConnection();
			dataIniziale = dataIniziale.replaceAll("/", "");
			dataFinale = dataFinale.replaceAll("/", "");
			int annoIniziale = new Integer(dataIniziale.substring(4)).intValue();
			int annoFinale = new Integer(dataFinale.substring(4)).intValue();
			for (int anno = annoIniziale; anno <= annoFinale; anno++) {
				vTemp = new Vector<>();
				smssdao = new StatisticheMSSqlDAO(c);
				smssdao.ricercaTotaleMotivoPerMagistratoAnno(anno, codMagistrato);
				smssdao.start();
				while (smssdao.next())
					vTemp.add(smssdao.getModelTotaliPerAnno("MAG"));
				v[cont] = vTemp;
				cont++;
			}
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new DAOException("StatisticheMSController.contaMotiviMagAnno. Non posso leggere: " + daoEx);
		} finally {
			cleanup(smssdao);
			cleanup(c);
		}

		// valore di ritorno
		return v;
	}

	private int setTotaliAttivitaMagistrati(HSSFSheet sheet, HSSFCellStyle csBold, int nRow, int nRowAnno,
			int nColAnno, int[] righeBuone, HSSFCellStyle style, HSSFCellStyle stylered) {

		HSSFRow row = null;

		String somma = "";
		int forRow = 0;
		int RRiga = 0;
		RRiga = nRow;
		int coltot = 0;
		coltot = nColAnno;
		coltot = coltot - 1;
		nRow++;
		row = sheet.getRow(nRow);
		if (row == null)
			row = sheet.createRow(nRow);

		setCell(row, 0, "TOTALE", csBold);
		nRow = RRiga;

		// Ciclo per scrivere il totale per tipologia per tutti gli anni
		// tranne le righe dove c'e' "DI CUI"
		for (forRow = nRowAnno; forRow < nRow + 1; forRow++) {
			row = sheet.getRow(forRow);
			if (row == null)
				row = sheet.createRow(forRow);

			String campo = "" + row.getCell(0);
			if (forRow == nRowAnno) {
				setCell(row, nColAnno, "TOTALE", csBold);
			} else {
				if (!campo.equals("DI CUI")) {
					somma = getStringaSomma(forRow, 1, forRow, nColAnno - 1);
					boolean rigaTipologia = false;
					for (int k = 0; k < righeBuone.length; k++) {
						if (forRow == righeBuone[k]) {
							// la riga corrente e' la riga del totale Tipologia
							rigaTipologia = true;
							break;
						}
					}
					if (rigaTipologia)
						setFormulaCell(row, nColAnno, somma, style); // blu
					else
						setFormulaCell(row, nColAnno, somma, stylered); // rosso
				}
			}
		}

		nRow++;
		// Totale generale
		row = sheet.getRow(nRow);
		if (row == null)
			row = sheet.createRow(nRow);
		for (int J = 1; J < (nColAnno); J++) {
			somma = getStringaSommaTipo(nRowAnno + 1, J, forRow - 1, J, righeBuone);
			setFormulaCell(row, J, somma, style);
		}

		somma = getStringaSommaTipo(nRowAnno + 1, nColAnno, forRow - 1, nColAnno, righeBuone);
		setFormulaCell(row, nColAnno, somma, style);

		// valore di ritorno
		return nRow;
	}

	private String getStringaSommaTipo(int nRow1, int nCol1, int nRow2, int nCol2, int[] righeBuone) {

		CellReference cellRef1 = new CellReference(nRow1, nCol1);
		String formula = "SUM(" + cellRef1.formatAsString();
		for (int k = 1; k < righeBuone.length; k++) {
			cellRef1 = new CellReference(righeBuone[k], nCol1);
			formula += "+" + cellRef1.formatAsString();
		}
		formula += ")";

		// valore di ritorno
		return formula;
	}

	public Vector<StatoFascicoloResModel> ricercaStatiFascicoloResMS() throws Exception {

		Connection c = null;
		Vector<StatoFascicoloResModel> v = new Vector<>();
		StatoFascicoloResMSDAO sfrdao = null;

		try {
			c = getDBConnection();
			sfrdao = new StatoFascicoloResMSDAO(c);
			sfrdao.setOrdinamento("ORDINAMENTO");
			sfrdao.start();
			while (sfrdao.next()) {
				StatoFascicoloResModel lStatoModel = (StatoFascicoloResModel) sfrdao.getModel();
				if (!ICostantiStatis.TIPO_CAMPO_TITOLO1.equals(lStatoModel.getTipoCampo())
						&& !ICostantiStatis.TIPO_CAMPO_TITOLO2.equals(lStatoModel.getTipoCampo())) {
					v.add(lStatoModel);
				}
			}
			if (v.size() == 0)
				throw new F3BException(F3BException.USER_MESSAGE, "Nessun Elemento trovato");
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new DAOException(
					"StatisticheMSController.RicercaStatiFascicoloRes: Non posso leggere : " + daoEx);
		} finally {
			cleanup(sfrdao);
			cleanup(c);
		}
		return v;
	}

	public Vector ExGetCountRiepilogoIspProvvedimentiMS(IspProvvedimentiModel aIspProvvedimenti)
			throws Exception {
		Connection lConn = null;
		Vector lIspProvvedimenti = new Vector();
		IspProvvedimentiSqlDAO lIspDao = null;

		try {

			lConn = getDBConnection();
			lIspDao = new IspProvvedimentiSqlDAO(lConn);

			lIspDao.getCountRiepilogoIspProvvedimentiMS(aIspProvvedimenti);

			lIspDao.start();
			while (lIspDao.next()) {
				lIspProvvedimenti.add(lIspDao.getModelCountRiepilogo());
			}
			if (lIspProvvedimenti.size() == 0) {
				throw new F3BException(F3BException.USER_MESSAGE, "Nessun Elemento trovato");
			}
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new DAOException(
					"StatisController.ExGetCountRiepilogoIspProvvedimenti: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lIspDao);
			cleanup(lConn);
		}
		return lIspProvvedimenti;
	}

	/**
	 * Metodo che crea il foglio 'RIEPILOGO' del Riepilogo Ispettivo SIEP
	 *
	 * @param aIspModVect
	 *            <IspProvvedimentiModel>
	 * @param wb
	 * @param uffUteConnesso
	 * @param UffScelto
	 * @throws F3BException
	 */
	public void ExCreateRiepilogoIspProvvedimentiMS(Vector aIspModVect, HSSFWorkbook wb,
			UfficioModel uffUteConnesso, String UffScelto) {

		// Stile della cella vuoto
		HSSFCellStyle csNull = wb.createCellStyle();

		// Stile della cella con bordi
		HSSFCellStyle cs = getBordo4Lati(wb);

		// Stile della cella con bordi e grassetto
		HSSFCellStyle csBold = getBordo4Lati(wb);
		// Create a new font and alter it.
		HSSFFont fontBold = wb.createFont();
		fontBold.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		csBold.setFont(fontBold);

		// Titolo 1
		HSSFCellStyle csTitolo1 = getBordo4Lati(wb);

		csTitolo1.setAlignment(HSSFCellStyle.ALIGN_LEFT);
		csTitolo1.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		csTitolo1.setFillForegroundColor(HSSFColor.GREEN.index);
		csTitolo1.setFont(fontBold);

		// Titolo 2
		HSSFCellStyle csTitolo2 = getBordo4Lati(wb);

		csTitolo2.setAlignment(HSSFCellStyle.ALIGN_LEFT);
		csTitolo2.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		csTitolo2.setFillForegroundColor(HSSFColor.YELLOW.index);
		csTitolo2.setFont(fontBold);

		HSSFPalette palette = wb.getCustomPalette();
		palette.setColorAtIndex(HSSFColor.GREEN.index, (byte) 204, (byte) 255, (byte) 204);

		// primo foglio
		HSSFSheet sheet = wb.createSheet("Riepilogo");
		// larghezza colonne
		sheet.setColumnWidth(0, (110 * 256));
		sheet.setColumnWidth(1, (20 * 256));

		// Intestazione del foglio excel
		int nRow = 0;
		if (UffScelto.equals("-"))
			nRow = setIntestazione(sheet, uffUteConnesso, csNull);
		else
			nRow = setIntestazione(sheet, uffUteConnesso, csNull, UffScelto);

		nRow++;
		nRow++;

		// Create a row and put some cells in it. Rows are 0 based.
		HSSFRow row = sheet.createRow(nRow);
		setCell(row, 0,
				"PROSPETTO RIEPILOGATIVO DELLE ESECUZIONI PENDENTI ED IN CORSO DISTINTE SECONDO LO STATO DELLA PROCEDURA",
				csNull);

		nRow++;
		nRow++;

		row = sheet.createRow(nRow);
		setCell(row, 0, "", cs);
		setCell(row, 1, "TOTALE PROCEDURE", cs);

		Iterator itx = aIspModVect.iterator();

		nRow++;

		Integer lContaArchiviati = 0;
		while (itx.hasNext()) {
			IspProvvedimentiModel lMod = (IspProvvedimentiModel) itx.next();

			if (ICostantiStatis.TIPO_CAMPO_TITOLO1.equals(lMod.getTipoCampo())
					|| ICostantiStatis.TIPO_CAMPO_TITOLO2.equals(lMod.getTipoCampo())) {
				// Se non si vogliono visualizzare i 'Titoli' sul RIEPILOGO
				// continue;
			}

			if (lMod.getCodStatoFascicoloRes().intValue() == Integer
					.parseInt(ICostantiStatis.COD_ARCHIVIAZIONI_MS)) {
				lContaArchiviati = lMod.getConta();
				siesLogger.debug("Conta Archiviati: " + "" + lContaArchiviati);
			}

			row = sheet.createRow(nRow);
			if (ICostantiStatis.TIPO_CAMPO_TITOLO1.equals(lMod.getTipoCampo())) {
				setCell(row, 0, lMod.getDescrStatoFascicoloRes(), csTitolo1);
				setCell(row, 1, "", cs);
				sheet.addMergedRegion(new CellRangeAddress(nRow, nRow, 0, 1));
			} else if (ICostantiStatis.TIPO_CAMPO_TITOLO2.equals(lMod.getTipoCampo())) {
				setCell(row, 0, lMod.getDescrStatoFascicoloRes(), csTitolo2);
				setCell(row, 1, "", cs);
				sheet.addMergedRegion(new CellRangeAddress(nRow, nRow, 0, 1));
			} else {
				setCell(row, 0, lMod.getDescrStatoFascicoloRes(), cs);
				setCell(row, 1, lMod.getConta().doubleValue(), cs);
			}
			nRow++;
		}

		row = sheet.createRow(nRow);
		setCell(row, 0, "", cs);
		setCell(row, 1, "", cs);

		// // ===============================
		// // TOTALE PROCEDIMENTI IN CORSO
		// nRow++;
		// row = sheet.createRow(nRow);
		// setCell(row, 0, "TOTALE PROCEDIMENTI IN CORSO", csBold);
		// setFormulaCell(row, 1, "SUM(B" + (rifRow + 1) + ":B" + (nRow - 1) + ")", csBold);
		//
		// // TOTALE PROCEDIMENTI DEFINITI (SU TUTTA LA BASE DATI)
		// nRow++;
		// row = sheet.createRow(nRow);
		// setCell(row, 0, "TOTALE PROCEDIMENTI DEFINITI (SU TUTTA LA BASE DATI)", csBold);
		// setCell(row, 1, lContaArchiviati, csBold);
		//
		// // TOTALE GENERALE
		// nRow++;
		// row = sheet.createRow(nRow);
		// setCell(row, 0, "TOTALE GENERALE", csBold);
		// setFormulaCell(row, 1, "SUM(B" + (nRow - 1) + ":B" + (nRow) + ")", csBold);
		// // ==============
	}

	/*
	 * ISSUE MEV : aggiunti metodi per scelta "TUTTI I MAGISTRATI" Numero MEV : 39 Autore : Gioggi Data : 13
	 * nov 2019 Branch : MEV_39
	 */
	public void creaRiepilogoProcedimentiPendentiPeriodoVett(Vector tuttiPrimoFoglioMagManip, HSSFWorkbook wb,
			UfficioModel ufficioUtenteConnesso, String dataIniziale, String dataFinale, String descrComune,
			Vector tuttiMagsCognomeNome) {

		// ciclo esterno
		Iterator est = tuttiPrimoFoglioMagManip.iterator();
		int contEst = 0;
		int nRowAppo = 0;
		boolean isFirstTime = true;
		HSSFSheet sheetAppo = null;
		while (est.hasNext()) {
			Vector<StatisticheMSModel> v = (Vector<StatisticheMSModel>) est.next();
			if (v != null && !v.isEmpty()) {
				HSSFCellStyle csNull = wb.createCellStyle();

				// stile per celle col bordo
				HSSFCellStyle cs = getBordo4Lati(wb);

				// stile per celle col bordo con carattere grassetto
				HSSFCellStyle csBold = getBordo4Lati(wb);
				// Create a new font and alter it.
				HSSFFont font = wb.createFont();
				font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
				csBold.setFont(font);

				HSSFCellStyle csBoldCenter = getBordo4Lati(wb);
				csBoldCenter.setFont(font);
				csBoldCenter.setAlignment(HSSFCellStyle.ALIGN_CENTER);

				// primo foglio; RIEPILOGO_ISCRIZIONI
				HSSFSheet sheet = sheetAppo;
				// if ("".equals(cognomeNomeMag))
				// sheet = wb.createSheet("Riepilogo Iscrizioni");
				// else
				int nRow = nRowAppo;
				HSSFRow row;
				if (isFirstTime) {
					isFirstTime = false;
					sheet = wb.createSheet("Riepilogo Iscrizioni Mag");
					sheet.setColumnWidth(0, (75 * 256));

					// int nRow = 0;

					// Intestazione del foglio excel
					nRow = setIntestazione(sheet, ufficioUtenteConnesso, csNull, descrComune);

					nRow++;
					nRow++;

					row = sheet.createRow(nRow);
					setCell(row, 0, "Riepilogo Iscrizioni relativo al periodo dal " + dataIniziale + " al "
							+ dataFinale, csNull);
				}
				nRow++;
				nRow++;

				String cognomeNomeMag = StringUtils.toStringJSP(tuttiMagsCognomeNome.get(contEst));
				if (!"".equals(cognomeNomeMag)) {
					row = sheet.createRow(nRow);
					setCell(row, 0, "Statistiche relative al Magistrato: " + cognomeNomeMag, csNull);
					nRow++;
					nRow++;
				}

				// Tipologia tempi
				String tipologia = "RIEPILOGO ISCRIZIONI MISURE DI SICUREZZA";
				int colonnaIniziale = 0;

				// creazione riga con altezza per wraptext
				row = sheet.getRow(nRow);
				if (row == null)
					row = sheet.createRow(nRow);
				csBoldCenter.setWrapText(true);
				setCell(row, colonnaIniziale, tipologia, csBoldCenter);
				for (int a = 0; a < v.get(0).getAnni().size(); a++) {
					colonnaIniziale++;
					setCell(row, colonnaIniziale, v.get(0).getAnni().get(a).intValue(), csBoldCenter);
				}

				// int nRowAnno = nRow;
				int nColAnno = 0;

				// inizio ciclo di scrittura dei dati
				Iterator itx = v.iterator();
				while (itx.hasNext()) {
					StatisticheMSModel smsm = (StatisticheMSModel) itx.next();

					int jump = 0;
					nColAnno = 0;
					// ciclo per scrittura colonne
					for (int i = 0; i < smsm.getAnni().size(); i++) {
						nColAnno++;
						if (jump == 0)
							nRow++;
						// scrittura tipologia e valore
						row = sheet.getRow(nRow);
						if (row == null)
							row = sheet.createRow(nRow);
						setCell(row, 0, smsm.getTipoMS(), cs);
						setCell(row, nColAnno, smsm.getIscrittiParziali().get(i).doubleValue(), cs);
						jump += 1;
					}
				}
				nRowAppo = nRow;
				sheetAppo = sheet;

				// 20191122 [SG]: tolta riga "Totali Iscritti nel Periodo"
				// nRow++;
				// // scrittura totale anno
				// row = sheet.getRow(nRow);
				// if (row == null)
				// row = sheet.createRow(nRow);
				// setCell(row, 0, "Totali Iscritti nel Periodo", csBoldCenter);
				// int colonnaFinale = 1;
				// for (int a = 0; a < v.get(0).getAnni().size(); a++) {
				// String formula = getStringaSomma(nRowAnno + 1, colonnaFinale, nRow - 1, colonnaFinale);
				// setFormulaCell(row, colonnaFinale, formula, csBoldCenter);
				// colonnaFinale++;
				// }
				// 20191125 [SG]: tolta ulitma colonna "Totali"
				// fuori ciclo
				// Richiamo metodo per la scrittura delle formule contenenti
				// le somme parziali e generali
				// nColAnno++;
				// nRow = setTotaliTempi(sheet, csBold, csBoldCenter, nRow, nRowAnno, nColAnno);
				// nRow++;
			}
			contEst++;
		}
	}

	public void creaRiepilogoPPPTipologiaMisuraVett(Vector tuttiSecondoFoglioMag, HSSFWorkbook wb,
			UfficioModel ufficioUtenteConnesso, String dataIniziale, String dataFinale, String descrComune,
			Vector tuttiMagsCognomeNome) {

		// ciclo esterno
		Iterator est = tuttiSecondoFoglioMag.iterator();
		int contEst = 0;
		int nRowAppo = 0;
		boolean isFirstTime = true;
		HSSFSheet sheetAppo = null;
		while (est.hasNext()) {
			Vector<StatisticheMSModel> v = (Vector<StatisticheMSModel>) est.next();
			if (v != null && !v.isEmpty()) {
				HSSFCellStyle csNull = wb.createCellStyle();

				// stile per celle col bordo
				HSSFCellStyle cs = getBordo4Lati(wb);

				// stile per celle col bordo con carattere grassetto
				HSSFCellStyle csBold = getBordo4Lati(wb);
				// Create a new font and alter it.
				HSSFFont font = wb.createFont();
				font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
				csBold.setFont(font);

				HSSFCellStyle csBoldCenter = getBordo4Lati(wb);
				csBoldCenter.setFont(font);
				csBoldCenter.setAlignment(HSSFCellStyle.ALIGN_CENTER);

				// primo foglio; Tipologia MS
				HSSFSheet sheet = sheetAppo;
				// if ("".equals(cognomeNomeMag))
				// sheet = wb.createSheet("Tipologia MS");
				// else
				int nRow = nRowAppo;
				HSSFRow row;
				if (isFirstTime) {
					isFirstTime = false;
					sheet = wb.createSheet("Tipologia MS Mag");
					sheet.setColumnWidth(0, (75 * 256));

					// int nRow = 0;

					// Intestazione del foglio excel
					nRow = setIntestazione(sheet, ufficioUtenteConnesso, csNull, descrComune);

					nRow++;
					nRow++;

					row = sheet.createRow(nRow);
					setCell(row, 0, "Riepilogo Iscrizioni relativo al periodo dal " + dataIniziale + " al "
							+ dataFinale, csNull);
				}
				nRow++;
				nRow++;

				String cognomeNomeMag = StringUtils.toStringJSP(tuttiMagsCognomeNome.get(contEst));
				if (!"".equals(cognomeNomeMag)) {
					row = sheet.createRow(nRow);
					setCell(row, 0, "Statistiche relative al Magistrato: " + cognomeNomeMag, csNull);
					nRow++;
					nRow++;
				}

				// Tipologia tempi
				String tipologia = "TIPO MISURA DI SICUREZZA";
				int colonnaIniziale = 0;

				// creazione riga con altezza per wraptext
				row = sheet.getRow(nRow);
				if (row == null)
					row = sheet.createRow(nRow);
				csBoldCenter.setWrapText(true);
				setCell(row, colonnaIniziale, tipologia, csBoldCenter);
				for (int a = 0; a < v.get(0).getPeriodi().size(); a++) {
					colonnaIniziale++;
					sheet.setColumnWidth(colonnaIniziale, (15 * 256));
					String testo = v.get(0).getPeriodi().get(a).replace("'", "");
					setCell(row, colonnaIniziale, testo, csBoldCenter);
				}

				int nRowAnno = nRow;
				int nColAnno = 0;

				// inizio ciclo di scrittura dei dati
				Iterator itx = v.iterator();
				while (itx.hasNext()) {
					StatisticheMSModel smsm = (StatisticheMSModel) itx.next();

					int jump = 0;
					nColAnno = 0;
					// ciclo per scrittura colonne
					for (int i = 0; i < smsm.getPeriodi().size(); i++) {
						nColAnno++;
						if (jump == 0)
							nRow++;
						// scrittura tipologia e valore
						row = sheet.getRow(nRow);
						if (row == null)
							row = sheet.createRow(nRow);
						setCell(row, 0, smsm.getTipoMS(), cs);
						setCell(row, nColAnno, smsm.getIscrittiParziali().get(i).doubleValue(), cs);
						jump += 1;
					}
				}

				// 20191122 [SG]: tolta riga "Totali Iscritti nel Periodo"
				// nRow++;
				// // scrittura totale anno
				// row = sheet.getRow(nRow);
				// if (row == null)
				// row = sheet.createRow(nRow);
				// setCell(row, 0, "Totali Iscritti nel Periodo", csBoldCenter);
				// int colonnaFinale = 1;
				// for (int a = 0; a < v.get(0).getPeriodi().size(); a++) {
				// String formula = getStringaSomma(nRowAnno + 1, colonnaFinale, nRow - 1, colonnaFinale);
				// setFormulaCell(row, colonnaFinale, formula, csBoldCenter);
				// colonnaFinale++;
				// }

				// fuori ciclo
				// Richiamo metodo per la scrittura delle formule contenenti
				// le somme parziali e generali
				nColAnno++;
				nRow = setTotaliTempi(sheet, csBold, csBoldCenter, nRow, nRowAnno, nColAnno);
				nRow++;
				nRowAppo = nRow;
				sheetAppo = sheet;
			}
			contEst++;
		}
	}

	public void creaDettaglioProcedimentiPendentiPeriodoVett(Vector tuttiTerzoFoglio, HSSFWorkbook wb,
			UfficioModel ufficioUtenteConnesso, String dataIniziale, String dataFinale, String descrComune,
			String tipo, Vector tuttiMagsCognomeNome) {

		// ciclo esterno
		Iterator est = tuttiTerzoFoglio.iterator();
		int contEst = 0;
		int nRowAppo = 0;
		boolean isFirstTime = true;
		HSSFSheet sheetAppo = null;
		while (est.hasNext()) {
			Vector<StatisticheMSModel> v = (Vector<StatisticheMSModel>) est.next();
			if (v != null && !v.isEmpty()) {
				// Stile della cella vuoto
				HSSFCellStyle csNull = wb.createCellStyle();
				// Stile della cella grassetto
				HSSFCellStyle csBold = wb.createCellStyle();
				// Create a new font and alter it
				HSSFFont fontBold = wb.createFont();
				fontBold.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
				csBold.setFont(fontBold);

				// Stile della cella con bordi ed allineamento a destra
				HSSFCellStyle csC = getBordo4Lati(wb);
				csC.setAlignment(HSSFCellStyle.ALIGN_CENTER);

				// stile per celle col bordo con carattere grassetto
				// ALLINEATO AL CENTRO
				HSSFCellStyle csBoldCenter = getBordo4Lati(wb);
				csBoldCenter.setFont(fontBold);
				csBoldCenter.setAlignment(HSSFCellStyle.ALIGN_CENTER);

				// Stile Titolo 1
				HSSFCellStyle csTitolo1 = getBordo4Lati(wb);

				csTitolo1.setAlignment(HSSFCellStyle.ALIGN_CENTER);
				csTitolo1.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
				csTitolo1.setFillForegroundColor(HSSFColor.GREEN.index);
				csTitolo1.setFont(fontBold);

				// Stile Titolo 2
				HSSFCellStyle csTitolo2 = getBordo4Lati(wb);

				csTitolo2.setAlignment(HSSFCellStyle.ALIGN_CENTER);
				csTitolo2.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
				csTitolo2.setFillForegroundColor(HSSFColor.YELLOW.index);
				csTitolo2.setFont(fontBold);

				// Utilizzo colore non standard (204,255,204)
				HSSFPalette palette = wb.getCustomPalette();
				palette.setColorAtIndex(HSSFColor.GREEN.index, (byte) 204, (byte) 255, (byte) 204);

				int nRow = nRowAppo;
				HSSFSheet sheet = sheetAppo;
				HSSFRow row;
				if (isFirstTime) {
					isFirstTime = false;
					sheet = wb.createSheet("Dettagli " + tipo);

					// int nRow = 0;
					if ("-".equals(descrComune))
						nRow = setIntestazione(sheet, ufficioUtenteConnesso, csNull);
					else
						nRow = setIntestazione(sheet, ufficioUtenteConnesso, csNull, descrComune);

					nRow++;
					nRow++;

					row = sheet.createRow(nRow);
					setCell(row, 0, "Intervallo Date Ricerca dal " + dataIniziale + " al " + dataFinale,
							csNull);
				}
				sheet.setDefaultColumnWidth(30);
				nRow++;
				nRow++;

				String cognomeNomeMag = StringUtils.toStringJSP(tuttiMagsCognomeNome.get(contEst));
				if (!"".equals(cognomeNomeMag)) {
					row = sheet.createRow(nRow);
					setCell(row, 0, "Statistiche relative al Magistrato: " + cognomeNomeMag, csNull);
				}

				int nCell = 0;
				int maxCellxRow = 8;
				String tipoMS = "";

				Iterator itx = v.iterator();
				while (itx.hasNext()) {
					StatisticheMSModel smsm = (StatisticheMSModel) itx.next();

					if (!tipoMS.equals(smsm.getTipoMS())) {
						nRow++;
						nRow++;
						row = sheet.createRow(nRow);
						setCell(row, 0, smsm.getTipoMS(), csBold);
						nRow++;
						nRow++;
					} else
						nRow++;
					tipoMS = smsm.getTipoMS();

					row = sheet.createRow(nRow);
					if ("-".equals(descrComune))
						setCell(row, 0,
								"ANNO " + StringUtils.toStringJSP(smsm.getAnno()) + " - " + descrComune,
								csNull);
					else
						setCell(row, 0, "ANNO " + StringUtils.toStringJSP(smsm.getAnno()), csNull);

					nCell = 0;
					nRow++;

					row = sheet.getRow(nRow);
					if (row == null)
						row = sheet.createRow(nRow);

					// =======================================
					// Scrivo il Numero Fascicolo
					// =======================================
					for (int i = 0; i < smsm.getProgFasc().size(); i++) {
						// 24/11/2019 (INTERVENTO POST COLLAUDO 11.3) : GESTIONE MISURE PROVVISORIE
						String annoNumero = StringUtils.toStringJSP(smsm.getProgFasc().get(i)); // es:
																								// -201640012
																								// (se arriva
																								// un numero
																								// negativo
																								// indicano
																								// che la
																								// misura è
																								// provvisoria)
						if (annoNumero.startsWith("-")) {
							annoNumero = annoNumero.substring(1, 5) + "/"
									+ annoNumero.substring(5, annoNumero.length()) + " (Misura Provvisoria)";
						} else {
							annoNumero = annoNumero.substring(0, 4) + "/"
									+ annoNumero.substring(4, annoNumero.length());
						}
						setCell(row, nCell, annoNumero, csC);
						if (nCell == maxCellxRow) {
							nCell = 0;
							nRow++;
							row = sheet.createRow(nRow);
						} else {
							nCell++;
						}
					}

					nRow++;
					setRowTotaliProvvedimenti(sheet, nRow, "TOTALE ANNO", csBoldCenter, csNull,
							smsm.getTotFasc().intValue());
				} // end while
				nRowAppo = nRow;
				sheetAppo = sheet;
			}
			contEst++;
		}
	}
	// ***** FINE INTERVENTO MEV_39 *****//

}