package siap.sius.depositoordinanzapc.action;

import java.util.ArrayList;
import java.util.List;

import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.decodifiche.model.DecodificheModel;
import siap.sius.SIUSException;
import siap.sius.depositodecreto.action.ActLoadEmissioneDecreto;
import siap.sius.fascicolo.model.FascicoloGPModel;

/**
 * <p>
 * Title: ActLoadEmissioneOrdinanzaUDS
 * </p>
 * <p>
 * Description: Classe Action per la load inserisci di Emissione Ordinanza UDS
 * </p>
 * Poichè l'azione deve implementare la stessa funzione implementata da ActLoadEmissioneDecreto, viene estesa questa in
 * modo di utilizzare il suo processRequest(). Si sfrutta l'override della funzione generaListaTipi() per differenziare
 * la jsp.
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: Bull
 * </p>
 * 
 * @version 1.0
 */

public class ActLoadEmissioneOrdinanzaUDS extends ActLoadEmissioneDecreto {
	// Generazione della lista di tipi ordinanza

	public String processRequest() throws Exception {
		String strCodTipoUfficio = getUfficioUtenteConnesso().getCodTipoUfficio();
		String lPage = super.processRequest();
		if (this.isSessionAttributeNullObj("fascicoloSiusGP"))
			throw new SIUSException(SIUSException.USER_MESSAGE, "fascicoloSiusGP non in sessione");
		FascicoloGPModel lFasGPMod = (FascicoloGPModel) getSessionAttribute("fascicoloSiusGP");
		// Controllo esistenza udienza solo in TDS !!
		// MEV10-s3: aggiunta or condition per gestire trib. sorv. minori
		if ("TDS".equals(strCodTipoUfficio) || "TDSM".equals(strCodTipoUfficio)) {
			if (lFasGPMod.getGeneraleProcedimentoModel().getDataCameraConsiglio() == null)
				throw new SIUSException(SIUSException.USER_MESSAGE,
						"Impossibile emettere un'ordinanza per il fascicolo "
								+ lFasGPMod.getFascicoloSiusModel().getChiaveAnno() + "/"
								+ lFasGPMod.getFascicoloSiusModel().getChiaveProgr()
								+ ". Non è stata fissata l'Udienza.");
		}
		setRequestAttribute("flagOrdinanza", "ordinanza");
		return lPage;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List generaListaTipi() {
		List<DecodificheModel> lTipoOrdinanza = null;
		setRequestAttribute("flagOrdinanza", "ordinanza");
		if (DecodificheManager.getInstance().getTipoOrdinanza() == null) {
			lTipoOrdinanza = new ArrayList<DecodificheModel>();
		} else {
			lTipoOrdinanza = new ArrayList<DecodificheModel>(DecodificheManager.getInstance().getTipoOrdinanza());
		}
		lTipoOrdinanza.add(new DecodificheModel("00", "Generazione Automatica", "-", "-", "-", "-", "-", "-", "-"));

		return lTipoOrdinanza;
	}
}