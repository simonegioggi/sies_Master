package siap.sius.decretounificazione.action;

import java.math.BigDecimal;

import f3b.web.IWebConstants;
import siap.sico.web.ActionSiap;
import siap.sius.decretounificazione.controller.IDecretoUnificazione;
import siap.sius.util.SIUSLookupRemote;

/**
 * <p>
 * Title: ActCancellaDecretoUnificazione
 * </p>
 * <p>
 * Description: Classe Azione per richiesta cancellazione del Decreto di Unificazione di 2 Procedimenti SIUS
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @version 1.0
 */
public class ActCancellaDecretoUnificazione extends ActionSiap implements ICostantiDecretoUnificazione {

	public String processRequest() throws Exception {

		// Preleva dalla sessione i dati dell'utente connesso.
		// STUB :20030623 Verificare se si può evitare di utilizzare altri oggetti String
		String lCodiceOperatore = getCodUtenteConnesso();
		String lCodiceUfficio = getCodUfficioUtenteConnesso();
		// String lCodComune = getCodComuneUtenteConnesso();
		String lDescrComune = getUfficioUtenteConnesso().getDescrComune();
		// String lDescrTipoUfficio = getUfficioUtenteConnesso().getDescrTipoUfficio();

		// UfficioModel lUfficio = getUfficioUtenteConnesso();
		String nextAct = null;
		String retPage = "";

		// Preleva dalla request la chiave dell'evento come parametro
		BigDecimal lKeyEvento = super.getRequestBigDecimalParameter(
				ICostantiDecretoUnificazione.CAMPO_ID_EVENTO_UNIFICAZIONE);

		IDecretoUnificazione lCtrl = SIUSLookupRemote.getDecretoUnificazioneRemote();
		lCtrl.ExCancellaDecretoUnificazione(lKeyEvento, lCodiceUfficio, lCodiceOperatore, lDescrComune);
		// Azione dopo la cancellazione
		if (!isRequestParameterNullObj(IWebConstants.ACTION_DOPO_CANCELLAZIONE)) {
			nextAct = getRequestStringParameter(IWebConstants.ACTION_DOPO_CANCELLAZIONE);
			if (!isRequestParameterNullObj("noQuery")) {
				nextAct += "&noQuery=";
				nextAct += "OK";
			}
		} else
			nextAct = "siap.sius.decretounificazione.action.ActLoadVerificaDecretoUnificazione";
		retPage = ritornoDopoCancellazione("Cancellazione Avvenuta Correttamente!", nextAct);

		return retPage; // restituisce la jsp di VIEW
	}

}