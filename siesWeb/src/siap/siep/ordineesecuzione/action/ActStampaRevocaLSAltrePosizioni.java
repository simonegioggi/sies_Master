package siap.siep.ordineesecuzione.action;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;

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
import siap.siep.motivoevento.controller.IMotivoEvento;
import siap.siep.motivoevento.model.MotivoEventoModel;
import siap.siep.posizione.controller.IPosizioneGiuridica;
import siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel;
import siap.siep.util.SIEPLookupRemote;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
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
public class ActStampaRevocaLSAltrePosizioni extends ActionSiap implements ICostantiOrdineEsecuzione {

	public String processRequest() throws F3BException {

		String lId = getRequestStringParameter(ICostantiEvento.CAMPO_ID_EVENTO);

		FascicoloSiepModel lFascicoloModel = (FascicoloSiepModel) getSessionAttribute("fascicolo");
		UtenteModel lUtenteMod = this.getUtenteConnesso();
		UfficioModel lUff = this.getUfficioUtenteConnesso();

		PosizioneGiuridicaLuogoDetenzioneAltraCausaModel lPos = new PosizioneGiuridicaLuogoDetenzioneAltraCausaModel();
		IPosizioneGiuridica lPosCtrl = SIEPLookupRemote.getPosizioneGiuridicaRemote();
		lPos = lPosCtrl.ExRicercaPosizioneGiuridicaLuogoDetenzioneAltraCausaCorrentiByIdFascicolo(
				lFascicoloModel.getIdFascicoloSiep());

		EventoNotificaModel lEveMod = new EventoNotificaModel();

		IEvento lCtrl = SICOLookupRemote.getEventoRemote();
		EventoModel lEventoModel = lCtrl.ExRicercaEventoByKey(new BigDecimal(lId));

		lEveMod.getEvento().setIdEvento(new BigDecimal(lId));
		lEveMod.getEvento().setFasSieIdFascicoloSiep(lFascicoloModel.getIdFascicoloSiep());

		lEveMod.getEvento().setDescrLuogoEmittente(lUff.getDescrComune());
		lEveMod.getEvento().setDescrUfficioEmittente(lUff.getDescrTipoUfficio());

		lEveMod.getEvento().setDataAggiornamento(DateUtils.getSysDate());
		lEveMod.getEvento().setCodUfficioAggiornamento(lUff.getCodUfficio());
		lEveMod.getEvento().setCodOperatoreAggiornamento(this.getCodUtenteConnesso());
		lEveMod.getEvento().setFlagDocumentoRegistrato("N");

		String lTemplate = null;

		// ricerca motivo_evento
		IMotivoEvento lCtrlMotivoEvento = SIEPLookupRemote.getMotivoEventoRemote();
		MotivoEventoModel lMotivoEveMod = new MotivoEventoModel();
		lMotivoEveMod = lCtrlMotivoEvento.ExRicercaMotivoEventoByEveIdEvento(new BigDecimal(lId));

		if (lMotivoEveMod != null && lMotivoEveMod.getCodMotivoRevoca() != null
				&& lMotivoEveMod.getCodMotivoRevoca().equals("0002")) { // reiezione
			if (lPos.getAltraCausa() != null && lPos.getAltraCausa().getIdAltraCausa() != null
					&& lPos.getAltraCausa().getDataDecorrenza() != null
					&& lPos.getAltraCausa().getDataScadenza() != null) {
				lTemplate = "2";
			} else if (lPos.getAltraCausa() != null && lPos.getAltraCausa().getIdAltraCausa() != null) {
				if (lPos.getAltraCausa().isLiberoAltraCausa()) {
					lTemplate = "0";
				} else {
					lTemplate = "1";
				}
			} else if (lPos.getPosizioneGiuridica() != null && lPos.getPosizioneGiuridica().isLibero()) {
				lTemplate = "0";
			} else if (lPos.getPosizioneGiuridica() != null
					&& lPos.getPosizioneGiuridica().getCodPosizioneGiuridica() != null
					&& (lPos.getPosizioneGiuridica().getCodPosizioneGiuridica().equals("02")
							|| lPos.getPosizioneGiuridica().getCodPosizioneGiuridica().equals("04")
							|| lPos.getPosizioneGiuridica().getCodPosizioneGiuridica().equals("82")
							|| lPos.getPosizioneGiuridica().getCodPosizioneGiuridica().equals("83")
							|| lPos.getPosizioneGiuridica().getCodPosizioneGiuridica().equals("84")
							|| lPos.getPosizioneGiuridica().getCodPosizioneGiuridica().equals("77")
							|| lPos.getPosizioneGiuridica().getCodPosizioneGiuridica().equals("78")
							|| lPos.getPosizioneGiuridica().getCodPosizioneGiuridica().equals("79")
							|| lPos.getPosizioneGiuridica().getCodPosizioneGiuridica().equals("80")
							|| lPos.getPosizioneGiuridica().getCodPosizioneGiuridica().equals("81")
							|| lPos.getPosizioneGiuridica().getCodPosizioneGiuridica().equals("76"))) {
				lTemplate = "3";
			} else if (lPos.getPosizioneGiuridica() != null
					&& lPos.getPosizioneGiuridica().getCodPosizioneGiuridica() != null
					&& lPos.getPosizioneGiuridica().getCodPosizioneGiuridica().equals("49")) {
				lTemplate = "4";
			}
		} else if (lMotivoEveMod != null && lMotivoEveMod.getCodMotivoRevoca() != null
				&& lMotivoEveMod.getCodMotivoRevoca().equals("0003")) { // omessa
			if (lPos.getAltraCausa() != null && lPos.getAltraCausa().getIdAltraCausa() != null
					&& lPos.getAltraCausa().getDataDecorrenza() != null
					&& lPos.getAltraCausa().getDataScadenza() != null
					&& (lPos.getAltraCausa() != null && lPos.getAltraCausa().getCodTipoPosGiuridica() != null
							&& !lPos.getAltraCausa().getCodTipoPosGiuridica().equals("77")
							&& !lPos.getAltraCausa().getCodTipoPosGiuridica().equals("78")
							&& !lPos.getAltraCausa().getCodTipoPosGiuridica().equals("79")
							&& !lPos.getAltraCausa().getCodTipoPosGiuridica().equals("80")
							&& !lPos.getAltraCausa().getCodTipoPosGiuridica().equals("81")
							&& !lPos.getAltraCausa().getCodTipoPosGiuridica().equals("76"))) {
				lTemplate = "7";
			}
			/*
			 * else if(lPos.getAltraCausa() != null && lPos.getAltraCausa().getIdAltraCausa()!= null &&
			 * (lPos.getAltraCausa().getCodTipoPosGiuridica()!=null &&
			 * !lPos.getAltraCausa().getCodTipoPosGiuridica().equals("77") &&
			 * !lPos.getAltraCausa().getCodTipoPosGiuridica().equals("78") &&
			 * !lPos.getAltraCausa().getCodTipoPosGiuridica().equals("79") &&
			 * !lPos.getAltraCausa().getCodTipoPosGiuridica().equals("80") &&
			 * !lPos.getAltraCausa().getCodTipoPosGiuridica().equals("81") &&
			 * !lPos.getAltraCausa().getCodTipoPosGiuridica().equals("76")) ) { lTemplate = "6"; }
			 */
			else if (lPos.getAltraCausa() != null && lPos.getAltraCausa().getIdAltraCausa() != null
					&& lPos.getAltraCausa().getCodTipoPosGiuridica() != null
					&& (lPos.getAltraCausa().getCodTipoPosGiuridica().equals("76")
							|| lPos.getAltraCausa().getCodTipoPosGiuridica().equals("77")
					// || lPos.getAltraCausa().getCodTipoPosGiuridica().equals("78")
					// || lPos.getAltraCausa().getCodTipoPosGiuridica().equals("79")
					// || lPos.getAltraCausa().getCodTipoPosGiuridica().equals("80")
					// || lPos.getAltraCausa().getCodTipoPosGiuridica().equals("81")
					)) {
				lTemplate = "6";
				// lTemplate = "0";
			} else if (lPos.getAltraCausa() != null && lPos.getAltraCausa().getIdAltraCausa() != null
					&& lPos.getAltraCausa().getCodTipoPosGiuridica() != null
					&& (lPos.getAltraCausa().getCodTipoPosGiuridica().equals("78")
							|| lPos.getAltraCausa().getCodTipoPosGiuridica().equals("79")
							|| lPos.getAltraCausa().getCodTipoPosGiuridica().equals("80")
							|| lPos.getAltraCausa().getCodTipoPosGiuridica().equals("81"))) {
				lTemplate = "9";
			} else if (lPos.getPosizioneGiuridica() != null && lPos.getPosizioneGiuridica().isLibero()
					|| (lPos.getAltraCausa() != null && lPos.getAltraCausa().getCodTipoPosGiuridica() != null
							&& !lPos.getAltraCausa().getCodTipoPosGiuridica().equals("77")
							&& !lPos.getAltraCausa().getCodTipoPosGiuridica().equals("78")
							&& !lPos.getAltraCausa().getCodTipoPosGiuridica().equals("79")
							&& !lPos.getAltraCausa().getCodTipoPosGiuridica().equals("80")
							&& !lPos.getAltraCausa().getCodTipoPosGiuridica().equals("81")
							&& !lPos.getAltraCausa().getCodTipoPosGiuridica().equals("76"))) {
				lTemplate = "5";
			}
		} else if (lMotivoEveMod != null && lMotivoEveMod.getCodMotivoRevoca() != null
				&& lMotivoEveMod.getCodMotivoRevoca().equals("0001")) { // revoca pm
			// Commentato e sostituito con codice seguente come richiesto da
			// segnalazione jira n. 218
			/*
			 * if(lPos.getPosizioneGiuridica() != null && lPos.getPosizioneGiuridica().isLibero() ||
			 * (lPos.getAltraCausa()!=null && lPos.getAltraCausa().getCodTipoPosGiuridica()!=null &&
			 * !lPos.getAltraCausa().getCodTipoPosGiuridica().equals("77") &&
			 * !lPos.getAltraCausa().getCodTipoPosGiuridica().equals("78") &&
			 * !lPos.getAltraCausa().getCodTipoPosGiuridica().equals("79") &&
			 * !lPos.getAltraCausa().getCodTipoPosGiuridica().equals("80") &&
			 * !lPos.getAltraCausa().getCodTipoPosGiuridica().equals("81") &&
			 * !lPos.getAltraCausa().getCodTipoPosGiuridica().equals("76") ) ) lTemplate = "8"; }
			 */
			if (lPos.getAltraCausa() != null && lPos.getAltraCausa().getCodTipoPosGiuridica() != null) {
				if (lPos.getAltraCausa().getCodTipoPosGiuridica().equals("76")
						|| lPos.getAltraCausa().getCodTipoPosGiuridica().equals("77")) {
					lTemplate = "8";
				} else if (lPos.getAltraCausa().getCodTipoPosGiuridica().equals("78")
						|| lPos.getAltraCausa().getCodTipoPosGiuridica().equals("79")
						|| lPos.getAltraCausa().getCodTipoPosGiuridica().equals("80")
						|| lPos.getAltraCausa().getCodTipoPosGiuridica().equals("81")) {
					lTemplate = "9";
				}
			}
			// 20200129 [SG]: segnalazione post collaudo 11.3: se soggetto è libero allora deve uscire
			// template "SIEP_RS_REVO.rtf"
			else if (lPos.getPosizioneGiuridica() != null && lPos.getPosizioneGiuridica().isLibero())
				lTemplate = "8";
		}

		TemplateModel lTemMod = new TemplateModel();
		ITemplate lCtrlTem = SICOLookupRemote.getTemplateRemote();

		if (lTemplate != null && lTemplate != null && lEventoModel.getCodMotivo() != null
				&& !lEventoModel.getCodMotivo().equals("0000")) {
			lTemMod = lCtrlTem.ExRicercaTemplateByTipEveTipoProvCodMotivoFlagTemplate(
					lEventoModel.getCodTipoEvento(), lEventoModel.getCodTipoProvvedimento(),
					lEventoModel.getCodMotivo(), lTemplate);
			lEveMod.setNomeTemplate(lTemMod.getIdTemplate());
		} else {
			lEveMod.setNomeTemplate(TEMPLATE_ALTRE_POSIZIONI_VUOTO);
		}

		ByteArrayOutputStream lReport = lCtrl.ExStampaDocumento(lEveMod, lUtenteMod);
		// setta la risposta nella request
		setRequestAttribute("report", lReport);
		setRequestAttribute("fc", getRequestStringParameter("fc"));

		return IWebConstants.PG_DOWNLOAD;
	}

}