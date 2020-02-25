package siap.siep.modulocumulo.action;

import java.io.ByteArrayOutputStream;

import f3b.log.LogF3B;
import f3b.web.IWebConstants;

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
    siesLogger.info(" START -----> ");
    
    RichiesteInviateCumModel lRicMod = new RichiesteInviateCumModel();
    lRicMod.setIdRichiesteInviateCum(getRequestBigDecimalParameter(CAMPO_ID_RICHIESTA_INVIATA_CUM) );
    
    IRichiestePmInCumulo lCtrlRic = SIEPLookupRemote.getRichiestePmInCumuloRemote();
    ByteArrayOutputStream lReport = lCtrlRic.ExGetDocumento( lRicMod );
 
    setRequestAttribute("report", lReport);
    siesLogger.info("Report Size : " + lReport.size());
    
    siesLogger.info(" -----> END ");
    
    return IWebConstants.PG_DOWNLOAD_DOCUMENT;
  }
}