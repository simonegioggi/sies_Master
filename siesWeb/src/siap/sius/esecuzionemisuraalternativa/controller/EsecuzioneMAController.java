package siap.sius.esecuzionemisuraalternativa.controller;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Vector;

import org.apache.log4j.Logger;

import siap.controller.SiapController;
import siap.sius.SIUSException;
import siap.sius.esecuzionemisuraalternativa.dao.EsecuzioneMisuraAlternativaDAO;
import siap.sius.esecuzionemisuraalternativa.dao.EsecuzioneMisuraAlternativaSqlDAO;
import siap.sius.esecuzionemisuraalternativa.model.EMAFascGPModel;
import siap.sius.esecuzionemisuraalternativa.model.EsecuzioneMisuraAlternativaModel;
import siap.sius.fascicolo.model.FascicoloGPModel;
import siap.sius.tenore.dao.TenoreSqlDAO;
import siap.sius.tenore.model.TenoreModel;
import f3b.dao.DAOException;
import f3b.log.LogF3B;
import f3b.util.F3BException;

/**
 * <p>
 * Title: EsecuzioneMAController
 * </p>
 * <p>
 * Description: Classe Controller per EsecuzioneMisuraAlternativa
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: Bull
 * </p>
 * 
 * @version 1.0
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class EsecuzioneMAController extends SiapController implements IEsecuzioneMA {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public EsecuzioneMisuraAlternativaModel ExInserisciEsecuzioneMisuraAlternativa(
			EsecuzioneMisuraAlternativaModel aEsecuzioneMisuraAlternativa) throws F3BException {
		Connection lConn = null;
		EsecuzioneMisuraAlternativaDAO lEseDao = null;
		EsecuzioneMisuraAlternativaModel lEseMod = null;
		try {
			lConn = getDBConnection();
			lEseMod = new EsecuzioneMisuraAlternativaModel(aEsecuzioneMisuraAlternativa);
			lEseDao = new EsecuzioneMisuraAlternativaDAO(lConn);
			lEseDao.setDAOFromModel(aEsecuzioneMisuraAlternativa);
			BigDecimal lKey = null;
			lKey = lEseDao.insert();
			commit(lConn);
			lEseMod.setIdEsecuzioneMisuraAlternati(lKey);
		} catch (DAOException daoEx) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("DAOException: " + daoEx);
			throw new SIUSException(F3BException.USER_MESSAGE,
					"EsecuzioneMAController.ExInserisciEsecuzioneMisuraAlternativa: " + daoEx);
		} catch (Exception e) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("Exception: " + e);
			throw new F3BException(F3BException.USER_MESSAGE, e.getMessage());
		}

		finally {
			cleanup(lEseDao);
			cleanup(lConn);
		}
		return lEseMod;
	}

	public Vector ExRicercaEsecuzioneMisuraAlternativa(
			EsecuzioneMisuraAlternativaModel aEsecuzioneMisuraAlternativa) throws F3BException {
		Connection lConn = null;
		Vector lEsecuzioneMisuraAlternativi = new Vector();
		EsecuzioneMisuraAlternativaSqlDAO lEseDao = null;
		try {
			lConn = getDBConnection();
			lEseDao = new EsecuzioneMisuraAlternativaSqlDAO(lConn);
			lEseDao.ricercaEsecuzioneMisuraAlternativa(aEsecuzioneMisuraAlternativa);
			lEsecuzioneMisuraAlternativi = new Vector(lEseDao.getModels());

		} catch (DAOException daoEx) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("DAOException: " + daoEx);
			throw new SIUSException(F3BException.USER_MESSAGE,
					"EsecuzioneMAController.ExRicercaEsecuzioneMisuraAlternativa: " + daoEx);
		} catch (Exception e) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("Exception: " + e);
			throw new F3BException(F3BException.USER_MESSAGE, e.getMessage());
		}

		finally {
			cleanup(lEseDao);
			cleanup(lConn);
		}
		return lEsecuzioneMisuraAlternativi;
	}

	public EsecuzioneMisuraAlternativaModel ExRicercaEsecuzioneMisuraAlternativaByKey(BigDecimal aKey)
			throws F3BException {
		Connection lConn = null;
		EsecuzioneMisuraAlternativaSqlDAO lEseDao = null;
		EsecuzioneMisuraAlternativaModel lEseMod;
		try {
			lConn = getDBConnection();
			lEseDao = new EsecuzioneMisuraAlternativaSqlDAO(lConn);
			lEseDao.ricercaEsecuzioneMisuraAlternativaByKey(aKey);
			lEseMod = (EsecuzioneMisuraAlternativaModel) lEseDao.getModelByKey();
		} catch (DAOException daoEx) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("DAOException: " + daoEx);
			throw new SIUSException(F3BException.USER_MESSAGE,
					"EsecuzioneMAController.ExRicercaEsecuzioneMisuraAlternativaByKey: " + daoEx);
		} catch (Exception e) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("Exception: " + e);
			throw new F3BException(F3BException.USER_MESSAGE, e.getMessage());
		}

		finally {
			cleanup(lEseDao);
			cleanup(lConn);
		}
		return lEseMod;
	}

	public EsecuzioneMisuraAlternativaModel ExRicercaEsecuzioneMisuraAlternativaByAnnoProg(BigDecimal aAnno,
			BigDecimal aProg) throws F3BException {
		Connection lConn = null;
		EsecuzioneMisuraAlternativaSqlDAO lEseDao = null;
		EsecuzioneMisuraAlternativaModel lEseMod = null;
		if (aAnno != null && aProg != null) {
			try {
				lConn = getDBConnection();
				lEseDao = new EsecuzioneMisuraAlternativaSqlDAO(lConn);
				lEseDao.ricercaEsecuzioneMisuraAlternativaByAnnoProg(aAnno, aProg);
				lEseMod = (EsecuzioneMisuraAlternativaModel) lEseDao.getModelByKey();
			} catch (DAOException daoEx) {
				rollback(lConn);
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.error("DAOException: " + daoEx);
				throw new SIUSException(F3BException.USER_MESSAGE,
						"EsecuzioneMAController.ExRicercaEsecuzioneMisuraAlternativaByAnnoProg: " + daoEx);
			} catch (Exception e) {
				rollback(lConn);
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.error("Exception: " + e);
				throw new F3BException(F3BException.USER_MESSAGE, e.getMessage());
			}

			finally {
				cleanup(lEseDao);
				cleanup(lConn);
			}
		}
		return lEseMod;
	}

	public EsecuzioneMisuraAlternativaModel ExRicercaEsecuzioneMisuraAlternativaByIdFascicolo(BigDecimal aKey)
			throws F3BException {
		Connection lConn = null;
		EsecuzioneMisuraAlternativaSqlDAO lEseDao = null;
		EsecuzioneMisuraAlternativaModel lEseMod;
		try {
			lConn = getDBConnection();
			lEseDao = new EsecuzioneMisuraAlternativaSqlDAO(lConn);
			lEseDao.ricercaEsecuzioneMisuraAlternativaByIdFascicolo(aKey);
			lEseMod = (EsecuzioneMisuraAlternativaModel) lEseDao.getModelByKey();
		} catch (DAOException daoEx) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("DAOException: " + daoEx);
			throw new SIUSException(F3BException.USER_MESSAGE,
					"EsecuzioneMAController.ExRicercaEsecuzioneMisuraAlternativaByIdFascicolo: " + daoEx);
		} catch (Exception e) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("Exception: " + e);
			throw new F3BException(F3BException.USER_MESSAGE, e.getMessage());
		}

		finally {
			cleanup(lEseDao);
			cleanup(lConn);
		}
		return lEseMod;
	}

	public EsecuzioneMisuraAlternativaModel ExRicercaEsecuzioneMisuraAlternativaByIdFascicolo(
			BigDecimal aKey, Connection aConn) throws F3BException {
		EsecuzioneMisuraAlternativaSqlDAO lEseDao = null;
		EsecuzioneMisuraAlternativaModel lEseMod;
		try {
			lEseDao = new EsecuzioneMisuraAlternativaSqlDAO(aConn);
			lEseDao.ricercaEsecuzioneMisuraAlternativaByIdFascicolo(aKey);
			lEseMod = (EsecuzioneMisuraAlternativaModel) lEseDao.getModelByKey();
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("DAOException: " + daoEx);
			throw new SIUSException(F3BException.USER_MESSAGE,
					"EsecuzioneMAController.ExRicercaEsecuzioneMisuraAlternativaByIdFascicolo: " + daoEx);
		} catch (Exception e) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("Exception: " + e);
			throw new F3BException(F3BException.USER_MESSAGE, e.getMessage());
		}

		finally {
			cleanup(lEseDao);
		}
		return lEseMod;
	}

	public EsecuzioneMisuraAlternativaModel ExModificaEsecuzioneMisuraAlternativa(
			EsecuzioneMisuraAlternativaModel aEsecuzioneMisuraAlternativa) throws F3BException {
		Connection lConn = null;
		EsecuzioneMisuraAlternativaDAO lEseDao = null;
		EsecuzioneMisuraAlternativaModel lEseMod = new EsecuzioneMisuraAlternativaModel(
				aEsecuzioneMisuraAlternativa);
		try {
			lConn = getDBConnection();
			lEseDao = new EsecuzioneMisuraAlternativaDAO(lConn);

			lEseDao.setDAOFromModelForUpdate(aEsecuzioneMisuraAlternativa);
			lEseDao.update();
			commit(lConn);
		} catch (DAOException ex) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("DAOException: " + ex);
			throw new SIUSException(F3BException.USER_MESSAGE,
					"EsecuzioneMAController.ExModificaEsecuzioneMisuraAlternativa : " + ex);
		} catch (Exception e) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("Exception: " + e);
			throw new F3BException(F3BException.USER_MESSAGE, e.getMessage());
		}

		finally {
			cleanup(lEseDao);
			cleanup(lConn);
		}
		return lEseMod;
	}

	public EsecuzioneMisuraAlternativaModel ExModificaEMAbyFascicolo(
			EsecuzioneMisuraAlternativaModel aEsecuzioneMisuraAlternativa) throws F3BException {
		Connection lConn = null;
		EsecuzioneMisuraAlternativaDAO lEseDao = null;
		EsecuzioneMisuraAlternativaModel lEseMod = new EsecuzioneMisuraAlternativaModel(
				aEsecuzioneMisuraAlternativa);
		try {
			lConn = getDBConnection();
			lEseDao = new EsecuzioneMisuraAlternativaDAO(lConn);

			lEseDao.setDAOFromModelForUpdatebyFascicolo(aEsecuzioneMisuraAlternativa);
			lEseDao.update();
			commit(lConn);
		} catch (DAOException ex) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("DAOException: " + ex);
			throw new SIUSException(F3BException.USER_MESSAGE,
					"EsecuzioneMAController.ExModificaEsecuzioneMisuraAlternativa : " + ex);
		} catch (Exception e) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("Exception: " + e);
			throw new F3BException(F3BException.USER_MESSAGE, e.getMessage());
		}

		finally {
			cleanup(lEseDao);
			cleanup(lConn);
		}
		return lEseMod;
	}

	public void ExCancellaEsecuzioneMisuraAlternativa(BigDecimal idEMA) throws F3BException {
		Connection lConn = null;
		EsecuzioneMisuraAlternativaDAO lEseDao = null;
		try {
			lConn = getDBConnection();
			lEseDao = new EsecuzioneMisuraAlternativaDAO(lConn);
			lEseDao.setCondizioneUpdate(idEMA);
			lEseDao.delete();
			commit(lConn);
		} catch (DAOException daoEx) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("DAOException: " + daoEx);
			throw new SIUSException(F3BException.USER_MESSAGE,
					"EsecuzioneMAController.ExCancellaEsecuzioneMisuraAlternativa: " + daoEx);
		} catch (Exception e) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("Exception: " + e);
			throw new F3BException(F3BException.USER_MESSAGE, e.getMessage());
		}

		finally {
			cleanup(lEseDao);
			cleanup(lConn);
		}
	}

	/**
	 * Ricerca Procedimenti di Esecuzione Misure Alternative
	 * 
	 * @param lAnno
	 * @param lProgr
	 * @param lAnnoIniziale
	 * @param lProgrIniziale
	 * @param lAnnoFinale
	 * @param lProgrFinale
	 * @param lUfficioUtenteConnesso
	 * @return Vettore di EMAFascGPModel
	 * @throws F3BException
	 */
	public Vector ExRicercaEsecuzioneMisureAlternative(String lAnno, String lProgr, String lAnnoIniziale,
			String lProgrIniziale, String lAnnoFinale, String lProgrFinale, String lUfficioUtenteConnesso,
			int aPageNum) throws F3BException {
		Connection lConn = null;
		Vector lEsecuzioneMA = new Vector();

		EsecuzioneMisuraAlternativaSqlDAO lEseMASqlDao = null;

		try {
			lConn = getDBConnection();
			lEseMASqlDao = new EsecuzioneMisuraAlternativaSqlDAO(lConn);
			lEseMASqlDao.ricercaEsecuzioneMisureAlternative(lAnno, lProgr, lAnnoIniziale, lProgrIniziale,
					lAnnoFinale, lProgrFinale, lUfficioUtenteConnesso);
			lEseMASqlDao.startPage(aPageNum);

			EMAFascGPModel lEMaFascGP = null;
			while (lEseMASqlDao.next()) {
				lEMaFascGP = (EMAFascGPModel) lEseMASqlDao.getEsecuzioneMisureAlternative();
				lEsecuzioneMA.add(lEMaFascGP);
			}
			lEseMASqlDao.stop();

			if (lEsecuzioneMA.isEmpty())
				throw new SIUSException(F3BException.USER_MESSAGE,
						"Esecuzione Misura Alternativa non trovata");
		} catch (DAOException daoEx) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("DAOException: " + daoEx);
			throw new SIUSException(F3BException.USER_MESSAGE,
					"EsecuzioneMAController.ExRicercaEsecuzioneMisureAlternative: " + daoEx);
		} catch (Exception e) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("Exception: " + e);
			throw new F3BException(F3BException.USER_MESSAGE, e.getMessage());
		}

		finally {
			cleanup(lEseMASqlDao);
			cleanup(lConn);
		}
		return lEsecuzioneMA;
	}

	/**
	 * Ricerca Numero Procedimenti di Esecuzione Misure Alternative
	 * 
	 * @param lAnno
	 * @param lProgr
	 * @param lAnnoIniziale
	 * @param lProgrIniziale
	 * @param lAnnoFinale
	 * @param lProgrFinale
	 * @param lUfficioUtenteConnesso
	 * @return Vettore di EMAFascGPModel
	 * @throws F3BException
	 */
	public BigDecimal ExGetNumRicercaEsecuzioneMisureAlternative(String lAnno, String lProgr,
			String lAnnoIniziale, String lProgrIniziale, String lAnnoFinale, String lProgrFinale,
			String lUfficioUtenteConnesso) throws F3BException {
		Connection lConn = null;
		EsecuzioneMisuraAlternativaSqlDAO lEseMASqlDao = null;
		BigDecimal lCont = new BigDecimal(0);

		try {
			lConn = getDBConnection();
			lEseMASqlDao = new EsecuzioneMisuraAlternativaSqlDAO(lConn);
			lEseMASqlDao.ricercaEsecuzioneMisureAlternative(lAnno, lProgr, lAnnoIniziale, lProgrIniziale,
					lAnnoFinale, lProgrFinale, lUfficioUtenteConnesso);
			lCont = lEseMASqlDao.getNumRowsSelected();
		} catch (DAOException daoEx) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("DAOException: " + daoEx);
			throw new SIUSException(F3BException.USER_MESSAGE,
					"EsecuzioneMAController.ExRicercaEsecuzioneMisureAlternative: " + daoEx);
		} catch (Exception e) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("Exception: " + e);
			throw new F3BException(F3BException.USER_MESSAGE, e.getMessage());
		}

		finally {
			cleanup(lEseMASqlDao);
			cleanup(lConn);
		}
		return lCont;
	}

	/**
	 * Ricerca dei Procedimenti relativi ad una Esecuzione di Misure Alternative. (Dettaglio Esec. Mis. Alt.).
	 * 
	 * @param aEseMAKey
	 * @param aIdSoggetto
	 * @return Vettore di FascicoloGPModel
	 * @throws F3BException
	 */
	public Vector ExRicercaDettaglioEsecuzioneMA(BigDecimal aEseMAKey, BigDecimal aIdSoggetto,
			String lUfficioUtenteConnesso) throws F3BException {
		Connection lConn = null;
		Vector lMisure = new Vector();
		EsecuzioneMisuraAlternativaSqlDAO lEseMASqlDao = null;
//		TenoreModel lTenMod = null;
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

			lEseMASqlDao = new EsecuzioneMisuraAlternativaSqlDAO(lConn);
			lEseMASqlDao.ricercaDettaglioEsecuzioneMA(aEseMAKey, lUfficioUtenteConnesso);
			lEseMASqlDao.start();

			FascicoloGPModel lFascicolo = null;
			while (lEseMASqlDao.next()) {
				lFascicolo = (FascicoloGPModel) lEseMASqlDao.getFascicoloSiusGPModel();

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
			lEseMASqlDao.stop();

		} catch (DAOException daoEx) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("DAOException: " + daoEx);
			throw new SIUSException(F3BException.USER_MESSAGE,
					"EsecuzioneMAController.ExRicercaDettaglioEsecuzioneMA: " + daoEx);
		} catch (Exception e) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("Exception: " + e);
			throw new F3BException(F3BException.USER_MESSAGE, e.getMessage());
		}

		finally {
			cleanup(lEseMASqlDao);
			cleanup(lTenDao);
			// cleanup(lSogDao);
			cleanup(lConn);
		}
		return lMisure;
	}

	/**
	 * Ricerca dei Procedimenti relativi ad una Esecuzione di Misure Alternative. (Dettaglio Esec. Mis. Alt.).
	 * 
	 * @param aEseMAKey
	 * @param aIdSoggetto
	 * @param aConn
	 * @return Vettore di FascicoloGPModel
	 * @throws F3BException
	 */
	public Vector ExRicercaDettaglioEsecuzioneMA(BigDecimal aEseMAKey, BigDecimal aIdSoggetto,
			String lUfficioUtenteConnesso, Connection aConn) throws F3BException {
		Vector lMisure = new Vector();
		EsecuzioneMisuraAlternativaSqlDAO lEseMASqlDao = null;
//		TenoreModel lTenMod = null;
		TenoreSqlDAO lTenDao = null;
		// SoggettoSqlDAO lSogDao = null;

		try {
			// STUB 26/04/2007 Il model del soggetto nei figli non va normalizzato, perchè puo essere diverso
			// da quello del padre.
			// Si Popola di tutti i dati il model del soggetto.
			// lSogDao = new SoggettoSqlDAO(aConn);
			// lSogDao.ricercaSoggettoByKey( aIdSoggetto );
			// SoggettoModel aSogModel = (SoggettoModel) lSogDao.getModelByKey();

			lEseMASqlDao = new EsecuzioneMisuraAlternativaSqlDAO(aConn);
			lEseMASqlDao.ricercaDettaglioEsecuzioneMA(aEseMAKey, lUfficioUtenteConnesso);
			lEseMASqlDao.start();

			FascicoloGPModel lFascicolo = null;
			while (lEseMASqlDao.next()) {
				lFascicolo = (FascicoloGPModel) lEseMASqlDao.getFascicoloSiusGPModel();

				// STUB 26/04/2007 Il model del soggetto nei figli non va normalizzato, perchè puo essere
				// diverso da quello del padre.
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
			lEseMASqlDao.stop();

		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("DAOException: " + daoEx);
			throw new SIUSException(F3BException.USER_MESSAGE,
					"EsecuzioneMAController.ExRicercaDettaglioEsecuzioneMA: " + daoEx);
		} catch (Exception e) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("Exception: " + e);
			throw new F3BException(F3BException.USER_MESSAGE, e.getMessage());
		}

		finally {
			cleanup(lEseMASqlDao);
			cleanup(lTenDao);
			// cleanup(lSogDao);
		}
		return lMisure;
	}

	/**
	 * STUB 02/11/2005 Ricerca dei Procedimenti relativi ad una Esecuzione di Misure Alternative. (Dettaglio
	 * Esec. Mis. Alt.). e per ogni Misura, si cercano i procedimenti correlati.
	 * 
	 * @param aEseMAKey
	 * @param aIdSoggetto
	 * @return Coppia di Vettori di FascicoloGPModel
	 * @throws F3BException
	 */
	public Vector[] ExRicercaDettaglioEMAeCorrelati(BigDecimal aEseMAKey, BigDecimal aIdSoggetto,
			String lUfficioUtenteConnesso) throws F3BException {
		Connection lConn = null;
		Vector[] lMisureECorrelati = new Vector[2];
		Vector lMisure = new Vector();
		Vector lCorrelati = new Vector();
		EsecuzioneMisuraAlternativaSqlDAO lEseMASqlDao = null;
		EsecuzioneMisuraAlternativaSqlDAO lEseMASqlDao2 = null;
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

			lEseMASqlDao = new EsecuzioneMisuraAlternativaSqlDAO(lConn);
			lEseMASqlDao.ricercaDettaglioEsecuzioneMA(aEseMAKey, lUfficioUtenteConnesso);
			lEseMASqlDao.start();

			FascicoloGPModel lFascicolo = null;
			while (lEseMASqlDao.next()) {
				lFascicolo = (FascicoloGPModel) lEseMASqlDao.getFascicoloSiusGPModel();

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

				// Caricamento Procedimenti Correlati all'EMA corrente.
				if (lFascicolo.getFascicoloSiusModel().getNumeroFascicoliUnificati() != null
						&& lFascicolo.getFascicoloSiusModel().getNumeroFascicoliUnificati().intValue() > 0) {
					lEseMASqlDao2 = new EsecuzioneMisuraAlternativaSqlDAO(lConn);
					lEseMASqlDao2.ricercaProcedimentiCorrelatiAllEMA(lFascicolo.getFascicoloSiusModel()
							.getIdFascicoloSius(), lUfficioUtenteConnesso);
					lEseMASqlDao2.start();
					FascicoloGPModel lFascCorrelato = null;
					while (lEseMASqlDao2.next()) {
						lFascCorrelato = (FascicoloGPModel) lEseMASqlDao2.getFascicoloSiusGPModel();
						lCorrelati.add(lFascCorrelato);
					}
					lEseMASqlDao2.stop();
				}
			}
			lEseMASqlDao.stop();

		} catch (DAOException daoEx) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("DAOException: " + daoEx);
			throw new SIUSException(F3BException.USER_MESSAGE,
					"EsecuzioneMAController.ExRicercaDettaglioEMAeCorrelati: " + daoEx);
		} catch (Exception e) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("Exception: " + e);
			throw new F3BException(F3BException.USER_MESSAGE, e.getMessage());
		}

		finally {
			cleanup(lEseMASqlDao);
			cleanup(lEseMASqlDao2);
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
	 * Ricerca dei Procedimenti relativi ad una Esecuzione di Misure Alternative per IdFascicolo. (Dettaglio
	 * Esec. Mis. Alt.).
	 * 
	 * @param aIdFascicolo
	 * @param aIdSoggetto
	 * @return Vettore di FascicoloGPModel
	 * @throws F3BException
	 */
	public Vector ExRicercaDettaglioEsecuzioneMAbyFascicolo(BigDecimal aIdFascicolo, BigDecimal aIdSoggetto,
			String lUfficioUtenteConnesso) throws F3BException {
		Connection lConn = null;
		Vector lMisure = new Vector();
		EsecuzioneMisuraAlternativaSqlDAO lEseMASqlDao = null;
//		TenoreModel lTenMod = null;
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

			lEseMASqlDao = new EsecuzioneMisuraAlternativaSqlDAO(lConn);
			lEseMASqlDao.ricercaDettaglioEsecuzioneMAbyFascicolo(aIdFascicolo, lUfficioUtenteConnesso);
			lEseMASqlDao.start();

			FascicoloGPModel lFascicolo = null;
			while (lEseMASqlDao.next()) {
				lFascicolo = (FascicoloGPModel) lEseMASqlDao.getFascicoloSiusGPModel();

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
			lEseMASqlDao.stop();

			if (lMisure.isEmpty())
				throw new SIUSException(F3BException.USER_MESSAGE,
						"Esecuzione Misura Alternativa priva di procedimenti");
		} catch (DAOException daoEx) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("DAOException: " + daoEx);
			throw new SIUSException(F3BException.USER_MESSAGE,
					"EsecuzioneMAController.ExRicercaDettaglioEsecuzioneMAbyFascicolo: " + daoEx);
		} catch (Exception e) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("Exception: " + e);
			throw new F3BException(F3BException.USER_MESSAGE, e.getMessage());
		}

		finally {
			cleanup(lEseMASqlDao);
			cleanup(lTenDao);
			// cleanup(lSogDao);
			cleanup(lConn);
		}

		return lMisure;
	}

}