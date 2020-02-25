package siap.siep.sentenza.action;

import java.util.Vector;

import f3b.util.DateUtils;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;
import siap.sico.security.action.ICostantiSecurity;
import siap.sico.utente.model.UtenteModel;
import siap.sico.web.ActionSiap;
import siap.siep.SIEPException;
import siap.siep.misurasicurezza.action.ICostantiMisuraSicurezza;
import siap.siep.sentenza.controller.ISentenza;
import siap.siep.sentenza.model.SentenzaModel;
import siap.siep.util.SIEPLookupRemote;
import siap.web.ISIAPCostantiWeb;

/**
 * <p>
 * Title : ActModificaDatiProvvedimentoMSFuoriSent
 * </p>
 * <p>
 * Description: Modifica dati del decreto/ordinanza relativo a un fascicolo
 * </p>
 * <p>
 * di Misura Sicurezza disposta Fuori Sentenza o Provvisoria
 * </p>
 * <p>
 * Copyright: Copyright (c) 2015
 * </p>
 * <p>
 * Company: Intersistemi Italia s.p.a.
 * </p>
 * 
 * @version 8.3
 */
public class ActModificaDatiProvvedimentoMSFuoriSent extends ActionSiap implements ICostantiSentenza {

	public String processRequest() throws Exception {

		SentenzaModel lSenMod = new SentenzaModel();

		UtenteModel lUtenteMod = new UtenteModel(
				(UtenteModel) getSessionAttribute(ICostantiSecurity.SESSION_UTENTE_CONNESSO));
		lSenMod.setDescrUfficioInserimento(lUtenteMod.getUfficioUtente().getDescrTipoUfficio() + " di "
				+ lUtenteMod.getUfficioUtente().getDescrComune());

		if (!isRequestParameterNullObj(CAMPO_ANNO_REGE_PM)) {
			lSenMod.setNumeroRegePm(getRequestStringParameter(CAMPO_NUMERO_REGE_PM));
			lSenMod.setAnnoRegePm(getRequestBigDecimalParameter(CAMPO_ANNO_REGE_PM));
		}

		if (!isRequestParameterNullObj("TipoRG")) {
			if (getRequestStringParameter("TipoRG").equalsIgnoreCase("gip")) {
				lSenMod.setAnnoRegeGip(getRequestBigDecimalParameter("ARG"));
				lSenMod.setNumeroRegeGip(getRequestStringParameter("NRG"));
			}
			if (getRequestStringParameter("TipoRG").equalsIgnoreCase("dib")) {
				lSenMod.setAnnoRegeDib(getRequestBigDecimalParameter("ARG"));
				lSenMod.setNumeroRegeDib(getRequestStringParameter("NRG"));
			}
			if (getRequestStringParameter("TipoRG").equalsIgnoreCase("cas")) {
				lSenMod.setAnnoRegeCas(getRequestBigDecimalParameter("ARG"));
				lSenMod.setNumeroRegeCas(getRequestStringParameter("NRG"));
			}
			if (getRequestStringParameter("TipoRG").equalsIgnoreCase("cap")) {
				lSenMod.setAnnoRegeCap(getRequestBigDecimalParameter("ARG"));
				lSenMod.setNumeroRegeCap(getRequestStringParameter("NRG"));
			}
			if (getRequestStringParameter("TipoRG").equalsIgnoreCase("casap")) {
				lSenMod.setAnnoRegeCasap(getRequestBigDecimalParameter("ARG"));
				lSenMod.setNumeroRegeCasap(getRequestStringParameter("NRG"));
			}
			// MEV_66: aggiunte quattro nuove proprietà
			if (getRequestStringParameter("TipoRG").equalsIgnoreCase("gup")) {
				lSenMod.setAnnoRegeGup(getRequestBigDecimalParameter("ARG"));
				lSenMod.setNumeroRegeGup(getRequestStringParameter("NRG"));
			}
			if (getRequestStringParameter("TipoRG").equalsIgnoreCase("capsm")) {
				lSenMod.setAnnoRegeCapsm(getRequestBigDecimalParameter("ARG"));
				lSenMod.setNumeroRegeCapsm(getRequestStringParameter("NRG"));
			}
		}

		if (!isRequestParameterNullObj(CAMPO_SEDE_NOTIZIA_REATO)) {
			String lCodSedeNotizia = getCodComuneByDescr(getRequestStringParameter(CAMPO_SEDE_NOTIZIA_REATO))
					.getCodComune();
			lSenMod.setCodSedeNotiziaReato(lCodSedeNotizia);
		} else {
			lSenMod.setCodSedeNotiziaReato("-");
		}

		if (!isRequestParameterNullObj(CAMPO_ANNO_DATA_PROVVEDIMENTO))
			lSenMod.setDataProvvedimento(getRequestDateParameter(CAMPO_ANNO_DATA_PROVVEDIMENTO,
					CAMPO_MESE_DATA_PROVVEDIMENTO, CAMPO_GIORNO_DATA_PROVVEDIMENTO));

		lSenMod.setNumeroSentenza(getRequestStringParameter(CAMPO_NUMERO_SENTENZA));
		lSenMod.setAnnoSentenza(getRequestBigDecimalParameter(CAMPO_ANNO_SENTENZA));

		// NON si tratta di Sentenza
		if (getRequestStringParameter(CAMPO_COD_TIPO_PROVV_RIF).equals("02")) {
			lSenMod.setCodTipoProvvedimento("02"); // Decreto
		} else {
			lSenMod.setCodTipoProvvedimento("03"); // Ordinanza
		}
		// -------------------------------------------------------------------------------------

		String lCodTipo = getRequestStringParameter(CAMPO_COD_TIPO_AUTORITA_EMITTENTE);
		String lCodSedeEmittente = getCodComuneByDescr(getRequestStringParameter(CAMPO_COD_LUOGO_EMITTENTE))
				.getCodComune();
		lSenMod.setCodLuogoEmittente(lCodSedeEmittente);
		lSenMod.setCodTipoAutoritaEmittente(lCodTipo);

		if (!isRequestParameterNullObj(CAMPO_NUM_SEZIONE_AUTORITA_EMITTENTE))
			lSenMod.setNumSezioneAutoritaEmittente(
					getRequestStringParameter(CAMPO_NUM_SEZIONE_AUTORITA_EMITTENTE));

		if (!isRequestParameterNullObj(CAMPO_COD_TIPO_RITO))
			lSenMod.setCodTipoRito(getRequestStringParameter(CAMPO_COD_TIPO_RITO));
		else
			lSenMod.setCodTipoRito("-");

		if (!isRequestParameterNullObj(CAMPO_NOTE) && !getRequestStringParameter(CAMPO_NOTE).equals("")) {
			lSenMod.setNote(getRequestStringParameter(CAMPO_NOTE));
		}

		// Per le join
		lSenMod.setCodBilanciamentoCircostanze("-");
		lSenMod.setCodTipoAutoritaProvvRif("-");
		lSenMod.setCodTipoProvvRif("-");
		lSenMod.setCodLuogoProvvRif("-");
		lSenMod.setCodTipoProvvedimentoRif("-");
		lSenMod.setCodTipoProvvedimentoAltro("-");
		lSenMod.setCodTipoDecisioneCassazione("-");

		lSenMod.setCodOperatoreAggiornamento(getCodUtenteConnesso());
		lSenMod.setDataAggiornamento(DateUtils.getSysDate());
		lSenMod.setCodUfficioAggiornamento(getCodUfficioUtenteConnesso());

		lSenMod.setIdSentenza(getRequestBigDecimalParameter(CAMPO_ID_SENTENZA));

		// --> Controllo Sentenza esistente
		String lMess = "";
		lMess = getControlloSentenzaEsistente(lSenMod);

		/* Se esiste un provvedimento doppio viene segnalato prima della Insert nel DB */

		String lAzione = "siap.siep.sentenza.action.ActModificaDatiProvvedimentoMSFuoriSent";
		String lPage = "";

		if (!lMess.equals("")) {
			if (isRequestParameterNullObj(ICostantiMisuraSicurezza.CAMPO_CK_WARNING_MIS)
					|| getRequestStringParameter(ICostantiMisuraSicurezza.CAMPO_CK_WARNING_MIS) == null) {
				// Se nella form di Warning hai selezionato "OK" NON entra più qui, perchè
				// CAMPO_CK_WARNING_MIS = "S"
				RedirectTo lRedirigi = new RedirectTo();
				lRedirigi.setPage(IWebConstants.PG_MAIN);
				lRedirigi.setAction(lAzione);

				setRequestAttribute(IWebConstants.MESSAGE_TEXT, lMess);
				setRequestAttribute(ISIAPCostantiWeb.CAMPO_AZIONE_CHIAMANTE, "" + getClass().getName());
				setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi);

				return ICostantiMisuraSicurezza.PG_WARNING_MIS_SIC_FUORI_SENT;
			}
		}
		SentenzaModel lSen = new SentenzaModel();
		ISentenza lSCtrl = SIEPLookupRemote.getSentenzaRemote();

