package siap.siep.cumulo.action;

import java.math.BigDecimal;

import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.controller.IEventoSimeone;
import siap.sico.evento.model.EventoModel;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.util.SICOLookupRemote;
import siap.siep.cumulo.model.CumuloModel;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.notifica.action.ICostantiNotifica;
import siap.siep.notifica.model.NotificaModel;
import f3b.util.F3BException;
import f3b.web.IWebConstants;

/**
* <p>Title: ActInserisciCumulo</p>
* <p>Description: Classe Action per l'inserimento di Cumulo</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/
public class ActInserisciStampaCumulo extends ActCumulo implements ICostantiCumulo
{
/**
* Azione di Inserimento del Cumulo
* @return Nome della pagina JSP da visualizzare
* al termine dell'elaborazione
* @throws F3BException
*/
public String processRequest() throws F3BException
{
    BigDecimal lFascID =((FascicoloSiepModel)(getSessionAttribute("fascicolo"))).getIdFascicoloSiep();

    FascicoloSiepModel lFascMod = ((FascicoloSiepModel)(getSessionAttribute("fascicolo")));

    EventoNotificaModel lEveNotMod = new EventoNotificaModel();
    EventoModel lEventoModel = null;

    String lTipologia = this.getRequestStringParameter(ICostantiCumulo.CAMPO_FLAG_TIPO_STAMPA);
    CumuloModel lCumMod = new CumuloModel();
    lCumMod.setFlagTipoStampa(this.getRequestStringParameter(ICostantiCumulo.CAMPO_FLAG_TIPO_STAMPA));
    lCumMod.setFasSieIdFascicoloSiep(lFascID);

    IEvento lCtrlEvento = SICOLookupRemote.getEventoRemote();
    lEventoModel = lCtrlEvento.ExRicercaEventoByKey(this.getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO));
    
    //setto l'evento
    //lEveNotMod.getEvento().setCodTipoEvento("01");
    //lEveNotMod.getEvento().setCodTipoProvvedimento("04");

    //if(lTipologia != null && lTipologia.equals("0"))
    //{
    //  lEveNotMod.getEvento().setCodMotivo("0222");
    //}
    //else if(lTipologia != null && lTipologia.equals("1"))
    //{
    //  lEveNotMod.getEvento().setCodMotivo("0223");
    //}
    //else if(lTipologia != null && lTipologia.equals("2"))
    //{
    //  lEveNotMod.getEvento().setCodMotivo("0224");
    //}
    //else
    //{
    //  lEveNotMod.getEvento().setCodMotivo("0277");
    //}

    lEventoModel.setDataEmissione(getRequestDateParameter(ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE, ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE, ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE));
    lEventoModel.setDataTrasmissioneAtti(getRequestDateParameter(ICostantiNotifica.CAMPO_ANNO_DATA_INVIO, ICostantiNotifica.CAMPO_MESE_DATA_INVIO, ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO));
    lEventoModel.setCodUfficioEmittente(this.getCodUfficioUtenteConnesso());
    lEventoModel.setCodLuogoEmittente(this.getCodComuneUtenteConnesso());
    lEventoModel.setCodEsito("-");
    lEventoModel.setFlagStampaSiep("S");
    lEventoModel.setFlagVideoSiep("S");
    lEventoModel.setCodUfficioDestinatario("-");
    lEventoModel.setCodLuogoDestinatario("-");
    lEventoModel.setCodTipoUfficioDestinatario("-");
    //lEveNotMod.getEvento().setFasSieIdFascicoloSiep(lFascID);
    lEventoModel.setCodMagistrato(this.calcolaMagistrato());
    lEventoModel.setCodOperatoreAggiornamento(this.getCodUtenteConnesso());
    lEventoModel.setCodUfficioAggiornamento(this.getCodUfficioUtenteConnesso());
    lEveNotMod.setEvento(lEventoModel);
    
//setto le notifiche
    NotificaModel[] lNotifiche = this.setNotificheCumuloStampa(lTipologia);
    lEveNotMod.setNotifiche(lNotifiche);

//Inserisce evento notifica
    IEventoSimeone lCtrlEventoSime = SICOLookupRemote.getEventoSimeoneRemote();
    EventoNotificaModel lEveNot = new EventoNotificaModel();
    lEveNot = lCtrlEventoSime.ExInseriscioModificaEventoNotificaCumulo(lFascMod, lEveNotMod,lCumMod);

//Pagina di dettaglio
    String lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD + "=siap.siep.cumulo.action.ActDettaglioCumuloStampa&" +
                   ICostantiEvento.CAMPO_ID_EVENTO + "=" +
                   lEveNot.getEvento().getIdEvento();

    return lPage;
  }
}