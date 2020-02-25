package siap.siep.calcolopena.action;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.Vector;

import org.apache.log4j.Logger;

import siap.sico.calendar.model.CalendarModel;
import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.decodifiche.model.DecodificheModel;
import siap.sico.magistratocompetente.controller.IMagistratoCompetente;
import siap.sico.magistratocompetente.model.MagistratoCompetenteMagistratoModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siep.SIEPException;
import siap.siep.avvocato.controller.IAvvocato;
import siap.siep.calcolopena.model.CalcoloPenaModel;
import siap.siep.fascicolo.action.ICostantiFascicoloSiep;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.penacomplessiva.controller.IPenaComplessiva;
import siap.siep.penacomplessiva.model.PenaComplessivaModel;
import siap.siep.penaresidua.controller.IPenaResidua;
import siap.siep.penaresidua.model.PenaResiduaModel;
import siap.siep.posizione.controller.IPosizioneGiuridica;
import siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel;
import siap.siep.util.SIEPLookupRemote;
import siap.web.ISIAPCostantiWeb;
import f3b.log.LogF3B;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;
import f3b.web.html.Option;

/**
 * <p>
 * Title: ActLoadRidetPenaAltro
 * </p>
 * <p>
 * Description: Azione Load Rideterminazione Pena Altro
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author unascribed
 * @version 1.0
 */
