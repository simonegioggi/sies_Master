package siap.siep.annotazioneesitotrasmissione.action;

/**
* <p>Title: ActRicercaAnnotazioneEsitoTrasmissione</p>
* <p>Description: Classe Action per la ricerca di AnnotazioneEsitoTrasmissione</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.util.Vector;

import org.apache.log4j.Logger;

import siap.sico.web.ActionSiap;
import siap.siep.annotazioneesitotrasmissione.controller.IAnnotazioneEsitoTrasmissione;
import siap.siep.annotazioneesitotrasmissione.model.AnnotazioneEsitoTrasmissioneModel;
import siap.siep.util.SIEPLookupRemote;
import f3b.util.F3BException;
import f3b.web.IWebConstants;

@SuppressWarnings("rawtypes")
public class ActRicercaAnnotazioneEsitoTrasmissione extends ActionSiap
		implements ICostantiAnnotazioneEsitoTrasmissione {

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
		AnnotazioneEsitoTrasmissioneModel lAnnMod = new AnnotazioneEsitoTrasmissioneModel();

		lAnnMod.setIdEsitoTrasmissione(getRequestBigDecimalParameter(CAMPO_ID_ESITO_TRASMISSIONE));
		lAnnMod.setDataTrasmissione(getRequestDateParameter(CAMPO_ANNO_DATA_TRASMISSIONE,
				CAMPO_MESE_DATA_TRASMISSIONE, CAMPO_GIORNO_DATA_TRASMISSIONE));
		lAnnMod.setOggettoTrasmissione(getRequestStringParameter(CAMPO_OGGETTO_TRASMISSIONE));
		lAnnMod.setCodUfficioDestinatario(getRequestStringParameter(CAMPO_COD_UFFICIO_DESTINATARIO));
		lAnnMod.setCodUfficioInoltrante(getRequestStringParameter(CAMPO_COD_UFFICIO_INOLTRANTE));
		lAnnMod.setCodUfficioEsito(getRequestStringParameter(CAMPO_COD_UFFICIO_ESITO));
		lAnnMod.setDataEsito(getRequestDateParameter(CAMPO_ANNO_DATA_ESITO, CAMPO_MESE_DATA_ESITO,
				CAMPO_GIORNO_DATA_ESITO));
		lAnnMod.setCodEsito(getRequestStringParameter(CAMPO_COD_ESITO));
		lAnnMod.setNoteEsito(getRequestStringParameter(CAMPO_NOTE_ESITO));
		lAnnMod.setChiaveAnno(getRequestBigDecimalParameter(CAMPO_CHIAVE_ANNO));
		lAnnMod.setChiaveProgr(getRequestBigDecimalParameter(CAMPO_CHIAVE_PROGR));
		lAnnMod.setChiaveUfficio(getRequestStringParameter(CAMPO_CHIAVE_UFFICIO));
		lAnnMod.setEveIdEvento(getRequestBigDecimalParameter(CAMPO_EVE_ID_EVENTO));
		lAnnMod.setFasSieIdFascicoloSiep(getRequestBigDecimalParameter(CAMPO_FAS_SIE_ID_FASCICOLO_SIEP));
		lAnnMod.setMesIdMessaggioRichiesta(getRequestBigDecimalParameter(CAMPO_MES_ID_MESSAGGIO_RICHIESTA));
		lAnnMod.setMesIdMessaggioEsito(getRequestBigDecimalParameter(CAMPO_MES_ID_MESSAGGIO_ESITO));
		lAnnMod.setCodOperatoreInserimento(getRequestStringParameter(CAMPO_COD_OPERATORE_INSERIMENTO));
		lAnnMod.setDataInserimento(getRequestDateParameter(CAMPO_ANNO_DATA_INSERIMENTO,
				CAMPO_MESE_DATA_INSERIMENTO, CAMPO_GIORNO_DATA_INSERIMENTO));
		lAnnMod.setCodUfficioInserimento(getRequestStringParameter(CAMPO_COD_UFFICIO_INSERIMENTO));
		lAnnMod.setCodOperatoreAggiornamento(getRequestStringParameter(CAMPO_COD_OPERATORE_AGGIORNAMENTO));
		lAnnMod.setDataAggiornamento(getRequestDateParameter(CAMPO_ANNO_DATA_AGGIORNAMENTO,
				CAMPO_MESE_DATA_AGGIORNAMENTO, CAMPO_GIORNO_DATA_AGGIORNAMENTO));
		lAnnMod.setCodUfficioAggiornamento(getRequestStringParameter(CAMPO_COD_UFFICIO_AGGIORNAMENTO));

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
			IAnnotazioneEsitoTrasmissione lCtrl = SIEPLookupRemote.getAnnotazioneEsitoTrasmissioneRemote();
			Vector lVect = lCtrl.ExRicercaAnnotazioneEsitoTrasmissione(lAnnMod);

			if (lVect.size() == 0) {
				this.setRequestAttribute(IWebConstants.MESSAGE_TEXT, "Nessun dato presente");
				return IWebConstants.PG_MESSAGE;
			}

			setRequestAttribute("annotazioneesitotrasmissione", lVect);
			setRequestAttribute("tipo_ricerca", tipo_ricerca);

			lReturnPage = PG_RICERCAANNOTAZIONEESITOTRASMISSIONE;
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
			IAnnotazioneEsitoTrasmissione lCtrl = SIEPLookupRemote.getAnnotazioneEsitoTrasmissioneRemote();
			Vector lVect = lCtrl.ExRicercaAnnotazioneEsitoTrasmissionePaged(lAnnMod,
					Integer.parseInt(lPagina));

			if (lVect.size() == 0) {
				this.setRequestAttribute(IWebConstants.MESSAGE_TEXT, "Nessun dato presente");
				return IWebConstants.PG_MESSAGE;
			}

			// ======================================================================
			// Recupero il numero di record totali della ricerca utilizzato per
			// calcolare il numero totale di pagine necessarie a visualizzare i dati
			// ======================================================================
			if (isRequestParameterNullObj("CountRisultati"))
				CountRisultati = lCtrl.ExGetCountAnnotazioneEsitoTrasmissione(lAnnMod).toString();
			else
				CountRisultati = getRequestStringParameter("CountRisultati");

			if (Integer.parseInt(CountRisultati) == 1) {
				// Nel caso di un solo record visualizzo direttamente il dettaglio...
				lAnnMod = new AnnotazioneEsitoTrasmissioneModel(
						(AnnotazioneEsitoTrasmissioneModel) lVect.firstElement());
				setRequestAttribute("annotazioneesitotrasmissione", lAnnMod);
				setFunctionsAvailableToRequest(
						"siap.siep.annotazioneesitotrasmissione.action.ActLoadDettaglioAnnotazioneEsitoTrasmissione");

				lReturnPage = PG_LOAD_DETTAGLIOANNOTAZIONEESITOTRASMISSIONE;
			} else {
				// ...altrimenti la pagina con i risultati
				setRequestAttribute("CountRisultati", CountRisultati);
				setRequestAttribute(IWebConstants.NUM_PAGE, lPagina);
				setRequestAttribute(IWebConstants.REQUEST_FOR_PAGING, getCompleteRequestURL());
				setRequestAttribute("tipo_ricerca", tipo_ricerca);

				setRequestAttribute("annotazioneesitotrasmissione", lVect);

				lReturnPage = PG_RICERCAANNOTAZIONEESITOTRASMISSIONE;
			}
		} // fine if tipo_ricerca

		return lReturnPage;
	}

}