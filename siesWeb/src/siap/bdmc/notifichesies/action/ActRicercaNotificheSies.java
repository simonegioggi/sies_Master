package siap.bdmc.notifichesies.action;

import java.util.Vector;

import siap.bdmc.BDMCLookupRemote;
import siap.bdmc.notifichesies.controller.INotificheSies;
import siap.bdmc.notifichesies.model.NotificheSiesModel;
import siap.sico.web.ActionSiap;
import siap.web.ISIAPCostantiWeb;
import f3b.util.F3BException;

/**
 * <p>
 * Title: ActRicercaNotificheSies
 * </p>
 * <p>
 * Description: Classe Action per la ricerca di NotificheSies
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
public class ActRicercaNotificheSies extends ActionSiap implements ICostantiNotificheSies {

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
		NotificheSiesModel lNotMod = new NotificheSiesModel();

		lNotMod.setIdNotificheSies(getRequestBigDecimalParameter(CAMPO_ID_NOTIFICHE_SIES));
		lNotMod.setAnnoSiep(getRequestBigDecimalParameter(CAMPO_ANNO_SIEP));
		lNotMod.setProgSiep(getRequestBigDecimalParameter(CAMPO_PROG_SIEP));
		lNotMod.setUfficioSiep(getRequestStringParameter(CAMPO_UFFICIO_SIEP));
		lNotMod.setAnnoFascBdmc(getRequestBigDecimalParameter(CAMPO_ANNO_FASC_BDMC));
		lNotMod.setUfficioFascBdmc(getRequestStringParameter(CAMPO_UFFICIO_FASC_BDMC));
		lNotMod.setNumeroFascBdmc(getRequestBigDecimalParameter(CAMPO_NUMERO_FASC_BDMC));
		lNotMod.setTipoNotifica(getRequestStringParameter(CAMPO_TIPO_NOTIFICA));
		lNotMod.setDataNotifica(getRequestDateParameter(CAMPO_ANNO_DATA_NOTIFICA, CAMPO_MESE_DATA_NOTIFICA,
				CAMPO_GIORNO_DATA_NOTIFICA));
		lNotMod.setStatoTrasmissione(getRequestStringParameter(CAMPO_STATO_TRASMISSIONE));
		lNotMod.setDataTrasmissione(getRequestDateParameter(CAMPO_ANNO_DATA_TRASMISSIONE,
				CAMPO_MESE_DATA_TRASMISSIONE, CAMPO_GIORNO_DATA_TRASMISSIONE));
		lNotMod.setIdPren(getRequestBigDecimalParameter(CAMPO_ID_PREN));
		lNotMod.setProgPeriPres(getRequestBigDecimalParameter(CAMPO_PROG_PERI_PRES));
		lNotMod.setCodOperatoreInserimento(getRequestStringParameter(CAMPO_COD_OPERATORE_INSERIMENTO));
		lNotMod.setDataInserimento(getRequestDateParameter(CAMPO_ANNO_DATA_INSERIMENTO,
				CAMPO_MESE_DATA_INSERIMENTO, CAMPO_GIORNO_DATA_INSERIMENTO));
		lNotMod.setCodUfficioInserimento(getRequestStringParameter(CAMPO_COD_UFFICIO_INSERIMENTO));

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
			INotificheSies lCtrl = BDMCLookupRemote.getNotificheSiesRemote();
			Vector lVect = lCtrl.ExRicercaNotificheSies(lNotMod);

			if (lVect.size() == 0) {
				this.setRequestAttribute(ISIAPCostantiWeb.MESSAGE_TEXT, "Nessun dato presente");
				return ISIAPCostantiWeb.PG_MESSAGE;
			}

			setRequestAttribute("notifichesies", lVect);
			setRequestAttribute("tipo_ricerca", tipo_ricerca);

			lReturnPage = PG_RICERCANOTIFICHESIES;
		} else {
			String lPagina = "1";
			String CountRisultati;

			// ============================================================================
			// Recupero la pagina da visualizzare se prevengo dalla finestra dei risultati
			// ============================================================================
			if (!isRequestParameterNullObj(ISIAPCostantiWeb.NUM_PAGE))
				lPagina = getRequestStringParameter(ISIAPCostantiWeb.NUM_PAGE);

			// =========================================================
			// Istanzio il controller ed effettuo la ricerca paginata
			// =========================================================
			INotificheSies lCtrl = BDMCLookupRemote.getNotificheSiesRemote();
			Vector lVect = lCtrl.ExRicercaNotificheSiesPaged(lNotMod, Integer.parseInt(lPagina));

			if (lVect.size() == 0) {
				this.setRequestAttribute(ISIAPCostantiWeb.MESSAGE_TEXT, "Nessun dato presente");
				return ISIAPCostantiWeb.PG_MESSAGE;
			}

			// ======================================================================
			// Recupero il numero di record totali della ricerca utilizzato per
			// calcolare il numero totale di pagine necessarie a visualizzare i dati
			// ======================================================================
			if (isRequestParameterNullObj("CountRisultati"))
				CountRisultati = lCtrl.ExGetCountNotificheSies(lNotMod).toString();
			else
				CountRisultati = getRequestStringParameter("CountRisultati");

			if (Integer.parseInt(CountRisultati) == 1) {
				// Nel caso di un solo record visualizzo direttamente il dettaglio...
				lNotMod = new NotificheSiesModel((NotificheSiesModel) lVect.firstElement());
				setRequestAttribute("notifichesies", lNotMod);
				setFunctionsAvailableToRequest("siap.bdmc.notifichesies.action.ActLoadDettaglioNotificheSies");

				lReturnPage = PG_LOAD_DETTAGLIONOTIFICHESIES;
			} else {
				// ...altrimenti la pagina con i risultati
				setRequestAttribute("CountRisultati", CountRisultati);
				setRequestAttribute(ISIAPCostantiWeb.NUM_PAGE, lPagina);
				setRequestAttribute(ISIAPCostantiWeb.REQUEST_FOR_PAGING, getCompleteRequestURL());
				setRequestAttribute("tipo_ricerca", tipo_ricerca);

				setRequestAttribute("notifichesies", lVect);

				lReturnPage = PG_RICERCANOTIFICHESIES;
			}
		} // fine if tipo_ricerca

		return lReturnPage;
	}

}