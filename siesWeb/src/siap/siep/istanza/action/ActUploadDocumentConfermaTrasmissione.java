package siap.siep.istanza.action;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.math.BigDecimal;

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
import siap.siep.jms.controller.ITrasmissioneJMS;
import siap.siep.notifica.model.NotificaModel;
import siap.siep.util.SIEPLookupRemote;
import f3b.util.DateUtils;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;

/**
 * <p>
 * Title: ActUploadDocumentConfermaTrasmissione
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
public class ActUploadDocumentConfermaTrasmissione extends ActionSiap implements ICostantiIstanza,
		ICostantiJMS {

	public String processRequest() throws Exception {

		BigDecimal lEveId = getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO);

		// ------ Update del Documento BLOB sull'evento
		EventoModel lModel = new EventoModel();
		lModel.setIdEvento(lEveId);

		InputStream lInput = getFile(ICostantiEvento.CAMPO_BLOB);

		if (lInput != null) {
			byte[] lBuffer = new byte[lInput.available()];
			lInput.read(lBuffer);
			ByteArrayInputStream lSt = new ByteArrayInputStream(lBuffer);
			lModel.setDocBlobIn(lSt);
		}

		lModel.setDataAggiornamento(DateUtils.getSysDate());

		lModel.setCodUfficioAggiornamento(getCodUfficioUtenteConnesso());
		lModel.setCodOperatoreAggiornamento(getCodUtenteConnesso());

		/*
		 * if(isRequestChecked( ICostantiEvento.CAMPO_VALIDA) )
		 */
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

		// ******** Esegue tutta una serie di operazioni sul DB locale **********************
		// Prepara l'Evento per la modifica
		EventoModel lEveMod = new EventoModel();
		lEveMod.setIdEvento(lEveId);
		lEveMod.setDataTrasmissioneAtti(DateUtils.getSysDate());
		lEveMod.setCodUfficioDestinatario(lCodiceUfficio);
		lEveMod.setCodLuogoDestinatario(lLocal.getCodComune());
		lEveMod.setFasSieIdFascicoloSiep(lFascicoloModel.getIdFascicoloSiep());

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

		// 29/12/2009 Nella fase di trasmissione istanza SIEP occorre evitare l'inserimento di un nuovo
		// STATO_PROCEDIMENTO
		// se la "send" non ha successo. Spostata la ExConfermaTrasferisciIstanza alla fine.
		// /boolean lStatoProcedimento = false;
		// /EventoNotificaModel lEveNotMod = lCtrl.ExConfermaTrasferisciIstanza(lEveMod,
		// lNot,lStatoProcedimento);
		// /**************************************************************************************/

		ITrasmissioneJMS lCtrlMess = SIEPLookupRemote.getTrasmissioneJMS();
		MessaggioModel lMessage = lCtrlMess
				.getMessageForIstanza(lEveId, lFascicoloModel.getIdFascicoloSiep());

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
		lMessage.setCodTipoOperazione(TRASFERIMENTO_ISTANZA);
		lMessage.setCodiceUtenteMittente(this.getCodUtenteConnesso());
		lMessage.setDataInvio(DateUtils.getSysDate());
		// SETTA RIFERIMENTI FASCICOLO SIEP
		lMessage.setChiaveAnnoSiep(lFascicoloModel.getChiaveAnno());
		lMessage.setChiaveProgrSiep(lFascicoloModel.getChiaveProgr());

		SIAPSender lSender = new SIAPSender();
		lSender.send(lMessage);
		setRequestAttribute("IDEvento", lEveId.toString());

		// 29/12/2009 Nella fase di trasmissione istanza SIEP occorre evitare l'inserimento di un nuovo
		// STATO_PROCEDIMENTO
		// se la "send" non ha successo. Spostata la ExConfermaTrasferisciIstanza alla fine.
		boolean lStatoProcedimento = false;
		/*EventoNotificaModel lEveNotMod = */lCtrl
				.ExConfermaTrasferisciIstanza(lEveMod, lNot, lStatoProcedimento);

		// setta la risposta nella request
		setRequestAttribute(IWebConstants.MESSAGE_TEXT, "Trasmissione Istanza sottomessa al Sistema!");

		// Prepara la "pagina" di destinAction
		RedirectTo lRedirigi = new RedirectTo();
		lRedirigi.setPage(IWebConstants.PG_MAIN);
		// STUB 11/05/2005 lRedirigi.setAction( "siap.siep.istanza.action.ActListaIstanzeTrasmesse" );
		lRedirigi.setAction("siap.siep.istanza.action.ActDettaglioIstanzaTrasmessa");
		lRedirigi.setParameter(ICostantiMessaggio.CAMPO_ID_MESSAGGIO, lMessage.getIdMessaggio().toString());

		setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi);

		return IWebConstants.PG_MESSAGE;
	}

}