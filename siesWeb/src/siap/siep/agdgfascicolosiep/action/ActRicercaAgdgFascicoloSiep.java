package siap.siep.agdgfascicolosiep.action;

import java.util.Vector;

import siap.sico.web.ActionSiap;
import siap.siep.agdgfascicolosiep.controller.IAgdgFascicoloSiep;
import siap.siep.agdgfascicolosiep.model.AgdgFascicoloSiepModel;
import siap.siep.altrigradigiudizio.model.AltriGradiGiudizioModel;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.sentenza.model.SentenzaModel;
import siap.siep.util.SIEPLookupRemote;
import f3b.util.F3BException;

/**
 * <p>
 * Title: ActRicercaAgdgFascicoloSiep
 * </p>
 * <p>
 * Description: Classe Action per la ricerca di AgdgFascicoloSiep
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
public class ActRicercaAgdgFascicoloSiep extends ActionSiap implements ICostantiAgdgFascicoloSiep {

	public String processRequest() throws F3BException {

		SentenzaModel lSen = new SentenzaModel((SentenzaModel) getSessionAttribute("sentenza"));
		FascicoloSiepModel lFascMod = (FascicoloSiepModel) this.getSessionAttribute("fascicolo");

		AgdgFascicoloSiepModel lAgdMod = new AgdgFascicoloSiepModel();
		AltriGradiGiudizioModel lSenMod = new AltriGradiGiudizioModel();

		lAgdMod.setFasSieIdFascicoloSiep(lFascMod.getIdFascicoloSiep());
		lAgdMod.setAltriGradiGiudizioModel(lSenMod);
		lSenMod.setSenIdSentenza(lSen.getIdSentenza());

		IAgdgFascicoloSiep lCtrl = SIEPLookupRemote.getAgdgFascicoloSiepRemote();
		Vector lVect = lCtrl.ExRicercaAgdgFascicoloSiep(lAgdMod);
		setRequestAttribute("agdgfascicolosiep", lVect);

		return PG_RICERCAAGDGFASCICOLOSIEP;
	}

}