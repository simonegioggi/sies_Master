package siap.jms;

//import java.io.InputStreamReader;

//import javax.jms.MessageListener;
import java.rmi.ConnectException;

import javax.naming.NamingException;

import org.apache.log4j.Logger;

import siap.jms.config.JMSProperties;
import siap.jms.connection.ConnectionPoolJMS;
import siap.jms.util.JMXDestinationManager;
import f3b.log.LogF3B;
import f3b.util.F3BException;

/**
 * <p>
 * Title: SIAPReceiver
 * </p>
 * <p>
 * Description: Siap Receiver è la classe Singleton che fa partire i Listner sia sulla Coda Ricevente che
 * sulla coda in Partenza
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company:
 * </p>
 */
public class SIAPReceiver implements ICostantiJMS {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	// 28/05/2019 [EC] - Modifico la destinazione su log PER MEV PROBLEMA CODE INTRODOTTO IN SIES 11.3
	private static Logger siesLogger = Logger.getLogger(LogF3B.JMS_LOG);
		
	private ConnectionPoolJMS mPoolConnection;
	private static SIAPReceiver mSIAPReceiver = null;

	protected SIAPReceiver() throws Exception {
		// Prende l'istanza del Connection Pool
		setmPoolConnection(ConnectionPoolJMS.getInstance());
	}

	public synchronized static SIAPReceiver getInstance() throws Exception {
		try {
			if (mSIAPReceiver == null) {
				mSIAPReceiver = new SIAPReceiver();
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.info("[JMS]: Partenza dei listner sulle code.");

				// Partenza dei Listner sulle Code
				// fatta solo la prima volta che viene
				// chiamato il SIAPReceiver
				mSIAPReceiver.receive();
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.info("[JMS]: Listner su coda inArrivo : Partito! ");

				mSIAPReceiver.sendMessageSent();
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.info("[JMS]: Listner su coda inPartenza : Partito! ");

				mSIAPReceiver.stampa();
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.info("[JMS]: Listner su coda Stampa : Partito! ");

			} else {

				JMXDestinationManager lJMX = JMXDestinationManager.getInstance();

				if (lJMX != null) {
					// Verifica stato dei listner sulla coda "inArrivo" ed eventuale restart.
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger.info("[JMS]: Verifica listner su coda : inArrivo.");
					if (lJMX.getNumActiveConsumer(
							JMSProperties.getInstance().getProperty(QUEUE_IN_ARRIVO)) == 0) {
						// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto
						// di LogF3B.getLogger()
						siesLogger.info("[JMS]: Ripartenza listner su coda : inArrivo.");
						mSIAPReceiver.receive();
					}
					// Verifica stato dei listner sulla coda "inPartenza" ed eventuale restart.
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger.info("[JMS]: Verifica listner su coda : inPartenza.");
					if (lJMX.getNumActiveConsumer(
							JMSProperties.getInstance().getProperty(QUEUE_IN_PARTENZA)) == 0) {
						// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto
						// di LogF3B.getLogger()
						siesLogger.info("[JMS]: Ripartenza listner su coda : inPartenza.");
						mSIAPReceiver.sendMessageSent();
					}

					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger.info("[JMS]: Verifica listner su coda : Stampa.");
					if (lJMX.getNumActiveConsumer("Stampa") == 0) {
						// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto
						// di LogF3B.getLogger()
						siesLogger.info("[JMS]: Ripartenza listner su coda : Stampa.");
						mSIAPReceiver.stampa();
					}
				} else {
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger.error("[JMS]: "
							+ " Non è possibile stabilire una connessione come amministratore al servizio JMS!");
				}

			}
		} catch (NamingException exNam) {
			throw new F3BException(F3BException.USER_MESSAGE, "Errore durante la Connessione al Server JMS");
		} catch (ConnectException connEx) {
			throw new F3BException(F3BException.USER_MESSAGE, "Errore durante la Connessione al Server JMS");
		} catch (Exception e) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("[JMS]: " + e.getMessage());
			throw e;
		}

