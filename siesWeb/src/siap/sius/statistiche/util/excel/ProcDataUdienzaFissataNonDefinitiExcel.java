package siap.sius.statistiche.util.excel;

import java.util.Collection;
import java.util.Iterator;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import f3b.util.DateUtils;
import f3b.util.StringUtils;
import siap.sico.ufficio.model.UfficioModel;
import siap.sius.statistiche.model.EveFasGepSogProvModel;
import siap.sius.statistiche.model.RicercaProcedimentoModel;
import siap.util.excel.SIAPExcelProducer;

public class ProcDataUdienzaFissataNonDefinitiExcel extends SIAPExcelProducer {

	@SuppressWarnings("rawtypes")
	public HSSFWorkbook creaFoglioProcFissatiNonDef(UfficioModel aUfficioUtenteConnesso,
			RicercaProcedimentoModel aRicercaModel, Collection<EveFasGepSogProvModel> aElenco) {

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

		lWb = new HSSFWorkbook();

		lSheet = lWb.createSheet("Elenco Procedimenti con Data Udienza fissata e non Derfiniti");
		lCellStyleNull = lWb.createCellStyle();

		// Intestazione del foglio excel
		lRowCounter = setIntestazione(lSheet, aUfficioUtenteConnesso, lCellStyleNull);
		lRowCounter += 2;

		// Stampa filtri di ricerca
		if (aRicercaModel != null) {

			if (aRicercaModel.getDataIscrizioneInizio() != null
					|| aRicercaModel.getDataIscrizioneFine() != null) {
				lBuffer = "Procedimenti con Data Iscrizione";
				if (aRicercaModel.getDataIscrizioneInizio() != null) {
					lBuffer += " dal " + DateUtils.getDateToString(aRicercaModel.getDataIscrizioneInizio(),
							lPatternData);
				}
				if (aRicercaModel.getDataIscrizioneFine() != null) {
					lBuffer += " al "
							+ DateUtils.getDateToString(aRicercaModel.getDataIscrizioneFine(), lPatternData);
				}
				lRow = lSheet.createRow(lRowCounter);
				setCell(lRow, (short) 0, lBuffer, lCellStyleNull);
				lRowCounter++;
			}

			if (aRicercaModel.getDataCameraConsiglioInizio() != null
					|| aRicercaModel.getDataCameraConsiglioFine() != null) {
				lBuffer = "Procedimenti con Data Udienza Fissata";
				if (aRicercaModel.getDataCameraConsiglioInizio() != null) {
					lBuffer += " dal " + DateUtils
							.getDateToString(aRicercaModel.getDataCameraConsiglioInizio(), lPatternData);
				}
				if (aRicercaModel.getDataCameraConsiglioFine() != null) {
					lBuffer += " al " + DateUtils.getDateToString(aRicercaModel.getDataCameraConsiglioFine(),
							lPatternData);
				}
				lRow = lSheet.createRow(lRowCounter);
				setCell(lRow, (short) 0, lBuffer, lCellStyleNull);
				lRowCounter++;
			}

			if (aRicercaModel.getCodOggettoProcedimento() != null
					&& aRicercaModel.getCodOggettoProcedimento().compareTo("-") != 0) {
				lBuffer = "Solo Procedimenti di : " + aRicercaModel.getDescrOggettoProcedimento();
				lRow = lSheet.createRow(lRowCounter);
				setCell(lRow, (short) 0, lBuffer, lCellStyleNull);
				lRowCounter++;
			}

			if (aRicercaModel.getCodPosizioneGiuridica() != null
					&& aRicercaModel.getCodPosizioneGiuridica().compareTo("-") != 0) {
				lBuffer = "Procedimenti con Posizione Giuridica : "
						+ aRicercaModel.getDescrPosizioneGiuridica();
				lRow = lSheet.createRow(lRowCounter);
				setCell(lRow, (short) 0, lBuffer, lCellStyleNull);
				lRowCounter++;
			}
		}

		lRowCounter++;
		lRow = lSheet.createRow(lRowCounter);
		setCell(lRow, (short) 0, "Elenco Procedimenti con Data Udienza Fissata non Definiti", lCellStyleNull);
		lRowCounter += 2;

		// stile per celle col bordo con testo centrato
		lCellStyleCenter = getBordo4Lati(lWb);
		lCellStyleCenter.setAlignment(org.apache.poi.ss.usermodel.HorizontalAlignment.CENTER);
		lCellStyleCenter.setVerticalAlignment(org.apache.poi.ss.usermodel.VerticalAlignment.CENTER);
		lCellStyleCenter.setWrapText(true);

		lRow = lSheet.createRow(lRowCounter++);

		lSheet.setColumnWidth(0, 10 * 256); // Prog
		lSheet.setColumnWidth(1, 15 * 256); // Procedimento Sius
		lSheet.setColumnWidth(2, 35 * 256); // Generalit? Soggetto
		lSheet.setColumnWidth(3, 15 * 256); // Data Udienza
		lSheet.setColumnWidth(4, 15 * 256); // Data Iscrzione
		lSheet.setColumnWidth(5, 35 * 256); // Contenuto
		lSheet.setColumnWidth(6, 35 * 256); // Pos. Giuridica
		lSheet.setColumnWidth(7, 45 * 256); // Provvedimento

		// Intestazione colonne
		setCell(lRow, (short) 0, "Prog", lCellStyleCenter);
		setCell(lRow, (short) 1, "Procedimento SIUS", lCellStyleCenter);
		setCell(lRow, (short) 2, "Generalit? Soggetto", lCellStyleCenter);
		setCell(lRow, (short) 3, "Data Udienza", lCellStyleCenter);
		setCell(lRow, (short) 4, "Data Iscrizione", lCellStyleCenter);
		setCell(lRow, (short) 5, "Contenuto", lCellStyleCenter);
		setCell(lRow, (short) 6, "Pos. Giuridica", lCellStyleCenter);
		setCell(lRow, (short) 7, "Provvedimento", lCellStyleCenter);

		lItx = aElenco.iterator();
		while (lItx.hasNext()) {
			lModel = (EveFasGepSogProvModel) lItx.next();

			lContatore++;
			lRow = lSheet.createRow(lRowCounter++);

			setCell(lRow, (short) 0, "" + lContatore, lCellStyleCenter);

			setCell(lRow, (short) 1, "" + lModel.getFascicoloSius().getChiaveAnno() + "/"
					+ lModel.getFascicoloSius().getChiaveProgr(), lCellStyleCenter);

			setCell(lRow, (short) 2, lModel.getFascicoloSius().getSoggetto().getCognome() + " "
					+ lModel.getFascicoloSius().getSoggetto().getNome(), lCellStyleCenter);

			setCell(lRow, (short) 3, DateUtils
					.getDateToString(lModel.getGeneraleProcedimento().getDataCameraConsiglio(), lPatternData),
					lCellStyleCenter);

			setCell(lRow, (short) 4,
					DateUtils.getDateToString(lModel.getFascicoloSius().getDataInserimento(), lPatternData),
					lCellStyleCenter);

			setCell(lRow, (short) 5, lModel.getGeneraleProcedimento().getDescrOggettoProcedimento(),
					lCellStyleCenter);

			setCell(lRow, (short) 6, lModel.getGeneraleProcedimento().getDescrPosGiuridica(),
					lCellStyleCenter);

			setCell(lRow, (short) 7,
					(lModel.getEvento() != null ? StringUtils
							.toStringJSP(DateUtils.getDateToString(lModel.getEvento().getDataEmissione(),
									lPatternData), " - ")
							+ " - "
							+ StringUtils.toStringJSP(lModel.getEvento().getDescrTipoProvvedimento(), "")
							+ " - " + StringUtils.toStringJSP(lModel.getEvento().getDescrEsito(), "") + " - "
							+ StringUtils.toStringJSP(lModel.getEvento().getDescrMotivo(), "") : " - "),
					lCellStyleCenter);
		}

		return lWb;
	}

}