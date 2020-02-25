package siap.regesies.regereato.action;

import java.util.Vector;

import f3b.util.F3BException;
import siap.regesies.action.ActionRegeSiap;
import siap.regesies.regereato.controller.IRegeReato;
import siap.regesies.util.RegeSiesLookupRemote;

/**
 * <p>
 * Title: ActRicercaRegeReato
 * </p>
 * <p>
 * Description: Classe Action per la ricerca di RegeReato
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
public class ActRicercaRegeReato extends ActionRegeSiap implements ICostantiRegeReato {

	public String processRequest() throws F3BException {

//		String lPage = "";

		isProvvedimentoRegeInSession();

		String lIdFile = getRequestStringParameter(CAMPO_ID_FILE);

//		RegeReatoModel lRegMod = new RegeReatoModel();

		IRegeReato lCtrl = RegeSiesLookupRemote.getRegeReatoRemote();
		Vector lVect = lCtrl.ExRicercaReatoCircostanzaByProvvedimento(lIdFile);
		setRequestAttribute("regereati", lVect);

		return PG_RICERCAREGEREATO;
	}

}