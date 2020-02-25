package siap.bdmc.statoprenotazionibdmc.controller;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Vector;

import siap.bdmc.statoprenotazionibdmc.dao.StatoPrenotazioniBdmcDAO;
import siap.bdmc.statoprenotazionibdmc.dao.StatoPrenotazioniBdmcSqlDAO;
import siap.bdmc.statoprenotazionibdmc.model.StatoPrenotazioniBdmcModel;
import siap.controller.SiapController;
import f3b.dao.DAOException;
import f3b.util.F3BException;

/**
 * <p>
 * Title: StatoPrenotazioniBdmcController
 * </p>
 * <p>
 * Description: Classe Controller per StatoPrenotazioniBdmc
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
public class StatoPrenotazioniBdmcController extends SiapController implements IStatoPrenotazioniBdmc {

	/*****************************************************************************
	 * Effettua l'inserimento di un StatoPrenotazioniBdmc a partire dai dati contenuti nel Model
	 * 
	 * @param aStatoPrenotazioniBdmc
	 *            Model con i dati da inserire
	 * @return il model con i dati inseriti e l'aggiunta dell'id del record inserito
	 * @throws F3BException
	 ****************************************************************************/
	public StatoPrenotazioniBdmcModel ExInserisciStatoPrenotazioniBdmc(
			StatoPrenotazioniBdmcModel aStatoPrenotazioniBdmc) throws F3BException {
		Connection lConn = null;
		StatoPrenotazioniBdmcDAO lStaDao = null;
		StatoPrenotazioniBdmcModel lStaMod = null;

		try {
			lConn = getDBConnection();
			lStaDao = new StatoPrenotazioniBdmcDAO(lConn);
			lStaDao.setDAOFromModel(aStatoPrenotazioniBdmc);
			BigDecimal lSequence = lStaDao.insert();
			commit(lConn);
			lStaMod = new StatoPrenotazioniBdmcModel(aStatoPrenotazioniBdmc);
			lStaMod.setMessage("Inserimento avvenuto correttamente!");
			lStaMod.setStatoPrenotazioniBdmc(lSequence);
		} catch (DAOException ex) {
			rollback(lConn);
			throw new F3BException("StatoPrenotazioniBdmcController.ExInserisci: Non posso inserire: " + ex);
		} finally {
			cleanup(lStaDao);
			cleanup(lConn);
		}

		return lStaMod;
	}

	/*****************************************************************************
	 * Effettua la ricerca dei dati StatoPrenotazioniBdmc
	 * 
	 * @param aStatoPrenotazioniBdmc
	 *            Model utilizzato per costruire le condizioni di ricerca Ogni valore attualizzato nel model
	 *            verrà utilizzato per imporre una condizione di ricerca
	 * @return un vettore di model con il risultato della ricerca
	 * @throws F3BException
	 ****************************************************************************/
	public Vector ExRicercaStatoPrenotazioniBdmc(StatoPrenotazioniBdmcModel aStatoPrenotazioniBdmc)
			throws F3BException {
		Connection lConn = null;
		Vector lStatoPrenotazioniBdmi = new Vector();
		StatoPrenotazioniBdmcDAO lStaDao = null;

		try {
			lConn = getDBConnection();
			lStaDao = new StatoPrenotazioniBdmcDAO(lConn);
			lStaDao.setCondizioni(aStatoPrenotazioniBdmc);
			lStaDao.setOrderBy();
			lStaDao.start();
			while (lStaDao.next()) {
				lStatoPrenotazioniBdmi.add((StatoPrenotazioniBdmcModel) lStaDao.getModel());
			}
			lStaDao.stop();
		} catch (DAOException daoEx) {
			throw new F3BException(
					"StatoPrenotazioniBdmcController.ExRicercaStatoPrenotazioniBdmc: Non posso leggere : "
							+ daoEx);
		} finally {
			cleanup(lStaDao);
			cleanup(lConn);
		}

		return lStatoPrenotazioniBdmi;
	}

	/*****************************************************************************
	 * Effettua la ricerca per chiave
	 * 
	 * @param akey
	 *            valore della chiave del record da ricercare
	 * @return il model con i dati trovati
	 * @throws F3BException
	 ****************************************************************************/
	public StatoPrenotazioniBdmcModel ExRicercaStatoPrenotazioniBdmcById(BigDecimal aStatoPrenotazioniBdmc)
			throws F3BException {
		Connection lConn = null;
		StatoPrenotazioniBdmcModel lStatoPrenotazioniBdmcMod = new StatoPrenotazioniBdmcModel();
		StatoPrenotazioniBdmcSqlDAO lStatoPrenotazioniBdmcSqlDao = null;

		try {
			lConn = getDBConnection();
			lStatoPrenotazioniBdmcSqlDao = new StatoPrenotazioniBdmcSqlDAO(lConn);
			lStatoPrenotazioniBdmcSqlDao.ricercaStatoPrenotazioniBdmcByKey(aStatoPrenotazioniBdmc);
			lStatoPrenotazioniBdmcMod = (StatoPrenotazioniBdmcModel) lStatoPrenotazioniBdmcSqlDao
					.getModelByKey();
		} catch (DAOException daoEx) {
			throw new F3BException(
					"StatoPrenotazioniBdmcController.ExRicercaStatoPrenotazioniBdmcById: Non posso leggere : "
							+ daoEx);
		} finally {
			cleanup(lStatoPrenotazioniBdmcSqlDao);
			cleanup(lConn);
		}

		return lStatoPrenotazioniBdmcMod;
	}

	/*****************************************************************************
	 * Metodo che modifica i dati dell'StatoPrenotazioniBdmc Viene fatto l'update di tutti i campi del record
	 * recuperando i valori dal Model Se mancano dati nel model i corrispondenti valori della tabella verranno
	 * impostati a null
	 * 
	 * @param aStatoPrenotazioniBdmc
	 *            Model con i nuovi valori
	 * @throws F3BException
	 ****************************************************************************/
	public void ExModificaStatoPrenotazioniBdmc(StatoPrenotazioniBdmcModel aStatoPrenotazioniBdmc)
			throws F3BException {
		Connection lConn = null;
		StatoPrenotazioniBdmcDAO lStaDao = null;

		try {
			lConn = getDBConnection();
			lStaDao = new StatoPrenotazioniBdmcDAO(lConn);
			lStaDao.setDAOFromModel(aStatoPrenotazioniBdmc);
			lStaDao.selCondizioneUpdate(aStatoPrenotazioniBdmc.getStatoPrenotazioniBdmc());
			lStaDao.update();
			commit(lConn);
		} catch (DAOException ex) {
			rollback(lConn);
			throw new F3BException("StatoPrenotazioniBdmcController.ExModifica: Non posso inserire: " + ex);
		} finally {
			cleanup(lStaDao);
			cleanup(lConn);
		}
	}

	/*****************************************************************************
	 * Effettua la cancellazione del record
	 * 
	 * @param aStatoPrenotazioniBdmc
	 * @throws F3BException
	 ****************************************************************************/
	public void ExCancellaStatoPrenotazioniBdmc(StatoPrenotazioniBdmcModel aStatoPrenotazioniBdmc)
			throws F3BException {
		Connection lConn = null;
		StatoPrenotazioniBdmcDAO lStaDao = null;

		try {
			lConn = getDBConnection();
			lStaDao = new StatoPrenotazioniBdmcDAO(lConn);
			lStaDao.selCondizioneUpdate(aStatoPrenotazioniBdmc.getStatoPrenotazioniBdmc());
			lStaDao.delete();
			commit(lConn);
		} catch (DAOException daoEx) {
			rollback(lConn);
			throw new F3BException(
					"StatoPrenotazioniBdmcController.ExCancellaStatoPrenotazioniBdmc: Non posso leggere : "
							+ daoEx);
		} finally {
			cleanup(lStaDao);
			cleanup(lConn);
		}
	}

	/*****************************************************************************
	 * Recupera il numero di record restituiti della ricerca. Utile in caso di ricerche paginate per ottenere
	 * il numero totale di record
	 * 
	 * @param aStatoPrenotazioniBdmc
	 * @return numero di record trovati dalla funzione dei ricerca
	 * @throws F3BException
	 ****************************************************************************/
	public BigDecimal ExGetCountStatoPrenotazioniBdmc(StatoPrenotazioniBdmcModel aStatoPrenotazioniBdmc)
			throws F3BException {
		Connection lConn = null;
		BigDecimal lCount = new BigDecimal(0);
		StatoPrenotazioniBdmcSqlDAO lStatoPrenotazioniBdmcSqlDao = null;

		try {
			lConn = getDBConnection();
			lStatoPrenotazioniBdmcSqlDao = new StatoPrenotazioniBdmcSqlDAO(lConn);
			lStatoPrenotazioniBdmcSqlDao.getCountStatoPrenotazioniBdmc(aStatoPrenotazioniBdmc);
			lStatoPrenotazioniBdmcSqlDao.start();
			lStatoPrenotazioniBdmcSqlDao.next();
			lCount = lStatoPrenotazioniBdmcSqlDao.getBigDecimal("HowManyRecords");
			lStatoPrenotazioniBdmcSqlDao.stop();
		} catch (DAOException daoEx) {
			throw new F3BException(
					"StatoPrenotazioniBdmcController.ExGetCountStatoPrenotazioniBdmc: Non posso leggere : "
							+ daoEx);
		} finally {
			cleanup(lStatoPrenotazioniBdmcSqlDao);
			cleanup(lConn);
		}

		return lCount;
	}

	/*****************************************************************************
	 * Funzione di ricerca utilizzata per la paginazione che restituisce i risultati da visualizzare nella
	 * pagina specificata in input
	 * 
	 * @param aStatoPrenotazioniBdmc
	 *            model contenete i parametri della ricerca
	 * @param aPage
	 *            pagina per la quale si vogliono ottenere i risultati
	 * @return Vettore di model con i record da visualizzare nella pagina specificata in input
	 * @throws F3BException
	 ****************************************************************************/
	public Vector ExRicercaStatoPrenotazioniBdmcPaged(StatoPrenotazioniBdmcModel aStatoPrenotazioniBdmc,
			int aPage) throws F3BException {
		Connection lConn = null;
		Vector lStatoPrenotazioniBdmi = new Vector();
		StatoPrenotazioniBdmcSqlDAO lStatoPrenotazioniBdmcSqlDao = null;

		try {
			lConn = getDBConnection();
			lStatoPrenotazioniBdmcSqlDao = new StatoPrenotazioniBdmcSqlDAO(lConn);
			lStatoPrenotazioniBdmcSqlDao.ricercaStatoPrenotazioniBdmcPaged(aStatoPrenotazioniBdmc, aPage);
			lStatoPrenotazioniBdmi = new Vector(lStatoPrenotazioniBdmcSqlDao.getModels());
		} catch (DAOException daoEx) {
			throw new F3BException(
					"StatoPrenotazioniBdmcController.ExRicercaStatoPrenotazioniBdmcPaged: Non posso leggere : "
							+ daoEx);
		} finally {
			cleanup(lStatoPrenotazioniBdmcSqlDao);
			cleanup(lConn);
		}
		return lStatoPrenotazioniBdmi;
	}

}