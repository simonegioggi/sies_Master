package siap.siep.fungibilita.controller;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.dao.DAOException;
import f3b.log.LogF3B;
import f3b.util.F3BException;
import siap.controller.SiapController;
import siap.siep.fungibilita.dao.FungibilitaDAO;
import siap.siep.fungibilita.dao.FungibilitaSqlDAO;
import siap.siep.fungibilita.model.FungibilitaModel;

/**
 * <p>
 * Title: FungibilitaController
 * </p>
 * <p>
 * Description: Classe Controller per Fungibilita
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
public class FungibilitaController extends SiapController implements IFungibilita {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public FungibilitaModel ExInserisciFungibilita(FungibilitaModel aFungibilita) throws F3BException {

		Connection lConn = null;
		FungibilitaDAO lFunDao = null;
		FungibilitaModel lFunMod = null;

		try {
			lConn = getDBConnection();

			lFunMod = new FungibilitaModel(aFungibilita);

			lFunDao = new FungibilitaDAO(lConn);
			lFunDao.setDAOFromModel(aFungibilita);
			BigDecimal lKey = null;
			lKey = lFunDao.insert();
			lFunMod.setIdFungibilita(lKey);
			commit(lConn);
		} catch (DAOException ex) {
			rollback(lConn);
			throw new F3BException("FungibilitaController.ExInserisci: Non posso inserire: " + ex);
		} finally {
			cleanup(lFunDao);
			cleanup(lConn);
		}

		return lFunMod;
	}

	/*
	 * public Vector ExRicercaFungibilita(FungibilitaModel aFungibilita) throws F3BException { Connection
	 * lConn = null;
	 *
	 * Vector lFungibiliti = new Vector(); FungibilitaSqlDAO lFunDao = null;
	 *
	 * try { lConn = getDBConnection(); lFunDao = new FungibilitaSqlDAO(lConn);
	 * lFunDao.ricercaFungibilita(aFungibilita); lFungibiliti = new Vector(lFunDao.getModels());
	 *
	 * if (lFungibiliti.size() == 0) { throw new F3BException(F3BException.USER_MESSAGE,
	 * "Nessun Elemento trovato"); } } catch (DAOException daoEx) { throw new
	 * F3BException("FungibilitaController.ExRicercaFungibilita: Non posso leggere : " + daoEx); } catch
	 * (SQLException sqe) { throw new
	 * F3BException("FungibilitaController.ExRicercaFungibilita: Non posso leggere  : " + sqe); } finally {
	 * cleanup(lFunDao); cleanup(lConn); }
	 *
	 * return lFungibiliti; }
	 */

	public FungibilitaModel ExRicercaFungibilitaByKey(BigDecimal aKey) throws F3BException {

		Connection lConn = null;
		FungibilitaSqlDAO lFunDao = null;
		FungibilitaModel lFunMod;

		try {
			lConn = getDBConnection();
			lFunDao = new FungibilitaSqlDAO(lConn);
			lFunDao.ricercaFungibilitaByKey(aKey);
			lFunMod = (FungibilitaModel) lFunDao.getModelByKey();
		} catch (DAOException daoEx) {
			throw new F3BException(
					"FungibilitaController.ExRicercaFungibilita: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lFunDao);
			cleanup(lConn);
		}

		return lFunMod;
	}

	public FungibilitaModel ExRicercaFungibilitaByKeyEvento(BigDecimal aKey) throws F3BException {

		Connection lConn = null;

		FungibilitaSqlDAO lFunDao = null;
		FungibilitaModel lFunMod;

		try {
			lConn = getDBConnection();
			lFunDao = new FungibilitaSqlDAO(lConn);
			lFunDao.ricercaFungibilitaByKeyEvento(aKey);
			lFunMod = (FungibilitaModel) lFunDao.getModelByKey();
		} catch (DAOException daoEx) {
			throw new F3BException(
					"FungibilitaController.ExRicercaFungibilitaByKeyEvento: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lFunDao);
			cleanup(lConn);
		}

		return lFunMod;
	}

	/**
	 * Ricerca Fungibilità tramite chiave Fascicolo
	 *
	 * @param aIdFasicolo
	 * @return lFungibilità
	 * @throws F3BException
	 */
	public Vector ExRicercaFungibilitaByIdFascicolo(BigDecimal aIdFascicolo) throws F3BException {

		Connection lConn = null;

		Vector lFungibilita = new Vector();
		FungibilitaSqlDAO lFunDao = null;
		// FungibilitaModel lFunMod;

		try {
			lConn = getDBConnection();

			lFunDao = new FungibilitaSqlDAO(lConn);
			lFunDao.ricercaFungibilitaDescByIdFascicolo(aIdFascicolo);
			lFungibilita = new Vector(lFunDao.getModels());
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException("FungibilitaController.ExRicercaFungibilitaByIdFascicolo: " + daoEx);
		} finally {
			cleanup(lFunDao);
			cleanup(lConn);
		}
		return lFungibilita;
	}

	/**
	 * Inserisci i records di Fungibilità per JMS senza assegnare la sequence
	 *
	 * @param aFungibilita
	 * @param lConn
	 * @return lCodEsito
	 * @throws F3BException
	 */
	public String ExInserisciFungibilitaWithoutSequence(ArrayList aFungibilita, ArrayList aEventiInseriti,
			Connection lConn) throws F3BException {

		String lCodEsito = "00000";
		FungibilitaDAO lPenDao = null;
		FungibilitaModel lPenResMod = null;

		try {
			lPenDao = new FungibilitaDAO(lConn);

			if (aFungibilita != null && aFungibilita.size() > 0) {
				for (int i = 0; i < aFungibilita.size(); i++) {
					lPenResMod = (FungibilitaModel) aFungibilita.get(i);
					if (lPenResMod != null) {
						if (lPenResMod.getIdFungibilita() != null
								&& aEventiInseriti.contains(lPenResMod.getEveIdEvento())) {
							lPenDao.setDAOFromModel(lPenResMod);
							lPenDao.setWithoutSequence(true);
							lPenDao.insert();
							lPenDao.stop();
						}
					}
				}
			}
		} catch (DAOException ex) {
			if (ex.UNIQUE_CONSTRAINT_VIOLATED) {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
				siesLogger.error("Fungibilità gia' presente...");
				lCodEsito = "00001";
			} else {
				lCodEsito = "01400";
				throw new F3BException(F3BException.USER_MESSAGE, "Impossibile inserire la Fungibilità! ");
			}
		} finally {
			cleanup(lPenDao);
		}
		return lCodEsito;
	}

	public FungibilitaModel ExModificaFungibilita(FungibilitaModel aFungibilita) throws F3BException {

		Connection lConn = null;
		FungibilitaDAO lFunDao = null;
		FungibilitaModel lFunMod = new FungibilitaModel(aFungibilita);

		try {
			lConn = getDBConnection();
			lFunDao = new FungibilitaDAO(lConn);
			lFunDao.setDAOFromModelForUpdate(aFungibilita);
			lFunDao.update();

			commit(lConn);
		} catch (DAOException ex) {
			rollback(lConn);
			throw new F3BException("FungibilitaController.ExModifica: Non posso inserire: " + ex);
		} finally {
			cleanup(lFunDao);
			cleanup(lConn);
		}

		return lFunMod;
	}

	public void ExCancellaFungibilita(FungibilitaModel aFungibilita) throws F3BException {

		Connection lConn = null;
		FungibilitaDAO lFunDao = null;

		try {
			lConn = getDBConnection();
			lFunDao = new FungibilitaDAO(lConn);
			lFunDao.setCondizioneUpdate(aFungibilita.getIdFungibilita());
			lFunDao.delete();
			commit(lConn);
		} catch (DAOException daoEx) {
			throw new F3BException(
					"FungibilitaController.ExCancellaFungibilita: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lFunDao);
			cleanup(lConn);
		}
	}

	/**
	*
	*/
	public void ExValidaFungibilita(BigDecimal aKey) throws F3BException {

		Connection lConn = null;
		FungibilitaDAO lFunDao = null;

		try {
			lConn = getDBConnection();

			lFunDao = new FungibilitaDAO(lConn);

			lFunDao.setFlagValidato("S");
			lFunDao.setCondizioneUpdate(aKey);
			lFunDao.update();

			commit(lConn);
		} catch (DAOException daoEx) {
			throw new F3BException(
					"FungibilitaController.ExCancellaFungibilita: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lFunDao);
			cleanup(lConn);
		}
	}

}