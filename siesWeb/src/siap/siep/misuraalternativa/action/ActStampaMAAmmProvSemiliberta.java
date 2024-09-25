package siap.siep.misuraalternativa.action;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
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
import siap.sico.utente.model.UtenteModel;
import siap.sico.util.SICOLookupRemote;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.posizione.controller.IPosizioneGiuridica;
import siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel;
import siap.siep.posizione.model.PosizioneGiuridicaModel;
import siap.siep.util.SIEPLookupRemote;

/*
 * Funzione di stampa dei provvedimenti di Ammissione Provvisoria alla Semilibertà
 *
 * n.b. si opera sulla falsa riga della stampa della concessione Semilibertà (0004)
 *
 * @since MEV_9-SIEP 03.2024
 */
public class ActStampaMAAmmProvSemiliberta extends ActConcessione implements ICostantiMisuraAlternativa {

	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public String processRequest() throws F3BException {

		FascicoloSiepModel lFascicoloModel = (FascicoloSiepModel) getSessionAttribute("fascicolo");
		UtenteModel lUtenteMod = this.getUtenteConnesso();

		// Recupero la PG corrente e se detenuto AC
		PosizioneGiuridicaLuogoDetenzioneAltraCausaModel lPosAltra = null;
		IPosizioneGiuridica lPosCtrl = SIEPLookupRemote.getPosizioneGiuridicaRemote();
		lPosAltra = lPosCtrl.ExRicercaPosizioneGiuridicaLuogoDetenzioneAltraCausaCorrentiByIdFascicolo(
				lFascicoloModel.getIdFascicoloSiep());

		PosizioneGiuridicaModel lPosGiu = lPosAltra.getPosizioneGiuridica();

		// Se altra causa
		// String lCodTipoPosGiuridicaAltraCausa = "";
		// if (lPosAltra.getAltraCausa() != null &&
		// lPosAltra.getAltraCausa().getCodTipoPosGiuridica() != null ){
		// lCodTipoPosGiuridicaAltraCausa =
		// lPosAltra.getAltraCausa().getCodTipoPosGiuridica();
		// }

		// Recupero l'evento da stampare
		String lIdEvento = getRequestStringParameter(ICostantiEvento.CAMPO_ID_EVENTO);
		IEvento lCtrl = SICOLookupRemote.getEventoRemote();
		EventoModel lEventoModel = lCtrl.ExRicercaEventoByKey(new BigDecimal(lIdEvento));

		// Preparo l'evento per la stampa
		EventoNotificaModel lEveMod = new EventoNotificaModel();

		lEveMod.getEvento().setIdEvento(new BigDecimal(lIdEvento));
		lEveMod.getEvento().setFasSieIdFascicoloSiep(lFascicoloModel.getIdFascicoloSiep());
		lEveMod.getEvento().setDescrLuogoEmittente(this.getUfficioUtenteConnesso().getDescrComune());
		lEveMod.getEvento().setDescrUfficioEmittente(this.getUfficioUtenteConnesso().getDescrTipoUfficio());
		lEveMod.getEvento().setFlagDocumentoRegistrato("N");

		lEveMod.getEvento().setDataAggiornamento(DateUtils.getSysDate());
		lEveMod.getEvento().setCodUfficioAggiornamento(this.getUfficioUtenteConnesso().getCodUfficio());
		lEveMod.getEvento().setCodOperatoreAggiornamento(this.getCodUtenteConnesso());

		// ricerca misura alternativa legata all'evento (decreto/ordinanza) puntato
		// dall'evento oggetto della stampa
		IMisuraAlternativa lMisAltCtrl = SICOLookupRemote.getMisuraAlternativaRemote();
		MisuraAlternativaModel lMisAlModConcessa = new MisuraAlternativaModel();
		lMisAlModConcessa = lMisAltCtrl.ExRicercaMisuraAlternativaByIdEvento(lEventoModel.getEveIdEvento());

		// String lflagScarcerato = lMisAlModConcessa.getCodTipoUfficioScarcerazione();

		// ricerca posizione precedente
		// PosizioneGiuridicaModel lPosPrec = new PosizioneGiuridicaModel();
		/* lPosPrec = */lPosCtrl
				.ExRicercaPosizioneGiuridicaPrecedenteByIdFascicolo(lFascicoloModel.getIdFascicoloSiep());

		// Stabilisco il flagTemplate
		// n.b. si opera sulla falsa riga della stampa della concessione Semilibertà
		// (0004)
		String flagTemplate = null;
		// flagTemplate = getFlagTemplateSemiliberta(lPosPrec, lPosGiu.getCodPosizioneGiuridica(),
		// lMisAlModConcessa);

		String lflagScarcerato = lMisAlModConcessa.getCodTipoUfficioScarcerazione();

		if ("2007".equals(lEventoModel.getCodMotivo())) {
			if (lEventoModel.getCodTipoProvvedimento().equals("09")) {
				// Ordine di scarcerazione a seguito del verbale di arresto/sottoscrizione
				flagTemplate = "1";
			} else if (lPosGiu.isLibero()) {
				flagTemplate = "0";
			} else if (lPosGiu.getCodPosizioneGiuridica().equals("04")
					|| lPosGiu.getCodPosizioneGiuridica().equals("12")
					|| lPosGiu.getCodPosizioneGiuridica().equals("13")
					|| lPosGiu.getCodPosizioneGiuridica().equals("14")
					|| lPosGiu.getCodPosizioneGiuridica().equals("29")
					|| lPosGiu.getCodPosizioneGiuridica().equals("54")) { // Arresti domiciliari e simili
				flagTemplate = "3";
			} else if (lPosGiu.getCodPosizioneGiuridica().equals("03")) {
				// detenuto
				flagTemplate = "2";
			}
		} else {
			// per tutti gli altri codici vale solo chi esegua
			if (lflagScarcerato.equals("PROC"))
				flagTemplate = "0";
			else if (lflagScarcerato.equals("SORV"))
				flagTemplate = "1";
		}

		if (flagTemplate != null && lEventoModel.getCodMotivo() != null
				&& !lEventoModel.getCodMotivo().equals("0000")) {
			ITemplate lCtrlTem = SICOLookupRemote.getTemplateRemote();
			TemplateModel lTemMod = null;
			lTemMod = lCtrlTem.ExRicercaTemplateByTipEveTipoProvCodMotivoFlagTemplate("01", "03",
					lEventoModel.getCodMotivo(), flagTemplate);
			lEveMod.setNomeTemplate(lTemMod.getIdTemplate());

			siesLogger.debug("lTemMod = " + lTemMod);
		} else {
			lEveMod.setNomeTemplate(TEMPLATE_VUOTO);
		}

		ByteArrayOutputStream lReport = lCtrl.ExStampaDocumento(lEveMod, lUtenteMod);

		// Prepara la pagina di destinazione
		setRequestAttribute("report", lReport);

		return IWebConstants.PG_DOWNLOAD;
	}
}
