package siap.siep.fascicolo.controller;

import java.sql.Connection;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.dao.DAOException;
import f3b.log.LogF3B;
import f3b.util.F3BException;
import siap.controller.SiapController;
import siap.siep.SIEPException;
import siap.siep.fascicolo.dao.FascicoloCheckSqlDAO;
import siap.siep.fascicolo.model.FascicoloCheckModel;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class FascicoloSiepCheckController extends SiapController implements IFascicoloSiepCheck {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public Vector ExContaFascicoli(String aChiaveUfficio) throws F3BException {

		Connection lConn = null;

		FascicoloCheckSqlDAO lFascCheckSqlDao = null;

		Vector lListaFascicoli = new Vector();

		try {
			lConn = getDBConnection();

			lFascCheckSqlDao = new FascicoloCheckSqlDAO(lConn);

			lFascCheckSqlDao.ricercaFascicoliIscrittiTot(aChiaveUfficio);

			lFascCheckSqlDao.start();

			while (lFascCheckSqlDao.next()) {
				FascicoloCheckModel lFascSiepModel = (FascicoloCheckModel) lFascCheckSqlDao.getModel();

				lListaFascicoli.add(lFascSiepModel);
			}

			lFascCheckSqlDao.stop();
		} catch (DAOException dex) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: ", dex);
			throw new SIEPException("FascicoloSiepCheckController.ExRicercaFascicoli: " + dex);
		} catch (Exception ex) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("Exception: ", ex);
			throw new SIEPException("FascicoloSiepCheckController.ExRicercaFascicoli: " + ex);
		} finally {
			cleanup(lFascCheckSqlDao);
			cleanup(lConn);
		}

		return lListaFascicoli;
	}

	public Vector ExContaFascicoliIscrittiSIEP(String aChiaveUfficio) throws F3BException {

		Connection lConn = null;

		FascicoloCheckSqlDAO lFascCheckSqlDao = null;

		Vector lListaFascicoli = new Vector();

		try {
			lConn = getDBConnection();

			lFascCheckSqlDao = new FascicoloCheckSqlDAO(lConn);

			lFascCheckSqlDao.ricercaFascicoliIscrittiSIEP(aChiaveUfficio);

			lFascCheckSqlDao.start();

			while (lFascCheckSqlDao.next()) {
				FascicoloCheckModel lFascSiepModel = (FascicoloCheckModel) lFascCheckSqlDao.getModel();

				lListaFascicoli.add(lFascSiepModel);
			}

			lFascCheckSqlDao.stop();
		} catch (DAOException dex) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: ", dex);
			throw new SIEPException("FascicoloSiepCheckController.ExRicercaFascicoli: " + dex);
		} catch (Exception ex) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("Exception: ", ex);
			throw new SIEPException("FascicoloSiepCheckController.ExRicercaFascicoli: " + ex);
		} finally {
			cleanup(lFascCheckSqlDao);
			cleanup(lConn);
		}

		return lListaFascicoli;
	}

	public Vector ExContaFascicoliIscrittiRES(String aChiaveUfficio) throws F3BException {

		Connection lConn = null;

		FascicoloCheckSqlDAO lFascCheckSqlDao = null;

		Vector lListaFascicoli = new Vector();

		try {
			lConn = getDBConnection();

			lFascCheckSqlDao = new FascicoloCheckSqlDAO(lConn);

			lFascCheckSqlDao.ricercaFascicoliIscrittiRES(aChiaveUfficio);

			lFascCheckSqlDao.start();

			while (lFascCheckSqlDao.next()) {
				FascicoloCheckModel lFascSiepModel = (FascicoloCheckModel) lFascCheckSqlDao.getModel();

				lListaFascicoli.add(lFascSiepModel);
			}

			lFascCheckSqlDao.stop();
		} catch (DAOException dex) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: ", dex);
			throw new SIEPException("FascicoloSiepCheckController.ExRicercaFascicoli: " + dex);
		} catch (Exception ex) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("Exception: ", ex);
			throw new SIEPException("FascicoloSiepCheckController.ExRicercaFascicoli: " + ex);
		} finally {
			cleanup(lFascCheckSqlDao);
			cleanup(lConn);
		}

		return lListaFascicoli;
	}

}