package siap.siep.misuraalternativa.action;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;

import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
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
import siap.siep.refertoscarcerazione.controller.IRefertoScarcerazione;
import siap.siep.refertoscarcerazione.model.RefertoScarcerazioneModel;
import siap.siep.util.SIEPLookupRemote;

/**
 * <p>
 * Title: ActStampaMAPerditaEfficacia
 * </p>
 * <p>
 * Description: Produce il documento
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author not attributable
 * @version 1.0
 */

public class ActStampaMAPerditaEfficacia extends ActionSiap implements ICostantiMisuraAlternativa {
	public String processRequest() throws F3BException {
		FascicoloSiepModel lFascicoloModel = (FascicoloSiepModel) getSessionAttribute("fascicolo");
		UtenteModel lUtenteMod = this.getUtenteConnesso();
		UfficioModel lUff = this.getUfficioUtenteConnesso();

		// String notificaE = this.getRequestStringParameter("notificaE");
		// String tipoMisura = null;
		String lId = getRequestStringParameter(ICostantiEvento.CAMPO_ID_EVENTO);

		IEvento lCtrl = SICOLookupRemote.getEventoRemote();
		EventoModel lEventoModel = lCtrl.ExRicercaEventoByKey(new BigDecimal(lId));
		String lMotivo = lEventoModel.getCodMotivo();

		if (lMotivo.equals("2164") || lMotivo.equals("2165") || lMotivo.equals("2166")
				|| lMotivo.equals("2167")) {
			// tipoMisura = "DETENZIONE";
		}
		if (lMotivo.equals("2160") || lMotivo.equals("2161") || lMotivo.equals("2162")) {
			// tipoMisura = "AFFIDAMENTO";
		}
		if (lMotivo.equals("2163")) {
			// tipoMisura = "SEMILIBERTA";
		}
		if (lMotivo.equals("2289")) {
			// tipoMisura = "INDULTINO";
		}
		// 08/11/2010 Espiazione pena presso Domicilio
		if (lMotivo.equals(ICostantiMisuraAlternativa.PEREFF_ESP_PRESSO_DOM_MOTIVO)) {
			// tipoMisura = ICostantiMisuraAlternativa.ESP_PRESSO_DOM;
		}

		EventoNotificaModel lEveMod = new EventoNotificaModel();

		lEveMod.getEvento().setIdEvento(new BigDecimal(lId));
		lEveMod.getEvento().setFasSieIdFascicoloSiep(lFascicoloModel.getIdFascicoloSiep());

		lEveMod.getEvento().setDescrLuogoEmittente(lUff.getDescrComune());
		lEveMod.getEvento().setDescrUfficioEmittente(lUff.getDescrTipoUfficio());

		lEveMod.getEvento().setDataAggiornamento(DateUtils.getSysDate());
		lEveMod.getEvento().setCodUfficioAggiornamento(lUff.getCodUfficio());
		lEveMod.getEvento().setCodOperatoreAggiornamento(this.getCodUtenteConnesso());
		lEveMod.getEvento().setFlagDocumentoRegistrato("N");

		// ricerca misura alternativa

		IMisuraAlternativa lMisAltCtrl = SICOLookupRemote.getMisuraAlternativaRemote();
		MisuraAlternativaModel lMisAlModConcessa = new MisuraAlternativaModel();
		lMisAlModConcessa = lMisAltCtrl.ExRicercaMisuraAlternativaByIdEvento(lEventoModel.getEveIdEvento());

		IRefertoScarcerazione lRefCtrl = SIEPLookupRemote.getRefertoScarcerazioneRemote();
		RefertoScarcerazioneModel lRefScaMod = new RefertoScarcerazioneModel();
		lRefScaMod = lRefCtrl.ExRicercaRefertoScarcerazioneByEveIdEvento(lEventoModel.getEveIdEvento());

		ITemplate lCtrlTem = SICOLookupRemote.getTemplateRemote();
		TemplateModel lTemMod = new TemplateModel();
		String flagTemplate = null;
		String lTipoEve = null;

		if (lRefScaMod != null && lRefScaMod.getIdRefertoScarcerazione() != null) {
			flagTemplate = "0";
			lTipoEve = "14";
		} else if (lMisAlModConcessa != null && lMisAlModConcessa.getIdMisuraAlternativa() != null) {
			flagTemplate = "1";
			lTipoEve = "01";
		}

		lTemMod = lCtrlTem.ExRicercaTemplateByTipEveTipoProvCodMotivoFlagTemplate(lTipoEve, "03", lMotivo,
				flagTemplate);
		lEveMod.setNomeTemplate(lTemMod.getIdTemplate());

		ByteArrayOutputStream lReport = lCtrl.ExStampaDocumento(lEveMod, lUtenteMod); // setta la risposta
																						// nella request

		// Prepara la pagina di destinazione

		setRequestAttribute("report", lReport);

		return IWebConstants.PG_DOWNLOAD;
	}

}