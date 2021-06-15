package siap.siep.sentenza.action;

import java.math.BigDecimal;
import java.util.Vector;

import siap.sico.decodifiche.action.ICostantiComune;
import siap.sico.decodifiche.model.ComuneModel;
import siap.sico.security.action.ICostantiSecurity;
import siap.sico.soggetto.action.ICostantiSoggetto;
import siap.sico.soggetto.model.SoggettoModel;
import siap.sico.utente.model.UtenteModel;
import siap.sico.web.ActionSiap;
import siap.siep.sentenza.controller.ISentenza;
import siap.siep.sentenza.model.SentenzaModel;
import siap.siep.sentenza.model.SentenzaSoggettoFascicoloModel;
import siap.siep.util.SIEPLookupRemote;
import siap.web.ISIAPCostantiWeb;
import f3b.web.IWebConstants;

/**
 * 
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
public class ActRicercaSentenzaSoggetto extends ActionSiap implements ICostantiSentenza, ICostantiSoggetto {

	@SuppressWarnings("rawtypes")
	public String processRequest() throws Exception {

		String lPagina = "1";
		// String flagRicercaData = "N";
		if (!isRequestParameterNullObj(IWebConstants.NUM_PAGE))
			lPagina = getRequestStringParameter(IWebConstants.NUM_PAGE);
		SentenzaModel lSmod = new SentenzaModel();

		UtenteModel lUtenteMod = new UtenteModel(
				(UtenteModel) getSessionAttribute(ICostantiSecurity.SESSION_UTENTE_CONNESSO));
		// ricerca provvedimento per date
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

		// Riempie i model del Soggetto
		SoggettoModel lSogmod = new SoggettoModel();
		// if (!isRequestParameterNullObj(ICostantiSoggetto.CAMPO_COGNOME))
		lSogmod.setCognome(getRequestStringParameter(ICostantiSoggetto.CAMPO_COGNOME).toUpperCase());
		// if (!isRequestParameterNullObj(ICostantiSoggetto.CAMPO_NOME))
		lSogmod.setNome(getRequestStringParameter(ICostantiSoggetto.CAMPO_NOME).toUpperCase());

		if (!isRequestParameterNullObj(ICostantiSoggetto.CAMPO_ANNO_DATA_NASCITA)
				&& getRequestStringParameter(ICostantiSoggetto.CAMPO_ANNO_DATA_NASCITA).length() > 2)
			lSogmod.setDataNascita(getRequestDateParameter(ICostantiSoggetto.CAMPO_ANNO_DATA_NASCITA,
					ICostantiSoggetto.CAMPO_MESE_DATA_NASCITA, ICostantiSoggetto.CAMPO_GIORNO_DATA_NASCITA));

		if (!isRequestParameterNullObj(ICostantiSoggetto.CAMPO_COD_COMUNE_NASCITA)
				&& getRequestStringParameter(ICostantiSoggetto.CAMPO_COD_COMUNE_NASCITA).length() > 1) {

			// Recupero dati del Comune di nascita
			ComuneModel lComMod;
			if (!isRequestParameterNullObj(ICostantiComune.CAMPO_COD_COMUNE_REALE)
					&& getRequestStringParameter(ICostantiComune.CAMPO_COD_COMUNE_REALE).length() > 0) {
				// se presente dal codice comune (e descrizione)
				// 20210524	MEV_Scheda-21 Correzione Comune Nascita per omonimie dei Comuni senza flag validità.
				//lComMod = new ComuneModel(getDatiComuneByCodDescrFlagVal(
				lComMod = new ComuneModel(getDatiComuneByCodDescr(
						getRequestStringParameter(ICostantiComune.CAMPO_COD_COMUNE_REALE),
						getRequestStringParameter(ICostantiSoggetto.CAMPO_COD_COMUNE_NASCITA)));
			} else {
				// altrimenti dalla sola descrizione (rischio omonimi)
				lComMod = new ComuneModel(
						// 20210524	MEV_Scheda-21 Correzione Comune Nascita per omonimie dei Comuni senza flag validità.
						//getDatiComuneByDescrOmonimiaFlagVal(getRequestStringParameter(ICostantiSoggetto.CAMPO_COD_COMUNE_NASCITA)));
						getDatiComuneByDescrOmonimia(getRequestStringParameter(ICostantiSoggetto.CAMPO_COD_COMUNE_NASCITA)));
			}

			lSogmod.setCodComuneNascita(lComMod.getCodComune());
		}

		lSmod.setCodUfficioInserimento(lUtenteMod.getUfficioUtente().getCodUfficio());
		lSogmod.setCodUfficioInserimento(lUtenteMod.getUfficioUtente().getCodUfficio());

		ISentenza lSCtrl = SIEPLookupRemote.getSentenzaRemote();

		Vector lSentenze = lSCtrl.ExRicercaSentenzeSoggettoPaged(lSmod, lSogmod, Integer.parseInt(lPagina));

		// imposta la risposta la risposta nella request
		String lReturnPage = "";

		if (lSentenze.size() == 1) {
			lReturnPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
					+ "=siap.siep.sentenza.action.ActLoadDettaglioSentenza&" + CAMPO_ID_SENTENZA + "="
					+ ((SentenzaSoggettoFascicoloModel) lSentenze.get(0)).getIdSentenza().toString();
		} else {
			// setta la risposta nella request

			BigDecimal CountRisultati;
			if (isRequestParameterNullObj("CountRisultati")) {
				CountRisultati = lSCtrl.ExGetCountSentenzeSoggetto(lSogmod, lSmod);
				// CountRisultati=new BigDecimal(lSentenze.size());
			} else
				CountRisultati = getRequestBigDecimalParameter("CountRisultati");

			setRequestAttribute("CountRisultati", CountRisultati);
			setRequestAttribute(IWebConstants.NUM_PAGE, lPagina);
			// setRequestAttribute("flagRicercaData", flagRicercaData);

			setRequestAttribute(IWebConstants.REQUEST_FOR_PAGING, getCompleteRequestURL());

			setRequestAttribute("soggetto", lSogmod);
			setRequestAttribute("sentenze", lSentenze);
			lReturnPage = PG_RICERCASENTENZESOGGETTO;

			String lAzione = "siap.siep.sentenza.action.ActRicercaSentenzaSoggetto";
			setRequestAttribute(ISIAPCostantiWeb.CAMPO_AZIONE_CHIAMANTE, lAzione);
		}

		return lReturnPage; // restituisce la jsp di VIEW
	}

}