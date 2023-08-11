package siap.siep.rateizzazionepp.action;

import java.math.BigDecimal;
import java.util.Iterator;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.F3BException;
import f3b.util.Utils;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.penacomplessiva.controller.IPenaComplessiva;
import siap.siep.penacomplessiva.model.DettaglioPenaComplessivaModel;
import siap.siep.rateizzazionepp.controller.IRateizzazionePP;
import siap.siep.rateizzazionepp.model.RateizzazionePPModel;
import siap.siep.util.SIEPLookupRemote;

/**
 * Classe per il caricamento del dettaglio rateizzazioni
 * 
 * @since MEV_2023-13
 * @version 1.0
 */
public class ActLoadDettagloRateizzazione extends ActionSiap implements ICostantiRateizzazionePP {

	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public String processRequest() throws F3BException {

		// info per il log
		siesLogger.debug(getClass().getName() + ".processRequest: inizio");

		FascicoloSiepModel fsm = (FascicoloSiepModel) getSessionAttribute("fascicolo");

		// Recupero la pena Complessiva da visualizzare (multa e ammenda)
		IPenaComplessiva ipc = SIEPLookupRemote.getPenaComplessivaRemote();
		DettaglioPenaComplessivaModel dpcm = ipc
				.ExRicercaPenaCompSanzioneSostContinuazioniByIdFascicolo(fsm.getIdFascicoloSiep());
		setRequestAttribute("dettaglioPenaComplessiva", dpcm);

		// Ricerca i pagamenti per id Facicolo
		Vector<RateizzazionePPModel> listaRateizzazioni = new Vector<>();
		IRateizzazionePP irpp = SIEPLookupRemote.getRateizzazionePPRemote();
		listaRateizzazioni = irpp.exRicercaRateizzazioniByIdFasc(fsm.getIdFascicoloSiep());
		// MEV_2023-33: aggiunto controllo per storicizzazione evento OIP
		// Ricerca i pagamenti per id Fascicolo
		boolean isEventoRateizzazioneAnnullato = false;
		if (!listaRateizzazioni.isEmpty()) {
			Iterator<RateizzazionePPModel> iterRPPM = listaRateizzazioni.iterator();
			while (iterRPPM.hasNext()) {
				RateizzazionePPModel rppm = iterRPPM.next();
				BigDecimal idEvento = rppm.getEveIdEvento();
				if (!Utils.isNullObj(idEvento)) {
					IEvento ie = SICOLookupRemote.getEventoRemote();
					EventoModel em = ie.ExRicercaEventoByKey(idEvento);
					rppm.setStoricizzato("A".equals(em.getFlagDocumentoRegistrato()));
					isEventoRateizzazioneAnnullato = "A".equals(em.getFlagDocumentoRegistrato());
				} else
					isEventoRateizzazioneAnnullato = false;
			}
		}

		setRequestAttribute("isEventoRateizzazioneAnnullato", "" + isEventoRateizzazioneAnnullato);
		setRequestAttribute("listaRateizzazioni", listaRateizzazioni);

		// info per il log
		siesLogger.debug(getClass().getName() + ".processRequest: fine");

		return PG_LOAD_DETTAGLIO_RATEIZZAZIONE_PP;
	}

}