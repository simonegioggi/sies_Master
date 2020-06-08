package siap.siep.annotazioneesitotrasmissione.controller;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.dao.DAOException;
import f3b.log.LogF3B;
import f3b.util.F3BException;
import siap.controller.SiapController;
import siap.siep.annotazioneesitotrasmissione.dao.AnnotazioneEsitoTrasmissioneDAO;
import siap.siep.annotazioneesitotrasmissione.dao.AnnotazioneEsitoTrasmissioneSqlDAO;
import siap.siep.annotazioneesitotrasmissione.model.AnnotazioneEsitoTrasmissioneModel;

/**
 * <p>
 * Title: AnnotazioneEsitoTrasmissioneController
 * </p>
 * <p>
 * Description: Classe Controller per AnnotazioneEsitoTrasmissione
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
@SuppressWarnings("unchecked")
public class AnnotazioneEsitoTrasmissioneController extends SiapController
		implements IAnnotazioneEsitoTrasmissione {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	/*****************************************************************************
	 * Effettua l'inserimento di un AnnotazioneEsitoTrasmissione a partire dai dati contenuti nel Model
	 *
	 * @param aAnnotazioneEsitoTrasmissione
	 *            Model con i dati da inserire
	 * @return il model con i dati inseriti e l'aggiunta dell'id del record inserito
	 * @throws F3BException
	 ****************************************************************************/
	public AnnotazioneEsitoTrasmissioneModel ExInserisciAnnotazioneEsitoTrasmissione(
			AnnotazioneEsitoTrasmissioneModel aAnnotazioneEsitoTrasmissione) throws F3BException {

		Connection lConn = null;
		AnnotazioneEsitoTrasmissioneDAO lAnnDao = null;
		AnnotazioneEsitoTrasmissioneModel lAnnMod = null;

		try {
			lConn = getDBConnection();
			lAnnDao = new AnnotazioneEsitoTrasmissioneDAO(lConn);
			lAnnDao.setDAOFromModel(aAnnotazioneEsitoTrasmissione);
			BigDecimal lSequence = lAnnDao.insert();
			commit(lConn);
			lAnnMod = new AnnotazioneEsitoTrasmissioneModel(aAnnotazioneEsitoTrasmissione);
			lAnnMod.setMessage("Inserimento avvenuto correttamente!");
			lAnnMod.setIdEsitoTrasmissione(lSequence);
		} catch (DAOException ex) {
			rollback(lConn);
			throw new F3BException(
					"AnnotazioneEsitoTrasmissioneController.ExInserisci: Non posso inserire: " + ex);
		} finally {
			cleanup(lAnnDao);
			cleanup(lConn);
		}

		return lAnnMod;
	}

	/*****************************************************************************
	 * Effettua la ricerca dei dati AnnotazioneEsitoTrasmissione
	 *
	 * @param aAnnotazioneEsitoTrasmissione
	 *            Model utilizzato per costruire le condizioni di ricerca Ogni valore attualizzato nel model
	 *            verrà utilizzato per imporre una condizione di ricerca
	 * @return un vettore di model con il risultato della ricerca
	 * @throws F3BException
	 ****************************************************************************/
	public Vector<AnnotazioneEsitoTrasmissioneModel> ExRicercaAnnotazioneEsitoTrasmissione(
			AnnotazioneEsitoTrasmissioneModel aAnnotazioneEsitoTrasmissione) throws F3BException {

		Connection lConn = null;
		AnnotazioneEsitoTrasmissioneSqlDAO lAnnSqlDao = null;

		Vector<AnnotazioneEsitoTrasmissioneModel> lAnnotazioneEsitoTrasmissioni = new Vector<>();

		try {
			lConn = getDBConnection();
			lAnnSqlDao = new AnnotazioneEsitoTrasmissioneSqlDAO(lConn);
			lAnnSqlDao.ricercaAnnotazioneEsitoTrasmissione(aAnnotazioneEsitoTrasmissione);
			lAnnSqlDao.start();
			while (lAnnSqlDao.next()) {
				lAnnotazioneEsitoTrasmissioni.add((AnnotazioneEsitoTrasmissioneModel) lAnnSqlDao.getModel());
			}
			lAnnSqlDao.stop();
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("DAOException: ", daoEx);
			throw new F3BException(
					"AnnotazioneEsitoTrasmissioneController.ExRicercaAnnotazioneEsitoTrasmissione: Non posso leggere : "
							+ daoEx);
		} finally {
			cleanup(lAnnSqlDao);
			cleanup(lConn);
		}

		return lAnnotazioneEsitoTrasmissioni;
	}

	/*****************************************************************************
	 * Effettua la ricerca per chiave
	 *
	 * @param akey
	 *            valore della chiave del record da ricercare
	 * @return il model con i dati trovati
	 * @throws F3BException
	 ****************************************************************************/
	public AnnotazioneEsitoTrasmissioneModel ExRicercaAnnotazioneEsitoTrasmissioneById(
			BigDecimal aIdEsitoTrasmissione) throws F3BException {

		Connection lConn = null;
		AnnotazioneEsitoTrasmissioneSqlDAO lAnnotazioneEsitoTrasmissioneSqlDao = null;

		AnnotazioneEsitoTrasmissioneModel lAnnotazioneEsitoTrasmissioneMod = new AnnotazioneEsitoTrasmissioneModel();

		try {
			lConn = getDBConnection();
			lAnnotazioneEsitoTrasmissioneSqlDao = new AnnotazioneEsitoTrasmissioneSqlDAO(lConn);
			lAnnotazioneEsitoTrasmissioneSqlDao
					.ricercaAnnotazioneEsitoTrasmissioneByKey(aIdEsitoTrasmissione);
			lAnnotazioneEsitoTrasmissioneMod = (AnnotazioneEsitoTrasmissioneModel) lAnnotazioneEsitoTrasmissioneSqlDao
					.getModelByKey();
		} catch (DAOException daoEx) {
			throw new F3BException(
					"AnnotazioneEsitoTrasmissioneController.ExRicercaAnnotazioneEsitoTrasmissioneById: Non posso leggere : "
							+ daoEx);
		} finally {
			cleanup(lAnnotazioneEsitoTrasmissioneSqlDao);
			cleanup(lConn);
		}

		return lAnnotazioneEsitoTrasmissioneMod;
	}

	/*****************************************************************************
	 * Metodo che modifica i dati dell'AnnotazioneEsitoTrasmissione Viene fatto l'update di tutti i campi del
	 * record recuperando i valori dal Model Se mancano dati nel model i corrispondenti valori della tabella
	 * verranno impostati a null
	 *
	 * @param aAnnotazioneEsitoTrasmissione
	 *            Model con i nuovi valori
	 * @throws F3BException
	 ****************************************************************************/
	public void ExModificaAnnotazioneEsitoTrasmissione(
			AnnotazioneEsitoTrasmissioneModel aAnnotazioneEsitoTrasmissione) throws F3BException {

		Connection lConn = null;
		AnnotazioneEsitoTrasmissioneDAO lAnnDao = null;

		try {
			lConn = getDBConnection();
			lAnnDao = new AnnotazioneEsitoTrasmissioneDAO(lConn);
			lAnnDao.setDAOFromModel(aAnnotazioneEsitoTrasmissione);
			lAnnDao.selCondizioneUpdate(aAnnotazioneEsitoTrasmissione.getIdEsitoTrasmissione());
			lAnnDao.update();
			commit(lConn);
		} catch (DAOException ex) {
			rollback(lConn);
			throw new F3BException(
					"AnnotazioneEsitoTrasmissioneController.ExModifica: Non posso inserire: " + ex);
		} finally {
			cleanup(lAnnDao);
			cleanup(lConn);
		}
	}

	/*****************************************************************************
	 * Effettua la cancellazione del record
	 *
	 * @param aAnnotazioneEsitoTrasmissione
	 * @throws F3BException
	 ****************************************************************************/
	public void ExCancellaAnnotazioneEsitoTrasmissione(
			AnnotazioneEsitoTrasmissioneModel aAnnotazioneEsitoTrasmissione) throws F3BException {

		Connection lConn = null;
		AnnotazioneEsitoTrasmissioneDAO lAnnDao = null;

		try {
			lConn = getDBConnection();
			lAnnDao = new AnnotazioneEsitoTrasmissioneDAO(lConn);
			lAnnDao.selCondizioneUpdate(aAnnotazioneEsitoTrasmissione.getIdEsitoTrasmissione());
			lAnnDao.delete();
			commit(lConn);
		} catch (DAOException daoEx) {
			rollback(lConn);
			throw new F3BException(
					"AnnotazioneEsitoTrasmissioneController.ExCancellaAnnotazioneEsitoTrasmissione: Non posso leggere : "
							+ daoEx);
		} finally {
			cleanup(lAnnDao);
			cleanup(lConn);
		}
	}

	/*****************************************************************************
	 * Recupera il numero di record restituiti della ricerca. Utile in caso di ricerche paginate per ottenere
	 * il numero totale di record
	 *
	 * @param aAnnotazioneEsitoTrasmissione
	 * @return numero di record trovati dalla funzione dei ricerca
	 * @throws F3BException
	 ****************************************************************************/
	public BigDecimal ExGetCountAnnotazioneEsitoTrasmissione(
			AnnotazioneEsitoTrasmissioneModel aAnnotazioneEsitoTrasmissione) throws F3BException {

		Connection lConn = null;
		AnnotazioneEsitoTrasmissioneSqlDAO lAnnotazioneEsitoTrasmissioneSqlDao = null;
		BigDecimal lCount = new BigDecimal(0);

		try {
			lConn = getDBConnection();
			lAnnotazioneEsitoTrasmissioneSqlDao = new AnnotazioneEsitoTrasmissioneSqlDAO(lConn);
			lAnnotazioneEsitoTrasmissioneSqlDao
					.getCountAnnotazioneEsitoTrasmissione(aAnnotazioneEsitoTrasmissione);
			lAnnotazioneEsitoTrasmissioneSqlDao.start();
			lAnnotazioneEsitoTrasmissioneSqlDao.next();
			lCount = lAnnotazioneEsitoTrasmissioneSqlDao.getBigDecimal("HowManyRecords");
			lAnnotazioneEsitoTrasmissioneSqlDao.stop();
		} catch (DAOException daoEx) {
			throw new F3BException(
					"AnnotazioneEsitoTrasmissioneController.ExGetCountAnnotazioneEsitoTrasmissione: Non posso leggere : "
							+ daoEx);
		} finally {
			cleanup(lAnnotazioneEsitoTrasmissioneSqlDao);
			cleanup(lConn);
		}

		return lCount;
	}

	/*****************************************************************************
	 * Funzione di ricerca utilizzata per la paginazione che restituisce i risultati da visualizzare nella
	 * pagina specificata in input
	 *
	 * @param aAnnotazioneEsitoTrasmissione
	 *            model contenete i parametri della ricerca
	 * @param aPage
	 *            pagina per la quale si vogliono ottenere i risultati
	 * @return Vettore di model con i record da visualizzare nella pagina specificata in input
	 * @throws F3BException
	 ****************************************************************************/
	public Vector<AnnotazioneEsitoTrasmissioneModel> ExRicercaAnnotazioneEsitoTrasmissionePaged(
			AnnotazioneEsitoTrasmissioneModel aAnnotazioneEsitoTrasmissione, int aPage) throws F3BException {

		Connection lConn = null;
		AnnotazioneEsitoTrasmissioneSqlDAO lAnnotazioneEsitoTrasmissioneSqlDao = null;
		Vector<AnnotazioneEsitoTrasmissioneModel> lAnnotazioneEsitoTrasmissioni = new Vector<>();

		try {
			lConn = getDBConnection();
			lAnnotazioneEsitoTrasmissioneSqlDao = new AnnotazioneEsitoTrasmissioneSqlDAO(lConn);
			lAnnotazioneEsitoTrasmissioneSqlDao
					.ricercaAnnotazioneEsitoTrasmissionePaged(aAnnotazioneEsitoTrasmissione, aPage);
			lAnnotazioneEsitoTrasmissioni = new Vector<AnnotazioneEsitoTrasmissioneModel>(
					lAnnotazioneEsitoTrasmissioneSqlDao.getModels());
		} catch (DAOException daoEx) {
			throw new F3BException(
					"AnnotazioneEsitoTrasmissioneController.ExRicercaAnnotazioneEsitoTrasmissionePaged: Non posso leggere : "
							+ daoEx);
		} finally {
			cleanup(lAnnotazioneEsitoTrasmissioneSqlDao);
			cleanup(lConn);
		}
		return lAnnotazioneEsitoTrasmissioni;
	}

	/**
	*
	*/
	public AnnotazioneEsitoTrasmissioneModel ExRicercaAnnotazioneEsitoTrasmissioneByIdEvento(
			BigDecimal aIdEvento) throws F3BException {

		Connection lConn = null;
		AnnotazioneEsitoTrasmissioneSqlDAO lAnnotazioneEsitoTrasmissioneSqlDao = null;
		AnnotazioneEsitoTrasmissioneModel lAnnotazioneEsitoTrasmissioneMod = new AnnotazioneEsitoTrasmissioneModel();

		try {
			lConn = getDBConnection();
			lAnnotazioneEsitoTrasmissioneSqlDao = new AnnotazioneEsitoTrasmissioneSqlDAO(lConn);
			lAnnotazioneEsitoTrasmissioneSqlDao.ricercaAnnotazioneEsitoTrasmissioneByIdEvento(aIdEvento);
			lAnnotazioneEsitoTrasmissioneMod = (AnnotazioneEsitoTrasmissioneModel) lAnnotazioneEsitoTrasmissioneSqlDao
					.getModelByKey();
		} catch (DAOException daoEx) {
			throw new F3BException(
					"AnnotazioneEsitoTrasmissioneController.ExRicercaAnnotazioneEsitoTrasmissioneByIdEvento: Non posso leggere : "
							+ daoEx);
		} finally {
			cleanup(lAnnotazioneEsitoTrasmissioneSqlDao);
			cleanup(lConn);
		}

		return lAnnotazioneEsitoTrasmissioneMod;
	}

}