package siap.sius.statistiche.action;

import java.math.BigDecimal;
import java.util.Collection;

import f3b.web.IWebConstants;
import siap.sius.ActionSius;
import siap.sius.statistiche.controller.IStatisticheSius;
import siap.sius.statistiche.model.PenaSostitutivaSospesaModel;
import siap.sius.util.SIUSLookupRemote;

/**
 * MEV_2026-124_SG: monitoraggio dei procedimenti con pena sostitutiva sospesa e priva di una successiva
 * ripresa (Statistiche/Monitoraggio - Ricerche).
 */
public class ActRicercaPeneSostitutiveSospese extends ActionSius implements ICostantiStatistiche {

	public String processRequest() throws Exception {
		this.setLinkRitorno();
		String lPagina = "1";
		if (!isRequestParameterNullObj(IWebConstants.NUM_PAGE))
			lPagina = getRequestStringParameter(IWebConstants.NUM_PAGE);

		String lCodUfficio = getUtenteConnesso().getUfficioUtente().getCodUfficio();
		IStatisticheSius lCtrl = SIUSLookupRemote.getStatisticheSiusRemote();
		Collection<PenaSostitutivaSospesaModel> lElenco = lCtrl
				.ExRicercaPeneSostitutiveSospesePaginata(lCodUfficio, Integer.parseInt(lPagina));

		BigDecimal lRecords;
		if (isRequestParameterNullObj("CountRisultati"))
			lRecords = lCtrl.ExGetNumRicercaPeneSostitutiveSospese(lCodUfficio);
		else
			lRecords = getRequestBigDecimalParameter("CountRisultati");

		setRequestAttribute("CountRisultati", lRecords);
		setRequestAttribute(IWebConstants.NUM_PAGE, lPagina);
		setRequestAttribute(IWebConstants.REQUEST_FOR_PAGING, getCompleteRequestURL());
		setRequestAttribute("elencoPeneSospese", lElenco);

		return PG_RICERCA_PENE_SOSTITUTIVE_SOSPESE;
	}
}
