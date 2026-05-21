package siap.siep.misurasicurezza.action;

import java.math.BigDecimal;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.util.xml.TreeModel;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;
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
import siap.siep.jms.controller.IPresaInCaricoJMS;
import siap.siep.util.SIEPLookupRemote;
import siap.sius.SIUSException;

/**
 * Questa azione effetta la presa in carico del procedimento ricevuto ovvero:
 *
 * - L'iscrizione dei dati a sistema se proveniente da fuori distretto o evetualmente l'aggiornamento se già
 * presenti - L'aggiornamento del messaggio come preso elaborato (FLAG_VISTO=S) - L'invio della risposta
 * alll'ufficio mittente
 *
 *
 * @author d.fiorletta
 * @version 1.0
 */
public class ActPresaInCaricoAttoRicevuto extends ActionSiap
		implements ICostantiMisuraSicurezza, ICostantiJMS {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public String processRequest() throws F3BException, Exception {

		// Avvio i listener
		if (JMSProperties.getInstance().getProperty("LISTENER_NUOVA_GESTIONE") != null && JMSProperties
				.getInstance().getProperty("LISTENER_NUOVA_GESTIONE").trim().equalsIgnoreCase("true")) {
			SIAPReceiver.getInstance().testInArrivo();
			SIAPReceiver.getInstance().testInPartenza();
			SIAPReceiver.getInstance().testStampa();
		} else {
			SIAPReceiver.getInstance();
		}

		// =========================
		// Recupero il messaggio
		// =========================
		BigDecimal lIdMess = getRequestBigDecimalParameter(ICostantiMessaggio.CAMPO_ID_MESSAGGIO);

		IMessaggio lCrtl = JMSLookupRemote.getMessaggioRemote();
		MessaggioModel lMessaggio = lCrtl.ExRicercaMessaggioByKey(lIdMess);

		// ==========================================================================
		// Acquisizione a sistema dei dati del fascicolo
		// ==========================================================================
		// il messaggio potrebbe venire come inoltro per cui va testata la BDI
		// del fascicolo e non quella del mittente.
		UfficioModel lUfficioAtti = getUfficioByCodUfficio(lMessaggio.getChiaveUfficioSiep());

		if (lUfficioAtti.getCodDistretto().equalsIgnoreCase(getCodDistrettoUtenteConnesso())) {
			// Non faccio nulla. I dati sono già a sistema.
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Stessa BDI i dati sono già a sistema");
		} else {
			try {
				// creo una copia
				MessaggioModel lMessIns = new MessaggioModel(lMessaggio);

				IPresaInCaricoJMS lPres = SIEPLookupRemote.getPresaInCarico();

				lPres.ExInserisciFascicoloSiep(lMessIns);
			} catch (F3BException ex) {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.error("Exception >>> " + ex, ex);
				throw new SIUSException(SIUSException.USER_MESSAGE,
						"Si è verificato un errore durante il caricamento dell'Atto pervenuto.<br> Riprovare in seguito.");
			}
		}

		// ==========================================================================
		// Creazione del MESSAGGIO di risposta
		// n.b. la risposta va inviata all'ufficio a cui appartengono gli atti e
		// non all'ufficio mittente che potrebbe averli semplicemente inoltrati
		// ==========================================================================
		UfficioModel lBDI = this.getUfficioByCodUfficio(this.getCodUfficioUtenteConnesso());

		MessaggioModel lMessageRisp = new MessaggioModel();

		UfficioModel lUfficioReply = null;

		if (lMessaggio.getCodUfficioReplyTo() != null) {
			// Il messaggio mi è stato inoltrato. Rispondo all'ufficio indicato
			// nell'apposito campo. In caso di messaggio ricevuto come inoltro, l'id_messaggio
			// della richiesta originale è presente nel campo JMS_CORRELATION_REPLY_TO
			// mentre il campo JMS_CORRELATION_ID_MESSAGE contiene l'ID_MESSAGGIO
			// del mesaggio di inoltro
			lUfficioReply = getUfficioByCodUfficio(lMessaggio.getCodUfficioReplyTo());

			// Indico nella risposta l'ID_MESSAGGIO della RICHIESTA presente sulla
			// BDI di Origine in modo che sia possibile collegare richiesta e risposta
			lMessageRisp.setJmsCorrelationIdMessage(lMessaggio.getJmsCorrelationReplyTo());

		} else {
			// Messaggio Diretto
			lUfficioReply = getUfficioByCodUfficio(lMessaggio.getCodUfficioMittente());

			// Indico nella risposta l'ID_MESSAGGIO della RICHIESTA presente sulla
			// BDI di Origine in modo che sia possibile collegare richiesta e risposta
			lMessageRisp.setJmsCorrelationIdMessage(lMessaggio.getJmsCorrelationIdMessage());
		}

		UfficioModel lUfficioPGCAPDest = getUfficioByCodUfficio(lUfficioReply.getCodDistretto());
		lMessageRisp.setDescrBdiDestinataria(lUfficioPGCAPDest.getDescrComune());
		lMessageRisp.setCodBdiDestinataria(lUfficioReply.getCodDistretto());
		lMessageRisp.setCodUfficioDestinatario(lUfficioReply.getCodUfficio());

		// ==========================================================================

		// Ufficio corrente
		lMessageRisp.setCodBdiMittente(lBDI.getCodDistretto());
		lMessageRisp.setCodUfficioMittente(this.getCodUfficioUtenteConnesso());
		lMessageRisp.setCodiceUtenteMittente(this.getCodUtenteConnesso());

		lMessageRisp.setChiaveAnnoSiep(lMessaggio.getChiaveAnnoSiep());
		lMessageRisp.setChiaveProgrSiep(lMessaggio.getChiaveProgrSiep());
		lMessageRisp.setChiaveUfficioSiep(lMessaggio.getChiaveUfficioSiep());

		// Dati del Soggetto
		lMessageRisp.setNomeSoggetto(lMessaggio.getNomeSoggetto());
		lMessageRisp.setCognomeSoggetto(lMessaggio.getCognomeSoggetto());
		lMessageRisp.setDataNascita(lMessaggio.getDataNascita());
		lMessageRisp.setCodComuneNascita(lMessaggio.getCodComuneNascita());
		lMessageRisp.setCodStatoNascita(lMessaggio.getCodStatoNascita());

		lMessageRisp.setCodTipoMessaggio(ESITO);
		lMessageRisp.setCodTipoOperazione(ESITO_TRASFERIMENTO_COMPETENZA_MS);
		lMessageRisp.setCodEsito(PRESAINCARICO);

		// n.b. le date non vengono prese in considerazione nella spedizione del
		// messaggio jms ma solo per l'aggiornamento locale. NON sono propertyJms
		// Sulla BDI di destinazione, in fase di ricezione, verrà utilizzata
		// come DATA_INVIO/DATA_ESITO quella dello scarico.
		lMessageRisp.setDataInvio(lMessaggio.getDataInvio());
		lMessageRisp.setDataEsito(DateUtils.getSysDate());

		// lMessageRisp.setTreeModel(null);
		// n.b. TreeModel NON può essere null, ma non ha senso rimandare indietro tutto
		// il fascicolo solo per comunicare che è stato preso in carico, quindi
		// rimando in dietro un blob non significativo
		TreeModel lTreeRoot = new TreeModel();
		lMessageRisp.setTreeModel(lTreeRoot);
		// lMess.getTreeModel()
		// =========================================
		// Invio il messaggio di risposta
		// =========================================
		SIAPSender lSender = new SIAPSender();
		lSender.send(lMessageRisp);

		// =============================================
		// Marco il messaggio di !!Richiesta!! evaso.
		// =============================================
		lMessaggio.setFlagVisto("S");
		lMessaggio.setCodEsito(PRESAINCARICO);
		lMessaggio.setDataEsito(DateUtils.getSysDate());
		lCrtl.ExModificaMessaggio(lMessaggio);

		// setta la risposta nella request
		setRequestAttribute(IWebConstants.MESSAGE_TEXT,
				"Ricezione Atto Completata e Esito rispedito al Mittente. Il fascicolo è stato preso in carico!"
						+ "  E' possibile procedere all'iscrizione del procedimento di classe IV");

		// Prepara la "pagina" di destinAction
		RedirectTo lRedirigi = new RedirectTo();
		lRedirigi.setPage(IWebConstants.PG_MAIN);
		lRedirigi.setAction("siap.siep.misurasicurezza.action.ActLoadDettaglioAttoRicevuto");
		lRedirigi.setParameter(ICostantiMessaggio.CAMPO_ID_MESSAGGIO, "" + lIdMess);
		setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi);

		return IWebConstants.PG_MESSAGE;
	}

}