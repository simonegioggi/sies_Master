package siap.siep.misuracautelarebdmc.action;

import java.util.Vector;

import siap.sico.web.ActionSiap;
import siap.siep.misuracautelarebdmc.controller.IMisuraCautelareBdmc;
import siap.siep.misuracautelarebdmc.model.MisuraCautelareBdmcModel;
import siap.siep.util.SIEPLookupRemote;
import siap.web.ISIAPCostantiWeb;
import f3b.util.F3BException;

/**
* <p>Title: ActRicercaMisuraCautelareBdmc</p>
* <p>Description: Classe Action per la ricerca di MisuraCautelareBdmc</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/
public class ActRicercaMisuraCautelareBdmc extends ActionSiap implements ICostantiMisuraCautelareBdmc
 {

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
		MisuraCautelareBdmcModel lMisMod = new MisuraCautelareBdmcModel();

		lMisMod.setIdMisuraCautelare(getRequestBigDecimalParameter(CAMPO_ID_MISURA_CAUTELARE));
		lMisMod.setFasSieIdFascicoloSiep(getRequestBigDecimalParameter(CAMPO_FAS_SIE_ID_FASCICOLO_SIEP));
		lMisMod.setSogIdSoggetto(getRequestBigDecimalParameter(CAMPO_SOG_ID_SOGGETTO));
		lMisMod.setEveIdEvento(getRequestBigDecimalParameter(CAMPO_EVE_ID_EVENTO));
		lMisMod.setPenResIdPenaResidua(getRequestBigDecimalParameter(CAMPO_PEN_RES_ID_PENA_RESIDUA));
		lMisMod.setIdPren(getRequestBigDecimalParameter(CAMPO_ID_PREN));
		lMisMod.setProgPeriPres(getRequestBigDecimalParameter(CAMPO_PROG_PERI_PRES));
		lMisMod.setFlagCaricamento(getRequestStringParameter(CAMPO_FLAG_CARICAMENTO));
		lMisMod.setFlagStato(getRequestStringParameter(CAMPO_FLAG_STATO));
		lMisMod.setCodTipoMisura(getRequestStringParameter(CAMPO_COD_TIPO_MISURA));
		lMisMod.setDataInizio(getRequestDateParameter(CAMPO_ANNO_DATA_INIZIO, CAMPO_MESE_DATA_INIZIO,
				CAMPO_GIORNO_DATA_INIZIO));
		lMisMod.setDataFine(getRequestDateParameter(CAMPO_ANNO_DATA_FINE, CAMPO_MESE_DATA_FINE,
				CAMPO_GIORNO_DATA_FINE));
		lMisMod.setNumAnni(getRequestBigDecimalParameter(CAMPO_NUM_ANNI));
		lMisMod.setNumMesi(getRequestBigDecimalParameter(CAMPO_NUM_MESI));
		lMisMod.setNumGiorni(getRequestBigDecimalParameter(CAMPO_NUM_GIORNI));
		lMisMod.setIstDetIdIstitutoDetenzione(getRequestStringParameter(CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE));
		lMisMod.setAltroLuogoDetenzione(getRequestStringParameter(CAMPO_ALTRO_LUOGO_DETENZIONE));
		lMisMod.setFlagComputabile(getRequestStringParameter(CAMPO_FLAG_COMPUTABILE));
		lMisMod.setCodMotivoNonComputabile(getRequestStringParameter(CAMPO_COD_MOTIVO_NON_COMPUTABILE));
		lMisMod.setCodTipoUfficioRifer(getRequestStringParameter(CAMPO_COD_TIPO_UFFICIO_RIFER));
		lMisMod.setCodLuogoUfficioRifer(getRequestStringParameter(CAMPO_COD_LUOGO_UFFICIO_RIFER));
		lMisMod.setDataComputo(getRequestDateParameter(CAMPO_ANNO_DATA_COMPUTO, CAMPO_MESE_DATA_COMPUTO,
				CAMPO_GIORNO_DATA_COMPUTO));
		lMisMod.setAnnoFascSiep(getRequestBigDecimalParameter(CAMPO_ANNO_FASC_SIEP));
		lMisMod.setNumeFascSiep(getRequestBigDecimalParameter(CAMPO_NUME_FASC_SIEP));
		lMisMod.setNote(getRequestStringParameter(CAMPO_NOTE));
		lMisMod.setAnnoFascBdmc(getRequestBigDecimalParameter(CAMPO_ANNO_FASC_BDMC));
		lMisMod.setNumeFascBdmc(getRequestBigDecimalParameter(CAMPO_NUME_FASC_BDMC));
		lMisMod.setCodUfficioBdmc(getRequestStringParameter(CAMPO_COD_UFFICIO_BDMC));
		lMisMod.setAnnoRgnr(getRequestBigDecimalParameter(CAMPO_ANNO_RGNR));
		lMisMod.setNumeRgnr(getRequestBigDecimalParameter(CAMPO_NUME_RGNR));
		lMisMod.setCodUfficioRgnr(getRequestStringParameter(CAMPO_COD_UFFICIO_RGNR));
		lMisMod.setAnnoRegeGip(getRequestBigDecimalParameter(CAMPO_ANNO_REGE_GIP));
		lMisMod.setNumeroRegeGip(getRequestBigDecimalParameter(CAMPO_NUMERO_REGE_GIP));
		lMisMod.setCodUfficioGip(getRequestStringParameter(CAMPO_COD_UFFICIO_GIP));
		lMisMod.setAnnoRegeDib(getRequestBigDecimalParameter(CAMPO_ANNO_REGE_DIB));
		lMisMod.setNumeroRegeDib(getRequestBigDecimalParameter(CAMPO_NUMERO_REGE_DIB));
		lMisMod.setCodUfficioDib(getRequestStringParameter(CAMPO_COD_UFFICIO_DIB));
		lMisMod.setAnnoRegeCas(getRequestBigDecimalParameter(CAMPO_ANNO_REGE_CAS));
		lMisMod.setNumeroRegeCas(getRequestBigDecimalParameter(CAMPO_NUMERO_REGE_CAS));
		lMisMod.setCodUfficioCas(getRequestStringParameter(CAMPO_COD_UFFICIO_CAS));
		lMisMod.setAnnoRegeCap(getRequestBigDecimalParameter(CAMPO_ANNO_REGE_CAP));
		lMisMod.setNumeroRegeCap(getRequestBigDecimalParameter(CAMPO_NUMERO_REGE_CAP));
		lMisMod.setCodUfficioCap(getRequestStringParameter(CAMPO_COD_UFFICIO_CAP));
		lMisMod.setAnnoRegeCasap(getRequestBigDecimalParameter(CAMPO_ANNO_REGE_CASAP));
		lMisMod.setNumeroRegeCasap(getRequestBigDecimalParameter(CAMPO_NUMERO_REGE_CASAP));
		lMisMod.setCodUfficioCasap(getRequestStringParameter(CAMPO_COD_UFFICIO_CASAP));
		lMisMod.setCodOperatoreInserimento(getRequestStringParameter(CAMPO_COD_OPERATORE_INSERIMENTO));
		lMisMod.setDataInserimento(getRequestDateParameter(CAMPO_ANNO_DATA_INSERIMENTO,
				CAMPO_MESE_DATA_INSERIMENTO, CAMPO_GIORNO_DATA_INSERIMENTO));
		lMisMod.setCodUfficioInserimento(getRequestStringParameter(CAMPO_COD_UFFICIO_INSERIMENTO));
		lMisMod.setCodOperatoreAggiornamento(getRequestStringParameter(CAMPO_COD_OPERATORE_AGGIORNAMENTO));
		lMisMod.setDataAggiornamento(getRequestDateParameter(CAMPO_ANNO_DATA_AGGIORNAMENTO,
				CAMPO_MESE_DATA_AGGIORNAMENTO, CAMPO_GIORNO_DATA_AGGIORNAMENTO));
		lMisMod.setCodUfficioAggiornamento(getRequestStringParameter(CAMPO_COD_UFFICIO_AGGIORNAMENTO));

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
			IMisuraCautelareBdmc lCtrl = SIEPLookupRemote.getMisuraCautelareBdmcRemote();
			Vector lVect = lCtrl.ExRicercaMisuraCautelareBdmc(lMisMod);

			if (lVect.size() == 0) {
				this.setRequestAttribute(ISIAPCostantiWeb.MESSAGE_TEXT, "Nessun dato presente");
				return ISIAPCostantiWeb.PG_MESSAGE;
			}

			setRequestAttribute("misuracautelarebdmc", lVect);
			setRequestAttribute("tipo_ricerca", tipo_ricerca);

			lReturnPage = PG_RICERCAMISURACAUTELAREBDMC;
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
			IMisuraCautelareBdmc lCtrl = SIEPLookupRemote.getMisuraCautelareBdmcRemote();
			Vector lVect = lCtrl.ExRicercaMisuraCautelareBdmcPaged(lMisMod, Integer.parseInt(lPagina));

			if (lVect.size() == 0) {
				this.setRequestAttribute(ISIAPCostantiWeb.MESSAGE_TEXT, "Nessun dato presente");
				return ISIAPCostantiWeb.PG_MESSAGE;
			}

			// ======================================================================
			// Recupero il numero di record totali della ricerca utilizzato per
			// calcolare il numero totale di pagine necessarie a visualizzare i dati
			// ======================================================================
			if (isRequestParameterNullObj("CountRisultati"))
				CountRisultati = lCtrl.ExGetCountMisuraCautelareBdmc(lMisMod).toString();
			else
				CountRisultati = getRequestStringParameter("CountRisultati");

			if (Integer.parseInt(CountRisultati) == 1) {
				// Nel caso di un solo record visualizzo direttamente il dettaglio...
				lMisMod = new MisuraCautelareBdmcModel((MisuraCautelareBdmcModel) lVect.firstElement());
				setRequestAttribute("misuracautelarebdmc", lMisMod);
				setFunctionsAvailableToRequest("siap.siep.misuracautelarebdmc.action.ActLoadDettaglioMisuraCautelareBdmc");

				lReturnPage = PG_LOAD_DETTAGLIOMISURACAUTELAREBDMC;
			} else {
				// ...altrimenti la pagina con i risultati
				setRequestAttribute("CountRisultati", CountRisultati);
				setRequestAttribute(ISIAPCostantiWeb.NUM_PAGE, lPagina);
				setRequestAttribute(ISIAPCostantiWeb.REQUEST_FOR_PAGING, getCompleteRequestURL());
				setRequestAttribute("tipo_ricerca", tipo_ricerca);

				setRequestAttribute("misuracautelarebdmc", lVect);

				lReturnPage = PG_RICERCAMISURACAUTELAREBDMC;
			}
		} // fine if tipo_ricerca

		return lReturnPage;
	}

}