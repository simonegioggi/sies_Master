package siap.sico.w_magistrato.controller;

import java.sql.Connection;
import java.util.Vector;

import f3b.dao.DAOException;
import f3b.util.F3BException;
import siap.controller.SiapController;
import siap.sico.w_magistrato.dao.WMagistratoDAO;
import siap.sico.w_magistrato.dao.WMagistratoSqlDAO;
import siap.sico.w_magistrato.model.WMagistratoModel;

/**
 * <p>
 * Title: WMagistratoController
 * </p>
 * <p>
 * Description: Classe Controller per WMagistrato
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
public class WMagistratoController extends SiapController implements IWMagistrato {

	public WMagistratoModel ExInserisciWMagistrato(WMagistratoModel aWMagistrato) throws F3BException {

		Connection lConn = null;
		WMagistratoDAO lWMaDao = null;
		WMagistratoModel lWMaMod = null;

		try {
			lConn = getDBConnection();
			lWMaMod = new WMagistratoModel(aWMagistrato);
			lWMaDao = new WMagistratoDAO(lConn);
			lWMaDao.setDAOFromModel(aWMagistrato);
			// BigDecimal lKey = null;
			/* lKey = */lWMaDao.insert();
			commit(lConn);
			// lWMaMod.setIdWMagistrato(lKey); commento Luigi:8-5-03
		} catch (DAOException ex) {
			rollback(lConn);
			throw new F3BException("WMagistratoController.ExInserisci: Non posso inserire: " + ex);
		} finally {
			cleanup(lWMaDao);
			cleanup(lConn);
		}
		return lWMaMod;
	}

	public Vector ExRicercaWMagistratoByCognome(String aCognome) throws F3BException {

		Connection lConn = null;
		Vector lWMagistrati = new Vector();
		WMagistratoSqlDAO lWMaDao = null;

		try {
			lConn = getDBConnection();
			lWMaDao = new WMagistratoSqlDAO(lConn);
			lWMaDao.ricercaWMagistratoByCognome(aCognome);
			lWMagistrati = new Vector(lWMaDao.getModels());
			if (lWMagistrati.size() == 0) {
				throw new F3BException(F3BException.USER_MESSAGE, "Nessun Elemento trovato");
			}
		} catch (DAOException daoEx) {
			throw new F3BException(
					"WMagistratoController.ExRicercaWMagistrato: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lWMaDao);
			cleanup(lConn);
		}
		return lWMagistrati;
	}

	public Vector ExRicercaWMagistrato(WMagistratoModel aWMagistrato) throws F3BException {

		Connection lConn = null;
		Vector lWMagistrati = new Vector();
		WMagistratoSqlDAO lWMaDao = null;

		try {
			lConn = getDBConnection();
			lWMaDao = new WMagistratoSqlDAO(lConn);
			lWMaDao.ricercaWMagistrato(aWMagistrato);
			lWMagistrati = new Vector(lWMaDao.getModels());
			if (lWMagistrati.size() == 0) {
				throw new F3BException(F3BException.USER_MESSAGE, "Nessun Elemento trovato");
			}
		} catch (DAOException daoEx) {
			throw new F3BException(
					"WMagistratoController.ExRicercaWMagistrato: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lWMaDao);
			cleanup(lConn);
		}
		return lWMagistrati;
	}

	public WMagistratoModel ExRicercaWMagistratoByKey(String aKey) throws F3BException {

		Connection lConn = null;
		WMagistratoSqlDAO lWMaDao = null;
		WMagistratoModel lWMaMod;

		try {
			lConn = getDBConnection();
			lWMaDao = new WMagistratoSqlDAO(lConn);
			lWMaDao.ricercaWMagistratoByKey(aKey);
			lWMaMod = (WMagistratoModel) lWMaDao.getModelByKey();
		} catch (DAOException daoEx) {
			throw new F3BException(
					"WMagistratoController.ExRicercaWMagistrato: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lWMaDao);
			cleanup(lConn);
		}
		return lWMaMod;
	}

	public WMagistratoModel ExModificaWMagistrato(WMagistratoModel aWMagistrato) throws F3BException {

		Connection lConn = null;
		WMagistratoDAO lWMaDao = null;
		WMagistratoModel lWMaMod = new WMagistratoModel(aWMagistrato);

		try {
			lConn = getDBConnection();
			lWMaDao = new WMagistratoDAO(lConn);
			lWMaDao.setDAOFromModelForUpdate(aWMagistrato);
			lWMaDao.update();
			lWMaDao.update();
			commit(lConn);
		} catch (DAOException ex) {
			rollback(lConn);
			throw new F3BException("WMagistratoController.ExModifica: Non posso inserire: " + ex);
		} finally {
			cleanup(lWMaDao);
			cleanup(lConn);
		}
		return lWMaMod;
	}

	public void ExCancellaWMagistrato(WMagistratoModel aWMagistrato) throws F3BException {

		Connection lConn = null;
		WMagistratoDAO lWMaDao = null;

		try {
			lConn = getDBConnection();
			lWMaDao = new WMagistratoDAO(lConn);
			// lWMaDao.setCondizioneUpdate(aWMagistrato.getIdWMagistrato()); commento Luigi: 8-5-03
			lWMaDao.delete();
			commit(lConn);
		} catch (DAOException daoEx) {
			throw new F3BException(
					"WMagistratoController.ExCancellaWMagistrato: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lWMaDao);
			cleanup(lConn);
		}
	}

}