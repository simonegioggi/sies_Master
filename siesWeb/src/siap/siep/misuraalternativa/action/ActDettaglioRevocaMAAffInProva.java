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
import siap.sico.misuraalternativa.controller.IMisuraAlternativaIndultino;
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
import f3b.util.F3BException;
import f3b.web.html.Option;

/**
 * <p>
 * Title: ActDettaglioRevocaMAAffInProva
 * </p>
 * <p>
 * Description: Classe Action per la load inserisci di Revoca Affidamento in Prova e Indultino
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
public class ActDettaglioRevocaMAAffInProva extends ActMisuraAlternativa implements
		ICostantiMisuraAlternativa {

	@SuppressWarnings("rawtypes")
	public String processRequest() throws F3BException {

		FascicoloSiepModel lFascMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");

		// id dell'evento inserito
		BigDecimal lIdEvento = this.getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO);

		// Esistenza pena residua non validata per quel fascicolo
		/*
		 * REWORK DETTAGLIO PenaResiduaModel lPenaResMod = new PenaResiduaModel(); IPenaResidua lPenResCtrl =
		 * SIEPLookupRemote.getPenaResiduaRemote(); lPenaResMod =
		 * lPenResCtrl.ExRicercaPenaResiduaCorrenteByFascicoloSiep(lFascMod.getIdFascicoloSiep());
		 * setRequestAttribute("nuovapenaresidua", lPenaResMod);
		 */
		PenaResiduaModel llPenMod = this.getPenaResidua(lIdEvento, lFascMod.getIdFascicoloSiep());

		setRequestAttribute("nuovapenaresidua", llPenMod);

		// ---Si deve cercare la pena precedente a quella appena inserita.
		// Oppure nel caso di dettaglio da elenco si deve far riferiemnto alla pena
		// precedente all'evento corrente

		/*
		 * REWORK IPenaResidua lPenResCtrl = SIEPLookupRemote.getPenaResiduaRemote(); PenaResiduaModel
		 * lPenValidata = new PenaResiduaModel(); lPenValidata =
		 * lPenResCtrl.ExRicercaPenaResiduaUltimaValidata(lFascMod.getIdFascicoloSiep());
		 */
		PenaResiduaModel lPenValidata = this.getPenaResiduaPrecedenteValidata(lIdEvento,
				lFascMod.getIdFascicoloSiep());
		setRequestAttribute("penaresidua", lPenValidata);

		// ricerca posizione giuridica
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

		// ricerco la misura precedente per vedere se è una concessione
		IMisuraAlternativaIndultino lMisCtrl = SICOLookupRemote.getMisuraAlternativaRemoteIndultino();
		MisuraAlternativaModel lMisAlModConcessa = lMisCtrl
				.ExRicercaMisuraAlternativaPrecedenteByIdFascicolo(lFascMod.getIdFascicoloSiep());

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

		if (lDecreto != null && lDecreto.getCodTipoMisura() != null
				&& lDecreto.getCodTipoMisura().equals("0196")) {
			setRequestAttribute("tipoMisura", "INDULTINO");

			if (lMisAlModConcessa != null && lMisAlModConcessa.getIdMisuraAlternativa() != null) {
				if (lMisAlModConcessa.getCodTipoDecisione() == null
						|| !lMisAlModConcessa.getCodTipoDecisione().equals("03")
						|| lMisAlModConcessa.getCodNaturaDecisione() == null
						|| !lMisAlModConcessa.getCodNaturaDecisione().equals("CO")
						|| lMisAlModConcessa.getCodTipoMisura() == null
						|| !lMisAlModConcessa.getCodTipoMisura().equals("2245")) {
					lMisAlModConcessa = null;
				}
			}
		}
		// 03/11/2010 Revoca Espiazione Pena presso Domicilio
		else if (lDecreto != null
				&& lDecreto.getCodTipoMisura() != null
				&& (lDecreto.getCodTipoMisura().equals(
						ICostantiMisuraAlternativa.REVOCA_ESP_PRESSO_DOM_MOTIVO) || lDecreto
						.getCodTipoMisura().equals(
								ICostantiMisuraAlternativa.REVOCA_ESP_PRESSO_DOM_MOTIVO_DAUDS))) // 05/10/2011
		{
			setRequestAttribute("tipoMisura", ICostantiMisuraAlternativa.ESP_PRESSO_DOM);

			if (lMisAlModConcessa != null && lMisAlModConcessa.getIdMisuraAlternativa() != null) {
				if (lMisAlModConcessa.getCodTipoDecisione() == null
						|| !lMisAlModConcessa.getCodTipoDecisione().equals("03")
						|| lMisAlModConcessa.getCodNaturaDecisione() == null
						|| !lMisAlModConcessa.getCodNaturaDecisione().equals("CO")
						|| lMisAlModConcessa.getCodTipoMisura() == null
						|| (!lMisAlModConcessa.getCodTipoMisura().equals(
								ICostantiMisuraAlternativa.REVOCA_ESP_PRESSO_DOM_MOTIVO) && !lMisAlModConcessa
								.getCodTipoMisura().equals(
										ICostantiMisuraAlternativa.REVOCA_ESP_PRESSO_DOM_MOTIVO_DAUDS))) // 05/10/2011
				{
					lMisAlModConcessa = null;
				}
			}
		}

		else {
			setRequestAttribute("tipoMisura", "AFFIDAMENTO");

			if (lMisAlModConcessa != null && lMisAlModConcessa.getIdMisuraAlternativa() != null) {
				if (lMisAlModConcessa.getCodTipoDecisione() == null
						|| !lMisAlModConcessa.getCodTipoDecisione().equals("03")
						|| lMisAlModConcessa.getCodNaturaDecisione() == null
						|| !lMisAlModConcessa.getCodNaturaDecisione().equals("CO")
						|| lMisAlModConcessa.getCodTipoMisura() == null
						|| (!lMisAlModConcessa.getCodTipoMisura().equals("0001")
								&& !lMisAlModConcessa.getCodTipoMisura().equals("0002") && !lMisAlModConcessa
								.getCodTipoMisura().equals("0003"))) {
					lMisAlModConcessa = null;
				}
			}
		}

		setRequestAttribute("misuraconcessa", lMisAlModConcessa);

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
		// MEV10-s3: aggiunta variabile e set di attributo nella richiesta
		String descrTipoUfficioTDS = null;
		if (lTable.get("UffTDS") != null) {
			UffTDS = ((NotificaModel) lTable.get("UffTDS")).getUfficio().getDescrComune();
			NoteTDS = ((NotificaModel) lTable.get("UffTDS")).getNote();
			descrTipoUfficioTDS = ((NotificaModel) lTable.get("UffTDS")).getUfficio().getDescrTipoUfficio();
			setRequestAttribute("UffTDS", UffTDS);
			setRequestAttribute("NoteTDS", NoteTDS);
			setRequestAttribute("descrTipoUfficioTDS", descrTipoUfficioTDS);
		}

		// Ufficio UDS
		String UffUDS = null;
		String NoteUDS = null;
		// MEV10-s3: aggiunte variabili e set di attributi nella richiesta
		String descrTipoUfficioUDS = null;
		String codTipoUfficioUDS = null;
		if (lTable.get("UffUDS") != null) {
			UffUDS = ((NotificaModel) lTable.get("UffUDS")).getUfficio().getDescrComune();
			NoteUDS = ((NotificaModel) lTable.get("UffUDS")).getNote();
			descrTipoUfficioUDS = ((NotificaModel) lTable.get("UffUDS")).getUfficio().getDescrTipoUfficio();
			codTipoUfficioUDS = ((NotificaModel) lTable.get("UffUDS")).getUfficio().getCodTipoUfficio();
			setRequestAttribute("UffUDS", UffUDS);
			setRequestAttribute("NoteUDS", NoteUDS);
			setRequestAttribute("descrTipoUfficioUDS", descrTipoUfficioUDS);
			setRequestAttribute("codTipoUfficioUDS", codTipoUfficioUDS);
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

		// setto il campo codice motivo
		Option lOption = new Option(DecodificheManager.getInstance().getTipoIstituto());
		setRequestAttribute("tipoIstituto", "" + lOption);

		return PG_DETTAGLIO_MA_REVOCA_AFF_PROV;
	}

}