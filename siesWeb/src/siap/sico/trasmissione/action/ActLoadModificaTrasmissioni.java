package siap.sico.trasmissione.action;


/**
* <p>Title: ActLoadModificaTrasmissioni</p>
* <p>Description: Classe Action per la load modifica di Trasmissioni</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.math.BigDecimal;

import org.apache.log4j.Logger;

import siap.sico.trasmissione.controller.ITrasmissioni;
//import siap.sico.trasmissione.controller.ITrasmissioni;
import siap.sico.trasmissione.model.TrasmissioniModel;
//import per le combo
//import f3b.web.html.Option;
//import xxxx.decodifiche.controller.DecodificheManager;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import f3b.util.F3BException;
import f3b.web.IWebConstants;

public class ActLoadModificaTrasmissioni extends ActionSiap implements ICostantiTrasmissioni
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
    BigDecimal lIdTrasmissione   = getRequestBigDecimalParameter ( CAMPO_ID_TRASMISSIONE) ;
    logger.info("Key record = "); 

    //========================================== 
    // Recupera i dati del record da modificare  
    //========================================== 
    logger.info("Recupero i dati del record da modificare "); 
    ITrasmissioni lCtrl = SICOLookupRemote.getTrasmissioniRemote();
    TrasmissioniModel lTrasmissioni = lCtrl.ExRicercaTrasmissioniById( lIdTrasmissione );
 
    //===========================================================
    // Restituisce la msg se i dati non sono stati recuperati. 
    //===========================================================
    if (lTrasmissioni==null){ 
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
    logger.debug("Dati Record: "+lTrasmissioni); 
    setRequestAttribute("trasmissioni", lTrasmissioni);

    // Inserire Eventuali ComboBOX precaricando i dati del model
    // Option lOption = new Option( DecodificheManager.getInstance().get???());
    // setRequestAttribute("???", "" + lOption );

    // Imposta Modalità.
    setRequestAttribute("modalita", "M");

    //==========================================================================
    // Restituisce la pagina di modifica.
    // n.b. è la stessa della pagina di Inserimento ma con modalità differente 
    //==========================================================================
    logger.debug("return: "+PG_LOAD_INSERISCITRASMISSIONI); 
    return PG_LOAD_INSERISCITRASMISSIONI; 
  }
}