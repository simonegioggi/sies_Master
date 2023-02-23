package siap.sico.webservice.action;

import java.io.ByteArrayInputStream;
import java.math.BigDecimal;
import java.security.MessageDigest;
import java.util.Calendar;

import org.apache.log4j.Logger;
import org.apache.xerces.impl.dv.util.Base64;

import f3b.log.LogF3B;
import f3b.security.SecurityException;
import f3b.util.DateUtils;
import f3b.util.F3BProperties;
import f3b.util.Utils;
import f3b.web.IWebConstants;
import it.giustizia.www.serviziTelematici.serviziGenerici.AnagraficaSoggetto;
import it.giustizia.www.serviziTelematici.serviziGenerici.DatiMarcaBolloDigitale;
import it.giustizia.www.serviziTelematici.serviziGenerici.DatiSingoloVersamento;
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
import siap.sico.util.SICOLookupRemote;
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
public class ActInvocaWSGeneraAvvisoPagoPA extends ActWsBase implements IWebConstants {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public String processRequest() throws Exception {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.info("######## ActInvocaWSGeneraAvvisoPagoPA ##########");

		FascicoloSiepModel fsm = null;
		FascicoloGPModel fgpm = null;

		BigDecimal idEvento = null;
		EventoModel em = null;

		if (!isRequestParameterNullObj("idEvento")) {
			idEvento = getRequestBigDecimalParameter("idEvento");
			IEvento ie = SICOLookupRemote.getEventoRemote();
			em = ie.ExRicercaEventoByKey(idEvento);
		}
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

		siesLogger
				.debug("ID FASCICOLO: " + idFascicolo + "; con anno/numero: " + annoProc + "/" + numeroProc);

		// INVOCO WS:
		// inizio chiamata al servizio REGINDE
		String endpointAddress = F3BProperties.getProperty("EndpointAddressPagoPA");
		ServiziInvioPagamentiTelematiciBeanServiceLocator service = new ServiziInvioPagamentiTelematiciBeanServiceLocator();
		service.setServiziInvioPagamentiTelematiciSOAPPortEndpointAddress(endpointAddress);
		ServiziInvioPagamentiTelematici port = service.getServiziInvioPagamentiTelematiciSOAPPort();
		siesLogger.debug("Chiamo generaAvviso(RichiestaPagamentoTelematico) su " + endpointAddress);

