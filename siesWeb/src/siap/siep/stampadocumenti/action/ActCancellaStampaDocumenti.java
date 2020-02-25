package siap.siep.stampadocumenti.action;


/**
* <p>Title: ActCancellaStampaDocumenti</p>
* <p>Description: Classe Action per la cancellazione di StampaDocumenti</p>
* <p>Copyright: Copyright (c) 2007</p>
* <p>Company: Eunics</p>
* @version 1.0
*/

import java.math.BigDecimal;

import siap.sico.web.ActionSiap;
import siap.siep.stampadocumenti.controller.StampaDocumentiController;
import siap.siep.stampadocumenti.model.StampaDocumentiModel;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;

public class ActCancellaStampaDocumenti extends ActionSiap implements ICostantiStampaDocumenti
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
    BigDecimal lIdStampa             = getRequestBigDecimalParameter ( CAMPO_ID_STAMPA) ;

    //========================================== 
    // Istanzia il model   
    //========================================== 
    StampaDocumentiModel lStaMod = new StampaDocumentiModel();

    lStaMod.setIdStampa(lIdStampa);

    //====================================================== 
    // Recupera il Controller ed effettua la cancellazione 
    //====================================================== 
    StampaDocumentiController lCtrl = new StampaDocumentiController();
    lCtrl.ExCancellaStampaDocumenti(lStaMod);

    //===========================================================
    // Restituisce la pagina di Conferma avvenuta Cancellazione. 
    //===========================================================
    setRequestAttribute(IWebConstants.MESSAGE_TEXT,"Cancellazione Avvenuta Correttamente!");

    //Prepara la "pagina" di destinAction
    RedirectTo lRedirigi = new RedirectTo();
    lRedirigi.setPage( IWebConstants.PG_MAIN );
    lRedirigi.setAction( "siap.siep.stampadocumenti.action.ActRicercaStampaDocumenti" );
    setRequestAttribute( IWebConstants.GOTO_PAGE, "" + lRedirigi );

   
    return IWebConstants.PG_MESSAGE;

  }
}