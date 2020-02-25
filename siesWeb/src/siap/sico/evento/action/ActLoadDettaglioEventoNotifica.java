package siap.sico.evento.action;


/**
* <p>Title: ActLoadDettaglioEvento</p>
* <p>Description: Classe Action per la load dettaglio di Evento</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.math.BigDecimal;

import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siep.ordineesecuzione.action.ICostantiOrdineEsecuzione;
import f3b.util.F3BException;

public class ActLoadDettaglioEventoNotifica extends ActionSiap implements ICostantiEvento
{

public String processRequest() throws F3BException
  {

 		 String lId = getRequestStringParameter(CAMPO_ID_EVENTO);

 		 // riempie il model
 		 EventoNotificaModel lEveMod = new EventoNotificaModel();

		 // chiama il controller

		 IEvento lCtrl = SICOLookupRemote.getEventoRemote();
		 lEveMod = lCtrl.ExRicercaEventoNotificaByKey(new BigDecimal(lId));


     setRequestAttribute("eventonotifica", lEveMod);

     //setRequestAttribute("modifica",getRequestStringParameter("modifica"));

		 return  ICostantiOrdineEsecuzione.PG_LOAD_DETTAGLIO_OE_CONDANNATO_LIBERO;
	 }
}
