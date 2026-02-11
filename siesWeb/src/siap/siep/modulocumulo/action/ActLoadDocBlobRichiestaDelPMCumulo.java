package siap.siep.modulocumulo.action;

import java.io.ByteArrayOutputStream;

import f3b.log.LogF3B;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
import siap.siep.SIEPException;
import siap.siep.modulocumulo.controller.IRichiestePmInCumulo;
import siap.siep.modulocumulo.model.RichiesteInviateCumModel;
import siap.siep.util.SIEPLookupRemote;

import org.apache.log4j.Logger;

/**
 * <p>Title: ActLoadDocBlobRichiestaDelPMCumulo</p>
 * <p>Description: Carica il Documento BLOB </p>
 * <p>	Gestione Cumulo - Richieste del PM 	</p>
 * <p>Copyright: Copyright (c) 2002</p>
 */

public class ActLoadDocBlobRichiestaDelPMCumulo extends ActionModuloCumulo
implements ICostantiRichiestePmInCumulo
{
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
	
  public String processRequest() throws Exception
  {
    siesLogger.debug(" START -----> ");
    
    RichiesteInviateCumModel lRicMod = new RichiesteInviateCumModel();
    lRicMod.setIdRichiesteInviateCum(getRequestBigDecimalParameter(CAMPO_ID_RICHIESTA_INVIATA_CUM) );
    
    IRichiestePmInCumulo lCtrlRic = SIEPLookupRemote.getRichiestePmInCumuloRemote();
    ByteArrayOutputStream lReport = null;
    try {
        lReport = lCtrlRic.ExGetDocumento( lRicMod );
    }
    catch (F3BException e) {
        siesLogger.warn("ActLoadDocBlobRichiestaDelPMCumulo F3BException: "+e.getMessage());
        if (e.getErrorCode()==F3BException.USER_MESSAGE)
            throw new SIEPException(F3BException.USER_MESSAGE,e.getMessage());
    //                "Non si può trasferire l'istruttoria sullo stesso fascicolo");
    }
    catch (Exception e) {
        siesLogger.warn("ActLoadDocBlobRichiestaDelPMCumulo Exception: "+e.getMessage());
    }
    setRequestAttribute("report", lReport);
    siesLogger.debug("Report Size : " + lReport.size());
    
    siesLogger.debug(" -----> END ");
    
    return IWebConstants.PG_DOWNLOAD_DOCUMENT;
  }
}