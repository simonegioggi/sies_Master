package siap.sius.esecuzionemisurasicurezza.controller;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Vector;

import org.apache.log4j.Logger;

import siap.controller.SiapController;
import siap.sius.SIUSException;
import siap.sius.esecuzionemisurasicurezza.dao.EsecuzioneMisuraSicurezzaDAO;
import siap.sius.esecuzionemisurasicurezza.dao.EsecuzioneMisuraSicurezzaSqlDAO;
import siap.sius.esecuzionemisurasicurezza.model.EMSFascGPModel;
import siap.sius.esecuzionemisurasicurezza.model.EsecuzioneMisuraSicurezzaModel;
import siap.sius.fascicolo.model.FascicoloGPModel;
import siap.sius.tenore.dao.TenoreSqlDAO;
import siap.sius.tenore.model.TenoreModel;
import f3b.dao.DAOException;
import f3b.log.LogF3B;
import f3b.util.F3BException;

/**
 * <p>
 * Title: EsecuzioneMSController
 * </p>
 * <p>
 * Description: Classe Controller per EsecuzioneMisuraSicurezza
 * </p>
 * <p>
 * Copyright: Copyright (c) 2007
 * </p>
 * <p>
 * Company: Bull
 * </p>
 * 
 * @version 1.0
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class EsecuzioneMSController extends SiapController implements IEsecuzioneMS {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public EsecuzioneMisuraSicurezzaModel ExInserisciEsecuzioneMisuraSicurezza(
			EsecuzioneMisuraSicurezzaModel aEsecuzioneMisuraSicurezza) throws F3BException {
		Connection lConn = null;
		EsecuzioneMisuraSicurezzaDAO lEseDao = null;
		EsecuzioneMisuraSicurezzaModel lEseMod = null;
		try {
			lConn = getDBConnection();
			lEseMod = new EsecuzioneMisuraSicurezzaModel(aEsecuzioneMisuraSicurezza);
			lEseDao = new EsecuzioneMisuraSicurezzaDAO(lConn);
			lEseDao.setDAOFromModel(aEsecuzioneMisuraSicurezza);
			BigDecimal lKey = null;
			lKey = lEseDao.insert();
			commit(lConn);
			lEseMod.setIdEsecuzioneMisuraSicurezza(lKey);
		} catch (DAOException daoEx) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("DAOException: " + daoEx);
			throw new SIUSException(F3BException.USER_MESSAGE,
					"EsecuzioneMSController.ExInserisciEsecuzioneMisuraSicurezza: " + daoEx);
		} catch (Exception e) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("Exception: " + e);
			throw new F3BException(F3BException.USER_MESSAGE, e.getMessage());
		} finally {
			cleanup(lEseDao);
			cleanup(lConn);
		}
		return lEseMod;
	}

	public Vector ExRicercaEsecuzioneMisuraSicurezza(EsecuzioneMisuraSicurezzaModel aEsecuzioneMisuraSicurezza)
			throws F3BException {
		Connection lConn = null;
		Vector lEsecuzioneMisureSicurezza = new Vector();
		EsecuzioneMisuraSicurezzaSqlDAO lEseDao = null;
		try {
			lConn = getDBConnection();
			lEseDao = new EsecuzioneMisuraSicurezzaSqlDAO(lConn);
			lEseDao.ricercaEsecuzioneMisuraSicurezza(aEsecuzioneMisuraSicurezza);
			lEsecuzioneMisureSicurezza = new Vector(lEseDao.getModels());

		} catch (DAOException daoEx) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("DAOException: " + daoEx);
			throw new SIUSException(F3BException.USER_MESSAGE,
					"EsecuzioneMSController.ExRicercaEsecuzioneMisuraSicurezza: " + daoEx);
		} catch (Exception e) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("Exception: " + e);
			throw new F3BException(F3BException.USER_MESSAGE, e.getMessage());
		} finally {
			cleanup(lEseDao);
			cleanup(lConn);
		}
		return lEsecuzioneMisureSicurezza;
	}

	public EsecuzioneMisuraSicurezzaModel ExRicercaEsecuzioneMisuraSicurezzaByKey(BigDecimal aKey)
			throws F3BException {
		Connection lConn = null;
		EsecuzioneMisuraSicurezzaSqlDAO lEseDao = null;
		EsecuzioneMisuraSicurezzaModel lEseMod;
		try {
			lConn = getDBConnection();
			lEseDao = new EsecuzioneMisuraSicurezzaSqlDAO(lConn);
			lEseDao.ricercaEsecuzioneMisuraSicurezzaByKey(aKey);
			lEseMod = (EsecuzioneMisuraSicurezzaModel) lEseDao.getModelByKey();
		} catch (DAOException daoEx) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("DAOException: " + daoEx);
			throw new SIUSException(F3BException.USER_MESSAGE,
					"EsecuzioneMSController.ExRicercaEsecuzioneMisuraSicurezzaByKey: " + daoEx);
		} catch (Exception e) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("Exception: " + e);
			throw new F3BException(F3BException.USER_MESSAGE, e.getMessage());
		} finally {
			cleanup(lEseDao);
			cleanup(lConn);
		}
		return lEseMod;
	}

	public EsecuzioneMisuraSicurezzaModel ExRicercaEsecuzioneMisuraSicurezzaByAnnoProg(BigDecimal aAnno,
			BigDecimal aProg) throws F3BException {
		Connection lConn = null;
		EsecuzioneMisuraSicurezzaSqlDAO lEseDao = null;
		EsecuzioneMisuraSicurezzaModel lEseMod = null;
		if (aAnno != null && aProg != null) {
			try {
				lConn = getDBConnection();
				lEseDao = new EsecuzioneMisuraSicurezzaSqlDAO(lConn);
				lEseDao.ricercaEsecuzioneMisuraSicurezzaByAnnoProg(aAnno, aProg);
				lEseMod = (EsecuzioneMisuraSicurezzaModel) lEseDao.getModelByKey();
			} catch (DAOException daoEx) {
				rollback(lConn);
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.error("DAOException: " + daoEx);
				throw new SIUSException(F3BException.USER_MESSAGE,
						"EsecuzioneMSController.ExRicercaEsecuzioneMisuraSicurezzaByAnnoProg: " + daoEx);
			} catch (Exception e) {
				rollback(lConn);
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.error("Exception: " + e);
				throw new F3BException(F3BException.USER_MESSAGE, e.getMessage());
			} finally {
				cleanup(lEseDao);
				cleanup(lConn);
			}
		}
		return lEseMod;
	}

	public EsecuzioneMisuraSicurezzaModel ExRicercaEsecuzioneMisuraSicurezzaByIdFascicolo(BigDecimal aKey)
			throws F3BException {
		Connection lConn = null;
		EsecuzioneMisuraSicurezzaSqlDAO lEseDao = null;
		EsecuzioneMisuraSicurezzaModel lEseMod;
		try {
			lConn = getDBConnection();
			lEseDao = new EsecuzioneMisuraSicurezzaSqlDAO(lConn);
			lEseDao.ricercaEsecuzioneMisuraSicurezzaByIdFascicolo(aKey);
			lEseMod = (EsecuzioneMisuraSicurezzaModel) lEseDao.getModelByKey();
		} catch (DAOException daoEx) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("DAOException: " + daoEx);
			throw new SIUSException(F3BException.USER_MESSAGE,
					"EsecuzioneMSController.ExRicercaEsecuzioneMisuraSicurezzaByIdFascicolo: " + daoEx);
		} catch (Exception e) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("Exception: " + e);
			throw new F3BException(F3BException.USER_MESSAGE, e.getMessage());
		} finally {
			cleanup(lEseDao);
			cleanup(lConn);
		}
		return lEseMod;
	}

	public EsecuzioneMisuraSicurezzaModel ExRicercaEsecuzioneMisuraSicurezzaByIdOrdinanza(
			BigDecimal aIdOrdinanza) throws F3BException {
		Connection lConn = null;
		EsecuzioneMisuraSicurezzaSqlDAO lEseDao = null;
		EsecuzioneMisuraSicurezzaModel lEseMod;
		try {
			lConn = getDBConnection();
			lEseDao = new EsecuzioneMisuraSicurezzaSqlDAO(lConn);
			lEseDao.ricercaEsecuzioneMisuraSicurezzaByIdOrdinanza(aIdOrdinanza);
			lEseMod = (EsecuzioneMisuraSicurezzaModel) lEseDao.getModelByKey();
		} catch (DAOException daoEx) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("DAOException: " + daoEx);
			throw new SIUSException(F3BException.USER_MESSAGE,
					"EsecuzioneMSController.ExRicercaEsecuzioneMisuraSicurezzaByIdOrdinanza: " + daoEx);
		} catch (Exception e) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("Exception: " + e);
			throw new F3BException(F3BException.USER_MESSAGE, e.getMessage());
		} finally {
			cleanup(lEseDao);
			cleanup(lConn);
		}
		return lEseMod;
	}

	public EsecuzioneMisuraSicurezzaModel ExRicercaEsecuzioneMisuraSicurezzaByIdFascicolo(BigDecimal aKey,
			Connection aConn) throws F3BException {
		EsecuzioneMisuraSicurezzaSqlDAO lEseDao = null;
		EsecuzioneMisuraSicurezzaModel lEseMod;
		try {
			lEseDao = new EsecuzioneMisuraSicurezzaSqlDAO(aConn);
			lEseDao.ricercaEsecuzioneMisuraSicurezzaByIdFascicolo(aKey);
			lEseMod = (EsecuzioneMisuraSicurezzaModel) lEseDao.getModelByKey();
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("DAOException: " + daoEx);
			throw new SIUSException(F3BException.USER_MESSAGE,
					"EsecuzioneMSController.ExRicercaEsecuzioneMisuraSicurezzaByIdFascicolo: " + daoEx);
		} catch (Exception e) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("Exception: " + e);
			throw new F3BException(F3BException.USER_MESSAGE, e.getMessage());
		} finally {
			cleanup(lEseDao);
		}
		return lEseMod;
	}

	public EsecuzioneMisuraSicurezzaModel ExModificaEsecuzioneMisuraSicurezza(
			EsecuzioneMisuraSicurezzaModel aEsecuzioneMisuraSicurezza) throws F3BException {
		Connection lConn = null;
		EsecuzioneMisuraSicurezzaDAO lEseDao = null;
		EsecuzioneMisuraSicurezzaModel lEseMod = new EsecuzioneMisuraSicurezzaModel(
				aEsecuzioneMisuraSicurezza);
		try {
			lConn = getDBConnection();
			lEseDao = new EsecuzioneMisuraSicurezzaDAO(lConn);

			lEseDao.setDAOFromModelForUpdate(aEsecuzioneMisuraSicurezza);
			lEseDao.update();
			commit(lConn);
		} catch (DAOException ex) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("DAOException: " + ex);
			throw new SIUSException(F3BException.USER_MESSAGE,
					"EsecuzioneMSController.ExModificaEsecuzioneMisuraSicurezza : " + ex);
		} catch (Exception e) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("Exception: " + e);
			throw new F3BException(F3BException.USER_MESSAGE, e.getMessage());
		} finally {
			cleanup(lEseDao);
			cleanup(lConn);
		}
		return lEseMod;
	}

	public EsecuzioneMisuraSicurezzaModel ExModificaEMSbyFascicolo(
			EsecuzioneMisuraSicurezzaModel aEsecuzioneMisuraSicurezza) throws F3BException {
		Connection lConn = null;
		EsecuzioneMisuraSicurezzaDAO lEseDao = null;
		EsecuzioneMisuraSicurezzaModel lEseMod = new EsecuzioneMisuraSicurezzaModel(
				aEsecuzioneMisuraSicurezza);
		try {
			lConn = getDBConnection();
			lEseDao = new EsecuzioneMisuraSicurezzaDAO(lConn);

			lEseDao.setDAOFromModelForUpdatebyFascicolo(aEsecuzioneMisuraSicurezza);
			lEseDao.update();
			commit(lConn);
		} catch (DAOException ex) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("DAOException: " + ex);
			throw new SIUSException(F3BException.USER_MESSAGE,
					"EsecuzioneMSController.ExModificaEsecuzioneMisuraSicurezza : " + ex);
		} catch (Exception e) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("Exception: " + e);
			throw new F3BException(F3BException.USER_MESSAGE, e.getMessage());
		} finally {
			cleanup(lEseDao);
			cleanup(lConn);
		}
		return lEseMod;
	}

	public void ExCancellaEsecuzioneMisuraSicurezza(BigDecimal idEMS) throws F3BException {
		Connection lConn = null;
		EsecuzioneMisuraSicurezzaDAO lEseDao = null;
		try {
			lConn = getDBConnection();
			lEseDao = new EsecuzioneMisuraSicurezzaDAO(lConn);
			lEseDao.setCondizioneUpdate(idEMS);
			lEseDao.delete();
			commit(lConn);
		} catch (DAOException daoEx) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("DAOException: " + daoEx);
			throw new SIUSException(F3BException.USER_MESSAGE,
					"EsecuzioneMSController.ExCancellaEsecuzioneMisuraSicurezza: " + daoEx);
		} catch (Exception e) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("Exception: " + e);
			throw new F3BException(F3BException.USER_MESSAGE, e.getMessage());
		} finally {
			cleanup(lEseDao);
			cleanup(lConn);
		}
	}

	/**
	 * Ricerca Procedimenti di Esecuzione Misure Sicurezza
	 * 
	 * @param lAnno
	 * @param lProgr
	 * @param lAnnoIniziale
	 * @param lProgrIniziale
	 * @param lAnnoFinale
	 * @param lProgrFinale
	 * @param lUfficioUtenteConnesso
	 * @return Vettore di EMSFascGPModel
	 * @throws F3BException
	 */
	public Vector ExRicercaEsecuzioneMisureSicurezza(String lAnno, String lProgr, String lAnnoIniziale,
			String lProgrIniziale, String lAnnoFinale, String lProgrFinale, String lUfficioUtenteConnesso,
			int aPageNum) throws F3BException {
		Connection lConn = null;
		Vector lEsecuzioneMS = new Vector();

		EsecuzioneMisuraSicurezzaSqlDAO lEseMSSqlDao = null;

		try {
			lConn = getDBConnection();
			lEseMSSqlDao = new EsecuzioneMisuraSicurezzaSqlDAO(lConn);
			lEseMSSqlDao.ricercaEsecuzioneMisureSicurezza(lAnno, lProgr, lAnnoIniziale, lProgrIniziale,
					lAnnoFinale, lProgrFinale, lUfficioUtenteConnesso);
			lEseMSSqlDao.startPage(aPageNum);

			EMSFascGPModel lEMaFascGP = null;
			while (lEseMSSqlDao.next()) {
				lEMaFascGP = (EMSFascGPModel) lEseMSSqlDao.getEsecuzioneMisureSicurezza();
				lEsecuzioneMS.add(lEMaFascGP);
			}
			lEseMSSqlDao.stop();

			if (lEsecuzioneMS.isEmpty())
				throw new SIUSException(F3BException.USER_MESSAGE, "Esecuzione Misura Sicurezza non trovata");
		} catch (DAOException daoEx) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("DAOException: " + daoEx);
			throw new SIUSException(F3BException.USER_MESSAGE,
					"EsecuzioneMSController.ExRicercaEsecuzioneMisureSicurezza: " + daoEx);
		} catch (Exception e) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("Exception: " + e);
			throw new F3BException(F3BException.USER_MESSAGE, e.getMessage());
		} finally {
			cleanup(lEseMSSqlDao);
			cleanup(lConn);
		}
		return lEsecuzioneMS;
	}

	/**
	 * Ricerca Numero Procedimenti di Esecuzione Misure Sicurezza.
	 * 
	 * @param lAnno
	 * @param lProgr
	 * @param lAnnoIniziale
	 * @param lProgrIniziale
	 * @param lAnnoFinale
	 * @param lProgrFinale
	 * @param lUfficioUtenteConnesso
	 * @return Vettore di EMSFascGPModel
	 * @throws F3BException
	 */
	public BigDecimal ExGetNumRicercaEsecuzioneMisureSicurezza(String lAnno, String lProgr,
			String lAnnoIniziale, String lProgrIniziale, String lAnnoFinale, String lProgrFinale,
			String lUfficioUtenteConnesso) throws F3BException {
		Connection lConn = null;
		EsecuzioneMisuraSicurezzaSqlDAO lEseMSSqlDao = null;
		BigDecimal lCont = new BigDecimal(0);

		try {
			lConn = getDBConnection();
			lEseMSSqlDao = new EsecuzioneMisuraSicurezzaSqlDAO(lConn);
			lEseMSSqlDao.ricercaEsecuzioneMisureSicurezza(lAnno, lProgr, lAnnoIniziale, lProgrIniziale,
					lAnnoFinale, lProgrFinale, lUfficioUtenteConnesso);
			lCont = lEseMSSqlDao.getNumRowsSelected();
		} catch (DAOException daoEx) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("DAOException: " + daoEx);
			throw new SIUSException(F3BException.USER_MESSAGE,
					"EsecuzioneMSController.ExRicercaEsecuzioneMisureSicurezza: " + daoEx);
		} catch (Exception e) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("Exception: " + e);
			throw new F3BException(F3BException.USER_MESSAGE, e.getMessage());
		} finally {
			cleanup(lEseMSSqlDao);
			cleanup(lConn);
		}
		return lCont;
	}

	/**
	 * Ricerca dei Procedimenti relativi ad una Esecuzione di Misure Sicurezza. (Dettaglio Esec. Mis. Alt.).
	 * 
	 * @param aEseMSKey
	 * @param aIdSoggetto
	 * @return Vettore di FascicoloGPModel
	 * @throws F3BException
	 */
	public Vector ExRicercaDettaglioEsecuzioneMS(BigDecimal aEseMSKey, BigDecimal aIdSoggetto,
			String lUfficioUtenteConnesso) throws F3BException {
		Connection lConn = null;
		Vector lMisure = new Vector();
		EsecuzioneMisuraSicurezzaSqlDAO lEseMSSqlDao = null;
		// TenoreModel lTenMod = null;
		TenoreSqlDAO lTenDao = null;
		// SoggettoSqlDAO lSogDao = null;

		try {
			lConn = getDBConnection();

			// STUB 26/04/2007 Il model del soggetto nei figli non va normalizzato, perchè puo essere diverso
			// da quello del padre.
			// Si Popola di tutti i dati il model del soggetto.
			// lSogDao = new SoggettoSqlDAO(lConn);
			// lSogDao.ricercaSoggettoByKey( aIdSoggetto );
			// SoggettoModel aSogModel = (SoggettoModel) lSogDao.getModelByKey();

			lEseMSSqlDao = new EsecuzioneMisuraSicurezzaSqlDAO(lConn);
			lEseMSSqlDao.ricercaDettaglioEsecuzioneMS(aEseMSKey, lUfficioUtenteConnesso);
			lEseMSSqlDao.start();

			FascicoloGPModel lFascicolo = null;
			while (lEseMSSqlDao.next()) {
				lFascicolo = (FascicoloGPModel) lEseMSSqlDao.getFascicoloSiusGPModel();

				// STUB 26/04/2007 Il model del soggetto nei figli non va normalizzato, perchè puo essere
				// diverso da quello del padre.
				// Si reimposta il model del soggetto nel Fascicolo Sius.
				// lFascicolo.getFascicoloSiusModel().setSoggetto(aSogModel);

				// Si Inseriscono i Tenori
				lTenDao = new TenoreSqlDAO(lConn);
				lTenDao.ricercaTenoreByGeneraleProc(lFascicolo.getGeneraleProcedimentoModel()
						.getIdGeneraleProcedimento());

				// Si Caricano i dati del tenore nell'array di Tenori in FascicoloGPModel.
				Vector lVectTenori = new Vector(lTenDao.getModels());
				if (lVectTenori != null) {
					TenoreModel[] lTenoriModel = (TenoreModel[]) lVectTenori.toArray(new TenoreModel[0]);
					lFascicolo.setTenori(lTenoriModel);
				}
				lMisure.add(lFascicolo);

			}
			lEseMSSqlDao.stop();

		} catch (DAOException daoEx) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("DAOException: " + daoEx);
			throw new SIUSException(F3BException.USER_MESSAGE,
					"EsecuzioneMSController.ExRicercaDettaglioEsecuzioneMS: " + daoEx);
		} catch (Exception e) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("Exception: " + e);
			throw new F3BException(F3BException.USER_MESSAGE, e.getMessage());
		} finally {
			cleanup(lEseMSSqlDao);
			cleanup(lTenDao);
			// cleanup(lSogDao);
			cleanup(lConn);
		}
		return lMisure;
	}

	/**
	 * Ricerca dei Procedimenti relativi ad una Esecuzione di Misure Sicurezza. (Dettaglio Esec. Mis. Sic.).
	 * 
	 * @param aEseMSKey
	 * @param aIdSoggetto
	 * @param aConn
	 * @return Vettore di FascicoloGPModel
	 * @throws F3BException
	 */
	public Vector ExRicercaDettaglioEsecuzioneMS(BigDecimal aEseMSKey, BigDecimal aIdSoggetto,
			String lUfficioUtenteConnesso, Connection aConn) throws F3BException {
		Vector lMisure = new Vector();
		EsecuzioneMisuraSicurezzaSqlDAO lEseMSSqlDao = null;
		// TenoreModel lTenMod = null;
		TenoreSqlDAO lTenDao = null;
		// SoggettoSqlDAO lSogDao = null;

		try {
			// STUB 26/04/2007 Il model del soggetto nei figli non va normalizzato, perchè puo essere diverso
			// da quello del padre.
			// Si Popola di tutti i dati il model del soggetto.
			// lSogDao = new SoggettoSqlDAO(aConn);
			// lSogDao.ricercaSoggettoByKey( aIdSoggetto );
			// SoggettoModel aSogModel = (SoggettoModel) lSogDao.getModelByKey();

			lEseMSSqlDao = new EsecuzioneMisuraSicurezzaSqlDAO(aConn);
			lEseMSSqlDao.ricercaDettaglioEsecuzioneMS(aEseMSKey, lUfficioUtenteConnesso);
			lEseMSSqlDao.start();

			FascicoloGPModel lFascicolo = null;
			while (lEseMSSqlDao.next()) {
				lFascicolo = (FascicoloGPModel) lEseMSSqlDao.getFascicoloSiusGPModel();

				// Il model del soggetto nei figli non va normalizzato, perchè puo essere diverso da quello
				// del padre.
				// Si reimposta il model del soggetto nel Fascicolo Sius.
				// lFascicolo.getFascicoloSiusModel().setSoggetto(aSogModel);

				// Si Inseriscono i Tenori
				lTenDao = new TenoreSqlDAO(aConn);
				lTenDao.ricercaTenoreByGeneraleProc(lFascicolo.getGeneraleProcedimentoModel()
						.getIdGeneraleProcedimento());

				// Si Caricano i dati del tenore nell'array di Tenori in FascicoloGPModel.
				Vector lVectTenori = new Vector(lTenDao.getModels());
				if (lVectTenori != null) {
					TenoreModel[] lTenoriModel = (TenoreModel[]) lVectTenori.toArray(new TenoreModel[0]);
					lFascicolo.setTenori(lTenoriModel);
				}
				lMisure.add(lFascicolo);

			}
			lEseMSSqlDao.stop();

		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("DAOException: " + daoEx);
			throw new SIUSException(F3BException.USER_MESSAGE,
					"EsecuzioneMSController.ExRicercaDettaglioEsecuzioneMS: " + daoEx);
		} catch (Exception e) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("Exception: " + e);
			throw new F3BException(F3BException.USER_MESSAGE, e.getMessage());
		} finally {
			cleanup(lEseMSSqlDao);
			cleanup(lTenDao);
			// cleanup(lSogDao);
		}
		return lMisure;
	}

	/**
	 * Ricerca dei Procedimenti relativi ad una Esecuzione di Misure Sicurezza. (Dettaglio Esec. Mis. Sic.). e
	 * per ogni Misura, si cercano i procedimenti correlati.
	 * 
	 * @param aEseMSKey
	 * @param aIdSoggetto
	 * @return Coppia di Vettori di FascicoloGPModel
	 * @throws F3BException
	 */
	public Vector[] ExRicercaDettaglioEMSeCorrelati(BigDecimal aEseMSKey, BigDecimal aIdSoggetto,
			String lUfficioUtenteConnesso) throws F3BException {
		Connection lConn = null;
		Vector[] lMisureECorrelati = new Vector[2];
		Vector lMisure = new Vector();
		Vector lCorrelati = new Vector();
		EsecuzioneMisuraSicurezzaSqlDAO lEseMSSqlDao = null;
		EsecuzioneMisuraSicurezzaSqlDAO lEseMSSqlDao2 = null;
		// TenoreModel lTenMod = null;
		TenoreSqlDAO lTenDao = null;
		// SoggettoSqlDAO lSogDao = null;

		try {
			lConn = getDBConnection();

			// STUB 26/04/2007 Il model del soggetto nei figli non va normalizzato, perchè puo essere diverso
			// da quello del padre.
			// Si Popola di tutti i dati il model del soggetto.
			// lSogDao = new SoggettoSqlDAO(lConn);
			// lSogDao.ricercaSoggettoByKey( aIdSoggetto );
			// SoggettoModel aSogModel = (SoggettoModel) lSogDao.getModelByKey();

			lEseMSSqlDao = new EsecuzioneMisuraSicurezzaSqlDAO(lConn);
			lEseMSSqlDao.ricercaDettaglioEsecuzioneMS(aEseMSKey, lUfficioUtenteConnesso);
			lEseMSSqlDao.start();

			FascicoloGPModel lFascicolo = null;
			while (lEseMSSqlDao.next()) {
				lFascicolo = (FascicoloGPModel) lEseMSSqlDao.getFascicoloSiusGPModel();

				// STUB 26/04/2007 Il model del soggetto nei figli non va normalizzato, perchè puo essere
				// diverso da quello del padre.
				// Si reimposta il model del soggetto nel Fascicolo Sius.
				// lFascicolo.getFascicoloSiusModel().setSoggetto(aSogModel);

				// Si Inseriscono i Tenori
				lTenDao = new TenoreSqlDAO(lConn);
				lTenDao.ricercaTenoreByGeneraleProc(lFascicolo.getGeneraleProcedimentoModel()
						.getIdGeneraleProcedimento());

				// Si Caricano i dati del tenore nell'array di Tenori in FascicoloGPModel.
				Vector lVectTenori = new Vector(lTenDao.getModels());
				if (lVectTenori != null) {
					TenoreModel[] lTenoriModel = (TenoreModel[]) lVectTenori.toArray(new TenoreModel[0]);
					lFascicolo.setTenori(lTenoriModel);
				}
				lMisure.add(lFascicolo);

				// Caricamento Procedimenti Correlati all'EMS corrente.
				if (lFascicolo.getFascicoloSiusModel().getNumeroFascicoliUnificati() != null
						&& lFascicolo.getFascicoloSiusModel().getNumeroFascicoliUnificati().intValue() > 0) {
					lEseMSSqlDao2 = new EsecuzioneMisuraSicurezzaSqlDAO(lConn);
					lEseMSSqlDao2.ricercaProcedimentiCorrelatiAllEMS(lFascicolo.getFascicoloSiusModel()
							.getIdFascicoloSius(), lUfficioUtenteConnesso);
					lEseMSSqlDao2.start();
					FascicoloGPModel lFascCorrelato = null;
					while (lEseMSSqlDao2.next()) {
						lFascCorrelato = (FascicoloGPModel) lEseMSSqlDao2.getFascicoloSiusGPModel();
						lCorrelati.add(lFascCorrelato);
					}
					lEseMSSqlDao2.stop();
				}
			}
			lEseMSSqlDao.stop();
		} catch (DAOException daoEx) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("DAOException: " + daoEx);
			throw new SIUSException(F3BException.USER_MESSAGE,
					"EsecuzioneMSController.ExRicercaDettaglioEMSeCorrelati: " + daoEx);
		} catch (Exception e) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("Exception: " + e);
			throw new F3BException(F3BException.USER_MESSAGE, e.getMessage());
		} finally {
			cleanup(lEseMSSqlDao);
			cleanup(lEseMSSqlDao2);
			cleanup(lTenDao);
			// cleanup(lSogDao);
			cleanup(lConn);
		}
		lMisureECorrelati[0] = lMisure;
		lMisureECorrelati[1] = lCorrelati;
		// return lMisure;
		return lMisureECorrelati;
	}

	/**
	 * Ricerca dei Procedimenti relativi ad una Esecuzione di Misure Sicurezza per IdFascicolo. (Dettaglio
	 * Esec. Mis. Alt.).
	 * 
	 * @param aIdFascicolo
	 * @param aIdSoggetto
	 * @return Vettore di FascicoloGPModel
	 * @throws F3BException
	 */
	public Vector ExRicercaDettaglioEsecuzioneMSbyFascicolo(BigDecimal aIdFascicolo, BigDecimal aIdSoggetto,
			String lUfficioUtenteConnesso) throws F3BException {
		Connection lConn = null;
		Vector lMisure = new Vector();
		EsecuzioneMisuraSicurezzaSqlDAO lEseMSSqlDao = null;
		// TenoreModel lTenMod = null;
		TenoreSqlDAO lTenDao = null;
		// SoggettoSqlDAO lSogDao = null;

		try {
			lConn = getDBConnection();

			// STUB 26/04/2007 Il model del soggetto nei figli non va normalizzato, perchè puo essere diverso
			// da quello del padre.
			// Si Popola di tutti i dati il model del soggetto.
			// lSogDao = new SoggettoSqlDAO(lConn);
			// lSogDao.ricercaSoggettoByKey( aIdSoggetto );
			// SoggettoModel aSogModel = (SoggettoModel) lSogDao.getModelByKey();

			lEseMSSqlDao = new EsecuzioneMisuraSicurezzaSqlDAO(lConn);
			lEseMSSqlDao.ricercaDettaglioEsecuzioneMSbyFascicolo(aIdFascicolo, lUfficioUtenteConnesso);
			lEseMSSqlDao.start();

			FascicoloGPModel lFascicolo = null;
			while (lEseMSSqlDao.next()) {
				lFascicolo = (FascicoloGPModel) lEseMSSqlDao.getFascicoloSiusGPModel();

				// STUB 26/04/2007 Il model del soggetto nei figli non va normalizzato, perchè puo essere
				// diverso da quello del padre.
				// Si reimposta il model del soggetto nel Fascicolo Sius.
				// lFascicolo.getFascicoloSiusModel().setSoggetto(aSogModel);

				// Si Inseriscono i Tenori
				lTenDao = new TenoreSqlDAO(lConn);
				lTenDao.ricercaTenoreByGeneraleProc(lFascicolo.getGeneraleProcedimentoModel()
						.getIdGeneraleProcedimento());

				// 05/11/2003 Carico i dati del tenore nell'array di Tenori in FascicoloGPModel.
				Vector lVectTenori = new Vector(lTenDao.getModels());
				if (lVectTenori != null) {
					TenoreModel[] lTenoriModel = (TenoreModel[]) lVectTenori.toArray(new TenoreModel[0]);
					lFascicolo.setTenori(lTenoriModel);
				}

				lMisure.add(lFascicolo);
			}
			lEseMSSqlDao.stop();

			if (lMisure.isEmpty())
				throw new SIUSException(F3BException.USER_MESSAGE,
						"Esecuzione Misura Sicurezza priva di procedimenti");
		} catch (DAOException daoEx) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("DAOException: " + daoEx);
			throw new SIUSException(F3BException.USER_MESSAGE,
					"EsecuzioneMSController.ExRicercaDettaglioEsecuzioneMSbyFascicolo: " + daoEx);
		} catch (Exception e) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("Exception: " + e);
			throw new F3BException(F3BException.USER_MESSAGE, e.getMessage());
		} finally {
			cleanup(lEseMSSqlDao);
			cleanup(lTenDao);
			cleanup(lConn);
		}

		return lMisure;
	}

	// TODO carmela da verificare
	public Vector ExRicercaEsecuzioneMisureSicRidByIdOrdinanza(BigDecimal aIdOrdinanza) throws F3BException {
		Connection lConn = null;
		EsecuzioneMisuraSicurezzaSqlDAO lEseDao = null;
		Vector lEseMisSisRid = new Vector();
		try {
			lConn = getDBConnection();
			lEseDao = new EsecuzioneMisuraSicurezzaSqlDAO(lConn);
			lEseDao.ricercaEsecuzioneMisuraSicurezzaByIdOrdinanza(aIdOrdinanza);

			lEseMisSisRid = new Vector(lEseDao.getModels());

		} catch (DAOException daoEx) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("DAOException: " + daoEx);
			throw new SIUSException(F3BException.USER_MESSAGE,
					"EsecuzioneMSController.ExRicercaEsecuzioneMisureSicRidByIdOrdinanza: " + daoEx);
		} catch (Exception e) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("Exception: " + e);
			throw new F3BException(F3BException.USER_MESSAGE, e.getMessage());
		} finally {
			cleanup(lEseDao);
			cleanup(lConn);
		}
		return lEseMisSisRid;
	}

}