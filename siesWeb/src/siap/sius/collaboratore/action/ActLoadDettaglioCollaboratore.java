package siap.sius.collaboratore.action;

import java.math.BigDecimal;
import java.util.Vector;

import f3b.util.F3BException;
import siap.sius.ActionSius;
import siap.sius.collaboratore.controller.ICollaboratore;
import siap.sius.collaboratore.model.CollaboratoreModel;
import siap.sius.fascicolo.model.FascicoloGPModel;
import siap.sius.util.SIUSLookupRemote;

/**
 * <p>
 * Title: ActLoadDettaglioCollaboratore
 * </p>
 * <p>
 * Description: Classe Action per il caricamento dettaglio dell'eventuale Collaboratore di Giustizia legato a
 * Procedimento SIUS.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2008
 * </p>
 * <p>
 * Company: Eutelia
 * </p>
 * 
 * @version 1.0
 */
public class ActLoadDettaglioCollaboratore extends ActionSius implements ICostantiCollaboratore {

	@SuppressWarnings("rawtypes")
	public String processRequest() throws Exception {

		// Si ricava il Fascicolo dalla sessione
		FascicoloGPModel lFasGPMod = (FascicoloGPModel) getSessionAttribute("fascicoloSiusGP");
		BigDecimal lIdFascicolo = lFasGPMod.getFascicoloSiusModel().getIdFascicoloSius();

		Vector lElencoColl = null;
		// flag che indica se il Fascicolo risulta collegato ad un Collaboratore attivo
		String lIsCollaboratore = "NO";

		setLinkRitorno();
		if (lIdFascicolo != null) {
			ICollaboratore lCtrl = SIUSLookupRemote.getCollaboratoreRemote();
			if (lCtrl.ExIsPackage()) {
				CollaboratoreModel lModel = new CollaboratoreModel();
				lModel.setIdFascicoloSius(lIdFascicolo);
				lModel.setCodUfficio(getCodUfficioUtenteConnesso());

				// lElencoColl = lCtrl.ExGetCollaboratore(lIdFascicolo, getCodUfficioUtenteConnesso());
				// lElencoColl = lCtrl.ExGetCollaboratoreArr(lIdFascicolo, getCodUfficioUtenteConnesso());
				lElencoColl = lCtrl.ExRicercaCollaboratore(lModel);

				setRequestAttribute("movimenti_collaboratore", lElencoColl);

				if (lCtrl.ExIsCollaboratore(lIdFascicolo, getCodUfficioUtenteConnesso()))
					lIsCollaboratore = "SI";
			} else
				throw new F3BException(F3BException.USER_MESSAGE, "funzione non disponibile!");
		}

		// Leggo se il fascicolo e' modificabile
		String lModificabile = "NO";
		if (IsFascicoloSiusModificabile() == true)
			lModificabile = "SI";
		else
			lModificabile = "NO";
		setRequestAttribute("isModificabile", lModificabile);
		setRequestAttribute("isCollaboratore", lIsCollaboratore);
		setRequestAttribute("idFascicoloSius", lIdFascicolo.toString());

		return PG_LOAD_DETTAGLIOCOLLABORATORE;
	}

}