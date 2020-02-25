package siap.siep.alias.action;

import java.math.BigDecimal;
import java.util.Vector;

import f3b.util.F3BException;
import f3b.web.IWebConstants;
import siap.sico.soggetto.controller.ISoggetto;
import siap.sico.soggetto.model.SoggettoModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siep.alias.controller.AliasController;
import siap.siep.fascicolo.action.ICostantiFascicoloSiep;
import siap.siep.fascicolo.controller.IFascicoloSiep;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.util.SIEPLookupRemote;

/**
 * <p>
 * Title: ActRicercaAlias
 * </p>
 * <p>
 * Description: Classe Action per la ricerca di Alias
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
@SuppressWarnings("rawtypes")
public class ActRicercaAliasFascicolo extends ActionSiap implements ICostantiAlias {

	public String processRequest() throws F3BException {

		// AliasModel lAliMod = new AliasModel();

		String lPagina = "1";
		if (!isRequestParameterNullObj(IWebConstants.NUM_PAGE))
			lPagina = getRequestStringParameter(IWebConstants.NUM_PAGE);

		if (!isRequestParameterNullObj(ICostantiFascicoloSiep.CAMPO_ID_FASCICOLO_SIEP)) {
			BigDecimal lIdFasc = getRequestBigDecimalParameter(
					ICostantiFascicoloSiep.CAMPO_ID_FASCICOLO_SIEP);
			// FascicoloSiepModel lFasMod= new FascicoloSiepModel();
			// FascicoloSiepController lCtrl = new FascicoloSiepController();
			IFascicoloSiep lCtrl = SIEPLookupRemote.getFascicoloSiepRemote();
			FascicoloSiepModel lFasMod = lCtrl.ExRicercaFascicoloByKey(lIdFasc);

			AliasController lCtrlAl = new AliasController();
			Vector lVect = lCtrlAl.ExRicercaAliasByIdSoggettoPaged(lFasMod.getSogIdSoggetto(),
					Integer.parseInt(lPagina));
			setRequestAttribute("aliasvect", lVect);

			ISoggetto lSogCtrl = SICOLookupRemote.getSoggettoRemote();
			SoggettoModel lSoggetto = lSogCtrl.ExRicercaSoggettoByKey(lFasMod.getSogIdSoggetto());
			setRequestAttribute("soggetto", lSoggetto);

			BigDecimal CountRisultati;
			if (isRequestParameterNullObj("CountRisultati")) {
				CountRisultati = lCtrlAl.ExGetCountAliasByIdSoggetto(lFasMod.getSogIdSoggetto());
			} else
				CountRisultati = getRequestBigDecimalParameter("CountRisultati");

			setRequestAttribute("CountRisultati", CountRisultati);
			setRequestAttribute(IWebConstants.NUM_PAGE, lPagina);

			setRequestAttribute(IWebConstants.REQUEST_FOR_PAGING, getCompleteRequestURL());

		}
		// setRequestAttribute("soggetto", lFasMod.getSogIdSoggetto());

		return PG_RICERCAALIAS;
	}

}