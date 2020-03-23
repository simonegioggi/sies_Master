package siap.siep.penaaccessoria.controller;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.dao.DAOException;
import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import siap.controller.SiapController;
import siap.siep.penaaccessoria.dao.PenaAccessoriaDAO;
import siap.siep.penaaccessoria.dao.PenaAccessoriaSqlDAO;
import siap.siep.penaaccessoria.model.PenaAccessoriaModel;
import siap.sige.penaaccessoria.dao.PenaAccSenSigeDAO;
import siap.sige.penaaccessoria.model.PenaAccSigeModel;

/**
 * <p>
 * Title: PenaAccessoriaController
 * </p>
 * <p>
 * Description: Classe Controller per PenaAccessoria
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
public class PenaAccessoriaController extends SiapController implements IPenaAccessoria {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public PenaAccessoriaModel ExInserisciPenaAccessoria(PenaAccessoriaModel aPenaAccessoria)
			throws F3BException {

		Connection lConn = null;
		PenaAccessoriaDAO lPenDao = null;

		try {
			lConn = getDBConnection();
			lPenDao = new PenaAccessoriaDAO(lConn);
			lPenDao.setDAOFromModel(aPenaAccessoria);
			aPenaAccessoria.setIdPenaAccessoria(lPenDao.insert());

			// Pena Accessoria SIGE
			if (aPenaAccessoria instanceof siap.sige.penaaccessoria.model.PenaAccSigeModel) {
				// Inserimento record di relazione in PENA_ACCESSORIA_SENTENZA_SIGE
				PenaAccSenSigeDAO lPenaAccSigeDAO = new PenaAccSenSigeDAO(lConn);
				lPenaAccSigeDAO.setDAOFromModel((PenaAccSigeModel) aPenaAccessoria);
				lPenaAccSigeDAO.insert();
				lPenaAccSigeDAO.stop();
				cleanup(lPenaAccSigeDAO);
			}

			commit(lConn);
			return aPenaAccessoria;
		} catch (DAOException ex) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + ex);
			throw new F3BException("PenaAccessoriaController.ExInserisci: Non posso inserire: " + ex);
		} catch (Exception sqe) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("Exception: " + sqe);
			throw new F3BException(
					"PenaAccessoriaController.ExInserisciPenaAccessoria: Non posso inserire -> " + sqe);
		} finally {
			cleanup(lPenDao);
			cleanup(lConn);
		}
	}

	/**
	 * Ricerca la PEna Accessoria
	 *
	 * @param aPenaAccessoria
	 * @return
	 * @throws F3BException
	 */
	public Vector ExRicercaPenaAccessoria(PenaAccessoriaModel aPenaAccessoria) throws F3BException {

		Connection lConn = null;
		Vector lPenaAccessori = new Vector();
		PenaAccessoriaSqlDAO lPenDao = null;

		try {
			lConn = getDBConnection();
			lPenDao = new PenaAccessoriaSqlDAO(lConn);
			lPenDao.ricercaPenaAccessoria(aPenaAccessoria);
			lPenaAccessori = new Vector(lPenDao.getModels());
			if (lPenaAccessori.size() == 0) {
				throw new F3BException(F3BException.USER_MESSAGE, "Nessun Elemento trovato");
			}
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException(
					"PenaAccessoriaController.ExRicercaPenaAccessoria: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lPenDao);
			cleanup(lConn);
		}
		return lPenaAccessori;
	}

	/**
	 * Ricerca la Pena Accessoria per la chiave
	 *
	 * @param aPenaAccessoria
	 * @return
	 * @throws F3BException
	 */
	public PenaAccessoriaModel ExRicercaPenaAccessoriaByKey(PenaAccessoriaModel aPenaAccessoria)
			throws F3BException {

		Connection lConn = null;
		PenaAccessoriaSqlDAO lPenDao = null;
		PenaAccessoriaModel lPenMod = null;

		try {
			lConn = getDBConnection();
			lPenDao = new PenaAccessoriaSqlDAO(lConn);
			lPenDao.ricercaPenaAccessoria(aPenaAccessoria);
			lPenMod = (PenaAccessoriaModel) lPenDao.getModelByKey();
			lPenDao.stop();
			// Recupero Numero Eventi Correlati.
			lPenDao = new PenaAccessoriaSqlDAO(lConn);
			lPenMod.setNumeroEventiCorrelati(lPenDao.getNumeroEventiCorrelati(lPenMod.getIdPenaAccessoria()));
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException(
					"PenaAccessoriaController.ExRicercaPenaAccessoria: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lPenDao);
			cleanup(lConn);
		}
		return lPenMod;
	}

	/**
	 * Modifica Pena Accessoria
	 *
	 * @param aPenaAccessoria
	 * @return
	 * @throws F3BException
	 */
	public PenaAccessoriaModel ExModificaPenaAccessoria(PenaAccessoriaModel aPenaAccessoria)
			throws F3BException {

		Connection lConn = null;
		PenaAccessoriaDAO lPenDao = null;

		try {
			lConn = getDBConnection();
			lPenDao = new PenaAccessoriaDAO(lConn);
			lPenDao.setDAOFromModelForUpdate(aPenaAccessoria);
			lPenDao.selCondizioneUpdate(aPenaAccessoria.getIdPenaAccessoria());
			lPenDao.update();
			commit(lConn);
		} catch (DAOException ex) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + ex);
			throw new F3BException("PenaAccessoriaController.ExModifica: " + ex);
		} finally {
			cleanup(lPenDao);
			cleanup(lConn);
		}
		return aPenaAccessoria;
	}

	/**
	 * MOdifica Ordinanza pena Accessoria
	 *
	 * @param aPenaAccessoria
	 * @return
	 * @throws F3BException
	 */
	public PenaAccessoriaModel ExModificaOrdinanzaPenaAccessoria(PenaAccessoriaModel aPenaAccessoria)
			throws F3BException {

		Connection lConn = null;
		PenaAccessoriaDAO lPenDao = null;

		try {
			lConn = getDBConnection();
			lPenDao = new PenaAccessoriaDAO(lConn);
			lPenDao.setDAOFromModelForUpdateOrdinanzaPA(aPenaAccessoria);
			lPenDao.selCondizioneUpdate(aPenaAccessoria.getIdPenaAccessoria());
			lPenDao.update();
			commit(lConn);
		} catch (DAOException ex) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + ex);
			throw new F3BException("PenaAccessoriaController.ExModificaOrdinanzaPenaAccessoria : " + ex);
		} finally {
			cleanup(lPenDao);
			cleanup(lConn);
		}
		return aPenaAccessoria;
	}

	/**
	 * Modifica Pena Accesoria
	 *
	 * @param aPenaAccessoria
	 * @return
	 * @throws F3BException
	 */
	public PenaAccessoriaModel ExModificaCodNuovoTipoPA(PenaAccessoriaModel aPenaAccessoria)
			throws F3BException {

		Connection lConn = null;
		PenaAccessoriaDAO lPenDao = null;

		try {
			lConn = getDBConnection();
			lPenDao = new PenaAccessoriaDAO(lConn);
			lPenDao.setDAOFromModelForUpdateCodNuovoTipoPA(aPenaAccessoria);
			lPenDao.selCondizioneUpdate(aPenaAccessoria.getIdPenaAccessoria());
			lPenDao.update();
			commit(lConn);
		} catch (DAOException ex) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + ex);
			throw new F3BException("PenaAccessoriaController.ExModificaCodNuovoTipoPA : " + ex);
		} finally {
			cleanup(lPenDao);
			cleanup(lConn);
		}
		return aPenaAccessoria;
	}

	/**
	 * Cancella Pena Accessoria
	 *
	 * @param aPenaAccessoria
	 * @throws F3BException
	 */
	public void ExCancellaPenaAccessoria(PenaAccessoriaModel aPenaAccessoria) throws F3BException {

		Connection lConn = null;
		PenaAccessoriaModel lPenMod = null;
		PenaAccessoriaDAO lPenDao = null;
		PenaAccessoriaSqlDAO lPenSqlDao = null;

		try {
			lConn = getDBConnection();
			lPenDao = new PenaAccessoriaDAO(lConn);
			lPenSqlDao = new PenaAccessoriaSqlDAO(lConn);
			boolean lEsisteEvCo = lPenSqlDao.ExistEventoCollegato(aPenaAccessoria.getIdPenaAccessoria());
			lPenSqlDao.stop();
			if (lEsisteEvCo)
				throw new F3BException(F3BException.USER_MESSAGE,
						"Pena Accessoria non cancellabile a causa dell'esistenza di Provvedimenti!");

			else {
				// Pena Accessoria SIGE
				if (aPenaAccessoria instanceof siap.sige.penaaccessoria.model.PenaAccSigeModel) {
					// Inserimento record di relazione in PENA_ACCESSORIA_SENTENZA_SIGE
					PenaAccSenSigeDAO lPenaAccSigeDAO = new PenaAccSenSigeDAO(lConn);
					lPenaAccSigeDAO.setCondizioneDeletePenaAccessoria(aPenaAccessoria.getIdPenaAccessoria());
					lPenaAccSigeDAO.delete();
					lPenaAccSigeDAO.stop();
					cleanup(lPenaAccSigeDAO);
				}

				lPenSqlDao.ricercaPenaAccessoriaByKey(aPenaAccessoria.getIdPenaAccessoria());
				lPenMod = (PenaAccessoriaModel) lPenSqlDao.getModelByKey();
				lPenSqlDao.stop();
				lPenDao.selCondizioneUpdate(aPenaAccessoria.getIdPenaAccessoria());
				lPenDao.delete();
				lPenDao.stop();
				// In caso di cancellazione di P.A. sostitutiva, aggiorna anche la P.A. Origine.
				if (lPenMod.getIdPenaAccessoriaOrigine() != null) {
					lPenSqlDao.ricercaPenaAccessoriaByKey(lPenMod.getIdPenaAccessoriaOrigine());
					lPenMod = (PenaAccessoriaModel) lPenSqlDao.getModelByKey();
					if (lPenMod != null) {
						lPenMod.setFlagCondonata("-");
						lPenMod.setCodNuovoTipoPenaAccessoria("-");
						lPenMod.setNote(
								lPenMod.getNote() + " - Cancellata Pena Accessoria sostitutiva in data "
										+ DateUtils.getSysDate("dd/MM/yyyy") + ".");
						lPenDao.setDAOFromModelForUpdate(lPenMod);
						lPenDao.selCondizioneUpdate(lPenMod.getIdPenaAccessoria());
						lPenDao.update();
					}
				}
				commit(lConn);
			}
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException(
					"PenaAccessoriaController.ExCancellaPenaAccessoria: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lPenDao);
			cleanup(lPenSqlDao);
			cleanup(lConn);
		}
	}

	public String ExInserisciPenaAccessoriaWithoutSequence(ArrayList aPene, Connection lConn)
			throws F3BException {

		String lCodEsito = "00000";
		PenaAccessoriaDAO lPenDao = null;

		try {
			lPenDao = new PenaAccessoriaDAO(lConn);

			if (aPene.size() > 0) {
				for (int i = 0; i < aPene.size(); i++) {
					lPenDao.setDAOFromModel((PenaAccessoriaModel) aPene.get(i));
					lPenDao.setWithoutSequence(true);
					lPenDao.insert();
					lPenDao.stop();
				}
			}
		} catch (DAOException ex) {
			if (ex.UNIQUE_CONSTRAINT_VIOLATED) {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
				siesLogger.error("Pene ccessorie gia' presente...");
				lCodEsito = "00001";
			} else {
				lCodEsito = "01400";
				throw new F3BException(F3BException.USER_MESSAGE,
						"Impossibile inserire le Pene Accessorie! ");
			}
		} finally {
			cleanup(lPenDao);
		}
		return lCodEsito;
	}

	/**
	 * Verifica l'esistenza della Pena Accessoria Sostitutiva
	 *
	 * @param aModel
	 * @return boolean
	 * @throws F3BException
	 */
	public boolean ExistPASostitutiva(BigDecimal aIdPenaAccessoria) throws F3BException {

		Connection lConn = null;
		boolean esiste = false;
		// PenaAccessoriaModel lFascicolo = null;
		PenaAccessoriaSqlDAO lPASqlDao = null;

		try {
			lConn = getDBConnection();

			lPASqlDao = new PenaAccessoriaSqlDAO(lConn);

			esiste = lPASqlDao.ExistPASostitutiva(aIdPenaAccessoria);
		} catch (DAOException dex) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("DAOException: " + dex);
			throw new F3BException(F3BException.USER_MESSAGE,
					"PenaAccessoriaController.ExistPASostitutiva: " + dex);
		} catch (Exception e) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("Exception: " + e);
			throw new F3BException(F3BException.USER_MESSAGE, e.getMessage());
		} finally {
			cleanup(lPASqlDao);

			cleanup(lConn);
		}

		return esiste;
	}

	/**
	 * ExRicercaPenaAccessoriaNoError non rilancia l'eccezione di Nessun elemento trovato Ricerca la PEna
	 * Accessoria
	 *
	 * @param aPenaAccessoria
	 * @return
	 * @throws F3BException
	 */
	public Vector ExRicercaPenaAccessoriaNoError(PenaAccessoriaModel aPenaAccessoria) throws F3BException {

		Connection lConn = null;
		Vector lPenaAccessori = new Vector();
		PenaAccessoriaSqlDAO lPenDao = null;

		try {
			lConn = getDBConnection();
			lPenDao = new PenaAccessoriaSqlDAO(lConn);
			lPenDao.ricercaPenaAccessoria(aPenaAccessoria);
			lPenaAccessori = new Vector(lPenDao.getModels());

		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException(
					"PenaAccessoriaController.ExRicercaPenaAccessoria: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lPenDao);
			cleanup(lConn);
		}
		return lPenaAccessori;
	}

}