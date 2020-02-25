package siap.sige.aula.action;

import java.math.BigDecimal;

import org.apache.log4j.Logger;

import java.util.Vector;

import siap.sico.web.ActionSiap;
import siap.sige.aula.controller.IAula;
import siap.sige.aula.model.AulaUdienzaModel;
import siap.sige.util.SIGELookupRemote;
import f3b.log.LogF3B;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;

/**
 * <p>
 * Title: ActRicercaCollegio
 * </p>
 * <p>
 * Description: Classe Action per la ricerca del Collegio
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
@SuppressWarnings("rawtypes")
public class ActRicercaAula extends ActionSiap implements ICostantiAula {
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	private String popUpRequest() throws Exception {
		String ret = POPUP_RICERCA_AULA;

		AulaUdienzaModel lAulaMod = new AulaUdienzaModel();
		if (!isRequestParameterNullObj(CAMPO_ID_SEZIONE)
				&& !getRequestStringParameter(CAMPO_ID_SEZIONE).equals(""))
			lAulaMod.setIdSezione(getRequestBigDecimalParameter(CAMPO_ID_SEZIONE));

		// La ricerca è sempre filtrata per ufficio
		lAulaMod.setCodUfficioInserimento(getCodUfficioUtenteConnesso());

		IAula lCtrl = SIGELookupRemote.getAulaRemote();
		Vector lVect = lCtrl.ExRicercaAula(lAulaMod);

		setRequestAttribute("aule", lVect);

		return ret;
	}

	private String standardRequest() throws Exception {
		super.setLinkRitorno();

		String lPagina = "1";

		if (!isRequestParameterNullObj(IWebConstants.NUM_PAGE))
			lPagina = getRequestStringParameter(IWebConstants.NUM_PAGE);

		String lReturnPage = ""; // pagina jsp di ritorno

		AulaUdienzaModel lAulaMod = new AulaUdienzaModel();

		if (!isRequestParameterNullObj(CAMPO_ID_SEZIONE)
				&& !getRequestStringParameter(CAMPO_ID_SEZIONE).equals(""))
			lAulaMod.setIdSezione(getRequestBigDecimalParameter(CAMPO_ID_SEZIONE));

		if (!isRequestParameterNullObj(CAMPO_DESCRIZIONE_AULA)) {
			lAulaMod.setDescrizioneAula(getRequestStringParameter(CAMPO_DESCRIZIONE_AULA));
		}

		if (!isRequestParameterNullObj(CAMPO_DESCRIZIONE_STANZA)) {
			lAulaMod.setDescrizioneStanza(getRequestStringParameter(CAMPO_DESCRIZIONE_STANZA));
		}

		if (!isRequestParameterNullObj(CAMPO_DESCRIZIONE_INGRESSO)) {
			lAulaMod.setDescrizioneIngresso(getRequestStringParameter(CAMPO_DESCRIZIONE_INGRESSO));
		}

		if (!isRequestParameterNullObj(CAMPO_DESCRIZIONE_INGRESSO)) {
			// 20170908: è un varchar nel db
			lAulaMod.setNumeroPiano(getRequestStringParameter(CAMPO_NUMERO_PIANO));
			// lAulaMod.setNumeroPiano(getRequestBigDecimalParameter(CAMPO_NUMERO_PIANO));
		}

		// La ricerca è sempre filtrata per ufficio
		lAulaMod.setCodUfficioInserimento(getCodUfficioUtenteConnesso());

		IAula lCtrl = SIGELookupRemote.getAulaRemote();
		Vector lVect = lCtrl.ExRicercaAula(lAulaMod);

		if (lVect.size() == 1) { // Unica Aula.
			AulaUdienzaModel lAula = new AulaUdienzaModel((AulaUdienzaModel) lVect.firstElement());

			setFunctionsAvailableToRequest("siap.sige.aula.action.ActDettaglioAula");

			RedirectTo lRedirect = new RedirectTo();
			lRedirect.setPage(IWebConstants.PG_MAIN);
			lRedirect.setAction("siap.sige.aula.action.ActDettaglioAula");
			lRedirect.setParameter(ICostantiAula.CAMPO_ID_AULA, lAula.getIdAula().toString());
			lRedirect.setParameter(ICostantiAula.CAMPO_ID_SEZIONE, lAula.getIdSezione().toString());

			lReturnPage = lRedirect.toString();
		} else {
			// Paginazione
			int CountRisultati;
			if (isRequestParameterNullObj("CountRisultati")) {
				CountRisultati = lCtrl.ExGetNumRicercaAula(lAulaMod);
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.warn("CountRisultati : " + CountRisultati);
			} else
				CountRisultati = getRequestIntParameter("CountRisultati");

			setRequestAttribute("CountRisultati", new BigDecimal("" + CountRisultati));
			setRequestAttribute(IWebConstants.NUM_PAGE, lPagina);
			setRequestAttribute(IWebConstants.REQUEST_FOR_PAGING, getCompleteRequestURL());

			setRequestAttribute("aule", lVect);

			lReturnPage = PG_RICERCA_AULA;
		}

		return lReturnPage;
	}

	/**
	* Esegue la ricerca dell'Aula.
	* <p>
	 * 
	 * @return Nome della pagina JSP da visualizzare al termine dell'elaborazione
	* <p>
	* @throws Exception
	*/
	public String processRequest() throws Exception {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(getClass().getName() + ".processRequest : inizio");
		String ret = "";

		if (!isRequestParameterNullObj(IWebConstants.POPUP_PAGE)) {
			ret = popUpRequest();
		} else {
			ret = standardRequest();
		}

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(getClass().getName() + ".processRequest : fine");
		return ret;
	}

}