package siap.sige.curatore.controller;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Vector;

import f3b.dao.DAOException;
import f3b.model.DecodeModel;
import f3b.util.F3BException;
import siap.controller.SiapController;
import siap.sige.SIGEException;
import siap.sige.curatore.dao.CuratoreDAO;
import siap.sige.curatore.dao.CuratoreSqlDAO;
import siap.sige.curatore.model.CuratoreModel;

/**
 * <p>
 * Title: CuratoreController
 * </p>
 * <p>
 * Description: Classe Controller per il Curatore
 * </p>
 * <p>
 * Copyright: Copyright (c) 2008
 * </p>
 * <p>
 * Company: Eutelia
 * </p>
 *
 * @version 1.0
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class CuratoreController extends SiapController implements ICuratore {

	/**
	 * Metodo che esegue l'inserimento di un curatore.
	 * <p>
	 *
	 * @param aCuratore
	 *            CuratoreModel Il model con i dati da inserire.
	 * @throws F3BException
	 *             propaga errore di eccezione.
	 * @return CuratoreModel ritorna il model.
	 */
	public CuratoreModel ExInserisciCuratore(CuratoreModel aCuratore) throws F3BException {

		Connection lConn = null;
		CuratoreDAO lCurDao = null;
		CuratoreModel lCurMod = null;

		try {
			lConn = getDBConnection();
			lCurMod = new CuratoreModel(aCuratore);
			lCurDao = new CuratoreDAO(lConn);
			lCurDao.setDAOFromModel(aCuratore);
			BigDecimal lKey = null;
			lKey = lCurDao.insert();
			lCurMod.setIdCuratore(lKey);

			commit(lConn);
		} catch (DAOException daoex) {
			rollback(lConn);
			if (daoex.UNIQUE_CONSTRAINT_VIOLATED)
				throw new SIGEException(SIGEException.USER_MESSAGE,
						"Inserimento impossibile: Curatore già presente! Controllare il C. F.!");
			throw new F3BException("CuratoreController.ExInserisciCuratore: Non posso inserire: " + daoex);
		} catch (Exception ex) {
			rollback(lConn);
			throw new F3BException("CuratoreController.ExInserisciCuratore: Non posso inserire : " + ex);
		} finally {
			cleanup(lCurDao);
			cleanup(lConn);
		}

		return lCurMod;
	}

	/**
	 * Metodo che esegue la ricerca di un Curatore.
	 * <p>
	 *
	 * @param aCuratore
	 *            Model popolato con i parametri necessari per la ricerca
	 * @return ritorna l'insieme delle occorrenze.
	 * @throws F3BException
	 *             propaga errore di eccezione.
	 */
	public Vector ExRicercaCuratore(CuratoreModel aCuratore) throws F3BException {

		Connection lConn = null;
		Vector lCuratori = new Vector();
		CuratoreSqlDAO lCurDao = null;

		try {
			lConn = getDBConnection();
			lCurDao = new CuratoreSqlDAO(lConn);
			lCurDao.ricercaCuratore(aCuratore);
			lCuratori = new Vector(lCurDao.getModels());

			if (lCuratori.size() == 0)
				throw new F3BException(F3BException.USER_MESSAGE, "Nessun Elemento trovato");
		} catch (DAOException daoEx) {
			throw new F3BException("CuratoreController.ExRicercaCuratore: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lCurDao);
			cleanup(lConn);
		}

		return lCuratori;
	}

	/**
	 * Metodo che si occupa di recupera l'elenco degli esperti per popolare elementi Combobox oppurtamente
	 * filtrati per il codice ufficio.
	 * <p>
	 *
	 * @param aCodUfficio
	 *            codice uffcio.
	 * @return ritorna l'insieme di EsperoModel delle occorrenze.
	 * @throws F3BException
	 *             propaga errore di eccezione.
	 */
	public Vector ExElencoCbxCuratoriByCodUfficio(String aCodUfficio) throws F3BException {

		Connection lConn = null;
		CuratoreSqlDAO lCurDao = null;
		Vector lCurDecMods = new Vector();

		try {
			lConn = getDBConnection();
			lCurDao = new CuratoreSqlDAO(lConn);

			lCurDao.ricercaCuratoreByCodUfficio(aCodUfficio);
			lCurDao.start();

			while (lCurDao.next()) {
				String lIdCuratore = lCurDao.getBigDecimal("ID_CURATORE").toString();
				String lNome = lCurDao.getString("NOME");
				String lCognome = lCurDao.getString("COGNOME");

				lCurDecMods.add(new DecodeModel(lIdCuratore, lCognome + " " + lNome));
			}

			lCurDao.stop();
		} catch (DAOException daoEx) {
			throw new F3BException(
					"CuratoreController.ExElencoCbxCuratoriByCodUfficio: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lCurDao);
			cleanup(lConn);
		}

		return lCurDecMods;
	}

	/**
	 * Metodo che esegue la ricerca puntuale per l'id di un Curatore.
	 * <p>
	 *
	 * @param aKey
	 *            id chiave di puntamento al record.
	 * @return CuratoreModel ritorna il model opportunamente popolato.
	 * @throws F3BException
	 *             propga errore di eccezione.
	 */
	public CuratoreModel ExRicercaCuratoreByKey(BigDecimal aKey) throws F3BException {

		Connection lConn = null;
		CuratoreSqlDAO lCurDao = null;
		CuratoreModel lCurMod;

		try {
			lConn = getDBConnection();
			lCurDao = new CuratoreSqlDAO(lConn);
			lCurDao.ricercaCuratoreByKey(aKey);
			lCurMod = (CuratoreModel) lCurDao.getModelByKey();
		} catch (DAOException daoEx) {
			throw new F3BException("CuratoreController.ExRicercaCuratore: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lCurDao);
			cleanup(lConn);
		}
		return lCurMod;
	}

	/**
	 * Metodo che esegue la modifica dei dati di un Curatore.
	 * <p>
	 *
	 * @param aCuratore
	 *            Model Curatore.
	 * @return model dell'e Curatore di ritorno.
	 * @throws F3BException
	 *             propga errore di eccezione.
	 */
	public CuratoreModel ExModificaCuratore(CuratoreModel aCuratore) throws F3BException {

		Connection lConn = null;
		CuratoreDAO lCurDao = null;
		CuratoreModel lCurMod = new CuratoreModel(aCuratore);

		try {
			lConn = getDBConnection();
			lCurDao = new CuratoreDAO(lConn);
			lCurDao.setDAOFromModelForUpdate(aCuratore);

			lCurDao.update();
			commit(lConn);
		} catch (DAOException daoex) {
			rollback(lConn);
			throw new F3BException("CuratoreController.ExModificaCuratore: Non posso modificare : " + daoex);
		} catch (Exception ex) {
			rollback(lConn);
			throw new F3BException("CuratoreController.ExModificaCuratore: Non posso modificare : " + ex);
		} finally {
			cleanup(lCurDao);
			cleanup(lConn);
		}
		return lCurMod;
	}

	/**
	 * <p>
	 * Description: : la funzione effettua la cancellazione di un record nella tabella Curatore
	 *
	 * @param IdCuratore
	 *            : identificatore univoco Curatore
	 * @return
	 * @throws F3BException
	 */
	public void ExCancellaCuratore(BigDecimal IdCuratore) throws F3BException {

		Connection lConn = null;
		CuratoreDAO lCurDao = null;

		try {
			lConn = getDBConnection();
			lCurDao = new CuratoreDAO(lConn);
			lCurDao.setCondizioneUpdate(IdCuratore);

			lCurDao.delete();
			commit(lConn);
		} catch (DAOException daoEx) {
			rollback(lConn);
			if (daoEx.INTEGRITY_CONSTRAINT_VIOLATED)
				throw new SIGEException(SIGEException.USER_MESSAGE,
						"Curatore collegato ad altri dati. Impossibile effettuare la Cancellazione!");

			throw new F3BException("CuratoreController.ExCancellaCuratore: Non posso cancellare : " + daoEx);
		} catch (Exception ex) {
			rollback(lConn);
			throw new F3BException("CuratoreController.ExCancellaCuratore: Non posso cancellare : " + ex);
		} finally {
			cleanup(lCurDao);
			cleanup(lConn);
		}
	}

	/**
	 * <p>
	 * Description: : restituisce l'elenco degli Esperti per ufficio
	 *
	 * @param aCodUfficio
	 *            : COdice ufficio di appartenenza
	 * @return
	 * @throws F3BException
	 *             propaga errore di eccezione.
	 */
	public Vector ExRicercaCuratoreByCodUfficio(String aCodUfficio) throws F3BException {

		Connection lConn = null;
		CuratoreSqlDAO lCurDao = null;
		Vector lCurMods = null;

		try {
			lConn = getDBConnection();
			lCurDao = new CuratoreSqlDAO(lConn);

			lCurDao.ricercaCuratoreByCodUfficio(aCodUfficio);
			lCurMods = new Vector(lCurDao.getModels());
		} catch (DAOException daoEx) {
			throw new F3BException(
					"CuratoreController.ExRicercaCuratoreByCodUfficio: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lCurDao);
			cleanup(lConn);
		}

		return lCurMods;
	}

	/**
	 * Numero dei record occorsi.
	 * <p>
	 *
	 * @param aCuratore
	 * @return
	 * @throws F3BException
	 */
	public int ExGetNumRicercaCuratore(CuratoreModel aCuratore) throws F3BException {

		Connection lConn = null;
		CuratoreSqlDAO lCurDao = null;

		int lNum = 0;

		try {
			lConn = getDBConnection();
			lCurDao = new CuratoreSqlDAO(lConn);
			lCurDao.ricercaCuratore(aCuratore);
			lNum = lCurDao.getNumRowsSelected().intValue();
		} catch (DAOException daoEx) {
			throw new F3BException("CuratoreController.ExRicercaCuratore: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lCurDao);
			cleanup(lConn);
		}
		return lNum;
	}

}