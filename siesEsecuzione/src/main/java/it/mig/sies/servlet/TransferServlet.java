package it.mig.sies.servlet;

import it.mig.sies.business.CommunicationService;
import it.mig.sies.business.LoadService;
import it.mig.sies.business.ResultService;
import it.mig.sies.business.TrasferService;
import it.mig.sies.model.ResponseData;
import it.mig.sies.model.TrasferEntry;
import it.mig.sies.type.esecuzione_NEW.RequestData;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

/**
 * SIES FASE 2 - Servlet che gestisce il trasferimento massivo
 * 
 * @author Federico Paparoni
 */
public class TransferServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1332458862096688303L;

	private static final Logger logger = Logger.getLogger(TransferServlet.class);

	/**
	 * Gestisce il caricamento massivo di titoli
	 * 
	 * @throws IOException,ServletException
	 */
	private void processRequest(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {

		logger.info("TrasferServlet called");

		// Setto il content type pertinente
		response.setContentType("text/html;charset=UTF-8");
		String tipologia = request.getParameter("tipologia");
		logger.info("Tipologia trasferimento massivo: " + tipologia);

		// Istanzio il servizio che si occupa di caricare
		// le entry da trasferire
		TrasferService trasferService = new TrasferService();
		List<TrasferEntry> entryList = trasferService.load(tipologia);

		trasferisci(entryList);
		/**
		 * Viene fatto il forward alla JSP per la visualizzazione del risultato
		 */
		request.setAttribute("entryList", entryList);
		getServletConfig().getServletContext().getRequestDispatcher("/risultatoTrasferimentoMassivo.jsp")
				.forward(request, response);

	}

	/**
	 * Gestisce il caricamento massivo di titoli
	 * 
	 * @param entryList
	 *            Lista dei titoli da trasferire
	 * @throws IOException,ServletException
	 */
	private void trasferisci(List<TrasferEntry> entryList) {
		/**
		 * Per ogni titolo esecutivo viene ripercorso lo stesso iter che viene fatto per l'invio da
		 * interfaccia web
		 */
		for (TrasferEntry trasferEntry : entryList) {
			ResponseData resp = new ResponseData();
			try {
				// Si effettua il trasferimento ipotizzando sempre come action INSERT
				LoadService loadService = new LoadService("" + trasferEntry.getIdEvento(), "INSERT");
				RequestData requestData = loadService.execute();

				CommunicationService communicationService = new CommunicationService(requestData);
				it.mig.sies.type.esecuzione_NEW.ResponseData responseData = communicationService.execute();

				ResultService resultService = new ResultService(requestData, responseData,
						"" + trasferEntry.getIdEvento(), loadService.getRiepilogoOperazione(), null);
				resp = resultService.execute();
			} catch (Exception e) {
				resp.setEsito(e.toString());
				e.printStackTrace();
			}
			// Viene popolatata l'entry con le informazioni riguardanti l'operazione
			trasferEntry.setResponseId(resp.getId());
			trasferEntry.setCompleted(resp.isCompleted());
			trasferEntry.setEstratto(resp.getEstratto() != null ? true : false);
			trasferEntry.setEsito(resp.getEsito());
			// MEV 31: aggiunta impostazione di proprieta' per gestire elenco sinonimi
			trasferEntry.setElencoSinonimi(resp.getElencoSinonimi());
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