package siap.sige.avvocato.controller;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.dao.DAOException;
import f3b.log.LogF3B;
import f3b.util.F3BException;
import siap.controller.SiapController;
import siap.sico.avvocato.dao.AvvocatoDAO;
import siap.sico.avvocato.model.AvvocatoModel;
import siap.siep.storicoavvocato.dao.StoricoAvvocatoDAO;
import siap.siep.storicoavvocato.model.StoricoAvvocatoModel;
import siap.sige.SIGEException;
import siap.sige.avvocato.dao.AvvocatoFascicoloSigeDAO;
import siap.sige.avvocato.dao.AvvocatoFascicoloSigeSqlDAO;
import siap.sige.avvocato.dao.AvvocatoSqlDAO;
import siap.sige.avvocato.dao.DifensoreSqlDao;
import siap.sige.avvocato.model.AvvocatoFascicoloSigeModel;
import siap.sige.avvocato.model.AvvocatoSigeModel;
import siap.sige.sentenza.model.SentenzaSigeModel;
import siap.sige.udienzaparti.dao.PartiUdienzaDifensoreDAO;
import siap.sige.udienzaparti.dao.PartiUdienzaDifensoreSqlDAO;
import siap.sige.udienzaparti.model.AvvocatoParteModel;
import siap.sige.udienzaparti.model.PartiUdienzaDifensoreModel;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class AvvocatoController extends SiapController implements IAvvocato {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	/**
	 * ExInserisciAvvocato - Metodo che permette l'inserimento di un avvovato nella tabella
	 * AVVOCATO_FASCICOLO_SIGE
	 */
	public AvvocatoSigeModel ExInserisciAvvocato(AvvocatoModel aAvvocato,
			AvvocatoFascicoloSigeModel aAvvFascMod) throws F3BException {

		Connection lConn = null;
		AvvocatoDAO lAvvDao = null;
		AvvocatoFascicoloSigeDAO lAvvFascDao = null;
		AvvocatoSigeModel aAvvocatoSige = new AvvocatoSigeModel();

		try {
			lConn = getDBTransaction();
			if (aAvvocato.getIdAvvocato().compareTo(new BigDecimal(0)) == 0) {
				lAvvDao = new AvvocatoDAO(lConn);
				lAvvDao.setDAOFromModel(aAvvocato);
				BigDecimal lSequence = lAvvDao.insert();
				aAvvocato.setIdAvvocato(lSequence);
				aAvvocatoSige.getAvvocato().setIdAvvocato(lSequence);
			}
			lAvvFascDao = new AvvocatoFascicoloSigeDAO(lConn);
			aAvvFascMod.setAvvIdAvvocato(aAvvocato.getIdAvvocato());
			lAvvFascDao.setDAOFromModel(aAvvFascMod);
			BigDecimal lSequenceFasc = lAvvFascDao.insert();
			aAvvFascMod.setIdAvvocatoFascicoloSige(lSequenceFasc);
			aAvvocatoSige.getAvvocatoFascicoloSigeModel().setIdAvvocatoFascicoloSige(lSequenceFasc);

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
		return aAvvocatoSige;
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
	 * ExRicercaAvvocato - Metodo che permette la ricerca di un avvovato
	 *
	 * @version 1.0
	 */
	public Vector ExRicercaAvvocato(AvvocatoModel aAvvocato, AvvocatoFascicoloSigeModel aAvvFascMod)
			throws F3BException {

		Connection lConn = null;
		AvvocatoSqlDAO lAvvDao = null;

		Vector lAvvocati = new Vector();

		try {
			lConn = getDBConnection();
			lAvvDao = new AvvocatoSqlDAO(lConn);

			lAvvDao.ricercaAvvocato(aAvvocato, aAvvFascMod);
			lAvvDao.start();

			while (lAvvDao.next()) {
				lAvvocati.add(lAvvDao.getModel());
			}

			lAvvDao.stop();

			if (lAvvocati.size() == 0)
				throw new SIGEException(SIGEException.USER_MESSAGE, "Nessun Elemento trovato");

		} catch (DAOException daoEx) {
			throw new F3BException("AvvocatoController.ExRicercaAvvocato: " + daoEx);
		} catch (SIGEException se) {
			throw se;
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
		AvvocatoSqlDAO lAvvDao = null;

		AvvocatoModel lAvvocato = new AvvocatoModel();

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
			throw new F3BException("AvvocatoController.ExRicercaAvvocatoByKey: " + daoEx);
		} catch (Exception ex) {
			throw new F3BException("AvvocatoController.ExRicercaAvvocatoByKey: " + ex);
		} finally {
			cleanup(lAvvDao);
			cleanup(lConn);
		}
		return lAvvocato;
	}

	/**
	 * ExRicercaAvvocato - Metodo che permette la ricerca di un avvovato tra quelli che già stanno seguendo un
	 * procedimento Sige
	 *
	 * @version 1.0
	 */
	public Vector ExRicercaAvvocatoSiep(AvvocatoModel aAvvocato, BigDecimal iKey) throws F3BException {

		Connection lConn = null;
		AvvocatoSqlDAO lAvvDao = null;

		Vector lAvvocati = new Vector();

		try {
			lConn = getDBConnection();
			lAvvDao = new AvvocatoSqlDAO(lConn);
			lAvvDao.ricercaAvvocatoSiep(aAvvocato, iKey);
			lAvvDao.start();

			while (lAvvDao.next())
				lAvvocati.add(lAvvDao.getModelSiep());

			lAvvDao.stop();

			if (lAvvocati.size() == 0)
				throw new SIGEException(SIGEException.USER_MESSAGE, "Nessun Elemento trovato");
		} catch (DAOException daoEx) {
			throw new F3BException("AvvocatoController.ExRicercaAvvocatoSiep: " + daoEx);
		} catch (SIGEException se) {
			throw se;
		} catch (Exception ex) {
			throw new F3BException("AvvocatoController.ExRicercaAvvocatoSiep: " + ex);
		} finally {
			cleanup(lAvvDao);
			cleanup(lConn);
		}

		return lAvvocati;
	}

	public Vector ExRicercaAvvocatiAttualiFascicolo(AvvocatoModel aAvvocato,
			AvvocatoFascicoloSigeModel aAvvFascMod) throws F3BException {

		Connection lConn = null;
		AvvocatoSqlDAO lAvvDao = null;

		Vector lAvvocati = new Vector();

		try {
			lConn = getDBConnection();
			lAvvDao = new AvvocatoSqlDAO(lConn);
			lAvvDao.ricercaAvvocatoAttualeFascicolo(aAvvocato, aAvvFascMod);
			lAvvDao.start();

			while (lAvvDao.next())
				lAvvocati.add(lAvvDao.getModel());

			lAvvDao.stop();
		} catch (DAOException daoEx) {
			throw new F3BException("AvvocatoController.ExRicercaAvvocato: Non posso leggere : " + daoEx);
		} catch (Exception e) {
			throw new F3BException("AvvocatoController.ExRicercaAvvocato: " + e);
		} finally {
			cleanup(lAvvDao);
			cleanup(lConn);
		}

		return lAvvocati;
	}

	// ** Per verificare se ci sono già due avvocati per lo stesso fascicolo
	public ArrayList ExRicercaAvvocatiByFascicoloNoError(BigDecimal aKey) throws F3BException {

		Connection lConn = null;
		AvvocatoFascicoloSigeSqlDAO lAvvDao = null;

		ArrayList lAvvocati = null;

		try {
			lConn = getDBConnection();
			lAvvDao = new AvvocatoFascicoloSigeSqlDAO(lConn);
			lAvvDao.ricercaAvvocatiByFascicolo(aKey);
			lAvvocati = new ArrayList(lAvvDao.getModels());
		} catch (DAOException daoEx) {
			throw new F3BException("AvvocatoController.ExRicercaAvvocato: Non posso leggere : " + daoEx);
		} catch (Exception e) {
			throw new F3BException("AvvocatoController.ExRicercaAvvocato: " + e);
		} finally {
			cleanup(lAvvDao);
			cleanup(lConn);
		}
		return lAvvocati;
	}

	public Vector ExRicercaAvvocatiByFascicolo(BigDecimal aKey) throws F3BException {

		Connection lConn = null;
		AvvocatoFascicoloSigeSqlDAO lAvvDao = null;

		Vector lAvvocati = null;

		try {
			lConn = getDBConnection();
			lAvvDao = new AvvocatoFascicoloSigeSqlDAO(lConn);
			lAvvDao.ricercaAvvocatiByFascicolo(aKey);
			lAvvocati = new Vector(lAvvDao.getModels());

			if (lAvvocati.size() == 0)
				throw new SIGEException(SIGEException.USER_MESSAGE,
						"Nessun avvocato associato al fascicolo.");
		} catch (DAOException daoEx) {
			throw new F3BException("AvvocatoController.ExRicercaAvvocatiByFascicolo: " + daoEx);
		} catch (SIGEException se) {
			throw se;
		} catch (Exception e) {
			throw new F3BException("AvvocatoController.ExRicercaAvvocatiByFascicolo: " + e);
		} finally {
			cleanup(lAvvDao);
			cleanup(lConn);
		}

		return lAvvocati;
	}

	/**
	 * ExRicercaAvvocato - Metodo che permette la ricerca di un avvovato
	 */
	public Vector ExRicercaAvvocato(AvvocatoModel aAvvocato) throws F3BException {

		Connection lConn = null;
		AvvocatoSqlDAO lAvvDao = null;

		Vector lAvvocati = new Vector();

		try {
			lConn = getDBConnection();
			lAvvDao = new AvvocatoSqlDAO(lConn);
			lAvvDao.ricercaAvvocato(aAvvocato);
			lAvvDao.start();

			while (lAvvDao.next())
				lAvvocati.add(lAvvDao.getModel());

			lAvvDao.stop();

			if (lAvvocati.size() == 0)
				throw new SIGEException(SIGEException.USER_MESSAGE, "Nessun Elemento trovato");
		} catch (DAOException daoEx) {
			throw new F3BException("AvvocatoController.ExRicercaAvvocato: " + daoEx);
		} catch (SIGEException se) {
			throw se;
		} catch (Exception e) {
			throw new F3BException("AvvocatoController.ExRicercaAvvocato: " + e);
		} finally {
			cleanup(lAvvDao);
			cleanup(lConn);
		}

		return lAvvocati;
	}

	/**
	 * ExRicercaAvvocatoProcedimento - Classe che permette la ricerca di un avvocato
	 *
	 * @version 1.0
	 */
	public Vector ExRicercaAvvocatoProvvedimento(AvvocatoFascicoloSigeModel aAvvocato) throws F3BException {

		Connection lConn = null;
		AvvocatoSqlDAO lAvvDao = null;

		Vector lAvvocati = new Vector();

		try {
			lConn = getDBConnection();
			lAvvDao = new AvvocatoSqlDAO(lConn);
			lAvvDao.ricercaAvvocatoSige(aAvvocato);
			lAvvDao.start();

			while (lAvvDao.next())
				lAvvocati.add(lAvvDao.getModel());

			lAvvDao.stop();

			if (lAvvocati.size() == 0)
				throw new SIGEException(SIGEException.USER_MESSAGE, "Nessun Elemento trovato");
		} catch (DAOException daoEx) {
			throw new F3BException("AvvocatoController.ExRicercaAvvocatoProcedimento: " + daoEx);
		} catch (SIGEException se) {
			throw se;
		} catch (Exception e) {
			throw new F3BException("AvvocatoController.ExRicercaAvvocatoProcedimento: " + e);
		} finally {
			cleanup(lAvvDao);
			cleanup(lConn);
		}

		return lAvvocati;
	}

	public AvvocatoSigeModel ExRicercaAvvocatoByKeyAvvocatoFasSige(BigDecimal aKey) throws F3BException {

		Connection lConn = null;
		AvvocatoFascicoloSigeSqlDAO lAvvDao = null;

		AvvocatoSigeModel lAvvFasModel = new AvvocatoSigeModel();

		try {
			lConn = getDBConnection();
			lAvvDao = new AvvocatoFascicoloSigeSqlDAO(lConn);
			lAvvDao.ricercaAvvocatoByKeyAvvocatoFasSige(aKey);

			lAvvFasModel = (AvvocatoSigeModel) lAvvDao.getModelByKey();
		} catch (DAOException daoEx) {
			throw new F3BException("AvvocatoController.ExRicercaAvvocatoByKeyAvvocatoFasSige: " + daoEx);
		} catch (Exception e) {
			throw new F3BException("AvvocatoController.ExRicercaAvvocatoByKeyAvvocatoFasSige: " + e);
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
			lConn = getDBTransaction();
			lAvvDao = new AvvocatoDAO(lConn);
			lAvvDao.setCondizioneUpdate(aAvvocato.getIdAvvocato());
			lAvvDao.delete();
			commit(lConn);
		} catch (DAOException daoEx) {
			if (daoEx.INTEGRITY_CONSTRAINT_VIOLATED)
				throw new SIGEException(SIGEException.USER_MESSAGE,
						" Impossibile effettuare la Cancellazione!");

			throw new F3BException("AvvocatoController.ExCancellaAvvocato: " + daoEx);
		} catch (SIGEException se) {
			throw se;
		} catch (Exception e) {
			throw new F3BException("AvvocatoController.ExCancellaAvvocato: " + e);
		} finally {
			cleanup(lAvvDao);
			cleanup(lConn);
		}
	}

	/**
	 * Effettua la concellazione di un avvocato FascicoloSige.
	 *
	 * @param aAvvocato
	 *            AvvocatoModel.
	 * @throws F3BException
	 *             propaga gli errorei di eccezione.
	 */
	public void ExCancellaAvvocatoFascicoloSige(AvvocatoFascicoloSigeModel aAvvocato) throws F3BException {

		Connection lConn = null;
		AvvocatoFascicoloSigeDAO lAvvDao = null;

		try {
			lConn = getDBConnection();
			lAvvDao = new AvvocatoFascicoloSigeDAO(lConn);
			lAvvDao.setCondizioneUpdateFascSige(aAvvocato.getIdAvvocatoFascicoloSige());

			lAvvDao.delete();
			commit(lConn);
		} catch (DAOException daoEx) {
			if (daoEx.INTEGRITY_CONSTRAINT_VIOLATED)
				throw new SIGEException(SIGEException.USER_MESSAGE,
						" Impossibile effettuare la Cancellazione!");
			throw new F3BException("AvvocatoController.ExCancellaAvvocatoFascicoloSige: : " + daoEx);
		} catch (SIGEException se) {
			throw se;
		} catch (Exception e) {
			throw new F3BException("AvvocatoController.ExCancellaAvvocatoFascicoloSige: " + e);
		} finally {
			cleanup(lAvvDao);
			cleanup(lConn);
		}
	}

	public Vector ExRicercaAvvocatoFascicoloSigeByKeyAvvocato(BigDecimal aKey) throws F3BException {

		Connection lConn = null;
		AvvocatoFascicoloSigeSqlDAO lAvvDao = null;

		Vector lAvvocati = new Vector();

		try {
			lConn = getDBConnection();
			lAvvDao = new AvvocatoFascicoloSigeSqlDAO(lConn);
			lAvvDao.ricercaAvvocatoFascicoloSigeByKeyAvvocato(aKey);

			lAvvDao.start();

			while (lAvvDao.next())
				lAvvocati.add(lAvvDao.getModel());

			lAvvDao.stop();

			if (lAvvocati.size() == 0)
				// MEV_21: modificato msg di risposta
				// throw throw new SIGEException(SIGEException.USER_MESSAGE,
				// "Nessun avvocato associato al fascicolo.");
				throw new SIGEException(SIGEException.USER_MESSAGE,
						"Avvocato con Foro inesistente, procedere con la Deassegnazione.");
		} catch (DAOException daoEx) {
			throw new F3BException(
					"AvvocatoController.ExRicercaAvvocatoFascicoloSigeByKeyAvvocato: " + daoEx);
		} catch (SIGEException se) {
			throw se;
		} catch (Exception e) {
			throw new F3BException("AvvocatoController.ExRicercaAvvocatoFascicoloSigeByKeyAvvocato: " + e);
		} finally {
			cleanup(lAvvDao);
			cleanup(lConn);
		}

		return lAvvocati;
	}

	public Vector ExRicercaDifensoreAttualiFascicolo(AvvocatoModel aAvvocato,
			AvvocatoFascicoloSigeModel aAvvFascMod) throws F3BException {

		Connection lConn = null;
		DifensoreSqlDao lDifDao = null;

		Vector lAvvocati = new Vector();

		try {
			lConn = getDBConnection();
			lDifDao = new DifensoreSqlDao(lConn);
			lDifDao.ricercaDifensoreAttualeFascicolo(aAvvocato, aAvvFascMod);
			lDifDao.start();

			while (lDifDao.next()) {
				AvvocatoSigeModel lAvvModel = lDifDao.getModelAvvSige();
				lAvvocati.add(lAvvModel);
			}

			lDifDao.stop();
		} catch (DAOException daoEx) {
			throw new F3BException(
					"AvvocatoController.ExRicercaDifensoreAttualiFascicolo: Non posso leggere : " + daoEx);
		} catch (Exception e) {
			throw new F3BException("AvvocatoController.ExDifensoreAttualiFascicolo: " + e);
		} finally {
			cleanup(lDifDao);
			cleanup(lConn);
		}

		return lAvvocati;
	}

	public AvvocatoFascicoloSigeModel ExDeassegnaAvvocato(AvvocatoFascicoloSigeModel aAvvocato)
			throws F3BException {

		Connection lConn = null;
		AvvocatoFascicoloSigeDAO lAvvFascDao = null;

		try {
			lConn = getDBTransaction();
			lAvvFascDao = new AvvocatoFascicoloSigeDAO(lConn);
			lAvvFascDao.setDAOFromModelForUpdateIdAvvocatoIdFascicolo(aAvvocato);
			lAvvFascDao.update();
			commit(lConn);
		} catch (DAOException ex) {
			rollback(lConn);
			throw new F3BException("AvvocatoController.ExDeassegnaAvvocato: " + ex);
		} catch (Exception e) {
			rollback(lConn);
			throw new F3BException("AvvocatoController.ExDeassegnaAvvocato: " + e);
		} finally {
			cleanup(lAvvFascDao);
			cleanup(lConn);
		}

		return aAvvocato;
	}

	public AvvocatoFascicoloSigeModel ExSostituzioneAvvocato(AvvocatoFascicoloSigeModel aAvvocatoUp,
			AvvocatoFascicoloSigeModel aAvvocatoIns) throws F3BException {

		Connection lConn = null;
		AvvocatoDAO lAvvDao = null;
		AvvocatoFascicoloSigeDAO lAvvFascDao = null;

		try {
			lConn = getDBTransaction();

			lAvvFascDao = new AvvocatoFascicoloSigeDAO(lConn);
			lAvvFascDao.setDAOFromModelForUpdateIdAvvocatoIdFascicolo(aAvvocatoUp);
			lAvvFascDao.update();
			lAvvFascDao.stop();

			lAvvFascDao.setDAOFromModel(aAvvocatoIns);
			BigDecimal lSequence = lAvvFascDao.insert();
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug(" CHIAVE AVVOCATO_FASCICOLO_SIGE INSERITO = " + lSequence);
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
		DifensoreSqlDao lAvvDao = null;

		Vector lAvvocati = new Vector();

		try {
			lConn = getDBConnection();
			lAvvDao = new DifensoreSqlDao(lConn);
			lAvvDao.ricercaDifensore(aAvvocato);
			lAvvDao.start();

			while (lAvvDao.next())
				lAvvocati.add(lAvvDao.getModel());
			lAvvDao.stop();

			if (lAvvocati.size() == 0)
				throw new SIGEException(SIGEException.USER_MESSAGE, "Nessun Elemento trovato");
		} catch (DAOException daoEx) {
			throw new F3BException("AvvocatoController.ExRicercaDifensore: " + daoEx);
		} catch (SIGEException se) {
			throw se;
		} catch (Exception e) {
			throw new F3BException("AvvocatoController.ExRicercaDifensore: " + e);
		} finally {
			cleanup(lAvvDao);
			cleanup(lConn);
		}

		return lAvvocati;
	}

	public Vector ricercaDifensoreDallaListaSiep(AvvocatoModel aAvvocato, SentenzaSigeModel sentenza)
			throws F3BException {

		Connection lConn = null;
		DifensoreSqlDao lAvvDao = null;

		Vector lAvvocati = new Vector();

		try {
			lConn = getDBConnection();
			lAvvDao = new DifensoreSqlDao(lConn);
			lAvvDao.ricercaDifensoreDallaListaSiep(aAvvocato, sentenza);
			lAvvDao.start();

			while (lAvvDao.next())
				lAvvocati.add(lAvvDao.getModel());
			lAvvDao.stop();

			if (lAvvocati.size() == 0)
				throw new SIGEException(SIGEException.USER_MESSAGE, "Nessun Elemento trovato");
		} catch (DAOException daoEx) {
			throw new F3BException("AvvocatoController.ExRicercaDifensore: " + daoEx);
		} catch (SIGEException se) {
			throw se;
		} catch (Exception e) {
			throw new F3BException("AvvocatoController.ExRicercaDifensore: " + e);
		} finally {
			cleanup(lAvvDao);
			cleanup(lConn);
		}

		return lAvvocati;
	}

	public Vector ExRicercaForo() throws F3BException {

		Connection lConn = null;
		AvvocatoSqlDAO lAvvDao = null;

		Vector lFori = new Vector();

		try {
			lConn = getDBConnection();
			lAvvDao = new AvvocatoSqlDAO(lConn);
			lAvvDao.ricercaForo();
			lAvvDao.start();

			while (lAvvDao.next())
				lFori.add(lAvvDao.getModelForo());

			lAvvDao.stop();

			if (lFori.size() == 0)
				throw new SIGEException(SIGEException.USER_MESSAGE,
						"Nessun Foro trovato, impossibile caricare gli avvocati");
		} catch (DAOException daoEx) {
			throw new F3BException("AvvocatoController.ExRicercaForo: " + daoEx);
		} catch (SIGEException se) {
			throw se;
		} catch (Exception e) {
			throw new F3BException("AvvocatoController.ExRicercaForo: " + e);
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

	public Vector ExRicercaAvvocatiByParteUdienza(BigDecimal aKey) throws F3BException {

		Connection lConn = null;
		PartiUdienzaDifensoreSqlDAO lParteUdienzaDao = null;

		Vector lAvvocati = null;

		try {
			lConn = getDBConnection();
			lParteUdienzaDao = new PartiUdienzaDifensoreSqlDAO(lConn);
			lParteUdienzaDao.ricercaDifensoreByIdSoggetto(aKey);
			lAvvocati = new Vector(lParteUdienzaDao.getModels());

			if (lAvvocati.size() == 0)
				throw new SIGEException(SIGEException.USER_MESSAGE,
						"Nessun avvocato associato alla Parte Udienza.");

		} catch (DAOException daoEx) {
			throw new F3BException("AvvocatoController.ExRicercaAvvocatiByParteUdienza: " + daoEx);
		} catch (SIGEException se) {
			throw se;
		} catch (Exception e) {
			throw new F3BException("AvvocatoController.ExRicercaAvvocatiByParteUdienza: " + e);
		} finally {
			cleanup(lParteUdienzaDao);
			cleanup(lConn);
		}

		return lAvvocati;
	}

	public AvvocatoParteModel ExInserisciAvvocato(AvvocatoModel aAvvocato,
			PartiUdienzaDifensoreModel aAvvParteMod) throws F3BException {

		Connection lConn = null;
		AvvocatoDAO lAvvDao = null;
		PartiUdienzaDifensoreDAO lParteUdienzaDao = null;

		AvvocatoParteModel aAvvocatoParte = new AvvocatoParteModel();

		try {
			lConn = getDBTransaction();

			if (aAvvocato.getIdAvvocato().compareTo(new BigDecimal(0)) == 0) {
				lAvvDao = new AvvocatoDAO(lConn);
				lAvvDao.setDAOFromModel(aAvvocato);
				BigDecimal lSequence = lAvvDao.insert();
				aAvvocato.setIdAvvocato(lSequence);
				aAvvocatoParte.getAvvocato().setIdAvvocato(lSequence);
			} else
				aAvvocatoParte.getAvvocato().setIdAvvocato(aAvvocato.getIdAvvocato());

			lParteUdienzaDao = new PartiUdienzaDifensoreDAO(lConn);
			aAvvParteMod.setAvvIdAvvocato(aAvvocato.getIdAvvocato());
			lParteUdienzaDao.setDAOFromModel(aAvvParteMod);
			BigDecimal lSequence = lParteUdienzaDao.insert();
			aAvvParteMod.setIdAvvocatoParteUdienza(lSequence);
			aAvvocatoParte.getAvvocatoParteUdienzaModel().setIdAvvocatoParteUdienza(lSequence);

			commit(lConn);
		} catch (DAOException ex) {
			rollback(lConn);
			throw new F3BException("AvvocatoController.ExInserisciAvvocato: " + ex);
		} catch (Exception e) {
			rollback(lConn);
			throw new F3BException("AvvocatoController.ExInserisciAvvocato: " + e);
		} finally {
			cleanup(lAvvDao);
			cleanup(lParteUdienzaDao);
			cleanup(lConn);
		}
		return aAvvocatoParte;
	}

	public Vector ExRicercaDifensoreAttualiParteUdienza(AvvocatoModel aAvvocato,
			PartiUdienzaDifensoreModel aPartiUdienzaDifMod) throws F3BException {

		Connection lConn = null;
		DifensoreSqlDao lDifDao = null;

		Vector lAvvocati = new Vector();

		try {
			lConn = getDBConnection();
			lDifDao = new DifensoreSqlDao(lConn);
			lDifDao.ricercaDifensoreAttualeParteUdienza(aAvvocato, aPartiUdienzaDifMod);
			lDifDao.start();

			while (lDifDao.next()) {
				AvvocatoParteModel lAvvModel = lDifDao.getModelAvvParteUdienza();
				lAvvocati.add(lAvvModel);
			}

			lDifDao.stop();
		} catch (DAOException daoEx) {
			throw new F3BException(
					"AvvocatoController.ExRicercaDifensoreAttualiParteUdienza: Non posso leggere : " + daoEx);
		} catch (Exception e) {
			throw new F3BException("AvvocatoController.ExRicercaDifensoreAttualiParteUdienza: " + e);
		} finally {
			cleanup(lDifDao);
			cleanup(lConn);
		}

		return lAvvocati;
	}

	public PartiUdienzaDifensoreModel ExDeassegnaDifensoreParteUdienza(PartiUdienzaDifensoreModel aPUDModel)
			throws F3BException {

		Connection lConn = null;
		PartiUdienzaDifensoreDAO lPUDDao = null;

		try {
			lConn = getDBTransaction();
			lPUDDao = new PartiUdienzaDifensoreDAO(lConn);
			lPUDDao.setDAOFromModelForUpdateIdAvvocatoIdParte(aPUDModel);
			lPUDDao.update();
			commit(lConn);
		} catch (DAOException ex) {
			rollback(lConn);
			throw new F3BException("AvvocatoController.ExDeassegnaDifensoreParteUdienza: " + ex);
		} catch (Exception e) {
			rollback(lConn);
			throw new F3BException("AvvocatoController.ExDeassegnaDifensoreParteUdienza: " + e);
		} finally {
			cleanup(lPUDDao);
			cleanup(lConn);
		}

		return aPUDModel;
	}

	public Vector ExRicercaDifensoreParteByKeyAvvocato(BigDecimal aKey) throws F3BException {

		Connection lConn = null;
		PartiUdienzaDifensoreSqlDAO lAvvDao = null;

		Vector lAvvocati = new Vector();

		try {
			lConn = getDBConnection();
			lAvvDao = new PartiUdienzaDifensoreSqlDAO(lConn);
			lAvvDao.ricercaDifensoreParteByKeyAvvocato(aKey);

			lAvvDao.start();

			while (lAvvDao.next())
				lAvvocati.add(lAvvDao.getModel());

			lAvvDao.stop();

			if (lAvvocati.size() == 0)
				throw new SIGEException(SIGEException.USER_MESSAGE,
						"Nessun avvocato associato alla Parte Udienza.");
		} catch (DAOException daoEx) {
			throw new F3BException("AvvocatoController.ExRicercaDifensoreParteByKeyAvvocato: " + daoEx);
		} catch (SIGEException se) {
			throw se;
		} catch (Exception e) {
			throw new F3BException("AvvocatoController.ExRicercaDifensoreParteByKeyAvvocato: " + e);
		} finally {
			cleanup(lAvvDao);
			cleanup(lConn);
		}

		return lAvvocati;
	}

	public PartiUdienzaDifensoreModel ExSostituzioneAvvocato(PartiUdienzaDifensoreModel aAvvocatoUp,
			PartiUdienzaDifensoreModel aAvvocatoIns) throws F3BException {

		Connection lConn = null;
		AvvocatoDAO lAvvDao = null;
		PartiUdienzaDifensoreDAO lAvvParteDao = null;

		try {
			lConn = getDBTransaction();

			lAvvParteDao = new PartiUdienzaDifensoreDAO(lConn);
			lAvvParteDao.setDAOFromModelForUpdateIdAvvocatoIdParte(aAvvocatoUp);
			lAvvParteDao.update();
			lAvvParteDao.stop();

			lAvvParteDao.setDAOFromModel(aAvvocatoIns);
			BigDecimal lSequence = lAvvParteDao.insert();
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug(" CHIAVE AVVOCATO_PARTE_UDIENZA INSERITO = " + lSequence);
			lAvvParteDao.stop();

			commit(lConn);
		} catch (DAOException ex) {
			rollback(lConn);
			throw new F3BException("AvvocatoController.ExSostituzioneAvvocato (Parte Udienza): " + ex);
		} catch (Exception e) {
			rollback(lConn);
			throw new F3BException("AvvocatoController.ExSostituzioneAvvocato (Parte Udienza): " + e);
		} finally {
			cleanup(lAvvDao);
			cleanup(lAvvParteDao);
			cleanup(lConn);
		}

		return aAvvocatoIns;
	}

	// MEV_21: aggiunto metodo di ricerca avvocato certificato reginde
	public AvvocatoModel ExRicercaAvvocatoCertRegInde(AvvocatoModel amParam) throws F3BException {

		Connection c = null;
		AvvocatoSqlDAO asdao = null;
		AvvocatoModel am = null;

		try {
			c = getDBConnection();
			asdao = new AvvocatoSqlDAO(c);
			asdao.ricercaAvvocatoCertRegInde(amParam);
			asdao.start();
			if (asdao.next()) {
				am = (AvvocatoModel) asdao.getModel();
			}
			asdao.stop();
		} catch (Exception ex) {
			siesLogger.error(getClass().getName() + ".ExRicercaAvvocatoCertReginde: " + ex);
			// throw new F3BException(getClass().getName() + ".ExRicercaAvvocatoCertReginde: " + ex);
		} finally {
			cleanup(asdao);
			cleanup(c);
		}
		// valore di ritorno
		return am;
	}

	// MEV_21: aggiunto metodo di aggiornamento avvocato da reginde
	public AvvocatoModel ExAggiornaAvvocatoDaReginde(AvvocatoModel am) throws F3BException {

		Connection c = null;
		AvvocatoDAO adao = null;

		try {
			c = getDBConnection();
			adao = new AvvocatoDAO(c);
			adao.setDAOFromModelForUpdate(am);
			adao.update();
			commit(c);
		} catch (DAOException ex) {
			rollback(c);
			throw new F3BException(
					this.getClass().getName() + ".ExAggiornaAvvocatoDaReginde - Non posso inserire: " + ex);
		} finally {
			cleanup(adao);
			cleanup(c);
		}

		return am;
	}

}