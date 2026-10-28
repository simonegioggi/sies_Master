// 20261028-MEV_2026-105_INLAY - MEV Legge 140/2026: ricerca provvedimenti emessi ex art. 94 ter DPR 309/90
package siap.siep.scadenzario.action;

import java.math.BigDecimal;
import java.util.List;

import siap.siep.scadenzario.controller.IScadenzario;
import siap.siep.scadenzario.model.ScadenzarioModel;
import siap.siep.util.SIEPLookupRemote;
import f3b.web.IWebConstants;

/**
 * Ricerca paginata dei provvedimenti emessi ex art. 94 ter DPR 309/90.
 */
@SuppressWarnings("rawtypes")
public class ActRicercaProvvedimenti94Ter extends ActProvvedimenti94TerBase {

	public String processRequest() throws Exception {
		String lPagina = "1";
		if (!isRequestParameterNullObj(IWebConstants.NUM_PAGE)) {
			lPagina = getRequestStringParameter(IWebConstants.NUM_PAGE);
		}

		ScadenzarioModel lScaMod = buildFiltro();
		Boolean lDefiniti = getDefiniti();
		String lCodUfficio = this.getCodUfficioUtenteConnesso();

		IScadenzario lCtrl = SIEPLookupRemote.getScadenzarioRemote();

		BigDecimal lCount;
		if (isRequestParameterNullObj("CountRisultati")) {
			lCount = lCtrl.ExGetCountProvvedimenti94Ter(lScaMod, lDefiniti, lCodUfficio);
		} else {
			lCount = getRequestBigDecimalParameter("CountRisultati");
		}

		setRequestAttribute("CountRisultati", lCount);
		setRequestAttribute(IWebConstants.NUM_PAGE, lPagina);
		setRequestAttribute(IWebConstants.REQUEST_FOR_PAGING, getCompleteRequestURL());

		List lList = lCtrl.ExRicercaProvvedimenti94TerPaged(lScaMod, Integer.parseInt(lPagina), lDefiniti,
				lCodUfficio);
		setRequestAttribute("ListaProvv94Ter", lList);
		return PG_RICERCA_PROVVEDIMENTI_94TER;
	}
}
