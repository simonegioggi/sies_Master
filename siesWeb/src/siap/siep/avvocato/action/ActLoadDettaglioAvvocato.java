package siap.siep.avvocato.action;

/**
* <p>Title: ActLoadDettaglioAvvocato</p>
* <p>Description: Classe Action per la load dettaglio di Avvocato</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.math.BigDecimal;
import java.util.Vector;

import siap.sico.web.ActionSiap;
import siap.siep.avvocato.controller.IAvvocato;
import siap.siep.avvocato.model.AvvocatoFascicoloSiepModel;
import siap.siep.avvocato.model.AvvocatoModel;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.util.SIEPLookupRemote;
import f3b.util.F3BException;

@SuppressWarnings("rawtypes")
public class ActLoadDettaglioAvvocato extends ActionSiap implements ICostantiAvvocato {

	public String processRequest() throws F3BException {

		String lId = getRequestStringParameter(CAMPO_ID_AVVOCATO);

		// riempie il model
		AvvocatoModel lAvvMod = new AvvocatoModel();
		lAvvMod.setIdAvvocato(new BigDecimal(lId));
		AvvocatoFascicoloSiepModel lAvvFascMod = new AvvocatoFascicoloSiepModel();
		lAvvFascMod.setAvvIdAvvocato(new BigDecimal(lId));
		lAvvFascMod.setFasSieIdFascicoloSiep(
				((FascicoloSiepModel) getSessionAttribute("fascicolo")).getIdFascicoloSiep());
		// chiama il controller
		// AvvocatoController lCtrl = new AvvocatoController();
		IAvvocato lCtrl = SIEPLookupRemote.getAvvocatoRemote();
		Vector lVect = lCtrl.ExRicercaAvvocato(lAvvMod, lAvvFascMod);

		setRequestAttribute("modalita", "D");
		setRequestAttribute("avvocato", lVect);

		return PG_LOAD_INSERISCIAVVOCATO;
	}

}