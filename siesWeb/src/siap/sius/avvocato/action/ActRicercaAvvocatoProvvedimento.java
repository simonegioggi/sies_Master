package siap.sius.avvocato.action;

import java.util.Vector;

import siap.sius.ActionSius;
//import siap.sius.avvocato.controller.AvvocatoController;
import siap.sius.avvocato.controller.IAvvocato;
import siap.sius.avvocato.model.AvvocatoFascicoloSiusModel;
import siap.sius.fascicolo.model.FascicoloGPModel;
import siap.sius.util.SIUSLookupRemote;
import f3b.util.F3BException;

/**
 * <p>
 * Title: ActRicercaAvvocatoProvvedimento
 * </p>
 * <p>
 * Description: Classe Action per la ricerca di un Avvocato Legato ad un Provvedimento
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
public class ActRicercaAvvocatoProvvedimento extends ActionSius implements ICostantiAvvocato {

	@SuppressWarnings("rawtypes")
	public String processRequest() throws F3BException {

//		AvvocatoModel lAvvMod = new AvvocatoModel();
		AvvocatoFascicoloSiusModel lAvvFascMod = new AvvocatoFascicoloSiusModel();

		FascicoloGPModel FasGPMod = (FascicoloGPModel) getSessionAttribute("fascicoloSiusGP");
		lAvvFascMod.setFasSiuIdFascicoloSius(FasGPMod.getFascicoloSiusModel().getIdFascicoloSius());

		// AvvocatoController lCtrl = new AvvocatoController();
		IAvvocato lCtrl = SIUSLookupRemote.getAvvocatoRemote();

		Vector lVect = lCtrl.ExRicercaAvvocatoProvvedimento(lAvvFascMod);

		// Controlla se il fascicolo e' modificabile
		String lModificabile = "NO";
		if (this.IsFascicoloSiusModificabile() == true)
			lModificabile = "SI";
		else
			lModificabile = "NO";
		this.setRequestAttribute("isModificabile", lModificabile);

		setRequestAttribute("avvocato", lVect);

		String lPage = PG_RICERCAAVVOCATOPROVVEDIMENTO;

		return lPage;
	}

}