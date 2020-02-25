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
public class ActDettaglioAvvocato extends ActionSiap implements ICostantiAvvocato {

	public String processRequest() throws F3BException {

		String lAvvId = getRequestStringParameter(CAMPO_ID_AVVOCATO);

		// riempie il model
		AvvocatoModel lAvvMod = new AvvocatoModel();
		lAvvMod.setIdAvvocato(new BigDecimal(lAvvId));
		// STUB 24/2/2003 (gianluca)
		// Ho lasciato il doppio model, eventualmente per affinare ricerche sull'avvocato
		// dato che in questa fase le esigenze non sono chiarissime
		// In questa fase è ovviamente ridondante.
		// To do : verificare effettiva necessità, e qualora serva, eliminare AvvocatoModel

		// chiama il controller
		// AvvocatoController lCtrl = new AvvocatoController();

		IAvvocato lCtrl = SIEPLookupRemote.getAvvocatoRemote();
		Vector lVect = lCtrl.ExRicercaAvvocatoPerInserimento(lAvvMod);
		AvvocatoModel lAvv = new AvvocatoModel((AvvocatoModel) lVect.get(0));

		setRequestAttribute("modalita", "D");
		setRequestAttribute("avvocato", lAvv);

		return PG_DETTAGLIO_AVVOCATO;
	}

}