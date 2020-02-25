package siap.siep.richiesta.action;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;
import siap.sico.calendar.model.CalendarModel;
import siap.sico.web.ActionSiap;
import siap.siep.SIEPException;
import siap.siep.annotazionemanuale.controller.IAnnotazioneManuale;
import siap.siep.annotazionemanuale.model.AnnotazioneManualeModel;
import siap.siep.calcolopena.action.ActCalcoloPenaMain;
import siap.siep.calcolopena.controller.ICalcoloPena;
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
 * Classe madre delle Action di caricamento delle form di inserimento delle richieste al GE di
 * rideterminazione pena: - INCOSTITUZIONALITA - ActLoadRichiestaIncostituzionalita - DEPANALIZZAZIONE -
 * ActLoadRichiestaDepenalizzazione - AMNISTIA/INDULTO - ActLoadRichiestaAmnistiaIndulto La classe espone il
 * metodo loadRichiestaGE che carica sulla request tutti i dati che devono essere visualizzati dalla form di
 * inserimento. - Posizione giuridice - pena residua
 */
@SuppressWarnings("rawtypes")
public class ActLoadRichiestaGE extends ActionSiap implements ICostantiRichiesta {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	protected String checkFascicolo() throws F3BException {
		String lPage = null;

		// Controllo Presenza del Fascicolo in Sessione
		if (this.isSessionAttributeNullObj("fascicolo")) {
			return ICostantiFascicoloSiep.REDIRECT_FASCICOLO_RICERCATO + getClass().getName();
		}
		this.isFascicoloSiepDiCompetenza();

		if (this.isFascicoloNonValidato())
			return IWebConstants.PG_MESSAGE;

		if (this.isFascicoloArchiviatoDefinito())
			return IWebConstants.PG_MESSAGE;

		return lPage;

	}

