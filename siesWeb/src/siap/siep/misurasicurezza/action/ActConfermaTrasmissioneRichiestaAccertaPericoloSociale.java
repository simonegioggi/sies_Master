package siap.siep.misurasicurezza.action;

import java.math.BigDecimal;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;
import siap.jms.ICostantiJMS;
import siap.jms.SIAPSender;
import siap.jms.messaggio.action.ICostantiMessaggio;
import siap.jms.messaggio.model.MessaggioModel;
import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoModel;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.istanza.action.ICostantiIstanza;
import siap.siep.jms.controller.ITrasmissioneJMS;
import siap.siep.notifica.model.NotificaModel;
import siap.siep.util.SIEPLookupRemote;

/**
 * <p>
 * Title: ActConfermaTrasmissioneRichiestaAccertaPericoloSociale
 * </p>
 * <p>
 * Description: Classe Action per la conferma del trasferimento della Richiesta di
 * </p>
 * <p>
 * Accertamento pericolosità sociale al MDS
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company:
 * </p>
 *
 * @author Ambrosino
 * @version 1.0
 */
public class ActConfermaTrasmissioneRichiestaAccertaPericoloSociale extends ActionSiap
		implements ICostantiMisuraSicurezza, ICostantiJMS {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	// @SuppressWarnings("rawtypes")
	public String processRequest() throws Exception {

		BigDecimal lEveId = getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO);

		// ------ Update del Documento BLOB sull'evento
		EventoModel lModel = new EventoModel();
		lModel.setIdEvento(lEveId);

		lModel.setDataAggiornamento(DateUtils.getSysDate());
		lModel.setCodUfficioAggiornamento(getCodUfficioUtenteConnesso());
		lModel.setCodOperatoreAggiornamento(getCodUtenteConnesso());
		lModel.setFlagDocumentoRegistrato("S");

		IEvento lCtrl = SICOLookupRemote.getEventoRemote();
		lCtrl.ExUpdateDocument(lModel);

		EventoNotificaModel lNotEvento = lCtrl.ExRicercaEventoNotificaByKey(lEveId);

		// Preparo la trasmissione vera e propria dell'Istanza
		FascicoloSiepModel lFascicoloModel = (FascicoloSiepModel) getSessionAttribute("fascicolo");

		String lTipoUff = getRequestStringParameter(ICostantiIstanza.CAMPO_COD_TIPO_UFFICIO_DESTINATARIO);
		String lSedeUff = getRequestStringParameter(ICostantiIstanza.CAMPO_COD_LUOGO_DESTINATARIO);

		String lCodiceUfficio = this.getCodUfficioByCodTipoUfficioDescrComune(lTipoUff, lSedeUff);

		UfficioModel lLocal = getUfficioByCodUfficio(lCodiceUfficio);
		UfficioModel lBDI = getUfficioByCodUfficio(lLocal.getCodDistretto());

		UfficioModel lBDIMittente = getUfficioByCodUfficio(getUfficioUtenteConnesso().getCodDistretto());

		// ******** Esegue tutta una serie di operazioni sul DB locale **********************
		// Prepara l'Evento per la modifica
		EventoModel lEveMod = new EventoModel();
		lEveMod.setIdEvento(lEveId);
		lEveMod.setDataTrasmissioneAtti(DateUtils.getSysDate());
		lEveMod.setCodTipoUfficioDestinatario(lTipoUff);
		lEveMod.setCodUfficioDestinatario(lCodiceUfficio);
		lEveMod.setCodLuogoDestinatario(lLocal.getCodComune());

		NotificaModel lNot = new NotificaModel(lNotEvento.getNotifiche()[0]);

		// Ticket#20200902016 - Commentato il controllo sull'obbligatorietà della presenza dell'avvocato come
		// da richiesta. Il sistema consentiva l'inserimento, stampa e validazione in assenza di avvocato ma
		// bloccava la sola trasmissione. Testata trasmissione e presa in carico SIUS, stessa e altra BDI, non
		// c'è evidenza di nullpointer in assenza di avvocato.
		// inizio intervento post 11.3 per la gestione del nullPointer sull'avvocato
		// try {
		// AvvocatoModel lAvvMod = new AvvocatoModel();
		// AvvocatoFascicoloSiepModel lAvvFascMod = new AvvocatoFascicoloSiepModel();
		// lAvvFascMod.setFasSieIdFascicoloSiep(
		// ((FascicoloSiepModel) (getSessionAttribute("fascicolo"))).getIdFascicoloSiep());
		// IAvvocato lCtrlavv = SIEPLookupRemote.getAvvocatoRemote();
		// Vector lVect = lCtrlavv.ExRicercaAvvocatiAttualiFascicolo(lAvvMod, lAvvFascMod);
		// if (lVect == null || lVect.size() == 0)
		// throw new F3BException(F3BException.USER_MESSAGE, "Nessun avvocato associato al fascicolo.");
		// } catch (siap.siep.SIEPException e) {
		// throw new F3BException(F3BException.USER_MESSAGE, "Nessun avvocato associato al fascicolo.");
		// }
		// fine intervento post 11.3 per la gestione del nullPointer sull'avvocato
		// Ticket#20200902016 - FINE

		// 18/02/2011 si gestisce in try/catch la trasmissione.
		MessaggioModel lMessage = new MessaggioModel();
		try {
			ITrasmissioneJMS lCtrlMess = SIEPLookupRemote.getTrasmissioneJMS();
			lMessage = lCtrlMess.getMessageForProvvedimento(lEveId, lFascicoloModel.getIdFascicoloSiep());

			// -------->>>>>>>>>>> Inserire un meccanismo di reperimento della BDI a partire
			// da un ufficio qualsiasi >>>>>>>>>>>>>>>>>>>>-----------------------------------------
			lMessage.setDescrBdiDestinataria(lBDI.getDescrComune());
			lMessage.setCodBdiDestinataria(lBDI.getCodUfficio());
			// >>>>>>>>> Destinazxione fissa e non da maschera.......
			lMessage.setCodBdiMittente(lBDIMittente.getCodUfficio());
			lMessage.setDescrBdiMittente(lBDIMittente.getDescrComune());
			lMessage.setCodUfficioDestinatario(lCodiceUfficio);
			lMessage.setCodUfficioMittente(getCodUfficioUtenteConnesso());
			lMessage.setCodTipoMessaggio(RICHIESTA);
			lMessage.setCodTipoOperazione(TRASFERIMENTO_RICHIESTA_ACCERTA_PERICOLO_SOCIALE);
			lMessage.setCodiceUtenteMittente(this.getCodUtenteConnesso());
			lMessage.setDataInvio(DateUtils.getSysDate());
			// SETTA RIFERIMENTI FASCICOLO SIEP
			lMessage.setChiaveAnnoSiep(lFascicoloModel.getChiaveAnno());
			lMessage.setChiaveProgrSiep(lFascicoloModel.getChiaveProgr());

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug(" ----------->   XMSG ---->  MESSAGGIO DA SPEDIRE A " + lBDI.getDescrComune()
					+ " : " + lMessage.toString());

			SIAPSender lSender = new SIAPSender();
			lSender.send(lMessage);
			setRequestAttribute("IDEvento", lEveId.toString());

			// 18/02/2011 Posticipata la trasmissione.
			boolean lStatoProcedimento = true;
			/* EventoNotificaModel lEveNotMod = */lCtrl.ExConfermaTrasferisciIstanza(lEveMod, lNot,
					lStatoProcedimento);
		} catch (F3BException e) {
			siesLogger.error("Eccezione in fase di trasmissione. ErrCode " + e.getErrorCode(), e);
			// Ticket#20200902016 - Collateralmente al ticket è stato riilevato che con servizio JMS down
			// veniva rilanciato un nullpointer sulla successiva istruzione
			// lRedirigi.setParameter(ICostantiMessaggio.CAMPO_ID_MESSAGGIO,
			// lMessage.getIdMessaggio().toString());
			// In casi di eccezione va sempre rilanciata, non ha senso andare avanti indicando che la
			// "trasmissione è stata sottomessa"
			// if (e.getErrorCode() != F3BException.USER_MESSAGE) {
			throw e;
			// }
		}

		// setta la risposta nella request
		setRequestAttribute(IWebConstants.MESSAGE_TEXT, "Trasmissione Provvedimento sottomessa al Sistema!");

		// Prepara la "pagina" di destinAction
		RedirectTo lRedirigi = new RedirectTo();
		lRedirigi.setPage(IWebConstants.PG_MAIN);
		// STUB 11/05/2005 lRedirigi.setAction( "siap.siep.jms.action.ActListaMessaggiTrasmessi" );
		lRedirigi.setAction("siap.siep.ordineesecuzione.action.ActDettaglioProvvedimentoTrasmesso");
		lRedirigi.setParameter(ICostantiMessaggio.CAMPO_ID_MESSAGGIO, lMessage.getIdMessaggio().toString());
		setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi);

		return IWebConstants.PG_MESSAGE;
	}

}