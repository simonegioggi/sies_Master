package siap.jms;

import java.math.BigDecimal;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueReceiver;
import javax.jms.QueueSession;
import javax.jms.Session;
import javax.naming.NamingException;

import org.apache.log4j.Logger;

import siap.jms.config.JMSProperties;
import siap.jms.connection.ConnectionPoolJMS;
import siap.jms.manage.ManageRicerca;
import siap.jms.messaggio.controller.IMessaggio;
import siap.jms.messaggio.model.MessaggioModel;

import com.sun.messaging.jms.MessageFormatException;

import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.xml.TreeModel;

/**
 * <p>
 * Title: SIAPListnerReceiver
 * </p>
 * <p>
 * Description: Classe che realizza il Listner in ascolto per i messaggi in Arrivo
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company:
 * </p>
 */
public class SIAPListnerReceiver implements MessageListener, ICostantiJMS {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	// 28/05/2019 [EC] - Modifico la destinazione su log PER MEV PROBLEMA CODE INTRODOTTO IN SIES 11.3
	private static Logger siesLogger = Logger.getLogger(LogF3B.JMS_LOG);

	// private QueueSession mQueueSession;
	// private Queue mQueue;
	QueueReceiver mReceiver;
	private static ConnectionPoolJMS mPoolConnection;
	private static String idMessage = "";

	protected SIAPListnerReceiver() {
		super();
	}

	/**
	 * Metodo statico per l'unico punto di accesso al listner
	 *
	 * @return
	 * @throws JMSException
	 * @throws NamingException
	 * @throws Exception
	 */
	public synchronized static SIAPListnerReceiver newSIAPListnerReceiver() throws Exception {
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.info("[JMS]: inizio");
		SIAPListnerReceiver listner = new SIAPListnerReceiver();
		listner.initialize();
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.info("[JMS]: fine");
		return listner;
	}

