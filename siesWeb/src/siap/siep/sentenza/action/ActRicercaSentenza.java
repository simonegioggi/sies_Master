package siap.siep.sentenza.action;

/**
 *
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

import java.math.BigDecimal;
import java.util.Vector;

import f3b.web.IWebConstants;
import siap.sico.security.action.ICostantiSecurity;
import siap.sico.utente.model.UtenteModel;
import siap.sico.web.ActionSiap;
import siap.siep.sentenza.controller.ISentenza;
import siap.siep.sentenza.model.SentenzaModel;
import siap.siep.util.SIEPLookupRemote;
import siap.web.ISIAPCostantiWeb;

/**
 * <p>
 * Title: ActRicercaSentenza
 * </p>
 * <p>
 * Description: Ricerca Titolo Esecutivo
 * </p>
 * <p>
 * Copyright: Copyright (c) 2004
 * </p>
 * <p>
 * Company: Bull Italia S.p.A.
 * </p>
 * not attributable 1.0
 */
public class ActRicercaSentenza extends ActionSiap implements ICostantiSentenza {

	@SuppressWarnings("rawtypes")
	public String processRequest() throws Exception {

		String lPagina = "1";
		String flagRicercaData = "N";
		if (!isRequestParameterNullObj(IWebConstants.NUM_PAGE))
			lPagina = getRequestStringParameter(IWebConstants.NUM_PAGE);
		SentenzaModel lSmod = new SentenzaModel();

		UtenteModel lUtenteMod = new UtenteModel(
				(UtenteModel) getSessionAttribute(ICostantiSecurity.SESSION_UTENTE_CONNESSO));
		// ricerca provvedimento per date
		if (!isRequestParameterNullObj(CAMPO_ANNO_DATA_PROVVEDIMENTO)
				&& !isRequestParameterNullObj(CAMPO_MESE_DATA_PROVVEDIMENTO)
				&& !isRequestParameterNullObj(CAMPO_GIORNO_DATA_PROVVEDIMENTO)) {
			lSmod.setDataProvvedimento(getRequestDateParameter(CAMPO_ANNO_DATA_PROVVEDIMENTO,
					CAMPO_MESE_DATA_PROVVEDIMENTO, CAMPO_GIORNO_DATA_PROVVEDIMENTO));
		}

		if (!isRequestParameterNullObj(CAMPO_DA_ANNO_PROVVEDIMENTO)
				&& !isRequestParameterNullObj(CAMPO_DA_MESE_PROVVEDIMENTO)
				&& !isRequestParameterNullObj(CAMPO_DA_GIORNO_PROVVEDIMENTO)) {
			lSmod.setDataProvvedimentoIniziale(getRequestDateParameter(CAMPO_DA_ANNO_PROVVEDIMENTO,
					CAMPO_DA_MESE_PROVVEDIMENTO, CAMPO_DA_GIORNO_PROVVEDIMENTO));
		}

		if (!isRequestParameterNullObj(CAMPO_A_ANNO_PROVVEDIMENTO)
				&& !isRequestParameterNullObj(CAMPO_A_MESE_PROVVEDIMENTO)
				&& !isRequestParameterNullObj(CAMPO_A_GIORNO_PROVVEDIMENTO)) {
			lSmod.setDataProvvedimentoFinale(getRequestDateParameter(CAMPO_A_ANNO_PROVVEDIMENTO,
					CAMPO_A_MESE_PROVVEDIMENTO, CAMPO_A_GIORNO_PROVVEDIMENTO));
		}
		/***********************
		 * modifica 26 marzo 04
		 ************************************************************/
		/*
		 * if (!isRequestParameterNullObj(CAMPO_DA_ANNO_IRREVOCABILITA) &&
		 * !isRequestParameterNullObj(CAMPO_DA_MESE_IRREVOCABILITA) &&
		 * !isRequestParameterNullObj(CAMPO_DA_GIORNO_IRREVOCABILITA)) {
		 * lSmod.setDataIrrevocabilitaIniziale(getRequestDateParameter(CAMPO_DA_ANNO_IRREVOCABILITA,
		 * CAMPO_DA_MESE_IRREVOCABILITA, CAMPO_DA_GIORNO_IRREVOCABILITA)); } if
		 * (!isRequestParameterNullObj(CAMPO_A_ANNO_IRREVOCABILITA) &&
		 * !isRequestParameterNullObj(CAMPO_A_MESE_IRREVOCABILITA) &&
		 * !isRequestParameterNullObj(CAMPO_A_GIORNO_IRREVOCABILITA)) {
		 * lSmod.setDataIrrevocabilitaFinale(getRequestDateParameter(CAMPO_A_ANNO_IRREVOCABILITA,
		 * CAMPO_A_MESE_IRREVOCABILITA, CAMPO_A_GIORNO_IRREVOCABILITA)); }
		 */
		/*
		 * if (!isRequestParameterNullObj(CAMPO_ANNO_DATA_IRREVOCABILITA) &&
		 * !isRequestParameterNullObj(CAMPO_MESE_DATA_IRREVOCABILITA) &&
		 * !isRequestParameterNullObj(CAMPO_GIORNO_DATA_IRREVOCABILITA)) {
		 * lSmod.setDataIrrevocabilita(getRequestDateParameter(CAMPO_ANNO_DATA_IRREVOCABILITA,
		 * CAMPO_MESE_DATA_IRREVOCABILITA, CAMPO_GIORNO_DATA_IRREVOCABILITA)); }
		 */

		if (!isRequestParameterNullObj(CAMPO_NUMERO_SENTENZA))
			lSmod.setNumeroSentenza(getRequestStringParameter(CAMPO_NUMERO_SENTENZA));

		if (!isRequestParameterNullObj(CAMPO_ANNO_SENTENZA))
			lSmod.setAnnoSentenza(getRequestBigDecimalParameter(CAMPO_ANNO_SENTENZA));

		if (!isRequestParameterNullObj(CAMPO_NUMERO_REGISTRO_GENERALE)) {
			if (getRequestStringParameter("valore").equals("gip"))
				lSmod.setNumeroRegeGip(getRequestStringParameter(CAMPO_NUMERO_REGISTRO_GENERALE));

			if (getRequestStringParameter("valore").equals("dib"))
				lSmod.setNumeroRegeDib(getRequestStringParameter(CAMPO_NUMERO_REGISTRO_GENERALE));

			if (getRequestStringParameter("valore").equals("cas"))
				lSmod.setNumeroRegeCas(getRequestStringParameter(CAMPO_NUMERO_REGISTRO_GENERALE));

			if (getRequestStringParameter("valore").equals("cap"))
				lSmod.setNumeroRegeCap(getRequestStringParameter(CAMPO_NUMERO_REGISTRO_GENERALE));

			if (getRequestStringParameter("valore").equals("casap"))
				lSmod.setNumeroRegeCasap(getRequestStringParameter(CAMPO_NUMERO_REGISTRO_GENERALE));

			// MEV_66: aggiunte quattro nuove proprietà
			if (getRequestStringParameter("valore").equals("gup"))
				lSmod.setNumeroRegeGup(getRequestStringParameter(CAMPO_NUMERO_REGISTRO_GENERALE));

			if (getRequestStringParameter("valore").equals("capsm"))
				lSmod.setNumeroRegeCapsm(getRequestStringParameter(CAMPO_NUMERO_REGISTRO_GENERALE));
		}

		if (!isRequestParameterNullObj(CAMPO_ANNO_REGISTRO_GENERALE)) {
			if (getRequestStringParameter("valore").equals("gip"))
				lSmod.setAnnoRegeGip(getRequestBigDecimalParameter(CAMPO_ANNO_REGISTRO_GENERALE));

			if (getRequestStringParameter("valore").equals("dib"))
				lSmod.setAnnoRegeDib(getRequestBigDecimalParameter(CAMPO_ANNO_REGISTRO_GENERALE));

			if (getRequestStringParameter("valore").equals("cas"))
				lSmod.setAnnoRegeCas(getRequestBigDecimalParameter(CAMPO_ANNO_REGISTRO_GENERALE));

			if (getRequestStringParameter("valore").equals("cap"))
				lSmod.setAnnoRegeCap(getRequestBigDecimalParameter(CAMPO_ANNO_REGISTRO_GENERALE));

			if (getRequestStringParameter("valore").equals("casap"))
				lSmod.setAnnoRegeCasap(getRequestBigDecimalParameter(CAMPO_ANNO_REGISTRO_GENERALE));

			// MEV_66: aggiunte quattro nuove proprietà
			if (getRequestStringParameter("valore").equals("gup"))
				lSmod.setAnnoRegeGup(getRequestBigDecimalParameter(CAMPO_ANNO_REGISTRO_GENERALE));

			if (getRequestStringParameter("valore").equals("capsm"))
				lSmod.setAnnoRegeCapsm(getRequestBigDecimalParameter(CAMPO_ANNO_REGISTRO_GENERALE));
		}

		if (!isRequestParameterNullObj(CAMPO_NUMERO_REGE_PM))
			lSmod.setNumeroRegePm(getRequestStringParameter(CAMPO_NUMERO_REGE_PM));

		if (!isRequestParameterNullObj(CAMPO_ANNO_REGE_PM))
			lSmod.setAnnoRegePm(getRequestBigDecimalParameter(CAMPO_ANNO_REGE_PM));

		/*
		 * if (lSmod.getDataIrrevocabilita() != null || lSmod.getDataIrrevocabilitaFinale() != null ||
		 * lSmod.getDataIrrevocabilitaIniziale() != null) { flagRicercaData = "S"; }
		 */

		if (!isRequestParameterNullObj(PARAMETRO_ORDINAMENTO)) // ma non dovrebbe essere mai nullo
			lSmod.setCodOrdinamento(getRequestStringParameter(PARAMETRO_ORDINAMENTO));

		// Riempie il model
		lSmod.setCodUfficioInserimento(lUtenteMod.getUfficioUtente().getCodUfficio());

		ISentenza lSCtrl = SIEPLookupRemote.getSentenzaRemote();

		// Paolo Cherubini 12/04/2011 inserisco la ricerca gia usata nella nuova istanza che riporta prima le
		// sentenze
		// e poi i procedimenti collegati commento la if per risultato = 1 e modifico la setRequestAttribute
		// Vector lSentenze = lSCtrl.ExRicercaSentenzaPaged(lSmod, Integer.parseInt(lPagina));
		Vector lSentenzeFascicoli = lSCtrl.ExRicercaSentenzaFascicoliPaged(lSmod, Integer.parseInt(lPagina));

		// imposta la risposta la risposta nella request
		String lReturnPage = "";

		// if (lSentenze.size() == 1)
		// {
		// lReturnPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD +
		// "=siap.siep.sentenza.action.ActLoadDettaglioSentenza&" + CAMPO_ID_SENTENZA + "=" +
		// ((SentenzaModel) lSentenze.get(0)).getIdSentenza().toString();
		// }
		// else
		// {
		// setta la risposta nella request
		// setRequestAttribute("sentenze", lSentenze);

		BigDecimal CountRisultati;
		if (isRequestParameterNullObj("CountRisultati")) {
			CountRisultati = lSCtrl.ExGetCountSentenze(lSmod);
		} else
			CountRisultati = getRequestBigDecimalParameter("CountRisultati");

		setRequestAttribute("CountRisultati", CountRisultati);
		setRequestAttribute(IWebConstants.NUM_PAGE, lPagina);
		setRequestAttribute("flagRicercaData", flagRicercaData);

		setRequestAttribute(IWebConstants.REQUEST_FOR_PAGING, getCompleteRequestURL());

		// setRequestAttribute("sentenze", lSentenze);
		setRequestAttribute("sentenze", lSentenzeFascicoli);
		lReturnPage = PG_RICERCASENTENZE;

		String lAzione = "siap.siep.sentenza.action.ActRicercaSentenza";
		setRequestAttribute(ISIAPCostantiWeb.CAMPO_AZIONE_CHIAMANTE, lAzione);
		// }

		return lReturnPage; // restituisce la jsp di VIEW
	}

}