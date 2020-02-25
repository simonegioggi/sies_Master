package siap.sius.stralcio.action;

import java.math.BigDecimal;

import siap.sico.web.ActionSiap;
import siap.sius.fascicolo.action.ICostantiFascicoloSius;
import siap.sius.stralcio.controller.IStralcio;
import siap.sius.util.SIUSLookupRemote;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;

/**
 * <p>
 * Title: ActCancellaStralcio
 * </p>
 * <p>
 * Description: Classe Azione per richiesta cancellazione del di Stralcio di 2 Procedimenti SIUS
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
public class ActCancellaStralcio extends ActionSiap implements ICostantiStralcio {

	public String processRequest() throws Exception {

		// Preleva dalla sessione i dati dell'utente connesso.
		// STUB :20030623 Verificare se si può evitare di utilizzare altri oggetti String
		String lCodiceOperatore = getCodUtenteConnesso();
		String lCodiceUfficio = getCodUfficioUtenteConnesso();
//		String lCodComune = getCodComuneUtenteConnesso();
		String lDescrComune = getUfficioUtenteConnesso().getDescrComune();
//		String lDescrTipoUfficio = getUfficioUtenteConnesso().getDescrTipoUfficio();

//		UfficioModel lUfficio = getUfficioUtenteConnesso();

		// Preleva dalla request la chiave dell'evento di Stralcio come parametro
		BigDecimal lKeyEvento = super
				.getRequestBigDecimalParameter(ICostantiStralcio.CAMPO_ID_EVENTO_STRALCIO);

		IStralcio lCtrl = SIUSLookupRemote.getStralcioRemote();
		lCtrl.ExCancellaStralcio(lKeyEvento, lCodiceUfficio, lCodiceOperatore, lDescrComune);

		// setta la risposta nella request
		setRequestAttribute(IWebConstants.MESSAGE_TEXT, "Cancellazione Stralcio Avvenuta Correttamente!");

		// Prepara la "pagina" di destinAction
		RedirectTo lRedirigi = new RedirectTo();
		lRedirigi.setPage(IWebConstants.PG_MAIN);
		lRedirigi.setAction(this.getRequestStringParameter(ACTION_DOPO_CANCELLAZIONE));
		lRedirigi.setParameter(ICostantiFascicoloSius.CAMPO_ID_FASCICOLO_SIUS,
				this.getRequestStringParameter(ICostantiFascicoloSius.CAMPO_CHIAVE_ANNO));

		lRedirigi.setParameter(ICostantiFascicoloSius.CAMPO_CHIAVE_ANNO,
				this.getRequestStringParameter(ICostantiFascicoloSius.CAMPO_CHIAVE_ANNO));
		lRedirigi.setParameter(ICostantiFascicoloSius.CAMPO_ID_FASCICOLO_SIUS,
				this.getRequestStringParameter(ICostantiFascicoloSius.CAMPO_ID_FASCICOLO_SIUS));

		lRedirigi.setParameter("noQuery", "OK");

		setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi);

		return IWebConstants.PG_MESSAGE; // restituisce la jsp di VIEW
	}

}