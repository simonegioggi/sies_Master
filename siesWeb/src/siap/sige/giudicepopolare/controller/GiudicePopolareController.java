package siap.sige.giudicepopolare.controller;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Vector;

import siap.controller.SiapController;
import siap.sige.SIGEException;
import siap.sige.giudicepopolare.dao.GiudicePopolareDAO;
import siap.sige.giudicepopolare.dao.GiudicePopolareSqlDAO;
import siap.sige.giudicepopolare.model.GiudicePopolareModel;
import f3b.dao.DAOException;
import f3b.model.DecodeModel;
import f3b.util.F3BException;

/**
 * <p>
 * Title: GiudicePopolareController
 * </p>
 * <p>
 * Description: Classe Controller per il GiudicePopolare
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
public class GiudicePopolareController extends SiapController implements IGiudicePopolare {

	/**
	 * Metodo che esegue l'inserimento di un curatore.
	 * <p>
	 * 
	 * @param aGiudicePopolare
	 *            GiudicePopolareModel Il model con i dati da inserire.
	 * @throws F3BException
	 *             propaga errore di eccezione.
	 * @return GiudicePopolareModel ritorna il model.
	 */
	public GiudicePopolareModel ExInserisciGiudicePopolare(GiudicePopolareModel aGiudicePopolare)
			throws F3BException {
		Connection lConn = null;
		GiudicePopolareDAO lGiuPopDao = null;
		GiudicePopolareModel lGiuPopMod = null;

		try {
			lConn = getDBConnection();
			lGiuPopMod = new GiudicePopolareModel(aGiudicePopolare);
			lGiuPopDao = new GiudicePopolareDAO(lConn);
			lGiuPopDao.setDAOFromModel(aGiudicePopolare);
			BigDecimal lKey = null;
			lKey = lGiuPopDao.insert();
			lGiuPopMod.setIdGiudicePopolare(lKey);

			commit(lConn);
		} catch (DAOException daoex) {
			rollback(lConn);
			if (daoex.UNIQUE_CONSTRAINT_VIOLATED)
				throw new SIGEException(SIGEException.USER_MESSAGE,
						"Inserimento impossibile: GiudicePopolare già presente! Controllare il C. F.!");
			throw new F3BException(
					"GiudicePopolareController.ExInserisciGiudicePopolare: Non posso inserire: " + daoex);
		} catch (Exception ex) {
			rollback(lConn);
			throw new F3BException(
					"GiudicePopolareController.ExInserisciGiudicePopolare: Non posso inserire : " + ex);
		} finally {
			cleanup(lGiuPopDao);
			cleanup(lConn);
		}

		return lGiuPopMod;
	}

	/**
	 * Metodo che esegue la ricerca di un GiudicePopolare.
	 * <p>
	 * 
	 * @param aGiudicePopolare
	 *            Model popolato con i parametri necessari per la ricerca
	 * @return ritorna l'insieme delle occorrenze.
	 * @throws F3BException
	 *             propaga errore di eccezione.
	 */
	public Vector ExRicercaGiudicePopolare(GiudicePopolareModel aGiudicePopolare) throws F3BException {
		Connection lConn = null;
		Vector lGiudiciPopolari = new Vector();
		GiudicePopolareSqlDAO lGiuPopDao = null;

		try {
			lConn = getDBConnection();
			lGiuPopDao = new GiudicePopolareSqlDAO(lConn);
			lGiuPopDao.ricercaGiudicePopolare(aGiudicePopolare);
			lGiudiciPopolari = new Vector(lGiuPopDao.getModels());

			if (lGiudiciPopolari.size() == 0)
				throw new F3BException(F3BException.USER_MESSAGE, "Nessun Elemento trovato");
		} catch (DAOException daoEx) {
			throw new F3BException(
					"GiudicePopolareController.ExRicercaGiudicePopolare: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lGiuPopDao);
			cleanup(lConn);
		}

		return lGiudiciPopolari;
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
	public Vector ExElencoCbxGiudiciPopolariByCodUfficio(String aCodUfficio) throws F3BException {
		Connection lConn = null;
		GiudicePopolareSqlDAO lGiuPopDao = null;
		Vector lGiuPopDecMods = new Vector();

		try {
			lConn = getDBConnection();
			lGiuPopDao = new GiudicePopolareSqlDAO(lConn);

			lGiuPopDao.ricercaGiudicePopolareByCodUfficio(aCodUfficio);
			lGiuPopDao.start();

			while (lGiuPopDao.next()) {
				String lIdGiudicePopolare = lGiuPopDao.getBigDecimal("ID_GIUDICE_POPOLARE").toString();
				String lNome = lGiuPopDao.getString("NOME");
				String lCognome = lGiuPopDao.getString("COGNOME");

				lGiuPopDecMods.add(new DecodeModel(lIdGiudicePopolare, lCognome + " " + lNome));
			}

			lGiuPopDao.stop();
		} catch (DAOException daoEx) {
			throw new F3BException(
					"GiudicePopolareController.ExElencoCbxGiudiciPopolariByCodUfficio: Non posso leggere : "
							+ daoEx);
		} finally {
			cleanup(lGiuPopDao);
			cleanup(lConn);
		}

		return lGiuPopDecMods;
	}

	/**
	 * Metodo che esegue la ricerca puntuale per l'id di un GiudicePopolare.
	 * <p>
	 * 
	 * @param aKey
	 *            id chiave di puntamento al record.
	 * @return GiudicePopolareModel ritorna il model opportunamente popolato.
	 * @throws F3BException
	 *             propga errore di eccezione.
	 */
	public GiudicePopolareModel ExRicercaGiudicePopolareByKey(BigDecimal aKey) throws F3BException {
		Connection lConn = null;
		GiudicePopolareSqlDAO lGiuPopDao = null;
		GiudicePopolareModel lGiuPopMod;

		try {
			lConn = getDBConnection();
			lGiuPopDao = new GiudicePopolareSqlDAO(lConn);
			lGiuPopDao.ricercaGiudicePopolareByKey(aKey);
			lGiuPopMod = (GiudicePopolareModel) lGiuPopDao.getModelByKey();
		} catch (DAOException daoEx) {
			throw new F3BException(
					"GiudicePopolareController.ExRicercaGiudicePopolare: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lGiuPopDao);
			cleanup(lConn);
		}
		return lGiuPopMod;
	}

	/**
	 * Metodo che esegue la modifica dei dati di un GiudicePopolare.
	 * <p>
	 * 
	 * @param aGiudicePopolare
	 *            Model GiudicePopolare.
	 * @return model dell'e GiudicePopolare di ritorno.
	 * @throws F3BException
	 *             propga errore di eccezione.
	 */
	public GiudicePopolareModel ExModificaGiudicePopolare(GiudicePopolareModel aGiudicePopolare)
			throws F3BException {
		Connection lConn = null;
		GiudicePopolareDAO lGiuPopDao = null;
		GiudicePopolareModel lGiuPopMod = new GiudicePopolareModel(aGiudicePopolare);

		try {
			lConn = getDBConnection();
			lGiuPopDao = new GiudicePopolareDAO(lConn);
			lGiuPopDao.setDAOFromModelForUpdate(aGiudicePopolare);
			lGiuPopDao.update();

			commit(lConn);
		} catch (DAOException daoex) {
			rollback(lConn);
			throw new F3BException(
					"GiudicePopolareController.ExModificaGiudicePopolare: Non posso modificare : " + daoex);
		} catch (Exception ex) {
			rollback(lConn);
			throw new F3BException(
					"GiudicePopolareController.ExModificaGiudicePopolare: Non posso modificare : " + ex);
		} finally {
			cleanup(lGiuPopDao);
			cleanup(lConn);
		}
		return lGiuPopMod;
	}

	/**
	 * <p>
	 * Description: : la funzione effettua la cancellazione di un record nella tabella GiudicePopolare
	 * 
	 * @param IdGiudicePopolare
	 *            : identificatore univoco GiudicePopolare
	 * @return
	 * @throws F3BException
	 */
	public void ExCancellaGiudicePopolare(BigDecimal IdGiudicePopolare) throws F3BException {
		Connection lConn = null;
		GiudicePopolareDAO lGiuPopDao = null;

		try {
			lConn = getDBConnection();
			lGiuPopDao = new GiudicePopolareDAO(lConn);
			lGiuPopDao.setCondizioneUpdate(IdGiudicePopolare);

			lGiuPopDao.delete();
			commit(lConn);
		} catch (DAOException daoEx) {
			rollback(lConn);
			if (daoEx.INTEGRITY_CONSTRAINT_VIOLATED)
				throw new SIGEException(SIGEException.USER_MESSAGE,
						"GiudicePopolare collegato ad altri dati. Impossibile effettuare la Cancellazione!");

			throw new F3BException(
					"GiudicePopolareController.ExCancellaGiudicePopolare: Non posso cancellare : " + daoEx);
		} catch (Exception ex) {
			rollback(lConn);
			throw new F3BException(
					"GiudicePopolareController.ExCancellaGiudicePopolare: Non posso cancellare : " + ex);
		} finally {
			cleanup(lGiuPopDao);
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
	public Vector ExRicercaGiudicePopolareByCodUfficio(String aCodUfficio) throws F3BException {
		Connection lConn = null;
		GiudicePopolareSqlDAO lGiuPopDao = null;
		Vector lGiuPopMods = null;

		try {
			lConn = getDBConnection();
			lGiuPopDao = new GiudicePopolareSqlDAO(lConn);

			lGiuPopDao.ricercaGiudicePopolareByCodUfficio(aCodUfficio);
			lGiuPopMods = new Vector(lGiuPopDao.getModels());
		} catch (DAOException daoEx) {
			throw new F3BException(
					"GiudicePopolareController.ExRicercaGiudicePopolareByCodUfficio: Non posso leggere : "
							+ daoEx);
		} finally {
			cleanup(lGiuPopDao);
			cleanup(lConn);
		}

		return lGiuPopMods;
	}

	/**
	 * Numero dei record occorsi.
	 * <p>
	 * 
	 * @param aGiudicePopolare
	 * @return
	 * @throws F3BException
	 */
	public int ExGetNumRicercaGiudicePopolare(GiudicePopolareModel aGiudicePopolare) throws F3BException {
		Connection lConn = null;
		GiudicePopolareSqlDAO lGiuPopDao = null;

		int lNum = 0;

		try {
			lConn = getDBConnection();
			lGiuPopDao = new GiudicePopolareSqlDAO(lConn);
			lGiuPopDao.ricercaGiudicePopolare(aGiudicePopolare);
			lNum = lGiuPopDao.getNumRowsSelected().intValue();
		} catch (DAOException daoEx) {
			throw new F3BException(
					"GiudicePopolareController.ExRicercaGiudicePopolare: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lGiuPopDao);
			cleanup(lConn);
		}
		return lNum;
	}

}