package siap.siep.tipologiaorario.controller;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Vector;

import siap.siep.tipologiaorario.dao.TipologiaOrarioDAO;
import siap.siep.tipologiaorario.dao.TipologiaOrarioSqlDAO;
import siap.siep.tipologiaorario.model.TipologiaOrarioModel;
import f3b.controller.GenericController;
import f3b.dao.DAOException;
import f3b.util.F3BException;

/**
 * <p>
 * Title: TipologiaOrarioController
 * </p>
 * <p>
 * Description: Classe Controller per TipologiaOrario
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
public class TipologiaOrarioController extends GenericController implements ITipologiaOrario {

	/*****************************************************************************
	 * Effettua l'inserimento di un TipologiaOrario a partire dai dati contenuti nel Model
	 * 
	 * @param aTipologiaOrario
	 *            Model con i dati da inserire
	 * @return il model con i dati inseriti e l'aggiunta dell'id del record inserito
	 * @throws F3BException
	 ****************************************************************************/
	public TipologiaOrarioModel ExInserisciTipologiaOrario(TipologiaOrarioModel aTipologiaOrario)
			throws F3BException {
		Connection lConn = null;
		TipologiaOrarioDAO lTipDao = null;
		TipologiaOrarioModel lTipMod = null;

		try {
			lConn = getDBConnection();
			lTipDao = new TipologiaOrarioDAO(lConn);
			lTipDao.setDAOFromModel(aTipologiaOrario);
			BigDecimal lSequence = lTipDao.insert();
			commit(lConn);
			lTipMod = new TipologiaOrarioModel(aTipologiaOrario);
			lTipMod.setMessage("Inserimento avvenuto correttamente!");
			lTipMod.setIdTipologiaOrario(lSequence);
		} catch (DAOException ex) {
			rollback(lConn);
			throw new F3BException("TipologiaOrarioController.ExInserisci: Non posso inserire: " + ex);
		} finally {
			cleanup(lTipDao);
			cleanup(lConn);
		}

		return lTipMod;
	}

	/*****************************************************************************
	 * Effettua la ricerca dei dati TipologiaOrario
	 * 
	 * @param aTipologiaOrario
	 *            Model utilizzato per costruire le condizioni di ricerca Ogni valore attualizzato nel model
	 *            verrà utilizzato per imporre una condizione di ricerca
	 * @return un vettore di model con il risultato della ricerca
	 * @throws F3BException
	 ****************************************************************************/
	public Vector ExRicercaTipologiaOrario(TipologiaOrarioModel aTipologiaOrario) throws F3BException {
		Connection lConn = null;
		Vector lTipologiaOrarii = new Vector();
		TipologiaOrarioDAO lTipDao = null;

		try {
			lConn = getDBConnection();
			lTipDao = new TipologiaOrarioDAO(lConn);
			lTipDao.setCondizioni(aTipologiaOrario);
			lTipDao.setOrderBy();
			lTipDao.start();
			while (lTipDao.next()) {
				lTipologiaOrarii.add((TipologiaOrarioModel) lTipDao.getModel());
			}
			lTipDao.stop();
		} catch (DAOException daoEx) {
			throw new F3BException("TipologiaOrarioController.ExRicercaTipologiaOrario: Non posso leggere : "
					+ daoEx);
		} finally {
			cleanup(lTipDao);
			cleanup(lConn);
		}

		return lTipologiaOrarii;
	}

	/*****************************************************************************
	 * Effettua la ricerca per chiave
	 * 
	 * @param akey
	 *            valore della chiave del record da ricercare
	 * @return il model con i dati trovati
	 * @throws F3BException
	 ****************************************************************************/
	public TipologiaOrarioModel ExRicercaTipologiaOrarioById(BigDecimal aIdTipologiaOrario)
			throws F3BException {
		Connection lConn = null;
		TipologiaOrarioModel lTipologiaOrario = new TipologiaOrarioModel();
		TipologiaOrarioSqlDAO lTipologiaOrarioSqlDao = null;

		try {
			lConn = getDBConnection();
			lTipologiaOrarioSqlDao = new TipologiaOrarioSqlDAO(lConn);
			lTipologiaOrarioSqlDao.ricercaTipologiaOrarioByKey(aIdTipologiaOrario);
			lTipologiaOrario = (TipologiaOrarioModel) lTipologiaOrarioSqlDao.getModelByKey();
		} catch (DAOException daoEx) {
			throw new F3BException(
					"TipologiaOrarioController.ExRicercaTipologiaOrarioById: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lTipologiaOrarioSqlDao);
			cleanup(lConn);
		}

		return lTipologiaOrario;
	}

	public Vector ExRicercaTipologiaOrarioByIdBeneficio(BigDecimal aIdBeneficio) throws F3BException {
		Connection lConn = null;
		Vector lTipologiaOrarioMod = null;
		TipologiaOrarioSqlDAO lTipologiaOrarioSqlDao = null;

		try {
			lConn = getDBConnection();
			lTipologiaOrarioSqlDao = new TipologiaOrarioSqlDAO(lConn);
			lTipologiaOrarioSqlDao.ricercaTipologiaOrarioByIdBeneficio(aIdBeneficio);
			lTipologiaOrarioMod = new Vector(lTipologiaOrarioSqlDao.getModels());
		} catch (DAOException daoEx) {
			throw new F3BException(
					"TipologiaOrarioController.ExRicercaTipologiaOrarioByIdBeneficio: Non posso leggere : "
							+ daoEx);
		} finally {
			cleanup(lTipologiaOrarioSqlDao);
			cleanup(lConn);
		}

		return lTipologiaOrarioMod;
	}

	/*****************************************************************************
	 * Metodo che modifica i dati dell'TipologiaOrario Viene fatto l'update di tutti i campi del record
	 * recuperando i valori dal Model Se mancano dati nel model i corrispondenti valori della tabella verranno
	 * impostati a null
	 * 
	 * @param aTipologiaOrario
	 *            Model con i nuovi valori
	 * @throws F3BException
	 ****************************************************************************/
	public void ExModificaTipologiaOrario(TipologiaOrarioModel aTipologiaOrario) throws F3BException {
		Connection lConn = null;
		TipologiaOrarioDAO lTipDao = null;

		try {
			lConn = getDBConnection();
			lTipDao = new TipologiaOrarioDAO(lConn);
			lTipDao.setDAOFromModel(aTipologiaOrario);
			lTipDao.selCondizioneUpdate(aTipologiaOrario.getIdTipologiaOrario());
			lTipDao.update();
			commit(lConn);
		} catch (DAOException ex) {
			rollback(lConn);
			throw new F3BException("TipologiaOrarioController.ExModifica: Non posso inserire: " + ex);
		} finally {
			cleanup(lTipDao);
			cleanup(lConn);
		}
	}

	/*****************************************************************************
	 * Effettua la cancellazione del record
	 * 
	 * @param aTipologiaOrario
	 * @throws F3BException
	 ****************************************************************************/
	public void ExCancellaTipologiaOrario(TipologiaOrarioModel aTipologiaOrario) throws F3BException {
		Connection lConn = null;
		TipologiaOrarioDAO lTipDao = null;

		try {
			lConn = getDBConnection();
			lTipDao = new TipologiaOrarioDAO(lConn);
			lTipDao.selCondizioneUpdate(aTipologiaOrario.getIdTipologiaOrario());
			lTipDao.delete();
			commit(lConn);
		} catch (DAOException daoEx) {
			rollback(lConn);
			throw new F3BException(
					"TipologiaOrarioController.ExCancellaTipologiaOrario: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lTipDao);
			cleanup(lConn);
		}
	}

	/*****************************************************************************
	 * Recupera il numero di record restituiti della ricerca. Utile in caso di ricerche paginate per ottenere
	 * il numero totale di record
	 * 
	 * @param aTipologiaOrario
	 * @return numero di record trovati dalla funzione dei ricerca
	 * @throws F3BException
	 ****************************************************************************/
	public BigDecimal ExGetCountTipologiaOrario(TipologiaOrarioModel aTipologiaOrario) throws F3BException {
		Connection lConn = null;
		BigDecimal lCount = new BigDecimal(0);
		TipologiaOrarioSqlDAO lTipologiaOrarioSqlDao = null;

		try {
			lConn = getDBConnection();
			lTipologiaOrarioSqlDao = new TipologiaOrarioSqlDAO(lConn);
			lTipologiaOrarioSqlDao.getCountTipologiaOrario(aTipologiaOrario);
			lTipologiaOrarioSqlDao.start();
			lTipologiaOrarioSqlDao.next();
			lCount = lTipologiaOrarioSqlDao.getBigDecimal("HowManyRecords");
			lTipologiaOrarioSqlDao.stop();
		} catch (DAOException daoEx) {
			throw new F3BException(
					"TipologiaOrarioController.ExGetCountTipologiaOrario: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lTipologiaOrarioSqlDao);
			cleanup(lConn);
		}

		return lCount;
	}

	/*****************************************************************************
	 * Funzione di ricerca utilizzata per la paginazione che restituisce i risultati da visualizzare nella
	 * pagina specificata in input
	 * 
	 * @param aTipologiaOrario
	 *            model contenete i parametri della ricerca
	 * @param aPage
	 *            pagina per la quale si vogliono ottenere i risultati
	 * @return Vettore di model con i record da visualizzare nella pagina specificata in input
	 * @throws F3BException
	 ****************************************************************************/
	public Vector ExRicercaTipologiaOrarioPaged(TipologiaOrarioModel aTipologiaOrario, int aPage)
			throws F3BException {
		Connection lConn = null;
		Vector lTipologiaOrarii = new Vector();
		TipologiaOrarioSqlDAO lTipologiaOrarioSqlDao = null;

		try {
			lConn = getDBConnection();
			lTipologiaOrarioSqlDao = new TipologiaOrarioSqlDAO(lConn);
			lTipologiaOrarioSqlDao.ricercaTipologiaOrarioPaged(aTipologiaOrario, aPage);
			lTipologiaOrarii = new Vector(lTipologiaOrarioSqlDao.getModels());
		} catch (DAOException daoEx) {
			throw new F3BException(
					"TipologiaOrarioController.ExRicercaTipologiaOrarioPaged: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lTipologiaOrarioSqlDao);
			cleanup(lConn);
		}
		return lTipologiaOrarii;
	}

	// MEV26 CUMULO
	public Vector ExRicercaTipologiaOrarioByIdBeneficioCumulo(BigDecimal aIdBeneficioCumulo)
			throws F3BException {
		Connection lConn = null;
		Vector lTipologiaOrarioMod = null;
		TipologiaOrarioSqlDAO lTipologiaOrarioSqlDao = null;

		try {
			lConn = getDBConnection();
			lTipologiaOrarioSqlDao = new TipologiaOrarioSqlDAO(lConn);
			lTipologiaOrarioSqlDao.ricercaTipologiaOrarioByIdBeneficioCumulo(aIdBeneficioCumulo);
			lTipologiaOrarioMod = new Vector(lTipologiaOrarioSqlDao.getModels());
		} catch (DAOException daoEx) {
			throw new F3BException(
					"TipologiaOrarioController.ExRicercaTipologiaOrarioByIdBeneficio: Non posso leggere : "
							+ daoEx);
		} finally {
			cleanup(lTipologiaOrarioSqlDao);
			cleanup(lConn);
		}

		return lTipologiaOrarioMod;
	}

}