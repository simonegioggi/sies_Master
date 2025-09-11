package siap.util.excel;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;

import f3b.util.Utils;
import f3b.util.excel.ExcelProducer;
import siap.sico.ufficio.model.UfficioModel;

public class SIAPExcelProducer extends ExcelProducer {

	protected int setIntestazione(HSSFSheet sheet, UfficioModel uffUteConnesso, HSSFCellStyle csNull) {

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

		// [SG] 22/05/2023 se è null scrivo "" altrimenti appare "null" sul foglio Excel
		value = ("Tel. " + (Utils.isPresent(uffUteConnesso.getTelefono()) ? uffUteConnesso.getTelefono() : "")
				+ " - Fax " + (Utils.isPresent(uffUteConnesso.getFax()) ? uffUteConnesso.getFax() : ""));
		setCell(row, (short) 0, value, csNull);

		return nRow;
	}

}