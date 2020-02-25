package siap.siep.penaresidua.action;

import java.math.BigDecimal;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.F3BException;
import siap.sico.evento.action.ICostantiEvento;
import siap.sico.libertaanticipata.controller.ILicenzaPeriodiLibAnticipata;
import siap.sico.libertaanticipata.model.LicenzaLibAnticipataModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siep.penaresidua.controller.IPenaResidua;
import siap.siep.penaresidua.model.PenaResiduaModel;
import siap.siep.util.SIEPLookupRemote;

/**
 * <p>
 * Title: ActLoadDettaglioPenaResiduaManuale
 * </p>
 * <p>
 * Description: Classe Action per la load dettaglio di PenaResidua
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

public class ActLoadDettaglioPenaResiduaManuale extends ActionSiap implements ICostantiPenaResidua {
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	@SuppressWarnings("rawtypes")
	public String processRequest() throws F3BException {

		// String lId = getRequestStringParameter(CAMPO_ID_PENA_RESIDUA);
		//
		// IPenaResidua lCtrl = SIEPLookupRemote.getPenaResiduaRemote();
		// PenaResiduaModel llPenMod = lCtrl.ExRicercaPenaResiduaByKey(new BigDecimal(lId));
		// setRequestAttribute("penaresidua", llPenMod);
		//
		// return PG_LOAD_DETTAGLIOPENARESIDUA_MANUALE;

		// =========================================================================
		// Recupero i dati da passare alla form di visualizzazione del dettaglio
		// della pena residua manuale
		// =========================================================================
		String lIdEvento = getRequestStringParameter(ICostantiEvento.CAMPO_ID_EVENTO);
		IPenaResidua lCtrl = SIEPLookupRemote.getPenaResiduaRemote();
		PenaResiduaModel llPenMod = lCtrl.ExRicercaPenaResiduaByIdEvento(new BigDecimal(lIdEvento));
		setRequestAttribute("penaresidua", llPenMod);

		Vector lVectorLibAnt = new Vector();
		ILicenzaPeriodiLibAnticipata lLibAntCTRL = SICOLookupRemote.getLicenzaPeriodiLibAntRemote();
		LicenzaLibAnticipataModel libAntMod = new LicenzaLibAnticipataModel();
		try {
			lVectorLibAnt = lLibAntCTRL.ExRicercaLicenzeByEve(new BigDecimal(lIdEvento));
			if (lVectorLibAnt != null && lVectorLibAnt.size() > 0) {
				libAntMod = (LicenzaLibAnticipataModel) lVectorLibAnt.elementAt(0);
			}
		} catch (Exception e) {
			// N.B. il metodo di ricerca rilancia una eccezione se non trova nulla
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("" + e.getMessage());
		}
		setRequestAttribute("libAntMod", libAntMod);

		// 20/05/2014 - Nuova Ordinanza L.A. - >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
		// Vettore lVectorLibAnt deve viaggiare nella request per estrarre i gg. di L.A. , L.A. Speciale, e di
		// Integrazione L.A. ,

		setRequestAttribute("LicenzeLibAnt", lVectorLibAnt);

		return PG_LOAD_DETTAGLIOPENARESIDUA_MANUALE;
	}
}