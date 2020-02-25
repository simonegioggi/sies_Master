package siap.siep.competenza.action;

/**
* <p>Title: ActRicercaCompetenza</p>
* <p>Description: Classe Action per la ricerca di Competenza</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.util.Vector;

import f3b.util.F3BException;
import siap.sico.web.ActionSiap;
import siap.sico.web.ISICOCostantiWeb;
import siap.siep.competenza.controller.ICompetenza;
import siap.siep.competenza.model.CompetenzaModel;
import siap.siep.util.SIEPLookupRemote;

public class ActRicercaCompetenza extends ActionSiap implements ICostantiCompetenza {
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
		CompetenzaModel lComMod = new CompetenzaModel();

		lComMod.setIdCompetenza(getRequestBigDecimalParameter(CAMPO_ID_COMPETENZA));
		lComMod.setCodTipoProvvedimento(getRequestStringParameter(CAMPO_COD_TIPO_PROVVEDIMENTO));
		lComMod.setDataProvvedimento(getRequestDateParameter(CAMPO_ANNO_DATA_PROVVEDIMENTO,
				CAMPO_MESE_DATA_PROVVEDIMENTO, CAMPO_GIORNO_DATA_PROVVEDIMENTO));
		lComMod.setCodTipoAutoritaEmittente(getRequestStringParameter(CAMPO_COD_TIPO_AUTORITA_EMITTENTE));
		lComMod.setCodLuogoEmittente(getRequestStringParameter(CAMPO_COD_LUOGO_EMITTENTE));
		lComMod.setNumSezioneAutoritaEmittente(
				getRequestStringParameter(CAMPO_NUM_SEZIONE_AUTORITA_EMITTENTE));
		lComMod.setAnnoSentenza(getRequestBigDecimalParameter(CAMPO_ANNO_SENTENZA));
		lComMod.setNumeroSentenza(getRequestStringParameter(CAMPO_NUMERO_SENTENZA));
		lComMod.setDataIrrevocabilita(getRequestDateParameter(CAMPO_ANNO_DATA_IRREVOCABILITA,
				CAMPO_MESE_DATA_IRREVOCABILITA, CAMPO_GIORNO_DATA_IRREVOCABILITA));
		lComMod.setSenIdSentenza(getRequestBigDecimalParameter(CAMPO_SEN_ID_SENTENZA));
		lComMod.setFasSieIdFascicoloSiep(getRequestBigDecimalParameter(CAMPO_FAS_SIE_ID_FASCICOLO_SIEP));
		lComMod.setEveIdEvento(getRequestBigDecimalParameter(CAMPO_EVE_ID_EVENTO));
		lComMod.setChiaveAnno(getRequestBigDecimalParameter(CAMPO_CHIAVE_ANNO));
		lComMod.setChiaveUfficio(getRequestStringParameter(CAMPO_CHIAVE_UFFICIO));
		lComMod.setChiaveProgr(getRequestBigDecimalParameter(CAMPO_CHIAVE_PROGR));
		lComMod.setCodOperatoreInserimento(getRequestStringParameter(CAMPO_COD_OPERATORE_INSERIMENTO));
		lComMod.setDataInserimento(getRequestDateParameter(CAMPO_ANNO_DATA_INSERIMENTO,
				CAMPO_MESE_DATA_INSERIMENTO, CAMPO_GIORNO_DATA_INSERIMENTO));
		lComMod.setCodUfficioInserimento(getRequestStringParameter(CAMPO_COD_UFFICIO_INSERIMENTO));
		lComMod.setCodOperatoreAggiornamento(getRequestStringParameter(CAMPO_COD_OPERATORE_AGGIORNAMENTO));
		lComMod.setDataAggiornamento(getRequestDateParameter(CAMPO_ANNO_DATA_AGGIORNAMENTO,
				CAMPO_MESE_DATA_AGGIORNAMENTO, CAMPO_GIORNO_DATA_AGGIORNAMENTO));
		lComMod.setCodUfficioAggiornamento(getRequestStringParameter(CAMPO_COD_UFFICIO_AGGIORNAMENTO));

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
			ICompetenza lCtrl = SIEPLookupRemote.getCompetenzaRemote();
			Vector lVect = lCtrl.ExRicercaCompetenza(lComMod);

			if (lVect.size() == 0) {
				this.setRequestAttribute(ISICOCostantiWeb.MESSAGE_TEXT, "Nessun dato presente");
				return ISICOCostantiWeb.PG_MESSAGE;
			}

			setRequestAttribute("competenza", lVect);
			setRequestAttribute("tipo_ricerca", tipo_ricerca);

			lReturnPage = PG_RICERCACOMPETENZA;
		} else {
			String lPagina = "1";
			String CountRisultati;

			// ============================================================================
			// Recupero la pagina da visualizzare se prevengo dalla finestra dei risultati
			// ============================================================================
			if (!isRequestParameterNullObj(ISICOCostantiWeb.NUM_PAGE))
				lPagina = getRequestStringParameter(ISICOCostantiWeb.NUM_PAGE);

			// =========================================================
			// Istanzio il controller ed effettuo la ricerca paginata
			// =========================================================
			ICompetenza lCtrl = SIEPLookupRemote.getCompetenzaRemote();
			Vector lVect = lCtrl.ExRicercaCompetenzaPaged(lComMod, Integer.parseInt(lPagina));

			if (lVect.size() == 0) {
				this.setRequestAttribute(ISICOCostantiWeb.MESSAGE_TEXT, "Nessun dato presente");
				return ISICOCostantiWeb.PG_MESSAGE;
			}

			// ======================================================================
			// Recupero il numero di record totali della ricerca utilizzato per
			// calcolare il numero totale di pagine necessarie a visualizzare i dati
			// ======================================================================
			if (isRequestParameterNullObj("CountRisultati"))
				CountRisultati = lCtrl.ExGetCountCompetenza(lComMod).toString();
			else
				CountRisultati = getRequestStringParameter("CountRisultati");

			if (Integer.parseInt(CountRisultati) == 1) {
				// Nel caso di un solo record visualizzo direttamente il dettaglio...
				lComMod = new CompetenzaModel((CompetenzaModel) lVect.firstElement());
				setRequestAttribute("competenza", lComMod);
				setFunctionsAvailableToRequest("siap.siep.competenza.action.ActLoadDettaglioCompetenza");

				lReturnPage = PG_LOAD_DETTAGLIOCOMPETENZA;
			} else {
				// ...altrimenti la pagina con i risultati
				setRequestAttribute("CountRisultati", CountRisultati);
				setRequestAttribute(ISICOCostantiWeb.NUM_PAGE, lPagina);
				setRequestAttribute(ISICOCostantiWeb.REQUEST_FOR_PAGING, getCompleteRequestURL());
				setRequestAttribute("tipo_ricerca", tipo_ricerca);

				setRequestAttribute("competenza", lVect);

				lReturnPage = PG_RICERCACOMPETENZA;
			}
		} // fine if tipo_ricerca

		return lReturnPage;

	}
}
