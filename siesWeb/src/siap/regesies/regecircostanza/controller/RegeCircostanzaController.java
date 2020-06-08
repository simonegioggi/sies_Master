package siap.regesies.regecircostanza.controller;

import java.sql.Connection;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.dao.DAOException;
import f3b.log.LogF3B;
import f3b.util.F3BException;
import siap.controller.SiapController;
import siap.regesies.regecircostanza.dao.RegeCircostanzaDAO;
import siap.regesies.regecircostanza.dao.RegeCircostanzaSqlDAO;
import siap.regesies.regecircostanza.model.RegeCircostanzaModel;

/**
 * <p>
 * Title: RegeCircostanzaController
 * </p>
 * <p>
 * Description: Classe Controller per RegeCircostanza
 * </p>
 * <p>
 * Copyright: Copyright (c) 2006
 * </p>
 * <p>
 * Company: Bull
 * </p>
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class RegeCircostanzaController extends SiapController implements IRegeCircostanza {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	/**
	 * Ricerca Circostanze per l'elenco Circostanze
	 *
	 * @param aRegeCircostanza
	 * @return Vettore di Circostanze
	 * @throws F3BException
	 */
	public Vector ExRicercaRegeCircostanza(String aKey) throws F3BException {

		Connection lConn = null;
		Vector lRegeCircostanzi = new Vector();
		RegeCircostanzaSqlDAO lRegDao = null;

		try {
			lConn = getDBConnection();
			lRegDao = new RegeCircostanzaSqlDAO(lConn);
			lRegDao.ricercaRegeCircostanza(aKey);
			lRegeCircostanzi = new Vector(lRegDao.getModels());
			if (lRegeCircostanzi.size() == 0) {
				throw new F3BException(F3BException.USER_MESSAGE, "Nessun Elemento trovato");
			}
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException(
					"RegeCircostanzaController.ExRicercaRegeCircostanza: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lRegDao);
			cleanup(lConn);
		}
		return lRegeCircostanzi;
	}

	public RegeCircostanzaModel ExRicercaRegeCircostanzaByKey(String aKey, int aProgrCirc)
			throws F3BException {

		Connection lConn = null;
		RegeCircostanzaSqlDAO lRegDao = null;
		RegeCircostanzaModel lRegMod;

		try {
			lConn = getDBConnection();
			lRegDao = new RegeCircostanzaSqlDAO(lConn);
			lRegDao.ricercaRegeCircostanzaByKey(aKey, aProgrCirc);
			lRegMod = (RegeCircostanzaModel) lRegDao.getModelByKey();
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException(
					"RegeCircostanzaController.ExRicercaRegeCircostanza: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lRegDao);
			cleanup(lConn);
		}
		return lRegMod;
	}

	/**
	 * Modifica la Rege Circostanza
	 *
	 * @param aRegeCircostanza
	 * @return
	 * @throws F3BException
	 */
	public RegeCircostanzaModel ExModificaRegeCircostanza(RegeCircostanzaModel aRegeCircostanza)
			throws F3BException {

		Connection lConn = null;
		RegeCircostanzaDAO lRegDao = null;
		RegeCircostanzaModel lRegMod = new RegeCircostanzaModel(aRegeCircostanza);

		try {
			lConn = getDBConnection();
			lRegDao = new RegeCircostanzaDAO(lConn);
			lRegDao.setDAOFromModelForUpdate(aRegeCircostanza);
			lRegDao.update();
			commit(lConn);
		} catch (DAOException ex) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + ex);
			throw new F3BException(
					"RegeCircostanzaController.ExModificaRegeCircostanza: Non posso inserire: " + ex);
		} finally {
			cleanup(lRegDao);
			cleanup(lConn);
		}
		return lRegMod;
	}

	/**
	 * Realizza la cancella zione della Circostanza
	 *
	 * @param aRegeCircostanza
	 * @throws F3BException
	 */
	public void ExCancellaRegeCircostanza(RegeCircostanzaModel aRegeCircostanza) throws F3BException {

		Connection lConn = null;
		RegeCircostanzaDAO lRegDao = null;

		try {
			lConn = getDBConnection();
			lRegDao = new RegeCircostanzaDAO(lConn);
			lRegDao.setCondizioneUpdate(aRegeCircostanza.getIdFile(), aRegeCircostanza.getProgrCircostanza());
			lRegDao.delete();
			commit(lConn);
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException(
					"RegeCircostanzaController.ExCancellaRegeCircostanza: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lRegDao);
			cleanup(lConn);
		}
	}

}