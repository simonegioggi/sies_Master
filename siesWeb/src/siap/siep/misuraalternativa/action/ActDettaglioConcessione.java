package siap.siep.misuraalternativa.action;

import java.math.BigDecimal;
import java.util.Hashtable;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.F3BException;
import f3b.util.Utils;
import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.controller.IEventoSimeone;
import siap.sico.evento.model.EventoModel;
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
import siap.siep.sanzionesostitutiva.controller.ISanzioneSostitutiva;
import siap.siep.sanzionesostitutiva.model.SanzioneSostResiduaModel;
import siap.siep.util.SIEPLookupRemote;
import siap.siep.verbale.controller.IVerbale;
import siap.siep.verbale.model.VerbaleModel;

/**
 * ActDettaglioConcessione - Classe Action per la load dettaglio Concessione
 *
 * @version 1.0
 */
public class ActDettaglioConcessione extends ActMisuraAlternativa implements ICostantiMisuraAlternativa {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	@SuppressWarnings("rawtypes")
	public String processRequest() throws F3BException {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(getClass().getName() + ".processRequest: inizio");

		FascicoloSiepModel lFascMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");

		// id dell'evento inserito
		BigDecimal lIdEvento = getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO);

		// Controllo Esistenza pena residua per quel fascicolo
		/*
		 * REWORK DETTAGLIO PenaResiduaModel lPenaResMod = new PenaResiduaModel(); IPenaResidua lPenResCtrl =
		 * SIEPLookupRemote.getPenaResiduaRemote(); lPenaResMod =
		 * lPenResCtrl.ExRicercaPenaResiduaCorrenteByFascicoloSiep(lFascMod.getIdFascicoloSiep());
		 * setRequestAttribute("penaresidua", lPenaResMod);
		 */
		PenaResiduaModel prm = getPenaResidua(lIdEvento, lFascMod.getIdFascicoloSiep());
		if (Utils.isPresent(prm)) {
			setRequestAttribute("penaresidua", prm);

			// sanzioni sostitutive
			ISanzioneSostitutiva lSSCtrl = SIEPLookupRemote.getSanzioneSostitutivaRemote();
			SanzioneSostResiduaModel lSSResiduaModel = lSSCtrl
					.getUltimaSSResidua(lFascMod.getIdFascicoloSiep(), "S");

			// Inserisco la SS residua nel model della PR
			if (lSSResiduaModel == null || lSSResiduaModel.getIdSanzioneSostResidua() == null)
				lSSResiduaModel = lSSCtrl.getUltimaSSResidua(lFascMod.getIdFascicoloSiep(), "N");

			prm.setSanzSostResidua(lSSResiduaModel);
		}

		// ricerca posizione giuridica
		/*
		 * REWORK DETTAGLIO IPosizioneGiuridica lPosCtrl = SIEPLookupRemote.getPosizioneGiuridicaRemote();
		 * PosizioneGiuridicaLuogoDetenzioneAltraCausaModel lPos = new
		 * PosizioneGiuridicaLuogoDetenzioneAltraCausaModel(); PosizioneGiuridicaModel lPosGiuModificata = new
		 * PosizioneGiuridicaModel(); lPos =
		 * lPosCtrl.ExRicercaPosizioneGiuridicaLuogoDetenzioneAltraCausaCorrentiByIdFascicolo
		 * (lFascMod.getIdFascicoloSiep());
		 */

		PosizioneGiuridicaLuogoDetenzioneAltraCausaModel lPos = getPosizioneGiuridicaLuogoDetenzioneAltraCausa(
				lIdEvento, lFascMod.getIdFascicoloSiep());

		setRequestAttribute("posizioneluogoaltra", lPos);

		// ricerca evento notifica
		IEvento lCtrlEvento = SICOLookupRemote.getEventoRemote();
		EventoNotificaModel lEveMod = new EventoNotificaModel();
		lEveMod = lCtrlEvento.ExRicercaEventoNotificaByKey(lIdEvento);
		setRequestAttribute("eventonotifica", lEveMod);
		Hashtable lTable = ricercaNotifiche(lEveMod.getNotifiche());

