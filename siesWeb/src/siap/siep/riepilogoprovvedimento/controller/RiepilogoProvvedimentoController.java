package siap.siep.riepilogoprovvedimento.controller;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Vector;

import f3b.dao.DAOException;
import f3b.util.F3BException;
import siap.controller.SiapController;
import siap.siep.riepilogoprovvedimento.dao.RiepilogoProvvedimentoDAO;
import siap.siep.riepilogoprovvedimento.dao.RiepilogoProvvedimentoSqlDAO;
import siap.siep.riepilogoprovvedimento.model.RiepilogoProvvedimentoModel;

/**
 * <p>
 * Title: RiepilogoProvvedimentoController
 * </p>
 * <p>
 * Description: Classe Controller per RiepilogoProvvedimento
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
public class RiepilogoProvvedimentoController extends SiapController implements IRiepilogoProvvedimento {

	public RiepilogoProvvedimentoModel ExInserisciRiepilogoProvvedimento(
			RiepilogoProvvedimentoModel aRiepilogoProvvedimento) throws F3BException {

		Connection lConn = null;
		RiepilogoProvvedimentoDAO lRieDao = null;
		RiepilogoProvvedimentoModel lRieMod = null;

		try {
			lConn = getDBConnection();
			lRieMod = new RiepilogoProvvedimentoModel(aRiepilogoProvvedimento);
			lRieDao = new RiepilogoProvvedimentoDAO(lConn);
			lRieDao.setDAOFromModel(aRiepilogoProvvedimento);
			BigDecimal lKey = null;
			lKey = lRieDao.insert();
			commit(lConn);
			lRieMod.setIdRiepilogoProvvedimento(lKey);
		} catch (DAOException ex) {
			rollback(lConn);
			throw new F3BException("RiepilogoProvvedimentoController.ExInserisci: Non posso inserire: " + ex);
		} finally {
			cleanup(lRieDao);
			cleanup(lConn);
		}
		return lRieMod;
	}

	public Vector ExRicercaRiepilogoProvvedimento(RiepilogoProvvedimentoModel aRiepilogoProvvedimento)
			throws F3BException {

		Connection lConn = null;
		Vector lRiepilogoProvvedimenti = new Vector();
		RiepilogoProvvedimentoSqlDAO lRieDao = null;

		try {
			lConn = getDBConnection();
			lRieDao = new RiepilogoProvvedimentoSqlDAO(lConn);
			lRieDao.ricercaRiepilogoProvvedimento(aRiepilogoProvvedimento);
			lRiepilogoProvvedimenti = new Vector(lRieDao.getModels());
			if (lRiepilogoProvvedimenti.size() == 0) {
				throw new F3BException(F3BException.USER_MESSAGE, "Nessun Elemento trovato");
			}
		} catch (DAOException daoEx) {
			throw new F3BException(
					"RiepilogoProvvedimentoController.ExRicercaRiepilogoProvvedimento: Non posso leggere : "
							+ daoEx);
		} finally {
			cleanup(lRieDao);
			cleanup(lConn);
		}
		return lRiepilogoProvvedimenti;
	}

	public RiepilogoProvvedimentoModel ExRicercaRiepilogoProvvedimentoByKey(BigDecimal aKey)
			throws F3BException {

		Connection lConn = null;
		RiepilogoProvvedimentoSqlDAO lRieDao = null;
		RiepilogoProvvedimentoModel lRieMod;

		try {
			lConn = getDBConnection();
			lRieDao = new RiepilogoProvvedimentoSqlDAO(lConn);
			lRieDao.ricercaRiepilogoProvvedimentoByKey(aKey);
			lRieMod = (RiepilogoProvvedimentoModel) lRieDao.getModelByKey();
		} catch (DAOException daoEx) {
			throw new F3BException(
					"RiepilogoProvvedimentoController.ExRicercaRiepilogoProvvedimento: Non posso leggere : "
							+ daoEx);
		} finally {
			cleanup(lRieDao);
			cleanup(lConn);
		}
		return lRieMod;
	}

	public RiepilogoProvvedimentoModel ExModificaRiepilogoProvvedimento(
			RiepilogoProvvedimentoModel aRiepilogoProvvedimento) throws F3BException {

		Connection lConn = null;
		RiepilogoProvvedimentoDAO lRieDao = null;
		RiepilogoProvvedimentoModel lRieMod = new RiepilogoProvvedimentoModel(aRiepilogoProvvedimento);

		try {
			lConn = getDBConnection();
			lRieDao = new RiepilogoProvvedimentoDAO(lConn);
			lRieDao.setDAOFromModelForUpdate(aRiepilogoProvvedimento);
			lRieDao.update();
			lRieDao.update();
			commit(lConn);
		} catch (DAOException ex) {
			rollback(lConn);
			throw new F3BException("RiepilogoProvvedimentoController.ExModifica: Non posso inserire: " + ex);
		} finally {
			cleanup(lRieDao);
			cleanup(lConn);
		}
		return lRieMod;
	}

	public void ExCancellaRiepilogoProvvedimento(RiepilogoProvvedimentoModel aRiepilogoProvvedimento)
			throws F3BException {

		Connection lConn = null;
		RiepilogoProvvedimentoDAO lRieDao = null;

		try {
			lConn = getDBConnection();
			lRieDao = new RiepilogoProvvedimentoDAO(lConn);
			lRieDao.setCondizioneUpdate(aRiepilogoProvvedimento.getIdRiepilogoProvvedimento());
			lRieDao.delete();
			commit(lConn);
		} catch (DAOException daoEx) {
			throw new F3BException(
					"RiepilogoProvvedimentoController.ExCancellaRiepilogoProvvedimento: Non posso leggere : "
							+ daoEx);
		} finally {
			cleanup(lRieDao);
			cleanup(lConn);
		}
	}

}