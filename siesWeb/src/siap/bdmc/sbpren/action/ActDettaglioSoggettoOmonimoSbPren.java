package siap.bdmc.sbpren.action;

import java.math.BigDecimal;

import siap.sico.soggetto.controller.ISoggetto;
import siap.sico.soggetto.model.SoggettoModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.web.ISIAPCostantiWeb;

/**
 * <p>
 * Title: ActLoadDettaglioRegeSoggetto
 * </p>
 * <p>
 * Description: Classe Action per il dettaglio di un Soggetto Omonimo
 * </p>
 * <p>
 * Copyright: Copyright (c) 2006
 * </p>
 * <p>
 * Company: Bull
 * </p>
 */
public class ActDettaglioSoggettoOmonimoSbPren extends ActionSiap implements ICostantiSbPren {

	public String processRequest() throws Exception {

		BigDecimal lId = getRequestBigDecimalParameter("IdSoggetto");
		// Chiama il controller.
		ISoggetto lSogCtrl = SICOLookupRemote.getSoggettoRemote();
		SoggettoModel lSoggetto = lSogCtrl.ExRicercaSoggettoByKey(lId);

		// Inserisce il model soggetto nella request
		setRequestAttribute("SoggettoOmonimo", lSoggetto);
		setRequestAttribute(ISIAPCostantiWeb.CAMPO_AZIONE_CHIAMANTE,
				"siap.bdmc.sbpren.action.ActLoadDettaglioSbPren");

		return PG_LOAD_DETTAGLIOSOGGETTO;
	}

}