package siap.sige.fascicolo.action;

/**
* <p>Title: ActRicercaDomiciliByProcedimentoSige</p>
* <p>Description: Classe Action per la ricerca di Domicili</p>
* <p>Copyright: Copyright (c) 2008</p>
* <p>Company: </p>
* @version 1.0
*/

import java.math.BigDecimal;
import java.util.Vector;

import siap.sige.fascicolo.controller.FascicoloSigeController;
import siap.sige.fascicolo.model.FascicoloSigeEstesoModel;
import siap.sige.web.ActionSige;

public class ActRicercaDomiciliByProcedimentoSige extends ActionSige implements ICostantiFascicoloSige {

	@SuppressWarnings("rawtypes")
	public String processRequest() throws Exception {

		// Attivazione punto di Ritorno
		setLinkRitorno();

		FascicoloSigeEstesoModel lFasSigeEsteso = (FascicoloSigeEstesoModel) getSessionAttribute(
				"FascicoloSigeEsteso");

		BigDecimal lIdFascicoloSige = lFasSigeEsteso.getFascicoloSige().getIdFascicoloSige();

		// 'Residenza' (R), 'Domicilio' (D)
		FascicoloSigeController lCtrl = new FascicoloSigeController();
		Vector lVect = lCtrl.ExRicercaResidenzaByProcedimentoSige(lIdFascicoloSige, 'D');

		String lModificabile = "NO";

		if (IsFascicoloSigeModificabile())
			lModificabile = "SI";
		else
			lModificabile = "NO";

		setRequestAttribute("isModificabile", lModificabile);

		setRequestAttribute("residenze", lVect);

		return PG_RICERCADOMICILISIGE;
	}

}