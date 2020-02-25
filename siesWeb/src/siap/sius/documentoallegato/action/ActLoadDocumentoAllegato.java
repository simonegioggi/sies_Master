package siap.sius.documentoallegato.action;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

import java.io.ByteArrayOutputStream;

import siap.sico.web.ActionSiap;
import siap.sius.documentoallegato.controller.IDocumentoAllegato;
import siap.sius.util.SIUSLookupRemote;
import f3b.web.IWebConstants;


public class ActLoadDocumentoAllegato extends ActionSiap
  implements ICostantiDocumentoAllegato
{

   public String processRequest() throws Exception
    {

    IDocumentoAllegato lCtrl = SIUSLookupRemote.getDocumentoAllegatoRemote();
    ByteArrayOutputStream lReport = lCtrl.ExGetDocumentoByKey(getRequestBigDecimalParameter( CAMPO_ID_DOCUMENTO_ALLEGATO));

    //Prepara la pagina di destinazione
    setRequestAttribute("report", lReport);

    return IWebConstants.PG_DOWNLOAD;
    }
}
