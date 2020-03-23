package siap.siep.modulocumulo.controller;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Iterator;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.dao.DAOException;
import f3b.log.LogF3B;
import f3b.util.F3BException;
import siap.controller.SiapController;
import siap.siep.modulocumulo.dao.ComputiCumuloDAO;
import siap.siep.modulocumulo.dao.ComputiCumuloSqlDAO;
import siap.siep.modulocumulo.model.ComputiCumuloModel;

/**
 * <p>
 * Title: ComputiCumuloController
 * </p>
 * <p>
 * Description: Classe Controller per ComputiCumulo
 * </p>
 *
 * @version 1.0
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class ComputiCumuloController extends SiapController implements IComputiCumulo {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	/*****************************************************************************
	 * Effettua l'inserimento di un ComputiCumulo a partire dai dati contenuti nel Model
	 *
	 * @param aComputiCumulo
	 *            Model con i dati da inserire
	 * @return il model con i dati inseriti e l'aggiunta dell'id del record inserito
	 * @throws F3BException
	 ****************************************************************************/
	public ComputiCumuloModel ExInserisciComputiCumulo(ComputiCumuloModel aComputiCumulo)
			throws F3BException {

		Connection lConn = null;
		ComputiCumuloDAO lComDao = null;
		ComputiCumuloModel lComMod = null;

		try {
			lConn = getDBConnection();
			lComDao = new ComputiCumuloDAO(lConn);
			lComDao.setDAOFromModel(aComputiCumulo);
			BigDecimal lSequence = lComDao.insert();
			commit(lConn);

			lComMod = new ComputiCumuloModel(aComputiCumulo);
			lComMod.setMessage("Inserimento avvenuto correttamente!");
			lComMod.setIdComputiCumulo(lSequence);
		} catch (DAOException ex) {
			rollback(lConn);
			siesLogger.error("DAOException: ", ex);
			throw new F3BException(
					"ComputiCumuloController.ExInserisciComputiCumulo: Non posso inserire: " + ex);
		} finally {
			cleanup(lComDao);
			cleanup(lConn);
		}

		return lComMod;
	}

	/*****************************************************************************
	 * Effettua la ricerca dei dati ComputiCumulo
	 *
	 * @param aComputiCumulo
	 *            Model utilizzato per costruire le condizioni di ricerca Ogni valore attualizzato nel model
	 *            verrà utilizzato per imporre una condizione di ricerca
	 * @return un vettore di model con il risultato della ricerca
	 * @throws F3BException
	 ****************************************************************************/
	public Vector<ComputiCumuloModel> ExRicercaComputiCumulo(ComputiCumuloModel aComputiCumulo)
			throws F3BException {

		Connection lConn = null;
		Vector<ComputiCumuloModel> lComputiCumuli = new Vector<>();
		ComputiCumuloDAO lComDao = null;

		try {
			lConn = getDBConnection();
			lComDao = new ComputiCumuloDAO(lConn);
			lComDao.setCondizioni(aComputiCumulo);
			lComDao.setOrderBy();
			lComDao.start();
			while (lComDao.next()) {
				lComputiCumuli.add((ComputiCumuloModel) lComDao.getModel());
			}
			lComDao.stop();
		} catch (DAOException daoEx) {
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException(
					"ComputiCumuloController.ExRicercaComputiCumulo: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lComDao);
			cleanup(lConn);
		}

		return lComputiCumuli;
	}

	/*****************************************************************************
	 * Effettua la ricerca per chiave
	 *
	 * @param akey
	 *            valore della chiave del record da ricercare
	 * @return il model con i dati trovati
	 * @throws F3BException
	 ****************************************************************************/
	public ComputiCumuloModel ExRicercaComputiCumuloById(BigDecimal aIdComputiCumulo) throws F3BException {

		Connection lConn = null;
		ComputiCumuloModel lComputiCumuloMod = new ComputiCumuloModel();
		ComputiCumuloSqlDAO lComputiCumuloSqlDao = null;

		try {
			lConn = getDBConnection();
			lComputiCumuloSqlDao = new ComputiCumuloSqlDAO(lConn);
			lComputiCumuloSqlDao.ricercaComputiCumuloByKey(aIdComputiCumulo);
			lComputiCumuloMod = (ComputiCumuloModel) lComputiCumuloSqlDao.getModelByKey();
		} catch (DAOException daoEx) {
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException(
					"ComputiCumuloController.ExRicercaComputiCumuloById: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lComputiCumuloSqlDao);
			cleanup(lConn);
		}

		return lComputiCumuloMod;
	}

	/*****************************************************************************
	 * Metodo che modifica i dati dell'ComputiCumulo Viene fatto l'update di tutti i campi del record
	 * recuperando i valori dal Model Se mancano dati nel model i corrispondenti valori della tabella verranno
	 * impostati a null
	 *
	 * @param aComputiCumulo
	 *            Model con i nuovi valori
	 * @throws F3BException
	 ****************************************************************************/
	public void ExModificaComputiCumulo(ComputiCumuloModel aComputiCumulo) throws F3BException {

		Connection lConn = null;
		ComputiCumuloDAO lComDao = null;

		try {
			lConn = getDBConnection();
			lComDao = new ComputiCumuloDAO(lConn);
			lComDao.setDAOFromModelForUpdate(aComputiCumulo);
			lComDao.selCondizioneUpdate(aComputiCumulo.getIdComputiCumulo());
			lComDao.update();
			commit(lConn);
		} catch (DAOException ex) {
			rollback(lConn);
			siesLogger.error("DAOException: " + ex);
			throw new F3BException(
					"ComputiCumuloController.ExModificaComputiCumulo: Non posso inserire: " + ex);
		} finally {
			cleanup(lComDao);
			cleanup(lConn);
		}
	}

	/*****************************************************************************
	 * Effettua la cancellazione del record
	 *
	 * @param aComputiCumulo
	 * @throws F3BException
	 ****************************************************************************/
	public void ExCancellaComputiCumuloBykey(BigDecimal aIdComputiCumulo) throws F3BException {

		Connection lConn = null;
		ComputiCumuloDAO lComDao = null;

		if (aIdComputiCumulo == null)
			throw new F3BException(
					"ComputiCumuloController.ExCancellaComputiCumulo: Impossibile cancellare: idComputo non specificato.");

		try {
			lConn = getDBConnection();
			lComDao = new ComputiCumuloDAO(lConn);
			lComDao.selCondizioneUpdate(aIdComputiCumulo);
			lComDao.delete();
			commit(lConn);
		} catch (DAOException daoEx) {
			siesLogger.error("DAOException: " + daoEx);
			rollback(lConn);
			throw new F3BException(
					"ComputiCumuloController.ExCancellaComputiCumulo: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lComDao);
			cleanup(lConn);
		}
	}

	/*****************************************************************************
	 * Recupera il numero di record restituiti della ricerca. Utile in caso di ricerche paginate per ottenere
	 * il numero totale di record
	 *
	 * @param aComputiCumulo
	 * @return numero di record trovati dalla funzione dei ricerca
	 * @throws F3BException
	 ****************************************************************************/
	public BigDecimal ExGetCountComputiCumulo(ComputiCumuloModel aComputiCumulo) throws F3BException {

		Connection lConn = null;
		BigDecimal lCount = new BigDecimal(0);
		ComputiCumuloSqlDAO lComputiCumuloSqlDao = null;

		try {
			lConn = getDBConnection();
			lComputiCumuloSqlDao = new ComputiCumuloSqlDAO(lConn);
			lComputiCumuloSqlDao.getCountComputiCumulo(aComputiCumulo);
			lComputiCumuloSqlDao.start();
			lComputiCumuloSqlDao.next();
			lCount = lComputiCumuloSqlDao.getBigDecimal("HowManyRecords");
			lComputiCumuloSqlDao.stop();
		} catch (DAOException daoEx) {
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException(
					"ComputiCumuloController.ExGetCountComputiCumulo: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lComputiCumuloSqlDao);
			cleanup(lConn);
		}

		return lCount;
	}

	/*****************************************************************************
	 * Funzione di ricerca utilizzata per la paginazione che restituisce i risultati da visualizzare nella
	 * pagina specificata in input
	 *
	 * @param aComputiCumulo
	 *            model contenete i parametri della ricerca
	 * @param aPage
	 *            pagina per la quale si vogliono ottenere i risultati
	 * @return Vettore di model con i record da visualizzare nella pagina specificata in input
	 * @throws F3BException
	 ****************************************************************************/
	public Vector<ComputiCumuloModel> ExRicercaComputiCumuloPaged(ComputiCumuloModel aComputiCumulo,
			int aPage) throws F3BException {

		Connection lConn = null;
		Vector<ComputiCumuloModel> lComputiCumuli = new Vector<>();
		ComputiCumuloSqlDAO lComputiCumuloSqlDao = null;

		try {
			lConn = getDBConnection();
			lComputiCumuloSqlDao = new ComputiCumuloSqlDAO(lConn);
			lComputiCumuloSqlDao.ricercaComputiCumuloPaged(aComputiCumulo, aPage);
			lComputiCumuli = new Vector<ComputiCumuloModel>(lComputiCumuloSqlDao.getModels());
		} catch (DAOException daoEx) {
			siesLogger.error("DAOException: ", daoEx);
			throw new F3BException(
					"ComputiCumuloController.ExRicercaComputiCumuloPaged: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lComputiCumuloSqlDao);
			cleanup(lConn);
		}
		return lComputiCumuli;
	}

	/**
	 * Recupera tutte le Richieste con Anticipazione (computi Cumulo) legati all'istruttoria indicata
	 *
	 * @param aIdIstruttoria
	 *            = id dell'istruttoria
	 */
	public Vector<ComputiCumuloModel> ExRicercaComputiCumuloByIdIstruttoria(BigDecimal aIdIstruttoria,
			BigDecimal aIdDatiFinali) throws F3BException {

		Connection lConn = null;

		Vector<ComputiCumuloModel> lComputiCumulo = new Vector<>();

		ComputiCumuloSqlDAO lComputiSqlDao = null;

		try {
			lConn = getDBConnection();

			lComputiSqlDao = new ComputiCumuloSqlDAO(lConn);

			if (aIdDatiFinali == null) {
				lComputiSqlDao.ricercaComputiCumuloByIdIstruttoria(aIdIstruttoria);
			} else {
				lComputiSqlDao.ricercaComputiCumuloByIdIstruttoriaDatiFinali(aIdIstruttoria, aIdDatiFinali);
			}

			lComputiCumulo = new Vector<ComputiCumuloModel>(lComputiSqlDao.getModels());
		} catch (DAOException daoEx) {
			siesLogger.error("DAOException: ", daoEx);
			throw new F3BException(
					"ComputiCumuloController.ExRicercaComputiCumuloByIdIstruttoria: Non posso leggere : "
							+ daoEx);
		} catch (Exception ex) {
			siesLogger.error("Exception: ", ex);
			throw new F3BException(
					"ComputiCumuloController.ExRicercaComputiCumuloByIdIstruttoria: Non posso leggere : "
							+ ex);
		} finally {
			cleanup(lComputiSqlDao);
			cleanup(lConn);
		}

		return lComputiCumulo;
	}

	/**
	 * MEV 26 CUMULO Step2 La Insert viene fatta in modalità 'NO SEQUENCE', senza utilizzare le sequnce, ma
	 * inserendo il valore della Primary_Key è già preimpostato; Questo metodo è usato nella funzione di presa
	 * in carico, per scaricare Tutti i dati del Fascicolo sulla nuova Base dati.
	 */
	public String ExInserisciComputiCumuloWithoutSequence(Vector<ComputiCumuloModel> VecComputiCum,
			Connection lConn) throws F3BException {

		String EsitodiRitorno = "00000";
		ComputiCumuloDAO lCompCumDao = null;
		ComputiCumuloModel lCompCumMod = null;

		try {
			lCompCumDao = new ComputiCumuloDAO(lConn);

			if (VecComputiCum != null && VecComputiCum.size() > 0) {
				Iterator ItxC = VecComputiCum.iterator();
				while (ItxC.hasNext()) {
					lCompCumMod = (ComputiCumuloModel) ItxC.next();
					if (lCompCumMod != null && lCompCumMod.getIdComputiCumulo() != null) {
						try {
							lCompCumDao.setDAOFromModel(lCompCumMod);
							lCompCumDao.setWithoutSequence(true);
							lCompCumDao.insert();
							lCompCumDao.stop();
						} catch (DAOException daoEx) {
							if (daoEx.UNIQUE_CONSTRAINT_VIOLATED) {
								siesLogger.warn("Computi Cumulo gia' presente...>"
										+ lCompCumMod.getIdComputiCumulo() + "<");
								EsitodiRitorno = "00001";
							} else {
								throw daoEx;
							}
						}
					}
				}
			}
		} catch (DAOException daoEx) {
			EsitodiRitorno = "01400";
			throw new F3BException(F3BException.USER_MESSAGE, "Impossibile inserire Computi Cumulo! ");
		} finally {
			cleanup(lCompCumDao);
		}

		return EsitodiRitorno;
	} // CHIUDE ExInserisciComputiCumuloWithoutSequence

	/**
	 * Recupera tutte le revoche con esito accolto by id_Titolo
	 */
	public Vector<ComputiCumuloModel> ExRicercaComputiCumuloByIdTitoloCum(BigDecimal aIdTitolo)
			throws F3BException {

		Connection lConn = null;

		Vector<ComputiCumuloModel> lComputiCumulo = new Vector<>();

		ComputiCumuloSqlDAO lComputiSqlDao = null;

		try {
			lConn = getDBConnection();
			lComputiSqlDao = new ComputiCumuloSqlDAO(lConn);
			lComputiSqlDao.ricercaComputiCumuloByIdTitoloCum(aIdTitolo);

			lComputiCumulo = new Vector<ComputiCumuloModel>(lComputiSqlDao.getModels());

		} catch (DAOException daoEx) {
			siesLogger.error("DAOException: ", daoEx);
			throw new F3BException(
					"ComputiCumuloController.ExRicercaComputiCumuloByIdTitoloCum: Non posso leggere : "
							+ daoEx);
		} catch (Exception ex) {
			siesLogger.error("Exception: ", ex);
			throw new F3BException(
					"ComputiCumuloController.ExRicercaComputiCumuloByIdTitoloCum: Non posso leggere : " + ex);
		} finally {
			cleanup(lComputiSqlDao);
			cleanup(lConn);
		}

		return lComputiCumulo;
	} // chiudi ExRicercaComputiCumuloByIdTitoloCum();

}