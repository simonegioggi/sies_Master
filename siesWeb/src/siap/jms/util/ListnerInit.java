package siap.jms.util;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import siap.jms.SIAPReceiver;
import f3b.log.LogF3B;

public class ListnerInit extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8373756425365159287L;

	// 28/05/2019 [EC] - Modifico la destinazione su log PER MEV PROBLEMA CODE INTRODOTTO IN SIES 11.3
	private static Logger siesLogger = Logger.getLogger(LogF3B.JMS_LOG);

	public void init() {
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.info("[JMS] : inizio");

		try {
			SIAPReceiver.getInstance();
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.info("[JMS] : Listners initiated.");
		} catch (Exception ex) {

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("[JMS] : Errore su Connessione al JMS : ", ex);
		}

		/*
		 * Timer lTimer = new Timer(); JMSTask lJMSTask = new JMSTask(); lTimer.schedule(lJMSTask, 60000,
		 * 60000);
		 */

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.info("[JMS] : fine");

		/*
		 * try { Thread.sleep(100000); SIAPReceiver.getInstance(); } catch (Exception ex) { }
		 */

	}

	public void doGet(HttpServletRequest req, HttpServletResponse res) {
	}

	public void service(HttpServletRequest req, HttpServletResponse res) {
	}

}