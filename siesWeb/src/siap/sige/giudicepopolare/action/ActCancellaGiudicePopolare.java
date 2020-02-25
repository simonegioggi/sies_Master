package siap.sige.giudicepopolare.action;

import org.apache.log4j.Logger;

import siap.sico.web.ActionSiap;
import siap.sige.giudicepopolare.controller.IGiudicePopolare;
import siap.sige.util.SIGELookupRemote;
import f3b.log.LogF3B;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;

/**
 * <p>
 * Title: ActCancellaGiudicePopolare
 * </p>
 * <p>
 * Description: Classe Action per la cancellazione di un Giudice Popolare
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: Eutelia S.p.A.
 * </p>
 * 
 * @version 1.0
 */
public class ActCancellaGiudicePopolare extends ActionSiap implements ICostantiGiudicePopolare {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public String processRequest() throws Exception {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(getClass().getName() + ".processRequest : inizio");

		// String lIdGiudicePopolare = getRequestStringParameter(CAMPO_ID_GIUDICE_POPOLARE);
		// if (lIdGiudicePopolare.equals("0"))
		// throw new F3BException(F3BException.USER_MESSAGE,"Non è consentita la cancellazione di questo
		// record: Curatore di default.");

		// Chiama il controller.
		IGiudicePopolare lCtrl = SIGELookupRemote.getGiudicePopolareRemote();
		lCtrl.ExCancellaGiudicePopolare(getRequestBigDecimalParameter(CAMPO_ID_GIUDICE_POPOLARE));

		// Setta la risposta nella request.
		setRequestAttribute(IWebConstants.MESSAGE_TEXT, "Cancellazione Avvenuta Correttamente!");

		// Prepara la "pagina" di destinAction.
		RedirectTo lRedirigi = new RedirectTo();
		lRedirigi.setPage(IWebConstants.PG_MAIN);
		lRedirigi.setAction("siap.sige.giudicepopolare.action.ActLoadRicercaGiudicePopolare");
		setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi);

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(getClass().getName() + ".processRequest : fine");

		return IWebConstants.PG_MESSAGE; // restituisce la jsp di VIEW }
	}

}