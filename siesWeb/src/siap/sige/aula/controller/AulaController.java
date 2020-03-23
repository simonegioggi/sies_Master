package siap.sige.aula.controller;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Vector;

import f3b.dao.DAOException;
import f3b.util.F3BException;
import siap.controller.SiapController;
import siap.sico.SICOException;
import siap.sige.SIGEException;
import siap.sige.aula.dao.AulaDAO;
import siap.sige.aula.dao.AulaSqlDAO;
import siap.sige.aula.model.AulaUdienzaModel;
import siap.sige.udienza.dao.UdienzaSigeSqlDAO;

/**
 * <p>
 * Title: AulaController
 * </p>
 * <p>
 * Description: Classe Controller per l'Aula
 * </p>
 * <p>
 * Copyright: Copyright (c) 2008
 * </p>
 * <p>
 * Company: Engineering S.p.A.
 * </p>
 *
 * @version 1.0
 */
public class AulaController extends SiapController implements IAula {

	/**
	 * Metodo che esegue l'inserimento di un'Aula.
	 * <p>
	 *
	 * @param aAula
	 *            AulaUdienzaModel Il model con i dati da inserire.
	 * @throws F3BException
	 *             propaga errore di eccezione.
	 * @return AulaUdienzaModel ritorna il model.
	 */
	public AulaUdienzaModel ExInserisciAula(AulaUdienzaModel aAula) throws F3BException {

		Connection lConn = null;
		AulaDAO lAulaDao = null;
		AulaUdienzaModel lAulaMod = null;

		try {
			lConn = getDBConnection();
			lAulaMod = new AulaUdienzaModel(aAula);
			lAulaDao = new AulaDAO(lConn);
			lAulaDao.setDAOFromModel(aAula);

			BigDecimal lKey = null;
			lKey = lAulaDao.insert();
			lAulaMod.setIdAula(lKey);

			commit(lConn);
		} catch (Exception ex) {
			rollback(lConn);
			throw new F3BException("AulaController.ExInserisciAula: Non posso inserire : " + ex);
		} finally {
			cleanup(lAulaDao);
			cleanup(lConn);
		}

		return lAulaMod;
	}

	/**
	 * Metodo che esegue la ricerca puntuale per chiave (ID_AULA, ID_SEZIONE) dell'Aula.
	 * <p>
	 *
	 * @param aIdAula
	 * @param aIdSezione
	 * @return AulaUdienzaModel ritorna il model opportunamente popolato.
	 * @throws F3BException
	 *             propaga errore di eccezione.
	 */
	public AulaUdienzaModel ExRicercaAulaByKey(BigDecimal aIdAula, BigDecimal aIdSezione)
			throws F3BException {

		Connection lConn = null;
		AulaSqlDAO lAulaDao = null;
		AulaUdienzaModel lAulaMod;

		try {
			lConn = getDBConnection();
			lAulaDao = new AulaSqlDAO(lConn);
			lAulaDao.ricercaAulaByKey(aIdAula, aIdSezione);
			lAulaMod = (AulaUdienzaModel) lAulaDao.getModelByKey();

		} catch (DAOException daoEx) {
			throw new F3BException("AulaController.ExRicercaAulaByKey : Non posso leggere : " + daoEx);
		} finally {
			cleanup(lAulaDao);
			cleanup(lConn);
		}
		return lAulaMod;
	}

	/**
	 * Metodo che esegue la ricerca dell'aula per sezione e descrizione aula.
	 * <p>
	 *
	 * @param aIdSezione
	 * @param descAula
	 * @return AulaUdienzaModel ritorna il model opportunamente popolato.
	 * @throws F3BException
	 *             propaga errore di eccezione.
	 */
	public AulaUdienzaModel ExRicercaAulaByDescrizione(BigDecimal aIdSezione, String descAula)
			throws F3BException {

		Connection lConn = null;
		AulaSqlDAO lAulaDao = null;
		AulaUdienzaModel lAulaMod;

		try {
			lConn = getDBConnection();
			lAulaDao = new AulaSqlDAO(lConn);
			lAulaDao.ricercaAulaByDescrizione(aIdSezione, descAula);
			lAulaMod = (AulaUdienzaModel) lAulaDao.getModelByKey();

		} catch (DAOException daoEx) {
			throw new F3BException(
					"AulaController.ExRicercaAulaByDescrizione : Non posso leggere : " + daoEx);
		} finally {
			cleanup(lAulaDao);
			cleanup(lConn);
		}
		return lAulaMod;
	}

