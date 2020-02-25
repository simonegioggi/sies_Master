package siap.bdmc.sbviewcapoimpu.action;

import java.util.Vector;

import siap.bdmc.BDMCLookupRemote;
import siap.bdmc.sbviewcapoimpu.controller.ISbViewCapoimpu;
import siap.bdmc.sbviewcapoimpu.model.SbViewCapoimpuModel;
import siap.sico.web.ActionSiap;
import siap.web.ISIAPCostantiWeb;
import f3b.util.F3BException;

/**
 * <p>
 * Title: ActRicercaSbViewCapoimpu
 * </p>
 * <p>
 * Description: Classe Action per la ricerca di SbViewCapoimpu
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
public class ActRicercaSbViewCapoimpu extends ActionSiap implements ICostantiSbViewCapoimpu {

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
		SbViewCapoimpuModel lSbVMod = new SbViewCapoimpuModel();

		lSbVMod.setFlagArti0056(getRequestStringParameter(CAMPO_FLAG_ARTI_0056));
		lSbVMod.setFlagArti0061(getRequestStringParameter(CAMPO_FLAG_ARTI_0061));
		lSbVMod.setArti0061Comm(getRequestStringParameter(CAMPO_ARTI_0061_COMM));
		lSbVMod.setFlagArti0081(getRequestStringParameter(CAMPO_FLAG_ARTI_0081));
		lSbVMod.setArti0081Comm(getRequestStringParameter(CAMPO_ARTI_0081_COMM));
		lSbVMod.setFlagArt0110(getRequestStringParameter(CAMPO_FLAG_ART_0110));
		lSbVMod.setFlagArti0112(getRequestStringParameter(CAMPO_FLAG_ARTI_0112));
		lSbVMod.setArti0112Commi(getRequestStringParameter(CAMPO_ARTI_0112_COMMI));
		lSbVMod.setFlagArti0113(getRequestStringParameter(CAMPO_FLAG_ARTI_0113));
		lSbVMod.setFlagArti0114(getRequestStringParameter(CAMPO_FLAG_ARTI_0114));
		lSbVMod.setFlagArti0116(getRequestStringParameter(CAMPO_FLAG_ARTI_0116));
		lSbVMod.setFlagArti0117(getRequestStringParameter(CAMPO_FLAG_ARTI_0117));
		lSbVMod.setLuogReat(getRequestStringParameter(CAMPO_LUOG_REAT));
		lSbVMod.setFlagPeriTemp(getRequestStringParameter(CAMPO_FLAG_PERI_TEMP));
		lSbVMod.setDataReat0101(getRequestDateParameter(CAMPO_ANNO_DATA_REAT_0101, CAMPO_MESE_DATA_REAT_0101,
				CAMPO_GIORNO_DATA_REAT_0101));
		lSbVMod.setDataReat0202(getRequestDateParameter(CAMPO_ANNO_DATA_REAT_0202, CAMPO_MESE_DATA_REAT_0202,
				CAMPO_GIORNO_DATA_REAT_0202));
		lSbVMod.setDescPeriTemp(getRequestStringParameter(CAMPO_DESC_PERI_TEMP));
		lSbVMod.setNumeProgCapoImpu(getRequestBigDecimalParameter(CAMPO_NUME_PROG_CAPO_IMPU));
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
			ISbViewCapoimpu lCtrl = BDMCLookupRemote.getSbViewCapoimpuRemote();
			Vector lVect = lCtrl.ExRicercaSbViewCapoimpu(lSbVMod);

			if (lVect.size() == 0) {
				this.setRequestAttribute(ISIAPCostantiWeb.MESSAGE_TEXT, "Nessun dato presente");
				return ISIAPCostantiWeb.PG_MESSAGE;
			}

			setRequestAttribute("sbviewcapoimpu", lVect);
			setRequestAttribute("tipo_ricerca", tipo_ricerca);

			lReturnPage = PG_RICERCASBVIEWCAPOIMPU;
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
			ISbViewCapoimpu lCtrl = BDMCLookupRemote.getSbViewCapoimpuRemote();
			Vector lVect = lCtrl.ExRicercaSbViewCapoimpuPaged(lSbVMod, Integer.parseInt(lPagina));

			if (lVect.size() == 0) {
				this.setRequestAttribute(ISIAPCostantiWeb.MESSAGE_TEXT, "Nessun dato presente");
				return ISIAPCostantiWeb.PG_MESSAGE;
			}

			// ======================================================================
			// Recupero il numero di record totali della ricerca utilizzato per
			// calcolare il numero totale di pagine necessarie a visualizzare i dati
			// ======================================================================
			if (isRequestParameterNullObj("CountRisultati"))
				CountRisultati = lCtrl.ExGetCountSbViewCapoimpu(lSbVMod).toString();
			else
				CountRisultati = getRequestStringParameter("CountRisultati");

			if (Integer.parseInt(CountRisultati) == 1) {
				// Nel caso di un solo record visualizzo direttamente il dettaglio...
				lSbVMod = new SbViewCapoimpuModel((SbViewCapoimpuModel) lVect.firstElement());
				setRequestAttribute("sbviewcapoimpu", lSbVMod);
				setFunctionsAvailableToRequest("siap.bdmc.sbviewcapoimpu.action.ActLoadDettaglioSbViewCapoimpu");

				lReturnPage = PG_LOAD_DETTAGLIOSBVIEWCAPOIMPU;
			} else {
				// ...altrimenti la pagina con i risultati
				setRequestAttribute("CountRisultati", CountRisultati);
				setRequestAttribute(ISIAPCostantiWeb.NUM_PAGE, lPagina);
				setRequestAttribute(ISIAPCostantiWeb.REQUEST_FOR_PAGING, getCompleteRequestURL());
				setRequestAttribute("tipo_ricerca", tipo_ricerca);

				setRequestAttribute("sbviewcapoimpu", lVect);

				lReturnPage = PG_RICERCASBVIEWCAPOIMPU;
			}
		} // fine if tipo_ricerca

		return lReturnPage;
	}

}
