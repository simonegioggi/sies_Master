package siap.siep.sanzionesostitutiva.action;

import java.math.BigDecimal;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.web.IWebConstants;
import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.model.EventoModel;
import siap.sico.evento.model.EventoNotificaModel;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.notifica.model.NotificaModel;
import siap.siep.rateizzazionepp.action.ICostantiRateizzazionePP;
import siap.siep.sanzionesostitutiva.controller.ISanzioneSostitutiva;
import siap.siep.util.SIEPLookupRemote;

public class ActModificaOrdineIngiunzione extends ActInserisciOrdineIngiunzione {
    private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

    public String processRequest() throws Exception {

        FascicoloSiepModel lFascicoloModel = (FascicoloSiepModel) getSessionAttribute("fascicolo");
        
        EventoNotificaModel lEveNot = new EventoNotificaModel();
        // Procedo alla cancellazione e quind al nuovo inserimento
        EventoModel lEve = super.getEventoOrdineIngiunzione();
        NotificaModel[] lNotifiche = super.getNotificheOrdineIngiunzione();
        
        
        BigDecimal lIdEvento = getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO);
        lEve.setIdEvento(lIdEvento);
        
        lEveNot.setEvento(lEve);
        lEveNot.setNotifiche(lNotifiche);

        // recupero le rateizzazioni da collegare all'evento
        String[] lArrayIdRate = this.getRequestStringParameters(ICostantiRateizzazionePP.CAMPO_EVE_ID_EVENTO);
        
        ISanzioneSostitutiva lCtrlSS = SIEPLookupRemote.getSanzioneSostitutivaRemote();
        lCtrlSS.exModificaOrdineIngiunzione(lEveNot, lArrayIdRate);

        String lPage = null;

        lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
                + "=siap.siep.sanzionesostitutiva.action.ActLoadDettaglioOrdineIngiunzione&"
                + ICostantiEvento.CAMPO_ID_EVENTO + "=" + lEveNot.getEvento().getIdEvento();

        return lPage;
    }
}
