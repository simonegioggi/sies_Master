package siap.sige.curatore.action;

import java.math.BigDecimal;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.StringUtils;
import f3b.web.IWebConstants;
import siap.sico.web.ActionSiap;
import siap.sige.curatore.controller.ICuratore;
import siap.sige.curatore.model.CuratoreModel;
import siap.sige.util.SIGELookupRemote;

/**
 * <p>
 * Title: ActRicercaCuratore
 * </p>
 * <p>
 * Description: Classe Action per la ricerca del Curatore
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
public class ActRicercaCuratore extends ActionSiap implements ICostantiCuratore {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	/**
	 * Esegue la ricerca del Curatore.
	 * <p>
	 * 
	 * @return Nome della pagina JSP da visualizzare al termine dell'elaborazione
	 *         <p>
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public String processRequest() throws Exception {
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(getClass().getName() + ".processRequest : inizio");

		super.setLinkRitorno();

		String lPagina = "1";
		if (!isRequestParameterNullObj(IWebConstants.NUM_PAGE))
			lPagina = getRequestStringParameter(IWebConstants.NUM_PAGE);

		String lReturnPage = ""; // pagina jsp di ritorno

		CuratoreModel lCurMod = new CuratoreModel();

		lCurMod.setCognome(
				StringUtils.convertSqlString(getRequestStringParameter(CAMPO_COGNOME).toUpperCase()));
		lCurMod.setNome(StringUtils.convertSqlString(getRequestStringParameter(CAMPO_NOME).toUpperCase()));

		// La ricerca è sempre filtrata per ufficio
		lCurMod.setCodUfficioAppartenenza(getCodUfficioUtenteConnesso());

		ICuratore lCtrl = SIGELookupRemote.getCuratoreRemote();
		Vector lVect = lCtrl.ExRicercaCuratore(lCurMod);

		if (lVect.size() == 1) { // unico Esperto
			CuratoreModel lCuratore = new CuratoreModel((CuratoreModel) lVect.firstElement());

			// Inserisce il model Curatore nella request
			setRequestAttribute("curatore", lCuratore);
			setFunctionsAvailableToRequest("siap.sige.curatore.action.ActLoadDettaglioCuratore");
			lReturnPage = PG_LOAD_DETTAGLIOCURATORE;
		} else {
			// Paginazione
			int CountRisultati;
			if (isRequestParameterNullObj("CountRisultati")) {
				CountRisultati = lCtrl.ExGetNumRicercaCuratore(lCurMod);
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.warn("CountRisultati : " + CountRisultati);
			} else
				CountRisultati = getRequestIntParameter("CountRisultati");

			setRequestAttribute("CountRisultati", new BigDecimal("" + CountRisultati));
			setRequestAttribute(IWebConstants.NUM_PAGE, lPagina);
			setRequestAttribute(IWebConstants.REQUEST_FOR_PAGING, getCompleteRequestURL());

			// lista dei Curatori
			setRequestAttribute("curatori", lVect);
			lReturnPage = PG_RICERCACURATORE;
		}

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(getClass().getName() + ".processRequest : fine");

		return lReturnPage;
	}

}