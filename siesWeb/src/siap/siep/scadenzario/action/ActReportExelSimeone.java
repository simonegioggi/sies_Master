package siap.siep.scadenzario.action;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import siap.sico.calendar.model.CalendarModel;
import siap.sico.web.ActionSiap;
import siap.siep.calcolopena.controller.CalcoloPenaController;
import siap.siep.fascicolo.action.ICostantiFascicoloSiep;
import siap.siep.scadenzario.model.ScadenzarioModel;
import siap.siep.statis.controller.StatisController;
import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;

/**
 * <p>
 * Title: ActReportExelSimeone
 * </p>
 * <p>
 * Description: Classe Action per la creazione dei report ricerca scadenzario Simeone
 * </p>
 * <p>
 * Copyright: Copyright (c) 2006
 * </p>
 * <p>
 * Company: Bull
 * </p>
 * 
 * @version 1.0
 */
public class ActReportExelSimeone extends ActionSiap implements ICostantiScadenzario, ICostantiFascicoloSiep {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public String processRequest() throws F3BException {

		ScadenzarioModel lScaMod = new ScadenzarioModel();

		lScaMod.setCodTipoScadenzario("01");
		// lScaMod.setFlagVisto("N"); //***************************************
		lScaMod.setCodUfficioInserimento(this.getCodUfficioUtenteConnesso());