	/**
	 * Questo metodo verifica se esistono i prerequisiti per poter inserire una richiesta (fascicolo
	 * validato....). Carica i dati da visualizzare nelle form di inserimento della richiesta - Pena
	 * complessiva - Posizione Giuridica - Pena attualmente in espiazione
	 * 
	 * @return eventuale pagina di segnalazione errore o warning altrimenti nulla
	 * @throws F3BException
	 */
	protected String loadRichiestaGE(String aTipoAnnotazione) throws F3BException {
		String lReturnPage = null;

		FascicoloSiepModel lFascMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");
		BigDecimal lIdFascicolo = lFascMod.getIdFascicoloSiep();

		String lErrore = null;
		String lAzioneChiamante = null;

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
		// Recupero l'ultima pena validata
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

		ICalcoloPena lCalPen = SIEPLookupRemote.getCalcoloPenaRemote();
		boolean lIsCalcoloPenaAbInitio = lCalPen.ExIsCalcoloPenaAbInizio(lIdFascicolo);
		setRequestAttribute("isCalcoloPenaAbInitio", new Boolean(lIsCalcoloPenaAbInitio));

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
		// else if (lUltimaPenaValidata == null && !lIsCalcoloPenaAbInitio)
		// {
		// // Se Libero, non abInizio e pena vlidata assente, carico l'ultima pena
		// // a sistema
		// lUltimaPenaValidata = lCtrlPenRes.ExRicercaPenaResiduaCorrenteByFascicoloSiep(lIdFascicolo);
		// }

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
		// Carica i dati relativi alla pena attuale
		//
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
			PenaResiduaModel lPenaResiduaCorrente = null;

			try {
				lPenaResiduaCorrente = lCalcPenaModel.getPenaDaEspiare(lDataInizioPena, null, null);
			} catch (Exception e) {
				// pezza da togliere inserita per gestire le eccezioni in caso di dati a
				// sistema non congruenti
				throw new F3BException(e);
			}

			// Recupero la Reclusione
			lCalReclusione = lPenaResiduaCorrente.getQuantumReclusione();
			if (lPenaResiduaCorrente.getImportoMulta() == null)
				lCalReclusione.setImportoMulta(new BigDecimal(0).doubleValue());
			else
				lCalReclusione.setImportoMulta(lPenaResiduaCorrente.getImportoMulta().doubleValue());

			// Recupero gli arresti
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
				lCalReclusione.setErrorMsg("Libero");
			}
		} else {
			// Ergastolo
			// Imposto i giorni di LA
			setRequestAttribute("LADaConcedere", new BigDecimal(0));
			setRequestAttribute("LAConcesse", new BigDecimal(0));

			if (lDataInizioPena != null) {
				lCalReclusione.setErrorMsg("Non Libero");
				lCalReclusione.setDataInizio(lUltimaPenaValidata.getDataInizio());
				lCalReclusione.setDataFine(lUltimaPenaValidata.getDataFine());
			} else {
				lCalReclusione.setErrorMsg("Libero");
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
		// if (lPosLuoAltr.getPosizioneGiuridica().isLibero())
		// {
		// lCalReclusione.setErrorMsg("Libero");
		// if (lUltimaPenaValidata != null)
		// {
		// // SE ESISTE UNA PENA RESIDUA VALIDATA, CONSIDERO TALE PENA
		// if (lUltimaPenaValidata.getImportoMulta() == null)
		// lCalReclusione.setImportoMulta(new BigDecimal(0).doubleValue());
		// else
		// lCalReclusione.setImportoMulta(lUltimaPenaValidata.getImportoMulta().doubleValue());
		//
		// lCalReclusione.setNumAnni(lUltimaPenaValidata.getNumAnniReclusione());
		// lCalReclusione.setNumMesi(lUltimaPenaValidata.getNumMesiReclusione());
		// lCalReclusione.setNumGiorni(lUltimaPenaValidata.getNumGiorniReclusione());
		//
		// if (lUltimaPenaValidata.getImportoAmmenda() == null)
		// lCalArresti.setImportoAmmenda(new BigDecimal(0).doubleValue());
		// else
		// lCalArresti.setImportoAmmenda(lUltimaPenaValidata.getImportoAmmenda().doubleValue());
		//
		// lCalArresti.setNumAnni(lUltimaPenaValidata.getNumAnniArresto());
		// lCalArresti.setNumMesi(lUltimaPenaValidata.getNumMesiArresto());
		// lCalArresti.setNumGiorni(lUltimaPenaValidata.getNumGiorniArresto());
		// }
		// else
		// {
		// // SE NON ESISTE PENA RESIDUA VALIDATA CONSIDERA PENA COMPLESSIVA
		// if (lPenComMod.getImportoMulta() == null)
		// lCalReclusione.setImportoMulta(new BigDecimal(0).doubleValue());
		// else
		// lCalReclusione.setImportoMulta(lPenComMod.getImportoMulta().doubleValue());
		//
		// lCalReclusione.setNumAnni(lPenComMod.getNumAnniReclusione());
		// lCalReclusione.setNumMesi(lPenComMod.getNumMesiReclusione());
		// lCalReclusione.setNumGiorni(lPenComMod.getNumGiorniReclusione());
		//
		// if (lPenComMod.getImportoAmmenda() == null)
		// lCalArresti.setImportoAmmenda(new BigDecimal(0).doubleValue());
		// else
		// lCalArresti.setImportoAmmenda(lPenComMod.getImportoAmmenda().doubleValue());
		//
		// lCalArresti.setNumAnni(lPenComMod.getNumAnniArresto());
		// lCalArresti.setNumMesi(lPenComMod.getNumMesiArresto());
		// lCalArresti.setNumGiorni(lPenComMod.getNumGiorniArresto());
		// }
		// }
		// else //non libero
		// {
		// lCalReclusione.setErrorMsg("Non Libero");
		// //SE NON ESISTE PENA RESIDUA VALIDATA CONSIDERA PENA COMPLESSIVA
		// if (lUltimaPenaValidata != null)
		// {
		// lCalReclusione.setDataFine(lUltimaPenaValidata.getDataFine());
		// lCalReclusione.setDataInizio(lUltimaPenaValidata.getDataInizio());
		// }
		// else
		// { //err sono già uscito con segnalazione di errore
		// lCalReclusione.setDataFine(lPenComMod.getDataFine());
		// lCalReclusione.setDataInizio(lPenComMod.getDataInizio());
		// }
		// }

		/******************************* Fine Pena Residua ****************************/
		// Non esistono ANN devo inserire un nuovo Provvedimento
		Vector lReaVect = checkReati(aTipoAnnotazione, lIdFascicolo);

		setRequestAttribute("posizioneluogoaltra", lPosLuoAltr);
		setRequestAttribute("PenRes1", lCalReclusione);
		setRequestAttribute("PenRes2", lCalArresti);

		setRequestAttribute("reati", lReaVect);

		setRequestAttribute("PenaComplessiva", lPenComMod);
		setRequestAttribute("PenaResidua", lUltimaPenaValidata);

		if (!isRequestParameterNullObj("isAnticipazione")) {
			String isAnticipazione = getRequestStringParameter("isAnticipazione");
			setRequestAttribute("isAnticipazione", isAnticipazione);
		}
		// Nel caso ritorna null, tutti i controlli sono passati

		// ----------------------------ANNOTAZIONI MANUALI---------------------------------
		lReturnPage = this.getAnnotazioniManuali(lIdFascicolo, aTipoAnnotazione);

		return lReturnPage;
	}

	// **
	// ** ! Modificare l'implementazione di questo metodo !
	// **
	/**
	 * Metodo che recupera i reati da visualizzare nella maschere di inserimento della richiesta. Setta il
	 * setFlagVisto sul reato a S o N. Se è già presente a sistema una richiesta per lo stesso reato
	 * (REA_ID_REATO) devo flaggare il reato come già visto, in modo che compaia anche in maschera nella
	 * sezione Ann. Inserita il segno di spunta.
	 */
	protected Vector checkReati(String aTipoAnnotazione, BigDecimal aIdFascicolo) throws F3BException {
		/******************************* Reati **********************************/
		IReato IRea = SIEPLookupRemote.getReatoRemote();
		Vector lReaVect = IRea.ExRicercaReatiByFascicoloNoError(aIdFascicolo);

		/******************************* Reati già elaborati ***************************************/
		IAnnotazioneManuale IAnn = SIEPLookupRemote.getAnnotazioneManualeRemote();

		for (int i = 0; i < lReaVect.size(); i++) {
			ReatoModel lReato = (ReatoModel) lReaVect.get(i);

			// Recupero tutte le annotazioni per lo stesso reato e verifico se sono
			// RICHIESTE dello stesso tipo di quella che voglio inserire.
			Vector lVectAnnModel = IAnn.ExRicercaAnnotazioneManualeNoErrorByIdReato(lReato.getIdReato());

			for (int j = 0; j < lVectAnnModel.size(); j++) {
				AnnotazioneManualeModel lAnnModel = (AnnotazioneManualeModel) lVectAnnModel.get(j);

				// Nel caso di AMNISTIA va considerato anche INDULTO
				if (aTipoAnnotazione.equals(AMNISTIA)) {
					if (lAnnModel != null
							&& ((lAnnModel.getCodTipoAnnotazione() != null
									&& lAnnModel.getCodTipoAnnotazione().equals(INDULTO))
									|| (lAnnModel.getCodTipoAnnotazione() != null
											&& lAnnModel.getCodTipoAnnotazione().equals(AMNISTIA)))
							&& ((lAnnModel.getFlagAppProvvisoria() != null
									&& lAnnModel.getFlagAppProvvisoria().equals("A"))
									|| (lAnnModel.getFlagAppProvvisoria() != null
											&& lAnnModel.getFlagAppProvvisoria().equals("R")))) {
						lReato.setFlagVisto("S");
						break;
					} else
						lReato.setFlagVisto("N");
				} else {
					if (lAnnModel != null
							&& ((lAnnModel.getCodTipoAnnotazione() != null
									&& lAnnModel.getCodTipoAnnotazione().equals(aTipoAnnotazione)))
							&& ((lAnnModel.getFlagAppProvvisoria() != null
									&& lAnnModel.getFlagAppProvvisoria().equals("A"))
									|| (lAnnModel.getFlagAppProvvisoria() != null
											&& lAnnModel.getFlagAppProvvisoria().equals("R")))) {
						lReato.setFlagVisto("S");
						break;
					} else
						lReato.setFlagVisto("N");
				}
			}
		}

		return lReaVect;
	}

	/**
	 * Recupera la Annotazioni manuali di tipo richiesta (FLAG_APP_PROVVISORIA='R' OR
	 * FLAG_APP_PROVVISORIA='A') al GE validate o meno. Se trova una sola annotazione non validata,
	 * restituisce la Action di dettaglio. Se ne trova più di una o una sola validata validata, restituisce la
	 * jsp di visualizzazione con l'elenco delle annotazioni già a sistema.
	 * 
	 * @param lIdFascicolo
	 * @param aTipoAnnotazione
	 *            = codTipoAnnotazione (es: 003 per amnistia/indulto)
	 * @return
	 * @throws F3BException
	 */
	protected String getAnnotazioniManuali(BigDecimal lIdFascicolo, String aTipoAnnotazione)
			throws F3BException {
		String lReturnPage = null;

		// Cerco le Annotazioni MAnuali
		IAnnotazioneManuale lCtrlAnnMan = SIEPLookupRemote.getAnnotazioneManualeRemote();
		AnnotazioneManualeModel lAnnMod = new AnnotazioneManualeModel();
		lAnnMod.setFasSieIdFascicoloSiep(lIdFascicolo);
		lAnnMod.setCodTipoAnnotazione(aTipoAnnotazione);
		lAnnMod.setFlagAppProvvisoria("RICHIESTE");

		// Cerco le annotazioni manuali validate e non
		// ---- lAnnMod.setFlagValidato("N");
		Vector lListAnnMan = null;

		try {
			lListAnnMan = lCtrlAnnMan.ExRicercaAnnotazioneManualeGenerico(lAnnMod);
		} catch (F3BException ex) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug(getClass().getName() + " Nessun elemento Trovato");
		}

		AnnotazioneManualeModel lAnnManIns = null;

		if (lListAnnMan != null) {
			// Setto l'ultima annotazione manuale trovata
			// Attenzione non è presente una order by sulla select!!!!
			lAnnManIns = (AnnotazioneManualeModel) lListAnnMan.get((lListAnnMan.size() - 1));
			// Se non è unica e non validata vado avanti altrimenti
			if (lAnnManIns.getFlagValidato().equals("S") || lListAnnMan.size() > 1) { // Non ho solo una
																						// annotazione manuale
																						// non validata quindi
																						// visulizzo l'elenco
																						// delle
																						// annotazioni
				setRequestAttribute("ListaAnnotazioniValidate", lListAnnMan);
				setRequestAttribute("TipoAnnotazione", aTipoAnnotazione);
				lReturnPage = IWebConstants.ROOT_DIR
						+ "/files/siap/siep/richiesta/ListaAnnotazioniManualiValidate.jsp";
			} else {
				// Sono nel caso di una sola Annotazione non Validata
				lReturnPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
						+ "=siap.siep.richiesta.action.ActLoadDettaglioAnnotazioniAnticipazioniAmnistia";
			}
		}

		return lReturnPage;
	}

}