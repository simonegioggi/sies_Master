package siap.sius.avvocato.action;

import java.util.Vector;

import siap.sius.ActionSius;
//import siap.siep.avvocato.controller.AvvocatoController;
import siap.sius.avvocato.controller.IAvvocato;
import siap.sius.avvocato.model.AvvocatoFascicoloSiusModel;
import siap.sius.avvocato.model.AvvocatoModel;
import siap.sius.fascicolo.model.FascicoloGPModel;
import siap.sius.util.SIUSLookupRemote;

/**
 * <p>
 * Title: ActRicercaAvvocato
 * </p>
 * <p>
 * Description: Classe Action per la ricerca di Avvocato
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
public class ActRicercaAvvocatoFascicoloSius extends ActionSius implements ICostantiAvvocato {

	@SuppressWarnings("rawtypes")
	public String processRequest() throws Exception {

		this.gestioneRitorno();

		AvvocatoModel lAvvMod = new AvvocatoModel();
		AvvocatoFascicoloSiusModel lAvvFascMod = new AvvocatoFascicoloSiusModel();
		lAvvFascMod.setFasSiuIdFascicoloSius(((FascicoloGPModel) getSessionAttribute("fascicoloSiusGP"))
				.getFascicoloSiusModel().getIdFascicoloSius());
		// AvvocatoController lCtrl = new AvvocatoController();
		IAvvocato lCtrl = SIUSLookupRemote.getAvvocatoRemote();
		Vector lVect = lCtrl.ExRicercaDifensoreAttualiFascicolo(lAvvMod, lAvvFascMod);
		setRequestAttribute("modalita", "NoPop");
		setRequestAttribute("avvocato", lVect);
		setRequestAttribute("elenco", "S");
		// Leggo se il fascicolo e' modificabile
		String lModificabile = "NO";
		lModificabile = IsFascicoloSiusModificabile() ? "SI" : "NO";
		setRequestAttribute("modificabile", lModificabile);

		return PG_ASSEGNA_INSERISCI_AVVOCATO;
	}

}