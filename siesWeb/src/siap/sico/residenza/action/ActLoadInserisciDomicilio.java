package siap.sico.residenza.action;

/**
* <p>Title: ActLoadInserisciDomicilio</p>
* <p>Description: Classe Action per la load inserisci di Domicilio</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.util.Vector;

import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.residenza.controller.ResidenzaController;
import siap.sico.residenza.model.ResidenzaModel;
import siap.sico.soggetto.model.SoggettoModel;
import siap.sico.web.ActionSiap;
import f3b.util.F3BException;
import f3b.web.html.Option;

@SuppressWarnings("rawtypes")
public class ActLoadInserisciDomicilio extends ActionSiap implements ICostantiResidenza {

	public String processRequest() throws F3BException {

		// Per default ITALIA (039)
		Option lOption = new Option(DecodificheManager.getInstance().getNazioni(), "039");
		setRequestAttribute("nazioni", "" + lOption);

		// Imposta Modalità.
		setRequestAttribute("modalita", "I");

		if (!this.isRequestParameterNullObj("lTipoFunzione")) // paramentro passato solo nel caso di
																// iscrizione guidata
		{
			this.setRequestAttribute("lTipoFunzione", this.getRequestStringParameter("lTipoFunzione"));
		} else {
			// paolo cherubini aggiunta righe elenco residenza 6 agosto 2009
			ResidenzaModel lResMod = new ResidenzaModel();
			lResMod.setSogIdSoggetto(((SoggettoModel) getSessionAttribute("soggetto")).getIdSoggetto());
			lResMod.setCodTipoResidenza("D");
			ResidenzaController lCtrl = new ResidenzaController();
			try {
				Vector lVect = lCtrl.ExRicercaResidenza(lResMod);
				setRequestAttribute("residenze", lVect);
			} catch (Exception e) {
				// Nessun Elemento Trovato
			}

		}
		return PG_LOAD_INSERISCIDOMICILIO; // restituisce la jsp di VIEW
	}

}