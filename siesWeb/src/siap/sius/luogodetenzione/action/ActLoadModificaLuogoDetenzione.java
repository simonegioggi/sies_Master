package siap.sius.luogodetenzione.action;

import java.math.BigDecimal;
import java.util.Collection;

import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.web.ActionSiap;
import siap.siep.luogodetenzione.controller.ILuogoDetenzione;
import siap.siep.luogodetenzione.model.LuogoDetenzioneModel;
import siap.siep.ordineesecuzione.controller.IOrdineEsecuzione;
import siap.siep.posizione.controller.IPosizioneGiuridica;
import siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel;
import siap.siep.util.SIEPLookupRemote;
import siap.sius.fascicolo.model.FascicoloGPModel;

/**
 * <p>
 * Title: ActLoadDettaglioLuogoDetenzione
 * </p>
 * <p>
 * Description: Classe Action per la load dettaglio di LuogoDetenzione
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

public class ActLoadModificaLuogoDetenzione extends ActionSiap implements ICostantiLuogoDetenzione {
	@SuppressWarnings("rawtypes")
	public String processRequest() throws Exception {
		FascicoloGPModel lFasGPMod = (FascicoloGPModel) getSessionAttribute("fascicoloSiusGP");
		BigDecimal lIdFascicolo = lFasGPMod.getFascicoloSiusModel().getIdFascicoloSius();
		BigDecimal lIdFascicoloSIEP = lFasGPMod.getFascicoloSiusModel().getFasSieIdFascicoloSiep();

		String lId = getRequestStringParameter(CAMPO_ID_LUOGO_DETENZIONE);
		// riempie il model
		// chiama il controller

		ILuogoDetenzione lCtrl = SIEPLookupRemote.getLuogoDetenzioneRemote();
		LuogoDetenzioneModel llLuoMod = lCtrl.ExRicercaLuogoDetenzioneByKey(new BigDecimal(lId));

		PosizioneGiuridicaLuogoDetenzioneAltraCausaModel lPosLuoAltMod = new PosizioneGiuridicaLuogoDetenzioneAltraCausaModel();
		IPosizioneGiuridica lCtrlPos = SIEPLookupRemote.getPosizioneGiuridicaRemote();
		lPosLuoAltMod = lCtrlPos
				.ExRicercaPosizioneGiuridicaLuogoDetenzioneAltraCausaCorrentiByIdFascicoloSius(
						lIdFascicoloSIEP, lIdFascicolo);

		// Controlla se per il fascicolo selezionato esiste un ordine di esecuzione (o legge simeone)
		IOrdineEsecuzione lCtrlOr = SIEPLookupRemote.getOrdineEsecuzioneRemote();
		boolean lFlagOrdineEsecuzione = lCtrlOr.ExEsisteOrdineEsecuzioneByFascicoloSiep(lIdFascicoloSIEP);

		// Imposta Combo posizione Giuridica
		Collection lTipoPosizioni = null;
		if (!lFlagOrdineEsecuzione)
			lTipoPosizioni = DecodificheManager.getInstance().getPosizioneGiuridicaIscrizione();
		else
			lTipoPosizioni = DecodificheManager.getInstance().getPosizioneGiuridicaEsecuzione();

		lPosLuoAltMod.setLuogoDetenzione(llLuoMod);

		setRequestAttribute("posizioneGiuridica", lTipoPosizioni); // Elenco POsizioni Giuridiche
		setRequestAttribute("PosizioneGiuridicaLuogoDetenzioneAltraCausaModel", lPosLuoAltMod);
		setRequestAttribute("modalita", "M");

		return PG_LOAD_INSERISCILUOGODETENZIONE;
		// return PG_LOAD_DETTAGLIOLUOGODETENZIONE;
	}

}