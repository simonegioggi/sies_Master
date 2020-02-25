package siap.sico.trasmissione.action;

/**
* <p>Title: ActRicercaTrasmissioni</p>
* <p>Description: Classe Action per la ricerca di Trasmissioni</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.util.Vector;

import org.apache.log4j.Logger;

import siap.sico.trasmissione.controller.ITrasmissioni;
import siap.sico.trasmissione.model.TrasmissioniModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import f3b.util.F3BException;
import f3b.web.IWebConstants;

@SuppressWarnings("rawtypes")
public class ActRicercaTrasmissioni extends ActionSiap implements ICostantiTrasmissioni {

	Logger logger = Logger.getLogger("actionLogger");

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
		TrasmissioniModel lTraMod = new TrasmissioniModel();

		lTraMod.setIdTrasmissione(getRequestBigDecimalParameter(CAMPO_ID_TRASMISSIONE));
		lTraMod.setTipoTrasmissione(getRequestStringParameter(CAMPO_TIPO_TRASMISSIONE));
		lTraMod.setDataTrasmissione(getRequestDateParameter(CAMPO_ANNO_DATA_TRASMISSIONE,
				CAMPO_MESE_DATA_TRASMISSIONE, CAMPO_GIORNO_DATA_TRASMISSIONE));
		lTraMod.setEsitoTrasmissione(getRequestStringParameter(CAMPO_ESITO_TRASMISSIONE));
		lTraMod.setCodErrore(getRequestStringParameter(CAMPO_COD_ERRORE));
		lTraMod.setTipoOperazione(getRequestStringParameter(CAMPO_TIPO_OPERAZIONE));
		lTraMod.setDestinazione(getRequestStringParameter(CAMPO_DESTINAZIONE));
		lTraMod.setChiaveSiesSogg(getRequestBigDecimalParameter(CAMPO_CHIAVE_SIES_SOGG));
		lTraMod.setChiaveSiesFasc(getRequestBigDecimalParameter(CAMPO_CHIAVE_SIES_FASC));
		lTraMod.setChiaveNscSogg(getRequestBigDecimalParameter(CAMPO_CHIAVE_NSC_SOGG));
		lTraMod.setChiaveNscProv(getRequestBigDecimalParameter(CAMPO_CHIAVE_NSC_PROV));
		lTraMod.setChiaveAnno(getRequestBigDecimalParameter(CAMPO_CHIAVE_ANNO));
		lTraMod.setChiaveProgr(getRequestBigDecimalParameter(CAMPO_CHIAVE_PROGR));
		lTraMod.setCodOperatoreInserimento(getRequestStringParameter(CAMPO_COD_OPERATORE_INSERIMENTO));
		lTraMod.setDataInserimento(getRequestDateParameter(CAMPO_ANNO_DATA_INSERIMENTO,
				CAMPO_MESE_DATA_INSERIMENTO, CAMPO_GIORNO_DATA_INSERIMENTO));
		lTraMod.setCodUfficioInserimento(getRequestStringParameter(CAMPO_COD_UFFICIO_INSERIMENTO));

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
			ITrasmissioni lCtrl = SICOLookupRemote.getTrasmissioniRemote();
			Vector lVect = lCtrl.ExRicercaTrasmissioni(lTraMod);

			if (lVect.size() == 0) {
				this.setRequestAttribute(IWebConstants.MESSAGE_TEXT, "Nessun dato presente");
				return IWebConstants.PG_MESSAGE;
			}

			setRequestAttribute("trasmissioni", lVect);
			setRequestAttribute("tipo_ricerca", tipo_ricerca);

			lReturnPage = PG_RICERCATRASMISSIONI;
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
			ITrasmissioni lCtrl = SICOLookupRemote.getTrasmissioniRemote();
			Vector lVect = lCtrl.ExRicercaTrasmissioniPaged(lTraMod, Integer.parseInt(lPagina));

			if (lVect.size() == 0) {
				this.setRequestAttribute(IWebConstants.MESSAGE_TEXT, "Nessun dato presente");
				return IWebConstants.PG_MESSAGE;
			}

			// ======================================================================
			// Recupero il numero di record totali della ricerca utilizzato per
			// calcolare il numero totale di pagine necessarie a visualizzare i dati
			// ======================================================================
			if (isRequestParameterNullObj("CountRisultati"))
				CountRisultati = lCtrl.ExGetCountTrasmissioni(lTraMod).toString();
			else
				CountRisultati = getRequestStringParameter("CountRisultati");

			if (Integer.parseInt(CountRisultati) == 1) {
				// Nel caso di un solo record visualizzo direttamente il dettaglio...
				lTraMod = new TrasmissioniModel((TrasmissioniModel) lVect.firstElement());
				setRequestAttribute("trasmissioni", lTraMod);
				setFunctionsAvailableToRequest("siap.sico.trasmissione.action.ActLoadDettaglioTrasmissioni");

				lReturnPage = PG_LOAD_DETTAGLIOTRASMISSIONI;
			} else {
				// ...altrimenti la pagina con i risultati
				setRequestAttribute("CountRisultati", CountRisultati);
				setRequestAttribute(IWebConstants.NUM_PAGE, lPagina);
				setRequestAttribute(IWebConstants.REQUEST_FOR_PAGING, getCompleteRequestURL());
				setRequestAttribute("tipo_ricerca", tipo_ricerca);

				setRequestAttribute("trasmissioni", lVect);

				lReturnPage = PG_RICERCATRASMISSIONI;
			}
		} // fine if tipo_ricerca

		return lReturnPage;
	}

}