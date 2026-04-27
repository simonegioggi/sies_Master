package siap.sige.fascicolo.action;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Iterator;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.util.StringUtils;
import f3b.util.Utils;
import f3b.web.IWebConstants;
import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.decodifiche.util.DecodificheUtils;
import siap.sico.ufficio.model.UfficioModel;
import siap.sige.fascicolo.controller.IFascicoloSige;
import siap.sige.fascicolo.model.FascicoloSigeEstesoModel;
import siap.sige.fascicolo.model.RicercaFascicoloSigeModel;
import siap.sige.magistrato.controller.IMagistrato;
import siap.sige.magistrato.model.MagistratoModel;
import siap.sige.sezione.controller.ISezione;
import siap.sige.sezione.model.SezioneModel;
import siap.sige.util.SIGELookupRemote;

/**
 * Title: ActRicercaFSigePerEstremiStatistica - Description: Attiva la Ricerca dei Procedimenti SIGE per
 * Estremi Atto per stamparne l'elenco in un foglio excel.
 *
 * @version 1.0
 */
@SuppressWarnings("rawtypes")
public class ActRicercaFSigePerEstremiStatistica extends ActRicercaFSigePerEstremi {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	// Intestazione Foglio Excel
	private int setIntestazione(HSSFSheet sheet, UfficioModel uffUteConnesso, HSSFCellStyle csNull,
			RicercaFascicoloSigeModel ricercaFascSigeModel) {

		int nRow = 0;

		// Create a row and put some cells in it. Rows are 0 based.
		HSSFRow row = sheet.createRow(nRow);
		// Create a cell and put a value in it.
		String value = (uffUteConnesso.getDescrTipoUfficio().toUpperCase() + " DI "
				+ uffUteConnesso.getDescrComune().toUpperCase());
		setCell(row, (short) 0, value, csNull);
		nRow++;

		// row = sheet.createRow(nRow);
		// value = ("Tel. " + uffUteConnesso.getTelefono() + " - Fax " + uffUteConnesso.getFax());
		// setCell(row, (short) 0, value, csNull);

		row = sheet.createRow(nRow);
		setCell(row, (short) 0, "T3 - Servizi Penali", csNull);
		nRow++;
		row = sheet.createRow(nRow);
		setCell(row, (short) 0, "T3b - Tribunale, monocratico e collegiale, e Corte di Assise", csNull);
		nRow++;
		row = sheet.createRow(nRow);
		setCell(row, (short) 0,
				"T3b.13 - Elenco degli incidenti d'esecuzione conclusi dopo oltre 1 anno dall'iscrizione",
				csNull);
		nRow++;
		row = sheet.createRow(nRow);
		setCell(row, (short) 0, "Fonte del dato: cartacea/informatica", csNull);
		// nRow++;

		return nRow;
	}

