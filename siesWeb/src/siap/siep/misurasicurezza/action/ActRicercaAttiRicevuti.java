package siap.siep.misurasicurezza.action;

import java.math.BigDecimal;
import java.util.Stack;
import java.util.Vector;

import org.apache.log4j.Logger;

import siap.jms.ICostantiJMS;
import siap.jms.SIAPReceiver;
import siap.jms.config.JMSProperties;
import siap.jms.messaggio.model.MessaggioModel;
import siap.sico.web.ActionSiap;
import siap.siep.misurasicurezza.controller.IMisuraSicurezza;
import siap.siep.util.SIEPLookupRemote;
import f3b.log.LogF3B;
import f3b.util.F3BException;
import f3b.web.IWebConstants;


/**
 * Action per la ricerca degli atti ricevuti per competenza Misure di Sicurezza 
 * non ancora elaborati: FLAG_VISTO = 'N'.
 * 
 * Restituisce la jsl cone l'elenco degli atti
 * 
 * 
 * @author d.fiorletta
 *
 */
public class ActRicercaAttiRicevuti extends ActionSiap implements ICostantiMisuraSicurezza
{
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
  public String processRequest() throws F3BException, Exception
  {
	  //Ticket#20220111018 - Aggiunta pagina di transizione di wait solo sulla prima pagina
if (isRequestParameterNullObj("vai")) {
	// Pagina di attesa (rotellina)
	setRequestAttribute("titolo", " RICERCA ATTI RICEVUTI PER COMPETENZA ");
	setRequestAttribute("next_action", getClass().getName());
	
	if (!isRequestParameterNullObj(IWebConstants.NUM_PAGE))
		setRequestAttribute(IWebConstants.NUM_PAGE, getRequestStringParameter(IWebConstants.NUM_PAGE));
	if (!isRequestParameterNullObj("CountRisultati"))
		setRequestAttribute("CountRisultati", getRequestStringParameter("CountRisultati"));
	siesLogger.debug("Pagina di attesa :" + getClass().getName());
	return ICostantiMisuraSicurezza.PG_ATTESA_RICERCA;
} else {    
    try {
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
    }
    catch (Exception e){
      // do nothing. La mancanza di connessione con il provider non è boccante
      // in questa fase. L'utente potrà visualizzare solo i messaggi già scaricati
      // ma non eventuali messaggi fermi in coda
    }
    
    this.setLinkRitorno();    
    setRequestAttribute(IWebConstants.LINK_RITORNO, "10");
    
    // Ticket#20220111018 - rimuovo dal link torna indietro il parametro "&vai=pippo" altrimenti
    // non parte la pagina di wait quando si torna del dettaglio
    Stack lRetStack = (Stack) getSessionAttribute("StackDiRitorno");
    String retURL = lRetStack.peek().toString();
    if (retURL.contains("&vai=pippo")) {
	    retURL = retURL.replace("&vai=pippo","");
	    lRetStack.pop(); // Rimuovo il vecchio valore
	    lRetStack.push(retURL); // lo sostituisco con il nuovo
	    // Rimetto in sessione lo Stack
		this.setSessionAttribute("StackDiRitorno", lRetStack);	    
    }   
    // Ticket#20220111018
//    
//    MessaggioModel lMessaggio = new MessaggioModel();
//    
//    lMessaggio.setCodUfficioDestinatario(getCodUfficioUtenteConnesso());
//    lMessaggio.setCodTipoMessaggio   (ICostantiJMS.RICHIESTA);
//    lMessaggio.setCodTipoOperazione  (ICostantiJMS.TRASFERIMENTO_COMPETENZA_MS);
//    
//    lMessaggio.setFlagVisto("N");
    //lMessaggio.setCodUfficioMittente("");  
    
    // Ricerca Messaggi
//    IMessaggio lCrtl = JMSLookupRemote.getMessaggioRemote();
//    Vector lVect = lCrtl.ExRicercaMessaggio(lMessaggio);
    
    Vector <String> lListaTipoOperazione = new Vector <String> ();
    lListaTipoOperazione.add (ICostantiJMS.TRASFERIMENTO_COMPETENZA_MS);
    lListaTipoOperazione.add (ICostantiJMS.TRASFERIMENTO_ESECUZIONE_MS);
    
    
    // Ticket#20220111018 - Aggiunta sezione per la paginazione
	String lPagina = "1";
	if (!isRequestParameterNullObj(IWebConstants.NUM_PAGE))
		lPagina = getRequestStringParameter(IWebConstants.NUM_PAGE);
	// Ticket#20220111018 - FINE
    
    IMisuraSicurezza lCtrl = SIEPLookupRemote.getMisuraSicurezzaRemote();
    Vector <MessaggioModel> lVect = lCtrl.ExRicercaMessaggi (ICostantiJMS.DELIVERY_MODE_RICEVUTO
                                                           , ICostantiJMS.RICHIESTA
                                                           , lListaTipoOperazione
                                                           , null // lCodEsito
                                                           , "N" // Flag_visto.
                                                           , null // aChiaveAnnoSiep
                                                           , null // aChiaveProgrSiep
                                                           , null // aChiaveUfficioSiep
                                                           , null   // aCodUfficioMitt
                                                           , getCodUfficioUtenteConnesso() // ufficio dest
                                                           , null //lDataTrasmissioneDal
                                                           , null //lDataTrasmissioneAl 
                                                           // //Ticket#20220111018 x paginazione
                                                           , Integer.parseInt(lPagina));
                                                           //, 0);  
    													   // //Ticket#20220111018 x paginazione
    
    //=======================================================
    // Per ogni messaggio verifico se presente un sollecito
    //=======================================================
    if (lVect!=null){
      for (int i=0;i<lVect.size(); i++) {
        MessaggioModel lMessaggioRichiesta = lVect.elementAt(i);
        
        // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
        siesLogger.debug("Id Richiesta = "+lMessaggioRichiesta.getIdMessaggio());
        siesLogger.debug("getContaSolleciti() = "+lMessaggioRichiesta.getContaSolleciti());
        
        // Ticket#20220111018 - Gestione recupero dei solleciti solo se presenti
        if (lMessaggioRichiesta.getContaSolleciti()!=null && lMessaggioRichiesta.getContaSolleciti().intValue()>0)
        {
        // Ticket#20220111018 - FINE
	        BigDecimal idMessaggioSollecitato = null;
	        if (lMessaggioRichiesta.getJmsCorrelationReplyTo()!=null)
	          idMessaggioSollecitato = new BigDecimal(lMessaggioRichiesta.getJmsCorrelationReplyTo());
	        else 
	          idMessaggioSollecitato = new BigDecimal(lMessaggioRichiesta.getJmsCorrelationIdMessage());
	          
	        Vector <MessaggioModel> lVectSoll = lCtrl.ExRicercaSollecitiByIdRich (ICostantiJMS.DELIVERY_MODE_RICEVUTO
	                                                                            , ICostantiJMS.RICHIESTA
	                                                                            , ICostantiJMS.SOLLECITO_TRASFERIMENTO_COMPETENZA_MS
	                                                                            , idMessaggioSollecitato
	                                                                            , null //"N" // Flag_visto.
	                                                                            , getCodUfficioUtenteConnesso() // ufficio dest
	                                                                            ); 
	        if (lVectSoll!=null)
	          // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
	          siesLogger.debug("Solleciti trovati = "+lVectSoll.size());
	        else
	          // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
	          siesLogger.debug("Solleciti trovati = 0");
	
	        lMessaggioRichiesta.setMessaggiCorrelati(lVectSoll);
        }
      }
    }
    
    
    setRequestAttribute("Messaggi", lVect);

    // Ticket#20220111018 - Gestione Paginazione
	// Recupero il numero totale di record
	BigDecimal lCountRisultati;
	if (isRequestParameterNullObj("CountRisultati")) {
		lCountRisultati = lCtrl.ExCountRicercaMessaggi(ICostantiJMS.DELIVERY_MODE_RICEVUTO
                , ICostantiJMS.RICHIESTA
                , lListaTipoOperazione
                , null // lCodEsito
                , "N" // Flag_visto.
                , null // aChiaveAnnoSiep
                , null // aChiaveProgrSiep
                , null // aChiaveUfficioSiep
                , null   // aCodUfficioMitt
                , getCodUfficioUtenteConnesso() // ufficio dest
                , null //lDataTrasmissioneDal
                , null //lDataTrasmissioneAl 
                );
	} else {
		lCountRisultati = getRequestBigDecimalParameter("CountRisultati");
	}    
	
	setRequestAttribute("CountRisultati", lCountRisultati);
	setRequestAttribute(IWebConstants.NUM_PAGE, lPagina);
	// Rimuovo il parametro "&vai=pippo" aggiunto dalla pagina di wait altrimenti non ripassarà
	// più per la wait quando seleziono una pagina differente
	String requestUrl = getCompleteRequestURL();
	requestUrl = requestUrl.replace("&vai=pippo","");
	setRequestAttribute(IWebConstants.REQUEST_FOR_PAGING, requestUrl);		
	//setRequestAttribute(IWebConstants.REQUEST_FOR_PAGING, getCompleteRequestURL());	
	// Ticket#20220111018 - Gestione Paginazione FINE
    
    
    return ICostantiMisuraSicurezza.PG_LISTA_ATTI_RICEVUTI_TRASMISSIONE_COMPETENZA_MS;
} // end if rotella    
  }
}