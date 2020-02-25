package siap.sige.fascicolo.action;

import siap.sige.web.ActionSige;

/**
 * MEV_65: aggiunta classe Action per la visualizzazione della maschera di Ricerca Procedimenti Sige Con
 * Ricorso od Opposizione.
 *
 * @author Gioggi
 * @version 1.0
 */
public class ActLoadRicercaProcedimentiSigeConRicorsoOpposizione extends ActionSige
		implements ICostantiFascicoloSige {

	public String processRequest() throws Exception {

		// Punto di ritorno
		setLinkRitorno();
		// recupero oggetti in sessione
		if (!isSessionAttributeNullObj("modelRO")) {
			setRequestAttribute("model", getSessionAttribute("modelRO"));
			removeSessionAttribute("modelRO");
		}

		// restituisce la jsp di VIEW
		return PG_LOAD_RICERCAPROCEDIMENTISIGE_CONRICORSOOPPOSIZIONE;
	}

}