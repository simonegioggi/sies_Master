package siap.siep.sanzionesostitutiva.action;

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
 * Azione di stampa della nota di trasmissione
 *
 * @author d.fiorletta
 * @since MEV_2023-33
 */
public class ActStampaNotaTrasmissione extends ActionSiap {

	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public String processRequest() throws F3BException {

		siesLogger.info(getClass().getName() + ".processRequest: inizio");

		FascicoloSiepModel lFascicoloModel = (FascicoloSiepModel) getSessionAttribute("fascicolo");

		BigDecimal lIdEvento = this.getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO);

		UtenteModel lUtenteMod = this.getUtenteConnesso();
		UfficioModel lUff = this.getUfficioUtenteConnesso();

		// ==========================================================================
		// Recupero l'evento per il quale produrre la Stampa (comunicazione)
		// ==========================================================================
		IEvento lCtrl = SICOLookupRemote.getEventoRemote();
		EventoModel lEventoModel = lCtrl.ExRicercaEventoByKey(lIdEvento);

		// ==========================================================================
		// Recupero il template
		// ==========================================================================
		String flagTemplate = null;
		ITemplate lCtrlTem = SICOLookupRemote.getTemplateRemote();
		TemplateModel lTemMod = new TemplateModel();
		lTemMod = lCtrlTem.ExRicercaTemplateByTipEveTipoProvCodMotivoFlagTemplate(
				lEventoModel.getCodTipoEvento(), lEventoModel.getCodTipoProvvedimento(),
				lEventoModel.getCodMotivo(), flagTemplate);

		siesLogger.debug("lTemMod = " + lTemMod);

		// ==========================================================================
		// Genero il model Evento da passare alla funzione di stampa
		// ==========================================================================
		EventoNotificaModel lEveNotMod = new EventoNotificaModel();
		lEveNotMod.setNomeTemplate(lTemMod.getIdTemplate());

		lEveNotMod.getEvento().setIdEvento(lIdEvento);
		lEveNotMod.getEvento().setFasSieIdFascicoloSiep(lFascicoloModel.getIdFascicoloSiep());

		lEveNotMod.getEvento().setDescrLuogoEmittente(lUff.getDescrComune());
		lEveNotMod.getEvento().setDescrUfficioEmittente(lUff.getDescrTipoUfficio());

		lEveNotMod.getEvento().setDataAggiornamento(DateUtils.getSysDate());
		lEveNotMod.getEvento().setCodUfficioAggiornamento(lUff.getCodUfficio());
		lEveNotMod.getEvento().setCodOperatoreAggiornamento(this.getCodUtenteConnesso());

		lEveNotMod.getEvento().setFlagDocumentoRegistrato("N");

		// ==========================================================================
		// Produce la stampa
		// ==========================================================================
		ByteArrayOutputStream lReport = lCtrl.ExStampaDocumento(lEveNotMod, lUtenteMod);

		// ==============================================
		// Setta il documento di stampa sulla response
		// ==============================================
		setRequestAttribute("report", lReport);

		// info per il log
		siesLogger.info(getClass().getName() + ".processRequest: fine");

		// valore di ritorno
		return IWebConstants.PG_DOWNLOAD;
	}

}