	// Criteri di Ricerca Selezionati
	private int setCriteriRicercaSelezionati(HSSFSheet sheet, RicercaFascicoloSigeModel ricercaFascSigeModel,
			HSSFCellStyle csNull, int nRow) throws Exception {

		if (!(ricercaFascSigeModel.getDataIscrizioneIniziale() == null
				&& ricercaFascSigeModel.getDataIscrizioneFinale() == null
				&& ricercaFascSigeModel.getDataDefinizioneIniziale() == null
				&& ricercaFascSigeModel.getDataDefinizioneFinale() == null
				&& ricercaFascSigeModel.getCodTipoAtto() == null
				&& ricercaFascSigeModel.getCodOggettoSige() == null
				&& ricercaFascSigeModel.getCodMagistrato() == null
				&& ricercaFascSigeModel.getIdSezione() == null
				&& ricercaFascSigeModel.getCodTipoRito() == null
				&& ricercaFascSigeModel.getCodTipoRito().equals("-")
				&& ricercaFascSigeModel.getDataFinePendenza() == null)) {

			// Create a row and put some cells in it. Rows are 0 based.
			HSSFRow row = sheet.createRow(nRow);
			// Create a cell and put a value in it.
			String value = ("Criteri di ricerca selezionati: ");
			setCell(row, (short) 0, value, csNull);
			nRow++;
			row = sheet.createRow(nRow);

			row = sheet.createRow(nRow);
			String dataIniziale = (ricercaFascSigeModel.getDataIscrizioneIniziale() == null ? ""
					: DateUtils.getDateToString(ricercaFascSigeModel.getDataIscrizioneIniziale(),
							"dd/MM/yyyy"));
			String dataFinale = (ricercaFascSigeModel.getDataIscrizioneFinale() == null ? ""
					: DateUtils.getDateToString(ricercaFascSigeModel.getDataIscrizioneFinale(),
							"dd/MM/yyyy"));
			// 20221214: [SG] aggiunto controllo consistenza dato
			if (Utils.isPresent(dataIniziale) || Utils.isPresent(dataFinale)) {
				value = "Intervallo date di Iscrizione:";
				if (Utils.isPresent(dataIniziale))
					value += " dal " + dataIniziale;
				if (Utils.isPresent(dataFinale))
					value += " al " + dataFinale;
				setCell(row, (short) 0, value, csNull);
			}

			if (ricercaFascSigeModel.getCodTipoAtto() != null
					&& ricercaFascSigeModel.getCodTipoAtto().length() > 1) {
				nRow++;
				row = sheet.createRow(nRow);
				value = ("Tipo Atto: "
						+ DecodificheUtils.getDescbyCode(DecodificheManager.getInstance().getTipoAttoSige(),
								ricercaFascSigeModel.getCodTipoAtto()));
				setCell(row, (short) 0, value, csNull);
			}

			if (ricercaFascSigeModel.getCodOggettoSige() != null
					&& ricercaFascSigeModel.getCodOggettoSige().length() > 1) {
				nRow++;
				row = sheet.createRow(nRow);
				value = ("Oggetto: "
						+ DecodificheUtils.getDescbyCode(DecodificheManager.getInstance().getOggettoSige(),
								ricercaFascSigeModel.getCodOggettoSige()));
				setCell(row, (short) 0, value, csNull);
			} else { // 20221214: [SG] aggiunta visualizzazione parametro di ricerca
				nRow++;
				row = sheet.createRow(nRow);
				value = ("Oggetto: Tutti");
				setCell(row, (short) 0, value, csNull);
			}

			// 20221214: [SG] > 0 NON > 1
			if (ricercaFascSigeModel.getCodMagistrato() != null
					&& ricercaFascSigeModel.getCodMagistrato().length() > 0) {
				if (ricercaFascSigeModel.getCodMagistrato().equals("9")) {
					value = ("Magistrato: Tutti i Magistrati");
				} else if (ricercaFascSigeModel.getCodMagistrato().equals("0")) {
					value = ("Magistrato: Magistrato non presente");
				} else {
					IMagistrato iMag = SIGELookupRemote.getMagistratoRemote();
					// 20170918: [SG] aggiunto parametro di passaggio poich� il magistrato pu� essere inserito
					// da un ufficio differente da quello in cui ha delle udienze poich� trasferito
					MagistratoModel magModel = iMag.ExRicercaMagistratoByCod(
							ricercaFascSigeModel.getCodMagistrato(), getCodUfficioUtenteConnesso());
					String nomeCognomeMag = magModel.getNome() + " " + magModel.getCognome();
					value = ("Magistrato: " + nomeCognomeMag);
				}
				nRow++;
				row = sheet.createRow(nRow);
				setCell(row, (short) 0, value, csNull);
			}

			// 20221214: [SG] > 0 NON > 1 (.toString().length())
			if (ricercaFascSigeModel.getIdSezione() != null
					&& ricercaFascSigeModel.getIdSezione().toString().length() > 0) {
				if (ricercaFascSigeModel.getIdSezione().equals(new BigDecimal(9))) {
					value = ("Sezione: Tutte le Sezioni");
				} else if (ricercaFascSigeModel.getIdSezione().equals(new BigDecimal(0))) {
					value = ("Sezione: Sezione non presente");
				} else {
					ISezione lSezCtrl = SIGELookupRemote.getSezioneRemote();
					SezioneModel lSezione = lSezCtrl
							.ExRicercaSezioneByKey(ricercaFascSigeModel.getIdSezione());
					String descSezione = lSezione.getDescrizione();
					value = ("Sezione: " + descSezione);
				}
				nRow++;
				row = sheet.createRow(nRow);
				setCell(row, (short) 0, value, csNull);
			}

			if (ricercaFascSigeModel.getCodTipoRito() != null
					&& !ricercaFascSigeModel.getCodTipoRito().equals("-")) {
				if (ricercaFascSigeModel.getCodTipoRito().equals("N")) {
					value = ("Tipo Rito: Mancante");
				} else if (ricercaFascSigeModel.getCodTipoRito().equals("T")) {
					value = ("Tipo Rito: Monocratico e Collegiale");
				} else if (ricercaFascSigeModel.getCodTipoRito().equals("C")) {
					value = ("Tipo Rito: Collegiale");
				} else if (ricercaFascSigeModel.getCodTipoRito().equals("M")) {
					value = ("Tipo Rito: Monocratico");
				}
				nRow++;
				row = sheet.createRow(nRow);
				setCell(row, (short) 0, value, csNull);
			}

			/*
			 * if(ricercaFascSigeModel.getDataIscrizioneIniziale() != null ||
			 * ricercaFascSigeModel.getDataIscrizioneFinale() != null ){ value =
			 * ("Procedimenti con Data Iscrizione: " ); if(ricercaFascSigeModel.getDataIscrizioneIniziale() !=
			 * null){ value = value + (" Dal  ") +
			 * DateUtils.getDateToString(ricercaFascSigeModel.getDataIscrizioneIniziale(), "dd/MM/yyyy"); }
			 * if(ricercaFascSigeModel.getDataIscrizioneFinale() != null){ value = value + (" Al  ") +
			 * DateUtils.getDateToString(ricercaFascSigeModel.getDataIscrizioneFinale(), "dd/MM/yyyy"); }
			 * setCell(row, (short) 0, value, csNull); }
			 */

			if (ricercaFascSigeModel.getDataDefinizioneIniziale() != null
					|| ricercaFascSigeModel.getDataDefinizioneFinale() != null) {
				value = ("Procedimenti con Data Definizione: ");
				if (ricercaFascSigeModel.getDataDefinizioneIniziale() != null) {
					value = value + (" Dal  ") + DateUtils
							.getDateToString(ricercaFascSigeModel.getDataDefinizioneIniziale(), "dd/MM/yyyy");
				}
				if (ricercaFascSigeModel.getDataDefinizioneFinale() != null) {
					value = value + (" Al  ") + DateUtils
							.getDateToString(ricercaFascSigeModel.getDataDefinizioneFinale(), "dd/MM/yyyy");
				}
				nRow++;
				row = sheet.createRow(nRow);
				setCell(row, (short) 0, value, csNull);
			}

			if (ricercaFascSigeModel.getDataFinePendenza() != null) {
				value = ("Procedimenti Pendenti al: ")
						+ DateUtils.getDateToString(ricercaFascSigeModel.getDataFinePendenza(), "dd/MM/yyyy");
				nRow++;
				row = sheet.createRow(nRow);
				setCell(row, (short) 0, value, csNull);
			}
		}
		return nRow;
	}

