package siap.siep.misuraalternativa.action;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
import f3b.web.html.Option;
import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.evento.action.ICostantiEvento;
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
import siap.siep.fascicolo.controller.IFascicoloSiep;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.notifica.model.NotificaModel;
import siap.siep.penaresidua.controller.IPenaResidua;
import siap.siep.penaresidua.model.PenaResiduaModel;
import siap.siep.posizione.controller.IPosizioneGiuridica;
import siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel;
import siap.siep.posizione.model.PosizioneGiuridicaModel;
import siap.siep.sanzionesostitutiva.controller.ISanzioneSostitutiva;
import siap.siep.sanzionesostitutiva.model.SanzioneSostResiduaModel;
import siap.siep.util.SIEPLookupRemote;
import siap.siep.verbale.controller.IVerbale;
import siap.siep.verbale.model.VerbaleModel;

/**
 * ActConcessione - Classe Action per il padre della concessione
 * 
 * @version 1.0
 */
@SuppressWarnings("rawtypes")
public class ActConcessione extends ActMisuraAlternativa implements ICostantiMisuraAlternativa {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public String getConcessione(String aPosizione) throws F3BException {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(getClass().getName() + ".getConcessione: inizio");

		if (isSessionAttributeNullObj("fascicolo"))
			return ICostantiFascicoloSiep.REDIRECT_FASCICOLO_RICERCATO + getClass().getName();

		FascicoloSiepModel lFascMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");
		
		// Ricarico info del Fascicolo SIEP perchè potrebbero essere cambiate per via della modifica della
		// posizioen giuridica
		IFascicoloSiep lFascCtrl = SIEPLookupRemote.getFascicoloSiepRemote();
		lFascMod = lFascCtrl.ExRicercaFascicoloByKey(lFascMod.getIdFascicoloSiep());
		setSessionAttribute("fascicolo", lFascMod);
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

		if (notEsistePenaResiduaCorrenteByFascicoloSiep(lPenaResMod))
			return IWebConstants.PG_MESSAGE;

		if (lPenaResMod != null && lPenaResMod.getFlagValidato().equals("N"))
			setRequestAttribute("dataeditabile", "S");

		setRequestAttribute("penaresidua", lPenaResMod);

		// sanzioni sostitutive
		ISanzioneSostitutiva lSSCtrl = SIEPLookupRemote.getSanzioneSostitutivaRemote();
		SanzioneSostResiduaModel lSSResiduaModel = lSSCtrl.getUltimaSSResidua(lFascMod.getIdFascicoloSiep(),
				"S");

		// Inserisco la SS residua nel model della PR
		if (lSSResiduaModel == null || lSSResiduaModel.getIdSanzioneSostResidua() == null) {
			lSSResiduaModel = lSSCtrl.getUltimaSSResidua(lFascMod.getIdFascicoloSiep(), "N");
		}

		lPenaResMod.setSanzSostResidua(lSSResiduaModel);

		// posizione Precedente
		PosizioneGiuridicaModel lPosPre = new PosizioneGiuridicaModel();
		IPosizioneGiuridica lCtrPos = SIEPLookupRemote.getPosizioneGiuridicaRemote();
		lPosPre = lCtrPos.ExRicercaPosizioneGiuridicaPrecedenteByIdFascicolo(lFascMod.getIdFascicoloSiep());
		String lFlagAffi = "N";
		if (lPosPre != null && lPosPre.getCodPosizioneGiuridica() != null && lPos != null
				&& lPos.getPosizioneGiuridica() != null
				&& lPos.getPosizioneGiuridica().getCodPosizioneGiuridica() != null && lPosPre.isLibero()
				&& lPos.getPosizioneGiuridica().getCodPosizioneGiuridica().equals(aPosizione)) {
			lFlagAffi = "S";
		}

		setRequestAttribute("lFlagAffi", lFlagAffi);

		// ricerca misura per il fascicolo
		IEvento lCtrlEvento = SICOLookupRemote.getEventoRemote();
		EventoNotificaModel lEveMod = new EventoNotificaModel();

