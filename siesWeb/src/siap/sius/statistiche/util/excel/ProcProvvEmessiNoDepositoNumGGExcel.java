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
import siap.sius.statistiche.controller.IStatisticheSius;
import siap.sius.statistiche.model.EveFasGepSogProvModel;
import siap.sius.statistiche.model.RicercaProcedimentoModel;
import siap.sius.util.SIUSLookupRemote;
import siap.util.excel.SIAPExcelProducer;

public class ProcProvvEmessiNoDepositoNumGGExcel extends SIAPExcelProducer {

	@SuppressWarnings("rawtypes")
	public ByteArrayOutputStream creaFoglioProcProvvEmessiNoDepositoNumGG(UfficioModel aUfficioUtenteConnesso,
			RicercaProcedimentoModel aRicercaModel) throws F3BException {

		ByteArrayOutputStream lFileOut = null;
		HSSFWorkbook lWb = null;
		HSSFSheet lSheet = null;
		HSSFCellStyle lCellStyleNull = null;
		HSSFCellStyle lCellStyleCenter = null;
		HSSFRow lRow = null;
		Iterator lItx = null;
		int lRowCounter = 0;
		int lContatore = 0;
		EveFasGepSogProvModel lModel = null;
		String lPatternData = "dd/MM/yyyy";
		String lBuffer = null;
		IStatisticheSius lCtrlStatSius = null;
		Collection<EveFasGepSogProvModel> lElenco = null;

		lWb = new HSSFWorkbook();

		lSheet = lWb.createSheet("Elenco Procedimenti");
		lCellStyleNull = lWb.createCellStyle();

		// Intestazione del foglio excel
		lRowCounter = setIntestazione(lSheet, aRicercaModel.getUtenteConnesso().getUfficioUtente(),
				lCellStyleNull);
		lRowCounter++;

		// Inserimento dei parametri di ricerca.
		lRow = lSheet.createRow(lRowCounter);
		setCell(lRow, 0, "Elenco Procedimenti con Provvedimenti emessi non depositati ", lCellStyleNull);
		lRowCounter += 2;

		// Stampa filtri di ricerca
		if (aRicercaModel != null) {
			lRow = lSheet.createRow(lRowCounter);
			setCell(lRow, 0, lBuffer, lCellStyleNull);
			lRowCounter++;

			if (aRicercaModel.getDataEmissioneInizio() != null
					|| aRicercaModel.getDataEmissioneFine() != null) {
				lBuffer = "Procedimenti con Data Emissione Provvedimento : ";
				if (aRicercaModel.getDataEmissioneInizio() != null) {
					lBuffer += " dal "
							+ DateUtils.getDateToString(aRicercaModel.getDataEmissioneInizio(), lPatternData);
				}
				if (aRicercaModel.getDataEmissioneFine() != null) {
					lBuffer += " al "
							+ DateUtils.getDateToString(aRicercaModel.getDataEmissioneFine(), lPatternData);
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
				lBuffer = "Solo i Procedimenti con Provvedimenti emessi da almeno "
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
		lSheet.setColumnWidth(4, 15 * 256); // Data Emissione
		lSheet.setColumnWidth(5, 50 * 256); // Provvedimento
		lSheet.setColumnWidth(6, 10 * 256); // Numero Giorni
		lSheet.setColumnWidth(7, 45 * 256); // Magistrato

		// Intestazione colonne
		setCell(lRow, 0, "Prog.", lCellStyleCenter);
		setCell(lRow, 1, "Procedimento SIUS", lCellStyleCenter);
		setCell(lRow, 2, "Generalità Soggetto", lCellStyleCenter);
		setCell(lRow, 3, "Data Iscrizione", lCellStyleCenter);
		setCell(lRow, 4, "Data Emissione", lCellStyleCenter);
		setCell(lRow, 5, "Provvedimento", lCellStyleCenter);
		setCell(lRow, 6, "N.ro giorni trascorsi", lCellStyleCenter);
		setCell(lRow, 7, "Magistrato", lCellStyleCenter);

		lCtrlStatSius = SIUSLookupRemote.getStatisticheSiusRemote();
		lElenco = lCtrlStatSius.ExRicercaProcProvvEmessiNoDepNumGG(aRicercaModel);

		lItx = lElenco.iterator();
		while (lItx.hasNext()) {
			lContatore++;
			lModel = (EveFasGepSogProvModel) lItx.next();
			lRow = lSheet.createRow(lRowCounter++);

			setCell(lRow, 0, lContatore + "", lCellStyleCenter);
			if (lModel.getFascicoloSius() != null && lModel.getFascicoloSius().getChiaveAnno() != null
					&& lModel.getFascicoloSius().getChiaveProgr() != null) {
				setCell(lRow, 1, lModel.getFascicoloSius().getChiaveAnno() + "/"
						+ lModel.getFascicoloSius().getChiaveProgr(), lCellStyleCenter);
			} else {
				setCell(lRow, 1, "", lCellStyleCenter);
			}
			if (lModel.getFascicoloSius() != null && lModel.getFascicoloSius().getSoggetto() != null) {
				setCell(lRow, 2,
						StringUtils.toStringJSP(lModel.getFascicoloSius().getSoggetto().getCognome()) + " "
								+ StringUtils.toStringJSP(lModel.getFascicoloSius().getSoggetto().getNome()),
						lCellStyleCenter);
			} else {
				setCell(lRow, 2, "-", lCellStyleCenter);
			}
			if (lModel.getFascicoloSius() != null) {
				setCell(lRow, 3,
						StringUtils.toStringJSP(DateUtils.getDateToString(
								lModel.getFascicoloSius().getDataIscrizione(), "dd-MM-yyyy"), "-"),
						lCellStyleCenter);
			} else {
				setCell(lRow, 3, "-", lCellStyleCenter);
			}
			if (lModel.getEvento() != null) {
				setCell(lRow, 4,
						StringUtils.toStringJSP(DateUtils
								.getDateToString(lModel.getEvento().getDataEmissione(), "dd-MM-yyyy"), "-"),
						lCellStyleCenter);
			} else {
				setCell(lRow, 4, "-", lCellStyleCenter);
			}
			if (lModel.getGeneraleProcedimento() != null) {
				setCell(lRow, 5,
						StringUtils.toStringJSP(
								lModel.getGeneraleProcedimento().getDescrOggettoProcedimento(), "-"),
						lCellStyleCenter);
			} else {
				setCell(lRow, 5, "-", lCellStyleCenter);
			}
			setCell(lRow, 6, StringUtils.toStringJSP(lModel.getTotale()), lCellStyleCenter);

			if (lModel.getMagistrato() != null) {
				String lMag = lModel.getMagistrato().getCognome() + " " + lModel.getMagistrato().getNome();
				setCell(lRow, 7, lMag, lCellStyleCenter);
			} else {
				setCell(lRow, 7, "-", lCellStyleCenter);
			}

		}

		lFileOut = new ByteArrayOutputStream();
		try {
			lWb.write(lFileOut);
		} catch (IOException ioe) {
			throw new F3BException("StatisController.creaFoglioProcProvvEmessiNoDepositoNumGG : " + ioe);
		}

		return lFileOut;
	}

}