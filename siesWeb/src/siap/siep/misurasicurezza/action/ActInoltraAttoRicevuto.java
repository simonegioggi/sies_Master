package siap.siep.misurasicurezza.action;

import java.math.BigDecimal;

import siap.jms.ICostantiJMS;
import siap.jms.JMSLookupRemote;
import siap.jms.SIAPReceiver;
import siap.jms.SIAPSender;
import siap.jms.config.JMSProperties;
import siap.jms.messaggio.action.ICostantiMessaggio;
import siap.jms.messaggio.controller.IMessaggio;
import siap.jms.messaggio.model.MessaggioModel;
import siap.sico.SICOException;
import siap.sico.evento.action.ICostantiEvento;
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.web.ActionSiap;
import siap.sius.SIUSException;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.util.xml.TreeModel;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;

public class ActInoltraAttoRicevuto extends ActionSiap implements ICostantiMisuraSicurezza, ICostantiJMS
{
  public String processRequest() throws F3BException, Exception
  {
    
    if (   JMSProperties.getInstance().getProperty("LISTENER_NUOVA_GESTIONE")!=null
        && JMSProperties.getInstance().getProperty("LISTENER_NUOVA_GESTIONE").trim().equalsIgnoreCase("true")
       )
    {
      SIAPReceiver.getInstance().testInArrivo();
      SIAPReceiver.getInstance().testInPartenza();
      SIAPReceiver.getInstance().testStampa();
    }
    else{
      SIAPReceiver.getInstance();
    }
    
    //=============================================
    // Recupero il messaggio originario
    //=============================================
    BigDecimal lIdMessOrig = getRequestBigDecimalParameter(ICostantiMessaggio.CAMPO_ID_MESSAGGIO);
    IMessaggio lCrtl = JMSLookupRemote.getMessaggioRemote();
    MessaggioModel lMessaggioOrig = lCrtl.ExRicercaMessaggioByKey(lIdMessOrig);
    
    
    // Controllo che l'ufficio non suia quello titolare degli atti
    // Recupero i dati dell'ufficio Competente
    //UFFICIO COMPETENZA
    String lCodiceUffDest = getCodUfficioByCodTipoUfficioDescrComune(getRequestStringParameter(ICostantiEvento.CAMPO_COD_TIPO_UFFICIO_DESTINATARIO), 
                                                                     getRequestStringParameter(ICostantiEvento.CAMPO_COD_LUOGO_DESTINATARIO));
    
    if (lCodiceUffDest.equals(getCodUfficioUtenteConnesso())){
      // Errore l'ufficio non può trasmettere gli atti a se stesso
      throw new SIUSException(SICOException.USER_MESSAGE, "Non è possibile inoltrare gli atti a se stessi.<br> Verificare di aver selezionato correttamente l'ufficio destinatario.");
    }
    
    if (lCodiceUffDest.equals(lMessaggioOrig.getCodUfficioMittente())) {
      // Se Atti ricevuti da inoltro non consento di inoltrarli indietro
      throw new SIUSException(SICOException.USER_MESSAGE, "Non è possibile inoltrare gli atti all'Ufficio Mittente.<br> Utilizzare la funzione di Restituzione.");
    }    
    
    if (lCodiceUffDest.equals(lMessaggioOrig.getChiaveUfficioSiep())) {
      // Errore l'ufficio non può trasmettere gli atti a se stesso
      throw new SIUSException(SICOException.USER_MESSAGE, "Non è possibile inoltrare gli atti all'Ufficio Titolare.<br> Utilizzare la funzione di Restituzione.");
    }
    
    
    String lNoteInoltro = null;
    if (!isRequestParameterNullObj(ICostantiJMS.NOTE)){     
      lNoteInoltro = getRequestStringParameter(ICostantiJMS.NOTE);
    }
    
    //================================
    // Inoltro il messaggio
    //================================
    this.inoltraMessaggio(lMessaggioOrig, lCodiceUffDest, lNoteInoltro);
    
    //==============================================================
    // Comunico all'ufficio titolare degli atti l'avvenuto inoltro
    //==============================================================
    String lMessComunicazioneInoltro = "";
    try {
      this.notificaTitolareAtto (lMessaggioOrig, lCodiceUffDest, lNoteInoltro);
      lMessComunicazioneInoltro = " E' stata inviata comunicazione dell'inoltro all'Ufficio titolare degli atti.";
    }
    catch (Exception e){
      lMessComunicazioneInoltro = " Non è stato possibile inviare comunicazione dell'inoltro all'Ufficio titolare degli atti.";
    }
    
    
    // setta la risposta nella request
    setRequestAttribute(IWebConstants.MESSAGE_TEXT, "Gli Atti sono stati inoltrati all'Ufficio indicato. "+lMessComunicazioneInoltro);

    //Prepara la "pagina" di destinAction
    RedirectTo lRedirigi = new RedirectTo();
    lRedirigi.setPage( IWebConstants.PG_MAIN );
    lRedirigi.setAction( "siap.siep.misurasicurezza.action.ActRicercaAttiRicevuti" );
    
    setRequestAttribute( IWebConstants.GOTO_PAGE, "" + lRedirigi );
    return IWebConstants.PG_MESSAGE;
  }
  
