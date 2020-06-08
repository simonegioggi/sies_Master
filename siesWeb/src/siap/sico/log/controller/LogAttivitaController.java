package siap.sico.log.controller;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Date;
import java.util.Vector;

import f3b.dao.DAOException;
import f3b.util.F3BException;
import siap.controller.SiapController;
import siap.sico.log.dao.LogAttivitaDAO;
import siap.sico.log.dao.LogAttivitaSqlDAO;
import siap.sico.log.model.LogAttivitaModel;

/**
 * <p>
 * Title: LogAttivitaController
 * </p>
 * <p>
 * Description: Classe Controller per LogAttivita
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
public class LogAttivitaController extends SiapController implements ILogAttivita {

	public LogAttivitaModel ExInserisciLogAttivita(LogAttivitaModel aLogAttivita) throws F3BException {

		Connection lConn = null;
		LogAttivitaDAO lLogDao = null;
		LogAttivitaModel lLogMod = null;

		try {
			lConn = getDBConnection();

			lLogMod = new LogAttivitaModel(aLogAttivita);
			lLogDao = new LogAttivitaDAO(lConn);
			lLogDao.setDAOFromModel(aLogAttivita);
			// BigDecimal lKey = null;
			/* lKey = */lLogDao.insert();
			commit(lConn);
		} catch (DAOException ex) {
			rollback(lConn);
			throw new F3BException("LogAttivitaController.ExInserisciLogAttivita: Non posso inserire: " + ex);
		} finally {
			cleanup(lLogDao);
			cleanup(lConn);
		}

		return lLogMod;
	}

	public Vector ExRicercaLogAttivita(LogAttivitaModel aLogAttivita, Date aDataInizio, Date aDataFine,
			String FiltroUtenteConnesso) throws F3BException {

		Connection lConn = null;
		Vector lLogAttiviti = new Vector();
		LogAttivitaSqlDAO lLogDao = null;

		try {
			lConn = getDBConnection();
			lLogDao = new LogAttivitaSqlDAO(lConn);
			lLogDao.ricercaLogAttivita(aLogAttivita, aDataInizio, aDataFine, FiltroUtenteConnesso);
			lLogAttiviti = new Vector(lLogDao.getModels());

			if (lLogAttiviti.size() == 0) {
				throw new F3BException(F3BException.USER_MESSAGE, "Nessun Elemento trovato");
			}
		} catch (DAOException daoEx) {
			throw new F3BException(
					"LogAttivitaController.ExRicercaLogAttivita: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lLogDao);
			cleanup(lConn);
		}

		return lLogAttiviti;
	}

	public LogAttivitaModel ExRicercaLogAttivitaByKey(BigDecimal aKey) throws F3BException {

		Connection lConn = null;
		LogAttivitaSqlDAO lLogDao = null;
		LogAttivitaModel lLogMod;

		try {
			lConn = getDBConnection();
			lLogDao = new LogAttivitaSqlDAO(lConn);
			lLogDao.ricercaLogAttivitaByKey(aKey);
			lLogMod = (LogAttivitaModel) lLogDao.getModelByKey();
		} catch (DAOException daoEx) {
			throw new F3BException(
					"LogAttivitaController.ExRicercaLogAttivitaByKey: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lLogDao);
			cleanup(lConn);
		}

		return lLogMod;
	}

	public LogAttivitaModel ExModificaLogAttivita(LogAttivitaModel aLogAttivita) throws F3BException {

		Connection lConn = null;
		LogAttivitaDAO lLogDao = null;
		LogAttivitaModel lLogMod = new LogAttivitaModel(aLogAttivita);

		try {
			lConn = getDBConnection();
			lLogDao = new LogAttivitaDAO(lConn);
			lLogDao.setDAOFromModelForUpdate(aLogAttivita);
			lLogDao.update();
			commit(lConn);
		} catch (DAOException ex) {
			rollback(lConn);
			throw new F3BException("LogAttivitaController.ExModificaLogAttivita: Non posso inserire: " + ex);
		} finally {
			cleanup(lLogDao);
			cleanup(lConn);
		}

		return lLogMod;
	}

	public void ExCancellaLogAttivita(LogAttivitaModel aLogAttivita) throws F3BException {

		Connection lConn = null;
		LogAttivitaDAO lLogDao = null;

		try {
			lConn = getDBConnection();
			lLogDao = new LogAttivitaDAO(lConn);
			// lLogDao.setCondizioneUpdate(aLogAttivita.getIdLogAttivita());
			lLogDao.delete();
		} catch (DAOException daoEx) {
			throw new F3BException(
					"LogAttivitaController.ExCancellaLogAttivita: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lLogDao);
			cleanup(lConn);
		}
	}

}