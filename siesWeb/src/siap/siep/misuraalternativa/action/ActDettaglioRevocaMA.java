package siap.siep.misuraalternativa.action;

//import per le combo
//import f3b.web.html.Option;
//import siap.sico.decodifiche.controller.DecodificheManager;
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
import siap.siep.notifica.model.NotificaModel;
import siap.siep.penaresidua.model.PenaResiduaModel;
import siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel;
import siap.siep.util.MinorMask;
import f3b.util.F3BException;
import f3b.web.html.Option;

/**
 * <p>
 * Title: ActDettaglioRevocaMA
 * </p>
 * <p>
 * Description: Classe Action per la load dettaglio di Revoca
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

public class ActDettaglioRevocaMA extends ActMisuraAlternativa implements ICostantiMisuraAlternativa {
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

		IEvento lCtrlEvento = SICOLookupRemote.getEventoRemote();

		// ricerca evento notifica
		EventoNotificaModel lEveMod = new EventoNotificaModel();

		lEveMod = lCtrlEvento.ExRicercaEventoNotificaByKey(lIdEvento);
		this.setRequestAttribute("eventonotifica", lEveMod);
		Hashtable lTable = this.ricercaNotifiche(lEveMod.getNotifiche());

		// ricerca misura per il fascicolo
		IMisuraAlternativa lMisAltCtrl = SICOLookupRemote.getMisuraAlternativaRemote();

		MisuraAlternativaModel lDecreto = new MisuraAlternativaModel();

		lDecreto = lMisAltCtrl.ExRicercaMisuraAlternativaByIdEvento(lEveMod.getEvento().getEveIdEvento());
		setRequestAttribute("misuraalternativa", lDecreto);

		if (lDecreto.getCodTipoMisura().equals("0016") || lDecreto.getCodTipoMisura().equals("0087")
				|| lDecreto.getCodTipoMisura().equals("0089") || lDecreto.getCodTipoMisura().equals("0088")
				|| lDecreto.getCodTipoMisura().equals("2270")) {
			setRequestAttribute("tipoMisura", "DETENZIONE");
		}
		if (lDecreto.getCodTipoMisura().equals("0091")) {
			setRequestAttribute("tipoMisura", "SEMILIBERTA");
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

		// Ricerca Magistrato
		IMagistrato lCtrlM = SICOLookupRemote.getMagistratoRemote();
		MagistratoModel lMagi = lCtrlM.ExRicercaMagistratoByCod(lEveMod.getEvento().getCodMagistrato());
		setRequestAttribute("magistrato", lMagi);

		// Sede Ufficio di Sorveglianza
		UfficioModel lUffEmiMod = new UfficioModel();

		IUfficio lCtrlUffEmi = SICOLookupRemote.getUfficioRemote();
		lUffEmiMod = lCtrlUffEmi.getUfficioByKey(lDecreto.getChiaveUfficioFascicoloSius());
		setRequestAttribute("sedeUfficioEmittente", lUffEmiMod);

		// MEV10-s3: aggiunta impostazione attributo nella richiesta
		UtenteModel lUtenteMod = new UtenteModel(
				(UtenteModel) getSessionAttribute(ICostantiSecurity.SESSION_UTENTE_CONNESSO));
		String lCodTipoUfficio = lUtenteMod.getUfficioUtente().getCodTipoUfficio();
		setRequestAttribute("codiceTipoUfficio", lCodTipoUfficio);

		// Avvocato
		// IAvvocato lAvvCtrl = SIEPLookupRemote.getAvvocatoRemote();
		// Vector lAvvocati = lAvvCtrl.ExRicercaAvvocatiByFascicolo(lFascMod.getIdFascicoloSiep());
		// setRequestAttribute("avvocati", lAvvocati);

		// Pena Residua
		/*
		 * REWORK DETTAGLIO IPenaResidua lCtrl = SIEPLookupRemote.getPenaResiduaRemote(); PenaResiduaModel
		 * llPenMod = lCtrl.ExRicercaPenaResiduaCorrenteByFascicoloSiep(lFascMod.getIdFascicoloSiep());
		 */

		PenaResiduaModel llPenMod = this.getPenaResidua(lIdEvento, lFascMod.getIdFascicoloSiep());

		setRequestAttribute("penaresidua", llPenMod);

		// setto il campo codice motivo
		Option lOption = new Option(DecodificheManager.getInstance().getTipoIstituto());
		setRequestAttribute("tipoIstituto", "" + lOption);

		return PG_DETTAGLIO_MA_REVOCA;
	}

}