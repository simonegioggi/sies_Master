package siap.siep.pagoPA.action;

import java.io.ByteArrayInputStream;
import java.math.BigDecimal;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.F3BProperties;
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
import siap.siep.fascicolo.controller.FascicoloSiepController;
import siap.siep.fascicolo.model.FascicoloSiepCertBlobModel;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.penacomplessiva.controller.IPenaComplessiva;
import siap.siep.penacomplessiva.model.DettaglioPenaComplessivaModel;
import siap.siep.util.SIEPLookupRemote;
import siap.sius.fascicolo.controller.FascicoloSiusController;
import siap.sius.fascicolo.model.FascicoloGPModel;
import siap.sius.fascicolo.model.FascicoloSiusCertBlobModel;

/**
 * MEV_2023-13: aggiunta classe di invocazione ws genera avviso pagoPA
 *
 * @author sgioggi
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

		BigDecimal idEvento = null;
		EventoModel em = null;

		if (!isRequestParameterNullObj("idEvento")) {
			idEvento = getRequestBigDecimalParameter("idEvento");
			IEvento ie = SICOLookupRemote.getEventoRemote();
			em = ie.ExRicercaEventoByKey(idEvento);
		}
		// info per il log
		siesLogger.debug(em.getIdEvento() + " " + em.getDescrProvvedimento() + " " + em.getDescrEsito() + " "
				+ em.getDescrMotivo() + " " + em.getDescrTipoEvento() + " " + em.getDescrTipoProvvedimento());

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

		DettaglioPenaComplessivaModel dpcm = null;
		// PenaResiduaModel prm = null;
		IPenaComplessiva ipc = SIEPLookupRemote.getPenaComplessivaRemote();
		// IPenaResidua ipr = SIEPLookupRemote.getPenaResiduaRemote();
		if (fsm != null) {
			sm = fsm.getSoggetto();
			annoProc = fsm.getChiaveAnno();
			numeroProc = fsm.getChiaveProgr();
			idFascicolo = fsm.getIdFascicoloSiep();
			dpcm = ipc.ExRicercaPenaCompSanzioneSostContinuazioniByIdFascicolo(idFascicolo);
			// prm = ipr.ExRicercaPenaResiduaCorrenteByFascicoloSiep(idFascicolo);
		} else if (fgpm != null) {
			sm = fgpm.getFascicoloSiusModel().getSoggetto();
			annoProc = fgpm.getFascicoloSiusModel().getChiaveAnno();
			numeroProc = fgpm.getFascicoloSiusModel().getChiaveProgr();
			idFascicolo = fgpm.getFascicoloSiusModel().getIdFascicoloSius();
			dpcm = ipc.ExRicercaPenaCompSanzioneSostContinuazioniByIdFascicolo(idFascicolo);
			// prm = ipr.ExRicercaPenaResiduaCorrenteByFascicoloSiep(idFascicolo);
		}

		// info per il log
		siesLogger
				.debug("ID FASCICOLO: " + idFascicolo + "; con anno/numero: " + annoProc + "/" + numeroProc);

		// INVOCO WS:
		// inizio chiamata al servizio PST - EndpointAddressPagoPA_ServiziInvioPagamentiTelematici
		String endpointAddress = F3BProperties.getProperty("EAPPA_SIPT");
		ServiziInvioPagamentiTelematiciBeanServiceLocator service = new ServiziInvioPagamentiTelematiciBeanServiceLocator();
		service.setServiziInvioPagamentiTelematiciSOAPPortEndpointAddress(endpointAddress);
		ServiziInvioPagamentiTelematici port = service.getServiziInvioPagamentiTelematiciSOAPPort();
		// info per il log
		siesLogger.debug("Chiamo generaAvviso(RichiestaPagamentoTelematico) su " + endpointAddress);

		// DATI PER RICHIESTA PAGAMENTO
		RichiestaPagamentoTelematico rpt = GeneraAvvisoPagoPAUtil.caricaDatiRichiestaPagamentoTelematico(ufm);
		// DATI VERSAMENTO
		DatiVersamento dv = GeneraAvvisoPagoPAUtil.caricaDatiVersamento(sm, dpcm);
		rpt.setDatiVersamento(dv);
		// SOGGETTO PAGATORE (è il soggetto debitore nei confronti della PA)
		AnagraficaSoggetto asp = GeneraAvvisoPagoPAUtil.caricaDatiAnagraficaSoggetto(sm);
		rpt.setSoggettoPagatore(asp);
		// SOGGETTO VERSANTE (opzionale, è il soggetto che effettivamente paga, inserire solo se diverso dal
		// pagatore)
		// AnagraficaSoggetto asv = GeneraAvvisoPagoPAUtil.caricaDatiAnagraficaSoggetto(sm);
		// rpt.setSoggettoVersante(asv);

		EsitoGeneraAvviso ega = port.generaAvviso(rpt);
		// info per il log
		siesLogger.debug("EsitoGeneraAvviso: " + ega.getNumeroAvviso() + " # " + ega.getBollettino());

		// per recuperare l'avviso
		// byte[] avviso = port.downloadAvviso(ega.getNumeroAvviso());

		FascicoloSiusCertBlobModel fsscb = new FascicoloSiusCertBlobModel();
		FascicoloSiepCertBlobModel fspcb = new FascicoloSiepCertBlobModel();

		if (fsm != null) {
			fspcb.setFascicoloSiep(fsm);
		} else {
			fsscb.setFascicoloSius(fgpm.getFascicoloSiusModel());
		}

		// Caricamento BOLLETTINO (Campo BLOB) nel Model
		ByteArrayInputStream bais = new ByteArrayInputStream(ega.getBollettino());

		if (fspcb.getFascicoloSiep() != null) { // Salvare il bollettino sulla tabella FASCICOLO_SIEP
			FascicoloSiepController fsc = new FascicoloSiepController();
			fspcb.setCodUfficioAggiornamento(utm.getUfficioUtente().getCodUfficio());
			fspcb.setCodOperatoreAggiornamento(utm.getUserId());
			fspcb.setDataAggiornamento(DateUtils.getSysDate());
			fspcb.caricaCertPenaleBlobIn(bais);
			fsc.ExInsertCertificatoPenale(fspcb);
		} else { // Salvare il bollettino sulla tabella FASCICOLO_SIUS
			FascicoloSiusController fsc = new FascicoloSiusController();
			fsscb.setCodUfficioAggiornamento(utm.getUfficioUtente().getCodUfficio());
			fsscb.setCodOperatoreAggiornamento(utm.getUserId());
			fsscb.setDataAggiornamento(DateUtils.getSysDate());
			fsscb.caricaCertPenaleBlobIn(bais);
			fsc.ExInsertCertificatoPenale(fsscb);
		}

		// pagina di ritorno
		return PG_VISUALIZZA_AVVISO_PAGOPA;
	}

}