package siap.siep.misuraalternativa.action;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Hashtable;
import java.util.Vector;

import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.magistrato.controller.IMagistrato;
import siap.sico.magistrato.model.MagistratoModel;
import siap.sico.misuraalternativa.controller.IMisuraAlternativa;
import siap.sico.misuraalternativa.model.MisuraAlternativaModel;
import siap.sico.ufficio.controller.IUfficio;
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.util.SICOLookupRemote;
import siap.siep.autoritaesterna.model.AutoritaEsternaModel;
import siap.siep.avvocato.controller.IAvvocato;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.notifica.model.NotificaModel;
import siap.siep.penaresidua.controller.IPenaResidua;
import siap.siep.penaresidua.model.PenaResiduaModel;
import siap.siep.posizione.controller.IPosizioneGiuridica;
import siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel;
import siap.siep.util.SIEPLookupRemote;
import f3b.util.DateUtils;
import f3b.util.F3BException;

/**
 * <p>
 * Title: ActDettaglioMADicEffAffInProva
 * </p>
 * <p>
 * Description: Classe Action per la load dettaglio
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
@SuppressWarnings("rawtypes")
public class ActDettaglioMADicEffAffInProva extends ActMisuraAlternativa implements
		ICostantiMisuraAlternativa {

	public String processRequest() throws F3BException {

		FascicoloSiepModel lFascMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");
//		String flagRitorno = null;
//		BigDecimal lIdPosGiuridica;
		// id dell'evento inserito
		BigDecimal lIdEvento = this.getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO);
		// Controllo Esistenza pena residua non validata per quel fascicolo
		PenaResiduaModel lPenaResMod = new PenaResiduaModel();
		IPenaResidua lPenResCtrl = SIEPLookupRemote.getPenaResiduaRemote();
		lPenaResMod = lPenResCtrl.ExRicercaPenaResiduaCorrenteByFascicoloSiep(lFascMod.getIdFascicoloSiep());

		// Pena Residua
		Date lDataInizioPena = null;
		Date lDataFinePenaA = null;
//		Date lDataFinePenaM = null;

		lDataInizioPena = lPenaResMod.getDataInizio();
//		lDataFinePenaM = lPenaResMod.getDataFine();
		lDataFinePenaA = lPenaResMod.getDataFinePresunta();

		setRequestAttribute("StrdataInizioPena", DateUtils.getDateToString(lDataInizioPena, "dd/MM/yyyy"));

		setRequestAttribute("StrdataFinePenaA", DateUtils.getDateToString(lDataFinePenaA, "dd/MM/yyyy"));
		setRequestAttribute("penaresidua", lPenaResMod);

		// ricerca posizione giuridica
		// se ritorna dall'UpLoad e flagRitorno = 'S' la posizione giuridica è cambiata
		IPosizioneGiuridica lPosCtrl = SIEPLookupRemote.getPosizioneGiuridicaRemote();
		PosizioneGiuridicaLuogoDetenzioneAltraCausaModel lPos = new PosizioneGiuridicaLuogoDetenzioneAltraCausaModel();
//		PosizioneGiuridicaModel lPosGiuModificata = new PosizioneGiuridicaModel();
		lPos = lPosCtrl.ExRicercaPosizioneGiuridicaLuogoDetenzioneAltraCausaCorrentiByIdFascicolo(lFascMod
				.getIdFascicoloSiep());

		setRequestAttribute("posizioneluogoaltra", lPos);

		// ricerca evento notifica
		IEvento lCtrlEvento = SICOLookupRemote.getEventoRemote();

		EventoNotificaModel lEveMod = new EventoNotificaModel();

		lEveMod = lCtrlEvento.ExRicercaEventoNotificaByKey(lIdEvento);
		this.setRequestAttribute("eventonotifica", lEveMod);
		Hashtable lTable = this.ricercaNotifiche(lEveMod.getNotifiche());

		IMisuraAlternativa lMisAltCtrl = SICOLookupRemote.getMisuraAlternativaRemote();

		MisuraAlternativaModel lMisAlModConcessa = new MisuraAlternativaModel();
		lMisAlModConcessa = lMisAltCtrl.ExRicercaMisuraAlternativaByIdEvento(lEveMod.getEvento()
				.getEveIdEvento());

		setRequestAttribute("misuraalternativa", lMisAlModConcessa);

		// Autorità esterna E

		AutoritaEsternaModel lAutE = null;
		String NoteAutE = null;
		if (lTable.get("AutE") != null) {
			lAutE = ((NotificaModel) lTable.get("AutE")).getAutoritaEsterna();
			NoteAutE = ((NotificaModel) lTable.get("AutE")).getNote();
			setRequestAttribute("NoteAutE", NoteAutE);
			setRequestAttribute("autoritaEsternaE", lAutE);
		}

		// Autorità esterna N

		AutoritaEsternaModel lAutN = null;
		String NoteAutN = null;

		if (lTable.get("AutN") != null) {
			lAutN = ((NotificaModel) lTable.get("AutN")).getAutoritaEsterna();
			NoteAutN = ((NotificaModel) lTable.get("AutN")).getNote();
			setRequestAttribute("NoteAutN", NoteAutN);
			setRequestAttribute("autoritaEsternaN", lAutN);

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

		// Ufficio TDS

		String UffTDS = null;
		String NoteTDS = null;
		if (lTable.get("UffTDS") != null) {
			UffTDS = ((NotificaModel) lTable.get("UffTDS")).getUfficio().getDescrComune();
			NoteTDS = ((NotificaModel) lTable.get("UffTDS")).getNote();
			setRequestAttribute("UffTDS", UffTDS);
			setRequestAttribute("NoteTDS", NoteTDS);

		}

		// Ufficio UDS

		String UffUDS = null;
		String NoteUDS = null;

		if (lTable.get("UffUDS") != null) {
			UffUDS = ((NotificaModel) lTable.get("UffUDS")).getUfficio().getDescrComune();
			NoteUDS = ((NotificaModel) lTable.get("UffUDS")).getNote();
			setRequestAttribute("UffUDS", UffUDS);
			setRequestAttribute("NoteUDS", NoteUDS);

		}

		// Ricerca Magistrato
		IMagistrato lCtrlM = SICOLookupRemote.getMagistratoRemote();
		MagistratoModel lMagi = lCtrlM.ExRicercaMagistratoByCod(lEveMod.getEvento().getCodMagistrato());

		setRequestAttribute("magistrato", lMagi);

		// Sede Ufficio di Sorveglianza
		UfficioModel lUffEmiMod = new UfficioModel();

		IUfficio lCtrlUffEmi = SICOLookupRemote.getUfficioRemote();
		lUffEmiMod = lCtrlUffEmi.getUfficioByKey(lMisAlModConcessa.getChiaveUfficioFascicoloSius());
		setRequestAttribute("sedeUfficioEmittente", lUffEmiMod);

		// Avvocato
		IAvvocato lAvvCtrl = SIEPLookupRemote.getAvvocatoRemote();
		Vector lAvvocati = lAvvCtrl.ExRicercaAvvocatiByFascicolo(lFascMod.getIdFascicoloSiep());
		setRequestAttribute("avvocati", lAvvocati);

		return PG_DETTAGLIO_MA_DIC_EFF_AFFINPROVA;
	}

}