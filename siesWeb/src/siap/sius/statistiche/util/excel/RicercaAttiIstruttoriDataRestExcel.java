package siap.sius.statistiche.util.excel;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.util.StringUtils;
import siap.sico.ufficio.model.UfficioModel;
import siap.sius.statistiche.controller.IStatisticheSius;
import siap.sius.statistiche.model.EveFasGepSogProvModel;
import siap.sius.statistiche.model.RicercaProcedimentoModel;
import siap.sius.util.SIUSLookupRemote;
import siap.util.excel.SIAPExcelProducer;

public class RicercaAttiIstruttoriDataRestExcel extends SIAPExcelProducer {
	public ByteArrayOutputStream creaFoglioRicercaAttiIstruttoriDataRest(
			UfficioModel aUfficioUtenteConnesso, RicercaProcedimentoModel aRicercaModel) throws F3BException 
	{
		ByteArrayOutputStream lFileOut = null;
		HSSFWorkbook lWb = null;
		HSSFSheet lSheet = null;
		HSSFCellStyle lCellStyleNull = null;
		HSSFCellStyle lCellStyleCenter = null;
		HSSFRow lRow = null;
		Iterator lItx = null;
		
		int lRowCounter = 0;
		EveFasGepSogProvModel lModel = null;
		String lPatternData = "dd/MM/yyyy";
		String lBuffer = null;
		IStatisticheSius lCtrlStatSius = null;
		Collection<EveFasGepSogProvModel> lElenco = null;

		lWb = new HSSFWorkbook();

		lCtrlStatSius = SIUSLookupRemote.getStatisticheSiusRemote();
		lElenco = lCtrlStatSius.ExRicercaAttiIstruttoriDataRestPaginata(aRicercaModel,0);

		// creazione foglio
		lSheet = lWb.createSheet("Elenco Procedimenti");
		lCellStyleNull = lWb.createCellStyle();

		// Intestazione del foglio excel
		lRowCounter = setIntestazione(lSheet, aUfficioUtenteConnesso, lCellStyleNull);
		lRowCounter += 2;

		// Inserimento dei parametri di ricerca.
		lRow = lSheet.createRow(lRowCounter);
		setCell(lRow, 0, "Criteri di ricerca selezionati : ", lCellStyleNull);
		lRowCounter++;

		
		// Stampa filtri di ricerca
		if (aRicercaModel != null) {
			if (aRicercaModel.getAnnoInizio() != null || aRicercaModel.getAnnoFine() != null) {
				lBuffer = "Intervallo Estremi Procedimenti : ";
				if (aRicercaModel.getAnnoInizio() != null) {
					lBuffer += " dal " + aRicercaModel.getAnnoInizio();
				}
				if (aRicercaModel.getNumeroInizio() != null) {
					lBuffer += "/" + aRicercaModel.getNumeroInizio();
				}
				if (aRicercaModel.getAnnoFine() != null) {
					lBuffer += " al " + aRicercaModel.getAnnoFine();
				}
				if (aRicercaModel.getNumeroFine() != null) {
					lBuffer += "/" + aRicercaModel.getNumeroFine();
				}
				lRow = lSheet.createRow(lRowCounter);
				setCell(lRow, 0, lBuffer, lCellStyleNull);
				lRowCounter++;
			}	
			
			if (aRicercaModel.getDataDepositoInizio() != null
					|| aRicercaModel.getDataDepositoFine() != null) {
				lBuffer = "Procedimenti con Data Iscrizione : ";
				if (aRicercaModel.getDataDepositoInizio() != null) {
					lBuffer += " dal "
							+ DateUtils.getDateToString(aRicercaModel.getDataDepositoInizio(), lPatternData);
				}
				if (aRicercaModel.getDataDepositoFine() != null) {
					lBuffer += " al "
							+ DateUtils.getDateToString(aRicercaModel.getDataDepositoFine(), lPatternData);
				}
				lRow = lSheet.createRow(lRowCounter);
				setCell(lRow, 0, lBuffer, lCellStyleNull);
				lRowCounter++;
			}
			
			if (aRicercaModel.getDataRestituzioneInizio() != null
					|| aRicercaModel.getDataRestituzioneFine() != null) {
				lBuffer = "Procedimenti con Data Restituzione : ";
				if (aRicercaModel.getDataRestituzioneInizio() != null) {
					lBuffer += " dal "
							+ DateUtils.getDateToString(aRicercaModel.getDataRestituzioneInizio(), lPatternData);
				}
				if (aRicercaModel.getDataRestituzioneFine() != null) {
					lBuffer += " al "
							+ DateUtils.getDateToString(aRicercaModel.getDataRestituzioneFine(), lPatternData);
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

		lSheet.setColumnWidth(0, 10 * 256); // Prog
		lSheet.setColumnWidth(1, 15 * 256); // Procedimento Sius
		lSheet.setColumnWidth(2, 35 * 256); // Generalità Soggetto
		lSheet.setColumnWidth(3, 15 * 256); // Data Iscrizione
		lSheet.setColumnWidth(4, 15 * 256); // Data Emissione
		lSheet.setColumnWidth(5, 15 * 256); // Data Restituzione
		lSheet.setColumnWidth(6, 35 * 256); // Contenuto
		lSheet.setColumnWidth(7, 45 * 256); // Atto Istruttorio
		lSheet.setColumnWidth(8, 45 * 256); // Stato Procedimento

		// Intestazione colonne
		setCell(lRow, 0, "Progr.", lCellStyleCenter);
		setCell(lRow, 1, "Procedimento Sius", lCellStyleCenter);
		setCell(lRow, 2, "Generalità Soggetto", lCellStyleCenter);
		setCell(lRow, 3, "Data Iscrizione", lCellStyleCenter);
		setCell(lRow, 4, "Data Emissione", lCellStyleCenter);
		setCell(lRow, 5, "Data Restituzione", lCellStyleCenter);
		setCell(lRow, 6, "Contenuto", lCellStyleCenter);
		setCell(lRow, 7, "Atto Istruttorio", lCellStyleCenter);
		setCell(lRow, 8, "Stato Procedimento", lCellStyleCenter);
		
		/*
		HSSFCellStyle lCellStyleIntestazione = null;
		lCellStyleIntestazione = getBordo4Lati(lWb);
		lCellStyleIntestazione.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		lCellStyleIntestazione.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		lCellStyleIntestazione.setWrapText(true);
        HSSFFont fontBold=lWb.createFont();
        fontBold.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        lCellStyleIntestazione.setFont(fontBold);
		 
		setCell(lRow, 0, "Progr.", lCellStyleIntestazione);
		setCell(lRow, 1, "Procedimento Sius", lCellStyleIntestazione);
		setCell(lRow, 2, "Generalità Soggetto", lCellStyleIntestazione);
		setCell(lRow, 3, "Data Iscrizione", lCellStyleIntestazione);
		setCell(lRow, 4, "Data Emissione", lCellStyleIntestazione);
		setCell(lRow, 5, "Data Restituzione", lCellStyleIntestazione);
		setCell(lRow, 6, "Contenuto", lCellStyleIntestazione);
		setCell(lRow, 7, "Atto Istruttorio", lCellStyleIntestazione);
		setCell(lRow, 8, "Stato Procedimento", lCellStyleIntestazione);
		*/
		
		
		int lContatore = 0;
		lItx = lElenco.iterator();
		while (lItx.hasNext()) {
			lModel = (EveFasGepSogProvModel) lItx.next();
			
			lContatore++;
			lRow = lSheet.createRow(lRowCounter++);
			
			setCell(lRow, 0, "" + lContatore, lCellStyleCenter);

			setCell(lRow, 1, "" + lModel.getFascicoloSius().getChiaveAnno() + "/"
					+ lModel.getFascicoloSius().getChiaveProgr(), lCellStyleCenter);

			setCell(lRow, 2, lModel.getFascicoloSius().getSoggetto().getCognome() + " "
					+ lModel.getFascicoloSius().getSoggetto().getNome(), lCellStyleCenter);

			setCell(lRow, 3,
					StringUtils.toStringJSP(DateUtils.getDateToString(
							lModel.getFascicoloSius().getDataIscrizione(), lPatternData), "-"),
					lCellStyleCenter);
			// Data Emissione
			setCell(lRow, 4,
					lModel.getEvento().getDataEmissione() != null ? StringUtils.toStringJSP(
							DateUtils.getDateToString(
									lModel.getEvento().getDataEmissione(), lPatternData),
							"-") : "-",
					lCellStyleCenter);
			
			// Data Restituzione getGeneraleProcedimento().getDescrOggettoProcedimento()
			setCell(lRow, 5,
					lModel.getEvento().getDataRestituzioneAi() != null ? StringUtils.toStringJSP(
							DateUtils.getDateToString(
									lModel.getEvento().getDataRestituzioneAi(), lPatternData),
							"-") : "-",
					lCellStyleCenter);			
			// Contenuto
			setCell(lRow, 6,
					StringUtils.toStringJSP(lModel.getGeneraleProcedimento().getDescrOggettoProcedimento(),"-"),
					lCellStyleCenter);
			// Atto Istruttorio
			setCell(lRow, 7,
					StringUtils.toStringJSP(lModel.getEvento().getDescrMotivo(),"-"),
					lCellStyleCenter);
			// Stato Procedimento
			setCell(lRow, 8,
					StringUtils.toStringJSP(lModel.getFascicoloSius().getDescrStatoFascicolo(),"-"),
					lCellStyleCenter);			
			
		}
		
		lFileOut = new ByteArrayOutputStream();

		try {
			lWb.write(lFileOut);
		} catch (IOException ioe) {
			throw new F3BException("StatisController.creaFoglioProcPerProvvNoValidatiNoDeposito: " + ioe);
		}
		
		return lFileOut;
	}
}
