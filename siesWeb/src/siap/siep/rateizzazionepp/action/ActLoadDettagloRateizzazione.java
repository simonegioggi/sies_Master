package siap.siep.rateizzazionepp.action;

import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.F3BException;
import siap.sico.web.ActionSiap;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.penacomplessiva.controller.IPenaComplessiva;
import siap.siep.penacomplessiva.model.DettaglioPenaComplessivaModel;
import siap.siep.rateizzazionepp.controller.IRateizzazionePP;
import siap.siep.rateizzazionepp.model.RateizzazionePPModel;
import siap.siep.util.SIEPLookupRemote;

public class ActLoadDettagloRateizzazione extends ActionSiap implements ICostantiRateizzazionePP {

	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public String processRequest() throws F3BException {

		// info per il log
		siesLogger.debug(getClass().getName() + ".processRequest: inizio");

		FascicoloSiepModel lFascMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");

		// Recupero la pena Complessiva da visualizzare (multa e ammenda)
		IPenaComplessiva lCtrl = SIEPLookupRemote.getPenaComplessivaRemote();
		DettaglioPenaComplessivaModel lDettMod = lCtrl
				.ExRicercaPenaCompSanzioneSostContinuazioniByIdFascicolo(lFascMod.getIdFascicoloSiep());
		setRequestAttribute("dettaglioPenaComplessiva", lDettMod);

		// Ricerca i pagamenti per id Facicolo
		Vector<RateizzazionePPModel> listaRateizzazioni = new Vector<>();
		IRateizzazionePP lRateCTRL = SIEPLookupRemote.getRateizzazionePPRemote();
		listaRateizzazioni = lRateCTRL.exRicercaRateizzazioniByIdFasc(lFascMod.getIdFascicoloSiep());

		setRequestAttribute("listaRateizzazioni", listaRateizzazioni);

		// info per il log
		siesLogger.debug(getClass().getName() + ".processRequest: fine");

		return PG_LOAD_DETTAGLIO_RATEIZZAZIONE_PP;
	}

}