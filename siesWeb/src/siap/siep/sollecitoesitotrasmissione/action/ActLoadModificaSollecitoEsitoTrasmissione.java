package siap.siep.sollecitoesitotrasmissione.action;


/**
* <p>Title: ActLoadModificaSollecitoEsitoTrasmissione</p>
* <p>Description: Classe Action per la load modifica di SollecitoEsitoTrasmissione</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.math.BigDecimal;

import org.apache.log4j.Logger;

import siap.sico.web.ActionSiap;
import siap.siep.sollecitoesitotrasmissione.controller.ISollecitoEsitoTrasmissione;
import siap.siep.sollecitoesitotrasmissione.model.SollecitoEsitoTrasmissioneModel;
import siap.siep.util.SIEPLookupRemote;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
//import siap.jms.sollecitoesitotrasmissione.controller.ISollecitoEsitoTrasmissione;
//import per le combo
//import f3b.web.html.Option;
//import xxxx.decodifiche.controller.DecodificheManager;

public class ActLoadModificaSollecitoEsitoTrasmissione extends ActionSiap implements ICostantiSollecitoEsitoTrasmissione
{
  Logger logger = Logger.getLogger("actionLogger");
 /*****************************************************************************
  * Azione di caricamento della pagina di Modifica dei dati. Si occupa anche 
  * di precaricare tutti i dati da visualizzare i tale pagina (es: combo) 
  * 
  * @return Nome della pagina JSP da visualizzare
  * @throws F3BException
  *****************************************************************************/
  public String processRequest() throws F3BException {
    logger.info("Start"); 
    //==========================================
    // Recupera la key del record da modificare 
    //==========================================
    BigDecimal lIdSollecito               = getRequestBigDecimalParameter ( CAMPO_ID_SOLLECITO) ;
    logger.info("Key record = "); 

    //========================================== 
    // Recupera i dati del record da modificare  
    //========================================== 
    logger.info("Recupero i dati del record da modificare "); 
    ISollecitoEsitoTrasmissione lCtrl = SIEPLookupRemote.getSollecitoEsitoTrasmissioneRemote();
    SollecitoEsitoTrasmissioneModel lSollecitoEsitoTrasmissione = lCtrl.ExRicercaSollecitoEsitoTrasmissioneById( lIdSollecito );
 
    //===========================================================
    // Restituisce la msg se i dati non sono stati recuperati. 
    //===========================================================
    if (lSollecitoEsitoTrasmissione==null){ 
      logger.error("Record non trovato!!"); 
      setRequestAttribute(IWebConstants.MESSAGE_TEXT, "I dati richiesti non sono stati trovati");
      // Specificare eventualmente la jump page dove verrà ridirezionata la 
      // PG_MESSAGE quando viene premuto OK. Se non viene attualizzato il 
      // parametro GOTO_PAGE, la PG_MESSAGE effettua un history.go(-1) 
      // n.b. il path da specificare nel parametro GOTO_PAGE deve essere completo 
      //      della root_dir es /siap/frame.htm  
    setRequestAttribute(IWebConstants.GOTO_PAGE, IWebConstants.ROOT_DIR); 
      return IWebConstants.PG_MESSAGE;
    }

    //==================================================== 
    // Passa il Model alla componente di visualizzazione  
    //==================================================== 
    logger.debug("Dati Record: "+lSollecitoEsitoTrasmissione); 
    setRequestAttribute("sollecitoesitotrasmissione", lSollecitoEsitoTrasmissione);

    // Inserire Eventuali ComboBOX precaricando i dati del model
    // Option lOption = new Option( DecodificheManager.getInstance().get???());
    // setRequestAttribute("???", "" + lOption );

    // Imposta Modalità.
    setRequestAttribute("modalita", "M");

    //==========================================================================
    // Restituisce la pagina di modifica.
    // n.b. è la stessa della pagina di Inserimento ma con modalità differente 
    //==========================================================================
    logger.debug("return: "+PG_LOAD_INSERISCISOLLECITOESITOTRASMISSIONE); 
    return PG_LOAD_INSERISCISOLLECITOESITOTRASMISSIONE; 
  }
}