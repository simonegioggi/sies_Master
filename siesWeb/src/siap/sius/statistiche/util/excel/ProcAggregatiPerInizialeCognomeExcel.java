package siap.sius.statistiche.util.excel;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Iterator;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import f3b.util.DateUtils;
import f3b.util.F3BException;
import siap.sico.ufficio.model.UfficioModel;
//import siap.sius.fascicolo.util.HSSFUtils;
import siap.sius.statistiche.controller.IStatisticheSius;
import siap.sius.statistiche.model.ProcAggregatiCognomeElencoModel;
import siap.sius.statistiche.model.ProcAggregatiCognomeModel;
import siap.sius.statistiche.model.RicercaAggregatiCognomeModel;
import siap.sius.util.SIUSLookupRemote;
import siap.util.excel.SIAPExcelProducer;

@SuppressWarnings("rawtypes")
public class ProcAggregatiPerInizialeCognomeExcel extends SIAPExcelProducer {

	public ByteArrayOutputStream creaFoglioProcAggregatiPerInizialeCognome(
			UfficioModel aUfficioUtenteConnesso, RicercaAggregatiCognomeModel aRicercaModel)
			throws F3BException {

		ByteArrayOutputStream lFileOut = null;
		HSSFWorkbook lWb = null;

		try {
			lWb = new HSSFWorkbook();
			lFileOut = new ByteArrayOutputStream();

			this.creaFoglioProcAggregatiPerInizialeCognome1Lettera(lWb, aUfficioUtenteConnesso,
					aRicercaModel);
			this.creaFoglioProcAggregatiPerInizialeCognome2Lettere(lWb, aUfficioUtenteConnesso,
					aRicercaModel);
			this.creaFoglioProcAggregatiPerInizialeCognomeElenco(lWb, aUfficioUtenteConnesso, aRicercaModel);

			lWb.write(lFileOut);
		} catch (IOException ioe) {
			throw new F3BException("StatisController.creaFoglioProcAggregatiPerInizialeCognome: " + ioe);
		}

		return lFileOut;
	}

	private void creaFoglioProcAggregatiPerInizialeCognome1Lettera(HSSFWorkbook aWb,
			UfficioModel aUfficioUtenteConnesso, RicercaAggregatiCognomeModel aRicercaModel)
			throws F3BException {

		HSSFSheet lSheet = null;
		HSSFCellStyle lCellStyleNull = null;
		HSSFCellStyle lCellStyleRight = null;
		HSSFCellStyle lCellStyleLeft = null;
		HSSFCellStyle lCellStyleLeftBold = null;
		HSSFCellStyle lCellStyleRightBold = null;
		HSSFFont lFontBold = null;
		HSSFRow lRow = null;
		Iterator lItx = null;
		// int lContatore = 0;
		int lRowCounter = 0;
		ProcAggregatiCognomeModel lModel = null;
		String lPatternData = "dd/MM/yyyy";
		String lBuffer = null;
		IStatisticheSius lCtrlStatSius = null;
		Collection<ProcAggregatiCognomeModel> lElenco = null;
		BigDecimal lTotaleComplessivo = null;

		lCtrlStatSius = SIUSLookupRemote.getStatisticheSiusRemote();

		// creazione foglio
		lSheet = aWb.createSheet("Lettera Iniziale");
		lCellStyleNull = aWb.createCellStyle();

		// Intestazione del foglio excel
		// lRowCounter = HSSFUtils.getInstance().setIntestazione(lSheet, aUfficioUtenteConnesso,
		// lCellStyleNull);
		lRowCounter = setIntestazione(lSheet, aUfficioUtenteConnesso, lCellStyleNull);
		lRowCounter += 2;

		// Stampa filtri di ricerca
		if (aRicercaModel != null) {

			lRow = lSheet.createRow(lRowCounter);
			// HSSFUtils.getInstance().setCell(lRow, 0, lBuffer , lCellStyleNull);
			setCell(lRow, 0, lBuffer, lCellStyleNull);
			lRowCounter++;

			if (aRicercaModel.getDataInizio() != null && aRicercaModel.getDataFine() != null) {
				lBuffer = "Procedimenti iscritti nel periodo dal ";
				lBuffer += DateUtils.getDateToString(aRicercaModel.getDataInizio(), lPatternData);
				lBuffer += " al ";
				lBuffer += DateUtils.getDateToString(aRicercaModel.getDataFine(), lPatternData);
				lBuffer += ", aggregati per lettera iniziale cognome soggetto ";
				lRow = lSheet.createRow(lRowCounter);
				// HSSFUtils.getInstance().setCell(lRow, 0, lBuffer , lCellStyleNull);
				setCell(lRow, 0, lBuffer, lCellStyleNull);
				lRowCounter++;
			}
		}

		lRowCounter += 2;

		// font grassetto
		lFontBold = aWb.createFont();
		lFontBold.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);

