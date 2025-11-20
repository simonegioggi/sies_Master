package siap.sius.esecuzionemisuraalternativa.action;

import siap.sico.web.ActionSiap;

/**
 * ActLoadRicercaDataScadenzaProcedimentiEsecuzioneMA - Classe Action per il caricamento della pagina di
 * ricerca data scadenza Procedimenti Esecuzione Misura Alternativa
 *
 * @since MEV_2025-48
 * @author sgioggi
 * @version 1.0
 */
public class ActLoadRicercaDataScadenzaProcedimentiEsecuzioneMA extends ActionSiap
		implements ICostantiEsecuzioneMA {

	public String processRequest() throws Exception {

		// restituisce la jsp di VIEW
		return PG_LOAD_RICERCA_DATA_SCADENZA_PROCEDIMENTI_ESECUZIONE_MA;
	}

}