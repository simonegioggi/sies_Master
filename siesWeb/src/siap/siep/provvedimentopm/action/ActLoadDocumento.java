package siap.siep.provvedimentopm.action;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

import java.io.ByteArrayOutputStream;

import siap.sico.web.ActionSiap;
//import siap.siep.provvedimentopm.controller.ProvvedimentoController;
import siap.siep.provvedimentopm.controller.IProvvedimento;
import siap.siep.provvedimentopm.model.ProvvedimentoModel;
import siap.siep.util.SIEPLookupRemote;
import f3b.util.F3BException;
import f3b.web.IWebConstants;

public class ActLoadDocumento extends ActionSiap
  implements ICostantiProvvedimento
{

   public String processRequest() throws F3BException
    {
    ProvvedimentoModel lProMod = new ProvvedimentoModel();

    lProMod.setIdProvvedimento( getRequestBigDecimalParameter( CAMPO_ID_PROVVEDIMENTO) );

    IProvvedimento lCtrl = SIEPLookupRemote.getProvvedimentoRemote();
    ByteArrayOutputStream lReport = lCtrl.ExGetDocumento( lProMod );


    //Prepara la pagina di destinazione
    setRequestAttribute("report", lReport);

    return IWebConstants.PG_DOWNLOAD;
    }
}