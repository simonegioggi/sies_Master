package siap.siep.penaaccessoria.action;

import java.math.BigDecimal;
import java.util.Vector;

import siap.siep.beneficio.controller.IBeneficio;
import siap.siep.beneficio.model.BeneficioModel;
import siap.siep.penaaccessoria.controller.IPenaAccessoria;
import siap.siep.penaaccessoria.model.PenaAccessoriaModel;
import siap.siep.util.SIEPLookupRemote;
import siap.siep.web.ActSIESDettaglioProvvedimento;

/**
 * <p>
 * Title: ActLoadDettaglioPenaAccessoria
 * </p>
 * <p>
 * Description: Classe Action per la load dettaglio di PenaAccessoria
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: Bull
 * </p>
 * 
 * @version 1.0
 */

public class ActLoadDettaglioPenaAccessoria extends ActSIESDettaglioProvvedimento
		implements ICostantiPenaAccessoria {
	protected PenaAccessoriaModel mPenMod = new PenaAccessoriaModel();

	@SuppressWarnings("rawtypes")
	public String processRequest() throws Exception {
		// FascicoloSiepModel lFascMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");
		// Gestione ritorno
		setLinkRitorno();

		if (!this.isRequestAttributeNullObj("lTipoFunzione")) // paramentro passato solo nel caso di
																// iscrizione guidata
		{
			this.setRequestAttribute("lTipoFunzione", this.getRequestAttribute("lTipoFunzione"));
		}

		// paramentro passato solo nel caso di iscrizione guidata E VENGO DA DETTAGLIO SOGGETTO
		if (!this.isRequestParameterNullObj("lTipoFunzione")) {
			this.setRequestAttribute("lTipoFunzione", this.getRequestStringParameter("lTipoFunzione"));
		}

		String lId = getRequestStringParameter(CAMPO_ID_PENA_ACCESSORIA);
		// riempie il model
		mPenMod.setIdPenaAccessoria(new BigDecimal(lId));
		// chiama il controller

		IPenaAccessoria lCtrl = SIEPLookupRemote.getPenaAccessoriaRemote();
		Vector lVect = lCtrl.ExRicercaPenaAccessoria(mPenMod);
		mPenMod = new PenaAccessoriaModel((PenaAccessoriaModel) lVect.firstElement());
		setRequestAttribute("penaaccessoria", mPenMod);

		// ricerca beneficio relativo alla pena accessoria
		BeneficioModel lBenMod = new BeneficioModel();
		if (mPenMod != null && mPenMod.getBenIdBeneficio() != null) {
			IBeneficio lCtrlBen = SIEPLookupRemote.getBeneficioRemote();
			lBenMod = lCtrlBen.ExRicercaBeneficioByKey(mPenMod.getBenIdBeneficio());
		}

		setRequestAttribute("beneficio", lBenMod);

		return PG_LOAD_DETTAGLIOPENAACCESSORIA;
	}

}