package siap.siep.misuraalternativa.action;

import java.math.BigDecimal;
import java.util.Date;

import f3b.util.F3BException;
import f3b.web.IWebConstants;
import siap.sico.calendar.model.CalendarModel;
import siap.sico.cssa.action.ICostantiCSSA;
import siap.sico.decodifiche.model.ComuneModel;
import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.misuraalternativa.controller.IMisuraAlternativa;
import siap.sico.misuraalternativa.model.MisuraAlternativaModel;
import siap.sico.util.SICOLookupRemote;
import siap.siep.calcolopena.controller.ICalcoloPena;
import siap.siep.notifica.model.NotificaModel;
import siap.siep.penaresidua.action.ICostantiPenaResidua;
import siap.siep.penaresidua.model.PenaResiduaModel;
import siap.siep.posizione.action.ICostantiPosizioneGiuridica;
import siap.siep.posizione.model.PosizioneGiuridicaModel;
import siap.siep.util.SIEPLookupRemote;
import siap.sius.depositoordinanzapc.model.DepositoOrdinanzaPcModel;
import siap.sius.tenore.model.TenoreModel;

public class ActInserisciMADetDomTemp extends ActMisuraAlternativa implements ICostantiMisuraAlternativa {

	/**
	 * Azione di Inserimento del MisuraAlternativa concessione detenzione domiciliare
	 *
	 * @return Nome della pagina JSP da visualizzare al termine dell'elaborazione
	 * @throws F3BException
	 */
	public String processRequest() throws F3BException {

		MisuraAlternativaModel lMisMod = new MisuraAlternativaModel();

		EventoNotificaModel lRetModel = new EventoNotificaModel();

		String PosizioneGiu = getRequestStringParameter(
				ICostantiPosizioneGiuridica.CAMPO_COD_POSIZIONE_GIURIDICA);
		setRequestAttribute("posizionegiuridica", PosizioneGiu);
		PosizioneGiuridicaModel lPosMod = new PosizioneGiuridicaModel();
		lPosMod.setCodPosizioneGiuridica(PosizioneGiu);

		// Ticket#202210170122 - SIEP
		// POSIZIONE_GIURIDICA 12 Espiazione Pena in Regime di Detenzione Domiciliare
		// // posizione giuridica precedente
		// PosizioneGiuridicaModel lPosPre = new PosizioneGiuridicaModel();
		// FascicoloSiepModel lFascicoloModel = (FascicoloSiepModel) getSessionAttribute("fascicolo");
		// IPosizioneGiuridica lCtrPos = SIEPLookupRemote.getPosizioneGiuridicaRemote();
		// lPosPre = lCtrPos
		// .ExRicercaPosizioneGiuridicaPrecedenteByIdFascicolo(lFascicoloModel.getIdFascicoloSiep());
		// String lFlagAffi = "N";
		// if (lPosPre != null && lPosPre.getCodPosizioneGiuridica() != null && PosizioneGiu != null
		// && (lPosPre.isLibero()) && (PosizioneGiu.equals("12")))
		// lFlagAffi = "S";
		// Non serve ricalcolarlo sta già calcolato nella JSP
		String lFlagAffi = getRequestStringParameter("lFlagAffi");

		String lPage = null;
		IMisuraAlternativa lMisAltCtrl = SICOLookupRemote.getMisuraAlternativaRemote();
		MisuraAlternativaModel lMisAlModConcessa = null;
		BigDecimal lIdOrdinanza = getRequestBigDecimalParameter(
				ICostantiMisuraAlternativa.CAMPO_ID_DOCUMENTO_SIUS);
		if (lIdOrdinanza != null && !lIdOrdinanza.toString().equals(""))
			lMisAlModConcessa = lMisAltCtrl.ExRicercaMisuraAlternativaByIdEvento(lIdOrdinanza);

		// Calcolo della pena
		BigDecimal lAnniMisura = null;
		BigDecimal lGiorniMisura = null;
		BigDecimal lMesiMisura = null;

		if (!isRequestParameterNullObj(ICostantiMisuraAlternativa.CAMPO_NUM_ANNI_MISURA)
				&& !getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_NUM_ANNI_MISURA).equals(""))
			lAnniMisura = new BigDecimal(
					getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_NUM_ANNI_MISURA));

		if (!isRequestParameterNullObj(ICostantiMisuraAlternativa.CAMPO_NUM_GIORNI_MISURA)
				&& !getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_NUM_GIORNI_MISURA).equals(""))
			lGiorniMisura = new BigDecimal(
					getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_NUM_GIORNI_MISURA));

		if (!isRequestParameterNullObj(ICostantiMisuraAlternativa.CAMPO_NUM_MESI_MISURA)
				&& !getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_NUM_MESI_MISURA).equals(""))
			lMesiMisura = new BigDecimal(
					getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_NUM_MESI_MISURA));

		Date lDataFineMisura = null;
		Date lDataInizio = null;

		// Ticket#202210170122 - SIEP
		// se la posizione precedente è libero il flagAffi vale "S" e quindi non valorizzava la lDataInizio
		// che andava in nullPointer nella chiamata al metodo "exCalcolaNuovaDataFine(...)"
		if (!lPosMod.isLibero() /* && lFlagAffi.equals("N") */) {
			lDataInizio = getRequestDateParameter(ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE,
					ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE, ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE);
		}
		if (lAnniMisura != null || lGiorniMisura != null || lMesiMisura != null) {
			CalendarModel lCalMod = new CalendarModel();
			lCalMod.setNumAnni(lAnniMisura);
			lCalMod.setNumGiorni(lGiorniMisura);
			lCalMod.setNumMesi(lMesiMisura);

			if (PosizioneGiu.equals("03") || PosizioneGiu.equals("14")) {
				if (getRequestStringParameter("tipo").equals("scarcerato")) {
					lDataInizio = getRequestDateParameter(
							ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_SCARCERAZIONE,
							ICostantiMisuraAlternativa.CAMPO_MESE_DATA_SCARCERAZIONE,
							ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_SCARCERAZIONE);
				}
				ICalcoloPena lCtrlCalcolo = SIEPLookupRemote.getCalcoloPenaRemote();
				lDataFineMisura = lCtrlCalcolo.exCalcolaNuovaDataFine(lDataInizio, lCalMod, true);
			} else if (PosizioneGiu.equals("12") || PosizioneGiu.equals("13") || PosizioneGiu.equals("29")
					|| PosizioneGiu.equals("54")) {
				ICalcoloPena lCtrlCalcolo = SIEPLookupRemote.getCalcoloPenaRemote();
				lDataFineMisura = lCtrlCalcolo.exCalcolaNuovaDataFine(lDataInizio, lCalMod, true);
			}
		}

		if (lMisAlModConcessa == null) {
			// INSERISCO EVENTO E NOTIFICA DEL TDS
			EventoNotificaModel lEveMod = new EventoNotificaModel();
			lEveMod.getEvento().setCodMotivo(getRequestStringParameter(ICostantiEvento.CAMPO_COD_MOTIVO));
			String lCodiceUffEmi = getCodUfficioByCodTipoUfficioDescrComune(
					getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_COD_UFFICIO_SORVEGLIANZA),
					getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_SEDE_TDS_EMITT));
			ComuneModel lComModAutEmi = new ComuneModel(getCodComuneByDescr(
					getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_SEDE_TDS_EMITT)));
			Date lDataEmisTras = getRequestDateParameter(ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_DECISIONE,
					ICostantiMisuraAlternativa.CAMPO_MESE_DATA_DECISIONE,
					ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_DECISIONE);
			lEveMod.setEvento(setEventoOrdinanzaDecretoMisuraAlternativa(lEveMod.getEvento(), "03",
					lCodiceUffEmi, lComModAutEmi, lDataEmisTras));

			// setto il deposito ordinanza
			DepositoOrdinanzaPcModel lDepOrdMod = setDepositoOrdinanzaPc(lCodiceUffEmi);
			if (!isRequestParameterNullObj(ICostantiCSSA.CAMPO_ID_CSSA)) {
				lDepOrdMod.setIdCssaComp(getRequestBigDecimalParameter(ICostantiCSSA.CAMPO_ID_CSSA));
			}
			if (!isRequestParameterNullObj(ICostantiMisuraAlternativa.CAMPO_DESCR_LUOGO_PROVA)) {
				lDepOrdMod.setLuogoSvolgimentoProva(
						getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_DESCR_LUOGO_PROVA));
			}

			// setto il tenore
			TenoreModel lTenMod = setTenore(new BigDecimal(1), "0001");

			// misura alternativa
			String lUfficioScar = "-";
			if (!lPosMod.isLibero() && lFlagAffi.equals("N") && !PosizioneGiu.equals("12")
					&& !PosizioneGiu.equals("13") && !PosizioneGiu.equals("29")
					&& !PosizioneGiu.equals("54")) {
				if (getRequestStringParameter("tipo").equals("scarcerato"))
					lUfficioScar = "SORV";
				else if (getRequestStringParameter("tipo").equals("scarcerare"))
					lUfficioScar = "PROC";
			}
			lMisMod = setMisuraAlternativa("03", "DD", lCodiceUffEmi,
					getRequestStringParameter(ICostantiEvento.CAMPO_COD_MOTIVO), lUfficioScar);

			if (!isRequestParameterNullObj(ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_SCARCERAZIONE))
				lMisMod.setDataScarcerazione(
						getRequestDateParameter(ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_SCARCERAZIONE,
								ICostantiMisuraAlternativa.CAMPO_MESE_DATA_SCARCERAZIONE,
								ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_SCARCERAZIONE));

			if (lAnniMisura != null)
				lMisMod.setNumAnniMisura(lAnniMisura);

			if (lMesiMisura != null)
				lMisMod.setNumMesiMisura(lMesiMisura);

			if (lGiorniMisura != null)
				lMisMod.setNumGiorniMisura(lGiorniMisura);

			if (!isRequestParameterNullObj(ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_FINE_MISURA)
					&& getRequestStringParameter(
							ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_FINE_MISURA) != null
					&& !getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_FINE_MISURA)
							.equals("")) {
				lMisMod.setDataFineMisura(
						getRequestDateParameter(ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_FINE_MISURA,
								ICostantiMisuraAlternativa.CAMPO_MESE_DATA_FINE_MISURA,
								ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_FINE_MISURA));
			} else if (lDataFineMisura != null) {
				lMisMod.setDataFineMisura(lDataFineMisura);
			}

			if (lDataInizio != null)
				lMisMod.setDataInizioMisura(lDataInizio);

			if (!isRequestParameterNullObj(ICostantiCSSA.CAMPO_ID_CSSA))
				lMisMod.setCssIdCssa(getRequestBigDecimalParameter(ICostantiCSSA.CAMPO_ID_CSSA));

			if (!isRequestParameterNullObj(ICostantiMisuraAlternativa.CAMPO_DESCR_LUOGO_PROVA))
				lMisMod.setDescrLuogoProva(
						getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_DESCR_LUOGO_PROVA));

			// MEV10-s3: anticipo questo metodo per prevenire errore inserimento dati ufficio UDS / TDS
			NotificaModel[] lNotificheMod = setNotificheMisuraAlternativa();

			IMisuraAlternativa lCtrlMisura = SICOLookupRemote.getMisuraAlternativaRemote();
			MisuraAlternativaModel lMisuraModel = lCtrlMisura
					.ExInserisciMisuraAlternativaEventoNotifica(lEveMod, lDepOrdMod, lTenMod, lMisMod);

			// INSERISCO EVENTO E NOTIFICA DELL'UFFICIO EMITTENTE
			EventoNotificaModel lEveNot = getProvvedimentoMotivo(PosizioneGiu, lFlagAffi,
					getRequestStringParameter(ICostantiEvento.CAMPO_COD_MOTIVO));
			lEveNot.getEvento().setCodEsito("0112");
			lEveNot.setEvento(setEventoProvvedimentoMisuraAlternativa(lEveNot.getEvento()));
			lEveNot.getEvento().setEveIdEvento(lMisuraModel.getEveIdEvento());

			// notifiche
			// NotificaModel[] lNotificheMod = setNotificheMisuraAlternativa();
			lEveNot.setNotifiche(lNotificheMod);

			BigDecimal lIdPenaRes = getRequestBigDecimalParameter(ICostantiPenaResidua.CAMPO_ID_PENA_RESIDUA);
			PenaResiduaModel lPenaRes = new PenaResiduaModel();
			lPenaRes.setIdPenaResidua(lIdPenaRes);
			if (!isRequestParameterNullObj(ICostantiPenaResidua.CAMPO_ANNO_DATA_FINE))
				lPenaRes.setDataFine(getRequestDateParameter(ICostantiPenaResidua.CAMPO_ANNO_DATA_FINE,
						ICostantiPenaResidua.CAMPO_MESE_DATA_FINE,
						ICostantiPenaResidua.CAMPO_GIORNO_DATA_FINE));

			IMisuraAlternativa lCtrlMisuraAlt = SICOLookupRemote.getMisuraAlternativaRemote();
			EventoNotificaModel lEveNotModel = lCtrlMisuraAlt.ExInserisciOModificaMANotifica(lEveNot,
					lPenaRes, null, null);

			lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
					+ "=siap.siep.misuraalternativa.action.ActDettaglioMADetDomTemp&"
					+ ICostantiEvento.CAMPO_ID_EVENTO + "=" + lEveNotModel.getEvento().getIdEvento();
		} else {
			// la misura alternativa esiste
			setRequestAttribute("tipoMisura", "DETENZIONEATEMPO");

			// MEV_62 [EC] 15/05/2018 - INIZIO
			if (lMisAlModConcessa.getCodTipoUfficioScarcerazione() == null)
				lMisAlModConcessa.setCodTipoUfficioScarcerazione("PROC");

			EventoNotificaModel lEve = getProvvedimentoMotivo(PosizioneGiu, lFlagAffi,
					lMisAlModConcessa.getCodTipoMisura());
			lEve.getEvento().setCodEsito("0112");
			lEve.setEvento(setEventoProvvedimentoMisuraAlternativa(lEve.getEvento()));
			lEve.getEvento().setEveIdEvento(lIdOrdinanza);

			// Inserisco l'array di Notifiche nell'Evento
			NotificaModel[] lNotifiche = setNotificheMisuraAlternativa();
			lEve.setNotifiche(lNotifiche);
			IMisuraAlternativa lCtrlMisura = SICOLookupRemote.getMisuraAlternativaRemote();

			BigDecimal lIdPenaRes = getRequestBigDecimalParameter(ICostantiPenaResidua.CAMPO_ID_PENA_RESIDUA);
			PenaResiduaModel lPenaRes = new PenaResiduaModel();
			lPenaRes.setIdPenaResidua(lIdPenaRes);
			if (!isRequestParameterNullObj(ICostantiPenaResidua.CAMPO_ANNO_DATA_FINE))
				lPenaRes.setDataFine(getRequestDateParameter(ICostantiPenaResidua.CAMPO_ANNO_DATA_FINE,
						ICostantiPenaResidua.CAMPO_MESE_DATA_FINE,
						ICostantiPenaResidua.CAMPO_GIORNO_DATA_FINE));

			// Paolo Cherubini 13/02/2012 risolvendo la seguente segnalazione b2/rr/008 mi sono accorto che
			// in caso di misura iscritta da SIUS, se SIEP inserisce la durata della misura questi dati non
			// venivano riportati
			if (lAnniMisura != null)
				lMisAlModConcessa.setNumAnniMisura(lAnniMisura);

			if (lMesiMisura != null)
				lMisAlModConcessa.setNumMesiMisura(lMesiMisura);

			if (lGiorniMisura != null)
				lMisAlModConcessa.setNumGiorniMisura(lGiorniMisura);
			// fine Paolo Cherubini 13/02/2012

			if (lDataFineMisura != null)
				lMisAlModConcessa.setDataFineMisura(lDataFineMisura);

			if (lDataInizio != null)
				lMisAlModConcessa.setDataInizioMisura(lDataInizio);

			if (!lPosMod.isLibero() && lFlagAffi.equals("N") && !PosizioneGiu.equals("12")
					&& !PosizioneGiu.equals("13") && !PosizioneGiu.equals("29")
					&& !PosizioneGiu.equals("54")) {
				if (getRequestStringParameter("tipo").equals("scarcerato")) {
					lMisAlModConcessa.setCodTipoUfficioScarcerazione("SORV");
					lMisAlModConcessa.setDataScarcerazione(
							getRequestDateParameter(ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_SCARCERAZIONE,
									ICostantiMisuraAlternativa.CAMPO_MESE_DATA_SCARCERAZIONE,
									ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_SCARCERAZIONE));
				} else if (getRequestStringParameter("tipo").equals("scarcerare")) {
					lMisAlModConcessa.setCodTipoUfficioScarcerazione("PROC");
				}
			}

			if (!isRequestParameterNullObj(ICostantiMisuraAlternativa.CAMPO_NOTE))
				lMisAlModConcessa.setNote(getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_NOTE));

			lRetModel = lCtrlMisura.ExInserisciOModificaMANotifica(lEve, lPenaRes, lMisAlModConcessa, null);
			lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
					+ "=siap.siep.misuraalternativa.action.ActDettaglioMADetDomTemp&"
					+ ICostantiEvento.CAMPO_ID_EVENTO + "=" + lRetModel.getEvento().getIdEvento();
		}

		return lPage;
	}

	// metodo privato per prendere il codice del provvedimento e quello del motivo
	private EventoNotificaModel getProvvedimentoMotivo(String aPosizioneGiu, String aFlagAffi, String aMotivo)
			throws F3BException {

		EventoNotificaModel lEve = new EventoNotificaModel();
		switch (Integer.parseInt(aPosizioneGiu)) {
		case 7: // LIBERO
		case 10: // LIBERO
		case 16: // LIBERO IN DIFFERIMENTO PENA
		case 17: // LIBERO IN DIFFERIMENTO PENA (PROVVISORIA)
		case 20: // EVASO
		case 26: // ESPULSO
		case 30: // ESTRADATO
		case 46: // LIBERO in Sospensione
		case 47: { // LIBERO in Sospensione
			lEve.getEvento().setCodTipoProvvedimento("06");
			lEve.getEvento().setCodMotivo(aMotivo);
			break;
		}
		case 3: // Espiazione Pena in Regime Carcerario
		case 14: { // Espiazione Pena in Semiliberta
			if (getRequestStringParameter("tipo").equals("scarcerare")) { // da scarcerare
				lEve.getEvento().setCodTipoProvvedimento("09");
				lEve.getEvento().setCodMotivo(aMotivo);
			} else if (getRequestStringParameter("tipo").equals("scarcerato")) { // già scarcerato
				lEve.getEvento().setCodTipoProvvedimento("12");
				lEve.getEvento().setCodMotivo(aMotivo);
			} else {
				lEve.getEvento().setCodTipoProvvedimento("04");
				lEve.getEvento().setCodMotivo("0000");
			}
			break;
		}
		case 4: { // Arresti Domiciliari Ex Art. 656/10
			lEve.getEvento().setCodTipoProvvedimento("04");
			lEve.getEvento().setCodMotivo(aMotivo);
			break;
		}
		case 12: // Espiazione Pena in Regime di Detenzione Domiciliare
		case 13: // Affidamento in prova
		case 29: // Detenzione domiciliare provvisoria
		case 54: { // Affidamento in prova provvisorio
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

		if (aFlagAffi != null && aFlagAffi.equals("S")) { // Registrazione data inizio misura
			lEve.getEvento().setCodTipoProvvedimento("12");
			lEve.getEvento().setCodMotivo(aMotivo);
		}

		// mev_62
		if (!isRequestParameterNullObj("tipo") && getRequestStringParameter("tipo").equals("scarcerare")) {
			// da scarcerare
			lEve.getEvento().setCodTipoProvvedimento("09");
			lEve.getEvento().setCodMotivo(aMotivo);
		} else if (!isRequestParameterNullObj("tipo")
				&& getRequestStringParameter("tipo").equals("scarcerato")) { // già scarcerato
			lEve.getEvento().setCodTipoProvvedimento("12");
			lEve.getEvento().setCodMotivo(aMotivo);
		}
		return lEve;
	}

}