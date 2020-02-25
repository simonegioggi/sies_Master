package siap.sige.avvocato.action;

/**
* <p>Title: ActLoadDettaglioAvvocatoFascicoloSius</p>
* <p>Description: Classe Action per la load dettaglio di Avvocato Sostituito</p>
* <p>Copyright: Copyright (c) 2008</p>
* <p>Company: Eutelia</p>
* @version 1.0
*/

import java.math.BigDecimal;
import java.util.Vector;

import f3b.util.F3BException;
import siap.sico.avvocato.model.AvvocatoModel;
import siap.sico.web.ActionSiap;
import siap.siep.istitutodetenzione.controller.IIstitutoDetenzione;
import siap.siep.istitutodetenzione.model.IstitutoDetenzioneModel;
import siap.siep.util.SIEPLookupRemote;
import siap.sige.avvocato.controller.IAvvocato;
import siap.sige.avvocato.model.AvvocatoFascicoloSigeModel;
import siap.sige.avvocato.model.AvvocatoSigeModel;
import siap.sige.fascicolo.model.FascicoloSigeEstesoModel;
import siap.sige.util.SIGELookupRemote;

public class ActLoadDettaglioAvvocatoFascicoloSige extends ActionSiap implements ICostantiAvvocato {

	@SuppressWarnings("rawtypes")
	public String processRequest() throws F3BException {

		String lAvvId = getRequestStringParameter(CAMPO_ID_AVVOCATO);

		// riempie il model
		AvvocatoModel lAvvMod = new AvvocatoModel();
		lAvvMod.setIdAvvocato(new BigDecimal(lAvvId));
		AvvocatoFascicoloSigeModel lAvvFascMod = new AvvocatoFascicoloSigeModel();
		lAvvFascMod.setFasSigeIdFascicoloSige(
				((FascicoloSigeEstesoModel) getSessionAttribute("FascicoloSigeEsteso")).getFascicoloSige()
						.getIdFascicoloSige());

		// chiama il controller
		// AvvocatoController lCtrl = new AvvocatoController();
		IAvvocato lCtrl = SIGELookupRemote.getAvvocatoRemote();
		Vector lVect = lCtrl.ExRicercaAvvocatoFascicoloSigeByKeyAvvocato(lAvvMod.getIdAvvocato());

		AvvocatoSigeModel lAvv = new AvvocatoSigeModel((AvvocatoSigeModel) lVect.get(0));

		IIstitutoDetenzione lCtrlIstituto = SIEPLookupRemote.getIstitutoDetenzioneRemote();
		IstitutoDetenzioneModel lIstMod = new IstitutoDetenzioneModel();

		lIstMod = lCtrlIstituto.ExRicercaIstitutoDetenzioneByKey(
				lAvv.getAvvocatoFascicoloSigeModel().getIstDetIdIstitutoDetenzione());

		setRequestAttribute("modalita", "D");
		setRequestAttribute("avvocato", lAvv);
		setRequestAttribute("lIstMod", lIstMod);

		return PG_DETTAGLIO_AVVOCATO_FASCICOLO_SIGE;
	}

}