package siap.siep.sentenzariunita.action;

import java.util.Vector;

import f3b.util.F3BException;
import siap.sico.web.ActionSiap;
import siap.siep.sentenza.model.SentenzaModel;
import siap.siep.sentenzariunita.controller.ISentenzaRiunita;
import siap.siep.sentenzariunita.model.SentenzaRiunitaModel;
import siap.siep.util.SIEPLookupRemote;

/**
 * <p>
 * Title: ActRicercaSentenzaRiunita
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
public class ActRicercaSentenzaRiunita extends ActionSiap implements ICostantiSentenzaRiunita {

	@SuppressWarnings("rawtypes")
	public String processRequest() throws F3BException {

		SentenzaModel lSen = new SentenzaModel((SentenzaModel) getSessionAttribute("sentenza"));

		SentenzaRiunitaModel lSenMod = new SentenzaRiunitaModel();

		lSenMod.setSenIdSentenza(lSen.getIdSentenza());

		ISentenzaRiunita lCtrl = SIEPLookupRemote.getSentenzaRiunitaRemote();
		Vector lVect = lCtrl.ExRicercaSentenzaRiunita(lSenMod);
		setRequestAttribute("sentenzeriunite", lVect);

		return PG_RICERCASENTENZARIUNITA;
	}

}