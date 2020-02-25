package siap.sico.soggettocertificato.action;

/**
* <p>Title: ActRicercaSoggettoCertificato</p>
* <p>Description: Classe Action per la ricerca di SoggettoCertificato</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.util.Vector;

import siap.sico.soggettocertificato.controller.ISoggettoCertificato;
import siap.sico.soggettocertificato.model.SoggettoCertificatoModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import f3b.util.F3BException;
import f3b.web.IWebConstants;

@SuppressWarnings("rawtypes")
public class ActRicercaSoggettoCertificato extends ActionSiap implements ICostantiSoggettoCertificato {

	/*****************************************************************************
	 * Azione di Ricerca. Recupera i dati dalla form ed effettua la ricerca.
	 * 
	 * @return Nome della pagina JSP da visualizzare
	 * @throws F3BException
	 *****************************************************************************/
	public String processRequest() throws F3BException {

		// ==============================================================
		// Recupero dalla form i campi di ricerca
		// n.b. cancellare o commentare i campi non presenti nella form
		// ==============================================================
		SoggettoCertificatoModel lSogMod = new SoggettoCertificatoModel();

		lSogMod.setIdSoggettoCertificato(getRequestBigDecimalParameter(CAMPO_ID_SOGGETTO_CERTIFICATO));
		lSogMod.setSogIdSoggetto(getRequestBigDecimalParameter(CAMPO_SOG_ID_SOGGETTO));
		lSogMod.setDataInserimento(getRequestDateParameter(CAMPO_ANNO_DATA_INSERIMENTO,
				CAMPO_MESE_DATA_INSERIMENTO, CAMPO_GIORNO_DATA_INSERIMENTO));
		lSogMod.setCodOperatoreInserimento(getRequestStringParameter(CAMPO_COD_OPERATORE_INSERIMENTO));
		lSogMod.setCodUfficioInserimento(getRequestStringParameter(CAMPO_COD_UFFICIO_INSERIMENTO));
		lSogMod.setDataAggiornamento(getRequestDateParameter(CAMPO_ANNO_DATA_AGGIORNAMENTO,
				CAMPO_MESE_DATA_AGGIORNAMENTO, CAMPO_GIORNO_DATA_AGGIORNAMENTO));
		lSogMod.setCodOperatoreAggiornamento(getRequestStringParameter(CAMPO_COD_OPERATORE_AGGIORNAMENTO));
		lSogMod.setCodUfficioAggiornamento(getRequestStringParameter(CAMPO_COD_UFFICIO_AGGIORNAMENTO));

		// =================================
		// Selezionare il tipo di ricerca
		// =================================
		String tipo_ricerca = "semplice";
		// String tipo_ricerca = "paginata";

		String lReturnPage = null;

		if (tipo_ricerca.equals("semplice")) {
			// =========================================================
			// Istanzio il controller ed effettuo la ricerca semplice
			// =========================================================
			ISoggettoCertificato lCtrl = SICOLookupRemote.getSoggettoCertificatoRemote();
			Vector lVect = lCtrl.ExRicercaSoggettoCertificato(lSogMod);

			if (lVect.size() == 0) {
				this.setRequestAttribute(IWebConstants.MESSAGE_TEXT, "Nessun dato presente");
				return IWebConstants.PG_MESSAGE;
			}

			setRequestAttribute("soggettocertificato", lVect);
			setRequestAttribute("tipo_ricerca", tipo_ricerca);

			lReturnPage = PG_RICERCASOGGETTOCERTIFICATO;
		} else {
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
			ISoggettoCertificato lCtrl = SICOLookupRemote.getSoggettoCertificatoRemote();
			Vector lVect = lCtrl.ExRicercaSoggettoCertificatoPaged(lSogMod, Integer.parseInt(lPagina));

			if (lVect.size() == 0) {
				this.setRequestAttribute(IWebConstants.MESSAGE_TEXT, "Nessun dato presente");
				return IWebConstants.PG_MESSAGE;
			}

			// ======================================================================
			// Recupero il numero di record totali della ricerca utilizzato per
			// calcolare il numero totale di pagine necessarie a visualizzare i dati
			// ======================================================================
			if (isRequestParameterNullObj("CountRisultati"))
				CountRisultati = lCtrl.ExGetCountSoggettoCertificato(lSogMod).toString();
			else
				CountRisultati = getRequestStringParameter("CountRisultati");

			if (Integer.parseInt(CountRisultati) == 1) {
				// Nel caso di un solo record visualizzo direttamente il dettaglio...
				lSogMod = new SoggettoCertificatoModel((SoggettoCertificatoModel) lVect.firstElement());
				setRequestAttribute("soggettocertificato", lSogMod);
				setFunctionsAvailableToRequest(
						"siap.sico.soggettocertificato.action.ActLoadDettaglioSoggettoCertificato");

				lReturnPage = PG_LOAD_DETTAGLIOSOGGETTOCERTIFICATO;
			} else {
				// ...altrimenti la pagina con i risultati
				setRequestAttribute("CountRisultati", CountRisultati);
				setRequestAttribute(IWebConstants.NUM_PAGE, lPagina);
				setRequestAttribute(IWebConstants.REQUEST_FOR_PAGING, getCompleteRequestURL());
				setRequestAttribute("tipo_ricerca", tipo_ricerca);

				setRequestAttribute("soggettocertificato", lVect);

				lReturnPage = PG_RICERCASOGGETTOCERTIFICATO;
			}
		} // fine if tipo_ricerca

		return lReturnPage;
	}

}