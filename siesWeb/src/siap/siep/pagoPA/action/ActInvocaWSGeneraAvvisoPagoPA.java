package siap.siep.pagoPA.action;

import java.io.ByteArrayInputStream;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Iterator;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.F3BProperties;
import f3b.util.Utils;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;
import it.giustizia.www.serviziTelematici.serviziGenerici.AnagraficaSoggetto;
import it.giustizia.www.serviziTelematici.serviziGenerici.DatiVersamento;
import it.giustizia.www.serviziTelematici.serviziGenerici.EsitoGeneraAvviso;
import it.giustizia.www.serviziTelematici.serviziGenerici.RichiestaPagamentoTelematico;
import it.giustizia.www.serviziTelematici.serviziGenerici.ServiziInvioPagamentiTelematici;
import it.giustizia.www.serviziTelematici.serviziGenerici.ServiziInvioPagamentiTelematiciBeanServiceLocator;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoModel;
import siap.sico.security.action.ICostantiSecurity;
import siap.sico.soggetto.model.SoggettoModel;
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.utente.model.UtenteModel;
import siap.sico.util.GeneraAvvisoPagoPAUtil;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.pagoPA.controller.IBollettinoPagopa;
import siap.siep.pagoPA.model.BollettinoPagopaModel;
import siap.siep.util.SIEPLookupRemote;
import siap.sius.fascicolo.model.FascicoloGPModel;

/**
 * Classe che permette di invocare ws genera avviso pagoPA
 *
 * @author sgioggi
 * @since MEV_2023-13
 * @version 1.0
 */
public class ActInvocaWSGeneraAvvisoPagoPA extends ActionSiap implements ICostantiPagoPA {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public String processRequest() throws Exception {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.info(getClass().getName() + ".processRequest: inizio");

		FascicoloSiepModel fsm = null;
		FascicoloGPModel fgpm = null;

		UtenteModel utm = new UtenteModel(
				(UtenteModel) getSessionAttribute(ICostantiSecurity.SESSION_UTENTE_CONNESSO));

		UfficioModel ufm = utm.getUfficioUtente();

		String tipoFascicolo = "";
		SoggettoModel sm = null;
		BigDecimal annoProc = null;
		BigDecimal numeroProc = null;
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

		if (fsm != null) {
			sm = fsm.getSoggetto();
			annoProc = fsm.getChiaveAnno();
			numeroProc = fsm.getChiaveProgr();
			idFascicolo = fsm.getIdFascicoloSiep();
		} else if (fgpm != null) {
			sm = fgpm.getFascicoloSiusModel().getSoggetto();
			annoProc = fgpm.getFascicoloSiusModel().getChiaveAnno();
			numeroProc = fgpm.getFascicoloSiusModel().getChiaveProgr();
			idFascicolo = fgpm.getFascicoloSiusModel().getIdFascicoloSius();
		}
		// info per il log
		siesLogger
				.debug("ID FASCICOLO: " + idFascicolo + "; con anno/numero: " + annoProc + "/" + numeroProc);

		// String pathkeystore = System.getProperty("jboss.home.dir") + System.getProperty("file.separator")
		// + "standalone" + System.getProperty("file.separator") + "configuration"
		// + System.getProperty("file.separator") + "serversies.jks";
		// siesLogger.debug("PERCORSO DEL keystore: " + pathkeystore);
		// System.setProperty("javax.net.ssl.keyStore", pathkeystore);
		// System.setProperty("javax.net.ssl.keyStorePassword", "siescoll2014");
		// System.setProperty("javax.net.debug", "ssl");
		// inizio chiamata al servizio PST - EndpointAddressPagoPA_ServiziInvioPagamentiTelematici
		String endpointAddress = F3BProperties.getProperty("EAPPA_SIPT");
		ServiziInvioPagamentiTelematiciBeanServiceLocator service = new ServiziInvioPagamentiTelematiciBeanServiceLocator();
		service.setServiziInvioPagamentiTelematiciSOAPPortEndpointAddress(endpointAddress);
		ServiziInvioPagamentiTelematici port = service.getServiziInvioPagamentiTelematiciSOAPPort();
		// info per il log
		siesLogger.debug("Chiamo generaAvviso(RichiestaPagamentoTelematico) su " + endpointAddress);
		// INVOCO WS: impostazioni per il certificato
		// CONFIG = /var/SIES/CONFIG (pathProp)
		// String pathProp = System.getProperty("path.properties");
		// String truststore = "/certs/sies.jks";
		// String keystore = "/certs/serversies.jks";
		// String pathtruststore = pathProp + truststore;
		// siesLogger.debug("PERCORSO DEL truststore: " + pathJKS);
		// System.setProperty("javax.net.ssl.trustStore", pathJKS);
		// System.setProperty("javax.net.ssl.trustStorePassword", "testsies");
		// String pathkeystore = pathProp + keystore;

