package siap.siep.calcolopena.action;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Vector;

import org.apache.log4j.Logger;

import siap.sico.calendar.model.CalendarModel;
import siap.sico.util.CalendarUtil;
import siap.sico.web.ActionSiap;
import siap.siep.SIEPException;
import siap.siep.annotazionemanuale.action.ICostantiAnnotazioneManuale;
import siap.siep.annotazionemanuale.controller.IAnnotazioneManuale;
import siap.siep.annotazionemanuale.model.AnnotazioneManualeModel;
import siap.siep.calcolopena.controller.ICalcoloPenaF5;
import siap.siep.calcolopena.model.CalcoloPenaModel;
import siap.siep.fascicolo.action.ICostantiFascicoloSiep;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.fungibilita.controller.IFungibilita;
import siap.siep.fungibilita.model.FungibilitaModel;
import siap.siep.penacomplessiva.controller.IPenaComplessiva;
import siap.siep.penacomplessiva.model.PenaComplessivaModel;
import siap.siep.penaresidua.controller.IPenaResidua;
import siap.siep.penaresidua.model.PenaResiduaModel;
import siap.siep.posizione.controller.IPosizioneGiuridica;
import siap.siep.posizione.model.PosizioneGiuridicaModel;
import siap.siep.util.SIEPLookupRemote;
import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;

/**
 * Classe Action per effettuare il calcolo e inserimento della pena a seguito delle seguenti operazioni:<br>
 * Rideterminazione Pena / Provvedimenti con Richiesta al GE:<br>
 * lFlagPage = GE - Depenelizzazione <br>
 * - Incostituzionalità<br>
 * - Amnistia/Indulto<br>
 * Decisioni del GE / Applicazione Benefici:<br>
 * - Depenelizzazione <br>
 * - Incostituzionalità<br>
 * - Amnistia/Indulto<br>
 * Rideterminazione Pena / Provvedimenti del PM:<br>
 * - presofferto<br>
 * - fungibilità (pena detentiva)<br>
 * - fungibilità (misura cautelare)<br>
 * Rideterminazione Pena / Annota avvenuto pagamento pena pecuniaria <br>
 * lFlagPage = ANNOTAPP
 *
 * Viene effettuato anche il calcolo e inserimento della Fungibilità La provenienza della chiamata viene
 * riconoscita dal parametro 'lFlagPage' letto sulla request.
 * 
 * 
 * Richieste al GE: 'GE' Decisioni del GE: 'AMNI', 'DEPEN', 'INCOST' Rideterminazione pena: A=Altro Titolo,
 * S=Senza Titolo, D=Stesso Titolo
 * 
 * @author
 *
 */
