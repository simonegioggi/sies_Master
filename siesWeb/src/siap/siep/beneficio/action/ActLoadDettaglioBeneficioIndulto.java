package siap.siep.beneficio.action;

import java.math.BigDecimal;
import java.util.Vector;

import siap.sico.web.ActionSiap;
import siap.siep.beneficio.controller.IBeneficio;
import siap.siep.beneficio.model.BeneficioModel;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.penaaccessoria.controller.IPenaAccessoria;
import siap.siep.penaaccessoria.model.PenaAccessoriaModel;
import siap.siep.penaresidua.controller.IPenaResidua;
import siap.siep.penaresidua.model.PenaResiduaModel;
import siap.siep.util.SIEPLookupRemote;

/**
 * <p>
 * Title: ActLoadDettaglioBeneficioIndulto
 * </p>
 * <p>
 * Description: Classe Action per la load dettaglio di Beneficio Indulto
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

public class ActLoadDettaglioBeneficioIndulto extends ActionSiap implements ICostantiBeneficio {
	protected BeneficioModel mBenMod = null;

	@SuppressWarnings("rawtypes")
	public String processRequest() throws Exception {

		BigDecimal lId = getRequestBigDecimalParameter(CAMPO_ID_BENEFICIO);

		IBeneficio lCtrl = SIEPLookupRemote.getBeneficioRemote();
		mBenMod = lCtrl.ExRicercaBeneficioByKey(lId);
		setRequestAttribute("beneficio", mBenMod);

		PenaAccessoriaModel lPenAccMod = new PenaAccessoriaModel();
		lPenAccMod.setBenIdBeneficio(mBenMod.getIdBeneficio());
		lPenAccMod.setFasSieIdFascicoloSiep(mBenMod.getFasSieIdFascicoloSiep());

		IPenaAccessoria lCtrlPen = SIEPLookupRemote.getPenaAccessoriaRemote();
		Vector lPenAcce = lCtrlPen.ExRicercaPenaAccessoriaNoError(lPenAccMod);
		setRequestAttribute("peneaccessorie", lPenAcce);

		// per iscrizione guidata
		if (!this.isRequestAttributeNullObj("lTipoFunzione")) // paramentro passato solo nel caso di
																// iscrizione guidata
		{
			this.setRequestAttribute("lTipoFunzione", this.getRequestAttribute("lTipoFunzione"));
		}

		// richiesta asir a9/rr/075 04-06-2009 SIEP MEV - Warning sul primo calcolo della pena
		// paolo cherubini lunedi 11/10/2010
		// Ricerca l'ultima pena residua per quel fascicolo
		if (!isSessionAttributeNullObj("fascicolo")) {
			BigDecimal lIdFascicolo = ((FascicoloSiepModel) getSessionAttribute("fascicolo"))
					.getIdFascicoloSiep();
			FascicoloSiepModel lFascMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");
			if (lFascMod.getFlagValidato().equals("N")) {
				IPenaResidua lPenResCtrl = SIEPLookupRemote.getPenaResiduaRemote();
				PenaResiduaModel lPenaResMod = lPenResCtrl
						.ExRicercaPenaResiduaCorrenteByFascicoloSiep(lIdFascicolo);
				if (lPenaResMod != null && lPenaResMod.getFlagValidato().equals("N")) {
					setRequestAttribute("lPenaResMod", lPenaResMod);
				}
			}
		}
		// fine a9/rr/075

		return PG_LOAD_DETTAGLIO_BENEFICIO_INDULTO;
	}

}