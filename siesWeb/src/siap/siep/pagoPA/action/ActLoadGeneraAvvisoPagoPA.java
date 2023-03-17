package siap.siep.pagoPA.action;

import java.math.BigDecimal;
import java.util.Vector;

import siap.sico.evento.action.ICostantiEvento;
import siap.sico.web.ActionSiap;
import siap.siep.rateizzazionepp.controller.IRateizzazionePP;
import siap.siep.rateizzazionepp.model.EventoRateizzazionePPModel;
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

	public String processRequest() throws Exception {

		BigDecimal idFascicolo = getRequestBigDecimalParameter(
				ICostantiEvento.CAMPO_FAS_SIE_ID_FASCICOLO_SIEP);
		// Ricerca i pagamenti per id evento
		IRateizzazionePP irpp = SIEPLookupRemote.getRateizzazionePPRemote();
		Vector<EventoRateizzazionePPModel> listaRichiestaBollettini = irpp
				.exRicercaEventoRateizzazionePP(idFascicolo);
		setRequestAttribute("listaRichiestaBollettini", listaRichiestaBollettini);

		return PG_LOAD_GENERA_AVVISO_PAGOPA;
	}

}