package siap.sius.unificazione.action;

import java.math.BigDecimal;

import siap.sico.web.ActionSiap;
import siap.sius.fascicolo.action.ICostantiFascicoloSius;
import siap.sius.unificazione.controller.IUnificazione;
import siap.sius.util.SIUSLookupRemote;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;

/**
 * <p>
 * Title: ActCancellaUnificazione
 * </p>
 * <p>
 * Description: Classe Azione per richiesta cancellazione del di Unificazione di 2 Procedimenti SIUS
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
public class ActCancellaUnificazione extends ActionSiap implements ICostantiUnificazione {

	public String processRequest() throws Exception {

		// Preleva dalla sessione i dati dell'utente connesso.
		// STUB :20030623 Verificare se si può evitare di utilizzare altri oggetti String
		String lCodiceOperatore = getCodUtenteConnesso();
		String lCodiceUfficio = getCodUfficioUtenteConnesso();
//		String lCodComune = getCodComuneUtenteConnesso();
		String lDescrComune = getUfficioUtenteConnesso().getDescrComune();
//		String lDescrTipoUfficio = getUfficioUtenteConnesso().getDescrTipoUfficio();

//		UfficioModel lUfficio = getUfficioUtenteConnesso();

		// Preleva dalla request la chiave dell'evento come parametro
		BigDecimal lKeyEvento = super
				.getRequestBigDecimalParameter(ICostantiUnificazione.CAMPO_ID_EVENTO_UNIFICAZIONE);

		IUnificazione lCtrl = SIUSLookupRemote.getUnificazioneRemote();
		lCtrl.ExCancellaUnificazione(lKeyEvento, lCodiceUfficio, lCodiceOperatore, lDescrComune);

		// setta la risposta nella request
		setRequestAttribute(IWebConstants.MESSAGE_TEXT, "Cancellazione Avvenuta Correttamente!");

		// Prepara la "pagina" di destinAction
		RedirectTo lRedirigi = new RedirectTo();
		lRedirigi.setPage(IWebConstants.PG_MAIN);
		lRedirigi.setAction(getRequestStringParameter(ACTION_DOPO_CANCELLAZIONE));

		lRedirigi.setParameter(ICostantiFascicoloSius.CAMPO_CHIAVE_ANNO,
				getRequestStringParameter(ICostantiFascicoloSius.CAMPO_CHIAVE_ANNO));
		lRedirigi.setParameter(ICostantiFascicoloSius.CAMPO_CHIAVE_PROGR,
				getRequestStringParameter(ICostantiFascicoloSius.CAMPO_CHIAVE_PROGR));

		lRedirigi.setParameter("noQuery", "OK");

		setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi);

		return IWebConstants.PG_MESSAGE; // restituisce la jsp di VIEW
	}

}