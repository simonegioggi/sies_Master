package siap.sige.impugnazione.action;

import siap.sige.web.ActionSige;
import f3b.web.IWebConstants;

public class ActLoadGrigliaMenuImpugnazioni extends ActionSige implements ICostantiImpugnazioneSige {
	public String processRequest() throws Exception {
		super.setLinkRitorno();
		setRequestAttribute(IWebConstants.LINK_RITORNO, "20");
	    return PG_GRIGLIA_GESTIONE_IMPUGNAZIONI;
	}
}
