package siap.sico.helponline.controller;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Vector;

import f3b.dao.DAOException;
import f3b.util.F3BException;
import siap.controller.SiapController;
import siap.sico.helponline.dao.HelponlineDAO;
import siap.sico.helponline.dao.HelponlineSqlDAO;
import siap.sico.helponline.model.HelponlineModel;

/**
 * <p>
 * Title: HelponlineController
 * </p>
 * <p>
 * Description: Classe Controller per Helponline
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
public class HelponlineController extends SiapController implements IHelponline {

	/*****************************************************************************
	 * Effettua l'inserimento di un Helponline a partire dai dati contenuti nel Model
	 *
	 * @param aHelponline
	 *            Model con i dati da inserire
	 * @return il model con i dati inseriti e l'aggiunta dell'id del record inserito
	 * @throws F3BException
	 ****************************************************************************/
	public HelponlineModel ExInserisciHelponline(HelponlineModel aHelponline) throws F3BException {

		Connection lConn = null;
		HelponlineDAO lHelDao = null;
		HelponlineModel lHelMod = null;

		try {
			lConn = getDBConnection();
			lHelDao = new HelponlineDAO(lConn);
			lHelDao.setDAOFromModel(aHelponline);
			/* BigDecimal lSequence = */lHelDao.insert();
			commit(lConn);
			lHelMod = new HelponlineModel(aHelponline);
			lHelMod.setMessage("Inserimento avvenuto correttamente!");
			// lHelMod.setIdHelponline(lSequence);
		} catch (DAOException ex) {
			rollback(lConn);
			throw new F3BException("HelponlineController.ExInserisci: Non posso inserire: " + ex);
		} finally {
			cleanup(lHelDao);
			cleanup(lConn);
		}

		return lHelMod;
	}

	/*****************************************************************************
	 * Effettua la ricerca dei dati Helponline
	 *
	 * @param aHelponline
	 *            Model utilizzato per costruire le condizioni di ricerca Ogni valore attualizzato nel model
	 *            verrà utilizzato per imporre una condizione di ricerca
	 * @return un vettore di model con il risultato della ricerca
	 * @throws F3BException
	 ****************************************************************************/
	public Vector ExRicercaHelponline(HelponlineModel aHelponline) throws F3BException {

		Connection lConn = null;
		Vector lHelponlini = new Vector();
		HelponlineDAO lHelDao = null;

		try {
			lConn = getDBConnection();
			lHelDao = new HelponlineDAO(lConn);
			lHelDao.setCondizioni(aHelponline);
			lHelDao.setOrderBy();
			lHelDao.start();
			while (lHelDao.next()) {
				lHelponlini.add(lHelDao.getModel());
			}
			lHelDao.stop();
		} catch (DAOException daoEx) {
			throw new F3BException("HelponlineController.ExRicercaHelponline: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lHelDao);
			cleanup(lConn);
		}

		return lHelponlini;
	}

	/*****************************************************************************
	 * Effettua la ricerca per chiave
	 *
	 * @param akey
	 *            valore della chiave del record da ricercare
	 * @return il model con i dati trovati
	 * @throws F3BException
	 ****************************************************************************/
	public HelponlineModel ExRicercaHelponlineById(BigDecimal aIdHelponline) throws F3BException {

		Connection lConn = null;
		HelponlineModel lHelponlineMod = new HelponlineModel();
		HelponlineSqlDAO lHelponlineSqlDao = null;

		try {
			lConn = getDBConnection();
			lHelponlineSqlDao = new HelponlineSqlDAO(lConn);
			lHelponlineSqlDao.ricercaHelponlineByKey(aIdHelponline);
			lHelponlineMod = (HelponlineModel) lHelponlineSqlDao.getModelByKey();
		} catch (DAOException daoEx) {
			throw new F3BException(
					"HelponlineController.ExRicercaHelponlineById: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lHelponlineSqlDao);
			cleanup(lConn);
		}

		return lHelponlineMod;
	}

	/*****************************************************************************
	 * Metodo che modifica i dati dell'Helponline Viene fatto l'update di tutti i campi del record recuperando
	 * i valori dal Model Se mancano dati nel model i corrispondenti valori della tabella verranno impostati a
	 * null
	 *
	 * @param aHelponline
	 *            Model con i nuovi valori
	 * @throws F3BException
	 ****************************************************************************/
	public void ExModificaHelponline(HelponlineModel aHelponline) throws F3BException {

		Connection lConn = null;
		HelponlineDAO lHelDao = null;

		try {
			lConn = getDBConnection();
			lHelDao = new HelponlineDAO(lConn);
			lHelDao.setDAOFromModel(aHelponline);
			lHelDao.selCondizioneUpdate(aHelponline.getFunIdFunzione());
			lHelDao.update();
			commit(lConn);
		} catch (DAOException ex) {
			rollback(lConn);
			throw new F3BException("HelponlineController.ExModifica: Non posso inserire: " + ex);
		} finally {
			cleanup(lHelDao);
			cleanup(lConn);
		}
	}

	/*****************************************************************************
	 * Effettua la cancellazione del record
	 *
	 * @param aHelponline
	 * @throws F3BException
	 ****************************************************************************/
	public void ExCancellaHelponline(HelponlineModel aHelponline) throws F3BException {

		Connection lConn = null;
		HelponlineDAO lHelDao = null;

		try {
			lConn = getDBConnection();
			lHelDao = new HelponlineDAO(lConn);
			lHelDao.selCondizioneUpdate(aHelponline.getFunIdFunzione());
			lHelDao.delete();
			commit(lConn);
		} catch (DAOException daoEx) {
			rollback(lConn);
			throw new F3BException("HelponlineController.ExCancellaHelponline: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lHelDao);
			cleanup(lConn);
		}
	}

	/*****************************************************************************
	 * Recupera il numero di record restituiti della ricerca. Utile in caso di ricerche paginate per ottenere
	 * il numero totale di record
	 *
	 * @param aHelponline
	 * @return numero di record trovati dalla funzione dei ricerca
	 * @throws F3BException
	 ****************************************************************************/
	public BigDecimal ExGetCountHelponline(HelponlineModel aHelponline) throws F3BException {

		Connection lConn = null;
		BigDecimal lCount = new BigDecimal(0);
		HelponlineSqlDAO lHelponlineSqlDao = null;

		try {
			lConn = getDBConnection();
			lHelponlineSqlDao = new HelponlineSqlDAO(lConn);
			lHelponlineSqlDao.getCountHelponline(aHelponline);
			lHelponlineSqlDao.start();
			lHelponlineSqlDao.next();
			lCount = lHelponlineSqlDao.getBigDecimal("HowManyRecords");
			lHelponlineSqlDao.stop();
		} catch (DAOException daoEx) {
			throw new F3BException("HelponlineController.ExGetCountHelponline: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lHelponlineSqlDao);
			cleanup(lConn);
		}

		return lCount;
	}

	/*****************************************************************************
	 * Funzione di ricerca utilizzata per la paginazione che restituisce i risultati da visualizzare nella
	 * pagina specificata in input
	 *
	 * @param aHelponline
	 *            model contenete i parametri della ricerca
	 * @param aPage
	 *            pagina per la quale si vogliono ottenere i risultati
	 * @return Vettore di model con i record da visualizzare nella pagina specificata in input
	 * @throws F3BException
	 ****************************************************************************/
	public Vector ExRicercaHelponlinePaged(HelponlineModel aHelponline, int aPage) throws F3BException {

		Connection lConn = null;
		Vector lHelponlini = new Vector();
		HelponlineSqlDAO lHelponlineSqlDao = null;

		try {
			lConn = getDBConnection();
			lHelponlineSqlDao = new HelponlineSqlDAO(lConn);
			lHelponlineSqlDao.ricercaHelponlinePaged(aHelponline, aPage);
			lHelponlini = new Vector(lHelponlineSqlDao.getModels());
		} catch (DAOException daoEx) {
			throw new F3BException(
					"HelponlineController.ExRicercaHelponlinePaged: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lHelponlineSqlDao);
			cleanup(lConn);
		}
		return lHelponlini;
	}

}