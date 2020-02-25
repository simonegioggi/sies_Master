package siap.siep.calcolopena.action;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Vector;

import org.apache.log4j.Logger;

import siap.sico.calendar.model.CalendarModel;
import siap.sico.libertaanticipata.model.LicenzaLibAnticipataModel;
import siap.sico.util.CalendarUtil;
import siap.sico.web.ActionSiap;
import siap.siep.annotazionemanuale.model.AnnotazioneManualeModel;
import siap.siep.calcolopena.controller.ICalcoloPena;
import siap.siep.calcolopena.model.CalcoloPenaModel;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.penaresidua.controller.IPenaResidua;
import siap.siep.penaresidua.model.PenaResiduaModel;
import siap.siep.penaresidua.util.PenaResiduaUtil;
import siap.siep.util.SIEPLookupRemote;
import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.web.IWebConstants;

/**
 * Classe Action per effettuare il calcolo della pena residua de espiare a una certa data. Utlizzata dalla
 * 'Calcolatrice' della form di richiesta al GE.
 * 
 * @author
 *
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class ActCalcolaPenaResiduaAl extends ActionSiap {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	/**
	 * Questo metodo effettua il calcolo del quantum da sottrarre alla pena in espiazione per ottenere una
	 * data fine pena pari alla data di scarcerazione passata sulla request.
	 * 
	 * @return popup di visualizzazione del risultato
	 */
	public String processRequest() throws Exception {

		// ==========================================================================
		// In condizioni normali sarebbe sufficiente simulare una interruzione alla
		// data di scarcerazione per determinare il residuo da espiare. L'espiato,
		// ottenuto per differenza dovrebbe essere tale da determinare un fine pena
		// pari alla data di scarcerazione voluta.
		// es: pena in espiazione 1 anno data inizione 19/03/2008 fine pena 18/03/2009
		// Data interruzione = 15/08/2008, pena espiata 4 mesi e 27 giorni
		// se ricalcolo la data fine pena applicando 4 mesi e 27 gg ottengo il 14/08/2008
		// e non il 15/08/2008
		// Questo a causa degli errori di approssimazione insiti nei calcoli.
		// Per questo motivo dopo aver determinato l'espiato si deve controllare che
		// il fine pena ottenuto corrisponda a quanto cercato e in caso negativo si
		// procede per tentativi aggiungendo o sottraendo 1g finchè non si ottiene
		// il risultato voluto o non si esce per impossibilità a convergere
		// ==========================================================================

		BigDecimal lIdFascicoloSiep = ((FascicoloSiepModel) getSessionAttribute("fascicolo"))
				.getIdFascicoloSiep();

		// Recupero la data di scarcerazione. Se assente utilizzo la data di sistema
		Date lDataScarcerazione = null;
		if (this.isRequestParameterNullObj("annoScarcerazione")) {
			Date lOggi = DateUtils.getSysDate();
			// Devo eliminare minuti e secondi dalla sysdate altrimenti le funzioni
			// di compare tra date falliscono
			String lGiorno = DateUtils.getDateToString(lOggi, "dd");
			String lMese = DateUtils.getDateToString(lOggi, "MM");
			String lAnno = DateUtils.getDateToString(lOggi, "yyyy");

			lDataScarcerazione = DateUtils.getDate(lAnno, lMese, lGiorno);
		} else {
			lDataScarcerazione = getRequestDateParameter("annoScarcerazione", "meseScarcerazione",
					"giornoScarcerazione");
		}

		// ========================================================================
		// Recupero il nome della form chiamante nella quale verranno caricati i
		// dati dalla jsp che visualizza il risultato di questa funzione
		// ========================================================================
		String lFormName = "";
		if (!isRequestParameterNullObj("formname")) {
			lFormName = getRequestStringParameter("formname");
		}

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("lDataScarcerazione = " + lDataScarcerazione);
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("lFormName = " + lFormName);

		// ==========================================================================
		// Recupero la pena a partire dalla quale verranno effettuati i calcoli
		// ==========================================================================
		ActCalcoloPenaMain lActCalcoloPenaMain = new ActCalcoloPenaMain();
		CalcoloPenaModel lCalcPenaModel = lActCalcoloPenaMain.calcoloPena(lIdFascicoloSiep, null);

		// ==========================================================================
		// Recupero l'ultima pena residua per la data inizio pena
		// ==========================================================================
		IPenaResidua lPenResCtrl = SIEPLookupRemote.getPenaResiduaRemote();
		PenaResiduaModel lUltimaPenaResiduaValidata = lPenResCtrl
				.ExRicercaPenaResiduaUltimaByDate(lIdFascicoloSiep);

		if (lUltimaPenaResiduaValidata == null) { // se non presente una pena validata utilizzo l'ultima in
													// assoluto
													// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di
													// istanza siesLogger al posto di LogF3B.getLogger()
			siesLogger.debug("Pena residua validata assente provo ultima non validata");
			lUltimaPenaResiduaValidata = lPenResCtrl
					.ExRicercaPenaResiduaCorrenteByFascicoloSiep(lIdFascicoloSiep);
		}

		if (lUltimaPenaResiduaValidata == null) { // Non esiste proprio un pena validata non è mai stato fatto
													// il primo calcolo
													// della pena lo effettuo ora per avere dati da
													// visualizzare sulla form
													// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di
													// istanza siesLogger al posto di LogF3B.getLogger()
			siesLogger.debug("Pena residua assente effettuo il primo calcolo");
			lUltimaPenaResiduaValidata = new PenaResiduaModel();
			Date lDataInizioPena = lActCalcoloPenaMain.getDataPrimoCalcolo(lIdFascicoloSiep);
			lUltimaPenaResiduaValidata.setDataInizio(lDataInizioPena);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Data inizio pena: " + lDataInizioPena);
		}

		// Calcolo la pena attualmente in espiazione
		PenaResiduaModel lUltimaPenaCalcolata = lCalcPenaModel
				.getPenaDaEspiare(lUltimaPenaResiduaValidata.getDataInizio(), null, "all");

		// ==========================================================================
		// n.b. devo determinare un quantum che sottratto a quello in espiazione
		// determina una data fine pena tale che arretrata dei giorni di LA
		// mi consente di ottenere proprio la data di scarcerazione
		// Attenzione se ho fungibilità con i dati a sistema è inutile proseguire
		// Ipotesi:
		// ==========================================================================

		// ==========================================================================
		// Calcolo il residuo da espiare alla data di scarcerazione come se fosse
		// una interruzione della pena.
		// Tale quantum porta dentro i giorni di LA, ma sottratto ai quantum a
		// sistema potrebbe non consentirmi di ottenere il risultato voluto a causa
		// degli errori di approssimazione.
		// - effettuo una simulazione di calcolo: e controllo le date
		// ==========================================================================
		lCalcPenaModel.calcolaPenaDaSospensione(null, lDataScarcerazione);
		PenaResiduaModel lPenaAncoraDaEspiareAl = lCalcPenaModel.getPenaResiduaRicalcolata();
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("lPenaAncoraDaEspiareAl = " + lPenaAncoraDaEspiareAl);

		// ==========================================================================
		// Verifico se il quantum ricalcolato è tale da determinare una data fine
		// pena pari al giorno di calcolo. Per effetto degli errori di calcolo
		// i conti potrebbero non tornare per un giorno
		// ==========================================================================
		// Inizializzo un CalcoloPenaModel per i successivi calcoli per tentativi:
		// Il model simula una pena iniziale pari alla Pena Attuale (per comodità
		// caricata come pena manuale), con LA pari a quelle attualmente presenti.
		// A partire da questa situazione di comodo simulo di applicare una richiesta
		// al GE pari alla pena residua calcolata tra la data di scarcerazione e la
		// data fine pena prevista.
		// Quindi verifico se la data fine pena così calcolata coincide effettivamente
		// con la data di scarcerazione. In caso negativo provo ad aggiungere/
		// sottrarre giorni alla richiesta fino a che non ottengo il risultato voluto
		// o esco per impossibilità di convergenza a risultato
		// ==========================================================================
		CalcoloPenaModel lCalcoloModelApp = new CalcoloPenaModel();
		lCalcoloModelApp.setTipoPenaIniziale(ICostantiCalcoloPena.PENA_MANUALE);
		lCalcoloModelApp.setDataDal(lUltimaPenaResiduaValidata.getDataInizio());

		// Inizializzo la Pena Manuale con i valori dell'ultima pena calcolata
		PenaResiduaModel lPenaAttuale = new PenaResiduaModel();
		lPenaAttuale.setQuantumReclusione(lUltimaPenaCalcolata.getQuantumReclusione());
		lPenaAttuale.setQuantumArresto(lUltimaPenaCalcolata.getQuantumArresto());
		lCalcoloModelApp.setPenaResiduaManuale(lPenaAttuale);

		// Aggiungo le LA
		LicenzaLibAnticipataModel lLibAnt = new LicenzaLibAnticipataModel();
		int lGiorniLA = lCalcPenaModel.getLiberazioneAnticipata();
		lLibAnt.setNumeroGiorni(new BigDecimal(lGiorniLA));
		Vector lListaLA = lCalcoloModelApp.getLibAnticipate();
		lListaLA.add(lLibAnt);

		// Aggiungo DL92 n.b. d.f.08/09/2015 modifica non realizzata e non conteggiata
		// con intervento con DL92
		LicenzaLibAnticipataModel lLibAntDL92 = new LicenzaLibAnticipataModel();
		int lGiorniRD = lCalcPenaModel.getRimediRisarcitori();
		lLibAntDL92.setNumeroGiorni(new BigDecimal(lGiorniRD));
		lListaLA.add(lLibAntDL92);

		// Inizializzo il quantum della richiesta con la pena residua calcolata
		AnnotazioneManualeModel lAnnRichiesta = new AnnotazioneManualeModel();
		lAnnRichiesta.setQuantumReclusione(lPenaAncoraDaEspiareAl.getQuantumReclusione());
		lAnnRichiesta.setQuantumArresto(lPenaAncoraDaEspiareAl.getQuantumArresto());
		lAnnRichiesta.setFlagPiuMeno("-");
		Vector lListaRichiesteGE = lCalcoloModelApp.getIndultoR();
		lListaRichiesteGE.add(lAnnRichiesta);

		// ==========================================================================
		// Entro nel ciclo di verifica:
		// aggiungo una richiesta al GE pari al quantum residuo calcolato ed
		// effettuo i calcoli del fine pena
		// ==========================================================================
		CalendarUtil lCalUtil = new CalendarUtil();
		CalendarModel lCalRecAncoraDaEspiareAl = new CalendarModel();
		CalendarModel lCalArrAncoraDaEspiareAl = new CalendarModel();

		// boolean uguale = false;
		boolean maggiore = false;
		boolean minore = false;

		String lMessage = "";

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("Entro nel ciclo di verifica con:");
		Date lDataInizio = lUltimaPenaResiduaValidata.getDataInizio();
		int lMaxLoopCount = 5;
		int lLoopCount = 0;

		// ==========================================================================
		//
		// ==========================================================================
		while (true) {
			lLoopCount++;

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("lLoopCount = " + lLoopCount);

			if (lLoopCount > lMaxLoopCount) {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("Esco per eccessive iterate ");
				lMessage = "Attenzione! Impossibile determinare un Quantum tale da ottenere la Data Scarcerazione richiesta.";
				break;
			}

			Date lDataFinePena = null;
			PenaResiduaModel lPenaResidua = lCalcoloModelApp.getPenaDaEspiare(lDataInizio, null, "all");
			lDataFinePena = lPenaResidua.getDataFine();

			if (lDataFinePena == null) {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("lDataFinePena null");
				// lDataFinePena = null se la pena è a 0 o in negativo
				if (CalendarUtil.getTotGiorni(lPenaResidua.getQuantumReclusione()) <= 0
						|| CalendarUtil.getTotGiorni(lPenaResidua.getQuantumArresto()) <= 0) {
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger.debug("Pena residua nulla o negativa esco");
					break;
				}
			} else if (lDataFinePena.compareTo(lDataScarcerazione) == 0) {
				// uguale = true;
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug(
						"Data fine rideterminata coincidente con la data di scarcerazione. Quantum trovato!");
				break;
			} else if (lDataFinePena.before(lDataScarcerazione)) {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug(
						"Data fine rideterminata (" + DateUtils.getDateToString(lDataFinePena, "dd-MM-yyyy")
								+ ") minore della data di scarcerazione ("
								+ DateUtils.getDateToString(lDataScarcerazione, "dd-MM-yyyy")
								+ "). Tolgo un giorno al residuo.");
				if (maggiore) {
					// condizione per evitare loop
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger.debug("ERR. Esco per evitare loop");
					lMessage = "Attenzione! Impossibile determinare un Quantum tale da ottenere la Data Scarcerazione richiesta.";
					break;
				}
				minore = true;
				// Ho tolto troppo devo togliere un giorno in meno e riprovare,
				// tolgo un giorno alla pena ancora da espiare in modo che la pena
				// espiata abbia un giorno in più e la data fine sia maggiorne di un giorno
				lAnnRichiesta = (AnnotazioneManualeModel) lListaRichiesteGE.elementAt(0);
				lCalRecAncoraDaEspiareAl = lAnnRichiesta.getQuantumReclusione();
				lCalArrAncoraDaEspiareAl = lAnnRichiesta.getQuantumArresto();
				int totReclusione = CalendarUtil.getTotGiorni(lCalRecAncoraDaEspiareAl);
				int totArresto = CalendarUtil.getTotGiorni(lCalArrAncoraDaEspiareAl);
				if (totReclusione > 0) {
					totReclusione--;
					lCalRecAncoraDaEspiareAl.setNumGiorni(totReclusione);
					lCalRecAncoraDaEspiareAl.setNumMesi(0);
					lCalRecAncoraDaEspiareAl.setNumAnni(0);
					lCalRecAncoraDaEspiareAl = lCalUtil.ricalcolaGAM(lCalRecAncoraDaEspiareAl);
				} else if (totArresto > 0) {
					totArresto--;
					lCalArrAncoraDaEspiareAl.setNumGiorni(totArresto);
					lCalArrAncoraDaEspiareAl.setNumMesi(0);
					lCalArrAncoraDaEspiareAl.setNumAnni(0);
					lCalArrAncoraDaEspiareAl = lCalUtil.ricalcolaGAM(lCalArrAncoraDaEspiareAl);
				}
				lAnnRichiesta.setQuantumReclusione(lCalRecAncoraDaEspiareAl);
				lAnnRichiesta.setQuantumArresto(lCalArrAncoraDaEspiareAl);
			} else if (lDataFinePena.after(lDataScarcerazione)) {
				if (minore) {
					// condizione per evitare loop
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger.debug("ERR. Esco per evitare loop");
					lMessage = "Attenzione! Impossibile determinare un Quantum tale da ottenere la Data Scarcerazione richiesta.";
					break;
				}
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug(
						"Data fine rideterminata (" + DateUtils.getDateToString(lDataFinePena, "dd-MM-yyyy")
								+ ") maggiore della data di scarcerazione ("
								+ DateUtils.getDateToString(lDataScarcerazione, "dd-MM-yyyy")
								+ "). Aggiungo un giorno al residuo.");
				maggiore = true;
				// Ho tolto troppo poco, devo togliere un giorno in più e riprovare.
				// In questo modo tolgo un giorno alla pena ancora da espiare in modo che
				// la pena espiata abbia un giorno in meno e la data fine sia minore
				// di un giorno lCalRecAncoraDaEspiareAl
				lAnnRichiesta = (AnnotazioneManualeModel) lListaRichiesteGE.elementAt(0);
				lCalRecAncoraDaEspiareAl = lAnnRichiesta.getQuantumReclusione();
				lCalArrAncoraDaEspiareAl = lAnnRichiesta.getQuantumArresto();
				int totReclusione = CalendarUtil.getTotGiorni(lCalRecAncoraDaEspiareAl);
				int totArresto = CalendarUtil.getTotGiorni(lCalArrAncoraDaEspiareAl);
				if (totReclusione > 0) {
					totReclusione++;
					lCalRecAncoraDaEspiareAl.setNumGiorni(totReclusione);
					lCalRecAncoraDaEspiareAl.setNumMesi(0);
					lCalRecAncoraDaEspiareAl.setNumAnni(0);
					lCalRecAncoraDaEspiareAl = lCalUtil.ricalcolaGAM(lCalRecAncoraDaEspiareAl);
				} else if (totArresto > 0) {
					totArresto++;
					lCalArrAncoraDaEspiareAl.setNumGiorni(totArresto);
					lCalArrAncoraDaEspiareAl.setNumMesi(0);
					lCalArrAncoraDaEspiareAl.setNumAnni(0);
					lCalArrAncoraDaEspiareAl = lCalUtil.ricalcolaGAM(lCalArrAncoraDaEspiareAl);
				}
				lAnnRichiesta.setQuantumReclusione(lCalRecAncoraDaEspiareAl);
				lAnnRichiesta.setQuantumArresto(lCalArrAncoraDaEspiareAl);
			}
		}

		// Recupero il quantum della richiesta ottenuto dal ciclo di loop
		lAnnRichiesta = (AnnotazioneManualeModel) lListaRichiesteGE.elementAt(0);
		lCalRecAncoraDaEspiareAl = lAnnRichiesta.getQuantumReclusione();
		lCalArrAncoraDaEspiareAl = lAnnRichiesta.getQuantumArresto();
		lPenaAncoraDaEspiareAl.setQuantumReclusione(lCalRecAncoraDaEspiareAl);
		lPenaAncoraDaEspiareAl.setQuantumArresto(lCalArrAncoraDaEspiareAl);

		setRequestAttribute("Message", lMessage);
		setRequestAttribute("PenaResiduaAl", lPenaAncoraDaEspiareAl);
		setRequestAttribute("DataScarcerazione", lDataScarcerazione);
		setRequestAttribute("FormName", lFormName);

		return IWebConstants.ROOT_DIR + "files/siap/siep/calcolopena/PenaResiduaAl.jsp";
	}

	/**
	 * Metodo utilizzato nella vecchia versione del calcolo della pena
	 * 
	 * @deprecated
	 * @return
	 * @throws Exception
	 */
	public String processRequestOLD() throws Exception {
		BigDecimal lIdFascicoloSiep = ((FascicoloSiepModel) getSessionAttribute("fascicolo"))
				.getIdFascicoloSiep();

		// Recupero la data di scarcerazione
		Date lDataScarcerazione = getRequestDateParameter("annoScarcerazione", "meseScarcerazione",
				"giornoScarcerazione");

		String lFormName = getRequestStringParameter("formname");

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("lDataScarcerazione = " + lDataScarcerazione);

		PenaResiduaModel lUltimaPenaResidua = null;

		IPenaResidua IPenRes = SIEPLookupRemote.getPenaResiduaRemote();
		lUltimaPenaResidua = IPenRes.ExRicercaPenaResiduaUltimaByDate(lIdFascicoloSiep);
		// lUltimaPenaResidua = IPenRes.ExRicercaPenaResiduaByKey(lIdFascicoloSiep);

		ICalcoloPena lCalcPenaCtrl = SIEPLookupRemote.getCalcoloPenaRemote();
		// Normalizzo prima del calcolo
		boolean lIsCalcoloPenaAbInitio = lCalcPenaCtrl.ExIsCalcoloPenaAbInizio(lIdFascicoloSiep);
		if (!lIsCalcoloPenaAbInitio) {
			lUltimaPenaResidua = PenaResiduaUtil.calcolaPenaNuovaDataFine(lUltimaPenaResidua.getDataFine(),
					lUltimaPenaResidua, false);
		}

		PenaResiduaModel lPenaAncoraDaEspiareAl = new PenaResiduaModel();

		lPenaAncoraDaEspiareAl = lCalcPenaCtrl.exCalcolaPenaResiduaAl(lUltimaPenaResidua, lDataScarcerazione);

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("lPenaAncoraDaEspiareAl = " + lPenaAncoraDaEspiareAl);

		// ==========================================================================
		// Verifico se il quantum ricalcolato è tale da determinare una data fine
		// pena pari al giorno di calcolo. Per effetto degli errori di calcolo
		// i conti potrebbero non tornare per un giorno
		// ==========================================================================
		CalendarUtil lCalUtil = new CalendarUtil();

		PenaResiduaModel lPenaResiduaRidet = new PenaResiduaModel();

		CalendarModel lCalRecAncoraDaEspiareAl = new CalendarModel();
		CalendarModel lCalArrAncoraDaEspiareAl = new CalendarModel();

		// lCalRecAncoraDaEspiareAl
		lCalRecAncoraDaEspiareAl = lPenaAncoraDaEspiareAl.getQuantumReclusione();
		lCalArrAncoraDaEspiareAl = lPenaAncoraDaEspiareAl.getQuantumArresto();

		// ==========================================================================
		// possibile pezza: cerco di partire da una pena residua al più bassa di quella
		// calcolata. Questo per far convergere la procedura dal basso.
		// da fare.

		//
		CalendarModel lCalReclusione = new CalendarModel();
		CalendarModel lCalArresto = new CalendarModel();

		// Reclusione/Arresto
		lCalReclusione = lUltimaPenaResidua.getQuantumReclusione();
		lCalArresto = lUltimaPenaResidua.getQuantumArresto();

		CalendarModel lCalReclusioneRidet = new CalendarModel();
		CalendarModel lCalArrestoRidet = new CalendarModel();
		lCalReclusioneRidet = lCalUtil.sottraiGiorniNew(lCalReclusione, lCalRecAncoraDaEspiareAl);
		lCalArrestoRidet = lCalUtil.sottraiGiorniNew(lCalArresto, lCalArrAncoraDaEspiareAl);

		lPenaResiduaRidet.setQuantumReclusione(lCalReclusioneRidet);
		lPenaResiduaRidet.setQuantumArresto(lCalArrestoRidet);

		// boolean uguale = false;
		boolean maggiore = false;
		boolean minore = false;

		String lMessage = "";

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("Entro nel ciclo di verifica con:");
		while (true) {
			Date lDataFinePena = null;
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Reclusione Residua = " + lCalRecAncoraDaEspiareAl);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Arresti    Residui = " + lCalArrAncoraDaEspiareAl);

			Vector lDateFine = lCalcPenaCtrl.exCalcolaDataFinePena(lUltimaPenaResidua.getDataInizio(),
					lPenaResiduaRidet, true);

			if (lDateFine.size() == 0) {
				// quantum rideterminati negativi
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("ERR!! Quantum rideterminati negativi.Esco!");
				break;
			} else if (lDateFine.size() == 1) {
				// Solo fine pena
				lDataFinePena = (Date) lDateFine.get(0);
			} else if (lDateFine.size() == 2) {
				// Fine reclusione e fine arresto
				lDataFinePena = (Date) lDateFine.get(1);
			}

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Data fine calcolata: " + lDataFinePena);

			if (lDataFinePena.compareTo(lDataScarcerazione) == 0) {
				// uguale = true;
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug(
						"Data fine rideterminata coincidente con la data di scarcerazione. Quantum trovato!");
				break;
			} else if (lDataFinePena.before(lDataScarcerazione)) {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug(
						"Data fine rideterminata minore della data di scarcerazione. Tolgo un giorno al residuo.");
				if (maggiore) {
					// condizione per evitare loop
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger.debug("ERR. Esco per evitare loop");
					lMessage = "Attenzione! Impossibile determinare un Quantum tale da ottenere la Data Scarcerazione richiesta.";
					break;
				}
				minore = true;
				// Ho tolto troppo devo togliere un giorno in meno e riprovare,
				// tolgo un giorno alla pena ancora da espiare in modo che la pena
				// espiata abbia un giorno in più e la data fine sia maggiorne di un giorno
				// lCalRecAncoraDaEspiareAl
				int totReclusione = CalendarUtil.getTotGiorni(lCalRecAncoraDaEspiareAl);
				int totArresto = CalendarUtil.getTotGiorni(lCalArrAncoraDaEspiareAl);
				if (totReclusione > 0) {
					totReclusione--;
					lCalRecAncoraDaEspiareAl.setNumGiorni(totReclusione);
					lCalRecAncoraDaEspiareAl.setNumMesi(0);
					lCalRecAncoraDaEspiareAl.setNumAnni(0);
					lCalRecAncoraDaEspiareAl = lCalUtil.ricalcolaGAM(lCalRecAncoraDaEspiareAl);
				} else if (totArresto > 0) {
					totArresto--;
					lCalArrAncoraDaEspiareAl.setNumGiorni(totArresto);
					lCalArrAncoraDaEspiareAl.setNumMesi(0);
					lCalArrAncoraDaEspiareAl.setNumAnni(0);
					lCalArrAncoraDaEspiareAl = lCalUtil.ricalcolaGAM(lCalArrAncoraDaEspiareAl);
				}
				lCalReclusioneRidet = lCalUtil.sottraiGiorniNew(lCalReclusione, lCalRecAncoraDaEspiareAl);
				lCalArrestoRidet = lCalUtil.sottraiGiorniNew(lCalArresto, lCalArrAncoraDaEspiareAl);

				lPenaResiduaRidet.setQuantumReclusione(lCalReclusioneRidet);
				lPenaResiduaRidet.setQuantumArresto(lCalArrestoRidet);
			} else if (lDataFinePena.after(lDataScarcerazione)) {
				if (minore) {
					// condizione per evitare loop
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger.debug("ERR. Esco per evitare loop");
					lMessage = "Attenzione! Impossibile determinare un Quantum tale da ottenere la Data Scarcerazione richiesta.";
					// throw new F3BException("Impossibile determinare convergere");
					// break;
				}
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug(
						"Data fine rideterminata maggiore della data di scarcerazione. Aggiungo un giorno al residuo.");
				maggiore = true;
				// Ho tolto troppo poco, devo togliere un giorno in più e riprovare.
				// In questo modo tolgo un giorno alla pena ancora da espiare in modo che
				// la pena espiata abbia un giorno in meno e la data fine sia minore
				// di un giorno lCalRecAncoraDaEspiareAl
				int totReclusione = CalendarUtil.getTotGiorni(lCalRecAncoraDaEspiareAl);
				int totArresto = CalendarUtil.getTotGiorni(lCalArrAncoraDaEspiareAl);
				if (totReclusione > 0) {
					totReclusione++;
					lCalRecAncoraDaEspiareAl.setNumGiorni(totReclusione);
					lCalRecAncoraDaEspiareAl.setNumMesi(0);
					lCalRecAncoraDaEspiareAl.setNumAnni(0);
					lCalRecAncoraDaEspiareAl = lCalUtil.ricalcolaGAM(lCalRecAncoraDaEspiareAl);
				} else if (totArresto > 0) {
					totArresto++;
					lCalArrAncoraDaEspiareAl.setNumGiorni(totArresto);
					lCalArrAncoraDaEspiareAl.setNumMesi(0);
					lCalArrAncoraDaEspiareAl.setNumAnni(0);
					lCalArrAncoraDaEspiareAl = lCalUtil.ricalcolaGAM(lCalArrAncoraDaEspiareAl);
				}
				lCalReclusioneRidet = lCalUtil.sottraiGiorniNew(lCalReclusione, lCalRecAncoraDaEspiareAl);
				lCalArrestoRidet = lCalUtil.sottraiGiorniNew(lCalArresto, lCalArrAncoraDaEspiareAl);

				lPenaResiduaRidet.setQuantumReclusione(lCalReclusioneRidet);
				lPenaResiduaRidet.setQuantumArresto(lCalArrestoRidet);
			}
		}

		lPenaAncoraDaEspiareAl.setQuantumReclusione(lCalRecAncoraDaEspiareAl);
		lPenaAncoraDaEspiareAl.setQuantumArresto(lCalArrAncoraDaEspiareAl);

		setRequestAttribute("Message", lMessage);
		setRequestAttribute("PenaResiduaAl", lPenaAncoraDaEspiareAl);
		setRequestAttribute("DataScarcerazione", lDataScarcerazione);
		setRequestAttribute("FormName", lFormName);

		return IWebConstants.ROOT_DIR + "files/siap/siep/calcolopena/PenaResiduaAl.jsp";
	}

}