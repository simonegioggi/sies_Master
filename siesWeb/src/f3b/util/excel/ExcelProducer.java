package f3b.util.excel;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

public class ExcelProducer {
		
	  protected HSSFCell setCell(HSSFRow row, int nCol, String value, HSSFCellStyle cs) {
		  HSSFCell cell = row.createCell(nCol);
		  cell.setCellValue(value);
		  cell.setCellStyle(cs);
		  return cell;
	  }

	  protected HSSFCell setCell(HSSFRow row, int nCol, double value, HSSFCellStyle cs) {
		  HSSFCell cell = row.createCell(nCol);
		  cell.setCellValue(value);
		  cell.setCellStyle(cs);
		  return cell;
	  }

	  protected HSSFCell setCell(HSSFRow row, short nCol, String value, HSSFCellStyle cs){
		  HSSFCell cell = row.createCell(nCol);
		  cell.setCellValue(value);
		  cell.setCellStyle(cs);
		  return cell;
	  }

	  protected HSSFCell setCell(HSSFRow row, short nCol, double value, HSSFCellStyle cs){
		  HSSFCell cell = row.createCell(nCol);
		  cell.setCellValue(value);
		  cell.setCellStyle(cs);
		  return cell;
	  }

	  protected HSSFCell setFormulaCell(HSSFRow row, short nCol, String value, HSSFCellStyle cs) {
		  HSSFCell cell = row.createCell(nCol);
		  //cell.setCellType(HSSFCell.CELL_TYPE_FORMULA);
		  cell.setCellFormula(value);
		  cell.setCellStyle(cs);
		  return cell;
	  }
	  
	  protected HSSFCellStyle getBordo4Lati(HSSFWorkbook wb) {
		  HSSFCellStyle cs = wb.createCellStyle();
		  cs.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		  cs.setBorderTop(HSSFCellStyle.BORDER_THIN);
		  cs.setBorderRight(HSSFCellStyle.BORDER_THIN);
		  cs.setBorderLeft(HSSFCellStyle.BORDER_THIN);
	    return cs;
	  }

}