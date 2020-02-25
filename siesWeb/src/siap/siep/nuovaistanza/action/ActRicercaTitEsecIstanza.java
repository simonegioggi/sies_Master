package siap.siep.nuovaistanza.action;

import java.math.BigDecimal;
import java.util.Vector;

import f3b.web.IWebConstants;
import f3b.web.RedirectTo;
import siap.sico.decodifiche.model.ComuneModel;
import siap.sico.security.action.ICostantiSecurity;
import siap.sico.utente.model.UtenteModel;
import siap.sico.web.ActionSiap;
import siap.siep.sentenza.action.ICostantiSentenza;
import siap.siep.sentenza.controller.ISentenza;
import siap.siep.sentenza.model.SentenzaModel;
import siap.siep.util.SIEPLookupRemote;

/**
 * <p>
 * Title: ActRicercaTitEsecIstanza
 * </p>
 * <p>
 * Description: Ricerca Titolo Esecutivo per iIstanza
 * </p>
 * <p>
 * Copyright: Copyright (c) 2009
 * </p>
 * <p>
 * Company: Agile.
 * </p>
 * 5.0
 */
public class ActRicercaTitEsecIstanza extends ActionSiap implements ICostantiSentenza, ICostantiNuovaIstanza {

	@SuppressWarnings("rawtypes")
	public String processRequest() throws Exception {

		// Attivazione punto di Ritorno
		setLinkRitorno();

		SentenzaModel lSmod = new SentenzaModel();

		UtenteModel lUtenteMod = new UtenteModel(
				(UtenteModel) getSessionAttribute(ICostantiSecurity.SESSION_UTENTE_CONNESSO));
		// ricerca provvedimento per date
		if (!isRequestParameterNullObj(CAMPO_ANNO_DATA_PROVVEDIMENTO)
				&& !isRequestParameterNullObj(CAMPO_MESE_DATA_PROVVEDIMENTO)
				&& !isRequestParameterNullObj(CAMPO_GIORNO_DATA_PROVVEDIMENTO)
				&& getRequestStringParameter(CAMPO_ANNO_DATA_PROVVEDIMENTO) != null
				&& !getRequestStringParameter(CAMPO_ANNO_DATA_PROVVEDIMENTO).equals("")) {
			lSmod.setDataProvvedimento(getRequestDateParameter(CAMPO_ANNO_DATA_PROVVEDIMENTO,
					CAMPO_MESE_DATA_PROVVEDIMENTO, CAMPO_GIORNO_DATA_PROVVEDIMENTO));
		}

		if (!isRequestParameterNullObj(CAMPO_COD_LUOGO_EMITTENTE)
				&& getRequestStringParameter(CAMPO_COD_LUOGO_EMITTENTE) != null
				&& !getRequestStringParameter(CAMPO_COD_LUOGO_EMITTENTE).equals("")) {
			ComuneModel lComMod = getCodComuneByDescr(getRequestStringParameter(CAMPO_COD_LUOGO_EMITTENTE));
			lSmod.setCodLuogoEmittente(lComMod.getCodComune());

		}

		if (!isRequestParameterNullObj(CAMPO_COD_TIPO_AUTORITA_EMITTENTE)
				&& getRequestStringParameter(CAMPO_COD_TIPO_AUTORITA_EMITTENTE) != null
				&& !getRequestStringParameter(CAMPO_COD_TIPO_AUTORITA_EMITTENTE).equals("")
				&& !getRequestStringParameter(CAMPO_COD_TIPO_AUTORITA_EMITTENTE).equals("-")) {
			lSmod.setCodTipoAutoritaEmittente(getRequestStringParameter(CAMPO_COD_TIPO_AUTORITA_EMITTENTE));
		}

		if (!isRequestParameterNullObj(CAMPO_NUMERO_SENTENZA)
				&& getRequestStringParameter(CAMPO_NUMERO_SENTENZA) != null
				&& !getRequestStringParameter(CAMPO_NUMERO_SENTENZA).equals(""))
			lSmod.setNumeroSentenza(getRequestStringParameter(CAMPO_NUMERO_SENTENZA));

		if (!isRequestParameterNullObj(CAMPO_ANNO_SENTENZA)
				&& getRequestStringParameter(CAMPO_ANNO_SENTENZA) != null
				&& !getRequestStringParameter(CAMPO_ANNO_SENTENZA).equals(""))
			lSmod.setAnnoSentenza(getRequestBigDecimalParameter(CAMPO_ANNO_SENTENZA));

		if (!isRequestParameterNullObj(CAMPO_NUMERO_REGISTRO_GENERALE)
				&& getRequestStringParameter(CAMPO_NUMERO_REGISTRO_GENERALE) != null
				&& !getRequestStringParameter(CAMPO_NUMERO_REGISTRO_GENERALE).equals("")) {
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

		if (!isRequestParameterNullObj(CAMPO_ANNO_REGISTRO_GENERALE)
				&& getRequestStringParameter(CAMPO_ANNO_REGISTRO_GENERALE) != null
				&& !getRequestStringParameter(CAMPO_ANNO_REGISTRO_GENERALE).equals("")) {
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

		if (!isRequestParameterNullObj(CAMPO_NUMERO_REGE_PM)
				&& getRequestStringParameter(CAMPO_NUMERO_REGE_PM) != null
				&& !getRequestStringParameter(CAMPO_NUMERO_REGE_PM).equals(""))
			lSmod.setNumeroRegePm(getRequestStringParameter(CAMPO_NUMERO_REGE_PM));

		if (!isRequestParameterNullObj(CAMPO_ANNO_REGE_PM)
				&& getRequestStringParameter(CAMPO_ANNO_REGE_PM) != null
				&& !getRequestStringParameter(CAMPO_ANNO_REGE_PM).equals(""))
			lSmod.setAnnoRegePm(getRequestBigDecimalParameter(CAMPO_ANNO_REGE_PM));

		lSmod.setCodUfficioInserimento(lUtenteMod.getUfficioUtente().getCodUfficio());

		String lPagina = "1";
		if (!isRequestParameterNullObj(IWebConstants.NUM_PAGE))
			lPagina = getRequestStringParameter(IWebConstants.NUM_PAGE);

		String lRetPage = PG_RICERCA_TIT_ESEC_ISTANZA;

		ISentenza lSCtrl = SIEPLookupRemote.getSentenzaRemote();

		try {
			Vector lSentenzeFascicoli = lSCtrl.ExRicercaSentenzaFascicoliPaged(lSmod,
					Integer.parseInt(lPagina));

			if (lSentenzeFascicoli.size() == 0) {
				lRetPage = PG_LOAD_INSERISCI_TIT_ESEC_ISTANZA;
			} else {
				BigDecimal CountRisultati;
				if (isRequestParameterNullObj("CountRisultati")) {
					CountRisultati = lSCtrl.ExGetCountSentenze(lSmod);
				} else
					CountRisultati = getRequestBigDecimalParameter("CountRisultati");

				setRequestAttribute("CountRisultati", CountRisultati);
				setRequestAttribute(IWebConstants.NUM_PAGE, lPagina);
				setRequestAttribute(IWebConstants.REQUEST_FOR_PAGING, getCompleteRequestURL());

				setRequestAttribute("sentenzefascicoli", lSentenzeFascicoli);

			}
		} catch (Exception Ex) {
			setRequestAttribute(IWebConstants.MESSAGE_TEXT, Ex.getMessage());

			// Prepara la "pagina" di destinAction
			RedirectTo lRedirigi = new RedirectTo();
			lRedirigi.setPage(IWebConstants.PG_MAIN);
			lRedirigi.setAction("siap.siep.nuovaistanza.action.ActLoadInserisciIstanzaPerTitoloEsecutivo");

			setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi);

			return IWebConstants.PG_MESSAGE;
		}

		return lRetPage; // restituisce la jsp di VIEW
	}

}