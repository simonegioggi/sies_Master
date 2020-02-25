package siap.sius.avvocato.controller;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Vector;

import org.apache.log4j.Logger;

import siap.controller.SiapController;
import siap.siep.storicoavvocato.dao.StoricoAvvocatoDAO;
import siap.siep.storicoavvocato.model.StoricoAvvocatoModel;
import siap.sius.SIUSException;
import siap.sius.avvocato.dao.AvvocatoDAO;
import siap.sius.avvocato.dao.AvvocatoFascicoloSiusDAO;
import siap.sius.avvocato.dao.AvvocatoFascicoloSiusSqlDAO;
import siap.sius.avvocato.dao.AvvocatoSqlDAO;
import siap.sius.avvocato.dao.DifensoreSqlDao;
import siap.sius.avvocato.model.AvvocatoFascicoloSiusModel;
import siap.sius.avvocato.model.AvvocatoModel;
import siap.sius.avvocato.model.AvvocatoSiusModel;
import f3b.dao.DAOException;
import f3b.log.LogF3B;
import f3b.util.F3BException;

/**
 * <p>
 * Title: AvvocatoController
 * </p>
 * <p>
 * Description: Classe Controller per Avvocato
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
public class AvvocatoController extends SiapController implements IAvvocato {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	/**
	 * <p>
	 * Title: ExInserisciAvvocato
	 * </p>
	 * <p>
	 * Description: Classe che permette l'inserimento di un avvovato nella tabella AVVOCATO_FASCICOLO_SIUS
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
	public AvvocatoSiusModel ExInserisciAvvocato(AvvocatoModel aAvvocato,
			AvvocatoFascicoloSiusModel aAvvFascMod) throws F3BException {
		Connection lConn = null;
		AvvocatoDAO lAvvDao = null;
		AvvocatoFascicoloSiusDAO lAvvFascDao = null;
		AvvocatoSiusModel aAvvocatoSius = new AvvocatoSiusModel();

		try {
			lConn = getDBTransaction();
			if (aAvvocato.getIdAvvocato().compareTo(new BigDecimal(0)) == 0) {
				lAvvDao = new AvvocatoDAO(lConn);
				lAvvDao.setDAOFromModel(aAvvocato);
				BigDecimal lSequence = lAvvDao.insert();
				aAvvocato.setIdAvvocato(lSequence);
				aAvvocatoSius.getAvvocato().setIdAvvocato(lSequence);
			}
			lAvvFascDao = new AvvocatoFascicoloSiusDAO(lConn);
			aAvvFascMod.setAvvIdAvvocato(aAvvocato.getIdAvvocato());
			lAvvFascDao.setDAOFromModel(aAvvFascMod);
			BigDecimal lSequenceFasc = lAvvFascDao.insert();
			aAvvFascMod.setIdAvvocatoFascicoloSius(lSequenceFasc);
			aAvvocatoSius.getAvvocatoFascicoloSiusModel().setIdAvvocatoFascicoloSius(lSequenceFasc);

			commit(lConn);
		} catch (DAOException ex) {
			rollback(lConn);
			throw new F3BException("AvvocatoController.ExInserisciAvvocato: " + ex);
		} catch (Exception e) {
			rollback(lConn);
			throw new F3BException("AvvocatoController.ExInserisciAvvocato: " + e);
		} finally {
			cleanup(lAvvDao);
			cleanup(lAvvFascDao);
			cleanup(lConn);
		}
		return aAvvocatoSius;
	}

	public AvvocatoModel ExInserisciAvvocato(AvvocatoModel aAvvocato) throws F3BException {
		Connection lConn = null;
		AvvocatoDAO lAvvDao = null;
		AvvocatoModel lAvv = null;

		try {
			lConn = getDBConnection();

			lAvvDao = new AvvocatoDAO(lConn);

			lAvvDao.setDAOFromModel(aAvvocato);
			BigDecimal lSequence = lAvvDao.insert();

			commit(lConn);
			lAvv = new AvvocatoModel(aAvvocato);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug(" CHIAVE SOGGETTO POSTO INSERIMENTO = " + lSequence);

			lAvv.setIdAvvocato(lSequence);
		} catch (DAOException ex) {
			rollback(lConn);
			throw new F3BException("AvvocatoController.ExInserisci: " + ex);
		} finally {
			cleanup(lAvvDao);
			cleanup(lConn);
		}

		return lAvv;
	}

	/**
	 * <p>
	 * Title: ExRicercaAvvocato
	 * </p>
	 * <p>
	 * Description: Classe che permette la ricerca di un avvovato
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
	public Vector ExRicercaAvvocato(AvvocatoModel aAvvocato, AvvocatoFascicoloSiusModel aAvvFascMod)
			throws F3BException {
		Connection lConn = null;
		Vector lAvvocati = new Vector();
		AvvocatoSqlDAO lAvvDao = null;

		try {
			lConn = getDBConnection();
			lAvvDao = new AvvocatoSqlDAO(lConn);

			lAvvDao.ricercaAvvocato(aAvvocato, aAvvFascMod);
			lAvvDao.start();

			while (lAvvDao.next()) {
				lAvvocati.add((AvvocatoModel) lAvvDao.getModel());
			}

			lAvvDao.stop();

			if (lAvvocati.size() == 0)
				throw new SIUSException(SIUSException.USER_MESSAGE, "Nessun Elemento trovato");

		} catch (DAOException daoEx) {
			throw new F3BException("AvvocatoController.ExRicercaAvvocato: Non posso leggere : " + daoEx);
		} catch (Exception ex) {
			throw new F3BException("AvvocatoController.ExRicercaAvvocato: " + ex);
		} finally {
			cleanup(lAvvDao);
			cleanup(lConn);
		}

		return lAvvocati;
	}

	public AvvocatoModel ExRicercaAvvocatoByKey(BigDecimal aIdAvvocato) throws F3BException {
		Connection lConn = null;
		AvvocatoModel lAvvocato = new AvvocatoModel();
		AvvocatoSqlDAO lAvvDao = null;

		try {
			lConn = getDBConnection();
			lAvvDao = new AvvocatoSqlDAO(lConn);
			lAvvDao.ricercaAvvocatobyKey(aIdAvvocato);
			lAvvDao.start();
			if (lAvvDao.next()) {
				lAvvocato = (AvvocatoModel) lAvvDao.getModelSiep();
			}

			lAvvDao.stop();

		} catch (DAOException daoEx) {
			throw new F3BException("AvvocatoController.ExRicercaAvvocato: Non posso leggere : " + daoEx);
		} catch (Exception ex) {
			throw new F3BException("AvvocatoController.ExRicercaAvvocato: Non posso leggere  : " + ex);
		} finally {
			cleanup(lAvvDao);
			cleanup(lConn);
		}
		return lAvvocato;
	}

	/***********************************/

	/**
	 * <p>
	 * Title: ExRicercaAvvocato
	 * </p>
	 * <p>
	 * Description: Classe che permette la ricerca di un avvovato tra quelli che già stanno seguendo un
	 * procedimento Sius
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

	public Vector ExRicercaAvvocatoSiep(AvvocatoModel aAvvocato, BigDecimal iKey) throws F3BException {
		Connection lConn = null;
		Vector lAvvocati = new Vector();
		AvvocatoSqlDAO lAvvDao = null;

		try {
			lConn = getDBConnection();
			lAvvDao = new AvvocatoSqlDAO(lConn);
			lAvvDao.ricercaAvvocatoSiep(aAvvocato, iKey);
			lAvvDao.start();

			while (lAvvDao.next()) {
				lAvvocati.add((AvvocatoModel) lAvvDao.getModelSiep());
			}

			lAvvDao.stop();

			if (lAvvocati.size() == 0)
				throw new SIUSException(SIUSException.USER_MESSAGE, "Nessun Elemento trovato");

		} catch (DAOException daoEx) {
			throw new F3BException("AvvocatoController.ExRicercaAvvocatoSiep: Non posso leggere : " + daoEx);
		}

		finally {
			cleanup(lAvvDao);
			cleanup(lConn);
		}

		return lAvvocati;
	}

	public Vector ExRicercaAvvocatiAttualiFascicolo(AvvocatoModel aAvvocato,
			AvvocatoFascicoloSiusModel aAvvFascMod) throws F3BException {
		Connection lConn = null;
		Vector lAvvocati = new Vector();
		AvvocatoSqlDAO lAvvDao = null;

		try {
			lConn = getDBConnection();
			lAvvDao = new AvvocatoSqlDAO(lConn);
			lAvvDao.ricercaAvvocatoAttualeFascicolo(aAvvocato, aAvvFascMod);
			lAvvDao.start();

			while (lAvvDao.next()) {
				lAvvocati.add((AvvocatoModel) lAvvDao.getModel());
			}

			lAvvDao.stop();
		} catch (DAOException daoEx) {
			throw new F3BException("AvvocatoController.ExRicercaAvvocato: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lAvvDao);
			cleanup(lConn);
		}

		return lAvvocati;
	}

	// ** Per verificare se ci sono già due avvocati per lo stesso fascicolo
	public Vector ExRicercaAvvocatiByFascicoloNoError(BigDecimal aKey) throws F3BException {
		Connection lConn = null;
		Vector lAvvocati = null;
		AvvocatoFascicoloSiusSqlDAO lAvvDao = null;

		try {
			lConn = getDBConnection();
			lAvvDao = new AvvocatoFascicoloSiusSqlDAO(lConn);
			lAvvDao.ricercaAvvocatiByFascicolo(aKey);
			lAvvocati = new Vector(lAvvDao.getModels());
		} catch (DAOException daoEx) {
			throw new F3BException("AvvocatoController.ExRicercaAvvocato: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lAvvDao);
			cleanup(lConn);
		}
		return lAvvocati;
	}

	// *********************
	public Vector ExRicercaAvvocatiByFascicolo(BigDecimal aKey) throws F3BException {
		Connection lConn = null;
		Vector lAvvocati = null;
		AvvocatoFascicoloSiusSqlDAO lAvvDao = null;

		try {
			lConn = getDBConnection();
			lAvvDao = new AvvocatoFascicoloSiusSqlDAO(lConn);
			lAvvDao.ricercaAvvocatiByFascicolo(aKey);
			lAvvocati = new Vector(lAvvDao.getModels());

			if (lAvvocati.size() == 0)
				throw new SIUSException(SIUSException.USER_MESSAGE, "Nessun avvocato associato al fascicolo.");

		} catch (DAOException daoEx) {
			throw new F3BException("AvvocatoController.ExRicercaAvvocato: " + daoEx);
		} finally {
			cleanup(lAvvDao);
			cleanup(lConn);
		}

		return lAvvocati;
	}

	/**
	 * <p>
	 * Title: ExRicercaAvvocato
	 * </p>
	 * <p>
	 * Description: Classe che permette la ricerca di un avvovato
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

	public Vector ExRicercaAvvocato(AvvocatoModel aAvvocato) throws F3BException {
		Connection lConn = null;
		Vector lAvvocati = new Vector();
		AvvocatoSqlDAO lAvvDao = null;

		try {
			lConn = getDBConnection();
			lAvvDao = new AvvocatoSqlDAO(lConn);
			lAvvDao.ricercaAvvocato(aAvvocato);
			lAvvDao.start();

			while (lAvvDao.next()) {
				lAvvocati.add((AvvocatoModel) lAvvDao.getModel());
			}

			lAvvDao.stop();

			if (lAvvocati.size() == 0)
				throw new SIUSException(SIUSException.USER_MESSAGE, "Nessun Elemento trovato");

		} catch (DAOException daoEx) {
			throw new F3BException("AvvocatoController.ExRicercaAvvocato: " + daoEx);
		} finally {
			cleanup(lAvvDao);
			cleanup(lConn);
		}

		return lAvvocati;
	}

	/**
	 * <p>
	 * Title: ExRicercaAvvocatoProcedimento
	 * </p>
	 * <p>
	 * Description: Classe che permette la ricerca di un avvocato
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

	public Vector ExRicercaAvvocatoProvvedimento(AvvocatoFascicoloSiusModel aAvvocato) throws F3BException {
		Connection lConn = null;
		Vector lAvvocati = new Vector();
		AvvocatoSqlDAO lAvvDao = null;

		try {
			lConn = getDBConnection();
			lAvvDao = new AvvocatoSqlDAO(lConn);
			lAvvDao.ricercaAvvocatoSius(aAvvocato);
			lAvvDao.start();

			while (lAvvDao.next()) {
				lAvvocati.add((AvvocatoModel) lAvvDao.getModel());
			}

			lAvvDao.stop();

			if (lAvvocati.size() == 0)
				throw new SIUSException(SIUSException.USER_MESSAGE, "Nessun Elemento trovato");

		} catch (DAOException daoEx) {
			throw new F3BException("AvvocatoController.ExRicercaAvvocatoProcedimento: " + daoEx);
		} finally {
			cleanup(lAvvDao);
			cleanup(lConn);
		}

		return lAvvocati;
	}

	public AvvocatoSiusModel ExRicercaAvvocatoByKeyAvvocatoFasSius(BigDecimal aKey) throws F3BException {
		Connection lConn = null;
		AvvocatoSiusModel lAvvFasModel = new AvvocatoSiusModel();
		AvvocatoFascicoloSiusSqlDAO lAvvDao = null;

		try {
			lConn = getDBConnection();
			lAvvDao = new AvvocatoFascicoloSiusSqlDAO(lConn);
			lAvvDao.ricercaAvvocatoByKeyAvvocatoFasSius(aKey);

			lAvvFasModel = (AvvocatoSiusModel) lAvvDao.getModelByKey();

		} catch (DAOException daoEx) {
			throw new F3BException("AvvocatoController.ExRicercaAvvocato: " + daoEx);
		} finally {
			cleanup(lAvvDao);
			cleanup(lConn);
		}

		return lAvvFasModel;
	}

	public AvvocatoModel ExModificaAvvocato(AvvocatoModel aAvvocato) throws F3BException {
		Connection lConn = null;
		AvvocatoDAO lAvvDao = null;

		try {
			lConn = getDBConnection();
			lAvvDao = new AvvocatoDAO(lConn);
			lAvvDao.setDAOFromModel(aAvvocato);
			lAvvDao.update();
			commit(lConn);
		} catch (DAOException ex) {
			rollback(lConn);
			throw new F3BException("AvvocatoController.ExModifica: " + ex);
		} finally {
			cleanup(lAvvDao);
			cleanup(lConn);
		}

		return aAvvocato;
	}

	/**
	 * Effettua la concellazione di un avvocato.
	 * <p>
	 * 
	 * @param aAvvocato
	 *            AvvocatoModel.
	 * @throws F3BException
	 *             propaga gli errorei di eccezione.
	 */
	public void ExCancellaAvvocato(AvvocatoModel aAvvocato) throws F3BException {
		Connection lConn = null;
		AvvocatoDAO lAvvDao = null;

		try {
			lConn = getDBConnection();
			lAvvDao = new AvvocatoDAO(lConn);
			lAvvDao.setCondizioneUpdate(aAvvocato.getIdAvvocato());

			lAvvDao.delete();
			commit(lConn);
		} catch (DAOException daoEx) {
			if (daoEx.INTEGRITY_CONSTRAINT_VIOLATED)
				throw new SIUSException(SIUSException.USER_MESSAGE,
						" Impossibile effettuare la Cancellazione!");

			throw new F3BException("AvvocatoController.ExCancellaAvvocato: " + daoEx);
		} finally {
			cleanup(lAvvDao);
			cleanup(lConn);
		}
	}

	/**
	 * Effettua la concellazione di un avvocato FascicoloSius.
	 * <p>
	 * 
	 * @param aAvvocato
	 *            AvvocatoModel.
	 * @throws F3BException
	 *             propaga gli errorei di eccezione.
	 */
	public void ExCancellaAvvocatoFascicoloSius(AvvocatoFascicoloSiusModel aAvvocato) throws F3BException {
		Connection lConn = null;
		AvvocatoFascicoloSiusDAO lAvvDao = null;

		try {
			lConn = getDBConnection();
			lAvvDao = new AvvocatoFascicoloSiusDAO(lConn);
			lAvvDao.setCondizioneUpdateFascSius(aAvvocato.getIdAvvocatoFascicoloSius());

			lAvvDao.delete();
			commit(lConn);
		} catch (DAOException daoEx) {
			if (daoEx.INTEGRITY_CONSTRAINT_VIOLATED)
				throw new SIUSException(SIUSException.USER_MESSAGE,
						" Impossibile effettuare la Cancellazione!");

			throw new F3BException("AvvocatoController.ExCancellaAvvocatoFascicoloSius: : " + daoEx);
		} finally {
			cleanup(lAvvDao);
			cleanup(lConn);
		}
	}

	public Vector ExRicercaAvvocatoFascicoloSiusByKeyAvvocato(BigDecimal aKey) throws F3BException {
		Connection lConn = null;
//		AvvocatoSiusModel lAvvFasModel = new AvvocatoSiusModel();
		AvvocatoFascicoloSiusSqlDAO lAvvDao = null;
		Vector lAvvocati = new Vector();

		try {
			lConn = getDBConnection();
			lAvvDao = new AvvocatoFascicoloSiusSqlDAO(lConn);
			lAvvDao.ricercaAvvocatoFascicoloSiusByKeyAvvocato(aKey);

			lAvvDao.start();

			while (lAvvDao.next()) {
				lAvvocati.add((AvvocatoSiusModel) lAvvDao.getModel());
			}

			lAvvDao.stop();

			if (lAvvocati.size() == 0)
				throw new SIUSException(SIUSException.USER_MESSAGE, "Nessun avvocato associato al fascicolo.");
		} catch (DAOException daoEx) {
			throw new F3BException("AvvocatoController.ExRicercaAvvocatoFascicoloSiusByKeyAvvocato: " + daoEx);
		} finally {
			cleanup(lAvvDao);
			cleanup(lConn);
		}

		return lAvvocati;
	}

	public Vector ExRicercaDifensoreAttualiFascicolo(AvvocatoModel aAvvocato,
			AvvocatoFascicoloSiusModel aAvvFascMod) throws F3BException {
		Connection lConn = null;
		Vector lAvvocati = new Vector();
		DifensoreSqlDao lDifDao = null;

		try {
			lConn = getDBConnection();
			lDifDao = new DifensoreSqlDao(lConn);
			lDifDao.ricercaDifensoreAttualeFascicolo(aAvvocato, aAvvFascMod);
			lDifDao.start();

			while (lDifDao.next()) {
				lAvvocati.add((AvvocatoSiusModel) lDifDao.getModel());
			}

			lDifDao.stop();
		} catch (DAOException daoEx) {
			throw new F3BException(
					"AvvocatoController.ExRicercaDifensoreAttualiFascicolo: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lDifDao);
			cleanup(lConn);
		}

		return lAvvocati;
	}

	public AvvocatoFascicoloSiusModel ExDeassegnaAvvocato(AvvocatoFascicoloSiusModel aAvvocato)
			throws F3BException {
		Connection lConn = null;
		AvvocatoFascicoloSiusDAO lAvvFascDao = null;

		try {
			lConn = getDBTransaction();
			lAvvFascDao = new AvvocatoFascicoloSiusDAO(lConn);
			lAvvFascDao.setDAOFromModelForUpdateIdAvvocatoIdFascicolo(aAvvocato);
			lAvvFascDao.update();
			commit(lConn);
		} catch (DAOException ex) {
			rollback(lConn);
			throw new F3BException("AvvocatoController.ExDeassegnaAvvocato: " + ex);
		} finally {
			cleanup(lAvvFascDao);
			cleanup(lConn);
		}

		return aAvvocato;
	}

	public AvvocatoFascicoloSiusModel ExSostituzioneAvvocato(AvvocatoFascicoloSiusModel aAvvocatoUp,
			AvvocatoFascicoloSiusModel aAvvocatoIns) throws F3BException {
		Connection lConn = null;
		AvvocatoDAO lAvvDao = null;
		AvvocatoFascicoloSiusDAO lAvvFascDao = null;

		try {
			lConn = getDBTransaction();

			lAvvFascDao = new AvvocatoFascicoloSiusDAO(lConn);
			lAvvFascDao.setDAOFromModelForUpdateIdAvvocatoIdFascicolo(aAvvocatoUp);
			lAvvFascDao.update();
			lAvvFascDao.stop();

			lAvvFascDao.setDAOFromModel(aAvvocatoIns);
			/*BigDecimal lSequence = */lAvvFascDao.insert();
			lAvvFascDao.stop();

			commit(lConn);
		} catch (DAOException ex) {
			rollback(lConn);
			throw new F3BException("AvvocatoController.ExSostituzioneAvvocato: " + ex);
		} catch (Exception e) {
			rollback(lConn);
			throw new F3BException("AvvocatoController.ExSostituzioneAvvocato: " + e);
		} finally {
			cleanup(lAvvDao);
			cleanup(lAvvFascDao);

			cleanup(lConn);
		}

		return aAvvocatoIns;
	}

	public Vector ExRicercaDifensore(AvvocatoModel aAvvocato) throws F3BException {
		Connection lConn = null;
		Vector lAvvocati = new Vector();
		DifensoreSqlDao lAvvDao = null;

		try {
			lConn = getDBConnection();
			lAvvDao = new DifensoreSqlDao(lConn);
			lAvvDao.ricercaDifensore(aAvvocato);
			lAvvDao.start();

			while (lAvvDao.next()) {
				lAvvocati.add((AvvocatoSiusModel) lAvvDao.getModel());
			}

			lAvvDao.stop();

			if (lAvvocati.size() == 0)
				throw new SIUSException(SIUSException.USER_MESSAGE, "Nessun Elemento trovato");

		} catch (DAOException daoEx) {
			throw new F3BException("AvvocatoController.ExRicercaDifensore: " + daoEx);
		} finally {
			cleanup(lAvvDao);
			cleanup(lConn);
		}

		return lAvvocati;
	}

	public Vector ExRicercaForo() throws F3BException {
		Connection lConn = null;
		Vector lFori = new Vector();
		AvvocatoSqlDAO lAvvDao = null;

		try {
			lConn = getDBConnection();
			lAvvDao = new AvvocatoSqlDAO(lConn);
			lAvvDao.ricercaForo();
			lAvvDao.start();

			while (lAvvDao.next()) {
				lFori.add((AvvocatoModel) lAvvDao.getModelForo());
			}

			lAvvDao.stop();

			if (lFori.size() == 0)
				throw new SIUSException(SIUSException.USER_MESSAGE,
						"Nessun Foro trovato, impossibile caricare gli avvocati");

		} catch (DAOException daoEx) {
			throw new F3BException("AvvocatoController.ExRicercaForo: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lAvvDao);
			cleanup(lConn);
		}

		return lFori;
	}

	public AvvocatoModel ExModificaStoricizzaAvvocato(AvvocatoModel aAvvocato, StoricoAvvocatoModel aStorico)
			throws F3BException {
		Connection lConn = null;
		AvvocatoDAO lAvvDao = null;
		StoricoAvvocatoDAO lStoricoAvvDAO = null;

		try {
			lConn = getDBTransaction();

			lAvvDao = new AvvocatoDAO(lConn);

			lAvvDao.setDAOFromModelForUpdate(aAvvocato);
			lAvvDao.update();

			// lStoricoModel = new StoricoAvvocatoModel();
			lStoricoAvvDAO = new StoricoAvvocatoDAO(lConn);

			lStoricoAvvDAO.setDAOFromModel(aStorico);
			BigDecimal lSequence = lStoricoAvvDAO.insert();
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug(" CHIAVE  INSERIMENTO = " + lSequence);

			commit(lConn);
		} catch (DAOException ex) {
			rollback(lConn);
			throw new F3BException("AvvocatoController.ExModifica: Non posso inserire: " + ex);
		} catch (Exception ex) {
			rollback(lConn);
			throw new F3BException("AvvocatoController.ExModifica: " + ex);
		} finally {
			cleanup(lAvvDao);
			cleanup(lStoricoAvvDAO);

			cleanup(lConn);
		}

		return aAvvocato;
	}

}