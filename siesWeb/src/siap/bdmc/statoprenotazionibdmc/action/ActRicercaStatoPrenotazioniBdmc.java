package siap.bdmc.statoprenotazionibdmc.action;

import java.math.BigDecimal;
import java.util.Vector;

import siap.bdmc.BDMCLookupRemote;
import siap.bdmc.statoprenotazionibdmc.controller.IStatoPrenotazioniBdmc;
import siap.bdmc.statoprenotazionibdmc.model.StatoPrenotazioniBdmcModel;
import siap.sico.web.ActionSiap;
import siap.web.ISIAPCostantiWeb;
import f3b.util.F3BException;

/**
 * <p>
 * Title: ActRicercaStatoPrenotazioniBdmc
 * </p>
 * <p>
 * Description: Classe Action per la ricerca di StatoPrenotazioniBdmc
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
public class ActRicercaStatoPrenotazioniBdmc extends ActionSiap implements ICostantiStatoPrenotazioniBdmc {

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
		StatoPrenotazioniBdmcModel lStaMod = new StatoPrenotazioniBdmcModel();

		lStaMod.setDataTrasmissione(getRequestDateParameter(CAMPO_ANNO_DATA_TRASMISSIONE,
				CAMPO_MESE_DATA_TRASMISSIONE, CAMPO_GIORNO_DATA_TRASMISSIONE));

		if (getRequestStringParameter(CAMPO_ESITO_ID).compareTo("-") != 0)
			lStaMod.setEsitoId(getRequestBigDecimalParameter(CAMPO_ESITO_ID));

		lStaMod.setIdPrenotazione(getRequestBigDecimalParameter(CAMPO_ID_PRENOTAZIONE));

		if (getRequestStringParameter(CAMPO_TIPO_TRASMISSIONE).compareTo("-") != 0)
			lStaMod.setTipoTrasmissione(getRequestStringParameter(CAMPO_TIPO_TRASMISSIONE));

		// =================================
		// Selezionare il tipo di ricerca
		// =================================
		String tipo_ricerca = "paginata";
		// String tipo_ricerca = "paginata";

		String lReturnPage = null;

		if (tipo_ricerca.equals("semplice")) {
			// =========================================================
			// Istanzio il controller ed effettuo la ricerca semplice
			// =========================================================
			IStatoPrenotazioniBdmc lCtrl = BDMCLookupRemote.getStatoPrenotazioniBdmcRemote();
			Vector lVect = lCtrl.ExRicercaStatoPrenotazioniBdmc(lStaMod);

			if (lVect.size() == 0) {
				this.setRequestAttribute(ISIAPCostantiWeb.MESSAGE_TEXT, "Nessun dato presente");
				return ISIAPCostantiWeb.PG_MESSAGE;
			}

			setRequestAttribute("statoprenotazionibdmc", lVect);
			setRequestAttribute("tipo_ricerca", tipo_ricerca);

			lReturnPage = PG_RICERCASTATOPRENOTAZIONIBDMC;
		} else {
			String lPagina = "1";
			BigDecimal CountRisultati;

			// ============================================================================
			// Recupero la pagina da visualizzare se prevengo dalla finestra dei risultati
			// ============================================================================
			if (!isRequestParameterNullObj(ISIAPCostantiWeb.NUM_PAGE))
				lPagina = getRequestStringParameter(ISIAPCostantiWeb.NUM_PAGE);

			// =========================================================
			// Istanzio il controller ed effettuo la ricerca paginata
			// =========================================================
			IStatoPrenotazioniBdmc lCtrl = BDMCLookupRemote.getStatoPrenotazioniBdmcRemote();
			Vector lVect = lCtrl.ExRicercaStatoPrenotazioniBdmcPaged(lStaMod, Integer.parseInt(lPagina));

			if (lVect.size() == 0) {
				this.setRequestAttribute(ISIAPCostantiWeb.MESSAGE_TEXT, "Nessun dato presente");
				return ISIAPCostantiWeb.PG_MESSAGE;
			}

			// ======================================================================
			// Recupero il numero di record totali della ricerca utilizzato per
			// calcolare il numero totale di pagine necessarie a visualizzare i dati
			// ======================================================================
			if (isRequestParameterNullObj("CountRisultati"))
				CountRisultati = lCtrl.ExGetCountStatoPrenotazioniBdmc(lStaMod);
			else
				CountRisultati = getRequestBigDecimalParameter("CountRisultati");

			/*
			 * if (CountRisultati.intValue()==1) { // Nel caso di un solo record visualizzo direttamente il
			 * dettaglio... lStaMod = new StatoPrenotazioniBdmcModel
			 * ((StatoPrenotazioniBdmcModel)lVect.firstElement());
			 * setRequestAttribute("statoprenotazionibdmc",lStaMod); setFunctionsAvailableToRequest(
			 * "siap.bdmc.statoprenotazionibdmc.action.ActLoadDettaglioStatoPrenotazioniBdmc");
			 * 
			 * lReturnPage = PG_LOAD_DETTAGLIOSTATOPRENOTAZIONIBDMC; } else {
			 */
			// ...altrimenti la pagina con i risultati
			setRequestAttribute("CountRisultati", CountRisultati);
			setRequestAttribute(ISIAPCostantiWeb.NUM_PAGE, lPagina);
			setRequestAttribute(ISIAPCostantiWeb.REQUEST_FOR_PAGING, getCompleteRequestURL());
			setRequestAttribute("tipo_ricerca", tipo_ricerca);

			setRequestAttribute("statoprenotazionibdmc", lVect);

			lReturnPage = PG_RICERCASTATOPRENOTAZIONIBDMC;
			// }
		} // fine if tipo_ricerca

		return lReturnPage;
	}

}