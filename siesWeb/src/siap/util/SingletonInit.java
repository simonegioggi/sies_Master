package siap.util;

import it.eng.giustizia.avvocatura.util.AvvocaturaProperties;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.inviosegnalazione.config.ISProperties;
import siap.sico.template.controller.TemplateManager;
import f3b.log.LogF3B;

/**
 * <p>
 * Title: SingletonInit
 * </p>
 * <p>
 * Description: Servlet caricata in fase di startup per il Singleton
 * </p>
 */
public class SingletonInit extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7753634807566556654L;

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public void init() {

		try {
			DecodificheManager.getInstance();
			TemplateManager.getInstance();
			// MEV10-s3: aggiunto file di properties in sostituzione di HD non usato
			// HELPDESKProperties.getInstance();
			ISProperties.getInstance();
			// MEV AVVOCATURA: aggiunto file di properties
			AvvocaturaProperties.getInstance();
		} catch (Exception ex) {
			ex.printStackTrace();
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("SingletonInit.init", ex);
		}
	}

	public void doGet(HttpServletRequest req, HttpServletResponse res) {
	}

	public void service(HttpServletRequest req, HttpServletResponse res) {
		DecodificheManager.getInstance();
	}

}