package siap.siep.misuraalternativa.action;

import java.math.BigDecimal;

import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.magistrato.controller.IMagistrato;
import siap.sico.magistrato.model.MagistratoModel;
import siap.sico.misuraalternativa.controller.IMisuraAlternativa;
import siap.sico.misuraalternativa.model.MisuraAlternativaModel;
import siap.sico.security.action.ICostantiSecurity;
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

public class ActDettaglioMAProsecuzione51Bis extends ActMisuraAlternativa implements
		ICostantiMisuraAlternativa {

	public String processRequest() throws F3BException {
		FascicoloSiepModel lFascMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");
		// id dell'evento
		BigDecimal lIdEvento = this.getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO);

		if (!isRequestParameterNullObj("ChgPosMsg")) {
			// Provengo dalla fase di validazione e devo visualizzare il msg di cambio
			// posizione giuridica
			setRequestAttribute(IWebConstants.MESSAGE_TEXT,
					"Attenzione! Il soggetto è in espiazione per altra causa.");

			RedirectTo lRedirigi = new RedirectTo();
			lRedirigi.setPage(IWebConstants.PG_MAIN);
			lRedirigi.setAction("siap.siep.misuraalternativa.action.ActDettaglioMAProsecuzione51Bis" + "&"
					+ ICostantiEvento.CAMPO_ID_EVENTO + "="
					+ getRequestStringParameter(ICostantiEvento.CAMPO_ID_EVENTO));
			setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi);

			return IWebConstants.PG_MESSAGE;
		}

		PenaResiduaModel llPenMod = this.getPenaResidua(lIdEvento, lFascMod.getIdFascicoloSiep());
		setRequestAttribute("nuovapenaresidua", llPenMod);

		PenaResiduaModel lPenValidata = this.getPenaResiduaPrecedenteValidata(lIdEvento,
				lFascMod.getIdFascicoloSiep());
		setRequestAttribute("penaresidua", lPenValidata);

		PosizioneGiuridicaLuogoDetenzioneAltraCausaModel lPos = this
				.getPosizioneGiuridicaLuogoDetenzioneAltraCausa(lIdEvento, lFascMod.getIdFascicoloSiep());
		setRequestAttribute("posizioneluogoaltra", lPos);

		// ricerca evento notifica
		IEvento lCtrlEvento = SICOLookupRemote.getEventoRemote();
		EventoNotificaModel lEveNotMod = new EventoNotificaModel();

		lEveNotMod = lCtrlEvento.ExRicercaEventoNotificaByKey(lIdEvento);
		this.setRequestAttribute("eventonotifica", lEveNotMod);
		String lMotivoEvento = lEveNotMod.getEvento().getCodMotivo();

		// ricerca misura per il fascicolo
		IMisuraAlternativa lMisAltCtrl = SICOLookupRemote.getMisuraAlternativaRemote();
		MisuraAlternativaModel lMisuraModel = new MisuraAlternativaModel();

		lMisuraModel = lMisAltCtrl.ExRicercaMisuraAlternativaByIdEvento(lEveNotMod.getEvento()
				.getEveIdEvento());
		setRequestAttribute("misuraalternativa", lMisuraModel);
		if (lMisuraModel.getCodNaturaDecisione().equals("PC")) // FIXME DL146/2013 verificare il
																// getCodNaturaDecisione
			setRequestAttribute("isConCumulo", "S");
		else
			setRequestAttribute("isConCumulo", "N");

		// ==========================================================================

		if (lMotivoEvento.equals("5470") || lMotivoEvento.equals("5471")
				|| lMotivoEvento.equals("5472") // MDS
				|| lMotivoEvento.equals("5480") || lMotivoEvento.equals("5481")
				|| lMotivoEvento.equals("5482") // TDS
		) {
			setRequestAttribute("tipoMisura", ICostantiMisuraAlternativa.AFFIDAMENTO_IN_PROVA);
		} else if (lMotivoEvento.equals("5473") || lMotivoEvento.equals("5474")
				|| lMotivoEvento.equals("5475")
				|| lMotivoEvento.equals("5476") // MDS
				|| lMotivoEvento.equals("5483") || lMotivoEvento.equals("5484")
				|| lMotivoEvento.equals("5485") || lMotivoEvento.equals("5486") // TDS
		) {
			setRequestAttribute("tipoMisura", ICostantiMisuraAlternativa.DETENZIONE_DOMICILIARE);
		} else if (lMotivoEvento.equals("5477") // MDS
				|| lMotivoEvento.equals("5487") // TDS
		) {
			setRequestAttribute("tipoMisura", ICostantiMisuraAlternativa.SEMILIBERTA);
		} else if (lMotivoEvento.equals("5478") || lMotivoEvento.equals("5489")) {
			setRequestAttribute("tipoMisura", ICostantiMisuraAlternativa.ESP_PRESSO_DOM);
		} else if (lMotivoEvento.equals("5479") // MDS
				|| lMotivoEvento.equals("5490") // TDS
		) {
			setRequestAttribute("tipoMisura", ICostantiMisuraAlternativa.DETENZIONE_DOMICILIARE_TERMINE);
		}

		// Recupero l'ufficio emittente
		UfficioModel lUffEmiMod = getUfficioByCodUfficio(lMisuraModel.getChiaveUfficioFascicoloSius());
		setRequestAttribute("ufficioEmittente", lUffEmiMod);

		// MEV10-s3: aggiunta impostazione attributo nella richiesta
		UtenteModel lUtenteMod = new UtenteModel(
				(UtenteModel) getSessionAttribute(ICostantiSecurity.SESSION_UTENTE_CONNESSO));
		String lCodTipoUfficio = lUtenteMod.getUfficioUtente().getCodTipoUfficio();
		setRequestAttribute("codiceTipoUfficio", lCodTipoUfficio);

		// Recupero l'ufficio emittente
		if (lMisuraModel.getChiaveUfficioFascicoloSiusMaAt() != null
				&& !lMisuraModel.getChiaveUfficioFascicoloSiusMaAt().equals("-")) {
			UfficioModel lUffEmiMAAtMod = getUfficioByCodUfficio(lMisuraModel
					.getChiaveUfficioFascicoloSiusMaAt());
			setRequestAttribute("ufficioEmittenteMaAt", lUffEmiMAAtMod);
		}

		// ==========================================================================
		// Estraggo le notifiche per tipologia e le passo alla jsp
		// ==========================================================================
		NotificaModel[] lNotifiche = lEveNotMod.getNotifiche();
		for (int i = 0; i < lNotifiche.length; i++) {
			if (lNotifiche[i].getIstDetIdIstitutoDetenzione() != null) {
				setRequestAttribute("notificaIstituto", lNotifiche[i]);
			} else if (lNotifiche[i].getCssIdCssa() != null) {
				setRequestAttribute("notificaUEPE", lNotifiche[i]);
			} else if (lNotifiche[i].getUffCodUfficio() != null) {
				// MEV10-s3: aggiunte casistiche in OR condition
				String codUfficio = lNotifiche[i].getUfficio().getCodTipoUfficio();
				if ("UDS".equals(codUfficio) || "UDSM".equals(codUfficio)) {
					setRequestAttribute("notificaUDS", lNotifiche[i]);
				} else if ("TDS".equals(codUfficio) || "TDSM".equals(codUfficio)) {
					setRequestAttribute("notificaTDS", lNotifiche[i]);
				}
			} else if (lNotifiche[i].getAutoritaEsterna() != null
					&& lNotifiche[i].getCodTipoNotifica().equals("C")) {
				setRequestAttribute("notificaForzeP", lNotifiche[i]);
			}
		}

		// Ricerca Magistrato
		IMagistrato lCtrlM = SICOLookupRemote.getMagistratoRemote();
		MagistratoModel lMagi = lCtrlM.ExRicercaMagistratoByCod(lEveNotMod.getEvento().getCodMagistrato());
		setRequestAttribute("magistrato", lMagi);

		return PG_DETTAGLIO_MA_PROSECUZIONE_51_BIS;
	}
}
