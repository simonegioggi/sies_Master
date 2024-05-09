package siap.siep.rateizzazionepp.action;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;

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

/**
 * Classe per la stampa della Rideterminazione Pena Pecuniaria
 *
 * @author sgioggi
 * @since MEV_2023-33
 * @version 1.0
 */
public class ActStampaRideterminazionePP extends ActionSiap {

	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public String processRequest() throws F3BException {

		// info per il log
		siesLogger.info(getClass().getName() + ".processRequest: inizio");

		FascicoloSiepModel fsm = (FascicoloSiepModel) getSessionAttribute("fascicolo");

		BigDecimal idEvento = getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO);

		UtenteModel utm = getUtenteConnesso();
		UfficioModel ufm = getUfficioUtenteConnesso();

		// ==========================================================================
		// Recupero l'evento per il quale produrre la Stampa (comunicazione)
		// ==========================================================================
		IEvento ie = SICOLookupRemote.getEventoRemote();
		EventoModel em = ie.ExRicercaEventoByKey(idEvento);

		// ==========================================================================
		// Recupero il template
		// ==========================================================================
		ITemplate it = SICOLookupRemote.getTemplateRemote();
		TemplateModel tm = new TemplateModel();
		tm = it.ExRicercaTemplateByTipEveTipoProvCodMotivoFlagTemplate(em.getCodTipoEvento(),
				em.getCodTipoProvvedimento(), em.getCodMotivo(), null);

		siesLogger.debug("tm = " + tm);

		// ==========================================================================
		// Genero il model Evento da passare alla funzione di stampa
		// ==========================================================================
		EventoNotificaModel enm = new EventoNotificaModel();
		enm.setNomeTemplate(tm.getIdTemplate());
		enm.getEvento().setIdEvento(idEvento);
		enm.getEvento().setFasSieIdFascicoloSiep(fsm.getIdFascicoloSiep());
		enm.getEvento().setDescrLuogoEmittente(ufm.getDescrComune());
		enm.getEvento().setDescrUfficioEmittente(ufm.getDescrTipoUfficio());
		enm.getEvento().setDataAggiornamento(DateUtils.getSysDate());
		enm.getEvento().setCodUfficioAggiornamento(ufm.getCodUfficio());
		enm.getEvento().setCodOperatoreAggiornamento(getCodUtenteConnesso());
		enm.getEvento().setFlagDocumentoRegistrato("N");

		// ==========================================================================
		// Produce la stampa
		// ==========================================================================
		ByteArrayOutputStream baos = ie.ExStampaDocumento(enm, utm);
		// ==============================================
		// Setta il documento di stampa sulla response
		// ==============================================
		setRequestAttribute("report", baos);

		// info per il log
		siesLogger.info(getClass().getName() + ".processRequest: fine");

		// valore di ritorno
		return IWebConstants.PG_DOWNLOAD;
	}

}