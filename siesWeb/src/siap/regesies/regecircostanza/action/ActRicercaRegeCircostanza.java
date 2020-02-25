package siap.regesies.regecircostanza.action;

import java.util.Vector;

import siap.regesies.regecircostanza.controller.IRegeCircostanza;
import siap.regesies.util.RegeSiesLookupRemote;
import siap.sico.web.ActionSiap;
import f3b.util.F3BException;

/**
 * <p>
 * Title: ActRicercaRegeCircostanza
 * </p>
 * <p>
 * Description: Classe Action per la ricerca di RegeCircostanza
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: Bull
 * </p>
 */
@SuppressWarnings("rawtypes")
public class ActRicercaRegeCircostanza extends ActionSiap implements ICostantiRegeCircostanza {

	public String processRequest() throws F3BException {

		String lId = getRequestStringParameter(CAMPO_ID_FILE);

		IRegeCircostanza lCtrl = RegeSiesLookupRemote.getRegeCircostanzaRemote();
		Vector lVect = lCtrl.ExRicercaRegeCircostanza(lId);

		setRequestAttribute("regecircostanze", lVect);

		return PG_RICERCAREGECIRCOSTANZA;
	}

}