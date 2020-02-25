package siap.bdmc.sbviewnotifiche.controller;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Vector;

import siap.bdmc.sbviewnotifiche.dao.SbViewNotificheDAO;
import siap.bdmc.sbviewnotifiche.dao.SbViewNotificheSqlDAO;
import siap.bdmc.sbviewnotifiche.dao.VariazioneStatoNotificaStoreProcedureDAO;
import siap.bdmc.sbviewnotifiche.model.SbViewNotificheModel;
import siap.controller.SiapController;
import f3b.dao.DAOException;
import f3b.util.F3BException;

/**
 * <p>
 * Title: SbViewNotificheController
 * </p>
 * <p>
 * Description: Classe Controller per SbViewNotifiche
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
public class SbViewNotificheController extends SiapController implements ISbViewNotifiche {

	/*****************************************************************************
	 * Effettua l'inserimento di un SbViewNotifiche a partire dai dati contenuti nel Model
	 * 
	 * @param aSbViewNotifiche
	 *            Model con i dati da inserire
	 * @return il model con i dati inseriti e l'aggiunta dell'id del record inserito
	 * @throws F3BException
	 ****************************************************************************/
	public SbViewNotificheModel ExInserisciSbViewNotifiche(SbViewNotificheModel aSbViewNotifiche)
			throws F3BException {
		Connection lConn = null;
		SbViewNotificheDAO lSbVDao = null;
		SbViewNotificheModel lSbVMod = null;

		try {
			lConn = getDBConnection();
			lSbVDao = new SbViewNotificheDAO(lConn);
			lSbVDao.setDAOFromModel(aSbViewNotifiche);
			lSbVDao.insert();
			commit(lConn);
			lSbVMod = new SbViewNotificheModel(aSbViewNotifiche);
			lSbVMod.setMessage("Inserimento avvenuto correttamente!");
		} catch (DAOException ex) {
			rollback(lConn);
			throw new F3BException("SbViewNotificheController.ExInserisci: Non posso inserire: " + ex);
		} finally {
			cleanup(lSbVDao);
			cleanup(lConn);
		}

		return lSbVMod;
	}

	/*****************************************************************************
	 * Effettua la ricerca dei dati SbViewNotifiche
	 * 
	 * @param aSbViewNotifiche
	 *            Model utilizzato per costruire le condizioni di ricerca Ogni valore attualizzato nel model
	 *            verrà utilizzato per imporre una condizione di ricerca
	 * @return un vettore di model con il risultato della ricerca
	 * @throws F3BException
	 ****************************************************************************/
	public Vector ExRicercaSbViewNotifiche(SbViewNotificheModel aSbViewNotifiche) throws F3BException {
		Connection lConn = null;
		Vector lSbViewNotifichi = new Vector();
		SbViewNotificheDAO lSbVDao = null;

		try {
			lConn = getDBConnection();
			lSbVDao = new SbViewNotificheDAO(lConn);
			lSbVDao.setCondizioni(aSbViewNotifiche);
			lSbVDao.setOrderBy();
			lSbVDao.start();
			while (lSbVDao.next()) {
				lSbViewNotifichi.add((SbViewNotificheModel) lSbVDao.getModel());
			}
			lSbVDao.stop();
		} catch (DAOException daoEx) {
			throw new F3BException("Problemi durante la connessione alla Banca Dati Misure Cautelari: "
					+ daoEx);
		} finally {
			cleanup(lSbVDao);
			cleanup(lConn);
		}

		return lSbViewNotifichi;
	}

	/*****************************************************************************
	 * Effettua la ricerca per chiave
	 * 
	 * @param akey
	 *            valore della chiave del record da ricercare
	 * @return il model con i dati trovati
	 * @throws F3BException
	 ****************************************************************************/
	public SbViewNotificheModel ExRicercaSbViewNotificheById(BigDecimal aProgNoti) throws F3BException {
		Connection lConn = null;
		SbViewNotificheModel lSbViewNotificheMod = new SbViewNotificheModel();
		SbViewNotificheSqlDAO lSbViewNotificheSqlDao = null;

		try {
			lConn = getDBConnection();
			lSbViewNotificheSqlDao = new SbViewNotificheSqlDAO(lConn);
			lSbViewNotificheSqlDao.ricercaSbViewNotificheByKey(aProgNoti);
			lSbViewNotificheMod = (SbViewNotificheModel) lSbViewNotificheSqlDao.getModelByKey();
		} catch (DAOException daoEx) {
			throw new F3BException(
					"SbViewNotificheController.ExRicercaSbViewNotificheById: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lSbViewNotificheSqlDao);
			cleanup(lConn);
		}

		return lSbViewNotificheMod;
	}

	/*****************************************************************************
	 * Metodo che modifica i dati dell'SbViewNotifiche Viene fatto l'update di tutti i campi del record
	 * recuperando i valori dal Model Se mancano dati nel model i corrispondenti valori della tabella verranno
	 * impostati a null
	 * 
	 * @param aSbViewNotifiche
	 *            Model con i nuovi valori
	 * @throws F3BException
	 ****************************************************************************/
	public void ExModificaSbViewNotifiche(SbViewNotificheModel aSbViewNotifiche) throws F3BException {
		Connection lConn = null;
		SbViewNotificheDAO lSbVDao = null;

		try {
			lConn = getDBConnection();
			lSbVDao = new SbViewNotificheDAO(lConn);
			lSbVDao.setDAOFromModel(aSbViewNotifiche);
			lSbVDao.selCondizioneUpdate(aSbViewNotifiche.getProgNoti());
			lSbVDao.update();
			commit(lConn);
		} catch (DAOException ex) {
			rollback(lConn);
			throw new F3BException("SbViewNotificheController.ExModifica: Non posso inserire: " + ex);
		} finally {
			cleanup(lSbVDao);
			cleanup(lConn);
		}
	}

	/*****************************************************************************
	 * Effettua la cancellazione del record
	 * 
	 * @param aSbViewNotifiche
	 * @throws F3BException
	 ****************************************************************************/
	public void ExCancellaSbViewNotifiche(SbViewNotificheModel aSbViewNotifiche) throws F3BException {
		Connection lConn = null;
		SbViewNotificheDAO lSbVDao = null;

		try {
			lConn = getDBConnection();
			lSbVDao = new SbViewNotificheDAO(lConn);
			lSbVDao.selCondizioneUpdate(aSbViewNotifiche.getProgNoti());
			lSbVDao.delete();
			commit(lConn);
		} catch (DAOException daoEx) {
			rollback(lConn);
			throw new F3BException(
					"SbViewNotificheController.ExCancellaSbViewNotifiche: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lSbVDao);
			cleanup(lConn);
		}
	}

	/*****************************************************************************
	 * Recupera il numero di record restituiti della ricerca. Utile in caso di ricerche paginate per ottenere
	 * il numero totale di record
	 * 
	 * @param aSbViewNotifiche
	 * @return numero di record trovati dalla funzione dei ricerca
	 * @throws F3BException
	 ****************************************************************************/
	public BigDecimal ExGetCountSbViewNotifiche(SbViewNotificheModel aSbViewNotifiche) throws F3BException {
		Connection lConn = null;
		BigDecimal lCount = new BigDecimal(0);
		SbViewNotificheSqlDAO lSbViewNotificheSqlDao = null;

		try {
			lConn = getDBConnection();
			lSbViewNotificheSqlDao = new SbViewNotificheSqlDAO(lConn);
			lSbViewNotificheSqlDao.getCountSbViewNotifiche(aSbViewNotifiche);
			lSbViewNotificheSqlDao.start();
			lSbViewNotificheSqlDao.next();
			lCount = lSbViewNotificheSqlDao.getBigDecimal("HowManyRecords");
			lSbViewNotificheSqlDao.stop();
		} catch (DAOException daoEx) {
			throw new F3BException(
					"SbViewNotificheController.ExGetCountSbViewNotifiche: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lSbViewNotificheSqlDao);
			cleanup(lConn);
		}

		return lCount;
	}

	public void ExChiudiSbViewNotifiche(SbViewNotificheModel aSbViewNotifiche) throws F3BException {
		Connection lConn = null;
		VariazioneStatoNotificaStoreProcedureDAO lProc = null;
		try {

			lConn = getDBTransaction();
			lProc = new VariazioneStatoNotificaStoreProcedureDAO(lConn);
			lProc.setProgNoti(aSbViewNotifiche.getProgNoti());

			lProc.setFlagStato("002");

			lProc.setDataChiuNoti(new java.sql.Date(aSbViewNotifiche.getDataChiuNoti().getTime()));
			lProc.execute();

			/*
			 * if (lProc.getEsito() != null && lProc.getEsito().compareTo(new BigDecimal(0)) != 0) { // [FT] -
			 * 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			 * LogF3B.getLogger() siesLogger.error("ERRORE DURANTE LA STORE PROCEDURE...");
			 * throw new DAOException("Errore"+ lProc.getEsito()
			 * +" durante la chiusura della notifica n° "+ aSbViewNotifiche.getProgNoti() ); }
			 */

			lConn.commit();
		} catch (DAOException ex) {
			rollback(lConn);
			throw new F3BException(F3BException.USER_MESSAGE, "Errore durante la chiusura della notifica n° "
					+ aSbViewNotifiche.getProgNoti());
		} catch (SQLException ex) {
			rollback(lConn);
			throw new F3BException(F3BException.USER_MESSAGE,
					"Errore durante ila chiusura della notifica n° " + aSbViewNotifiche.getProgNoti());
		} catch (Exception ex) {
			rollback(lConn);
			throw new F3BException(F3BException.USER_MESSAGE, "Errore durante la chiusura della notifica n° "
					+ aSbViewNotifiche.getProgNoti());
		} finally {
			cleanup(lProc);
			cleanup(lConn);
		}
	}

	/*****************************************************************************
	 * Funzione di ricerca utilizzata per la paginazione che restituisce i risultati da visualizzare nella
	 * pagina specificata in input
	 * 
	 * @param aSbViewNotifiche
	 *            model contenete i parametri della ricerca
	 * @param aPage
	 *            pagina per la quale si vogliono ottenere i risultati
	 * @return Vettore di model con i record da visualizzare nella pagina specificata in input
	 * @throws F3BException
	 ****************************************************************************/
	public Vector ExRicercaSbViewNotifichePaged(SbViewNotificheModel aSbViewNotifiche, int aPage)
			throws F3BException {
		Connection lConn = null;
		Vector lSbViewNotifichi = new Vector();
		SbViewNotificheSqlDAO lSbViewNotificheSqlDao = null;

		try {
			lConn = getDBConnection();
			lSbViewNotificheSqlDao = new SbViewNotificheSqlDAO(lConn);
			lSbViewNotificheSqlDao.ricercaSbViewNotifichePaged(aSbViewNotifiche, aPage);
			lSbViewNotifichi = new Vector(lSbViewNotificheSqlDao.getModels());
		} catch (DAOException daoEx) {
			throw new F3BException("Problemi durante la connessione alla Banca Dati Misure Cautelari: "
					+ daoEx);
		} finally {
			cleanup(lSbViewNotificheSqlDao);
			cleanup(lConn);
		}
		return lSbViewNotifichi;
	}

}