		MisuraAlternativaModel lMisAlModConcessa = new MisuraAlternativaModel();
		if (!isRequestParameterNullObj(CAMPO_ID_MISURA_ALTERNATIVA)) {
			BigDecimal lIdMisuraAlternativa = getRequestBigDecimalParameter(CAMPO_ID_MISURA_ALTERNATIVA);
			if (lIdMisuraAlternativa != null) {
				IMisuraAlternativa lMisAltCtrl = SICOLookupRemote.getMisuraAlternativaRemote();
				lMisAlModConcessa = lMisAltCtrl.ExRicercaMisuraAlternativaByKey(lIdMisuraAlternativa);
				setRequestAttribute("misuraalternativa", lMisAlModConcessa);

				// ricerca per il provvedimento e le notifiche relative
				if (!(lMisAlModConcessa.getCodTipoMisura().equals("0001")
						|| lMisAlModConcessa.getCodTipoMisura().equals("0002")
						|| lMisAlModConcessa.getCodTipoMisura().equals("0003"))) {
					lEveMod = lCtrlEvento
							.ExRicercaEventoNotificaByEveIdEvento(lMisAlModConcessa.getEveIdEvento());
					setRequestAttribute("eventonotifica", lEveMod);
				}

				// Data Sottoscrizione Verbale Obblighi
				String lTipoProvvVerbale = null;
				if (lMisAlModConcessa.getCodTipoMisura().equals("0005")
						|| lMisAlModConcessa.getCodTipoMisura().equals("0010")
						|| lMisAlModConcessa.getCodTipoMisura().equals("0013"))
					lTipoProvvVerbale = "16";
				else if (lMisAlModConcessa.getCodTipoMisura().equals("0001")
						|| lMisAlModConcessa.getCodTipoMisura().equals("0002")
						|| lMisAlModConcessa.getCodTipoMisura().equals("0003"))
					lTipoProvvVerbale = "18";
				else if (lMisAlModConcessa.getCodTipoMisura().equals("0004"))
					lTipoProvvVerbale = "16";
				else if (lMisAlModConcessa.getCodTipoMisura().equals("2245"))
					lTipoProvvVerbale = "18";
				// AMNBROSINO -Ammissione Provvisoria Affidamento in Prova
				else if (lMisAlModConcessa.getCodTipoMisura().equals("2006"))
					lTipoProvvVerbale = "18";
				// 03/08/2010 Espiazione Presso Domicilio
				if (lMisAlModConcessa.getCodTipoMisura().equals(ESP_PRESSO_DOM_MOTIVO))
					lTipoProvvVerbale = "16";

				EventoModel lEveVer = new EventoModel();
				IEventoSimeone lCtrlEven = SICOLookupRemote.getEventoSimeoneRemote();
				lEveVer = lCtrlEven.ExRicercaEventoByEveIdEventoTipoProvCodMotivo(
						lMisAlModConcessa.getEveIdEvento(), "07", lTipoProvvVerbale, "0314");

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
			lTable = ricercaNotifiche(lEveMod.getNotifiche());

		// ricerca magistrato competente
		IMagistratoCompetente imc = SICOLookupRemote.getMagistratoCompetenteRemote();
		MagistratoCompetenteMagistratoModel mcmm = imc
				.ExRicercaMagistratoCompetenteByFascicolo(lFascMod.getIdFascicoloSiep());
		if (mcmm != null)
			setRequestAttribute("magistratocompetente", mcmm);

		// Avvocato
		IAvvocato lAvvCtrl = SIEPLookupRemote.getAvvocatoRemote();
		Vector lAvvocati = lAvvCtrl.ExRicercaAvvocatiByFascicolo(lFascMod.getIdFascicoloSiep());
		setRequestAttribute("avvocati", lAvvocati);

		// Autorita' esterna E
		AutoritaEsternaModel lAutE = null;
		if (lTable.get("AutE") != null)
			lAutE = ((NotificaModel) lTable.get("AutE")).getAutoritaEsterna();

		setRequestAttribute("autoritaEsternaE", lAutE);

		Option lOptionAutoritaE = null;
		if (lAutE != null && lAutE.getCodTipoAutorita() != null)
			lOptionAutoritaE = new Option(DecodificheManager.getInstance().getTipoAutorita(),
					lAutE.getCodTipoAutorita());
		else
			lOptionAutoritaE = new Option(DecodificheManager.getInstance().getTipoAutorita());

		setRequestAttribute("codiceAutoritaE", "" + lOptionAutoritaE);

		// Autorita' esterna N
		AutoritaEsternaModel lAutN = null;
		if (lTable.get("AutN") != null) {
			lAutN = ((NotificaModel) lTable.get("AutN")).getAutoritaEsterna();
			setRequestAttribute("autoritaEsternaN", lAutN);
		}
		Option lOptionAutoritaN = new Option(DecodificheManager.getInstance().getTipoAutorita());
		setRequestAttribute("codiceAutoritaN", "" + lOptionAutoritaN);

		// Autorita' esterna C
		AutoritaEsternaModel lAutC = null;
		if (lTable.get("AutC") != null)
			lAutC = ((NotificaModel) lTable.get("AutC")).getAutoritaEsterna();

		setRequestAttribute("autoritaEsternaC", lAutC);

		Option lOptionAutoritaC = null;
		if (lAutC != null && lAutC.getCodTipoAutorita() != null)
			lOptionAutoritaC = new Option(DecodificheManager.getInstance().getTipoAutorita(),
					lAutC.getCodTipoAutorita());
		else
			lOptionAutoritaC = new Option(DecodificheManager.getInstance().getTipoAutorita());
		setRequestAttribute("codiceAutoritaC", "" + lOptionAutoritaC);

		// Cssa
		if (lTable.get("NotCssa") != null) {
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

		Option lOption = new Option(DecodificheManager.getInstance().getTipoAutorita(), "22");
		setRequestAttribute("autoritaEsternaAvv", "" + lOption);

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(getClass().getName() + ".getConcessione: fine");

		return "";
	}

	protected EventoNotificaModel getProvvedimentoMotivoAffidamento(String aFlagSan, String aPosizione,
			String aFlagAffi, String aTipo, String aMotivo, String IdEventoAmmProvvAff) throws F3BException {
		EventoNotificaModel lEve = new EventoNotificaModel();

		switch (Integer.parseInt(aPosizione)) {
		case 7: // LIBERO
		case 10: // LIBERO
		case 16: // LIBERO IN DIFFERIMENTO PENA
		case 17: // LIBERO IN DIFFERIMENTO PENA (PROVVISORIA)
		case 20: // EVASO
		case 26: // ESPULSO
		case 30: // ESTRADATO
		case 46: // LIBERO in Sospensione
		case 47: // LIBERO in Sospensione
		{
			lEve.getEvento().setCodTipoProvvedimento("26");
			if ("0001".equals(aMotivo))
				lEve.getEvento().setCodMotivo("0370");
			else if ("0002".equals(aMotivo))
				lEve.getEvento().setCodMotivo("0244");
			else if ("0003".equals(aMotivo))
				lEve.getEvento().setCodMotivo("0245");

			break;
		}
		case 3: // Espiazione Pena in Regime Carcerario
		case 4: // Arresti Domiciliari Ex Art. 656/10
		case 12: // Espiazione Pena in Regime di Detenzione Domiciliare
			// paolo cherubini 15/02/2011 su indicazione ti testa/alfieri 50, 53 sono equiparabili a 04
			// arresti domiciliari art. 656
		case 50: // Esecuzione presso domicilio della pena detentiva
		case 53: // Arresti domiciliari - Esecuzione presso domicilio della pena detentiva
		case 14: // Espiazione Pena in Regime di Semiliberta'
		case 29: // Detenzione Domiciliare Provvisoria
		case 82: // Permanenza in Casa ex art. 656/10 - ex art. 656 comma 10 cpp
		case 83: // Collocamento in Comunita' ex art. 656 comma 10 cpp
		case 84: // Arresti Domiciliare ex art 89 dpr 309/90 - ex art. 656 comma 10 cpp
		case 85: // Permanenza domiciliare - Esecuzione presso domicilio della pena detentiva
		case 86: // Collocamento in comunita' - Esecuzione presso domicilio della pena detentiva
		case 87: // Arresti domiciliare ex art. 89 dpr 309/90 - Esecuzione presso domicilio della pena
					// detentiva
		{
			if ("0001".equals(aMotivo))
				lEve.getEvento().setCodMotivo("0371");
			else if ("0002".equals(aMotivo))
				lEve.getEvento().setCodMotivo("0226");
			else if ("0003".equals(aMotivo))
				lEve.getEvento().setCodMotivo("0227");

			if (aTipo.equals("PROC")) // da scarcerare
				lEve.getEvento().setCodTipoProvvedimento("09");
			else if (aTipo.equals("SORV")) // gia' scarcerato
				lEve.getEvento().setCodTipoProvvedimento("12");
			else {
				lEve.getEvento().setCodTipoProvvedimento("04");
				lEve.getEvento().setCodMotivo("0000");
			}

			break;
		}
		default: {
			lEve.getEvento().setCodTipoProvvedimento("04");
			lEve.getEvento().setCodMotivo("0000");
			break;
		}
		}

		// vecchio affidamento in prova provvisorio
		if ((aFlagAffi != null && aFlagAffi.equals("S"))
				|| (aPosizione.equals("13")
						&& (IdEventoAmmProvvAff != null && !IdEventoAmmProvvAff.equals("")))
				|| (aPosizione.equals("54"))) { // affidamento in prova provvisorio
			lEve.getEvento().setCodTipoProvvedimento("12");
			if ("0001".equals(aMotivo))
				lEve.getEvento().setCodMotivo("0371");
			else if ("0002".equals(aMotivo))
				lEve.getEvento().setCodMotivo("0226");
			else if ("0003".equals(aMotivo))
				lEve.getEvento().setCodMotivo("0227");
		}

		if (aPosizione.equals("19") && "S".equals(aFlagSan)) {
			if ("0001".equals(getRequestStringParameter(ICostantiEvento.CAMPO_COD_MOTIVO)))
				lEve.getEvento().setCodMotivo("0607");
			else if ("0002".equals(getRequestStringParameter(ICostantiEvento.CAMPO_COD_MOTIVO)))
				lEve.getEvento().setCodMotivo("0605");
			else if ("0003".equals(getRequestStringParameter(ICostantiEvento.CAMPO_COD_MOTIVO)))
				lEve.getEvento().setCodMotivo("0606");

			if (aTipo.equals("PROC")) // da scarcerare
				lEve.getEvento().setCodTipoProvvedimento("09");
			else if (aTipo.equals("SORV")) // gia' scarcerato
				lEve.getEvento().setCodTipoProvvedimento("12");
		}

		// MEV_2024-092: richiesta valida solo se soggetto libero
		List<String> posizioniLibero = Arrays.asList("07", "10", "16", "17", "20", "26", "30", "46", "47");
		if (posizioniLibero.contains(aPosizione)) {
			// '26' - RICHIESTA
			if ("26".equals(lEve.getEvento().getCodTipoProvvedimento())) {
				if ("0680".equals(aMotivo))
					lEve.getEvento().setCodMotivo("5443");
				else if ("0681".equals(aMotivo))
					lEve.getEvento().setCodMotivo("5444");
				else if ("0690".equals(aMotivo))
					lEve.getEvento().setCodMotivo("5445");
				else if ("0691".equals(aMotivo))
					lEve.getEvento().setCodMotivo("5446");
				else if ("0692".equals(aMotivo))
					lEve.getEvento().setCodMotivo("5447");
			}
		} else {
			if ("0680".equals(aMotivo))
				lEve.getEvento().setCodMotivo("1416");
			else if ("0681".equals(aMotivo))
				lEve.getEvento().setCodMotivo("1417");
			else if ("0690".equals(aMotivo))
				lEve.getEvento().setCodMotivo("1421");
			else if ("0691".equals(aMotivo))
				lEve.getEvento().setCodMotivo("1422");
			else if ("0692".equals(aMotivo))
				lEve.getEvento().setCodMotivo("1423");
		}

		// aggiunto controllo per codici tipo misura - COMUNICAZIONE ('12')
		if ("0720".equals(aMotivo) || "0721".equals(aMotivo) || "0730".equals(aMotivo)
				|| "0731".equals(aMotivo) || "0732".equals(aMotivo))
			lEve.getEvento().setCodTipoProvvedimento("12");

		// model di ritorno
		return lEve;
	}

	protected EventoNotificaModel getProvvedimentoMotivoDetDom(String aPosizione, String aFlagAffi,
			String aTipo, String aMotivo) {
		EventoNotificaModel lEveNot = new EventoNotificaModel();

		if ("0013".equals(aMotivo))
			lEveNot.getEvento().setCodMotivo("0372");
		else if ("0005".equals(aMotivo))
			lEveNot.getEvento().setCodMotivo("0228");
		else if ("0010".equals(aMotivo))
			lEveNot.getEvento().setCodMotivo("0229");

		switch (Integer.parseInt(aPosizione)) {
		case 7: // LIBERO
		case 10: // LIBERO
		case 16: // LIBERO IN DIFFERIMENTO PENA
		case 17: // LIBERO IN DIFFERIMENTO PENA (PROVVISORIA)
		case 20: // EVASO
		case 26: // ESPULSO
		case 30: // ESTRADATO
		case 46: // LIBERO in Sospensione
		case 47: // LIBERO in Sospensione
		{
			lEveNot.getEvento().setCodTipoProvvedimento("06");
			break;
		}
		case 3: // Espiazione Pena in Regime Carcerario
		case 14: // Espiazione Pena in Regime di Semiliberta'
		{
			if (aTipo.equals("PROC")) // da scarcerare
				lEveNot.getEvento().setCodTipoProvvedimento("09");
			else if (aTipo.equals("SORV")) // gia' scarcerato
				lEveNot.getEvento().setCodTipoProvvedimento("12");
			else {
				lEveNot.getEvento().setCodTipoProvvedimento("04");
				if(lEveNot.getEvento().getCodMotivo() == null)
					lEveNot.getEvento().setCodMotivo("0000");
			}

			break;
		}
		case 4: // Arresti Domiciliari Ex Art. 656/10
		case 82: // Permanenza in Casa ex art. 656/10 - ex art. 656 comma 10 cpp
		case 83: // Collocamento in Comunita' ex art. 656 comma 10 cpp
		case 84: // Arresti Domiciliare ex art 89 dpr 309/90 - ex art. 656 comma 10 cpp
		case 85: // Permanenza domiciliare - Esecuzione presso domicilio della pena detentiva
		case 86: // Collocamento in comunita' - Esecuzione presso domicilio della pena detentiva
		case 87: // Arresti domiciliare ex art. 89 dpr 309/90 - Esecuzione presso domicilio della pena
					// detentiva
		{
			if (aTipo.equals("PROC")) // da scarcerare
				lEveNot.getEvento().setCodTipoProvvedimento("06");
			else if (aTipo.equals("SORV")) // gia' scarcerato
				lEveNot.getEvento().setCodTipoProvvedimento("12");
			else {
				lEveNot.getEvento().setCodTipoProvvedimento("04");
				if(lEveNot.getEvento().getCodMotivo() == null)
					lEveNot.getEvento().setCodMotivo("0000");
			}

			break;
		}
		case 29: // Detenzione Domiciliare Provvisoria
			// case 54: // Affidamento in Prova Provvisorio 04/09/2015 TEST MEVxx - Gestione Misure
			// Provvisorie per ora non richiesto e non rilasciato
		{
			lEveNot.getEvento().setCodTipoProvvedimento("12");
			break;
		}
		case 50: // Esecuzione presso il domicilio
		{
			lEveNot.getEvento().setCodTipoProvvedimento("12");
			break;
		}
		default: {
			lEveNot.getEvento().setCodTipoProvvedimento("04");
			if(lEveNot.getEvento().getCodMotivo() == null)
				lEveNot.getEvento().setCodMotivo("0000");
			break;
		}
		}

		// Registrazione data inizio misura
		if (aFlagAffi != null && aFlagAffi.equals("S")) {
			lEveNot.getEvento().setCodTipoProvvedimento("12");
			if ("0013".equals(aMotivo))
				lEveNot.getEvento().setCodMotivo("0372");
			else if ("0005".equals(aMotivo))
				lEveNot.getEvento().setCodMotivo("0228");
			else if ("0010".equals(aMotivo))
				lEveNot.getEvento().setCodMotivo("0229");
		}
		// mev 62
		if (aTipo!=null && aTipo.equals("PROC")) // da scarcerare
			lEveNot.getEvento().setCodTipoProvvedimento("06");
		return lEveNot;
	}

	protected EventoNotificaModel getProvvedimentoMotivoSemiliberta(String aPosizione, String aFlagAffi) {
		EventoNotificaModel lEve = new EventoNotificaModel();
		switch (Integer.parseInt(aPosizione)) {
		case 7: // LIBERO
		case 10: // LIBERO
		case 16: // LIBERO IN DIFFERIMENTO PENA
		case 17: // LIBERO IN DIFFERIMENTO PENA (PROVVISORIA)
		case 20: // EVASO
		case 26: // ESPULSO
		case 30: // ESTRADATO
		case 46: // LIBERO in Sospensione
		case 47: // LIBERO in Sospensione
		{
			lEve.getEvento().setCodTipoProvvedimento("06");
			lEve.getEvento().setCodMotivo("0373");
			break;
		}
		case 3: // Espiazione Pena in Regime Carcerario
		{
			lEve.getEvento().setCodTipoProvvedimento("12");
			lEve.getEvento().setCodMotivo("0373");
			break;
		}
		case 4: // Arresti Domiciliari Ex Art. 656/10
		case 82: // Permanenza in Casa ex art. 656/10 - ex art. 656 comma 10 cpp
		case 83: // Collocamento in Comunita' ex art. 656 comma 10 cpp
		case 84: // Arresti Domiciliare ex art 89 dpr 309/90 - ex art. 656 comma 10 cpp
		case 85: // Permanenza domiciliare - Esecuzione presso domicilio della pena detentiva
		case 86: // Collocamento in comunita' - Esecuzione presso domicilio della pena detentiva
		case 87: // Arresti domiciliare ex art. 89 dpr 309/90 - Esecuzione presso domicilio della pena
					// detentiva
		{
			lEve.getEvento().setCodTipoProvvedimento("06");
			lEve.getEvento().setCodMotivo("0373");
			break;
		}
		default: {
			lEve.getEvento().setCodTipoProvvedimento("04");
			if(lEve.getEvento().getCodMotivo() == null)
				lEve.getEvento().setCodMotivo("0000");			
			break;
		}
		}

		// Registrazione data inizio misura
		if (aFlagAffi != null && aFlagAffi.equals("S")) {
			lEve.getEvento().setCodTipoProvvedimento("09");
			lEve.getEvento().setCodMotivo("0373");
		}
		return lEve;
	}

	protected EventoNotificaModel getProvvedimentoMotivoIndultino(String aPosizione, String aFlagAffi,
			String aTipo, String aMotivo) {

		EventoNotificaModel lEve = new EventoNotificaModel();
		switch (Integer.parseInt(aPosizione)) {
		case 7: // LIBERO
		case 10: // LIBERO
		case 16: // LIBERO IN DIFFERIMENTO PENA
		case 17: // LIBERO IN DIFFERIMENTO PENA (PROVVISORIA)
		case 20: // EVASO
		case 26: // ESPULSO
		case 30: // ESTRADATO
		case 46: // LIBERO in Sospensione
		case 47: // LIBERO in Sospensione
		{
			lEve.getEvento().setCodTipoProvvedimento("06");
			lEve.getEvento().setCodMotivo(aMotivo);
			break;
		}
		case 3: // Espiazione Pena in Regime Carcerario
		case 4: // Arresti Domiciliari Ex Art. 656/10
		case 12: // Espiazione Pena in Regime di Detenzione Domiciliare
		case 14: // Espiazione Pena in Regime di Semiliberta'
		case 50: // DETENZIONE presso domicilio. (24/08/2010)
		case 53: // Arresti Domiciliari - Esecuzione presso domicilio. (17/12/2010)
		case 82: // Permanenza in Casa ex art. 656/10 - ex art. 656 comma 10 cpp
		case 83: // Collocamento in Comunita' ex art. 656 comma 10 cpp
		case 84: // Arresti Domiciliare ex art 89 dpr 309/90 - ex art. 656 comma 10 cpp
		case 85: // Permanenza domiciliare - Esecuzione presso domicilio della pena detentiva
		case 86: // Collocamento in comunita' - Esecuzione presso domicilio della pena detentiva
		case 87: // Arresti domiciliare ex art. 89 dpr 309/90 - Esecuzione presso domicilio della pena
					// detentiva
		{
			if (aTipo.equals("PROC")) // da scarcerare
			{
				lEve.getEvento().setCodTipoProvvedimento("09");
				lEve.getEvento().setCodMotivo(aMotivo);
			} else if (aTipo.equals("SORV")) // gia' scarcerato
			{
				lEve.getEvento().setCodTipoProvvedimento("12");
				lEve.getEvento().setCodMotivo(aMotivo);
			} else {
				lEve.getEvento().setCodTipoProvvedimento("04");
				lEve.getEvento().setCodMotivo("0000");
			}

			break;
		}
		case 29:
		case 54: { // MEV29 - gestisco anche la provenienza dalle provvisorie
			lEve.getEvento().setCodTipoProvvedimento("12");
			lEve.getEvento().setCodMotivo(aMotivo);
			break;
		}
		default: {
			lEve.getEvento().setCodTipoProvvedimento("04");
			lEve.getEvento().setCodMotivo("0000");
			break;
		}
		}

		if (aFlagAffi != null && aFlagAffi.equals("S")) // Registrazione data inizio misura
		{
			lEve.getEvento().setCodTipoProvvedimento("12");
			lEve.getEvento().setCodMotivo(aMotivo);
		}

		return lEve;
	}

	// selezione flag template nella stampa
	protected String getFlagTemplateAffidamento(PosizioneGiuridicaModel aPosPrec, String aPosizioneGiu,
			MisuraAlternativaModel aMisMod, String IdEveAPF) {
		String flagTemplate = null;

		if (aPosizioneGiu.equals("13") && (IdEveAPF != null && !IdEveAPF.equals(""))
				|| aPosizioneGiu.equals("54")) {
			flagTemplate = "A";
		}
		// libero
		else if (aPosPrec != null && aPosPrec.getCodPosizioneGiuridica() != null && aPosPrec.isLibero()
				&& aMisMod.getDataInizioMisura() != null && aPosizioneGiu.equals("13")) {
			flagTemplate = "0";
		} else {
			switch (Integer.parseInt(aPosizioneGiu)) {
			case 7: // LIBERO
			case 10: // LIBERO
			case 16: // LIBERO IN DIFFERIMENTO PENA
			case 17: // LIBERO IN DIFFERIMENTO PENA (PROVVISORIA)
			case 20: // EVASO
			case 26: // ESPULSO
			case 30: // ESTRADATO
			case 46: // LIBERO in Sospensione
			case 47: // LIBERO in Sospensione
			{
				flagTemplate = "1";
				break;
			}
			case 3: // Espiazione Pena in Regime Carcerario
			{
				// da scarcerare
				if (aMisMod != null && aMisMod.getCodTipoUfficioScarcerazione().equals("PROC"))
					flagTemplate = "2";
				// gia' scarcerato
				else if (aMisMod != null && aMisMod.getCodTipoUfficioScarcerazione().equals("SORV"))
					flagTemplate = "6";

				break;
			}
			case 4: // Arresti Domiciliari Ex Art. 656/10
			case 82: // Permanenza in Casa ex art. 656/10 - ex art. 656 comma 10 cpp
			case 83: // Collocamento in Comunita' ex art. 656 comma 10 cpp
			case 84: // Arresti Domiciliare ex art 89 dpr 309/90 - ex art. 656 comma 10 cpp
			case 85: // Permanenza domiciliare - Esecuzione presso domicilio della pena detentiva
			case 86: // Collocamento in comunita' - Esecuzione presso domicilio della pena detentiva
			case 87: // Arresti domiciliare ex art. 89 dpr 309/90 - Esecuzione presso domicilio della pena
						// detentiva
			{
				// da scarcerare
				if (aMisMod != null && aMisMod.getCodTipoUfficioScarcerazione().equals("PROC"))
					flagTemplate = "4";
				// gia' scarcerato
				else if (aMisMod != null && aMisMod.getCodTipoUfficioScarcerazione().equals("SORV"))
					flagTemplate = "8";

				break;
			}
			// 28/02/2011 su indicazione di Testa/Alfieri 50 e 53, vanno gestiti 2 nuovi template (con valori
			// di flagTemplate diversi).
			case 50: // Esecuzione presso domicilio della pena detentiva
			case 53: // Arresti domiciliari - Esecuzione presso domicilio della pena detentiva
			{
				// da scarcerare
				if (aMisMod != null && aMisMod.getCodTipoUfficioScarcerazione().equals("PROC"))
					flagTemplate = "B";
				// gia' scarcerato
				else if (aMisMod != null && aMisMod.getCodTipoUfficioScarcerazione().equals("SORV"))
					flagTemplate = "C";

				break;
			}
			case 12: // Espiazione Pena in Regime di Detenzione Domiciliare
			case 29: // Detenzione Domiciliare Provvisoria
			{
				// da scarcerare
				if (aMisMod != null && aMisMod.getCodTipoUfficioScarcerazione().equals("PROC"))
					flagTemplate = "3";
				// gia' scarcerato
				else if (aMisMod != null && aMisMod.getCodTipoUfficioScarcerazione().equals("SORV"))
					flagTemplate = "7";

				break;
			}
			case 14: // Espiazione Pena in Regime di Semiliberta'
			{
				// da scarcerare
				if (aMisMod != null && aMisMod.getCodTipoUfficioScarcerazione().equals("PROC"))
					flagTemplate = "5";
				// gia' scarcerato
				else if (aMisMod != null && aMisMod.getCodTipoUfficioScarcerazione().equals("SORV"))
					flagTemplate = "9";

				break;
			}
			}
		}
		return flagTemplate;
	}

	protected String getFlagTemplateDetDom(PosizioneGiuridicaModel aPosPrec, String aPosizioneGiu,
			MisuraAlternativaModel aMisMod) {
		String lFlagTemplate = null;
		// libero
		if (aPosPrec != null && aPosPrec.getCodPosizioneGiuridica() != null && aPosPrec.isLibero()
				&& aMisMod.getDataInizioMisura() != null && aPosizioneGiu.equals("12")) {
			lFlagTemplate = "1";
		} else {
			switch (Integer.parseInt(aPosizioneGiu)) {
			case 7: // LIBERO
			case 10: // LIBERO
			case 16: // LIBERO IN DIFFERIMENTO PENA
			case 17: // LIBERO IN DIFFERIMENTO PENA (PROVVISORIA)
			case 20: // EVASO
			case 26: // ESPULSO
			case 30: // ESTRADATO
			case 46: // LIBERO in Sospensione
			case 47: // LIBERO in Sospensione
			{
				lFlagTemplate = "0";
				break;
			}
			case 3: // Espiazione Pena in Regime Carcerario
			{
				// da scarcerare
				if (aMisMod != null && aMisMod.getCodTipoUfficioScarcerazione().equals("PROC"))
					lFlagTemplate = "2";
				// gia' scarcerato
				else if (aMisMod != null && aMisMod.getCodTipoUfficioScarcerazione().equals("SORV"))
					lFlagTemplate = "5";

				break;
			}
			case 4: // Arresti Domiciliari Ex Art. 656/10
			case 82: // Permanenza in Casa ex art. 656/10 - ex art. 656 comma 10 cpp
			case 83: // Collocamento in Comunita' ex art. 656 comma 10 cpp
			case 84: // Arresti Domiciliare ex art 89 dpr 309/90 - ex art. 656 comma 10 cpp
			case 85: // Permanenza domiciliare - Esecuzione presso domicilio della pena detentiva
			case 86: // Collocamento in comunita' - Esecuzione presso domicilio della pena detentiva
			case 87: // Arresti domiciliare ex art. 89 dpr 309/90 - Esecuzione presso domicilio della pena
						// detentiva
			{
				// da scarcerare
				if (aMisMod != null && aMisMod.getCodTipoUfficioScarcerazione().equals("PROC"))
					lFlagTemplate = "4";
				// gia' scarcerato
				else if (aMisMod != null && aMisMod.getCodTipoUfficioScarcerazione().equals("SORV"))
					lFlagTemplate = "7";

				break;
			}
			case 14: // Espiazione Pena in Regime di Semiliberta'
			{
				// da scarcerare
				if (aMisMod != null && aMisMod.getCodTipoUfficioScarcerazione().equals("PROC"))
					lFlagTemplate = "3";
				// gia' scarcerato
				else if (aMisMod != null && aMisMod.getCodTipoUfficioScarcerazione().equals("SORV"))
					lFlagTemplate = "6";

				break;
			}
			case 29: // Detenzione Domiciliare Provvisoria
				// case 54: // Affidamento in Prova Provvisorio 04/09/2015 TEST MEVxx - Gestione Misure
				// Provvisorie per ora non richiesto e non rilasciato
			case 50: {
				lFlagTemplate = "9";
				break;
			}
			}
		}
		// mev 62
		// da scarcerare, quindi il soggetto è detenuto
		if (lFlagTemplate == null && aMisMod != null && aMisMod.getCodTipoUfficioScarcerazione() != null
				&& aMisMod.getCodTipoUfficioScarcerazione().equals("PROC")) {
			lFlagTemplate = "2";
		} else if (lFlagTemplate == null && aMisMod != null
				&& aMisMod.getCodTipoUfficioScarcerazione() != null
				&& aMisMod.getCodTipoUfficioScarcerazione().equals("SORV")) {
			lFlagTemplate = "5";
		}
		return lFlagTemplate;
	}

	protected String getFlagTemplateSemiliberta(PosizioneGiuridicaModel aPosPrec, String aPosizioneGiu,
			MisuraAlternativaModel aMisMod) {
		String FlagTemplate = null;

		// libero
		if (aPosPrec != null && aPosPrec.getCodPosizioneGiuridica() != null && aPosPrec.isLibero()
				&& aMisMod.getDataInizioMisura() != null && aPosizioneGiu.equals("14")) {
			FlagTemplate = "1";
		} else {
			switch (Integer.parseInt(aPosizioneGiu)) {
			case 7: // LIBERO
			case 10: // LIBERO
			case 16: // LIBERO IN DIFFERIMENTO PENA
			case 17: // LIBERO IN DIFFERIMENTO PENA (PROVVISORIA)
			case 20: // EVASO
			case 26: // ESPULSO
			case 30: // ESTRADATO
			case 46: // LIBERO in Sospensione
			case 47: // LIBERO in Sospensione
			{
				FlagTemplate = "0";
				break;
			}
			case 3: // Espiazione Pena in Regime Carcerario
			{
				FlagTemplate = "2";
				break;
			}
			case 4: // Arresti Domiciliari Ex Art. 656/10
			case 82: // Permanenza in Casa ex art. 656/10 - ex art. 656 comma 10 cpp
			case 83: // Collocamento in Comunita' ex art. 656 comma 10 cpp
			case 84: // Arresti Domiciliare ex art 89 dpr 309/90 - ex art. 656 comma 10 cpp
			case 85: // Permanenza domiciliare - Esecuzione presso domicilio della pena detentiva
			case 86: // Collocamento in comunita' - Esecuzione presso domicilio della pena detentiva
			case 87: // Arresti domiciliare ex art. 89 dpr 309/90 - Esecuzione presso domicilio della pena
						// detentiva
			{
				FlagTemplate = "3";
				break;
			}
			}
		}
		return FlagTemplate;
	}

	protected String getFlagTemplateIndultino(PosizioneGiuridicaModel aPosPrec, String aPosizioneGiu,
			MisuraAlternativaModel aMisMod) {
		String flagTemplate = null;
		if (aPosPrec != null && aPosPrec.getCodPosizioneGiuridica() != null && (aPosPrec.isLibero())
				&& aMisMod.getDataInizioMisura() != null && aPosizioneGiu.equals("27")) {
			flagTemplate = "0";
		} else if (((aPosizioneGiu.equals("07") || aPosizioneGiu.equals("10") || aPosizioneGiu.equals("16")
				|| aPosizioneGiu.equals("17") || aPosizioneGiu.equals("46") || aPosizioneGiu.equals("47")
				|| aPosizioneGiu.equals("20") || aPosizioneGiu.equals("26") || aPosizioneGiu.equals("30")))
				&& aMisMod.getDataInizioMisura() == null) {
			flagTemplate = "1";
		} else if ((aPosizioneGiu.equals("03") || aPosizioneGiu.equals("12")
				// paolo cherubini 15/02/2011 su indicazione ti testa/alfieri 50, 53 sono equiparabili a 04
				// arresti domiciliari art. 656
				// 50 Esecuzione presso domicilio della pena detentiva
				// 53 Arresti domiciliari - Esecuzione presso domicilio della pena detentiva
				|| aPosizioneGiu.equals("04") || aPosizioneGiu.equals("50") || aPosizioneGiu.equals("53")
				|| aPosizioneGiu.equals("14") || aPosizioneGiu.equals("82") || aPosizioneGiu.equals("83")
				|| aPosizioneGiu.equals("84") || aPosizioneGiu.equals("85") || aPosizioneGiu.equals("86")
				|| aPosizioneGiu.equals("87")) && aMisMod.getCodTipoUfficioScarcerazione().equals("PROC")) {
			flagTemplate = "2";
		} else if ((aPosizioneGiu.equals("03") || aPosizioneGiu.equals("12")
				// paolo cherubini 15/02/2011 su indicazione ti testa/alfieri 50, 53 sono equiparabili a 04
				// arresti domiciliari art. 656
				// 50 Esecuzione presso domicilio della pena detentiva
				// 53 Arresti domiciliari - Esecuzione presso domicilio della pena detentiva
				|| aPosizioneGiu.equals("04") || aPosizioneGiu.equals("50") || aPosizioneGiu.equals("53")
				|| aPosizioneGiu.equals("14") || aPosizioneGiu.equals("82") || aPosizioneGiu.equals("83")
				|| aPosizioneGiu.equals("84") || aPosizioneGiu.equals("85") || aPosizioneGiu.equals("86")
				|| aPosizioneGiu.equals("87")) && aMisMod.getCodTipoUfficioScarcerazione().equals("SORV")) {
			flagTemplate = "3";
		}
		return flagTemplate;
	}

	protected String getFlagTemplateEspPressoDomicilio(PosizioneGiuridicaModel aPosPrec, String aPosizioneGiu,
			MisuraAlternativaModel aMisMod) {

		String flagTemplate = null;
		if (aPosPrec != null && aPosPrec.getCodPosizioneGiuridica() != null && aPosPrec.isLibero()
				&& aMisMod.getDataInizioMisura() != null && aPosizioneGiu.equals("50")) {
			flagTemplate = "0";
		}
		// 25/02/2011 Casi Templates SIEP_MA_ESECDOM_DETD_ARDOM.rtf o SIEP_MA_ESECDOM_DETD_ARDOM_SCARC.rtf.
		else if (aPosizioneGiu.equals("53") && aPosPrec.getCodPosizioneGiuridica().equals("02")
				&& aMisMod.getCodTipoUfficioScarcerazione().equals("PROC")) {
			flagTemplate = "4";
		} else if (aPosizioneGiu.equals("53") && aPosPrec.getCodPosizioneGiuridica().equals("02")
				&& aMisMod.getCodTipoUfficioScarcerazione().equals("SORV")) {
			flagTemplate = "5";
		} else if ((aPosizioneGiu.equals("07") || aPosizioneGiu.equals("10") || aPosizioneGiu.equals("16")
				|| aPosizioneGiu.equals("17") || aPosizioneGiu.equals("46") || aPosizioneGiu.equals("47")
				|| aPosizioneGiu.equals("20") || aPosizioneGiu.equals("26") || aPosizioneGiu.equals("30"))
				&& aMisMod.getDataInizioMisura() == null) {
			flagTemplate = "1";
		} else if ((aPosizioneGiu.equals("03") || aPosizioneGiu.equals("12")
				// paolo cherubini 15/02/2011 su indicazione ti testa/alfieri 50, 53 sono equiparabili a 04
				// arresti domiciliari art. 656
				// 50 Esecuzione presso domicilio della pena detentiva
				// 53 Arresti domiciliari - Esecuzione presso domicilio della pena detentiva
				|| aPosizioneGiu.equals("04") || aPosizioneGiu.equals("50") || aPosizioneGiu.equals("14")
				|| aPosizioneGiu.equals("53") || aPosizioneGiu.equals("82") || aPosizioneGiu.equals("83")
				|| aPosizioneGiu.equals("84") || aPosizioneGiu.equals("85") || aPosizioneGiu.equals("86")
				|| aPosizioneGiu.equals("87")) // 17/12/2010
				&& aMisMod.getCodTipoUfficioScarcerazione().equals("PROC")) {
			flagTemplate = "2";
		} else if ((aPosizioneGiu.equals("03") || aPosizioneGiu.equals("12")
				// paolo cherubini 15/02/2011 su indicazione ti testa/alfieri 50, 53 sono equiparabili a 04
				// arresti domiciliari art. 656
				// 50 Esecuzione presso domicilio della pena detentiva
				// 53 Arresti domiciliari - Esecuzione presso domicilio della pena detentiva
				|| aPosizioneGiu.equals("04") || aPosizioneGiu.equals("50") || aPosizioneGiu.equals("14")
				|| aPosizioneGiu.equals("53") || aPosizioneGiu.equals("82") || aPosizioneGiu.equals("83")
				|| aPosizioneGiu.equals("84") || aPosizioneGiu.equals("85") || aPosizioneGiu.equals("86")
				|| aPosizioneGiu.equals("87")) // 17/12/2010
				&& aMisMod.getCodTipoUfficioScarcerazione().equals("SORV")) {
			flagTemplate = "3";
		} else if (aPosizioneGiu.equals("29") || aPosizioneGiu.equals("54")) {
			// MEV29 - Aggiunta gestione delle PG in 'Ammissione Provvisoria'
			flagTemplate = "6"; // nuovo
		}

		return flagTemplate;
	}

	protected String getFlagTemplateSospensioneEspPressoDomicilio(PosizioneGiuridicaModel aPosPrec,
			MisuraAlternativaModel aMisMod) {
		String flagTemplate = null;
		if (aPosPrec != null && aPosPrec.getCodPosizioneGiuridica() != null
				&& aMisMod.getDataInizioMisura() != null) {
			if (aMisMod.getCodTipoUfficioScarcerazione().equals("PROC")) {
				flagTemplate = "0";
			} else if (aMisMod.getCodTipoUfficioScarcerazione().equals("SORV")) {
				flagTemplate = "1";
			}
		}
		return flagTemplate;
	}

}