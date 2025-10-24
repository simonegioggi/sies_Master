package siap.sius.statistiche.action;

import siap.sius.ActionSius;

/**
 * 
 * @author d.fiorletta
 * @since MEV 9
 */
public class ActLoadRicercaAttiIstruttoriDataRest extends ActionSius 
implements ICostantiStatistiche {
	public String processRequest() throws Exception {
		return PG_LOAD_ATTI_ISTRUTTORI_DATA_REST;
	}
}