package siap.sige.decretounificazione.action;

import java.math.BigDecimal;

import f3b.web.IWebConstants;
import siap.sico.web.ActionSiap;
import siap.sige.decretounificazione.controller.IDecretoUnificazioneSige;
import siap.sige.fascicolo.action.ICostantiFascicoloSige;
import siap.sige.util.SIGELookupRemote;

/**
 * <p>
 * Title: ActCancellaDecretoUnificazioneSige
 * </p>
 * <p>
 * Description: Classe Azione per richiesta cancellazione del Decreto di Unificazione di 2 Procedimenti SIGE
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
public class ActCancellaDecretoUnificazioneSige extends ActionSiap
		implements ICostantiDecretoUnificazioneSige {

	public String processRequest() throws Exception {

		// Preleva dalla sessione i dati dell'utente connesso.
		String lCodiceOperatore = getCodUtenteConnesso();
		String lCodiceUfficio = getCodUfficioUtenteConnesso();
		// String lCodComune = getCodComuneUtenteConnesso();
		String lDescrComune = getUfficioUtenteConnesso().getDescrComune();
		// String lDescrTipoUfficio = getUfficioUtenteConnesso().getDescrTipoUfficio();

		// UfficioModel lUfficio = getUfficioUtenteConnesso();
		String nextAct = null;
		String retPage = "";

		// Preleva dalla request la chiave dell'evento come parametro
		// BigDecimal lKeyEvento =
		// super.getRequestBigDecimalParameter(ICostantiDecretoUnificazioneSige.CAMPO_ID_EVENTO_UNIFICAZIONE);
		BigDecimal lKeyProvvedimento = super.getRequestBigDecimalParameter(
				ICostantiDecretoUnificazioneSige.CAMPO_ID_DECRETO_UNIFICAZIONE);

		// 16/01/2018 SC *** inizio ****
		// Risolta anomalia su tasto 'Indietro' riscontrata durante fase di test di pre-collaudo
		BigDecimal idFascicoloSigeUnificato = null;
		if (!isRequestParameterNullObj(ICostantiFascicoloSige.CAMPO_ID_FASCICOLO_SIGE)) {
			idFascicoloSigeUnificato = getRequestBigDecimalParameter(
					ICostantiFascicoloSige.CAMPO_ID_FASCICOLO_SIGE);
		}
		// 16/01/2018 SC *** fine ****

		IDecretoUnificazioneSige lCtrl = SIGELookupRemote.getDecretoUnificazioneSigeRemote();
		lCtrl.ExCancellaDecretoUnificazioneSige(lKeyProvvedimento, lCodiceUfficio, lCodiceOperatore,
				lDescrComune);
		// Azione dopo la cancellazione
		if (!isRequestParameterNullObj(IWebConstants.ACTION_DOPO_CANCELLAZIONE)) {
			nextAct = getRequestStringParameter(IWebConstants.ACTION_DOPO_CANCELLAZIONE);
			if (!isRequestParameterNullObj("noQuery")) {
				nextAct += "&noQuery=";
				nextAct += "OK";
			}
			// 16/01/2018 SC *** inizio ****
			// Risolta anomalia su tasto 'Indietro' riscontrata durante fase di test di pre-collaudo
		} else {
			if (idFascicoloSigeUnificato != null) {
				nextAct = "siap.sige.fascicolo.action.ActLoadDettaglioFascicolo&IdFascicoloSige="
						+ idFascicoloSigeUnificato;
			} else {
				nextAct = "siap.sige.unificazione.action.ActLoadVerificaVerbaleUnificazioneSige";
			}
		}
		// 16/01/2018 SC *** fine ****

		retPage = ritornoDopoCancellazione("Cancellazione Avvenuta Correttamente!", nextAct);

		return retPage; // restituisce la jsp di VIEW
	}

}