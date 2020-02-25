package siap.siep.provvedimentopm.action;

/**
* <p>Title: ActLoadDettaglioProvvedimento</p>
* <p>Description: Classe Action per la load dettaglio di Provvedimento</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.math.BigDecimal;
import java.util.Vector;

import f3b.util.F3BException;
import siap.sico.web.ActionSiap;
import siap.siep.provvedimentopm.controller.ProvvedimentoController;
import siap.siep.provvedimentopm.model.ProvvedimentoModel;

public class ActLoadDettaglioProvvedimento extends ActionSiap implements ICostantiProvvedimento {

	@SuppressWarnings("rawtypes")
	public String processRequest() throws F3BException {

		String lId = getRequestStringParameter(CAMPO_ID_PROVVEDIMENTO);
		// riempie il model
		ProvvedimentoModel lProMod = new ProvvedimentoModel();
		lProMod.setIdProvvedimento(new BigDecimal(lId));
		// chiama il controller
		ProvvedimentoController lCtrl = new ProvvedimentoController();
		Vector lVect = lCtrl.ExRicercaProvvedimento(lProMod);
		setRequestAttribute("modalita", "D");
		setRequestAttribute("provvedimento", lVect);

		return PG_LOAD_INSERISCIPROVVEDIMENTO;
	}

}