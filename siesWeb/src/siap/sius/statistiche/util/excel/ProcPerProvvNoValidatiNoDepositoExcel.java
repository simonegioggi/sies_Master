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

public class ProcPerProvvNoValidatiNoDepositoExcel extends SIAPExcelProducer {

	@SuppressWarnings("rawtypes")
	public ByteArrayOutputStream creaFoglioProcPerProvvNoValidatiNoDeposito(
			UfficioModel aUfficioUtenteConnesso, RicercaProcedimentoModel aRicercaModel) throws F3BException {

		ByteArrayOutputStream lFileOut = null;
		HSSFWorkbook lWb = null;
		HSSFSheet lSheet = null;
		HSSFCellStyle lCellStyleNull = null;
		HSSFCellStyle lCellStyleCenter = null;
		HSSFRow lRow = null;
		Iterator lItx = null;
		int lContatore = 0;
		int lRowCounter = 0;
		EveFasGepSogProvModel lModel = null;
		String lPatternData = "dd/MM/yyyy";
		String lBuffer = null;
		IStatisticheSius lCtrlStatSius = null;
		Collection<EveFasGepSogProvModel> lElenco = null;

		lWb = new HSSFWorkbook();

		lCtrlStatSius = SIUSLookupRemote.getStatisticheSiusRemote();
		lElenco = lCtrlStatSius.ExRicercaProcPerProvvNoValidatiNoDeposito(aRicercaModel);

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

			switch (aRicercaModel.getStatoProcedimento()) {
			case 0:
				lBuffer = "Procedimenti Privi di Provvedimento";
				break;
			case 1:
				lBuffer = "Procedimenti con Provvedimenti Emessi non Validati";
				break;
			case 2:
				lBuffer = "Procedimenti con Provvedimenti non Depositati";
				break;
			case 3:
				lBuffer = "Procedimenti con Provvedimenti Depositati non Validati";
				break;
			}
			lRow = lSheet.createRow(lRowCounter);
			setCell(lRow, 0, lBuffer, lCellStyleNull);
			lRowCounter++;

			if (aRicercaModel.getAnnoInizio() != null || aRicercaModel.getAnnoFine() != null) {
				lBuffer = "Intervallo Estremi Procedimenti : ";
				if (aRicercaModel.getAnnoInizio() != null) {
					lBuffer += " da " + aRicercaModel.getAnnoInizio() + "/" + aRicercaModel.getNumeroInizio();
				}
				if (aRicercaModel.getAnnoFine() != null) {
					lBuffer += " a " + aRicercaModel.getAnnoFine() + "/" + aRicercaModel.getNumeroFine();
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
		lSheet.setColumnWidth(4, 15 * 256); // Data Udienza
		lSheet.setColumnWidth(5, 15 * 256); // Data Emissione
		lSheet.setColumnWidth(6, 35 * 256); // Tipo Provveedimento
		lSheet.setColumnWidth(7, 45 * 256); // Oggetto
		lSheet.setColumnWidth(8, 45 * 256); // Esito
		lSheet.setColumnWidth(9, 25 * 256); // Provvedimento Validato
		lSheet.setColumnWidth(10, 15 * 256); // Data Deposito
		lSheet.setColumnWidth(11, 25 * 256); // Deposito Validato

		// Intestazione colonne
		setCell(lRow, 0, "Progr.", lCellStyleCenter);
		setCell(lRow, 1, "Procedimento Sius", lCellStyleCenter);
		setCell(lRow, 2, "Generalità Soggetto", lCellStyleCenter);
		setCell(lRow, 3, "Data Iscrizione", lCellStyleCenter);
		setCell(lRow, 4, "Data Udienza", lCellStyleCenter);
		setCell(lRow, 5, "Data Emissione", lCellStyleCenter);
		setCell(lRow, 6, "Tipo Provvedimento", lCellStyleCenter);
		setCell(lRow, 7, "Oggetto", lCellStyleCenter);
		setCell(lRow, 8, "Esito", lCellStyleCenter);
		setCell(lRow, 9, "Provvedimento Validato", lCellStyleCenter);
		setCell(lRow, 10, "Data Deposito", lCellStyleCenter);
		setCell(lRow, 11, "Deposito Validato", lCellStyleCenter);

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

			setCell(lRow, 4,
					lModel.getGeneraleProcedimento() != null ? StringUtils.toStringJSP(
							DateUtils.getDateToString(
									lModel.getGeneraleProcedimento().getDataCameraConsiglio(), lPatternData),
							"-") : "-",
					lCellStyleCenter);

			if (aRicercaModel.getStatoProcedimento() == 0) {
				setCell(lRow, 5, "-", lCellStyleCenter);
				setCell(lRow, 6, "-", lCellStyleCenter);
				setCell(lRow, 8, "-", lCellStyleCenter);
				setCell(lRow, 9, "-", lCellStyleCenter);
				setCell(lRow, 10, "-", lCellStyleCenter);
				setCell(lRow, 11, "-", lCellStyleCenter);

			} else {
				setCell(lRow, 5,
						lModel.getEvento() != null ? StringUtils.toStringJSP(DateUtils
								.getDateToString(lModel.getEvento().getDataEmissione(), lPatternData), "-")
								: "-",
						lCellStyleCenter);

				// Tipo Provvedimento
				setCell(lRow, 6,
						lModel.getEvento() != null ? lModel.getEvento().getDescrTipoProvvedimento() : "-",
						lCellStyleCenter);
				// Esito
				setCell(lRow, 8, (lModel.getEvento() != null ? lModel.getEvento().getDescrEsito() : " - "),
						lCellStyleCenter);

				setCell(lRow, 9, (lModel.getEvento() != null ? lModel.getEvento().getFlagDocumentoRegistrato()
						: " - "), lCellStyleCenter);

				setCell(lRow, 10,
						lModel.getDocumentoAllegato() != null
								? StringUtils.toStringJSP(DateUtils.getDateToString(
										lModel.getDocumentoAllegato().getDataEmissione(), lPatternData), "-")
								: "-",
						lCellStyleCenter);
				//
				setCell(lRow, 11,
						lModel.getDocumentoAllegato() != null
								? lModel.getDocumentoAllegato().getFlagDocumentoRegistrato()
								: "-",
						lCellStyleCenter);

			}

			// Oggetto
			setCell(lRow, 7, lModel.getEvento() != null ? lModel.getEvento().getDescrMotivo() : "-",
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