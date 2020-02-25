package siap.siep.beneficio.action;

import java.math.BigDecimal;
import java.util.Iterator;
import java.util.Vector;

import f3b.util.F3BException;
import siap.sico.web.ActionSiap;
import siap.siep.beneficio.controller.IBeneficio;
import siap.siep.beneficio.model.BeneficioModel;
import siap.siep.beneficio.model.BeneficioPenaAccessoriaModel;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.penaaccessoria.controller.IPenaAccessoria;
import siap.siep.penaaccessoria.model.PenaAccessoriaModel;
import siap.siep.penaresidua.controller.IPenaResidua;
import siap.siep.penaresidua.model.PenaResiduaModel;
import siap.siep.util.SIEPLookupRemote;

/**
 * <p>
 * Title: ActRicercaBeneficio
 * </p>
 * <p>
 * Description: Classe Action per la ricerca di Beneficio
 * </p>
 * <p>
 * Copyright: Copyright (c) 2008
 * </p>
 * <p>
 * Company: Eutelia
 * </p>
 * 
 * @version 1.0
 */
public class ActRicercaBeneficio extends ActionSiap implements ICostantiBeneficio {

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String processRequest() throws F3BException {

		BeneficioModel lBenMod = new BeneficioModel();

		lBenMod.setFasSieIdFascicoloSiep(
				((FascicoloSiepModel) getSessionAttribute("fascicolo")).getIdFascicoloSiep());
		if (!isRequestParameterNullObj(CAMPO_COD_NATURA_BENEFICIO)) {
			lBenMod.setCodNaturaBeneficio(getRequestStringParameter(CAMPO_COD_NATURA_BENEFICIO));
		} else {
			lBenMod.setCodNaturaBeneficio("C");
		}

		if (!isRequestParameterNullObj(CAMPO_COD_TIPO_BENEFICIO)) {
			lBenMod.setCodTipoBeneficio(getRequestStringParameter(CAMPO_COD_TIPO_BENEFICIO));
		}

		IBeneficio lCtrl = SIEPLookupRemote.getBeneficioRemote();
		Vector lVect = lCtrl.ExRicercaBeneficio(lBenMod);

		if (lVect != null && lVect.size() == 0) {
			throw new F3BException(F3BException.USER_MESSAGE, "Nessun Elemento trovato");
		}

		Vector lPenBenVec = new Vector();
		BeneficioPenaAccessoriaModel lBenPenMod = null;
		if (lVect != null) {
			Iterator itx = lVect.iterator();
			while (itx.hasNext()) {
				lBenPenMod = new BeneficioPenaAccessoriaModel();
				BeneficioModel lBenModel = (BeneficioModel) itx.next();
				lBenPenMod.setBeneficio(lBenModel);

				if (lBenModel != null && lBenModel.getIdBeneficio() != null) {
					PenaAccessoriaModel lPenMod = new PenaAccessoriaModel();
					lPenMod.setFasSieIdFascicoloSiep(lBenModel.getFasSieIdFascicoloSiep());
					lPenMod.setBenIdBeneficio(lBenModel.getIdBeneficio());
					IPenaAccessoria lCtrlPen = SIEPLookupRemote.getPenaAccessoriaRemote();
					Vector lVectPenaAcc = lCtrlPen.ExRicercaPenaAccessoriaNoError(lPenMod);

					if (lVectPenaAcc != null && !lVectPenaAcc.isEmpty()) {
						lBenPenMod.setPresenzaPenaAccessoria(true);
					}

				}
				lPenBenVec.add(lBenPenMod);
			}
		}

		setRequestAttribute("beneficiopenaaccessoria", lPenBenVec);

		// richiesta asir a9/rr/075 04-06-2009 SIEP MEV - Warning sul primo calcolo della pena
		// paolo cherubini lunedi 11/10/2010
		// Ricerca l'ultima pena residua per quel fascicolo
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
		// fine a9/rr/075

		return PG_RICERCABENEFICIO;
	}

}