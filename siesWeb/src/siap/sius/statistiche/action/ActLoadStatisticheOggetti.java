package siap.sius.statistiche.action;

/**
* <p>Title: ActLoadTempiEmissione</p>
* <p>Description: Action adibita al caricamento della form di Statistiche su
* "Movimenti Provvedimenti distinti per oggetti".
* <p>Copyright: Copyright (c) 2007</p>
* <p>Company: Eunics</p>
* @version 3.0
*/

import java.util.Date;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
import f3b.web.html.Option;
import siap.sico.lock.controller.LockController;
import siap.sico.lock.model.LockModel;
import siap.sico.web.ActionSiap;
import siap.sius.cancassfascsius.action.ICostantiCancAssFascSius;
import siap.sius.cancassfascsius.model.CancAssFascSiusModel;
import siap.sius.cancelleriaassegnataria.controller.ICancelleriaAssegnataria;
import siap.sius.cancelleriaassegnataria.model.CancelleriaAssegnatariaModel;
import siap.sius.collaboratore.controller.ICollaboratore;
import siap.sius.statistiche.controller.IStatisticheSius;
import siap.sius.util.SIUSLookupRemote;

/**
 * Azione che gestisce l'elaborazione delle Statistiche SIUS di Monitoraggio Procedimenti per Oggetti. Alla
 * sua prima attivazione l'azione visualizza la form di input dell'intervallo di date di riferimento. L'azione
 * viene poi richiamata dalla stessa form ed effettua nella seconda istanza il caricamento dei dati statistici
 * e la preparazione delle opzioni previste per la generazione del report.
 * 
 * @author Lesposito
 *
 */
@SuppressWarnings("rawtypes")
public class ActLoadStatisticheOggetti extends ActionSiap implements ICostantiStatistiche {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	// Intervallo di date di riferimento
	Date mDataIni = null, mDataFin = null;
	// Controller per le Statistiche SIUS
	IStatisticheSius mCtrl;

	public String processRequest() throws F3BException {

		// Lock
		// LockModel lck =
		// LockController.lockIfNotLocked(getServletContext(),"STATISTICHE_OGGETTI","1",getCodUtenteConnesso(),getSession().getId());
		// mod. michele 5/12/2008
		LockModel lck = LockController.lockIfNotLocked(getServletContext(), "STATISTICHE",
				getCodUfficioUtenteConnesso(), getCodUtenteConnesso(), getSession().getId());

		if (lck != null) {
			setRequestAttribute(IWebConstants.MESSAGE_TEXT,
					"Questa funzione non può essere attivata contemporaneamente da più utenti !<BR>Riprovare più tardi !");
			return IWebConstants.PG_MESSAGE;
		}

		// Controllo se primo giro o secondo

		if (!isRequestParameterNullObj(CAMPO_ANNO_INIZIALE)) {

			// Lettura Intervallo date
			letturaDate();

			// lettura eventuale Cancelleria Assegnataria di Filtro
			String lCodCancelleria = ricercaCancelleriaAssegnataria();

			// lettura eventuale filtro su Collaboratore di Giustizia
			String lFiltroCollaboratore = getFiltroCollaboratore();

			// lettura eventuale filtro su Collaboratore di Giustizia
			String lPosizioneGiuridica = getPosizioneGiuridica();

			// Controller per l'attivazione del Caricamento
			mCtrl = SIUSLookupRemote.getStatisticheSiusRemote();
			mCtrl.ExStatoOggettiSiusStoredProcedure(getCodUfficioUtenteConnesso(), mDataIni, mDataFin,
					lCodCancelleria, lFiltroCollaboratore, lPosizioneGiuridica);

			// RICERCA Magistrati PER RIEMPIRE LA COMBOBOX
			Vector lMagModVect = mCtrl.ExRicercaMagistratiOggettiEstratti(
					getCodUfficioUtenteConnesso()); /* mod michele 2/12/2008 */
			setRequestAttribute("magistrati", lMagModVect);

			// RICERCA Oggetti PER RIEMPIRE LA COMBOBOX
			ricercaOggettiEstratti(TABELLA_ESTRAZIONE_OGGETTI, getCodUfficioUtenteConnesso());

			setRequestAttribute("SecondoGiro", "SI");

		} else {
			ricercaCancellerieAssegnatarie();
			filtroCollaboratore();
		}

		return PG_LOAD_ESTRAZIONE_OGGETTI; // restituisce la jsp di VIEW

	}

