package siap.siep.scadenzario.action;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Iterator;
import java.util.Vector;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import siap.sico.calendar.model.CalendarModel;
import siap.sico.lock.controller.LockController;
import siap.sico.lock.model.LockModel;
import siap.sico.soggetto.model.SoggettoModel;
import siap.sico.ufficio.controller.IUfficio;
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siep.calcolopena.controller.CalcoloPenaController;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.scadenzario.controller.IScadenzario;
import siap.siep.scadenzario.model.ScadenzarioModel;
import siap.siep.scadenzario.util.ScadenzarioUtils;
import siap.siep.util.SIEPLookupRemote;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.util.StringUtils;
import f3b.web.IWebConstants;

/**
 * MEV_39
 * <p>
 * Title: ActStampaScadezarioDifferimentoMS
 * </p>
 * <p>
 * Description: Classe Action per la creazione dei report per Scadezario
 * </p>
 * <p>
 * Copyright: Copyright (c) 2017
 * </p>
 * <p>
 * Company: EII
 * </p>
 * 
 * @author Gioggi
 * @version 1.0
 */
@SuppressWarnings("rawtypes")
public class ActStampaScadenzarioDifferimentoMS extends ActionSiap implements ICostantiScadenzario {

	public String processRequest() throws F3BException {

		// Lock
		LockModel lm = LockController.lockIfNotLocked(getServletContext(), "SCADENZARIO_DIFFERIMENTO_MS", "1",
				getCodUtenteConnesso(), getSession().getId());

		if (lm != null) {
			setRequestAttribute(IWebConstants.MESSAGE_TEXT,
					"Questa funzione non può essere attivata contemporaneamente da più utenti!<BR>Riprovare più tardi!");
			return IWebConstants.PG_MESSAGE;
		}

		ScadenzarioModel sm = new ScadenzarioModel();
		// Imposto fisso 21=Differimento MS
		sm.setCodTipoScadenzario("21");
		sm.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
		sm.setCodStatoNotifica(null);
		String titolo = new String();
		titolo = "Tutti";

		// In scadenza oggi
		if (getRequestStringParameter("tipo").equals("oggi")) {
			sm.setDataFineScadenza(DateUtils.getSysDateAsDate("dd/MM/yyyy"));
			titolo = "In Scadenza Oggi";
			sm.setTipoRic("oggi");
		}

		// In scadenza entro
		// mev 39
		BigDecimal lAnni = null, lMesi = null, lGiorni = null;
		if (getRequestStringParameter("tipo").equals("sette")) {
			if (!isRequestParameterNullEmptyObj("anniScadenza"))
				lAnni = new BigDecimal(getRequestStringParameter("anniScadenza"));
			if (!isRequestParameterNullEmptyObj("mesiScadenza"))
				lMesi = new BigDecimal(getRequestStringParameter("mesiScadenza"));
			if (!isRequestParameterNullEmptyObj("giorniScadenza"))
				lGiorni = new BigDecimal(getRequestStringParameter("giorniScadenza"));
			CalendarModel lCalMod = new CalendarModel();
			CalendarModel lDataInizio = new CalendarModel();
			lDataInizio.setNumAnni(new BigDecimal(DateUtils.getYearToString(DateUtils.getSysDate())));
			lDataInizio.setNumMesi(new BigDecimal(DateUtils.getMonthToString(DateUtils.getSysDate())));
			lDataInizio.setNumGiorni(new BigDecimal(DateUtils.getDayToString(DateUtils.getSysDate())));
			if (lAnni != null && lAnni.intValue() != 0) {
				sm.setNumAnni(lAnni);
				lCalMod.setNumAnni(sm.getNumAnni());
			}
			if (lGiorni != null && lGiorni.intValue() != 0) {
				sm.setNumGiorni(lGiorni);
				lCalMod.setNumGiorni(sm.getNumGiorni());
			}
			if (lMesi != null && lMesi.intValue() != 0) {
				sm.setNumMesi(lMesi);
				lCalMod.setNumMesi(sm.getNumMesi());
			}

			sm.setTipoRic("sette");
			CalcoloPenaController lCalPen = new CalcoloPenaController();
			sm.setDataFineScadenza(lCalPen.exCalcolaNuovaDataFine(lDataInizio, lCalMod, false));
			sm.setDataInizioScadenza(DateUtils.getSysDateAsDate("dd/MM/yyyy"));
			titolo = "In Scadenza";
		}

		// Scaduti
		if (getRequestStringParameter("tipo").equals("scaduto")) {
			sm.setDataInizioScadenza(DateUtils.getSysDateAsDate("dd/MM/yyyy"));
			titolo = "Scaduti";
			sm.setTipoRic("scaduto");
		}

		IScadenzario is = SIEPLookupRemote.getScadenzarioRemote();
		Vector scadenzari = is.ExRicercaScadenzario(sm, "DIFFMS");

		HSSFWorkbook wb = new HSSFWorkbook();

		IUfficio lCtrlu = SICOLookupRemote.getUfficioRemote();
		UfficioModel ufm = new UfficioModel();
		ufm = lCtrlu.getUfficioByKey(getCodUfficioUtenteConnesso());
		String descUffIntestazione = ufm.getDescrComune();

		// creazione del file excel
		creaFoglioScadenzarioDifferimentoMS(sm, scadenzari, wb, getUfficioUtenteConnesso(),
				descUffIntestazione, titolo);

		// Generazione file xls
		ByteArrayOutputStream fileOut = new ByteArrayOutputStream();
		try {
			wb.write(fileOut);
		} catch (IOException ioe) {
			throw new F3BException("ActStampaScadezarioDifferimentoMS.processRequest: " + ioe);
		}

		setRequestAttribute("report", fileOut);
		setRequestAttribute(IWebConstants.DISPOSITION_FIELD, IWebConstants.ATTACHMENT_DISPOSITION_FILE);

		return IWebConstants.PG_DOWNLOAD_DOCUMENT;
	}

