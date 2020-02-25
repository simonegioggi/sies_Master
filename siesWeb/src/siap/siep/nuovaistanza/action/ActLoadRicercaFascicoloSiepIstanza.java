package siap.siep.nuovaistanza.action;

/**
 * <p>Title: </p>
 * <p>Description: ActLoadRicercaFascicoloSiepIstanza</p>
 * <p>Copyright: Copyright (c) 2009</p>
 * <p>Company: Agile s.r.l.</p>
 * @author Dario Barbarini
 * @version 5.0
 */

import java.math.BigDecimal;

import f3b.util.F3BException;
import f3b.web.IWebConstants;
import siap.sico.web.ActionSiap;
import siap.siep.fascicolo.action.ICostantiFascicoloSiep;
import siap.siep.fascicolo.controller.IFascicoloSiep;
import siap.siep.fascicolo.model.DettaglioFascicoloModel;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.util.SIEPLookupRemote;

public class ActLoadRicercaFascicoloSiepIstanza extends ActionSiap implements ICostantiNuovaIstanza {
	public String processRequest() throws Exception {
		if (isSessionAttributeNullObj("fascicolo")) {
			String lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
					+ "=siap.siep.fascicolo.action.ActLoadRicercaFascicoloUnivoco&"
					+ ICostantiFascicoloSiep.CAMPO_AZIONE_CHIAMANTE + "="
					+ "siap.siep.nuovaistanza.action.ActLoadRicercaFascicoloSiepIstanza";

			return lPage;
		}

		isFascicoloSiepDiCompetenza();

		String lPagina = "1";
		if (!isRequestParameterNullObj(IWebConstants.NUM_PAGE))
			lPagina = getRequestStringParameter(IWebConstants.NUM_PAGE);

		FascicoloSiepModel lFascicoloModel = (FascicoloSiepModel) getSessionAttribute("fascicolo");

		BigDecimal aId = lFascicoloModel.getIdFascicoloSiep();

		IFascicoloSiep lCtrl = SIEPLookupRemote.getFascicoloSiepRemote();

		DettaglioFascicoloModel lDettaglio = null;
		lDettaglio = lCtrl.ExDettaglioFascicoloSiep(aId);

		if (lDettaglio == null)
			throw new F3BException(F3BException.USER_MESSAGE, "Fascicolo non presente");

		FascicoloSiepModel lFasMod = lDettaglio.getFascicoloSiep();

		setRequestAttribute("dettagliofascicolo", lDettaglio);

		// Inserisce nella session il fascicolo (contenente Soggetto e Sentenza)
		setSessionAttribute("fascicolo", lFasMod);

		setRequestAttribute("CountRisultati", new BigDecimal("1"));
		setRequestAttribute(IWebConstants.NUM_PAGE, lPagina);

		return IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
				+ "=siap.siep.nuovaistanza.action.ActLoadInserisciIstanzaPerProcedimentoSiep&"
				+ ICostantiFascicoloSiep.CAMPO_CHIAVE_PROGR + "=" + lFasMod.getChiaveProgr() + "&"
				+ ICostantiFascicoloSiep.CAMPO_CHIAVE_ANNO + "=" + lFasMod.getChiaveAnno();
	}

}