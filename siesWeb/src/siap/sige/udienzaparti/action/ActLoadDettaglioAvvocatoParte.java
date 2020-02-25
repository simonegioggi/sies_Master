package siap.sige.udienzaparti.action;

import java.math.BigDecimal;
import java.util.Vector;

import siap.sico.avvocato.model.AvvocatoModel;
import siap.sico.web.ActionSiap;
import siap.sige.avvocato.action.ICostantiAvvocato;
import siap.sige.avvocato.controller.IAvvocato;
import siap.sige.udienzaparti.controller.IPartiUdienza;
import siap.sige.udienzaparti.model.AnagraficaPartiUdienzaModel;
import siap.sige.udienzaparti.model.AvvocatoParteModel;
import siap.sige.udienzaparti.model.PartiUdienzaDifensoreModel;
import siap.sige.util.SIGELookupRemote;

/**
 * <p>
 * Title: ActLoadDettaglioAvvocatoParte
 * </p>
 * <p>
 * Description: Classe Action per la load dettaglio di Avvocato - Parte Udienza
 * </p>
 * <p>
 * Copyright: Copyright (c) 2008
 * </p>
 * <p>
 * Company: Engineering
 * </p>
 * 
 * @version 1.0
 */
public class ActLoadDettaglioAvvocatoParte extends ActionSiap implements ICostantiAvvocato {

	@SuppressWarnings("rawtypes")
	public String processRequest() throws Exception {

		String lAvvId = getRequestStringParameter(CAMPO_ID_AVVOCATO);
		String lIdAvvParteUdienza = getRequestStringParameter(
				ICostantiPartiUdienza.CAMPO_ID_AVVOCATO_PARTE_UDIENZA);
		String lIdSoggetto = getRequestStringParameter(ICostantiPartiUdienza.CAMPO_ID_SOGGETTO);
		String lIdEventoUdienza = getRequestStringParameter(ICostantiPartiUdienza.CAMPO_ID_EVENTO_UDIENZA);

		IPartiUdienza lCtrlPU = SIGELookupRemote.getPartiUdienzaRemote();
		AnagraficaPartiUdienzaModel anagParteUdienzaRet = lCtrlPU
				.ExRicercaParteUdienzaByKey(new BigDecimal(lIdSoggetto));

		// riempie il model
		AvvocatoModel lAvvMod = new AvvocatoModel();
		if (lAvvId != null && !lAvvId.equals("null")) {
			lAvvMod.setIdAvvocato(new BigDecimal(lAvvId));
		}

		PartiUdienzaDifensoreModel lParteUdienzaDifMod = new PartiUdienzaDifensoreModel();
		lParteUdienzaDifMod.setIdAvvocatoParteUdienza(new BigDecimal(lIdAvvParteUdienza));

		IAvvocato lCtrl = SIGELookupRemote.getAvvocatoRemote();
		Vector lVect = lCtrl.ExRicercaDifensoreAttualiParteUdienza(lAvvMod, lParteUdienzaDifMod);
		AvvocatoParteModel lAvv = new AvvocatoParteModel((AvvocatoParteModel) lVect.get(0));
		setRequestAttribute("modalita", "D");
		setRequestAttribute("avvocatoParteUdienza", lAvv);
		setRequestAttribute("anagraficaParteUdienza", anagParteUdienzaRet);
		setRequestAttribute("idEventoUdienza", lIdEventoUdienza);

		return ICostantiPartiUdienza.PG_DETTAGLIO_AVVOCATO_PARTE;
	}

}