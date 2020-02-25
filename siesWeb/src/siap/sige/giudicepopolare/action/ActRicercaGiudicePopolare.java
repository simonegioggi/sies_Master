package siap.sige.giudicepopolare.action;

import java.math.BigDecimal;
import java.util.Vector;
//import java.util.Collection;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.StringUtils;
import f3b.web.IWebConstants;
//import siap.sico.decodifiche.util.DecodificheUtils;
//import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.web.ActionSiap;
import siap.sige.giudicepopolare.controller.IGiudicePopolare;
import siap.sige.giudicepopolare.model.GiudicePopolareModel;
import siap.sige.util.SIGELookupRemote;

/**
 * <p>
 * Title: ActRicercaGiudicePopolare
 * </p>
 * <p>
 * Description: Classe Action per la ricerca del Giudice Popolare
 * </p>
 * <p>
 * Copyright: Copyright (c) 2008
 * </p>
 * <p>
 * Company: Eutelia S.p.A.
 * </p>
 * 
 * @version 1.0
 */
public class ActRicercaGiudicePopolare extends ActionSiap implements ICostantiGiudicePopolare {
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	/**
	 * Esegue la ricerca del Giudice Popolare.
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

		GiudicePopolareModel lGiuPopMod = new GiudicePopolareModel();
		lGiuPopMod.setCognome(
				StringUtils.convertSqlString(getRequestStringParameter(CAMPO_COGNOME).toUpperCase()));
		lGiuPopMod.setNome(StringUtils.convertSqlString(getRequestStringParameter(CAMPO_NOME).toUpperCase()));

		// Impostazione condizioni di ricerca per la sezione.

		/*
		 * if( isRequestParameterNullObj(CAMPO_SEZ_ID_SEZIONE) && isRequestChecked("flagTutti") ) {
		 * lGiuPopMod.setSezIdSezione(null); } else if(!isRequestParameterNullObj(CAMPO_SEZ_ID_SEZIONE) &&
		 * getRequestStringParameter(CAMPO_SEZ_ID_SEZIONE).equals("")) { lGiuPopMod.setSezIdSezione(new
		 * BigDecimal("-1")); } else
		 * lGiuPopMod.setSezIdSezione(getRequestBigDecimalParameter(CAMPO_SEZ_ID_SEZIONE));
		 */

		if (!isRequestParameterNullObj(CAMPO_SEZ_ID_SEZIONE)) {
			if (getRequestStringParameter(CAMPO_SEZ_ID_SEZIONE).equals("Tutte"))
				lGiuPopMod.setSezIdSezione(null);
			else
				lGiuPopMod.setSezIdSezione(getRequestBigDecimalParameter(CAMPO_SEZ_ID_SEZIONE));
		}

		// Impostazione filtri sulla data di fine validità.
		lGiuPopMod.setDataInizioValidita(getRequestDateParameter(CAMPO_ANNO_DATA_INIZIO_VALIDITA,
				CAMPO_MESE_DATA_INIZIO_VALIDITA, CAMPO_GIORNO_DATA_INIZIO_VALIDITA));

		if (isRequestChecked("flagDataFine@Null"))
			lGiuPopMod.setMessage("dataFineisNull");

		// La ricerca è sempre filtrata per ufficio di appartenenza.
		lGiuPopMod.setCodUfficioAppartenenza(getCodUfficioUtenteConnesso());

		IGiudicePopolare lCtrl = SIGELookupRemote.getGiudicePopolareRemote();
		Vector lVect = lCtrl.ExRicercaGiudicePopolare(lGiuPopMod);

		if (lVect.size() == 1) {
			// Unico Giudice Popolare
			GiudicePopolareModel lGiudicePopolare = new GiudicePopolareModel(
					(GiudicePopolareModel) lVect.firstElement());

			// Inserisce il model Curatore nella request
			setRequestAttribute("giudicepopolare", lGiudicePopolare);
			setFunctionsAvailableToRequest(
					"siap.sige.giudicepopolare.action.ActLoadDettaglioGiudicePopolare");
			lReturnPage = PG_LOAD_DETTAGLIOGIUDICE_POPOLARE;
		} else {
			// Paginazione
			int CountRisultati;
			if (isRequestParameterNullObj("CountRisultati")) {
				CountRisultati = lCtrl.ExGetNumRicercaGiudicePopolare(lGiuPopMod);
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.warn("CountRisultati : " + CountRisultati);
			} else
				CountRisultati = getRequestIntParameter("CountRisultati");

			setRequestAttribute("CountRisultati", new BigDecimal("" + CountRisultati));
			setRequestAttribute(IWebConstants.NUM_PAGE, lPagina);
			setRequestAttribute(IWebConstants.REQUEST_FOR_PAGING, getCompleteRequestURL());

			// Lista dei Giudici Popolari
			setRequestAttribute("giudicipopolari", lVect);
			lReturnPage = PG_RICERCAGIUDICE_POPOLARE;
		}

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(getClass().getName() + ".processRequest : fine");

		return lReturnPage;
	}

}