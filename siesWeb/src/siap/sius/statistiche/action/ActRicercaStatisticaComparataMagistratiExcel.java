package siap.sius.statistiche.action;

import java.io.ByteArrayOutputStream;

import siap.sico.web.ActionSiap;
import siap.sius.statistiche.model.RicercaProcedimentoModel;
import siap.sius.statistiche.util.excel.StatisticaComparataMagistratiExcel;
import f3b.web.IWebConstants;

public class ActRicercaStatisticaComparataMagistratiExcel extends ActionSiap implements ICostantiStatistiche {
    public String processRequest() throws Exception {
        RicercaProcedimentoModel              lRicercaModel   = null;
        ByteArrayOutputStream                 lFileOut        = null;   
        
        lRicercaModel = (RicercaProcedimentoModel) getSessionAttribute("ricercaProcedimento");
        
        StatisticaComparataMagistratiExcel lExcel = new StatisticaComparataMagistratiExcel();
        lFileOut = lExcel.creaStatisticaComparataMagistratiExcel(lRicercaModel, getUfficioUtenteConnesso());
        
        setRequestAttribute("report", lFileOut);
        setRequestAttribute(IWebConstants.DISPOSITION_FIELD,IWebConstants.ATTACHMENT_DISPOSITION_FILE); 

        return IWebConstants.PG_DOWNLOAD_DOCUMENT;
    }

}
