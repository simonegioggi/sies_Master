package siap.jms.connection;

import java.util.Vector;

import javax.jms.QueueConnection;

import org.apache.log4j.Logger;

import com.sun.messaging.ConnectionConfiguration;
import com.sun.messaging.jms.Connection;
import com.sun.messaging.jms.notification.EventListener;

import f3b.log.LogF3B;
import f3b.util.F3BException; // 15/07/2005
import f3b.util.Utils;
import siap.jms.ICostantiJMS;
import siap.jms.config.JMSProperties;

/**
 * ConnectionPoolJMS - Classe che realizza il Pool delle connessioni OpenJMS locali Infatti il pool è con le
 * code locali che ricevono e che vengono riempite per spedire.
 *
 * @version 1.0
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class ConnectionPoolJMS implements ICostantiJMS {

	// 28/05/2019 [EC] - Modifico la destinazione su log PER MEV PROBLEMA CODE INTRODOTTO IN SIES 11.3
	private static Logger siesLogger = Logger.getLogger(LogF3B.JMS_LOG);

	// [FT] - 03/08/2016 - MAC_LOG - Commento la dichiarazione di mLog in favore della variabile siesLogger
	// private static Logger mLog = LogF3B.getLogger();

	private static boolean isAccessible = true;

	// URL del JMS Server
	private String mUrl = null;
	// Numero iniziale di connessioni JMS
	private int mSize = 0;
	// Vector di JMS Connections
	private static Vector mPool = null;
	// Istanza a se stesso--- Singleton
	private static ConnectionPoolJMS mConnectionPool = null;

	// NUOVA INFRASTRUTTURA
	private String mPort = null;

	protected ConnectionPoolJMS() throws Exception {
		initializePool();
	}

	public synchronized static ConnectionPoolJMS getInstance() throws Exception {
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
		siesLogger.info("[JMS]: inizio");
		// STUB 15/07/2005 - Vincenzo.
		// Posto in Try&Catch la getInstance per segnalare l'assenza del servizio JMS.
		try {
			if (mConnectionPool == null)
				mConnectionPool = new ConnectionPoolJMS();
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new F3BException(F3BException.USER_MESSAGE,
					"Attenzione! Il Servizio JMS non è attivo. Rivolgersi all'amministratore di sistema!");
		}
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
		siesLogger.info("[JMS]: fine");

		return mConnectionPool;
	}

	/**
	 * Usato nel caso in cui il server JMS ha chiuso la connessione VIene fatto ripartire e viene ricreata una
	 * connessione.
	 *
	 * @throws Exception
	 */
	public synchronized static void restart() throws Exception {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
		siesLogger.info("[JMS]: inizio");

		try {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.info("[JMS]: checkPool() = " + mConnectionPool.checkPool());
			mConnectionPool.emptyPool();
			mConnectionPool = null;
			mPool = null;

			mConnectionPool = new ConnectionPoolJMS();
		} catch (Exception ex) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("[JMS]: Errore nel restart : " + ex.getClass().getName(), ex);
			ex.printStackTrace();
			throw new F3BException(F3BException.USER_MESSAGE,
					"Attenzione! Il Servizio JMS non è attivo. Rivolgersi all'amministratore di sistema!");
		}

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
		siesLogger.info("[JMS]: fine");
	}

	public String getURL() {
		return mUrl;
	}

	public int getSize() {
		return mSize;
	}

	public void setSize(int value) {
		if (value > 0)
			mSize = value;
	}

	public void setURL(String value) {
		if (value != null)
			mUrl = value;
	}

	public static synchronized void setAccessible(boolean aFlag) {
		isAccessible = aFlag;
	}

	/**
	 * Inizializzo il Pool di Connessioni JMS
	 */
	private void initializePool() throws Exception {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
		siesLogger.info("[JMS]: inizio");

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
		siesLogger.info(
				" <<<<<<<<<<<<<<<<<<< Inizializzo il POOL di Connessioni MessageQueue >>>>>>>>>>>>>>>>>>>>>>");
		// Imposto i valori iniziali
		mSize = JMSProperties.getInstance().getIntProperty(POOL_SIZE);

		if (mSize < 1) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("La size del pool JMS è minore di 1!"
					+ "- Verificare che il File siapjms.properties sia impostato correttamente.");
			throw new Exception("La size del pool JMS è minore di 1!"
					+ "- Verificare che il File siapjms.properties sia impostato correttamente.");
		}

		// *****
		// IMposto la size del pool ad una sola connessione.
		// Message Queue e' in grado di gestire questa sola connessione.
		// ****

		mSize = 1;

		// Crea le Connessioni
		try {
			for (int x = 0; x < mSize; x++) {
				QueueConnection lCon = createConnection();
				if (lCon != null) {
					// Crea una PooledConnectionche incapsula le connessioni JMS
					PooledConnection lPCon = new PooledConnection(lCon);
					// Add la Connessione al pool.
					addConnection(lPCon);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("[JMS]: Errore in initiatePool :" + e.getClass().getName(), e);
			throw new Exception(e.getMessage());
		}

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
		siesLogger.info("[JMS]: fine");
	}

	/**
	 *
	 * @return una Connessione al predefinito Server OpenJMS
	 * @throws Exception
	 */
	private QueueConnection createConnection() throws Exception {
		QueueConnection lCon = null;

		try {
			// Setta le proprietà della connessione JMS
			// Properties props = new Properties();
			mUrl = JMSProperties.getInstance().getProperty(LOCAL);

			if (!mUrl.startsWith("http")) {
				/*
				 * props.setProperty(Context.INITIAL_CONTEXT_FACTORY,
				 * "org.exolab.jms.jndi.InitialContextFactory"); } else {
				 */
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
				siesLogger.error("Connessione JMS non impostata con protocollo HTTP! " + "- " + mUrl
						+ " - Verificare che il File siapjms.properties sia impostato correttamente.");
				throw new Exception("Connessione JMS non impostata con protocollo HTTP! " + "- " + mUrl
						+ " - Verificare che il File siapjms.properties sia impostato correttamente.");
			}

			// props.setProperty(Context.PROVIDER_URL, mUrl);

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.info(" >>> Provo a connettermi a " + mUrl + " <<<");

			// Initial Context
			// Context context = new InitialContext(props);
			// Connection Factory
			// QueueConnectionFactory qFactory = (QueueConnectionFactory)
			// context.lookup(JMS_CONNECTION_FACTORY);

			com.sun.messaging.ConnectionFactory qFactory = new com.sun.messaging.ConnectionFactory();

			// FIXME: DECOMMENTARE SOLO IN LOCALE, NO IN ESERCIZIO
			qFactory.setProperty(ConnectionConfiguration.imqAddressList, mUrl + "/imqhttp/tunnel");
			qFactory.setProperty(ConnectionConfiguration.imqReconnectEnabled, "true");
			qFactory.setProperty(ConnectionConfiguration.imqReconnectInterval, "30000");
			qFactory.setProperty(ConnectionConfiguration.imqReconnectAttempts, "-1");

			// NUOVA INFRASTRUTTURA
			mPort = JMSProperties.getInstance().getProperty(PORT);
			if (Utils.isNullObj(mPort))
				mPort = "7676";
			qFactory.setProperty(ConnectionConfiguration.imqBrokerHostPort, mPort);

			// Creazione di una Connection per interazioni Point to Point
			lCon = qFactory.createQueueConnection();

			// 2010-10-07 - Casting per consentire di i inserire eventmonitor di connessione.
			javax.jms.Connection lConn = lCon;
			Connection llConMQ = (Connection) lConn;

			// Aggancio EventMonitor.
			EventListener eListener = new siap.jms.util.ApplicationEventListener();
			// imposta event listener alla MQ connection.
			llConMQ.setEventListener(eListener);

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger
					.info(" >>> MessageQueue Connesso a " + lCon.getMetaData().getJMSProviderName() + " <<<");
		} catch (Exception ex) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.info(" Errore in createConnection MessageQueue Connesso a " + ex, ex);
			ex.printStackTrace();
			throw ex;
		}

		return lCon;
	}

	/**
	 * Aggiunge la PooledConnection al pool
	 */
	private void addConnection(PooledConnection value) {
		// Se il pool è null, crea un nuovo vector
		// con size = "size"
		if (mPool == null) {
			mPool = new Vector(mSize);
		}
		// Add the PooledConnection Object to the vector
		mPool.addElement(value);
	}

	/**
	 * Rilasca la connessione del pool di Connessioni JMS
	 *
	 * @param con
	 *            La connessione da rilasciare
	 */
	// 2010-10-25 - Cambiato tipo argomento da Connection a QueueConnection
	// per errore compilazione in IstruttoriaController.
	public synchronized void releaseConnection(QueueConnection con) {
		if (con != null) {
			/*
			 * long now = System.currentTimeMillis();
			 *
			 * // cerca l'Oggetto PooledConnection nel Pool for (int x = 0; x < mPool.size(); x++) {
			 * PooledConnection lPCon = (PooledConnection) mPool.elementAt(x);
			 *
			 * if (lPCon.getConnection() == con) { long inUsoDa = (now - lPCon.getLastUsed());
			 *
			 * // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			 * siesLogger.info(" >>> Liberata Connessione JMS ("+lPCon. getConnection().hashCode()+") n. " + x
			 * + " dopo "+inUsoDa+" ms<<< "); // Setta l'attributo InUse a false, che // di fatto rilascia la
			 * connessione
			 *
			 * //lPCon.setInUse(false); //lPCon.setLastUsed(0L);
			 *
			 * break; } }
			 */
		}
	}

	public synchronized QueueConnection getConnection() throws Exception {
		// Controllo di accessibilità al pool
		if (!isAccessible)
			throw new F3BException(F3BException.USER_MESSAGE,
					"Il sistema JMS temporaneamente non è disponibile, riprovare!");

		String lNomeChiamante = null;
		try {
			// cerco di recuperare il nome della classe e metodo chiamante
			// [FT] - 08/08/2016 - MAC_LOG - Recupero il chiamante utilizzando direttamente lo stacktrace
			// in quanto essendo stato rimosso log4j, LocationInfo non è più disponibile.
			StackTraceElement locationInfo = new Exception().getStackTrace()[1];
			// LocationInfo locationInfo = new LocationInfo(new Throwable(),
			// "siap.jms.connection.ConnectionPoolJMS.getConnection");
			// LogF3B.getLogger("SIESCtrl").debug(locationInfo.getFormattedString());
			lNomeChiamante = locationInfo.getClassName() + "." + locationInfo.getMethodName();
			// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			// siesLogger.error(lNomeChiamante);

			PooledConnection pcon = null;

			long now = System.currentTimeMillis();

			String lStatoPool = "Stato del Pool JMS: ";

			// cerca una connessione non in uso
			for (int x = 0; x < mPool.size(); x++) {

				pcon = (PooledConnection) mPool.elementAt(x);

				// Testa se la connessione è in uso
				if (!pcon.inUse()) {
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// mLog
					siesLogger.info(" >>> Presa Connessione JMS (" + pcon.getConnection().hashCode() + ") n. "
							+ x + " da una size " + mPool.size() + " su una SIZE iniziale " + mSize
							+ " <<< ");

					// Si marca IN USO -- Deprecato MessageQueue e' tutto sincronizzato
					// questa gestione casalinga non serve
					// pcon.setInUse(true);
					// pcon.setLastUsed(now);
					pcon.setConsumerClassName(lNomeChiamante);
					// ritorna la JMS Connection memorizzata nel PooledConnection
					return pcon.getConnection();
				} else {
					long inUsoDa = (now - pcon.getLastUsed());
					lStatoPool = lStatoPool + "<br> Connessione JMS n. " + (x + 1) + "/" + mPool.size()
							+ " non disponibile. In uso da " + inUsoDa + " ms, "
							+ pcon.getConsumerClassName();
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// mLog
					siesLogger.error(" >>> Connessione JMS n. " + (x + 1) + "/" + mPool.size()
							+ " non disponibile, in uso da " + inUsoDa + " ms, "
							+ pcon.getConsumerClassName());
				}
			}
		} catch (Exception e) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("Errore durante la connessione a Message Queue", e);
			if (e.getMessage().startsWith(""))
				initializePool();

			throw new F3BException(F3BException.USER_MESSAGE,
					"Il sistema sta provando a prendere una commessione con il sistema JMS. <br> Riprovare la spedizione della richiesta.");
		}
		throw new F3BException(F3BException.USER_MESSAGE,
				"Il sistema sta provando a prendere una commessione con il sistema JMS. <br> Riprovare la spedizione della richiesta.");

		// Non trovo una connessione libera
		// throw una new exception
		// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
		// siesLogger.error(getClass().getName() +
		// " OpenJMS - No Connection avaiable "+lStatoPool);
		// throw new Exception("No Connection avaiable <br>"+lStatoPool);

	}

	/**
	 * Restituisce la prima connessione libera
	 */

	/*
	 * public synchronized QueueConnection getConnection() throws Exception { String lNomeChiamante = null;
	 * try { // cerco di recuperare il nome della classe e metodo chiamante LocationInfo locationInfo = new
	 * LocationInfo(new Throwable(),"siap.jms.connection.ConnectionPoolJMS.getConnection"); //
	 * LogF3B.getLogger("SIESCtrl").debug(locationInfo.getFormattedString()); lNomeChiamante = // [FT] -
	 * 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
	 * locationInfo.getClassName()+"."+locationInfo.getMethodName(); //siesLogger.error(lNomeChiamante);
	 *
	 * PooledConnection pcon = null;
	 *
	 * long now = System.currentTimeMillis();
	 *
	 * String lStatoPool = "Stato del Pool JMS: ";
	 *
	 * // cerca una connessione non in uso for (int x = 0; x < mPool.size(); x++) {
	 *
	 * pcon = (PooledConnection) mPool.elementAt(x);
	 *
	 * // Testa se la connessione è in uso if (pcon.inUse() == false) { LogF3B.getLogger
	 * ().info(" >>> Presa Connessione JMS ("+pcon.getConnection( ).hashCode()+") n. "
	 * +x+" da una size "+mPool.size()+" su una SIZE iniziale "+mSize+" <<< ");
	 *
	 * // Si marca IN USO -- Deprecato MessageQueue e' tutto sincronizzato //questa gestione casalinga non
	 * serve // pcon.setInUse(true); // pcon.setLastUsed(now); pcon.setConsumerClassName(lNomeChiamante); //
	 * ritorna la JMS Connection memorizzata nel PooledConnection return pcon.getConnection(); } else { long
	 * inUsoDa = (now - pcon.getLastUsed()); lStatoPool = lStatoPool +
	 * "<br> Connessione JMS n. "+(x+1)+"/"+mPool.size( )+" non disponibile. In uso da " +// [FT] - 03/08/2016
	 * - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
	 * +inUsoDa+" ms, "+pcon.getConsumerClassName(); siesLogger.error(" >>> Connessione JMS n. "
	 * +(x+1)+"/"+mPool.size() +" non disponibile, in uso da "+inUsoDa+" ms, "+pcon .getConsumerClassName());
	 * }
	 *
	 * } } // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog } }
	 * catch(Exception e) { siesLogger.error("Errore durante la connessione a Message Queue" ,e);
	 * if(e.getMessage().startsWith("")) initializePool();
	 *
	 * throw new F3BException(F3BException.USER_MESSAGE,
	 * "Il sistema sta provando a prendere una commessione con il sistema JMS. <br> Riprovare la spedizione della richiesta."
	 * ); } throw new F3BException(F3BException.USER_MESSAGE,
	 * "Il sistema sta provando a prendere una commessione con il sistema JMS. <br> Riprovare la spedizione della richiesta."
	 * );
	 *
	 * // // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog // Non
	 * trovo una connessione libera // throw una new exception //siesLogger.error(getClass().getName() +
	 * " OpenJMS - No Connection avaiable "+lStatoPool); //throw new
	 * Exception("No Connection avaiable <br>"+lStatoPool);
	 *
	 * }
	 */

	/**
	 * Svuotamento del pool
	 */
	public synchronized void emptyPool() {

		// Itera sul pool chiudendo le Connessioni
		for (int x = 0; x < mPool.size(); x++) {
			PooledConnection lPCon = (PooledConnection) mPool.elementAt(x);
			// Se la PooledConnectionnon è in uso si chiude
			if (!lPCon.inUse()) {
				lPCon.close();
			} else {
				// Se è ancora in use, si dorme per 30 secondi e forza la chiusura
				try {
					java.lang.Thread.sleep(30000);
					lPCon.close();
				} catch (InterruptedException ie) {
					System.err.println(ie.getMessage());
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// mLog
					siesLogger.error(ie.getClass().getName(), ie);
				}
			}

		}

		mPool = null;
	}

	/**
	 * Metodo di utility che effettua il check della Pool loggando lo stato di ogni singola connessione del
	 * pool
	 *
	 */
	public synchronized String checkPool() {
		String lStatoPool = "Stato del Pool JMS: ";

		long now = System.currentTimeMillis();

		// Itera sul pool
		for (int x = 0; x < mPool.size(); x++) {
			PooledConnection lPCon = (PooledConnection) mPool.elementAt(x);
			long inUsoDa = (now - lPCon.getLastUsed());

			// Se la PooledConnectionnon è in uso si chiude
			if (lPCon.inUse()) {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
				siesLogger.error("Connessione " + (x + 1) + "/" + mPool.size() + ": stato = in Uso da "
						+ inUsoDa + " ms da " + lPCon.getConsumerClassName());
				lStatoPool = lStatoPool + "<br> Connessione JMS n. " + (x + 1) + "/" + mPool.size()
						+ " non disponibile. In uso da " + inUsoDa + " ms, " + lPCon.getConsumerClassName();
			} else {
				if (lPCon.getConsumerClassName() == null) {
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// mLog
					siesLogger
							.error("Connessione " + (x + 1) + "/" + mPool.size() + ": stato = disponibile.");
					lStatoPool = lStatoPool + "<br> Connessione JMS n. " + (x + 1) + "/" + mPool.size()
							+ " disponibile.";
				} else {
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// mLog
					siesLogger.error("Connessione " + (x + 1) + "/" + mPool.size()
							+ ": stato = disponibile. Richiesta ultima volta da "
							+ lPCon.getConsumerClassName() + " " + inUsoDa + " ms fa");
					lStatoPool = lStatoPool + "<br> Connessione JMS n. " + (x + 1) + "/" + mPool.size()
							+ " disponibile. Richiesta ultima volta da " + lPCon.getConsumerClassName() + " "
							+ inUsoDa + " ms fa";
				}
			}
		}

		return lStatoPool;
	}

	/**
	 * Metodo invocato quando il Garbage Collector distrugge l'oggetto. In questo modo vengono chiuse tutte le
	 * connessioni altrimenti rimangono operti i socket (TCP) dei listener registrati sulle connessioni
	 *
	 * non funziona
	 */
	// protected void finalize(){
	// for (int x = 0; x < mPool.size(); x++)
	// {
	// PooledConnection lPCon = (PooledConnection) mPool.elementAt(x);
	// lPCon.close();
	// }
	// }

}