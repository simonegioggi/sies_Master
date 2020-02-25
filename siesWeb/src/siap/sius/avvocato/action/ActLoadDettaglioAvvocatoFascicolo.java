package siap.sius.avvocato.action;

/**
* <p>Title: ActLoadDettaglioAvvocatoFascicoloSius</p>
* <p>Description: Classe Action per la load dettaglio di Avvocato - Sentenza</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.math.BigDecimal;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.F3BException;
import siap.sico.web.ActionSiap;
import siap.sius.avvocato.controller.IAvvocato;
import siap.sius.avvocato.model.AvvocatoFascicoloSiusModel;
import siap.sius.avvocato.model.AvvocatoModel;
import siap.sius.avvocato.model.AvvocatoSiusModel;
import siap.sius.fascicolo.model.FascicoloGPModel;
import siap.sius.util.SIUSLookupRemote;

public class ActLoadDettaglioAvvocatoFascicolo extends ActionSiap implements ICostantiAvvocato {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	@SuppressWarnings("rawtypes")
	public String processRequest() throws F3BException {

		String lAvvId = getRequestStringParameter(CAMPO_ID_AVVOCATO);
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(" PAOLO lAvvId = " + lAvvId);

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

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(" PAOLO CONTR2 = " + lAvvMod.getIdAvvocato());
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(" PAOLO CONTR3 = " + lAvvFascMod.getAvvIdAvvocato());

		// chiama il controller
		// AvvocatoController lCtrl = new AvvocatoController();
		IAvvocato lCtrl = SIUSLookupRemote.getAvvocatoRemote();
		Vector lVect = lCtrl.ExRicercaDifensoreAttualiFascicolo(lAvvMod, lAvvFascMod);
		AvvocatoSiusModel lAvv = new AvvocatoSiusModel((AvvocatoSiusModel) lVect.get(0));
		setRequestAttribute("modalita", "D");
		setRequestAttribute("avvocatoFascSius", lAvv);

		// chiama il controller
		// IAvvocato lCtrl = SIUSLookupRemote.getAvvocatoRemote();
		// AvvocatoSiusModel lAvvFascMod = lCtrl.ExRicercaAvvocatoByKeyAvvocatoFasSius(lAvvId);

		// setRequestAttribute("modalita", "D");
		// setRequestAttribute("avvocatoFascSius", lAvvFascMod);

		return PG_DETTAGLIO_AVVOCATO_SENTENZA;
	}

}