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
//import siap.sius.fascicolo.util.HSSFUtils;
import siap.sius.statistiche.controller.IStatisticheSius;
import siap.sius.statistiche.model.EveFasGepSogDetModel;
import siap.sius.statistiche.model.RicercaProcedimentoModel;
import siap.sius.util.SIUSLookupRemote;
import siap.util.excel.SIAPExcelProducer;

@SuppressWarnings("rawtypes")
public class ProcAggregatiPerIstitutoDetenzioneExcel extends SIAPExcelProducer {

	public ByteArrayOutputStream creaExcelProcAggrIstitutiDetenzione(RicercaProcedimentoModel aRicercaModel)
			throws F3BException {

		ByteArrayOutputStream lFileOut = null;
		HSSFWorkbook lWb = null;
		IStatisticheSius lCtrlStatSius = null;
		Collection<EveFasGepSogDetModel> lElenco = null;

		lWb = new HSSFWorkbook();

		lCtrlStatSius = SIUSLookupRemote.getStatisticheSiusRemote();
		lElenco = lCtrlStatSius.ExRicercaProcSoggettiIstitutiDetenzione(aRicercaModel);
		this.creaFoglioAggrSoggettiIstitutiDetenzione(lWb, aRicercaModel, lElenco);

		lElenco = lCtrlStatSius.ExRicercaProcAggrIstitutiDetenzione(aRicercaModel);
		this.creaFoglioAggrIstitutiDetenzione(lWb, aRicercaModel, lElenco);
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
	private HSSFWorkbook creaFoglioAggrSoggettiIstitutiDetenzione(HSSFWorkbook aWb,
			RicercaProcedimentoModel aRicerca, Collection<EveFasGepSogDetModel> aElenco) {

		HSSFSheet lSheet = null;
		HSSFCellStyle lCellStyleNull = null;
		HSSFCellStyle lCellStyleCenter = null;
		HSSFRow lRow = null;
		Iterator lItx = null;
		int lRowCounter = 0;
		EveFasGepSogDetModel lModel = null;
		String lPatternData = "dd/MM/yyyy";
		String lBuffer = null;

		if (aWb == null)
			aWb = new HSSFWorkbook();

		// creazione primo foglio
		lSheet = aWb.createSheet("Elenco dei Procedimenti con soggetti detenuti");
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
		lCellStyleCenter.setAlignment(org.apache.poi.ss.usermodel.HorizontalAlignment.CENTER);
		lCellStyleCenter.setVerticalAlignment(org.apache.poi.ss.usermodel.VerticalAlignment.CENTER);
		lCellStyleCenter.setWrapText(true);

		lRow = lSheet.createRow(lRowCounter++);

		lSheet.setColumnWidth(0, 40 * 256); // Istituto Detenzione
		lSheet.setColumnWidth(1, 20 * 256); // Tipo Istituto Detenzione
		lSheet.setColumnWidth(2, 30 * 256); // Cognome Magistrato
		lSheet.setColumnWidth(3, 30 * 256); // Nome Magistrato
		lSheet.setColumnWidth(4, 10 * 256); // Chiave Anno
		lSheet.setColumnWidth(5, 10 * 256); // Chiave Progr
		lSheet.setColumnWidth(6, 30 * 256); // Cognome
		lSheet.setColumnWidth(7, 30 * 256); // Nome
		lSheet.setColumnWidth(8, 10 * 256); // Data Iscrizione
		lSheet.setColumnWidth(9, 10 * 256); // Stato Procedimento
		lSheet.setColumnWidth(10, 40 * 256); // Contenuto
		lSheet.setColumnWidth(11, 40 * 256); // Oggetto
		lSheet.setColumnWidth(12, 40 * 256); // Posizione Giuridica
		lSheet.setColumnWidth(13, 15 * 256); // Id Istituto Detenzione

		// Intestazione colonne
		setCell(lRow, 0, "Istituto Detenzione", lCellStyleCenter);
		setCell(lRow, 1, "Tipo Istituto Detenzione", lCellStyleCenter);
		setCell(lRow, 2, "Cognome Magistrato", lCellStyleCenter);
		setCell(lRow, 3, "Nome Magistrato", lCellStyleCenter);
		setCell(lRow, 4, "Chiave Anno", lCellStyleCenter);
		setCell(lRow, 5, "Chiave Progr", lCellStyleCenter);
		setCell(lRow, 6, "Cognome", lCellStyleCenter);
		setCell(lRow, 7, "Nome", lCellStyleCenter);
		setCell(lRow, 8, "Data Iscrizione", lCellStyleCenter);
		setCell(lRow, 9, "Stato Procedimento", lCellStyleCenter);
		setCell(lRow, 10, "Contenuto", lCellStyleCenter);
		setCell(lRow, 11, "Oggetto", lCellStyleCenter);
		setCell(lRow, 12, "Posizione Giuridica", lCellStyleCenter);
		setCell(lRow, 13, "Id Istituto Detenzione", lCellStyleCenter);

		lItx = aElenco.iterator();
		while (lItx.hasNext()) {
			lModel = (EveFasGepSogDetModel) lItx.next();
			lRow = lSheet.createRow(lRowCounter++);

			setCell(lRow, 0, lModel.getIstitutoDetenzione().getDescrizione(), lCellStyleCenter);
			setCell(lRow, 1, lModel.getIstitutoDetenzione().getDescrTipoIstituto(), lCellStyleCenter);
			setCell(lRow, 2, lModel.getMagistrato().getCognome(), lCellStyleCenter);
			setCell(lRow, 3, lModel.getMagistrato().getNome(), lCellStyleCenter);
			setCell(lRow, 4, "" + lModel.getFascicoloSius().getChiaveAnno(), lCellStyleCenter);
			setCell(lRow, 5, "" + lModel.getFascicoloSius().getChiaveProgr(), lCellStyleCenter);
			setCell(lRow, 6, lModel.getFascicoloSius().getSoggetto().getCognome(), lCellStyleCenter);
			setCell(lRow, 7, lModel.getFascicoloSius().getSoggetto().getNome(), lCellStyleCenter);
			setCell(lRow, 8,
					StringUtils.toStringJSP(DateUtils.getDateToString(
							lModel.getFascicoloSius().getDataIscrizione(), lPatternData), "-"),
					lCellStyleCenter);
			setCell(lRow, 9, StringUtils.toStringJSP(lModel.getFascicoloSius().getDescrStatoFascicolo(), "-"),
					lCellStyleCenter);
			setCell(lRow, 10,
					lModel.getGeneraleProcedimento() != null
							? lModel.getGeneraleProcedimento().getDescrOggettoProcedimento()
							: "-",
					lCellStyleCenter);

			setCell(lRow, 11, lModel.getGeneraleProcedimento() != null ? "-" : "-", lCellStyleCenter);

			setCell(lRow, 12,
					lModel.getGeneraleProcedimento() != null
							? lModel.getGeneraleProcedimento().getDescrPosGiuridica()
							: "-",
					lCellStyleCenter);

			setCell(lRow, 13,
					lModel.getIstitutoDetenzione() != null
							? lModel.getIstitutoDetenzione().getIdIstitutoDetenzione()
							: "-",
					lCellStyleCenter);
		}

		return aWb;
	}

	/**
	 * 
	 * @param aWb
	 * @param aElenco
	 * @return
	 */
	private HSSFWorkbook creaFoglioAggrIstitutiDetenzione(HSSFWorkbook aWb, RicercaProcedimentoModel aRicerca,
			Collection<EveFasGepSogDetModel> aElenco) {

		HSSFSheet lSheet = null;
		HSSFCellStyle lCellStyleNull = null;
		HSSFCellStyle lCellStyleCenter = null;
		HSSFRow lRow = null;
		Iterator lItx = null;
		int lRowCounter = 0;
		EveFasGepSogDetModel lModel = null;
		String lPatternData = "dd/MM/yyyy";
		String lBuffer = null;

		if (aWb == null)
			aWb = new HSSFWorkbook();

		// creazione primo foglio
		lSheet = aWb.createSheet("Elenco Procedimenti aggregati per istituto detenzione");
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

				lBuffer += " aggregati per istituto detenzione ";

				lRow = lSheet.createRow(lRowCounter);
				setCell(lRow, 0, lBuffer, lCellStyleNull);
				lRowCounter++;
			}
		}

