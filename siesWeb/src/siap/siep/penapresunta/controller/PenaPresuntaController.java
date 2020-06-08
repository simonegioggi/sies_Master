package siap.siep.penapresunta.controller;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Vector;

import f3b.dao.DAOException;
import f3b.util.F3BException;
import siap.controller.SiapController;
import siap.siep.penapresunta.dao.PenaPresuntaDAO;
import siap.siep.penapresunta.dao.PenaPresuntaSqlDAO;
import siap.siep.penapresunta.model.PenaPresuntaModel;

/**
 * <p>
 * Title: PenaPresuntaController
 * </p>
 * <p>
 * Description: Classe Controller per PenaPresunta
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
public class PenaPresuntaController extends SiapController implements IPenaPresunta {

	public PenaPresuntaModel ExInserisciPenaPresunta(PenaPresuntaModel aPenaPresunta) throws F3BException {

		Connection lConn = null;
		PenaPresuntaDAO lPenDao = null;
		PenaPresuntaModel lPenMod = null;

		try {
			lConn = getDBConnection();
			lPenMod = new PenaPresuntaModel(aPenaPresunta);
			lPenDao = new PenaPresuntaDAO(lConn);
			lPenDao.setDAOFromModel(aPenaPresunta);
			BigDecimal lKey = null;
			lKey = lPenDao.insert();
			commit(lConn);
			lPenMod.setIdPenaPresunta(lKey);
		} catch (DAOException ex) {
			rollback(lConn);
			throw new F3BException("PenaPresuntaController.ExInserisci: Non posso inserire: " + ex);
		} finally {
			cleanup(lPenDao);
			cleanup(lConn);
		}
		return lPenMod;
	}

	public Vector ExRicercaPenaPresunta(PenaPresuntaModel aPenaPresunta) throws F3BException {

		Connection lConn = null;
		Vector lPenaPresunti = new Vector();
		PenaPresuntaSqlDAO lPenDao = null;

		try {
			lConn = getDBConnection();
			lPenDao = new PenaPresuntaSqlDAO(lConn);
			lPenDao.ricercaPenaPresunta(aPenaPresunta);
			lPenaPresunti = new Vector(lPenDao.getModels());
			if (lPenaPresunti.size() == 0) {
				throw new F3BException(F3BException.USER_MESSAGE, "Nessun Elemento trovato");
			}
		} catch (DAOException daoEx) {
			throw new F3BException(
					"PenaPresuntaController.ExRicercaPenaPresunta: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lPenDao);
			cleanup(lConn);
		}
		return lPenaPresunti;
	}

	// pena presunta corrente
	public PenaPresuntaModel ExRicercaPenaPresuntaCorrenteByFascicoloSiep(BigDecimal aKey)
			throws F3BException {

		Connection lConn = null;
		PenaPresuntaSqlDAO lPenPresDao = null;
		PenaPresuntaModel lPenPresMod;
		try {
			lConn = getDBConnection();
			lPenPresDao = new PenaPresuntaSqlDAO(lConn);
			lPenPresDao.ricercaPenaPresuntaCorrenteByFascicoloSiep(aKey);
			lPenPresMod = (PenaPresuntaModel) lPenPresDao.getModelByKey();
		} catch (DAOException daoEx) {
			throw new F3BException(
					"PenaPresuntaController.ExRicercaPenaPresuntaCorrenteByFascicoloSiep: Non posso leggere : "
							+ daoEx);
		} finally {
			cleanup(lPenPresDao);
			cleanup(lConn);
		}
		return lPenPresMod;
	}

	public PenaPresuntaModel ExRicercaPenaPresuntaByKey(BigDecimal aKey) throws F3BException {

		Connection lConn = null;
		PenaPresuntaSqlDAO lPenDao = null;
		PenaPresuntaModel lPenMod;

		try {
			lConn = getDBConnection();
			lPenDao = new PenaPresuntaSqlDAO(lConn);
			lPenDao.ricercaPenaPresuntaByKey(aKey);
			lPenMod = (PenaPresuntaModel) lPenDao.getModelByKey();
		} catch (DAOException daoEx) {
			throw new F3BException(
					"PenaPresuntaController.ExRicercaPenaPresunta: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lPenDao);
			cleanup(lConn);
		}
		return lPenMod;
	}

	public PenaPresuntaModel ExModificaPenaPresunta(PenaPresuntaModel aPenaPresunta) throws F3BException {

		Connection lConn = null;
		PenaPresuntaDAO lPenDao = null;
		PenaPresuntaModel lPenMod = new PenaPresuntaModel(aPenaPresunta);

		try {
			lConn = getDBConnection();
			lPenDao = new PenaPresuntaDAO(lConn);
			lPenDao.setDAOFromModelForUpdate(aPenaPresunta);
			lPenDao.update();
			commit(lConn);
		} catch (DAOException ex) {
			rollback(lConn);
			throw new F3BException("PenaPresuntaController.ExModifica: Non posso inserire: " + ex);
		} finally {
			cleanup(lPenDao);
			cleanup(lConn);
		}
		return lPenMod;
	}

	public void ExCancellaPenaPresunta(PenaPresuntaModel aPenaPresunta) throws F3BException {

		Connection lConn = null;
		PenaPresuntaDAO lPenDao = null;

		try {
			lConn = getDBConnection();
			lPenDao = new PenaPresuntaDAO(lConn);
			lPenDao.setCondizioneUpdate(aPenaPresunta.getIdPenaPresunta());
			lPenDao.delete();
			commit(lConn);
		} catch (DAOException daoEx) {
			throw new F3BException(
					"PenaPresuntaController.ExCancellaPenaPresunta: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lPenDao);
			cleanup(lConn);
		}
	}

	public void ExCancellaPenaPresuntaByIdFascicolo(PenaPresuntaModel aPenaPresunta) throws F3BException {

		Connection lConn = null;
		PenaPresuntaDAO lPenDao = null;

		try {
			lConn = getDBConnection();
			lPenDao = new PenaPresuntaDAO(lConn);
			lPenDao.setCondizioneUpdatebyIdFascicolo(aPenaPresunta.getFasSieIdFascicoloSiep());
			lPenDao.delete();
			commit(lConn);
		} catch (DAOException daoEx) {
			throw new F3BException(
					"PenaPresuntaController.ExCancellaPenaPresunta: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lPenDao);
			cleanup(lConn);
		}
	}

	public String ExInserisciPenaPresuntaWithoutSequence(PenaPresuntaModel aPenaPresunta, Connection lConn)
			throws F3BException {

		String lCodEsito = "00000";
		PenaPresuntaDAO lPenDao = null;
		try {
			if (aPenaPresunta != null) {
				lPenDao = new PenaPresuntaDAO(lConn);
				lPenDao.setDAOFromModel(aPenaPresunta);
				lPenDao.setWithoutSequence(true);
				lPenDao.insert();
			}
		} catch (DAOException ex) {
			if (ex.UNIQUE_CONSTRAINT_VIOLATED) {
				// Inizializzo la chiave del fascicolo con quella inviatami
				// lChiave = lPars.getFascicolo().getIdFascicoloSiep();
				lCodEsito = "00001";
			} else {
				lCodEsito = "01400";
				throw new F3BException(F3BException.USER_MESSAGE, "Impossibile inserire la Pena Presunta! ");
			}
		} finally {
			cleanup(lPenDao);
		}
		return lCodEsito;
	}

}