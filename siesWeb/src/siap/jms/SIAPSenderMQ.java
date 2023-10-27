package siap.jms;

import java.math.BigDecimal;
import java.util.Iterator;
import java.util.Vector;

import javax.jms.DeliveryMode;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueSender;
import javax.jms.QueueSession;
import javax.jms.Session;
import javax.naming.NamingException;

import org.apache.log4j.Logger;

import siap.jms.config.JMSProperties;
import siap.jms.connection.ConnectionJMS;
import siap.jms.connection.ConnectionPoolJMS;
import siap.jms.jmscode.model.JmsCodeModel;
import siap.jms.messaggio.controller.IMessaggio;
import siap.jms.messaggio.model.MessaggioModel;
import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.util.StringUtils;

/**
 * <p>
 * Title: SIAPSenderMQ
 * </p>
 * <p>
 * Description: Classe che ha la responsabilità delle spedizioni dei messaggi su JMS
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company:
 * </p>
 */
@SuppressWarnings("rawtypes")
public class SIAPSenderMQ implements ICostantiJMS {

	// 28/05/2019 [EC] - Modifico la destinazione su log PER MEV PROBLEMA CODE INTRODOTTO IN SIES 11.3
	private static Logger siesLogger = Logger.getLogger(LogF3B.JMS_LOG);

	// Connessione dal Pool JMS
	private ConnectionPoolJMS mPoolConnection;
	private QueueSession mSession;
	private QueueSender mQueueSender;
	private Queue mQueue;

	protected SIAPSenderMQ()
	// throws Exception
	{
		// Prende il riferimento all'unica istanza del Connection Pool
	}

