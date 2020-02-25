package siap.sius.rifasiep.controller;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Vector;

import org.apache.log4j.Logger;

import siap.controller.SiapController;
import siap.siep.misurasicurezza.dao.MisuraSicurezzaDAO;
import siap.siep.misurasicurezza.dao.MisuraSicurezzaSqlDAO;
import siap.siep.misurasicurezza.model.MisuraSicurezzaModel;
import siap.sius.rifasiep.dao.RiferimentoFascicoloSiepDAO;
import siap.sius.rifasiep.dao.RiferimentoFascicoloSiepSqlDAO;
import siap.sius.rifasiep.model.RiferimentoFascicoloSiepModel;
import f3b.dao.DAOException;
import f3b.log.LogF3B;
import f3b.util.F3BException;

/**
 * <p>
 * Title: RiferimentoFascicoloSiepController
 * </p>
 * <p>
 * Description: Classe Controller per RiferimentoFascicoloSiep
 * </p>
 * <p>
 * Copyright: Copyright (c) 2004
 * </p>
 * <p>
 * Company: Bull
 * </p>
 * 
 * @version 1.0
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class RiferimentoFascicoloSiepController extends SiapController implements IRiferimentoFascicoloSiep {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public RiferimentoFascicoloSiepModel ExInserisciRiferimentoFascicoloSiep(
			RiferimentoFascicoloSiepModel aRiferimentoFascicoloSiep) throws F3BException {
		Connection lConn = null;
		RiferimentoFascicoloSiepDAO lRifDao = null;
		RiferimentoFascicoloSiepSqlDAO lRifSqlDao = null;
		RiferimentoFascicoloSiepModel lRifMod = null;
		try {
			lConn = getDBConnection();
			lRifMod = new RiferimentoFascicoloSiepModel(aRiferimentoFascicoloSiep);
			lRifDao = new RiferimentoFascicoloSiepDAO(lConn);
			lRifSqlDao = new RiferimentoFascicoloSiepSqlDAO(lConn);
			lRifSqlDao.ricercaRiferimentoFascicoloSiep(aRiferimentoFascicoloSiep);
			Vector lRiFaSiep = new Vector(lRifSqlDao.getModels());
			if (lRiFaSiep.size() == 0) {
				lRifDao.setDAOFromModel(aRiferimentoFascicoloSiep);
				BigDecimal lKey = null;
				lKey = lRifDao.insert();
				commit(lConn);
				lRifMod.setIdRiferimentoFascicoloSiep(lKey);
			} else {
			}
		} catch (DAOException ex) {
			rollback(lConn);
			throw new F3BException("RiferimentoFascicoloSiepController.ExInserisciRiferimentoFascicoloSiep: "
					+ ex);
		} finally {
			cleanup(lRifDao);
			cleanup(lRifSqlDao);
			cleanup(lConn);
		}
		return lRifMod;
	}

	public Vector ExRicercaRiferimentoFascicoloSiep(RiferimentoFascicoloSiepModel aRiferimentoFascicoloSiep)
			throws F3BException {
		Connection lConn = null;
		Vector lRiFaSiep = new Vector();
		RiferimentoFascicoloSiepSqlDAO lRifDao = null;
		try {
			lConn = getDBConnection();
			lRifDao = new RiferimentoFascicoloSiepSqlDAO(lConn);
			lRifDao.ricercaRiferimentoFascicoloSiep(aRiferimentoFascicoloSiep);
			lRiFaSiep = new Vector(lRifDao.getModels());
		} catch (DAOException daoEx) {
			throw new F3BException("RiferimentoFascicoloSiepController.ExRicercaRiferimentoFascicoloSiep: "
					+ daoEx);
		} finally {
			cleanup(lRifDao);
			cleanup(lConn);
		}
		return lRiFaSiep;
	}

	public RiferimentoFascicoloSiepModel ExRicercaRiferimentoFascicoloSiepByKey(BigDecimal aKey)
			throws F3BException {
		Connection lConn = null;
		RiferimentoFascicoloSiepSqlDAO lRifDao = null;
		RiferimentoFascicoloSiepModel lRifMod;
		try {
			lConn = getDBConnection();
			lRifDao = new RiferimentoFascicoloSiepSqlDAO(lConn);
			lRifDao.ricercaRiferimentoFascicoloSiepByKey(aKey);
			lRifMod = (RiferimentoFascicoloSiepModel) lRifDao.getModelByKey();
		} catch (DAOException daoEx) {
			throw new F3BException("RiferimentoFascicoloSiepController.ExRicercaRiferimentoFascicoloSiep: "
					+ daoEx);
		} finally {
			cleanup(lRifDao);
			cleanup(lConn);
		}
		return lRifMod;
	}

	public RiferimentoFascicoloSiepModel ExModificaRiferimentoFascicoloSiep(
			RiferimentoFascicoloSiepModel aRiferimentoFascicoloSiep) throws F3BException {
		Connection lConn = null;
		RiferimentoFascicoloSiepDAO lRifDao = null;
		RiferimentoFascicoloSiepModel lRifMod = new RiferimentoFascicoloSiepModel(aRiferimentoFascicoloSiep);
		try {
			lConn = getDBConnection();
			lRifDao = new RiferimentoFascicoloSiepDAO(lConn);
			lRifDao.setDAOFromModelForUpdate(aRiferimentoFascicoloSiep);
			lRifDao.update();
			lRifDao.update();
			commit(lConn);
		} catch (DAOException ex) {
			rollback(lConn);
			throw new F3BException("RiferimentoFascicoloSiepController.ExModifica: " + ex);
		} finally {
			cleanup(lRifDao);
			cleanup(lConn);
		}
		return lRifMod;
	}

	public void ExCancellaRiferimentoFascicoloSiep(RiferimentoFascicoloSiepModel aRiferimentoFascicoloSiep)
			throws F3BException {
		Connection lConn = null;
		RiferimentoFascicoloSiepDAO lRifDao = null;
		try {
			lConn = getDBConnection();
			lRifDao = new RiferimentoFascicoloSiepDAO(lConn);
			lRifDao.setCondizioneUpdate(aRiferimentoFascicoloSiep.getIdRiferimentoFascicoloSiep());
			lRifDao.delete();
			commit(lConn);
		} catch (DAOException daoEx) {
			throw new F3BException("RiferimentoFascicoloSiepController.ExCancellaRiferimentoFascicoloSiep: "
					+ daoEx);
		} finally {
			cleanup(lRifDao);
			cleanup(lConn);
		}
	}

	public void ExCancellaRiferimentoFascicoloSiep(BigDecimal idRiferimentoFascicoloSiep) throws F3BException {
		Connection lConn = null;
		RiferimentoFascicoloSiepDAO lRifDao = null;
		try {
			lConn = getDBConnection();
			lRifDao = new RiferimentoFascicoloSiepDAO(lConn);
			lRifDao.setCondizioneUpdate(idRiferimentoFascicoloSiep);
			lRifDao.delete();
			commit(lConn);
		} catch (DAOException daoEx) {
			throw new F3BException("RiferimentoFascicoloSiepController.ExCancellaRiferimentoFascicoloSiep: "
					+ daoEx);
		} finally {
			cleanup(lRifDao);
			cleanup(lConn);
		}
	}

	// TODO carmela verificare
	public Vector ExRicercaRiferimentoFascicoloSiepByIdFasSius(BigDecimal idFascicoloSius)
			throws F3BException {

		Connection lConn = null;
		Vector lRiFaSiep = new Vector();
		RiferimentoFascicoloSiepSqlDAO lRifDao = null;

		try {
			lConn = getDBConnection();
			lRifDao = new RiferimentoFascicoloSiepSqlDAO(lConn);
			lRifDao.ricercaRiferimentoFascicoloSiepByIdFasSius(idFascicoloSius);
			lRiFaSiep = new Vector(lRifDao.getModels());
		} catch (DAOException daoEx) {
			throw new F3BException(
					"RiferimentoFascicoloSiepController.ExRicercaRiferimentoFascicoloSiepByIdFasSius: "
							+ daoEx);
		} finally {
			cleanup(lRifDao);
			cleanup(lConn);
		}
		return lRiFaSiep;
	}

	public void ExCancellaRiferimentoFascicoloSiepByIdFasSius(BigDecimal idFascicoloSius) throws F3BException {
		Connection lConn = null;
		RiferimentoFascicoloSiepDAO lRifDao = null;
		try {
			lConn = getDBConnection();
			lRifDao = new RiferimentoFascicoloSiepDAO(lConn);
			lRifDao.setCondizioneUpdateByIdFasSius(idFascicoloSius);
			lRifDao.delete();
			commit(lConn);
		} catch (DAOException daoEx) {
			throw new F3BException(
					"RiferimentoFascicoloSiepController.ExCancellaRiferimentoFascicoloSiepByIdFasSius: "
							+ daoEx);
		} finally {
			cleanup(lRifDao);
			cleanup(lConn);
		}
	}

	// 01-2015
	public BigDecimal ExInserisciRiferimentoSiepUpdateMisuraSic(
			RiferimentoFascicoloSiepModel aRiferimentoFascicoloSiep, BigDecimal aIdMisura)
			throws F3BException {
		Connection lConn = null;

		BigDecimal idMisura = aIdMisura;
		BigDecimal lKey = null;
		RiferimentoFascicoloSiepDAO lRifDao = null;
		RiferimentoFascicoloSiepModel lRifMod = null;

		MisuraSicurezzaSqlDAO lMisqlDao = null;
		MisuraSicurezzaDAO lMisDao = null;
		MisuraSicurezzaModel lMisMod = null;

		try {
			lConn = getDBConnection();
			// RiferimentoFascicoloSIEP: insert
			lRifMod = new RiferimentoFascicoloSiepModel(aRiferimentoFascicoloSiep);
			lRifDao = new RiferimentoFascicoloSiepDAO(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("--XX-- ExInserisciRiferimentoSiepUpdateMisuraSic - lRifMod = " + lRifMod);
			lRifDao.setDAOFromModel(aRiferimentoFascicoloSiep);
			lKey = lRifDao.insert();

			// Misura di Sicurezza: Ricerca
			lMisqlDao = new MisuraSicurezzaSqlDAO(lConn);

			lMisqlDao.ricercaMisuraSicurezzaByKey(aIdMisura);
			lMisMod = (MisuraSicurezzaModel) lMisqlDao.getModelByKey();

			lMisMod.setDataAggiornamento(aRiferimentoFascicoloSiep.getDataInserimento());
			lMisMod.setCodOperatoreAggiornamento(aRiferimentoFascicoloSiep.getCodOperatoreInserimento());
			lMisMod.setCodUfficioAggiornamento(aRiferimentoFascicoloSiep.getCodUfficioInserimento());
			lMisMod.setFasSieIdFascicoloSiepRif(lKey);

			// Misura di Sicurezza: Update
			lMisDao = new MisuraSicurezzaDAO(lConn);
			lMisDao.setDAOFromModelForUpdate(lMisMod);
			lMisDao.update();
			lMisDao.stop();
			commit(lConn);
		} catch (DAOException ex) {
			rollback(lConn);
			throw new F3BException(
					"RiferimentoFascicoloSiepController.ExInserisciRiferimentoSiepUpdateMisuraSic: " + ex);
		} finally {
			cleanup(lRifDao);
			cleanup(lMisDao);
			cleanup(lMisqlDao);
			cleanup(lConn);
		}

		return idMisura;

	} // Chiude ExInserisciRiferimentoSiepUpdateMisuraSic

	public void ExCancellaRiferimentoFascicoloSiepUpdateMisuraSic(MisuraSicurezzaModel aMisuraMod)
			throws F3BException {
		// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		// siesLogger.debug("--XX-- ExCancellaRiferimentoFascicoloSiepUpdateMisuraSic - Start");

		Connection lConn = null;
		RiferimentoFascicoloSiepDAO lRifDao = null;

//		MisuraSicurezzaSqlDAO lMisqlDao = null;
		MisuraSicurezzaDAO lMisDao = null;
		MisuraSicurezzaModel lMisMod = null;
		try {
			lConn = getDBConnection();
			// RiferimentoFascicoloSiep
			lRifDao = new RiferimentoFascicoloSiepDAO(lConn);
			lRifDao.setCondizioneUpdate(aMisuraMod.getFasSieIdFascicoloSiepRif());
			lRifDao.delete();

			// Misura di Sicurezza
			lMisDao = new MisuraSicurezzaDAO(lConn);
			lMisMod = new MisuraSicurezzaModel(aMisuraMod);
			lMisMod.setFasSieIdFascicoloSiepRif(null);

			lMisDao.setDAOFromModelForUpdate(lMisMod);
			lMisDao.update();
			lMisDao.stop();

			commit(lConn);
		} catch (DAOException daoEx) {
			throw new F3BException(
					"RiferimentoFascicoloSiepController.ExCancellaRiferimentoFascicoloSiepUpdateMisuraSic: "
							+ daoEx);
		} finally {
			cleanup(lRifDao);
			cleanup(lMisDao);
			cleanup(lConn);
		}
	}

}