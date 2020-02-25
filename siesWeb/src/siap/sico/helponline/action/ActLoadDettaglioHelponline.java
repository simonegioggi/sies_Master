package siap.sico.helponline.action;
  
/**
* <p>Title: ActLoadDettaglioHelponline</p>
* <p>Description: Classe Action per la load dettaglio di Helponline</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import org.apache.log4j.Logger;

import siap.sico.web.ActionSiap;
import f3b.log.LogF3B;
import f3b.util.F3BException;


public class ActLoadDettaglioHelponline extends ActionSiap implements ICostantiHelponline
{       
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
 /*****************************************************************************
  * Azione di caricamento della pagina di Dettaglio dei dati. 
  * 
  * @return Nome della pagina JSP da visualizzare
  * @throws F3BException
  *****************************************************************************/
  public String processRequest() throws F3BException {

    /*
    String LastFunctionID=(String)getSession().getAttribute("LastFunctionID");
    String OnlyFunctionID;
    int pos = LastFunctionID.indexOf("-", 0);
    OnlyFunctionID = LastFunctionID.substring(0, pos-1);
  
   */
    
    //========================================== 
    // Recupera i dati del record da modificare  
    //========================================== 
    //IHelponline lCtrl = SICOLookupRemote.getHelponlineRemote();
    
    //HelponlineModel lHelMod = lCtrl.ExRicercaHelponlineById(new BigDecimal(OnlyFunctionID));

    //===========================================================
    // Restituisce la msg se i dati non sono stati recuperati. 
    //===========================================================
    /*
    if (lHelMod==null){ 
      setRequestAttribute(IWebConstants.MESSAGE_TEXT, "I dati richiesti non sono stati trovati");
      // Specificare eventualmente la jump page dove verrà ridirezionata la 
      // PG_MESSAGE quando viene premuto OK. Se non viene attualizzato il 
      // parametro GOTO_PAGE, la PG_MESSAGE effettua un history.go(-1) 
      // n.b. il path da specificare nel parametro GOTO_PAGE deve essere completo 
      //      della root_dir es /siep/frame.htm  
    setRequestAttribute(IWebConstants.GOTO_PAGE, IWebConstants.ROOT_DIR); 
      return IWebConstants.PG_MESSAGE;
    }
    */
    String lPage = (String) getSessionAttribute("HelpPage");
    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.info("-----------------------------------------");
    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.info("ActLoadDettaglioHelpOnLine: "+ lPage);
    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.info("-----------------------------------------");
    return lPage;
  }
}