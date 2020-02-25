package siap.siep.penacumulo.controller;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Vector;

import org.apache.log4j.Logger;

import siap.controller.SiapController;
import siap.siep.penacumulo.dao.PenaCumuloDAO;
import siap.siep.penacumulo.dao.PenaCumuloSqlDAO;
import siap.siep.penacumulo.model.PenaCumuloModel;
import f3b.dao.DAOException;
import f3b.log.LogF3B;
import f3b.util.F3BException;

/**
 * <p>
 * Title: PenaCumuloController
 * </p>
 * <p>
 * Description: Classe Controller per PenaCumulo
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
public class PenaCumuloController extends SiapController implements IPenaCumulo {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	/**
	 * Funzione di inserimento di un record PENA_CUMULO
	 * 
	 * @param aPenaCumulo
	 */
	public PenaCumuloModel ExInserisciPenaCumulo(PenaCumuloModel aPenaCumulo) throws F3BException {
		Connection lConn = null;
		PenaCumuloDAO lPenDao = null;
		PenaCumuloModel lPenMod = null;

		try {
			lConn = getDBConnection();
			lPenMod = new PenaCumuloModel(aPenaCumulo);
			lPenDao = new PenaCumuloDAO(lConn);
			lPenDao.setDAOFromModel(aPenaCumulo);
			BigDecimal lKey = null;
			lKey = lPenDao.insert();
			commit(lConn);
			lPenMod.setIdPenaCumulo(lKey);
		} catch (DAOException ex) {
			rollback(lConn);
			throw new F3BException("PenaCumuloController.ExInserisci: Non posso inserire: " + ex);
		} finally {
			cleanup(lPenDao);
			cleanup(lConn);
		}

		return lPenMod;
	}

	/**
	 * Metodo di ricerca
	 *
	 */
	public Vector ExRicercaPenaCumulo(PenaCumuloModel aPenaCumulo) throws F3BException {
		Connection lConn = null;
		Vector lPenaCumuli = new Vector();
		PenaCumuloSqlDAO lPenDao = null;

		try {
			lConn = getDBConnection();
			lPenDao = new PenaCumuloSqlDAO(lConn);
			lPenDao.ricercaPenaCumulo(aPenaCumulo);
			lPenaCumuli = new Vector(lPenDao.getModels());

			if (lPenaCumuli.size() == 0) {
				throw new F3BException(F3BException.USER_MESSAGE, "Nessun Elemento trovato");
			}
		} catch (DAOException daoEx) {
			throw new F3BException("PenaCumuloController.ExRicercaPenaCumulo: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lPenDao);
			cleanup(lConn);
		}

		return lPenaCumuli;

	}

	/**
	 * Ricerca PENA_CUMULO per id pena
	 */
	public PenaCumuloModel ExRicercaPenaCumuloByKey(BigDecimal aKey) throws F3BException {
		Connection lConn = null;
		PenaCumuloSqlDAO lPenDao = null;
		PenaCumuloModel lPenMod;

		try {
			lConn = getDBConnection();
			lPenDao = new PenaCumuloSqlDAO(lConn);
			lPenDao.ricercaPenaCumuloByKey(aKey);
			lPenMod = (PenaCumuloModel) lPenDao.getModelByKey();
		} catch (DAOException daoEx) {
			throw new F3BException("PenaCumuloController.ExRicercaPenaCumulo: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lPenDao);
			cleanup(lConn);
		}

		return lPenMod;

	}

	/**
	 * Ricerca PENA_COMULO per id Cumulo
	 * 
	 * @param idClumulo
	 */
	public PenaCumuloModel ExRicercaPenaCumuloByIdCumulo(BigDecimal aKey) throws F3BException {
		Connection lConn = null;
		PenaCumuloSqlDAO lPenDao = null;
		PenaCumuloModel lPenMod;

		try {
			lConn = getDBConnection();
			lPenDao = new PenaCumuloSqlDAO(lConn);
			lPenDao.ricercaPenaCumuloByIdCumulo(aKey);
			lPenMod = (PenaCumuloModel) lPenDao.getModelByKey();
		} catch (DAOException daoEx) {
			throw new F3BException("PenaCumuloController.ExRicercaPenaCumulo: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lPenDao);
			cleanup(lConn);
		}

		return lPenMod;
	}

	/**
	 * Aggiornamento PENA_CUMULO
	 */
	public PenaCumuloModel ExModificaPenaCumulo(PenaCumuloModel aPenaCumulo) throws F3BException {
		Connection lConn = null;
		PenaCumuloDAO lPenDao = null;
		PenaCumuloModel lPenMod = new PenaCumuloModel(aPenaCumulo);

		try {
			lConn = getDBConnection();
			lPenDao = new PenaCumuloDAO(lConn);
			lPenDao.setDAOFromModelForUpdate(aPenaCumulo);
			lPenDao.update();
			lPenDao.update();
			commit(lConn);
		} catch (DAOException ex) {
			rollback(lConn);
			throw new F3BException("PenaCumuloController.ExModifica: Non posso inserire: " + ex);
		} finally {
			cleanup(lPenDao);
			cleanup(lConn);
		}

		return lPenMod;

	}

	/**
	 * Effettua la cancellazione di un record pena cumulo
	 * 
	 * @param aPenaCumulo
	 */
	public void ExCancellaPenaCumulo(PenaCumuloModel aPenaCumulo) throws F3BException {
		Connection lConn = null;
		PenaCumuloDAO lPenDao = null;

		try {
			lConn = getDBConnection();
			lPenDao = new PenaCumuloDAO(lConn);
			lPenDao.setCondizioneUpdate(aPenaCumulo.getIdPenaCumulo());
			lPenDao.delete();
			commit(lConn);
		} catch (DAOException daoEx) {
			throw new F3BException("PenaCumuloController.ExCancellaPenaCumulo: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lPenDao);
			cleanup(lConn);
		}
	}

	/**
	 * Ricerca l'ultima PENA_CUMULO inserita per il fascicolo e VALIDATA
	 * 
	 * @param aIdFascicolo
	 *            id del fascicolo
	 * @return ultima pena cumulo o null se non presente
	 * @throws F3BException
	 */
	public PenaCumuloModel ExRicercaUltimaPenaCumuloByIdFascicolo(BigDecimal aIdFascicolo)
			throws F3BException {
		Connection lConn = null;
		PenaCumuloSqlDAO lPenDao = null;
		PenaCumuloModel lPenMod;

		try {
			lConn = getDBConnection();
			lPenDao = new PenaCumuloSqlDAO(lConn);
			lPenDao.ricercaUltimaPenaCumuloByIdFascicolo(aIdFascicolo);
			lPenMod = (PenaCumuloModel) lPenDao.getModelByKey();
		} catch (DAOException daoEx) {
			throw new F3BException(
					"PenaCumuloController.ExRicercaUltimaPenaCumuloByIdFascicolo: Non posso leggere : "
							+ daoEx);
		} finally {
			cleanup(lPenDao);
			cleanup(lConn);
		}

		return lPenMod;
	}

	/**
	 * Effettua l'inserimento di un Elenco PENA_CUMULO <br>
	 *
	 * @param aPeneCumuli
	 *            - Elenco PeneCumuli
	 * @param lConn
	 *            - Connection parametro
	 * @return String - Rapporto Operazione
	 */
	public String ExInserisciPenaCumuloWithoutSequence(ArrayList aPeneCumuli, Connection lConn)
			throws F3BException {

		String lCodEsito = "00000";
		PenaCumuloDAO lPenaCumuloDao = null;
		PenaCumuloModel lPenaCumuloMod = null;
		try {

			lPenaCumuloDao = new PenaCumuloDAO(lConn);

			if (aPeneCumuli != null && aPeneCumuli.size() > 0) {
				for (int i = 0; i < aPeneCumuli.size(); i++) {
					lPenaCumuloMod = (PenaCumuloModel) aPeneCumuli.get(i);
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger.info("Cumulo da inserire = " + lPenaCumuloMod);
					if (lPenaCumuloMod != null && lPenaCumuloMod.getIdPenaCumulo() != null) {
						lPenaCumuloDao.setDAOFromModel(lPenaCumuloMod);
						lPenaCumuloDao.setWithoutSequence(true);
						lPenaCumuloDao.insert();
						lPenaCumuloDao.stop();
					}
				}
			}
		} catch (DAOException ex) {
			if (ex.UNIQUE_CONSTRAINT_VIOLATED) {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
				siesLogger.warn("PenaCumulo gia' presente...");
				lCodEsito = "00001";
			} else {
				lCodEsito = "01400";
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
				siesLogger.error(F3BException.USER_MESSAGE + " Impossibile inserire la PenaCumulo! ");
			}
		} finally {
			cleanup(lPenaCumuloDao);
		}
		return lCodEsito;
	}

}