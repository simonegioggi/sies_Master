package siap.sige.fascicolo.action;

/**
* <p>Title: ActRicercaResidenzaByProcedimentoSige</p>
* <p>Description: Classe Action per la ricerca di Residenza Fascicolo SIGE</p>
* <p>Copyright: Copyright (c) 2008</p>
* <p>Company: </p>
* @version 1.0
*/

import java.math.BigDecimal;
import java.util.Vector;

import siap.sige.fascicolo.controller.FascicoloSigeController;
import siap.sige.fascicolo.model.FascicoloSigeEstesoModel;
import siap.sige.web.ActionSige;

public class ActRicercaResidenzaByProcedimentoSige extends ActionSige implements ICostantiFascicoloSige {

	@SuppressWarnings("rawtypes")
	public String processRequest() throws Exception {

		// Attivazione punto di Ritorno
		setLinkRitorno();

		FascicoloSigeEstesoModel lFasEsteso = (FascicoloSigeEstesoModel) getSessionAttribute(
				"FascicoloSigeEsteso");

		BigDecimal lIdFascicoloSige = lFasEsteso.getFascicoloSige().getIdFascicoloSige();

		// 'Residenza' (R), 'Domicilio' (D)
		FascicoloSigeController lCtrl = new FascicoloSigeController();
		Vector lVect = lCtrl.ExRicercaResidenzaByProcedimentoSige(lIdFascicoloSige, 'R');

		String lModificabile = "NO";

		if (IsFascicoloSigeModificabile())
			lModificabile = "SI";
		else
			lModificabile = "NO";

		setRequestAttribute("isModificabile", lModificabile);

		setRequestAttribute("residenze", lVect);

		return PG_RICERCARESIDENZASIGE;
	}

}