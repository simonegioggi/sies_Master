package siap.sico.webservice.action;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Vector;

import siap.sico.trasmissione.controller.ITrasmissioni;
import siap.sico.util.SICOLookupRemote;
import f3b.web.IWebConstants;

@SuppressWarnings("rawtypes")
public class ActListaEsitiRicercaTrasmissioni extends ActWsBase implements ICostantiNsc {

	public String processRequest() throws Exception {

		String lTipoTrasmissione = "", lEsitoTrasmissione = "";
		String lPagina = "1";

		if (!isRequestParameterNullObj(IWebConstants.NUM_PAGE)) {
			lPagina = getRequestStringParameter(IWebConstants.NUM_PAGE);
		}

		// TrasmissioniModel lTrasmissioniModel = new TrasmissioniModel();
		Date dataRicercaInizio = null;
		if (!this.isRequestParameterNullObj("GGDataTrasmissioneInizio")
				&& getRequestStringParameter("AADataTrasmissioneInizio").length() > 1) {
			dataRicercaInizio = (getRequestDateParameter("AADataTrasmissioneInizio",
					"MMDataTrasmissioneInizio", "GGDataTrasmissioneInizio"));
		}
		Date dataRicercaFine = null;
		if (!this.isRequestParameterNullObj("GGDataTrasmissioneInizio")
				&& getRequestStringParameter("AADataTrasmissioneInizio").length() > 1) {
			dataRicercaFine = (getRequestDateParameter("AADataTrasmissioneFine", "MMDataTrasmissioneFine",
					"GGDataTrasmissioneFine"));
		}

		if (!this.isRequestParameterNullObj("cmbTipoTrasmissione")
				&& !getRequestStringParameter("cmbTipoTrasmissione").equals("-")) {
			lTipoTrasmissione = getRequestStringParameter("cmbTipoTrasmissione");
		}

		if (!this.isRequestParameterNullObj("cmbEsitoTrasmissione")
				&& !getRequestStringParameter("cmbEsitoTrasmissione").equals("-")) {
			lEsitoTrasmissione = getRequestStringParameter("cmbEsitoTrasmissione");
		}

		ITrasmissioni lCtrlTrasm = SICOLookupRemote.getTrasmissioniRemote();
		Vector lTrasmissioni = new Vector();

		lTrasmissioni = lCtrlTrasm.ExRicercaTrasmissioniPerDateTipoEsito(dataRicercaInizio, dataRicercaFine,
				lTipoTrasmissione, lEsitoTrasmissione, getCodUfficioUtenteConnesso(),
				Integer.parseInt(lPagina));
		// ExRicercaSoggettoPerDistretto(lSogMod,getCodDistrettoUtenteConnesso(),Integer.parseInt(lPagina));

		// Bottone di Ritorno
		setLinkRitorno();

		String lReturnPage = "";

		BigDecimal CountRisultati;
		CountRisultati = new BigDecimal(lTrasmissioni.size());

		if (isRequestParameterNullObj("CountRisultati")) {
			CountRisultati = lCtrlTrasm.ExGetCountPerDateTipoEsito(dataRicercaInizio, dataRicercaFine,
					lTipoTrasmissione, lEsitoTrasmissione, getCodUfficioUtenteConnesso());
		} else {
			CountRisultati = getRequestBigDecimalParameter("CountRisultati");
		}

		setRequestAttribute("CountRisultati", CountRisultati);
		setRequestAttribute(IWebConstants.NUM_PAGE, lPagina);

		setRequestAttribute(IWebConstants.REQUEST_FOR_PAGING, getCompleteRequestURL());

		setRequestAttribute("trasmissioni", lTrasmissioni);

		lReturnPage = PG_LOAD_LISTA_ESITI_RICERCATRASMISSIONI;
		return lReturnPage;

		/*
		 * setRequestAttribute(IWebConstants.MESSAGE_TEXT, "Inserimento Effettuato Correttamente!"); return
		 * IWebConstants.PG_MESSAGE;
		 */
	}

}