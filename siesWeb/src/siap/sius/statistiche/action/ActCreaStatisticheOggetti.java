package siap.sius.statistiche.action;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.decodifiche.util.DecodificheUtils;
import siap.sico.lock.controller.LockController;
import siap.sico.lock.model.LockModel;
import siap.sico.magistrato.model.MagistratoModel;
import siap.sico.web.ActionSiap;
import siap.siep.statis.controller.StatisController;
import siap.sius.cancassfascsius.action.ICostantiCancAssFascSius;
import siap.sius.cancassfascsius.model.CancAssFascSiusModel;
import siap.sius.cancelleriaassegnataria.controller.ICancelleriaAssegnataria;
import siap.sius.cancelleriaassegnataria.model.CancelleriaAssegnatariaModel;
import siap.sius.esperto.model.EspertoModel;
import siap.sius.statistiche.controller.IStatisticheSius;
import siap.sius.statistiche.model.IspEstrazioneOggettiModel;
import siap.sius.util.SIUSLookupRemote;
import f3b.log.LogF3B;
import f3b.util.F3BException;
import f3b.web.IWebConstants;

/**
 * <p>
 * Title: ActCreaStatisticheOggetti
 * </p>
 * <p>
 * Description: Classe Action per la creazione dei report Statistica Procedimenti SIUS per Oggetti
 * </p>
 * <p>
 * Copyright: Copyright (c) 2006
 * </p>
 * <p>
 * Company: Eutelia
 * </p>
 * 
 * @version 1.0
 */
@SuppressWarnings({ "rawtypes" })
public class ActCreaStatisticheOggetti extends ActionSiap implements ICostantiStatistiche {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
	// Descrizione Oggetto selezionato per dettaglio
	String mDescOggetto = null;
	// Controller Statistiche SIUS
	IStatisticheSius mCtrl;
	// Eventuale Cancelleria Assegnataria usata come filtro nell'estrazione
	CancelleriaAssegnatariaModel mCancMod = null;

