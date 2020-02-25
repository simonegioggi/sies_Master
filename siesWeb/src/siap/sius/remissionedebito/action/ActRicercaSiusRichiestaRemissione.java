package siap.sius.remissionedebito.action;

/**
* <p>Title: ActRicercaRichiestaRemissione</p>
* <p>Description: Classe Action per la ricerca di Richiesta Remissione Debito</p>
* <p>Copyright: Copyright (c) 2008</p>
* <p>Company: </p>
* @version 1.0
*/

import java.util.Vector;

import siap.sius.ActionSius;
import siap.sius.fascicolo.model.FascicoloGPModel;
import siap.sius.remissionedebito.controller.RichiestaRemissioneController;
import siap.sius.remissionedebito.model.RichiestaRemissioneModel;

public class ActRicercaSiusRichiestaRemissione extends ActionSius implements ICostantiSiusRemissioneDebito {

	@SuppressWarnings("rawtypes")
	public String processRequest() throws Exception {

		// Attivazione punto di Ritorno
		setLinkRitorno();

		FascicoloGPModel lFasGPMod = (FascicoloGPModel) getSessionAttribute("fascicoloSiusGP");

		// Lettura elenco Richieste di Remissione Collegate al Fascicolo SIUS.
		RichiestaRemissioneModel aRichiestaRemissione = new RichiestaRemissioneModel();
		aRichiestaRemissione.setFasSiuIdFascicoloSius(lFasGPMod.getFascicoloSiusModel().getIdFascicoloSius());
		RichiestaRemissioneController lCtrl = new RichiestaRemissioneController();
		Vector lVect = lCtrl.ExRicercaRichiesteRemissioneDebito(aRichiestaRemissione);

		String lModificabile = "NO";
		if (this.IsFascicoloSiusModificabile() == true)
			lModificabile = "SI";
		else
			lModificabile = "NO";
		this.setRequestAttribute("isModificabile", lModificabile);

		setRequestAttribute("richiesteRemissioni", lVect);

		return PG_RICERCA_SIUS_RICHIESTAREMISSIONE;
	}

}