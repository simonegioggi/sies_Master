package siap.siepe.espertoattivita.controller;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Vector;

import org.apache.log4j.Logger;

import siap.controller.SiapController;
import siap.siepe.espertoattivita.dao.EspertoAttivitaDAO;
import siap.siepe.espertoattivita.dao.EspertoAttivitaSqlDAO;
import siap.siepe.espertoattivita.model.EspertoAttivitaModel;
import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.F3BException;

/**
 * <p>
 * Title: EspertoAttivitaController
 * </p>
 * <p>
 * Description: Classe Controller per l'accesso ai dati nella tabella EspertoAttivita
 * </p>
 * Tale tabella costituisce una tabella di relazione che memorizza le associazioni esistenti tra attività ed
 * esperti ad esse assegnate nel tempo.
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
public class EspertoAttivitaController extends SiapController implements IEspertoAttivita {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	/**
	 * Funzione di inserimento di un record in tabella.
	 * 
	 * @param aEspertoAttivita
	 * @return
	 * @throws F3BException
	 */
	public EspertoAttivitaModel ExInserisciEspertoAttivita(EspertoAttivitaModel aEspertoAttivita)
			throws F3BException {
		Connection lConn = null;
		EspertoAttivitaDAO lEspDao = null;
		EspertoAttivitaModel lEspMod = null;

		try {
			lConn = getDBConnection();
			lEspMod = new EspertoAttivitaModel(aEspertoAttivita);
			lEspDao = new EspertoAttivitaDAO(lConn);
			lEspDao.setDAOFromModel(aEspertoAttivita);
			lEspDao.insert();
			commit(lConn);
		} catch (Exception ex) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("Exception: " + ex);
			throw new F3BException("EspertoAttivitaController.ExInserisci: Non posso inserire: " + ex);
		} finally {
			cleanup(lEspDao);
			cleanup(lConn);
		}
		return lEspMod;
	}

	/**
	 * Funzione di ricerca. Trova tutti gli esperti associati ad una attività specificata attraverso la sua
	 * chiave.
	 * 
	 * @param aKey
	 * @return
	 * @throws F3BException
	 */
	public Vector ExRicercaEspertiXAttivita(BigDecimal aKey) throws F3BException {
		Connection lConn = null;
		EspertoAttivitaSqlDAO lEspDao = null;
		Vector lEspertiAttivita = new Vector();

		try {
			lConn = getDBConnection();
			lEspDao = new EspertoAttivitaSqlDAO(lConn);
			lEspDao.ricercaEspertiXAttivita(aKey);
			lEspertiAttivita = new Vector(lEspDao.getModels());
		} catch (Exception e) {
			throw new F3BException("RelazioneController.ExRicercaRelazioniByAttivita: " + e);
		} finally {
			cleanup(lEspDao);
			cleanup(lConn);
		}
		return lEspertiAttivita;
	}

	/**
	 * Funzione di ricerca. Trova tutti gli esperti associati ad una attività specificata che siano attivi,
	 * ovvero che abbiano un periodo di abilitazione che comprenda la data di sistema.
	 * 
	 * @param aKey
	 * @return
	 * @throws F3BException
	 */
	public Vector ExRicercaEspertiAttiviXAttivita(BigDecimal aKey) throws F3BException {
		Connection lConn = null;
		EspertoAttivitaSqlDAO lEspDao = null;
		Vector lEspertiAttivita = new Vector();

		try {
			lConn = getDBConnection();
			lEspDao = new EspertoAttivitaSqlDAO(lConn);
			lEspDao.ricercaEspertiAttiviXAttivita(aKey, DateUtils.getSysDate());
			lEspertiAttivita = new Vector(lEspDao.getModels());
		} catch (Exception e) {
			throw new F3BException("RelazioneController.ExRicercaRelazioniByAttivita: " + e);
		} finally {
			cleanup(lEspDao);
			cleanup(lConn);
		}
		return lEspertiAttivita;
	}

	public EspertoAttivitaModel ExChiudiEspertoAttivita(EspertoAttivitaModel aEspertoAttivita)
			throws F3BException {
		Connection lConn = null;
		EspertoAttivitaDAO lEspDao = null;
		EspertoAttivitaModel lEspMod = new EspertoAttivitaModel(aEspertoAttivita);

		try {
			lConn = getDBConnection();
			lEspDao = new EspertoAttivitaDAO(lConn);
			lEspDao.setDAOFromModelXChiusura(aEspertoAttivita);
			lEspDao.update();
			commit(lConn);
		} catch (Exception ex) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("Exception: " + ex);
			throw new F3BException(
					"EspertoAttivitaController.ExChiudiEspertoAttivita: Non posso inserire: " + ex);
		} finally {
			cleanup(lEspDao);
			cleanup(lConn);
		}
		return lEspMod;
	}

	public EspertoAttivitaModel ExModificaEspertoAttivita(EspertoAttivitaModel aEspertoAttivita)
			throws F3BException {
		Connection lConn = null;
		EspertoAttivitaDAO lEspDao = null;
		EspertoAttivitaModel lEspMod = new EspertoAttivitaModel(aEspertoAttivita);

		try {
			lConn = getDBConnection();
			lEspDao = new EspertoAttivitaDAO(lConn);
			lEspDao.setDAOFromModelForUpdate(aEspertoAttivita);
			lEspDao.update();
			lEspDao.update();
			commit(lConn);
		} catch (Exception ex) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("Exception: " + ex);
			throw new F3BException("EspertoAttivitaController.ExModifica: Non posso inserire: " + ex);
		} finally {
			cleanup(lEspDao);
			cleanup(lConn);
		}
		return lEspMod;
	}

	/*
	 * public void ExCancellaEspertoAttivita (EspertoAttivitaModel aEspertoAttivita ) throws F3BException {
	 * Connection lConn = null; EspertoAttivitaDAO lEspDao = null;
	 * 
	 * 
	 * try { lConn = getDBConnection(); lEspDao = new EspertoAttivitaDAO(lConn);
	 * lEspDao.setCondizioneUpdate(aEspertoAttivita.getIdEspertoAttivita()); lEspDao.delete(); commit(lConn);
	 * } catch (DAOException daoEx) { // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza
	 * siesLogger al posto di mLog siesLogger.error("DAOException: " + daoEx); throw new
	 * F3BException("EspertoAttivitaController.ExCancellaEspertoAttivita: Non posso leggere : " + daoEx); }
	 * catch (SQLException sqe) { // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger
	 * al posto di mLog siesLogger.error("SQLException: " + sqe); throw new
	 * F3BException("EspertoAttivitaController.ExCancellaEspertoAttivita: Non posso leggere  : " + sqe); }
	 * finally { cleanup(lEspDao); cleanup(lConn); } }
	 */
	/*
	 * public Vector ExRicercaEspertoAttivita (EspertoAttivitaModel aEspertoAttivita ) throws F3BException {
	 * Connection lConn = null; Vector lEspertoAttiviti = new Vector(); EspertoAttivitaSqlDAO lEspDao = null;
	 * 
	 * 
	 * 
	 * try { lConn = getDBConnection(); lEspDao = new EspertoAttivitaSqlDAO(lConn);
	 * lEspDao.ricercaEspertoAttivita(aEspertoAttivita); lEspertoAttiviti = new Vector(lEspDao.getModels());
	 * if ( lEspertoAttiviti.size() == 0 ) { throw new
	 * F3BException(F3BException.USER_MESSAGE,"Nessun Elemento trovato"); } } catch (DAOException daoEx) { //
	 * [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
	 * siesLogger.error("DAOException: " + daoEx); throw new
	 * F3BException("EspertoAttivitaController.ExRicercaEspertoAttivita: Non posso leggere : " + daoEx); }
	 * catch (SQLException sqe) { // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger
	 * al posto di mLog siesLogger.error("SQLException: " + sqe); throw new
	 * F3BException("EspertoAttivitaController.ExRicercaEspertoAttivita: Non posso leggere  : " + sqe); }
	 * finally { cleanup(lEspDao); cleanup(lConn); } return lEspertoAttiviti; }
	 */

	/*
	 * public EspertoAttivitaModel ExRicercaEspertoAttivitaByKey ( BigDecimal aKey) throws F3BException {
	 * Connection lConn = null; EspertoAttivitaSqlDAO lEspDao = null; EspertoAttivitaModel lEspMod;
	 * 
	 * 
	 * 
	 * try { lConn = getDBConnection(); lEspDao = new EspertoAttivitaSqlDAO(lConn);
	 * lEspDao.ricercaEspertoAttivitaByKey(aKey); lEspMod = (EspertoAttivitaModel)lEspDao.getModelByKey(); }
	 * catch (DAOException daoEx) { // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza
	 * siesLogger al posto di mLog siesLogger.error("DAOException: " + daoEx); throw new
	 * F3BException("EspertoAttivitaController.ExRicercaEspertoAttivita: Non posso leggere : " + daoEx); }
	 * catch (SQLException sqe) { // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger
	 * al posto di mLog siesLogger.error("SQLException: " + sqe); throw new
	 * F3BException("EspertoAttivitaController.ExRicercaEspertoAttivita: Non posso leggere  : " + sqe); }
	 * finally { cleanup(lEspDao); cleanup(lConn); } return lEspMod; }
	 */

}