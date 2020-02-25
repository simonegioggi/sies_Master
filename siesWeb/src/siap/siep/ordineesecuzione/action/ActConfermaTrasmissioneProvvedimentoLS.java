package siap.siep.ordineesecuzione.action;

import java.math.BigDecimal;

import org.apache.log4j.Logger;

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
import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;

/**
 * <p>
 * Title: ActConfermaTrasmissioneLSOrdineEsecuzione
 * </p>
 * <p>
 * Description:
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
public class ActConfermaTrasmissioneProvvedimentoLS extends ActionSiap
		implements ICostantiIstanza, ICostantiJMS {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public String processRequest() throws Exception {

		BigDecimal lEveId = getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO);

		// ------ Update del Documento BLOB sull'evento
		EventoModel lModel = new EventoModel();
		lModel.setIdEvento(lEveId);

		/*
		 * InputStream lInput = getFile(ICostantiEvento.CAMPO_BLOB);
		 * 
		 * if(lInput != null) { byte[] lBuffer = new byte[lInput.available()]; lInput.read(lBuffer);
		 * ByteArrayInputStream lSt = new ByteArrayInputStream(lBuffer); lModel.setDocBlobIn(lSt); }
		 * 
		 */
		lModel.setDataAggiornamento(DateUtils.getSysDate());

		lModel.setCodUfficioAggiornamento(getCodUfficioUtenteConnesso());
		lModel.setCodOperatoreAggiornamento(getCodUtenteConnesso());

		/* if(isRequestChecked( ICostantiEvento.CAMPO_VALIDA) ) */

		// Le Istanze sono considerate come documenti gia' registrati!!!!
		lModel.setFlagDocumentoRegistrato("S");

		/*
		 * else lModel.setFlagDocumentoRegistrato("N");
		 */

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

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("BDI MIttente = " + lBDIMittente);
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("BDI Destinatario = " + lBDI);

		// ******** Esegue tutta una serie di operazioni sul DB locale **********************
		// Prepara l'Evento per la modifica
		EventoModel lEveMod = new EventoModel();
		lEveMod.setIdEvento(lEveId);
		lEveMod.setDataTrasmissioneAtti(DateUtils.getSysDate());
		lEveMod.setCodTipoUfficioDestinatario(lTipoUff);
		lEveMod.setCodUfficioDestinatario(lCodiceUfficio);
		lEveMod.setCodLuogoDestinatario(lLocal.getCodComune());

		NotificaModel lNot = new NotificaModel(lNotEvento.getNotifiche()[0]);
		/*----	// Prepara la Notifica
		lNot.setCodTipoNotifica("N");
		lNot.setDataInvio(DateUtils.getSysDate());
		lNot.setCodEsito("-");
		lNot.setCodOperatoreInserimento(getCodUtenteConnesso());
		lNot.setDataInserimento(DateUtils.getSysDate());
		lNot.setCodUfficioInserimento(this.getCodUfficioUtenteConnesso());
		lNot.setUffCodUfficio(lCodiceUfficio);
		lNot.setEveIdEvento(lEveId);
		lNot.setUfficio(lLocal);*/

		// **************************************************************************************/

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
			lMessage.setCodTipoOperazione(TRASFERIMENTO_PROVVEDIMENTO);
			lMessage.setCodiceUtenteMittente(this.getCodUtenteConnesso());
			lMessage.setDataInvio(DateUtils.getSysDate());
			// SETTA RIFERIMENTI FASCICOLO SIEP
			lMessage.setChiaveAnnoSiep(lFascicoloModel.getChiaveAnno());
			lMessage.setChiaveProgrSiep(lFascicoloModel.getChiaveProgr());

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("MESSAGGIO DA SPEDIRE A " + lBDI.getDescrComune() + " : " + lMessage.toString());

			SIAPSender lSender = new SIAPSender();
			lSender.send(lMessage);
			setRequestAttribute("IDEvento", lEveId.toString());

			// 18/02/2011 Posticipata la trasmissione.
			boolean lStatoProcedimento = true;
			/* EventoNotificaModel lEveNotMod = */lCtrl.ExConfermaTrasferisciIstanza(lEveMod, lNot,
					lStatoProcedimento);
		} catch (F3BException e) {
			if (e.getErrorCode() != F3BException.USER_MESSAGE) {
				throw e;
			}
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