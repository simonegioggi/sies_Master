package siap.siep.alias.controller;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Vector;

import f3b.dao.DAOException;
import f3b.util.F3BException;
import siap.controller.SiapController;
import siap.siep.alias.dao.AliasDAO;
import siap.siep.alias.dao.AliasSqlDAO;
import siap.siep.alias.model.AliasModel;

/**
 * <p>
 * Title: AliasController
 * </p>
 * <p>
 * Description: Classe Controller per Alias
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
public class AliasController extends SiapController implements IAlias {

	public AliasModel ExInserisciAlias(AliasModel aAlias) throws F3BException {

		Connection lConn = null;
		AliasDAO lAliDao = null;
		AliasModel lAliMod = null;

		try {
			lConn = getDBConnection();
			lAliMod = new AliasModel(aAlias);
			lAliDao = new AliasDAO(lConn);
			lAliDao.setDAOFromModel(aAlias);
			BigDecimal lKey = null;
			lKey = lAliDao.insert();
			commit(lConn);
			lAliMod.setIdAlias(lKey);
		} catch (DAOException ex) {
			rollback(lConn);
			throw new F3BException("AliasController.ExInserisci: Non posso inserire: " + ex);
		} finally {
			cleanup(lAliDao);
			cleanup(lConn);
		}
		return lAliMod;
	}

	public Vector ExRicercaAlias(AliasModel aAlias) throws F3BException {

		Connection lConn = null;
		Vector lAliai = new Vector();
		AliasSqlDAO lAliDao = null;

		try {
			lConn = getDBConnection();
			lAliDao = new AliasSqlDAO(lConn);
			lAliDao.ricercaAlias(aAlias);
			lAliai = new Vector(lAliDao.getModels());
			if (lAliai.size() == 0) {
				throw new F3BException(F3BException.USER_MESSAGE, "Nessun Elemento trovato");
			}
		} catch (DAOException daoEx) {
			throw new F3BException("AliasController.ExRicercaAlias: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lAliDao);
			cleanup(lConn);
		}
		return lAliai;
	}

	public Vector ExRicercaAliasByIdSoggetto(BigDecimal aKey) throws F3BException {

		Connection lConn = null;
		Vector lAliai = new Vector();
		AliasSqlDAO lAliDao = null;
		try {
			lConn = getDBConnection();
			lAliDao = new AliasSqlDAO(lConn);
			lAliDao.ricercaAliasByIdSoggetto(aKey);
			lAliai = new Vector(lAliDao.getModels());
			if (lAliai.size() == 0) {
				throw new F3BException(F3BException.USER_MESSAGE, "Nessun Elemento trovato");
			}
		} catch (DAOException daoEx) {
			throw new F3BException(
					"AliasController.ExRicercaAliasByIdSoggetto: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lAliDao);
			cleanup(lConn);
		}
		return lAliai;
	}

	public Vector ExRicercaAliasByIdSoggettoPaged(BigDecimal aKey, int aPage) throws F3BException {

		Connection lConn = null;
		Vector lAliai = new Vector();
		AliasSqlDAO lAliDao = null;
		try {
			lConn = getDBConnection();
			lAliDao = new AliasSqlDAO(lConn);
			lAliDao.ricercaAliasByIdSoggettoPaged(aKey, aPage);
			lAliai = new Vector(lAliDao.getModels());
			if (lAliai.size() == 0) {
				throw new F3BException(F3BException.USER_MESSAGE, "Nessun Elemento trovato");
			}
		} catch (DAOException daoEx) {
			throw new F3BException(
					"AliasController.ExRicercaAliasByIdSoggetto: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lAliDao);
			cleanup(lConn);
		}
		return lAliai;
	}

	public BigDecimal ExGetCountAliasByIdSoggetto(BigDecimal aKey) throws F3BException {

		BigDecimal lCount = new BigDecimal(0);
		Connection lConn = null;
		AliasSqlDAO lAliSqlDao = null;

		try {
			lConn = getDBConnection();
			lAliSqlDao = new AliasSqlDAO(lConn);
			lAliSqlDao.getCountAliasByIdSoggetto(aKey);
			lAliSqlDao.start();
			lAliSqlDao.next();
			lCount = lAliSqlDao.getBigDecimal("HowManyRecords");
			lAliSqlDao.stop();
		} catch (DAOException daoEx) {
			throw new F3BException("AliasController.ExGetCountAliasByIdSoggetto: " + daoEx);
		} finally {
			cleanup(lAliSqlDao);
			cleanup(lConn);
		}
		return lCount;
	}

	public AliasModel ExRicercaAliasByKey(BigDecimal aKey) throws F3BException {

		Connection lConn = null;
		AliasSqlDAO lAliDao = null;
		AliasModel lAliMod;

		try {
			lConn = getDBConnection();
			lAliDao = new AliasSqlDAO(lConn);
			lAliDao.ricercaAliasByKey(aKey);
			lAliMod = (AliasModel) lAliDao.getModelByKey();
		} catch (DAOException daoEx) {
			throw new F3BException("AliasController.ExRicercaAliasByKey: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lAliDao);
			cleanup(lConn);
		}
		return lAliMod;
	}

	public AliasModel ExModificaAlias(AliasModel aAlias) throws F3BException {

		Connection lConn = null;
		AliasDAO lAliDao = null;
		AliasModel lAliMod = new AliasModel(aAlias);

		try {
			lConn = getDBConnection();
			lAliDao = new AliasDAO(lConn);
			lAliDao.setDAOFromModelForUpdate(aAlias);
			lAliDao.update();
			lAliDao.update();
			commit(lConn);
		} catch (DAOException ex) {
			rollback(lConn);
			throw new F3BException("AliasController.ExModifica: Non posso inserire: " + ex);
		} finally {
			cleanup(lAliDao);
			cleanup(lConn);
		}
		return lAliMod;
	}

	public void ExCancellaAlias(AliasModel aAlias) throws F3BException {

		Connection lConn = null;
		AliasDAO lAliDao = null;

		try {
			lConn = getDBConnection();
			lAliDao = new AliasDAO(lConn);
			lAliDao.setCondizioneUpdate(aAlias.getIdAlias());
			lAliDao.delete();
			commit(lConn);
		} catch (DAOException daoEx) {
			throw new F3BException("AliasController.ExCancellaAlias: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lAliDao);
			cleanup(lConn);
		}
	}

}