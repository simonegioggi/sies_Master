package it.mig.sies.servlet;

import it.mig.sies.business.CommunicationService;
import it.mig.sies.business.ControlService;
import it.mig.sies.business.LoadService;
import it.mig.sies.business.ResultService;
import it.mig.sies.exception.SiesWsException;
import it.mig.sies.model.ResponseData;
import it.mig.sies.type.esecuzione_NEW.RequestData;
import java.io.IOException;
import java.io.Serial;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.log4j.Logger;

/**
 * SIES FASE 2 - Servlet entry point che gestisce l'inserimento/modifica/cancellazione del provvedimento
 * dell'esecuzione
 * 
 * @author Federico Paparoni
 */
public class EntryPointServlet extends HttpServlet {

	/**
	 * 
	 */
	@Serial
	private static final long serialVersionUID = -5813823235783430519L;

	private static final Logger logger = Logger.getLogger(EntryPointServlet.class);

	/**
	 * Gestisce la richiesta di elaborazione ed effettua il forward verso la pagina che visualizza il
	 * risultato
	 * 
	 * @throws IOException,ServletException
	 */
	protected void processRequest(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		logger.info("Inizio trasferimento provvedimento esecutivo");
		response.setContentType("text/html;charset=UTF-8");
		ResponseData resp = null;

		try {
			// Recupero delle informazioni nella request HTTP
			String idEvento = request.getParameter("idEvento");
			// MEV 35253
			String idUtente = request.getParameter("idUtente");
			String action = request.getParameter("action");
			// MEV 4
			// Questo parametro mi identifica il tipo di operazione che bisogna eseguire:
			// DELETE = cancellazione del Foglio Complementare
			// ANNULLA = annullamento del Foglio Complementare
			String tipoOperazione = request.getParameter("tipoOperazione");

			logger.info("Richiesta di trasferimento: idEvento[" + idEvento + "] action[" + action
					+ "] idUtente[" + idUtente + "] tipoOperazione[" + tipoOperazione + "]");

			/**
			 * Caricamento dei dati partendo dall'idEvento legato al provvedimento dell'esecuzione
			 */
			LoadService loadService = new LoadService(idEvento, action, idUtente);
			RequestData requestData = loadService.execute();

			/**
			 * MEV 23010 Controlli vari relativi ai dati che devono essere trasmessi
			 */
			ControlService controlService = new ControlService(requestData);
			controlService.execute();

			/**
			 * Invio della richiesta al webservice su NSC/SIES
			 */
			CommunicationService communicationService = new CommunicationService(requestData);
			it.mig.sies.type.esecuzione_NEW.ResponseData responseData = communicationService.execute();

			/**
			 * Gestione della risposta
			 */
			ResultService resultService = new ResultService(requestData, responseData, idEvento,
					loadService.getRiepilogoOperazione(), tipoOperazione);
			resp = resultService.execute();

			logger.info(resp.getEsito());
		}
		// MEV 23010
		catch (SiesWsException e) {
			resp = new ResponseData();
			resp.setEsito(e.getResponseMessage());
			logger.error(ExceptionUtils.getFullStackTrace(e));
			resp.setDettaglioErrore(ExceptionUtils.getFullStackTrace(e));
		}

		/**
		 * Viene fatto il forward alla JSP per la visualizzazione del risultato
		 */
		logger.info("Forward alla JSP per la visualizzazione del risultato");
		request.setAttribute("response", resp);
		getServletConfig().getServletContext().getRequestDispatcher("/risultatoTrasferimento.jsp")
				.forward(request, response);

	}

	/**
	 * Gestisce la chiamata HTTP GET
	 * 
	 * @throws IOException,ServletException
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		processRequest(request, response);
	}

}