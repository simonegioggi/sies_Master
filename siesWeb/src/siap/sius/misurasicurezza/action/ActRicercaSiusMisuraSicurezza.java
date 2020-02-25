package siap.sius.misurasicurezza.action;

/**
 * <p>Title: ActRicercaRichiestaRemissione</p>
 * <p>Description: Classe Action per la ricerca di Misura Sicurezza</p>
 * <p>Copyright: Copyright (c) 2008</p>
 * <p>Company: </p>
 * @version 1.0
 */

import java.util.Iterator;
import java.util.Vector;

import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoModel;
import siap.sico.util.SICOLookupRemote;
import siap.siep.misurasicurezza.controller.MisuraSicurezzaController;
import siap.siep.misurasicurezza.model.MisuraSicurezzaModel;
import siap.sius.ActionSius;
import siap.sius.fascicolo.model.FascicoloGPModel;
import f3b.util.DateUtils;

public class ActRicercaSiusMisuraSicurezza extends ActionSius implements ICostantiSiusMisuraSicurezza {

	@SuppressWarnings("rawtypes")
	public String processRequest() throws Exception {

		// Attivazione punto di Ritorno
		setLinkRitorno();

		FascicoloGPModel lFasGPMod = (FascicoloGPModel) getSessionAttribute("fascicoloSiusGP");

		// Lettura elenco Misure di Sicurezza Collegate al Fascicolo SIUS.
		MisuraSicurezzaModel aMisuraSicurezza = new MisuraSicurezzaModel();
		aMisuraSicurezza.setFasSiuIdFascicoloSius(lFasGPMod.getFascicoloSiusModel().getIdFascicoloSius());
		MisuraSicurezzaController lCtrl = new MisuraSicurezzaController();
		Vector lVect = lCtrl.ExRicercaMisuraSicurezzaAndRifTitoloEsec(aMisuraSicurezza);

		// Leggere l'eventuale data di fine validità per una misura come data di un eventuale ordinanza che la
		// trasformi
		Iterator itx = lVect.iterator();
		while (itx.hasNext()) {
			MisuraSicurezzaModel lMisuraSicurezza = (MisuraSicurezzaModel) itx.next();
			if (lMisuraSicurezza.getEveIdEvento() != null) {
				IEvento lCtrEv = SICOLookupRemote.getEventoRemote();
				EventoModel lEve = lCtrEv.ExRicercaEventoByKey(lMisuraSicurezza.getEveIdEvento());
				if (lEve != null && lEve.getDataInserimento() != null) {
					setRequestAttribute("dataFineValidita",
							DateUtils.getDateToString(lEve.getDataInserimento(), "dd/MM/yyyy"));
					break;
				}
			}
		}

		String lModificabile = "NO";
		if (this.IsFascicoloSiusModificabile() == true)
			lModificabile = "SI";
		else
			lModificabile = "NO";
		this.setRequestAttribute("isModificabile", lModificabile);

		setRequestAttribute("misureSicurezza", lVect);

		return PG_RICERCA_SIUS_MISURASICUREZZA;
	}

}