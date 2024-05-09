package siap.siep.penacomplessiva.controller;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import f3b.dao.DAOException;
import f3b.util.F3BException;
import siap.controller.SiapController;
import siap.siep.continuazione.dao.ContinuazioneDAO;
import siap.siep.continuazione.dao.ContinuazioneSqlDAO;
import siap.siep.continuazione.model.ContinuazioneModel;
import siap.siep.penacomplessiva.dao.PenaComplessivaDAO;
import siap.siep.penacomplessiva.dao.PenaComplessivaSqlDAO;
import siap.siep.penacomplessiva.model.DettaglioPenaComplessivaModel;
import siap.siep.penacomplessiva.model.PenaComplessivaModel;
import siap.siep.penacomplessiva.model.PenaComplessivaSanzioneSostitutivaModel;
import siap.siep.sanzionesostitutiva.dao.SanzioneSostitutivaDAO;
import siap.siep.sanzionesostitutiva.dao.SanzioneSostitutivaSqlDAO;
import siap.siep.sanzionesostitutiva.model.SanzioneSostitutivaModel;
import siap.sige.penacomplessiva.dao.PenaCompSenSigeDAO;
import siap.sige.penacomplessiva.model.PenaCompSigeModel;

/**
 * Title: PenaComplessivaController
 * Description: Classe Controller per PenaComplessiva
 *
 * @version 1.0
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class PenaComplessivaController extends SiapController implements IPenaComplessiva {

	public PenaComplessivaModel ExInserisciPenaComplessiva(PenaComplessivaModel aPenaComplessiva)
			throws F3BException {

		Connection lConn = null;

		PenaComplessivaDAO lPenDao = null;
		PenaComplessivaModel lPenMod = null;

		try {
			lConn = getDBConnection();

			lPenMod = new PenaComplessivaModel(aPenaComplessiva);
			lPenDao = new PenaComplessivaDAO(lConn);
			lPenDao.setDAOFromModel(aPenaComplessiva);
			BigDecimal lKey = null;
			lKey = lPenDao.insert();

			commit(lConn);

			lPenMod.setIdPenaComplessiva(lKey);
		} catch (DAOException ex) {
			rollback(lConn);
			throw new F3BException(
					"PenaComplessivaController.ExInserisciPenaComplessiva: Non posso inserire: " + ex);
		} finally {
			cleanup(lPenDao);
			cleanup(lConn);
		}

		return lPenMod;
	}

	public PenaComplessivaModel ExInserisciPenaCompSanzioneSostContinuazioni(
			PenaComplessivaModel aPenaComplessiva, SanzioneSostitutivaModel aSanzioneSostitutiva,
			List aContinuazioni) throws F3BException {

		return ExInserisciPenaCompSige(aPenaComplessiva, aSanzioneSostitutiva, aContinuazioni, null);
	}

	public PenaComplessivaModel ExInserisciPenaCompSige(PenaComplessivaModel aPenaComplessiva,
			SanzioneSostitutivaModel aSanzioneSostitutiva, List aContinuazioni, BigDecimal aIdFasSigeSen)
			throws F3BException {

		Connection lConn = null;

		PenaComplessivaDAO lPenDao = null;
		SanzioneSostitutivaDAO lSanzDao = null;
		ContinuazioneDAO lContDao = null;
		ContinuazioneSqlDAO lContSqlDAo = null;
		// tabella di Relazione per Pena Complessiva SIGE
		PenaCompSenSigeDAO lPenaCompSigeDAO = null;

		try {
			lConn = getDBTransaction();

			// Inserimento Pena Complessiva
			lPenDao = new PenaComplessivaDAO(lConn);

			lPenDao.setDAOFromModel(aPenaComplessiva);
			BigDecimal lKeyPenaComplessiva = null;
			lKeyPenaComplessiva = lPenDao.insert();

			// Inserimento Sanzione Sostitutiva
			if (aSanzioneSostitutiva != null) {
				lSanzDao = new SanzioneSostitutivaDAO(lConn);

				aSanzioneSostitutiva.setPenComIdPenaComplessiva(lKeyPenaComplessiva);

				lSanzDao.setDAOFromModel(aSanzioneSostitutiva);

				BigDecimal lKeySanzioneSostitutiva = null;
				lKeySanzioneSostitutiva = lSanzDao.insert();

				aSanzioneSostitutiva.setIdSanzioneSostitutiva(lKeySanzioneSostitutiva);
			}

			// Inserimento Continuazioni
			lContDao = new ContinuazioneDAO(lConn);
			lContSqlDAo = new ContinuazioneSqlDAO(lConn);

			Iterator lIter = aContinuazioni.iterator();
			while (lIter.hasNext()) {
				ContinuazioneModel lItem = (ContinuazioneModel) lIter.next();

				// Gestione Progressivo
				BigDecimal lProgr = lContSqlDAo.getProgressivoContinuazione(lKeyPenaComplessiva);
				lItem.setProgrContinuazione(new BigDecimal(lProgr.intValue() + 1));
				lItem.setPenComIdPenaComplessiva(lKeyPenaComplessiva);

				lContDao.setDAOFromModel(lItem);
				lContDao.insert();
				lContDao.stop();
			}

			aPenaComplessiva.setIdPenaComplessiva(lKeyPenaComplessiva);

			// Inserimento record di relazione in PENA_COMPLESSIVA_SENTENZA_SIGE
			if (aIdFasSigeSen != null) {
				PenaCompSigeModel lPenaCompSige = new PenaCompSigeModel(lKeyPenaComplessiva, aIdFasSigeSen);
				lPenaCompSigeDAO = new PenaCompSenSigeDAO(lConn);
				lPenaCompSigeDAO.setDAOFromModel(lPenaCompSige);
				lPenaCompSigeDAO.insert();
				lPenaCompSigeDAO.stop();
			}

			commit(lConn);
		} catch (DAOException ex) {
			rollback(lConn);
			throw new F3BException(
					"PenaComplessivaController.ExInserisciPenaCompSanzioneSostContinuazioni: " + ex);
		} catch (Exception ex) {
			rollback(lConn);
			throw new F3BException(
					"PenaComplessivaController.ExInserisciPenaCompSanzioneSostContinuazioni: " + ex);
		} finally {
			cleanup(lPenDao);
			cleanup(lSanzDao);
			cleanup(lContDao);
			cleanup(lContSqlDAo);
			cleanup(lPenaCompSigeDAO);
			cleanup(lConn);
		}

		return aPenaComplessiva;
	}

	public void ExInserisciUlterioriContinuazioni(List aContinuazioni) throws F3BException {

		Connection lConn = null;

		ContinuazioneDAO lContDao = null;
		ContinuazioneSqlDAO lContSqlDAo = null;

		try {
			lConn = getDBTransaction();

			// Inserimento Continuazioni
			lContDao = new ContinuazioneDAO(lConn);
			lContSqlDAo = new ContinuazioneSqlDAO(lConn);

			Iterator lIter = aContinuazioni.iterator();
			while (lIter.hasNext()) {
				ContinuazioneModel lItem = (ContinuazioneModel) lIter.next();

				// Gestione Progressivo
				BigDecimal lProgr = lContSqlDAo
						.getProgressivoContinuazione(lItem.getPenComIdPenaComplessiva());
				lItem.setProgrContinuazione(new BigDecimal(lProgr.intValue() + 1));

				lContDao.setDAOFromModel(lItem);
				lContDao.insert();
				lContDao.stop();
			}

			commit(lConn);
		} catch (DAOException ex) {
			rollback(lConn);
			ex.printStackTrace();
			throw new F3BException("PenaComplessivaController.ExInserisciUlterioriContinuazioni: " + ex);
		} catch (Exception ex) {
			rollback(lConn);
			ex.printStackTrace();
			throw new F3BException("PenaComplessivaController.ExInserisciUlterioriContinuazioni: " + ex);
		} finally {
			cleanup(lContDao);
			cleanup(lContSqlDAo);
			cleanup(lConn);
		}
	}

	public Vector ExRicercaPenaComplessiva(PenaComplessivaModel aPenaComplessiva) throws F3BException {

		Connection lConn = null;

		Vector lPenaComplessivi = new Vector();
		PenaComplessivaSqlDAO lPenDao = null;

		try {
			lConn = getDBConnection();
			lPenDao = new PenaComplessivaSqlDAO(lConn);
			lPenDao.ricercaPenaComplessiva(aPenaComplessiva);
			lPenaComplessivi = new Vector(lPenDao.getModels());
			if (lPenaComplessivi.size() == 0) {
				throw new F3BException(F3BException.USER_MESSAGE, "Nessun Elemento trovato");
			}
		} catch (DAOException daoEx) {
			throw new F3BException(
					"PenaComplessivaController.ExRicercaPenaComplessiva: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lPenDao);
			cleanup(lConn);
		}

		return lPenaComplessivi;
	}

	public Vector ExRicercaPenaComplessivaNoError(PenaComplessivaModel aPenaComplessiva) throws F3BException {

		Connection lConn = null;

		Vector lPenaComplessivi = new Vector();
		PenaComplessivaSqlDAO lPenDao = null;

		try {
			lConn = getDBConnection();
			lPenDao = new PenaComplessivaSqlDAO(lConn);
			lPenDao.ricercaPenaComplessiva(aPenaComplessiva);
			lPenaComplessivi = new Vector(lPenDao.getModels());
		} catch (DAOException daoEx) {
			throw new F3BException(
					"PenaComplessivaController.ExRicercaPenaComplessiva: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lPenDao);
			cleanup(lConn);
		}

		return lPenaComplessivi;
	}

	public PenaComplessivaModel ExRicercaPenaComplessivaByKey(BigDecimal aKey) throws F3BException {

		Connection lConn = null;

		PenaComplessivaSqlDAO lPenDao = null;
		PenaComplessivaModel lPenMod;

		try {
			lConn = getDBConnection();

			lPenDao = new PenaComplessivaSqlDAO(lConn);
			lPenDao.ricercaPenaComplessivaByKey(aKey);
			lPenMod = (PenaComplessivaModel) lPenDao.getModelByKey();
		} catch (DAOException daoEx) {
			throw new F3BException(
					"PenaComplessivaController.ExRicercaPenaComplessiva: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lPenDao);
			cleanup(lConn);
		}
		return lPenMod;
	}

	public PenaComplessivaModel ExRicercaPenaComplessivaByIdFascicolo(BigDecimal aKey) throws F3BException {

		Connection lConn = null;

		PenaComplessivaSqlDAO lPenDao = null;
		PenaComplessivaModel lPenMod;

		try {
			lConn = getDBConnection();

			lPenDao = new PenaComplessivaSqlDAO(lConn);
			lPenDao.ricercaPenaComplessivaByIdFascicolo(aKey);
			lPenMod = (PenaComplessivaModel) lPenDao.getModelByKey();
		} catch (DAOException daoEx) {
			throw new F3BException(
					"PenaComplessivaController.ExRicercaPenaComplessiva: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lPenDao);
			cleanup(lConn);
		}
		return lPenMod;
	}

	public PenaComplessivaSanzioneSostitutivaModel ExRicercaPenaComplessivaSanzioneSostitutivaByKey(
			BigDecimal aKey) throws F3BException {

		Connection lConn = null;

		PenaComplessivaSqlDAO lPenDao = null;
		SanzioneSostitutivaSqlDAO lSanDao = null;

		PenaComplessivaSanzioneSostitutivaModel lPenSanMod = null;
		PenaComplessivaModel lPenMod = null;
		SanzioneSostitutivaModel lSanMod = null;

		try {
			lConn = getDBConnection();

			lPenDao = new PenaComplessivaSqlDAO(lConn);
			lPenDao.ricercaPenaComplessivaByKey(aKey);

			lPenMod = (PenaComplessivaModel) lPenDao.getModelByKey();

			if (lPenMod != null) {
				lSanDao = new SanzioneSostitutivaSqlDAO(lConn);
				lSanDao.ricercaSanzioneSostitutivaByIdPenaComplessiva(lPenMod.getIdPenaComplessiva());

				lSanDao.start();
				if (lSanDao.next()) {
					lSanMod = (SanzioneSostitutivaModel) lSanDao.getModel();
				}
				lSanDao.stop();

				lPenSanMod = new PenaComplessivaSanzioneSostitutivaModel(lPenMod, lSanMod);
			}
		} catch (DAOException daoEx) {
			throw new F3BException(
					"PenaComplessivaController.ExRicercaPenaComplessivaSanzioneSostitutivaByKey: " + daoEx);
		} finally {
			cleanup(lPenDao);
			cleanup(lSanDao);
			cleanup(lConn);
		}

		return lPenSanMod;
	}

	public PenaComplessivaSanzioneSostitutivaModel ExRicercaPenaComplessivaSanzioneSostitutivaByIdFascicoloSiep(
			BigDecimal aIdFascicoloSiep) throws F3BException {

		Connection lConn = null;

		PenaComplessivaSqlDAO lPenDao = null;
		SanzioneSostitutivaSqlDAO lSanDao = null;

		PenaComplessivaSanzioneSostitutivaModel lPenSanMod = null;
		PenaComplessivaModel lPenMod = null;
		SanzioneSostitutivaModel lSanMod = null;

		try {
			lConn = getDBConnection();

			lPenDao = new PenaComplessivaSqlDAO(lConn);
			lPenDao.ricercaPenaComplessivaByIdFascicolo(aIdFascicoloSiep);

			lPenDao.start();
			if (lPenDao.next()) {
				lPenMod = (PenaComplessivaModel) lPenDao.getModel();
			}
			lPenDao.stop();

			if (lPenMod != null) {
				lSanDao = new SanzioneSostitutivaSqlDAO(lConn);
				lSanDao.ricercaSanzioneSostitutivaByIdPenaComplessiva(lPenMod.getIdPenaComplessiva());

				lSanDao.start();
				if (lSanDao.next()) {
					lSanMod = (SanzioneSostitutivaModel) lSanDao.getModel();
				}
				lSanDao.stop();

				lPenSanMod = new PenaComplessivaSanzioneSostitutivaModel(lPenMod, lSanMod);
			}
		} catch (DAOException daoEx) {
			throw new F3BException(
					"PenaComplessivaController.ExRicercaPenaComplessivaSanzioneSostitutivaByIdFascicoloSiep: "
							+ daoEx);
		} finally {
			cleanup(lPenDao);
			cleanup(lSanDao);
			cleanup(lConn);
		}

		return lPenSanMod;
	}

	public DettaglioPenaComplessivaModel ExRicercaPenaCompSanzioneSostContinuazioniByKey(
			BigDecimal aIdPenaComplessiva) throws F3BException {

		Connection lConn = null;

		PenaComplessivaSqlDAO lPenDao = null;
		SanzioneSostitutivaSqlDAO lSanDao = null;
		ContinuazioneSqlDAO lContDao = null;

		DettaglioPenaComplessivaModel lDettPenCompl = null;

		PenaComplessivaModel lPenMod = null;
		SanzioneSostitutivaModel lSanMod = null;
		List lListCircMod = null;

		try {
			lConn = getDBConnection();

			lPenDao = new PenaComplessivaSqlDAO(lConn);
			lPenDao.ricercaPenaComplessivaByKey(aIdPenaComplessiva);

			lPenDao.start();
			if (lPenDao.next()) {
				lPenMod = (PenaComplessivaModel) lPenDao.getModel();
			}
			lPenDao.stop();

			if (lPenMod != null) {
				lSanDao = new SanzioneSostitutivaSqlDAO(lConn);
				lSanDao.ricercaSanzioneSostitutivaByIdPenaComplessiva(lPenMod.getIdPenaComplessiva());

				lSanDao.start();
				if (lSanDao.next()) {
					lSanMod = (SanzioneSostitutivaModel) lSanDao.getModel();
				}
				lSanDao.stop();

				lContDao = new ContinuazioneSqlDAO(lConn);
				lContDao.ricercaContinuazioneByIdPenaComplessiva(aIdPenaComplessiva);
				lListCircMod = new ArrayList(lContDao.getModels());

				PenaComplessivaSanzioneSostitutivaModel lMod = new PenaComplessivaSanzioneSostitutivaModel(
						lPenMod, lSanMod);
				lDettPenCompl = new DettaglioPenaComplessivaModel(lMod, lListCircMod);
			}
		} catch (DAOException daoEx) {
			throw new F3BException(
					"PenaComplessivaController.ExRicercaPenaCompSanzioneSostContinuazioniByKey: " + daoEx);
		} finally {
			cleanup(lPenDao);
			cleanup(lSanDao);
			cleanup(lContDao);
			cleanup(lConn);
		}

		return lDettPenCompl;
	}

	public DettaglioPenaComplessivaModel ExRicercaPenaCompSanzioneSostContinuazioniByIdFascicolo(
			BigDecimal aIdFascicolo) throws F3BException {

		Connection lConn = null;

		PenaComplessivaSqlDAO lPenDao = null;
		SanzioneSostitutivaSqlDAO lSanDao = null;
		ContinuazioneSqlDAO lContDao = null;

		DettaglioPenaComplessivaModel lDettPenCompl = null;

		PenaComplessivaModel lPenMod = null;
		SanzioneSostitutivaModel lSanMod = null;
		List lListCircMod = null;

		try {
			lConn = getDBConnection();

			lPenDao = new PenaComplessivaSqlDAO(lConn);
			lPenDao.ricercaPenaComplessivaByIdFascicolo(aIdFascicolo);

			lPenDao.start();
			if (lPenDao.next()) {
				lPenMod = (PenaComplessivaModel) lPenDao.getModel();
			}
			lPenDao.stop();

			if (lPenMod != null) {
				lSanDao = new SanzioneSostitutivaSqlDAO(lConn);
				lSanDao.ricercaSanzioneSostitutivaByIdPenaComplessiva(lPenMod.getIdPenaComplessiva());

				lSanDao.start();
				if (lSanDao.next()) {
					lSanMod = (SanzioneSostitutivaModel) lSanDao.getModel();
				}
				lSanDao.stop();

				lContDao = new ContinuazioneSqlDAO(lConn);
				lContDao.ricercaContinuazioneByIdPenaComplessiva(lPenMod.getIdPenaComplessiva());
				lListCircMod = new ArrayList(lContDao.getModels());

				PenaComplessivaSanzioneSostitutivaModel lMod = new PenaComplessivaSanzioneSostitutivaModel(
						lPenMod, lSanMod);
				lDettPenCompl = new DettaglioPenaComplessivaModel(lMod, lListCircMod);
			}
		} catch (DAOException daoEx) {
			throw new F3BException(
					"PenaComplessivaController.ExRicercaPenaCompSanzioneSostContinuazioniByIdFascicolo: "
							+ daoEx);
		} finally {
			cleanup(lPenDao);
			cleanup(lSanDao);
			cleanup(lContDao);
			cleanup(lConn);
		}

		return lDettPenCompl;
	}

	public PenaComplessivaModel ExModificaPenaComplessiva(PenaComplessivaModel aPenaComplessiva)
			throws F3BException {

		Connection lConn = null;

		PenaComplessivaDAO lPenDao = null;
		PenaComplessivaModel lPenMod = new PenaComplessivaModel(aPenaComplessiva);

		try {
			lConn = getDBConnection();
			lPenDao = new PenaComplessivaDAO(lConn);
			lPenDao.setDAOFromModelForUpdate(aPenaComplessiva);
			lPenDao.update();

			commit(lConn);
		} catch (DAOException ex) {
			rollback(lConn);
			throw new F3BException("PenaComplessivaController.ExModifica: Non posso inserire: " + ex);
		} finally {
			cleanup(lPenDao);
			cleanup(lConn);
		}

		return lPenMod;
	}

	public PenaComplessivaSanzioneSostitutivaModel ExModificaPenaComplessivaSanzioneSostitutiva(
			PenaComplessivaModel aPenaComplessiva, SanzioneSostitutivaModel aSanzioneSostitutiva,
			boolean aflagSanzioneSostitutiva) throws F3BException {

		Connection lConn = null;

		PenaComplessivaDAO lPenDao = null;
		SanzioneSostitutivaDAO lSanzDao = null;

		PenaComplessivaSanzioneSostitutivaModel aPenaComplessivaSanzioneSostitutiva = null;

		try {
			lConn = getDBTransaction();

			lPenDao = new PenaComplessivaDAO(lConn);

			// Modifica Pena Complessiva
			lPenDao = new PenaComplessivaDAO(lConn);
			lPenDao.setDAOFromModelForUpdate(aPenaComplessiva);
			lPenDao.update();

			// Controlla la presenza della Sanzione Sostitutiva
			lSanzDao = new SanzioneSostitutivaDAO(lConn);
			if (aSanzioneSostitutiva != null) {
				if (aflagSanzioneSostitutiva) {
					if (aSanzioneSostitutiva.getIdSanzioneSostitutiva() == null) {
						// Se non presente la inserisce
						lSanzDao.setDAOFromModel(aSanzioneSostitutiva);
						BigDecimal lKeySanzioneSostitutiva = null;
						lKeySanzioneSostitutiva = lSanzDao.insert();
						aSanzioneSostitutiva.setIdSanzioneSostitutiva(lKeySanzioneSostitutiva);
					} else {
						// Altrimenti la modifica
						lSanzDao.setDAOFromModelForUpdate(aSanzioneSostitutiva);
						lSanzDao.update();
					}
				} else {
					if (aSanzioneSostitutiva.getIdSanzioneSostitutiva() != null) {
						lSanzDao.setCondizioneUpdate(aSanzioneSostitutiva.getIdSanzioneSostitutiva());
						lSanzDao.delete();

					}

				}

			}

			commit(lConn);

			aPenaComplessivaSanzioneSostitutiva = new PenaComplessivaSanzioneSostitutivaModel(
					aPenaComplessiva, aSanzioneSostitutiva);
		} catch (DAOException ex) {
			rollback(lConn);
			throw new F3BException("PenaComplessivaController.ExModifica: Non posso inserire: " + ex);
		} catch (Exception ex) {
			rollback(lConn);
			throw new F3BException(
					"PenaComplessivaController.ExInserisciPenaComplessivaSanzioneSostitutiva: " + ex);
		} finally {
			cleanup(lPenDao);
			cleanup(lSanzDao);
			cleanup(lConn);
		}

		return aPenaComplessivaSanzioneSostitutiva;
	}

	public void ExCancellaPenaComplessiva(PenaComplessivaModel aPenaComplessiva) throws F3BException {

		Connection lConn = null;

		PenaComplessivaDAO lPenDao = null;

		try {
			lConn = getDBConnection();

			lPenDao = new PenaComplessivaDAO(lConn);

			lPenDao.setCondizioneUpdate(aPenaComplessiva.getIdPenaComplessiva());

			lPenDao.delete();

			commit(lConn);
		} catch (DAOException daoEx) {
			throw new F3BException("PenaComplessivaController.ExCancellaPenaComplessiva: " + daoEx);
		} finally {
			cleanup(lPenDao);
			cleanup(lConn);
		}
	}

	public void ExCancellaPenaComplessivaSanzioneSostitutivaContinuazioni(
			PenaComplessivaModel aPenaComplessiva) throws F3BException {

		Connection lConn = null;
		/*
		 * PenaComplessivaDAO lPenDao = null; SanzioneSostitutivaDAO lSanDao = null; ContinuazioneDAO lConDao
		 * = null;
		 */
		try {
			lConn = getDBTransaction();

			cancellaPenaComplessivaSanzioneSostitutivaContinuazioni(aPenaComplessiva, lConn);

			/*
			 * //** Cancella prima i record associati //** e poi cancella Pena Complessiva BigDecimal
			 * lIdPenaComplessiva = aPenaComplessiva.getIdPenaComplessiva();
			 *
			 * //* Cancella la Sanzione Sostitutiva associata lSanDao = new SanzioneSostitutivaDAO(lConn);
			 * lSanDao.setCondizioneByIdPenaComplessiva(lIdPenaComplessiva); lSanDao.delete();
			 *
			 * //* Cancella le Continuazioni associate lConDao = new ContinuazioneDAO(lConn);
			 * lConDao.setCondizioneByIdPenaComplessiva(lIdPenaComplessiva); lConDao.delete();
			 *
			 * //* Cancella la Pena Complessiva lPenDao = new PenaComplessivaDAO(lConn);
			 * lPenDao.setCondizioneUpdate(lIdPenaComplessiva); lPenDao.delete();
			 */
			commit(lConn);
		} catch (F3BException fe) {
			rollback(lConn);
			throw fe;
		} catch (Exception ex) {
			rollback(lConn);
			throw new F3BException(
					"PenaComplessivaController.ExCancellaPenaComplessivaSanzioneSostitutivaContinuazioni-> "
							+ ex);
		} finally {
			cleanup(lConn);
		}
	}

	private void cancellaPenaComplessivaSanzioneSostitutivaContinuazioni(
			PenaComplessivaModel aPenaComplessiva, Connection lConn) throws F3BException {

		PenaComplessivaDAO lPenDao = null;
		SanzioneSostitutivaDAO lSanDao = null;
		ContinuazioneDAO lConDao = null;

		try {

			// ** Cancella prima i record associati
			// ** e poi cancella Pena Complessiva
			BigDecimal lIdPenaComplessiva = aPenaComplessiva.getIdPenaComplessiva();

			// * Cancella la Sanzione Sostitutiva associata
			lSanDao = new SanzioneSostitutivaDAO(lConn);
			lSanDao.setCondizioneByIdPenaComplessiva(lIdPenaComplessiva);
			lSanDao.delete();

			// * Cancella le Continuazioni associate
			lConDao = new ContinuazioneDAO(lConn);
			lConDao.setCondizioneByIdPenaComplessiva(lIdPenaComplessiva);
			lConDao.delete();

			// * Cancella la Pena Complessiva
			lPenDao = new PenaComplessivaDAO(lConn);
			lPenDao.setCondizioneUpdate(lIdPenaComplessiva);
			lPenDao.delete();
		} catch (Exception ex) {
			throw new F3BException(
					"PenaComplessivaController.CancellaPenaComplessivaSanzioneSostitutivaContinuazioni: "
							+ ex);
		} finally {
			cleanup(lPenDao);
			cleanup(lSanDao);
			cleanup(lConDao);
		}
	}

	public String ExInserisciPenaComplessivaWithoutSequence(
			PenaComplessivaSanzioneSostitutivaModel aPenaComplessiva, Connection lConn) throws F3BException {

		PenaComplessivaDAO lPenDao = null;
		SanzioneSostitutivaDAO lSanzDao = null;
		ContinuazioneDAO lContDao = null;
		ContinuazioneSqlDAO lContSqlDAo = null;
		String lCodEsito = "00000";

		try {
			// Inserimento Pena Complessiva
			lPenDao = new PenaComplessivaDAO(lConn);

			lPenDao.setDAOFromModel(aPenaComplessiva.getPenaComplessiva());
			lPenDao.setWithoutSequence(true);
			lPenDao.insert();

			// Inserimento Sanzione Sostitutiva
			if (aPenaComplessiva.getSanzioneSostitutiva() != null) {
				lSanzDao = new SanzioneSostitutivaDAO(lConn);

				lSanzDao.setDAOFromModel(aPenaComplessiva.getSanzioneSostitutiva());
				lSanzDao.setWithoutSequence(true);
				lSanzDao.insert();
			}

			// Inserimento Continuazioni
			/*---GDV per ora non trattate....
			    lContDao = new ContinuazioneDAO(lConn);
			    lContSqlDAo = new ContinuazioneSqlDAO(lConn);

			    Iterator lIter = aContinuazioni.iterator();
			    while (lIter.hasNext())
			    {
			      ContinuazioneModel lItem = (ContinuazioneModel) lIter.next();

			      //Gestione Progressivo
			      BigDecimal lProgr = lContSqlDAo.getProgressivoContinuazione(lKeyPenaComplessiva);
			      lItem.setProgrContinuazione(new BigDecimal(lProgr.intValue() + 1));
			      lItem.setPenComIdPenaComplessiva(lKeyPenaComplessiva);

			      lContDao.setDAOFromModel(lItem);
			      lContDao.insert();
			      lContDao.stop();
			    }

			 */
		} catch (DAOException ex) {
			if (ex.UNIQUE_CONSTRAINT_VIOLATED) {
				// Inizializzo la chiave del fascicolo con quella inviatami
				// lChiave = lPars.getFascicolo().getIdFascicoloSiep();
				lCodEsito = "00001";
			} else {
				lCodEsito = "01400";
				throw new F3BException(F3BException.USER_MESSAGE,
						"Impossibile inserire la Pena Complessiva! ");
			}
		} finally {
			cleanup(lPenDao);
			cleanup(lSanzDao);
			cleanup(lContDao);
			cleanup(lContSqlDAo);
		}

		return lCodEsito;
	}

	public DettaglioPenaComplessivaModel ExRicercaPenaComplessivaCompletaByIdSIGE(BigDecimal aKey)
			throws F3BException {

		Connection lConn = null;
		PenaCompSenSigeDAO lPenaCompSigeDAO = null;

		// Pena Complessiva risultato della ricerca
		DettaglioPenaComplessivaModel lPenSanMod = null;

		try {
			lConn = getDBConnection();
			lPenaCompSigeDAO = new PenaCompSenSigeDAO(lConn);

			// Si cerca nella tabella di relazione ID Pena Complessiva
			lPenaCompSigeDAO.setCondizioneIdFasSigeSen(aKey);
			PenaCompSigeModel lPenaCompSige = (PenaCompSigeModel) lPenaCompSigeDAO.getModelByKey();

			// Si richiama la ricerca Pena Complessiva per ID
			if (lPenaCompSige != null && lPenaCompSige.getIdPenaComplessiva() != null)
				lPenSanMod = ExRicercaPenaCompSanzioneSostContinuazioniByKey(
						lPenaCompSige.getIdPenaComplessiva());
		} catch (DAOException daoEx) {
			throw new F3BException(
					"PenaComplessivaController.ExRicercaPenaComplessivaCompletaByIdSIGE: " + daoEx);
		} catch (F3BException fe) {
			throw fe;
		} catch (Exception e) {
			throw new F3BException(
					"PenaComplessivaController.ExRicercaPenaComplessivaCompletaByIdSIGE-> " + e);
		} finally {
			cleanup(lPenaCompSigeDAO);
			cleanup(lConn);
		}

		return lPenSanMod;
	}

	/**
	 * Cancellazione di una Pena Complessiva in relazione con un Procedimento SIGE.
	 */
	public void ExCancellaPenaComplessivaSige(PenaComplessivaModel aPenaComplessiva) throws F3BException {

		Connection lConn = null;
		PenaCompSenSigeDAO lPenComSigeDAO = null;

		try {
			lConn = getDBTransaction();

			lPenComSigeDAO = new PenaCompSenSigeDAO(lConn);

			// Prima si cancella il riferimento nella tabella di relazione
			lPenComSigeDAO.setCondizioneDeletePenaComplessiva(aPenaComplessiva.getIdPenaComplessiva());
			lPenComSigeDAO.delete();
			lPenComSigeDAO.stop();

			// Poi si cancellano i dati standard
			cancellaPenaComplessivaSanzioneSostitutivaContinuazioni(aPenaComplessiva, lConn);

			commit(lConn);
		} catch (F3BException fe) {
			rollback(lConn);
			throw fe;
		} catch (Exception ex) {
			rollback(lConn);
			throw new F3BException("PenaComplessivaController.ExCancellaPenaComplessivaSige -> " + ex);
		} finally {
			cleanup(lPenComSigeDAO);
			cleanup(lConn);
		}
	}

	/**
	 * Aggiunto metodo di ricerca pena sostituiva per tipologia (highValue della
	 * cg_ref_codes.TIPO_SANZIONE_SOSTITUTIVA)
	 * 
	 * @author 	sgioggi
	 * @since	MEV_2023-33
	 * 
	 * @param 	idFascicoloSiep
	 * @param 	highValue
	 * @return 	PenaComplessivaSanzioneSostitutivaModel
	 * @throws	F3BException
	 */
	@Override
	public PenaComplessivaSanzioneSostitutivaModel ExRicercaPenaComplessivaPenaSostitutivaByIdFascicoloSiep(
			BigDecimal idFascicoloSiep, String highValue) throws F3BException {

		Connection c = null;

		PenaComplessivaSqlDAO pcsdao = null;
		SanzioneSostitutivaSqlDAO sssdao = null;

		PenaComplessivaSanzioneSostitutivaModel pcssm = null;
		PenaComplessivaModel pcm = null;
		SanzioneSostitutivaModel ssm = null;

		try {
			c = getDBConnection();

			pcsdao = new PenaComplessivaSqlDAO(c);
			pcsdao.ricercaPenaComplessivaByIdFascicolo(idFascicoloSiep);

			pcsdao.start();
			if (pcsdao.next())
				pcm = (PenaComplessivaModel) pcsdao.getModel();
			pcsdao.stop();

			if (pcm != null) {
				sssdao = new SanzioneSostitutivaSqlDAO(c);
				sssdao.ricercaPenaSostitutivaByIdPenaComplessiva(pcm.getIdPenaComplessiva(), highValue);
				sssdao.start();
				if (sssdao.next())
					ssm = (SanzioneSostitutivaModel) sssdao.getModel();
				sssdao.stop();
				pcssm = new PenaComplessivaSanzioneSostitutivaModel(pcm, ssm);
			}
		} catch (DAOException daoEx) {
			throw new F3BException(
					"PenaComplessivaController.ExRicercaPenaComplessivaPenaSostitutivaByIdFascicoloSiep: "
							+ daoEx);
		} finally {
			cleanup(pcsdao);
			cleanup(sssdao);

			cleanup(c);
		}

		return pcssm;
	}
	// FINE MEV_2023-33

}