	private HSSFCell setCell(HSSFRow row, short nCol, String value, HSSFCellStyle cs) {

		HSSFCell cell = row.createCell(nCol);
		cell.setCellValue(value);
		cell.setCellStyle(cs);

		return cell;
	}

	// 20191008 [SG]: richiesta Nunzia Alfieri Post Collaudo 11.3
	private HSSFCell setCellNumeric(HSSFRow row, short nCol, int value, HSSFCellStyle cs) {

		HSSFCell cell = row.createCell(nCol);
		cell.setCellValue(value);
		cell.setCellStyle(cs);

		return cell;
	}

	private HSSFCellStyle getBordo4Lati(HSSFWorkbook wb) {

		HSSFCellStyle cs = wb.createCellStyle();
		cs.setBorderBottom(BorderStyle.THIN);
		cs.setBorderTop(BorderStyle.THIN);
		cs.setBorderRight(BorderStyle.THIN);
		cs.setBorderLeft(BorderStyle.THIN);

		return cs;
	}

	// private HSSFCellStyle getBordo4LatiBold(HSSFWorkbook wb) {
	//
	// HSSFCellStyle cs = wb.createCellStyle();
	// cs.setBorderBottom(BorderStyle.THICK);
	// cs.setBorderTop(BorderStyle.THICK);
	// cs.setBorderRight(BorderStyle.THICK);
	// cs.setBorderLeft(BorderStyle.THICK);
	//
	// return cs;
	// }

