package siap.sige.fascicolo.action;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Iterator;
import java.util.Vector;

import org.apache.log4j.Logger;
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
		// Create a row and put some cells in it. Rows are 0 based.
		// row = sheet.createRow(nRow);
		// Create a cell and put a value in it.

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

		nRow++;
		row = sheet.createRow(nRow);
		String dataIniziale = (ricercaFascSigeModel.getDataIscrizioneIniziale() == null ? ""
				: DateUtils.getDateToString(ricercaFascSigeModel.getDataIscrizioneIniziale(), "dd/MM/yyyy"));
		String dataFinale = (ricercaFascSigeModel.getDataIscrizioneFinale() == null ? ""
				: DateUtils.getDateToString(ricercaFascSigeModel.getDataIscrizioneFinale(), "dd/MM/yyyy"));

		value = "periodo dal " + dataIniziale + " al " + dataFinale;
		setCell(row, (short) 0, value, csNull);

		return nRow;
	}

	// Criteri di Ricerca Selezionati
	private int setCriteriRicercaSelezionati(HSSFSheet sheet, RicercaFascicoloSigeModel mRicercaFascSigeModel,
			HSSFCellStyle csNull, int nRow) throws Exception {

		if (!(mRicercaFascSigeModel.getDataIscrizioneIniziale() == null
				&& mRicercaFascSigeModel.getDataIscrizioneFinale() == null
				&& mRicercaFascSigeModel.getDataDefinizioneIniziale() == null
				&& mRicercaFascSigeModel.getDataDefinizioneFinale() == null
				&& mRicercaFascSigeModel.getCodTipoAtto() == null
				&& mRicercaFascSigeModel.getCodOggettoSige() == null
				&& mRicercaFascSigeModel.getCodMagistrato() == null
				&& mRicercaFascSigeModel.getIdSezione() == null
				&& mRicercaFascSigeModel.getCodTipoRito() == null
				&& mRicercaFascSigeModel.getCodTipoRito().equals("-")
				&& mRicercaFascSigeModel.getDataFinePendenza() == null)) {

			// Create a row and put some cells in it. Rows are 0 based.
			HSSFRow row = sheet.createRow(nRow);
			// Create a cell and put a value in it.
			String value = ("Criteri di ricerca selezionati: ");
			setCell(row, (short) 0, value, csNull);

			nRow++;
			// Create a row and put some cells in it. Rows are 0 based.
			row = sheet.createRow(nRow);
			// Create a cell and put a value in it.

			if (mRicercaFascSigeModel.getCodTipoAtto() != null
					&& mRicercaFascSigeModel.getCodTipoAtto().length() > 1) {
				nRow++;
				row = sheet.createRow(nRow);
				value = ("Tipo Atto: "
						+ DecodificheUtils.getDescbyCode(DecodificheManager.getInstance().getTipoAttoSige(),
								mRicercaFascSigeModel.getCodTipoAtto()));
				setCell(row, (short) 0, value, csNull);
			}

			if (mRicercaFascSigeModel.getCodOggettoSige() != null
					&& mRicercaFascSigeModel.getCodOggettoSige().length() > 1) {
				nRow++;
				row = sheet.createRow(nRow);
				value = ("Oggetto: "
						+ DecodificheUtils.getDescbyCode(DecodificheManager.getInstance().getOggettoSige(),
								mRicercaFascSigeModel.getCodOggettoSige()));
				setCell(row, (short) 0, value, csNull);
			}

			if (mRicercaFascSigeModel.getCodMagistrato() != null
					&& mRicercaFascSigeModel.getCodMagistrato().length() > 1) {
				if (mRicercaFascSigeModel.getCodMagistrato().equals("9")) {
					value = ("Magistrato: Tutti i Magistrati");
				} else if (mRicercaFascSigeModel.getCodMagistrato().equals("0")) {
					value = ("Magistrato: Magistrato non presente");
				} else {
					IMagistrato iMag = SIGELookupRemote.getMagistratoRemote();
					// 20170918: [SG] aggiunto parametro di passaggio poichè il magistrato può essere inserito
					// da un ufficio differente da quello in cui ha delle udienze poichè trasferito
					MagistratoModel magModel = iMag.ExRicercaMagistratoByCod(
							mRicercaFascSigeModel.getCodMagistrato(), getCodUfficioUtenteConnesso());
					String nomeCognomeMag = magModel.getNome() + " " + magModel.getCognome();
					value = ("Magistrato: " + nomeCognomeMag);
				}
				nRow++;
				row = sheet.createRow(nRow);
				setCell(row, (short) 0, value, csNull);
			}

			if (mRicercaFascSigeModel.getIdSezione() != null
					&& mRicercaFascSigeModel.getIdSezione().intValue() > 1) {
				if (mRicercaFascSigeModel.getIdSezione().equals(new BigDecimal(9))) {
					value = ("Sezione: Tutte le Sezioni");
				} else if (mRicercaFascSigeModel.getIdSezione().equals(new BigDecimal(0))) {
					value = ("Sezione: Sezione non presente");
				} else {
					ISezione lSezCtrl = SIGELookupRemote.getSezioneRemote();
					SezioneModel lSezione = lSezCtrl
							.ExRicercaSezioneByKey(mRicercaFascSigeModel.getIdSezione());
					String descSezione = lSezione.getDescrizione();
					value = ("Sezione: " + descSezione);
				}
				nRow++;
				row = sheet.createRow(nRow);
				setCell(row, (short) 0, value, csNull);
			}

			if (mRicercaFascSigeModel.getCodTipoRito() != null
					&& !mRicercaFascSigeModel.getCodTipoRito().equals("-")) {
				if (mRicercaFascSigeModel.getCodTipoRito().equals("N")) {
					value = ("Tipo Rito: Mancante");
				} else if (mRicercaFascSigeModel.getCodTipoRito().equals("T")) {
					value = ("Tipo Rito: Monocratico e Collegiale");
				} else if (mRicercaFascSigeModel.getCodTipoRito().equals("C")) {
					value = ("Tipo Rito: Collegiale");
				} else if (mRicercaFascSigeModel.getCodTipoRito().equals("M")) {
					value = ("Tipo Rito: Monocratico");
				}
				nRow++;
				row = sheet.createRow(nRow);
				setCell(row, (short) 0, value, csNull);
			}

			/*
			 * if(mRicercaFascSigeModel.getDataIscrizioneIniziale() != null ||
			 * mRicercaFascSigeModel.getDataIscrizioneFinale() != null ){ value =
			 * ("Procedimenti con Data Iscrizione: " ); if(mRicercaFascSigeModel.getDataIscrizioneIniziale()
			 * != null){ value = value + (" Dal  ") +
			 * DateUtils.getDateToString(mRicercaFascSigeModel.getDataIscrizioneIniziale(), "dd/MM/yyyy"); }
			 * if(mRicercaFascSigeModel.getDataIscrizioneFinale() != null){ value = value + (" Al  ") +
			 * DateUtils.getDateToString(mRicercaFascSigeModel.getDataIscrizioneFinale(), "dd/MM/yyyy"); }
			 * setCell(row, (short) 0, value, csNull); }
			 */

			if (mRicercaFascSigeModel.getDataDefinizioneIniziale() != null
					|| mRicercaFascSigeModel.getDataDefinizioneFinale() != null) {
				value = ("Procedimenti con Data Definizione: ");
				if (mRicercaFascSigeModel.getDataDefinizioneIniziale() != null) {
					value = value + (" Dal  ") + DateUtils.getDateToString(
							mRicercaFascSigeModel.getDataDefinizioneIniziale(), "dd/MM/yyyy");
				}
				if (mRicercaFascSigeModel.getDataDefinizioneFinale() != null) {
					value = value + (" Al  ") + DateUtils
							.getDateToString(mRicercaFascSigeModel.getDataDefinizioneFinale(), "dd/MM/yyyy");
				}
				nRow++;
				row = sheet.createRow(nRow);
				setCell(row, (short) 0, value, csNull);
			}

			if (mRicercaFascSigeModel.getDataDefinizioneIniziale() != null
					|| mRicercaFascSigeModel.getDataDefinizioneFinale() != null) {
				value = ("Procedimenti con Data Definizione: ");
				if (mRicercaFascSigeModel.getDataDefinizioneIniziale() != null) {
					value = value + (" Dal  ") + DateUtils.getDateToString(
							mRicercaFascSigeModel.getDataDefinizioneIniziale(), "dd/MM/yyyy");
				}
				if (mRicercaFascSigeModel.getDataDefinizioneFinale() != null) {
					value = value + (" Al  ") + DateUtils
							.getDateToString(mRicercaFascSigeModel.getDataDefinizioneFinale(), "dd/MM/yyyy");
				}
				nRow++;
				row = sheet.createRow(nRow);
				setCell(row, (short) 0, value, csNull);
			}

			if (mRicercaFascSigeModel.getDataFinePendenza() != null) {
				value = ("Procedimenti Pendenti al: ") + DateUtils
						.getDateToString(mRicercaFascSigeModel.getDataFinePendenza(), "dd/MM/yyyy");
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
		cs.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		cs.setBorderTop(HSSFCellStyle.BORDER_THIN);
		cs.setBorderRight(HSSFCellStyle.BORDER_THIN);
		cs.setBorderLeft(HSSFCellStyle.BORDER_THIN);

		return cs;
	}

	// private HSSFCellStyle getBordo4LatiBold(HSSFWorkbook wb) {
	//
	// HSSFCellStyle cs = wb.createCellStyle();
	// cs.setBorderBottom(HSSFCellStyle.BORDER_THICK);
	// cs.setBorderTop(HSSFCellStyle.BORDER_THICK);
	// cs.setBorderRight(HSSFCellStyle.BORDER_THICK);
	// cs.setBorderLeft(HSSFCellStyle.BORDER_THICK);
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
			Vector<FascicoloSigeEstesoModel> lFascicoliSige, RicercaFascicoloSigeModel mRicercaFascSigeModel)
			throws Exception {

		short numCol = 0;
		FascicoloSigeEstesoModel lFascicoloSigeEsteso = null;

		HSSFSheet sheet = wb.createSheet("ElencoProcedimentiPerEstremiAtto");
		HSSFCellStyle csNull = wb.createCellStyle();
		HSSFCellStyle csNullBold = wb.createCellStyle();

		int nRow = 0;

		// Intestazione del foglio excel
		nRow = setIntestazione(sheet, aUffUteConnesso, csNull, mRicercaFascSigeModel);

		nRow++;
		nRow++;

		HSSFRow row = sheet.createRow(nRow);
		row = sheet.createRow(nRow);

		// Criteri di Ricerca Selezionati
		nRow = setCriteriRicercaSelezionati(sheet, mRicercaFascSigeModel, csNull, nRow);
		nRow++;
		nRow++;

		row = sheet.createRow(nRow);

		HSSFFont my_font = wb.createFont();
		my_font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);

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
		csCenter.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		csCenter.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		csCenter.setWrapText(true);

		// stile per celle col bordo con testo allineato a sinistra
		HSSFCellStyle csLeft = getBordo4Lati(wb);
		csLeft.setAlignment(HSSFCellStyle.ALIGN_LEFT);
		csLeft.setVerticalAlignment(HSSFCellStyle.ALIGN_LEFT);
		csLeft.setWrapText(true);

		// stile per celle col bordo con testo centrato e grossetto
		HSSFCellStyle csCenterBold = getBordo4Lati(wb);
		csCenterBold.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		csCenterBold.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		csCenterBold.setWrapText(true);
		csCenterBold.setFont(my_font);

		row = sheet.createRow(nRow++);

		// Intestazione colonne
		numCol = 0;
		setCell(row, numCol++, "N°\nord.", csCenterBold);
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
			// in presenza di più Oggetti viene riportata la dicitura "Oggetto Multiplo")
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

		RicercaFascicoloSigeModel mRicercaFascSigeModel = letturaParametriRicerca();

		// Vector<FascicoloSigeEstesoModel> lFascicoliSige = getElencoFascicoliSige(mRicercaFascSigeModel,
		// "-1");
		Vector<FascicoloSigeEstesoModel> lFascicoliSige = getElencoFascicoliStatisticaSige(
				mRicercaFascSigeModel);
		// risultato della ricerca supera 65536 record
		if (lFascicoliSige != null && lFascicoliSige.size() > 65536) {
			throw new F3BException(F3BException.USER_MESSAGE,
					"Impostare almeno un parametro di ricerca, in quanto il risultato non è interamente visualizzabile.");
		}

		if (lFascicoliSige != null)
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Risultati ricerca completa : " + lFascicoliSige.size());

		HSSFWorkbook wb = new HSSFWorkbook();
		ByteArrayOutputStream fileOut = new ByteArrayOutputStream();
		creaFoglioElencoProcedimentiSige(wb, getUfficioUtenteConnesso(), lFascicoliSige,
				mRicercaFascSigeModel);

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

		Vector<FascicoloSigeEstesoModel> lFascicoli = null;

		// lFascicoli = lCtrl.ExRicercaFascicoloSigeByEstremiPagina(lFascicoloSigeMod,
		// Integer.parseInt(lPagina));

		lFascicoli = lCtrl.ExRicercaStatisticaFascicoloSigeByEstremi(lFascicoloSigeMod);

		return lFascicoli;
	}

}