package siap.siep.continuazione.controller;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Vector;

import f3b.dao.DAOException;
import f3b.util.F3BException;
import siap.controller.SiapController;
import siap.siep.continuazione.dao.ContinuazioneDAO;
import siap.siep.continuazione.dao.ContinuazioneSqlDAO;
import siap.siep.continuazione.model.ContinuazioneModel;

/**
 * <p>
 * Title: ContinuazioneController
 * </p>
 * <p>
 * Description: Classe Controller per Continuazione
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
public class ContinuazioneController extends SiapController implements IContinuazione {

	public ContinuazioneModel ExInserisciContinuazione(ContinuazioneModel aContinuazione)
			throws F3BException {

		Connection lConn = null;

		ContinuazioneDAO lConDao = null;
		ContinuazioneModel lConMod = null;

		try {
			lConn = getDBConnection();

			lConMod = new ContinuazioneModel(aContinuazione);

			lConDao = new ContinuazioneDAO(lConn);

			lConDao.setDAOFromModel(aContinuazione);

			BigDecimal lKey = null;

			lKey = lConDao.insert();

			commit(lConn);

			lConMod.setIdContinuazione(lKey);
		} catch (DAOException ex) {
			rollback(lConn);
			throw new F3BException("ContinuazioneController.ExInserisciContinuazione: " + ex);
		} finally {
			cleanup(lConDao);
			cleanup(lConn);
		}

		return lConMod;
	}

	public Vector ExRicercaContinuazione(ContinuazioneModel aContinuazione) throws F3BException {

		Connection lConn = null;

		Vector lContinuazioni = new Vector();
		ContinuazioneSqlDAO lConDao = null;

		try {
			lConn = getDBConnection();
			lConDao = new ContinuazioneSqlDAO(lConn);
			lConDao.ricercaContinuazione(aContinuazione);
			lContinuazioni = new Vector(lConDao.getModels());

			if (lContinuazioni.size() == 0) {
				throw new F3BException(F3BException.USER_MESSAGE, "Nessun Elemento trovato");
			}
		} catch (DAOException daoEx) {
			throw new F3BException("ContinuazioneController.ExRicercaContinuazione: " + daoEx);
		} finally {
			cleanup(lConDao);
			cleanup(lConn);
		}

		return lContinuazioni;
	}

	public ContinuazioneModel ExRicercaContinuazioneByKey(BigDecimal aKey) throws F3BException {

		Connection lConn = null;

		ContinuazioneSqlDAO lConDao = null;
		ContinuazioneModel lConMod;

		try {
			lConn = getDBConnection();
			lConDao = new ContinuazioneSqlDAO(lConn);
			lConDao.ricercaContinuazioneByKey(aKey);
			lConMod = (ContinuazioneModel) lConDao.getModelByKey();
		} catch (DAOException daoEx) {
			throw new F3BException("ContinuazioneController.ExRicercaContinuazioneByKey: " + daoEx);
		} finally {
			cleanup(lConDao);
			cleanup(lConn);
		}

		return lConMod;
	}

	public Vector ExRicercaContinuazioneByIDPenaComplessiva(BigDecimal aKey) throws F3BException {

		Connection lConn = null;

		ContinuazioneSqlDAO lConDao = null;
		Vector lContinuazioni = new Vector();

		try {
			lConn = getDBConnection();
			lConDao = new ContinuazioneSqlDAO(lConn);
			lConDao.ricercaContinuazioneByIdPenaComplessiva(aKey);
			// lConMod = (ContinuazioneModel)lConDao.getModelByKey();
			lContinuazioni = new Vector(lConDao.getModels());

		} catch (DAOException daoEx) {
			throw new F3BException(
					"ContinuazioneController.ExRicercaContinuazioneByIDPenaComplessiva: " + daoEx);
		} finally {
			cleanup(lConDao);
			cleanup(lConn);
		}

		return lContinuazioni;
	}

	public ContinuazioneModel ExModificaContinuazione(ContinuazioneModel aContinuazione) throws F3BException {

		Connection lConn = null;

		ContinuazioneDAO lConDao = null;
		ContinuazioneModel lConMod = new ContinuazioneModel(aContinuazione);

		try {
			lConn = getDBConnection();
			lConDao = new ContinuazioneDAO(lConn);
			lConDao.setDAOFromModelForUpdate(aContinuazione);
			lConDao.update();

			commit(lConn);
		} catch (DAOException ex) {
			rollback(lConn);
			throw new F3BException("ContinuazioneController.ExModificaContinuazione: " + ex);
		} finally {
			cleanup(lConDao);
			cleanup(lConn);
		}

		return lConMod;
	}

	public void ExCancellaContinuazione(ContinuazioneModel aContinuazione) throws F3BException {

		Connection lConn = null;
		ContinuazioneDAO lConDao = null;

		try {
			lConn = getDBConnection();
			lConDao = new ContinuazioneDAO(lConn);
			lConDao.setCondizioneUpdate(aContinuazione.getIdContinuazione());
			lConDao.delete();
			commit(lConn);
		} catch (DAOException daoEx) {
			throw new F3BException("ContinuazioneController.ExCancellaContinuazione: " + daoEx);
		} finally {
			cleanup(lConDao);
			cleanup(lConn);
		}
	}

}