package siap.sius.statistiche.action;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Vector;

import org.apache.log4j.Logger;

import siap.sico.security.action.ICostantiSecurity;
import siap.sico.utente.model.UtenteModel;
import siap.sico.web.ActionSiap;
import siap.sius.SIUSException;
import siap.sius.stampa.controller.IStampaSius;
import siap.sius.statistiche.controller.IStatisticheSius;
import siap.sius.statistiche.model.RicercaProvvedimentoModel;
import siap.sius.util.SIUSLookupRemote;
import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.web.IWebConstants;

/**
 * <p>
 * Title: ActRicercaProvvedimentiPriviDiFC
 * </p>
 * <p>
 * Description: Classe Action per la ricerca dei provvedimenti privi di Foglio Complementare;
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: EII
 * </p>
 * 
 * @since MEV10-s3
 * @author SIMONE GIOGGI
 * @version 1.0
 */
@SuppressWarnings("rawtypes")
public class ActRicercaProvvedimentiPriviDiFC extends ActionSiap implements ICostantiStatistiche {
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
	RicercaProvvedimentoModel mRicercaModel;
	// Elenco risultato della ricerca
	private Vector mVect = null;

	public String processRequest() throws Exception {
		String lReturnPage = PG_ELENCO_PROVVEDIMENTI;

		// Testo con i contatori risultato della ricerca effettuata
		String lConteggi = null;

		// Inizializzazione numero di pagina
		String lPagina = "1";
		if (!isRequestParameterNullObj(IWebConstants.NUM_PAGE))
			lPagina = getRequestStringParameter(IWebConstants.NUM_PAGE);

		mRicercaModel = new RicercaProvvedimentoModel();
		mRicercaModel.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
		letturaFiltro();

		// Controller per la Ricerca
		IStatisticheSius lCtrl = SIUSLookupRemote.getStatisticheSiusRemote();

		// Stampa
		if (!isRequestParameterNullObj("Stampa")) {
			// Ricerca completa
			mVect = lCtrl.ExRicercaProcSiusXProvvedimentiPaginata(mRicercaModel, -1);

			if (mVect != null)
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
				siesLogger.debug("Risultati ricerca completa : " + mVect.size());

			// Chiamata solo per valorizzare i contatori
			lCtrl.ExGetNumRicercaProcSiusXProvvedimenti(mRicercaModel);
			mRicercaModel.setDescCalcoli(lConteggi);

			lReturnPage = Stampa();
		} else {
			// Bottone di ritorno
			setLinkRitorno();

			// Ricerca paginata
			mVect = lCtrl.ExRicercaProcSiusXProvvedimentiPaginata(mRicercaModel, Integer.parseInt(lPagina));
			
			if (mVect != null)
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
				siesLogger.debug("Risultati ricerca paginata : " + mVect.size());

			// Paginazione
			BigDecimal CountRisultati;
			if (isRequestParameterNullObj("CountRisultati")) {
				// La prima pagina
				CountRisultati = lCtrl.ExGetNumRicercaProcSiusXProvvedimenti(mRicercaModel);
				// Si valorizza la Descrizione dei conteggi statistici
				mRicercaModel.setDescCalcoli(lConteggi);
				setSessionAttribute(CAMPO_DESC_CALCOLI, mRicercaModel.getDescCalcoli());
			} else {
				// Pagine successive
				CountRisultati = getRequestBigDecimalParameter("CountRisultati");
				// La descrizione dei conteggi viene letta dalla sessione
				lConteggi = (String) getSessionAttribute(CAMPO_DESC_CALCOLI);
				mRicercaModel.setDescCalcoli(lConteggi);
			}

			setRequestAttribute("CountRisultati", CountRisultati);
			setRequestAttribute(IWebConstants.NUM_PAGE, lPagina);
			setRequestAttribute(IWebConstants.REQUEST_FOR_PAGING, getCompleteRequestURL());

			// L'elenco viene passato nella request
			setRequestAttribute("Provvedimenti", mVect);

			// Metto in sessione il modello di ricerca per utilizzarlo nella stampa excel
			setSessionAttribute("ProvvRicercaModel", mRicercaModel);

			/*
			 * Viene passato nella request il Filtro di ricerca utilizzato per la sua visualizzazione nella
			 * pagina risultato. Nello stesso model viene valorizzata la descrizione di calcoli statistici
			 * sulla ricerca effettuata.
			 */

			setRequestAttribute("FiltoRicerca", mRicercaModel);
		}

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
		siesLogger.debug("Model di Ricerca: " + mRicercaModel);
		return lReturnPage;
	}