		lRowCounter += 2;

		// stile per celle col bordo con testo centrato
		lCellStyleCenter = getBordo4Lati(aWb);
		lCellStyleCenter.setAlignment(org.apache.poi.ss.usermodel.HorizontalAlignment.CENTER);
		lCellStyleCenter.setVerticalAlignment(org.apache.poi.ss.usermodel.VerticalAlignment.CENTER);
		lCellStyleCenter.setWrapText(true);

		lRow = lSheet.createRow(lRowCounter++);

		lSheet.setColumnWidth(0, 40 * 256); // Istituto Detenzione
		lSheet.setColumnWidth(1, 15 * 256); // Totale Procedimenti

		// Intestazione colonne
		setCell(lRow, 0, "Istituto Detenzione", lCellStyleCenter);
		setCell(lRow, 1, "Totale Procedimenti", lCellStyleCenter);

		lItx = aElenco.iterator();
		while (lItx.hasNext()) {
			lModel = (EveFasGepSogDetModel) lItx.next();
			lRow = lSheet.createRow(lRowCounter++);

			setCell(lRow, 0, lModel.getIstitutoDetenzione().getDescrizione(), lCellStyleCenter);
			setCell(lRow, 1, "" + lModel.getTotale(), lCellStyleCenter);
		}

		return aWb;
	}

}