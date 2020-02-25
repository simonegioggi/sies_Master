package siap.siep.ripristino.action;

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
import siap.sico.utente.model.UtenteModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.posizione.controller.IPosizioneGiuridica;
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
public class ActStampaProvvedimentoRipristino extends ActionSiap {

	public String processRequest() throws F3BException {

		BigDecimal lIdEvento = getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO);

		FascicoloSiepModel lFascicoloModel = (FascicoloSiepModel) getSessionAttribute("fascicolo");
		UtenteModel lUtenteMod = this.getUtenteConnesso();

		this.isFascicoloSiepDiCompetenza();

		EventoModel lEventoMod = new EventoModel();

		IEvento lCtrl = SICOLookupRemote.getEventoRemote();
		lEventoMod = lCtrl.ExRicercaEventoByKey(lIdEvento);

		if (lEventoMod == null)
			throw new F3BException(F3BException.USER_MESSAGE, "Evento non Registrato");

		// POSIZIONE GIURIDICA
		// PosizioneGiuridicaModel lPos = new PosizioneGiuridicaModel();
		IPosizioneGiuridica lPosCtrl = SIEPLookupRemote.getPosizioneGiuridicaRemote();
		/* lPos = */lPosCtrl
				.ExRicercaPosizioneGiuridicaCorrenteByIdFascicolo(lFascicoloModel.getIdFascicoloSiep());
		// String lCodicePosizione = lPos.getCodPosizioneGiuridica();

		// RICERCA TEMPLATE
		TemplateModel lTempMod = new TemplateModel();

		ITemplate lCtrlTem = SICOLookupRemote.getTemplateRemote();

		String lFlagTemplate = "0";

		lTempMod = lCtrlTem.ExRicercaTemplateByTipEveTipoProvCodMotivoFlagTemplate(
				lEventoMod.getCodTipoEvento(), lEventoMod.getCodTipoProvvedimento(),
				lEventoMod.getCodMotivo(), lFlagTemplate);
		String lIdTemplate = "";
		lIdTemplate = lTempMod.getIdTemplate();

		EventoNotificaModel lEveMod = new EventoNotificaModel();

		lEventoMod.setDescrLuogoEmittente(lUtenteMod.getUfficioUtente().getDescrComune());
		lEventoMod.setDescrUfficioEmittente(lUtenteMod.getUfficioUtente().getDescrTipoUfficio());

		lEventoMod.setDataAggiornamento(DateUtils.getSysDate());
		lEventoMod.setCodUfficioAggiornamento(lUtenteMod.getUfficioUtente().getCodUfficio());
		lEventoMod.setCodOperatoreAggiornamento(lUtenteMod.getUserId());
		lEventoMod.setFlagDocumentoRegistrato("N");

		lEveMod.setEvento(lEventoMod);
		lEveMod.setNomeTemplate(lIdTemplate);

		ByteArrayOutputStream lReport = lCtrl.ExStampaDocumento(lEveMod, lUtenteMod); // setta la risposta
																						// nella request

		// Prepara la pagina di destinazione
		setRequestAttribute("report", lReport);

		return IWebConstants.PG_DOWNLOAD;
	}

}