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

/**
 * <p>
 * Title: ActStampaMASospProvv
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

public class ActStampaMASospProvv extends ActionSiap implements ICostantiMisuraAlternativa {

	public String processRequest() throws F3BException {

		FascicoloSiepModel lFascicoloModel = (FascicoloSiepModel) getSessionAttribute("fascicolo");
		UtenteModel lUtenteMod = this.getUtenteConnesso();
		UfficioModel lUff = this.getUfficioUtenteConnesso();

		// String notificaE = this.getRequestStringParameter("notificaE");
		String tipoMisura = this.getRequestStringParameter("tipoMisura");
		String lId = getRequestStringParameter(ICostantiEvento.CAMPO_ID_EVENTO);

		IEvento lCtrl = SICOLookupRemote.getEventoRemote();
		EventoModel lEventoModel = lCtrl.ExRicercaEventoByKey(new BigDecimal(lId));
		// String lMotivo = lEventoModel.getCodMotivo();

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
		String lflagScarcerato = lMisAlModConcessa.getCodTipoUfficioScarcerazione();

		ITemplate lCtrlTem = SICOLookupRemote.getTemplateRemote();
		TemplateModel lTemMod = new TemplateModel();
		String flagTemplate = null;

		if (tipoMisura.equals("AFFIDAMENTO") || tipoMisura.equals("DETENZIONE")
				|| tipoMisura.equals("SEMILIBERTA") || tipoMisura.equals("INDULTINO")
				|| tipoMisura.equals(ICostantiMisuraAlternativa.ESP_PRESSO_DOM)) // 22/11/2010
		{
			if (lflagScarcerato.equals("SORV"))
				flagTemplate = "1";
			else if (lflagScarcerato.equals("PROC"))
				flagTemplate = "0";
		} else if (tipoMisura.equals("AFFIDAMENTO51BIS") || tipoMisura.equals("DETENZIONE51BIS")
				|| tipoMisura.equals("SEMILIBERTA51BIS") || tipoMisura.equals("INDULTINO51BIS")
				|| tipoMisura.equals(ICostantiMisuraAlternativa.SOSP_ESP_PRESSO_DOM_51BIS)) // 22/11/2010
		{
			if (lflagScarcerato.equals("SORV"))
				flagTemplate = "3";
			else if (lflagScarcerato.equals("PROC"))
				flagTemplate = "4";
		}

		lTemMod = lCtrlTem.ExRicercaTemplateByTipEveTipoProvCodMotivoFlagTemplate("01", "02",
				lMisAlModConcessa.getCodTipoMisura(), flagTemplate);
		lEveMod.setNomeTemplate(lTemMod.getIdTemplate());
		ByteArrayOutputStream lReport = lCtrl.ExStampaDocumento(lEveMod, lUtenteMod); // setta la risposta
																						// nella request

		// Prepara la pagina di destinazione
		setRequestAttribute("report", lReport);

		return IWebConstants.PG_DOWNLOAD;
	}

}