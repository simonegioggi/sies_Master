package siap.siep.altrigradigiudizio.controller;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.dao.DAOException;
import f3b.log.LogF3B;
import f3b.util.F3BException;
import siap.controller.SiapController;
import siap.siep.agdgfascicolosiep.dao.AgdgFascicoloSiepDAO;
import siap.siep.agdgfascicolosiep.dao.AgdgFascicoloSiepSqlDAO;
import siap.siep.agdgfascicolosiep.model.AgdgFascicoloSiepModel;
import siap.siep.altrigradigiudizio.dao.AltriGradiGiudizioDAO;
import siap.siep.altrigradigiudizio.dao.AltriGradiGiudizioSqlDAO;
import siap.siep.altrigradigiudizio.model.AltriGradiGiudizioModel;
import siap.siep.sentenza.dao.SentenzaFascicoloSqlDAO;

/**
 * <p>
 * Title: AltriGradiGiudizioController
 * </p>
 * <p>
 * Description: Classe Controller per AltriGradiGiudizio
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
public class AltriGradiGiudizioController extends SiapController implements IAltriGradiGiudizio {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public AltriGradiGiudizioModel ExInserisciAltriGradiGiudizio(AltriGradiGiudizioModel aAltriGradiGiudizio)
			throws F3BException {

		Connection lConn = null;
		AltriGradiGiudizioDAO lAltDao = null;
		AltriGradiGiudizioModel lAltMod = null;

		try {
			lConn = getDBConnection();
			lAltMod = new AltriGradiGiudizioModel(aAltriGradiGiudizio);
			lAltDao = new AltriGradiGiudizioDAO(lConn);
			lAltDao.setDAOFromModel(aAltriGradiGiudizio);
			BigDecimal lKey = null;
			lKey = lAltDao.insert();
			commit(lConn);
			lAltMod.setIdAltrigradigiudizio(lKey);
		} catch (DAOException ex) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + ex);
			throw new F3BException("AltriGradiGiudizioController.ExInserisci: Non posso inserire: " + ex);
		} finally {
			cleanup(lAltDao);
			cleanup(lConn);
		}
		return lAltMod;
	}

	public Vector ExRicercaAltriGradiGiudizio(AltriGradiGiudizioModel aAltriGradiGiudizio)
			throws F3BException {

		Connection lConn = null;
		AltriGradiGiudizioSqlDAO lAltDao = null;

		Vector lAltriGradiGiudizii = new Vector();

		try {
			lConn = getDBConnection();
			lAltDao = new AltriGradiGiudizioSqlDAO(lConn);
			lAltDao.ricercaAltriGradiGiudizio(aAltriGradiGiudizio);
			lAltriGradiGiudizii = new Vector(lAltDao.getModels());
			if (lAltriGradiGiudizii.size() == 0) {
				throw new F3BException(F3BException.USER_MESSAGE, "Nessun Elemento trovato");
			}
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException(
					"AltriGradiGiudizioController.ExRicercaAltriGradiGiudizio: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lAltDao);
			cleanup(lConn);
		}
		return lAltriGradiGiudizii;
	}

	public AltriGradiGiudizioModel ExRicercaAltriGradiGiudizioByKey(BigDecimal aKey) throws F3BException {

		Connection lConn = null;
		AltriGradiGiudizioSqlDAO lAltDao = null;
		SentenzaFascicoloSqlDAO lFasSenDao = null;
		AltriGradiGiudizioModel lAltMod;

		try {
			lConn = getDBConnection();
			lAltDao = new AltriGradiGiudizioSqlDAO(lConn);
			lAltDao.ricercaAltriGradiGiudizioByKey(aKey);
			lAltMod = (AltriGradiGiudizioModel) lAltDao.getModelByKey();

			lFasSenDao = new SentenzaFascicoloSqlDAO(lConn);
			lFasSenDao.getCountFascicoli(lAltMod.getSenIdSentenza());
			lFasSenDao.start();
			lFasSenDao.next();
			int lCount = lFasSenDao.getInt("HowManyFascicoli");
			lFasSenDao.stop();
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.debug("FAscicoli trovati = " + lCount);
			if (lCount > 0)
				lAltMod.setEsistonoFascicoliAssociati(true);
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException(
					"AltriGradiGiudizioController.ExRicercaAltriGradiGiudizio: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lAltDao);
			// Scheda Intervento n° 6 - Ottimizzazione SIUS Avvocati
			cleanup(lFasSenDao);
			cleanup(lConn);
		}
		return lAltMod;
	}

	public AltriGradiGiudizioModel ExModificaAltriGradiGiudizio(AltriGradiGiudizioModel aAltriGradiGiudizio)
			throws F3BException {

		Connection lConn = null;
		AltriGradiGiudizioDAO lAltDao = null;
		AltriGradiGiudizioModel lAltMod = new AltriGradiGiudizioModel(aAltriGradiGiudizio);

		try {
			lConn = getDBConnection();
			lAltDao = new AltriGradiGiudizioDAO(lConn);
			lAltDao.setDAOFromModelForUpdate(aAltriGradiGiudizio);
			lAltDao.update();
			lAltDao.update();
			commit(lConn);
		} catch (DAOException ex) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + ex);
			throw new F3BException("AltriGradiGiudizioController.ExModifica: Non posso inserire: " + ex);
		} finally {
			cleanup(lAltDao);
			cleanup(lConn);
		}
		return lAltMod;
	}

	public void ExCancellaAltriGradiGiudizio(AltriGradiGiudizioModel aAltriGradiGiudizio)
			throws F3BException {

		Connection lConn = null;
		AltriGradiGiudizioDAO lAltDao = null;

		try {
			lConn = getDBConnection();
			lAltDao = new AltriGradiGiudizioDAO(lConn);
			lAltDao.setCondizioneUpdate(aAltriGradiGiudizio.getIdAltrigradigiudizio());
			lAltDao.delete();
			commit(lConn);
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException(
					"AltriGradiGiudizioController.ExCancellaAltriGradiGiudizio: Non posso leggere : "
							+ daoEx);
		} finally {
			cleanup(lAltDao);
			cleanup(lConn);
		}
	}

	public AltriGradiGiudizioModel ExInserisciAltriGradiGiudizioFascicoloSiep(
			AltriGradiGiudizioModel aAltriGradiGiudizio, AgdgFascicoloSiepModel aAltriGradiGiudizioFasSiep)
			throws F3BException {

		Connection lConn = null;
		AltriGradiGiudizioDAO lSenDao = null;
		AgdgFascicoloSiepDAO lSenFascDao = null;

		AltriGradiGiudizioModel lSenMod = null;

		try {
			lConn = getDBTransaction();
			lSenMod = new AltriGradiGiudizioModel(aAltriGradiGiudizio);
			lSenDao = new AltriGradiGiudizioDAO(lConn);
			lSenDao.setDAOFromModel(aAltriGradiGiudizio);
			BigDecimal lKey = null;
			lKey = lSenDao.insert();

			lSenMod.setIdAltrigradigiudizio(lKey);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("lAgdgMod.id= " + lSenMod.getIdAltrigradigiudizio());

			// inserisco il record di relazione col fascicolo
			aAltriGradiGiudizioFasSiep.setAgdgIdAltrigradigiudizio(lKey);
			// lSenFascMod = new AgdgFascicoloSiepModel(aAltriGradiGiudizioFasSiep);

			lSenFascDao = new AgdgFascicoloSiepDAO(lConn);
			lSenFascDao.setDAOFromModel(aAltriGradiGiudizioFasSiep);
			// BigDecimal lfascKey = null;
			/* lfascKey = */lSenFascDao.insert();

			commit(lConn);
		} catch (DAOException ex) {
			rollback(lConn);
			throw new F3BException("AltriGradiGiudizioController.ExInserisci: Non posso inserire: " + ex);
		} finally {
			cleanup(lSenDao);
			cleanup(lSenFascDao);
			cleanup(lConn);
		}
		return lSenMod;
	}

	public Vector ExRicercaAltriGradiGiudizioFascSiep(AgdgFascicoloSiepModel aAltriGradiGiudizio)
			throws F3BException {

		Connection lConn = null;
		AgdgFascicoloSiepSqlDAO lSenDao = null;

		Vector lAltriGradiGiudizio = new Vector();

		try {
			lConn = getDBConnection();
			lSenDao = new AgdgFascicoloSiepSqlDAO(lConn);
			lSenDao.ricercaAgdgFascicoloSiep(aAltriGradiGiudizio);
			lAltriGradiGiudizio = new Vector(lSenDao.getModels());
			if (lAltriGradiGiudizio.size() == 0) {
				throw new F3BException(F3BException.USER_MESSAGE, "Nessun Elemento trovato");
			}
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException(
					"AltriGradiGiudizioController.ExRicercaAltriGradiGiudizio: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lSenDao);
			cleanup(lConn);
		}
		return lAltriGradiGiudizio;
	}

	public void ExAggiornaAltriGradiGiudizioFascicolo(AgdgFascicoloSiepModel aAltriGradiGiudizioFasSiep,
			int modo) throws F3BException {

		// BigDecimal lfascKey = null;
		Connection lConn = null;
		AgdgFascicoloSiepDAO lSenFascDao = null;

		try {
			lConn = getDBTransaction();
			// inserisco il record di relazione col fascicolob

			lSenFascDao = new AgdgFascicoloSiepDAO(lConn);
			lSenFascDao.setDAOFromModel(aAltriGradiGiudizioFasSiep);

			if (modo == 1)
				/* lfascKey = */lSenFascDao.insert();
			if (modo == 2) {
				lSenFascDao.setCondizioneUpdate(aAltriGradiGiudizioFasSiep.getIdAgdgFascicoloSiep());
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
				siesLogger
						.info("CHIAVE da CANCELLARE=" + aAltriGradiGiudizioFasSiep.getIdAgdgFascicoloSiep());

				lSenFascDao.delete();
			}
			commit(lConn);
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException(
					"AltriGradiGiudizioController.ExAggiornaAltriGradiGiudizioFascicolo: Non posso leggere : "
							+ daoEx);
		} finally {
			cleanup(lSenFascDao);
			cleanup(lConn);
		}
	}

	/**
	 * Inserisci i records di Altri Gradi Giudizio per JMS senza assegnare la sequence
	 *
	 * @param aAltriGradiGiudizio
	 * @param lConn
	 * @return lCodEsito
	 * @throws F3BException
	 */
	public String ExInserisciAltriGradiGiudizioWithoutSequence(ArrayList aAltriGradiGiudizio,
			Connection lConn) throws F3BException {

		String lCodEsito = "00000";
		AgdgFascicoloSiepDAO lAgdgFasSiepDao = null;
		AltriGradiGiudizioDAO lAGDGDao = null;
		AgdgFascicoloSiepModel lAGDGFasSiepModel = null;
		// AltriGradiGiudizioModel lAltGraGiuModel = null;

		try {
			lAGDGDao = new AltriGradiGiudizioDAO(lConn);
			lAgdgFasSiepDao = new AgdgFascicoloSiepDAO(lConn);

			if (aAltriGradiGiudizio != null && aAltriGradiGiudizio.size() > 0) {
				for (int i = 0; i < aAltriGradiGiudizio.size(); i++) {
					lAGDGFasSiepModel = (AgdgFascicoloSiepModel) aAltriGradiGiudizio.get(i);
					if (lAGDGFasSiepModel != null) {
						if (lAGDGFasSiepModel.getAgdgIdAltrigradigiudizio() != null) {
							lAGDGDao.setDAOFromModel(lAGDGFasSiepModel.getAltriGradiGiudizioModel());
							lAGDGDao.setWithoutSequence(true);
							lAGDGDao.insert();
							lAGDGDao.stop();

							lAgdgFasSiepDao.setAgdgIdAltrigradigiudizio(
									lAGDGFasSiepModel.getAgdgIdAltrigradigiudizio());
							lAgdgFasSiepDao
									.setFasSieIdFascicoloSiep(lAGDGFasSiepModel.getFasSieIdFascicoloSiep());
							lAgdgFasSiepDao
									.setIdAgdgFascicoloSiep(lAGDGFasSiepModel.getIdAgdgFascicoloSiep());
							lAgdgFasSiepDao.setWithoutSequence(true);
							lAgdgFasSiepDao.insert();
							lAgdgFasSiepDao.stop();

							// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al
							// posto di LogF3B.getLogger()
							siesLogger.info("=========> ALTRI_GRADO_GIUDIZIO scritta----->");
							// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al
							// posto di LogF3B.getLogger()
							siesLogger.info("IdAltrigradigiudizio = "
									+ lAGDGFasSiepModel.getAgdgIdAltrigradigiudizio());
						}
					}
				}
			}
		} catch (DAOException ex) {
			if (ex.UNIQUE_CONSTRAINT_VIOLATED) {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
				siesLogger.error("Altri Gradi Giudizio gia' presente...");
				lCodEsito = "00001";
			} else {
				lCodEsito = "01400";
				throw new F3BException(F3BException.USER_MESSAGE,
						"Impossibile inserire Altri Gradi Giudizio! ");
			}
		} finally {
			cleanup(lAgdgFasSiepDao);
			cleanup(lAGDGDao);
		}
		return lCodEsito;
	}

}