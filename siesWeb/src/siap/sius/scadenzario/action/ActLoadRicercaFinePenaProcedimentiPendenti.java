package siap.sius.scadenzario.action;

import siap.sico.web.ActionSiap;

/**
 * ActLoadRicercaFinePenaProcedimentiPendenti - Classe Action per la load Ricerca Fine Pena Procedimenti
 * Pendenti di Scadenzario SIUS
 *
 * @author sgioggi
 * @since MEV_2026-1
 * @version 1.0
 */
public class ActLoadRicercaFinePenaProcedimentiPendenti extends ActionSiap
		implements ICostantiScadenzarioSius {

	public String processRequest() throws Exception {

		// navigazione
		setLinkRitorno();

		// restituisce la jsp di VIEW
		return PG_LOAD_RICERCAFINEPENAPROCEDIMENTIPENDENTI;
	}

}