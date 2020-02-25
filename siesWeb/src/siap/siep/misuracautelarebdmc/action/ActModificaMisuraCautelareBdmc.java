package siap.siep.misuracautelarebdmc.action;

import siap.sico.web.ActionSiap;
import siap.siep.misuracautelarebdmc.controller.IMisuraCautelareBdmc;
import siap.siep.misuracautelarebdmc.model.MisuraCautelareBdmcModel;
import siap.siep.util.SIEPLookupRemote;
import siap.web.ISIAPCostantiWeb;
import f3b.util.F3BException;

/**
 * <p>
 * Title: ActModificaMisuraCautelareBdmc
 * </p>
 * <p>
 * Description: Classe Action per la modifica di MisuraCautelareBdmc
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
public class ActModificaMisuraCautelareBdmc extends ActionSiap implements ICostantiMisuraCautelareBdmc {

	/*****************************************************************************
	 * Azione di Modifica del MisuraCautelareBdmc
	 * 
	 * @return Nome della pagina JSP da visualizzare al termine dell'elaborazione
	 * @throws F3BException
	 *****************************************************************************/
	public String processRequest() throws F3BException {

		// =====================================================
		// Riempie il model con i campi recuperati dalla form
		// n.b. per i campi del model non valorizzati, i corrispondenti
		// campi della tabella verranno impostati a null
		// Il campo chiave è obbligatorio perchè utilizzato nelle clausola where
		// per individuare il record da aggiornare
		// =====================================================
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

		// ===================================================
		// Recupera il controller ed effettua la modifica
		// ===================================================
		IMisuraCautelareBdmc lCtrl = SIEPLookupRemote.getMisuraCautelareBdmcRemote();
		lCtrl.ExModificaMisuraCautelareBdmc(lMisMod, null);

		// ======================================================================
		// Prepara la pagina di destinazione
		// Viene restituita la pagina di dettaglio con i dati appena inseriti
		// ======================================================================
		String lPage = "";
		lPage = ISIAPCostantiWeb.PG_MAIN + "?" + ISIAPCostantiWeb.ACTION_FIELD
				+ "=siap.siep.misuracautelarebdmc.action.ActLoadDettaglioMisuraCautelareBdmc";
		lPage += "&" + CAMPO_ID_MISURA_CAUTELARE + "=" + lMisMod.getIdMisuraCautelare().toString();

		return lPage;
	}

}