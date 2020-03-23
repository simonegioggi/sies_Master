package siap.bdmc.sbviewprocpena.controller;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Vector;

import f3b.dao.DAOException;
import f3b.util.F3BException;
import siap.bdmc.sbviewprocpena.dao.SbViewProcpenaDAO;
import siap.bdmc.sbviewprocpena.dao.SbViewProcpenaSqlDAO;
import siap.bdmc.sbviewprocpena.model.SbViewProcpenaModel;
import siap.controller.SiapController;

/**
 * <p>
 * Title: SbViewProcpenaController
 * </p>
 * <p>
 * Description: Classe Controller per SbViewProcpena
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
public class SbViewProcpenaController extends SiapController implements ISbViewProcpena {

	/*****************************************************************************
	 * Effettua l'inserimento di un SbViewProcpena a partire dai dati contenuti nel Model
	 *
	 * @param aSbViewProcpena
	 *            Model con i dati da inserire
	 * @return il model con i dati inseriti e l'aggiunta dell'id del record inserito
	 * @throws F3BException
	 ****************************************************************************/
	public SbViewProcpenaModel ExInserisciSbViewProcpena(SbViewProcpenaModel aSbViewProcpena)
			throws F3BException {

		Connection lConn = null;
		SbViewProcpenaDAO lSbVDao = null;
		SbViewProcpenaModel lSbVMod = null;

		try {
			lConn = getDBConnection();
			lSbVDao = new SbViewProcpenaDAO(lConn);
			lSbVDao.setDAOFromModel(aSbViewProcpena);
			BigDecimal lSequence = lSbVDao.insert();
			commit(lConn);
			lSbVMod = new SbViewProcpenaModel(aSbViewProcpena);
			lSbVMod.setMessage("Inserimento avvenuto correttamente!");
			lSbVMod.setIdPren(lSequence);
		} catch (DAOException ex) {
			rollback(lConn);
			throw new F3BException("SbViewProcpenaController.ExInserisci: Non posso inserire: " + ex);
		} finally {
			cleanup(lSbVDao);
			cleanup(lConn);
		}

		return lSbVMod;
	}

	/*****************************************************************************
	 * Effettua la ricerca dei dati SbViewProcpena
	 *
	 * @param aSbViewProcpena
	 *            Model utilizzato per costruire le condizioni di ricerca Ogni valore attualizzato nel model
	 *            verrà utilizzato per imporre una condizione di ricerca
	 * @return un vettore di model con il risultato della ricerca
	 * @throws F3BException
	 ****************************************************************************/
	public Vector ExRicercaSbViewProcpena(SbViewProcpenaModel aSbViewProcpena) throws F3BException {

		Connection lConn = null;
		Vector lSbViewProcpeni = new Vector();
		SbViewProcpenaSqlDAO lSbVDao = null;

		try {
			lConn = getDBConnection();
			lSbVDao = new SbViewProcpenaSqlDAO(lConn);
			lSbVDao.ricercaSbViewProcpena(aSbViewProcpena);
			// lSbVDao.setOrderBy();
			lSbViewProcpeni = new Vector(lSbVDao.getModels());
		} catch (DAOException daoEx) {
			throw new F3BException(
					"SbViewProcpenaController.ExRicercaSbViewProcpena: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lSbVDao);
			cleanup(lConn);
		}

		return lSbViewProcpeni;
	}

	/*****************************************************************************
	 * Effettua la ricerca per chiave
	 *
	 * @param akey
	 *            valore della chiave del record da ricercare
	 * @return il model con i dati trovati
	 * @throws F3BException
	 ****************************************************************************/
	public SbViewProcpenaModel ExRicercaSbViewProcpenaById(BigDecimal aIdPren) throws F3BException {

		Connection lConn = null;
		SbViewProcpenaModel lSbViewProcpenaMod = new SbViewProcpenaModel();
		SbViewProcpenaSqlDAO lSbViewProcpenaSqlDao = null;

		try {
			lConn = getDBConnection();
			lSbViewProcpenaSqlDao = new SbViewProcpenaSqlDAO(lConn);
			lSbViewProcpenaSqlDao.ricercaSbViewProcpenaByKey(aIdPren);
			lSbViewProcpenaMod = (SbViewProcpenaModel) lSbViewProcpenaSqlDao.getModelByKey();
		} catch (DAOException daoEx) {
			throw new F3BException(
					"SbViewProcpenaController.ExRicercaSbViewProcpenaById: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lSbViewProcpenaSqlDao);
			cleanup(lConn);
		}

		return lSbViewProcpenaMod;
	}

	/*****************************************************************************
	 * Metodo che modifica i dati dell'SbViewProcpena Viene fatto l'update di tutti i campi del record
	 * recuperando i valori dal Model Se mancano dati nel model i corrispondenti valori della tabella verranno
	 * impostati a null
	 *
	 * @param aSbViewProcpena
	 *            Model con i nuovi valori
	 * @throws F3BException
	 ****************************************************************************/
	public void ExModificaSbViewProcpena(SbViewProcpenaModel aSbViewProcpena) throws F3BException {

		Connection lConn = null;
		SbViewProcpenaDAO lSbVDao = null;

		try {
			lConn = getDBConnection();
			lSbVDao = new SbViewProcpenaDAO(lConn);
			lSbVDao.setDAOFromModel(aSbViewProcpena);
			lSbVDao.selCondizioneUpdate(aSbViewProcpena.getIdPren());
			lSbVDao.update();
			commit(lConn);
		} catch (DAOException ex) {
			rollback(lConn);
			throw new F3BException("SbViewProcpenaController.ExModifica: Non posso inserire: " + ex);
		} finally {
			cleanup(lSbVDao);
			cleanup(lConn);
		}
	}

	/*****************************************************************************
	 * Effettua la cancellazione del record
	 *
	 * @param aSbViewProcpena
	 * @throws F3BException
	 ****************************************************************************/
	public void ExCancellaSbViewProcpena(SbViewProcpenaModel aSbViewProcpena) throws F3BException {

		Connection lConn = null;
		SbViewProcpenaDAO lSbVDao = null;

		try {
			lConn = getDBConnection();
			lSbVDao = new SbViewProcpenaDAO(lConn);
			lSbVDao.selCondizioneUpdate(aSbViewProcpena.getIdPren());
			lSbVDao.delete();
			commit(lConn);
		} catch (DAOException daoEx) {
			rollback(lConn);
			throw new F3BException(
					"SbViewProcpenaController.ExCancellaSbViewProcpena: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lSbVDao);
			cleanup(lConn);
		}
	}

	/*****************************************************************************
	 * Recupera il numero di record restituiti della ricerca. Utile in caso di ricerche paginate per ottenere
	 * il numero totale di record
	 *
	 * @param aSbViewProcpena
	 * @return numero di record trovati dalla funzione dei ricerca
	 * @throws F3BException
	 ****************************************************************************/
	public BigDecimal ExGetCountSbViewProcpena(SbViewProcpenaModel aSbViewProcpena) throws F3BException {

		Connection lConn = null;
		BigDecimal lCount = new BigDecimal(0);
		SbViewProcpenaSqlDAO lSbViewProcpenaSqlDao = null;

		try {
			lConn = getDBConnection();
			lSbViewProcpenaSqlDao = new SbViewProcpenaSqlDAO(lConn);
			lSbViewProcpenaSqlDao.getCountSbViewProcpena(aSbViewProcpena);
			lSbViewProcpenaSqlDao.start();
			lSbViewProcpenaSqlDao.next();
			lCount = lSbViewProcpenaSqlDao.getBigDecimal("HowManyRecords");
			lSbViewProcpenaSqlDao.stop();
		} catch (DAOException daoEx) {
			throw new F3BException(
					"SbViewProcpenaController.ExGetCountSbViewProcpena: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lSbViewProcpenaSqlDao);
			cleanup(lConn);
		}

		return lCount;
	}

	/*****************************************************************************
	 * Funzione di ricerca utilizzata per la paginazione che restituisce i risultati da visualizzare nella
	 * pagina specificata in input
	 *
	 * @param aSbViewProcpena
	 *            model contenete i parametri della ricerca
	 * @param aPage
	 *            pagina per la quale si vogliono ottenere i risultati
	 * @return Vettore di model con i record da visualizzare nella pagina specificata in input
	 * @throws F3BException
	 ****************************************************************************/
	public Vector ExRicercaSbViewProcpenaPaged(SbViewProcpenaModel aSbViewProcpena, int aPage)
			throws F3BException {

		Connection lConn = null;
		Vector lSbViewProcpeni = new Vector();
		SbViewProcpenaSqlDAO lSbViewProcpenaSqlDao = null;

		try {
			lConn = getDBConnection();
			lSbViewProcpenaSqlDao = new SbViewProcpenaSqlDAO(lConn);
			lSbViewProcpenaSqlDao.ricercaSbViewProcpenaPaged(aSbViewProcpena, aPage);
			lSbViewProcpeni = new Vector(lSbViewProcpenaSqlDao.getModels());
		} catch (DAOException daoEx) {
			throw new F3BException(
					"SbViewProcpenaController.ExRicercaSbViewProcpenaPaged: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lSbViewProcpenaSqlDao);
			cleanup(lConn);
		}
		return lSbViewProcpeni;
	}

}