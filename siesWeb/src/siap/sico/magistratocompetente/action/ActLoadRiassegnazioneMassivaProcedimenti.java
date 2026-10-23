// 20261023-MEV_2026-105_INLAY - MEV Legge 140/2026
package siap.sico.magistratocompetente.action;

import f3b.util.F3BException;
import siap.sico.web.ActionSiap;

public class ActLoadRiassegnazioneMassivaProcedimenti extends ActionSiap
		implements ICostantiMagistratoCompetente {

	/**
	 * Caricamento della pagina di Riassegnazione massiva procedimenti
	 *
	 * @return Nome della pagina JSP da visualizzare al termine dell'elaborazione
	 * @throws F3BException
	 */
	public String processRequest() throws F3BException {
		return PG_LOAD_RIASSEGNAZIONEMASSIVA_PROCEDIMENTI;
	}
}
