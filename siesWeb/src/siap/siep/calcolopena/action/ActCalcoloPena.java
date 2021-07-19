package siap.siep.calcolopena.action;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.Vector;

import org.apache.log4j.Logger;

import siap.sico.calendar.model.CalendarModel;
import siap.sico.cssa.action.ICostantiCSSA;
import siap.sico.decodifiche.model.ComuneModel;
import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoModel;
import siap.sico.util.CalendarUtil;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siep.SIEPException;
import siap.siep.annotazionemanuale.action.ICostantiAnnotazioneManuale;
import siap.siep.autoritaesterna.action.ICostantiAutoritaEsterna;
import siap.siep.autoritaesterna.model.AutoritaEsternaModel;
import siap.siep.avvocato.action.ICostantiAvvocato;
import siap.siep.calcolopena.controller.ICalcoloPenaF5;
import siap.siep.calcolopena.model.CalcoloPenaModel;
import siap.siep.fascicolo.action.ICostantiFascicoloSiep;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.fungibilita.model.FungibilitaModel;
import siap.siep.luogodetenzione.action.ICostantiLuogoDetenzione;
import siap.siep.misuraalternativa.action.ICostantiMisuraAlternativa;
import siap.siep.notifica.model.NotificaModel;
import siap.siep.ordineesecuzione.action.ICostantiOrdineEsecuzione;
import siap.siep.penacomplessiva.controller.IPenaComplessiva;
import siap.siep.penacomplessiva.model.PenaComplessivaModel;
import siap.siep.penaresidua.controller.IPenaResidua;
import siap.siep.penaresidua.model.PenaResiduaModel;
import siap.siep.posizione.model.PosizioneGiuridicaModel;
import siap.siep.sanzionesostitutiva.model.SanzioneSostResiduaModel;
import siap.siep.sanzionesostitutiva.model.SanzioneSostitutivaModel;
import siap.siep.util.SIEPLookupRemote;
import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class ActCalcoloPena extends ActionSiap implements ICostantiAnnotazioneManuale {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public String processRequest() throws Exception {

		BigDecimal lFascID = ((FascicoloSiepModel) getSessionAttribute("fascicolo")).getIdFascicoloSiep();

		// return CalcoloPena(lFascID);
		return nuovoCalcoloPena(lFascID);
	}

	/**
	 * Metodo CalcoloPena Effettua il calcolo e inserimento della pena residua solo se non presente già a
	 * sistema una pena residua validata. L'inserimento viene sempre effettuato con lo stato non validato I
	 * dati presi in considerazione sono: - pena in sentenza - benefici ( concessi revocati) - misure
	 * cautelari sofferte computabili -
	 * 
	 * @param lFascID
	 *            - Id del fascicolo su cui effettuare il calcolo della pena
	 * @return lPage - Pagina di ritorno che dipende da tipo di pena
	 * @throws F3BException
	 * @deprecated sostituito dal metodo nuovoCalcoloPena per la revisione calcolo pena
	 */
//	private String CalcoloPena(BigDecimal lFascID) throws Exception {
//
//		String Segnalazione = "N";
//		String lPage = new String();
//
//		// ==========================================================================
//		// Recupero TUTTI i record PENA_RESIDUA per il fascicolo per verificare se
//		// già una pena validata, in questo caso esco
//		// ==========================================================================
//		PenaResiduaModel lPenRes = new PenaResiduaModel();
//		lPenRes.setFasSieIdFascicoloSiep(lFascID);
//		IPenaResidua IPenRes = SIEPLookupRemote.getPenaResiduaRemote();
//		Vector lVect = IPenRes.ExRicercaPenaResidua(lPenRes);
//
//		// ==========================================================================
//		// Verifico se esiste pena già validata, in questo caso esco, in caso contrario
//		// verifico se andare in update o in insert.
//		// Si va in aggiornamento della pena residua se esiste e non validata,
//		// se validata errore, altrimanti si va in insert
//		// ==========================================================================
//		String op = new String("");
//		BigDecimal lIndicePenaResidua = null;
//		boolean noMore = false;
//		if (lVect.size() == 0) {
//			op = "insert";
//		} else {
//			for (int i = 0; i < lVect.size(); i++) {
//				lPenRes = (PenaResiduaModel) lVect.get(i);
//				if (lPenRes.getFlagValidato() != null && lPenRes.getFlagValidato().equals("N")) {
//					lIndicePenaResidua = lPenRes.getIdPenaResidua();
//					noMore = true;
//				} else { // se trova un record già validato esco
//					throw new SIEPException(SIEPException.USER_MESSAGE,
//							"Primo calcolo della pena già eseguito.");
//				}
//			}
//		}
//
//		if (noMore) {
//			op = ((PenaResiduaModel) lVect.get(0)).getIdPenaResidua() + "";
//		} else
//			op = "insert";
//
//		// ==========================================================================
//		// Recupero i dati necessari per calcolare il quantum di pena:
//		// - PENA COMPLESSIVA
//		// - BENEFICI CONCESSI/REVOCATI
//		// - MISURE CAUTELARI COMPUTABILI
//		// ==========================================================================
//
//		// =====================================================
//		// Recupero i dati della PENA COMPLESSIVA (in sentenza)
//		// =====================================================
//		PenaComplessivaModel lPenMod = new PenaComplessivaModel();
//		lPenMod.setFasSieIdFascicoloSiep(lFascID);
//
//		IPenaComplessiva lPCon = SIEPLookupRemote.getPenaComplessivaRemote();
//		Vector lPComples = lPCon.ExRicercaPenaComplessivaNoError(lPenMod);
//
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
//		// ==================================================
//		// Recupero i dati dei BENEFICI (periodo - durata)
//		// ==================================================
//		ICalcoloPena lCalPen = SIEPLookupRemote.getCalcoloPenaRemote();
//
//		CalendarModel lBeneficiConcessiReclusione = new CalendarModel(
//				lCalPen.exGetBeneficiConcessiReclusione(lFascID));
//		CalendarModel lBeneficiRevocatiReclusione = new CalendarModel(
//				lCalPen.exGetBeneficiRevocatiReclusione(lFascID));
//		CalendarModel lBeneficiConcessiArresto = new CalendarModel(
//				lCalPen.exGetBeneficiConcessiArresto(lFascID));
//		CalendarModel lBeneficiRevocatiArresto = new CalendarModel(
//				lCalPen.exGetBeneficiRevocatiArresto(lFascID));
//
//		CalendarModel lBeneficiConcessiReclusioneJSP = new CalendarModel(lBeneficiConcessiReclusione);
//		CalendarModel lBeneficiRevocatiReclusioneJSP = new CalendarModel(lBeneficiRevocatiReclusione);
//		CalendarModel lBeneficiConcessiArrestoJSP = new CalendarModel(lBeneficiConcessiArresto);
//		CalendarModel lBeneficiRevocatiArrestoJSP = new CalendarModel(lBeneficiRevocatiArresto);
//
//		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
//		// LogF3B.getLogger()
//		siesLogger.debug("--------------------------------------");
//		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
//		// LogF3B.getLogger()
//		siesLogger.debug("Arresto Concessi : " + lBeneficiConcessiArrestoJSP.getImportoAmmenda() + "----"
//				+ lBeneficiConcessiArrestoJSP.getNumAnni() + "/" + lBeneficiConcessiArrestoJSP.getNumMesi()
//				+ "/" + lBeneficiConcessiArrestoJSP.getNumGiorni());
//		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
//		// LogF3B.getLogger()
//		siesLogger.debug("Arresto Revocati : " + lBeneficiRevocatiArrestoJSP.getImportoAmmenda() + "----"
//				+ lBeneficiRevocatiArrestoJSP.getNumAnni() + "/" + lBeneficiRevocatiArrestoJSP.getNumMesi()
//				+ "/" + lBeneficiRevocatiArrestoJSP.getNumGiorni());
//		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
//		// LogF3B.getLogger()
//		siesLogger.debug("--------------------------------------");
//		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
//		// LogF3B.getLogger()
//		siesLogger.debug("--------------------------------------");
//		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
//		// LogF3B.getLogger()
//		siesLogger.debug("Reclusione Concessi : " + lBeneficiConcessiReclusioneJSP.getImportoAmmenda()
//				+ "----" + lBeneficiConcessiReclusioneJSP.getNumAnni() + "/"
//				+ lBeneficiConcessiReclusioneJSP.getNumMesi() + "/"
//				+ lBeneficiConcessiReclusioneJSP.getNumGiorni());
//		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
//		// LogF3B.getLogger()
//		siesLogger.debug("Reclusione Revocati : " + lBeneficiRevocatiReclusioneJSP.getImportoAmmenda()
//				+ "----" + lBeneficiRevocatiReclusioneJSP.getNumAnni() + "/"
//				+ lBeneficiRevocatiReclusioneJSP.getNumMesi() + "/"
//				+ lBeneficiRevocatiReclusioneJSP.getNumGiorni());
//		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
//		// LogF3B.getLogger()
//		siesLogger.debug("--------------------------------------");
//
//		// =========================
//		// MISURE CAUTELARI
//		// =========================
//		// Custodia cautelare in carcere (TIPO_MISURA_CAUTELARE = CA)
//		CalendarModel lMisCauComputabiliReclusione = new CalendarModel(
//				lCalPen.exGetMisureCautelariComputabiliReclusione(lFascID));
//		// Custodia cautelare in Arresti domiciliari (TIPO_MISURA_CAUTELARE = AD)
//		CalendarModel lMisCauComputabiliArresto = new CalendarModel(
//				lCalPen.exGetMisureCautelariComputabiliArresto(lFascID));
//
//		// Custodia Cautelare in Misura di Sicurezza Applicata in via Provvisoria (TIPO_MISURA_CAUTELARE = CD)
//		CalendarModel lMisCauComputabiliMisSicApplicata = new CalendarModel(
//				lCalPen.exGetMisureCautelariComputabiliMisSicApplicata(lFascID));
//		// Custodia Cautelare in Regime di Arresti Domiciliari ex art 89 dpr 309/90 (TIPO_MISURA_CAUTELARE =
//		// CM)
//		CalendarModel lMisCauComputabiliArrestiDomiciliari = new CalendarModel(
//				lCalPen.exGetMisureCautelariComputabiliArrestiDomiciliari(lFascID));
//		// Custodia Cautelare in Regime di Permanenza in Casa (TIPO_MISURA_CAUTELARE = CB)
//		CalendarModel lMisCauComputabiliPermanenzaInCasa = new CalendarModel(
//				lCalPen.exGetMisureCautelariComputabiliPermanenzaInCasa(lFascID));
//		// Custodia Cautelare in Collocamento in Comunita' (TIPO_MISURA_CAUTELARE = CC)
//		CalendarModel lMisCauComputabiliCollocamentoInComunita = new CalendarModel(
//				lCalPen.exGetMisureCautelariComputabiliCollocamentoInComunita(lFascID));
//		// Custodia cautelare in Camera di Sicurezza (TIPO_MISURA_CAUTELARE = CE)
//		CalendarModel lMisCauComputabiliCameraDiSicurezza = new CalendarModel(
//				lCalPen.exGetMisureCautelariComputabiliCameraDiSicurezza(lFascID));
//		// Computo periodo di messa alla prova (TIPO_MISURA_CAUTELARE = CL)
//		CalendarModel lMisCauComputabiliPeriodoMessaAllaProva = new CalendarModel(
//				lCalPen.exGetMisureCautelariComputabiliPeriodoMessaAllaProva(lFascID));
//
//		// ==========================================================================
//		// Se non ergastolo calcolo la pena residua passando in input alla
//		// funzione i dati
//		// In caso di ergastolo il calcolo della pena
//		// ==========================================================================
//		if (!(lPenMod.getCodTipoPenaDetentiva().equals("03") || lPenMod.getCodTipoPenaDetentiva()
//				.equals("04"))) {
//			// ==========================================================================
//			// Il metodo calcola a partire dalla pena complessiva (in sentenza),
//			// dai benefici e dalle misure cautelari la pena residua E LA INSERISCE
//			// sul DB
//			// Questo primo calcolo tiene solo conto dei quantum non calcola le date
//			// ==========================================================================
//			PenaResiduaModel lPenaComplessiva = null;
//			lPenaComplessiva = lCalPen.exCalcolaQuantumPenaComplessivaIniziale(lFascID, lPenMod,
//					lBeneficiConcessiReclusione, lBeneficiRevocatiReclusione, lBeneficiConcessiArresto,
//					lBeneficiRevocatiArresto, lMisCauComputabiliReclusione, lMisCauComputabiliArresto,
//					getCodUtenteConnesso(), getCodUfficioUtenteConnesso());
//
//			lIndicePenaResidua = lPenaComplessiva.getIdPenaResidua();
//
//			// ========================================================================
//			// Se è stata specificata la DATA_INIZIO, calcolo:
//			// DATA_FINE_PRESUNTA (data_fine_pena)
//			// DATA_FINE_RECLUSIONE
//			// DATA_INIZIO_ARRESTO
//			// E vado in update del record PENA_RESIDUA precedentemente inserito che
//			// contiene solo la durata (gg,mm aa)
//			// ========================================================================
//			String vedoDataIntermedia = "S";
//			String lVedoJSP = "N";
//
//			Date lDataInizioPena = null;
//			Date lDataFineReclusione = null;
//			Date lDataInizioArresto = null;
//			Date lDataFinePena = null;
//
//			if (!getRequestStringParameter("GiornoInizio").equals("")
//					&& !getRequestStringParameter("GiornoInizio").equals("-")) {
//				// Calcolo le date fine pena per Reclusione e Arresto (sono 2 date distinte,
//				// viene scontata prima la reclusione, quindi l'arresto)
//				lDataInizioPena = getRequestDateParameter("AnnoInizio", "MeseInizio", "GiornoInizio");
//
//				Vector lDateFine = lCalPen.exCalcolaDataFinePena(lDataInizioPena, lPenaComplessiva, true); // il
//																											// flag
//																											// true
//																											// indica
//																											// CON
//																											// DIES_A_QUO
//
//				// Indica alla jsp di visualizzare la sezione con le date e il bottone
//				// per la validazione della pena residua con la possibilità di modificare
//				// manualmente la data fine pena
//				lVedoJSP = "S";
//
//				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
//				// LogF3B.getLogger()
//				siesLogger.debug("lDateFine.size() : " + lDateFine.size());
//
//				if (lDateFine.size() == 1) {// solo arresti o reclusione
//					vedoDataIntermedia = "N";
//
//					boolean cond = lPenaComplessiva.getNumAnniArresto().equals(null)
//							|| lPenaComplessiva.getNumAnniArresto().intValue() == 0;
//					cond = cond
//							&& (lPenaComplessiva.getNumMesiArresto().equals(null) || lPenaComplessiva
//									.getNumMesiArresto().intValue() == 0);
//					cond = cond
//							&& (lPenaComplessiva.getNumGiorniArresto().equals(null) || lPenaComplessiva
//									.getNumGiorniArresto().intValue() == 0);
//					if (!cond)
//						lDataInizioArresto = getRequestDateParameter("AnnoInizio", "MeseInizio",
//								"GiornoInizio");
//
//					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
//					// LogF3B.getLogger()
//					siesLogger.debug("USCITO DAL CALCOLO DELLA DATA FINE PENA CON : " + lDateFine.get(0));
//
//					lDataFinePena = (Date) lDateFine.get(0);
//				}
//
//				if (lDateFine.size() == 2) { // sia reclusione che arresto
//					lDataFineReclusione = (Date) lDateFine.get(0);
//					lDataInizioArresto = DateUtils.getDayAfter(lDataFineReclusione);
//					lDataFinePena = (Date) lDateFine.get(1);
//				}
//
//				if (lDateFine.size() == 0) {
//					Segnalazione = "S"; // quantum rideterminati negativi o nulli (<=0)
//				}
//
//				if (lDateFine.size() != 0) {
//					lPenaComplessiva.setDataInizio(lDataInizioPena);
//					lPenaComplessiva.setDataFineReclusione(lDataFineReclusione);
//					lPenaComplessiva.setDataInizioArresto(lDataInizioArresto);
//					lPenaComplessiva.setDataFinePresunta(lDataFinePena); // n.b. il fine pena effettivo verrà
//																			// impostato dall'utente
//				}
//				//
//			}
//
//			// ========================================================================
//			// Aggiorno la pena Residua, perchè qui e non nell'if precedente, se non
//			// mi trovo nell'if non ha senso
//			// ========================================================================
//			lPenaComplessiva.setDiesAQuo("S"); // non serve è già stato fatto dalla
//												// exCalcolaQuantumPenaComplessivaIniziale
//			lPenaComplessiva.setIdPenaResidua(lIndicePenaResidua);
//			IPenaResidua lPPres = SIEPLookupRemote.getPenaResiduaRemote();
//			lPPres.ExModificaPenaResidua(lPenaComplessiva);
//
//			// ========================================================================
//			// LIBERAZIONE ANTICIPATA
//			// ========================================================================
//			// Recupero le LA non ancora computate. Tali LA vengono comunque passate
//			// alla finestra di visualizzazione dei calcoli. Se è presente una data
//			// fine pena, viene anticipata e la LA risultano 'Concesse già detratte',
//			// altrimenti restano 'Concesse da detrarre'.
//			// CERCA IL TOTALE GIORNI LIB ANTICIPATA con FLAG_ELABORATO ad N o null
//			ILicenzaPeriodiLibAnticipata lCtrlLib = SICOLookupRemote.getLicenzaPeriodiLibAntRemote();
//			int lTotGiorniLA = lCtrlLib.ExTotalePeriodiConcessiNonElaboratiByIdFascicoloSiep(lFascID);
//
//			setRequestAttribute("totalegiornilibanticipata", new BigDecimal(lTotGiorniLA));
//
//			IPenaResidua lPenResCtrl = SIEPLookupRemote.getPenaResiduaRemote();
//			PenaResiduaModel lPenaResMod = lPenResCtrl.ExRicercaPenaResiduaCorrenteByFascicoloSiep(lFascID);
//			// n.b. restituisce il record con data inserimento + recente e flag validato qualunque
//			// chiamata a questo punto restituisce necessariamente il record non validato
//			// inserito/aggiornato precedentemente
//
//			lPenaResMod.setDataFine(lDataFinePena);
//
//			// Aggiorno la pena residua (gg,mm,aa e date fine) se sono presenti dei
//			// giorni di liberazione anticipata non elaborati e se è presente una data fine
//			if (lTotGiorniLA != 0 && lPenaResMod != null && lPenaResMod.getDataFine() != null) {
//				// CALCOLA LA NUOVA PENA RESIDUA sottraendo i GG di libertà anticipata
//				// alla data fine pena
//				Date lDataFineRicalcolata = DateUtils.moveDateTo(lPenaResMod.getDataFine(),
//						Calendar.DAY_OF_MONTH, -(lTotGiorniLA));
//				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
//				// LogF3B.getLogger()
//				siesLogger.debug("Data Fine Ricalcolata : " + lDataFineRicalcolata);
//
//				// Normalizza i quantum
//				lPenaResMod = PenaResiduaUtil.calcolaPenaNuovaDataFine(lDataFineRicalcolata, lPenaResMod,
//						true);
//				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
//				// LogF3B.getLogger()
//				siesLogger.debug("Nuova Pena Residua : " + lPenaResMod);
//
//				// Perchè il metodo PenaResiduaUtil.calcolaPenaNuovaDataFine() non tocca
//				// la DATA_FINE_PRESUNTA
//				lPenaResMod.setDataFinePresunta(lPenaResMod.getDataFine()); // la data fine presunta resta
//																			// quella non anticipata
//
//				lPenaResMod.setCodOperatoreInserimento(getCodUtenteConnesso());
//				lPenaResMod.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
//				lPenaResMod.setDataInserimento(DateUtils.getSysDate());
//
//				lPenaResMod.setCodOperatoreAggiornamento(null);
//				lPenaResMod.setDataAggiornamento(null);
//				lPenaResMod.setCodUfficioAggiornamento(null);
//
//				// ======================================================================
//				// Aggiorno il record PENA_RESIDUA.
//				// ======================================================================
//				lPenaResMod = lPenResCtrl.ExInsertOrUpdatePenaResidua(lPenaResMod);
//
//				// Passo alla form le date modificate per effetto delle LA
//				lDataFinePena = lPenaResMod.getDataFine();
//				lDataFineReclusione = lPenaResMod.getDataFineReclusione();
//				lDataInizioArresto = lPenaResMod.getDataInizioArresto();
//
//				// Aggiorno anche i quantum modificati per effetto delle LA
//				lPenaComplessiva.setNumAnniArresto(lPenaResMod.getNumAnniArresto());
//				lPenaComplessiva.setNumMesiArresto(lPenaResMod.getNumMesiArresto());
//				lPenaComplessiva.setNumGiorniArresto(lPenaResMod.getNumGiorniArresto());
//
//				lPenaComplessiva.setNumAnniReclusione(lPenaResMod.getNumAnniReclusione());
//				lPenaComplessiva.setNumMesiReclusione(lPenaResMod.getNumMesiReclusione());
//				lPenaComplessiva.setNumGiorniReclusione(lPenaResMod.getNumGiorniReclusione());
//
//				// AGGIORNA I RECORD APPENA COMPUTATI DI LIB. ANTICIPATA DA N o null AD 'E'
//				lCtrlLib.ExModificaFlagElaboratoLicenzaLibanticipataByIdFascicoloSiep(lFascID, "N", "E");
//
//				// perchè vengono segnati come computati? Se si effettua di nuovo il
//				// calcolo non verranno + calcolati ottenendo 2 risultati differenti
//
//			}
//			// ******************************************************************************
//			CalendarUtil lCalCon = new CalendarUtil();
//
//			setRequestAttribute("PenaComplessivaSentenza", lPenMod);
//			setRequestAttribute("BeneficiConcessiArresto", lCalCon.toJsp(lBeneficiConcessiArrestoJSP));
//			setRequestAttribute("BeneficiRevocatiArresto", lCalCon.toJsp(lBeneficiRevocatiArrestoJSP));
//			setRequestAttribute("BeneficiConcessiReclusione", lCalCon.toJsp(lBeneficiConcessiReclusioneJSP));
//			setRequestAttribute("BeneficiRevocatiReclusione", lCalCon.toJsp(lBeneficiRevocatiReclusioneJSP));
//
//			setRequestAttribute("MisCauComputabiliReclusione", lCalCon.toJsp(lMisCauComputabiliReclusione));
//			setRequestAttribute("MisCauComputabiliArresto", lCalCon.toJsp(lMisCauComputabiliArresto));
//
//			setRequestAttribute("MisCauComputabiliMisSicApplicata",
//					lCalCon.toJsp(lMisCauComputabiliMisSicApplicata));
//			setRequestAttribute("MisCauComputabiliArrestiDomiciliari",
//					lCalCon.toJsp(lMisCauComputabiliArrestiDomiciliari));
//			setRequestAttribute("MisCauComputabiliPermanenzaInCasa",
//					lCalCon.toJsp(lMisCauComputabiliPermanenzaInCasa));
//			setRequestAttribute("MisCauComputabiliCollocamentoInComunita",
//					lCalCon.toJsp(lMisCauComputabiliCollocamentoInComunita));
//			setRequestAttribute("MisCauComputabiliCameraDiSicurezza",
//					lCalCon.toJsp(lMisCauComputabiliCameraDiSicurezza));
//			setRequestAttribute("MisCauComputabiliPeriodoMessaAllaProva",
//					lCalCon.toJsp(lMisCauComputabiliPeriodoMessaAllaProva));
//
//			setRequestAttribute("PenaComplessiva", lPenaComplessiva);
//			setRequestAttribute("DataInizioPena", lDataInizioPena);
//			setRequestAttribute("DataFineReclusione", lDataFineReclusione);
//			setRequestAttribute("DataInizioArresto", lDataInizioArresto);
//			setRequestAttribute("DataFinePena", lDataFinePena);
//			//
//			// Data fine Pena =
//			//
//
//			setRequestAttribute("Segnalazione", Segnalazione);
//			setRequestAttribute("VedoJSP", lVedoJSP);
//			setRequestAttribute("vedoDataIntermedia", vedoDataIntermedia);
//			setRequestAttribute("FlagAltraCausa",
//					((FascicoloSiepModel) getSessionAttribute("fascicolo")).getFlagAltraCausa());
//			setRequestAttribute("CodPosizioneGiuridica", "");
//
//			lPage = new String(f3b.web.IWebConstants.ROOT_DIR
//					+ "/files/siap/siep/calcolopena/VediCalcoloPena.jsp");
//		} else // ERGASTOLO
//		{
//			// ========================================================================
//			// Nel caso dell'ERGASTOLO. Non effettuo un vero calcolo della pena, ma
//			// recupero i dati solamente dalla Pena Complessiva in sentenza
//			// ========================================================================
//			lPenRes = new PenaResiduaModel();
//			Date lDataFinePena = null;
//			Date lDataInizioPena = null;
//
//			if (!getRequestStringParameter("GiornoInizio").equals("")
//					&& !getRequestStringParameter("GiornoInizio").equals("-"))
//				lDataInizioPena = getRequestDateParameter("AnnoInizio", "MeseInizio", "GiornoInizio");
//
//			lDataFinePena = DateUtils.getDate(9999, 12, 31);
//			if (lPenMod.getCodTipoPenaDetentiva().equals("03")) // Ergastolo
//			{
//				lPenRes.setFlagErgastolo("S");
//			} else if (lPenMod.getCodTipoPenaDetentiva().equals("04")) // Ergastolo con isolamento
//			{
//				lPenRes.setFlagErgastolo("D");
//			}
//
//			lPenRes.setDataInizio(lDataInizioPena);
//			lPenRes.setDataFine(lDataFinePena); // 31/12/9999
//
//			lPenRes.setDataInizioIsolamentoDiurno(lPenMod.getDataInizioIsolamentoDiurno());
//			lPenRes.setDataFineIsolamentoDiurno(lPenMod.getDataFineIsolamentoDiurno());
//
//			lPenRes.setNumGiorniIsolamentoDiurno(lPenMod.getNumGiorniIsolamentoDiurno());
//			lPenRes.setNumMesiIsolamentoDiurno(lPenMod.getNumMesiIsolamentoDiurno());
//			lPenRes.setNumAnniIsolamentoDiurno(lPenMod.getNumAnniIsolamentoDiurno());
//
//			lPenRes.setFlagValidato("S");
//			lPenRes.setDiesAQuo("S");
//			lPenRes.setFasSieIdFascicoloSiep(lFascID);
//
//			if (!op.equals("insert")) { // vado in aggiornamento
//				lPenRes.setIdPenaResidua(new BigDecimal(op));
//				lPenRes.setCodOperatoreAggiornamento(getCodUtenteConnesso());
//				lPenRes.setCodUfficioAggiornamento(getCodUfficioUtenteConnesso());
//				lPenRes.setDataAggiornamento(DateUtils.getSysDate());
//
//				IPenRes.ExModificaPenaResidua(lPenRes);
//			} else { // vado in insert
//				lPenRes.setCodOperatoreInserimento(getCodUtenteConnesso());
//				lPenRes.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
//				lPenRes.setDataInserimento(DateUtils.getSysDate());
//				IPenRes.ExInserisciPenaResidua(lPenRes);
//			}
//
//			setRequestAttribute("PenaResidua", lPenRes);
//
//			// modifica richiesta da viviana 04-11-04--dario
//			setRequestAttribute("PenaComplessiva", lPenMod);
//
//			lPage = new String(f3b.web.IWebConstants.ROOT_DIR
//					+ "/files/siap/siep/calcolopena/VediErgastolo.jsp");
//		}
//
//		// ==========================================================================
//		//
//		// ==========================================================================
//		if (!isRequestParameterNullObj(ISIAPCostantiWeb.CAMPO_AZIONE_CHIAMANTE)) {
//			setRequestAttribute(ISIAPCostantiWeb.CAMPO_AZIONE_CHIAMANTE,
//					getRequestStringParameter(ISIAPCostantiWeb.CAMPO_AZIONE_CHIAMANTE));
//		}
//
//		return lPage;
//	}

	/**
	 * Imposta tutte le notifiche associate all'evento prodotto
	 * 
	 * @param lKey
	 * @return
	 * @throws F3BException
	 */
	protected NotificaModel[] setNotificheAnnotazioniManuali() throws F3BException {

		String lCodiceOperatore = this.getCodUtenteConnesso();
		String lCodiceUfficio = this.getCodUfficioUtenteConnesso();
		Date lDataEmissione = getRequestDateParameter(ICostantiEvento.CAMPO_ANNO_DATA_TRASMISSIONE_ATTI,
				ICostantiEvento.CAMPO_MESE_DATA_TRASMISSIONE_ATTI,
				ICostantiEvento.CAMPO_GIORNO_DATA_TRASMISSIONE_ATTI);
		String codPosizioneGiu = getRequestStringParameter("codPosizioneGiu");
		String[] lAvvocati = this.getRequestStringParameters(ICostantiAvvocato.CAMPO_ID_AVVOCATO);
		ArrayList lNotifiche = new ArrayList();

		// SETTO NOTIFICA AUTORITA NC
		if (!this.isRequestParameterNullObj(ICostantiAnnotazioneManuale.AUTORITA_ESTERNA_NC)
				&& !getRequestStringParameter(ICostantiAnnotazioneManuale.AUTORITA_ESTERNA_NC).equals("-")) {
			String lPolizia = this.getRequestStringParameter(ICostantiAnnotazioneManuale.AUTORITA_ESTERNA_NC);
			String lSedePolizia = this
					.getRequestStringParameter(ICostantiAnnotazioneManuale.SEDE_AUTORITA_ESTERNA_NC);
			String lNotePolizia = this
					.getRequestStringParameter(ICostantiAnnotazioneManuale.NOTE_AUTORITA_ESTERNA_NC);

			NotificaModel lNotModPol = new NotificaModel();

			lNotModPol.setNote(lNotePolizia);
			lNotModPol.setCodEsito("-");
			lNotModPol.setCodOperatoreInserimento(lCodiceOperatore);
			lNotModPol.setDataInserimento(DateUtils.getSysDate());
			lNotModPol.setCodUfficioInserimento(lCodiceUfficio);
			lNotModPol.setCodTipoNotifica("NC");
			lNotModPol.setDataInvio(lDataEmissione);

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("setto AUTORITA NC = " + lNotModPol.getCodTipoNotifica());

			AutoritaEsternaModel lAut = new AutoritaEsternaModel();

			lAut.setCodTipoAutorita(lPolizia);

			ComuneModel lComMod = new ComuneModel(getCodComuneByDescr(lSedePolizia));
			lAut.setCodSede(lComMod.getCodComune());
			lAut.setCodOperatoreInserimento(lCodiceOperatore);
			lAut.setCodUfficioInserimento(lCodiceUfficio);
			lAut.setDataInserimento(DateUtils.getSysDate());
			lNotModPol.setAutoritaEsterna(lAut);

			lNotifiche.add(lNotModPol);
		}

		// SETTO NOTIFICA AUTORITA E o N
		if (this.isRequestParameterNullObj("fungibilita")
				&& !this.isRequestParameterNullObj(ICostantiAnnotazioneManuale.AUTORITA_ESTERNA)
				&& !getRequestStringParameter(ICostantiAnnotazioneManuale.AUTORITA_ESTERNA).equals("-")) {
			String lPolizia = this.getRequestStringParameter(ICostantiAnnotazioneManuale.AUTORITA_ESTERNA);
			String lSedePolizia = this
					.getRequestStringParameter(ICostantiAnnotazioneManuale.SEDE_AUTORITA_ESTERNA);
			String lNotePolizia = this
					.getRequestStringParameter(ICostantiAnnotazioneManuale.NOTE_AUTORITA_ESTERNA);

			NotificaModel lNotModPol = new NotificaModel();

			lNotModPol.setNote(lNotePolizia);
			lNotModPol.setCodEsito("-");
			lNotModPol.setCodOperatoreInserimento(lCodiceOperatore);
			lNotModPol.setDataInserimento(DateUtils.getSysDate());
			lNotModPol.setCodUfficioInserimento(lCodiceUfficio);

			PosizioneGiuridicaModel lPosMod = new PosizioneGiuridicaModel();
			lPosMod.setCodPosizioneGiuridica(codPosizioneGiu);

			if (codPosizioneGiu.equals("07") || codPosizioneGiu.equals("10")) {
				lNotModPol.setCodTipoNotifica("E");
			}
			// else if(codPosizioneGiu.equals("11") || codPosizioneGiu.equals("12") ||
			// codPosizioneGiu.equals("13") || codPosizioneGiu.equals("14") || codPosizioneGiu.equals("19"))
			else if (lPosMod.isMisAlt()) {
				lNotModPol.setCodTipoNotifica("N");
			}

			lNotModPol.setDataInvio(lDataEmissione);

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("setto AUTORITA E o N " + lNotModPol.getCodTipoNotifica());

			AutoritaEsternaModel lAut = new AutoritaEsternaModel();

			lAut.setCodTipoAutorita(lPolizia);

			ComuneModel lComMod = new ComuneModel(getCodComuneByDescr(lSedePolizia));
			lAut.setCodSede(lComMod.getCodComune());
			lAut.setCodOperatoreInserimento(lCodiceOperatore);
			lAut.setCodUfficioInserimento(lCodiceUfficio);
			lAut.setDataInserimento(DateUtils.getSysDate());
			lNotModPol.setAutoritaEsterna(lAut);

			lNotifiche.add(lNotModPol);
		}

		// SETTO NOTIFICA AUTORITA E
		if (!this.isRequestParameterNullObj("fungibilita")
				&& !this.isRequestParameterNullObj(ICostantiAnnotazioneManuale.AUTORITA_ESTERNA_E)
				&& !getRequestStringParameter(ICostantiAnnotazioneManuale.AUTORITA_ESTERNA_E).equals("-"))

		{
			String lPolizia = this.getRequestStringParameter(ICostantiAnnotazioneManuale.AUTORITA_ESTERNA_E);
			String lSedePolizia = this
					.getRequestStringParameter(ICostantiAnnotazioneManuale.SEDE_AUTORITA_ESTERNA_E);
			String lNotePolizia = this
					.getRequestStringParameter(ICostantiAnnotazioneManuale.NOTE_AUTORITA_ESTERNA_E);

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("lPolizia_E" + lPolizia);

			NotificaModel lNotModPol = new NotificaModel();

			lNotModPol.setNote(lNotePolizia);
			lNotModPol.setCodEsito("-");
			lNotModPol.setCodOperatoreInserimento(lCodiceOperatore);
			lNotModPol.setDataInserimento(DateUtils.getSysDate());
			lNotModPol.setCodUfficioInserimento(lCodiceUfficio);

			lNotModPol.setCodTipoNotifica("E");
			lNotModPol.setDataInvio(lDataEmissione);

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("setto AUTORITA E fungibilita: " + lNotModPol);

			if (!this.isRequestParameterNullObj("evento06")) {
				AutoritaEsternaModel lAut = new AutoritaEsternaModel();

				lAut.setCodTipoAutorita(lPolizia);

				ComuneModel lComMod = new ComuneModel(getCodComuneByDescr(lSedePolizia));
				lAut.setCodSede(lComMod.getCodComune());
				lAut.setCodOperatoreInserimento(lCodiceOperatore);
				lAut.setCodUfficioInserimento(lCodiceUfficio);
				lAut.setDataInserimento(DateUtils.getSysDate());
				lNotModPol.setAutoritaEsterna(lAut);

				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("setto AUTORITA E fungibilita CON evento06  " + lAut);
			} else {
				String lCodUfficioNot = getCodUfficioByCodTipoUfficioDescrComune(lPolizia, lSedePolizia);

				lNotModPol.setUffCodUfficio(lCodUfficioNot);

				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("setto AUTORITA E fungibilita SENZA evento06  " + lNotModPol);
			}

			lNotifiche.add(lNotModPol);
		}

		// SETTO NOTIFICA AUTORITA N
		if (!this.isRequestParameterNullObj("fungibilita")
				&& !this.isRequestParameterNullObj(ICostantiAnnotazioneManuale.AUTORITA_ESTERNA)
				&& !getRequestStringParameter(ICostantiAnnotazioneManuale.AUTORITA_ESTERNA).equals("-")) {
			String lPolizia = this.getRequestStringParameter(ICostantiAnnotazioneManuale.AUTORITA_ESTERNA);
			String lSedePolizia = this
					.getRequestStringParameter(ICostantiAnnotazioneManuale.SEDE_AUTORITA_ESTERNA);
			String lNotePolizia = this
					.getRequestStringParameter(ICostantiAnnotazioneManuale.NOTE_AUTORITA_ESTERNA);

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("lPolizia_N = " + lPolizia);

			NotificaModel lNotModPol = new NotificaModel();

			lNotModPol.setNote(lNotePolizia);
			lNotModPol.setCodEsito("-");
			lNotModPol.setCodOperatoreInserimento(lCodiceOperatore);
			lNotModPol.setDataInserimento(DateUtils.getSysDate());
			lNotModPol.setCodUfficioInserimento(lCodiceUfficio);

			lNotModPol.setCodTipoNotifica("N");
			lNotModPol.setDataInvio(lDataEmissione);

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("setto AUTORITA  N  fungibilità " + lNotModPol.getCodTipoNotifica());

			/*
			 * AutoritaEsternaModel lAut = new AutoritaEsternaModel();
			 * 
			 * lAut.setCodTipoAutorita(lPolizia);
			 * 
			 * ComuneModel lComMod = new ComuneModel(getCodComuneByDescr(lSedePolizia));
			 * lAut.setCodSede(lComMod.getCodComune()); lAut.setCodOperatoreInserimento(lCodiceOperatore);
			 * lAut.setCodUfficioInserimento(lCodiceUfficio); lAut.setDataInserimento(DateUtils.getSysDate());
			 * lNotModPol.setAutoritaEsterna(lAut); // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di
			 * istanza siesLogger al posto di LogF3B.getLogger()
			 * siesLogger.debug("setto AUTORITA N fungibilita @@@@@@@@@@@@@@@@@@@@@@@"+lAut);
			 */

			String lCodUfficioNot = getCodUfficioByCodTipoUfficioDescrComune(lPolizia, lSedePolizia);

			lNotModPol.setUffCodUfficio(lCodUfficioNot);

			lNotifiche.add(lNotModPol);
		}

		// SETTO NOTIFICA ufficio DI SORVEGLIANZA E
		if (!this.isRequestParameterNullObj(ICostantiAnnotazioneManuale.CAMPO_COD_UDS)) {

			NotificaModel lNotModTDS = new NotificaModel();
			String lUDS = this.getCodUfficioByCodTipoUfficioDescrComune("UDS",
					getRequestStringParameter(ICostantiAnnotazioneManuale.CAMPO_COD_UDS));

			// String lSedeMagistrato =
			// this.getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_SEDE_MAG);
			String lNoteUDS = this.getRequestStringParameter(ICostantiAnnotazioneManuale.CAMPO_NOTE_UDS);

			lNotModTDS.setNote(lNoteUDS);
			lNotModTDS.setCodEsito("-");
			lNotModTDS.setCodOperatoreInserimento(lCodiceOperatore);
			lNotModTDS.setDataInserimento(DateUtils.getSysDate());
			lNotModTDS.setCodUfficioInserimento(lCodiceUfficio);
			lNotModTDS.setCodTipoNotifica("E");
			lNotModTDS.setDataInvio(lDataEmissione);
			lNotModTDS.setUffCodUfficio(lUDS);
			lNotifiche.add(lNotModTDS);

		}

		// SETTO CSSA
		if (!this.isRequestParameterNullObj(ICostantiCSSA.CAMPO_ID_CSSA)) {
			NotificaModel lNotModCSSA = new NotificaModel();

			BigDecimal lCssa = this.getRequestBigDecimalParameter(ICostantiCSSA.CAMPO_ID_CSSA);

			// String lSedeCssa = this.getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_SEDE_CSSA);

			String lNoteCssa = this.getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_NOTE_CSSA);

			lNotModCSSA.setNote(lNoteCssa);
			lNotModCSSA.setCodEsito("-");
			lNotModCSSA.setCodOperatoreInserimento(lCodiceOperatore);
			lNotModCSSA.setDataInserimento(DateUtils.getSysDate());
			lNotModCSSA.setCodUfficioInserimento(lCodiceUfficio);

			lNotModCSSA.setCodTipoNotifica("NC");
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("setto cssa NC = " + lNotModCSSA.getCodTipoNotifica());

			lNotModCSSA.setDataInvio(lDataEmissione);
			lNotModCSSA.setCssIdCssa(lCssa);
			lNotifiche.add(lNotModCSSA);
		}

		// AVVOCATI
		int lIndex = 0;

		for (lIndex = 0; lIndex < lAvvocati.length; lIndex++) {
			NotificaModel lNotAvv = new NotificaModel();

			lNotAvv.setCodTipoNotifica("ND");
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("setto AVVOCATI = " + lNotAvv.getCodTipoNotifica());

			// lNot.setNote(lArrayNote[lIndMisura]);
			lNotAvv.setDataInvio(lDataEmissione);
			lNotAvv.setCodEsito("-");
			lNotAvv.setCodOperatoreInserimento(lCodiceOperatore);
			lNotAvv.setDataInserimento(DateUtils.getSysDate());
			lNotAvv.setCodUfficioInserimento(lCodiceUfficio);
			lNotAvv.setAvvIdAvvocatoFascicoloSiep(new BigDecimal(lAvvocati[lIndex]));

			if (!this.isRequestParameterNullObj(ICostantiAnnotazioneManuale.AUTORITA_ESTERNA_ND)
					&& !getRequestStringParameter(ICostantiAnnotazioneManuale.AUTORITA_ESTERNA_ND)
							.equals("-")) {
				String[] lTipoAutoritaEsternaAvvocato = this
						.getRequestStringParameters(ICostantiAnnotazioneManuale.AUTORITA_ESTERNA_ND);

				String[] lSedeAutoritaEsternaAvvocato = this
						.getRequestStringParameters(ICostantiAnnotazioneManuale.SEDE_AUTORITA_ESTERNA_ND);

				String[] lNoteAvvocato = this
						.getRequestStringParameters(ICostantiAnnotazioneManuale.NOTE_AVVOCATI);
				lNotAvv.setNote(lNoteAvvocato[lIndex]);
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("-->Note_AVVOCATO " + lNoteAvvocato[lIndex]);
				// String lNoteAvvocato = this.getRequestStringParameter(ICostantiNotifica.CAMPO_NOTE);
				// lNotAvv.setNote(lNoteAvvocato);

				AutoritaEsternaModel lAut = new AutoritaEsternaModel();
				lAut.setCodTipoAutorita(lTipoAutoritaEsternaAvvocato[lIndex]);

				//INIZIO: MEV_21 (avvocati) - si inibisce la selezione di comuni non validi (tipo NAPOLI NORD)
//				ComuneModel lComMod = new ComuneModel(
//						getCodComuneByDescr(lSedeAutoritaEsternaAvvocato[lIndex]));
				ComuneModel lComMod = new ComuneModel(
						getCodComuneByDescrFlagVal(lSedeAutoritaEsternaAvvocato[lIndex]));
				//FINE: MEV_21
				
				lAut.setCodSede(lComMod.getCodComune());
				lAut.setCodOperatoreInserimento(lCodiceOperatore);
				lAut.setCodUfficioInserimento(lCodiceUfficio);
				lAut.setDataInserimento(DateUtils.getSysDate());
				lNotAvv.setAutoritaEsterna(lAut);

				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("autorità avvocati = " + lNotAvv.getAutoritaEsterna());
			}
			lNotifiche.add(lNotAvv);
		}

		// SETTO ISTITUTO DETENZIONE
		if ((this.isRequestParameterNullObj("fungibilita") && !this
				.isRequestParameterNullObj(ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE))) {
			NotificaModel lNotModIst = new NotificaModel();
			String lIstituto = this
					.getRequestStringParameter(ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE);
			String lNoteIstituto = this.getRequestStringParameter(ICostantiLuogoDetenzione.CAMPO_NOTE);

			lNotModIst.setNote(lNoteIstituto);
			lNotModIst.setCodEsito("-");
			lNotModIst.setCodOperatoreInserimento(lCodiceOperatore);
			lNotModIst.setDataInserimento(DateUtils.getSysDate());
			lNotModIst.setCodUfficioInserimento(lCodiceUfficio);

			PosizioneGiuridicaModel lPosMod = new PosizioneGiuridicaModel();
			lPosMod.setCodPosizioneGiuridica(codPosizioneGiu);
			// if(codPosizioneGiu.equals("07") || codPosizioneGiu.equals("10") || codPosizioneGiu.equals("11")
			// || codPosizioneGiu.equals("12") || codPosizioneGiu.equals("13") || codPosizioneGiu.equals("14")
			// || codPosizioneGiu.equals("19"))
			if (codPosizioneGiu.equals("07") || codPosizioneGiu.equals("10") || lPosMod.isMisAlt()) {
				lNotModIst.setCodTipoNotifica("C");
			} else {
				lNotModIst.setCodTipoNotifica("E");
			}

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("setto ISTITUTO " + lNotModIst.getCodTipoNotifica());

			lNotModIst.setDataInvio(lDataEmissione);

			// lNotModCSSA.setAvvIdAvvocatoFascicoloSiep(new BigDecimal(lAvvocati[lIndMisura]));

			lNotModIst.setIstDetIdIstitutoDetenzione(lIstituto);
			lNotifiche.add(lNotModIst);
		}

		// setto istituto per la fungibilita
		if ((!this.isRequestParameterNullObj("fungibilita") && !this
				.isRequestParameterNullObj(ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE))) {
			NotificaModel lNotModIst = new NotificaModel();
			String lIstituto = this
					.getRequestStringParameter(ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE);
			String lNoteIstituto = this.getRequestStringParameter(ICostantiLuogoDetenzione.CAMPO_NOTE);
			String istituto = this.getRequestStringParameter("istituto");
			lNotModIst.setNote(lNoteIstituto);
			lNotModIst.setCodEsito("-");
			lNotModIst.setCodOperatoreInserimento(lCodiceOperatore);
			lNotModIst.setDataInserimento(DateUtils.getSysDate());
			lNotModIst.setCodUfficioInserimento(lCodiceUfficio);
			lNotModIst.setCodTipoNotifica(istituto);

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("setto ISTITUTO " + lNotModIst.getCodTipoNotifica());

			lNotModIst.setDataInvio(lDataEmissione);

			// lNotModCSSA.setAvvIdAvvocatoFascicoloSiep(new BigDecimal(lAvvocati[lIndMisura]));

			lNotModIst.setIstDetIdIstitutoDetenzione(lIstituto);
			lNotifiche.add(lNotModIst);
		}

		// Foglio Complementare
		NotificaModel lNotFoglio = new NotificaModel();
		if (!this.isRequestParameterNullObj(ICostantiOrdineEsecuzione.FOGLIO_COMPLEMENTARE)) {
			if (this.isRequestChecked(ICostantiOrdineEsecuzione.FOGLIO_COMPLEMENTARE)) {
				lNotFoglio = new NotificaModel();

				lNotFoglio.setCodTipoNotifica("FC");
				lNotFoglio.setDataInvio(lDataEmissione);
				lNotFoglio.setCodEsito("-");
				lNotFoglio.setCodOperatoreInserimento(lCodiceOperatore);
				lNotFoglio.setDataInserimento(DateUtils.getSysDate());
				lNotFoglio.setCodUfficioInserimento(lCodiceUfficio);

				ComuneModel lComCasellarioMod = new ComuneModel(
						getCodComuneByDescr(getRequestStringParameter(ICostantiAutoritaEsterna.CAMPO_COD_SEDE_CAS)));
				/*
				 * SedeGiudiziariaModel lSedeGiuMod = null; String lCodCas =
				 * lFascicoloModel.getSoggetto().getCodComuneCasellario(); ISedeGiudiziaria lCtrl =
				 * SIEPLookupRemote.getSedeGiudiziariaRemote(); SedeGiudiziariaModel lSedGiuMod =
				 * lCtrl.ExRicercaSedeGiudiziariaByKey(lCodCas);
				 */

				AutoritaEsternaModel lAutMod = new AutoritaEsternaModel();

				lAutMod.setCodTipoAutorita("24");

				// lAutMod.setCodSede(lSedGiuMod.getCodComune());
				lAutMod.setCodSede(lComCasellarioMod.getCodComune());
				lAutMod.setCodOperatoreInserimento(lCodiceOperatore);
				lAutMod.setCodUfficioInserimento(lCodiceUfficio);
				lAutMod.setDataInserimento(DateUtils.getSysDate());

				// Setto l'Autorita Esterna per la notifica corrente
				lNotFoglio.setAutoritaEsterna(lAutMod);

				lNotifiche.add(lNotFoglio);
			}
		}

		for (int i = 0; i < lNotifiche.size(); i++) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("notifica numero " + i + " --> " + lNotifiche.get(i));
		}

		return (NotificaModel[]) lNotifiche.toArray(new NotificaModel[0]);
	}

	protected Hashtable ricercaNotificheAM(NotificaModel[] aNotifiche) {

		Hashtable lTable = new Hashtable();
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("lunghezza array notifiche " + aNotifiche.length);

		for (int i = 0; i < aNotifiche.length; i++) {

			// cssa
			if (aNotifiche[i].getCssIdCssa() != null) {
				NotificaModel lNotCssa = aNotifiche[i];
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("setto CSSA = " + lNotCssa);

				lTable.put("NotCssa", lNotCssa);
			}

			// istituto
			if (aNotifiche[i].getIstitutoDetenzione() != null) {
				if (aNotifiche[i].getCodTipoNotifica().equals("E")) {
					NotificaModel lNotIstitutoE = aNotifiche[i];
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger.debug("setto Istituto = " + lNotIstitutoE);

					lTable.put("lNotIstitutoE", lNotIstitutoE);
				} else {
					NotificaModel lNotIstitutoC = aNotifiche[i];
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger.debug("setto Istituto " + lNotIstitutoC);
					lTable.put("lNotIstitutoC", lNotIstitutoC);
				}
			}

			// autorità esterna
			if (aNotifiche[i].getAutEstIdAutoritaEsterna() != null
					&& aNotifiche[i].getCodTipoNotifica().equals("E")) {
				NotificaModel lNotAutoritaE = aNotifiche[i];
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("setto lNotAutoritaE " + lNotAutoritaE);

				lTable.put("AutE", lNotAutoritaE);
			}

			// autorità esterna N
			if (aNotifiche[i].getAutEstIdAutoritaEsterna() != null
					&& aNotifiche[i].getCodTipoNotifica().equals("N")) {
				NotificaModel lNotAutoritaN = aNotifiche[i];
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("setto lNotAutoritaN = " + lNotAutoritaN);

				lTable.put("AutN", lNotAutoritaN);
			}

			// autorità esterna NC
			if (aNotifiche[i].getAutEstIdAutoritaEsterna() != null
					&& aNotifiche[i].getCodTipoNotifica().equals("NC")) {
				NotificaModel lNotAutoritaNC = aNotifiche[i];
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("setto lNotAutoritaC = " + lNotAutoritaNC);

				lTable.put("AutNC", lNotAutoritaNC);
			}

			// Ufficio
			if (aNotifiche[i].getUffCodUfficio() != null) {
				NotificaModel lNotAutoritaNC = aNotifiche[i];
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("setto Ufficio " + lNotAutoritaNC);

				lTable.put("Ufficio", lNotAutoritaNC);
			}

			if (aNotifiche[i].getUffCodUfficio() != null) {
				NotificaModel lNotUfficio = aNotifiche[i];
				if (lNotUfficio.getUfficio().getCodTipoUfficio().equals("UDS")) {
					NotificaModel lNotUfficioUDS = aNotifiche[i];
					lTable.put("UffUDS", lNotUfficioUDS);
				}

				if (lNotUfficio.getUfficio().getCodTipoUfficio().equals("TDS")) {
					NotificaModel lNotUfficioTDS = aNotifiche[i];
					lTable.put("UffTDS", lNotUfficioTDS);
				}
			}
		}

		return lTable;
	}

	/**
	 * Nuova versione del calcolo della pena che utilizza i metodi centralizzati. Metodo utilizzato SOLO per
	 * il <b>primo calcolo della pena</b>. Effettua quindi il calcolo e inserimento della pena residua solo se
	 * non presente già a sistema una pena residua validata. Se già presente una pena (non validata) la
	 * aggiorna, altrimenti la inserisce L'inserimento viene sempre effettuato con lo stato non validato (???)
	 * I dati presi in considerazione sono: - pena in sentenza - benefici ( concessi revocati) - misure
	 * cautelari sofferte computabili -
	 * 
	 * @param lFascID
	 *            - Id del fascicolo su cui effettuare il calcolo della pena
	 * @return lPage - Pagina di ritorno che dipende da tipo di pena (ergastolo)
	 * @throws F3BException
	 */
	private String nuovoCalcoloPena(BigDecimal lFascID) throws Exception {

		String lPage = "";

		// ==========================================================================
		// Recupero i dati della PENA COMPLESSIVA (in sentenza) per vedere se trattasi
		// di Ergastolo
		// ==========================================================================
		PenaComplessivaModel lPenCompMod = new PenaComplessivaModel();
		lPenCompMod.setFasSieIdFascicoloSiep(lFascID);

		IPenaComplessiva lPCon = SIEPLookupRemote.getPenaComplessivaRemote();
		Vector lPComples = lPCon.ExRicercaPenaComplessivaNoError(lPenCompMod);

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

		lPenCompMod = (PenaComplessivaModel) (lPComples.get(0));

		// ==========================================================================
		// Recupero TUTTI i record PENA_RESIDUA per il fascicolo per verificare se
		// già una pena validata, in questo caso esco
		// ==========================================================================
		PenaResiduaModel lPenRes = new PenaResiduaModel();
		lPenRes.setFasSieIdFascicoloSiep(lFascID);
		IPenaResidua lPenResCtrl = SIEPLookupRemote.getPenaResiduaRemote();
		Vector lVect = lPenResCtrl.ExRicercaPenaResidua(lPenRes);

		// ==========================================================================
		// Verifico se esiste pena già validata, in questo caso esco, in caso contrario
		// verifico se andare in update o in insert.
		// Si va in aggiornamento della pena residua se esiste e non validata,
		// se validata errore, altrimanti si va in insert
		// ==========================================================================
		BigDecimal lIndicePenaResidua = null;

		for (int i = 0; i < lVect.size(); i++) {
			lPenRes = (PenaResiduaModel) lVect.get(i);

			if (lPenRes.getFlagValidato() != null && lPenRes.getFlagValidato().equals("N")) {
				lIndicePenaResidua = lPenRes.getIdPenaResidua();
			} else { // se trova un record già validato esco
				throw new SIEPException(SIEPException.USER_MESSAGE, "Primo calcolo della pena già eseguito.");
			}
		}

		// ==========================================================================
		// Recupero la data inizio pena che viene eventualmente passata sulla request
		// ==========================================================================
		Date lDataInizioPena = null;
		if (!getRequestStringParameter("GiornoInizio").equals("")
				&& !getRequestStringParameter("GiornoInizio").equals("-")) {
			lDataInizioPena = getRequestDateParameter("AnnoInizio", "MeseInizio", "GiornoInizio");
		}

		// ==========================================================================
		// Se non ergastolo calcolo la pena residua recuperando tutti i dati che
		// concorrono al calcolo
		// ==========================================================================
		if (!(lPenCompMod.getCodTipoPenaDetentiva().equals("03") || lPenCompMod.getCodTipoPenaDetentiva()
				.equals("04"))) {
			// ==========================================================================
			// Recupero i dati necessari per calcolare il quantum di pena:
			// - PENA COMPLESSIVA
			// - BENEFICI CONCESSI/REVOCATI
			// - MISURE CAUTELARI COMPUTABILI
			// n.b. il primo calcolo dovrebbe sempre essere legato alla pena in
			// sentenza in quanto in tutti gli altri casi esiste sempre già una
			// pena validata a sistema e non sarebbe possibile effettuare il
			// primo calcolo
			// ==========================================================================
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Recupero la situazione attuale della Pena (dati Validati)");
			ActCalcoloPenaMain lCalcoloPenaMain = new ActCalcoloPenaMain();
			CalcoloPenaModel lCalcoloPenaModel = lCalcoloPenaMain.calcoloPena(lFascID, null);

			// ==========================================================================
			// Effettuo i calcoli
			// ==========================================================================
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Effettuo il calcolo della pena con tutti i dati...");
			PenaResiduaModel lPenaRideterminata = lCalcoloPenaModel.getPenaDaEspiare(lDataInizioPena, null,
					"all");
			FungibilitaModel lFungModel = lCalcoloPenaModel.getFungibilitaCalcolata();

			// Calcolo l'eventuale Sanzione Sostitutiva residua
			CalendarUtil lCalUtilSS = new CalendarUtil();
			CalendarModel lMCTotali = lCalUtilSS
					.sommaGiornieValute(lCalcoloPenaModel.getMCReclusioneInSentenza(),
							lCalcoloPenaModel.getMCArrestiInSentenza());

			SanzioneSostResiduaModel lSSResidua = lCalcoloPenaModel
					.getSanzioneSostitutivaDaEspiare(lMCTotali);

			if (lSSResidua != null) {
				lSSResidua.setFasSieIdFascicoloSiep(lFascID);
				lSSResidua.setFlagValidato("N");

				lSSResidua.setCodOperatoreInserimento(getCodUtenteConnesso());
				lSSResidua.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
				lSSResidua.setDataInserimento(DateUtils.getSysDate());
			}

			if (lSSResidua != null
					&& (lSSResidua.getCodTipoSanzione().equals("S") || lSSResidua.getCodTipoSanzione()
							.equals("L"))) { // Carico la SS nel model solo se trattasi di Semidetenzioe o
												// Libertà
												// controllata.
				lPenaRideterminata.setSanzSostResidua(lSSResidua);

				// In questo caso devo verificare anche se presente già a sistema la
				// Trasmissione atti per competenza (02-31-0396,0941) in quanto in
				// questo cso il tasto Conferma deve portare su Comunicazione Nuovo Residuo
				// Pena
				EventoModel lEveTrasm = new EventoModel();
				lEveTrasm.setFasSieIdFascicoloSiep(lFascID);
				lEveTrasm.setCodTipoEvento("02");
				lEveTrasm.setCodTipoProvvedimento("31");
				lEveTrasm.setFlagDocumentoRegistrato("S");

				String[] lMotivi = new String[] { "0396", "0941" };
				IEvento lEveCtrl = SICOLookupRemote.getEventoRemote();
				EventoModel lUltimaTrasm = lEveCtrl.ExRicercaEventoPerMotivo(lMotivi, lEveTrasm);
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("lUltimaTrasm = " + lUltimaTrasm);
				if (lUltimaTrasm != null && lUltimaTrasm.getIdEvento() != null) {
					setRequestAttribute("EsisteTrasmissioneAtti", "SI");
				}

			}
			// CalendarModel lPenaGiaEspiata = lCalcoloPenaModel.getPenaEspiata();

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Pena residua ricalcolata: " + lPenaRideterminata);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Fungibilità calcolata: " + lFungModel);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Sanzione Sostitutiva ricalcolata = " + lSSResidua);

			// ========================================================================
			// Completo i dati del model per l'inserimento
			// ========================================================================
			lPenaRideterminata.setFasSieIdFascicoloSiep(lFascID);
			lPenaRideterminata.setFlagValidato("N");
			lPenaRideterminata.setDiesAQuo("S");
			lPenaRideterminata.setFlagErgastolo("N");
			lPenaRideterminata.setCodOperatoreInserimento(getCodUtenteConnesso());
			lPenaRideterminata.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
			lPenaRideterminata.setDataInserimento(DateUtils.getSysDate());

			// ========================================================================
			// Inserisco i dati a sistema PenaResidua
			// ========================================================================
			lPenResCtrl.ExInsertOrUpdatePenaResidua(lPenaRideterminata);

			// ========================================================================
			// AGGIORNA I RECORD APPENA COMPUTATI DI LIB. ANTICIPATA DA N o null AD 'E'
			// n.b. attenzione verificare se è il caso di farlo qui o nella funzione
			// di validazione
			// l'aggiornamento del flag è stato spostato nella action ActInserisciPenaValidata
			// che valida anche la pena residua
			// ========================================================================
			// ILicenzaPeriodiLibAnticipata lCtrlLib = SICOLookupRemote.getLicenzaPeriodiLibAntRemote();
			// lCtrlLib.ExModificaFlagElaboratoLicenzaLibanticipataByIdFascicoloSiep(lFascID, "N", "E");

			// ========================================================================
			// Recupero l'eventuale sanzione sostitutiva in sentenza
			// ========================================================================
			// if (lPenaRideterminata.getFlagSanzioneSostitutiva()!=null &&
			// lPenaRideterminata.getFlagSanzioneSostitutiva().equals("S"))

			if (lSSResidua != null) {
				ICalcoloPenaF5 lCalcPenaF5Ctrl = SIEPLookupRemote.getCalcoloPenaF5();
				SanzioneSostitutivaModel lSanzSostMod = lCalcPenaF5Ctrl.exGetSanzioneSostitutiva(lPenCompMod
						.getIdPenaComplessiva());
				setRequestAttribute("SanzioneSostitutivaInSentenza", lSanzSostMod);
			}

			// ========================================================================
			// Passo i dati alla jsp per la visualizzazione
			// ========================================================================
			CalendarUtil lCalUtil = new CalendarUtil();
			setRequestAttribute("totalegiornilibanticipata",
					new BigDecimal(lCalcoloPenaModel.getLiberazioneAnticipata()));

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("LLA = " + lCalcoloPenaModel.getLiberazioneAnticipata());

			setRequestAttribute("PenaComplessivaSentenza", lPenCompMod);
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

			setRequestAttribute("MisCauComputabiliMisSicApplicata",
					lCalcoloPenaModel.getMCMisuraSicurezzaApplicataInSentenza());
			setRequestAttribute("MisCauComputabiliArrestiDomiciliari",
					lCalcoloPenaModel.getMCArrestiDomiciliariInSentenza());
			setRequestAttribute("MisCauComputabiliPermanenzaInCasa",
					lCalcoloPenaModel.getMCPermanenzaInCasaInSentenza());
			setRequestAttribute("MisCauComputabiliCollocamentoInComunita",
					lCalcoloPenaModel.getMCCollocamentoInComunitaInSentenza());
			setRequestAttribute("MisCauComputabiliCameraDiSicurezza",
					lCalcoloPenaModel.getMCCameraSicurezzaInSentenza());
			setRequestAttribute("MisCauComputabiliPeriodoMessaAllaProva",
					lCalcoloPenaModel.getMCComputoMessoAllaProvaInSentenza());

			setRequestAttribute("PenaComplessiva", lPenaRideterminata);
			setRequestAttribute("DataInizioPena", lPenaRideterminata.getDataInizio());
			setRequestAttribute("DataFineReclusione", lPenaRideterminata.getDataFineReclusione());
			setRequestAttribute("DataInizioArresto", lPenaRideterminata.getDataInizioArresto());
			setRequestAttribute("DataFinePena", lPenaRideterminata.getDataFine());

			if (!lCalUtil.isPositiveTime(lPenaRideterminata.getQuantumReclusione())
					|| !lCalUtil.isPositiveTime(lPenaRideterminata.getQuantumArresto()))
				setRequestAttribute("Segnalazione", "N");
			else
				setRequestAttribute("Segnalazione", "S");

			if (lPenaRideterminata.getDataInizio() != null)
				setRequestAttribute("VedoJSP", "S");
			else
				setRequestAttribute("VedoJSP", "N");

			if (lPenaRideterminata.getDataFineReclusione() == null
					|| lPenaRideterminata.getDataInizioArresto() == null)
				setRequestAttribute("vedoDataIntermedia", "N");
			else
				setRequestAttribute("vedoDataIntermedia", "S");

			setRequestAttribute("", "");

			setRequestAttribute("FlagAltraCausa",
					((FascicoloSiepModel) getSessionAttribute("fascicolo")).getFlagAltraCausa());
			setRequestAttribute("CodPosizioneGiuridica", "");

			lPage = new String(f3b.web.IWebConstants.ROOT_DIR
					+ "/files/siap/siep/calcolopena/VediCalcoloPena.jsp");

		} else {
			// ========================================================================
			// Nel caso dell'ERGASTOLO. Non effettuo un vero calcolo della pena, ma
			// recupero i dati solamente dalla Pena Complessiva in sentenza
			// ========================================================================
			lPenRes = new PenaResiduaModel();
			Date lDataFinePena = null;
			lDataFinePena = DateUtils.getDate(9999, 12, 31);

			if (lPenCompMod.getCodTipoPenaDetentiva().equals("03")) // Ergastolo
			{
				lPenRes.setFlagErgastolo("S");
			} else if (lPenCompMod.getCodTipoPenaDetentiva().equals("04")) // Ergastolo con isolamento
			{
				lPenRes.setFlagErgastolo("D");
			}

			lPenRes.setDataInizio(lDataInizioPena);
			lPenRes.setDataFine(lDataFinePena); // 31/12/9999

			lPenRes.setDataInizioIsolamentoDiurno(lPenCompMod.getDataInizioIsolamentoDiurno());
			lPenRes.setDataFineIsolamentoDiurno(lPenCompMod.getDataFineIsolamentoDiurno());

			lPenRes.setNumGiorniIsolamentoDiurno(lPenCompMod.getNumGiorniIsolamentoDiurno());
			lPenRes.setNumMesiIsolamentoDiurno(lPenCompMod.getNumMesiIsolamentoDiurno());
			lPenRes.setNumAnniIsolamentoDiurno(lPenCompMod.getNumAnniIsolamentoDiurno());

			lPenRes.setFlagValidato("S");
			lPenRes.setDiesAQuo("S");
			lPenRes.setFasSieIdFascicoloSiep(lFascID);

			if (lIndicePenaResidua != null) // id pena res non validata
			{ // vado in aggiornamento
				lPenRes.setIdPenaResidua(lIndicePenaResidua);

				lPenRes.setCodOperatoreAggiornamento(getCodUtenteConnesso());
				lPenRes.setCodUfficioAggiornamento(getCodUfficioUtenteConnesso());
				lPenRes.setDataAggiornamento(DateUtils.getSysDate());

				lPenResCtrl.ExModificaPenaResidua(lPenRes);
			} else { // vado in insert
				lPenRes.setCodOperatoreInserimento(getCodUtenteConnesso());
				lPenRes.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
				lPenRes.setDataInserimento(DateUtils.getSysDate());

				lPenResCtrl.ExInserisciPenaResidua(lPenRes);
			}

			setRequestAttribute("PenaResidua", lPenRes);

			// modifica richiesta da viviana 04-11-04--dario
			setRequestAttribute("PenaComplessiva", lPenCompMod);

			lPage = new String(f3b.web.IWebConstants.ROOT_DIR
					+ "/files/siap/siep/calcolopena/VediErgastolo.jsp");
		}

		return lPage;
	}

}