		lSen = lSCtrl.ExModificaSentenza(lSenMod);

		lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
				+ "=siap.siep.sentenza.action.ActLoadDettaglioDatiProvvedimentoMSFuoriSent";
		lPage += "&" + CAMPO_ID_SENTENZA + "=" + lSen.getIdSentenza().toString();

		return lPage;
	} // Chiude processRequest()

	@SuppressWarnings("rawtypes")
	private String getControlloSentenzaEsistente(SentenzaModel lSenMod) throws Exception {

		SentenzaModel lRis = null;
		ISentenza lSCtrl = SIEPLookupRemote.getSentenzaRemote();
		Vector lRisRic = null;
		String lEsiste = "";

		try {
			lRisRic = lSCtrl.ExRicercaSentenzaDuplicata(lSenMod);
		} catch (SIEPException e) {
			e.printStackTrace();
		}

		if (lRisRic != null && lRisRic.size() > 0) {
			lRis = (SentenzaModel) lRisRic.get(0);
			String lDescrizioneProvv = "";
			if (lRis != null && lRis.getCodTipoProvvedimento() != null) {
				if (lRis.getCodTipoProvvedimento().equals("03"))
					lDescrizioneProvv = "L'ordinanza ";
				else if (lRis.getCodTipoProvvedimento().equals("02"))
					lDescrizioneProvv = "Il decreto ";
				else
					lDescrizioneProvv = "Il provvedimento ";

				lEsiste += lDescrizioneProvv + lRis.getAnnoSentenza() + "/" + lRis.getNumeroSentenza() + " - "
						+ lRis.getDescrTipoAutoritaEmittente() + " <br>di " + lRis.getDescrLuogoEmittente()
						+ " è già presente in archivio.";
			}
		}

		return lEsiste;
	} // CHIUDE getControlloSentenzaEsistente()

} // Chiude Classe