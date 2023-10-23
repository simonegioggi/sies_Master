package siap.siep.rateizzazionepp.action;

import java.math.BigDecimal;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.web.IWebConstants;
import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.model.EventoModel;
import siap.sico.evento.model.EventoNotificaModel;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.notifica.model.NotificaModel;
import siap.siep.sanzionesostitutiva.controller.ISanzioneSostitutiva;
import siap.siep.util.SIEPLookupRemote;

/**
 * Classe per modificare il Provvedimento Estinzione Pena Pecuniaria
 *
 * @author sgioggi
 * @since MEV_2023-33
 * @version 1.0
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class ActModificaProvvedimentoEstinzionePP extends ActInserisciProvvedimentoEstinzionePP implements ICostantiRateizzazionePP {

  private static Logger siesLogger = Logger.getLogger(LogF3B.WS_PAGO_PA_LOG);

  public String processRequest() throws Exception {

    siesLogger.info(getClass().getName() + ".processRequest: inizio");

    FascicoloSiepModel fsm = (FascicoloSiepModel) getSessionAttribute("fascicolo");
    siesLogger.debug("ID_FASCICOLO = " + fsm.getIdFascicoloSiep());
    
    EventoNotificaModel lEveNot = new EventoNotificaModel();
    EventoModel lEve = super.getEventoProvvedimentoEstinzione();
    NotificaModel[] lNotifiche = super.getNotificheProvvedimentoEstinzione();
    
    BigDecimal lIdEvento = getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO);
    lEve.setIdEvento(lIdEvento);
    
    lEveNot.setEvento(lEve);
    lEveNot.setNotifiche(lNotifiche);

    // recupero le rateizzazioni da collegare all'evento
    //String[] lArrayIdRate = this.getRequestStringParameters(ICostantiRateizzazionePP.CAMPO_EVE_ID_EVENTO);
    
    ISanzioneSostitutiva lCtrlSS = SIEPLookupRemote.getSanzioneSostitutivaRemote();
    lCtrlSS.exModificaProvvedimentoEstinzione(lEveNot);

    String lPage = null;

    lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
            + "=siap.siep.rateizzazionepp.action.ActDettaglioProvvedimentoEstinzionePP&"
            + ICostantiEvento.CAMPO_ID_EVENTO + "=" + lEveNot.getEvento().getIdEvento();

    siesLogger.info(getClass().getName() + ".processRequest: fine");

    return lPage;
  }

}