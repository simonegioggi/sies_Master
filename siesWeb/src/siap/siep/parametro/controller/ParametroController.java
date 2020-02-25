package siap.siep.parametro.controller;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Vector;

import siap.controller.SiapController;
import siap.siep.parametro.dao.ParametroDAO;
import siap.siep.parametro.dao.ParametroSqlDAO;
import siap.siep.parametro.model.ParametroModel;
import f3b.dao.DAOException;
import f3b.util.F3BException;

/**
 * <p>
 * Title: ParametroController
 * </p>
 * <p>
 * Description: Classe Controller per Parametro
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
public class ParametroController extends SiapController implements IParametro {

	public ParametroModel ExInserisciParametro(ParametroModel aParametro) throws F3BException {

		Connection lConn = null;
		ParametroDAO lParDao = null;
		ParametroModel lParMod = null;

		try {
			lConn = getDBConnection();
			lParMod = new ParametroModel(aParametro);
			lParDao = new ParametroDAO(lConn);
			lParDao.setDAOFromModel(aParametro);
			BigDecimal lKey = null;
			lKey = lParDao.insert();
			commit(lConn);
			lParMod.setIdParametro(lKey);
		} catch (DAOException ex) {
			rollback(lConn);
			throw new F3BException("ParametroController.ExInserisciParametro: " + ex);
		} finally {
			cleanup(lParDao);
			cleanup(lConn);
		}
		return lParMod;
	}

	public Vector ExRicercaParametro(ParametroModel aParametro) throws F3BException {
		Connection lConn = null;
		Vector lParametri = new Vector();
		ParametroSqlDAO lParDao = null;

		try {
			lConn = getDBConnection();

			lParDao = new ParametroSqlDAO(lConn);
			lParDao.ricercaParametro(aParametro);
			lParametri = new Vector(lParDao.getModels());

			if (lParametri.size() == 0) {
				throw new F3BException(F3BException.USER_MESSAGE, "Nessun Elemento trovato");
			}
		} catch (DAOException daoEx) {
			throw new F3BException("ParametroController.ExRicercaParametro: " + daoEx);
		} finally {
			cleanup(lParDao);
			cleanup(lConn);
		}
		return lParametri;
	}

	public ParametroModel ExRicercaParametroByKey(BigDecimal aKey) throws F3BException {
		Connection lConn = null;
		ParametroSqlDAO lParDao = null;
		ParametroModel lParMod;

		try {
			lConn = getDBConnection();
			lParDao = new ParametroSqlDAO(lConn);
			lParDao.ricercaParametroByKey(aKey);
			lParMod = (ParametroModel) lParDao.getModelByKey();
		} catch (DAOException daoEx) {
			throw new F3BException("ParametroController.ExRicercaParametro: " + daoEx);
		} finally {
			cleanup(lParDao);
			cleanup(lConn);
		}
		return lParMod;
	}

	public Vector ExRicercaParametroScadenzario(ParametroModel aParametro) throws F3BException {
		Connection lConn = null;

		Vector lParametri = new Vector();
		Vector lParametro = new Vector();
		ParametroSqlDAO lParDao = null;

		try {
			lConn = getDBConnection();

			lParDao = new ParametroSqlDAO(lConn);
			lParDao.ricercaParametroScadenzario(aParametro.getNomeParametro(),
					aParametro.getCodUfficioValidita());
			lParametri = new Vector(lParDao.getModels());

			if (lParametri.size() == 0) {
				lParDao.ricercaParametroScadenzario(aParametro.getNomeParametro(), null);
				lParametro = new Vector(lParDao.getModels());
				if (lParametro.size() == 0) {
					throw new F3BException(F3BException.USER_MESSAGE, "Parametro "
							+ aParametro.getNomeParametro() + " non impostato");
				}
			}
		} catch (DAOException daoEx) {
			throw new F3BException("ParametroController.ExRicercaParametro: " + daoEx);
		} finally {
			cleanup(lParDao);
			cleanup(lConn);
		}

		if (lParametri.size() == 0) {
			return lParametro;
		} else {
			return lParametri;
		}
	}

	public Vector ExRicercaParametroUfficioConnesso(ParametroModel aParametro) throws F3BException {
		Connection lConn = null;
		Vector lParametri = new Vector();
		ParametroSqlDAO lParDao = null;

		try {
			lConn = getDBConnection();

			lParDao = new ParametroSqlDAO(lConn);
			lParDao.ricercaParametroUfficioConnesso(aParametro);
			lParametri = new Vector(lParDao.getModels());
		} catch (DAOException daoEx) {
			throw new F3BException("ParametroController.ExRicercaParametroUfficioConnesso: " + daoEx);
		} finally {
			cleanup(lParDao);
			cleanup(lConn);
		}

		return lParametri;
	}

	public ParametroModel ExModificaParametro(ParametroModel aParametro) throws F3BException {
		Connection lConn = null;

		ParametroDAO lParDao = null;
		ParametroModel lParMod = new ParametroModel(aParametro);

		try {
			lConn = getDBConnection();

			lParDao = new ParametroDAO(lConn);
			lParDao.setDAOFromModelForUpdate(aParametro);
			lParDao.update();

			commit(lConn);
		} catch (DAOException ex) {
			rollback(lConn);
			throw new F3BException("ParametroController.ExModificaParametro: " + ex);
		} finally {
			cleanup(lParDao);
			cleanup(lConn);
		}

		return lParMod;
	}

	public void ExCancellaParametro(ParametroModel aParametro) throws F3BException {
		Connection lConn = null;

		ParametroDAO lParDao = null;

		try {
			lConn = getDBConnection();

			lParDao = new ParametroDAO(lConn);
			lParDao.setCondizioneUpdate(aParametro.getIdParametro());
			lParDao.delete();

			commit(lConn);
		} catch (DAOException daoEx) {
			throw new F3BException("ParametroController.ExCancellaParametro: " + daoEx);
		} finally {
			cleanup(lParDao);
			cleanup(lConn);
		}
	}

	public ParametroModel ExRicercaParametroUfficioConnesso(String aNomeParametro, String aUfficioValidita)
			throws F3BException {
		Connection lConn = null;

		ParametroSqlDAO lParSqlDao = null;

		ParametroModel lParMod = null;

		try {
			lConn = getDBConnection();

			lParSqlDao = new ParametroSqlDAO(lConn);
			lParSqlDao.ricercaParametroUfficioConnesso(aNomeParametro, aUfficioValidita);
			lParMod = (ParametroModel) lParSqlDao.getModelByKey();
		} catch (DAOException daoEx) {
			throw new F3BException("ParametroController.ExRicercaParametroUfficioConnesso: " + daoEx);
		} finally {
			cleanup(lParSqlDao);
			cleanup(lConn);
		}

		return lParMod;
	}

}