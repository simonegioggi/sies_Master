package siap.sius.provvedimento.action;

import java.math.BigDecimal;
import java.util.Vector;

import siap.sico.evento.controller.IEvento;
import siap.sico.util.SICOLookupRemote;
import siap.sius.ActionSius;
import siap.sius.fascicolo.action.ICostantiFascicoloSius;
import siap.sius.fascicolo.model.FascicoloGPModel;

/**
 * <p>
 * Title: ActLoadElencoCompFoglioComp
 * </p>
 * <p>
 * Description: Azione specializzazione per la ricerca dei Provvedimenti legati al fascicolo SIUS.
 * <p>
 * Copyright: Bull Italia S.p.A.Copyright (c) 2002
 * </p>
 * <p>
 * Company: Bull Italia S.p.A.
 * </p>
 * 
 * @version 1.0
 */

public class ActLoadElencoCompFoglioComp extends ActionSius implements ICostantiProvvedimento {

	@SuppressWarnings("rawtypes")
	public String processRequest() throws Exception {
		// Gestione del punto di ritorno
		this.setLinkRitorno();

		Vector lVect = null;
		BigDecimal lIdFascicolo = null;

		if (!isRequestParameterNullObj(ICostantiFascicoloSius.CAMPO_ID_FASCICOLO_SIUS)) {
			lIdFascicolo = getRequestBigDecimalParameter(ICostantiFascicoloSius.CAMPO_ID_FASCICOLO_SIUS);
		} else {
			FascicoloGPModel lFasGPMod = (FascicoloGPModel) getSessionAttribute("fascicoloSiusGP");
			lIdFascicolo = lFasGPMod.getFascicoloSiusModel().getIdFascicoloSius();
		}
		IEvento mCtrl = SICOLookupRemote.getEventoRemote();
		// MEV10-s3: aggiunto parametro di passaggio per gestire tipologia ufficio minorenni
		String strCodTipoUfficio = getUfficioUtenteConnesso().getCodTipoUfficio();
		lVect = mCtrl.ExRicercaEventoXCFC(lIdFascicolo, COD_EVENTO_PROVVEDIMENTO, strCodTipoUfficio);
		setRequestAttribute("provvedimenti", lVect);

		// Controlla se il fascicolo e' modificabile
		String lModificabile = "NO";
		if (IsFascicoloSiusModificabile() == true)
			lModificabile = "SI";
		else
			lModificabile = "NO";
		setRequestAttribute("Modificabile", lModificabile);

		return PG_ELENCOPROVVEDIMENTICFC;
	}
}