	public String processRequest() throws F3BException {

		MagistratoModel lMagistrato = null;
		EspertoModel lEsperto = null;
		String lFiltroCollab = null;
		String lFiltroPosGiurid = null;
		String strRelatore = null;
		Vector lStatisticheRelatori = null;
		Vector lProcPriviDiRelatore = null;

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

		// Lettura Intervallo date
		Date lDataIni = null, lDataFin = null;

		lDataIni = getRequestDateParameter(CAMPO_ANNO_INIZIALE, CAMPO_MESE_INIZIALE, CAMPO_GIORNO_INIZIALE);
		lDataFin = getRequestDateParameter(CAMPO_ANNO_FINALE, CAMPO_MESE_FINALE, CAMPO_GIORNO_FINALE);

		// Recupero dati della maschera
		String lCodMag = getRequestStringParameter(CB_LISTA_MAGISTRATI);
		int lTipoEstrazione = getRequestIntParameter(CB_TIPO_ESTRAZIONE_OGGETTI);

		// Recupero del Codice Ufficio
		String lCodUfficio = getCodUfficioUtenteConnesso();

		// Controller per l'attivazione della STORED PROCEDURE adibita alla
		// creazione delle Statistiche.
		mCtrl = SIUSLookupRemote.getStatisticheSiusRemote();

		if (lCodMag.compareTo("0") == 0) {
			mCtrl.ExCreaStatisticaRelatoriStoredProcedure(lCodUfficio, lDataIni, lDataFin);
			lStatisticheRelatori = mCtrl.ExRicercaStatisticaRelatori(lCodUfficio);
			lProcPriviDiRelatore = elencoProcedimentiPriviDiRelatoreEstratti(lCodUfficio);
		}

		mCtrl.ExCreaStatisticaOggettiStoredProcedure(lCodUfficio, lCodMag, lDataIni, lDataFin);

		// Eventuale ricerca dei procedimenti da riportare nel dettaglio
		Vector lProcEstratti = dettaglioProcedimentiEstratti(lCodUfficio, lCodMag);

		// Eventuale ricerca dei procedimenti da riportare nel dettaglio
		Vector lProcEstrattiMagistrato = dettaglioProcedimentiEstrattiMagistrato(lCodUfficio, lCodMag);

		// Eventuale ricerca dei procedimenti pendenti da stampare
		Vector lProcPendenti = elencoProcedimentiPendentiEstratti(lCodUfficio, lCodMag); // Mod. Michele
																							// 15/12/2008
		Vector lOggettiPendenti = elencoOggettiPendentiEstratti(lCodUfficio, lCodMag);

		// Ricerca dei procedimenti unificati da stampare
		Vector lProcUnificati = elencoProcedimentiUnificatiEstratti(lCodUfficio, lCodMag);

		// Ricerca degli oggetti cancellati da stampare
		// Vector lOggettiCancellati = elencoOggettiCancellatiEstratti(lCodUfficio, lCodMag);
		Vector lOggettiCancellati = elencoOggettiCancellatiEstratti(lCodUfficio, lCodMag, lDataIni, lDataFin);

		mCancMod = leggiCancelleriaAssegnataria();

		lFiltroCollab = leggiFiltroCollaboratore();

		lFiltroPosGiurid = leggiFiltroPosizioneGiuridica();

		if (lCodMag.compareTo("0") != 0) {
			if (lCodMag.length() < 9) {
				// Ricerca del Magistrato
				lMagistrato = mCtrl.ExRicercaMagistratoByCod(lCodMag);
				strRelatore = lMagistrato.getCognome() + " " + lMagistrato.getNome();
			} else {
				// Ricerca dell' Esperto
				lEsperto = mCtrl.ExRicercaEspertoByCod(lCodMag);
				strRelatore = lEsperto.getCognome() + " " + lEsperto.getNome() + " (ESPERTO)";
				// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				// siesLogger.debug(">>>> Test 011 <<<<"+ nomeMagistrato);
			}

		}

		HSSFWorkbook wb = new HSSFWorkbook();

		// Ricerca riepilogo -- michele 4/12/2008
		Vector lStatistiche = mCtrl.ExRicercaStatisticaOggetti(lCodUfficio);

		// creazione del file excel (foglio dettaglio)
		StatisController lStatisCtrl = new StatisController();
		lStatisCtrl.ExCreateDettaglioOggettiSIUS(lStatistiche, wb, getUfficioUtenteConnesso(), lDataIni,
				lDataFin, lTipoEstrazione, strRelatore, lProcEstratti, lProcEstrattiMagistrato,
				lProcPendenti, lOggettiPendenti, lProcUnificati, lOggettiCancellati, lProcPriviDiRelatore,
				lStatisticheRelatori, mDescOggetto, mCancMod, lFiltroCollab, lFiltroPosGiurid);
		// Generazione file xls
		ByteArrayOutputStream fileOut = new ByteArrayOutputStream();
		try {
			wb.write(fileOut);
		} catch (IOException ioe) {
			throw new F3BException("ActCreaStatisticheOggetti.processRequest: " + ioe);
		}

		setRequestAttribute("report", fileOut);
		setRequestAttribute(IWebConstants.DISPOSITION_FIELD, IWebConstants.ATTACHMENT_DISPOSITION_FILE);

		return IWebConstants.PG_DOWNLOAD_DOCUMENT;
	}

