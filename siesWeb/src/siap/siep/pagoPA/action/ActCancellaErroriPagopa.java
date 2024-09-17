package siap.siep.pagoPA.action;

import java.math.BigDecimal;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;
import siap.sico.web.ActionSiap;
import siap.siep.pagoPA.controller.IErroriSiesPagopa;
import siap.siep.util.SIEPLookupRemote;

public class ActCancellaErroriPagopa extends ActionSiap implements ICostantiErroriSiesPagopa {
  private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

  public String processRequest() throws Exception {
  
    siesLogger.debug(getClass().getName() + ".processRequest: inizio");    
    BigDecimal idErrore = getRequestBigDecimalParameter(ICostantiErroriSiesPagopa.CAMPO_ID_ERRORI_SIES_PAGOPA);
    
    IErroriSiesPagopa lCtrl = SIEPLookupRemote.getErroriSiesPagopaRemote();
    lCtrl.ExCancellaErroreSiesPagopa(idErrore);
    
    siesLogger.debug(getClass().getName() + ".processRequest: fine"); 
    RedirectTo rt = new RedirectTo();
    rt.setPage(IWebConstants.PG_MAIN);
    rt.setAction("siap.siep.pagoPA.action.ActVerificaErroriPagopa");
    return rt.toString();
  }
}
  
