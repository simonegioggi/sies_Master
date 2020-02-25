package siap.siep.sollecito.action;

import java.math.BigDecimal;
import java.util.Vector;

import f3b.util.F3BException;
import f3b.web.IWebConstants;
import siap.sico.web.ActionSiap;
import siap.siep.fascicolo.action.ICostantiFascicoloSiep;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.sollecito.controller.ISollecito;
import siap.siep.util.SIEPLookupRemote;

public class ActRicercaStatoAttiSollecito extends ActionSiap implements ICostantiSollecito {

	@SuppressWarnings("rawtypes")
	public String processRequest() throws F3BException {

		if (isSessionAttributeNullObj("fascicolo")) {
			String lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
					+ "=siap.siep.fascicolo.action.ActLoadRicercaFascicoloUnivoco&"
					+ ICostantiFascicoloSiep.CAMPO_AZIONE_CHIAMANTE + "="
					+ "siap.siep.sollecito.action.ActRicercaStatoAttiSollecito";

			return lPage;
		}
		String lPagina = "1";
		if (!isRequestParameterNullObj(IWebConstants.NUM_PAGE)) {
			lPagina = getRequestStringParameter(IWebConstants.NUM_PAGE);
		}

		int appo = Integer.parseInt(lPagina);
		ISollecito lSoll = SIEPLookupRemote.getSollecitoRemote();
		FascicoloSiepModel lFascicoloModel = (FascicoloSiepModel) getSessionAttribute("fascicolo");
		Vector lVect = lSoll.ExRicercaEventiPerFascicolo(lFascicoloModel.getIdFascicoloSiep(), appo);
		BigDecimal CountRisultati;
		if (isRequestParameterNullObj("CountRisultati")) {
			CountRisultati = new BigDecimal(lSoll.getEventiSize());
		} else
			CountRisultati = getRequestBigDecimalParameter("CountRisultati");

		setRequestAttribute("CountRisultati", CountRisultati);
		setRequestAttribute(IWebConstants.NUM_PAGE, lPagina);

		setRequestAttribute(IWebConstants.REQUEST_FOR_PAGING, getCompleteRequestURL());

		setRequestAttribute("atti", lVect);

		return PG_RICERCA_STATO_ATTI;
	}

}