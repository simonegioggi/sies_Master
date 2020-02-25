package siap.regesies.regescarti.action;

import java.math.BigDecimal;
import java.util.Vector;

import siap.regesies.regescarti.controller.RegeFileController;
import siap.regesies.regescarti.model.RegeFileModel;
import siap.sico.web.ActionSiap;
import siap.web.ISIAPCostantiWeb;
import f3b.util.F3BException;
import f3b.web.IWebConstants;

/**
 * <p>
 * Title: ActRicercaRegeFile
 * </p>
 * <p>
 * Description: Classe Action per la ricerca dei file scartati REGE
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
@SuppressWarnings("rawtypes")
public class ActRicercaRegeFile extends ActionSiap implements ICostantiRegeFile {

	public String processRequest() throws F3BException {

		// istanzia il Model
		RegeFileModel lRegMod = new RegeFileModel();

		// Passa come chiave di ricerca COD_STATO = 3
		lRegMod.setCodStato(new BigDecimal(3));
		// Passa come chiave di ricerca il Codice Ufficio dell'Utente Connesso
		lRegMod.setCodComune(this.getCodComuneUtenteConnesso());

		// Setta la prima pagina e le seguenti se trovo NUM_PAGE
		String lPagina = "1";
		if (!isRequestParameterNullObj(IWebConstants.NUM_PAGE))
			lPagina = getRequestStringParameter(IWebConstants.NUM_PAGE);

		// chiama il controller
		// IRegeFile lCtrl = SIEPLookupRemote.getRegeFileRemote();
		RegeFileController lCtrl = new RegeFileController();

		Vector lVect = null;

		try {
			// Instanzia il vettore
			lVect = lCtrl.ExRicercaRegeFilePage(lRegMod, Integer.parseInt(lPagina));
		} catch (F3BException f3be) {

			if (f3be.getErrorCode() == F3BException.USER_MESSAGE) {

				setRequestAttribute(IWebConstants.GOTO_PAGE, ISIAPCostantiWeb.PG_PAGINA_VUOTA);
				setRequestAttribute(IWebConstants.MESSAGE_TEXT, "" + f3be.getMessage());

				return IWebConstants.PG_MESSAGE; // restituisce la jsp di VIEW
			} else {

				throw new F3BException(f3be.getMessage());
			}
		}

		// setta la risposta nella request
		setRequestAttribute("elencoregefile", lVect);

		// Trova il totale dei record
		BigDecimal CountRisultati;
		if (isRequestParameterNullObj("CountRisultati")) {
			CountRisultati = lCtrl.ExgetCountFileRege(lRegMod);
		} else
			CountRisultati = getRequestBigDecimalParameter("CountRisultati");

		// Passa alla request il numero di record trovati e i dati della paginazione
		setRequestAttribute("CountRisultati", CountRisultati);
		setRequestAttribute(IWebConstants.NUM_PAGE, lPagina);
		setRequestAttribute(IWebConstants.REQUEST_FOR_PAGING, getCompleteRequestURL());

		// Apre la pagina con l'elenco dei file REGE Scartati
		return PG_RICERCAREGEFILE;
	}

}