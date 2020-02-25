package siap.sico.cssa.action;

import siap.sico.web.ActionSiap;
import f3b.util.F3BException;

public class ActLoadListaCSSAFiltroComune extends ActionSiap implements ICostantiCSSA {

	public String processRequest() throws F3BException {
	
		return PG_LISTACSSA_FILTROCOMUNE; // restituisce la jsp di VIEW
	}

}
