package siap.sius.fascicolo.util;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import siap.sico.ufficio.model.UfficioModel;

public class HSSFUtils {
    
    private static HSSFUtils mHSSFUtils;
    
    private HSSFUtils() { 
    }
    
    public static HSSFUtils getInstance() {
       if(mHSSFUtils == null) {
           mHSSFUtils = new HSSFUtils();
       }
       return mHSSFUtils;
    }
	
	public HSSFCellStyle getBordo4Lati(HSSFWorkbook wb) {
	    HSSFCellStyle cs = wb.createCellStyle();
	    cs.setBorderBottom(HSSFCellStyle.BORDER_THIN);
	    cs.setBorderTop(HSSFCellStyle.BORDER_THIN);
	    cs.setBorderRight(HSSFCellStyle.BORDER_THIN);
	    cs.setBorderLeft(HSSFCellStyle.BORDER_THIN);

	    return cs;
	  }
	  
	  public HSSFCell setCell(HSSFRow row, int nCol, String value, HSSFCellStyle cs) {
	    HSSFCell cell = row.createCell(nCol);
	    cell.setCellValue(value);
	    cell.setCellStyle(cs);

	    return cell;
	  }

	  public HSSFCell setCell(HSSFRow row, int nCol, double value, HSSFCellStyle cs) {
	    HSSFCell cell = row.createCell(nCol);
	    cell.setCellValue(value);
	    cell.setCellStyle(cs);

	    return cell;
	  }

	  public int setIntestazione(HSSFSheet sheet, UfficioModel uffUteConnesso, HSSFCellStyle csNull) {
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
	  
	  // Duplico setIntestazione per NGG Statistiche SIEP
	  
	  public int setIntestazione(HSSFSheet sheet, UfficioModel uffUteConnesso, HSSFCellStyle csNull, String DescUffIntesta) {
		    
	    int nRow = 0;

	    // Create a row and put some cells in it. Rows are 0 based.
	    HSSFRow row = sheet.createRow(nRow);
	    // Create a cell and put a value in it.
	    String value = (uffUteConnesso.getDescrTipoUfficio().toUpperCase()
	      + " DI " + DescUffIntesta.toUpperCase());
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