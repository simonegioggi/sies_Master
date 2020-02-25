package siap.sige.avvocato.action;

/**
* <p>Title: ActLoadDettaglioAvvocatoFascicolo</p>
* <p>Description: Classe Action per la load dettaglio di Avvocato - Sentenza</p>
* <p>Copyright: Copyright (c) 2008</p>
* <p>Company: Eutelia</p>
* @version 1.0
*/

import java.math.BigDecimal;
import java.util.Vector;

import f3b.util.F3BException;
import siap.sico.avvocato.model.AvvocatoModel;
import siap.sico.web.ActionSiap;
import siap.sige.avvocato.controller.IAvvocato;
import siap.sige.avvocato.model.AvvocatoFascicoloSigeModel;
import siap.sige.avvocato.model.AvvocatoSigeModel;
import siap.sige.fascicolo.model.FascicoloSigeEstesoModel;
import siap.sige.util.SIGELookupRemote;

public class ActLoadDettaglioAvvocatoFascicolo extends ActionSiap implements ICostantiAvvocato {

	@SuppressWarnings("rawtypes")
	public String processRequest() throws F3BException {

		// Passa la action di destinazione
		String lAvvId = getRequestStringParameter(CAMPO_ID_AVVOCATO);

		// riempie il model
		AvvocatoModel lAvvMod = new AvvocatoModel();
		lAvvMod.setIdAvvocato(new BigDecimal(lAvvId));

		AvvocatoFascicoloSigeModel lAvvFascMod = new AvvocatoFascicoloSigeModel();
		lAvvFascMod.setFasSigeIdFascicoloSige(
				((FascicoloSigeEstesoModel) getSessionAttribute("FascicoloSigeEsteso")).getFascicoloSige()
						.getIdFascicoloSige());
		// STUB 24/2/2003 (gianluca)
		// Ho lasciato il doppio model, eventualmente per affinare ricerche sull'avvocato
		// dato che in questa fase le esigenze non sono chiarissime
		// In questa fase è ovviamente ridondante.
		// To do : verificare effettiva necessità, e qualora serva, eliminare AvvocatoModel

		// chiama il controller
		// AvvocatoController lCtrl = new AvvocatoController();
		IAvvocato lCtrl = SIGELookupRemote.getAvvocatoRemote();
		Vector lVect = lCtrl.ExRicercaDifensoreAttualiFascicolo(lAvvMod, lAvvFascMod);
		AvvocatoSigeModel lAvv = new AvvocatoSigeModel((AvvocatoSigeModel) lVect.get(0));
		setRequestAttribute("modalita", "D");
		setRequestAttribute("avvocatoFascSige", lAvv);

		return PG_DETTAGLIO_AVVOCATO_SENTENZA;
	}

}