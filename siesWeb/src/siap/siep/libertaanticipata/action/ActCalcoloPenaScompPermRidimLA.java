package siap.siep.libertaanticipata.action;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.Vector;

import org.apache.log4j.Logger;

import siap.sico.libertaanticipata.action.ICostantiLibertaAnticipata;
import siap.sico.libertaanticipata.controller.ILicenzaPeriodiLibAnticipata;
import siap.sico.libertaanticipata.model.LicenzaLibAnticipataModel;
import siap.sico.util.CalendarUtil;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siep.annotazionemanuale.action.ICostantiAnnotazioneManuale;
import siap.siep.calcolopena.action.ActCalcoloPenaMain;
import siap.siep.calcolopena.controller.ICalcoloPena;
import siap.siep.calcolopena.model.CalcoloPenaModel;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.fungibilita.action.ICostantiFungibilita;
import siap.siep.fungibilita.model.FungibilitaModel;
import siap.siep.penaresidua.controller.IPenaResidua;
import siap.siep.penaresidua.model.PenaResiduaModel;
import siap.siep.util.SIEPLookupRemote;
import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.web.IWebConstants;

/**
 * Action per il calcolo della pena a seguito dello scomputo permesso inserito da: - Decisioni Sorveglianza -
 * Scomputo Permesso
 * 
 * 
 * Action invocata da: Scomputi Permesso siap\siep\calcolopena\DettaglioRidetPenaScomputiSORV.jsp [Primo
 * calcolo] siap\siep\calcolopena\VediCalcoloPenaScomputiSORV.jsp [Dettaglio calcolo Conferma]
 * 
 * Anvocata anche da [da verificare se trattasi di refuso è sempre il js del calcolo che invoca tale action se
 * presento LA] siap\siep\calcolopena\DettaglioAnnotaPagamentoPP.jsp
 * siap\siep\calcolopena\DettaglioRidetPenaAltroNew.jsp
 * 
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class ActCalcoloPenaScompPermRidimLA extends ActionSiap implements ICostantiLibertaAnticipata {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public String processRequest() throws Exception {

		FascicoloSiepModel lFascMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");
		BigDecimal lIdFascicolo = lFascMod.getIdFascicoloSiep();

		// Id dell'evento di computo a cui sono collegati i giorni da rideterminare
		BigDecimal lIdEvento = getRequestBigDecimalParameter(ICostantiAnnotazioneManuale.CAMPO_EVE_ID_EVENTO);

		// ==========================================================================
		// Recupero i giorni di Ridimensionamento LA o Scomputo permesso/licenza
		// Attenzione: le due quantità sono iscritte entrambe su LICENZA_LIBANTICIPATA
		// ma vanno trattate diversamente
		// ==========================================================================
		ILicenzaPeriodiLibAnticipata lLicCtrl = SICOLookupRemote.getLicenzaPeriodiLibAntRemote();
		Vector lVectLic = new Vector();
		lVectLic = lLicCtrl.ExRicercaLicenzeByEve(lIdEvento);
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("lVectLic.size() = " + lVectLic.size());

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

		// ==========================================================================
		// Prima Calcolo abFine
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

		// ==========================================================================
		// calcolo: abInizio
		// - Recupero la situazione attuale della pena.
		// - Aggiungo i giorni di scomputo permesso/ridimensionamento LA
		// - Rieffettuo i calcoli con data inizio se presente
		// Costruisce un CalcoloPenaModel che contiene in modo strutturato
		// tutte le informazioni necessarie a determinare la Pena Residua
		// ad un dato momento
		// ==========================================================================
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("Recupero la situazione attuale della Pena (dati Validati)");
		ActCalcoloPenaMain lCalcoloPenaMain = new ActCalcoloPenaMain();
		CalcoloPenaModel lCalcoloPenaModel = lCalcoloPenaMain.calcoloPena(lIdFascicolo, null);

		// Setto l'ultima pena validata per il calcolo dell'eventuale fungibilità
		// n.b. solo nel caso di data inizio pena <> null e quantum rideterminati<==
		// ANNA ------ lCalcoloPenaModel.setPenaUltimaValidata(lUltimaPenResVal); //'???????

		// Prima di aggiungere i gg estraggoi dati della situazione attuale da passare
		// alla form
		// int lTotLAAttuale = lCalcoloPenaModel.getLiberazioneAnticipata();
		// int lTotScommputiAttuali = lCalcoloPenaModel.getTotaleScomputi();
		int lTotLAAttuale = lCalcoloPenaModel.getLiberazioneAnticipataGiaConcesse();
		int lTotScomputiAttuali = lCalcoloPenaModel.getScomputiGiaConcessi();
		setRequestAttribute("lTotLAAttuale", "" + new BigDecimal(lTotLAAttuale));
		setRequestAttribute("lTotScomputiAttuali", "" + new BigDecimal(lTotScomputiAttuali));

		LicenzaLibAnticipataModel lLicModel = null;
		int lTotGiorni = 0;
		for (int i = 0; i < lVectLic.size(); i++) {
			lLicModel = (LicenzaLibAnticipataModel) lVectLic.elementAt(i);
			lTotGiorni = lTotGiorni + lLicModel.getNumeroGiorni().intValue();
		}
		setRequestAttribute("lGiorniScomputati", "" + new BigDecimal(lTotGiorni));

		// 03/06/2016 FIX la nuova versione del calcolo legge i computi come se fossero
		// licenze per cui i conputi vanno caricati su getLibAnticipate
		// lCalcoloPenaModel.getScomputiPermLic().addAll(lVectLic);
		lCalcoloPenaModel.getLibAnticipate().addAll(lVectLic);

		// Ricalcolo le date di decorrenza e scadenza se presente la data inizio
		Date lDataFine = lUltimaPenResVal.getDataFine();
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("Vecchia data fine = " + DateUtils.getDateToString(lDataFine, "dd-MM-yyyy"));

		if (lDataFine != null) { // Avanzo il fine pena dei giorni revocati
			if (getRequestStringParameter("lFlagTipoScomputo").indexOf("reclamo") > 0) {
				lDataFine = DateUtils.moveDateTo(lDataFine, Calendar.DAY_OF_MONTH, -(lTotGiorni));
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("RECLAMO SCOMPUTO->Nuova data fine = "
						+ DateUtils.getDateToString(lDataFine, "dd-MM-yyyy"));
			} else {
				lDataFine = DateUtils.moveDateTo(lDataFine, Calendar.DAY_OF_MONTH, +(lTotGiorni));
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug(
						"SCOMPUTO->Nuova data fine = " + DateUtils.getDateToString(lDataFine, "dd-MM-yyyy"));
			}
		}

		// ==========================================================================
		// 03/06/2016 - Effettuo i calcoli abInizio per determinare la fungibilità
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
		siesLogger.debug("dataScarcerazione = " + dataScarcerazione);

		Date lOggi = DateUtils.getSysDateAsDate("dd/MM/yyyy");
		if (dataScarcerazione != null) {
			dataSistemaPerCalcoli = dataScarcerazione;
		} else {
			dataSistemaPerCalcoli = lOggi;
		}
		setRequestAttribute("dataScarcerazione", dataScarcerazione);

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("Ricalcolo la pena abInizio");
		PenaResiduaModel lPenaRidetAbInizio = lCalcoloPenaModel
				.getPenaDaEspiare(lUltimaPenResVal.getDataInizio(), dataSistemaPerCalcoli, "all", null);
		FungibilitaModel lFungModel = lCalcoloPenaModel.getFungibilitaCalcolata();

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("lPenaRidetAbInizio = " + lPenaRidetAbInizio);
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("lFungModel = " + lFungModel);
		// ==========================================================================

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("lUltimaPenResVal abFine = " + lUltimaPenResVal);
		PenaResiduaModel lPenaRideterminata = new PenaResiduaModel(lUltimaPenResVal);

		// lPenaRideterminata.setDataFine (lDataFine);

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

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("Pena CHE PASSO COME PARAMETRO!!! = " + lPenaRideterminata);

		// ===========================================================
		//
		// ===========================================================
		// OLD lPenResCtrl.ExInsertOrUpdatePenaResidua(lPenaRideterminata);

		// new
		// lPenaRideterminata = lCalcoloPenaModel.getPenaResiduaRicalcolata();

		lPenaRideterminata.setDataFine(lPenaRidetAbInizio.getDataFine());
		lPenaRideterminata.setDataFinePresunta(lPenaRidetAbInizio.getDataFinePresunta());

		CalendarUtil lCalUtil = new CalendarUtil();
		if (lFungModel != null && !lCalUtil.isZero(lFungModel.getQuantumFungibilita())) {

			lFungModel.setEveIdEvento(lIdEvento);
			lFungModel.setFasSieIdFascicoloSiep(lFascMod.getIdFascicoloSiep());

			lFungModel.setCodTipoFungibilita(ICostantiFungibilita.PENA_ESPIATA_IN_ECCESSSO);
			lFungModel.setFlagValidato("N");

			lFungModel.setCodOperatoreInserimento(getCodUtenteConnesso());
			lFungModel.setDataInserimento(DateUtils.getSysDate());
			lFungModel.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
		} else {
			lFungModel = null;
		}
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("lFungModel = " + lFungModel);
		ICalcoloPena lCalcoloCtrl = SIEPLookupRemote.getCalcoloPenaRemote();
		lCalcoloCtrl.ExInserisciAggiornaPenaResiduaFungibilita(lPenaRideterminata, lFungModel);
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("lFungModel dopo inserimento = " + lFungModel);
		setRequestAttribute("lFungibilita", lFungModel);

		// ============================================================
		// Passo i dati alla form per la visualizzazione
		// ============================================================
		//

		setRequestAttribute("lCalcoloPenaModel", lCalcoloPenaModel);
		setRequestAttribute("lPenaRideterminata", lPenaRideterminata);
		setRequestAttribute("lPenaDiPartenza", lUltimaPenResVal);

		//
		String lPage = "";
		lPage = IWebConstants.ROOT_DIR + "/files/siap/siep/calcolopena/VediCalcoloPenaScomputiSORV.jsp";
		return lPage;
	}

}