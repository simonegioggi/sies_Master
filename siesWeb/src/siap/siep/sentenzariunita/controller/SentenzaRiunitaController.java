package siap.siep.sentenzariunita.controller;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.dao.DAOException;
import f3b.log.LogF3B;
import f3b.util.F3BException;
//fine modifica marzo 2010
import siap.controller.SiapController;
import siap.siep.sentenzariunita.dao.SentenzaRiunitaDAO;
import siap.siep.sentenzariunita.dao.SentenzaRiunitaFascSiepDAO;
import siap.siep.sentenzariunita.dao.SentenzaRiunitaFascSiepSqlDAO;
import siap.siep.sentenzariunita.dao.SentenzaRiunitaSqlDAO;
// inizio modifica marzo 2010
import siap.siep.sentenzariunita.model.SentenzaRiunitaFascSiepModel;
import siap.siep.sentenzariunita.model.SentenzaRiunitaModel;

/**
 * <p>
 * Title: SentenzaRiunitaController
 * </p>
 * <p>
 * Description: Classe Controller per SentenzaRiunita
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
public class SentenzaRiunitaController extends SiapController implements ISentenzaRiunita {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public SentenzaRiunitaModel ExInserisciSentenzaRiunita(SentenzaRiunitaModel aSentenzaRiunita)
			throws F3BException {

		Connection lConn = null;
		SentenzaRiunitaDAO lSenDao = null;
		SentenzaRiunitaModel lSenMod = null;

		try {
			lConn = getDBConnection();
			lSenMod = new SentenzaRiunitaModel(aSentenzaRiunita);
			lSenDao = new SentenzaRiunitaDAO(lConn);
			lSenDao.setDAOFromModel(aSentenzaRiunita);
			BigDecimal lKey = null;
			lKey = lSenDao.insert();
			commit(lConn);
			lSenMod.setIdSentenzaRiunita(lKey);
		} catch (DAOException ex) {
			rollback(lConn);
			throw new F3BException("SentenzaRiunitaController.ExInserisci: Non posso inserire: " + ex);
		} finally {
			cleanup(lSenDao);
			cleanup(lConn);
		}
		return lSenMod;
	}

	public Vector ExRicercaSentenzaRiunita(SentenzaRiunitaModel aSentenzaRiunita) throws F3BException {

		Connection lConn = null;
		Vector lSentenzaRiuniti = new Vector();
		SentenzaRiunitaSqlDAO lSenDao = null;

		try {
			lConn = getDBConnection();
			lSenDao = new SentenzaRiunitaSqlDAO(lConn);
			lSenDao.ricercaSentenzaRiunita(aSentenzaRiunita);
			lSentenzaRiuniti = new Vector(lSenDao.getModels());
			if (lSentenzaRiuniti.size() == 0) {
				throw new F3BException(F3BException.USER_MESSAGE, "Nessun Elemento trovato");
			}
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException(
					"SentenzaRiunitaController.ExRicercaSentenzaRiunita: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lSenDao);
			cleanup(lConn);
		}
		return lSentenzaRiuniti;
	}

	public SentenzaRiunitaModel ExRicercaSentenzaRiunitaByKey(BigDecimal aKey) throws F3BException {

		Connection lConn = null;
		SentenzaRiunitaSqlDAO lSenDao = null;
		SentenzaRiunitaModel lSenMod;

		try {
			lConn = getDBConnection();
			lSenDao = new SentenzaRiunitaSqlDAO(lConn);
			lSenDao.ricercaSentenzaRiunitaByKey(aKey);
			lSenMod = (SentenzaRiunitaModel) lSenDao.getModelByKey();
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException(
					"SentenzaRiunitaController.ExRicercaSentenzaRiunita: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lSenDao);
			cleanup(lConn);
		}
		return lSenMod;
	}

	public SentenzaRiunitaModel ExModificaSentenzaRiunita(SentenzaRiunitaModel aSentenzaRiunita)
			throws F3BException {

		Connection lConn = null;
		SentenzaRiunitaDAO lSenDao = null;
		SentenzaRiunitaModel lSenMod = new SentenzaRiunitaModel(aSentenzaRiunita);

		try {
			lConn = getDBConnection();
			lSenDao = new SentenzaRiunitaDAO(lConn);
			lSenDao.setDAOFromModelForUpdate(aSentenzaRiunita);
			// lSenDao.selCondizioneUpdate(aSentenzaRiunita.getIdSentenzaRiunita());
			lSenDao.update();
			commit(lConn);
		} catch (DAOException ex) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + ex);
			throw new F3BException("SentenzaRiunitaController.ExModifica: Non posso inserire: " + ex);
		} finally {
			cleanup(lSenDao);
			cleanup(lConn);
		}
		return lSenMod;
	}

	public void ExCancellaSentenzaRiunita(SentenzaRiunitaModel aSentenzaRiunita) throws F3BException {

		Connection lConn = null;
		SentenzaRiunitaDAO lSenDao = null;

		try {
			lConn = getDBConnection();
			lSenDao = new SentenzaRiunitaDAO(lConn);
			lSenDao.selCondizioneUpdate(aSentenzaRiunita.getIdSentenzaRiunita());
			lSenDao.delete();
			commit(lConn);
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException(
					"SentenzaRiunitaController.ExCancellaSentenzaRiunita: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lSenDao);
			cleanup(lConn);
		}
	}

	// inizio modifica marzo 2010
	public SentenzaRiunitaModel ExInserisciSentenzaRiunitaFascicoloSiep(SentenzaRiunitaModel aSentenzaRiunita,
			SentenzaRiunitaFascSiepModel aSentenzaRiunitaFasSiep) throws F3BException {

		Connection lConn = null;
		SentenzaRiunitaDAO lSenDao = null;
		SentenzaRiunitaModel lSenMod = null;

		SentenzaRiunitaFascSiepDAO lSenFascDao = null;
		// SentenzaRiunitaFascSiepModel lSenFascMod = null;

		try {
			lConn = getDBTransaction();
			lSenMod = new SentenzaRiunitaModel(aSentenzaRiunita);
			lSenDao = new SentenzaRiunitaDAO(lConn);
			lSenDao.setDAOFromModel(aSentenzaRiunita);
			BigDecimal lKey = null;
			lKey = lSenDao.insert();

			lSenMod.setIdSentenzaRiunita(lKey);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("lSenMod.id= " + lSenMod.getIdSentenzaRiunita());

			// inserisco il record di relazione col fascicolo
			aSentenzaRiunitaFasSiep.setSenRiuIdSentenzaRiunita(lKey);
			// lSenFascMod = new SentenzaRiunitaFascSiepModel(aSentenzaRiunitaFasSiep);

			lSenFascDao = new SentenzaRiunitaFascSiepDAO(lConn);
			lSenFascDao.setDAOFromModel(aSentenzaRiunitaFasSiep);
			// BigDecimal lfascKey = null;
			/* lfascKey = */lSenFascDao.insert();

			commit(lConn);
		} catch (DAOException ex) {
			rollback(lConn);
			throw new F3BException("SentenzaRiunitaController.ExInserisci: Non posso inserire: " + ex);
		} finally {
			cleanup(lSenDao);
			cleanup(lSenFascDao);
			cleanup(lConn);
		}
		return lSenMod;
	}

	public Vector ExRicercaSentenzaRiunitaFascSiep(SentenzaRiunitaFascSiepModel aSentenzaRiunita)
			throws F3BException {

		Connection lConn = null;
		Vector lSentenzaRiuniti = new Vector();
		SentenzaRiunitaFascSiepSqlDAO lSenDao = null;

		try {
			lConn = getDBConnection();
			lSenDao = new SentenzaRiunitaFascSiepSqlDAO(lConn);
			lSenDao.ricercaSentenzaRiunitaFascSiep(aSentenzaRiunita);
			lSentenzaRiuniti = new Vector(lSenDao.getModels());
			if (lSentenzaRiuniti.size() == 0) {
				throw new F3BException(F3BException.USER_MESSAGE, "Nessun Elemento trovato");
			}
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException(
					"SentenzaRiunitaController.ExRicercaSentenzaRiunita: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lSenDao);
			cleanup(lConn);
		}
		return lSentenzaRiuniti;
	}

	public void ExAggiornaSentenzaRiunitaFascicolo(SentenzaRiunitaFascSiepModel aSentenzaRiunitaFasSiep,
			int modo) throws F3BException {

		// BigDecimal lfascKey = null;
		Connection lConn = null;
		SentenzaRiunitaFascSiepDAO lSenFascDao = null;
		SentenzaRiunitaFascSiepModel lSenFascMod = null;

		try {
			lConn = getDBConnection();
			// inserisco il record di relazione col fascicolo
			lSenFascMod = new SentenzaRiunitaFascSiepModel(aSentenzaRiunitaFasSiep);

			lSenFascDao = new SentenzaRiunitaFascSiepDAO(lConn);
			lSenFascDao.setDAOFromModel(aSentenzaRiunitaFasSiep);

			if (modo == 1)
				/* lfascKey = */lSenFascDao.insert();
			if (modo == 2) {
				lSenFascDao.setCondizioneUpdate(aSentenzaRiunitaFasSiep.getIdSentenzaRiunitaFascSiep());
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
				siesLogger.error(
						"CHIAVE da CANCELLARE=" + aSentenzaRiunitaFasSiep.getIdSentenzaRiunitaFascSiep());
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
				siesLogger.error("2 CHIAVE da CANCELLARE=" + lSenFascMod.getIdSentenzaRiunitaFascSiep());
				lSenFascDao.delete();
			}
			commit(lConn);
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException(
					"SentenzaRiunitaController.ExRicercaSentenzaRiunita: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lSenFascDao);
			cleanup(lConn);
		}
	}

	/**
	 * Restituisce le Sentenze Riunite associate al fascicolo Siep con id passato in input
	 *
	 * @param aIdFascicoloSiep
	 * @return Vector <SentenzaRiunitaFascSiepModel>
	 * @throws F3BException
	 */
	public Vector<SentenzaRiunitaFascSiepModel> ExRicercaSentenzeRiuniteByIdFascSiep(
			BigDecimal aIdFascicoloSiep) throws F3BException {

		Connection lConn = null;
		Vector<SentenzaRiunitaFascSiepModel> lListaSentenzeRiunite = new Vector<>();
		SentenzaRiunitaFascSiepSqlDAO lSenSqlDao = null;

		try {
			lConn = getDBConnection();

			lSenSqlDao = new SentenzaRiunitaFascSiepSqlDAO(lConn);

			lSenSqlDao.ricercaSentenzaRiunitaFascSiepByIdFasciolo(aIdFascicoloSiep);

			lListaSentenzeRiunite = new Vector<SentenzaRiunitaFascSiepModel>(lSenSqlDao.getModels());
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: ", daoEx);
			throw new F3BException(
					"SentenzaRiunitaController.ExRicercaSentenzeRiuniteByIdFascSiep: Non posso leggere : "
							+ daoEx);
		} finally {
			cleanup(lSenSqlDao);
			cleanup(lConn);
		}
		return lListaSentenzeRiunite;
	}

	/**
	 * Inserisci i records di Sentenza Riunita per JMS senza assegnare la sequence
	 *
	 * @param aSentenzeRiunite
	 * @param lConn
	 * @return lCodEsito
	 * @throws F3BException
	 */
	public String ExInserisciSentenzaRiunitaWithoutSequence(ArrayList aSentenzeRiunite, Connection lConn)
			throws F3BException {

		String lCodEsito = "00000";

		SentenzaRiunitaFascSiepDAO lSentRiunitFasSiepDAO = null;
		SentenzaRiunitaDAO lSentRiunitDAO = null;

		SentenzaRiunitaFascSiepModel lSentRiunitaFascSiepModel = null;
		// SentenzaRiunitaModel lSentRiunitaModel = null;

		try {
			lSentRiunitDAO = new SentenzaRiunitaDAO(lConn);
			lSentRiunitFasSiepDAO = new SentenzaRiunitaFascSiepDAO(lConn);

			if (aSentenzeRiunite != null && aSentenzeRiunite.size() > 0) {
				for (int i = 0; i < aSentenzeRiunite.size(); i++) {
					lSentRiunitaFascSiepModel = (SentenzaRiunitaFascSiepModel) aSentenzeRiunite.get(i);

					if (lSentRiunitaFascSiepModel != null) {
						if (lSentRiunitaFascSiepModel.getSentenzaRiunitaModel() != null) {
							// Inserisco SENTENZA_RIUNITA
							lSentRiunitDAO
									.setDAOFromModel(lSentRiunitaFascSiepModel.getSentenzaRiunitaModel());
							lSentRiunitDAO.setWithoutSequence(true);
							lSentRiunitDAO.insert();
							lSentRiunitDAO.stop();

							// Inserisco SENTENZARIUNITA_FASC_SIEP
							lSentRiunitFasSiepDAO.setIdSentenzaRiunitaFascSiep(
									lSentRiunitaFascSiepModel.getIdSentenzaRiunitaFascSiep());
							lSentRiunitFasSiepDAO.setSenRiuIdSentenzaRiunita(
									lSentRiunitaFascSiepModel.getSenRiuIdSentenzaRiunita());
							lSentRiunitFasSiepDAO.setFasSieIdFascicoloSiep(
									lSentRiunitaFascSiepModel.getFasSieIdFascicoloSiep());

							lSentRiunitFasSiepDAO.setWithoutSequence(true);
							lSentRiunitFasSiepDAO.insert();
							lSentRiunitFasSiepDAO.stop();

							// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al
							// posto di LogF3B.getLogger()
							siesLogger.info("=========> SENTENZARIUNITA_FASC_SIEP scritta----->");
						}
					}
				}
			}
		} catch (DAOException ex) {
			if (ex.UNIQUE_CONSTRAINT_VIOLATED) {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
				siesLogger.error("Sentenza Riunita gia' presente...");
				lCodEsito = "00001";
			} else {
				lCodEsito = "01400";
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.error("Errore in fase di inserimento sentenze riunite:", ex);
				throw new F3BException(F3BException.USER_MESSAGE, "Impossibile inserire Sentenza Riunita! ");
			}
		} finally {
			cleanup(lSentRiunitFasSiepDAO);
			cleanup(lSentRiunitDAO);
		}
		return lCodEsito;
	}

}