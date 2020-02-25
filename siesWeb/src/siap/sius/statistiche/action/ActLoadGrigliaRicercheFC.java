package siap.sius.statistiche.action;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */
import siap.sico.web.ActionSiap;

public class ActLoadGrigliaRicercheFC extends ActionSiap implements ICostantiStatistiche {
	public String processRequest() throws Exception {
		// MEV10-s3: aggiunto attributo nella richiesta per gestire visualizzazione griglia
		String codTipoUfficio = getUfficioUtenteConnesso().getCodTipoUfficio();
		setRequestAttribute("codTipoUfficio", codTipoUfficio);

		// valore di ritorno
		return PG_LOAD_GRIGLIA_RICERCHE_STAT;
	}
}