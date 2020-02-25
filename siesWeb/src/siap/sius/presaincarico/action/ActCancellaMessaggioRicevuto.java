package siap.sius.presaincarico.action;

import java.math.BigDecimal;

import org.apache.log4j.Logger;

import siap.jms.ICostantiJMS;
import siap.jms.JMSLookupRemote;
import siap.jms.SIAPReceiver;
import siap.jms.config.JMSProperties;
import siap.jms.messaggio.action.ICostantiMessaggio;
import siap.jms.messaggio.controller.IMessaggio;
import siap.jms.messaggio.model.MessaggioModel;
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.web.ActionSiap;
import f3b.log.LogF3B;
import f3b.util.DateUtils;

public class ActCancellaMessaggioRicevuto extends ActionSiap implements ICostantiJMS
{
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
  public String processRequest() throws Exception
  {
    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.debug( getClass().getName() + ".processRequest: inizio" );
    String lRectPage =  "";
    
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
    
    BigDecimal lIdMess = getRequestBigDecimalParameter(ICostantiMessaggio.CAMPO_ID_MESSAGGIO);

    IMessaggio lCrtl = JMSLookupRemote.getMessaggioRemote();
    MessaggioModel lMess = lCrtl.ExRicercaMessaggioByKey(lIdMess);

    UfficioModel lBDI = this.getUfficioByCodUfficio(this.getCodUfficioUtenteConnesso());

    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.debug("lMess =" + lMess);
    String lEsito = "01000"; //Setto l'esito di cancellazione messaggio

    MessaggioModel lMessage = new MessaggioModel();

    lMessage.setDescrBdiDestinataria(lMess.getDescrBdiMittente());
    lMessage.setCodBdiDestinataria(lMess.getCodBdiMittente());
    lMessage.setCodUfficioDestinatario(lMess.getCodUfficioMittente());
    lMessage.setCodBdiMittente(lBDI.getCodDistretto());
    lMessage.setCodUfficioMittente(this.getCodUfficioUtenteConnesso());
    lMessage.setCodiceUtenteMittente(this.getCodUtenteConnesso());
    lMessage.setCodTipoMessaggio(ESITO);
    lMessage.setCodTipoOperazione(ESITO_TRASFERIMENTO_DECRETO);
    lMessage.setCodiceUtenteMittente(this.getCodUtenteConnesso());
    lMessage.setJmsCorrelationIdMessage(lMess.getJmsCorrelationIdMessage());
    lMessage.setDataInvio(DateUtils.getSysDate());
    lMessage.setDataEsito(DateUtils.getSysDate());
    lMessage.setCodEsito(lEsito);
    lMessage.setTreeModel(lMess.getTreeModel());

    // STUB 05/11/2004 Da Abilitare
    // SIAPSender lSender = new SIAPSender();
    // lSender.send(lMessage);

    lCrtl.ExCancellaMessaggio(lMess.getIdMessaggio());

    lRectPage = ritornoDopoCancellazione("Cancellazione Avvenuta Correttamente!", lRectPage);
    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.debug( getClass().getName() + ".processRequest: fine" );
    return lRectPage;

  }
}