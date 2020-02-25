package siap.sius.cancelleriaassegnataria.action;

import f3b.util.F3BException;

/**
 * <p>
 * Title: ActLoadRicercaCancelleriaAssegnataria
 * </p>
 * <p>
 * Description: Classe Action per la load ricerca di CancelleriaAssegnataria
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
public class ActLoadModificaCancelleriaAssegnataria extends ActLoadDettaglioCancelleriaAssegnataria implements
		ICostantiCancelleriaAssegnataria {

	public String processRequest() throws F3BException {

		ricercaCancelleriaAssegnataria();
		setRequestAttribute("modalita", "M");

		return PG_LOAD_INSERISCICANCELLERIAASSEGNATARIA; // restituisce la jsp di VIEW
	}

}