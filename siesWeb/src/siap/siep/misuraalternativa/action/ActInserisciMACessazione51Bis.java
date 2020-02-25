package siap.siep.misuraalternativa.action;

import java.math.BigDecimal;
import java.util.Date;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
import siap.sico.calendar.model.CalendarModel;
import siap.sico.decodifiche.model.ComuneModel;
import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.misuraalternativa.controller.IMisuraAlternativa;
import siap.sico.misuraalternativa.model.MisuraAlternativaModel;
import siap.sico.util.SICOLookupRemote;
import siap.siep.SIEPException;
import siap.siep.calcolopena.action.ActCalcoloPenaMain;
import siap.siep.calcolopena.controller.ICalcoloPena;
import siap.siep.calcolopena.model.CalcoloPenaModel;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.notifica.model.NotificaModel;
import siap.siep.penaresidua.action.ICostantiPenaResidua;
import siap.siep.penaresidua.controller.IPenaResidua;
import siap.siep.penaresidua.model.PenaResiduaModel;
import siap.siep.posizione.action.ICostantiPosizioneGiuridica;
import siap.siep.posizione.model.PosizioneGiuridicaModel;
import siap.siep.sospensione.model.SospensioneModel;
import siap.siep.util.SIEPLookupRemote;
import siap.sius.depositodecreto.model.DepositoDecretoModel;
import siap.sius.depositoordinanzapc.model.DepositoOrdinanzaPcModel;
import siap.sius.tenore.model.TenoreModel;

/**
 * Action di Inserimento della Cessazione della Misura disposta dal MDS 51bis. Nuova classe aggiunta per
 * gestire le cessazioni disposte dal MDS DL 146/2013.
 *
 * La action viene invocata due volte, la prima per effettuare l'inserimento del provvedimento della
 * Sorveglianza e l'eventuale calcolo della pena. La seconda per effettuare l'innserimento del provvedimento
 * SIEP di esecuzione.
 *
 * @author d.fiorletta
 * @since DL 146/2013
 */