	/**
	 * newSIAPSenderMQ
	 * 
	 * @return
	 * @throws F3BException
	 */
	public synchronized static SIAPSenderMQ newSIAPSenderMQ() throws F3BException {
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.info("[JMS]: inizio");
		// mPoolConnection = ConnectionPoolJMS.getInstance();
		SIAPSenderMQ lSiapSend = new SIAPSenderMQ();
		try {
			lSiapSend.initialize();
		} catch (F3BException f3bEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("[JMS]: SIAPSenderMQ.initialize(). Exception = " + f3bEx.getMessage(), f3bEx);
			throw f3bEx;
		} catch (Exception ex) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("[JMS]: SIAPSenderMQ.initialize(). Exception = " + ex.getMessage(), ex);
			throw new F3BException(
					"Errore durante l'inizializzazione del sistema JMS. <br>Riprovare in un secondo momento.");
		}

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.info("[JMS]: fine");
		return lSiapSend;
	}

	/**
	 * Inizializzazione del Pool
	 * 
	 * @throws NamingException
	 * @throws JMSException
	 * @throws Exception
	 */
	protected void initialize() throws NamingException, JMSException, Exception {
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.info("[JMS]: inizio");

		mPoolConnection = ConnectionPoolJMS.getInstance();
		QueueConnection lConnection = mPoolConnection.getConnection();
		try {
			mSession = lConnection.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
		} catch (Exception ex) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("[JMS]: Errore durante la connessione a Message Queue", ex);
			if (ex.getMessage().indexOf("connection is closed") > 0) {
				ConnectionPoolJMS.restart();
				lConnection = mPoolConnection.getConnection();
				mSession = lConnection.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
			} else
				throw new F3BException(
						F3BException.USER_MESSAGE,
						"Il sistema sta provando a prendere una connessione con il sistema JMS. <br> Riprovare la spedizione della richiesta.");

		}

		// Definisco la coda di Spedizione Locale
		String lNameQueue = JMSProperties.getInstance().getProperty(QUEUE_IN_PARTENZA);
		// n.b. la create queue crea una coda solo se non esiste già
		mQueue = mSession.createQueue(lNameQueue);

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.info("[JMS]: fine");
	}

	/**
	 * Metodo per spedire un Messaggio con openJMS. Spedisce i messaggi sempre sulla conda inPartenza del
	 * server JMS locale utilizzando una connessione prelevata dal Connection POOL
	 * 
	 * @param aMessage
	 * @throws Exception
	 */
	public void send(MessaggioModel aSiapMessage) throws Exception {
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.info("[JMS]: inizio Send");

		boolean lStessaBDI = false;
		// Verifica Messaggio da inviare
		aSiapMessage.VerifySingleMessage();

		try {
			BigDecimal lIdMessage = aSiapMessage.getIdMessaggio();

			// Verifico se è un caso di scambio tra stesse BDI
			if (aSiapMessage.getCodBdiMittente().compareTo(aSiapMessage.getCodBdiDestinataria()) == 0)
				lStessaBDI = true;

			// Scrivo il messaggio sulla Tabella MESSAGGIO
			if (((aSiapMessage.getCodTipoMessaggio().equals(RICHIESTA)) || (aSiapMessage
					.getCodTipoMessaggio().equals(RICHIESTA_RICERCA)))) {
				lIdMessage = writeMessage(aSiapMessage);
				aSiapMessage.setIdMessaggio(lIdMessage);
			}

			// Stablisco una Sessione JMS

			// Creo un Object Message
			Message lMessage = mSession.createObjectMessage(aSiapMessage.getTreeModel());

			// Setto le properties del Messaggio partendo dal MessaggioModel
			lMessage.setStringProperty(BDI_DESTINATARIA, aSiapMessage.getDescrBdiDestinataria());
			lMessage.setStringProperty(COD_BDI_DESTINATARIA, aSiapMessage.getCodBdiDestinataria());
			lMessage.setStringProperty(BDI_MITTENTE, aSiapMessage.getDescrBdiMittente());
			lMessage.setStringProperty(COD_BDI_MITTENTE, aSiapMessage.getCodBdiMittente());
			lMessage.setStringProperty(TIPO_OPERAZIONE, aSiapMessage.getCodTipoOperazione());
			lMessage.setStringProperty(TIPO_MESSAGGIO, aSiapMessage.getCodTipoMessaggio());
			lMessage.setStringProperty(UFFICIO_DESTINATARIO, aSiapMessage.getCodUfficioDestinatario());
			lMessage.setStringProperty(UFFICIO_MITTENTE, aSiapMessage.getCodUfficioMittente());
			lMessage.setStringProperty(UTENTE_MITTENTE, aSiapMessage.getCodiceUtenteMittente());
			lMessage.setStringProperty(CHIAVE_ANNO_SIEP,
					StringUtils.toStringJSP(aSiapMessage.getChiaveAnnoSiep(), ""));
			lMessage.setStringProperty(CHIAVE_PROGR_SIEP,
					StringUtils.toStringJSP(aSiapMessage.getChiaveProgrSiep(), ""));
			lMessage.setStringProperty(CHIAVE_UFFICIO_SIEP,
					StringUtils.toStringJSP(aSiapMessage.getChiaveUfficioSiep(), ""));

			lMessage.setStringProperty(CHIAVE_ANNO_SIUS,
					StringUtils.toStringJSP(aSiapMessage.getChiaveAnnoSius(), ""));
			lMessage.setStringProperty(CHIAVE_PROGR_SIUS,
					StringUtils.toStringJSP(aSiapMessage.getChiaveProgrSius(), ""));
			// RESTITUZIONE TRASMISSIONE COMPETENZA
			lMessage.setStringProperty(NOTE, StringUtils.toStringJSP(aSiapMessage.getNote(), ""));
			if (aSiapMessage.getChiaveAnnoFasCumulante() != null)
				lMessage.setStringProperty(CHIAVE_ANNO_FAS_CUMULANTE,
						StringUtils.toStringJSP(aSiapMessage.getChiaveAnnoFasCumulante(), ""));
			if (aSiapMessage.getChiaveProgrFasCumulante() != null)
				lMessage.setStringProperty(CHIAVE_PROGR_FAS_CUMULANTE,
						StringUtils.toStringJSP(aSiapMessage.getChiaveProgrFasCumulante(), ""));
			if (aSiapMessage.getChiaveUfficioFasCumulante() != null)
				lMessage.setStringProperty(CHIAVE_UFFICIO_FAS_CUMULANTE,
						StringUtils.toStringJSP(aSiapMessage.getChiaveUfficioFasCumulante(), ""));
			
			//	MEV_39: TRASMETTO ANCHE DATA_EMISSIONE_CUMULO
			if (aSiapMessage.getDataEmissioneCumulo() != null)
				lMessage.setStringProperty(DATA_EMISSIONE_CUMULO,
						DateUtils.getDateToString(aSiapMessage.getDataEmissioneCumulo(), "dd/MM/yyyy"));

			// UEPE
			if (aSiapMessage.getChiaveAnnoSiepe() != null)
				lMessage.setStringProperty(CHIAVE_ANNO_SIEPE,
						StringUtils.toStringJSP(aSiapMessage.getChiaveAnnoSiepe(), ""));
			if (aSiapMessage.getChiaveProgrSiepe() != null)
				lMessage.setStringProperty(CHIAVE_PROGR_SIEPE,
						StringUtils.toStringJSP(aSiapMessage.getChiaveProgrSiepe(), ""));
			if (aSiapMessage.getNomeSoggetto() != null)
				lMessage.setStringProperty(NOME_SOGGETTO,
						StringUtils.toStringJSP(aSiapMessage.getNomeSoggetto(), ""));
			if (aSiapMessage.getCognomeSoggetto() != null)
				lMessage.setStringProperty(COGNOME_SOGGETTO,
						StringUtils.toStringJSP(aSiapMessage.getCognomeSoggetto(), ""));
			/* Commentato perchè non funziona Luigi 11-08-2006 */
			if (aSiapMessage.getDataNascita() != null)
				lMessage.setStringProperty(DATA_NASCITA,
						DateUtils.getDateToString(aSiapMessage.getDataNascita(), "dd/MM/yyyy"));
			if (aSiapMessage.getCodStatoNascita() != null)
				lMessage.setStringProperty(COD_STATO_NASCITA,
						StringUtils.toStringJSP(aSiapMessage.getCodStatoNascita(), ""));
			if (aSiapMessage.getCodComuneNascita() != null)
				lMessage.setStringProperty(COD_COMUNE_NASCITA,
						StringUtils.toStringJSP(aSiapMessage.getCodComuneNascita(), ""));

			// ========================================================================
			// if (aSiapMessage.getDeliveryMode() != null)
			// lMessage.setStringProperty(DELIVERY_MODE, aSiapMessage.getDeliveryMode() );

			if (aSiapMessage.getCodUfficioInoltro() != null)
				lMessage.setStringProperty(COD_UFFICIO_INOLTRO, aSiapMessage.getCodUfficioInoltro());

			if (aSiapMessage.getCodBdiInoltro() != null)
				lMessage.setStringProperty(COD_BDI_INOLTRO, aSiapMessage.getCodBdiInoltro());

			if (aSiapMessage.getCodUfficioReplyTo() != null)
				lMessage.setStringProperty(COD_UFFICIO_REPLY_TO, aSiapMessage.getCodUfficioReplyTo());

			if (aSiapMessage.getCodBdiReplyTo() != null)
				lMessage.setStringProperty(COD_BDI_REPLY_TO, aSiapMessage.getCodBdiReplyTo());

			if (aSiapMessage.getJmsCorrelationReplyTo() != null)
				lMessage.setStringProperty(JMS_CORRELATION_REPLY_TO, aSiapMessage.getJmsCorrelationReplyTo());

			if (aSiapMessage.getIdMessaggioSollecitato() != null)
				lMessage.setStringProperty(ID_MESSAGGIO_SOLLECITATO, aSiapMessage.getIdMessaggioSollecitato());
			// ========================================================================

			if (aSiapMessage.getIdRichiesta() != null)
				lMessage.setStringProperty(ID_RICHIESTA, aSiapMessage.getIdRichiesta().toString());

			if (lStessaBDI)
				lMessage.setBooleanProperty(STESSA_BDI, true);

			// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			// siesLogger.info("ESITOOOOOIIIIIOOO" +aSiapMessage.getCodEsito());

			if (aSiapMessage.getCodEsito() != null && aSiapMessage.getCodEsito().length() > 1)
				lMessage.setStringProperty(COD_ESITO, aSiapMessage.getCodEsito());
			else
				lMessage.setStringProperty(COD_ESITO, "-");

			lMessage.setStringProperty(UFFICIO_MITTENTE, aSiapMessage.getCodUfficioMittente());

			lMessage.setJMSDeliveryMode(DeliveryMode.PERSISTENT);

			// Setto l'ID del FIle inserito sul DB
			if ((aSiapMessage.getCodTipoMessaggio().compareTo(ESITO) == 0)
					|| (aSiapMessage.getCodTipoMessaggio().compareTo(ESITO_RICERCA) == 0)) {
				lMessage.setStringProperty(ID_MESSAGGIO, aSiapMessage.getJmsCorrelationIdMessage());

				if (aSiapMessage.getIdMessaggio() != null)
					lMessage.setStringProperty(ID_MESSAGGIO, aSiapMessage.getIdMessaggio().toString());

				lMessage.setStringProperty(CORRELATION_ID_MESSAGGIO,
						aSiapMessage.getJmsCorrelationIdMessage());
			} else // RIchiesta
			{
				if (lIdMessage != null) {
					lMessage.setStringProperty(ID_MESSAGGIO, lIdMessage.toString());
					lMessage.setStringProperty(CORRELATION_ID_MESSAGGIO, lIdMessage.toString());
				}
			}

			// Definisco un Queue Sender
			mQueueSender = mSession.createSender(mQueue);
			// Spedizione
			mQueueSender.send(lMessage);

			// Update Messaggio su tabella MESSAGGIO
			aSiapMessage.setJmsIdMessaggio(lMessage.getJMSMessageID());

			if ((lStessaBDI)
					&& (aSiapMessage.getCodTipoMessaggio().equals(RICHIESTA) || aSiapMessage
							.getCodTipoMessaggio().equals(RICHIESTA_RICERCA))) {
				aSiapMessage.setJmsCorrelationIdMessage(lIdMessage.toString());
			}
			// Update del Messaggio LOCALE
			updateMessage(aSiapMessage);

			// qSession.close(); // Aggiunto da Diego per liberare la memoria
			// Closes the session.
			// Since a provider may allocate some resources on behalf of a session
			// outside the JVM, clients should close the resources when they are not
			// needed. Relying on garbage collection to eventually reclaim these resources
			// may not be timely enough.
			// --- Faccio partire i Listner sulle code---
			SIAPReceiver.getInstance();
			mQueueSender.close();
			// Si rilascia la Connessione
			// mPoolConnection.releaseConnection(lQueueConn);
		} catch (JMSException jmsEx) {
			// mPoolConnection.releaseConnection(lQueueConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("[JMS]: SIAPSender.send():Exception = " + jmsEx.getErrorCode());
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("[JMS]: SIAPSender.send():Exception = ", jmsEx);

			jmsEx.printStackTrace();
			// Si cancella il Messaggio inserito in Tabella MEssaggio
			deleteMessage(aSiapMessage);
			try {
				mQueueSender.close();
			} catch (Exception e) {
			}

			throw new F3BException("Eccezione durante la spedizione del messaggio: " + jmsEx);
		} catch (Exception exception) {
			// mPoolConnection.releaseConnection(lQueueConn);
			exception.printStackTrace();
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("[JMS]: SIAPSender.send():Exception = " + exception);
			if (exception.toString().equals("java.lang.NullPointerException"))
				exception.printStackTrace();
			// Si cancella il Messaggio inserito in Tabella MEssaggio
			deleteMessage(aSiapMessage);

			mQueueSender.close();
			throw new F3BException("Eccezione durante la spedizione del messaggio: " + exception);
		}
		//
		// Nota : 2010-11-02, introdurre successivamente la clausola finally per la mQueueSender.close();
		//
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.info("[JMS]: fine Send");
	}

	/**
	 * Metodo per spedire un Messaggio con openJMS a diverse BDI Metodo utilizzato per la ricerca su piu' BDI
	 * 
	 * @param aMessage
	 * @throws Exception
	 */
	public void sendToMultipleBDI(MessaggioModel aSiapMessage) throws Exception {
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.info("[JMS]: inizio");

		boolean lStessaBDI = false;
		// COnnessione JMS
//		QueueConnection lQueueConn = null;

		// Verifica la correttezza del messaggio da inviare
		aSiapMessage.VerifyMessage();

		try {
			BigDecimal lIdMessage = aSiapMessage.getIdMessaggio();

			// Verifico se è un caso di scambio tra stesse BDI
			if (aSiapMessage.getCodBdiMittente().compareTo(aSiapMessage.getCodBdiDestinataria()) == 0)
				lStessaBDI = true;

			// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			// siesLogger.debug("lQueueConn = "+lQueueConn.getClientID());

			// Creo un Object Message
			Message lMessage = mSession.createObjectMessage(aSiapMessage.getTreeModel());

			// While sulle BDI

			Vector allBDI = JMSProperties.getInstance().getAllBDI();
			Iterator lItx = allBDI.iterator();
			aSiapMessage.setCodBdiDestinataria(COD_TUTTE);
			aSiapMessage.setDescrBdiDestinataria(TUTTE);
			lIdMessage = writeMessage(aSiapMessage);
			aSiapMessage.setIdMessaggio(lIdMessage);
			mQueueSender = mSession.createSender(mQueue);
			int count = 0;

			while (lItx.hasNext()) {
				// Setto le properties del Messaggio partendo dal MessaggioModel
				JmsCodeModel lCodBDI = (JmsCodeModel) lItx.next();
				// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				// siesLogger.info("\nCodMittente = " + aSiapMessage.getCodBdiMittente());
				// Se non si tratta della BDI MIttente spedisco il messaggio
				if (!lCodBDI.getCodice().equals(aSiapMessage.getCodBdiMittente())) {
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger.info("[JMS]: Giro numero " + count + " --- Spedisco a : "
							+ lCodBDI.getCodice() + " " + lCodBDI.getDescrizione() + "- Id Messaggio = "
							+ lIdMessage);

					// Setto tutti i parametri del messaggio da inviare
					lMessage.setStringProperty(BDI_DESTINATARIA, lCodBDI.getDescrizione());
					lMessage.setStringProperty(COD_BDI_DESTINATARIA, lCodBDI.getCodice());
					lMessage.setStringProperty(BDI_MITTENTE, aSiapMessage.getDescrBdiMittente());
					lMessage.setStringProperty(COD_BDI_MITTENTE, aSiapMessage.getCodBdiMittente());
					lMessage.setStringProperty(TIPO_OPERAZIONE, aSiapMessage.getCodTipoOperazione());
					lMessage.setStringProperty(TIPO_MESSAGGIO, aSiapMessage.getCodTipoMessaggio());
					// -- NON serve ufficio per RICERCA lMessage.setStringProperty(UFFICIO_DESTINATARIO,
					// aSiapMessage.getCodUfficioDestinatario());
					lMessage.setStringProperty(UFFICIO_MITTENTE, aSiapMessage.getCodUfficioMittente());
					lMessage.setStringProperty(UTENTE_MITTENTE, aSiapMessage.getCodiceUtenteMittente());
					lMessage.setStringProperty(COD_ESITO, "-");
					lMessage.setStringProperty(CHIAVE_ANNO_SIEP,
							StringUtils.toStringJSP(aSiapMessage.getChiaveAnnoSiep(), ""));
					lMessage.setStringProperty(CHIAVE_PROGR_SIEP,
							StringUtils.toStringJSP(aSiapMessage.getChiaveProgrSiep(), ""));
					lMessage.setStringProperty(CHIAVE_UFFICIO_SIEP,
							StringUtils.toStringJSP(aSiapMessage.getChiaveUfficioSiep(), ""));

					lMessage.setStringProperty(CHIAVE_ANNO_SIUS,
							StringUtils.toStringJSP(aSiapMessage.getChiaveAnnoSius(), ""));
					lMessage.setStringProperty(CHIAVE_PROGR_SIUS,
							StringUtils.toStringJSP(aSiapMessage.getChiaveProgrSius(), ""));
					// UEPE
					lMessage.setStringProperty(CHIAVE_ANNO_SIEPE,
							StringUtils.toStringJSP(aSiapMessage.getChiaveAnnoSiepe(), ""));
					lMessage.setStringProperty(CHIAVE_PROGR_SIEPE,
							StringUtils.toStringJSP(aSiapMessage.getChiaveProgrSiepe(), ""));
					lMessage.setStringProperty(NOME_SOGGETTO,
							StringUtils.toStringJSP(aSiapMessage.getNomeSoggetto(), ""));
					lMessage.setStringProperty(COGNOME_SOGGETTO,
							StringUtils.toStringJSP(aSiapMessage.getCognomeSoggetto(), ""));
					if (aSiapMessage.getDataNascita() != null)
						lMessage.setStringProperty(DATA_NASCITA,
								StringUtils.toStringJSP(aSiapMessage.getDataNascita().toString(), ""));
					lMessage.setStringProperty(COD_STATO_NASCITA,
							StringUtils.toStringJSP(aSiapMessage.getCodStatoNascita(), ""));
					lMessage.setStringProperty(COD_COMUNE_NASCITA,
							StringUtils.toStringJSP(aSiapMessage.getCodComuneNascita(), ""));

					if (lStessaBDI)
						lMessage.setBooleanProperty(STESSA_BDI, true);

					lMessage.setJMSDeliveryMode(DeliveryMode.PERSISTENT);
					lMessage.setStringProperty(ID_MESSAGGIO, lIdMessage.toString());
					lMessage.setStringProperty(CORRELATION_ID_MESSAGGIO, lIdMessage.toString());

					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger.info("[JMS]: Messaggio da spedire (lMessage) = " + lMessage);

					try {
						// Definisco un Queue Sender

						// Spedizione
						mQueueSender.send(lMessage);
						// ---Il messaggio locale non deve essere ulteriormente toccato---
						// aSiapMessage.setJmsIdMessaggio(lMessage.getJMSMessageID());
						// Update del Messaggio LOCALE
						// updateMessage(aSiapMessage);
					} catch (Exception ex) {
						// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto
						// di LogF3B.getLogger()
						siesLogger.error("Exception nel while di spedizione a più BDI per"
								+ lCodBDI.getDescrizione() + "..." + ex.getMessage().toString()
						// + "   " +ex.getCause().toString()
						// + "   " + ex.getStackTrace().toString()
								);
					}
					count++;

				}
				// --- Faccio partire i Listner sulle code---
				SIAPReceiver.getInstance();
				// Mando una sveglia ai listner remoti
				// /mQueueSender.close();

			} // / End While
			mQueueSender.close();
			// Si rilascia la Connessione
			// wakeUpListner();
			// mPoolConnection.releaseConnection(lQueueConn);
		} catch (JMSException jmsEx) {
			// mPoolConnection.releaseConnection(lQueueConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("[JMS]: SIAPSender.sendToMultipleBDI() " + jmsEx.getErrorCode());
			jmsEx.printStackTrace();
			// Si cancella il Messaggio inserito in Tabella MEssaggio
			deleteMessage(aSiapMessage);
			mQueueSender.close();
			throw new F3BException("Eccezione durante la spedizione del messaggio: " + jmsEx);
		} catch (Exception exception) {
			// mPoolConnection.releaseConnection(lQueueConn);
			exception.printStackTrace();
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("[JMS]: SIAPSender.sendToMultipleBDI() " + exception);
			// Si cancella il Messaggio inserito in Tabella MEssaggio
			deleteMessage(aSiapMessage);
			mQueueSender.close();
			throw new F3BException("Eccezione durante la spedizione del messaggio: " + exception);
		}

		//
		// Nota : 2010-11-02, introdurre successivamente la clausola finally per la mQueueSender.close();
		//

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.info("[JMS]: fine");
	}

	/**
	 * Metodo usato per spedire i Messaggi all'effettivo desitnatario e non alla coda locale di Partenza
	 * 
	 * @param aMessage
	 * @throws Exception
	 */
	public void sendToTrueDestination(Message aMessage) throws Exception {
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.info("[JMS]: inizio");

		QueueConnection lQueueConn = null;

		try {
			// Stablisco una Sessione
			ConnectionJMS lConn = new ConnectionJMS();

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger
					.info("[JMS]: Messaggio da spedire a = " + aMessage.getStringProperty(BDI_DESTINATARIA));

			lQueueConn = lConn.getConnection(aMessage.getStringProperty(BDI_DESTINATARIA));

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.info("[JMS]: dopo getConnection");

			QueueSession qSession = lQueueConn.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.info("[JMS]: dopo createQueueSession");
			// Definisco la coda di Spedizione di Arrivo dell'OpenJMS Remoto
			String lNameQueue = JMSProperties.getInstance().getProperty(QUEUE_IN_ARRIVO);
			Queue queue = qSession.createQueue(lNameQueue);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.info("[JMS]: dopo createQueue");

			if (queue != null) {
				// Definisco un Queue Sender
				QueueSender qSender = qSession.createSender(queue);
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.info("[JMS]: dopo createSender");

				if (qSender != null) {
					// Spedisci
					qSender.send(aMessage);
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger.info("dopo send");
				}
			}

			// 2010-10-12 : Commentato in fase di test, vedi note funzione checkTimeOut() in SIAPReceiver.java
			// wakeUp(aMessage.getStringProperty(BDI_DESTINATARIA)); // Capire se si può omettere 2010-10-11
			// Paolo...

			// qSession.close(); // add diego
			// Si rilascia la Connessione
			lQueueConn.close();
		} catch (Exception exception) {
			exception.printStackTrace();
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("[JMS]: SIAPSender.send():Exception = " + exception);
			lQueueConn.close();
			throw new Exception("Exception in processign request " + exception);
		}
		//
		// Nota : 2010-11-02, introdurre, successivamente, la clausola finally per la lQueueConn.close();
		//

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.info("[JMS]: fine");
	}

	/**
	 * Metodo usato per rispedire i Messaggi all'effettivo desitnatario e non alla coda locale di Partenza.
	 * 
	 * @param aMessage
	 * @throws Exception
	 */
	public void sendToTrueDestination(MessaggioModel aMessage) throws Exception {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.info("[JMS]: inizio");

		QueueConnection lQueueConn = null;

		try {
			// Stablisco una Sessione
			ConnectionJMS lConn = new ConnectionJMS();

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.info("[JMS]: Messaggio da rispedire a = " + aMessage.getDescrBdiDestinataria());

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.info("[JMS]: Messaggio da rispedire (aMessage) = " + aMessage);

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.info("[JMS]: JMSCorrelationID (aMessage) = " + aMessage.getJmsCorrelationIdMessage());

			lQueueConn = lConn.getConnection(aMessage.getDescrBdiDestinataria());
			QueueSession qSession = lQueueConn.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);

			// Definisco la coda di Spedizione di Arrivo dell'OpenJMS Remoto
			String lNameQueue = JMSProperties.getInstance().getProperty(QUEUE_IN_ARRIVO);
			Queue queue = qSession.createQueue(lNameQueue);

			Message lMessage = qSession.createObjectMessage(aMessage.getTreeModel());

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.info("[JMS]: II Step Messaggio da rispedire (lMessage) = " + lMessage);

			// Setto le properties del Messaggio partendo dal MessaggioModel
			// Ticket#20231024011 - scriveva erroneamente come BDI destinatari la BDI mittente
      //lMessage.setStringProperty(BDI_DESTINATARIA, aMessage.getDescrBdiMittente());
      //lMessage.setStringProperty(COD_BDI_DESTINATARIA, aMessage.getCodBdiMittente());
      lMessage.setStringProperty(BDI_DESTINATARIA, aMessage.getDescrBdiDestinataria());
      lMessage.setStringProperty(COD_BDI_DESTINATARIA, aMessage.getCodBdiDestinataria());
      // Ticket#20231024011 - FINE
      
			lMessage.setStringProperty(BDI_MITTENTE, aMessage.getDescrBdiMittente());
			lMessage.setStringProperty(COD_BDI_MITTENTE, aMessage.getCodBdiMittente());
			lMessage.setStringProperty(TIPO_OPERAZIONE, aMessage.getCodTipoOperazione());
			lMessage.setStringProperty(TIPO_MESSAGGIO, aMessage.getCodTipoMessaggio());
			lMessage.setStringProperty(UFFICIO_DESTINATARIO, aMessage.getCodUfficioDestinatario());
			lMessage.setStringProperty(UFFICIO_MITTENTE, aMessage.getCodUfficioMittente());
			lMessage.setStringProperty(UTENTE_MITTENTE, aMessage.getCodiceUtenteMittente());
			
			//================================================================================================================
			// Ticket#20231024011 - Vanno aggiunti anche gli altri campi della tabella messaggio
			
      // Identificatovo del fascicolo SIEP			
      lMessage.setStringProperty(CHIAVE_ANNO_SIEP    ,StringUtils.toStringJSP(aMessage.getChiaveAnnoSiep(), ""));
      lMessage.setStringProperty(CHIAVE_PROGR_SIEP   ,StringUtils.toStringJSP(aMessage.getChiaveProgrSiep(), ""));
      lMessage.setStringProperty(CHIAVE_UFFICIO_SIEP ,StringUtils.toStringJSP(aMessage.getChiaveUfficioSiep(), ""));

      // Identificatovo del fascicolo SIUS
      lMessage.setStringProperty(CHIAVE_ANNO_SIUS,  StringUtils.toStringJSP(aMessage.getChiaveAnnoSius(), ""));
      lMessage.setStringProperty(CHIAVE_PROGR_SIUS, StringUtils.toStringJSP(aMessage.getChiaveProgrSius(), ""));

      // RESTITUZIONE TRASMISSIONE COMPETENZA
      lMessage.setStringProperty(NOTE, StringUtils.toStringJSP(aMessage.getNote(), ""));
      
      // Identificatovo del fascicolo SIEP CUMULANTE
      if (aMessage.getChiaveAnnoFasCumulante() != null)
        lMessage.setStringProperty(CHIAVE_ANNO_FAS_CUMULANTE,StringUtils.toStringJSP(aMessage.getChiaveAnnoFasCumulante(), ""));
      if (aMessage.getChiaveProgrFasCumulante() != null)
        lMessage.setStringProperty(CHIAVE_PROGR_FAS_CUMULANTE,StringUtils.toStringJSP(aMessage.getChiaveProgrFasCumulante(), ""));
      if (aMessage.getChiaveUfficioFasCumulante() != null)
        lMessage.setStringProperty(CHIAVE_UFFICIO_FAS_CUMULANTE,StringUtils.toStringJSP(aMessage.getChiaveUfficioFasCumulante(), ""));
      
      // UEPE
      if (aMessage.getChiaveAnnoSiepe() != null)
        lMessage.setStringProperty(CHIAVE_ANNO_SIEPE,StringUtils.toStringJSP(aMessage.getChiaveAnnoSiepe(), ""));
      if (aMessage.getChiaveProgrSiepe() != null)
        lMessage.setStringProperty(CHIAVE_PROGR_SIEPE,StringUtils.toStringJSP(aMessage.getChiaveProgrSiepe(), ""));
      
      // SOGGETO
      if (aMessage.getNomeSoggetto() != null)
        lMessage.setStringProperty(NOME_SOGGETTO,StringUtils.toStringJSP(aMessage.getNomeSoggetto(), ""));
      if (aMessage.getCognomeSoggetto() != null)
        lMessage.setStringProperty(COGNOME_SOGGETTO,StringUtils.toStringJSP(aMessage.getCognomeSoggetto(), ""));
      if (aMessage.getDataNascita() != null)
        lMessage.setStringProperty(DATA_NASCITA,DateUtils.getDateToString(aMessage.getDataNascita(), "dd/MM/yyyy"));
      if (aMessage.getCodStatoNascita() != null)
        lMessage.setStringProperty(COD_STATO_NASCITA,StringUtils.toStringJSP(aMessage.getCodStatoNascita(), ""));
      if (aMessage.getCodComuneNascita() != null)
        lMessage.setStringProperty(COD_COMUNE_NASCITA,StringUtils.toStringJSP(aMessage.getCodComuneNascita(), ""));

      // INOLTRO
      if (aMessage.getCodUfficioInoltro() != null)
        lMessage.setStringProperty(COD_UFFICIO_INOLTRO, aMessage.getCodUfficioInoltro());
      if (aMessage.getCodBdiInoltro() != null)
        lMessage.setStringProperty(COD_BDI_INOLTRO, aMessage.getCodBdiInoltro());
      
      // REPLY TO
      if (aMessage.getCodUfficioReplyTo() != null)
        lMessage.setStringProperty(COD_UFFICIO_REPLY_TO, aMessage.getCodUfficioReplyTo());
      if (aMessage.getCodBdiReplyTo() != null)
        lMessage.setStringProperty(COD_BDI_REPLY_TO, aMessage.getCodBdiReplyTo());
      if (aMessage.getJmsCorrelationReplyTo() != null)
        lMessage.setStringProperty(JMS_CORRELATION_REPLY_TO, aMessage.getJmsCorrelationReplyTo());

      if (aMessage.getIdMessaggioSollecitato() != null)
        lMessage.setStringProperty(ID_MESSAGGIO_SOLLECITATO, aMessage.getIdMessaggioSollecitato());

      if (aMessage.getIdRichiesta() != null)
        lMessage.setStringProperty(ID_RICHIESTA, aMessage.getIdRichiesta().toString());				
      
      if (aMessage.getCodEsito() != null && aMessage.getCodEsito().length() > 1)
        lMessage.setStringProperty(COD_ESITO, aMessage.getCodEsito());
      else
        lMessage.setStringProperty(COD_ESITO, "-");
      
      // Verifico se è un caso di scambio tra stesse BDI
      if (aMessage.getCodBdiMittente().compareTo(aMessage.getCodBdiDestinataria()) == 0)
        lMessage.setBooleanProperty(STESSA_BDI, true);
      
      // Ticket#20231024011 - FINE =====================================================================================		
			
			lMessage.setStringProperty(CORRELATION_ID_MESSAGGIO, aMessage.getJmsCorrelationIdMessage());
			// 2010-11-01 : da rimuovere... 2010-10-29 Aggiunta riga per valorizzare il correlation message (
			// prova ... )
			// lMessage.setJMSCorrelationID(aMessage.getJmsCorrelationIdMessage());

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.info("[JMS]: III JMSCorrelationID (lMessage) = " + lMessage.getJMSCorrelationID());

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.info("[JMS]: III Step Messaggio da rispedire (lMessage) = " + lMessage);

			if (queue != null) {
				// Definisco un Queue Sender
				QueueSender qSender = qSession.createSender(queue);

				if (qSender != null) {
					// Spedisci
					qSender.send(lMessage);
				}
			}
			// Si rilascia la Connessione
			// wakeUp(aMessage.getDescrBdiDestinataria());

			// lQueueConn.close(); // 2010-11-03 Commentata poichè si è introdotta la clausola finally.
		} catch (Exception exception) {
			exception.printStackTrace();
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("[JMS]: SIAPSender.send():Exception = " + exception);
			// lQueueConn.close(); // 2010-11-03 Commentata poichè si è introdotta la clausola finally.
			throw new Exception("Exception in processign request " + exception);
		}
		//
		// Nota : 2010-11-02, introdurre successivamente la clausola finally per la lQueueConn.close();
		//
		finally {
			if (lQueueConn != null)
				lQueueConn.close();
		}

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.info("[JMS]: fine");
	}

	/**
	 * inserisce nella Tabella Messaggio il MEssaggio da spedire
	 * 
	 * @param aMessage
	 *            deainserire
	 * @return id del messaggio
	 * @throws Exception
	 */
	private BigDecimal writeMessage(MessaggioModel aMessage) throws Exception {
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.info("[JMS]: inizio");

		try {
			MessaggioModel lMessModel = new MessaggioModel(aMessage);

			if (lMessModel.getCodEsito() != null) {
				if (lMessModel.getCodEsito().length() <= 1)
					lMessModel.setCodEsito("-");
				else
					lMessModel.setCodEsito(aMessage.getCodEsito());
			} else
				lMessModel.setCodEsito("-");

			lMessModel.setFlagVisto("N");

			IMessaggio lCtrMess = JMSLookupRemote.getMessaggioRemote();
			MessaggioModel lInd = lCtrMess.ExInserisciMessaggio(lMessModel);

			return lInd.getIdMessaggio();
		} catch (Exception ex) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("[JMS]: " + getClass().getName(), ex);
			throw ex;
		} finally {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.info("[JMS]: fine");
		}
	}

	/**
	 * Update dell' ID JMS spedito
	 * 
	 * @param aMessage
	 * @throws Exception
	 */
	private void updateMessage(MessaggioModel aMessage) throws Exception {
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.info("[JMS]: inizio Update");

		try {
			IMessaggio lCtrMess = JMSLookupRemote.getMessaggioRemote();
			lCtrMess.ExModificaMessaggioJMSId(aMessage);

		} catch (Exception ex) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error(getClass().getName() + "", ex);
			throw ex;
		}

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.info("[JMS]: fine");

	}

	/**
	 * Delete il messaggio che non è stato spedito
	 * 
	 * @param aMessage
	 * @throws Exception
	 */
	private void deleteMessage(MessaggioModel aMessage) throws Exception {
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.info("[JMS]: inizio");

		try {
			IMessaggio lCtrMess = JMSLookupRemote.getMessaggioRemote();
			lCtrMess.ExCancellaMessaggio(aMessage.getIdMessaggio());
		} catch (Exception ex) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error(getClass().getName() + "", ex);
			throw ex;
		}

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.info("[JMS]: fine");
	}

	// 2010-10-11 : wakeup necessario ? mah...
