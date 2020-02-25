package siap.sius.esperto.controller;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Collection;
import java.util.Vector;

import siap.controller.SiapController;
import siap.sius.SIUSException;
import siap.sius.esperto.dao.EspertoDAO;
import siap.sius.esperto.dao.EspertoSqlDAO;
import siap.sius.esperto.model.EspertoModel;
import f3b.dao.DAOException;
import f3b.model.DecodeModel;
import f3b.util.F3BException;

/**
 * <p>
 * Title: EspertoController
 * </p>
 * <p>
 * Description: Classe Controller per Esperto
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
@SuppressWarnings({"rawtypes", "unchecked"})
public class EspertoController extends SiapController implements IEsperto {

	/**
	 * Metodo che esegue l'inserimento di un esperto.
	 * <p>
	 * 
	 * @param aEsperto
	 *            EspertoModel Il model con i dati da inserire.
	 * @throws F3BException
	 *             propaga errore di eccezione.
	 * @return EspertoModel ritorna il model.
	 */
	public EspertoModel ExInserisciEsperto(EspertoModel aEsperto) throws F3BException {

		Connection lConn = null;
		EspertoDAO lEspDao = null;
		EspertoModel lEspMod = null;

		try {
			lConn = getDBConnection();
			lEspMod = new EspertoModel(aEsperto);
			lEspDao = new EspertoDAO(lConn);
			lEspDao.setDAOFromModel(aEsperto);
			BigDecimal lKey = null;
			lKey = lEspDao.insert();
			lEspMod.setIdEsperto(lKey);

			commit(lConn);
		} catch (DAOException daoex) {
			rollback(lConn);
			if (daoex.UNIQUE_CONSTRAINT_VIOLATED)
				throw new SIUSException(SIUSException.USER_MESSAGE,
						"Inserimento impossibile: Esperto già presente! Controllare il C. F.!");
			throw new F3BException("EspertoController.ExInserisciEsperto: Non posso inserire: " + daoex);
		} catch (Exception ex) {
			rollback(lConn);
			throw new F3BException("EspertoController.ExInserisciEsperto: Non posso inserire : " + ex);
		} finally {
			cleanup(lEspDao);
			cleanup(lConn);
		}
		return lEspMod;
	}

	/**
	 * Metodo che esegue la ricerca di un esperto.
	 * <p>
	 * 
	 * @param aEsperto
	 *            Model popolato con i parametri necessari per la ricerca
	 * @return ritorna l'insieme delle occorrenze.
	 * @throws F3BException
	 *             propaga errore di eccezione.
	 */
	public Vector ExRicercaEsperto(EspertoModel aEsperto) throws F3BException {

		Connection lConn = null;
		Vector lEsperti = new Vector();
		EspertoSqlDAO lEspDao = null;

		try {
			lConn = getDBConnection();
			lEspDao = new EspertoSqlDAO(lConn);
			lEspDao.ricercaEsperto(aEsperto);
			lEsperti = new Vector(lEspDao.getModels());

			if (lEsperti.size() == 0)
				throw new F3BException(F3BException.USER_MESSAGE, "Nessun Elemento trovato");
		} catch (DAOException daoEx) {
			throw new F3BException("EspertoController.ExRicercaEsperto: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lEspDao);
			cleanup(lConn);
		}

		return lEsperti;
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
	public Vector ExElencoCbxEspertiByCodUfficio(String aCodUfficio) throws F3BException {

		Connection lConn = null;
		EspertoSqlDAO lEspDao = null;
		Vector lEspDecMods = new Vector();

		try {
			lConn = getDBConnection();
			lEspDao = new EspertoSqlDAO(lConn);

			lEspDao.ricercaEspertoByCodUfficio(aCodUfficio);
			lEspDao.start();

			while (lEspDao.next()) {
				String lIdEsperto = lEspDao.getBigDecimal("ID_ESPERTO").toString();
				String lNome = lEspDao.getString("NOME");
				String lCognome = lEspDao.getString("COGNOME");

				lEspDecMods.add(new DecodeModel(lIdEsperto, lCognome + " " + lNome));
			}

			lEspDao.stop();
		} catch (DAOException daoEx) {
			throw new F3BException("EspertoController.ExElencoCbxEspertiByCodUfficio: Non posso leggere : "
					+ daoEx);
		} finally {
			cleanup(lEspDao);
			cleanup(lConn);
		}

		return lEspDecMods;
	}

	/**
	 * Metodo che esegue la ricerca puntuale per l'id di un esperto.
	 * <p>
	 * 
	 * @param aKey
	 *            id chiave di puntamento al record.
	 * @return EspertoModel ritorna il model opportunamente popolato.
	 * @throws F3BException
	 *             propga errore di eccezione.
	 */
	public EspertoModel ExRicercaEspertoByKey(BigDecimal aKey) throws F3BException {

		Connection lConn = null;
		EspertoSqlDAO lEspDao = null;
		EspertoModel lEspMod;

		try {
			lConn = getDBConnection();
			lEspDao = new EspertoSqlDAO(lConn);
			lEspDao.ricercaEspertoByKey(aKey);
			lEspMod = (EspertoModel) lEspDao.getModelByKey();
		} catch (DAOException daoEx) {
			throw new F3BException("EspertoController.ExRicercaEsperto: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lEspDao);
			cleanup(lConn);
		}
		return lEspMod;
	}

	/**
	 * Metodo che esegue la modifica dei dati di un esperto.
	 * <p>
	 * 
	 * @param aEsperto
	 *            Model esperto.
	 * @return model dell'e esperto di ritorno.
	 * @throws F3BException
	 *             propga errore di eccezione.
	 */
	public EspertoModel ExModificaEsperto(EspertoModel aEsperto) throws F3BException {

		Connection lConn = null;
		EspertoDAO lEspDao = null;
		EspertoModel lEspMod = new EspertoModel(aEsperto);

		try {
			lConn = getDBConnection();
			lEspDao = new EspertoDAO(lConn);
			lEspDao.setDAOFromModelForUpdate(aEsperto);

			lEspDao.update();
			commit(lConn);
		} catch (DAOException daoex) {
			rollback(lConn);
			throw new F3BException("EspertoController.ExModificaEsperto: Non posso modificare : " + daoex);
		} catch (Exception ex) {
			rollback(lConn);
			throw new F3BException("EspertoController.ExModificaEsperto: Non posso modificare : " + ex);
		} finally {
			cleanup(lEspDao);
			cleanup(lConn);
		}
		return lEspMod;
	}

	/**
	 * <p>
	 * Description: : la funzione effettua la cancellazione di un record nella tabella Esperto
	 * 
	 * @param IdEsperto
	 *            : identificatore univoco Esperto
	 * @return
	 * @throws F3BException
	 */
	public void ExCancellaEsperto(BigDecimal IdEsperto) throws F3BException {

		Connection lConn = null;
		EspertoDAO lEspDao = null;

		try {
			lConn = getDBConnection();
			lEspDao = new EspertoDAO(lConn);
			lEspDao.setCondizioneUpdate(IdEsperto);

			lEspDao.delete();
			commit(lConn);
		} catch (DAOException daoEx) {
			rollback(lConn);
			if (daoEx.INTEGRITY_CONSTRAINT_VIOLATED)
				throw new SIUSException(SIUSException.USER_MESSAGE,
						"Esperto collegato ad altri dati. Impossibile effettuare la Cancellazione!");

			throw new F3BException("EspertoController.ExCancellaEsperto: Non posso cancellare : " + daoEx);
		} catch (Exception ex) {
			rollback(lConn);
			throw new F3BException("EspertoController.ExCancellaEsperto: Non posso cancellare : " + ex);
		} finally {
			cleanup(lEspDao);
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
	public Vector ExRicercaEspertoByCodUfficio(String aCodUfficio) throws F3BException {

		Connection lConn = null;
		EspertoSqlDAO lEspDao = null;
		Vector lEspMods = null;

		try {
			lConn = getDBConnection();
			lEspDao = new EspertoSqlDAO(lConn);

			lEspDao.ricercaEspertoByCodUfficio(aCodUfficio);
			lEspMods = new Vector(lEspDao.getModels());
		} catch (DAOException daoEx) {
			throw new F3BException("EspertoController.ExRicercaEspertoByCodUfficio: Non posso leggere : "
					+ daoEx);
		} finally {
			cleanup(lEspDao);
			cleanup(lConn);
		}

		return lEspMods;
	}

	/**
	 * Numero dei record occorsi.
	 * <p>
	 * 
	 * @param aEsperto
	 * @return
	 * @throws F3BException
	 */
	public int ExGetNumRicercaEsperto(EspertoModel aEsperto) throws F3BException {

		Connection lConn = null;
		EspertoSqlDAO lEspDao = null;

		int lNum = 0;

		try {
			lConn = getDBConnection();
			lEspDao = new EspertoSqlDAO(lConn);
			lEspDao.ricercaEsperto(aEsperto);
			lNum = lEspDao.getNumRowsSelected().intValue();
		} catch (DAOException daoEx) {
			throw new F3BException("EspertoController.ExRicercaEsperto: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lEspDao);
			cleanup(lConn);
		}
		return lNum;
	}

	/**
	 * MERGE v10: aggiunto metodo di ricerca
	 */
	public Collection ExElencoCbxEspertiByCodAndTipoUff(String aCodUfficio, String aCodTipoUfficio)
			 throws F3BException {

		Connection lConn = null;
		EspertoSqlDAO lEspDao = null;
		Vector lEspDecMods = new Vector();

		try {
			lConn = getDBConnection();
			lEspDao = new EspertoSqlDAO(lConn);

			lEspDao.ricercaEspertoByCodUfficio(aCodUfficio);
			lEspDao.start();

			if ("UDSM".equals(aCodTipoUfficio) || "TDSM".equals(aCodTipoUfficio))
				lEspDecMods.add(new DecodeModel("0", "-"));

			while (lEspDao.next()) {
				String lIdEsperto = lEspDao.getBigDecimal("ID_ESPERTO").toString();
				String lNome = lEspDao.getString("NOME");
				String lCognome = lEspDao.getString("COGNOME");

				lEspDecMods.add(new DecodeModel(lIdEsperto, lCognome + " " + lNome));
			}

			lEspDao.stop();
		} catch (DAOException daoEx) {
			throw new F3BException("EspertoController.ExElencoCbxEspertiByCodAndTipoUff! Non posso leggere: "
					+ daoEx);
		} finally {
			cleanup(lEspDao);
			cleanup(lConn);
		}

		return lEspDecMods;
	}

}