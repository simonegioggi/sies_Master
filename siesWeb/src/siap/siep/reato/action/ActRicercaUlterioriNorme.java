package siap.siep.reato.action;

import java.util.Vector;

import f3b.web.IWebConstants;
import siap.sico.web.ActionSiap;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.reato.controller.IReato;
import siap.siep.reato.model.ReatoModel;
import siap.siep.util.SIEPLookupRemote;

/**
 * <p>
 * Title: ActRicercaReato
 * </p>
 * <p>
 * Description: Classe Action per la ricerca di Reato
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
public class ActRicercaUlterioriNorme extends ActionSiap implements ICostantiReato {

	@SuppressWarnings("rawtypes")
	public String processRequest() throws Exception {

		if (!this.isRequestAttributeNullObj("lTipoFunzione")) // paramentro passato solo nel caso di
																// iscrizione guidata
		{
			this.setRequestAttribute("lTipoFunzione", this.getRequestAttribute("lTipoFunzione"));
		}

		// paramentro passato solo nel caso di iscrizione guidata E VENGO DA DETTAGLIO SOGGETTO
		if (!this.isRequestParameterNullObj("lTipoFunzione")) {
			this.setRequestAttribute("lTipoFunzione", this.getRequestStringParameter("lTipoFunzione"));
		}

		ReatoModel lReaMod = new ReatoModel();

		lReaMod.setFasSieIdFascicoloSiep(
				((FascicoloSiepModel) getSessionAttribute("fascicolo")).getIdFascicoloSiep());

		if (!isRequestParameterNullObj(CAMPO_PROGR_REATO)) {
			lReaMod.setProgrReato(getRequestBigDecimalParameter(CAMPO_PROGR_REATO));
		}

		IReato lCtrl = SIEPLookupRemote.getReatoRemote();
		Vector lVect = lCtrl.ExRicercaReato(lReaMod);

		String lReturnPage = "";

		setSessionAttribute("reato", lVect.get(0));

		if (lVect.size() == 1) {
			lReturnPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
					+ "=siap.siep.reato.action.ActLoadDettaglioReato&" + CAMPO_ID_REATO + "="
					+ ((ReatoModel) lVect.get(0)).getIdReato().toString();
		} else {
			setSessionAttribute("reato", lVect.get(0));

			setRequestAttribute("reati", lVect);

			lReturnPage = PG_RICERCAULTERIORINORME;
		}

		return lReturnPage;
	}
}
