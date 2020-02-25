package siap.sius.presaincarico.action;

import java.math.BigDecimal;

import siap.jms.ICostantiJMS;
import siap.jms.JMSLookupRemote;
import siap.jms.SIAPReceiver;
import siap.jms.SIAPSender;
import siap.jms.config.JMSProperties;
import siap.jms.messaggio.action.ICostantiMessaggio;
import siap.jms.messaggio.controller.IMessaggio;
import siap.jms.messaggio.model.MessaggioModel;
import siap.sico.web.ActionSiap;
import siap.siep.fascicolo.action.ICostantiFascicoloSiep;
import siap.siep.fascicolo.controller.IFascicoloSiep;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.richiesta.controller.IRichiesta;
import siap.siep.util.SIEPLookupRemote;
import f3b.util.DateUtils;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;

/**
 * <p>Title: ActConfermaPresaInCaricoAttiSiepStessaBDI</p>
 * Action di presa in carico Atti ricevuti per competenza da stessa BDI.
 * La action non effettua alcuna presa in carico ma si limita a rispondere e 
 * marcare il messaggio locale come preso in carico (FLAG_VISTO="S")
 * 
 * n.b. è una funzione SIEP sebbene questa classe si trovi nel doinio SIUS
 *
 * FIXME DA SPOSTARE SOTTO SIEP (siap.siep.presaincarico o istruttoria cumulo)
 * 
 * @deprecated sostituita dalla siap.siep.presaincarico.action.ActConfermaPresaInCaricoAttiSiep
 * 
 * @version 1.0
 */
public class ActConfermaPresaInCaricoAttiSiepStessaBDI extends ActionSiap implements ICostantiJMS
{
  public String processRequest() throws Exception
  {
    if (   JMSProperties.getInstance().getProperty("LISTENER_NUOVA_GESTIONE")!=null
        && JMSProperties.getInstance().getProperty("LISTENER_NUOVA_GESTIONE").trim().equalsIgnoreCase("true")
          )
    {
      SIAPReceiver.getInstance().testInArrivo();
      SIAPReceiver.getInstance().testInPartenza();
      SIAPReceiver.getInstance().testStampa();
    }
    else
    {
      SIAPReceiver.getInstance();
    }
    
    //eseguo le operazioni per il messaggio di risposta (esito della trasmissione)
    BigDecimal lIdMess = getRequestBigDecimalParameter(ICostantiMessaggio.CAMPO_ID_MESSAGGIO);

    IMessaggio lCrtl = JMSLookupRemote.getMessaggioRemote();
    MessaggioModel lMess = lCrtl.ExRicercaMessaggioByKey(lIdMess);
    
    
    //=================================
    // Crea il messaggio di risposta
    //=================================
    MessaggioModel lMessage = new MessaggioModel();

    lMessage.setDescrBdiDestinataria   (lMess.getDescrBdiMittente());
    lMessage.setCodBdiDestinataria     (lMess.getCodBdiMittente());
    lMessage.setCodUfficioDestinatario (lMess.getCodUfficioMittente());
    
    lMessage.setCodBdiMittente       (this.getCodDistrettoUtenteConnesso());
    lMessage.setCodUfficioMittente   (this.getCodUfficioUtenteConnesso());
    lMessage.setCodiceUtenteMittente (this.getCodUtenteConnesso());
    
    lMessage.setCodTipoMessaggio  (ESITO);
    lMessage.setCodTipoOperazione (ESITO_TRASFERIMENTO_COMPETENZA);
    lMessage.setCodEsito          (PRESAINCARICO);    
    
    lMessage.setChiaveAnnoSiep    (lMess.getChiaveAnnoSiep());
    lMessage.setChiaveProgrSiep   (lMess.getChiaveProgrSiep());
    lMessage.setChiaveUfficioSiep (lMess.getChiaveUfficioSiep());
    
   
    lMessage.setChiaveAnnoFasCumulante  (lMess.getChiaveAnnoFasCumulante());  // SIEP Trasmissione Competenza
    lMessage.setChiaveProgrFasCumulante (lMess.getChiaveProgrFasCumulante());  // SIEP Trasmissione Competenza

    
    lMessage.setJmsCorrelationIdMessage (lMess.getJmsCorrelationIdMessage());
    
    lMessage.setDataInvio (lMess.getDataInvio());
    lMessage.setDataEsito (DateUtils.getSysDate());    
    lMessage.setTreeModel (lMess.getTreeModel()); // SERVE???
    
    //==========================================================================
    // Invio il messaggio di risposta
    //==========================================================================
    SIAPSender lSender = new SIAPSender();
    lSender.send(lMessage);

    //==========================================================================
    // Marco il messaggio di richiesta evaso.
    //==========================================================================
    lMess.setFlagVisto("S");
    lMess.setDataEsito(DateUtils.getSysDate());
    lCrtl.ExModificaMessaggio(lMess);
   
    
    //==========================================================================
    // Aggiorno lo stato Procedimento del Fascicolo ricevuto e preso in carico
    // 
    //==========================================================================
    BigDecimal aIdFas = getRequestBigDecimalParameter(ICostantiFascicoloSiep.CAMPO_ID_FASCICOLO_SIEP);
    IFascicoloSiep lCtrl = SIEPLookupRemote.getFascicoloSiepRemote();
    FascicoloSiepModel aFascicolo = lCtrl.ExRicercaFascicoloByKey(aIdFas);
  
    if(aFascicolo !=null){
      IRichiesta lRichCtrl = SIEPLookupRemote.getRichiestaRemote();     
      lRichCtrl.ExUpdStatoProcPresaincaricoStessaBDI (aFascicolo, DateUtils.getSysDate(), 
                                              DateUtils.getSysDate(), this.getCodUtenteConnesso(), getCodUfficioUtenteConnesso());
    }
    
    // setta la risposta nella request
    setRequestAttribute(IWebConstants.MESSAGE_TEXT, "Ricezione Atto Completata e Esito rispedito al Mittente. Il fascicolo è stato preso in carico!");
    
    RedirectTo lRedirigi = new RedirectTo();
    lRedirigi.setPage( IWebConstants.PG_MAIN );
    lRedirigi.setAction( "siap.siep.richiesta.action.ActLoadRicercaAttiCompetenzaRicevuti" );
    
    setRequestAttribute( IWebConstants.GOTO_PAGE, "" + lRedirigi );
    return IWebConstants.PG_MESSAGE;

  }
}