	/**
	 * La funzione prepara la pagina xls con l'elenco dei Procedimenti Sige per Estremi Atto.
	 *
	 * @param wb
	 * @param aUffUteConnesso
	 * @param lFascicoliSige
	 * @throws Exception
	 */
	private void creaFoglioElencoProcedimentiSige(HSSFWorkbook wb, UfficioModel aUffUteConnesso,
			Vector<FascicoloSigeEstesoModel> lFascicoliSige, RicercaFascicoloSigeModel ricercaFascSigeModel)
			throws Exception {

		short numCol = 0;
		FascicoloSigeEstesoModel lFascicoloSigeEsteso = null;

		HSSFSheet sheet = wb.createSheet("ElencoProcedimentiPerEstremiAtto");
		HSSFCellStyle csNull = wb.createCellStyle();
		HSSFCellStyle csNullBold = wb.createCellStyle();

		int nRow = 0;

		// Intestazione del foglio excel
		nRow = setIntestazione(sheet, aUffUteConnesso, csNull, ricercaFascSigeModel);

		nRow++;
		nRow++;

		HSSFRow row = sheet.createRow(nRow);
		row = sheet.createRow(nRow);

		// Criteri di Ricerca Selezionati
		nRow = setCriteriRicercaSelezionati(sheet, ricercaFascSigeModel, csNull, nRow);
		nRow++;
		nRow++;

		row = sheet.createRow(nRow);

		HSSFFont my_font = wb.createFont();
		my_font.setBold(true);

		csNullBold.setFont(my_font);

		setCell(row, (short) 0, "Elenco Procedimenti Sige per Estremi Atto ", csNullBold);
		nRow += 2;

		// settaggio della larghezza
		// delle colonne
		numCol = 0;
		sheet.setColumnWidth(numCol++, (short) (10 * 256));
		sheet.setColumnWidth(numCol++, (short) (20 * 256));
		sheet.setColumnWidth(numCol++, (short) (20 * 256));
		sheet.setColumnWidth(numCol++, (short) (30 * 256));
		sheet.setColumnWidth(numCol++, (short) (30 * 256));
		sheet.setColumnWidth(numCol++, (short) (20 * 256));
		sheet.setColumnWidth(numCol++, (short) (20 * 256));
		sheet.setColumnWidth(numCol++, (short) (20 * 256));
		sheet.setColumnWidth(numCol++, (short) (20 * 256));
		sheet.setColumnWidth(numCol++, (short) (20 * 256));
		sheet.setColumnWidth(numCol++, (short) (20 * 256));
		sheet.setColumnWidth(numCol++, (short) (20 * 256));
		sheet.setColumnWidth(numCol++, (short) (20 * 256));
		sheet.setColumnWidth(numCol++, (short) (30 * 256));

		// stile per celle col bordo con testo centrato
		HSSFCellStyle csCenter = getBordo4Lati(wb);
		csCenter.setAlignment(HorizontalAlignment.CENTER);
		csCenter.setVerticalAlignment(VerticalAlignment.CENTER);
		csCenter.setWrapText(true);

		// stile per celle col bordo con testo allineato a sinistra
		HSSFCellStyle csLeft = getBordo4Lati(wb);
		csLeft.setAlignment(HorizontalAlignment.LEFT);
		csLeft.setVerticalAlignment(VerticalAlignment.CENTER);
		csLeft.setWrapText(true);

		// stile per celle col bordo con testo centrato e grossetto
		HSSFCellStyle csCenterBold = getBordo4Lati(wb);
		csCenterBold.setAlignment(HorizontalAlignment.CENTER);
		csCenterBold.setVerticalAlignment(VerticalAlignment.CENTER);
		csCenterBold.setWrapText(true);
		csCenterBold.setFont(my_font);

		row = sheet.createRow(nRow++);

		// Intestazione colonne
		numCol = 0;
		setCell(row, numCol++, "N�\nord.", csCenterBold);
		setCell(row, numCol++, "Numero\nRegistro mod.\n32", csCenterBold);
		setCell(row, numCol++, "Tipologia Atto", csCenterBold);
		setCell(row, numCol++, "Tipologia\nincidente\nd'esecuzione", csCenterBold);
		setCell(row, numCol++, "Magistrato", csCenterBold);
		setCell(row, numCol++, "Data Arrivo in Cancelleria", csCenterBold);
		setCell(row, numCol++, "Data\nIscrizione", csCenterBold);
		setCell(row, numCol++, "Tipologia Rito", csCenterBold);
		setCell(row, numCol++, "Data prima\nUdienza", csCenterBold);
		setCell(row, numCol++, "Data ultima\nUdienza", csCenterBold);
		setCell(row, numCol++, "Data deposito\nProvvedimento", csCenterBold);
		setCell(row, numCol++, "Motivo Altra Definizione Opposizione/Ricorso", csCenterBold);
		setCell(row, numCol++, "Numero\ngiorni\nintercorsi\ntra\niscrizione e\ndefinizione", csCenterBold);
		setCell(row, numCol++, "Numero\ngiorni oltre\ni 365", csCenterBold);

		row = sheet.createRow(nRow++);

		// Ciclo sui fascicoli SIGE
		Iterator itx = lFascicoliSige.iterator();
		int progr = 0;

		// inizio ciclo di scrittura dei dati
		while (itx.hasNext()) {
			numCol = 0;

			lFascicoloSigeEsteso = (FascicoloSigeEstesoModel) itx.next();

			// Prog.
			setCell(row, numCol++, new Integer(++progr).toString(), csCenter);
			// Numero SIGE
			setCell(row, numCol++, lFascicoloSigeEsteso.getFascicoloSige().getChiaveAnno() + "/"
					+ lFascicoloSigeEsteso.getFascicoloSige().getChiaveProgr(), csCenter);
			// Tipo Atto
			if (lFascicoloSigeEsteso.getRichiestaSige().getDescrTipoAtto() != null) {
				setCell(row, numCol++, lFascicoloSigeEsteso.getRichiestaSige().getDescrTipoAtto(), csCenter);
			} else {
				setCell(row, numCol++, "", csCenter);
			}
			// Tipologia incidente d'esecuzione
			// (In questa colonna viene riportato l'Oggetto del Procedimento,
			// in presenza di pi� Oggetti viene riportata la dicitura "Oggetto Multiplo")
			setCell(row, numCol++,
					StringUtils.toStringJSP(lFascicoloSigeEsteso.getFascicoloSige().getDescOggetto()),
					csCenter);
			// Cognome e nome Magistrato
			setCell(row, numCol++, lFascicoloSigeEsteso.getFascicoloSige().getNomeCognomeMagistrato(),
					csCenter);
			// Data arrivo in cancelleria
			if (lFascicoloSigeEsteso.getRichiestaSige().getDataArrivoCancelleria() != null) {
				setCell(row, numCol++,
						DateUtils.getDateToString(
								lFascicoloSigeEsteso.getRichiestaSige().getDataArrivoCancelleria(),
								"dd/MM/yyyy"),
						csCenter);
			} else {
				setCell(row, numCol++, "", csCenter);
			}
			// Data di Iscrizione
			if (lFascicoloSigeEsteso.getFascicoloSige().getDataIscrizione() != null) {
				setCell(row, numCol++, DateUtils.getDateToString(
						lFascicoloSigeEsteso.getFascicoloSige().getDataIscrizione(), "dd/MM/yyyy"), csCenter);
			} else {
				setCell(row, numCol++, "-", csCenter);
			}
			// Tipologia Rito
			if (lFascicoloSigeEsteso.getFascicoloSige().getDescrTipoGiudizio() != null) {
				setCell(row, numCol++, lFascicoloSigeEsteso.getFascicoloSige().getTipologiaRito(), csCenter);
			} else {
				setCell(row, numCol++, "", csCenter);
			}
			// PRIMA Data Udienza
			if (lFascicoloSigeEsteso.getFascicoloSige().getDataPrimaUdienza() != null) {
				setCell(row, numCol++,
						DateUtils.getDateToString(
								lFascicoloSigeEsteso.getFascicoloSige().getDataPrimaUdienza(), "dd/MM/yyyy"),
						csCenter);
			} else {
				setCell(row, numCol++, "-", csCenter);
			}
			// ULTIMA DATA Udienza
			if (lFascicoloSigeEsteso.getFascicoloSige().getDataUltimaUdienza() != null) {
				setCell(row, numCol++,
						DateUtils.getDateToString(
								lFascicoloSigeEsteso.getFascicoloSige().getDataUltimaUdienza(), "dd/MM/yyyy"),
						csCenter);
			} else {
				setCell(row, numCol++, "-", csCenter);
			}

			// ULTIMA DEFINIZIONE
			if (lFascicoloSigeEsteso.getFascicoloSige().getDataDefinizione() != null) {
				setCell(row, numCol++,
						DateUtils.getDateToString(
								lFascicoloSigeEsteso.getFascicoloSige().getDataDefinizione(), "dd/MM/yyyy"),
						csCenter);
			} else {
				setCell(row, numCol++, "-", csCenter);
			}
			// Motivo Altra Definizione (Opposizione/Ricorso)
			if (lFascicoloSigeEsteso.getFascicoloSige().getMotivoAltraDefinizione() != null) {
				setCell(row, numCol++, lFascicoloSigeEsteso.getFascicoloSige().getMotivoAltraDefinizione(),
						csCenter);
			} else {
				setCell(row, numCol++, "", csCenter);
			}
			// Numero di giorni intercorsi tra iscrizione e definizione
			if (lFascicoloSigeEsteso.getFascicoloSige()
					.getNumGiorniIntercorsiTraIscrizioneEDeposito() != null) {
				// 20191008 [SG]: richiesta Nunzia Alfieri Post Collaudo 11.3
				setCellNumeric(
						row, numCol++, lFascicoloSigeEsteso.getFascicoloSige()
								.getNumGiorniIntercorsiTraIscrizioneEDeposito().toBigInteger().intValue(),
						csCenter);
			} else {
				// 20191008 [SG]: richiesta Nunzia Alfieri Post Collaudo 11.3
				setCell(row, numCol++, "", csCenter);
			}
			// Numero giorni oltre i 365
			if (lFascicoloSigeEsteso.getFascicoloSige()
					.getNumGiorniIntercorsiTraIscrizioneEDepositoAnno() != null) {
				// 20191008 [SG]: richiesta Nunzia Alfieri Post Collaudo 11.3
				setCellNumeric(row, numCol++,
						lFascicoloSigeEsteso.getFascicoloSige()
								.getNumGiorniIntercorsiTraIscrizioneEDepositoAnno().toBigInteger().intValue(),
						csCenter);
			} else {
				// 20191008 [SG]: richiesta Nunzia Alfieri Post Collaudo 11.3
				setCell(row, numCol++, "", csCenter);
			}

			// Scrittura riga del report
			row = sheet.createRow(nRow++);
		}
	}

