package siap.sius.decretounificazione.action;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;

import f3b.util.DateUtils;
import f3b.web.IWebConstants;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoModel;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.sius.SIUSException;
import siap.sius.decretounificazione.controller.IDecretoUnificazione;
import siap.sius.util.SIUSLookupRemote;

/**
 * <p>
 * Title: ActStampaDecretoUnificazione
 * </p>
 * <p>
 * Description: Classe Azione per richiesta stampa del Decreto di Unificazione di 2 Procedimenti SIUS
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
public class ActStampaDecretoUnificazione extends ActionSiap implements ICostantiDecretoUnificazione {

	public String processRequest() throws Exception {

		// Preleva dalla sessione i dati dell'utente connesso.
		// STUB :20030623 Verificare se si può evitare di utilizzare altri oggetti String
		String lCodiceOperatore = getCodUtenteConnesso();
		String lCodiceUfficio = getCodUfficioUtenteConnesso();
		// String lCodComune = getCodComuneUtenteConnesso();
		String lDescrComune = getUfficioUtenteConnesso().getDescrComune();
		String lDescrTipoUfficio = getUfficioUtenteConnesso().getDescrTipoUfficio();

		UfficioModel lUfficio = getUfficioUtenteConnesso();

		// Preleva dalla request la chiave dell'evento come parametro
		BigDecimal lKeyEvento = super.getRequestBigDecimalParameter(
				ICostantiDecretoUnificazione.CAMPO_ID_EVENTO_UNIFICAZIONE);

		// EventoModel lEveMod = new EventoModel();

		// Preleva l'eventoModel
		IEvento lEveCtrl = SICOLookupRemote.getEventoRemote();
		EventoModel lEveMod = lEveCtrl.ExRicercaEventoByKey(lKeyEvento);

		lEveMod.setIdEvento(lKeyEvento);
		lEveMod.setDataAggiornamento(DateUtils.getSysDate());
		lEveMod.setCodUfficioAggiornamento(lCodiceUfficio);
		lEveMod.setCodOperatoreAggiornamento(lCodiceOperatore);
		lEveMod.setDescrLuogoEmittente(lDescrComune);
		lEveMod.setDescrUfficioEmittente(lDescrTipoUfficio);
		lEveMod.setDataAggiornamento(DateUtils.getSysDate());
		lEveMod.setCodUfficioAggiornamento(lCodiceUfficio);
		lEveMod.setCodOperatoreAggiornamento(lCodiceOperatore);
		lEveMod.setFlagDocumentoRegistrato("N");

		IDecretoUnificazione lCtrl = SIUSLookupRemote.getDecretoUnificazioneRemote();
		// EventoNotificaModel lEveNotModel = lCtrl.ExStampaDecretoUnificazione( lEveMod, lUfficio );

		EventoNotificaModel lEveNotModel = lCtrl.ExStampaDecretoUnificazione(lEveMod, lUfficio,
				super.getUtenteConnesso());

		ByteArrayOutputStream lReport = lEveNotModel.getEvento().getDocBlobOut();

		// Prepara la pagina di destinazione
		if (lReport != null)
			setRequestAttribute("report", lReport);
		else
			throw new SIUSException(SIUSException.USER_MESSAGE, "Nessun documento è stato generato!");

		return IWebConstants.PG_DOWNLOAD_NEW;
	}

}