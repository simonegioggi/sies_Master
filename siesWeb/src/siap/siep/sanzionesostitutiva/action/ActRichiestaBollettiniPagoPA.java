package siap.siep.sanzionesostitutiva.action;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.F3BException;
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

	public String processRequest() throws F3BException {

		// info per il log
		siesLogger.debug(getClass().getName() + ".processRequest: inizio");

		// if (!isSessionAttributeNullObj("fascicolo") && getSessionAttribute("fascicolo") != null)
		FascicoloSiepModel fsm = (FascicoloSiepModel) getSessionAttribute("fascicolo");
		BigDecimal idFascicolo = fsm.getIdFascicoloSiep();

		IRateizzazionePP irpp = SIEPLookupRemote.getRateizzazionePPRemote();
		Vector<EventoRateizzazionePPModel> listaRichiestaBollettini = irpp
				.exRicercaEventoRateizzazionePP(idFascicolo);
		setRequestAttribute("listaRichiestaBollettini", listaRichiestaBollettini);
		if (!listaRichiestaBollettini.isEmpty()) {
			Vector<RateizzazionePPModel> rateizzazioni = listaRichiestaBollettini.firstElement()
					.getListaRateizzazioniPP();
			List<String> listaRateizzazioni = new ArrayList<>(listaRichiestaBollettini.size());
			Iterator<RateizzazionePPModel> iter = rateizzazioni.iterator();
			String testo = "";
			int cont = 0;
			while (iter.hasNext()) {
				RateizzazionePPModel rata = iter.next();
				if (cont == 0)
					testo = "Importo da Pagare: " + rata.getImportoDaPagare() + "&#8364; ";
				if ("R".equals(rata.getTipoRateizzazione())) { // RATE
					if (cont == 0)
						testo += "in:<br>";
					else
						testo += "<br>";
					testo += "" + rata.getNumeroRate() + " rate da " + "" + rata.getImportoRata();
					if (!Utils.isNullObj(rata.getScadenzaGiorni()))
						testo += ", termine di pagamento fissato entro " + rata.getScadenzaGiorni().toString()
								+ " giorni dalla Notifica dell'Avviso di Pagamento";
				} else { // UNICA SOLUZIONE
					testo += " in un'unica soluzione";
					if (!Utils.isNullObj(rata.getScadenzaGiorni()))
						testo += ", termine di pagamento fissato entro " + rata.getScadenzaGiorni().toString()
								+ " giorni dalla Notifica dell'Avviso di Pagamento";
				}
				cont++;
			}
			listaRateizzazioni.add(testo);
			setRequestAttribute("listaRateizzazioni", listaRateizzazioni);
		}

		// info per il log
		siesLogger.debug(getClass().getName() + ".processRequest: fine");

		return ICostantiSanzioneSostitutiva.PG_ELENCO_RICHIESTA_BOLLETTINI;
	}

}