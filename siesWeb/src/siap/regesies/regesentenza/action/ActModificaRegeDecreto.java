package siap.regesies.regesentenza.action;

/**
* <p>Title: ActModificaSentenza</p>
* <p>Description: Classe Action per la modifica di Sentenza</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import siap.regesies.regesentenza.controller.IRegeSentenza;
import siap.regesies.regesentenza.model.RegeSentenzaModel;
import siap.regesies.util.RegeSiesLookupRemote;
import siap.sico.decodifiche.model.ComuneModel;
import siap.sico.security.action.ICostantiSecurity;
import siap.sico.utente.model.UtenteModel;
import siap.sico.web.ActionSiap;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;

public class ActModificaRegeDecreto extends ActionSiap implements ICostantiRegeSentenza {

	/**
	 * Azione di Modifica del Sentenza
	 * 
	 * @return Nome della pagina JSP da visualizzare al termine dell'elaborazione
	 * @throws F3BException
	 */
	public String processRequest() throws Exception {

		String lId = getRequestStringParameter(CAMPO_ID_FILE);

		// riempie il model
		RegeSentenzaModel lSenMod = new RegeSentenzaModel();
		lSenMod.setIdFile(lId);
		lSenMod.setCodTipoProvvedimento("02");
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
		// lSenMod.setCodLuogoEmittente( getRequestStringParameter( CAMPO_COD_LUOGO_EMITTENTE) );
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

		lSenMod.setCodTipoDecisioneCassazione(getRequestStringParameter(CAMPO_COD_TIPO_DECISIONE_CASSAZIONE));
		lSenMod.setAnnoSentenzaCassazione(getRequestIntParameter(CAMPO_ANNO_SENTENZA_CASSAZIONE));
		lSenMod.setNumeroSentenzaCassazione(getRequestStringParameter(CAMPO_NUMERO_SENTENZA_CASSAZIONE));
		lSenMod.setAnnoRaccoltaGenerale(getRequestIntParameter(CAMPO_ANNO_RACCOLTA_GENERALE));
		lSenMod.setNumeroRaccoltaGenerale(getRequestStringParameter(CAMPO_NUMERO_RACCOLTA_GENERALE));
		lSenMod.setNote(getRequestStringParameter(CAMPO_NOTE));

		lSenMod.setCodLuogoProvvRif("-"); // Per le join
		lSenMod.setCodTipoAutoritaProvvRif("-"); // Per le join
		lSenMod.setCodTipoProvvRif("-"); // Per le join
		// lSenMod.setCodBilanciamentoCircostanze("-"); // Per le join

		// chiama il controller
		// SentenzaController lCtrl = new SentenzaController();
		IRegeSentenza lCtrl = RegeSiesLookupRemote.getRegeSentenzaRemote();
		RegeSentenzaModel lSenRet = lCtrl.ExModificaRegeSentenza(lSenMod);

		setRequestAttribute("sentenza", lSenRet);

		String lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
				+ "=siap.regesies.regesentenza.action.ActDettaglioRegeSentenza&" + CAMPO_ID_FILE + "="
				+ lSenRet.getIdFile();

		return lPage;
	}

}