		// stile per celle col bordo con testo a sinistra
		lCellStyleLeft = getBordo4Lati(aWb);
		lCellStyleLeft.setAlignment(HSSFCellStyle.ALIGN_LEFT);
		lCellStyleLeft.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		lCellStyleLeft.setWrapText(true);

		// stile per celle col bordo con testo a sinistra grassetto
		lCellStyleLeftBold = getBordo4Lati(aWb);
		lCellStyleLeftBold.setFont(lFontBold);
		lCellStyleLeftBold.setAlignment(HSSFCellStyle.ALIGN_LEFT);
		lCellStyleLeftBold.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		lCellStyleLeftBold.setWrapText(true);

		// stile per celle col bordo con testo a destra
		lCellStyleRight = getBordo4Lati(aWb);
		lCellStyleRight.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
		lCellStyleRight.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		lCellStyleRight.setWrapText(true);

		// stile per celle col bordo con testo a destra grassetto
		lCellStyleRightBold = getBordo4Lati(aWb);
		lCellStyleRightBold.setFont(lFontBold);
		lCellStyleRightBold.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
		lCellStyleRightBold.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		lCellStyleRightBold.setWrapText(true);

		lRow = lSheet.createRow(lRowCounter++);

		lSheet.setColumnWidth(0, 35 * 256); // Lettera Iniziale Cognome
		lSheet.setColumnWidth(1, 15 * 256); // Totale

		// Intestazione colonne
		// HSSFUtils.getInstance().setCell(lRow, 0, "Lettera Iniziale Cognome", lCellStyleLeftBold);
		// HSSFUtils.getInstance().setCell(lRow, 1, "Totale", lCellStyleRightBold);
		setCell(lRow, 0, "Lettera Iniziale Cognome", lCellStyleLeftBold);
		setCell(lRow, 1, "Totale", lCellStyleRightBold);

		lTotaleComplessivo = new BigDecimal(0);
		lElenco = lCtrlStatSius.ExRicercaProcAggregati1Lettera(aRicercaModel);
		lItx = lElenco.iterator();
		while (lItx.hasNext()) {
			lModel = (ProcAggregatiCognomeModel) lItx.next();

			lRow = lSheet.createRow(lRowCounter++);

			/*
			 * HSSFUtils.getInstance().setCell(lRow, 0, lModel.getIniziale(), lCellStyleLeft);
			 */
			/*
			 * HSSFUtils.getInstance().setCell(lRow, 1, "" + lModel.getTotale(), lCellStyleRight);
			 */

			setCell(lRow, 0, lModel.getIniziale(), lCellStyleLeft);
			setCell(lRow, 1, "" + lModel.getTotale(), lCellStyleRight);

			lTotaleComplessivo = lTotaleComplessivo.add(lModel.getTotale());
		}
		lRow = lSheet.createRow(lRowCounter++);

		setCell(lRow, 0, "Totale Complessivo", lCellStyleLeft);

