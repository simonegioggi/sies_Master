package siap.sius.udienza.action;

import java.math.BigDecimal;
import java.util.Vector;

import siap.sico.magistrato.controller.IMagistrato;
import siap.sico.magistrato.model.MagistratoModel;
import siap.sico.util.SICOLookupRemote;
import siap.sius.avvocato.controller.IAvvocato;
import siap.sius.avvocato.model.AvvocatoFascicoloSiusModel;
import siap.sius.esperto.controller.IEsperto;
import siap.sius.esperto.model.EspertoModel;
import siap.sius.fascicolo.action.ActRicercaFSPuntuale;
import siap.sius.fascicolo.model.FascicoloGPModel;
import siap.sius.magistratorelatore.controller.IMagistratoRelatore;
import siap.sius.magistratorelatore.model.MagistratoRelatoreModel;
import siap.sius.util.SIUSLookupRemote;

/**
 * <p>
 * Title: ActLoadInserisciVerbaleUdienza
 * </p>
 * <p>
 * Description: Classe Action per la load della form di inserimento Verbale Udienza
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
public class ActLoadInserisciVerbaleUdienza extends ActRicercaFSPuntuale implements ICostantiUdienza {

	@SuppressWarnings("rawtypes")
	public String processRequest() throws Exception {

		BigDecimal lIdFasSius = null;
		FascicoloGPModel lFasGPMod = null;
		MagistratoRelatoreModel lMagRel = null;
		MagistratoModel lMagistrato = null;
		EspertoModel lEsperto = null;

		setLinkRitorno();
		if (this.isRequestParameterNullObj("ritorno")) {
			// Invoca la process Request della superclasse se provengo dal menu'.
			super.processRequest();
		}

		// Fascicolo Sius
		lFasGPMod = (FascicoloGPModel) getSessionAttribute("fascicoloSiusGP");
		lIdFasSius = lFasGPMod.getFascicoloSiusModel().getIdFascicoloSius();

		// Magistrato relatore
		IMagistratoRelatore lMagRelCtrl = SIUSLookupRemote.getMagistratoRelatoreRemote();
		lMagRel = lMagRelCtrl.ExRicercaMagRelByFascicolo(lIdFasSius);

		if (lMagRel != null) {
			// Magistrato
			if (lMagRel.getMagCodMagistrato() != null) {
				IMagistrato lMagCtrl = SICOLookupRemote.getMagistratoRemote();
				lMagistrato = lMagCtrl.ExRicercaMagistratoByCod(lMagRel.getMagCodMagistrato());
			}

			// Esperto
			if (lMagRel.getEspIdEsperto() != null) {
				IEsperto lEspCtrl = SIUSLookupRemote.getEspertoRemote();
				lEsperto = lEspCtrl.ExRicercaEspertoByKey(lMagRel.getEspIdEsperto());
			}
		}

		AvvocatoFascicoloSiusModel lAvvFascMod = new AvvocatoFascicoloSiusModel();
		lAvvFascMod.setFasSiuIdFascicoloSius((lFasGPMod.getFascicoloSiusModel()).getIdFascicoloSius());

		// IAvvocato lAvvCtrl = SIUSLookupRemote.getAvvocatoRemote();
		// Vector lAvvocato = lAvvCtrl.ExRicercaAvvocatiAttualiFascicolo(null,lAvvFascMod);

		// Ricerca avvocati assegnati al fascicolo Luigi 9-7-04
		IAvvocato lAvvCtrl = SIUSLookupRemote.getAvvocatoRemote();
		Vector lAvvocato = lAvvCtrl
				.ExRicercaAvvocatiByFascicoloNoError(lFasGPMod.getFascicoloSiusModel().getIdFascicoloSius());

		setRequestAttribute("magistrato", lMagistrato);
		setRequestAttribute("avvocato", lAvvocato);
		setRequestAttribute("esperto", lEsperto);

		return PG_LOAD_INSERISCIVERBALEUDIENZA;
	}

}