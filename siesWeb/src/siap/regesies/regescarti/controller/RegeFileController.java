package siap.regesies.regescarti.controller;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.dao.DAOException;
import f3b.log.LogF3B;
import f3b.util.F3BException;
import siap.controller.SiapController;
import siap.regesies.regescarti.dao.RegeFileDAO;
import siap.regesies.regescarti.dao.RegeFileSqlDAO;
import siap.regesies.regescarti.model.RegeFileModel;

/**
 * <p>
 * Title: RegeFileController
 * </p>
 * <p>
 * Description: Classe Controller per file scartati REGE
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
public class RegeFileController extends SiapController implements IRegeFile {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public RegeFileModel ExInserisciRegeFile(RegeFileModel aRegeFile) throws F3BException {

		Connection lConn = null;
		RegeFileDAO lRegDao = null;
		RegeFileModel lRegMod = null;
		try {
			lConn = getDBConnection();
			lRegMod = new RegeFileModel(aRegeFile);
			lRegDao = new RegeFileDAO(lConn);
			lRegDao.setDAOFromModel(aRegeFile);
			// BigDecimal lKey = null;
			/* lKey = */lRegDao.insert();
			commit(lConn);
			// lRegMod.setIdRegeFile(lKey);
		} catch (DAOException ex) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + ex);
			throw new F3BException("RegeFileController.ExInserisci: Non posso inserire: " + ex);
		} finally {
			cleanup(lRegDao);
			cleanup(lConn);
		}
		return lRegMod;
	}

	public BigDecimal ExgetCountFileRege(RegeFileModel aRegeFile) throws F3BException {

		BigDecimal lCount = new BigDecimal(0);
		Connection lConn = null;

		RegeFileSqlDAO lSqlDao = null;
		try {
			lConn = getDBConnection();
			lSqlDao = new RegeFileSqlDAO(lConn);
			lSqlDao.getCountFileRege(aRegeFile);
			lSqlDao.start();
			lSqlDao.next();
			lCount = lSqlDao.getBigDecimal("HowManyRecords");
			lSqlDao.stop();
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error(daoEx.getLocalizedMessage());
			throw new F3BException(F3BException.USER_MESSAGE,
					"RegeFileController.ExgetCountFileRege: Non posso leggere i Fascicoli : " + daoEx);
		} finally {
			cleanup(lSqlDao);
			cleanup(lConn);
		}
		return lCount;
	}

	public Vector ExRicercaRegeFilePage(RegeFileModel aRegeFile, int aPage) throws F3BException {

		Connection lConn = null;
		Vector lRegeFili = new Vector();
		RegeFileSqlDAO lRegDao = null;

		try {
			lConn = getDBConnection();
			lRegDao = new RegeFileSqlDAO(lConn);
			lRegDao.ricercaRegeFilePage(aRegeFile, aPage);
			lRegeFili = new Vector(lRegDao.getModels());
			if (lRegeFili.size() == 0) {
				throw new F3BException(F3BException.USER_MESSAGE, "Nessun Elemento trovato");
			}
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException("RegeFileController.ExRicercaRegeFilePage: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lRegDao);
			cleanup(lConn);
		}
		return lRegeFili;
	}

	public RegeFileModel ExRicercaRegeFileByKey(String aKey) throws F3BException {

		Connection lConn = null;
		RegeFileSqlDAO lRegDao = null;
		RegeFileModel lRegMod;

		try {
			lConn = getDBConnection();
			lRegDao = new RegeFileSqlDAO(lConn);
			lRegDao.ricercaRegeFileByKey(aKey);
			lRegMod = (RegeFileModel) lRegDao.getModelByKey();
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException("RegeFileController.ExRicercaRegeFile: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lRegDao);
			cleanup(lConn);
		}
		return lRegMod;
	}

	public RegeFileModel ExModificaRegeFile(RegeFileModel aRegeFile) throws F3BException {

		Connection lConn = null;
		RegeFileDAO lRegDao = null;
		RegeFileModel lRegMod = new RegeFileModel(aRegeFile);

		try {
			lConn = getDBConnection();
			lRegDao = new RegeFileDAO(lConn);
			// lRegDao.setDAOFromModelForUpdate(aRegeFile );
			lRegDao.setCodStato(aRegeFile.getCodStato());
			lRegDao.setDescErr(aRegeFile.getDescErr());
			lRegDao.setCondizioneUpdate(aRegeFile.getIdFile());

			lRegDao.update();
			lRegDao.update();
			commit(lConn);
		} catch (DAOException ex) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + ex);
			throw new F3BException("RegeFileController.ExModifica: Non posso inserire: " + ex);
		} finally {
			cleanup(lRegDao);
			cleanup(lConn);
		}
		return lRegMod;
	}

	public void ExCancellaRegeFile(RegeFileModel aRegeFile) throws F3BException {

		Connection lConn = null;
		RegeFileDAO lRegDao = null;

		try {
			lConn = getDBConnection();
			lRegDao = new RegeFileDAO(lConn);
			lRegDao.setCondizioneUpdate(aRegeFile.getIdFile());
			lRegDao.delete();
			commit(lConn);
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException("RegeFileController.ExCancellaRegeFile: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lRegDao);
			cleanup(lConn);
		}
	}

}