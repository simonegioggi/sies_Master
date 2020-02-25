package siap.siep.statoprocedimento.action;

import java.util.Vector;

import f3b.util.F3BException;
import siap.sico.web.ActionSiap;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.statoprocedimento.controller.IStatoProcedimento;
import siap.siep.util.SIEPLookupRemote;

/**
 * <p>
 * Title: ActLoadDettaglioStatoProcedimento
 * </p>
 * <p>
 * Description: Classe Action
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
public class ActLoadDettaglioStatoProcedimento extends ActionSiap implements ICostantiStatoProcedimento {

	@SuppressWarnings("rawtypes")
	public String processRequest() throws F3BException {

		FascicoloSiepModel lFascMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");

		Vector lStato = new Vector();
		IStatoProcedimento lCtrl = SIEPLookupRemote.getStatoProcedimentoRemote();
		lStato = lCtrl.ExRicercaStatoProcedimentoByFascicoloSiep(lFascMod.getIdFascicoloSiep());

		this.setRequestAttribute("statoprocedimento", lStato);

		return PG_LOAD_DETTAGLIO_STATOPROCEDIMENTO;
	}

}