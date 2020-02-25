package siap.sico.magistratocompetente.controller;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Vector;

import org.apache.log4j.Logger;

import siap.controller.SiapController;
import siap.sico.magistrato.dao.MagistratoSqlDAO;
import siap.sico.magistratocompetente.dao.MagistratoCompetenteDAO;
import siap.sico.magistratocompetente.dao.MagistratoCompetenteMagistratoSqlDAO;
import siap.sico.magistratocompetente.dao.MagistratoCompetenteSqlDAO;
import siap.sico.magistratocompetente.model.MagistratoCompetenteMagistratoModel;
import siap.sico.magistratocompetente.model.MagistratoCompetenteModel;
import f3b.dao.DAOException;
import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.F3BException;

/**
 * <p>
 * Title: MagistratoCompetenteController
 * </p>
 * <p>
 * Description: Classe Controller per MagistratoCompetente
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
public class MagistratoCompetenteController extends SiapController implements IMagistratoCompetente {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public MagistratoCompetenteModel ExInserisciMagistratoCompetente(
			MagistratoCompetenteModel aMagistratoCompetente) throws F3BException {
		Connection lConn = null;

		MagistratoCompetenteDAO lMagDao = null;
		MagistratoCompetenteModel lMagMod = null;

		try {
			lConn = getDBConnection();

			lMagMod = new MagistratoCompetenteModel(aMagistratoCompetente);
			lMagDao = new MagistratoCompetenteDAO(lConn);
			lMagDao.setDAOFromModel(aMagistratoCompetente);
//			BigDecimal lKey = null;
			/*lKey = */lMagDao.insert();
			commit(lConn);
			// lMagMod.setIdMagistratoCompetente(lKey);
		} catch (DAOException ex) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("DAOException: " + ex);
			throw new F3BException("MagistratoCompetenteController.ExInserisciMagistratoCompetente: " + ex);
		} finally {
			cleanup(lMagDao);
			cleanup(lConn);
		}

		return lMagMod;
	}

	public Vector ExRicercaMagistratoCompetente(MagistratoCompetenteModel aMagistratoCompetente)
			throws F3BException {
		Connection lConn = null;

		Vector lMagistratoCompetenti = new Vector();
		MagistratoCompetenteSqlDAO lMagDao = null;

		try {
			lConn = getDBConnection();

			lMagDao = new MagistratoCompetenteSqlDAO(lConn);
			lMagDao.ricercaMagistratoCompetente(aMagistratoCompetente);
			lMagistratoCompetenti = new Vector(lMagDao.getModels());

			if (lMagistratoCompetenti.size() == 0) {
				throw new F3BException(F3BException.USER_MESSAGE, "Nessun Elemento trovato");
			}
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException("MagistratoCompetenteController.ExRicercaMagistratoCompetente: " + daoEx);
		} finally {
			cleanup(lMagDao);
			cleanup(lConn);
		}

		return lMagistratoCompetenti;
	}

	public MagistratoCompetenteMagistratoModel ExRicercaMagistratoCompetenteByFascicolo(BigDecimal aKey)
			throws F3BException {
		Connection lConn = null;

		MagistratoCompetenteMagistratoModel lModel = new MagistratoCompetenteMagistratoModel();
		MagistratoCompetenteMagistratoSqlDAO lMagDao = null;

		try {
			lConn = getDBConnection();
			lMagDao = new MagistratoCompetenteMagistratoSqlDAO(lConn);

			lMagDao.ricercaMagistratoCompetenteByFascicolo(aKey);
			// lMagDao.(aKey);
			lModel = ((MagistratoCompetenteMagistratoModel) lMagDao.getModelByKey());
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException(
					"MagistratoCompetenteController.ExRicercaMagistratoCompetenteByFascicolo: " + daoEx);
		} finally {
			cleanup(lMagDao);
			cleanup(lConn);
		}

		return lModel;
	}

	public MagistratoCompetenteMagistratoModel ExRicercaMagistratoCompetenteByFascicoloDataFine(
			BigDecimal aKey) throws F3BException {
		Connection lConn = null;

		MagistratoCompetenteMagistratoModel lModel = new MagistratoCompetenteMagistratoModel();
		MagistratoCompetenteMagistratoSqlDAO lMagDao = null;

		try {
			lConn = getDBConnection();
			lMagDao = new MagistratoCompetenteMagistratoSqlDAO(lConn);

			lMagDao.ricercaMagistratoCompetenteByFascicoloDataFine(aKey);
			// lMagDao.(aKey);
			lModel = ((MagistratoCompetenteMagistratoModel) lMagDao.getModelByKey());
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException(
					"MagistratoCompetenteController.ExRicercaMagistratoCompetenteByFascicoloDataFine: "
							+ daoEx);
		} finally {
			cleanup(lMagDao);
			cleanup(lConn);
		}

		return lModel;
	}

	public MagistratoCompetenteModel ExRicercaMagistratoCompetenteByKey(BigDecimal aKey) throws F3BException {
		Connection lConn = null;

		MagistratoCompetenteSqlDAO lMagDao = null;
		MagistratoCompetenteModel lMagMod;

		try {
			lConn = getDBConnection();
			lMagDao = new MagistratoCompetenteSqlDAO(lConn);
			lMagDao.ricercaMagistratoCompetenteByKey(aKey);
			lMagMod = (MagistratoCompetenteModel) lMagDao.getModelByKey();
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException("MagistratoCompetenteController.ExRicercaMagistratoCompetenteByKey: "
					+ daoEx);
		} finally {
			cleanup(lMagDao);
			cleanup(lConn);
		}

		return lMagMod;
	}

	public MagistratoCompetenteModel ExModificaMagistratoCompetente(
			MagistratoCompetenteModel aMagistratoCompetente) throws F3BException {
		Connection lConn = null;

		MagistratoCompetenteDAO lMagDao = null;
		MagistratoCompetenteModel lMagMod = new MagistratoCompetenteModel(aMagistratoCompetente);

		try {
			lConn = getDBConnection();

			lMagDao = new MagistratoCompetenteDAO(lConn);
			lMagDao.setDAOFromModelForUpdate(aMagistratoCompetente);
			lMagDao.update();
			commit(lConn);
		} catch (DAOException ex) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("DAOException: " + ex);
			throw new F3BException("MagistratoCompetenteController.ExModificaMagistratoCompetente: " + ex);
		} finally {
			cleanup(lMagDao);
			cleanup(lConn);
		}

		return lMagMod;
	}

	public void ExCancellaMagistratoCompetente(MagistratoCompetenteModel aMagistratoCompetente)
			throws F3BException {
		Connection lConn = null;

		MagistratoCompetenteDAO lMagDao = null;

		try {
			lConn = getDBConnection();
			lMagDao = new MagistratoCompetenteDAO(lConn);
			// lMagDao.setCondizioneUpdate(aMagistratoCompetente.getIdMagistratoCompetente());
			lMagDao.delete();
			commit(lConn);
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException("MagistratoCompetenteController.ExCancellaMagistratoCompetente: " + daoEx);
		} finally {
			cleanup(lMagDao);
			cleanup(lConn);
		}
	}

	public MagistratoCompetenteModel ExInserisciAggiornaMagistratoCompetente(
			MagistratoCompetenteMagistratoModel aMagistratoCompetenteMagistrato) throws F3BException {
		Connection lConn = null;

		MagistratoCompetenteDAO lMagCompDao = null;
		MagistratoCompetenteModel lMagMod = null;
		MagistratoCompetenteModel lNuovoMagMod = null;
		// Vector lMagistrati = new Vector();
		MagistratoSqlDAO lMagDao = null;
		MagistratoCompetenteSqlDAO lMagSqlDAO = null;

		try {
			lConn = getDBTransaction();

			// se non è stata richiesta nessuna variazione
			// controllo spostato nella jsp
			// if(aMagistratoCompetenteMagistrato.getMagistrato().getCodMagistrato().equals(aMagistratoCompetenteMagistrato.getMagistratoCompetente().getMagCodMagistrato()))
			// {
			// throw new F3BException(F3BException.USER_MESSAGE,"Nessuna Variazione Richiesta");
			// }

			// ricerca magistrato esistenete

			lMagSqlDAO = new MagistratoCompetenteSqlDAO(lConn);
			lMagSqlDAO.ricercaMagistratoEsistente(aMagistratoCompetenteMagistrato);
			lMagMod = (MagistratoCompetenteModel) lMagSqlDAO.getModelByKey();
			lMagCompDao = new MagistratoCompetenteDAO(lConn);
			if (lMagMod != null) {
				// chiude il magistrato precedente
				lMagCompDao.setDataFine(DateUtils.getSysDate());
				lMagCompDao.setCodOperatoreAggiornamento(aMagistratoCompetenteMagistrato
						.getMagistratoCompetente().getCodOperatoreInserimento());
				lMagCompDao.setCodUfficioAggiornamento(aMagistratoCompetenteMagistrato
						.getMagistratoCompetente().getCodUfficioInserimento());
				lMagCompDao.setDataAggiornamento(DateUtils.getSysDate());
				lMagCompDao.setCondizioneUpdate(lMagMod.getFasSieIdFascicoloSiep(),
						lMagMod.getMagCodMagistrato());
				lMagCompDao.update();
				lMagCompDao.stop();
			}

			lMagCompDao.setDAOFromModel(aMagistratoCompetenteMagistrato.getMagistratoCompetente());
			lMagCompDao.insert();

			lNuovoMagMod = aMagistratoCompetenteMagistrato.getMagistratoCompetente();

			// BigDecimal lKey = null;
			// lKey = lMagDao.insert();

			commit(lConn);
		} catch (DAOException ex) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("DAOException: " + ex);

			if (ex.getMessage().indexOf("MAG_COM_PK") != -1)
				throw new F3BException(F3BException.USER_MESSAGE,
						"Impossibile inserire il Magistrato Competente! Esiste un Magistrato per lo stesso numero SIEP");

			throw new F3BException("MagistratoCompetenteController.ExInserisciAggiornaMagistratoCompetente: "
					+ ex);
		} finally {
			cleanup(lMagCompDao);
			cleanup(lMagDao);
			cleanup(lMagSqlDAO);
			cleanup(lConn);
		}
		return lNuovoMagMod;
	}

	public String ExInserisciMagistratoCompetenteWithoutSequence(BigDecimal aIdFascicolo,
			MagistratoCompetenteMagistratoModel aMagCompetente, Connection lConn) throws F3BException {

		String lCodEsito = "00000";
		MagistratoCompetenteDAO lMagCompDao = null;
		MagistratoCompetenteModel lMagCompMod = null;
		try {
			lMagCompDao = new MagistratoCompetenteDAO(lConn);

			if (aMagCompetente.getMagistratoCompetente() != null
					&& aMagCompetente.getMagistratoCompetente().getMagCodMagistrato() != null) {
				lMagCompMod = aMagCompetente.getMagistratoCompetente();
				lMagCompMod.setMagCodMagistrato(aMagCompetente.getMagistrato().getCodMagistrato());
				lMagCompMod.setFasSieIdFascicoloSiep(aIdFascicolo);
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
				siesLogger.info(" Magistrato da inserire = " + lMagCompMod);
				lMagCompDao.setDAOFromModel(lMagCompMod);
				lMagCompDao.setWithoutSequence(true);
				lMagCompDao.insert();
				lMagCompDao.stop();
			}
		} catch (DAOException ex) {
			if (ex.UNIQUE_CONSTRAINT_VIOLATED) {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
				siesLogger.warn("Magistrato Competente gia' presente...");
				lCodEsito = "00001";
			} else {
				lCodEsito = "01400";
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
				siesLogger.error(F3BException.USER_MESSAGE
						+ " Impossibile inserire il Magistrato Competente! ");
			}
		} finally {
			cleanup(lMagCompDao);
		}
		return lCodEsito;
	}

	/**
   * 
   */
	public void ExModificaMultiplaMagistratoCompetente(MagistratoCompetenteModel aMagCompModel,
			String[] aListaFascicoli) throws F3BException {
		Connection lConn = null;

		MagistratoCompetenteDAO lMagCompDao = null;

		try {
			lConn = getDBConnection();

			// ========================================================================
			//
			// ========================================================================
			lMagCompDao = new MagistratoCompetenteDAO(lConn);
			for (int i = 0; i < aListaFascicoli.length; i++) {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("id_fascicolo = " + aListaFascicoli[i]);

				BigDecimal lIdFascicoloSiep = new BigDecimal(aListaFascicoli[i]);

				// ======================================================================
				// Aggiorno la data fine validità del vecchio magistrato
				// n.b. Il magistrato competente attuale è quello con DATA_FINE = null
				// ======================================================================

				lMagCompDao.setDataFine(DateUtils.getSysDate()); // '?????????????????????????????

				lMagCompDao.setCodOperatoreAggiornamento(aMagCompModel.getCodOperatoreInserimento());
				lMagCompDao.setCodUfficioAggiornamento(aMagCompModel.getCodUfficioInserimento());
				lMagCompDao.setDataAggiornamento(DateUtils.getSysDate());

				lMagCompDao.setCondizioneUpdateMagistratoCorrente(lIdFascicoloSiep);
				lMagCompDao.update();
				lMagCompDao.stop();

				// ======================================================================
				// Inserisco il nuovo magistrato
				// ======================================================================
				aMagCompModel.setFasSieIdFascicoloSiep(lIdFascicoloSiep);
				lMagCompDao.setDAOFromModel(aMagCompModel);
				lMagCompDao.insert();
				lMagCompDao.stop();
			}

			commit(lConn);
			// rollback(lConn);
		} catch (DAOException ex) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("DAOException: " + ex);
			throw new F3BException("MagistratoCompetenteController.ExModificaMultiplaMagistratoCompetente: "
					+ ex);
		} finally {
			cleanup(lMagCompDao);

			cleanup(lConn);
		}

		// return lMagMod;
	}

}