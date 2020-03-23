package siap.sige.sezione.controller;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Vector;

import f3b.dao.DAOException;
import f3b.model.DecodeModel;
import f3b.util.F3BException;
import siap.controller.SiapController;
import siap.sige.SIGEException;
import siap.sige.magistratosezione.dao.MagistratoSezioneSqlDAO;
import siap.sige.sezione.dao.SezioneDAO;
import siap.sige.sezione.dao.SezioneSqlDAO;
import siap.sige.sezione.model.SezioneModel;

/**
 * <p>
 * Title: CuratoreController
 * </p>
 * <p>
 * Description: Classe Controller per il Sezione
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
public class SezioneController extends SiapController implements ISezione {

	/**
	 * Metodo che esegue l'inserimento di una sezione.
	 *
	 * @param aSezione
	 *            SezioneModel Il model con i dati da inserire.
	 * @throws F3BException
	 *             propaga errore di eccezione.
	 * @return CuratoreModel ritorna il model.
	 */
	public SezioneModel ExInserisciSezione(SezioneModel aSezione) throws F3BException {

		Connection lConn = null;
		SezioneDAO lSezDao = null;
		SezioneModel lSezMod = null;

		try {
			lConn = getDBConnection();
			lSezMod = new SezioneModel(aSezione);
			lSezDao = new SezioneDAO(lConn);
			lSezDao.setDAOFromModel(aSezione);
			BigDecimal lKey = null;
			lKey = lSezDao.insert();
			lSezMod.setIdSezione(lKey);

			commit(lConn);
		} catch (DAOException daoex) {
			rollback(lConn);
			if (daoex.UNIQUE_CONSTRAINT_VIOLATED)
				throw new SIGEException(SIGEException.USER_MESSAGE,
						"Inserimento impossibile: Sezione già presente! ");
			throw new F3BException("SezioneController.ExInserisciSezione: Non posso inserire: " + daoex);
		} catch (Exception ex) {
			rollback(lConn);
			throw new F3BException("SezioneController.ExInserisciSezione: Non posso inserire : " + ex);
		} finally {
			cleanup(lSezDao);
			cleanup(lConn);
		}

		return lSezMod;
	}

	/**
	 * Metodo che esegue la ricerca di un Sezione.
	 *
	 * @param aSezione
	 *            Model popolato con i parametri necessari per la ricerca
	 * @return ritorna l'insieme delle occorrenze.
	 * @throws F3BException
	 *             propaga errore di eccezione.
	 */
	public Vector ExRicercaSezione(SezioneModel aSezione) throws F3BException {

		Connection lConn = null;
		Vector lSezioni = new Vector();
		SezioneSqlDAO lSezDao = null;

		try {
			lConn = getDBConnection();
			lSezDao = new SezioneSqlDAO(lConn);
			lSezDao.ricercaSezione(aSezione);
			lSezioni = new Vector(lSezDao.getModels());

			if (lSezioni.size() == 0)
				throw new F3BException(F3BException.USER_MESSAGE, "Nessun Elemento trovato");
		} catch (DAOException daoEx) {
			throw new F3BException("SezioneController.ExRicercaSezione: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lSezDao);
			cleanup(lConn);
		}

		return lSezioni;
	}

	/**
	 * Metodo che si occupa di recupera l'elenco delle sezioni per popolare elementi Combobox oppurtamente
	 * filtrati per il codice ufficio.
	 *
	 * @param aCodUfficio
	 *            codice uffcio.
	 * @return ritorna l'insieme di SezioneModel delle occorrenze.
	 * @throws F3BException
	 *             propaga errore di eccezione.
	 */
	public Collection ExElencoCbxSezioniByCodUfficio(String aCodUfficio) throws F3BException {

		Connection lConn = null;
		SezioneSqlDAO lSezDao = null;
		Collection lColl = new ArrayList();

		try {
			lConn = getDBConnection();
			lSezDao = new SezioneSqlDAO(lConn);
			lSezDao.ricercaSezioneByCodUfficio(aCodUfficio);
			lSezDao.start();

			while (lSezDao.next()) {
				String lIdSezione = lSezDao.getBigDecimal("ID_SEZIONE").toString();
				String lCodSezione = lSezDao.getBigDecimal("CODICE").toString() + " - "
						+ lSezDao.getString("DESCRIZIONE");

				lColl.add(new DecodeModel(lIdSezione, lCodSezione));
			}

			lSezDao.stop();
		} catch (DAOException daoEx) {
			throw new F3BException(
					"SezioneController.ExElencoCbxCuratoriByCodUfficio: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lSezDao);
			cleanup(lConn);
		}

		return lColl;
	}

	/**
	 * Metodo che esegue la ricerca puntuale per l'id di un Sezione.
	 *
	 * @param aKey
	 *            id chiave di puntamento al record.
	 * @return SezioneModel ritorna il model opportunamente popolato.
	 * @throws F3BException
	 *             propga errore di eccezione.
	 */
	public SezioneModel ExRicercaSezioneByKey(BigDecimal aKey) throws F3BException {

		Connection lConn = null;
		SezioneSqlDAO lSezDao = null;
		SezioneModel lSezMod;

		try {
			lConn = getDBConnection();
			lSezDao = new SezioneSqlDAO(lConn);
			lSezDao.ricercaSezioneByKey(aKey);
			lSezMod = (SezioneModel) lSezDao.getModelByKey();
		} catch (DAOException daoEx) {
			throw new F3BException("SezioneController.ExRicercaSezione: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lSezDao);
			cleanup(lConn);
		}
		return lSezMod;
	}

	/**
	 * Metodo che esegue la modifica dei dati di un Sezione.
	 *
	 * @param aSezione
	 *            Model Sezione.
	 * @return model dell'e Sezione di ritorno.
	 * @throws F3BException
	 *             propga errore di eccezione.
	 */
	public SezioneModel ExModificaSezione(SezioneModel aSezione) throws F3BException {

		Connection lConn = null;
		SezioneDAO lSezDao = null;
		SezioneModel lSezMod = new SezioneModel(aSezione);

		try {
			lConn = getDBConnection();
			lSezDao = new SezioneDAO(lConn);
			lSezDao.setDAOFromModelForUpdate(aSezione);

			lSezDao.update();
			commit(lConn);
		} catch (DAOException daoex) {
			rollback(lConn);
			throw new F3BException("SezioneController.ExModificaSezione: Non posso modificare : " + daoex);
		} catch (Exception ex) {
			rollback(lConn);
			throw new F3BException("SezioneController.ExModificaSezione: Non posso modificare : " + ex);
		} finally {
			cleanup(lSezDao);
			cleanup(lConn);
		}
		return lSezMod;
	}

	/**
	 * Description: : la funzione effettua la cancellazione di un record nella tabella Sezione
	 *
	 * @param IdSezione
	 *            : identificatore univoco Sezione
	 * @return
	 * @throws F3BException
	 */
	public void ExCancellaSezione(BigDecimal IdSezione) throws F3BException {

		Connection lConn = null;
		SezioneDAO lSezDao = null;

		try {
			lConn = getDBConnection();
			lSezDao = new SezioneDAO(lConn);
			lSezDao.setCondizioneUpdate(IdSezione);

			lSezDao.delete();
			commit(lConn);
		} catch (DAOException daoEx) {
			rollback(lConn);
			if (daoEx.INTEGRITY_CONSTRAINT_VIOLATED)
				throw new SIGEException(SIGEException.USER_MESSAGE,
						"Sezione collegato ad altri dati. Impossibile effettuare la Cancellazione!");

			throw new F3BException("SezioneController.ExCancellaSezione: Non posso cancellare : " + daoEx);
		} catch (Exception ex) {
			rollback(lConn);
			throw new F3BException("SezioneController.ExCancellaSezione: Non posso cancellare : " + ex);
		} finally {
			cleanup(lSezDao);
			cleanup(lConn);
		}
	}

	/**
	 * Description: : restituisce l'elenco degli Esperti per ufficio
	 *
	 * @param aCodUfficio
	 *            : COdice ufficio di appartenenza
	 * @return
	 * @throws F3BException
	 *             propaga errore di eccezione.
	 */
	public Vector ExRicercaSezioneByCodUfficio(String aCodUfficio) throws F3BException {

		Connection lConn = null;
		SezioneSqlDAO lCurDao = null;
		Vector lCurMods = null;

		try {
			lConn = getDBConnection();
			lCurDao = new SezioneSqlDAO(lConn);

			lCurDao.ricercaSezioneByCodUfficio(aCodUfficio);
			lCurMods = new Vector(lCurDao.getModels());
		} catch (DAOException daoEx) {
			throw new F3BException(
					"SezioneController.ExRicercaSezioneByCodUfficio: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lCurDao);
			cleanup(lConn);
		}

		return lCurMods;
	}

	/**
	 * Numero dei record occorsi.
	 *
	 * @param aSezione
	 * @return
	 * @throws F3BException
	 */
	public int ExGetNumRicercaSezione(SezioneModel aSezione) throws F3BException {

		Connection lConn = null;
		SezioneSqlDAO lCurDao = null;

		int lNum = 0;

		try {
			lConn = getDBConnection();
			lCurDao = new SezioneSqlDAO(lConn);
			lCurDao.ricercaSezione(aSezione);
			lNum = lCurDao.getNumRowsSelected().intValue();
		} catch (DAOException daoEx) {
			throw new F3BException("SezioneController.ExRicercaSezione: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lCurDao);
			cleanup(lConn);
		}
		return lNum;
	}

	/**
	 * 20171013: [EC] aggiungo metodo per recuperare le sezioni per codice magistrato ed ufficio appartenenza
	 *
	 * Metodo che si occupa di recupera l'elenco delle sezioni per popolare elementi Combobox oppurtamente
	 * filtrati per il codice ufficio e magistrato
	 *
	 * @param aCodUfficio
	 *            codice uffcio.
	 * @return ritorna l'insieme di SezioneModel delle occorrenze.
	 * @throws F3BException
	 *             propaga errore di eccezione.
	 */
	public Collection getElencoSezioniModificabiliByCodUfficio(String codMagistrato, String aCodUfficio)
			throws F3BException {

		Connection lConn = null;
		Collection lColl = new ArrayList();
		MagistratoSezioneSqlDAO lMagSezSqlDao = null;

		try {
			lConn = getDBConnection();
			lMagSezSqlDao = new MagistratoSezioneSqlDAO(lConn);
			lMagSezSqlDao.ricercaMagistratoSezioneByCodMagistrato(codMagistrato, aCodUfficio);

			lMagSezSqlDao.start();

			while (lMagSezSqlDao.next()) {
				String lIdSezione = lMagSezSqlDao.getBigDecimal("SEZ_ID_SEZIONE").toString();
				String lCodSezione = lMagSezSqlDao.getBigDecimal("CODICE").toString() + " - "
						+ lMagSezSqlDao.getString("DESCRIZIONE");

				lColl.add(new DecodeModel(lIdSezione, lCodSezione));
			}

			lMagSezSqlDao.stop();
		} catch (DAOException daoEx) {
			throw new F3BException(
					"SezioneController.getElencoSezioniModificabiliByCodUfficio: Non posso leggere : "
							+ daoEx);
		} finally {
			cleanup(lMagSezSqlDao);
			cleanup(lConn);
		}

		return lColl;
	}

}