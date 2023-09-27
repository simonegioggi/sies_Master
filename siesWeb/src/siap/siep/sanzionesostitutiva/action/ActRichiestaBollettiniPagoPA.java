package siap.siep.sanzionesostitutiva.action;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.StringUtils;
import f3b.util.Utils;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;
import siap.sico.soggetto.controller.ISoggetto;
import siap.sico.soggetto.model.SoggettoModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.pagoPA.controller.IBollettinoPagopa;
import siap.siep.pagoPA.model.BollettinoPagopaModel;
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

	private static Logger siesLogger = Logger.getLogger(LogF3B.WS_PAGO_PA_LOG);

	public String processRequest() throws Exception {

		// info per il log
		siesLogger.debug(getClass().getName() + ".processRequest: inizio");
		setLinkRitorno();

		FascicoloSiepModel fsm = (FascicoloSiepModel) getSessionAttribute("fascicolo");
		BigDecimal idFascicolo = fsm.getIdFascicoloSiep();

		// controllo obbligatorietà CF
		ISoggetto is = SICOLookupRemote.getSoggettoRemote();
		SoggettoModel sm = is.ExRicercaSoggettoByKey(fsm.getSoggetto().getIdSoggetto());
		if (!Utils.isPresent(sm.getCodFiscale())) {
			siesLogger
					.info("Soggetto Privo di Codice Fiscale: reindirizzo alla pagina di modifica soggetto!");
			// pagina di ritorno
			RedirectTo rt = new RedirectTo();
			rt.setPage(IWebConstants.PG_MAIN);
			setRequestAttribute(IWebConstants.MESSAGE_TEXT, "Attenzione! Impossibile generare la Richiesta "
					+ "Bollettini poiché il condannato risulta privo di Codice Fiscale.");
			rt.setAction(
					"siap.sico.soggetto.action.ActLoadModificaSoggetto&IdSoggetto=" + sm.getIdSoggetto());
			setRequestAttribute(IWebConstants.GOTO_PAGE, "" + rt);
			// return rt.toString();
			return IWebConstants.PG_MESSAGE;
		}

		IRateizzazionePP irpp = SIEPLookupRemote.getRateizzazionePPRemote();
		Vector<EventoRateizzazionePPModel> listaRichiestaBollettini = irpp
				.exRicercaEventoRateizzazionePP(idFascicolo, "ALL");
		// imposto nella request
		setRequestAttribute("listaRichiestaBollettini", listaRichiestaBollettini);

		boolean isUnico = false;
		List<String> testi = new ArrayList<String>();

		if (!listaRichiestaBollettini.isEmpty()) {
			Iterator<EventoRateizzazionePPModel> iterERPPM = listaRichiestaBollettini.iterator();
			while (iterERPPM.hasNext()) {
				EventoRateizzazionePPModel erppm = iterERPPM.next();
				Vector<RateizzazionePPModel> rateizzazioni = erppm.getListaRateizzazioniPP();
				Iterator<RateizzazionePPModel> iterRPP = rateizzazioni.iterator();
				String testo = "";
				int cont = 0;
				while (iterRPP.hasNext()) {
					RateizzazionePPModel rata = iterRPP.next();
					if (cont == 0)
						testo = "Importo da Pagare: " + StringUtils.toEuroFormat(rata.getImportoDaPagare())
								+ " ";
					if ("R".equals(rata.getTipoRateizzazione())) { // RATE
						if (cont == 0) {
							testo += "in:";
							testo += "<ul>";
						}
						testo += "<li>" + "" + rata.getNumeroRate() + " rate da " + ""
								+ StringUtils.toEuroFormat(rata.getImportoRata());
						if (!Utils.isNullObj(rata.getScadenzaGiorni()) && cont == 0)
							// MEV_2023-33: cambiata frase
							testo += ", termine di pagamento della prima rata fissato entro "
									+ rata.getScadenzaGiorni().toString()
									+ " giorni dalla Notifica dell'Avviso di Pagamento" + "</li>";
						else
							testo += "</li>";
						if (cont == rateizzazioni.size() - 1)
							testo += "</ul>";
					} else { // UNICA SOLUZIONE
						testo += " in un'unica soluzione";
						if (!Utils.isNullObj(rata.getScadenzaGiorni()))
							testo += ", termine di pagamento fissato entro "
									+ rata.getScadenzaGiorni().toString()
									+ " giorni dalla Notifica dell'Avviso di Pagamento";
						isUnico = true;
					}
					cont++;
				}
				// aggiungo alla lista
				testi.add(testo);
			}
		}

		// imposto nella request
		setRequestAttribute("modalitaPagamento", testi);

		// MEV_2023-33: aggiunte impostazioni di attributo
		boolean isSoloPrimaRata = false;
		boolean areRateGiaGenerate = false;
		IBollettinoPagopa ibp = SIEPLookupRemote.getBollettinoPagopaRemote();
		// Ricerca lo stato dei pagamenti per id fascicolo
		Vector<BollettinoPagopaModel> elencoStatoPagamenti = ibp
				.ExRicercaBollettinoPagopaByFasSieIdFascicoloSiep(idFascicolo);
		if (!elencoStatoPagamenti.isEmpty() && listaRichiestaBollettini.isEmpty()) {
			isUnico = elencoStatoPagamenti.size() == 1
					&& "U".equals(elencoStatoPagamenti.get(0).getTipoRateizzazione());
		}
		setRequestAttribute("isRateale", !isUnico);
		if (!elencoStatoPagamenti.isEmpty() && !isUnico && elencoStatoPagamenti.size() > 1) {
			Iterator<BollettinoPagopaModel> itx = elencoStatoPagamenti.iterator();
			int contaIUV = 0;
			while (itx.hasNext()) {
				BollettinoPagopaModel bpm = itx.next();
				if (Utils.isPresent(bpm.getIuv()))
					contaIUV++;
			}
			if (contaIUV == 1)
				isSoloPrimaRata = true;
			BollettinoPagopaModel primaRata = elencoStatoPagamenti.get(0);
			BollettinoPagopaModel rataSuccessiva = elencoStatoPagamenti.get(1);
			if (!Utils.isNullObj(primaRata.getDataGenerazioneBollettino())
					&& !Utils.isNullObj(rataSuccessiva.getDataGenerazioneBollettino())
					&& !DateUtils.isEqualsLocalDateTime(primaRata.getDataGenerazioneBollettino(),
							rataSuccessiva.getDataGenerazioneBollettino())) {
				isSoloPrimaRata = true;
				areRateGiaGenerate = true;
			} else if (contaIUV != 1)
				isSoloPrimaRata = false;
		}
		// imposto l'attributo nella request
		setRequestAttribute("isSoloPrimaRata", isSoloPrimaRata);
		setRequestAttribute("areRateGiaGenerate", areRateGiaGenerate);

		// info per il log
		siesLogger.debug(getClass().getName() + ".processRequest: fine");

		return PG_ELENCO_RICHIESTA_BOLLETTINI;
	}

}