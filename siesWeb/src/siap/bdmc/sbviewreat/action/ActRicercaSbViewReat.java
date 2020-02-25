package siap.bdmc.sbviewreat.action;

import java.util.Vector;

import siap.bdmc.BDMCLookupRemote;
import siap.bdmc.sbviewreat.controller.ISbViewReat;
import siap.bdmc.sbviewreat.model.SbViewReatModel;
import siap.sico.web.ActionSiap;
import siap.web.ISIAPCostantiWeb;
import f3b.util.F3BException;

/**
 * <p>
 * Title: ActRicercaSbViewReat
 * </p>
 * <p>
 * Description: Classe Action per la ricerca di SbViewReat
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
public class ActRicercaSbViewReat extends ActionSiap implements ICostantiSbViewReat {

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
		SbViewReatModel lSbVMod = new SbViewReatModel();

		lSbVMod.setNumeProgCapoImpu(getRequestBigDecimalParameter(CAMPO_NUME_PROG_CAPO_IMPU));
		lSbVMod.setNumeProgReat(getRequestBigDecimalParameter(CAMPO_NUME_PROG_REAT));
		lSbVMod.setCodiFontGiur(getRequestStringParameter(CAMPO_CODI_FONT_GIUR));
		lSbVMod.setAnnoFontGiur(getRequestBigDecimalParameter(CAMPO_ANNO_FONT_GIUR));
		lSbVMod.setNumeFontGiur(getRequestBigDecimalParameter(CAMPO_NUME_FONT_GIUR));
		lSbVMod.setArtiFontGiur(getRequestBigDecimalParameter(CAMPO_ARTI_FONT_GIUR));
		lSbVMod.setCommiArtiFont(getRequestStringParameter(CAMPO_COMMI_ARTI_FONT));
		lSbVMod.setLettArtiFont(getRequestStringParameter(CAMPO_LETT_ARTI_FONT));
		lSbVMod.setNumeArtiFont(getRequestStringParameter(CAMPO_NUME_ARTI_FONT));
		lSbVMod.setArtiQualFont(getRequestStringParameter(CAMPO_ARTI_QUAL_FONT));
		lSbVMod.setIdPren(getRequestBigDecimalParameter(CAMPO_ID_PREN));
		lSbVMod.setAnnoFascBdmc(getRequestBigDecimalParameter(CAMPO_ANNO_FASC_BDMC));
		lSbVMod.setNumeFascBdmc(getRequestBigDecimalParameter(CAMPO_NUME_FASC_BDMC));
		lSbVMod.setCodiSedeInst(getRequestStringParameter(CAMPO_CODI_SEDE_INST));

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
			ISbViewReat lCtrl = BDMCLookupRemote.getSbViewReatRemote();
			Vector lVect = lCtrl.ExRicercaSbViewReat(lSbVMod);

			if (lVect.size() == 0) {
				this.setRequestAttribute(ISIAPCostantiWeb.MESSAGE_TEXT, "Nessun dato presente");
				return ISIAPCostantiWeb.PG_MESSAGE;
			}

			setRequestAttribute("sbviewreat", lVect);
			setRequestAttribute("tipo_ricerca", tipo_ricerca);

			lReturnPage = PG_RICERCASBVIEWREAT;
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
			ISbViewReat lCtrl = BDMCLookupRemote.getSbViewReatRemote();
			Vector lVect = lCtrl.ExRicercaSbViewReatPaged(lSbVMod, Integer.parseInt(lPagina));

			if (lVect.size() == 0) {
				this.setRequestAttribute(ISIAPCostantiWeb.MESSAGE_TEXT, "Nessun dato presente");
				return ISIAPCostantiWeb.PG_MESSAGE;
			}

			// ======================================================================
			// Recupero il numero di record totali della ricerca utilizzato per
			// calcolare il numero totale di pagine necessarie a visualizzare i dati
			// ======================================================================
			if (isRequestParameterNullObj("CountRisultati"))
				CountRisultati = lCtrl.ExGetCountSbViewReat(lSbVMod).toString();
			else
				CountRisultati = getRequestStringParameter("CountRisultati");

			if (Integer.parseInt(CountRisultati) == 1) {
				// Nel caso di un solo record visualizzo direttamente il dettaglio...
				lSbVMod = new SbViewReatModel((SbViewReatModel) lVect.firstElement());
				setRequestAttribute("sbviewreat", lSbVMod);
				setFunctionsAvailableToRequest("siap.bdmc.sbviewreat.action.ActLoadDettaglioSbViewReat");

				lReturnPage = PG_LOAD_DETTAGLIOSBVIEWREAT;
			} else {
				// ...altrimenti la pagina con i risultati
				setRequestAttribute("CountRisultati", CountRisultati);
				setRequestAttribute(ISIAPCostantiWeb.NUM_PAGE, lPagina);
				setRequestAttribute(ISIAPCostantiWeb.REQUEST_FOR_PAGING, getCompleteRequestURL());
				setRequestAttribute("tipo_ricerca", tipo_ricerca);

				setRequestAttribute("sbviewreat", lVect);

				lReturnPage = PG_RICERCASBVIEWREAT;
			}
		} // fine if tipo_ricerca

		return lReturnPage;
	}

}