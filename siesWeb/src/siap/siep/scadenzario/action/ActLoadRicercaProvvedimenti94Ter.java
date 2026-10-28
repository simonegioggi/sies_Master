// 20261028-MEV_2026-105_INLAY - MEV Legge 140/2026: ricerca provvedimenti emessi ex art. 94 ter DPR 309/90
package siap.siep.scadenzario.action;

import siap.sico.web.ActionSiap;
import f3b.util.F3BException;

/**
 * Carica la form di ricerca dei provvedimenti emessi ex art. 94 ter DPR 309/90.
 */
public class ActLoadRicercaProvvedimenti94Ter extends ActionSiap implements ICostantiScadenzario {

	public String processRequest() throws F3BException {
		return PG_LOAD_RICERCA_PROVVEDIMENTI_94TER;
	}
}
