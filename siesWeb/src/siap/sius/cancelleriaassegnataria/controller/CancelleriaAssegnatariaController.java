package siap.sius.cancelleriaassegnataria.controller;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Vector;

import f3b.dao.DAOException;
import f3b.util.F3BException;
import siap.controller.SiapController;
import siap.sius.cancelleriaassegnataria.dao.CancelleriaAssegnatariaDAO;
import siap.sius.cancelleriaassegnataria.dao.CancelleriaAssegnatariaSqlDAO;
import siap.sius.cancelleriaassegnataria.model.CancelleriaAssegnatariaModel;

/**
 * <p>
 * Title: CancelleriaAssegnatariaController
 * </p>
 * <p>
 * Description: Classe Controller per CancelleriaAssegnataria
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
public class CancelleriaAssegnatariaController extends SiapController implements ICancelleriaAssegnataria {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	// private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	/**
	 * Funzione di inserimento di un nuovo record Cancelleria Assegnataria.
	 *
	 * @param aCancelleriaAssegnataria
	 * @return
	 * @throws F3BException
	 */
	public CancelleriaAssegnatariaModel ExInserisciCancelleriaAssegnataria(
			CancelleriaAssegnatariaModel aCancelleriaAssegnataria) throws F3BException {

		Connection lConn = null;
		CancelleriaAssegnatariaDAO lCanDao = null;
		CancelleriaAssegnatariaModel lCanMod = null;

		try {
			lConn = getDBConnection();
			lCanMod = new CancelleriaAssegnatariaModel(aCancelleriaAssegnataria);
			lCanDao = new CancelleriaAssegnatariaDAO(lConn);
			lCanDao.setDAOFromModel(aCancelleriaAssegnataria);
			lCanDao.insert();
			commit(lConn);
		} catch (DAOException dex) {
			rollback(lConn);
			if (dex.UNIQUE_CONSTRAINT_VIOLATED)
				throw new F3BException(F3BException.USER_MESSAGE,
						"Cancelleria Assegnataria già presente in archivio !");
			throw new F3BException(
					"CancelleriaAssegnatariaController.ExInserisci: Non posso inserire: " + dex);
		} catch (Exception ex) {
			rollback(lConn);
			throw new F3BException(
					"CancelleriaAssegnatariaController.ExInserisci: Non posso inserire: " + ex);
		} finally {
			cleanup(lCanDao);
			cleanup(lConn);
		}
		return lCanMod;
	}

	public Vector ExRicercaCancelleriaAssegnataria(CancelleriaAssegnatariaModel aCancelleriaAssegnataria)
			throws F3BException {

		Connection lConn = null;
		Vector lCancelleriaAssegnatariaLista = new Vector();
		CancelleriaAssegnatariaSqlDAO lCanDao = null;

		try {
			lConn = getDBConnection();
			lCanDao = new CancelleriaAssegnatariaSqlDAO(lConn);
			lCanDao.ricercaCancelleriaAssegnataria(aCancelleriaAssegnataria);
			lCancelleriaAssegnatariaLista = new Vector(lCanDao.getModels());
		} catch (Exception sqe) {
			throw new F3BException(
					"CancelleriaAssegnatariaController.ExRicercaCancelleriaAssegnataria: Non posso leggere  : "
							+ sqe);
		} finally {
			cleanup(lCanDao);
			cleanup(lConn);
		}
		return lCancelleriaAssegnatariaLista;
	}

	/**
	 * Ricerca Cancelleria Assegnataria paginata
	 *
	 * @param CancelleriaAssegnatariaModel
	 * @param aPageNum
	 *            : numero pagina > 0
	 * @return Vettore di CancelleriaAssegnatariaModel
	 * @throws F3BException
	 */
	public Vector ExRicercaCancelleriaAssegnatariaPagina(
			CancelleriaAssegnatariaModel aCancelleriaAssegnataria, int aPageNum) throws F3BException {

		Connection lConn = null;
		Vector lCancelleriaAssegnatariaLista = new Vector();
		CancelleriaAssegnatariaSqlDAO lCanDao = null;

		try {
			lConn = getDBConnection();
			lCanDao = new CancelleriaAssegnatariaSqlDAO(lConn);
			lCanDao.ricercaCancelleriaAssegnataria(aCancelleriaAssegnataria);

			// Ricerca paginata
			lCanDao.startPage(aPageNum);
			CancelleriaAssegnatariaModel lPosMatModel = null;

			while (lCanDao.next()) {
				lPosMatModel = (CancelleriaAssegnatariaModel) lCanDao.getModel();
				lCancelleriaAssegnatariaLista.add(lPosMatModel);
			}
			lCanDao.stop();

			if (lCancelleriaAssegnatariaLista.size() == 0) {
				throw new F3BException(F3BException.EX_NOT_FOUND, "Nessun Elemento trovato");
			}
		} catch (F3BException fe) {
			throw fe;
		} catch (Exception sqe) {
			throw new F3BException(
					"CancelleriaAssegnatariaController.ExRicercaCancelleriaAssegnatariaPagina: Non posso leggere  : "
							+ sqe);
		} finally {
			cleanup(lCanDao);
			cleanup(lConn);
		}
		return lCancelleriaAssegnatariaLista;
	}

	/**
	 * Ritorna n.ro di record risultato di una ExRicercaPosizioneMaterialePagina
	 *
	 * @param PosizioneMaterialeModel
	 * @return BigDecimal n.ro di record
	 * @throws F3BException
	 */
	public BigDecimal ExGetNumRicercaCancelleriaAssegnatariaPagina(
			CancelleriaAssegnatariaModel aCancelleriaAssegnataria) throws F3BException {

		Connection lConn = null;
		// Vector lCancelleriaAssegnatariaLista = new Vector();
		CancelleriaAssegnatariaSqlDAO lCanDao = null;
		BigDecimal lCont = new BigDecimal(0);

		try {
			lConn = getDBConnection();
			lCanDao = new CancelleriaAssegnatariaSqlDAO(lConn);
			lCanDao.ricercaCancelleriaAssegnataria(aCancelleriaAssegnataria);
			lCont = lCanDao.getNumRowsSelected();
		} catch (Exception sqe) {
			throw new F3BException(
					"CancelleriaAssegnatariaController.ExGetNumRicercaCancelleriaAssegnatariaPagina: Non posso leggere  : "
							+ sqe);
		} finally {
			cleanup(lCanDao);
			cleanup(lConn);
		}
		return lCont;
	}

	/**
	 * Funzione di modifica di una Cancelleria Assegnataria. Attraverso il parametro
	 * CancelleriaAssegnatariaModel viene passata la nuova descrizione e la chiave
	 * (COD_CANCELLERIA_ASSEGNATARIA, COD_UFFICIO) per individuare il record da aggiornare.
	 *
	 * @param aCancelleriaAssegnataria
	 * @return
	 * @throws F3BException
	 */
	public CancelleriaAssegnatariaModel ExModificaCancelleriaAssegnataria(
			CancelleriaAssegnatariaModel aCancelleriaAssegnataria) throws F3BException {

		Connection lConn = null;
		CancelleriaAssegnatariaDAO lCanDao = null;
		CancelleriaAssegnatariaModel lCanMod = new CancelleriaAssegnatariaModel(aCancelleriaAssegnataria);

		try {
			lConn = getDBConnection();
			lCanDao = new CancelleriaAssegnatariaDAO(lConn);
			lCanDao.setDAOFromModelForUpdate(aCancelleriaAssegnataria);
			lCanDao.update();
			commit(lConn);
		} catch (Exception ex) {
			rollback(lConn);
			// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			// siesLogger.error("DAOException: " + ex);
			throw new F3BException(
					"CancelleriaAssegnatariaController.ExModifica: Non posso modificare: " + ex);
		} finally {
			cleanup(lCanDao);
			cleanup(lConn);
		}
		return lCanMod;
	}

	/**
	 * Funzione di cancellazione. Il record da cancellare è individuato dalla chiave composta
	 * (COD_CANCELLERIA_ASSEGNATARIA, COD_UFFICIO) passata attraverso l'argomento CancelleriaAssegnatariaModel
	 *
	 * @param aCancelleriaAssegnataria
	 * @throws F3BException
	 */
	public void ExCancellaCancelleriaAssegnataria(CancelleriaAssegnatariaModel aCancelleriaAssegnataria)
			throws F3BException {

		Connection lConn = null;
		CancelleriaAssegnatariaDAO lCanDao = null;

		try {
			lConn = getDBConnection();
			lCanDao = new CancelleriaAssegnatariaDAO(lConn);
			lCanDao.setCondizione(aCancelleriaAssegnataria.getCodCancelleriaAssegnataria(),
					aCancelleriaAssegnataria.getCodUfficio());
			lCanDao.delete();
			commit(lConn);
		} catch (DAOException daoEx) {
			if (daoEx.INTEGRITY_CONSTRAINT_VIOLATED)
				throw new F3BException(F3BException.USER_MESSAGE,
						"Cancelleria Assegnataria in uso. Impossibile effettuare la Cancellazione!");
			throw new F3BException(
					"CancelleriaAssegnatariaController.ExCancellaCancelleriaAssegnataria: " + daoEx);
		} catch (Exception eEx) {
			rollback(lConn);
			throw new F3BException(
					"CancelleriaAssegnatariaController.ExCancellaCancelleriaAssegnataria: " + eEx);
		} finally {
			cleanup(lCanDao);
			cleanup(lConn);
		}
	}

}