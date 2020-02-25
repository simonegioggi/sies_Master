package siap.siep.calcolopena.action;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;
import f3b.web.html.Option;
import siap.sico.calendar.model.CalendarModel;
import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoModel;
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siep.SIEPException;
import siap.siep.annotazionemanuale.controller.IAnnotazioneManuale;
import siap.siep.annotazionemanuale.model.AnnotazioneManualeModel;
import siap.siep.calcolopena.model.CalcoloPenaModel;
import siap.siep.fascicolo.action.ICostantiFascicoloSiep;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.penacomplessiva.controller.IPenaComplessiva;
import siap.siep.penacomplessiva.model.PenaComplessivaModel;
import siap.siep.penaresidua.controller.IPenaResidua;
import siap.siep.penaresidua.model.PenaResiduaModel;
import siap.siep.posizione.controller.IPosizioneGiuridica;
import siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel;
import siap.siep.reato.controller.IReato;
import siap.siep.reato.model.ReatoModel;
import siap.siep.util.SIEPLookupRemote;
import siap.web.ISIAPCostantiWeb;

/**
 * <p>
 * Title: ActLoadInserisciAnnotazioniManuali
 * </p>
 * Classe padre delle classi di caricamento delle pagine di inserimento delle annotazioni manuali
 * (LoadInserisci):<br>
 * <br>
 * Questa classe espone il metodo loadRichiestaAnnotazioniManuali che centralizza le operazioni di controllo e
 * load dei dati comuni e tutte le action di LoadInserisci che estendono questa classe:<br>
 * <br>
 *
 * Decisioni del GE:<br>
 * - siap.siep.calcolopena.ActLoadNuovoCalcoloPenaBenefici (0284- Amnistia/Indulto)<br>
 * - siap.siep.calcolopena.ActLoadGEDepen (0285 - Depenalizzazione)<br>
 * - siap.siep.calcolopena.ActLoadGEIncost (0286 - Incostituzionalità)<br>
 * <br>
 * 
 * Rideterminazione della pena:<br>
 * - siap.siep.calcolopena.ActLoadAnnotazioniManualiMC (0121-Presofferto)<br>
 * - siap.siep.calcolopena.ActLoadAnnotazioniManualiCompAltroTitolo (0212-Fungibilità MC)<br>
 * - siap.siep.calcolopena.ActLoadAnnotazioniManualiCompSenzaTitolo (0213-Fungibilità PD)<br>
 * <br>
 *
 * @author
 * @version 1.0
 */
@SuppressWarnings("rawtypes")
public class ActLoadInserisciAnnotazioniManuali extends ActionSiap {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	/**
	 * Azione di caricamento della form d'inserimento Annotazioni Manuali Carica
	 * 
	 * @return
	 * @throws F3BException
	 */
	protected String loadRichiestaAnnotazioniManuali(String aMotivoProvvedimento) throws Exception {

		// ==========================================================================
		// Sezione con i controlli preliminari
		// ==========================================================================
		// Controllo Presenza del Fascicolo in Sessione
		if (this.isSessionAttributeNullObj("fascicolo")) {
			return ICostantiFascicoloSiep.REDIRECT_FASCICOLO_RICERCATO + getClass().getName();
		}

		this.isFascicoloSiepDiCompetenza();
		FascicoloSiepModel lFascMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");
		BigDecimal lIdFascicolo = lFascMod.getIdFascicoloSiep();

		// Controllo Validazione Fascicolo
		if (lFascMod.getFlagValidato().equalsIgnoreCase("N")) {
			RedirectTo lRedirigi = new RedirectTo();

			lRedirigi.setPage(IWebConstants.PG_MAIN);
			setRequestAttribute(IWebConstants.MESSAGE_TEXT, "Il Procedimento N." + lFascMod.getChiaveAnno()
					+ "/" + lFascMod.getChiaveProgr() + " non è stato Validato. Impossibile procedere!");
			lRedirigi.setAction("siap.siep.fascicolo.action.ActLoadRicercaFascicoloUnivoco&"
					+ ICostantiFascicoloSiep.CAMPO_AZIONE_CHIAMANTE + "=" + getClass().getName());
			setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi);

			return IWebConstants.PG_MESSAGE;
		}

