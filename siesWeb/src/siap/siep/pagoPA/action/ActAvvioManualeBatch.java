package siap.siep.pagoPA.action;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.web.IWebConstants;
import siap.sico.web.ActionSiap;
import siap.siep.pagoPaBatch.controller.IBatchPagopa;
import siap.siep.util.SIEPLookupRemote;

public class ActAvvioManualeBatch extends ActionSiap implements ICostantiPagoPA {

  private static Logger siesLogger = Logger.getLogger(LogF3B.PAGO_PA_LOG);

  public String processRequest() throws Exception {

    siesLogger.debug(getClass().getName() + ".processRequest: inizio");

    IBatchPagopa lCtrlBatch = null;
    lCtrlBatch = SIEPLookupRemote.getBatchPagopaPagopaRemote();
    
    
    try {
      siesLogger.debug("Lancio Manuale...");
      lCtrlBatch.ExLancioBatchPagopa (getUtenteConnesso());
      siesLogger.debug("...Ritorno");    
    } catch (Exception e) {
      siesLogger.error("Lancio Manuale in errore",e);
    }
    
    siesLogger.debug(getClass().getName() + ".processRequest: fine");
    
    String lPage = null;
    lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
        + "=siap.siep.pagoPaBatch.action.ActVisualizzaBatchPagoPa";    
    
    return lPage;
  }

}