		setCell(lRow, 1, "" + lTotaleComplessivo, lCellStyleRight);
	}

	private void creaFoglioProcAggregatiPerInizialeCognome2Lettere(HSSFWorkbook aWb,
			UfficioModel aUfficioUtenteConnesso, RicercaAggregatiCognomeModel aRicercaModel)
			throws F3BException {
		HSSFSheet lSheet = null;
		HSSFCellStyle lCellStyleNull = null;
		HSSFCellStyle lCellStyleRight = null;
		HSSFCellStyle lCellStyleLeft = null;
		HSSFCellStyle lCellStyleLeftBold = null;
		HSSFCellStyle lCellStyleRightBold = null;
		HSSFFont lFontBold = null;
		HSSFRow lRow = null;
		Iterator lItx = null;
		// int lContatore = 0;
		int lRowCounter = 0;
		ProcAggregatiCognomeModel lModel = null;
		String lPatternData = "dd/MM/yyyy";
		String lBuffer = null;
		IStatisticheSius lCtrlStatSius = null;
		Collection<ProcAggregatiCognomeModel> lElenco = null;
		BigDecimal lTotaleComplessivo = null;

		lCtrlStatSius = SIUSLookupRemote.getStatisticheSiusRemote();

		// creazione foglio
		lSheet = aWb.createSheet("2 Lettere Iniziali");
		lCellStyleNull = aWb.createCellStyle();

		// Intestazione del foglio excel
		lRowCounter = setIntestazione(lSheet, aUfficioUtenteConnesso, lCellStyleNull);
		lRowCounter += 2;

		// Stampa filtri di ricerca
		if (aRicercaModel != null) {

			lRow = lSheet.createRow(lRowCounter);
			setCell(lRow, 0, lBuffer, lCellStyleNull);
			lRowCounter++;

			if (aRicercaModel.getDataInizio() != null && aRicercaModel.getDataFine() != null) {
				lBuffer = "Procedimenti iscritti nel periodo dal ";
				lBuffer += DateUtils.getDateToString(aRicercaModel.getDataInizio(), lPatternData);
				lBuffer += " al ";
				lBuffer += DateUtils.getDateToString(aRicercaModel.getDataFine(), lPatternData);
				lBuffer += ", aggregati per le prime 2 lettere iniziali cognome soggetto ";
				lRow = lSheet.createRow(lRowCounter);
				setCell(lRow, 0, lBuffer, lCellStyleNull);
				lRowCounter++;
			}
		}

		lRowCounter += 2;

		// font grassetto
		lFontBold = aWb.createFont();
		lFontBold.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);

		// stile per celle col bordo con testo a sinistra
		lCellStyleLeft = getBordo4Lati(aWb);
		lCellStyleLeft.setAlignment(HSSFCellStyle.ALIGN_LEFT);
		lCellStyleLeft.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		lCellStyleLeft.setWrapText(true);

		// stile per celle col bordo con testo a sinistra grassetto
		lCellStyleLeftBold = getBordo4Lati(aWb);
		lCellStyleLeftBold.setFont(lFontBold);
		lCellStyleLeftBold.setAlignment(HSSFCellStyle.ALIGN_LEFT);
		lCellStyleLeftBold.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		lCellStyleLeftBold.setWrapText(true);

		// stile per celle col bordo con testo a destra
		lCellStyleRight = getBordo4Lati(aWb);
		lCellStyleRight.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
		lCellStyleRight.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		lCellStyleRight.setWrapText(true);

		// stile per celle col bordo con testo a destra grassetto
		lCellStyleRightBold = getBordo4Lati(aWb);
		lCellStyleRightBold.setFont(lFontBold);
		lCellStyleRightBold.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
		lCellStyleRightBold.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		lCellStyleRightBold.setWrapText(true);

		lRow = lSheet.createRow(lRowCounter++);

		lSheet.setColumnWidth(0, 35 * 256); // Prime 2 lettere iniziali cognome
		lSheet.setColumnWidth(1, 15 * 256); // Totale

		// Intestazione colonne
		setCell(lRow, 0, "Prime 2 lettere iniziali cognome", lCellStyleLeftBold);
		setCell(lRow, 1, "Totale", lCellStyleRightBold);

		lTotaleComplessivo = new BigDecimal(0);
		lElenco = lCtrlStatSius.ExRicercaProcAggregati2Lettere(aRicercaModel);
		lItx = lElenco.iterator();
		while (lItx.hasNext()) {
			lModel = (ProcAggregatiCognomeModel) lItx.next();

			lRow = lSheet.createRow(lRowCounter++);

			setCell(lRow, 0, lModel.getIniziale(), lCellStyleLeft);
			setCell(lRow, 1, "" + lModel.getTotale(), lCellStyleRight);

			lTotaleComplessivo = lTotaleComplessivo.add(lModel.getTotale());
		}
		lRow = lSheet.createRow(lRowCounter++);
		setCell(lRow, 0, "Totale Complessivo", lCellStyleLeft);
		setCell(lRow, 1, "" + lTotaleComplessivo, lCellStyleRight);
	}

	private void creaFoglioProcAggregatiPerInizialeCognomeElenco(HSSFWorkbook aWb,
			UfficioModel aUfficioUtenteConnesso, RicercaAggregatiCognomeModel aRicercaModel)
			throws F3BException {

		HSSFSheet lSheet = null;
		HSSFCellStyle lCellStyleNull = null;
		HSSFCellStyle lCellStyleRight = null;
		HSSFCellStyle lCellStyleCenter = null;
		HSSFCellStyle lCellStyleLeft = null;
		HSSFCellStyle lCellStyleLeftBold = null;
		HSSFFont lFontBold = null;
		HSSFRow lRow = null;
		Iterator lItx = null;
		// int lContatore = 0;
		int lRowCounter = 0;
		ProcAggregatiCognomeElencoModel lModel = null;
		String lPatternData = "dd/MM/yyyy";
		String lBuffer = null;
		IStatisticheSius lCtrlStatSius = null;
		Collection<ProcAggregatiCognomeElencoModel> lElenco = null;

		lCtrlStatSius = SIUSLookupRemote.getStatisticheSiusRemote();

		// creazione foglio
		lSheet = aWb.createSheet("Elenco procedimenti");
		lCellStyleNull = aWb.createCellStyle();

		// Intestazione del foglio excel
		lRowCounter = setIntestazione(lSheet, aUfficioUtenteConnesso, lCellStyleNull);
		lRowCounter += 2;

		// Stampa filtri di ricerca
		if (aRicercaModel != null) {

			lRow = lSheet.createRow(lRowCounter);
			setCell(lRow, 0, lBuffer, lCellStyleNull);
			lRowCounter++;

			if (aRicercaModel.getDataInizio() != null && aRicercaModel.getDataFine() != null) {
				lBuffer = "Procedimenti iscritti nel periodo dal ";
				lBuffer += DateUtils.getDateToString(aRicercaModel.getDataInizio(), lPatternData);
				lBuffer += " al ";
				lBuffer += DateUtils.getDateToString(aRicercaModel.getDataFine(), lPatternData);
				lBuffer += ", ordinati alfabeticamente per cognome ";
				lRow = lSheet.createRow(lRowCounter);
				setCell(lRow, 0, lBuffer, lCellStyleNull);
				lRowCounter++;
			}
		}

		lRowCounter += 2;

		// stile per celle col bordo con testo a sinistra
		lCellStyleLeft = getBordo4Lati(aWb);
		lCellStyleLeft.setAlignment(HSSFCellStyle.ALIGN_LEFT);
		lCellStyleLeft.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		lCellStyleLeft.setWrapText(true);

		// stile per celle col bordo con testo a sinistra grassetto
		lFontBold = aWb.createFont();
		lFontBold.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		lCellStyleLeftBold = getBordo4Lati(aWb);
		lCellStyleLeftBold.setFont(lFontBold);
		lCellStyleLeftBold.setAlignment(HSSFCellStyle.ALIGN_LEFT);
		lCellStyleLeftBold.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		lCellStyleLeftBold.setWrapText(true);

		// stile per celle col bordo con testo centrato
		lCellStyleCenter = getBordo4Lati(aWb);
		lCellStyleCenter.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		lCellStyleCenter.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		lCellStyleCenter.setWrapText(true);

		// stile per celle col bordo con testo a destra
		lCellStyleRight = getBordo4Lati(aWb);
		lCellStyleRight.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
		lCellStyleRight.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		lCellStyleRight.setWrapText(true);

		lRow = lSheet.createRow(lRowCounter++);

		lSheet.setColumnWidth(0, 15 * 256); // Prima lettera
		lSheet.setColumnWidth(1, 15 * 256); // Prime 2 lettere
		lSheet.setColumnWidth(2, 15 * 256); // Prime 3 lettere
		lSheet.setColumnWidth(3, 30 * 256); // Cognome
		lSheet.setColumnWidth(4, 30 * 256); // Nome
		lSheet.setColumnWidth(5, 15 * 256); // Data Nascita
		lSheet.setColumnWidth(6, 30 * 256); // Luogo Nascita
		lSheet.setColumnWidth(7, 10 * 256); // Anno
		lSheet.setColumnWidth(8, 15 * 256); // Progressivo
		lSheet.setColumnWidth(9, 30 * 256); // Posizione Giuridica
		lSheet.setColumnWidth(10, 50 * 256); // Contenuto
		lSheet.setColumnWidth(11, 30 * 256); // Stato Procedimento

		// Intestazione colonne
		setCell(lRow, 0, "Prima lettera", lCellStyleLeftBold);
		setCell(lRow, 1, "Prime 2 lettere", lCellStyleLeftBold);
		setCell(lRow, 2, "Prime 3 lettere", lCellStyleLeftBold);
		setCell(lRow, 3, "Cognome", lCellStyleLeftBold);
		setCell(lRow, 4, "Nome", lCellStyleLeftBold);
		setCell(lRow, 5, "Data Nascita", lCellStyleLeftBold);
		setCell(lRow, 6, "Luogo Nascita", lCellStyleLeftBold);
		setCell(lRow, 7, "Anno", lCellStyleLeftBold);
		setCell(lRow, 8, "Progressivo", lCellStyleLeftBold);
		setCell(lRow, 9, "Posizione Giuridica", lCellStyleLeftBold);
		setCell(lRow, 10, "Contenuto", lCellStyleLeftBold);
		setCell(lRow, 11, "Stato Procedimento", lCellStyleLeftBold);

		lElenco = lCtrlStatSius.ExRicercaProcAggregatiElenco(aRicercaModel);
		lItx = lElenco.iterator();
		while (lItx.hasNext()) {
			lModel = (ProcAggregatiCognomeElencoModel) lItx.next();

			lRow = lSheet.createRow(lRowCounter++);

			setCell(lRow, 0, lModel.getIniziale1Lettera(), lCellStyleLeft);
			setCell(lRow, 1, lModel.getIniziale2Lettere(), lCellStyleLeft);
			setCell(lRow, 2, lModel.getIniziale3Lettere(), lCellStyleLeft);
			setCell(lRow, 3, lModel.getCognome(), lCellStyleLeft);
			setCell(lRow, 4, lModel.getNome(), lCellStyleLeft);
			setCell(lRow, 5, DateUtils.getDateToString(lModel.getDataNascita(), lPatternData),
					lCellStyleCenter);
			setCell(lRow, 6, lModel.getLuogoNascita(), lCellStyleLeft);
			setCell(lRow, 7, lModel.getChiaveAnno(), lCellStyleRight);
			setCell(lRow, 8, lModel.getChiaveProgressivo(), lCellStyleLeft);
			setCell(lRow, 9, lModel.getPosizioneGiuridica(), lCellStyleLeft);
			setCell(lRow, 10, lModel.getContenuto(), lCellStyleLeft);
			setCell(lRow, 11, lModel.getStatoProcedimento(), lCellStyleLeft);
		}
	}

}