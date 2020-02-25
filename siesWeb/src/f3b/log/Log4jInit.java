package f3b.log;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.PropertyConfigurator;

/**
 * Servlet utilizzata per il caricamento del file di property di log4j. inserire nel file web.xml il seguente
 * codice
 * 
 * <servlet> <servlet-name>log4j-init</servlet-name> <servlet-class>f3b.log.Log4jInit</servlet-class>
 * 
 * <init-param> <param-name>log4j-init-file</param-name> <param-value>WEB-INF/log4j.properties</param-value>
 * </init-param>
 * 
 * <load-on-startup>1</load-on-startup> </servlet>
 */
public class Log4jInit extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8707170566540932550L;

	public void init() {
		String prefix = getServletContext().getRealPath("/"); // Path del contesto
		String file = getInitParameter("log4j-init-file");
		// if the log4j-init-file is not set, then no point in trying
		if (file != null) {
			PropertyConfigurator.configure(prefix + file);
		}
	}

	public void doGet(HttpServletRequest req, HttpServletResponse res) {
	}

}