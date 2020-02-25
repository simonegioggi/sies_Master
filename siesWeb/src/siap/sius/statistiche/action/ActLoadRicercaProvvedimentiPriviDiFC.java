package siap.sius.statistiche.action;

import siap.sico.web.ActionSiap;

/**
 * <p>Title: ActLoadRicercaProvvedimentiPriviDiFC</p>
 * <p>Description: Visualizza la form di "Ricerca Provvedimenti Privi di Fogli Complementari".
 * </p>
 * <p>Copyright: Copyright (c) 2007</p>
 * <p>Company: EII</p>
 * 
 * @author Simone Gioggi
 * @since MEV10-s3
 * @version 1.0
 */
public class ActLoadRicercaProvvedimentiPriviDiFC extends ActionSiap implements ICostantiStatistiche {
	public String processRequest() throws Exception {
		setRequestAttribute("modalitaRicerca", RICERCA_PROVVEDIMENTI_PRIVI_DI_FC);

		return PG_LOAD_RICERCA_PROVV_PRIVI_DI_FC;
	}
}