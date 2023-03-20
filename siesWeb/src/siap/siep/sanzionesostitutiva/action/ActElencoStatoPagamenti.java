package siap.siep.sanzionesostitutiva.action;

import java.math.BigDecimal;
import java.util.Iterator;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.Utils;
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
 * Title: ActElencoStatoPagamenti 
 * Description: Classe che mostra elenco stato pagamento bollettini PagoPA
 *
 * @author sgioggi
 * @since MEV_2023-13
 * @version 1.0
 */
public class ActElencoStatoPagamenti extends ActionSiap implements ICostantiSanzioneSostitutiva {

	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public String processRequest() throws Exception {

		// info per il log
		siesLogger.debug(getClass().getName() + ".processRequest: inizio");

		FascicoloSiepModel fsm = (FascicoloSiepModel) getSessionAttribute("fascicolo");
		BigDecimal idFascicolo = fsm.getIdFascicoloSiep();
		siesLogger.debug("ID_FASCICOLO = " + idFascicolo);
		BigDecimal idEvento = getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO);
		siesLogger.debug("ID_EVENTO = " + idEvento);
		// Ricerca lo stato dei pagamenti per id fascicolo
		IBollettinoPagopa ibp = SIEPLookupRemote.getBollettinoPagopaRemote();
		Vector<BollettinoPagopaModel> elencoStatoPagamenti = ibp
				.ExRicercaBollettinoPagopaByFasSieIdFascicoloSiep(idFascicolo);
		Iterator<BollettinoPagopaModel> iterBPM = elencoStatoPagamenti.iterator();
		BigDecimal importoPagato = new BigDecimal(0);
		BigDecimal importoDaPagare = new BigDecimal(0);
		while (iterBPM.hasNext()) {
			BollettinoPagopaModel bpm = iterBPM.next();
			if ("PA".equals(bpm.getStatoPagamento()))
				importoPagato = importoPagato.add(bpm.getImportoPagato());
			else
				importoDaPagare = importoDaPagare.add(bpm.getImportoRata());
		}
		setRequestAttribute("importoPagato", importoPagato.toString());
		setRequestAttribute("importoDaPagare", importoDaPagare.toString());
		setRequestAttribute("elencoStatoPagamenti", elencoStatoPagamenti);

		IRateizzazionePP irpp = SIEPLookupRemote.getRateizzazionePPRemote();
		Vector<EventoRateizzazionePPModel> listaRichiestaBollettini = irpp
				.exRicercaEventoRateizzazionePP(idFascicolo);
		if (!listaRichiestaBollettini.isEmpty()) {
			Vector<RateizzazionePPModel> rateizzazioni = listaRichiestaBollettini.firstElement()
					.getListaRateizzazioniPP();
			setRequestAttribute("evento", listaRichiestaBollettini.firstElement().getEvento());
			Iterator<RateizzazionePPModel> iter = rateizzazioni.iterator();
			String testo = "";
			int cont = 0;
			while (iter.hasNext()) {
				RateizzazionePPModel rata = iter.next();
				if (cont == 0)
					testo = "Importo da Pagare:  <font class='cRosso'>" + rata.getImportoDaPagare()
							+ "&#8364;</font> ";
				if ("R".equals(rata.getTipoRateizzazione())) { // RATE
					if (cont == 0) {
						testo += " con le seguenti modalit&agrave:";
						testo += "<ul>";
					}
					testo += "<li><font class='cViola'>" + "" + rata.getNumeroRate() + "</font> rate da "
							+ "<font class='cViola'>" + rata.getImportoRata() + "</font>";
					if (!Utils.isNullObj(rata.getScadenzaGiorni()) /* && cont == 0 */)
						testo += ", con scadenza pagamento entro n.ro giorni <font class='cViola'>"
								+ rata.getScadenzaGiorni().toString()
								+ "</font> dalla Notifica dell'Ingiunzione" + "</li>";
					else
						testo += "</li>";
					if (cont == rateizzazioni.size() - 1)
						testo += "</ul>";
				} else { // UNICA SOLUZIONE
					testo += " in un'unica soluzione";
					if (!Utils.isNullObj(rata.getScadenzaGiorni()))
						testo += ", termine di pagamento fissato entro " + rata.getScadenzaGiorni().toString()
								+ " giorni dalla Notifica dell'Avviso di Pagamento";
				}
				cont++;
			}
			setRequestAttribute("modalitaPagamento", testo);
		}

		// info per il log
		siesLogger.debug(getClass().getName() + ".processRequest: fine");

		return PG_ELENCO_STATO_PAGAMENTI;
	}

}