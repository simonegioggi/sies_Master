package siap.siep.avvocato.action;

/**
* <p>Title: ActDettaglioAvvocato</p>
* <p>Description: Classe Action per il dettaglio di Avvocato - Sentenza</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.math.BigDecimal;
import java.util.Vector;

import siap.sico.web.ActionSiap;
//import siap.siep.avvocato.controller.AvvocatoController;
import siap.siep.avvocato.controller.IAvvocato;
import siap.siep.avvocato.model.AvvocatoModel;
import siap.siep.util.SIEPLookupRemote;
import f3b.util.F3BException;

@SuppressWarnings("rawtypes")
public class ActDettaglioAvvocatoPopUp extends ActionSiap implements ICostantiAvvocato {

	public String processRequest() throws F3BException {

		String lAvvId = getRequestStringParameter(CAMPO_ID_AVVOCATO);
		AvvocatoModel lAvvMod = new AvvocatoModel();
		lAvvMod.setIdAvvocato(new BigDecimal(lAvvId));
		IAvvocato lCtrl = SIEPLookupRemote.getAvvocatoRemote();
		Vector lVect = lCtrl.ExRicercaAvvocatoPerInserimento(lAvvMod);
		AvvocatoModel lAvv = new AvvocatoModel((AvvocatoModel) lVect.get(0));

		setRequestAttribute("avvocato", lAvv);
		setRequestAttribute("formname", getRequestStringParameter("formname"));

		return PG_DETTAGLIO_AVVOCATO_POPUP;
	}

}