	public Vector<AulaUdienzaModel> ExRicercaAulaByIdSezione(BigDecimal idSezione) throws F3BException {

		Connection lConn = null;
		Vector<AulaUdienzaModel> lAule = new Vector<>();
		AulaSqlDAO lAulaDao = null;

		try {
			lConn = getDBConnection();
			lAulaDao = new AulaSqlDAO(lConn);
			lAulaDao.ricercaAulaByIdSezione(idSezione);
			lAulaDao.start();

			while (lAulaDao.next()) {
				AulaUdienzaModel lAulaModel = (AulaUdienzaModel) lAulaDao.getModel();
				lAule.add(lAulaModel);
			}

			lAulaDao.stop();
		} catch (DAOException daoEx) {
			throw new F3BException("AulaController.ExRicercaAulaByIdSezione: Non posso leggere : " + daoEx);
		} catch (Exception e) {
			throw new F3BException("AulaController.ExRicercaAulaByIdSezione: " + e);
		} finally {
			cleanup(lAulaDao);
			cleanup(lConn);
		}

		return lAule;
	}

	/**
	 * Metodo che esegue la ricerca di un'Aula.
	 * <p>
	 *
	 * @param aAula
	 *            Model popolato con i parametri necessari per la ricerca
	 * @return ritorna l'insieme delle occorrenze.
	 * @throws F3BException
	 *             propaga errore di eccezione.
	 */
	@SuppressWarnings("unchecked")
	public Vector<AulaUdienzaModel> ExRicercaAula(AulaUdienzaModel aAula) throws F3BException {

		Connection lConn = null;
		Vector<AulaUdienzaModel> lAule = new Vector<>();
		AulaSqlDAO lAulaDao = null;

		try {
			lConn = getDBConnection();
			lAulaDao = new AulaSqlDAO(lConn);
			lAulaDao.ricercaAula(aAula);
			lAule = new Vector<AulaUdienzaModel>(lAulaDao.getModels());

			if (lAule.size() == 0)
				throw new F3BException(F3BException.USER_MESSAGE, "Nessun Elemento trovato");
		} catch (DAOException daoEx) {
			throw new F3BException("AulaController.ExRicercaAula: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lAulaDao);
			cleanup(lConn);
		}

		return lAule;
	}

	/**
	 * Numero dei record occorsi.
	 * <p>
	 *
	 * @param aAula
	 * @return
	 * @throws F3BException
	 */
	public int ExGetNumRicercaAula(AulaUdienzaModel aAula) throws F3BException {

		Connection lConn = null;
		AulaSqlDAO lAulaDao = null;

		int lNum = 0;

		try {
			lConn = getDBConnection();
			lAulaDao = new AulaSqlDAO(lConn);
			lAulaDao.ricercaAula(aAula);
			lNum = lAulaDao.getNumRowsSelected().intValue();
		} catch (DAOException daoEx) {
			throw new F3BException("AulaController.ExGetNumRicercaAula: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lAulaDao);
			cleanup(lConn);
		}
		return lNum;
	}

	/**
	 * Esegue la cancellazione dell'aula.
	 * <p>
	 *
	 * @param idAula
	 * @param idSezione
	 * @throws F3BException
	 */
	public void ExCancellaAula(BigDecimal idAula, BigDecimal idSezione) throws F3BException {

		Connection lConn = null;
		AulaDAO lAulaDao = null;
		UdienzaSigeSqlDAO lUdiSigeSqlDao = null;

		try {
			lConn = getDBConnection();

			// Controllo collegamento ad UDIENZA.
			lUdiSigeSqlDao = new UdienzaSigeSqlDAO(lConn);
			lUdiSigeSqlDao.countUdienzaSezioneAula(idAula, idSezione);
			lUdiSigeSqlDao.start();
			lUdiSigeSqlDao.next();

			// Preleva la count e verifica che sia == a zero.
			if (lUdiSigeSqlDao.getInt("COUNT") != 0)
				throw new SIGEException(SICOException.USER_MESSAGE,
						"Cancellazione impossibile: L'Aula è associata ad una Udienza.");

			// Cancellazione Aula
			lAulaDao = new AulaDAO(lConn);
			lAulaDao.setCondizioneUpdate(idAula, idSezione);
			lAulaDao.delete();

			commit(lConn);
		} catch (SIGEException SigeEx) {
			rollback(lConn);
			throw SigeEx;
		} catch (Exception ex) {
			rollback(lConn);
			throw new SIGEException("AulaController.ExCancellaAula: " + ex);
		} finally {
			cleanup(lAulaDao);
			cleanup(lUdiSigeSqlDao);
			cleanup(lConn);
		}
	}

	/**
	 * Metodo che esegue la modifica dei dati di un'Aula.
	 * <p>
	 *
	 * @param aAula
	 *            Model AulaUdienza.
	 * @return model dell'Aula di ritorno.
	 * @throws F3BException
	 *             propaga errore di eccezione.
	 */
	public AulaUdienzaModel ExModificaAula(AulaUdienzaModel aAula) throws F3BException {

		Connection lConn = null;
		AulaDAO lAulaDao = null;
		AulaUdienzaModel lAulaMod = new AulaUdienzaModel(aAula);

		try {
			lConn = getDBConnection();
			lAulaDao = new AulaDAO(lConn);
			lAulaDao.setDAOFromModelForUpdate(aAula);
			lAulaDao.update();

			commit(lConn);
		} catch (DAOException daoex) {
			rollback(lConn);
			throw new F3BException("AulaController.ExModificaAula: Non posso modificare : " + daoex);
		} catch (Exception ex) {
			rollback(lConn);
			throw new F3BException("AulaController.ExModificaAula: Non posso modificare : " + ex);
		} finally {
			cleanup(lAulaDao);
			cleanup(lConn);
		}

		return lAulaMod;
	}

	@Override
	public AulaUdienzaModel ExRicercaAulaPredefinitaSezione(String idSezione) throws F3BException {

		Connection lConn = null;
		AulaSqlDAO lAulaDao = null;
		AulaUdienzaModel lAulaMod;

		try {
			lConn = getDBConnection();
			lAulaDao = new AulaSqlDAO(lConn);
			lAulaDao.ricercaAulaPredefinitaSezione(idSezione);
			lAulaMod = (AulaUdienzaModel) lAulaDao.getModelByKey();

		} catch (DAOException daoEx) {
			throw new F3BException(
					"AulaController.ExRicercaAulaByDescrizione : Non posso leggere : " + daoEx);
		} finally {
			cleanup(lAulaDao);
			cleanup(lConn);
		}
		return lAulaMod;
	}

	@Override
	public AulaUdienzaModel ExRicercaAulaByIdAula(BigDecimal idAula) throws F3BException {

		Connection lConn = null;
		AulaSqlDAO lAulaDao = null;
		AulaUdienzaModel lAulaMod;

		try {
			lConn = getDBConnection();
			lAulaDao = new AulaSqlDAO(lConn);
			lAulaDao.ricercaAulaById(idAula);
			lAulaMod = (AulaUdienzaModel) lAulaDao.getModelByKey();
		} catch (DAOException daoEx) {
			throw new F3BException(
					"AulaController.ExRicercaAulaByDescrizione : Non posso leggere : " + daoEx);
		} finally {
			cleanup(lAulaDao);
			cleanup(lConn);
		}
		return lAulaMod;
	}

}