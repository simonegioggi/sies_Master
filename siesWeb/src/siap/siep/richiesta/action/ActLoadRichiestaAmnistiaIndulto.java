package siap.siep.richiesta.action;

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
import siap.sico.decodifiche.model.DecodificheModel;
import siap.siep.SIEPException;
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
import siap.siep.util.SIEPLookupRemote;
import siap.web.ISIAPCostantiWeb;

/**
 * <p>
 * Title: ActLoadRichiestaAmnistiaIndulto
 * </p>
 * <p>
 * Description: Azione Load del Calcolo Pena
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @version 1.0
 */

public class ActLoadRichiestaAmnistiaIndulto extends ActLoadRichiestaGE implements ICostantiRichiesta {
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	/**
	 * Azione di caricamento della form d'inserimento della richiesta Amnistia/Indulto
	 * 
	 * @return Nome della pagina JSP su cui posizionarsi al termine dell'elaborazione
	 * @throws F3BException
	 */
	@SuppressWarnings("rawtypes")
	public String processRequest() throws F3BException {
		String lPage = checkFascicolo();

		if (lPage != null) {
			// verifico se presente errore perchè validato
			if (!isRequestAttributeNullObj(IWebConstants.MESSAGE_TEXT)) {
				String lMessaggio = (String) this.getRequestAttribute(IWebConstants.MESSAGE_TEXT);
				// Richieste indulto anche sui fascicoli archiviati
				if (lMessaggio.indexOf("Definito") == -1) {
					return lPage;
				}
			} else {
				return lPage;
			}
		}

		BigDecimal lIdFascicolo = ((FascicoloSiepModel) getSessionAttribute("fascicolo"))
				.getIdFascicoloSiep();

		// Recupero le Annotazion Manuali
		lPage = this.getAnnotazioniManuali(lIdFascicolo, AMNISTIA);

		if (isRequestParameterNullObj("ForzaInserimento") && lPage != null) { // Se esiste un elenco di
																				// Annotazioni o una
																				// annotazione non validata la
																				// faccio vedere
			return lPage;
		}

		// Prima di procedere all'inserimento verifico se sono presenti eventi non validati
		this.isEventoNonValidato();

		// String lReturnPage = null;
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
		// Verifico se abinizio (da eliminare)
		// ==========================================================================
		ICalcoloPena lCalPen = SIEPLookupRemote.getCalcoloPenaRemote();
		boolean lIsCalcoloPenaAbInitio = lCalPen.ExIsCalcoloPenaAbInizio(lIdFascicolo);
		setRequestAttribute("isCalcoloPenaAbInitio", new Boolean(lIsCalcoloPenaAbInitio));

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
		// else if (lPMod == null && !lIsCalcoloPenaAbInitio)
		// {
		// // Se Libero, non abInizio e pena vlidata assente, carico l'ultima pena
		// // a sistema
		// lPMod = lCtrlPenRes.ExRicercaPenaResiduaCorrenteByFascicoloSiep(lIdFascicolo);
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
				// pezza da togliere
				throw new F3BException(e);
			}

			// Nel caso di Amnistia/Indulto pesso alla from tutto il model in quanto
			// viene visualizzato il quantum di pena anche nel caso di pena in espiazione
			setRequestAttribute("PenaResiduaCorrente", lPenaResiduaCorrente);

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
				lCalReclusione.setErrorMsg("Libero");
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
		// Libero
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
		Vector lReaVect = checkReati(AMNISTIA, lIdFascicolo);

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
		/*
		 * lReturnPage = this.getAnnotazioniManuali(lIdFascicolo, AMNISTIA);
		 * 
		 * if (lReturnPage != null) return lReturnPage;
		 */

		// 31/07/2006 Aggiunto posizionamento combo Tipo beneficio su Indulto.
		Option lOption = new Option(DecodificheManager.getInstance().getTipoAnnotazioneManualeBenefici(),
				"002");
		setRequestAttribute("TipoAnnotazioneManuale", "" + lOption);

		lOption = new Option(DecodificheManager.getInstance().getDPR());
		// 31/07/2006 Aggiunto posizionamento combo DPR all'ultimo elemento.
		Vector lVect = (Vector) DecodificheManager.getInstance().getDPR();
		DecodificheModel lDecMod = (DecodificheModel) lVect.lastElement();
		lOption.setSelected(lDecMod.getCode());
		setRequestAttribute("listaDPR", "" + lOption);

		return PG_LOAD_RICHIESTA_AMNISTIA_INDULTO;
	}
}