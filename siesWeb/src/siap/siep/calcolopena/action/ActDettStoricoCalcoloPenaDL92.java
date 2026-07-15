package siap.siep.calcolopena.action;

import java.math.BigDecimal;

import siap.sico.ufficio.model.UfficioModel;
import siap.sico.web.ActionSiap;
import siap.siep.calcolopenadl92.controller.ICalcoloPenaDL92;
import siap.siep.calcolopenadl92.model.CalcoloPenaDL92ModelDB;
import siap.siep.util.SIEPLookupRemote;

/**
 * ActDettStoricoCalcoloPenaDL92 - Classe Action per il dettaglio dello storico
 *
 * @since MEV-2026_1
 */
public class ActDettStoricoCalcoloPenaDL92 extends ActionSiap implements ICostantiCalcoloPena {

	// private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	@Override
	public String processRequest() throws Exception {

		BigDecimal idCalcolo = null;

		idCalcolo = getRequestBigDecimalParameter(CAMPO_ID_CALCOLO_PENA_DL92);

		ICalcoloPenaDL92 lCalcDL92Ctrl = SIEPLookupRemote.getCalcoloPenaDL92();

		CalcoloPenaDL92ModelDB lCalcoloModel = lCalcDL92Ctrl.GetDettaglioStoricoById(idCalcolo);

		UfficioModel lUfficioCalcolo = getUfficioByCodUfficio(lCalcoloModel.getCodUfficioInserimento());
		setRequestAttribute("EsitoCalcolo", lCalcoloModel);
		setRequestAttribute("UfficioCalcolo", lUfficioCalcolo);

		if (!isRequestParameterNullObj("fromLista")) {
			setRequestAttribute("fromLista", "S");
		}

		return PG_DETT_STORICO_CALCPENA_DL92;
	}

}