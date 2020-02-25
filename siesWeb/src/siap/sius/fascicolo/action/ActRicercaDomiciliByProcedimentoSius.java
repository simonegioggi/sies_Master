package siap.sius.fascicolo.action;

/**
* <p>Title: ActRicercaDomiciliByProcedimentoSius</p>
* <p>Description: Classe Action per la ricerca di Domicili</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.math.BigDecimal;
import java.util.Vector;

import siap.sius.ActionSius;
import siap.sius.fascicolo.controller.FascicoloSiusController;
import siap.sius.fascicolo.model.FascicoloGPModel;

public class ActRicercaDomiciliByProcedimentoSius extends ActionSius implements ICostantiFascicoloSius {

	@SuppressWarnings("rawtypes")
	public String processRequest() throws Exception {

		// Attivazione punto di Ritorno
		setLinkRitorno();

		FascicoloGPModel lFasGPMod = (FascicoloGPModel) getSessionAttribute("fascicoloSiusGP");

		BigDecimal lIdFascicoloSius = lFasGPMod.getFascicoloSiusModel().getIdFascicoloSius();

		// 'Residenza' (R), 'Domicilio' (D)
		FascicoloSiusController lCtrl = new FascicoloSiusController();
		Vector lVect = lCtrl.ExRicercaResidenzaByProcedimentoSius(lIdFascicoloSius, 'D');

		// Controlla se il fascicolo e' modificabile
		String lModificabile = "NO";
		if (this.IsFascicoloSiusModificabile() == true)
			lModificabile = "SI";
		else
			lModificabile = "NO";
		this.setRequestAttribute("isModificabile", lModificabile);

		setRequestAttribute("residenze", lVect);

		return PG_RICERCADOMICILISIUS;
	}

}