		// Controllo Fascicolo definito
		if (lFascMod.getDescrStatoFascicolo().equalsIgnoreCase("ARCHIVIATO/DEFINITO")
				// l'indulto può essere iscritto anche su fascicoli Archiviati
				&& !aMotivoProvvedimento.equals("0284")) {
			RedirectTo lRedirigi = new RedirectTo();

			lRedirigi.setPage(IWebConstants.PG_MAIN);
			setRequestAttribute(IWebConstants.MESSAGE_TEXT, "Il Procedimento N." + lFascMod.getChiaveAnno()
					+ "/" + lFascMod.getChiaveProgr() + " risulta Definito. Impossibile procedere!");
			lRedirigi.setAction("siap.siep.fascicolo.action.ActLoadRicercaFascicoloUnivoco&"
					+ ICostantiFascicoloSiep.CAMPO_AZIONE_CHIAMANTE + "=" + getClass().getName());
			setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi);

			return IWebConstants.PG_MESSAGE;
		}

		/******************************* Posizione Giuridica **********************************/
		// ==========================================================================
		// Sezione per il recupero dei dati da visualizzare nella form.
		// - posizione giuridica (luogo di detenzione)
		// - reati
		// - pena complessiva
		// - pena residua in espiazione
		// ==========================================================================
		/*
		 * IPosizioneGiuridica lCtrlPosGiu = SIEPLookupRemote.getPosizioneGiuridicaRemote();
		 * PosizioneGiuridicaModel lPG =
		 * lCtrlPosGiu.ExRicercaPosizioneGiuridicaCorrenteByIdFascicolo(lIdFascicolo);
		 */

		IPosizioneGiuridica lPosCtrl = SIEPLookupRemote.getPosizioneGiuridicaRemote();
		PosizioneGiuridicaLuogoDetenzioneAltraCausaModel lPosLuoAltr = lPosCtrl
				.ExRicercaPosizioneGiuridicaLuogoDetenzioneAltraCausaCorrentiByIdFascicolo(lIdFascicolo);

		if (lPosLuoAltr == null || lPosLuoAltr.getPosizioneGiuridica() == null)
			throw new SIEPException(SIEPException.USER_MESSAGE,
					"Inserire prima la Posizione Giuridica. Impossibile eseguire la richiesta.");

		// ==========================================================================
		// Recupero i reati collegati a richieste al GE di Amnistia/Indulto
		// con o senza anticipazione ma non ancora validati. Tali annotazioni
		// verranno mostrate nella form
		// ==========================================================================
		IAnnotazioneManuale IAnn = SIEPLookupRemote.getAnnotazioneManualeRemote();

		// Recupero le Richieste al GE di Amnistia/Indulto con anticipazione
		// non ancora validate
		Vector lReaAntEffetti = IAnn
				.ExRicercaAnnotazioneManualeRichiesteAnticipazioneAministiaIndultoByIdFascicolo(lIdFascicolo);

		// Recupero le Richieste al GE di Amnistia/Indulto senza anticipazione
		// non ancora validate
		Vector lReaRichiesti = IAnn
				.ExRicercaAnnotazioneManualeRichiesteAministiaIndultoByIdFascicolo(lIdFascicolo);

		// ==========================================================================
		// Recupero tutti i reati collegati al fascicolo e controllo quali sono
		// già associati a una annotazione (validata ò meno) che non sia però
		// una richiesta.
		// Tali reati vengono considerati già elaborati e non devono essere
		// selezionabili nella form. Per questi reati viene posto il FlagVisto='S'
		// in modo che nella form venga spuntato
		// ==========================================================================
		IReato IRea = SIEPLookupRemote.getReatoRemote();
		Vector lReaVect = IRea.ExRicercaReatiByFascicoloNoError(lIdFascicolo);

		//
		for (int i = 0; i < lReaVect.size(); i++) {
			ReatoModel lReato = (ReatoModel) lReaVect.get(i);

			// Ricerca le annotazioni associate al reato ma con flag_app_provv <> A e <> R
			// in modo da scartare quelle delle richieste.
			// Validate o meno (Perchè anche quelle validate?).
			// Se al reato è già associata una annotazione non potrà essere selezionato
			// nella form
			Vector lVectAnnModel = IAnn.ExRicercaAnnotazioniManualiNonRichiesteByIdReato(lReato.getIdReato());
			for (int j = 0; j < lVectAnnModel.size(); j++) {
				AnnotazioneManualeModel lAnnModel = (AnnotazioneManualeModel) lVectAnnModel.get(j);
				if (lAnnModel != null && ((lAnnModel.getCodTipoAnnotazione() != null
						&& lAnnModel.getCodTipoAnnotazione().equals("002")) // INDULTO
						|| (lAnnModel.getCodTipoAnnotazione() != null
								&& lAnnModel.getCodTipoAnnotazione().equals("003")) // AMNISTIA
				)) {
					lReato.setFlagVisto("S");
					break;
				} else {
					lReato.setFlagVisto("N");
				}
			}
		}
		/******************************* Fine Reati già elaborati **********************************/

		String lErrore = null;
		String lAzioneChiamante = null;

		// ==========================================================================
		// Recupero la pena complessiva
		// ==========================================================================
		IPenaComplessiva ICtrlPenCom = SIEPLookupRemote.getPenaComplessivaRemote();
		PenaComplessivaModel lPenComMod = ICtrlPenCom.ExRicercaPenaComplessivaByIdFascicolo(lIdFascicolo);

		if (lPenComMod == null)
			throw new SIEPException(SIEPException.USER_MESSAGE,
					"Pena Complessiva non presente. Impossibile eseguire la richiesta.");

		// ==========================================================================
		// Recupero la pena residua (ultima validata)
		// ==========================================================================
		IPenaResidua lCtrlPenRes = SIEPLookupRemote.getPenaResiduaRemote();
		PenaResiduaModel lUltimaPenaValidata = lCtrlPenRes.ExRicercaPenaResiduaUltimaValidata(lIdFascicolo);

		// if (lUltimaPenaValidata == null)
		// { // se non presente una pena validata utilizzo l'ultima in assoluto NO!
		// // non ha senso! Se non esiste una validata tanto vale ricalcolarla come
		// // se fosse il primo calcolo della pena ricalcolando la data inizio.
		// // Se si usa la data inizio pena dll'ultima non validata si rischia di
		// // agganciare un calcolo intermedio errato effettuato prima della modifica
		// // della posizione giuridica
		// lUltimaPenaValidata = lCtrlPenRes.ExRicercaPenaResiduaCorrenteByFascicoloSiep(lIdFascicolo);
		// }

		if (lUltimaPenaValidata == null) { // Non esiste proprio un pena validata non è mai stato fatto il
											// primo calcolo
											// della pena lo effettuo ora per avere dati da visualizzare sulla
											// form
											// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza
											// siesLogger al posto di LogF3B.getLogger()
			siesLogger.debug("Pena residua assente effettuo il primo calcolo");
			ActCalcoloPenaMain lActCalcoloPenaMain = new ActCalcoloPenaMain();
			CalcoloPenaModel lCalcPenaModel = lActCalcoloPenaMain.calcoloPena(lIdFascicolo, null);

			Date lDataInizioPena = null;
			try {
				lDataInizioPena = lActCalcoloPenaMain.getDataPrimoCalcolo(lIdFascicolo);
			} catch (F3BException e) { // Primo calcolo ma posizioni giuridiche e MC non coerenti, non posso
										// proseguire. Come primo calcolo della pena
				RedirectTo lRedirigi = new RedirectTo();
				lRedirigi.setPage(IWebConstants.PG_MAIN);
				setRequestAttribute(IWebConstants.MESSAGE_TEXT, e.getMessage());
				lRedirigi.setAction("siap.siep.fascicolo.action.ActLoadDettaglioFascicolo&"
						+ ICostantiFascicoloSiep.CAMPO_ID_FASCICOLO_SIEP + "="
						+ ((FascicoloSiepModel) getSessionAttribute("fascicolo")).getIdFascicoloSiep());
				setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi);
				return IWebConstants.PG_MESSAGE;
			}

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Data inizio pena: " + lDataInizioPena);
			try {
				lUltimaPenaValidata = lCalcPenaModel.getPenaDaEspiare(lDataInizioPena, null, null);
			} catch (Exception e) {
				// pezza da togliere serve solo per gestire la catch
				throw new F3BException(e);
			}
		}
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("lUltimaPenaValidata = " + lUltimaPenaValidata);

		// Controllo Esistenza Pena Residua validata se != LIBERO
		// 12-01-2007 aggiunta la posizione Latitante ('Prima')
		if (!lPosLuoAltr.getPosizioneGiuridica().isLibero()
				&& !lPosLuoAltr.getPosizioneGiuridica().getCodPosizioneGiuridica().equals("05")) {
			if (lUltimaPenaValidata == null) {
				// throw new SIEPException(SIEPException.USER_MESSAGE, "Eseguire prima il Calcolo della Pena e
				// validarla. Impossibile eseguire la richiesta.");
				lErrore = "Pena Residua da Espiare Inesistente o non validata. Eseguire Calcolo della pena?";
				lAzioneChiamante = "siap.siep.calcolopena.action.ActLoadCalcoloPena";
			}
		}

		// Ridireziona sul calcolo della pena se non presente validato
		if (lErrore != null) {
			RedirectTo lRedirigi = new RedirectTo();
			lRedirigi.setPage(IWebConstants.PG_MAIN);
			setRequestAttribute(IWebConstants.MESSAGE_TEXT, lErrore);
			lRedirigi.setAction(lAzioneChiamante + "&" + ISIAPCostantiWeb.CAMPO_AZIONE_CHIAMANTE + "="
					+ getClass().getName());
			setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi);

			return IWebConstants.PG_MESSAGE;
		}

		// ==========================================================================
		// Per la visualizzazione dei dati della pena in form, vengono sempre
		// utilizzati i dati dell'ultima pena residua validata se esiste.
		// Se libero può non esistere una pena validata, in questo caso considero
		// la pena complessiva comprensiva di benefici e misure cautelari.
		// n.b. se non libero visualizzo solo decorrenza e scadenza, non i quantum
		// ==========================================================================

		// Da modificare con la nuova versione del calcolo della pena. La pena in
		// espiazione viene ricalcolata da capo recuperando eventuali LA
		// ==========================================================================
		// Recupero la pena a partire dalla quale verranno effettuati i calcoli
		// ==========================================================================

		CalendarModel lCalReclusione = new CalendarModel(); // Reclusione
		CalendarModel lCalArresti = new CalendarModel(); // Arresti
		lCalReclusione.setErrorMsg("-");

		// ==========================================================================
		// Nuova gestione caricamento pena in corso di espiazione
		// Se non ergastolo viene ricalcolata la pena in base ai dati a sistema
		// e passata alla form di visualizzazione
		// ==========================================================================
		Date lDataInizioPena = null;
		if (lUltimaPenaValidata != null)
			lDataInizioPena = lUltimaPenaValidata.getDataInizio();

		if (lPenComMod.getCodTipoPenaDetentiva() != null && lPenComMod.getCodTipoPenaDetentiva() != ""
				&& !(lPenComMod.getCodTipoPenaDetentiva().equals("03")
						|| lPenComMod.getCodTipoPenaDetentiva().equals("04"))) {

			// Effettuo il ricalcolo della pena
			ActCalcoloPenaMain lActCalcoloPenaMain = new ActCalcoloPenaMain();
			CalcoloPenaModel lCalcPenaModel = lActCalcoloPenaMain.calcoloPena(lIdFascicolo, null);
			// n.b. terzo parametro a null per non considerare la LA non ancora computate
			PenaResiduaModel lPenaResiduaCorrente = lCalcPenaModel.getPenaDaEspiare(lDataInizioPena, null,
					null);

			// Recupero la Reclusione
			lCalReclusione = lPenaResiduaCorrente.getQuantumReclusione();
			if (lPenaResiduaCorrente.getImportoMulta() == null)
				lCalReclusione.setImportoMulta(new BigDecimal(0).doubleValue());
			else
				lCalReclusione.setImportoMulta(lPenaResiduaCorrente.getImportoMulta().doubleValue());

			// Recupero li arresti
			lCalArresti = lPenaResiduaCorrente.getQuantumArresto();
			if (lPenaResiduaCorrente.getImportoAmmenda() == null)
				lCalArresti.setImportoAmmenda(new BigDecimal(0).doubleValue());
			else
				lCalArresti.setImportoAmmenda(lPenaResiduaCorrente.getImportoAmmenda().doubleValue());

			// Imposto i giorni di LA
			setRequestAttribute("LAConcesse",
					new BigDecimal(lCalcPenaModel.getLiberazioneAnticipataGiaConcesse()));
			setRequestAttribute("LADaConcedere",
					new BigDecimal(lCalcPenaModel.getLiberazioneAnticipataDaConcedere()));

			// Pena in espiazione
			if (lDataInizioPena != null) {
				lCalReclusione.setErrorMsg("Non Libero");
				lCalReclusione.setDataInizio(lUltimaPenaValidata.getDataInizio());
				lCalReclusione.setDataFine(lUltimaPenaValidata.getDataFine());
			} else {
				lCalReclusione.setErrorMsg("Libero - PENA RESIDUA");
			}
		} else {
			// Ergastolo
			setRequestAttribute("LAConcesse", new BigDecimal(0));
			setRequestAttribute("LADaConcedere", new BigDecimal(0));

			if (lDataInizioPena != null) {
				lCalReclusione.setErrorMsg("Non Libero");
				lCalReclusione.setDataInizio(lUltimaPenaValidata.getDataInizio());
				lCalReclusione.setDataFine(lUltimaPenaValidata.getDataFine());
			} else {
				lCalReclusione.setErrorMsg("Libero - PENA RESIDUA");
			}
		}

		// ==========================================================================
		// Vecchia versione delle gestione dei dati della pena
		// - se libero vengono caricati i quantum di pena da espiare recuperati
		// dall'ultima pena residua validata. Se non esiste pena validata viene
		// recuperata la pena in sentenza (che però non tiene conto dei benefici
		// e della Misure Cautelari in sentenza)
		// - se pena in espiazione non vengono passati i quantum, ma solo la data
		// inizio e la data fine. Nella form viene visualizzato il quantum calcolato
		// al volo tra la data di sistema e il fine pena previsto.
		// ==========================================================================
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("lUltimaPenaValidata = " + lUltimaPenaValidata);
		// Libero
		// if( lPosLuoAltr.getPosizioneGiuridica().isLibero() )
		// {
		// if(lUltimaPenaValidata != null)
		// {
		// lCalReclusione.setErrorMsg("Libero - PENA RESIDUA"); //Patch per gestire il titolo sulla JSP
		//
		// if (lUltimaPenaValidata.getImportoMulta() == null)
		// lCalReclusione.setImportoMulta(new BigDecimal(0).doubleValue());
		// else
		// lCalReclusione.setImportoMulta(lUltimaPenaValidata.getImportoMulta().doubleValue());
		//
		// lCalReclusione.setNumAnni (lUltimaPenaValidata.getNumAnniReclusione());
		// lCalReclusione.setNumMesi (lUltimaPenaValidata.getNumMesiReclusione());
		// lCalReclusione.setNumGiorni (lUltimaPenaValidata.getNumGiorniReclusione());
		//
		// if (lUltimaPenaValidata.getImportoAmmenda() == null)
		// lCalArresti.setImportoAmmenda(new BigDecimal(0).doubleValue());
		// else
		// lCalArresti.setImportoAmmenda(lUltimaPenaValidata.getImportoAmmenda().doubleValue());
		//
		// lCalArresti.setNumAnni (lUltimaPenaValidata.getNumAnniArresto());
		// lCalArresti.setNumMesi (lUltimaPenaValidata.getNumMesiArresto());
		// lCalArresti.setNumGiorni (lUltimaPenaValidata.getNumGiorniArresto());
		// }
		// else
		// {
		// // SE NON ESISTE PENA RESIDUA VALIDATA CONSIDERA PENA COMPLESSIVA
		// // modifica 12/2006 per tener conto anche di benefici e misure cautelari
		// // in sentenza
		// ActCalcoloPenaMain lCalcoloPenaMain = new ActCalcoloPenaMain();
		// CalcoloPenaModel lCalcoloPenaModel = lCalcoloPenaMain.calcoloPena(lIdFascicolo, null);
		// PenaResiduaModel lPenaRideterminata = lCalcoloPenaModel.getPenaDaEspiare(null, null);
		//
		// lCalReclusione.setErrorMsg("Libero - PENA COMPLESSIVA"); //Patch per gestire il titolo sulla JSP
		//
		// if (lPenaRideterminata.getImportoMulta()== null)
		// lCalReclusione.setImportoMulta(new BigDecimal(0).doubleValue());
		// else
		// lCalReclusione.setImportoMulta(lPenaRideterminata.getImportoMulta().doubleValue());
		//
		// lCalReclusione.setNumAnni (lPenaRideterminata.getNumAnniReclusione());
		// lCalReclusione.setNumMesi (lPenaRideterminata.getNumMesiReclusione());
		// lCalReclusione.setNumGiorni (lPenaRideterminata.getNumGiorniReclusione());
		//
		// if (lPenaRideterminata.getImportoAmmenda()== null)
		// lCalArresti.setImportoAmmenda(new BigDecimal(0).doubleValue());
		// else
		// lCalArresti.setImportoAmmenda(lPenaRideterminata.getImportoAmmenda().doubleValue());
		//
		// lCalArresti.setNumAnni (lPenaRideterminata.getNumAnniArresto());
		// lCalArresti.setNumMesi (lPenaRideterminata.getNumMesiArresto());
		// lCalArresti.setNumGiorni (lPenaRideterminata.getNumGiorniArresto());
		//
		//// if (lPenComMod.getImportoMulta()== null)
		//// lCal1.setImportoMulta(new BigDecimal(0).doubleValue());
		//// else
		//// lCal1.setImportoMulta(lPenComMod.getImportoMulta().doubleValue());
		////
		//// lCal1.setNumAnni (lPenComMod.getNumAnniReclusione());
		//// lCal1.setNumMesi (lPenComMod.getNumMesiReclusione());
		//// lCal1.setNumGiorni (lPenComMod.getNumGiorniReclusione());
		////
		//// if (lPenComMod.getImportoAmmenda()== null)
		//// lCal2.setImportoAmmenda(new BigDecimal(0).doubleValue());
		//// else
		//// lCal2.setImportoAmmenda(lPenComMod.getImportoAmmenda().doubleValue());
		////
		//// lCal2.setNumAnni(lPenComMod.getNumAnniArresto());
		//// lCal2.setNumMesi(lPenComMod.getNumMesiArresto());
		//// lCal2.setNumGiorni(lPenComMod.getNumGiorniArresto());
		// }
		// }
		// else //non libero
		// { // pena in espiazine passo solo decorrenza scadenza
		// lCalReclusione.setErrorMsg("Non Libero");
		// lCalReclusione.setDataInizio(lUltimaPenaValidata.getDataInizio());
		// lCalReclusione.setDataFine(lUltimaPenaValidata.getDataFine());
		// }

		/******************************* Fine Pena Residua ****************************/
		// ======================================================================
		// Se non è un computo (presofferto, fungibilità), verifico se provengo
		// da Aggiungi e in questo caso recupero l'ordinanza e le annotazioni
		// da passare alla form
		// ======================================================================
		if (!"0121".equals(aMotivoProvvedimento) // 0121-computo Misura Cautelare stesso Reato art. 657 c.p.p.
				&& !"0213".equals(aMotivoProvvedimento) // 0213-computo Pena Detentiva Espiata per Altro Reato
														// (fungibilità) art. 657 c.p.p.
				&& !"0212".equals(aMotivoProvvedimento) // 0212-computo Misura Cautelare Altro Reato art. 657
														// c.p.p.
		) {
			popolaRequestPerBenefici(lIdFascicolo, aMotivoProvvedimento);
		}

		setRequestAttribute("posizioneluogoaltra", lPosLuoAltr);
		setRequestAttribute("PenRes1", lCalReclusione); // Reclusione - Ultima validata o Complessiva
		setRequestAttribute("PenRes2", lCalArresti); // Arresti
		setRequestAttribute("reati", lReaVect);
		setRequestAttribute("ReaAntEffetti", lReaAntEffetti);
		setRequestAttribute("ReaRichiesti", lReaRichiesti);
		setRequestAttribute("contesto", "benefici");
		setRequestAttribute("PenaComplessiva", lPenComMod);
		setRequestAttribute("PenaResidua", lUltimaPenaValidata);

		// Nel caso ritorna null, tutti i controlli sono passati
		return null;
	}

	/**
	 * Nel caso di DECISIONI DEL GE, è presente sulla pagina di conferma il tasto 'Aggiungi' per poter legare
	 * più annotazioni alla stessa ordinanza. In questo caso recupero l'eventuale ordinanza e una qualsiasi
	 * annotazione già inserita per passarli alla form di inserimento in modo di poter rendere l'inserimento
	 * della nuova annotazione coerente con quella precedente.
	 * 
	 * @param lIdFascicolo
	 * @param aMotivoProvvedimento
	 * @throws Exception
	 */
	private void popolaRequestPerBenefici(BigDecimal lIdFascicolo, String aMotivoProvvedimento)
			throws Exception {

		// ==========================================================================
		// Recupero i dati dell'ordinanza del GE se provengo da 'Aggiungi' e sto
		// inserendo una seconda annotazione.
		// ==========================================================================
		IEvento ICtrlEve = SICOLookupRemote.getEventoRemote();

		EventoModel lEveModPar = new EventoModel();

		lEveModPar.setCodTipoEvento("01");
		lEveModPar.setCodTipoProvvedimento("03");
		lEveModPar.setCodMotivo(aMotivoProvvedimento);
		lEveModPar.setFasSieIdFascicoloSiep(lIdFascicolo);

		// EventoModel lEveMod = ICtrlEve.ExRicercaUltimoTipoEventoByIdFascicolo(lEveModPar);
		EventoModel lEveMod = ICtrlEve.ExRicercaEventoNonRegistrato(lEveModPar);

		// ==========================================================================
		// Recupera l'annotazione associata all'ordinanza.
		// Serve per la visualizzazione dei dati in maschera relativi alla
		// declaratoria
		// ==========================================================================
		AnnotazioneManualeModel lAnnGE = null;
		if (lEveMod != null && lEveMod.getIdEvento() != null) {
			IAnnotazioneManuale IAnn = SIEPLookupRemote.getAnnotazioneManualeRemote();
			lAnnGE = IAnn.ExRicercaAnnotazioniManualiByIdEvento(lEveMod.getIdEvento());
		}

		setRequestAttribute("OrdinanzaGEAnn", lAnnGE);
		setRequestAttribute("OrdinanzaGEEve", lEveMod);

		// ==========================================================================
		// Recupero l'ufficio emittente
		// Tutti e soli gli uffici che possono emettere la declaratoria
		// ==========================================================================
		// MEV_66: aggiunti 5 uffici che possono emettere la declaratoria
		String[] aFiltroUffici = { "CAP", "CAS", "CASAP", "GIP", "DIB", "TRIBSD", "GUPM", "CAPSM", "DIBM", "GIPM", "GP" };

		Option lOption = new Option(DecodificheManager.getInstance().getTipoUfficio(), true);
		lOption.setValueBlankItem("-");
		lOption.setFilter(aFiltroUffici);
		if (lEveMod != null && lEveMod.getIdEvento() != null && lEveMod.getCodUfficioEmittente() != null) {
			UfficioModel lUfficioEmittente = getUfficioByCodUfficio(lEveMod.getCodUfficioEmittente());
			lOption.setSelected(lUfficioEmittente.getCodTipoUfficio());
		}

		setRequestAttribute("UfficioEmittente", "" + lOption);

		// ==========================================================================
		// new Recupero tutte le richieste al GE validate da precaricare hidden
		// nella form (solo nel caso di amnistia/indulto
		// ==========================================================================
		AnnotazioneManualeModel lAnnPerRicerca = new AnnotazioneManualeModel();
		lAnnPerRicerca.setFasSieIdFascicoloSiep(lIdFascicolo);
		lAnnPerRicerca.setCodTipoAnnotazione("002"); // ricerca sia 002 che 003
		// ==========================================================================
		// new Recupero tutte le richieste al GE validate da precaricare hidden
		// nella form anche per incostituzionalità e depenalizzazione
		// ==========================================================================
		if (aMotivoProvvedimento.compareTo("0285") == 0) // Depenalizzazione
			lAnnPerRicerca.setCodTipoAnnotazione("004");
		if (aMotivoProvvedimento.compareTo("0286") == 0) // Incostituzionalità
			lAnnPerRicerca.setCodTipoAnnotazione("013");

		lAnnPerRicerca.setFlagAppProvvisoria("RICHIESTE"); // FLAG_APP_PROVVISORIA='R' OR
															// FLAG_APP_PROVVISORIA='A'
		lAnnPerRicerca.setFlagValidato("S");

		Vector lListaRichieste = new Vector();
		try {
			IAnnotazioneManuale IAnn = SIEPLookupRemote.getAnnotazioneManualeRemote();
			lListaRichieste = IAnn.ExRicercaAnnotazioneManualeGenerico(lAnnPerRicerca);
		} catch (F3BException ex) {
			if (ex.getErrorCode() == SIEPException.EX_NOT_FOUND) {
				// non faccio nulla
			} else
				throw ex;
		}

		setRequestAttribute("RichiesteAlGE", lListaRichieste);
	}

}