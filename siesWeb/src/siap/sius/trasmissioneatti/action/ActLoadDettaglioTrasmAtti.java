package siap.sius.trasmissioneatti.action;

import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoNotificaModel;
//import siap.sius.util.SIUSLookupRemote;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;

public class ActLoadDettaglioTrasmAtti extends ActionSiap implements ICostantiTrasmissioneAtti {

	public String processRequest() throws Exception {

		// Recupero il Fascicolo SIUS dalla sessione
		// FascicoloGPModel lFasGPMod = (FascicoloGPModel)getSessionAttribute("fascicoloSiusGP");

		// Carico il dettaglio dell'evento di Trasm. Atti.
		EventoNotificaModel lEveNotMod = new EventoNotificaModel();
		lEveNotMod.getEvento().setIdEvento(getRequestBigDecimalParameter(CAMPO_ID_EVENTO));
		IEvento lCtrl = SICOLookupRemote.getEventoRemote();
		lEveNotMod = lCtrl
				.ExRicercaEventoNotificaByKeyForTrasmAtti(getRequestBigDecimalParameter(CAMPO_ID_EVENTO));

		// Metto in sessione l'eveto di Trasmissione atti per consentire le funzionalità annesse.
		setRequestAttribute("eventoNotTA", lEveNotMod);
		setSessionAttribute("eventoNotTA", lEveNotMod);

		return PG_DETTAGLIOTRASMISSIONEATTI;
	}

}