package siap.siep.misuraalternativa.action;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Vector;

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
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.util.SICOLookupRemote;
import siap.siep.calcolopena.action.ActCalcoloPenaMain;
import siap.siep.calcolopena.controller.ICalcoloPena;
import siap.siep.calcolopena.model.CalcoloPenaModel;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.notifica.model.NotificaModel;
import siap.siep.penaresidua.action.ICostantiPenaResidua;
import siap.siep.penaresidua.controller.IPenaResidua;
import siap.siep.penaresidua.model.PenaResiduaModel;
import siap.siep.util.SIEPLookupRemote;
import siap.sius.depositodecreto.model.DepositoDecretoModel;
import siap.sius.depositoordinanzapc.model.DepositoOrdinanzaPcModel;
import siap.sius.tenore.model.TenoreModel;

/**
 * Nuova gestione della Prosecuzione della Misura a seguito delle modifiche introdotte del DL 146/2013. La
 * prosecuzione è disposta direttamente dall'MDS o dal TDS su Reclamo
 *
 * Questa Action viene invocata due volte. La prima in fase di inserimento o aggancio dell'ordinanza ed
 * eventuale calcolo pena a partire dalla data inizio misura digitata. Una seconda volta in fase di
 * registrazione del provvedimento dell'esecuzione.
 *
 * n.b. in funzione del tipo di prosecuzione (TDS/UDS) cambia il tipo di provvedimento emesso.
 *
 *
 * @author d.fiorletta
 * @since 04/02/2014 DL 146/2013
 */
