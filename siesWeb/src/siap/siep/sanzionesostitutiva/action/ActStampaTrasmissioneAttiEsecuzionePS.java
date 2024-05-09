package siap.siep.sanzionesostitutiva.action;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoModel;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.template.controller.ITemplate;
import siap.sico.template.model.TemplateModel;
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.utente.model.UtenteModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.posizione.controller.IPosizioneGiuridica;
import siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel;
import siap.siep.posizione.model.PosizioneGiuridicaModel;
import siap.siep.sanzionesostitutiva.controller.ISanzioneSostitutiva;
import siap.siep.util.SIEPLookupRemote;

/**
 * Classe Action per la stampa della trasmissione atti per l'esecuzione pena sostitutiva
 *
 * @author sgioggi
 * @since MEV_2023-33
 * @version 1.0
 */
public class ActStampaTrasmissioneAttiEsecuzionePS extends ActionSiap {

	// info per il log
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public String processRequest() throws F3BException {

		// info per il log
		siesLogger.debug(getClass().getName() + ".processRequest: inizio");

		FascicoloSiepModel fsm = (FascicoloSiepModel) getSessionAttribute("fascicolo");
		UtenteModel utm = getUtenteConnesso();
		UfficioModel ufm = getUfficioUtenteConnesso();
		BigDecimal idEvento = getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO);

		// ricerca evento
		IEvento ie = SICOLookupRemote.getEventoRemote();
		EventoModel em = ie.ExRicercaEventoByKey(idEvento);

		// evento
		EventoNotificaModel enm = new EventoNotificaModel();
		enm.getEvento().setIdEvento(idEvento);
		enm.getEvento().setFasSieIdFascicoloSiep(fsm.getIdFascicoloSiep());
		enm.getEvento().setDescrLuogoEmittente(ufm.getDescrComune());
		enm.getEvento().setDescrUfficioEmittente(ufm.getDescrTipoUfficio());
		enm.getEvento().setDataAggiornamento(DateUtils.getSysDate());
		enm.getEvento().setCodUfficioAggiornamento(ufm.getCodUfficio());
		enm.getEvento().setCodOperatoreAggiornamento(getCodUtenteConnesso());
		enm.getEvento().setFlagDocumentoRegistrato("N");

		// cerco il documento
		ITemplate it = SICOLookupRemote.getTemplateRemote();
		TemplateModel tm = new TemplateModel();
		PosizioneGiuridicaLuogoDetenzioneAltraCausaModel pgldacm = new PosizioneGiuridicaLuogoDetenzioneAltraCausaModel();
		IPosizioneGiuridica ipg = SIEPLookupRemote.getPosizioneGiuridicaRemote();
		pgldacm = ipg.ExRicercaPosizioneGiuridicaLuogoDetenzioneAltraCausaCorrentiByIdFascicolo(
				fsm.getIdFascicoloSiep());
		PosizioneGiuridicaModel pgm = pgldacm.getPosizioneGiuridica();
		String codPosizioneGiuridica = pgm.getCodPosizioneGiuridica().trim();
		// List<String> libero = Arrays.asList("07", "10", "16", "17", "46", "47", "89", "90");
		// UDS + AVV + AD
		List<String> ciadpqc = Arrays.asList("02", "23", "70", "71", "72"); // UDS + AVV + AD + FP
		List<String> ccpqcrdad = Arrays.asList("01", "22", "55", "73"); // UDS + AVV + AD + ID
		List<String> ccacrdad = Arrays.asList("78", "79", "80", "81"); // UDS + AVV + AD
		List<String> ccacird = Arrays.asList("75", "76", "77"); // UDS + AVV + AD
		String flagTemplate = "0"; // libero
		if (ciadpqc.contains(codPosizioneGiuridica))
			flagTemplate = "1";
		else if (ccpqcrdad.contains(codPosizioneGiuridica))
			flagTemplate = "2";
		else if (ccacrdad.contains(codPosizioneGiuridica))
			flagTemplate = "3";
		else if (ccacird.contains(codPosizioneGiuridica))
			flagTemplate = "4";

		tm = it.ExRicercaTemplateByTipEveTipoProvCodMotivoFlagTemplate(em.getCodTipoEvento(),
				em.getCodTipoProvvedimento(), em.getCodMotivo(), flagTemplate);
		enm.setNomeTemplate(tm.getIdTemplate());

		// produce la stampa
		ISanzioneSostitutiva iss = SIEPLookupRemote.getSanzioneSostitutivaRemote();
		ByteArrayOutputStream baos = iss.exStampaSS(enm, utm);
		// setta la risposta nella request
		setRequestAttribute("report", baos);

		// info per il log
		siesLogger.debug(getClass().getName() + ".processRequest: fine");

		// valore di ritorno
		return IWebConstants.PG_DOWNLOAD;
	}

}