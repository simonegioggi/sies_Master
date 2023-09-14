package siap.siep.sanzionesostitutiva.action;

import f3b.util.F3BException;
import siap.sico.web.ActionSiap;

/**
 * MEV_2023-33: aggiunta classe per caricamento griglia scadenzari
 *
 * @author df
 * @version 1.0
 */
public class ActGrigliaScadenzari extends ActionSiap implements ICostantiSanzioneSostitutiva {

	public String processRequest() throws F3BException {

		if (this.isSessionAttributeNullObj("fascicolo")) {
			// return ICostantiFascicoloSiep.REDIRECT_FASCICOLO_RICERCATO + getClass().getName();
			setRequestAttribute("fascicoloNotInSession", "S");
		}

		// pagina di ritorno
		return ICostantiSanzioneSostitutiva.PG_LOAD_GRIGLIA_SCADENZARI;
	}

}