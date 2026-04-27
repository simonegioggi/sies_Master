package siap.siep.calcolopena.action;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.util.CellRangeAddress;

import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.util.StringUtils;
import f3b.web.IWebConstants;
import siap.sico.ufficio.model.UfficioModel;
import siap.siep.circostanza.model.CircostanzaModel;
import siap.siep.fascicolo.model.DettaglioFascicoloModel;
import siap.siep.modulocumulo.model.CircostanzaCumuloModel;
import siap.siep.modulocumulo.model.ReatoCircostanzaCumuloModel;
import siap.siep.modulocumulo.model.ReatoCumuloModel;
import siap.siep.reato.action.ICostantiReato;
import siap.siep.reato.model.ReatoCircostanzaModel;
import siap.siep.reato.model.ReatoModel;

/**
 * Title: ActStampaElencoProcReatoInExcel - Description: Attiva la Ricerca dei Procedimenti per Reato per
 * stamparne l'elenco in un foglio excel.
 *
 * @version 1.0
 */
@SuppressWarnings("rawtypes")
public class ActStampaElencoProcReatoInExcel extends ActElencoProcReato {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	// Intestazione Foglio Excel
	private int setIntestazione(HSSFSheet sheet, UfficioModel uffUteConnesso, HSSFCellStyle csNull) {

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

	private HSSFCell setCell(HSSFRow row, short nCol, String value, HSSFCellStyle cs) {

		HSSFCell cell = row.createCell(nCol);
		cell.setCellValue(value);
		cell.setCellStyle(cs);

		return cell;
	}

	private HSSFCellStyle getBordo4Lati(HSSFWorkbook wb) {

		HSSFCellStyle cs = wb.createCellStyle();
		cs.setBorderBottom(BorderStyle.THIN);
		cs.setBorderTop(BorderStyle.THIN);
		cs.setBorderRight(BorderStyle.THIN);
		cs.setBorderLeft(BorderStyle.THIN);

		return cs;
	}

	private HSSFCellStyle getBordo4LatiBold(HSSFWorkbook wb) {

		HSSFCellStyle cs = wb.createCellStyle();
		cs.setBorderBottom(BorderStyle.THICK);
		cs.setBorderTop(BorderStyle.THICK);
		cs.setBorderRight(BorderStyle.THICK);
		cs.setBorderLeft(BorderStyle.THICK);

		return cs;
	}

	private HSSFCellStyle getNoBordo(HSSFWorkbook wb) {

		HSSFCellStyle cs = wb.createCellStyle();
		cs.setBorderBottom(BorderStyle.NONE);
		cs.setBorderTop(BorderStyle.NONE);
		cs.setBorderRight(BorderStyle.NONE);
		cs.setBorderLeft(BorderStyle.NONE);

		return cs;
	}

	// Restituisce una stringa con la concatenazione dei
	// reati legati al Procedimento
	private String[] calcolaReati(DettaglioFascicoloModel lDettFascicolo) {
		String[] reatoData = new String[2];
		String reati = "";
		String dataReati = "";

		List lReatiCirostanze = lDettFascicolo.getReatiCircostanze();
		if (lReatiCirostanze != null && lReatiCirostanze.size() != 0) {

			Iterator lIterReati = lReatiCirostanze.iterator();
			while (lIterReati.hasNext()) {

				ReatoCircostanzaModel lReatoCircostanza = (ReatoCircostanzaModel) lIterReati.next();
				ReatoModel lReato = lReatoCircostanza.getReato();

				if (lReato.getProgrNumeroManuale() != null && !lReato.getProgrNumeroManuale().equals("")) {
					reati += "Reato " + lReato.getProgrNumeroManuale() + ": ";
					dataReati += "Reato " + lReato.getProgrNumeroManuale() + ": ";
				} else {
					reati += "Reato " + lReato.getProgrReato() + ": ";
					dataReati += "Reato " + lReato.getProgrReato() + ": ";
				}

				reati += lReato.getDescrTipoReato() + "\n";
				dataReati += lReato.getDescrPeriodoConsumazione() + "\n";

			} // end while

		} // end if(lReatiCirostanze != null && lReatiCirostanze.size() != 0) {

		reatoData[0] = reati;
		reatoData[1] = dataReati;

		return reatoData;
	}

	// Restituisce una stringa con la concatenazione dei
	// reati legati al Procedimento
	private String calcolaCirco(DettaglioFascicoloModel lDettFascicolo) {
		String aggravante = "";

		List lCAggravanti = lDettFascicolo.getCircostanze();
		if (lCAggravanti != null && lCAggravanti.size() != 0) {
			int Progr = 0;
			Iterator lIterCAgg = lCAggravanti.iterator();
			while (lIterCAgg.hasNext()) {
				Progr++;
				CircostanzaModel lCircostanza = (CircostanzaModel) lIterCAgg.next();
				if (lCircostanza != null && lCircostanza.getIdCircostanza() != null) {
					aggravante += "Circostanza " + Progr + ": ";
					aggravante += lCircostanza.getDescrTipoCircostanza() + "\n";
				}

			} // end while

		} // end if(lReatiCirostanze != null && lReatiCirostanze.size() != 0) {

		return aggravante;
	}

	// MEV 26 CUMULO Step 2 - Aggiungo REATO_CUMULO, CIRCOSTANZA_CUMULO nella funzione Ricerca Reato By Cumulo
	// -

	// Restituisce una stringa con la concatenazione dei Reati legati All'Istruttoria Cumulo
	private String[] calcolaReatiCumulo(DettaglioFascicoloModel lDettFascicolo) {
		String[] reatoData = new String[2];
		String reati = "";
		String dataReati = "";

		List lReatiCirostanze = lDettFascicolo.getReatoCircoCumulo();
		if (lReatiCirostanze != null && lReatiCirostanze.size() != 0) {

			Iterator lIterReati = lReatiCirostanze.iterator();
			while (lIterReati.hasNext()) {

				ReatoCircostanzaCumuloModel lReatoCircostanza = (ReatoCircostanzaCumuloModel) lIterReati
						.next();
				ReatoCumuloModel lReato = lReatoCircostanza.getReatoCum();

				if (lReato.getProgrNumeroManuale() != null && !lReato.getProgrNumeroManuale().equals("")) {
					reati += "Reato " + lReato.getProgrNumeroManuale() + ": ";
					dataReati += "Reato " + lReato.getProgrNumeroManuale() + ": ";
				} else {
					reati += "Reato " + lReato.getProgrReato() + ": ";
					dataReati += "Reato " + lReato.getProgrReato() + ": ";
				}

				reati += lReato.getDescrTipoReato() + "\n";
				dataReati += lReato.getDescrPeriodoConsumazioneCum() + "\n";

			} // end while

		} // end if(lReatiCirostanze != null && lReatiCirostanze.size() != 0) {

		reatoData[0] = reati;
		reatoData[1] = dataReati;

		return reatoData;
	}

	// Restituisce una stringa con la concatenazione deile Aggravanti legate All'Istruttoria_Cumulo
	private String calcolaCircoCumulo(DettaglioFascicoloModel lDettFascicolo) {
		String aggravante = "";

		List lCAggravanti = lDettFascicolo.getCircostanzaCumulo();
		if (lCAggravanti != null && lCAggravanti.size() != 0) {
			int Progr = 0;
			Iterator lIterCAgg = lCAggravanti.iterator();
			while (lIterCAgg.hasNext()) {
				Progr++;
				CircostanzaCumuloModel lCircostanza = (CircostanzaCumuloModel) lIterCAgg.next();
				if (lCircostanza != null && lCircostanza.getIdCircostanzaCumulo() != null) {
					aggravante += "Circostanza " + Progr + ": ";
					aggravante += lCircostanza.getDescrTipoCircostanza() + "\n";
				}

			} // end while

		} // end if(lReatiCirostanze != null && lReatiCirostanze.size() != 0) {

		return aggravante;
	}

	// END MEV 26 CUMULO step 2

	/**
	 * La funzione prepara la pagina xls con l'elenco dei Procedimenti per Reato L'elenco nella lista � quello
	 * passato attraverso il parametro lFascicoli.
	 *
	 * @param wb
	 * @param aUffUteConnesso
	 * @param lDettaglioFascicoli
	 */
	private void creaFoglioElencoProcedimentiPerReato(HSSFWorkbook wb, UfficioModel aUffUteConnesso,
			Vector lDettaglioFascicoli, String cumulatiSINO, String IReato, String IDate, String INazio,
			String TipologiaProc, String ICircoAggravanti, String TipoRicerca) throws F3BException {
		short numCol = 0;
		DettaglioFascicoloModel lDettFascicolo = null;

		HSSFSheet sheet = wb.createSheet("ElencoProcedimentiPerReato");
		HSSFCellStyle csNull = wb.createCellStyle();
		HSSFCellStyle csNullBold = wb.createCellStyle();

		int nRow = 0;

		// Intestazione del foglio excel
		nRow = setIntestazione(sheet, aUffUteConnesso, csNull);

		nRow++;
		nRow++;

		HSSFRow row = sheet.createRow(nRow);
		row = sheet.createRow(nRow);

		HSSFFont my_font = wb.createFont();
		my_font.setBold(true);

		csNullBold.setFont(my_font);
		// Intestazione Riga 1
		String PrimaRigaIntestazione = "";
		String testaReato = "Elenco Procedimenti per Reato";
		String testaCirco = "Elenco Procedimenti per Circostanza";

		// PrimaRigaIntestazione ="Procedimenti per Reato Tipo Circostanza Aggravante";
		if (IDate != null && !IDate.equals("")) {
			if (TipoRicerca.equals("R"))
				PrimaRigaIntestazione += testaReato + ": " + IReato + " - " + IDate;
			else if (TipoRicerca.equals("A"))
				PrimaRigaIntestazione += testaCirco + ": " + ICircoAggravanti + " - " + IDate;
			else if (TipoRicerca.equals("RA"))
				PrimaRigaIntestazione += testaReato + ": " + IReato + " + Circostanza: " + ICircoAggravanti
						+ " - " + IDate;
		} else {
			if (TipoRicerca.equals("R"))
				PrimaRigaIntestazione += testaReato + ": " + IReato;
			else if (TipoRicerca.equals("A"))
				PrimaRigaIntestazione += testaCirco + ": " + ICircoAggravanti;
			else if (TipoRicerca.equals("RA"))
				PrimaRigaIntestazione += testaReato + ": " + IReato + " + Circostanza: " + ICircoAggravanti;
		}

		setCell(row, (short) 0, PrimaRigaIntestazione, csNullBold);
		nRow++;

		// Intestazione Riga 2
		String SecondaRigaIntestazione = "";
		if (!cumulatiSINO.equals("")) {
			SecondaRigaIntestazione = TipologiaProc + " - Solo con Procedimenti di Cumulo";
		} else {
			SecondaRigaIntestazione = TipologiaProc;
		}

		if (!INazio.equals("")) {
			SecondaRigaIntestazione += " - Nazionalit� " + INazio;
		}

		row = sheet.createRow(nRow);
		setCell(row, (short) 0, SecondaRigaIntestazione, csNullBold);
		nRow += 2;
		// nRow +=4;
		//
		// settaggio della larghezza
		// delle colonne
		numCol = 0;
		sheet.setColumnWidth((short) numCol++, (short) (10 * 256));
		sheet.setColumnWidth((short) numCol++, (short) (20 * 256));
		// MEV Agosto 2014 - Colanna CUMULO
		sheet.setColumnWidth((short) numCol++, (short) (10 * 256));
		//
		sheet.setColumnWidth((short) numCol++, (short) (50 * 256)); // stato proc
		sheet.setColumnWidth((short) numCol++, (short) (30 * 256)); // sogg
		sheet.setColumnWidth((short) numCol++, (short) (20 * 256)); // nazional
		sheet.setColumnWidth((short) numCol++, (short) (20 * 256)); // Pos giu
		sheet.setColumnWidth((short) numCol++, (short) (20 * 256)); // data fine pena
		sheet.setColumnWidth((short) numCol++, (short) (10 * 256));
		sheet.setColumnWidth((short) numCol++, (short) (10 * 256));
		sheet.setColumnWidth((short) numCol++, (short) (10 * 256));
		sheet.setColumnWidth((short) numCol++, (short) (10 * 256));
		sheet.setColumnWidth((short) numCol++, (short) (10 * 256));
		sheet.setColumnWidth((short) numCol++, (short) (10 * 256)); //
		sheet.setColumnWidth((short) numCol++, (short) (10 * 256));
		sheet.setColumnWidth((short) numCol++, (short) (10 * 256));
		sheet.setColumnWidth((short) numCol++, (short) (10 * 256));
		sheet.setColumnWidth((short) numCol++, (short) (10 * 256));
		sheet.setColumnWidth((short) numCol++, (short) (10 * 256));
		sheet.setColumnWidth((short) numCol++, (short) (10 * 256)); //
		sheet.setColumnWidth((short) numCol++, (short) (60 * 256)); // reato
		sheet.setColumnWidth((short) numCol++, (short) (40 * 256)); // data reato
		// MEV Agosto 2014 - Colanna CIRCOSTANZE AGGRAVANTI
		sheet.setColumnWidth((short) numCol++, (short) (40 * 256));

		// sheet.addMergedRegion(new CellRangeAddress(5,5,7,9));

		// stile per celle col bordo con testo centrato
		HSSFCellStyle csCenter = getBordo4Lati(wb);
		csCenter.setAlignment(HorizontalAlignment.CENTER);
		csCenter.setVerticalAlignment(VerticalAlignment.CENTER);
		csCenter.setWrapText(true);

		// stile per celle col bordo con testo allineato a sinistra
		HSSFCellStyle csLeft = getBordo4Lati(wb);
		csLeft.setAlignment(HorizontalAlignment.LEFT);
		csLeft.setVerticalAlignment(VerticalAlignment.CENTER);
		csLeft.setWrapText(true);

		// stile per celle col bordo con testo centrato e grossetto
		HSSFCellStyle csCenterBold = getBordo4LatiBold(wb);
		csCenterBold.setAlignment(HorizontalAlignment.CENTER);
		csCenterBold.setVerticalAlignment(VerticalAlignment.CENTER);
		csCenterBold.setWrapText(true);
		csCenterBold.setFont(my_font);

		// stile per celle senza bordo vuote
		HSSFCellStyle csvuoto = getNoBordo(wb);
		// csCenterBold.setAlignment(HorizontalAlignment.CENTER);
		// csCenterBold.setVerticalAlignment(VerticalAlignment.CENTER);
		// csCenterBold.setWrapText(true);
		// csCenterBold.setFont(my_font);

		// ------- Aggiunta 1
		row = sheet.createRow(nRow++);
		int Inte1 = nRow - 1;

		// Intestazione colonne
		numCol = 0;
		setCell(row, numCol++, "", csvuoto);
		setCell(row, numCol++, "", csvuoto);
		// MEV Agosto 2014 - Colanna CUMULO
		setCell(row, numCol++, "", csvuoto);
		//
		setCell(row, numCol++, "", csvuoto);
		setCell(row, numCol++, "", csvuoto);
		setCell(row, numCol++, "", csvuoto);
		setCell(row, numCol++, "", csvuoto);
		setCell(row, numCol++, "", csvuoto);
		setCell(row, numCol++, "Pena Irrogata in Sentenza", csCenterBold);
		setCell(row, numCol++, "", csCenterBold);
		setCell(row, numCol++, "", csCenterBold);
		setCell(row, numCol++, "", csCenterBold);
		setCell(row, numCol++, "", csCenterBold);
		setCell(row, numCol++, "", csCenterBold);
		setCell(row, numCol++, "Pena da Espiare", csCenterBold);
		setCell(row, numCol++, "", csCenterBold);
		setCell(row, numCol++, "", csCenterBold);
		setCell(row, numCol++, "", csCenterBold);
		setCell(row, numCol++, "", csCenterBold);
		setCell(row, numCol++, "", csCenterBold);
		setCell(row, numCol++, "", csvuoto);
		setCell(row, numCol++, "", csvuoto);
		// Aggravanti
		setCell(row, numCol++, "", csvuoto);

		// ---------------- Aggiunta 2
		row = sheet.createRow(nRow++);
		int Inte2 = nRow - 1;

		// Intestazione colonne
		numCol = 0;
		setCell(row, numCol++, "", csvuoto);
		setCell(row, numCol++, "", csvuoto);
		// Cumulo
		setCell(row, numCol++, "", csvuoto);
		setCell(row, numCol++, "", csvuoto);
		setCell(row, numCol++, "", csvuoto);
		setCell(row, numCol++, "", csvuoto);
		setCell(row, numCol++, "", csvuoto);
		setCell(row, numCol++, "", csvuoto);
		setCell(row, numCol++, "Reclusione", csCenterBold);
		setCell(row, numCol++, "", csCenterBold);
		setCell(row, numCol++, "", csCenterBold);
		setCell(row, numCol++, "Arresto", csCenterBold);
		setCell(row, numCol++, "", csCenterBold);
		setCell(row, numCol++, "", csCenterBold);
		setCell(row, numCol++, "Reclusione", csCenterBold);
		setCell(row, numCol++, "", csCenterBold);
		setCell(row, numCol++, "", csCenterBold);
		setCell(row, numCol++, "Arresto", csCenterBold);
		setCell(row, numCol++, "", csCenterBold);
		setCell(row, numCol++, "", csCenterBold);
		setCell(row, numCol++, "", csvuoto);
		setCell(row, numCol++, "", csvuoto);
		// Aggravanti
		setCell(row, numCol++, "", csvuoto);

		// ---------------------- Aggiunta 3
		row = sheet.createRow(nRow++);
		int Inte3 = nRow - 1;

		// Intestazione colonne
		numCol = 0;
		setCell(row, numCol++, "Prog", csCenterBold);
		setCell(row, numCol++, "Numero SIEP", csCenterBold);
		// Cumulo
		setCell(row, numCol++, "Cumulo", csCenterBold);
		setCell(row, numCol++, "Stato Procedimento", csCenterBold);
		setCell(row, numCol++, "Soggetto", csCenterBold);
		setCell(row, numCol++, "Nazione", csCenterBold);
		setCell(row, numCol++, "Regime di Espiazione", csCenterBold);
		setCell(row, numCol++, "Data Fine Pena", csCenterBold);
		setCell(row, numCol++, "Anni", csCenterBold);
		setCell(row, numCol++, "Mesi", csCenterBold);
		setCell(row, numCol++, "Giorni", csCenterBold);
		setCell(row, numCol++, "Anni", csCenterBold);
		setCell(row, numCol++, "Mesi", csCenterBold);
		setCell(row, numCol++, "Giorni", csCenterBold);
		setCell(row, numCol++, "Anni", csCenterBold);
		setCell(row, numCol++, "Mesi", csCenterBold);
		setCell(row, numCol++, "Giorni", csCenterBold);
		setCell(row, numCol++, "Anni", csCenterBold);
		setCell(row, numCol++, "Mesi", csCenterBold);
		setCell(row, numCol++, "Giorni", csCenterBold);
		setCell(row, numCol++, "Reati", csCenterBold);
		setCell(row, numCol++, "Data Reati", csCenterBold);
		// Aggravanti
		setCell(row, numCol++, "Circostanze", csCenterBold);
		// -----------------------------------------------------------------------

		sheet.addMergedRegion(new CellRangeAddress(Inte3, Inte3, 0, 0));
		sheet.addMergedRegion(new CellRangeAddress(Inte3, Inte3, 1, 1));
		// Cumulo
		sheet.addMergedRegion(new CellRangeAddress(Inte3, Inte3, 2, 2));
		sheet.addMergedRegion(new CellRangeAddress(Inte3, Inte3, 3, 3));
		sheet.addMergedRegion(new CellRangeAddress(Inte3, Inte3, 4, 4));
		sheet.addMergedRegion(new CellRangeAddress(Inte3, Inte3, 5, 5));
		sheet.addMergedRegion(new CellRangeAddress(Inte3, Inte3, 6, 6));
		sheet.addMergedRegion(new CellRangeAddress(Inte3, Inte3, 7, 7));

		sheet.addMergedRegion(new CellRangeAddress(Inte1, Inte1, 8, 13));
		sheet.addMergedRegion(new CellRangeAddress(Inte2, Inte2, 8, 10));
		sheet.addMergedRegion(new CellRangeAddress(Inte2, Inte2, 11, 13));

		sheet.addMergedRegion(new CellRangeAddress(Inte1, Inte1, 14, 19));
		sheet.addMergedRegion(new CellRangeAddress(Inte2, Inte2, 14, 16));
		sheet.addMergedRegion(new CellRangeAddress(Inte2, Inte2, 17, 19));

		sheet.addMergedRegion(new CellRangeAddress(Inte3, Inte3, 20, 20));
		sheet.addMergedRegion(new CellRangeAddress(Inte3, Inte3, 21, 21));
		// Aggravanti
		sheet.addMergedRegion(new CellRangeAddress(Inte3, Inte3, 22, 22));
		//

		// Ciclo sui fascicoli
		Iterator itx = lDettaglioFascicoli.iterator();
		int progr = 0;

		// inizio ciclo di scrittura dei dati
		while (itx.hasNext()) {
			numCol = 0;

			lDettFascicolo = (DettaglioFascicoloModel) itx.next();

			String[] reati = calcolaReati(lDettFascicolo);
			String reato = reati[0];
			String dataReato = reati[1];

			String circostanza = calcolaCirco(lDettFascicolo);

			String[] reatiCumulo = calcolaReatiCumulo(lDettFascicolo);
			String reatoCumulo = reatiCumulo[0];
			String dataReatoCumulo = reatiCumulo[1];

			String circostanzaCumulo = calcolaCircoCumulo(lDettFascicolo);

			// Scrittura riga del report
			row = sheet.createRow(nRow++);

			// Prog.
			setCell(row, numCol++, new Integer(++progr).toString(), csCenter);
			// Numero SIEP
			setCell(row, numCol++, lDettFascicolo.getFascicoloSiep().getChiaveAnno() + "/"
					+ lDettFascicolo.getFascicoloSiep().getChiaveProgr(), csCenter);
			// Cumulo
			if (lDettFascicolo.getFascicoloSiep().getFlagCumulante() != null
					&& lDettFascicolo.getFascicoloSiep().getFlagCumulante().compareTo("S") == 0) {
				setCell(row, numCol++, "CUMULO", csCenter);
			} else {
				setCell(row, numCol++, "", csCenter);
			}
			// Stato del procedimento
			setCell(row, numCol++, lDettFascicolo.getFascicoloSiep().getDescrStatoProcedimento(), csCenter);
			// Soggetto
			setCell(row, numCol++, lDettFascicolo.getFascicoloSiep().getSoggetto().getCognome() + " "
					+ lDettFascicolo.getFascicoloSiep().getSoggetto().getNome(), csCenter);
			// Nazione
			setCell(row, numCol++, lDettFascicolo.getFascicoloSiep().getSoggetto().getDescrNazionalita(),
					csCenter);
			// Regime di Espiazione
			if (lDettFascicolo.getPosizioneGiuridica() != null) {
				setCell(row, numCol++, lDettFascicolo.getPosizioneGiuridica().getDescrPosizioneGiuridica(),
						csCenter);
			} else {
				setCell(row, numCol++, "", csCenter);
			}
			// Data Fine Pena
			if (lDettFascicolo.getPenaResidua() != null
					&& lDettFascicolo.getPenaResidua().getDataFine() != null) {
				setCell(row, numCol++, DateUtils.getDateToString(
						lDettFascicolo.getPenaResidua().getDataFine(), "dd/MM/yyyy"), csCenter);
			} else {
				setCell(row, numCol++, " ", csCenter);
			}
			// Pena Irrogata in Sentenza (Priva di Cumulo)
			if (lDettFascicolo.getPenaComplessivaSanzioneSostitutiva() != null
					&& lDettFascicolo.getPenaComplessivaSanzioneSostitutiva().getPenaComplessiva() != null) {
				// Reclusione
				setCell(row, numCol++, "" + StringUtils.toStringJSP(lDettFascicolo
						.getPenaComplessivaSanzioneSostitutiva().getPenaComplessiva().getNumAnniReclusione()),
						csCenter);
				setCell(row, numCol++, "" + StringUtils.toStringJSP(lDettFascicolo
						.getPenaComplessivaSanzioneSostitutiva().getPenaComplessiva().getNumMesiReclusione()),
						csCenter);
				setCell(row, numCol++,
						"" + StringUtils.toStringJSP(lDettFascicolo.getPenaComplessivaSanzioneSostitutiva()
								.getPenaComplessiva().getNumGiorniReclusione()),
						csCenter);
				// Arresto
				setCell(row, numCol++, "" + StringUtils.toStringJSP(lDettFascicolo
						.getPenaComplessivaSanzioneSostitutiva().getPenaComplessiva().getNumAnniArresto()),
						csCenter);
				setCell(row, numCol++, "" + StringUtils.toStringJSP(lDettFascicolo
						.getPenaComplessivaSanzioneSostitutiva().getPenaComplessiva().getNumMesiArresto()),
						csCenter);
				setCell(row, numCol++, "" + StringUtils.toStringJSP(lDettFascicolo
						.getPenaComplessivaSanzioneSostitutiva().getPenaComplessiva().getNumGiorniArresto()),
						csCenter);
			}
			// Pena Irrogata in Sentenza (Con Cumulo)
			else if (lDettFascicolo.getPenaCumulo() != null) {
				// Reclusione
				setCell(row, numCol++,
						"" + StringUtils.toStringJSP(lDettFascicolo.getPenaCumulo().getNumAnniReclusione()),
						csCenter);
				setCell(row, numCol++,
						"" + StringUtils.toStringJSP(lDettFascicolo.getPenaCumulo().getNumMesiReclusione()),
						csCenter);
				setCell(row, numCol++,
						"" + StringUtils.toStringJSP(lDettFascicolo.getPenaCumulo().getNumGiorniReclusione()),
						csCenter);
				// Arresto
				setCell(row, numCol++,
						"" + StringUtils.toStringJSP(lDettFascicolo.getPenaCumulo().getNumAnniArresto()),
						csCenter);
				setCell(row, numCol++,
						"" + StringUtils.toStringJSP(lDettFascicolo.getPenaCumulo().getNumMesiArresto()),
						csCenter);
				setCell(row, numCol++,
						"" + StringUtils.toStringJSP(lDettFascicolo.getPenaCumulo().getNumGiorniArresto()),
						csCenter);
			} else {
				setCell(row, numCol++, " ", csCenter);
				setCell(row, numCol++, " ", csCenter);
				setCell(row, numCol++, " ", csCenter);

				setCell(row, numCol++, " ", csCenter);
				setCell(row, numCol++, " ", csCenter);
				setCell(row, numCol++, " ", csCenter);
			}

			// Pena da Espiare
			if (lDettFascicolo != null && lDettFascicolo.getCalendario() != null
					&& lDettFascicolo.getCalendario().getDataFine() != null) {
				// Calendario Reclusione
				if (lDettFascicolo.getCalendario().getDataFine().after(DateUtils.getSysDate())) {
					setCell(row, numCol++,
							"" + StringUtils.toStringJSP(lDettFascicolo.getCalendario().getNumAnni()),
							csCenter);
					setCell(row, numCol++,
							"" + StringUtils.toStringJSP(lDettFascicolo.getCalendario().getNumMesi()),
							csCenter);
					setCell(row, numCol++,
							"" + StringUtils.toStringJSP(lDettFascicolo.getCalendario().getNumGiorni()),
							csCenter);
				} else if (lDettFascicolo.getCalendario().getDataFine()
						.compareTo(DateUtils.getSysDate()) == 0) {
					setCell(row, numCol++, "0", csCenter);
					setCell(row, numCol++, "0", csCenter);
					setCell(row, numCol++, "0", csCenter);
				} else {
					setCell(row, numCol++, " ", csCenter);
					setCell(row, numCol++, " ", csCenter);
					setCell(row, numCol++, " ", csCenter);
				}

				// Arresto
				setCell(row, numCol++, " ", csCenter);
				setCell(row, numCol++, " ", csCenter);
				setCell(row, numCol++, " ", csCenter);
			} else if (lDettFascicolo != null && lDettFascicolo.getPenaResidua() != null) {
				// Reclusione
				setCell(row, numCol++,
						"" + StringUtils.toStringJSP(lDettFascicolo.getPenaResidua().getNumAnniReclusione()),
						csCenter);
				setCell(row, numCol++,
						"" + StringUtils.toStringJSP(lDettFascicolo.getPenaResidua().getNumMesiReclusione()),
						csCenter);
				setCell(row, numCol++,
						"" + StringUtils
								.toStringJSP(lDettFascicolo.getPenaResidua().getNumGiorniReclusione()),
						csCenter);
				// Arresto
				setCell(row, numCol++,
						"" + StringUtils.toStringJSP(lDettFascicolo.getPenaResidua().getNumAnniArresto()),
						csCenter);
				setCell(row, numCol++,
						"" + StringUtils.toStringJSP(lDettFascicolo.getPenaResidua().getNumMesiArresto()),
						csCenter);
				setCell(row, numCol++,
						"" + StringUtils.toStringJSP(lDettFascicolo.getPenaResidua().getNumGiorniArresto()),
						csCenter);
			} else {
				setCell(row, numCol++, " ", csCenter);
				setCell(row, numCol++, " ", csCenter);
				setCell(row, numCol++, " ", csCenter);

				setCell(row, numCol++, " ", csCenter);
				setCell(row, numCol++, " ", csCenter);
				setCell(row, numCol++, " ", csCenter);
			}

			// MEV 26 CUMULO Step 2
			if (lDettFascicolo.getFlagIstruttoriaPresente() != null
					&& lDettFascicolo.getFlagIstruttoriaPresente().equals("S")) {
				// Reati_Cumulo
				setCell(row, numCol++, reatoCumulo, csLeft);
				// Data Reati_Cumulo
				setCell(row, numCol++, dataReatoCumulo, csLeft);
				// Circo Agravanti_Cumulo
				setCell(row, numCol++, circostanzaCumulo, csLeft);
			} else {
				// Reati
				setCell(row, numCol++, reato, csLeft);
				// Data Reati
				setCell(row, numCol++, dataReato, csLeft);
				// Circo Agravanti
				setCell(row, numCol++, circostanza, csLeft);
			}
			// END MEV 26

		}
	}

	public String processRequest() throws Exception {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("" + getClass().getName() + " .processRequest: inizio ");

		ReatoModel mRicercaModel = (ReatoModel) getSessionAttribute("ReatoRicercaModel");
		CircostanzaModel mCircoAggravaModel = (CircostanzaModel) getSessionAttribute("CircoAggrRicercaModel");

		// MEV Agosto 2014 - Ricerca procedimenti by Reato - aggiunti criteri di Ricerca
		String cumulatiSINO = "";
		Boolean solocumulati = false;

		if (!isRequestParameterNullObj(ICostantiReato.CAMPO_CERCA_CUMULATI))
			if (getRequestStringParameter(ICostantiReato.CAMPO_CERCA_CUMULATI).compareTo("SI") == 0) {
				solocumulati = true;
				cumulatiSINO = "SI";
			}

		// Tipologia Procedimenti
		String TipologiaProc = "";
		if (!isRequestParameterNullObj("TipoProc")) {
			TipologiaProc = getRequestStringParameter("TipoProc");
		}

		// Intestazione tipo reato
		String IReato = "";
		if (!isRequestParameterNullObj("IntestaReato")) {
			IReato = getRequestStringParameter("IntestaReato");
		}

		// Intestazione Date
		String IDate = "";
		if (!isRequestParameterNullObj("IntestaDate")) {
			IDate = getRequestStringParameter("IntestaDate");
		}

		// Descrizione tipo Nazionalit�
		String INazio = "";
		if (!isRequestParameterNullObj("DescNazio")) {
			INazio = getRequestStringParameter("DescNazio");
		}

		// Intestazione Circostanze Aggravanti
		String ICircAggr = "";
		if (!isRequestParameterNullObj("IntestaCirco")) {
			ICircAggr = getRequestStringParameter("IntestaCirco");
		}

		// Tipologia Ricerca: Reato / Circostanze / Reato e Circostanze
		String TipologiaRicerca = "";
		if (!isRequestParameterNullObj("TipoRicerca")) {
			TipologiaRicerca = getRequestStringParameter("TipoRicerca");
		}

		Vector<DettaglioFascicoloModel> lDettaglioFascicoli = getElencoFascicoli(mRicercaModel,
				mCircoAggravaModel, TipologiaRicerca, solocumulati, "-1");

		// risultato della ricerca supera 65536 record
		if (lDettaglioFascicoli != null && lDettaglioFascicoli.size() > 65536) {
			throw new F3BException(F3BException.USER_MESSAGE,
					"Impostare almeno un parametro di ricerca, in quanto il risultato non � interamente visualizzabile.");
		}

		if (lDettaglioFascicoli != null)
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Risultati ricerca completa : " + lDettaglioFascicoli.size());

		HSSFWorkbook wb = new HSSFWorkbook();
		ByteArrayOutputStream fileOut = new ByteArrayOutputStream();
		creaFoglioElencoProcedimentiPerReato(wb, getUfficioUtenteConnesso(), lDettaglioFascicoli,
				cumulatiSINO, IReato, IDate, INazio, TipologiaProc, ICircAggr, TipologiaRicerca);

		// Generazione file xls
		try {
			wb.write(fileOut);
		} catch (IOException ioe) {
			throw new F3BException("ActStampaElencoProcReatoInExcel.processRequest: " + ioe);
		}

		setRequestAttribute("report", fileOut);
		setRequestAttribute(IWebConstants.DISPOSITION_FIELD, IWebConstants.ATTACHMENT_DISPOSITION_FILE);

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("" + getClass().getName() + " .processRequest: fine ");

		return IWebConstants.PG_DOWNLOAD_DOCUMENT;
	}

}
