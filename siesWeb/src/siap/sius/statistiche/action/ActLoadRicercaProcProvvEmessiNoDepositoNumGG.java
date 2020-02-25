package siap.sius.statistiche.action;

import siap.sico.web.ActionSiap;

public class ActLoadRicercaProcProvvEmessiNoDepositoNumGG extends ActionSiap implements 
	ICostantiStatistiche {

    public String processRequest() throws Exception {
        return PG_LOAD_RICERCA_PROC_PROVV_EMESSI_NO_DEPOSITO_NUMGG;
    }
}
