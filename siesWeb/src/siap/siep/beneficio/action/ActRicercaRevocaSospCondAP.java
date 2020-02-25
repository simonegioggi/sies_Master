package siap.siep.beneficio.action;

import java.util.Iterator;
import java.util.Vector;

import f3b.util.F3BException;
import siap.sico.web.ActionSiap;
import siap.siep.beneficio.controller.IBeneficio;
import siap.siep.beneficio.model.BeneficioModel;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.util.SIEPLookupRemote;

/**
 * <p>
 * Title: ActRicercaRevocaSospCondAP
 * </p>
 * <p>
 * Description: Classe Action per la ricerca dei Benefici Revocati
 * </p>
 * <p>
 * (di tipo Sospensione Condizionale e Non Menzione)
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

public class ActRicercaRevocaSospCondAP extends ActionSiap implements ICostantiBeneficio {
	@SuppressWarnings("rawtypes")
	public String processRequest() throws F3BException {

		BeneficioModel lBenMod = new BeneficioModel();

		lBenMod.setFasSieIdFascicoloSiep(
				((FascicoloSiepModel) getSessionAttribute("fascicolo")).getIdFascicoloSiep());
		lBenMod.setCodNaturaBeneficio("R");

		// Ricerca tutte le revoche (di tipo 01 - Sospensione condizionale- ,
		// 02 - Non Menzione -
		// 03 - Indulto - )
		// nella form successiva avviene la selezione, e cosi' a video vanno solo i tipi 01 e 02

		IBeneficio lCtrl = SIEPLookupRemote.getBeneficioRemote();
		Vector lBeneficii = lCtrl.ExRicercaBeneficio(lBenMod);

		int TotRBene = 0;
		Iterator lIterBenefici1 = lBeneficii.iterator();

		while (lIterBenefici1.hasNext()) {
			BeneficioModel lBene1 = (BeneficioModel) lIterBenefici1.next();
			if (lBene1.getCodTipoBeneficio().equals("01") || lBene1.getCodTipoBeneficio().equals("02"))
				TotRBene = TotRBene + 1;
		}

		if (TotRBene == 0) {
			throw new F3BException(F3BException.USER_MESSAGE, "Nessun Elemento trovato");
		}

		// setta la risposta nella request
		setRequestAttribute("benefici", lBeneficii);

		setRequestAttribute("ComingFromInsert", "YES");

		if (!this.isRequestParameterNullObj(CAMPO_PROVENIENZA))
			setRequestAttribute("ComingFrom", getRequestStringParameter(CAMPO_PROVENIENZA));

		return PG_RICERCA_REVOCA_SOSPCOND;
	}

}