	public String processRequest() throws Exception {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("" + getClass().getName() + " .processRequest: inizio ");

		RicercaFascicoloSigeModel ricercaFascSigeModel = letturaParametriRicerca();

		Vector<FascicoloSigeEstesoModel> lFascicoliSige = getElencoFascicoliStatisticaSige(
				ricercaFascSigeModel);
		// risultato della ricerca supera 65536 record
		if (lFascicoliSige != null && lFascicoliSige.size() > 65536) {
			throw new F3BException(F3BException.USER_MESSAGE,
					"Impostare almeno un parametro di ricerca, in quanto il risultato non � interamente visualizzabile.");
		}

		if (lFascicoliSige != null)
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Risultati ricerca completa : " + lFascicoliSige.size());

		HSSFWorkbook wb = new HSSFWorkbook();
		ByteArrayOutputStream fileOut = new ByteArrayOutputStream();
		creaFoglioElencoProcedimentiSige(wb, getUfficioUtenteConnesso(), lFascicoliSige,
				ricercaFascSigeModel);

		// Generazione file xls
		try {
			wb.write(fileOut);
		} catch (IOException ioe) {
			siesLogger.info("IOException: " + ioe);
			throw new F3BException("ActRicercaFSigePerEstremiStatistica.processRequest: " + ioe);
		}

		setRequestAttribute("report", fileOut);
		setRequestAttribute(IWebConstants.DISPOSITION_FIELD, IWebConstants.ATTACHMENT_DISPOSITION_FILE);

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("" + getClass().getName() + " .processRequest: fine ");

		return IWebConstants.PG_DOWNLOAD_DOCUMENT;
	}

	protected Vector<FascicoloSigeEstesoModel> getElencoFascicoliStatisticaSige(
			RicercaFascicoloSigeModel lFascicoloSigeMod) throws F3BException {

		// Viene istanziato il controller per la ricerca
		IFascicoloSige lCtrl = SIGELookupRemote.getFascicoloSigeRemote();
		Vector<FascicoloSigeEstesoModel> lFascicoli = lCtrl
				.ExRicercaStatisticaFascicoloSigeByEstremi(lFascicoloSigeMod);

		return lFascicoli;
	}

}