	/*
	 * La funzione legge dalla request l'intervallo di date di riferimento e passa le stesse alla request di
	 * uscita.
	 *
	 */
	protected void letturaDate() throws F3BException {

		// Lettura Intervallo date
		mDataIni = getRequestDateParameter(CAMPO_ANNO_INIZIALE, CAMPO_MESE_INIZIALE, CAMPO_GIORNO_INIZIALE);
		mDataFin = getRequestDateParameter(CAMPO_ANNO_FINALE, CAMPO_MESE_FINALE, CAMPO_GIORNO_FINALE);

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("data iniziale : " + DateUtils.getDateToString(mDataIni, "dd/MM/yyyy"));
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("data finale : " + DateUtils.getDateToString(mDataFin, "dd/MM/yyyy"));

		// Passgggio dei parametri alla request
		setRequestAttribute("AnnoI", getRequestStringParameter(CAMPO_ANNO_INIZIALE));
		setRequestAttribute("MeseI", getRequestStringParameter(CAMPO_MESE_INIZIALE));
		setRequestAttribute("GiornoI", getRequestStringParameter(CAMPO_GIORNO_INIZIALE));
		setRequestAttribute("AnnoF", getRequestStringParameter(CAMPO_ANNO_FINALE));
		setRequestAttribute("MeseF", getRequestStringParameter(CAMPO_MESE_FINALE));
		setRequestAttribute("GiornoF", getRequestStringParameter(CAMPO_GIORNO_FINALE));
	}

	/*
	 * La funzione ricerca nella tabella di estrazione il cui nome viene passato come parametro gli oggetti
	 * estratti e li passa nella request per la costruzione della combo.
	 */
	protected void ricercaOggettiEstratti(String aNomeTabella) throws F3BException {

		// RICERCA Oggetti PER RIEMPIRE LA COMBOBOX
		Vector lOggetti = mCtrl.ExListaOggettiEstratti(aNomeTabella);
		Option lOption = new Option(lOggetti);
		setRequestAttribute("ListaOggetti", lOption.toString());
	}

	/*
	 * La funzione ricerca nella tabella di estrazione il cui nome viene passato come parametro gli oggetti
	 * estratti per l'ufficio di competenza e li passa nella request per la costruzione della combo.
	 */
	protected void ricercaOggettiEstratti(String aNomeTabella, String aCodUfficio) throws F3BException {

		// RICERCA Oggetti PER RIEMPIRE LA COMBOBOX
		Vector lOggetti = mCtrl.ExListaOggettiEstratti(aNomeTabella, aCodUfficio);
		Option lOption = new Option(lOggetti);
		setRequestAttribute("ListaOggetti", lOption.toString());
	}

	/**
	 * Ricerca delle Cancellerie Assegnatarie definite per l'Ufficio. Se trovate le Cancellerie vengono
	 * passate nella request per valorizzare la combo di scelta.
	 * 
	 * @throws F3BException
	 */
	protected void ricercaCancellerieAssegnatarie() throws F3BException {

		// Prepara il model di ricerca delle Cancellerie Assegnatarie previste per l'Ufficio dell'uttente
		CancelleriaAssegnatariaModel lCancAssModel = new CancelleriaAssegnatariaModel();
		lCancAssModel.setCodUfficio(getCodUfficioUtenteConnesso());

		// Ricerca
		ICancelleriaAssegnataria lCancAssCtrl = SIUSLookupRemote.getCancelleriaAssegnatariaRemote();
		Vector lElencoCancellerie = lCancAssCtrl.ExRicercaCancelleriaAssegnataria(lCancAssModel);
		if (lElencoCancellerie != null && lElencoCancellerie.size() > 0) {
			setRequestAttribute("cancellerie", lElencoCancellerie);
		}

	}

