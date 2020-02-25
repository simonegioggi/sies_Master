package siap.siep.calcolopena.action;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.web.IWebConstants;
import siap.sico.libertaanticipata.action.ICostantiLibertaAnticipata;
import siap.sico.libertaanticipata.action.ICostantiLicenzaLibanticipata;
import siap.sico.libertaanticipata.controller.ILicenzaPeriodiLibAnticipata;
import siap.sico.libertaanticipata.model.LicenzaLibAnticipataModel;
import siap.sico.utente.model.DatiOperazioneModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siep.annotazionemanuale.action.ICostantiAnnotazioneManuale;
import siap.siep.calcolopena.controller.ICalcoloPena;
import siap.siep.calcolopena.model.CalcoloPenaModel;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.penaresidua.controller.IPenaResidua;
import siap.siep.penaresidua.model.PenaResiduaModel;
import siap.siep.util.SIEPLookupRemote;

/**
 * Action per il calcolo della pena a seguito del ridimensionamento delle LA o scomputo permesso - Decisioni
 * Sorveglianza - Ridimensionamento LA - Revoca LA - Scomputo Permesso ( da verificare: lo scomputo richiama
 * siap.siep.libertaanticipata.action.ActCalcoloPenaScompPermRidimLA)
 * 
 * 
 */
