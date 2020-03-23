package siap.siep.scarti.controller;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Vector;

import f3b.dao.DAOException;
import f3b.util.F3BException;
import siap.controller.SiapController;
import siap.siep.SIEPException;
import siap.siep.scarti.dao.WScartiDAO;
import siap.siep.scarti.dao.WScartiSqlDAO;
import siap.siep.scarti.model.WScartiModel;

/**
 * <p>
 * Title: WScartiController
 * </p>
 * <p>
 * Description: Classe Controller per WScarti
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
public class WScartiController extends SiapController implements IWScarti {

	public WScartiModel ExInserisciWScarti(WScartiModel aWScarti) throws F3BException {

		Connection lConn = null;
		WScartiDAO lWScDao = null;
		WScartiModel lWScMod = null;

		try {
			lConn = getDBConnection();
			lWScMod = new WScartiModel(aWScarti);
			lWScDao = new WScartiDAO(lConn);
			lWScDao.setDAOFromModel(aWScarti);
			BigDecimal lKey = null;
			lKey = lWScDao.insert();
			commit(lConn);
			lWScMod.setIdScarti(lKey);
		} catch (DAOException ex) {
			rollback(lConn);
			throw new F3BException("WScartiController.ExInserisci: Non posso inserire: " + ex);
		} finally {
			cleanup(lWScDao);
			cleanup(lConn);
		}
		return lWScMod;
	}

	public Vector ExRicercaWScarti(WScartiModel aWScarti) throws F3BException {

		Connection lConn = null;
		Vector lWScarti = new Vector();
		WScartiSqlDAO lWScDao = null;

		try {
			lConn = getDBConnection();
			lWScDao = new WScartiSqlDAO(lConn);
			lWScDao.ricercaWScarti(aWScarti);
			lWScarti = new Vector(lWScDao.getModels());
			if (lWScarti.size() == 0) {
				throw new F3BException(F3BException.USER_MESSAGE, "Nessun Elemento trovato");
			}
		} catch (DAOException daoEx) {
			throw new F3BException("WScartiController.ExRicercaWScarti: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lWScDao);
			cleanup(lConn);
		}
		return lWScarti;
	}

	public WScartiModel ExRicercaWScartiByKey(BigDecimal aKey) throws F3BException {

		Connection lConn = null;
		WScartiSqlDAO lWScDao = null;
		WScartiModel lWScMod;

		try {
			lConn = getDBConnection();
			lWScDao = new WScartiSqlDAO(lConn);
			lWScDao.ricercaWScartiByKey(aKey);
			lWScMod = (WScartiModel) lWScDao.getModelByKey();
		} catch (DAOException daoEx) {
			throw new F3BException("WScartiController.ExRicercaWScarti: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lWScDao);
			cleanup(lConn);
		}
		return lWScMod;
	}

	public WScartiModel ExModificaWScarti(WScartiModel aWScarti) throws F3BException {

		Connection lConn = null;
		WScartiDAO lWScDao = null;
		WScartiModel lWScMod = new WScartiModel(aWScarti);

		try {
			lConn = getDBConnection();
			lWScDao = new WScartiDAO(lConn);
			lWScDao.setDAOFromModelForUpdate(aWScarti);
			lWScDao.update();
			lWScDao.update();
			commit(lConn);
		} catch (DAOException ex) {
			rollback(lConn);
			throw new F3BException("WScartiController.ExModifica: Non posso inserire: " + ex);
		} finally {
			cleanup(lWScDao);
			cleanup(lConn);
		}
		return lWScMod;
	}

	public void ExCancellaWScarti(WScartiModel aWScarti) throws F3BException {

		Connection lConn = null;
		WScartiDAO lWScDao = null;

		try {
			lConn = getDBConnection();
			lWScDao = new WScartiDAO(lConn);
			lWScDao.setCondizioneUpdate(aWScarti.getIdScarti());
			lWScDao.delete();
			commit(lConn);
		} catch (DAOException daoEx) {
			throw new F3BException("WScartiController.ExCancellaWScarti: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lWScDao);
			cleanup(lConn);
		}
	}

	public Vector ExRicercaScartiPaged(WScartiModel aWScarti, int aPage) throws F3BException {

		Connection lConn = null;
		Vector lScarti = new Vector();
		WScartiSqlDAO lScartiDao = null;

		try {
			lConn = getDBConnection();
			lScartiDao = new WScartiSqlDAO(lConn);
			lScartiDao.ricercaScartiPaged(aWScarti, aPage);
			lScartiDao.start();

			while (lScartiDao.next()) {
				lScarti.add(lScartiDao.getModel());
			}

			lScartiDao.stop();

			if (lScarti.size() == 0)
				throw new SIEPException(SIEPException.USER_MESSAGE, "Nessun Elemento trovato");
		} catch (DAOException daoEx) {
			throw new F3BException("WScartiController.ExRicercaScartiPaged: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lScartiDao);
			cleanup(lConn);
		}

		return lScarti;
	}

	public BigDecimal ExGetCountScarti(WScartiModel aScarti) throws F3BException {

		BigDecimal lCount = new BigDecimal(0);
		Connection lConn = null;

		WScartiSqlDAO lScartiDao = null;
		try {
			lConn = getDBConnection();
			lScartiDao = new WScartiSqlDAO(lConn);
			lScartiDao.getCountScarti(aScarti);
			lScartiDao.start();
			lScartiDao.next();
			lCount = lScartiDao.getBigDecimal("HowManyRecords");
			lScartiDao.stop();
		} catch (DAOException daoEx) {
			throw new SIEPException(SIEPException.USER_MESSAGE,
					"WScartiController.ExGetCountScarti: Non posso leggere gli elementi : " + daoEx);
		} finally {
			cleanup(lScartiDao);
			cleanup(lConn);
		}
		return lCount;
	}

}