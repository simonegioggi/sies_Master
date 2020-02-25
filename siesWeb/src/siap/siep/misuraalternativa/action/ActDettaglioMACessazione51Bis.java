package siap.siep.misuraalternativa.action;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.magistrato.controller.IMagistrato;
import siap.sico.magistrato.model.MagistratoModel;
import siap.sico.misuraalternativa.controller.IMisuraAlternativa;
import siap.sico.misuraalternativa.model.MisuraAlternativaModel;
import siap.sico.security.action.ICostantiSecurity;
import siap.sico.ufficio.controller.IUfficio;
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.utente.model.UtenteModel;
import siap.sico.util.SICOLookupRemote;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.notifica.model.NotificaModel;
import siap.siep.penaresidua.model.PenaResiduaModel;
import siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;

/**
 * Action per la visualizzazione del Dettaglio del provvedimento di Cessazione 51bis delle misure altrenative
 * (disposte dal MDS) n.b. visualizza sia il dettaglio del provvedimento NON ancora validato, sia il dettaglio
 * di un provvedimento già validato
 * 
 * @author d.fiorletta
 * @since DL 146/2013
 */
public class ActDettaglioMACessazione51Bis extends ActMisuraAlternativa implements ICostantiMisuraAlternativa {
	public String processRequest() throws F3BException {

		FascicoloSiepModel lFascMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");

		if (!isRequestParameterNullObj("ChgPosMsg")) {
			// Provengo dalla fase di validazione e devo visualizzare il msg di cambio
			// posizione giuridica
			setRequestAttribute(IWebConstants.MESSAGE_TEXT,
					"Attenzione! Il soggetto è in espiazione pena per altra causa"
							+ ", occorre effettuare l'aggiornamento della posizione giuridica"
							+ " ed annotare la data decorrenza futura dalla funzione Notifica Carcere.");

			RedirectTo lRedirigi = new RedirectTo();
			lRedirigi.setPage(IWebConstants.PG_MAIN);
			lRedirigi.setAction("siap.siep.misuraalternativa.action.ActDettaglioMACessazione51Bis" + "&"
					+ ICostantiEvento.CAMPO_ID_EVENTO + "="
					+ getRequestStringParameter(ICostantiEvento.CAMPO_ID_EVENTO));
			setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi);

			return IWebConstants.PG_MESSAGE;
		}

