package siap.sico.trasmissione.controller;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Date;
import java.util.Vector;

import org.apache.log4j.Logger;

import siap.controller.SiapController;
import siap.sico.SICOException;
import siap.sico.trasmissione.dao.TrasmissioniDAO;
import siap.sico.trasmissione.dao.TrasmissioniSqlDAO;
import siap.sico.trasmissione.model.TrasmissioniModel;
import f3b.dao.DAOException;
import f3b.util.F3BException;

/**
 * <p>
 * Title: TrasmissioniController
 * </p>
 * <p>
 * Description: Classe Controller per Trasmissioni
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
public class TrasmissioniController extends SiapController implements ITrasmissioni {

	Logger logger = Logger.getLogger("ctrlLogger");

	/*****************************************************************************
	 * Effettua l'inserimento di un Trasmissioni a partire dai dati contenuti nel Model
	 * 
	 * @param aTrasmissioni
	 *            Model con i dati da inserire
	 * @return il model con i dati inseriti e l'aggiunta dell'id del record inserito
	 * @throws F3BException
	 ****************************************************************************/
	public TrasmissioniModel ExInserisciTrasmissioni(TrasmissioniModel aTrasmissioni) throws F3BException {
		Connection lConn = null;
		TrasmissioniDAO lTraDao = null;
		TrasmissioniModel lTraMod = null;

		try {
			lConn = getDBConnection();
			lTraDao = new TrasmissioniDAO(lConn);
			lTraDao.setDAOFromModel(aTrasmissioni);
			BigDecimal lSequence = lTraDao.insert();
			commit(lConn);
			lTraMod = new TrasmissioniModel(aTrasmissioni);
			lTraMod.setMessage("Inserimento avvenuto correttamente!");
			lTraMod.setIdTrasmissione(lSequence);
		} catch (DAOException ex) {
			rollback(lConn);
			throw new F3BException("TrasmissioniController.ExInserisci: Non posso inserire: " + ex);
		} finally {
			cleanup(lTraDao);
			cleanup(lConn);
		}

		return lTraMod;
	}

	/*****************************************************************************
	 * Effettua la ricerca dei dati Trasmissioni
	 * 
	 * @param aTrasmissioni
	 *            Model utilizzato per costruire le condizioni di ricerca Ogni valore attualizzato nel model
	 *            verrà utilizzato per imporre una condizione di ricerca
	 * @return un vettore di model con il risultato della ricerca
	 * @throws F3BException
	 ****************************************************************************/
	public Vector ExRicercaTrasmissioni(TrasmissioniModel aTrasmissioni) throws F3BException {
		Connection lConn = null;
		Vector lTrasmissioni = new Vector();
		TrasmissioniDAO lTraDao = null;

		try {
			lConn = getDBConnection();
			lTraDao = new TrasmissioniDAO(lConn);
			lTraDao.setCondizioni(aTrasmissioni);
			lTraDao.setOrderBy();
			lTraDao.start();
			while (lTraDao.next()) {
				lTrasmissioni.add((TrasmissioniModel) lTraDao.getModel());
			}
			lTraDao.stop();
		} catch (DAOException daoEx) {
			throw new F3BException("TrasmissioniController.ExRicercaTrasmissioni: Non posso leggere : "
					+ daoEx);
		} finally {
			cleanup(lTraDao);
			cleanup(lConn);
		}

		return lTrasmissioni;
	}

	/*****************************************************************************
	 * Effettua la ricerca per chiave
	 * 
	 * @param akey
	 *            valore della chiave del record da ricercare
	 * @return il model con i dati trovati
	 * @throws F3BException
	 ****************************************************************************/
	public TrasmissioniModel ExRicercaTrasmissioniById(BigDecimal aIdTrasmissione) throws F3BException {
		Connection lConn = null;
		TrasmissioniModel lTrasmissioniMod = new TrasmissioniModel();
		TrasmissioniSqlDAO lTrasmissioniSqlDao = null;

		try {
			lConn = getDBConnection();
			lTrasmissioniSqlDao = new TrasmissioniSqlDAO(lConn);
			lTrasmissioniSqlDao.ricercaTrasmissioniByKey(aIdTrasmissione);
			lTrasmissioniMod = (TrasmissioniModel) lTrasmissioniSqlDao.getModelByKey();
		} catch (DAOException daoEx) {
			throw new F3BException("TrasmissioniController.ExRicercaTrasmissioniById: Non posso leggere : "
					+ daoEx);
		} finally {
			cleanup(lTrasmissioniSqlDao);
			cleanup(lConn);
		}

		return lTrasmissioniMod;
	}

	/*****************************************************************************
	 * Metodo che modifica i dati dell'Trasmissioni Viene fatto l'update di tutti i campi del record
	 * recuperando i valori dal Model Se mancano dati nel model i corrispondenti valori della tabella verranno
	 * impostati a null
	 * 
	 * @param aTrasmissioni
	 *            Model con i nuovi valori
	 * @throws F3BException
	 ****************************************************************************/
	public void ExModificaTrasmissioni(TrasmissioniModel aTrasmissioni) throws F3BException {
		Connection lConn = null;
		TrasmissioniDAO lTraDao = null;

		try {
			lConn = getDBConnection();
			lTraDao = new TrasmissioniDAO(lConn);
			lTraDao.setDAOFromModel(aTrasmissioni);
			lTraDao.selCondizioneUpdate(aTrasmissioni.getIdTrasmissione());
			lTraDao.update();
			commit(lConn);
		} catch (DAOException ex) {
			rollback(lConn);
			throw new F3BException("TrasmissioniController.ExModifica: Non posso inserire: " + ex);
		} finally {
			cleanup(lTraDao);
			cleanup(lConn);
		}
	}

	/*****************************************************************************
	 * Effettua la cancellazione del record
	 * 
	 * @param aTrasmissioni
	 * @throws F3BException
	 ****************************************************************************/
	public void ExCancellaTrasmissioni(TrasmissioniModel aTrasmissioni) throws F3BException {
		Connection lConn = null;
		TrasmissioniDAO lTraDao = null;

		try {
			lConn = getDBConnection();
			lTraDao = new TrasmissioniDAO(lConn);
			lTraDao.selCondizioneUpdate(aTrasmissioni.getIdTrasmissione());
			lTraDao.delete();
			commit(lConn);
		} catch (DAOException daoEx) {
			rollback(lConn);
			throw new F3BException("TrasmissioniController.ExCancellaTrasmissioni: Non posso leggere : "
					+ daoEx);
		} finally {
			cleanup(lTraDao);
			cleanup(lConn);
		}
	}

	/*****************************************************************************
	 * Recupera il numero di record restituiti della ricerca. Utile in caso di ricerche paginate per ottenere
	 * il numero totale di record
	 * 
	 * @param aTrasmissioni
	 * @return numero di record trovati dalla funzione dei ricerca
	 * @throws F3BException
	 ****************************************************************************/
	public BigDecimal ExGetCountTrasmissioni(TrasmissioniModel aTrasmissioni) throws F3BException {
		Connection lConn = null;
		BigDecimal lCount = new BigDecimal(0);
		TrasmissioniSqlDAO lTrasmissioniSqlDao = null;

		try {
			lConn = getDBConnection();
			lTrasmissioniSqlDao = new TrasmissioniSqlDAO(lConn);
			lTrasmissioniSqlDao.getCountTrasmissioni(aTrasmissioni);
			lTrasmissioniSqlDao.start();
			lTrasmissioniSqlDao.next();
			lCount = lTrasmissioniSqlDao.getBigDecimal("HowManyRecords");
			lTrasmissioniSqlDao.stop();
		} catch (DAOException daoEx) {
			throw new F3BException("TrasmissioniController.ExGetCountTrasmissioni: Non posso leggere : "
					+ daoEx);
		} finally {
			cleanup(lTrasmissioniSqlDao);
			cleanup(lConn);
		}

		return lCount;
	}

	/*****************************************************************************
	 * Funzione di ricerca utilizzata per la paginazione che restituisce i risultati da visualizzare nella
	 * pagina specificata in input
	 * 
	 * @param aTrasmissioni
	 *            model contenete i parametri della ricerca
	 * @param aPage
	 *            pagina per la quale si vogliono ottenere i risultati
	 * @return Vettore di model con i record da visualizzare nella pagina specificata in input
	 * @throws F3BException
	 ****************************************************************************/
	public Vector ExRicercaTrasmissioniPaged(TrasmissioniModel aTrasmissioni, int aPage) throws F3BException {
		Connection lConn = null;
		Vector lTrasmissioni = new Vector();
		TrasmissioniSqlDAO lTrasmissioniSqlDao = null;

		try {
			lConn = getDBConnection();
			lTrasmissioniSqlDao = new TrasmissioniSqlDAO(lConn);
			lTrasmissioniSqlDao.ricercaTrasmissioniPaged(aTrasmissioni, aPage);
			lTrasmissioni = new Vector(lTrasmissioniSqlDao.getModels());
		} catch (DAOException daoEx) {
			throw new F3BException("TrasmissioniController.ExRicercaTrasmissioniPaged: Non posso leggere : "
					+ daoEx);
		} finally {
			cleanup(lTrasmissioniSqlDao);
			cleanup(lConn);
		}
		return lTrasmissioni;
	}

	public Vector ExRicercaTrasmissioniPerDateTipoEsito(Date dataRicercaInizio, Date dataRicercaFine,
			String lTipoTrasmissione, String lEsitoTrasmissione, String lUfficioUtenteConnesso, int aPage)
			throws F3BException {
		Connection lConn = null;
		Vector lTrasmissioni = new Vector();
		TrasmissioniSqlDAO lTrasmissioniSqlDao = null;

		try {
			lConn = getDBConnection();
			lTrasmissioniSqlDao = new TrasmissioniSqlDAO(lConn);
			lTrasmissioniSqlDao.ricercaTrasmissioniPerDateTipoEsito(dataRicercaInizio, dataRicercaFine,
					lTipoTrasmissione, lEsitoTrasmissione, lUfficioUtenteConnesso, aPage);
			lTrasmissioniSqlDao.start();
			while (lTrasmissioniSqlDao.next()) {
				lTrasmissioni.add((TrasmissioniModel) lTrasmissioniSqlDao.getModel());
			}
			lTrasmissioniSqlDao.stop();

			if (lTrasmissioni.isEmpty())
				throw new SICOException(SICOException.USER_MESSAGE, "Nessun Elemento trovato");
		} catch (DAOException daoEx) {
			throw new F3BException(
					"TrasmissioniController.ExRicercaTrasmissioniPerDateTipoEsito: Non posso leggere : "
							+ daoEx);
		} finally {
			cleanup(lTrasmissioniSqlDao);
			cleanup(lConn);
		}
		return lTrasmissioni;
	}

	public BigDecimal ExGetCountPerDateTipoEsito(Date dataRicercaInizio, Date dataRicercaFine,
			String lTipoTrasmissione, String lEsitoTrasmissione, String lUfficioUtenteConnesso)
			throws F3BException {
		Connection lConn = null;
		BigDecimal lCount = new BigDecimal(0);
		TrasmissioniSqlDAO lTrasmissioniSqlDao = null;

		try {
			lConn = getDBConnection();
			lTrasmissioniSqlDao = new TrasmissioniSqlDAO(lConn);
			lTrasmissioniSqlDao.getCountTrasmissioniPerDateTipoEsito(dataRicercaInizio, dataRicercaFine,
					lTipoTrasmissione, lEsitoTrasmissione, lUfficioUtenteConnesso);
			lTrasmissioniSqlDao.start();
			lTrasmissioniSqlDao.next();
			lCount = lTrasmissioniSqlDao.getBigDecimal("HowManyRecords");
			lTrasmissioniSqlDao.stop();
		} catch (DAOException daoEx) {
			throw new F3BException("TrasmissioniController.ExGetCountTrasmissioni: Non posso leggere : "
					+ daoEx);
		} finally {
			cleanup(lTrasmissioniSqlDao);
			cleanup(lConn);
		}

		return lCount;
	}

}