	private void letturaFiltro() throws Exception {
		// Modalit‡ di Ricerca: se per Provvedimento o per decreto
		String lModalit‡Ricera = getRequestStringParameter(CAMPO_MODALITA_RICERCA);
		if (lModalit‡Ricera != null && lModalit‡Ricera.trim().length() > 0)
			mRicercaModel.setModalitaRicerca(lModalit‡Ricera);

		// Lettura del tipo di Intewrvallo di Rcerca
		String lTipoRicerca = getRequestStringParameter(CAMPO_TIPO_INTERVALLO);
		mRicercaModel.setTipoIntervalloRicerca(lTipoRicerca);

		// Lettura opzione su Stato Validazione delle Provvedimenti
		String lStatoValidazione = getRequestStringParameter(CAMPO_STATO_VALIDAZIONE);
		mRicercaModel.setStatoValidazione(lStatoValidazione);

		// Gli Intervalli dipendono dal tipo di Ricerca
		if (mRicercaModel.isTipoIntervalloRicercaXEstremiProvvedimento()) {
			// Lettura Intervallo per ANNO / NUM iniziale e finale Provvedimenti
			mRicercaModel.setAnnoNumIniziale(getRequestBigDecimalParameter(CAMPO_ANNO_INI),
					getRequestBigDecimalParameter(CAMPO_NUM_INI));
			mRicercaModel.setAnnoNumFinale(getRequestBigDecimalParameter(CAMPO_ANNO_FINE),
					getRequestBigDecimalParameter(CAMPO_NUM_FINE));
		} else {
			// Lettura Intervallo date iniziale e finale
			letturaDate();
		}
	}

	/*
	 * Lettura di Data di inizio e Data di fine dalla form di input.
	 */
	private void letturaDate() throws Exception {
		// Intervallo date
		Date lDataIni = null, lDataFin = null;

		lDataIni = getRequestDateParameter(CAMPO_ANNO_DATA_DEPOSITO_INI, CAMPO_MESE_DATA_DEPOSITO_INI,
				CAMPO_GIORNO_DATA_DEPOSITO_INI);
		lDataFin = getRequestDateParameter(CAMPO_ANNO_DATA_DEPOSITO_FINE, CAMPO_MESE_DATA_DEPOSITO_FINE,
				CAMPO_GIORNO_DATA_DEPOSITO_FINE);

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
		siesLogger.debug(
				"data iniziale deposito: " + DateUtils.getDateToString(lDataIni, "dd/MM/yyyy"));
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
		siesLogger
				.debug("data finale deposito: " + DateUtils.getDateToString(lDataFin, "dd/MM/yyyy"));

		if (mRicercaModel.isRicercaXDateArrivoCancelleria()) {
			mRicercaModel.setDataArrivoInCancelleriaIniziale(lDataIni);
			mRicercaModel.setDataArrivoInCancelleriaFinale(lDataFin);
		} else if (mRicercaModel.isRicercaXDateDeposito()) {
			mRicercaModel.setDataDepositoIniziale(lDataIni);
			mRicercaModel.setDataDepositoFinale(lDataFin);
		} else if (mRicercaModel.isRicercaXDateEmissione()) {
			mRicercaModel.setDataEmissioneIniziale(lDataIni);
			mRicercaModel.setDataEmissioneFinale(lDataFin);
		}
	}

	/**
	 * Esegue la stampa.
	 * <p>
	 * 
	 * @throws Exception
	 *             propaga errore di eccezione.
	 * @return String Ritorna documneto rtf di stampa.
	 */
	private String Stampa() throws Exception {
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
		siesLogger.debug("" + getClass().getName() + " .Stampa(): inizio ");

		// Pagina di stampa
		String lRet = IWebConstants.PG_DOWNLOAD;
		// Documento di stampa
		ByteArrayOutputStream lReport = null;

		// Si Recupera l'utente dalla sessione.
		UtenteModel lUtenteMod = (UtenteModel) getSessionAttribute(ICostantiSecurity.SESSION_UTENTE_CONNESSO);
		lUtenteMod.getUfficioUtente().getCodComune();

		// Generazione documento di stampa
		IStampaSius lCtrlSta = SIUSLookupRemote.getStampaRemote();

		lReport = lCtrlSta.ExPreStampaProcSiusXProv(mRicercaModel, mVect, lUtenteMod);

		// Prepara la pagina di destinazione
		if (lReport != null)
			setRequestAttribute("report", lReport);
		else
			throw new SIUSException(SIUSException.USER_MESSAGE, "Nessun documento Ë stato generato!");

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
		siesLogger.debug("" + getClass().getName() + " .Stampa(): fine ");

		return lRet;
	}

}