package siap.siepe.ricezioneatti.action;

import java.math.BigDecimal;

import org.apache.log4j.Logger;

import siap.jms.ICostantiJMS;
import siap.jms.JMSLookupRemote;
import siap.jms.SIAPReceiver;
import siap.jms.SIAPSender;
import siap.jms.config.JMSProperties;
import siap.jms.messaggio.action.ICostantiMessaggio;
import siap.jms.messaggio.controller.IMessaggio;
import siap.jms.messaggio.model.MessaggioModel;
import siap.sico.evento.model.XModel;
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.web.ActionSiap;
import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.xml.TreeModel;

public class ActRestituzioneAttoRicevuto extends ActionSiap implements ICostantiJMS
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
    String codTipoOperazione = getRequestStringParameter("CodTipoOperazione");

    String lMotivazioni = getRequestStringParameter("Motivazioni");

    // Costruzione del TreeModel da inserire nel BLOB.
    XModel lXModel = new XModel();
    lXModel.setTipoUfficio(getUfficioUtenteConnesso().getCodTipoUfficio());
    lXModel.setTipoUfficioT1(getUfficioUtenteConnesso().getDescrTipoUfficio());
    lXModel.setUfficio(getUfficioUtenteConnesso().getDescrComune().toUpperCase());
    lXModel.setMessage(lMotivazioni);
    TreeModel lTree = new TreeModel(lXModel);

    IMessaggio lCrtl = JMSLookupRemote.getMessaggioRemote();
    MessaggioModel lMess = lCrtl.ExRicercaMessaggioByKey(lIdMess);

    UfficioModel lBDI = this.getUfficioByCodUfficio(this.getCodUfficioUtenteConnesso());

    String lEsito = ICostantiJMS.RESTITUITO; //Setto l'esito di Restituzione

    MessaggioModel lMessage = new MessaggioModel();

    lMessage.setDescrBdiDestinataria(lMess.getDescrBdiMittente());
    lMessage.setCodBdiDestinataria(lMess.getCodBdiMittente());
    lMessage.setCodUfficioDestinatario(lMess.getCodUfficioMittente());
    lMessage.setCodBdiMittente(lBDI.getCodDistretto());
    lMessage.setCodUfficioMittente(this.getCodUfficioUtenteConnesso());
    lMessage.setCodiceUtenteMittente(this.getCodUtenteConnesso());
    lMessage.setCodTipoMessaggio(ESITO);
    lMessage.setCodTipoOperazione(codTipoOperazione);
    lMessage.setCodiceUtenteMittente(this.getCodUtenteConnesso());
    lMessage.setJmsCorrelationIdMessage(lMess.getJmsCorrelationIdMessage());
    lMessage.setDataInvio(DateUtils.getSysDate());
    lMessage.setDataEsito(DateUtils.getSysDate());
    lMessage.setCodEsito(lEsito);
    lMessage.setTreeModel(lTree);

    SIAPSender lSender = new SIAPSender();
    lSender.send(lMessage);

    lMess.setFlagVisto("R");
    lMess.setDataEsito(DateUtils.getSysDate());
    lCrtl.ExModificaMessaggio(lMess);

    // Forzatura per la restituzione: inserito il messaggio correlato per recuperare il MOTIVO RESTITUZIONE.
    lMessage.setFlagVisto("R");
    lMessage.setJmsCorrelationIdMessage(lMess.getIdMessaggio().toString());
    lCrtl.ExInserisciMessaggio(lMessage);

    lRectPage = ritornoDopoCancellazione("Messaggio Restituito al Mittente per incompetenza!", lRectPage);
    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.debug( getClass().getName() + ".processRequest: fine" );
    return lRectPage;
  }
}