		// recupero il/i bollettino/i
		IBollettinoPagopa ibp = SIEPLookupRemote.getBollettinoPagopaRemote();
		Vector<BollettinoPagopaModel> bpms = ibp
				.ExRicercaBollettinoPagopaByFasSieIdFascicoloSiep(idFascicolo);
		// DATI PER RICHIESTA PAGAMENTO
		RichiestaPagamentoTelematico rpt = GeneraAvvisoPagoPAUtil.caricaDatiRichiestaPagamentoTelematico(ufm);
		// SOGGETTO PAGATORE (è il soggetto debitore nei confronti della PA)
		AnagraficaSoggetto asp = GeneraAvvisoPagoPAUtil.caricaDatiAnagraficaSoggetto(sm);
		rpt.setSoggettoPagatore(asp);
		// DATI VERSAMENTO
		Iterator<BollettinoPagopaModel> iter = bpms.iterator();
		while (iter.hasNext()) {
			BollettinoPagopaModel bpm = iter.next();
			DatiVersamento dv = GeneraAvvisoPagoPAUtil.caricaDatiVersamento(sm, bpm);
			rpt.setDatiVersamento(dv);
			Calendar c = Calendar.getInstance();
			c.setTime(bpm.getDataScadenzaRich());
			rpt.setDataScadenza(c);

			EsitoGeneraAvviso ega = null;
			try {
				ega = port.generaAvviso(rpt);
			} catch (Exception e) {
				e.printStackTrace();
				siesLogger.error(e.getMessage());
				throw e;
			}
			// info per il log
			siesLogger.debug("EsitoGeneraAvviso: " + ega.getNumeroAvviso() + " # " + ega.getBollettino());

			// Caricamento BOLLETTINO (Campo BLOB) nel Model
			ByteArrayInputStream bais = new ByteArrayInputStream(ega.getBollettino());
			// Salvare il bollettino sulla tabella BOLLETTINO_PAGOPA
			bpm.setCodUfficioAggiornamento(utm.getUfficioUtente().getCodUfficio());
			bpm.setCodOperatoreAggiornamento(utm.getUserId());
			bpm.setDataAggiornamento(DateUtils.getSysDate());
			bpm.setDocBollBlob(bais);
			bpm.setCodiceFiscale(sm.getCodFiscale());
			bpm.setCodiceDistretto(rpt.getCodiceDistretto());
			String iuv = Utils.isPresent(ega.getNumeroAvviso()) ? ega.getNumeroAvviso().substring(1) : "";
			bpm.setIuv(iuv);
			ibp.ExModificaBollettinoPagopa(bpm);
		}
		setRequestAttribute("idFascicolo", idFascicolo.toString());

		// aggiorna l'evento di ingiunzione
		BigDecimal idEvento = null;
		EventoModel em = null;
		if (!isRequestParameterNullObj("idEvento")) {
			idEvento = getRequestBigDecimalParameter("idEvento");
			IEvento ie = SICOLookupRemote.getEventoRemote();
			em = ie.ExRicercaEventoByKey(idEvento);
			// info per il log
			siesLogger.debug(em.getIdEvento() + " " + em.getDescrProvvedimento() + " " + em.getDescrEsito()
					+ " " + em.getDescrMotivo() + " " + em.getDescrTipoEvento() + " "
					+ em.getDescrTipoProvvedimento());
			em.setCodUfficioAggiornamento(utm.getUfficioUtente().getCodUfficio());
			em.setCodOperatoreAggiornamento(utm.getUserId());
			em.setDataAggiornamento(DateUtils.getSysDate());
			em.setDataRicezioneAtti(DateUtils.getSysDate());
			em.setDataTrasmissioneAtti(DateUtils.getSysDate());
			ie.ExModificaEvento(em);
			siesLogger.debug("EVENTO MODIFICATO con data ricezione e trasmissione atti = "
					+ DateUtils.getSysDateAsDate("dd/MM/yyyy"));
		}

