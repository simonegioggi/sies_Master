package siap.siep.posizionemateriale.controller;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import f3b.dao.DAOException;
import f3b.util.F3BException;
import siap.controller.SiapController;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.posizionemateriale.dao.PosizioneMaterialeDAO;
import siap.siep.posizionemateriale.dao.PosizioneMaterialeSqlDAO;
import siap.siep.posizionemateriale.model.PosizioneMaterialeModel;
import siap.siep.posizionematerialefasc.dao.PosizioneMaterialeFascSqlDAO;
import siap.siep.posizionematerialefasc.model.PosizioneMaterialeFascicoliModel;
import siap.sius.fascicolo.model.FascicoloGPModel;

/**
 * <p>
 * Title: PosizioneMaterialeController
 * </p>
 * <p>
 * Description: Classe Controller per PosizioneMateriale
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
public class PosizioneMaterialeController extends SiapController implements IPosizioneMateriale {

	public PosizioneMaterialeModel ExInserisciPosizioneMateriale(PosizioneMaterialeModel aPosizioneMateriale)
			throws F3BException {

		Connection lConn = null;
		PosizioneMaterialeDAO lPosDao = null;
		PosizioneMaterialeModel lPosMod = null;

		try {
			lConn = getDBConnection();
			lPosMod = new PosizioneMaterialeModel(aPosizioneMateriale);
			lPosDao = new PosizioneMaterialeDAO(lConn);
			lPosDao.setDAOFromModel(aPosizioneMateriale);
			/* BigDecimal lKey = */lPosDao.insert();
			commit(lConn);
			// lPosMod.setIdPosizioneMateriale(lKey);
		} catch (DAOException dex) {
			rollback(lConn);
			if (dex.UNIQUE_CONSTRAINT_VIOLATED)
				throw new F3BException(F3BException.USER_MESSAGE,
						"Posizione Materiale già presente in archivio !");
			throw new F3BException("PosizioneMaterialeController.ExInserisci: Non posso inserire: " + dex);
		} catch (Exception ex) {
			rollback(lConn);
			throw new F3BException("PosizioneMaterialeController.ExInserisci: Non posso inserire: " + ex);
		} finally {
			cleanup(lPosDao);
			cleanup(lConn);
		}
		return lPosMod;
	}

	public Vector ExRicercaPosizioneMateriale(PosizioneMaterialeModel aPosizioneMateriale)
			throws F3BException {

		Connection lConn = null;

		Vector lPosizioneMateriali = new Vector();
		PosizioneMaterialeSqlDAO lPosDao = null;

		try {
			lConn = getDBConnection();

			lPosDao = new PosizioneMaterialeSqlDAO(lConn);
			lPosDao.ricercaPosizioneMateriale(aPosizioneMateriale);
			lPosizioneMateriali = new Vector(lPosDao.getModels());

			if (lPosizioneMateriali.size() == 0) {
				throw new F3BException(F3BException.EX_NOT_FOUND, "Nessun Elemento trovato");
			}
		} catch (F3BException fe) {
			throw fe;
		} catch (Exception sqe) {
			throw new F3BException(
					"PosizioneMaterialeController.ExRicercaPosizioneMateriale: Non posso leggere  : " + sqe);
		} finally {
			cleanup(lPosDao);

			cleanup(lConn);
		}

		return lPosizioneMateriali;
	}

	public PosizioneMaterialeFascicoliModel ExRicercaPosizioneMaterialeFascicoli(
			PosizioneMaterialeModel aPosizioneMateriale) throws F3BException {

		Connection lConn = null;

		PosizioneMaterialeFascicoliModel lPosizioneFascicoli = new PosizioneMaterialeFascicoliModel();
		PosizioneMaterialeModel lPosMod = null;
		List lFascicoliSiep = new ArrayList();
		List lFascicoliSius = new ArrayList();

		PosizioneMaterialeSqlDAO lPosDao = null;
		PosizioneMaterialeFascSqlDAO lPosFascDao = null;

		try {
			lConn = getDBConnection();

			lPosDao = new PosizioneMaterialeSqlDAO(lConn);
			lPosDao.ricercaPosizioneMateriale(aPosizioneMateriale);
			lPosMod = (PosizioneMaterialeModel) lPosDao.getModelByKey();

			if (lPosMod == null) {
				throw new F3BException(F3BException.EX_NOT_FOUND, "Nessun Elemento trovato");
			}

			lPosizioneFascicoli.setPosizioneMateriale(lPosMod);

			if (aPosizioneMateriale != null && aPosizioneMateriale.getCodPosizioneMateriale() != null
					&& aPosizioneMateriale.getCodUfficio() != null) {
				lPosFascDao = new PosizioneMaterialeFascSqlDAO(lConn);

				// ************************ CERCA I FASCICOLI SIEP *************************************
				lPosFascDao.ricercaPosizioneMaterialeFascicolo(aPosizioneMateriale.getCodPosizioneMateriale(),
						aPosizioneMateriale.getCodUfficio());

				lPosFascDao.start();
				while (lPosFascDao.next()) {
					FascicoloSiepModel lAgg = (FascicoloSiepModel) lPosFascDao.getModelFascicoloPosizione();
					lFascicoliSiep.add(lAgg);
				}
				lPosFascDao.stop();

				if (!lFascicoliSiep.isEmpty()) {
					lPosizioneFascicoli.setFascicoliSiep(lFascicoliSiep);
				}
				// *************************************************************************************

				// ************************ CERCA I FASCICOLI SIUS *************************************
				lPosFascDao.ricercaPosizioneMaterialeFascicoloSius(
						aPosizioneMateriale.getCodPosizioneMateriale(), aPosizioneMateriale.getCodUfficio());

				lPosFascDao.start();
				while (lPosFascDao.next()) {
					FascicoloGPModel lAgg = (FascicoloGPModel) lPosFascDao.getModelFascicoloSiusPosizione();
					lFascicoliSius.add(lAgg);
				}
				lPosFascDao.stop();

				if (!lFascicoliSius.isEmpty()) {
					lPosizioneFascicoli.setFascicoliSius(lFascicoliSius);
				}
				// *************************************************************************************
			}
		} catch (F3BException fe) {
			fe.printStackTrace();
			throw fe;
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new F3BException(
					"PosizioneMaterialeController.ExRicercaPosizioneMaterialeFascicoli: " + ex);
		} finally {
			cleanup(lPosFascDao);
			cleanup(lPosDao);

			cleanup(lConn);
		}

		return lPosizioneFascicoli;
	}

	/**
	 * Ricerca Posizione Materiale paginata
	 *
	 * @param PosizioneMaterialeModel
	 * @param aPageNum
	 *            : numero pagina > 0
	 * @return Vettore di PosizioneMaterialeModel
	 * @throws F3BException
	 */
	public Vector ExRicercaPosizioneMaterialePagina(PosizioneMaterialeModel aPosizioneMateriale, int aPageNum)
			throws F3BException {

		Connection lConn = null;

		PosizioneMaterialeSqlDAO lPosDao = null;

		Vector lPosizioneMateriali = new Vector();

		try {
			lConn = getDBConnection();
			lPosDao = new PosizioneMaterialeSqlDAO(lConn);
			lPosDao.ricercaPosizioneMateriale(aPosizioneMateriale);

			// Ricerca paginata
			lPosDao.startPage(aPageNum);
			PosizioneMaterialeModel lPosMatModel = null;

			while (lPosDao.next()) {
				lPosMatModel = (PosizioneMaterialeModel) lPosDao.getModel();
				lPosizioneMateriali.add(lPosMatModel);
			}
			lPosDao.stop();

			if (lPosizioneMateriali.size() == 0) {
				throw new F3BException(F3BException.EX_NOT_FOUND, "Nessun Elemento trovato");
			}
		} catch (F3BException fe) {
			throw fe;
		} catch (Exception sqe) {
			throw new F3BException("PosizioneMaterialeController.ExRicercaPosizioneMaterialePagina: " + sqe);
		} finally {
			cleanup(lPosDao);

			cleanup(lConn);
		}

		return lPosizioneMateriali;
	}

	/**
	 * Ritorna n.ro di record risultato di una ExRicercaPosizioneMaterialePagina
	 *
	 * @param PosizioneMaterialeModel
	 * @return BigDecimal n.ro di record
	 * @throws F3BException
	 */
	public BigDecimal ExGetNumRicercaPosizioneMaterialePagina(PosizioneMaterialeModel aPosizioneMateriale)
			throws F3BException {

		Connection lConn = null;
		// Vector lPosizioneMateriali = new Vector();
		PosizioneMaterialeSqlDAO lPosDao = null;
		BigDecimal lCont = new BigDecimal(0);

		try {
			lConn = getDBConnection();
			lPosDao = new PosizioneMaterialeSqlDAO(lConn);
			lPosDao.ricercaPosizioneMateriale(aPosizioneMateriale);
			lCont = lPosDao.getNumRowsSelected();

		} catch (Exception sqe) {
			throw new F3BException(
					"PosizioneMaterialeController.ExRicercaPosizioneMateriale: Non posso leggere  : " + sqe);
		} finally {
			cleanup(lPosDao);
			cleanup(lConn);
		}
		return lCont;
	}

	public PosizioneMaterialeModel ExModificaPosizioneMateriale(PosizioneMaterialeModel aPosizioneMateriale)
			throws F3BException {

		Connection lConn = null;
		PosizioneMaterialeDAO lPosDao = null;
		PosizioneMaterialeModel lPosMod = new PosizioneMaterialeModel(aPosizioneMateriale);

		try {
			lConn = getDBConnection();
			lPosDao = new PosizioneMaterialeDAO(lConn);
			lPosDao.setDAOFromModelForUpdate(aPosizioneMateriale);
			lPosDao.update();
			commit(lConn);
		} catch (Exception ex) {
			rollback(lConn);
			throw new F3BException("PosizioneMaterialeController.ExModifica: Non posso inserire: " + ex);
		} finally {
			cleanup(lPosDao);
			cleanup(lConn);
		}
		return lPosMod;
	}

	public void ExCancellaPosizioneMateriale(PosizioneMaterialeModel aPosizioneMateriale)
			throws F3BException {

		Connection lConn = null;
		PosizioneMaterialeDAO lPosDao = null;

		try {
			lConn = getDBConnection();
			lPosDao = new PosizioneMaterialeDAO(lConn);
			lPosDao.setCondizione(aPosizioneMateriale.getCodPosizioneMateriale(),
					aPosizioneMateriale.getCodUfficio());
			lPosDao.delete();
			commit(lConn);
		} catch (DAOException daoEx) {
			if (daoEx.INTEGRITY_CONSTRAINT_VIOLATED)
				throw new F3BException(F3BException.USER_MESSAGE,
						"Posizione Materiale in uso. Impossibile effettuare la Cancellazione!");
			throw new F3BException("PosizioneMaterialeController.ExCancellaPosizioneMateriale: " + daoEx);
		} catch (Exception eEx) {
			rollback(lConn);
			throw new F3BException("PosizioneMaterialeController.ExCancellaPosizioneMateriale: " + eEx);
		} finally {
			cleanup(lPosDao);
			cleanup(lConn);
		}
	}

}