		return mSIAPReceiver;
	}

	/**
	 * Attiva il ricevente sulla coda In Arrivo
	 * 
	 * @throws Exception
	 */
	private void receive() throws Exception {
		try {
			SIAPListnerReceiver.newSIAPListnerReceiver();
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new F3BException(F3BException.USER_MESSAGE,
					"Errore durante la Connessione al Server JMS. " + ex.getMessage());
		} finally {
			//// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			//// LogF3B.getLogger()
			// siesLogger.info("[JMS]: Rilascio Connessione1 ");
		}
	}

	/**
	 * Attiva il Listner per i messaggi nella coda in Partenza verso altri server OpenJMS
	 * 
	 * @throws Exception
	 */
	private void sendMessageSent() throws Exception {
		try {
			SIAPListnerSender.newSIAPListnerSender();
		} catch (Exception ex) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("[JMS]: Eccezione " + ex.getMessage());
			ex.printStackTrace();
			throw new F3BException(F3BException.USER_MESSAGE, "Errore durante la Connessione al Server JMS");
		} finally {
		}
	}

	/**
	 * Attiva il ricevente sulla coda di Stampa
	 * 
	 * @throws Exception
	 */
	private void stampa() throws Exception {
		try {
			SIAPListnerStampa.newSIAPListnerStampa();
		} catch (Exception ex) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("[JMS]: Eccezione " + ex.getMessage());
			ex.printStackTrace();
			throw new F3BException(F3BException.USER_MESSAGE, "Errore durante la Connessione al Server JMS");
		} finally {
		}
	}

	/**
	 * Attiva la connessione sulla coda di utilità "queue1" che serve per risvegliare i listener che dormono.
	 * Viene invocata dalla servlet ServletJMSWakeUp invocata a sua volta dei Sender remoti
	 * (SIAPSender.sendToTrueDestination()). Per essere sicuri che il messaggio venga scodato.
	 * 
	 * Effettua un receiveNoWait su una coda locale vuota (queue1), se fallisce... invoca: receive() che
	 * attiva un listener sulla coda inArrivo sendMessageSent() che attiva un listener sulla coda inPartenza
	 * 
	 * @throws Exception
	 */

	// NOTE : 2010-10-11 Secondo me questo metodo è inutile, crea solo un numero crescente
	// di consumers sulla coda Queue1, con conseguente probabile blocco di openMQ.

	//
	public void checkTimeOut() throws Exception {
		//
		// 2010-10-12 : Commentata questa parte per inibire la funzione di wakeup
		// che allo stato attuale crea in numero sempre crescente di consumers
		// apparentemente inattivi e che potrebbero sovraccaricare inutilmente il sistema.
		// Si eseguiranno analisi più approfondite.
		//
		/*
		 * QueueConnection lConnection = null; try { lConnection = mPoolConnection.getConnection();
		 * QueueSession qSession = lConnection.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
		 * 
		 * String lNameQueue = "queue1";
		 * 
		 * Queue lQueue = qSession.createQueue(lNameQueue); QueueReceiver qReceiver =
		 * qSession.createReceiver(lQueue);
		 * 
		 * // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		 * LogF3B.getLogger() siesLogger.info("[JMS]: \nCreato Receiver su " + lNameQueue + "\n");
		 * 
		 * Message msg = qReceiver.receiveNoWait(); // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di
		 * istanza siesLogger al posto di LogF3B.getLogger()
		 * siesLogger.info("[JMS]: >>>> ...connesione JMS attiva");
		 * 
		 * // // 2010-10-12 - Da provare se manca una close.... // per adesso commentata poichè si sta ancora
		 * indagando // sull'uso ( retaggio di openJMS ? ) ed ventuali ripercussioni. // Sta di fatto che ad
		 * ogni invocazione della servlet di wakeup, // si creano comsumer in numero crescente sulla coda :
		 * Queue1. // qReceiver.close(); //
		 * 
		 * } catch (JMSException jmse) { // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza
		 * siesLogger al posto di LogF3B.getLogger()
		 * siesLogger.error("[JMS]: >>>> ...connesione JMS --- NON --- attiva");
		 * 
		 * mPoolConnection.releaseConnection((com.sun.messaging.jms.Connection)lConnection); // 2010-10-08
		 * //mPoolConnection.releaseConnection(lConnection); // 2010-10-08 mSIAPReceiver.receive();
		 * mSIAPReceiver.sendMessageSent(); // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza
		 * siesLogger al posto di LogF3B.getLogger() siesLogger.error("[JMS]: >>>> Ripartiti"); } finally { if
		 * (lConnection != null){
		 * mPoolConnection.releaseConnection((com.sun.messaging.jms.Connection)lConnection);
		 * //mPoolConnection.releaseConnection(lConnection); // 2010-10-08 } }
		 */
	}

	public synchronized void testInArrivo() throws Exception {
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("start");
	}

	public synchronized void testInPartenza() throws Exception {
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("start");
	}

	public synchronized void testStampa() throws Exception {
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("start");
	}

	public ConnectionPoolJMS getmPoolConnection() {
		return mPoolConnection;
	}

	public void setmPoolConnection(ConnectionPoolJMS mPoolConnection) {
		this.mPoolConnection = mPoolConnection;
	}

}