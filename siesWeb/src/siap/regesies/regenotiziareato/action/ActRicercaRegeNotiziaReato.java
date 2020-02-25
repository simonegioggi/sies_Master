package siap.regesies.regenotiziareato.action;

import java.util.Vector;

import siap.regesies.regenotiziareato.controller.IRegeNotiziaReato;
import siap.regesies.util.RegeSiesLookupRemote;
import siap.sico.web.ActionSiap;
import f3b.util.F3BException;

/**
 * <p>
 * Title: ActRicercaRegeNotiziaReato
 * </p>
 * <p>
 * Description: Classe Action per la ricerca di RegeNotiziaReato
 * </p>
 * <p>
 * Copyright: Copyright (c) 2006
 * </p>
 * <p>
 * Company: Bull
 * </p>
 * 
 * @version 1.0
 */
@SuppressWarnings("rawtypes")
public class ActRicercaRegeNotiziaReato extends ActionSiap implements ICostantiRegeNotiziaReato {

	public String processRequest() throws F3BException {

		String lIdFile = getRequestStringParameter(CAMPO_ID_FILE);

		IRegeNotiziaReato lCtrl = RegeSiesLookupRemote.getRegeNotiziaReatoRemote();
		Vector lVect = lCtrl.ExRicercaRegeNotiziaReato(lIdFile);
		setRequestAttribute("regenotizieReati", lVect);

		return PG_RICERCAREGENOTIZIAREATO;
	}

}