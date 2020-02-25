package siap.siep.agdgfascicolosiep.controller;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Vector;

import org.apache.log4j.Logger;

import siap.controller.SiapController;
import siap.siep.agdgfascicolosiep.dao.AgdgFascicoloSiepDAO;
import siap.siep.agdgfascicolosiep.dao.AgdgFascicoloSiepSqlDAO;
import siap.siep.agdgfascicolosiep.model.AgdgFascicoloSiepModel;
import f3b.dao.DAOException;
import f3b.log.LogF3B;
import f3b.util.F3BException;

/**
 * <p>
 * Title: AgdgFascicoloSiepController
 * </p>
 * <p>
 * Description: Classe Controller per AgdgFascicoloSiep
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
public class AgdgFascicoloSiepController extends SiapController implements IAgdgFascicoloSiep {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public AgdgFascicoloSiepModel ExInserisciAgdgFascicoloSiep(AgdgFascicoloSiepModel aAgdgFascicoloSiep)
			throws F3BException {
		Connection lConn = null;
		AgdgFascicoloSiepDAO lAgdDao = null;
		AgdgFascicoloSiepModel lAgdMod = null;

		try {
			lConn = getDBConnection();
			lAgdMod = new AgdgFascicoloSiepModel(aAgdgFascicoloSiep);
			lAgdDao = new AgdgFascicoloSiepDAO(lConn);
			lAgdDao.setDAOFromModel(aAgdgFascicoloSiep);
			BigDecimal lKey = null;
			lKey = lAgdDao.insert();
			commit(lConn);
			lAgdMod.setIdAgdgFascicoloSiep(lKey);
		} catch (DAOException ex) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + ex);
			throw new F3BException("AgdgFascicoloSiepController.ExInserisci: Non posso inserire: " + ex);
		} finally {
			cleanup(lAgdDao);
			cleanup(lConn);
		}
		return lAgdMod;
	}

	public Vector ExRicercaAgdgFascicoloSiep(AgdgFascicoloSiepModel aAgdgFascicoloSiep) throws F3BException {
		Connection lConn = null;
		Vector lAgdgFascicoloSiei = new Vector();
		AgdgFascicoloSiepSqlDAO lAgdDao = null;

		try {
			lConn = getDBConnection();
			lAgdDao = new AgdgFascicoloSiepSqlDAO(lConn);
			lAgdDao.ricercaAgdgFascicoloSiep(aAgdgFascicoloSiep);
			lAgdgFascicoloSiei = new Vector(lAgdDao.getModels());
			if (lAgdgFascicoloSiei.size() == 0) {
				throw new F3BException(F3BException.USER_MESSAGE, "Nessun Elemento trovato");
			}
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException("AgdgFascicoloSiepController.ExRicercaAgdgFascicoloSiep: " + daoEx);
		} finally {
			cleanup(lAgdDao);
			cleanup(lConn);
		}
		return lAgdgFascicoloSiei;
	}

	/**
	 * 
	 */
	public Vector ExRicercaAgdgFascicoloSiepByIdFascicoloSiep(BigDecimal aIdFascicoloSiep)
			throws F3BException {
		Connection lConn = null;
		Vector lAgdgFascicoloSiep = new Vector();
		AgdgFascicoloSiepSqlDAO lAgdDao = null;

		try {
			lConn = getDBConnection();
			lAgdDao = new AgdgFascicoloSiepSqlDAO(lConn);
			lAgdDao.ricercaAgdgFascicoloSiepByIdFasSiep(aIdFascicoloSiep);
			lAgdgFascicoloSiep = new Vector(lAgdDao.getModels());
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException(
					"AgdgFascicoloSiepController.ExRicercaAgdgFascicoloSiepByIdFascicoloSiep: " + daoEx);
		} catch (Exception ex) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("Exception: " + ex);
			throw new F3BException(
					"AgdgFascicoloSiepController.ExRicercaAgdgFascicoloSiepByIdFascicoloSiep: " + ex);
		} finally {
			cleanup(lAgdDao);
			cleanup(lConn);
		}
		return lAgdgFascicoloSiep;
	}

	public AgdgFascicoloSiepModel ExRicercaAgdgFascicoloSiepByKey(BigDecimal aKey) throws F3BException {
		Connection lConn = null;
		AgdgFascicoloSiepSqlDAO lAgdDao = null;
		AgdgFascicoloSiepModel lAgdMod;

		try {
			lConn = getDBConnection();
			lAgdDao = new AgdgFascicoloSiepSqlDAO(lConn);
			lAgdDao.ricercaAgdgFascicoloSiepByKey(aKey);
			lAgdMod = (AgdgFascicoloSiepModel) lAgdDao.getModelByKey();
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException(
					"AgdgFascicoloSiepController.ExRicercaAgdgFascicoloSiep: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lAgdDao);
			cleanup(lConn);
		}
		return lAgdMod;
	}

	public AgdgFascicoloSiepModel ExModificaAgdgFascicoloSiep(AgdgFascicoloSiepModel aAgdgFascicoloSiep)
			throws F3BException {
		Connection lConn = null;
		AgdgFascicoloSiepDAO lAgdDao = null;
		AgdgFascicoloSiepModel lAgdMod = new AgdgFascicoloSiepModel(aAgdgFascicoloSiep);

		try {
			lConn = getDBConnection();
			lAgdDao = new AgdgFascicoloSiepDAO(lConn);
			lAgdDao.setDAOFromModelForUpdate(aAgdgFascicoloSiep);
			lAgdDao.update();
			lAgdDao.update();
			commit(lConn);
		} catch (DAOException ex) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + ex);
			throw new F3BException("AgdgFascicoloSiepController.ExModifica: Non posso inserire: " + ex);
		} finally {
			cleanup(lAgdDao);
			cleanup(lConn);
		}
		return lAgdMod;
	}

	public void ExCancellaAgdgFascicoloSiep(AgdgFascicoloSiepModel aAgdgFascicoloSiep) throws F3BException {
		Connection lConn = null;
		AgdgFascicoloSiepDAO lAgdDao = null;

		try {
			lConn = getDBConnection();
			lAgdDao = new AgdgFascicoloSiepDAO(lConn);
			lAgdDao.setCondizioneUpdate(aAgdgFascicoloSiep.getIdAgdgFascicoloSiep());
			lAgdDao.delete();
			commit(lConn);
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException(
					"AgdgFascicoloSiepController.ExCancellaAgdgFascicoloSiep: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lAgdDao);
			cleanup(lConn);
		}
	}

	/**
	 * Inserisci i records di AGDG Fascicolo SIEP per JMS senza assegnare la sequence
	 * 
	 * @param aAGDGFasSiep
	 * @param lConn
	 * @return lCodEsito
	 * @throws F3BException
	 */
	public String ExInserisciAGDGFasSiepWithoutSequence(ArrayList aAGDGFasSiep, Connection lConn)
			throws F3BException {
		String lCodEsito = "00000";
		AgdgFascicoloSiepDAO lAGDGFasSiepDao = null;
		AgdgFascicoloSiepModel lGDGFasSiepMod = null;

		try {
			lAGDGFasSiepDao = new AgdgFascicoloSiepDAO(lConn);

			if (aAGDGFasSiep != null && aAGDGFasSiep.size() > 0) {
				for (int i = 0; i < aAGDGFasSiep.size(); i++) {
					lGDGFasSiepMod = (AgdgFascicoloSiepModel) aAGDGFasSiep.get(i);
					if (lGDGFasSiepMod != null) {
						if (lGDGFasSiepMod.getIdAgdgFascicoloSiep() != null) {
							lAGDGFasSiepDao.setDAOFromModel(lGDGFasSiepMod);
							// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al
							// posto di LogF3B.getLogger()
							siesLogger.info("=========> AGDG_FASCICOLO_SIEP scritta----->");
							// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al
							// posto di LogF3B.getLogger()
							siesLogger.info("IdAgdgFascicoloSiep = "
									+ lGDGFasSiepMod.getIdAgdgFascicoloSiep());

							lAGDGFasSiepDao.setWithoutSequence(true);
							lAGDGFasSiepDao.insert();
							lAGDGFasSiepDao.stop();
						}
					}
				}
			}
		} catch (DAOException ex) {
			if (ex.UNIQUE_CONSTRAINT_VIOLATED) {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
				siesLogger.error("AGDG Fascicolo SIEP presente...");
				lCodEsito = "00001";
			} else {
				lCodEsito = "01400";
				throw new F3BException(F3BException.USER_MESSAGE,
						"Impossibile inserire AGDG Fascicolo SIEP! ");
			}
		} finally {
			cleanup(lAGDGFasSiepDao);
		}
		return lCodEsito;
	}

}