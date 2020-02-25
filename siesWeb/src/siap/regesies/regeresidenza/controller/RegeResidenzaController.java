package siap.regesies.regeresidenza.controller;

import java.sql.Connection;
import java.util.Vector;

import org.apache.log4j.Logger;

import siap.controller.SiapController;
import siap.regesies.regeresidenza.dao.RegeResidenzaDAO;
import siap.regesies.regeresidenza.dao.RegeResidenzaSqlDAO;
import siap.regesies.regeresidenza.model.RegeResidenzaModel;
import f3b.dao.DAOException;
import f3b.log.LogF3B;
import f3b.util.F3BException;

/**
 * <p>
 * Title: RegeResidenzaController
 * </p>
 * <p>
 * Description: Classe Controller per RegeResidenza
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
public class RegeResidenzaController extends SiapController implements IRegeResidenza {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	/*
	 * public RegeResidenzaModel ExInserisciRegeResidenza (RegeResidenzaModel aRegeResidenza ) throws
	 * F3BException { Connection lConn = null; RegeResidenzaDAO lRegDao = null; RegeResidenzaModel lRegMod =
	 * null; try { lConn = getDBConnection(); lRegMod = new RegeResidenzaModel(aRegeResidenza); lRegDao = new
	 * RegeResidenzaDAO(lConn); lRegDao.setDAOFromModel(aRegeResidenza ); BigDecimal lKey = null; lKey =
	 * lRegDao.insert(); commit(lConn); lRegMod.setIdFile(lKey); } catch (DAOException ex) { rollback(lConn);
	 * // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
	 * siesLogger.error("DAOException: " + ex); throw new
	 * F3BException("RegeResidenzaController.ExInserisci: Non posso inserire: " + ex); } catch (SQLException
	 * sqe) { rollback(lConn); // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al
	 * posto di mLog siesLogger.error("SQLException: " + sqe); throw new
	 * F3BException("RegeResidenzaController.ExInserisciRegeResidenza: Non posso inserire il soggetti : " +
	 * sqe); } finally { cleanup(lRegDao); cleanup(lConn); } return lRegMod; }
	 */

	public Vector ExRicercaRegeResidenza(String aIdFile) throws F3BException {
		Connection lConn = null;
		Vector lRegeResidenzi = new Vector();
		RegeResidenzaSqlDAO lRegDao = null;

		try {
			lConn = getDBConnection();
			lRegDao = new RegeResidenzaSqlDAO(lConn);
			lRegDao.ricercaRegeResidenza(aIdFile);
			lRegeResidenzi = new Vector(lRegDao.getModels());
			if (lRegeResidenzi.size() == 0) {
				throw new F3BException(F3BException.USER_MESSAGE, "Nessun Elemento trovato");
			}
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException("RegeResidenzaController.ExRicercaRegeResidenza: Non posso leggere : "
					+ daoEx);
		} finally {
			cleanup(lRegDao);
			cleanup(lConn);
		}
		return lRegeResidenzi;
	}

	/**
	 * Ricerca una singola Residenza da File e da residenza
	 * 
	 * @param aKey
	 * @return RegeResidenzaModel
	 * @throws F3BException
	 */
	public RegeResidenzaModel ExRicercaRegeResidenzaByKey(String aKey, String aTipoRes) throws F3BException {
		Connection lConn = null;
		RegeResidenzaModel lRegeResidenza = null;
		RegeResidenzaSqlDAO lRegDao = null;
		try {
			lConn = getDBConnection();
			lRegDao = new RegeResidenzaSqlDAO(lConn);
			lRegDao.ricercaRegeResidenzaByKey(aKey, aTipoRes);
			lRegeResidenza = (RegeResidenzaModel) lRegDao.getModelByKey();
			if (lRegeResidenza == null)
				throw new F3BException(F3BException.USER_MESSAGE, "Nessun Elemento trovato");
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException(
					"RegeResidenzaController.ExRicercaRegeResidenzaByKey: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lRegDao);
			cleanup(lConn);
		}
		return lRegeResidenza;
	}

	/**
	 * Esegue l'aggiornamento del rege Residenza
	 * 
	 * @param aRegeResidenza
	 *            residenza da modificare
	 * @return Rege Residenza modificata
	 * @throws F3BException
	 */
	public RegeResidenzaModel ExModificaRegeResidenza(RegeResidenzaModel aRegeResidenza) throws F3BException {
		Connection lConn = null;
		RegeResidenzaDAO lRegDao = null;
		RegeResidenzaModel lRegMod = new RegeResidenzaModel(aRegeResidenza);

		try {
			lConn = getDBConnection();
			lRegDao = new RegeResidenzaDAO(lConn);
			lRegDao.setDAOFromModelForUpdate(aRegeResidenza);
			lRegDao.update();
			commit(lConn);
		} catch (DAOException ex) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + ex);
			throw new F3BException("RegeResidenzaController.ExModificaRegeResidenza: Non posso modificare: "
					+ ex);
		} finally {
			cleanup(lRegDao);
			cleanup(lConn);
		}
		return lRegMod;
	}

	/**
	 * Cancella la residenza Rege
	 * 
	 * @param aKey
	 *            - chiave file
	 * @param aTipo
	 *            - tipo residenza
	 * @throws F3BException
	 */
	public void ExCancellaRegeResidenza(String aKey, String aTipo) throws F3BException {
		Connection lConn = null;
		RegeResidenzaDAO lRegDao = null;

		try {
			lConn = getDBConnection();
			lRegDao = new RegeResidenzaDAO(lConn);
			lRegDao.setCondizioneUpdate(aKey, aTipo);
			lRegDao.delete();
			commit(lConn);
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException("RegeResidenzaController.ExCancellaRegeResidenza: Non posso leggere : "
					+ daoEx);
		} finally {
			cleanup(lRegDao);
			cleanup(lConn);
		}
	}

}