	/**
	 * La funzione effettua la ricerca degli oggetti estratti per codice Oggetto ed eventualmente Cod
	 * magistrato. La ricerca viene effettuata solo se è stato selezionato il check Dettaglio.
	 * 
	 * @param aCodMag
	 * @return
	 * @throws F3BException
	 */
	private Vector dettaglioProcedimentiEstratti(String lCodUfficio, String aCodMag) throws F3BException {
		Vector lElenco = null;
		IspEstrazioneOggettiModel lFiltroModel;
		if (isRequestChecked(CHK_DETTAGLIO) && !isRequestParameterNullObj(CB_LISTA_OGGETTI)) {
			mDescOggetto = new String();
			lFiltroModel = new IspEstrazioneOggettiModel();
			lFiltroModel.setCodOggettoTenore(getRequestStringParameter(CB_LISTA_OGGETTI));
			lFiltroModel.setFasSiuChiaveUfficio(lCodUfficio);
			if (aCodMag != null && !aCodMag.equalsIgnoreCase("0"))
				lFiltroModel.setCodMagistrato(aCodMag);
			lElenco = mCtrl.ExRicercaOggettiEstratti(lFiltroModel);

			// decodifica dell'oggetto
			mDescOggetto = DecodificheUtils.getDescbyCode(DecodificheManager.getInstance()
					.getMotivoProvvedimento(), getRequestStringParameter(CB_LISTA_OGGETTI));
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("FiltroRicercaOggetti" + lFiltroModel.toString());
		}
		return lElenco;
	}

	/**
	 * La funzione effettua la ricerca degli oggetti estratti per Cod magistrato. La ricerca viene effettuata
	 * solo se è stato selezionato il check Dettaglio, ma non è stato selezionato alcun oggetto
	 * 
	 * @param aCodMag
	 * @return
	 * @throws F3BException
	 */
	private Vector dettaglioProcedimentiEstrattiMagistrato(String lCodUfficio, String aCodMag)
			throws F3BException {
		Vector lElenco = null;
		IspEstrazioneOggettiModel lFiltroModel;
		if (aCodMag != null && !aCodMag.equalsIgnoreCase("0")) {
			mDescOggetto = new String();
			lFiltroModel = new IspEstrazioneOggettiModel();
			// lFiltroModel.setCodOggettoTenore(getRequestStringParameter(CB_LISTA_OGGETTI));
			lFiltroModel.setFasSiuChiaveUfficio(lCodUfficio);
			lFiltroModel.setCodMagistrato(aCodMag);
			lElenco = mCtrl.ExRicercaOggettiEstrattiOrdinati(lFiltroModel);

			// decodifica dell'oggetto
			// mDescOggetto =
			// DecodificheUtils.getDescbyCode(DecodificheManager.getInstance().getMotivoProvvedimento(),
			// getRequestStringParameter(CB_LISTA_OGGETTI));
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("FiltroRicercaOggetti" + lFiltroModel.toString());
		}
		return lElenco;
	}

	/**
	 * La funzione effettua la ricerca degli oggetti cancellati estratti
	 * 
	 * @param aCodMag
	 * @return
	 * @throws F3BException
	 */
	private Vector elencoOggettiCancellatiEstratti(String lCodUfficio, String aCodMag, Date aDataInizio,
			Date aDataFine) throws F3BException {
		Vector lElenco = null;

		lElenco = mCtrl.ExRicercaOggettiCancellati(lCodUfficio, aCodMag, aDataInizio, aDataFine);

		return lElenco;

	}

	/**
	 * La funzione effettua la ricerca dei Procedimenti estratti e in stato di "pendente fine periodo" La
	 * ricerca viene effettuata solo se è stato selezionato il check relativo nella form di ricerca.
	 * 
	 * @return Vector
	 * @throws F3BException
	 */
	// Mod. Michele 15/12/2008
	private Vector elencoProcedimentiPendentiEstratti(String lCodUfficio, String aCodMag) throws F3BException {
		Vector lElenco = null;
//		IspEstrazioneOggettiModel lFiltroModel;
		if (isRequestChecked(CHK_PENDENTI)) {
			lElenco = mCtrl.ExRicercaFascicoliPendenti(lCodUfficio, aCodMag);
		}
		return lElenco;
	}

	/**
	 * La funzione effettua la ricerca dei Procedimenti estratti e in stato di "pendente fine periodo" La
	 * ricerca viene effettuata solo se è stato selezionato il check relativo nella form di ricerca.
	 * 
	 * @return Vector
	 * @throws F3BException
	 */
	// Mod. Michele 15/12/2008
	private Vector elencoOggettiPendentiEstratti(String lCodUfficio, String aCodMag) throws F3BException {
		Vector lElenco = null;
//		IspEstrazioneOggettiModel lFiltroModel;
		if (isRequestChecked(CHK_PENDENTI)) {
			lElenco = mCtrl.ExRicercaOggettiPendenti(lCodUfficio, aCodMag);
		}
		return lElenco;
	}

