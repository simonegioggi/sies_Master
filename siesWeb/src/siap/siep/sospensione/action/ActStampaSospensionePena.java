package siap.siep.sospensione.action;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;

import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
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
import siap.siep.penaresidua.controller.IPenaResidua;
import siap.siep.posizione.controller.IPosizioneGiuridica;
import siap.siep.posizione.model.PosizioneGiuridicaModel;
import siap.siep.sospensione.controller.ISospensione;
import siap.siep.util.SIEPLookupRemote;

/**
 * <p>
 * Title: ActStampaSospensionePena
 * </p>
 * <p>
 * Description: Classe Action per la Stampa di Sospensione Pena
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: Bull
 * </p>
 * 
 * @version 1.0
 */
public class ActStampaSospensionePena extends ActionSiap implements ICostantiSospensione {

	public String processRequest() throws F3BException {

		FascicoloSiepModel lFascicoloModel = (FascicoloSiepModel) getSessionAttribute("fascicolo");
		UtenteModel lUtenteMod = this.getUtenteConnesso();
		UfficioModel lUff = this.getUfficioUtenteConnesso();

		// String tipoMisura = null;
		String lId = getRequestStringParameter("IdEvento");

		IEvento lCtrl = SICOLookupRemote.getEventoRemote();
		EventoModel lEventoModel = lCtrl.ExRicercaEventoByKey(new BigDecimal(lId));
		String lMotivo = lEventoModel.getCodMotivo();

		EventoNotificaModel lEveMod = new EventoNotificaModel();

		lEveMod.getEvento().setIdEvento(new BigDecimal(lId));
		lEveMod.getEvento().setFasSieIdFascicoloSiep(lFascicoloModel.getIdFascicoloSiep());

		lEveMod.getEvento().setDescrLuogoEmittente(lUff.getDescrComune());
		lEveMod.getEvento().setDescrUfficioEmittente(lUff.getDescrTipoUfficio());

		lEveMod.getEvento().setDataAggiornamento(DateUtils.getSysDate());
		lEveMod.getEvento().setCodUfficioAggiornamento(lUff.getCodUfficio());
		lEveMod.getEvento().setCodOperatoreAggiornamento(this.getCodUtenteConnesso());
		lEveMod.getEvento().setFlagDocumentoRegistrato("N");

		IPosizioneGiuridica lCtrlPosGiu = SIEPLookupRemote.getPosizioneGiuridicaRemote();
		PosizioneGiuridicaModel lPosizione = lCtrlPosGiu
				.ExRicercaPosizioneGiuridicaCorrenteByIdFascicolo(lFascicoloModel.getIdFascicoloSiep());
		IPenaResidua lCtrlPenRes = SIEPLookupRemote.getPenaResiduaRemote();

		// impostare template
		String flagTemplate = null;

		// PenaResiduaModel lPenaResidua = null;
		/* lPenaResidua = */lCtrlPenRes
				.ExRicercaPenaResiduaUltimaValidata(lFascicoloModel.getIdFascicoloSiep());

		if (lPosizione.isLibero()) {
			flagTemplate = "1";
		} else {
			flagTemplate = "0";
		}

		ITemplate lCtrlTem = SICOLookupRemote.getTemplateRemote();
		TemplateModel lTemMod = new TemplateModel();
		lTemMod = lCtrlTem.ExRicercaTemplateByTipEveTipoProvCodMotivoFlagTemplate(
				lEventoModel.getCodTipoEvento(), lEventoModel.getCodTipoProvvedimento(), lMotivo,
				flagTemplate);
		lEveMod.setNomeTemplate(lTemMod.getIdTemplate());

		ISospensione lctrSosp = SIEPLookupRemote.getSospensioneRemote();
		ByteArrayOutputStream lReport = lctrSosp.ExStampaDocumentoSospensioni(lEveMod, lUtenteMod); // setta
																									// la
																									// risposta
																									// nella
																									// request

		setRequestAttribute("report", lReport);

		return IWebConstants.PG_DOWNLOAD;
	}

}