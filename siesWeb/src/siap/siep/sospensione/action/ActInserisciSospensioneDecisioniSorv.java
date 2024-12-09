package siap.siep.sospensione.action;

import java.math.BigDecimal;
import java.util.Date;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.Utils;
import f3b.web.IWebConstants;
import siap.sico.calendar.model.CalendarModel;
import siap.sico.decodifiche.model.ComuneModel;
import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoModel;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.misuraalternativa.controller.IMisuraAlternativa;
import siap.sico.misuraalternativa.model.MisuraAlternativaModel;
import siap.sico.util.CalendarUtil;
import siap.sico.util.SICOLookupRemote;
import siap.siep.calcolopena.action.ActCalcoloPenaMain;
import siap.siep.calcolopena.model.CalcoloPenaModel;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.misuraalternativa.action.ActMisuraAlternativa;
import siap.siep.misuraalternativa.action.ICostantiMisuraAlternativa;
import siap.siep.notifica.model.NotificaModel;
import siap.siep.ordineesecuzione.controller.IOrdineEsecuzione;
import siap.siep.penacomplessiva.controller.IPenaComplessiva;
import siap.siep.penacomplessiva.model.PenaComplessivaModel;
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
 * ActInserisciSospensioneDecisioniSorv - Classe Action per l'inserimento di Decisione della sorveglianza di
 * Sospensione della pena
 *
 * @version 1.0
 */
