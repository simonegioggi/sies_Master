package siap.sico.profilo.controller;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Vector;

import siap.controller.SiapController;
import siap.sico.profilo.dao.ProfiloDAO;
import siap.sico.profilo.dao.ProfiloSqlDAO;
import siap.sico.profilo.model.ProfiloModel;
import f3b.dao.DAOException;
import f3b.util.F3BException;

/**
 * <p>
 * Title: ProfiloController
 * </p>
 * <p>
 * Description: Classe Controller per Profilo
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
public class ProfiloController extends SiapController implements IProfilo {
	public ProfiloModel ExInserisciProfilo(ProfiloModel aProfilo) throws F3BException {
		Connection lConn = null;
		ProfiloDAO lProDao = null;
		ProfiloModel lProMod = null;

		try {
			lConn = getDBConnection();
			lProMod = new ProfiloModel(aProfilo);
			lProDao = new ProfiloDAO(lConn);
			lProDao.setDAOFromModel(aProfilo);
			BigDecimal lKey = null;
			lKey = lProDao.insert();
			commit(lConn);
			lProMod.setCodProfilo(lKey);
		} catch (DAOException ex) {
			rollback(lConn);
			throw new F3BException("ProfiloController.ExInserisci: Non posso inserire: " + ex);
		} finally {
			cleanup(lProDao);
			cleanup(lConn);
		}
		return lProMod;
	}

	public Vector ExRicercaProfilo(ProfiloModel aProfilo) throws F3BException {
		Connection lConn = null;
		Vector lProfili = new Vector();
		ProfiloSqlDAO lProDao = null;

		try {
			lConn = getDBConnection();
			lProDao = new ProfiloSqlDAO(lConn);
			lProDao.ricercaProfilo(aProfilo);
			lProfili = new Vector(lProDao.getModels());
			if (lProfili.size() == 0) {
				throw new F3BException(F3BException.USER_MESSAGE, "Nessun Elemento trovato");
			}
		} catch (DAOException daoEx) {
			throw new F3BException("ProfiloController.ExRicercaProfilo: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lProDao);
			cleanup(lConn);
		}
		return lProfili;
	}

	public Vector ExRicercaListaProfili() throws F3BException {
		Connection lConn = null;
		Vector lProfili = new Vector();
		ProfiloSqlDAO lProDao = null;

		try {
			lConn = getDBConnection();
			lProDao = new ProfiloSqlDAO(lConn);
			lProDao.ListaProfili();
			lProfili = new Vector(lProDao.getModels());
			if (lProfili.size() == 0) {
				throw new F3BException(F3BException.USER_MESSAGE, "Nessun Elemento trovato");
			}
		} catch (DAOException daoEx) {
			throw new F3BException("ProfiloController.ExRicercaProfilo: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lProDao);
			cleanup(lConn);
		}
		return lProfili;
	}

	public ProfiloModel ExRicercaProfiloByKey(BigDecimal aKey) throws F3BException {
		Connection lConn = null;
		ProfiloSqlDAO lProDao = null;
		ProfiloModel lProMod;

		try {
			lConn = getDBConnection();
			lProDao = new ProfiloSqlDAO(lConn);
			lProDao.ricercaProfiloByKey(aKey);
			lProMod = (ProfiloModel) lProDao.getModelByKey();
		} catch (DAOException daoEx) {
			throw new F3BException("ProfiloController.ExRicercaProfilo: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lProDao);
			cleanup(lConn);
		}
		return lProMod;
	}

	public ProfiloModel ExModificaProfilo(ProfiloModel aProfilo) throws F3BException {
		Connection lConn = null;
		ProfiloDAO lProDao = null;
		ProfiloModel lProMod = new ProfiloModel(aProfilo);

		try {
			lConn = getDBConnection();
			lProDao = new ProfiloDAO(lConn);
			lProDao.setDAOFromModelForUpdate(aProfilo);
			lProDao.update();
			commit(lConn);
		} catch (DAOException ex) {
			rollback(lConn);
			throw new F3BException("ProfiloController.ExModifica: Non posso inserire: " + ex);
		} finally {
			cleanup(lProDao);
			cleanup(lConn);
		}
		return lProMod;
	}

	public ProfiloModel ExRicercaProfiloByCodUtente(String aCodUtente) throws F3BException {
		Connection lConn = null;
		ProfiloSqlDAO lProDao = null;
		ProfiloModel lProMod = new ProfiloModel();

		try {
			lConn = getDBConnection();
			lProDao = new ProfiloSqlDAO(lConn);
			lProDao.ricercaProfiloByCodiceUtente(aCodUtente);
			lProMod = (ProfiloModel) lProDao.getModelByKey();

		} catch (DAOException ex) {

			throw new F3BException("ProfiloController.ExRicercaProfiloByCodUtente: Non posso leggere: " + ex);
		} finally {
			cleanup(lProDao);
			cleanup(lConn);
		}
		return lProMod;
	}

	public void ExCancellaProfilo(ProfiloModel aProfilo) throws F3BException {
		Connection lConn = null;
		ProfiloDAO lProDao = null;

		try {
			lConn = getDBConnection();
			lProDao = new ProfiloDAO(lConn);
			lProDao.setCondizioneUpdate(aProfilo.getCodProfilo());
			lProDao.delete();
			commit(lConn);
		} catch (DAOException daoEx) {
			throw new F3BException("ProfiloController.ExCancellaProfilo: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lProDao);
			cleanup(lConn);
		}
	}

	/**
	 * Metodo che esegue attraverso l'utilizzo del SQLDao, la ricerca di tutti i profili corrspondenti al
	 * codice tipo uffcio passato come argomento.
	 * <p>
	 * 
	 * @param aCodTipoUfficio
	 *            String Codice tipo ufficio
	 * @throws F3BException
	 *             propaga l'errore di eccezione
	 * @return Vector ritorna l'inseime delle occorrenze.
	 */
	public Vector ExRicercaProfiliByCodTipoUfficio(String aCodTipoUfficio) throws F3BException {
		Connection lConn = null;
		Vector lProfili = new Vector();
		ProfiloSqlDAO lProDao = null;

		try {
			lConn = getDBConnection();
			lProDao = new ProfiloSqlDAO(lConn);
			lProDao.ricercaProfiliByCodTipoUfficio(aCodTipoUfficio);
			lProfili = new Vector(lProDao.getModels());
			// Se non esistono profili rilancia un errore di eccezione, a livello di
			// segnalazione utente
			if (lProfili.isEmpty())
				throw new F3BException(F3BException.USER_MESSAGE, "Nessun Elemento trovato");
		} catch (DAOException daoEx) {
			throw new F3BException("ProfiloController.ExRicercaProfiliByCodTipoUfficio: Non posso leggere : "
					+ daoEx);
		} finally {
			cleanup(lProDao);
			cleanup(lConn);
		}

		return lProfili;
	}

}