		// ==============================
		// Ricerca evento notifica
		// ==============================
		BigDecimal lIdEvento = this.getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO);
		IEvento lCtrlEvento = SICOLookupRemote.getEventoRemote();
		EventoNotificaModel lEveNotMod = lCtrlEvento.ExRicercaEventoNotificaByKey(lIdEvento);
		this.setRequestAttribute("eventonotifica", lEveNotMod);

		// ================================
		// Recupero la Misura Alternativa (CESSAZIONE)
		// ================================
		IMisuraAlternativa lMisAltCtrl = SICOLookupRemote.getMisuraAlternativaRemote();
		MisuraAlternativaModel lMisuraModel = new MisuraAlternativaModel();
		lMisuraModel = lMisAltCtrl.ExRicercaMisuraAlternativaByIdEvento(lEveNotMod.getEvento()
				.getEveIdEvento());
		setRequestAttribute("misuraalternativa", lMisuraModel);

		UfficioModel lUffEmiMod = new UfficioModel();
		IUfficio lCtrlUffEmi = SICOLookupRemote.getUfficioRemote();
		lUffEmiMod = lCtrlUffEmi.getUfficioByKey(lMisuraModel.getChiaveUfficioFascicoloSius());
		setRequestAttribute("sedeUfficioEmittente", lUffEmiMod);

		// MEV10-s3: aggiunta impostazione attributo nella richiesta
		UtenteModel lUtenteMod = new UtenteModel(
				(UtenteModel) getSessionAttribute(ICostantiSecurity.SESSION_UTENTE_CONNESSO));
		String lCodTipoUfficio = lUtenteMod.getUfficioUtente().getCodTipoUfficio();
		setRequestAttribute("codiceTipoUfficio", lCodTipoUfficio);

		List<String> lCodiciAffidamento = Arrays.asList("2281", "2282", "2205");
		List<String> lCodiciDetenzione = Arrays.asList("2284", "2285", "2286", "2287", "2288");
		List<String> lCodiciSemiliberta = Arrays.asList("2283");
		List<String> lCodiciIndultino = Arrays.asList("2290");
		List<String> lCodiciEspPreDom = Arrays.asList("2299");

		String lTipoCessazione = null;
		if (lCodiciAffidamento.contains(lMisuraModel.getCodTipoMisura())) {
			lTipoCessazione = "AFFIDAMENTO";
		} else if (lCodiciDetenzione.contains(lMisuraModel.getCodTipoMisura())) {
			lTipoCessazione = "DETENZIONE";
		} else if (lCodiciSemiliberta.contains(lMisuraModel.getCodTipoMisura())) {
			lTipoCessazione = "SEMILIBERTA";
		} else if (lCodiciIndultino.contains(lMisuraModel.getCodTipoMisura())) {
			lTipoCessazione = "INDULTINO";
		} else if (lCodiciEspPreDom.contains(lMisuraModel.getCodTipoMisura())) {
			lTipoCessazione = ICostantiMisuraAlternativa.ESP_PRESSO_DOM;
		}

		setRequestAttribute("tipoCessazione", lTipoCessazione);

		// ==============================
		// Pena Residua
		// ==============================
		PenaResiduaModel lPenValidata = this.getPenaResiduaPrecedenteValidata(lIdEvento,
				lFascMod.getIdFascicoloSiep());
		setRequestAttribute("penaresidua", lPenValidata);

		PenaResiduaModel llPenMod = this.getPenaResidua(lIdEvento, lFascMod.getIdFascicoloSiep());
		setRequestAttribute("nuovapenaresidua", llPenMod);

		// ==============================
		// Posizione Giuridica
		// ==============================
		PosizioneGiuridicaLuogoDetenzioneAltraCausaModel lPos = this
				.getPosizioneGiuridicaLuogoDetenzioneAltraCausa(lIdEvento, lFascMod.getIdFascicoloSiep());
		setRequestAttribute("posizioneluogoaltra", lPos);

		// ==========================================================================
		// Ricerco l'eventuale MA di Concessione
		// n.b. essendo un 51bis la vedo grigia trovarla visto che è stata concessa
		// su altro titolo
		// ==========================================================================
		// IMisuraAlternativaIndultino lMisCtrl = SICOLookupRemote.getMisuraAlternativaRemoteIndultino();
		// MisuraAlternativaModel lMisAlModConcessa =
		// lMisCtrl.ExRicercaMisuraAlternativaPrecedenteByIdFascicolo(lFascMod.getIdFascicoloSiep());
		//
		// setRequestAttribute("misuraconcessa", lMisAlModConcessa);

		// ==========================================================================
		//
		// ==========================================================================
		NotificaModel[] lNotifiche = lEveNotMod.getNotifiche();
		for (int i = 0; i < lNotifiche.length; i++) {
			if (lNotifiche[i].getIstDetIdIstitutoDetenzione() != null) {
				setRequestAttribute("notificaIstituto", lNotifiche[i]);
			} else if (lNotifiche[i].getUffCodUfficio() != null) {
				// MEV10-s3: aggiunte casistiche in OR condition
				String codUfficio = lNotifiche[i].getUfficio().getCodTipoUfficio();
				if ("UDS".equals(codUfficio) || "UDSM".equals(codUfficio)) {
					setRequestAttribute("notificaUDS", lNotifiche[i]);
				}
			} else if (lNotifiche[i].getAutoritaEsterna() != null
					&& lNotifiche[i].getAvvIdAvvocatoFascicoloSiep() == null
					&& lNotifiche[i].getCodTipoNotifica().equals("E")) {
				setRequestAttribute("notificaForzeP", lNotifiche[i]);
			}
		}

		// Ricerca Magistrato
		IMagistrato lCtrlM = SICOLookupRemote.getMagistratoRemote();
		MagistratoModel lMagi = lCtrlM.ExRicercaMagistratoByCod(lEveNotMod.getEvento().getCodMagistrato());
		setRequestAttribute("magistrato", lMagi);

		return PG_DETTAGLIO_MA_CESSAZIONE_51BIS;
	}

}