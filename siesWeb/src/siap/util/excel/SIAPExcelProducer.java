package siap.util.excel;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;

import siap.sico.ufficio.model.UfficioModel;
import f3b.util.excel.ExcelProducer;

public class SIAPExcelProducer extends ExcelProducer{

	  protected int setIntestazione(HSSFSheet sheet, UfficioModel uffUteConnesso, HSSFCellStyle csNull) {

	    int nRow = 0;

	    // Create a row and put some cells in it. Rows are 0 based.
	    HSSFRow row = sheet.createRow(nRow);
	    // Create a cell and put a value in it.
	    String value = (uffUteConnesso.getDescrTipoUfficio().toUpperCase()
	      + " DI " + uffUteConnesso.getDescrComune().toUpperCase());
	    setCell(row, (short) 0, value, csNull);

	    nRow++;
	    // Create a row and put some cells in it. Rows are 0 based.
	    row = sheet.createRow(nRow);
	    // Create a cell and put a value in it.

	    value = ("Tel. " + uffUteConnesso.getTelefono() + " - Fax "
	      + uffUteConnesso.getFax());
	    setCell(row, (short) 0, value, csNull);

	    return nRow;
	  }	
}