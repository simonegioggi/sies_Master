package siap.sico.storicosoggetto.controller;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Vector;

import f3b.dao.DAOException;
import f3b.util.F3BException;
import siap.controller.SiapController;
import siap.sico.soggetto.dao.SoggettoSqlDAO;
import siap.sico.soggetto.model.SoggettoModel;
import siap.sico.storicosoggetto.dao.StoricoSoggettoDAO;
import siap.sico.storicosoggetto.dao.StoricoSoggettoSqlDAO;
import siap.sico.storicosoggetto.model.SoggettoStoricoSoggettoModel;
import siap.sico.storicosoggetto.model.StoricoSoggettoModel;
import siap.siep.fascicolo.dao.FascicoloSiepOnViewSqlDAO;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.sius.fascicolo.dao.FascicoloSiusSoggettoSqlDAO;
import siap.sius.fascicolo.model.FascicoloGPModel;

/**
 * <p>
 * Title: StoricoSoggettoController
 * </p>
 * <p>
 * Description: Classe Controller per StoricoSoggetto
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
public class StoricoSoggettoController extends SiapController implements IStoricoSoggetto {

	public StoricoSoggettoModel ExInserisciStoricoSoggetto(StoricoSoggettoModel aStoricoSoggetto)
			throws F3BException {

		Connection lConn = null;
		StoricoSoggettoDAO lStoDao = null;
		StoricoSoggettoModel lStoMod = null;

		try {
			lConn = getDBConnection();
			lStoMod = new StoricoSoggettoModel(aStoricoSoggetto);
			lStoDao = new StoricoSoggettoDAO(lConn);
			lStoDao.setDAOFromModel(aStoricoSoggetto);
			// BigDecimal lKey = null;
			/* lKey = */lStoDao.insert();
			commit(lConn);
			// lStoMod.setIdStoricoSoggetto(lKey);
		} catch (DAOException ex) {
			rollback(lConn);
			throw new F3BException("StoricoSoggettoController.ExInserisci: Non posso inserire: " + ex);
		} finally {
			cleanup(lStoDao);
			cleanup(lConn);
		}
		return lStoMod;
	}

	public Vector ExRicercaStoricoSoggetto(StoricoSoggettoModel aStoricoSoggetto) throws F3BException {

		Connection lConn = null;
		Vector lStoricoSoggetti = new Vector();
		StoricoSoggettoSqlDAO lStoDao = null;

		try {
			lConn = getDBConnection();
			lStoDao = new StoricoSoggettoSqlDAO(lConn);
			lStoDao.ricercaStoricoSoggetto(aStoricoSoggetto);
			lStoricoSoggetti = new Vector(lStoDao.getModels());
			if (lStoricoSoggetti.size() == 0) {
				throw new F3BException(F3BException.USER_MESSAGE, "Nessun Elemento trovato");
			}
		} catch (DAOException daoEx) {
			throw new F3BException(
					"StoricoSoggettoController.ExRicercaStoricoSoggetto: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lStoDao);
			cleanup(lConn);
		}
		return lStoricoSoggetti;
	}

	public StoricoSoggettoModel ExRicercaStoricoSoggettoByKey(BigDecimal aKey) throws F3BException {

		Connection lConn = null;
		StoricoSoggettoSqlDAO lStoDao = null;
		StoricoSoggettoModel lStoMod;

		try {
			lConn = getDBConnection();
			lStoDao = new StoricoSoggettoSqlDAO(lConn);
			lStoDao.ricercaStoricoSoggettoByKey(aKey);
			lStoMod = (StoricoSoggettoModel) lStoDao.getModelByKey();
		} catch (DAOException daoEx) {
			throw new F3BException(
					"StoricoSoggettoController.ExRicercaStoricoSoggetto: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lStoDao);
			cleanup(lConn);
		}
		return lStoMod;
	}

	public Vector ExRicercaStoricoSoggettoByIdSogVariato(BigDecimal aKey) throws F3BException {

		Connection lConn = null;
		StoricoSoggettoSqlDAO lStoDao = null;
		Vector lStoricoSoggetti = null;
		SoggettoStoricoSoggettoModel lSogStoricoSoMod = null;
		SoggettoSqlDAO lSqlDAO = null;
		Vector lStoricoSoggettiTutti = new Vector();
		FascicoloSiepModel lFacMod = null;
		FascicoloSiepOnViewSqlDAO lFacSql = null;

		try {
			lConn = getDBConnection();
			lStoDao = new StoricoSoggettoSqlDAO(lConn);
			lStoDao = new StoricoSoggettoSqlDAO(lConn);
			lSqlDAO = new SoggettoSqlDAO(lConn);
			lFacSql = new FascicoloSiepOnViewSqlDAO(lConn);
			lFacMod = new FascicoloSiepModel();
			lSogStoricoSoMod = new SoggettoStoricoSoggettoModel();

			lStoDao.ricercaStoricoSoggettoByIdSogVariato(aKey);
			lStoricoSoggetti = new Vector(lStoDao.getModels());

			if (lStoricoSoggetti.size() > 0) {
				lSogStoricoSoMod.setStoricoSoggetto(new ArrayList(lStoricoSoggetti));
				for (int i = 0; i < lStoricoSoggetti.size(); i++) {
					/* lStoMod = (StoricoSoggettoModel) */ lStoricoSoggetti.get(i);
				}
			}

			lFacMod.setSogIdSoggetto(aKey);

			lFacSql.ricercaFascicoloSoggetto(lFacMod);

			lSogStoricoSoMod.setFascicoloSiep(new ArrayList(lFacSql.getModels()));

			lStoricoSoggettiTutti.add(lSogStoricoSoMod);
		} catch (DAOException daoEx) {
			throw new F3BException(
					"StoricoSoggettoController.ExRicercaStoricoSoggettoByIdSogVariato: Non posso leggere : "
							+ daoEx);
		} catch (Exception e) {
			throw new F3BException(
					"StoricoSoggettoController.ExRicercaStoricoSoggettoByIdSogVariato: Non posso leggere  : "
							+ e);
		} finally {
			cleanup(lStoDao);
			cleanup(lSqlDAO);
			cleanup(lFacSql);

			cleanup(lConn);
		}
		return lStoricoSoggettiTutti;
	}

	public Vector ExRicercaStoricoSoggettoSiusByIdSogVariato(BigDecimal aKey) throws F3BException {

		Connection lConn = null;
		StoricoSoggettoSqlDAO lStoDao = null;
		Vector lStoricoSoggetti = null;
		SoggettoStoricoSoggettoModel lSogStoricoSoMod = null;
		SoggettoSqlDAO lSqlDAO = null;
		Vector lStoricoSoggettiTutti = new Vector();
		FascicoloSiusSoggettoSqlDAO lFacSql = null;
		SoggettoModel lSog = null;

		try {
			lConn = getDBConnection();
			lStoDao = new StoricoSoggettoSqlDAO(lConn);
			lStoDao = new StoricoSoggettoSqlDAO(lConn);
			lSqlDAO = new SoggettoSqlDAO(lConn);
			lFacSql = new FascicoloSiusSoggettoSqlDAO(lConn);
			// lFacMod = new FascicoloSiusModel();
			lSog = new SoggettoModel();

			lSogStoricoSoMod = new SoggettoStoricoSoggettoModel();

			lStoDao.ricercaStoricoSoggettoByIdSogVariato(aKey);
			lStoricoSoggetti = new Vector(lStoDao.getModels());

			if (lStoricoSoggetti.size() > 0) {
				lSogStoricoSoMod.setStoricoSoggetto(new ArrayList(lStoricoSoggetti));
				for (int i = 0; i < lStoricoSoggetti.size(); i++) {
					/* lStoMod = (StoricoSoggettoModel) */lStoricoSoggetti.get(i);
				}
			}

			lSog.setIdSoggetto(aKey);

			lFacSql.ricercaFascicoloSiusSoggettoForStorico(lSog);
			lFacSql.start();
			FascicoloGPModel lFascicolo = null;
			Vector fascicoli = new Vector();
			while (lFacSql.next()) {
				lFascicolo = (FascicoloGPModel) lFacSql.getFascicoloSiusGPModelForStorico();

				// STUB - 21/05/2003 ricarico il model del soggetto nel fascicolo Sius.
				lFascicolo.getFascicoloSiusModel();
				fascicoli.add(lFascicolo);

			}

			lSogStoricoSoMod.setFascicoloSius(fascicoli);
			lStoricoSoggettiTutti.add(lSogStoricoSoMod);
		} catch (DAOException daoEx) {
			throw new F3BException(
					"StoricoSoggettoController.ExRicercaStoricoSoggettoSiusByIdSogVariato: Non posso leggere : "
							+ daoEx);
		} catch (Exception e) {
			throw new F3BException(
					"StoricoSoggettoController.ExRicercaStoricoSoggettoSiusByIdSogVariato: Non posso leggere  : "
							+ e);
		} finally {
			cleanup(lStoDao);
			cleanup(lSqlDAO);
			cleanup(lFacSql);

			cleanup(lConn);
		}
		return lStoricoSoggettiTutti;
	}

	public Vector ExRicercaStoricoSoggettoByIdSogNuovo(BigDecimal aKey) throws F3BException {

		Connection lConn = null;
		StoricoSoggettoSqlDAO lStoDao = null;
		StoricoSoggettoModel lStoMod;
		Vector lStoricoSoggetti = null;
		SoggettoStoricoSoggettoModel lSogStoricoSoMod = null;
		SoggettoSqlDAO lSqlDAO = null;
		SoggettoModel lSogMod = null;
		Vector lStoricoSoggettiTutti = new Vector();
		FascicoloSiepModel lFacMod = null;
		FascicoloSiepOnViewSqlDAO lFacSql = null;

		try {
			lConn = getDBConnection();
			lStoDao = new StoricoSoggettoSqlDAO(lConn);
			lStoDao = new StoricoSoggettoSqlDAO(lConn);
			lSqlDAO = new SoggettoSqlDAO(lConn);
			lFacSql = new FascicoloSiepOnViewSqlDAO(lConn);
			lFacMod = new FascicoloSiepModel();
			lSogMod = new SoggettoModel();
			lSogStoricoSoMod = new SoggettoStoricoSoggettoModel();

			lStoDao.ricercaStoricoSoggettoByIdSog(aKey);
			lStoricoSoggetti = new Vector(lStoDao.getModels());
			lSogStoricoSoMod.setStoricoSoggetto(new ArrayList(lStoricoSoggetti));

			if (lStoricoSoggetti.size() > 0) {
				lSqlDAO.ricercaSoggettoByKey(aKey);
				lSogMod = (SoggettoModel) lSqlDAO.getModelByKey();
				lSogStoricoSoMod.setSoggetto(lSogMod);
				for (int i = 0; i < lStoricoSoggetti.size(); i++) {
					lStoMod = (StoricoSoggettoModel) lStoricoSoggetti.get(i);
					if (lStoMod != null) {

						lFacMod.setSogIdSoggetto(lStoMod.getIdSoggettoVariato());
						lFacSql.ricercaFascicolo(lFacMod);
						lSogStoricoSoMod.setFascicoloSiep(new ArrayList(lFacSql.getModels()));
					}

					lStoricoSoggettiTutti.add(lSogStoricoSoMod);
				}
			}
		} catch (DAOException daoEx) {
			throw new F3BException(
					"StoricoSoggettoController.ExRicercaStoricoSoggettoByIdSogNuovo: Non posso leggere : "
							+ daoEx);
		} finally {
			cleanup(lStoDao);
			cleanup(lSqlDAO);
			cleanup(lFacSql);

			cleanup(lConn);
		}
		return lStoricoSoggettiTutti;
	}

	public StoricoSoggettoModel ExModificaStoricoSoggetto(StoricoSoggettoModel aStoricoSoggetto)
			throws F3BException {

		Connection lConn = null;
		StoricoSoggettoDAO lStoDao = null;
		StoricoSoggettoModel lStoMod = new StoricoSoggettoModel(aStoricoSoggetto);

		try {
			lConn = getDBConnection();
			lStoDao = new StoricoSoggettoDAO(lConn);
			lStoDao.setDAOFromModelForUpdate(aStoricoSoggetto);
			lStoDao.update();
			lStoDao.update();
			commit(lConn);
		} catch (DAOException ex) {
			rollback(lConn);
			throw new F3BException("StoricoSoggettoController.ExModifica: Non posso inserire: " + ex);
		} finally {
			cleanup(lStoDao);
			cleanup(lConn);
		}
		return lStoMod;
	}

	public void ExCancellaStoricoSoggetto(StoricoSoggettoModel aStoricoSoggetto) throws F3BException {

		Connection lConn = null;
		StoricoSoggettoDAO lStoDao = null;

		try {
			lConn = getDBConnection();
			lStoDao = new StoricoSoggettoDAO(lConn);
			lStoDao.setCondizioneUpdate(aStoricoSoggetto.getProgressivoStorico());
			lStoDao.delete();
			commit(lConn);
		} catch (DAOException daoEx) {
			throw new F3BException(
					"StoricoSoggettoController.ExCancellaStoricoSoggetto: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lStoDao);
			cleanup(lConn);
		}
	}

}