package siap.sius.permesso.controller;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Vector;

import org.apache.log4j.Logger;

import siap.controller.SiapController;
import siap.sius.permesso.dao.EventoPermessoLicenzaDAO;
import siap.sius.permesso.dao.EventoPermessoLicenzaSqlDAO;
import siap.sius.permesso.model.EventoPermessoLicenzaModel;
import f3b.dao.DAOException;
import f3b.log.LogF3B;
import f3b.util.F3BException;

/**
 * <p>
 * Title: EventoPermessoLicenzaController
 * </p>
 * <p>
 * Description: Classe Controller per EventoPermessoLicenza
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
public class EventoPermessoLicenzaController extends SiapController implements IEventoPermessoLicenza {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public EventoPermessoLicenzaModel ExInserisciEventoPermessoLicenza(
			EventoPermessoLicenzaModel aEventoPermessoLicenza) throws F3BException {
		Connection lConn = null;
		EventoPermessoLicenzaDAO lEveDao = null;
		EventoPermessoLicenzaModel lEveMod = null;

		try {
			lConn = getDBConnection();
			lEveMod = new EventoPermessoLicenzaModel(aEventoPermessoLicenza);
			lEveDao = new EventoPermessoLicenzaDAO(lConn);
			lEveDao.setDAOFromModel(aEventoPermessoLicenza);
			BigDecimal lKey = null;
			lKey = lEveDao.insert();
			commit(lConn);
			lEveMod.setIdEventoPermessoLicenza(lKey);
		} catch (DAOException ex) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + ex);
			throw new F3BException("EventoPermessoLicenzaController.ExInserisci: Non posso inserire: " + ex);
		} catch (Exception ex) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("Exception: " + ex);
			throw new F3BException(
					"EventoPermessoLicenzaController.ExInserisciEventoPermessoLicenza: Non posso inserire i dati : "
							+ ex);
		} finally {
			cleanup(lEveDao);
			cleanup(lConn);
		}

		return lEveMod;
	}

	public Vector ExRicercaEventoPermessoLicenzaByKeyLicLib(BigDecimal aKey) throws F3BException {
		Connection lConn = null;
		Vector lEventoPermessoLicenze = new Vector();
		EventoPermessoLicenzaSqlDAO lEveDao = null;

		try {
			lConn = getDBConnection();
			lEveDao = new EventoPermessoLicenzaSqlDAO(lConn);
			lEveDao.ricercaEventoPermessoLicenzaByKeyLicLib(aKey);
			lEventoPermessoLicenze = new Vector(lEveDao.getModels());
			/*
			 * if ( lEventoPermessoLicenze.size() == 0 ) throw new
			 * F3BException(F3BException.USER_MESSAGE,"Nessun Elemento trovato");
			 */
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException(
					"EventoPermessoLicenzaController.ExRicercaEventoPermessoLicenza: Non posso leggere : "
							+ daoEx);
		} catch (Exception ex) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("Exception: " + ex);
			throw new F3BException(
					"EventoPermessoLicenzaController.ExRicercaEventoPermessoLicenza: Non posso leggere  : "
							+ ex);
		} finally {
			cleanup(lEveDao);
			cleanup(lConn);
		}
		return lEventoPermessoLicenze;
	}

	/*
	 * public Vector ExRicercaEventoPermessoLicenza (EventoPermessoLicenzaModel aEventoPermessoLicenza )
	 * throws F3BException { Connection lConn = null; Vector lEventoPermessoLicenze = new Vector();
	 * EventoPermessoLicenzaSqlDAO lEveDao = null;
	 * 
	 * try { lConn = getDBConnection(); lEveDao = new EventoPermessoLicenzaSqlDAO(lConn);
	 * lEveDao.ricercaEventoPermessoLicenza(aEventoPermessoLicenza); lEventoPermessoLicenze = new
	 * Vector(lEveDao.getModels()); if ( lEventoPermessoLicenze.size() == 0 ) throw new
	 * F3BException(F3BException.USER_MESSAGE,"Nessun Elemento trovato"); } catch (DAOException daoEx) { //
	 * [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
	 * siesLogger.error("DAOException: " + daoEx); throw new
	 * F3BException("EventoPermessoLicenzaController.ExRicercaEventoPermessoLicenza: Non posso leggere : " +
	 * daoEx); } catch (Exception ex) { // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza
	 * siesLogger al posto di mLog siesLogger.error("Exception: " + ex); throw new
	 * F3BException("EventoPermessoLicenzaController.ExRicercaEventoPermessoLicenza: Non posso leggere  : " +
	 * ex); } finally { cleanup(lEveDao); cleanup(lConn); } return lEventoPermessoLicenze; }
	 */
	public EventoPermessoLicenzaModel ExRicercaEventoPermessoLicenzaByKey(BigDecimal aKey)
			throws F3BException {
		Connection lConn = null;
		EventoPermessoLicenzaSqlDAO lEveDao = null;
		EventoPermessoLicenzaModel lEveMod;

		try {
			lConn = getDBConnection();
			lEveDao = new EventoPermessoLicenzaSqlDAO(lConn);
			lEveDao.ricercaEventoPermessoLicenzaByKey(aKey);
			lEveMod = (EventoPermessoLicenzaModel) lEveDao.getModelByKey();
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException(
					"EventoPermessoLicenzaController.ExRicercaEventoPermessoLicenza: Non posso leggere i dati : "
							+ daoEx);
		} catch (Exception ex) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("Exception: " + ex);
			throw new F3BException(
					"EventoPermessoLicenzaController.ExRicercaEventoPermessoLicenza: Non posso leggere i dati : "
							+ ex);
		} finally {
			cleanup(lEveDao);
			cleanup(lConn);
		}

		return lEveMod;
	}

	public EventoPermessoLicenzaModel ExModificaEventoPermessoLicenza(
			EventoPermessoLicenzaModel aEventoPermessoLicenza) throws F3BException {
		Connection lConn = null;
		EventoPermessoLicenzaDAO lEveDao = null;
		EventoPermessoLicenzaModel lEveMod = new EventoPermessoLicenzaModel(aEventoPermessoLicenza);

		try {
			lConn = getDBConnection();
			lEveDao = new EventoPermessoLicenzaDAO(lConn);
			lEveDao.setDAOFromModelForUpdate(aEventoPermessoLicenza);
			lEveDao.update();
			commit(lConn);
		} catch (DAOException daoEx) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException(
					"EventoPermessoLicenzaController.ExModifica: Non posso aggiornare di dati : " + daoEx);
		} catch (Exception ex) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("Exception: " + ex);
			throw new F3BException(
					"EventoPermessoLicenzaController.ExModificaEventoPermessoLicenza: Non posso aggiornare i dati : "
							+ ex);
		} finally {
			cleanup(lEveDao);
			cleanup(lConn);
		}

		return lEveMod;
	}

	public void ExCancellaEventoPermessoLicenza(EventoPermessoLicenzaModel aEventoPermessoLicenza)
			throws F3BException {
		Connection lConn = null;
		EventoPermessoLicenzaDAO lEveDao = null;

		try {
			lConn = getDBConnection();
			lEveDao = new EventoPermessoLicenzaDAO(lConn);
			lEveDao.setCondizioneUpdate(aEventoPermessoLicenza.getIdEventoPermessoLicenza());
			lEveDao.delete();
			commit(lConn);
		} catch (DAOException daoEx) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException(
					"EventoPermessoLicenzaController.ExCancellaEventoPermessoLicenza: Non posso leggere : "
							+ daoEx);
		} catch (Exception ex) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("Exception: " + ex);
			throw new F3BException(
					"EventoPermessoLicenzaController.ExCancellaEventoPermessoLicenza: Non posso leggere  : "
							+ ex);
		} finally {
			cleanup(lEveDao);
			cleanup(lConn);
		}
	}

}