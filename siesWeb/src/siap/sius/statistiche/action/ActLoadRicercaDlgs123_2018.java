package siap.sius.statistiche.action;

import siap.sico.web.ActionSiap;

public class ActLoadRicercaDlgs123_2018 extends ActionSiap implements ICostantiStatistiche {

	public String processRequest() throws Exception {

		String lTitle = "Estrazioni Procedimenti Dlgs 123/2018";
		String lAction = "siap.sius.statistiche.action.ActRicercaProcDlgs123_2018Excel";

		setRequestAttribute("title", lTitle);
		setRequestAttribute("action", lAction);

		return PG_LOAD_RICERCHE_DLGS_123_2018;
	}

}