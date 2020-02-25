package siap.siep.tipoeventibdmc.controller;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Vector;

import siap.controller.SiapController;
import siap.siep.tipoeventibdmc.dao.TipoEventiBdmcDAO;
import siap.siep.tipoeventibdmc.dao.TipoEventiBdmcSqlDAO;
import siap.siep.tipoeventibdmc.model.TipoEventiBdmcModel;
import f3b.dao.DAOException;
import f3b.util.F3BException;

/**
 * <p>
 * Title: TipoEventiBdmcController
 * </p>
 * <p>
 * Description: Classe Controller per TipoEventiBdmc
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
public class TipoEventiBdmcController extends SiapController implements ITipoEventiBdmc {

	/*****************************************************************************
	 * Effettua l'inserimento di un TipoEventiBdmc a partire dai dati contenuti nel Model
	 * 
	 * @param aTipoEventiBdmc
	 *            Model con i dati da inserire
	 * @return il model con i dati inseriti e l'aggiunta dell'id del record inserito
	 * @throws F3BException
	 ****************************************************************************/
	public TipoEventiBdmcModel ExInserisciTipoEventiBdmc(TipoEventiBdmcModel aTipoEventiBdmc)
			throws F3BException {
		Connection lConn = null;
		TipoEventiBdmcDAO lTipDao = null;
		TipoEventiBdmcModel lTipMod = null;

		try {
			lConn = getDBConnection();
			lTipDao = new TipoEventiBdmcDAO(lConn);
			lTipDao.setDAOFromModel(aTipoEventiBdmc);
			BigDecimal lSequence = lTipDao.insert();
			commit(lConn);
			lTipMod = new TipoEventiBdmcModel(aTipoEventiBdmc);
			lTipMod.setMessage("Inserimento avvenuto correttamente!");
			lTipMod.setIdTipoEventiBdmc(lSequence);
		} catch (DAOException ex) {
			rollback(lConn);
			throw new F3BException("TipoEventiBdmcController.ExInserisci: Non posso inserire: " + ex);
		} finally {
			cleanup(lTipDao);
			cleanup(lConn);
		}

		return lTipMod;
	}

	/*****************************************************************************
	 * Effettua la ricerca dei dati TipoEventiBdmc
	 * 
	 * @param aTipoEventiBdmc
	 *            Model utilizzato per costruire le condizioni di ricerca Ogni valore attualizzato nel model
	 *            verrà utilizzato per imporre una condizione di ricerca
	 * @return un vettore di model con il risultato della ricerca
	 * @throws F3BException
	 ****************************************************************************/
	public Vector ExRicercaTipoEventiBdmc(TipoEventiBdmcModel aTipoEventiBdmc) throws F3BException {
		Connection lConn = null;
		Vector lTipoEventiBdmi = new Vector();
		TipoEventiBdmcDAO lTipDao = null;

		try {
			lConn = getDBConnection();
			lTipDao = new TipoEventiBdmcDAO(lConn);
			lTipDao.setCondizioni(aTipoEventiBdmc);
			lTipDao.setOrderBy();
			lTipDao.start();
			while (lTipDao.next()) {
				lTipoEventiBdmi.add((TipoEventiBdmcModel) lTipDao.getModel());
			}
			lTipDao.stop();
		} catch (DAOException daoEx) {
			throw new F3BException("TipoEventiBdmcController.ExRicercaTipoEventiBdmc: Non posso leggere : "
					+ daoEx);
		} finally {
			cleanup(lTipDao);
			cleanup(lConn);
		}

		return lTipoEventiBdmi;
	}

	/*****************************************************************************
	 * Effettua la ricerca per chiave
	 * 
	 * @param akey
	 *            valore della chiave del record da ricercare
	 * @return il model con i dati trovati
	 * @throws F3BException
	 ****************************************************************************/
	public TipoEventiBdmcModel ExRicercaTipoEventiBdmcById(BigDecimal aIdTipoEventiBdmc) throws F3BException {
		Connection lConn = null;
		TipoEventiBdmcModel lTipoEventiBdmcMod = new TipoEventiBdmcModel();
		TipoEventiBdmcSqlDAO lTipoEventiBdmcSqlDao = null;

		try {
			lConn = getDBConnection();
			lTipoEventiBdmcSqlDao = new TipoEventiBdmcSqlDAO(lConn);
			lTipoEventiBdmcSqlDao.ricercaTipoEventiBdmcByKey(aIdTipoEventiBdmc);
			lTipoEventiBdmcMod = (TipoEventiBdmcModel) lTipoEventiBdmcSqlDao.getModelByKey();
		} catch (DAOException daoEx) {
			throw new F3BException(
					"TipoEventiBdmcController.ExRicercaTipoEventiBdmcById: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lTipoEventiBdmcSqlDao);
			cleanup(lConn);
		}

		return lTipoEventiBdmcMod;
	}

	/*****************************************************************************
	 * Metodo che modifica i dati dell'TipoEventiBdmc Viene fatto l'update di tutti i campi del record
	 * recuperando i valori dal Model Se mancano dati nel model i corrispondenti valori della tabella verranno
	 * impostati a null
	 * 
	 * @param aTipoEventiBdmc
	 *            Model con i nuovi valori
	 * @throws F3BException
	 ****************************************************************************/
	public void ExModificaTipoEventiBdmc(TipoEventiBdmcModel aTipoEventiBdmc) throws F3BException {
		Connection lConn = null;
		TipoEventiBdmcDAO lTipDao = null;

		try {
			lConn = getDBConnection();
			lTipDao = new TipoEventiBdmcDAO(lConn);
			lTipDao.setDAOFromModel(aTipoEventiBdmc);
			lTipDao.selCondizioneUpdate(aTipoEventiBdmc.getIdTipoEventiBdmc());
			lTipDao.update();
			commit(lConn);
		} catch (DAOException ex) {
			rollback(lConn);
			throw new F3BException("TipoEventiBdmcController.ExModifica: Non posso inserire: " + ex);
		} finally {
			cleanup(lTipDao);
			cleanup(lConn);
		}
	}

	/*****************************************************************************
	 * Effettua la cancellazione del record
	 * 
	 * @param aTipoEventiBdmc
	 * @throws F3BException
	 ****************************************************************************/
	public void ExCancellaTipoEventiBdmc(TipoEventiBdmcModel aTipoEventiBdmc) throws F3BException {
		Connection lConn = null;
		TipoEventiBdmcDAO lTipDao = null;

		try {
			lConn = getDBConnection();
			lTipDao = new TipoEventiBdmcDAO(lConn);
			lTipDao.selCondizioneUpdate(aTipoEventiBdmc.getIdTipoEventiBdmc());
			lTipDao.delete();
			commit(lConn);
		} catch (DAOException daoEx) {
			rollback(lConn);
			throw new F3BException("TipoEventiBdmcController.ExCancellaTipoEventiBdmc: Non posso leggere : "
					+ daoEx);
		} finally {
			cleanup(lTipDao);
			cleanup(lConn);
		}
	}

	/*****************************************************************************
	 * Recupera il numero di record restituiti della ricerca. Utile in caso di ricerche paginate per ottenere
	 * il numero totale di record
	 * 
	 * @param aTipoEventiBdmc
	 * @return numero di record trovati dalla funzione dei ricerca
	 * @throws F3BException
	 ****************************************************************************/
	public BigDecimal ExGetCountTipoEventiBdmc(TipoEventiBdmcModel aTipoEventiBdmc) throws F3BException {
		Connection lConn = null;
		BigDecimal lCount = new BigDecimal(0);
		TipoEventiBdmcSqlDAO lTipoEventiBdmcSqlDao = null;

		try {
			lConn = getDBConnection();
			lTipoEventiBdmcSqlDao = new TipoEventiBdmcSqlDAO(lConn);
			lTipoEventiBdmcSqlDao.getCountTipoEventiBdmc(aTipoEventiBdmc);
			lTipoEventiBdmcSqlDao.start();
			lTipoEventiBdmcSqlDao.next();
			lCount = lTipoEventiBdmcSqlDao.getBigDecimal("HowManyRecords");
			lTipoEventiBdmcSqlDao.stop();
		} catch (DAOException daoEx) {
			throw new F3BException("TipoEventiBdmcController.ExGetCountTipoEventiBdmc: Non posso leggere : "
					+ daoEx);
		} finally {
			cleanup(lTipoEventiBdmcSqlDao);
			cleanup(lConn);
		}

		return lCount;
	}

	/*****************************************************************************
	 * Funzione di ricerca utilizzata per la paginazione che restituisce i risultati da visualizzare nella
	 * pagina specificata in input
	 * 
	 * @param aTipoEventiBdmc
	 *            model contenete i parametri della ricerca
	 * @param aPage
	 *            pagina per la quale si vogliono ottenere i risultati
	 * @return Vettore di model con i record da visualizzare nella pagina specificata in input
	 * @throws F3BException
	 ****************************************************************************/
	public Vector ExRicercaTipoEventiBdmcPaged(TipoEventiBdmcModel aTipoEventiBdmc, int aPage)
			throws F3BException {
		Connection lConn = null;
		Vector lTipoEventiBdmi = new Vector();
		TipoEventiBdmcSqlDAO lTipoEventiBdmcSqlDao = null;

		try {
			lConn = getDBConnection();
			lTipoEventiBdmcSqlDao = new TipoEventiBdmcSqlDAO(lConn);
			lTipoEventiBdmcSqlDao.ricercaTipoEventiBdmcPaged(aTipoEventiBdmc, aPage);
			lTipoEventiBdmi = new Vector(lTipoEventiBdmcSqlDao.getModels());
		} catch (DAOException daoEx) {
			throw new F3BException(
					"TipoEventiBdmcController.ExRicercaTipoEventiBdmcPaged: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lTipoEventiBdmcSqlDao);
			cleanup(lConn);
		}
		return lTipoEventiBdmi;
	}

}