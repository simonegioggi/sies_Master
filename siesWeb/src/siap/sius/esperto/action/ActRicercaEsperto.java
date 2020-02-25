package siap.sius.esperto.action;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.StringUtils;
import f3b.web.IWebConstants;
import siap.sico.decodifiche.controller.DecodificheManager;
// per la decodifica
import siap.sico.decodifiche.util.DecodificheUtils;
import siap.sico.web.ActionSiap;
import siap.sius.esperto.controller.IEsperto;
import siap.sius.esperto.model.EspertoModel;
import siap.sius.util.SIUSLookupRemote;

/**
 * <p>
 * Title: ActRicercaEsperto
 * </p>
 * <p>
 * Description: Classe Action per la ricerca di Esperto
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
public class ActRicercaEsperto extends ActionSiap implements ICostantiEsperto {
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	/**
	 * Esegue la ricerca dell'Esperto.
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

		EspertoModel lEspMod = new EspertoModel();

		// Collection di decodifica di Flag_stato
		Collection lCol = (DecodificheManager.getInstance()).getFlagStato();

		lEspMod.setCognome(
				StringUtils.convertSqlString(getRequestStringParameter(CAMPO_COGNOME).toUpperCase()));
		lEspMod.setNome(StringUtils.convertSqlString(getRequestStringParameter(CAMPO_NOME).toUpperCase()));

		// La ricerca è sempre filtrata per ufficio
		lEspMod.setCodUfficioAppartenenza(getCodUfficioUtenteConnesso());

		IEsperto lCtrl = SIUSLookupRemote.getEspertoRemote();
		Vector lVect = lCtrl.ExRicercaEsperto(lEspMod);

		if (lVect.size() == 1) { // unico Esperto
			EspertoModel lEsperto = new EspertoModel((EspertoModel) lVect.firstElement());

			// Decodifica di Flag_stato
			lEsperto.setFlagStato(DecodificheUtils.getDescbyCode(lCol, lEsperto.getFlagStato()));

			// Inserisce il model Esperto nella request
			setRequestAttribute("esperto", lEsperto);
			setFunctionsAvailableToRequest("siap.sius.esperto.action.ActLoadDettaglioEsperto");
			lReturnPage = PG_LOAD_DETTAGLIOESPERTO;
		} else {
			// Paginazione
			int CountRisultati;
			if (isRequestParameterNullObj("CountRisultati")) {
				CountRisultati = lCtrl.ExGetNumRicercaEsperto(lEspMod);
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.warn("CountRisultati : " + CountRisultati);
			} else
				CountRisultati = getRequestIntParameter("CountRisultati");

			setRequestAttribute("CountRisultati", new BigDecimal("" + CountRisultati));
			setRequestAttribute(IWebConstants.NUM_PAGE, lPagina);
			setRequestAttribute(IWebConstants.REQUEST_FOR_PAGING, getCompleteRequestURL());

			// lista di Esperti
			setRequestAttribute("esperti", lVect);
			setRequestAttribute("flags", lCol);
			lReturnPage = PG_RICERCAESPERTO;
		}

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(getClass().getName() + ".processRequest : fine");

		return lReturnPage;
	}

}