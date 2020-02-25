package siap.sige.statistiche.controller;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Iterator;
import java.util.Vector;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import f3b.controller.GenericController;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.util.StringUtils;
import f3b.util.Utils;
import siap.sico.soggetto.controller.ISoggetto;
import siap.sico.soggetto.model.SoggettoModel;
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.util.SICOLookupRemote;
import siap.sige.fascicolo.model.FascicoloSigeEstesoModel;
import siap.sige.fascicolo.model.RicercaFascicoloSigeModel;
import siap.sige.statistiche.model.EveFasGepSogProvModel;
import siap.sige.statistiche.model.RicercaFogliCompModel;
import siap.sige.statistiche.model.RiepilogoStatisticheFogliComplementari;
import siap.sige.statistiche.model.StatisticheFogliComplementariContainerModel;
import siap.sige.statistiche.model.StatisticheFogliComplementariModel;
import siap.sige.util.SIGELookupRemote;
import siap.sius.fascicolo.util.HSSFUtils;
import siap.sius.statistiche.action.ICostantiStatistiche;

/**
 *
 * <p>
 * Title: StatisController
 * </p>
 * <p>
 * Description: Controller per le statistiche
 * </p>
 * <p>
 * </p>
 * <p>
 * Company: Bull Italia S.p.A.
 * </p>
 */
@SuppressWarnings("rawtypes")
public class StatisController extends GenericController {

	private HSSFCell setCell(HSSFRow row, short nCol, String value, HSSFCellStyle cs) {

		HSSFCell cell = row.createCell(nCol);
		cell.setCellValue(value);
		cell.setCellStyle(cs);

		return cell;
	}

	// private HSSFCell setCell(HSSFRow row, short nCol, double value, HSSFCellStyle cs) {
	//
	// HSSFCell cell = row.createCell(nCol);
	// cell.setCellValue(value);
	// cell.setCellStyle(cs);
	//
	// return cell;
	// }

	// private HSSFCell setFormulaCell(HSSFRow row, short nCol, String value, HSSFCellStyle cs) {
	//
	// HSSFCell cell = row.createCell(nCol);
	// // cell.setCellType(HSSFCell.CELL_TYPE_FORMULA);
	// cell.setCellFormula(value);
	// cell.setCellStyle(cs);
	//
	// return cell;
	// }

	private HSSFCell setCell(HSSFRow row, short nCol, String value) {

		HSSFCell cell = row.createCell(nCol);
		cell.setCellValue(value);

		return cell;
	}

	public HSSFWorkbook getReportStatisticheFogliComplementari(
			StatisticheFogliComplementariContainerModel container) {
		HSSFWorkbook wb = new HSSFWorkbook();
		Vector<StatisticheFogliComplementariModel> iscrittiManualmente = container.getFcIscrittiManualmente();
		Vector<StatisticheFogliComplementariModel> fcAnnullati = container.getFcAnnullati();
		Vector<StatisticheFogliComplementariModel> provvedimentiPriviFC = container.getProvvedimentiPriviFc();
		Vector<StatisticheFogliComplementariModel> provvedimentiConFC = container.getProvvedimentiConFc();

		int row = 0;

		HSSFSheet riepilogoSheet = wb.createSheet("Riepilogo");
		row = writeIntestazioneRiepilogo(container, riepilogoSheet, wb);
		elaboraRiepilogo(riepilogoSheet, row, container, wb);

		if (iscrittiManualmente != null) {
			HSSFSheet sheetIscrittiManualmente = wb.createSheet("FC Iscritti Manualmente");
			row = writeIntestazioneStatisticheFC(container, sheetIscrittiManualmente, wb);
			elaboraSheetStatisticheFC(iscrittiManualmente, sheetIscrittiManualmente, row);
		}

		if (fcAnnullati != null) {
			HSSFSheet sheetAnnullati = wb.createSheet("FC Annullati");
			row = writeIntestazioneStatisticheFC(container, sheetAnnullati, wb);
			elaboraSheetStatisticheFC(fcAnnullati, sheetAnnullati, row);
		}

		if (provvedimentiPriviFC != null) {
			HSSFSheet sheetProvvedimentiPriviFc = wb.createSheet("Provvedimenti privi di FC");
			row = writeIntestazioneStatisticheFC(container, sheetProvvedimentiPriviFc, wb);
			elaboraSheetStatisticheFC(provvedimentiPriviFC, sheetProvvedimentiPriviFc, row);
		}

		if (provvedimentiConFC != null && provvedimentiConFC.size() > 0) {
			HSSFSheet sheetProvvedimentiConFc = wb.createSheet("Provvedimenti Con FC");
			row = writeIntestazioneStatisticheFC(container, sheetProvvedimentiConFc, wb);
			elaboraSheetStatisticheFC(provvedimentiConFC, sheetProvvedimentiConFc, row);
		}

		return wb;
	}

	private int writeIntestazioneRiepilogo(StatisticheFogliComplementariContainerModel container,
			HSSFSheet sheet, HSSFWorkbook wb) {
		int nRow = 0;
		HSSFRow row = sheet.createRow(nRow);

		UfficioModel uffUteConnesso = container.getUffUteConnesso();
		RicercaFogliCompModel filtro = container.getFiltro();
		String value = (uffUteConnesso.getDescrTipoUfficio().toUpperCase() + " DI "
				+ uffUteConnesso.getDescrComune().toUpperCase());
		setCell(row, (short) 0, "Ufficio", getBoldStyle(wb));
		setCell(row, (short) 1, value);

		row = sheet.createRow(++nRow);
		setCell(row, (short) 0, "Elaborato il: ", getBoldStyle(wb));
		Date oggi = new Date();

		setCell(row, (short) 1, DateUtils.getDateToString(oggi, "dd-MM-yyyy"));

		String lCriterio1 = "";
		String lCriterio2 = "";

		if (filtro.getAnnoIniziale() != null) {
			if (filtro.getAnnoFinale() == null) {
				lCriterio1 = "Anno: " + filtro.getAnnoIniziale();
			}

			if (filtro.getAnnoFinale() != null) {
				lCriterio1 = "Dall'Anno: " + filtro.getAnnoIniziale() + " all'anno " + filtro.getAnnoFinale();
			}
		}

		if (filtro.getDataEmissioneIniziale() != null) {
			if (filtro.getDataEmissioneFinale() == null) {
				lCriterio2 = "Data Compilazione: "
						+ DateUtils.getDateToString(filtro.getDataEmissioneIniziale(), "dd-MM-yyyy");
			}

			if (filtro.getDataEmissioneFinale() != null) {
				lCriterio2 = "Data Compilazione dal"
						+ DateUtils.getDateToString(filtro.getDataEmissioneIniziale(), "dd-MM-yyyy") + " al "
						+ DateUtils.getDateToString(filtro.getDataEmissioneFinale(), "dd-MM-yyyy");
			}
		}

		row = sheet.createRow(++nRow);
		setCell(row, (short) 0, "Criteri di Ricerca selezionati:", getBoldStyle(wb));
		setCell(row, (short) 1, lCriterio1);
		setCell(row, (short) 2, lCriterio2);

		row = sheet.createRow(++nRow);
		row = sheet.createRow(++nRow);
		Vector<String> testataRiepilogo = container.getTestataRiepilogo();
		Iterator<String> it = testataRiepilogo.iterator();
		short cellIndex = 0;
		while (it.hasNext()) {
			setCell(row, cellIndex, it.next(), getBoldStyle(wb));
			cellIndex++;
		}

		setCell(row, cellIndex, "Totale", getBoldStyle(wb));
		return nRow;
	}

