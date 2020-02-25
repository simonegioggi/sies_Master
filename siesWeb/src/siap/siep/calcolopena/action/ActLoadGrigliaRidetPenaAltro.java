package siap.siep.calcolopena.action;

import java.math.BigDecimal;

import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siep.annotazionemanuale.action.ICostantiAnnotazioneManuale;
import siap.siep.fungibilita.controller.IFungibilita;
import siap.siep.fungibilita.model.FungibilitaModel;
import siap.siep.penaresidua.controller.IPenaResidua;
import siap.siep.penaresidua.model.PenaResiduaModel;
import siap.siep.util.SIEPLookupRemote;

/**
 * Classe per la Load della Grigla dei provvedimenti collegati al computo
 * per 'Rideterminazione pena - Altro'
 *  
 * @author 
 * @since 4.0
 */

public class ActLoadGrigliaRidetPenaAltro extends ActionSiap implements ICostantiEvento, ICostantiAnnotazioneManuale
{
  /**
   *
   * @return jsp di visualizzazione del detteglio
   */
  public String processRequest() throws Exception
  {
    
    // id dell'evento di computo
    BigDecimal lIdEvento = this.getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO);
    
    //============================================
    // Ricerca evento di computo inserito
    //============================================
    IEvento lCtrlEvento = SICOLookupRemote.getEventoRemote();
    EventoModel lEveComputo = lCtrlEvento.ExRicercaEventoByKey(lIdEvento);

    this.setRequestAttribute("aEventoComputo",lEveComputo);

    //==============================================
    // Ricerca la pena residua collegata al computo
    //==============================================
    IPenaResidua lCtrlPenaRes = SIEPLookupRemote.getPenaResiduaRemote();
    PenaResiduaModel lPenRes = lCtrlPenaRes.ExRicercaPenaResiduaByIdEvento(lIdEvento);

    setRequestAttribute("penaresidua", lPenRes);
    
    //==============================================
    // Ricerca l'eventuale fungibilità
    //==============================================
    IFungibilita lFungCtrl = SIEPLookupRemote.getFungibilitaRemote();
    FungibilitaModel lFunMod = lFungCtrl.ExRicercaFungibilitaByKeyEvento(lIdEvento);

    setRequestAttribute("fungibilita", lFunMod);
    
    return PG_GRIGLIA_RIDETERMINAZIONE_PENA_ALTRO;
  }

}

