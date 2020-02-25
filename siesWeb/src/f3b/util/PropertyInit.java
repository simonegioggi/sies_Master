package f3b.util;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import siap.util.SIAPPathProperties;

/**
 * setta la proprietà di sistema path.properties al path della cartella che contiene tutti i file di
 * propertuies che contine tutti i file di properties dell'applicazione SIES
 * 
 * 
 * aggiungere al web.xml che si trova sotto la WEB-INF dell'applicazione dopo la servlet mapping del LOG4J
 * <servlet> <servlet-name>property-init</servlet-name> <servlet-class>f3b.util.PropertyInit</servlet-class>
 * <load-on-startup>2</load-on-startup> </servlet>
 *
 * ATTENZIONE al parametro load-on-startup la sequenza esatta è 1 PER log4j-init 2 PER property-init 3 PER
 * SingletonInit
 * 
 * @author Barbarini
 *
 */
public class PropertyInit extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3528360361419889051L;

	// NUOVA INFRASTRUTTURA: aggiunte variabili di classe
	private SIAPPathProperties mPathProperties = SIAPPathProperties.getInstance();
	private String mPath = null;

	public void init() {

		try {
			// NUOVA INFRASTRUTTURA: cambiato il path per differenziare macchina windows da macchina UNIX
			mPath = mPathProperties.getProperty("CONFIG");
			System.setProperty("path.properties", mPath);
		} catch (F3BException e) {
			e.printStackTrace();
		}
	}

	public void doGet(HttpServletRequest req, HttpServletResponse res) {
	}

}