	/**
	 * La funzione effettua la ricerca dei Procedimenti estratti e in stato di "unificato".
	 * 
	 * @return Vector
	 * @throws F3BException
	 */
	private Vector elencoProcedimentiUnificatiEstratti(String lCodUfficio, String aCodMag)
			throws F3BException {
		Vector lElenco = null;
//		IspEstrazioneOggettiModel lFiltroModel;

		lElenco = mCtrl.ExRicercaFascicoliUnificati(lCodUfficio, aCodMag);

		return lElenco;
	}

	/**
	 * La funzione effettua la ricerca dei Procedimenti estratti a cui non è ancora stato assegnato un
	 * relatore. La ricerca viene effettuata solo se è stato selezionato il check per la stampa di dettaglio e
	 * nella lista degli Oggetti è selezionato "Tutti".
	 * 
	 * @return Vector
	 * @throws F3BException
	 */

	private Vector elencoProcedimentiPriviDiRelatoreEstratti(String lCodUfficio) throws F3BException {
		Vector lElenco = null;

		lElenco = mCtrl.ExRicercaFascicoliPriviDiRelatore(lCodUfficio);

		return lElenco;
	}

	/**
	 * La funzione effettua la ricerca dei Procedimenti estratti e in stato di "pendente fine periodo" La
	 * ricerca viene effettuata solo se è stato selezionato il check relativo nella form di ricerca.
	 * 
	 * @return Vector
	 * @throws F3BException
	 */
	protected CancelleriaAssegnatariaModel leggiCancelleriaAssegnataria() throws F3BException {
		CancelleriaAssegnatariaModel lCancMod = null;

		// Se è stato selezionato il filtro per Cancelleria Assegnataria si prepara il model per la ricerca
		CancAssFascSiusModel lCancAssFasc = null;
		if (!isRequestParameterNullObj(ICostantiCancAssFascSius.CAMPO_COD_CANCELLERIA_ASSEGNATARIA)) {
			String lCodCancelleriaAssegnataria = getRequestStringParameter(ICostantiCancAssFascSius.CAMPO_COD_CANCELLERIA_ASSEGNATARIA);
			if (lCodCancelleriaAssegnataria.trim().length() > 0) {
				lCancAssFasc = new CancAssFascSiusModel();
				lCancAssFasc.setCodCancelleriaAssegnataria(lCodCancelleriaAssegnataria);
				// La ricerca è sempre limitata all'Ufficio dell'utente connesso
				lCancAssFasc.setCodUfficio(getCodUfficioUtenteConnesso());
				// Ricerca della Cancelleria Assegnataria da passare nella request
				// per indicarla tra le condizioni di ricerca
				ICancelleriaAssegnataria lCancCtrl = SIUSLookupRemote.getCancelleriaAssegnatariaRemote();
				Vector lCancellerie = lCancCtrl.ExRicercaCancelleriaAssegnataria(lCancAssFasc);
				if (lCancellerie != null && lCancellerie.size() > 0)
					lCancMod = (CancelleriaAssegnatariaModel) lCancellerie.get(0);
			}
		}
		return lCancMod;
	}

	// Funzione di lettura eventuale Filtro sul Collaboratore
	protected String leggiFiltroCollaboratore() throws F3BException {
		String lRet = null;
		if (!isRequestParameterNullObj(FILTRO_COLLABORATORE))
			lRet = getRequestStringParameter(FILTRO_COLLABORATORE);
		return lRet;
	}

	// Funzione di lettura eventuale Filtro sulla posizione giuridica
	protected String leggiFiltroPosizioneGiuridica() throws F3BException {
		String lRet = null;
		if (!isRequestParameterNullObj(FILTRO_POSIZIONE_GIURIDICA))
			lRet = getRequestStringParameter(FILTRO_POSIZIONE_GIURIDICA);
		return lRet;
	}

}