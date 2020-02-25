package siap.sius.statistiche.action;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
//import java.util.ArrayList;
import java.util.Collection;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.Iterator;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import siap.sige.web.ActionSige;
import siap.sius.statistiche.controller.IStatisticheSius;
import siap.sius.statistiche.model.EveFasGepSogProvModel;
//import siap.sius.statistiche.model.RicercaOrdinanzaModel;
import siap.sius.statistiche.model.RicercaProcedimentoModel;
import siap.sius.statistiche.util.excel.ProcDataUdienzaFissataNonDefinitiExcel;
//import siap.sius.fascicolo.controller.IFascicoloSius;
import siap.sius.util.SIUSLookupRemote;
//import java.util.Vector;
import f3b.log.LogF3B;

//import f3b.model.DecodeModel;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
//import siap.sico.decodifiche.controller.DecodificheManager;
//import siap.sico.decodifiche.util.DecodificheUtils;

public class ActRicercaProcDataUdienzaFissataNonDefinitiExcel extends ActionSige implements ICostantiStatistiche
{
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
    public String processRequest() throws Exception
    {
        RicercaProcedimentoModel              lRicercaModel   = null;
        HSSFWorkbook                          lWb             = null;
        IStatisticheSius                      lCtrlStatSius   = null;
        Collection<EveFasGepSogProvModel>     lElenco         = null;
        ByteArrayOutputStream                 fileOut         = null;   
        
        
        lRicercaModel = (RicercaProcedimentoModel) getSessionAttribute("ricercaProcedimenti");
        
        lCtrlStatSius = SIUSLookupRemote.getStatisticheSiusRemote();
        lElenco  = lCtrlStatSius.ExRicercaProcFissatiNoDef(lRicercaModel);
        
        if (lElenco != null)
            // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
            siesLogger.debug( "Risultati ricerca completa : " + lElenco.size() );
        
        fileOut = new ByteArrayOutputStream();    
        /*
        StatisController lStatisCtrl = new StatisController();

        // Generazione file xls
        lWb = lStatisCtrl.creaFoglioProcFissatiNonDef(getUfficioUtenteConnesso(), lRicercaModel, lElenco);
        */
        ProcDataUdienzaFissataNonDefinitiExcel lExcel = new ProcDataUdienzaFissataNonDefinitiExcel();
        lWb = lExcel.creaFoglioProcFissatiNonDef(getUfficioUtenteConnesso(), lRicercaModel, lElenco);
        
        try  {
            lWb.write(fileOut);
        } catch (IOException ioe){
            throw new F3BException(
                    "ActStampaElencoProcInExcel.processRequest: " + ioe);           
        }
        
        setRequestAttribute("report", fileOut);
        setRequestAttribute(IWebConstants.DISPOSITION_FIELD,IWebConstants.ATTACHMENT_DISPOSITION_FILE); 

        // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
        siesLogger.debug( "" + getClass().getName() + " .processRequest: fine " );

        return IWebConstants.PG_DOWNLOAD_DOCUMENT;
    }
}