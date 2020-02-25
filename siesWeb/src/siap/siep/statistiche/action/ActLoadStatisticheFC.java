package siap.siep.statistiche.action;

import siap.sico.web.ActionSiap;

public class ActLoadStatisticheFC extends ActionSiap implements ICostantiStatistiche{
	public String processRequest() throws Exception{
		super.setLinkRitorno();  
		return PG_LOAD_RICERCA_STATISTICHE_FC;
	}
}
