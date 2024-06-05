package siap.sius.esecuzionesanzionesostitutiva.action;

import siap.sico.web.ActionSiap;

/**
 * Ricerca Esecuzione Sanzioni Sostitutive - Classe per il caricamento della pagina di ricerca Esecuzione
 * Sanzioni Sostituive
 *
 * @version 1.0
 */
public class ActLoadRicercaEsecuzioneSS extends ActionSiap implements ICostantiEsecuzioneSS {

	public String processRequest() throws Exception {

		// MEV_2023-35 si aggiunge il parametro sul contebuto per gestire anche le EPS
		setRequestAttribute("ContenutoES", "U019");
		return PG_LOAD_RICERCA_ESECUZIONE_SS;
	}

}