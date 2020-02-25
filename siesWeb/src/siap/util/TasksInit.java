package siap.util;

import java.util.Date;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import siap.siep.jms.controller.PresaInCaricoJMSTask;
import f3b.log.LogF3B;
import f3b.util.TaskQueue;

public class TasksInit extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7736814670920165556L;

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	@SuppressWarnings("unchecked")
	public void init() {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.info("[JMS] : inizio");

		TaskQueue lTaskQueue = TaskQueue.getInstance();
		lTaskQueue.put("JMSTask", new PresaInCaricoJMSTask());
		lTaskQueue.schedule("JMSTask", 60000, 900000);

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.info("[JMS] scheduled : " + new Date(lTaskQueue.getScheduledExecutionTime("JMSTask")));

		/*
		 * Timer lTimer = new Timer(); JMSTask lJMSTask = new JMSTask(); lTimer.schedule(lJMSTask, 60000,
		 * 900000);
		 */

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.info("[JMS] : fine");
	}

	public void doGet(HttpServletRequest req, HttpServletResponse res) {
	}

	public void service(HttpServletRequest req, HttpServletResponse res) {
	}

}