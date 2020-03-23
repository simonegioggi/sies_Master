package siap.siepe.assistentesociale.controller;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Vector;

import f3b.dao.DAOException;
import f3b.model.DecodeModel;
import f3b.util.F3BException;
import siap.controller.SiapController;
import siap.siepe.SIEPEException;
import siap.siepe.assistentesociale.dao.AssistenteSocialeDAO;
import siap.siepe.assistentesociale.dao.AssistenteSocialeSqlDAO;
import siap.siepe.assistentesociale.model.AssistenteSocialeModel;

/**
 * <p>
 * Title: AssistenteSocialeController
 * </p>
 * <p>
 * Description: Classe Controller per AssistenteSociale
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
public class AssistenteSocialeController extends SiapController implements IAssistenteSociale {

	/**
	 * Metodo che si occupa dell'inserimento di un assistente sociale.
	 * <p>
	 *
	 * @param aAssistenteSociale
	 *            AssistenteSocialeModel Dati da inserire
	 * @throws F3BException
	 *             propga errore di eccezione
	 * @return AssistenteSocialeModel Ritorna il model popolato.
	 */
	public AssistenteSocialeModel ExInserisciAssistenteSociale(AssistenteSocialeModel aAssistenteSociale)
			throws F3BException {

		Connection lConn = null;
		AssistenteSocialeDAO lAssSocDao = null;
		AssistenteSocialeModel lAssSocMod = null;

		try {
			lConn = getDBConnection();
			lAssSocMod = new AssistenteSocialeModel(aAssistenteSociale);
			lAssSocDao = new AssistenteSocialeDAO(lConn);
			lAssSocDao.setDAOFromModel(aAssistenteSociale);

			BigDecimal lKey = null;
			lKey = lAssSocDao.insert();
			lAssSocMod.setIdAssistenteSociale(lKey);

			commit(lConn);
		} catch (DAOException daoex) {
			rollback(lConn);
			if (daoex.UNIQUE_CONSTRAINT_VIOLATED)
				throw new SIEPEException(SIEPEException.USER_MESSAGE,
						"Inserimento impossibile: AssistenteSociale già presente! Controllare il C. F.!");
			throw new SIEPEException(
					"AssistenteSocialeController.ExInserisciAssistenteSociale: Non posso inserire: " + daoex);
		} catch (Exception ex) {
			rollback(lConn);
			throw new SIEPEException(
					"AssistenteSocialeController.ExInserisciAssistenteSociale: Non posso inserire il soggetti : "
							+ ex);
		} finally {
			cleanup(lAssSocDao);
			cleanup(lConn);
		}
		return lAssSocMod;
	}

	/**
	 * Metodo che si occupa della ricerca di uno o più Assistente Sociale.
	 * <p>
	 *
	 * @param aAssistenteSociale
	 *            AssistenteSocialeModel Model con parametri di ricerca.
	 * @throws F3BException
	 *             Propaga errore di eccezione.
	 * @return Vector Elenco di assistenti sociali.
	 */
	public Vector ExRicercaAssistenteSociale(AssistenteSocialeModel aAssistenteSociale) throws F3BException {

		Connection lConn = null;
		Vector lAssistentiSociali = new Vector();
		AssistenteSocialeSqlDAO lAssSocDao = null;

		try {
			lConn = getDBConnection();
			lAssSocDao = new AssistenteSocialeSqlDAO(lConn);
			lAssSocDao.ricercaAssistenteSociale(aAssistenteSociale);
			lAssistentiSociali = new Vector(lAssSocDao.getModels());
			// Se non esiste nessun assistente sociale, rilancia errore di eccezione.
			if (lAssistentiSociali.size() == 0)
				throw new F3BException(F3BException.USER_MESSAGE, "Nessun Elemento trovato");
		} catch (DAOException daoEx) {
			throw new SIEPEException(
					"AssistenteSocialeController.ExRicercaAssistenteSociale: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lAssSocDao);
			cleanup(lConn);
		}

		return lAssistentiSociali;
	}

	/**
	 * Metodo di creazione elenco degli assistenti sociali per combo-box, filtrati per codice ufficio.
	 * <p>
	 *
	 * @param aCodUfficio
	 *            String codice d'ufficio
	 * @throws F3BException
	 *             propaga errpore di eccezione
	 * @return Vector lista Assistenti sociali per ComboBox
	 */
	public Vector ExElencoCbxAssistentiSocialiByCodUfficio(String aCodUfficio) throws F3BException {

		Connection lConn = null;
		AssistenteSocialeSqlDAO lAssSocDao = null;
		Vector lAssSocDecMods = new Vector();

		try {
			lConn = getDBConnection();
			lAssSocDao = new AssistenteSocialeSqlDAO(lConn);
			lAssSocDao.ricercaAssistenteSocialeByCodUfficio(aCodUfficio);
			lAssSocDao.start();

			while (lAssSocDao.next()) {
				String lIdAssistenteSociale = lAssSocDao.getBigDecimal("ID_ASSISTENTE_SOCIALE").toString();
				String lNome = lAssSocDao.getString("NOME");
				String lCognome = lAssSocDao.getString("COGNOME");

				lAssSocDecMods.add(new DecodeModel(lIdAssistenteSociale, lCognome + " " + lNome));
			}
		} catch (DAOException daoex) {
			throw new SIEPEException(
					"AssistenteSocialeController.ExElencoCbxEspertiByCodUfficio: Non posso leggere : "
							+ daoex);
		} finally {
			cleanup(lAssSocDao);
			cleanup(lConn);
		}

		return lAssSocDecMods;
	}

	/**
	 * Metodo che esegue la ricerca di un Assistente Sociale attraverso l'id.
	 * <p>
	 *
	 * @param aKey
	 *            BigDecimal Id Assistente sociale.
	 * @throws F3BException
	 *             Propaga errore di eccezione.
	 * @return AssistenteSocialeModel Model Assistente Sociale opportunamente popolato.
	 */
	public AssistenteSocialeModel ExRicercaAssistenteSocialeByKey(BigDecimal aKey) throws F3BException {

		Connection lConn = null;
		AssistenteSocialeSqlDAO lAssSocDao = null;
		AssistenteSocialeModel lAssSocMod;

		try {
			lConn = getDBConnection();
			lAssSocDao = new AssistenteSocialeSqlDAO(lConn);
			lAssSocDao.ricercaAssistenteSocialeByKey(aKey);
			lAssSocMod = (AssistenteSocialeModel) lAssSocDao.getModelByKey();
		} catch (DAOException daoex) {
			throw new SIEPEException(
					"AssistenteSocialeController.ExRicercaAssistenteSocialeByKey: Non posso leggere : "
							+ daoex);
		} finally {
			cleanup(lAssSocDao);
			cleanup(lConn);
		}
		return lAssSocMod;
	}

	/**
	 * Metodo che esgue la modifica di un Assistente Sociale, con i dati contenuti nel relativo model passato
	 * come argomento.
	 * <p>
	 *
	 * @param aAssistenteSociale
	 *            AssistenteSocialeModel Model con i dati
	 * @throws F3BException
	 *             propagazione erroe di eccezione
	 * @return AssistenteSocialeModel Model con i dati modificati.
	 */
	public AssistenteSocialeModel ExModificaAssistenteSociale(AssistenteSocialeModel aAssistenteSociale)
			throws F3BException {

		Connection lConn = null;
		AssistenteSocialeDAO lAssSocDao = null;
		AssistenteSocialeModel lAssSocMod = new AssistenteSocialeModel(aAssistenteSociale);

		try {
			lConn = getDBConnection();
			lAssSocDao = new AssistenteSocialeDAO(lConn);
			lAssSocDao.setDAOFromModelForUpdate(aAssistenteSociale);
			lAssSocDao.update();
			commit(lConn);
		} catch (DAOException daoex) {
			rollback(lConn);
			throw new SIEPEException(
					"AssistenteSocialeController.ExModificaAssistenteSociale: Non posso modificare : "
							+ daoex);
		} catch (Exception ex) {
			rollback(lConn);
			throw new SIEPEException(
					"AssistenteSocialeController.ExModificaAssistenteSociale: Non posso modificare l'assistente sociale : "
							+ ex);
		} finally {
			cleanup(lAssSocDao);
			cleanup(lConn);
		}
		return lAssSocMod;
	}

	/**
	 * Metodo che si occupa della rimozione di un determinato Assistente Sociale.
	 * <p>
	 *
	 * @param IdAssistenteSociale
	 *            BigDecimal Id dell'assistente sociale.
	 * @throws F3BException
	 *             propaga errore di eccezione.
	 */
	public void ExCancellaAssistenteSociale(BigDecimal IdAssistenteSociale) throws F3BException {

		Connection lConn = null;
		AssistenteSocialeDAO lAssSocDao = null;

		try {
			lConn = getDBConnection();
			lAssSocDao = new AssistenteSocialeDAO(lConn);
			lAssSocDao.setCondizioneUpdate(IdAssistenteSociale);
			lAssSocDao.delete();
			commit(lConn);
		} catch (DAOException daoEx) {
			rollback(lConn);
			if (daoEx.INTEGRITY_CONSTRAINT_VIOLATED)
				throw new SIEPEException(SIEPEException.USER_MESSAGE,
						"AssistenteSociale collegato ad altri dati. Impossibile effettuare la Cancellazione!");
			throw new SIEPEException(
					"AssistenteSocialeController.ExCancellaAssistenteSociale: Non posso cancellare : "
							+ daoEx);
		} catch (Exception ex) {
			rollback(lConn);
			throw new SIEPEException(
					"AssistenteSocialeController.ExCancellaAssistenteSociale: Non posso cancellare  : " + ex);
		} finally {
			cleanup(lAssSocDao);
			cleanup(lConn);
		}
	}

	/**
	 * Ricerca Assistente Sociale per ufficio.
	 * <p>
	 *
	 * @param aCodUfficio
	 *            String Codice ufficio.
	 * @throws F3BException
	 *             propaga errore di eccezione.
	 * @return Vector Elenco di assistenti sociali per ufficio.
	 */
	public Vector ExRicercaAssistenteSocialeByCodUfficio(String aCodUfficio) throws F3BException {

		Connection lConn = null;
		AssistenteSocialeSqlDAO lAssSocDao = null;
		Vector lAssSocMods = null;

		try {
			lConn = getDBConnection();
			lAssSocDao = new AssistenteSocialeSqlDAO(lConn);
			lAssSocDao.ricercaAssistenteSocialeByCodUfficio(aCodUfficio);
			lAssSocMods = new Vector(lAssSocDao.getModels());
		} catch (DAOException daoex) {
			throw new SIEPEException(
					"AssistenteSocialeController.ExRicercaAssistenteSocialeByCodUfficio : Non posso leggere : "
							+ daoex);
		} finally {
			cleanup(lAssSocDao);
			cleanup(lConn);
		}
		return lAssSocMods;
	}

}