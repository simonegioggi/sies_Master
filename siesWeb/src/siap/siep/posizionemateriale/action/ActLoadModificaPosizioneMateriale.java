package siap.siep.posizionemateriale.action;

import java.util.Vector;

import siap.sico.web.ActionSiap;
import siap.siep.posizionemateriale.controller.IPosizioneMateriale;
import siap.siep.posizionemateriale.model.PosizioneMaterialeModel;
import siap.siep.util.SIEPLookupRemote;

/**
 * <p>
 * Title: ActLoadModificaPosizioneMateriale
 * </p>
 * <p>
 * Description: Classe Action per la load Modifica di PosizioneMateriale
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
public class ActLoadModificaPosizioneMateriale extends ActionSiap implements ICostantiPosizioneMateriale {

	@SuppressWarnings("rawtypes")
	public String processRequest() throws Exception {

		// Riempie il model per ricercare il record da modificare
		PosizioneMaterialeModel lPosModRic = new PosizioneMaterialeModel();
		lPosModRic.setCodPosizioneMateriale(getRequestStringParameter(CAMPO_COD_POSIZIONE_MATERIALE));
		lPosModRic.setCodUfficio(getRequestStringParameter(CAMPO_COD_UFFICIO));

		// Ricerca Posizione Materiale
		IPosizioneMateriale lCtrl = SIEPLookupRemote.getPosizioneMaterialeRemote();
		Vector lPosizioni = lCtrl.ExRicercaPosizioneMateriale(lPosModRic);
		PosizioneMaterialeModel lPosMod = (PosizioneMaterialeModel) lPosizioni.get(0);
		setRequestAttribute("posizionemateriale", lPosMod);

		setRequestAttribute("modalita", "M");
		return PG_LOAD_INSERISCIPOSIZIONEMATERIALE;
	}

}