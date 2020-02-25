package siap.sico.trasmissione.action;


/**
* <p>Title: ActCancellaTrasmissioni</p>
* <p>Description: Classe Action per la cancellazione di Trasmissioni</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.math.BigDecimal;

import org.apache.log4j.Logger;

import siap.sico.trasmissione.controller.ITrasmissioni;
import siap.sico.trasmissione.model.TrasmissioniModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import f3b.util.F3BException;
import f3b.web.IWebConstants;

public class ActCancellaTrasmissioni extends ActionSiap implements ICostantiTrasmissioni
{
  Logger logger = Logger.getLogger("actionLogger");
 /*****************************************************************************
  * Azione per la cancellazione dei dati. 
  * 
  * @return PG_MESSAGE di avvenuta cancellazione
  * @throws F3BException
  *****************************************************************************/
  public String processRequest() throws F3BException {
    //==========================================
    // Recupera la key del record da Cancellare 
    //==========================================
    BigDecimal lIdTrasmissione   = getRequestBigDecimalParameter ( CAMPO_ID_TRASMISSIONE) ;

    //========================================== 
    // Istanzia il model   
    //========================================== 
    TrasmissioniModel lTraMod = new TrasmissioniModel();

    lTraMod.setIdTrasmissione   (lIdTrasmissione);

    //====================================================== 
    // Recupera il Controller ed effettua la cancellazione 
    //====================================================== 
    ITrasmissioni lCtrl = SICOLookupRemote.getTrasmissioniRemote();
    lCtrl.ExCancellaTrasmissioni(lTraMod);

    //===========================================================
    // Restituisce la pagina di Conferma avvenuta Cancellazione. 
    //===========================================================
    //setRequestAttribute(IWebConstantsAGOST.MESSAGE_TEXT, "Cancellazione effettuata");
    // Specificare eventualmente la jump page dove verrà ridirezionata la 
    // PG_MESSAGE quando viene premuto OK. Se non viene attualizzato il 
    // parametro GOTO_PAGE, la PG_MESSAGE effettua un history.go(-1) 
    // n.b. il path da specificare nel parametro GOTO_PAGE deve essere completo 
    //      della root_dir es /siap/frame.htm  
    //setRequestAttribute(IWebConstantsAGOST.GOTO_PAGE, IWebConstantsAGOST.ROOT_DIR); 

    return IWebConstants.PG_MESSAGE;

  }
}