  /**
   * Crea il messaggio di inoltro e lo invia all'ufficio indicato
   * @param aMessaggioOrig
   * @param aCodiceUffDest
   * @param aNoteInoltro
   * @throws Exception
   */
  private void inoltraMessaggio (MessaggioModel aMessaggioOrig, String aCodiceUffDest, String aNoteInoltro) throws Exception
  {
    MessaggioModel lMessageInoltro = new MessaggioModel();
    
    // Mittente
    UfficioModel lBDI = this.getUfficioByCodUfficio(this.getCodUfficioUtenteConnesso());
    lMessageInoltro.setCodBdiMittente       (lBDI.getCodDistretto());
    lMessageInoltro.setCodUfficioMittente   (this.getCodUfficioUtenteConnesso());
    lMessageInoltro.setCodiceUtenteMittente (this.getCodUtenteConnesso());
    
    // Destinatario
    UfficioModel lUffDest = getUfficioByCodUfficio(aCodiceUffDest);
    UfficioModel lUfficioPGCAPDest = getUfficioByCodUfficio(lUffDest.getCodDistretto());
    lMessageInoltro.setDescrBdiDestinataria   (lUfficioPGCAPDest.getDescrComune());
    lMessageInoltro.setCodBdiDestinataria     (lUffDest.getCodDistretto());
    lMessageInoltro.setCodUfficioDestinatario (lUffDest.getCodUfficio()); 
    
    //Titolo
    lMessageInoltro.setChiaveAnnoSiep    (aMessaggioOrig.getChiaveAnnoSiep());
    lMessageInoltro.setChiaveProgrSiep   (aMessaggioOrig.getChiaveProgrSiep());
    lMessageInoltro.setChiaveUfficioSiep (aMessaggioOrig.getChiaveUfficioSiep());
    
    // Dati del Soggetto
    lMessageInoltro.setNomeSoggetto     (aMessaggioOrig.getNomeSoggetto());
    lMessageInoltro.setCognomeSoggetto  (aMessaggioOrig.getCognomeSoggetto());
    lMessageInoltro.setDataNascita      (aMessaggioOrig.getDataNascita());
    lMessageInoltro.setCodComuneNascita (aMessaggioOrig.getCodComuneNascita());
    lMessageInoltro.setCodStatoNascita  (aMessaggioOrig.getCodStatoNascita());
    
    // Natura del messaggio
    lMessageInoltro.setCodTipoMessaggio  (aMessaggioOrig.getCodTipoMessaggio());
    lMessageInoltro.setCodTipoOperazione (aMessaggioOrig.getCodTipoOperazione());
    lMessageInoltro.setCodEsito          ("-");
    lMessageInoltro.setDeliveryMode      (DELIVERY_MODE_INOLTRATO);
    
    lMessageInoltro.setDataInvio(DateUtils.getSysDate());
    lMessageInoltro.setDataEsito(DateUtils.getSysDate());
    
    lMessageInoltro.setTreeModel(aMessaggioOrig.getTreeModel());
    
    lMessageInoltro.setNote(aNoteInoltro);
    
    //lMessageInoltro.setJmsCorrelationIdMessage() lo scrive il Sender
    // Indico nel messaggio a chi dovrà essere indirizzata la risposta
    if (aMessaggioOrig.getCodUfficioReplyTo()!=null) {
      lMessageInoltro.setCodUfficioReplyTo     (aMessaggioOrig.getCodUfficioReplyTo());
      lMessageInoltro.setCodBdiReplyTo         (aMessaggioOrig.getCodBdiReplyTo());
      lMessageInoltro.setJmsCorrelationReplyTo (aMessaggioOrig.getJmsCorrelationReplyTo());      
    }
    else {
      lMessageInoltro.setCodUfficioReplyTo     (aMessaggioOrig.getCodUfficioMittente());
      lMessageInoltro.setCodBdiReplyTo         (aMessaggioOrig.getCodBdiMittente());
      lMessageInoltro.setJmsCorrelationReplyTo (aMessaggioOrig.getJmsCorrelationIdMessage());
    }
    
    //=========================================
    // Invio il messaggio di inoltro
    //=========================================
    SIAPSender lSender = new SIAPSender();
    lSender.send(lMessageInoltro);         

    // Marco il messaggio di !!Richiesta!! evaso.
    aMessaggioOrig.setFlagVisto("S");
    aMessaggioOrig.setCodEsito(TRASFERITO);
    aMessaggioOrig.setDataEsito(DateUtils.getSysDate());
    
    IMessaggio lCrtl = JMSLookupRemote.getMessaggioRemote();
    lCrtl.ExModificaMessaggio(aMessaggioOrig);   

  }
  
