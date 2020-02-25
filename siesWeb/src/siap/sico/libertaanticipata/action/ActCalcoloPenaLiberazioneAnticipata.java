package siap.sico.libertaanticipata.action;

import java.math.BigDecimal;
import java.util.Date;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
import siap.sico.utente.model.DatiOperazioneModel;
import siap.sico.web.ActionSiap;
import siap.siep.SIEPException;
import siap.siep.annotazionemanuale.action.ICostantiAnnotazioneManuale;
import siap.siep.calcolopena.action.ActCalcoloPenaMain;
import siap.siep.calcolopena.controller.ICalcoloPena;
import siap.siep.calcolopena.model.CalcoloPenaModel;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.fungibilita.model.FungibilitaModel;
import siap.siep.penaresidua.controller.IPenaResidua;
import siap.siep.penaresidua.model.PenaResiduaModel;
import siap.siep.posizione.controller.IPosizioneGiuridica;
import siap.siep.posizione.model.PosizioneGiuridicaModel;
import siap.siep.util.SIEPLookupRemote;
import siap.sius.depositoordinanzapc.controller.IDepositoOrdinanzaPc;
import siap.sius.depositoordinanzapc.model.DepositoOrdinanzaPcModel;
import siap.sius.util.SIUSLookupRemote;

