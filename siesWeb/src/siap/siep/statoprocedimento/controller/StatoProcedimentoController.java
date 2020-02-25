package siap.siep.statoprocedimento.controller;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Vector;

import siap.controller.SiapController;
import siap.siep.statoprocedimento.dao.StatoProcedimentoDAO;
import siap.siep.statoprocedimento.dao.StatoProcedimentoSqlDAO;
import siap.siep.statoprocedimento.model.StatoProcedimentoModel;
import f3b.dao.DAOException;
import f3b.util.F3BException;

/**
 * <p>Title: StatoProcedimentoController</p>
 * <p>Description: Classe Controller per StatoProcedimento</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: Bull</p>
 * @version 1.0
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class StatoProcedimentoController extends SiapController implements IStatoProcedimento
 {

	public StatoProcedimentoModel ExInserisciStatoProcedimento(StatoProcedimentoModel aStatoProcedimento)
			throws F3BException {
		Connection lConn = null;
		StatoProcedimentoDAO lStaDao = null;
		StatoProcedimentoModel lStaMod = null;

		try {
			lConn = getDBConnection();
			lStaMod = new StatoProcedimentoModel(aStatoProcedimento);
			lStaDao = new StatoProcedimentoDAO(lConn);
			lStaDao.setDAOFromModel(aStatoProcedimento);
			BigDecimal lKey = null;
			lKey = lStaDao.insert();
			commit(lConn);
			lStaMod.setProgressivo(lKey);
		} catch (DAOException ex) {
			rollback(lConn);
			throw new F3BException("StatoProcedimentoController.ExInserisci: Non posso inserire: " + ex);
		} finally {
			cleanup(lStaDao);
			cleanup(lConn);
		}
		return lStaMod;
	}

	public StatoProcedimentoModel ExCancellaInserisciStatoProcedimento(
			StatoProcedimentoModel aStatoProcedimento) throws F3BException {
		Connection lConn = null;

		StatoProcedimentoDAO lStaDao = null;

		StatoProcedimentoModel lStaMod = new StatoProcedimentoModel();

		try {
			lConn = getDBTransaction();

			lStaDao = new StatoProcedimentoDAO(lConn);

			// cancellazione
			lStaDao.setCondizioneByIdFascicolo(aStatoProcedimento.getFasSieIdFascicoloSiep());
			lStaDao.delete();
			lStaDao.stop();

			// inserimento
			lStaDao.setDAOFromModel(aStatoProcedimento);
			BigDecimal lKey = null;
			lStaDao.insert();

			commit(lConn);

			lStaMod.setProgressivo(lKey);
		} catch (DAOException ex) {
			rollback(lConn);
			throw new F3BException("StatoProcedimentoController.ExCancellaInserisciStatoProcedimento: " + ex);
		} catch (Exception exp) {
			rollback(lConn);
			throw new F3BException("StatoProcedimentoController.ExCancellaInserisciStatoProcedimento: " + exp);
		} finally {
			cleanup(lStaDao);
			cleanup(lConn);
		}

		return lStaMod;
	}

	public Vector ExRicercaStatoProcedimento(StatoProcedimentoModel aStatoProcedimento) throws F3BException {
		Connection lConn = null;
		Vector lStatoProcedimenti = new Vector();
		StatoProcedimentoSqlDAO lStaDao = null;

		try {
			lConn = getDBConnection();
			lStaDao = new StatoProcedimentoSqlDAO(lConn);
			lStaDao.ricercaStatoProcedimento(aStatoProcedimento);
			lStatoProcedimenti = new Vector(lStaDao.getModels());
			if (lStatoProcedimenti.size() == 0) {
				throw new F3BException(F3BException.USER_MESSAGE, "Nessun Elemento trovato");
			}
		} catch (DAOException daoEx) {
			throw new F3BException(
					"StatoProcedimentoController.ExRicercaStatoProcedimento: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lStaDao);
			cleanup(lConn);
		}
		return lStatoProcedimenti;
	}

	public Vector ExRicercaStatoProcedimentoByFascicoloSiep(BigDecimal aKeyFascicolo) throws F3BException {
		Connection lConn = null;
		Vector lStatoProcedimenti = new Vector();
		StatoProcedimentoSqlDAO lStaDao = null;

		try {
			lConn = getDBConnection();
			lStaDao = new StatoProcedimentoSqlDAO(lConn);
			lStaDao.ricercaStatoProcedimentoByFascicoloSiep(aKeyFascicolo);
			lStatoProcedimenti = new Vector(lStaDao.getModels());
			if (lStatoProcedimenti.size() == 0) {
				// throw new F3BException(F3BException.USER_MESSAGE,"Nessun Elemento trovato");
			}
		} catch (DAOException daoEx) {
			throw new F3BException(
					"StatoProcedimentoController.ExRicercaStatoProcedimento: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lStaDao);
			cleanup(lConn);
		}
		return lStatoProcedimenti;
	}

	public String ExGetMaxStatoProcedimentoByFascicoloSiep(BigDecimal aKeyFascicolo) throws F3BException {
		Connection lConn = null;
		String lCodStatoProcedimento = "";
		StatoProcedimentoSqlDAO lStaDao = null;

		try {
			lConn = getDBConnection();
			lStaDao = new StatoProcedimentoSqlDAO(lConn);
			lStaDao.ricercaMaxStatoProcedimentoByFascicoloSiep(aKeyFascicolo);
			lCodStatoProcedimento = lStaDao.getCodStatoByKey();
		} catch (DAOException daoEx) {
			throw new F3BException(
					"StatoProcedimentoController.ExGetMaxStatoProcedimentoByFascicoloSiep: Non posso leggere : "
							+ daoEx);
		} finally {
			cleanup(lStaDao);
			cleanup(lConn);
		}
		return lCodStatoProcedimento;
	}

	public Vector ExRicercaStatoProcedimentoByFascicoloSiepStato(BigDecimal aKeyFascicolo)
			throws F3BException {
		Connection lConn = null;
		Vector lStatoProcedimento = new Vector();
		// StatoProcedimentoModel lStatoProcedimento = new StatoProcedimentoModel();
		StatoProcedimentoSqlDAO lStaDao = null;

		try {
			lConn = getDBConnection();
			lStaDao = new StatoProcedimentoSqlDAO(lConn);
			lStaDao.ricercaStatoProcedimentoByFascicoloSiepStato(aKeyFascicolo);
			lStatoProcedimento = new Vector(lStaDao.getModels());

		} catch (DAOException daoEx) {
			throw new F3BException(
					"StatoProcedimentoController.ExRicercaStatoProcedimento: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lStaDao);
			cleanup(lConn);
		}
		return lStatoProcedimento;
	}

	public StatoProcedimentoModel ExRicercaStatoProcedimentoByKey(BigDecimal aKey) throws F3BException {
		Connection lConn = null;
		StatoProcedimentoSqlDAO lStaDao = null;
		StatoProcedimentoModel lStaMod;

		try {
			lConn = getDBConnection();
			lStaDao = new StatoProcedimentoSqlDAO(lConn);
			lStaDao.ricercaStatoProcedimentoByKey(aKey);
			lStaMod = (StatoProcedimentoModel) lStaDao.getModelByKey();
		} catch (DAOException daoEx) {
			throw new F3BException(
					"StatoProcedimentoController.ExRicercaStatoProcedimento: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lStaDao);
			cleanup(lConn);
		}
		return lStaMod;
	}

	public StatoProcedimentoModel ExModificaStatoProcedimento(StatoProcedimentoModel aStatoProcedimento)
			throws F3BException {
		Connection lConn = null;

		StatoProcedimentoDAO lStaDao = null;

		StatoProcedimentoModel lStaMod = new StatoProcedimentoModel(aStatoProcedimento);

		try {
			lConn = getDBConnection();

			lStaDao = new StatoProcedimentoDAO(lConn);
			lStaDao.setDAOFromModelForUpdate(aStatoProcedimento);

			lStaDao.update();

			commit(lConn);
		} catch (DAOException ex) {
			rollback(lConn);
			throw new F3BException("StatoProcedimentoController.ExModifica: Non posso inserire: " + ex);
		} finally {
			cleanup(lStaDao);
			cleanup(lConn);
		}
		return lStaMod;
	}

	public void ExCancellaStatoProcedimento(StatoProcedimentoModel aStatoProcedimento) throws F3BException {
		Connection lConn = null;
		StatoProcedimentoDAO lStaDao = null;

		try {
			lConn = getDBConnection();
			lStaDao = new StatoProcedimentoDAO(lConn);
			lStaDao.setCondizioneUpdate(aStatoProcedimento);
			lStaDao.delete();
			commit(lConn);
		} catch (DAOException daoEx) {
			throw new F3BException(
					"StatoProcedimentoController.ExCancellaStatoProcedimento: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lStaDao);
			cleanup(lConn);
		}
	}

	public String ExInserisciStatoProcedimentoWithoutSequence(ArrayList aStati, Connection lConn)
			throws F3BException {

		String lCodEsito = "00000";
		StatoProcedimentoDAO lStaDao = null;
		StatoProcedimentoModel lStaMod = null;

		try {
			if (aStati.size() > 0) {
				for (int i = 0; i < aStati.size(); i++) {
					lStaMod = new StatoProcedimentoModel();
					lStaMod = (StatoProcedimentoModel) aStati.get(i);
					lStaDao = new StatoProcedimentoDAO(lConn);
					lStaDao.setDAOFromModel(lStaMod);
					lStaDao.setWithoutSequence(true);
					lStaDao.insert();
					lStaDao.stop();
				}
			}

		} catch (DAOException ex) {
			if (ex.UNIQUE_CONSTRAINT_VIOLATED) {
				lCodEsito = "00001";
			} else {
				lCodEsito = "01400";
				throw new F3BException(F3BException.USER_MESSAGE,
						"Impossibile inserire lo Stato Procedimento! ");
			}
		} finally {
			cleanup(lStaDao);
		}
		return lCodEsito;

	}

}