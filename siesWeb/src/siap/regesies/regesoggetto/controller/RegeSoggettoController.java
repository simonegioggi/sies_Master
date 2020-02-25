package siap.regesies.regesoggetto.controller;

import java.sql.Connection;
import java.util.Vector;

import org.apache.log4j.Logger;

import siap.controller.SiapController;
import siap.regesies.regesentenza.model.RegeSentenzaModel;
import siap.regesies.regesoggetto.dao.RegeSoggettoDAO;
import siap.regesies.regesoggetto.dao.RegeSoggettoSentenzaSqlDAO;
import siap.regesies.regesoggetto.dao.RegeSoggettoSqlDAO;
import siap.regesies.regesoggetto.model.RegeSoggettoModel;
import f3b.dao.DAOException;
import f3b.log.LogF3B;
import f3b.util.F3BException;

/**
 * <p>
 * Title: RegeSoggettoController
 * </p>
 * <p>
 * Description: Classe Controller per RegeSoggetto
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
public class RegeSoggettoController extends SiapController implements IRegeSoggetto {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	/*
	 * public RegeSoggettoModel ExInserisciRegeSoggetto (RegeSoggettoModel aRegeSoggetto ) throws F3BException
	 * { Connection lConn = null; RegeSoggettoDAO lRegDao = null; RegeSoggettoModel lRegMod = null; try {
	 * lConn = getDBConnection(); lRegMod = new RegeSoggettoModel(aRegeSoggetto); lRegDao = new
	 * RegeSoggettoDAO(lConn); lRegDao.setDAOFromModel(aRegeSoggetto ); lRegDao.insert(); commit(lConn);
	 * //lRegMod.setIdFile(lKey); } catch (DAOException ex) { rollback(lConn); // [FT] - 03/08/2016 - MAC_LOG
	 * - Utilizzo la variabile di istanza siesLogger al posto di mLog siesLogger.error("DAOException: " + ex);
	 * throw new F3BException("RegeSoggettoController.ExInserisci: Non posso inserire: " + ex); } catch
	 * (SQLException sqe) { rollback(lConn); // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza
	 * siesLogger al posto di mLog siesLogger.error("SQLException: " + sqe); throw new
	 * F3BException("RegeSoggettoController.ExInserisciRegeSoggetto: Non posso inserire il soggetti : " +
	 * sqe); } finally { cleanup(lRegDao); cleanup(lConn); } return lRegMod; }
	 */

	public Vector ExRicercaRegeSoggetto(RegeSoggettoModel aRegeSoggetto) throws F3BException {
		Connection lConn = null;
		Vector lRegeSoggetti = new Vector();
		RegeSoggettoSqlDAO lRegDao = null;

		try {
			lConn = getDBConnection();
			lRegDao = new RegeSoggettoSqlDAO(lConn);
			lRegDao.ricercaRegeSoggetto(aRegeSoggetto);
			lRegeSoggetti = new Vector(lRegDao.getModels());
			if (lRegeSoggetti.size() == 0) {
				throw new F3BException(F3BException.USER_MESSAGE, "Nessun Elemento trovato");
			}
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException("RegeSoggettoController.ExRicercaRegeSoggetto: Non posso leggere : "
					+ daoEx);
		} finally {
			cleanup(lRegDao);
			cleanup(lConn);
		}
		return lRegeSoggetti;
	}

	/**
	 * ExRicercaRegeSoggettoByKey
	 * 
	 * @param aKey
	 *            - Chiave File
	 * @return
	 * @throws F3BException
	 */
	public RegeSoggettoModel ExRicercaRegeSoggettoByKey(String aKey) throws F3BException {
		Connection lConn = null;
		RegeSoggettoSqlDAO lRegDao = null;
		RegeSoggettoModel lRegMod;

		try {
			lConn = getDBConnection();
			lRegDao = new RegeSoggettoSqlDAO(lConn);
			lRegDao.ricercaRegeSoggettoByKey(aKey);
			lRegMod = (RegeSoggettoModel) lRegDao.getModelByKey();
			if (lRegMod == null) {
				throw new F3BException(F3BException.USER_MESSAGE, "Nessun Elemento trovato");
			}
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException("RegeSoggettoController.ExRicercaRegeSoggetto: Non posso leggere : "
					+ daoEx);
		} finally {
			cleanup(lRegDao);
			cleanup(lConn);
		}
		return lRegMod;
	}

	/**
	 * Azione di Modifica del Rege Soggetto
	 * 
	 * @param aRegeSoggetto
	 *            da Modificare
	 * @return Rege Sottetto Modificato
	 * @throws F3BException
	 */
	public RegeSoggettoModel ExModificaRegeSoggetto(RegeSoggettoModel aRegeSoggetto) throws F3BException {
		Connection lConn = null;
		RegeSoggettoDAO lRegDao = null;
		RegeSoggettoModel lRegMod = new RegeSoggettoModel(aRegeSoggetto);

		try {
			lConn = getDBConnection();
			lRegDao = new RegeSoggettoDAO(lConn);
			lRegDao.setDAOFromModelForUpdate(aRegeSoggetto);
			lRegDao.update();
			commit(lConn);
		} catch (DAOException ex) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + ex);
			throw new F3BException("RegeSoggettoController.ExModifica: " + ex);
		} finally {
			cleanup(lRegDao);
			cleanup(lConn);
		}
		return lRegMod;
	}

	public void ExCancellaRegeSoggetto(RegeSoggettoModel aRegeSoggetto) throws F3BException {
		Connection lConn = null;
		RegeSoggettoDAO lRegDao = null;

		try {
			lConn = getDBConnection();
			lRegDao = new RegeSoggettoDAO(lConn);
			lRegDao.setCondizioneUpdate(aRegeSoggetto.getIdFile());
			lRegDao.delete();
			commit(lConn);
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException("RegeSoggettoController.ExCancellaRegeSoggetto: Non posso leggere : "
					+ daoEx);
		} finally {
			cleanup(lRegDao);
			cleanup(lConn);
		}
	}

	/**
	 * Ricerca i soggetti associati ad un provvedimento
	 * 
	 * @param aRegeSentenza
	 * @return
	 * @throws F3BException
	 */
	public Vector ExRicercaRegeSoggettoPerProvvedimento(RegeSentenzaModel aRegeSentenza) throws F3BException {
		Connection lConn = null;
		Vector lRegeSoggetti = new Vector();
		RegeSoggettoSentenzaSqlDAO lRegDao = null;

		try {
			lConn = getDBConnection();
			lRegDao = new RegeSoggettoSentenzaSqlDAO(lConn);
			lRegDao.ricercaRegeSoggettoSentenza(aRegeSentenza);
			lRegeSoggetti = new Vector(lRegDao.getModels());
			if (lRegeSoggetti.size() == 0) {
				throw new F3BException(F3BException.USER_MESSAGE, "Nessun Elemento trovato");
			}
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException("RegeSoggettoController.ExRicercaRegeSoggetto: Non posso leggere : "
					+ daoEx);
		} finally {
			cleanup(lRegDao);
			cleanup(lConn);
		}
		return lRegeSoggetti;

	}

}