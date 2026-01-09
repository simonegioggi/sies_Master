package it.mig.sies.servlet;

import it.mig.sies.business.CommunicationTrasferisciFCService;
import it.mig.sies.business.LoadTrasferisciFCService;
import it.mig.sies.business.ResultTrasferisciFCService;
import it.mig.sies.exception.SiesWsException;
import it.mig.sies.model.ResponseData;
import it.mig.sies.type.foglicomplementari.RequestData;
import it.mig.sies.util.PropertyUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.log4j.Logger;

/**
 * MEV 16 - Servlet entry point che gestisce l'invio del foglio complementare ad nsc
 * 
 * @author SIMONE GIOGGI
 */
public class TrasferisciFoglioComplementareSIEPServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1437043850893865575L;

	private static final Logger logger = Logger.getLogger(TrasferisciFoglioComplementareSIEPServlet.class);

	/**
	 * Gestisce la richiesta di elaborazione ed effettua il forward verso la pagina che visualizza il
	 * risultato
	 * 
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	protected void processRequest(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		// info per il log
		logger.info("Inizio trasferimento foglio complementare!");
		// impostazione proprieta'
		response.setContentType("text/html;charset=UTF-8");
		// instanzio ed inizializzo una lista di oggetti di tipo "ResponseData"
		List<ResponseData> responseDataList = new ArrayList<ResponseData>();
		// instanzio un oggetto di tipo "ResponseData"
		ResponseData responseData = null;
		// MEV 16 CUMULO: instanzio ed inizializzo una lista di oggetti di tipo "RequestData"
		List<RequestData> requestDataList = new ArrayList<RequestData>();
		String idEvento = "";
		String idUtente = "";
		String idSentenza = "";
		String idFascicoloSiep = "";
		String action = "";

		try {
			// Recupero delle informazioni nella request HTTP
			idEvento = request.getParameter("idEvento");
			idUtente = request.getParameter("idUtente");
			action = request.getParameter("action");
			// Questo parametro mi identifica il tipo di operazione che bisogna eseguire:
			// DELETE = cancellazione del Foglio Complementare
			// ANNULLA = annullamento del Foglio Complementare
			String tipoOperazione = request.getParameter("tipoOperazione");
			// Recupero un parametro iniziale
			String initParam = super.getInitParameter("tipoWS");
			String idSoggetto = request.getParameter("idSoggetto");
			idSentenza = request.getParameter("idSentenza");
			idFascicoloSiep = request.getParameter("idFascicoloSiep");
			// MEV 16 CUMULO: aggiunti parametri nella request
			String idSinonimo = request.getParameter("idSinonimo");
			if (!PropertyUtil.isPresent(idSinonimo) || "undefined".equals(idSinonimo))
				idSinonimo = "";
			String idSoggettoNSC = request.getParameter("idSoggettoNSC");
			String azioneTrasfCumulo = request.getParameter("azioneTrasfCumulo");
			if (!PropertyUtil.isPresent(idSoggettoNSC) || "undefined".equals(idSoggettoNSC))
				idSoggettoNSC = "";
			if (!PropertyUtil.isPresent(azioneTrasfCumulo) || "undefined".equals(azioneTrasfCumulo))
				azioneTrasfCumulo = "";
			if (PropertyUtil.isPresent(idSoggettoNSC))
				idSinonimo = idSoggettoNSC + "#TFCCUMCONF#" + azioneTrasfCumulo;
			else if (PropertyUtil.isPresent(idSinonimo))
				idSinonimo += "#TFCCUM#" + azioneTrasfCumulo;

			logger.info("Richiesta di trasferimento: idEvento[" + idEvento + "] action[" + action
					+ "] idUtente[" + idUtente + "] tipoOperazione[" + tipoOperazione + "] initParam["
					+ initParam + "] idSoggetto[" + idSoggetto + "] idSentenza[" + idSentenza
					+ "] idFascicoloSiep[" + idFascicoloSiep + "]" + "] idSinonimo[" + idSinonimo + "]"
					+ "] idSoggettoNSC[" + idSoggettoNSC + "]" + "] azioneTrasfCumulo[" + azioneTrasfCumulo
					+ "]");

			/**
			 * Caricamento dei dati partendo dall'idEvento legato al foglio complementare
			 */
			LoadTrasferisciFCService loadService = new LoadTrasferisciFCService(idEvento, action, idUtente,
					idSoggetto, idSentenza, idFascicoloSiep, idSinonimo);
			requestDataList = loadService.execute();

			// ciclo sul risultato ottenuto
			for (RequestData requestData : requestDataList) {
				/**
				 * Invio della richiesta al webservice su SIES/NSC
				 */
				CommunicationTrasferisciFCService communicationService = new CommunicationTrasferisciFCService(
						requestData);
				it.mig.sies.type.foglicomplementari.ResponseData responseDataFC = communicationService
						.execute();

				/**
				 * Gestione della risposta
				 */
				ResultTrasferisciFCService resultService = new ResultTrasferisciFCService(requestData,
						responseDataFC, idEvento, loadService.getRiepilogoOperazione(), tipoOperazione);
				responseData = resultService.execute();
				// aggiungo alla lista
				responseDataList.add(responseData);
			}
		} catch (SiesWsException e) {
			// inizializzo la lista di oggetti di tipo "ResponseData"
			responseDataList = new ArrayList<ResponseData>();
			// inizializzo l'oggetto di tipo "ResponseData"
			responseData = new ResponseData();
			// set di proprieta'
			responseData.setEsito(e.getResponseMessage());
			responseData.setDettaglioErrore(ExceptionUtils.getFullStackTrace(e));
			// info per il log
			logger.error(ExceptionUtils.getFullStackTrace(e));
			// aggiungo alla lista
			responseDataList.add(responseData);
		}

		// MEV 16 CUMULO: setto nuovi attributi nella richiesta
		String cup = "";
		if (!requestDataList.isEmpty() && requestDataList.size() == 1) {
			cup = requestDataList.get(0).getFoglioComplementare().getDatiPubblicoMinistero()
					.getCodiceUnivocoProvvedimento();
			if (PropertyUtil.isPresent(cup) && cup.contains("CUMULO"))
				cup = "CUMULO";
		}
		String isTrasferibile = "NO";
		if (!responseDataList.isEmpty() && responseDataList.size() == 1 && "CUMULO".equals(cup)) {
			ResponseData rd = responseDataList.get(0);
			if ("414".equals(rd.getCodiceEsito())) {
				isTrasferibile = "SI";
				// imposto la descrizione dell'esito
				String autorita = rd.getTitoloEsecutivo().getComuneUfficio();
				String dataEmissione = PropertyUtil
						.getDateToString(rd.getTitoloEsecutivo().getDataEmissione(), "dd/MM/yyyy");
				String soggetto = rd.getSoggetto().getCognome() + " " + rd.getSoggetto().getNome();
				String sesso = rd.getSoggetto().getSesso();
				if ("M".equals(sesso))
					sesso = "o";
				else
					sesso = "a";
				String dataNascita = PropertyUtil.getDateToString(rd.getSoggetto().getDataNascita(),
						"dd/MM/yyyy");
				String luogoNascita = "";
				if ("Italia".equals(rd.getSoggetto().getDescNazioneNascita()))
					luogoNascita = rd.getSoggetto().getDescComuneNascita();
				else
					luogoNascita = rd.getSoggetto().getDescNazioneNascita();
				String descCodiceEsito = rd.getDescCodiceEsito();
				descCodiceEsito = descCodiceEsito.replace("{0}", autorita);
				descCodiceEsito = descCodiceEsito.replace("{1}", dataEmissione);
				descCodiceEsito = descCodiceEsito.replace("{2}", soggetto);
				descCodiceEsito = descCodiceEsito.replace("{3}", sesso);
				descCodiceEsito = descCodiceEsito.replace("{4}", dataNascita);
				descCodiceEsito = descCodiceEsito.replace("{5}", luogoNascita);
				rd.setDescCodiceEsito(descCodiceEsito);
			}
		}
		// TODO: controllare i due if sopra [quelli con --> responseDataList.size() == 1]
		// (per il caso di richieste/risposte multiple) FIXME

		request.setAttribute("idEvento", idEvento);
		request.setAttribute("idUtente", idUtente);
		request.setAttribute("idSentenza", idSentenza);
		request.setAttribute("idFascicoloSiep", idFascicoloSiep);
		request.setAttribute("cup", cup);
		request.setAttribute("isTrasferibile", isTrasferibile);
		request.setAttribute("action", action);

		// gestione della risposta definitiva
		responseData = gestisciResponseDataList(responseDataList);
		// info per il log
		logger.info(responseData.getEsito());
		logger.info("Forward alla JSP per la visualizzazione del risultato");
		// imposto l'attributo nella richiesta
		request.setAttribute("response", responseData);
		/**
		 * Viene fatto il forward alla JSP per la visualizzazione del risultato
		 */
		getServletConfig().getServletContext().getRequestDispatcher("/risultatoTrasferimento.jsp")
				.forward(request, response);
	}

	/**
	 * Metodo che gestisce la risposta del servizio
	 * 
	 * @param responseDataList
	 * @return ResponseData
	 */
	private ResponseData gestisciResponseDataList(List<ResponseData> responseDataList) {

		// instanzio ed inizializzo un oggetto di tipo "ResponseData"
		ResponseData responseDataRet = new ResponseData();
		int contaResp = 1;
		boolean isRespToList = responseDataList.size() > 1 ? true : false;
		// ciclo sugli elementi della lista di passaggio e restituisco la risposta finale
		for (ResponseData rd : responseDataList) {
			if (rd.getChiaveNSC() != 0) {
				if (responseDataRet.getChiaveNSC() == 0
						|| responseDataRet.getChiaveNSC() != rd.getChiaveNSC())
					responseDataRet.setChiaveNSC(rd.getChiaveNSC());
			}

			if (rd.getChiaveSies() != 0) {
				if (responseDataRet.getChiaveSies() == 0
						|| responseDataRet.getChiaveSies() != rd.getChiaveSies())
					responseDataRet.setChiaveSies(rd.getChiaveSies());
			}

			if (rd.getDataOperazione() != null) {
				if (responseDataRet.getDataOperazione() == null
						|| responseDataRet.getDataOperazione().compareTo(rd.getDataOperazione()) != 0)
					responseDataRet.setDataOperazione(rd.getDataOperazione());
			}

			if (rd.getDescrizioneProvvedimento() != null) {
				if (responseDataRet.getDescrizioneProvvedimento() == null
						|| responseDataRet.getDescrizioneProvvedimento() != rd.getDescrizioneProvvedimento())
					responseDataRet.setDescrizioneProvvedimento(rd.getDescrizioneProvvedimento());
			}

			if (rd.getDettaglioErrore() != null) {
				if (responseDataRet.getDettaglioErrore() == null)
					if (isRespToList)
						responseDataRet.setDettaglioErrore(" " + contaResp + ") " + rd.getDettaglioErrore());
					else
						responseDataRet.setDettaglioErrore(rd.getDettaglioErrore());
				else if (!responseDataRet.getDettaglioErrore().equals(rd.getDettaglioErrore()))
					if (isRespToList)
						responseDataRet.setDettaglioErrore(responseDataRet.getDettaglioErrore() + " "
								+ contaResp + ") " + rd.getDettaglioErrore());
					else
						responseDataRet.setDettaglioErrore(
								responseDataRet.getDettaglioErrore() + "; " + rd.getDettaglioErrore());
			}

			if (rd.getEsito() != null) {
				if (responseDataRet.getEsito() == null)
					if (isRespToList)
						responseDataRet.setEsito(" " + contaResp + ") " + rd.getEsito());
					else
						responseDataRet.setEsito(rd.getEsito());
				else if (!responseDataRet.getEsito().equals(rd.getEsito()))
					if (isRespToList)
						responseDataRet.setEsito(
								responseDataRet.getEsito() + " " + contaResp + ") " + rd.getEsito());
					else
						responseDataRet.setEsito(responseDataRet.getEsito() + "; " + rd.getEsito());
			}

			// MEV 16 CUMULO: aggiunta impostazione di proprietÃ 
			if (rd.getDescCodiceEsito() != null) {
				if (responseDataRet.getDescCodiceEsito() == null)
					if (isRespToList)
						responseDataRet.setDescCodiceEsito(" " + contaResp + ") " + rd.getDescCodiceEsito());
					else
						responseDataRet.setDescCodiceEsito(rd.getDescCodiceEsito());
				else if (!responseDataRet.getDescCodiceEsito().equals(rd.getDescCodiceEsito()))
					if (isRespToList)
						responseDataRet.setDescCodiceEsito(responseDataRet.getDescCodiceEsito() + " "
								+ contaResp + ") " + rd.getDescCodiceEsito());
					else
						responseDataRet.setDescCodiceEsito(
								responseDataRet.getDescCodiceEsito() + "; " + rd.getDescCodiceEsito());
			}

			if (rd.getEstratto() != null) {
				if (responseDataRet.getEstratto() == null
						|| responseDataRet.getEstratto() != rd.getEstratto())
					responseDataRet.setEstratto(rd.getEstratto());
				if (rd.getId() != 0) {
					if (responseDataRet.getId() == 0 || responseDataRet.getId() != rd.getId())
						responseDataRet.setId(rd.getId());
				}
			}

			if (rd.getOperazione() != null) {
				if (responseDataRet.getOperazione() == null)
					if (isRespToList)
						responseDataRet.setOperazione(" " + contaResp + ") " + rd.getOperazione());
					else
						responseDataRet.setOperazione(rd.getOperazione());
				else if (!responseDataRet.getOperazione().equals(rd.getOperazione()))
					if (isRespToList)
						responseDataRet.setOperazione(responseDataRet.getOperazione() + " " + contaResp + ") "
								+ rd.getOperazione());
					else
						responseDataRet
								.setOperazione(responseDataRet.getOperazione() + "; " + rd.getOperazione());
			}

			if (rd.getSoggetto() != null) {
				if (responseDataRet.getSoggetto() == null
						|| responseDataRet.getSoggetto() != rd.getSoggetto())
					responseDataRet.setSoggetto(rd.getSoggetto());
			}

			if (rd.getTitoloEsecutivo() != null) {
				if (responseDataRet.getTitoloEsecutivo() == null
						|| responseDataRet.getTitoloEsecutivo() != rd.getTitoloEsecutivo())
					responseDataRet.setTitoloEsecutivo(rd.getTitoloEsecutivo());
			}

			if (rd.getTitoloGiudiziarioList() != null) {
				if (responseDataRet.getTitoloGiudiziarioList() == null
						|| responseDataRet.getTitoloGiudiziarioList() != rd.getTitoloGiudiziarioList())
					responseDataRet.setTitoloGiudiziarioList(rd.getTitoloGiudiziarioList());
			}

			if (rd.getElencoSinonimi() != null) {
				if (responseDataRet.getElencoSinonimi() == null
						|| responseDataRet.getElencoSinonimi() != rd.getElencoSinonimi())
					responseDataRet.setElencoSinonimi(rd.getElencoSinonimi());
			}

			// MEV 16 CUMULO: aggiunto elenco provvedimenti cumulabili
			if (rd.getElencoProvvedimentiNSC() != null) {
				if (responseDataRet.getElencoProvvedimentiNSC() == null
						|| responseDataRet.getElencoProvvedimentiNSC() != rd.getElencoProvvedimentiNSC())
					responseDataRet.setElencoProvvedimentiNSC(rd.getElencoProvvedimentiNSC());
			}

			// MEV 16 CUMULO: aggiunta impostazione di proprietÃ 
			if (rd.getDescEsitoCumulo() != null) {
				if (responseDataRet.getDescEsitoCumulo() == null
						|| responseDataRet.getDescEsitoCumulo() != rd.getDescEsitoCumulo())
					responseDataRet.setDescEsitoCumulo(rd.getDescEsitoCumulo());
			}

			// incremento il contatore delle response
			contaResp++;
		}

		// valore di ritorno
		return responseDataRet;
	}

	/**
	 * Gestisce la chiamata HTTP GET
	 * 
	 * @param request
	 * @param response
	 * @throws IOException
	 * @throws ServletException
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		processRequest(request, response);
	}

}