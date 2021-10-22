package siap.siep.misurasicurezza.action;

import java.math.BigDecimal;

import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siep.ordineesecuzione.controller.IOrdineEsecuzione;
import siap.siep.util.SIEPLookupRemote;

/**
 * Action di cancellazione sollecito trasmissione agganciata dalla form del 
 * dettaglio dell'evento NON validato
 * 
 * @author d.fiorletta
 * @since 06/08/2014
 */
public class ActCancellaSollecitoTrasmissione extends ActionSiap
      implements ICostantiMisuraSicurezza 
{
  public String processRequest() throws Exception
  { 
    //==========================================================================
    // Dati da Cancellare:
    // - Notifiche (legate all'evento)
    // - CampoNote (eventuale) (legate all'evento)
    // - Evento
    //==========================================================================
    BigDecimal lIdEvento = getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO);
    
    EventoModel lEveModel = new EventoModel();
    
    IEvento lCtrlEvento = SICOLookupRemote.getEventoRemote();
    lEveModel = lCtrlEvento.ExRicercaEventoByKey(lIdEvento);
    
    IOrdineEsecuzione lOECtrl = SIEPLookupRemote.getOrdineEsecuzioneRemote();
    lOECtrl.ExCancellaEventoConStoreProcedure(lEveModel);
    
    String lPage = ritornoDopoCancellazione("Provvedimento Cancellato!", null);
//    lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD +
//           "=siap.siep.misurasicurezza.action.ActGestioneMisureSicurezza";
    
    return lPage ;
  }
}
