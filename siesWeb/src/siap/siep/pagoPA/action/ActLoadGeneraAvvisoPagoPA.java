package siap.siep.pagoPA.action;

import java.math.BigDecimal;
import java.util.Iterator;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import siap.sico.evento.action.ICostantiEvento;
import siap.sico.util.GeneraAvvisoPagoPAUtil;
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
	private static String codUtente = null;
	private static String codUfficio = null;

	public String processRequest() throws Exception {

		// info per il log
		siesLogger.debug(getClass().getName() + ".processRequest: inizio");

		// info utente ufficio collegato
		codUtente = getCodUtenteConnesso();
		codUfficio = getCodUfficioUtenteConnesso();

		BigDecimal idEvento = getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO);
		siesLogger.debug("ID_EVENTO = " + idEvento);
		FascicoloSiepModel fsm = (FascicoloSiepModel) getSessionAttribute("fascicolo");
		BigDecimal idFascicolo = fsm.getIdFascicoloSiep();
		siesLogger.debug("ID_FASCICOLO = " + idFascicolo);
		IBollettinoPagopa ibp = SIEPLookupRemote.getBollettinoPagopaRemote();
		// Ricerca lo stato dei pagamenti per id fascicolo
		Vector<BollettinoPagopaModel> elencoStatoPagamenti = ibp
				.ExRicercaBollettinoPagopaByFasSieIdFascicoloSiep(idFascicolo);
		boolean isElencoEmpty = elencoStatoPagamenti.isEmpty();
		// Ricerca i pagamenti per id evento
		IRateizzazionePP irpp = SIEPLookupRemote.getRateizzazionePPRemote();
		Vector<EventoRateizzazionePPModel> listaRichiestaBollettini = irpp
				.exRicercaEventoRateizzazionePP(idFascicolo);
		if (!listaRichiestaBollettini.isEmpty()) {
			Vector<RateizzazionePPModel> rateizzazioni = listaRichiestaBollettini.firstElement()
					.getListaRateizzazioniPP();
			setRequestAttribute("evento", listaRichiestaBollettini.firstElement().getEvento());
			if (isElencoEmpty) {
				// dalle rateizzazioni creo i bollettini
				Iterator<RateizzazionePPModel> iter = rateizzazioni.iterator();
				while (iter.hasNext()) {
					RateizzazionePPModel rata = iter.next();
					for (int i = 0; i < rata.getNumeroRate().intValue(); i++) {
						BollettinoPagopaModel bpm = GeneraAvvisoPagoPAUtil.popolaBollettino(rata,
								codUtente, codUfficio, "PN", i + 1);
						ibp.ExInserisciBollettinoPagopa(bpm);
					}
				}
				elencoStatoPagamenti = ibp.ExRicercaBollettinoPagopaByFasSieIdFascicoloSiep(idFascicolo);
			}
		}

		// poi li imposto nella pagina
		setRequestAttribute("elencoStatoPagamenti", elencoStatoPagamenti);

		// info per il log
		siesLogger.debug(getClass().getName() + ".processRequest: fine");

		return PG_LOAD_GENERA_AVVISO_PAGOPA;
	}

}