public class ActCalcoloPenaComputo extends ActionSiap implements ICostantiAnnotazioneManuale {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	/**
	 *
	 * @return Pagina di visualizzazione del dettaglio della pena ricalcolata per la conferma dei dati
	 *         (VediNuovoCalcoloPenaNew.jsp), oppure nel caso di Ergastolo direttamente la pagina delle
	 *         Stampe.
	 * @throws Exception
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String processRequest() throws Exception {

		String lPage = "";
		CalendarUtil lCalUtil = new CalendarUtil();

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("");
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("=================================================");
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("                                                 ");
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("         INIZIO CALCOLO DELLA PENA               ");
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("                                                 ");
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("=================================================");

		// ==========================================================================
		// Controlli preliminari
		// - fascicolo di competenza
		// - esistenza posizione giuridica
		// - esistenza PenaComplessiva
		// ==========================================================================
		BigDecimal lFascID = ((FascicoloSiepModel) getSessionAttribute("fascicolo")).getIdFascicoloSiep();
		isFascicoloSiepDiCompetenza();

		// Esistenza posizione giuridica
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

		// ======================
		// PENA Complessiva
		// ======================
		PenaComplessivaModel lPenComplMod = new PenaComplessivaModel();
		lPenComplMod.setFasSieIdFascicoloSiep(lFascID);
		IPenaComplessiva lPCon = SIEPLookupRemote.getPenaComplessivaRemote();
		Vector lPComples = lPCon.ExRicercaPenaComplessivaNoError(lPenComplMod);

		if (lPComples.size() == 0) {
			RedirectTo lRedirigi = new RedirectTo();
			lRedirigi.setPage(IWebConstants.PG_MAIN);
			setRequestAttribute(IWebConstants.MESSAGE_TEXT, "Pena Complessiva mancante");
			lRedirigi.setAction("siap.siep.fascicolo.action.ActLoadDettaglioFascicolo&"
					+ ICostantiFascicoloSiep.CAMPO_ID_FASCICOLO_SIEP + "="
					+ ((FascicoloSiepModel) getSessionAttribute("fascicolo")).getIdFascicoloSiep());
			setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi);

			return IWebConstants.PG_MESSAGE;
		}
		lPenComplMod = (PenaComplessivaModel) (lPComples.get(0));

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

		// ==========================================================================
		// Recupero la data inizio pena per i calcoli delle date di decorrenza:
		// La data inizio pena è data da: dataInizio dell'ultima pena residua VALIDATA
		// se esiste, altrimenti dell'ultima pena non validata se il computo è il
		// primo provvedimento.
		// ==========================================================================
		Date lDataInizioPena = null;

		IPenaResidua lPenResCtrl = SIEPLookupRemote.getPenaResiduaRemote();

		PenaResiduaModel lUltimaPenResVal = new PenaResiduaModel();
		lUltimaPenResVal = lPenResCtrl.ExRicercaPenaResiduaUltimaByDate(lFascID);
		// if (lUltimaPenResVal == null)
		// { // se non presente una pena validata utilizzo l'ultima in assoluto NO!
		// // non ha senso! Se non esiste una validata tanto vale ricalcolarla come
		// // se fosse il primo calcolo della pena ricalcolando la data inizio.
		// // Se si usa la data inizio pena dll'ultima non validata si richia di
		// // agganciare un calcolo intermedio errato effettuato prima della modifica
		// // della posizione giuridica
		// lUltimaPenResVal = lPenResCtrl.ExRicercaPenaResiduaCorrenteByFascicoloSiep(lFascID);
		// }
		if (lUltimaPenResVal == null) { // Non esiste proprio un pena validata non è mai stato fatto il primo
										// calcolo
										// della pena lo effettuo ora per avere dati da visualizzare sulla
										// form
										// Se trattasi di ergastolo???
										// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza
										// siesLogger al posto di LogF3B.getLogger()
			siesLogger.debug("Pena residua assente effettuo il primo calcolo");
			ActCalcoloPenaMain lActCalcoloPenaMain = new ActCalcoloPenaMain();
			CalcoloPenaModel lCalcPenaModel = lActCalcoloPenaMain.calcoloPena(lFascID, null);
			Date lDataInizioPena1 = lActCalcoloPenaMain.getDataPrimoCalcolo(lFascID);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Data inizio pena: " + lDataInizioPena1);

			try {
				lUltimaPenResVal = lCalcPenaModel.getPenaDaEspiare(lDataInizioPena1, null, null);
			} catch (Exception e) {
				// pezza da togliere serve solo per gestire la catch
				throw new F3BException(e);
			}
		}
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("Ultima pena residua VALIDATA = " + lUltimaPenResVal);

		if (lUltimaPenResVal == null) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger
					.debug("ATTENZIONE! non esiste una pena validata a sistema, impossibile determinare la Data Inizio Pena");
		} else if (lUltimaPenResVal.getDataInizio() == null) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger
					.debug("ATTENZIONE! Pena Validata a sistema senza data inizio, impossibile determinare la Data Inizio Pena");
		} else {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Recupero la data inizio pena in decorrenza...");
			lDataInizioPena = lUltimaPenResVal.getDataInizio();
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("...lDataInizioPena = " + lDataInizioPena);
		}

		// ==========================================================================
		// Solo se non ergastolo viene rieffettuato il vero calcolo della pena,
		// vale a dire vengono:
		// - ricalcolati i quantum
		// - le date di decorrenza
		// - l'eventuale fungibilità
		// 30/03/2007 - se specificati solo i quantum di pena pecuniaria, non va
		// rifatto l'intero calcolo della pena, ma aggiornati solo gli
		// importi dell'ultima validata
		// Se ergastolo invece:
		// - copia i dati dell'ultima pena
		// -
		// ==========================================================================
		if (!(lPenComplMod.getCodTipoPenaDetentiva().equals("03") || lPenComplMod.getCodTipoPenaDetentiva()
				.equals("04"))) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Soggetto non in ergastolo! Effettuo i calcoli ");

			// ========================================================================
			// Recupero i quantum di pena Validati che concorrono alla calcolo della
			// pena (n.b. non vengono recuperati i dati correnti da computare in
			// quanto non validati)
			// ========================================================================
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Recupero la situazione attuale della Pena (dati Validati)");
			ActCalcoloPenaMain lCalcoloPenaMain = new ActCalcoloPenaMain();
			CalcoloPenaModel lCalcoloPenaModel = lCalcoloPenaMain.calcoloPena(lFascID, null);
			lCalcoloPenaModel.setPenaUltimaValidata(lUltimaPenResVal);

			// ========================================================================
			// Effettuo un precalcolo della pena per verificare quale è la pena di
			// partenza. n.b. mi interessano solo i quantum
			// ========================================================================
			PenaResiduaModel lPenaDiPartenza = lCalcoloPenaModel.getPenaDaEspiare(null, null, null);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("**************************************************");
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("lPenaDiPartenza = " + lPenaDiPartenza);
			setRequestAttribute("lPenaDiPartenza", lPenaDiPartenza);

			// Recupero l'id dell'ultima Annotazione Manuale inserita
			// per passarlo alla JSP
			// se ne è presente più di una (tasto aggiungi)?
			if (!isRequestParameterNullObj(CAMPO_ID_ANNOTAZIONE_MANUALE)) {
				String lIdAnnotazioneManuale = getRequestStringParameter(CAMPO_ID_ANNOTAZIONE_MANUALE);
				setRequestAttribute("lIdAnnotazioneManualeUltimaInserita", lIdAnnotazioneManuale);
			}

			// ========================================================================
			// Recupero i quantum di Benefici Concessi/Revocati per i quali sto
			// effettuando i calcoli
			// ========================================================================
			String lCodTipoAnnotazione = getRequestStringParameter(CAMPO_COD_TIPO_ANNOTAZIONE);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger
					.debug("Recupero le Annotazioni manuali correnti (da validare): " + lCodTipoAnnotazione);

			ICalcoloPenaF5 lCalPenCtrlF5 = SIEPLookupRemote.getCalcoloPenaF5();

			String lFlagPage = this.getRequestStringParameter("lFlagPage");
			Vector lAnnotazioniCorrentiDaComputare = new Vector();

			if (lFlagPage.equals("GE")) {
				// Richieste al GE con anticipazione degli effetti (A)
				lAnnotazioniCorrentiDaComputare = lCalPenCtrlF5.exGetAnnotazioniManualiDaComputare(lFascID,
						lCodTipoAnnotazione, "A", null);
			} else if (lFlagPage.equals("AMNI") || lFlagPage.equals("DEPEN") || lFlagPage.equals("INCOST")) {
				// Decisioni del GE
				lAnnotazioniCorrentiDaComputare = lCalPenCtrlF5.exGetAnnotazioniManualiDaComputare(lFascID,
						lCodTipoAnnotazione, "-", null);
			} else if (lFlagPage.equals("A") || lFlagPage.equals("S") || lFlagPage.equals("D")) {
				// Computi (presofferti, fungibilità)
				lAnnotazioniCorrentiDaComputare = lCalPenCtrlF5.exGetAnnotazioniManualiDaComputare(lFascID,
						lCodTipoAnnotazione, "-", null);
			} else if (lFlagPage.equals("ALTRO") || lFlagPage.equals("ANNOTAPP")) {
				// Computi rideterminazione pena : Altro e Pagamento PP
				lAnnotazioniCorrentiDaComputare = lCalPenCtrlF5.exGetAnnotazioniManualiDaComputare(lFascID,
						lCodTipoAnnotazione, "-", null);
			}

			// ==========================================================================
			// Aggiungo i quantum recuperati al lCalcoloPenaModel che contiene solo i
			// dati validati, quindi non quelli correnti
			// ==========================================================================
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("lAnnotazioniCorrentiDaComputare.size()="
					+ lAnnotazioniCorrentiDaComputare.size());

			// Calendar contenenti i dati correnti da passare alla form di visualizzazione
			CalendarModel lAnnManConcessiReclusione = new CalendarModel();
			CalendarModel lAnnManConcessiArresto = new CalendarModel();
			CalendarModel lAnnManRevocatiReclusione = new CalendarModel();
			CalendarModel lAnnManRevocatiArresto = new CalendarModel();

			// Calendar contenenti i dati delle Richieste agganciate alle decisioni
			// correnti
			CalendarModel lAnnManRicConcessiReclusione = new CalendarModel();
			CalendarModel lAnnManRicConcessiArresto = new CalendarModel();
			CalendarModel lAnnManRicRevocatiReclusione = new CalendarModel();
			CalendarModel lAnnManRicRevocatiArresto = new CalendarModel();

			// Parametro da passare alla form di visualizzazione per indicare se le
			// richieste sono con anticipazione (A), senza (R) o miste (M)
			String lTipoRichieste = "";
			// Parametro da passare alla form di visualizzazione per indicare se la/le
			// decisioni sono:
			// - conformi (C)
			// - difformi (D)
			// - rigetti (R)
			// - dichiara inammissibilità (I)
			// - riunisce (U)
			// - senza richiesta (-)
			// - 'Miste' se più decisioni di tipo diverso
			String lTipoDecisione = "";

			boolean lIsSoloImporti = true;
			boolean lIsRichiestaSoloImporti = true;
			// Indica se il ricalcolo è legato a soli importi. In questo caso non devo
			// modificare nulla della PR già a sistema tranne gli importi.
			// Restano invariati quantum e date
			// N.B. SE STIAMO INSERENDO UNA DECISIONE DEL GE LEGATA A UNA O PIU'
			// RICHIESTE BISOGNA VERIFICARE SIA LA RICHIESTA CHE LA DECISIONE!!!
			// SE NELLA RICHIESTA CON ANTICIPAZIONE ERANO PRESENTI ANCHE I QUANTUM
			// DEVONO ESSERE RIFATTI I CALCOLI.

			// ========================================================================
			// Se presente solo importi mi trovo necessariamente su una richiesta al
			// GE o una Decisione. In questo caso devo sommare/sottrarre gli importi
			// dell'annotazione corrente dalla pena a sistema senza modificare quantum
			// e date di decorrenza. Quindi parto dall'ultima pena validata. Se esiste.
			// ========================================================================

			// ========================================================================
			// Per ogni annotazione corrente devo:
			// - sommare i quantum da passare alla form di visualizzazione
			// Se trattasi di DECISIONI del GE
			// - recuperare le richieste e sommare i quantum da passare alla form di visualizzazione
			// - eliminare dal CalcoloPenaModel le richieste su cui si sta esprimendo la decisione
			//
			// - verificare se la decisione è computabile o meno
			// - aggiungere il record al CalcolaPenaModel se computabile (che contiene solo i dati validati)
			//
			for (int i = 0; i < lAnnotazioniCorrentiDaComputare.size(); i++) {
				String lFlagComputabile = "S"; // di default i quantum sono tutti computabili

				AnnotazioneManualeModel lAnnMod = (AnnotazioneManualeModel) lAnnotazioniCorrentiDaComputare
						.elementAt(i);
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("Annotazione corrente:" + lAnnMod.toString2());

				if (lAnnMod.getFlagConforme() != null) {
					if (lTipoDecisione.equals("")) {
						lTipoDecisione = lAnnMod.getFlagConforme();
					} else if (!lTipoDecisione.equals(lAnnMod.getFlagConforme())) {
						lTipoDecisione = "M";
					}
				}

				// ======================================================================
				// Sommo i parziali concessi con il provvedimento corrente da passare
				// alla jsp di visualizzazione
				// ======================================================================
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("Sommo i parziali concessi/revocati con il provvedimento corrente...");
				// Reclusione
				CalendarModel lCalModRec = new CalendarModel();
				lCalModRec = lAnnMod.getQuantumReclusione();
				if (lAnnMod.getImportoMulta() != null)
					lCalModRec.setImportoMulta(lAnnMod.getImportoMulta().doubleValue());

				// Arresti
				CalendarModel lCalModArr = new CalendarModel();
				lCalModArr = lAnnMod.getQuantumArresto();
				if (lAnnMod.getImportoAmmenda() != null)
					lCalModArr.setImportoAmmenda(lAnnMod.getImportoAmmenda().doubleValue());

				if (CalendarUtil.getTotGiorni(lCalModRec) > 0 || CalendarUtil.getTotGiorni(lCalModArr) > 0) {
					lIsSoloImporti = false;
				}

				if (lAnnMod.getFlagPiuMeno() != null) {
					if (lAnnMod.getFlagPiuMeno().equals("-")) {
						lAnnManConcessiReclusione = lCalUtil.sommaGiornieValute(lAnnManConcessiReclusione,
								lCalModRec);
						lAnnManConcessiArresto = lCalUtil.sommaGiornieValute(lAnnManConcessiArresto,
								lCalModArr);
					} else if (lAnnMod.getFlagPiuMeno().equals("+")) {
						lAnnManRevocatiReclusione = lCalUtil.sommaGiornieValute(lAnnManRevocatiReclusione,
								lCalModRec);
						lAnnManRevocatiArresto = lCalUtil.sommaGiornieValute(lAnnManRevocatiArresto,
								lCalModArr);
					}
				} else {
					// Richeste senza segno. Prima della 1.2.1 era possibile inserire
					// richieste senza segno
				}

				// ======================================================================
				// Nel caso particolare delle Decisioni del GE, il lCalcoloPenaModel
				// contiene anche le Richieste su cui si è espresso il Giudice con la
				// decisione corrente.
				// Tali richiesta hanno il campo ANNO_ID_ANNOTAZIONE_MANUALE valorizzato,
				// ma vengono recuperate comunque perché collegate a decisioni non ancora
				// validate (quella che si sta inserendo).
				// Tali quantum non vanno più computati nei calcoli in quanto sostituiti
				// dalla decisione per cui devo eliminarli dal model prima di effettuare
				// i calcoli.
				// Attenzione!! Se tra la richiesta e la decisione è stata effettuata una
				// pena manuale o un evento interruttivo, la richiesta non
				// è tra i dati caricati.
				// Ma di fatto è stata computata (forse). A questo punto ho
				// la RICHIESTA computata sul quantum di pena del
				// lCalcoloPenaModel per cui se sottraggo anche la decisione
				// di fatto computo sia la richiesta che la decisione.
				// Non è possibile quindi eliminare la richiesta dal lCalcoloPenaModel
				// perchè non presenti. Devo quindi sottrarre la richiesta inserendola
				// nel model con segno opposto alla decisione
				//
				//
				// ======================================================================
				// Se Decisione di Amnistia o Indulto
				// n.b. per ora non vengono considerari depenalizzazione e incostituzionalità
				if ((lAnnMod.getCodTipoAnnotazione().equals("002")
						|| lAnnMod.getCodTipoAnnotazione().equals("003")
						// || lAnnMod.getCodTipoAnnotazione().equals("013")
						// MEV 37 -Inizio
						|| lAnnMod.getCodTipoAnnotazione().equals("004") || lAnnMod.getCodTipoAnnotazione()
						.equals("017")
				// MEV 37 -Fine
						)
						&& (lAnnMod.getFlagAppProvvisoria() == null || lAnnMod.getFlagAppProvvisoria()
								.equals("-") // decisione
						)) {
					// Devo :
					// - rimuovere la/le richieste
					// - verificare se la decisione corrente va considerata computabile
					// La decisione è computabile se:
					// - Senza richiesta
					// - conforme e richiesta in calcola pena model (cioè

					// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto
					// di LogF3B.getLogger()
					// siesLogger.debug("Rimuovo le Richieste dal CalcoloPenaModel ");

					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger.debug("Prima della rimozione Indulto: "
							+ lCalcoloPenaModel.getIndultoR().size());
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger.debug("Prima della rimozione Amnistia: "
							+ lCalcoloPenaModel.getAmnistiaR().size());
					// MEV 37
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger.debug("Prima della rimozione Depenalizza: "
							+ lCalcoloPenaModel.getDepenalizzazioneR().size());

					// ====================================================================
					// Recupero le richieste (con o senza anticipazione) collegate alla
					// decisione per sommare i quantum. (n.b. nel calcolo pena model sono
					// presenti solo le richieste con anticipazione, ma nella jsp di
					// visualizzazione devo visualizzare i quantum delle richieste anche
					// se le decisioni sono relative a richiesta senza anticipazione)
					// ====================================================================
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger.debug("Recupero le richieste associate alla decisione...");
					ICalcoloPenaF5 lCalcPenaF5 = SIEPLookupRemote.getCalcoloPenaF5();
					Vector lRichiestePerDecisione = lCalcPenaF5.exGetRichiesteAlGEbyIdDecisione(lAnnMod
							.getIdAnnotazioneManuale());
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger.debug("...richieste trovate: " + lRichiestePerDecisione.size());
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger.debug("Sommo i quantum delle richieste");

					// ====================================================================
					// Sommo i quantum delle richieste e verifico la loro natura:
					// - solo importo
					// - con o senza anticipazione, miste
					// Attenzione prima della versione 1.2.1 era possibile inserire richieste
					// senza quantum e con FLAG_PIU_MENO a null
					// ====================================================================
					for (int j = 0; j < lRichiestePerDecisione.size(); j++) {
						AnnotazioneManualeModel lRichiestaMod = (AnnotazioneManualeModel) lRichiestePerDecisione
								.elementAt(j);

						// Reclusione
						CalendarModel lCalModRecApp = new CalendarModel();
						lCalModRecApp = lRichiestaMod.getQuantumReclusione();
						if (lRichiestaMod.getImportoMulta() != null)
							lCalModRecApp.setImportoMulta(lRichiestaMod.getImportoMulta().doubleValue());

						// Arresti
						CalendarModel lCalModArrApp = new CalendarModel();
						lCalModArrApp = lRichiestaMod.getQuantumArresto();
						if (lRichiestaMod.getImportoAmmenda() != null)
							lCalModArrApp.setImportoAmmenda(lRichiestaMod.getImportoAmmenda().doubleValue());

						// ==================================================================
						// Se la richiesta era con anticipazione degli effetti e con quantum
						// di pena, devo comunque rieffettuare i calcoli.
						// ==================================================================
						if (lRichiestaMod.getFlagAppProvvisoria() != null
								&& lRichiestaMod.getFlagAppProvvisoria().equals("A")
								&& (CalendarUtil.getTotGiorni(lCalModRecApp) > 0 || CalendarUtil
										.getTotGiorni(lCalModArrApp) > 0)) {
							lIsRichiestaSoloImporti = false;
						}

						if (lRichiestaMod.getFlagPiuMeno() != null) {
							if (lRichiestaMod.getFlagPiuMeno().equals("-")) {
								lAnnManRicConcessiReclusione = lCalUtil.sommaGiornieValute(
										lAnnManRicConcessiReclusione, lCalModRecApp);
								lAnnManRicConcessiArresto = lCalUtil.sommaGiornieValute(
										lAnnManRicConcessiArresto, lCalModArrApp);
							} else if (lRichiestaMod.getFlagPiuMeno().equals("+")) {
								lAnnManRicRevocatiReclusione = lCalUtil.sommaGiornieValute(
										lAnnManRicRevocatiReclusione, lCalModRecApp);
								lAnnManRicRevocatiArresto = lCalUtil.sommaGiornieValute(
										lAnnManRicRevocatiArresto, lCalModArrApp);
							}
						} else {
							// La richiesta non ha segno e quindi quantum. Possibile prima delle 1.2.1
							//
						}

						// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto
						// di LogF3B.getLogger()
						siesLogger.debug("Richiesta: " + lRichiestaMod.toString2());
						// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto
						// di LogF3B.getLogger()
						siesLogger.debug("lTipoRichieste prima... " + lTipoRichieste);
						if (lTipoRichieste.equals("")) {
							lTipoRichieste = lRichiestaMod.getFlagAppProvvisoria();
						} else if (!lTipoRichieste.equals(lRichiestaMod.getFlagAppProvvisoria())) {
							// 'M' indica che il tipo di richieste associate alla decisione sono
							// in parte con anticipazione in parte senza
							lTipoRichieste = "M";
						}
						// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto
						// di LogF3B.getLogger()
						siesLogger.debug("...lTipoRichieste dopo " + lTipoRichieste);
					}

					// Attenzione a questo punto devo veirificare in quale scenario di
					// calcolo mi trovo. Devo eliminare comunque la richiesta dal model
					// ed decidere se la decisione corrente è computabile o meno. Inserirla
					// quindi nel model e porre il flag a computabile

					// ====================================================================
					// Rimuovo la richiesta con anticipazione cercandola o tra i record
					// amnistia o tra indulto
					// ====================================================================
					Vector lListaRichiesteIndulto = lCalcoloPenaModel.getIndultoR();
					boolean lRichiestaRimossa = false;
					for (int j = 0; j < lListaRichiesteIndulto.size(); j++) {
						AnnotazioneManualeModel lRichiestaMod = (AnnotazioneManualeModel) lListaRichiesteIndulto
								.elementAt(j);
						if (lRichiestaMod.getAnnoIdAnnotazioneManuale() != null
								&& lRichiestaMod.getAnnoIdAnnotazioneManuale().compareTo(
										lAnnMod.getIdAnnotazioneManuale()) == 0) {
							// rimuovo la Richiesta dal model in modo che non entri nei calcoli
							// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al
							// posto di LogF3B.getLogger()
							siesLogger.debug("Rimuovo Richiesta " + j + ": " + lRichiestaMod);
							lRichiestaRimossa = true;
							lListaRichiesteIndulto.remove(j);
							j--;
						}
					}

					// Cerco la richiesta tra Amnistia
					Vector lListaRichiesteAmnistia = lCalcoloPenaModel.getAmnistiaR();
					for (int j = 0; j < lListaRichiesteAmnistia.size(); j++) {
						AnnotazioneManualeModel lRichiestaMod = (AnnotazioneManualeModel) lListaRichiesteAmnistia
								.elementAt(j);
						if (lRichiestaMod.getAnnoIdAnnotazioneManuale() != null
								&& lRichiestaMod.getAnnoIdAnnotazioneManuale().compareTo(
										lAnnMod.getIdAnnotazioneManuale()) == 0) {
							// rimuovo la Richiesta dal model in modo che non entri nei calcoli
							// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al
							// posto di LogF3B.getLogger()
							siesLogger.debug("Rimuovo Richiesta " + j + ": " + lRichiestaMod);
							lRichiestaRimossa = true;
							lListaRichiesteAmnistia.remove(j);
							j--;
						}
					}

					// MEV 37 - Inizio
					// Cerco la richiesta tra Amnistia
					Vector lListaRichiesteDepe = lCalcoloPenaModel.getDepenalizzazioneR();
					for (int j = 0; j < lListaRichiesteDepe.size(); j++) {
						AnnotazioneManualeModel lRichiestaMod = (AnnotazioneManualeModel) lListaRichiesteDepe
								.elementAt(j);
						if (lRichiestaMod.getAnnoIdAnnotazioneManuale() != null
								&& lRichiestaMod.getAnnoIdAnnotazioneManuale().compareTo(
										lAnnMod.getIdAnnotazioneManuale()) == 0) {
							// rimuovo la Richiesta dal model in modo che non entri nei calcoli
							// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al
							// posto di LogF3B.getLogger()
							siesLogger.debug("Rimuovo Richiesta Depe " + j + ": " + lRichiestaMod);
							lRichiestaRimossa = true;
							lListaRichiesteDepe.remove(j);
							j--;
						}
					}
					// MEV 37 - Fine

					// Cerco la richiesta tra Gli Indulti RES
					Vector lListaIndultiMigratiRes = lCalcoloPenaModel.getIndulto(); // n.b. per ora richieste
																						// e decisioni res
																						// vengono trattate
																						// come decisioni
					for (int j = 0; j < lListaIndultiMigratiRes.size(); j++) {
						AnnotazioneManualeModel lRichiestaMod = (AnnotazioneManualeModel) lListaIndultiMigratiRes
								.elementAt(j);
						if (lRichiestaMod.getAnnoIdAnnotazioneManuale() != null
								&& lRichiestaMod.getAnnoIdAnnotazioneManuale().compareTo(
										lAnnMod.getIdAnnotazioneManuale()) == 0) {
							// rimuovo la Richiesta dal model in modo che non entri nei calcoli
							// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al
							// posto di LogF3B.getLogger()
							siesLogger.debug("Rimuovo Richiesta " + j + ": " + lRichiestaMod);
							lRichiestaRimossa = true;
							lListaIndultiMigratiRes.remove(j);
							j--;
						}
					}

					// ====================================================================
					// Verifico se la decisione è computabile o meno. Serve per gestire il
					// caso in cui tra la richiesta con anticipazione e la decisione è
					// presente un evento intermedio.
					// ====================================================================
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger.debug("Verifico se la decisione è computabile o meno...");
					if (lAnnMod.getFlagConforme().equals("-")) {// Decisione senza richiesta sempre
																// computabile
																// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la
																// variabile di istanza siesLogger al posto di
																// LogF3B.getLogger()
						siesLogger.debug("Decisione senza richiesta sempre computabile");
						lFlagComputabile = "S";
					} else if (lRichiestaRimossa) { // La decisione era legata ad almeno una richiesta con
													// anticipazione inserita dopo
													// l'evento interruttivo. La decisione è computabile se:
													// conforme o difforme
													// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di
													// istanza siesLogger al posto di LogF3B.getLogger()
						siesLogger
								.debug("Decisione legata ad almeno una richiesta con anticipazione inserita dopo l'evento interruttivo.");
						if (lAnnMod.getFlagConforme().equals("C") // Conforme
								|| lAnnMod.getFlagConforme().equals("D") // Difforme
						) {
							// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al
							// posto di LogF3B.getLogger()
							siesLogger.debug("Decisione C o D, quindi computabile");
							lFlagComputabile = "S";
						} else if (lAnnMod.getFlagConforme().equals("R") // Rigetta
								|| lAnnMod.getFlagConforme().equals("I") // Inammissibile
								|| lAnnMod.getFlagConforme().equals("U") // Unisce
						) {
							// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al
							// posto di LogF3B.getLogger()
							siesLogger.debug("Decisione R,I o U quindi non computabile");
							lFlagComputabile = "N";
						}
					} else {
						// Richiesta non presente nel CalcoloPenaModel. Ciò implica che:
						// - o è senza anticipazione
						// - è stata inserita prima di un evento interruttivo per cui non
						// è stata recuperata per il calcolo.
						// - oppure non è stata agganciata la richiesta alla decisione in
						// fase di scarico. In questo caso comunque la decisione va
						// considerata computabile.
						// Se la richiesta era senza ancticipazione, che sia stata inserita
						// prima o dopo un evento interruttivo nulla cambia.
						// Diverso è il discorso se la richiesta era con anticipazione
						// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto
						// di LogF3B.getLogger()
						siesLogger
								.debug("Richiesta inserita prima di un evento interruttivo o senza anticipazione");
						boolean lIsRichAnticipazione = false;
						for (int j = 0; j < lRichiestePerDecisione.size(); j++) {
							AnnotazioneManualeModel lRichiestaMod = (AnnotazioneManualeModel) lRichiestePerDecisione
									.elementAt(j);
							if (lRichiestaMod.getFlagAppProvvisoria().equals("A")) {
								lIsRichAnticipazione = true;
							}
						}

						if (lRichiestePerDecisione.size() == 0) {
							// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al
							// posto di LogF3B.getLogger()
							siesLogger.debug("Richiesta non indicata");
						}

						if (lIsRichAnticipazione) {
							// Non computabile solo se conforme (già caricata su Pena Iniziale)
							// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al
							// posto di LogF3B.getLogger()
							siesLogger.debug("Richiesta con Anticipazione ");
							if (lAnnMod.getFlagConforme().equals("C"))
								lFlagComputabile = "N";
							else
								lFlagComputabile = "S";
						}

						else if (lRichiestePerDecisione.size() > 0) {
							// senza anticipazione degli effetti
							// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al
							// posto di LogF3B.getLogger()
							siesLogger.debug("Richiesta senza Anticipazione ");
							if (lAnnMod.getFlagConforme().equals("C") // Conforme
									|| lAnnMod.getFlagConforme().equals("D") // Difforme
							) {
								lFlagComputabile = "S";
							} else if (lAnnMod.getFlagConforme().equals("R") // Rigetta
									|| lAnnMod.getFlagConforme().equals("I") // Inammissibile
									|| lAnnMod.getFlagConforme().equals("U") // Unisce
							) {
								lFlagComputabile = "N";
							}
						} else {
							// Richiesta non presente o comunque non agganciata
							// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al
							// posto di LogF3B.getLogger()
							siesLogger.debug("Richiesta non presente o comunque non agganciata");
							// non so cosa fare in questo caso, quidi computo sempre i dati della
							// decisione. Se l'utente decide di fare il calcolo della pena
							// ciò che ha scritto è computabile. Il calcolo della pena infatti
							// dovrebbe servire a modificare la pena attualmente a sistema e
							// l'unico modo è quello di computare quanto indicato con relativo
							// segno. (23/06/2009)
							lFlagComputabile = "S";
						}
					}
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger.debug("Decisione computabile: " + lFlagComputabile);
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger.debug("Dopo la rimozione Indulto: " + lCalcoloPenaModel.getIndultoR().size());
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger
							.debug("Dopo la rimozione Amnistia: " + lCalcoloPenaModel.getAmnistiaR().size());
					// MEV 37
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger.debug("Dopo la rimozione Depe: "
							+ lCalcoloPenaModel.getDepenalizzazioneR().size());

				} // chiude if ( ( lAnnMod.getCodTipoAnnotazione().equals("002") .........

				// ======================================================================
				// Aggiungo il provvedimento al CalcoloPenaModel per utilizzarli nel
				// calcolo totale della pena.
				// n.b. devo aggiungerlo all'opportuno vettore
				// n.b. se trattasi di Decisioni del GE di tipo R(rigetto), I(dichiara inammissibile)
				// U=Riunisce, la decisione non va inserita nel CalcoloPenaModel
				// perchè non deve entrare nei calcoli. Infatti il rigetto di una
				// richiesta con anticipazione già annulla la richiesta, quindi non
				// deve sottrarre nuovamente i quantum rigettati.
				// Da verificare con la nuova versione
				// ======================================================================
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("Aggiungo le annotazioni correnti ai dati validati...");
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("lFlagComputabile= " + lFlagComputabile);
				if (lFlagComputabile.equalsIgnoreCase("S")) {
					if (lAnnMod.getCodTipoAnnotazione().equals("004")
					// MEV 37 - Inizio
							|| lAnnMod.getCodTipoAnnotazione().equals("017"))
					// MEV 37 - Fine
					{ // Depenalizzazione o Illecito Amministrativo
						if (lAnnMod.getFlagAppProvvisoria().equals("A")) {
							lCalcoloPenaModel.getDepenalizzazioneR().add(lAnnMod);
						} else {
							lAnnMod.setFlagComputabile("S");
							lCalcoloPenaModel.getDepenalizzazione().add(lAnnMod);
						}
					} else if (lAnnMod.getCodTipoAnnotazione().equals("013")) { // Incostituzionalità
						if (lAnnMod.getFlagAppProvvisoria().equals("A")) {
							lCalcoloPenaModel.getIncostituzionalitaR().add(lAnnMod);
						} else {
							lAnnMod.setFlagComputabile("S");
							lCalcoloPenaModel.getIncostituzionalita().add(lAnnMod);
						}
					} else if (lAnnMod.getCodTipoAnnotazione().equals("003")) { // Amnistia
						if (lAnnMod.getFlagAppProvvisoria().equals("A"))
							lCalcoloPenaModel.getAmnistiaR().add(lAnnMod);
						// else if ( lAnnMod.getFlagConforme()==null
						// || ( lAnnMod.getFlagConforme()!=null
						// && !lAnnMod.getFlagConforme().equals("R")
						// && !lAnnMod.getFlagConforme().equals("I")
						// && !lAnnMod.getFlagConforme().equals("U")
						// )
						// )
						else {
							lAnnMod.setFlagComputabile("S");
							lCalcoloPenaModel.getAmnistia().add(lAnnMod);
						}
					} else if (lAnnMod.getCodTipoAnnotazione().equals("002")) { // Indulto
						if (lAnnMod.getFlagAppProvvisoria().equals("A"))
							lCalcoloPenaModel.getIndultoR().add(lAnnMod);
						// else if ( lAnnMod.getFlagConforme()==null
						// || ( lAnnMod.getFlagConforme()!=null
						// && !lAnnMod.getFlagConforme().equals("R")
						// && !lAnnMod.getFlagConforme().equals("I")
						// && !lAnnMod.getFlagConforme().equals("U")
						// )
						// )
						else {
							lAnnMod.setFlagComputabile("S");
							lCalcoloPenaModel.getIndulto().add(lAnnMod);
						}
					} else if (lAnnMod.getCodTipoAnnotazione().equals("005")) { // Presofferto
						lCalcoloPenaModel.getPresoffertoAltroReato().add(lAnnMod);
					} else if (lAnnMod.getCodTipoAnnotazione().equals("006")) { // Fungibilità MC
						lCalcoloPenaModel.getFungibilitaAltroReatoMC().add(lAnnMod);
					} else if (lAnnMod.getCodTipoAnnotazione().equals("007")) { // Fungibilità PD
						lCalcoloPenaModel.getFungibilitaAltroReatoPD().add(lAnnMod);
					} else if (lAnnMod.getCodTipoAnnotazione().equals("014")) { // Rideterminazione pena
																				// 'Altro'
						lCalcoloPenaModel.getFungibilitaAltroReatoPD().add(lAnnMod);
					} else {
						// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto
						// di LogF3B.getLogger()
						siesLogger.error("!!!!!Codice Annotazione " + lAnnMod.getCodTipoAnnotazione()
								+ " non gestito");
						// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto
						// di LogF3B.getLogger()
						siesLogger.error("!!!!! i quantum non verranno aggiunti e quindi calcolati");
					}
				} else {
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger.debug("Non aggiungo la richiesta corrente in quanto non computabile");
				}

			} // CHIUDE CICLO for (int i=0; i<lAnnotazioniCorrentiDaComputare.size(); i++)

			// ========================================================================
			// Nel caso dei computi vengono passate alla finestra le annotazioni
			// invece dei quantum separati
			// ========================================================================
			setRequestAttribute("ListaAnnotazioni", lAnnotazioniCorrentiDaComputare);
			setRequestAttribute("TipoRichieste", lTipoRichieste);
			setRequestAttribute("TipoDecisione", lTipoDecisione);

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("lTipoRichieste finale " + lTipoRichieste);

			// ==========================================================================
			// Effettuo i calcoli
			// ==========================================================================
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Effettuo il calcolo della pena con tutti i dati...");
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Solo importi: " + lIsSoloImporti);
			// Casi possibili:
			// Richiesta al GE: in questo caso è difficile (ma non impossibile) che
			// abbiano effettuato una richiesta con anticipazione di soli importi
			// Decisione del GE: potrebbe essere di soli importi, legata a richiesta con o senza quantum.
			// Altro
			PenaResiduaModel lPenaRideterminata = null;
			if (lIsSoloImporti && lIsRichiestaSoloImporti && lUltimaPenResVal != null) {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("Aggiorno solo gli importi");
				// Effettuo i soli calcoli degli importi.
				// n.b. il metodo rieffettua tutti i calcoli, ma poi prendo per buoni
				// i soli importi e copio tal quali i quantum e le date
				lPenaRideterminata = lCalcoloPenaModel.getPenaDaEspiare(null, null, null);

				// Copio i quantum
				lPenaRideterminata.setNumGiorniReclusione(lUltimaPenResVal.getNumGiorniReclusione());
				lPenaRideterminata.setNumMesiReclusione(lUltimaPenResVal.getNumMesiReclusione());
				lPenaRideterminata.setNumAnniReclusione(lUltimaPenResVal.getNumAnniReclusione());

				lPenaRideterminata.setNumGiorniArresto(lUltimaPenResVal.getNumGiorniArresto());
				lPenaRideterminata.setNumMesiArresto(lUltimaPenResVal.getNumMesiArresto());
				lPenaRideterminata.setNumAnniArresto(lUltimaPenResVal.getNumAnniArresto());

				// Copio le date dell'ultima pena validata
				lPenaRideterminata.setDataInizio(lUltimaPenResVal.getDataInizio());
				lPenaRideterminata.setDataFineReclusione(lUltimaPenResVal.getDataFineReclusione());
				lPenaRideterminata.setDataInizioArresto(lUltimaPenResVal.getDataInizioArresto());
				lPenaRideterminata.setDataFinePresunta(lUltimaPenResVal.getDataFinePresunta());
				lPenaRideterminata.setDataFine(lUltimaPenResVal.getDataFine());

				// annullo l'eventuale fungibilità calcolate se quantum negativi
				lCalcoloPenaModel.setFungibilitaCalcolata(new FungibilitaModel());
			} else {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("Rieffettuo tutti i calcoli");
				lPenaRideterminata = lCalcoloPenaModel.getPenaDaEspiare(lDataInizioPena,
						dataSistemaPerCalcoli, "all");
			}

			FungibilitaModel lFungModel = lCalcoloPenaModel.getFungibilitaCalcolata();
			CalendarModel lPenaGiaEspiata = lCalcoloPenaModel.getPenaEspiata();

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Pena residua ricalcolata: " + lPenaRideterminata);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Fungibilità ricalcolata: " + lFungModel);

			// ==========================================================================
			// Finisco di valorizzare i campi del PenaResiduaModel.
			// n.b. la funzione getPenaDaEspiare valorizza SOLO i quantum e gli importi
			// del model oltre alle date di espiazione (se possibile)
			// ==========================================================================
			lPenaRideterminata.setFasSieIdFascicoloSiep(lFascID);
			lPenaRideterminata.setFlagValidato("N");
			lPenaRideterminata.setDiesAQuo("S");
			lPenaRideterminata.setFlagErgastolo("N");

			// devo copiare il flag perchè altrimenti mi perdo l'informazione sullo
			// stato della pena (se sospesa, deve rimanere sospesa)
			if (lUltimaPenResVal != null) {
				lPenaRideterminata.setFlagPenaSospesa(lUltimaPenResVal.getFlagPenaSospesa());
			}

			lPenaRideterminata.setCodOperatoreInserimento(getCodUtenteConnesso());
			lPenaRideterminata.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
			lPenaRideterminata.setDataInserimento(DateUtils.getSysDate());

			// ==========================================================================
			// Finisco di valorizzare i campi del FungibilitaModel.
			// n.b. la funzione getFungibilitaCalcolata valorizza SOLO i quantum della
			// fungibilità
			// ==========================================================================
			if (lFungModel != null) {
				// La fungibilità è legata all'evento e al fascicolo
				lFungModel.setFasSieIdFascicoloSiep(lFascID);
				lFungModel.setEveIdEvento(getRequestBigDecimalParameter(CAMPO_EVE_ID_EVENTO));

				lFungModel.setCodTipoFungibilita("02"); // 02=PENA ESPIATA IN ECCESSO
				lFungModel.setFlagValidato("N");

				lFungModel.setCodOperatoreInserimento(getCodUtenteConnesso());
				lFungModel.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
				lFungModel.setDataInserimento(DateUtils.getSysDate());
			}

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Pena residua ricalcolata: " + lPenaRideterminata);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Fungibilità ricalcolata: " + lFungModel);

			// lPPres.ExModificaPenaResidua(lPenaComplessiva);

			// ========================================================================
			// Aggiorno la pena_residua aggiungendo l'id dell'evento
			// questo serve nel caso delle Decisioni del GE e Computi in cui è
			// necessario legare subito la pena residua al provvedimento (lo stesso
			// a cui è legata l'annotazione)
			// Nel caso di Richieste invece, la pena residua viene agganciata all'evento
			// prodotto in fase di stampa
			// ========================================================================
			// if ( !lFlagPage.equals("GE") )
			// {
			BigDecimal lEveIdEvento = getRequestBigDecimalParameter(CAMPO_EVE_ID_EVENTO);
			lPenaRideterminata.setEveIdEvento(lEveIdEvento);
			setRequestAttribute("IdEvento", lEveIdEvento);
			// }

			// ========================================================================
			// Inserisco i dati a sistema PenaResidua-Fungibilità
			// ========================================================================
			lPenaRideterminata = lPenResCtrl.ExInsertOrUpdatePenaResidua(lPenaRideterminata);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Dopo Inserimento/Aggiornamento PR " + lPenaRideterminata.getIdPenaResidua());

			// ========================================================================
			// Verifico se presente fungibilità già agganciata all'evento in un calcolo
			// precedente in questo caso la cancello ed eventualmente la reinserisco
			// ========================================================================
			IFungibilita lFungCtrl = SIEPLookupRemote.getFungibilitaRemote();

			FungibilitaModel lFungApp = null;

			lFungApp = lFungCtrl.ExRicercaFungibilitaByKeyEvento(lFungModel.getEveIdEvento());
			if (lFungApp != null) {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("Cancello la fungibilità: " + lFungApp.getIdFungibilita());
				lFungCtrl.ExCancellaFungibilita(lFungApp);
			}

			if (!lCalUtil.isZero(lFungModel.getQuantumFungibilita())) {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("Inserisco la fungibilità");
				lFungCtrl = SIEPLookupRemote.getFungibilitaRemote();
				lFungModel = lFungCtrl.ExInserisciFungibilita(lFungModel);
			}

			// //========================================================================
			// // New. Per gestione azzeramento quantum in caso di richieste con
			// // scarcerazione immediata
			// // Richieste: FlagPage = GE
			// // Fine pena rideterminato <= di data di sistema
			// //========================================================================
			// // Verifico se già presente la sospensione per eveitare di duplicarla
			// // n.b. il tasto calcolo pena può essere premuto più volte
			// ISospensione lSospCtrl = SIEPLookupRemote.getSospensioneRemote();
			// SospensioneModel lSospApp = null;
			// lSospApp =
			// lSospCtrl.ExRicercaSospensioneByIdPenaResidua(lPenaRideterminata.getIdPenaResidua());
			// if (lSospApp!=null){
			// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			// siesLogger.debug("Cancello la sospensione "+lSospApp.getIdSospensione());
			// lSospCtrl.ExCancellaSospensione(lSospApp);
			// }
			//
			//
			// if ( lFlagPage.equals("GE")
			// && !(lIsSoloImporti && lIsRichiestaSoloImporti)
			// && lPenaRideterminata.getDataInizio()!=null
			// && lPenaRideterminata.getDataFine()!=null
			// )
			// {
			// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			// siesLogger.debug(" Verifico se possibile azzeramento ");
			// Date lOggi = DateUtils.getSysDate();
			//
			// String lGiorno = DateUtils.getDateToString(lOggi,"dd");
			// String lMese = DateUtils.getDateToString(lOggi,"MM");
			// String lAnno = DateUtils.getDateToString(lOggi,"yyyy");
			// lOggi = DateUtils.getDate( lAnno, lMese, lGiorno );
			// if ( !DateUtils.isGreater(lPenaRideterminata.getDataFine(),lOggi) )
			// {
			// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			// siesLogger.debug(" Possibile azzeramento. Fine pena <= data di sistema");
			// // Devo calcolare la pena espiata e la pena
			// SospensioneModel lSospMod = new SospensioneModel();
			//
			// lSospMod.setFasSieIdFascicoloSiep(lPenaRideterminata.getFasSieIdFascicoloSiep());
			// lSospMod.setPenResIdPenaResidua(lPenaRideterminata.getIdPenaResidua());
			//
			// CalendarModel lRec = lPenaRideterminata.getQuantumReclusione();
			// CalendarModel lArr = lPenaRideterminata.getQuantumArresto();
			// CalendarModel lPenaEspiata = lCalUtil.sommaGiorni(lRec,lArr);
			//
			// // Inizio interruzione = data scarcerazione = data fine pena (!!!DA VERIFICARE
			// lSospMod.setDataInizio(lPenaRideterminata.getDataFine());
			//
			// // Pena espiata
			// lSospMod.setNumAnniPenaEspiata (new BigDecimal (lPenaEspiata.getNumAnni()));
			// lSospMod.setNumMesiPenaEspiata (new BigDecimal (lPenaEspiata.getNumMesi()));
			// lSospMod.setNumGiorniPenaEspiata (new BigDecimal (lPenaEspiata.getNumGiorni()));
			// lSospMod.setNumGiorniLibanticipata (new BigDecimal
			// (lCalcoloPenaModel.getLiberazioneAnticipata()));
			//
			// // Reclusione
			// lSospMod.setNumAnniPenaResiduaReclus (new BigDecimal(0));
			// lSospMod.setNumMesiPenaResiduaReclus (new BigDecimal(0));
			// lSospMod.setNumGiorniPenaResiduaReclus (new BigDecimal(0));
			// lSospMod.setMultaResidua(lPenaRideterminata.getImportoMulta());
			//
			// // Arresti
			// lSospMod.setNumAnniPenaResiduaArres (new BigDecimal(0));
			// lSospMod.setNumMesiPenaResiduaArres (new BigDecimal(0));
			// lSospMod.setNumGiorniPenaResiduaArres (new BigDecimal(0));
			// lSospMod.setAmmendaResidua(lPenaRideterminata.getImportoAmmenda());
			//
			//
			// lSospMod.setCodOperatoreInserimento (lPenaRideterminata.getCodOperatoreInserimento());
			// lSospMod.setDataInserimento (lPenaRideterminata.getDataInserimento());
			// lSospMod.setCodUfficioInserimento (lPenaRideterminata.getCodUfficioInserimento());
			//
			// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			// siesLogger.debug("SOSPENSIONE - "+lSospMod);
			//
			// // Inserisco sospensione
			// lSospCtrl = SIEPLookupRemote.getSospensioneRemote();
			// lSospMod = lSospCtrl.ExInserisciSospensione(lSospMod);
			//
			// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			// siesLogger.debug("Dopo inserimento SOSPENSIONE - "+lSospMod);
			// }
			// }

			// ========================================================================
			// Nel caso delle DECISIONI del GE devo aggiornare il flag computabile
			// sulle annotazioni collegate alla decisione (provvedimento).
			// Attenzione NON tutte le decisioni sono computabili anche se sto effettando
			// il calcolo della pena, dipende dal tipo di decisione e dal tipo di richieste
			// ========================================================================
			if (lFlagPage.equals("AMNI") || lFlagPage.equals("DEPEN") || lFlagPage.equals("INCOST")) {
				for (int i = 0; i < lAnnotazioniCorrentiDaComputare.size(); i++) {
					AnnotazioneManualeModel lAnnMod = (AnnotazioneManualeModel) lAnnotazioniCorrentiDaComputare
							.elementAt(i);
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger.debug("Annotazione corrente:" + lAnnMod.toString2());
					if (lAnnMod.getFlagComputabile() != null
							&& lAnnMod.getFlagComputabile().equalsIgnoreCase("S")) {
						// Decisioni del GE
						// BigDecimal lEveIdEvento = getRequestBigDecimalParameter(CAMPO_EVE_ID_EVENTO);
						IAnnotazioneManuale lCtrlAnnMan = SIEPLookupRemote.getAnnotazioneManualeRemote();
						lCtrlAnnMan.ExUpdateAnnotazioneFlagComputabile(lAnnMod.getIdAnnotazioneManuale());
					}
				}
			}

			// ========================================================================
			// Restituisco i dati alla finestra di visualizzazione
			// ========================================================================
			setRequestAttribute("PenaRideterminata", lPenaRideterminata); // la pena rideterminata
			setRequestAttribute("PenaGiaEspiata", lPenaGiaEspiata); // pena già espiata
			setRequestAttribute("Fungibilita", lFungModel); // fungibilità

			if (lIsSoloImporti && lIsRichiestaSoloImporti) {
				setRequestAttribute("lIsSoloImporti", new Boolean(true));
			} else {
				setRequestAttribute("lIsSoloImporti", new Boolean(false));
			}

			setRequestAttribute("lIsCalcoloPenaAbInitio", new Boolean(true)); // da eliminare

			setRequestAttribute("lCalcoloPenaModel", lCalcoloPenaModel);
			// ========================================================================
			// Quantum Complessivi
			setRequestAttribute("PenaComplessivaSentenza", lCalcoloPenaModel.getPenaInSentenza());
			setRequestAttribute("BeneficiConcessiReclusione",
					lCalcoloPenaModel.getBeneficiReclusioneInSentenza("C"));
			setRequestAttribute("BeneficiConcessiArresto",
					lCalcoloPenaModel.getBeneficiArrestiInSentenza("C"));
			setRequestAttribute("BeneficiRevocatiReclusione",
					lCalcoloPenaModel.getBeneficiReclusioneInSentenza("R"));
			setRequestAttribute("BeneficiRevocatiArresto",
					lCalcoloPenaModel.getBeneficiArrestiInSentenza("R"));

			setRequestAttribute("MisCauComputabiliReclusione", lCalcoloPenaModel.getMCReclusioneInSentenza());
			setRequestAttribute("MisCauComputabiliArresto", lCalcoloPenaModel.getMCArrestiInSentenza());

			// Benefici concessi/revocati con provvedimento
			// setRequestAttribute("AnnManConcessiReclusione", lCalcoloPenaModel.getBeneficiReclusione("-"));
			// setRequestAttribute("AnnManConcessiArresto" , lCalcoloPenaModel.getBeneficiArresti("-"));
			// setRequestAttribute("AnnManRevocatiReclusione", lCalcoloPenaModel.getBeneficiReclusione("+"));
			// setRequestAttribute("AnnManRevocatiArresto" , lCalcoloPenaModel.getBeneficiArresti("+"));

			setRequestAttribute("AnnManConcessiReclusione", lAnnManConcessiReclusione);
			setRequestAttribute("AnnManConcessiArresto", lAnnManConcessiArresto);
			setRequestAttribute("AnnManRevocatiReclusione", lAnnManRevocatiReclusione);
			setRequestAttribute("AnnManRevocatiArresto", lAnnManRevocatiArresto);

			// Quantum globali delle richieste che sono state computate nel provvedimento
			// corrente
			setRequestAttribute("AnnManRicConcessiReclusione", lAnnManRicConcessiReclusione);
			setRequestAttribute("AnnManRicConcessiArresto", lAnnManRicConcessiArresto);
			setRequestAttribute("AnnManRicRevocatiReclusione", lAnnManRicRevocatiReclusione);
			setRequestAttribute("AnnManRicRevocatiArresto", lAnnManRicRevocatiArresto);

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("AnnManConcessiReclusione " + lAnnManConcessiReclusione);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("AnnManConcessiArresto    " + lAnnManConcessiArresto);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("AnnManRevocatiReclusione " + lAnnManRevocatiReclusione);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("AnnManRevocatiArresto    " + lAnnManRevocatiArresto);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("AnnManRicConcessiReclusione " + lAnnManRicConcessiReclusione);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("AnnManRicConcessiArresto    " + lAnnManRicConcessiArresto);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("AnnManRicRevocatiReclusione " + lAnnManRicRevocatiReclusione);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("AnnManRicRevocatiArresto    " + lAnnManRicRevocatiArresto);

			// Campi che vengono semplicemente rigirati alla pagina di inserimento
			// hidden sulla pagina di visualizzazione
			setRequestAttribute("CodPosizioneGiuridica", PGMod2.getCodPosizioneGiuridica());
			setRequestAttribute("FlagAltraCausa",
					((FascicoloSiepModel) getSessionAttribute("fascicolo")).getFlagAltraCausa());
			setRequestAttribute("lFlagPage", this.getRequestStringParameter("lFlagPage"));
			setRequestAttribute("dataSistemaPerCalcoli", dataSistemaPerCalcoli);
			setRequestAttribute("dataScarcerazione", dataScarcerazione);

			setRequestAttribute("ultimaPenaValidata", lUltimaPenResVal);

			if (lFlagPage.equals("AMNI"))
				lPage = new String(f3b.web.IWebConstants.ROOT_DIR
						+ "/files/siap/siep/calcolopena/VediCalcoloPenaDecisioniGE.jsp");
			else
				lPage = new String(f3b.web.IWebConstants.ROOT_DIR
						+ "/files/siap/siep/calcolopena/VediNuovoCalcoloPenaNew.jsp");

		} // CHIUDE IF if ( !( lPenComplMod.getCodTipoPenaDetentiva().equals("03")
			// || lPenComplMod.getCodTipoPenaDetentiva().equals("04")
		else {
			// ========================================================================
			// Nel caso di ERGASTOLO non viene ricalcolata la pena residua, ma
			// semplicemente duplicata e agganciata all'evento in modo da averla sulle
			// stampe
			// ========================================================================
			PenaResiduaModel lPenRes = new PenaResiduaModel();

			lPenRes.setFasSieIdFascicoloSiep(lFascID);

			Date lDataFinePena = DateUtils.getDate(9999, 12, 31);

			if (lPenComplMod.getCodTipoPenaDetentiva().equals("03")) {
				lPenRes.setFlagErgastolo("S");
			} else if (lPenComplMod.getCodTipoPenaDetentiva().equals("04")) {
				lPenRes.setFlagErgastolo("D");
			}

			lPenRes.setDataInizio(lDataInizioPena);
			lPenRes.setDataFine(lDataFinePena);

			lPenRes.setDataInizioIsolamentoDiurno(lPenComplMod.getDataInizioIsolamentoDiurno());
			lPenRes.setDataFineIsolamentoDiurno(lPenComplMod.getDataFineIsolamentoDiurno());

			lPenRes.setNumGiorniIsolamentoDiurno(lPenComplMod.getNumGiorniIsolamentoDiurno());
			lPenRes.setNumMesiIsolamentoDiurno(lPenComplMod.getNumMesiIsolamentoDiurno());
			lPenRes.setNumAnniIsolamentoDiurno(lPenComplMod.getNumAnniIsolamentoDiurno());

			lPenRes.setFlagValidato("N");
			lPenRes.setDiesAQuo("S");

			// devo copiare il flag perchè altrimenti mi perdo l'informazione sullo
			// stato della pena (se sospesa, deve rimanere sospesa)
			if (lUltimaPenResVal != null) {
				lPenRes.setFlagPenaSospesa(lUltimaPenResVal.getFlagPenaSospesa());
			}

			// ========================================================================
			// Aggiorno la pena_residua aggiungendo l'id dell'evento
			// questo serve nel caso delle Decisioni del GE e Computi in cui è
			// necessario legare subito la pena residua al provvedimento (lo stesso
			// a cui è legata l'annotazione)
			// Nel caso di Richieste invece, la pena residua viene agganciata all'evento
			// prodotto in fase di stampa
			// ========================================================================
			String lFlagPage = getRequestStringParameter("lFlagPage");
			// if ( !lFlagPage.equals("GE") )
			// {
			BigDecimal lEveIdEvento = getRequestBigDecimalParameter(CAMPO_EVE_ID_EVENTO);
			lPenRes.setEveIdEvento(lEveIdEvento);

			// setRequestAttribute("IdEvento", lEveIdEvento);
			// }

			lPenRes.setCodOperatoreInserimento(getCodUtenteConnesso());
			lPenRes.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
			lPenRes.setDataInserimento(DateUtils.getSysDate());

			lPenResCtrl.ExInsertOrUpdatePenaResidua(lPenRes);

			setRequestAttribute("PenaResidua", lPenRes);
			setRequestAttribute("PenaComplessivaSentenza", lPenComplMod);
			setRequestAttribute("lFlagPage", this.getRequestStringParameter("lFlagPage"));

			// =======================================================================
			// Imposto la pagina di ritorno che nel caso di ergastolo è direttamente
			// la pagina di visualizzazione della pena e scelta delle stampe.
			// Nel caso non ergastolo invece si passa per la pagina intermedia di
			// conferma dei dati della pena (VediNuovoCalcoloPena.jsp)
			// =======================================================================

			if (lFlagPage.equals("GE")) {
				// Richieste al GE
				setRequestAttribute("lFlagPage", lFlagPage);
				lPage = f3b.web.IWebConstants.ROOT_DIR
						+ "/files/siap/siep/calcolopena/VediCalcoloPenaValidataRichiestaGE.jsp";
			} else if (lFlagPage.equals("A") || lFlagPage.equals("S") || lFlagPage.equals("D")) {
				// Rideterminazione pena A=Altro Titolo, S=Senza Titolo, D=Stesso Titolo
				setRequestAttribute("lFlagPage", lFlagPage);
				lPage = IWebConstants.ROOT_DIR
						+ "/files/siap/siep/calcolopena/VediCalcoloPenaValidataAnnotazioniComputo.jsp";
			} else if (lFlagPage.equals("AMNI") || lFlagPage.equals("DEPEN") || lFlagPage.equals("INCOST")) {
				// Decisioni del GE
				setRequestAttribute("lFlagPage", lFlagPage);
				lPage = IWebConstants.ROOT_DIR
						+ "/files/siap/siep/calcolopena/VediCalcoloPenaValidataAnnotazioni.jsp";
			}
		}

		return lPage;
	}

	/*****************************************************************************
	 * Action che effettua il calcolo e inserimento/aggiornamento della pena residua a seguito di un
	 * inserimento di: Decisione GE dei seguenti tipi: - Amnistia/Indulto - Depenalizzazione -
	 * Incostituzionalità Rideterminazione Pena: - presofferto - fungibilità (pena detentiva) - fungibilità
	 * (misura cautelare)
	 *
	 *
	 * @return String - jsp di visualizzazione dei risultati
	 * @throws Exception
	 * @deprecated sostituita a seguito del rework sul Calcolo della Pena
	 ************************************************************************** */
//	public String processRequestOLD() throws Exception {
//		// DA QUI COPIA E INCOLLA (e modifiche) della LOADCALCOLOPENA
//		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
//		// LogF3B.getLogger()
//		siesLogger.debug("ActCalcoloPenaComputo.processRequest(): start...");
//
//		String lPage = "";
//
//		BigDecimal lFascID = ((FascicoloSiepModel) getSessionAttribute("fascicolo")).getIdFascicoloSiep();
//		isFascicoloSiepDiCompetenza();
//
//		// ===========================
//		// Recupera pos Giuridica
//		// ===========================
//		IPosizioneGiuridica lPG = SIEPLookupRemote.getPosizioneGiuridicaRemote();
//		PosizioneGiuridicaModel PGMod = new PosizioneGiuridicaModel();
//		PGMod.setFasSieIdFascicoloSiep(lFascID);
//		PosizioneGiuridicaModel PGMod2 = lPG.ExRicercaPosizioneGiuridicaCorrente(PGMod);
//
//		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
//		// LogF3B.getLogger()
//		siesLogger.debug("POSIZIONE GIURIDICA : " + PGMod2.getCodPosizioneGiuridica());
//
//		// Utilizzati SOLO nel caso di ergastolo e solo se detenuto altra causa.
//		// In questo caso rappresentano il giorno
//		String GiornoInizio = new String("");
//		String MeseInizio = new String("");
//		String AnnoInizio = new String("");
//
//		String FlagAltraCausa = "N";
//
//		boolean libero = false; // mai usato
//		boolean detenutoQuestaCausa = false;
//		boolean detenutoAltraCausa = false;
//
//		if (PGMod2 == null)
//			throw new SIEPException(SIEPException.USER_MESSAGE,
//					"Rivedere Misure Cautelari o Posizione Giuridica !");
//
//		if (PGMod2.getCodPosizioneGiuridica().equals("07") // LIBERO
//				|| PGMod2.getCodPosizioneGiuridica().equals("10") // LIBERO
//				|| PGMod2.getCodPosizioneGiuridica().equals("16") // LIBERO IN DIFFERIMENTO PENA
//				|| PGMod2.getCodPosizioneGiuridica().equals("17")) // LIBERO IN DIFFERIMENTO PENA
//																	// (PROVVISORIA)
//		{
//			libero = true;
//		} else
//			detenutoQuestaCausa = true;
//
//		// ==========================================================================
//		// Se non detenuto per questa causa, verifico se detenuto per altra causa
//		// e in questo caso recupero (se presente) la data_scadenza da utilizzare
//		// come data inizio (+1gg) per il calcolo della pena residua
//		// ==========================================================================
//		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
//		// LogF3B.getLogger()
//		siesLogger.debug("ActCalcoloPenaComputo.processRequest(): libero = " + libero);
//		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
//		// LogF3B.getLogger()
//		siesLogger.debug("ActCalcoloPenaComputo.processRequest(): detenutoQuestaCausa = "
//				+ detenutoQuestaCausa);
//		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
//		// LogF3B.getLogger()
//		siesLogger
//				.debug("ActCalcoloPenaComputo.processRequest(): detenutoAltraCausa = " + detenutoAltraCausa);
//
//		if (libero) {
//			// Verifico se detenuto per altra causa, per impostare datainizio, GiornoInizio,
//			// MeseInizio, AnnoInizio alla data scadenza altra causa + 1gg
//			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
//			// LogF3B.getLogger()
//			siesLogger.debug("LIBERO");
//
//			FascicoloSiepModel lFascMod = new FascicoloSiepModel();
//			IFascicoloSiep lFS = SIEPLookupRemote.getFascicoloSiepRemote();
//			lFascMod = lFS.ExRicercaFascicoloByKey(lFascID);
//
//			if (lFascMod.getFlagAltraCausa() != null && lFascMod.getFlagAltraCausa().equals("S")) {
//				detenutoAltraCausa = true;
//				FlagAltraCausa = "S";
//
//				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
//				// LogF3B.getLogger()
//				siesLogger.debug("LIBERO con flag S");
//
//				AltraCausaModel lAcModel = new AltraCausaModel();
//				IAltraCausa lAC = SIEPLookupRemote.getAltraCausa();
//				lAcModel = lAC.ExRicercaAltraCausaByFascicolo(lFascID);
//
//				if (lAcModel.getDataDecorrenza() != null && lAcModel.getDataScadenza() != null) { // Attenzione
//																									// tale
//																									// data
//																									// non
//																									// viene
//																									// comunque
//																									// utilizzata
//																									// nel
//																									// calcolo
//																									// della
//																									// pena.
//																									// Viene
//																									// usata
//																									// solo
//																									// come
//																									// data
//																									// inizio
//																									// dell'ergastolo.
//																									// tra
//																									// l'altro
//																									// in modo
//																									// errato!!!
//																									// Viene
//																									// inoltre
//																									// azzerata
//																									// se
//																									// manca
//																									// la data
//																									// inizio
//																									// pena
//																									// sull'ultima
//																									// pena
//																									// validata
//					Date datainizio = null;
//					datainizio = DateUtils.getDayAfter(lAcModel.getDataScadenza());
//					GiornoInizio = DateUtils.getDayToString(datainizio);
//					MeseInizio = DateUtils.getMonthToString(datainizio);
//					AnnoInizio = DateUtils.getYearToString(datainizio);
//
//					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
//					// LogF3B.getLogger()
//					siesLogger.debug("Data inizio presa da AltraCausa ");
//				} else {
//					GiornoInizio = "-";
//
//					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
//					// LogF3B.getLogger()
//					siesLogger.debug("LIBERO con flag S e data inizio NULL");
//				}
//			}
//		}
//
//		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
//		// LogF3B.getLogger()
//		siesLogger.debug("ActCalcoloPenaComputo.processRequest(): libero = " + libero);
//		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
//		// LogF3B.getLogger()
//		siesLogger.debug("ActCalcoloPenaComputo.processRequest(): detenutoQuestaCausa = "
//				+ detenutoQuestaCausa);
//		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
//		// LogF3B.getLogger()
//		siesLogger
//				.debug("ActCalcoloPenaComputo.processRequest(): detenutoAltraCausa = " + detenutoAltraCausa);
//
//		// ===============================
//		// Recupero la PENA COMPLESSIVA
//		// ===============================
//		/**
//		 * TODO spostare ad inizio metodo, è un controllo vincolante per proseguire
//		 *
//		 */
//		// Indica alla jsp di visualizzare la sezione con le date e il bottone
//		// per la validazione della pena residua con la possibilità di modificare
//		// manualmente la data fine pena
//		String lVedoJSP = "N";
//
//		PenaComplessivaModel lPenMod = new PenaComplessivaModel();
//		lPenMod.setFasSieIdFascicoloSiep(lFascID);
//		IPenaComplessiva lPCon = SIEPLookupRemote.getPenaComplessivaRemote();
//		Vector lPComples = lPCon.ExRicercaPenaComplessivaNoError(lPenMod);
//		if (lPComples.size() == 0) {
//			// throw new F3BException(F3BException.USER_MESSAGE,"Pena Complessiva mancante");
//			RedirectTo lRedirigi = new RedirectTo();
//			lRedirigi.setPage(IWebConstants.PG_MAIN);
//			setRequestAttribute(IWebConstants.MESSAGE_TEXT, "Pena Complessiva mancante");
//			lRedirigi.setAction("siap.siep.fascicolo.action.ActLoadDettaglioFascicolo&"
//					+ ICostantiFascicoloSiep.CAMPO_ID_FASCICOLO_SIEP + "="
//					+ ((FascicoloSiepModel) getSessionAttribute("fascicolo")).getIdFascicoloSiep());
//			// ICostantiFascicoloSiep.CAMPO_AZIONE_CHIAMANTE + "=" + getClass().getName());
//			setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi);
//
//			return IWebConstants.PG_MESSAGE;
//		}
//
//		lPenMod = (PenaComplessivaModel) (lPComples.get(0));
//
//		// ==========================================================================
//		// CALCOLO PENA GIA' ESPIATA
//		// ==========================================================================
//		// Viene recuperata l'ultima PENA_RESIDUA VALIDATA e utilizzata la data
//		// inizio come data inizio della pena già espiata e data fine la data corrente
//		// (sysdate)
//		// Viene quindi calcolato il quantum
//		// n.b. questo è vero se il soggetto non è libero e detenuto per questa causa????
//		// La pena già espiata non viene utilizzato per il calcolo della pena residua,
//		// ma nel calcolo della fungibilità
//		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
//		// LogF3B.getLogger()
//		siesLogger.debug("CALCOLO PENA GIA ESPIATA ");
//
//		PenaResiduaModel lPenRes = new PenaResiduaModel();
//
//		PenaResiduaModel lUltimaPenRes = new PenaResiduaModel();
//		IPenaResidua IPenRes = SIEPLookupRemote.getPenaResiduaRemote();
//		// recupero ultima pena residua validata
//		lUltimaPenRes = IPenRes.ExRicercaPenaResiduaUltimaByDate(lFascID);
//
//		Date lDataFineReclusione = null;
//		Date lDataInizioArresto = null;
//		Date lDataFinePena = null;
//		Date lDataInizioPena = null;
//
//		CalendarUtil lCalCon = new CalendarUtil();
//		CalendarModel lPenaGiaEspiata = new CalendarModel();
//
//		if (lUltimaPenRes == null || lUltimaPenRes.getDataInizio() == null) {
//			GiornoInizio = "-";
//		} else {
//			lDataInizioPena = lUltimaPenRes.getDataInizio();
//			lDataFinePena = lUltimaPenRes.getDataFine();
//
//			lPenaGiaEspiata.setDataInizio(lUltimaPenRes.getDataInizio());
//			lPenaGiaEspiata.setDataFine(DateUtils.getSysDate());
//
//			lPenaGiaEspiata = lCalCon.CalcolaNumGiorniMesiAnni(lPenaGiaEspiata, false);
//		}
//
//		lPenaGiaEspiata = lCalCon.ricalcolaGAM(lPenaGiaEspiata);
//
//		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
//		// LogF3B.getLogger()
//		siesLogger.debug("ActCalcoloPenaComputo.processRequest(): lPenaGiaEspiata = " + lPenaGiaEspiata);
//
//		// -------------------------------------------------------
//		// FINE CALCOLO PENA GIA ESPIATA
//		// -------------------------------------------------------
//		/**
//		 * TODO posizionare opportunamente queste variabili
//		 *
//		 */
//		boolean ForzaFungibilita = false;
//		String ForzaSysdateManuale = "N";
//		String ChiedereValidazionePena = "S";
//
//		// ==================================
//
//		// ==============================================================================
//		// DA QUI COPIA E INCOLLA DEL CALCOLO DELLA PENA
//		// ==============================================================================
//		// ==========================================================================
//		// Controlla se il calcolo della pena deve essere AB INITIO o no
//		// Se il calcolo è abInizio vengono prese in considerazione:
//		// - PENA COMPLESSIVA IN SENTENZA
//		// - BENEFICI (concessi, revocati)
//		// - MISURE CAUTELARI (computabili)
//		// - ANNOTAZIONI MANUALI (a sistema)
//		// Se non ab inizio:
//		// - ultima pena residua validata
//		// - ANNOTAZIONI MANUALI (a sistema)
//		//
//		// ==========================================================================
//		ICalcoloPena lCalPen = SIEPLookupRemote.getCalcoloPenaRemote();
//		boolean lIsCalcoloPenaAbInitio = lCalPen.ExIsCalcoloPenaAbInizio(lFascID);
//
//		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
//		// LogF3B.getLogger()
//		siesLogger.debug("lIsCalcoloPenaAbInitio : " + lIsCalcoloPenaAbInitio);
//
//		// ==========================================================================
//		// Recupero i BENEFICI (concessi revocati)
//		// ==========================================================================
//		CalendarModel lBeneficiConcessiReclusione = new CalendarModel();
//		CalendarModel lBeneficiRevocatiReclusione = new CalendarModel();
//		CalendarModel lBeneficiConcessiArresto = new CalendarModel();
//		CalendarModel lBeneficiRevocatiArresto = new CalendarModel();
//
//		CalendarModel lBeneficiConcessiReclusioneJSP = new CalendarModel();
//		CalendarModel lBeneficiRevocatiReclusioneJSP = new CalendarModel();
//		CalendarModel lBeneficiConcessiArrestoJSP = new CalendarModel();
//		CalendarModel lBeneficiRevocatiArrestoJSP = new CalendarModel();
//
//		CalendarModel lMisCauComputabiliReclusione = new CalendarModel();
//		CalendarModel lMisCauComputabiliArresto = new CalendarModel();
//
//		// ==========================================================================
//		// Questa sezione determina la pena di partenza che dipende dal tipo di
//		// calcolo, abInizio i non abInizio
//		// se abInizio: Pena complessiva+benefici+misure cautelari
//		// se non abinizio: ultima pena residua validata
//		// ==========================================================================
//		if (!lIsCalcoloPenaAbInitio) {
//			// Se CALCOLO PENA NON AB INITIO, utilizzo come punto di partenza per il
//			// calcolo della pena residua, l'ultimo record pena residua VALIDATO (se esiste)
//			// invece della pena complessiva in sentenza.
//			// E comunque non considero i benefici e le misure cautelari
//			// CALCOLO NUOVO QUANTUM (gg,mm,aa) A PARTIRE DALLE DATE
//			if (lUltimaPenRes != null && lUltimaPenRes.getDataInizio() != null
//					&& lUltimaPenRes.getDataFine() != null) { // Ricalcolo la pena complessiva a partire
//																// dall'ultima pena_residua
//																// validata
//
//				// Prima di effettuare i calcoli normalizzo i quantum
//				PenaResiduaModel lPenaRicalcolata = null;
//				lPenaRicalcolata = PenaResiduaUtil.calcolaPenaNuovaDataFine(lUltimaPenRes.getDataFine(),
//						lUltimaPenRes, false); // true o false ??
//
//				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
//				// LogF3B.getLogger()
//				siesLogger.debug("Pena ricalcolata : " + lPenaRicalcolata);
//				// n.b. Sovrascrivo i dati della pana complessiva in sentenza
//
//				lPenMod.setNumAnniArresto(lPenaRicalcolata.getNumAnniArresto());
//				lPenMod.setNumMesiArresto(lPenaRicalcolata.getNumMesiArresto());
//				lPenMod.setNumGiorniArresto(lPenaRicalcolata.getNumGiorniArresto());
//
//				lPenMod.setImportoAmmenda(lPenaRicalcolata.getImportoAmmenda());
//
//				lPenMod.setNumAnniReclusione(lPenaRicalcolata.getNumAnniReclusione());
//				lPenMod.setNumMesiReclusione(lPenaRicalcolata.getNumMesiReclusione());
//				lPenMod.setNumGiorniReclusione(lPenaRicalcolata.getNumGiorniReclusione());
//
//				lPenMod.setImportoMulta(lPenaRicalcolata.getImportoMulta());
//			} else if (lUltimaPenRes != null && PGMod2.isLibero()) { // Recupero solo i quantum
//																		// Punto di partenza Ultima Pena NON
//																		// NORMALIZZATA in quanto soggetto
//																		// libero
//																		// [FT] - 03/08/2016 - MAC_LOG -
//																		// Utilizzo la variabile di istanza
//																		// siesLogger al posto di
//																		// LogF3B.getLogger()
//				siesLogger.debug("Sono Libero: punto di partenza la pena residua a sistema");
//
//				lPenMod.setNumAnniArresto(lUltimaPenRes.getNumAnniArresto());
//				lPenMod.setNumMesiArresto(lUltimaPenRes.getNumMesiArresto());
//				lPenMod.setNumGiorniArresto(lUltimaPenRes.getNumGiorniArresto());
//
//				lPenMod.setImportoAmmenda(lUltimaPenRes.getImportoAmmenda());
//
//				lPenMod.setNumAnniReclusione(lUltimaPenRes.getNumAnniReclusione());
//				lPenMod.setNumMesiReclusione(lUltimaPenRes.getNumMesiReclusione());
//				lPenMod.setNumGiorniReclusione(lUltimaPenRes.getNumGiorniReclusione());
//
//				lPenMod.setImportoMulta(lUltimaPenRes.getImportoMulta());
//			} else {
//				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
//				// LogF3B.getLogger()
//				siesLogger
//						.debug("ATTENZIONE!! Pena residua validata assente o mancante di date di non in espiazione");
//				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
//				// LogF3B.getLogger()
//				siesLogger.debug("==> Punto di partenza resta la Pena Complessiva");
//			}
//		} else { // calcolo ab inizio
//		// BENEFICI
//			lBeneficiConcessiReclusione = new CalendarModel(lCalPen.exGetBeneficiConcessiReclusione(lFascID));
//			lBeneficiRevocatiReclusione = new CalendarModel(lCalPen.exGetBeneficiRevocatiReclusione(lFascID));
//			lBeneficiConcessiArresto = new CalendarModel(lCalPen.exGetBeneficiConcessiArresto(lFascID));
//			lBeneficiRevocatiArresto = new CalendarModel(lCalPen.exGetBeneficiRevocatiArresto(lFascID));
//
//			lBeneficiConcessiReclusioneJSP = new CalendarModel(lBeneficiConcessiReclusione);
//			lBeneficiRevocatiReclusioneJSP = new CalendarModel(lBeneficiRevocatiReclusione);
//			lBeneficiConcessiArrestoJSP = new CalendarModel(lBeneficiConcessiArresto);
//			lBeneficiRevocatiArrestoJSP = new CalendarModel(lBeneficiRevocatiArresto);
//
//			// MISURE CAUTELARI
//			lMisCauComputabiliReclusione = new CalendarModel(
//					lCalPen.exGetMisureCautelariComputabiliReclusione(lFascID));
//			lMisCauComputabiliArresto = new CalendarModel(
//					lCalPen.exGetMisureCautelariComputabiliArresto(lFascID));
//		}
//
//		// !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
//		// FINE QUI TUTTO IDENTICO AD ActCalcolaPenaGE
//		// le operazioni effettuate nellal prima parte sono relative al recupero dei
//		// dati di partenza:
//		// - se ab inizio
//		// - quantum di partenza: (Pena complessiva+benefici+misure cautelari oppure ultima pena)
//		// - data inizio pena
//		// - calcolo pena già espiata
//		// - a sezione che segue recupera i dati
//		// !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
//
//		// ==========================================================================
//		// DA QUI LE EFFETTIVE ANNOTAZIONI MANUALI
//		// 002 - Indulto
//		// 003 - Amnistia
//		// 004 - Depenalizzazione
//		// 005 - Pena Espiata per lo Stesso Titolo (presofferto)
//		// 006 - Pena Espiata per Altro Titolo (fungibilità) misura cautelare
//		// 007 - Pena Espiata Senza Titolo (fungibilità) pena detentiva
//		// Calcolo il Quantum delle annotazini manuali dello stesso tipo di quelle
//		// inserite (CAMPO_COD_TIPO_ANNOTAZIONE)
//		// ==========================================================================
//		String lCodTipoAnnotazione = getRequestStringParameter(CAMPO_COD_TIPO_ANNOTAZIONE);
//
//		// Queste funzioni differiscono da quelle utilizzate nel caso di ActCalcolaPenaGE
//		// utilizzate nella rideterminazione pena con richiesta al GE. In quel caso
//		// le funzioni erano exGetAnnotazioniManualiConcessiAnticipazioneReclusione
//		// contenevano cioè la dizione Anticipazione. Tali funzioni recuperano tutte
//		// le annotazioni (ad eccezione di quelle inserite con richieste al GE).
//		// Se il calcolo è ab inizio vengono prese in considerazione anche quelle
//		// validate altrimenti solo quelle non validate dello stesso tipo di quelle
//		// correnti.
//		CalendarModel lAnnManConcessiReclusione = new CalendarModel(
//				lCalPen.exGetAnnotazioniManualiConcessiReclusione(lFascID, lCodTipoAnnotazione,
//						lIsCalcoloPenaAbInitio));
//		CalendarModel lAnnManRevocatiReclusione = new CalendarModel(
//				lCalPen.exGetAnnotazioniManualiRevocatiReclusione(lFascID, lCodTipoAnnotazione,
//						lIsCalcoloPenaAbInitio));
//		CalendarModel lAnnManConcessiArresto = new CalendarModel(
//				lCalPen.exGetAnnotazioniManualiConcessiArresto(lFascID, lCodTipoAnnotazione,
//						lIsCalcoloPenaAbInitio));
//		CalendarModel lAnnManRevocatiArresto = new CalendarModel(
//				lCalPen.exGetAnnotazioniManualiRevocatiArresto(lFascID, lCodTipoAnnotazione,
//						lIsCalcoloPenaAbInitio));
//
//		// SOLO se il calcolo è abInizio, recupero anche le altre annotazioni
//		// inserite con anticipazione degli effetti, che devono essere computate nel
//		// quantum finale, altrimenti sono già state computate sulla pena residua
//		if (lIsCalcoloPenaAbInitio) {
//			CalendarModel lAnnManConcessiReclusioneAnt = new CalendarModel(
//					lCalPen.exGetAnnotazioniManualiConcessiAnticipazioneReclusione(lFascID,
//							lCodTipoAnnotazione, lIsCalcoloPenaAbInitio));
//			CalendarModel lAnnManRevocatiReclusioneAnt = new CalendarModel(
//					lCalPen.exGetAnnotazioniManualiRevocatiAnticipazioneReclusione(lFascID,
//							lCodTipoAnnotazione, lIsCalcoloPenaAbInitio));
//			CalendarModel lAnnManConcessiArrestoAnt = new CalendarModel(
//					lCalPen.exGetAnnotazioniManualiConcessiAnticipazioneArresto(lFascID, lCodTipoAnnotazione,
//							lIsCalcoloPenaAbInitio));
//			CalendarModel lAnnManRevocatiArrestoAnt = new CalendarModel(
//					lCalPen.exGetAnnotazioniManualiRevocatiAnticipazioneArresto(lFascID, lCodTipoAnnotazione,
//							lIsCalcoloPenaAbInitio));
//
//			// Sommo il tutto
//			lAnnManConcessiReclusione = new CalendarModel(lCalCon.sommaGiornieValute(
//					lAnnManConcessiReclusione, lAnnManConcessiReclusioneAnt));
//			lAnnManRevocatiReclusione = new CalendarModel(lCalCon.sommaGiornieValute(
//					lAnnManRevocatiReclusione, lAnnManRevocatiReclusioneAnt));
//			lAnnManConcessiArresto = new CalendarModel(lCalCon.sommaGiornieValute(lAnnManConcessiArresto,
//					lAnnManConcessiArrestoAnt));
//			lAnnManRevocatiArresto = new CalendarModel(lCalCon.sommaGiornieValute(lAnnManRevocatiArresto,
//					lAnnManRevocatiArrestoAnt));
//		}
//
//		// CalendarModel lAnnManTotaliArresto = new
//		// CalendarModel(lCalCon.sommaGiornieValute(lAnnManConcessiArresto, lAnnManRevocatiArresto));
//		// CalendarModel lAnnManTotaliReclusione = new
//		// CalendarModel(lCalCon.sommaGiornieValute(lAnnManConcessiReclusione, lAnnManRevocatiReclusione));
//
//		CalendarModel lAnnManConcessiReclusioneJSP = new CalendarModel(lAnnManConcessiReclusione);
//		CalendarModel lAnnManRevocatiReclusioneJSP = new CalendarModel(lAnnManRevocatiReclusione);
//		CalendarModel lAnnManConcessiArrestoJSP = new CalendarModel(lAnnManConcessiArresto);
//		CalendarModel lAnnManRevocatiArrestoJSP = new CalendarModel(lAnnManRevocatiArresto);
//
//		// Sommo Annotazioni Manuali ai Benefici per poterli passare in una sola
//		// variabile al metodo di calcolo della Pena Residua
//		// n.b. if not abInizio i benefici sono non valorizzati, quindi nulli
//		lBeneficiConcessiReclusione = new CalendarModel(lCalCon.sommaGiornieValute(
//				lBeneficiConcessiReclusione, lAnnManConcessiReclusione));
//		lBeneficiRevocatiReclusione = new CalendarModel(lCalCon.sommaGiornieValute(
//				lBeneficiRevocatiReclusione, lAnnManRevocatiReclusione));
//		lBeneficiConcessiArresto = new CalendarModel(lCalCon.sommaGiornieValute(lBeneficiConcessiArresto,
//				lAnnManConcessiArresto));
//		lBeneficiRevocatiArresto = new CalendarModel(lCalCon.sommaGiornieValute(lBeneficiRevocatiArresto,
//				lAnnManRevocatiArresto));
//
//		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
//		// LogF3B.getLogger()
//		siesLogger.debug("--------------------------------------");
//		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
//		// LogF3B.getLogger()
//		siesLogger
//				.debug("Benefici Arresto Concessi : " + lBeneficiConcessiArrestoJSP.getImportoAmmenda()
//						+ "----" + lBeneficiConcessiArrestoJSP.getNumAnni() + "/"
//						+ lBeneficiConcessiArrestoJSP.getNumMesi() + "/"
//						+ lBeneficiConcessiArrestoJSP.getNumGiorni());
//		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
//		// LogF3B.getLogger()
//		siesLogger
//				.debug("Benefici Arresto Revocati : " + lBeneficiRevocatiArrestoJSP.getImportoAmmenda()
//						+ "----" + lBeneficiRevocatiArrestoJSP.getNumAnni() + "/"
//						+ lBeneficiRevocatiArrestoJSP.getNumMesi() + "/"
//						+ lBeneficiRevocatiArrestoJSP.getNumGiorni());
//		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
//		// LogF3B.getLogger()
//		siesLogger.debug("--------------------------------------");
//		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
//		// LogF3B.getLogger()
//		siesLogger.debug("--------------------------------------");
//		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
//		// LogF3B.getLogger()
//		siesLogger
//				.debug("Benefici Reclusione Concessi : " + lBeneficiConcessiReclusione.getImportoAmmenda()
//						+ "----" + lBeneficiConcessiReclusione.getNumAnni() + "/"
//						+ lBeneficiConcessiReclusione.getNumMesi() + "/"
//						+ lBeneficiConcessiReclusione.getNumGiorni());
//		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
//		// LogF3B.getLogger()
//		siesLogger
//				.debug("Benefici Reclusione Revocati : " + lBeneficiRevocatiReclusione.getImportoAmmenda()
//						+ "----" + lBeneficiRevocatiReclusione.getNumAnni() + "/"
//						+ lBeneficiRevocatiReclusione.getNumMesi() + "/"
//						+ lBeneficiRevocatiReclusione.getNumGiorni());
//		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
//		// LogF3B.getLogger()
//		siesLogger.debug("--------------------------------------");
//
//		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
//		// LogF3B.getLogger()
//		siesLogger.debug("--------------------------------------");
//		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
//		// LogF3B.getLogger()
//		siesLogger.debug("Totali Arresto Concessi : " + lBeneficiConcessiArresto.getImportoAmmenda() + "----"
//				+ lBeneficiConcessiArresto.getNumAnni() + "/" + lBeneficiConcessiArresto.getNumMesi() + "/"
//				+ lBeneficiConcessiArresto.getNumGiorni());
//		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
//		// LogF3B.getLogger()
//		siesLogger.debug("Totali Arresto Revocati : " + lBeneficiRevocatiArresto.getImportoAmmenda() + "----"
//				+ lBeneficiRevocatiArresto.getNumAnni() + "/" + lBeneficiRevocatiArresto.getNumMesi() + "/"
//				+ lBeneficiRevocatiArresto.getNumGiorni());
//		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
//		// LogF3B.getLogger()
//		siesLogger.debug("--------------------------------------");
//		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
//		// LogF3B.getLogger()
//		siesLogger.debug("--------------------------------------");
//		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
//		// LogF3B.getLogger()
//		siesLogger
//				.debug("Totali Reclusione Concessi : " + lBeneficiConcessiReclusione.getImportoMulta()
//						+ "----" + lBeneficiConcessiReclusione.getNumAnni() + "/"
//						+ lBeneficiConcessiReclusione.getNumMesi() + "/"
//						+ lBeneficiConcessiReclusione.getNumGiorni());
//		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
//		// LogF3B.getLogger()
//		siesLogger
//				.debug("Totali Reclusione Revocati : " + lBeneficiRevocatiReclusione.getImportoMulta()
//						+ "----" + lBeneficiRevocatiReclusione.getNumAnni() + "/"
//						+ lBeneficiRevocatiReclusione.getNumMesi() + "/"
//						+ lBeneficiRevocatiReclusione.getNumGiorni());
//		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
//		// LogF3B.getLogger()
//		siesLogger.debug("--------------------------------------");
//		// -------------------------------------------------------
//		// FINE ANNOTAZIONI MANUALI
//		// -------------------------------------------------------
//
//		// per vedere se arrivo da computo altro titolo stesso o senza
//		setRequestAttribute("lFlagPage", this.getRequestStringParameter("lFlagPage"));
//
//		if (!(lPenMod.getCodTipoPenaDetentiva().equals("03") || lPenMod.getCodTipoPenaDetentiva()
//				.equals("04"))) { // not ergastolo
//									// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza
//									// siesLogger al posto di LogF3B.getLogger()
//			siesLogger.debug("FORZAFUNG:" + ForzaFungibilita);
//			String Segnalazione = "N";
//
//			// =======================================================
//			// Aggiorno/inseriscio il quantum pena residua + importi
//			// =======================================================
//			PenaResiduaModel lPenaComplessiva = null;
//
//			// ========================================================================
//			// n.b. lPenMod contiene la pena complessiva in sentenza o l'ultima
//			// pena validata
//			// lBeneficiXXX contiene i Benefici+Annotazioni manuali
//			// lMisCauXXX contiene le Misure Cautelari
//			// I calcoli e inserimenti vengono fatti nello stesso identico modo del
//			// primo calcolo della pena con la sola differenza del CONTENUTO delle
//			// variabili passate in input alla funzione
//			// I dati calcolati potrebbero essere <0
//			// ========================================================================
//			lPenaComplessiva = lCalPen.exCalcolaQuantumPenaComplessivaNuovo(
//					lFascID,
//					lPenMod, // pena complessiva in sentenza o (Ultima pena Residua Validata)
//					lBeneficiConcessiReclusione, lBeneficiRevocatiReclusione, lBeneficiConcessiArresto,
//					lBeneficiRevocatiArresto, lMisCauComputabiliReclusione, lMisCauComputabiliArresto,
//					getCodUtenteConnesso(), getCodUfficioUtenteConnesso(), ForzaFungibilita, // sempre false
//					lPenaGiaEspiata); // non usato se ForzaFungibilita = false, quindi non usato
//
//			// Il quantum calcolato potrebbe addirittura essere negativo in quanto
//			// il genere le annotazioni manuali sottraggono quindi se ho una pena
//			// residua di 20 giorni ma un presofferto di 25 in quantum calcolato
//			// dovrebbe essere negativo
//
//			// ========================================================================
//			// Calcolo il quantum TOTALE pena complessiva da espiare(Reclusione+Arresti)
//			// ========================================================================
//			CalendarModel lTotResiduaCalen = new CalendarModel();
//			lTotResiduaCalen.setNumAnni(lPenaComplessiva.getNumAnniArresto().add(
//					lPenaComplessiva.getNumAnniReclusione()));
//			lTotResiduaCalen.setNumMesi(lPenaComplessiva.getNumMesiArresto().add(
//					lPenaComplessiva.getNumMesiReclusione()));
//			lTotResiduaCalen.setNumGiorni(lPenaComplessiva.getNumGiorniArresto().add(
//					lPenaComplessiva.getNumGiorniReclusione()));
//
//			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
//			// LogF3B.getLogger()
//			siesLogger.debug("Quantum totale residuo da espiare calcolato lTotResiduaCalen = "
//					+ lTotResiduaCalen);
//
//			//
//			// Se il totale da espiare è maggiore della pena già espiata (eventualmente =0)
//			// o il totale da espiare vale 0...
//			/**
//			 * TODO ForzaFungibilita è già false, non viene mai impostata a true!!! Inoltre che mi rappresenta
//			 * questa condizione?
//			 *
//			 */
//			if (!detenutoAltraCausa
//					&& (lCalCon.isGreater(lTotResiduaCalen, lPenaGiaEspiata) || lCalCon
//							.isZero(lTotResiduaCalen))) {
//				ForzaFungibilita = false;
//				// ChiedereValidazionePena="N";
//			}
//
//			String vedoDataIntermedia = "S";
//			// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
//			// LogF3B.getLogger()
//			// siesLogger.debug("GIORNO INIZIO : "+getRequestStringParameter("GiornoInizio") );
//
//			// ========================================================================
//			// Se la pena è in espiazione (lDataInizioPena<>null) calcolo le date di
//			// espiazione della pena residua:
//			// DataFineReclusione, DataInizioArresti, Data Fine Pena
//			//
//			// n.b. lDataInizioPena=data inizio ultima pena residua VALIDATA
//			// Quindi aggiorno il record PENA_RESIDUA con le date
//			// ========================================
//			if (lDataInizioPena != null) {
//				/*
//				 * Attenzione! il quantum calcolato potrebbere essere negativo (arresti) in questo caso la
//				 * funzione exCalcolaDataFinePena restituisce un vettore vuoto (size=0)
//				 */
//				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
//				// LogF3B.getLogger()
//				siesLogger.debug("Calcolo della date con data inizio : " + lDataInizioPena);
//				Vector lDateFine = lCalPen.exCalcolaDataFinePena(lDataInizioPena, lPenaComplessiva, true);
//
//				lVedoJSP = "S";
//				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
//				// LogF3B.getLogger()
//				siesLogger.debug("lDateFine.size() : " + lDateFine.size());
//
//				if (lDateFine.size() == 1) { // solo Reclusione o Arresti: ho quindi solo data fine
//					vedoDataIntermedia = "N";
//
//					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
//					// LogF3B.getLogger()
//					siesLogger.debug("USCITO DAL CALCOLO DELLA DATA FINE PENA CON : " + lDateFine.get(0)
//							+ " con " + lDateFine.size() + " elementi");
//
//					// verifico se trattasi di Reclusione o arresti
//					boolean cond = lPenaComplessiva.getNumAnniArresto().equals(null)
//							|| lPenaComplessiva.getNumAnniArresto().intValue() == 0;
//					cond = cond
//							&& (lPenaComplessiva.getNumMesiArresto().equals(null) || lPenaComplessiva
//									.getNumMesiArresto().intValue() == 0);
//					cond = cond
//							&& (lPenaComplessiva.getNumGiorniArresto().equals(null) || lPenaComplessiva
//									.getNumGiorniArresto().intValue() == 0);
//
//					if (!cond) // solo arresti
//						lDataInizioArresto = lDataInizioPena;
//
//					lDataFinePena = (Date) lDateFine.get(0);
//				}
//
//				/**
//				 * TODO n.b. lDataFinePena potrebbe essere valorizzato con la data fine pena dell'ultimo
//				 * record pena residua validato (vedi precedente calcolo pena già espiata) Se invece non
//				 * esiste un record data fine validato e lDateFine.size()==2, lDataFinePena=null per cui non
//				 * entro nel nell'if e lascio le date fine a null Se invece lDataFinePena dell'ultimo record
//				 * Validato>oggi
//				 */
//
//				// Assegno datafine reclusione e data inizio arresti e data fine pena
//				// con i valori calcolati, solo se la data fine pena dell'ultimo record
//				// pena residua VALIDATO (lDataFinePena) risulta successiva alla data odierna
//				// altrimenti ????? come mi comporto????
//				// n.b. lDataFinePena potrebbe essere stato valorizzao al passo precedente
//				// ma in questo caso lDateFine.size()==1 per cui non andrò avanti
//				/**
//				 * TODO cosa mi rappresenta questa condizione? Se sono in questo if di calcolo date vuol dire
//				 * che lDataInizioPena!=null=data inizio ultima pena validata quindi dovrebbe essere !=null
//				 * anche lDataFinePena Se ho sia Reclusione che Arresto, le date calcolate le prendo in
//				 * considerazione solo se le data fine pena prevista (validata)> di Oggi Perchè???
//				 * lDataFinePena non è quella ricalcolata con i nuovi quantum, ma quella prima del ricalcolo.
//				 * Questa condizione dice che: se la pena, PRIMA della concessione/revoca benefici, era già
//				 * scaduta (finepena<sysdate) non aggiorno le date. Per quale motivo???? Atrimenti -
//				 * lDataFineReclusione = resta a null - lDataInizioArresto = resta a null - lDataFinePena:
//				 * resta valorizzata a quanto già previsto nella PR validata
//				 */
//				// ======================================================================
//				// Se è presente sia Reclusione che Arresto (lDateFine.size() == 2)
//				// aggiorno le date intermedie solo se
//				// ======================================================================
//				if (lDataFinePena != null && lDataFinePena.after(DateUtils.getSysDate())) {
//					if (lDateFine.size() == 2) {
//						lDataFineReclusione = (Date) lDateFine.get(0);
//						lDataInizioArresto = DateUtils.getDayAfter(lDataFineReclusione);
//						lDataFinePena = (Date) lDateFine.get(1);
//					}
//				}
//
//				if (lDateFine.size() == 0) {
//					Segnalazione = "S";
//					// Indica alla jsp che il quantum pena calcolato risulta minore o
//					// uguale a zero, vale a dire che il soggetto andrebbe rimesso in libertà (?)
//					// n.b. data inizio...
//				}
//
//				// Se ho le date intermedie (quantum positivo) aggiorno il record pena
//				// residua
//				// Attenzione nel caso lDateFine.size() == 2 potrei non aver impostato
//				// lDataFineReclusione e lDataInizioArresto
//				// n.b. non aggiorno la data fine, ma la data fine presunta
//				if (lDateFine.size() != 0 && !ForzaFungibilita) {
//					lPenaComplessiva.setDataInizio(lDataInizioPena);
//					lPenaComplessiva.setDataFinePresunta(lDataFinePena);
//					lPenaComplessiva.setDataInizioArresto(lDataInizioArresto);
//					lPenaComplessiva.setDataFineReclusione(lDataFineReclusione);
//
//					lPenaComplessiva.setDiesAQuo("S");
//
//					lPenaComplessiva.setCodOperatoreInserimento(getCodUtenteConnesso());
//					lPenaComplessiva.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
//					lPenaComplessiva.setDataInserimento(DateUtils.getSysDate());
//
//					IPenaResidua lPPres = SIEPLookupRemote.getPenaResiduaRemote();
//					lPPres.ExModificaPenaResidua(lPenaComplessiva);
//				} else {
//					// In questo else entra solo se lDateFine.size()==0. In questo caso
//					// vuol dire che la funzione exCalcolaDataFinePena ha restituito 0.
//					// Cioè il quantum di pena calcolato (lPenaComplessiva) risulta
//					// essere negativo o nullo
//					// Imposto comunque data inizio e data fine, n.b. in questo caso non
//					// la data fine presunta che resta a null
//
//					lPenaComplessiva.setDataInizio(lDataInizioPena);
//					lPenaComplessiva.setDataFine(lDataFinePena); // è la data fine della PR Validata iniziale
//
//					lPenaComplessiva.setDiesAQuo("S");
//
//					lPenaComplessiva.setCodOperatoreInserimento(getCodUtenteConnesso());
//					lPenaComplessiva.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
//					lPenaComplessiva.setDataInserimento(DateUtils.getSysDate());
//
//					IPenaResidua lPPres = SIEPLookupRemote.getPenaResiduaRemote();
//					// Aggiorno il DB solo se il soggetto non è da scarcerare
//					if (lDataFinePena.after(DateUtils.getSysDate()))
//						lPPres.ExModificaPenaResidua(lPenaComplessiva);
//				}
//			}
//
//			// ========================================================================
//			// CALCOLO FUNGIBILITA'
//			// verifico se il soggetto ha espiato più di quanto dovuto, in questo caso
//			// la pena in eccesso espiata viene registrata come fungibilità
//			// ========================================================================
//			CalendarModel lDiff = new CalendarModel(); // differenza tra la pena espiata e quella da espiare.
//														// SE >0 allora fungibilità
//
//			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
//			// LogF3B.getLogger()
//			siesLogger.debug("---------------------------------------------------------------");
//			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
//			// LogF3B.getLogger()
//			siesLogger.debug("lTotResiduaCalen: AA:" + lTotResiduaCalen.getNumAnni() + "MM:"
//					+ lTotResiduaCalen.getNumMesi() + "GG:" + lTotResiduaCalen.getNumGiorni());
//			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
//			// LogF3B.getLogger()
//			siesLogger.debug("lPenaGiaEspiata: AA:" + lPenaGiaEspiata.getNumAnni() + "MM:"
//					+ lPenaGiaEspiata.getNumMesi() + "GG:" + lPenaGiaEspiata.getNumGiorni());
//			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
//			// LogF3B.getLogger()
//			siesLogger.debug("FlagAltraCausa: " + FlagAltraCausa);
//			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
//			// LogF3B.getLogger()
//			siesLogger.debug("---------------------------------------------------------------");
//
//			// if (FlagAltraCausa.equals("N") && lCalCon.isZero(lFungCalen))
//
//			// ========================================================================
//			// Se il quantum pena residua (reclusione+arresti) è nullo
//			// la fungibilità coincide con la pena già espiata (eventualmente 0 se
//			// il soggetto non è in espiazione) e la data fine = data inizio
//			// ========================================================================
//			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
//			// LogF3B.getLogger()
//			siesLogger.debug("ActCalcoloPenaComputo.processRequest(): Fungibilità...");
//			if (lCalCon.isZero(lTotResiduaCalen)) {
//				// CalendarModel tmp = new CalendarModel();
//				lDiff = new CalendarModel();
//
//				// Setto la data fine = data inizio essendo il quantum = 0
//				if (lUltimaPenRes != null && lUltimaPenRes.getDataInizio() != null) {
//					lDataFinePena = lUltimaPenRes.getDataInizio();
//				}
//
//				// Nel caso di nuova Pena Residua a 0 la Fungibilità coincide con
//				// la pena già espiata
//				lDiff = new CalendarModel(lPenaGiaEspiata);
//
//				ForzaSysdateManuale = "S";
//			} else // >0 o <0
//			{
//				// ======================================================================
//				//
//				// ======================================================================
//				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
//				// LogF3B.getLogger()
//				siesLogger.debug("lTotResiduaCalen = " + lTotResiduaCalen);
//				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
//				// LogF3B.getLogger()
//				siesLogger.debug("lPenaGiaEspiata = " + lPenaGiaEspiata);
//				// n.b. lTotResiduaCalen potrebbe essere <0
//				// leggasi se lTotResiduaCalen<=lPenaGiaEspiata anche quindi nel caso di
//				// lTotResiduaCalen<0
//				if (!lCalCon.isGreater(lTotResiduaCalen, lPenaGiaEspiata)) // || ForzaFungibilita))
//				{ // Nuova Pena da Espiare < Pena già espiata
//					// Potrei essere non detenuto (lPenaGiaEspiata=0) e lTotResiduaCalen<0
//					// oppure detenuto e lTotResiduaCalen<=lPenaGiaEspiata
//					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
//					// LogF3B.getLogger()
//					siesLogger.debug("calcolo Fungibilità...");
//					ForzaSysdateManuale = "S";
//
//					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
//					// LogF3B.getLogger()
//					siesLogger.debug("lDataInizioPena = " + lDataInizioPena);
//					if (lDataInizioPena != null) { //
//						if (detenutoQuestaCausa) // non libero (7,10,16,17)
//						{
//							// ho una data inizio quindi ho pure una data di fine (????Davvero?????)
//							if (lDataFinePena == null) { // lDataFinePena == data fine pena validata se
//															// presente, oppure data fine
//															// pena ricalcolata se quantum > 0
//															// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la
//															// variabile di istanza siesLogger al posto di
//															// LogF3B.getLogger()
//								siesLogger.debug("ENTRO SU DATA FINE A NULL");
//
//								if (lPenaGiaEspiata.getDataFine() != null
//										&& lPenaGiaEspiata.getDataFine().before(DateUtils.getSysDate())) { // n.b.
//																											// lPenaGiaEspiata.getDataFine()
//																											// =
//																											// data
//																											// fine
//																											// pena
//																											// validata
//																											// se
//																											// esiste
//									CalendarModel tmp = new CalendarModel();
//									tmp.setDataFine(DateUtils.getSysDate());
//									tmp.setDataInizio(lPenaGiaEspiata.getDataFine());
//									lDiff = lCalCon.CalcolaNumGiorniMesiAnni(tmp);
//								}
//								// else????
//							} else {
//								// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger
//								// al posto di LogF3B.getLogger()
//								siesLogger.debug("ENTRO SU DATA FINE NON A NULL!!!!!!");
//
//								if (lDataFinePena.before(DateUtils.getSysDate())) {
//									CalendarModel tmp = new CalendarModel();
//									tmp.setDataFine(DateUtils.getSysDate());
//									tmp.setDataInizio(lDataFinePena);
//									lDiff = lCalCon.CalcolaNumGiorniMesiAnni(tmp);
//								}
//							}
//						} else if (FlagAltraCausa.equals("S")) { // Libero ma detenuto altra causa
//							if (lDataFinePena.before(DateUtils.getSysDate())) {
//								CalendarModel tmp = new CalendarModel();
//								tmp.setDataFine(DateUtils.getSysDate());
//								tmp.setDataInizio(lDataFinePena);
//								lDiff = lCalCon.CalcolaNumGiorniMesiAnni(tmp);
//							}
//						} else { // Libero e basta
//							if (lDataFinePena.before(DateUtils.getSysDate())) {
//								lDiff = lCalCon.ricalcolaGAM(lCalCon.BeneficisottraiGiornieValute(
//										lTotResiduaCalen, lPenaGiaEspiata));
//							}
//						}
//					} else { // lDataInizioPena == null
//						/**
//						 * TODO se lDataInizioPena == null lo è necessariamente anche lPenaGiaEspiata per cui
//						 * lTotResiduaCalen<0 e la fungibilità è pari al quantum calcolato La
//						 * BeneficisottraiGiornieValute cambia di segno i quantum per cui lDiff diventa >0
//						 * Questa condizione equivale a lDiff = abs(lTotResiduaCalen);
//						 */
//						lDiff = lCalCon.ricalcolaGAM(lCalCon.BeneficisottraiGiornieValute(lTotResiduaCalen,
//								lPenaGiaEspiata));
//					}
//				}
//			}
//
//			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
//			// LogF3B.getLogger()
//			siesLogger.debug("fungibilità calcolata:lDiff = " + lDiff);
//			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
//			// LogF3B.getLogger()
//			siesLogger.debug("---------------------------------------------------------------");
//			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
//			// LogF3B.getLogger()
//			siesLogger.debug(" FUNGIBILITA : AA:" + lDiff.getNumAnni() + "MM:" + lDiff.getNumMesi() + "GG:"
//					+ lDiff.getNumGiorni());
//			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
//			// LogF3B.getLogger()
//			siesLogger.debug("---------------------------------------------------------------");
//
//			// se libero (01,07,16,17) ma nemmeno detenuto per altra causa e il quantum
//			// rideterminato vale 0 allora la fungibilità è pari alla pena espiata
//			// Attenzione la condizione è ambigua (!) perchè da un lato testa la
//			// posizione giuridica, dall'altro lo stato della pena in quanto la pena
//			// già espiata viene calcolata come differenza tra la data inizio ultima
//			// pena residua validata e la data di sistema.
//			if (lCalCon.isZero(lTotResiduaCalen) && !detenutoQuestaCausa && FlagAltraCausa.equals("N"))
//				lDiff = new CalendarModel(lPenaGiaEspiata);
//
//			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
//			// LogF3B.getLogger()
//			siesLogger.debug("fungibilità calcolata:lDiff = " + lDiff);
//			// =========================================
//			// Inserisce la FUNGIBILITA' se presente
//			// =========================================
//			FungibilitaModel lFunMod = new FungibilitaModel();
//			if (!lCalCon.isZero(lDiff)) {
//				lFunMod.setFasSieIdFascicoloSiep(lFascID);
//				lFunMod.setCodTipoFungibilita("02"); // 02=PENA ESPIATA IN ECCESSO ....Cg_ref_codes 24/10/2003
//				lFunMod.setNumAnni(new BigDecimal(lDiff.getNumAnni() + ""));
//				lFunMod.setNumMesi(new BigDecimal(lDiff.getNumMesi() + ""));
//				lFunMod.setNumGiorni(new BigDecimal(lDiff.getNumGiorni() + ""));
//				lFunMod.setFlagValidato("N");
//
//				lFunMod.setCodOperatoreInserimento(getCodUtenteConnesso());
//				lFunMod.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
//				lFunMod.setDataInserimento(DateUtils.getSysDate());
//
//				BigDecimal lEveIdEvento = getRequestBigDecimalParameter(CAMPO_EVE_ID_EVENTO);
//				lFunMod.setEveIdEvento(lEveIdEvento);
//
//				IFungibilita iFun = SIEPLookupRemote.getFungibilitaRemote();
//				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
//				// LogF3B.getLogger()
//				siesLogger.debug("inserisco fungibilità = " + lFunMod);
//				lFunMod = iFun.ExInserisciFungibilita(lFunMod);
//
//				// Modifica la data fine pena se detenuto altra causa
//				if (detenutoAltraCausa)
//					lDataFinePena = DateUtils.getSysDate();
//			}
//
//			// ============================================
//			// FINE CALCOLO E INSERIMENTO FUNGIBILITA'
//			// ============================================
//
//			// ========================================================================
//			// Recupero le annotazioni manuali appena computate e le passo alla jsp
//			// Attenzione questa sezione differisce dalla ActCalcoloPenaGE.
//			// Vengono recuperate le annotazioni relative ai computi
//			// ========================================================================
//			IAnnotazioneManuale lCtrlAnnMan = SIEPLookupRemote.getAnnotazioneManualeRemote();
//			Vector lListAnnMan = lCtrlAnnMan.ExRicercaAnnotazioniManualiByIdFascicoloNonRichiesteTipoAnn(
//					lFascID, lCodTipoAnnotazione, "N");
//			setRequestAttribute("ListaAnnotazioni", lListAnnMan);
//
//			// Se il calcolo non è AbInitio non deve visualizzare la Pena Complessiva
//
//			setRequestAttribute("lIsCalcoloPenaAbInitio", new Boolean(lIsCalcoloPenaAbInitio));
//
//			setRequestAttribute("ChiedereValidazionePena", ChiedereValidazionePena); // fisso a S
//			setRequestAttribute("BeneficiConcessiArresto", lCalCon.toJsp(lBeneficiConcessiArrestoJSP));
//			setRequestAttribute("BeneficiRevocatiArresto", lCalCon.toJsp(lBeneficiRevocatiArrestoJSP));
//			setRequestAttribute("BeneficiConcessiReclusione", lCalCon.toJsp(lBeneficiConcessiReclusioneJSP));
//			setRequestAttribute("BeneficiRevocatiReclusione", lCalCon.toJsp(lBeneficiRevocatiReclusioneJSP));
//			setRequestAttribute("MisCauComputabiliReclusione", lCalCon.toJsp(lMisCauComputabiliReclusione));
//			setRequestAttribute("MisCauComputabiliArresto", lCalCon.toJsp(lMisCauComputabiliArresto));
//			setRequestAttribute("AnnManConcessiReclusione", lCalCon.toJsp(lAnnManConcessiReclusioneJSP));
//			setRequestAttribute("AnnManRevocatiReclusione", lCalCon.toJsp(lAnnManRevocatiReclusioneJSP));
//			setRequestAttribute("AnnManConcessiArresto", lCalCon.toJsp(lAnnManConcessiArrestoJSP));
//			setRequestAttribute("AnnManRevocatiArresto", lCalCon.toJsp(lAnnManRevocatiArrestoJSP));
//
//			// setRequestAttribute("BeneficiTotaliArresto",lCalCon.toJsp(lBeneficiTotaliArresto));
//			// setRequestAttribute("BeneficiTotaliReclusione", lCalCon.toJsp(lBeneficiTotaliReclusione));
//			// setRequestAttribute("MisCauNonComputabiliReclusione",
//			// lCalCon.toJsp(lMisCauNonComputabiliReclusione));
//			// setRequestAttribute("MisCauNonComputabiliArresto",
//			// lCalCon.toJsp(lMisCauNonComputabiliArresto));
//			// setRequestAttribute("EspiatoErgastolo", lEspiatoErgastolo);
//
//			setRequestAttribute("IdFungibilita", lFunMod.getIdFungibilita() + ""); // se inserito
//			setRequestAttribute("Fungibilita", lFunMod);
//
//			setRequestAttribute("PenaComplessivaSentenza", lPenMod);
//			setRequestAttribute("PenaComplessiva", lPenaComplessiva);
//			setRequestAttribute("PenaGiaEspiata", lPenaGiaEspiata);
//
//			//
//			setRequestAttribute("vedoDataIntermedia", vedoDataIntermedia);
//			setRequestAttribute("DataInizioPena", lDataInizioPena);
//			setRequestAttribute("DataFineReclusione", lDataFineReclusione);
//			setRequestAttribute("DataInizioArresto", lDataInizioArresto);
//			setRequestAttribute("DataFinePena", lDataFinePena);
//
//			setRequestAttribute("Segnalazione", Segnalazione);
//			setRequestAttribute("VedoJSP", lVedoJSP);
//			setRequestAttribute("FlagAltraCausa",
//					((FascicoloSiepModel) getSessionAttribute("fascicolo")).getFlagAltraCausa());
//			setRequestAttribute("CodPosizioneGiuridica", PGMod2.getCodPosizioneGiuridica());
//			setRequestAttribute("ForzaSysdateManuale", ForzaSysdateManuale);
//
//			// ******************************************************************************
//			// ** PROBLEMA DELLA TRANSAZIONE relativo al calcolo pena precedente!!!!!!!!!!!**
//			// ******************************************************************************
//			// Update ID_EVENTO di PENA_RESIDUA NON IN TRANSAZIONE!!!!
//			/*
//			 * lPenaComplessiva.setEveIdEvento(lAnnManIns.getEveIdEvento());
//			 * IPenRes.ExModificaPenaResidua(lPenaComplessiva);
//			 * 
//			 * setRequestAttribute("IdEvento", lAnnManIns.getEveIdEvento());
//			 */
//			// Aggiorno la pena_residua inserita/aggiornata, aggiungendo l'id dell'evento
//			BigDecimal lEveIdEvento = getRequestBigDecimalParameter(CAMPO_EVE_ID_EVENTO);
//			lPenaComplessiva.setEveIdEvento(lEveIdEvento);
//			IPenRes.ExModificaPenaResidua(lPenaComplessiva);
//
//			setRequestAttribute("IdEvento", lEveIdEvento);
//			// *******************************************************
//
//			lPage = new String(f3b.web.IWebConstants.ROOT_DIR
//					+ "/files/siap/siep/calcolopena/VediNuovoCalcoloPena.jsp");
//		} else // ERGASTOLO
//		{
//			lPenRes = new PenaResiduaModel();
//
//			// ????????????????????????????????????????????????????????????????????????
//			// GiornoInizio = data fine pena + 1 altra causa, oppure null se data inizio
//			// pena dell'ultimo record pena validato è null indipendentemente dalla
//			// altra causa.
//			// Se non ho una data inizio sull'ultima pena validata, comunque GiornoInizio
//			// viene posto a '-' quindi lDataInizioPena sarà == null. La data altra
//			// causa viene ignorata.
//			// Se invece ho una data inizio pena valorizzata (lDataInizioPena), questa
//			// viene sovrascritta/aggiornata dalla data altra causa se presente,
//			// altrimenti resta quella corrente
//			// ????????????????????????????????????????????????????????????????????????
//			if (!GiornoInizio.equals("") && !GiornoInizio.equals("-"))
//				lDataInizioPena = DateUtils.getDate(AnnoInizio, MeseInizio, GiornoInizio);
//
//			lDataFinePena = DateUtils.getDate(9999, 12, 31);
//			if (lPenMod.getCodTipoPenaDetentiva().equals("03")) {
//				lPenRes.setFlagErgastolo("S");
//			} else if (lPenMod.getCodTipoPenaDetentiva().equals("04")) {
//				lPenRes.setFlagErgastolo("D");
//			}
//
//			lPenRes.setDataFine(lDataFinePena);
//			lPenRes.setDataInizio(lDataInizioPena);
//
//			lPenRes.setDataInizioIsolamentoDiurno(lPenMod.getDataInizioIsolamentoDiurno());
//			lPenRes.setDataFineIsolamentoDiurno(lPenMod.getDataFineIsolamentoDiurno());
//			lPenRes.setNumGiorniIsolamentoDiurno(lPenMod.getNumGiorniIsolamentoDiurno());
//			lPenRes.setNumMesiIsolamentoDiurno(lPenMod.getNumMesiIsolamentoDiurno());
//			lPenRes.setNumAnniIsolamentoDiurno(lPenMod.getNumAnniIsolamentoDiurno());
//
//			lPenRes.setFlagValidato("N");
//			lPenRes.setDiesAQuo("S");
//			lPenRes.setFasSieIdFascicoloSiep(lFascID);
//
//			lPenRes.setCodOperatoreInserimento(getCodUtenteConnesso());
//			lPenRes.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
//			lPenRes.setDataInserimento(DateUtils.getSysDate());
//
//			IPenRes.ExInsertOrUpdatePenaResidua(lPenRes);
//			setRequestAttribute("PenaResidua", lPenRes);
//			setRequestAttribute("PenaComplessivaSentenza", lPenMod);
//
//			// ========================================================================
//			// da quì differente da ActCalcoloPenaGE solo perchè deve restituire due
//			// differenti pagine di stampe una per i computi una per le decisioni
//			// ========================================================================
//
//			// =======================================================================
//			// Imposto la pagina di ritorno che nel caso di ergastolo è direttamente
//			// la pagina di visualizzazione della pena e scelta delle stampe.
//			// Nel caso non ergastolo invece si passa per la pagina intermedia di
//			// conferma dei dati della pena (VediNuovoCalcoloPena.jsp)
//			// =======================================================================
//			if (!isRequestParameterNullObj("lFlagPage")) {
//				String lFlagPage = getRequestStringParameter("lFlagPage");
//
//				if (lFlagPage.equals("A") || lFlagPage.equals("S") || lFlagPage.equals("D")) {
//					// Rideterminazione pena A=Altro Titolo, S=Senza Titolo, D=Stesso Titolo
//					setRequestAttribute("lFlagPage", lFlagPage);
//					return IWebConstants.ROOT_DIR
//							+ "/files/siap/siep/calcolopena/VediCalcoloPenaValidataAnnotazioniComputo.jsp";
//				} else if (lFlagPage.equals("AMNI") || lFlagPage.equals("DEPEN")
//						|| lFlagPage.equals("INCOST")) {
//					// Decisioni del GE
//					return IWebConstants.ROOT_DIR
//							+ "/files/siap/siep/calcolopena/VediCalcoloPenaValidataAnnotazioni.jsp";
//				}
//			}
//
//			// altri casi, vale a dire lFlagPage = null (mai vero) oppure (AMNI,DEPEN,INCOST)
//			lPage = IWebConstants.ROOT_DIR
//					+ "/files/siap/siep/calcolopena/VediCalcoloPenaValidataAnnotazioni.jsp";
//		} // fine Ergastolo
//
//		// lPage=IWebConstants.ROOT_DIR+"/files/siap/siep/calcolopena/LoadAnnotazioniManualiBenefici.jsp";
//		return lPage;
//	}

}