package siap.siep.misuraalternativa.action;

import java.math.BigDecimal;
import java.util.Hashtable;

import siap.sico.decodifiche.controller.DecodificheManager;
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
import siap.siep.autoritaesterna.model.AutoritaEsternaModel;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.istitutodetenzione.model.IstitutoDetenzioneModel;
import siap.siep.notifica.model.NotificaModel;
import siap.siep.penaresidua.model.PenaResiduaModel;
import siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel;
import siap.siep.util.MinorMask;
import f3b.util.F3BException;
import f3b.web.html.Option;

/**
 * <p>
 * Title: ActDettaglioMASospProvv
 * </p>
 * <p>
 * Description: Classe Action per la load dettaglio di Sospensione Provvisoria
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: Bull
 * </p>
 * 
 * @version 1.0
 */

public class ActDettaglioMASospProvv extends ActMisuraAlternativa implements ICostantiMisuraAlternativa {
	@SuppressWarnings("rawtypes")
	public String processRequest() throws F3BException {
		FascicoloSiepModel lFascMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");

		// id dell'evento inserito
		BigDecimal lIdEvento = this.getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO);

		// ricerca posizione giuridica
		// se ritorna dall'UpLoad e flagRitorno = 'S' la posizione giuridica è cambiata

		/*
		 * REWORK DETTAGLIO IPosizioneGiuridica lPosCtrl = SIEPLookupRemote.getPosizioneGiuridicaRemote();
		 * PosizioneGiuridicaLuogoDetenzioneAltraCausaModel lPos = new
		 * PosizioneGiuridicaLuogoDetenzioneAltraCausaModel(); PosizioneGiuridicaModel lPosGiuModificata = new
		 * PosizioneGiuridicaModel(); lPos =
		 * lPosCtrl.ExRicercaPosizioneGiuridicaLuogoDetenzioneAltraCausaCorrentiByIdFascicolo
		 * (lFascMod.getIdFascicoloSiep());
		 */

		PosizioneGiuridicaLuogoDetenzioneAltraCausaModel lPos = this
				.getPosizioneGiuridicaLuogoDetenzioneAltraCausa(lIdEvento, lFascMod.getIdFascicoloSiep());

		setRequestAttribute("posizioneluogoaltra", lPos);

		// ricerca evento notifica
		IEvento lCtrlEvento = SICOLookupRemote.getEventoRemote();
		EventoNotificaModel lEveMod = new EventoNotificaModel();
		lEveMod = lCtrlEvento.ExRicercaEventoNotificaByKey(lIdEvento);
		this.setRequestAttribute("eventonotifica", lEveMod);
		Hashtable lTable = this.ricercaNotifiche(lEveMod.getNotifiche());

		// ricerca misura per il fascicolo
		IMisuraAlternativa lMisAltCtrl = SICOLookupRemote.getMisuraAlternativaRemote();
		MisuraAlternativaModel lDecreto = new MisuraAlternativaModel();
		lDecreto = lMisAltCtrl.ExRicercaMisuraAlternativaByIdEvento(lEveMod.getEvento().getEveIdEvento());
		setRequestAttribute("misuraalternativa", lDecreto);

		if (lDecreto != null && lDecreto.getCodTipoMisura() != null) {
			if (lDecreto.getCodTipoMisura().equals("2149") || lDecreto.getCodTipoMisura().equals("2150")
					|| lDecreto.getCodTipoMisura().equals("2151")
					|| lDecreto.getCodTipoMisura().equals("2293")
					|| lDecreto.getCodTipoMisura().equals("2153")) {
				setRequestAttribute("tipoMisura", "DETENZIONE");
			} else if (lDecreto.getCodTipoMisura().equals("2145")
					|| lDecreto.getCodTipoMisura().equals("2146")
					|| lDecreto.getCodTipoMisura().equals("2147")) {
				setRequestAttribute("tipoMisura", "AFFIDAMENTO");
			} else if (lDecreto.getCodTipoMisura().equals("2148")) {
				setRequestAttribute("tipoMisura", "SEMILIBERTA");
			} else if (lDecreto.getCodTipoMisura().equals("2280")) {
				setRequestAttribute("tipoMisura", "INDULTINO");
			} else if (lDecreto.getCodTipoMisura().equals("2284")
					|| lDecreto.getCodTipoMisura().equals("2285")
					|| lDecreto.getCodTipoMisura().equals("2288")
					|| lDecreto.getCodTipoMisura().equals("2286")
					|| lDecreto.getCodTipoMisura().equals("2287")) {
				setRequestAttribute("tipoMisura", "DETENZIONE51BIS");
			} else if (lDecreto.getCodTipoMisura().equals("2205")
					|| lDecreto.getCodTipoMisura().equals("2281")
					|| lDecreto.getCodTipoMisura().equals("2282")) {
				setRequestAttribute("tipoMisura", "AFFIDAMENTO51BIS");
			} else if (lDecreto.getCodTipoMisura().equals("2283")) {
				setRequestAttribute("tipoMisura", "SEMILIBERTA51BIS");
			} else if (lDecreto.getCodTipoMisura().equals("2290")) {
				setRequestAttribute("tipoMisura", "INDULTINO51BIS");
			}
			// 25/10/2010 Espiazione Presso Domicilio
			else if (lDecreto.getCodTipoMisura()
					.equals(ICostantiMisuraAlternativa.SOSP_ESP_PRESSO_DOM_MOTIVO)) {
				setRequestAttribute("tipoMisura", ICostantiMisuraAlternativa.ESP_PRESSO_DOM);
			} else if (lDecreto.getCodTipoMisura().equals(
					ICostantiMisuraAlternativa.SOSP_ESP_PRESSO_DOM_51BIS_MOTIVO)) {
				setRequestAttribute("tipoMisura", ICostantiMisuraAlternativa.SOSP_ESP_PRESSO_DOM_51BIS);
			}
		}

