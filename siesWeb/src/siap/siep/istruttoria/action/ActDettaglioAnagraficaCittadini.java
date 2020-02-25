package siap.siep.istruttoria.action;

import java.math.BigDecimal;

import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.util.SICOLookupRemote;
import siap.siep.web.ActSIESDettaglioProvvedimento;

/**
* <p>Title: ActDettaglioAnagraficaCittadini</p>
* <p>Description: Classe Action per la load dettaglio Richiesta Anagrafica Cittadini</p>
* <p>Copyright: Copyright (c) 2007</p>
* <p>Company: Eutelia</p>
* @version 1.0
*/

public class ActDettaglioAnagraficaCittadini extends ActSIESDettaglioProvvedimento
{

 public String processRequest() throws Exception
   {
      String lId = getRequestStringParameter(ICostantiEvento.CAMPO_ID_EVENTO);

      // riempie il model
      EventoNotificaModel lEveMod = new EventoNotificaModel();

     // chiama il controller
     IEvento lCtrl = SICOLookupRemote.getEventoRemote();
     lEveMod = lCtrl.ExRicercaEventoNotificaByKey(new BigDecimal(lId));

     setRequestAttribute("eventonotifica", lEveMod);

     return  ICostantiIstruttoria.PG_DETTAGLIO_ANAGRAFICA_CITTADINI;
   }
}
