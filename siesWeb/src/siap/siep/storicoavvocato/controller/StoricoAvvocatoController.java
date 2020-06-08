package siap.siep.storicoavvocato.controller;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Vector;

import f3b.dao.DAOException;
import f3b.util.F3BException;
import siap.controller.SiapController;
import siap.siep.storicoavvocato.dao.StoricoAvvocatoDAO;
import siap.siep.storicoavvocato.dao.StoricoAvvocatoSqlDAO;
import siap.siep.storicoavvocato.model.StoricoAvvocatoModel;

/**
 * <p>
 * Title: StoricoAvvocatoController
 * </p>
 * <p>
 * Description: Classe Controller per StoricoAvvocato
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
public class StoricoAvvocatoController extends SiapController implements IStoricoAvvocato {

	public StoricoAvvocatoModel ExInserisciStoricoAvvocato(StoricoAvvocatoModel aStoricoAvvocato)
			throws F3BException {

		Connection lConn = null;
		StoricoAvvocatoDAO lStoDao = null;
		StoricoAvvocatoModel lStoMod = null;

		try {
			lConn = getDBConnection();
			lStoMod = new StoricoAvvocatoModel(aStoricoAvvocato);
			lStoDao = new StoricoAvvocatoDAO(lConn);
			lStoDao.setDAOFromModel(aStoricoAvvocato);
			BigDecimal lKey = null;
			lKey = lStoDao.insert();
			commit(lConn);
			lStoMod.setIdStoricoAvvocato(lKey);
		} catch (DAOException ex) {
			rollback(lConn);
			throw new F3BException("StoricoAvvocatoController.ExInserisci: Non posso inserire: " + ex);
		} finally {
			cleanup(lStoDao);
			cleanup(lConn);
		}
		return lStoMod;
	}

	public Vector ExRicercaStoricoAvvocato(StoricoAvvocatoModel aStoricoAvvocato) throws F3BException {

		Connection lConn = null;
		Vector lStoricoAvvocati = new Vector();
		StoricoAvvocatoSqlDAO lStoDao = null;

		try {
			lConn = getDBConnection();
			lStoDao = new StoricoAvvocatoSqlDAO(lConn);
			lStoDao.ricercaStoricoAvvocato(aStoricoAvvocato);
			lStoricoAvvocati = new Vector(lStoDao.getModels());
			if (lStoricoAvvocati.size() == 0) {
				throw new F3BException(F3BException.USER_MESSAGE, "Nessun Elemento trovato");
			}
		} catch (DAOException daoEx) {
			throw new F3BException(
					"StoricoAvvocatoController.ExRicercaStoricoAvvocato: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lStoDao);
			cleanup(lConn);
		}
		return lStoricoAvvocati;
	}

	public Vector ExRicercaStoricoAvvocatoByIdAvvocato(BigDecimal aKey) throws F3BException {

		Connection lConn = null;
		Vector lStoricoAvvocati = new Vector();
		StoricoAvvocatoSqlDAO lStoDao = null;

		try {
			lConn = getDBConnection();
			lStoDao = new StoricoAvvocatoSqlDAO(lConn);
			lStoDao.ricercaStoricoAvvocatoByIdAvvocato(aKey);
			lStoricoAvvocati = new Vector(lStoDao.getModels());
			if (lStoricoAvvocati.size() == 0) {
				throw new F3BException(F3BException.USER_MESSAGE, "Nessun Elemento trovato");
			}
		} catch (DAOException daoEx) {
			throw new F3BException(
					"StoricoAvvocatoController.ExRicercaStoricoAvvocato: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lStoDao);
			cleanup(lConn);
		}
		return lStoricoAvvocati;
	}

	public StoricoAvvocatoModel ExRicercaStoricoAvvocatoByKey(BigDecimal aKey) throws F3BException {

		Connection lConn = null;
		StoricoAvvocatoSqlDAO lStoDao = null;
		StoricoAvvocatoModel lStoMod;

		try {
			lConn = getDBConnection();
			lStoDao = new StoricoAvvocatoSqlDAO(lConn);
			lStoDao.ricercaStoricoAvvocatoByKey(aKey);
			lStoMod = (StoricoAvvocatoModel) lStoDao.getModelByKey();
		} catch (DAOException daoEx) {
			throw new F3BException(
					"StoricoAvvocatoController.ExRicercaStoricoAvvocato: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lStoDao);
			cleanup(lConn);
		}
		return lStoMod;
	}

	public StoricoAvvocatoModel ExModificaStoricoAvvocato(StoricoAvvocatoModel aStoricoAvvocato)
			throws F3BException {

		Connection lConn = null;
		StoricoAvvocatoDAO lStoDao = null;
		StoricoAvvocatoModel lStoMod = new StoricoAvvocatoModel(aStoricoAvvocato);

		try {
			lConn = getDBConnection();
			lStoDao = new StoricoAvvocatoDAO(lConn);
			lStoDao.setDAOFromModelForUpdate(aStoricoAvvocato);
			lStoDao.update();
			lStoDao.update();
			commit(lConn);
		} catch (DAOException ex) {
			rollback(lConn);
			throw new F3BException("StoricoAvvocatoController.ExModifica: Non posso inserire: " + ex);
		} finally {
			cleanup(lStoDao);
			cleanup(lConn);
		}
		return lStoMod;
	}

	public void ExCancellaStoricoAvvocato(StoricoAvvocatoModel aStoricoAvvocato) throws F3BException {

		Connection lConn = null;
		StoricoAvvocatoDAO lStoDao = null;

		try {
			lConn = getDBConnection();
			lStoDao = new StoricoAvvocatoDAO(lConn);
			lStoDao.setCondizioneUpdate(aStoricoAvvocato.getIdStoricoAvvocato());
			lStoDao.delete();
			commit(lConn);
		} catch (DAOException daoEx) {
			throw new F3BException(
					"StoricoAvvocatoController.ExCancellaStoricoAvvocato: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lStoDao);
			cleanup(lConn);
		}
	}

}