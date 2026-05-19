package f3b.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.log4j.Logger;

/**
 * Classe di utility per tracciare le chiamate che aprono un PreparedStatement o un ResultSet e poi li
 * chiudono. Nel file di log sono tracciate le sequenze di apertura e chiusura e il numero di PS e RS aperti
 * contemporaneamente.
 *
 * Utile per verificare inefficienze con potenziali cursori lasciati aperti.
 *
 * Come best practice i controller nell'utilizzare i sqlDao dovrebbero sempre chiamare i metodi -
 * getModelByKey - gerModels
 *
 * es: UfficioModel lUffMod = (UfficioModel) lUffSqlDao.getModelByKey();
 *
 * es: Vector lListaCont = new Vector(lContCumuloSqlDao.getModels());
 *
 * Tali metodi infatti chiamano lo stop() chiudendo RS e PS dopo averli utilizzati e quidi sono SAFE by
 * design. Inoltre garantiscono che il cursore resti aperto lo stretto tempo necessario.
 *
 * Se invece si chiama lo start() per ciclare sul RS, i controller devono sempre chiamre lo stop() al termine
 * del ciclo. es: lMisCautCumSqlDao.start(); while (lMisCautCumSqlDao.next()) { MisuraCautelareCumuloModel
 * lMCModel = (MisuraCautelareCumuloModel) lMisCautCumSqlDao.getModel(); ..... } lMisCautCumSqlDao.stop();
 * !!!!!!!!!!! da chiudere il prima possibile appena usciti dal while
 *
 * Altrimenti i cursori restano aperti fino al finally, sempre che non si perda il reference in questo caso
 * vengono chiusi dla Garbage Collection sul finalize() del GenericDAO.
 *
 * Attenzione in particolare all'istanziare i sqlDao nei cicli for-while. Se non si chiama subito lo stop(),
 * il finally avrà il riferimento solo all'ultimo sqlDao istanziato e potrà chiudere solo quello.
 *
 * for (............) { MisuraCautelareSqlDao lMisCautCumSqlDao = new MisuraCautelareSqlDao (lConn); !!! New
 * nel ciclo lMisCautCumSqlDao.start(); while (lMisCautCumSqlDao.next()) { MisuraCautelareCumuloModel lMCModel
 * = (MisuraCautelareCumuloModel) lMisCautCumSqlDao.getModel(); ..... } lMisCautCumSqlDao.stop(); // se non si
 * chiude qui il finally vede solo l'ultimo lMisCautCumSqlDao istanziato }
 *
 *
 * Come utilizzarlo Sono per debug locale. Non usare per ora in esercizio.
 *
 * Mettere le chiamate a enableTracking e disableTracking a cavallo del codice che si vuole monitorare. es in
 * una action prima della chiamata a un controller
 *
 * ActStampaProvvedimentoCumulo:
 *
 * JdbcTrackerUtil.enableTracking(null); IDatiFinaliCumulo lCtrlDatiFinali =
 * SIEPLookupRemote.getDatiFinaliCumuloRemote(); ByteArrayOutputStream lReport =
 * lCtrlDatiFinali.ExStampaProvvedimentoCumulo(lEveNotMod,lFascicoloModel, lUtente, lUfficio);
 * JdbcTrackerUtil.disableTracking(null);
 *
 * n.b. non passare la connection ad enable e disable. Nella prima versione era monitorata la singola
 * connection, ma alcuni controller ne chiamano altri in cascata che usano altre connessioni per cui è stato
 * disabilitato l'utilizzo della tracciatura a livello di connection
 *
 *
 * Per utilizzare il monitoraggio "globale" ovvero senza attivarlo sulla singola funzione è possibile
 * commentare le righe if (!isActive) return; nei metodi trackOpen e trackClose In questo caso il logger
 * traccerà tutte le chiusure a aperture mentre si utilizza il SIES
 *
 *
 * LOGGATURA: logga su un logger specifico JdbcTrackerUtil, quindi in
 * [JBOSS_HOME\modules\config\log\main\logback.xml] Va dichiarato un logger JdbcTrackerUtil con relativo file
 * per l'appender (es JdbcTrackerUtil.log) es: <logger name="JdbcTrackerUtil" additivity="false" level="ALL">
 * <appender-ref ref="destJdbcTrackerUtil"/> </logger>
 *
 * @author difiorlett
 *
 */
public class JdbcTrackerUtil {

	private static Logger siesLogger = Logger.getLogger("JdbcTrackerUtil");

	// Insieme delle connessioni da tracciare (Thread-safe)
	private static final Set<Integer> trackedConnections = Collections
			.newSetFromMap(new ConcurrentHashMap<Integer, Boolean>());

	// Contatori per thread
	private static final ThreadLocal<Integer> depth = ThreadLocal.withInitial(() -> 0);
	private static final ThreadLocal<AtomicInteger> openStatements = ThreadLocal
			.withInitial(() -> new AtomicInteger(0));
	private static final ThreadLocal<AtomicInteger> openResultSets = ThreadLocal
			.withInitial(() -> new AtomicInteger(0));

	private static boolean isActive = false;

	/**
	 * Abilita la tracciatura. Inserire la chiamata es in una Action a cavallo della chiamata a un controller
	 * se si vogliono monitorizzae tutte le attività effettuate dal CTRL
	 *
	 * @param conn
	 *            - passare a null
	 */
	public static void enableTracking(Connection conn) {

		// if (conn == null) return;
		isActive = true;
		trackedConnections.add(System.identityHashCode(conn));
		siesLogger.info("\n" + getIndent() + ">>> [MONITOR ATTIVATO] Connessione: " + conn);
		depth.set(depth.get() + 1);
	}

