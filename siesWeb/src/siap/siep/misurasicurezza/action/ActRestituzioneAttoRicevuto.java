package siap.siep.misurasicurezza.action;

import java.math.BigDecimal;

import siap.jms.ICostantiJMS;
import siap.jms.JMSLookupRemote;
import siap.jms.SIAPReceiver;
import siap.jms.SIAPSender;
import siap.jms.config.JMSProperties;
import siap.jms.messaggio.action.ICostantiMessaggio;
import siap.jms.messaggio.controller.IMessaggio;
import siap.jms.messaggio.model.MessaggioModel;
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.web.ActionSiap;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.util.xml.TreeModel;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;

/**
 * 
 * @author d.fiorletta
 *
 */
public class ActRestituzioneAttoRicevuto extends ActionSiap implements ICostantiMisuraSicurezza, ICostantiJMS {

	public String processRequest() throws F3BException, Exception {

		if (JMSProperties.getInstance().getProperty("LISTENER_NUOVA_GESTIONE") != null
				&& JMSProperties.getInstance().getProperty("LISTENER_NUOVA_GESTIONE").trim()
						.equalsIgnoreCase("true")) {
			SIAPReceiver.getInstance().testInArrivo();
			SIAPReceiver.getInstance().testInPartenza();
			SIAPReceiver.getInstance().testStampa();
		} else {
			SIAPReceiver.getInstance();
		}

		// ==========================================================================
		// Creazione del MESSAGGIO di risposta
		// ==========================================================================
		BigDecimal lIdMess = getRequestBigDecimalParameter(ICostantiMessaggio.CAMPO_ID_MESSAGGIO);

		IMessaggio lCrtl = JMSLookupRemote.getMessaggioRemote();
		MessaggioModel lMessaggioRich = lCrtl.ExRicercaMessaggioByKey(lIdMess);

		UfficioModel lBDI = this.getUfficioByCodUfficio(this.getCodUfficioUtenteConnesso());

		MessaggioModel lMessageRisp = new MessaggioModel();

		lMessageRisp.setNote(getRequestStringParameter("MotivoRestituzione"));

		// n.b. la risposta va inviata all'ufficio a cui appartengono gli atti e
		// non all'ufficio mittente che potrebbe averli semplicemente inoltrati
		/*UfficioModel lUfficioAtti = */getUfficioByCodUfficio(lMessaggioRich.getChiaveUfficioSiep());

		// UfficioModel lUfficioPGCAPDest = getUfficioByCodUfficio(lUfficioAtti.getCodDistretto());
		// lMessageRisp.setDescrBdiDestinataria (lUfficioPGCAPDest.getDescrComune());
		// lMessageRisp.setCodBdiDestinataria (lUfficioAtti.getCodDistretto());
		// lMessageRisp.setCodUfficioDestinatario (lUfficioAtti.getCodUfficio());
		UfficioModel lUfficioReply = null;

		if (lMessaggioRich.getCodUfficioReplyTo() != null) {
			// Il messaggio mi è stato inoltrato. Rispondo all'ufficio indicato
			// nell'apposito campo (REPLY_TO). In caso di messaggio ricevuto come inoltro, l'id_messaggio
			// della richiesta originale è presente nel campo JMS_CORRELATION_REPLY_TO
			// mentre il campo JMS_CORRELATION_ID_MESSAGE contiene l'ID_MESSAGGIO
			// del messaggio di inoltro
			lUfficioReply = getUfficioByCodUfficio(lMessaggioRich.getCodUfficioReplyTo());

			// Indico nella risposta l'ID_MESSAGGIO della RICHIESTA presente sulla
			// BDI di Origine in modo che sia possibile collegare richiesta e risposta
			lMessageRisp.setJmsCorrelationIdMessage(lMessaggioRich.getJmsCorrelationReplyTo());
		} else {
			// Messaggio Diretto
			lUfficioReply = getUfficioByCodUfficio(lMessaggioRich.getCodUfficioMittente());

			// Indico nella risposta l'ID_MESSAGGIO della RICHIESTA presente sulla
			// BDI di Origine in modo che sia possibile collegare richiesta e risposta
			lMessageRisp.setJmsCorrelationIdMessage(lMessaggioRich.getJmsCorrelationIdMessage());
		}

		UfficioModel lUfficioPGCAPDest = getUfficioByCodUfficio(lUfficioReply.getCodDistretto());
		lMessageRisp.setDescrBdiDestinataria(lUfficioPGCAPDest.getDescrComune());
		lMessageRisp.setCodBdiDestinataria(lUfficioReply.getCodDistretto());
		lMessageRisp.setCodUfficioDestinatario(lUfficioReply.getCodUfficio());

		lMessageRisp.setCodBdiMittente(lBDI.getCodDistretto());
		lMessageRisp.setCodUfficioMittente(this.getCodUfficioUtenteConnesso());
		lMessageRisp.setCodiceUtenteMittente(this.getCodUtenteConnesso());

		lMessageRisp.setChiaveAnnoSiep(lMessaggioRich.getChiaveAnnoSiep());
		lMessageRisp.setChiaveProgrSiep(lMessaggioRich.getChiaveProgrSiep());
		lMessageRisp.setChiaveUfficioSiep(lMessaggioRich.getChiaveUfficioSiep());

		// Dati del Soggetto
		lMessageRisp.setNomeSoggetto(lMessaggioRich.getNomeSoggetto());
		lMessageRisp.setCognomeSoggetto(lMessaggioRich.getCognomeSoggetto());
		lMessageRisp.setDataNascita(lMessaggioRich.getDataNascita());
		lMessageRisp.setCodComuneNascita(lMessaggioRich.getCodComuneNascita());
		lMessageRisp.setCodStatoNascita(lMessaggioRich.getCodStatoNascita());

		lMessageRisp.setCodTipoMessaggio(ESITO);
		lMessageRisp.setCodTipoOperazione(ESITO_TRASFERIMENTO_COMPETENZA_MS);
		lMessageRisp.setCodEsito(RESTITUITO);

		// n.b. le date non vengono prese in considerazione nella spedizione del
		// messaggio jms ma solo per l'aggiornamento locale. NON sono propertyJms
		// Sulla BDI di destinazione, in fase di ricezione, verrà utilizzata
		// come DATA_INVIO/DATA_ESITO quella dello scarico.
		lMessageRisp.setDataInvio(lMessaggioRich.getDataInvio());
		lMessageRisp.setDataEsito(DateUtils.getSysDate());

		// n.b. TreeModel NON può essere null, ma non ha senso rimandare indietro tutto
		// il fascicolo solo per comunicare che è stato restituito, quindi
		// rimando in dietro un blob non significativo
		// lMessageRisp.setTreeModel(lMess.getTreeModel());
		lMessageRisp.setTreeModel(new TreeModel());

		// =========================================
		// Invio il messaggio di risposta
		// =========================================
		SIAPSender lSender = new SIAPSender();
		lSender.send(lMessageRisp);

		// Marco il messaggio di !!Richiesta!! evaso.
		lMessaggioRich.setFlagVisto("S");
		lMessaggioRich.setCodEsito(RESTITUITO);
		lMessaggioRich.setDataEsito(DateUtils.getSysDate());
		lCrtl.ExModificaMessaggio(lMessaggioRich);

		// setta la risposta nella request
		setRequestAttribute(IWebConstants.MESSAGE_TEXT,
				"Le motivazioni della restituzione degli Atti sono state inviate all'Ufficio mittente");

		// Prepara la "pagina" di destinAction
		RedirectTo lRedirigi = new RedirectTo();
		lRedirigi.setPage(IWebConstants.PG_MAIN);
		lRedirigi.setAction("siap.siep.misurasicurezza.action.ActRicercaAttiRicevuti");

		setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi);
		return IWebConstants.PG_MESSAGE;
	}

}