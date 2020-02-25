package siap.sico.libertaanticipata.action;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Vector;

import org.apache.log4j.Logger;

import siap.sico.libertaanticipata.controller.ILicenzaPeriodiLibAnticipata;
import siap.sico.libertaanticipata.model.LicenzaLibAnticipataModel;
import siap.sico.libertaanticipata.model.LicenzaPeriodiLibAnticipataModel;
import siap.sico.utente.model.DatiOperazioneModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siep.annotazionemanuale.action.ICostantiAnnotazioneManuale;
import siap.siep.calcolopena.action.ActCalcoloPenaMain;
import siap.siep.calcolopena.controller.ICalcoloPena;
import siap.siep.calcolopena.model.CalcoloPenaModel;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.fungibilita.model.FungibilitaModel;
import siap.siep.penaresidua.controller.IPenaResidua;
import siap.siep.penaresidua.model.PenaResiduaModel;
import siap.siep.util.SIEPLookupRemote;
import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.web.IWebConstants;

/**
 * <p>
 * Title: ActCalcoloPenaeclamoRimediRisarcitori
 * </p>
 * <p>
 * Description: Classe Action per il calcolo della pena nel caso di concessione Reclamo Rimedi Risarcitori
 * così come previsti dal DL92/2014
 * </p>
 * 
 * @version 1.0
 * @since 10/2014
 */