public class ActInserisciSospensioneDecisioniSorv extends ActMisuraAlternativa
		implements ICostantiSospensione {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public String processRequest() throws Exception {

		// info per il log
		siesLogger.info(getClass().getName() + "processRequest: inizio");

		/*
		 * Quest'azione viene chiamata due volte, la prima per inserire la misura e calcolare la penaresidua
		 * che metterà in sessione e la seconda volta per inserire il provvedimento e la pena che era in
		 * sessione!
		 */
		MisuraAlternativaModel lMisMod = new MisuraAlternativaModel();
		EventoNotificaModel lRetModel = new EventoNotificaModel();

		FascicoloSiepModel lFascicoloModel = (FascicoloSiepModel) getSessionAttribute("fascicolo");

		String lCodiceOperatore = getCodUtenteConnesso();
		String lCodiceUfficio = getCodUfficioUtenteConnesso();

		Date lDataSospensione = null;
		if (!isRequestParameterNullObj(ICostantiSospensione.CAMPO_ANNO_DATA_INIZIO)) {
			lDataSospensione = getRequestDateParameter(ICostantiSospensione.CAMPO_ANNO_DATA_INIZIO,
					ICostantiSospensione.CAMPO_MESE_DATA_INIZIO,
					ICostantiSospensione.CAMPO_GIORNO_DATA_INIZIO);
		}

		String PosizioneGiu = getRequestStringParameter(
				ICostantiPosizioneGiuridica.CAMPO_COD_POSIZIONE_GIURIDICA);
		setRequestAttribute("posizionegiuridica", PosizioneGiu);
		PosizioneGiuridicaModel lPosMod = new PosizioneGiuridicaModel();
		lPosMod.setCodPosizioneGiuridica(PosizioneGiu);

		BigDecimal lIdPenaRes = getRequestBigDecimalParameter(ICostantiPenaResidua.CAMPO_ID_PENA_RESIDUA);
		IMisuraAlternativa lMisAltCtrl = SICOLookupRemote.getMisuraAlternativaRemote();

		/************************************************
		 * INIZIO CALCOLO DELLA PENA PER LA SOSPENSIONE *
		 ************************************************/
		boolean isPenaRicalcolata = true;
		// se la penaresidua non è in sessione vuol dire che la devo ancora calcolare e quindi la calcolo
		if (isSessionAttributeNullObj("SOSPpenaresidua")) {
			isPenaRicalcolata = false;
			// Pena Complessiva
			IPenaComplessiva lCtrlPenComp = SIEPLookupRemote.getPenaComplessivaRemote();
			PenaComplessivaModel lPenMod = lCtrlPenComp
					.ExRicercaPenaComplessivaByIdFascicolo(lFascicoloModel.getIdFascicoloSiep());

			String lFlagErgastolo = "N";
			// se la Pena Complessiva è un ergastolo o ergastolo con isolamento diurno
			if (lPenMod.getCodTipoPenaDetentiva() != null && lPenMod.getCodTipoPenaDetentiva() != ""
					&& (lPenMod.getCodTipoPenaDetentiva().equals("03")
							|| lPenMod.getCodTipoPenaDetentiva().equals("04"))) {
				lFlagErgastolo = "S";
			}

			// PENA RESIDUA
			IPenaResidua lPenResCtrl = SIEPLookupRemote.getPenaResiduaRemote();

			// Cerca l'ultima pena residua validata...
			PenaResiduaModel lUltimaPenaResidua = lPenResCtrl
					.ExRicercaPenaResiduaUltimaValidata(lFascicoloModel.getIdFascicoloSiep());
			// ...se non la trova cerca l'ultima in assoluto
			if (lUltimaPenaResidua == null)
				lUltimaPenaResidua = lPenResCtrl
						.ExRicercaPenaResiduaCorrenteByFascicoloSiep(lFascicoloModel.getIdFascicoloSiep());

			PenaResiduaModel lPenaResiduaNuova = new PenaResiduaModel();
			CalendarModel lPenaEspiataSosp = new CalendarModel();
			BigDecimal lNumGiorniLA = new BigDecimal(0);

			if (lFlagErgastolo.equals("N")) {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("Recupero la situazione attuale della Pena (dati Validati)");
				ActCalcoloPenaMain lCalcoloPenaMain = new ActCalcoloPenaMain();
				CalcoloPenaModel lCalcoloPenaModel = lCalcoloPenaMain
						.calcoloPena(lFascicoloModel.getIdFascicoloSiep(), null);

				// Pena Ricalcolata sul
				PenaResiduaModel lPenaResiduaIniziale = lCalcoloPenaModel
						.getPenaDaEspiare(lUltimaPenaResidua.getDataInizio(), null, "all");
				lCalcoloPenaModel.calcolaPenaDaSospensione(lPenaResiduaIniziale, lDataSospensione);
				lPenaResiduaNuova = lCalcoloPenaModel.getPenaResiduaRicalcolata();
				lPenaEspiataSosp = lCalcoloPenaModel.getPenaEspiata();
				lNumGiorniLA = new BigDecimal(lCalcoloPenaModel.getLiberazioneAnticipata());

				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("lPenaResiduaNuova : " + lPenaResiduaNuova);
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("lPenaEspiataSosp : " + lPenaEspiataSosp);

				// Vengono settati quei parametri
				// che non vengono gestiti nel CalcoloPenaModel
				lPenaResiduaNuova.setIdPenaResidua(lUltimaPenaResidua.getIdPenaResidua());
				lPenaResiduaNuova.setFasSieIdFascicoloSiep(lFascicoloModel.getIdFascicoloSiep());
				lPenaResiduaNuova.setDiesAQuo(lUltimaPenaResidua.getDiesAQuo());
				lPenaResiduaNuova.setFlagErgastolo(lUltimaPenaResidua.getFlagErgastolo());
				lPenaResiduaNuova.setFlagValidato("N");
				lPenaResiduaNuova.setFlagPenaSospesa("S");
				lPenaResiduaNuova.setEveIdEvento(null);
			} else {
				// Se Ergastolo non ricalcolo la pena, ma copio i dati dell'ultimo record
				lPenaResiduaNuova = lUltimaPenaResidua;
				lPenaResiduaNuova.setMisAltIdMisuraAlternativa(null);
				lPenaResiduaNuova.setEveIdEvento(null);
				lPenaResiduaNuova.setFlagPenaSospesa("S");
				lPenaResiduaNuova.setFlagValidato("N");
			}

			// Gestione della Data Inserimento
			lPenaResiduaNuova.setCodOperatoreInserimento(lCodiceOperatore);
			lPenaResiduaNuova.setCodUfficioInserimento(lCodiceUfficio);
			lPenaResiduaNuova.setDataInserimento(DateUtils.getSysDate());
			lPenaResiduaNuova.setCodOperatoreAggiornamento(null);
			lPenaResiduaNuova.setDataAggiornamento(null);
			lPenaResiduaNuova.setCodUfficioAggiornamento(null);

			if (lFlagErgastolo.equals("S")) {
				lPenaResiduaNuova.setDataFine(DateUtils.getDate(9999, 12, 31));

				if (lPenMod.getCodTipoPenaDetentiva().equals("03"))
					lPenaResiduaNuova.setFlagErgastolo("S");
				else if (lPenMod.getCodTipoPenaDetentiva().equals("04"))
					lPenaResiduaNuova.setFlagErgastolo("D");
			}

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("lPenaResiduaNuova completa: " + lPenaResiduaNuova);

			setSessionAttribute("SOSPpenaresidua", lPenaResiduaNuova);

			// ============================
			// INSERIMENTO SOSPENSIONE
			// ============================
			if (!lPosMod.isLibero() && lUltimaPenaResidua.getDataInizio() != null) {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("Prepara SopensioneModel");

				SospensioneModel lSospModel = new SospensioneModel();
				lSospModel.setDataInizio(lDataSospensione);

				if (lFlagErgastolo.equals("N")) {
					lSospModel.setNumAnniPenaResiduaReclus(lPenaResiduaNuova.getNumAnniReclusione());
					lSospModel.setNumMesiPenaResiduaReclus(lPenaResiduaNuova.getNumMesiReclusione());
					lSospModel.setNumGiorniPenaResiduaReclus(lPenaResiduaNuova.getNumGiorniReclusione());
					lSospModel.setNumAnniPenaResiduaArres(lPenaResiduaNuova.getNumAnniArresto());
					lSospModel.setNumMesiPenaResiduaArres(lPenaResiduaNuova.getNumMesiArresto());
					lSospModel.setNumGiorniPenaResiduaArres(lPenaResiduaNuova.getNumGiorniArresto());

					lSospModel.setAmmendaResidua(lPenaResiduaNuova.getImportoAmmenda());
					lSospModel.setMultaResidua(lPenaResiduaNuova.getImportoMulta());

					lSospModel.setNumAnniPenaEspiata(new BigDecimal(lPenaEspiataSosp.getNumAnni()));
					lSospModel.setNumMesiPenaEspiata(new BigDecimal(lPenaEspiataSosp.getNumMesi()));
					lSospModel.setNumGiorniPenaEspiata(new BigDecimal(lPenaEspiataSosp.getNumGiorni()));
				} else {
					// Nel caso di ergastolo
					// Calcolo la pena espiata come intervallo tra la data inizio e la data
					// di sospensione (considerato come giorno espiato)
					CalendarModel lCalPenaEspiataCalcoloErg = new CalendarModel();
					CalendarUtil lCalUtil = new CalendarUtil();

					lCalPenaEspiataCalcoloErg.setDataInizio(lUltimaPenaResidua.getDataInizio());
					lCalPenaEspiataCalcoloErg.setDataFine(lDataSospensione);

					lCalPenaEspiataCalcoloErg = lCalUtil.CalcolaNumGiorniMesiAnni(lCalPenaEspiataCalcoloErg);
					lCalPenaEspiataCalcoloErg = lCalUtil.ricalcolaGAM(lCalPenaEspiataCalcoloErg);

					lSospModel.setNumAnniPenaEspiata(new BigDecimal(lCalPenaEspiataCalcoloErg.getNumAnni()));
					lSospModel.setNumMesiPenaEspiata(new BigDecimal(lCalPenaEspiataCalcoloErg.getNumMesi()));
					lSospModel.setNumGiorniPenaEspiata(
							new BigDecimal(lCalPenaEspiataCalcoloErg.getNumGiorni()));
				}

				// lSospensione.setPenResIdPenaResidua(lKeyPena);
				lSospModel.setNumGiorniLibanticipata(lNumGiorniLA);
				lSospModel.setFasSieIdFascicoloSiep(lFascicoloModel.getIdFascicoloSiep());
				lSospModel.setCodOperatoreInserimento(lCodiceOperatore);
				lSospModel.setCodUfficioInserimento(lCodiceUfficio);
				lSospModel.setDataInserimento(DateUtils.getSysDate());

				setSessionAttribute("SOSPENSIONE", lSospModel);
			}
		}
		/*************************** FINE CALCOLO DELLA PENA ***********************************/

		String lPage = null;
		MisuraAlternativaModel lSospensione = null;
		// Se ordinanza selezionata dalla lista e non modificata, recupero i dati
		// dalla tabella misura alternativa
		BigDecimal lIdOrdinanza = getRequestBigDecimalParameter(
				ICostantiMisuraAlternativa.CAMPO_ID_DOCUMENTO_SIUS);
		if (lIdOrdinanza != null && !lIdOrdinanza.toString().equals(""))
			lSospensione = lMisAltCtrl.ExRicercaMisuraAlternativaByIdEvento(lIdOrdinanza);

		// ==========================================================================
		// Inserimento ordinanza/decreto della sorveglianza se non selezionata dalla
		// lista o modificata,
		// ==========================================================================
		if (lSospensione == null && !isPenaRicalcolata) {
			// inserisco evento del TDS
			String lTipoDecisione = getRequestStringParameter(
					ICostantiMisuraAlternativa.CAMPO_COD_TIPO_DECISIONE);
			String lCodiceUffEmi = getCodUfficioByCodTipoUfficioDescrComune(
					getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_COD_UFFICIO_SORVEGLIANZA),
					getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_SEDE_TDS_EMITT));
			ComuneModel lComModAutEmi = new ComuneModel(getCodComuneByDescr(
					getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_SEDE_TDS_EMITT)));
			Date lDataEmisTras = getRequestDateParameter(ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_DECISIONE,
					ICostantiMisuraAlternativa.CAMPO_MESE_DATA_DECISIONE,
					ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_DECISIONE);

			EventoNotificaModel lEveMod = new EventoNotificaModel();
			lEveMod.getEvento().setCodMotivo(getRequestStringParameter(ICostantiEvento.CAMPO_COD_MOTIVO));

			lEveMod.setEvento(setEventoOrdinanzaDecretoMisuraAlternativa(lEveMod.getEvento(), lTipoDecisione,
					lCodiceUffEmi, lComModAutEmi, lDataEmisTras));

			// setto il deposito ordinanza
			DepositoDecretoModel lDepDecMod = null;
			DepositoOrdinanzaPcModel lDepOrdMod = null;

			if (lTipoDecisione.equals("03"))
				lDepOrdMod = setDepositoOrdinanzaPc(lCodiceUffEmi);
			else if (lTipoDecisione.equals("02"))
				lDepDecMod = setDepositoDecreto(lCodiceUffEmi);

			// setto il tenore
			TenoreModel lTenMod = setTenore(new BigDecimal(1), "0001");

			// misura alternativa
			String lUfficioScarc = "-";
			if (!isRequestParameterNullObj("tipo")) {
				if (getRequestStringParameter("tipo").equals("scarcerato"))
					lUfficioScarc = "SORV";
				else if (getRequestStringParameter("tipo").equals("scarcerare"))
					lUfficioScarc = "PROC";
			}

			lMisMod = setMisuraAlternativa(lTipoDecisione, "CO", lCodiceUffEmi,
					getRequestStringParameter(ICostantiEvento.CAMPO_COD_MOTIVO), lUfficioScarc);
			lMisMod.setDataScarcerazione(lDataSospensione);

			// MEV_2019-09-SIEP: aggiunto metodo
			// MEV_2024-092: rimosso metodo
			// settaDatiOrdinanzaProvvisoria(lMisMod);

			/*
			 * nel momento in cui inserisco la misura alternativa e calcolo la pena richiamo la maschera
			 * d'inserimento di chi mi ha chiamato per visualizzare la seconda parte della maschera ossia i
			 * destinatari
			 */
			MisuraAlternativaModel lMisuraModel = null;
			if (lTipoDecisione.equals("03"))
				lMisuraModel = lMisAltCtrl.ExInserisciMisuraAlternativaEventoNotifica(lEveMod, lDepOrdMod,
						lTenMod, lMisMod);
			else if (lTipoDecisione.equals("02"))
				lMisuraModel = lMisAltCtrl.ExInserisciDecretoSospEventoNotifica(lEveMod, lDepDecMod, lTenMod,
						lMisMod);

			lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
					+ "=siap.siep.sospensione.action.ActLoadInserisciSospensioneDecisioniSorv&"
					+ ICostantiPenaResidua.CAMPO_ID_PENA_RESIDUA + "=" + lIdPenaRes + "&"
					+ ICostantiMisuraAlternativa.CAMPO_ID_DOCUMENTO_SIUS + "="
					+ lMisuraModel.getEveIdEvento();
		} else {
			// MEV_2019-09-SIEP: aggiunta nuova gestione per modifica
			// qui entro sia al primo giro che al secondo
			String tipoOperazione = null;
			if (!isRequestParameterNullObj("tipoOperazione"))
				tipoOperazione = getRequestStringParameter("tipoOperazione");
			if ("MODIFICA".equals(tipoOperazione)) {
				BigDecimal idEventoOld = getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO);
				if (!Utils.isNullObj(idEventoOld)) {
					siesLogger.debug(
							"Sono in MODIFICA Sospensione procedo alla cancellazione dell'evento con id = "
									+ idEventoOld);
					IEvento ie = SICOLookupRemote.getEventoRemote();
					EventoModel lEveModRic = ie.ExRicercaEventoByKey(idEventoOld);
					IOrdineEsecuzione ioe = SIEPLookupRemote.getOrdineEsecuzioneRemote();
					ioe.ExCancellaEventoConStoreProcedure(lEveModRic);
					siesLogger.debug("Evento cancellato proseguo con un nuovo inserimento Sospensione");
				}
			}
			// la misura esiste
			// ========================================================================
			// O esiste la misura oppure sto inserendo il provvedimento, secondo giro
			// ========================================================================
			if (!isRequestParameterNullObj(ICostantiMisuraAlternativa.CAMPO_NOTE))
				lSospensione.setNote(getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_NOTE));

			// MEV_2019-09-SIEP: aggiunto metodo ed impostazione della data scarcerazione
			// MEV_2024-092: rimosso metodo
			// settaDatiOrdinanzaProvvisoria(lSospensione);
			if (Utils.isNullObj(lDataSospensione)) {
				if (!isRequestParameterNullEmptyObj(
						ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_SCARCERAZIONE)
						&& !isRequestParameterNullEmptyObj(
								ICostantiMisuraAlternativa.CAMPO_MESE_DATA_SCARCERAZIONE)
						&& !isRequestParameterNullEmptyObj(
								ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_SCARCERAZIONE))
					lDataSospensione = getRequestDateParameter(
							ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_SCARCERAZIONE,
							ICostantiMisuraAlternativa.CAMPO_MESE_DATA_SCARCERAZIONE,
							ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_SCARCERAZIONE);
			}
			lSospensione.setDataScarcerazione(lDataSospensione);

			if (isRequestParameterNullObj("presenzanuovopenaricalcolata")) {
				// primo giro
				lMisAltCtrl.ExModificaMisuraAlternativa(lSospensione);
				lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
						+ "=siap.siep.sospensione.action.ActLoadInserisciSospensioneDecisioniSorv&"
						+ ICostantiPenaResidua.CAMPO_ID_PENA_RESIDUA + "=" + lIdPenaRes;
			} else {
				// secondo giro
				if (getRequestStringParameter("tipo").equals("scarcerato"))
					lSospensione.setCodTipoUfficioScarcerazione("SORV");
				else if (getRequestStringParameter("tipo").equals("scarcerare"))
					lSospensione.setCodTipoUfficioScarcerazione("PROC");

				String codiceMotivo = lSospensione.getCodTipoMisura();
				EventoNotificaModel lEve = new EventoNotificaModel();

				if (lSospensione != null && lSospensione.getCodTipoUfficioScarcerazione().equals("SORV")) {
					// Detenuto già scarcerato
					lEve.getEvento().setCodTipoProvvedimento("12");
				} else {
					// Detenuto da scarcerare
					lEve.getEvento().setCodTipoProvvedimento("09");
				}

				if (codiceMotivo.equals("2000") || codiceMotivo.equals("2001"))
					lEve.getEvento().setCodMotivo("0263");
				else if (codiceMotivo.equals("2480"))
					lEve.getEvento().setCodMotivo("0241");
				// MEV_2019-09-SIEP: aggiunte casistiche
				else if ("0724".equals(codiceMotivo))
					lEve.getEvento().setCodMotivo("5469");
				else if ("0735".equals(codiceMotivo))
					lEve.getEvento().setCodMotivo("5496");
				// MEV_2024-092: aggiunte impostazioni di motivo 1420 & 1426
				else if (codiceMotivo.equals("0684"))
					lEve.getEvento().setCodMotivo("1420");
				else if (codiceMotivo.equals("0695"))
					lEve.getEvento().setCodMotivo("1426");

				lEve.setEvento(setEventoProvvedimentoMisuraAlternativa(lEve.getEvento()));
				lEve.getMagistrato().setCodMagistrato(calcolaMagistrato());
				lEve.getEvento().setEveIdEvento(lIdOrdinanza);

				NotificaModel[] lNotifiche = setNotificheMisuraAlternativa();
				lEve.setNotifiche(lNotifiche);

				// inserisco la pena contestualmente al provvedimento
				PenaResiduaModel lPenaRes = (PenaResiduaModel) getSessionAttribute("SOSPpenaresidua");
				lPenaRes.setIdPenaResidua(null);

				// inserisco la sospensione contestualmente al provvedimento
				SospensioneModel lSospModel = null;
				if (!isSessionAttributeNullObj("SOSPENSIONE"))
					lSospModel = (SospensioneModel) getSessionAttribute("SOSPENSIONE");

				// rimuovo la pena dalla sessione
				removeSessionAttribute("SOSPpenaresidua");
				removeSessionAttribute("SOSPENSIONE");

				if (!isRequestParameterNullObj(ICostantiPenaResidua.CAMPO_ANNO_DATA_FINE))
					lPenaRes.setDataFine(getRequestDateParameter(ICostantiPenaResidua.CAMPO_ANNO_DATA_FINE,
							ICostantiPenaResidua.CAMPO_MESE_DATA_FINE,
							ICostantiPenaResidua.CAMPO_GIORNO_DATA_FINE));

				lRetModel = lMisAltCtrl.ExInserisciOModificaMANotifica(lEve, lPenaRes, lSospensione,
						lSospModel);

				lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
						+ "=siap.siep.sospensione.action.ActLoadDettaglioSospensioneDecisioniSorv&"
						+ ICostantiEvento.CAMPO_ID_EVENTO + "=" + lRetModel.getEvento().getIdEvento();
			}
		}

		// info per il log
		siesLogger.info(getClass().getName() + "processRequest: fine");

		// pagina di ritorno
		return lPage;
	}

	// MEV_2019-09-SIEP: aggiunto metodo
	// MEV_2024-092: rimosso metodo
	// private void settaDatiOrdinanzaProvvisoria(MisuraAlternativaModel mam) throws F3BException {
	//
	// if (!isRequestParameterNullObj(ICostantiMisuraAlternativa.CAMPO_ANNO_REGISTRO_MA_AT))
	// mam.setAnnoRegistroMaAt(
	// getRequestBigDecimalParameter(ICostantiMisuraAlternativa.CAMPO_ANNO_REGISTRO_MA_AT));
	// if (!isRequestParameterNullObj(ICostantiMisuraAlternativa.CAMPO_NUMERO_REGISTRO_MA_AT))
	// mam.setNumeroRegistroMaAt(
	// getRequestBigDecimalParameter(ICostantiMisuraAlternativa.CAMPO_NUMERO_REGISTRO_MA_AT));
	// if (!isRequestParameterNullObj(ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_DECISIONE_MA_AT)
	// && !isRequestParameterNullObj(ICostantiMisuraAlternativa.CAMPO_MESE_DATA_DECISIONE_MA_AT)
	// && !isRequestParameterNullObj(ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_DECISIONE_MA_AT))
	// mam.setDataDecisioneMaAt(
	// getRequestDateParameter(ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_DECISIONE_MA_AT,
	// ICostantiMisuraAlternativa.CAMPO_MESE_DATA_DECISIONE_MA_AT,
	// ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_DECISIONE_MA_AT));
	// }

}