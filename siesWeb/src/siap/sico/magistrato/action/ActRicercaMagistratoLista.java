package siap.sico.magistrato.action;

import java.util.Vector;

import siap.sico.magistrato.controller.IMagistrato;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import f3b.util.F3BException;
import f3b.util.StringUtils;

/**
 * <p>
 * Title: ActRicercaMagistratoLista
 * </p>
 * <p>
 * Description: Classe Action per la ricerca di Magistrato
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
public class ActRicercaMagistratoLista extends ActionSiap implements ICostantiMagistrato {

	public String processRequest() throws F3BException {

		String lCognome = null;
		String lUffAppa = null;

		IMagistrato lCtrl = SICOLookupRemote.getMagistratoRemote();
		lCognome = StringUtils.convertSqlString((getRequestStringParameter(CAMPO_COGNOME)).toUpperCase());
		lUffAppa = this.getCodUfficioUtenteConnesso();

		Vector lVect = lCtrl.ExRicercaMagistratoByCognome(lCognome, lUffAppa);

		setRequestAttribute("formname", getRequestStringParameter("formname"));
		setRequestAttribute("fieldname", getRequestStringParameter("fieldname"));
		setRequestAttribute("magistrati", lVect);

		return PG_RICERCA_MAGISTRATO_LISTA;
	}

}