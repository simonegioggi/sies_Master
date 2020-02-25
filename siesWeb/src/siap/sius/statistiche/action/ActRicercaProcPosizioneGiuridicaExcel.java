package siap.sius.statistiche.action;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Collection;
//import java.util.HashMap;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

//import siap.sico.ufficio.model.UfficioModel;
import siap.sico.web.ActionSiap;
//import siap.siep.statis.controller.StatisController;
//import siap.sius.cancassfascsius.model.CancAssFascSiusModel;
//import siap.sius.fascicolo.controller.IFascicoloSius;
//import siap.sius.fascicolo.model.FascicoloSiusModel;
import siap.sius.statistiche.controller.IStatisticheSius;
import siap.sius.statistiche.model.EveFasGepSogCancModel;
import siap.sius.statistiche.model.RicercaProcedimentoModel;
import siap.sius.statistiche.util.excel.ProcPosizioneGiuridicaExcel;
import siap.sius.util.SIUSLookupRemote;
import f3b.log.LogF3B;
import f3b.util.F3BException;
import f3b.web.IWebConstants;

public class ActRicercaProcPosizioneGiuridicaExcel extends ActionSiap implements ICostantiStatistiche {
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
	public String processRequest() throws Exception {
        RicercaProcedimentoModel              lRicerca   	= null;
        HSSFWorkbook                          lWb           = null;
        IStatisticheSius                      lCtrlStatSius	= null;
        Collection<EveFasGepSogCancModel>     lElenco       = null;
        ByteArrayOutputStream                 fileOut       = null;   
        
        lRicerca = (RicercaProcedimentoModel) getSessionAttribute("ricercaModel");
        lCtrlStatSius = SIUSLookupRemote.getStatisticheSiusRemote();
        lElenco  = lCtrlStatSius.ExRicercaProcPosizioneGiuridica(lRicerca);
        
        if (lElenco != null)
            // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
            siesLogger.debug( "Risultati ricerca completa : " + lElenco.size() );
        
        fileOut = new ByteArrayOutputStream();    
        
        // Generazione file xls
        ProcPosizioneGiuridicaExcel lExcel = new ProcPosizioneGiuridicaExcel();
        lWb = lExcel.creaProcPosizioneGiuridica(getUfficioUtenteConnesso(), lRicerca, lElenco);
                
        try {
        	lWb.write(fileOut);
        } catch (IOException ioe){
            throw new F3BException( "ActRicercaProcPosizioneGiuridicaExcel.processRequest: " + ioe);           
        }
        
        setRequestAttribute("report", fileOut);
        setRequestAttribute(IWebConstants.DISPOSITION_FIELD,IWebConstants.ATTACHMENT_DISPOSITION_FILE); 

        // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
        siesLogger.debug( "" + getClass().getName() + " .processRequest: fine " );

        return IWebConstants.PG_DOWNLOAD_DOCUMENT;
	    
	}
}