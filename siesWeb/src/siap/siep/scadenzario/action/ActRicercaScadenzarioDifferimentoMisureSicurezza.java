package siap.siep.scadenzario.action;

import java.math.BigDecimal;
import java.util.Vector;

import siap.sico.calendar.model.CalendarModel;
import siap.sico.web.ActionSiap;
import siap.siep.calcolopena.controller.CalcoloPenaController;
import siap.siep.scadenzario.controller.IScadenzario;
import siap.siep.scadenzario.model.ScadenzarioModel;
import siap.siep.util.SIEPLookupRemote;
import f3b.util.DateUtils;
import f3b.web.IWebConstants;

/**
 * MEV_39
 * <p>
 * Title: ActRicercaScadenzarioDifferimentoMisureSicurezza
 * </p>
 * <p>
 * Description: Classe Action per la ricerca di Scadenzario Differimento MS
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
public class ActRicercaScadenzarioDifferimentoMisureSicurezza extends ActionSiap implements
		ICostantiScadenzario {

	@SuppressWarnings("rawtypes")
	public String processRequest() throws Exception {

		ScadenzarioModel lScaMod = new ScadenzarioModel();
		String titolo = new String();
		titolo = "Tutti";

		String lPagina = "1";
		if (!isRequestParameterNullObj(IWebConstants.NUM_PAGE))
			lPagina = getRequestStringParameter(IWebConstants.NUM_PAGE);

		// Imposto fisso 21=Differimento MS
		lScaMod.setCodTipoScadenzario("21");
		lScaMod.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
		lScaMod.setCodStatoNotifica(null);

		// In scadenza oggi
		if (getRequestStringParameter("tipo").equals("oggi")) {
			lScaMod.setDataFineScadenza(DateUtils.getSysDateAsDate("dd/MM/yyyy"));
			titolo = "In Scadenza Oggi";
			lScaMod.setTipoRic("oggi");
		}

		// In scadenza entro
		BigDecimal lAnni = null, lMesi = null, lGiorni = null;
		if (getRequestStringParameter("tipo").equals("sette")) {
			if (!isRequestParameterNullObj(CAMPO_ANNI_SCADENZA)
					&& getRequestBigDecimalParameter(CAMPO_ANNI_SCADENZA) != null)
				lAnni = getRequestBigDecimalParameter(CAMPO_ANNI_SCADENZA);
			if (!isRequestParameterNullObj(CAMPO_MESI_SCADENZA)
					&& getRequestBigDecimalParameter(CAMPO_MESI_SCADENZA) != null)
				lMesi = getRequestBigDecimalParameter(CAMPO_MESI_SCADENZA);
			if (!isRequestParameterNullObj(CAMPO_GIORNI_SCADENZA)
					&& getRequestBigDecimalParameter(CAMPO_GIORNI_SCADENZA) != null)
				lGiorni = getRequestBigDecimalParameter(CAMPO_GIORNI_SCADENZA);
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
			lScaMod.setDataInizioScadenza(DateUtils.getSysDateAsDate("dd/MM/yyyy"));
			titolo = "In Scadenza";
		}

		// Scaduti
		if (getRequestStringParameter("tipo").equals("scaduto")) {
			lScaMod.setDataInizioScadenza(DateUtils.getSysDateAsDate("dd/MM/yyyy"));
			titolo = "Scaduti";
			lScaMod.setTipoRic("scaduto");
		}

		IScadenzario lCtrl = SIEPLookupRemote.getScadenzarioRemote();
		BigDecimal CountRisultati;
		if (isRequestParameterNullObj("CountRisultati"))
			CountRisultati = lCtrl.ExGetCountScadenzariDifferimentoMS(lScaMod);
		else
			CountRisultati = getRequestBigDecimalParameter("CountRisultati");

		setRequestAttribute("CountRisultati", CountRisultati);
		setRequestAttribute(IWebConstants.NUM_PAGE, lPagina);

		setRequestAttribute(IWebConstants.REQUEST_FOR_PAGING, getCompleteRequestURL());

		Vector lVect = lCtrl.ExRicercaScadenzarioDifferimentoMSPaged(lScaMod, Integer.parseInt(lPagina));
		setRequestAttribute("scadenzario", lVect);
		setRequestAttribute("tipo", getRequestStringParameter("tipo"));
		setRequestAttribute("titolo", titolo);
		setRequestAttribute("giorniScadenza", lGiorni != null ? lGiorni.toString() : "");
		setRequestAttribute("mesiScadenza", lMesi != null ? lMesi.toString() : "");
		setRequestAttribute("anniScadenza", lAnni != null ? lAnni.toString() : "");

		// pagina di ritorno
		return PG_RICERCASCADENZARIODIFFERIMENTO_MS;
	}

}