	private int elaboraRiepilogo(HSSFSheet riepilogoSheet, int nRow,
			StatisticheFogliComplementariContainerModel container, HSSFWorkbook wb) {
		Vector<RiepilogoStatisticheFogliComplementari> fcAnnullati = container.getRiepilogoAnnullati();
		Vector<RiepilogoStatisticheFogliComplementari> fcIscrittiManualmente = container
				.getRiepilogFCIscrittiManualmente();
		Vector<RiepilogoStatisticheFogliComplementari> provvedimentiPriviFC = container
				.getRiepilogoProvvedimentiPriviFC();
		Vector<RiepilogoStatisticheFogliComplementari> provvedimentiConFC = container
				.getRiepilogoProvvedimentiConFC();

		if (fcIscrittiManualmente.size() > 0) {
			HSSFRow row = riepilogoSheet.createRow(++nRow);
			Iterator<RiepilogoStatisticheFogliComplementari> it = fcIscrittiManualmente.iterator();
			int totale = 0;
			short cellIndex = 0;

			RiepilogoStatisticheFogliComplementari riepilogo = it.next();
			setCell(row, cellIndex, riepilogo.getDescrizione(), getBoldStyle(wb));
			cellIndex += 1;
			setCell(row, cellIndex, riepilogo.getConteggio().toString());
			totale += riepilogo.getConteggio().intValue();

			while (it.hasNext()) {
				riepilogo = it.next();
				// BigDecimal totAnno = riepilogo.getConteggio();
				cellIndex += 1;
				setCell(row, cellIndex, riepilogo.getConteggio().toString());
				totale += riepilogo.getConteggio().intValue();
			}
			cellIndex += 1;
			setCell(row, cellIndex, String.valueOf(totale));
		}

		if (fcAnnullati.size() > 0) {
			HSSFRow row = riepilogoSheet.createRow(++nRow);
			Iterator<RiepilogoStatisticheFogliComplementari> it = fcAnnullati.iterator();
			int totale = 0;
			short cellIndex = 0;
			RiepilogoStatisticheFogliComplementari riepilogo = it.next();
			setCell(row, cellIndex, riepilogo.getDescrizione(), getBoldStyle(wb));
			cellIndex++;
			setCell(row, cellIndex, riepilogo.getConteggio().toString());
			totale += riepilogo.getConteggio().intValue();

			while (it.hasNext()) {
				riepilogo = it.next();
				BigDecimal totAnno = riepilogo.getConteggio();
				cellIndex++;
				setCell(row, cellIndex, totAnno.toString());
				totale += totAnno.intValue();
			}

			setCell(row, ++cellIndex, String.valueOf(totale));
		}

		if (provvedimentiPriviFC.size() > 0) {
			HSSFRow row = riepilogoSheet.createRow(++nRow);
			Iterator<RiepilogoStatisticheFogliComplementari> it = provvedimentiPriviFC.iterator();
			int totale = 0;
			short cellIndex = 0;
			RiepilogoStatisticheFogliComplementari riepilogo = it.next();
			setCell(row, cellIndex, riepilogo.getDescrizione(), getBoldStyle(wb));
			cellIndex++;
			setCell(row, cellIndex, riepilogo.getConteggio().toString());
			totale += riepilogo.getConteggio().intValue();

			while (it.hasNext()) {
				riepilogo = it.next();
				BigDecimal totAnno = riepilogo.getConteggio();
				cellIndex++;
				setCell(row, cellIndex, totAnno.toString());
				totale += totAnno.intValue();
			}

			setCell(row, ++cellIndex, String.valueOf(totale));
		}

		if (provvedimentiConFC.size() > 0) {
			HSSFRow row = riepilogoSheet.createRow(++nRow);
			Iterator<RiepilogoStatisticheFogliComplementari> it = provvedimentiConFC.iterator();
			int totale = 0;
			short cellIndex = 0;
			RiepilogoStatisticheFogliComplementari riepilogo = it.next();
			setCell(row, cellIndex, riepilogo.getDescrizione(), getBoldStyle(wb));
			cellIndex++;
			setCell(row, cellIndex, riepilogo.getConteggio().toString());
			totale += riepilogo.getConteggio().intValue();

			while (it.hasNext()) {
				riepilogo = it.next();
				BigDecimal totAnno = riepilogo.getConteggio();
				cellIndex++;
				setCell(row, cellIndex, totAnno.toString());
				totale += totAnno.intValue();
			}

			setCell(row, ++cellIndex, String.valueOf(totale));
		}
		return nRow;
	}

	private int writeIntestazioneStatisticheFC(StatisticheFogliComplementariContainerModel container,
			HSSFSheet sheet, HSSFWorkbook wb) {
		int nRow = 0;
		HSSFRow row = sheet.createRow(nRow);

		UfficioModel uffUteConnesso = container.getUffUteConnesso();
		RicercaFogliCompModel filtro = container.getFiltro();
		String value = (uffUteConnesso.getDescrTipoUfficio().toUpperCase() + " DI "
				+ uffUteConnesso.getDescrComune().toUpperCase());
		setCell(row, (short) 0, "Ufficio", getBoldStyle(wb));
		setCell(row, (short) 1, value);

		row = sheet.createRow(++nRow);
		setCell(row, (short) 0, "Elaborato il: ", getBoldStyle(wb));
		Date oggi = new Date();

		setCell(row, (short) 1, DateUtils.getDateToString(oggi, "dd-MM-yyyy"));

		String lCriterio1 = "";
		String lCriterio2 = "";

		if (filtro.getAnnoIniziale() != null) {
			if (filtro.getAnnoFinale() == null) {
				lCriterio1 = "Anno: " + filtro.getAnnoIniziale();
			}

			if (filtro.getAnnoFinale() != null) {
				lCriterio1 = "Dall'Anno: " + filtro.getAnnoIniziale() + " all'anno " + filtro.getAnnoFinale();
			}
		}

		if (filtro.getDataEmissioneIniziale() != null) {
			if (filtro.getDataEmissioneFinale() == null) {
				lCriterio2 = "Data Compilazione: "
						+ DateUtils.getDateToString(filtro.getDataEmissioneIniziale(), "dd-MM-yyyy");
			}

			if (filtro.getDataEmissioneFinale() != null) {
				lCriterio2 = "Data Compilazione dal"
						+ DateUtils.getDateToString(filtro.getDataEmissioneIniziale(), "dd-MM-yyyy") + " al "
						+ DateUtils.getDateToString(filtro.getDataEmissioneFinale(), "dd-MM-yyyy");
			}
		}

		row = sheet.createRow(++nRow);
		setCell(row, (short) 0, "Criteri di Ricerca selezionati:", getBoldStyle(wb));
		setCell(row, (short) 1, lCriterio1);
		setCell(row, (short) 2, lCriterio2);

		row = sheet.createRow(++nRow);
		row = sheet.createRow(++nRow);
		setCell(row, (short) 0, "Numero SIGE", getBoldStyle(wb));
		setCell(row, (short) 1, "Data Provvedimento", getBoldStyle(wb));
		setCell(row, (short) 2, "Provvedimento", getBoldStyle(wb));
		setCell(row, (short) 3, "Data Foglio Complementare", getBoldStyle(wb));
		setCell(row, (short) 4, "Esito", getBoldStyle(wb));

		return nRow;
	}

	private int elaboraSheetStatisticheFC(Vector<StatisticheFogliComplementariModel> dati, HSSFSheet sheet,
			int nRow) {
		Iterator<StatisticheFogliComplementariModel> it = dati.iterator();
		while (it.hasNext()) {
			StatisticheFogliComplementariModel model = it.next();
			HSSFRow row = sheet.createRow(++nRow);
			setCell(row, (short) 0, model.getDescrFascicolo());
			setCell(row, (short) 1, DateUtils.getDateToString(model.getDataProvvedimento(), "dd-MM-yyyy"));
			setCell(row, (short) 2, model.getDescrProvvedimento());
			setCell(row, (short) 3, model.getDataFoglioComplementare());
			setCell(row, (short) 4, model.getDescrEsito());
		}
		return nRow;
	}

	private HSSFCellStyle getBoldStyle(HSSFWorkbook wb) {
		HSSFCellStyle boldStyle = wb.createCellStyle();
		HSSFFont fontBold = wb.createFont();

		fontBold.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		boldStyle.setFont(fontBold);
		return boldStyle;
	}

