package siap.siep.istruttoriacumulo.controller;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.dao.DAOException;
import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.util.report.ReportGenerator;
import f3b.util.xml.TreeModel;
import siap.controller.SiapController;
import siap.jms.ICostantiJMS;
import siap.jms.messaggio.dao.MessaggioDAO;
import siap.jms.messaggio.dao.MessaggioSqlDAO;
import siap.jms.messaggio.model.MessaggioModel;
import siap.sico.camponota.dao.CampoNotaDAO;
import siap.sico.evento.dao.EventoDAO;
import siap.sico.evento.dao.EventoSqlDAO;
import siap.sico.evento.dao.EventoStoreProcedurePulisciDAO;
import siap.sico.evento.model.EventoModel;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.soggetto.model.SoggettoModel;
import siap.sico.template.controller.TemplateManager;
import siap.sico.ufficio.dao.UfficioSqlDAO;
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.utente.model.DatiOperazioneModel;
import siap.sico.utente.model.UtenteModel;
import siap.siep.SIEPException;
import siap.siep.annotazioneesitotrasmissione.dao.AnnotazioneEsitoTrasmissioneDAO;
import siap.siep.annotazioneesitotrasmissione.dao.AnnotazioneEsitoTrasmissioneSqlDAO;
import siap.siep.annotazioneesitotrasmissione.model.AnnotazioneEsitoTrasmissioneModel;
import siap.siep.cumulo.dao.CumuloDAO;
import siap.siep.fascicolo.dao.FascicoloSiepSoggettoSqlDAO;
import siap.siep.fascicolo.dao.FascicoloSiepSqlDAO;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.istruttoriacumulo.action.ICostantiIstruttoriaCumulo;
import siap.siep.istruttoriacumulo.dao.EsitoArchiviazioniCumuloSqlDAO;
import siap.siep.istruttoriacumulo.dao.IstruttoriaCumuloDAO;
import siap.siep.istruttoriacumulo.dao.IstruttoriaCumuloSqlDAO;
import siap.siep.istruttoriacumulo.model.EsitoArchiviazioniCumuloModel;
import siap.siep.istruttoriacumulo.model.IstruttoriaCumuloModel;
import siap.siep.modulocumulo.controller.IModuloCumulo;
import siap.siep.modulocumulo.dao.BeneficioCumuloSqlDAO;
import siap.siep.modulocumulo.dao.CircostanzaCumuloSqlDAO;
import siap.siep.modulocumulo.dao.ComputiCumuloSqlDAO;
import siap.siep.modulocumulo.dao.ContinuazioneCumuloSqlDAO;
import siap.siep.modulocumulo.dao.DatiFinaliCumuloDAO;
import siap.siep.modulocumulo.dao.DatiFinaliCumuloSqlDAO;
import siap.siep.modulocumulo.dao.DatiFinaliUlterioriSanzioniSqlDAO;
import siap.siep.modulocumulo.dao.LibAnticipataCumuloSqlDAO;
import siap.siep.modulocumulo.dao.MisuraCautelareCumuloSqlDAO;
import siap.siep.modulocumulo.dao.MisuraSicurezzaCumuloSqlDAO;
import siap.siep.modulocumulo.dao.NotificaCumuloSqlDAO;
import siap.siep.modulocumulo.dao.PenaAccessoriaCumuloSqlDAO;
import siap.siep.modulocumulo.dao.PenaComplessivaCumuloSqlDAO;
import siap.siep.modulocumulo.dao.PenaRideterminataCumuloSqlDAO;
import siap.siep.modulocumulo.dao.PeriodoLibAntCumuloSqlDAO;
import siap.siep.modulocumulo.dao.PosizioneGiuridicaCumuloSqlDAO;
import siap.siep.modulocumulo.dao.ProcedimentoCumulatoSqlDAO;
import siap.siep.modulocumulo.dao.ProvvedimentoGeSorvCumSqlDAO;
import siap.siep.modulocumulo.dao.ReatoCumuloSqlDAO;
import siap.siep.modulocumulo.dao.RichPMBeneficioCumSqlDAO;
import siap.siep.modulocumulo.dao.RichPMMisSicCumSqlDAO;
import siap.siep.modulocumulo.dao.RichPMPenAccCumSqlDAO;
import siap.siep.modulocumulo.dao.RichPMReatoCumSqlDAO;
import siap.siep.modulocumulo.dao.RichPMSanzioneSostCumSqlDAO;
import siap.siep.modulocumulo.dao.RichPMStatoEsecCumSqlDAO;
import siap.siep.modulocumulo.dao.RichPMTitoloCumSqlDAO;
import siap.siep.modulocumulo.dao.RichiesteInviateCumDAO;
import siap.siep.modulocumulo.dao.RichiesteInviateCumSqlDAO;
import siap.siep.modulocumulo.dao.RichiestePmInCumuloSqlDAO;
import siap.siep.modulocumulo.dao.SanzioneSostitutivaCumuloSqlDAO;
import siap.siep.modulocumulo.dao.SoggettoCumulatoSqlDAO;
import siap.siep.modulocumulo.dao.StatoEsecTitoloCumulatoSqlDAO;
import siap.siep.modulocumulo.dao.TitoloCumulatoSqlDAO;
import siap.siep.modulocumulo.model.BeneficioCumuloModel;
import siap.siep.modulocumulo.model.CircostanzaCumuloModel;
import siap.siep.modulocumulo.model.ComputiCumuloModel;
import siap.siep.modulocumulo.model.ContinuazioneCumuloModel;
import siap.siep.modulocumulo.model.DatiFinaliCumuloModel;
import siap.siep.modulocumulo.model.DatiFinaliUlterioriSanzioniModel;
import siap.siep.modulocumulo.model.LibAnticipataCumuloModel;
import siap.siep.modulocumulo.model.MisuraCautelareCumuloModel;
import siap.siep.modulocumulo.model.MisuraSicurezzaCumuloModel;
import siap.siep.modulocumulo.model.NotificaCumuloModel;
import siap.siep.modulocumulo.model.PenaAccessoriaCumuloModel;
import siap.siep.modulocumulo.model.PenaComplessivaCumuloModel;
import siap.siep.modulocumulo.model.PenaRideterminataCumuloModel;
import siap.siep.modulocumulo.model.PeriodoLibAntCumuloModel;
import siap.siep.modulocumulo.model.PosizioneGiuridicaCumuloModel;
import siap.siep.modulocumulo.model.ProcedimentoCumulatoModel;
import siap.siep.modulocumulo.model.ProvvedimentoGeSorvCumModel;
import siap.siep.modulocumulo.model.ReatoCumuloModel;
import siap.siep.modulocumulo.model.RichPMBeneficioCumModel;
import siap.siep.modulocumulo.model.RichPMMisSicCumModel;
import siap.siep.modulocumulo.model.RichPMPenAccCumModel;
import siap.siep.modulocumulo.model.RichPMReatoCumModel;
import siap.siep.modulocumulo.model.RichPMSanSostCumModel;
import siap.siep.modulocumulo.model.RichPMStatoEsecCumModel;
import siap.siep.modulocumulo.model.RichPMTitoloCumModel;
import siap.siep.modulocumulo.model.RichiesteInviateCumModel;
import siap.siep.modulocumulo.model.RichiestePmInCumuloModel;
import siap.siep.modulocumulo.model.SanzioneSostitutivaCumuloModel;
import siap.siep.modulocumulo.model.SoggettoCumulatoModel;
import siap.siep.modulocumulo.model.StatoEsecTitoloCumulatoModel;
import siap.siep.modulocumulo.model.TitoloCumulatoModel;
import siap.siep.modulocumulo.util.CalcoloPenaCumuloModel;
import siap.siep.modulocumulo.util.StatoEsecuzioneCumuloUtils;
import siap.siep.util.SIEPLookupRemote;

