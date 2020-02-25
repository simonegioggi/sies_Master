package siap.sius.curatore.action;

/**
* <p>Title: ActRicercaCuratoreSius</p>
* <p>Description: Classe Action per la ricerca di Curatore/Tutore Fascicolo SIUS</p>
* <p>Copyright: Copyright (c) 2011</p>
* <p>Company: </p>
* @version 1.0
*/

import java.math.BigDecimal;
import java.util.Vector;

import siap.sius.ActionSius;
import siap.sius.curatore.controller.CuratoreSiusController;
import siap.sius.fascicolo.model.FascicoloGPModel;

public class ActRicercaCuratoreSius extends ActionSius implements ICostantiCuratoreSius {

	@SuppressWarnings("rawtypes")
	public String processRequest() throws Exception {

		// Attivazione punto di Ritorno
		setLinkRitorno();

		FascicoloGPModel lFasGPMod = (FascicoloGPModel) getSessionAttribute("fascicoloSiusGP");

		BigDecimal lIdFascicoloSius = lFasGPMod.getFascicoloSiusModel().getIdFascicoloSius();

		CuratoreSiusController lCtrl = new CuratoreSiusController();
		Vector lVect = lCtrl.ExRicercaCurSiusByFascicolo(lIdFascicoloSius,
				this.getCodUfficioUtenteConnesso());

		String lModificabile = "NO";

		if (this.IsFascicoloSiusModificabile() == true)
			lModificabile = "SI";
		else
			lModificabile = "NO";
		this.setRequestAttribute("isModificabile", lModificabile);

		setRequestAttribute("curatori", lVect);

		return PG_RICERCA_CURATORE_SIUS;
	}

}