package siap.siep.sanzionesostitutiva.action;

import java.math.BigDecimal;

import f3b.util.F3BException;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siep.notifica.controller.INotifica;
import siap.siep.notifica.model.NotificaModel;
import siap.siep.rinnovo.action.ICostantiRinnovo;
import siap.siep.rinnovo.controller.IRinnovo;
import siap.siep.rinnovo.model.RinnovoModel;
import siap.siep.util.SIEPLookupRemote;

/**
 * Funzione di dettaglio dei Solleciti per Ordini di Ingiunzione al Pagamento
 * @author d.fiorletta
 * @since MEV_2023-33
 */
public class ActLoadDettaglioSollecitiOIPP extends ActionSiap implements ICostantiSanzioneSostitutiva {
  public String processRequest() throws F3BException
  {
    String lIdRinnovo = this.getRequestStringParameter(ICostantiRinnovo.CAMPO_ID_RINNOVO);

    RinnovoModel lRinMod = new RinnovoModel();
    IRinnovo lCtrl = SIEPLookupRemote.getRinnovoRemote();
    lRinMod = lCtrl.ExRicercaRinnovoByKey(new BigDecimal(lIdRinnovo));
    
    INotifica lCtrlNotifica = SIEPLookupRemote.getNotificaRemote();
    NotificaModel lNotifica = lCtrlNotifica.ExRicercaNotificaByKey(lRinMod.getNotIdNotifica());
    
    IEvento lCtrlEvento = SICOLookupRemote.getEventoRemote();
    EventoNotificaModel lEveNotMod = lCtrlEvento.ExRicercaEventoNotificaByKey(lNotifica.getEveIdEvento());
    setRequestAttribute("ordineIngiunzione", lEveNotMod);
    
    setRequestAttribute("sollecito",lRinMod);

    return PG_DETTAGLIO_SOLLECITI_OIPP;  //restituisce la jsp di VIEW
  }

}
