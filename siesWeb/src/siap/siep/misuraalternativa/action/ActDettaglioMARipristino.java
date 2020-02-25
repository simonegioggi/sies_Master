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
 * Title: ActDettaglioMARipristino
 * </p>
 * <p>
 * Description: Classe Action per la load dettaglio di Ripristino
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

public class ActDettaglioMARipristino extends ActMisuraAlternativa implements ICostantiMisuraAlternativa {
	@SuppressWarnings("rawtypes")
	public String processRequest() throws F3BException {
		FascicoloSiepModel lFascMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");

		// id dell'evento inserito
		BigDecimal lIdEvento = this.getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO);

		// ricerca evento notifica
		IEvento lCtrlEvento = SICOLookupRemote.getEventoRemote();
		EventoNotificaModel lEveMod = new EventoNotificaModel();
		lEveMod = lCtrlEvento.ExRicercaEventoNotificaByKey(lIdEvento);
		this.setRequestAttribute("eventonotifica", lEveMod);
		Hashtable lTable = this.ricercaNotifiche(lEveMod.getNotifiche());

		// ricerca misura per il fascicolo
		IMisuraAlternativa lMisAltCtrl = SICOLookupRemote.getMisuraAlternativaRemote();
		MisuraAlternativaModel lOrd = new MisuraAlternativaModel();

		if (lEveMod.getEvento().getEveIdEvento() != null)
			lOrd = lMisAltCtrl.ExRicercaMisuraAlternativaByIdEvento(lEveMod.getEvento().getEveIdEvento());

		if (lOrd != null)
			setRequestAttribute("misuraalternativa", lOrd);

		// Controllo Esistenza pena residua per quel fascicolo
		/*
		 * REWORK DETTAGLIO PenaResiduaModel lPenaResMod = new PenaResiduaModel(); IPenaResidua lPenResCtrl =
		 * SIEPLookupRemote.getPenaResiduaRemote(); lPenaResMod =
		 * lPenResCtrl.ExRicercaPenaResiduaCorrenteByFascicoloSiep(lFascMod.getIdFascicoloSiep());
		 */

		PenaResiduaModel lPenaResMod = this.getPenaResidua(lIdEvento, lFascMod.getIdFascicoloSiep());
		setRequestAttribute("penaresidua", lPenaResMod);

		// ricerca posizione giuridica
		/*
		 * REWORK DETTAGLIO IPosizioneGiuridica lPosCtrl = SIEPLookupRemote.getPosizioneGiuridicaRemote();
		 * PosizioneGiuridicaLuogoDetenzioneAltraCausaModel lPos = new
		 * PosizioneGiuridicaLuogoDetenzioneAltraCausaModel(); lPos =
		 * lPosCtrl.ExRicercaPosizioneGiuridicaLuogoDetenzioneAltraCausaCorrentiByIdFascicolo
		 * (lFascMod.getIdFascicoloSiep());
		 */
		PosizioneGiuridicaLuogoDetenzioneAltraCausaModel lPos = this
				.getPosizioneGiuridicaLuogoDetenzioneAltraCausa(lIdEvento, lFascMod.getIdFascicoloSiep());
		setRequestAttribute("posizioneluogoaltra", lPos);

		if (lOrd.getCodTipoMisura().equals("0016") || lOrd.getCodTipoMisura().equals("0087")
				|| lOrd.getCodTipoMisura().equals("0089") || lOrd.getCodTipoMisura().equals("0088"))
			setRequestAttribute("tipoMisura", "DETENZIONE");
		else if (lOrd.getCodTipoMisura().equals("0014") || lOrd.getCodTipoMisura().equals("0015")
				|| lOrd.getCodTipoMisura().equals("0086"))
			setRequestAttribute("tipoMisura", "AFFIDAMENTO");
		else if (lOrd.getCodTipoMisura().equals("0091"))
			setRequestAttribute("tipoMisura", "SEMILIBERTA");
		else if (lOrd.getCodTipoMisura().equals("0196"))
			setRequestAttribute("tipoMisura", "INDULTINO");
		else if (lOrd.getCodTipoMisura().equals(ICostantiMisuraAlternativa.ESP_PRESSO_DOM))
			setRequestAttribute("tipoMisura", ICostantiMisuraAlternativa.ESP_PRESSO_DOM);

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

