package siap.util;

import java.sql.Driver;
import java.sql.DriverManager;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.PropertyConfigurator;

/**
 * <p>
 * Title: Log4jInit
 * </p>
 * <p>
 * Description: Servlet caricata in fase di startup per la configurazione del log4j
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company:
 * </p>
 *
 * @author unascribed
 * @version 1.0
 */
public class Log4jInit extends HttpServlet {

	/**
	 *
	 */
	private static final long serialVersionUID = -6836492400668201551L;

	@Override
	public void init() {
		try {
			Driver d = (Driver) (Class.forName("oracle.jdbc.driver.OracleDriver").newInstance());
			DriverManager.registerDriver(d);
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		String lPrefix = getServletContext().getRealPath("/");
		String lFile = getInitParameter("log4j-init-file");
		// if the log4j-init-file is not set, then no point in trying

		if (lFile != null) {
			PropertyConfigurator.configure(lPrefix + lFile);
		}
	}

	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse res) {
	}

}