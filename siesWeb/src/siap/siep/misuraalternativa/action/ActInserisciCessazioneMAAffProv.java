package siap.siep.misuraalternativa.action;

/**
 * <p>Title: ActInserisciCessazioneMAAffProv</p>
 * <p>Description: Classe Action per l'inserimento di MisuraAlternativa di Revoca Affidamento in Prova/Indultino</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: Bull</p>
 * @version 1.0
 */

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

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
import siap.sius.depositoordinanzapc.model.DepositoOrdinanzaPcModel;
import siap.sius.tenore.model.TenoreModel;

public class ActInserisciCessazioneMAAffProv extends ActMisuraAlternativa
		implements ICostantiMisuraAlternativa {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	/**
	 * Azione di Inserimento della Revoca dell'affidamento in prova e della revoca dell'Indultino e ricalcola
	 * la pena
	 *
	 * @return Nome della pagina JSP da visualizzare al termine dell'elaborazione
	 * @throws F3BException
	 */
	public String processRequest() throws F3BException {

		/*
		 * Quest'azione viene chiamata due volte, la prima per inserire la misura e calcolare la penaresidua
		 * che metterà in sessione e la seconda volta per inserire il provvedimento e la pena che era in
		 * sessione!!
		 */
		MisuraAlternativaModel lMisMod = new MisuraAlternativaModel();
		EventoNotificaModel lRetModel = new EventoNotificaModel();

		String tipoMisura = getRequestStringParameter("tipomisura");
		FascicoloSiepModel lFascicoloModel = (FascicoloSiepModel) getSessionAttribute("fascicolo");
		String lCodiceOperatore = this.getCodUtenteConnesso();
		String lCodiceUfficio = this.getCodUfficioUtenteConnesso();

		String PosizioneGiu = getRequestStringParameter(
				ICostantiPosizioneGiuridica.CAMPO_COD_POSIZIONE_GIURIDICA);
		this.setRequestAttribute("posizionegiuridica", PosizioneGiu);
		PosizioneGiuridicaModel lPosMod = new PosizioneGiuridicaModel();
		lPosMod.setCodPosizioneGiuridica(PosizioneGiu);

		BigDecimal lIdPenaRes = getRequestBigDecimalParameter(ICostantiPenaResidua.CAMPO_ID_PENA_RESIDUA);
		IMisuraAlternativa lMisAltCtrl = SICOLookupRemote.getMisuraAlternativaRemote();

		/***************************
		 * INIZIO CALCOLO DELLA PENA PER LA REVOCA
		 ***********************************/
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

		boolean isPenaRicalcolata = true;
		// se la penaresidua non è in sessione vuol dire che la devo ancora calcolare e quindi la calcolo
		if (this.isSessionAttributeNullObj("REMApenaresidua")) {
			//// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			//// LogF3B.getLogger()
			// siesLogger.debug("REMApenaresidua non in sessione, inizio calcolo.... ");
			isPenaRicalcolata = false;
			String lPeriodoEspiato = null;

			BigDecimal lIdFasc = lFascicoloModel.getIdFascicoloSiep();
			if (!this.isRequestParameterNullObj(ICostantiMisuraAlternativa.CAMPO_FLAG_PERIODO_ESPIATO)
					&& getRequestStringParameter(
							ICostantiMisuraAlternativa.CAMPO_FLAG_PERIODO_ESPIATO) != null
					&& !getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_FLAG_PERIODO_ESPIATO)
							.equals("")) {
				lPeriodoEspiato = getRequestStringParameter(
						ICostantiMisuraAlternativa.CAMPO_FLAG_PERIODO_ESPIATO);
			}

			Date lDataDalRevoca = null;
			if (!this.isRequestParameterNullObj(ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_INIZIO_REVOCA)) { // Misura
																												// Alternativa
																												// Cessata
																												// dal
				lDataDalRevoca = getRequestDateParameter(
						ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_INIZIO_REVOCA,
						ICostantiMisuraAlternativa.CAMPO_MESE_DATA_INIZIO_REVOCA,
						ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_INIZIO_REVOCA);
			}

			boolean oldPenaResidua = false;
			// Se non sono stati specificati ne i nuovi quantum ne la data di revoca
			// non viene effettuato il calcolo della pena e vale quella a sistema
			if (lAnniReclusioneRE == null && lGiorniReclusioneRE == null && lMesiReclusioneRE == null
					&& lAnniArrestoRE == null && lGiorniArrestoRE == null && lMesiArrestoRE == null
					&& lDataDalRevoca == null) {
				// if(tipoMisura != null && (tipoMisura.equals("ESP_PRESSO_DOM") ||
				// tipoMisura.equals("INDULTINO")))
				if (tipoMisura != null && (tipoMisura.equals("ESP_PRESSO_DOM")
						|| tipoMisura.equals("INDULTINO") || tipoMisura.equals("AFFIDAMENTO")))
					oldPenaResidua = true;
			}

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
			// lStatoDetenuto = 5 = default nessun calcolo
			// Attenzione lStatoDetenuto = utilizzato per il calcolo della pena, i
			// valori 0 e 1 sono equivalenti

			if (tipoMisura != null && tipoMisura.equals("AFFIDAMENTO")) {
				// In sospensione (51 bis o 51 ter)
				if (PosizioneGiu.equals("32") || PosizioneGiu.equals("37")) {
					lStatoDetenuto = 0;
				}

				// In misura (eventualmente provvisoria 54)
				if (PosizioneGiu.equals("13") || PosizioneGiu.equals("54")) {
					lStatoDetenuto = 1;
				}

				// is Libero()
				if (PosizioneGiu.equals("07") || PosizioneGiu.equals("10") || PosizioneGiu.equals("16")
						|| PosizioneGiu.equals("17") || PosizioneGiu.equals("46") || PosizioneGiu.equals("47")
						|| PosizioneGiu.equals("20") || PosizioneGiu.equals("26")
						|| PosizioneGiu.equals("30")) {
					lStatoDetenuto = 2;
				}
			} else if (tipoMisura != null && tipoMisura.equals("INDULTINO")) {
				// In sospensione (51 bis o 51 ter)
				if (PosizioneGiu.equals("35") || PosizioneGiu.equals("40")) {
					lStatoDetenuto = 0;
				}

				// In misura
				if (PosizioneGiu.equals("27")) {
					lStatoDetenuto = 1;
				}

				// isLibero
				if (PosizioneGiu.equals("07") || PosizioneGiu.equals("10") || PosizioneGiu.equals("16")
						|| PosizioneGiu.equals("17") || PosizioneGiu.equals("46") || PosizioneGiu.equals("47")
						|| PosizioneGiu.equals("20") || PosizioneGiu.equals("26")
						|| PosizioneGiu.equals("30")) {
					lStatoDetenuto = 2;
				}
			}
			// 02/11/2010 Gestione Revoca Espiazione Pena presso Domicilio
			else if (tipoMisura != null && tipoMisura.equals(ICostantiMisuraAlternativa.ESP_PRESSO_DOM)) {
				// In sospensione (51 bis o 51 ter)
				if (PosizioneGiu.equals("51") || PosizioneGiu.equals("52")) {
					lStatoDetenuto = 0;
				}

				// In misura
				if (PosizioneGiu.equals("50")) {
					lStatoDetenuto = 1;
				}

				// isLibero()
				if (PosizioneGiu.equals("07") || PosizioneGiu.equals("10") || PosizioneGiu.equals("16")
						|| PosizioneGiu.equals("17") || PosizioneGiu.equals("46") || PosizioneGiu.equals("47")
						|| PosizioneGiu.equals("20") || PosizioneGiu.equals("26")
						|| PosizioneGiu.equals("30")) {
					lStatoDetenuto = 2;
				}
			}

			// Recupero il flag detenuto/non detenuto
			String lDetenuto = null;
			if (!lPosMod.isLibero()) {
				if (this.getRequestStringParameter("tipo").equals("detenuto"))
					lDetenuto = "S";
				else
					lDetenuto = "N";
			} else {
				lDetenuto = "N";
			}

			// Detenuto dal
			Date lDataNuovoInPe = null;
			if (!this.isRequestParameterNullObj(
					ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_INGRESSO_ISTITUTO)) {
				lDataNuovoInPe = getRequestDateParameter(CAMPO_ANNO_DATA_INGRESSO_ISTITUTO,
						CAMPO_MESE_DATA_INGRESSO_ISTITUTO, CAMPO_GIORNO_DATA_INGRESSO_ISTITUTO);
			}

			PenaResiduaModel lPenMod = new PenaResiduaModel();

			//// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			//// LogF3B.getLogger()
			// siesLogger.debug("Recupero la situazione attuale della Pena (dati Validati)");
			ActCalcoloPenaMain lCalcoloPenaMain = new ActCalcoloPenaMain();
			CalcoloPenaModel lCalcoloPenaModel = lCalcoloPenaMain.calcoloPena(lIdFasc, null);

			ICalcoloPena lCtrlCalPen = SIEPLookupRemote.getCalcoloPenaRemote();
			CalcoloPenaModel lCalcoloPenaModelRet = lCtrlCalPen.exCalcoloRevocheMisureAlternative(lIdFasc,
					lDataDalRevoca, lCalModRec, lCalModArr, lPeriodoEspiato, lStatoDetenuto, lDataNuovoInPe,
					lDetenuto, lCalcoloPenaModel);

			// Per INDULTINO o ESP_PRESSO_DOM è possibile non inserire alcun dato relativo alla nuova pena
			// residua
			// In quel caso occorre riferirsi alla vecchia pena residua
			if (oldPenaResidua) {
				IPenaResidua lPenResCtrl = SIEPLookupRemote.getPenaResiduaRemote();
				PenaResiduaModel lastPenaResidua = new PenaResiduaModel();
				lastPenaResidua = lPenResCtrl.ExRicercaPenaResiduaByKey(lIdPenaRes);
				lCalcoloPenaModelRet.setPenaResiduaRicalcolata(lastPenaResidua);
			}

			lPenMod = lCalcoloPenaModelRet.getPenaResiduaRicalcolata();

			lPenMod.setCodOperatoreInserimento(getCodUtenteConnesso());
			lPenMod.setCodUfficioInserimento(getCodUfficioUtenteConnesso());

			if (lPenMod != null && lPenMod.getMessage() != null && lPenMod.getMessage().equals("Errore")) {
				throw new SIEPException(SIEPException.USER_MESSAGE,
						"Impossibile effettuare il calcolo della pena:quantum negativo.");
			} else {
				//// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				//// LogF3B.getLogger()
				// siesLogger.debug("REMApenaresidua = "+lCalcoloPenaModelRet);
				//// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				//// LogF3B.getLogger()
				// siesLogger.debug("getPenaResiduaRicalcolata =
				//// "+lCalcoloPenaModelRet.getPenaResiduaRicalcolata());

				this.setSessionAttribute("REMApenaresidua", lCalcoloPenaModelRet);
			}
		}

		/***************************
		 * FINE CALCOLO DELLA PENA PER LA REVOCA
		 ***********************************/

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("FINE CALCOLO DELLA PENA PER LA REVOCA");

		String lPage = null;
		MisuraAlternativaModel lrevoca = null;
		BigDecimal lIdOrdinanza = getRequestBigDecimalParameter(
				ICostantiMisuraAlternativa.CAMPO_ID_DOCUMENTO_SIUS);

		// EventoNotificaModel lEveNotMod = new EventoNotificaModel();
		if (lIdOrdinanza != null && !lIdOrdinanza.toString().equals(""))
			lrevoca = lMisAltCtrl.ExRicercaMisuraAlternativaByIdEvento(lIdOrdinanza);

		if (lrevoca == null && !isPenaRicalcolata) {
			// MA non a sistema e ho effettuato il calcolo della pena
			// isPenaRicalcolata = false: quindi sto nella prima chiamata alla Action, devo
			// inserire i dati del provvedimento SIUS
			// lrevoca = null: il provvedimento non è stato selezionato dalla lista,
			// oppure ne sono stati modificati i dati
			//// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			// siesLogger.debug("inserisco evento del TDS");

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
			lEveMod.setEvento(setEventoOrdinanzaDecretoMisuraAlternativa(lEveMod.getEvento(), lTipoDecisione,
					lCodiceUffEmi, lComModAutEmi, lDataEmisTras));

			//// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			//// LogF3B.getLogger()
			// siesLogger.debug("setto il deposito ordinanza");

			DepositoOrdinanzaPcModel lDepOrdMod = null;
			// DepositoDecretoModel lDepDescr = null;
			// setto il deposito ordinanza
			// if (lTipoDecisione.equals("03"))
			lDepOrdMod = setDepositoOrdinanzaPc(lCodiceUffEmi);
			// else if (lTipoDecisione.equals("02"))
			// lDepDescr = setDepositoDecreto(lCodiceUffEmi);

			//// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			//// LogF3B.getLogger()
			// siesLogger.debug("setto il tenore");
			String lTipoUffSorv = getRequestStringParameter(
					ICostantiMisuraAlternativa.CAMPO_COD_UFFICIO_SORVEGLIANZA);

			// setto il tenore // Daniela 16.12.2010 modificato da 0006 a 0018
			// ESITO_TENORE
			// TDS = 0018 = Dichiara Non Validamente Espiata la Pena
			// MDS (51bis) = 0010 = Dichiara inefficace/cessata la misura
			// TDS (51bis) = 0348 - Accoglie il reclamo del PM e dichiara cessata la misura
			TenoreModel lTenMod = null; // setTenore(new BigDecimal(1),"0018");
			if (lTipoUffSorv.startsWith("UDS")) {
				lTenMod = setTenore(new BigDecimal(1), "0010"); // U037
			} else if (lTipoUffSorv.startsWith("TDS")) {
				List<String> lCodiciTDS51Bis = Arrays.asList("1200", "1201", "1202", "1203", "1204", "1205",
						"1206", "1207", "1208", "1209");

				if (lCodiciTDS51Bis.contains(lEveMod.getEvento().getCodMotivo()))
					lTenMod = setTenore(new BigDecimal(1), "0348"); // C045
				else
					lTenMod = setTenore(new BigDecimal(1), "0018"); // C008
			}

			//// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			//// LogF3B.getLogger()
			// siesLogger.debug("misura alternativa");

			// misura alternativa
			String lUfficioScarc = "-";
			if (!lPosMod.isLibero()) {
				if (this.getRequestStringParameter("tipo").equals("detenuto"))
					lUfficioScarc = "SORV";
				else if (this.getRequestStringParameter("tipo").equals("nondetenuto"))
					lUfficioScarc = "PROC";
			}

			// lTipoDecisione = 03 = Ordinanza o 02; CE = Cessazione
			lMisMod = setMisuraAlternativa(lTipoDecisione, "CE", lCodiceUffEmi,
					getRequestStringParameter(ICostantiEvento.CAMPO_COD_MOTIVO), lUfficioScarc);

			// n.b. Data Inizio Misura = data revoca o data inizio misura sospensione
			if (!this.isRequestParameterNullObj(ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_INIZIO_REVOCA)) {
				// FIXME perchè la data di revoca viene considerata come data inizio misura
				lMisMod.setDataInizioMisura(
						getRequestDateParameter(ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_INIZIO_REVOCA,
								ICostantiMisuraAlternativa.CAMPO_MESE_DATA_INIZIO_REVOCA,
								ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_INIZIO_REVOCA));
				lMisMod.setDataInizioRevoca(lMisMod.getDataInizioMisura());
			} else if (!this.isRequestParameterNullObj("ggnuovoiniziopena")
					&& !this.isRequestParameterNullObj("yyyynuovoiniziopena")) { // Se presente la sospensione
																					// recuperao la
																					// DataInizioMisura della
																					// Sospensione
				lMisMod.setDataInizioMisura(getRequestDateParameter("yyyynuovoiniziopena",
						"MMnuovoiniziopena", "ggnuovoiniziopena"));
			} else if (!this.isRequestParameterNullObj(
					ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_INGRESSO_ISTITUTO)) {
				lMisMod.setDataInizioMisura(
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

			if (!this.isRequestParameterNullObj(
					ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_INGRESSO_ISTITUTO)) {
				lMisMod.setDataIngressoIstituto(
						getRequestDateParameter(ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_INGRESSO_ISTITUTO,
								ICostantiMisuraAlternativa.CAMPO_MESE_DATA_INGRESSO_ISTITUTO,
								ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_INGRESSO_ISTITUTO));
			}

			if (!this.isRequestParameterNullObj(ICostantiMisuraAlternativa.CAMPO_FLAG_PERIODO_ESPIATO))
				lMisMod.setFlagPeriodoEspiato(
						getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_FLAG_PERIODO_ESPIATO));

			/*
			 * nel momento in cui inserisco la misura alternativa e calcolo la pena richiamo la maschera
			 * d'inserimento di chi mi ha chiamato per visualizzare la seconda parte della maschera ossia i
			 * destinatari
			 */
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("inserisci alternativa");

			MisuraAlternativaModel lMisuraModel = lMisAltCtrl
					.ExInserisciMisuraAlternativaEventoNotifica(lEveMod, lDepOrdMod, lTenMod, lMisMod);

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("inserita alternativa");

			if (tipoMisura != null && tipoMisura.equals("AFFIDAMENTO")) {
				lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
						+ "=siap.siep.misuraalternativa.action.ActLoadInserisciMACessazioneAffProva&"
						+ ICostantiPenaResidua.CAMPO_ID_PENA_RESIDUA + "=" + lIdPenaRes + "&"
						+ ICostantiMisuraAlternativa.CAMPO_ID_DOCUMENTO_SIUS + "="
						+ lMisuraModel.getEveIdEvento();
			} else if (tipoMisura != null && tipoMisura.equals("INDULTINO")) {
				lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
						+ "=siap.siep.misuraalternativa.action.ActLoadInserisciCessazioneIndultino&"
						+ ICostantiPenaResidua.CAMPO_ID_PENA_RESIDUA + "=" + lIdPenaRes + "&"
						+ ICostantiMisuraAlternativa.CAMPO_ID_DOCUMENTO_SIUS + "="
						+ lMisuraModel.getEveIdEvento();
			}
			// 02/11/2010 Gestione cessazione Espiazione Pena presso Domicilio
			else if (tipoMisura != null && tipoMisura.equals(ICostantiMisuraAlternativa.ESP_PRESSO_DOM)) {
				lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
						+ "=siap.siep.misuraalternativa.action.ActLoadInserisciCessazioneEspPressoDom&"
						+ ICostantiPenaResidua.CAMPO_ID_PENA_RESIDUA + "=" + lIdPenaRes + "&"
						+ ICostantiMisuraAlternativa.CAMPO_ID_DOCUMENTO_SIUS + "="
						+ lMisuraModel.getEveIdEvento();
			}
		} else {
			// ========================================================================
			// O La misura esiste o calcolo pena non effettuato
			// Entro qui se sto inserendo il provvedimento SIEP (MA a sistema != null e pena non ricalcolata
			// oppure se MA a sistema in quanto selezionata dalla lista ma sto comunque
			// inserendo/ aggiornando i dati dell'ordinanza
			// ========================================================================
			// Aggiorna la MA nei campi:
			// - COD_TIPO_UFFICIO_SCARCERAZIONE
			// - DATA_INGRESSO_ISTITUTO
			// - NOTE
			// Infatti
			if (!lPosMod.isLibero()) {
				// In maschera sono presenti i radio
				// - Esecuzione contro soggetto non detenuto
				// - Esecuzione contro soggetto detenuto - Data Incarcerazione
				// n.b. tali campi non vengono
				if (this.getRequestStringParameter("tipo").equals("detenuto")) {
					lrevoca.setCodTipoUfficioScarcerazione("SORV");
					lrevoca.setDataIngressoIstituto(getRequestDateParameter(
							ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_INGRESSO_ISTITUTO,
							ICostantiMisuraAlternativa.CAMPO_MESE_DATA_INGRESSO_ISTITUTO,
							ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_INGRESSO_ISTITUTO));
				} else if (this.getRequestStringParameter("tipo").equals("nondetenuto")) {
					lrevoca.setCodTipoUfficioScarcerazione("PROC");
				}
			} else {
				lrevoca.setCodTipoUfficioScarcerazione("-");
			}

			if (!this.isRequestParameterNullObj(ICostantiMisuraAlternativa.CAMPO_NOTE)) {
				lrevoca.setNote(getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_NOTE));
			}

			// ========================================================================
			//
			// ========================================================================
			if (this.isRequestParameterNullObj("presenzanuovopenaricalcolata")) {
				// ======================================================================
				// Sto sulla prima maschera, MA è a sistema (!=null) in quanto selezionata
				// dalla lista, devo aggiornarla nei campi:
				// - COD_TIPO_UFFICIO_SCARCERAZIONE
				// - DATA_INGRESSO_ISTITUTO
				// - NOTE
				// ======================================================================
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("modifica alternativa");

				lMisAltCtrl.ExModificaMisuraAlternativa(lrevoca);

				if (tipoMisura != null && tipoMisura.equals("AFFIDAMENTO")) {
					lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
							+ "=siap.siep.misuraalternativa.action.ActLoadInserisciMACessazioneAffProva&"
							+ ICostantiPenaResidua.CAMPO_ID_PENA_RESIDUA + "=" + lIdPenaRes;
				} else if (tipoMisura != null && tipoMisura.equals("INDULTINO")) {
					lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
							+ "=siap.siep.misuraalternativa.action.ActLoadInserisciCessazioneIndultino&"
							+ ICostantiPenaResidua.CAMPO_ID_PENA_RESIDUA + "=" + lIdPenaRes;
				}
				// 02/11/2010 Gestione Revoca Espiazione Pena presso Domicilio
				else if (tipoMisura != null && tipoMisura.equals(ICostantiMisuraAlternativa.ESP_PRESSO_DOM)) {
					lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
							+ "=siap.siep.misuraalternativa.action.ActLoadInserisciCessazioneEspPressoDom&"
							+ ICostantiPenaResidua.CAMPO_ID_PENA_RESIDUA + "=" + lIdPenaRes;
				}
			} else {
				// ======================================================================
				// Sto in fase di iscrizione del provvedimento SIEP e contestualmente
				// la pena recuperata dalla sessione
				// ======================================================================
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("Fase di Inserimante del Provvedimento SIEP");
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("tipoMisura =" + tipoMisura);
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("PosizioneGiu =" + PosizioneGiu);

				String codiceMotivo = lrevoca.getCodTipoMisura();

				EventoNotificaModel lEve = new EventoNotificaModel();

				if ((tipoMisura != null && tipoMisura.equals("AFFIDAMENTO")
						&& (PosizioneGiu.equals("32") || PosizioneGiu.equals("37") || lPosMod.isLibero()
								|| PosizioneGiu.equals("13") || PosizioneGiu.equals("54")))
						|| (tipoMisura != null && tipoMisura.equals("INDULTINO")
								&& (PosizioneGiu.equals("35") || PosizioneGiu.equals("40")
										|| PosizioneGiu.equals("27") || lPosMod.isLibero()))
						|| (tipoMisura != null && tipoMisura.equals(ICostantiMisuraAlternativa.ESP_PRESSO_DOM)
								&& (PosizioneGiu.equals("51") || PosizioneGiu.equals("52")
										|| PosizioneGiu.equals("50") || lPosMod.isLibero()))) {
					// stesso codice della misura, ovvero oggetto misura
					lEve.getEvento().setCodTipoProvvedimento("06");

					// =====================================
					// Rimappatura Codici MDS DL 146/2013
					// =====================================
					if (codiceMotivo.equals("2281"))
						lEve.getEvento().setCodMotivo("5430"); // Affidamento
					else if (codiceMotivo.equals("2205"))
						lEve.getEvento().setCodMotivo("5431"); // Affidamento
					else if (codiceMotivo.equals("2282"))
						lEve.getEvento().setCodMotivo("5432"); // Affidamento
					// =================================================
					// Rimappatura Codici TDS su Reclamo DL 146/2013
					// =================================================
					else if (codiceMotivo.equals("1200"))
						lEve.getEvento().setCodMotivo("5450"); // Affidamento
					else if (codiceMotivo.equals("1201"))
						lEve.getEvento().setCodMotivo("5451"); // Affidamento
					else if (codiceMotivo.equals("1202"))
						lEve.getEvento().setCodMotivo("5452"); // Affidamento
					else if (codiceMotivo.equals("1209"))
						lEve.getEvento().setCodMotivo("5458"); // Espiazione Presso il Domicilio
					// FIXME gestire Indultino
					// =============================================
					// Vecchi Codici TDS si mantiene la mappatura
					// con gli Oggetti della Sorveglianza
					// =============================================
					else
						lEve.getEvento().setCodMotivo(codiceMotivo);

				} else { // Provvedimento generico (04)
							// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al
							// posto di LogF3B.getLogger()
					siesLogger.warn("Posizione giuridica non gestita: inserisco un provvedimento generico");
					lEve.getEvento().setCodTipoProvvedimento("04"); // 04 - Provvedimento
					lEve.getEvento().setCodMotivo("0000"); // 0000 - Provvedimento Indeterminato
				}

				lEve.setEvento(setEventoProvvedimentoMisuraAlternativa(lEve.getEvento()));
				lEve.getMagistrato().setCodMagistrato(this.calcolaMagistrato());
				lEve.getEvento().setEveIdEvento(lIdOrdinanza);

				NotificaModel[] lNotifiche = this.setNotificheMisuraAlternativa();
				lEve.setNotifiche(lNotifiche);

				// ====================================================
				// Inserisco la pena contestualmente al provvedimento
				// ====================================================
				CalcoloPenaModel lCalcoloPenaModelRet = (CalcoloPenaModel) this
						.getSessionAttribute("REMApenaresidua");
				PenaResiduaModel lPenaRes = lCalcoloPenaModelRet.getPenaResiduaRicalcolata();

				//// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				//// LogF3B.getLogger()
				// siesLogger.debug("Pene recuperara dalla sessione: "+lPenaRes);

				lPenaRes.setIdPenaResidua(null);

				// SOSPENSIONE
				// FIXME la SOSPENSIONE va valorizzata SOLO se la pena viene EFFETTIVAMENTE
				// RIDETERMINATA
				SospensioneModel lSospModel = new SospensioneModel();

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

				lSospModel.setCodOperatoreInserimento(lCodiceOperatore);
				lSospModel.setCodUfficioInserimento(lCodiceUfficio);
				lSospModel.setDataInserimento(DateUtils.getSysDate());

				// rimuovo la pena dalla sessione
				this.removeSessionAttribute("REMApenaresidua");

				if (!isRequestParameterNullObj(ICostantiPenaResidua.CAMPO_ANNO_DATA_FINE)) {
					lPenaRes.setDataFine(getRequestDateParameter(ICostantiPenaResidua.CAMPO_ANNO_DATA_FINE,
							ICostantiPenaResidua.CAMPO_MESE_DATA_FINE,
							ICostantiPenaResidua.CAMPO_GIORNO_DATA_FINE));
				}

				// ======================================================================
				//
				// ======================================================================
				lPenaRes.setFlagValidato("N");

				lPenaRes.setCodOperatoreInserimento(getCodUtenteConnesso());
				lPenaRes.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
				lPenaRes.setDataInserimento(DateUtils.getSysDate());

				lPenaRes.setCodOperatoreAggiornamento(null);
				lPenaRes.setCodUfficioAggiornamento(null);
				lPenaRes.setDataAggiornamento(null);

				//// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				//// LogF3B.getLogger()
				// siesLogger.debug("Pene recuperara dalla sessione dopo aggiornamento: "+lPenaRes);
				lRetModel = lMisAltCtrl.ExInserisciOModificaMANotifica(lEve, lPenaRes, lrevoca, lSospModel);

				lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
						+ "=siap.siep.misuraalternativa.action.ActDettaglioCessazioneMAAffInProva&"
						+ ICostantiEvento.CAMPO_ID_EVENTO + "=" + lRetModel.getEvento().getIdEvento();
			}
		}

		setRequestAttribute("inserimentoCessazione", "SI");
		return lPage;
	}

}