		// DATI PER RICHIESTA PAGAMENTO
		RichiestaPagamentoTelematico rpt = new RichiestaPagamentoTelematico();
		rpt.setAutenticazioneSoggetto("OTH");
		rpt.setCodiceDistretto(ufm.getCodDistretto());
		rpt.setCodiceUfficio(ufm.getCodUfficio());
		Calendar c = Calendar.getInstance();
		c.setTime(DateUtils.getSysDate());
		rpt.setDataScadenza(c); // OBBLIGATORIA, altrimenti 30 giorni in automatico
		// DATI VERSAMENTO
		DatiVersamento dv = new DatiVersamento();
		dv.setBicAddebito(null);
		DatiSingoloVersamento[] dsv = new DatiSingoloVersamento[1];
		dsv[0] = new DatiSingoloVersamento();
		String causale = "Pagamenti in favore Amministrazione";
		DatiMarcaBolloDigitale dmbd = new DatiMarcaBolloDigitale();
		MessageDigest md = null;
		String str = "ciccio";
		String strCriptata = null;
		try {
			md = MessageDigest.getInstance("SHA-256");
			strCriptata = new String(Base64.encode(md.digest(str.getBytes())));
		} catch (Exception ex) {
			throw new SecurityException("Errore durante il crypting del digest");
		}
		dmbd.setHashDocumento(strCriptata);
		dmbd.setProvinciaResidenza(sm.getCodProvinciaNascita());
		dmbd.setTipoBollo("01");
		dsv[0].setDatiMarcaBolloDigitale(dmbd);
		dsv[0].setDatiSpecificiRiscossione("PENPE"); // valore fisso
		BigDecimal importo = !Utils.isNullObj(dpcm.getPenaComplessivaSanzioneSostitutiva()
				.getSanzioneSostitutiva().getSanzionePecuniariaMulta())
						? dpcm.getPenaComplessivaSanzioneSostitutiva().getSanzioneSostitutiva()
								.getSanzionePecuniariaMulta()
						: dpcm.getPenaComplessivaSanzioneSostitutiva().getSanzioneSostitutiva()
								.getSanzionePecuniariaAmmenda();
		dsv[0].setImporto(importo);
		dsv[0].setCausale("/" + dsv[0].getImporto() + "/TXT/" + causale); // MAX 100 chars
		dv.setDatiSingoloVersamento(dsv);
		dv.setDatiSingoloVersamento(0, dsv[0]);
		dv.setIbanAddebito(null);
		dv.setImportoTotale(null);
		rpt.setDatiVersamento(dv);
		// SOGGETTO PAGATORE (è il destinatario dell'avviso)
		AnagraficaSoggetto asp = new AnagraficaSoggetto();
		asp.setCap(null);
		asp.setCivico(null);
		asp.setCodiceIdentificativoUnivoco(sm.getCodFiscale()); // C.F. or P.I.
		asp.setEmail(null);
		asp.setIndirizzo(null);
		asp.setLocalita(null);
		asp.setNaturaGiuridica("F"); // F or G
		asp.setNazione(sm.getCodStatoNascita());
		asp.setNominativo(sm.getNome() + " " + sm.getCognome()); // MAX 70 chars
		asp.setProvincia(sm.getCodProvinciaNascita());
		asp.setRegione(sm.getCodComuneNascita());
		rpt.setSoggettoPagatore(asp);
		// SOGGETTO VERSANTE (è il soggetto che paga)
		AnagraficaSoggetto asv = new AnagraficaSoggetto();
		asv.setCap(null);
		asv.setCivico(null);
		asv.setCodiceIdentificativoUnivoco(sm.getCodFiscale()); // C.F. or P.I.
		asv.setEmail(null);
		asv.setIndirizzo(null);
		asv.setLocalita(null);
		asv.setNaturaGiuridica("F");
		asv.setNazione(sm.getCodStatoNascita());
		asv.setNominativo(sm.getNome() + " " + sm.getCognome());
		asv.setProvincia(sm.getCodProvinciaNascita());
		asv.setRegione(sm.getCodComuneNascita());
		rpt.setSoggettoVersante(asv);

		EsitoGeneraAvviso ega = port.generaAvviso(rpt);
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

		// Salvare il bollettino sulla tabella FASCICOLO_SIEP
		if (fspcb.getFascicoloSiep() != null) {
			FascicoloSiepController fsc = new FascicoloSiepController();
			fspcb.setCodUfficioAggiornamento(utm.getUfficioUtente().getCodUfficio());
			fspcb.setCodOperatoreAggiornamento(utm.getUserId());
			fspcb.setDataAggiornamento(DateUtils.getSysDate());
			// Caricamento BOLLETTINO (Campo BLOB) nel Model
			ByteArrayInputStream bais = new ByteArrayInputStream(ega.getBollettino());
			fspcb.caricaCertPenaleBlobIn(bais);
			fsc.ExInsertCertificatoPenale(fspcb);
		} else { // Salvare il bollettino sulla tabella FASCICOLO_SIUS
			FascicoloSiusController fsc = new FascicoloSiusController();
			fsscb.setCodUfficioAggiornamento(utm.getUfficioUtente().getCodUfficio());
			fsscb.setCodOperatoreAggiornamento(utm.getUserId());
			fsscb.setDataAggiornamento(DateUtils.getSysDate());
			// Caricamento BOLLETTINO (Campo BLOB) nel Model
			ByteArrayInputStream bais = new ByteArrayInputStream(ega.getBollettino());
			fsscb.caricaCertPenaleBlobIn(bais);
			fsc.ExInsertCertificatoPenale(fsscb);
		}

		return PG_VISUALIZZA_AVVISO_PAGOPA;
	}

}