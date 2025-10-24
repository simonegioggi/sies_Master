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
import siap.sico.util.CalendarUtil;
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

/**
 * <p>
 * Title: ActInserisciRevocaMAAffProv
 * </p>
 * <p>
 * Description: Classe Action per l'inserimento della Revoca Affidamento in Prova/Indultino/Espiazione presso
 * il domicilio
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
public class ActInserisciRevocaMAAffProv extends ActMisuraAlternativa implements ICostantiMisuraAlternativa {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	/**
	 * Azione di Inserimento della Revoca dell'affidamento in prova e della revoca dell'Indultino e
	 * dell'espiazione presso il domicilio e ricalcolo della pena
	 *
	 * @return Nome della pagina JSP da visualizzare al termine dell'elaborazione
	 * @throws F3BException
	 */
	public String processRequest() throws Exception {

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

		String lCodiceUffEmi = "";

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
		// Sul primo giro è sicuramente null
		if (this.isSessionAttributeNullObj("REMApenaresidua")) {
			isPenaRicalcolata = false;
			String lPeriodoEspiato = null;
			// Controlla esistenza ufficio
			lCodiceUffEmi = getCodUfficioByCodTipoUfficioDescrComune(
					getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_COD_UFFICIO_SORVEGLIANZA),
					getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_SEDE_TDS_EMITT));

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
			if (!this.isRequestParameterNullObj(ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_INIZIO_REVOCA)) {
				lDataDalRevoca = getRequestDateParameter(
						ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_INIZIO_REVOCA,
						ICostantiMisuraAlternativa.CAMPO_MESE_DATA_INIZIO_REVOCA,
						ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_INIZIO_REVOCA);
			}

			boolean oldPenaResidua = false;
			if (lAnniReclusioneRE == null && lGiorniReclusioneRE == null && lMesiReclusioneRE == null
					&& lAnniArrestoRE == null && lGiorniArrestoRE == null && lMesiArrestoRE == null
					&& lDataDalRevoca == null) { // se dati non indicati in maschera in quanto non
													// obbligatori, non ricalcolo
													// la pena ma utilizzo quella precedente
				if (tipoMisura != null
						&& (tipoMisura.equals("ESP_PRESSO_DOM") || tipoMisura.equals("INDULTINO")
						// || tipoMisura.equals("AFFIDAMENTO") //FIXME 02/2015 test df
						))
					oldPenaResidua = true;
			}

			CalendarModel lCalModRec = new CalendarModel();
			lCalModRec.setNumAnni(lAnniReclusioneRE);
			lCalModRec.setNumGiorni(lGiorniReclusioneRE);
			lCalModRec.setNumMesi(lMesiReclusioneRE);

			CalendarModel lCalModArr = new CalendarModel();
			lCalModArr.setNumAnni(lAnniArrestoRE);
			lCalModArr.setNumGiorni(lGiorniArrestoRE);
			lCalModArr.setNumMesi(lMesiArrestoRE);

			int lStatoDetenuto = 5;

			if (tipoMisura != null && tipoMisura.equals("AFFIDAMENTO")) {
				if (PosizioneGiu.equals("32") || PosizioneGiu.equals("37")) {
					lStatoDetenuto = 0;
				}

				if (PosizioneGiu.equals("13") || PosizioneGiu.equals("54")) {
					lStatoDetenuto = 1;
				}

				if (PosizioneGiu.equals("07") || PosizioneGiu.equals("10") || PosizioneGiu.equals("16")
						|| PosizioneGiu.equals("17") || PosizioneGiu.equals("46") || PosizioneGiu.equals("47")
						|| PosizioneGiu.equals("20") || PosizioneGiu.equals("26")
						|| PosizioneGiu.equals("30")) {
					lStatoDetenuto = 2;
				}
			} else if (tipoMisura != null && tipoMisura.equals("INDULTINO")) {
				if (PosizioneGiu.equals("35") || PosizioneGiu.equals("40")) {
					lStatoDetenuto = 0;
				}

				if (PosizioneGiu.equals("27")) {
					lStatoDetenuto = 1;
				}

				if (PosizioneGiu.equals("07") || PosizioneGiu.equals("10") || PosizioneGiu.equals("16")
						|| PosizioneGiu.equals("17") || PosizioneGiu.equals("46") || PosizioneGiu.equals("47")
						|| PosizioneGiu.equals("20") || PosizioneGiu.equals("26")
						|| PosizioneGiu.equals("30")) {
					lStatoDetenuto = 2;
				}
			}
			// 02/11/2010 Gestione Revoca Espiazione Pena presso Domicilio
			else if (tipoMisura != null && tipoMisura.equals(ICostantiMisuraAlternativa.ESP_PRESSO_DOM)) {
				if (PosizioneGiu.equals("51") || PosizioneGiu.equals("52")) {
					lStatoDetenuto = 0;
				}

				if (PosizioneGiu.equals("50")) {
					lStatoDetenuto = 1;
				}

				if (PosizioneGiu.equals("07") || PosizioneGiu.equals("10") || PosizioneGiu.equals("16")
						|| PosizioneGiu.equals("17") || PosizioneGiu.equals("46") || PosizioneGiu.equals("47")
						|| PosizioneGiu.equals("20") || PosizioneGiu.equals("26")
						|| PosizioneGiu.equals("30")) {
					lStatoDetenuto = 2;
				}
			}

			String lDetenuto = null;
			if (!lPosMod.isLibero()) {
				if (this.getRequestStringParameter("tipo").equals("detenuto"))
					lDetenuto = "S";
				else
					lDetenuto = "N";
			} else {
				lDetenuto = "N";
			}

			Date lDataNuovoInPe = null;
			if (!this.isRequestParameterNullObj(
					ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_INGRESSO_ISTITUTO)) {
				lDataNuovoInPe = getRequestDateParameter(CAMPO_ANNO_DATA_INGRESSO_ISTITUTO,
						CAMPO_MESE_DATA_INGRESSO_ISTITUTO, CAMPO_GIORNO_DATA_INGRESSO_ISTITUTO);
			}

			PenaResiduaModel lPenMod = new PenaResiduaModel();

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Recupero la situazione attuale della Pena (dati Validati)");
			ActCalcoloPenaMain lCalcoloPenaMain = new ActCalcoloPenaMain();
			CalcoloPenaModel lCalcoloPenaModel = lCalcoloPenaMain.calcoloPena(lIdFasc, null);

			ICalcoloPena lCtrlCalPen = SIEPLookupRemote.getCalcoloPenaRemote();
			CalcoloPenaModel lCalcoloPenaModelRet = lCtrlCalPen.exCalcoloRevocheMisureAlternative(lIdFasc,
					lDataDalRevoca, lCalModRec, lCalModArr, lPeriodoEspiato, lStatoDetenuto, lDataNuovoInPe,
					lDetenuto, lCalcoloPenaModel);

			// Per INDULTINO o ESP_PRESSO_DOM è possibile non inserire alcun dato relativo
			// alla nuova pena residua. In quel caso occorre riferirsi alla vecchia pena residua
			if (oldPenaResidua) {
				IPenaResidua lPenResCtrl = SIEPLookupRemote.getPenaResiduaRemote();
				PenaResiduaModel lastPenaResidua = new PenaResiduaModel();
				lastPenaResidua = lPenResCtrl.ExRicercaPenaResiduaByKey(lIdPenaRes);
				lCalcoloPenaModelRet.setPenaResiduaRicalcolata(lastPenaResidua);
			}

			lPenMod = lCalcoloPenaModelRet.getPenaResiduaRicalcolata();

			lPenMod.setCodOperatoreInserimento(getCodUtenteConnesso());
			lPenMod.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
			// correzione d.f. 16/02/2015 la data inserimento va sempre aggiornata
			// altrimenti le funzioni che recuperano l'ultima PR per data ins. rischiano
			// di beccare il vecchio record.
			lPenMod.setDataInserimento(DateUtils.getSysDate());

			// =========================================================================
			// Test d.f. 17/02/2015 provo a calcolare l'espiato anche nel caso in cui
			// l'utente non abbia indicato la data revoca ma solo i quantum revocati
			// =========================================================================
			if (lDataDalRevoca == null && (!lCalModRec.isQuantumZero() || !lCalModArr.isQuantumZero())) {
				// non è stata indicata la data revoca ma solo i quantum, in questo caso
				// il metodo exCalcoloRevocheMisureAlternative non calcola l'espiato
				// utile per il record SOSPENSIONE
				CalendarUtil lCalendarUtil = new CalendarUtil();
				// PenaResiduaModel lPenaAttuale = lCalcoloPenaModel.getPenaDaEspiare(null, null, null,null);
				PenaResiduaModel lPenaAttuale = null;
				IPenaResidua lPenResCtrl = SIEPLookupRemote.getPenaResiduaRemote();
				lPenaAttuale = lPenResCtrl
						.ExRicercaPenaResiduaUltimaValidata(lFascicoloModel.getIdFascicoloSiep());

				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("lPenaAttuale = " + lPenaAttuale);

				CalendarModel lTotQuantumPrima = lCalendarUtil
						.sommaGiorni(lPenaAttuale.getQuantumReclusione(), lPenaAttuale.getQuantumArresto());

				CalendarModel lTotQuantumDopo = lCalendarUtil.sommaGiorni(lPenMod.getQuantumReclusione(),
						lPenMod.getQuantumArresto());

				CalendarModel lPenaEspiata = lCalendarUtil.sottraiGiorni(lTotQuantumPrima, lTotQuantumDopo);

				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("lPenaEspiata = " + lPenaEspiata.getStringPerStampa());
				lCalcoloPenaModel.setPenaEspiata(lPenaEspiata);
			}

			if (lPenMod != null && lPenMod.getMessage() != null && lPenMod.getMessage().equals("Errore")) {
				throw new SIEPException(SIEPException.USER_MESSAGE,
						"Impossibile effettuare il calcolo della pena:quantum negativo.");
			} else {
				this.setSessionAttribute("REMApenaresidua", lCalcoloPenaModelRet);
			}
		}

		/***************************
		 * FINE CALCOLO DELLA PENA PER LA REVOCA
		 ***********************************/

		String lPage = null;
		MisuraAlternativaModel lrevoca = null;
		BigDecimal lIdOrdinanza = getRequestBigDecimalParameter(
				ICostantiMisuraAlternativa.CAMPO_ID_DOCUMENTO_SIUS);

		// EventoNotificaModel lEveNotMod = new EventoNotificaModel();
		if (lIdOrdinanza != null && !lIdOrdinanza.toString().equals("")) {
			lrevoca = lMisAltCtrl.ExRicercaMisuraAlternativaByIdEvento(lIdOrdinanza);
		}

		if (lrevoca == null && !isPenaRicalcolata) {
			// ========================================================================
			// lrevoca == null - Solo sul primo giro se Ordinanza NON selezionata dalla lista
			// isPenaRicalcolata = false - Solo sul primo giro (TEST INUTILE)
			// ========================================================================
			// inserisco evento del TDS
			// ========================================================================
			EventoNotificaModel lEveMod = new EventoNotificaModel();
			lEveMod.getEvento().setCodMotivo(getRequestStringParameter(ICostantiEvento.CAMPO_COD_MOTIVO));
			// String lCodiceUffEmi =
			// getCodUfficioByCodTipoUfficioDescrComune(getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_COD_UFFICIO_SORVEGLIANZA),getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_SEDE_TDS_EMITT));
			ComuneModel lComModAutEmi = new ComuneModel(getCodComuneByDescr(
					getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_SEDE_TDS_EMITT)));
			Date lDataEmisTras = getRequestDateParameter(ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_DECISIONE,
					ICostantiMisuraAlternativa.CAMPO_MESE_DATA_DECISIONE,
					ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_DECISIONE);
			lEveMod.setEvento(setEventoOrdinanzaDecretoMisuraAlternativa(lEveMod.getEvento(), "03",
					lCodiceUffEmi, lComModAutEmi, lDataEmisTras));

			// setto il deposito ordinanza
			DepositoOrdinanzaPcModel lDepOrdMod = setDepositoOrdinanzaPc(lCodiceUffEmi);

			// setto il tenore
			TenoreModel lTenMod = setTenore(new BigDecimal(1), "0006");

			// misura alternativa
			String lUfficioScarc = "-";
			if (!lPosMod.isLibero()) {
				if (this.getRequestStringParameter("tipo").equals("detenuto"))
					lUfficioScarc = "SORV";
				else if (this.getRequestStringParameter("tipo").equals("nondetenuto"))
					lUfficioScarc = "PROC";
			}

			lMisMod = setMisuraAlternativa("03", "RE", lCodiceUffEmi,
					getRequestStringParameter(ICostantiEvento.CAMPO_COD_MOTIVO), lUfficioScarc);

			// FIXME Revoca MA:d.f. 13/03/2015 che senso ha questo codice? Se stiamo facendo una revoca
			// per quale motivo cerchiamo di determinare la data inizio misura valorizzandola
			// con dati incongruenti? (dtaInizioRevoca, DataInizioPena, DataIngressoIstituto)
			if (!this.isRequestParameterNullObj(ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_INIZIO_REVOCA)) {
				// FIXME Revoca MA: che senso ha valorizzare la data inizio misura con la data revoca?
				lMisMod.setDataInizioMisura(
						getRequestDateParameter(ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_INIZIO_REVOCA,
								ICostantiMisuraAlternativa.CAMPO_MESE_DATA_INIZIO_REVOCA,
								ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_INIZIO_REVOCA));
				lMisMod.setDataInizioRevoca(lMisMod.getDataInizioMisura()); // paolo cherubini 05/11/2011
																			// aggiungo anche la data inizio
																			// revoca
			} else if (!this.isRequestParameterNullObj("ggnuovoiniziopena")
					&& !this.isRequestParameterNullObj("yyyynuovoiniziopena")) {
				lMisMod.setDataInizioMisura(getRequestDateParameter("yyyynuovoiniziopena",
						"MMnuovoiniziopena", "ggnuovoiniziopena"));
			} else if (!this.isRequestParameterNullObj(
					ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_INGRESSO_ISTITUTO)) {
				lMisMod.setDataInizioMisura(
						getRequestDateParameter(ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_INGRESSO_ISTITUTO,
								ICostantiMisuraAlternativa.CAMPO_MESE_DATA_INGRESSO_ISTITUTO,
								ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_INGRESSO_ISTITUTO));
			}

			lMisMod.setNumAnniRevocaReclusione(lAnniReclusioneRE);
			lMisMod.setNumGiorniRevocaReclusione(lGiorniReclusioneRE);
			lMisMod.setNumMesiRevocaReclusione(lMesiReclusioneRE);
			lMisMod.setNumAnniRevocaArresto(lAnniArrestoRE);
			lMisMod.setNumGiorniRevocaArresto(lGiorniArrestoRE);
			lMisMod.setNumMesiRevocaArresto(lMesiArrestoRE);

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
			MisuraAlternativaModel lMisuraModel = lMisAltCtrl
					.ExInserisciMisuraAlternativaEventoNotifica(lEveMod, lDepOrdMod, lTenMod, lMisMod);
			if (tipoMisura != null && tipoMisura.equals("AFFIDAMENTO")) {
				lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
						+ "=siap.siep.misuraalternativa.action.ActLoadInserisciMARevocaAffProva&"
						+ ICostantiPenaResidua.CAMPO_ID_PENA_RESIDUA + "=" + lIdPenaRes + "&"
						+ ICostantiMisuraAlternativa.CAMPO_ID_DOCUMENTO_SIUS + "="
						+ lMisuraModel.getEveIdEvento();
			} else if (tipoMisura != null && tipoMisura.equals("INDULTINO")) {
				lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
						+ "=siap.siep.misuraalternativa.action.ActLoadInserisciMARevocaIndultino&"
						+ ICostantiPenaResidua.CAMPO_ID_PENA_RESIDUA + "=" + lIdPenaRes + "&"
						+ ICostantiMisuraAlternativa.CAMPO_ID_DOCUMENTO_SIUS + "="
						+ lMisuraModel.getEveIdEvento();
			}
			// 02/11/2010 Gestione Revoca Espiazione Pena presso Domicilio
			else if (tipoMisura != null && tipoMisura.equals(ICostantiMisuraAlternativa.ESP_PRESSO_DOM)) {
				lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
						+ "=siap.siep.misuraalternativa.action.ActLoadInserisciMARevocaEspPressoDom&"
						+ ICostantiPenaResidua.CAMPO_ID_PENA_RESIDUA + "=" + lIdPenaRes + "&"
						+ ICostantiMisuraAlternativa.CAMPO_ID_DOCUMENTO_SIUS + "="
						+ lMisuraModel.getEveIdEvento();
			}
		} else // la misura esiste
		{ // if( lrevoca == null && !isPenaRicalcolata)
			// o lrevoca!=null = primo giro con provvedimento selezionato dalla lista oppure secondo giro
			// o isPenaRicalcolata = true (secondo giro)
			// ========================================================================
			// Si entra qui in 2 casi:
			// - Primo giro con Ordinanza selezionata dalla lista
			// - Secondoo giro
			// In entrambi i casi la Misura è a sistema e la aggiorni nei campi
			// - COD_TIPO_UFFICIO_SCARCERAZIONE
			// - DATA_INGRESSO_ISTITUTO
			// - NOTE
			// ========================================================================

			if (!lPosMod.isLibero()) {
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

			if (this.isRequestParameterNullObj("presenzanuovopenaricalcolata")) {
				// Mi trovo sul primo giro (ordinanza seleziona dalla lista)
				lMisAltCtrl.ExModificaMisuraAlternativa(lrevoca);

				if (tipoMisura != null && tipoMisura.equals("AFFIDAMENTO")) {
					lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
							+ "=siap.siep.misuraalternativa.action.ActLoadInserisciMARevocaAffProva&"
							+ ICostantiPenaResidua.CAMPO_ID_PENA_RESIDUA + "=" + lIdPenaRes;
				} else if (tipoMisura != null && tipoMisura.equals("INDULTINO")) {
					lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
							+ "=siap.siep.misuraalternativa.action.ActLoadInserisciMARevocaIndultino&"
							+ ICostantiPenaResidua.CAMPO_ID_PENA_RESIDUA + "=" + lIdPenaRes;
				}
				// 02/11/2010 Gestione Revoca Espiazione Pena presso Domicilio
				else if (tipoMisura != null && tipoMisura.equals(ICostantiMisuraAlternativa.ESP_PRESSO_DOM)) {
					lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
							+ "=siap.siep.misuraalternativa.action.ActLoadInserisciMARevocaEspPressoDom&"
							+ ICostantiPenaResidua.CAMPO_ID_PENA_RESIDUA + "=" + lIdPenaRes;
				}
			} else {
				// =======================================================================
				// Inserimento provvedimento di esecuzione
				// Pos Giu Gestite: In Misura, In Sospensione (51 ter/ 51 bis), libero(), Ammissione
				// Provvisoria
				// =======================================================================
				String codiceMotivo = lrevoca.getCodTipoMisura();
				this.setRequestAttribute("tipoMisura", tipoMisura);

				EventoNotificaModel lEve = new EventoNotificaModel();
				lEve.getEvento().setCodTipoProvvedimento("06");

				if ((tipoMisura != null && tipoMisura.equals("AFFIDAMENTO")
						&& (PosizioneGiu.equals("32") || PosizioneGiu.equals("37") || lPosMod.isLibero()
								|| PosizioneGiu.equals("13") || PosizioneGiu.equals("54")))
						|| (tipoMisura != null && tipoMisura.equals("INDULTINO")
								&& (PosizioneGiu.equals("35") || PosizioneGiu.equals("40")
										|| PosizioneGiu.equals("27") || lPosMod.isLibero()))
						|| (tipoMisura != null && tipoMisura.equals(ICostantiMisuraAlternativa.ESP_PRESSO_DOM)
								&& (PosizioneGiu.equals("51") || PosizioneGiu.equals("52")
										|| PosizioneGiu.equals("50") || lPosMod.isLibero()))) {
					lEve.getEvento().setCodMotivo(codiceMotivo);
				} else {
					lEve.getEvento().setCodTipoProvvedimento("04");
					lEve.getEvento().setCodMotivo("0000");
				}

				lEve.setEvento(setEventoProvvedimentoMisuraAlternativa(lEve.getEvento()));
				lEve.getMagistrato().setCodMagistrato(this.calcolaMagistrato());
				lEve.getEvento().setEveIdEvento(lIdOrdinanza);

				NotificaModel[] lNotifiche = this.setNotificheMisuraAlternativa();
				lEve.setNotifiche(lNotifiche);

				// Inserisco la pena contestualmente al provvedimento
				CalcoloPenaModel lCalcoloPenaModelRet = (CalcoloPenaModel) this
						.getSessionAttribute("REMApenaresidua");
				PenaResiduaModel lPenaRes = lCalcoloPenaModelRet.getPenaResiduaRicalcolata();
				lPenaRes.setIdPenaResidua(null);

				// FIXME d.f. 17/02/2014 se la pena non viene rideterminata il record sospensione
				// non andrebbe inserito non essendoci dati significativi e potrebbe
				// essere forviante per il modulo di calcolo pena che potrebbe interpretare
				// la revoca come evento di pena iniziale (ESEC_PRE_DOM e INDULTINO)
				// SOSPENSIONE
				SospensioneModel lSospModel = new SospensioneModel();

				// lSospModel.setDataInizio(lDataDalRevoca);
				lSospModel.setNumAnniPenaResiduaReclus(lPenaRes.getNumAnniReclusione());
				lSospModel.setNumMesiPenaResiduaReclus(lPenaRes.getNumMesiReclusione());
				lSospModel.setNumGiorniPenaResiduaReclus(lPenaRes.getNumGiorniReclusione());

				lSospModel.setMultaResidua(lPenaRes.getImportoMulta());

				lSospModel.setNumAnniPenaResiduaArres(lPenaRes.getNumAnniArresto());
				lSospModel.setNumMesiPenaResiduaArres(lPenaRes.getNumMesiArresto());
				lSospModel.setNumGiorniPenaResiduaArres(lPenaRes.getNumGiorniArresto());

				lSospModel.setAmmendaResidua(lPenaRes.getImportoAmmenda());

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

				lRetModel = lMisAltCtrl.ExInserisciOModificaMANotifica(lEve, lPenaRes, lrevoca, lSospModel);

				lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
						+ "=siap.siep.misuraalternativa.action.ActDettaglioRevocaMAAffInProva&"
						+ ICostantiEvento.CAMPO_ID_EVENTO + "=" + lRetModel.getEvento().getIdEvento();
			}
		}
		return lPage;
	}

}