		IUfficio lCtrlUffEmi = SICOLookupRemote.getUfficioRemote();
		UfficioModel lUffMod = new UfficioModel();
		lUffMod = lCtrlUffEmi.getUfficioByKey(lDecreto.getChiaveUfficioFascicoloSius());
		setRequestAttribute("UfficioEmittente", lUffMod);
		// MEV10-s3: aggiunta impostazione attributo nella richiesta
		UtenteModel lUtenteMod = new UtenteModel(
				(UtenteModel) getSessionAttribute(ICostantiSecurity.SESSION_UTENTE_CONNESSO));
		String lCodTipoUfficio = lUtenteMod.getUfficioUtente().getCodTipoUfficio();
		setRequestAttribute("codiceTipoUfficio", lCodTipoUfficio);

		// istituto
		IstitutoDetenzioneModel lIstMod = null;
		if (lTable.get("lNotIstituto") != null) {
			lIstMod = ((NotificaModel) lTable.get("lNotIstituto")).getIstitutoDetenzione();
			setRequestAttribute("lIstMod", lIstMod);
		}

		// Autorità esterna E
		AutoritaEsternaModel lAutE = null;
		String NoteAutE = null;
		if (lTable.get("AutE") != null) {
			lAutE = ((NotificaModel) lTable.get("AutE")).getAutoritaEsterna();
			NoteAutE = ((NotificaModel) lTable.get("AutE")).getNote();
			setRequestAttribute("NoteAutE", NoteAutE);
			setRequestAttribute("autoritaEsternaE", lAutE);
		}

		// Ufficio TDS
		String UffTDS = null;
		String NoteTDS = null;
		if (lTable.get("UffTDS") != null) {
			NotificaModel nm = (NotificaModel) lTable.get("UffTDS");
			UffTDS = MinorMask.dettaglioTribunale(nm.getUfficio());
			NoteTDS = nm.getNote();
			setRequestAttribute("UffTDS", UffTDS);
			setRequestAttribute("NoteTDS", NoteTDS);
		}

		// Ufficio UDS
		String UffUDS = null;
		String NoteUDS = null;
		if (lTable.get("UffUDS") != null) {
			NotificaModel nm = (NotificaModel) lTable.get("UffUDS");
			UffUDS = MinorMask.dettaglioMagistrato(nm.getUfficio(), lCodTipoUfficio);
			NoteTDS = nm.getNote();
			setRequestAttribute("UffUDS", UffUDS);
			setRequestAttribute("NoteUDS", NoteUDS);
		}

		// Ricerca Magistrato
		IMagistrato lCtrlM = SICOLookupRemote.getMagistratoRemote();
		MagistratoModel lMagi = lCtrlM.ExRicercaMagistratoByCod(lEveMod.getEvento().getCodMagistrato());
		;
		setRequestAttribute("magistrato", lMagi);

		// Sede Ufficio di Sorveglianza
		UfficioModel lUffEmiMod = new UfficioModel();
		lUffEmiMod = lCtrlUffEmi.getUfficioByKey(lDecreto.getChiaveUfficioFascicoloSius());
		setRequestAttribute("sedeUfficioEmittente", lUffEmiMod);

		// Pena Residua
		/*
		 * REWORK DETTAGLIO IPenaResidua lCtrl = SIEPLookupRemote.getPenaResiduaRemote(); PenaResiduaModel
		 * llPenMod = lCtrl.ExRicercaPenaResiduaCorrenteByFascicoloSiep(lFascMod.getIdFascicoloSiep());
		 */

		PenaResiduaModel llPenMod = this.getPenaResidua(lIdEvento, lFascMod.getIdFascicoloSiep());

		setRequestAttribute("penaresidua", llPenMod);

		/*
		 * --- NOn viene utilizzato dalla JSP //ISTITUTO DI DETENZIONE LuogoDetenzioneModel lLuoMod = new
		 * LuogoDetenzioneModel(); ILuogoDetenzione lLuoDetCtrl = SIEPLookupRemote.getLuogoDetenzioneRemote();
		 * lLuoMod = lLuoDetCtrl.ExRicercaLuogoDetByFascicolo(lFascMod.getIdFascicoloSiep());
		 * setRequestAttribute("luogodetenzione", lLuoMod);
		 */
		// setto il campo codice motivo
		Option lOption = new Option(DecodificheManager.getInstance().getTipoIstituto());
		setRequestAttribute("tipoIstituto", "" + lOption);

		if (lIstMod != null)
			setRequestAttribute("notificaE", "istituto");
		if (lAutE != null)
			setRequestAttribute("notificaE", "autorita");

		return PG_DETTAGLIO_MA_SOSP_PROVV;
	}
}