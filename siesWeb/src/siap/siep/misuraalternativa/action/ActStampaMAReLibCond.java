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
import siap.siep.util.SIEPLookupRemote;

/**
 * <p>
 * Title: ActStampaMAReLibCond
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
public class ActStampaMAReLibCond extends ActionSiap implements ICostantiMisuraAlternativa {

	public String processRequest() throws F3BException {

		FascicoloSiepModel lFascicoloModel = (FascicoloSiepModel) getSessionAttribute("fascicolo");
		UtenteModel lUtenteMod = this.getUtenteConnesso();
		UfficioModel lUff = this.getUfficioUtenteConnesso();

		// BigDecimal lIdEvento = this.getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO);

		// PosizioneGiuridicaLuogoDetenzioneAltraCausaModel lPosAltra = new
		// PosizioneGiuridicaLuogoDetenzioneAltraCausaModel();
		// PosizioneGiuridicaModel lPosGiuModificata = new PosizioneGiuridicaModel();
		IPosizioneGiuridica lPosCtrl = SIEPLookupRemote.getPosizioneGiuridicaRemote();

		/* lPosAltra = */lPosCtrl.ExRicercaPosizioneGiuridicaLuogoDetenzioneAltraCausaCorrentiByIdFascicolo(
				lFascicoloModel.getIdFascicoloSiep());
		// String lPosizioneGiu = lPosAltra.getPosizioneGiuridica().getCodPosizioneGiuridica();

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

		// ricerca misura alternativa
		// MisuraAlternativaModel lMisMod = new MisuraAlternativaModel();
		IMisuraAlternativa lCtrlMis = SICOLookupRemote.getMisuraAlternativaRemote();
		/* lMisMod = */lCtrlMis.ExRicercaMisuraAlternativaByIdEvento(lEveMod.getEvento().getEveIdEvento());

		// ricerca posizione precedente
		// PosizioneGiuridicaModel lPos = new PosizioneGiuridicaModel();
		// PosizioneGiuridicaModel lPosPrec = new PosizioneGiuridicaModel();
		/* lPosPrec = */lPosCtrl
				.ExRicercaPosizioneGiuridicaPrecedenteByIdFascicolo(lFascicoloModel.getIdFascicoloSiep());

		// Ricerca id template
		String lFlagTemplate = "0";
		String lTipoEvento = "01";
		String lTipoProv = "03";

		TemplateModel lModTem = new TemplateModel();
		ITemplate lCtrlTem = SICOLookupRemote.getTemplateRemote();
		lModTem = lCtrlTem.ExRicercaTemplateByTipEveTipoProvCodMotivoFlagTemplate(lTipoEvento, lTipoProv,
				lMotivo, lFlagTemplate);

		lEveMod.setNomeTemplate(lModTem.getIdTemplate());

		ByteArrayOutputStream lReport = lCtrl.ExStampaDocumento(lEveMod, lUtenteMod); // setta la risposta
																						// nella request

		// Prepara la pagina di destinazione

		setRequestAttribute("report", lReport);

		return IWebConstants.PG_DOWNLOAD;
	}

}