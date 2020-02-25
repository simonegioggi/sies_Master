package siap.siep.notifica.action;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.web.IWebConstants;
import siap.sico.security.action.ICostantiSecurity;
import siap.sico.utente.model.UtenteModel;
import siap.sico.web.ActionSiap;
import siap.siep.notifica.controller.INotifica;
import siap.siep.notifica.model.RicercaNotificheSiusModel;
import siap.siep.util.SIEPLookupRemote;
import siap.sius.SIUSException;
import siap.sius.stampa.controller.IStampaSius;
import siap.sius.util.SIUSLookupRemote;

/**
 * <p>
 * Title: ActdRicercaPareri
 * </p>
 * <p>
 * Description: Attiva la Ricerca delle Notifiche/Comunicazioni per Procedimenti SIUS. sia per la sua
 * visualizzazione che per la stampa.
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
public class ActRicercaNotificaSius extends ActionSiap implements ICostantiNotifica {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	// Model utilizzato per settare i parametri di filtro nella ricerca
	private RicercaNotificheSiusModel mRicercaModel = new RicercaNotificheSiusModel();

	// Elenco risultato della ricerca
	@SuppressWarnings("rawtypes")
	private Vector mVect = null;

	public String processRequest() throws Exception {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("" + getClass().getName() + " .processRequest: inizio ");

		// Inizializzazione numero di pagina
		String lPagina = "1";
		if (!isRequestParameterNullObj(IWebConstants.NUM_PAGE))
			lPagina = getRequestStringParameter(IWebConstants.NUM_PAGE);

		// Lettura dei dati di filtro per la ricerca
		letturaFiltroRicerca();

		// Controller per la Ricerca
		INotifica lCtrl = SIEPLookupRemote.getNotificaRemote();

		// la pagina JSP di visualizzazione del risultato
		String lReturnPage = PG_ELENCO_NOTIFICHE_SIUS;

		// Stampa
		if (!isRequestParameterNullObj("Stampa")) {
			// Ricerca completa
			mVect = lCtrl.ExRicercaPaginataNotificheXFasSius(mRicercaModel, -1);

			if (mVect != null)
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("Risultati ricerca completa : " + mVect.size());

			lReturnPage = Stampa();
		} else {
			// Bottone di ritorno
			setLinkRitorno();

			// Ricerca paginata
			mVect = lCtrl.ExRicercaPaginataNotificheXFasSius(mRicercaModel, Integer.parseInt(lPagina));

			if (mVect != null)
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("Risultati ricerca paginata : " + mVect.size());

			// Paginazione
			BigDecimal CountRisultati;
			if (isRequestParameterNullObj("CountRisultati"))
				CountRisultati = lCtrl.ExGetNumRicercaNotificheXFasSius(mRicercaModel);
			else
				CountRisultati = getRequestBigDecimalParameter("CountRisultati");

			setRequestAttribute("CountRisultati", CountRisultati);
			setRequestAttribute(IWebConstants.NUM_PAGE, lPagina);
			setRequestAttribute(IWebConstants.REQUEST_FOR_PAGING, getCompleteRequestURL());

			// L'elenco viene passato nella request
			setRequestAttribute("Notifiche", mVect);

			/*
			 * Viene passato nella request il Filtro di ricerca utilizzato per la sua visualizzazione nella
			 * pagina risultato.
			 */
			setRequestAttribute("FiltoRicerca", mRicercaModel);

			/*
			 * L'intestazione dell'Elenco risultato viene composto in base al tipo di atto ed al periodo di
			 * date.
			 */
			String lAtti = (mRicercaModel.getCodTipoNotifica().equalsIgnoreCase("C")) ? "Comunicazioni"
					: "Notifiche";
			String lInTesta = "Elenco delle " + lAtti + " richieste nel periodo "
					+ DateUtils.getDateToString(mRicercaModel.getDataIniziale(), "dd/MM/yyyy") + " - "
					+ DateUtils.getDateToString(mRicercaModel.getDataFinale(), "dd/MM/yyyy");
			setRequestAttribute("InTesta", lInTesta);
		}

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("" + getClass().getName() + " .processRequest: fine ");

		return lReturnPage;
	}

	/**
	 * Esegue stampa elenco delle Notifiche.
	 * <p>
	 * 
	 * @throws Exception
	 *             propaga errore di eccezione.
	 * @return String Ritorna documneto rtf di stampa.
	 */
	private String Stampa() throws Exception {
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
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

		lReport = lCtrlSta.ExPreStampaNotificheSius(mRicercaModel, mVect, lUtenteMod);

		// Prepara la pagina di destinazione
		if (lReport != null)
			setRequestAttribute("report", lReport);
		else
			throw new SIUSException(SIUSException.USER_MESSAGE, "Nessun documento è stato generato!");

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("" + getClass().getName() + " .Stampa(): fine ");

		return lRet;
	}

	/**
	 * Metodo private che esegue la lettura del filtro d'applicare per la ricerca.
	 * <p>
	 * 
	 * @throws Exception
	 *             propaga errore di eccezione.
	 */
	private void letturaFiltroRicerca() throws Exception {
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("" + getClass().getName() + " .letturaFiltroRicerca(): inizio ");

		// La Ricerca è sempre per l'ufficio dell'utente connesso
		mRicercaModel.setCodUfficioInserimento(getCodUfficioUtenteConnesso());

		// Lettura Codice Utente
		String lCodiceUtente = getRequestStringParameter(CAMPO_COD_OPERATORE_INSERIMENTO);
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("Codice Utente : " + lCodiceUtente);
		// Se valorizzato la ricerca avviene anche per utente di inserimento
		if (lCodiceUtente.trim().length() > 0)
			mRicercaModel.setCodOperatoreInserimento(lCodiceUtente);

		// Intervallo date
		letturaDate();

		// Tipo Atto (Notifica o Comunicazione)
		mRicercaModel.setCodTipoNotifica(getRequestStringParameter(CAMPO_COD_TIPO_NOTIFICA));
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("Tipo Notifica : " + mRicercaModel.getCodTipoNotifica());

		// Tipo provvedimenti
		letturaTipoProvvedimento();

		// Tipo destinatario
		letturaTipoDestinatario();

		// Filtro su NOTE
		if (!isRequestParameterNullObj(CAMPO_NOTE)) {
			mRicercaModel.setFiltroNote(getRequestStringParameter(CAMPO_NOTE));
		}

		// Tipo di Ordinamento
		mRicercaModel.setOrdinamento(getRequestStringParameter(TIPO_ORDINAMENTO));
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("Tipo Ordinamento : " + mRicercaModel.getOrdinamento());

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("" + getClass().getName() + " .letturaFiltroRicerca(): fine ");
		return;
	}
	/*
	 * Lettura di Data di inizio e Data di fine dalla form di input.
	 */

	private void letturaDate() throws Exception {
		// Intervallo date
		Date lDataIni = null, lDataFin = null;

		lDataIni = getRequestDateParameter(CAMPO_ANNO_DATA_INSERIMENTO, CAMPO_MESE_DATA_INSERIMENTO,
				CAMPO_GIORNO_DATA_INSERIMENTO);
		lDataFin = getRequestDateParameter(CAMPO_ANNO_DATA_INSERIMENTO2, CAMPO_MESE_DATA_INSERIMENTO2,
				CAMPO_GIORNO_DATA_INSERIMENTO2);

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("data iniziale : " + DateUtils.getDateToString(lDataIni, "dd/MM/yyyy"));
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("data finale : " + DateUtils.getDateToString(lDataFin, "dd/MM/yyyy"));

		mRicercaModel.setDataIniziale(lDataIni);
		mRicercaModel.setDataFinale(lDataFin);

	}

	/*
	 * La funzione stabilisce i criteri di ricerca sul tipo di provvedimento in base alla lettura della form
	 * di input. Le possibilità sono: Solo decreto di citazione; Tutti i provvedimenti escluso i decreti di
	 * citazione; Tutti (default):
	 */
	private void letturaTipoProvvedimento() throws Exception {
		String lCodTipoProvvedimento = getRequestStringParameter(TIPO_PROVVEDIMENTO);
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("Tipo Provvedimento : " + lCodTipoProvvedimento);
		if (lCodTipoProvvedimento.equalsIgnoreCase(DECRETO_CITAZIONE))
			mRicercaModel.setDecret1Citazione();
		else if (lCodTipoProvvedimento.equalsIgnoreCase(ALTRI_PROVVEDIMENTI))
			mRicercaModel.setEsclusoDecret1Citazione();

		// Viene memorizzato la selezione sul filtro tipo provvedimento
		mRicercaModel.setTipoProvvedimento(lCodTipoProvvedimento);

	}

	/*
	 * La funzione stabilisce i criteri di ricerca sul tipo di destinatario in base alla lettura della form di
	 * input. Le possibilità sul tipo di Destinatario sono: UNEP_SEDE - Ufficio UNEP di una sede specifica;
	 * UNEP_ALTRE_SEDI - Ufficio UNEP escluso una sede specifica; UNEP_TUTTI - Ufficio UNEP; UNEP_ESCLUSO -
	 * Ufficio non UNEP; Qualunque destinatario (default).
	 */

	private void letturaTipoDestinatario() throws Exception {
		String lCodTipoDestinatario = getRequestStringParameter(TIPO_DESTINATARIO);
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("Tipo Destinatario : " + lCodTipoDestinatario);

		String lCodSede = ""; // Comune dell'Ufficio
		String lCodSedePGCAP = ""; // Comune sede Procura Generale

		// Si Recupera l'utente dalla sessione per risalire al codice del Comune.
		UtenteModel lUtenteMod = (UtenteModel) getSessionAttribute(ICostantiSecurity.SESSION_UTENTE_CONNESSO);
		if (lUtenteMod != null && lUtenteMod.getUfficioUtente() != null) {
			lCodSede = lUtenteMod.getUfficioUtente().getCodComune();
			lCodSedePGCAP = lUtenteMod.getUfficioUtente().getCodDistretto().substring(0, 6);
		}
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("Comune sede : " + lCodSede);
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("Comune PGCAP : " + lCodSedePGCAP);

		// Filtro su Autorità Esterna UEPE
		if (lCodTipoDestinatario.equalsIgnoreCase(UNEP_SEDE))
			mRicercaModel.setUNEP(lCodSede);
		else if (lCodTipoDestinatario.equalsIgnoreCase(UNEP_ALTRE_SEDI))
			mRicercaModel.setUNEPEsclusoSede(lCodSede);
		else if (lCodTipoDestinatario.equalsIgnoreCase(UNEP_TUTTI))
			mRicercaModel.setUNEP();
		else if (lCodTipoDestinatario.equalsIgnoreCase(UNEP_ESCLUSO))
			mRicercaModel.setEsclusoUNEP();

		// Filtro su Ufficio Procura della Repubblica (PM)
		else if (lCodTipoDestinatario.equalsIgnoreCase(PM_TUTTI))
			mRicercaModel.setUffDestinatario("PM");
		else if (lCodTipoDestinatario.equalsIgnoreCase(PM_SEDE))
			mRicercaModel.setUffDestinatario("PM", lCodSede);
		else if (lCodTipoDestinatario.equalsIgnoreCase(PM_ALTRE_SEDI))
			mRicercaModel.setEsclusoSedeUffDestinatario("PM", lCodSede);
		else if (lCodTipoDestinatario.equalsIgnoreCase(PM_ESCLUSO))
			mRicercaModel.setEsclusoUffDestinatario("PM");

		// Filtro su Procura Generale (PGCAP)
		else if (lCodTipoDestinatario.equalsIgnoreCase(PGCAP_TUTTI))
			mRicercaModel.setUffDestinatario("PGCAP");
		else if (lCodTipoDestinatario.equalsIgnoreCase(PGCAP_SEDE))
			mRicercaModel.setUffDestinatario("PGCAP", lCodSedePGCAP);
		else if (lCodTipoDestinatario.equalsIgnoreCase(PGCAP_ALTRE_SEDI))
			mRicercaModel.setEsclusoSedeUffDestinatario("PGCAP", lCodSedePGCAP);
		else if (lCodTipoDestinatario.equalsIgnoreCase(PGCAP_ESCLUSO))
			mRicercaModel.setEsclusoUffDestinatario("PGCAP");

		// Viene memorizzato la selezione sul filtro tipo destinatario
		mRicercaModel.setTipoDestinatario(lCodTipoDestinatario);
	}

}