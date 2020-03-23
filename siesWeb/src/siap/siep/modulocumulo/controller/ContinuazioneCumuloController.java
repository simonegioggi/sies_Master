package siap.siep.modulocumulo.controller;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.dao.DAOException;
import f3b.log.LogF3B;
import f3b.util.F3BException;
import siap.controller.SiapController;
import siap.siep.modulocumulo.dao.ContinuazioneCumuloDAO;
import siap.siep.modulocumulo.dao.ContinuazioneCumuloSqlDAO;
import siap.siep.modulocumulo.model.ContinuazioneCumuloModel;

/**
 * <p>
 * Title: ContinuazioneCumuloController
 * </p>
 * <p>
 * Description: Classe Controller per Continuazione
 * </p>
 * <p>
 * in ambito Cumulo (Continuazione_Cumulo)
 * </p>
 *
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class ContinuazioneCumuloController extends SiapController implements IContinuazioneCumulo {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public ContinuazioneCumuloModel ExInserisciContinuazione(ContinuazioneCumuloModel aContinuazione)
			throws F3BException {

		Connection lConn = null;

		ContinuazioneCumuloDAO lConDao = null;
		ContinuazioneCumuloModel lConMod = null;

		try {
			lConn = getDBConnection();

			lConMod = new ContinuazioneCumuloModel(aContinuazione);

			lConDao = new ContinuazioneCumuloDAO(lConn);

			lConDao.setDAOFromModel(aContinuazione);

			BigDecimal lKey = null;

			lKey = lConDao.insert();

			commit(lConn);

			lConMod.setIdContinuazioneCum(lKey);
		} catch (DAOException ex) {
			rollback(lConn);
			siesLogger.error("DAOException: " + ex);
			throw new F3BException("ContinuazioneCumuloController.ExInserisciContinuazioneCumulo: " + ex);
		} catch (Exception ex) {
			rollback(lConn);
			siesLogger.error("Exception: " + ex);
			throw new F3BException("ContinuazioneCumuloController.ExInserisciContinuazioneCumulo: " + ex);
		} finally {
			cleanup(lConDao);
			cleanup(lConn);
		}

		return lConMod;
	}

	/**
	 * @deprecated da verificare. Mai referenziato
	 */
	public Vector<ContinuazioneCumuloModel> ExRicercaContinuazione(ContinuazioneCumuloModel aContinuazione)
			throws F3BException {

		Connection lConn = null;

		Vector<ContinuazioneCumuloModel> lContinuazioni = new Vector<>();
		ContinuazioneCumuloSqlDAO lConDao = null;

		try {
			lConn = getDBConnection();
			lConDao = new ContinuazioneCumuloSqlDAO(lConn);
			lConDao.ricercaContinuazione(aContinuazione);
			lContinuazioni = new Vector<ContinuazioneCumuloModel>(lConDao.getModels());
		} catch (DAOException daoEx) {
			siesLogger.error("DAOException: ", daoEx);
			throw new F3BException("ContinuazioneCumuloController.ExRicercaContinuazioneCumulo: " + daoEx);
		} catch (Exception ex) {
			siesLogger.error("Exception : " + ex);
			throw new F3BException("ContinuazioneCumuloController.ExRicercaContinuazioneCumulo: " + ex);
		} finally {
			cleanup(lConDao);
			cleanup(lConn);
		}

		return lContinuazioni;
	}

	/**
	 *
	 */
	public ContinuazioneCumuloModel ExRicercaContinuazioneCumByKey(BigDecimal aKey) throws F3BException {

		Connection lConn = null;

		ContinuazioneCumuloSqlDAO lConDao = null;
		ContinuazioneCumuloModel lConMod;

		try {
			lConn = getDBConnection();
			lConDao = new ContinuazioneCumuloSqlDAO(lConn);
			lConDao.ricercaContinuazioneByKey(aKey);
			lConMod = (ContinuazioneCumuloModel) lConDao.getModelByKey();
		} catch (DAOException daoEx) {
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException("ContinuazioneCumuloController.ExRicercaContinuazioneByKey: " + daoEx);
		} catch (Exception ex) {
			siesLogger.error("Exception: " + ex);
			throw new F3BException("ContinuazioneCumuloController.ExRicercaContinuazioneByKey: " + ex);
		} finally {
			cleanup(lConDao);
			cleanup(lConn);
		}

		return lConMod;
	}

	/**
	 *
	 * @deprecated da verificare. Mai referenziato
	 */
	public Vector ExRicercaContinuazioneByIDPenaComplessiva(BigDecimal aKey) throws F3BException {

		Connection lConn = null;

		ContinuazioneCumuloSqlDAO lConDao = null;
		Vector lContinuazioni = new Vector();

		try {
			lConn = getDBConnection();
			lConDao = new ContinuazioneCumuloSqlDAO(lConn);
			lConDao.ricercaContinuazioneByIdPenaComplessivaCum(aKey);
			// lConMod = (ContinuazioneModel)lConDao.getModelByKey();
			lContinuazioni = new Vector(lConDao.getModels());

		} catch (DAOException daoEx) {
			siesLogger.error("DAOException: " + daoEx, daoEx);
			throw new F3BException(
					"ContinuazioneCumuloController.ExRicercaContinuazioneByIDPenaComplessiva: " + daoEx);
		} catch (Exception ex) {
			siesLogger.error("Exception: " + ex, ex);
			throw new F3BException(
					"ContinuazioneCumuloController.ExRicercaContinuazioneByIDPenaComplessiva: " + ex);
		} finally {
			cleanup(lConDao);
			cleanup(lConn);
		}

		return lContinuazioni;
	}

	/**
	 *
	 * @param aIdTitolo
	 * @return
	 * @throws F3BException
	 */
	public Vector<ContinuazioneCumuloModel> ExRicercaContinuazioneByIDTitolo(BigDecimal aIdTitolo)
			throws F3BException {

		Connection lConn = null;

		ContinuazioneCumuloSqlDAO lConDao = null;
		Vector<ContinuazioneCumuloModel> lContinuazioni = new Vector<>();

		try {
			lConn = getDBConnection();
			lConDao = new ContinuazioneCumuloSqlDAO(lConn);
			lConDao.ricercaContinuazioneByIdTitolo(aIdTitolo);
			lContinuazioni = new Vector<ContinuazioneCumuloModel>(lConDao.getModels());
		} catch (DAOException daoEx) {
			siesLogger.error("DAOException: " + daoEx, daoEx);
			throw new F3BException(
					"ContinuazioneCumuloController.ExRicercaContinuazioneByIDTitolo: " + daoEx);
		} catch (Exception ex) {
			siesLogger.error("Exception: " + ex, ex);
			throw new F3BException("ContinuazioneCumuloController.ExRicercaContinuazioneByIDTitolo: " + ex);
		} finally {
			cleanup(lConDao);
			cleanup(lConn);
		}

		return lContinuazioni;
	}

	/**
	 *
	 */
	public ContinuazioneCumuloModel ExModificaContinuazioneCum(ContinuazioneCumuloModel aContinuazione)
			throws F3BException {

		Connection lConn = null;

		ContinuazioneCumuloDAO lConDao = null;
		ContinuazioneCumuloModel lConMod = new ContinuazioneCumuloModel(aContinuazione);

		try {
			lConn = getDBConnection();
			lConDao = new ContinuazioneCumuloDAO(lConn);
			lConDao.setDAOFromModelForUpdate(aContinuazione);
			lConDao.update();

			commit(lConn);
		} catch (DAOException ex) {
			rollback(lConn);
			siesLogger.error("DAOException: " + ex);
			throw new F3BException("ContinuazioneCumuloController.ExModificaContinuazioneCum: " + ex);
		} catch (Exception ex) {
			rollback(lConn);
			siesLogger.error("Exception: " + ex);
			throw new F3BException("ContinuazioneCumuloController.ExModificaContinuazioneCum: " + ex);
		} finally {
			cleanup(lConDao);
			cleanup(lConn);
		}

		return lConMod;
	}

	public void ExCancellaContinuazioneCum(ContinuazioneCumuloModel aContinuazione) throws F3BException {

		Connection lConn = null;

		ContinuazioneCumuloDAO lConDao = null;

		try {
			lConn = getDBConnection();

			lConDao = new ContinuazioneCumuloDAO(lConn);

			lConDao.setCondizioneUpdate(aContinuazione.getIdContinuazioneCum());

			lConDao.delete();

			commit(lConn);
		} catch (DAOException daoEx) {
			rollback(lConn);
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException("ContinuazioneCumuloController.ExCancellaContinuazione: " + daoEx);
		} catch (Exception ex) {
			rollback(lConn);
			siesLogger.error("Exception: " + ex);
			throw new F3BException("ContinuazioneCumuloController.ExCancellaContinuazione: " + ex);
		} finally {
			cleanup(lConDao);
			cleanup(lConn);
		}
	}

}