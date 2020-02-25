package siap.siep.calcolopena.action;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Vector;

import org.apache.log4j.Logger;

import siap.sico.calendar.model.CalendarModel;
import siap.sico.util.CalendarUtil;
import siap.sico.web.ActionSiap;
import siap.siep.SIEPException;
import siap.siep.altracausa.controller.IAltraCausa;
import siap.siep.altracausa.model.AltraCausaModel;
import siap.siep.annotazionemanuale.action.ICostantiAnnotazioneManuale;
import siap.siep.calcolopena.controller.ICalcoloPena;
import siap.siep.fascicolo.action.ICostantiFascicoloSiep;
import siap.siep.fascicolo.controller.IFascicoloSiep;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.fungibilita.controller.IFungibilita;
import siap.siep.fungibilita.model.FungibilitaModel;
import siap.siep.penacomplessiva.controller.IPenaComplessiva;
import siap.siep.penacomplessiva.model.PenaComplessivaModel;
import siap.siep.penaresidua.controller.IPenaResidua;
import siap.siep.penaresidua.model.PenaResiduaModel;
import siap.siep.penaresidua.util.PenaResiduaUtil;
import siap.siep.posizione.controller.IPosizioneGiuridica;
import siap.siep.posizione.model.PosizioneGiuridicaModel;
import siap.siep.util.SIEPLookupRemote;
import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;

/**
 * Classe action che effettua il calcolo della Pena Residua e l'inserimento nel caso di Rideterminazione Pena
 * / Provvedimenti con Richiesta al GE: - Depenelizzazione - Incostituzionalità - Amnistia/Indulto
 * 
 * Viene richiamata dal tasto 'Calcolo Pena' del dettaglio dell'inserimento SOLO nel caso di Anticipazione
 * degli Effetti.
 * 
 * La procedura di calcolo è la seguente: 1) si ridetermina il quantum di pena a partire dai dati già a
 * sistema e da quelli appena inseriti 2) se il soggetto è in espiazione si rideterminano le date di fine pena
 * a partire dai quantum ricalcolati al punto 1 e della data inizio pena 3) si verifica se con i nuovi dati
 * nasce un periodo fungibile e lo si registra a sistema
 * 
 * Prerequisiti: da fare
 * 
 * Punto 1: I Dati già a sistema vengono recuperati nel seguente modo: - se il calcolo è ab inizio, vengono
 * recuperati e sommati i quantum di: - Pena Complessiva in sentenza - Benefici concessi - Benefici Revocati -
 * Misure Cautelari (computabili) - Annotazioni Manuali (concesse) NO! - Annotazioni Manuali (richieste con
 * anticipazione degli effeti e non 'stornate') - se il calcolo non è ab inizio, il quantum di partenza dei
 * calcoli è quello presente in: - ultima pena residua VALIDATA (se esiste) - Pne complessiva in sentenza
 * altrimenti (caso raro e comunque errato)
 * 
 * I dati appena inseriti sono le annotazioni manuali non ancora validate con FLAG_APP_PROVVISORIA = 'A' che
 * indica l'anticipazione, cioè la volontà di conteggiare subito il quantum nel calcolo della pena senza
 * aspettare la decisione del GE. Delle annotazioni non ancora validate vengono prese solo quelle dello stesso
 * tipo del CAMPO_COD_TIPO_ANNOTAZIONE. Questo perchè è possibile inserire delle annotazioni senza validarle,
 * e quindi inserirne altre di altro tipo e richiedere il calcolo della pena. Sulla finestra in cui è presente
 * il tasto Calcolo Pena, viene visualizzata infatti la pena a sistema e il dettaglio delle annotazioni che si
 * andranno a computare, quindi vanno recuperate solo quelle annotazioni e non altre.
 * 
 * @deprecated classe sostituita dalla ActCalcolaPenaComputo che gestisce anche le Richieste al GE
 */
@SuppressWarnings("rawtypes")
public class ActCalcoloPenaGE extends ActionSiap implements ICostantiAnnotazioneManuale {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public String processRequest() throws Exception {

		// DA QUI COPIA E INCOLLA (e modifiche) della LOADCALCOLOPENA
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("");
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("=================================================");
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("  INIZIO CALCOLO DELLA PENA PER RICHIESTE AL GE  ");
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("  CON ANTICIPAZIONE DEGLI EFFETTI                ");
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("=================================================");

		BigDecimal lFascID = ((FascicoloSiepModel) getSessionAttribute("fascicolo")).getIdFascicoloSiep();
		isFascicoloSiepDiCompetenza();

		/*
		 * !!!!!!!!!!!!!!!! sezione di test per gestire il caso in cui il soggetto risulti in espiazione con
		 * data di decorrenza valorizzata, ma in effetti è libero ma si sono 'scordati' di scriverlo. In
		 * questo caso l'operatore imputa in maschera la data di scarcerazione e il sistema deve fare i
		 * calcoli come se il soggetto fosse libero a quella data.
		 * 
		 * Modifiche: - calcolo del quantum: se abInizio: non cambia nulla fino alla funzione di
		 * rideterminazione in quanto la pena a sistema - calcolo date decorrenza pena: non vanno calcolate
		 * perchè il soggetto è libero Se abInizio: il calcolo del quantum procede come sempre
		 * 
		 */

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
			dataSistemaPerCalcoli = DateUtils.getSysDate();
		}
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("dataSistemaPerCalcoli = " + dataSistemaPerCalcoli);
		// ==========================================================================
		// indica alla jsp se utilizzare come data fine pena manuale la data data
		// fine pena calcolata (N) o la data id systema(S)
		// ==========================================================================
		String ForzaSysdateManuale = "N";
		String ChiedereValidazionePena = "S";
		String lPage = null;

		// ==========================================================================
		// Recupero la posizione giuridica
		// Attenzione!!!!!! LA posizione giuridica è alquanto aleatoria perchè
		// può essere modificata in qualunque momento per cui posso avere un soggetto
		// libero , ma con una pena residua in corso (data inizio <> null)
		// ==========================================================================
		IPosizioneGiuridica lPG = SIEPLookupRemote.getPosizioneGiuridicaRemote();

		PosizioneGiuridicaModel PGMod = new PosizioneGiuridicaModel();
		PGMod.setFasSieIdFascicoloSiep(lFascID);
		PosizioneGiuridicaModel PGMod2 = lPG.ExRicercaPosizioneGiuridicaCorrente(PGMod);

		if (PGMod2 == null)
			throw new SIEPException(SIEPException.USER_MESSAGE,
					"Rivedere Misure Cautelari o Posizione Giuridica !");

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("POSIZIONE GIURIDICA : " + PGMod2.getCodPosizioneGiuridica());

		boolean detenutoQuestaCausa = false;
		boolean libero = false;
		boolean detenutoAltraCausa = false;

		if (PGMod2.getCodPosizioneGiuridica().equals("07") // LIBERO
				|| PGMod2.getCodPosizioneGiuridica().equals("10") // LIBERO
				|| PGMod2.getCodPosizioneGiuridica().equals("16") // LIBERO IN DIFFERIMENTO PENA
				|| PGMod2.getCodPosizioneGiuridica().equals("17") // LIBERO IN DIFFERIMENTO PENA (PROVVISORIA)
		) {
			libero = true;
		}

		// ==========================================================================
		// Recupero la data inizio pena per i calcoli:
		// Se libero, potrebbe essere detenuto per altra causa. In questo caso utilizzo
		// la data_scadenza (se presente) pena altra causa + 1 come data inizio.
		// Altrimenti (libero veramente) non ho una data inizio pena e non calcolerò
		// le date di decorrenza
		// ==========================================================================
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("libero = " + libero);

		String lGiornoInizio = new String("");
		String lMeseInizio = new String("");
		String lAnnoInizio = new String("");

		// String FlagAltraCausa = "N";

		if (libero) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("LIBERO verifico se detenuto per altra causa");

			FascicoloSiepModel lFascMod = new FascicoloSiepModel();
			IFascicoloSiep lFS = SIEPLookupRemote.getFascicoloSiepRemote();
			lFascMod = lFS.ExRicercaFascicoloByKey(lFascID);

