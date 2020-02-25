package siap.siep.statistiche.action;

import siap.sico.web.ActionSiap;

public class ActLoadGrigliaRicercheFC extends ActionSiap implements ICostantiStatistiche {

	public String processRequest() throws Exception {

		// valore di ritorno
		return PG_LOAD_GRIGLIA_RICERCHE_STAT;
	}

}