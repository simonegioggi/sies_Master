package siap.sige.datiprovsige.controller;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.dao.DAOException;
import f3b.log.LogF3B;
import f3b.util.F3BException;
import siap.controller.SiapController;
import siap.sige.datiprovsige.dao.DatiProvvedimentoSigeDAO;
import siap.sige.datiprovsige.dao.DatiProvvedimentoSigeSqlDAO;
import siap.sige.datiprovsige.model.DatiProvvedimentoSigeModel;

/**
 * <p>
 * Title: DatiProvvedimentoSigeController
 * </p>
 * <p>
 * Description: Classe Controller per DatiProvvedimentoSige
 * </p>
 * <p>
 * Copyright: Copyright (c) 2008
 * </p>
 * <p>
 * Company: Eutelia
 * </p>
 *
 * @version 1.0
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class DatiProvvedimentoSigeController extends SiapController implements IDatiProvvedimentoSige {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public DatiProvvedimentoSigeModel ExInserisciDatiProvvedimentoSige(
			DatiProvvedimentoSigeModel aDatiProvvedimentoSige) throws F3BException {

		Connection lConn = null;
		DatiProvvedimentoSigeDAO lDatDao = null;
		DatiProvvedimentoSigeModel lDatMod = null;

		try {
			lConn = getDBConnection();
			lDatMod = new DatiProvvedimentoSigeModel(aDatiProvvedimentoSige);
			lDatDao = new DatiProvvedimentoSigeDAO(lConn);
			lDatDao.setDAOFromModel(aDatiProvvedimentoSige);
			BigDecimal lKey = null;
			lKey = lDatDao.insert();
			commit(lConn);
			lDatMod.setIdDatiProvvedimentoSige(lKey);
		} catch (DAOException ex) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + ex);
			throw new F3BException("DatiProvvedimentoSigeController.ExInserisci: Non posso inserire: " + ex);
		} finally {
			cleanup(lDatDao);
			cleanup(lConn);
		}
		return lDatMod;
	}

	public Vector ExRicercaDatiProvvedimentoSigeByIdTenore(BigDecimal aIdTenore) throws F3BException {

		Connection lConn = null;
		Vector lDatiProvvedimentoSige = null;
		DatiProvvedimentoSigeSqlDAO lDatDao = null;

		try {
			lConn = getDBConnection();
			lDatDao = new DatiProvvedimentoSigeSqlDAO(lConn);
			lDatDao.ricercaDatiProvvedimentoSigeByIdTenore(aIdTenore);

			lDatiProvvedimentoSige = new Vector(lDatDao.getModels());
		} catch (DAOException daoEx) {
			throw new F3BException(
					"DatiProvvedimentoSigeController.ExRicercaDatiProvvedimentoSigeByIdTenore: Non posso leggere : "
							+ daoEx);
		} catch (Exception e) {
			throw new F3BException(
					"DatiProvvedimentoSigeController.ExRicercaDatiProvvedimentoSigeByIdTenore: Non posso leggere  : "
							+ e);
		} finally {
			cleanup(lDatDao);
			cleanup(lConn);
		}
		return lDatiProvvedimentoSige;
	}

	public DatiProvvedimentoSigeModel ExRicercaDatiProvvedimentoSigeByKey(BigDecimal aKey)
			throws F3BException {

		Connection lConn = null;
		DatiProvvedimentoSigeSqlDAO lDatDao = null;
		DatiProvvedimentoSigeModel lDatMod;

		try {
			lConn = getDBConnection();
			lDatDao = new DatiProvvedimentoSigeSqlDAO(lConn);
			lDatDao.ricercaDatiProvvedimentoSigeByKey(aKey);
			lDatMod = (DatiProvvedimentoSigeModel) lDatDao.getModelByKey();
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException(
					"DatiProvvedimentoSigeController.ExRicercaDatiProvvedimentoSige: Non posso leggere : "
							+ daoEx);
		} finally {
			cleanup(lDatDao);
			cleanup(lConn);
		}
		return lDatMod;
	}

	public DatiProvvedimentoSigeModel ExModificaDatiProvvedimentoSige(
			DatiProvvedimentoSigeModel aDatiProvvedimentoSige) throws F3BException {

		Connection lConn = null;
		DatiProvvedimentoSigeDAO lDatDao = null;
		DatiProvvedimentoSigeModel lDatMod = new DatiProvvedimentoSigeModel(aDatiProvvedimentoSige);

		try {
			lConn = getDBConnection();
			lDatDao = new DatiProvvedimentoSigeDAO(lConn);
			lDatDao.setDAOFromModelForUpdate(aDatiProvvedimentoSige);
			lDatDao.update();
			lDatDao.update();
			commit(lConn);
		} catch (DAOException ex) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + ex);
			throw new F3BException("DatiProvvedimentoSigeController.ExModifica: Non posso inserire: " + ex);
		} finally {
			cleanup(lDatDao);
			cleanup(lConn);
		}
		return lDatMod;
	}

	public void ExCancellaDatiProvvedimentoSige(DatiProvvedimentoSigeModel aDatiProvvedimentoSige)
			throws F3BException {

		Connection lConn = null;
		DatiProvvedimentoSigeDAO lDatDao = null;

		try {
			lConn = getDBConnection();
			lDatDao = new DatiProvvedimentoSigeDAO(lConn);
			lDatDao.setCondizioneUpdate(aDatiProvvedimentoSige.getIdDatiProvvedimentoSige());
			lDatDao.delete();
			commit(lConn);
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException(
					"DatiProvvedimentoSigeController.ExCancellaDatiProvvedimentoSige: Non posso leggere : "
							+ daoEx);
		} finally {
			cleanup(lDatDao);
			cleanup(lConn);
		}
	}

}