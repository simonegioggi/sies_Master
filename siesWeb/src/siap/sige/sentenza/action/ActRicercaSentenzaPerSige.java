package siap.sige.sentenza.action;

import java.math.BigDecimal;
import java.util.Vector;

import f3b.web.IWebConstants;
import siap.sico.security.action.ICostantiSecurity;
import siap.sico.utente.model.UtenteModel;
import siap.siep.sentenza.action.ICostantiSentenza;
import siap.siep.sentenza.model.SentenzaModel;
import siap.sige.sentenza.controller.IFasSigeSentenza;
import siap.sige.util.SIGELookupRemote;
import siap.sige.web.ActionSige;
import siap.web.ISIAPCostantiWeb;

/**
 * <p>
 * Title: ActRicercaSentenza
 * </p>
 * <p>
 * Description: Ricerca Titolo Esecutivo
 * </p>
 *
 * @version 1.0
 */
public class ActRicercaSentenzaPerSige extends ActionSige implements ICostantiSentenza {

	@SuppressWarnings("rawtypes")
	public String processRequest() throws Exception {

		setLinkRitorno();
		String lPagina = "1";
		String flagRicercaData = "N";
		if (!isRequestParameterNullObj(IWebConstants.NUM_PAGE))
			lPagina = getRequestStringParameter(IWebConstants.NUM_PAGE);
		SentenzaModel lSenMod = new SentenzaModel();
		lSenMod.setNumeroRegeGip("");
		lSenMod.setNumeroRegeDib("");
		lSenMod.setNumeroRegeCas("");
		lSenMod.setNumeroRegeCap("");
		lSenMod.setNumeroRegeCasap("");

		UtenteModel lUtenteMod = new UtenteModel(
				(UtenteModel) getSessionAttribute(ICostantiSecurity.SESSION_UTENTE_CONNESSO));
		// ricerca provvedimento per date
		if (!isRequestParameterNullObj(CAMPO_ANNO_DATA_PROVVEDIMENTO)
				&& !isRequestParameterNullObj(CAMPO_MESE_DATA_PROVVEDIMENTO)
				&& !isRequestParameterNullObj(CAMPO_GIORNO_DATA_PROVVEDIMENTO)) {
			lSenMod.setDataProvvedimento(getRequestDateParameter(CAMPO_ANNO_DATA_PROVVEDIMENTO,
					CAMPO_MESE_DATA_PROVVEDIMENTO, CAMPO_GIORNO_DATA_PROVVEDIMENTO));
		}

		if (!isRequestParameterNullObj(CAMPO_DA_ANNO_PROVVEDIMENTO)
				&& !isRequestParameterNullObj(CAMPO_DA_MESE_PROVVEDIMENTO)
				&& !isRequestParameterNullObj(CAMPO_DA_GIORNO_PROVVEDIMENTO)) {
			lSenMod.setDataProvvedimentoIniziale(getRequestDateParameter(CAMPO_DA_ANNO_PROVVEDIMENTO,
					CAMPO_DA_MESE_PROVVEDIMENTO, CAMPO_DA_GIORNO_PROVVEDIMENTO));
		}

		if (!isRequestParameterNullObj(CAMPO_A_ANNO_PROVVEDIMENTO)
				&& !isRequestParameterNullObj(CAMPO_A_MESE_PROVVEDIMENTO)
				&& !isRequestParameterNullObj(CAMPO_A_GIORNO_PROVVEDIMENTO)) {
			lSenMod.setDataProvvedimentoFinale(getRequestDateParameter(CAMPO_A_ANNO_PROVVEDIMENTO,
					CAMPO_A_MESE_PROVVEDIMENTO, CAMPO_A_GIORNO_PROVVEDIMENTO));
		}
		if (!isRequestParameterNullObj(CAMPO_DA_ANNO_IRREVOCABILITA)
				&& !isRequestParameterNullObj(CAMPO_DA_MESE_IRREVOCABILITA)
				&& !isRequestParameterNullObj(CAMPO_DA_GIORNO_IRREVOCABILITA)) {
			lSenMod.setDataIrrevocabilitaIniziale(getRequestDateParameter(CAMPO_DA_ANNO_IRREVOCABILITA,
					CAMPO_DA_MESE_IRREVOCABILITA, CAMPO_DA_GIORNO_IRREVOCABILITA));
		}
		if (!isRequestParameterNullObj(CAMPO_A_ANNO_IRREVOCABILITA)
				&& !isRequestParameterNullObj(CAMPO_A_MESE_IRREVOCABILITA)
				&& !isRequestParameterNullObj(CAMPO_A_GIORNO_IRREVOCABILITA)) {
			lSenMod.setDataIrrevocabilitaFinale(getRequestDateParameter(CAMPO_A_ANNO_IRREVOCABILITA,
					CAMPO_A_MESE_IRREVOCABILITA, CAMPO_A_GIORNO_IRREVOCABILITA));
		}
		if (!isRequestParameterNullObj(CAMPO_ANNO_DATA_IRREVOCABILITA)
				&& !isRequestParameterNullObj(CAMPO_MESE_DATA_IRREVOCABILITA)
				&& !isRequestParameterNullObj(CAMPO_GIORNO_DATA_IRREVOCABILITA)) {
			lSenMod.setDataIrrevocabilitaIniziale(getRequestDateParameter(CAMPO_ANNO_DATA_IRREVOCABILITA,
					CAMPO_MESE_DATA_IRREVOCABILITA, CAMPO_GIORNO_DATA_IRREVOCABILITA));
		}

		if (!isRequestParameterNullObj(CAMPO_NUMERO_SENTENZA))
			lSenMod.setNumeroSentenza(getRequestStringParameter(CAMPO_NUMERO_SENTENZA));

		if (!isRequestParameterNullObj(CAMPO_ANNO_SENTENZA))
			lSenMod.setAnnoSentenza(getRequestBigDecimalParameter(CAMPO_ANNO_SENTENZA));

		if (!isRequestParameterNullObj(CAMPO_NUMERO_REGISTRO_GENERALE)) {
			if (getRequestStringParameter("valore").equals("gip"))
				lSenMod.setNumeroRegeGip(getRequestStringParameter(CAMPO_NUMERO_REGISTRO_GENERALE));

			if (getRequestStringParameter("valore").equals("dib"))
				lSenMod.setNumeroRegeDib(getRequestStringParameter(CAMPO_NUMERO_REGISTRO_GENERALE));

			if (getRequestStringParameter("valore").equals("cas"))
				lSenMod.setNumeroRegeCas(getRequestStringParameter(CAMPO_NUMERO_REGISTRO_GENERALE));

			if (getRequestStringParameter("valore").equals("cap"))
				lSenMod.setNumeroRegeCap(getRequestStringParameter(CAMPO_NUMERO_REGISTRO_GENERALE));

			if (getRequestStringParameter("valore").equals("casap"))
				lSenMod.setNumeroRegeCasap(getRequestStringParameter(CAMPO_NUMERO_REGISTRO_GENERALE));

			// MEV_66: aggiunte quattro nuove proprietà
			if (getRequestStringParameter("valore").equals("gup"))
				lSenMod.setNumeroRegeGup(getRequestStringParameter(CAMPO_NUMERO_REGISTRO_GENERALE));

			if (getRequestStringParameter("valore").equals("capsm"))
				lSenMod.setNumeroRegeCapsm(getRequestStringParameter(CAMPO_NUMERO_REGISTRO_GENERALE));
		}

		if (!isRequestParameterNullObj(CAMPO_ANNO_REGISTRO_GENERALE)) {
			if (getRequestStringParameter("valore").equals("gip"))
				lSenMod.setAnnoRegeGip(getRequestBigDecimalParameter(CAMPO_ANNO_REGISTRO_GENERALE));

			if (getRequestStringParameter("valore").equals("dib"))
				lSenMod.setAnnoRegeDib(getRequestBigDecimalParameter(CAMPO_ANNO_REGISTRO_GENERALE));

			if (getRequestStringParameter("valore").equals("cas"))
				lSenMod.setAnnoRegeCas(getRequestBigDecimalParameter(CAMPO_ANNO_REGISTRO_GENERALE));

			if (getRequestStringParameter("valore").equals("cap"))
				lSenMod.setAnnoRegeCap(getRequestBigDecimalParameter(CAMPO_ANNO_REGISTRO_GENERALE));

			if (getRequestStringParameter("valore").equals("casap"))
				lSenMod.setAnnoRegeCasap(getRequestBigDecimalParameter(CAMPO_ANNO_REGISTRO_GENERALE));

			// MEV_66: aggiunte quattro nuove proprietà
			if (getRequestStringParameter("valore").equals("gup"))
				lSenMod.setAnnoRegeGup(getRequestBigDecimalParameter(CAMPO_ANNO_REGISTRO_GENERALE));

			if (getRequestStringParameter("valore").equals("capsm"))
				lSenMod.setAnnoRegeCapsm(getRequestBigDecimalParameter(CAMPO_ANNO_REGISTRO_GENERALE));
		}

		if (!isRequestParameterNullObj(CAMPO_NUMERO_REGE_PM))
			lSenMod.setNumeroRegePm(getRequestStringParameter(CAMPO_NUMERO_REGE_PM));

		if (!isRequestParameterNullObj(CAMPO_ANNO_REGE_PM))
			lSenMod.setAnnoRegePm(getRequestBigDecimalParameter(CAMPO_ANNO_REGE_PM));

		if (lSenMod.getDataIrrevocabilitaIniziale() != null || lSenMod.getDataIrrevocabilitaFinale() != null
				|| lSenMod.getDataIrrevocabilitaIniziale() != null) {
			flagRicercaData = "S";
		}

		if (!isRequestParameterNullObj(PARAMETRO_ORDINAMENTO)) // ma non dovrebbe essere mai nullo
			lSenMod.setCodOrdinamento(getRequestStringParameter(PARAMETRO_ORDINAMENTO));

		// [SG]: 20220627
		// SIGE Ricerche >> Titolo Esecutivo >> Anno/Numero Titolo Esecutivo >> Ricerca >> Azioni-Dettagli >>
		// Elenco Procedimenti SIGE (Vai)
		String lAmbitoRicerca;
		// Si imposta il codice ufficio se l'Ambito di ricerca è l'ufficio.
		try {
			lAmbitoRicerca = getRequestStringParameter(ICostantiFasSigeSentenza.CAMPO_AMBITO_RICERCA);
		} catch (Exception e) {
			lAmbitoRicerca = "U";
		}

		if (lAmbitoRicerca.equalsIgnoreCase("U"))
			lSenMod.setCodUfficioInserimento(lUtenteMod.getUfficioUtente().getCodUfficio());
		else
			lSenMod.setCodUfficioInserimento(null);

		IFasSigeSentenza lFSSCtrl = SIGELookupRemote.getFasSigeSentenzaRemote();
		// 20181031: aggiunto parametro di passaggio
		Vector lSentenze = lFSSCtrl.ExRicercaSentenzePerSIGEPaged(lSenMod, Integer.parseInt(lPagina),
				checkMinori());

		// imposta la risposta la risposta nella request
		String lReturnPage = "";

		// Si passano i parametri del Vettore di Elementi individuati.
		setRequestAttribute("sentenzeSige", lSentenze);

		// Settaggio dei criteri di ricerca.
		setRequestAttribute("senMod", lSenMod);
		setRequestAttribute(ICostantiFasSigeSentenza.CAMPO_AMBITO_RICERCA, lAmbitoRicerca);
		// MEV_57 MODIFICA DEL 05/11/2018
		setRequestAttribute("ambitoRicerca", lAmbitoRicerca);

		BigDecimal CountRisultati;
		if (isRequestParameterNullObj("CountRisultati"))
			CountRisultati = lFSSCtrl.ExGetCountFasSigeSentenze(lSenMod);
		else
			CountRisultati = getRequestBigDecimalParameter("CountRisultati");

		setRequestAttribute("CountRisultati", CountRisultati);
		setRequestAttribute(IWebConstants.NUM_PAGE, lPagina);
		setRequestAttribute("flagRicercaData", flagRicercaData);

		setRequestAttribute(IWebConstants.REQUEST_FOR_PAGING, getCompleteRequestURL());

		lReturnPage = ICostantiFasSigeSentenza.PG_RICERCA_FASSIGE_SENTENZE;

		String lAzione = "siap.siep.sentenza.action.ActRicercaSentenzaPerSige";
		setRequestAttribute(ISIAPCostantiWeb.CAMPO_AZIONE_CHIAMANTE, lAzione);

		return lReturnPage; // restituisce la jsp di VIEW
	}

}