		// String lCodMotivo = lEveMod.getEvento().getCodMotivo();

		// ricerca misura per il fascicolo
		IMisuraAlternativa lMisAltCtrl = SICOLookupRemote.getMisuraAlternativaRemote();
		MisuraAlternativaModel lMisAlModConcessa = new MisuraAlternativaModel();
		lMisAlModConcessa = lMisAltCtrl
				.ExRicercaMisuraAlternativaByIdEvento(lEveMod.getEvento().getEveIdEvento());
		setRequestAttribute("misuraalternativa", lMisAlModConcessa);

		String lTipoProvvVerbale = null;

		if (lMisAlModConcessa != null) {
			if (lMisAlModConcessa.getCodTipoMisura().equals("0005")
					|| lMisAlModConcessa.getCodTipoMisura().equals("0010")
					|| lMisAlModConcessa.getCodTipoMisura().equals("0013")
					// MEV_9-SIEP: aggiunti codici tipo misura x DETENZIONE
					|| "0722".equals(lMisAlModConcessa.getCodTipoMisura())
					|| "0733".equals(lMisAlModConcessa.getCodTipoMisura())) {
				setRequestAttribute("tipoMisura", "DETENZIONE");
				lTipoProvvVerbale = "16";
			} else if (lMisAlModConcessa.getCodTipoMisura().equals("0001")
					|| lMisAlModConcessa.getCodTipoMisura().equals("0002")
					|| lMisAlModConcessa.getCodTipoMisura().equals("0003")
					|| lMisAlModConcessa.getCodTipoMisura().equals("0030")
					// MEV_9: aggiunti codici tipo misura
					|| lMisAlModConcessa.getCodTipoMisura().equals("0680")
					|| lMisAlModConcessa.getCodTipoMisura().equals("0681")
					|| lMisAlModConcessa.getCodTipoMisura().equals("0690")
					|| lMisAlModConcessa.getCodTipoMisura().equals("0691")
					|| lMisAlModConcessa.getCodTipoMisura().equals("0692")
					// MEV_9-SIEP: aggiunti codici tipo misura x AFFIDAMENTO
					|| lMisAlModConcessa.getCodTipoMisura().equals("0720")
					|| lMisAlModConcessa.getCodTipoMisura().equals("0721")
					|| lMisAlModConcessa.getCodTipoMisura().equals("0730")
					|| lMisAlModConcessa.getCodTipoMisura().equals("0731")
					|| lMisAlModConcessa.getCodTipoMisura().equals("0732")) {
				setRequestAttribute("tipoMisura", "AFFIDAMENTO");
				lTipoProvvVerbale = "18";
			} else if (lMisAlModConcessa.getCodTipoMisura().equals("0004")
					// MEV_9-SIEP: aggiunti codici tipo misura x SEMILIBERTA'
					|| "0723".equals(lMisAlModConcessa.getCodTipoMisura())
					|| "0734".equals(lMisAlModConcessa.getCodTipoMisura())) {
				setRequestAttribute("tipoMisura", "SEMILIBERTA");
				lTipoProvvVerbale = "16";
			} else if (lMisAlModConcessa.getCodTipoMisura().equals("2245")) {
				setRequestAttribute("tipoMisura", "INDULTINO");
				lTipoProvvVerbale = "18";
			} else if (lMisAlModConcessa.getCodTipoMisura().equals("2630")
					|| lMisAlModConcessa.getCodTipoMisura().equals("0610")) {
				setRequestAttribute("tipoMisura", "ESP_PRESSO_DOM");
				lTipoProvvVerbale = "18";
			}
		}

