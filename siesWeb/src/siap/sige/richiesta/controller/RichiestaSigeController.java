package siap.sige.richiesta.controller;

import java.math.BigDecimal;
import java.sql.Connection;

import org.apache.log4j.Logger;

import f3b.dao.DAOException;
import f3b.log.LogF3B;
import f3b.util.F3BException;
import siap.controller.SiapController;
import siap.sige.richiesta.dao.RichiestaSigeDAO;
import siap.sige.richiesta.model.RichiestaSigeModel;

/**
 * <p>
 * Title: RichiestaSigeController
 * </p>
 * <p>
 * Description: Classe Controller per RichiestaSige
 * </p>
 * <p>
 * Copyright: Copyright (c) 2008
 * </p>
 * <p>
 * Company: Eutelia
 * </p>
 *
 * @version 5.0
 */
public class RichiestaSigeController extends SiapController implements IRichiestaSige {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public RichiestaSigeModel ExInserisciRichiestaSige(RichiestaSigeModel aRichiestaSige)
			throws F3BException {

		Connection lConn = null;
		RichiestaSigeDAO lRicDao = null;
		RichiestaSigeModel lRicMod = null;

		try {
			lConn = getDBConnection();
			lRicMod = new RichiestaSigeModel(aRichiestaSige);
			lRicDao = new RichiestaSigeDAO(lConn);
			lRicDao.setDAOFromModel(aRichiestaSige);
			BigDecimal lKey = null;
			lKey = lRicDao.insert();
			commit(lConn);
			lRicMod.setIdRichiestaSige(lKey);
		} catch (DAOException ex) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + ex);
			throw new F3BException("RichiestaSigeController.ExInserisci DAOException:  " + ex);
		} catch (Exception e) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("Exception: " + e);
			throw new F3BException("RichiestaSigeController.ExInserisciRichiestaSige Exception:  " + e);
		} finally {
			cleanup(lRicDao);
			cleanup(lConn);
		}
		return lRicMod;
	}

	public RichiestaSigeModel ExRicercaRichiestaSigeByKey(BigDecimal aKey) throws F3BException {

		Connection lConn = null;
		RichiestaSigeDAO lRicDao = null;
		RichiestaSigeModel lRicMod;

		try {
			lConn = getDBConnection();
			lRicDao = new RichiestaSigeDAO(lConn);
			lRicDao.setIdRichiestaSige(aKey);
			lRicDao.selByKey();
			lRicMod = (RichiestaSigeModel) lRicDao.getModelByKey();
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException("RichiestaSigeController.ExRicercaRichiestaSige DAOException:  " + daoEx);
		} catch (Exception e) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("Exception: " + e);
			throw new F3BException("RichiestaSigeController.ExRicercaRichiestaSige Exception:  " + e);
		} finally {
			cleanup(lRicDao);
			cleanup(lConn);
		}
		return lRicMod;
	}

	public RichiestaSigeModel ExModificaRichiestaSige(RichiestaSigeModel aRichiestaSige) throws F3BException {

		Connection lConn = null;
		RichiestaSigeDAO lRicDao = null;
		RichiestaSigeModel lRicMod = new RichiestaSigeModel(aRichiestaSige);

		try {
			lConn = getDBConnection();
			lRicDao = new RichiestaSigeDAO(lConn);
			lRicDao.setDAOFromModelForUpdate(aRichiestaSige);
			lRicDao.update();
			commit(lConn);
		} catch (DAOException ex) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + ex);
			throw new F3BException("RichiestaSigeController.ExModifica DAOException: " + ex);
		} catch (Exception e) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("Exception: " + e);
			throw new F3BException("RichiestaSigeController.ExModificaRichiestaSige Exception : " + e);
		} finally {
			cleanup(lRicDao);
			cleanup(lConn);
		}
		return lRicMod;
	}

	public void ExCancellaRichiestaSige(RichiestaSigeModel aRichiestaSige) throws F3BException {

		Connection lConn = null;
		RichiestaSigeDAO lRicDao = null;

		try {
			lConn = getDBConnection();
			lRicDao = new RichiestaSigeDAO(lConn);
			lRicDao.setCondizioneUpdate(aRichiestaSige.getIdRichiestaSige());
			lRicDao.delete();
			commit(lConn);
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException("RichiestaSigeController.ExCancellaRichiestaSige DAOException: " + daoEx);
		} catch (Exception e) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("SQLException: " + e);
			throw new F3BException("RichiestaSigeController.ExCancellaRichiestaSige Exception: " + e);
		} finally {
			cleanup(lRicDao);
			cleanup(lConn);
		}
	}

}