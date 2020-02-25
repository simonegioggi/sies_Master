package siap.siep.calcolopena.action;

/**
 * <p>Title: ActInserisciComputoCustodiaCautelare</p>
 * <p>Description: Classe Action per l'inserimento di Computo</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: Bull</p>
 * @version 1.0
 */

import java.math.BigDecimal;

import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoModel;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.magistrato.action.ICostantiMagistrato;
import siap.sico.util.SICOLookupRemote;
import siap.siep.notifica.model.NotificaModel;
import siap.siep.richiesta.controller.IRichiesta;
import siap.siep.util.SIEPLookupRemote;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;

public class ActInserisciComputoCustodiaCautelare
       extends ActCalcoloPena
{
  /**
   * Azione di Inserimento/Aggiornamento del provvedimento di Computo Custodia
   * Cautelare Stesso Reato/Altro Reato 
   * Il parametro letto dalla form flagPage indica il tipo di computo:
   * flagPage: D = Stesso Titolo (Presofferto)
   * flagPage: A = Altro Titolo (Fungibilità altro reato Misura Cautelare)
   * 
   * @return Stringa di chiamata alla Action di Dettaglio
   * @throws F3BException
   */
  public String processRequest() throws F3BException
  {
    String lCodiceOperatore = this.getCodUtenteConnesso();
    String lCodiceUfficio   = this.getCodUfficioUtenteConnesso();
    
    String codPosizioneGiu=this.getRequestStringParameter("codPosizioneGiu");
    this.setRequestAttribute("codPosizioneGiu",codPosizioneGiu);

    //==========================================================================
    // Recupero l'evento di Computo. Tale evento viene aggiornato con i dati in 
    // form e i destinatari specificati
    //==========================================================================
    BigDecimal lIdEvento = this.getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO);
    EventoModel lEve = new EventoModel();
    IEvento lCtrlEvento = SICOLookupRemote.getEventoRemote();
    lEve = lCtrlEvento.ExRicercaEventoByKey(lIdEvento);


    //==========================================================================
    // Completo i dati dell'evento recuperandoli dalla form
    //==========================================================================
    EventoNotificaModel lEveMod = new EventoNotificaModel();
    lEve.setCodUfficioEmittente(lCodiceUfficio);

    lEve.setCodLuogoEmittente(this.getCodComuneUtenteConnesso());
    lEve.setDataEmissione(getRequestDateParameter(ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE, 
                                                  ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE,
                                                  ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE));
    lEve.setDataTrasmissioneAtti(getRequestDateParameter(ICostantiEvento.CAMPO_ANNO_DATA_TRASMISSIONE_ATTI, 
                                                         ICostantiEvento.CAMPO_MESE_DATA_TRASMISSIONE_ATTI,
                                                         ICostantiEvento.CAMPO_GIORNO_DATA_TRASMISSIONE_ATTI));

    lEve.setCodMagistrato(getRequestStringParameter(ICostantiMagistrato.CAMPO_COD_MAGISTRATO));
    
    lEve.setCodOperatoreAggiornamento (lCodiceOperatore);
    lEve.setDataAggiornamento         (DateUtils.getSysDate());
    lEve.setCodUfficioAggiornamento   (lCodiceUfficio);

    lEve.setFlagStampaSiep("S");
    lEve.setFlagVideoSiep("S");
    //---NO ---lEve.setFlagDocumentoRegistrato("N");

    lEveMod.setEvento(lEve);

    //==========================================================================
    // Aggiungo le notifiche
    //==========================================================================
    NotificaModel[] lNotifiche = this.setNotificheAnnotazioniManuali();
    lEveMod.setNotifiche(lNotifiche);

    //==========================================================================
    // Inserisco/Aggiorno l'evento e le notifiche
    //==========================================================================
    EventoNotificaModel lRetModel = new EventoNotificaModel();
    
    IRichiesta lCtrlRich = SIEPLookupRemote.getRichiestaRemote();
    lRetModel = lCtrlRich.ExInserisciOModificaNotifica(lEveMod);

    //
    String lPage = null;
    lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD + "=siap.siep.calcolopena.action.ActDettaglioComputoCustodiaCautelare&flagPage="+this.getRequestStringParameter("flagPage")+"&"+ICostantiEvento.CAMPO_ID_EVENTO + "=" +lRetModel.getEvento().getIdEvento();

    return lPage;
  }

}