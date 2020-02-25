package siap.siep.penapecuniaria.action;


import java.math.BigDecimal;

import siap.jms.ICostantiJMS;
import siap.jms.SIAPSender;
import siap.jms.messaggio.action.ICostantiMessaggio;
import siap.jms.messaggio.model.MessaggioModel;
import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoModel;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.ufficio.action.ICostantiUfficio;
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.jms.controller.IRicercaJMS;
//import siap.siep.notifica.model.NotificaModel;
import siap.siep.util.SIEPLookupRemote;
import f3b.util.DateUtils;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;


/**
 * <p>Title: ActConfermaTrasmissioneLSOrdineEsecuzione</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */
public class ActConfermaTrasmissioneConversione extends ActionSiap 
				implements ICostantiJMS
{
  public String processRequest() throws Exception
  {
    BigDecimal lEveId = getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO);

    IEvento lCtrl = SICOLookupRemote.getEventoRemote();
    EventoNotificaModel lNotEvento = lCtrl.ExRicercaEventoNotificaByKey(lEveId);

    if(lNotEvento != null && lNotEvento.getEvento()!= null &&
       !"S".equals(lNotEvento.getEvento().getFlagDocumentoRegistrato()))
    {
     EventoModel lModel = new EventoModel();
     lModel.setIdEvento(lEveId);
     lModel.setDataAggiornamento( DateUtils.getSysDate());
     lModel.setCodUfficioAggiornamento(getCodUfficioUtenteConnesso() );
     lModel.setCodOperatoreAggiornamento(getCodUtenteConnesso() );
     lModel.setFlagDocumentoRegistrato("S");

     lCtrl.ExUpdateDocument(lModel);
    }

    //Preparo la trasmissione vera e propria dell'Istanza
    FascicoloSiepModel lFascicoloModel = (FascicoloSiepModel) getSessionAttribute("fascicolo");

    /*String lTipoUff = getRequestStringParameter(ICostantiIstanza.CAMPO_COD_TIPO_UFFICIO_DESTINATARIO);
    String lSedeUff = getRequestStringParameter(ICostantiIstanza.CAMPO_COD_LUOGO_DESTINATARIO);*/

    String lCodiceUfficio = getRequestStringParameter(ICostantiUfficio.CAMPO_COD_UFFICIO);

    UfficioModel lLocal = getUfficioByCodUfficio(lCodiceUfficio);
    UfficioModel lBDI = getUfficioByCodUfficio(lLocal.getCodDistretto());

    UfficioModel lBDIMittente = getUfficioByCodUfficio(getUfficioUtenteConnesso().getCodDistretto());


//******** Esegue tutta una serie di operazioni sul DB locale **********************
    EventoModel lEveMod = new EventoModel();
    lEveMod.setIdEvento(lEveId);
    lEveMod.setDataTrasmissioneAtti(DateUtils.getSysDate());
    lEveMod.setCodUfficioDestinatario(lCodiceUfficio);
    lEveMod.setCodLuogoDestinatario(lLocal.getCodComune());


  //  NotificaModel lNot = new NotificaModel(lNotEvento.getNotifiche()[0]);

    // 26/03/2008 Rispettare la transazionalità delle fasi monolitiche!
    // ExConfermaTrasmissione modifica l'evento di SS contrassegnandolo come "TRASFERITA A uds"
    // ma potrebbe non andar bene la vera e propria spedizione!
  //  EventoNotificaModel lEveNotMod = lCtrl.ExConfermaTrasmissione(lEveMod, lNot);
    //**************************************************************************************/

    //ITrasmissioneJMS lCtrlMess = SIEPLookupRemote.getTrasmissioneJMS();
    //MessaggioModel lMessage = lCtrlMess.getMessageForProvvedimento(lEveId, lFascicoloModel.getIdFascicoloSiep());
    //ITrasmissioneJMS lCtrlMess = SIEPLookupRemote.getTrasmissioneJMS();
    //MessaggioModel lMessage = lCtrlMess.getMessageForProvvedimento(lEveId, lFascicoloModel.getIdFascicoloSiep());
    IRicercaJMS lCtrlMes = SIEPLookupRemote.getRicercaJMS();
    MessaggioModel lMessage = lCtrlMes.ExRicercaFascicoloSiepPerTrasferimento(lFascicoloModel);

    //-------->>>>>>>>>>> Inserire un meccanismo di reperimento della BDI a partire
    lMessage.setDescrBdiDestinataria(lBDI.getDescrComune());
    lMessage.setCodBdiDestinataria(lBDI.getCodUfficio());
    lMessage.setCodBdiMittente(lBDIMittente.getCodUfficio());
    lMessage.setDescrBdiMittente(lBDIMittente.getDescrComune());
    lMessage.setCodUfficioDestinatario(lCodiceUfficio);
    lMessage.setCodUfficioMittente(getCodUfficioUtenteConnesso());
    lMessage.setCodTipoMessaggio(RICHIESTA);
    
 // attenzione verificare in seguito
 //   lMessage.setCodTipoOperazione(TRASFERIMENTO_SANZIONE_SOSTITUTIVA);
    lMessage.setCodiceUtenteMittente(this.getCodUtenteConnesso());
    lMessage.setDataInvio(DateUtils.getSysDate());
    //SETTA RIFERIMENTI FASCICOLO SIEP
    lMessage.setChiaveAnnoSiep(lFascicoloModel.getChiaveAnno());
    lMessage.setChiaveProgrSiep(lFascicoloModel.getChiaveProgr());


    SIAPSender lSender = new SIAPSender();
    lSender.send(lMessage);

    // setta la risposta nella request
    setRequestAttribute(IWebConstants.MESSAGE_TEXT, "Trasmissione Provvedimento sottomessa al Sistema!");

    //Prepara la "pagina" di destinAction
    RedirectTo lRedirigi = new RedirectTo();
    lRedirigi.setPage( IWebConstants.PG_MAIN );
    lRedirigi.setAction( "siap.siep.penapecuniaria.action.ActDettaglioTrasmissioneConversione" );
    lRedirigi.setParameter( ICostantiMessaggio.CAMPO_ID_MESSAGGIO, lMessage.getIdMessaggio().toString() );
    lRedirigi.setParameter( ICostantiEvento.CAMPO_ID_EVENTO, lEveId.toString() );

    setRequestAttribute( IWebConstants.GOTO_PAGE, "" + lRedirigi );

    return IWebConstants.PG_MESSAGE;
  }
}
