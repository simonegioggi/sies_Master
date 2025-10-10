package siap.sius.statistiche.controller;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.dao.DAOException;
import f3b.log.LogF3B;
import f3b.util.F3BException;
import siap.controller.SiapController;
import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.decodifiche.dao.DecodificheDAO;
import siap.sico.decodifiche.model.DecodificheModel;
import siap.sico.decodifiche.util.DecodificheUtils;
import siap.sico.magistrato.dao.MagistratoSqlDAO;
import siap.sico.magistrato.model.MagistratoModel;
import siap.sico.ufficio.model.UfficioModel;
import siap.siep.statis.dao.EspertoFirmatarioSqlDAO;
import siap.siep.statis.dao.MagistratoFirmatarioSqlDAO;
import siap.sius.SIUSException;
import siap.sius.esperto.dao.EspertoSqlDAO;
import siap.sius.esperto.model.EspertoModel;
import siap.sius.impugnazione.model.ImpugnazioneModel;
import siap.sius.statistiche.action.ICostantiStatistiche;
import siap.sius.statistiche.dao.EveFasGepSogDecSqlDAO;
import siap.sius.statistiche.dao.EveFasGepSogFogSqlDAO;
import siap.sius.statistiche.dao.EveFasGepSogImpSqlDAO;
import siap.sius.statistiche.dao.EveFasGepSogOrdSqlDAO;
import siap.sius.statistiche.dao.EveFasGepSogSenSqlDAO;
import siap.sius.statistiche.dao.EveFasGepSogSqlDAO;
import siap.sius.statistiche.dao.IspConteggioOggettiDAO;
import siap.sius.statistiche.dao.IspConteggioOggettiSenzaRiscontroSqlDAO;
import siap.sius.statistiche.dao.IspConteggioOggettiSqlDAO;
import siap.sius.statistiche.dao.IspConteggioRelatoriDAO;
import siap.sius.statistiche.dao.IspConteggioRelatoriMagistratiSqlDAO;
import siap.sius.statistiche.dao.IspConteggioTempiDAO;
import siap.sius.statistiche.dao.IspEstrazioneOggettiTribDAO;
import siap.sius.statistiche.dao.IspFascicoliSqlDAO;
import siap.sius.statistiche.dao.IspMotivoOggettoSelezionatiDAO;
import siap.sius.statistiche.dao.IspProcIntervalliDAO;
import siap.sius.statistiche.dao.IspRelatoreSelezionatiDAO;
import siap.sius.statistiche.dao.ProcAggregatiCognomeElencoSqlDAO;
import siap.sius.statistiche.dao.ProcAggregatiCognomeSqlDAO;
import siap.sius.statistiche.dao.ProcAggregatiIstitutoDetenzioneSqlDAO;
import siap.sius.statistiche.dao.ProcAggregatiProcuraMittenteSqlDAO;
import siap.sius.statistiche.dao.ProcAttiIstruttoriDataRestSqlDAO;
import siap.sius.statistiche.dao.ProcDataUdienzaFissataNoDefinitiNumGGSqlDAO;
import siap.sius.statistiche.dao.ProcPerStatisticaMisureAlternativeSqlDAO;
import siap.sius.statistiche.dao.ProcPosizioneGiuridicaSqlDAO;
import siap.sius.statistiche.dao.ProcProvvEmessiNoDepositoNumGGSqlDAO;
import siap.sius.statistiche.dao.ProcProvvNoValidatiNoDepositoSqlDAO;
import siap.sius.statistiche.dao.ProcUdiFissateNoDefSqlDAO;
import siap.sius.statistiche.dao.ProcedimentiDlgs123_2018SqlDAO;
import siap.sius.statistiche.dao.StatisSiusStoreProcedureDAO;
import siap.sius.statistiche.model.EveFasGepSogCancModel;
import siap.sius.statistiche.model.EveFasGepSogDetModel;
import siap.sius.statistiche.model.EveFasGepSogModel;
import siap.sius.statistiche.model.EveFasGepSogProvModel;
import siap.sius.statistiche.model.IspConteggioOggettiModel;
import siap.sius.statistiche.model.IspConteggioRelatoriMagistratiModel;
import siap.sius.statistiche.model.IspEstrazioneOggettiModel;
import siap.sius.statistiche.model.IspMotivoOggettoSelezionatiModel;
import siap.sius.statistiche.model.IspProcIntervalliModel;
import siap.sius.statistiche.model.ProcAggregatiCognomeElencoModel;
import siap.sius.statistiche.model.ProcAggregatiCognomeModel;
import siap.sius.statistiche.model.RicercaAggregatiCognomeModel;
import siap.sius.statistiche.model.RicercaOrdinanzaModel;
import siap.sius.statistiche.model.RicercaProcedimentoModel;
import siap.sius.statistiche.model.RicercaProvvedimentoModel;

