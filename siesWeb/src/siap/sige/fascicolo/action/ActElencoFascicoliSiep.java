package siap.sige.fascicolo.action;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Vector;

import f3b.web.IWebConstants;
import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.soggetto.controller.ISoggetto;
import siap.sico.soggetto.model.SoggettoModel;
import siap.sico.util.SICOLookupRemote;
import siap.siep.fascicolo.action.ICostantiFascicoloSiep;
import siap.siep.fascicolo.controller.IFascicoloSiep;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.util.SIEPLookupRemote;
import siap.sige.web.ActionSige;
import siap.sius.fascicolo.action.ICostantiFascicoloSius;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author unascribed
 * @version 1.0
 */

public class ActElencoFascicoliSiep extends ActionSige {
	@SuppressWarnings("rawtypes")
	public String processRequest() throws Exception {
		SoggettoModel lSogMod = new SoggettoModel();

		String lPagina = "1";
		if (!isRequestParameterNullObj(IWebConstants.NUM_PAGE))
			lPagina = getRequestStringParameter(IWebConstants.NUM_PAGE);

		// Si ricava ID Soggetto dal Fascicolo in sessione
		lSogMod.setIdSoggetto(getFascicoloSigeInSessione().getSogIdSoggetto());

		// paolo cherubini x supersoggetto 03/08/2009
		ISoggetto lSogCtrl = SICOLookupRemote.getSoggettoRemote();
		lSogMod = lSogCtrl.ExRicercaSoggettoByKey(getFascicoloSigeInSessione().getSogIdSoggetto());
		lSogMod.setIdSoggetto(new BigDecimal(0));
		// fine paolo

		// chiama il controller
		IFascicoloSiep lFascSogCtrl = SIEPLookupRemote.getFascicoloSiepRemote();
		Vector<FascicoloSiepModel> lFascicoliSoggetti = lFascSogCtrl
				.ExRicercaFascicoloSiepBySuperSoggettoPerSIGEPaged(lSogMod, Integer.parseInt(lPagina));

		String lReturnPage = "";
		if (lFascicoliSoggetti.size() == 1) {
			gestioneRitorno();

			FascicoloSiepModel lFascicoloSoggettoSiep = new FascicoloSiepModel(
					lFascicoliSoggetti.firstElement());

			// Inserisce il model fasciciolo-soggetto sessione
			setSessionAttribute("fascicolo", lFascicoloSoggettoSiep);

			// Inserisce il model soggetto-soggetto nella request
			setRequestAttribute("fascicolo", lFascicoloSoggettoSiep);

			lReturnPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
					+ "=siap.siep.fascicolo.action.ActLoadDettaglioFascicolo&"
					+ ICostantiFascicoloSiep.CAMPO_ID_FASCICOLO_SIEP + "="
					+ lFascicoloSoggettoSiep.getIdFascicoloSiep();

		} else {
			setLinkRitorno();

			setRequestAttribute("titolo", "Elenco Procedimenti SIEP del Soggetto");

			// Collection di decodifica del COD_STATO
			Collection lCol = DecodificheManager.getInstance().getStatoProcedimento();
			setRequestAttribute("CodStato", lCol);

			// Paginazione
			BigDecimal CountRisultati;
			if (isRequestParameterNullObj("CountRisultati"))
				CountRisultati = lFascSogCtrl.ExGetNumFascicoloSiepBySuperSoggetto(lSogMod);
			else
				CountRisultati = getRequestBigDecimalParameter("CountRisultati");

			setRequestAttribute("CountRisultati", CountRisultati);
			setRequestAttribute(IWebConstants.NUM_PAGE, lPagina);
			setRequestAttribute(IWebConstants.REQUEST_FOR_PAGING, getCompleteRequestURL());

			// setta la risposta nella request
			setRequestAttribute("fascicoli", lFascicoliSoggetti);
			lReturnPage = ICostantiFascicoloSius.PG_RICERCAFASCICOLO;
		}

		return lReturnPage; // restituisce la jsp di VIEW
	}
}
