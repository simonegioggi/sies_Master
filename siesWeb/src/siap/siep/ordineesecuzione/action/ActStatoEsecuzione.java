package siap.siep.ordineesecuzione.action;

import java.math.BigDecimal;
import java.util.Vector;

import siap.sico.web.ActionSiap;
import siap.siep.fascicolo.action.ICostantiFascicoloSiep;
import siap.siep.fascicolo.controller.IFascicoloSiep;
import siap.siep.fascicolo.model.DettaglioFascicoloModel;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.ordineesecuzione.controller.IOrdineEsecuzione;
import siap.siep.util.SIEPLookupRemote;
import f3b.util.F3BException;
import f3b.web.IWebConstants;

/**
 * <p>
 * Title: ActRicercaEvento
 * </p>
 * <p>
 * Description: Classe Action per la ricerca di Evento
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: Bull
 * </p>
 * 
 * @version 1.0
 */
public class ActStatoEsecuzione extends ActionSiap implements ICostantiOrdineEsecuzione {

	@SuppressWarnings("rawtypes")
	public String processRequest() throws F3BException {

		if (isSessionAttributeNullObj("fascicolo")) {
			String lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
					+ "=siap.siep.fascicolo.action.ActLoadRicercaFascicoloUnivoco&"
					+ ICostantiFascicoloSiep.CAMPO_AZIONE_CHIAMANTE + "="
					+ "siap.siep.ordineesecuzione.action.ActStatoEsecuzione";

			return lPage;
		}

		isFascicoloSiepDiCompetenza();

		FascicoloSiepModel lFascicoloModel = (FascicoloSiepModel) getSessionAttribute("fascicolo");

		BigDecimal aId = lFascicoloModel.getIdFascicoloSiep();

		// FascicoloSiepController lCtrl = new FascicoloSiepController();
		IFascicoloSiep lCtrl = SIEPLookupRemote.getFascicoloSiepRemote();

		DettaglioFascicoloModel lDettaglio = null;
		lDettaglio = lCtrl.ExDettaglioFascicoloSiep(aId);

		if (lDettaglio == null)
			throw new F3BException(F3BException.USER_MESSAGE, "Fascicolo non presente");

		FascicoloSiepModel lFasMod = lDettaglio.getFascicoloSiep();

		setRequestAttribute("dettagliofascicolo", lDettaglio);

		// Sono al secondo passaggio avendo fatto uin update sulla gestione stato esecuzione
		if (!this.isRequestParameterNullObj("ModificaEseguita")) {
			String lModifica = this.getRequestStringParameter("ModificaEseguita");
			if (lModifica != null && lModifica.equals("SI")) {
				setRequestAttribute("ModificaEseguita", lModifica);
			}
		}
		// Inserisce nella session il fascicolo (contenente Soggetto e Sentenza)
		setSessionAttribute("fascicolo", lFasMod);

		String lPagina = "1";
		if (!isRequestParameterNullObj(IWebConstants.NUM_PAGE))
			lPagina = getRequestStringParameter(IWebConstants.NUM_PAGE);

		IOrdineEsecuzione lCtrlOrd = SIEPLookupRemote.getOrdineEsecuzioneRemote();
		try {
			Vector lVect = lCtrlOrd.ExRicercaTuttiEventiByFascicoloPaged(
					lFascicoloModel.getIdFascicoloSiep(), Integer.parseInt(lPagina));
			BigDecimal CountRisultati;
			if (isRequestParameterNullObj("CountRisultati")) {
				CountRisultati = lCtrlOrd.ExGetCountEventi(lFascicoloModel.getIdFascicoloSiep());
			} else
				CountRisultati = getRequestBigDecimalParameter("CountRisultati");

			setRequestAttribute("CountRisultati", CountRisultati);
			setRequestAttribute(IWebConstants.NUM_PAGE, lPagina);
			setRequestAttribute(IWebConstants.REQUEST_FOR_PAGING, getCompleteRequestURL());

			setRequestAttribute("eventi", lVect);

		} catch (Exception e) {

		}

		return PG_STATO_ESECUZIONE;
	}
}