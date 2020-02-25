package siap.sius.statistiche.util.excel;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.util.StringUtils;
import siap.sico.ufficio.model.UfficioModel;
//import siap.sius.fascicolo.util.HSSFUtils;
import siap.sius.statistiche.controller.IStatisticheSius;
import siap.sius.statistiche.model.EveFasGepSogDetModel;
import siap.sius.statistiche.model.EveFasGepSogModel;
//import siap.sius.statistiche.model.EveFasGepSogModel;
import siap.sius.statistiche.model.RicercaProcedimentoModel;
import siap.sius.util.SIUSLookupRemote;
import siap.util.excel.SIAPExcelProducer;

public class ProcDataUdienzaFissataNoDefinitiNumGGExcel extends SIAPExcelProducer {

	@SuppressWarnings("rawtypes")
	public ByteArrayOutputStream creaFoglioProcDataUdienzaFissataNoDefinitiNumGG(
			UfficioModel aUfficioUtenteConnesso, RicercaProcedimentoModel aRicercaModel) throws F3BException {

		ByteArrayOutputStream lFileOut = null;
		HSSFWorkbook lWb = null;
		HSSFSheet lSheet = null;
		HSSFCellStyle lCellStyleNull = null;
		HSSFCellStyle lCellStyleCenter = null;
		HSSFRow lRow = null;
		Iterator lItx = null;
		int lRowCounter = 0;
		int lContatore = 0;
		EveFasGepSogDetModel lModel = null;
		String lPatternData = "dd/MM/yyyy";
		String lBuffer = null;
		IStatisticheSius lCtrlStatSius = null;
		Collection<EveFasGepSogModel> lElenco = null;

		lWb = new HSSFWorkbook();

		lSheet = lWb.createSheet("Elenco Procedimenti");
		lCellStyleNull = lWb.createCellStyle();

		// Intestazione del foglio excel
		lRowCounter = setIntestazione(lSheet, aRicercaModel.getUtenteConnesso().getUfficioUtente(),
				lCellStyleNull);
		lRowCounter++;

		// Inserimento dei parametri di ricerca.
		lRow = lSheet.createRow(lRowCounter);
		setCell(lRow, 0, "Elenco Procedimenti non Definiti", lCellStyleNull);
		lRowCounter += 2;

		// Stampa filtri di ricerca
		if (aRicercaModel != null) {
			lRow = lSheet.createRow(lRowCounter);
			setCell(lRow, 0, lBuffer, lCellStyleNull);
			lRowCounter++;

			if (aRicercaModel.getDataCameraConsiglioInizio() != null
					|| aRicercaModel.getDataCameraConsiglioFine() != null) {
				lBuffer = "Procedimenti con Data Udienza Fissata : ";
				if (aRicercaModel.getDataCameraConsiglioInizio() != null) {
					lBuffer += " dal " + DateUtils
							.getDateToString(aRicercaModel.getDataCameraConsiglioInizio(), lPatternData);
				}
				if (aRicercaModel.getDataCameraConsiglioFine() != null) {
					lBuffer += " al " + DateUtils.getDateToString(aRicercaModel.getDataCameraConsiglioFine(),
							lPatternData);
				}
				lRow = lSheet.createRow(lRowCounter);
				setCell(lRow, 0, lBuffer, lCellStyleNull);
				lRowCounter++;
			}

			if (aRicercaModel.getDataIscrizioneInizio() != null
					|| aRicercaModel.getDataIscrizioneFine() != null) {
				lBuffer = "Procedimenti con Data Iscrizione : ";
				if (aRicercaModel.getDataIscrizioneInizio() != null) {
					lBuffer += " dal " + DateUtils.getDateToString(aRicercaModel.getDataIscrizioneInizio(),
							lPatternData);
				}
				if (aRicercaModel.getDataIscrizioneFine() != null) {
					lBuffer += " al "
							+ DateUtils.getDateToString(aRicercaModel.getDataIscrizioneFine(), lPatternData);
				}
				lRow = lSheet.createRow(lRowCounter);
				setCell(lRow, 0, lBuffer, lCellStyleNull);
				lRowCounter++;
			}

			if (aRicercaModel.getNumeroGiorni() != null) {
				lBuffer = "Solo i procedimenti, non definiti, con data fissata da almeno "
						+ aRicercaModel.getNumeroGiorni() + " giorni";
				if (aRicercaModel.getDataFine() != null) {
					lBuffer += " alla data del "
							+ DateUtils.getDateToString(aRicercaModel.getDataFine(), lPatternData);
				}
				lRow = lSheet.createRow(lRowCounter);
				setCell(lRow, 0, lBuffer, lCellStyleNull);
				lRowCounter++;
			}
		}

		lRowCounter += 2;

		// stile per celle col bordo con testo centrato
		lCellStyleCenter = getBordo4Lati(lWb);
		lCellStyleCenter.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		lCellStyleCenter.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		lCellStyleCenter.setWrapText(true);

		lRow = lSheet.createRow(lRowCounter++);

		lSheet.setColumnWidth(0, 10 * 256); // Progr
		lSheet.setColumnWidth(1, 20 * 256); // Procedimento SIUS
		lSheet.setColumnWidth(2, 50 * 256); // Generalità Soggetto
		lSheet.setColumnWidth(3, 15 * 256); // Data Iscrizione
		lSheet.setColumnWidth(4, 15 * 256); // Data Udienza
		lSheet.setColumnWidth(5, 50 * 256); // Contenuto
		lSheet.setColumnWidth(6, 10 * 256); // Numero Giorni
		lSheet.setColumnWidth(7, 20 * 256); // Magistrato

		// Intestazione colonne
		setCell(lRow, 0, "Prog.", lCellStyleCenter);
		setCell(lRow, 1, "Procedimento SIUS", lCellStyleCenter);
		setCell(lRow, 2, "Generalità Soggetto", lCellStyleCenter);
		setCell(lRow, 3, "Data Iscrizione", lCellStyleCenter);
		setCell(lRow, 4, "Ultima Data Udienza", lCellStyleCenter);
		setCell(lRow, 5, "Contenuto", lCellStyleCenter);
		setCell(lRow, 6, "Numero Giorni", lCellStyleCenter);
		setCell(lRow, 7, "Magistrato", lCellStyleCenter);

		lCtrlStatSius = SIUSLookupRemote.getStatisticheSiusRemote();
		lElenco = lCtrlStatSius.ExRicercaProcFissatiNoDefNumGG(aRicercaModel);

		lItx = lElenco.iterator();
		while (lItx.hasNext()) {
			lContatore++;
			lModel = (EveFasGepSogDetModel) lItx.next();
			lRow = lSheet.createRow(lRowCounter++);

			setCell(lRow, 0, lContatore + "", lCellStyleCenter);
			setCell(lRow, 1, lModel.getFascicoloSius().getChiaveAnno() + "/"
					+ lModel.getFascicoloSius().getChiaveProgr(), lCellStyleCenter);
			setCell(lRow, 2, lModel.getFascicoloSius().getSoggetto().getCognome() + " "
					+ lModel.getFascicoloSius().getSoggetto().getNome(), lCellStyleCenter);
			setCell(lRow, 3,
					StringUtils.toStringJSP(DateUtils.getDateToString(
							lModel.getFascicoloSius().getDataIscrizione(), "dd-MM-yyyy"), "-"),
					lCellStyleCenter);
			setCell(lRow, 4,
					StringUtils.toStringJSP(DateUtils.getDateToString(
							lModel.getGeneraleProcedimento().getUdienza().getDataUdienza(), "dd-MM-yyyy"),
							"-"),
					lCellStyleCenter);
			setCell(lRow, 5, StringUtils.toStringJSP(
					lModel.getGeneraleProcedimento().getDescrOggettoProcedimento(), "-"), lCellStyleCenter);
			setCell(lRow, 6, lModel.getTotale() + "", lCellStyleCenter);
			setCell(lRow, 7, lModel.getMagistrato().getCognome() + " " + lModel.getMagistrato().getNome(),
					lCellStyleCenter);
		}

		lFileOut = new ByteArrayOutputStream();
		try {
			lWb.write(lFileOut);
		} catch (IOException ioe) {
			throw new F3BException(
					"StatisController.creaFoglioProcDataUdienzaFissataNoDefinitiNumGG : " + ioe);
		}

		return lFileOut;
	}

}