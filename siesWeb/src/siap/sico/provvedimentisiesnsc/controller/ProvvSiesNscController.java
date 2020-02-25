package siap.sico.provvedimentisiesnsc.controller;

import java.sql.Connection;
import java.util.Vector;

import siap.controller.SiapController;
import siap.sico.provvedimentisiesnsc.dao.ProvvSiesNscDAO;
import siap.sico.provvedimentisiesnsc.dao.ProvvSiesNscSqlDAO;
import siap.sico.provvedimentisiesnsc.model.ProvvSiesNscModel;
import f3b.dao.DAOException;
import f3b.util.F3BException;

/**
 * <p>
 * Title: ProvvSiesNscController
 * </p>
 * <p>
 * Description: Classe Controller per ProvvSiesNsc
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
public class ProvvSiesNscController extends SiapController implements IProvvSiesNsc {

	/*****************************************************************************
	 * Effettua l'inserimento di un ProvvSiesNsc a partire dai dati contenuti nel Model
	 * 
	 * @param aProvvSiesNsc
	 *            Model con i dati da inserire
	 * @return il model con i dati inseriti e l'aggiunta dell'id del record inserito
	 * @throws F3BException
	 ****************************************************************************/
	/*
	 * public ProvvSiesNscModel ExInserisciProvvSiesNsc (ProvvSiesNscModel aProvvSiesNsc ) throws F3BException
	 * { Connection lConn = null; ProvvSiesNscDAO lProDao = null; ProvvSiesNscModel lProMod = null;
	 * 
	 * try { lConn = getDBConnection(); lProDao = new ProvvSiesNscDAO(lConn);
	 * lProDao.setDAOFromModel(aProvvSiesNsc ); BigDecimal lSequence =lProDao.insert(); commit(lConn); lProMod
	 * = new ProvvSiesNscModel(aProvvSiesNsc); lProMod.setMessage("Inserimento avvenuto correttamente!");
	 * lProMod.setProvvCodcentr(lSequence); } catch (DAOException ex) { rollback(lConn); throw new
	 * F3BException("ProvvSiesNscController.ExInserisci: Non posso inserire: " + ex); } finally {
	 * cleanup(lProDao); cleanup(lConn); }
	 * 
	 * return lProMod; }
	 */

	/*****************************************************************************
	 * Effettua la ricerca dei dati ProvvSiesNsc
	 * 
	 * @param aProvvSiesNsc
	 *            Model utilizzato per costruire le condizioni di ricerca Ogni valore attualizzato nel model
	 *            verrà utilizzato per imporre una condizione di ricerca
	 * @return un vettore di model con il risultato della ricerca
	 * @throws F3BException
	 ****************************************************************************/
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Vector ExRicercaProvvSiesNsc(ProvvSiesNscModel aProvvSiesNsc) throws F3BException {
		Connection lConn = null;
		Vector lProvvSiesNsi = new Vector();
		ProvvSiesNscDAO lProDao = null;

		try {
			lConn = getDBConnection();
			lProDao = new ProvvSiesNscDAO(lConn);
			lProDao.setCondizioni(aProvvSiesNsc);
			lProDao.setOrderBy();
			lProDao.start();
			while (lProDao.next()) {
				lProvvSiesNsi.add((ProvvSiesNscModel) lProDao.getModel());
			}
			lProDao.stop();
		} catch (DAOException daoEx) {
			throw new F3BException("ProvvSiesNscController.ExRicercaProvvSiesNsc: Non posso leggere : "
					+ daoEx);
		} finally {
			cleanup(lProDao);
			cleanup(lConn);
		}

		return lProvvSiesNsi;
	}

	/*****************************************************************************
	 * Effettua la ricerca per chiave
	 * 
	 * @param akey
	 *            valore della chiave del record da ricercare
	 * @return il model con i dati trovati
	 * @throws F3BException
	 ****************************************************************************/
	public ProvvSiesNscModel ExRicercaProvvSiesNscById(String aProvvDomain, String aProvvCodcentr)
			throws F3BException {
		Connection lConn = null;
		ProvvSiesNscModel lProvvSiesNscMod = new ProvvSiesNscModel();
		ProvvSiesNscSqlDAO lProvvSiesNscSqlDao = null;

		try {
			lConn = getDBConnection();
			lProvvSiesNscSqlDao = new ProvvSiesNscSqlDAO(lConn);
			lProvvSiesNscSqlDao.ricercaProvvSiesNscByKey(aProvvDomain, aProvvCodcentr);
			lProvvSiesNscMod = (ProvvSiesNscModel) lProvvSiesNscSqlDao.getModelByKey();
		} catch (DAOException daoEx) {
			throw new F3BException("ProvvSiesNscController.ExRicercaProvvSiesNscById: Non posso leggere : "
					+ daoEx);
		} finally {
			cleanup(lProvvSiesNscSqlDao);
			cleanup(lConn);
		}

		return lProvvSiesNscMod;
	}

	/*****************************************************************************
	 * Metodo che modifica i dati dell'ProvvSiesNsc Viene fatto l'update di tutti i campi del record
	 * recuperando i valori dal Model Se mancano dati nel model i corrispondenti valori della tabella verranno
	 * impostati a null
	 * 
	 * @param aProvvSiesNsc
	 *            Model con i nuovi valori
	 * @throws F3BException
	 ****************************************************************************/
	/*
	 * public void ExModificaProvvSiesNsc (ProvvSiesNscModel aProvvSiesNsc ) throws F3BException { Connection
	 * lConn = null; ProvvSiesNscDAO lProDao = null;
	 * 
	 * try { lConn = getDBConnection(); lProDao = new ProvvSiesNscDAO(lConn);
	 * lProDao.setDAOFromModel(aProvvSiesNsc ); lProDao.selCondizioneUpdate(
	 * aProvvSiesNsc.getProvvCodcentr()); lProDao.update(); commit(lConn); } catch (DAOException ex) {
	 * rollback(lConn); throw new F3BException("ProvvSiesNscController.ExModifica: Non posso inserire: " +
	 * ex); } finally { cleanup(lProDao); cleanup(lConn); } }
	 */

	/*****************************************************************************
	 * Effettua la cancellazione del record
	 * 
	 * @param aProvvSiesNsc
	 * @throws F3BException
	 ****************************************************************************/

	/*
	 * public void ExCancellaProvvSiesNsc (ProvvSiesNscModel aProvvSiesNsc ) throws F3BException { Connection
	 * lConn = null; ProvvSiesNscDAO lProDao = null;
	 * 
	 * try { lConn = getDBConnection(); lProDao = new ProvvSiesNscDAO(lConn); lProDao.selCondizioneUpdate(
	 * aProvvSiesNsc.getProvvCodcentr()); lProDao.delete(); commit(lConn); } catch (DAOException daoEx) {
	 * rollback(lConn); throw new
	 * F3BException("ProvvSiesNscController.ExCancellaProvvSiesNsc: Non posso leggere : " + daoEx); } finally
	 * { cleanup(lProDao); cleanup(lConn); } }
	 */

	/*****************************************************************************
	 * Recupera il numero di record restituiti della ricerca. Utile in caso di ricerche paginate per ottenere
	 * il numero totale di record
	 * 
	 * @param aProvvSiesNsc
	 * @return numero di record trovati dalla funzione dei ricerca
	 * @throws F3BException
	 ****************************************************************************/
	/*
	 * public BigDecimal ExGetCountProvvSiesNsc (ProvvSiesNscModel aProvvSiesNsc ) throws F3BException {
	 * Connection lConn = null; BigDecimal lCount=new BigDecimal(0); ProvvSiesNscSqlDAO lProvvSiesNscSqlDao =
	 * null;
	 * 
	 * try { lConn = getDBConnection(); lProvvSiesNscSqlDao = new ProvvSiesNscSqlDAO(lConn);
	 * lProvvSiesNscSqlDao.getCountProvvSiesNsc(aProvvSiesNsc); lProvvSiesNscSqlDao.start();
	 * lProvvSiesNscSqlDao.next(); lCount=lProvvSiesNscSqlDao.getBigDecimal("HowManyRecords");
	 * lProvvSiesNscSqlDao.stop(); } catch (DAOException daoEx) { throw new
	 * F3BException("ProvvSiesNscController.ExGetCountProvvSiesNsc: Non posso leggere : " + daoEx); } finally
	 * { cleanup(lProvvSiesNscSqlDao); cleanup(lConn); }
	 * 
	 * return lCount; }
	 */

	/*****************************************************************************
	 * Funzione di ricerca utilizzata per la paginazione che restituisce i risultati da visualizzare nella
	 * pagina specificata in input
	 * 
	 * @param aProvvSiesNsc
	 *            model contenete i parametri della ricerca
	 * @param aPage
	 *            pagina per la quale si vogliono ottenere i risultati
	 * @return Vettore di model con i record da visualizzare nella pagina specificata in input
	 * @throws F3BException
	 ****************************************************************************/
	/*
	 * public Vector ExRicercaProvvSiesNscPaged (ProvvSiesNscModel aProvvSiesNsc,int aPage ) throws
	 * F3BException { Connection lConn = null; Vector lProvvSiesNsi = new Vector(); ProvvSiesNscSqlDAO
	 * lProvvSiesNscSqlDao = null;
	 * 
	 * try { lConn = getDBConnection(); lProvvSiesNscSqlDao = new ProvvSiesNscSqlDAO(lConn);
	 * lProvvSiesNscSqlDao.ricercaProvvSiesNscPaged(aProvvSiesNsc, aPage ); lProvvSiesNsi = new
	 * Vector(lProvvSiesNscSqlDao.getModels()); } catch (DAOException daoEx) { throw new
	 * F3BException("ProvvSiesNscController.ExRicercaProvvSiesNscPaged: Non posso leggere : " + daoEx); }
	 * finally { cleanup(lProvvSiesNscSqlDao); cleanup(lConn); } return lProvvSiesNsi; }
	 */

}