//	private void wakeUpListner() {
//		String lBdi = "";
//		// While sulle BDI
//		try {
//			Vector allBDI = JMSProperties.getInstance().getAllBDI();
//			Iterator lItx = allBDI.iterator();
//
//			// SiapHttpClient lClient = new SiapHttpClient();
//
//			while (lItx.hasNext()) {
//				JmsCodeModel lCodBDI = (JmsCodeModel) lItx.next();
//				lBdi = lCodBDI.getDescrizione();
//
//				wakeUp(lBdi);
//
//				/*
//				 * String lUrl = JMSProperties.getInstance().getConnectionString(lBdi); try { // [FT] -
//				 * 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
//				 * LogF3B.getLogger() siesLogger.info("Richiamo via Http l'URL: " + lUrl +
//				 * "ServletJMSWakeUp"); lClient.postMethod(lUrl + "/ServletJMSWakeUp", " "); // [FT] -
//				 * 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
//				 * LogF3B.getLogger() siesLogger.info("Chiamato l'URL: " + lUrl + "/ServletJMSWakeUp"); }
//				 * catch (Exception ex) { // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza
//				 * siesLogger al posto di LogF3B.getLogger() siesLogger.info("Exception nel wakeUpListner a "
//				 * + lBdi); // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al
//				 * posto di LogF3B.getLogger() siesLogger.info("Errore: " + ex.getMessage() +
//				 * ex.getStackTrace()[0]); }
//				 */
//			}
//
//		} catch (Exception ex) {
//			ex.printStackTrace();
//			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
//			// LogF3B.getLogger()
//			siesLogger.info("[JMS]: Exception nel wakeUpListner a " + lBdi);
//		}
//	}

	/**
	 * Metodo per attivare una singola BDI
	 * 
	 * @param lNomeBDI
	 */

	// NOTE : 2010-10-11 Eliminerei questo metodo e tutti i suoi riferimenti.
	// Precisamente, eliminerei la parte afferente alla servlet di wakeup, non penso
	// che possa essere utile, anzi...