public class ActLoadRidetPenaAltro extends ActionSiap {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	/**
	 * Azione di caricamento della form d'inserimento della Rideterminazione Pena 'Altro'.
	 * 
	 * @return Nome della pagina JSP su cui posizionarsi al termine dell'elaborazione
	 * @throws F3BException
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String processRequest() throws Exception {

		// ===============================================
		// Controllo Presenza del Fascicolo in Sessione
		// ===============================================
		if (this.isSessionAttributeNullObj("fascicolo")) {
			return ICostantiFascicoloSiep.REDIRECT_FASCICOLO_RICERCATO + getClass().getName();
		}
		FascicoloSiepModel lFascMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");
		BigDecimal lIdFascicolo = lFascMod.getIdFascicoloSiep();

		this.isFascicoloSiepDiCompetenza();

		// ===============================================
		// Controllo Validazione Fascicolo
		// ===============================================
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

		// ===============================================
		// Controllo Fascicolo definito
		// ===============================================
		if (lFascMod.getDescrStatoFascicolo().equalsIgnoreCase("ARCHIVIATO/DEFINITO")) {
			RedirectTo lRedirigi = new RedirectTo();

			lRedirigi.setPage(IWebConstants.PG_MAIN);
			setRequestAttribute(IWebConstants.MESSAGE_TEXT, "Il Procedimento N." + lFascMod.getChiaveAnno()
					+ "/" + lFascMod.getChiaveProgr() + " risulta Definito. Impossibile procedere!");
			lRedirigi.setAction("siap.siep.fascicolo.action.ActLoadRicercaFascicoloUnivoco&"
					+ ICostantiFascicoloSiep.CAMPO_AZIONE_CHIAMANTE + "=" + getClass().getName());
			setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi);

			return IWebConstants.PG_MESSAGE;
		}

		this.isEventoNonValidato();

		// ==========================================================================
		// Recupero la Posizione Giuridica
		// ==========================================================================
		IPosizioneGiuridica lPosCtrl = SIEPLookupRemote.getPosizioneGiuridicaRemote();
		PosizioneGiuridicaLuogoDetenzioneAltraCausaModel lPosLuoAltr = lPosCtrl
				.ExRicercaPosizioneGiuridicaLuogoDetenzioneAltraCausaCorrentiByIdFascicolo(lIdFascicolo);

		if (lPosLuoAltr == null || lPosLuoAltr.getPosizioneGiuridica() == null)
			throw new SIEPException(SIEPException.USER_MESSAGE,
					"Inserire prima la Posizione Giuridica. Impossibile eseguire la richiesta.");

		// ==========================================================================
		// Recupero la Pena Complessiva
		// ==========================================================================
		IPenaComplessiva ICtrlPenCom = SIEPLookupRemote.getPenaComplessivaRemote();
		PenaComplessivaModel lPenComMod = ICtrlPenCom.ExRicercaPenaComplessivaByIdFascicolo(lIdFascicolo);

		if (lPenComMod == null)
			throw new SIEPException(SIEPException.USER_MESSAGE,
					"Pena Complessiva non presente. Impossibile eseguire la richiesta.");

		// ==========================================================================
		// Recupero i dati della pena da visualizzare nella form.
		// Se non libero deve esistere una pena residua Validata
		// Se libero può non esistere, in questo caso passo i dati della Pena complessiva
		// Recupero l'ultima pena residua validata
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
		siesLogger.debug("Ultima pena residua VALIDATA = " + lUltimaPenaValidata);

		// Controllo Esistenza Pena Residua validata se != LIBERO
		// 12-01-2007 aggiunta la posizione Latitante ('Prima')
		if (!lPosLuoAltr.getPosizioneGiuridica().isLibero()
				&& !lPosLuoAltr.getPosizioneGiuridica().getCodPosizioneGiuridica().equals("05")) {
			if (lUltimaPenaValidata == null) {
				String lErrore = null;
				String lAzioneChiamante = null;
				lErrore = "Pena Residua da Espiare Inesistente o non validata. Eseguire Calcolo della pena?";
				lAzioneChiamante = "siap.siep.calcolopena.action.ActLoadCalcoloPena";
				// Ridireziona sul calcolo della pena se non presente validato
				RedirectTo lRedirigi = new RedirectTo();
				lRedirigi.setPage(IWebConstants.PG_MAIN);
				setRequestAttribute(IWebConstants.MESSAGE_TEXT, lErrore);
				lRedirigi.setAction(lAzioneChiamante + "&" + ISIAPCostantiWeb.CAMPO_AZIONE_CHIAMANTE + "="
						+ getClass().getName());
				setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi);

				return IWebConstants.PG_MESSAGE;
			}
		}

		// ==========================================================================
		// Recupero Reclusione e Arresto
		// ==========================================================================
		CalendarModel lCalReclusione = new CalendarModel();
		CalendarModel lCalArresti = new CalendarModel();
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
		// Libero
		// if( lPosLuoAltr.getPosizioneGiuridica().isLibero() )
		// {
		// // SE ESISTE PENA RESIDUA VALIDATA
		// if(lUltimaPenaValidata != null)
		// {
		// // Reclusione
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
		// // Arresto
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
		// // Reclusione
		// lCalReclusione.setErrorMsg("Libero - PENA COMPLESSIVA"); //Patch per gestire il titolo sulla JSP
		//
		// if (lPenComMod.getImportoMulta()== null)
		// lCalReclusione.setImportoMulta(new BigDecimal(0).doubleValue());
		// else
		// lCalReclusione.setImportoMulta(lPenComMod.getImportoMulta().doubleValue());
		//
		// lCalReclusione.setNumAnni (lPenComMod.getNumAnniReclusione());
		// lCalReclusione.setNumMesi (lPenComMod.getNumMesiReclusione());
		// lCalReclusione.setNumGiorni (lPenComMod.getNumGiorniReclusione());
		//
		// // Arresti
		// if (lPenComMod.getImportoAmmenda()== null)
		// lCalArresti.setImportoAmmenda(new BigDecimal(0).doubleValue());
		// else
		// lCalArresti.setImportoAmmenda(lPenComMod.getImportoAmmenda().doubleValue());
		//
		// lCalArresti.setNumAnni (lPenComMod.getNumAnniArresto());
		// lCalArresti.setNumMesi (lPenComMod.getNumMesiArresto());
		// lCalArresti.setNumGiorni (lPenComMod.getNumGiorniArresto());
		// }
		// }
		// else //non libero
		// {
		// lCalReclusione.setErrorMsg("Non Libero");
		// lCalReclusione.setDataFine (lUltimaPenaValidata.getDataFine()); //????
		// lCalReclusione.setDataInizio (lUltimaPenaValidata.getDataInizio()); //????
		// }

		// ==========================================================================
		// Passo i dati alla form
		// ==========================================================================
		setRequestAttribute("posizioneluogoaltra", lPosLuoAltr);
		setRequestAttribute("PenRes1", lCalReclusione); // Reclusione
		setRequestAttribute("PenRes2", lCalArresti); // Arresti
		setRequestAttribute("PenaComplessiva", lPenComMod);
		setRequestAttribute("PenaResidua", lUltimaPenaValidata);

		// ==========================================================================
		// Recupero i dati per i destinatari
		// ==========================================================================
		// Avvocato
		IAvvocato lAvvCtrl = SIEPLookupRemote.getAvvocatoRemote();
		Vector lAvvocati = lAvvCtrl.ExRicercaAvvocatiByFascicolo(lFascMod.getIdFascicoloSiep());
		setRequestAttribute("avvocati", lAvvocati);

		// ricerca magistrato competente
		IMagistratoCompetente lMagComp = SICOLookupRemote.getMagistratoCompetenteRemote();
		MagistratoCompetenteMagistratoModel lMagMod = lMagComp
				.ExRicercaMagistratoCompetenteByFascicolo(lFascMod.getIdFascicoloSiep());
		if (lMagMod != null)
			setRequestAttribute("magistratocompetente", lMagMod);

		// ==========================================================================
		// Combo codice motivo
		// ==========================================================================
		Option lOption = new Option(DecodificheManager.getInstance().getRideterminazionePenaAltro());
		setRequestAttribute("oggetto", "" + lOption);

		// ==============================================================================
		// ==============================================================================
		// ==============================================================================
		// ==============================================================================
		// NUOVO CODICE
		//
		// New modifiche da apportare per la nuova gestione
		// - la combo oggetti viene sdoppiata
		// - prevista sezione per specificare i provvedimenti di altro ufficio
		// ==========================================================================
		// Previsti 2 casi: D'ufficio - In esecuzione di provvedimento altro ufficio
		// Se 'In esecuzione di provvedimento altro ufficio' previsti 3 casi
		// Su provvedimento altro ufficio 3 casi: Altra Autorità
		// Giudice Esecuzione
		// Giudice Sorveglianza
		// Il contenuto della combo oggetto varia in funzione dell'ufficio, quindi
		// sono presenti in tutto 4 casi per la combo oggetto.
		// Inoltre cambia anche il contenuto della combo Autorità emittente

		if (!isRequestParameterNullObj("codMotivo")) {
			String codMotivo = getRequestStringParameter("codMotivo");
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("codMotivo = " + codMotivo);
			// Provengo dalle Decisioni della Sorveglianza. In questo caso dovo
			// caricare una form semplificata. Il codice motivo è già caricato.
		}

		// =======================================================================
		// Carico la combo con i soli codici motivo previsti per i provvedimenti
		// di questo ufficio
		// =======================================================================
		Option lOptionDufficio = new Option(
				DecodificheManager.getInstance().getRideterminazionePenaAltroDufficio());
		setRequestAttribute("oggettoDufficio", "" + lOptionDufficio);

		// =======================================================================
		// Carico la combo con i soli codici motivo previsti per i provvedimenti
		// di altro ufficio
		// =======================================================================
		Option lOptionAltroUfficio = new Option(
				DecodificheManager.getInstance().getRideterminazionePenaAltroAUfficio());
		setRequestAttribute("oggettoAltroUfficio", "" + lOptionAltroUfficio);

		// Oggetti per il caricamento delle combo Altra Autorità
		Option lOptionSorv = new Option(DecodificheManager.getInstance().getTipoProvvSorveglianza(), "-");
		setRequestAttribute("tipoprovvedimento", "" + lOptionSorv);

		// ==========================================================================
		// Vettore con i tipo emittenti 'Provvedimento Altra Autorità'
		// ==========================================================================
		Collection lEmessoDa = new Vector();
		lEmessoDa.add(new DecodificheModel("0000", "-", "", "", "", "", "", "", ""));
		lEmessoDa.add(new DecodificheModel("0001", "Altra Autorità", "", "", "", "", "", "", ""));
		lEmessoDa.add(new DecodificheModel("0002", "Giudice Esecuzione", "", "", "", "", "", "", ""));
		lEmessoDa.add(new DecodificheModel("0003", "Giudice Sorveglianza", "", "", "", "", "", "", ""));
		setRequestAttribute("emessoDa", lEmessoDa); // Provvedimento emesso da

		// ==========================================================================
		// Carico le 3 Collection contenenti i codici motivo per i per i tre casi
		// Altro Ufficio
		// ==========================================================================
		// ==========================================================================
		// Autorità da caricare dentro la combo. In pratica l'oggetto 'autorita'
		// passato alla jsp è un ArrayList di tante collection quante sono gli
		// oggetti della collection emessoDa. Il collegamento tre le due collection
		// è puramente posizionale. Se si seleziona il primo elemento della 'emessoDa',
		// le tipologie di Uffici vengono recuperate dalla prima collection dell'Array
		// 'autorita'. Stesso per i codici motivo (oggettoProvvedimento). Quindi
		// vanno caricate nell'ordine giusto
		// ==========================================================================
		ArrayList lAutorita = new ArrayList();
		Collection lAutorVuota = new Vector();
		lAutorVuota.add(new DecodificheModel("-", "-               ", "", "", "", "", "", "", ""));
		lAutorita.add(lAutorVuota);
		// MEV_66: aggiunte autorità alle tre collezioni lato DB
		lAutorita.add(DecodificheManager.getInstance().getAutoritaRdpAltro());
		lAutorita.add(DecodificheManager.getInstance().getAutoritaRdpGE());
		Collection destinatari = DecodificheManager.getInstance().getAutoritaRdpSorv();
		Iterator i = destinatari.iterator();
		while (i.hasNext()) {
			DecodificheModel dm = (DecodificheModel) i.next();
			if ("UDSM".equalsIgnoreCase(dm.getCodiceAlternativo())) {
				// Per SIEP la descrizione UDSM cambia da
				// "Ufficio di Sorveglianza presso il Tribunale per minorenni"
				// in "Magistrato di Sorveglianza per i minorenni"
				dm.setDescription("Magistrato di Sorveglianza per i minorenni");
				break;
			}
		}
		lAutorita.add(destinatari);
		// FINE MEV_66
		setRequestAttribute("autorita", lAutorita);

		// ==========================================================================
		// Carico le 3 Collection contenenti i codici motivo per i per i tre casi
		// Altro Ufficio
		// ==========================================================================
		ArrayList lOggetto = new ArrayList();
		Collection lOggettoVuota = new Vector();
		lOggettoVuota.add(new DecodificheModel("-", "-               ", "", "", "", "", "", "", ""));
		lOggetto.add(lOggettoVuota);
		lOggetto.add(DecodificheManager.getInstance().getRideterminazionePenaAltroAUfficio());
		lOggetto.add(DecodificheManager.getInstance().getRideterminazionePenaAltroGE());
		lOggetto.add(DecodificheManager.getInstance().getRideterminazionePenaAltroSORV());
		setRequestAttribute("oggettoProvvedimento", lOggetto);

		// Autorità esterna
		lOption = new Option(DecodificheManager.getInstance().getTipoAutorita(), "22");
		setRequestAttribute("autoritaEsternaAvv", "" + lOption);

		// Autorità esterna E
		Option lOptionAutoritaE = null;
		lOptionAutoritaE = new Option(DecodificheManager.getInstance().getTipoAutorita(), "22");
		setRequestAttribute("autoritaEsternaE", "" + lOptionAutoritaE);

		// Autorità esterna altra
		Option lOptionAutoritaAltra = null;
		lOptionAutoritaAltra = new Option(DecodificheManager.getInstance().getTipoAutorita());
		setRequestAttribute("autoritaEsternaAltra", "" + lOptionAutoritaAltra);

		// String lPage=IWebConstants.ROOT_DIR+"/files/siap/siep/calcolopena/LoadRidetPenaAltro.jsp";
		String lPage = IWebConstants.ROOT_DIR + "/files/siap/siep/calcolopena/LoadRidetPenaAltro_new.jsp";

		return lPage;
	}

}