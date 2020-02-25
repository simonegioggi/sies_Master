package siap.sige.collegio.action;

import org.apache.log4j.Logger;

import siap.sige.collegio.util.CollegioUtils;
import siap.sige.sezione.util.SezioneUtils;
import f3b.log.LogF3B;
import f3b.web.IWebConstants;
import f3b.web.html.Option;

/**
 * <p>
 * Title: ActLoadInserisciCollegio
 * </p>
 * <p>
 * Description: Classe Action per la load inserisci del Collegio
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
public class ActLoadInserisciCollegio extends ActionCollegio implements ICostantiCollegio {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	/**
	 * Carica la form per l'inserimento del Collegio.
	 * <p>
	 * 
	 * @return Nome della pagina JSP da visualizzare al termine dell'elaborazione.
	 *         <p>
	 * @throws Exception
	 *             propaga errore di eccezione.
	 */
	public String processRequest() throws Exception {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(getClass().getName() + ".processRequest : inizio");

		// 20171124: FUNZIONE DISABILITATA
		if (isRequestParameterNullObj("from")) {
			siesLogger.debug("VIETATO INSERIMENTO COLLEGIO DA FUNZIONI AMMINISTRATIVE!");
			setRequestAttribute(IWebConstants.MESSAGE_TEXT,
					"Attenzione! L'inserimento del collegio va fatto dalla funzione 'Inserimento Fissazione Udienza'.");
			return IWebConstants.PG_MESSAGE;
		} else {
			if ("jsp".equals(getRequestStringParameter("from")))
				siesLogger.debug("INSERIMENTO COLLEGIO DA PAGINE INTERNE ALL'APPLICAZIONE!");
		}

		// valore di ritorno
		String ret = "";

		gestioneRitorno();
		Option lOption = null;

		lOption = new Option(SezioneUtils.getElencoSezioni(getCodUfficioUtenteConnesso()), Option.BLANK_ITEM);
		setRequestAttribute("elencoSezioni", "" + lOption);

		lOption = new Option(CollegioUtils.getElencoCodiciCollegi(), Option.BLANK_ITEM);
		setRequestAttribute("elencoCodiciCollegi", "" + lOption);

		ret = getInsViewJSP();
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(getClass().getName() + ".getViewJSP() : " + ret);

		// Imposta Modalità Inserimento.
		setRequestAttribute("modalita", "I");

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(getClass().getName() + ".processRequest : fine");

		return ret; // Restituisce la jsp di VIEW Form Inserimento.
	}

	// protected void caricaMagistrati() throws Exception {
	// BigDecimal lIdSez = super.getRequestBigDecimalParameter(CAMPO_SEZ_ID_SEZIONE);
	// IMagistrato lCtrl = SIGELookupRemote.getMagistratoRemote();
	// Collection lColl = lCtrl.ExElencoCbxMagistratiByIdSezione(lIdSez);
	// Option lOption = new Option(lColl, Option.BLANK_ITEM);
	// setRequestAttribute("elencoMagistrati", "" + lOption);
	// }

}