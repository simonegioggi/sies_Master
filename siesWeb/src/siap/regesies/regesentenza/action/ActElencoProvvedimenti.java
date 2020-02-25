package siap.regesies.regesentenza.action;

import java.math.BigDecimal;
import java.util.Vector;

import org.apache.log4j.Logger;

import siap.regesies.action.ActionRegeSiap;
import siap.regesies.regesentenza.controller.IRegeSentenza;
import siap.regesies.util.RegeSiesLookupRemote;
import siap.web.ISIAPCostantiWeb;
import f3b.log.LogF3B;
import f3b.util.F3BException;
import f3b.web.IWebConstants;

/**
 * <p>
 * Title: ActElencoProvvedimenti
 * </p>
 * <p>
 * Description: Elenco Provvedimenti provenienti da Rege
 * </p>
 * <p>
 * Copyright: Copyright (c) 2006
 * </p>
 * <p>
 * Company: Bull Italia S.p.A.
 * </p>
 */
@SuppressWarnings("rawtypes")
public class ActElencoProvvedimenti extends ActionRegeSiap implements ICostantiRegeSentenza {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public String processRequest() throws F3BException {

		String lPagina = "1";

		if (!isRequestParameterNullObj(IWebConstants.NUM_PAGE))
			lPagina = getRequestStringParameter(IWebConstants.NUM_PAGE);

		String lComune = this.getCodComuneUtenteConnesso();

		if (this.isUfficioSecondoGrado()) { // NOn deve filtrare per comene se l'ufficio è di secondo grado
			lComune = "000000";
		}

		IRegeSentenza lCtrl = RegeSiesLookupRemote.getRegeSentenzaRemote();
		Vector lSentenze = lCtrl.ExRicercaElencoProvvedimenti(Integer.parseInt(lPagina), lComune);

		int CountRisultati;
		// Eseguo la query per reperire quanti elementi sono oppure lo recupero dalla request
		if (isRequestParameterNullObj("CountRisultati")) {
			CountRisultati = lCtrl.ExGetCountProvvedimenti(lComune);
		} else
			CountRisultati = getRequestIntParameter("CountRisultati");

		setRequestAttribute("CountRisultati", new BigDecimal(CountRisultati));

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.info("CountRisultati = " + CountRisultati);

		setRequestAttribute(IWebConstants.NUM_PAGE, lPagina);
		setRequestAttribute(IWebConstants.REQUEST_FOR_PAGING, getCompleteRequestURL());

		setRequestAttribute("sentenze", lSentenze);

		String lAzione = "siap.regesies.regesentenza.action.ActElencoProvvedimenti";
		setRequestAttribute(ISIAPCostantiWeb.CAMPO_AZIONE_CHIAMANTE, lAzione);

		setRequestAttribute("sentenze", lSentenze);

		return PG_ELENCO_PROVVEDIMENTI; // restituisce la jsp di VIEW
	}

}