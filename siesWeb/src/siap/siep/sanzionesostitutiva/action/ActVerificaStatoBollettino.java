package siap.siep.sanzionesostitutiva.action;

import java.math.BigDecimal;
import java.util.Iterator;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.StringUtils;
import f3b.util.Utils;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;
import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.model.EventoModel;
import siap.sico.web.ActionSiap;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.rateizzazionepp.controller.IRateizzazionePP;
import siap.siep.rateizzazionepp.model.EventoRateizzazionePPModel;
import siap.siep.rateizzazionepp.model.RateizzazionePPModel;
import siap.siep.util.SIEPLookupRemote;

/**
 * Classe per la verifica stato bollettino PagoPA
 *
 * @author sgioggi
 * @since MEV_2023-13
 * @version 1.0
 */
public class ActVerificaStatoBollettino extends ActionSiap implements ICostantiSanzioneSostitutiva {

	private static Logger siesLogger = Logger.getLogger(LogF3B.WS_PAGO_PA_LOG);

	public String processRequest() throws Exception {

		// info per il log
		siesLogger.debug(getClass().getName() + ".processRequest: inizio");
		setLinkRitorno();

		BigDecimal idFascicolo = null;
		if (!isRequestParameterNullEmptyObj(ICostantiEvento.CAMPO_FAS_SIE_ID_FASCICOLO_SIEP))
			idFascicolo = getRequestBigDecimalParameter(ICostantiEvento.CAMPO_FAS_SIE_ID_FASCICOLO_SIEP);
		else {
			FascicoloSiepModel fsm = (FascicoloSiepModel) getSessionAttribute("fascicolo");
			idFascicolo = fsm.getIdFascicoloSiep();
		}

		IRateizzazionePP irpp = SIEPLookupRemote.getRateizzazionePPRemote();
		Vector<EventoRateizzazionePPModel> listaRichiestaBollettini = irpp
				.exRicercaEventoRateizzazionePP(idFascicolo);
		setRequestAttribute("listaRichiestaBollettini", listaRichiestaBollettini);
		if (!listaRichiestaBollettini.isEmpty()) {
			EventoModel em = listaRichiestaBollettini.firstElement().getEvento();
			if (Utils.isNullObj(em.getDataRicezioneAtti()) && Utils.isNullObj(em.getDataTrasmissioneAtti())) {
				// pagina di ritorno
				RedirectTo rt = new RedirectTo();
				rt.setPage(IWebConstants.PG_MAIN);
				setRequestAttribute(IWebConstants.MESSAGE_TEXT,
						"Attenzione! Non sono stati emessi Bollettini per questo fascicolo.");
				rt.setAction("siap.siep.sanzionesostitutiva.action.ActGrigliaBollettiniPagoPA");
				setRequestAttribute(IWebConstants.GOTO_PAGE, "" + rt);
				// return rt.toString();
				return IWebConstants.PG_MESSAGE;
			}
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
					testo += "<li>" + "" + rata.getNumeroRate() + " rate da " + ""
							+ StringUtils.toEuroFormat(rata.getImportoRata());
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

		return PG_VERIFICA_STATO_BOLLETTINO;
	}

}