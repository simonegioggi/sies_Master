package siap.siep.ordineesecuzione.action;

import java.math.BigDecimal;

import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import f3b.util.F3BException;


/**
* <p>Title: Act Dettaglio Scadenzario Evento</p>
* <p>Description: Classe Action per la load dettaglio di Scadenzario</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

public class ActDettaglioScadenzarioEvento extends ActionSiap implements ICostantiOrdineEsecuzione
{
public String processRequest() throws F3BException {

          String lId = getRequestStringParameter(ICostantiEvento.CAMPO_ID_EVENTO);

          EventoNotificaModel lEveMod = new EventoNotificaModel();

         // chiama il controller

         IEvento lCtrl = SICOLookupRemote.getEventoRemote();
         lEveMod = lCtrl.ExRicercaEventoNotificaByKey(new BigDecimal(lId));

         setRequestAttribute("eventonotifica", lEveMod);

		 return PG_DETTAGLIO_SCADENZARIO_EVENTO;
	 }
}