		// // inizio chiamata al servizio PST - EndpointAddressPagoPA_ServiziInvioPagamentiTelematici
		// String endpointAddressSCPT = F3BProperties.getProperty("EAPPA_SCPT");
		// ServiziConsultazionePagamentiTelematiciBeanServiceLocator scptbsl = new
		// ServiziConsultazionePagamentiTelematiciBeanServiceLocator();
		// scptbsl.setServiziConsultazionePagamentiTelematiciSOAPPortEndpointAddress(endpointAddressSCPT);
		// ServiziConsultazionePagamentiTelematici scpt = scptbsl
		// .getServiziConsultazionePagamentiTelematiciSOAPPort();
		// // info per il log
		// siesLogger.debug("Chiamo elencoPagamenti(...) su " + endpointAddressSCPT);
		// RisultatoRicerca rr = null;
		// try {
		// // java.lang.String codiceCRS, java.lang.String tipologia, java.lang.String codiceFiscale,
		// // java.lang.String codiceDistretto, java.lang.String causale, java.lang.String stato,
		// // java.util.Calendar dataRichiestaDa, java.util.Calendar dataRichiestaA, int
		// // dimensionePagina,
		// // int numeroPagina
		// Calendar c = Calendar.getInstance();
		// c.setTime(DateUtils.getSysDate());
		// rr = scpt.elencoPagamenti(/* ega.getNumeroAvviso() */null, "PENPE",
		// /* asp.getCodiceIdentificativoUnivoco() */"SGMSMV72D23H501S", "GLTO",
		// "Sanzione pecuniaria", null, null, null, 0, 0);
		// } catch (Exception e) {
		// e.printStackTrace();
		// siesLogger.error(e.getMessage());
		// throw e;
		// }
		// // info per il log
		// siesLogger.debug("Risultato Ricerca: Count = " + rr.getCount());
		// siesLogger.debug("codiceCRS = " + /* ega.getNumeroAvviso() */"330097149392676039");
		// siesLogger.debug("tipologia = PENPE");
		// siesLogger
		// .debug("codiceFiscale = " + /* asp.getCodiceIdentificativoUnivoco() */"SGMSMV72D23H501S");
		// siesLogger.debug("codiceDistretto = GLTO");
		// siesLogger.debug("causale = Sanzione pecuniaria");
		// siesLogger.debug("stato = #NULL=tutti#");
		// if (rr != null && rr.getCount() > 0) {
		// Object[] srps = rr.getItems();
		// for (int i = 0; i < srps.length; i++) {
		// StatoRichiestaPagamento srp = (StatoRichiestaPagamento) srps[i];
		// String dataRichiesta = (srp.getDataRichiesta() != null)
		// ? DateUtils.getDateToString(srp.getDataRichiesta().getTime(), "dd/MM/yyyy")
		// : null;
		// siesLogger.debug("Risultato Ricerca: Count = " + (i + 1) + "; Denominazione Pagatore = "
		// + srp.getDenominazionePagatore() + "; Descrizione Tipologia = "
		// + srp.getDescrizioneTipologia() + "; Importo = " + srp.getImporto()
		// + "; Numero Avviso = " + srp.getNumeroAvviso() + "; Pagatore = "
		// + srp.getPagatore() + "; Stato = " + srp.getStato() + "; Data Richiesta = "
		// + dataRichiesta);
		// }
		// }

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.info(getClass().getName() + ".processRequest: fine");

		// pagina di ritorno
		RedirectTo rt = new RedirectTo();
		rt.setPage(IWebConstants.PG_MAIN);
		setRequestAttribute(IWebConstants.MESSAGE_TEXT,
				"La Generazione dell'Avviso PagoPA è andata a buon fine!");
		rt.setAction("siap.siep.sanzionesostitutiva.action.ActRichiestaBollettiniPagoPA");
		setRequestAttribute(IWebConstants.GOTO_PAGE, "" + rt);
		// return rt.toString();
		return IWebConstants.PG_MESSAGE;
	}

}