public class ActCalcoloPenaScompRidimLA extends ActionSiap implements ICostantiLibertaAnticipata {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	/**
	 * 
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public String processRequest() throws Exception {

		FascicoloSiepModel lFascMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");
		BigDecimal lIdFascicolo = lFascMod.getIdFascicoloSiep();

		// Id dell'evento di computo a cui sono collegati i giorni da rideterminare
		BigDecimal lIdEvento = getRequestBigDecimalParameter(ICostantiAnnotazioneManuale.CAMPO_EVE_ID_EVENTO);
		setRequestAttribute("IdEventoStr", "" + lIdEvento);

		// ==========================================================================
		// Effettuo il calcolo della pena che consiste nel recuperare la situazione
		// della pena corrente e:
		// Se pena non in decorrenza: allora non faccio nulla di particolare, devo
		// solo riportare sulla maschera di dettaglio i
		// giorni di LA concessi e quelli scomputati
		// Se pena in decorrenza: oltre a riportare i dati devo correggere il fine
		// pena che per effetto dello scomputo permesso o
		// ridimensionamento LA deve essere portato avanti.
		//
		// n.b. come nel caso della concessione di LA si possono verificare 2 casi:
		// 1) prendo la data fine pena dell'ultima pena residua e avanzo il
		// fine pena aggiungendo i giorni revocati (LA o permesso) (abFine)
		// 2) rieffettuo tutti i calcoli da capo (abInizio)
		// Nei casi 1) e 2) il risultato finale potrebbe non coincidere in quanto il
		// fine pena attuale (ultima pena validata) potrebbe essere stato forzato
		// manualmente o calcolato con un algoritmo differente (dies a quo, RES)
		//
		// Per ora avanzo semplicemente il fine pena.
		// ==========================================================================
		// Calcolo abFine
		// Recupero l'ultima pena validata, mi serve la data inizio se presente
		// ==========================================================================
		IPenaResidua lPenResCtrl = SIEPLookupRemote.getPenaResiduaRemote();
		// PenaResiduaModel lPenaResMod =
		// lPenResCtrl.ExRicercaPenaResiduaCorrenteByFascicoloSiep(lFascMod.getIdFascicoloSiep());
		PenaResiduaModel lUltimaPenResVal = lPenResCtrl
				.ExRicercaPenaResiduaUltimaByDate(lFascMod.getIdFascicoloSiep());
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("Ultima Pena Validata = " + lUltimaPenResVal);

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("Recupero la situazione attuale della Pena (dati Validati)");
		ActCalcoloPenaMain lCalcoloPenaMain = new ActCalcoloPenaMain();
		CalcoloPenaModel lCalcoloPenaModel = lCalcoloPenaMain.calcoloPena(lIdFascicolo, null);

		// Prima di aggiungere i gg estraggo i dati della situazione attuale da passare
		// alla form
		// int lTotLAAttuale = lCalcoloPenaModel.getLiberazioneAnticipata();
		// int lTotScomputiAttuali = lCalcoloPenaModel.getTotaleScomputi();

		int lTotLAAttuale = lCalcoloPenaModel.getLiberazioneAnticipataGiaConcesse();
		int lTotScomputiAttuali = lCalcoloPenaModel.getScomputiGiaConcessi();

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("Rscomputati   = " + lTotScomputiAttuali);
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("ggLA concessi = " + lTotLAAttuale);

		setRequestAttribute("lTotLAAttuale", "" + new BigDecimal(lTotLAAttuale));
		setRequestAttribute("lTotScomputiAttuali", "" + new BigDecimal(lTotScomputiAttuali));

		// ==========================================================================
		// Recupero i giorni di Ridimensionamento LA o Scomputo permesso/licenza
		// Attenzione: le due quantità sono iscritte entrambe su LICENZA_LIBANTICIPATA
		// ma vanno trattate diversamente
		// ==========================================================================
		ILicenzaPeriodiLibAnticipata lLicCtrl = SICOLookupRemote.getLicenzaPeriodiLibAntRemote();
		Vector<LicenzaLibAnticipataModel> lVectLic = lLicCtrl.ExRicercaLicenzeByEve(lIdEvento);

		// ==========================================================================
		// Calcola il totale di GG di LA ridimensionati con provvedimento (idEvento)
		// sia in valore assoluto che per tipologia.
		// Dati da passare alla form per la visualizzazione
		// ==========================================================================
		Vector<LicenzaLibAnticipataModel> lVectLicLA = lLicCtrl.ExRicercaLicenzeByEve(lIdEvento, "LA");
		Vector<LicenzaLibAnticipataModel> lVectLicLS = lLicCtrl.ExRicercaLicenzeByEve(lIdEvento, "LS");
		Vector<LicenzaLibAnticipataModel> lVectLicLI = lLicCtrl.ExRicercaLicenzeByEve(lIdEvento, "LI");

		LicenzaLibAnticipataModel lLicModel = null;
		int lTotGiorni = 0;
		String ConcedeScomputa = "";
		for (int i = 0; i < lVectLic.size(); i++) {
			lLicModel = (LicenzaLibAnticipataModel) lVectLic.elementAt(i);
			lTotGiorni = lTotGiorni + lLicModel.getNumeroGiorni().intValue();
			ConcedeScomputa = lLicModel.getFlagConcesso();
		}
		setRequestAttribute("lGiorniScomputati", "" + new BigDecimal(lTotGiorni));

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("lTotGiorni  = " + lTotGiorni);

		// Nuova L.A. DL 146/2013
		int lTotLAAttualeLA = lCalcoloPenaModel.getLiberazioneAnticipataGiaConcesseLA();
		int lTotLAAttualeSPE = lCalcoloPenaModel.getLiberazioneAnticipataGiaConcesseLS();
		int lTotLAAttualeINT = lCalcoloPenaModel.getLiberazioneAnticipataGiaConcesseLI();
		//
		setRequestAttribute("lTotAttualeLA", "" + new BigDecimal(lTotLAAttualeLA));
		setRequestAttribute("lTotAttualeLS", "" + new BigDecimal(lTotLAAttualeSPE));
		setRequestAttribute("lTotAttualeLI", "" + new BigDecimal(lTotLAAttualeINT));
		//
		int lTotScomputiAttualiLA = lCalcoloPenaModel.getScomputiGiaConcessiLA();
		int lTotScomputiAttualiSPE = lCalcoloPenaModel.getScomputiGiaConcessiLS();
		int lTotScomputiAttualiINT = lCalcoloPenaModel.getScomputiGiaConcessiLI();
		//
		setRequestAttribute("lTotScomputiAttualiLA", "" + new BigDecimal(lTotScomputiAttualiLA));
		setRequestAttribute("lTotScomputiAttualiLS", "" + new BigDecimal(lTotScomputiAttualiSPE));
		setRequestAttribute("lTotScomputiAttualiLI", "" + new BigDecimal(lTotScomputiAttualiINT));

		LicenzaLibAnticipataModel lLicModelLA = null;
		int lTotGiorniLA = 0;
		for (int i = 0; i < lVectLicLA.size(); i++) {
			lLicModelLA = (LicenzaLibAnticipataModel) lVectLicLA.elementAt(i);
			lTotGiorniLA = lTotGiorniLA + lLicModelLA.getNumeroGiorni().intValue();
		}
		setRequestAttribute("lGiorniScomputatiLA", "" + new BigDecimal(lTotGiorniLA));

		LicenzaLibAnticipataModel lLicModelLS = null;
		int lTotGiorniLS = 0;
		for (int i = 0; i < lVectLicLS.size(); i++) {
			lLicModelLS = (LicenzaLibAnticipataModel) lVectLicLS.elementAt(i);
			lTotGiorniLS = lTotGiorniLS + lLicModelLS.getNumeroGiorni().intValue();
		}
		setRequestAttribute("lGiorniScomputatiLS", "" + new BigDecimal(lTotGiorniLS));

		LicenzaLibAnticipataModel lLicModelLI = null;
		int lTotGiorniLI = 0;
		for (int i = 0; i < lVectLicLI.size(); i++) {
			lLicModelLI = (LicenzaLibAnticipataModel) lVectLicLI.elementAt(i);
			lTotGiorniLI = lTotGiorniLI + lLicModelLI.getNumeroGiorni().intValue();
		}
		setRequestAttribute("lGiorniScomputatiLI", "" + new BigDecimal(lTotGiorniLI));

		//
		// ==========================================================================

		// Aggiungo i giorni revocati LA o scomputo permesso/licenza
		// IEvento lEventoCtrl = SICOLookupRemote.getEventoRemote();
		// EventoModel lEveModel = lEventoCtrl.ExRicercaEventoByKey(lIdEvento);

		// ==========================================================================
		// Ricalcolo le date di decorrenza e scadenza se presente la data inizio
		// ==========================================================================
		Date lDataFine = lUltimaPenResVal.getDataFine();
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("Vecchia data fine = " + DateUtils.getDateToString(lDataFine, "dd-MM-yyyy"));
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("lTotGiorni = " + lTotGiorni);

		// n.b. ID_LASORV inserito solo nel caso di Ridimensionamento dalla ActInserisciRidimensionamentoLA
		// e solo se LA selezionata dalla lista. Serve per recuperare le LA concesse
		// e verificare se sono state elaborate o meno.
		String Id_LaSorv = "";
		if (!this.isSessionAttributeNullObj("ID_LASORV"))
			Id_LaSorv = (String) this.getSessionAttribute("ID_LASORV");

		Date lOggi = DateUtils.getSysDateAsDate("dd/MM/yyyy");
		if (lDataFine != null && DateUtils.isGreater(lDataFine, lOggi)) // 31/05/2016 avanzo il fine pena SOLO
																		// se la DATA FINE PENA è FUTURA
		// if (lDataFine!=null)
		{ // Avanzo il fine pena dei giorni revocati
			// ANNA per modifica conteggio ridimensionamento se evento della sorv. è gia elaborato

			// d.f. 31/05/2016 Attenzione nel caso di Pena Già Espiata (data fine < data sistema)
			// il calcolo non deve toccare il fine pena e quindi non
			// va fatto il calcolo abFine ma solo quello abInizio
			// impostando la data scarcerazione
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("ID della LA di SORVEGLIANZA = " + Id_LaSorv);

			if (!Id_LaSorv.equals("")) { // RIDIMENSIONAMENTO LA di concessione selezionate dalla lista
				ILicenzaPeriodiLibAnticipata lCtrlLib = SICOLookupRemote.getLicenzaPeriodiLibAntRemote();
				LicenzaLibAnticipataModel lSorvModel = lCtrlLib
						.ExRicercaLicenzaLibanticipataByKey(new BigDecimal(Id_LaSorv));
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("FLAG della LA di SORVEGLIANZA = " + lSorvModel.getFlagElaborato());

				if (lSorvModel.getFlagElaborato() == null) {
					// LA da ridimensionare non ancora elaborate. I gg inseriti in form
					// allora sono da concedere
					lDataFine = DateUtils.moveDateTo(lDataFine, Calendar.DAY_OF_MONTH, -(lTotGiorni));
				} else {
					if (!lSorvModel.getFlagElaborato().equals("E")
							&& !lSorvModel.getFlagElaborato().equals("S"))
						lDataFine = DateUtils.moveDateTo(lDataFine, Calendar.DAY_OF_MONTH, -(lTotGiorni));
					else // LA da ridimensionare già elaborate e computate sul fine pena. I gg inseriti in
							// form vanno intesi come correzione (con segno)
						lDataFine = DateUtils.moveDateTo(lDataFine, Calendar.DAY_OF_MONTH, +(lTotGiorni));
				}
			} else {
				// in questo caso lTotGiorni veniva sempre sottratto alla data FINE PENA;
				// 20/05/2014 ------->>>>>>>>> Se esistono giorni già concessi di L.A.,
				// aggiungo alla Data Fine Pena i gg Scomputati
				if (ConcedeScomputa.equals("C")) {
					lDataFine = DateUtils.moveDateTo(lDataFine, Calendar.DAY_OF_MONTH, -(lTotGiorni));
					//// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					//// LogF3B.getLogger()
					// siesLogger.debug("KKKKK caso 1 = "+DateUtils.getDateToString(lDataFine, "dd-MM-yyyy")
					//// );
				} else if (ConcedeScomputa.equals("S")) {
					lDataFine = DateUtils.moveDateTo(lDataFine, Calendar.DAY_OF_MONTH, +(lTotGiorni));
					//// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					//// LogF3B.getLogger()
					// siesLogger.debug("KKKKK caso 2 = "+DateUtils.getDateToString(lDataFine, "dd-MM-yyyy")
					//// );
				} else {
					if (lTotLAAttuale > 0) {
						lDataFine = DateUtils.moveDateTo(lDataFine, Calendar.DAY_OF_MONTH, +(lTotGiorni));
						//// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al
						//// posto di LogF3B.getLogger()
						// siesLogger.debug("KKKKK caso 3 = "+DateUtils.getDateToString(lDataFine,
						//// "dd-MM-yyyy") );
					} else {
						lDataFine = DateUtils.moveDateTo(lDataFine, Calendar.DAY_OF_MONTH, -(lTotGiorni));
						//// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al
						//// posto di LogF3B.getLogger()
						// siesLogger.debug("KKKKK caso 4 = "+DateUtils.getDateToString(lDataFine,
						//// "dd-MM-yyyy") );
					}
				}
			}

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("KKKKK Nuova data fine = " + DateUtils.getDateToString(lDataFine, "dd-MM-yyyy"));
			this.setSessionAttribute("ID_LASORV", "");

			// lDataFine = DateUtils.moveDateTo( lDataFine, Calendar.DAY_OF_MONTH, +(lTotGiorni) );
			//// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			// siesLogger.debug("Nuova data fine = "+DateUtils.getDateToString(lDataFine, "dd-MM-yyyy") );
		} else if (lDataFine != null) {

		}

		this.setSessionAttribute("ID_LASORV", ""); // FIXME PORTATO FUORI resetto sempre: da VERIFICARE

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("lUltimaPenResVal abFine = " + lUltimaPenResVal);
		PenaResiduaModel lPenaRideterminata = new PenaResiduaModel(lUltimaPenResVal);

		lPenaRideterminata.setDataFine(lDataFine);

		// Inserisco la pena rideterminata.
		lPenaRideterminata.setEveIdEvento(lIdEvento);
		lPenaRideterminata.setFasSieIdFascicoloSiep(lIdFascicolo);
		lPenaRideterminata.setDiesAQuo("S");
		lPenaRideterminata.setFlagValidato("N");

		// Dati dell'ergastolo
		lPenaRideterminata.setFlagErgastolo(lUltimaPenResVal.getFlagErgastolo());
		lPenaRideterminata.setDataInizioIsolamentoDiurno(lUltimaPenResVal.getDataInizioIsolamentoDiurno());
		lPenaRideterminata.setDataFineIsolamentoDiurno(lUltimaPenResVal.getDataFineIsolamentoDiurno());
		lPenaRideterminata.setNumAnniIsolamentoDiurno(lUltimaPenResVal.getNumAnniIsolamentoDiurno());
		lPenaRideterminata.setNumGiorniIsolamentoDiurno(lUltimaPenResVal.getNumMesiIsolamentoDiurno());
		lPenaRideterminata.setNumMesiIsolamentoDiurno(lUltimaPenResVal.getNumGiorniIsolamentoDiurno());

		lPenaRideterminata.setCodOperatoreInserimento(getCodUtenteConnesso());
		lPenaRideterminata.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
		lPenaRideterminata.setDataInserimento(DateUtils.getSysDate());

		// ===========================================================
		//
		// ===========================================================
		// lPenResCtrl.ExInsertOrUpdatePenaResidua(lPenaRideterminata);

		// DA VERIFICARE!!!!!!!!!!!!!!!!!!!!!!!!!!!!

		// ==========================================================================
		// L'id dell'evento di Ordinanza selezionato da lista o inserito
		// ==========================================================================
		BigDecimal lEveIdEventoOrdinanza = getRequestBigDecimalParameter(
				ICostantiLicenzaLibanticipata.CAMPO_EVE_ID_EVENTO);
		setRequestAttribute("lEveIdEventoOrdinanza", "" + lEveIdEventoOrdinanza);

		// ..............................................................................
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("-PARTE DA VERIFICARE --------------------");
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
			// Date lOggi = DateUtils.getSysDate();
			// Devo eliminare minuti e secondi dalla sysdate altrimenti le funzioni
			// di compare tra date falliscono
			// String lGiorno = DateUtils.getDateToString(lOggi,"dd");
			// String lMese = DateUtils.getDateToString(lOggi,"MM");
			// String lAnno = DateUtils.getDateToString(lOggi,"yyyy");
			// dataSistemaPerCalcoli = DateUtils.getDate( lAnno, lMese, lGiorno );
			dataSistemaPerCalcoli = lOggi;
		}
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("dataSistemaPerCalcoli = " + dataSistemaPerCalcoli);

		// ..............................................................................

		// ==========================================================================
		// Costruisce un CalcoloPenaModel che contiene in modo strutturato
		// tutte le informazioni necessarie a determinare la Pena Residua
		// ad un dato momento
		// ==========================================================================
		// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		// siesLogger.debug("Recupero la situazione attuale della Pena (dati Validati)");
		// ActCalcoloPenaMain lCalcoloPenaMain = new ActCalcoloPenaMain();
		// CalcoloPenaModel lCalcoloPenaModel = lCalcoloPenaMain.calcoloPena(lIdFascicolo, null);

		// ==========================================================================
		// Effettuo il calcolo e l'inserimento/aggiornamento della data fine pena
		// della pena residua e inserisco l'eventuale fungibilità.
		// ==========================================================================
		DatiOperazioneModel lOperMod = new DatiOperazioneModel(getCodUtenteConnesso(), DateUtils.getSysDate(),
				getCodUfficioUtenteConnesso(), "");
		ICalcoloPena lCtrlCal = SIEPLookupRemote.getCalcoloPenaRemote();
		CalcoloPenaModel lCalcoloPena = lCtrlCal.exCalcoloLiberazioneAnticipata(lCalcoloPenaModel,
				lIdFascicolo, lEveIdEventoOrdinanza, dataSistemaPerCalcoli, null, null, lOperMod);
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("--FINE DELLA exCalcoloLiberazioneAnticipata !!!!-->");

		// ==========================================================================
		// Calcolo della data fine pena considerando non il nuovo metodo di calcolo,
		// ma DATA_FINE_CALCOLATO = DATA_FINE_PENA_RESIDUA_CORRENTE - LA_CONCESSE
		// ==========================================================================
		// FIXME 31/05/2016 verificare se questa parte di codice serve a qualche cosa
		// ricerca DEPOSITO_ORDINANZA_PC che punta l'evento di annotazione. Quindi cerca le
		// LA legate al deposito che nel caso di Ridimensionamento non esitono. Sono legate
		// al provvedimento
		// Verificare il caso della REVOCA che invece seleziona un provvedimento della SORV
		// L'unico campo gestito dalla form è la dataScarcerazione che NON viene
		// comunque calcolata nella IF me è eventualmente un dato in input

		/**
		 * 31/05/2016 momentaneamente commentato
		 * 
		 * // RICERCA DEPOSITO_ORDINANZA_PC per ottenere i giorni di LA concessi IDepositoOrdinanzaPc lCtrlDep
		 * = SIUSLookupRemote.getDepositoOrdinanzaPcRemote(); DepositoOrdinanzaPcModel lDepOrdMod =
		 * lCtrlDep.ExRicercaDepositoOrdinanzaPcByEvento(lEveIdEventoOrdinanza);
		 * 
		 * int lTotLA = 0; if(lDepOrdMod != null && lDepOrdMod.getNumGiorniLibanticipata() != null) { lTotLA =
		 * lDepOrdMod.getNumGiorniLibanticipata().intValue(); }
		 * 
		 * // Anticipa il fine pena (se presente) dell'ultima pena validata Date lDataFineAbFine =
		 * lCtrlCal.exCalcoloLiberazioneAnticipataSuDataFineUltimaPena (lTotLA, lIdFascicolo);
		 * 
		 * //========================================================================== // Confronto tra i due
		 * calcoli per presentare, in caso di discrepanza, // un pagina che consente di selezionare il fine
		 * pena voluto // (data fine nuovo calcolo/fine pena su DB meno la LA).
		 * //========================================================================== if( lDataFineAbFine !=
		 * null && lCalcoloPena.getPenaResiduaRicalcolata() != null) { PenaResiduaModel lPenaResNuovoCalcolo =
		 * lCalcoloPena.getPenaResiduaRicalcolata();
		 * 
		 * if( lPenaResNuovoCalcolo != null && lPenaResNuovoCalcolo.getDataFine() != null ) { if(
		 * !lDataFineAbFine.equals(lPenaResNuovoCalcolo.getDataFine()) ) { // [FT] - 03/08/2016 - MAC_LOG -
		 * Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
		 * siesLogger.debug("lDataFineAbFine ["+DateUtils.getDateToString(lDataFineAbFine, "dd/MM/yyyy")+"] !=
		 * da fine pena ricalcolato ["+DateUtils.getDateToString(lPenaResNuovoCalcolo.getDataFine(),
		 * "dd/MM/yyyy")+"]"); //==========================================================================
		 * //Cerca l'ultima Pena Residua Validata
		 * //========================================================================== IPenaResidua
		 * lCtrlPenRes = SIEPLookupRemote.getPenaResiduaRemote(); PenaResiduaModel lPenaResidua =
		 * lCtrlPenRes.ExRicercaPenaResiduaUltimaValidata(lIdFascicolo);
		 * 
		 * //========================================================================== // Per rieffettuare i
		 * calcoli "sul pulito" riassegno i riferimenti // del lCalcoloPenaMain e del lCalcoloPenaModel (forse
		 * non necessario?) //==========================================================================
		 * ActCalcoloPenaMain lCalcoloPenaMainSenzaLA = new ActCalcoloPenaMain(); CalcoloPenaModel
		 * lCalcoloPenaModelSenzaLA = lCalcoloPenaMainSenzaLA.calcoloPena(lIdFascicolo, null);
		 * 
		 * //========================================================================== // Per visualizzare la
		 * nuova data fine ab inizio effettuo il calcolo // senza le LA concesse
		 * //========================================================================== PenaResiduaModel
		 * lPenaNuovoCalcoloSenzaLA = lCalcoloPenaModelSenzaLA.getPenaDaEspiare(lPenaResidua.getDataInizio(),
		 * null, "all");
		 * 
		 * setRequestAttribute("DataFineAbInitioSenzaLA", lPenaNuovoCalcoloSenzaLA.getDataFine());
		 * setRequestAttribute("DataFineAbFineSenzaLA", lPenaResidua.getDataFine());
		 * setRequestAttribute("DataFineAbInitio", lPenaResNuovoCalcolo.getDataFine());
		 * setRequestAttribute("DataFineAbFine", lDataFineAbFine);
		 * 
		 * setRequestAttribute("dataScarcerazione",dataScarcerazione); } } }
		 * 
		 * 
		 * 
		 * // n.b. d.f. 31/05/2016 la form non gestisce nessuno dei dati passati in questo // codice fatta
		 * eccezione per la data dataScarcerazione
		 * 
		 * FungibilitaModel lFunMod = lCalcoloPena.getFungibilitaCalcolata(); if( lFunMod != null && (
		 * (lFunMod.getNumAnni() != null && lFunMod.getNumAnni().intValue() > 0) || (lFunMod.getNumMesi() !=
		 * null && lFunMod.getNumMesi().intValue() > 0) || (lFunMod.getNumGiorni() != null &&
		 * lFunMod.getNumGiorni().intValue() > 0) ) ) { // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile
		 * di istanza siesLogger al posto di LogF3B.getLogger() siesLogger.debug("Ho Fungibilità = "+lFunMod);
		 * setRequestAttribute("TotaleGiorniLibAnt", "" + lTotLA);
		 * 
		 * //========================================================================== // Restituisco i dati
		 * alla finestra di visualizzazione
		 * //==========================================================================
		 * setRequestAttribute("PenaResidua" , lCalcoloPena.getPenaResiduaRicalcolata()); // la pena
		 * rideterminata setRequestAttribute("PenaGiaEspiata" , lCalcoloPena.getPenaEspiata()); // pena già
		 * espiata setRequestAttribute("Fungibilita" , lCalcoloPena.getFungibilitaCalcolata()); // fungibilità
		 * 
		 * setRequestAttribute("dataScarcerazione",dataScarcerazione);
		 * 
		 * }
		 **/
		setRequestAttribute("dataScarcerazione", dataScarcerazione);

		// fine DA VERIFICARE !!!!!!!!!!!!!!!!!!!!!!!!!

		// ============================================================
		// Passo i dati alla form per la visualizzazione
		// ============================================================
		//
		lPenaRideterminata = lCalcoloPenaModel.getPenaResiduaRicalcolata();

		setRequestAttribute("lCalcoloPenaModel", lCalcoloPenaModel); // situazione attuale + LA revocate
		setRequestAttribute("lPenaRideterminata", lPenaRideterminata); // abFine
		setRequestAttribute("lPenaDiPartenza", lUltimaPenResVal);

		setRequestAttribute("lTotGiorniRDConcessi", "" + lCalcoloPena.getRimediRisarcitoriGiaConcessi());

		// lUltimaPenResVal
		String lPage = "";
		lPage = IWebConstants.ROOT_DIR + "/files/siap/siep/calcolopena/VediCalcoloPenaScomputiRidimLA.jsp";
		return lPage;
	}

}