package siap.sico.residenza.action;

/**
* <p>Title: ActLoadDettaglioResidenza</p>
* <p>Description: Classe Action per la load dettaglio di Residenza</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.math.BigDecimal;
import java.util.Vector;

import siap.sico.residenza.controller.ResidenzaController;
import siap.sico.residenza.model.ResidenzaModel;
import siap.sico.web.ActionSiap;
import f3b.util.F3BException;

@SuppressWarnings("rawtypes")
public class ActLoadDettaglioResidenza extends ActionSiap implements ICostantiResidenza {

	public String processRequest() throws F3BException {

		BigDecimal lId = getRequestBigDecimalParameter(CAMPO_ID_RESIDENZA);

		// riempie il model
		ResidenzaModel lResMod = new ResidenzaModel();
		lResMod.setIdResidenza(lId);

		// chiama il controller
		ResidenzaController lCtrl = new ResidenzaController();

		Vector lVect = lCtrl.ExRicercaResidenza(lResMod);
		if (lVect.size() != 0)
			lResMod = (ResidenzaModel) lVect.get(0);

		if (!this.isRequestAttributeNullObj("lTipoFunzione")) // paramentro passato solo nel caso di
																// iscrizione guidata
		{
			this.setRequestAttribute("lTipoFunzione", this.getRequestAttribute("lTipoFunzione"));
		} else {
			// paolo cherubini aggiunta righe elenco residenza 6 agosto 2009
			ResidenzaModel lResModNew = new ResidenzaModel();
			lResModNew.setSogIdSoggetto(lResMod.getSogIdSoggetto());
			lResModNew.setCodTipoResidenza(lResMod.getCodTipoResidenza());
			lVect = lCtrl.ExRicercaResidenza(lResModNew);
			setRequestAttribute("residenze", lVect);
		}
		// paramentro passato solo nel caso di iscrizione guidata E VENGO DA DETTAGLIO Residenza
		if (!this.isRequestParameterNullObj("lTipoFunzione")) {
			this.setRequestAttribute("lTipoFunzione", this.getRequestStringParameter("lTipoFunzione"));
		}

		setRequestAttribute("residenza", lResMod);
		return PG_DETTAGLIO_RES_DOM;
	}

}