	public HSSFWorkbook creaFoglioFoglioComplementare(UfficioModel aUfficioUtenteConnesso,
			RicercaFogliCompModel aRicerca) throws F3BException {

		HSSFWorkbook lWb = null;
		HSSFSheet lSheet = null;
		HSSFCellStyle lCellStyleNull = null;
		HSSFCellStyle lCellStyleCenter = null;
		HSSFRow lRow = null;
		Iterator lItx = null;
		int lRowCounter = 0;
		siap.sige.statistiche.model.EveFasGepSogProvModel lModel = null;
		String lPatternData = "dd/MM/yyyy";
		String lBuffer = null;
		IStatisticheSige lCtrlStatSige = null;

		Vector lElenco = null;
		int lContatore = 0;

		lWb = new HSSFWorkbook();

		// creazione primo foglio
		lSheet = lWb.createSheet("Elenco Fogli Complementari");
		lCellStyleNull = lWb.createCellStyle();

		// Intestazione del foglio excel
		lRowCounter = HSSFUtils.getInstance().setIntestazione(lSheet, aUfficioUtenteConnesso, lCellStyleNull);
		lRowCounter += 2;

		// Inserimento dei parametri di ricerca.
		lRow = lSheet.createRow(lRowCounter);
		HSSFUtils.getInstance().setCell(lRow, 0, "Criteri di ricerca selezionati : ", lCellStyleNull);
		lRowCounter++;

		// Stampa filtri di ricerca
		if (aRicerca != null) {

			if (aRicerca.getDataEmissioneIniziale() != null && aRicerca.getDataEmissioneFinale() != null) {
				lBuffer = "Data di emissione tra " + StringUtils.toStringJSP(
						DateUtils.getDateToString(aRicerca.getDataEmissioneIniziale(), "dd-MM-yyyy"), "-")
						+ " e "
						+ StringUtils.toStringJSP(
								DateUtils.getDateToString(aRicerca.getDataEmissioneFinale(), "dd-MM-yyyy"),
								"-");

				lRow = lSheet.createRow(lRowCounter);
				HSSFUtils.getInstance().setCell(lRow, 0, lBuffer, lCellStyleNull);
				lRowCounter++;
			}

			if (aRicerca.getAnnoIniziale() != null && aRicerca.getNumIniziale() != null
					&& aRicerca.getAnnoFinale() != null && aRicerca.getNumFinale() != null) {

				lBuffer = "Dal N. " + StringUtils.toStringJSP(aRicerca.getAnnoIniziale(), "-") + "/"
						+ StringUtils.toStringJSP(aRicerca.getNumIniziale(), "-") + " al "
						+ StringUtils.toStringJSP(aRicerca.getAnnoFinale(), "-") + "/"
						+ StringUtils.toStringJSP(aRicerca.getNumFinale(), "-");

				lRow = lSheet.createRow(lRowCounter);
				HSSFUtils.getInstance().setCell(lRow, 0, lBuffer, lCellStyleNull);
				lRowCounter++;
			}

			if (aRicerca.getStatoValidazione() == null) {
				lBuffer = "Tutti";
			} else if (aRicerca.getStatoValidazione().equalsIgnoreCase(ICostantiStatistiche.ANNULLATI)) {
				lBuffer = "Annullato";
			} else if (aRicerca.getStatoValidazione().equalsIgnoreCase(ICostantiStatistiche.NON_ANNULLATI)) {
				lBuffer = "Non Annullato";
			} else {
				lBuffer = "Tutti";
			}

			lBuffer = "Stato: " + lBuffer;

			lRow = lSheet.createRow(lRowCounter);
			HSSFUtils.getInstance().setCell(lRow, 0, lBuffer, lCellStyleNull);
			lRowCounter++;
		}

		lRowCounter += 2;

		// stile per celle col bordo con testo centrato
		lCellStyleCenter = getBordo4Lati(lWb);
		lCellStyleCenter.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		lCellStyleCenter.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		lCellStyleCenter.setWrapText(true);

		lRow = lSheet.createRow(lRowCounter++);

		lSheet.setColumnWidth(0, 10 * 256); // Prog
		lSheet.setColumnWidth(1, 15 * 256); // Anno Progressivo Foglio Complementare
		lSheet.setColumnWidth(2, 15 * 256); // Data Compilazione
		lSheet.setColumnWidth(3, 15 * 256); // Procedimento SIGE
		lSheet.setColumnWidth(4, 40 * 256); // Generalita' Soggetto
		lSheet.setColumnWidth(5, 15 * 256); // Tipo Atto
		lSheet.setColumnWidth(6, 20 * 256); // Contenuto Atto
		lSheet.setColumnWidth(7, 15 * 256); // Esito Provvedimento
		lSheet.setColumnWidth(8, 15 * 256); // Data Emissione
		lSheet.setColumnWidth(9, 15 * 256); // Stato

		// Intestazione colonne
		HSSFUtils.getInstance().setCell(lRow, 0, "Prog.", lCellStyleCenter);
		HSSFUtils.getInstance().setCell(lRow, 1, "Anno/Prog. F.C.", lCellStyleCenter);
		HSSFUtils.getInstance().setCell(lRow, 2, "Data Compilazione", lCellStyleCenter);
		HSSFUtils.getInstance().setCell(lRow, 3, "Procedimento SIGE", lCellStyleCenter);
		HSSFUtils.getInstance().setCell(lRow, 4, "Generalita' Soggetto", lCellStyleCenter);
		HSSFUtils.getInstance().setCell(lRow, 5, "Tipo Atto", lCellStyleCenter);
		HSSFUtils.getInstance().setCell(lRow, 6, "Contenuto Atto", lCellStyleCenter);
		HSSFUtils.getInstance().setCell(lRow, 7, "Esito Provvedimento", lCellStyleCenter);
		HSSFUtils.getInstance().setCell(lRow, 8, "Data Emissione", lCellStyleCenter);
		HSSFUtils.getInstance().setCell(lRow, 9, "Stato", lCellStyleCenter);

		lCtrlStatSige = SIGELookupRemote.getStatisticheSigeRemote();
		lElenco = lCtrlStatSige.ExRicercaFogliComplementariPaginata(aRicerca, -1);

		lContatore = 1;
		lItx = lElenco.iterator();
		while (lItx.hasNext()) {
			lModel = (siap.sige.statistiche.model.EveFasGepSogProvModel) lItx.next();
			lRow = lSheet.createRow(lRowCounter++);

			HSSFUtils.getInstance().setCell(lRow, 0, "" + lContatore++, lCellStyleCenter);
			HSSFUtils.getInstance().setCell(lRow, 1,
					(lModel.getDocumentoAllegato() != null
							&& lModel.getDocumentoAllegato().getAnnoFoglioComplementare() != null
							&& lModel.getDocumentoAllegato().getProgrFoglioComplementare() != null)
									? lModel.getDocumentoAllegato().getAnnoFoglioComplementare() + "/"
											+ lModel.getDocumentoAllegato().getProgrFoglioComplementare()
									: "-",
					lCellStyleCenter);
			HSSFUtils.getInstance()
					.setCell(lRow, 2,
							lModel.getProvvedimentoSigeEvento().getProvvedimento() != null
									? StringUtils.cStrForJS(
											DateUtils.getDateToString(lModel.getProvvedimentoSigeEvento()
													.getProvvedimento().getDataEmissione(), lPatternData))
									: "-",
							lCellStyleCenter);
			HSSFUtils.getInstance().setCell(lRow, 3,
					(lModel.getFascicoloSige() != null && lModel.getFascicoloSige().getChiaveAnno() != null
							&& lModel.getFascicoloSige().getChiaveProgr() != null)
									? lModel.getFascicoloSige().getChiaveAnno() + "/"
											+ lModel.getFascicoloSige().getChiaveProgr()
									: "-",
					lCellStyleCenter);

			if (lModel.getSoggetto() != null) {
				lBuffer = StringUtils.cStrForJS(lModel.getSoggetto().getCognome()) + " "
						+ StringUtils.cStrForJS(lModel.getSoggetto().getNome()) + "\n";
				lBuffer += StringUtils.cStrForJS(
						DateUtils.getDateToString(lModel.getSoggetto().getDataNascita(), lPatternData));
				lBuffer += " ";
				if (lModel.getSoggetto().getCodStatoNascita() != null) {
					if (lModel.getSoggetto().getCodStatoNascita().compareTo("039") == 0) {
						lBuffer += StringUtils.cStrForJS(lModel.getSoggetto().getDescrComuneNascita()) + " ("
								+ StringUtils.cStrForJS(lModel.getSoggetto().getCodProvinciaNascita()) + ")";
					} else {
						lBuffer += StringUtils.cStrForJS(lModel.getSoggetto().getDescComuneNascitaEstero());
						lBuffer += " (" + StringUtils.cStrForJS(lModel.getSoggetto().getDescrStatoNascita())
								+ ")";
					}
				}
			} else {
				lBuffer = "";
			}
			HSSFUtils.getInstance().setCell(lRow, 4, lBuffer, lCellStyleCenter);

			HSSFUtils.getInstance().setCell(lRow, 5,
					(lModel.getEvento() != null && lModel.getEvento().getDescrTipoProvvedimento() != null)
							? StringUtils.cStrForJS(lModel.getEvento().getDescrTipoProvvedimento())
							: "-",
					lCellStyleCenter);
			HSSFUtils.getInstance().setCell(lRow, 6,
					(lModel.getEvento() != null && lModel.getEvento().getDescrMotivo() != null)
							? StringUtils.cStrForJS(lModel.getEvento().getDescrMotivo())
							: "-",
					lCellStyleCenter);
			HSSFUtils.getInstance().setCell(lRow, 7,
					(lModel.getEvento() != null && lModel.getEvento().getDescrEsito() != null)
							? StringUtils.cStrForJS(lModel.getEvento().getDescrEsito())
							: "-",
					lCellStyleCenter);
			HSSFUtils.getInstance().setCell(lRow, 8,
					(lModel.getDocumentoAllegato() != null
							&& lModel.getDocumentoAllegato().getDataEmissione() != null)
									? DateUtils.getDateToString(
											lModel.getDocumentoAllegato().getDataEmissione(), lPatternData)
									: "-",
					lCellStyleCenter);
			if (lModel.getDocumentoAllegato() != null
					&& lModel.getDocumentoAllegato().getFlagDocumentoRegistrato() != null) {
				if (lModel.getDocumentoAllegato().getFlagDocumentoRegistrato()
						.compareTo(ICostantiStatistiche.ANNULLATI) == 0) {
					HSSFUtils.getInstance().setCell(lRow, 9, "ANNULLATO", lCellStyleCenter);
				} else {
					HSSFUtils.getInstance().setCell(lRow, 9, "", lCellStyleCenter);
				}
			} else {
				HSSFUtils.getInstance().setCell(lRow, 9, "-", lCellStyleCenter);
			}
		}

		return lWb;
	}