		// Cssa
		String lCssa = null;
		String NoteCssa = null;
		if (lTable.get("NotCssa") != null) {
			lCssa = ((NotificaModel) lTable.get("NotCssa")).getCSSA().getComune() + " "
					+ ((NotificaModel) lTable.get("NotCssa")).getCSSA().getIndirizzo();
			NoteCssa = ((NotificaModel) lTable.get("NotCssa")).getNote();
			setRequestAttribute("NoteCssa", NoteCssa);
			setRequestAttribute("Cssa", lCssa);
			setRequestAttribute("daticssa", ((NotificaModel) lTable.get("NotCssa")).getCSSA());
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

		// MEV10-s3: aggiunta impostazione attributo nella richiesta
		UtenteModel lUtenteMod = new UtenteModel(
				(UtenteModel) getSessionAttribute(ICostantiSecurity.SESSION_UTENTE_CONNESSO));
		String lCodTipoUfficio = lUtenteMod.getUfficioUtente().getCodTipoUfficio();
		setRequestAttribute("codiceTipoUfficio", lCodTipoUfficio);

		// Ufficio UDS
		String UffUDS = null;
		String NoteUDS = null;
		if (lTable.get("UffUDS") != null) {
			NotificaModel nm = (NotificaModel) lTable.get("UffUDS");
			UffUDS = MinorMask.dettaglioMagistrato(nm.getUfficio(), lCodTipoUfficio);
			NoteUDS = nm.getNote();
			setRequestAttribute("UffUDS", UffUDS);
			setRequestAttribute("NoteUDS", NoteUDS);
		}

		// Autorità esterna C
		AutoritaEsternaModel lAutC = null;
		String NoteAutC = null;

		if (lTable.get("AutC") != null) {
			lAutC = ((NotificaModel) lTable.get("AutC")).getAutoritaEsterna();
			NoteAutC = ((NotificaModel) lTable.get("AutC")).getNote();
			setRequestAttribute("NoteAutC", NoteAutC);
			setRequestAttribute("autoritaEsternaC", lAutC);

		}

		// Ricerca Magistrato competente
		IMagistrato lCtrlM = SICOLookupRemote.getMagistratoRemote();
		MagistratoModel lMagi = lCtrlM.ExRicercaMagistratoByCod(lEveMod.getEvento().getCodMagistrato());
		setRequestAttribute("magistrato", lMagi);

		// Sede Ufficio di Sorveglianza
		UfficioModel lUffEmiMod = new UfficioModel();
		IUfficio lCtrlUffEmi = SICOLookupRemote.getUfficioRemote();
		lUffEmiMod = lCtrlUffEmi.getUfficioByKey(lOrd.getChiaveUfficioFascicoloSius());
		setRequestAttribute("sedeUfficioEmittente", lUffEmiMod);

		/*
		 * //ISTITUTO DI DETENZIONE LuogoDetenzioneModel lLuoMod = new LuogoDetenzioneModel();
		 * ILuogoDetenzione lLuoDetCtrl = SIEPLookupRemote.getLuogoDetenzioneRemote(); lLuoMod =
		 * lLuoDetCtrl.ExRicercaLuogoDetByFascicolo(lFascMod.getIdFascicoloSiep());
		 * setRequestAttribute("luogodetenzione", lLuoMod);
		 */
		// setto il campo codice motivo
		Option lOption = new Option(DecodificheManager.getInstance().getTipoIstituto());
		setRequestAttribute("tipoIstituto", "" + lOption);

		if (lIstMod != null)
			setRequestAttribute("notificaE", "istituto");
		if (lAutE != null)
			setRequestAttribute("notificaE", "autorita");

		return PG_DETTAGLIO_MA_RIPRISTINO;
	}
}