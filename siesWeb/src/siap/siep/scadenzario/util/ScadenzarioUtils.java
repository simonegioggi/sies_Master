package siap.siep.scadenzario.util;

import java.util.Date;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import f3b.util.DateUtils;
import siap.sico.ufficio.model.UfficioModel;

public class ScadenzarioUtils {

	public static String getDifferenza(Date aGrande, Date aPiccolo) {

		String lRisultato = "";

		int lDiffGiorni;
		int lDiffMesi;
		int lDiffAnni;
		int lGiornoGrande = Integer.parseInt(DateUtils.getDateToString(aGrande, "dd"));
		int lMeseGrande = Integer.parseInt(DateUtils.getDateToString(aGrande, "MM"));
		int lAnnoGrande = Integer.parseInt(DateUtils.getDateToString(aGrande, "yyyy"));
		int lGiornoPiccolo = Integer.parseInt(DateUtils.getDateToString(aPiccolo, "dd"));
		int lMesePiccolo = Integer.parseInt(DateUtils.getDateToString(aPiccolo, "MM"));
		int lAnnoPiccolo = Integer.parseInt(DateUtils.getDateToString(aPiccolo, "yyyy"));
		int lUltimo = Integer
				.parseInt(DateUtils.getDateToString(DateUtils.getEndOfMonth(lAnnoGrande, lMeseGrande), "dd"));

		if (lGiornoGrande >= lGiornoPiccolo) {
			lDiffGiorni = lGiornoGrande - lGiornoPiccolo;
		} else {
			lDiffGiorni = (lGiornoGrande + lUltimo) - lGiornoPiccolo;
			lMeseGrande--;
		}

		if (lMeseGrande >= lMesePiccolo) {
			lDiffMesi = lMeseGrande - lMesePiccolo;
		} else {
			lDiffMesi = lMeseGrande - lMesePiccolo + 12;
			lAnnoGrande--;
		}

		lDiffAnni = lAnnoGrande - lAnnoPiccolo;

		if (lDiffAnni != 0) {
			lRisultato += lDiffAnni + " Anni ";
		}

		if (lDiffMesi != 0) {
			lRisultato += lDiffMesi + " Mesi ";
		}

		if (lDiffGiorni != 0) {
			lRisultato += lDiffGiorni + " Giorni";
		}

		if (lRisultato.equals("")) {
			lRisultato = "Oggi";
		}

		return lRisultato;
	}

	public static HSSFCellStyle getBordo4Lati(HSSFWorkbook wb) {

		HSSFCellStyle cs = wb.createCellStyle();
		cs.setBorderBottom(org.apache.poi.ss.usermodel.BorderStyle.THIN);
		cs.setBorderTop(org.apache.poi.ss.usermodel.BorderStyle.THIN);
		cs.setBorderRight(org.apache.poi.ss.usermodel.BorderStyle.THIN);
		cs.setBorderLeft(org.apache.poi.ss.usermodel.BorderStyle.THIN);

		return cs;
	}

	public static int setIntestazione(HSSFSheet sheet, UfficioModel uffUteConnesso, HSSFCellStyle csNull) {

		int nRow = 0;

		// Create a row and put some cells in it. Rows are 0 based.
		HSSFRow row = sheet.createRow(nRow);
		// Create a cell and put a value in it.
		String value = (uffUteConnesso.getDescrTipoUfficio().toUpperCase() + " DI "
				+ uffUteConnesso.getDescrComune().toUpperCase());
		setCell(row, (short) 0, value, csNull);

		nRow++;
		// Create a row and put some cells in it. Rows are 0 based.
		row = sheet.createRow(nRow);
		// Create a cell and put a value in it.

		value = ("Tel. " + uffUteConnesso.getTelefono() + " - Fax " + uffUteConnesso.getFax());
		setCell(row, (short) 0, value, csNull);

		return nRow;
	}

	public static HSSFCell setCell(HSSFRow row, short nCol, String value, HSSFCellStyle cs) {

		HSSFCell cell = row.createCell(nCol);
		cell.setCellValue(value);
		cell.setCellStyle(cs);

		return cell;
	}

}
