package siap.sico.evento.action;

/**
* <p>Title: ActLoadDettaglioEvento</p>
* <p>Description: Classe Action per la load dettaglio di Evento</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.math.BigDecimal;
import java.util.Vector;

import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siep.ordineesecuzione.action.ICostantiOrdineEsecuzione;
import f3b.util.F3BException;

@SuppressWarnings("rawtypes")
public class ActLoadDettaglioEvento extends ActionSiap implements ICostantiEvento {

	public String processRequest() throws F3BException {

		String lId = getRequestStringParameter(CAMPO_ID_EVENTO);

		// riempie il model
		EventoModel lEveMod = new EventoModel();
		lEveMod.setIdEvento(new BigDecimal(lId));
		// chiama il controller

		IEvento lCtrl = SICOLookupRemote.getEventoRemote();
		Vector lVect = lCtrl.ExRicercaEvento(lEveMod);
		lEveMod = new EventoModel((EventoModel) lVect.firstElement());
		setRequestAttribute("evento", lEveMod);

		return ICostantiOrdineEsecuzione.PG_LOAD_DETTAGLIO_OE_CONDANNATO_LIBERO;
	}

}