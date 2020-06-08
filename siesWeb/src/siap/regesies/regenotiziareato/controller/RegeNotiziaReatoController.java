package siap.regesies.regenotiziareato.controller;

import java.sql.Connection;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.dao.DAOException;
import f3b.log.LogF3B;
import f3b.util.F3BException;
import siap.controller.SiapController;
import siap.regesies.regenotiziareato.dao.RegeNotiziaReatoDAO;
import siap.regesies.regenotiziareato.dao.RegeNotiziaReatoSqlDAO;
import siap.regesies.regenotiziareato.model.RegeNotiziaReatoModel;

/**
 * <p>
 * Title: RegeNotiziaReatoController
 * </p>
 * <p>
 * Description: Classe Controller per RegeNotiziaReato
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
public class RegeNotiziaReatoController extends SiapController implements IRegeNotiziaReato {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	/**
	 * Ricerca le notizie di reato associate ad un provvedimento rege
	 *
	 * @param aKey
	 * @return
	 * @throws F3BException
	 */
	public Vector ExRicercaRegeNotiziaReato(String aKey) throws F3BException {

		Connection lConn = null;
		Vector lRegeNotiziaReati = new Vector();
		RegeNotiziaReatoSqlDAO lRegDao = null;

		try {
			lConn = getDBConnection();
			lRegDao = new RegeNotiziaReatoSqlDAO(lConn);
			lRegDao.ricercaRegeNotiziaReato(aKey);
			lRegeNotiziaReati = new Vector(lRegDao.getModels());
			if (lRegeNotiziaReati.size() == 0) {
				throw new F3BException(F3BException.USER_MESSAGE, "Nessun Elemento trovato");
			}
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException("RegeNotiziaReatoController.ExRicercaRegeNotiziaReato: " + daoEx);
		} finally {
			cleanup(lRegDao);
			cleanup(lConn);
		}
		return lRegeNotiziaReati;
	}

	/**
	 * Ricerca una Notizia di reato basandosi sulla sua chiave
	 *
	 * @param aKey
	 * @return
	 * @throws F3BException
	 */
	public RegeNotiziaReatoModel ExRicercaRegeNotiziaReatoByKey(String aKey, int aProgr) throws F3BException {

		Connection lConn = null;
		RegeNotiziaReatoSqlDAO lRegDao = null;
		RegeNotiziaReatoModel lRegMod;

		try {
			lConn = getDBConnection();
			lRegDao = new RegeNotiziaReatoSqlDAO(lConn);
			lRegDao.ricercaRegeNotiziaReatoByKey(aKey, aProgr);
			lRegMod = (RegeNotiziaReatoModel) lRegDao.getModelByKey();
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException("RegeNotiziaReatoController.ExRicercaRegeNotiziaReato: " + daoEx);
		} finally {
			cleanup(lRegDao);
			cleanup(lConn);
		}
		return lRegMod;
	}

	/**
	 * Metodo per la modifica della notizia di reato
	 *
	 * @param aRegeNotiziaReato
	 * @return notizia di reato modificata
	 * @throws F3BException
	 */
	public RegeNotiziaReatoModel ExModificaRegeNotiziaReato(RegeNotiziaReatoModel aRegeNotiziaReato)
			throws F3BException {

		Connection lConn = null;
		RegeNotiziaReatoDAO lRegDao = null;
		RegeNotiziaReatoModel lRegMod = new RegeNotiziaReatoModel(aRegeNotiziaReato);

		try {
			lConn = getDBConnection();
			lRegDao = new RegeNotiziaReatoDAO(lConn);
			lRegDao.setDAOFromModelForUpdate(aRegeNotiziaReato);
			lRegDao.update();
			commit(lConn);
		} catch (DAOException ex) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + ex);
			throw new F3BException("RegeNotiziaReatoController.ExModifica: Non posso inserire: " + ex);
		} finally {
			cleanup(lRegDao);
			cleanup(lConn);
		}
		return lRegMod;
	}

	/**
	 * Realizza la cancellazione della Notizia di reato
	 *
	 * @param aRegeNotiziaReato
	 * @throws F3BException
	 */
	public void ExCancellaRegeNotiziaReato(RegeNotiziaReatoModel aRegeNotiziaReato) throws F3BException {

		Connection lConn = null;
		RegeNotiziaReatoDAO lRegDao = null;

		try {
			lConn = getDBConnection();
			lRegDao = new RegeNotiziaReatoDAO(lConn);
			lRegDao.setCondizioneUpdate(aRegeNotiziaReato.getIdFile(), aRegeNotiziaReato.getProgrNotizia());
			lRegDao.delete();
			commit(lConn);
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException("RegeNotiziaReatoController.ExCancellaRegeNotiziaReato: " + daoEx);
		} finally {
			cleanup(lRegDao);
			cleanup(lConn);
		}
	}

}