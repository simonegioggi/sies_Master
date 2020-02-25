package siap.sius.statistiche.action;

import siap.sico.web.ActionSiap;

public class ActLoadRicercaProcAggregatiPerProcuraMittente extends ActionSiap implements 
	ICostantiStatistiche {

    public String processRequest() throws Exception {
    	String lTitle 	= 
    			"Estrazioni Procedimenti Iscritti Aggregati per Procura Mittente";
    	String lAction 	= 
    			"siap.sius.statistiche.action.ActRicercaProcAggregatiPerProcuraMittenteExcel";
    	
    	setRequestAttribute("title", lTitle);
    	setRequestAttribute("action", lAction);
    	
        return PG_LOAD_RICERCHE_PROC_AGGREGATI;
    }
}
