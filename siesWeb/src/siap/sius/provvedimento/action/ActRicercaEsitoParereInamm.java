package siap.sius.provvedimento.action;

import java.math.BigDecimal;
import java.util.Vector;

import siap.sico.evento.controller.IEvento;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.sius.fascicolo.action.ICostantiFascicoloSius;
import siap.sius.fascicolo.model.FascicoloGPModel;
import siap.sius.produzioneatti.action.ICostantiProduzioneAtti;

/**
 * <p>
 * Title: ActRicercaProvvedimentii
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
public class ActRicercaEsitoParereInamm extends ActionSiap implements ICostantiProvvedimento,
		ICostantiProduzioneAtti {

	@SuppressWarnings("rawtypes")
	public String processRequest() throws Exception {

		Vector lVect = null;
		BigDecimal lIdFascicolo = null;
		if (isRequestParameterNullObj("noQuery")) {
			lIdFascicolo = getRequestBigDecimalParameter(ICostantiFascicoloSius.CAMPO_ID_FASCICOLO_SIUS);
		} else {
			FascicoloGPModel lFasGPMod = (FascicoloGPModel) getSessionAttribute("fascicoloSiusGP");
			lIdFascicolo = lFasGPMod.getFascicoloSiusModel().getIdFascicoloSius();
		}
		IEvento mCtrl = SICOLookupRemote.getEventoRemote();
		lVect = mCtrl.ExRicercaEventoByFascEsitoParereInamm(lIdFascicolo, COD_EVENTO_PROVVEDIMENTO);
		setRequestAttribute("provvedimenti", lVect);
		return PG_ELENCOESITOPAREREINAMM;
	}

}