/**
 * IstruttoriaCumuloController - Classe Controller per IstruttoriaCumulo
 *
 * @version 1.0
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class IstruttoriaCumuloController extends SiapController implements IIstruttoriaCumulo {

	/*****************************************************************************
	 * Effettua l'inserimento di un IstruttoriaCumulo a partire dai dati contenuti nel Model. Inserisce anche
	 * il record CUMULO relativo al cumulante ed estrae i dati del cumulante
	 *
	 * @param aIstruttoriaCumulo
	 *            Model con i dati da inserire
	 * @param aFascicoloSiep
	 * @return il model con i dati inseriti e l'aggiunta dell'id del record inserito
	 * @throws F3BException
	 ****************************************************************************/
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	/*
	 * MEV_2025-48 – 2.14 Caricamento Istruttoria Annullata Modificata firma per gestire connessione in
	 * ingresso
	 *
	 * ATTENZIONE CHE E' UNA UTOPIA. ModuloCumuloController.ExInserisciTitoloInIstruttoria pur prevedendo la
	 * connessione in ingresso committa almeno in 2 punti
	 *
	 */
	// public IstruttoriaCumuloModel ExInserisciIstruttoriaCumulo(IstruttoriaCumuloModel aIstruttoriaCumulo,
	// FascicoloSiepModel aFascicoloSiep) throws F3BException {
	public IstruttoriaCumuloModel ExInserisciIstruttoriaCumulo(IstruttoriaCumuloModel aIstruttoriaCumulo,
			FascicoloSiepModel aFascicoloSiep, Connection aDBConnection) throws F3BException {

		Connection lConn = null;
		IstruttoriaCumuloDAO lIstruttoriaDao = null;
		IstruttoriaCumuloSqlDAO lIstruttoriaSqlDao = null;

		IstruttoriaCumuloModel lIstMod = null;

		try {

			// MEV_2025-48 – 2.14 Caricamento Istruttoria Annullata
			// lConn = getDBConnection();
			if (aDBConnection != null) {
				siesLogger.debug("Utilizzo connessione in input ");
				lConn = aDBConnection;
			} else {
				siesLogger.debug("Apro nuova connessione");
				lConn = getDBConnection();
			}
			// MEV_2025-48 – 2.14 - FINE

			// Recupero il progressivo protocollo
			lIstruttoriaSqlDao = new IstruttoriaCumuloSqlDAO(lConn);
			BigDecimal lProgr = lIstruttoriaSqlDao
					.getProgressivoIstruttoria(aIstruttoriaCumulo.getChiaveUfficio());
			aIstruttoriaCumulo.setNumProtocollo((new BigDecimal(lProgr.intValue() + 1)));

			// ========================================================================
			// Inserisco l'ISTRUTTORIA_CUMULO
			// ========================================================================
			lIstruttoriaDao = new IstruttoriaCumuloDAO(lConn);
			lIstruttoriaDao.setDAOFromModel(aIstruttoriaCumulo);

			BigDecimal lIdIstruttoria = lIstruttoriaDao.insert();

			// ========================================================================
			// Inserisco il titolo ed estraggo i dati analitici
			// ========================================================================
			IModuloCumulo lCtrlModCum = SIEPLookupRemote.getModuloCumuloRemote();

			DatiOperazioneModel lDatiOpModel = new DatiOperazioneModel();
			lDatiOpModel.setCodOperatore(aIstruttoriaCumulo.getCodOperatoreInserimento());
			lDatiOpModel.setCodUfficio(aIstruttoriaCumulo.getCodUfficioInserimento());
			lDatiOpModel.setData(aIstruttoriaCumulo.getDataInserimento());

			lCtrlModCum.ExInserisciTitoloInIstruttoria(lIdIstruttoria, aFascicoloSiep.getIdFascicoloSiep(),
					lDatiOpModel, lConn, null, "00");

			// ========================================================================

			// MEV_2025-48 – 2.14 Caricamento Istruttoria Annullata
			// commit(lConn);
			if (aDBConnection == null) {
				siesLogger.debug("ExInserisciIstruttoriaCumulo commit");
				commit(lConn);
			}
			// MEV_2025-48 – 2.14 - FINE

			lIstMod = new IstruttoriaCumuloModel(aIstruttoriaCumulo);
			lIstMod.setMessage("Inserimento avvenuto correttamente!");
			lIstMod.setIdIstruttoriaCumulo(lIdIstruttoria);
		} catch (DAOException ex) {
			rollback(lConn);
			siesLogger.error("", ex);
			throw new F3BException(
					"IstruttoriaCumuloController.ExInserisciIstruttoriaCumulo: Non posso inserire: " + ex);
		} finally {
			cleanup(lIstruttoriaDao);
			cleanup(lIstruttoriaSqlDao);

			// MEV_2025-48 – 2.14 Caricamento Istruttoria Annullata
			// cleanup(lConn);
			if (aDBConnection == null) {
				cleanup(lConn);
			}
			// MEV_2025-48 – 2.14 - FINE
		}

		return lIstMod;
	}

	public BigDecimal ExCountIstruttoriaCumuloPaged(IstruttoriaCumuloModel aIstruttoriaCumulo)
			throws F3BException {

		Connection lConn = null;
		BigDecimal HowManyRecords = null;
		IstruttoriaCumuloSqlDAO lIstCumSqlDao = null;

		try {
			lConn = getDBConnection();
			lIstCumSqlDao = new IstruttoriaCumuloSqlDAO(lConn);
			lIstCumSqlDao.getCountIstruttoriaCumulo(aIstruttoriaCumulo);
			lIstCumSqlDao.start();
			lIstCumSqlDao.next();
			HowManyRecords = lIstCumSqlDao.getBigDecimal("HowManyRecords");
			siesLogger.debug("--XX-- totale record = " + HowManyRecords);

		} catch (DAOException daoEx) {
			rollback(lConn);
			siesLogger.error("DAOException: ", daoEx);
			throw new SIEPException(F3BException.USER_MESSAGE,
					"IstruttoriaCumuloController.ExCountIstruttoriaCumuloPaged: " + daoEx);
		} catch (Exception e) {
			rollback(lConn);
			siesLogger.error("Exception: ", e);
			throw new SIEPException(F3BException.USER_MESSAGE, e.getMessage());
		} finally {
			cleanup(lIstCumSqlDao);
			cleanup(lConn);
		}

		return HowManyRecords;
	}

	/*****************************************************************************
	 * Effettua la ricerca dei dati IstruttoriaCumulo
	 *
	 * @param aIstruttoriaCumulo
	 *            Model utilizzato per costruire le condizioni di ricerca Ogni valore attualizzato nel model
	 *            verrà utilizzato per imporre una condizione di ricerca
	 * @return un vettore di model con il risultato della ricerca
	 * @throws F3BException
	 ****************************************************************************/
	public Vector<IstruttoriaCumuloModel> ExRicercaIstruttoriaCumulo(
			IstruttoriaCumuloModel aIstruttoriaCumulo) throws F3BException {

		Connection lConn = null;
		Vector<IstruttoriaCumuloModel> lIstruttoriaCumuli = new Vector<>();
		IstruttoriaCumuloDAO lIstDao = null;

		try {
			lConn = getDBConnection();
			lIstDao = new IstruttoriaCumuloDAO(lConn);
			lIstDao.setCondizioni(aIstruttoriaCumulo);
			lIstDao.setOrderBy();
			lIstDao.start();
			while (lIstDao.next()) {
				lIstruttoriaCumuli.add((IstruttoriaCumuloModel) lIstDao.getModel());
			}
			lIstDao.stop();
		} catch (DAOException daoEx) {
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException(
					"IstruttoriaCumuloController.ExRicercaIstruttoriaCumulo: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lIstDao);
			cleanup(lConn);
		}

		return lIstruttoriaCumuli;
	}

	public BigDecimal ExCountIstruttoriaPerTitoloCumulato(IstruttoriaCumuloModel aIstruttoriaCumulo)
			throws F3BException {

		siesLogger.debug("--XX-- ExCountIstruttoriaPerTitoloCumulato ...... INIZIO  ");
		Connection lConn = null;
		BigDecimal HowManyRecords = null;
		IstruttoriaCumuloSqlDAO lIstCumSqlDao = null;

		try {
			lConn = getDBConnection();
			lIstCumSqlDao = new IstruttoriaCumuloSqlDAO(lConn);
			lIstCumSqlDao.getCountIstruttoriaPerTitoloCumulato(aIstruttoriaCumulo);
			lIstCumSqlDao.start();
			lIstCumSqlDao.next();
			HowManyRecords = lIstCumSqlDao.getBigDecimal("HowManyRecords");
			siesLogger.debug("--XX-- totale record = " + HowManyRecords);

		} catch (DAOException daoEx) {
			rollback(lConn);
			siesLogger.error("DAOException: ", daoEx);
			throw new SIEPException(F3BException.USER_MESSAGE,
					"IstruttoriaCumuloController.ExCountIstruttoriaPerTitoloCumulato: " + daoEx);
		} catch (Exception e) {
			rollback(lConn);
			siesLogger.error("Exception: ", e);
			throw new SIEPException(F3BException.USER_MESSAGE, e.getMessage());
		} finally {
			cleanup(lIstCumSqlDao);
			cleanup(lConn);
		}

		return HowManyRecords;
	}

	/*******************************************************************************************
	 * Funzione di ricerca utilizzata per la paginazione che restituisce i risultati da visualizzare nella
	 * pagina specificata in input
	 *
	 * @param aIstruttoriaCumulo
	 *            Model utilizzato per costruire le condizioni di ricerca
	 * @param aPage
	 *            pagina per la quale si vogliono ottenere i risultati Ogni valore attualizzato nel model
	 *            verrà utilizzato per imporre una condizione di ricerca, compreso il ProvvedimentoCumulo che
	 *            contiene nella circostanza i parametri di Titolo Cumulato impostati per la ricerca.
	 * @return un vettore di model con il risultato della ricerca
	 * @throws F3BException
	 *******************************************************************************************/
	public Vector ExRicercaIstruttoriaPerTitoloCumulatoPaged(IstruttoriaCumuloModel aIstruttoriaCumulo,
			int aPage) throws F3BException {

		Connection lConn = null;
		Vector lIstruttoriaCumuli = new Vector();
		IstruttoriaCumuloSqlDAO lIstruttoriaCumuloSqlDao = null;

		try {
			lConn = getDBConnection();
			lIstruttoriaCumuloSqlDao = new IstruttoriaCumuloSqlDAO(lConn);
			lIstruttoriaCumuloSqlDao.ricercaIstruttoriaPerTitoloCumulatoPaged(aIstruttoriaCumulo, aPage);
			lIstruttoriaCumuli = new Vector(lIstruttoriaCumuloSqlDao.getModels());
		} catch (DAOException daoEx) {
			siesLogger.error("DAOException: ", daoEx);
			throw new F3BException(
					"IstruttoriaCumuloController.ExRicercaIstruttoriaPerTitoloCumulatoPaged: Non posso leggere : "
							+ daoEx);
		} finally {
			cleanup(lIstruttoriaCumuloSqlDao);
			cleanup(lConn);
		}
		return lIstruttoriaCumuli;
	}

	public BigDecimal ExCountIstruttoriaPerFasSIEP(FascicoloSiepModel aFasSiep, String ufficio)
			throws F3BException {

		Connection lConn = null;
		BigDecimal HowManyRecords = null;
		IstruttoriaCumuloSqlDAO lIstCumSqlDao = null;

		try {
			lConn = getDBConnection();
			lIstCumSqlDao = new IstruttoriaCumuloSqlDAO(lConn);
			lIstCumSqlDao.getCountIstruttoriaPerFasSiep(aFasSiep, ufficio);
			lIstCumSqlDao.start();
			lIstCumSqlDao.next();
			HowManyRecords = lIstCumSqlDao.getBigDecimal("HowManyRecords");
			siesLogger.debug("--XX-- totale record = " + HowManyRecords);

		} catch (DAOException daoEx) {
			rollback(lConn);
			siesLogger.error("DAOException: ", daoEx);
			throw new SIEPException(F3BException.USER_MESSAGE,
					"IstruttoriaCumuloController.ExCountIstruttoriaPerFasSIEP: " + daoEx);
		} catch (Exception e) {
			rollback(lConn);
			siesLogger.error("Exception: ", e);
			throw new SIEPException(F3BException.USER_MESSAGE, e.getMessage());
		} finally {
			cleanup(lIstCumSqlDao);
			cleanup(lConn);
		}

		return HowManyRecords;
	}

	/*******************************************************************************************
	 * Effettua la ricerca dei dati IstruttoriaCumulo
	 *
	 * @param aFascicoloSIEP
	 *            Model utilizzato per costruire le condizioni di ricerca
	 * @return un vettore di model con il risultato della ricerca
	 * @throws F3BException
	 *******************************************************************************************/
	public Vector<IstruttoriaCumuloModel> ExRicercaIstruttoriaPerFasSIEPpaged(
			FascicoloSiepModel aFascicoloSiep, String ufficio, int aPage) throws F3BException {

		Connection lConn = null;
		Vector lIstruttoriaCumuli = new Vector();
		IstruttoriaCumuloSqlDAO lIstruttoriaCumuloSqlDao = null;

		try {
			lConn = getDBConnection();
			lIstruttoriaCumuloSqlDao = new IstruttoriaCumuloSqlDAO(lConn);
			lIstruttoriaCumuloSqlDao.ricercaIstruttoriaPerFasSIEPpaged(aFascicoloSiep, ufficio, aPage);
			lIstruttoriaCumuli = new Vector(lIstruttoriaCumuloSqlDao.getModels());
		} catch (DAOException daoEx) {
			siesLogger.error("DAOException: ", daoEx);
			throw new F3BException(
					"IstruttoriaCumuloController.ExRicercaIstruttoriaPerTitoloCumulato: Non posso leggere : "
							+ daoEx);
		} finally {
			cleanup(lIstruttoriaCumuloSqlDao);
			cleanup(lConn);
		}
		return lIstruttoriaCumuli;
	}

	/*****************************************************************************
	 * Effettua la ricerca per chiave
	 *
	 * @param akey
	 *            valore della chiave del record da ricercare
	 * @return il model con i dati trovati
	 * @throws F3BException
	 ****************************************************************************/
	public IstruttoriaCumuloModel ExRicercaIstruttoriaCumuloById(BigDecimal aIdIstruttoriaCumulo)
			throws F3BException {

		Connection lConn = null;
		IstruttoriaCumuloModel lIstruttoriaCumuloMod = new IstruttoriaCumuloModel();
		IstruttoriaCumuloSqlDAO lIstruttoriaCumuloSqlDao = null;

		try {
			lConn = getDBConnection();
			lIstruttoriaCumuloSqlDao = new IstruttoriaCumuloSqlDAO(lConn);
			lIstruttoriaCumuloSqlDao.ricercaIstruttoriaCumuloByKey(aIdIstruttoriaCumulo);
			lIstruttoriaCumuloMod = (IstruttoriaCumuloModel) lIstruttoriaCumuloSqlDao.getModelByKey();
		} catch (DAOException daoEx) {
			siesLogger.error("DAOException: " + daoEx.getMessage(), daoEx);
			throw new F3BException(
					"IstruttoriaCumuloController.ExRicercaIstruttoriaCumuloById: Non posso leggere : "
							+ daoEx);
		} finally {
			cleanup(lIstruttoriaCumuloSqlDao);
			cleanup(lConn);
		}

		return lIstruttoriaCumuloMod;
	}

	/*****************************************************************************
	 * Metodo che modifica i dati dell'IstruttoriaCumulo Viene fatto l'update di tutti i campi del record
	 * recuperando i valori dal Model Se mancano dati nel model i corrispondenti valori della tabella verranno
	 * impostati a null
	 *
	 * @param aIstruttoriaCumulo
	 *            Model con i nuovi valori
	 * @throws F3BException
	 ****************************************************************************/
	public void ExModificaIstruttoriaCumulo(IstruttoriaCumuloModel aIstruttoriaCumulo) throws F3BException {

		Connection lConn = null;
		IstruttoriaCumuloDAO lIstDao = null;

		try {
			lConn = getDBConnection();
			lIstDao = new IstruttoriaCumuloDAO(lConn);
			lIstDao.setDAOFromModel(aIstruttoriaCumulo);
			lIstDao.selCondizioneUpdate(aIstruttoriaCumulo.getIdIstruttoriaCumulo());
			lIstDao.update();
			commit(lConn);
		} catch (DAOException ex) {
			rollback(lConn);
			siesLogger.error("DAOException: ", ex);
			throw new F3BException(
					"IstruttoriaCumuloController.ExModificaIstruttoriaCumulo: Non posso inserire: " + ex);
		} finally {
			cleanup(lIstDao);
			cleanup(lConn);
		}
	}

	/*****************************************************************************
	 * Effettua la cancellazione logica dell'istruttoria impostando il FLAG_STATO a N
	 *
	 * @param aIstruttoriaCumulo
	 * @throws F3BException
	 ****************************************************************************/
	public void ExCancellaIstruttoriaCumulo(IstruttoriaCumuloModel aIstruttoriaCumulo) throws F3BException {

		Connection lConn = null;
		IstruttoriaCumuloDAO lIstDao = null;

		try {
			lConn = getDBConnection();
			lIstDao = new IstruttoriaCumuloDAO(lConn);
			lIstDao.selCondizioneUpdate(aIstruttoriaCumulo.getIdIstruttoriaCumulo());
			lIstDao.delete();
			commit(lConn);
		} catch (DAOException daoEx) {
			siesLogger.error("DAOException: ", daoEx);
			rollback(lConn);
			throw new F3BException(
					"IstruttoriaCumuloController.ExCancellaIstruttoriaCumulo: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lIstDao);
			cleanup(lConn);
		}
	}

	/*****************************************************************************
	 * Recupera il numero di record restituiti della ricerca. Utile in caso di ricerche paginate per ottenere
	 * il numero totale di record
	 *
	 * @param aIstruttoriaCumulo
	 * @return numero di record trovati dalla funzione dei ricerca
	 * @throws F3BException
	 ****************************************************************************/
	public BigDecimal ExGetCountIstruttoriaCumulo(IstruttoriaCumuloModel aIstruttoriaCumulo)
			throws F3BException {

		Connection lConn = null;
		BigDecimal lCount = new BigDecimal(0);
		IstruttoriaCumuloSqlDAO lIstruttoriaCumuloSqlDao = null;

		try {
			lConn = getDBConnection();
			lIstruttoriaCumuloSqlDao = new IstruttoriaCumuloSqlDAO(lConn);
			lIstruttoriaCumuloSqlDao.getCountIstruttoriaCumulo(aIstruttoriaCumulo);
			lIstruttoriaCumuloSqlDao.start();
			lIstruttoriaCumuloSqlDao.next();
			lCount = lIstruttoriaCumuloSqlDao.getBigDecimal("HowManyRecords");
			lIstruttoriaCumuloSqlDao.stop();
		} catch (DAOException daoEx) {
			siesLogger.error("DAOException: ", daoEx);
			throw new F3BException(
					"IstruttoriaCumuloController.ExGetCountIstruttoriaCumulo: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lIstruttoriaCumuloSqlDao);
			cleanup(lConn);
		}

		return lCount;
	}

	/*****************************************************************************
	 * Funzione di ricerca utilizzata per la paginazione che restituisce i risultati da visualizzare nella
	 * pagina specificata in input
	 *
	 * @param aIstruttoriaCumulo
	 *            model contenete i parametri della ricerca
	 * @param aPage
	 *            pagina per la quale si vogliono ottenere i risultati
	 * @return Vettore di model con i record da visualizzare nella pagina specificata in input
	 * @throws F3BException
	 ****************************************************************************/
	public Vector ExRicercaIstruttoriaCumuloPaged(IstruttoriaCumuloModel aIstruttoriaCumulo, int aPage)
			throws F3BException {

		Connection lConn = null;
		Vector lIstruttoriaCumuli = new Vector();
		IstruttoriaCumuloSqlDAO lIstruttoriaCumuloSqlDao = null;

		try {
			lConn = getDBConnection();
			lIstruttoriaCumuloSqlDao = new IstruttoriaCumuloSqlDAO(lConn);
			lIstruttoriaCumuloSqlDao.ricercaIstruttoriaCumuloPaged(aIstruttoriaCumulo, aPage);
			lIstruttoriaCumuli = new Vector(lIstruttoriaCumuloSqlDao.getModels());
		} catch (DAOException daoEx) {
			siesLogger.error("DAOException: ", daoEx);
			throw new F3BException(
					"IstruttoriaCumuloController.ExRicercaIstruttoriaCumuloPaged: Non posso leggere : "
							+ daoEx);
		} finally {
			cleanup(lIstruttoriaCumuloSqlDao);
			cleanup(lConn);
		}
		return lIstruttoriaCumuli;
	}

	/*****************************************************************************
	 * Metodo che annulla una istruttoria cumulo impostando il FLAG_STATO dell'istruttoria a 'N' e il
	 * flag_validato dei record CUMULI associati ad 'A'
	 *
	 * @param aIstruttoriaCumulo
	 *            Model con i nuovi valori
	 * @throws F3BException
	 ****************************************************************************/
	public void ExAnnullaIstruttoriaCumulo(IstruttoriaCumuloModel aIstruttoriaCumulo) throws F3BException {

		Connection lConn = null;

		IstruttoriaCumuloDAO lIstruttoriaDao = null;
		CumuloDAO lCumuloDAO = null;

		// Ticket#20220803015 -
		TitoloCumulatoSqlDAO lTitoloCumulatoSqlDao = null;
		ProcedimentoCumulatoSqlDAO lProcCumSqlDao = null;
		AnnotazioneEsitoTrasmissioneDAO lAnnEsiDao = null;
		AnnotazioneEsitoTrasmissioneSqlDAO lAnnEsiSqlDao = null;
		CampoNotaDAO lCampoNotaDao = null;
		EventoDAO lEveDao = null;
		// Ticket#20220803015 -
		// Ticket#20220831013 - inizio
		EventoStoreProcedurePulisciDAO lEventoProcSqlDao = null;
		DatiFinaliCumuloSqlDAO lDatiFinaliSqlDAO = null;
		DatiFinaliCumuloDAO lDatiFinaliCumuloDAO = null;
		EventoSqlDAO lEveSqlDao = null;
		// Ticket#20220831013 - fine

		try {
			lConn = getDBConnection();

			// ========================================================================
			// Annullo l'istruttoria
			// ========================================================================
			lIstruttoriaDao = new IstruttoriaCumuloDAO(lConn);
			lIstruttoriaDao.setDAOFromModel(aIstruttoriaCumulo);
			lIstruttoriaDao.selCondizioneUpdate(aIstruttoriaCumulo.getIdIstruttoriaCumulo());
			lIstruttoriaDao.update();

			// Ticket#20220803015 - Se l'evento è già stato annullato sul fascicolo cumulante
			// è stata aggiunta la nota annullamento sulla tabella CAMPO_NOTA per cui la delete
			// dell'evento va in errore (FK). Si cancellaanche la nota se presente.
			lTitoloCumulatoSqlDao = new TitoloCumulatoSqlDAO(lConn);
			lTitoloCumulatoSqlDao.ricercaTitoloCumulatoByIstruttoriaOrderBy(
					aIstruttoriaCumulo.getIdIstruttoriaCumulo(), null);
			lTitoloCumulatoSqlDao.start();

			lEveDao = new EventoDAO(lConn);
			lProcCumSqlDao = new ProcedimentoCumulatoSqlDAO(lConn);
			lAnnEsiDao = new AnnotazioneEsitoTrasmissioneDAO(lConn);
			lAnnEsiSqlDao = new AnnotazioneEsitoTrasmissioneSqlDAO(lConn);
			lCampoNotaDao = new CampoNotaDAO(lConn);

			siesLogger.debug(
					"Ricerco eventuali titoli 'Iscrizione in Istruttoria Fascicolo proprio Ufficio' per annullare gli eventi di trasmissione sui cumulati");
			while (lTitoloCumulatoSqlDao.next()) {
				TitoloCumulatoModel lTitoloModel = (TitoloCumulatoModel) lTitoloCumulatoSqlDao.getModel();
				// 04 = Iscrizione in Istruttoeia Fascicolo proprio Ufficio
				if ("04".equals(lTitoloModel.getTipoIscrizione())) {
					siesLogger.debug(
							"Titolo iscritto proprio ufficio: id = " + lTitoloModel.getIdTitoloCumulato());
					siesLogger.debug("Ricerco il Procedimento Cumulato per avere Eve_id_Evento");

					lProcCumSqlDao.ricercaProcedimentoCumulatoByIdTitolo(lTitoloModel.getIdTitoloCumulato());
					ProcedimentoCumulatoModel lProcMod = (ProcedimentoCumulatoModel) lProcCumSqlDao
							.getModelByKey();
					lProcCumSqlDao.stop();

					if (lProcMod != null && lProcMod.getEveIdEvento() != null) {
						// Ricerco e Cancello Annotazione_Esito_Trasmissione
						lAnnEsiSqlDao
								.ricercaAnnotazioneEsitoTrasmissioneByIdEvento(lProcMod.getEveIdEvento());
						AnnotazioneEsitoTrasmissioneModel lAnnMod = (AnnotazioneEsitoTrasmissioneModel) lAnnEsiSqlDao
								.getModelByKey();
						lAnnEsiSqlDao.stop();

						lAnnEsiDao.selCondizioneUpdate(lAnnMod.getIdEsitoTrasmissione());
						lAnnEsiDao.delete();
						lAnnEsiDao.stop();

						siesLogger.debug("Cancellato AnnotazioneEsitoTrasmissioneModel");

						lCampoNotaDao.setCondizioneEvento(lProcMod.getEveIdEvento());
						lCampoNotaDao.delete();
						lCampoNotaDao.stop();

						// Cancello Evento
						lEveDao.selCondizioneUpdate(lProcMod.getEveIdEvento());
						lEveDao.delete();
						lEveDao.stop();
						// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto
						// di LogF3B.getLogger()
						siesLogger.debug("Cancellato Evento");
					}
				}
			}
			// Ticket#20220803015 - FINE

			// ========================================================================
			// Annullo tutti i CUMULI legati all'istruttoria
			// ========================================================================
			lCumuloDAO = new CumuloDAO(lConn);

			lCumuloDAO.setFlagValidato("A");
			lCumuloDAO.setCodOperatoreAggiornamento(aIstruttoriaCumulo.getCodOperatoreAggiornamento());
			lCumuloDAO.setCodUfficioAggiornamento(aIstruttoriaCumulo.getCodUfficioAggiornamento());
			lCumuloDAO.setDataAggiornamento(aIstruttoriaCumulo.getDataAggiornamento());

			lCumuloDAO.setCondizioneUpdateByFascIstr(aIstruttoriaCumulo.getFasSieIdFascicoloSiep(),
					aIstruttoriaCumulo.getIdIstruttoriaCumulo());

			lCumuloDAO.update();

			// Ticket#20220831013 - procedura SIEP n. 4/2022 PROCURA DEI MINORI DI CAMPOBASSO
			// Verifico se è stato prodotto, non validato, il provvedimento di cumulo, in questo caso
			// va cancellato altrimenti blocca l'emissione di altri provvedimenti
			lDatiFinaliSqlDAO = new DatiFinaliCumuloSqlDAO(lConn);
			lDatiFinaliSqlDAO
					.ricercaDatiFinaliCumuloByIdIstruttoria(aIstruttoriaCumulo.getIdIstruttoriaCumulo());

			DatiFinaliCumuloModel lDatiFinaliModel = (DatiFinaliCumuloModel) lDatiFinaliSqlDAO
					.getModelByKey();

			if (lDatiFinaliModel != null && lDatiFinaliModel.getEveIdEvento() != null) {
				// pulisco il puntamento dalla DatiFinaliCumulo
				lDatiFinaliCumuloDAO = new DatiFinaliCumuloDAO(lConn);
				lDatiFinaliCumuloDAO.selCondizioneUpdate(lDatiFinaliModel.getIdDatiFinaliCumulo());
				lDatiFinaliCumuloDAO.setEveIdEvento(null);
				lDatiFinaliCumuloDAO.update();

				// n.b. per sicurezza si testa la presenza effettiva dell'evento non essendo presente la FK
				lEveSqlDao = new EventoSqlDAO(lConn);
				lEveSqlDao.ricercaEventoByKey(lDatiFinaliModel.getEveIdEvento());
				EventoModel lProvvedimento = (EventoModel) lEveSqlDao.getModelByKey();

				if (lProvvedimento != null) {
					// se l'eveto esiste lo elimino
					lEventoProcSqlDao = new EventoStoreProcedurePulisciDAO(lConn);
					lEventoProcSqlDao.setIdEvento(lProvvedimento.getIdEvento());
					lEventoProcSqlDao.execute();
				}
			}
			// Ticket#20220831013 - FINE

			commit(lConn);
		} catch (DAOException ex) {
			rollback(lConn);
			siesLogger.error("DAOException: ", ex);
			throw new F3BException(
					"IstruttoriaCumuloController.ExAnnullaIstruttoriaCumulo: Non posso inserire: " + ex);
		} finally {
			cleanup(lIstruttoriaDao);
			cleanup(lCumuloDAO);
			cleanup(lTitoloCumulatoSqlDao);
			cleanup(lProcCumSqlDao);
			cleanup(lAnnEsiDao);
			cleanup(lAnnEsiSqlDao);
			cleanup(lCampoNotaDao);
			cleanup(lEveDao);
			// Ticket#20220831013 - inizio
			cleanup(lEventoProcSqlDao);
			cleanup(lDatiFinaliSqlDAO);
			cleanup(lDatiFinaliCumuloDAO);
			cleanup(lEveSqlDao);
			// Ticket#20220831013 - fine

			cleanup(lConn);
		}
	}

	public Vector<MessaggioModel> ExRicercaFascicoliTrasmessi(BigDecimal aChiaveAnno, BigDecimal aChiaveProgr,
			String aChiaveUfficio, String aFlagInCarico) throws F3BException {

		// Effettua la ricerca sulla tabella messaggio
		Connection lConn = null;

		Vector<MessaggioModel> lListaMessaggi = new Vector<>();
		MessaggioSqlDAO lMesSqlDao = null;

		MessaggioModel lMessMod = new MessaggioModel();
		lMessMod.setCodTipoMessaggio(ICostantiJMS.RICHIESTA);
		lMessMod.setCodTipoOperazione(ICostantiJMS.TRASFERIMENTO_COMPETENZA);

		lMessMod.setFlagVisto(aFlagInCarico);

		lMessMod.setChiaveAnnoFasCumulante(aChiaveAnno);
		lMessMod.setChiaveProgrFasCumulante(aChiaveProgr);
		lMessMod.setCodUfficioDestinatario(aChiaveUfficio);

		try {
			lConn = getDBConnection();
			lMesSqlDao = new MessaggioSqlDAO(lConn);
			lMesSqlDao.ricercaMessaggio(lMessMod);
			lListaMessaggi = new Vector<MessaggioModel>(lMesSqlDao.getModels());
		} catch (DAOException daoEx) {
			siesLogger.error(this.getClass().getName(), daoEx);
			throw new F3BException(
					"IstruttoriaCumuloController.ExRicercaFascicoliTrasmessi: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lMesSqlDao);
			cleanup(lConn);
		}

		return lListaMessaggi;
	}

	/**
	 * Effettua la ricerca dei titoli cumulati + procedimento cumulato collegati all'istruttoria.
	 *
	 *
	 */
	public Vector<TitoloCumulatoModel> ExRicercaTitoliByIstruttoria(BigDecimal aIdIstruttoriaCumulo)
			throws F3BException {

		return ExRicercaTitoliByIstruttoriaOrderBy(aIdIstruttoriaCumulo, null);
	}

	/**
	 * Effettua la ricerca dei titoli cumulati + procedimento cumulato collegati all'istruttoria.
	 *
	 *
	 */
	public Vector<TitoloCumulatoModel> ExRicercaTitoliByIstruttoriaOrderBy(BigDecimal aIdIstruttoriaCumulo,
			String aOrdinamento) throws F3BException {

		Connection lConn = null;

		Vector<TitoloCumulatoModel> lTitoliInIstruttoria = new Vector<>();

		TitoloCumulatoSqlDAO lTitoloSqlDao = null;
		TitoloCumulatoModel lTitoloRicerca = new TitoloCumulatoModel();

		ProcedimentoCumulatoSqlDAO lProcCumSqlDao = null;
		SoggettoCumulatoSqlDAO lSoggCumSqlDao = null;
		UfficioSqlDAO lUffSqlDao = null;

		lTitoloRicerca.setIstrIdIstruttoriaCumulo(aIdIstruttoriaCumulo);

		try {
			lConn = getDBConnection();

			lTitoloSqlDao = new TitoloCumulatoSqlDAO(lConn);
			lProcCumSqlDao = new ProcedimentoCumulatoSqlDAO(lConn);
			lSoggCumSqlDao = new SoggettoCumulatoSqlDAO(lConn);
			lUffSqlDao = new UfficioSqlDAO(lConn);

			lTitoloSqlDao.ricercaTitoloCumulatoByIstruttoriaOrderBy(aIdIstruttoriaCumulo, aOrdinamento);

			lTitoloSqlDao.start();

			while (lTitoloSqlDao.next()) {
				TitoloCumulatoModel lTitolo = (TitoloCumulatoModel) lTitoloSqlDao.getModel();
				lTitoliInIstruttoria.add(lTitolo);

				lProcCumSqlDao.ricercaProcedimentoCumulatoByIdTitolo(lTitolo.getIdTitoloCumulato());
				ProcedimentoCumulatoModel lProcModel = (ProcedimentoCumulatoModel) lProcCumSqlDao
						.getModelByKey();
				lTitolo.setProcedimentoCumulato(lProcModel);
				lProcCumSqlDao.stop();

				if (lProcModel != null && "S".equals(lProcModel.getFlagAccorpato())) {

					lUffSqlDao.selUfficioByCod(lProcModel.getChiaveUfficioOrigine());
					UfficioModel lUffMod = (UfficioModel) lUffSqlDao.getModelByKey();
					lUffSqlDao.stop();

					lProcModel.setUfficioOrigine(lUffMod);
				}

				lSoggCumSqlDao.ricercaSoggettoCumulatoByIdTitolo(lTitolo.getIdTitoloCumulato());
				SoggettoCumulatoModel lSoggModel = (SoggettoCumulatoModel) lSoggCumSqlDao.getModelByKey();
				lTitolo.setSoggettoCumulato(lSoggModel);
				lSoggCumSqlDao.stop();

			}
			lTitoloSqlDao.stop();
		} catch (DAOException daoEx) {
			siesLogger.error("DAOException: ", daoEx);
			throw new F3BException(
					"IstruttoriaCumuloController.ExRicercaTitoliByIstruttoria: Non posso leggere : " + daoEx);
		} catch (Exception ex) {
			siesLogger.error("Exception: ", ex);
			throw new F3BException(
					"IstruttoriaCumuloController.ExRicercaTitoliByIstruttoria: Non posso leggere : " + ex);
		} finally {
			cleanup(lTitoloSqlDao);
			cleanup(lProcCumSqlDao);
			cleanup(lSoggCumSqlDao);
			cleanup(lUffSqlDao);

			cleanup(lConn);
		}

		return lTitoliInIstruttoria;
	}

	public BigDecimal ExInserisciAnnotazioneEsitoTrasmComp(EventoNotificaModel aEventoNot,
			AnnotazioneEsitoTrasmissioneModel aAnnotaModel, BigDecimal aIdMess) throws F3BException {

		siesLogger.debug("Inizio inserimento annotazione...");

		Connection lConn = null;

		EventoDAO lEveDao = null;
		EventoSqlDAO lEventoSqlDao = null;
		CampoNotaDAO lCampoNotaDao = null;
		AnnotazioneEsitoTrasmissioneDAO lAnnotaDao = null;
		MessaggioDAO lMesDao = null;
		MessaggioSqlDAO lMesSqlDao = null;

		BigDecimal lKeyEvento = null;
		try {
			lConn = getDBConnection();

			// Setto l'anno e il progressivo...
			lEventoSqlDao = new EventoSqlDAO(lConn);
			BigDecimal lProgr = lEventoSqlDao.getProgressivo(aEventoNot.getEvento());
			aEventoNot.getEvento().setProgrProtocollo(new BigDecimal(lProgr.intValue() + 1));

			// =============================================
			// Insert dell'evento
			// =============================================
			lEveDao = new EventoDAO(lConn);
			lEveDao.setDAOFromModel(aEventoNot.getEvento());
			lKeyEvento = lEveDao.insert();
			lEveDao.stop();

			// =============================================
			// Insert AnnotazioneEsito
			// =============================================
			lAnnotaDao = new AnnotazioneEsitoTrasmissioneDAO(lConn);
			aAnnotaModel.setEveIdEvento(lKeyEvento);
			lAnnotaDao.setDAOFromModel(aAnnotaModel);
			lAnnotaDao.insert();
			lAnnotaDao.stop();

			// =============================================
			// Insert Note
			// =============================================
			if (aEventoNot.getCampoNote() != null) {
				siesLogger.debug("Inserimento Eventuali Note Aggiuntive Numero note Aggiuntive : "
						+ aEventoNot.getCampoNote().length);
				int count = 0;
				lCampoNotaDao = new CampoNotaDAO(lConn);
				while (count < aEventoNot.getCampoNote().length) {
					aEventoNot.getCampoNote()[count].setEveIdEvento(lKeyEvento);
					aEventoNot.getCampoNote()[count].setProgressivo(new BigDecimal((double) count + 1));
					lCampoNotaDao.setDAOFromModel(aEventoNot.getCampoNote()[count]);
					lCampoNotaDao.insert();
					lCampoNotaDao.stop();

					count++;
				}
			}

			// ===================================================
			// Marco il Messaggio di Comunicazione Evasa
			// ===================================================
			// MEV_2025-48 - 2.15 Gestione Annotazioni Trasmissioni
			// La marcatura viene fatta solo sulla validazione e non sull'ijserimento
			/*
			 * if (aIdMess != null) { siesLogger.debug("--XX-- Vado a Modoficare il messaggio..."); lMesDao =
			 * new MessaggioDAO(lConn); lMesSqlDao = new MessaggioSqlDAO(lConn); MessaggioModel lMesMod =
			 * null;
			 *
			 * lMesSqlDao.ricercaMessaggioByKey(aIdMess); lMesMod = (MessaggioModel)
			 * lMesSqlDao.getModelByKey(); lMesSqlDao.stop();
			 *
			 * if (lMesMod != null && lMesMod.getIdMessaggio() != null) { lMesMod.setFlagVisto("S");
			 * lMesDao.setDAOFromModelForUpdate(lMesMod); lMesDao.update(); } }
			 */
			// MEV_2025-48 - 2.15 Gestione Annotazioni Trasmissioni - FINE

			commit(lConn);
		} catch (Exception e) {
			rollback(lConn);
			siesLogger.error("IstruttoriaCumuloController.ExInserisciAnnotazioneEsito: ", e);
			throw new F3BException("IstruttoriaCumuloController.ExInserisciAnnotazioneEsito: " + e);
		} finally {
			cleanup(lEveDao);
			cleanup(lEventoSqlDao);
			cleanup(lCampoNotaDao);
			cleanup(lAnnotaDao);
			cleanup(lMesDao);
			cleanup(lMesSqlDao);

			cleanup(lConn);
		}
		return lKeyEvento;
	}

	public EventoModel ExUpdateValidaAnnotazioneEsitoTrasmComp(EventoModel aEvento,
			FascicoloSiepModel aFascicolo) throws F3BException {

		Connection lConn = null;

		EventoModel lEveMod = new EventoModel(aEvento);

		EventoSqlDAO lEveSqlDao = null;
		EventoDAO lEveDaoBlob = null;

		try {
			lConn = getDBTransaction();

			// ========================================================================
			// Recupero l'EVENTO completo, quello in input contiene solo i dati da
			// aggiornare
			// ========================================================================
			lEveSqlDao = new EventoSqlDAO(lConn);
			lEveSqlDao.ricercaEventoByKey(aEvento.getIdEvento());
			/* EventoModel lEveModel = (EventoModel) */lEveSqlDao.getModelByKey();

			// ========================================================================
			// Aggiorno il blob sull'evento
			// ========================================================================
			lEveDaoBlob = new EventoDAO(lConn);
			lEveDaoBlob.setDAOFromModelForUpdateBlob(aEvento);

			lEveDaoBlob.selCondizioneUpdate(aEvento.getIdEvento());
			lEveDaoBlob.update();
			lEveDaoBlob.stop();

			commit(lConn);

		} catch (Exception ex) {
			siesLogger.error("Exception: " + ex, ex);
			rollback(lConn);
			ex.printStackTrace();
			throw new F3BException(
					"IstruttoriaCumuloController.ExUpdateValidaAnnotazioneEsitoTrasmComp : " + ex);
		} finally {
			cleanup(lEveSqlDao);
			cleanup(lEveDaoBlob);
			cleanup(lConn);
		}

		return lEveMod;
	}

	public void ExUpdateOrdinamentoTitoli(IstruttoriaCumuloModel aIstruttoriaCumulo) throws F3BException {

		Connection lConn = null;
		IstruttoriaCumuloDAO lIstDao = null;

		try {
			lConn = getDBConnection();
			lIstDao = new IstruttoriaCumuloDAO(lConn);
			lIstDao.setOrdinamentoTitoli(aIstruttoriaCumulo.getOrdinamentoTitoli());
			lIstDao.selCondizioneUpdate(aIstruttoriaCumulo.getIdIstruttoriaCumulo());
			lIstDao.update();
			commit(lConn);
		} catch (DAOException ex) {
			rollback(lConn);
			siesLogger.error("DAOException: ", ex);
			throw new F3BException(
					"IstruttoriaCumuloController.ExUpdateOrdinamentoTitoli: Non posso inserire: " + ex);
		} finally {
			cleanup(lIstDao);
			cleanup(lConn);
		}
	}

	public Vector<TitoloCumulatoModel> ExRicercaTitoliValidiByIstruttoriaOrderBy(
			BigDecimal aIdIstruttoriaCumulo, String aOrdinamento) throws F3BException {

		Connection lConn = null;

		Vector<TitoloCumulatoModel> lTitoliInIstruttoria = new Vector<>();

		TitoloCumulatoSqlDAO lTitoloSqlDao = null;
		SoggettoCumulatoSqlDAO lSoggCumSqlDao = null;
		ProcedimentoCumulatoSqlDAO lProcCumSqlDao = null;
		UfficioSqlDAO lUffSqlDao = null;

		try {
			lConn = getDBConnection();

			lTitoloSqlDao = new TitoloCumulatoSqlDAO(lConn);
			lTitoloSqlDao.ricercaTitoloCumulatoByIstruttoriaOrderBy(aIdIstruttoriaCumulo, aOrdinamento);

			lSoggCumSqlDao = new SoggettoCumulatoSqlDAO(lConn);
			lProcCumSqlDao = new ProcedimentoCumulatoSqlDAO(lConn);
			lUffSqlDao = new UfficioSqlDAO(lConn);

			lTitoloSqlDao.start();
			while (lTitoloSqlDao.next()) {
				TitoloCumulatoModel lTitolo = (TitoloCumulatoModel) lTitoloSqlDao.getModel();

				if (lTitolo.getFlagEscluso() != null && lTitolo.getFlagEscluso().compareTo("S") != 0) {
					lSoggCumSqlDao.ricercaSoggettoCumulatoByIdTitolo(lTitolo.getIdTitoloCumulato());
					SoggettoCumulatoModel lSoggModel = (SoggettoCumulatoModel) lSoggCumSqlDao.getModelByKey();
					lTitolo.setSoggettoCumulato(lSoggModel);
					lSoggCumSqlDao.stop();

					// Recupero i dati del procedimento cumulato se presente e dell'eventuale
					// Ufficio Origine
					lProcCumSqlDao.ricercaProcedimentoCumulatoByIdTitolo(lTitolo.getIdTitoloCumulato());
					ProcedimentoCumulatoModel lProcCumModel = (ProcedimentoCumulatoModel) lProcCumSqlDao
							.getModelByKey();
					lProcCumSqlDao.stop();

					// Recupero i dati dell'ufficio origine se accorpato
					if (lProcCumModel != null && "S".equals(lProcCumModel.getFlagAccorpato())) {
						lUffSqlDao.selUfficioByCod(lProcCumModel.getChiaveUfficioOrigine());
						UfficioModel lUffModel = (UfficioModel) lUffSqlDao.getModelByKey();
						lUffSqlDao.stop();
						lProcCumModel.setUfficioOrigine(lUffModel);
					}
					lTitolo.setProcedimentoCumulato(lProcCumModel);

					lTitoliInIstruttoria.add(lTitolo);
				}
			}
			lTitoloSqlDao.stop();
		} catch (DAOException daoEx) {
			siesLogger.error("DAOException: ", daoEx);
			throw new F3BException(
					"IstruttoriaCumuloController.ExRicercaTitoliValidiByIstruttoriaOrderBy: Non posso leggere : "
							+ daoEx);
		} catch (Exception ex) {
			siesLogger.error("Exception: ", ex);
			throw new F3BException(
					"IstruttoriaCumuloController.ExRicercaTitoliValidiByIstruttoriaOrderBy: Non posso leggere : "
							+ ex);
		} finally {
			cleanup(lTitoloSqlDao);
			cleanup(lSoggCumSqlDao);
			cleanup(lProcCumSqlDao);
			cleanup(lUffSqlDao);

			cleanup(lConn);
		}

		return lTitoliInIstruttoria;
	}

	/**
	 * Stampa un documento di Prospetto Titoli Cumulati in Istruttoria
	 *
	 * @param aIstruttoriaCumulo
	 * @param aListaTitoli
	 * @return ByteArrayOutputStream
	 * @throws F3BException
	 */
	public ByteArrayOutputStream ExStampaProspettoTitoliCumulo(IstruttoriaCumuloModel aIstruttoriaCumulo,
			Vector aListaTitoli, FascicoloSiepModel lFascicoloModel, UtenteModel aUtenteMod,
			UfficioModel lUfficioMod, String aIdTemplate) throws F3BException {

		Connection lConn = null;
		ByteArrayOutputStream lByteArrayOut = null;

		try {
			String lNomeTemplate = TemplateManager.getInstance().getTemplateName(aIdTemplate);

			Date aDataEmissione = null;
			IStampaCumulo lStampaC = SIEPLookupRemote.getStampaCumuloRemote();
			TreeModel lTree = lStampaC.prelevaDatiIstruttoriaCumulo(lFascicoloModel, aUtenteMod, lUfficioMod,
					aIstruttoriaCumulo, aListaTitoli, aDataEmissione);

			siesLogger.info("NOME TEMPLATE >>>" + lNomeTemplate);

			ReportGenerator lReport = new ReportGenerator();
			lByteArrayOut = (ByteArrayOutputStream) lReport.generateDocument(lTree, lNomeTemplate);

			// La parte seguente per ora non serve
			/*
			 * ByteArrayInputStream lByteArrayInput = new ByteArrayInputStream(lByteArrayOut.toByteArray());
			 * CertificatoStatoEsecModel aCertSE = new CertificatoStatoEsecModel();
			 * aCertSE.setFlagUpload("0"); aCertSE.setCodOperatoreInserimento(aUtente.getUserId());
			 * aCertSE.setCodUfficioInserimento(aUtente.getUfficioUtente().getCodUfficio());
			 * aCertSE.setFasSieIdFascicoloSiep(aFasc.getIdFascicoloSiep());
			 * aCertSE.setDataInserimento(DateUtils.getSysDate()); aCertSE.setDocBlobIn(lByteArrayInput);
			 *
			 * //inserisco il record nella tabella lConn = getDBConnection(); lCertSEDao = new
			 * CertificatoStatoEsecDAO(lConn);
			 *
			 * lCertSEDao.setDAOFromModel(aCertSE );
			 */
		} catch (Exception e) {
			rollback(lConn);
			e.printStackTrace();
			siesLogger.debug("IstruttoriaCumuloController - -------> Exception: " + e, e);
			throw new F3BException("IstruttoriaCumuloController.ExStampaProspettoTitoli: " + e);
		} finally {
			cleanup(lConn);
		}

		return lByteArrayOut;
	}

	/**
	 * Stampa un documento di Prospetto Proposta provvedimenti da inserire nel Cumulo
	 *
	 * @param aIstruttoriaCumulo
	 * @param aListaTitoli
	 * @return ByteArrayOutputStream
	 * @throws F3BException
	 */
	public ByteArrayOutputStream ExStampaProspettoPropostaCumulo(IstruttoriaCumuloModel aIstruttoriaCumulo,
			Vector aListaTitoli, FascicoloSiepModel lFascicoloModel, UtenteModel aUtenteMod,
			UfficioModel lUfficioMod, String aIdTemplate) throws F3BException {

		Connection lConn = null;
		ByteArrayOutputStream lByteArrayOut = null;

		try {
			String lNomeTemplate = TemplateManager.getInstance().getTemplateName(aIdTemplate);

			IStampaCumulo lStampaC = SIEPLookupRemote.getStampaCumuloRemote();
			TreeModel lTree = lStampaC.prelevaDatiIstruttoriaPerPropostaCumulo(lFascicoloModel, aUtenteMod,
					lUfficioMod, aIstruttoriaCumulo, aListaTitoli, "Proposta");

			siesLogger.info("NOME TEMPLATE >>>" + lNomeTemplate);

			ReportGenerator lReport = new ReportGenerator();
			lByteArrayOut = (ByteArrayOutputStream) lReport.generateDocument(lTree, lNomeTemplate);

			// La parte seguente per ora non serve
			/*
			 * ByteArrayInputStream lByteArrayInput = new ByteArrayInputStream(lByteArrayOut.toByteArray());
			 * CertificatoStatoEsecModel aCertSE = new CertificatoStatoEsecModel();
			 * aCertSE.setFlagUpload("0"); aCertSE.setCodOperatoreInserimento(aUtente.getUserId());
			 * aCertSE.setCodUfficioInserimento(aUtente.getUfficioUtente().getCodUfficio());
			 * aCertSE.setFasSieIdFascicoloSiep(aFasc.getIdFascicoloSiep());
			 * aCertSE.setDataInserimento(DateUtils.getSysDate()); aCertSE.setDocBlobIn(lByteArrayInput);
			 *
			 * //inserisco il record nella tabella lConn = getDBConnection(); lCertSEDao = new
			 * CertificatoStatoEsecDAO(lConn);
			 *
			 * lCertSEDao.setDAOFromModel(aCertSE );
			 */
		} catch (Exception e) {
			rollback(lConn);
			e.printStackTrace();
			siesLogger.debug("IstruttoriaCumuloController - -------> Exception: " + e, e);
			throw new F3BException("IstruttoriaCumuloController.ExStampaProspettoPropostaCumulo: " + e);
		} finally {
			cleanup(lConn);
		}

		return lByteArrayOut;
	}

	/**
	 * Metodo che recupera tutti i dati dell'Istruttoria Cumulo al fine della trasmissione su altre BDI
	 *
	 * @param
	 * @param
	 * @since MEV 42 CUMULO Step2
	 */
	public IstruttoriaCumuloModel ExCercaIsruttoriaPerAltriDatiCumulo(BigDecimal aIdFascicoloSiep,
			BigDecimal aIdEvento, Connection aConn) throws F3BException {

		siesLogger.debug("--XX-- inizio CercaIstruttoriaperDatiCumuloNew - IdFasc = " + aIdFascicoloSiep
				+ " - IdEve = " + aIdEvento);
		BigDecimal IdIstruttoria = null;
		IstruttoriaCumuloSqlDAO IstruCumSqldao = null;
		IstruttoriaCumuloModel IstruCumMod = null;

		// ===============================================================
		// Oggetti Legati prevalentemente all'Istruttoria
		// ===============================================================
		// BigDecimal IdDatiFinali = null;
		DatiFinaliCumuloSqlDAO DatiFinaCumSqlDao = null;
		DatiFinaliCumuloModel DatiFinCumMod = null;

		PosizioneGiuridicaCumuloSqlDAO PosGiuCumSqlDao = null;
		PosizioneGiuridicaCumuloModel PosGiuCumMod = null;

		PenaRideterminataCumuloSqlDAO PenaRiCumSqlDao = null;
		Vector<PenaRideterminataCumuloModel> VecPenaRiCum = null;

		DatiFinaliUlterioriSanzioniSqlDAO DatiFinaUltSanSqlDao = null;
		Vector<DatiFinaliUlterioriSanzioniModel> VecDatFinUltSan = null;

		ComputiCumuloSqlDAO CompCumSqlDao = null;
		Vector<ComputiCumuloModel> VecCompCum = null;

		BigDecimal IdTitolo = null;
		TitoloCumulatoSqlDAO TitoCumSqlDao = null;
		Vector<TitoloCumulatoModel> VecTitoli = new Vector();

		// ==========================================================================================
		// Oggetti Richieste_PM ( sia al G.E che alla SORV.) Legati prevalentemente all'Istruttoria
		// ==========================================================================================
		RichiesteInviateCumSqlDAO RichInvSqlDao = null;

		RichiestePmInCumuloSqlDAO RichPmSqlDao = null;
		Vector<RichiestePmInCumuloModel> VecRichPmInCum = null;

		// ===============================================================
		// Oggetti Legati prevalentemente al Titolo_Cumulato
		// ===============================================================
		SoggettoCumulatoModel SoggCumMod = null;
		SoggettoCumulatoSqlDAO SoggCumSqlDao = null;

		ProcedimentoCumulatoModel ProcCumMod = null;
		ProcedimentoCumulatoSqlDAO ProcCumSqlDao = null;

		PenaComplessivaCumuloModel PenaCompCumMod = null;
		PenaComplessivaCumuloSqlDAO PenaCompCumSqlDao = null;

		SanzioneSostitutivaCumuloModel SanSosCumMod = null;
		SanzioneSostitutivaCumuloSqlDAO SanSosCumSqlDao = null;

		ContinuazioneCumuloSqlDAO ContinuaCumSqlDao = null;
		Vector<ContinuazioneCumuloModel> VecContinuaCum = null;

		ReatoCumuloSqlDAO ReatoCumuSqlDao = null;
		Vector<ReatoCumuloModel> VecReatiCum = null;

		CircostanzaCumuloSqlDAO CircoCumSqlDao = null;
		Vector<CircostanzaCumuloModel> VecCircoCum = null;

		MisuraSicurezzaCumuloModel MisSicCumuloMod = null;
		MisuraSicurezzaCumuloSqlDAO MisSicCumSqlDao = null;
		Vector<MisuraSicurezzaCumuloModel> VecMisSicCum = null;

		MisuraCautelareCumuloSqlDAO MisCauCumSqlDao = null;
		Vector<MisuraCautelareCumuloModel> VecMisCauCum = null;

		PenaAccessoriaCumuloModel PenaAccCumMod = null;
		PenaAccessoriaCumuloSqlDAO PenaAccCumSqlDao = null;
		Vector<PenaAccessoriaCumuloModel> VecPenaAccCum = null;

		BeneficioCumuloModel BeneCumMod = null;
		BeneficioCumuloSqlDAO BeneCumSqlDao = null;
		Vector<BeneficioCumuloModel> VecBeneCum = null;

		StatoEsecTitoloCumulatoSqlDAO StEsecCumSqlDao = null;
		Vector<StatoEsecTitoloCumulatoModel> VecStEsecCum = null;

		NotificaCumuloSqlDAO NotCumSqlDao = null;
		Vector<NotificaCumuloModel> VecNotCum = null;

		LibAnticipataCumuloSqlDAO LibAntCumSqldao = null;
		Vector<LibAnticipataCumuloModel> VecLibAntCum = null;

		PeriodoLibAntCumuloSqlDAO PeriodoSqldao = null;
		Vector<PeriodoLibAntCumuloModel> VecPeriodo = null;

		try {
			IstruCumSqldao = new IstruttoriaCumuloSqlDAO(aConn);
			IstruCumSqldao.RicercaIstruttoriaCumuloByIdFascicoloSiepIdEvento(aIdFascicoloSiep, aIdEvento);

			IstruCumMod = (IstruttoriaCumuloModel) IstruCumSqldao.getModelByKey();
			if (IstruCumMod != null && IstruCumMod.getIdIstruttoriaCumulo() != null) {
				IdIstruttoria = IstruCumMod.getIdIstruttoriaCumulo();
				siesLogger.debug("--XX-- Istruttoria = " + IdIstruttoria);

				// Dati_Finali_Cumulo
				DatiFinaCumSqlDao = new DatiFinaliCumuloSqlDAO(aConn);
				DatiFinaCumSqlDao.ricercaDatiFinaliCumuloByIdIstruttoria(IdIstruttoria);
				DatiFinCumMod = (DatiFinaliCumuloModel) DatiFinaCumSqlDao.getModelByKey();

				if (DatiFinCumMod != null && DatiFinCumMod.getIdDatiFinaliCumulo() != null) {
					// IdDatiFinali = DatiFinCumMod.getIdDatiFinaliCumulo();
					IstruCumMod.setDatiFinaliCumulo(DatiFinCumMod);
				}

				// Posizione_Giuridica_Cumulo
				PosGiuCumSqlDao = new PosizioneGiuridicaCumuloSqlDAO(aConn);
				PosGiuCumSqlDao.ricercaPosizioneGiuridicaCumuloByIdIstruttoria(IdIstruttoria);
				PosGiuCumMod = (PosizioneGiuridicaCumuloModel) PosGiuCumSqlDao.getModelByKey();

				if (PosGiuCumMod != null && PosGiuCumMod.getIdPosizioneGiuridicaCum() != null) {
					IstruCumMod.setPosizioneGiuridicaCumulo(PosGiuCumMod);
				}

				// Pena_Rideterminata_Cumulo
				PenaRiCumSqlDao = new PenaRideterminataCumuloSqlDAO(aConn);
				// PenaRiCumSqlDao.ricercaPenaRideterminataCumulByIdDatiFinali(IdDatiFinali); ///
				// <<-------------------------
				PenaRiCumSqlDao.ricercaPenaRideterminataCumulByIdIstruttoria(IdIstruttoria); /// <<-------------------------
				VecPenaRiCum = new Vector<PenaRideterminataCumuloModel>(PenaRiCumSqlDao.getModels());

				if (VecPenaRiCum != null && VecPenaRiCum.size() > 0) {
					IstruCumMod.setPenaRideterminataCumulo(VecPenaRiCum);
				}

				// Dati_Finali_Ulteriori_Sanzioni Cumulo
				DatiFinaUltSanSqlDao = new DatiFinaliUlterioriSanzioniSqlDAO(aConn);
				// DatiFinaUltSanSqlDao.ricercaDatiFinaliUlterioriSanzioniByIdDatiFinali(IdDatiFinali); ///
				// <<-------------------------
				DatiFinaUltSanSqlDao.ricercaDatiFinaliUlterioriSanzioniByIdIstruttoria(IdIstruttoria); /// <<--------------------------
				VecDatFinUltSan = new Vector<DatiFinaliUlterioriSanzioniModel>(
						DatiFinaUltSanSqlDao.getModels());

				if (VecDatFinUltSan != null && VecDatFinUltSan.size() > 0) {
					IstruCumMod.setDatiFinaliUlterioriSanzioni(VecDatFinUltSan);
				}

				// Computi_Cumulo
				CompCumSqlDao = new ComputiCumuloSqlDAO(aConn);
				CompCumSqlDao.ricercaComputiCumuloByIdIstruttoria(IdIstruttoria);
				VecCompCum = new Vector<ComputiCumuloModel>(CompCumSqlDao.getModels());

				if (VecCompCum != null && VecCompCum.size() > 0) {
					IstruCumMod.setComputiCumulo(VecCompCum);
				}

				// -------------------------------------> Caricamento delle RICHIESTE P.M. (Effettuate ed
				// Inviate) <-------------------------------------//
				// ================================================================================================================================================//
				RichInvSqlDao = new RichiesteInviateCumSqlDAO(aConn);
				RichPmSqlDao = new RichiestePmInCumuloSqlDAO(aConn);

				// RICHIESTE_INVIATE_CUMULO
				siesLogger.debug("--XX-- Ricerca delle Richieste del PM (Richieste_Inviate_Cum) ");
				RichInvSqlDao.ricercaRichiesteInviateCumByIdIstruttoria(IdIstruttoria);
				Vector<RichiesteInviateCumModel> VecRichInviate = new Vector<>();

				RichInvSqlDao.start();
				while (RichInvSqlDao.next()) {
					RichiesteInviateCumModel lRicInvMod = (RichiesteInviateCumModel) RichInvSqlDao.getModel();
					if (lRicInvMod != null && lRicInvMod.getIdRichiesteInviateCum() != null) {
						VecRichInviate.add(lRicInvMod);

						// RICHIESTE_PM_IN_CUMULO (legate alle Richieste Inviate)
						siesLogger.debug(
								"--XX-- Ricerca delle Richieste del PM (Richieste_PM_In_Cumulo By Id_Richiesta Inviata)");
						RichPmSqlDao.ricercaRichiestePmInCumuloByIdRichInviateCum(
								lRicInvMod.getIdRichiesteInviateCum());
						VecRichPmInCum = new Vector<RichiestePmInCumuloModel>(RichPmSqlDao.getModels());

						VecRichPmInCum = ScaricaDecisioneeAltriDatiRichiestePM(aConn, VecRichPmInCum);

						if (VecRichPmInCum != null && VecRichPmInCum.size() > 0) {
							// siesLogger.debug("--XX-----------> Totate RICHIESTE_PM_IN_CUMULO (effettuate) =
							// "+VecRichPmInCum.size());
							lRicInvMod.setListaRichiestePMinCumulo(VecRichPmInCum);
						}
					}

				}

				if (VecRichInviate != null && VecRichInviate.size() > 0) {
					// siesLogger.debug("--XX-----------> Totate RICHIESTE_INVIATE_CUM =
					// "+VecRichInviate.size());
					IstruCumMod.setRichiesteInviate(VecRichInviate);
				}

				RichInvSqlDao.stop();

				// --- RICHIESTE_PM_IN_CUMULO (NON ancora Inviate)
				// ===================================================================================================
				RichPmSqlDao.stop();
				Vector<RichiestePmInCumuloModel> VecRichiesteAperte = null;
				siesLogger.debug(
						"--XX-- Ricerca delle Richieste del PM (Richieste_PM_In_Cumulo By Id_Istruttoria) - Richieste da Inviare) ");
				RichPmSqlDao.ricercaRichiestePmInCumuloDaInviareByIdIstruttoria(IdIstruttoria);
				VecRichiesteAperte = new Vector<RichiestePmInCumuloModel>(RichPmSqlDao.getModels());

				VecRichiesteAperte = ScaricaDecisioneeAltriDatiRichiestePM(aConn, VecRichiesteAperte);
				if (VecRichiesteAperte != null && VecRichiesteAperte.size() > 0) {
					// siesLogger.debug("--XX---------------> Totate RICHIESTE_PM_IN_CUMULO (da INVIARE) =
					// "+VecRichiesteAperte.size());
					IstruCumMod.setListaRichiestePmInCumulo(VecRichiesteAperte);
				}

				// ---
				// --- Caricamento dei Titoli Cumulati legati All'Istruttoria
				// --------------------------------------
				// ===================================================================================================
				TitoCumSqlDao = new TitoloCumulatoSqlDAO(aConn);
				TitoCumSqlDao.ricercaTitoloCumulatoByIstruttoria(IdIstruttoria);

				TitoCumSqlDao.start();
				while (TitoCumSqlDao.next()) {
					TitoloCumulatoModel lTitolo = (TitoloCumulatoModel) TitoCumSqlDao.getModel();

					if (lTitolo != null && lTitolo.getIdTitoloCumulato() != null) {
						IdTitolo = lTitolo.getIdTitoloCumulato();
						siesLogger.debug("--XX-- Trovato titolo = " + IdTitolo);

						// Soggetto_Cumulato
						SoggCumSqlDao = new SoggettoCumulatoSqlDAO(aConn);
						SoggCumSqlDao.ricercaSoggettoCumulatoByIdTitolo(IdTitolo);
						SoggCumMod = (SoggettoCumulatoModel) SoggCumSqlDao.getModelByKey();

						if (SoggCumMod != null && SoggCumMod.getIdSoggettoCumulato() != null) {
							lTitolo.setSoggettoCumulato(SoggCumMod);
						}

						// Procedimento_Cumulato
						ProcCumSqlDao = new ProcedimentoCumulatoSqlDAO(aConn);
						ProcCumSqlDao.ricercaProcedimentoCumulatoByIdTitolo(IdTitolo);
						ProcCumMod = (ProcedimentoCumulatoModel) ProcCumSqlDao.getModelByKey();

						if (ProcCumMod != null && ProcCumMod.getIdProcedimentoCumulato() != null) {
							lTitolo.setProcedimentoCumulato(ProcCumMod);
						}

						// Pena_Complessiva cumulo
						PenaCompCumSqlDao = new PenaComplessivaCumuloSqlDAO(aConn);
						PenaCompCumSqlDao.ricercaPenaComplessivaCumuloByIdTitolo(IdTitolo);
						PenaCompCumMod = (PenaComplessivaCumuloModel) PenaCompCumSqlDao.getModelByKey();

						if (PenaCompCumMod != null && PenaCompCumMod.getIdPenaComplessivaCum() != null) {
							// Sanzione_Sostitutiva Cumulo
							SanSosCumSqlDao = new SanzioneSostitutivaCumuloSqlDAO(aConn);
							SanSosCumSqlDao.ricercaSanzioneSostitutivaByIdPenaComplessivaCum(
									PenaCompCumMod.getIdPenaComplessivaCum()); /// <<---------
							SanSosCumMod = (SanzioneSostitutivaCumuloModel) SanSosCumSqlDao.getModelByKey();

							if (SanSosCumMod != null && SanSosCumMod.getIdSanzioneSostitutivaCum() != null) {
								PenaCompCumMod.setSanzioneSostitutivaCumulo(SanSosCumMod);
							}

							// Continuazioni_Titoli in Sentenza Cumulo
							ContinuaCumSqlDao = new ContinuazioneCumuloSqlDAO(aConn);
							ContinuaCumSqlDao.ricercaContinuazioneByIdPenaComplessivaCum(
									PenaCompCumMod.getIdPenaComplessivaCum()); /// <<---------
							// ContinuaCumSqlDao.ricercaContinuazioneByIdTitolo(IdTitolo); /// <<---------
							VecContinuaCum = new Vector<ContinuazioneCumuloModel>(
									ContinuaCumSqlDao.getModels());

							if (VecContinuaCum != null && VecContinuaCum.size() > 0) {
								PenaCompCumMod.setContinuazioniCumulo(VecContinuaCum);
							}

							lTitolo.setPenaComplessivaCumulo(PenaCompCumMod);

						}

						// Reati_Cumulo
						ReatoCumuSqlDao = new ReatoCumuloSqlDAO(aConn);
						ReatoCumuSqlDao.ricercaReatiCumuloByIdTitolo(IdTitolo);
						VecReatiCum = new Vector<ReatoCumuloModel>(ReatoCumuSqlDao.getModels());

						if (VecReatiCum != null && VecReatiCum.size() > 0) {
							lTitolo.setReatiCumulo(VecReatiCum);
						}

						// Circostanze Attenuanti/Aggravanti Soggettive Cumulo
						CircoCumSqlDao = new CircostanzaCumuloSqlDAO(aConn);
						CircoCumSqlDao.ricercaCircostanzeCumuloByTitolo(IdTitolo);
						VecCircoCum = new Vector<CircostanzaCumuloModel>(CircoCumSqlDao.getModels());

						if (VecCircoCum != null && VecCircoCum.size() > 0) {
							lTitolo.setCircostanzeCumulo(VecCircoCum);
						}

						// Misure_Sicurezza Cumulo
						MisSicCumSqlDao = new MisuraSicurezzaCumuloSqlDAO(aConn);
						MisSicCumuloMod = new MisuraSicurezzaCumuloModel();
						MisSicCumuloMod.setTitIdTitoloCumulato(IdTitolo);
						MisSicCumSqlDao.ricercaMisuraSicurezzaCumulo(MisSicCumuloMod); /// Non esiste ricerca
																						/// per Titolo
						VecMisSicCum = new Vector<MisuraSicurezzaCumuloModel>(MisSicCumSqlDao.getModels());

						if (VecMisSicCum != null && VecMisSicCum.size() > 0) {
							lTitolo.setMisureSicurezzaCumulo(VecMisSicCum);
						}

						// pene Accessorie Cumulo
						PenaAccCumSqlDao = new PenaAccessoriaCumuloSqlDAO(aConn);
						PenaAccCumMod = new PenaAccessoriaCumuloModel();
						PenaAccCumMod.setTitIdTitoloCumulato(IdTitolo);
						PenaAccCumSqlDao.ricercaPenaAccessoriaCumulo(PenaAccCumMod); /// Non esiste ricerca
																						/// per Titolo
						VecPenaAccCum = new Vector<PenaAccessoriaCumuloModel>(PenaAccCumSqlDao.getModels());

						if (VecPenaAccCum != null && VecPenaAccCum.size() > 0) {
							lTitolo.setPeneAccessorieCumulo(VecPenaAccCum);
						}

						// Misure Cautelari Cumulo
						MisCauCumSqlDao = new MisuraCautelareCumuloSqlDAO(aConn);
						MisCauCumSqlDao.ricercaMisuraCautelareCumuloByIdTitolo(IdTitolo);
						VecMisCauCum = new Vector<MisuraCautelareCumuloModel>(MisCauCumSqlDao.getModels());

						if (VecMisCauCum != null && VecMisCauCum.size() > 0) {
							lTitolo.setMisureCautelariCumulo(VecMisCauCum);
						}

						// Benefici Cumulo
						BeneCumSqlDao = new BeneficioCumuloSqlDAO(aConn);
						BeneCumMod = new BeneficioCumuloModel();
						BeneCumMod.setTitIdTitoloCumulato(IdTitolo);
						BeneCumSqlDao.ricercaBeneficioCumulo(BeneCumMod); /// Non esiste ricerca per Titolo
						VecBeneCum = new Vector<BeneficioCumuloModel>(BeneCumSqlDao.getModels());

						if (VecBeneCum != null && VecBeneCum.size() > 0) {
							lTitolo.setBeneficiCumulo(VecBeneCum);
						}

						// Stato Esecuzione Titolo_Cumulato e Notifiche_Cumulo
						StEsecCumSqlDao = new StatoEsecTitoloCumulatoSqlDAO(aConn);
						StEsecCumSqlDao.ricercaStatoEsecTitoloCumulatoByIdTitolo(IdTitolo, null);
						VecStEsecCum = new Vector<StatoEsecTitoloCumulatoModel>(StEsecCumSqlDao.getModels());

						if (VecStEsecCum != null && VecStEsecCum.size() > 0) {
							NotCumSqlDao = new NotificaCumuloSqlDAO(aConn);
							LibAntCumSqldao = new LibAnticipataCumuloSqlDAO(aConn);
							PeriodoSqldao = new PeriodoLibAntCumuloSqlDAO(aConn);

							Iterator ItrST = VecStEsecCum.iterator();
							while (ItrST.hasNext()) {
								StatoEsecTitoloCumulatoModel lStatMod = (StatoEsecTitoloCumulatoModel) ItrST
										.next();
								if (lStatMod != null && lStatMod.getIdStatoEsecTitoloCumulato() != null) {
									// Notifiche_Cumulo
									NotCumSqlDao.ricercaNotificheCumuloByIdStatoEsec(
											lStatMod.getIdStatoEsecTitoloCumulato());
									VecNotCum = new Vector<NotificaCumuloModel>(NotCumSqlDao.getModels());

									if (VecNotCum != null && VecNotCum.size() > 0)
										lStatMod.setListaNotifiche(VecNotCum);

									// Lib_Anticipata_Cumulo
									LibAntCumSqldao.ricercaLibAnticipataCumuloByIdStatoEsec(
											lStatMod.getIdStatoEsecTitoloCumulato());
									VecLibAntCum = new Vector<LibAnticipataCumuloModel>(
											LibAntCumSqldao.getModels());

									if (VecLibAntCum != null && VecLibAntCum.size() > 0) {
										lStatMod.setListaLiberazioniAnticipate(VecLibAntCum);

										Iterator ItrLA = VecLibAntCum.iterator();
										while (ItrLA.hasNext()) {
											LibAnticipataCumuloModel lLibAntCumModel = (LibAnticipataCumuloModel) ItrLA
													.next();
											if (lLibAntCumModel != null
													&& lLibAntCumModel.getIdLibAnticipataCumulo() != null) {
												// Periodo_Lib_Anticipata
												PeriodoSqldao.ricercaPeriodoLibAntCumuloByLibIdLibAntCum(
														lLibAntCumModel.getIdLibAnticipataCumulo());
												VecPeriodo = new Vector<PeriodoLibAntCumuloModel>(
														PeriodoSqldao.getModels());

												if (VecPeriodo != null && VecPeriodo.size() > 0)
													lLibAntCumModel.setListaPeriodiLibAnticipate(VecPeriodo);
											}
										}
									}
								}
							}

							lTitolo.setStatoEsecuzioneTitoloCumulato(VecStEsecCum); // Da Controllare -
																					// mettere log con
																					// iterator per vedere se
																					// è OK
						}

						// ---
						// Aggiungo il TITOLO_CUMULATO alla lista Titoli legati All'Istruttoria
						VecTitoli.add(lTitolo);
					}
				}

				TitoCumSqlDao.stop();

				// Aggiungo la lista Titoli all'ISTRUTTORIA
				if (VecTitoli != null && VecTitoli.size() > 0)
					IstruCumMod.setTitoliCumulati(VecTitoli);

			} // CHIUDE if(IstruCumMod!=null && IstruCumMod.getIdIstruttoriaCumulo()!=null)

		} catch (Exception e) {
			e.printStackTrace();
			siesLogger.error(
					"IstruttoriaCumuloController.ExCercaIsruttoriaPerAltriDatiCumulo - -------> Exception: "
							+ e,
					e);
			throw new F3BException("IstruttoriaCumuloController.ExCercaIsruttoriaPerAltriDatiCumulo: " + e);
		} finally {
			cleanup(IstruCumSqldao);
			cleanup(DatiFinaCumSqlDao);
			cleanup(PosGiuCumSqlDao);
			cleanup(PenaRiCumSqlDao);
			cleanup(DatiFinaUltSanSqlDao);
			cleanup(CompCumSqlDao);
			cleanup(TitoCumSqlDao);
			// Scheda Intervento n° 6 - Ottimizzazione SIUS Avvocati
			cleanup(RichInvSqlDao);
			cleanup(RichPmSqlDao);
			cleanup(SoggCumSqlDao);
			cleanup(ProcCumSqlDao);
			cleanup(PenaCompCumSqlDao);
			cleanup(SanSosCumSqlDao);
			cleanup(ContinuaCumSqlDao);
			cleanup(ReatoCumuSqlDao);
			cleanup(CircoCumSqlDao);
			cleanup(MisSicCumSqlDao);
			cleanup(MisCauCumSqlDao);
			cleanup(PenaAccCumSqlDao);
			cleanup(BeneCumSqlDao);
			cleanup(StEsecCumSqlDao);
			cleanup(NotCumSqlDao);
			// Scheda Intervento n° 6 - Ottimizzazione SIUS Avvocati
			cleanup(LibAntCumSqldao);
			cleanup(PeriodoSqldao);
		}

		return IstruCumMod;
	} // CHIUDE ExCercaIsruttoriaPerAltriDatiCumulo

	/**
	 *
	 * @param lConn
	 * @param VecRichPmInCum
	 * @return
	 * @throws F3BException
	 */
	public Vector<RichiestePmInCumuloModel> ScaricaDecisioneeAltriDatiRichiestePM(Connection lConn,
			Vector<RichiestePmInCumuloModel> VecRichPmInCum) throws F3BException {

		siesLogger.debug("--XX-- - ScaricaDecisioneeAltriDatiRichiestePM - Inizio ");

		RichiestePmInCumuloModel RichPmModel = new RichiestePmInCumuloModel();

		ProvvedimentoGeSorvCumSqlDAO ProvvCumSqlDao = null;
		ProvvedimentoGeSorvCumModel ProvvCumMod = null;

		// Entità di Relazione
		RichPMTitoloCumSqlDAO RicPmTitCumSqlDao = null;
		RichPMBeneficioCumSqlDAO RicPmBeneCumSqlDao = null;
		RichPMMisSicCumSqlDAO RicPmMisSicCumSqlDao = null;
		RichPMPenAccCumSqlDAO RicPmPenAccCumSqlDao = null;
		RichPMReatoCumSqlDAO RicPmReaCumSqlDao = null;
		RichPMSanzioneSostCumSqlDAO RicPmSSCumSqlDao = null;
		RichPMStatoEsecCumSqlDAO RicPmStatEsecCumSqlDao = null;
		try {

			ProvvCumSqlDao = new ProvvedimentoGeSorvCumSqlDAO(lConn);
			RicPmTitCumSqlDao = new RichPMTitoloCumSqlDAO(lConn);
			RicPmBeneCumSqlDao = new RichPMBeneficioCumSqlDAO(lConn);
			RicPmMisSicCumSqlDao = new RichPMMisSicCumSqlDAO(lConn);
			RicPmPenAccCumSqlDao = new RichPMPenAccCumSqlDAO(lConn);
			RicPmReaCumSqlDao = new RichPMReatoCumSqlDAO(lConn);
			RicPmSSCumSqlDao = new RichPMSanzioneSostCumSqlDAO(lConn);
			RicPmStatEsecCumSqlDao = new RichPMStatoEsecCumSqlDAO(lConn);

			Iterator itxR = VecRichPmInCum.iterator();
			while (itxR.hasNext()) {
				RichPmModel = (RichiestePmInCumuloModel) (itxR.next());

				if (RichPmModel != null && RichPmModel.getIdRichiestePmInCumulo() != null) {
					// siesLogger.debug("--XX-- Ciclo su Vector Richieste di Input - id =
					// "+RichPmModel.getIdRichiestePmInCumulo());

					// PROVVEDIMENTO_GE_SORV-CUM
					ProvvCumSqlDao.ricercaProvvedimentoGeSorvCumByIdRichiesta(
							RichPmModel.getIdRichiestePmInCumulo());
					ProvvCumSqlDao.start();
					if (ProvvCumSqlDao.next()) {
						ProvvCumMod = (ProvvedimentoGeSorvCumModel) ProvvCumSqlDao.getModel();
						if (ProvvCumMod != null && ProvvCumMod.getIdProvvedimentoGeSorvCum() != null) {
							RichPmModel.setDecisioneGeSorvCum(ProvvCumMod);
						}
					}
					ProvvCumSqlDao.stop();

					// RICHPM_TITOLO_CUM
					RicPmTitCumSqlDao
							.ricercaRichPmTitoloCumByRichIdRich(RichPmModel.getIdRichiestePmInCumulo());
					Vector<RichPMTitoloCumModel> VecRichPmTitoCum = new Vector<RichPMTitoloCumModel>(
							RicPmTitCumSqlDao.getModels());
					if (VecRichPmTitoCum != null && VecRichPmTitoCum.size() > 0) {
						RichPmModel.setListaRichPmTitoloCum(VecRichPmTitoCum);
					}

					// RICHPM_BENEFICIO_CUM =====================
					RicPmBeneCumSqlDao
							.ricercaRichPmBeneficioCumByRichIdRich(RichPmModel.getIdRichiestePmInCumulo());
					Vector<RichPMBeneficioCumModel> VecRicBeneCum = new Vector<RichPMBeneficioCumModel>(
							RicPmBeneCumSqlDao.getModels());
					if (VecRicBeneCum != null && VecRicBeneCum.size() > 0) {
						RichPmModel.setListaRichPmBeneficioCum(VecRicBeneCum);
					}

					// RICHPM_MISSICUR_CUM
					RicPmMisSicCumSqlDao
							.ricercaRichPmMisSicCumByRichIdRich(RichPmModel.getIdRichiestePmInCumulo());
					Vector<RichPMMisSicCumModel> VecRichPmMSCum = new Vector<RichPMMisSicCumModel>(
							RicPmMisSicCumSqlDao.getModels());
					if (VecRichPmMSCum != null && VecRichPmMSCum.size() > 0) {
						RichPmModel.setListaRichPmMisuraSicurezzaCum(VecRichPmMSCum);
					}

					// RICHPM_PENACC_CUM
					RicPmPenAccCumSqlDao
							.ricercaRichPmPenAccCumByRichIdRich(RichPmModel.getIdRichiestePmInCumulo());
					Vector<RichPMPenAccCumModel> VecRicPACum = new Vector<RichPMPenAccCumModel>(
							RicPmPenAccCumSqlDao.getModels());
					if (VecRicPACum != null && VecRicPACum.size() > 0) {
						RichPmModel.setListaRichPmPenaAccessoriaCum(VecRicPACum);
					}

					// RICHPM_REATO_CUM
					RicPmReaCumSqlDao
							.ricercaRichPmReatoCumByRichIdRich(RichPmModel.getIdRichiestePmInCumulo());
					Vector<RichPMReatoCumModel> VecRicReaCum = new Vector<RichPMReatoCumModel>(
							RicPmReaCumSqlDao.getModels());
					if (VecRicReaCum != null && VecRicReaCum.size() > 0) {
						RichPmModel.setListaRichPmReatoCum(VecRicReaCum);
					}

					// RICHPM_SANZIONE_SOST_CUM
					RicPmSSCumSqlDao.ricercaRichPmSSCumByRichIdRich(RichPmModel.getIdRichiestePmInCumulo());
					Vector<RichPMSanSostCumModel> VecRicSSCum = new Vector<RichPMSanSostCumModel>(
							RicPmSSCumSqlDao.getModels());
					if (VecRicSSCum != null && VecRicSSCum.size() > 0) {
						RichPmModel.setListaRichPmSanzioneSostCum(VecRicSSCum);
					}

					// RICHPM_STATO_ESEC_CUM
					RicPmStatEsecCumSqlDao
							.ricercaRichPmStatoEsecCumByRichIdRich(RichPmModel.getIdRichiestePmInCumulo());
					Vector<RichPMStatoEsecCumModel> VecRicStesecCum = new Vector<RichPMStatoEsecCumModel>(
							RicPmStatEsecCumSqlDao.getModels());
					if (VecRicStesecCum != null && VecRicStesecCum.size() > 0) {
						RichPmModel.setListaRichPmStatoEsecCum(VecRicStesecCum);
					}
				} // Chiude RichPmModel.getIdRichiestePmInCumulo()!=null
			} // Chiude ciclo while()
		} catch (Exception e) {
			// rollback(lConn);
			e.printStackTrace();
			siesLogger.error(
					"IstruttoriaCumuloController.ScaricaDecisioneeAltriDatiRichiestePM - -------> Exception: "
							+ e,
					e);
			throw new F3BException("IstruttoriaCumuloController.ScaricaDecisioneeAltriDatiRichiestePM: " + e);
		} finally {
			cleanup(ProvvCumSqlDao);
			cleanup(RicPmTitCumSqlDao);
			cleanup(RicPmBeneCumSqlDao);
			cleanup(RicPmMisSicCumSqlDao);
			cleanup(RicPmPenAccCumSqlDao);
			cleanup(RicPmReaCumSqlDao);
			cleanup(RicPmSSCumSqlDao);
			cleanup(RicPmStatEsecCumSqlDao);
		}

		return VecRichPmInCum;
	} // Chiude ScaricaDecisioneeAltriDatiRichiestePM

	/**
	 * La Insert viene fatta in modalità 'NO SEQUENCE', senza utilizzare le sequence, ma inserendo
	 * L'ISTRUTTORIA_CUMULO con il valore di ID_ISTRUTTORIA_CUMULO già preimpostato; Questo metodo è usato
	 * nella funzione di presa in carico, per scaricare tutti i dati del Fascicolo sulla nuova Base dati.
	 *
	 */
	public String ExInserisciIstruttoriaCumuloWithoutSequence(IstruttoriaCumuloModel aIstruttoriaCumModel,
			Connection lConn) throws F3BException {

		String EsitodiRitorno = "00000";
		IstruttoriaCumuloDAO lIstruCumDao = null;

		try {
			lIstruCumDao = new IstruttoriaCumuloDAO(lConn);

			if (aIstruttoriaCumModel != null && aIstruttoriaCumModel.getIdIstruttoriaCumulo() != null) {
				lIstruCumDao.setDAOFromModel(aIstruttoriaCumModel);
				lIstruCumDao.setWithoutSequence(true);
				lIstruCumDao.insert();
				lIstruCumDao.stop();
			}
		} catch (DAOException daoEx) {
			if (daoEx.UNIQUE_CONSTRAINT_VIOLATED) {
				siesLogger.warn("Istruttoria_Cumulo gia' presente...>"
						+ aIstruttoriaCumModel.getIdIstruttoriaCumulo() + "<");
				EsitodiRitorno = "00001";
			} else {
				EsitodiRitorno = "01400";
				throw new F3BException(F3BException.USER_MESSAGE,
						"Impossibile inserire Istruttoria_CUMULO! ");
			}
		} finally {
			cleanup(lIstruCumDao);
		}

		return EsitodiRitorno;
	} // Chiude ExInserisciIstruttoriaCumuloWithoutSequence()
		// END MEV 26 Step2

	// =========================================================================================================
	// solo per PROVA
	/**
	 * Prova di Stampa un documento che poi sarà la stampa Richiersta del PM al GE di Applicazione Benefici (
	 * Gestione Istruttoria Cumulo)
	 */
	public ByteArrayOutputStream ExStampaRichiestaDelPMCumulo(IstruttoriaCumuloModel aIstruttoriaCumulo,
			Vector aListaTitoli, FascicoloSiepModel lFascicoloModel, UtenteModel aUtenteMod,
			UfficioModel lUfficioMod, String aIdTemplate, RichiesteInviateCumModel lRichMod)
			throws F3BException {

		siesLogger.debug("--XX-- Start ExStampa");
		Connection lConn = null;
		ByteArrayOutputStream lByteArrayOut = null;

		RichiesteInviateCumDAO lRicDao = null;

		try {
			IStampaCumulo lStampaC = SIEPLookupRemote.getStampaCumuloRemote();
			TreeModel lTree = lStampaC.prelevaDatiIstruttoriaPerPropostaCumulo(lFascicoloModel, aUtenteMod,
					lUfficioMod, aIstruttoriaCumulo, aListaTitoli, "Richiesta");

			Connection aConn = null;
			lTree = lStampaC.prelevaDatiRichiestaInviataCumulo(aConn, lTree, aUtenteMod, lUfficioMod,
					aIstruttoriaCumulo, lRichMod);

			String lNomeTemplate = TemplateManager.getInstance().getTemplateName(aIdTemplate);
			siesLogger.info("NOME TEMPLATE >>>" + lNomeTemplate);

			ReportGenerator lReport = new ReportGenerator();
			lByteArrayOut = (ByteArrayOutputStream) lReport.generateDocument(lTree, lNomeTemplate);

			ByteArrayInputStream lByteArrayInput = new ByteArrayInputStream(lByteArrayOut.toByteArray());

			lConn = getDBConnection();
			lRicDao = new RichiesteInviateCumDAO(lConn);

			lRichMod.setDocBlobIn(lByteArrayInput);

			lRicDao.setDAOFromModelForUploadBlob(lRichMod);
			lRicDao.selCondizioneUpdate(lRichMod.getIdRichiesteInviateCum());
			lRicDao.update();

			commit(lConn);
		} catch (DAOException daoEx) {
			rollback(lConn);
			daoEx.printStackTrace();
			siesLogger.debug(" IstruttoriaCumuloController - ---------> DaoException" + daoEx, daoEx);
			throw new F3BException("IstruttoriaCumuloController.ExStampaRichiestaDelPMCumulo: " + daoEx);
		} finally {
			cleanup(lRicDao);
			cleanup(lConn);
		}

		return lByteArrayOut;
	}

	/**
	 * La Ricerca è la stessa di ExRicercaTitoliByIstruttoriaOrderBy con in più La join con Reato_Cumulo per
	 * trovare solo determinati Titoli che abbiano determinati Reati; I Reati sono presi a condizione di
	 * Reato_Cumulo.Data_Inizio <= Data Parametro;
	 */
	public Vector<TitoloCumulatoModel> ExRicercaTitoliByIstruttoriaDataReatoCumOrderBy(
			BigDecimal aIdIstruttoriaCumulo, String aOrdinamento, Date lDataRea) throws F3BException {

		Connection lConn = null;

		Vector<TitoloCumulatoModel> lTitoliInIstruttoria = new Vector<>();

		TitoloCumulatoSqlDAO lTitoloSqlDao = null;
		TitoloCumulatoModel lTitoloRicerca = new TitoloCumulatoModel();

		ProcedimentoCumulatoSqlDAO lProcCumSqlDao = null;
		SoggettoCumulatoSqlDAO lSoggCumSqlDao = null;
		UfficioSqlDAO lUffSqlDao = null;

		lTitoloRicerca.setIstrIdIstruttoriaCumulo(aIdIstruttoriaCumulo);

		try {
			lConn = getDBConnection();

			lTitoloSqlDao = new TitoloCumulatoSqlDAO(lConn);
			lProcCumSqlDao = new ProcedimentoCumulatoSqlDAO(lConn);
			lSoggCumSqlDao = new SoggettoCumulatoSqlDAO(lConn);
			lUffSqlDao = new UfficioSqlDAO(lConn);

			lTitoloSqlDao.ricercaTitoloCumulatoByIstruttoriaDataReatoCumOrderBy(aIdIstruttoriaCumulo,
					aOrdinamento, lDataRea);

			lTitoloSqlDao.start();

			while (lTitoloSqlDao.next()) {
				TitoloCumulatoModel lTitolo = (TitoloCumulatoModel) lTitoloSqlDao.getModel();
				lTitoliInIstruttoria.add(lTitolo);

				lProcCumSqlDao.ricercaProcedimentoCumulatoByIdTitolo(lTitolo.getIdTitoloCumulato());
				ProcedimentoCumulatoModel lProcModel = (ProcedimentoCumulatoModel) lProcCumSqlDao
						.getModelByKey();
				lTitolo.setProcedimentoCumulato(lProcModel);
				lProcCumSqlDao.stop();

				if (lProcModel != null && "S".equals(lProcModel.getFlagAccorpato())) {

					lUffSqlDao.selUfficioByCod(lProcModel.getChiaveUfficioOrigine());
					UfficioModel lUffMod = (UfficioModel) lUffSqlDao.getModelByKey();
					lUffSqlDao.stop();

					lProcModel.setUfficioOrigine(lUffMod);
				}

				lSoggCumSqlDao.ricercaSoggettoCumulatoByIdTitolo(lTitolo.getIdTitoloCumulato());
				SoggettoCumulatoModel lSoggModel = (SoggettoCumulatoModel) lSoggCumSqlDao.getModelByKey();
				lTitolo.setSoggettoCumulato(lSoggModel);
				lSoggCumSqlDao.stop();

			}
			lTitoloSqlDao.stop();
		} catch (DAOException daoEx) {
			siesLogger.error("DAOException: ", daoEx);
			throw new F3BException(
					"IstruttoriaCumuloController.ExRicercaTitoliByIstruttoriaDataReatoCumOrderBy: Non posso leggere : "
							+ daoEx);
		} catch (Exception ex) {
			siesLogger.error("Exception: ", ex);
			throw new F3BException(
					"IstruttoriaCumuloController.ExRicercaTitoliByIstruttoriaDataReatoCumOrderBy: Non posso leggere : "
							+ ex);
		} finally {
			cleanup(lTitoloSqlDao);
			cleanup(lProcCumSqlDao);
			cleanup(lSoggCumSqlDao);
			cleanup(lUffSqlDao);

			cleanup(lConn);
		}

		return lTitoliInIstruttoria;
	}

	public Vector<TitoloCumulatoModel> ExRicercaTitoliBeneficiCumByIstruttoriaOrderBy(
			BigDecimal aIdIstruttoriaCumulo, String aOrdinamento, String aCodNaturaBen,
			Vector<String> aCodTipiBen) throws F3BException {

		Connection lConn = null;

		Vector<TitoloCumulatoModel> lTitoliInIstruttoria = new Vector<>();

		TitoloCumulatoSqlDAO lTitoloSqlDao = null;
		TitoloCumulatoModel lTitoloRicerca = new TitoloCumulatoModel();

		ProcedimentoCumulatoSqlDAO lProcCumSqlDao = null;
		BeneficioCumuloSqlDAO lBenCumSqlDao = null;
		UfficioSqlDAO lUffSqlDao = null;

		TitoloCumulatoSqlDAO lTitoloSqlDaoStato = null;
		Vector<TitoloCumulatoModel> lTitoliConStatoEsec = new Vector<>();

		StatoEsecTitoloCumulatoSqlDAO lStatoEsecSqlDao = null;
		StatoEsecTitoloCumulatoModel lStatoModel = null;

		ComputiCumuloSqlDAO lCompSqlDao = null;

		lTitoloRicerca.setIstrIdIstruttoriaCumulo(aIdIstruttoriaCumulo);

		try {
			lConn = getDBConnection();

			lTitoloSqlDao = new TitoloCumulatoSqlDAO(lConn);
			lProcCumSqlDao = new ProcedimentoCumulatoSqlDAO(lConn);
			// lSoggCumSqlDao = new SoggettoCumulatoSqlDAO (lConn);
			lUffSqlDao = new UfficioSqlDAO(lConn);
			lTitoloSqlDaoStato = new TitoloCumulatoSqlDAO(lConn);
			lStatoEsecSqlDao = new StatoEsecTitoloCumulatoSqlDAO(lConn);
			lCompSqlDao = new ComputiCumuloSqlDAO(lConn);

			// ====================================================================================
			// Ricerca dei Benefici assegnati in
			// sentenza\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
			lTitoloSqlDao.ricercaTitoloCumulatoJoinBeneficioCumByIstruttoriaOrderBy(aIdIstruttoriaCumulo,
					aOrdinamento, aCodNaturaBen, aCodTipiBen);
			lTitoloSqlDao.start();

			while (lTitoloSqlDao.next()) {
				TitoloCumulatoModel lTitolo = (TitoloCumulatoModel) lTitoloSqlDao.getModel();
				lTitoliInIstruttoria.add(lTitolo);

				lProcCumSqlDao.ricercaProcedimentoCumulatoByIdTitolo(lTitolo.getIdTitoloCumulato());
				ProcedimentoCumulatoModel lProcModel = (ProcedimentoCumulatoModel) lProcCumSqlDao
						.getModelByKey();
				lTitolo.setProcedimentoCumulato(lProcModel);
				lProcCumSqlDao.stop();

				if (lProcModel != null && "S".equals(lProcModel.getFlagAccorpato())) {

					lUffSqlDao.selUfficioByCod(lProcModel.getChiaveUfficioOrigine());
					UfficioModel lUffMod = (UfficioModel) lUffSqlDao.getModelByKey();
					lUffSqlDao.stop();

					lProcModel.setUfficioOrigine(lUffMod);
				}

				lBenCumSqlDao = new BeneficioCumuloSqlDAO(lConn);
				Vector<BeneficioCumuloModel> lVecBenCum = new Vector<>();
				BeneficioCumuloModel lBenMod = null;
				// String lCodNatBen = "C";
				lBenCumSqlDao.ricercaBeneficioCumuloByTitoloCum(lTitolo.getIdTitoloCumulato(), aCodNaturaBen,
						aCodTipiBen);
				// Vector<BeneficioCumuloModel> lVecBenCum = new
				// Vector<BeneficioCumuloModel>(lBenCumSqlDao.getModels());
				lBenCumSqlDao.start();
				while (lBenCumSqlDao.next()) {
					lBenMod = (BeneficioCumuloModel) lBenCumSqlDao.getModel();
					if (lBenMod != null && lBenMod.getIdBeneficioCumulo() != null) {
						if (!"C".equalsIgnoreCase(lBenMod.getFlagStato())) {
							lVecBenCum.addElement(lBenMod);
						}
					}
				}
				// Ticket#202603160115 - si stoppa subito il dao/sqldao CURSOR LEAK!!! Vengono aperti tanti cursori lBenCumSqlDao
				// quanti sono i titoli e chiusi solo nel finally. Con 120 titoli si hanno 120 cursori aperti contemporaneamente
				lBenCumSqlDao.stop();
				// Ticket#202603160115 – FINE
				if (lVecBenCum != null && lVecBenCum.size() > 0) {
					lTitolo.setBeneficiCumulo(lVecBenCum);
				}
			}
			lTitoloSqlDao.stop();

			// ===================================================================
			// == Ricerca delle Amnistia/Indulto assegnati con i provvedimenti
			Vector<String> listaTipoEve = new Vector<>();
			listaTipoEve.add("01"); // Provvedimento

			Vector<String> listaTipoProv = new Vector<>();
			listaTipoProv.add("03"); // Ordinanza

			Vector<String> listaMotivo = new Vector<>();
			listaMotivo.add("0284"); // Applicazione Amnistia/Indulto

			lTitoloSqlDaoStato.ricercaTitoloCumulatoJoinStatoEsecTitoloCumByIstruttoria_e_Provvedimento(
					aIdIstruttoriaCumulo, aOrdinamento, listaTipoEve, listaTipoProv, listaMotivo);
			lTitoloSqlDaoStato.start();
			while (lTitoloSqlDaoStato.next()) {
				TitoloCumulatoModel lTitoloStat = (TitoloCumulatoModel) lTitoloSqlDaoStato.getModel();
				lTitoliConStatoEsec.add(lTitoloStat);

				lProcCumSqlDao.ricercaProcedimentoCumulatoByIdTitolo(lTitoloStat.getIdTitoloCumulato());
				ProcedimentoCumulatoModel lProcModel = (ProcedimentoCumulatoModel) lProcCumSqlDao
						.getModelByKey();
				lTitoloStat.setProcedimentoCumulato(lProcModel);
				lProcCumSqlDao.stop();

				if (lProcModel != null && "S".equals(lProcModel.getFlagAccorpato())) {

					lUffSqlDao.selUfficioByCod(lProcModel.getChiaveUfficioOrigine());
					UfficioModel lUffMod = (UfficioModel) lUffSqlDao.getModelByKey();
					lUffSqlDao.stop();

					lProcModel.setUfficioOrigine(lUffMod);
				}

				// Ricerca Stato_Esec E computi
				Vector<StatoEsecTitoloCumulatoModel> lListaProvStatoEsec = new Vector<>();
				lStatoEsecSqlDao.ricercaStatoEsecTitoloCumulatoByIdTitolo(lTitoloStat.getIdTitoloCumulato(),
						listaMotivo);
				lStatoEsecSqlDao.start();
				while (lStatoEsecSqlDao.next()) {
					lStatoModel = (StatoEsecTitoloCumulatoModel) lStatoEsecSqlDao.getModel();

					lCompSqlDao.ricercaComputiCumuloByIdStatoEsec(lStatoModel.getIdStatoEsecTitoloCumulato());
					Vector<ComputiCumuloModel> lVecComp = new Vector<ComputiCumuloModel>(
							lCompSqlDao.getModels());
					if (lVecComp != null && lVecComp.size() > 0)
						lStatoModel.setListaComputi(lVecComp);

					lListaProvStatoEsec.add(lStatoModel);
				}
				// Ticket#202603160115 - si stoppa subito il dao/sqldao CURSOR LEAK!!! Vengono aperti tanti cursori lBenCumSqlDao
				// quanti sono i titoli e chiusi solo nel finally. Con 120 titoli si hanno 120 cursori aperti contemporaneamente
				lStatoEsecSqlDao.stop();
				// Ticket#202603160115 - FINE 
				lTitoloStat.setStatoEsecuzioneTitoloCumulato(lListaProvStatoEsec);
			}

			lTitoloSqlDaoStato.stop();
			// ================================================================================

			// Unifico le liste ottenute dalle 2 Ricerche per Ottenere UN UNICA LISTA DI TITOLI
			Vector<TitoloCumulatoModel> lTitoliDaAggiungere = new Vector<>();

			Iterator itxSTE = lTitoliConStatoEsec.iterator();
			while (itxSTE.hasNext()) {
				TitoloCumulatoModel lTitoloSTE = (TitoloCumulatoModel) itxSTE.next();
				boolean lTrovato = false;
				for (int kk = 0; kk < lTitoliInIstruttoria.size(); kk++) {
					TitoloCumulatoModel lTitoloSENT = lTitoliInIstruttoria.get(kk);
					if (lTitoloSTE.getIdTitoloCumulato().compareTo(lTitoloSENT.getIdTitoloCumulato()) == 0) {
						lTitoloSENT.setStatoEsecuzioneTitoloCumulato(
								lTitoloSTE.getStatoEsecuzioneTitoloCumulato());
						lTrovato = true;
					}
				}
				if (!lTrovato) {
					lTitoliDaAggiungere.addElement(lTitoloSTE);
				}
			}

			// ===
			lTitoliInIstruttoria.addAll(lTitoliDaAggiungere);
		} catch (DAOException daoEx) {
			siesLogger.error("DAOException: ", daoEx);
			throw new F3BException(
					"IstruttoriaCumuloController.ExRicercaTitoliBeneficiCumByIstruttoriaOrderBy: Non posso leggere : "
							+ daoEx);
		} catch (Exception ex) {
			siesLogger.error("Exception: ", ex);
			throw new F3BException(
					"IstruttoriaCumuloController.ExRicercaTitoliBeneficiCumByIstruttoriaOrderBy: Non posso leggere : "
							+ ex);
		} finally {
			cleanup(lTitoloSqlDao);
			cleanup(lProcCumSqlDao);
			cleanup(lBenCumSqlDao);
			cleanup(lUffSqlDao);
			cleanup(lTitoloSqlDaoStato);
			cleanup(lStatoEsecSqlDao);
			cleanup(lCompSqlDao);

			cleanup(lConn);
		}

		return lTitoliInIstruttoria;
	}

	/**
	 * La Ricerca è la stessa di ExRicercaTitoliByIstruttoriaOrderBy con in più La join con Beneficio_Cumulo e
	 * senza La Ricerca del Soggetto_Cumulo aggregato
	 */
	public Vector<TitoloCumulatoModel> ExRicercaTitoliMisureSicCumByIstruttoriaOrderBy(
			BigDecimal aIdIstruttoriaCumulo, String aOrdinamento) throws F3BException {

		Connection lConn = null;

		Vector<TitoloCumulatoModel> lTitoliInIstruttoria = new Vector<>();

		TitoloCumulatoSqlDAO lTitoloSqlDao = null;
		TitoloCumulatoModel lTitoloRicerca = new TitoloCumulatoModel();

		ProcedimentoCumulatoSqlDAO lProcCumSqlDao = null;
		MisuraSicurezzaCumuloSqlDAO lMisSicCumSqlDao = null;
		UfficioSqlDAO lUffSqlDao = null;

		lTitoloRicerca.setIstrIdIstruttoriaCumulo(aIdIstruttoriaCumulo);

		try {
			lConn = getDBConnection();

			lTitoloSqlDao = new TitoloCumulatoSqlDAO(lConn);
			lProcCumSqlDao = new ProcedimentoCumulatoSqlDAO(lConn);
			lUffSqlDao = new UfficioSqlDAO(lConn);

			// lTitoloSqlDao.ricercaTitoloCumulatoJoinBeneficioCumByIstruttoriaOrderBy (aIdIstruttoriaCumulo,
			// aOrdinamento, aCodNaturaBen, aCodTipiBen);
			lTitoloSqlDao.ricercaTitoloCumulatoJoinMisuraSicCumByIstruttoriaOrderBy(aIdIstruttoriaCumulo,
					aOrdinamento);

			lTitoloSqlDao.start();

			while (lTitoloSqlDao.next()) {
				TitoloCumulatoModel lTitolo = (TitoloCumulatoModel) lTitoloSqlDao.getModel();
				lTitoliInIstruttoria.add(lTitolo);

				lProcCumSqlDao.ricercaProcedimentoCumulatoByIdTitolo(lTitolo.getIdTitoloCumulato());
				ProcedimentoCumulatoModel lProcModel = (ProcedimentoCumulatoModel) lProcCumSqlDao
						.getModelByKey();
				lTitolo.setProcedimentoCumulato(lProcModel);
				lProcCumSqlDao.stop();

				if (lProcModel != null && "S".equals(lProcModel.getFlagAccorpato())) {

					lUffSqlDao.selUfficioByCod(lProcModel.getChiaveUfficioOrigine());
					UfficioModel lUffMod = (UfficioModel) lUffSqlDao.getModelByKey();
					lUffSqlDao.stop();

					lProcModel.setUfficioOrigine(lUffMod);
				}

				lMisSicCumSqlDao = new MisuraSicurezzaCumuloSqlDAO(lConn);
				// lBenCumSqlDao.ricercaBeneficioCumuloByTitoloCum(lTitolo.getIdTitoloCumulato(),
				// aCodNaturaBen, aCodTipiBen);
				// Vector<BeneficioCumuloModel> lVecBenCum = new
				// Vector<BeneficioCumuloModel>(lBenCumSqlDao.getModels());
				lMisSicCumSqlDao.ricercaMisuraSicurezzaCumuloByIdTitoloCum(lTitolo.getIdTitoloCumulato());
				Vector<MisuraSicurezzaCumuloModel> lVecMSCum = new Vector<MisuraSicurezzaCumuloModel>(
						lMisSicCumSqlDao.getModels());
				if (lVecMSCum != null && lVecMSCum.size() > 0) {
					lTitolo.setMisureSicurezzaCumulo(lVecMSCum);
				}

			}

			lTitoloSqlDao.stop();
		} catch (DAOException daoEx) {
			siesLogger.error("DAOException: ", daoEx);
			throw new F3BException(
					"IstruttoriaCumuloController.ExRicercaTitoliBeneficiCumByIstruttoriaOrderBy: Non posso leggere : "
							+ daoEx);
		} catch (Exception ex) {
			siesLogger.error("Exception: ", ex);
			throw new F3BException(
					"IstruttoriaCumuloController.ExRicercaTitoliBeneficiCumByIstruttoriaOrderBy: Non posso leggere : "
							+ ex);
		} finally {
			cleanup(lTitoloSqlDao);
			cleanup(lProcCumSqlDao);
			cleanup(lMisSicCumSqlDao);
			cleanup(lUffSqlDao);

			cleanup(lConn);
		}

		return lTitoliInIstruttoria;
	}

	/**
	 * La Ricerca è la stessa di ExRicercaTitoliByIstruttoriaOrderBy con in più La join con
	 * Sanzione_Sost_Cumulo e senza La Ricerca del Soggetto_Cumulo aggregato
	 */
	public Vector<TitoloCumulatoModel> ExRicercaTitoliSanzioneSostCumByIstruttoriaOrderBy(
			BigDecimal aIdIstruttoriaCumulo, String aOrdinamento, String[] lIdTitoliSelezionati)
			throws F3BException {

		Connection lConn = null;

		Vector<TitoloCumulatoModel> lTitoliInIstruttoria = new Vector<>();

		TitoloCumulatoSqlDAO lTitoloSqlDao = null;
		TitoloCumulatoModel lTitoloRicerca = new TitoloCumulatoModel();

		SanzioneSostitutivaCumuloModel lSanSosCumMod = null;
		SanzioneSostitutivaCumuloSqlDAO lSSCumSqlDao = null;

		ProcedimentoCumulatoSqlDAO lProcCumSqlDao = null;
		UfficioSqlDAO lUffSqlDao = null;

		lTitoloRicerca.setIstrIdIstruttoriaCumulo(aIdIstruttoriaCumulo);

		try {
			lConn = getDBConnection();

			lTitoloSqlDao = new TitoloCumulatoSqlDAO(lConn);
			lProcCumSqlDao = new ProcedimentoCumulatoSqlDAO(lConn);
			lUffSqlDao = new UfficioSqlDAO(lConn);

			String selezione = "";
			lTitoloSqlDao.ricercaTitoloCumulatoJoinSanzioneSostCumByIstruttoriaOrderBy(aIdIstruttoriaCumulo,
					aOrdinamento);

			lTitoloSqlDao.start();

			while (lTitoloSqlDao.next()) {
				TitoloCumulatoModel lTitolo = (TitoloCumulatoModel) lTitoloSqlDao.getModel();

				// Selezione del Titolo
				selezione = "NO";
				if (lIdTitoliSelezionati != null) {
					for (int i = 0; i < lIdTitoliSelezionati.length; i++) {
						if (lTitolo.getIdTitoloCumulato().equals(new BigDecimal(lIdTitoliSelezionati[i]))) {
							selezione = "SI";
						}
					}
				} else {
					selezione = "SI";
				}

				if ("SI".equals(selezione)) {
					lTitoliInIstruttoria.add(lTitolo);

					lProcCumSqlDao.ricercaProcedimentoCumulatoByIdTitolo(lTitolo.getIdTitoloCumulato());
					ProcedimentoCumulatoModel lProcModel = (ProcedimentoCumulatoModel) lProcCumSqlDao
							.getModelByKey();
					lTitolo.setProcedimentoCumulato(lProcModel);
					lProcCumSqlDao.stop();

					if (lProcModel != null && "S".equals(lProcModel.getFlagAccorpato())) {

						lUffSqlDao.selUfficioByCod(lProcModel.getChiaveUfficioOrigine());
						UfficioModel lUffMod = (UfficioModel) lUffSqlDao.getModelByKey();
						lUffSqlDao.stop();

						lProcModel.setUfficioOrigine(lUffMod);
					}

					lSSCumSqlDao = new SanzioneSostitutivaCumuloSqlDAO(lConn);
					lSSCumSqlDao.ricercaSanzioneSostitutivaCumByTitoloCum(lTitolo.getIdTitoloCumulato());

					lSSCumSqlDao.start();
					while (lSSCumSqlDao.next()) {
						lSanSosCumMod = (SanzioneSostitutivaCumuloModel) lSSCumSqlDao.getModel();
					}

					if (lSanSosCumMod != null && lSanSosCumMod.getIdSanzioneSostitutivaCum() != null)
						lTitolo.setSanzioneSostitutivaCumulo(lSanSosCumMod);

					lSSCumSqlDao.stop();
				}
			}

			lTitoloSqlDao.stop();
		} catch (DAOException daoEx) {
			siesLogger.error("DAOException: ", daoEx);
			throw new F3BException(
					"IstruttoriaCumuloController.ExRicercaTitoliSanzioneSostCumByIstruttoriaOrderBy: Non posso leggere : "
							+ daoEx);
		} catch (Exception ex) {
			siesLogger.error("Exception: ", ex);
			throw new F3BException(
					"IstruttoriaCumuloController.ExRicercaTitoliSanzioneSostCumByIstruttoriaOrderBy: Non posso leggere : "
							+ ex);
		} finally {
			cleanup(lTitoloSqlDao);
			cleanup(lProcCumSqlDao);
			cleanup(lSSCumSqlDao);
			cleanup(lUffSqlDao);

			cleanup(lConn);
		}

		return lTitoliInIstruttoria;
	} // Chiude ExRicercaTitoliSanzioneSostCumByIstruttoriaOrderBy

	/**
	 * La Ricerca è la stessa di ExRicercaTitoliByIstruttoriaOrderBy con in più La join con
	 * Pena_Accessoria_Cumulo e senza La Ricerca del Soggetto_Cumulo aggregato
	 */
	public Vector<TitoloCumulatoModel> ExRicercaTitoliPenaAccCumByIstruttoriaOrderBy(
			BigDecimal aIdIstruttoriaCumulo, String aOrdinamento, String[] lIdTitoliSelezionati)
			throws F3BException {

		Connection lConn = null;

		Vector<TitoloCumulatoModel> lTitoliInIstruttoria = new Vector<>();

		TitoloCumulatoSqlDAO lTitoloSqlDao = null;
		TitoloCumulatoModel lTitoloRicerca = new TitoloCumulatoModel();

		PenaAccessoriaCumuloModel lPACumModel = null;
		PenaAccessoriaCumuloSqlDAO lPACumSqlDao = null;
		Vector<PenaAccessoriaCumuloModel> lListaPA = null;

		ProcedimentoCumulatoSqlDAO lProcCumSqlDao = null;
		UfficioSqlDAO lUffSqlDao = null;

		lTitoloRicerca.setIstrIdIstruttoriaCumulo(aIdIstruttoriaCumulo);

		try {
			lConn = getDBConnection();

			lTitoloSqlDao = new TitoloCumulatoSqlDAO(lConn);
			lProcCumSqlDao = new ProcedimentoCumulatoSqlDAO(lConn);
			lUffSqlDao = new UfficioSqlDAO(lConn);

			String selezione = "";
			lTitoloSqlDao.ricercaTitoloCumulatoJoinSanzioneSostCumByIstruttoriaOrderBy(aIdIstruttoriaCumulo,
					aOrdinamento);
			lTitoloSqlDao.ricercaTitoloCumulatoJoinPenaAccCumByIstruttoriaOrderBy(aIdIstruttoriaCumulo,
					aOrdinamento);

			lTitoloSqlDao.start();

			while (lTitoloSqlDao.next()) {
				TitoloCumulatoModel lTitolo = (TitoloCumulatoModel) lTitoloSqlDao.getModel();

				// Selezione del Titolo
				selezione = "NO";
				if (lIdTitoliSelezionati != null) {
					for (int i = 0; i < lIdTitoliSelezionati.length; i++) {
						if (lTitolo.getIdTitoloCumulato().equals(new BigDecimal(lIdTitoliSelezionati[i]))) {
							selezione = "SI";
						}
					}
				} else {
					selezione = "SI";
				}

				if ("SI".equals(selezione)) {
					lTitoliInIstruttoria.add(lTitolo);

					lProcCumSqlDao.ricercaProcedimentoCumulatoByIdTitolo(lTitolo.getIdTitoloCumulato());
					ProcedimentoCumulatoModel lProcModel = (ProcedimentoCumulatoModel) lProcCumSqlDao
							.getModelByKey();
					lTitolo.setProcedimentoCumulato(lProcModel);
					lProcCumSqlDao.stop();

					if (lProcModel != null && "S".equals(lProcModel.getFlagAccorpato())) {

						lUffSqlDao.selUfficioByCod(lProcModel.getChiaveUfficioOrigine());
						UfficioModel lUffMod = (UfficioModel) lUffSqlDao.getModelByKey();
						lUffSqlDao.stop();

						lProcModel.setUfficioOrigine(lUffMod);
					}

					lListaPA = new Vector<>();
					lPACumSqlDao = new PenaAccessoriaCumuloSqlDAO(lConn);
					lPACumSqlDao.ricercaPenaAccessoriaCumuloByTitoloCum(lTitolo.getIdTitoloCumulato());

					lPACumSqlDao.start();
					while (lPACumSqlDao.next()) {
						lPACumModel = (PenaAccessoriaCumuloModel) lPACumSqlDao.getModel();

						if (lPACumModel != null && lPACumModel.getIdPenaAccessoriaCumulo() != null) {
							lListaPA.add(lPACumModel);
						}
					}

					if (lListaPA != null && lListaPA.size() > 0)
						lTitolo.setPeneAccessorieCumulo(lListaPA);

					lPACumSqlDao.stop();
				}

			}

			lTitoloSqlDao.stop();
		} catch (DAOException daoEx) {
			siesLogger.error("DAOException: ", daoEx);
			throw new F3BException(
					"IstruttoriaCumuloController.ExRicercaTitoliPenaAccCumByIstruttoriaOrderBy: Non posso leggere : "
							+ daoEx);
		} catch (Exception ex) {
			siesLogger.error("Exception: ", ex);
			throw new F3BException(
					"IstruttoriaCumuloController.ExRicercaTitoliPenaAccCumByIstruttoriaOrderBy: Non posso leggere : "
							+ ex);
		} finally {
			cleanup(lTitoloSqlDao);
			cleanup(lProcCumSqlDao);
			cleanup(lPACumSqlDao);
			cleanup(lUffSqlDao);

			cleanup(lConn);
		}

		return lTitoliInIstruttoria;
	} // Chiude ExRicercaTitoliPenaAccCumByIstruttoriaOrderBy

	/**
	 * La Ricerca è la stessa di ExRicercaTitoliByIstruttoriaOrderBy con in più La join con
	 * Stato_Esec_Titolo_Cum e senza La Ricerca del Soggetto_Cumulo aggregato
	 */
	public Vector<TitoloCumulatoModel> ExRicercaTitoliStatoEsecTitoloCumByIstruttoriaOrderBy(
			BigDecimal aIdIstruttoriaCumulo, String aOrdinamento, Vector<String> lCodici,
			String[] lIdTitoliSelezionati) throws F3BException {

		Connection lConn = null;

		Vector<TitoloCumulatoModel> lTitoliInIstruttoria = new Vector<>();

		TitoloCumulatoSqlDAO lTitoloSqlDao = null;
		TitoloCumulatoModel lTitoloRicerca = new TitoloCumulatoModel();

		StatoEsecTitoloCumulatoSqlDAO lStatoCumSqlDao = null;

		ProcedimentoCumulatoSqlDAO lProcCumSqlDao = null;
		UfficioSqlDAO lUffSqlDao = null;

		lTitoloRicerca.setIstrIdIstruttoriaCumulo(aIdIstruttoriaCumulo);

		try {
			lConn = getDBConnection();

			lTitoloSqlDao = new TitoloCumulatoSqlDAO(lConn);
			lProcCumSqlDao = new ProcedimentoCumulatoSqlDAO(lConn);
			lUffSqlDao = new UfficioSqlDAO(lConn);

			String selezione = "";
			lTitoloSqlDao.ricercaTitoloCumulatoJoinStatoEsecTitoloCumByIstruttoriaOrderBy(
					aIdIstruttoriaCumulo, aOrdinamento, lCodici);

			lTitoloSqlDao.start();

			while (lTitoloSqlDao.next()) {
				TitoloCumulatoModel lTitolo = (TitoloCumulatoModel) lTitoloSqlDao.getModel();

				// Selezione del Titolo
				selezione = "NO";
				if (lIdTitoliSelezionati != null) {
					for (int i = 0; i < lIdTitoliSelezionati.length; i++) {
						if (lTitolo.getIdTitoloCumulato().equals(new BigDecimal(lIdTitoliSelezionati[i]))) {
							selezione = "SI";
						}
					}
				} else {
					selezione = "SI";
				}

				if ("SI".equals(selezione)) {
					lTitoliInIstruttoria.add(lTitolo);

					// Procedimento_Cumulato
					lProcCumSqlDao.ricercaProcedimentoCumulatoByIdTitolo(lTitolo.getIdTitoloCumulato());
					ProcedimentoCumulatoModel lProcModel = (ProcedimentoCumulatoModel) lProcCumSqlDao
							.getModelByKey();
					lTitolo.setProcedimentoCumulato(lProcModel);
					lProcCumSqlDao.stop();

					if (lProcModel != null && "S".equals(lProcModel.getFlagAccorpato())) {

						lUffSqlDao.selUfficioByCod(lProcModel.getChiaveUfficioOrigine());
						UfficioModel lUffMod = (UfficioModel) lUffSqlDao.getModelByKey();
						lUffSqlDao.stop();

						lProcModel.setUfficioOrigine(lUffMod);
					}

					// Stato_Esec_Titolo_Cumulato
					// 27/07/2018 Stato Esecuzione con le sole M.A. concesse.
					Vector<StatoEsecTitoloCumulatoModel> lVecStato = new Vector<>();
					List<String> listCodEsiMAConc = new ArrayList<>();
					listCodEsiMAConc.addAll(StatoEsecuzioneCumuloUtils.aCodEsiMisAltEsclusi);
					lStatoCumSqlDao = new StatoEsecTitoloCumulatoSqlDAO(lConn);
					lStatoCumSqlDao.ricercaStatoEsecTitoloCumulatoByIdTitolo(lTitolo.getIdTitoloCumulato(),
							lCodici, listCodEsiMAConc);
					lVecStato = new Vector<StatoEsecTitoloCumulatoModel>(lStatoCumSqlDao.getModels());

					if (lVecStato != null && lVecStato.size() > 0) {
						lTitolo.setStatoEsecuzioneTitoloCumulato(lVecStato);
					}
				}
			}

			lTitoloSqlDao.stop();
		} catch (DAOException daoEx) {
			siesLogger.error("DAOException: ", daoEx);
			throw new F3BException(
					"IstruttoriaCumuloController.ExRicercaTitoliStatoEsecTitoloCumByIstruttoriaOrderBy: Non posso leggere : "
							+ daoEx);
		} catch (Exception ex) {
			siesLogger.error("Exception: ", ex);
			throw new F3BException(
					"IstruttoriaCumuloController.ExRicercaTitoliStatoEsecTitoloCumByIstruttoriaOrderBy: Non posso leggere : "
							+ ex);
		} finally {
			cleanup(lTitoloSqlDao);
			cleanup(lProcCumSqlDao);
			cleanup(lStatoCumSqlDao);
			cleanup(lUffSqlDao);

			cleanup(lConn);
		}

		return lTitoliInIstruttoria;
	} // Chiude ExRicercaTitoliStatoEsecTitoloCumByIstruttoriaOrderBy

	/**
	 * Modulo che preleva i dati che concorrono al calcolo della pena per il cumulo
	 *
	 * @param aIdIstruttoriaCumulo
	 * @param aIdTitolo
	 *            - Se valorizzato effettua il calcolo recuperando solo i dati del titolo indicato
	 * @param aComputaRichieste
	 *            - indica se computare le richieste
	 */
	public CalcoloPenaCumuloModel ExCalcolaPenaCumuloByIstruttoria(BigDecimal aIdIstruttoriaCumulo,
			BigDecimal aIdTitolo, boolean aComputaRichieste) throws F3BException {

		Connection lConn = null;

		TitoloCumulatoSqlDAO lTitoloSqlDao = null;
		ProcedimentoCumulatoSqlDAO lProcCumSqlDao = null;
		PenaComplessivaCumuloSqlDAO lPenSqlDao = null;
		SanzioneSostitutivaCumuloSqlDAO lSSCumSqlDao = null;
		MisuraCautelareCumuloSqlDAO lMisCautCumSqlDao = null;
		BeneficioCumuloSqlDAO lBeneficioSqlDao = null;
		LibAnticipataCumuloSqlDAO lLibAnticSqlDao = null;
		StatoEsecTitoloCumulatoSqlDAO lStatoEsecSqlDao = null;
		ComputiCumuloSqlDAO lComputiSqlDao = null;
		RichiestePmInCumuloSqlDAO lRichPmInCumuloSqlDao = null;
		ProvvedimentoGeSorvCumSqlDAO lProvvGeSorvSqlDao = null;
		UfficioSqlDAO lUffSqlDao = null;
		// MEV_2025-48 - ALTRO - Ordinamento titoli come x Lista titoli
		IstruttoriaCumuloSqlDAO lIstrSqlDao = null;

		// MEV_2025-48 - ALTRO - Visualizzazione Pena In Continuazione
		ContinuazioneCumuloSqlDAO lContCumuloSqlDao = null;

		CalcoloPenaCumuloModel lCalcoloPenaModel = new CalcoloPenaCumuloModel();

		Vector<TitoloCumulatoModel> lListaTitoli = null; // new Vector<TitoloCumulatoModel>();

		// Vector <SanzioneSostitutivaCumuloModel> lListaSanzioniSost = null;

		// HashTable <IdTitolo, true = PenaSospesa, false = PenaNonSospesa>
		// Hashtable<BigDecimal, TitoloCumulatoModel> lListaTitoliSospesi = new Hashtable<BigDecimal,
		// TitoloCumulatoModel>();
		Hashtable<BigDecimal, TitoloCumulatoModel> lListaTitoliSanSost = new Hashtable<>();
		Hashtable<BigDecimal, TitoloCumulatoModel> lListaTitoliEsclusi = new Hashtable<>();

		try {
			lConn = getDBConnection();

			// MEV_2025-48 - ALTRO - Ordinamento titoli come x Lista titoli
			lIstrSqlDao = new IstruttoriaCumuloSqlDAO(lConn);
			lIstrSqlDao.ricercaIstruttoriaCumuloByKey(aIdIstruttoriaCumulo);
			IstruttoriaCumuloModel lIstrCumulo = (IstruttoriaCumuloModel) lIstrSqlDao.getModelByKey();
			String lOrdinamentoTitoli = ICostantiIstruttoriaCumulo.ORDER_BY_DATA_IRREVOCABILITA_DESC;
			if (lIstrCumulo.getOrdinamentoTitoli() != null)
				lOrdinamentoTitoli = lIstrCumulo.getOrdinamentoTitoli();

			// recupero l'elenco dei titoli
			lTitoloSqlDao = new TitoloCumulatoSqlDAO(lConn);

			// MEV_2025-48 - ALTRO - Ordinamento titoli come x Lista titoli
			// lTitoloSqlDao.ricercaTitoloCumulatoByIstruttoriaOrderBy(aIdIstruttoriaCumulo,
			// ICostantiIstruttoriaCumulo.ORDER_BY_DATA_IRREVOCABILITA_DESC);
			lTitoloSqlDao.ricercaTitoloCumulatoByIstruttoriaOrderBy(aIdIstruttoriaCumulo, lOrdinamentoTitoli);
			// MEV_2025-48 - ALTRO - Ordinamento titoli come x Lista titoli - FINE
			lListaTitoli = new Vector<TitoloCumulatoModel>(lTitoloSqlDao.getModels());
			lCalcoloPenaModel.setListaTitoli(lListaTitoli);

			// Aggiungere eventuali dettagli sul procedimento cumulato vedi
			// ExRicercaTitoliByIstruttoriaOrderBy

			lUffSqlDao = new UfficioSqlDAO(lConn);

			// ===================================================
			// Ciclo SUI TITOLI
			// ===================================================
			lPenSqlDao = new PenaComplessivaCumuloSqlDAO(lConn);
			lStatoEsecSqlDao = new StatoEsecTitoloCumulatoSqlDAO(lConn);
			lProcCumSqlDao = new ProcedimentoCumulatoSqlDAO(lConn);

			for (TitoloCumulatoModel lTitolo : lListaTitoli) {
				String lDescTitolo = lTitolo.getDescrTipoProvvedimento() + " N° " + lTitolo.getAnnoSentenza()
						+ "/" + lTitolo.getNumeroSentenza();
				lDescTitolo += " - " + lTitolo.getCodTipoAutoritaEmittente() + " "
						+ lTitolo.getDescrLuogoEmittente();

				siesLogger.debug("lTitolo = " + lTitolo.getIdTitoloCumulato());

				// Se il titolo è escluso non carico i dati
				if ("S".equals(lTitolo.getFlagEscluso())) {
					siesLogger.debug("Titolo escluso momentaneamente dal calcolo");
					lListaTitoliEsclusi.put(lTitolo.getIdTitoloCumulato(), lTitolo);
					continue;
				} else if (aIdTitolo != null && aIdTitolo.compareTo(lTitolo.getIdTitoloCumulato()) != 0) {
					siesLogger.debug("Altro Titolo, lo salto");
					lListaTitoliEsclusi.put(lTitolo.getIdTitoloCumulato(), lTitolo);
					continue;
				}

				// ==============================================
				// Recupero il ProcedimentoCumulato se presente
				// ==============================================
				lProcCumSqlDao.ricercaProcedimentoCumulatoByIdTitolo(lTitolo.getIdTitoloCumulato());
				ProcedimentoCumulatoModel lProcModel = (ProcedimentoCumulatoModel) lProcCumSqlDao
						.getModelByKey();
				lTitolo.setProcedimentoCumulato(lProcModel);
				lProcCumSqlDao.stop();

				if (lProcModel != null && "S".equals(lProcModel.getFlagAccorpato())) {
					lUffSqlDao.selUfficioByCod(lProcModel.getChiaveUfficioOrigine());
					UfficioModel lUffMod = (UfficioModel) lUffSqlDao.getModelByKey();
					lUffSqlDao.stop();

					lProcModel.setUfficioOrigine(lUffMod);
				}

				BeneficioCumuloModel lBeneficioSosp = this.isPenaSospesa(lTitolo.getIdTitoloCumulato(),
						lConn);
				boolean isPenaSospesa = false;
				boolean isPenaInteramenteSospesa = false;
				boolean isPenaDetentivaSospesa = false;

				if (lBeneficioSosp != null && !lBeneficioSosp.getIsRevocato()) {
					siesLogger.debug("Presente Sospensione condizionale non revocata");

					isPenaSospesa = true;

					if ("02".equals(lBeneficioSosp.getCodSottotipoBeneficio()))
						isPenaDetentivaSospesa = true;
					else
						isPenaInteramenteSospesa = true;
				}

				// Verifico se la pena è in continuazione e assorbita sulla pena di altri titolo
				boolean isPenaInContinuazione = false;
				isPenaInContinuazione = this.isPenaInContinuazione(lTitolo.getIdTitoloCumulato(), lConn);
				siesLogger.debug("isPenaInContinuazione = " + isPenaInContinuazione);

				// ==============================================
				// Recupero la Pena Complessiva
				// ==============================================
				lPenSqlDao.ricercaPenaComplessivaCumuloByIdTitolo(lTitolo.getIdTitoloCumulato());
				PenaComplessivaCumuloModel lPenaComplMod = (PenaComplessivaCumuloModel) lPenSqlDao
						.getModelByKey();
				lPenSqlDao.stop();
				if (lPenaComplMod != null) {

					// MEV_2025-48 - ALTRO - Visualizzazione Pena In Continuazione
					// recupero eventuali cntinuazioni (solo visualizzazione)
					lContCumuloSqlDao = new ContinuazioneCumuloSqlDAO(lConn);
					lContCumuloSqlDao.ricercaContinuazioneByIdTitolo(lPenaComplMod.getTitIdTitoloCumulato());
					Vector lListaCont = new Vector(lContCumuloSqlDao.getModels());
					lPenaComplMod.setContinuazioniCumulo(lListaCont);

					// MEV_2025-48 - ALTRO - Visualizzazione Pena In Continuazione

					if (isPenaDetentivaSospesa) {
						// MEV_2025-48 - ALTRO – Visualizzazione Pena Sospesa
						// Devo passare tutti i dati per poterli visualizzare
						lPenaComplMod.setBeneficioSospensioneCumulo(lBeneficioSosp);
						siesLogger.debug("isPenaDetentivaSospesa = " + isPenaDetentivaSospesa);
						// Procedo ad azzerare i quantum, lascio solo la pecuniaria
						// Reclusione
						// lPenaComplMod.setNumGiorniReclusione(null);
						// lPenaComplMod.setNumMesiReclusione(null);
						// lPenaComplMod.setNumAnniReclusione(null);
						// Arresto
						// lPenaComplMod.setNumGiorniArresto(null);
						// lPenaComplMod.setNumMesiArresto(null);
						// lPenaComplMod.setNumAnniArresto(null);

						lCalcoloPenaModel.addPenaComplessiva(lPenaComplMod);
					} else if (isPenaInteramenteSospesa) {
						// non aggiungo la pena al model
						// MEV_2025-48 - ALTRO – Visualizzazione Pena Sospesa
						// Devo passare tutti i dati per poterli visualizzare
						lPenaComplMod.setBeneficioSospensioneCumulo(lBeneficioSosp);
						lCalcoloPenaModel.addPenaComplessiva(lPenaComplMod);
						// MEV_2025-48 - ALTRO – Visualizzazione Pena Sospesa - FINE
					} else if (isPenaInContinuazione) {
						// La pena è in continuazione. Esiste un altro titolo la cui pena
						// è dichiarata in continuazione con la pena corrente e il tipo
						// di continuazione è R (Pena Complessiva ritenuta la continuazione)
						// quindi la pena su tale titolo già ingloba la pena del titolo corrente
						// Non aggiungo la pena al model altrimenti verrebbe conteggiata 2 volte
						siesLogger.debug("NON carico la pena = " + lPenaComplMod);
						// MEV_2025-48 - ALTRO - Visualizzazione Pena In Continuazione
						// Si deve visualizzzare comunque la pena in continuazione ma non conteggiarla
						siesLogger.debug("isPenaInContinuazione carico la pena x la visualizzazione");
						// Procedo ad azzerare i quantum, multa e ammenda
						// Reclusione
						lPenaComplMod.setNumGiorniReclusione(null);
						lPenaComplMod.setNumMesiReclusione(null);
						lPenaComplMod.setNumAnniReclusione(null);
						// Arresto
						lPenaComplMod.setNumGiorniArresto(null);
						lPenaComplMod.setNumMesiArresto(null);
						lPenaComplMod.setNumAnniArresto(null);
						//
						lPenaComplMod.setImportoMulta(null);
						lPenaComplMod.setImportoAmmenda(null);

						TitoloCumulatoModel lTitoloR = this
								.getTitoloContinuazioneR(lPenaComplMod.getTitIdTitoloCumulato(), lConn);
						lPenaComplMod.setTitoloContinuazioneR(lTitoloR);
						lCalcoloPenaModel.addPenaComplessiva(lPenaComplMod);
						// MEV_2025-48 - ALTRO - Visualizzazione Pena In Continuazione - FINE
					} else {
						// Sospensiva non presente o revocata aggiungo la pena
						siesLogger.debug("Carico la pena = " + lPenaComplMod);
						lCalcoloPenaModel.addPenaComplessiva(lPenaComplMod);
					}
				}

				// ==============================================
				// Recupero la Sanzione Sostitutiva se presente
				// ==============================================
				SanzioneSostitutivaCumuloModel lSSCumModel = null;
				if (lPenaComplMod != null) {
					lSSCumSqlDao = new SanzioneSostitutivaCumuloSqlDAO(lConn);
					lSSCumSqlDao.ricercaSanzioneSostitutivaByIdPenaComplessivaCum(
							lPenaComplMod.getIdPenaComplessivaCum());
					lSSCumModel = (SanzioneSostitutivaCumuloModel) lSSCumSqlDao.getModelByKey();
					
					if (lSSCumModel != null && !"C".equals(lSSCumModel.getFlagStato())) {
						lPenaComplMod.setSanzioneSostitutivaCumulo(lSSCumModel);
						lCalcoloPenaModel.addSanzioneSost(lSSCumModel);
						lListaTitoliSanSost.put(lTitolo.getIdTitoloCumulato(), lTitolo);

						// Verifico se revocata
						this.isSSRevocata(lTitolo.getIstrIdIstruttoriaCumulo(), lSSCumModel, lConn);
						// lPenaComplMod.getSanzioneSostitutivaCumulo().setIsRevocata(true);
						siesLogger.debug("Get is revocata = "
								+ lPenaComplMod.getSanzioneSostitutivaCumulo().getIsRevocata());
					}
				}

				// ========================================================
				// Recupero le Misure Cautelari Riconosciute in sentenza
				// ========================================================
				// devo aggiungere tutte la MC di tutti i titoli order by data inizio
				// + i computi
				// FIXME provare ad ordinare dopo
				if (!isPenaSospesa && (lSSCumModel == null || lSSCumModel.getIsRevocata())) {
					siesLogger.debug("Pena NON Sospesa! Recupero le MC in sentenza ");
					lMisCautCumSqlDao = new MisuraCautelareCumuloSqlDAO(lConn);

					/*
					 * ISSUE MAC : aggiunto parametro con valore false (visCanLog). la query non tira fuori le
					 * misure cautelari cancellate logicamente Numero MAC : 20200110017 Autore : monica Data :
					 * 14/gen/2020 Branch : 11.2.5
					 */
					lMisCautCumSqlDao.ricercaMisuraCautelareCumuloByIdTitolo(lTitolo.getIdTitoloCumulato(),
							false);
					// ***** FINE INTERVENTO 20200110017 *****//

					lMisCautCumSqlDao.start();
					while (lMisCautCumSqlDao.next()) {
						MisuraCautelareCumuloModel lMCModel = (MisuraCautelareCumuloModel) lMisCautCumSqlDao
								.getModel();
						lCalcoloPenaModel.addMisuraCautelare(lMCModel);

						siesLogger.debug("MC: idTitolo = " + lTitolo.getIdTitoloCumulato() + " - idMisura = "
								+ lMCModel.getIdMisuraCautelareCumulo());
					}
					// Ticket#202603160115 - si stoppa subito il dao/sqldao (ERR-new in ciclo for, ne finallyu si ha il 
					//                       puntamento solo all'ultistanza, le altre restano appese. Inoltre anche se 
					// non ci fosse la new si aprirebbeo numerosi cursori contemporaneamente chiusi solo nel finally)
					// Generando un possibile CURSOR LEAK
					lMisCautCumSqlDao.stop();
					// Ticket#202603160115 - FINE
				} else {
					siesLogger.debug("Pena Sospesa! NON recupero le MC in sentenza ");
				}

				// ==============================================
				// Recupero i Computi disposti con Provvedimento
				// ==============================================
				// [fuori dal ciclo x ordinamento]

				// ==============================================
				// Recupero i Benefici in Sentenza NON revocati
				// ==============================================
				siesLogger.debug("Recupero benefici Concessi x Titolo: " + lDescTitolo);
				if (!isPenaSospesa && (lSSCumModel == null || lSSCumModel.getIsRevocata())) {
					siesLogger.debug("Pena NON Sospesa! Recupero benefici Concessi in sentenza ");

					Vector<String> lCodTipiBen = new Vector<>();
					lCodTipiBen.add("03"); // Indulto
					lCodTipiBen.add("04"); // Amnistia

					// lCodTipiBen.add("01"); // Sospenzione Condizionale

					lBeneficioSqlDao = new BeneficioCumuloSqlDAO(lConn);
					lBeneficioSqlDao.ricercaBeneficioCumuloByTitoloCum(lTitolo.getIdTitoloCumulato(), "C",
							lCodTipiBen);
					lBeneficioSqlDao.start();

					// BeneficioCumuloModel lSospCond = null;
					while (lBeneficioSqlDao.next()) {
						BeneficioCumuloModel lBeneficio = (BeneficioCumuloModel) lBeneficioSqlDao.getModel();
						lCalcoloPenaModel.addBeneficio(lBeneficio);
					}
					lBeneficioSqlDao.stop();
				} else {
					siesLogger.debug("Pena Sospesa! NON recupero i benefici Concessi in sentenza ");
				}

				// ==============================================
				// Recupero i Benefici Revocati in Sentenza
				// ==============================================
				siesLogger.debug("Recupero benefici Revocati x Titolo: " + lDescTitolo);

				if (!isPenaSospesa) {
					siesLogger.debug("Pena NON Sospesa! Recupero benefici Revocati in sentenza ");

					Vector<String> lCodTipiBen = new Vector<>();
					lCodTipiBen.add("03"); // Indulto
					// lCodTipiBen.add("04"); // Amnistia

					// lBeneficioSqlDao = new BeneficioCumuloSqlDAO (lConn);
					if (lBeneficioSqlDao == null)
						lBeneficioSqlDao = new BeneficioCumuloSqlDAO(lConn);

					lBeneficioSqlDao.ricercaBeneficioCumuloByTitoloCum(lTitolo.getIdTitoloCumulato(), "R",
							lCodTipiBen);
					lBeneficioSqlDao.start();

					while (lBeneficioSqlDao.next()) {
						BeneficioCumuloModel lBeneficio = (BeneficioCumuloModel) lBeneficioSqlDao.getModel();

						lCalcoloPenaModel.addBeneficio(lBeneficio);
					}
					// Ticket#202603160115 - si stoppa subito il dao/sqldao. Se non si chiude subito il cursore verrà chiuso 
				    // solo nel finally ed essendo nel ciclo sui titoli si rischia da avare parecchi cursori aperti contemporaneamente 
					lBeneficioSqlDao.stop();
					// Ticket#202603160115 - FINE
				} else {
					siesLogger.debug("Pena Sospesa! NON Recupero benefici Revocati in sentenza ");
				}

				// ======================================================================
				// Recupero i dati dei provvedimenti concessione benefici
				// Amnistia/Indulto/Depenalizzazione
				// ======================================================================
				// FIXME per ora carico tutti i provvedimenti ma va messo un filtro x classi
				// di interesse perchè c'è il rischio che gli utenti carichino TUTTI i
				// provvedimenti anche quelli inutili
				if (!isPenaSospesa && (lSSCumModel == null || lSSCumModel.getIsRevocata())) {
					siesLogger.debug("Pena NON Sospesa! Recupero i Provvedimenti con i computi");
					siesLogger.debug("=================================================");
					siesLogger.debug(" Recupero i provvedimenti dello stato esecuzione ");
					siesLogger.debug("=================================================");
					lStatoEsecSqlDao = new StatoEsecTitoloCumulatoSqlDAO(lConn);

					lStatoEsecSqlDao.ricercaStatoEsecTitoloCumulatoByIdTitolo(lTitolo.getIdTitoloCumulato(),
							null);

					Vector<StatoEsecTitoloCumulatoModel> lListaProvvedimentiCumulo = new Vector<StatoEsecTitoloCumulatoModel>(
							lStatoEsecSqlDao.getModels());
					lStatoEsecSqlDao.stop();

					siesLogger
							.debug("lListaProvvedimentiCumulo.size() = " + lListaProvvedimentiCumulo.size());

					siesLogger.debug("Inizio Recupero i computi...");
					lComputiSqlDao = new ComputiCumuloSqlDAO(lConn);
					for (int i = 0; i < lListaProvvedimentiCumulo.size(); i++) {
						StatoEsecTitoloCumulatoModel lStatoModel = lListaProvvedimentiCumulo.elementAt(i);

						siesLogger.debug("id: " + lStatoModel.getIdStatoEsecTitoloCumulato() + " ["
								+ lStatoModel.getCodTipoEvento() + "-" + lStatoModel.getCodTipoProvvedimento()
								+ "-" + lStatoModel.getCodMotivo() + "-" + lStatoModel.getDescrMotivo()
								+ "]");

						lComputiSqlDao.ricercaComputiCumuloByIdStatoEsec(
								lStatoModel.getIdStatoEsecTitoloCumulato());

						Vector<ComputiCumuloModel> lListaComputi = new Vector<ComputiCumuloModel>(
								lComputiSqlDao.getModels());
						siesLogger.debug("Computi trovati = " + lListaComputi.size());

						lStatoModel.setListaComputi(lListaComputi);
						lComputiSqlDao.stop();

						// FIXME se non presenti computi ha senso aggiungere il provvedimento?
						lCalcoloPenaModel.addProvvedimento(lStatoModel);
					}
				} // End if pena sospensa
				else {
					siesLogger.debug("Pena Sospesa! NON Recupero i provvedimenti con Computi ");
					if (isPenaDetentivaSospesa) {
						siesLogger.debug(
								"Sospesa la sola detentiva, recupero se presenti le annotazioni pagamento Pena Pecuniaria");

						StatoEsecTitoloCumulatoModel lStatoRicerca = new StatoEsecTitoloCumulatoModel();

						lStatoRicerca.setCodTipoEvento("01");
						lStatoRicerca.setCodTipoProvvedimento("25");
						lStatoRicerca.setCodMotivo("1007");
						lStatoRicerca.setTitIdTitoloCumulato(lTitolo.getIdTitoloCumulato());

						lStatoEsecSqlDao = new StatoEsecTitoloCumulatoSqlDAO(lConn);
						lStatoEsecSqlDao.ricercaStatoEsecTitoloCumulato(lStatoRicerca);

						Vector<StatoEsecTitoloCumulatoModel> lListaProvvPagamentoPP = new Vector(
								lStatoEsecSqlDao.getModels());
						
						lComputiSqlDao = new ComputiCumuloSqlDAO(lConn);
						for (int i = 0; i < lListaProvvPagamentoPP.size(); i++) {
							StatoEsecTitoloCumulatoModel lStatoModel = lListaProvvPagamentoPP.elementAt(i);

							lComputiSqlDao.ricercaComputiCumuloByIdStatoEsec(
									lStatoModel.getIdStatoEsecTitoloCumulato());

							Vector<ComputiCumuloModel> lListaPeriodi = new Vector(lComputiSqlDao.getModels());

							lStatoModel.setListaComputi(lListaPeriodi);
							lComputiSqlDao.stop();

							lCalcoloPenaModel.addProvvedimento(lStatoModel);
						}
					}
				}

				// ==============================================
				// Recupero i provvedimenti di LA
				// ==============================================
				if (!isPenaSospesa && (lSSCumModel == null || lSSCumModel.getIsRevocata())) {
					siesLogger.debug("Pena NON Sospesa! Recupero le LA");
					lLibAnticSqlDao = new LibAnticipataCumuloSqlDAO(lConn);
					lLibAnticSqlDao.ricercaLibAnticipataCumuloByIdTitolo(lTitolo.getIdTitoloCumulato());

					lLibAnticSqlDao.start();
					while (lLibAnticSqlDao.next()) {
						LibAnticipataCumuloModel lLibAnticipata = (LibAnticipataCumuloModel) lLibAnticSqlDao
								.getModel();
						lCalcoloPenaModel.addLibAnticipata(lLibAnticipata);
					}
					// Ticket#202603160115 - si stoppa subito il dao/sqldao (ERR-new in ciclo for nel finally si può 
					// chiudere solo l'ultimo dao) Inoltre resterebbero comunque apertitutti i cursori (uno per ogni titolo) 
					// fino al finally
					lLibAnticSqlDao.stop();
					// Ticket#202603160115 – FINE
				} // End if pena sospensa
				else {
					siesLogger.debug("Pena Sospesa! NON Recupero i provvedimento con LA ");
				}
			} // Fine ciclo sui titoli

			// ========================================================================
			// Recupero le Misure Cautelari fuori dal ciclo x poterle ordinare per data
			// Riportate in ciclo. L'ordinamento non può essere fatto solo sulle MC
			// ========================================================================
			// lMisCautCumSqlDao = new MisuraCautelareCumuloSqlDAO(lConn);
			// lMisCautCumSqlDao.ricercaMisuraCautelareCumuloByIdIstruttoria (aIdIstruttoriaCumulo);
			// Vector <MisuraCautelareCumuloModel> lListaMC = new Vector<MisuraCautelareCumuloModel>
			// (lMisCautCumSqlDao.getModels());
			// lCalcoloPenaModel.setListaMisureCautelari(lListaMC);

			// ========================================================================
			// Recupero la Richieste al GE con anticipazione COD_TIPO_RICHIESTA = 01:
			// - Amnistia (003)
			// - Indulto (002)
			// - Depenalizzazione (004)
			// Richiesta revoca beneficio (021)
			//
			// Recupero la Richieste alla SORV con anticipazione COD_TIPO_RICHIESTA = 02:
			// - Revoca LA ()
			// ========================================================================
			// n.b. Le richieste con Anticicpazione ma senza decisione
			// Le decisioni
			// ========================================================================
			siesLogger.debug("================================ ");
			siesLogger.debug("Recupero le richieste al GE/SORV ");
			siesLogger.debug("================================ ");

			// if (aIdTitolo==null) {
			if (aComputaRichieste) {
				lRichPmInCumuloSqlDao = new RichiestePmInCumuloSqlDAO(lConn);
				lRichPmInCumuloSqlDao.ricercaRichiestePmInCumuloByIdIstruttoria(aIdIstruttoriaCumulo);

				Vector<RichiestePmInCumuloModel> lListaRichieste = new Vector<RichiestePmInCumuloModel>(
						lRichPmInCumuloSqlDao.getModels());
				
				lProvvGeSorvSqlDao = new ProvvedimentoGeSorvCumSqlDAO(lConn);
				for (int i = 0; i < lListaRichieste.size(); i++) {
					RichiestePmInCumuloModel lRichiestaModel = lListaRichieste.elementAt(i);

					// Rimuovo le richieste che non concorrono al calcolo
					if (1 == 2 || (!"002".equals(lRichiestaModel.getCodTipoAnnotazione()) // Indulto
							&& !"003".equals(lRichiestaModel.getCodTipoAnnotazione()) // Amnistia
							&& !"004".equals(lRichiestaModel.getCodTipoAnnotazione()) // Depenalizzazione
							&& !"021".equals(lRichiestaModel.getCodTipoAnnotazione()) // Revoca Beneficio
							&& !"013".equals(lRichiestaModel.getCodTipoAnnotazione()) // Incostituzionalità
							&& !"017".equals(lRichiestaModel.getCodTipoAnnotazione()) // Illecito
																						// amministrativo

							&& !"020".equals(lRichiestaModel.getCodTipoAnnotazione()) // Revoca LA
							&& !"023".equals(lRichiestaModel.getCodTipoAnnotazione()) // Revoca Sanzione
																						// Sostitutiva
					)) {
						siesLogger.debug("Rimuovo la richiesta: [" + lRichiestaModel.getCodTipoAnnotazione()
								+ "," + lRichiestaModel.getFlagAppProvvisoria() + "] " + "[id: "
								+ lRichiestaModel.getIdRichiestePmInCumulo() + "], "
								+ lRichiestaModel.getDescrTipoAnnotazione() + ". ");
						lListaRichieste.remove(i);
						i--;
					} else {
						// Verifico se presente decisione
						siesLogger.debug("Richiesta " + lRichiestaModel.getIdRichiestePmInCumulo() + "-"
								+ lRichiestaModel.getCodTipoAnnotazione() + "-"
								+ lRichiestaModel.getDescrTipoAnnotazione()
								+ ": Verifico se presente la decisione");
						lProvvGeSorvSqlDao.ricercaProvvedimentoGeSorvCumByIdRichiesta(
								lRichiestaModel.getIdRichiestePmInCumulo());
						ProvvedimentoGeSorvCumModel lDecisioneModel = (ProvvedimentoGeSorvCumModel) lProvvGeSorvSqlDao
								.getModelByKey();
						
						if (lDecisioneModel == null && !"A".equals(lRichiestaModel.getFlagAppProvvisoria())) {
							siesLogger.debug("Richiesta " + lRichiestaModel.getDescrTipoAnnotazione()
									+ " senza Anticipazione e Decisione assente: la rimuovo");
							lListaRichieste.remove(i);
							i--;
						} else if (lDecisioneModel != null) {
							siesLogger.debug("Richiesta con decisione: aggiungo la decisione "
									+ lDecisioneModel.getIdProvvedimentoGeSorvCum() + " alla richiesta "
									+ lRichiestaModel.getIdRichiestePmInCumulo());
							lRichiestaModel.setDecisioneGeSorvCum(lDecisioneModel);
						} else {
							siesLogger.debug("Richiesta senza decisione ma con Anticipazione. La lascio.");
						}
					}
				}

				siesLogger.debug("Lista richieste dopo rimozione " + lListaRichieste.size());
				for (RichiestePmInCumuloModel lRichiestaModel : lListaRichieste) {
					String lLogRichDec = "";
					lLogRichDec += lRichiestaModel.getIdRichiestePmInCumulo();
					lLogRichDec += " - " + lRichiestaModel.getCodTipoAnnotazione();
					lLogRichDec += " - " + lRichiestaModel.getDescrTipoAnnotazione();
					lLogRichDec += " - " + lRichiestaModel.getFlagAppProvvisoria();

					ProvvedimentoGeSorvCumModel lDecisioneModel = lRichiestaModel.getDecisioneGeSorvCum();
					if (lDecisioneModel == null)
						lLogRichDec += " - DECISIONE: ASSENTE";
					else
						lLogRichDec += " - DECISIONE: " + lDecisioneModel.getIdProvvedimentoGeSorvCum();

					siesLogger.debug(lLogRichDec);
				}
				// Aggiungo la lista delle richieste
				siesLogger.debug("Aggiungo la lista delle richieste");
				lCalcoloPenaModel.setListaRichiestePM(lListaRichieste);

			} // end Computa RIchieste
		} catch (DAOException daoEx) {
			siesLogger.error("DAOException: ", daoEx);
			throw new F3BException(
					"IstruttoriaCumuloController.ExCalcolaPenaCumuloByIstruttoria: Non posso leggere : "
							+ daoEx);
		} catch (Exception ex) {
			siesLogger.error("Exception: ", ex);
			throw new F3BException(
					"IstruttoriaCumuloController.ExCalcolaPenaCumuloByIstruttoria: Non posso leggere : "
							+ ex);
		} finally {
			cleanup(lTitoloSqlDao);
			cleanup(lProcCumSqlDao);
			cleanup(lPenSqlDao);
			cleanup(lSSCumSqlDao);
			cleanup(lMisCautCumSqlDao);
			cleanup(lBeneficioSqlDao);
			cleanup(lLibAnticSqlDao);
			cleanup(lStatoEsecSqlDao);
			cleanup(lComputiSqlDao);
			cleanup(lRichPmInCumuloSqlDao);
			cleanup(lProvvGeSorvSqlDao);
			// Scheda Intervento n° 6 - Ottimizzazione SIUS Avvocati
			cleanup(lUffSqlDao);
			// MEV_2025-48 - ALTRO - Ordinamento titoli come x Lista titoli
			cleanup(lIstrSqlDao);
			// MEV_2025-48 - ALTRO - Visualizzazione Pena In Continuazione
			cleanup(lContCumuloSqlDao);

			cleanup(lConn);
		}

		return lCalcoloPenaModel;
	}

	/*
	 * MEV_2025-48 - Aggiunta ricerca per chiaveAnno/chiaveProgr e ufficio accorpato: Aggiunto
	 * FascicoloSiepModel
	 */
	// public BigDecimal ExCountFascicoliBySoggettoProprioUfficioPaged(SoggettoModel aSogModel,
	// String lCodUfficioUtenteConnesso, int aPage) throws F3BException {
	public BigDecimal ExCountFascicoliBySoggettoProprioUfficioPaged(SoggettoModel aSogModel,
			FascicoloSiepModel aFascModel, String lCodUfficioUtenteConnesso, int aPage) throws F3BException {

		siesLogger.debug("--XX-- ExCountFascicoliBySoggettoProprioUfficioPaged ...... INIZIO  ");
		Connection lConn = null;
		BigDecimal HowManyRecords = null;
		FascicoloSiepSoggettoSqlDAO lFSoggSqlDao = null;

		try {
			lConn = getDBConnection();
			lFSoggSqlDao = new FascicoloSiepSoggettoSqlDAO(lConn);
			/*
			 * MEV_2025-48 - Aggiunta ricerca per chiaveAnno/chiaveProgr e ufficio accorpato: Aggiunto
			 * FascicoloSiepModel
			 */
			// lFSoggSqlDao.RicercaFascicoliBySoggettoProprioUfficioPaged(aSogModel,
			// lCodUfficioUtenteConnesso,
			// aPage);
			lFSoggSqlDao.RicercaFascicoliBySoggettoProprioUfficioPaged(aSogModel, aFascModel,
					lCodUfficioUtenteConnesso, aPage);
			lFSoggSqlDao.start();
			lFSoggSqlDao.next();
			HowManyRecords = lFSoggSqlDao.getBigDecimal("HowManyRecords");
			// siesLogger.debug("--XX-- totale record = "+HowManyRecords );

		} catch (DAOException daoEx) {
			rollback(lConn);
			siesLogger.error("DAOException: ", daoEx);
			throw new SIEPException(F3BException.USER_MESSAGE,
					"IstruttoriaCumuloController.ExCountFascicoliBySoggettoProprioUfficioPaged: " + daoEx);
		} catch (Exception e) {
			rollback(lConn);
			siesLogger.error("Exception: ", e);
			throw new SIEPException(F3BException.USER_MESSAGE, e.getMessage());
		} finally {
			cleanup(lFSoggSqlDao);
			cleanup(lConn);
		}

		return HowManyRecords;
	}

	/*
	 * MEV_2025-48 - Aggiunta ricerca per chiaveAnno/chiaveProgr e ufficio accorpato: Aggiunto
	 * FascicoloSiepModel
	 */
	// public Vector<FascicoloSiepModel> ExRicercaFascicoliBySoggettoProprioUfficioPaged(SoggettoModel
	// aSogModel,
	// String lCodUfficioUtenteConnesso, int aPage, BigDecimal lIdIstruttoria
	// ) throws F3BException {
	public Vector<FascicoloSiepModel> ExRicercaFascicoliBySoggettoProprioUfficioPaged(SoggettoModel aSogModel,
			FascicoloSiepModel aFascModel, // Aggiunto parametro
			String lCodUfficioUtenteConnesso, int aPage, BigDecimal lIdIstruttoria) throws F3BException {

		siesLogger.debug("--XX-- ExRicercaFascicoliBySoggettoProprioUfficioPaged ...... INIZIO  ");
		Connection lConn = null;
		Vector lFascicoli = new Vector();

		FascicoloSiepSoggettoSqlDAO lFSoggSqlDao = null;
		FascicoloSiepModel lFascicolo = null;

		ProcedimentoCumulatoSqlDAO lProcSqlDao = null;

		try {
			lConn = getDBConnection();

			lFSoggSqlDao = new FascicoloSiepSoggettoSqlDAO(lConn);
			lProcSqlDao = new ProcedimentoCumulatoSqlDAO(lConn);
			/*
			 * MEV_2025-48 - Aggiunta ricerca per chiaveAnno/chiaveProgr e ufficio accorpato: Aggiunto
			 * FascicoloSiepModel
			 */
			// lFSoggSqlDao.RicercaFascicoliBySoggettoProprioUfficioPaged(aSogModel,
			// lCodUfficioUtenteConnesso,
			// aPage);
			lFSoggSqlDao.RicercaFascicoliBySoggettoProprioUfficioPaged(aSogModel, aFascModel,
					lCodUfficioUtenteConnesso, aPage);
			lFSoggSqlDao.start();
			while (lFSoggSqlDao.next()) {
				lFascicolo = (FascicoloSiepModel) lFSoggSqlDao.getModelFascSoggSentMioUfficio();

				if (lFascicolo != null && lFascicolo.getIdFascicoloSiep() != null) {
					ProcedimentoCumulatoModel lprocModel = new ProcedimentoCumulatoModel();
					lprocModel.setChiaveAnnoFasCumulato(lFascicolo.getChiaveAnno());
					lprocModel.setChiaveProgrFasCumulato(lFascicolo.getChiaveProgr());
					lprocModel.setCodUfficioFasCumulato(lFascicolo.getChiaveUfficio());

					lProcSqlDao.ricercaProcedimentoCumulatoInIstruttoria(lprocModel, lIdIstruttoria);
					lProcSqlDao.start();
					if (lProcSqlDao.next()) {
						lprocModel = (ProcedimentoCumulatoModel) lProcSqlDao.getModel();
						if (lprocModel != null && lprocModel.getIdProcedimentoCumulato() != null) {
							lFascicolo.setProcedimentoCumulato(lprocModel);
							lFascicolo.setgiaInIstruttoria("SI");
						}

					}

					lProcSqlDao.stop();
				}

				lFascicoli.add(lFascicolo);
			}

			lFSoggSqlDao.stop();

			/* 2026-03 si gestisce il risultato lato action 
			if (lFascicoli.isEmpty())
				throw new SIEPException(F3BException.USER_MESSAGE,
						"Nessun Soggetto individuato con i criteri di ricerca selezionati! ");
						*/
		} catch (DAOException daoEx) {
			rollback(lConn);
			siesLogger.error("DAOException: ", daoEx);
			throw new SIEPException(F3BException.USER_MESSAGE,
					"IstruttoriaCumuloController.ExRicercaFascicoliBySoggettoProprioUfficioPaged: " + daoEx);
		} catch (Exception e) {
			rollback(lConn);
			siesLogger.error("Exception: ", e);
			throw new SIEPException(F3BException.USER_MESSAGE, e.getMessage());
		} finally {
			cleanup(lProcSqlDao);
			cleanup(lFSoggSqlDao);
			cleanup(lConn);
		}

		return lFascicoli;
	}

	public Vector<EsitoArchiviazioniCumuloModel> ExRicercaEsitoArchiviazionideiCumulatiByIdEvento(
			BigDecimal aIdEvento) throws F3BException {

		siesLogger.debug("--XX-- ExRicercaEsitoArchiviazionideiCumulatiByIdEvento ...... INIZIO  ");
		Connection lConn = null;
		Vector<EsitoArchiviazioniCumuloModel> VecEsiti = null;

		EsitoArchiviazioniCumuloSqlDAO lEsiSqlDao = null;
		FascicoloSiepSqlDAO lFasSqlDao = null;
		EsitoArchiviazioniCumuloModel lEsiModel = null;
		FascicoloSiepModel lFascicoloMod = null;

		try {
			lConn = getDBConnection();
			lEsiSqlDao = new EsitoArchiviazioniCumuloSqlDAO(lConn);
			lEsiSqlDao.ricercaEsitoArchiviazioniCumuloByEveIdEvento(aIdEvento);
			VecEsiti = new Vector<EsitoArchiviazioniCumuloModel>(lEsiSqlDao.getModels());

			if (VecEsiti != null && VecEsiti.size() > 0) {
				Iterator Itx = VecEsiti.iterator();
				while (Itx.hasNext()) {
					lEsiModel = (EsitoArchiviazioniCumuloModel) Itx.next();
					if (lEsiModel != null && lEsiModel.getFasIdFascicoloSiep() != null) {
						lFasSqlDao = new FascicoloSiepSqlDAO(lConn);

						lFasSqlDao.ricercaFascicoloByKey(lEsiModel.getFasIdFascicoloSiep());
						lFascicoloMod = (FascicoloSiepModel) lFasSqlDao.getModelByKey();
						if (lFascicoloMod != null && lFascicoloMod.getIdFascicoloSiep() != null) {
							lEsiModel.setCodStatoFascAttuale(lFascicoloMod.getCodStatoFascicolo());
							lEsiModel.setDescrizione(lFascicoloMod.getDescrStatoFascicolo());

							/* MEV_2025-48 */
							lEsiModel.setChiaveProgrOrig(lFascicoloMod.getChiaveProgrOrig());
						}
					}
				}
			}
		} catch (DAOException daoEx) {
			rollback(lConn);
			siesLogger.error("DAOException: ", daoEx);
			throw new SIEPException(F3BException.USER_MESSAGE,
					"IstruttoriaCumuloController.ExRicercaEsitoArchiviazionideiCumulatiByIdEvento: " + daoEx);
		} catch (Exception e) {
			rollback(lConn);
			siesLogger.error("Exception: ", e);
			throw new SIEPException(F3BException.USER_MESSAGE,
					"IstruttoriaCumuloController.ExRicercaEsitoArchiviazionideiCumulatiByIdEvento"
							+ e.getMessage());
		} finally {
			cleanup(lEsiSqlDao);
			cleanup(lFasSqlDao);
			cleanup(lConn);
		}

		return VecEsiti;
	}

	public IstruttoriaCumuloModel ExRicercaIstruttoriaCumuloApertaByIdFasSiep(BigDecimal aIdFascicoloSiep)
			throws F3BException {

		IstruttoriaCumuloModel IstruModel = null;
		Connection lConn = null;
		IstruttoriaCumuloSqlDAO IstruCumSqldao = null;

		try {
			lConn = getDBConnection();
			IstruCumSqldao = new IstruttoriaCumuloSqlDAO(lConn);
			IstruCumSqldao.RicercaIstruttoriaCumuloApertaByIdFasSiep(aIdFascicoloSiep);

			IstruModel = (IstruttoriaCumuloModel) IstruCumSqldao.getModelByKey();

		} catch (DAOException daoEx) {
			rollback(lConn);
			siesLogger.error("DAOException: ", daoEx);
			throw new SIEPException(F3BException.USER_MESSAGE,
					"IstruttoriaCumuloController.ExRicercaIstruttoriaCumuloApertaByIdFasSiep: " + daoEx);
		} catch (Exception e) {
			rollback(lConn);
			siesLogger.error("Exception: ", e);
			throw new SIEPException(F3BException.USER_MESSAGE,
					"IstruttoriaCumuloController.ExRicercaIstruttoriaCumuloApertaByIdFasSiep"
							+ e.getMessage());
		} finally {
			cleanup(IstruCumSqldao);
			cleanup(lConn);
		}

		return IstruModel;
	}

	/**
	 * Metodo che verifica se per un titolo è presente una sospensione condizionale ancora attiva, non
	 * revocata. Se presente e attiva restituisce il record, altrimenti non restiuisce nulla. La verifica
	 * viene effettuate cercando nell'ordine: - Provvedimenti di annotazione revoca - Eventuale collegamento
	 * ad altro fascicolo fascicolo_siep.FAS_SIE_ID_FASCICOLO_SIEP - Altro titolo a sistema con annotata una
	 * revoca in sentenza per un titolo "uguale" a quello in fase di caricamento - La presenza di una
	 * richiesta di revoca in istruttoria con anticipazione o con decisione favorevole
	 *
	 * NOTA: L'annotazione revoca sul classe I derivato dal classe III non è sempre presente in quanto
	 * aggiunta di recente in fase di creazione del classe I E' presente invece il collegamento con il classe
	 * III E' prassi comune degli uffici non iscrivere il classe III ma direttamente il classe I in caso di
	 * revoca in sentenza altro titolo e annotare la revoca a testo libero come NOTA del fascicolo, non
	 * essendo possibile registrare un evento tipo annotazione revoca sui classe I (bloccato) Il risultato è
	 * che è fortemente possibile che un classe I con sospensiva in sentenza NON abbia alcuna indicazione
	 * (evento annotazione o collegamento al classe III) del fatto che la sospensiva sia stata revocata. In
	 * questo caso l'unica possibilità è trovare a sistema
	 *
	 * @param aIdTitolo
	 * @param aConn
	 * @return BeneficioCumuloModel se beneficio attivo, null se assente o revocato
	 * @throws F3BException
	 */
	public BeneficioCumuloModel isPenaSospesa(BigDecimal aIdTitolo, Connection aConn) throws F3BException {

		boolean isSospensioneRevocata = false;

		BeneficioCumuloModel lSospCond = null;

		BeneficioCumuloSqlDAO lBeneficioSqlDao = null;
		StatoEsecTitoloCumulatoSqlDAO lStatoEsecSqlDao = null;
		RichiestePmInCumuloSqlDAO lRichPmSqlDao = null;
		RichPMBeneficioCumSqlDAO lRicPmBenSqlDao = null;
		ProvvedimentoGeSorvCumSqlDAO lProvvGeSorvSqlDao = null;
		TitoloCumulatoSqlDAO lTitoloSqlDao = null;

		try {
			// Verifico se presente il beneficio della sospensiva in sentenza
			siesLogger.debug(
					"Verifico se presente il beneficio della sospensiva in sentenza IdTitolo=" + aIdTitolo);
			Vector<String> lCodTipiBen = new Vector<>();
			lCodTipiBen.add("01"); // Sospenzione Condizionale

			lBeneficioSqlDao = new BeneficioCumuloSqlDAO(aConn);
			lBeneficioSqlDao.ricercaBeneficioCumuloByTitoloCum(aIdTitolo, "C", lCodTipiBen);
			lBeneficioSqlDao.start();

			while (lBeneficioSqlDao.next()) {
				BeneficioCumuloModel lBeneficio = (BeneficioCumuloModel) lBeneficioSqlDao.getModel();

				if ("01".equals(lBeneficio.getCodTipoBeneficio())) {
					siesLogger.debug("Trovato beneficio " + lBeneficio.getIdBeneficioCumulo() + " in stato "
							+ lBeneficio.getFlagStato());
					if (!"C".equalsIgnoreCase(lBeneficio.getFlagStato())) // presente e non cancellato
																			// (logica)
						lSospCond = lBeneficio;
				}
			}
			lBeneficioSqlDao.stop();

			// Sospensione assente esco
			if (lSospCond == null) {
				siesLogger.debug("Sospensione Condizionale NON presente in sentenza");
				return null;
			} else {
				siesLogger.debug(
						"Presente Sospensione Condizionale: [" + lSospCond.getIdBeneficioCumulo() + "]");
				siesLogger.debug("Verifico se revocata...");
			}

			// ========================================================================
			// Eventuale Revoca in sentenza su altro Titolo
			// ========================================================================
			if (lSospCond.getTitIdTitoloCumulatoCollegato() != null) {
				// TitIdTitoloCumulato se valorizzato punta il titolo (in istruttoria)
				// che revoca il beneficio
				siesLogger.debug("Beneficio Revocato in sentenza dal titolo: "
						+ lSospCond.getTitIdTitoloCumulatoCollegato());

				lSospCond.setIsRevocato(true);

				// Recupero i dati del titolo per la stampa
				lTitoloSqlDao = new TitoloCumulatoSqlDAO(aConn);
				lTitoloSqlDao.ricercaTitoloCumulatoByKey(lSospCond.getTitIdTitoloCumulatoCollegato());
				TitoloCumulatoModel lTitoloRevocante = (TitoloCumulatoModel) lTitoloSqlDao.getModelByKey();
				lTitoloSqlDao.stop();

				if (lTitoloRevocante != null) {
					String lStringaRevoca = "Beneficio revocato con sentenza N. "
							+ lTitoloRevocante.getNumeroSentenza() + "/" + lTitoloRevocante.getAnnoSentenza();
					if (lTitoloRevocante.getAnnoRegGen() != null) {
						lStringaRevoca += " - Reg. Gen n. " + lTitoloRevocante.getNumeroRegGen() + "/"
								+ lTitoloRevocante.getAnnoRegGen() + " " + lTitoloRevocante.getTipoRegGen();
					}
					if (lTitoloRevocante.getAnnoRegePm() != null) {
						lStringaRevoca += " - R.G.N.R. n.  " + lTitoloRevocante.getNumeroRegePm() + "/"
								+ lTitoloRevocante.getAnnoRegePm() + ", ";
					}
					lStringaRevoca += " emessa in data " + DateUtils
							.getDateToString(lTitoloRevocante.getDataProvvedimento(), "dd-MM-yyyy");
					lStringaRevoca += " da " + lTitoloRevocante.getDescrTipoAutoritaEmittente() + " di "
							+ lTitoloRevocante.getDescrLuogoEmittente();
					lStringaRevoca += " definitiva il " + DateUtils
							.getDateToString(lTitoloRevocante.getDataIrrevocabilita(), "dd-MM-yyyy");

					lSospCond.setStringaRevoca(lStringaRevoca);

					siesLogger.debug("Stringa Revoca = " + lSospCond.getStringaRevoca());
				}
				return lSospCond;
			}

			// ========================================================================
			// Eventuale annotazione di revoca (da classe III)
			// ========================================================================
			if (!isSospensioneRevocata) {
				siesLogger.debug("Verifico se revocata con annotazione...");
				// Non trovata la revoca su titolo in istruttoria. Cerco se presente
				// una annotazione di revoca sullo stato esecuzione del titolo

				lStatoEsecSqlDao = new StatoEsecTitoloCumulatoSqlDAO(aConn);

				Vector<String> listaTipoProvv = new Vector<>();
				listaTipoProvv.add("25"); // Annotazione

				Vector<String> listaMotivoProvv = new Vector<>();
				listaMotivoProvv.add("1100");
				listaMotivoProvv.add("1101");
				listaMotivoProvv.add("1102");
				listaMotivoProvv.add("1103");
				listaMotivoProvv.add("1104");
				listaMotivoProvv.add("1105");
				listaMotivoProvv.add("1106");
				listaMotivoProvv.add("1107");
				listaMotivoProvv.add("1108");

				StatoEsecTitoloCumulatoModel lStatoRicerca = new StatoEsecTitoloCumulatoModel();
				lStatoRicerca.setCodTipoEvento("01");
				lStatoRicerca.setTitIdTitoloCumulato(aIdTitolo);

				lStatoEsecSqlDao.ricercaStatoEsecTitoloCumulatobylisteTipoMotivoProvv(lStatoRicerca,
						listaTipoProvv, listaMotivoProvv);

				Vector<StatoEsecTitoloCumulatoModel> lListaProvvedimentiCumulo = new Vector<>();
				lListaProvvedimentiCumulo = new Vector(lStatoEsecSqlDao.getModels());

				if (lListaProvvedimentiCumulo != null && lListaProvvedimentiCumulo.size() > 0) {
					siesLogger.debug("Beneficio Revocato con annotazione provvedimento!! ");

					lSospCond.setIsRevocato(true);
					lSospCond.setStringaRevoca("Beneficio Revocato");
					for (StatoEsecTitoloCumulatoModel lProvvRevoca : lListaProvvedimentiCumulo) {
						siesLogger.debug("Provv revoca: " + lProvvRevoca.getIdStatoEsecTitoloCumulato()
								+ " - " + lProvvRevoca.getIdEventoOrigine());
					}
					return lSospCond;
				}
			}

			// Verifico se valorizzato FASCICOLO_SIEP.FAS_SIE_ID_FASCICOLO_SIEP
			if (!isSospensioneRevocata) {
				// Per ora non ho il dato! Va caricato su PROCEDIMENTO_CUMULATO
			}

			// ========================================================================
			// Verifico se Presente Decisione del GE - Altre Ordinanze/Decreti
			// - Revoca Altri Provvedimenti (art. 674 c.p.p.)
			// - Revoca sospensinoe condizionale della pena (0818)
			// ========================================================================
			if (!isSospensioneRevocata) {
				siesLogger.debug("Verifico se revocata con Provv del GE...");

				lStatoEsecSqlDao = new StatoEsecTitoloCumulatoSqlDAO(aConn);

				Vector<String> listaTipoProvv = new Vector<>();
				listaTipoProvv.add("02"); // Decreto
				listaTipoProvv.add("03"); // Ordinanza

				Vector<String> listaMotivoProvv = new Vector<>();
				listaMotivoProvv.add("0818");

				StatoEsecTitoloCumulatoModel lStatoRicerca = new StatoEsecTitoloCumulatoModel();
				lStatoRicerca.setCodTipoEvento("01");
				lStatoRicerca.setTitIdTitoloCumulato(aIdTitolo);

				lStatoEsecSqlDao.ricercaStatoEsecTitoloCumulatobylisteTipoMotivoProvv(lStatoRicerca,
						listaTipoProvv, listaMotivoProvv);

				Vector<StatoEsecTitoloCumulatoModel> lListaProvvedimentiCumulo = new Vector<>();
				lListaProvvedimentiCumulo = new Vector(lStatoEsecSqlDao.getModels());

				if (lListaProvvedimentiCumulo != null && lListaProvvedimentiCumulo.size() > 0) {
					siesLogger.debug("Presenti Ordinanza/Decreti di Revoca del GE. Testo l'esito !! ");

					// testo l'esito 0006 =
					for (StatoEsecTitoloCumulatoModel lStaEsec : lListaProvvedimentiCumulo) {
						siesLogger.debug("Provv revoca: " + lStaEsec.getIdStatoEsecTitoloCumulato() + " - "
								+ lStaEsec.getIdEventoOrigine() + " - Esito Tenore = "
								+ lStaEsec.getCodEsitoTenore());
						if ("0006".equals(lStaEsec.getCodEsitoTenore())) {
							siesLogger.debug("Beneficio Revocato con Provvedimento del GE!! ");
							lSospCond.setIsRevocato(true);
							lSospCond.setStringaRevoca("Beneficio Revocato");

							return lSospCond;
						}
					}
				}
			}

			// Annotazione revoca non trovata sullo stato esecuzione verifico se presente
			// una richiesta in cumulo con anticipazione. Ed eventuale decisione del GE.
			// n.b. per ora NON POSSIBILE con anticipazione ma gestisco ugualmente il caso
			if (!isSospensioneRevocata) {
				// RICHIESTE_PM_IN_CUMULO.COD_TIPO_ANNOTAZIONE = '021' - Revoca Beneficio
				// RICHIESTE_PM_IN_CUMULO.COD_MOTIVO = stessi dell'evento di annotazione
				// RICHIESTE_PM_IN_CUMULO.TIT_ID_TITOLO_CUMULATO = il titolo del beneficio da revocare

				// Decisione del GE Punta la richiesta
				// PROVVEDIMENTO_GE_SORV_CUM.RIC_ID_RICHIESTE_PM_IN_CUMULO
				// PROVVEDIMENTO_GE_SORV_CUM.FLAG_CONFORME = C
				// PROVVEDIMENTO_GE_SORV_CUM.BEN_SOSP_COND = S ovvero revoca delle Sospe Cond
				// infatti si potrebbe richiedere la revoca della sola non menzione
				// PROVVEDIMENTO_GE_SORV_CUM.BEN_NON_MENZIONE = N
				siesLogger
						.debug("Ricerco se presente la richiesta di revoca della Sospensione Condizionale ");

				lRichPmSqlDao = new RichiestePmInCumuloSqlDAO(aConn);
				lProvvGeSorvSqlDao = new ProvvedimentoGeSorvCumSqlDAO(aConn);

				lRichPmSqlDao.ricercaRichiestePmInCumuloByIdTitoloTipoRich(aIdTitolo, "01", "021"); // 01 =
																									// rich al
																									// GE,
																									// 021=
																									// riche
																									// revoca
																									// beneficio

				Vector<RichiestePmInCumuloModel> lListaRichieste = new Vector(lRichPmSqlDao.getModels());
				
				lRicPmBenSqlDao = new RichPMBeneficioCumSqlDAO(aConn);

				// Scorro le richieste revoca beneficio per vedere se ne esiste una
				// relativa al beneficio corrente, se con anticipazione e se esiste
				// la decisione del GE e di che tipo di decisione si tratta.
				for (RichiestePmInCumuloModel lRichiesta : lListaRichieste) {
					siesLogger.debug("Trovata richiesta revoca beneficio: " + lRichiesta);

					// Recupero i benefici puntati dalla richiesta
					lRicPmBenSqlDao
							.ricercaRichPmBeneficioCumByRichIdRich(lRichiesta.getIdRichiestePmInCumulo());

					Vector<RichPMBeneficioCumModel> lVecRicRevBeneCum = new Vector<RichPMBeneficioCumModel>(
							lRicPmBenSqlDao.getModels());

					// Verifico se punta il beneficio della sospensione
					for (RichPMBeneficioCumModel lRichPMBenMod : lVecRicRevBeneCum) {

						if (lRichPMBenMod.getBenIdBeneficioCum()
								.compareTo(lSospCond.getIdBeneficioCumulo()) == 0) {
							siesLogger.debug(
									"La richiesta si riferisce proprio alla beneficio della Sospensione");

							// Cerco se presente la decisione del GE
							lProvvGeSorvSqlDao.ricercaProvvedimentoGeSorvCumByIdRichiesta(
									lRichiesta.getIdRichiestePmInCumulo());

							ProvvedimentoGeSorvCumModel lDecisioneGE = (ProvvedimentoGeSorvCumModel) lProvvGeSorvSqlDao
									.getModelByKey();
							lProvvGeSorvSqlDao.stop();

							if (lDecisioneGE != null) {
								siesLogger.debug("Presente decisione del GE "
										+ lDecisioneGE.getIdProvvedimentoGeSorvCum());
								siesLogger.debug("Esito decisione: " + lDecisioneGE.getFlagConforme());
								// BEN_SOSP_COND=S

								String lStringaRevoca = "Beneficio revocato con Ordinanza N. "
										+ lDecisioneGE.getNumeroProvv() + "/" + lDecisioneGE.getAnnoProvv();
								lStringaRevoca += " emessa in data "
										+ DateUtils.getDateToString(lDecisioneGE.getDataD(), "dd-MM-yyyy");
								lStringaRevoca += " da " + lDecisioneGE.getDescrUfficioEmittente() + " di "
										+ lDecisioneGE.getDescrLuogoEmittente();

								if ("C".equals(lDecisioneGE.getFlagConforme())) {
									// C=conforme,
									siesLogger.debug(
											"Trovata decisione del GE conforme. La Sospensiva è REVOCATA");
									lSospCond.setIsRevocato(true);
									lSospCond.setStringaRevoca(lStringaRevoca);
									return lSospCond;
								} else if ("D".equals(lDecisioneGE.getFlagConforme())
										&& "S".equals(lDecisioneGE.getBenSospCond())) {
									// D=Difforme,
									siesLogger.debug(
											"Trovata decisione del GE difforme ma BEN_SOSP_COND=S. La Sospensiva è REVOCATA");
									lSospCond.setIsRevocato(true);
									lSospCond.setStringaRevoca(lStringaRevoca);
									return lSospCond;
								} else {
									siesLogger.debug(
											"Trovata decisione del GE NON conforme. La Sospensiva è attiva");
								}
							} else {
								siesLogger.debug(
										"Manca la decisione del GE, verifico se la richiesta è con anticipazione");
								if ("A".equals(lRichiesta.getFlagAppProvvisoria())) {
									siesLogger.debug(
											"Manca la decisione del GE, ma richiesta con Anticipazione. La Sospensiva è REVOCATA.");
									return null;
								}
							}
						}
					}

					lRicPmBenSqlDao.stop();
				}
			}
		} catch (DAOException daoEx) {
			siesLogger.error("DAOException: ", daoEx);
			throw new SIEPException(F3BException.USER_MESSAGE,
					"IstruttoriaCumuloController.isPenaSospesaRevocata: " + daoEx);
		} catch (Exception e) {
			siesLogger.error("Exception: ", e);
			throw new SIEPException(F3BException.USER_MESSAGE,
					"IstruttoriaCumuloController.isPenaSospesaRevocata" + e.getMessage());
		} finally {
			cleanup(lBeneficioSqlDao);
			cleanup(lStatoEsecSqlDao);
			cleanup(lRichPmSqlDao);
			cleanup(lProvvGeSorvSqlDao);
			cleanup(lTitoloSqlDao);
			// Scheda Intervento n° 6 - Ottimizzazione SIUS Avvocati
			cleanup(lRicPmBenSqlDao);
		}

		return lSospCond;
	}

	/**
	 * Metodo che verifica se esiste un altro titolo in istruttoria la cui pena complessiva è dichiara in
	 * continuazione con quella del titolo corrente, e il tipo di continuazione e' R
	 *
	 * @param aIdTitolo
	 * @param aConn
	 * @return true se la pena è in continuazione di tipo R, false altrimenti
	 * @throws F3BException
	 */
	public boolean isPenaInContinuazione(BigDecimal aIdTitolo, Connection aConn) throws F3BException {

		boolean isInContinuazione = false;

		ContinuazioneCumuloSqlDAO lContinuazioneCumSqlDao = null;

		try {
			// Verifico se presente il beneficio della sospensiva in sentenza
			siesLogger.debug("Verifico se la pena del titolo IdTitolo=" + aIdTitolo + " è in continuazione");

			lContinuazioneCumSqlDao = new ContinuazioneCumuloSqlDAO(aConn);

			lContinuazioneCumSqlDao.ricercaContinuazioneByIdTitoloCont(aIdTitolo);

			lContinuazioneCumSqlDao.start();

			while (lContinuazioneCumSqlDao.next()) {
				ContinuazioneCumuloModel lConCumModel = (ContinuazioneCumuloModel) lContinuazioneCumSqlDao
						.getModel();

				if ("R".equals(lConCumModel.getCodTipoContinuazione())) {
					siesLogger.debug(
							"Trovata continuazione " + lConCumModel.getIdContinuazioneCum() + " di tipo R ");
					isInContinuazione = true;
				}
			}
			lContinuazioneCumSqlDao.stop();
		} catch (DAOException daoEx) {
			siesLogger.error("DAOException: ", daoEx);
			throw new SIEPException(F3BException.USER_MESSAGE,
					"IstruttoriaCumuloController.isPenaInContinuazione: " + daoEx);
		} catch (Exception e) {
			siesLogger.error("Exception: ", e);
			throw new SIEPException(F3BException.USER_MESSAGE,
					"IstruttoriaCumuloController.isPenaInContinuazione" + e.getMessage());
		} finally {
			cleanup(lContinuazioneCumSqlDao);
		}

		return isInContinuazione;
	}

	/**
	 * Metodo che verifica se una SS risulta revocata e quindi riattiva la PP
	 *
	 * Per ora gestita solo la revoca in instruttori su richiesta: - Richiete PM al GE - Richiesta Revoca SS
	 *
	 * @param aIdTitolo
	 * @param aConn
	 * @return
	 * @throws F3BException
	 */
	public boolean isSSRevocata(BigDecimal aIdIstruttoria, SanzioneSostitutivaCumuloModel aSanzioneSostMod,
			Connection aConn) throws F3BException {

		boolean isSSRevocata = false;

		RichiestePmInCumuloSqlDAO lRichPmSqlDao = null;
		RichPMSanzioneSostCumSqlDAO lRicPmSanSostSqlDao = null;
		ProvvedimentoGeSorvCumSqlDAO lProvvGeSorvSqlDao = null;

		siesLogger.debug("isSSRevocata");

		try {
			siesLogger.debug("Ricerco se presente la richiesta di revoca della Sanzione Sostitutiva ");

			lRichPmSqlDao = new RichiestePmInCumuloSqlDAO(aConn);
			lProvvGeSorvSqlDao = new ProvvedimentoGeSorvCumSqlDAO(aConn);

			lRichPmSqlDao.ricercaRichiestePmInCumuloByIdIstruttoriaTipoRich(aIdIstruttoria, "01", "023"); // 01
																											// =
																											// rich
																											// al
																											// GE,
																											// 021=
																											// Revoca
																											// Sanzioni
																											// Sostitutive

			Vector<RichiestePmInCumuloModel> lListaRichieste = new Vector<RichiestePmInCumuloModel>(
					lRichPmSqlDao.getModels());

			// Scorro le richieste revoca SS per vedere se ne esiste una
			// relativa al beneficio corrente, se con anticipazione e se esiste
			// la decisione del GE e di che tipo di decisione si tratta.
			lRicPmSanSostSqlDao = new RichPMSanzioneSostCumSqlDAO(aConn);
			for (RichiestePmInCumuloModel lRichiesta : lListaRichieste) {
				siesLogger.debug("Trovata richiesta revoca Sanzione Sost: " + lRichiesta);

				// Recupero le SS puntate dalla richiesta
				lRicPmSanSostSqlDao.ricercaRichPmSSCumByRichIdRich(lRichiesta.getIdRichiestePmInCumulo());

				Vector<RichPMSanSostCumModel> lVecRicRevSSCum = new Vector<RichPMSanSostCumModel>(
						lRicPmSanSostSqlDao.getModels());
				
				// Verifico se punta il beneficio della sospensione
				for (RichPMSanSostCumModel lRichPMSSMod : lVecRicRevSSCum) {
					if (lRichPMSSMod.getSanIdSanSostCumulo()
							.compareTo(aSanzioneSostMod.getIdSanzioneSostitutivaCum()) == 0) {
						siesLogger.debug("La richiesta si riferisce proprio alla SS");
						// ==============
						// Cerco se presente la decisione del GE
						lProvvGeSorvSqlDao.ricercaProvvedimentoGeSorvCumByIdRichiesta(
								lRichiesta.getIdRichiestePmInCumulo());

						ProvvedimentoGeSorvCumModel lDecisioneGE = (ProvvedimentoGeSorvCumModel) lProvvGeSorvSqlDao
								.getModelByKey();
						lProvvGeSorvSqlDao.stop();

						if (lDecisioneGE != null) {
							siesLogger.debug("Presente decisione del GE "
									+ lDecisioneGE.getIdProvvedimentoGeSorvCum());
							siesLogger.debug("Esito decisione: " + lDecisioneGE.getFlagConforme());

							// Eventualmente per la stampa
							String lStringaRevoca = "Beneficio revocato con Ordinanza N. "
									+ lDecisioneGE.getNumeroProvv() + "/" + lDecisioneGE.getAnnoProvv();
							lStringaRevoca += " emessa in data "
									+ DateUtils.getDateToString(lDecisioneGE.getDataD(), "dd-MM-yyyy");
							lStringaRevoca += " da " + lDecisioneGE.getDescrUfficioEmittente() + " di "
									+ lDecisioneGE.getDescrLuogoEmittente();

							if ("C".equals(lDecisioneGE.getFlagConforme())) {
								// C=conforme,
								siesLogger.debug("Trovata decisione del GE conforme. La SS è REVOCATA");
								aSanzioneSostMod.setIsRevocata(true);
								aSanzioneSostMod.setStringaRevoca(lStringaRevoca);
								isSSRevocata = true;
							} else {
								siesLogger.debug(
										"Trovata decisione del GE NON conforme. La Sospensiva è ATTIVA");
								aSanzioneSostMod.setIsRevocata(false);
								isSSRevocata = false;
							}
						} else {
							// Se con anticipazione
							siesLogger.debug(
									"Manca la decisione del GE, verifico se la richiesta è con anticipazione");
							if ("A".equals(lRichiesta.getFlagAppProvvisoria())) {
								siesLogger.debug(
										"Manca la decisione del GE, ma richiesta con Anticipazione. La SS è REVOCATA.");
								aSanzioneSostMod.setIsRevocata(true);
								isSSRevocata = true;
							}
						}
					}
				} // end ciclo RichPMSanSostCumModel
			} // end ciclo RichiestePmInCumuloModel
		} catch (DAOException daoEx) {
			siesLogger.error("DAOException: ", daoEx);
			throw new SIEPException(F3BException.USER_MESSAGE,
					"IstruttoriaCumuloController.isSSRevocata: " + daoEx);
		} catch (Exception e) {
			siesLogger.error("Exception: ", e);
			throw new SIEPException(F3BException.USER_MESSAGE,
					"IstruttoriaCumuloController.isSSRevocata" + e.getMessage());
		} finally {
			cleanup(lRichPmSqlDao);
			cleanup(lRicPmSanSostSqlDao);
			cleanup(lProvvGeSorvSqlDao);
		}

		return isSSRevocata;
	}

	// 23/04/2019 MEV70
	public BigDecimal ExCountPresenzeTitoloNellaStessaIstruttoria(BigDecimal aIdIstruttoria,
			FascicoloSiepModel aFasSiep, String ufficio) throws F3BException {

		Connection lConn = null;
		BigDecimal HowManyRecords = null;
		IstruttoriaCumuloSqlDAO lIstCumSqlDao = null;

		try {
			lConn = getDBConnection();
			lIstCumSqlDao = new IstruttoriaCumuloSqlDAO(lConn);
			lIstCumSqlDao.getCountPresenzeTitoloInIstruttoria(aIdIstruttoria, aFasSiep, ufficio);
			lIstCumSqlDao.start();
			lIstCumSqlDao.next();
			HowManyRecords = lIstCumSqlDao.getBigDecimal("HowManyRecords");
			siesLogger.debug("--XX-- totale record = " + HowManyRecords);

		} catch (DAOException daoEx) {
			rollback(lConn);
			siesLogger.error("DAOException: ", daoEx);
			throw new SIEPException(F3BException.USER_MESSAGE,
					"IstruttoriaCumuloController.ExCountPresenzeTitoloNellaStessaIstruttoria: " + daoEx);
		} catch (Exception e) {
			rollback(lConn);
			siesLogger.error("Exception: ", e);
			throw new SIEPException(F3BException.USER_MESSAGE, e.getMessage());
		} finally {
			cleanup(lIstCumSqlDao);
			cleanup(lConn);
		}

		return HowManyRecords;
	}

	/**
	 * 30/04/2019 Metodo che estrae l'eventuale primo Titolo doppio per una Istruttoria Cumulo.
	 *
	 * @param aIdIstruttoria
	 * @return TitoloCumulatoModel.
	 * @throws F3BException
	 */
	public Vector<TitoloCumulatoModel> titoloDoppioInIstruttoria(BigDecimal aIdIstruttoria)
			throws F3BException {

		Vector<TitoloCumulatoModel> titoliDoppi = null;
		IstruttoriaCumuloSqlDAO lIstCumSqlDao = null;

		Connection lConn = null;

		try {
			lConn = getDBConnection();
			lIstCumSqlDao = new IstruttoriaCumuloSqlDAO(lConn);
			titoliDoppi = lIstCumSqlDao.titoloDoppioInIstruttoria(aIdIstruttoria);
		} catch (DAOException daoEx) {
			siesLogger.error("DAOException: ", daoEx);
			throw new SIEPException(F3BException.USER_MESSAGE,
					"IstruttoriaCumuloController.titoloDoppioInIstruttoria: " + daoEx);
		} catch (Exception e) {
			siesLogger.error("Exception: ", e);
			throw new SIEPException(F3BException.USER_MESSAGE,
					"IstruttoriaCumuloController.titoloDoppioInIstruttoria" + e.getMessage());
		} finally {
			cleanup(lIstCumSqlDao);
			// Scheda Intervento n° 6 - Ottimizzazione SIUS Avvocati
			cleanup(lConn);
		}

		return titoliDoppi;
	}

	/**
	 * MEV_2025-48 – 2.14 Caricamento Istruttoria Annullata Nuovo metodo che trasferisce i dati
	 * dell'istruttoria corrente sul fascicolo indicato Se sul fascicolo indicato NON è presente una
	 * istruttoria aperta - Apre l'istruttoria - Carica in istruttoria il fascicolo corrente - Carica in
	 * istruttoria anche i dati dell'istruttoria corrente Se esiste una istruttoria aperta - Carica in
	 * istruttoria il fascicolo corrente (se non presente) - Carica in istruttoria anche i dati
	 * dell'istruttoria corrente (anche se aperta) Per i fascicoli proprio ufficio dell'istruttoria corrente
	 * aggiorna i puntamenti al nuovo fascicolo
	 *
	 * Al termine del caricamento chiude l'istruttoria corrente (annulla)
	 *
	 * Opera solo tra fascicoli dello STESSO ufficio
	 *
	 * @param aIstruttoriaToAdd
	 *            - Istruttoria targhet se non esiste viene creata
	 * @param aIstruttoriaCorrente
	 *            - istruttoria corrente da chiudere
	 * @since MEV_2025-48
	 */
	public IstruttoriaCumuloModel ExTrasferisciIstruttoriaCumulo(IstruttoriaCumuloModel aIstruttoriaToAdd,
			IstruttoriaCumuloModel aIstruttoriaCorrente) throws F3BException {

		Connection lConn = null;
		IstruttoriaCumuloDAO lIstruttoriaDao = null;
		IstruttoriaCumuloSqlDAO lIstruttoriaSqlDao = null;

		IstruttoriaCumuloModel lIstMod = null;

		FascicoloSiepSqlDAO lFascSqlDao = null;
		EventoStoreProcedurePulisciDAO lEventoProcSqlDao = null;

		DatiFinaliCumuloDAO lDatiFinaliCumuloDAO = null;
		DatiFinaliCumuloSqlDAO lDatiFinaliSqlDAO = null;
		EventoSqlDAO lEveSqlDao = null;

		AnnotazioneEsitoTrasmissioneDAO lAnnEsiDao = null;
		AnnotazioneEsitoTrasmissioneSqlDAO lAnnEsiSqlDao = null;

		try {
			lConn = getDBConnection();

			lIstruttoriaSqlDao = new IstruttoriaCumuloSqlDAO(lConn);
			lIstruttoriaDao = new IstruttoriaCumuloDAO(lConn);

			siesLogger.debug("Ricerco eventuale istruttoria Aperta sul fascicolo target "
					+ aIstruttoriaToAdd.getFasSieIdFascicoloSiep());
			IstruttoriaCumuloModel lIstrTarget = null;
			// Verifico la presenza suil fasciolo di destinazione di una istruttoria aperta

			lIstruttoriaSqlDao
					.RicercaIstruttoriaCumuloApertaByIdFasSiep(aIstruttoriaToAdd.getFasSieIdFascicoloSiep());
			lIstrTarget = (IstruttoriaCumuloModel) lIstruttoriaSqlDao.getModelByKey();

			if (lIstrTarget == null) {
				siesLogger.debug("Istruttoria Aperta non trovata procedo ad aprirla");

				FascicoloSiepModel lFasc = new FascicoloSiepModel();
				lFasc.setIdFascicoloSiep(aIstruttoriaToAdd.getFasSieIdFascicoloSiep());

				siesLogger.debug("Chiamo ExInserisciIstruttoriaCumulo per il fascicolo target");
				// crea l'istruttoria e carica il cumulante. Restituisce Istr con solo ID caricato
				lIstrTarget = this.ExInserisciIstruttoriaCumulo(aIstruttoriaToAdd, lFasc, lConn);
			} else {
				siesLogger.debug("Istruttoria Aperta trovata");
			}

			// ========================================================================
			// Carico il procedimento corrente in istruttoria
			// Inserisco il fascicolo corrente ed estraggo i dati analitici
			// Mi serve
			// - IdIstruttoria Target
			// - IdDel fascicolo da iscrivere
			// ========================================================================
			IModuloCumulo lCtrlModCum = SIEPLookupRemote.getModuloCumuloRemote();

			DatiOperazioneModel lDatiOpModel = new DatiOperazioneModel();
			lDatiOpModel.setCodOperatore(aIstruttoriaToAdd.getCodOperatoreInserimento());
			lDatiOpModel.setCodUfficio(aIstruttoriaToAdd.getCodUfficioInserimento());
			lDatiOpModel.setData(aIstruttoriaToAdd.getDataInserimento());

			siesLogger.debug("Carico il fascicolo corrente in Istruttoria");
			// n.b. tipo 04 = proprio ufficio necessario in caso di rimozione dall'istruttoria
			// per cancellare l'evento di trasmissione
			lCtrlModCum.ExInserisciTitoloInIstruttoria(lIstrTarget.getIdIstruttoriaCumulo(),
					aIstruttoriaCorrente.getFasSieIdFascicoloSiep(), lDatiOpModel, lConn, null, "04");

			// Carico i dati dell'istruttoria corrente in quella del fascicolo nuovo
			siesLogger.debug("Carico i dati dell'istruttoria aperta");
			lFascSqlDao = new FascicoloSiepSqlDAO(lConn);
			lFascSqlDao.ricercaFascicoloByKey(aIstruttoriaCorrente.getFasSieIdFascicoloSiep());
			FascicoloSiepModel lFascicolo = (FascicoloSiepModel) lFascSqlDao.getModelByKey();
			lCtrlModCum.EstraiDaPrecedenteCumulo(aIstruttoriaCorrente, lIstrTarget.getIdIstruttoriaCumulo(),
					lFascicolo, lDatiOpModel, lConn, "04");

			// Devo registrare l'evento di trasmissione sul fascicolo corrente
			lFascSqlDao.ricercaFascicoloByKey(aIstruttoriaToAdd.getFasSieIdFascicoloSiep());
			FascicoloSiepModel lFasCumulanteModel = (FascicoloSiepModel) lFascSqlDao.getModelByKey();

			lCtrlModCum.ExInserisciEventoAnnotazioneEsitoTrasm(lFasCumulanteModel,
					lIstrTarget.getIdIstruttoriaCumulo(), aIstruttoriaCorrente.getFasSieIdFascicoloSiep(),
					lDatiOpModel, lConn);

			// Attenzione!! per i fascicoli nell'istruttoria corrente, stesso ufficio, devo aggiornare
			// i puntamenti dell'evento di trasmissione per farli risultare in istruttoria sul
			// nuovo procedimento:
			// - scorro i titoli dell'istuttoria corrente
			// - se tipo iscrizione = 04 (e stesso ufficio!) provo a recuperare l'evento
			// - aggiorna il record ANNOTAZIONE_ESITO_TRASMISSIONE (anno e numero)
			//
			siesLogger.debug("Aggiorno i record ANNOTAZIONE_ESITO_TRASMISSIONE per i titoli stesso ufficio");
			Vector<TitoloCumulatoModel> lTitoliInIstrCorrente = null;
			lTitoliInIstrCorrente = ExRicercaTitoliByIstruttoriaOrderBy(
					aIstruttoriaCorrente.getIdIstruttoriaCumulo(), null);
			for (int k = 0; k < lTitoliInIstrCorrente.size(); k++) {
				TitoloCumulatoModel lTit = lTitoliInIstrCorrente.elementAt(k);
				siesLogger.debug("Titolo " + lTit.getIdTitoloCumulato() + ", Tipo iscrizione = "
						+ lTit.getTipoIscrizione());
				if ("04".equals(lTit.getTipoIscrizione())) {
					siesLogger.debug("Titolo " + lTit.getIdTitoloCumulato() + " iscritto da proprio uffiico");
					if (lTit.getProcedimentoCumulato() != null
							&& lTit.getProcedimentoCumulato().getEveIdEvento() != null) {
						siesLogger.debug("Aggiorno Anno e Progr sul record ANNOTAZIONE_ESITO_TRASMISSIONE");
						lAnnEsiSqlDao = new AnnotazioneEsitoTrasmissioneSqlDAO(lConn);
						lAnnEsiDao = new AnnotazioneEsitoTrasmissioneDAO(lConn);

						lAnnEsiSqlDao.ricercaAnnotazioneEsitoTrasmissioneByIdEvento(
								lTit.getProcedimentoCumulato().getEveIdEvento());
						AnnotazioneEsitoTrasmissioneModel lAnnMod = (AnnotazioneEsitoTrasmissioneModel) lAnnEsiSqlDao
								.getModelByKey();

						// Aggiorno Anno e Numero (se stesso ufficio)
						if (lFasCumulanteModel.getChiaveUfficio().equals(lAnnMod.getChiaveUfficio())) {
							lAnnEsiDao.setChiaveAnno(lFasCumulanteModel.getChiaveAnno());
							lAnnEsiDao.setChiaveProgr(lFasCumulanteModel.getChiaveProgr());

							lAnnEsiDao.selCondizioneUpdate(lAnnMod.getIdEsitoTrasmissione());

							lAnnEsiDao.update();
							lAnnEsiDao.stop();
						}
					}
				}
			}

			//
			// Infine chiudo l'istruttoria Corrente eliminando l'evento se presente
			//
			siesLogger.debug("Chiudo l'istruttoria corrente");
			aIstruttoriaCorrente.setFlagStato(ICostantiIstruttoriaCumulo.FLAG_STATO_ANNULLATA); // Annullato
			aIstruttoriaCorrente.setDataChiusura(DateUtils.getSysDate());
			aIstruttoriaCorrente.setNote("Istruttoria chiusa per trasferimento su Fascicolo "
					+ lFasCumulanteModel.getChiaveAnno() + " / " + lFasCumulanteModel.getChiaveProgr());

			aIstruttoriaCorrente.setCodOperatoreAggiornamento(aIstruttoriaToAdd.getCodOperatoreInserimento());
			aIstruttoriaCorrente.setCodUfficioAggiornamento(aIstruttoriaToAdd.getCodUfficioInserimento());
			aIstruttoriaCorrente.setDataAggiornamento(aIstruttoriaToAdd.getDataInserimento());

			lIstruttoriaDao = new IstruttoriaCumuloDAO(lConn);
			lIstruttoriaDao.setDAOFromModel(aIstruttoriaCorrente);
			lIstruttoriaDao.selCondizioneUpdate(aIstruttoriaCorrente.getIdIstruttoriaCumulo());
			lIstruttoriaDao.update();

			siesLogger.debug("Elimino eventuale EVENTO non validato collegato all'istruttoria");
			lDatiFinaliSqlDAO = new DatiFinaliCumuloSqlDAO(lConn);
			lDatiFinaliSqlDAO
					.ricercaDatiFinaliCumuloByIdIstruttoria(aIstruttoriaCorrente.getIdIstruttoriaCumulo());

			DatiFinaliCumuloModel lDatiFinaliModel = (DatiFinaliCumuloModel) lDatiFinaliSqlDAO
					.getModelByKey();

			if (lDatiFinaliModel != null && lDatiFinaliModel.getEveIdEvento() != null) {
				// pulisco il puntamento dalla DatiFinaliCumulo
				lDatiFinaliCumuloDAO = new DatiFinaliCumuloDAO(lConn);
				lDatiFinaliCumuloDAO.selCondizioneUpdate(lDatiFinaliModel.getIdDatiFinaliCumulo());
				lDatiFinaliCumuloDAO.setEveIdEvento(null);
				lDatiFinaliCumuloDAO.update();

				// n.b. per sicurezza si testa la presenza effettiva dell'evento non essendo presente la FK
				lEveSqlDao = new EventoSqlDAO(lConn);
				lEveSqlDao.ricercaEventoByKey(lDatiFinaliModel.getEveIdEvento());
				EventoModel lProvvedimento = (EventoModel) lEveSqlDao.getModelByKey();

				if (lProvvedimento != null) {
					// se l'evento esiste lo elimino
					lEventoProcSqlDao = new EventoStoreProcedurePulisciDAO(lConn);
					lEventoProcSqlDao.setIdEvento(lProvvedimento.getIdEvento());
					lEventoProcSqlDao.execute();
				}
			}
			// ========================================================================
			commit(lConn);

			lIstMod = new IstruttoriaCumuloModel();
			lIstruttoriaSqlDao.ricercaIstruttoriaCumuloByKey(lIstrTarget.getIdIstruttoriaCumulo());
			lIstMod = (IstruttoriaCumuloModel) lIstruttoriaSqlDao.getModelByKey();
			lIstMod.setMessage("Inserimento avvenuto correttamente!");
		} catch (DAOException ex) {
			rollback(lConn);
			siesLogger.error("DAOException", ex);
			throw new F3BException(
					"IstruttoriaCumuloController.ExInserisciIstruttoriaCumulo: Non posso inserire: " + ex);
		} finally {
			cleanup(lIstruttoriaDao);
			cleanup(lIstruttoriaSqlDao);

			cleanup(lFascSqlDao);
			cleanup(lEventoProcSqlDao);
			cleanup(lDatiFinaliSqlDAO);
			cleanup(lDatiFinaliCumuloDAO);
			cleanup(lEveSqlDao);

			cleanup(lAnnEsiDao);
			cleanup(lAnnEsiSqlDao);

			cleanup(lConn);
		}

		return lIstMod;
	}

	/**
	 * Recupera l'istruttoria collegata all'ultimo provvedimento di cumulo validatao per il fascciolo in input
	 *
	 */
	public IstruttoriaCumuloModel ExRicercaIstruttoriaUltimoCumulo(BigDecimal aIdFascicoloSiep)
			throws F3BException {

		Connection lConn = null;
		IstruttoriaCumuloSqlDAO lIstruttoriaSqlDao = null;
		IstruttoriaCumuloModel lUltimaIstruttoria = null;

		try {
			lConn = getDBConnection();

			lIstruttoriaSqlDao = new IstruttoriaCumuloSqlDAO(lConn);

			siesLogger.debug("Ricerca eventuale presenza procedimento di cumulo...");
			lIstruttoriaSqlDao = new IstruttoriaCumuloSqlDAO(lConn);
			lIstruttoriaSqlDao.ricercaIstruttoriaCumuloByIdFas(aIdFascicoloSiep);
			lUltimaIstruttoria = (IstruttoriaCumuloModel) lIstruttoriaSqlDao.getModelByKey();
			if (lUltimaIstruttoria == null)
				siesLogger.debug("Nessun cumulo validato collegato al fascicolo indicato.");

		} catch (DAOException ex) {
			siesLogger.error("DAOException", ex);
			throw new F3BException(
					"IstruttoriaCumuloController.ExRicercaIstruttoriaUltimoCumulo: Non posso inserire: "
							+ ex);
		} finally {
			cleanup(lIstruttoriaSqlDao);

			cleanup(lConn);
		}

		return lUltimaIstruttoria;
	}

	/**
	 * Etrae i dati del Titolo che ASSORBE la pena in contiinuazione rendendola non computabile. Metodo
	 * aggiunto per visualizzare comunque la pena sulla popup di calcolo
	 *
	 * @since MEV_2025-48 - ALTRO - Visualizzazione Pena In Continuazione
	 */
	public TitoloCumulatoModel getTitoloContinuazioneR(BigDecimal aIdTitolo, Connection aConn)
			throws F3BException {

		ContinuazioneCumuloSqlDAO lContinuazioneCumSqlDao = null;
		TitoloCumulatoSqlDAO lTitoloSqlDao = null;
		ProcedimentoCumulatoSqlDAO lProcCumSqlDao = null;

		TitoloCumulatoModel lTitoloModel = null;

		try {
			siesLogger.debug(
					"getTitoloContinuazioneR recupero i dati del titolo che assorbe in continuazione la PC del titolo ="
							+ aIdTitolo);

			lContinuazioneCumSqlDao = new ContinuazioneCumuloSqlDAO(aConn);
			lTitoloSqlDao = new TitoloCumulatoSqlDAO(aConn);
			lProcCumSqlDao = new ProcedimentoCumulatoSqlDAO(aConn);

			lContinuazioneCumSqlDao.ricercaContinuazioneByIdTitoloCont(aIdTitolo);
			// Ticket#202603160115 - si usa la getModels per chiudere subito il cursore
			Vector<ContinuazioneCumuloModel> listaCont = new Vector(lContinuazioneCumSqlDao.getModels());
			
//			lContinuazioneCumSqlDao.start();			
//			while (lContinuazioneCumSqlDao.next()) {
//				ContinuazioneCumuloModel lConCumModel = (ContinuazioneCumuloModel) lContinuazioneCumSqlDao
//						.getModel();
			for (int i = 0; i< listaCont.size(); i++) {	
				ContinuazioneCumuloModel lConCumModel = listaCont.elementAt(i);
				// Ticket#202603160115 -
				if ("R".equals(lConCumModel.getCodTipoContinuazione())) {
					siesLogger.debug(
							"Trovata continuazione " + lConCumModel.getIdContinuazioneCum() + " di tipo R ");

					// recupera il titolo
					siesLogger.debug("recupera i dati del titolo " + lConCumModel.getTitIdTitoloCumulato());
					lTitoloSqlDao.ricercaTitoloCumulatoByKey(lConCumModel.getTitIdTitoloCumulato());
					lTitoloModel = (TitoloCumulatoModel) lTitoloSqlDao.getModelByKey();

					// recupero se presento il procedimento
					siesLogger.debug("recupera i dati del procedimento collegato ");
					lProcCumSqlDao.ricercaProcedimentoCumulatoByIdTitolo(lTitoloModel.getIdTitoloCumulato());
					ProcedimentoCumulatoModel lProcModel = (ProcedimentoCumulatoModel) lProcCumSqlDao
							.getModelByKey();
					lTitoloModel.setProcedimentoCumulato(lProcModel);
					break;
				}
			}
			// Ticket#202603160115
			// lContinuazioneCumSqlDao.stop();
		} catch (DAOException daoEx) {
			siesLogger.error("DAOException: ", daoEx);
			throw new SIEPException(F3BException.USER_MESSAGE,
					"IstruttoriaCumuloController.getTitoloContinuazioneR: " + daoEx);
		} catch (Exception e) {
			siesLogger.error("Exception: ", e);
			throw new SIEPException(F3BException.USER_MESSAGE,
					"IstruttoriaCumuloController.getTitoloContinuazioneR" + e.getMessage());
		} finally {
			cleanup(lContinuazioneCumSqlDao);
			cleanup(lProcCumSqlDao);
			cleanup(lTitoloSqlDao);
		}

		return lTitoloModel;
	}

} // Chiude Controller