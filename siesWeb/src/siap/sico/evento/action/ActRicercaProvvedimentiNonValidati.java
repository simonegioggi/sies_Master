package siap.sico.evento.action;

/**
 * <p>Title: ActRicercaProvvedimentiNonValidati</p>
 * <p>Description: Classe Action per la ricerca dei provvedimenti non validati</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: Bull</p>
 * @version 1.0
 */

import java.math.BigDecimal;
import java.util.Vector;

import siap.sico.evento.controller.IEventoSimeone;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siep.ordineesecuzione.action.ICostantiOrdineEsecuzione;
import f3b.web.IWebConstants;

@SuppressWarnings("rawtypes")
public class ActRicercaProvvedimentiNonValidati extends ActionSiap implements ICostantiOrdineEsecuzione {

	public String processRequest() throws Exception {

		String lPagina = "1";
		if (!isRequestParameterNullObj(IWebConstants.NUM_PAGE))
			lPagina = getRequestStringParameter(IWebConstants.NUM_PAGE);

		// Ordinamento di default
		String lOrdinamento = "EM";

		// Lettura ordine data_invio
		if (!isRequestParameterNullObj("lOrdinamento")) {
			lOrdinamento = getRequestStringParameter("lOrdinamento");
		} else if (!isRequestAttributeNullObj("lOrdinamento")) {
			lOrdinamento = (String) getRequestAttribute("lOrdinamento");
		}

		this.setRequestAttribute("ordinamento", lOrdinamento);

		IEventoSimeone lCtrl = SICOLookupRemote.getEventoSimeoneRemote();
		// Nell'Elenco Provvedimenti PM anche i Verbali. Luigi 3-2-06
		// Nell'Elenco Provvedimenti PM anche le richieste. Dario 14-3-06
		// Nell'Elenco Provvedimenti PM anche le Pene accessorie. Vincenzo 29-3-06
		String[] lTipoEvento = { "01", "07", "02", "16", "17", "18" };
		String[] lTipoProv = { "03", "02" };
		Vector lVect = lCtrl.ExRicercaEventoByFascicoloSiepTipEventoNOTTipProvPaged(null,
				// getCodUfficioUtenteConnesso(),
				getUfficioUtenteConnesso(), lTipoEvento, lTipoProv, Integer.parseInt(lPagina), lOrdinamento);
		setRequestAttribute("eventi", lVect);

		BigDecimal CountRisultati;
		if (isRequestParameterNullObj("CountRisultati")) {
			CountRisultati = lCtrl.ExGetCountEventoByFascicoloSiepTipEventoNOTTipProvPaged(null,
					getCodUfficioUtenteConnesso(), lTipoEvento, lTipoProv);
		} else {
			CountRisultati = getRequestBigDecimalParameter("CountRisultati");
		}

		setRequestAttribute("CountRisultati", CountRisultati);
		setRequestAttribute(IWebConstants.NUM_PAGE, lPagina);

		setRequestAttribute(IWebConstants.REQUEST_FOR_PAGING, getCompleteRequestURL());

		return PG_RICERCA_PROVVEDIMENTI_NON_VALIDATI;
	}

}