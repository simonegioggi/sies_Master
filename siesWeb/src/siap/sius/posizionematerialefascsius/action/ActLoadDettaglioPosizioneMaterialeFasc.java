package siap.sius.posizionematerialefascsius.action;

import java.math.BigDecimal;
import java.util.Vector;

import siap.siep.posizionematerialefasc.action.ICostantiPosizioneMaterialeFasc;
import siap.siep.posizionematerialefasc.model.PosizioneMaterialeFascModel;
import siap.sius.ActionSius;
import siap.sius.fascicolo.model.FascicoloGPModel;
import siap.sius.posizionematerialefascsius.controller.IPosizioneMaterialeFascSius;
import siap.sius.util.SIUSLookupRemote;

/**
 * <p>
 * Title: ActLoadDettaglioPosizioneMaterialeFasc
 * </p>
 * <p>
 * Description: Classe Action per la load dettaglio di PosizioneMaterialeFasc
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
public class ActLoadDettaglioPosizioneMaterialeFasc extends ActionSius
		implements ICostantiPosizioneMaterialeFasc {

	@SuppressWarnings("rawtypes")
	public String processRequest() throws Exception {

		// Si ricava il Fascicolo dalla sessione
		FascicoloGPModel lFasGPMod = (FascicoloGPModel) getSessionAttribute("fascicoloSiusGP");
		BigDecimal lIdFascicolo = lFasGPMod.getFascicoloSiusModel().getIdFascicoloSius();

		this.setLinkRitorno();

		// riempie il model di ricerca
		PosizioneMaterialeFascModel lPosMod = new PosizioneMaterialeFascModel(
				PosizioneMaterialeFascModel.POSIZIONE_MATERIALE_FASCICOLO_SIUS);
		lPosMod.setFasSieIdFascicoloSiep(lIdFascicolo);
		// chiama il controller
		IPosizioneMaterialeFascSius lCtrl = SIUSLookupRemote.getPosizioneMaterialeFascSiusRemote();
		Vector lPosizioni = lCtrl.ExRicercaPosizioneMaterialeFasc(lPosMod);
		setRequestAttribute("posizioni", lPosizioni);
		// Imposta tipo fi Posizione Remota
		setRequestAttribute("tipo_posizione_materiale", "SIUS");
		// Leggo se il fascicolo e' modificabile
		String lModificabile = "NO";
		if (this.IsFascicoloSiusModificabile() == true)
			lModificabile = "SI";
		else
			lModificabile = "NO";
		this.setRequestAttribute("isModificabile", lModificabile);

		return PG_LOAD_DETTAGLIOPOSIZIONEMATERIALEFASC;
	}

}