		String lTipo = "";
		if (!isRequestParameterNullObj("tipo")) // RICERCA SCADENZARI SIMEONE "PARTITI"
		{
			lTipo = getRequestStringParameter("tipo");
			lScaMod.setCodStatoNotifica("S");

			if (lTipo.equals("oggi")) {
				// MEV_39: MODIFICATA DATA
				lScaMod.setDataFineScadenza(DateUtils.getSysDateAsDate("dd/MM/yyyy"));
				lScaMod.setTipoRic("oggi");
			}

			if (lTipo.equals("sette")) {
				BigDecimal lAnni = getRequestBigDecimalParameter(CAMPO_ANNI_SCADENZA);
				BigDecimal lMesi = getRequestBigDecimalParameter(CAMPO_MESI_SCADENZA);
				BigDecimal lGiorni = getRequestBigDecimalParameter(CAMPO_GIORNI_SCADENZA);

				CalendarModel lCalMod = new CalendarModel();
				CalendarModel lDataInizio = new CalendarModel();

				lDataInizio.setNumAnni(new BigDecimal(DateUtils.getYearToString(DateUtils.getSysDate())));
				lDataInizio.setNumMesi(new BigDecimal(DateUtils.getMonthToString(DateUtils.getSysDate())));
				lDataInizio.setNumGiorni(new BigDecimal(DateUtils.getDayToString(DateUtils.getSysDate())));

				if (lAnni != null && lAnni.intValue() != 0) {
					lScaMod.setNumAnni(lAnni);
					lCalMod.setNumAnni(lScaMod.getNumAnni());
				}
				if (lGiorni != null && lGiorni.intValue() != 0) {
					lScaMod.setNumGiorni(lGiorni);
					lCalMod.setNumGiorni(lScaMod.getNumGiorni());
				}
				if (lMesi != null && lMesi.intValue() != 0) {
					lScaMod.setNumMesi(lMesi);
					lCalMod.setNumMesi(lScaMod.getNumMesi());
				}

				lScaMod.setTipoRic("sette");
				CalcoloPenaController lCalPen = new CalcoloPenaController();
				lScaMod.setDataFineScadenza(lCalPen.exCalcolaNuovaDataFine(lDataInizio, lCalMod, false));
				// MEV_39: MODIFICATA DATA
				lScaMod.setDataInizioScadenza(DateUtils.getSysDateAsDate("dd/MM/yyyy"));
			}

			if (lTipo.equals("scaduto")) {
				// MEV_39: MODIFICATA DATA
				lScaMod.setDataInizioScadenza(DateUtils.getSysDateAsDate("dd/MM/yyyy"));
				lScaMod.setTipoRic("scaduto");
			}
		} else { // RICERCA SCADENZARI SIMEONE NON DEFINITI
			if (!isRequestParameterNullObj(CAMPO_CHIAVE_ANNO_INIZIALE))
				lScaMod.setChiaveAnnoIniziale(getRequestBigDecimalParameter(CAMPO_CHIAVE_ANNO_INIZIALE));

			if (!isRequestParameterNullObj(CAMPO_CHIAVE_PROGR_INIZIALE))
				lScaMod.setChiaveProgrIniziale(getRequestBigDecimalParameter(CAMPO_CHIAVE_PROGR_INIZIALE));

			if (!isRequestParameterNullObj(CAMPO_CHIAVE_ANNO_FINALE))
				lScaMod.setChiaveAnnoFinale(getRequestBigDecimalParameter(CAMPO_CHIAVE_ANNO_FINALE));

			if (!isRequestParameterNullObj(CAMPO_CHIAVE_PROGR_FINALE))
				lScaMod.setChiaveProgrFinale(getRequestBigDecimalParameter(CAMPO_CHIAVE_PROGR_FINALE));

			if (!isRequestParameterNullObj("GiornoEmissioneIniziale")
					&& !isRequestParameterNullObj("MeseEmissioneIniziale")
					&& !isRequestParameterNullObj("AnnoEmissioneIniziale")) {
				lScaMod.setDataEmissioneIniziale(getRequestDateParameter("AnnoEmissioneIniziale",
						"MeseEmissioneIniziale", "GiornoEmissioneIniziale"));
			}

			if (!isRequestParameterNullObj("GiornoEmissioneFinale")
					&& !isRequestParameterNullObj("MeseEmissioneFinale")
					&& !isRequestParameterNullObj("AnnoEmissioneFinale")) {
				lScaMod.setDataEmissioneFinale(getRequestDateParameter("AnnoEmissioneFinale",
						"MeseEmissioneFinale", "GiornoEmissioneFinale"));
			}

			String[] lCodiciStatoNotifica = this.getRequestStringParameters("tipoNotifica");
			String[] lFiltro = new String[lCodiciStatoNotifica.length];

			for (int i = 0; i < lCodiciStatoNotifica.length; i++) {
				String lSelezione = lCodiciStatoNotifica[i];

				if ("Tutti".equals(lSelezione)) {
					lScaMod.setCodiciStatoNotifica(new String[] { "N", "A", "M", "I", "R", "O" });

					break;
				} else {
					if ("Condannato".equals(lSelezione)) {
						lFiltro[i] = "N";
					} else if ("Avvocato".equals(lSelezione)) {
						lFiltro[i] = "A";
					} else if ("Mancata".equals(lSelezione)) {
						lFiltro[i] = "M";
					} else if ("Sollecito".equals(lSelezione)) {
						lFiltro[i] = "O";
					} else if ("Richiesta".equals(lSelezione)) {
						lFiltro[i] = "I";
					} else if ("Rinnovazione".equals(lSelezione)) {
						lFiltro[i] = "R";
					}

					if (i == (lCodiciStatoNotifica.length - 1)) {
						lScaMod.setCodiciStatoNotifica(lFiltro);
					}
				}
			}
		}

		HSSFWorkbook wb = new HSSFWorkbook();

		// creazione del file excel (foglio dettaglio)
		StatisController lStatisCtrl = new StatisController();
		lStatisCtrl.ExCreateReportScadenzarioSimeone(lScaMod, getUfficioUtenteConnesso(), wb);

		// Generazione file xls
		ByteArrayOutputStream fileOut = new ByteArrayOutputStream();

		try {
			wb.write(fileOut);
		} catch (IOException ioe) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("IOException: " + ioe);

			throw new F3BException("siap.siep.scadenzario.action.ActReportExel.processRequest: " + ioe);
		}

		setRequestAttribute("report", fileOut);
		setRequestAttribute(IWebConstants.DISPOSITION_FIELD, IWebConstants.ATTACHMENT_DISPOSITION_FILE);

		return IWebConstants.PG_DOWNLOAD_DOCUMENT;
	}

}