package siap.bdmc.sbviewreat.controller;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Vector;

import f3b.dao.DAOException;
import f3b.util.F3BException;
import siap.bdmc.sbviewreat.dao.SbViewReatDAO;
import siap.bdmc.sbviewreat.dao.SbViewReatSqlDAO;
import siap.bdmc.sbviewreat.model.SbViewReatModel;
import siap.controller.SiapController;

/**
 * <p>
 * Title: SbViewReatController
 * </p>
 * <p>
 * Description: Classe Controller per SbViewReat
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
public class SbViewReatController extends SiapController implements ISbViewReat {

	/*****************************************************************************
	 * Effettua l'inserimento di un SbViewReat a partire dai dati contenuti nel Model
	 *
	 * @param aSbViewReat
	 *            Model con i dati da inserire
	 * @return il model con i dati inseriti e l'aggiunta dell'id del record inserito
	 * @throws F3BException
	 ****************************************************************************/
	public SbViewReatModel ExInserisciSbViewReat(SbViewReatModel aSbViewReat) throws F3BException {

		Connection lConn = null;
		SbViewReatDAO lSbVDao = null;
		SbViewReatModel lSbVMod = null;

		try {
			lConn = getDBConnection();
			lSbVDao = new SbViewReatDAO(lConn);
			lSbVDao.setDAOFromModel(aSbViewReat);
			lSbVDao.insert();
			commit(lConn);
			lSbVMod = new SbViewReatModel(aSbViewReat);
			lSbVMod.setMessage("Inserimento avvenuto correttamente!");
		} catch (DAOException ex) {
			rollback(lConn);
			throw new F3BException("SbViewReatController.ExInserisci: Non posso inserire: " + ex);
		} finally {
			cleanup(lSbVDao);
			cleanup(lConn);
		}

		return lSbVMod;
	}

	/*****************************************************************************
	 * Effettua la ricerca dei dati SbViewReat
	 *
	 * @param aSbViewReat
	 *            Model utilizzato per costruire le condizioni di ricerca Ogni valore attualizzato nel model
	 *            verrà utilizzato per imporre una condizione di ricerca
	 * @return un vettore di model con il risultato della ricerca
	 * @throws F3BException
	 ****************************************************************************/
	public Vector ExRicercaSbViewReat(SbViewReatModel aSbViewReat) throws F3BException {

		Connection lConn = null;
		Vector lSbViewReai = new Vector();
		SbViewReatDAO lSbVDao = null;

		try {
			lConn = getDBConnection();
			lSbVDao = new SbViewReatDAO(lConn);
			lSbVDao.setCondizioni(aSbViewReat);
			lSbVDao.setOrderBy();
			lSbVDao.start();
			while (lSbVDao.next()) {
				lSbViewReai.add(lSbVDao.getModel());
			}
			lSbVDao.stop();
		} catch (DAOException daoEx) {
			throw new F3BException("SbViewReatController.ExRicercaSbViewReat: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lSbVDao);
			cleanup(lConn);
		}

		return lSbViewReai;
	}

	/*****************************************************************************
	 * Effettua la ricerca per chiave
	 *
	 * @param akey
	 *            valore della chiave del record da ricercare
	 * @return il model con i dati trovati
	 * @throws F3BException
	 ****************************************************************************/
	public SbViewReatModel ExRicercaSbViewReatById(BigDecimal aIdPren, BigDecimal aNumeProgCapoImpu,
			BigDecimal aNumeProgReat) throws F3BException {

		Connection lConn = null;
		SbViewReatModel lSbViewReatMod = new SbViewReatModel();
		SbViewReatSqlDAO lSbViewReatSqlDao = null;

		try {
			lConn = getDBConnection();
			lSbViewReatSqlDao = new SbViewReatSqlDAO(lConn);
			lSbViewReatSqlDao.ricercaSbViewReatByKey(aIdPren, aNumeProgCapoImpu, aNumeProgReat);
			lSbViewReatMod = (SbViewReatModel) lSbViewReatSqlDao.getModelByKey();
		} catch (DAOException daoEx) {
			throw new F3BException(
					"SbViewReatController.ExRicercaSbViewReatById: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lSbViewReatSqlDao);
			cleanup(lConn);
		}

		return lSbViewReatMod;
	}

	/*****************************************************************************
	 * Metodo che modifica i dati dell'SbViewReat Viene fatto l'update di tutti i campi del record recuperando
	 * i valori dal Model Se mancano dati nel model i corrispondenti valori della tabella verranno impostati a
	 * null
	 *
	 * @param aSbViewReat
	 *            Model con i nuovi valori
	 * @throws F3BException
	 ****************************************************************************/
	public void ExModificaSbViewReat(SbViewReatModel aSbViewReat) throws F3BException {

		Connection lConn = null;
		SbViewReatDAO lSbVDao = null;

		try {
			lConn = getDBConnection();
			lSbVDao = new SbViewReatDAO(lConn);
			lSbVDao.setDAOFromModel(aSbViewReat);
			lSbVDao.selCondizioneUpdate(aSbViewReat.getIdPren(), aSbViewReat.getNumeProgCapoImpu(),
					aSbViewReat.getNumeProgReat());
			lSbVDao.update();
			commit(lConn);
		} catch (DAOException ex) {
			rollback(lConn);
			throw new F3BException("SbViewReatController.ExModifica: Non posso inserire: " + ex);
		} finally {
			cleanup(lSbVDao);
			cleanup(lConn);
		}
	}

	/*****************************************************************************
	 * Effettua la cancellazione del record
	 *
	 * @param aSbViewReat
	 * @throws F3BException
	 ****************************************************************************/
	public void ExCancellaSbViewReat(SbViewReatModel aSbViewReat) throws F3BException {

		Connection lConn = null;
		SbViewReatDAO lSbVDao = null;

		try {
			lConn = getDBConnection();
			lSbVDao = new SbViewReatDAO(lConn);
			lSbVDao.selCondizioneUpdate(aSbViewReat.getIdPren(), aSbViewReat.getNumeProgCapoImpu(),
					aSbViewReat.getNumeProgReat());
			lSbVDao.delete();
			commit(lConn);
		} catch (DAOException daoEx) {
			rollback(lConn);
			throw new F3BException("SbViewReatController.ExCancellaSbViewReat: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lSbVDao);
			cleanup(lConn);
		}
	}

	/*****************************************************************************
	 * Recupera il numero di record restituiti della ricerca. Utile in caso di ricerche paginate per ottenere
	 * il numero totale di record
	 *
	 * @param aSbViewReat
	 * @return numero di record trovati dalla funzione dei ricerca
	 * @throws F3BException
	 ****************************************************************************/
	public BigDecimal ExGetCountSbViewReat(SbViewReatModel aSbViewReat) throws F3BException {

		Connection lConn = null;
		BigDecimal lCount = new BigDecimal(0);
		SbViewReatSqlDAO lSbViewReatSqlDao = null;

		try {
			lConn = getDBConnection();
			lSbViewReatSqlDao = new SbViewReatSqlDAO(lConn);
			lSbViewReatSqlDao.getCountSbViewReat(aSbViewReat);
			lSbViewReatSqlDao.start();
			lSbViewReatSqlDao.next();
			lCount = lSbViewReatSqlDao.getBigDecimal("HowManyRecords");
			lSbViewReatSqlDao.stop();
		} catch (DAOException daoEx) {
			throw new F3BException("SbViewReatController.ExGetCountSbViewReat: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lSbViewReatSqlDao);
			cleanup(lConn);
		}

		return lCount;
	}

	/*****************************************************************************
	 * Funzione di ricerca utilizzata per la paginazione che restituisce i risultati da visualizzare nella
	 * pagina specificata in input
	 *
	 * @param aSbViewReat
	 *            model contenete i parametri della ricerca
	 * @param aPage
	 *            pagina per la quale si vogliono ottenere i risultati
	 * @return Vettore di model con i record da visualizzare nella pagina specificata in input
	 * @throws F3BException
	 ****************************************************************************/
	public Vector ExRicercaSbViewReatPaged(SbViewReatModel aSbViewReat, int aPage) throws F3BException {

		Connection lConn = null;
		Vector lSbViewReai = new Vector();
		SbViewReatSqlDAO lSbViewReatSqlDao = null;

		try {
			lConn = getDBConnection();
			lSbViewReatSqlDao = new SbViewReatSqlDAO(lConn);
			lSbViewReatSqlDao.ricercaSbViewReatPaged(aSbViewReat, aPage);
			lSbViewReai = new Vector(lSbViewReatSqlDao.getModels());
		} catch (DAOException daoEx) {
			throw new F3BException(
					"SbViewReatController.ExRicercaSbViewReatPaged: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lSbViewReatSqlDao);
			cleanup(lConn);
		}
		return lSbViewReai;
	}

}