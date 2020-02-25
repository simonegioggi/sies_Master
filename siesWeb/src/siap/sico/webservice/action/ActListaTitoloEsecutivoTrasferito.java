package siap.sico.webservice.action;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Vector;

import siap.sico.util.SICOLookupRemote;
import siap.sico.webservice.controller.IWebServices;
import f3b.web.IWebConstants;

@SuppressWarnings("rawtypes")
public class ActListaTitoloEsecutivoTrasferito extends ActWsBase implements ICostantiNsc {

	public String processRequest() throws Exception {

		String lDestinazione = "", lStatoFascicolo = "";
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

		if (!this.isRequestParameterNullObj("cmbDestinazione")) {
			lDestinazione = getRequestStringParameter("cmbDestinazione");
		}

		if (!this.isRequestParameterNullObj("cmbStatoFascicolo")
				&& !getRequestStringParameter("cmbStatoFascicolo").equals("-")) {
			lStatoFascicolo = getRequestStringParameter("cmbStatoFascicolo");
		}

		IWebServices lCtrlWs = SICOLookupRemote.getWebServicesRemote();
		Vector lFascSoggSent = new Vector();

		lFascSoggSent = lCtrlWs.ExRicercaTitoloEsecutivoTrasferito(dataRicercaInizio, dataRicercaFine,
				lDestinazione, lStatoFascicolo, getCodUfficioUtenteConnesso(), Integer.parseInt(lPagina));
		// ExRicercaSoggettoPerDistretto(lSogMod,getCodDistrettoUtenteConnesso(),Integer.parseInt(lPagina));

		// Bottone di Ritorno
		setLinkRitorno();

		String lReturnPage = "";

		BigDecimal CountRisultati;
		CountRisultati = new BigDecimal(lFascSoggSent.size());

		if (isRequestParameterNullObj("CountRisultati")) {
			CountRisultati = lCtrlWs.ExGetCountRicercaTitoloEsecutivoTrasferito(dataRicercaInizio,
					dataRicercaFine, lDestinazione, lStatoFascicolo, getCodUfficioUtenteConnesso());
		} else {
			CountRisultati = getRequestBigDecimalParameter("CountRisultati");
		}

		setRequestAttribute("CountRisultati", CountRisultati);
		setRequestAttribute(IWebConstants.NUM_PAGE, lPagina);

		setRequestAttribute(IWebConstants.REQUEST_FOR_PAGING, getCompleteRequestURL());

		setRequestAttribute("FascSoggSent", lFascSoggSent);

		lReturnPage = PG_LOAD_LISTA_TITOLO_ESECUTIVO_TRASF;
		return lReturnPage;
	}

}