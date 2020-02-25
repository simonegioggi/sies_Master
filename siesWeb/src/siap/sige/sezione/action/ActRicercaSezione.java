package siap.sige.sezione.action;

import java.math.BigDecimal;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.StringUtils;
import f3b.web.IWebConstants;
import siap.sico.web.ActionSiap;
import siap.sige.sezione.controller.ISezione;
import siap.sige.sezione.model.SezioneModel;
import siap.sige.util.SIGELookupRemote;

/**
 * <p>
 * Title: ActRicercaSezione
 * </p>
 * <p>
 * Description: Classe Action per la ricerca del Sezione
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
public class ActRicercaSezione extends ActionSiap implements ICostantiSezione {
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	/**
	 * Esegue la ricerca del Sezione.
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

		SezioneModel lSezMod = new SezioneModel();

		lSezMod.setCodice(
				StringUtils.convertSqlString(getRequestStringParameter(CAMPO_CODICE).toUpperCase()));
		lSezMod.setDescrizione(
				StringUtils.convertSqlString(getRequestStringParameter(CAMPO_DESCRIZIONE).toUpperCase()));

		// La ricerca è sempre filtrata per ufficio
		lSezMod.setCodUfficioAppartenenza(getCodUfficioUtenteConnesso());

		ISezione lCtrl = SIGELookupRemote.getSezioneRemote();
		Vector lVect = lCtrl.ExRicercaSezione(lSezMod);

		if (lVect.size() == 1) { // unica Sezione
			SezioneModel lSezione = new SezioneModel((SezioneModel) lVect.firstElement());

			// Inserisce il model Sezione nella request
			setRequestAttribute("sezione", lSezione);
			setFunctionsAvailableToRequest("siap.sige.sezione.action.ActLoadDettaglioSezione");
			lReturnPage = PG_LOAD_DETTAGLIOSEZIONE;
		} else {
			// Paginazione
			int CountRisultati;
			if (isRequestParameterNullObj("CountRisultati")) {
				CountRisultati = lCtrl.ExGetNumRicercaSezione(lSezMod);
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.warn("CountRisultati : " + CountRisultati);
			} else
				CountRisultati = getRequestIntParameter("CountRisultati");

			setRequestAttribute("CountRisultati", new BigDecimal("" + CountRisultati));
			setRequestAttribute(IWebConstants.NUM_PAGE, lPagina);
			setRequestAttribute(IWebConstants.REQUEST_FOR_PAGING, getCompleteRequestURL());

			// lista delle sezioni
			setRequestAttribute("sezioni", lVect);
			lReturnPage = PG_RICERCASEZIONE;
		}

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(getClass().getName() + ".processRequest : fine");

		return lReturnPage;
	}

}