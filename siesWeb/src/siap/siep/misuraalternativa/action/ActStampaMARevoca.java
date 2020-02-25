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
import siap.siep.posizione.model.PosizioneGiuridicaModel;
import siap.siep.util.SIEPLookupRemote;

/**
 * <p>
 * Title: ActStampaMARevoca
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

public class ActStampaMARevoca extends ActionSiap implements ICostantiMisuraAlternativa {
	public String processRequest() throws F3BException {
		FascicoloSiepModel lFascicoloModel = (FascicoloSiepModel) getSessionAttribute("fascicolo");
		UtenteModel lUtenteMod = this.getUtenteConnesso();
		UfficioModel lUff = this.getUfficioUtenteConnesso();

		IPosizioneGiuridica lPosCtrl = SIEPLookupRemote.getPosizioneGiuridicaRemote();
		PosizioneGiuridicaModel lPosMod = lPosCtrl
				.ExRicercaPosizioneGiuridicaCorrenteByIdFascicolo(lFascicoloModel.getIdFascicoloSiep());

		String lId = getRequestStringParameter(ICostantiEvento.CAMPO_ID_EVENTO);

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
		if (lPosMod != null && lPosMod.getCodPosizioneGiuridica() != null) {
			if (lPosMod.getCodPosizioneGiuridica().equals("31")
					|| lPosMod.getCodPosizioneGiuridica().equals("33")
					|| lPosMod.getCodPosizioneGiuridica().equals("36")
					|| lPosMod.getCodPosizioneGiuridica().equals("38")) {
				flagTemplate = "2";
			} else if (lPosMod.getCodPosizioneGiuridica().equals("12")
					|| lPosMod.getCodPosizioneGiuridica().equals("14")) {
				flagTemplate = "3";
			} else if (lPosMod.getCodPosizioneGiuridica().equals("29")) {
				flagTemplate = "4";
			} else if (lPosMod.getCodPosizioneGiuridica().equals("64")) {
				flagTemplate = "1";
			}
		}

		if (flagTemplate != null && lMotivo != null && !lMotivo.equals("0000")) {
			ITemplate lCtrlTem = SICOLookupRemote.getTemplateRemote();
			TemplateModel lTemMod = new TemplateModel();
			lTemMod = lCtrlTem.ExRicercaTemplateByTipEveTipoProvCodMotivoFlagTemplate("01", "03", lMotivo,
					flagTemplate);
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