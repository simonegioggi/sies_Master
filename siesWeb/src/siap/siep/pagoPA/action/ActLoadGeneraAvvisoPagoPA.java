package siap.siep.pagoPA.action;

import java.math.BigDecimal;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import siap.sico.evento.action.ICostantiEvento;
import siap.sico.web.ActionSiap;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.pagoPA.controller.IBollettinoPagopa;
import siap.siep.pagoPA.model.BollettinoPagopaModel;
import siap.siep.rateizzazionepp.controller.IRateizzazionePP;
import siap.siep.rateizzazionepp.model.EventoRateizzazionePPModel;
import siap.siep.rateizzazionepp.model.RateizzazionePPModel;
import siap.siep.util.SIEPLookupRemote;

/**
 * Title: ActLoadGeneraAvvisoPagoPA 
 * Description: Classe che permette di generare un avviso di pagamento per PagoPA
 *
 * @author sgioggi
 * @since MEV_2023-13
 * @version 1.0
 */
public class ActLoadGeneraAvvisoPagoPA extends ActionSiap implements ICostantiPagoPA {

	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public String processRequest() throws Exception {

		// info per il log
		siesLogger.debug(getClass().getName() + ".processRequest: inizio");

		BigDecimal idEvento = getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO);
		siesLogger.debug("ID_EVENTO = " + idEvento);
		FascicoloSiepModel fsm = (FascicoloSiepModel) getSessionAttribute("fascicolo");
		BigDecimal idFascicolo = fsm.getIdFascicoloSiep();
		siesLogger.debug("ID_FASCICOLO = " + idFascicolo);
		// Ricerca i pagamenti per id evento
		IRateizzazionePP irpp = SIEPLookupRemote.getRateizzazionePPRemote();
		Vector<EventoRateizzazionePPModel> listaRichiestaBollettini = irpp
				.exRicercaEventoRateizzazionePP(idFascicolo);
		if (!listaRichiestaBollettini.isEmpty()) {
			Vector<RateizzazionePPModel> rateizzazioni = listaRichiestaBollettini.firstElement()
					.getListaRateizzazioniPP();
			setRequestAttribute("evento", listaRichiestaBollettini.firstElement().getEvento());
			// dalle rateizzazioni creo i bollettini
			// TODO
			// poi li imposto nella pagina
		}
		// Ricerca lo stato dei pagamenti per id fascicolo
		IBollettinoPagopa ibp = SIEPLookupRemote.getBollettinoPagopaRemote();
		Vector<BollettinoPagopaModel> elencoStatoPagamenti = ibp
				.ExRicercaBollettinoPagopaByFasSieIdFascicoloSiep(idFascicolo);
		setRequestAttribute("elencoStatoPagamenti", elencoStatoPagamenti);

		// info per il log
		siesLogger.debug(getClass().getName() + ".processRequest: fine");

		return PG_LOAD_GENERA_AVVISO_PAGOPA;
	}

}