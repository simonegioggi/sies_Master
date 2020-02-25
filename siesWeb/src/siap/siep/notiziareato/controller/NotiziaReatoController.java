package siap.siep.notiziareato.controller;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Vector;

import org.apache.log4j.Logger;

import siap.controller.SiapController;
import siap.siep.notiziareato.dao.NotiziaReatoDAO;
import siap.siep.notiziareato.dao.NotiziaReatoSqlDAO;
import siap.siep.notiziareato.model.NotiziaReatoModel;
import f3b.dao.DAOException;
import f3b.log.LogF3B;
import f3b.util.F3BException;

/**
 * <p>
 * Title: NotiziaReatoController
 * </p>
 * <p>
 * Description: Classe Controller per Notizia di Reato
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
public class NotiziaReatoController extends SiapController implements INotiziaReato {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	/**
	 * Inserisci Notizia Reato
	 * 
	 * @param aNotiziaReato
	 * @return
	 * @throws F3BException
	 */
	public NotiziaReatoModel ExInserisciNotiziaReato(NotiziaReatoModel aNotiziaReato) throws F3BException {
		Connection lConn = null;
		NotiziaReatoDAO lNotDao = null;
		NotiziaReatoModel lNotMod = null;

		try {
			lConn = getDBConnection();
			lNotMod = new NotiziaReatoModel(aNotiziaReato);
			lNotDao = new NotiziaReatoDAO(lConn);
			lNotDao.setDAOFromModel(aNotiziaReato);
			BigDecimal lKey = null;
			lKey = lNotDao.insert();
			commit(lConn);
			lNotMod.setIdNotiziaReato(lKey);
		} catch (DAOException ex) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + ex);
			throw new F3BException("NotiziaReatoController.ExInserisci: Non posso inserire: " + ex);
		} finally {
			cleanup(lNotDao);
			cleanup(lConn);
		}
		return lNotMod;
	}

	// 19.05.2009 ANGELA
	/*
	 * ExRicercaNotiziaReatoByIdFascicoloSige
	 * 
	 * @param BigDecimal aIdFascicolo
	 * 
	 * @return Vector - Elenco di notizie di reato trovate per il dato fascicolosige
	 */

	public Vector ExRicercaNotiziaReatoByIdFascicoloSige(BigDecimal aIdFascicolo) throws F3BException {
		Connection lConn = null;
		Vector lNotiziaReati = new Vector();
		NotiziaReatoSqlDAO lNotDao = null;

		try {
			lConn = getDBConnection();
			lNotDao = new NotiziaReatoSqlDAO(lConn);
			lNotDao.ricercaNotiziaReatoByIdFascicoloSigeOrder(aIdFascicolo);
			lNotiziaReati = new Vector(lNotDao.getModels());
			if (lNotiziaReati.size() == 0) {
				throw new F3BException(F3BException.USER_MESSAGE, "Nessun Elemento trovato");
			}
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException("NotiziaReatoController.ExRicercaNotiziaReato: Non posso leggere : "
					+ daoEx);
		} finally {
			cleanup(lNotDao);
			cleanup(lConn);
		}
		return lNotiziaReati;
	}

	/**
	 * ExRicercaNotiziaReatoByIdFascicoloSiep
	 * 
	 * @param BigDecimal
	 *            aIdFascicolo
	 * @return Vector - Elenco di notizie di reato trovate per il dato fascicolosiep
	 */
	public Vector ExRicercaNotiziaReatoByIdFascicoloSiep(BigDecimal aIdFascicolo) throws F3BException {
		Connection lConn = null;
		Vector lNotiziaReati = new Vector();
		NotiziaReatoSqlDAO lNotDao = null;

		try {
			lConn = getDBConnection();
			lNotDao = new NotiziaReatoSqlDAO(lConn);
			// lNotDao.ricercaNotiziaReatoByIdFascicolo(aIdFascicolo);
			lNotDao.ricercaNotiziaReatoByIdFascicoloOrder(aIdFascicolo);
			lNotiziaReati = new Vector(lNotDao.getModels());
			if (lNotiziaReati.size() == 0) {
				throw new F3BException(F3BException.USER_MESSAGE, "Nessun Elemento trovato");
			}
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException("NotiziaReatoController.ExRicercaNotiziaReato: Non posso leggere : "
					+ daoEx);
		} finally {
			cleanup(lNotDao);
			cleanup(lConn);
		}
		return lNotiziaReati;
	}

	/**
	 * 05/06/2009 ExRicercaNotiziaReato da fascicolo SIGE * @param NotiziaReatoModel aNotiziaReato
	 * 
	 * @return Vector
	 */
	public Vector ExRicercaNotiziaReatoBySIGE(NotiziaReatoModel aNotiziaReato) throws F3BException {
		Connection lConn = null;
		Vector lNotiziaReati = new Vector();
		NotiziaReatoSqlDAO lNotDao = null;

		try {
			lConn = getDBConnection();
			lNotDao = new NotiziaReatoSqlDAO(lConn);
			lNotDao.ricercaNotiziaReatoBySIGE(aNotiziaReato);
			lNotiziaReati = new Vector(lNotDao.getModels());
			if (lNotiziaReati.size() == 0) {
				throw new F3BException(F3BException.USER_MESSAGE, "Nessun Elemento trovato");
			}
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException("NotiziaReatoController.ExRicercaNotiziaReato: Non posso leggere : "
					+ daoEx);
		} finally {
			cleanup(lNotDao);
			cleanup(lConn);
		}
		return lNotiziaReati;
	}

	// //////////////////////////////
	/**
	 * ExRicercaNotiziaReato
	 * 
	 * @param NotiziaReatoModel
	 *            aNotiziaReato
	 * @return Vector
	 */
	public Vector ExRicercaNotiziaReato(NotiziaReatoModel aNotiziaReato) throws F3BException {
		Connection lConn = null;
		Vector lNotiziaReati = new Vector();
		NotiziaReatoSqlDAO lNotDao = null;

		try {
			lConn = getDBConnection();
			lNotDao = new NotiziaReatoSqlDAO(lConn);
			lNotDao.ricercaNotiziaReato(aNotiziaReato);
			lNotiziaReati = new Vector(lNotDao.getModels());
			if (lNotiziaReati.size() == 0) {
				throw new F3BException(F3BException.USER_MESSAGE, "Nessun Elemento trovato");
			}
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException("NotiziaReatoController.ExRicercaNotiziaReato: Non posso leggere : "
					+ daoEx);
		} finally {
			cleanup(lNotDao);
			cleanup(lConn);
		}
		return lNotiziaReati;
	}

	/**
	 * ExRicercaNotiziaReatoByKey
	 * 
	 * @param BigDecimal
	 *            aKey
	 * @return NotiziaReatoModel - La notizia di reato trovata per chiave
	 * @throws F3BException
	 */
	public NotiziaReatoModel ExRicercaNotiziaReatoByKey(BigDecimal aKey) throws F3BException {
		Connection lConn = null;
		NotiziaReatoSqlDAO lNotDao = null;
		NotiziaReatoModel lNotMod;

		try {
			lConn = getDBConnection();
			lNotDao = new NotiziaReatoSqlDAO(lConn);
			lNotDao.ricercaNotiziaReatoByKey(aKey);
			lNotMod = (NotiziaReatoModel) lNotDao.getModelByKey();
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException("NotiziaReatoController.ExRicercaNotiziaReatoByKey: Non posso leggere : "
					+ daoEx);
		} finally {
			cleanup(lNotDao);
			cleanup(lConn);
		}
		return lNotMod;
	}

	/**
	 * ExModificaNotiziaReato
	 * 
	 * @param aNotiziaReato
	 * @return
	 * @throws F3BException
	 */
	public NotiziaReatoModel ExModificaNotiziaReato(NotiziaReatoModel aNotiziaReato) throws F3BException {
		Connection lConn = null;
		NotiziaReatoDAO lNotDao = null;
		NotiziaReatoModel lNotMod = new NotiziaReatoModel(aNotiziaReato);

		try {
			lConn = getDBConnection();
			lNotDao = new NotiziaReatoDAO(lConn);
			lNotDao.setDAOFromModelForUpdate(aNotiziaReato);
			lNotDao.update();
			commit(lConn);
		} catch (DAOException ex) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + ex);
			throw new F3BException("NotiziaReatoController.ExModifica: Non posso inserire: " + ex);
		} finally {
			cleanup(lNotDao);
			cleanup(lConn);
		}
		return lNotMod;
	}

	/**
	 * ExCancellaNotiziaReato
	 * 
	 * @param aNotiziaReato
	 * @throws F3BException
	 */
	public void ExCancellaNotiziaReato(NotiziaReatoModel aNotiziaReato) throws F3BException {
		Connection lConn = null;
		NotiziaReatoDAO lNotDao = null;

		try {
			lConn = getDBConnection();
			lNotDao = new NotiziaReatoDAO(lConn);
			lNotDao.setCondizioneUpdate(aNotiziaReato.getIdNotiziaReato());
			lNotDao.delete();
			commit(lConn);
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException("NotiziaReatoController.ExCancellaNotiziaReato: Non posso leggere : "
					+ daoEx);
		} finally {
			cleanup(lNotDao);
			cleanup(lConn);
		}
	}

}