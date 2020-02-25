package siap.siep.richiesta.action;

import f3b.util.F3BException;
import f3b.web.IWebConstants;
import siap.sico.web.ActionSiap;
import siap.siep.fascicolo.action.ICostantiFascicoloSiep;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.penaresidua.controller.IPenaResidua;
import siap.siep.util.SIEPLookupRemote;

/**
 * <p>
 * Title: ActLoadGrigliaTrasmissioneCompetenza
 * </p>
 * <p>
 * Description: Classe Action per la load inserisci della Trasmissione per Competenza
 * </p>
 * <p>
 * Copyright: Copyright (c) 2008
 * </p>
 * <p>
 * Company: Eutelia
 * </p>
 * 
 * @version 1.0
 */
public class ActLoadGrigliaTrasmissioneCompetenza extends ActionSiap implements ICostantiRichiesta {

	public String processRequest() throws F3BException {

		if (this.isSessionAttributeNullObj("fascicolo")) {
			return ICostantiFascicoloSiep.REDIRECT_FASCICOLO_RICERCATO + getClass().getName();
		}
		FascicoloSiepModel lFasc = (FascicoloSiepModel) getSessionAttribute("fascicolo");

		this.isFascicoloSiepDiCompetenza();

		this.isEventoNonValidato();

		// Verifico se il procedimento è già associato con NSC
		String lAlertNSC = "Attenzione! Il Procedimento corrente (" + lFasc.getChiaveAnno() + "/"
				+ lFasc.getChiaveProgr() + ") non risulta ancora trasmesso a NSC.";

		// if (lFasc.getKeyProvvNsc()==null) {
		// lAlertNSC+=" Per poter procedere alla trasmissione per competenza è necessario prima affettuare lo
		// scarico su NSC." ;
		// throw new SIEPEException( SIEPEException.USER_MESSAGE, lAlertNSC );
		// }

		if (lFasc.getKeyProvvNsc() == null && this.isRequestParameterNullObj("warning")) { // Non bloccante
			lAlertNSC += " Per poter procedere alla trasmissione per competenza è consigliabile prima affettuare lo scarico su NSC. Si vuole procedere comunque?";
			setRequestAttribute(IWebConstants.ACTION_FIELD, "" + getClass().getName());
			setRequestAttribute(IWebConstants.MESSAGE_TEXT, lAlertNSC);

			return IWebConstants.PG_WARNING;
		}

		// Controllo Esistenza pena residua non validata per quel fascicolo
		// PenaResiduaModel lPenaResMod = new PenaResiduaModel();
		IPenaResidua lPenResCtrl = SIEPLookupRemote.getPenaResiduaRemote();
		/* lPenaResMod = */lPenResCtrl
				.ExRicercaPenaResiduaCorrenteByFascicoloSiep(lFasc.getIdFascicoloSiep());

		// if (this.notEsistePenaResiduaCorrenteByFascicoloSiep(lPenaResMod))
		// return IWebConstants.PG_MESSAGE;
		// ==========================================================

		// vado avanti solo se il fascicolo non è iscritto
		if (isFascicoloIscritto())
			return IWebConstants.PG_MESSAGE;

		setRequestAttribute("strFunzione", "Trasmissione per Competenza");

		return PG_LOAD_GRIGLIA_SELEZIONE_TIPO_TRASMISSIONE_COMP;
	}

}