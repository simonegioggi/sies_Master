package siap.siep.calcolopena.action;

import java.math.BigDecimal;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.web.IWebConstants;
import siap.sico.web.ActionSiap;
import siap.siep.calcolopenadl92.controller.ICalcoloPenaDL92;
import siap.siep.util.SIEPLookupRemote;

/**
 * ActCalcoloPenaDL92 - Classe Action per la Calcolatrice
 *
 * @since MEV_2026-1
 */

public class ActCancStoricoCalcoloPenaDL92 extends ActionSiap implements ICostantiCalcoloPena {
    private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
    
    public String processRequest() throws Exception {
        
        BigDecimal idCalcolo = null;
        
        idCalcolo = getRequestBigDecimalParameter(CAMPO_ID_CALCOLO_PENA_DL92);
        
        ICalcoloPenaDL92 lCalcDL92Ctrl = SIEPLookupRemote.getCalcoloPenaDL92();
        
        lCalcDL92Ctrl.ExCancellaCalcoloPenaDL92(idCalcolo);

        String lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
                + "=siap.siep.calcolopena.action.ActLoadStoricoCalcoloPenaDL92";
        
        return lPage;
    }
}
