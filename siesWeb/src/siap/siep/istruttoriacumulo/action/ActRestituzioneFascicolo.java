package siap.siep.istruttoriacumulo.action;

import java.math.BigDecimal;

import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.xml.TreeModel;
import f3b.web.IWebConstants;

import siap.jms.ICostantiJMS;
import siap.jms.JMSLookupRemote;
import siap.jms.SIAPReceiver;
import siap.jms.SIAPSender;
import siap.jms.config.JMSProperties;
import siap.jms.messaggio.action.ICostantiMessaggio;
import siap.jms.messaggio.controller.IMessaggio;
import siap.jms.messaggio.model.MessaggioModel;

import siap.siep.modulocumulo.action.ActionModuloCumulo;

import org.apache.log4j.Logger;

public class ActRestituzioneFascicolo extends ActionModuloCumulo implements ICostantiIstruttoriaCumulo
{
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
	
  public String processRequest() throws Exception
  {
    siesLogger.debug( getClass().getName() + ".processRequest: inizio" );
       
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

    //
    BigDecimal lIdIstruttoriaCumulo = null;
    if (!isRequestParameterNullObj (ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO)
        && !getRequestStringParameter(ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO).equals("")
        ) 
    {
      super.getDatiIstruttoria();
      lIdIstruttoriaCumulo = getRequestBigDecimalParameter ( CAMPO_ID_ISTRUTTORIA_CUMULO) ;
    }
    
    BigDecimal lIdMess = getRequestBigDecimalParameter(ICostantiMessaggio.CAMPO_ID_MESSAGGIO);
    String lMotivazioni = getRequestStringParameter(CAMPO_NOTE);

    IMessaggio lCrtl = JMSLookupRemote.getMessaggioRemote();
    MessaggioModel lMess = lCrtl.ExRicercaMessaggioByKey(lIdMess);
    siesLogger.debug("lMess =" + lMess);

    MessaggioModel lMessage = new MessaggioModel();

    lMessage.setDescrBdiDestinataria(lMess.getDescrBdiMittente());
    lMessage.setCodBdiDestinataria(lMess.getCodBdiMittente());
    lMessage.setCodUfficioDestinatario(lMess.getCodUfficioMittente());
    
    lMessage.setCodBdiMittente(getCodDistrettoUtenteConnesso());
    lMessage.setCodUfficioMittente(this.getCodUfficioUtenteConnesso());
    lMessage.setCodiceUtenteMittente(this.getCodUtenteConnesso());
    
    lMessage.setCodTipoMessaggio	(ICostantiJMS.ESITO);							// Mess di Esito (02)	
    lMessage.setCodTipoOperazione	(ICostantiJMS.ESITO_TRASFERIMENTO_COMPETENZA);	// Mess di esito Trasf Comp (00067)
    lMessage.setCodEsito			(ICostantiJMS.RESTITUITO);						// Mess Restituito (01003)

    lMessage.setJmsCorrelationIdMessage(lMess.getJmsCorrelationIdMessage());
    //lMessage.setJmsCorrelationIdMessage(lMess.getIdMessaggio().toString());
    
    lMessage.setDataInvio	(DateUtils.getSysDate());
    //lMessage.setDataInvio 	(lMess.getDataInvio()); //FIXME CUMULO verificare prechè non data di sistema
    lMessage.setDataEsito	(DateUtils.getSysDate());
    
    lMessage.setChiaveAnnoSiep    (lMess.getChiaveAnnoSiep());
    lMessage.setChiaveProgrSiep   (lMess.getChiaveProgrSiep());
    lMessage.setChiaveUfficioSiep (lMess.getChiaveUfficioSiep());
    
    //dati soggetto
    lMessage.setNomeSoggetto      (lMess.getNomeSoggetto());
    lMessage.setCognomeSoggetto   (lMess.getCognomeSoggetto());
    lMessage.setDataNascita       (lMess.getDataNascita());
    lMessage.setCodComuneNascita  (lMess.getCodComuneNascita());
    lMessage.setCodStatoNascita   (lMess.getCodStatoNascita());        
    
    lMessage.setChiaveAnnoFasCumulante    (lMess.getChiaveAnnoFasCumulante());
    lMessage.setChiaveProgrFasCumulante   (lMess.getChiaveProgrFasCumulante());
    lMessage.setChiaveUfficioFasCumulante (lMess.getChiaveUfficioFasCumulante());

// Costruzione del TreeModel da inserire nel BLOB : viene inseritp un TreeModel VUOTO    
    lMessage.setTreeModel (new TreeModel());
    
    // Motivazioni nelle Note
    if(!lMotivazioni.equals(null) && !lMotivazioni.equals(""))
    	lMessage.setNote(lMotivazioni);
    
    // Dentro il metodo .send viene fatto anche INSERT MESSAGGIO
    SIAPSender lSender = new SIAPSender();
    lSender.send(lMessage);
    
// Modifica del Messaggio di Richiesta, che è stato Restituito (CosEsito = 01003)   
    lMess.setDataEsito(DateUtils.getSysDate());  /// ????
    lMess.setCodEsito("01003");
    lCrtl.ExModificaMessaggio(lMess);

    siesLogger.debug( getClass().getName() + ".processRequest: fine" );
    
    String lPage="";
    
    String lAction = getRequestStringParameter(IWebConstants.GOTO_PAGE);

      lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD + "=" + lAction;
      if (lIdIstruttoriaCumulo!=null)
        lPage += "&" + CAMPO_ID_ISTRUTTORIA_CUMULO + "=" + lIdIstruttoriaCumulo;
      
      return lPage;			
    
  }
}
