package siap.siep.istanza.action;

import java.math.BigDecimal;
import java.util.Vector;

import f3b.web.IWebConstants;
import siap.sico.soggetto.action.ICostantiSoggetto;
import siap.sico.soggetto.model.SoggettoModel;
import siap.sico.web.ActionSiap;
import siap.siep.istanza.controller.IIstanza;
import siap.siep.istanza.model.IstanzaSoggettoEventoFascicoloSiepModel;
import siap.siep.util.SIEPLookupRemote;

/**
 * <p>
 * Title: ActRicercaIstanzaSoggetto
 * </p>
 * <p>
 * Description: Classe Action per la ricerca Paginata delle Istanze presentate da un soggetto
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

public class ActRicercaIstanzaSoggetto extends ActionSiap implements ICostantiIstanza {
	@SuppressWarnings("rawtypes")
	public String processRequest() throws Exception {
		SoggettoModel lSogMod = new SoggettoModel();

		// riempie il model
		lSogMod.setCognome(getRequestStringParameter(ICostantiSoggetto.CAMPO_COGNOME).toUpperCase());
		lSogMod.setNome(getRequestStringParameter(ICostantiSoggetto.CAMPO_NOME).toUpperCase());

		String lPagina = "1";
		if (!isRequestParameterNullObj(IWebConstants.NUM_PAGE))
			lPagina = getRequestStringParameter(IWebConstants.NUM_PAGE);

		// chiama il controller
		IIstanza lCtrl = SIEPLookupRemote.getIstanzaRemote();
		Vector lVect = lCtrl.ExRicercaIstanzaSoggettoPaged(lSogMod, Integer.parseInt(lPagina));

		BigDecimal CountRisultati;
		if (isRequestParameterNullObj("CountRisultati")) {
			CountRisultati = lCtrl.ExGetCountIstanzaSoggettoPaged(lSogMod);
		} else
			CountRisultati = getRequestBigDecimalParameter("CountRisultati");

		setRequestAttribute("CountRisultati", CountRisultati);
		setRequestAttribute(IWebConstants.NUM_PAGE, lPagina);

		setRequestAttribute(IWebConstants.REQUEST_FOR_PAGING, getCompleteRequestURL());

		if (lVect.size() == 1 && lPagina.equals("1")) {
			IstanzaSoggettoEventoFascicoloSiepModel lISEFM = (IstanzaSoggettoEventoFascicoloSiepModel) lVect
					.get(0);

			String lReturnPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
					+ "=siap.siep.istanza.action.ActLoadDettaglioIstanza&" + CAMPO_ID_ISTANZA + "="
					+ lISEFM.getIstanza().getIdIstanza().toString();

			return lReturnPage;
		} else {
			setRequestAttribute("istsogevefasc", lVect);

			return PG_RICERCAISTANZA_SOGGETTI;
		}

	}
}
