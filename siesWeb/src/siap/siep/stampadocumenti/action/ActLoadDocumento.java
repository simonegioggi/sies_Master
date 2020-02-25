package siap.siep.stampadocumenti.action;


import java.io.ByteArrayOutputStream;

import siap.sico.web.ActionSiap;
import siap.siep.stampadocumenti.controller.StampaDocumentiController;
import siap.siep.stampadocumenti.model.StampaDocumentiModel;
import f3b.web.IWebConstants;

/**
 * <p>Title: ActLoadDocumento</p>
 * <p>Description: Carica il Documento BLOB </p>
 * <p>Copyright: Copyright (c) 2002</p>
 */

public class ActLoadDocumento extends ActionSiap
  implements ICostantiStampaDocumenti
{

   public String processRequest() throws Exception
    {
    StampaDocumentiModel lStaMod = new StampaDocumentiModel();
    lStaMod.setIdStampa(getRequestBigDecimalParameter( CAMPO_ID_STAMPA));
    
    //StampaDocumentiController --- 
    StampaDocumentiController lCtrl = new StampaDocumentiController();
    ByteArrayOutputStream lReport = lCtrl.ExGetDocumento( lStaMod );

    //Prepara la pagina di destinazione
    setRequestAttribute("report", lReport);

    //setRequestAttribute("report", lReport);
    return IWebConstants.PG_DOWNLOAD;
   }
}
