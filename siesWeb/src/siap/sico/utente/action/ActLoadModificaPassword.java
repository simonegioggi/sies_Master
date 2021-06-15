package siap.sico.utente.action;

import f3b.util.F3BException;
import siap.sico.security.action.ICostantiSecurity;
import siap.sico.web.ActionSiap;

/**
 * <p>
 * Title: ActLoadModificaPassword
 * </p>
 * <p>
 * Description: Classe Action per la load della pagina di modifica psw
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
public class ActLoadModificaPassword extends ActionSiap implements ICostantiUtente {

	public String processRequest() throws F3BException {

		return ICostantiSecurity.PG_CHANGE_PASSWORD;
	}

}