		// Data Sottoscrizione Verbale Obblighi
		if (lMisAlModConcessa != null) {
			EventoModel lEveVer = new EventoModel();
			IEventoSimeone lCtrlEven = SICOLookupRemote.getEventoSimeoneRemote();
			lEveVer = lCtrlEven.ExRicercaEventoByEveIdEventoTipoProvCodMotivo(
					lMisAlModConcessa.getEveIdEvento(), "07", lTipoProvvVerbale, "0314");
			IVerbale lCtrlVe = SIEPLookupRemote.getVerbaleRemote();
			VerbaleModel lVerbMod = lCtrlVe.ExRicercaVerbaleObblighiByIdEvento(lEveVer.getIdEvento());
			setRequestAttribute("verbale", lVerbMod);
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

		// Autorità esterna C
		AutoritaEsternaModel lAutC = null;
		String NoteAutC = null;

		if (lTable.get("AutC") != null) {
			lAutC = ((NotificaModel) lTable.get("AutC")).getAutoritaEsterna();
			NoteAutC = ((NotificaModel) lTable.get("AutC")).getNote();
			setRequestAttribute("NoteAutC", NoteAutC);
			setRequestAttribute("autoritaEsternaC", lAutC);
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

		if (lMisAlModConcessa != null) {
			// Sede Ufficio di Sorveglianza
			UfficioModel lUffEmiMod = new UfficioModel();
			IUfficio lCtrlUffEmi = SICOLookupRemote.getUfficioRemote();
			lUffEmiMod = lCtrlUffEmi.getUfficioByKey(lMisAlModConcessa.getChiaveUfficioFascicoloSius());
			setRequestAttribute("sedeUfficioEmittente", lUffEmiMod);
			// MEV10-s3: aggiunta impostazione attributo nella richiesta
			UtenteModel lUtenteMod = new UtenteModel(
					(UtenteModel) getSessionAttribute(ICostantiSecurity.SESSION_UTENTE_CONNESSO));
			String lCodTipoUfficio = lUtenteMod.getUfficioUtente().getCodTipoUfficio();
			setRequestAttribute("codiceTipoUfficio", lCodTipoUfficio);
		}
		// Avvocato
		// IAvvocato lAvvCtrl = SIEPLookupRemote.getAvvocatoRemote();
		// Vector lAvvocati = lAvvCtrl.ExRicercaAvvocatiByFascicolo(lFascMod.getIdFascicoloSiep());
		// setRequestAttribute("avvocati", lAvvocati);

		// REWORK----
		/*
		 * ISTITUTO DI DETENZIONE LuogoDetenzioneModel lLuoMod = new LuogoDetenzioneModel(); ILuogoDetenzione
		 * lLuoDetCtrl = SIEPLookupRemote.getLuogoDetenzioneRemote(); lLuoMod =
		 * lLuoDetCtrl.ExRicercaLuogoDetByFascicolo(lFascMod.getIdFascicoloSiep());
		 * setRequestAttribute("luogodetenzione", lLuoMod);
		 */
		// REWORK----
		// Cerca gli Eventi "validati" con motivo "2006"
		// Instanzia il model dell'evento
		EventoModel lEvent = new EventoModel();

		// prende dalla Session l'ID del procedimento e lo carica nel model
		lEvent.setFasSieIdFascicoloSiep(
				((FascicoloSiepModel) getSessionAttribute("fascicolo")).getIdFascicoloSiep());
		// carica nel model il Tipo evento
		lEvent.setCodTipoEvento("01");
		// carica nel model il flag documento registrato
		lEvent.setFlagDocumentoRegistrato("S");
		// carica nel model il tipo motivo
		String[] lMotivo = { "2006" };
		// carica nel model il codice dei provvedimenti
		String[] lProvv = { "04", "09", "12" };

		// Ricerca nella tabella EVENTO
		IEvento lCtrlEventoAPA = SICOLookupRemote.getEventoRemote();
		EventoModel lEve = lCtrlEventoAPA.ExRicercaEventoPerMotivoPerProvv(lMotivo, lProvv, lEvent);

		// setta la risposta della ricerca nella request
		setRequestAttribute("eventoammissioneprovvisoriaaffidamento", lEve);

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(getClass().getName() + ".processRequest: fine");

		// pagina di ritorno
		return PG_LOAD_DETTAGLIO_MA_CONCESSIONE;
	}

}