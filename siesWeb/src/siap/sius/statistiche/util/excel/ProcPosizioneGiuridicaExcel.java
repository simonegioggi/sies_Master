package siap.sius.statistiche.util.excel;

import java.util.Collection;
import java.util.Iterator;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import f3b.util.DateUtils;
import siap.sico.ufficio.model.UfficioModel;
import siap.sius.statistiche.model.EveFasGepSogCancModel;
//import siap.sius.statistiche.model.EveFasGepSogProvModel;
import siap.sius.statistiche.model.RicercaProcedimentoModel;
import siap.util.excel.SIAPExcelProducer;

public class ProcPosizioneGiuridicaExcel extends SIAPExcelProducer {

	@SuppressWarnings("rawtypes")
	public HSSFWorkbook creaProcPosizioneGiuridica(UfficioModel aUfficioUtenteConnesso,
			RicercaProcedimentoModel aRicercaModel, Collection<EveFasGepSogCancModel> aElenco) {

		HSSFWorkbook lWb = null;
		HSSFSheet lSheet = null;
		HSSFCellStyle lCellStyleNull = null;
		HSSFCellStyle lCellStyleCenter = null;
		HSSFRow lRow = null;
		Iterator lItx = null;
		int lContatore = 0;
		int lRowCounter = 0;
		EveFasGepSogCancModel lModel = null;
		String lPatternData = "dd/MM/yyyy";
		String lBuffer = null;
		// int lGiorni = 0;

		lWb = new HSSFWorkbook();

		lSheet = lWb.createSheet(" Elenco Procedimenti per Posizione Giuridica ");
		lCellStyleNull = lWb.createCellStyle();

		// Intestazione del foglio excel
		lRowCounter = setIntestazione(lSheet, aUfficioUtenteConnesso, lCellStyleNull);
		lRowCounter += 2;

		// Stampa filtri di ricerca
		if (aRicercaModel != null) {

			if (aRicercaModel.getDataFinePendenza() != null) {
				lBuffer = "Procedimenti Pendenti al ";
				lBuffer += ""
						+ DateUtils.getDateToString(aRicercaModel.getDataIscrizioneFine(), lPatternData);
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

			if (aRicercaModel.getCodOggettoProcedimento() != null
					&& aRicercaModel.getCodOggettoProcedimento().compareTo("-") != 0) {
				lBuffer = " Procedimenti per tipo Atto : " + aRicercaModel.getDescrOggettoProcedimento();
				lRow = lSheet.createRow(lRowCounter);
				setCell(lRow, 0, lBuffer, lCellStyleNull);
				lRowCounter++;
			}

			if (aRicercaModel.getCodPosizioneGiuridica() != null
					&& aRicercaModel.getCodPosizioneGiuridica().compareTo("-") != 0) {
				lBuffer = " Procedimenti con Posizione Giuridica : "
						+ aRicercaModel.getDescrPosizioneGiuridica();
				lRow = lSheet.createRow(lRowCounter);
				setCell(lRow, 0, lBuffer, lCellStyleNull);
				lRowCounter++;
			}

			// if (aRicercaModel.getDescrMagistrato() != null ) {
			lBuffer = " Magistrato : "
					+ (aRicercaModel.getDescrMagistrato() != null ? aRicercaModel.getDescrMagistrato()
							: "Tutti");
			lRow = lSheet.createRow(lRowCounter);
			setCell(lRow, 0, lBuffer, lCellStyleNull);
			lRowCounter++;
			// }

			if (aRicercaModel.getCodCancelleria() != null
					&& aRicercaModel.getCodCancelleria().compareTo("-") != 0) {
				lBuffer = " Procedimenti con Cancelleria asegnataria : "
						+ aRicercaModel.getDescrCancelleria();
				lRow = lSheet.createRow(lRowCounter);
				setCell(lRow, 0, lBuffer, lCellStyleNull);
				lRowCounter++;
			}

		}

		lRowCounter++;
		lRow = lSheet.createRow(lRowCounter);
		setCell(lRow, 0, " Elenco Procedimenti per Posizione Giuridica ", lCellStyleNull);
		lRowCounter += 2;

		// stile per celle col bordo con testo centrato
		lCellStyleCenter = getBordo4Lati(lWb);
		lCellStyleCenter.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		lCellStyleCenter.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		lCellStyleCenter.setWrapText(true);

		lRow = lSheet.createRow(lRowCounter++);

		lSheet.setColumnWidth(0, 10 * 256); // Prog.
		lSheet.setColumnWidth(1, 15 * 256); // Procedimento Sius
		lSheet.setColumnWidth(2, 35 * 256); // Generalità Soggetto
		lSheet.setColumnWidth(3, 15 * 256); // Data Udienza
		lSheet.setColumnWidth(4, 15 * 256); // Data Arrivo in Cancelleria
		lSheet.setColumnWidth(5, 15 * 256); // Data Iscrizione
		lSheet.setColumnWidth(6, 15 * 256); // Data Definizione
		lSheet.setColumnWidth(7, 35 * 256); // Contenuto
		// lSheet.setColumnWidth( 8, 35 * 256); // Pos. Giuridica
		// lSheet.setColumnWidth( 9, 45 * 256); // Provvedimento

		// Intestazione colonne
		setCell(lRow, 0, "Progr.", lCellStyleCenter);
		setCell(lRow, 1, "Procedimento SIUS", lCellStyleCenter);
		setCell(lRow, 2, "Generalità Soggetto", lCellStyleCenter);
		setCell(lRow, 3, "Data Udienza", lCellStyleCenter);
		setCell(lRow, 4, "Data arrivo Cancelleria", lCellStyleCenter);
		setCell(lRow, 5, "Data Iscrizione", lCellStyleCenter);
		setCell(lRow, 6, "Data Definizione", lCellStyleCenter);
		setCell(lRow, 7, "Contenuto", lCellStyleCenter);
		// setCell(lRow, 8, "Pos. Giuridica", lCellStyleCenter);
		// setCell(lRow, 9, "Provvedimento", lCellStyleCenter);

		lItx = aElenco.iterator();
		while (lItx.hasNext()) {
			lModel = (EveFasGepSogCancModel) lItx.next();

			lContatore++;
			lRow = lSheet.createRow(lRowCounter++);

			setCell(lRow, 0, "" + lContatore, lCellStyleCenter);

			setCell(lRow, 1, "" + lModel.getFascicoloSius().getChiaveAnno() + "/"
					+ lModel.getFascicoloSius().getChiaveProgr(), lCellStyleCenter);

			setCell(lRow, 2, lModel.getFascicoloSius().getSoggetto().getCognome() + " "
					+ lModel.getFascicoloSius().getSoggetto().getNome(), lCellStyleCenter);

			setCell(lRow, 3, DateUtils
					.getDateToString(lModel.getGeneraleProcedimento().getDataCameraConsiglio(), lPatternData),
					lCellStyleCenter);

			setCell(lRow, 4,
					DateUtils.getDateToString(lModel.getGeneraleProcedimento().getDataArrivoCancelleria(),
							lPatternData),
					lCellStyleCenter);

			setCell(lRow, 5,
					DateUtils.getDateToString(lModel.getFascicoloSius().getDataIscrizione(), lPatternData),
					lCellStyleCenter);

			setCell(lRow, 6,
					DateUtils.getDateToString(lModel.getFascicoloSius().getDataDefinizione(), lPatternData),
					lCellStyleCenter);

			setCell(lRow, 7, lModel.getGeneraleProcedimento().getDescrOggettoProcedimento(),
					lCellStyleCenter);

			/*
			 * setCell(lRow, 6, lModel.getGeneraleProcedimento().getDescrPosGiuridica(), lCellStyleCenter);
			 */

			/*
			 * setCell(lRow, 7, ( lModel.getEvento() != null ? lModel.getEvento().getDescrTipoProvvedimento()
			 * + " - " + lModel.getEvento().getDescrEsito() + " - " + lModel.getEvento().getDescrMotivo() :
			 * " - "), lCellStyleCenter);
			 */
		}
		return lWb;
	}

}