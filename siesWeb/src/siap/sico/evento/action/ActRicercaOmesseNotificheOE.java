package siap.sico.evento.action;

/**
 * <p>Title: ActRicercaProvvedimentiNonValidati</p>
 * <p>Description: Classe Action per la ricerca di Evento</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: Bull</p>
 * @version 1.0
 */

import java.math.BigDecimal;
import java.util.List;

import siap.sico.evento.controller.IEventoSimeone;
import siap.sico.evento.model.EventoModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.web.ISIAPCostantiWeb;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;

@SuppressWarnings("rawtypes")
public class ActRicercaOmesseNotificheOE extends ActionSiap implements ICostantiEvento {

	public String processRequest() throws Exception {

		String lPagina = "1";
		if (!isRequestParameterNullObj(IWebConstants.NUM_PAGE)) {
			lPagina = getRequestStringParameter(IWebConstants.NUM_PAGE);
		}

		IEventoSimeone lCtrl = SICOLookupRemote.getEventoSimeoneRemote();
		EventoModel lEveModel = new EventoModel();

		lEveModel.setCodUfficioInserimento(this.getCodUfficioUtenteConnesso());
		List lFascicoliEventi = lCtrl.ExRicercaProvvedimentiPerOmesseNotificheOnViewPaged(lEveModel,
				getCodUfficioUtenteConnesso(), Integer.parseInt(lPagina));

		if (lFascicoliEventi.isEmpty()) {
			RedirectTo lRedirigi = new RedirectTo();

			lRedirigi.setPage(IWebConstants.PG_MAIN);
			setRequestAttribute(IWebConstants.MESSAGE_TEXT, "Non Esistono Omesse Notifiche con OE Simeone");
			lRedirigi.setAction("siap.siep.fascicolo.action.ActGrigliaRicerca");
			setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi);

			return IWebConstants.PG_MESSAGE;
		} else {
			BigDecimal CountRisultati;
			if (isRequestParameterNullObj("CountRisultati")) {
				CountRisultati = lCtrl.ExGetCountProvvedimentiPerOmesseNotifiche(lEveModel,
						getCodUfficioUtenteConnesso());
			} else {
				CountRisultati = getRequestBigDecimalParameter("CountRisultati");
			}

			setRequestAttribute("fascicoliEventi", lFascicoliEventi);

			if (!isRequestParameterNullObj(IWebConstants.NUM_PAGE))
				lPagina = getRequestStringParameter(IWebConstants.NUM_PAGE);

			setRequestAttribute("CountRisultati", CountRisultati);
			String lAzione = "siap.sico.evento.action.ActRicercaOmesseNotificheOE";
			setRequestAttribute(ISIAPCostantiWeb.CAMPO_AZIONE_CHIAMANTE, lAzione);

			setRequestAttribute(IWebConstants.NUM_PAGE, lPagina);
			setRequestAttribute(IWebConstants.REQUEST_FOR_PAGING, getCompleteRequestURL());
		}

		return PG_RICERCA_EVENTI_LS;
	}

}