	private HSSFCellStyle getBordo4Lati(HSSFWorkbook wb) {

		HSSFCellStyle cs = wb.createCellStyle();
		cs.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		cs.setBorderTop(HSSFCellStyle.BORDER_THIN);
		cs.setBorderRight(HSSFCellStyle.BORDER_THIN);
		cs.setBorderLeft(HSSFCellStyle.BORDER_THIN);

		return cs;
	}

	/**
	 * La funzione prepara la pagina xls con l'elenco dei provvedimenti Sige cercati L'elenco dei
	 * provvedimenti nella lista e' quello passato attraverso il parametro aElencoProc.
	 *
	 * @param wb
	 * @param aUuffUteConnesso
	 * @param aTitolo
	 * @param aElencoProc
	 * @param aDescOggetto
	 */
	public void creaFoglioElencoProvvedimentiSige(HSSFWorkbook wb, UfficioModel aUuffUteConnesso,
			String aCriterio1, String aCriterio2, String aCriterio3, Vector aElencoProvv)
			throws F3BException {

		short numCol = 0;
		siap.sige.statistiche.model.EveFasGepSogProvModel lProvvedimento = null;

		HSSFSheet sheet = wb.createSheet("ElencoProvvedimenti");
		HSSFCellStyle csNull = wb.createCellStyle();

		int nRow = 0;

		// Intestazione del foglio excel
		nRow = setIntestazione(sheet, aUuffUteConnesso, csNull);

		nRow++;
		nRow++;
		// HSSFRow row = sheet.createRow(nRow);
		// setCell(row, (short) 0, aTitolo , csNull);
		// nRow++;
		HSSFRow row = sheet.createRow(nRow);
		setCell(row, (short) 0, aCriterio1, csNull);
		nRow++;
		row = sheet.createRow(nRow);
		setCell(row, (short) 0, aCriterio2, csNull);
		nRow++;
		row = sheet.createRow(nRow);
		setCell(row, (short) 0, aCriterio3, csNull);
		nRow++;
		nRow++;
		row = sheet.createRow(nRow);
		setCell(row, (short) 0, "Elenco Provvedimenti Depositati ", csNull);
		nRow += 2;

		// settaggio della larghezza
		// delle colonne
		numCol = 0;
		sheet.setColumnWidth(numCol++, (short) (10 * 256));
		sheet.setColumnWidth(numCol++, (short) (20 * 256));
		sheet.setColumnWidth(numCol++, (short) (10 * 256));
		sheet.setColumnWidth(numCol++, (short) (40 * 256));
		sheet.setColumnWidth(numCol++, (short) (10 * 256));
		sheet.setColumnWidth(numCol++, (short) (20 * 256));
		sheet.setColumnWidth(numCol++, (short) (20 * 256));
		sheet.setColumnWidth(numCol++, (short) (10 * 256));
		sheet.setColumnWidth(numCol++, (short) (10 * 256));

		// stile per celle col bordo con testo centrato
		HSSFCellStyle csCenter = getBordo4Lati(wb);
		csCenter.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		csCenter.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		csCenter.setWrapText(true);

		row = sheet.createRow(nRow++);

		// Intestazione colonne
		numCol = 0;
		setCell(row, numCol++, "Progr.", csCenter);
		setCell(row, numCol++, "Anno/Progr. Provvedimento", csCenter);
		setCell(row, numCol++, "Procedimento SIGE", csCenter);
		setCell(row, numCol++, "Generalita' Soggetto", csCenter);
		setCell(row, numCol++, "Tipo Atto", csCenter);
		setCell(row, numCol++, "Contenuto Atto", csCenter);
		setCell(row, numCol++, "Esito Provvedimento", csCenter);
		setCell(row, numCol++, "Data Emissione", csCenter);
		setCell(row, numCol++, "Data Deposito", csCenter);

		Iterator itx = aElencoProvv.iterator();
		int progr = 0;

		// inizio ciclo di scrittura dei dati
		while (itx.hasNext()) {
			numCol = 0;

			// Inizializzazione Date
			lProvvedimento = (siap.sige.statistiche.model.EveFasGepSogProvModel) itx.next();
			String annoProgr = new String();
			String dataDeposito = new String();
			String generalitaSoggetto = new String();
			// String dataContAtto = new String();
			String lTipoProvvedimento = new String();
			String luogoNascita = new String();

			// Scrittura riga del report
			row = sheet.createRow(nRow++);

			if (lProvvedimento.getProvvedimentoSige() != null) {
				lTipoProvvedimento = "Ordinanza";
				annoProgr = lProvvedimento.getProvvedimentoSige().getChiaveAnno() + "/"
						+ lProvvedimento.getProvvedimentoSige().getChiaveProgr();
				if (lProvvedimento.getEvento().getFlagDocumentoRegistrato().compareTo("A") == 0)
					annoProgr += "\nANNULLATA";
				dataDeposito = DateUtils.getDateToString(
						lProvvedimento.getProvvedimentoSige().getDataDeposito(), "dd/MM/yyyy");
			}
			/*
			 * if (lProvvedimento.getDepositoDecreto() != null) { lTipoProvvedimento = "Decreto"; AnnoProgr =
			 * lProvvedimento.getDepositoDecreto().getAnnoS72() + "/" +
			 * lProvvedimento.getDepositoDecreto().getNumS72(); if
			 * (lProvvedimento.getEvento().getFlagDocumentoRegistrato().compareTo("A")==0) AnnoProgr+=
			 * "\nANNULLATO"; dataDeposito =
			 * DateUtils.getDateToString(lProvvedimento.getDepositoDecreto().getDataDeposito() ,
			 * "dd/MM/yyyy"); }
			 */
			BigDecimal lIdSoggetto = lProvvedimento.getSoggetto().getIdSoggetto();
			SoggettoModel lSoggetto = new SoggettoModel();

			try {
				ISoggetto lCtrlSoggetto = SICOLookupRemote.getSoggettoRemote();
				lSoggetto = lCtrlSoggetto.ExRicercaSoggettoByKey(lIdSoggetto);
			} catch (Exception e) {
				throw new F3BException("creaFoglioElencoProvvedimenti: ricerca Soggetto " + e);
			}

			if (lSoggetto.getCodStatoNascita().compareTo("039") == 0)
				luogoNascita = lSoggetto.getDescrComuneNascita() + "(" + lSoggetto.getCodProvinciaNascita()
						+ ")";
			else
				luogoNascita = lSoggetto.getDescComuneNascitaEstero() + "(" + lSoggetto.getDescrStatoNascita()
						+ ")";

			generalitaSoggetto = lSoggetto.getNome() + " " + lSoggetto.getCognome() + "\n"
					+ DateUtils.getDateToString(lSoggetto.getDataNascita(), "dd/MM/yyyy") + " "
					+ luogoNascita;

			// dataContAtto = DateUtils.getDateToString(lProvvedimento.getEvento().getDataEmissione(),
			// "dd/MM/yyyy")
			// + " "
			// + lTipoProvvedimento
			// + "\n"
			// + lProvvedimento.getEvento().getDescrMotivo()
			// + "\n"
			// + lProvvedimento.getEvento().getDescrEsito();
			setCell(row, numCol++, new Integer(++progr).toString(), csCenter);
			setCell(row, numCol++, annoProgr, csCenter);
			setCell(row, numCol++, lProvvedimento.getFascicoloSige().getChiaveAnno() + "/"
					+ lProvvedimento.getFascicoloSige().getChiaveProgr(), csCenter);
			setCell(row, numCol++, generalitaSoggetto, csCenter);
			setCell(row, numCol++, lTipoProvvedimento, csCenter);
			setCell(row, numCol++, lProvvedimento.getEvento().getDescrMotivo(), csCenter);
			setCell(row, numCol++, lProvvedimento.getEvento().getDescrEsito(), csCenter);
			setCell(row, numCol++,
					DateUtils.getDateToString(lProvvedimento.getEvento().getDataEmissione(), "dd/MM/yyyy"),
					csCenter);
			setCell(row, numCol++, dataDeposito, csCenter);

		}
	}

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

