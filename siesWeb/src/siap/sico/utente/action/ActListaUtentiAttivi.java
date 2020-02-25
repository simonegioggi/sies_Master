package siap.sico.utente.action;

import java.math.BigDecimal;
import java.util.Vector;

import org.apache.log4j.Logger;

import siap.sico.utente.controller.IUtente;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import f3b.log.LogF3B;
import f3b.web.IWebConstants;

/**
 * <p>
 * Title: ActListaUtenti
 * </p>
 * <p>
 * Description: Classe Action per il caricamento della lista Utenti Attivi e Utenti non attivi
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
@SuppressWarnings("unchecked")
public class ActListaUtentiAttivi extends ActionSiap implements ICostantiUtente {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	/**
	 * Carica la lista Paginata degli Utenti Attivi o Non Attivi. Se presente il parametro "tip" sulla request
	 * ricerca gli utenti non attivi, altrimenti quelli attivi.
	 *
	 * Se l'utente connesso ha profilo 99 (Gestore Utenti e Profili) filtra gli utenti di tutti gli uffici del
	 * distretto.
	 * 
	 * Se l'utente connesso ha profilo 90 (Amministratore di Ufficio) filtra gli utenti dell'ufficio
	 * dell'amministratore.
	 */
	public String processRequest() throws Exception {

		setLinkRitorno();

		Vector<Object> lVect = new Vector<Object>();
		String lPagina = "1";

		IUtente lCtrl = SICOLookupRemote.getUtenteRemote();
		BigDecimal CountRisultati;

		if (!isRequestParameterNullObj(IWebConstants.NUM_PAGE))
			lPagina = getRequestStringParameter(IWebConstants.NUM_PAGE);

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("Lista Utenti Attivi");
		if (isRequestParameterNullObj("CountRisultati")) {
			// Verifica se l'utente di sessione è un System Admin profilo 99
			if (getUtenteConnesso().isSysAdmin())
				CountRisultati = lCtrl.ExGetCountUtentiAttivi(getCodDistrettoUtenteConnesso());
			else
				CountRisultati = lCtrl.ExGetCountUtentiAttiviPerUfficio(getCodUfficioUtenteConnesso());
		} else {
			CountRisultati = getRequestBigDecimalParameter("CountRisultati");
		}

		// Verifica se l'utente di sessione è un System Admin profilo 99
		if (getUtenteConnesso().isSysAdmin())
			lVect = lCtrl.ExListaUtentiAttiviFromView(Integer.parseInt(lPagina),
					getCodDistrettoUtenteConnesso());
		else
			lVect = lCtrl.ExListaUtentiAttiviFromViewPerUfficio(Integer.parseInt(lPagina),
					getCodUfficioUtenteConnesso());

		setRequestAttribute("tip", "attivi");

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(CountRisultati);
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(lPagina);

		setRequestAttribute("CountRisultati", CountRisultati);
		setRequestAttribute(IWebConstants.NUM_PAGE, lPagina);
		setRequestAttribute(IWebConstants.REQUEST_FOR_PAGING, getCompleteRequestURL());
		setRequestAttribute("utenti", lVect);

		return PG_LISTAUTENTI;
	}

}