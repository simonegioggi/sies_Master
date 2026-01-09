package it.mig.sies.servlet;

import it.mig.sies.business.LoadService;
import it.mig.sies.model.ResponseData;
import java.io.IOException;
import java.io.OutputStream;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * SIES FASE 2 - Servlet che gestisce il caricamento dell'estratto presente su database
 * 
 * @author Federico Paparoni
 */
public class LoadDataServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5676937457032356635L;

	/**
	 * Gestisce il caricamento di una entry specifica relativa alla risposta ricevuta dal webservice
	 * 
	 * @throws IOException,ServletException
	 */
	protected void processRequest(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// Setto il content type pertinente per il pdf
		response.setContentType("application/pdf");
		response.setHeader("Content-Disposition", "attachment; filename=\"estratto.pdf\"");
		OutputStream out = response.getOutputStream();
		try {
			String id = request.getParameter("id");
			// Recupero il ResponseData
			LoadService loadService = new LoadService(id);
			ResponseData responseData = loadService.loadResponse();
			// Inserisco l'estratto sul flusso di output
			out.write(responseData.getEstratto());
			out.flush();
		} finally {
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