public class ActCalcoloPenaReclamoRimediRisarcitori extends ActionSiap implements ICostantiLibertaAnticipata {
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String processRequest() throws Exception {
		FascicoloSiepModel lFascMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");
		BigDecimal lIdFascicolo = lFascMod.getIdFascicoloSiep();

		// Verifico se provengo dalla funzione di Conferma Data fine
		String lIsConfermaDataFine = "N";
		if (!isRequestParameterNullObj("isConfermaDataFine")) {
			lIsConfermaDataFine = getRequestStringParameter("isConfermaDataFine");
		}

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("lIsConfermaDataFine = " + lIsConfermaDataFine);

		Date lDataFineSelezionata = null;
		if ("S".equals(lIsConfermaDataFine)) {
			// ==========================================================================
			// Provengo dalla form di conferma data fine, recupero la data fine pena
			// selezionata dall'utente
			// ==========================================================================
			String lRadioButtonScelto = getRequestStringParameter("DataFine");
			if ("DataFineAbInitio".equals(lRadioButtonScelto)) {
				lDataFineSelezionata = getRequestDateParameter("AnnoDataFineAbInitio",
						"MeseDataFineAbInitio", "GiornoDataFineAbInitio");
			} else {
				lDataFineSelezionata = getRequestDateParameter("AnnoDataFineAbFine", "MeseDataFineAbFine",
						"GiornoDataFineAbFine");
			}
		}

		// ????
		isFascicoloSiepDiCompetenza();

		DatiOperazioneModel lOperMod = new DatiOperazioneModel(getCodUtenteConnesso(),
				DateUtils.getSysDate(), getCodUfficioUtenteConnesso(), "");

		// ==========================================================================
		// Pagina di ritorno
		// ==========================================================================
		String lPage;

		// ==========================================================================
		// L'id dell'evento di Decreto/Ordinanza selezionato da lista o inserito
		// ==========================================================================
		BigDecimal lIdEveSorveglianza = getRequestBigDecimalParameter(ICostantiLicenzaLibanticipata.CAMPO_EVE_ID_EVENTO);

		// ==========================================================================
		// Recupero la data per i calcoli della fungibilità:
		// - data di scarcerazione se passata in input
		// - data di sistema altrimenti
		// ==========================================================================
		Date dataSistemaPerCalcoli = null;
		Date dataScarcerazione = null;

		if (!isRequestParameterNullObj(ICostantiAnnotazioneManuale.CAMPO_GIORNO_DATA_SCARCERAZIONE)) {
			dataScarcerazione = getRequestDateParameter(
					ICostantiAnnotazioneManuale.CAMPO_ANNO_DATA_SCARCERAZIONE,
					ICostantiAnnotazioneManuale.CAMPO_MESE_DATA_SCARCERAZIONE,
					ICostantiAnnotazioneManuale.CAMPO_GIORNO_DATA_SCARCERAZIONE);
		}

		// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		// siesLogger.debug("Data scarcerazione = "+dataScarcerazione);
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
		// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		// siesLogger.debug("dataSistemaPerCalcoli = "+dataSistemaPerCalcoli);

		// ==========================================================================
		// Costruisce un CalcoloPenaModel che contiene in modo strutturato
		// tutte le informazioni necessarie a determinare la Pena Residua
		// ad un dato momento
		// ==========================================================================
		// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		// siesLogger.debug("Recupero la situazione attuale della Pena (dati Validati)");
		ActCalcoloPenaMain lCalcoloPenaMain = new ActCalcoloPenaMain();
		CalcoloPenaModel lCalcoloPenaModel = lCalcoloPenaMain.calcoloPena(lIdFascicolo, null);

		// ==========================================================================
		// Effettuo il calcolo e l'inserimento/aggiornamento della data fine pena
		// della pena residua e inserisco l'eventuale fungibilità.
		// ==========================================================================
		ICalcoloPena lCtrlCal = SIEPLookupRemote.getCalcoloPenaRemote();
		CalcoloPenaModel lCalcoloPena = lCtrlCal
				.exCalcoloLiberazioneAnticipata(lCalcoloPenaModel, lIdFascicolo, lIdEveSorveglianza,
						dataSistemaPerCalcoli, lDataFineSelezionata, "RD", lOperMod);

		// ==========================================================================
		// Calcolo della data fine pena considerando non il nuovo metodo di calcolo,
		// ma DATA_FINE_CALCOLATO = DATA_FINE_PENA_RESIDUA_CORRENTE - LA_CONCESSE
		// ==========================================================================

		// RICERCA LICENZA e PERIODI
		ILicenzaPeriodiLibAnticipata lCtrlLib = SICOLookupRemote.getLicenzaPeriodiLibAntRemote();
		Vector<LicenzaPeriodiLibAnticipataModel> lLicenzePeriodi = lCtrlLib
				.ExRicercaLicenzeLibanticipataByEve(lIdEveSorveglianza);

		// *********************************** INIZIO MAC 2016/10/21
		int lTotGGLA = 0;
		Vector lLibAnticipate = lCalcoloPenaModel.getLibAnticipate();
//		String codMotivo = lCalcoloPenaModel.getEventoPenaIniziale().getCodMotivo();
		for (int i = 0; i < lLibAnticipate.size(); i++) {
			LicenzaLibAnticipataModel lLicenzaModel = (LicenzaLibAnticipataModel) lLibAnticipate.get(i);
			if ("RD".equals(lLicenzaModel.getCodTipoLicenza())
					&& "C".equals(lLicenzaModel.getFlagConcesso())
					&& "S".equals(lLicenzaModel.getFlagElaborato())
					/*&& "9027".equals(codMotivo)*/) {
				lTotGGLA += lLicenzaModel.getNumeroGiorni().intValue();
			}
		}
		// ************************************ FINE MAC 2016/10/21

		int lTotGiorni = 0;
		for (int i = 0; i < lLicenzePeriodi.size(); i++) {
			LicenzaPeriodiLibAnticipataModel lLicenzaPeriodo = lLicenzePeriodi.elementAt(i);
			if (lLicenzaPeriodo.getLicenza().getCodTipoLicenza().equals("RD")
					&& lLicenzaPeriodo.getLicenza().getFlagConcesso().equals("C")) {
				lTotGiorni = lLicenzaPeriodo.getLicenza().getNumeroGiorni().intValue();
			}
		}

		// *********************************** INIZIO MAC 2016/10/21
		// lTotGiorni = Giorni che ho inserito ora
		// lTotGGLA = già presenti in banca dati
		// si devono sottrarre i lTotGiorni dai lTotGGLA e poi il risultato va addizionato alla data fine pena
		// Per non modificare il meotdo exCalcoloLiberazioneAnticipataSuDataFineUltimaPena, possiamo o
		// scriverne uno nuovo
		// oppure rendere negativa la variabile lTotGiorni
		if (lTotGGLA != 0) {
			lTotGiorni = -(lTotGGLA - lTotGiorni);
		}
		// *********************************** FINE MAC 2016/10/21

		// ==========================================================================
		// Confronto tra i due calcoli per presentare, in caso di discrepanza,
		// un pagina che consente di selezionare il fine pena voluto
		// (data fine nuovo calcolo/fine pena su DB meno la LA).
		// ==========================================================================
		if ("N".equals(lIsConfermaDataFine)) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Primo calcolo, verifico abFine<>abInizio ");
			// Data fine calcolata a partire dal'ULTIMA data fine a sistema sottraendo i
			// gg concessi
			Date lDataFineAbFine = lCtrlCal.exCalcoloLiberazioneAnticipataSuDataFineUltimaPena(lTotGiorni,
					lIdFascicolo);

			if (lDataFineAbFine != null && lCalcoloPena.getPenaResiduaRicalcolata() != null) {
				PenaResiduaModel lPenaResNuovoCalcolo = lCalcoloPena.getPenaResiduaRicalcolata();

				if (lPenaResNuovoCalcolo != null && lPenaResNuovoCalcolo.getDataFine() != null) {
					if (!lDataFineAbFine.equals(lPenaResNuovoCalcolo.getDataFine())) {
						// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto
						// di LogF3B.getLogger()
						siesLogger.debug("abInizio <> da abFine, chiedo conferma data...");
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
						CalcoloPenaModel lCalcoloPenaModelSenzaLA = lCalcoloPenaMainSenzaLA.calcoloPena(
								lIdFascicolo, null);

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

						setRequestAttribute("lEveIdEventoOrdinanza", "" + lIdEveSorveglianza);

						setRequestAttribute("isDL92", "S");

						lPage = ICostantiLicenzaLibanticipata.PG_SELEZIONE_DATA_FINE_CALCOLO_PENA;
						return lPage;
					}
				}
			}
		} else {

		}

