package siap.siep.sanzionesostitutiva.action;

import java.math.BigDecimal;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.F3BException;
import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.magistrato.model.MagistratoModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siep.notifica.model.NotificaModel;
import siap.siep.posizione.controller.IPosizioneGiuridica;
import siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel;
import siap.siep.rateizzazionepp.controller.IRateizzazionePP;
import siap.siep.rateizzazionepp.model.RateizzazionePPModel;
import siap.siep.util.SIEPLookupRemote;

/**
 * Action per il caricamento della nota di trasmissione
 * 
 * @author d.fiorletta
 * @since MEV_2023-33
 */
public class ActLoadDettaglioNotaTrasmissione extends ActionSiap implements ICostantiSanzioneSostitutiva {

  private static Logger siesLogger = Logger.getLogger(LogF3B.WS_PAGO_PA_LOG);

  @SuppressWarnings({ "rawtypes", "unchecked" })
  public String processRequest() throws F3BException {
    siesLogger.info(getClass().getName() + ".processRequest: inizio");

    BigDecimal lIdEvento = getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO);

    IEvento lCtrlEvento = SICOLookupRemote.getEventoRemote();
    EventoNotificaModel lEveNotMod = lCtrlEvento.ExRicercaEventoNotificaByKey(lIdEvento);
    setRequestAttribute("notaTrasmissione", lEveNotMod);

    // Recupero l'OI da visualizzare puntato dalla nota di trasmissione e relative rate/bollettini
    {
      EventoNotificaModel lEveNotModOI = lCtrlEvento.ExRicercaEventoNotificaByKey(lEveNotMod.getEvento().getEveIdEvento());
      setRequestAttribute("ordineIngiunzione", lEveNotModOI);

      // Recupero le rate
      IRateizzazionePP irpp = SIEPLookupRemote.getRateizzazionePPRemote();
      Vector<RateizzazionePPModel> listaRate = irpp.exRicercaRateizzazioniBollettiniByIdEvento(lEveNotMod.getEvento().getEveIdEvento());
      setRequestAttribute("listaRateizzazioni", listaRate);
    }
    
    // NOTIFICA per l'esecuzione
    NotificaModel[] lNotifiche = lEveNotMod.getNotifiche();
    for (int i = 0; i < lNotifiche.length; i++) {
      if ("E".equals(lNotifiche[i].getCodTipoNotifica())) {
        setRequestAttribute("notificaAlCondannato", lNotifiche[i]);
      }
    }
    
    // MAGISTRATO
    MagistratoModel lMag = lEveNotMod.getMagistrato();
    setRequestAttribute("magistrato", lMag);
    
    
    PosizioneGiuridicaLuogoDetenzioneAltraCausaModel lPos = new PosizioneGiuridicaLuogoDetenzioneAltraCausaModel();
    IPosizioneGiuridica lPosCtrl = SIEPLookupRemote.getPosizioneGiuridicaRemote();
    lPos = lPosCtrl.ExRicercaPosizioneGiuridicaLuogoDetenzioneAltraCausaCorrentiByIdFascicolo(
        lEveNotMod.getEvento().getFasSieIdFascicoloSiep());
    setRequestAttribute("posizioneluogoaltra", lPos);

    siesLogger.info(getClass().getName() + ".processRequest: fine");

    return ICostantiSanzioneSostitutiva.PG_DETTAGLIO_NOTA_TRASNISSIONE;
  }

}