	/**
	 * Ricerca della Cancelleria Assegnataria selezionata. Se trovata la Cancellerie viene passata nella
	 * request.
	 * 
	 * @throws F3BException
	 */
	protected String ricercaCancelleriaAssegnataria() throws F3BException {

		String lCodCancelleria = "0";
		// Se è stato selezionato il filtro per Cancelleria Assegnataria si prepara il model per la ricerca
		CancAssFascSiusModel lCancAssFasc = null;
		if (!isRequestParameterNullObj(ICostantiCancAssFascSius.CAMPO_COD_CANCELLERIA_ASSEGNATARIA)) {
			String lCodCancelleriaAssegnataria = getRequestStringParameter(
					ICostantiCancAssFascSius.CAMPO_COD_CANCELLERIA_ASSEGNATARIA);
			if (lCodCancelleriaAssegnataria.trim().length() > 0) {
				lCancAssFasc = new CancAssFascSiusModel();
				lCancAssFasc.setCodCancelleriaAssegnataria(lCodCancelleriaAssegnataria);
				// La ricerca è sempre limitata all'Ufficio dell'utente connesso
				lCancAssFasc.setCodUfficio(getCodUfficioUtenteConnesso());
				// Ricerca della Cancelleria Assegnataria da passare nella request
				// per indicarla tra le condizioni di ricerca
				ICancelleriaAssegnataria lCancCtrl = SIUSLookupRemote.getCancelleriaAssegnatariaRemote();
				Vector lCancellerie = lCancCtrl.ExRicercaCancelleriaAssegnataria(lCancAssFasc);
				if (lCancellerie != null && lCancellerie.size() > 0) {
					setRequestAttribute("cancellerie", lCancellerie);
					CancelleriaAssegnatariaModel lCancMod = (CancelleriaAssegnatariaModel) lCancellerie
							.get(0);
					lCodCancelleria = lCancMod.getCodCancelleriaAssegnataria();
				}
			}
		}
		return lCodCancelleria;
	}

	/**
	 * La funzione abilita il filtro nella Ricerca sul Collaboratore di Giustizia.
	 * 
	 * @throws F3BException
	 */
	protected void filtroCollaboratore() throws F3BException {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("filtroCollaboratore() : inizio");

		// Si controlla se esiste l'interfaccia per la Gestione Collaboratore di Giustizia
		ICollaboratore lCtrl = SIUSLookupRemote.getCollaboratoreRemote();
		if (lCtrl.ExIsPackage()) {
			setRequestAttribute("collaboratore", "SI");
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("collaboratore : SI");
		}
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("filtroCollaboratore() : fine");

	}

	// Funzione di lettura Filtro sul Collaboratore eventualmente presente nella form di input
	protected String getFiltroCollaboratore() throws F3BException {

		String lRet = null;
		if (!isRequestParameterNullObj(FILTRO_COLLABORATORE)) {
			lRet = getRequestStringParameter(FILTRO_COLLABORATORE);
			if (!lRet.equalsIgnoreCase("tutti")) {
				setRequestAttribute("filtroCollaboratore", lRet);
				setRequestAttribute("collaboratore", "SI");
			}
		}

		return lRet;
	}

	// Funzione di lettura Filtro sul Collaboratore eventualmente presente nella form di input
	protected String getPosizioneGiuridica() throws F3BException {

		String lRet = null;
		if (!isRequestParameterNullObj(FILTRO_POSIZIONE_GIURIDICA)) {
			lRet = getRequestStringParameter(FILTRO_POSIZIONE_GIURIDICA);
			setRequestAttribute("filtroPosizioneGiuridica", lRet);
		}

		return lRet;
	}

}