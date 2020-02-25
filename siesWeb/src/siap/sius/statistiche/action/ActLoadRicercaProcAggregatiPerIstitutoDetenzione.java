package siap.sius.statistiche.action;

import siap.sico.web.ActionSiap;

public class ActLoadRicercaProcAggregatiPerIstitutoDetenzione extends ActionSiap implements 
	ICostantiStatistiche {

    public String processRequest() throws Exception {
    	String lTitle 	= 
    			"Estrazioni Procedimenti Iscritti Aggregati per Istituto Detenzione";
    	String lAction 	= 
    			"siap.sius.statistiche.action.ActRicercaProcAggregatiPerIstitutoDetenzioneExcel";
    	
    	setRequestAttribute("title", lTitle);
    	setRequestAttribute("action", lAction);
    	
        return PG_LOAD_RICERCHE_PROC_AGGREGATI;
    }
}