	private static String getIndent() {

		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < depth.get(); i++)
			sb.append(" | ");
		return sb.toString();
	}

	/**
	 * Richiamato dal GenericDAO quando viene richiamata la start() mPs = mCon.prepareStatement(mStatement);
	 * mRs = mPs.executeQuery();
	 *
	 * @param resource
	 * @param owner
	 */
	public static void trackOpen(Object resource, Connection owner, Object dao) {

		// if (!isTracked(owner)) return; // Ignora se la connessione non è monitorata
		if (!isActive)
			return;

		String type = "";
		int countPS = 0;
		int countRS = 0;
		if (resource instanceof PreparedStatement || resource instanceof Statement) {
			siesLogger.debug("" + dao.getClass());
			// Se si vuole tracciare il singolo sqlDAO da chi viene aperto
			// if (dao instanceof UfficioSqlDAO)
			// loggaStackTrace();
			type = "STMT";
			countPS = openStatements.get().incrementAndGet();
			siesLogger.info("Tipo STMT con PS pari a: " + countPS);
		} else if (resource instanceof ResultSet) {
			type = "RS  ";
			countRS = openResultSets.get().incrementAndGet();
			siesLogger.info("Tipo RS   con RS pari a: " + countRS);
		}

		// Impostare un livello oltre al quale si vuole loggare un potenziale problema.
		// La situazione ottimale sarebbe un solo RS aperto per volta se si utilizzano getModelByKey e
		// getModels
		// se si usa start() invece è possibile vedere 2 RS aperti contemporaneamente
		// da 3 in poi è sintomo di cicli annidati che andrebbero evitati
		if (countRS >= 3) {
			siesLogger.warn("Numero di ResultSet aperti elevato (" + countRS + ")! Verificare il codice");
			loggaStackTrace();
		}

		siesLogger.info(getIndent() + " [+] APERTO " + type + " | Totali attivi: (STMT: "
				+ openStatements.get().get() + ", RS: " + openResultSets.get().get() + ")");
		depth.set(depth.get() + 1);
	}

	/**
	 * Richiamato dal GenericDAO quando viene richiamata la close() sul RS o sul PS
	 *
	 * @param resource
	 * @param owner
	 */
	public static void trackClose(Object resource, Connection owner) {

		// if (!isTracked(owner)) return;
		if (!isActive)
			return;

		String type = "";
		if (resource instanceof PreparedStatement || resource instanceof Statement) {
			// Esco senza fare nulla probabilmente uno stop() inutile dopo un update o insert
			// serve per evitare che il contatore vada in negativo
			if (openStatements.get().get() == 0)
				return;

			type = "STMT";
			openStatements.get().decrementAndGet();
		} else if (resource instanceof ResultSet) {
			type = "RS  ";
			openResultSets.get().decrementAndGet();
		}

		depth.set(depth.get() - 1);
		siesLogger.info(getIndent() + " [-] CHIUSO " + type + " | Totali attivi: (STMT: "
				+ openStatements.get().get() + ", RS: " + openResultSets.get().get() + ")");

		// Se ho una chiusura che va in negativo la loggo
		if (openStatements.get().get() < 0)
			loggaStackTrace();
	}

	/**
	 * Trmina la tracciatura e segnala aventuali RS o PS rimasti aperti
	 *
	 * @param conn
	 *            - impostare a null
	 */
	public static void disableTracking(Connection conn) {

		// if (conn == null || !isTracked(conn)) return;
		// if (conn != null) return;
		isActive = false;

		int stLeft = openStatements.get().get();
		int rsLeft = openResultSets.get().get();

		depth.set(Math.max(0, depth.get() - 1));
		siesLogger.info(getIndent() + "<<< [MONITOR DISATTIVATO] Residui: (STMT: " + stLeft + ", RS: "
				+ rsLeft + ")\n");

		if (stLeft > 0 || rsLeft > 0) {
			siesLogger.error(getIndent() + " !!! LEAK RILEVATO !!! StackTrace segue:");
			for (StackTraceElement ste : Thread.currentThread().getStackTrace()) {
				siesLogger.error(getIndent() + "    at " + ste);
			}
		}

		// Pulizia finale
		trackedConnections.remove(System.identityHashCode(conn));
		openStatements.get().set(0);
		openResultSets.get().set(0);
	}

	// private static boolean isTracked(Connection conn) {
	// return conn != null && trackedConnections.contains(System.identityHashCode(conn));
	// }

	/**
	 * Metodo per scrivere nel log lo stack trace della chiamata. Limita lo stack alle classi di progetto
	 * (package siap ed f3b)
	 */
	private static void loggaStackTrace() {

		Throwable t = new Throwable("");
		StackTraceElement[] stack = t.getStackTrace();
		ArrayList<StackTraceElement> filteredStack = new ArrayList<>();
		for (int i = 0; i < stack.length; i++) {
			StackTraceElement elem = stack[i];
			if (elem.getClassName().startsWith("siap")
			// || elem.getClassName().startsWith("f3b")
			) // filtro sui package
				filteredStack.add(elem);
		}
		StackTraceElement[] stack2 = new StackTraceElement[filteredStack.size()];
		stack2 = filteredStack.toArray(stack2);
		t.setStackTrace(stack2);
		siesLogger.warn("", t);
	}

}