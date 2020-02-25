package siap.siep.sentenzariunita.action;

import java.util.Vector;

import f3b.util.F3BException;
import siap.sico.web.ActionSiap;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.sentenza.model.SentenzaModel;
import siap.siep.sentenzariunita.controller.ISentenzaRiunita;
import siap.siep.sentenzariunita.model.SentenzaRiunitaFascSiepModel;
import siap.siep.sentenzariunita.model.SentenzaRiunitaModel;
import siap.siep.util.SIEPLookupRemote;

/**
 * <p>
 * Title: ActRicercaSentenzaRiunitaFascicolo
 * </p>
 * <p>
 * Description: Classe Action per la ricerca di SentenzaRiunita
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
public class ActRicercaSentenzaRiunitaFascicolo extends ActionSiap implements ICostantiSentenzaRiunita {

	@SuppressWarnings("rawtypes")
	public String processRequest() throws F3BException {

		SentenzaModel lSen = new SentenzaModel((SentenzaModel) getSessionAttribute("sentenza"));
		FascicoloSiepModel lFascMod = (FascicoloSiepModel) this.getSessionAttribute("fascicolo");

		SentenzaRiunitaFascSiepModel lSenRiuMod = new SentenzaRiunitaFascSiepModel();
		SentenzaRiunitaModel lSenMod = new SentenzaRiunitaModel();

		lSenRiuMod.setFasSieIdFascicoloSiep(lFascMod.getIdFascicoloSiep());
		lSenRiuMod.setSentenzaRiunitaModel(lSenMod);
		lSenMod.setSenIdSentenza(lSen.getIdSentenza());

		ISentenzaRiunita lCtrl = SIEPLookupRemote.getSentenzaRiunitaRemote();
		Vector lVect = lCtrl.ExRicercaSentenzaRiunitaFascSiep(lSenRiuMod);
		setRequestAttribute("sentenzeriunite", lVect);

		return PG_RICERCASENTENZARIUNITAFASCICOLO;
	}

}