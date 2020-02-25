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
import siap.siep.util.SIEPLookupRemote;

/**
 * <p>
 * Title: ActStampaVariazioneMAAmmProvvisoria
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

public class ActStampaVariazioneMADecSca extends ActionSiap implements ICostantiMisuraAlternativa {
	public String processRequest() throws F3BException {
		FascicoloSiepModel lFascicoloModel = (FascicoloSiepModel) getSessionAttribute("fascicolo");
		UtenteModel lUtenteMod = this.getUtenteConnesso();
		UfficioModel lUff = this.getUfficioUtenteConnesso();
		// String lTipoMisura = this.getRequestStringParameter("tipoMisura");

		PosizioneGiuridicaLuogoDetenzioneAltraCausaModel lPosAltra = new PosizioneGiuridicaLuogoDetenzioneAltraCausaModel();
		PosizioneGiuridicaModel lPosGiu = new PosizioneGiuridicaModel();
		IPosizioneGiuridica lPosCtrl = SIEPLookupRemote.getPosizioneGiuridicaRemote();

		lPosAltra = lPosCtrl.ExRicercaPosizioneGiuridicaLuogoDetenzioneAltraCausaCorrentiByIdFascicolo(
				lFascicoloModel.getIdFascicoloSiep());
		String lPosizioneGiu = lPosAltra.getPosizioneGiuridica().getCodPosizioneGiuridica();
		lPosGiu.setCodPosizioneGiuridica(lPosizioneGiu);

		String lId = getRequestStringParameter(ICostantiEvento.CAMPO_ID_EVENTO);

		IEvento lCtrl = SICOLookupRemote.getEventoRemote();
		EventoModel lEventoModel = lCtrl.ExRicercaEventoByKey(new BigDecimal(lId));

		String lMotivo = lEventoModel.getCodMotivo();
		String lTipoProv = lEventoModel.getCodTipoProvvedimento();

		EventoNotificaModel lEveMod = new EventoNotificaModel();

		lEveMod.getEvento().setIdEvento(new BigDecimal(lId));
		lEveMod.getEvento().setFasSieIdFascicoloSiep(lFascicoloModel.getIdFascicoloSiep());

		lEveMod.getEvento().setDescrLuogoEmittente(lUff.getDescrComune());
		lEveMod.getEvento().setDescrUfficioEmittente(lUff.getDescrTipoUfficio());
		lEveMod.getEvento().setDataAggiornamento(DateUtils.getSysDate());
		lEveMod.getEvento().setCodUfficioAggiornamento(lUff.getCodUfficio());
		lEveMod.getEvento().setCodOperatoreAggiornamento(this.getCodUtenteConnesso());
		lEveMod.getEvento().setFlagDocumentoRegistrato("N");

		// AMBROSINO --> Cambiata la ricerca perchè l'evento che passo è legato all'evento principale
		// e non alla Misura Alternativa

		lCtrl = SICOLookupRemote.getEventoRemote();
		EventoModel lEveModAnn = new EventoModel();
		lEveModAnn = lCtrl.ExRicercaEventoByKey(lEventoModel.getEveIdEvento());

		// ricerca misura alternativa
		IMisuraAlternativa lMisAltCtrl = SICOLookupRemote.getMisuraAlternativaRemote();
		// MisuraAlternativaModel lMisAlModConcessa = new MisuraAlternativaModel();
		/* lMisAlModConcessa = */lMisAltCtrl
				.ExRicercaMisuraAlternativaByIdEvento(lEveModAnn.getEveIdEvento());
		// String lflagScarcerato = lMisAlModConcessa.getCodTipoUfficioScarcerazione();

		// ricerca del template
		ITemplate lCtrlTem = SICOLookupRemote.getTemplateRemote();
		TemplateModel lTemMod = new TemplateModel();
		String flagTemplate = "4";

		if (flagTemplate != null && lMotivo != null && lMotivo.equals("5415")) {
			lTemMod = lCtrlTem.ExRicercaTemplateByTipEveTipoProvCodMotivoFlagTemplate("01", lTipoProv,
					lMotivo, flagTemplate);
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