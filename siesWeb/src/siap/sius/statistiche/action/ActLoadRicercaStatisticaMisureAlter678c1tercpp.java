package siap.sius.statistiche.action;

import siap.sius.ActionSius;

// MEV_2019-09: aggiunta classe per le statistiche
public class ActLoadRicercaStatisticaMisureAlter678c1tercpp extends ActionSius
		implements ICostantiStatistiche {

	public String processRequest() throws Exception {

		return PG_LOAD_RICERCA_STATISTICA_MISURE_ALTER_678C1TERCPP;
	}

}