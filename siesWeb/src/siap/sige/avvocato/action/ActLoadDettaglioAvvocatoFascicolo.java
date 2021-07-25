package siap.sige.avvocato.action;

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