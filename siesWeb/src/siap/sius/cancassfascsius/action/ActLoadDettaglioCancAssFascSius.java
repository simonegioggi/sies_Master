package siap.sius.cancassfascsius.action;

import java.math.BigDecimal;
import java.util.Vector;

import siap.sius.ActionSius;
import siap.sius.cancassfascsius.controller.ICancAssFascSius;
import siap.sius.cancassfascsius.model.CancAssFascSiusModel;
import siap.sius.fascicolo.model.FascicoloGPModel;
import siap.sius.util.SIUSLookupRemote;

/**
 * <p>
 * Title: ActLoadDettaglioCancAssFascSius
 * </p>
 * <p>
 * Description: Classe Action per la load dettaglio di CancAssFascSius
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
public class ActLoadDettaglioCancAssFascSius extends ActionSius implements ICostantiCancAssFascSius {

	@SuppressWarnings("rawtypes")
	public String processRequest() throws Exception {

		// Si ricava il Fascicolo dalla sessione
		FascicoloGPModel lFasGPMod = (FascicoloGPModel) getSessionAttribute("fascicoloSiusGP");
		BigDecimal lIdFascicolo = lFasGPMod.getFascicoloSiusModel().getIdFascicoloSius();

		setLinkRitorno();

		// riempie il model di ricerca
		CancAssFascSiusModel lCancAssFasc = new CancAssFascSiusModel();
		lCancAssFasc.setFasSiusIdFascicoloSius(lIdFascicolo);
		// chiama il controller
		ICancAssFascSius lCtrl = SIUSLookupRemote.getCancAssFascSiusRemote();
		// Ricerca e passaggio alla request del risultato
		Vector lElenco = lCtrl.ExRicercaCancAssFascSius(lCancAssFasc);
		setRequestAttribute("elenco", lElenco);

		// Controllo se il fascicolo e' modificabile
		String lModificabile = "NO";
		if (IsFascicoloSiusModificabile() == true)
			lModificabile = "SI";
		else
			lModificabile = "NO";
		setRequestAttribute("isModificabile", lModificabile);

		return PG_LOAD_DETTAGLIOCANCASSFASCSIUS;
	}

}