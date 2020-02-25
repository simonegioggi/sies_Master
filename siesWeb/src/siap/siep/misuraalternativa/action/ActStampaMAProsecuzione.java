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
import siap.sico.template.controller.ITemplate;
import siap.sico.template.model.TemplateModel;
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.utente.model.UtenteModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.posizione.controller.IPosizioneGiuridica;
import siap.siep.util.SIEPLookupRemote;

/**
 * <p>
 * Title: ActStampaMAProsecuzione
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
public class ActStampaMAProsecuzione extends ActionSiap implements ICostantiMisuraAlternativa {

	public String processRequest() throws F3BException {

		FascicoloSiepModel lFascicoloModel = (FascicoloSiepModel) getSessionAttribute("fascicolo");
		UtenteModel lUtenteMod = this.getUtenteConnesso();
		UfficioModel lUff = this.getUfficioUtenteConnesso();

		IPosizioneGiuridica lPosCtrl = SIEPLookupRemote.getPosizioneGiuridicaRemote();
		/* PosizioneGiuridicaModel lPosMod = */lPosCtrl
				.ExRicercaPosizioneGiuridicaCorrenteByIdFascicolo(lFascicoloModel.getIdFascicoloSiep());

		String lId = getRequestStringParameter(ICostantiEvento.CAMPO_ID_EVENTO);
		String tipoMisura = getRequestStringParameter("tipoMisura");

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

		String flagTemplate = null;
		String lMotivoOrdinanza = null;

		if (lMotivo != null) {
			if (lMotivo.equals("0374")) {
				lMotivoOrdinanza = "2281";
			} else if (lMotivo.equals("0375")) {
				lMotivoOrdinanza = "2205";
			} else if (lMotivo.equals("0376")) {
				lMotivoOrdinanza = "2282";
			} else if (lMotivo.equals("0377")) {
				lMotivoOrdinanza = "2284";
			} else if (lMotivo.equals("0378")) {
				lMotivoOrdinanza = "2286";
			} else if (lMotivo.equals("0379")) {
				lMotivoOrdinanza = "2285";
			} else if (lMotivo.equals("0380")) {
				lMotivoOrdinanza = "2287";
			} else if (lMotivo.equals("0381")) {
				lMotivoOrdinanza = "2288";
			} else if (lMotivo.equals("0382")) {
				lMotivoOrdinanza = "2283";
			} else {
				lMotivoOrdinanza = "0000";
			}
			// if(lMotivo.equals("2205") || lMotivo.equals("2281") || lMotivo.equals("2282"))
			if (lMotivo.equals("0375") || lMotivo.equals("0374") || lMotivo.equals("0376")) {
				if (tipoMisura != null && tipoMisura.equals("AFFIDAMENTOCUMULO")) {
					flagTemplate = "5";
				} else {
					flagTemplate = "0";
				}
			}
			// else if(lMotivo.equals("2284") || lMotivo.equals("2285") || lMotivo.equals("2286") ||
			// lMotivo.equals("2287") || lMotivo.equals("2288"))
			else if (lMotivo.equals("0377") || lMotivo.equals("0379") || lMotivo.equals("0378")
					|| lMotivo.equals("0380") || lMotivo.equals("0381")) {
				if (tipoMisura != null && tipoMisura.equals("DETENZIONECUMULO")) {
					flagTemplate = "6";
				} else {
					flagTemplate = "1";
				}
			}
			// else if(lMotivo.equals("2283"))
			else if (lMotivo.equals("0382")) {
				if (tipoMisura != null && tipoMisura.equals("SEMILIBERTACUMULO")) {
					flagTemplate = "7";
				} else {
					flagTemplate = "2";
				}
			}
		}

		if (flagTemplate != null && lMotivoOrdinanza != null && !lMotivoOrdinanza.equals("0000")) {
			ITemplate lCtrlTem = SICOLookupRemote.getTemplateRemote();
			TemplateModel lTemMod = new TemplateModel();
			lTemMod = lCtrlTem.ExRicercaTemplateByTipEveTipoProvCodMotivoFlagTemplate("01", "03",
					lMotivoOrdinanza, flagTemplate);
			lEveMod.setNomeTemplate(lTemMod.getIdTemplate());
		} else {
			lEveMod.setNomeTemplate(TEMPLATE_VUOTO);
		}

		ByteArrayOutputStream lReport = lCtrl.ExStampaDocumento(lEveMod, lUtenteMod); // setta la risposta
																						// nella request

		// Prepara la pagina di destinazione
		setRequestAttribute("report", lReport);

		return IWebConstants.PG_DOWNLOAD;
	}

}