//	private void wakeUp(String lNomeBDI) {
//		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
//		// LogF3B.getLogger()
//		siesLogger.info("[JMS]: inizio");
//
//		String lBdi = "";
//		// While sulle BDI
//		try {
//			SiapHttpClient lClient = new SiapHttpClient();
//
//			String lUrl = JMSProperties.getInstance().getConnectionString(lNomeBDI);
//
//			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
//			// LogF3B.getLogger()
//			siesLogger.info("[JMS]: Richiamo via Http l'URL: " + lUrl + "ServletJMSWakeUp");
//			lClient.postMethod(lUrl + "ServletJMSWakeUp", " ");
//			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
//			// LogF3B.getLogger()
//			siesLogger.info("[JMS]: Chiamato l'URL: " + lUrl + "ServletJMSWakeUp");
//		} catch (Exception ex) {
//			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
//			// LogF3B.getLogger()
//			siesLogger.info("[JMS]: Exception nel wakeUpListner a " + lBdi);
//			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
//			// LogF3B.getLogger()
//			siesLogger.info("[JMS]: Errore: " + ex.getMessage());
//			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
//			// LogF3B.getLogger()
//			siesLogger.info("[JMS]: " + ex.getStackTrace()[0]);
//		}
//
//		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
//		// LogF3B.getLogger()
//		siesLogger.info("[JMS]: fine");
//
//	}
	
	
	/**
	 *  AGGIUNGO METODO PER MEV PROBLEMA CODE 
	 *  
	 * @param aSiapMessage
	 * @throws Exception
	 */
	public void sendError(MessaggioModel aSiapMessage)  throws Exception {
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.info("[JMS]: inizio sendError");

		boolean lStessaBDI = false;
		// Verifica Messaggio da inviare
		//aSiapMessage.VerifySingleMessage();

		try {
			BigDecimal lIdMessage = aSiapMessage.getIdMessaggio();

			// Verifico se è un caso di scambio tra stesse BDI
			if (aSiapMessage.getCodBdiMittente().compareTo(aSiapMessage.getCodBdiDestinataria()) == 0)
				lStessaBDI = true;

			// Scrivo il messaggio sulla Tabella MESSAGGIO
			if (((aSiapMessage.getCodTipoMessaggio().equals(RICHIESTA)) || (aSiapMessage
					.getCodTipoMessaggio().equals(RICHIESTA_RICERCA)))) {
				lIdMessage = writeMessage(aSiapMessage);
				aSiapMessage.setIdMessaggio(lIdMessage);
			}
	

			// Stablisco una Sessione JMS

			// Creo un Object Message
			Message lMessage = mSession.createObjectMessage();

			// Setto le properties del Messaggio partendo dal MessaggioModel
			lMessage.setStringProperty(BDI_DESTINATARIA, aSiapMessage.getDescrBdiDestinataria());
			lMessage.setStringProperty(COD_BDI_DESTINATARIA, aSiapMessage.getCodBdiDestinataria());
			lMessage.setStringProperty(BDI_MITTENTE, aSiapMessage.getDescrBdiMittente());
			lMessage.setStringProperty(COD_BDI_MITTENTE, aSiapMessage.getCodBdiMittente());
			lMessage.setStringProperty(TIPO_OPERAZIONE, aSiapMessage.getCodTipoOperazione());
			lMessage.setStringProperty(TIPO_MESSAGGIO, aSiapMessage.getCodTipoMessaggio());
			lMessage.setStringProperty(UFFICIO_DESTINATARIO, aSiapMessage.getCodUfficioDestinatario());
			lMessage.setStringProperty(UFFICIO_MITTENTE, aSiapMessage.getCodUfficioMittente());
			lMessage.setStringProperty(UTENTE_MITTENTE, aSiapMessage.getCodiceUtenteMittente());
		

			if (aSiapMessage.getCodUfficioInoltro() != null)
				lMessage.setStringProperty(COD_UFFICIO_INOLTRO, aSiapMessage.getCodUfficioInoltro());

			if (aSiapMessage.getCodBdiInoltro() != null)
				lMessage.setStringProperty(COD_BDI_INOLTRO, aSiapMessage.getCodBdiInoltro());

			if (aSiapMessage.getCodUfficioReplyTo() != null)
				lMessage.setStringProperty(COD_UFFICIO_REPLY_TO, aSiapMessage.getCodUfficioReplyTo());

			if (aSiapMessage.getCodBdiReplyTo() != null)
				lMessage.setStringProperty(COD_BDI_REPLY_TO, aSiapMessage.getCodBdiReplyTo());

			if (aSiapMessage.getJmsCorrelationReplyTo() != null)
				lMessage.setStringProperty(JMS_CORRELATION_REPLY_TO, aSiapMessage.getJmsCorrelationReplyTo());

			if (aSiapMessage.getIdMessaggioSollecitato() != null)
				lMessage.setStringProperty(ID_MESSAGGIO_SOLLECITATO, aSiapMessage.getIdMessaggioSollecitato());
			// ========================================================================

			if (aSiapMessage.getIdRichiesta() != null)
				lMessage.setStringProperty(ID_RICHIESTA, aSiapMessage.getIdRichiesta().toString());

			if (lStessaBDI)
				lMessage.setBooleanProperty(STESSA_BDI, true);
		

			if (aSiapMessage.getCodEsito() != null && aSiapMessage.getCodEsito().length() > 1)
				lMessage.setStringProperty(COD_ESITO, aSiapMessage.getCodEsito());
			else
				lMessage.setStringProperty(COD_ESITO, ICostantiJMS.ERRORE_DEPLOY);

			lMessage.setStringProperty(UFFICIO_MITTENTE, aSiapMessage.getCodUfficioMittente());

			lMessage.setJMSDeliveryMode(DeliveryMode.PERSISTENT);

			// Setto l'ID del FIle inserito sul DB
			if ((aSiapMessage.getCodTipoMessaggio().compareTo(ESITO) == 0)
					|| (aSiapMessage.getCodTipoMessaggio().compareTo(ESITO_RICERCA) == 0)) {
				lMessage.setStringProperty(ID_MESSAGGIO, aSiapMessage.getJmsCorrelationIdMessage());

				if (aSiapMessage.getIdMessaggio() != null)
					lMessage.setStringProperty(ID_MESSAGGIO, aSiapMessage.getIdMessaggio().toString());

				lMessage.setStringProperty(CORRELATION_ID_MESSAGGIO,
						aSiapMessage.getJmsCorrelationIdMessage());
			} else // RIchiesta
			{
				if (lIdMessage != null) {
					lMessage.setStringProperty(ID_MESSAGGIO, lIdMessage.toString());
					lMessage.setStringProperty(CORRELATION_ID_MESSAGGIO, lIdMessage.toString());
				}
			}
			
			

			// Definisco un Queue Sender
			mQueueSender = mSession.createSender(mQueue);
			// Spedizione
			mQueueSender.send(lMessage);

			// Update Messaggio su tabella MESSAGGIO
			aSiapMessage.setJmsIdMessaggio(lMessage.getJMSMessageID());

			if ((lStessaBDI)
					&& (aSiapMessage.getCodTipoMessaggio().equals(RICHIESTA) || aSiapMessage
							.getCodTipoMessaggio().equals(RICHIESTA_RICERCA))) {
				aSiapMessage.setJmsCorrelationIdMessage(lIdMessage.toString());
			}
			// Update del Messaggio LOCALE
			updateMessage(aSiapMessage);

			// qSession.close(); // Aggiunto da Diego per liberare la memoria
			// Closes the session.
			// Since a provider may allocate some resources on behalf of a session
			// outside the JVM, clients should close the resources when they are not
			// needed. Relying on garbage collection to eventually reclaim these resources
			// may not be timely enough.
			// --- Faccio partire i Listner sulle code ---
			SIAPReceiver.getInstance();
			mQueueSender.close();
			// Si rilascia la Connessione
			// mPoolConnection.releaseConnection(lQueueConn);
		} catch (JMSException jmsEx) {
			// mPoolConnection.releaseConnection(lQueueConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("[JMS]: SIAPSender.send():Exception = " + jmsEx.getErrorCode());
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("[JMS]: SIAPSender.send():Exception = ", jmsEx);

			jmsEx.printStackTrace();
			// Si cancella il Messaggio inserito in Tabella MEssaggio
			deleteMessage(aSiapMessage);
			try {
				mQueueSender.close();
			} catch (Exception e) {
			}

			throw new F3BException("Eccezione durante la spedizione del messaggio: " + jmsEx);
		} catch (Exception exception) {
			// mPoolConnection.releaseConnection(lQueueConn);
			exception.printStackTrace();
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("[JMS]: SIAPSender.send():Exception = " + exception);
			if (exception.toString().equals("java.lang.NullPointerException"))
				exception.printStackTrace();
			// Si cancella il Messaggio inserito in Tabella MEssaggio
			deleteMessage(aSiapMessage);

			mQueueSender.close();
			throw new F3BException("Eccezione durante la spedizione del messaggio: " + exception);
		}
		//
		// Nota : 2010-11-02, introdurre successivamente la clausola finally per la mQueueSender.close();
		//
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.info("[JMS]: fine Send");
	}



}