package siap.bdmc.sbperipren.controller;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Vector;

import siap.bdmc.sbperipren.dao.SbPeriprenDAO;
import siap.bdmc.sbperipren.dao.SbPeriprenSqlDAO;
import siap.bdmc.sbperipren.model.SbPeriprenModel;
import siap.controller.SiapController;
import f3b.dao.DAOException;
import f3b.util.F3BException;

/**
 * <p>
 * Title: SbPeriprenController
 * </p>
 * <p>
 * Description: Classe Controller per SbPeripren
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
public class SbPeriprenController extends SiapController implements ISbPeripren {

	/*****************************************************************************
	 * Effettua l'inserimento di un SbPeripren a partire dai dati contenuti nel Model
	 * 
	 * @param aSbPeripren
	 *            Model con i dati da inserire
	 * @return il model con i dati inseriti e l'aggiunta dell'id del record inserito
	 * @throws F3BException
	 ****************************************************************************/
	public SbPeriprenModel ExInserisciSbPeripren(SbPeriprenModel aSbPeripren) throws F3BException {
		Connection lConn = null;
		SbPeriprenDAO lSbPDao = null;
		SbPeriprenModel lSbPMod = null;

		try {
			lConn = getDBConnection();
			lSbPDao = new SbPeriprenDAO(lConn);
			lSbPDao.setDAOFromModel(aSbPeripren);
			BigDecimal lSequence = lSbPDao.insert();
			commit(lConn);
			lSbPMod = new SbPeriprenModel(aSbPeripren);
			lSbPMod.setMessage("Inserimento avvenuto correttamente!");
			lSbPMod.setProgPeriPres(lSequence);
		} catch (DAOException ex) {
			rollback(lConn);
			throw new F3BException("SbPeriprenController.ExInserisci: Non posso inserire: " + ex);
		} finally {
			cleanup(lSbPDao);
			cleanup(lConn);
		}

		return lSbPMod;
	}

	/*****************************************************************************
	 * Effettua la ricerca dei dati SbPeripren
	 * 
	 * @param aSbPeripren
	 *            Model utilizzato per costruire le condizioni di ricerca Ogni valore attualizzato nel model
	 *            verrà utilizzato per imporre una condizione di ricerca
	 * @return un vettore di model con il risultato della ricerca
	 * @throws F3BException
	 ****************************************************************************/
	public Vector ExRicercaSbPeripren(SbPeriprenModel aSbPeripren) throws F3BException {
		Connection lConn = null;
		Vector lSbPeriprei = new Vector();
		SbPeriprenDAO lSbPDao = null;

		try {
			lConn = getDBConnection();
			lSbPDao = new SbPeriprenDAO(lConn);
			lSbPDao.setCondizioni(aSbPeripren);
			lSbPDao.setOrderBy();
			lSbPDao.start();
			while (lSbPDao.next()) {
				lSbPeriprei.add((SbPeriprenModel) lSbPDao.getModel());
			}
			lSbPDao.stop();
		} catch (Exception daoEx) {
			throw new F3BException("SbPeriprenController.ExRicercaSbPeripren: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lSbPDao);
			cleanup(lConn);
		}

		return lSbPeriprei;
	}

	/*****************************************************************************
	 * Effettua la ricerca per chiave
	 * 
	 * @param akey
	 *            valore della chiave del record da ricercare
	 * @return il model con i dati trovati
	 * @throws F3BException
	 ****************************************************************************/
	public SbPeriprenModel ExRicercaSbPeriprenById(BigDecimal aProgPeriPres) throws F3BException {
		Connection lConn = null;
		SbPeriprenModel lSbPeriprenMod = new SbPeriprenModel();
		SbPeriprenSqlDAO lSbPeriprenSqlDao = null;

		try {
			lConn = getDBConnection();
			lSbPeriprenSqlDao = new SbPeriprenSqlDAO(lConn);
			lSbPeriprenSqlDao.ricercaSbPeriprenByKey(aProgPeriPres);
			lSbPeriprenMod = (SbPeriprenModel) lSbPeriprenSqlDao.getModelByKey();
		} catch (DAOException daoEx) {
			throw new F3BException("SbPeriprenController.ExRicercaSbPeriprenById: Non posso leggere : "
					+ daoEx);
		} finally {
			cleanup(lSbPeriprenSqlDao);
			cleanup(lConn);
		}

		return lSbPeriprenMod;
	}

	/*****************************************************************************
	 * Metodo che modifica i dati dell'SbPeripren Viene fatto l'update di tutti i campi del record recuperando
	 * i valori dal Model Se mancano dati nel model i corrispondenti valori della tabella verranno impostati a
	 * null
	 * 
	 * @param aSbPeripren
	 *            Model con i nuovi valori
	 * @throws F3BException
	 ****************************************************************************/
	public void ExModificaSbPeripren(SbPeriprenModel aSbPeripren) throws F3BException {
		Connection lConn = null;
		SbPeriprenDAO lSbPDao = null;

		try {
			lConn = getDBConnection();
			lSbPDao = new SbPeriprenDAO(lConn);
			lSbPDao.setDAOFromModel(aSbPeripren);
			lSbPDao.selCondizioneUpdate(aSbPeripren.getProgPeriPres());
			lSbPDao.update();
			commit(lConn);
		} catch (DAOException ex) {
			rollback(lConn);
			throw new F3BException("SbPeriprenController.ExModifica: Non posso inserire: " + ex);
		} finally {
			cleanup(lSbPDao);
			cleanup(lConn);
		}
	}

	/*****************************************************************************
	 * Effettua la cancellazione del record
	 * 
	 * @param aSbPeripren
	 * @throws F3BException
	 ****************************************************************************/
	public void ExCancellaSbPeripren(SbPeriprenModel aSbPeripren) throws F3BException {
		Connection lConn = null;
		SbPeriprenDAO lSbPDao = null;

		try {
			lConn = getDBConnection();
			lSbPDao = new SbPeriprenDAO(lConn);
			lSbPDao.selCondizioneUpdate(aSbPeripren.getProgPeriPres());
			lSbPDao.delete();
			commit(lConn);
		} catch (DAOException daoEx) {
			rollback(lConn);
			throw new F3BException("SbPeriprenController.ExCancellaSbPeripren: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lSbPDao);
			cleanup(lConn);
		}
	}

	/*****************************************************************************
	 * Recupera il numero di record restituiti della ricerca. Utile in caso di ricerche paginate per ottenere
	 * il numero totale di record
	 * 
	 * @param aSbPeripren
	 * @return numero di record trovati dalla funzione dei ricerca
	 * @throws F3BException
	 ****************************************************************************/
	public BigDecimal ExGetCountSbPeripren(SbPeriprenModel aSbPeripren) throws F3BException {
		Connection lConn = null;
		BigDecimal lCount = new BigDecimal(0);
		SbPeriprenSqlDAO lSbPeriprenSqlDao = null;

		try {
			lConn = getDBConnection();
			lSbPeriprenSqlDao = new SbPeriprenSqlDAO(lConn);
			lSbPeriprenSqlDao.getCountSbPeripren(aSbPeripren);
			lSbPeriprenSqlDao.start();
			lSbPeriprenSqlDao.next();
			lCount = lSbPeriprenSqlDao.getBigDecimal("HowManyRecords");
			lSbPeriprenSqlDao.stop();
		} catch (DAOException daoEx) {
			throw new F3BException("SbPeriprenController.ExGetCountSbPeripren: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lSbPeriprenSqlDao);
			cleanup(lConn);
		}

		return lCount;
	}

	/*****************************************************************************
	 * Funzione di ricerca utilizzata per la paginazione che restituisce i risultati da visualizzare nella
	 * pagina specificata in input
	 * 
	 * @param aSbPeripren
	 *            model contenete i parametri della ricerca
	 * @param aPage
	 *            pagina per la quale si vogliono ottenere i risultati
	 * @return Vettore di model con i record da visualizzare nella pagina specificata in input
	 * @throws F3BException
	 ****************************************************************************/
	public Vector ExRicercaSbPeriprenPaged(SbPeriprenModel aSbPeripren, int aPage) throws F3BException {
		Connection lConn = null;
		Vector lSbPeriprei = new Vector();
		SbPeriprenSqlDAO lSbPeriprenSqlDao = null;

		try {
			lConn = getDBConnection();
			lSbPeriprenSqlDao = new SbPeriprenSqlDAO(lConn);
			lSbPeriprenSqlDao.ricercaSbPeriprenPaged(aSbPeripren, aPage);
			lSbPeriprei = new Vector(lSbPeriprenSqlDao.getModels());
		} catch (DAOException daoEx) {
			throw new F3BException("SbPeriprenController.ExRicercaSbPeriprenPaged: Non posso leggere : "
					+ daoEx);
		} finally {
			cleanup(lSbPeriprenSqlDao);
			cleanup(lConn);
		}
		return lSbPeriprei;
	}

}