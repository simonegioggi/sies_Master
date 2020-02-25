package siap.sige.posizionematerialefascsige.action;

import java.math.BigDecimal;
import java.util.Vector;

import f3b.util.F3BException;
import siap.siep.posizionematerialefasc.action.ICostantiPosizioneMaterialeFasc;
import siap.siep.posizionematerialefasc.model.PosizioneMaterialeFascModel;
import siap.sige.fascicolo.action.ICostantiFascicoloSige;
import siap.sige.fascicolo.controller.IFascicoloSige;
import siap.sige.fascicolo.model.FascicoloSigeEstesoModel;
import siap.sige.posizionematerialefascsige.controller.IPosizioneMaterialeFascSige;
import siap.sige.util.SIGELookupRemote;
import siap.sige.web.ActionSige;

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
 * Company: Engineering
 * </p>
 * 
 * @version 1.0
 */
public class ActLoadDettaglioPosizioneMaterialeFasc extends ActionSige
		implements ICostantiPosizioneMaterialeFasc {

	protected FascicoloSigeEstesoModel mFascicoloEsteso = null;

	@SuppressWarnings("rawtypes")
	public String processRequest() throws Exception {

		// ==========================================================================
		// Verifico che il fascicolo sia effettivamente in sessione in quanto
		// questa funzione può essere richiamata anche dal menù di scelta rapida
		// ==========================================================================
		BigDecimal lIdFascicolo = null;
		if (!this.isRequestParameterNullObj(ICostantiFascicoloSige.CAMPO_ID_FASCICOLO_SIGE)) {
			lIdFascicolo = getRequestBigDecimalParameter(ICostantiFascicoloSige.CAMPO_ID_FASCICOLO_SIGE);
			// Ricerca Fascicolo
			IFascicoloSige lCtrl = SIGELookupRemote.getFascicoloSigeRemote();
			mFascicoloEsteso = lCtrl.ExRicercaEstesaFascicoloSigeByKey(lIdFascicolo);
			if (mFascicoloEsteso == null || mFascicoloEsteso.getFascicoloSige() == null)
				throw new F3BException(F3BException.USER_MESSAGE, "Fascicolo non trovato !");
			setSessionAttribute("FascicoloSigeEsteso", mFascicoloEsteso);

		} else if (!this.isSessionAttributeNullObj("FascicoloSigeEsteso")) {
			lIdFascicolo = ((FascicoloSigeEstesoModel) getSessionAttribute("FascicoloSigeEsteso"))
					.getFascicoloSige().getIdFascicoloSige();
		} else {
			// Se non ho il fascicolo in sessione restituisco la pagina di ricerca
			// fascicolo
			return ICostantiFascicoloSige.REDIRECT_FASCICOLO_RICERCATO + getClass().getName();
		}

		// this.setLinkRitorno();

		// riempie il model di ricerca
		PosizioneMaterialeFascModel lPosMod = new PosizioneMaterialeFascModel(
				PosizioneMaterialeFascModel.POSIZIONE_MATERIALE_FASCICOLO_SIGE);
		lPosMod.setFasSieIdFascicoloSiep(lIdFascicolo);
		// chiama il controller
		IPosizioneMaterialeFascSige lCtrl = SIGELookupRemote.getPosizioneMaterialeFascSigeRemote();
		Vector lPosizioni = lCtrl.ExRicercaPosizioneMaterialeFasc(lPosMod);
		setRequestAttribute("posizioni", lPosizioni);
		// Imposta tipo fi Posizione Remota
		setRequestAttribute("tipo_posizione_materiale", "SIGE");
		// Leggo se il fascicolo e' modificabile
		String lModificabile = "NO";
		if (this.IsFascicoloSigeModificabile() == true)
			lModificabile = "SI";
		else
			lModificabile = "NO";
		this.setRequestAttribute("isModificabile", lModificabile);

		return PG_LOAD_DETTAGLIOPOSIZIONEMATERIALEFASC;
	}

}