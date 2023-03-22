package siap.siep.pagoPA.action;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.F3BProperties;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;
import it.giustizia.www.serviziTelematici.serviziGenerici.RisultatoRicerca;
import it.giustizia.www.serviziTelematici.serviziGenerici.ServiziConsultazionePagamentiTelematici;
import it.giustizia.www.serviziTelematici.serviziGenerici.ServiziConsultazionePagamentiTelematiciBeanServiceLocator;
import it.giustizia.www.serviziTelematici.serviziGenerici.StatoRichiestaPagamento;
import siap.sico.web.ActionSiap;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.pagoPA.controller.IBollettinoPagopa;
import siap.siep.pagoPA.model.BollettinoPagopaModel;
import siap.siep.util.SIEPLookupRemote;
import siap.sius.fascicolo.model.FascicoloGPModel;

/**
 * Classe che permette di invocare ws verifica stato bollettini pagoPA
 *
 * @author sgioggi
 * @since MEV_2023-13
 * @version 1.0
 */
public class ActInvocaWSVerificaStatoBollettini extends ActionSiap implements ICostantiPagoPA {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public String processRequest() throws Exception {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.info(getClass().getName() + ".processRequest: inizio");

		FascicoloSiepModel fsm = null;
		FascicoloGPModel fgpm = null;

		String tipoFascicolo = "";
		BigDecimal idFascicolo = null;

		if (!isRequestParameterNullObj("tipoFascicolo")) {
			tipoFascicolo = getRequestStringParameter("tipoFascicolo");
			if (tipoFascicolo.equals("SIEP")) {
				if (!isSessionAttributeNullObj("fascicolo")) {
					fsm = ((FascicoloSiepModel) getSessionAttribute("fascicolo"));
				}
			} else {
				if (!isSessionAttributeNullObj("fascicoloSiusGP")) {
					fgpm = ((FascicoloGPModel) getSessionAttribute("fascicoloSiusGP"));
				}
			}
		}

		if (fsm != null)
			idFascicolo = fsm.getIdFascicoloSiep();
		else if (fgpm != null)
			idFascicolo = fgpm.getFascicoloSiusModel().getIdFascicoloSius();
		setRequestAttribute("idFascicolo", idFascicolo.toString());

		String listaIdBollettini = getRequestStringParameter("listaIdBollettini");
		String[] lista = listaIdBollettini.split("#");
		ArrayList<String> al = new ArrayList<>(Arrays.asList(lista));

		// recupero il/i bollettino/i
		IBollettinoPagopa ibp = SIEPLookupRemote.getBollettinoPagopaRemote();
		Vector<BollettinoPagopaModel> bpms = ibp
				.ExRicercaBollettinoPagopaByFasSieIdFascicoloSiep(idFascicolo);
		Iterator<BollettinoPagopaModel> iter = bpms.iterator();

		// inizio chiamata al servizio PST - EndpointAddressPagoPA_ServiziInvioPagamentiTelematici
		String endpointAddressSCPT = F3BProperties.getProperty("EAPPA_SCPT");
		ServiziConsultazionePagamentiTelematiciBeanServiceLocator scptbsl = new ServiziConsultazionePagamentiTelematiciBeanServiceLocator();
		scptbsl.setServiziConsultazionePagamentiTelematiciSOAPPortEndpointAddress(endpointAddressSCPT);
		ServiziConsultazionePagamentiTelematici scpt = scptbsl
				.getServiziConsultazionePagamentiTelematiciSOAPPort();
		// info per il log
		siesLogger.debug("Chiamo elencoPagamenti(...) su " + endpointAddressSCPT);
		RisultatoRicerca rr = null;
		while (iter.hasNext()) {
			BollettinoPagopaModel bpm = iter.next();
			if (al.contains(bpm.getIdBollettinoPagopa().toString())) {
				try {
					// java.lang.String codiceCRS, java.lang.String tipologia, java.lang.String codiceFiscale,
					// java.lang.String codiceDistretto, java.lang.String causale, java.lang.String stato,
					// java.util.Calendar dataRichiestaDa, java.util.Calendar dataRichiestaA, int
					// dimensionePagina, int numeroPagina
					rr = scpt.elencoPagamenti("3" + bpm.getIuv(), "PENPE", bpm.getCodiceFiscale(),
							bpm.getCodiceDistretto(), null, null, null, null, 0, 0);
				} catch (Exception e) {
					e.printStackTrace();
					siesLogger.error(e.getMessage());
					throw e;
				}
				// info per il log
				if (rr != null && rr.getCount() > 0) {
					Object[] srps = rr.getItems();
					for (int i = 0; i < srps.length; i++) {
						StatoRichiestaPagamento srp = (StatoRichiestaPagamento) srps[i];
						String dataRichiesta = (srp.getDataRichiesta() != null)
								? DateUtils.getDateToString(srp.getDataRichiesta().getTime(), "dd/MM/yyyy")
								: "";
						String dataRicevuta = (srp.getDataRicevuta() != null)
								? DateUtils.getDateToString(srp.getDataRicevuta().getTime(), "dd/MM/yyyy")
								: "";
						siesLogger
								.debug("Risultato Ricerca: Stato = " + srp.getStato() + "; Data Richiesta = "
										+ dataRichiesta + "; Data Ricevuta = " + dataRicevuta);
					}
				}
			}
		}

		// pagina di ritorno
		RedirectTo rt = new RedirectTo();
		rt.setPage(IWebConstants.PG_MAIN);
		setRequestAttribute(IWebConstants.MESSAGE_TEXT,
				"La Richiesta Verifica Stato Pagamento Bollettini è andata a buon fine!");
		rt.setAction("siap.siep.sanzionesostitutiva.action.ActVerificaStatoElencoBollettini&IdEvento="
				+ getRequestBigDecimalParameter("IdEvento"));
		setRequestAttribute(IWebConstants.GOTO_PAGE, "" + rt);
		// return rt.toString();
		return IWebConstants.PG_MESSAGE;
	}

}