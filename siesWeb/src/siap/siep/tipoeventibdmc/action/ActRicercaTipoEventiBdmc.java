package siap.siep.tipoeventibdmc.action;

import java.util.Vector;

import siap.sico.web.ActionSiap;
import siap.siep.tipoeventibdmc.controller.ITipoEventiBdmc;
import siap.siep.tipoeventibdmc.model.TipoEventiBdmcModel;
import siap.siep.util.SIEPLookupRemote;
import siap.web.ISIAPCostantiWeb;
import f3b.util.F3BException;

/**
 * <p>
 * Title: ActRicercaTipoEventiBdmc
 * </p>
 * <p>
 * Description: Classe Action per la ricerca di TipoEventiBdmc
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
public class ActRicercaTipoEventiBdmc extends ActionSiap implements ICostantiTipoEventiBdmc {

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
		TipoEventiBdmcModel lTipMod = new TipoEventiBdmcModel();

		lTipMod.setIdTipoEventiBdmc(getRequestBigDecimalParameter(CAMPO_ID_TIPO_EVENTI_BDMC));
		lTipMod.setCodTipoEvento(getRequestStringParameter(CAMPO_COD_TIPO_EVENTO));
		lTipMod.setCodProvvedimento(getRequestStringParameter(CAMPO_COD_PROVVEDIMENTO));
		lTipMod.setCodMotivo(getRequestStringParameter(CAMPO_COD_MOTIVO));

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
			ITipoEventiBdmc lCtrl = SIEPLookupRemote.getTipoEventiBdmcRemote();
			Vector lVect = lCtrl.ExRicercaTipoEventiBdmc(lTipMod);

			if (lVect.size() == 0) {
				this.setRequestAttribute(ISIAPCostantiWeb.MESSAGE_TEXT, "Nessun dato presente");
				return ISIAPCostantiWeb.PG_MESSAGE;
			}

			setRequestAttribute("tipoeventibdmc", lVect);
			setRequestAttribute("tipo_ricerca", tipo_ricerca);

			lReturnPage = PG_RICERCATIPOEVENTIBDMC;
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
			ITipoEventiBdmc lCtrl = SIEPLookupRemote.getTipoEventiBdmcRemote();
			Vector lVect = lCtrl.ExRicercaTipoEventiBdmcPaged(lTipMod, Integer.parseInt(lPagina));

			if (lVect.size() == 0) {
				this.setRequestAttribute(ISIAPCostantiWeb.MESSAGE_TEXT, "Nessun dato presente");
				return ISIAPCostantiWeb.PG_MESSAGE;
			}

			// ======================================================================
			// Recupero il numero di record totali della ricerca utilizzato per
			// calcolare il numero totale di pagine necessarie a visualizzare i dati
			// ======================================================================
			if (isRequestParameterNullObj("CountRisultati"))
				CountRisultati = lCtrl.ExGetCountTipoEventiBdmc(lTipMod).toString();
			else
				CountRisultati = getRequestStringParameter("CountRisultati");

			if (Integer.parseInt(CountRisultati) == 1) {
				// Nel caso di un solo record visualizzo direttamente il dettaglio...
				lTipMod = new TipoEventiBdmcModel((TipoEventiBdmcModel) lVect.firstElement());
				setRequestAttribute("tipoeventibdmc", lTipMod);
				setFunctionsAvailableToRequest("siap.siep.tipoeventibdmc.action.ActLoadDettaglioTipoEventiBdmc");

				lReturnPage = PG_LOAD_DETTAGLIOTIPOEVENTIBDMC;
			} else {
				// ...altrimenti la pagina con i risultati
				setRequestAttribute("CountRisultati", CountRisultati);
				setRequestAttribute(ISIAPCostantiWeb.NUM_PAGE, lPagina);
				setRequestAttribute(ISIAPCostantiWeb.REQUEST_FOR_PAGING, getCompleteRequestURL());
				setRequestAttribute("tipo_ricerca", tipo_ricerca);

				setRequestAttribute("tipoeventibdmc", lVect);

				lReturnPage = PG_RICERCATIPOEVENTIBDMC;
			}
		} // fine if tipo_ricerca

		return lReturnPage;
	}

}