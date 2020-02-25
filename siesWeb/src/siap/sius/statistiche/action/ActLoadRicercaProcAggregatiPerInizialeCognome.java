package siap.sius.statistiche.action;

import siap.sico.web.ActionSiap;

public class ActLoadRicercaProcAggregatiPerInizialeCognome extends ActionSiap implements 
	ICostantiStatistiche {

    public String processRequest() throws Exception {
    	String lTitle 	= 
    			"Estrazioni Procedimenti Iscritti Aggregati per Lettera Iniziale Cognome Soggetto";
    	String lAction 	= 
    			"siap.sius.statistiche.action.ActRicercaProcAggregatiPerInizialeCognome";
    	
    	setRequestAttribute("title", lTitle);
    	setRequestAttribute("action", lAction);
    	
        return PG_LOAD_RICERCHE_PROC_AGGREGATI;
    }
}
