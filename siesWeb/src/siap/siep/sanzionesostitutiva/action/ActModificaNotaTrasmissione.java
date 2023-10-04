package siap.siep.sanzionesostitutiva.action;

import java.math.BigDecimal;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.web.IWebConstants;
import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoModel;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.util.SICOLookupRemote;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.notifica.model.NotificaModel;
import siap.siep.sanzionesostitutiva.controller.ISanzioneSostitutiva;
import siap.siep.util.SIEPLookupRemote;

/**
 * Classe per modificare la nota di trasmissione
 * 
 * @author d.fiorletta
 * @since MEV_2023-33
 * @version 1.0
 */
public class ActModificaNotaTrasmissione extends ActInserisciNotaTrasmissione {

  private static Logger siesLogger = Logger.getLogger(LogF3B.WS_PAGO_PA_LOG);

  public String processRequest() throws Exception {
      
    siesLogger.info(getClass().getName() + ".processRequest: inizio");

    FascicoloSiepModel fsm = (FascicoloSiepModel) getSessionAttribute("fascicolo");
    
    EventoNotificaModel lEveNot = new EventoNotificaModel();
    // Procedo alla cancellazione e quind al nuovo inserimento
    EventoModel lEve = super.getEventoNotaTrasmissione();
    NotificaModel[] lNotifiche = super.getNotificheNotaTrasmissione();
    
    BigDecimal lIdNota = getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO);
    IEvento lCtrlEvento = SICOLookupRemote.getEventoRemote();
    EventoNotificaModel lEveNotMod = lCtrlEvento.ExRicercaEventoNotificaByKey(lIdNota);
    
    lEve.setIdEvento(lIdNota); // serve per la cancellazione
    lEve.setEveIdEvento(lEveNotMod.getEvento().getEveIdEvento()); // devo agganciare lo stesso OI
    lEveNot.setEvento(lEve);
    lEveNot.setNotifiche(lNotifiche);
    
    ISanzioneSostitutiva lCtrlSS = SIEPLookupRemote.getSanzioneSostitutivaRemote();
    EventoNotificaModel lRetModel = lCtrlSS.exModificaNotaTrasmissione(lEveNot);

    String lPage = null;

    lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
      + "=siap.siep.sanzionesostitutiva.action.ActLoadDettaglioNotaTrasmissione&"
      + ICostantiEvento.CAMPO_ID_EVENTO + "=" + lRetModel.getEvento().getIdEvento();

    siesLogger.info(getClass().getName() + ".processRequest: fine");

    return lPage;
  }

}
