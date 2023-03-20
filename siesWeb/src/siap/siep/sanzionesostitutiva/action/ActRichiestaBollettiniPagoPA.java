package siap.siep.sanzionesostitutiva.action;

import java.math.BigDecimal;
import java.util.Iterator;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.StringUtils;
import f3b.util.Utils;
import siap.sico.web.ActionSiap;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.rateizzazionepp.controller.IRateizzazionePP;
import siap.siep.rateizzazionepp.model.EventoRateizzazionePPModel;
import siap.siep.rateizzazionepp.model.RateizzazionePPModel;
import siap.siep.util.SIEPLookupRemote;

/**
 * Classe per la gestione della richiesta bollettini PagoPA
 *
 * @author sgioggi
 * @since MEV_2023-13
 * @version 1.0
 */
public class ActRichiestaBollettiniPagoPA extends ActionSiap implements ICostantiSanzioneSostitutiva {

	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public String processRequest() throws Exception {

		// info per il log
		siesLogger.debug(getClass().getName() + ".processRequest: inizio");
		setLinkRitorno();

		FascicoloSiepModel fsm = (FascicoloSiepModel) getSessionAttribute("fascicolo");
		BigDecimal idFascicolo = fsm.getIdFascicoloSiep();

		IRateizzazionePP irpp = SIEPLookupRemote.getRateizzazionePPRemote();
		Vector<EventoRateizzazionePPModel> listaRichiestaBollettini = irpp
				.exRicercaEventoRateizzazionePP(idFascicolo);
		setRequestAttribute("listaRichiestaBollettini", listaRichiestaBollettini);
		if (!listaRichiestaBollettini.isEmpty()) {
			Vector<RateizzazionePPModel> rateizzazioni = listaRichiestaBollettini.firstElement()
					.getListaRateizzazioniPP();
			Iterator<RateizzazionePPModel> iter = rateizzazioni.iterator();
			String testo = "";
			int cont = 0;
			while (iter.hasNext()) {
				RateizzazionePPModel rata = iter.next();
				if (cont == 0)
					testo = "Importo da Pagare: " + StringUtils.toEuroFormat(rata.getImportoDaPagare()) + " ";
				if ("R".equals(rata.getTipoRateizzazione())) { // RATE
					if (cont == 0) {
						testo += "in:";
						testo += "<ul>";
					}
					testo += "<li>" + "" + rata.getNumeroRate() + " rate da " + "" + rata.getImportoRata();
					if (!Utils.isNullObj(rata.getScadenzaGiorni()) && cont == 0)
						testo += ", termine di pagamento fissato entro " + rata.getScadenzaGiorni().toString()
								+ " giorni dalla Notifica dell'Avviso di Pagamento" + "</li>";
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

		return PG_ELENCO_RICHIESTA_BOLLETTINI;
	}

}