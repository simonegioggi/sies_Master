package siap.sius.statistiche.action;

import java.io.ByteArrayOutputStream;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.web.IWebConstants;
import siap.sius.ActionSius;
import siap.sius.statistiche.model.RicercaProcedimentoModel;
import siap.sius.statistiche.util.excel.RicercaAttiIstruttoriDataRestExcel;

public class ActRicercaAttiIstruttoriDataRestExcel extends ActionSius implements ICostantiStatistiche {

	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
	
	public String processRequest() throws Exception {
        RicercaProcedimentoModel lRicercaModel   = null;
        ByteArrayOutputStream    lFileOut        = null;   
                
        // Dalla SESSION
        lRicercaModel = (RicercaProcedimentoModel) getSessionAttribute("ricercaAttiIstruttori");
        
        RicercaAttiIstruttoriDataRestExcel lExcel = new RicercaAttiIstruttoriDataRestExcel();
        lFileOut = lExcel.creaFoglioRicercaAttiIstruttoriDataRest (getUfficioUtenteConnesso(), lRicercaModel); 
        
        setRequestAttribute("report", lFileOut);
        setRequestAttribute(IWebConstants.DISPOSITION_FIELD,IWebConstants.ATTACHMENT_DISPOSITION_FILE); 

        siesLogger.debug(getClass().getName() + " .processRequest: fine " );

        return IWebConstants.PG_DOWNLOAD_DOCUMENT;
    }
}
