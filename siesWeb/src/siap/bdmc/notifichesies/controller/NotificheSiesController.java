package siap.bdmc.notifichesies.controller;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Vector;

import f3b.dao.DAOException;
import f3b.util.F3BException;
import siap.bdmc.notifichesies.dao.NotificheSiesDAO;
import siap.bdmc.notifichesies.dao.NotificheSiesSqlDAO;
import siap.bdmc.notifichesies.model.NotificheSiesModel;
import siap.controller.SiapController;

/**
 * <p>
 * Title: NotificheSiesController
 * </p>
 * <p>
 * Description: Classe Controller per NotificheSies
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
public class NotificheSiesController extends SiapController implements INotificheSies {

	/*****************************************************************************
	 * Effettua l'inserimento di un NotificheSies a partire dai dati contenuti nel Model
	 *
	 * @param aNotificheSies
	 *            Model con i dati da inserire
	 * @return il model con i dati inseriti e l'aggiunta dell'id del record inserito
	 * @throws F3BException
	 ****************************************************************************/
	public NotificheSiesModel ExInserisciNotificheSies(NotificheSiesModel aNotificheSies)
			throws F3BException {

		Connection lConn = null;
		NotificheSiesDAO lNotDao = null;
		NotificheSiesModel lNotMod = null;

		try {
			lConn = getDBConnection();
			lNotDao = new NotificheSiesDAO(lConn);
			lNotDao.setDAOFromModel(aNotificheSies);
			BigDecimal lSequence = lNotDao.insert();
			commit(lConn);
			lNotMod = new NotificheSiesModel(aNotificheSies);
			lNotMod.setMessage("Inserimento avvenuto correttamente!");
			lNotMod.setIdNotificheSies(lSequence);
		} catch (DAOException ex) {
			rollback(lConn);
			throw new F3BException("NotificheSiesController.ExInserisci: Non posso inserire: " + ex);
		} finally {
			cleanup(lNotDao);
			cleanup(lConn);
		}

		return lNotMod;
	}

	/*****************************************************************************
	 * Effettua la ricerca dei dati NotificheSies
	 *
	 * @param aNotificheSies
	 *            Model utilizzato per costruire le condizioni di ricerca Ogni valore attualizzato nel model
	 *            verrà utilizzato per imporre una condizione di ricerca
	 * @return un vettore di model con il risultato della ricerca
	 * @throws F3BException
	 ****************************************************************************/
	public Vector ExRicercaNotificheSies(NotificheSiesModel aNotificheSies) throws F3BException {

		Connection lConn = null;
		Vector lNotificheSiei = new Vector();
		NotificheSiesDAO lNotDao = null;

		try {
			lConn = getDBConnection();
			lNotDao = new NotificheSiesDAO(lConn);
			lNotDao.setCondizioni(aNotificheSies);
			lNotDao.setOrderBy();
			lNotDao.start();
			while (lNotDao.next()) {
				lNotificheSiei.add(lNotDao.getModel());
			}
			lNotDao.stop();
		} catch (DAOException daoEx) {
			throw new F3BException(
					"NotificheSiesController.ExRicercaNotificheSies: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lNotDao);
			cleanup(lConn);
		}

		return lNotificheSiei;
	}

	/*****************************************************************************
	 * Effettua la ricerca per chiave
	 *
	 * @param akey
	 *            valore della chiave del record da ricercare
	 * @return il model con i dati trovati
	 * @throws F3BException
	 ****************************************************************************/
	public NotificheSiesModel ExRicercaNotificheSiesById(BigDecimal aIdNotificheSies) throws F3BException {

		Connection lConn = null;
		NotificheSiesModel lNotificheSiesMod = new NotificheSiesModel();
		NotificheSiesSqlDAO lNotificheSiesSqlDao = null;

		try {
			lConn = getDBConnection();
			lNotificheSiesSqlDao = new NotificheSiesSqlDAO(lConn);
			lNotificheSiesSqlDao.ricercaNotificheSiesByKey(aIdNotificheSies);
			lNotificheSiesMod = (NotificheSiesModel) lNotificheSiesSqlDao.getModelByKey();
		} catch (DAOException daoEx) {
			throw new F3BException(
					"NotificheSiesController.ExRicercaNotificheSiesById: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lNotificheSiesSqlDao);
			cleanup(lConn);
		}

		return lNotificheSiesMod;
	}

	/*****************************************************************************
	 * Metodo che modifica i dati dell'NotificheSies Viene fatto l'update di tutti i campi del record
	 * recuperando i valori dal Model Se mancano dati nel model i corrispondenti valori della tabella verranno
	 * impostati a null
	 *
	 * @param aNotificheSies
	 *            Model con i nuovi valori
	 * @throws F3BException
	 ****************************************************************************/
	public void ExModificaNotificheSies(NotificheSiesModel aNotificheSies) throws F3BException {

		Connection lConn = null;
		NotificheSiesDAO lNotDao = null;

		try {
			lConn = getDBConnection();
			lNotDao = new NotificheSiesDAO(lConn);
			lNotDao.setDAOFromModel(aNotificheSies);
			lNotDao.selCondizioneUpdate(aNotificheSies.getIdNotificheSies());
			lNotDao.update();
			commit(lConn);
		} catch (DAOException ex) {
			rollback(lConn);
			throw new F3BException("NotificheSiesController.ExModifica: Non posso inserire: " + ex);
		} finally {
			cleanup(lNotDao);
			cleanup(lConn);
		}
	}

	/*****************************************************************************
	 * Metodo che modifica i dati dell'NotificheSies senza effettuare il commit Viene fatto l'update di tutti
	 * i campi del record recuperando i valori dal Model Se mancano dati nel model i corrispondenti valori
	 * della tabella verranno impostati a null
	 *
	 * @param aNotificheSies
	 *            Model con i nuovi valori
	 * @throws F3BException
	 ****************************************************************************/
	public void ExModificaNotificheSiesNoCommit(Connection lConn, NotificheSiesModel aNotificheSies)
			throws F3BException {

		NotificheSiesDAO lNotDao = null;

		try {
			// lConn = getDBConnection();
			lNotDao = new NotificheSiesDAO(lConn);
			lNotDao.setDAOFromModel(aNotificheSies);
			lNotDao.selCondizioneUpdate(aNotificheSies.getIdNotificheSies());
			lNotDao.update();
		} catch (DAOException ex) {
			throw new F3BException("NotificheSiesController.ExModifica: Non posso inserire: " + ex);
		} finally {
			cleanup(lNotDao);
		}
	}

	/*****************************************************************************
	 * Effettua la cancellazione del record
	 *
	 * @param aNotificheSies
	 * @throws F3BException
	 ****************************************************************************/
	public void ExCancellaNotificheSies(NotificheSiesModel aNotificheSies) throws F3BException {

		Connection lConn = null;
		NotificheSiesDAO lNotDao = null;

		try {
			lConn = getDBConnection();
			lNotDao = new NotificheSiesDAO(lConn);
			lNotDao.selCondizioneUpdate(aNotificheSies.getIdNotificheSies());
			lNotDao.delete();
			commit(lConn);
		} catch (DAOException daoEx) {
			rollback(lConn);
			throw new F3BException(
					"NotificheSiesController.ExCancellaNotificheSies: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lNotDao);
			cleanup(lConn);
		}
	}

	/*****************************************************************************
	 * Recupera il numero di record restituiti della ricerca. Utile in caso di ricerche paginate per ottenere
	 * il numero totale di record
	 *
	 * @param aNotificheSies
	 * @return numero di record trovati dalla funzione dei ricerca
	 * @throws F3BException
	 ****************************************************************************/
	public BigDecimal ExGetCountNotificheSies(NotificheSiesModel aNotificheSies) throws F3BException {

		Connection lConn = null;
		BigDecimal lCount = new BigDecimal(0);
		NotificheSiesSqlDAO lNotificheSiesSqlDao = null;

		try {
			lConn = getDBConnection();
			lNotificheSiesSqlDao = new NotificheSiesSqlDAO(lConn);
			lNotificheSiesSqlDao.getCountNotificheSies(aNotificheSies);
			lNotificheSiesSqlDao.start();
			lNotificheSiesSqlDao.next();
			lCount = lNotificheSiesSqlDao.getBigDecimal("HowManyRecords");
			lNotificheSiesSqlDao.stop();
		} catch (DAOException daoEx) {
			throw new F3BException(
					"NotificheSiesController.ExGetCountNotificheSies: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lNotificheSiesSqlDao);
			cleanup(lConn);
		}

		return lCount;
	}

	/*****************************************************************************
	 * Funzione di ricerca utilizzata per la paginazione che restituisce i risultati da visualizzare nella
	 * pagina specificata in input
	 *
	 * @param aNotificheSies
	 *            model contenete i parametri della ricerca
	 * @param aPage
	 *            pagina per la quale si vogliono ottenere i risultati
	 * @return Vettore di model con i record da visualizzare nella pagina specificata in input
	 * @throws F3BException
	 ****************************************************************************/
	public Vector ExRicercaNotificheSiesPaged(NotificheSiesModel aNotificheSies, int aPage)
			throws F3BException {

		Connection lConn = null;
		Vector lNotificheSiei = new Vector();
		NotificheSiesSqlDAO lNotificheSiesSqlDao = null;

		try {
			lConn = getDBConnection();
			lNotificheSiesSqlDao = new NotificheSiesSqlDAO(lConn);
			lNotificheSiesSqlDao.ricercaNotificheSiesPaged(aNotificheSies, aPage);
			lNotificheSiei = new Vector(lNotificheSiesSqlDao.getModels());
		} catch (DAOException daoEx) {
			throw new F3BException(
					"NotificheSiesController.ExRicercaNotificheSiesPaged: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lNotificheSiesSqlDao);
			cleanup(lConn);
		}
		return lNotificheSiei;
	}

}