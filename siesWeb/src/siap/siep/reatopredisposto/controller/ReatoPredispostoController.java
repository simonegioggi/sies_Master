package siap.siep.reatopredisposto.controller;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.dao.DAOException;
import f3b.log.LogF3B;
import f3b.util.F3BException;
import siap.controller.SiapController;
import siap.siep.reatopredisposto.dao.ReatoPredispostoDAO;
import siap.siep.reatopredisposto.dao.ReatoPredispostoSqlDAO;
import siap.siep.reatopredisposto.model.ReatoPredispostoModel;

/**
 * <p>
 * Title: ReatoPredispostoController
 * </p>
 * <p>
 * Description: Classe Controller per ReatoPredisposto
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
public class ReatoPredispostoController extends SiapController implements IReatoPredisposto {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public ReatoPredispostoModel ExInserisciReatoPredisposto(ReatoPredispostoModel aReatoPredisposto)
			throws F3BException {

		Connection lConn = null;
		ReatoPredispostoDAO lReaDao = null;
		ReatoPredispostoModel lReaMod = null;

		try {
			lConn = getDBConnection();
			lReaMod = new ReatoPredispostoModel(aReatoPredisposto);
			lReaDao = new ReatoPredispostoDAO(lConn);
			lReaDao.setDAOFromModel(aReatoPredisposto);
			BigDecimal lKey = null;
			lKey = lReaDao.insert();
			commit(lConn);
			lReaMod.setIdReatoPredisposto(lKey);
		} catch (DAOException ex) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + ex);
			throw new F3BException("ReatoPredispostoController.ExInserisci: Non posso inserire: " + ex);
		} finally {
			cleanup(lReaDao);
			cleanup(lConn);
		}
		return lReaMod;
	}

	public Vector ExRicercaReatoPredisposto(ReatoPredispostoModel aReatoPredisposto) throws F3BException {

		Connection lConn = null;
		Vector lReatoPredisposti = new Vector();
		ReatoPredispostoSqlDAO lReaDao = null;

		try {
			lConn = getDBConnection();
			lReaDao = new ReatoPredispostoSqlDAO(lConn);
			lReaDao.ricercaReatoPredisposto(aReatoPredisposto);
			lReatoPredisposti = new Vector(lReaDao.getModels());
			if (lReatoPredisposti.size() == 0) {
				throw new F3BException(F3BException.USER_MESSAGE, "Nessun Elemento trovato");
			}
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException(
					"ReatoPredispostoController.ExRicercaReatoPredisposto: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lReaDao);
			cleanup(lConn);
		}
		return lReatoPredisposti;
	}

	public ReatoPredispostoModel ExRicercaReatoPredispostoByKey(BigDecimal aKey) throws F3BException {

		Connection lConn = null;
		ReatoPredispostoSqlDAO lReaDao = null;
		ReatoPredispostoModel lReaMod;

		try {
			lConn = getDBConnection();
			lReaDao = new ReatoPredispostoSqlDAO(lConn);
			lReaDao.ricercaReatoPredispostoByKey(aKey);
			lReaMod = (ReatoPredispostoModel) lReaDao.getModelByKey();
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException(
					"ReatoPredispostoController.ExRicercaReatoPredisposto: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lReaDao);
			cleanup(lConn);
		}
		return lReaMod;
	}

	public ReatoPredispostoModel ExModificaReatoPredisposto(ReatoPredispostoModel aReatoPredisposto)
			throws F3BException {

		Connection lConn = null;
		ReatoPredispostoDAO lReaDao = null;
		ReatoPredispostoModel lReaMod = new ReatoPredispostoModel(aReatoPredisposto);

		try {
			lConn = getDBConnection();
			lReaDao = new ReatoPredispostoDAO(lConn);
			lReaDao.setDAOFromModelForUpdate(aReatoPredisposto);
			lReaDao.update();
			// lReaDao.update();
			commit(lConn);
		} catch (DAOException ex) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + ex);
			throw new F3BException("ReatoPredispostoController.ExModifica: Non posso inserire: " + ex);
		} finally {
			cleanup(lReaDao);
			cleanup(lConn);
		}
		return lReaMod;
	}

	public void ExCancellaReatoPredisposto(ReatoPredispostoModel aReatoPredisposto) throws F3BException {

		Connection lConn = null;
		ReatoPredispostoDAO lReaDao = null;

		try {
			lConn = getDBConnection();
			lReaDao = new ReatoPredispostoDAO(lConn);

			if (aReatoPredisposto.getProgrNorma().intValue() == 1) {
				// se ho cancellato la norma base, devo cancellare anche le norme "legate"
				lReaDao.setCondizione_CancellazioneACatena(aReatoPredisposto);
			} else
				lReaDao.setCondizioneUpdate(aReatoPredisposto.getIdReatoPredisposto());

			lReaDao.delete();
			commit(lConn);
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException(
					"ReatoPredispostoController.ExCancellaReatoPredisposto: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lReaDao);
			cleanup(lConn);
		}
	}

	public ReatoPredispostoModel ExInserisciReatiPredisposti(ArrayList aReati) throws F3BException {

		Connection lConn = null;

		ReatoPredispostoDAO lReaDao = null;
		ReatoPredispostoSqlDAO lReaSqlDAo = null;

		ReatoPredispostoModel lReaMod = null;
		BigDecimal lProgrNorma = null;

		boolean used = false;

		try {
			lConn = getDBTransaction();
			lReaSqlDAo = new ReatoPredispostoSqlDAO(lConn);

			if (aReati.size() > 0) {

				for (int i = 0; i < aReati.size(); i++) {
					lReaMod = new ReatoPredispostoModel();
					lReaMod = (ReatoPredispostoModel) aReati.get(i);

					if (i == 0) {

						// Test Presenza Nome Elemento
						used = lReaSqlDAo.isNomeElementoUsed(lReaMod.getNomeElemento(),
								lReaMod.getCodUfficioInserimento());
						if (used)
							throw new F3BException("Test Presenza Nome Elemento");

						// Gestione Progressivo Norma
						lProgrNorma = lReaSqlDAo.getProgrNorma(lReaMod.getNomeElemento(),
								lReaMod.getCodUfficioInserimento());
					}

					lProgrNorma = new BigDecimal(lProgrNorma.intValue() + 1);

					lReaMod.setProgrNorma(lProgrNorma);

					lReaDao = new ReatoPredispostoDAO(lConn);

					lReaDao.setDAOFromModel(lReaMod);

					lReaDao.insert();

					lReaDao.stop();
				}
			}

			commit(lConn);
		} catch (DAOException ex) {
			rollback(lConn);
			throw new F3BException("ReatoController.ExInserisciReati: " + ex);
		} catch (Exception sqe) {
			rollback(lConn);
			if (used)
				throw new F3BException(F3BException.USER_MESSAGE, "Nome elemento già in uso!");
			else
				throw new F3BException("ReatoController.ExInserisciReati: " + sqe);
		} finally {
			cleanup(lReaDao);
			cleanup(lReaSqlDAo);
			cleanup(lConn);
		}

		return lReaMod;
	}

}