public class ActInserisciMACessazione51Bis extends ActMisuraAlternativa
		implements ICostantiMisuraAlternativa {
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public String processRequest() throws F3BException {
		String lPage = null;
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("isInsProvvSorv = " + getRequestStringParameter("isInsProvvSorv"));

		if (getRequestStringParameter("isInsProvvSorv").equals("S")) {
			// ========================================================================
			// Sto inserendo/agganciando l'ordinanza di Cessazione
			// - Ricalcolo la decorrenza scadenza pena
			// - Inserisco l'ordinanza se non presente
			// ========================================================================
			lPage = this.inserisciProvvedimentoSIUS();
		} else {
			// ========================================================================
			// Sto inserendo il provvedimento di Esecuzione ed eventualmente
			// aggiornando la Misura nei campi Nota e nei campi relativi al
			// all'ordinanza
			// ========================================================================
			lPage = this.inserisciProvvedimentoSIEP();
		}

		return lPage;
	}

	/**
	 * Inserimento del provvedimento SIUS ed eventuale calcolo pena che viene messa in sessione
	 * 
	 * @return
	 * @throws F3BException
	 */
	private String inserisciProvvedimentoSIUS() throws F3BException {
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("Fase di inserimento provvedimento SIUS");

		String lPage = null;

		String lCodPosizioneGiu = getRequestStringParameter(
				ICostantiPosizioneGiuridica.CAMPO_COD_POSIZIONE_GIURIDICA);
		PosizioneGiuridicaModel lPosMod = new PosizioneGiuridicaModel();
		lPosMod.setCodPosizioneGiuridica(lCodPosizioneGiu);

		MisuraAlternativaModel lMACessazioneModel = null;
		BigDecimal lIdOrdinanza = getRequestBigDecimalParameter(
				ICostantiMisuraAlternativa.CAMPO_ID_DOCUMENTO_SIUS);

		IMisuraAlternativa lMisAltCtrl = SICOLookupRemote.getMisuraAlternativaRemote();
		if (lIdOrdinanza != null && !lIdOrdinanza.toString().equals(""))
			lMACessazioneModel = lMisAltCtrl.ExRicercaMisuraAlternativaByIdEvento(lIdOrdinanza);

		BigDecimal lAnniReclusioneRE = null;
		BigDecimal lGiorniReclusioneRE = null;
		BigDecimal lMesiReclusioneRE = null;
		BigDecimal lAnniArrestoRE = null;
		BigDecimal lGiorniArrestoRE = null;
		BigDecimal lMesiArrestoRE = null;

		if (!this.isRequestParameterNullObj(ICostantiMisuraAlternativa.CAMPO_NUM_ANNI_REVOCA_RECLUSIONE)
				&& !getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_NUM_ANNI_REVOCA_RECLUSIONE)
						.equals(""))
			lAnniReclusioneRE = new BigDecimal(
					getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_NUM_ANNI_REVOCA_RECLUSIONE));

		if (!this.isRequestParameterNullObj(ICostantiMisuraAlternativa.CAMPO_NUM_GIORNI_REVOCA_RECLUSIONE)
				&& !getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_NUM_GIORNI_REVOCA_RECLUSIONE)
						.equals(""))
			lGiorniReclusioneRE = new BigDecimal(
					getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_NUM_GIORNI_REVOCA_RECLUSIONE));

		if (!this.isRequestParameterNullObj(ICostantiMisuraAlternativa.CAMPO_NUM_MESI_REVOCA_RECLUSIONE)
				&& !getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_NUM_MESI_REVOCA_RECLUSIONE)
						.equals(""))
			lMesiReclusioneRE = new BigDecimal(
					getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_NUM_MESI_REVOCA_RECLUSIONE));

		if (!this.isRequestParameterNullObj(ICostantiMisuraAlternativa.CAMPO_NUM_ANNI_REVOCA_ARRESTO)
				&& !getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_NUM_ANNI_REVOCA_ARRESTO)
						.equals(""))
			lAnniArrestoRE = new BigDecimal(
					getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_NUM_ANNI_REVOCA_ARRESTO));

		if (!this.isRequestParameterNullObj(ICostantiMisuraAlternativa.CAMPO_NUM_GIORNI_REVOCA_ARRESTO)
				&& !getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_NUM_GIORNI_REVOCA_ARRESTO)
						.equals(""))
			lGiorniArrestoRE = new BigDecimal(
					getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_NUM_GIORNI_REVOCA_ARRESTO));

		if (!this.isRequestParameterNullObj(ICostantiMisuraAlternativa.CAMPO_NUM_MESI_REVOCA_ARRESTO)
				&& !getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_NUM_MESI_REVOCA_ARRESTO)
						.equals(""))
			lMesiArrestoRE = new BigDecimal(
					getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_NUM_MESI_REVOCA_ARRESTO));

		// ==========================================================================
		// Se l'ordinanza non è stata selezionata, o è stata selezionata ma sono stati
		// modificati i dati, procedo all'inserimento di una MA
		// ==========================================================================
		if (lMACessazioneModel == null) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("MA non a sistema o modificat: inserisco evento del TDS");

			// inserisco evento del TDS
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

			String lTipoDecisione = getRequestStringParameter(
					ICostantiMisuraAlternativa.CAMPO_COD_TIPO_DECISIONE);

			lEveMod.setEvento(setEventoOrdinazaDecretoMisuraAlternativa(lEveMod.getEvento(), lTipoDecisione,
					lCodiceUffEmi, lComModAutEmi, lDataEmisTras));

			// =======================
			// Tenore
			// =======================
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("setto il tenore");
			// String lTipoUffSorv =
			// getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_COD_UFFICIO_SORVEGLIANZA);
			TenoreModel lTenMod = null;
			lTenMod = setTenore(new BigDecimal(1), "0010");

			// =======================
			// Misura alternativa
			// =======================
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Setto la Misura Alternativa");

			String lUfficioScarc = "SORV"; // default esegue sorv. Ovvero se Detenuto il check non è visibile
			if (!isRequestParameterNullObj("tipo")) {
				if (this.getRequestStringParameter("tipo").equals("misura"))
					lUfficioScarc = "PROC";
				else if (this.getRequestStringParameter("tipo").equals("detenuto"))
					lUfficioScarc = "SORV";
			}

			MisuraAlternativaModel lMisMod = null;
			lMisMod = setMisuraAlternativa(lTipoDecisione, "CE", lCodiceUffEmi,
					getRequestStringParameter(ICostantiEvento.CAMPO_COD_MOTIVO), lUfficioScarc);

			// n.b. Data Inizio Misura = data revoca o data inizio misura sospensione
			// FIXME DL 146/2013 verificare
			if (!this.isRequestParameterNullObj(ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_INIZIO_REVOCA)) {
				lMisMod.setDataInizioRevoca(
						getRequestDateParameter(ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_INIZIO_REVOCA,
								ICostantiMisuraAlternativa.CAMPO_MESE_DATA_INIZIO_REVOCA,
								ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_INIZIO_REVOCA));
			} else if (!this.isRequestParameterNullObj(
					ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_INGRESSO_ISTITUTO)) {
				lMisMod.setDataInizioRevoca(
						getRequestDateParameter(ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_INGRESSO_ISTITUTO,
								ICostantiMisuraAlternativa.CAMPO_MESE_DATA_INGRESSO_ISTITUTO,
								ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_INGRESSO_ISTITUTO));
			}

			if (!this.isRequestParameterNullObj(
					ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_INGRESSO_ISTITUTO)) {
				lMisMod.setDataIngressoIstituto(
						getRequestDateParameter(ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_INGRESSO_ISTITUTO,
								ICostantiMisuraAlternativa.CAMPO_MESE_DATA_INGRESSO_ISTITUTO,
								ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_INGRESSO_ISTITUTO));
			}

			lMisMod.setNumGiorniRevocaReclusione(lGiorniReclusioneRE);
			lMisMod.setNumMesiRevocaReclusione(lMesiReclusioneRE);
			lMisMod.setNumAnniRevocaReclusione(lAnniReclusioneRE);

			lMisMod.setNumGiorniRevocaArresto(lGiorniArrestoRE);
			lMisMod.setNumMesiRevocaArresto(lMesiArrestoRE);
			lMisMod.setNumAnniRevocaArresto(lAnniArrestoRE);

			// ========================================================================
			// INSERIMENTO MA
			// ========================================================================
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Inserisco alternativa");

			if ("02".equals(lTipoDecisione)) {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("Inserisco deposito decreto");
				DepositoDecretoModel lDepDecMod = setDepositoDecreto(lCodiceUffEmi);
				lMACessazioneModel = lMisAltCtrl.ExInserisciDecretoSospEventoNotifica(lEveMod, lDepDecMod,
						lTenMod, lMisMod);
			} else {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("Inserisco deposito ordinanza PC");
				DepositoOrdinanzaPcModel lDepOrdMod = setDepositoOrdinanzaPc(lCodiceUffEmi);
				lMACessazioneModel = lMisAltCtrl.ExInserisciMisuraAlternativaEventoNotifica(lEveMod,
						lDepOrdMod, lTenMod, lMisMod);
			}
			// lMACessazioneModel = lMisAltCtrl.ExInserisciMisuraAlternativaEventoNotifica(lEveMod,
			// lDepOrdMod, lTenMod, lMisMod);
		} else {
			// ========================================================================
			// MA già a sistema devo comunque aggiornare:
			// - eventuali le note
			// - eventuale data di cessazione
			// - eventuali giorni revocati
			// - contro soggetto detenuto/In misura
			// - eventuale data ingresso in istituto
			// ========================================================================
			// Note
			lMACessazioneModel.setNote(getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_NOTE));

			// Data Cessazione
			// Date lDataCessataDal = null;
			// if (!this.isRequestParameterNullObj(ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_INIZIO_REVOCA))
			// { // Misura
			// Alternativa
			// Cessata
			// dal
			// lDataCessataDal = getRequestDateParameter(
			// ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_INIZIO_REVOCA,
			// ICostantiMisuraAlternativa.CAMPO_MESE_DATA_INIZIO_REVOCA,
			// ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_INIZIO_REVOCA);
			// }

			// Quantum Revocati
			lMACessazioneModel.setNumGiorniRevocaReclusione(lGiorniReclusioneRE);
			lMACessazioneModel.setNumMesiRevocaReclusione(lMesiReclusioneRE);
			lMACessazioneModel.setNumAnniRevocaReclusione(lAnniReclusioneRE);

			lMACessazioneModel.setNumGiorniRevocaArresto(lGiorniArrestoRE);
			lMACessazioneModel.setNumMesiRevocaArresto(lMesiArrestoRE);
			lMACessazioneModel.setNumAnniRevocaArresto(lAnniArrestoRE);

			// In Misura/Detenuto + Eventuale Data Ingresso Istituto
			if (!isRequestParameterNullObj("tipo")) {
				if (this.getRequestStringParameter("tipo").equals("detenuto")) {
					lMACessazioneModel.setCodTipoUfficioScarcerazione("SORV");
					if (!isRequestParameterNullObj(
							ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_INGRESSO_ISTITUTO)
							&& !getRequestStringParameter(
									ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_INGRESSO_ISTITUTO)
											.equals("")) {
						lMACessazioneModel.setDataIngressoIstituto(getRequestDateParameter(
								ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_INGRESSO_ISTITUTO,
								ICostantiMisuraAlternativa.CAMPO_MESE_DATA_INGRESSO_ISTITUTO,
								ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_INGRESSO_ISTITUTO));
					}
				} else if (this.getRequestStringParameter("tipo").equals("misura")) {
					lMACessazioneModel.setCodTipoUfficioScarcerazione("PROC");
				}
			} else {
				lMACessazioneModel.setCodTipoUfficioScarcerazione("-");
			}

			lMACessazioneModel.setCodUfficioAggiornamento(getCodUfficioUtenteConnesso());
			lMACessazioneModel.setCodOperatoreAggiornamento(getCodUtenteConnesso());
			lMACessazioneModel.setDataAggiornamento(DateUtils.getSysDate());

			// ==================================
			// Aggiorno la MA
			// ==================================
			lMisAltCtrl.ExModificaMisuraAlternativa(lMACessazioneModel);
		}

		// ==========================================================================
		// Effettuo l'eventuale calcolo della pena
		// ==========================================================================
		this.calcolaPenaCessazione();

		String lTipoCessazione = getRequestStringParameter("tipoCessazione");
		String lAction = null;

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("lTipoCessazione = " + lTipoCessazione);

		if (lTipoCessazione.equals("AFFIDAMENTO"))
			lAction = "siap.siep.misuraalternativa.action.ActLoadInserisciMACess51bisAffProva";
		else if (lTipoCessazione.equals("DETENZIONE"))
			lAction = "siap.siep.misuraalternativa.action.ActLoadInserisciMACess51bisDetDom";
		else if (lTipoCessazione.equals("SEMILIBERTA"))
			lAction = "siap.siep.misuraalternativa.action.ActLoadInserisciMACess51bisSemiliberta";
		else if (lTipoCessazione.equals("INDULTINO"))
			lAction = "siap.siep.misuraalternativa.action.ActLoadInserisciMACess51bisIndultino";
		else if (lTipoCessazione.equals("DETENZIONE_DOMICILIARE_TERMINE"))
			lAction = "siap.siep.misuraalternativa.action.ActLoadInserisciMACess51bisDetDomTerm";
		else if (lTipoCessazione.equals(ICostantiMisuraAlternativa.ESP_PRESSO_DOM))
			lAction = "siap.siep.misuraalternativa.action.ActLoadInserisciMACess51bisEsecPenPressoDom";

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("lAction = " + lAction);
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("idOrdinanza = " + lMACessazioneModel.getEveIdEvento());

		lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD + "=" + lAction + "&"
				+ ICostantiMisuraAlternativa.CAMPO_ID_DOCUMENTO_SIUS + "="
				+ lMACessazioneModel.getEveIdEvento();

		return lPage;
	}

	/**
	 * Metodo di inserimento del provvedimento SIEP. - Inserisce il provvedimneto - Inserisce la PR (presa
	 * dalla sessione) e la aggancia al provvedimento - Aggiorna MA (note)
	 * 
	 * 
	 * @return la Action di detteglio
	 * @throws F3BException
	 */
	private String inserisciProvvedimentoSIEP() throws F3BException {
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("Fase di inserimento provvedimento SIEP");

		FascicoloSiepModel lFascicoloModel = (FascicoloSiepModel) getSessionAttribute("fascicolo");

		String lTipoCessazione = getRequestStringParameter("tipoCessazione");
		String lCodPosizione = getRequestStringParameter(
				ICostantiPosizioneGiuridica.CAMPO_COD_POSIZIONE_GIURIDICA);

		PosizioneGiuridicaModel lPosModel = new PosizioneGiuridicaModel();
		lPosModel.setCodPosizioneGiuridica(lCodPosizione);

		// ==========================================================================
		// Recupero MA per aggiornare il campo Note
		// ==========================================================================
		BigDecimal lIdOrdinanza = getRequestBigDecimalParameter(
				ICostantiMisuraAlternativa.CAMPO_ID_DOCUMENTO_SIUS);

		MisuraAlternativaModel lMACessazioneModel = null;

		IMisuraAlternativa lMisAltCtrl = SICOLookupRemote.getMisuraAlternativaRemote();
		lMACessazioneModel = lMisAltCtrl.ExRicercaMisuraAlternativaByIdEvento(lIdOrdinanza);

		lMACessazioneModel.setNote(getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_NOTE));

		// ==========================================================================
		// Determino il tipo di Evento
		// Le posizioni giuridiche gestite sono:
		// - "Libero"
		// - In Misura
		// - Detenuto (03)
		// Per le posizioni non gestite si emette un provvedimento generico
		// ==========================================================================
		String lCodiceMotivo = lMACessazioneModel.getCodTipoMisura();

		EventoNotificaModel lEve = new EventoNotificaModel();

		lEve.getEvento().setCodTipoEvento("01");

		if ((lPosModel.isLibero() || lCodPosizione.equals("03")) // Libero e detenuto x Tutte le misur( x ora)
				|| (lTipoCessazione.equals("AFFIDAMENTO") // 32-37
						&& (lCodPosizione.equals("13") || lCodPosizione.equals("54")))
				|| (lTipoCessazione.equals("DETENZIONE") // 31 - 36
						&& (lCodPosizione.equals("12") || lCodPosizione.equals("29")))
				|| (lTipoCessazione.equals("SEMILIBERTA") && (lCodPosizione.equals("14")))
				|| (lTipoCessazione.equals("INDULTINO") // 35 - 40
						&& (lCodPosizione.equals("27")))
				|| (lTipoCessazione.equals("DETENZIONE_DOMICILIARE_TERMINE") // 25 Det Dom Speciale
																				// (differimento)
						&& (lCodPosizione.equals("25") || lCodPosizione.equals("12")
								|| lCodPosizione.equals("29")))
				|| (lTipoCessazione.equals(ICostantiMisuraAlternativa.ESP_PRESSO_DOM) // 51 - 52
						&& (lCodPosizione.equals("50")))) {

			if ("PROC".equals(lMACessazioneModel.getCodTipoUfficioScarcerazione())) {
				lEve.getEvento().setCodTipoProvvedimento("06"); // Se esegue procura (soggetto in misura) è un
																// OE
			} else if ("SORV".equals(lMACessazioneModel.getCodTipoUfficioScarcerazione())) {
				lEve.getEvento().setCodTipoProvvedimento("12"); // Se eseguito SIUS (soggetto detenuto) è una
																// Comunicazione
			} else {
				// Potrebbe essere "-" o null se detenuto e quindi opzione "esegue" non disponibile
				lEve.getEvento().setCodTipoProvvedimento("12"); // Detenut
			}

			// =====================================
			// Rimappatura Codici MDS DL 146/2013
			// =====================================
			if (lCodiceMotivo.equals("2281"))
				lEve.getEvento().setCodMotivo("5430"); // Affidamento
			else if (lCodiceMotivo.equals("2205"))
				lEve.getEvento().setCodMotivo("5431"); // Affidamento
			else if (lCodiceMotivo.equals("2282"))
				lEve.getEvento().setCodMotivo("5432"); // Affidamento

			else if (lCodiceMotivo.equals("2284"))
				lEve.getEvento().setCodMotivo("5433"); // Detenzione Domiciliare
			else if (lCodiceMotivo.equals("2285"))
				lEve.getEvento().setCodMotivo("5434"); // Detenzione Domiciliare
			else if (lCodiceMotivo.equals("2287"))
				lEve.getEvento().setCodMotivo("5435"); // Detenzione Domiciliare
			else if (lCodiceMotivo.equals("2288"))
				lEve.getEvento().setCodMotivo("5436"); // Detenzione Domiciliare

			else if (lCodiceMotivo.equals("2283"))
				lEve.getEvento().setCodMotivo("5437"); // Semilibertà

			else if (lCodiceMotivo.equals("2299"))
				lEve.getEvento().setCodMotivo("5438"); // Esecuzione presso il domicilio

			else if (lCodiceMotivo.equals("2286"))
				lEve.getEvento().setCodMotivo("5439"); // Detenzione Domiciliare Speciale (nelle forme del
														// differimento)
			else if (lCodiceMotivo.equals("2290"))
				lEve.getEvento().setCodMotivo("5442"); // Indultino
		} else {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.warn("Posizione giuridica non gestita: inserisco un provvedimento generico");
			lEve.getEvento().setCodTipoProvvedimento("04"); // 04 - Provvedimento
			lEve.getEvento().setCodMotivo("0000"); // 0000 - Provvedimento Indeterminato
		}

		//
		lEve.setEvento(setEventoProvvedimentoMisuraAlternativa(lEve.getEvento()));

		lEve.getMagistrato().setCodMagistrato(this.calcolaMagistrato());
		lEve.getEvento().setEveIdEvento(lIdOrdinanza);

		NotificaModel[] lNotifiche = this.setNotificheMisuraAlternativa();
		lEve.setNotifiche(lNotifiche);

		// ==========================================================================
		// Recupero la PR dalla sessione per aggiornare il fine pena
		// ==========================================================================
		PenaResiduaModel lPenaRes = null;
		SospensioneModel lSospModel = null;
		if (!isSessionAttributeNullObj("penaresidua51bisMDS")) {

			CalcoloPenaModel lCalcoloPenaModelRet = (CalcoloPenaModel) this
					.getSessionAttribute("penaresidua51bisMDS");
			lPenaRes = lCalcoloPenaModelRet.getPenaResiduaRicalcolata();

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("lPenaRes = " + lPenaRes);
			// forzo a null l'id per forzare l'inserimento di una nuova PR
			lPenaRes.setIdPenaResidua(null);
			lPenaRes.setFlagValidato("N");
			lPenaRes.setCodOperatoreInserimento(getCodUtenteConnesso());
			lPenaRes.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
			lPenaRes.setDataInserimento(DateUtils.getSysDate());
			lPenaRes.setCodOperatoreAggiornamento(null);
			lPenaRes.setCodUfficioAggiornamento(null);
			lPenaRes.setDataAggiornamento(null);

			if (!isRequestParameterNullObj(ICostantiPenaResidua.CAMPO_ANNO_DATA_FINE)) {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("Data Fine presente in maschera la recupero...");

				lPenaRes.setDataFine(getRequestDateParameter(ICostantiPenaResidua.CAMPO_ANNO_DATA_FINE,
						ICostantiPenaResidua.CAMPO_MESE_DATA_FINE,
						ICostantiPenaResidua.CAMPO_GIORNO_DATA_FINE));

				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("Data Fine = " + lPenaRes.getDataFine());

			}

			// =========================================================================
			// SOSPENSIONE (eventuale se misura interrotta) Data cessazione Misura
			// =========================================================================
			// SospensioneModel lSospModel = new SospensioneModel();

			if ("S".equals(getRequestStringParameter("isPenaRideterminata"))) {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("Calcolo la sospensione");
				lSospModel = new SospensioneModel();

				// lSospModel.setDataInizio(lDataDalRevoca);
				lSospModel.setNumAnniPenaResiduaReclus(lPenaRes.getNumAnniReclusione());
				lSospModel.setNumMesiPenaResiduaReclus(lPenaRes.getNumMesiReclusione());
				lSospModel.setNumGiorniPenaResiduaReclus(lPenaRes.getNumGiorniReclusione());

				lSospModel.setNumAnniPenaResiduaArres(lPenaRes.getNumAnniArresto());
				lSospModel.setNumMesiPenaResiduaArres(lPenaRes.getNumMesiArresto());
				lSospModel.setNumGiorniPenaResiduaArres(lPenaRes.getNumGiorniArresto());

				lSospModel.setAmmendaResidua(lPenaRes.getImportoAmmenda());
				lSospModel.setMultaResidua(lPenaRes.getImportoMulta());

				if (lCalcoloPenaModelRet.getPenaEspiata() != null) {
					CalendarModel lPenaEspiataSosp = lCalcoloPenaModelRet.getPenaEspiata();

					lSospModel.setNumAnniPenaEspiata(new BigDecimal(lPenaEspiataSosp.getNumAnni()));
					lSospModel.setNumMesiPenaEspiata(new BigDecimal(lPenaEspiataSosp.getNumMesi()));
					lSospModel.setNumGiorniPenaEspiata(new BigDecimal(lPenaEspiataSosp.getNumGiorni()));
				}

				// lSospensione.setPenResIdPenaResidua(lKeyPena);

				lSospModel.setNumGiorniLibanticipata(
						new BigDecimal(lCalcoloPenaModelRet.getLiberazioneAnticipata()));

				lSospModel.setFasSieIdFascicoloSiep(lFascicoloModel.getIdFascicoloSiep());

				lSospModel.setCodOperatoreInserimento(getCodUtenteConnesso());
				lSospModel.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
				lSospModel.setDataInserimento(DateUtils.getSysDate());
			}
		}

		// ==========================================================================
		//
		// ==========================================================================
		EventoNotificaModel lEveRetModel = lMisAltCtrl.ExInserisciOModificaMANotifica(lEve, lPenaRes,
				lMACessazioneModel, lSospModel);

		String lPage = null;

		lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
				+ "=siap.siep.misuraalternativa.action.ActDettaglioMACessazione51Bis" + "&"
				+ ICostantiEvento.CAMPO_ID_EVENTO + "=" + lEveRetModel.getEvento().getIdEvento();

		return lPage;
	}

	/**
	 * Effettua il calcolo della pena in caso di cessazione. Il calcolo parte dai seguenti dati: 1) Misura
	 * Cessata dal 2) Quantum rideterminati 3) data ingresso in istituto.
	 * 
	 * Le prime due voci consentono di rideterminare i quantum residui da espiare a seguito della cessazione e
	 * sono mutuamente esclusivi. Se si indica 'Misura Cessata Dal', deve essere presente a sistema una pena
	 * in corso di espiazione (decorrenza e scadena).
	 * 
	 * Non è detto che la pena venga rideterminata, Se l'utente NON ha indicato i dati per il ricalcolo,
	 * 
	 * @throws F3BException
	 */
	private void calcolaPenaCessazione() throws F3BException {
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("Inizio calcolo Pena Cessazione");

		// =================================
		// Misura Alternativa Cessata dal
		// =================================
		Date lDataDalRevoca = null;
		if (!this.isRequestParameterNullObj(ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_INIZIO_REVOCA)) {
			lDataDalRevoca = getRequestDateParameter(ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_INIZIO_REVOCA,
					ICostantiMisuraAlternativa.CAMPO_MESE_DATA_INIZIO_REVOCA,
					ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_INIZIO_REVOCA);
		}

		// ===================================
		// Quantum rideterminati
		// ===================================
		BigDecimal lAnniReclusioneRE = null;
		BigDecimal lGiorniReclusioneRE = null;
		BigDecimal lMesiReclusioneRE = null;
		BigDecimal lAnniArrestoRE = null;
		BigDecimal lGiorniArrestoRE = null;
		BigDecimal lMesiArrestoRE = null;

		if (!this.isRequestParameterNullObj(ICostantiMisuraAlternativa.CAMPO_NUM_ANNI_REVOCA_RECLUSIONE)
				&& !getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_NUM_ANNI_REVOCA_RECLUSIONE)
						.equals(""))
			lAnniReclusioneRE = new BigDecimal(
					getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_NUM_ANNI_REVOCA_RECLUSIONE));

		if (!this.isRequestParameterNullObj(ICostantiMisuraAlternativa.CAMPO_NUM_GIORNI_REVOCA_RECLUSIONE)
				&& !getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_NUM_GIORNI_REVOCA_RECLUSIONE)
						.equals(""))
			lGiorniReclusioneRE = new BigDecimal(
					getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_NUM_GIORNI_REVOCA_RECLUSIONE));

		if (!this.isRequestParameterNullObj(ICostantiMisuraAlternativa.CAMPO_NUM_MESI_REVOCA_RECLUSIONE)
				&& !getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_NUM_MESI_REVOCA_RECLUSIONE)
						.equals(""))
			lMesiReclusioneRE = new BigDecimal(
					getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_NUM_MESI_REVOCA_RECLUSIONE));

		if (!this.isRequestParameterNullObj(ICostantiMisuraAlternativa.CAMPO_NUM_ANNI_REVOCA_ARRESTO)
				&& !getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_NUM_ANNI_REVOCA_ARRESTO)
						.equals(""))
			lAnniArrestoRE = new BigDecimal(
					getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_NUM_ANNI_REVOCA_ARRESTO));

		if (!this.isRequestParameterNullObj(ICostantiMisuraAlternativa.CAMPO_NUM_GIORNI_REVOCA_ARRESTO)
				&& !getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_NUM_GIORNI_REVOCA_ARRESTO)
						.equals(""))
			lGiorniArrestoRE = new BigDecimal(
					getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_NUM_GIORNI_REVOCA_ARRESTO));

		if (!this.isRequestParameterNullObj(ICostantiMisuraAlternativa.CAMPO_NUM_MESI_REVOCA_ARRESTO)
				&& !getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_NUM_MESI_REVOCA_ARRESTO)
						.equals(""))
			lMesiArrestoRE = new BigDecimal(
					getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_NUM_MESI_REVOCA_ARRESTO));

		// ===================================
		// Periodo espiato in istituto (GG)
		// ===================================
		String lPeriodoEspiato = null;
		if (!this.isRequestParameterNullObj(ICostantiMisuraAlternativa.CAMPO_FLAG_PERIODO_ESPIATO)
				&& getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_FLAG_PERIODO_ESPIATO) != null
				&& !getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_FLAG_PERIODO_ESPIATO)
						.equals("")) {
			lPeriodoEspiato = getRequestStringParameter(
					ICostantiMisuraAlternativa.CAMPO_FLAG_PERIODO_ESPIATO);
		}

		// ==========================================================================
		// Se non sono stati specificati ne i nuovi quantum ne la data di revoca
		// non viene effettuato il calcolo della pena e vale quella a sistema
		// ==========================================================================
		String lTipoMisura = getRequestStringParameter("tipoCessazione");

		if (lGiorniReclusioneRE == null && lMesiReclusioneRE == null && lAnniReclusioneRE == null
				&& lGiorniArrestoRE == null && lMesiArrestoRE == null && lAnniArrestoRE == null
				&& lDataDalRevoca == null) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug(
					"Pena Rideterminata non indicata, non effettuo il calcolo della pena ma recupero l'ultima pena ");
			// Non effettuo il calcolo della pena ma recupero l'ultima pena
			BigDecimal lIdPenaRes = getRequestBigDecimalParameter(ICostantiPenaResidua.CAMPO_ID_PENA_RESIDUA);

			IPenaResidua lPenResCtrl = SIEPLookupRemote.getPenaResiduaRemote();
			PenaResiduaModel lastPenaResidua = new PenaResiduaModel();
			lastPenaResidua = lPenResCtrl.ExRicercaPenaResiduaByKey(lIdPenaRes);

			CalcoloPenaModel lCalcoloPenaModelRet = new CalcoloPenaModel();
			lCalcoloPenaModelRet.setPenaResiduaRicalcolata(lastPenaResidua);

			// this.setSessionAttribute("penaresidua51bisMDS", lCalcoloPenaModelRet);
		} else {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Pena Rideterminata indicata, effettuo il calcolo della pena ");
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("lDataDalRevoca      = " + lDataDalRevoca);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("lGiorniReclusioneRE = " + lGiorniReclusioneRE);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("lMesiReclusioneRE   = " + lMesiReclusioneRE);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("lAnniReclusioneRE   = " + lAnniReclusioneRE);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("lGiorniArrestoRE    = " + lGiorniArrestoRE);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("lMesiArrestoRE      = " + lMesiArrestoRE);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("lAnniArrestoRE      = " + lAnniArrestoRE);

			CalendarModel lCalModRec = new CalendarModel();
			lCalModRec.setNumGiorni(lGiorniReclusioneRE);
			lCalModRec.setNumMesi(lMesiReclusioneRE);
			lCalModRec.setNumAnni(lAnniReclusioneRE);

			CalendarModel lCalModArr = new CalendarModel();
			lCalModArr.setNumGiorni(lGiorniArrestoRE);
			lCalModArr.setNumMesi(lMesiArrestoRE);
			lCalModArr.setNumAnni(lAnniArrestoRE);

			int lStatoDetenuto = 5;
			// lStatoDetenuto = 0 = In Sospensione
			// lStatoDetenuto = 1 = In misura
			// lStatoDetenuto = 2 = Libero
			// lStatoDetenuto = 5 = default nessun calcolo, ovvero Misura non gestita o posizione non gestita
			// Attenzione lStatoDetenuto = utilizzato per il calcolo della pena, i
			// valori 0 e 1 sono equivalenti

			String lCodPosizioneGiu = getRequestStringParameter(
					ICostantiPosizioneGiuridica.CAMPO_COD_POSIZIONE_GIURIDICA);

			// isLibero()
			if (lCodPosizioneGiu.equals("07") || lCodPosizioneGiu.equals("10")
					|| lCodPosizioneGiu.equals("16") || lCodPosizioneGiu.equals("17")
					|| lCodPosizioneGiu.equals("46") || lCodPosizioneGiu.equals("47")
					|| lCodPosizioneGiu.equals("20") || lCodPosizioneGiu.equals("26")
					|| lCodPosizioneGiu.equals("30")) {
				lStatoDetenuto = 2;
			} else if (lTipoMisura.equals("AFFIDAMENTO")) {
				// In sospensione (51 bis o 51 ter)
				if (lCodPosizioneGiu.equals("32") || lCodPosizioneGiu.equals("37")) {
					lStatoDetenuto = 0;
				}

				// In misura (eventualmente provvisoria 54)
				if (lCodPosizioneGiu.equals("13") || lCodPosizioneGiu.equals("54")) {
					lStatoDetenuto = 1;
				}
			} else if (lTipoMisura.equals("DETENZIONE")
					|| lTipoMisura.equals("DETENZIONE_DOMICILIARE_TERMINE")) { // anche a termine
				// In sospensione (51 bis o 51 ter)
				if (lCodPosizioneGiu.equals("31") || lCodPosizioneGiu.equals("36")) {
					lStatoDetenuto = 0;
				}

				// In misura (eventualmente provvisoria 29)
				// 25 - Espiazione della pena in regime di detenzione domiciliare speciale (differimento vedi
				// det dem term)
				if (lCodPosizioneGiu.equals("12") || lCodPosizioneGiu.equals("29")
						|| lCodPosizioneGiu.equals("25")) {
					lStatoDetenuto = 1;
				}
			} else if (lTipoMisura.equals("SEMILIBERTA")) {
				// In sospensione (51 bis o 51 ter)
				if (lCodPosizioneGiu.equals("33") || lCodPosizioneGiu.equals("38")) {
					lStatoDetenuto = 0;
				}

				// In misura
				if (lCodPosizioneGiu.equals("14")) {
					lStatoDetenuto = 1;
				}
			} else if (lTipoMisura.equals("INDULTINO")) {
				// In sospensione (51 bis o 51 ter)
				if (lCodPosizioneGiu.equals("35") || lCodPosizioneGiu.equals("40")) {
					lStatoDetenuto = 0;
				}

				// In misura
				if (lCodPosizioneGiu.equals("27")) {
					lStatoDetenuto = 1;
				}
			} else if (lTipoMisura.equals(ICostantiMisuraAlternativa.ESP_PRESSO_DOM)) {
				// In sospensione (51 bis o 51 ter)
				if (lCodPosizioneGiu.equals("51") || lCodPosizioneGiu.equals("52")) {
					lStatoDetenuto = 0;
				}

				// In misura
				if (lCodPosizioneGiu.equals("50")) {
					lStatoDetenuto = 1;
				}
			} else {
				// Posizione non gestita o non coerente con la Misura. Non si effettua il calcolo
				lStatoDetenuto = 5;
			}

			// manca la gestione della posizione detenuto: In realtà se detenuto
			// il calcolo NON va fatto.

			// Recupero il flag detenuto/non detenuto
			String lDetenuto = null;
			PosizioneGiuridicaModel lPosMod = new PosizioneGiuridicaModel();
			lPosMod.setCodPosizioneGiuridica(lCodPosizioneGiu);

			// if(!lPosMod.isLibero()) {
			if (!isRequestParameterNullObj("tipo")) {
				if (this.getRequestStringParameter("tipo").equals("detenuto"))
					lDetenuto = "S";
				else
					lDetenuto = "N";
			} else
				lDetenuto = "S";

			// }
			// else {
			// lDetenuto = "N";
			// }

			// Detenuto dal
			Date lDataNuovoInPe = null;
			if (!this.isRequestParameterNullObj(
					ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_INGRESSO_ISTITUTO)) {
				lDataNuovoInPe = getRequestDateParameter(CAMPO_ANNO_DATA_INGRESSO_ISTITUTO,
						CAMPO_MESE_DATA_INGRESSO_ISTITUTO, CAMPO_GIORNO_DATA_INGRESSO_ISTITUTO);
			}

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Recupero la situazione attuale della Pena (dati Validati)");
			FascicoloSiepModel lFascMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");

			ActCalcoloPenaMain lCalcoloPenaMain = new ActCalcoloPenaMain();
			CalcoloPenaModel lCalcoloPenaModel = lCalcoloPenaMain.calcoloPena(lFascMod.getIdFascicoloSiep(),
					null);

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Procedo al calcolo della pena");
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("lStatoDetenuto = " + lStatoDetenuto);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("lDataNuovoInPe = " + lDataNuovoInPe);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("lDetenuto      = " + lDetenuto);

			ICalcoloPena lCtrlCalPen = SIEPLookupRemote.getCalcoloPenaRemote();
			CalcoloPenaModel lCalcoloPenaModelRet = lCtrlCalPen.exCalcoloRevocheMisureAlternative(
					lFascMod.getIdFascicoloSiep(), lDataDalRevoca, lCalModRec, lCalModArr, lPeriodoEspiato,
					lStatoDetenuto, lDataNuovoInPe, lDetenuto, lCalcoloPenaModel);

			PenaResiduaModel lPenMod = lCalcoloPenaModelRet.getPenaResiduaRicalcolata();

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Pena rideterminata = " + lPenMod);

			lPenMod.setCodOperatoreInserimento(getCodUtenteConnesso());
			lPenMod.setCodUfficioInserimento(getCodUfficioUtenteConnesso());

			if (lPenMod != null && lPenMod.getMessage() != null && lPenMod.getMessage().equals("Errore")) {
				throw new SIEPException(SIEPException.USER_MESSAGE,
						"Impossibile effettuare il calcolo della pena:quantum negativo.");
			} else {
				this.setSessionAttribute("penaresidua51bisMDS", lCalcoloPenaModelRet);
			}

		}

	}
}