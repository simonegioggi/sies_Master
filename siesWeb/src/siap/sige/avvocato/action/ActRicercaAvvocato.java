package siap.sige.avvocato.action;

/**
* <p>Title: ActRicercaAvvocato</p>
* <p>Description: Classe Action per la ricerca di Avvocato</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.util.Vector;

import f3b.util.F3BException;
import siap.sico.avvocato.model.AvvocatoModel;
import siap.sico.web.ActionSiap;
import siap.sige.avvocato.controller.IAvvocato;
import siap.sige.util.SIGELookupRemote;

public class ActRicercaAvvocato extends ActionSiap implements ICostantiAvvocato {

	@SuppressWarnings("rawtypes")
	public String processRequest() throws F3BException {

		AvvocatoModel lAvvMod = new AvvocatoModel();

		lAvvMod.setCognome(getRequestStringParameter(CAMPO_COGNOME));
		if (!this.isRequestParameterNullObj(CAMPO_FORO))
			lAvvMod.setForo(getRequestStringParameter(CAMPO_FORO));
		lAvvMod.setCodUffAppartenenza(this.getCodUfficioUtenteConnesso());

		IAvvocato lCtrl = SIGELookupRemote.getAvvocatoRemote();

		Vector lVect = lCtrl.ExRicercaDifensore(lAvvMod);

		setRequestAttribute("formname", getRequestStringParameter("formname"));
		setRequestAttribute("avvocato", lVect);

		String lPage = PG_RICERCAAVVOCATO;

		if (getRequestStringParameter("modalita").compareTo("BREVE") == 0)
			lPage = PG_RICERCAAVVOCATOBREVE;

		return lPage;
	}

}