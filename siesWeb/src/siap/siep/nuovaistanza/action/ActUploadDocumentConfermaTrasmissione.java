package siap.siep.nuovaistanza.action;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.DateUtils;
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
//ANNA per dati del soggetto
import siap.sico.soggetto.controller.ISoggetto;
import siap.sico.soggetto.model.SoggettoModel;
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.jms.controller.ITrasmissioneJMS;
import siap.siep.notifica.model.NotificaModel;
import siap.siep.nuovaistanza.controller.INuovaIstanza;
import siap.siep.nuovaistanza.model.NuovaIstanzaModel;
import siap.siep.scadenzario.controller.IScadenzario;
import siap.siep.scadenzario.model.ScadenzarioModel;
import siap.siep.util.SIEPLookupRemote;

/**
 * <p>
 * Title: ActUploadDocumentConfermaTrasmissione
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2010
 * </p>
 * <p>
 * Company: Agile
 * </p>
 * 
 * @author not attributable
 * @version 1.0
 */
public class ActUploadDocumentConfermaTrasmissione extends ActionSiap
		implements ICostantiNuovaIstanza, ICostantiJMS {
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	@SuppressWarnings("rawtypes")
	public String processRequest() throws Exception {
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.info("inizio ActUploadDocumentConfermaTrasmissione");

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

		// Le Istanze sono considerate come documenti gia' registrati!!!!
		lModel.setFlagDocumentoRegistrato("S");

		IEvento lCtrl = SICOLookupRemote.getEventoRemote();
		lCtrl.ExUpdateDocument(lModel);

		EventoNotificaModel lNotEvento = lCtrl.ExRicercaEventoNotificaByKey(lEveId);

		// Preparo la trasmissione vera e propria dell'Istanza
		FascicoloSiepModel lFascicoloModel = (FascicoloSiepModel) getSessionAttribute("fascicolo");

		String lTipoUff = getRequestStringParameter(
				ICostantiNuovaIstanza.CAMPO_COD_TIPO_UFFICIO_DESTINATARIO);
		String lSedeUff = getRequestStringParameter(ICostantiNuovaIstanza.CAMPO_COD_LUOGO_DESTINATARIO);

		String lCodiceUfficio = getCodUfficioByCodTipoUfficioDescrComune(lTipoUff, lSedeUff);

		UfficioModel lLocal = getUfficioByCodUfficio(lCodiceUfficio);
		UfficioModel lBDI = getUfficioByCodUfficio(lLocal.getCodDistretto());

		UfficioModel lBDIMittente = getUfficioByCodUfficio(getUfficioUtenteConnesso().getCodDistretto());

		// ************ Esegue tutta una serie di operazioni sul DB locale **********************
		// Prepara l'Evento per la modifica
		EventoModel lEveMod = new EventoModel();
		lEveMod.setIdEvento(lEveId);
		lEveMod.setDataTrasmissioneAtti(DateUtils.getSysDate());
		lEveMod.setCodUfficioDestinatario(lCodiceUfficio);
		lEveMod.setCodTipoUfficioDestinatario(lTipoUff); // 04/03/2011
		lEveMod.setCodLuogoDestinatario(lSedeUff); // 04/03/2011
		lEveMod.setCodLuogoDestinatario(lLocal.getCodComune());
		lEveMod.setFasSieIdFascicoloSiep(lFascicoloModel.getIdFascicoloSiep());

		NotificaModel lNot = new NotificaModel(lNotEvento.getNotifiche()[0]);

		ITrasmissioneJMS lCtrlMess = SIEPLookupRemote.getTrasmissioneJMS();
		MessaggioModel lMessage = lCtrlMess.getMessageForNuovaIstanza(lEveId,
				lFascicoloModel.getIdFascicoloSiep());

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

		// setto i riferimenti del soggetto e sentenza
		SoggettoModel lSoggettoMod = new SoggettoModel();
		ISoggetto lCtrl1 = SICOLookupRemote.getSoggettoRemote();
		lSoggettoMod = lCtrl1.ExRicercaSoggettoByKey(lFascicoloModel.getSogIdSoggetto());
		lMessage.setCognomeSoggetto(lSoggettoMod.getCognome());
		lMessage.setNomeSoggetto(lSoggettoMod.getNome());
		lMessage.setDataNascita(lSoggettoMod.getDataNascita());
		lMessage.setCodStatoNascita(lSoggettoMod.getCodStatoNascita());
		lMessage.setCodComuneNascita(lSoggettoMod.getCodComuneNascita());

		SIAPSender lSender = new SIAPSender();
		lSender.send(lMessage);
		setRequestAttribute("IDEvento", lEveId.toString());

		// 29/12/2009 Nella fase di trasmissione istanza SIEP occorre evitare l'inserimento di un nuovo
		// STATO_PROCEDIMENTO
		// se la "send" non ha successo. Spostata la ExConfermaTrasferisciIstanza alla fine.
		boolean lStatoProcedimento = false;
		// EventoNotificaModel lEveNotMod =
		lCtrl.ExConfermaTrasferisciIstanza(lEveMod, lNot, lStatoProcedimento);

		// setta la risposta nella request
		setRequestAttribute(IWebConstants.MESSAGE_TEXT, "Trasmissione Istanza sottomessa al Sistema!");

		// Paolo 02/09/2010 aggiungo la cancellazione dello scadenzario di tipo '01' x trasmessa istanza
		// concessione misura alternativa
		INuovaIstanza lCtrlIst = SIEPLookupRemote.getNuovaIstanzaRemote();
		NuovaIstanzaModel lIstMod = lCtrlIst.ExRicercaNuovaIstanzaByEveIdEvento(lEveId);

		// Devo aggiornare lo stato dell'istanza a Trasmesso a sorv. o per competenza
		if (lTipoUff.equals("TDS") || lTipoUff.equals("UDS"))
			lIstMod.setCodStatoIstanza("03");
		else
			lIstMod.setCodStatoIstanza("02");

		lIstMod.setDataAggiornamento(DateUtils.getSysDate());
		lIstMod.setCodOperatoreAggiornamento(getCodUtenteConnesso());
		lIstMod.setCodUfficioAggiornamento(getCodUfficioUtenteConnesso());
		lIstMod.setCodLuogoDestinatario(lLocal.getCodComune());
		lIstMod.setCodUfficioDestinatario(lCodiceUfficio);
		lIstMod.setCodTipoUfficioDestinatario(lTipoUff);

		lCtrlIst.ExModificaStatoNuovaIstanza(lIstMod);

		if (("C001").equals(lIstMod.getCodContenuto()) && ("35").equals(lIstMod.getCodContenuto())) {
			ScadenzarioModel lScaMod = null;
			IScadenzario lCtrlSca = SIEPLookupRemote.getScadenzarioRemote();

			List lScaden = lCtrlSca.ExScadenzarioByIdFascicolo(lFascicoloModel.getIdFascicoloSiep());
			Iterator lIter = lScaden.iterator();

			while (lIter.hasNext()) {
				lScaMod = (ScadenzarioModel) lIter.next();
				lCtrlSca.ExCancellaScadenzarioSimeone(lScaMod);
			}
		}
		// fine paolo 02/09/2010

		// Prepara la "pagina" di destinAction
		RedirectTo lRedirigi = new RedirectTo();
		lRedirigi.setPage(IWebConstants.PG_MAIN);
		// STUB 11/05/2005 lRedirigi.setAction( "siap.siep.istanza.action.ActListaIstanzeTrasmesse" );
		lRedirigi.setAction("siap.siep.nuovaistanza.action.ActDettaglioNuovaIstanzaTrasmessa");
		lRedirigi.setParameter(ICostantiMessaggio.CAMPO_ID_MESSAGGIO, lMessage.getIdMessaggio().toString());

		setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi);

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.info("fine");

		return IWebConstants.PG_MESSAGE;
	}

}