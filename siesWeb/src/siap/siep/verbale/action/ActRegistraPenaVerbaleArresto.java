package siap.siep.verbale.action;

/**
* <p>Title: ActInserisciPenaResidua</p>
* <p>Description: Classe Action per l'inserimento di PenaResidua</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/
import java.math.BigDecimal;

import f3b.util.DateUtils;
import f3b.util.F3BException;
import siap.sico.web.ActionSiap;
import siap.siep.penaresidua.controller.IPenaResidua;
import siap.siep.penaresidua.model.PenaResiduaModel;
import siap.siep.util.SIEPLookupRemote;
import siap.siep.verbale.controller.IVerbale;

public class ActRegistraPenaVerbaleArresto extends ActionSiap implements ICostantiVerbale {

	/**
	 * Azione di Aggiornamento del PenaResidua
	 * 
	 * @return Nome della pagina JSP da visualizzare al termine dell'elaborazione
	 * @throws F3BException
	 */
	public String processRequest() throws F3BException {

		// FascicoloSiepModel lFascMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");
		PenaResiduaModel lPenMod = new PenaResiduaModel();

		BigDecimal lIdPenaResidua = new BigDecimal(this.getRequestStringParameter("idpenaresidua"));

		IPenaResidua IPenRes = SIEPLookupRemote.getPenaResiduaRemote();
		lPenMod = IPenRes.ExRicercaPenaResiduaByKey(lIdPenaResidua);

		lPenMod.setDataFine(getRequestDateParameter("APV", "MPV", "GPV"));
		lPenMod.setCodOperatoreAggiornamento(this.getCodUtenteConnesso());
		lPenMod.setCodUfficioAggiornamento(this.getCodUfficioUtenteConnesso());
		lPenMod.setDataAggiornamento(DateUtils.getSysDate());
		lPenMod.setFlagValidato("S");

		IVerbale lCtrl = SIEPLookupRemote.getVerbaleRemote();
		lCtrl.ExRegistraPenaVerbaleArresto(lPenMod);

		if (isRequestParameterNullObj("flagVerbaleNotifica"))
			setRequestAttribute("flagVerbaleNotifica", "verbale");
		else if (getRequestStringParameter("flagVerbaleNotifica").equalsIgnoreCase("notifica"))
			setRequestAttribute("flagVerbaleNotifica", "notifica");

		return PG_REGISTRA_PENA_RESIDUA_VERBALE_ARRESTO;
	}

}