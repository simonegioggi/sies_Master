package siap.sius.avvocato.action;

/**
* <p>Title: ActRicercaAvvocato</p>
* <p>Description: Classe Action per la ricerca di Avvocato</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.util.Vector;

import f3b.util.F3BException;
import siap.sico.web.ActionSiap;
import siap.sius.avvocato.controller.IAvvocato;
import siap.sius.avvocato.model.AvvocatoModel;
import siap.sius.fascicolo.model.FascicoloGPModel;
import siap.sius.util.SIUSLookupRemote;

public class ActRicercaAvvocatoSiep extends ActionSiap implements ICostantiAvvocato {

	@SuppressWarnings("rawtypes")
	public String processRequest() throws F3BException {

		AvvocatoModel lAvvMod = new AvvocatoModel();

		lAvvMod.setCognome(getRequestStringParameter(CAMPO_COGNOME));
		if (!this.isRequestParameterNullObj(CAMPO_FORO))
			lAvvMod.setForo(getRequestStringParameter(CAMPO_FORO));

		FascicoloGPModel lFasGPMod = new FascicoloGPModel();
		lFasGPMod = (FascicoloGPModel) getSessionAttribute("fascicoloSiusGP");

		IAvvocato lCtrl = SIUSLookupRemote.getAvvocatoRemote();
		Vector lVect = lCtrl.ExRicercaAvvocatoSiep(lAvvMod,
				lFasGPMod.getFascicoloSiusModel().getFasSieIdFascicoloSiep());

		setRequestAttribute("formname", getRequestStringParameter("formname"));
		setRequestAttribute("avvocato", lVect);

		String lPage = PG_RICERCAAVVOCATOSIEP;

		if (getRequestStringParameter("modalita").compareTo("BREVE") == 0)
			lPage = PG_RICERCAAVVOCATOBREVESIEP;

		return lPage;
	}

}