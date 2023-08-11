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
 * Classe per il caricamento della modifica delle rateizzazioni
 *
 * @since MEV_2023-13
 * @version 1.0
 */
public class ActLoadModificaRateizzazione extends ActionSiap implements ICostantiRateizzazionePP {

	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public String processRequest() throws F3BException {

		FascicoloSiepModel fsm = (FascicoloSiepModel) getSessionAttribute("fascicolo");

		// Recupero la pena Complessiva da visualizzare (multa e ammenda)
		IPenaComplessiva ipc = SIEPLookupRemote.getPenaComplessivaRemote();
		DettaglioPenaComplessivaModel dpcm = ipc
				.ExRicercaPenaCompSanzioneSostContinuazioniByIdFascicolo(fsm.getIdFascicoloSiep());
		setRequestAttribute("dettaglioPenaComplessiva", dpcm);

		siesLogger.debug("Sono in load modifica reteizzazione per il fascicolo " + fsm.getIdFascicoloSiep());
		Vector<RateizzazionePPModel> listaRateizzazioni = new Vector<>();

		// Ricerca i pagamenti per id Facicolo
		IRateizzazionePP irpp = SIEPLookupRemote.getRateizzazionePPRemote();
		listaRateizzazioni = irpp.exRicercaRateizzazioniByIdFasc(fsm.getIdFascicoloSiep());
		if (!listaRateizzazioni.isEmpty()) {
			Iterator<RateizzazionePPModel> iterRPPM = listaRateizzazioni.iterator();
			while (iterRPPM.hasNext()) {
				RateizzazionePPModel rppm = iterRPPM.next();
				BigDecimal idEvento = rppm.getEveIdEvento();
				if (!Utils.isNullObj(idEvento)) {
					IEvento ie = SICOLookupRemote.getEventoRemote();
					EventoModel em = ie.ExRicercaEventoByKey(idEvento);
					if ("A".equals(em.getFlagDocumentoRegistrato()))
						iterRPPM.remove();
				}
			}
		}

		// imposto valori nella request
		setRequestAttribute("listaRateizzazioni", listaRateizzazioni);
		setRequestAttribute("modalita", "M");

		return PG_LOAD_INSERISCI_RATEIZZAZIONE_PP;
	}

}