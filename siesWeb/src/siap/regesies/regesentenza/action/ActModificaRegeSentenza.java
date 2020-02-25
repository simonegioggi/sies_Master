package siap.regesies.regesentenza.action;

import siap.regesies.action.ActionRegeSiap;
import siap.regesies.regesentenza.controller.IRegeSentenza;
import siap.regesies.regesentenza.model.RegeSentenzaModel;
import siap.regesies.util.RegeSiesLookupRemote;
import siap.sico.decodifiche.model.ComuneModel;
import siap.sico.security.action.ICostantiSecurity;
import siap.sico.utente.model.UtenteModel;
import f3b.util.DateUtils;
import f3b.util.F3BException;

/**
 * <p>
 * Title: ActModificaSentenza
 * </p>
 * <p>
 * Description: Classe Action per la modifica di Sentenza
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

public class ActModificaRegeSentenza extends ActionRegeSiap implements ICostantiRegeSentenza {

	/**
	 * Azione di Modifica del Sentenza
	 * 
	 * @return Nome della pagina JSP da visualizzare al termine dell'elaborazione
	 * @throws F3BException
	 */
	public String processRequest() throws Exception {

		String lId = getRequestStringParameter(CAMPO_ID_FILE);

		RegeSentenzaModel lSenMod = new RegeSentenzaModel();
		lSenMod.setIdFile(lId);
		lSenMod.setCodTipoProvvedimento("01");

		UtenteModel lUtenteMod = new UtenteModel(
				(UtenteModel) getSessionAttribute(ICostantiSecurity.SESSION_UTENTE_CONNESSO));

		lSenMod.setCodOperatoreAggiornamento(lUtenteMod.getUserId());
		lSenMod.setDataAggiornamento(DateUtils.getSysDate());
		lSenMod.setCodUfficioAggiornamento(lUtenteMod.getUfficioUtente().getCodUfficio());
		lSenMod.setDescrUfficioInserimento(lUtenteMod.getUfficioUtente().getDescrTipoUfficio());

		lSenMod.setAnnoRegePm(getRequestIntParameter(CAMPO_ANNO_REGE_PM));
		lSenMod.setNumeroRegePm(getRequestStringParameter(CAMPO_NUMERO_REGE_PM));
		lSenMod.setDataArrivoAtto(getRequestDateParameter(CAMPO_ANNO_DATA_ARRIVO_ATTO,
				CAMPO_MESE_DATA_ARRIVO_ATTO, CAMPO_GIORNO_DATA_ARRIVO_ATTO));
		lSenMod.setDataIscrizione(getRequestDateParameter(CAMPO_ANNO_DATA_ISCRIZIONE,
				CAMPO_MESE_DATA_ISCRIZIONE, CAMPO_GIORNO_DATA_ISCRIZIONE));

		lSenMod.setDataProvvedimento(getRequestDateParameter(CAMPO_ANNO_DATA_PROVVEDIMENTO,
				CAMPO_MESE_DATA_PROVVEDIMENTO, CAMPO_GIORNO_DATA_PROVVEDIMENTO));
		lSenMod.setCodTipoAutoritaEmittente(getRequestStringParameter(CAMPO_COD_TIPO_AUTORITA_EMITTENTE));
		ComuneModel lComMod = new ComuneModel(
				getCodComuneByDescr(getRequestStringParameter(CAMPO_COD_LUOGO_EMITTENTE)));
		lSenMod.setCodLuogoEmittente(lComMod.getCodComune());

		/* String lCodice = */getCodUfficioByCodTipoUfficioDescrComune(
				getRequestStringParameter(CAMPO_COD_TIPO_AUTORITA_EMITTENTE),
				getRequestStringParameter(CAMPO_COD_LUOGO_EMITTENTE));

		lSenMod.setNumSezioneAutoritaEmittente(
				getRequestStringParameter(CAMPO_NUM_SEZIONE_AUTORITA_EMITTENTE));
		lSenMod.setAnnoSentenza(getRequestIntParameter(CAMPO_ANNO_SENTENZA));
		lSenMod.setNumeroSentenza(getRequestStringParameter(CAMPO_NUMERO_SENTENZA));
		lSenMod.setDataIrrevocabilita(getRequestDateParameter(CAMPO_ANNO_DATA_IRREVOCABILITA,
				CAMPO_MESE_DATA_IRREVOCABILITA, CAMPO_GIORNO_DATA_IRREVOCABILITA));
		lSenMod.setFlagSentenzaApplicazPena(getRequestStringParameter(CAMPO_FLAG_SENTENZA_APPLICAZ_PENA));
		lSenMod.setCodTipoProvvRif(getRequestStringParameter(CAMPO_COD_TIPO_PROVV_RIF));
		lSenMod.setDataProvvRif(getRequestDateParameter(CAMPO_ANNO_DATA_PROVV_RIF, CAMPO_MESE_DATA_PROVV_RIF,
				CAMPO_GIORNO_DATA_PROVV_RIF));
		lSenMod.setCodTipoAutoritaProvvRif(getRequestStringParameter(CAMPO_COD_TIPO_AUTORITA_PROVV_RIF));
		lSenMod.setAnnoProvvRif(getRequestIntParameter(CAMPO_ANNO_PROVV_RIF));
		lSenMod.setNumeroProvvRif(getRequestStringParameter(CAMPO_NUMERO_PROVV_RIF));

		lComMod = new ComuneModel(getCodComuneByDescr(getRequestStringParameter(CAMPO_COD_LUOGO_PROVV_RIF)));
		lSenMod.setCodLuogoProvvRif(lComMod.getCodComune());
		if (getRequestStringParameter(CAMPO_COD_TIPO_AUTORITA_PROVV_RIF).compareTo("-") != 0)
			/* lCodice = */getCodUfficioByCodTipoUfficioDescrComune(
					getRequestStringParameter(CAMPO_COD_TIPO_AUTORITA_PROVV_RIF),
					getRequestStringParameter(CAMPO_COD_LUOGO_PROVV_RIF));

		// Inizio MODIFICA 01-03-2006 -- DARIO -- VIVIANA
		// si è aggiunta la seguente espressione condizionale per uniformare il risultato della modifica
		// a quello dell'inserimento,ossia si cambia il codice da 01 a 03 e da 02 a 04 solo quando l'autorità
		// è una corte d'appello
		if (getRequestStringParameter(CAMPO_COD_TIPO_AUTORITA_PROVV_RIF).compareTo("-") != 0) {
			String lCodTipo = getRequestStringParameter(CAMPO_COD_TIPO_AUTORITA_EMITTENTE);
			if (lCodTipo.equals("CAP") || lCodTipo.equals("CAPSM") || lCodTipo.equals("CASAP")
					|| lCodTipo.equals("PGCAP") || lCodTipo.equals("PGMI") || lCodTipo.equals("PGMID")) {
				if (getRequestStringParameter(CAMPO_COD_TIPO_PROVV_RIF).equals("01"))
					lSenMod.setCodTipoProvvRif("03");
				else
					lSenMod.setCodTipoProvvRif("04");
			}
		}
		// Fine MODIFICA 01-03-2006 -- DARIO -- VIVIANA

		lSenMod.setNumSezioneAutoritaProvvRif(
				getRequestStringParameter(CAMPO_NUM_SEZIONE_AUTORITA_PROVV_RIF));
		lSenMod.setCodTipoDecisioneCassazione(getRequestStringParameter(CAMPO_COD_TIPO_DECISIONE_CASSAZIONE));
		lSenMod.setAnnoSentenzaCassazione(getRequestIntParameter(CAMPO_ANNO_SENTENZA_CASSAZIONE));
		lSenMod.setNumeroSentenzaCassazione(getRequestStringParameter(CAMPO_NUMERO_SENTENZA_CASSAZIONE));
		lSenMod.setAnnoRaccoltaGenerale(getRequestIntParameter(CAMPO_ANNO_RACCOLTA_GENERALE));
		lSenMod.setNumeroRaccoltaGenerale(getRequestStringParameter(CAMPO_NUMERO_RACCOLTA_GENERALE));
		lSenMod.setNote(getRequestStringParameter(CAMPO_NOTE));
		lSenMod.setDescrNumCampionePenale(getRequestStringParameter(CAMPO_DESCR_NUM_CAMPIONE_PENALE));

		if (getRequestStringParameter("TipoRG").equalsIgnoreCase("gip")) {
			lSenMod.setAnnoRegeGip(getRequestIntParameter("ARG"));
			lSenMod.setNumeroRegeGip(getRequestStringParameter("NRG"));
		}
		if (getRequestStringParameter("TipoRG").equalsIgnoreCase("dib")) {
			lSenMod.setAnnoRegeDib(getRequestIntParameter("ARG"));
			lSenMod.setNumeroRegeDib(getRequestStringParameter("NRG"));
		}
		if (getRequestStringParameter("TipoRG").equalsIgnoreCase("cas")) {
			lSenMod.setAnnoRegeCas(getRequestIntParameter("ARG"));
			lSenMod.setNumeroRegeCas(getRequestStringParameter("NRG"));
		}
		if (getRequestStringParameter("TipoRG").equalsIgnoreCase("cap")) {
			lSenMod.setAnnoRegeCap(getRequestIntParameter("ARG"));
			lSenMod.setNumeroRegeCap(getRequestStringParameter("NRG"));
		}
		if (getRequestStringParameter("TipoRG").equalsIgnoreCase("casap")) {
			lSenMod.setAnnoRegeCasap(getRequestIntParameter("ARG"));
			lSenMod.setNumeroRegeCasap(getRequestStringParameter("NRG"));
		}

		lSenMod.setNote1DecisioneCassazione(getRequestStringParameter("ANNOREGECAS"));
		lSenMod.setNote2DecisioneCassazione(getRequestStringParameter("NUMREGECAS"));
		lSenMod.setNote(getRequestStringParameter(CAMPO_NOTE));

		// lSenMod.setCodBilanciamentoCircostanze("-"); // Per le join

		// chiama il controller
		// SentenzaController lCtrl = new SentenzaController();
		IRegeSentenza lCtrl = RegeSiesLookupRemote.getRegeSentenzaRemote();
		RegeSentenzaModel lSenRet = lCtrl.ExModificaRegeSentenza(lSenMod);

		setRequestAttribute("modalita", "M");
		setRequestAttribute("sentenza", lSenRet);

		return PAGE_DETTAGLIO_RITORNO + lId;
	}

}