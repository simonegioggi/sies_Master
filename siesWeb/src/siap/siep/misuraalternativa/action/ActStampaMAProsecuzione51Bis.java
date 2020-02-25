package siap.siep.misuraalternativa.action;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoModel;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.misuraalternativa.controller.IMisuraAlternativa;
import siap.sico.misuraalternativa.model.MisuraAlternativaModel;
import siap.sico.template.controller.ITemplate;
import siap.sico.template.model.TemplateModel;
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.utente.model.UtenteModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;

/**
 * 
 * @author d.fiorletta
 * 
 */
public class ActStampaMAProsecuzione51Bis extends ActionSiap implements ICostantiMisuraAlternativa {

	public String processRequest() throws F3BException {

		FascicoloSiepModel lFascicoloModel = (FascicoloSiepModel) getSessionAttribute("fascicolo");
		UtenteModel lUtenteMod = this.getUtenteConnesso();
		UfficioModel lUff = this.getUfficioUtenteConnesso();

		String lIdEvento = getRequestStringParameter(ICostantiEvento.CAMPO_ID_EVENTO);

		// Recupero l'evento
		IEvento lCtrl = SICOLookupRemote.getEventoRemote();
		EventoModel lEventoModel = lCtrl.ExRicercaEventoByKey(new BigDecimal(lIdEvento));
		String lMotivoEvento = lEventoModel.getCodMotivo();

		List<String> lCodiciMDS = Arrays.asList("5470", "5471", "5472", "5473", "5474", "5475", "5476",
				"5477", "5478", "5479");
//		List<String> lCodiciTDS = Arrays.asList("5480", "5481", "5482", "5483", "5484", "5485", "5486",
//				"5487", "5489", "5490");

		// ==================================
		// Determino se con o senza cumulo
		// ==================================
		// Recupera la misura associata
		IMisuraAlternativa lMisAltCtrl = SICOLookupRemote.getMisuraAlternativaRemote();
		MisuraAlternativaModel lMisuraModel = lMisAltCtrl.ExRicercaMisuraAlternativaByIdEvento(lEventoModel
				.getEveIdEvento());

		String lIsConCumulo = null;
		if (lMisuraModel.getCodNaturaDecisione().equals("EC"))
			lIsConCumulo = "S";
		else
			lIsConCumulo = "N";

		// ==========================================================================
		// Per la scelta dalla stampa devo determinare:
		// - il tipo di Misura
		// - con cumulo o senza cumulo
		// n.b. in caso di TDS è sempre 'ED' con cumulo quindi flag template 0
		// ==========================================================================
		String flagTemplate = null;
		if (lCodiciMDS.contains(lMotivoEvento)) {
			if ("S".equals(lIsConCumulo))
				flagTemplate = "0";
			else
				flagTemplate = "1";
		} else
			flagTemplate = "0"; // TDS

		// ==========================================================================
		// Recupero il template associato
		// ==========================================================================
		String idTemplate = null;
//		if (flagTemplate != null) {
		ITemplate lCtrlTem = SICOLookupRemote.getTemplateRemote();
		TemplateModel lTemMod = new TemplateModel();
		lTemMod = lCtrlTem.ExRicercaTemplateByTipEveTipoProvCodMotivoFlagTemplate("01",
				lEventoModel.getCodTipoProvvedimento(), lEventoModel.getCodMotivo(), flagTemplate);
		idTemplate = lTemMod.getIdTemplate();
//		} else {
//			idTemplate = TEMPLATE_VUOTO; // per ora mai vero
//		}

		// ====================
		EventoNotificaModel lEveMod = new EventoNotificaModel();

		lEveMod.getEvento().setIdEvento(new BigDecimal(lIdEvento));
		lEveMod.getEvento().setFasSieIdFascicoloSiep(lFascicoloModel.getIdFascicoloSiep());

		lEveMod.getEvento().setDescrLuogoEmittente(lUff.getDescrComune());
		lEveMod.getEvento().setDescrUfficioEmittente(lUff.getDescrTipoUfficio());

		lEveMod.setNomeTemplate(idTemplate);

		lEveMod.getEvento().setDataAggiornamento(DateUtils.getSysDate());
		lEveMod.getEvento().setCodUfficioAggiornamento(lUff.getCodUfficio());
		lEveMod.getEvento().setCodOperatoreAggiornamento(this.getCodUtenteConnesso());

		lEveMod.getEvento().setFlagDocumentoRegistrato("N");

		// ============================
		ByteArrayOutputStream lReport = lCtrl.ExStampaDocumento(lEveMod, lUtenteMod);

		setRequestAttribute("report", lReport);

		return IWebConstants.PG_DOWNLOAD;
	}

}