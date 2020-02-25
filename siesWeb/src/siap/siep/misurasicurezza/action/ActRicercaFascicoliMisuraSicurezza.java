package siap.siep.misurasicurezza.action;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import siap.sico.web.ActionSiap;
import siap.siep.misurasicurezza.controller.IMisuraSicurezza;
import siap.siep.misurasicurezza.model.MisuraSicurezzaFascicoloModel;
import siap.siep.misurasicurezza.model.MisuraSicurezzaModel;
import siap.siep.util.SIEPLookupRemote;
import siap.web.ISIAPCostantiWeb;
import f3b.web.IWebConstants;

/**
 * <p>
 * Title: ActRicercaFascicoliMisuraSicurezza
 * </p>
 * <p>
 * Description: Classe Action per la ricerca dei Fascicoli siep con Misure di Sicurezza
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
public class ActRicercaFascicoliMisuraSicurezza extends ActionSiap implements ICostantiMisuraSicurezza {

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String processRequest() throws Exception {

		String lPagina = "1";
		if (!isRequestParameterNullObj(IWebConstants.NUM_PAGE)) {
			lPagina = getRequestStringParameter(IWebConstants.NUM_PAGE);
		}

		List lListMis = new ArrayList();

		IMisuraSicurezza lCtrl = SIEPLookupRemote.getMisuraSicurezzaRemote();

		lListMis = lCtrl.ExRicercaFascicoliMisuraSicurezza(getCodUfficioUtenteConnesso(),
				Integer.parseInt(lPagina));

		if (lListMis.size() == 1) {
			// MisuraSicurezzaModel lMisSicMod = (MisuraSicurezzaModel)lListMis.get(0);
			MisuraSicurezzaFascicoloModel lMisSicFascMod = (MisuraSicurezzaFascicoloModel) lListMis.get(0);
			List<MisuraSicurezzaModel> lListaMis = lMisSicFascMod.getMisureSicurezza();

			return IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
					+ "=siap.siep.misurasicurezza.action.ActLoadDettaglioMisuraSicurezza&"
					// +ICostantiMisuraSicurezza.CAMPO_ID_MISURA_SICUREZZA+"="+lMisSicMod.getIdMisuraSicurezza();
					+ ICostantiMisuraSicurezza.CAMPO_ID_MISURA_SICUREZZA + "="
					+ lListaMis.get(0).getIdMisuraSicurezza();
		} else {
			BigDecimal CountRisultati;
			if (isRequestParameterNullObj("CountRisultati")) {
				CountRisultati = lCtrl.ExGetCountFascicoliMisuraSicurezza(getCodUfficioUtenteConnesso());
			} else {
				CountRisultati = getRequestBigDecimalParameter("CountRisultati");
			}

			setRequestAttribute("CountRisultati", CountRisultati);
			setRequestAttribute(IWebConstants.NUM_PAGE, lPagina);
			setRequestAttribute(IWebConstants.REQUEST_FOR_PAGING, getCompleteRequestURL());

			String lAzione = "siap.siep.misurasicurezza.action.ActRicercaFascicoliMisuraSicurezza";
			setRequestAttribute(ISIAPCostantiWeb.CAMPO_AZIONE_CHIAMANTE, lAzione);

			setRequestAttribute("fascicolimisurasicurezza", lListMis);

			return PG_RICERCA_FASCICOLI_MISURASICUREZZA;
		}
	}

}