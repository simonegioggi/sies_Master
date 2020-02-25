package siap.siep.sollecitoesitotrasmissione.action;

/**
* <p>Title: ActRicercaSollecitoEsitoTrasmissione</p>
* <p>Description: Classe Action per la ricerca di SollecitoEsitoTrasmissione</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.util.F3BException;
import f3b.web.IWebConstants;
import siap.sico.web.ActionSiap;
import siap.siep.sollecitoesitotrasmissione.controller.ISollecitoEsitoTrasmissione;
import siap.siep.sollecitoesitotrasmissione.model.SollecitoEsitoTrasmissioneModel;
import siap.siep.util.SIEPLookupRemote;

public class ActRicercaSollecitoEsitoTrasmissione extends ActionSiap
		implements ICostantiSollecitoEsitoTrasmissione {
	Logger logger = Logger.getLogger("actionLogger");

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
		SollecitoEsitoTrasmissioneModel lSolMod = new SollecitoEsitoTrasmissioneModel();

		lSolMod.setIdSollecito(getRequestBigDecimalParameter(CAMPO_ID_SOLLECITO));
		lSolMod.setCodUffSollecitato(getRequestStringParameter(CAMPO_COD_UFF_SOLLECITATO));
		lSolMod.setOggettoMsSollecito(getRequestStringParameter(CAMPO_OGGETTO_MS_SOLLECITO));
		lSolMod.setOggettoMsSollecitato(getRequestStringParameter(CAMPO_OGGETTO_MS_SOLLECITATO));
		lSolMod.setDataInvioMsSollecitato(getRequestDateParameter(CAMPO_ANNO_DATA_INVIO_MS_SOLLECITATO,
				CAMPO_MESE_DATA_INVIO_MS_SOLLECITATO, CAMPO_GIORNO_DATA_INVIO_MS_SOLLECITATO));
		lSolMod.setMesIdMessaggioSollecitato(
				getRequestBigDecimalParameter(CAMPO_MES_ID_MESSAGGIO_SOLLECITATO));
		lSolMod.setCodUffInoltrante(getRequestStringParameter(CAMPO_COD_UFF_INOLTRANTE));
		lSolMod.setDataInoltro(getRequestDateParameter(CAMPO_ANNO_DATA_INOLTRO, CAMPO_MESE_DATA_INOLTRO,
				CAMPO_GIORNO_DATA_INOLTRO));
		lSolMod.setMesIdMessaggioInoltro(getRequestBigDecimalParameter(CAMPO_MES_ID_MESSAGGIO_INOLTRO));
		lSolMod.setEveIdEvento(getRequestBigDecimalParameter(CAMPO_EVE_ID_EVENTO));
		lSolMod.setFasSieIdFascicoloSiep(getRequestBigDecimalParameter(CAMPO_FAS_SIE_ID_FASCICOLO_SIEP));
		lSolMod.setCodOperatoreInserimento(getRequestStringParameter(CAMPO_COD_OPERATORE_INSERIMENTO));
		lSolMod.setDataInserimento(getRequestDateParameter(CAMPO_ANNO_DATA_INSERIMENTO,
				CAMPO_MESE_DATA_INSERIMENTO, CAMPO_GIORNO_DATA_INSERIMENTO));
		lSolMod.setCodUfficioInserimento(getRequestStringParameter(CAMPO_COD_UFFICIO_INSERIMENTO));
		lSolMod.setCodOperatoreAggiornamento(getRequestStringParameter(CAMPO_COD_OPERATORE_AGGIORNAMENTO));
		lSolMod.setDataAggiornamento(getRequestDateParameter(CAMPO_ANNO_DATA_AGGIORNAMENTO,
				CAMPO_MESE_DATA_AGGIORNAMENTO, CAMPO_GIORNO_DATA_AGGIORNAMENTO));
		lSolMod.setCodUfficioAggiornamento(getRequestStringParameter(CAMPO_COD_UFFICIO_AGGIORNAMENTO));

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
			ISollecitoEsitoTrasmissione lCtrl = SIEPLookupRemote.getSollecitoEsitoTrasmissioneRemote();
			Vector lVect = lCtrl.ExRicercaSollecitoEsitoTrasmissione(lSolMod);

			if (lVect.size() == 0) {
				this.setRequestAttribute(IWebConstants.MESSAGE_TEXT, "Nessun dato presente");
				return IWebConstants.PG_MESSAGE;
			}

			setRequestAttribute("sollecitoesitotrasmissione", lVect);
			setRequestAttribute("tipo_ricerca", tipo_ricerca);

			lReturnPage = PG_RICERCASOLLECITOESITOTRASMISSIONE;
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
			ISollecitoEsitoTrasmissione lCtrl = SIEPLookupRemote.getSollecitoEsitoTrasmissioneRemote();
			Vector lVect = lCtrl.ExRicercaSollecitoEsitoTrasmissionePaged(lSolMod, Integer.parseInt(lPagina));

			if (lVect.size() == 0) {
				this.setRequestAttribute(IWebConstants.MESSAGE_TEXT, "Nessun dato presente");
				return IWebConstants.PG_MESSAGE;
			}

			// ======================================================================
			// Recupero il numero di record totali della ricerca utilizzato per
			// calcolare il numero totale di pagine necessarie a visualizzare i dati
			// ======================================================================
			if (isRequestParameterNullObj("CountRisultati"))
				CountRisultati = lCtrl.ExGetCountSollecitoEsitoTrasmissione(lSolMod).toString();
			else
				CountRisultati = getRequestStringParameter("CountRisultati");

			if (Integer.parseInt(CountRisultati) == 1) {
				// Nel caso di un solo record visualizzo direttamente il dettaglio...
				lSolMod = new SollecitoEsitoTrasmissioneModel(
						(SollecitoEsitoTrasmissioneModel) lVect.firstElement());
				setRequestAttribute("sollecitoesitotrasmissione", lSolMod);
				setFunctionsAvailableToRequest(
						"siap.jms.sollecitoesitotrasmissione.action.ActLoadDettaglioSollecitoEsitoTrasmissione");

				lReturnPage = PG_LOAD_DETTAGLIOSOLLECITOESITOTRASMISSIONE;
			} else {
				// ...altrimenti la pagina con i risultati
				setRequestAttribute("CountRisultati", CountRisultati);
				setRequestAttribute(IWebConstants.NUM_PAGE, lPagina);
				setRequestAttribute(IWebConstants.REQUEST_FOR_PAGING, getCompleteRequestURL());
				setRequestAttribute("tipo_ricerca", tipo_ricerca);

				setRequestAttribute("sollecitoesitotrasmissione", lVect);

				lReturnPage = PG_RICERCASOLLECITOESITOTRASMISSIONE;
			}
		} // fine if tipo_ricerca

		return lReturnPage;

	}
}
