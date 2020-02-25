package siap.siep.verbale.action;

import java.math.BigDecimal;

import f3b.util.F3BException;
import siap.sico.web.ActionSiap;
import siap.siep.util.SIEPLookupRemote;
import siap.siep.verbale.controller.IVerbale;
import siap.siep.verbale.model.VerbaleModel;

/**
 * <p>
 * Title: ActLoadDettaglioVerbale
 * </p>
 * <p>
 * Description: Classe Action per la load dettaglio di Verbale
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
public class ActLoadDettaglioVerbaleVaneRicerche extends ActionSiap implements ICostantiVerbale {

	public String processRequest() throws F3BException {

		BigDecimal lIdVerbale = null;
		BigDecimal lIdEvento = null; // Id dell'evento collegato al verbale
		VerbaleModel lVerMod = new VerbaleModel();
		IVerbale lCtrl = SIEPLookupRemote.getVerbaleRemote();

		if (this.isRequestParameterNullObj(CAMPO_ID_VERBALE)) { // Siamo nel caso del dettalgio dell'evento...
																// Esiste solo l'id dell'evento, ricerco il
																// verbale da quello
			lIdEvento = getRequestBigDecimalParameter(
					siap.sico.evento.action.ICostantiEvento.CAMPO_ID_EVENTO);
			lVerMod = lCtrl.ExRicercaVerbaleByCodTipoIdEvento(lIdEvento, "02");
		} else { // Siamo nel caso del dettaglio del varbale appena inserito
			lIdVerbale = getRequestBigDecimalParameter(CAMPO_ID_VERBALE);
			lVerMod = lCtrl.ExRicercaVerbaleByKey(lIdVerbale);
			lIdEvento = lVerMod.getEveIdEvento();
		}

		// se proviene dalla maschera di omesse notifiche
		if (!this.isRequestAttributeNullObj("FlagOmesse"))
			setRequestAttribute("FlagOmesse", this.getRequestAttribute("FlagOmesse"));

		// BigDecimal lIdFasc = ((FascicoloSiepModel) getSessionAttribute("fascicolo")).getIdFascicoloSiep();
		setRequestAttribute("verbale", lVerMod);

		return PG_LOAD_DETTAGLIO_VERBALE_VANE_RICERCHE;
	}

}