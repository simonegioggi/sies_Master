package siap.sius.tenore.controller;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Vector;

import siap.controller.SiapController;
import siap.sius.tenore.dao.TenoreDAO;
import siap.sius.tenore.dao.TenoreSqlDAO;
import siap.sius.tenore.model.TenoreModel;
import f3b.dao.DAOException;
import f3b.util.F3BException;

/**
 * <p>
 * Title: TenoreController
 * </p>
 * <p>
 * Description: Classe Controller per Tenore
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
public class TenoreController extends SiapController implements ITenore {

	/**
	 * Esegue l'inserimento di un tenore.
	 * <p>
	 * 
	 * @param aTenore
	 *            tenero model con i dati.
	 * @return il tenore model con il suo id.
	 * @throws F3BException
	 *             propaga errore di eccezione.
	 */
	public TenoreModel ExInserisciTenore(TenoreModel aTenore) throws F3BException {
		Connection lConn = null;
		TenoreDAO lTenDao = null;
		TenoreModel lTenMod = null;

		try {
			lConn = getDBConnection();
			lTenMod = new TenoreModel(aTenore);
			lTenDao = new TenoreDAO(lConn);
			lTenDao.setDAOFromModel(aTenore);
			BigDecimal lKey = null;
			lKey = lTenDao.insert();
			commit(lConn);
			lTenMod.setIdTenore(lKey);
		} catch (DAOException ex) {
			rollback(lConn);
			throw new F3BException("TenoreController.ExInserisci: Non posso inserire: " + ex);
		} finally {
			cleanup(lTenDao);
			cleanup(lConn);
		}
		return lTenMod;
	}

	/**
	 * Esegue la ricerca dei tenori.
	 * <p>
	 * 
	 * @param aTenore
	 *            Model del tenore.
	 * @return elenco dei tenori ricercati.
	 * @throws F3BException
	 *             propaga errori di eccezione.
	 */
	public Vector ExRicercaTenore(TenoreModel aTenore) throws F3BException {
		Connection lConn = null;
		Vector lTenori = new Vector();
		TenoreSqlDAO lTenDao = null;

		try {
			lConn = getDBConnection();
			lTenDao = new TenoreSqlDAO(lConn);
			lTenDao.ricercaTenore(aTenore);
			lTenori = new Vector(lTenDao.getModels());

			if (lTenori.size() == 0) {
				throw new F3BException(F3BException.USER_MESSAGE, "Nessun Elemento trovato");
			}
		} catch (DAOException daoEx) {
			throw new F3BException("TenoreController.ExRicercaTenore: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lTenDao);
			cleanup(lConn);
		}

		return lTenori;
	}

	/**
	 * Esegue la ricerca del tenore per la sua chiave id.
	 * <p>
	 * 
	 * @param aKey
	 *            chaive id del tenore da ricercare.
	 * @return il tenore model.
	 * @throws F3BException
	 *             propaga errori di eccezione.
	 */
	public TenoreModel ExRicercaTenoreByKey(BigDecimal aKey) throws F3BException {
		Connection lConn = null;
		TenoreSqlDAO lTenDao = null;
		TenoreModel lTenMod;

		try {
			lConn = getDBConnection();
			lTenDao = new TenoreSqlDAO(lConn);
			lTenDao.ricercaTenoreByKey(aKey);
			lTenMod = (TenoreModel) lTenDao.getModelByKey();
		} catch (DAOException daoEx) {
			throw new F3BException("TenoreController.ExRicercaTenore: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lTenDao);
			cleanup(lConn);
		}
		return lTenMod;
	}

	/**
	 * Esegue la modifica di un tenore.
	 * <p>
	 * 
	 * @param aTenore
	 *            dati del tenore per modifica.
	 * @return ritorna il model con i dati modificati.
	 * @throws F3BException
	 *             propaga errore di eccezione.
	 */
	public TenoreModel ExModificaTenore(TenoreModel aTenore) throws F3BException {
		Connection lConn = null;
		TenoreDAO lTenDao = null;
		TenoreModel lTenMod = new TenoreModel(aTenore);

		try {
			lConn = getDBConnection();
			lTenDao = new TenoreDAO(lConn);
			lTenDao.setDAOFromModelForUpdate(aTenore);
			lTenDao.update();
			commit(lConn);
		} catch (DAOException ex) {
			rollback(lConn);
			throw new F3BException("TenoreController.ExModificaTenore: Non posso inserire: " + ex);
		} finally {
			cleanup(lTenDao);
			cleanup(lConn);
		}
		return lTenMod;
	}

	/**
	 * Effettua la rimozione di un record tenore.
	 * <p>
	 * 
	 * @param aTenore
	 *            tenore model con l'id del rec da cancellare.
	 * @throws F3BException
	 *             propaga l'errore di eccezione.
	 */
	public void ExCancellaTenore(TenoreModel aTenore) throws F3BException {
		Connection lConn = null;
		TenoreDAO lTenDao = null;

		try {
			lConn = getDBConnection();
			lTenDao = new TenoreDAO(lConn);
			lTenDao.setCondizioneUpdate(aTenore.getIdTenore());
			lTenDao.delete();
			commit(lConn);
		} catch (DAOException daoEx) {
			throw new F3BException("TenoreController.ExCancellaTenore: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lTenDao);
			cleanup(lConn);
		}
	}

	/**
	 * Esegue la ricerca di tenori per l'id del Generale procedimento.
	 * <p>
	 * 
	 * @param aKey
	 *            chaive id del generale procedimento.
	 * @return l'insime dei tenori.
	 * @throws F3BException
	 *             propaga l'errore di eccezione.
	 */
	public Vector ExRicercaTenoreByGenProc(BigDecimal aKey) throws F3BException {
		Connection lConn = null;
		TenoreSqlDAO lTenDao = null;
		Vector lTenori = null;
		try {
			lConn = getDBConnection();
			lTenDao = new TenoreSqlDAO(lConn);
			// 07/11/2003 lTenDao.ricercaTenoreByKey(aKey);
			lTenDao.ricercaTenoreByGeneraleProc(aKey);
			lTenori = new Vector(lTenDao.getModels());
		} catch (DAOException daoEx) {
			throw new F3BException("TenoreController.ExRicercaTenoreByGenProc: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lTenDao);
			cleanup(lConn);
		}

		return lTenori;
	}

	/**
	 * Esegue la ricerca di tenori per l'id del Deposito Ordinanza.
	 * <p>
	 * 
	 * @param aKey
	 *            chaive id del Deposito Ordinanza.
	 * @return Vector lista dei tenori.
	 * @throws F3BException
	 *             propaga l'errore di eccezione.
	 */
	public Vector ExRicercaTenoreByOrdinanza(BigDecimal aKey) throws F3BException {
		Connection lConn = null;
		TenoreSqlDAO lTenDao = null;
		Vector lTenori = null;
		try {
			lConn = getDBConnection();
			lTenDao = new TenoreSqlDAO(lConn);
			lTenDao.ricercaTenoreByOrdinanza(aKey);
			lTenori = new Vector(lTenDao.getModels());
		} catch (DAOException daoEx) {
			throw new F3BException("TenoreController.ExRicercaTenoreByOrdinanza: Non posso leggere : "
					+ daoEx);
		} finally {
			cleanup(lTenDao);
			cleanup(lConn);
		}
		return lTenori;
	}

	/**
	 * Esegue la ricerca di tenori per l'id del Deposito Decreto.
	 * <p>
	 * 
	 * @param aKey
	 *            chaive id del Deposito Decreto.
	 * @return Vector lista dei tenori.
	 * @throws F3BException
	 *             propaga l'errore di eccezione.
	 */
	public Vector ExRicercaTenoreByDecreto(BigDecimal aKey) throws F3BException {
		Connection lConn = null;
		TenoreSqlDAO lTenDao = null;
		Vector lTenori = null;
		try {
			lConn = getDBConnection();
			lTenDao = new TenoreSqlDAO(lConn);
			lTenDao.ricercaTenoreByDecreto(aKey);
			lTenori = new Vector(lTenDao.getModels());
		} catch (DAOException daoEx) {
			throw new F3BException("TenoreController.ExRicercaTenoreByDecreto: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lTenDao);
			cleanup(lConn);
		}
		return lTenori;
	}

	/**
	 * Esegue la ricerca di tenori per l'id del Deposito Decreto.
	 * <p>
	 * 
	 * @param aKey
	 *            chaive id del Deposito Decreto.
	 * @return Vector lista dei tenori.
	 * @throws F3BException
	 *             propaga l'errore di eccezione.
	 */
	public Vector ExRicercaTenoreByDecretoIrreperibilità(BigDecimal aKey) throws F3BException {
		Connection lConn = null;
		TenoreSqlDAO lTenDao = null;
		Vector lTenori = null;
		try {
			lConn = getDBConnection();
			lTenDao = new TenoreSqlDAO(lConn);
			lTenDao.ricercaTenoreByDecretoIrreperibilità(aKey);
			lTenori = new Vector(lTenDao.getModels());
		} catch (DAOException daoEx) {
			throw new F3BException(
					"TenoreController.ExRicercaTenoreByDecretoIrreperibilità: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lTenDao);
			cleanup(lConn);
		}
		return lTenori;
	}

	/**
	 * Esegue la ricerca di tenori per l'id del Generale procedimento, ordinati per peso.
	 * <p>
	 * 
	 * @param aKey
	 *            chiave id del generale procedimento.
	 * @return l'insieme dei tenori.
	 * @throws F3BException
	 *             propaga l'errore di eccezione.
	 */
	public Vector ExRicercaTenoreByGenProcOrderByPeso(BigDecimal aKey) throws F3BException {
		Connection lConn = null;
		TenoreSqlDAO lTenDao = null;
		Vector lTenori = null;

		try {
			lConn = getDBConnection();
			lTenDao = new TenoreSqlDAO(lConn);
			lTenDao.ricercaTenoriByGeneraleProcOrderByPeso(aKey);
			lTenori = new Vector(lTenDao.getModels());
		} catch (DAOException daoEx) {
			throw new F3BException(
					"TenoreController.ExRicercaTenoreByGenProcOrderByPeso: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lTenDao);
			cleanup(lConn);
		}

		return lTenori;
	}

	/**
	 * Esegue la ricerca di tenori per l'id del Deposito Ordinanza, ordinati per peso.
	 * <p>
	 * 
	 * @param aKey
	 *            chiave id del Deposito Ordinanza.
	 * @return l'insieme dei tenori.
	 * @throws F3BException
	 *             propaga l'errore di eccezione.
	 */
	public Vector ExRicercaTenoreByOrdinanzaOrderByPeso(BigDecimal aKey) throws F3BException {
		Connection lConn = null;
		TenoreSqlDAO lTenDao = null;
		Vector lTenori = null;

		try {
			lConn = getDBConnection();
			lTenDao = new TenoreSqlDAO(lConn);
			lTenDao.ricercaTenoriByOrdinanzaOrderByPeso(aKey);
			lTenori = new Vector(lTenDao.getModels());
		} catch (DAOException daoEx) {
			throw new F3BException(
					"TenoreController.ExRicercaTenoreByOrdinanzaOrderByPeso: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lTenDao);
			cleanup(lConn);
		}

		return lTenori;
	}

	/**
	 * Esegue la ricerca di tenori per l'id del Deposito Ordinanza, ordinati per peso.
	 * <p>
	 * 
	 * @param aKey
	 *            chiave id del Deposito Ordinanza.
	 * @return l'insieme dei tenori.
	 * @throws F3BException
	 *             propaga l'errore di eccezione.
	 */
	public Vector ExRicercaTenoreByOrdinanzaOrderByPesoNoGenProc(BigDecimal aKey) throws F3BException {
		Connection lConn = null;
		TenoreSqlDAO lTenDao = null;
		Vector lTenori = null;

		try {
			lConn = getDBConnection();
			lTenDao = new TenoreSqlDAO(lConn);
			lTenDao.ricercaTenoriByOrdinanzaOrderByPesoNoGenProc(aKey);
			lTenori = new Vector(lTenDao.getModels());
		} catch (DAOException daoEx) {
			throw new F3BException(
					"TenoreController.ExRicercaTenoreByOrdinanzaOrderByPesoNoGenProc: Non posso leggere : "
							+ daoEx);
		} finally {
			cleanup(lTenDao);
			cleanup(lConn);
		}

		return lTenori;
	}

	/**
	 * Esegue la ricerca di tenori per l'id del Deposito Decreto, ordinati per peso.
	 * <p>
	 * 
	 * @param aKey
	 *            chiave id del Deposito Decreto.
	 * @return l'insieme dei tenori.
	 * @throws F3BException
	 *             propaga l'errore di eccezione.
	 */
	public Vector ExRicercaTenoreByDecretoOrderByPeso(BigDecimal aKey, Connection aConn) throws F3BException {
		TenoreSqlDAO lTenDao = null;
		Vector lTenori = null;

		try {
			lTenDao = new TenoreSqlDAO(aConn);
			lTenDao.ricercaTenoriByDecretoOrderByPeso(aKey);
			lTenori = new Vector(lTenDao.getModels());
		} catch (DAOException daoEx) {
			throw new F3BException(
					"TenoreController.ExRicercaTenoreByDecretoOrderByPeso: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lTenDao);
		}

		return lTenori;
	}

	/**
	 * Esegue la ricerca di tenori per l'id del Deposito Decreto, ordinati per peso.
	 * <p>
	 * 
	 * @param aKey
	 *            chiave id del Deposito Decreto.
	 * @return l'insieme dei tenori.
	 * @throws F3BException
	 *             propaga l'errore di eccezione.
	 */
	public Vector ExRicercaTenoreByDecretoOrderByPesoNoGenProc(BigDecimal aKey) throws F3BException {
		Connection lConn = null;
		TenoreSqlDAO lTenDao = null;
		Vector lTenori = null;

		try {
			lConn = getDBConnection();
			lTenDao = new TenoreSqlDAO(lConn);
			lTenDao.ricercaTenoriByDecretoOrderByPesoNoGenProc(aKey);
			lTenori = new Vector(lTenDao.getModels());
		} catch (DAOException daoEx) {
			throw new F3BException(
					"TenoreController.ExRicercaTenoreByDecretoOrderByPesoNoGenProc: Non posso leggere : "
							+ daoEx);
		} finally {
			cleanup(lTenDao);
			cleanup(lConn);
		}

		return lTenori;
	}

	/**
	 * Esegue la ricerca di tenori per l'id del Deposito Sentenza, ordinati per peso.
	 * <p>
	 * 
	 * @param aKey
	 *            chiave id del Deposito Sentenza.
	 * @return l'insieme dei tenori.
	 * @throws F3BException
	 *             propaga l'errore di eccezione.
	 */
	public Vector ExRicercaTenoreBySentenzaOrderByPeso(BigDecimal aKey) throws F3BException {
		Connection lConn = null;
		TenoreSqlDAO lTenDao = null;
		Vector lTenori = null;

		try {
			lConn = getDBConnection();
			lTenDao = new TenoreSqlDAO(lConn);
			lTenDao.ricercaTenoriBySentenzaOrderByPeso(aKey);
			lTenori = new Vector(lTenDao.getModels());
		} catch (DAOException daoEx) {
			throw new F3BException(
					"TenoreController.ExRicercaTenoreBySentenzaOrderByPeso: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lTenDao);
			cleanup(lConn);
		}

		return lTenori;
	}

}