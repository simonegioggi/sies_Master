package siap.regesies.regereato.controller;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.dao.DAOException;
import f3b.log.LogF3B;
import f3b.util.F3BException;
import siap.controller.SiapController;
import siap.regesies.regereato.dao.RegeReatoDAO;
import siap.regesies.regereato.dao.RegeReatoSqlDAO;
import siap.regesies.regereato.model.RegeReatoCircostanzaModel;
import siap.regesies.regereato.model.RegeReatoModel;

/**
 * <p>
 * Title: RegeReatoController
 * </p>
 * <p>
 * Description: Classe Controller per RegeReato
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
public class RegeReatoController extends SiapController implements IRegeReato {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	/**
	 * Ricerca Rege Reati
	 *
	 * @param aKey
	 *            - id file
	 * @return Vettore di reati trovati
	 * @throws F3BException
	 */
	public Vector ExRicercaRegeReato(String aKey) throws F3BException {

		Connection lConn = null;
		Vector lRegeReati = new Vector();
		RegeReatoSqlDAO lRegDao = null;

		try {
			lConn = getDBConnection();
			lRegDao = new RegeReatoSqlDAO(lConn);
			lRegDao.ricercaRegeReato(aKey);
			lRegeReati = new Vector(lRegDao.getModels());
			if (lRegeReati.size() == 0) {
				throw new F3BException(F3BException.USER_MESSAGE, "Nessun Elemento trovato");
			}
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException("RegeReatoController.ExRicercaRegeReato: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lRegDao);
			cleanup(lConn);
		}
		return lRegeReati;
	}

	/**
	 * ExRicercaRegeReatoByKey
	 *
	 * @param aKey
	 *            - id file
	 * @param aProgr
	 *            - Progressivo reato
	 * @return - Reato model
	 * @throws F3BException
	 */
	public RegeReatoModel ExRicercaRegeReatoByKey(String aKey, int aProgr, int aProgrCirc)
			throws F3BException {

		Connection lConn = null;
		RegeReatoSqlDAO lRegDao = null;
		RegeReatoModel lRegMod = new RegeReatoModel();
		try {
			lConn = getDBConnection();
			lRegDao = new RegeReatoSqlDAO(lConn);
			lRegDao.ricercaRegeReatoByKey(aKey, aProgr, aProgrCirc);
			lRegMod = (RegeReatoModel) lRegDao.getModelByKey();

			if (lRegMod != null) {
				lRegMod.calcolaDataReato();
				lRegMod.calcolaStringaConsumazione();
			}
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException(
					"RegeReatoController.ExRicercaRegeReatoByKey: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lRegDao);
			cleanup(lConn);
		}
		return lRegMod;
	}

	/**
	 * MOdifica Rege Reato
	 *
	 * @param aRegeReato
	 * @return Il RegeReato model modificato
	 * @throws F3BException
	 */
	public RegeReatoModel ExModificaRegeReato(RegeReatoModel aRegeReato) throws F3BException {

		Connection lConn = null;
		RegeReatoDAO lRegDao = null;
		RegeReatoModel lRegMod = new RegeReatoModel(aRegeReato);
		try {
			lConn = getDBConnection();
			lRegDao = new RegeReatoDAO(lConn);
			lRegDao.setDAOFromModelForUpdate(aRegeReato);
			lRegDao.update();
			commit(lConn);
		} catch (DAOException ex) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + ex);
			throw new F3BException("RegeReatoController.ExModificaRegeReato: " + ex);
		} finally {
			cleanup(lRegDao);
			cleanup(lConn);
		}
		return lRegMod;
	}

	/**
	 * Cancella il reato Rege
	 *
	 * @param aRegeReato
	 * @throws F3BException
	 */
	public void ExCancellaRegeReato(RegeReatoModel aRegeReato) throws F3BException {

		Connection lConn = null;
		RegeReatoDAO lRegDao = null;

		try {
			lConn = getDBConnection();
			lRegDao = new RegeReatoDAO(lConn);
			lRegDao.setCondizioneUpdate(aRegeReato);
			lRegDao.delete();
			commit(lConn);
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException("RegeReatoController.ExCancellaRegeReato: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lRegDao);
			cleanup(lConn);
		}
	}

	/**
	 * Imposta Reati e Circostanze nel model RegeReatoCircostanzaModel
	 *
	 * @param aKey
	 * @return
	 * @throws F3BException
	 */
	public Vector ExRicercaReatoCircostanzaByProvvedimento(String aKey) throws F3BException {

		Connection lConn = null;
		RegeReatoSqlDAO lReaDao = null;
		Vector lListReaCirc = null;

		try {
			lConn = getDBConnection();
			lReaDao = new RegeReatoSqlDAO(lConn);
			lReaDao.ricercaRegeReatoByProvvedimento(aKey);
			Vector lReati = new Vector(lReaDao.getModels());
			if (lReati.size() == 0) {
				throw new F3BException(F3BException.USER_MESSAGE, "Nessun Elemento trovato");
			}
			lReaDao.stop();
			lListReaCirc = new Vector();
			if (lReati != null) {
				Iterator lItx = lReati.iterator();

				while (lItx.hasNext()) {
					RegeReatoCircostanzaModel aModel = new RegeReatoCircostanzaModel();
					aModel.setReato((RegeReatoModel) lItx.next());
					aModel.getReato().calcolaDataReato();
					lReaDao.ricercaCircostanzeReatoByProvvedimento(aModel.getReato().getProgrReato(), aKey);
					List lCircostanze = new ArrayList(lReaDao.getModels());
					if (lCircostanze != null)
						aModel.setCircostanze((RegeReatoModel[]) lCircostanze.toArray(new RegeReatoModel[0]));

					lListReaCirc.add(aModel);
				}
			}
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException("RegeReatoController.ExRicercaReatoCircostanzaByProvvedimento: " + daoEx);
		} finally {
			cleanup(lReaDao);
			cleanup(lConn);
		}

		return lListReaCirc;
	}

}