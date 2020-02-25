package siap.siep.stampadocumenti.action;

/**
* <p>Title: ActRicercaStampaDocumenti</p>
* <p>Description: Classe Action per la ricerca di StampaDocumenti</p>
* <p>Copyright: Copyright (c) 2007</p>
* <p>Company: Eunics</p>
* @version 1.0
*/

import java.util.Vector;

import f3b.util.F3BException;
import f3b.web.IWebConstants;
//import siap.siep.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siep.stampadocumenti.controller.StampaDocumentiController;
import siap.siep.stampadocumenti.model.StampaDocumentiModel;

public class ActRicercaStampaDocumenti extends ActionSiap implements ICostantiStampaDocumenti {
	/*****************************************************************************
	 * Azione di Ricerca. Recupera i dati dalla form ed effettua la ricerca.
	 * 
	 * @return Nome della pagina JSP da visualizzare
	 * @throws F3BException
	 *****************************************************************************/
	@SuppressWarnings("rawtypes")
	public String processRequest() throws F3BException {
		// ==============================================================
		// Recupero dalla form i campi di ricerca
		// n.b. cancellare o commentare i campi non presenti nella form
		// ==============================================================
		StampaDocumentiModel lStaMod = new StampaDocumentiModel();

		lStaMod.setIdUtente(this.getCodUtenteConnesso());

		// =================================
		// Selezionare il tipo di ricerca
		// =================================
		String lReturnPage = null;

		String lPagina = "1";
		String CountRisultati;

		// ============================================================================
		// Recupero la pagina da visualizzare se prevengo dalla finestra dei risultati
		// ============================================================================
		if (!isRequestParameterNullObj(IWebConstants.NUM_PAGE))
			lPagina = getRequestStringParameter(IWebConstants.NUM_PAGE);

		// =========================================================
		// Istanzio il controller ed effettuo la ricerca paginata
		// =========================================================
		StampaDocumentiController lCtrl = new StampaDocumentiController();
		Vector lVect = lCtrl.ExRicercaStampaDocumentiPaged(lStaMod, Integer.parseInt(lPagina));

		if (lVect.size() == 0) {
			this.setRequestAttribute(IWebConstants.MESSAGE_TEXT, "Nessun dato presente");
			return IWebConstants.PG_MESSAGE;
		}

		// ======================================================================
		// Recupero il numero di record totali della ricerca utilizzato per
		// calcolare il numero totale di pagine necessarie a visualizzare i dati
		// ======================================================================
		if (isRequestParameterNullObj("CountRisultati"))
			CountRisultati = lCtrl.ExGetCountStampaDocumenti(lStaMod).toString();
		else
			CountRisultati = getRequestStringParameter("CountRisultati");

		/*
		 * if (Integer.parseInt(CountRisultati)==1) { // Nel caso di un solo record visualizzo direttamente il
		 * dettaglio... lStaMod = new StampaDocumentiModel ((StampaDocumentiModel)lVect.firstElement());
		 * setRequestAttribute("stampadocumenti",lStaMod);
		 * setFunctionsAvailableToRequest("siap.siep.stampadocumenti.action.ActLoadDettaglioStampaDocumenti");
		 * 
		 * lReturnPage = PG_LOAD_DETTAGLIOSTAMPADOCUMENTI; } else {
		 */
		// ...altrimenti la pagina con i risultati
		setRequestAttribute("CountRisultati", CountRisultati);
		setRequestAttribute(IWebConstants.NUM_PAGE, lPagina);
		setRequestAttribute(IWebConstants.REQUEST_FOR_PAGING, getCompleteRequestURL());

		setRequestAttribute("stampadocumenti", lVect);

		lReturnPage = PG_RICERCASTAMPADOCUMENTI;
		// }

		return lReturnPage;
	}

}