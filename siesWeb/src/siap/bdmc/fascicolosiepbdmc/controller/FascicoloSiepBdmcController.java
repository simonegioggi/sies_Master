package siap.bdmc.fascicolosiepbdmc.controller;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Vector;

import f3b.dao.DAOException;
import f3b.util.F3BException;
import siap.bdmc.fascicolosiepbdmc.dao.FascicoloSiepBdmcDAO;
import siap.bdmc.fascicolosiepbdmc.dao.FascicoloSiepBdmcSqlDAO;
import siap.bdmc.fascicolosiepbdmc.model.FascicoloSiepBdmcModel;
import siap.controller.SiapController;

/**
 * <p>
 * Title: FascicoloSiepBdmcController
 * </p>
 * <p>
 * Description: Classe Controller per FascicoloSiepBdmc
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
public class FascicoloSiepBdmcController extends SiapController implements IFascicoloSiepBdmc {

	/*****************************************************************************
	 * Effettua l'inserimento di un FascicoloSiepBdmc a partire dai dati contenuti nel Model
	 *
	 * @param aFascicoloSiepBdmc
	 *            Model con i dati da inserire
	 * @return il model con i dati inseriti e l'aggiunta dell'id del record inserito
	 * @throws F3BException
	 ****************************************************************************/
	public FascicoloSiepBdmcModel ExInserisciFascicoloSiepBdmc(FascicoloSiepBdmcModel aFascicoloSiepBdmc)
			throws F3BException {

		Connection lConn = null;
		FascicoloSiepBdmcDAO lFasDao = null;
		FascicoloSiepBdmcModel lFasMod = null;

		try {
			lConn = getDBConnection();
			lFasDao = new FascicoloSiepBdmcDAO(lConn);
			lFasDao.setDAOFromModel(aFascicoloSiepBdmc);
			BigDecimal lSequence = lFasDao.insert();
			commit(lConn);
			lFasMod = new FascicoloSiepBdmcModel(aFascicoloSiepBdmc);
			lFasMod.setMessage("Inserimento avvenuto correttamente!");
			lFasMod.setIdFascicoloBdmc(lSequence);
		} catch (DAOException ex) {
			rollback(lConn);
			throw new F3BException("FascicoloSiepBdmcController.ExInserisci: Non posso inserire: " + ex);
		} finally {
			cleanup(lFasDao);
			cleanup(lConn);
		}

		return lFasMod;
	}

	/*****************************************************************************
	 * Effettua la ricerca dei dati FascicoloSiepBdmc
	 *
	 * @param aFascicoloSiepBdmc
	 *            Model utilizzato per costruire le condizioni di ricerca Ogni valore attualizzato nel model
	 *            verrà utilizzato per imporre una condizione di ricerca
	 * @return un vettore di model con il risultato della ricerca
	 * @throws F3BException
	 ****************************************************************************/
	public Vector ExRicercaFascicoloSiepBdmc(FascicoloSiepBdmcModel aFascicoloSiepBdmc) throws F3BException {

		Connection lConn = null;
		Vector lFascicoloSiepBdmi = new Vector();
		FascicoloSiepBdmcDAO lFasDao = null;

		try {
			lConn = getDBConnection();
			lFasDao = new FascicoloSiepBdmcDAO(lConn);
			lFasDao.setCondizioni(aFascicoloSiepBdmc);
			lFasDao.setOrderBy();
			lFasDao.start();
			while (lFasDao.next()) {
				lFascicoloSiepBdmi.add(lFasDao.getModel());
			}
			lFasDao.stop();
		} catch (DAOException daoEx) {
			throw new F3BException(
					"FascicoloSiepBdmcController.ExRicercaFascicoloSiepBdmc: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lFasDao);
			cleanup(lConn);
		}

		return lFascicoloSiepBdmi;
	}

	/*****************************************************************************
	 * Effettua la ricerca per chiave
	 *
	 * @param akey
	 *            valore della chiave del record da ricercare
	 * @return il model con i dati trovati
	 * @throws F3BException
	 ****************************************************************************/
	public FascicoloSiepBdmcModel ExRicercaFascicoloSiepBdmcById(BigDecimal aIdFascicoloBdmc)
			throws F3BException {

		Connection lConn = null;
		FascicoloSiepBdmcModel lFascicoloSiepBdmcMod = new FascicoloSiepBdmcModel();
		FascicoloSiepBdmcSqlDAO lFascicoloSiepBdmcSqlDao = null;

		try {
			lConn = getDBConnection();
			lFascicoloSiepBdmcSqlDao = new FascicoloSiepBdmcSqlDAO(lConn);
			lFascicoloSiepBdmcSqlDao.ricercaFascicoloSiepBdmcByKey(aIdFascicoloBdmc);
			lFascicoloSiepBdmcMod = (FascicoloSiepBdmcModel) lFascicoloSiepBdmcSqlDao.getModelByKey();
		} catch (DAOException daoEx) {
			throw new F3BException(
					"FascicoloSiepBdmcController.ExRicercaFascicoloSiepBdmcById: Non posso leggere : "
							+ daoEx);
		} finally {
			cleanup(lFascicoloSiepBdmcSqlDao);
			cleanup(lConn);
		}

		return lFascicoloSiepBdmcMod;
	}

	/*****************************************************************************
	 * Metodo che modifica i dati dell'FascicoloSiepBdmc Viene fatto l'update di tutti i campi del record
	 * recuperando i valori dal Model Se mancano dati nel model i corrispondenti valori della tabella verranno
	 * impostati a null
	 *
	 * @param aFascicoloSiepBdmc
	 *            Model con i nuovi valori
	 * @throws F3BException
	 ****************************************************************************/
	public void ExModificaFascicoloSiepBdmc(FascicoloSiepBdmcModel aFascicoloSiepBdmc) throws F3BException {

		Connection lConn = null;
		FascicoloSiepBdmcDAO lFasDao = null;

		try {
			lConn = getDBConnection();
			lFasDao = new FascicoloSiepBdmcDAO(lConn);
			lFasDao.setDAOFromModel(aFascicoloSiepBdmc);
			lFasDao.selCondizioneUpdate(aFascicoloSiepBdmc.getIdFascicoloBdmc());
			lFasDao.update();
			commit(lConn);
		} catch (DAOException ex) {
			rollback(lConn);
			throw new F3BException("FascicoloSiepBdmcController.ExModifica: Non posso inserire: " + ex);
		} finally {
			cleanup(lFasDao);
			cleanup(lConn);
		}
	}

	/*****************************************************************************
	 * Effettua la cancellazione del record
	 *
	 * @param aFascicoloSiepBdmc
	 * @throws F3BException
	 ****************************************************************************/
	public void ExCancellaFascicoloSiepBdmc(FascicoloSiepBdmcModel aFascicoloSiepBdmc) throws F3BException {

		Connection lConn = null;
		FascicoloSiepBdmcDAO lFasDao = null;

		try {
			lConn = getDBConnection();
			lFasDao = new FascicoloSiepBdmcDAO(lConn);
			lFasDao.selCondizioneUpdate(aFascicoloSiepBdmc.getIdFascicoloBdmc());
			lFasDao.delete();
			commit(lConn);
		} catch (DAOException daoEx) {
			rollback(lConn);
			throw new F3BException(
					"FascicoloSiepBdmcController.ExCancellaFascicoloSiepBdmc: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lFasDao);
			cleanup(lConn);
		}
	}

	/*****************************************************************************
	 * Recupera il numero di record restituiti della ricerca. Utile in caso di ricerche paginate per ottenere
	 * il numero totale di record
	 *
	 * @param aFascicoloSiepBdmc
	 * @return numero di record trovati dalla funzione dei ricerca
	 * @throws F3BException
	 ****************************************************************************/
	public BigDecimal ExGetCountFascicoloSiepBdmc(FascicoloSiepBdmcModel aFascicoloSiepBdmc)
			throws F3BException {

		Connection lConn = null;
		BigDecimal lCount = new BigDecimal(0);
		FascicoloSiepBdmcSqlDAO lFascicoloSiepBdmcSqlDao = null;

		try {
			lConn = getDBConnection();
			lFascicoloSiepBdmcSqlDao = new FascicoloSiepBdmcSqlDAO(lConn);
			lFascicoloSiepBdmcSqlDao.getCountFascicoloSiepBdmc(aFascicoloSiepBdmc);
			lFascicoloSiepBdmcSqlDao.start();
			lFascicoloSiepBdmcSqlDao.next();
			lCount = lFascicoloSiepBdmcSqlDao.getBigDecimal("HowManyRecords");
			lFascicoloSiepBdmcSqlDao.stop();
		} catch (DAOException daoEx) {
			throw new F3BException(
					"FascicoloSiepBdmcController.ExGetCountFascicoloSiepBdmc: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lFascicoloSiepBdmcSqlDao);
			cleanup(lConn);
		}

		return lCount;
	}

	/*****************************************************************************
	 * Funzione di ricerca utilizzata per la paginazione che restituisce i risultati da visualizzare nella
	 * pagina specificata in input
	 *
	 * @param aFascicoloSiepBdmc
	 *            model contenete i parametri della ricerca
	 * @param aPage
	 *            pagina per la quale si vogliono ottenere i risultati
	 * @return Vettore di model con i record da visualizzare nella pagina specificata in input
	 * @throws F3BException
	 ****************************************************************************/
	public Vector ExRicercaFascicoloSiepBdmcPaged(FascicoloSiepBdmcModel aFascicoloSiepBdmc, int aPage)
			throws F3BException {

		Connection lConn = null;
		Vector lFascicoloSiepBdmi = new Vector();
		FascicoloSiepBdmcSqlDAO lFascicoloSiepBdmcSqlDao = null;

		try {
			lConn = getDBConnection();
			lFascicoloSiepBdmcSqlDao = new FascicoloSiepBdmcSqlDAO(lConn);
			lFascicoloSiepBdmcSqlDao.ricercaFascicoloSiepBdmcPaged(aFascicoloSiepBdmc, aPage);
			lFascicoloSiepBdmi = new Vector(lFascicoloSiepBdmcSqlDao.getModels());
		} catch (DAOException daoEx) {
			throw new F3BException(
					"FascicoloSiepBdmcController.ExRicercaFascicoloSiepBdmcPaged: Non posso leggere : "
							+ daoEx);
		} finally {
			cleanup(lFascicoloSiepBdmcSqlDao);
			cleanup(lConn);
		}
		return lFascicoloSiepBdmi;
	}

}