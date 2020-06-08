package siap.siep.sollecitoesitotrasmissione.controller;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.dao.DAOException;
import f3b.log.LogF3B;
import f3b.util.F3BException;
import siap.controller.SiapController;
import siap.siep.sollecitoesitotrasmissione.dao.SollecitoEsitoTrasmissioneDAO;
import siap.siep.sollecitoesitotrasmissione.dao.SollecitoEsitoTrasmissioneSqlDAO;
import siap.siep.sollecitoesitotrasmissione.model.SollecitoEsitoTrasmissioneModel;

/**
 * <p>
 * Title: SollecitoEsitoTrasmissioneController
 * </p>
 * <p>
 * Description: Classe Controller per SollecitoEsitoTrasmissione
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
public class SollecitoEsitoTrasmissioneController extends SiapController
		implements ISollecitoEsitoTrasmissione {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
	Logger logger = Logger.getLogger("ctrlLogger");

	/*****************************************************************************
	 * Effettua l'inserimento di un SollecitoEsitoTrasmissione a partire dai dati contenuti nel Model
	 *
	 * @param aSollecitoEsitoTrasmissione
	 *            Model con i dati da inserire
	 * @return il model con i dati inseriti e l'aggiunta dell'id del record inserito
	 * @throws F3BException
	 ****************************************************************************/
	public SollecitoEsitoTrasmissioneModel ExInserisciSollecitoEsitoTrasmissione(
			SollecitoEsitoTrasmissioneModel aSollecitoEsitoTrasmissione) throws F3BException {

		Connection lConn = null;
		SollecitoEsitoTrasmissioneDAO lSolDao = null;
		SollecitoEsitoTrasmissioneModel lSolMod = null;

		try {
			lConn = getDBConnection();
			lSolDao = new SollecitoEsitoTrasmissioneDAO(lConn);
			lSolDao.setDAOFromModel(aSollecitoEsitoTrasmissione);
			BigDecimal lSequence = lSolDao.insert();
			commit(lConn);
			lSolMod = new SollecitoEsitoTrasmissioneModel(aSollecitoEsitoTrasmissione);
			lSolMod.setMessage("Inserimento avvenuto correttamente!");
			lSolMod.setIdSollecito(lSequence);
		} catch (DAOException ex) {
			rollback(lConn);
			throw new F3BException(
					"SollecitoEsitoTrasmissioneController.ExInserisci: Non posso inserire: " + ex);
		} finally {
			cleanup(lSolDao);
			cleanup(lConn);
		}

		return lSolMod;
	}

	/*****************************************************************************
	 * Effettua la ricerca dei dati SollecitoEsitoTrasmissione
	 *
	 * @param aSollecitoEsitoTrasmissione
	 *            Model utilizzato per costruire le condizioni di ricerca Ogni valore attualizzato nel model
	 *            verrà utilizzato per imporre una condizione di ricerca
	 * @return un vettore di model con il risultato della ricerca
	 * @throws F3BException
	 ****************************************************************************/
	public Vector ExRicercaSollecitoEsitoTrasmissione(
			SollecitoEsitoTrasmissioneModel aSollecitoEsitoTrasmissione) throws F3BException {

		Connection lConn = null;
		Vector lSollecitoEsitoTrasmissioni = new Vector();
		SollecitoEsitoTrasmissioneDAO lSolDao = null;

		try {
			lConn = getDBConnection();
			lSolDao = new SollecitoEsitoTrasmissioneDAO(lConn);
			lSolDao.setCondizioni(aSollecitoEsitoTrasmissione);
			lSolDao.setOrderBy();
			lSolDao.start();
			while (lSolDao.next()) {
				lSollecitoEsitoTrasmissioni.add(lSolDao.getModel());
			}
			lSolDao.stop();
		} catch (DAOException daoEx) {
			throw new F3BException(
					"SollecitoEsitoTrasmissioneController.ExRicercaSollecitoEsitoTrasmissione: Non posso leggere : "
							+ daoEx);
		} finally {
			cleanup(lSolDao);
			cleanup(lConn);
		}

		return lSollecitoEsitoTrasmissioni;
	}

	/*****************************************************************************
	 * Effettua la ricerca per chiave
	 *
	 * @param akey
	 *            valore della chiave del record da ricercare
	 * @return il model con i dati trovati
	 * @throws F3BException
	 ****************************************************************************/
	public SollecitoEsitoTrasmissioneModel ExRicercaSollecitoEsitoTrasmissioneById(BigDecimal aIdSollecito)
			throws F3BException {

		Connection lConn = null;
		SollecitoEsitoTrasmissioneModel lSollecitoEsitoTrasmissioneMod = new SollecitoEsitoTrasmissioneModel();
		SollecitoEsitoTrasmissioneSqlDAO lSollecitoEsitoTrasmissioneSqlDao = null;

		try {
			lConn = getDBConnection();
			lSollecitoEsitoTrasmissioneSqlDao = new SollecitoEsitoTrasmissioneSqlDAO(lConn);
			lSollecitoEsitoTrasmissioneSqlDao.ricercaSollecitoEsitoTrasmissioneByKey(aIdSollecito);
			lSollecitoEsitoTrasmissioneMod = (SollecitoEsitoTrasmissioneModel) lSollecitoEsitoTrasmissioneSqlDao
					.getModelByKey();
		} catch (DAOException daoEx) {
			throw new F3BException(
					"SollecitoEsitoTrasmissioneController.ExRicercaSollecitoEsitoTrasmissioneById: Non posso leggere : "
							+ daoEx);
		} finally {
			cleanup(lSollecitoEsitoTrasmissioneSqlDao);
			cleanup(lConn);
		}

		return lSollecitoEsitoTrasmissioneMod;
	}

	/*****************************************************************************
	 * Metodo che modifica i dati dell'SollecitoEsitoTrasmissione Viene fatto l'update di tutti i campi del
	 * record recuperando i valori dal Model Se mancano dati nel model i corrispondenti valori della tabella
	 * verranno impostati a null
	 *
	 * @param aSollecitoEsitoTrasmissione
	 *            Model con i nuovi valori
	 * @throws F3BException
	 ****************************************************************************/
	public void ExModificaSollecitoEsitoTrasmissione(
			SollecitoEsitoTrasmissioneModel aSollecitoEsitoTrasmissione) throws F3BException {

		Connection lConn = null;
		SollecitoEsitoTrasmissioneDAO lSolDao = null;

		try {
			lConn = getDBConnection();
			lSolDao = new SollecitoEsitoTrasmissioneDAO(lConn);
			lSolDao.setDAOFromModel(aSollecitoEsitoTrasmissione);
			lSolDao.selCondizioneUpdate(aSollecitoEsitoTrasmissione.getIdSollecito());
			lSolDao.update();
			commit(lConn);
		} catch (DAOException ex) {
			rollback(lConn);
			throw new F3BException(
					"SollecitoEsitoTrasmissioneController.ExModifica: Non posso inserire: " + ex);
		} finally {
			cleanup(lSolDao);
			cleanup(lConn);
		}
	}

	/*****************************************************************************
	 * Effettua la cancellazione del record
	 *
	 * @param aSollecitoEsitoTrasmissione
	 * @throws F3BException
	 ****************************************************************************/
	public void ExCancellaSollecitoEsitoTrasmissione(
			SollecitoEsitoTrasmissioneModel aSollecitoEsitoTrasmissione) throws F3BException {

		Connection lConn = null;
		SollecitoEsitoTrasmissioneDAO lSolDao = null;

		try {
			lConn = getDBConnection();
			lSolDao = new SollecitoEsitoTrasmissioneDAO(lConn);
			lSolDao.selCondizioneUpdate(aSollecitoEsitoTrasmissione.getIdSollecito());
			lSolDao.delete();
			commit(lConn);
		} catch (DAOException daoEx) {
			rollback(lConn);
			throw new F3BException(
					"SollecitoEsitoTrasmissioneController.ExCancellaSollecitoEsitoTrasmissione: Non posso leggere : "
							+ daoEx);
		} finally {
			cleanup(lSolDao);
			cleanup(lConn);
		}
	}

	/*****************************************************************************
	 * Recupera il numero di record restituiti della ricerca. Utile in caso di ricerche paginate per ottenere
	 * il numero totale di record
	 *
	 * @param aSollecitoEsitoTrasmissione
	 * @return numero di record trovati dalla funzione dei ricerca
	 * @throws F3BException
	 ****************************************************************************/
	public BigDecimal ExGetCountSollecitoEsitoTrasmissione(
			SollecitoEsitoTrasmissioneModel aSollecitoEsitoTrasmissione) throws F3BException {

		Connection lConn = null;
		BigDecimal lCount = new BigDecimal(0);
		SollecitoEsitoTrasmissioneSqlDAO lSollecitoEsitoTrasmissioneSqlDao = null;

		try {
			lConn = getDBConnection();
			lSollecitoEsitoTrasmissioneSqlDao = new SollecitoEsitoTrasmissioneSqlDAO(lConn);
			lSollecitoEsitoTrasmissioneSqlDao.getCountSollecitoEsitoTrasmissione(aSollecitoEsitoTrasmissione);
			lSollecitoEsitoTrasmissioneSqlDao.start();
			lSollecitoEsitoTrasmissioneSqlDao.next();
			lCount = lSollecitoEsitoTrasmissioneSqlDao.getBigDecimal("HowManyRecords");
			lSollecitoEsitoTrasmissioneSqlDao.stop();
		} catch (DAOException daoEx) {
			throw new F3BException(
					"SollecitoEsitoTrasmissioneController.ExGetCountSollecitoEsitoTrasmissione: Non posso leggere : "
							+ daoEx);
		} finally {
			cleanup(lSollecitoEsitoTrasmissioneSqlDao);
			cleanup(lConn);
		}

		return lCount;
	}

	/*****************************************************************************
	 * Funzione di ricerca utilizzata per la paginazione che restituisce i risultati da visualizzare nella
	 * pagina specificata in input
	 *
	 * @param aSollecitoEsitoTrasmissione
	 *            model contenete i parametri della ricerca
	 * @param aPage
	 *            pagina per la quale si vogliono ottenere i risultati
	 * @return Vettore di model con i record da visualizzare nella pagina specificata in input
	 * @throws F3BException
	 ****************************************************************************/
	public Vector ExRicercaSollecitoEsitoTrasmissionePaged(
			SollecitoEsitoTrasmissioneModel aSollecitoEsitoTrasmissione, int aPage) throws F3BException {

		Connection lConn = null;
		Vector lSollecitoEsitoTrasmissioni = new Vector();
		SollecitoEsitoTrasmissioneSqlDAO lSollecitoEsitoTrasmissioneSqlDao = null;

		try {
			lConn = getDBConnection();
			lSollecitoEsitoTrasmissioneSqlDao = new SollecitoEsitoTrasmissioneSqlDAO(lConn);
			lSollecitoEsitoTrasmissioneSqlDao
					.ricercaSollecitoEsitoTrasmissionePaged(aSollecitoEsitoTrasmissione, aPage);
			lSollecitoEsitoTrasmissioni = new Vector(lSollecitoEsitoTrasmissioneSqlDao.getModels());
		} catch (DAOException daoEx) {
			throw new F3BException(
					"SollecitoEsitoTrasmissioneController.ExRicercaSollecitoEsitoTrasmissionePaged: Non posso leggere : "
							+ daoEx);
		} finally {
			cleanup(lSollecitoEsitoTrasmissioneSqlDao);
			cleanup(lConn);
		}
		return lSollecitoEsitoTrasmissioni;
	}

	/*****************************************************************************
	 * Effettua la ricerca per chiave
	 *
	 * @param akey
	 *            valore della chiave del record da ricercare
	 * @return il model con i dati trovati
	 * @throws F3BException
	 ****************************************************************************/
	public SollecitoEsitoTrasmissioneModel ExRicercaSollecitoEsitoTrasmissioneByIdEvento(BigDecimal aIdEvento)
			throws F3BException {

		Connection lConn = null;
		SollecitoEsitoTrasmissioneModel lSollecitoEsitoTrasmissioneMod = new SollecitoEsitoTrasmissioneModel();
		SollecitoEsitoTrasmissioneSqlDAO lSollecitoEsitoTrasmissioneSqlDao = null;

		try {
			lConn = getDBConnection();
			lSollecitoEsitoTrasmissioneSqlDao = new SollecitoEsitoTrasmissioneSqlDAO(lConn);
			lSollecitoEsitoTrasmissioneSqlDao.ricercaSollecitoEsitoTrasmissioneByIdEvento(aIdEvento);
			lSollecitoEsitoTrasmissioneMod = (SollecitoEsitoTrasmissioneModel) lSollecitoEsitoTrasmissioneSqlDao
					.getModelByKey();
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("", daoEx);
			throw new F3BException(
					"SollecitoEsitoTrasmissioneController.ExRicercaSollecitoEsitoTrasmissioneByIdEvento: Non posso leggere : "
							+ daoEx);
		} finally {
			cleanup(lSollecitoEsitoTrasmissioneSqlDao);
			cleanup(lConn);
		}

		return lSollecitoEsitoTrasmissioneMod;
	}

}