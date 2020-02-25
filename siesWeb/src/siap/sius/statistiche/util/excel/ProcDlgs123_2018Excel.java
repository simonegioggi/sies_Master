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
import siap.sico.ufficio.model.UfficioModel;
//import siap.sius.fascicolo.util.HSSFUtils;
import siap.sius.statistiche.controller.IStatisticheSius;
import siap.sius.statistiche.model.EveFasGepSogModel;
import siap.sius.statistiche.model.RicercaProcedimentoModel;
import siap.sius.util.SIUSLookupRemote;
import siap.util.excel.SIAPExcelProducer;

public class ProcDlgs123_2018Excel extends SIAPExcelProducer {

	public ByteArrayOutputStream creaExcelProcProcDlgs123_2018(UfficioModel aUfficioUtenteConnesso,
			RicercaProcedimentoModel aRicercaModel) throws F3BException {

		ByteArrayOutputStream lFileOut = null;
		HSSFWorkbook lWb = null;
		IStatisticheSius lCtrlStatSius = null;
		Collection<EveFasGepSogModel> lElenco = null;

		lWb = new HSSFWorkbook();

		lCtrlStatSius = SIUSLookupRemote.getStatisticheSiusRemote();
		lElenco = lCtrlStatSius.ExRicercaProcDlgs123_2018(aUfficioUtenteConnesso, aRicercaModel);
		this.creaFoglioProcDlgs123_2018(lWb, aRicercaModel, lElenco);

		lFileOut = new ByteArrayOutputStream();

		try {
			lWb.write(lFileOut);
		} catch (IOException ioe) {
			throw new F3BException("StatisController.creaFoglioProcAggrIstitutiDetenzione : " + ioe);
		}

		return lFileOut;
	}

	/**
	 * 
	 * @param aWb
	 * @param aElenco
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	private HSSFWorkbook creaFoglioProcDlgs123_2018(HSSFWorkbook aWb, RicercaProcedimentoModel aRicerca,
			Collection<EveFasGepSogModel> aElenco) {

		HSSFSheet lSheet = null;
		HSSFCellStyle lCellStyleNull = null;
		HSSFCellStyle lCellStyleCenter = null;
		HSSFRow lRow = null;
		Iterator lItx = null;
		int lRowCounter = 0;
		EveFasGepSogModel lModel = null;
		String lPatternData = "dd/MM/yyyy";
		String lBuffer = null;

		if (aWb == null)
			aWb = new HSSFWorkbook();

		// creazione primo foglio
		lSheet = aWb.createSheet("Procedimenti(Dlgs 123 del 2018)");
		lCellStyleNull = aWb.createCellStyle();

		// Intestazione del foglio excel
		lRowCounter = setIntestazione(lSheet, aRicerca.getUtenteConnesso().getUfficioUtente(),
				lCellStyleNull);
		lRowCounter += 2;

		// Inserimento dei parametri di ricerca.
		lRow = lSheet.createRow(lRowCounter);
		setCell(lRow, 0, "Criteri di ricerca selezionati : ", lCellStyleNull);
		lRowCounter++;

		// Stampa filtri di ricerca
		if (aRicerca != null) {
			lRow = lSheet.createRow(lRowCounter);
			setCell(lRow, 0, lBuffer, lCellStyleNull);
			lRowCounter++;
			if (aRicerca.getDataIscrizioneInizio() != null || aRicerca.getDataIscrizioneFine() != null) {
				lBuffer = "Procedimenti iscritti : ";
				if (aRicerca.getDataIscrizioneInizio() != null) {
					lBuffer += " dal "
							+ DateUtils.getDateToString(aRicerca.getDataIscrizioneInizio(), lPatternData);
				}
				if (aRicerca.getDataIscrizioneFine() != null) {
					lBuffer += " al "
							+ DateUtils.getDateToString(aRicerca.getDataIscrizioneFine(), lPatternData);
				}
				lRow = lSheet.createRow(lRowCounter);
				setCell(lRow, 0, lBuffer, lCellStyleNull);
				lRowCounter++;
			}
		}

		lRowCounter += 2;

		// stile per celle col bordo con testo centrato
		lCellStyleCenter = getBordo4Lati(aWb);
		lCellStyleCenter.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		lCellStyleCenter.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		lCellStyleCenter.setWrapText(true);

		lRow = lSheet.createRow(lRowCounter++);

		lSheet.setColumnWidth(0, 40 * 256); // Chiave Anno
		lSheet.setColumnWidth(1, 20 * 256); // Chiave Progr
		lSheet.setColumnWidth(2, 30 * 256); // Cognome
		lSheet.setColumnWidth(3, 30 * 256); // Nome

		// Intestazione colonne
		setCell(lRow, 0, "Anno Procedimento", lCellStyleCenter);
		setCell(lRow, 1, "Numero Procedimento", lCellStyleCenter);
		setCell(lRow, 2, "Cognome Soggetto", lCellStyleCenter);
		setCell(lRow, 3, "Nome Soggetto", lCellStyleCenter);

		lItx = aElenco.iterator();
		while (lItx.hasNext()) {
			lModel = (EveFasGepSogModel) lItx.next();
			lRow = lSheet.createRow(lRowCounter++);

			setCell(lRow, 0, "" + lModel.getFascicoloSius().getChiaveAnno(), lCellStyleCenter);
			setCell(lRow, 1, "" + lModel.getFascicoloSius().getChiaveProgr(), lCellStyleCenter);
			setCell(lRow, 2, lModel.getFascicoloSius().getSoggetto().getCognome(), lCellStyleCenter);
			setCell(lRow, 3, lModel.getFascicoloSius().getSoggetto().getNome(), lCellStyleCenter);

		}

		return aWb;
	}

}