package siap.siep.parametro.action;

import java.util.Vector;

import f3b.util.F3BException;
//import per le combo
//import f3b.web.html.Option;
//import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.web.ActionSiap;
import siap.siep.parametro.controller.IParametro;
import siap.siep.parametro.model.ParametroModel;
import siap.siep.util.SIEPLookupRemote;

/**
 * <p>
 * Title: ActLoadInserisciParametro
 * </p>
 * <p>
 * Description: Classe Action per la load inserisci di Parametro
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
public class ActLoadInserisciParametro extends ActionSiap implements ICostantiParametro {

	@SuppressWarnings("rawtypes")
	public String processRequest() throws F3BException {

		ParametroModel lParModVR = new ParametroModel();
		lParModVR.setCodUfficioValidita(getCodUfficioUtenteConnesso());
		lParModVR.setNomeParametro("VANE RICERCHE");
		IParametro lCtrlPar = SIEPLookupRemote.getParametroRemote();
		Vector lParVectVR = lCtrlPar.ExRicercaParametroUfficioConnesso(lParModVR);
		if (lParVectVR.size() > 0) {
			lParModVR = (ParametroModel) lParVectVR.firstElement();
			setRequestAttribute("lParModVR", lParModVR);
		}

		ParametroModel lParModVRP = new ParametroModel();
		lParModVRP.setCodUfficioValidita(getCodUfficioUtenteConnesso());
		lParModVRP.setNomeParametro("VANE RICERCHE PERVENUTO");
		Vector lParVectVRP = lCtrlPar.ExRicercaParametroUfficioConnesso(lParModVRP);
		if (lParVectVRP.size() > 0) {
			lParModVRP = (ParametroModel) lParVectVRP.firstElement();
			setRequestAttribute("lParModVRP", lParModVRP);
		}

		setRequestAttribute("modalita", "I");
		return PG_LOAD_INSERISCIPARAMETRO; // restituisce la jsp di VIEW
	}

}