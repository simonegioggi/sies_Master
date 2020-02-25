package siap.sius.statistiche.action;

import siap.sico.web.ActionSiap;

public class ActLoadRicercaProcDataUdienzaFissataNoDefinitiNumGG extends ActionSiap implements 
	ICostantiStatistiche {

    public String processRequest() throws Exception {
    	
        return PG_LOAD_RICERCA_PROC_DATA_UDIENZA_FISSATA_NO_DEF_NUMGG;
    }
}
