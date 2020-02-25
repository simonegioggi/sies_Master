package siap.sico.w_magistrato.action;

import java.util.Vector;

import siap.sico.util.SICOLookupRemote;
import siap.sico.w_magistrato.controller.IWMagistrato;
import siap.sico.web.ActionSiap;
import f3b.util.F3BException;
import f3b.util.StringUtils;

/**
 * <p>
 * Title: ActRicercaWMagistrato
 * </p>
 * <p>
 * Description: Classe Action per la ricerca di WMagistrato
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
@SuppressWarnings("rawtypes")
public class ActRicercaWMagistratoComp extends ActionSiap implements ICostantiWMagistrato {

	public String processRequest() throws F3BException {

		String lCognome;

		IWMagistrato lCtrl = SICOLookupRemote.getWMagistratoRemote();
		lCognome = StringUtils.convertSqlString((getRequestStringParameter(CAMPO_COGNOME)).toUpperCase());
		Vector lVect = lCtrl.ExRicercaWMagistratoByCognome(lCognome);
		setRequestAttribute("formname", getRequestStringParameter("formname"));
		setRequestAttribute("wmagistrati", lVect);
		return PG_RICERCAWMAGISTRATO_COMP;
	}

}