	/**
	 * La funzione prepara la pagina xls con l'elenco dei Fogli Complementari ricercati L'elenco dei FC nella
	 * lista e' quello passato attraverso il parametro aElencoProvv.
	 *
	 * @param wb
	 * @param aUuffUteConnesso
	 * @param aTitolo
	 * @param aElencoProc
	 * @param aDescOggetto
	 */
	public void creaFoglioElencoFogliComplementari(HSSFWorkbook wb, UfficioModel aUuffUteConnesso,
			String aCriterio1, String aCriterio2, String aCriterio3, Vector aElencoProvv)
			throws F3BException {

		short numCol = 0;
		EveFasGepSogProvModel lProvvedimento = null;

		HSSFSheet sheet = wb.createSheet("ElencoFogliComplementari");
		HSSFCellStyle csNull = wb.createCellStyle();

		int nRow = 0;

		// Intestazione del foglio excel
		nRow = setIntestazione(sheet, aUuffUteConnesso, csNull);

		nRow++;
		nRow++;
		// HSSFRow row = sheet.createRow(nRow);
		// setCell(row, (short) 0, aTitolo , csNull);
		// nRow++;
		HSSFRow row = sheet.createRow(nRow);
		setCell(row, (short) 0, aCriterio1, csNull);
		nRow++;
		row = sheet.createRow(nRow);
		setCell(row, (short) 0, aCriterio2, csNull);
		nRow++;
		row = sheet.createRow(nRow);
		setCell(row, (short) 0, aCriterio3, csNull);
		nRow++;
		nRow++;
		row = sheet.createRow(nRow);
		setCell(row, (short) 0, "Elenco Fogli Complementari ", csNull);
		nRow += 2;

		// settaggio della larghezza
		// delle colonne
		numCol = 0;
		sheet.setColumnWidth(numCol++, (short) (10 * 256));
		sheet.setColumnWidth(numCol++, (short) (20 * 256));
		sheet.setColumnWidth(numCol++, (short) (10 * 256));
		sheet.setColumnWidth(numCol++, (short) (40 * 256));
		sheet.setColumnWidth(numCol++, (short) (20 * 256));
		sheet.setColumnWidth(numCol++, (short) (20 * 256));
		sheet.setColumnWidth(numCol++, (short) (20 * 256));
		sheet.setColumnWidth(numCol++, (short) (20 * 256));
		sheet.setColumnWidth(numCol++, (short) (10 * 256));

		// stile per celle col bordo con testo centrato
		HSSFCellStyle csCenter = getBordo4Lati(wb);
		csCenter.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		csCenter.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		csCenter.setWrapText(true);

		row = sheet.createRow(nRow++);

		// Intestazione colonne
		numCol = 0;
		setCell(row, numCol++, "Progr.", csCenter);
		setCell(row, numCol++, "Numero Foglio Complementare", csCenter);
		setCell(row, numCol++, "Numero SIUS", csCenter);
		setCell(row, numCol++, "Cognome Nome", csCenter);
		setCell(row, numCol++, "Data Emissione", csCenter);
		setCell(row, numCol++, "Data Emissione", csCenter);
		setCell(row, numCol++, "Oggetto", csCenter);
		setCell(row, numCol++, "Esito", csCenter);
		setCell(row, numCol++, "Stato", csCenter);

		Iterator itx = aElencoProvv.iterator();
		int progr = 0;

		// inizio ciclo di scrittura dei dati
		while (itx.hasNext()) {
			numCol = 0;

			// Inizializzazione Date
			lProvvedimento = (EveFasGepSogProvModel) itx.next();

			String numFC = new String();
			// String dataDeposito = new String();
			String generalitaSoggetto = new String();
			String stato = new String();

			String flagRegistrato = lProvvedimento.getEvento().getFlagDocumentoRegistrato();
			if (flagRegistrato != null && flagRegistrato.equals("A")) {
				stato = "ANNULLATO";
			} else {
				stato = "";
			}

			// Scrittura riga del report
			row = sheet.createRow(nRow++);

			numFC = lProvvedimento.getDocumentoAllegato().getAnnoFoglioComplementare() + "/"
					+ lProvvedimento.getDocumentoAllegato().getProgrFoglioComplementare();

			// // Ordinanza
			// if (lProvvedimento.getDepositoOrdinanzaPc() != null) {
			// dataDeposito = DateUtils.getDateToString(lProvvedimento.getDepositoOrdinanzaPc()
			// .getDataDeposito(), "dd/MM/yyyy");
			// }
			//
			// // Decreto
			// if (lProvvedimento.getDepositoDecreto() != null) {
			// dataDeposito = DateUtils.getDateToString(lProvvedimento.getDepositoDecreto()
			// .getDataDeposito(), "dd/MM/yyyy");
			// }

			BigDecimal lIdSoggetto = lProvvedimento.getSoggetto().getIdSoggetto();
			SoggettoModel lSoggetto = new SoggettoModel();

			try {
				ISoggetto lCtrlSoggetto = SICOLookupRemote.getSoggettoRemote();
				lSoggetto = lCtrlSoggetto.ExRicercaSoggettoByKey(lIdSoggetto);
			} catch (Exception e) {
				throw new F3BException("creaFoglioElencoFogliComplementari: ricerca Soggetto " + e);
			}

			generalitaSoggetto = lSoggetto.getCognome() + " " + lSoggetto.getNome();

			setCell(row, numCol++, new Integer(++progr).toString(), csCenter);
			setCell(row, numCol++, numFC, csCenter);
			setCell(row, numCol++, lProvvedimento.getFascicoloSige().getChiaveAnno() + "/"
					+ lProvvedimento.getFascicoloSige().getChiaveProgr(), csCenter);
			setCell(row, numCol++, generalitaSoggetto, csCenter);
			setCell(row, numCol++,
					DateUtils.getDateToString(lProvvedimento.getEvento().getDataEmissione(), "dd/MM/yyyy"),
					csCenter);
			setCell(row, numCol++, DateUtils.getDateToString(
					lProvvedimento.getDocumentoAllegato().getDataEmissione(), "dd/MM/yyyy"), csCenter);
			setCell(row, numCol++, lProvvedimento.getEvento().getDescrMotivo(), csCenter);
			setCell(row, numCol++, lProvvedimento.getEvento().getDescrEsito(), csCenter);
			setCell(row, numCol++, stato, csCenter);

		}
	}

