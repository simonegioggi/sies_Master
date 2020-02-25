package siap.sius.statistiche.action;

import siap.sius.ActionSius;

public class ActLoadRicercaProcPerProvviNoValidatiNoDeposito extends ActionSius 
implements ICostantiStatistiche {
	public String processRequest() throws Exception {
		return PG_LOAD_RICERCA_PROC_PROVV_NO_VALIDATI_NO_DEP;
	}
}