			if (lFascMod.getFlagAltraCausa() != null && lFascMod.getFlagAltraCausa().equals("S")) {
				detenutoAltraCausa = true;
				// FlagAltraCausa = "S";

				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("Detenuto per altra causa");

				AltraCausaModel lAcModel = new AltraCausaModel();
				IAltraCausa lAC = SIEPLookupRemote.getAltraCausa();
				lAcModel = lAC.ExRicercaAltraCausaByFascicolo(lFascID);

				if (lAcModel.getDataDecorrenza() != null && lAcModel.getDataScadenza() != null) {
					Date datainizio = DateUtils.getDayAfter(lAcModel.getDataScadenza());

					lGiornoInizio = DateUtils.getDayToString(datainizio);
					lMeseInizio = DateUtils.getMonthToString(datainizio);
					lAnnoInizio = DateUtils.getYearToString(datainizio);

					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger.debug("Data inizio presa da AltraCausa = " + datainizio);
				} else {
					lGiornoInizio = "-";
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger.debug("LIBERO, detenuto Altra Causa e data inizio NULL");
				}
			} else {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("Detenuto per questa causa");
				detenutoQuestaCausa = true;
			}
		}
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("detenutoQuestaCausa = " + detenutoQuestaCausa);
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("detenutoAltraCausa = " + detenutoAltraCausa);

		String lVedoJSP = "N";

		// ======================
		// PENA Complessiva
		// ======================
		PenaComplessivaModel lPenMod = new PenaComplessivaModel();
		lPenMod.setFasSieIdFascicoloSiep(lFascID);
		IPenaComplessiva lPCon = SIEPLookupRemote.getPenaComplessivaRemote();
		Vector lPComples = lPCon.ExRicercaPenaComplessivaNoError(lPenMod);

		if (lPComples.size() == 0) {
			RedirectTo lRedirigi = new RedirectTo();
			lRedirigi.setPage(IWebConstants.PG_MAIN);
			setRequestAttribute(IWebConstants.MESSAGE_TEXT, "Pena Complessiva mancante");
			lRedirigi.setAction("siap.siep.fascicolo.action.ActLoadDettaglioFascicolo&"
					+ ICostantiFascicoloSiep.CAMPO_ID_FASCICOLO_SIEP + "="
					+ ((FascicoloSiepModel) getSessionAttribute("fascicolo")).getIdFascicoloSiep());
			// ICostantiFascicoloSiep.CAMPO_AZIONE_CHIAMANTE + "=" + getClass().getName());
			setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi);

			return IWebConstants.PG_MESSAGE;
		}

		lPenMod = (PenaComplessivaModel) (lPComples.get(0));

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("Pena complessiva = " + lPenMod);

		// ==========================================================================
		// CALCOLO PENA GIA ESPIATA
		// ==========================================================================
		// Viene recuperata l'ultima PENA_RESIDUA VALIDATA e utilizzata la data
		// inizio come data inizio della pena già espiata e data fine la data corrente
		// (sysdate)
		// Viene quindi calcolato il quantum
		// n.b. questo è vero se il soggetto non è libero e detenuto per questa causa????
		// La pena già espiata non viene utilizzata per il calcolo della pena residua,
		// ma nel calcolo della fungibilità
		// Potrebbe accadere che esiste la data inizio ma abbia un valore futuro
		// rispetto alla data dei calcoli. In questo caso la pena espiata è ovviamente
		// null.
		PenaResiduaModel lPenRes = new PenaResiduaModel();

		IPenaResidua IPenRes = SIEPLookupRemote.getPenaResiduaRemote();

		Date lDataFineReclusione = null;
		Date lDataInizioArresto = null;
		Date lDataFinePena = null;
		Date lDataInizioPena = null;

		CalendarUtil lCalUtil = new CalendarUtil();
		CalendarModel lPenaGiaEspiata = new CalendarModel();

		PenaResiduaModel lUltimaPenRes = new PenaResiduaModel();
		lUltimaPenRes = IPenRes.ExRicercaPenaResiduaUltimaByDate(lFascID);
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("Ultima pena residua VALIDATA = " + lUltimaPenRes);

		if (lUltimaPenRes == null || lUltimaPenRes.getDataInizio() == null) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug(
					"ATTENZIONE! non esiste una pena validata a sistema o data inizio pena assente, impossibile calcolare la Pena Espiata");
			lGiornoInizio = "-";
		} else {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Calcolo la pena espiata");
			lDataInizioPena = lUltimaPenRes.getDataInizio();
			lDataFinePena = lUltimaPenRes.getDataFine();

			if (!DateUtils.isGreater(lDataInizioPena, dataSistemaPerCalcoli)) {
				// Il calcolo dell'espiato viene fatto solo se la data inizio<=data calcoli
				lPenaGiaEspiata.setDataInizio(lUltimaPenRes.getDataInizio());
				lPenaGiaEspiata.setDataFine(dataSistemaPerCalcoli);

				lPenaGiaEspiata = lCalUtil.CalcolaNumGiorniMesiAnni(lPenaGiaEspiata, false);
				lPenaGiaEspiata = lCalUtil.ricalcolaGAM(lPenaGiaEspiata);
			}
		}

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("Pena Già Espiata = " + lPenaGiaEspiata);

		// ==========================================================================
		// Controlla se il calcolo della pena deve essere AB INITIO o no
		// Se il calcolo è abInizio vengono prese in considerazione:
		// - PENA COMPLESSIVA IN SENTENZA
		// - BENEFICI (concessi, revocati)
		// - MISURE CAUTELARI (computabili)
		// - ANNOTAZIONI MANUALI (a sistema, tutte, validate o meno ma solo A/R richieste al GE)
		// Se non ab inizio:
		// - ultima pena residua VALIDATA
		// - ANNOTAZIONI MANUALI (a sistema solo A/R )
		//
		// ==========================================================================
		ICalcoloPena lCalPenCtrl = SIEPLookupRemote.getCalcoloPenaRemote();

		boolean lIsCalcoloPenaAbInitio = lCalPenCtrl.ExIsCalcoloPenaAbInizio(lFascID);

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("lIsCalcoloPenaAbInitio = " + lIsCalcoloPenaAbInitio);

		// ==========================================================================
		// Se il soggetto è libero, il calcolo non è ab inizio e manca una pena residua
		// validata, invece di partire dalla Pena Complessiva in Sentenza, parto
		// dalla pena residua non validata
		// Correzione del 09/2006 per computare anche benefici e misure cautelari
		// che altrimenti verrebbero escluse dal calcolo
		// ==========================================================================
		if (PGMod2.isLibero() && !lIsCalcoloPenaAbInitio && lUltimaPenRes == null) {
			// non testo se libero in quanto se non libero deve esistere la pena
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Carico l'ultima pena residua non validata ");
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
			lUltimaPenRes = IPenRes.ExRicercaPenaResiduaCorrenteByFascicoloSiep(lFascID);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Ultima pena non validata = " + lUltimaPenRes);
		}

		CalendarModel lBeneficiConcessiReclusione = new CalendarModel();
		CalendarModel lBeneficiRevocatiReclusione = new CalendarModel();
		CalendarModel lBeneficiConcessiArresto = new CalendarModel();
		CalendarModel lBeneficiRevocatiArresto = new CalendarModel();

		CalendarModel lBeneficiConcessiReclusioneJSP = new CalendarModel();
		CalendarModel lBeneficiRevocatiReclusioneJSP = new CalendarModel();
		CalendarModel lBeneficiConcessiArrestoJSP = new CalendarModel();
		CalendarModel lBeneficiRevocatiArrestoJSP = new CalendarModel();

		CalendarModel lMisCauComputabiliReclusione = new CalendarModel();
		CalendarModel lMisCauComputabiliArresto = new CalendarModel();

		if (!lIsCalcoloPenaAbInitio) {
			// Se CALCOLO PENA NON AB INITIO, utilizzo come punto di partenza per il
			// calcolo della pena residua, l'ultimo record pena residua VALIDATO (se esiste)
			// invece della pena complessiva in sentenza.
			// E comunque non considero i benefici e le misure cautelari
			// CALCOLO NUOVO QUANTUM (gg,mm,aa) A PARTIRE DALLE DATE
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug(
					"Calcolo pena non ab inizio: determino il punto di partenza per i calcoli a partire dalla pena residua VALIDATA a sistema");
			PenaResiduaModel lPenaRicalcolata = null;
			if (lUltimaPenRes != null && lUltimaPenRes.getDataInizio() != null
					&& lUltimaPenRes.getDataFine() != null) {
				// Punto di partenza Ultima Pena NORMALIZZATA in quanto ho le date
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("Pena residua in espiazione, ricalcolo i quantum (normalizzo)");

				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("Prima della normalizzazione = " + lUltimaPenRes);
				// normalizzo solo se presenti LLA computate
				lPenaRicalcolata = PenaResiduaUtil.calcolaPenaNuovaDataFine(lUltimaPenRes.getDataFine(),
						lUltimaPenRes, false);
				// lPenaRicalcolata = new PenaResiduaModel (lUltimaPenRes);
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("Dopo la normalizzazione = " + lPenaRicalcolata);

				lPenMod.setNumAnniArresto(lPenaRicalcolata.getNumAnniArresto());
				lPenMod.setNumMesiArresto(lPenaRicalcolata.getNumMesiArresto());
				lPenMod.setNumGiorniArresto(lPenaRicalcolata.getNumGiorniArresto());

				lPenMod.setImportoAmmenda(lPenaRicalcolata.getImportoAmmenda());

				lPenMod.setNumAnniReclusione(lPenaRicalcolata.getNumAnniReclusione());
				lPenMod.setNumMesiReclusione(lPenaRicalcolata.getNumMesiReclusione());
				lPenMod.setNumGiorniReclusione(lPenaRicalcolata.getNumGiorniReclusione());

				lPenMod.setImportoMulta(lPenaRicalcolata.getImportoMulta());
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug(
						"==> Punto di partenza Ultima Pena Residua VALIDATA e NORMALIZZATA: = " + lPenMod);
			} else if (lUltimaPenRes != null && PGMod2.isLibero()) {
				// Punto di partenza Ultima Pena NON NORMALIZZATA in quanto soggetto libero
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("Sono Libero: punto di partenza la pena residua a sistema");

				lPenMod.setNumAnniArresto(lUltimaPenRes.getNumAnniArresto());
				lPenMod.setNumMesiArresto(lUltimaPenRes.getNumMesiArresto());
				lPenMod.setNumGiorniArresto(lUltimaPenRes.getNumGiorniArresto());

				lPenMod.setImportoAmmenda(lUltimaPenRes.getImportoAmmenda());

				lPenMod.setNumAnniReclusione(lUltimaPenRes.getNumAnniReclusione());
				lPenMod.setNumMesiReclusione(lUltimaPenRes.getNumMesiReclusione());
				lPenMod.setNumGiorniReclusione(lUltimaPenRes.getNumGiorniReclusione());

				lPenMod.setImportoMulta(lUltimaPenRes.getImportoMulta());
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug(
						"==> Punto di partenza Ultima Pena Residua VALIDATA ma non normalizzata (libero): = "
								+ lPenMod);
			} else {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug(
						"ATTENZIONE!! Pena residua validata assente o mancante di date di non in espiazione");
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("==> Punto di partenza resta la Pena Complessiva");
			}
		} else { // Pena abInizio ho già la pena Complessiva, recupero Benefici e MC computabili
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
			siesLogger.debug("Calcolo pena ab inizio recupero i dati per i calcoli");
			// BENEFICI
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Recupero i benefici concessi e revocati (Reclusione/Arresto");
			lBeneficiConcessiReclusione = new CalendarModel(
					lCalPenCtrl.exGetBeneficiConcessiReclusione(lFascID));
			lBeneficiRevocatiReclusione = new CalendarModel(
					lCalPenCtrl.exGetBeneficiRevocatiReclusione(lFascID));
			lBeneficiConcessiArresto = new CalendarModel(lCalPenCtrl.exGetBeneficiConcessiArresto(lFascID));
			lBeneficiRevocatiArresto = new CalendarModel(lCalPenCtrl.exGetBeneficiRevocatiArresto(lFascID));

			lBeneficiConcessiReclusioneJSP = new CalendarModel(lBeneficiConcessiReclusione);
			lBeneficiRevocatiReclusioneJSP = new CalendarModel(lBeneficiRevocatiReclusione);
			lBeneficiConcessiArrestoJSP = new CalendarModel(lBeneficiConcessiArresto);
			lBeneficiRevocatiArrestoJSP = new CalendarModel(lBeneficiRevocatiArresto);

			// MISURE CAUTELARI
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Recupero le Misure Cautelari Computabili");
			lMisCauComputabiliReclusione = new CalendarModel(
					lCalPenCtrl.exGetMisureCautelariComputabiliReclusione(lFascID));
			lMisCauComputabiliArresto = new CalendarModel(
					lCalPenCtrl.exGetMisureCautelariComputabiliArresto(lFascID));
		}

		/**
		 * TODO fino a qui come ActCalcoloPenaComputo
		 */
		// ==========================================================================
		// DA QUI LE EFFETTIVE ANNOTAZIONI MANUALI
		// ==========================================================================
		String lCodTipoAnnotazione = getRequestStringParameter(CAMPO_COD_TIPO_ANNOTAZIONE);

		// Qui recupera dati differenti
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("Recupero le Annotazioni manuali");

		// ==========================================================================
		// Attenzione vengono prese in considerazione tutte le annotazioni manuali
		// manuali inserite con anticipazione degli effetti (FLAG_ANNOTAZIONE_PROVVISORIA = A).
		// Se il calcolo è abInizio prendo in considerazione tutte le annotazioni,
		// altrimenti solo quelle non validate dello stesso tipo di quella corrente
		// (di fatti prendo in considerazione solo quelle correnti)

		// Recupero le annotazioni con anticipazione (Richieste)
		CalendarModel lAnnManConcessiReclusione = new CalendarModel(
				lCalPenCtrl.exGetAnnotazioniManualiConcessiAnticipazioneReclusione(lFascID,
						lCodTipoAnnotazione, lIsCalcoloPenaAbInitio));
		CalendarModel lAnnManRevocatiReclusione = new CalendarModel(
				lCalPenCtrl.exGetAnnotazioniManualiRevocatiAnticipazioneReclusione(lFascID,
						lCodTipoAnnotazione, lIsCalcoloPenaAbInitio));
		CalendarModel lAnnManConcessiArresto = new CalendarModel(
				lCalPenCtrl.exGetAnnotazioniManualiConcessiAnticipazioneArresto(lFascID, lCodTipoAnnotazione,
						lIsCalcoloPenaAbInitio));
		CalendarModel lAnnManRevocatiArresto = new CalendarModel(
				lCalPenCtrl.exGetAnnotazioniManualiRevocatiAnticipazioneArresto(lFascID, lCodTipoAnnotazione,
						lIsCalcoloPenaAbInitio));

		// SOLO se il calcolo è abInizio, recupero anche le altre annotazioni
		// inserite con presofferti, fungibilità... che devono essere computate nel
		// quantum finale
		if (lIsCalcoloPenaAbInitio) {
			CalendarModel lAnnManConcessiReclusioneAltro = new CalendarModel(
					lCalPenCtrl.exGetAnnotazioniManualiConcessiReclusione(lFascID, lCodTipoAnnotazione,
							lIsCalcoloPenaAbInitio));
			CalendarModel lAnnManRevocatiReclusioneAltro = new CalendarModel(
					lCalPenCtrl.exGetAnnotazioniManualiRevocatiReclusione(lFascID, lCodTipoAnnotazione,
							lIsCalcoloPenaAbInitio));
			CalendarModel lAnnManConcessiArrestoAltro = new CalendarModel(
					lCalPenCtrl.exGetAnnotazioniManualiConcessiArresto(lFascID, lCodTipoAnnotazione,
							lIsCalcoloPenaAbInitio));
			CalendarModel lAnnManRevocatiArrestoAltro = new CalendarModel(
					lCalPenCtrl.exGetAnnotazioniManualiRevocatiArresto(lFascID, lCodTipoAnnotazione,
							lIsCalcoloPenaAbInitio));
			// Sommo il tutto
			lAnnManConcessiReclusione = new CalendarModel(
					lCalUtil.sommaGiornieValute(lAnnManConcessiReclusione, lAnnManConcessiReclusioneAltro));
			lAnnManRevocatiReclusione = new CalendarModel(
					lCalUtil.sommaGiornieValute(lAnnManRevocatiReclusione, lAnnManRevocatiReclusioneAltro));
			lAnnManConcessiArresto = new CalendarModel(
					lCalUtil.sommaGiornieValute(lAnnManConcessiArresto, lAnnManConcessiArrestoAltro));
			lAnnManRevocatiArresto = new CalendarModel(
					lCalUtil.sommaGiornieValute(lAnnManRevocatiArresto, lAnnManRevocatiArrestoAltro));
		}

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("lAnnManConcessiReclusione = " + lAnnManConcessiReclusione);
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("lAnnManRevocatiReclusione = " + lAnnManRevocatiReclusione);
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("lAnnManConcessiArresto    = " + lAnnManConcessiArresto);
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("lAnnManRevocatiArresto    = " + lAnnManRevocatiArresto);

		// CalendarModel lAnnManTotaliArresto = new
		// CalendarModel(lCalCon.sommaGiornieValute(lAnnManConcessiArresto,lAnnManRevocatiArresto));
		// CalendarModel lAnnManTotaliReclusione = new
		// CalendarModel(lCalCon.sommaGiornieValute(lAnnManConcessiReclusione,lAnnManRevocatiReclusione));

		CalendarModel lAnnManConcessiReclusioneJSP = new CalendarModel(lAnnManConcessiReclusione);
		CalendarModel lAnnManRevocatiReclusioneJSP = new CalendarModel(lAnnManRevocatiReclusione);
		CalendarModel lAnnManConcessiArrestoJSP = new CalendarModel(lAnnManConcessiArresto);
		CalendarModel lAnnManRevocatiArrestoJSP = new CalendarModel(lAnnManRevocatiArresto);

		// Sommo Annotazioni Manuali e Benefici .....
		lBeneficiConcessiReclusione = new CalendarModel(
				lCalUtil.sommaGiornieValute(lBeneficiConcessiReclusione, lAnnManConcessiReclusione));
		lBeneficiRevocatiReclusione = new CalendarModel(
				lCalUtil.sommaGiornieValute(lBeneficiRevocatiReclusione, lAnnManRevocatiReclusione));
		lBeneficiConcessiArresto = new CalendarModel(
				lCalUtil.sommaGiornieValute(lBeneficiConcessiArresto, lAnnManConcessiArresto));
		lBeneficiRevocatiArresto = new CalendarModel(
				lCalUtil.sommaGiornieValute(lBeneficiRevocatiArresto, lAnnManRevocatiArresto));

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("--------------------------------------");
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("Benefici Arresto Concessi : " + lBeneficiConcessiArrestoJSP.getImportoAmmenda()
				+ "----" + lBeneficiConcessiArrestoJSP.getNumAnni() + "/"
				+ lBeneficiConcessiArrestoJSP.getNumMesi() + "/"
				+ lBeneficiConcessiArrestoJSP.getNumGiorni());
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("Benefici Arresto Revocati : " + lBeneficiRevocatiArrestoJSP.getImportoAmmenda()
				+ "----" + lBeneficiRevocatiArrestoJSP.getNumAnni() + "/"
				+ lBeneficiRevocatiArrestoJSP.getNumMesi() + "/"
				+ lBeneficiRevocatiArrestoJSP.getNumGiorni());
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("--------------------------------------");
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("--------------------------------------");
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("Benefici Reclusione Concessi : " + lBeneficiConcessiReclusione.getImportoAmmenda()
				+ "----" + lBeneficiConcessiReclusione.getNumAnni() + "/"
				+ lBeneficiConcessiReclusione.getNumMesi() + "/"
				+ lBeneficiConcessiReclusione.getNumGiorni());
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("Benefici Reclusione Revocati : " + lBeneficiRevocatiReclusione.getImportoAmmenda()
				+ "----" + lBeneficiRevocatiReclusione.getNumAnni() + "/"
				+ lBeneficiRevocatiReclusione.getNumMesi() + "/"
				+ lBeneficiRevocatiReclusione.getNumGiorni());
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("--------------------------------------");

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("--------------------------------------");
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("Totali Arresto Concessi : " + lBeneficiConcessiArresto.getImportoAmmenda() + "----"
				+ lBeneficiConcessiArresto.getNumAnni() + "/" + lBeneficiConcessiArresto.getNumMesi() + "/"
				+ lBeneficiConcessiArresto.getNumGiorni());
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("Totali Arresto Revocati : " + lBeneficiRevocatiArresto.getImportoAmmenda() + "----"
				+ lBeneficiRevocatiArresto.getNumAnni() + "/" + lBeneficiRevocatiArresto.getNumMesi() + "/"
				+ lBeneficiRevocatiArresto.getNumGiorni());
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("--------------------------------------");
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("--------------------------------------");
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("Totali Reclusione Concessi : " + lBeneficiConcessiReclusione.getImportoMulta()
				+ "----" + lBeneficiConcessiReclusione.getNumAnni() + "/"
				+ lBeneficiConcessiReclusione.getNumMesi() + "/"
				+ lBeneficiConcessiReclusione.getNumGiorni());
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("Totali Reclusione Revocati : " + lBeneficiRevocatiReclusione.getImportoMulta()
				+ "----" + lBeneficiRevocatiReclusione.getNumAnni() + "/"
				+ lBeneficiRevocatiReclusione.getNumMesi() + "/"
				+ lBeneficiRevocatiReclusione.getNumGiorni());
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("--------------------------------------");

		// -------------------------------------------------------
		// FINE ANNOTAZIONI MANUALI
		// -------------------------------------------------------

		// ==========================================================================
		// Solo se non ergastolo viene rieffettuato il vero calcolo della pena,
		// vale a dire vengono:
		// - ricalcolati i quantum
		// - le date di decorrenza
		// - l'eventuale fungibilità
		//
		// Se ergastolo invece:
		// - copia i dati dell'ultima pena
		// -
		// ==========================================================================
		if (!(lPenMod.getCodTipoPenaDetentiva().equals("03")
				|| lPenMod.getCodTipoPenaDetentiva().equals("04"))) {
			boolean ForzaFungibilita = false;
			String lSegnalazione = "N";

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("FORZAFUNG:" + ForzaFungibilita);

			//
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("INIZIO CALCOLO QUANTUM DI PENA");

			PenaResiduaModel lPenaComplessiva = null;
			lPenaComplessiva = lCalPenCtrl.exCalcolaQuantumPenaComplessivaNuovo(lFascID, lPenMod,
					lBeneficiConcessiReclusione, lBeneficiRevocatiReclusione, lBeneficiConcessiArresto,
					lBeneficiRevocatiArresto, lMisCauComputabiliReclusione, lMisCauComputabiliArresto,
					getCodUtenteConnesso(), getCodUfficioUtenteConnesso(), ForzaFungibilita, lPenaGiaEspiata);

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("lPenaResidua Rideterminata dopo calcoli = " + lPenaComplessiva);

			// n.b. lTotRideterminato non è la fungibilità, ma il totale residuo
			// rideterminato può essere positivo o eventualmente negativo
			//
			CalendarModel lTotRideterminato = new CalendarModel();

			lTotRideterminato.setNumAnni(
					lPenaComplessiva.getNumAnniArresto().add(lPenaComplessiva.getNumAnniReclusione()));
			lTotRideterminato.setNumMesi(
					lPenaComplessiva.getNumMesiArresto().add(lPenaComplessiva.getNumMesiReclusione()));
			lTotRideterminato.setNumGiorni(
					lPenaComplessiva.getNumGiorniArresto().add(lPenaComplessiva.getNumGiorniReclusione()));

			//
			if (!detenutoAltraCausa && (lCalUtil.isGreater(lTotRideterminato, lPenaGiaEspiata)
					|| lCalUtil.isZero(lTotRideterminato))) {
				ForzaFungibilita = false;
				// ChiedereValidazionePena="N";
			}

			BigDecimal lIndicePenaResidua = null;
			lIndicePenaResidua = lPenaComplessiva.getIdPenaResidua();
			String vedoDataIntermedia = "S";

			// =======================================================================
			// lDataInizioPena disponibile solo se presente a sistema una pena
			// residua validata o meno e coincide con tale data
			// =======================================================================
			// =======================================================================
			// Determino le date di espiazione se possibile e aggiorno il record
			// pena residua. Le date vengono calcolate sole se esiste una data di
			// decorrenza (lDataInizioPena) di un record FINE PENA VALIDATO.
			// - lDataInizioPena (Data Inizio Ultima Pena Validata)
			// - lDataFineReclusione (Calcolata se presente sia reclusione che erresto)
			// - lDataInizioArresto (Data Fine Reclusione + 1, o data inizio pena)
			// - setDataFinePresunta (data fine calcolata)
			// - lDataFinePena (non inserita in questa fase)
			// Se il quantum ricalcolato risulta <=0:
			// - lDataInizioPena (Data Inizio Ultima Pena Validata)
			// - lDataFinePena (Data Fine Ultima Pena Validata)
			// Le date non vengono ricalcolate ma restano quelle della pena in
			// espiazione.
			// =======================================================================
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Data inizio Pena: " + lDataInizioPena);
			if (lDataInizioPena != null) {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug(
						"-------------------------------------------------------------------------------");
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug(" Ricalcolo le date di espiazione a partire dalla data inizio ("
						+ lDataInizioPena + ") della pena a sistema (validata) e del quantum calcolato ("
						+ lPenaComplessiva + ")");
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug(
						"-------------------------------------------------------------------------------");

				Vector lDateFine = lCalPenCtrl.exCalcolaDataFinePena(lDataInizioPena, lPenaComplessiva, true);

				lVedoJSP = "S";

				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("Date intermedie calcolate " + lDateFine.size());

				if (lDateFine.size() == 1) { // solo Reclusione o Arresti: ho quindi solo data fine
												// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di
												// istanza siesLogger al posto di LogF3B.getLogger()
					siesLogger.debug("Ho solo una data: " + lDateFine.get(0));
					vedoDataIntermedia = "N";

					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger.debug("USCITO DAL CALCOLO DELLA DATA FINE PENA CON : " + lDateFine.get(0)
							+ " con " + lDateFine.size() + " elementi");

					boolean cond = lPenaComplessiva.getNumAnniArresto().equals(null)
							|| lPenaComplessiva.getNumAnniArresto().intValue() == 0;
					cond = cond && (lPenaComplessiva.getNumMesiArresto().equals(null)
							|| lPenaComplessiva.getNumMesiArresto().intValue() == 0);
					cond = cond && (lPenaComplessiva.getNumGiorniArresto().equals(null)
							|| lPenaComplessiva.getNumGiorniArresto().intValue() == 0);

					if (!cond)
						lDataInizioArresto = lDataInizioPena;

					lDataFinePena = (Date) lDateFine.get(0);
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger.debug("Data fine ricalcolata: " + lDataFinePena);
				}

				/**
				 * TODO n.b. lDataFinePena potrebbe essere valorizzato con la data fine pena dell'ultimo
				 * record pena residua validato (vedi precedente calcolo pena già espiata) Se invece non
				 * esiste un record data fine validato e lDateFine.size()==2, lDataFinePena=null per cui non
				 * entro nel nell'if e lascio le date fine a null Se invece lDataFinePena dell'ultimo record
				 * Validato>oggi
				 */
				/*
				 * Se sono nell'if (lDataInizioPena!=null) sicuramente è !=null anche lDataFinePena e vale: 1)
				 * data fine ricalcolata nell'if precedente (lDateFine.size() == 1) ossia ho pena ricalcolata
				 * > 0 e un solo periodo di espiazione 2) data fine di ultima pena residua (validata o meno)
				 * prima del ricalcolo
				 * 
				 */

				// if (lDataFinePena!=null && lDataFinePena.after(DateUtils.getSysDate()))
				if (lDataFinePena != null && lDataFinePena.after(dataSistemaPerCalcoli)) {
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger.debug("Data fine ricalcolata > data di systema");
					if (lDateFine.size() == 2) {
						lDataFineReclusione = (Date) lDateFine.get(0);
						lDataInizioArresto = DateUtils.getDayAfter(lDataFineReclusione);
						lDataFinePena = (Date) lDateFine.get(1);
					}
				}

				if (lDateFine.size() == 0) { // quantum negativi (almeno uno dei due periodi) o nulli
					lSegnalazione = "S"; //
				}
				/*
				 * if (!detenutoAltraCausa) ChiedereValidazionePena="N";
				 */

				if (lDateFine.size() != 0 && !ForzaFungibilita) {
					// Se ho quantum positivi Aggiorno le date della pena residua
					// precedentemente inserita
					lPenaComplessiva.setIdPenaResidua(lIndicePenaResidua);

					lPenaComplessiva.setDataInizio(lDataInizioPena); // Data inizio della vecchia Pena residua
																		// (è sempre la stessa)
					lPenaComplessiva.setDataFineReclusione(lDataFineReclusione);
					lPenaComplessiva.setDataInizioArresto(lDataInizioArresto);
					lPenaComplessiva.setDataFinePresunta(lDataFinePena);

					lPenaComplessiva.setDiesAQuo("S");

					lPenaComplessiva.setCodOperatoreInserimento(getCodUtenteConnesso());
					lPenaComplessiva.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
					lPenaComplessiva.setDataInserimento(DateUtils.getSysDate());

					IPenaResidua lPPres = SIEPLookupRemote.getPenaResiduaRemote();
					lPPres.ExModificaPenaResidua(lPenaComplessiva);
				} else { // lDateFine.size() = 0 per cui ho dei quantum negativi o nulli,
							// con soggetto in espiazione (il che implica che necessariamente
							// ho una fungibilità) aggiorno la data inizio e data fine
							// lDataFinePena = data fine pena attuale, non quella ricalcolata

					// Se il quantum di pena ricalcolato è negativo o nullo lo lascio
					// a sistema, ma lascio anche le date di decorrenza previste per
					// prima del calcolo solo la data
					lPenaComplessiva.setIdPenaResidua(lIndicePenaResidua);

					lPenaComplessiva.setDataInizio(lDataInizioPena); // data decorrenza pena in corso di
																		// espiazione
					lPenaComplessiva.setDataFine(lDataFinePena); // è la data fine pena attuale, non quella
																	// rideterminata

					lPenaComplessiva.setDiesAQuo("S");

					lPenaComplessiva.setCodOperatoreInserimento(getCodUtenteConnesso());
					lPenaComplessiva.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
					lPenaComplessiva.setDataInserimento(DateUtils.getSysDate());

					IPenaResidua lPPres = SIEPLookupRemote.getPenaResiduaRemote();

					// ??? data fine pena è quella prevista prima dell'inizio dei calcoli
					// se sono giunto quì vuol dire che la pena rideterminata è <=0
					// in questo caso aggiorno le date a sistema solo se il soggetto
					// non è ancora in espiazione.
					// In questo caso ho un soggetto in espiazione al momento del computo
					// (data fine pena < data sistema) al quale sono stati applicati
					// dei benefici tali da azzerargli la pena o portarla addirittura
					// in negativo. Devo comunque inserire le date sulla pena residua.
					// if (lDataFinePena.after(DateUtils.getSysDate()))
					if (lDataFinePena.after(dataSistemaPerCalcoli))
						lPPres.ExModificaPenaResidua(lPenaComplessiva);
				}
			}

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug(
					"DataInizioPena     = " + DateUtils.getDateToString(lDataInizioPena, "dd/MM/yyyy"));
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug(
					"DataFineReclusione = " + DateUtils.getDateToString(lDataFineReclusione, "dd/MM/yyyy"));
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug(
					"DataInizioArresto  = " + DateUtils.getDateToString(lDataInizioArresto, "dd/MM/yyyy"));
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger
					.debug("DataFinePena       = " + DateUtils.getDateToString(lDataFinePena, "dd/MM/yyyy"));

			// =======================================================================
			// NUOVA VERSIONE DELLA GESTIONE DELLA FUNGIBILITA
			// =======================================================================
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("-------------------------------------------");
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("  Determino l'eventuale Fungibilità:       ");
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("-------------------------------------------");
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Tot Pena Rideterminata (reclusione+arresti): Anni: "
					+ lTotRideterminato.getNumAnni() + ", Mesi: " + lTotRideterminato.getNumMesi()
					+ ", Giorni: " + lTotRideterminato.getNumGiorni());
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Pena Già Espiata                           : Anni: "
					+ lPenaGiaEspiata.getNumAnni() + ", Mesi: " + lPenaGiaEspiata.getNumMesi() + ", Giorni: "
					+ lPenaGiaEspiata.getNumGiorni());

			CalendarModel lFungCalendar = new CalendarModel();

			if (lCalUtil.isZero(lTotRideterminato)) // Se la pena residua calcolata vale 0
			{
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("Pena complessiva rideterminata = 0");

				if (lDataInizioPena != null) {
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger.debug("Pena in espiazione, fungibilità = pena già espiata.");
					lFungCalendar = new CalendarModel(lPenaGiaEspiata);

					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger.debug("Reimposto la data fine pena = data inizio");
					lDataFinePena = lUltimaPenRes.getDataInizio(); // ???????? Perchè ???
				}

				ForzaSysdateManuale = "S"; // ????
			} else if (lCalUtil.isPositiveTime(lTotRideterminato)) {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("Tot Pena Rideterminata positivo");
				if (lDataInizioPena != null
						// && lDataFinePena.before(dataSistemaPerCalcoli)
						&& DateUtils.isLower(lDataFinePena, dataSistemaPerCalcoli)) {
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger.debug("Pena Rideterminata<=Pena Espiata");
					// Questo è il caso classico in cui la pena rideterminata è
					// minore dell'espiato
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger.debug("Data fine pena (" + lDataFinePena + ") inferiore alla data di sistema");
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger.debug(
							"Calcolo la fungibilità come intervallo tra la data fine ricalcolata (quella in cui sarebbe dovuto uscire) e la data di systema: ");
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger.debug("Data fine prevista (ricalcolata) = " + lDataFinePena);
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger.debug("Data di sistema = " + dataSistemaPerCalcoli);

					CalendarModel tmp = new CalendarModel();
					tmp.setDataInizio(lDataFinePena);
					tmp.setDataFine(dataSistemaPerCalcoli);
					lFungCalendar = lCalUtil.CalcolaNumGiorniMesiAnni(tmp, true);

					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger.debug("Fungibilità calcolata = " + lFungCalendar);
				} else {
					// La data fine pena è >= alla data di calcolo per cui non ho
					// fungibilità al più devo uscire subito
				}
			} else {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("Quantum rideterminato negativo");
				if (lDataInizioPena != null && !DateUtils.isGreater(lDataInizioPena, dataSistemaPerCalcoli)) {
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger.debug(
							"Soggetto in espiazione, calcolo la fungibilità come somma dell'espiato e del quantum negativo");
					lFungCalendar = lCalUtil.sommaGiorni(lCalUtil.abs(lTotRideterminato), lPenaGiaEspiata);
				} else {
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger.debug(
							"Soggetto non in espiazione, la fungibilità è pari al quantum rideterminato negativo");
					lFungCalendar = lCalUtil.abs(lTotRideterminato);
				}
			}

			// =======================================================================
			// CALCOLO FUNGIBILITA'
			// - il calcolo della fungibilità comporta anche il ricalcolo delle
			//
			// =======================================================================
			// /*
			// * lTotRideterminato = pena residua complessiva da espiare ottenuta sommando
			// * reclusione e arresto dopo il calcolo
			// * Verifico se devo calcolare la fungibilità. E' indipendente dal fatto
			// * che il soggetto sia in espiazione o meno
			// */
			// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			// siesLogger.debug("-------------------------------------------");
			// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			// siesLogger.debug(" Determino l'eventuale Fungibilità: ");
			// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			// siesLogger.debug("-------------------------------------------");
			// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			// siesLogger.debug("Tot Pena Rideterminata (reclusione+arresti): Anni:
			// "+lTotRideterminato.getNumAnni()+", Mesi: "+lTotRideterminato.getNumMesi()+", Giorni:
			// "+lTotRideterminato.getNumGiorni());
			// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			// siesLogger.debug("Pena Già Espiata : Anni: "+lPenaGiaEspiata.getNumAnni()+", Mesi:
			// "+lPenaGiaEspiata.getNumMesi()+", Giorni: "+lPenaGiaEspiata.getNumGiorni());
			// CalendarModel lFungCalendar=new CalendarModel();
			//
			//// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			//// siesLogger.debug(" Totale Pena residua ricalcolata (reclusione+arresti)=
			// "+lTotRideterminato);
			// if ( lCalCon.isZero(lTotRideterminato) ) // Se la pena residua calcolata vale 0
			// {
			// lFungCalendar = new CalendarModel();
			// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			// siesLogger.debug("Pena complessiva rideterminata = 0");
			//
			// if(lUltimaPenRes != null && lUltimaPenRes.getDataInizio() != null)
			// { // Soggetto in espiazione, pena residua 0,
			// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			// siesLogger.debug("Reimposto la data fine pena = data inizio");
			// lDataFinePena=lUltimaPenRes.getDataInizio();
			// }
			//
			// // Nel caso di nuova Pena Residua a 0 la Fungibilità vale:
			// // - 0 se il soggetto è libero (non in espiazione)
			// // - pena già espiata se il soggetto è in espiazione (tutto il periodo fin qui espiato non
			// doveva esserlo)
			// // il problema è che la pena espiata viene calcolata
			// lFungCalendar=new CalendarModel(lPenaGiaEspiata);
			// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			// siesLogger.debug("Pena complessiva rideterminata = 0, fungibilità = pena già espiata se esiste,
			// altrimenti 0: Fungibilità="+lFungCalendar);
			// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			// siesLogger.debug("Data fine pena = "+lDataFinePena);
			// ForzaSysdateManuale="S";
			// }
			// else
			// { // Residuo pena > 0 o <0
			// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			// siesLogger.debug("Pena complessiva rideterminata <> 0");
			// if ( (!lCalCon.isGreater(lTotRideterminato, lPenaGiaEspiata) )) //|| ForzaFungibilita)) //Nuova
			// Pena da Espiare < Pena già espiata
			// {
			// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			// siesLogger.debug("Pena complessiva rideterminata <= pena già espiata ");
			// /*
			// * n.b. se in espiazione: vuol dire che sarei dovuto già uscire
			// * se libero : lPenaGiaEspiata = 0 quindi vera solo se la pena rideterminata risulta < 0
			// * Nel primo caso: la fungibilità (pena espiata in eccesso è data dalla differenza
			// espiato-rideterminato
			// * Nel primo caso: non avendo espiato nulla non ho fungibilità
			// */
			// /*
			// * La pena rideterminata è negativa o comunque minore di ciò che
			// * ha già espiato per cui l'eventuale data fine pena sarà minore
			// * della data di sistema per cui propongo la data di sistema
			// */
			// ForzaSysdateManuale="S"; // perchè??
			//
			// if (lDataInizioPena != null)
			// { // Soggetto in espiazione: in questo caso la pena rideterminata
			// // potrebbe essere sia <0 che >0
			// // n.b. non è detto che il soggetto sia in espiazione perchè la data
			// // inizio potrebbe essere futura
			// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			// siesLogger.debug("Soggetto in espiazione...");
			// if (detenutoQuestaCausa)
			// {
			// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			// siesLogger.debug("...per questa causa");
			// /*
			// * Se lDataInizioPena!=null vuol dire che esiste un record pena
			// * residua VALIDATO e il soggetto è in espiazione per cui deve
			// * esistere anche una data fine
			// *
			// * Attenzione a questo punto al valore dalla data lDataFinePena:
			// * - vale null nei seguenti casi:
			// * - pena residua iniziale senza una data inizio (soggetto non in espiazione)IMPOSSIBILE
			// * - quantum rideterminato <0 (NO!!!!!!!!!!!!!)
			// * - è diversa da null se presente sulla pena iniziale e vale
			// * - data rideterminata con il nuovo quantum se un solo periodo
			// * - data iniziale se quantum rideterminato < 0
			// * - data iniziale se quantum > 0,
			// * due periodi residui (reclusione e arresto)
			// * e data fine pena INIZIALE > data odierna. Infatti
			// */
			// if (lDataFinePena == null)
			// { // quantum rideterminato <0
			// // Questa parte di codice non viene mai eseguita, infatti la
			// // data fine pena è valorizzata se lo è la data inizio, e anche
			// // se il quantum rideterminato risulta < 0 non viene modificata
			// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			// siesLogger.debug("ENTRO SU DATA FINE A NULL vale a dire quantum rideterminato < 0 ");
			//
			//// if (lPenaGiaEspiata.getDataFine().before(DateUtils.getSysDate()))
			// if (lPenaGiaEspiata.getDataFine().before(dataSistemaPerCalcoli))
			// {
			// //Attenzione!!! lPenaGiaEspiata.getDataFine()=DateUtils.getSysDate()
			// // in quanto è stata settata così
			// // QUESTA CONDIZIONE é SEMPRE FALSA!!!!!!!!!!!!!!!!!!!!!!!!!
			// CalendarModel tmp = new CalendarModel();
			//// tmp.setDataFine (DateUtils.getSysDate());
			// tmp.setDataFine (dataSistemaPerCalcoli);
			// tmp.setDataInizio (lPenaGiaEspiata.getDataFine());
			// lFungCalendar = lCalCon.CalcolaNumGiorniMesiAnni(tmp);
			// }
			// }
			// else
			// { // quantum rideterminato > 0 NON è vero perchè se il quantum <=0
			// // la data fine pena resta quella iniziale. Non viene mai modificata
			// // per cui a questo punto non so se è la data fine rideterminata
			// // con quantum positivi, o la data iniziale per via dei quantum negativ
			// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			// siesLogger.debug("ENTRO SU DATA FINE NON A NULL!!!!!!");
			// //if (lDataFinePena.before(DateUtils.getSysDate()))
			// if (lDataFinePena.before(dataSistemaPerCalcoli))
			// { // Questo è il caso classico in cui la pena rideterminata è
			// // minore dell'espiato
			// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			// siesLogger.debug("Soggetto in espiazione con Data fine pena (eventualmente
			// ricalcolata)"+lDataFinePena+" inferiore alla data di systema");
			// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			// siesLogger.debug("Calcolo la fungibilità come intervallo tra la data fine ricalcolata (quella
			// in cui sarebbe dovuto uscire) e la data di systema: ");
			// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			// siesLogger.debug("Data fine prevista (ricalcolata) = "+lDataFinePena);
			// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			// siesLogger.debug("Data di sistema = "+dataSistemaPerCalcoli);
			// CalendarModel tmp = new CalendarModel();
			//// tmp.setDataInizio (DateUtils.getDayAfter(lDataFinePena));
			// tmp.setDataInizio (lDataFinePena);
			// //tmp.setDataFine (DateUtils.getSysDate());
			// tmp.setDataFine (dataSistemaPerCalcoli);
			//
			// //lFungCalendar = lCalCon.CalcolaNumGiorniMesiAnni(tmp);
			// lFungCalendar = lCalCon.CalcolaNumGiorniMesiAnni(tmp,true);
			// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			// siesLogger.debug("Fungibilità calcolata = "+lFungCalendar);
			// }
			// }
			// }
			// else if (FlagAltraCausa.equals("S"))
			// {
			// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			// siesLogger.debug("... per altra causa");
			// // A rigor di logica se sono detenuto altra causa la data inizio
			// // della pena residua di QUESTO procedimento dovrebbe essere null
			// // per cui anche la data fine pena dovrebbe essere null per cui
			// // questa condizione sempre false
			//// if (lDataFinePena.before(DateUtils.getSysDate()))
			// if (lDataFinePena.before(dataSistemaPerCalcoli))
			// { // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			// { siesLogger.debug("Data fine prevista < data di sistema");
			// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			// siesLogger.debug("Calcolo la fungibilità come intervallo tra la data fine ricalcolata (quella
			// in cui sarebbe dovuto uscire) e la data di systema: ");
			// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			// siesLogger.debug("Data fine prevista (ricalcolata) = "+lDataFinePena);
			// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			// siesLogger.debug("Data di sistema = "+dataSistemaPerCalcoli);
			//
			// CalendarModel tmp = new CalendarModel();
			//
			// tmp.setDataInizio(lDataFinePena);
			//// tmp.setDataFine(DateUtils.getSysDate());
			// tmp.setDataFine(dataSistemaPerCalcoli);
			// //lFungCalendar = lCalCon.CalcolaNumGiorniMesiAnni(tmp);
			// lFungCalendar = lCalCon.CalcolaNumGiorniMesiAnni(tmp,true);
			// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			// siesLogger.debug("Fungibilità calcolata = "+lFungCalendar);
			// }
			// }
			// else
			// { // Libero con data fine valorizzata ????????? e chi me la dà
			// // Libero con data inizio valorizzata e quantum rideterminato
			// // > 0
			// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			// siesLogger.debug("...libero, ma con data fine pena valorizzata:"+lDataFinePena);
			// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			// siesLogger.debug("In questo caso calcolo la fungibilità solo se la data fine pena ricalcolata <
			// data di sistema");
			//// if (lDataFinePena.before(DateUtils.getSysDate())) {
			// if (lDataFinePena.before(dataSistemaPerCalcoli)) {
			// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			// siesLogger.debug("Calcolo la fungibilità come: Pena rideterminata - pena già espiata ");
			// lFungCalendar = lCalCon.ricalcolaGAM(lCalCon.BeneficisottraiGiornieValute(lTotRideterminato,
			// lPenaGiaEspiata));
			// }
			// }
			// }
			// else {
			// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			// siesLogger.debug("Quantum rideterminato < 0, calcolo la fungibilità come differenza tra la pena
			// rideterminata e quella già espiata");
			// lFungCalendar = lCalCon.ricalcolaGAM(lCalCon.BeneficisottraiGiornieValute(lTotRideterminato,
			// lPenaGiaEspiata));
			// }
			// }
			// }
			//
			// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			// siesLogger.debug("---------------------------------------------------------------");
			// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			// siesLogger.debug("FUNGIBILITA : AA:" + lFungCalendar.getNumAnni() +
			// "MM:"+lFungCalendar.getNumMesi() + "GG:"+lFungCalendar.getNumGiorni());
			// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			// siesLogger.debug("---------------------------------------------------------------");
			//
			// if (lCalCon.isZero(lTotRideterminato) && !detenutoQuestaCausa && FlagAltraCausa.equals("N")) {
			// // Se libero (07,10,16,17)
			// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			// siesLogger.debug("Soggetto libero (07,10,16,17) e non detenuto per altra causa, ridetermino la
			// fungibilità peri alla pena già espiata");
			// lFungCalendar=new CalendarModel(lPenaGiaEspiata);
			// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			// siesLogger.debug("fungibilità rideterminata = "+lFungCalendar);
			// }
			//

			FungibilitaModel lFunMod = new FungibilitaModel();
			if (!lCalUtil.isZero(lFungCalendar)) {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("_________________________________________");

				lFunMod.setFasSieIdFascicoloSiep(lFascID);

				lFunMod.setCodTipoFungibilita("02"); // 02=PENA ESPIATA IN ECCESSO ....Cg_ref_codes 24/10/2003
				lFunMod.setNumAnni(new BigDecimal(lFungCalendar.getNumAnni() + ""));
				lFunMod.setNumMesi(new BigDecimal(lFungCalendar.getNumMesi() + ""));
				lFunMod.setNumGiorni(new BigDecimal(lFungCalendar.getNumGiorni() + ""));
				lFunMod.setFlagValidato("N");

				lFunMod.setCodOperatoreInserimento(getCodUtenteConnesso());
				lFunMod.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
				lFunMod.setDataInserimento(DateUtils.getSysDate());

				BigDecimal lEveIdEvento = getRequestBigDecimalParameter(CAMPO_EVE_ID_EVENTO);
				lFunMod.setEveIdEvento(lEveIdEvento);

				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("Inserisco a sistema la fungibilità: " + lFunMod);

				IFungibilita iFun = SIEPLookupRemote.getFungibilitaRemote();
				lFunMod = iFun.ExInserisciFungibilita(lFunMod);

				if (detenutoAltraCausa)
					lDataFinePena = dataSistemaPerCalcoli;
				// lDataFinePena=DateUtils.getSysDate();
			}

			setRequestAttribute("lIsCalcoloPenaAbInitio", new Boolean(lIsCalcoloPenaAbInitio));

			setRequestAttribute("IdFungibilita", lFunMod.getIdFungibilita() + "");
			setRequestAttribute("ChiedereValidazionePena", ChiedereValidazionePena);

			setRequestAttribute("BeneficiConcessiArresto", lCalUtil.toJsp(lBeneficiConcessiArrestoJSP));
			setRequestAttribute("BeneficiRevocatiArresto", lCalUtil.toJsp(lBeneficiRevocatiArrestoJSP));
			setRequestAttribute("BeneficiConcessiReclusione", lCalUtil.toJsp(lBeneficiConcessiReclusioneJSP));
			setRequestAttribute("BeneficiRevocatiReclusione", lCalUtil.toJsp(lBeneficiRevocatiReclusioneJSP));

			setRequestAttribute("AnnManConcessiReclusione", lCalUtil.toJsp(lAnnManConcessiReclusioneJSP));
			setRequestAttribute("AnnManRevocatiReclusione", lCalUtil.toJsp(lAnnManRevocatiReclusioneJSP));
			setRequestAttribute("AnnManConcessiArresto", lCalUtil.toJsp(lAnnManConcessiArrestoJSP));
			setRequestAttribute("AnnManRevocatiArresto", lCalUtil.toJsp(lAnnManRevocatiArrestoJSP));

			setRequestAttribute("MisCauComputabiliReclusione", lCalUtil.toJsp(lMisCauComputabiliReclusione));
			setRequestAttribute("MisCauComputabiliArresto", lCalUtil.toJsp(lMisCauComputabiliArresto));

			setRequestAttribute("PenaComplessivaSentenza", lPenMod);
			// Non è detto che sia la pena residua in sentenza, se il calcolo non è
			// ab inizio è pari all'ultima pena validata eventualmente normalizzata

			setRequestAttribute("PenaComplessiva", lPenaComplessiva); // la pena rideterminata
			setRequestAttribute("PenaGiaEspiata", lPenaGiaEspiata);
			setRequestAttribute("Fungibilita", lFunMod);

			setRequestAttribute("ForzaSysdateManuale", ForzaSysdateManuale);

			// ======================================================================
			//
			// ======================================================================
			setRequestAttribute("DataInizioPena", lDataInizioPena);
			setRequestAttribute("DataFineReclusione", lDataFineReclusione);
			setRequestAttribute("DataInizioArresto", lDataInizioArresto);
			setRequestAttribute("DataFinePena", lDataFinePena);

			setRequestAttribute("Segnalazione", lSegnalazione);
			setRequestAttribute("VedoJSP", lVedoJSP);
			setRequestAttribute("vedoDataIntermedia", vedoDataIntermedia);
			setRequestAttribute("CodPosizioneGiuridica", PGMod2.getCodPosizioneGiuridica());

			setRequestAttribute("FlagAltraCausa",
					((FascicoloSiepModel) getSessionAttribute("fascicolo")).getFlagAltraCausa());
			// setRequestAttribute("dataSistemaPerCalcoli", dataSistemaPerCalcoli);
			setRequestAttribute("dataScarcerazione", dataScarcerazione);

			// aggiunta da diego per poter visualizzare la pena di partenza nel caso di calcolo non abInizio
			setRequestAttribute("ultimaPenaValidata", lUltimaPenRes);

			// ******************************************************************************
			// ** PROBLEMA DELLA TRANSAZIONE relativo al calcolo pena precedente!!!!!!!!!!!**
			// ******************************************************************************

			// Update ID_EVENTO di PENA_RESIDUA NON IN TRANSAZIONE!!!!
			// lPenaComplessiva.setEveIdEvento(lAnnManIns.getEveIdEvento());
			// IPenRes.ExModificaPenaResidua(lPenaComplessiva);

			// setRequestAttribute("IdEvento", lAnnManIns.getEveIdEvento());

			// *******************************************************

			// FLAG PER DECIDERE LA PAGINA DA RICHIAMARE
			this.setRequestAttribute("lFlagPage", "GE");
			lPage = new String(
					f3b.web.IWebConstants.ROOT_DIR + "/files/siap/siep/calcolopena/VediNuovoCalcoloPena.jsp");
		} else // ERGASTOLO
		{
			lPenRes = new PenaResiduaModel();

			if (!lGiornoInizio.equals("") && !lGiornoInizio.equals("-"))
				lDataInizioPena = DateUtils.getDate(lAnnoInizio, lMeseInizio, lGiornoInizio);

			lDataFinePena = DateUtils.getDate(9999, 12, 31);

			if (lPenMod.getCodTipoPenaDetentiva().equals("03")) {
				lPenRes.setFlagErgastolo("S");
			} else if (lPenMod.getCodTipoPenaDetentiva().equals("04")) {
				lPenRes.setFlagErgastolo("D");
			}

			lPenRes.setDataFine(lDataFinePena);
			lPenRes.setDataInizio(lDataInizioPena);

			lPenRes.setDataInizioIsolamentoDiurno(lPenMod.getDataInizioIsolamentoDiurno());
			lPenRes.setDataFineIsolamentoDiurno(lPenMod.getDataFineIsolamentoDiurno());

			lPenRes.setNumGiorniIsolamentoDiurno(lPenMod.getNumGiorniIsolamentoDiurno());
			lPenRes.setNumMesiIsolamentoDiurno(lPenMod.getNumMesiIsolamentoDiurno());
			lPenRes.setNumAnniIsolamentoDiurno(lPenMod.getNumAnniIsolamentoDiurno());

			lPenRes.setFlagValidato("N");
			lPenRes.setDiesAQuo("S");
			lPenRes.setFasSieIdFascicoloSiep(lFascID);

			lPenRes.setCodOperatoreInserimento(getCodUtenteConnesso());
			lPenRes.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
			lPenRes.setDataInserimento(DateUtils.getSysDate());

			/*
			 * if (!op.equals("insert")) { lPenRes.setIdPenaResidua(new BigDecimal(op));
			 * IPenRes.ExModificaPenaResidua(lPenRes); } else IPenRes.ExInserisciPenaResidua(lPenRes);
			 */

			// !!!! PROBLEMA DELLA TRANSAZIONE !!!!
			IPenRes.ExInsertOrUpdatePenaResidua(lPenRes);

			setRequestAttribute("PenaResidua", lPenRes);
			setRequestAttribute("PenaComplessivaSentenza", lPenMod);

			lPage = new String(f3b.web.IWebConstants.ROOT_DIR
					+ "/files/siap/siep/calcolopena/VediCalcoloPenaValidataRichiestaGE.jsp");
		}

		setRequestAttribute("lFlagPage", this.getRequestStringParameter("lFlagPage"));

		// lPage = new String(f3b.web.IWebConstants.ROOT_DIR
		// +"/files/siap/siep/calcolopena/VediCalcoloPenaValidataRichiestaGE.jsp");

		// ==========================================================================
		// La pagina di ritorno cambia in funzione di ergastolo o meno.
		// Se ergastolo viene visualizzata la pagina VediCalcoloPenaValidataRichiestaGE.jsp
		// Altrimenti VediNuovoCalcoloPena.jsp
		// ==========================================================================
		return lPage;
	}

}