package siap.siep.calcolopena.action;

import java.math.BigDecimal;

import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.util.SICOLookupRemote;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.penaresidua.controller.IPenaResidua;
import siap.siep.penaresidua.model.PenaResiduaModel;
import siap.siep.posizione.controller.IPosizioneGiuridica;
import siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel;
import siap.siep.util.SIEPLookupRemote;
import siap.siep.web.ActSIESDettaglioProvvedimento;

/**
 * Classe Action per la load del Dettaglio della Comunicazione Nuovo Residuo
 * Pena a seguito di provvedimento di Rideterminazione Pena - Altro
 *  
 * @since4.0
 */
public class ActLoadDettComNuovoResPenaRidetPenaAltro extends ActSIESDettaglioProvvedimento implements ICostantiCalcoloPena
{
  public String processRequest() throws Exception
  {
    
    FascicoloSiepModel lFascMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");
    BigDecimal lIdFascicolo = lFascMod.getIdFascicoloSiep();

    // id dell'evento
    BigDecimal lIdEvento = this.getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO);
   
    //==========================================================================
    // Recupero i dati dell'evento e delle notifiche da visualizzare nella 
    // maschera
    //==========================================================================
    IEvento lCtrlEvento = SICOLookupRemote.getEventoRemote();
    EventoNotificaModel lEveNotMod = new EventoNotificaModel();
    lEveNotMod = lCtrlEvento.ExRicercaEventoNotificaByKey(lIdEvento);

    this.setRequestAttribute("eventonotifica",lEveNotMod);

    //==========================================================================
    // Recupero il provvedimento di computo per visualizzare i dati in maschera
    //==========================================================================
    EventoNotificaModel lEveNotComputo = new EventoNotificaModel();
    lEveNotComputo = lCtrlEvento.ExRicercaEventoNotificaByKey(lEveNotMod.getEvento().getEveIdEvento());

    this.setRequestAttribute("eventonotificacomputo",lEveNotComputo);
    
    
    //============================================
    // ricerca posizione giuridica
    //============================================
    IPosizioneGiuridica lPosCtrl = SIEPLookupRemote.getPosizioneGiuridicaRemote();
    PosizioneGiuridicaLuogoDetenzioneAltraCausaModel lPos = new PosizioneGiuridicaLuogoDetenzioneAltraCausaModel();
    lPos = lPosCtrl.ExRicercaPosizioneGiuridicaLuogoDetenzioneAltraCausaCorrentiByIdFascicolo(lIdFascicolo);

    setRequestAttribute("posizioneluogoaltra", lPos);

    //============================================
    // ricerca la pena residua
    //============================================
    IPenaResidua lCtrlPenaRes = SIEPLookupRemote.getPenaResiduaRemote();
    PenaResiduaModel lPenRes = lCtrlPenaRes.ExRicercaPenaResiduaByIdEvento(lIdEvento);

    setRequestAttribute("penaresidua", lPenRes);


    //========================================================
    // Restituisce la pagina di visualizzazione del Dettaglio
    //========================================================
    return PG_LOAD_DETT_COM_NUOVO_RES_PENA_RIDET_PENA_ALTRO;
  }
}