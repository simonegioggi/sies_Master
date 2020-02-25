package siap.siep.beneficio.action;

import java.util.Vector;

import f3b.util.F3BException;
import siap.sico.web.ActionSiap;
import siap.siep.beneficio.controller.IBeneficio;
import siap.siep.beneficio.model.BeneficioModel;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.util.SIEPLookupRemote;

/**
 * <p>
 * Title: ActRicercaRevocaIndultoAP
 * </p>
 * <p>
 * Description: Classe Action per la ricerca dei Benefici Revocati (di tipo Indulto)
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

public class ActRicercaRevocaIndultoAP extends ActionSiap implements ICostantiBeneficio {
	@SuppressWarnings("rawtypes")
	public String processRequest() throws F3BException {

		BeneficioModel lBenMod = new BeneficioModel();

		lBenMod.setFasSieIdFascicoloSiep(
				((FascicoloSiepModel) getSessionAttribute("fascicolo")).getIdFascicoloSiep());
		lBenMod.setCodNaturaBeneficio("R");
		lBenMod.setCodTipoBeneficio("03");

		IBeneficio lCtrl = SIEPLookupRemote.getBeneficioRemote();
		Vector lBeneficii = lCtrl.ExRicercaBeneficio(lBenMod);

		if (lBeneficii.size() == 0) {
			throw new F3BException(F3BException.USER_MESSAGE, "Nessun Elemento trovato");
		}

		// setta la risposta nella request
		setRequestAttribute("benefici", lBeneficii);

		setRequestAttribute("ComingFromInsert", "YES");
		if (!this.isRequestParameterNullObj(CAMPO_PROVENIENZA))
			setRequestAttribute("ComingFrom", getRequestStringParameter(CAMPO_PROVENIENZA));

		return PG_RICERCA_REVOCA_INDULTO;
	}

}