	/**
	 * MEV_65: aggiunto metodo per gestire nuova funzionalita'
	 *
	 * @param rfsm
	 * @param v
	 * @param um
	 * @return HSSFWorkbook
	 * @throws F3BException
	 */
	public HSSFWorkbook getReportSoggettiSigePerPosizioneGiuridica(RicercaFascicoloSigeModel rfsm,
			Vector<FascicoloSigeEstesoModel> v, UfficioModel um) throws F3BException {

		HSSFWorkbook hssfwb = new HSSFWorkbook();
		HSSFSheet foglio = hssfwb.createSheet("Elenco Sogg. per Pos. Giuridica");

		int numeroRiga = 0;
		HSSFRow riga = foglio.createRow(numeroRiga);

		String value = um.getDescrTipoUfficio().toUpperCase() + " DI " + um.getDescrComune().toUpperCase();
		setCell(riga, (short) 0, "Ufficio", getBoldStyle(hssfwb));
		setCell(riga, (short) 1, value);

		riga = foglio.createRow(numeroRiga++);
		setCell(riga, (short) 0, "Elaborato il: ", getBoldStyle(hssfwb));
		Date oggi = new Date();
		setCell(riga, (short) 1, DateUtils.getDateToString(oggi, "dd/MM/yyyy"));

		String riga1 = "", riga2 = "", riga3 = "", riga4 = "", riga5 = "", riga6 = "", riga7 = "";
		if (!Utils.isNullObj(rfsm.getDataIscrizioneIniziale()))
			riga1 += "Fascicoli Sige iscritti dal: "
					+ DateUtils.getDateToString(rfsm.getDataIscrizioneIniziale(), "dd/MM/yyyy") + " al "
					+ DateUtils.getDateToString(rfsm.getDataIscrizioneFinale(), "dd/MM/yyyy");
		if (!Utils.isNullObj(rfsm.getChiaveAnnoIniziale()))
			riga2 += "Fascicoli Sige dal " + rfsm.getChiaveAnnoIniziale() + "/"
					+ rfsm.getChiaveProgrIniziale() + " al " + rfsm.getChiaveAnnoFinale() + "/"
					+ rfsm.getChiaveProgrFinale();
		riga3 += "Posizione Giuridica: " + rfsm.getDescPosizioneGiuridica();
		riga4 += "Stato Procedimento: "
				+ (Utils.isPresent(rfsm.getDataFinePendenza())
						? "Solo Pendenti fino al "
								+ DateUtils.getDateToString(rfsm.getDataFinePendenza(), "dd/MM/yyyy")
						: "Tutti");
		if (Utils.isPresent(rfsm.getDescMagistrato()))
			riga5 += "Magistrato: " + rfsm.getDescMagistrato();
		if (Utils.isPresent(rfsm.getDescSezione()))
			riga6 += "Sezione: " + rfsm.getDescSezione();
		if (Utils.isPresent(rfsm.getDescNazione()))
			riga7 += "Nazionalita': " + rfsm.getDescNazione();

		riga = foglio.createRow(numeroRiga++);
		setCell(riga, (short) 0, "Criteri di Ricerca selezionati:", getBoldStyle(hssfwb));
		if (Utils.isPresent(riga1)) {
			riga = foglio.createRow(numeroRiga++);
			setCell(riga, (short) 0, riga1);
		}
		if (Utils.isPresent(riga2)) {
			riga = foglio.createRow(numeroRiga++);
			setCell(riga, (short) 0, riga2);
		}
		if (Utils.isPresent(riga3)) {
			riga = foglio.createRow(numeroRiga++);
			setCell(riga, (short) 0, riga3);
		}
		if (Utils.isPresent(riga4)) {
			riga = foglio.createRow(numeroRiga++);
			setCell(riga, (short) 0, riga4);
		}
		if (Utils.isPresent(riga5)) {
			riga = foglio.createRow(numeroRiga++);
			setCell(riga, (short) 0, riga5);
		}
		if (Utils.isPresent(riga6)) {
			riga = foglio.createRow(numeroRiga++);
			setCell(riga, (short) 0, riga6);
		}
		if (Utils.isPresent(riga7)) {
			riga = foglio.createRow(numeroRiga++);
			setCell(riga, (short) 0, riga7);
		}

		riga = foglio.createRow(numeroRiga++);
		riga = foglio.createRow(numeroRiga++);

		// stile per celle col bordo con testo centrato ed in grassetto
		HSSFCellStyle hssfcs = hssfwb.createCellStyle();
		hssfcs.setBorderBottom(HSSFCellStyle.BORDER_THICK);
		hssfcs.setBorderTop(HSSFCellStyle.BORDER_THICK);
		hssfcs.setBorderRight(HSSFCellStyle.BORDER_THICK);
		hssfcs.setBorderLeft(HSSFCellStyle.BORDER_THICK);
		hssfcs.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		hssfcs.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		hssfcs.setWrapText(true);
		HSSFFont hssff = hssfwb.createFont();
		hssff.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		hssfcs.setFont(hssff);

		short numCol = 0;
		foglio.setColumnWidth(numCol++, 10 * 256); // Progr.
		foglio.setColumnWidth(numCol++, 10 * 256); // N° SIGE
		foglio.setColumnWidth(numCol++, 15 * 256); // Data Iscrizione
		foglio.setColumnWidth(numCol++, 15 * 256); // Data Arrivo in Cancelleria
		foglio.setColumnWidth(numCol++, 20 * 256); // Cognome
		foglio.setColumnWidth(numCol++, 20 * 256); // Nome
		foglio.setColumnWidth(numCol++, 25 * 256); // Luogo di Nascita
		foglio.setColumnWidth(numCol++, 15 * 256); // Data di Nascita
		foglio.setColumnWidth(numCol++, 15 * 256); // Nazionalita'
		foglio.setColumnWidth(numCol++, 30 * 256); // Posizione Giuridica
		foglio.setColumnWidth(numCol++, 15 * 256); // Data Fine Pena
		foglio.setColumnWidth(numCol++, 15 * 256); // Data Udienza
		foglio.setColumnWidth(numCol++, 15 * 256); // Magistrato
		if (Utils.isPresent(riga5))
			foglio.setColumnWidth(numCol++, 40 * 256); // Magistrato
		foglio.setColumnWidth(numCol++, 15 * 256); // Sezione
		if (Utils.isPresent(riga6))
			foglio.setColumnWidth(numCol++, 30 * 256); // Sezione

		numCol = 0;
		// Intestazione colonne
		HSSFUtils.getInstance().setCell(riga, numCol++, "Progr.", hssfcs);
		HSSFUtils.getInstance().setCell(riga, numCol++, "N° SIGE", hssfcs);
		HSSFUtils.getInstance().setCell(riga, numCol++, "Data Iscrizione", hssfcs);
		HSSFUtils.getInstance().setCell(riga, numCol++, "Data Arrivo in Cancelleria", hssfcs);
		HSSFUtils.getInstance().setCell(riga, numCol++, "Cognome", hssfcs);
		HSSFUtils.getInstance().setCell(riga, numCol++, "Nome", hssfcs);
		HSSFUtils.getInstance().setCell(riga, numCol++, "Luogo di Nascita", hssfcs);
		HSSFUtils.getInstance().setCell(riga, numCol++, "Data di Nascita", hssfcs);
		HSSFUtils.getInstance().setCell(riga, numCol++, "Nazionalita'", hssfcs);
		HSSFUtils.getInstance().setCell(riga, numCol++, "Posizione Giuridica", hssfcs);
		HSSFUtils.getInstance().setCell(riga, numCol++, "Data Fine Pena", hssfcs);
		HSSFUtils.getInstance().setCell(riga, numCol++, "Data Udienza", hssfcs);
		HSSFUtils.getInstance().setCell(riga, numCol++, "Magistrato", hssfcs);
		HSSFUtils.getInstance().setCell(riga, numCol++, "Sezione", hssfcs);

		// stile per celle col bordo con testo centrato
		HSSFCellStyle hssfcsCorpo = null;
		hssfcsCorpo = getBordo4Lati(hssfwb);
		hssfcsCorpo.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		hssfcsCorpo.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		hssfcsCorpo.setWrapText(true);

		Iterator<FascicoloSigeEstesoModel> i = v.iterator();
		int progr = 1;
		while (i.hasNext()) {
			numCol = 0;
			FascicoloSigeEstesoModel fsem = i.next();
			riga = foglio.createRow(numeroRiga++);
			HSSFUtils.getInstance().setCell(riga, numCol++, "" + progr, hssfcsCorpo);
			HSSFUtils.getInstance().setCell(riga, numCol++, fsem.getFascicoloSige().getChiaveAnno().toString()
					+ "/" + fsem.getFascicoloSige().getChiaveProgr().toString(), hssfcsCorpo);
			HSSFUtils.getInstance().setCell(riga, numCol++,
					DateUtils.getDateToString(fsem.getFascicoloSige().getDataIscrizione(), "dd/MM/yyyy"),
					hssfcsCorpo);
			HSSFUtils.getInstance().setCell(riga, numCol++, DateUtils.getDateToString(
					fsem.getRichiestaSige().getDataArrivoCancelleria(), "dd/MM/yyyy"), hssfcsCorpo);
			HSSFUtils.getInstance().setCell(riga, numCol++, fsem.getSoggetto().getCognome(), hssfcsCorpo);
			HSSFUtils.getInstance().setCell(riga, numCol++, fsem.getSoggetto().getNome(), hssfcsCorpo);
			HSSFUtils.getInstance().setCell(riga, numCol++,
					(Utils.isPresent(fsem.getSoggetto().getDescrComuneNascita())
							&& !"-".equals(fsem.getSoggetto().getDescrComuneNascita()))
									? fsem.getSoggetto().getDescrComuneNascita()
									: Utils.isPresent(fsem.getSoggetto().getDescComuneNascitaEstero())
											? fsem.getSoggetto().getDescComuneNascitaEstero()
											: "-",
					hssfcsCorpo);

			HSSFUtils.getInstance().setCell(riga, numCol++,
					(fsem.getSoggetto().getDataNascita() != null)
							? DateUtils.getDateToString(fsem.getSoggetto().getDataNascita(), "dd/MM/yyyy")
							: "-",
					hssfcsCorpo);
			HSSFUtils.getInstance().setCell(riga, numCol++,
					Utils.isPresent(fsem.getSoggetto().getDescrStatoNascita())
							? fsem.getSoggetto().getDescrStatoNascita()
							: "-",
					hssfcsCorpo);
			HSSFUtils.getInstance().setCell(riga, numCol++,
					fsem.getFascicoloSige().getDescrPosizioneGiuridica(), hssfcsCorpo);
			HSSFUtils.getInstance().setCell(riga, numCol++,
					StringUtils.toStringJSP(DateUtils
							.getDateToString(fsem.getFascicoloSige().getDataFinePena(), "dd/MM/yyyy"), "-"),
					hssfcsCorpo);
			// MEV_65: aggiunta proprieta'
			// HSSFUtils.getInstance().setCell(riga, numCol++,
			// StringUtils.toStringJSP(DateUtils.getDateToString(
			// fsem.getUdienzaProcedimento().getDataUdienzaSige(), "dd/MM/yyyy"), "-"),
			// hssfcsCorpo);
			HSSFUtils.getInstance().setCell(riga, numCol++,
					fsem.getUdienzaProcedimento().getListaDateUdienzaSige(), hssfcsCorpo);
			String descMagistrato = "-";
			if (fsem.getMagAssegnatario() != null && fsem.getMagAssegnatario().getMagistrato() != null
					&& Utils.isPresent(fsem.getMagAssegnatario().getMagistrato().getCodMagistrato()))
				descMagistrato = fsem.getMagAssegnatario().getMagistrato().getCognome() + " "
						+ fsem.getMagAssegnatario().getMagistrato().getNome() + " - "
						+ fsem.getMagAssegnatario().getMagistrato().getCodMagistrato();
			HSSFUtils.getInstance().setCell(riga, numCol++,
					Utils.isPresent(descMagistrato) ? descMagistrato : rfsm.getDescMagistrato(), hssfcsCorpo);
			String descSezione = Utils.isPresent(fsem.getFascicoloSige().getDescrSezione())
					? fsem.getFascicoloSige().getDescrSezione()
					: "-";
			HSSFUtils.getInstance().setCell(riga, numCol++, descSezione, hssfcsCorpo);
			progr++;
		}

		// valore di ritorno
		return hssfwb;
	}

