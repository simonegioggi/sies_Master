package siap.sico.codici_sies_nsc.controller;

import java.sql.Connection;
import java.util.Vector;

import siap.controller.SiapController;
import siap.sico.codici_sies_nsc.dao.CodiciSiesNscDAO;
import siap.sico.codici_sies_nsc.dao.CodiciSiesNscSqlDAO;
import siap.sico.codici_sies_nsc.model.CodiciSiesNscModel;
import f3b.dao.DAOException;
import f3b.util.F3BException;

/**
 * <p>
 * Title: CodiciSiesNscController
 * </p>
 * <p>
 * Description: Classe Controller per CodiciSiesNsc
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
public class CodiciSiesNscController extends SiapController implements ICodiciSiesNsc {

	/*****************************************************************************
	 * Effettua l'inserimento di un CodiciSiesNsc a partire dai dati contenuti nel Model
	 * 
	 * @param aCodiciSiesNsc
	 *            Model con i dati da inserire
	 * @return il model con i dati inseriti e l'aggiunta dell'id del record inserito
	 * @throws F3BException
	 ****************************************************************************/
	/*
	 * public CodiciSiesNscModel ExInserisciCodiciSiesNsc (CodiciSiesNscModel aCodiciSiesNsc ) throws
	 * F3BException { Connection lConn = null; CodiciSiesNscDAO lCodDao = null; CodiciSiesNscModel lCodMod =
	 * null;
	 * 
	 * try { lConn = getDBConnection(); lCodDao = new CodiciSiesNscDAO(lConn);
	 * lCodDao.setDAOFromModel(aCodiciSiesNsc ); lCodDao.insert(); commit(lConn); lCodMod = new
	 * CodiciSiesNscModel(aCodiciSiesNsc); lCodMod.setMessage("Inserimento avvenuto correttamente!"); } catch
	 * (DAOException ex) { rollback(lConn); throw new
	 * F3BException("CodiciSiesNscController.ExInserisci: Non posso inserire: " + ex); } finally {
	 * cleanup(lCodDao); cleanup(lConn); }
	 * 
	 * return lCodMod; }
	 * 
	 * 
	 * 
	 * /***************************************************************************** Effettua la ricerca dei
	 * dati CodiciSiesNsc
	 * 
	 * @param aCodiciSiesNsc Model utilizzato per costruire le condizioni di ricerca Ogni valore attualizzato
	 * nel model verrà utilizzato per imporre una condizione di ricerca
	 * 
	 * @return un vettore di model con il risultato della ricerca
	 * 
	 * @throws F3BException**************************************************************************
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Vector ExRicercaCodiciSiesNsc(CodiciSiesNscModel aCodiciSiesNsc) throws F3BException {
		Connection lConn = null;
		Vector lCodiciSiesNsi = new Vector();
		CodiciSiesNscDAO lCodDao = null;

		try {
			lConn = getDBConnection();
			lCodDao = new CodiciSiesNscDAO(lConn);
			lCodDao.setCondizioni(aCodiciSiesNsc);
			lCodDao.setOrderBy();
			lCodDao.start();
			while (lCodDao.next()) {
				lCodiciSiesNsi.add((CodiciSiesNscModel) lCodDao.getModel());
			}
			lCodDao.stop();
		} catch (DAOException daoEx) {
			throw new F3BException("CodiciSiesNscController.ExRicercaCodiciSiesNsc: Non posso leggere : "
					+ daoEx);
		} finally {
			cleanup(lCodDao);
			cleanup(lConn);
		}

		return lCodiciSiesNsi;
	}

	/*****************************************************************************
	 * Effettua la ricerca per chiave
	 * 
	 * @param akey
	 *            valore della chiave del record da ricercare
	 * @return il model con i dati trovati
	 * @throws F3BException
	 ****************************************************************************/
	public CodiciSiesNscModel ExRicercaCodiciSiesNscById(String aCoDomain, String aCoCodcentr)
			throws F3BException {
		Connection lConn = null;
		CodiciSiesNscModel lCodiciSiesNscMod = new CodiciSiesNscModel();
		CodiciSiesNscSqlDAO lCodiciSiesNscSqlDao = null;

		try {
			lConn = getDBConnection();
			lCodiciSiesNscSqlDao = new CodiciSiesNscSqlDAO(lConn);
			lCodiciSiesNscSqlDao.ricercaCodiciSiesNscByKey(aCoDomain, aCoCodcentr);
			lCodiciSiesNscMod = (CodiciSiesNscModel) lCodiciSiesNscSqlDao.getModelByKey();
		} catch (DAOException daoEx) {
			throw new F3BException("CodiciSiesNscController.ExRicercaCodiciSiesNscById: Non posso leggere : "
					+ daoEx);
		} finally {
			cleanup(lCodiciSiesNscSqlDao);
			cleanup(lConn);
		}

		return lCodiciSiesNscMod;
	}

	/*
	 * /***************************************************************************** Metodo che modifica i
	 * dati dell'CodiciSiesNsc Viene fatto l'update di tutti i campi del record recuperando i valori dal Model
	 * Se mancano dati nel model i corrispondenti valori della tabella verranno impostati a null
	 * 
	 * @param aCodiciSiesNsc Model con i nuovi valori
	 * 
	 * @throws F3BException**************************************************************************
	 */
	/*
	 * public void ExModificaCodiciSiesNsc (CodiciSiesNscModel aCodiciSiesNsc ) throws F3BException {
	 * Connection lConn = null; CodiciSiesNscDAO lCodDao = null;
	 * 
	 * try { lConn = getDBConnection(); lCodDao = new CodiciSiesNscDAO(lConn);
	 * lCodDao.setDAOFromModel(aCodiciSiesNsc ); lCodDao.selCondizioneUpdate( aCodiciSiesNsc.getCoCodcentr());
	 * lCodDao.update(); commit(lConn); } catch (DAOException ex) { rollback(lConn); throw new
	 * F3BException("CodiciSiesNscController.ExModifica: Non posso inserire: " + ex); } finally {
	 * cleanup(lCodDao); cleanup(lConn); } }
	 * 
	 * 
	 * 
	 * /***************************************************************************** Effettua la
	 * cancellazione del record
	 * 
	 * @param aCodiciSiesNsc
	 * 
	 * @throws F3BException**************************************************************************
	 */
	/*
	 * public void ExCancellaCodiciSiesNsc (CodiciSiesNscModel aCodiciSiesNsc ) throws F3BException {
	 * Connection lConn = null; CodiciSiesNscDAO lCodDao = null;
	 * 
	 * try { lConn = getDBConnection(); lCodDao = new CodiciSiesNscDAO(lConn); lCodDao.selCondizioneUpdate(
	 * aCodiciSiesNsc.getCoCodcentr()); lCodDao.delete(); commit(lConn); } catch (DAOException daoEx) {
	 * rollback(lConn); throw new
	 * F3BException("CodiciSiesNscController.ExCancellaCodiciSiesNsc: Non posso leggere : " + daoEx); }
	 * finally { cleanup(lCodDao); cleanup(lConn); } }
	 * 
	 * 
	 * 
	 * /***************************************************************************** Recupera il numero di
	 * record restituiti della ricerca. Utile in caso di ricerche paginate per ottenere il numero totale di
	 * record
	 * 
	 * @param aCodiciSiesNsc
	 * 
	 * @return numero di record trovati dalla funzione dei ricerca
	 * 
	 * @throws F3BException**************************************************************************
	 */
	/*
	 * public BigDecimal ExGetCountCodiciSiesNsc (CodiciSiesNscModel aCodiciSiesNsc ) throws F3BException {
	 * Connection lConn = null; BigDecimal lCount=new BigDecimal(0); CodiciSiesNscSqlDAO lCodiciSiesNscSqlDao
	 * = null;
	 * 
	 * try { lConn = getDBConnection(); lCodiciSiesNscSqlDao = new CodiciSiesNscSqlDAO(lConn);
	 * lCodiciSiesNscSqlDao.getCountCodiciSiesNsc(aCodiciSiesNsc); lCodiciSiesNscSqlDao.start();
	 * lCodiciSiesNscSqlDao.next(); lCount=lCodiciSiesNscSqlDao.getBigDecimal("HowManyRecords");
	 * lCodiciSiesNscSqlDao.stop(); } catch (DAOException daoEx) { throw new
	 * F3BException("CodiciSiesNscController.ExGetCountCodiciSiesNsc: Non posso leggere : " + daoEx); }
	 * finally { cleanup(lCodiciSiesNscSqlDao); cleanup(lConn); }
	 * 
	 * return lCount; }
	 * 
	 * 
	 * 
	 * /***************************************************************************** Funzione di ricerca
	 * utilizzata per la paginazione che restituisce i risultati da visualizzare nella pagina specificata in
	 * input
	 * 
	 * @param aCodiciSiesNsc model contenete i parametri della ricerca
	 * 
	 * @param aPage pagina per la quale si vogliono ottenere i risultati
	 * 
	 * @return Vettore di model con i record da visualizzare nella pagina specificata in input
	 * 
	 * @throws F3BException**************************************************************************
	 */
	/*
	 * public Vector ExRicercaCodiciSiesNscPaged (CodiciSiesNscModel aCodiciSiesNsc,int aPage ) throws
	 * F3BException { Connection lConn = null; Vector lCodiciSiesNsi = new Vector(); CodiciSiesNscSqlDAO
	 * lCodiciSiesNscSqlDao = null;
	 * 
	 * try { lConn = getDBConnection(); lCodiciSiesNscSqlDao = new CodiciSiesNscSqlDAO(lConn);
	 * lCodiciSiesNscSqlDao.ricercaCodiciSiesNscPaged(aCodiciSiesNsc, aPage ); lCodiciSiesNsi = new
	 * Vector(lCodiciSiesNscSqlDao.getModels()); } catch (DAOException daoEx) { throw new
	 * F3BException("CodiciSiesNscController.ExRicercaCodiciSiesNscPaged: Non posso leggere : " + daoEx); }
	 * finally { cleanup(lCodiciSiesNscSqlDao); cleanup(lConn); } return lCodiciSiesNsi; }
	 */

}