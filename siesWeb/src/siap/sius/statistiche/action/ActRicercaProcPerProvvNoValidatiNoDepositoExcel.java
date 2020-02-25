package siap.sius.statistiche.action;


import java.io.ByteArrayOutputStream;

import org.apache.log4j.Logger;

//import f3b.util.StringUtils;
//import freemarker.core.ArithmeticEngine.BigDecimalEngine;
import siap.sius.ActionSius;
//import siap.sius.statistiche.model.EveFasGepSogProvPosGiuModel;
//import siap.sius.statistiche.model.RicercaProcPerProvvNoValidatiNoDepositoModel;
import siap.sius.statistiche.model.RicercaProcedimentoModel;
import siap.sius.statistiche.util.excel.ProcPerProvvNoValidatiNoDepositoExcel;
import f3b.log.LogF3B;
import f3b.web.IWebConstants;
//import f3b.web.RedirectTo;
//import siap.sius.depositoordinanzapc.action.ActRicercaDepositoOrdinanzaPcByAnnoNumUff;
//import siap.sius.depositoordinanzapc.action.ICostantiDepositoOrdinanzaPc;
//import siap.sius.impugnazione.model.ImpugnazioneModel;
//import siap.sius.impugnazione.action.ICostantiImpugnazione;
//import siap.sico.decodifiche.controller.DecodificheManager;
//import siap.sico.decodifiche.util.DecodificheUtils;
//import siap.sico.evento.action.ICostantiEvento;


/**
 * <p>Title: ActRicercaImpugnazioneByAnnoNumUff</p>
 * <p>Description: Classe Action per la ricerca del Ricorso/Impugnazione 
 * da Anno, Num e codice Ufficio di Inserimento.</p>
 * <p>La classe è stata ottenuta specializzando la ActRicercaDepositoOrdinanzaPc 
 * per riutilizzare la stessa funzione per la ricerca dei dati da inserire in session.</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: Bull</p>
 * @version 1.0
 */

public class ActRicercaProcPerProvvNoValidatiNoDepositoExcel extends ActionSius implements ICostantiStatistiche {
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
	public String processRequest() throws Exception {
        RicercaProcedimentoModel              lRicercaModel   = null;
        ByteArrayOutputStream                 lFileOut        = null;   
                
        lRicercaModel = (RicercaProcedimentoModel) getSessionAttribute("ricercaProcedimenti");
        
        ProcPerProvvNoValidatiNoDepositoExcel lExcel = new ProcPerProvvNoValidatiNoDepositoExcel();
        lFileOut = lExcel.creaFoglioProcPerProvvNoValidatiNoDeposito(getUfficioUtenteConnesso(), lRicercaModel); 
        
        setRequestAttribute("report", lFileOut);
        setRequestAttribute(IWebConstants.DISPOSITION_FIELD,IWebConstants.ATTACHMENT_DISPOSITION_FILE); 

        // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
        siesLogger.debug(getClass().getName() + " .processRequest: fine " );

        return IWebConstants.PG_DOWNLOAD_DOCUMENT;
    }
}