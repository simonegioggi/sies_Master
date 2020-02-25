package siap.sius.statistiche.action;

import java.io.ByteArrayOutputStream;

import org.apache.log4j.Logger;

import siap.sius.ActionSius;
import siap.sius.statistiche.model.RicercaProcedimentoModel;
import siap.sius.statistiche.util.excel.ProcProvvEmessiNoDepositoNumGGExcel;
import f3b.log.LogF3B;
import f3b.web.IWebConstants;

public class ActRicercaProcProvvEmessiNoDepositoNumGGExcel extends ActionSius implements ICostantiStatistiche {
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
    public String processRequest() throws Exception {
        RicercaProcedimentoModel              lRicercaModel   = null;
        ByteArrayOutputStream                 lFileOut        = null;   
        
        lRicercaModel = (RicercaProcedimentoModel) getSessionAttribute("ricercaProcedimenti");
        
        /*
        StatisController lStatisCtrl = new StatisController();

        // Generazione file xls
        lFileOut = lStatisCtrl.creaFoglioProcProvvEmessiNoDepositoNumGG(getUfficioUtenteConnesso(), lRicercaModel);
        */
        
        ProcProvvEmessiNoDepositoNumGGExcel lExcel = new ProcProvvEmessiNoDepositoNumGGExcel();
        lFileOut = lExcel.creaFoglioProcProvvEmessiNoDepositoNumGG(getUfficioUtenteConnesso(), lRicercaModel);

        setRequestAttribute("report", lFileOut);
        setRequestAttribute(IWebConstants.DISPOSITION_FIELD,IWebConstants.ATTACHMENT_DISPOSITION_FILE); 

        // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
        siesLogger.debug(getClass().getName() + " .processRequest: fine " );

        return IWebConstants.PG_DOWNLOAD_DOCUMENT;
    }

}