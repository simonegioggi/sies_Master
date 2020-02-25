package siap.sius.cancelleriaassegnataria.action;

import java.math.BigDecimal;
import java.util.Vector;

import f3b.web.IWebConstants;
import f3b.web.RedirectTo;
import siap.sico.web.ActionSiap;
import siap.sius.cancelleriaassegnataria.controller.ICancelleriaAssegnataria;
import siap.sius.cancelleriaassegnataria.model.CancelleriaAssegnatariaModel;
import siap.sius.util.SIUSLookupRemote;

/**
 * <p>
 * Title: ActRicercaCancelleriaAssegnataria
 * </p>
 * <p>
 * Description: Classe Action per la ricerca di CancelleriaAssegnataria
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

public class ActRicercaCancelleriaAssegnataria extends ActionSiap
		implements ICostantiCancelleriaAssegnataria {

	@SuppressWarnings("rawtypes")
	public String processRequest() throws Exception {

		String lRetPage = PG_RICERCACANCELLERIAASSEGNATARIA;

		// Gestione numero di pagina
		String lPagina = "1";
		if (!isRequestParameterNullObj(IWebConstants.NUM_PAGE))
			lPagina = getRequestStringParameter(IWebConstants.NUM_PAGE);

		// Riempie il model di ricerca
		CancelleriaAssegnatariaModel lCanMod = new CancelleriaAssegnatariaModel();
		lCanMod.setCodCancelleriaAssegnataria(getRequestStringParameter(CAMPO_COD_CANCELLERIA_ASSEGNATARIA));
		lCanMod.setCodUfficio(getRequestStringParameter(CAMPO_COD_UFFICIO));
		lCanMod.setDescCancelleriaAssegnataria(
				getRequestStringParameter(CAMPO_DESC_CANCELLERIA_ASSEGNATARIA));

		// Ricerca
		ICancelleriaAssegnataria lCtrl = SIUSLookupRemote.getCancelleriaAssegnatariaRemote();
		Vector lVect = lCtrl.ExRicercaCancelleriaAssegnatariaPagina(lCanMod, Integer.parseInt(lPagina));

		// Paginazione
		BigDecimal CountRisultati;
		if (isRequestParameterNullObj("CountRisultati")) {
			CountRisultati = lCtrl.ExGetNumRicercaCancelleriaAssegnatariaPagina(lCanMod);
		} else
			CountRisultati = getRequestBigDecimalParameter("CountRisultati");

		if (lVect.size() == 1 && CountRisultati.compareTo(new BigDecimal(1)) == 0) {
			lCanMod = (CancelleriaAssegnatariaModel) lVect.get(0);

			// Si passa al dettaglio
			RedirectTo lPage = new RedirectTo();
			lPage.setPage(IWebConstants.PG_MAIN);
			lPage.setAction(
					"siap.sius.cancelleriaassegnataria.action.ActLoadDettaglioCancelleriaAssegnataria");
			lPage.setParameter(CAMPO_COD_CANCELLERIA_ASSEGNATARIA, lCanMod.getCodCancelleriaAssegnataria());
			lPage.setParameter(CAMPO_COD_UFFICIO, lCanMod.getCodUfficio());
			lRetPage = lPage.toString();
		} else {
			// Bottone di ritorno
			setLinkRitorno();
			// Parametri paginazione
			setRequestAttribute("CountRisultati", CountRisultati);
			setRequestAttribute(IWebConstants.NUM_PAGE, lPagina);
			setRequestAttribute(IWebConstants.REQUEST_FOR_PAGING, getCompleteRequestURL());

			setRequestAttribute("elenco", lVect);
		}

		return lRetPage;
	}

}