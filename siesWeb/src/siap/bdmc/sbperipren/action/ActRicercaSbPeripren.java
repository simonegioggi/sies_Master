package siap.bdmc.sbperipren.action;

import java.util.Vector;

import siap.bdmc.BDMCLookupRemote;
import siap.bdmc.sbperipren.controller.ISbPeripren;
import siap.bdmc.sbperipren.model.SbPeriprenModel;
import siap.sico.web.ActionSiap;
import siap.web.ISIAPCostantiWeb;
import f3b.util.F3BException;

/**
 * <p>
 * Title: ActRicercaSbPeripren
 * </p>
 * <p>
 * Description: Classe Action per la ricerca di SbPeripren
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
public class ActRicercaSbPeripren extends ActionSiap implements ICostantiSbPeripren {

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
		SbPeriprenModel lSbPMod = new SbPeriprenModel();

		lSbPMod.setDataInizPeri(getRequestDateParameter(CAMPO_ANNO_DATA_INIZ_PERI, CAMPO_MESE_DATA_INIZ_PERI,
				CAMPO_GIORNO_DATA_INIZ_PERI));
		lSbPMod.setDataFinePeri(getRequestDateParameter(CAMPO_ANNO_DATA_FINE_PERI, CAMPO_MESE_DATA_FINE_PERI,
				CAMPO_GIORNO_DATA_FINE_PERI));
		lSbPMod.setProgPeriPres(getRequestBigDecimalParameter(CAMPO_PROG_PERI_PRES));
		lSbPMod.setIdPren(getRequestBigDecimalParameter(CAMPO_ID_PREN));
		lSbPMod.setCodiUffiSies(getRequestStringParameter(CAMPO_COD_UFFI_SIES));
		lSbPMod.setAnnoFascSiep(getRequestBigDecimalParameter(CAMPO_ANNO_FASC_SIEP));
		lSbPMod.setNumeFascSiep(getRequestBigDecimalParameter(CAMPO_NUME_FASC_SIEP));
		lSbPMod.setCodiSedeInst(getRequestStringParameter(CAMPO_CODI_SEDE_INST));
		lSbPMod.setAnnoFascBdmc(getRequestBigDecimalParameter(CAMPO_ANNO_FASC_BDMC));
		lSbPMod.setNumeFascBdmc(getRequestBigDecimalParameter(CAMPO_NUME_FASC_BDMC));
		lSbPMod.setCodStatPrenPeri(getRequestStringParameter(CAMPO_COD_STAT_PREN_PERI));
		// lSbPMod.setCodTipoPeri ( getRequestStringParameter ( CAMPO_COD_TIPO_PERI) );
		lSbPMod.setDataPrenPeri(getRequestDateParameter(CAMPO_ANNO_DATA_PREN_PERI, CAMPO_MESE_DATA_PREN_PERI,
				CAMPO_GIORNO_DATA_PREN_PERI));

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
			ISbPeripren lCtrl = BDMCLookupRemote.getSbPeriprenRemote();
			Vector lVect = lCtrl.ExRicercaSbPeripren(lSbPMod);

			if (lVect.size() == 0) {
				this.setRequestAttribute(ISIAPCostantiWeb.MESSAGE_TEXT, "Nessun dato presente");
				return ISIAPCostantiWeb.PG_MESSAGE;
			}

			setRequestAttribute("sbperipren", lVect);
			setRequestAttribute("tipo_ricerca", tipo_ricerca);

			lReturnPage = PG_RICERCASBPERIPREN;
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
			ISbPeripren lCtrl = BDMCLookupRemote.getSbPeriprenRemote();
			Vector lVect = lCtrl.ExRicercaSbPeriprenPaged(lSbPMod, Integer.parseInt(lPagina));

			if (lVect.size() == 0) {
				this.setRequestAttribute(ISIAPCostantiWeb.MESSAGE_TEXT, "Nessun dato presente");
				return ISIAPCostantiWeb.PG_MESSAGE;
			}

			// ======================================================================
			// Recupero il numero di record totali della ricerca utilizzato per
			// calcolare il numero totale di pagine necessarie a visualizzare i dati
			// ======================================================================
			if (isRequestParameterNullObj("CountRisultati"))
				CountRisultati = lCtrl.ExGetCountSbPeripren(lSbPMod).toString();
			else
				CountRisultati = getRequestStringParameter("CountRisultati");

			if (Integer.parseInt(CountRisultati) == 1) {
				// Nel caso di un solo record visualizzo direttamente il dettaglio...
				lSbPMod = new SbPeriprenModel((SbPeriprenModel) lVect.firstElement());
				setRequestAttribute("sbperipren", lSbPMod);
				setFunctionsAvailableToRequest("siap.bdmc.sbperipren.action.ActLoadDettaglioSbPeripren");

				lReturnPage = PG_LOAD_DETTAGLIOSBPERIPREN;
			} else {
				// ...altrimenti la pagina con i risultati
				setRequestAttribute("CountRisultati", CountRisultati);
				setRequestAttribute(ISIAPCostantiWeb.NUM_PAGE, lPagina);
				setRequestAttribute(ISIAPCostantiWeb.REQUEST_FOR_PAGING, getCompleteRequestURL());
				setRequestAttribute("tipo_ricerca", tipo_ricerca);

				setRequestAttribute("sbperipren", lVect);

				lReturnPage = PG_RICERCASBPERIPREN;
			}
		} // fine if tipo_ricerca

		return lReturnPage;
	}

}