/**
 * Title: StatisticheSiusController 
 * Description: Classe Controller per Statistiche SIUS
 *
 * @version 1.0
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class StatisticheSiusController extends SiapController implements IStatisticheSius {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	/**
	 * Ritorna n.ro di record risultato della ExRicercaProcSiusXProvvedimentiPaginata
	 *
	 * @param RicercaOrdinanzaModel
	 * @return BigDecimal n.ro di record
	 * @throws F3BException
	 */
	public BigDecimal ExGetNumRicercaProcSiusXProvvedimenti(RicercaOrdinanzaModel aModel)
			throws F3BException {

		Connection lConn = null; // connessione

		// SqlDAO utilizzato viene definito come generico
		EveFasGepSogSqlDAO lDao = null;
		// Risultato
		BigDecimal lCont = new BigDecimal(0);
		// BigDecimal lContAnnullati = new BigDecimal(0);

		try {
			lConn = getDBConnection();

			// Scelta del DAO specializzato al tipo di ricerca
			if (aModel.isRicercaXOrdinanza())
				lDao = new EveFasGepSogOrdSqlDAO(lConn);
			else if (aModel.isRicercaXDecreto())
				lDao = new EveFasGepSogDecSqlDAO(lConn);
			else if (aModel.isRicercaXImpugnazioneRicorso())
				lDao = new EveFasGepSogImpSqlDAO(lConn);
			else if (aModel.isRicercaXFoglioComplementare())
				lDao = new EveFasGepSogFogSqlDAO(lConn);
			else if (aModel.isRicercaXSentenza())
				lDao = new EveFasGepSogSenSqlDAO(lConn);

			else
				throw new F3BException(F3BException.USER_MESSAGE, " Modalità di ricerca non definita !");

			lDao.ricercaProcSiusXProvvedimenti(aModel);
			lCont = lDao.getNumRowsSelected();
			aModel.setNumTotali(lCont);

			// Calcolo parziale effettuato solo se la ricerca non filtra lo stato
			if (aModel.isRicercaStatoTutti()) {
				aModel.setNumAnnullati(ExGetNumAnnullati(aModel, lDao));
				if (aModel.isRicercaXDecreto() || aModel.isRicercaXOrdinanza())
					aModel.setNumNonValidati(ExGetNumNonValidati(aModel, lDao));
				aModel.setCalcolatiTotali(true);
			}
		} catch (F3BException fe) {
			throw fe;
		} catch (Exception e) {
			// MEV_55 Modifica del 11/12/2017 Inizio ********
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("ExGetNumRicercaProcSiusXProvvedimenti - Exception: " + e);
			throw new SIUSException(SIUSException.USER_MESSAGE,
					ICostantiStatistiche.MESSAGGIO_ERRORE_GENERICO);
			// MEV_55 Modifica del 11/12/2017 Fine********
		} finally {
			// Scheda Intervento n° 6 - Ottimizzazione SIUS Avvocati
			cleanup(lDao);
			cleanup(lConn);
		}
		return lCont;
	}

	private BigDecimal ExGetNumAnnullati(RicercaOrdinanzaModel aModel, EveFasGepSogSqlDAO aDao)
			throws Exception {

		BigDecimal lCont = new BigDecimal(0);

		// Si copia RicercaModel per effettuare la stessa ricerca già definita
		// ma aggiungendo solo lo stato Annullato.
		RicercaOrdinanzaModel lRicercaModel = new RicercaOrdinanzaModel(aModel);
		lRicercaModel.setStatoAnnullati();

		aDao.ricercaProcSiusXProvvedimenti(lRicercaModel);
		lCont = aDao.getNumRowsSelected();

		return lCont;
	}

	private BigDecimal ExGetNumNonValidati(RicercaOrdinanzaModel aModel, EveFasGepSogSqlDAO aDao)
			throws Exception {

		BigDecimal lCont = new BigDecimal(0);

		// Si copia RicercaModel per effettuare la stessa ricerca già definita
		// ma aggiungendo solo lo stato Non Validato.
		RicercaOrdinanzaModel lRicercaModel = new RicercaOrdinanzaModel(aModel);
		lRicercaModel.setStatoNonValidati();

		aDao.ricercaProcSiusXProvvedimenti(lRicercaModel);
		lCont = aDao.getNumRowsSelected();

		return lCont;
	}

	public EveFasGepSogProvModel ExRicercaImpugnazioneByAnnoNumUfficio(ImpugnazioneModel aModel)
			throws F3BException {

		Connection lConn = null; // connessione

		// SqlDAO utilizzato
		EveFasGepSogImpSqlDAO lDao = null;
		// Aggregato di dati risultato della ricerca
		EveFasGepSogProvModel lProvvedimento = null;

		try {
			lConn = getDBConnection();

			lDao = new EveFasGepSogImpSqlDAO(lConn);
			lDao.ricercaImpugnazioneEvento(aModel);

			// Ricerca
			lDao.start();
			if (lDao.next())
				lProvvedimento = (EveFasGepSogProvModel) lDao.getModelImpEve();
			else
				throw new F3BException(F3BException.USER_MESSAGE, "Ricorso/Impugnazione non trovato");

			lDao.stop();
		} catch (F3BException fe) {
			throw fe;
		} catch (Exception e) {
			// MEV_55 Modifica del 11/12/2017 Inizio ********
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("ExRicercaImpugnazioneByAnnoNumUfficio - Exception: " + e);
			throw new SIUSException(SIUSException.USER_MESSAGE,
					ICostantiStatistiche.MESSAGGIO_ERRORE_GENERICO);
			// MEV_55 Modifica del 11/12/2017 Fine********
		} finally {
			cleanup(lDao);
			cleanup(lConn);
		}

		return lProvvedimento;
	}

	/**
	 * La funzione realizza una ricerca di Provvedimenti Sius. I Provvedimenti possono essere Ordinanze oppure
	 * Decreti in base alla modalità di ricerca attivata. Il filtro di ricerca da realizzare è codificato nel
	 * RicercaOrdinanzaModel passato come argomento. Il risultato è una lista (Vector) di model
	 * EveFasGepSogProvModel; in questi sono presenti oltre ai dati dell'Ordinanza ( o del Decreto) anche dati
	 * relativi a: Fascicolo Sius, Evento, Soggetto.
	 *
	 * @param aModel
	 *            RicercaOrdinanzaModel.
	 * @return Vector
	 * @throws F3BException
	 */

	public Vector ExRicercaProcSiusXProvvedimentiPaginata(RicercaOrdinanzaModel aModel, int aPageNum)
			throws F3BException {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("" + getClass().getName() + " .ExRicercaProcSiusXProvvedimentiPaginata(): inizio ");
		// connessione
		Connection lConn = null; // connessione

		// SqlDAO utilizzato viene definito come generico
		EveFasGepSogSqlDAO lDao = null;

		// Aggregato di dati risultato della ricerca
		EveFasGepSogProvModel lProvvedimento = null;

		// Elenco risultati
		Vector lElencoProc = new Vector();

		try {
			lConn = getDBConnection();
			// Scelta del DAO specializzato al tipo di ricerca
			if (aModel.isRicercaXOrdinanza())
				lDao = new EveFasGepSogOrdSqlDAO(lConn);
			else if (aModel.isRicercaXDecreto())
				lDao = new EveFasGepSogDecSqlDAO(lConn);
			else if (aModel.isRicercaXImpugnazioneRicorso())
				lDao = new EveFasGepSogImpSqlDAO(lConn);
			else if (aModel.isRicercaXFoglioComplementare())
				lDao = new EveFasGepSogFogSqlDAO(lConn);
			else if (aModel.isRicercaXSentenza())
				lDao = new EveFasGepSogSenSqlDAO(lConn);
			else
				throw new F3BException(F3BException.USER_MESSAGE, " Modalità di ricerca non definita !");

			lDao.ricercaProcSiusXProvvedimenti(aModel);

			// Ricerca paginata
			// Viene fatta la ricerca non paginata se aPageNum ha un valore <= 0
			if (aPageNum > 0)
				lDao.startPage(aPageNum);
			else
				lDao.start();

			while (lDao.next()) {
				lProvvedimento = (EveFasGepSogProvModel) lDao.getModel();
				lElencoProc.add(lProvvedimento);
			}
			lDao.stop();
			if (lElencoProc.isEmpty())
				throw new F3BException(F3BException.USER_MESSAGE, "Nessun Elemento trovato");
		} catch (F3BException fe) {
			throw fe;
		} catch (Exception e) {
			// MEV_55 Modifica del 11/12/2017 Inizio ********
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("ExRicercaProcSiusXProvvedimentiPaginata - Exception: " + e);
			throw new SIUSException(SIUSException.USER_MESSAGE,
					ICostantiStatistiche.MESSAGGIO_ERRORE_GENERICO);
			// MEV_55 Modifica del 11/12/2017 Fine********
		} finally {
			// Scheda Intervento n° 6 - Ottimizzazione SIUS Avvocati
			cleanup(lDao);
			cleanup(lConn);
		}

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("" + getClass().getName() + " .ExRicercaProcSiusXProvvedimentiPaginata(): fine ");

		return lElencoProc;
	}

	/**
	 * Chiama la stored procedure ISPETTORATO_SIUS.stato_oggetti_sius
	 *
	 * @param ufficio
	 *            Codice ufficio
	 * @param aDataInizio
	 *            Data di inizio intervallo di controllo
	 * @param aDataFine
	 *            Data di fine intervallo di controllo
	 * @param aCodCancAss
	 *            codice Cancelleria Assegnataria
	 * @param aFiltroCollab
	 *            Filtro su Collaboratore di Giustizia
	 * @throws F3BException
	 *             propaga l'eccezione.
	 */
	public void ExStatoOggettiSiusStoredProcedure(String aCodUfficio, Date aDataInizio, Date aDataFine,
			String aCodCancAss, String aFiltroCollab, String aFiltroPosGiurid) throws F3BException {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("ExStatoOggettiSiusStoredProcedure : inizio ");

		Connection lConn = null;
		StatisSiusStoreProcedureDAO lSpDao = null;

		try {
			lConn = getDBConnection();

			lSpDao = new StatisSiusStoreProcedureDAO(lConn);
			lSpDao.setStatoOggettiSius();
			lSpDao.setCodUfficio(aCodUfficio);
			lSpDao.setDataInizio(aDataInizio);
			lSpDao.setDataFine(aDataFine);
			lSpDao.setCodCancAssegnataria(aCodCancAss);
			lSpDao.setFiltroCollab(aFiltroCollab);
			lSpDao.setPosizioneGiuridica(aFiltroPosGiurid);
			lSpDao.execute();

			commit(lConn);

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("ExStatoOggettiSiusStoredProcedure : fine ");
		} catch (Exception eEx) {
			rollback(lConn);
			// MEV_55 Modifica del 11/12/2017 Inizio ********
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("ExStatoOggettiSiusStoredProcedure - Exception: " + eEx);
			throw new SIUSException(SIUSException.USER_MESSAGE,
					ICostantiStatistiche.MESSAGGIO_ERRORE_GENERICO);
			// MEV_55 Modifica del 11/12/2017 Inizio ********
		} finally {
			cleanup(lSpDao);
			cleanup(lConn);
		}
	}

	/**
	 * Chiama la stored procedure ISPETTORATO_SIUS.creastatisticaoggetti
	 *
	 * @param aTipoEstrazione
	 * @param aCodMagistrato
	 * @param aDataInizio
	 *            Data di inizio intervallo di controllo
	 * @param aDataFine
	 *            Data di fine intervallo di controllo
	 * @throws F3BException
	 *             propaga l'eccezione.
	 */
	public void ExCreaStatisticaOggettiStoredProcedure(String aCodUfficio, String aCodMagistrato,
			Date aDataInizio, Date aDataFine) throws F3BException {

		Connection lConn = null;
		StatisSiusStoreProcedureDAO lSpDao = null;

		try {

			lConn = getDBConnection();

			lSpDao = new StatisSiusStoreProcedureDAO(lConn);
			lSpDao.setCreaStatisticaOggetti();
			lSpDao.setCodUfficio(aCodUfficio);
			lSpDao.setCodMagistrato(aCodMagistrato);
			lSpDao.setDataInizio(aDataInizio);
			lSpDao.setDataFine(aDataFine);

			lSpDao.execute();

			commit(lConn);

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("ExcreaStatisticaOggettiStoredProcedure : fine ");
		} catch (Exception eEx) {
			rollback(lConn);
			// MEV_55 Modifica del 11/12/2017 Inizio ********
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("ExCreaStatisticaOggettiStoredProcedure - Exception: " + eEx);
			throw new SIUSException(SIUSException.USER_MESSAGE,
					ICostantiStatistiche.MESSAGGIO_ERRORE_GENERICO);
			// MEV_55 Modifica del 11/12/2017 Fine ********
		} finally {
			cleanup(lSpDao);
			cleanup(lConn);
		}
	}

	/**
	 * Chiama la stored procedure ISPETTORATO_SIUS.creastatisticaoggetti
	 *
	 * @param aTipoEstrazione
	 * @param aCodMagistrato
	 * @param aDataInizio
	 *            Data di inizio intervallo di controllo
	 * @param aDataFine
	 *            Data di fine intervallo di controllo
	 * @throws F3BException
	 *             propaga l'eccezione.
	 */
	public void ExCreaStatisticaRelatoriStoredProcedure(String aCodUfficio, Date aDataInizio, Date aDataFine)
			throws F3BException {

		Connection lConn = null;
		StatisSiusStoreProcedureDAO lSpDao = null;

		try {

			lConn = getDBConnection();

			lSpDao = new StatisSiusStoreProcedureDAO(lConn);
			lSpDao.setCreaStatisticaRelatori();
			lSpDao.setCodUfficio(aCodUfficio);
			lSpDao.setDataInizio(aDataInizio);
			lSpDao.setDataFine(aDataFine);

			lSpDao.execute();

			commit(lConn);

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("ExcreaStatisticaMagistratiStoredProcedure : fine ");
		} catch (Exception eEx) {
			rollback(lConn);
			// MEV_55 Modifica del 11/12/2017 Inizio ********
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("ExCreaStatisticaRelatoriStoredProcedure - Exception: " + eEx);
			throw new SIUSException(SIUSException.USER_MESSAGE,
					ICostantiStatistiche.MESSAGGIO_ERRORE_GENERICO);
			// MEV_55 Modifica del 11/12/2017 Fine ********
		} finally {
			cleanup(lSpDao);
			cleanup(lConn);
		}
	}

	/**
	 * Ricerca dei Magistrati associati a Procedimenti SIUS estratti dalle funzioni statistiche e memorizzati
	 * in tabellA ISP_ESTRAZIONE_OGGETTI_TRIB.
	 *
	 * @return
	 * @throws F3BException
	 */
	public Vector ExRicercaMagistratiOggettiEstratti(String aCodUfficio) /* Mod. Michele 2/12/2008 */
			throws F3BException {

		Connection lConn = null;
		Vector lMagistrati = new Vector();
		MagistratoFirmatarioSqlDAO lMagSqlDao = null;

		try {
			lConn = getDBConnection();
			lMagSqlDao = new MagistratoFirmatarioSqlDAO(lConn);
			lMagSqlDao.ricercaMagistratoProSIUS(aCodUfficio); /* Mod. Michele 2/12/2008 */
			lMagSqlDao.start();

			while (lMagSqlDao.next()) {
				lMagistrati.add(lMagSqlDao.getModel());
			}
			if (lMagistrati.size() == 0) {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
				siesLogger.info(
						"MagistratoFirmatarioController.ExRicercaMagistratiOggettiEstratti: Nessun magistrato !");
				// throw new F3BException(F3BException.USER_MESSAGE, "Nessun Elemento trovato");
			}
		} catch (Exception daoEx) {
			// MEV_55 Modifica del 11/12/2017 Inizio ********
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("ExRicercaMagistratiOggettiEstratti - Exception: " + daoEx);
			throw new SIUSException(SIUSException.USER_MESSAGE,
					ICostantiStatistiche.MESSAGGIO_ERRORE_GENERICO);
			// MEV_55 Modifica del 11/12/2017 Fine ********
		} finally {
			cleanup(lMagSqlDao);
			cleanup(lConn);
		}
		return lMagistrati;
	}

	public MagistratoModel ExRicercaMagistratoByCod(String aCodMag) throws F3BException {

		Connection lConn = null;
		MagistratoFirmatarioSqlDAO lMagSqlDao = null;
		MagistratoModel aModel = null;

		try {
			lConn = getDBConnection();
			lMagSqlDao = new MagistratoFirmatarioSqlDAO(lConn);
			lMagSqlDao.ricercaMagistratoByCod(aCodMag);
			aModel = (MagistratoModel) lMagSqlDao.getModelByKey();
		} catch (Exception daoEx) {
			// MEV_55 Modifica del 11/12/2017 Inizio ********
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("ExRicercaMagistratoByCod - Exception: " + daoEx);
			throw new SIUSException(SIUSException.USER_MESSAGE,
					ICostantiStatistiche.MESSAGGIO_ERRORE_GENERICO);
			// MEV_55 Modifica del 11/12/2017 Fine ********
		} finally {
			cleanup(lMagSqlDao);
			cleanup(lConn);
		}
		return aModel;
	}

	public EspertoModel ExRicercaEspertoByCod(String aCodEsp) throws F3BException {

		Connection lConn = null;
		EspertoFirmatarioSqlDAO lEspSqlDao = null;
		EspertoModel aModel = null;

		try {
			lConn = getDBConnection();
			lEspSqlDao = new EspertoFirmatarioSqlDAO(lConn);
			lEspSqlDao.ricercaEspertoByCod(aCodEsp);
			aModel = (EspertoModel) lEspSqlDao.getModelByKey();
		} catch (Exception daoEx) {
			// MEV_55 Modifica del 11/12/2017 Inizio ********
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("ExRicercaEspertoByCod - Exception: " + daoEx);
			throw new SIUSException(SIUSException.USER_MESSAGE,
					ICostantiStatistiche.MESSAGGIO_ERRORE_GENERICO);
			// MEV_55 Modifica del 11/12/2017 Fine ********
		} finally {
			cleanup(lEspSqlDao);
			cleanup(lConn);
		}
		return aModel;
	}

	/**
	 * La funzione restituisce l'elenco dei contatatori statistici contenuti nella tabella
	 * ISP_CONTEGGIO_OGGETTI. Tali dati sono stati memorizzati all'atto della creazione della "Statistica
	 * Procedimenti SIUS X Oggetti" e l'elenco fornito da questa funzione verrà utilizzato per la creazione
	 * del report.
	 *
	 * 2013-11-28 P.Mastroianni Al set di dati viene aggiunto l'elenco degli Oggetti appartenenti a
	 * ISP_MOTIVO_OGGETTO ma che non hanno riscontro nel suddetto elenco
	 *
	 * @return
	 * @throws F3BException
	 */
	public Vector ExRicercaStatisticaOggetti(String aCodUfficio) throws F3BException {

		/* Mod. Michele 4/12/2008 */
		Connection lConn = null;
		Vector lStatistiche = new Vector();

		IspConteggioOggettiDAO lIspDao = null;
		IspConteggioOggettiSenzaRiscontroSqlDAO lIspSqlDAO = null;

		try {
			lConn = getDBConnection(); // Preleva connessione dal dbase
			lIspDao = new IspConteggioOggettiDAO(lConn);
			/* Mod. Michele 4/12/2008 */
			lIspDao.setCondizione("FAS_SIU_CHIAVE_UFFICIO = '" + aCodUfficio+"'");
			lIspDao.setOrdinamentoPerContenutoStatistico();
			lStatistiche = new Vector(lIspDao.getModels()); // Recupera le occorrenze

			// ricerca Oggetti senza riscontro
			lIspSqlDAO = new IspConteggioOggettiSenzaRiscontroSqlDAO(lConn);
			lIspSqlDAO.ricercaOggettiSenzaRiscontro(aCodUfficio);

			// il risultato della query (IspConteggioOggettiSenzaRiscontroSqlDAO)
			// viene accodato al vettore precedentemente popolato
			// con i dati di IspConteggioOggettiDAO
			lStatistiche.addAll(lIspSqlDAO.getModels());
		} catch (Exception daoEx) {
			// MEV_55 Modifica del 11/12/2017 Inizio ********
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("ExRicercaStatisticaOggetti - Exception: " + daoEx);
			throw new SIUSException(SIUSException.USER_MESSAGE,
					ICostantiStatistiche.MESSAGGIO_ERRORE_GENERICO);
			// MEV_55 Modifica del 11/12/2017 Fine********
		} finally {
			cleanup(lIspDao);
			cleanup(lIspSqlDAO);
			cleanup(lConn);
		}
		return lStatistiche;
	}

	/**
	 * La funzione restituisce l'elenco dei contatatori statistici contenuti nella tabella
	 * ISP_CONTEGGIO_MAGISTRATI. Tali dati sono stati memorizzati all'atto della creazione della "Statistica
	 * Procedimenti SIUS X Oggetti" e l'elenco fornito da questa funzione verrà utilizzato per la creazione
	 * del report.
	 *
	 * @return
	 * @throws F3BException
	 */
	public Vector ExRicercaStatisticaRelatori(String aCodUfficio) throws F3BException {

		Connection lConn = null;
		Vector lStatistiche = new Vector();

		IspConteggioRelatoriDAO lIspDao = null;

		try {
			lConn = getDBConnection(); // Preleva connessione dal dbase
			lIspDao = new IspConteggioRelatoriDAO(lConn);

			lIspDao.setCondizione("FAS_SIU_CHIAVE_UFFICIO = " + aCodUfficio);
			lIspDao.setOrdinamentoPerCodiceRelatore();
			lStatistiche = new Vector(lIspDao.getModels()); // Recupera le occorrenze
		} catch (Exception daoEx) {
			// MEV_55 Modifica del 11/12/2017 Inizio ********
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("ExRicercaStatisticaRelatori - Exception: " + daoEx);
			throw new SIUSException(SIUSException.USER_MESSAGE,
					ICostantiStatistiche.MESSAGGIO_ERRORE_GENERICO);
			// MEV_55 Modifica del 11/12/2017 Fine********
		} finally {
			cleanup(lIspDao);
			cleanup(lConn);
		}
		return lStatistiche;
	}

	/**
	 * Chiama la stored procedure ISPETTORATO_SIUS.estraiProcedimentiDepositati. La stored procedure attivata
	 * effettua l'estrazione dei provvedimenti SIUS depositati nel periodo specificato ed emessi dall'ufficio
	 * e ne deposita i dati nella tabella ISP_PROC_INTERVALLI.
	 *
	 * @param ufficio
	 *            Codice ufficio
	 * @param aDataInizio
	 *            Data di inizio intervallo di controllo
	 * @param aDataFine
	 *            Data di fine intervallo di controllo
	 * @param aCodCancAss
	 *            codice Cancelleria Assegnataria
	 * @param aFiltroCollab
	 *            Filtro su Collaboratore di Giustizia
	 * @param aPosGiu
	 *            Filtro sulla Posizione Giuridica
	 * @throws F3BException
	 *             propaga l'eccezione.
	 */
	public void ExEstraiProcedimentiDepositatiStProc(String aCodUfficio, Date aDataInizio, Date aDataFine,
			String aCodCancAss, String aFiltroCollab, String aPosGiu) throws F3BException {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("ExEstraiProcedimentiDepositatiStProc : inizio ");

		Connection lConn = null;
		StatisSiusStoreProcedureDAO lSpDao = null;

		try {
			lConn = getDBConnection();

			lSpDao = new StatisSiusStoreProcedureDAO(lConn);
			lSpDao.setEstraiProcedimentiDepositati();
			lSpDao.setCodUfficio(aCodUfficio);
			lSpDao.setDataInizio(aDataInizio);
			lSpDao.setDataFine(aDataFine);
			lSpDao.setCodCancAssegnataria(aCodCancAss);
			lSpDao.setFiltroCollab(aFiltroCollab);
			lSpDao.setPosizioneGiuridica(aPosGiu);

			lSpDao.execute();

			commit(lConn);

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("ExEstraiProcedimentiDepositatiStProc : fine ");
		} catch (Exception eEx) {
			rollback(lConn);
			// MEV_55 Modifica del 11/12/2017 Inizio ********
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("ExEstraiProcedimentiDepositatiStProc - Exception: " + eEx);
			throw new SIUSException(SIUSException.USER_MESSAGE,
					ICostantiStatistiche.MESSAGGIO_ERRORE_GENERICO);
			// MEV_55 Modifica del 11/12/2017 Fine ********
		} finally {
			cleanup(lSpDao);
			cleanup(lConn);
		}
	}

	/**
	 * Chiama la stored procedure ISPETTORATO_SIUS.creaStatisticaTempi. La stored procedure attivata effettua
	 * il conteggio dei tempi e ne deposita i dati nella tabella ISP_CONTEGGIO_TEMPI.
	 *
	 * @param aCodUfficio
	 *            Codice ufficio
	 * @param aCodMagistrato
	 *            Magistrato Assegnatario
	 * @param aDataInizio
	 *            Data di inizio intervallo di controllo
	 * @param aDataFine
	 *            Data di fine intervallo di controllo
	 * @throws F3BException
	 *             propaga l'eccezione.
	 */
	public void ExCreaStatisticaTempiStProc(String aCodUfficio, String aCodMagistrato, Date aDataInizio,
			Date aDataFine) throws F3BException {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("ExCreaStatisticaTempiStProc : inizio ");

		Connection lConn = null;
		StatisSiusStoreProcedureDAO lSpDao = null;

		try {

			lConn = getDBConnection();

			lSpDao = new StatisSiusStoreProcedureDAO(lConn);
			lSpDao.setCreaStatisticaTempi();
			lSpDao.setCodUfficio(aCodUfficio);
			lSpDao.setCodMagistrato(aCodMagistrato);
			lSpDao.setDataInizio(aDataInizio);
			lSpDao.setDataFine(aDataFine);

			lSpDao.execute();

			commit(lConn);

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("ExCreaStatisticaTempiStProc : fine ");
		} catch (Exception eEx) {
			rollback(lConn);
			// MEV_55 Modifica del 11/12/2017 Inizio ********
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("ExCreaStatisticaTempiStProc - Exception: " + eEx);
			throw new SIUSException(SIUSException.USER_MESSAGE,
					ICostantiStatistiche.MESSAGGIO_ERRORE_GENERICO);
			// MEV_55 Modifica del 11/12/2017 Fine********
		} finally {
			cleanup(lSpDao);
			cleanup(lConn);
		}
	}

	/**
	 * La funzione restituisce l'elenco dei contatatori statistici contenuti nella tabella
	 * ISP_CONTEGGIO_TEMPI. Tali dati sono stati memorizzati all'atto della creazione della funzione
	 * Statistica: "Numero Procedimenti SIUS per Principali Intervalli Temporali" L'elenco fornito da questa
	 * funzione verrà utilizzato per la creazione del report XLS.
	 *
	 * @return
	 * @throws F3BException
	 */
	public Vector ExRicercaStatisticaTempi(String aCodUfficio) throws F3BException {

		/* mod. michele 5/12/2008 */
		Connection lConn = null;
		Vector lStatistiche = new Vector();

		IspConteggioTempiDAO lIspDao = null;

		try {
			lConn = getDBConnection(); // Preleva connessione dal dbase
			lIspDao = new IspConteggioTempiDAO(lConn);
			/* Mod. Michele 5/12/2008 */
			lIspDao.setCondizione("FAS_SIU_CHIAVE_UFFICIO = " + aCodUfficio);
			lIspDao.setOrdinamentoPerContenutoStatistico();
			lStatistiche = new Vector(lIspDao.getModels()); // Recupara le occorrenze
		} catch (Exception daoEx) {
			// MEV_55 Modifica del 11/12/2017 Inizio ********
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("ExRicercaStatisticaTempi - Exception: " + daoEx);
			throw new SIUSException(SIUSException.USER_MESSAGE,
					ICostantiStatistiche.MESSAGGIO_ERRORE_GENERICO);
			// MEV_55 Modifica del 11/12/2017 Fine********
		} finally {
			cleanup(lIspDao);
			cleanup(lConn);
		}
		return lStatistiche;
	}

	/**
	 * La funzione restituisce l'elenco degli oggetti estratti dalle funzioni statistiche e memorizzati in
	 * tabellA ISP_ESTRAZIONE_OGGETTI_TRIB.
	 *
	 * @return
	 * @throws F3BException
	 */
	public Vector ExListaOggettiEstratti(String aNomeTabEstrazione) throws F3BException {

		Connection lConn = null;
		Vector lOggetti = new Vector();

		DecodificheDAO lDecDao = null;

		try {
			lConn = getDBConnection(); // Preleva connessione dal dbase
			lDecDao = new DecodificheDAO(lConn);
			lDecDao.setRicercaOggettiStatisticaSius(aNomeTabEstrazione); // Predispone le condizioni di
																			// ricerca
			lOggetti = new Vector(lDecDao.getModels()); // Recupara le occorrenze
		} catch (Exception daoEx) {
			// MEV_55 Modifica del 11/12/2017 Inizio ********
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("ExListaOggettiEstratti - Exception: " + daoEx);
			throw new SIUSException(SIUSException.USER_MESSAGE,
					ICostantiStatistiche.MESSAGGIO_ERRORE_GENERICO);
			// MEV_55 Modifica del 11/12/2017 Fine********
		} finally {
			cleanup(lDecDao);
			cleanup(lConn);
		}
		return lOggetti;
	}

	/**
	 * La funzione restituisce l'elenco degli oggetti estratti dalle funzioni statistiche e memorizzati in
	 * tabellA ISP_ESTRAZIONE_OGGETTI_TRIB per l'ufficio dell'utente collegato Inserita da Michele il
	 * 5/12/2008
	 *
	 * @return
	 * @throws F3BException
	 */
	public Vector ExListaOggettiEstratti(String aNomeTabEstrazione, String aCodUfficio) throws F3BException {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("# ExListaOggettiEstratti - START #");

		Connection lConn = null;
		Vector lOggetti = new Vector();
		DecodificheDAO lDecDao = null;

		try {
			lConn = getDBConnection(); // Preleva connessione dal dbase
			lDecDao = new DecodificheDAO(lConn);
			lDecDao.setRicercaOggettiStatisticaSius(aNomeTabEstrazione, aCodUfficio); // Predispone le
																						// condizioni di
																						// ricerca
			lOggetti = new Vector(lDecDao.getModels()); // Recupara le occorrenze
		} catch (Exception daoEx) {
			// MEV_55 Modifica del 11/12/2017 Inizio ********
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("ExListaOggettiEstratti - Exception: " + daoEx);
			throw new SIUSException(SIUSException.USER_MESSAGE,
					ICostantiStatistiche.MESSAGGIO_ERRORE_GENERICO);
			// MEV_55 Modifica del 11/12/2017 Fine********
		} finally {
			cleanup(lDecDao);
			cleanup(lConn);
		}

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("# ExListaOggettiEstratti - STOP #");

		return lOggetti;
	}

	/**
	 * La funzione restituisce tutti i record dalla tabella ISP_ESTRAZIONE_OGGETTI_TRIB filtrati per
	 * COD_OGGETTO_TENORE ed eventualmente anche per COD_MAGISTRATO.
	 *
	 * @param IspEstrazioneOggettiModel
	 *            : filtro di ricerca
	 * @return Vector
	 * @throws F3BException
	 */
	public Vector ExRicercaOggettiEstratti(IspEstrazioneOggettiModel aModel) throws F3BException {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("# ExRicercaOggettiEstratti - START #");

		Connection lConn = null;
		Vector lOggetti = new Vector();
		IspEstrazioneOggettiTribDAO lDao = null;

		try {
			lConn = getDBConnection(); // Preleva connessione dal dbase
			lDao = new IspEstrazioneOggettiTribDAO(lConn);
			lDao.setCondizione(aModel);
			lOggetti = new Vector(lDao.getModels()); // Recupera le occorrenze
		} catch (Exception daoEx) {
			// MEV_55 Modifica del 11/12/2017 Inizio ********
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("ExRicercaOggettiEstratti - Exception: " + daoEx);
			throw new SIUSException(SIUSException.USER_MESSAGE,
					ICostantiStatistiche.MESSAGGIO_ERRORE_GENERICO);
			// MEV_55 Modifica del 11/12/2017 Fine********
		} finally {
			cleanup(lDao);
			cleanup(lConn);
		}

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("# ExRicercaOggettiEstratti - STOP #");

		return lOggetti;
	}

	/**
	 * La funzione restituisce tutti i record dalla tabella ISP_ESTRAZIONE_OGGETTI_TRIB filtrati per
	 * COD_OGGETTO_TENORE ed eventualmente anche per COD_MAGISTRATO. ordinati per COD_OGGETTO_TENORE
	 *
	 * @param IspEstrazioneOggettiModel
	 *            : filtro di ricerca
	 * @return Vector
	 * @throws F3BException
	 */
	public Vector ExRicercaOggettiEstrattiOrdinati(IspEstrazioneOggettiModel aModel) throws F3BException {

		Connection lConn = null;
		Vector lOggetti = new Vector();
		IspEstrazioneOggettiTribDAO lDao = null;

		try {
			lConn = getDBConnection(); // Preleva connessione dal dbase
			lDao = new IspEstrazioneOggettiTribDAO(lConn);
			lDao.setCondizioneOrdinataPerOggetto(aModel);
			lOggetti = new Vector(lDao.getModels()); // Recupera le occorrenze
		} catch (Exception daoEx) {
			// MEV_55 Modifica del 11/12/2017 Inizio ********
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("ExRicercaOggettiEstrattiOrdinati - Exception: " + daoEx);
			throw new SIUSException(SIUSException.USER_MESSAGE,
					ICostantiStatistiche.MESSAGGIO_ERRORE_GENERICO);
			// MEV_55 Modifica del 11/12/2017 Fine********
		} finally {
			cleanup(lDao);
			cleanup(lConn);
		}
		return lOggetti;
	}

	/**
	 * La funzione restituisce tutti i record dalla tabella ISP_PROC_INTERVALLI filtrati in base alle
	 * condizioni passate attraverso il model (COD_OGGETTO_TENORE).
	 *
	 * @param IspEstrazioneOggettiModel
	 *            : filtro di ricerca
	 * @return Vector
	 * @throws F3BException
	 */
	public Vector ExRicercaTempiEstratti(IspProcIntervalliModel aModel) throws F3BException {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.info("####### ExRicercaTempiEstratti - START ####### ");

		Connection lConn = null;
		Vector<IspProcIntervalliModel> lOggetti = new Vector<>();
		IspProcIntervalliDAO lDao = null;
		MagistratoSqlDAO lMagSqlDao = null;
		// 20180110 [EC] risoluzione anomalia n. 3 plo mev 55
		EspertoSqlDAO lEspDao = null;
		EspertoModel lEspMod = null;

		try {
			lConn = getDBConnection(); // Preleva connessione dal dbase
			lMagSqlDao = new MagistratoSqlDAO(lConn);
			lEspDao = new EspertoSqlDAO(lConn);

			lDao = new IspProcIntervalliDAO(lConn);
			lDao.setCondizione(aModel);
			lDao.setOrder(aModel); // 20131124 - eventuale ordinamento, se valorizzato.

			IspProcIntervalliModel lProcInt = null;
			MagistratoModel lMag = null;
			lDao.start();

			while (lDao.next()) {
				lProcInt = (IspProcIntervalliModel) lDao.getModel();
				// Ricerca magistrato per recupero Nome e Cognome.
				String codiceM = lProcInt.getCodMagistrato();

				// 20180110 [EC] risoluzione anomalia n. 3 plo mev 55
				if (codiceM != null && codiceM.indexOf(" (ESPERTO)") != -1) {
					// Esperto
					codiceM = codiceM.substring(0, codiceM.indexOf(" (ESPERTO)"));
					lEspDao.ricercaEspertoByKey(new BigDecimal(codiceM));
					lEspMod = (EspertoModel) lEspDao.getModelByKey();
					if (lEspMod != null)
						lProcInt.setDescrMagistrato(lEspMod.getCognome() + " " + lEspMod.getNome());
				} else if (codiceM != null) {
					// Magistrato Relatore
					lMagSqlDao.ricercaMagistratoByCod(lProcInt.getCodMagistrato());
					lMag = (MagistratoModel) lMagSqlDao.getModelByKey();
					// Valorizza il campo descr magistrato del model IspProcIntervalliModel.
					if (lMag != null)
						lProcInt.setDescrMagistrato(lMag.getCognome() + " " + lMag.getNome());
				}
				lOggetti.add(lProcInt);
			}

			lDao.stop();
		} catch (Exception daoEx) {
			// MEV_55 Modifica del 11/12/2017 Inizio ********
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("ExRicercaTempiEstratti - Exception: " + daoEx);
			throw new SIUSException(SIUSException.USER_MESSAGE,
					ICostantiStatistiche.MESSAGGIO_ERRORE_GENERICO);
			// MEV_55 Modifica del 11/12/2017 Fine********
		} finally {
			cleanup(lDao);
			cleanup(lMagSqlDao);
			// Scheda Intervento n° 6 - Ottimizzazione SIUS Avvocati
			cleanup(lEspDao);
			cleanup(lConn);
		}

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.info("####### ExRicercaTempiEstratti - STOP ####### ");
		return lOggetti;
	}

	/**
	 * La funzione restituisce dalla tabella ISP_ESTRAZIONE_OGGETTI_TRIB l'elenco dei Fascicoli SIUS che
	 * risultino in stato "pendente" (cioè con DEFINITO = 'N).
	 *
	 * @param IspEstrazioneOggettiModel
	 *            : filtro di ricerca
	 * @return Vector
	 * @throws F3BException
	 */
	/* Mod. Michele 15/12/2008 */
	public Vector ExRicercaFascicoliPendenti(String aCodUfficio, String aCodMag) throws F3BException {

		Connection lConn = null;
		Vector lOggetti = new Vector();
		IspFascicoliSqlDAO lDao = null;

		try {
			lConn = getDBConnection(); // Preleva connessione dal dbase
			lDao = new IspFascicoliSqlDAO(lConn);
			lDao.ricercaFascicoliEstrattiPendenti(aCodUfficio, aCodMag);
			lOggetti = new Vector(lDao.getModels()); // Recupera le occorrenze
		} catch (Exception daoEx) {
			// MEV_55 Modifica del 11/12/2017 Inizio ********
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("ExRicercaFascicoliPendenti - Exception: " + daoEx);
			throw new SIUSException(SIUSException.USER_MESSAGE,
					ICostantiStatistiche.MESSAGGIO_ERRORE_GENERICO);
			// MEV_55 Modifica del 11/12/2017 Fine********
		} finally {
			cleanup(lDao);
			cleanup(lConn);
		}
		return lOggetti;
	}

	/**
	 * La funzione restituisce dalla tabella ISP_ESTRAZIONE_OGGETTI_TRIB l'elenco degli Oggetti SIUS che
	 * risultino in stato "pendente" (cioè con DEFINITO = 'N).
	 *
	 * @param IspEstrazioneOggettiModel
	 *            : filtro di ricerca
	 * @return Vector
	 * @throws F3BException
	 */
	/* Mod. Michele 15/12/2008 */
	public Vector ExRicercaOggettiPendenti(String aCodUfficio, String aCodMag) throws F3BException {

		Connection lConn = null;
		Vector lOggetti = new Vector();
		IspFascicoliSqlDAO lDao = null;

		try {
			lConn = getDBConnection(); // Preleva connessione dal dbase
			lDao = new IspFascicoliSqlDAO(lConn);
			lDao.ricercaOggettiEstrattiPendenti(aCodUfficio, aCodMag);
			lOggetti = new Vector(lDao.getModels()); // Recupera le occorrenze
		} catch (Exception daoEx) {
			// MEV_55 Modifica del 11/12/2017 Inizio ********
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("ExRicercaOggettiPendenti - Exception: " + daoEx);
			throw new SIUSException(SIUSException.USER_MESSAGE,
					ICostantiStatistiche.MESSAGGIO_ERRORE_GENERICO);
			// MEV_55 Modifica del 11/12/2017 Fine********
		} finally {
			cleanup(lDao);
			cleanup(lConn);
		}
		return lOggetti;
	}

	/**
	 * La funzione restituisce dalla tabella ISP_ESTRAZIONE_OGGETTI_TRIB l'elenco dei Fascicoli SIUS che
	 * risultino in stato "unificato" (cioè con FAS_SIU_COD_STATO_FASCICOLO = '05).
	 *
	 * @param IspEstrazioneOggettiModel
	 *            : filtro di ricerca
	 * @return Vector
	 * @throws F3BException
	 */
	/* Mod. Michele 15/12/2008 */
	public Vector ExRicercaFascicoliUnificati(String aCodUfficio, String aCodMag) throws F3BException {

		Connection lConn = null;
		Vector lOggetti = new Vector();
		IspFascicoliSqlDAO lDao = null;

		try {
			lConn = getDBConnection(); // Preleva connessione dal dbase
			lDao = new IspFascicoliSqlDAO(lConn);
			lDao.ricercaFascicoliEstrattiUnificati(aCodUfficio, aCodMag);
			lOggetti = new Vector(lDao.getModels()); // Recupera le occorrenze
		} catch (Exception daoEx) {
			// MEV_55 Modifica del 11/12/2017 Inizio ********
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("ExRicercaFascicoliUnificati - Exception: " + daoEx);
			throw new SIUSException(SIUSException.USER_MESSAGE,
					ICostantiStatistiche.MESSAGGIO_ERRORE_GENERICO);
			// MEV_55 Modifica del 11/12/2017 Fine********
		} finally {
			cleanup(lDao);
			cleanup(lConn);
		}
		return lOggetti;
	}

	/**
	 * La funzione restituisce dalla tabella ISP_ESTRAZIONE_OGGETTI_TRIB l'elenco dei Fascicoli SIUS che
	 * risultino ancora privi di un relatore assegnato.
	 *
	 * @param IspEstrazioneOggettiModel
	 *            : filtro di ricerca
	 * @return Vector
	 * @throws F3BException
	 */
	public Vector ExRicercaFascicoliPriviDiRelatore(String aCodUfficio) throws F3BException {

		Connection lConn = null;
		Vector lOggetti = new Vector();
		IspEstrazioneOggettiTribDAO lDao = null;

		try {
			lConn = getDBConnection(); // Preleva connessione dal dbase
			lDao = new IspEstrazioneOggettiTribDAO(lConn);
			lDao.setCondizioneMagNull(aCodUfficio);
			lOggetti = new Vector(lDao.getModels()); // Recupera le occorrenze
		} catch (Exception daoEx) {
			// MEV_55 Modifica del 11/12/2017 Inizio ********
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("ExRicercaFascicoliPriviDiRelatore - Exception: " + daoEx);
			throw new SIUSException(SIUSException.USER_MESSAGE,
					ICostantiStatistiche.MESSAGGIO_ERRORE_GENERICO);
			// MEV_55 Modifica del 11/12/2017 Fine********
		} finally {
			cleanup(lDao);
			cleanup(lConn);
		}
		return lOggetti;
	}

	/**
	 * La funzione restituisce dalla tabella ISP_ESTRAZIONE_OGGETTI_TRIB l'elenco degli oggetti SIUS che
	 * risultino cancellati nel periodo
	 *
	 * @param IspEstrazioneOggettiModel
	 *            : filtro di ricerca
	 * @return Vector
	 * @throws F3BException
	 */
	public Vector ExRicercaOggettiCancellati(String aCodUfficio, String aCodMag, Date aDataInizio,
			Date aDataFine) throws F3BException {

		Connection lConn = null;
		Vector lOggetti = new Vector();
		IspEstrazioneOggettiTribDAO lDao = null;

		try {
			lConn = getDBConnection(); // Preleva connessione dal dbase
			lDao = new IspEstrazioneOggettiTribDAO(lConn);
			// lDao.setCondizioneTenDataFineNotNull(aCodUfficio, aCodMag);
			lDao.setCondizioneTenDataFineCancellati(aCodUfficio, aCodMag, aDataInizio, aDataFine);
			lOggetti = new Vector(lDao.getModels()); // Recupera le occorrenze
		} catch (Exception daoEx) {
			// MEV_55 Modifica del 11/12/2017 Inizio ********
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("ExRicercaOggettiCancellati - Exception: " + daoEx);
			throw new SIUSException(SIUSException.USER_MESSAGE,
					ICostantiStatistiche.MESSAGGIO_ERRORE_GENERICO);
			// MEV_55 Modifica del 11/12/2017 Fine********
		} finally {
			cleanup(lDao);
			cleanup(lConn);
		}
		return lOggetti;
	}

	/**
	 *
	 * @param RicercaProcedimentoModel
	 * @return Collection
	 * @throws F3BException
	 */
	public Collection<EveFasGepSogProvModel> ExRicercaProcFissatiNoDef(RicercaProcedimentoModel aModel)
			throws F3BException {

		Collection<EveFasGepSogProvModel> lProcedimenti = null;
		lProcedimenti = this.ExRicercaProcFissatiNoDefPaginata(aModel, 0);

		return lProcedimenti;
	}

	/**
	 * Ritorna i records paginati; se aPagina == 0, ritorna tutti i records
	 *
	 * @param RicercaProcedimentoModel
	 * @return Collection
	 * @throws F3BException
	 */
	public Collection<EveFasGepSogProvModel> ExRicercaProcFissatiNoDefPaginata(
			RicercaProcedimentoModel aModel, int aPagina) throws F3BException {

		Connection lConn = null;
		Collection<EveFasGepSogProvModel> lProcedimenti = null;
		ProcUdiFissateNoDefSqlDAO lSqlDao = null;

		try {
			lConn = getDBConnection(); // Preleva connessione dal dbase
			lSqlDao = new ProcUdiFissateNoDefSqlDAO(lConn);
			lSqlDao.ricercaProcedimentiPerDataUdienza(aModel);

			if (aPagina > 0) {
				lSqlDao.startPage(aPagina);
			} else {
				lSqlDao.start();
			}

			lProcedimenti = new ArrayList<>();

			// Necessario iterare per via della paginazione.
			while (lSqlDao.next()) {
				EveFasGepSogProvModel lModel = (EveFasGepSogProvModel) lSqlDao.getModel();
				lProcedimenti.add(lModel);
			}
			lSqlDao.stop();
		} catch (Exception ex) {
			// MEV_55 Modifica del 11/12/2017 Inizio ********
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("ExRicercaProcFissatiNoDefPaginata - Exception: " + ex);
			throw new SIUSException(SIUSException.USER_MESSAGE,
					ICostantiStatistiche.MESSAGGIO_ERRORE_GENERICO);
			// MEV_55 Modifica del 11/12/2017 Fine********
		} finally {
			cleanup(lSqlDao);
			cleanup(lConn);
		}
		return lProcedimenti;
	}

	/**
	 * Ritorna il numero di records selezionati in base al filtro passato come model
	 *
	 * @param RicercaProcedimentoModel
	 * @return BigDecimal
	 * @throws F3BException
	 */
	public BigDecimal ExGetNumRicercaProcFissatiNoDef(RicercaProcedimentoModel aModel) throws F3BException {

		BigDecimal lRecords = new BigDecimal(0);
		Connection lConn = null;
		ProcUdiFissateNoDefSqlDAO lSqlDao = null;

		try {
			lConn = getDBConnection(); // Preleva connessione dal dbase
			lSqlDao = new ProcUdiFissateNoDefSqlDAO(lConn);
			lSqlDao.ricercaProcedimentiPerDataUdienza(aModel);
			lRecords = lSqlDao.getNumRowsSelected();
		} catch (Exception ex) {
			// MEV_55 Modifica del 11/12/2017 Inizio ********
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("ExGetNumRicercaProcFissatiNoDef - Exception: " + ex);
			throw new SIUSException(SIUSException.USER_MESSAGE,
					ICostantiStatistiche.MESSAGGIO_ERRORE_GENERICO);
			// MEV_55 Modifica del 11/12/2017 Fine********
		} finally {
			cleanup(lSqlDao);
			cleanup(lConn);
		}
		return lRecords;
	}

	public Collection<EveFasGepSogProvModel> ExRicercaProcPerProvvNoValidatiNoDeposito(
			RicercaProcedimentoModel aModel) throws F3BException {

		return ExRicercaProcPerProvvNoValidatiNoDepositoPaginata(aModel, 0);
	}

	public Collection<EveFasGepSogProvModel> ExRicercaProcPerProvvNoValidatiNoDepositoPaginata(
			RicercaProcedimentoModel aModel, int aPagina) throws F3BException {

		Connection lConn = null;
		Collection<EveFasGepSogProvModel> lProcedimenti = null;
		ProcProvvNoValidatiNoDepositoSqlDAO lSqlDao = null;

		try {
			lConn = getDBConnection(); // Preleva connessione dal dbase
			lSqlDao = new ProcProvvNoValidatiNoDepositoSqlDAO(lConn);

			switch (aModel.getStatoProcedimento()) {
			case 0:
				lSqlDao.ricercaProcedimentiPriviProvvedimenti(aModel);
				break;
			case 1:
				lSqlDao.ricercaProcedimentiPerProvvedimentiNoValidati(aModel);
				break;
			case 2:
				lSqlDao.ricercaProcedimentiPerProvvedimentiNoDepositati(aModel);
				break;
			case 3:
				lSqlDao.ricercaProcedimentiPerProvvedimentiDepostatiNoValidati(aModel);
				break;
			default:
				throw new F3BException(
						"ExRicercaProcPerProvvNoValidatiNoDepositoPaginata : Valore dell StatoProcedimento = "
								+ aModel.getStatoProcedimento()
								+ " non valido. Deve essere compreso nel range 0-3.");
			}

			if (aPagina > 0) {
				lSqlDao.startPage(aPagina);
			} else {
				lSqlDao.start();
			}

			lProcedimenti = new ArrayList<>();
			while (lSqlDao.next()) {
				EveFasGepSogProvModel lModel = (EveFasGepSogProvModel) lSqlDao.getModel();
				lProcedimenti.add(lModel);
			}
			lSqlDao.stop();
		} catch (Exception ex) {
			// MEV_55 Modifica del 11/12/2017 Inizio ********
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("ExRicercaProcPerProvvNoValidatiNoDepositoPaginata - Exception: " + ex);
			throw new SIUSException(SIUSException.USER_MESSAGE,
					ICostantiStatistiche.MESSAGGIO_ERRORE_GENERICO);
			// MEV_55 Modifica del 11/12/2017 Fine********
		} finally {
			cleanup(lSqlDao);
			cleanup(lConn);
		}
		return lProcedimenti;
	}

	public BigDecimal ExGetNumRicercaProcPerProvvNoValidatiNoDeposito(RicercaProcedimentoModel aModel)
			throws F3BException {

		BigDecimal lRecords = new BigDecimal(0);
		Connection lConn = null;
		ProcProvvNoValidatiNoDepositoSqlDAO lSqlDao = null;

		try {
			lConn = getDBConnection(); // Preleva connessione dal dbase
			lSqlDao = new ProcProvvNoValidatiNoDepositoSqlDAO(lConn);

			switch (aModel.getStatoProcedimento()) {
			case 0:
				lSqlDao.ricercaProcedimentiPriviProvvedimenti(aModel);
				break;
			case 1:
				lSqlDao.ricercaProcedimentiPerProvvedimentiNoValidati(aModel);
				break;
			case 2:
				lSqlDao.ricercaProcedimentiPerProvvedimentiNoDepositati(aModel);
				break;
			case 3:
				lSqlDao.ricercaProcedimentiPerProvvedimentiDepostatiNoValidati(aModel);
				break;
			default:
				throw new F3BException(
						"ExGetNumRicercaProcPerProvvNoValidatiNoDeposito : Valore dell StatoProcedimento = "
								+ aModel.getStatoProcedimento()
								+ " non valido. Deve essere compreso nel range 0-3.");
			}

			lRecords = lSqlDao.getNumRowsSelected();
		} catch (Exception ex) {
			// MEV_55 Modifica del 11/12/2017 Inizio ********
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("ExGetNumRicercaProcPerProvvNoValidatiNoDeposito - Exception: " + ex);
			throw new SIUSException(SIUSException.USER_MESSAGE,
					ICostantiStatistiche.MESSAGGIO_ERRORE_GENERICO);
			// MEV_55 Modifica del 11/12/2017 Fine********
		} finally {
			cleanup(lSqlDao);
			cleanup(lConn);
		}
		return lRecords;
	}

	@Override
	public Collection<ProcAggregatiCognomeModel> ExRicercaProcAggregati1Lettera(
			RicercaAggregatiCognomeModel aModel) throws F3BException {

		Connection lConn = null;
		Collection<ProcAggregatiCognomeModel> lProcedimenti = null;
		ProcAggregatiCognomeSqlDAO lSqlDao = null;

		try {
			lConn = getDBConnection(); // Preleva connessione dal dbase
			lSqlDao = new ProcAggregatiCognomeSqlDAO(lConn);

			lSqlDao.ricercaAggregati1Lettera(aModel);

			lProcedimenti = lSqlDao.getModels();
			lSqlDao.stop();
		} catch (Exception ex) {
			// MEV_55 Modifica del 11/12/2017 Inizio ********
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("ExRicercaProcAggregati1Lettera - Exception: " + ex);
			throw new SIUSException(SIUSException.USER_MESSAGE,
					ICostantiStatistiche.MESSAGGIO_ERRORE_GENERICO);
			// MEV_55 Modifica del 11/12/2017 Fine********
		} finally {
			cleanup(lSqlDao);
			cleanup(lConn);
		}
		return lProcedimenti;
	}

	@Override
	public Collection<ProcAggregatiCognomeModel> ExRicercaProcAggregati2Lettere(
			RicercaAggregatiCognomeModel aModel) throws F3BException {

		Connection lConn = null;
		Collection<ProcAggregatiCognomeModel> lProcedimenti = null;
		ProcAggregatiCognomeSqlDAO lSqlDao = null;

		try {
			lConn = getDBConnection(); // Preleva connessione dal dbase
			lSqlDao = new ProcAggregatiCognomeSqlDAO(lConn);

			lSqlDao.ricercaAggregati2Lettere(aModel);

			lProcedimenti = lSqlDao.getModels();
			lSqlDao.stop();

		} catch (Exception ex) {
			// MEV_55 Modifica del 11/12/2017 Inizio ********
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("ExRicercaProcAggregati2Lettere - Exception: " + ex);
			throw new SIUSException(SIUSException.USER_MESSAGE,
					ICostantiStatistiche.MESSAGGIO_ERRORE_GENERICO);
			// MEV_55 Modifica del 11/12/2017 Fine********
		} finally {
			cleanup(lSqlDao);
			cleanup(lConn);
		}
		return lProcedimenti;
	}

	@Override
	public Collection<ProcAggregatiCognomeElencoModel> ExRicercaProcAggregatiElenco(
			RicercaAggregatiCognomeModel aModel) throws F3BException {

		Connection lConn = null;
		Collection<ProcAggregatiCognomeElencoModel> lProcedimenti = null;
		ProcAggregatiCognomeElencoSqlDAO lSqlDAO = null;

		try {
			lConn = getDBConnection(); // Preleva connessione dal dbase
			lSqlDAO = new ProcAggregatiCognomeElencoSqlDAO(lConn);

			lSqlDAO.ricercaElenco(aModel);

			lProcedimenti = lSqlDAO.getModels();

			lSqlDAO.stop();
		} catch (Exception ex) {
			// MEV_55 Modifica del 11/12/2017 Inizio ********
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("ExRicercaProcAggregatiElenco - Exception: " + ex);
			throw new SIUSException(SIUSException.USER_MESSAGE,
					ICostantiStatistiche.MESSAGGIO_ERRORE_GENERICO);
			// MEV_55 Modifica del 11/12/2017 Fine********
		} finally {
			cleanup(lSqlDAO);
			cleanup(lConn);
		}

		return lProcedimenti;
	}

	@Override
	public Collection<EveFasGepSogDetModel> ExRicercaProcSoggettiIstitutiDetenzione(
			RicercaProcedimentoModel aModel) throws F3BException {

		Connection lConn = null;
		ProcAggregatiIstitutoDetenzioneSqlDAO lSqlDao = null;
		Collection<EveFasGepSogDetModel> lProc = null;

		try {
			lConn = getDBConnection();
			lSqlDao = new ProcAggregatiIstitutoDetenzioneSqlDAO(lConn);
			lSqlDao.ricercaProcedimentiSoggettiIstitutoDetenzione(aModel);
			lProc = lSqlDao.getModels();
		} catch (Exception ex) {
			// MEV_55 Modifica del 11/12/2017 Inizio ********
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("ExRicercaProcSoggettiIstitutiDetenzione - Exception: " + ex);
			throw new SIUSException(SIUSException.USER_MESSAGE,
					ICostantiStatistiche.MESSAGGIO_ERRORE_GENERICO);
			// MEV_55 Modifica del 11/12/2017 Fine********
		} finally {
			cleanup(lSqlDao);
			cleanup(lConn);
		}

		return lProc;
	}

	@Override
	public Collection<EveFasGepSogDetModel> ExRicercaProcAggrIstitutiDetenzione(
			RicercaProcedimentoModel aModel) throws F3BException {

		Connection lConn = null;
		ProcAggregatiIstitutoDetenzioneSqlDAO lSqlDao = null;
		// Collection<EveFasGepSogDetModel> lProc = new ArrayList();
		Collection<EveFasGepSogDetModel> lProc = null;

		try {
			lConn = getDBConnection();
			lSqlDao = new ProcAggregatiIstitutoDetenzioneSqlDAO(lConn);
			lSqlDao.ricercaAggregatiPerIstitutiDetenzione(aModel);
			lProc = lSqlDao.getModels();
		} catch (Exception ex) {
			// MEV_55 Modifica del 11/12/2017 Inizio ********
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("ExRicercaProcAggrIstitutiDetenzione - Exception: " + ex);
			throw new SIUSException(SIUSException.USER_MESSAGE,
					ICostantiStatistiche.MESSAGGIO_ERRORE_GENERICO);
			// MEV_55 Modifica del 11/12/2017 Fine********
		} finally {
			cleanup(lSqlDao);
			cleanup(lConn);
		}

		return lProc;
	}

	@Override
	public Collection<EveFasGepSogModel> ExRicercaProcProcuraMittente(RicercaProcedimentoModel aModel)
			throws F3BException {

		Connection lConn = null;
		ProcAggregatiProcuraMittenteSqlDAO lSqlDao = null;
		Collection<EveFasGepSogModel> lProc = null;

		try {
			lConn = getDBConnection();
			lSqlDao = new ProcAggregatiProcuraMittenteSqlDAO(lConn);
			lSqlDao.ricercaProcPerProcuraMittente(aModel);
			lProc = lSqlDao.getModels();
		} catch (Exception ex) {
			// MEV_55 Modifica del 11/12/2017 Inizio ********
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("ExRicercaProcProcuraMittente - Exception: " + ex);
			throw new SIUSException(SIUSException.USER_MESSAGE,
					ICostantiStatistiche.MESSAGGIO_ERRORE_GENERICO);
			// MEV_55 Modifica del 11/12/2017 Fine********
		} finally {
			cleanup(lSqlDao);
			cleanup(lConn);
		}

		return lProc;
	}

	@Override
	public Collection<EveFasGepSogModel> ExRicercaProcTotaliProcuraMittente(RicercaProcedimentoModel aModel)
			throws F3BException {

		Connection lConn = null;
		ProcAggregatiProcuraMittenteSqlDAO lSqlDao = null;
		Collection<EveFasGepSogModel> lProc = null;

		try {
			lConn = getDBConnection();
			lSqlDao = new ProcAggregatiProcuraMittenteSqlDAO(lConn);
			lSqlDao.ricercaProcTotaliPerProcuraMittente(aModel);
			lProc = lSqlDao.getModels();
		} catch (Exception ex) {
			// MEV_55 Modifica del 11/12/2017 Inizio ********
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("ExRicercaProcTotaliProcuraMittente - Exception: " + ex);
			throw new SIUSException(SIUSException.USER_MESSAGE,
					ICostantiStatistiche.MESSAGGIO_ERRORE_GENERICO);
			// MEV_55 Modifica del 11/12/2017 Fine********
		} finally {
			cleanup(lSqlDao);
			cleanup(lConn);
		}

		return lProc;
	}

	/**
	 * @param RicercaProcedimentoModel
	 * @return Collection
	 * @throws F3BException
	 */
	public Collection<EveFasGepSogModel> ExRicercaProcFissatiNoDefNumGG(RicercaProcedimentoModel aModel)
			throws F3BException {

		Collection<EveFasGepSogModel> lProcedimenti = null;
		lProcedimenti = this.ExRicercaProcFissatiNoDefNumGGPaginata(aModel, 0);

		return lProcedimenti;
	}

	/**
	 * Ritorna i records paginati; se aPagina == 0, ritorna tutti i records
	 *
	 * @param RicercaProcedimentoModel
	 * @return Collection
	 * @throws F3BException
	 */
	public Collection<EveFasGepSogModel> ExRicercaProcFissatiNoDefNumGGPaginata(
			RicercaProcedimentoModel aModel, int aPagina) throws F3BException {

		Connection lConn = null;
		Collection<EveFasGepSogModel> lProcedimenti = null;
		ProcDataUdienzaFissataNoDefinitiNumGGSqlDAO lSqlDao = null;

		try {
			lConn = getDBConnection(); // Preleva connessione dal dbase
			lSqlDao = new ProcDataUdienzaFissataNoDefinitiNumGGSqlDAO(lConn);
			// lEveSqlDao = new EventoSqlDAO(lConn);

			lSqlDao.ricercaProcDataUdienzaFissataNoDefinitiNumGG(aModel);

			if (aPagina > 0) {
				lSqlDao.startPage(aPagina);
			} else {
				lSqlDao.start();
			}

			lProcedimenti = new ArrayList<>();

			// Necessario iterare per via della paginazione.
			EveFasGepSogModel lModel;
			while (lSqlDao.next()) {
				lModel = (EveFasGepSogModel) lSqlDao.getModel();
				lProcedimenti.add(lModel);
			}
			lSqlDao.stop();
		} catch (Exception ex) {
			// MEV_55 Modifica del 11/12/2017 Inizio ********
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("ExRicercaProcFissatiNoDefNumGGPaginata - Exception: " + ex);
			throw new SIUSException(SIUSException.USER_MESSAGE,
					ICostantiStatistiche.MESSAGGIO_ERRORE_GENERICO);
			// MEV_55 Modifica del 11/12/2017 Fine********
		} finally {
			cleanup(lSqlDao);
			cleanup(lConn);
		}
		return lProcedimenti;
	}

	/**
	 * Ritorna il numero di records selezionati in base al filtro passato come model
	 *
	 * @param RicercaProcedimentoModel
	 * @return BigDecimal
	 * @throws F3BException
	 */
	public BigDecimal ExGetNumRicercaProcFissatiNoDefNumGG(RicercaProcedimentoModel aModel)
			throws F3BException {

		BigDecimal lRecords = new BigDecimal(0);
		Connection lConn = null;
		ProcDataUdienzaFissataNoDefinitiNumGGSqlDAO lSqlDao = null;

		try {
			lConn = getDBConnection(); // Preleva connessione dal dbase
			lSqlDao = new ProcDataUdienzaFissataNoDefinitiNumGGSqlDAO(lConn);
			lSqlDao.ricercaProcDataUdienzaFissataNoDefinitiNumGG(aModel);
			lRecords = lSqlDao.getNumRowsSelected();
		} catch (Exception ex) {
			// MEV_55 Modifica del 11/12/2017 Inizio ********
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("ExGetNumRicercaProcFissatiNoDefNumGG - Exception: " + ex);
			throw new SIUSException(SIUSException.USER_MESSAGE,
					ICostantiStatistiche.MESSAGGIO_ERRORE_GENERICO);
			// MEV_55 Modifica del 11/12/2017 Fine********
		} finally {
			cleanup(lSqlDao);
			cleanup(lConn);
		}
		return lRecords;
	}

	// FR019 - FA020
	@Override
	public Collection<EveFasGepSogProvModel> ExRicercaProcProvvEmessiNoDepNumGG(
			RicercaProcedimentoModel aModel) throws F3BException {

		Collection<EveFasGepSogProvModel> lProcedimenti = null;
		lProcedimenti = this.ExRicercaProcProvvEmessiNoDepNumGGPaginata(aModel, 0);
		return lProcedimenti;
	}

	@Override
	public Collection<EveFasGepSogProvModel> ExRicercaProcProvvEmessiNoDepNumGGPaginata(
			RicercaProcedimentoModel aModel, int aPagina) throws F3BException {

		Connection lConn = null;
		Collection<EveFasGepSogProvModel> lProcedimenti = null;
		ProcProvvEmessiNoDepositoNumGGSqlDAO lSqlDao = null;

		try {
			lConn = getDBConnection(); // Preleva connessione dal dbase
			lSqlDao = new ProcProvvEmessiNoDepositoNumGGSqlDAO(lConn);
			lSqlDao.ricercaProcProvvEmessiNoDepositoNumGG(aModel);

			if (aPagina > 0) {
				lSqlDao.startPage(aPagina);
			} else {
				lSqlDao.start();
			}

			lProcedimenti = new ArrayList<>();

			// Necessario iterare per via della paginazione.
			EveFasGepSogProvModel lModel;
			while (lSqlDao.next()) {
				lModel = (EveFasGepSogProvModel) lSqlDao.getModel();
				lProcedimenti.add(lModel);
			}
			lSqlDao.stop();
		} catch (Exception ex) {
			// MEV_55 Modifica del 11/12/2017 Inizio ********
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("ExRicercaProcProvvEmessiNoDepNumGGPaginata - Exception: " + ex);
			throw new SIUSException(SIUSException.USER_MESSAGE,
					ICostantiStatistiche.MESSAGGIO_ERRORE_GENERICO);
			// MEV_55 Modifica del 11/12/2017 Fine********
		} finally {
			cleanup(lSqlDao);
			cleanup(lConn);
		}
		return lProcedimenti;
	}

	@Override
	public BigDecimal ExGetNumRicercaProcProvvEmessiNoDepNumGG(RicercaProcedimentoModel aModel)
			throws F3BException {

		BigDecimal lRecords = new BigDecimal(0);
		Connection lConn = null;
		ProcProvvEmessiNoDepositoNumGGSqlDAO lSqlDao = null;

		try {
			lConn = getDBConnection(); // Preleva connessione dal dbase
			lSqlDao = new ProcProvvEmessiNoDepositoNumGGSqlDAO(lConn);
			lSqlDao.ricercaProcProvvEmessiNoDepositoNumGG(aModel);
			lRecords = lSqlDao.getNumRowsSelected();
		} catch (Exception ex) {
			// MEV_55 Modifica del 11/12/2017 Inizio ********
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("ExGetNumRicercaProcProvvEmessiNoDepNumGG - Exception: " + ex);
			throw new SIUSException(SIUSException.USER_MESSAGE,
					ICostantiStatistiche.MESSAGGIO_ERRORE_GENERICO);
			// MEV_55 Modifica del 11/12/2017 Fine********
		} finally {
			cleanup(lSqlDao);
			cleanup(lConn);
		}
		return lRecords;
	}

	/**
	 * Ritorna n.ro di record risultato di una RicercaFascicoloSiusByEstremi
	 *
	 * @param aFascicoloSius
	 * @return BigDecimal n.ro di record
	 * @throws F3BException
	 */
	public BigDecimal ExGetNumRicercaProcPosizioneGiuridica(RicercaProcedimentoModel aRicerca)
			throws F3BException {

		Connection lConn = null;
		ProcPosizioneGiuridicaSqlDAO lProcPosGiuSqlDao = null;
		BigDecimal lCont = new BigDecimal(0);

		try {
			lConn = getDBConnection();
			lProcPosGiuSqlDao = new ProcPosizioneGiuridicaSqlDAO(lConn);
			lProcPosGiuSqlDao.ricercaProcedimentiPosizioneGiuridica(aRicerca);
			lCont = lProcPosGiuSqlDao.getNumRowsSelected();
		} catch (DAOException daoEx) {
			// MEV_55 Modifica del 11/12/2017 Inizio ********
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("ExGetNumRicercaProcPosizioneGiuridica - Exception: " + daoEx);
			throw new SIUSException(SIUSException.USER_MESSAGE,
					ICostantiStatistiche.MESSAGGIO_ERRORE_GENERICO);
			// MEV_55 Modifica del 11/12/2017 Fine********
		} catch (Exception e) {
			// MEV_55 Modifica del 11/12/2017 Inizio ********
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("ExGetNumRicercaProcPosizioneGiuridica - Exception: " + e);
			throw new SIUSException(SIUSException.USER_MESSAGE,
					ICostantiStatistiche.MESSAGGIO_ERRORE_GENERICO);
			// MEV_55 Modifica del 11/12/2017 Fine********
		} finally {
			cleanup(lProcPosGiuSqlDao);
			cleanup(lConn);
		}
		return lCont;
	}

	/**
	 * 20140120 - creazione nuovo metodo di ricerca non paginata, per stampa folglio xls. Ricerca Procedimenti
	 * per Posizione Giuridica.
	 *
	 * @param aRicerca
	 * @return
	 * @throws F3BException
	 */
	public Collection<EveFasGepSogCancModel> ExRicercaProcPosizioneGiuridica(
			RicercaProcedimentoModel aRicerca) throws F3BException {

		Connection lConn = null;
		Collection<EveFasGepSogCancModel> lProcedimenti = new ArrayList<>();
		ProcPosizioneGiuridicaSqlDAO lProcPosGiuSqlDao = null;

		try {
			lConn = getDBConnection();
			lProcPosGiuSqlDao = new ProcPosizioneGiuridicaSqlDAO(lConn);
			lProcPosGiuSqlDao.ricercaProcedimentiPosizioneGiuridica(aRicerca);
			lProcedimenti = lProcPosGiuSqlDao.getModels();
			// if (lProcedimenti.isEmpty())
			// throw new SIUSException(F3BException.USER_MESSAGE, "Nessun Elemento trovato");
		} catch (DAOException daoEx) {
			// MEV_55 Modifica del 11/12/2017 Inizio ********
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("ExRicercaProcPosizioneGiuridica - Exception: " + daoEx);
			throw new SIUSException(SIUSException.USER_MESSAGE,
					ICostantiStatistiche.MESSAGGIO_ERRORE_GENERICO);
			// MEV_55 Modifica del 11/12/2017 Fine********
		} catch (Exception e) {
			// MEV_55 Modifica del 11/12/2017 Inizio ********
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("ExRicercaProcPosizioneGiuridica - Exception: " + e);
			throw new SIUSException(SIUSException.USER_MESSAGE,
					ICostantiStatistiche.MESSAGGIO_ERRORE_GENERICO);
			// MEV_55 Modifica del 11/12/2017 Fine********
		} finally {
			cleanup(lProcPosGiuSqlDao);
			cleanup(lConn);
		}
		return lProcedimenti;
	}

	/**
	 * Ricerca paginata procedimenti SIUS per posizione Giuridica.
	 *
	 * @param aRicerca
	 * @param aPageNum
	 *            : numero pagina > 0
	 * @return Collection di EveFasGepSogCancModel.
	 * @throws F3BException
	 */
	public Collection<EveFasGepSogCancModel> ExRicercaProcPosizioneGiuridicaPaginata(
			RicercaProcedimentoModel aRicerca, int aPage) throws F3BException {

		Connection lConn = null;
		Collection<EveFasGepSogCancModel> lProcedimenti = new ArrayList<>();
		ProcPosizioneGiuridicaSqlDAO lProcPosGiuSqlDao = null;

		try {
			lConn = getDBConnection();
			lProcPosGiuSqlDao = new ProcPosizioneGiuridicaSqlDAO(lConn);
			lProcPosGiuSqlDao.ricercaProcedimentiPosizioneGiuridica(aRicerca);

			if (aPage > 0)
				lProcPosGiuSqlDao.startPage(aPage);
			else
				lProcPosGiuSqlDao.start();

			lProcedimenti = new ArrayList<>();
			// Necessario iterare per via della paginazione.
			EveFasGepSogCancModel lModel;
			while (lProcPosGiuSqlDao.next()) {
				lModel = (EveFasGepSogCancModel) lProcPosGiuSqlDao.getModel();
				lProcedimenti.add(lModel);
			}
			lProcPosGiuSqlDao.stop();

			// if (lProcedimenti.isEmpty())
			// throw new SIUSException(F3BException.USER_MESSAGE, "Nessun Elemento trovato");

		} catch (DAOException daoEx) {
			// MEV_55 Modifica del 11/12/2017 Inizio ********
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("ExRicercaProcPosizioneGiuridicaPaginata - Exception: " + daoEx);
			throw new SIUSException(SIUSException.USER_MESSAGE,
					ICostantiStatistiche.MESSAGGIO_ERRORE_GENERICO);
			// MEV_55 Modifica del 11/12/2017 Fine********
		} catch (Exception e) {
			// MEV_55 Modifica del 11/12/2017 Inizio ********
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("ExRicercaProcPosizioneGiuridicaPaginata - Exception: " + e);
			throw new SIUSException(SIUSException.USER_MESSAGE,
					ICostantiStatistiche.MESSAGGIO_ERRORE_GENERICO);
			// MEV_55 Modifica del 11/12/2017 Fine********
		} finally {
			cleanup(lProcPosGiuSqlDao);
			cleanup(lConn);
		}

		return lProcedimenti;
	}

	//
	// FI021_FR022_FA023_FA024 - Ricerca statistica comparata Magistrati
	//
	/**
	 * 20140129 - creazione nuovo metodo di ricerca non paginata, per stampa folglio xls. Ricerca Procedimenti
	 * comparata per Magistrati.
	 *
	 * @param aRicerca
	 * @return
	 * @throws F3BException
	 */
	public Collection<IspConteggioRelatoriMagistratiModel> ExRicercaStatisticaComparataMagistrati(
			RicercaProcedimentoModel aRicerca) throws F3BException {

		Connection lConn = null;
		Collection<IspConteggioRelatoriMagistratiModel> lConteggi = new ArrayList<>();
		StatisSiusStoreProcedureDAO lSpDao = null;
		IspMotivoOggettoSelezionatiDAO lOggettiSelezionatiDao = null;
		IspRelatoreSelezionatiDAO lRelatoreSelezionatiDao = null;
		IspConteggioRelatoriMagistratiSqlDAO lSqlMagDao = null;

		try {
			lConn = getDBConnection();
			lOggettiSelezionatiDao = new IspMotivoOggettoSelezionatiDAO(lConn);
			// Esegue la cancellazione degli oggetti precedentemente selezionati
			// relativi all'ufficio dell'utente connesso.
			// 20140314 - Paolo;Henry : patch per cancellazione totale dei records
			// lOggettiSelezionatiDao.setCondizioneByUfficio(aRicerca.getCodUfficio());
			lOggettiSelezionatiDao.setCondizione("1=1");
			lOggettiSelezionatiDao.delete();
			lOggettiSelezionatiDao.stop();

			lRelatoreSelezionatiDao = new IspRelatoreSelezionatiDAO(lConn);
			// Esegue la cancellazione degli oggetti precedentemente selezionati
			// relativi all'ufficio dell'utente connesso.
			// 20140314 - Paolo;Henry : patch per cancellazione totale dei records
			// lRelatoreSelezionatiDao.setCondizioneByUfficio(aRicerca.getCodUfficio());
			lRelatoreSelezionatiDao.setCondizione("1=1");
			lRelatoreSelezionatiDao.delete();
			lRelatoreSelezionatiDao.stop();

			// Esegue il caricamento dei nuovi oggetti selezionati per ufficio
			// dell'utente connesso.
			Collection<DecodificheModel> lMotivi = null;
			// MEV10-s3: aggiunte or condition per gestire uffici minorenni
			if (aRicerca.getCodTipoUfficio().equals("TDS") || aRicerca.getCodTipoUfficio().equals("TDSM")) {
				lMotivi = // DecodificheManager.getInstance().getMotivoProvvedimento();
						DecodificheUtils.getFilteredByCodAlt(
								DecodificheManager.getInstance().getMotivoProvvedimento(), "^[C]\\d{3}");
			} else if (aRicerca.getCodTipoUfficio().equals("UDS")
					|| aRicerca.getCodTipoUfficio().equals("UDSM")) {
				lMotivi = // DecodificheManager.getInstance().getMotivoProvvedimento();
						DecodificheUtils.getFilteredByCodAlt(
								DecodificheManager.getInstance().getMotivoProvvedimento(), "^[U]\\d{3}");
			}

			for (String lCodMotivo : aRicerca.getCodMotivi()) {
				lOggettiSelezionatiDao.setCodMotivo(lCodMotivo);
				lOggettiSelezionatiDao.setDescMotivo(DecodificheUtils.getDescbyCode(lMotivi, lCodMotivo));
				lOggettiSelezionatiDao.setCodOggetto(DecodificheUtils.getCodAltebyCode(lMotivi, lCodMotivo));
				// lOggettiSelezionatiDao.setDescOggetto(DecodificheUtils.getDescbyCode(lMotivi, lCodMotivo));
				lOggettiSelezionatiDao.setTipoUfficio(aRicerca.getCodTipoUfficio());
				lOggettiSelezionatiDao.setCodUfficio(aRicerca.getCodUfficio());
				lOggettiSelezionatiDao.insert();
				lOggettiSelezionatiDao.stop();
			}

			//
			// Carica i relatori selezionti
			//
			for (String lCodMagistrato : aRicerca.getCodMagistrati()) {
				lRelatoreSelezionatiDao.setCodMagistrato(lCodMagistrato);
				lRelatoreSelezionatiDao.setTipoUfficio(aRicerca.getCodTipoUfficio());
				lRelatoreSelezionatiDao.setCodUfficio(aRicerca.getCodUfficio());
				lRelatoreSelezionatiDao.insert();
				lRelatoreSelezionatiDao.stop();
			}

			lConn.commit();

			lSpDao = new StatisSiusStoreProcedureDAO(lConn);

			// 1 - StatoOggettiSius
			lSpDao.setStatoOggettiSiusCompMag();
			lSpDao.setCodUfficio(aRicerca.getCodUfficio());
			lSpDao.setDataInizio(aRicerca.getDataIscrizioneInizio());
			lSpDao.setDataFine(aRicerca.getDataIscrizioneFine());
			lSpDao.setCodCancAssegnataria("0");
			lSpDao.setFiltroCollab("tutti");
			lSpDao.setPosizioneGiuridica("tutti");

			lSpDao.execute();
			lSpDao.stop();

			// 2 - creaStatRelatoriMotOggSel
			lSpDao = new StatisSiusStoreProcedureDAO(lConn);
			lSpDao.setCreaStatRelatoriMotOggSel();
			lSpDao.setCodUfficio(aRicerca.getCodUfficio());
			lSpDao.setDataInizio(aRicerca.getDataIscrizioneInizio());
			lSpDao.setDataFine(aRicerca.getDataIscrizioneFine());

			lSpDao.execute();
			lSpDao.stop();

			lConn.commit();

			// Lettura dei dati dalla tabella di ISP_CONTEGGIO_RELATORI
			lSqlMagDao = new IspConteggioRelatoriMagistratiSqlDAO(lConn);
			lSqlMagDao.ricercaConteggioRelatoriMagistrati(aRicerca);
			lConteggi = lSqlMagDao.getModels();
		} catch (Exception e) {
			// MEV_55 Modifica del 11/12/2017 Inizio ********
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("ExRicercaStatisticaComparataMagistrati - Exception: " + e);
			throw new SIUSException(SIUSException.USER_MESSAGE,
					ICostantiStatistiche.MESSAGGIO_ERRORE_GENERICO);
			// MEV_55 Modifica del 11/12/2017 Fine********
		} finally {
			cleanup(lOggettiSelezionatiDao);
			cleanup(lRelatoreSelezionatiDao);
			// Scheda Intervento n° 6 - Ottimizzazione SIUS Avvocati
			cleanup(lSpDao);
			cleanup(lSqlMagDao);
			cleanup(lConn);
		}

		return lConteggi;
	}

	public ArrayList<IspMotivoOggettoSelezionatiModel> ExListaMotiviOggettiSelezionati() throws F3BException {

		ArrayList<IspMotivoOggettoSelezionatiModel> lElenco = new ArrayList<>();
		IspMotivoOggettoSelezionatiDAO lDao = null;
		Connection lConn = null;

		try {
			lConn = getDBConnection();
			lDao = new IspMotivoOggettoSelezionatiDAO(lConn);
			lElenco = (ArrayList<IspMotivoOggettoSelezionatiModel>) lDao.getModels();
		} catch (Exception e) {
			// MEV_55 Modifica del 11/12/2017 Inizio ********
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("ExListaMotiviOggettiSelezionati - Exception: " + e);
			throw new SIUSException(SIUSException.USER_MESSAGE,
					ICostantiStatistiche.MESSAGGIO_ERRORE_GENERICO);
			// MEV_55 Modifica del 11/12/2017 Fine********
		} finally {
			cleanup(lDao);
			cleanup(lConn);
		}

		return lElenco;
	}

	/**
	 * Recupera i motivo oggetti per intersezione con la tabella oggetti selezionati.
	 *
	 * @param aCodUfficio
	 * @return
	 * @throws F3BException
	 */
	public ArrayList<IspMotivoOggettoSelezionatiModel> ExListaMotivoOggettiSelezionati(String aCodUfficio)
			throws F3BException {

		ArrayList<IspMotivoOggettoSelezionatiModel> lElenco = new ArrayList<>();
		IspMotivoOggettoSelezionatiDAO lDao = null;
		Connection lConn = null;

		try {
			lConn = getDBConnection();
			lDao = new IspMotivoOggettoSelezionatiDAO(lConn);
			lDao.setCondizioneByUfficio(aCodUfficio);
			lElenco = (ArrayList<IspMotivoOggettoSelezionatiModel>) lDao.getModels();
		} catch (Exception e) {
			// MEV_55 Modifica del 11/12/2017 Inizio ********
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("ExListaMotiviOggettiSelezionati - Exception: " + e);
			throw new SIUSException(SIUSException.USER_MESSAGE,
					ICostantiStatistiche.MESSAGGIO_ERRORE_GENERICO);
			// MEV_55 Modifica del 11/12/2017 Fine********
		} finally {
			cleanup(lDao);
			cleanup(lConn);
		}

		return lElenco;
	}

	public Collection<IspConteggioOggettiModel> ExConteggioOggettiMagistrato(
			RicercaProcedimentoModel aRicerca) throws F3BException {

		Connection lConn = null;
		Collection<IspConteggioOggettiModel> lOggetti = null;
		StatisSiusStoreProcedureDAO lSpDao = null;
		IspConteggioOggettiSqlDAO lOggettiSqlDao = null;

		try {
			lConn = getDBConnection();

			lSpDao = new StatisSiusStoreProcedureDAO(lConn);
			lSpDao.setSvuotaConteggioOggetti();
			lSpDao.setCodUfficio(aRicerca.getCodUfficio());
			lSpDao.execute();

			lSpDao = new StatisSiusStoreProcedureDAO(lConn);
			lSpDao.setCreaStatisticaOggetti();
			lSpDao.setCodUfficio(aRicerca.getCodUfficio());
			lSpDao.setCodMagistrato(aRicerca.getCodMagistrato());
			lSpDao.setDataInizio(aRicerca.getDataIscrizioneInizio());
			lSpDao.setDataFine(aRicerca.getDataIscrizioneFine());

			lSpDao.execute();

			// Lettura dati
			lOggettiSqlDao = new IspConteggioOggettiSqlDAO(lConn);
			lOggettiSqlDao.ricercaConteggioOggetti(aRicerca.getCodUfficio());
			lOggetti = lOggettiSqlDao.getModels();

			commit(lConn);

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("ExCreaStatisticaOggettiStoredProcedure : fine ");
		} catch (Exception eEx) {
			rollback(lConn);
			// MEV_55 Modifica del 11/12/2017 Inizio ********
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("ExConteggioOggettiMagistrato - Exception: " + eEx);
			throw new SIUSException(SIUSException.USER_MESSAGE,
					ICostantiStatistiche.MESSAGGIO_ERRORE_GENERICO);
			// MEV_55 Modifica del 11/12/2017 Fine********
		} finally {
			cleanup(lSpDao);
			cleanup(lOggettiSqlDao);
			cleanup(lConn);
		}

		return lOggetti;
	}

	/**
	 * La funzione restituisce tutti i record dalla tabella ISP_ESTRAZIONE_OGGETTI_TRIB filtrati per un
	 * insieme di oggetti selezionati 'IN COD_OGGETTO_TENORE' ed eventualmente anche per COD_MAGISTRATO.
	 * ordinati per COD_OGGETTO_TENORE
	 *
	 * @param IspEstrazioneOggettiModel
	 *            : filtro di ricerca
	 * @return Vector
	 * @throws F3BException
	 */
	public Collection<IspEstrazioneOggettiModel> ExRicercaProcedimentiEstrattiOggettiSelezionatiOrdinati(
			IspEstrazioneOggettiModel aModel) throws F3BException {

		Connection lConn = null;
		Collection<IspEstrazioneOggettiModel> lOggetti = new Vector<>();
		IspEstrazioneOggettiTribDAO lDao = null;

		try {
			lConn = getDBConnection(); // Preleva connessione dal dbase
			lDao = new IspEstrazioneOggettiTribDAO(lConn);
			lDao.setCondizioneOrdinataPerOggetti(aModel);
			lOggetti = lDao.getModels(); // Recupera le occorrenze
		} catch (Exception daoEx) {
			// MEV_55 Modifica del 11/12/2017 Inizio ********
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("ExRicercaProcedimentiEstrattiOggettiSelezionatiOrdinati - Exception: " + daoEx);
			throw new SIUSException(SIUSException.USER_MESSAGE,
					ICostantiStatistiche.MESSAGGIO_ERRORE_GENERICO);
			// MEV_55 Modifica del 11/12/2017 Fine********
		} finally {
			cleanup(lDao);
			cleanup(lConn);
		}
		return lOggetti;
	}

	/**
	 * MEV10-s3: aggiunto metodo
	 *
	 * @param aModel
	 * @param aPageNum
	 * @return Vector
	 * @throws F3BException
	 */
	public Vector ExRicercaProcSiusXProvvedimentiPaginata(RicercaProvvedimentoModel aModel, int aPageNum)
			throws F3BException {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("" + getClass().getName() + " .ExRicercaProcSiusXProvvedimentiPaginata(): inizio ");
		// connessione
		Connection lConn = null; // connessione

		// SqlDAO utilizzato viene definito come generico
		EveFasGepSogSqlDAO lDao = null;

		// Aggregato di dati risultato della ricerca
		EveFasGepSogProvModel lProvvedimento = null;

		// Elenco risultati
		Vector lElencoProc = new Vector();

		try {
			lConn = getDBConnection();
			// Scelta del DAO specializzato al tipo di ricerca
			if (aModel.isRicercaXProvvedimento())
				lDao = new EveFasGepSogOrdSqlDAO(lConn);
			else if (aModel.isRicercaXDecreto())
				lDao = new EveFasGepSogDecSqlDAO(lConn);
			else if (aModel.isRicercaXImpugnazioneRicorso())
				lDao = new EveFasGepSogImpSqlDAO(lConn);
			else if (aModel.isRicercaXFoglioComplementare())
				lDao = new EveFasGepSogFogSqlDAO(lConn);
			else if (aModel.isRicercaXSentenza())
				lDao = new EveFasGepSogSenSqlDAO(lConn);
			else
				throw new F3BException(F3BException.USER_MESSAGE, "Modalità di ricerca non definita!");

			lDao.ricercaProcSiusXProvvedimenti(aModel);

			// Ricerca paginata
			// Viene fatta la ricerca non paginata se aPageNum ha un valore <= 0
			if (aPageNum > 0)
				lDao.startPage(aPageNum);
			else
				lDao.start();

			while (lDao.next()) {
				lProvvedimento = (EveFasGepSogProvModel) lDao.getModel();
				lElencoProc.add(lProvvedimento);
			}
			lDao.stop();
			if (lElencoProc.isEmpty())
				throw new F3BException(F3BException.USER_MESSAGE, "Nessun Elemento trovato");
		} catch (F3BException fe) {
			throw fe;
		} catch (Exception e) {
			// MEV_55 Modifica del 11/12/2017 Inizio ********
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("ExRicercaProcSiusXProvvedimentiPaginata - Exception: " + e);
			throw new SIUSException(SIUSException.USER_MESSAGE,
					ICostantiStatistiche.MESSAGGIO_ERRORE_GENERICO);
			// MEV_55 Modifica del 11/12/2017 Fine********
		} finally {
			// Scheda Intervento n° 6 - Ottimizzazione SIUS Avvocati
			cleanup(lDao);
			cleanup(lConn);
		}

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("" + getClass().getName() + " .ExRicercaProcSiusXProvvedimentiPaginata(): fine ");

		return lElencoProc;
	}

	/**
	 * MEV10-s3: aggiunto metodo
	 *
	 * @param aModel
	 * @return BigDecimal
	 * @throws F3BException
	 */
	public BigDecimal ExGetNumRicercaProcSiusXProvvedimenti(RicercaProvvedimentoModel aModel)
			throws F3BException {

		Connection lConn = null; // connessione

		// SqlDAO utilizzato viene definito come generico
		EveFasGepSogSqlDAO lDao = null;
		// Risultato
		BigDecimal lCont = new BigDecimal(0);

		try {
			lConn = getDBConnection();

			// Scelta del DAO specializzato al tipo di ricerca
			if (aModel.isRicercaXProvvedimento())
				lDao = new EveFasGepSogOrdSqlDAO(lConn);
			else if (aModel.isRicercaXDecreto())
				lDao = new EveFasGepSogDecSqlDAO(lConn);
			else if (aModel.isRicercaXImpugnazioneRicorso())
				lDao = new EveFasGepSogImpSqlDAO(lConn);
			else if (aModel.isRicercaXFoglioComplementare())
				lDao = new EveFasGepSogFogSqlDAO(lConn);
			else if (aModel.isRicercaXSentenza())
				lDao = new EveFasGepSogSenSqlDAO(lConn);
			else
				throw new F3BException(F3BException.USER_MESSAGE, "Modalità di ricerca non definita!");

			lDao.ricercaProcSiusXProvvedimenti(aModel);
			lCont = lDao.getNumRowsSelected();
			aModel.setNumTotali(lCont);

			// Calcolo parziale effettuato solo se la ricerca non filtra lo stato
			if (aModel.isRicercaStatoTutti()) {
				aModel.setNumAnnullati(ExGetNumAnnullati(aModel, lDao));
				if (aModel.isRicercaXDecreto() || aModel.isRicercaXProvvedimento())
					aModel.setNumNonValidati(ExGetNumNonValidati(aModel, lDao));
				aModel.setCalcolatiTotali(true);
			}
		} catch (F3BException fe) {
			throw fe;
		} catch (Exception e) {
			// MEV_55 Modifica del 11/12/2017 Inizio ********
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("ExGetNumRicercaProcSiusXProvvedimenti - Exception: " + e);
			throw new SIUSException(SIUSException.USER_MESSAGE,
					ICostantiStatistiche.MESSAGGIO_ERRORE_GENERICO);
			// MEV_55 Modifica del 11/12/2017 Fine********
		} finally {
			// Scheda Intervento n° 6 - Ottimizzazione SIUS Avvocati
			cleanup(lDao);
			cleanup(lConn);
		}
		return lCont;
	}

	/**
	 * MEV10-s3: aggiunto metodo
	 *
	 * @param aModel
	 * @param aDao
	 * @return BigDecimal
	 * @throws Exception
	 */
	private BigDecimal ExGetNumAnnullati(RicercaProvvedimentoModel aModel, EveFasGepSogSqlDAO aDao)
			throws Exception {

		BigDecimal lCont = new BigDecimal(0);

		// Si copia RicercaModel per effettuare la stessa ricerca già definita
		// ma aggiungendo solo lo stato Annullato.
		RicercaProvvedimentoModel lRicercaModel = new RicercaProvvedimentoModel(aModel);
		lRicercaModel.setStatoAnnullati();

		aDao.ricercaProcSiusXProvvedimenti(lRicercaModel);
		lCont = aDao.getNumRowsSelected();

		return lCont;
	}

	/**
	 * MEV10-s3: aggiunto metodo
	 *
	 * @param aModel
	 * @param aDao
	 * @return BigDecimal
	 * @throws Exception
	 */
	private BigDecimal ExGetNumNonValidati(RicercaProvvedimentoModel aModel, EveFasGepSogSqlDAO aDao)
			throws Exception {

		BigDecimal lCont = new BigDecimal(0);

		// Si copia RicercaModel per effettuare la stessa ricerca già definita
		// ma aggiungendo solo lo stato Non Validato.
		RicercaProvvedimentoModel lRicercaModel = new RicercaProvvedimentoModel(aModel);
		lRicercaModel.setStatoNonValidati();

		aDao.ricercaProcSiusXProvvedimenti(lRicercaModel);
		lCont = aDao.getNumRowsSelected();

		return lCont;
	}

	/**
	 * MEV73: aggiunto metodo
	 *
	 * @param ufficioModel
	 * @param ricercaProcedimentoModel
	 *
	 */
	public Collection<EveFasGepSogModel> ExRicercaProcDlgs123_2018(UfficioModel ufficioModel,
			RicercaProcedimentoModel ricercaProcedimentoModel) throws F3BException {

		Connection lConn = null;
		ProcedimentiDlgs123_2018SqlDAO lSqlDao = null;
		Collection<EveFasGepSogModel> lProc = null;

		try {
			lConn = getDBConnection();
			lSqlDao = new ProcedimentiDlgs123_2018SqlDAO(lConn);
			lSqlDao.ricercaProcedimentiDlgs123_2018(ufficioModel, ricercaProcedimentoModel);
			lProc = lSqlDao.getModels();
		} catch (Exception ex) {
			siesLogger.error("Exception : " + ex);
			throw new F3BException("ExRicercaProcDlgs123_2018 : " + ex);
		} finally {
			cleanup(lSqlDao);
			cleanup(lConn);
		}

		return lProc;
	}

	/**
	 * Ricerca dei Magistrati associati a Procedimenti SIUS estratti dalle funzioni statistiche e memorizzati
	 * in tabella isp_proc_intervalli.
	 *
	 * @return
	 * @throws F3BException
	 */
	public Vector ExRicercaMagistratiProcIntervalli(String aCodUfficio) throws F3BException {

		Connection lConn = null;
		Vector lMagistrati = new Vector();
		MagistratoFirmatarioSqlDAO lMagSqlDao = null;

		try {
			lConn = getDBConnection();
			lMagSqlDao = new MagistratoFirmatarioSqlDAO(lConn);
			lMagSqlDao.ricercaMagistratiProcIntervalli(aCodUfficio);
			lMagSqlDao.start();

			while (lMagSqlDao.next()) {
				lMagistrati.add(lMagSqlDao.getModel());
			}
			if (lMagistrati.size() == 0) {
				siesLogger.info(
						"MagistratoFirmatarioController.ExRicercaMagistratiOggettiEstratti: Nessun magistrato !");
			}
		} catch (Exception daoEx) {
			siesLogger.error("ExRicercaMagistratiOggettiEstratti - Exception: " + daoEx);
			throw new SIUSException(SIUSException.USER_MESSAGE,
					ICostantiStatistiche.MESSAGGIO_ERRORE_GENERICO);
		} finally {
			cleanup(lMagSqlDao);
			cleanup(lConn);
		}
		return lMagistrati;
	}

	// MEV_2019-09: aggiunti metodi per le statistiche di Misure Alternative
	@Override
	public Collection<EveFasGepSogProvModel> ProcPerStatisticaMisureAlternative(RicercaProcedimentoModel rpm)
			throws F3BException {

		return ExRicercaProcPerStatisticaMisureAlternative(rpm, 0);
	}

	public Collection<EveFasGepSogProvModel> ExRicercaProcPerStatisticaMisureAlternative(
			RicercaProcedimentoModel rpm, int pagina) throws F3BException {

		Connection c = null;
		Collection<EveFasGepSogProvModel> procedimenti = null;
		ProcPerStatisticaMisureAlternativeSqlDAO ppsmasdao = null;

		try {
			c = getDBConnection(); // Preleva connessione dal DB
			ppsmasdao = new ProcPerStatisticaMisureAlternativeSqlDAO(c);

			switch (rpm.getStatoProcedimento()) {
			case 0:
				ppsmasdao.ricercaOrdinanzeNonEmesseAttiAlPresidente(rpm);
				break;
			case 1:
				ppsmasdao.ricercaOrdinanzeNonEmesse(rpm);
				break;
			case 2:
				ppsmasdao.ricercaOrdinanzeApplicazioneProvvisoriaEmesseNoDataEsecutivita(rpm);
				break;
			case 3:
				ppsmasdao.ricercaOrdinanzeApplicazioneProvvisoriaEmesseNoDecisioneCollegio(rpm);
				break;
			case 4:
				ppsmasdao.ricercaProcedimentiPriviProvvedimenti(rpm);
				break;
			default:
				throw new F3BException(
						"ExRicercaProcPerStatisticaMisureAlternative : Valore dell StatoProcedimento = "
								+ rpm.getStatoProcedimento()
								+ " non valido. Deve essere compreso nel range 0-4.");
			}

			if (pagina > 0) {
				ppsmasdao.startPage(pagina);
			} else {
				ppsmasdao.start();
			}

			procedimenti = new ArrayList<>();
			while (ppsmasdao.next()) {
				EveFasGepSogProvModel lModel = (EveFasGepSogProvModel) ppsmasdao.getModel();
				procedimenti.add(lModel);
			}
			ppsmasdao.stop();
		} catch (Exception ex) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("ExRicercaProcPerStatisticaMisureAlternative - Exception: " + ex);
			throw new SIUSException(SIUSException.USER_MESSAGE,
					ICostantiStatistiche.MESSAGGIO_ERRORE_GENERICO);
		} finally {
			cleanup(ppsmasdao);
			cleanup(c);
		}
		return procedimenti;
	}

	@Override
	public BigDecimal ExGetNumRicercaProcPerStatisticaMisureAlternative(RicercaProcedimentoModel rpm)
			throws F3BException {

		BigDecimal records = new BigDecimal(0);
		Connection c = null;
		ProcPerStatisticaMisureAlternativeSqlDAO ppsmasdao = null;

		try {
			c = getDBConnection(); // Preleva connessione dal dbase
			ppsmasdao = new ProcPerStatisticaMisureAlternativeSqlDAO(c);

			switch (rpm.getStatoProcedimento()) {
			case 0:
				ppsmasdao.ricercaOrdinanzeNonEmesseAttiAlPresidente(rpm);
				break;
			case 1:
				ppsmasdao.ricercaOrdinanzeNonEmesse(rpm);
				break;
			case 2:
				ppsmasdao.ricercaOrdinanzeApplicazioneProvvisoriaEmesseNoDataEsecutivita(rpm);
				break;
			case 3:
				ppsmasdao.ricercaOrdinanzeApplicazioneProvvisoriaEmesseNoDecisioneCollegio(rpm);
				break;
			case 4:
				ppsmasdao.ricercaProcedimentiPriviProvvedimenti(rpm);
				break;
			default:
				throw new F3BException(
						"ExGetNumRicercaProcPerStatisticaMisureAlternative : Valore dell StatoProcedimento = "
								+ rpm.getStatoProcedimento()
								+ " non valido. Deve essere compreso nel range 0-4.");
			}

			records = ppsmasdao.getNumRowsSelected();
		} catch (Exception ex) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("ExGetNumRicercaProcPerStatisticaMisureAlternative - Exception: " + ex);
			throw new SIUSException(SIUSException.USER_MESSAGE,
					ICostantiStatistiche.MESSAGGIO_ERRORE_GENERICO);
		} finally {
			cleanup(ppsmasdao);
			cleanup(c);
		}
		return records;
	}
	// FINE MEV_2019-09

	
	// MEV9
	public Collection<EveFasGepSogProvModel> ExRicercaAttiIstruttoriDataRestPaginata (
			RicercaProcedimentoModel aModel, int aPagina) throws F3BException {

		Connection lConn = null;
		Collection<EveFasGepSogProvModel> lProcedimenti = null;
		ProcAttiIstruttoriDataRestSqlDAO lSqlDao = null;

		try {
			lConn = getDBConnection();
			lSqlDao = new ProcAttiIstruttoriDataRestSqlDAO(lConn);

			lSqlDao.ricercaProcedimentiAttiIstruttoriDataRest(aModel);

			if (aPagina > 0) {
				lSqlDao.startPage(aPagina);
			} else {
				lSqlDao.start();
			}

			lProcedimenti = new ArrayList<>();
			while (lSqlDao.next()) {
				EveFasGepSogProvModel lModel = (EveFasGepSogProvModel) lSqlDao.getModel();
				lProcedimenti.add(lModel);
			}
			lSqlDao.stop();
		} catch (Exception ex) {
			siesLogger.error("ExRicercaAttiIstruttoriDataRestPaginata - Exception: ",ex);
			throw new SIUSException(SIUSException.USER_MESSAGE,
					ICostantiStatistiche.MESSAGGIO_ERRORE_GENERICO);
		} finally {
			cleanup(lSqlDao);
			cleanup(lConn);
		}
		return lProcedimenti;
	}
	
	
	public BigDecimal ExGetNumRicercaAttiIstruttoriDataRestPaginata (RicercaProcedimentoModel aModel)
			throws F3BException {

		BigDecimal lRecords = new BigDecimal(0);
		Connection lConn = null;
		ProcAttiIstruttoriDataRestSqlDAO lSqlDao = null;

		try {
			lConn = getDBConnection(); 
			lSqlDao = new ProcAttiIstruttoriDataRestSqlDAO(lConn);

			lSqlDao.ricercaProcedimentiAttiIstruttoriDataRest(aModel);

			lRecords = lSqlDao.getNumRowsSelected();
		} catch (Exception ex) {
			siesLogger.error("ExGetNumRicercaAttiIstruttoriDataRestPaginata - Exception: ", ex);
			throw new SIUSException(SIUSException.USER_MESSAGE,
					ICostantiStatistiche.MESSAGGIO_ERRORE_GENERICO);
		} finally {
			cleanup(lSqlDao);
			cleanup(lConn);
		}
		return lRecords;
	}
	// MEV9 - FINE
	
	
	
}