/**
 * <p>
 * Title: ActCalcoloPenaLiberazioneAnticipata
 * </p>
 * <p>
 * Description: Classe Action per il calcolo della pena di liberazione anticipata
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
public class ActCalcoloPenaLiberazioneAnticipata extends ActionSiap implements ICostantiLibertaAnticipata {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	/**
	 * Azione di Calcolo Pena Liberazione Anticipata
	 *
	 * @return Nome della pagina JSP da visualizzare al termine dell'elaborazione
	 * @throws F3BException
	 */
	public String processRequest() throws Exception {

		FascicoloSiepModel lFascMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");
		BigDecimal lIdFascicolo = lFascMod.getIdFascicoloSiep();

		isFascicoloSiepDiCompetenza();

		/******************************* Posizione Giuridica **********************************/
		IPosizioneGiuridica lCtrlPosGiu = SIEPLookupRemote.getPosizioneGiuridicaRemote();
		PosizioneGiuridicaModel lPosizione = lCtrlPosGiu
				.ExRicercaPosizioneGiuridicaCorrenteByIdFascicolo(lIdFascicolo);
		if (lPosizione == null)
			throw new SIEPException(SIEPException.USER_MESSAGE,
					"Inserire prima la Posizione Giuridica. Impossibile eseguire la richiesta.");

		DatiOperazioneModel lOperMod = new DatiOperazioneModel(getCodUtenteConnesso(), DateUtils.getSysDate(),
				getCodUfficioUtenteConnesso(), "");

		// ==========================================================================
		// Pagina di ritorno
		// ==========================================================================
		String lPage;

		// ==========================================================================
		// L'id dell'evento di Ordinanza selezionato da lista o inserito
		// ==========================================================================
		BigDecimal lEveIdEventoOrdinanza = getRequestBigDecimalParameter(
				ICostantiLicenzaLibanticipata.CAMPO_EVE_ID_EVENTO);
		setRequestAttribute("lEveIdEventoOrdinanza", "" + lEveIdEventoOrdinanza);

		// ..............................................................................
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("---------------------");
		// ==========================================================================
		// Recupero la data per i calcoli della fungibilità:
		// - data di scarcerazione se passata in input
		// - data di systema altrimenti
		// ==========================================================================
		Date dataSistemaPerCalcoli = null;
		Date dataScarcerazione = null;

		if (!isRequestParameterNullObj(ICostantiAnnotazioneManuale.CAMPO_GIORNO_DATA_SCARCERAZIONE)) {
			dataScarcerazione = getRequestDateParameter(
					ICostantiAnnotazioneManuale.CAMPO_ANNO_DATA_SCARCERAZIONE,
					ICostantiAnnotazioneManuale.CAMPO_MESE_DATA_SCARCERAZIONE,
					ICostantiAnnotazioneManuale.CAMPO_GIORNO_DATA_SCARCERAZIONE);
		}

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("Data scarcerazione = " + dataScarcerazione);
		if (dataScarcerazione != null) {
			dataSistemaPerCalcoli = dataScarcerazione;
		} else {
			Date lOggi = DateUtils.getSysDate();
			// Devo eliminare minuti e secondi dalla sysdate altrimenti le funzioni
			// di compare tra date falliscono
			String lGiorno = DateUtils.getDateToString(lOggi, "dd");
			String lMese = DateUtils.getDateToString(lOggi, "MM");
			String lAnno = DateUtils.getDateToString(lOggi, "yyyy");
			dataSistemaPerCalcoli = DateUtils.getDate(lAnno, lMese, lGiorno);
		}
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("dataSistemaPerCalcoli = " + dataSistemaPerCalcoli);

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("---------------------");
		// ..............................................................................

		// ==========================================================================
		// Costruisce un CalcoloPenaModel che contiene in modo strutturato
		// tutte le informazioni necessarie a determinare la Pena Residua
		// ad un dato momento
		// ==========================================================================
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("Recupero la situazione attuale della Pena (dati Validati)");
		ActCalcoloPenaMain lCalcoloPenaMain = new ActCalcoloPenaMain();
		CalcoloPenaModel lCalcoloPenaModel = lCalcoloPenaMain.calcoloPena(lIdFascicolo, null);

		// ==========================================================================
		// Effettuo il calcolo e l'inserimento/aggiornamento della data fine pena
		// della pena residua e inserisco l'eventuale fungibilità.
		// ==========================================================================
		ICalcoloPena lCtrlCal = SIEPLookupRemote.getCalcoloPenaRemote();
		CalcoloPenaModel lCalcoloPena = lCtrlCal.exCalcoloLiberazioneAnticipata(lCalcoloPenaModel,
				lIdFascicolo, lEveIdEventoOrdinanza, dataSistemaPerCalcoli, null, null, lOperMod);

		// ==========================================================================
		// Calcolo della data fine pena considerando non il nuovo metodo di calcolo,
		// ma DATA_FINE_CALCOLATO = DATA_FINE_PENA_RESIDUA_CORRENTE - LA_CONCESSE
		// ==========================================================================

		// RICERCA DEPOSITO_ORDINANZA_PC per ottenere i giorni di LA concessi
		IDepositoOrdinanzaPc lCtrlDep = SIUSLookupRemote.getDepositoOrdinanzaPcRemote();
		DepositoOrdinanzaPcModel lDepOrdMod = lCtrlDep
				.ExRicercaDepositoOrdinanzaPcByEvento(lEveIdEventoOrdinanza);

		int lTotLA = 0;
		if (lDepOrdMod != null && lDepOrdMod.getNumGiorniLibanticipata() != null) {
			lTotLA = lDepOrdMod.getNumGiorniLibanticipata().intValue();
		}

		Date lDataFineAbFine = lCtrlCal.exCalcoloLiberazioneAnticipataSuDataFineUltimaPena(lTotLA,
				lIdFascicolo);

		// ==========================================================================
		// Confronto tra i due calcoli per presentare, in caso di discrepanza,
		// un pagina che consente di selezionare il fine pena voluto
		// (data fine nuovo calcolo/fine pena su DB meno la LA).
		// ==========================================================================
		if (lDataFineAbFine != null && lCalcoloPena.getPenaResiduaRicalcolata() != null) {
			PenaResiduaModel lPenaResNuovoCalcolo = lCalcoloPena.getPenaResiduaRicalcolata();

			if (lPenaResNuovoCalcolo != null && lPenaResNuovoCalcolo.getDataFine() != null) {
				if (!lDataFineAbFine.equals(lPenaResNuovoCalcolo.getDataFine())) {
					// ==========================================================================
					// Cerca l'ultima Pena Residua Validata
					// ==========================================================================
					IPenaResidua lCtrlPenRes = SIEPLookupRemote.getPenaResiduaRemote();
					PenaResiduaModel lPenaResidua = lCtrlPenRes
							.ExRicercaPenaResiduaUltimaValidata(lIdFascicolo);

					// ==========================================================================
					// Per rieffettuare i calcoli "sul pulito" riassegno i riferimenti
					// del lCalcoloPenaMain e del lCalcoloPenaModel (forse non necessario?)
					// ==========================================================================
					ActCalcoloPenaMain lCalcoloPenaMainSenzaLA = new ActCalcoloPenaMain();
					CalcoloPenaModel lCalcoloPenaModelSenzaLA = lCalcoloPenaMainSenzaLA
							.calcoloPena(lIdFascicolo, null);

					// ==========================================================================
					// Per visualizzare la nuova data fine ab inizio effettuo il calcolo
					// senza le LA concesse
					// ==========================================================================
					PenaResiduaModel lPenaNuovoCalcoloSenzaLA = lCalcoloPenaModelSenzaLA
							.getPenaDaEspiare(lPenaResidua.getDataInizio(), null, "all");

					setRequestAttribute("DataFineAbInitioSenzaLA", lPenaNuovoCalcoloSenzaLA.getDataFine());
					setRequestAttribute("DataFineAbFineSenzaLA", lPenaResidua.getDataFine());
					setRequestAttribute("DataFineAbInitio", lPenaResNuovoCalcolo.getDataFine());
					setRequestAttribute("DataFineAbFine", lDataFineAbFine);

					setRequestAttribute("dataScarcerazione", dataScarcerazione);

					lPage = ICostantiLicenzaLibanticipata.PG_SELEZIONE_DATA_FINE_CALCOLO_PENA;
					return lPage;
				}
			}
		}

		/*
		 * ISSUE MAC : aggiunta nella if la condizione in or 'Arresti Domiciliare ex art 89 dpr 309/90 - ex
		 * art. 656 comma 10 cpp' (84) Numero MAC : MAC_20191112011 Autore : monica Data : 19/nov/2019 Branch
		 * : VERSIONE_SIES_11.2.3
		 */
		if (lPosizione.isMisAlt() || lPosizione.getCodPosizioneGiuridica().equals("04")
		// Arresti domiciliari ex art.656/10 oppure Arresti Domiciliare ex art 89 dpr 309/90 - ex art. 656
		// comma 10 cpp
				|| lPosizione.getCodPosizioneGiuridica().equals("84"))
			lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
					+ "=siap.siep.ordinescarcerazione.action.ActLoadInserisciOSLiberazioneAnticipataMA&lAzioneOS=libant&"
					+ ICostantiLicenzaLibanticipata.CAMPO_EVE_ID_EVENTO + "=" + lEveIdEventoOrdinanza;
		else
			lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
					+ "=siap.siep.ordinescarcerazione.action.ActLoadInserisciOSLiberazioneAnticipata&lAzioneOS=libant&"
					+ ICostantiLicenzaLibanticipata.CAMPO_EVE_ID_EVENTO + "=" + lEveIdEventoOrdinanza;

		// ***** FINE INTERVENTO MAC_20191112011 *****//
		FungibilitaModel lFunMod = lCalcoloPena.getFungibilitaCalcolata();
		if (lFunMod != null && ((lFunMod.getNumAnni() != null && lFunMod.getNumAnni().intValue() > 0)
				|| (lFunMod.getNumMesi() != null && lFunMod.getNumMesi().intValue() > 0)
				|| (lFunMod.getNumGiorni() != null && lFunMod.getNumGiorni().intValue() > 0))) {
			setRequestAttribute("TotaleGiorniLibAnt", "" + lTotLA);

			// ==========================================================================
			// Restituisco i dati alla finestra di visualizzazione
			// ==========================================================================
			setRequestAttribute("PenaResidua", lCalcoloPena.getPenaResiduaRicalcolata()); // la pena
																							// rideterminata
			setRequestAttribute("PenaGiaEspiata", lCalcoloPena.getPenaEspiata()); // pena già espiata
			setRequestAttribute("Fungibilita", lCalcoloPena.getFungibilitaCalcolata()); // fungibilità

			setRequestAttribute("dataScarcerazione", dataScarcerazione);

			lPage = ICostantiLicenzaLibanticipata.PG_DETTAGLIO_CALCOLO_PENA;
		}

		return lPage;
	}

}