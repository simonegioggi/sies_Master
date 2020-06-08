package siap.sius.rifasius.controller;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Vector;

import f3b.dao.DAOException;
import f3b.util.F3BException;
import siap.controller.SiapController;
import siap.sius.rifasius.dao.RiferimentoFascicoloSiusDAO;
import siap.sius.rifasius.dao.RiferimentoFascicoloSiusSqlDAO;
import siap.sius.rifasius.model.RiferimentoFascicoloSiusModel;

/**
 * <p>
 * Title: RiferimentoFascicoloSiusController
 * </p>
 * <p>
 * Description: Classe Controller per RiferimentoFascicoloSius
 * </p>
 * <p>
 * Copyright: Copyright (c) 2004
 * </p>
 * <p>
 * Company: Bull
 * </p>
 *
 * @version 1.0
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class RiferimentoFascicoloSiusController extends SiapController implements IRiferimentoFascicoloSius {

	public RiferimentoFascicoloSiusModel ExInserisciRiferimentoFascicoloSius(
			RiferimentoFascicoloSiusModel aRiferimentoFascicoloSius) throws F3BException {

		Connection lConn = null;
		RiferimentoFascicoloSiusDAO lRifDao = null;
		RiferimentoFascicoloSiusModel lRifMod = null;

		try {
			lConn = getDBConnection();
			lRifMod = new RiferimentoFascicoloSiusModel(aRiferimentoFascicoloSius);
			lRifDao = new RiferimentoFascicoloSiusDAO(lConn);
			lRifDao.setDAOFromModel(aRiferimentoFascicoloSius);
			BigDecimal lKey = null;
			lKey = lRifDao.insert();
			commit(lConn);
			lRifMod.setIdRiferimentoFascicoloSius(lKey);
		} catch (DAOException ex) {
			rollback(lConn);
			throw new F3BException(
					"RiferimentoFascicoloSiusController.ExInserisci: Non posso inserire: " + ex);
		} finally {
			cleanup(lRifDao);
			cleanup(lConn);
		}
		return lRifMod;
	}

	public Vector ExRicercaRiferimentoFascicoloSius(RiferimentoFascicoloSiusModel aRiferimentoFascicoloSius)
			throws F3BException {

		Connection lConn = null;
		Vector lRiFaSius = new Vector();
		RiferimentoFascicoloSiusSqlDAO lRifDao = null;

		try {
			lConn = getDBConnection();
			lRifDao = new RiferimentoFascicoloSiusSqlDAO(lConn);
			lRifDao.ricercaRiferimentoFascicoloSius(aRiferimentoFascicoloSius);
			lRiFaSius = new Vector(lRifDao.getModels());
			if (lRiFaSius.size() == 0)
				throw new F3BException(F3BException.USER_MESSAGE,
						"Nessun Riferimento Fascicolo Sius trovato");
		} catch (DAOException daoEx) {
			throw new F3BException(
					"RiferimentoFascicoloSiusController.ExRicercaRiferimentoFascicoloSius: " + daoEx);
		} finally {
			cleanup(lRifDao);
			cleanup(lConn);
		}
		return lRiFaSius;
	}

	public RiferimentoFascicoloSiusModel ExRicercaRiferimentoFascicoloSiusByKey(BigDecimal aKey)
			throws F3BException {

		Connection lConn = null;
		RiferimentoFascicoloSiusSqlDAO lRifDao = null;
		RiferimentoFascicoloSiusModel lRifMod;

		try {
			lConn = getDBConnection();
			lRifDao = new RiferimentoFascicoloSiusSqlDAO(lConn);
			lRifDao.ricercaRiferimentoFascicoloSiusByKey(aKey);
			lRifMod = (RiferimentoFascicoloSiusModel) lRifDao.getModelByKey();
		} catch (DAOException daoEx) {
			throw new F3BException(
					"RiferimentoFascicoloSiusController.ExRicercaRiferimentoFascicoloSius: Non posso leggere : "
							+ daoEx);
		} finally {
			cleanup(lRifDao);
			cleanup(lConn);
		}
		return lRifMod;
	}

	public RiferimentoFascicoloSiusModel ExModificaRiferimentoFascicoloSius(
			RiferimentoFascicoloSiusModel aRiferimentoFascicoloSius) throws F3BException {

		Connection lConn = null;
		RiferimentoFascicoloSiusDAO lRifDao = null;
		RiferimentoFascicoloSiusModel lRifMod = new RiferimentoFascicoloSiusModel(aRiferimentoFascicoloSius);

		try {
			lConn = getDBConnection();
			lRifDao = new RiferimentoFascicoloSiusDAO(lConn);
			lRifDao.setDAOFromModelForUpdate(aRiferimentoFascicoloSius);
			lRifDao.update();
			lRifDao.update();
			commit(lConn);
		} catch (DAOException ex) {
			rollback(lConn);
			throw new F3BException(
					"RiferimentoFascicoloSiusController.ExModifica: Non posso inserire: " + ex);
		} finally {
			cleanup(lRifDao);
			cleanup(lConn);
		}
		return lRifMod;
	}

	public void ExCancellaRiferimentoFascicoloSius(RiferimentoFascicoloSiusModel aRiferimentoFascicoloSius)
			throws F3BException {

		Connection lConn = null;
		RiferimentoFascicoloSiusDAO lRifDao = null;

		try {
			lConn = getDBConnection();
			lRifDao = new RiferimentoFascicoloSiusDAO(lConn);
			lRifDao.setCondizioneUpdate(aRiferimentoFascicoloSius.getIdRiferimentoFascicoloSius());
			lRifDao.delete();
			commit(lConn);
		} catch (DAOException daoEx) {
			throw new F3BException(
					"RiferimentoFascicoloSiusController.ExCancellaRiferimentoFascicoloSius: Non posso leggere : "
							+ daoEx);
		} finally {
			cleanup(lRifDao);
			cleanup(lConn);
		}
	}

}