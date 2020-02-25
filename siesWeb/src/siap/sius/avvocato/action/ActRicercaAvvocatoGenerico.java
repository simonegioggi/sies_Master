package siap.sius.avvocato.action;

import java.util.Vector;

import siap.sico.web.ActionSiap;
//import siap.sius.avvocato.controller.AvvocatoController;
import siap.sius.avvocato.controller.IAvvocato;
import siap.sius.avvocato.model.AvvocatoModel;
import siap.sius.util.SIUSLookupRemote;
import f3b.util.F3BException;

/**
 * <p>
 * Title: ActRicercaAvvocato
 * </p>
 * <p>
 * Description: Classe Action per la ricerca di Avvocato
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
public class ActRicercaAvvocatoGenerico extends ActionSiap implements ICostantiAvvocato {

	@SuppressWarnings("rawtypes")
	public String processRequest() throws F3BException {

		AvvocatoModel lAvvMod = new AvvocatoModel();
//		AvvocatoFascicoloSiusModel lAvvFascMod = new AvvocatoFascicoloSiusModel();

		lAvvMod.setCognome(getRequestStringParameter(CAMPO_COGNOME));

		// AvvocatoController lCtrl = new AvvocatoController();
		IAvvocato lCtrl = SIUSLookupRemote.getAvvocatoRemote();

		Vector lVect = lCtrl.ExRicercaAvvocato(lAvvMod);

		setRequestAttribute("formname", getRequestStringParameter("formname"));
		setRequestAttribute("avvocato", lVect);

		String lPage = PG_RICERCAAVVOCATOGENERICO;

		if (getRequestStringParameter("modalita").compareTo("BREVE") == 0)
			lPage = PG_RICERCAAVVOCATOBREVEGENERICO;

		return lPage;
	}

}