package siap.siep.rinnovo.action;


import java.io.ByteArrayOutputStream;

import siap.sico.web.ActionSiap;
import siap.siep.rinnovo.controller.IRinnovo;
import siap.siep.rinnovo.model.RinnovoModel;
import siap.siep.util.SIEPLookupRemote;
import f3b.web.IWebConstants;

/**
 * <p>Title: ActLoadDocumento</p>
 * <p>Description: Carica il Documento BLOB </p>
 * <p>Copyright: Copyright (c) 2002</p>
 */

public class ActLoadDocumentoRinnovo extends ActionSiap
                                     implements ICostantiRinnovo
{
   public String processRequest() throws Exception
   {
    RinnovoModel lRinMod = new RinnovoModel();

    lRinMod.setIdRinnovo( getRequestBigDecimalParameter( CAMPO_ID_RINNOVO) );

    IRinnovo lCtrl = SIEPLookupRemote.getRinnovoRemote();
    ByteArrayOutputStream lReport = lCtrl.ExGetDocumento( lRinMod );

    //Prepara la pagina di destinazione
    setRequestAttribute("report", lReport);
    //setRequestAttribute("report", lReport);

    return IWebConstants.PG_DOWNLOAD;
   }
}
