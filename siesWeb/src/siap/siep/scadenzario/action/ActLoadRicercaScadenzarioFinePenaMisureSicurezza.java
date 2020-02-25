package siap.siep.scadenzario.action;

import siap.sico.web.ActionSiap;
import f3b.util.F3BException;

/**
 * <p>
 * Title: ActLoadRicercaScadenzarioFinePenaMisureSicurezza
 * </p>
 * <p>
 * Description: Classe Action per la load ricerca di Scadenzario per
 * </p>
 * <p>
 * Fine Pena di procedimenti di Misure Sicurezza
 * </p>
 * <p>
 * Copyright: Copyright (c) 2015
 * </p>
 * <p>
 * Company: IntersistemiItalia
 * </p>
 * 
 * @version 8.3
 */
public class ActLoadRicercaScadenzarioFinePenaMisureSicurezza extends ActionSiap implements
		ICostantiScadenzario {

	public String processRequest() throws F3BException {

		return PG_LOAD_RICERCASCADENZARIOFINEPENA_MS; // restituisce la jsp di VIEW
	}

}