package siap.sige.udienzaparti.action;

import java.math.BigDecimal;
import java.util.Vector;

import siap.sico.web.ActionSiap;
import siap.siep.avvocato.action.ICostantiAvvocato;
//import siap.siep.avvocato.controller.AvvocatoController;
import siap.siep.avvocato.controller.IAvvocato;
import siap.siep.avvocato.model.AvvocatoModel;
import siap.siep.util.SIEPLookupRemote;
import f3b.util.F3BException;

/**
 * <p>
 * Title: ActDettaglioAvvocato
 * </p>
 * <p>
 * Description: Classe Action per il dettaglio di Avvocato
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: Engineering S.p.A.
 * </p>
 * 
 * @version 1.0
 */
@SuppressWarnings("unchecked")
public class ActDettaglioAvvocato extends ActionSiap implements ICostantiAvvocato {

	public String processRequest() throws F3BException {

		String lAvvId = getRequestStringParameter(CAMPO_ID_AVVOCATO);

		// riempie il model
		AvvocatoModel lAvvMod = new AvvocatoModel();
		lAvvMod.setIdAvvocato(new BigDecimal(lAvvId));

		IAvvocato lCtrl = SIEPLookupRemote.getAvvocatoRemote();
		Vector<AvvocatoModel> lVect = lCtrl.ExRicercaAvvocatoPerInserimento(lAvvMod);
		AvvocatoModel lAvv = new AvvocatoModel((AvvocatoModel) lVect.get(0));

		setRequestAttribute("modalita", "D");
		setRequestAttribute("avvocato", lAvv);

		return ICostantiPartiUdienza.PG_DETTAGLIO_AVVOCATO;
	}

}