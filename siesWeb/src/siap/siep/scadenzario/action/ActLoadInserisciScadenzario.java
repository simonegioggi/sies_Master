package siap.siep.scadenzario.action;

import siap.sico.web.ActionSiap;
import f3b.util.F3BException;

/**
 * <p>
 * Title: ActLoadInserisciScadenzario
 * </p>
 * <p>
 * Description: Classe Action per la load inserisci di Scadenzario
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: Bull
 * </p>
 * 
 * @version 1.0
 */
public class ActLoadInserisciScadenzario extends ActionSiap implements ICostantiScadenzario {

	public String processRequest() throws F3BException {

		// Imposta Modalità.
		setRequestAttribute("modalita", "I");
		return PG_LOAD_INSERISCISCADENZARIO; // restituisce la jsp di VIEW
	}

}