package siap.siep.altrigradigiudizio.action;

import java.util.Vector;

import siap.sico.web.ActionSiap;
import siap.siep.altrigradigiudizio.controller.IAltriGradiGiudizio;
import siap.siep.altrigradigiudizio.model.AltriGradiGiudizioModel;
import siap.siep.sentenza.model.SentenzaModel;
import siap.siep.util.SIEPLookupRemote;
import f3b.util.F3BException;

/**
 * <p>
 * Title: ActRicercaAltriGradiGiudizio
 * </p>
 * <p>
 * Description: Classe Action per la ricerca di AltriGradiGiudizio
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
@SuppressWarnings("rawtypes")
public class ActRicercaAltriGradiGiudizio extends ActionSiap implements ICostantiAltriGradiGiudizio {

	public String processRequest() throws F3BException {

		SentenzaModel lSen = new SentenzaModel((SentenzaModel) getSessionAttribute("sentenza"));

		AltriGradiGiudizioModel lAltMod = new AltriGradiGiudizioModel();

		lAltMod.setSenIdSentenza(lSen.getIdSentenza());

		IAltriGradiGiudizio lCtrl = SIEPLookupRemote.getAltriGradiGiudizioRemote();
		Vector lVect = lCtrl.ExRicercaAltriGradiGiudizio(lAltMod);
		setRequestAttribute("altrigradigiudizio", lVect);

		return PG_RICERCAALTRIGRADIGIUDIZIO;
	}

}