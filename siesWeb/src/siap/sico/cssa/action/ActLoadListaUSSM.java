package siap.sico.cssa.action;

import java.util.Vector;

import siap.sico.cssa.controller.ICSSA;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import f3b.util.F3BException;

@SuppressWarnings("rawtypes")
public class ActLoadListaUSSM extends ActionSiap implements ICostantiCSSA {

	public String processRequest() throws F3BException {

		ICSSA lCtrl = SICOLookupRemote.getCSSARemote();

		Vector lUSSM = lCtrl.ListaUSSM();

		// imposta la risposta nella request
		setRequestAttribute("ListaCSSA", lUSSM);

		// return PG_LISTACSSA; //restituisce la jsp di VIEW
		return PG_LISTAUSSM; // restituisce la jsp di VIEW
	}

}