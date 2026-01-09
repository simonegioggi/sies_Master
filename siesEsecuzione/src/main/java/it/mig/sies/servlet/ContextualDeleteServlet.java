package it.mig.sies.servlet;

import it.mig.sies.business.ControlService;
import it.mig.sies.model.ResponseData;
import it.mig.sies.util.ApplicationProperties;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

/**
 * Servlet che gestisce solo la cancellazione del provvedimento dell'esecuzione, richiamata in maniera
 * contestuale alla cancellazione su SIES - MEV 06
 * 
 * @author Federico Paparoni
 */
public class ContextualDeleteServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 732611276908202867L;

	private static final Logger logger = Logger.getLogger(ContextualDeleteServlet.class);

	/**
	 * Gestisce la richiesta di cancellazione ed effettua il forward verso la pagina che visualizza il
	 * risultato (ok/ko)
	 * 
	 * @throws IOException,ServletException
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		logger.info("Controllo per cancellazione contestuale");
		request.setAttribute("visualizzazioneSemplice", Boolean.TRUE);
		ControlService controlService = new ControlService(null);
		boolean presente = controlService.verificaPresenza(request.getParameter("idEvento"));

		if (!presente) {
			logger.info("Foglio complementare non trasmesso quindi nessuna necessita di cancellarlo");
			ApplicationProperties applicationProperties = ApplicationProperties.getIstance();
			String deleteSuperflua = applicationProperties.getProperty("messaggio.delete.superflua");
			ResponseData responseData = new ResponseData();
			responseData.setEsito(deleteSuperflua);
			request.setAttribute("response", responseData);
			getServletConfig().getServletContext().getRequestDispatcher("/risultatoTrasferimento.jsp")
					.forward(request, response);
		} else {
			logger.info("Forward alla EntryPointServlet per la cancellazione del foglio complementare");
			// MEV 16: aggiunto controllo preventivo per indirizzamento
			if (request.getParameter("tipoWS") != null
					&& "siepToNsc".equals(request.getParameter("tipoWS"))) {
				request.removeAttribute("visualizzazioneSemplice");
				getServletConfig().getServletContext()
						.getRequestDispatcher("/trasferisciFoglioComplementareSIEP")
						.forward(request, response);
			} else
				getServletConfig().getServletContext().getRequestDispatcher("/trasferimento").forward(request,
						response);
		}
	}

}