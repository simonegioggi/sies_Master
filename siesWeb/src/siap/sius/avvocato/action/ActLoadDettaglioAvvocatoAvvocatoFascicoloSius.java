package siap.sius.avvocato.action;

/**
* <p>Title: ActLoadDettaglioAvvocatoAvvocatoFascicoloSius</p>
* <p>Description: Classe Action per la load dettaglio di Avvocato - Sentenza</p>
* <p>Copyright: Copyright (c) 2004</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.math.BigDecimal;
import java.util.Vector;

import f3b.util.F3BException;
import siap.sico.web.ActionSiap;
import siap.siep.istitutodetenzione.controller.IIstitutoDetenzione;
import siap.siep.istitutodetenzione.model.IstitutoDetenzioneModel;
import siap.siep.util.SIEPLookupRemote;
//import siap.sius.avvocato.controller.AvvocatoController;
import siap.sius.avvocato.controller.IAvvocato;
import siap.sius.avvocato.model.AvvocatoFascicoloSiusModel;
import siap.sius.avvocato.model.AvvocatoModel;
import siap.sius.avvocato.model.AvvocatoSiusModel;
import siap.sius.fascicolo.model.FascicoloGPModel;
import siap.sius.util.SIUSLookupRemote;

public class ActLoadDettaglioAvvocatoAvvocatoFascicoloSius extends ActionSiap implements ICostantiAvvocato {

	@SuppressWarnings("rawtypes")
	public String processRequest() throws F3BException {

		String lAvvId = getRequestStringParameter(CAMPO_ID_AVVOCATO);

		// riempie il model
		AvvocatoModel lAvvMod = new AvvocatoModel();
		lAvvMod.setIdAvvocato(new BigDecimal(lAvvId));
		AvvocatoFascicoloSiusModel lAvvFascMod = new AvvocatoFascicoloSiusModel();
		lAvvFascMod.setFasSiuIdFascicoloSius(((FascicoloGPModel) getSessionAttribute("fascicoloSiusGP"))
				.getFascicoloSiusModel().getIdFascicoloSius());
		// STUB 24/2/2003 (gianluca)
		// Ho lasciato il doppio model, eventualmente per affinare ricerche sull'avvocato
		// dato che in questa fase le esigenze non sono chiarissime
		// In questa fase è ovviamente ridondante.
		// To do : verificare effettiva necessità, e qualora serva, eliminare AvvocatoModel

		// chiama il controller
		// AvvocatoController lCtrl = new AvvocatoController();
		IAvvocato lCtrl = SIUSLookupRemote.getAvvocatoRemote();
		Vector lVect = lCtrl.ExRicercaAvvocatoFascicoloSiusByKeyAvvocato(lAvvMod.getIdAvvocato());

		AvvocatoSiusModel lAvv = new AvvocatoSiusModel((AvvocatoSiusModel) lVect.get(0));

		IIstitutoDetenzione lCtrlIstituto = SIEPLookupRemote.getIstitutoDetenzioneRemote();
		IstitutoDetenzioneModel lIstMod = new IstitutoDetenzioneModel();

		lIstMod = lCtrlIstituto.ExRicercaIstitutoDetenzioneByKey(
				lAvv.getAvvocatoFascicoloSiusModel().getIstDetIdIstitutoDetenzione());

		setRequestAttribute("modalita", "D");
		setRequestAttribute("avvocato", lAvv);
		setRequestAttribute("lIstMod", lIstMod);

		return PG_DETTAGLIO_AVVOCATO_AVVOCATO_FASCICOLO_SIUS;
	}

}