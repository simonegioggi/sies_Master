package siap.siep.posizionematerialefasc.action;

import java.math.BigDecimal;
import java.util.Vector;

import siap.sico.web.ActionSiap;
import siap.siep.fascicolo.action.ICostantiFascicoloSiep;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.posizionematerialefasc.controller.IPosizioneMaterialeFasc;
import siap.siep.posizionematerialefasc.model.PosizioneMaterialeFascModel;
import siap.siep.util.SIEPLookupRemote;

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

public class ActLoadDettaglioPosizioneMaterialeFasc extends ActionSiap
		implements ICostantiPosizioneMaterialeFasc {

	@SuppressWarnings("rawtypes")
	public String processRequest() throws Exception {

		// ==========================================================================
		// Verifico che il fascicolo sia effettivamente in sessione in quanto
		// questa funzione può essere richiamata anche dal menù di scelta rapida
		// (29/05/2006)
		// ==========================================================================
		BigDecimal lIdFascicolo = null;
		if (!this.isSessionAttributeNullObj("fascicolo")) {
			lIdFascicolo = ((FascicoloSiepModel) getSessionAttribute("fascicolo")).getIdFascicoloSiep();
		} else {
			// Se non ho il fascicolo in sessione restituisco la pagina di ricerca
			// fascicolo
			return ICostantiFascicoloSiep.REDIRECT_FASCICOLO_RICERCATO + getClass().getName();
		}

		this.setLinkRitorno();
		// riempie il model di ricerca
		PosizioneMaterialeFascModel lPosMod = new PosizioneMaterialeFascModel();
		lPosMod.setFasSieIdFascicoloSiep(lIdFascicolo);
		// chiama il controller
		IPosizioneMaterialeFasc lCtrl = SIEPLookupRemote.getPosizioneMaterialeFascRemote();
		Vector lPosizioni = lCtrl.ExRicercaPosizioneMaterialeFasc(lPosMod);
		setRequestAttribute("posizioni", lPosizioni);

		return PG_LOAD_DETTAGLIOPOSIZIONEMATERIALEFASC;
	}

}