	/**
	 * initialize
	 *
	 * @throws NamingException
	 * @throws JMSException
	 * @throws Exception
	 */
	protected void initialize() throws Exception {
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.info("[JMS]: inizio");
		mPoolConnection = ConnectionPoolJMS.getInstance();

		QueueConnection lConnection = mPoolConnection.getConnection();
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.info("[JMS]: HashConnessione " + lConnection.hashCode());

		try {
			QueueSession qSession = lConnection.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
			String lNameQueue = JMSProperties.getInstance().getProperty(QUEUE_IN_ARRIVO);

			Queue lQueue = qSession.createQueue(lNameQueue);
			QueueReceiver mReceiver = qSession.createReceiver(lQueue);

			SIAPListnerReceiver qListener = this;
			mReceiver.setMessageListener(qListener);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.info(
					"[JMS]: Partito il Listner " + qListener.hashCode() + " " + "sulla Coda " + lNameQueue);

			lConnection.start();
		} catch (Exception ex) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("[JMS]: Errore in SIAPListnerReceiver.inizialize" + ex.getMessage(), ex);
			// ex.printStackTrace();

			if (ex instanceof javax.jms.IllegalStateException) {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.info("[JMS]: Restart Pool Connection ");
				ConnectionPoolJMS.restart();
				//// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				//// LogF3B.getLogger()
				// siesLogger.error("[JMS]: Errore in SIAPListnerReceiver.inizialize" + ex.getMessage(),ex);
				this.initialize();
			}

		}

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.info("[JMS]: fine");
	}

	/**
	 * Metodo che viene attivato alla ricezione di un messaggio
	 *
	 * @param aMessage
	 * @throws RuntimeException
	 */
	public void onMessage(Message aMessage) throws RuntimeException {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.info("[JMS]: inizio");
		// MessaggioModel lMsgInserito = null;
		ObjectMessage lMess = null;
		try {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.info("[JMS]: Letto Messaggio in Arrivo " + aMessage.getJMSType() + " ID = "
					+ aMessage.getJMSMessageID());

			 lMess = (ObjectMessage) aMessage;

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("[JMS]: ObjectMessagge : " + lMess.toString());

			// Modifica Anti-loop sui messaggi di richiesta ricerca in Arrivo
			if (!idMessage.equals(lMess.getJMSMessageID())) {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug(
						"[JMS]: \n\nNO LOOP " + idMessage + " != " + lMess.getJMSMessageID() + " \n\n");
				idMessage = lMess.getJMSMessageID();
			} else {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("[JMS]: \n\nLLLLLLOOOOOOOOOOOOOOOOOOOOOOOOOOPPPPPPPPPPPPPPPP\n\n");
				return;
			}

			// Verifico se il messaggio è un ESITO o una RICHIESTA
			if (lMess.getStringProperty(TIPO_MESSAGGIO).equals(ESITO)) {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.info("[JMS]: >>>>>>>> MESSAGGIO ESITO RICEVUTO DA "
						+ lMess.getStringProperty(COD_BDI_MITTENTE) + " <<<<<<<<<");
				// Si scrive l'esito nella tabella MESSAGGIO
				/* lMsgInserito = */writeMessage(lMess);

				if (lMess.getStringProperty(TIPO_OPERAZIONE).equals(ESITO_TRASFERIMENTO_COMPETENZA_MS)) {
					// MessaggioModel lMessaggioModel = new MessaggioModel(lMess);
					// IMisuraSicurezza lCtrlMisSic = SIEPLookupRemote.getMisuraSicurezzaRemote();
					// lCtrlMisSic.ExElaboraMessaggioEsito(lMessaggioModel);

					// Aggiorno lo stato del messaggio originario
					String idMessRichiesta = lMess.getStringProperty(CORRELATION_ID_MESSAGGIO);
					IMessaggio lCrtl = JMSLookupRemote.getMessaggioRemote();
					MessaggioModel lMessaggio = lCrtl
							.ExRicercaMessaggioByKey(new BigDecimal(idMessRichiesta));
					if (lMessaggio != null) {
						lMessaggio.setCodEsito(lMess.getStringProperty(COD_ESITO));
						lMessaggio.setDataEsito(DateUtils.getSysDate());
						lCrtl.ExModificaMessaggio(lMessaggio);
					}
				}
			} else if (lMess.getStringProperty(TIPO_MESSAGGIO).equals(RICHIESTA)) {

				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.info("[JMS]: >>>>>>>> MESSAGGIO RICHIESTA RICEVUTO  DA "
						+ lMess.getStringProperty(COD_BDI_MITTENTE) + " <<<<<<<<<");
				if (!lMess.getBooleanProperty(STESSA_BDI)) {
					/* lMsgInserito = */writeMessage(lMess);
				} else if (lMess.getStringProperty(TIPO_OPERAZIONE).equals(TRASFERIMENTO_COMPETENZA_MS)
						|| lMess.getStringProperty(TIPO_OPERAZIONE).equals(TRASFERIMENTO_ESECUZIONE_MS)
						|| lMess.getStringProperty(TIPO_OPERAZIONE)
								.equals(SOLLECITO_TRASFERIMENTO_COMPETENZA_MS)) {
					// Nel caso di trasfermento competenza MS si salva in locale il messaggio
					// ricevuto anche se stessa BDI con DELIVERY_MODE = RICEVUTO
					if (lMess.getStringProperty(TIPO_OPERAZIONE).equals(TRASFERIMENTO_COMPETENZA_MS))
						// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto
						// di LogF3B.getLogger()
						siesLogger.debug(
								"Ricevuto messaggio Trasferimento x competenza Mis Sic stessa BDI procedo all'inserimento");
					if (lMess.getStringProperty(TIPO_OPERAZIONE).equals(TRASFERIMENTO_ESECUZIONE_MS))
						// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto
						// di LogF3B.getLogger()
						siesLogger.debug(
								"Ricevuto messaggio Trasferimento x esecuzione Mis Sic stessa BDI procedo all'inserimento");
					if (lMess.getStringProperty(TIPO_OPERAZIONE)
							.equals(SOLLECITO_TRASFERIMENTO_COMPETENZA_MS))
						// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto
						// di LogF3B.getLogger()
						siesLogger.debug(
								"Ricevuto messaggio di SOllecito Trasferimento Mis Sic stessa BDI procedo all'inserimento");
					// lMess.setStringProperty(DELIVERY_MODE, DELIVERY_MODE_RICEVUTO);
					/* lMsgInserito = */writeMessage(lMess);
				}

				// ---Recupero il TreeModel dai messaggi inviati verificando che sia corretto
				Object lObj = lMess.getObject();
				// TreeModel lTree;

				if (!(lObj instanceof TreeModel)) {
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger.error("[JMS]: * * * Eccezione in SIAPListnerReceiver onMessage: Il Messaggio "
							+ " con ID= " + aMessage.getJMSMessageID() + " arrivato da = "
							+ aMessage.getStringProperty(BDI_MITTENTE) + " arrivato con TimeStamp = "
							+ aMessage.getJMSTimestamp() + " NON CONTIENE UN BODY VALIDO");
				}

				// ===========================================================
				// MEV 42 Cumulo. Elaborazione automatica del seguito ATTI
				// ===========================================================
				// 07/2018 scarico del seguito atti non più automatico
				// if
				// (lMess.getStringProperty(TIPO_OPERAZIONE).equals(SEGUITO_ATTI_TRASFERIMENTO_COMPETENZA)){
				// siesLogger.debug("Messaggio di Seguito ATTI, procedo all'elaborazione");
				// // Se fascicolo altra BDI provvedo all'aggiornamento
				// ManageSeguitoAtti lManagerSeguito = new ManageSeguitoAtti();
				// lManagerSeguito.elaboraMessaggioSeguitoAtti(lMess,lMsgInserito);
				// }

			} else if (lMess.getStringProperty(TIPO_MESSAGGIO).equals(RICHIESTA_RICERCA)) { // Richiesta
																							// Ricerca
																							// [FT] -
																							// 03/08/2016 -
																							// MAC_LOG -
																							// Utilizzo la
																							// variabile di
																							// istanza
																							// siesLogger al
																							// posto di
																							// LogF3B.getLogger()
				siesLogger.info("[JMS]: >>>>>>>> MESSAGGIO RICHIESTA RICERCA RICEVUTO DA "
						+ lMess.getStringProperty(COD_BDI_MITTENTE) + " <<<<<<<<<");

				if (!lMess.getBooleanProperty(STESSA_BDI)) {
					writeMessage(lMess);
				}

				ManageRicerca lRicerca = new ManageRicerca();
				lRicerca.elaboraMessaggioRicerca(lMess);

			} else if (lMess.getStringProperty(TIPO_MESSAGGIO).equals(ESITO_RICERCA)) { // Esito Ricerca

				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.info("[JMS]: >>>>>>>> MESSAGGIO ESITO RICERCA RICEVUTO DA "
						+ lMess.getStringProperty(COD_BDI_MITTENTE) + " <<<<<<<<<");
				// --if (!lMess.getBooleanProperty(STESSA_BDI))
				if (lMess.getStringProperty(TIPO_MESSAGGIO).equals(ESITO_RICERCA_SOGGETTO)) {
					ManageRicerca lMan = new ManageRicerca();
					lMan.esitoRicercaSoggettoNotSend(lMess);
				}

				writeMessage(lMess);
			}
		} catch (MessageFormatException invEx) {
			
			siesLogger.error("[JMS]: * * * Eccezione in SIAPListnerReceiver onMessage ", invEx);
			invEx.printStackTrace();
			try {
				// QUI MEMORIZZO L'ARRIVO DI UN MESSAGGIO CHE è ANDATO IN ERRORE PER  Deserialize message failed. - cause: java.io.InvalidClassException
				writeMessageWithError(lMess);
				//spedisci esito per messaggio con Errore AL MITTENTE
				inviaRispostaWitError(lMess);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
		} catch (JMSException eJms) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("[JMS]: * * * Eccezione in SIAPListnerReceiver onMessage ", eJms);
			eJms.printStackTrace();
		} catch (Exception eJms) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("[JMS]: * * * Eccezione in SIAPListnerReceiver onMessage " + eJms.getMessage());
			eJms.printStackTrace();
		}
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.info("[JMS]: fine");
	}

	private void inviaRispostaWitError(ObjectMessage aMessage) {
		
		siesLogger.info("[JMS]: inizio inviaRispostaWitError");

		MessaggioModel lMessage;
		
		try {
			
			lMessage = new MessaggioModel(aMessage, "");
			
			// COME CODICE TIPO OPERAZIONE IMPOSTO SEMPRE ESITO_DI_ERRORE
		    lMessage.setCodTipoOperazione(ESITO_DI_ERRORE);						
			
			if (lMessage != null) {
				lMessage.setDescrBdiDestinataria(aMessage.getStringProperty(BDI_MITTENTE));
				lMessage.setCodBdiDestinataria(aMessage.getStringProperty(COD_BDI_MITTENTE));
				lMessage.setCodBdiMittente(aMessage.getStringProperty(COD_BDI_DESTINATARIA));
				lMessage.setDescrBdiMittente(aMessage.getStringProperty(BDI_DESTINATARIA));
				lMessage.setCodTipoMessaggio(ESITO_RICERCA);
				lMessage.setCodEsito(ICostantiJMS.ERRORE_DEPLOY);

				if (aMessage.getStringProperty(UFFICIO_MITTENTE) != null)
					lMessage.setCodUfficioDestinatario(aMessage.getStringProperty(UFFICIO_MITTENTE));
				else
					lMessage.setCodUfficioDestinatario("-");

				if (aMessage.getStringProperty(UFFICIO_DESTINATARIO) == null)
					lMessage.setCodUfficioMittente("-");
				else
					lMessage.setCodUfficioMittente(aMessage.getStringProperty(UFFICIO_DESTINATARIO));

				lMessage.setCodiceUtenteMittente("OPENJMS");

				if (aMessage.getStringProperty(CORRELATION_ID_MESSAGGIO) != null) 
					lMessage.setJmsCorrelationIdMessage(aMessage.getStringProperty(CORRELATION_ID_MESSAGGIO));

				lMessage.setDataInvio(DateUtils.getSysDate());

				siesLogger.info("[JMS]: aMessage : " + aMessage.toString());

				if (aMessage.getStringProperty(ID_MESSAGGIO) != null)
					lMessage.setIdMessaggio(new BigDecimal(aMessage.getStringProperty(ID_MESSAGGIO)));
			}

			siesLogger.info("SPEDIZIONE MESSAGGIO ESITO CON ERRORE RICERCA AL MITTENTE  : " + lMessage.toString() + "....");
			SIAPSender lSender = new SIAPSender();
			lSender.sendError(lMessage);
		
			siesLogger.info("...MESSAGGIO ESITO RICERCA RISPEDITO AL MITTENTE");
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
		// TODO Auto-generated method stub
		
	}

	/**
	 * Scrive il messaggio sulla Tabella MESSAGGIO
	 *
	 * @param aMessage
	 * @throws Exception
	 */
	private MessaggioModel writeMessage(ObjectMessage aMessage) throws Exception {
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.info("[JMS]: inizio");

		MessaggioModel lMessModel = new MessaggioModel(aMessage);

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("[JMS]: MODEL DA SCRIVERE " + lMessModel);

		if (lMessModel.getCodEsito() != null) {
			if (lMessModel.getCodEsito().length() <= 1)
				lMessModel.setCodEsito("-");
		} else
			lMessModel.setCodEsito("-");

		lMessModel.setFlagVisto("N");

		lMessModel.setDeliveryMode(DELIVERY_MODE_RICEVUTO);

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.info("[JMS]: Esito Messaggio " + lMessModel.getCodEsito());

		IMessaggio lCtrMess = JMSLookupRemote.getMessaggioRemote();
		// MessaggioModel lInd = lCtrMess.ExInserisciMessaggio(lMessModel);

		MessaggioModel lInd = lCtrMess.ExInserisciMessaggioEsitoTrasferimento(lMessModel);

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("[JMS]: Messaggio id : " + lInd.getIdMessaggio());

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.info("[JMS]: fine");
		return lInd;
	}
	
	
	/**
	 * Scrive il messaggio sulla Tabella MESSAGGIO
	 *
	 * @param aMessage
	 * @throws Exception
	 */
	private MessaggioModel writeMessageWithError(ObjectMessage aMessage) throws Exception {
	
		siesLogger.info("[JMS]: inizio writeMessageWithError");

		MessaggioModel lMessModel = new MessaggioModel(aMessage, "");

		siesLogger.debug("[JMS]: MODEL DA SCRIVERE " + lMessModel);

		if (lMessModel.getCodEsito() != null) {
			if (lMessModel.getCodEsito().length() <= 1)
				lMessModel.setCodEsito("-");
		} else
			lMessModel.setCodEsito("-");
		

		lMessModel.setFlagVisto("N");

		lMessModel.setDeliveryMode(DELIVERY_MODE_RICEVUTO);

		
		siesLogger.info("[JMS]: Esito Messaggio " + lMessModel.getCodEsito()  + ">>>>>>>>> writeMessageWithError >>> Deserialize message failed. - cause: java.io.InvalidClassException");

		IMessaggio lCtrMess = JMSLookupRemote.getMessaggioRemote();
		// MessaggioModel lInd = lCtrMess.ExInserisciMessaggio(lMessModel);

		MessaggioModel lInd = lCtrMess.ExInserisciMessaggioEsitoTrasferimento(lMessModel);

		siesLogger.debug("[JMS]: Messaggio id : " + lInd.getIdMessaggio());

		
		siesLogger.info("[JMS]: fine");
		return lInd;
	}

	/*
	 * private boolean esitoRicercaSoggettoNotSend(ObjectMessage aMessage) throws Exception {
	 *
	 * IMessaggio lCtrMess = JMSLookupRemote.getMessaggioRemote();
	 * lCtrMess.ExRicercaCancellaMessaggioEsitoByCorrelationId(
	 * aMessage.getStringProperty(this.CORRELATION_ID_MESSAGGIO),aMessage.getStringProperty(this.
	 * COD_BDI_MITTENTE));
	 *
	 *
	 *
	 * return false; }
	 */

}