public class ActInserisciMAProsecuzione51Bis extends ActMisuraAlternativa
		implements ICostantiMisuraAlternativa {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public String processRequest() throws F3BException {

		String lPage = null;

		if (getRequestStringParameter("isFaseOrdinanza").equals("S")) {
			// ========================================================================
			// Sto inserendo/agganciando l'ordinanza di concessione
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
	 * Prima fase viene effettuato l'inserimento/aggiornamento del provvedimento SIUS
	 *
	 * @return
	 * @throws F3BException
	 */
	private String inserisciProvvedimentoSIUS() throws F3BException {

		String lPage = null;

		String tipoMisura = getRequestStringParameter("tipomisura");
		String isConCumulo = getRequestStringParameter("isConCumulo");

		FascicoloSiepModel lFascicoloModel = (FascicoloSiepModel) getSessionAttribute("fascicolo");

		BigDecimal lIdPenaRes = getRequestBigDecimalParameter(ICostantiPenaResidua.CAMPO_ID_PENA_RESIDUA);
		IPenaResidua IPenRes = SIEPLookupRemote.getPenaResiduaRemote();
		PenaResiduaModel lPenMod = IPenRes.ExRicercaPenaResiduaByKey(lIdPenaRes);

		if (isConCumulo.equals("N")) {
			PenaResiduaModel lNuovaPenaModel = null;

			// Calcolo decorrenza e scadenza solo se la pena non è in esecuzione
			if (lPenMod.getDataInizio() == null)
				lNuovaPenaModel = this.calcolaNuovaPena(lPenMod, lFascicoloModel.getIdFascicoloSiep());

			if (lNuovaPenaModel != null)
				this.setSessionAttribute("MAPenaResiduaRideterminata", lNuovaPenaModel);
			else
				this.setSessionAttribute("MAPenaResiduaRideterminata", lPenMod);
		} else {
			this.setSessionAttribute("MAPenaResiduaRideterminata", lPenMod);
		}

		// Verifico se l'ordinanza è stata selezionata dalla lista
		BigDecimal lIdOrdinanza = getRequestBigDecimalParameter(
				ICostantiMisuraAlternativa.CAMPO_ID_DOCUMENTO_SIUS);

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("lIdOrdinanza = " + lIdOrdinanza);
		MisuraAlternativaModel lProsecuMAMod = null;
		IMisuraAlternativa lMisAltCtrl = SICOLookupRemote.getMisuraAlternativaRemote();
		if (lIdOrdinanza != null && !lIdOrdinanza.toString().equals("")) {
			lProsecuMAMod = lMisAltCtrl.ExRicercaMisuraAlternativaByIdEvento(lIdOrdinanza);
		}

		// ==========================================================================
		// Se MA non a sistema la Inserisco
		// ==========================================================================
		if (lProsecuMAMod == null) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Inserisco LA MA");

			// INSERISCO EVENTO DEL TDS
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

			String lTipoProvv = getRequestStringParameter(
					ICostantiMisuraAlternativa.CAMPO_COD_TIPO_DECISIONE);

			lEveMod.setEvento(setEventoOrdinazaDecretoMisuraAlternativa(lEveMod.getEvento(), lTipoProvv,
					lCodiceUffEmi, lComModAutEmi, lDataEmisTras));

			String lTipoUffSorv = getRequestStringParameter(
					ICostantiMisuraAlternativa.CAMPO_COD_UFFICIO_SORVEGLIANZA);

			// setto il tenore
			String lCodEsitoTenore = "";
			if (lTipoUffSorv.startsWith("UDS"))
				lCodEsitoTenore = "0009"; // 0009 = Estende misura alternativa
			else if (lTipoUffSorv.startsWith("TDS"))
				lCodEsitoTenore = "0347"; // 0347 = Accoglie reclamo e dispone prosecuzione

			TenoreModel lTenMod = setTenore(new BigDecimal(1), lCodEsitoTenore);

			// Misura alternativa
			String lCodNaturaDecisione = null;
			if (lTipoUffSorv.startsWith("UDS")) {
				if ("S".equals(isConCumulo))
					lCodNaturaDecisione = "EC"; // n.b. DL146/2013 NON esiste in SUIS ma solo in SIEP. SIUS
												// non distingue tra Cumulo o senza Cumulo
				else
					lCodNaturaDecisione = "ED"; // DL146/2013 ED = Estende Misura Alternativa
			} else if (lTipoUffSorv.startsWith("TDS")) {
				lCodNaturaDecisione = "ED"; // FIXME da verificare
			}

			lProsecuMAMod = setMisuraAlternativa(lTipoProvv, lCodNaturaDecisione, lCodiceUffEmi,
					getRequestStringParameter(ICostantiEvento.CAMPO_COD_MOTIVO), "-");

			if (getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_INIZIO_MISURA)
					.length() == 4) {
				Date lDataInizioMisura = getRequestDateParameter(
						ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_INIZIO_MISURA,
						ICostantiMisuraAlternativa.CAMPO_MESE_DATA_INIZIO_MISURA,
						ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_INIZIO_MISURA);
				lProsecuMAMod.setDataInizioMisura(lDataInizioMisura);
			}

			// ========================================================================
			// Nel caso TDS, in maschera sono presenti campi aggiuntivi:
			// - da scarcerare/scarcerato + data scarcerazione
			// - Luogo della prova
			if (!this.isRequestParameterNullObj(ICostantiMisuraAlternativa.CAMPO_DESCR_LUOGO_PROVA))
				lProsecuMAMod.setDescrLuogoProva(
						getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_DESCR_LUOGO_PROVA));

			// if
			// (!this.isRequestParameterNullObj(ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_SCARCERAZIONE))
			if (!this.isRequestParameterNullObj("tipo")) {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("tipo = " + this.getRequestStringParameter("tipo"));
				if (this.getRequestStringParameter("tipo").equals("scarcerato")) {
					lProsecuMAMod.setCodTipoUfficioScarcerazione("SORV");
					lProsecuMAMod.setDataScarcerazione(
							getRequestDateParameter(ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_SCARCERAZIONE,
									ICostantiMisuraAlternativa.CAMPO_MESE_DATA_SCARCERAZIONE,
									ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_SCARCERAZIONE));
				} else {
					lProsecuMAMod.setCodTipoUfficioScarcerazione("PROC");
				}
			}

			// ========================================================================
			// Solo per det dom term la durata della misura viene indicata nella form
			// essendo una misura a termine
			// ========================================================================
			if (!this.isRequestParameterNullObj(ICostantiMisuraAlternativa.CAMPO_NUM_GIORNI_MISURA)
					&& !getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_NUM_GIORNI_MISURA)
							.equals(""))
				lProsecuMAMod.setNumGiorniMisura(new BigDecimal(
						getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_NUM_GIORNI_MISURA)));

			if (!this.isRequestParameterNullObj(ICostantiMisuraAlternativa.CAMPO_NUM_MESI_MISURA)
					&& !getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_NUM_MESI_MISURA)
							.equals(""))
				lProsecuMAMod.setNumMesiMisura(new BigDecimal(
						getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_NUM_MESI_MISURA)));

			if (!this.isRequestParameterNullObj(ICostantiMisuraAlternativa.CAMPO_NUM_ANNI_MISURA)
					&& !getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_NUM_ANNI_MISURA)
							.equals(""))
				lProsecuMAMod.setNumAnniMisura(new BigDecimal(
						getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_NUM_ANNI_MISURA)));

			if (!this.isRequestParameterNullObj(ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_FINE_MISURA)
					&& this.getRequestStringParameter(
							ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_FINE_MISURA) != null
					&& !this.getRequestStringParameter(
							ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_FINE_MISURA).equals("")) {
				lProsecuMAMod.setDataFineMisura(
						getRequestDateParameter(ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_FINE_MISURA,
								ICostantiMisuraAlternativa.CAMPO_MESE_DATA_FINE_MISURA,
								ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_FINE_MISURA));
			}

			// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			// siesLogger.debug("Setto il deposito ordinanza / deposito decreto");

			if ("02".equals(lTipoProvv)) {
				// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				// siesLogger.debug("Inserisco deposito decreto");
				DepositoDecretoModel lDepDecMod = setDepositoDecreto(lCodiceUffEmi);
				lProsecuMAMod = lMisAltCtrl.ExInserisciDecretoSospEventoNotifica(lEveMod, lDepDecMod, lTenMod,
						lProsecuMAMod);
			} else {
				// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				// siesLogger.debug("Inserisco deposito ordinanza PC");
				DepositoOrdinanzaPcModel lDepOrdMod = setDepositoOrdinanzaPc(lCodiceUffEmi);
				lProsecuMAMod = lMisAltCtrl.ExInserisciMisuraAlternativaEventoNotifica(lEveMod, lDepOrdMod,
						lTenMod, lProsecuMAMod);
			}
		} else {
			// Misura selezionata dalla lista e non modificata aggiorno il campo note
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Misura già a sistema non la Inserisco");

			if (!this.isRequestParameterNullObj(ICostantiMisuraAlternativa.CAMPO_NOTE))
				lProsecuMAMod.setNote(getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_NOTE));

			// da scarcerare/scarvcerato
			if (!this.isRequestParameterNullObj("tipo")) {
				if ("scarcerare".equals(getRequestStringParameter("tipo")))
					lProsecuMAMod.setCodTipoUfficioScarcerazione("PROC");
				else if ("scarcerato".equals(getRequestStringParameter("tipo")))
					lProsecuMAMod.setCodTipoUfficioScarcerazione("SORV");
			}

			if (!this.isRequestParameterNullObj(ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_SCARCERAZIONE)
					&& !getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_SCARCERAZIONE)
							.equals("")) {
				lProsecuMAMod.setDataScarcerazione(
						getRequestDateParameter(ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_SCARCERAZIONE,
								ICostantiMisuraAlternativa.CAMPO_MESE_DATA_SCARCERAZIONE,
								ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_SCARCERAZIONE));

			} else
				lProsecuMAMod.setDataScarcerazione(null);

			// ========================================================================
			// Solo per det dom term la durata della misura viene indicata nella form
			// essendo una misura a termine
			// ========================================================================
			if (!this.isRequestParameterNullObj(ICostantiMisuraAlternativa.CAMPO_NUM_GIORNI_MISURA)
					&& !getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_NUM_GIORNI_MISURA)
							.equals(""))
				lProsecuMAMod.setNumGiorniMisura(new BigDecimal(
						getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_NUM_GIORNI_MISURA)));
			else
				lProsecuMAMod.setNumGiorniMisura(null);

			if (!this.isRequestParameterNullObj(ICostantiMisuraAlternativa.CAMPO_NUM_MESI_MISURA)
					&& !getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_NUM_MESI_MISURA)
							.equals(""))
				lProsecuMAMod.setNumMesiMisura(new BigDecimal(
						getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_NUM_MESI_MISURA)));
			else
				lProsecuMAMod.setNumMesiMisura(null);

			if (!this.isRequestParameterNullObj(ICostantiMisuraAlternativa.CAMPO_NUM_ANNI_MISURA)
					&& !getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_NUM_ANNI_MISURA)
							.equals(""))
				lProsecuMAMod.setNumAnniMisura(new BigDecimal(
						getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_NUM_ANNI_MISURA)));
			else
				lProsecuMAMod.setNumAnniMisura(null);

			if (!this.isRequestParameterNullObj(ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_FINE_MISURA)
					&& this.getRequestStringParameter(
							ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_FINE_MISURA) != null
					&& !this.getRequestStringParameter(
							ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_FINE_MISURA).equals("")) {
				lProsecuMAMod.setDataFineMisura(
						getRequestDateParameter(ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_FINE_MISURA,
								ICostantiMisuraAlternativa.CAMPO_MESE_DATA_FINE_MISURA,
								ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_FINE_MISURA));
			} else
				lProsecuMAMod.setDataFineMisura(null);

			lMisAltCtrl.ExModificaMisuraAlternativa(lProsecuMAMod);
		}

		// ========================================================================
		// Ritorno sulla pagina di inserimento dei dati
		String lAzione = null;
		if (tipoMisura.equals(ICostantiMisuraAlternativa.AFFIDAMENTO_IN_PROVA))
			lAzione = "siap.siep.misuraalternativa.action.ActLoadInserisciMAProsecAffProva";
		else if (tipoMisura.equals(ICostantiMisuraAlternativa.DETENZIONE_DOMICILIARE))
			lAzione = "siap.siep.misuraalternativa.action.ActLoadInserisciMAProsecDetDom";
		else if (tipoMisura.equals(ICostantiMisuraAlternativa.SEMILIBERTA))
			lAzione = "siap.siep.misuraalternativa.action.ActLoadInserisciMAProsecSemiliberta";
		else if (tipoMisura.equals(ICostantiMisuraAlternativa.DETENZIONE_DOMICILIARE_TERMINE))
			lAzione = "siap.siep.misuraalternativa.action.ActLoadInserisciMAProsecDetDomTerm";
		else if (tipoMisura.equals(ICostantiMisuraAlternativa.ESP_PRESSO_DOM))
			lAzione = "siap.siep.misuraalternativa.action.ActLoadInserisciMAProsecEsecPenPressoDom";
		else
			lAzione = "";

		lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD + "=" + lAzione + "&"
				+ ICostantiMisuraAlternativa.CAMPO_ID_DOCUMENTO_SIUS + "=" + lProsecuMAMod.getEveIdEvento();

		return lPage;
	}

	/**
	 * Registrazione del provvedimento SIEP
	 *
	 * @return
	 */
	private String inserisciProvvedimentoSIEP() throws F3BException {

		String lPage = null;

		// ==========================================================================
		// Se digitati in maschera aggiungo alla MA i dati del provvedimento di
		// concessione della MA altro titolo
		// ==========================================================================
		BigDecimal lIdOrdinanza = getRequestBigDecimalParameter(
				ICostantiMisuraAlternativa.CAMPO_ID_DOCUMENTO_SIUS);

		MisuraAlternativaModel lProsecuMAMod = null;
		IMisuraAlternativa lMisAltCtrl = SICOLookupRemote.getMisuraAlternativaRemote();
		lProsecuMAMod = lMisAltCtrl.ExRicercaMisuraAlternativaByIdEvento(lIdOrdinanza);

		if (!this.isRequestParameterNullObj(ICostantiMisuraAlternativa.CAMPO_NOTE))
			lProsecuMAMod.setNote(getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_NOTE));

		// Aggiungo i campi relativi i dati dell'ordinanza di concessione della Misura
		// che deve proseguire
		if (!isRequestParameterNullObj(ICostantiMisuraAlternativa.CAMPO_COD_TIPO_MISURA_MA_AT) && !"-"
				.equals(getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_COD_TIPO_MISURA_MA_AT))) {
			lProsecuMAMod.setCodTipoDecisioneMaAt(
					getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_COD_TIPO_DECISIONE_MA_AT));// 02/03
			lProsecuMAMod.setCodTipoMisuraMaAt(
					getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_COD_TIPO_MISURA_MA_AT)); // oggetto

			if (getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_DECISIONE_MA_AT)
					.length() == 4) {
				Date lDataDecisioneMaAt = getRequestDateParameter(
						ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_DECISIONE_MA_AT,
						ICostantiMisuraAlternativa.CAMPO_MESE_DATA_DECISIONE_MA_AT,
						ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_DECISIONE_MA_AT);
				lProsecuMAMod.setDataDecisioneMaAt(lDataDecisioneMaAt);
			}
			// Anno - Numero - ufficio SIUS
			lProsecuMAMod.setChiaveAnnoFascicoloSiusMaAt(getRequestBigDecimalParameter(
					ICostantiMisuraAlternativa.CAMPO_CHIAVE_ANNO_FAS_SIUS_MA_AT));
			lProsecuMAMod.setChiaveProgrFascicoloSiusMaAt(getRequestBigDecimalParameter(
					ICostantiMisuraAlternativa.CAMPO_CHIAVE_PROGR_FAS_SIUS_MA_AT));

			if (!getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_SEDE_UFF_EMITT_MA_AT).trim()
					.equals("")) {
				String lChiaveUfficio = "";
				if (!isRequestParameterNullObj(ICostantiMisuraAlternativa.CAMPO_TIPO_UFF_EMITT_MA_AT)) {
					lChiaveUfficio = getCodUfficioByCodTipoUfficioDescrComune(
							getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_TIPO_UFF_EMITT_MA_AT),
							getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_SEDE_UFF_EMITT_MA_AT));
				}
				if (!isRequestParameterNullObj(ICostantiMisuraAlternativa.CAMPO_COD_UFFICIO_SORVEGLIANZA)) {
					lChiaveUfficio = getCodUfficioByCodTipoUfficioDescrComune(
							getRequestStringParameter(
									ICostantiMisuraAlternativa.CAMPO_COD_UFFICIO_SORVEGLIANZA),
							getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_SEDE_UFF_EMITT_MA_AT));
				}

				lProsecuMAMod.setChiaveUfficioFascicoloSiusMaAt(lChiaveUfficio);
			}

			// Anno e numero Ordinanza
			lProsecuMAMod.setAnnoRegistroMaAt(
					getRequestBigDecimalParameter(ICostantiMisuraAlternativa.CAMPO_ANNO_REGISTRO_MA_AT));
			lProsecuMAMod.setNumeroRegistroMaAt(
					getRequestBigDecimalParameter(ICostantiMisuraAlternativa.CAMPO_NUMERO_REGISTRO_MA_AT));
		}

		// ==========================================================================
		// Se prosecuzione disposta del TDS e Misura = Differimento nelle forme
		// della detenzione domiciliare, devo calcolare il fine misura che non
		// coincide con il fine pene essendo una misura 'a termine'
		// ==========================================================================
		String lTipoMisura = getRequestStringParameter("tipomisura");
		UfficioModel lUffEmiMod = getUfficioByCodUfficio(lProsecuMAMod.getChiaveUfficioFascicoloSius());

		if (lTipoMisura.equals(ICostantiMisuraAlternativa.DETENZIONE_DOMICILIARE_TERMINE)
		// && "TDS".equals(lUffEmiMod.getCodTipoUfficio())
		) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Calcolo fine misura della detenzione domiciliare a termine");
			setDataFineMisura(lProsecuMAMod, lUffEmiMod.getCodTipoUfficio());
		}

		// =====================================
		// Dati del Provvedimento SIEP
		// =====================================
		String codiceMotivo = lProsecuMAMod.getCodTipoMisura();
		EventoNotificaModel lEve = new EventoNotificaModel();

		// Tipo Provvedimento:
		// - MDS sempre 12 - Comunicazione
		// - TDS : 12 - Comunicazione - se Scarcerato (SORV)
		// 09 - Ordine di scarcerazione - Se da scarcerare (PROC)
		List<String> lCodiciMDS = Arrays.asList("2281", "2282", "2205", "2284", "2285", "2287", "2288",
				"2283", "2299", "2286");
		List<String> lCodiciTDS = Arrays.asList("1200", "1201", "1202", "1203", "1204", "1205", "1206",
				"1207", "1208", "1209");

		if (lCodiciMDS.contains(codiceMotivo)) {
			lEve.getEvento().setCodTipoProvvedimento("12"); // Comunicazione
		} else if (lCodiciTDS.contains(codiceMotivo)) {
			if ("PROC".equals(lProsecuMAMod.getCodTipoUfficioScarcerazione())) {
				lEve.getEvento().setCodTipoProvvedimento("09"); // Ordine di scarcerazione
			} else if ("SORV".equals(lProsecuMAMod.getCodTipoUfficioScarcerazione())) {
				lEve.getEvento().setCodTipoProvvedimento("12"); // Comunicazione
			}
		}

		// ==========================================================================
		// Codici del MDS
		// ==========================================================================
		// Affidamento
		if (codiceMotivo.equals("2281"))
			lEve.getEvento().setCodMotivo("5470");
		else if (codiceMotivo.equals("2205"))
			lEve.getEvento().setCodMotivo("5471");
		else if (codiceMotivo.equals("2282"))
			lEve.getEvento().setCodMotivo("5472");
		// Detenzione domiciliare
		else if (codiceMotivo.equals("2284"))
			lEve.getEvento().setCodMotivo("5473");
		else if (codiceMotivo.equals("2285"))
			lEve.getEvento().setCodMotivo("5474");
		else if (codiceMotivo.equals("2287"))
			lEve.getEvento().setCodMotivo("5475");
		else if (codiceMotivo.equals("2288"))
			lEve.getEvento().setCodMotivo("5476");
		// Semilibertà
		else if (codiceMotivo.equals("2283"))
			lEve.getEvento().setCodMotivo("5477");
		// Det dom term (differimento)
		else if (codiceMotivo.equals("2286"))
			lEve.getEvento().setCodMotivo("5479");
		// Espiazione presso il domicilio
		else if (codiceMotivo.equals("2299"))
			lEve.getEvento().setCodMotivo("5478");

		// ==========================================================================
		// Codici del TDS
		// ==========================================================================
		// Affidamento
		if (codiceMotivo.equals("1200"))
			lEve.getEvento().setCodMotivo("5480");
		else if (codiceMotivo.equals("1201"))
			lEve.getEvento().setCodMotivo("5481");
		else if (codiceMotivo.equals("1202"))
			lEve.getEvento().setCodMotivo("5482");
		// Detenzione domiciliare
		else if (codiceMotivo.equals("1203"))
			lEve.getEvento().setCodMotivo("5483");
		else if (codiceMotivo.equals("1205"))
			lEve.getEvento().setCodMotivo("5484");
		else if (codiceMotivo.equals("1206"))
			lEve.getEvento().setCodMotivo("5485");
		else if (codiceMotivo.equals("1207"))
			lEve.getEvento().setCodMotivo("5486");
		// Semilibertà
		else if (codiceMotivo.equals("1208"))
			lEve.getEvento().setCodMotivo("5487");
		// Indultino (????) da stabilire NON PREVISTO
		// else if(codiceMotivo.equals("1211"))
		// lEve.getEvento().setCodMotivo("5477");
		// Det dom term (differimento)
		else if (codiceMotivo.equals("1204"))
			lEve.getEvento().setCodMotivo("5490");
		// Espiazione presso il domicilio
		else if (codiceMotivo.equals("1209"))
			lEve.getEvento().setCodMotivo("5489");

		lEve.setEvento(setEventoProvvedimentoMisuraAlternativa(lEve.getEvento()));
		lEve.getMagistrato().setCodMagistrato(this.calcolaMagistrato());
		lEve.getEvento().setEveIdEvento(lIdOrdinanza);

		// Inserisco l'array di Notifiche nell'Evento
		NotificaModel[] lNotifiche = this.setNotificheMisuraAlternativa();
		lEve.setNotifiche(lNotifiche);

		// Inserisco la pena contestualmente al provvedimento. La pena è stata
		// calcolata e messa in sessione in fase di registrazione/aggancio
		// dell'ordinanza
		// n.b. solo se MDS
		PenaResiduaModel lPenaRes = (PenaResiduaModel) this.getSessionAttribute("MAPenaResiduaRideterminata");

		// n.b. lPenaRes.setIdPenaResidua(null) forza la funzione ad inserire un nuovo
		// record pena residua.
		// n.b. la PR in sessione potrebbe essere una pena rideterminata o solo
		// l'ultima PR a ristema. Inoltre potrebbe essere
		if ("S".equals(lPenaRes.getFlagValidato())) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("lPenaRes = " + lPenaRes);
			lPenaRes.setIdPenaResidua(null);
		}

		// recupero il fine pena eventualmente modificata in maschera
		if (!isRequestParameterNullObj(ICostantiPenaResidua.CAMPO_ANNO_DATA_FINE))
			lPenaRes.setDataFine(getRequestDateParameter(ICostantiPenaResidua.CAMPO_ANNO_DATA_FINE,
					ICostantiPenaResidua.CAMPO_MESE_DATA_FINE, ICostantiPenaResidua.CAMPO_GIORNO_DATA_FINE));

		// rimuovo la pena dalla sessione (verificare se è il caso di farlo qui)
		this.removeSessionAttribute("MAPenaResiduaRideterminata");

		// ======================================================================
		//
		// ======================================================================
		EventoNotificaModel lRetModel = new EventoNotificaModel();
		lRetModel = lMisAltCtrl.ExInserisciOModificaMANotifica(lEve, lPenaRes, lProsecuMAMod, null);

		lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
				+ "=siap.siep.misuraalternativa.action.ActDettaglioMAProsecuzione51Bis&"
				+ ICostantiEvento.CAMPO_ID_EVENTO + "=" + lRetModel.getEvento().getIdEvento();

		return lPage;
	}

	/**
	 * Effettua il calcolo della nuova pena residua a partire della pena a sistema e dai dati Inizio
	 * pena/Inizio misura digitati in maschera
	 */
	@SuppressWarnings("rawtypes")
	private PenaResiduaModel calcolaNuovaPena(PenaResiduaModel aPenMod, BigDecimal aIdFascicoloSiep)
			throws F3BException {

		PenaResiduaModel lNuovaPenaModel = null;
		lNuovaPenaModel = new PenaResiduaModel(aPenMod);

		Date lDataFinePena = null;
		Date lDataInizioArresto = null;
		Date lDataFineReclusione = null;

		// FIXME gestire anche inizio pena in maschera
		Date lDataInizioMisura = getRequestDateParameter(
				ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_INIZIO_MISURA,
				ICostantiMisuraAlternativa.CAMPO_MESE_DATA_INIZIO_MISURA,
				ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_INIZIO_MISURA);
		Date lDataInizio = lDataInizioMisura;

		// La data inizio misura non è più obbligatoria quindi se non presente esco
		// senza effettuare il calcolo
		if (lDataInizio == null)
			return null;

		lNuovaPenaModel.setDataInizio(lDataInizio);

		if (lNuovaPenaModel.getFlagErgastolo().equals("N")) {
			ICalcoloPena ICalPen = SIEPLookupRemote.getCalcoloPenaRemote();
			Vector lVectFine = ICalPen.exCalcolaDataFinePena(lDataInizio, lNuovaPenaModel, true); // il flag
																									// true
																									// indica
																									// CON
																									// DIES_A_QUO

			if (lVectFine.size() == 0)
				throw new F3BException(F3BException.USER_MESSAGE,
						"Impossibile calcolare la data di Fine Pena");

			if (lVectFine.size() == 1)
				lDataFinePena = (Date) lVectFine.get(0);

			if (lVectFine.size() == 2) { // Ho sia Reclusione che Arresto
				lDataFineReclusione = (Date) lVectFine.get(0);
				lDataFinePena = (Date) lVectFine.get(1); // fine arresto
				lDataInizioArresto = DateUtils.moveDateTo(lDataFineReclusione, Calendar.DAY_OF_MONTH, 1);

				lNuovaPenaModel.setDataFineReclusione(lDataFineReclusione);
				lNuovaPenaModel.setDataInizioArresto(lDataInizioArresto);
			}

			// ==========================================================================
			// Anticipo il fine pena se presenti LA
			// n.b. vengono anticipati sia il fine reclusione che inizio arresto che
			// fine pena. Se fine reclusione<data inizio viene eliminato
			// ==========================================================================
			// ==========================================================================
			// Recupero i dati della pena: mi servono le LA da scomputare
			// ==========================================================================
			CalcoloPenaModel lCalcoloPenaMod = null;
			ActCalcoloPenaMain lActCalcolaPenaMain = new ActCalcoloPenaMain();
			lCalcoloPenaMod = lActCalcolaPenaMain.calcoloPena(aIdFascicoloSiep, null);

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Recupero LA per anticipazione fine pena");
			if (lCalcoloPenaMod.getLiberazioneAnticipata() > 0 && lDataFinePena != null) {
				lDataFinePena = DateUtils.moveDateTo(lDataFinePena, java.util.Calendar.DAY_OF_MONTH,
						-lCalcoloPenaMod.getLiberazioneAnticipata());

				// Arretro le data fine reclusione, inizio arresto
				if (lNuovaPenaModel.getDataFineReclusione() != null) {
					lNuovaPenaModel.setDataFineReclusione(
							DateUtils.moveDateTo(lNuovaPenaModel.getDataFineReclusione(),
									Calendar.DAY_OF_MONTH, -lCalcoloPenaMod.getLiberazioneAnticipata()));
				}

				if (lNuovaPenaModel.getDataInizioArresto() != null) {
					lNuovaPenaModel
							.setDataInizioArresto(DateUtils.moveDateTo(lNuovaPenaModel.getDataInizioArresto(),
									Calendar.DAY_OF_MONTH, -lCalcoloPenaMod.getLiberazioneAnticipata()));
				}

				// Azzero le date se per effetto delle LA sono arretrate oltre la data inizio pena
				if (lNuovaPenaModel.getDataFineReclusione() != null
						&& DateUtils.isGreater(lDataInizio, lNuovaPenaModel.getDataFineReclusione())) {
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger.debug("Data fine reclusione < data inizio pena per effetto arretramento LA");
					lNuovaPenaModel.setDataFineReclusione(null);
				}

				if (lNuovaPenaModel.getDataInizioArresto() != null
						&& DateUtils.isGreater(lDataInizio, lNuovaPenaModel.getDataInizioArresto())) {
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger.debug("Data inizio arresto < data inizio pena per effetto arretramento LA");
					lNuovaPenaModel.setDataInizioArresto(null);
				}
			}

			lNuovaPenaModel.setDataFinePresunta(lDataFinePena);
		} else // inizio ergastolo
		{
			Date lDataFinePenaErga = new Date();
			lDataFinePenaErga = DateUtils.getDate(9999, 12, 31);
			lNuovaPenaModel.setDataFinePresunta(lDataFinePenaErga);
		} // fine ergastolo

		lNuovaPenaModel.setDataFine(null); // = DataFinePresunta se l'utente non modificherà il fine pena in
											// form
		lNuovaPenaModel.setEveIdEvento(null);

		// Se l'ultima pena residua da cui si è partiti risulta validata, imposto
		// il flag
		if (("S").equals(lNuovaPenaModel.getFlagValidato())) {
			lNuovaPenaModel.setFlagValidato("N");

			lNuovaPenaModel.setCodUfficioInserimento(this.getCodUfficioUtenteConnesso());
			lNuovaPenaModel.setCodOperatoreInserimento(this.getCodUtenteConnesso());
			lNuovaPenaModel.setDataInserimento(DateUtils.getSysDate());

			lNuovaPenaModel.setCodUfficioAggiornamento(null);
			lNuovaPenaModel.setCodOperatoreAggiornamento(null);
			lNuovaPenaModel.setDataAggiornamento(null);
		}

		if (lNuovaPenaModel.getFlagValidato().equals("N")) {
			lNuovaPenaModel.setCodUfficioAggiornamento(this.getCodUfficioUtenteConnesso());
			lNuovaPenaModel.setCodOperatoreAggiornamento(this.getCodUtenteConnesso());
			lNuovaPenaModel.setDataAggiornamento(DateUtils.getSysDate());
		}

		return lNuovaPenaModel;
	}

	/**
	 * Imposta eventualmente il fine misura nel caso di prosecuzione del Differimento nelle forme della
	 * Detenzione Domiciliare
	 *
	 * @param aMisModel
	 * @throws Exception
	 */
	private void setDataFineMisura(MisuraAlternativaModel aMisModel, String aTipoUff) {

		// Se la data è stata digita non è necessario calcolarla
		try {
			if (aMisModel.getDataFineMisura() != null) {
				return;
			} else {
				BigDecimal lGiorniMisura = aMisModel.getNumGiorniMisura();
				BigDecimal lMesiMisura = aMisModel.getNumMesiMisura();
				BigDecimal lAnniMisura = aMisModel.getNumAnniMisura();

				Date lDataInizio = null;
				Date lDataFineMisura = null;

				// Data inizio = data scarcerazione se digitato oppure data emissione provvedimento
				if ("TDS".startsWith(aTipoUff)) {
					if (aMisModel.getDataScarcerazione() != null) {
						lDataInizio = aMisModel.getDataScarcerazione();
					} else {
						lDataInizio = getRequestDateParameter(ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE,
								ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE,
								ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE);
					}
				} else { // UDS
					lDataInizio = aMisModel.getDataInizioMisura();
				}

				CalendarModel lCalMod = new CalendarModel();

				lCalMod.setNumGiorni(lGiorniMisura);
				lCalMod.setNumMesi(lMesiMisura);
				lCalMod.setNumAnni(lAnniMisura);

				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("Inizio calcolo fine Misura con data Inizio = "
						+ DateUtils.getDateToString(lDataInizio, "dd-MM-yyyy") + " e quantum = "
						+ lCalMod.getStringPerStampa());

				ICalcoloPena lCtrlCalcolo = SIEPLookupRemote.getCalcoloPenaRemote();
				lDataFineMisura = lCtrlCalcolo.exCalcolaNuovaDataFine(lDataInizio, lCalMod, true);

				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("Fine Misura Calcolato = "
						+ DateUtils.getDateToString(lDataFineMisura, "dd-MM-yyyy"));

				aMisModel.setDataFineMisura(lDataFineMisura);
			}
		} catch (Exception e) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("Eccezione in fase di calcolo del fine misura: ", e);
			// FIXME gestire l'eccezione
		}

	}

}