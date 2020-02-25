package siap.siep.competenza.action;


/**
* <p>Title: ActCancellaCompetenza</p>
* <p>Description: Classe Action per la cancellazione di Competenza</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.math.BigDecimal;

import siap.sico.web.ActionSiap;
import siap.sico.web.ISICOCostantiWeb;
import siap.siep.competenza.controller.ICompetenza;
import siap.siep.competenza.model.CompetenzaModel;
import siap.siep.util.SIEPLookupRemote;
import f3b.util.F3BException;

public class ActCancellaCompetenza extends ActionSiap implements ICostantiCompetenza
{
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
    BigDecimal lIdCompetenza                = getRequestBigDecimalParameter ( CAMPO_ID_COMPETENZA) ;

    //========================================== 
    // Istanzia il model   
    //========================================== 
    CompetenzaModel lComMod = new CompetenzaModel();

    lComMod.setIdCompetenza                (lIdCompetenza);

    //====================================================== 
    // Recupera il Controller ed effettua la cancellazione 
    //====================================================== 
    ICompetenza lCtrl = SIEPLookupRemote.getCompetenzaRemote();
    lCtrl.ExCancellaCompetenza(lComMod);

    //===========================================================
    // Restituisce la pagina di Conferma avvenuta Cancellazione. 
    //===========================================================
    setRequestAttribute(ISICOCostantiWeb.MESSAGE_TEXT, "Cancellazione effettuata");
    // Specificare eventualmente la jump page dove verrà ridirezionata la 
    // PG_MESSAGE quando viene premuto OK. Se non viene attualizzato il 
    // parametro GOTO_PAGE, la PG_MESSAGE effettua un history.go(-1) 
    // n.b. il path da specificare nel parametro GOTO_PAGE deve essere completo 
    //      della root_dir es /siap/frame.htm  
    setRequestAttribute(ISICOCostantiWeb.GOTO_PAGE, ISICOCostantiWeb.ROOT_DIR); 

    return ISICOCostantiWeb.PG_MESSAGE;

  }
}