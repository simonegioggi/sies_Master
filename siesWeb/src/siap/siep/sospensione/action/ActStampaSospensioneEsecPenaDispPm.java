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
import siap.siep.ordineesecuzione.controller.IOrdineEsecuzione;
import siap.siep.posizione.controller.IPosizioneGiuridica;
import siap.siep.posizione.model.PosizioneGiuridicaModel;
import siap.siep.sospensione.controller.ISospensione;
import siap.siep.util.SIEPLookupRemote;

/**
 * <p>
 * Title: ActStampaSospensioneEsecPenaDispPm
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
public class ActStampaSospensioneEsecPenaDispPm extends ActionSiap implements ICostantiSospensione {

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
		// IPenaResidua lCtrlPenRes = SIEPLookupRemote.getPenaResiduaRemote();

		IOrdineEsecuzione lCtrlOr = SIEPLookupRemote.getOrdineEsecuzioneRemote();
		boolean lFlagOrdineEsecuzione = lCtrlOr
				.ExEsisteOrdineEsecuzioneByFascicoloSiep(lFascicoloModel.getIdFascicoloSiep());

		//// impostare template
		String flagTemplate = null;
		if (lMotivo != null && (lMotivo.equals("0900") || lMotivo.equals("0901"))) {
			if (lPosizione.isLibero()) {
				if (lFlagOrdineEsecuzione)
					flagTemplate = "1";
				else
					flagTemplate = "0";
			} else if (lPosizione.getCodPosizioneGiuridica() != null
					&& (lPosizione.getCodPosizioneGiuridica().equals("01")
							|| lPosizione.getCodPosizioneGiuridica().equals("02"))) {
				flagTemplate = "3";
			} else {
				flagTemplate = "4";
			}
		} else if (lMotivo != null && lMotivo.equals("0902")) {
			flagTemplate = "1";
		} else if (lMotivo != null && lMotivo.equals("0903")) {
			if (lPosizione.isLibero()) {
				flagTemplate = "0";
			} else {
				flagTemplate = "3";
			}
		} else if (lMotivo != null && (lMotivo.equals("0920") || lMotivo.equals("0921"))) {
			if (lPosizione.isLibero()) {
				flagTemplate = "0";
			} else {
				flagTemplate = null;
			}
		}
		// MODIFICA PER A7-RR-029
		else if (lMotivo != null && lMotivo.equals("0937")) {
			if (lPosizione.isLibero()) {
				flagTemplate = "0";
			} else if (lPosizione.getCodPosizioneGiuridica().equals("01")
					|| lPosizione.getCodPosizioneGiuridica().equals("03")) {
				flagTemplate = "1";
			}
		}
		// Sospensione Legge Alfano. Luigi 1-9-2010
		else if ((lMotivo != null) && (lMotivo.equals("0947") || lMotivo.equals("0952"))) {
			flagTemplate = "0";
		}

		if (flagTemplate != null && lMotivo != null && !lMotivo.equals("0000")) {
			ITemplate lCtrlTem = SICOLookupRemote.getTemplateRemote();
			TemplateModel lTemMod = new TemplateModel();
			lTemMod = lCtrlTem.ExRicercaTemplateByTipEveTipoProvCodMotivoFlagTemplate(
					lEventoModel.getCodTipoEvento(), lEventoModel.getCodTipoProvvedimento(), lMotivo,
					flagTemplate);
			lEveMod.setNomeTemplate(lTemMod.getIdTemplate());
		} else {
			lEveMod.setNomeTemplate("SIEP_VUOTO");
		}

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