package siap.sius.presaincarico.action;

import java.math.BigDecimal;

import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.xml.TreeModel;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;

import siap.jms.ICostantiJMS;
import siap.jms.JMSLookupRemote;
import siap.jms.SIAPReceiver;
import siap.jms.SIAPSender;
import siap.jms.config.JMSProperties;
import siap.jms.messaggio.action.ICostantiMessaggio;
import siap.jms.messaggio.controller.IMessaggio;
import siap.jms.messaggio.model.MessaggioModel;
import siap.sico.web.ActionSiap;

import org.apache.log4j.Logger;

/**
 * <p>Title: ActRestituzioneCompetenza</p>
 * <p>Description: Restituzione degli atti a seguito della Trasmissione per Competenza</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class ActRestituzioneCompetenza extends ActionSiap implements ICostantiJMS
{
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
	
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
    else{
      SIAPReceiver.getInstance();
    }

    //eseguo le operazioni per il messaggio di risposta (esito della trasmissione)
    BigDecimal lIdMess = getRequestBigDecimalParameter(ICostantiMessaggio.CAMPO_ID_MESSAGGIO);

    IMessaggio lCrtl = JMSLookupRemote.getMessaggioRemote();
    MessaggioModel lMess = lCrtl.ExRicercaMessaggioByKey(lIdMess);

    siesLogger.debug(" Messaggio 00066 id = "+lMess.getIdMessaggio());
  
    //===============================================
    //
    //===============================================
    MessaggioModel lMessage = new MessaggioModel();
    
    lMessage.setNote (getRequestStringParameter("MotivoRestituzione"));
    
    lMessage.setDescrBdiDestinataria   (lMess.getDescrBdiMittente());
    lMessage.setCodBdiDestinataria     (lMess.getCodBdiMittente());
    lMessage.setCodUfficioDestinatario (lMess.getCodUfficioMittente());
    
    lMessage.setCodBdiMittente       (this.getCodDistrettoUtenteConnesso());
    lMessage.setCodUfficioMittente   (this.getCodUfficioUtenteConnesso());
    lMessage.setCodiceUtenteMittente (this.getCodUtenteConnesso());
    
    lMessage.setCodTipoMessaggio  (ESITO);
    lMessage.setCodTipoOperazione (ESITO_TRASFERIMENTO_COMPETENZA);
  //  lMessage.setCodEsito          (RESTITUITO);
    lMessage.setCodEsito          (RIGETTATO);

    lMessage.setJmsCorrelationIdMessage(lMess.getJmsCorrelationIdMessage());
    
    lMessage.setDataInvio(lMess.getDataInvio());
    lMessage.setDataEsito(DateUtils.getSysDate());
    
    //dati soggetto
    lMessage.setNomeSoggetto      (lMess.getNomeSoggetto());
    lMessage.setCognomeSoggetto   (lMess.getCognomeSoggetto());
    lMessage.setDataNascita       (lMess.getDataNascita());
    lMessage.setCodComuneNascita  (lMess.getCodComuneNascita());
    lMessage.setCodStatoNascita   (lMess.getCodStatoNascita());   
    
    //
    lMessage.setChiaveAnnoSiep    (lMess.getChiaveAnnoSiep());
    lMessage.setChiaveProgrSiep   (lMess.getChiaveProgrSiep());
    lMessage.setChiaveUfficioSiep (lMess.getChiaveUfficioSiep());
    
    lMessage.setChiaveAnnoFasCumulante    (lMess.getChiaveAnnoFasCumulante());
    lMessage.setChiaveProgrFasCumulante   (lMess.getChiaveProgrFasCumulante());
    lMessage.setChiaveUfficioFasCumulante (lMess.getChiaveUfficioFasCumulante());
  
    siesLogger.debug(" TreeModel Vuoto - Messaggio di Restituzione = "+lMessage);
    
    //lMessage.setTreeModel (lMess.getTreeModel());
    lMessage.setTreeModel (new TreeModel());

    SIAPSender lSender = new SIAPSender();
    lSender.send(lMessage);         

    //========================================
    // Marco il messaggio di richiesta evaso.
    //========================================
    lMess.setFlagVisto("S");
    lMess.setDataEsito(DateUtils.getSysDate());
    lMess.setCodEsito("01007");
    lCrtl.ExModificaMessaggio(lMess);

    // setta la risposta nella request
    setRequestAttribute(IWebConstants.MESSAGE_TEXT, 
        "Le motivazioni della restituzione degli Atti sono state inviate all'ufficio mittente");

    //Prepara la "pagina" di destinAction
    RedirectTo lRedirigi = new RedirectTo();
    lRedirigi.setPage( IWebConstants.PG_MAIN );
    lRedirigi.setAction( "siap.siep.richiesta.action.ActLoadRicercaAttiCompetenzaRicevuti" );
    
    setRequestAttribute( IWebConstants.GOTO_PAGE, "" + lRedirigi );
    return IWebConstants.PG_MESSAGE;   
  }
}