  /**
   * Notifica il titolare degli atti di aver inoltrato il mesaggio
   * @param aMessaggioOrig
   * @param aNoteInoltro
   */
  private void notificaTitolareAtto (MessaggioModel aMessaggioOrig, String aCodiceUffDest, String aNoteInoltro) throws Exception
  {
    MessaggioModel lMessageInoltro = new MessaggioModel();
      
    // Mittente
    UfficioModel lBDI = this.getUfficioByCodUfficio(this.getCodUfficioUtenteConnesso());
    lMessageInoltro.setCodBdiMittente       (lBDI.getCodDistretto());
    lMessageInoltro.setCodUfficioMittente   (this.getCodUfficioUtenteConnesso());
    lMessageInoltro.setCodiceUtenteMittente (this.getCodUtenteConnesso());
    
    // Destinatario
    UfficioModel lUfficioReply = null;
    if (aMessaggioOrig.getCodUfficioReplyTo()!=null) {
      lUfficioReply = getUfficioByCodUfficio(aMessaggioOrig.getCodUfficioReplyTo());      
      lMessageInoltro.setJmsCorrelationIdMessage(aMessaggioOrig.getJmsCorrelationReplyTo());
    }
    else {
      // Messaggio Diretto
      lUfficioReply = getUfficioByCodUfficio(aMessaggioOrig.getCodUfficioMittente());
      
      lMessageInoltro.setJmsCorrelationIdMessage(aMessaggioOrig.getJmsCorrelationIdMessage());
    }
    
    UfficioModel lUfficioPGCAPDest = getUfficioByCodUfficio(lUfficioReply.getCodDistretto());
    lMessageInoltro.setDescrBdiDestinataria   (lUfficioPGCAPDest.getDescrComune());
    lMessageInoltro.setCodBdiDestinataria     (lUfficioReply.getCodDistretto());
    lMessageInoltro.setCodUfficioDestinatario (lUfficioReply.getCodUfficio()); 
    
    // Indico nella comunicazione a quale ufficio sono stati inoltrati gli atti
    UfficioModel lUffInoltro = getUfficioByCodUfficio(aCodiceUffDest);
    lMessageInoltro.setCodUfficioInoltro (lUffInoltro.getCodUfficio());
    lMessageInoltro.setCodBdiInoltro     (lUffInoltro.getCodDistretto());
    
    //Titolo
    lMessageInoltro.setChiaveAnnoSiep    (aMessaggioOrig.getChiaveAnnoSiep());
    lMessageInoltro.setChiaveProgrSiep   (aMessaggioOrig.getChiaveProgrSiep());
    lMessageInoltro.setChiaveUfficioSiep (aMessaggioOrig.getChiaveUfficioSiep());
    
    // Dati del Soggetto
    lMessageInoltro.setNomeSoggetto     (aMessaggioOrig.getNomeSoggetto());
    lMessageInoltro.setCognomeSoggetto  (aMessaggioOrig.getCognomeSoggetto());
    lMessageInoltro.setDataNascita      (aMessaggioOrig.getDataNascita());
    lMessageInoltro.setCodComuneNascita (aMessaggioOrig.getCodComuneNascita());
    lMessageInoltro.setCodStatoNascita  (aMessaggioOrig.getCodStatoNascita());
    
    // Natura del messaggio
    lMessageInoltro.setCodTipoMessaggio  (ESITO);
    lMessageInoltro.setCodTipoOperazione (ESITO_TRASFERIMENTO_COMPETENZA_MS);
    lMessageInoltro.setCodEsito          (TRASFERITO);
    
    lMessageInoltro.setDataInvio(DateUtils.getSysDate());
    lMessageInoltro.setDataEsito(DateUtils.getSysDate());
    
    lMessageInoltro.setTreeModel(new TreeModel());
    
    lMessageInoltro.setNote(aNoteInoltro);
    
    //=========================================
    // Invio il messaggio di inoltro
    //=========================================
    SIAPSender lSender = new SIAPSender();
    lSender.send(lMessageInoltro);         

  }
}
