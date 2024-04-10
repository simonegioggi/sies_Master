package siap.siep.rateizzazionepp.action;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
import siap.sico.web.ActionSiap;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.rateizzazionepp.controller.IRateizzazionePP;
import siap.siep.util.SIEPLookupRemote;

/**
 * Classe per la cancellazione delle rateizzazioni
 * 
 * @since MEV_2023-13
 * @version 1.0
 */
public class ActCancellaRateizzazione extends ActionSiap implements ICostantiRateizzazionePP {

	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public String processRequest() throws F3BException {

		FascicoloSiepModel lFascMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");

		siesLogger.debug("Sono in cancellazione delle rateizzazioni per il fascicolo "
				+ lFascMod.getIdFascicoloSiep());

		// Effettuare la cancellazione
		IRateizzazionePP lRateCTRL = SIEPLookupRemote.getRateizzazionePPRemote();
		lRateCTRL.exCancellaRateizzazioniByIdFasc(lFascMod.getIdFascicoloSiep());

		// Ritorno al dettaglio della pena complessiva
		String lPage = "";
		lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
				+ "=siap.siep.penacomplessiva.action.ActLoadDettaglioPenaComplessiva&ChiaveFascicolo="
				+ lFascMod.getIdFascicoloSiep();
		return lPage;
	}

}