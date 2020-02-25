package siap.sige.produzioneatti.action;

// Dopo da eliminare import inutili.

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;

import f3b.util.DateUtils;
import f3b.web.IWebConstants;
import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoModel;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.security.action.ICostantiSecurity;
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.utente.model.UtenteModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.sius.SIUSException;
import siap.sius.richiestaatti.controller.IRichiestaAtti;
import siap.sius.util.SIUSLookupRemote;

/**
 * <p>
 * Title: ActStampaProduzioneAtti
 * </p>
 * <p>
 * Description: Classe Azione responsabile della richiesta stampa dei documenti della funzionalità richiesta
 * atti.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author not attributable
 * @version 1.0
 */
public class ActStampaProduzioneAtti extends ActionSiap implements ICostantiProduzioneAtti {

	public String processRequest() throws Exception {

		// Preleva dalla sessione i dati dell'utente connesso.
		String lCodiceOperatore = getCodUtenteConnesso();
		String lCodiceUfficio = getCodUfficioUtenteConnesso();
		// String lCodComune = getCodComuneUtenteConnesso();
		String lDescrComune = getUfficioUtenteConnesso().getDescrComune();
		String lDescrTipoUfficio = getUfficioUtenteConnesso().getDescrTipoUfficio();

		UfficioModel lUfficio = getUfficioUtenteConnesso();

		// Preleva dalla request la chiave dell'evento come parametro
		BigDecimal lKeyEvento = super.getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO);

		// Preleva l'Evento
		IEvento lCtrlEve = SICOLookupRemote.getEventoRemote();
		EventoModel lEveMod = lCtrlEve.ExRicercaEventoByKey(lKeyEvento);
		// Valorizza i campi d'aggiornamento

		lEveMod.setIdEvento(lKeyEvento);
		lEveMod.setDataAggiornamento(DateUtils.getSysDate());
		lEveMod.setCodUfficioAggiornamento(lCodiceUfficio);
		lEveMod.setCodOperatoreAggiornamento(lCodiceOperatore);
		lEveMod.setDescrLuogoEmittente(lDescrComune);
		lEveMod.setDescrUfficioEmittente(lDescrTipoUfficio);
		lEveMod.setFlagDocumentoRegistrato("N");

		// Si Recupera l'utente dalla sessione.
		UtenteModel lUtenteMod = (UtenteModel) getSessionAttribute(ICostantiSecurity.SESSION_UTENTE_CONNESSO);

		IRichiestaAtti lCtrl = SIUSLookupRemote.getRichiestaAttiRemote();
		EventoNotificaModel lEveNotModel = lCtrl.ExStampaRichiestaAtti(lEveMod, lUfficio, lUtenteMod);

		ByteArrayOutputStream lReport = lEveNotModel.getEvento().getDocBlobOut();

		// Prepara la pagina di destinazione
		if (lReport != null)
			setRequestAttribute("report", lReport);
		else
			throw new SIUSException(SIUSException.USER_MESSAGE, "Nessun documento è stato generato!");

		return IWebConstants.PG_DOWNLOAD;
	}

}