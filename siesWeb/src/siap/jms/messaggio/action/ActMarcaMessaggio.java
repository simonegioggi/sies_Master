package siap.jms.messaggio.action;

import java.math.BigDecimal;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.web.IWebConstants;
import siap.jms.ICostantiJMS;
import siap.jms.JMSLookupRemote;
import siap.jms.messaggio.controller.IMessaggio;
import siap.sico.web.ActionSiap;


public class ActMarcaMessaggio extends ActionSiap implements ICostantiJMS {
    private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
    
    public String processRequest() throws Exception
    {
        siesLogger.debug("ActMarcaMessaggio.start");
        BigDecimal lIdMess = getRequestBigDecimalParameter(ICostantiMessaggio.CAMPO_ID_MESSAGGIO);
    
        IMessaggio lCrtl = JMSLookupRemote.getMessaggioRemote();
        lCrtl.ExMarcaMessaggioVisto(lIdMess);
        
        String lReturnLink = getRequestStringParameter("Return");
        
        String lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
                + "="+lReturnLink;
        siesLogger.debug("lPage = "+lPage);
        
        return lPage;
    }
}
