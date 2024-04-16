package siap.siep.sospensione.action;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
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
import siap.siep.posizione.controller.IPosizioneGiuridica;
import siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel;
import siap.siep.util.SIEPLookupRemote;

/**
 * ActStampaSospensioneDecisioniSorv - Classe Action per la Stampa Sospensione della pena
 * 
 * @version 1.0
 */
public class ActStampaSospensioneDecisioniSorv extends ActionSiap implements ICostantiSospensione {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public String processRequest() throws F3BException {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(getClass().getName() + ".processRequest: inizio");

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

		IMisuraAlternativa lCtrlMis = SICOLookupRemote.getMisuraAlternativaRemote();
		MisuraAlternativaModel lMisMod = lCtrlMis
				.ExRicercaMisuraAlternativaByIdEvento(lEventoModel.getEveIdEvento());

		PosizioneGiuridicaLuogoDetenzioneAltraCausaModel lPos = new PosizioneGiuridicaLuogoDetenzioneAltraCausaModel();
		IPosizioneGiuridica lPosCtrl = SIEPLookupRemote.getPosizioneGiuridicaRemote();
		lPos = lPosCtrl.ExRicercaPosizioneGiuridicaLuogoDetenzioneAltraCausaCorrentiByIdFascicolo(
				lFascicoloModel.getIdFascicoloSiep());

		// impostare template
		String flagTemplate = null;
		if (lMisMod != null && lMisMod.getCodTipoUfficioScarcerazione() != null) {
			if (lMisMod.getCodTipoUfficioScarcerazione().equals("PROC")) {
				if (lMotivo.equals("0241") && lPos != null && lPos.getPosizioneGiuridica() != null
						&& lPos.getPosizioneGiuridica().getCodPosizioneGiuridica() != null
						&& (lPos.getPosizioneGiuridica().getCodPosizioneGiuridica().equals("02")
								|| lPos.getPosizioneGiuridica().getCodPosizioneGiuridica().equals("04"))) {
					flagTemplate = "2";
				} else {
					flagTemplate = "0";
				}
			} else if (lMisMod.getCodTipoUfficioScarcerazione().equals("SORV")) {
				if (lMotivo.equals("0241") && lPos != null && lPos.getPosizioneGiuridica() != null
						&& lPos.getPosizioneGiuridica().getCodPosizioneGiuridica() != null
						&& (lPos.getPosizioneGiuridica().getCodPosizioneGiuridica().equals("02")
								|| lPos.getPosizioneGiuridica().getCodPosizioneGiuridica().equals("04"))) {
					flagTemplate = "3";
				} else {
					flagTemplate = "1";
				}
			}
		}

		ITemplate lCtrlTem = SICOLookupRemote.getTemplateRemote();
		TemplateModel lTemMod = new TemplateModel();
		lTemMod = lCtrlTem.ExRicercaTemplateByTipEveTipoProvCodMotivoFlagTemplate(
				lEventoModel.getCodTipoEvento(), lEventoModel.getCodTipoProvvedimento(), lMotivo,
				flagTemplate);
		lEveMod.setNomeTemplate(lTemMod.getIdTemplate());

		ByteArrayOutputStream lReport = lCtrl.ExStampaDocumento(lEveMod, lUtenteMod);

		// setta la risposta nella request
		setRequestAttribute("report", lReport);

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(getClass().getName() + ".processRequest: fine");

		// pagina di ritorno
		return IWebConstants.PG_DOWNLOAD;
	}

}