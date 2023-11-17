package siap.siep.scadenzario.action;

import siap.sico.web.ActionSiap;

public class ActLoadRicercaScadenzarioStatoPagamentiPP extends ActionSiap implements ICostantiScadenzario {

	public String processRequest() throws Exception {

		if (this.isSessionAttributeNullObj("fascicolo"))
			setRequestAttribute("fascicoloNotInSession", "S");

		// pagina di ritorno
		return PG_LOAD_RICERCA_SCADENZARI_PP;
	}

}