package siap.siep.scadenzario.action;

import java.math.BigDecimal;
import java.util.List;

import siap.sico.calendar.model.CalendarModel;
import siap.sico.web.ActionSiap;
import siap.siep.calcolopena.controller.CalcoloPenaController;
import siap.siep.scadenzario.controller.IScadenzario;
import siap.siep.scadenzario.model.ScadenzarioModel;
import siap.siep.util.SIEPLookupRemote;
import f3b.util.DateUtils;
import f3b.util.StringUtils;
import f3b.web.IWebConstants;

/**
 * <p>
 * Title: ActRicercaScadenzario
 * </p>
 * <p>
 * Description: Classe Action per la ricerca di Scadenzario
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: Bull
 * </p>
 * 
 * @version 1.0
 */

public class ActRicercaScadenzario extends ActionSiap implements ICostantiScadenzario {

	@SuppressWarnings("rawtypes")
	public String processRequest() throws Exception {

		ScadenzarioModel lScaMod = new ScadenzarioModel();
		String titolo = new String();
		titolo = "Tutti";

		String lPagina = "1";
		if (!isRequestParameterNullObj(IWebConstants.NUM_PAGE)) {
			lPagina = getRequestStringParameter(IWebConstants.NUM_PAGE);
		}

		// Imposto fisso 01 = Simeone,
		// N = Tutti i non visti
		// COD_STATO_NOTIFICA = S = partito il conteggio dei giorni (notificato a tutti gli interessati
		// coinvolti!)
		lScaMod.setCodTipoScadenzario("01");
		lScaMod.setFlagVisto("N");
		lScaMod.setCodStatoNotifica("S");

		lScaMod.setCodUfficioInserimento(this.getCodUfficioUtenteConnesso());

		if (getRequestStringParameter("tipo").equals("oggi")) {
			// MEV_39: MODIFICATA DATA
			lScaMod.setDataFineScadenza(DateUtils.getSysDateAsDate("dd/MM/yyyy"));
			titolo = "In Scadenza Oggi";
			lScaMod.setTipoRic("oggi");
		}

		if (getRequestStringParameter("tipo").equals("sette")) {
			// BigDecimal lAnni = getRequestBigDecimalParameter( CAMPO_ANNI_SCADENZA );
			BigDecimal lMesi = getRequestBigDecimalParameter(CAMPO_MESI_SCADENZA);
			BigDecimal lGiorni = getRequestBigDecimalParameter(CAMPO_GIORNI_SCADENZA);

			// setRequestAttribute( CAMPO_ANNI_SCADENZA, StringUtils.toStringJSP(lAnni) );
			setRequestAttribute(CAMPO_MESI_SCADENZA, StringUtils.toStringJSP(lMesi));
			setRequestAttribute(CAMPO_GIORNI_SCADENZA, StringUtils.toStringJSP(lGiorni));

			CalendarModel lCalMod = new CalendarModel();
			CalendarModel lDataInizio = new CalendarModel();

			lDataInizio.setNumAnni(new BigDecimal(DateUtils.getYearToString(DateUtils.getSysDate())));
			lDataInizio.setNumMesi(new BigDecimal(DateUtils.getMonthToString(DateUtils.getSysDate())));
			lDataInizio.setNumGiorni(new BigDecimal(DateUtils.getDayToString(DateUtils.getSysDate())));

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
			titolo = "In Scadenza";
		}

		if (getRequestStringParameter("tipo").equals("scaduto")) {
			// MEV_39: MODIFICATA DATA
			lScaMod.setDataInizioScadenza(DateUtils.getSysDateAsDate("dd/MM/yyyy"));
			titolo = "Scaduti";
			lScaMod.setTipoRic("scaduto");
		}

		IScadenzario lCtrl = SIEPLookupRemote.getScadenzarioRemote();
		BigDecimal CountRisultati;
		if (isRequestParameterNullObj("CountRisultati")) {
			CountRisultati = lCtrl.ExGetCountScadenzarioSimeone(lScaMod);
		} else {
			CountRisultati = getRequestBigDecimalParameter("CountRisultati");
		}

		setRequestAttribute("CountRisultati", CountRisultati);
		setRequestAttribute(IWebConstants.NUM_PAGE, lPagina);

		setRequestAttribute(IWebConstants.REQUEST_FOR_PAGING, getCompleteRequestURL());

		List lList = lCtrl.ExRicercaScadenzarioSimeonePaged(lScaMod, Integer.parseInt(lPagina));

		setRequestAttribute("scadenzario", lList);
		setRequestAttribute("tipo", getRequestStringParameter("tipo"));
		setRequestAttribute("titolo", titolo);

		return PG_RICERCASCADENZARIO;
	}

}