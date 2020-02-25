package siap.sico.cssa.action;

import java.util.Vector;

import siap.sico.cssa.controller.ICSSA;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import f3b.util.F3BException;

@SuppressWarnings("rawtypes")
public class ActLoadListaCSSA extends ActionSiap implements ICostantiCSSA {

	public String processRequest() throws F3BException {

		ICSSA lCtrl = SICOLookupRemote.getCSSARemote();

		Vector lCSSA = lCtrl.ListaCSSA();

		// imposta la risposta nella request
		setRequestAttribute("ListaCSSA", lCSSA);

		return PG_LISTACSSA; // restituisce la jsp di VIEW
	}

}