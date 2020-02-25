package siap.sius.decretounificazione.action;

import java.util.Date;

import f3b.web.IWebConstants;
import siap.sico.evento.model.EventoModel;
import siap.sico.web.ActionSiap;
import siap.sius.decretounificazione.controller.IDecretoUnificazione;
import siap.sius.util.SIUSLookupRemote;

/**
 * <p>
 * Title: ActInserisciDecretoUnificazione
 * </p>
 * <p>
 * Description: Classe Action di inserimento del Decreto di Unificazione di due Procedimenti SIUS
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Bull
 * </p>
 */

public class ActInserisciDecretoUnificazione extends ActionSiap implements ICostantiDecretoUnificazione {

	public String processRequest() throws Exception {

		// Recupero della data di Unificazione digitata e del numero dei fascicoli da unificare.
		Date dataUnificazione = getRequestDateParameter(CAMPO_DATA_AAAA_UNIFICAZIONE,
				CAMPO_DATA_MM_UNIFICAZIONE, CAMPO_DATA_GG_UNIFICAZIONE);
		String annoDaUnif = getRequestStringParameter(CAMPO_ANNO_DA_UNIF);
		String numeroDaUnif = getRequestStringParameter(CAMPO_NUMERO_DA_UNIF);
		String annoUnificante = getRequestStringParameter(CAMPO_ANNO_UNIFICANTE);
		String numeroUnificante = getRequestStringParameter(CAMPO_NUMERO_UNIFICANTE);
		// String dataUnifString =
		// getRequestStringParameter(CAMPO_DATA_GG_UNIFICAZIONE)+"-"+getRequestStringParameter(CAMPO_DATA_MM_UNIFICAZIONE)
		// +"-"+getRequestStringParameter(CAMPO_DATA_AAAA_UNIFICAZIONE);
		// String idFascicoloUnificante = getRequestStringParameter( CAMPO_ID_FASCICOLO_UNIFICANTE);

		// Unificazione dei 2 Procedimenti.
		IDecretoUnificazione lCtrl = SIUSLookupRemote.getDecretoUnificazioneRemote();
		EventoModel lEveUnificazione = lCtrl.ExInserisciDecretoUnificazione(annoDaUnif, numeroDaUnif,
				annoUnificante, numeroUnificante, getCodUfficioUtenteConnesso(), getCodUtenteConnesso(),
				getCodComuneUtenteConnesso(), dataUnificazione);

		// restituisce la jsp di VIEW.
		// Nella request viaggia l'ID dell'Evento di Unificazione appena inserito.
		return IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
				+ "=siap.sius.decretounificazione.action.ActLoadDettaglioDecretoUnificazione&"
				+ CAMPO_ID_EVENTO_UNIFICAZIONE + "=" + lEveUnificazione.getIdEvento().toString()
				+ "&FlagIns=Y";
	}

}