package siap.siep.beneficio.action;

import java.math.BigDecimal;
import java.util.Vector;

import f3b.web.IWebConstants;
import siap.sico.web.ActionSiap;
import siap.siep.beneficio.controller.IBeneficio;
import siap.siep.beneficio.model.BeneficioModel;
import siap.siep.tipologiaorario.controller.ITipologiaOrario;
import siap.siep.util.SIEPLookupRemote;

/**
 * <p>
 * Title: ActLoadDettaglioBeneficio
 * </p>
 * <p>
 * Description: Classe Action per la load dettaglio di Beneficio
 * </p>
 * <p>
 * Copyright: Copyright (c) 2008
 * </p>
 * <p>
 * Company: Eutelia
 * </p>
 * 
 * @version 3.0
 */

public class ActLoadDettaglioBeneficio extends ActionSiap implements ICostantiBeneficio {
	protected BeneficioModel mBenMod = null;
	// indica Beneficio di tipo Indulto
	protected boolean isBeneficioIndulto = false;

	@SuppressWarnings("rawtypes")
	public String processRequest() throws Exception {

		String lId = getRequestStringParameter(CAMPO_ID_BENEFICIO);

		IBeneficio lCtrl = SIEPLookupRemote.getBeneficioRemote();
		mBenMod = lCtrl.ExRicercaBeneficioByKey(new BigDecimal(lId));
		setRequestAttribute("beneficio", mBenMod);

		// per iscrizione guidata
		if (!this.isRequestAttributeNullObj("lTipoFunzione")) // paramentro passato solo nel caso di
																// iscrizione guidata
		{
			this.setRequestAttribute("lTipoFunzione", this.getRequestAttribute("lTipoFunzione"));
		}

		if (mBenMod != null && ("03".equals(mBenMod.getCodTipoBeneficio())
				|| "04".equals(mBenMod.getCodTipoBeneficio()))) {

			isBeneficioIndulto = true;
			String lPage = "";
			lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
					+ "=siap.siep.beneficio.action.ActLoadDettaglioBeneficioIndulto&" + CAMPO_ID_BENEFICIO
					+ "=" + lId;

			return lPage;
		}

		// controllo se è stata inserita una non menzione
		BeneficioModel llBenNMMod = lCtrl.ExRicercaBeneficioByBenIdBeneficio(mBenMod.getIdBeneficio());
		setRequestAttribute("beneficiononmenzione", llBenNMMod);

		ITipologiaOrario CtrlTip = SIEPLookupRemote.getTipologiaOrarioRemote();
		Vector lTipilogia = CtrlTip.ExRicercaTipologiaOrarioByIdBeneficio(mBenMod.getIdBeneficio());
		setRequestAttribute("tipologiaorario", lTipilogia);

		return PG_LOAD_DETTAGLIOBENEFICIO;
	}

}