	/**
	 * MEV_65: aggiunto metodo per gestire nuova funzionalita'
	 *
	 * @param rfsm
	 * @param v
	 * @param um
	 * @return
	 * @throws F3BException
	 */
	public HSSFWorkbook getReportProcedimentiSigeConRicorsoOpposizione(RicercaFascicoloSigeModel rfsm,
			Vector<FascicoloSigeEstesoModel> v, UfficioModel um) throws F3BException {

		HSSFWorkbook hssfwb = new HSSFWorkbook();
		HSSFSheet foglio = hssfwb.createSheet("Elenco Proc. con Ric-Opp");

		int numeroRiga = 0;
		HSSFRow riga = foglio.createRow(numeroRiga);

		String value = um.getDescrTipoUfficio().toUpperCase() + " DI " + um.getDescrComune().toUpperCase();
		setCell(riga, (short) 0, "Ufficio", getBoldStyle(hssfwb));
		setCell(riga, (short) 1, value);

		riga = foglio.createRow(numeroRiga++);
		setCell(riga, (short) 0, "Elaborato il: ", getBoldStyle(hssfwb));
		Date oggi = new Date();
		setCell(riga, (short) 1, DateUtils.getDateToString(oggi, "dd/MM/yyyy"));

		String riga1 = "", riga2 = "", riga3 = "", riga4 = "", riga5 = "";
		riga1 += "Tipo Ricorso/Opposizione: " + rfsm.getDescTipoRicorso();
		if (!Utils.isNullObj(rfsm.getChiaveAnnoRicorso()))
			riga2 += "Anno/Numero Ricorso/Opposizione: " + rfsm.getChiaveAnnoRicorso() + "/"
					+ rfsm.getChiaveProgrRicorso();
		if (!Utils.isNullObj(rfsm.getChiaveAnnoIniziale()))
			riga3 += "Estremi Ricorso/Opposizione dal " + rfsm.getChiaveAnnoIniziale() + "/"
					+ rfsm.getChiaveProgrIniziale() + " al " + rfsm.getChiaveAnnoFinale() + "/"
					+ rfsm.getChiaveProgrFinale();
		if (!Utils.isNullObj(rfsm.getDataArrivoCancelleriaIniziale()))
			riga4 += "Date Arrivo in Cancelleria dal: "
					+ DateUtils.getDateToString(rfsm.getDataArrivoCancelleriaIniziale(), "dd/MM/yyyy")
					+ " al " + DateUtils.getDateToString(rfsm.getDataArrivoCancelleriaFinale(), "dd/MM/yyyy");
		if (Utils.isPresent(rfsm.getStatoValidazione()))
			riga5 += "Stato Validazione: " + rfsm.getStatoValidazione();

		riga = foglio.createRow(numeroRiga++);
		setCell(riga, (short) 0, "Criteri di Ricerca selezionati:", getBoldStyle(hssfwb));
		if (Utils.isPresent(riga1)) {
			riga = foglio.createRow(numeroRiga++);
			setCell(riga, (short) 0, riga1);
		}
		if (Utils.isPresent(riga2)) {
			riga = foglio.createRow(numeroRiga++);
			setCell(riga, (short) 0, riga2);
		}
		if (Utils.isPresent(riga3)) {
			riga = foglio.createRow(numeroRiga++);
			setCell(riga, (short) 0, riga3);
		}
		if (Utils.isPresent(riga4)) {
			riga = foglio.createRow(numeroRiga++);
			setCell(riga, (short) 0, riga4);
		}
		if (Utils.isPresent(riga5)) {
			riga = foglio.createRow(numeroRiga++);
			setCell(riga, (short) 0, riga5);
		}

		riga = foglio.createRow(numeroRiga++);
		riga = foglio.createRow(numeroRiga++);

		// stile per celle col bordo con testo centrato ed in grassetto
		HSSFCellStyle hssfcs = hssfwb.createCellStyle();
		hssfcs.setBorderBottom(HSSFCellStyle.BORDER_THICK);
		hssfcs.setBorderTop(HSSFCellStyle.BORDER_THICK);
		hssfcs.setBorderRight(HSSFCellStyle.BORDER_THICK);
		hssfcs.setBorderLeft(HSSFCellStyle.BORDER_THICK);
		hssfcs.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		hssfcs.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		hssfcs.setWrapText(true);
		HSSFFont hssff = hssfwb.createFont();
		hssff.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		hssfcs.setFont(hssff);

		short numCol = 0;
		foglio.setColumnWidth(numCol++, 15 * 256); // Progr.
		foglio.setColumnWidth(numCol++, 15 * 256); // N° Ricorso Opposizione
		foglio.setColumnWidth(numCol++, 15 * 256); // Procedimento SIGE
		foglio.setColumnWidth(numCol++, 25 * 256); // Generalita' Soggetto
		foglio.setColumnWidth(numCol++, 15 * 256); // Data Emissione
		foglio.setColumnWidth(numCol++, 15 * 256); // Tipo Provvedimento
		// foglio.setColumnWidth(numCol++, 15 * 256); // Contenuto Atto
		// foglio.setColumnWidth(numCol++, 15 * 256); // Esito Provvedimento
		foglio.setColumnWidth(numCol++, 15 * 256); // Data Deposito
		foglio.setColumnWidth(numCol++, 15 * 256); // Tipo Impugnazione
		foglio.setColumnWidth(numCol++, 15 * 256); // Presentato da
		foglio.setColumnWidth(numCol++, 15 * 256); // Data Impugnazione
		foglio.setColumnWidth(numCol++, 15 * 256); // Data Arrivo in Cancelleria
		foglio.setColumnWidth(numCol++, 15 * 256); // Data Trasmissione
		foglio.setColumnWidth(numCol++, 15 * 256); // Autorita' Destinataria
		foglio.setColumnWidth(numCol++, 15 * 256); // Data Decisione
		foglio.setColumnWidth(numCol++, 15 * 256); // Tenore Decisione
		foglio.setColumnWidth(numCol++, 15 * 256); // Data Restituzione Atti
		foglio.setColumnWidth(numCol++, 15 * 256); // Stato Esecuzione
		foglio.setColumnWidth(numCol++, 15 * 256); // Stato

		numCol = 0;
		// Intestazione colonne
		HSSFUtils.getInstance().setCell(riga, numCol++, "Progr.", hssfcs);
		HSSFUtils.getInstance().setCell(riga, numCol++, "N° Ricorso Opposizione", hssfcs);
		HSSFUtils.getInstance().setCell(riga, numCol++, "Procedimento SIGE", hssfcs);
		HSSFUtils.getInstance().setCell(riga, numCol++, "Generalita' Soggetto", hssfcs);
		HSSFUtils.getInstance().setCell(riga, numCol++, "Data Emissione", hssfcs);
		HSSFUtils.getInstance().setCell(riga, numCol++, "Tipo Provvedimento", hssfcs);
		// HSSFUtils.getInstance().setCell(riga, numCol++, "Contenuto Atto", hssfcs);
		// HSSFUtils.getInstance().setCell(riga, numCol++, "Esito Provvedimento", hssfcs);
		HSSFUtils.getInstance().setCell(riga, numCol++, "Data Deposito", hssfcs);
		HSSFUtils.getInstance().setCell(riga, numCol++, "Tipo Impugnazione", hssfcs);
		HSSFUtils.getInstance().setCell(riga, numCol++, "Presentato da", hssfcs);
		HSSFUtils.getInstance().setCell(riga, numCol++, "Data Impugnazione", hssfcs);
		HSSFUtils.getInstance().setCell(riga, numCol++, "Data Arrivo in Cancelleria", hssfcs);
		HSSFUtils.getInstance().setCell(riga, numCol++, "Data Trasmissione", hssfcs);
		HSSFUtils.getInstance().setCell(riga, numCol++, "Autorita' Destinataria", hssfcs);
		HSSFUtils.getInstance().setCell(riga, numCol++, "Data Decisione", hssfcs);
		HSSFUtils.getInstance().setCell(riga, numCol++, "Tenore Decisione", hssfcs);
		HSSFUtils.getInstance().setCell(riga, numCol++, "Data Rest. Atti", hssfcs);
		HSSFUtils.getInstance().setCell(riga, numCol++, "Stato Esecuzione", hssfcs);
		HSSFUtils.getInstance().setCell(riga, numCol++, "Stato", hssfcs);

		// stile per celle col bordo con testo centrato
		HSSFCellStyle hssfcsCorpo = null;
		hssfcsCorpo = getBordo4Lati(hssfwb);
		hssfcsCorpo.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		hssfcsCorpo.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		hssfcsCorpo.setWrapText(true);

		Iterator<FascicoloSigeEstesoModel> i = v.iterator();
		int progr = 1;
		while (i.hasNext()) {
			numCol = 0;
			FascicoloSigeEstesoModel fsem = i.next();
			riga = foglio.createRow(numeroRiga++);
			// Progr.
			HSSFUtils.getInstance().setCell(riga, numCol++, "" + progr, hssfcsCorpo);
			// N° Ricorso Opposizione
			HSSFUtils.getInstance().setCell(riga, numCol++, fsem.getImpugnazioneSige().getAnnoS7().toString()
					+ "/" + fsem.getImpugnazioneSige().getProgrS7().toString(), hssfcsCorpo);
			// Procedimento SIGE
			HSSFUtils.getInstance().setCell(riga, numCol++, fsem.getFascicoloSige().getChiaveAnno().toString()
					+ "/" + fsem.getFascicoloSige().getChiaveProgr().toString(), hssfcsCorpo);
			// Generalita' Soggetto
			String dn = (fsem.getSoggetto().getDataNascita() != null)
					? DateUtils.getDateToString(fsem.getSoggetto().getDataNascita(), "dd/MM/yyyy")
					: "";
			String ln = "";
			String sn = Utils.isPresent(fsem.getSoggetto().getDescrStatoNascita())
					? fsem.getSoggetto().getDescrStatoNascita()
					: "";
			if (Utils.isPresent(fsem.getSoggetto().getCodStatoNascita())
					&& "039".equals(fsem.getSoggetto().getCodStatoNascita())) {
				ln = (Utils.isPresent(fsem.getSoggetto().getDescrComuneNascita())
						&& !"-".equals(fsem.getSoggetto().getDescrComuneNascita()))
								? fsem.getSoggetto().getDescrComuneNascita() + " ("
										+ fsem.getSoggetto().getCodProvinciaNascita() + ")"
								: "";
			} else {
				if (Utils.isPresent(fsem.getSoggetto().getDescComuneNascitaEstero()))
					ln = fsem.getSoggetto().getDescComuneNascitaEstero() + " (" + sn + ")";
				else
					ln = sn;
			}
			String generalita = fsem.getSoggetto().getNome() + " " + fsem.getSoggetto().getCognome() + " "
					+ dn + " " + ln;
			HSSFUtils.getInstance().setCell(riga, numCol++, generalita, hssfcsCorpo);
			// Data Emissione
			HSSFUtils.getInstance().setCell(riga, numCol++,
					StringUtils.toStringJSP(DateUtils.getDateToString(fsem.getProvvedimentoEventoSige()
							.getEventoNotifica().getEvento().getDataEmissione(), "dd/MM/yyyy"), "-"),
					hssfcsCorpo);
			// Tipo Provvedimento
			HSSFUtils.getInstance()
					.setCell(
							riga, numCol++, StringUtils.toStringJSP(fsem.getProvvedimentoEventoSige()
									.getEventoNotifica().getEvento().getDescrTipoProvvedimento(), "-"),
							hssfcsCorpo);
			// Contenuto Atto
			// HSSFUtils.getInstance().setCell(riga, numCol++, StringUtils.toStringJSP(
			// fsem.getProvvedimentoEventoSige().getEventoNotifica().getEvento().getDescrMotivo(), "-"),
			// hssfcsCorpo);
			// String descrOggettoSige = null;
			// String descrEsitoTenoreSige = null;
			// if (fsem.getProvvedimentoEventoSige().getTenoriEstesi() != null
			// && !fsem.getProvvedimentoEventoSige().getTenoriEstesi().isEmpty()) {
			// TenoreSigeEstesoModel tsem = (TenoreSigeEstesoModel) fsem.getProvvedimentoEventoSige()
			// .getTenoriEstesi().get(0);
			// if (StringUtils.checkValidValue(tsem.getDescrOggettoSige()))
			// descrOggettoSige = tsem.getDescrOggettoSige();
			// if (StringUtils.checkValidValue(tsem.getDescrEsitoSige()))
			// descrEsitoTenoreSige = tsem.getDescrEsitoSige();
			// }
			// HSSFUtils.getInstance().setCell(riga, numCol++ StringUtils.toStringJSP(descrOggettoSige,
			// "-"),
			// hssfcsCorpo);
			// Esito Provvedimento
			// HSSFUtils.getInstance().setCell(riga, numCol++,
			// StringUtils.toStringJSP(
			// fsem.getProvvedimentoEventoSige().getEventoNotifica().getEvento().getDescrEsito(),
			// "-"),
			// hssfcsCorpo);
			// HSSFUtils.getInstance().setCell(riga, numCol++,
			// StringUtils.toStringJSP(descrEsitoTenoreSige, "-"), hssfcsCorpo);
			// Data Deposito
			HSSFUtils.getInstance().setCell(riga, numCol++,
					StringUtils.toStringJSP(DateUtils.getDateToString(
							fsem.getProvvedimentoEventoSige().getProvvedimento().getDataDeposito(),
							"dd/MM/yyyy"), "-"),
					hssfcsCorpo);
			// Tipo Impugnazione
			HSSFUtils.getInstance().setCell(riga, numCol++,
					StringUtils.toStringJSP(fsem.getImpugnazioneSige().getDescrTipoImpugnazione(), "-"),
					hssfcsCorpo);
			// Presentato da
			HSSFUtils.getInstance().setCell(riga, numCol++,
					StringUtils.toStringJSP(fsem.getImpugnazioneSige().getDescrSoggettoImpugnante(), "-"),
					hssfcsCorpo);
			// Data Impugnazione
			HSSFUtils.getInstance().setCell(riga, numCol++,
					StringUtils.toStringJSP(DateUtils
							.getDateToString(fsem.getImpugnazioneSige().getDataRicorso(), "dd/MM/yyyy"), "-"),
					hssfcsCorpo);
			// Data Arrivo in Cancelleria
			HSSFUtils.getInstance().setCell(riga, numCol++,
					StringUtils.toStringJSP(DateUtils.getDateToString(
							fsem.getImpugnazioneSige().getDataArrivoCancelleria(), "dd/MM/yyyy"), "-"),
					hssfcsCorpo);
			// Data Trasmissione
			HSSFUtils.getInstance().setCell(riga, numCol++,
					StringUtils.toStringJSP(DateUtils.getDateToString(
							fsem.getImpugnazioneSige().getDataTrasmissioneAtti(), "dd/MM/yyyy"), "-"),
					hssfcsCorpo);
			// Autorita' Destinataria
			HSSFUtils.getInstance().setCell(riga, numCol++,
					StringUtils.toStringJSP(fsem.getImpugnazioneSige().getDescrAutoritaDestinataria(), "-"),
					hssfcsCorpo);
			// Data Decisione
			HSSFUtils.getInstance()
					.setCell(riga, numCol++,
							StringUtils.toStringJSP(DateUtils.getDateToString(
									fsem.getImpugnazioneSige().getDataDecisione(), "dd/MM/yyyy"), "-"),
							hssfcsCorpo);
			// Tenore Decisione
			HSSFUtils.getInstance().setCell(riga, numCol++,
					StringUtils.toStringJSP(fsem.getImpugnazioneSige().getDescrTenoreDecisione(), "-"),
					hssfcsCorpo);
			// Data Restituzione Atti
			HSSFUtils.getInstance().setCell(riga, numCol++,
					StringUtils.toStringJSP(DateUtils.getDateToString(
							fsem.getImpugnazioneSige().getDataRestituzioneAtti(), "dd/MM/yyyy"), "-"),
					hssfcsCorpo);
			// Stato Esecuzione
			String statoEsec = null;
			String stato = null;
			if (fsem.getImpugnazioneSige().getFlagSospEsec() != null
					&& ("S".equals(fsem.getImpugnazioneSige().getFlagSospEsec())))
				statoEsec = "Esecuzione sospesa";
			HSSFUtils.getInstance().setCell(riga, numCol++, StringUtils.toStringJSP(statoEsec, "-"),
					hssfcsCorpo);
			// Stato
			if (fsem.getImpugnazioneSige().getFlagAnnullamento() != null
					&& "S".equals(fsem.getImpugnazioneSige().getFlagAnnullamento()))
				stato = "ANNULLATO";
			HSSFUtils.getInstance().setCell(riga, numCol++, StringUtils.toStringJSP(stato, "-"), hssfcsCorpo);
			progr++;
		}

		// valore di ritorno
		return hssfwb;
	}

} // Chiude classe StatisController