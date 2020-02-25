package siap.siep.misuraalternativa.action;

import java.math.BigDecimal;
import java.util.Hashtable;
import java.util.Vector;

import f3b.util.F3BException;
import f3b.web.IWebConstants;
import f3b.web.html.Option;
import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.controller.IEventoSimeone;
import siap.sico.evento.model.EventoModel;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.magistratocompetente.controller.IMagistratoCompetente;
import siap.sico.magistratocompetente.model.MagistratoCompetenteMagistratoModel;
import siap.sico.misuraalternativa.controller.IMisuraAlternativa;
import siap.sico.misuraalternativa.model.MisuraAlternativaModel;
import siap.sico.ufficio.controller.IUfficio;
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.util.SICOLookupRemote;
import siap.siep.autoritaesterna.model.AutoritaEsternaModel;
import siap.siep.avvocato.controller.IAvvocato;
import siap.siep.fascicolo.action.ICostantiFascicoloSiep;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.notifica.model.NotificaModel;
import siap.siep.penaresidua.controller.IPenaResidua;
import siap.siep.penaresidua.model.PenaResiduaModel;
import siap.siep.posizione.controller.IPosizioneGiuridica;
import siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel;
import siap.siep.posizione.model.PosizioneGiuridicaModel;
import siap.siep.util.SIEPLookupRemote;
import siap.siep.verbale.controller.IVerbale;
import siap.siep.verbale.model.VerbaleModel;