	private void creaFoglioScadenzarioDifferimentoMS(ScadenzarioModel sm, Vector scadenzari, HSSFWorkbook wb,
			UfficioModel um, String descUffIntestazione, String titolo) {

		// Stile della cella vuoto
		HSSFCellStyle csNull = wb.createCellStyle();
		HSSFCellStyle csBold = wb.createCellStyle();
		HSSFFont font = wb.createFont();
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		csBold.setFont(font);

		// stile per celle col bordo con carattere grassetto centrato
		HSSFCellStyle csBoldCenter = ScadenzarioUtils.getBordo4Lati(wb);
		csBoldCenter.setFont(font);
		csBoldCenter.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		csBoldCenter.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		csBoldCenter.setWrapText(true);

		// stile per celle col bordo con testo centrato
		HSSFCellStyle csCenter = ScadenzarioUtils.getBordo4Lati(wb);
		csCenter.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		csCenter.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		csCenter.setWrapText(true);

		// Stile della cella con bordi ed allineamento a destra
		HSSFCellStyle csR = ScadenzarioUtils.getBordo4Lati(wb);
		csR.setAlignment(HSSFCellStyle.ALIGN_RIGHT);

		// stile per celle col bordo con carattere grassetto
		// ALLINEATO A DESTRA
		HSSFCellStyle csBoldRight = ScadenzarioUtils.getBordo4Lati(wb);
		csBoldRight.setFont(font);
		csBoldRight.setAlignment(HSSFCellStyle.ALIGN_RIGHT);

		HSSFSheet sheet = wb.createSheet("Elenco");

		// Intestazione Ufficio
		int nRow = ScadenzarioUtils.setIntestazione(sheet, um, csNull);

		nRow++;
		nRow++;

		HSSFRow row = sheet.createRow(nRow);
		ScadenzarioUtils.setCell(row, (short) 0,
				"Consultazione Scadenzario Differimento Misure di Sicurezza - " + titolo, csNull);

		nRow++;
		nRow++;

		// Intestazione Elenco
		short numCol = 0;

		sheet.setColumnWidth((short) numCol++, (short) (20 * 256));
		sheet.setColumnWidth((short) numCol++, (short) (30 * 256));
		sheet.setColumnWidth((short) numCol++, (short) (30 * 256));
		sheet.setColumnWidth((short) numCol++, (short) (30 * 256));
		sheet.setColumnWidth((short) numCol++, (short) (30 * 256));
		sheet.setColumnWidth((short) numCol++, (short) (30 * 256));

		row = sheet.createRow(nRow);
		numCol = 0;

		ScadenzarioUtils.setCell(row, numCol++, "N° SIEP", csBoldCenter);
		ScadenzarioUtils.setCell(row, numCol++, "Cognome", csBoldCenter);
		ScadenzarioUtils.setCell(row, numCol++, "Nome", csBoldCenter);
		ScadenzarioUtils.setCell(row, numCol++, "Data Inizio Differimento", csBoldCenter);
		ScadenzarioUtils.setCell(row, numCol++, "Data Fine Differimento", csBoldCenter);
		ScadenzarioUtils.setCell(row, numCol++, "N° Giorni Residui", csBoldCenter);

		nRow++;

		Iterator itx = scadenzari.iterator();
		// inizio ciclo di scrittura dei dati
		while (itx.hasNext()) {
			numCol = 0;

			ScadenzarioModel lSca = (ScadenzarioModel) itx.next();
			FascicoloSiepModel lFas = lSca.getFascicoloModel();
			SoggettoModel lSog = lFas.getSoggetto();

			row = sheet.createRow(nRow++);
			ScadenzarioUtils.setCell(row, numCol++, lFas.getChiaveAnno() + "/" + lFas.getChiaveProgr(),
					csCenter);
			ScadenzarioUtils.setCell(row, numCol++, StringUtils.toStringJSP(lSog.getCognome()), csCenter);
			ScadenzarioUtils.setCell(row, numCol++, StringUtils.toStringJSP(lSog.getNome()), csCenter);
			ScadenzarioUtils.setCell(row, numCol++, StringUtils.toStringJSP(
					DateUtils.getDateToString(lSca.getDataInizioScadenza(), "dd/MM/yyyy")), csCenter);
			ScadenzarioUtils.setCell(row, numCol++, StringUtils.toStringJSP(
					DateUtils.getDateToString(lSca.getDataFineScadenza(), "dd/MM/yyyy")), csCenter);
			// Scaduti
			if (lSca.getGiorniResidui() != null && lSca.getGiorniResidui().intValue() < 0)
				ScadenzarioUtils.setCell(row, numCol++,
						ScadenzarioUtils.getDifferenza(DateUtils.getSysDate(), lSca.getDataFineScadenza()),
						csCenter);
			else
				// tutti altri casi
				ScadenzarioUtils.setCell(row, numCol++,
						ScadenzarioUtils.getDifferenza(lSca.getDataFineScadenza(), DateUtils.getSysDate()),
						csCenter);
		}
	}

}