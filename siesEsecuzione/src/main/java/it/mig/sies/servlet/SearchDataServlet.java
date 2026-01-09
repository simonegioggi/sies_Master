package it.mig.sies.servlet;

import it.mig.sies.business.LoadService;
import it.mig.sies.exception.ProfileException;
import it.mig.sies.model.ResponseData;
import it.mig.sies.util.ApplicationProperties;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.log4j.Logger;

/**
 * SIES FASE 2 - Servlet che gestisce il caricamento delle informazioni relative all'attivitï¿½ effettuate sul
 * singolo provvedimento dell'esecuzione
 * 
 * @author Federico Paparoni
 */
public class SearchDataServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2683234170485164666L;

	private static final Logger logger = Logger.getLogger(SearchDataServlet.class);

	/**
	 * Recupera lo storico delle operazioni effettuate su uno specifico idEvento
	 * 
	 * @throws IOException,ServletException
	 */
	protected void processRequest(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out = response.getWriter();
		String idEvento = request.getParameter("idEvento");
		// MEV 35253
		String idUtente = request.getParameter("idUtente");
		logger.info("Storico delle operazioni idEvento[" + idEvento + "] idUtente[" + idUtente + "]");
		try {
			// Viene caricata la lista di operazioni
			LoadService loadService = new LoadService(idEvento, LoadService.LOCAL_SEARCH, idUtente);
			List<ResponseData> responseDataList = loadService.loadResponseList();

			// MEV 42558 - Modificata label e gestione output
			if ((responseDataList != null) && (responseDataList.size() > 0)) {
				/**
				 * Viene fatto il forward alla JSP per la visualizzazione del risultato
				 */
				request.setAttribute("responseDataList", responseDataList);

			}
			getServletConfig().getServletContext().getRequestDispatcher("/search.jsp").forward(request,
					response);
		} catch (ProfileException e) {
			ApplicationProperties properties = ApplicationProperties.getIstance();
			String esito = properties.getProperty("messaggio.errore.profilo");
			logger.error(ExceptionUtils.getFullStackTrace(e));
			out.println(esito);
			out.close();
		} catch (Exception e) {
			ApplicationProperties properties = ApplicationProperties.getIstance();
			String esito = properties.getProperty("messaggio.errore.storico");
			logger.error(ExceptionUtils.getFullStackTrace(e));
			out.println(esito);
			out.close();
		}
	}

	/**
	 * Gestisce la chiamata HTTP GET
	 * 
	 * @param request
	 *            HttpServletRequest
	 * @param response
	 *            HttpServletResponse
	 * @throws IOException,ServletException
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		processRequest(request, response);
	}

	/**
	 * Gestisce la chiamata HTTP POST
	 * 
	 * @param request
	 *            HttpServletRequest
	 * @param response
	 *            HttpServletResponse
	 * @throws IOException,ServletException
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		processRequest(request, response);
	}

}