		// ==========================================================================
		//
		// ==========================================================================
		// IPosizioneGiuridica lCtrlPosGiu = SIEPLookupRemote.getPosizioneGiuridicaRemote();
		// PosizioneGiuridicaModel lPosizione =
		// lCtrlPosGiu.ExRicercaPosizioneGiuridicaCorrenteByIdFascicolo(lIdFascicolo);

		lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
				+ "=siap.sico.libertaanticipata.action.ActLoadInserisciOSRimediRisarcitori&"
				+ ICostantiLicenzaLibanticipata.CAMPO_EVE_ID_EVENTO + "=" + lIdEveSorveglianza;

		// ==========================================================================
		// In caso di Fungibilità...
		// ==========================================================================
		FungibilitaModel lFunMod = lCalcoloPena.getFungibilitaCalcolata();
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("lFunMod = " + lFunMod);
		if (lFunMod != null
				&& ((lFunMod.getNumAnni() != null && lFunMod.getNumAnni().intValue() > 0)
						|| (lFunMod.getNumMesi() != null && lFunMod.getNumMesi().intValue() > 0) || (lFunMod
						.getNumGiorni() != null && lFunMod.getNumGiorni().intValue() > 0))) {
			setRequestAttribute("TotaleGiorniLibAnt", "" + lTotGiorni);

			// ==========================================================================
			// Restituisco i dati alla finestra di visualizzazione
			// ==========================================================================
			setRequestAttribute("PenaResidua", lCalcoloPena.getPenaResiduaRicalcolata()); // la pena
																							// rideterminata
			setRequestAttribute("PenaGiaEspiata", lCalcoloPena.getPenaEspiata()); // pena già espiata
			setRequestAttribute("Fungibilita", lCalcoloPena.getFungibilitaCalcolata()); // fungibilità

			setRequestAttribute("dataScarcerazione", dataScarcerazione);
			setRequestAttribute("lEveIdEventoOrdinanza", "" + lIdEveSorveglianza);

			IPenaResidua lCtrlPenRes = SIEPLookupRemote.getPenaResiduaRemote();
			PenaResiduaModel lPenaResidua = lCtrlPenRes.ExRicercaPenaResiduaUltimaValidata(lIdFascicolo);
			setRequestAttribute("UltimaPenaResidua", lPenaResidua);

			lPage = ICostantiLicenzaLibanticipata.PG_DETTAGLIO_CALCOLO_PENA_RECLAMO_DL92;
		}

		return lPage;
	}
}