/**
 * <p>
 * Title: ActLoadInserisciMADetDomTemp
 * </p>
 * <p>
 * Description: Classe Action per la load inserisci di Concessione Detenzione Domiciliare
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

public class ActLoadInserisciMADetDomTemp extends ActMisuraAlternativa implements ICostantiMisuraAlternativa {
	@SuppressWarnings("rawtypes")
	public String processRequest() throws F3BException {
		if (this.isSessionAttributeNullObj("fascicolo"))
			return ICostantiFascicoloSiep.REDIRECT_FASCICOLO_RICERCATO + getClass().getName();

		FascicoloSiepModel lFascMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");

		if (isFascicoloNonValidato())
			return IWebConstants.PG_MESSAGE;

		isFascicoloSiepDiCompetenza();

		if (isFascicoloArchiviatoDefinito())
			return IWebConstants.PG_MESSAGE;

		// Posizione giuridica
		PosizioneGiuridicaLuogoDetenzioneAltraCausaModel lPos = new PosizioneGiuridicaLuogoDetenzioneAltraCausaModel();
		IPosizioneGiuridica lPosCtrl = SIEPLookupRemote.getPosizioneGiuridicaRemote();
		lPos = lPosCtrl.ExRicercaPosizioneGiuridicaLuogoDetenzioneAltraCausaCorrentiByIdFascicolo(
				lFascMod.getIdFascicoloSiep());

		if (notEsistePosizioneGiuridica(lPos))
			return IWebConstants.PG_MESSAGE;

		setRequestAttribute("posizioneluogoaltra", lPos);
		setRequestAttribute("lcodicePosizione", lPos.getPosizioneGiuridica().getCodPosizioneGiuridica());

		// Controllo Esistenza pena residua per quel fascicolo
		PenaResiduaModel lPenaResMod = new PenaResiduaModel();
		IPenaResidua lPenResCtrl = SIEPLookupRemote.getPenaResiduaRemote();
		lPenaResMod = lPenResCtrl.ExRicercaPenaResiduaCorrenteByFascicoloSiep(lFascMod.getIdFascicoloSiep());

		if (this.notEsistePenaResiduaCorrenteByFascicoloSiep(lPenaResMod))
			return IWebConstants.PG_MESSAGE;

		if (lPenaResMod != null && lPenaResMod.getFlagValidato().equals("N"))
			setRequestAttribute("dataeditabile", "S");

		setRequestAttribute("penaresidua", lPenaResMod);

		// posizione Precedente
		PosizioneGiuridicaModel lPosPre = new PosizioneGiuridicaModel();
		IPosizioneGiuridica lCtrPos = SIEPLookupRemote.getPosizioneGiuridicaRemote();
		lPosPre = lCtrPos.ExRicercaPosizioneGiuridicaPrecedenteByIdFascicolo(lFascMod.getIdFascicoloSiep());
		String lFlagAffi = "N";

		if (lPosPre != null && lPosPre.getCodPosizioneGiuridica() != null && lPos != null
				&& lPos.getPosizioneGiuridica() != null
				&& lPos.getPosizioneGiuridica().getCodPosizioneGiuridica() != null && lPosPre.isLibero()
				&& lPos.getPosizioneGiuridica().getCodPosizioneGiuridica().equals("12")) {
			lFlagAffi = "S";
		}

		setRequestAttribute("lFlagAffi", lFlagAffi);

		// ricerca misura per il fascicolo
		MisuraAlternativaModel lMisAlModConcessa = new MisuraAlternativaModel();

		// ricerca evento inserito dal TDS insieme alla misura alternativa
		IEvento lCtrlEvento = SICOLookupRemote.getEventoRemote();
		EventoNotificaModel lEveMod = new EventoNotificaModel();

		if (!this.isRequestParameterNullObj(CAMPO_ID_MISURA_ALTERNATIVA)) {
			BigDecimal lIdMisuraAlternativa = getRequestBigDecimalParameter(CAMPO_ID_MISURA_ALTERNATIVA);
			if (lIdMisuraAlternativa != null) {
				IMisuraAlternativa lMisAltCtrl = SICOLookupRemote.getMisuraAlternativaRemote();
				lMisAlModConcessa = lMisAltCtrl.ExRicercaMisuraAlternativaByKey(lIdMisuraAlternativa);
				setRequestAttribute("misuraalternativa", lMisAlModConcessa);

				// provvedimento
				lEveMod = lCtrlEvento.ExRicercaEventoNotificaByKey(lMisAlModConcessa.getEveIdEvento());

				// verbale
				EventoModel lEveVer = new EventoModel();
				IEventoSimeone lCtrlEven = SICOLookupRemote.getEventoSimeoneRemote();
				lEveVer = lCtrlEven.ExRicercaEventoByEveIdEventoTipoProvCodMotivo(
						lMisAlModConcessa.getEveIdEvento(), "07", "16", "0314");

				// VerbaleModel lVerMod = new VerbaleModel();
				IVerbale lCtrlVe = SIEPLookupRemote.getVerbaleRemote();
				VerbaleModel lVerbMod = lCtrlVe.ExRicercaVerbaleObblighiByIdEvento(lEveVer.getIdEvento());
				setRequestAttribute("verbale", lVerbMod);

				UfficioModel lUffEmiMod = new UfficioModel();
				IUfficio lCtrlUffEmi = SICOLookupRemote.getUfficioRemote();
				lUffEmiMod = lCtrlUffEmi.getUfficioByKey(lMisAlModConcessa.getChiaveUfficioFascicoloSius());
				setRequestAttribute("sedeUfficioEmittente", lUffEmiMod);
			}
		}

		// ricerca evento notifica ordinanza
		Hashtable lTable = new Hashtable();
		if (lEveMod != null && lEveMod.getNotifiche() != null)
			lTable = this.ricercaNotifiche(lEveMod.getNotifiche());

		// ricerca magistrato competente
		IMagistratoCompetente lMagComp = SICOLookupRemote.getMagistratoCompetenteRemote();
		MagistratoCompetenteMagistratoModel lMagMod = lMagComp
				.ExRicercaMagistratoCompetenteByFascicolo(lFascMod.getIdFascicoloSiep());
		if (lMagMod != null)
			setRequestAttribute("magistratocompetente", lMagMod);

		// Avvocato
		// modifica del 12-03-04 da rivedere
		IAvvocato lAvvCtrl = SIEPLookupRemote.getAvvocatoRemote();
		Vector lAvvocati = lAvvCtrl.ExRicercaAvvocatiByFascicolo(lFascMod.getIdFascicoloSiep());
		setRequestAttribute("avvocati", lAvvocati);

		// Pena Residua
		setRequestAttribute("penaresidua", lPenaResMod);

		// Autorità esterna E
		AutoritaEsternaModel lAutE = null;
		if (lTable.get("AutE") != null) {
			lAutE = ((NotificaModel) lTable.get("AutE")).getAutoritaEsterna();
		}
		setRequestAttribute("autoritaEsternaE", lAutE);

		Option lOptionAutoritaE = null;
		if (lAutE != null && lAutE.getCodTipoAutorita() != null) {
			lOptionAutoritaE = new Option(DecodificheManager.getInstance().getTipoAutorita(),
					lAutE.getCodTipoAutorita());
		} else {
			lOptionAutoritaE = new Option(DecodificheManager.getInstance().getTipoAutorita());
		}

		setRequestAttribute("codiceAutoritaE", "" + lOptionAutoritaE);

		// Autorità esterna N
		AutoritaEsternaModel lAutN = null;
		if (lTable.get("AutN") != null) {
			lAutN = ((NotificaModel) lTable.get("AutN")).getAutoritaEsterna();
			setRequestAttribute("autoritaEsternaN", lAutN);
		}

		Option lOptionAutoritaN = new Option(DecodificheManager.getInstance().getTipoAutorita(), "35");
		setRequestAttribute("codiceAutoritaN", "" + lOptionAutoritaN);

		// Autorità esterna C
		AutoritaEsternaModel lAutC = null;
		if (lTable.get("AutC") != null) {
			lAutC = ((NotificaModel) lTable.get("AutC")).getAutoritaEsterna();
		}
		setRequestAttribute("autoritaEsternaC", lAutC);

		Option lOptionAutoritaC = null;
		if (lAutC != null && lAutC.getCodTipoAutorita() != null) {
			lOptionAutoritaC = new Option(DecodificheManager.getInstance().getTipoAutorita(),
					lAutC.getCodTipoAutorita());
		} else {
			lOptionAutoritaC = new Option(DecodificheManager.getInstance().getTipoAutorita());
		}

		setRequestAttribute("codiceAutoritaC", "" + lOptionAutoritaC);

		// Cssa
		String lCssa = null;
		if (lTable.get("NotCssa") != null) {
			lCssa = ((NotificaModel) lTable.get("NotCssa")).getCSSA().getComune() + " "
					+ ((NotificaModel) lTable.get("NotCssa")).getCSSA().getIndirizzo();
			setRequestAttribute("Cssa", lCssa);
			setRequestAttribute("daticssa", ((NotificaModel) lTable.get("NotCssa")).getCSSA());
		}

		// Ufficio TDS
		String UffTDS = null;
		if (lTable.get("UffTDS") != null) {
			UffTDS = ((NotificaModel) lTable.get("UffTDS")).getUfficio().getDescrComune();
			setRequestAttribute("UffTDS", UffTDS);
		}

		// Ufficio UDS
		String UffUDS = null;
		if (lTable.get("UffUDS") != null) {
			UffUDS = ((NotificaModel) lTable.get("UffUDS")).getUfficio().getDescrComune();
			setRequestAttribute("UffUDS", UffUDS);
		}
		// istituto
		String Istituto = null;
		String LuogoIstituto = null;
		if (lTable.get("lNotIstituto") != null) {
			Istituto = ((NotificaModel) lTable.get("lNotIstituto")).getIstitutoDetenzione()
					.getDescrTipoIstituto();
			LuogoIstituto = ((NotificaModel) lTable.get("lNotIstituto")).getIstitutoDetenzione()
					.getDescrComune();

			setRequestAttribute("Istituto", Istituto);
			setRequestAttribute("LuogoIstituto", LuogoIstituto);
		}

		// setto il campo codice motivo
		Option lOption = new Option(DecodificheManager.getInstance().getMotivoProvvedimentoMADetDomTemp());
		setRequestAttribute("motivoProvv", "" + lOption);

		lOption = new Option(DecodificheManager.getInstance().getTipoAutorita(), "22");
		setRequestAttribute("autoritaEsternaAvv", "" + lOption);

		setRequestAttribute("tipoMisura", DIFFERIMENTO_PENA);
		setRequestAttribute("azione", "siap.siep.misuraalternativa.action.ActInserisciMADetDomTemp");
		setRequestAttribute("tipmis", "CONCESSIONE DETENZIONE DOMICILIARE A TERMINE");

		// MEV 10 - filtro sui minorenni
		setRequestAttribute("filtroMinorenni", this.getFiltroMinorenni());

		return PG_LOAD_INSERISCI_MA_DET_DOM_TEMP;
	}
}