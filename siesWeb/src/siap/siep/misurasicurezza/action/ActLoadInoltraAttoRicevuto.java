package siap.siep.misurasicurezza.action;

import java.math.BigDecimal;

import siap.jms.ICostantiJMS;
import siap.jms.JMSLookupRemote;
import siap.jms.SIAPReceiver;
import siap.jms.config.JMSProperties;
import siap.jms.messaggio.action.ICostantiMessaggio;
import siap.jms.messaggio.controller.IMessaggio;
import siap.jms.messaggio.model.MessaggioModel;
import siap.jms.util.ParserMessage;
import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.web.ActionSiap;
import siap.siep.fascicolo.model.DettaglioFascicoloModel;
import siap.sius.SIUSException;
import f3b.util.F3BException;
import f3b.web.html.Option;

public class ActLoadInoltraAttoRicevuto extends ActionSiap implements ICostantiMisuraSicurezza, ICostantiJMS
{
  public String processRequest() throws F3BException, Exception
  {
    // Avvio i listener
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
    
    //=========================
    // Recupero il messaggio
    //=========================
    BigDecimal lIdMess = getRequestBigDecimalParameter(ICostantiMessaggio.CAMPO_ID_MESSAGGIO);

    IMessaggio lCrtl = JMSLookupRemote.getMessaggioRemote();
    MessaggioModel lMessaggio = lCrtl.ExRicercaMessaggioByKey(lIdMess);
    setRequestAttribute("Messaggio", lMessaggio );
    

    //================================================================
    //  Passo il DettaglioFascicoloModel alla jsp di visualizzazione
    //================================================================
    ParserMessage lParser = null;  
    lParser = new ParserMessage(lMessaggio.getTreeModel());
    
    DettaglioFascicoloModel lDettaglioFasModel = null;
    if (lParser != null && lParser.getDettaglioFascicoloSiep() != null)
      lDettaglioFasModel = lParser.getDettaglioFascicoloSiep();
    else
      throw new SIUSException( SIUSException.USER_MESSAGE, "Errore nella Ricezione del Procedimento. <BR>Rivolgersi all'amministratore di sistema! " );

    this.setRequestAttribute("dettaglioFasSIEP", lDettaglioFasModel);
    

    //===============================
    // Tipo UFFICIO Destinatario    
    //===============================
    Option lOption = null;
    lOption = new Option(DecodificheManager.getInstance().getTipoUfficioPM());
    String[] lFiltroUffici =  {"-", "PM", "PMM"}; // i destinatari sono solo PM e PMM
    lOption.setFilter(lFiltroUffici);
    setRequestAttribute("ufficioPM", "" + lOption );
    
    return PG_LOAD_INOLTRO_ATTI_RICEVUTI;
  }
}
