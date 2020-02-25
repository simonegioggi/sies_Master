package siap.sius.penapecuniaria.action;

/**
* <p>Title: ActRicercaRichiestaConversione</p>
* <p>Description: Classe Action per la ricerca di Richiesta Conversione Pena Pecuniaria</p>
* <p>Copyright: Copyright (c) 2008</p>
* <p>Company: </p>
* @version 1.0
*/

import java.util.Vector;

import siap.siep.penapecuniaria.controller.RichiestaConversioneController;
import siap.sius.ActionSius;
import siap.sius.fascicolo.model.FascicoloGPModel;

public class ActRicercaSiusRichiestaConversione extends ActionSius implements ICostantiSiusPenaPecuniaria {

	@SuppressWarnings("rawtypes")
	public String processRequest() throws Exception {

		// Attivazione punto di Ritorno
		setLinkRitorno();

		FascicoloGPModel lFasGPMod = (FascicoloGPModel) getSessionAttribute("fascicoloSiusGP");

		// Lettura elenco Richieste di Conversione Collegate al Fascicolo SIUS.
		// RichiestaConversioneModel aRichiestaConversione = new RichiestaConversioneModel();
		// aRichiestaConversione.setFasSiuIdFascicoloSius(lFasGPMod.getFascicoloSiusModel().getIdFascicoloSius());
		RichiestaConversioneController lCtrl = new RichiestaConversioneController();
		Vector lVect = lCtrl.ExRicercaRichiesteConversionePenePecuniarieByIdFascicoloSius(
				lFasGPMod.getFascicoloSiusModel().getIdFascicoloSius());

		String lModificabile = "NO";
		if (this.IsFascicoloSiusModificabile() == true)
			lModificabile = "SI";
		else
			lModificabile = "NO";
		this.setRequestAttribute("isModificabile", lModificabile);

		setRequestAttribute("richiesteConversioni", lVect);

		return PG_RICERCA_SIUS_RICHIESTACONVERSIONE;
	}

}