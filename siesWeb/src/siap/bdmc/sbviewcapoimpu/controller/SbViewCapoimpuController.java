package siap.bdmc.sbviewcapoimpu.controller;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Vector;

import siap.bdmc.sbviewcapoimpu.dao.SbViewCapoimpuDAO;
import siap.bdmc.sbviewcapoimpu.dao.SbViewCapoimpuSqlDAO;
import siap.bdmc.sbviewcapoimpu.model.SbViewCapoimpuModel;
import siap.controller.SiapController;
import f3b.dao.DAOException;
import f3b.util.F3BException;

/**
 * <p>
 * Title: SbViewCapoimpuController
 * </p>
 * <p>
 * Description: Classe Controller per SbViewCapoimpu
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
public class SbViewCapoimpuController extends SiapController implements ISbViewCapoimpu {

	/*****************************************************************************
	 * Effettua l'inserimento di un SbViewCapoimpu a partire dai dati contenuti nel Model
	 * 
	 * @param aSbViewCapoimpu
	 *            Model con i dati da inserire
	 * @return il model con i dati inseriti e l'aggiunta dell'id del record inserito
	 * @throws F3BException
	 ****************************************************************************/
	public SbViewCapoimpuModel ExInserisciSbViewCapoimpu(SbViewCapoimpuModel aSbViewCapoimpu)
			throws F3BException {
		Connection lConn = null;
		SbViewCapoimpuDAO lSbVDao = null;
		SbViewCapoimpuModel lSbVMod = null;

		try {
			lConn = getDBConnection();
			lSbVDao = new SbViewCapoimpuDAO(lConn);
			lSbVDao.setDAOFromModel(aSbViewCapoimpu);
			lSbVDao.insert();
			commit(lConn);
			lSbVMod = new SbViewCapoimpuModel(aSbViewCapoimpu);
			lSbVMod.setMessage("Inserimento avvenuto correttamente!");
		} catch (DAOException ex) {
			rollback(lConn);
			throw new F3BException("SbViewCapoimpuController.ExInserisci: Non posso inserire: " + ex);
		} finally {
			cleanup(lSbVDao);
			cleanup(lConn);
		}

		return lSbVMod;
	}

	/*****************************************************************************
	 * Effettua la ricerca dei dati SbViewCapoimpu
	 * 
	 * @param aSbViewCapoimpu
	 *            Model utilizzato per costruire le condizioni di ricerca Ogni valore attualizzato nel model
	 *            verrà utilizzato per imporre una condizione di ricerca
	 * @return un vettore di model con il risultato della ricerca
	 * @throws F3BException
	 ****************************************************************************/
	public Vector ExRicercaSbViewCapoimpu(SbViewCapoimpuModel aSbViewCapoimpu) throws F3BException {
		Connection lConn = null;
		Vector lSbViewCapoimpi = new Vector();
		SbViewCapoimpuDAO lSbVDao = null;

		try {
			lConn = getDBConnection();
			lSbVDao = new SbViewCapoimpuDAO(lConn);
			lSbVDao.setCondizioni(aSbViewCapoimpu);
			lSbVDao.setOrderBy();
			lSbVDao.start();
			while (lSbVDao.next()) {
				lSbViewCapoimpi.add((SbViewCapoimpuModel) lSbVDao.getModel());
			}
			lSbVDao.stop();
		} catch (DAOException daoEx) {
			throw new F3BException("SbViewCapoimpuController.ExRicercaSbViewCapoimpu: Non posso leggere : "
					+ daoEx);
		} finally {
			cleanup(lSbVDao);
			cleanup(lConn);
		}

		return lSbViewCapoimpi;
	}

	/*****************************************************************************
	 * Effettua la ricerca per chiave
	 * 
	 * @param akey
	 *            valore della chiave del record da ricercare
	 * @return il model con i dati trovati
	 * @throws F3BException
	 ****************************************************************************/
	public SbViewCapoimpuModel ExRicercaSbViewCapoimpuById(BigDecimal aIdPren, BigDecimal aNumeProgCapoImpu)
			throws F3BException {
		Connection lConn = null;
		SbViewCapoimpuModel lSbViewCapoimpuMod = new SbViewCapoimpuModel();
		SbViewCapoimpuSqlDAO lSbViewCapoimpuSqlDao = null;

		try {
			lConn = getDBConnection();
			lSbViewCapoimpuSqlDao = new SbViewCapoimpuSqlDAO(lConn);
			lSbViewCapoimpuSqlDao.ricercaSbViewCapoimpuByKey(aIdPren, aNumeProgCapoImpu);
			lSbViewCapoimpuMod = (SbViewCapoimpuModel) lSbViewCapoimpuSqlDao.getModelByKey();
		} catch (DAOException daoEx) {
			throw new F3BException(
					"SbViewCapoimpuController.ExRicercaSbViewCapoimpuById: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lSbViewCapoimpuSqlDao);
			cleanup(lConn);
		}

		return lSbViewCapoimpuMod;
	}

	/*****************************************************************************
	 * Metodo che modifica i dati dell'SbViewCapoimpu Viene fatto l'update di tutti i campi del record
	 * recuperando i valori dal Model Se mancano dati nel model i corrispondenti valori della tabella verranno
	 * impostati a null
	 * 
	 * @param aSbViewCapoimpu
	 *            Model con i nuovi valori
	 * @throws F3BException
	 ****************************************************************************/
	public void ExModificaSbViewCapoimpu(SbViewCapoimpuModel aSbViewCapoimpu) throws F3BException {
		Connection lConn = null;
		SbViewCapoimpuDAO lSbVDao = null;

		try {
			lConn = getDBConnection();
			lSbVDao = new SbViewCapoimpuDAO(lConn);
			lSbVDao.setDAOFromModel(aSbViewCapoimpu);
			lSbVDao.selCondizioneUpdate(aSbViewCapoimpu.getIdPren(), aSbViewCapoimpu.getNumeProgCapoImpu());
			lSbVDao.update();
			commit(lConn);
		} catch (DAOException ex) {
			rollback(lConn);
			throw new F3BException("SbViewCapoimpuController.ExModifica: Non posso inserire: " + ex);
		} finally {
			cleanup(lSbVDao);
			cleanup(lConn);
		}
	}

	/*****************************************************************************
	 * Effettua la cancellazione del record
	 * 
	 * @param aSbViewCapoimpu
	 * @throws F3BException
	 ****************************************************************************/
	public void ExCancellaSbViewCapoimpu(SbViewCapoimpuModel aSbViewCapoimpu) throws F3BException {
		Connection lConn = null;
		SbViewCapoimpuDAO lSbVDao = null;

		try {
			lConn = getDBConnection();
			lSbVDao = new SbViewCapoimpuDAO(lConn);
			lSbVDao.selCondizioneUpdate(aSbViewCapoimpu.getIdPren(), aSbViewCapoimpu.getNumeProgCapoImpu());
			lSbVDao.delete();
			commit(lConn);
		} catch (DAOException daoEx) {
			rollback(lConn);
			throw new F3BException("SbViewCapoimpuController.ExCancellaSbViewCapoimpu: Non posso leggere : "
					+ daoEx);
		} finally {
			cleanup(lSbVDao);
			cleanup(lConn);
		}
	}

	/*****************************************************************************
	 * Recupera il numero di record restituiti della ricerca. Utile in caso di ricerche paginate per ottenere
	 * il numero totale di record
	 * 
	 * @param aSbViewCapoimpu
	 * @return numero di record trovati dalla funzione dei ricerca
	 * @throws F3BException
	 ****************************************************************************/
	public BigDecimal ExGetCountSbViewCapoimpu(SbViewCapoimpuModel aSbViewCapoimpu) throws F3BException {
		Connection lConn = null;
		BigDecimal lCount = new BigDecimal(0);
		SbViewCapoimpuSqlDAO lSbViewCapoimpuSqlDao = null;

		try {
			lConn = getDBConnection();
			lSbViewCapoimpuSqlDao = new SbViewCapoimpuSqlDAO(lConn);
			lSbViewCapoimpuSqlDao.getCountSbViewCapoimpu(aSbViewCapoimpu);
			lSbViewCapoimpuSqlDao.start();
			lSbViewCapoimpuSqlDao.next();
			lCount = lSbViewCapoimpuSqlDao.getBigDecimal("HowManyRecords");
			lSbViewCapoimpuSqlDao.stop();
		} catch (DAOException daoEx) {
			throw new F3BException("SbViewCapoimpuController.ExGetCountSbViewCapoimpu: Non posso leggere : "
					+ daoEx);
		} finally {
			cleanup(lSbViewCapoimpuSqlDao);
			cleanup(lConn);
		}

		return lCount;
	}

	/*****************************************************************************
	 * Funzione di ricerca utilizzata per la paginazione che restituisce i risultati da visualizzare nella
	 * pagina specificata in input
	 * 
	 * @param aSbViewCapoimpu
	 *            model contenete i parametri della ricerca
	 * @param aPage
	 *            pagina per la quale si vogliono ottenere i risultati
	 * @return Vettore di model con i record da visualizzare nella pagina specificata in input
	 * @throws F3BException
	 ****************************************************************************/
	public Vector ExRicercaSbViewCapoimpuPaged(SbViewCapoimpuModel aSbViewCapoimpu, int aPage)
			throws F3BException {
		Connection lConn = null;
		Vector lSbViewCapoimpi = new Vector();
		SbViewCapoimpuSqlDAO lSbViewCapoimpuSqlDao = null;

		try {
			lConn = getDBConnection();
			lSbViewCapoimpuSqlDao = new SbViewCapoimpuSqlDAO(lConn);
			lSbViewCapoimpuSqlDao.ricercaSbViewCapoimpuPaged(aSbViewCapoimpu, aPage);
			lSbViewCapoimpi = new Vector(lSbViewCapoimpuSqlDao.getModels());
		} catch (DAOException daoEx) {
			throw new F3BException(
					"SbViewCapoimpuController.ExRicercaSbViewCapoimpuPaged: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lSbViewCapoimpuSqlDao);
			cleanup(lConn);
		}
		return lSbViewCapoimpi;
	}

}