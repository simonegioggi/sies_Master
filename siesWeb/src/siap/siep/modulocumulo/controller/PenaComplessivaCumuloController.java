package siap.siep.modulocumulo.controller;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.dao.DAOException;
import f3b.log.LogF3B;
import f3b.util.F3BException;
import siap.controller.SiapController;
import siap.siep.modulocumulo.dao.ContinuazioneCumuloDAO;
import siap.siep.modulocumulo.dao.ContinuazioneCumuloSqlDAO;
import siap.siep.modulocumulo.dao.PenaComplessivaCumuloDAO;
import siap.siep.modulocumulo.dao.PenaComplessivaCumuloSqlDAO;
import siap.siep.modulocumulo.dao.SanzioneSostitutivaCumuloDAO;
import siap.siep.modulocumulo.dao.SanzioneSostitutivaCumuloSqlDAO;
import siap.siep.modulocumulo.model.ContinuazioneCumuloModel;
import siap.siep.modulocumulo.model.DettaglioPenaComplessivaCumuloModel;
import siap.siep.modulocumulo.model.PenaComplessivaCumuloModel;
import siap.siep.modulocumulo.model.PenaComplessivaSanzioneSostitutivaCumuloModel;
import siap.siep.modulocumulo.model.SanzioneSostitutivaCumuloModel;

/**
 * Title: PenaComplessivaCumuloController Description: Classe Controller per PenaComplessivaCumulo in ambito
 * Cumulo (Pena_complessiva_Cumulo)
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class PenaComplessivaCumuloController extends SiapController implements IPenaComplessivaCumulo {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public PenaComplessivaCumuloModel ExInserisciPenaComplessivaCumulo(
			PenaComplessivaCumuloModel aPenaComplessiva) throws F3BException {

		Connection lConn = null;

		PenaComplessivaCumuloDAO lPenCumDao = null;
		PenaComplessivaCumuloModel lPenCumMod = null;

		try {
			lConn = getDBConnection();

			lPenCumMod = new PenaComplessivaCumuloModel(aPenaComplessiva);
			lPenCumDao = new PenaComplessivaCumuloDAO(lConn);
			lPenCumDao.setDAOFromModel(aPenaComplessiva);
			BigDecimal lKey = null;
			lKey = lPenCumDao.insert();

			commit(lConn);

			lPenCumMod.setIdPenaComplessivaCum(lKey);
		} catch (DAOException ex) {
			rollback(lConn);
			siesLogger.error("DAOException: ", ex);
			throw new F3BException(
					"PenaComplessivaCumuloController.ExInserisciPenaComplessivaCumulo: Non posso inserire: "
							+ ex);
		} finally {
			cleanup(lPenCumDao);
			cleanup(lConn);
		}

		return lPenCumMod;
	}

	/**
	 * Metodo ceh effettua l'inserimento contestuale della Pena Complessiva, Sanzione sostitutiva e Sentenze
	 * in continuazione per un titolo iscritto in cumulo.
	 *
	 * @param aPenaComplessivaCum
	 * @param aSanzioneSostitutivaCum
	 * @param aContinuazioniCum
	 *            - Lista delle sentenza in continuazione
	 */
	public PenaComplessivaCumuloModel ExInserisciPenaCompSanzioneSostContinuazioniCum(
			PenaComplessivaCumuloModel aPenaComplessivaCum,
			SanzioneSostitutivaCumuloModel aSanzioneSostitutivaCum, List aContinuazioniCum)
			throws F3BException {

		Connection lConn = null;

		PenaComplessivaCumuloDAO lPenDao = null;
		SanzioneSostitutivaCumuloDAO lSanzDao = null;
		ContinuazioneCumuloDAO lContDao = null;
		ContinuazioneCumuloSqlDAO lContSqlDao = null;

		try {
			lConn = getDBTransaction();

			// Inserimento Pena Complessiva
			lPenDao = new PenaComplessivaCumuloDAO(lConn);

			lPenDao.setDAOFromModel(aPenaComplessivaCum);
			BigDecimal lKeyPenaComplessiva = null;
			lKeyPenaComplessiva = lPenDao.insert();
			aPenaComplessivaCum.setIdPenaComplessivaCum(lKeyPenaComplessiva);

			// Inserimento Sanzione Sostitutiva
			if (aSanzioneSostitutivaCum != null) {
				lSanzDao = new SanzioneSostitutivaCumuloDAO(lConn);

				aSanzioneSostitutivaCum.setPcIdPenaComplessivaCum(lKeyPenaComplessiva);

				lSanzDao.setDAOFromModel(aSanzioneSostitutivaCum);
				BigDecimal lKeySanzioneSostitutiva = null;
				lKeySanzioneSostitutiva = lSanzDao.insert();
				aSanzioneSostitutivaCum.setIdSanzioneSostitutivaCum(lKeySanzioneSostitutiva);
			}

			// Inserimento Continuazioni
			lContDao = new ContinuazioneCumuloDAO(lConn);
			lContSqlDao = new ContinuazioneCumuloSqlDAO(lConn);

			Iterator lIter = aContinuazioniCum.iterator();
			while (lIter.hasNext()) {
				ContinuazioneCumuloModel lItem = (ContinuazioneCumuloModel) lIter.next();

				// Gestione Progressivo
				BigDecimal lProgr = lContSqlDao.getProgressivoContinuazione(lKeyPenaComplessiva);

				lItem.setProgrContinuazione(new BigDecimal(lProgr.intValue() + 1));
				lItem.setPcIdPenaComplessivaCum(lKeyPenaComplessiva);

				lContDao.setDAOFromModel(lItem);
				lContDao.insert();
				lContDao.stop();
			}

			commit(lConn);
		} catch (DAOException ex) {
			siesLogger.error("DAOException: ", ex);
			rollback(lConn);
			throw new F3BException(
					"PenaComplessivaCumuloController.ExInserisciPenaCompSanzioneSostContinuazioniCum: " + ex);
		} catch (Exception ex) {
			siesLogger.error("DAOException: ", ex);
			rollback(lConn);
			throw new F3BException(
					"PenaComplessivaCumuloController.ExInserisciPenaCompSanzioneSostContinuazioniCum: " + ex);
		} finally {
			cleanup(lPenDao);
			cleanup(lSanzDao);
			cleanup(lContDao);
			cleanup(lContSqlDao);

			cleanup(lConn);
		}

		return aPenaComplessivaCum;
	}

	public void ExInserisciUlterioriContinuazioniCumulo(List aContinuazioni) throws F3BException {

		Connection lConn = null;

		ContinuazioneCumuloDAO lContDao = null;
		ContinuazioneCumuloSqlDAO lContSqlDao = null;

		try {
			lConn = getDBTransaction();

			// Inserimento Continuazioni
			lContDao = new ContinuazioneCumuloDAO(lConn);
			lContSqlDao = new ContinuazioneCumuloSqlDAO(lConn);

			Iterator lIter = aContinuazioni.iterator();
			while (lIter.hasNext()) {
				ContinuazioneCumuloModel lItem = (ContinuazioneCumuloModel) lIter.next();

				siesLogger.debug("ContinuazioneCumuloModel = " + lItem);

				// Gestione Progressivo
				BigDecimal lProgr = lContSqlDao
						.getProgressivoContinuazione(lItem.getPcIdPenaComplessivaCum());
				lItem.setProgrContinuazione(new BigDecimal(lProgr.intValue() + 1));

				siesLogger.debug("ContinuazioneCumuloModel = " + lItem);

				lContDao.setDAOFromModel(lItem);
				lContDao.insert();
				lContDao.stop();
			}

			commit(lConn);
		} catch (DAOException daoEx) {
			rollback(lConn);
			siesLogger.error("DAOException", daoEx);
			throw new F3BException(
					"PenaComplessivaCumuloController.ExInserisciUlterioriContinuazioniCumulo: " + daoEx);
		} catch (Exception ex) {
			siesLogger.error("", ex);
			rollback(lConn);
			throw new F3BException(
					"PenaComplessivaCumuloController.ExInserisciUlterioriContinuazioniCumulo: " + ex);
		} finally {
			cleanup(lContDao);
			cleanup(lContSqlDao);

			cleanup(lConn);
		}
	}

	public DettaglioPenaComplessivaCumuloModel ExRicercaPenaCompSanzioneSostContinuazioniCumByIdTitolo(
			BigDecimal aIdTitolo) throws F3BException {

		Connection lConn = null;

		PenaComplessivaCumuloSqlDAO lPenSqlDao = null;
		SanzioneSostitutivaCumuloSqlDAO lSanSqlDao = null;
		ContinuazioneCumuloSqlDAO lContSqlDao = null;

		DettaglioPenaComplessivaCumuloModel lDettPenCompl = null;

		PenaComplessivaCumuloModel lPenMod = null;
		SanzioneSostitutivaCumuloModel lSanMod = null;
		List lListCircMod = null;

		try {
			lConn = getDBConnection();

			lPenSqlDao = new PenaComplessivaCumuloSqlDAO(lConn);
			lPenSqlDao.ricercaPenaComplessivaCumuloByIdTitolo(aIdTitolo);
			lPenMod = (PenaComplessivaCumuloModel) lPenSqlDao.getModelByKey();

			if (lPenMod != null) {
				lSanSqlDao = new SanzioneSostitutivaCumuloSqlDAO(lConn);
				lSanSqlDao
						.ricercaSanzioneSostitutivaByIdPenaComplessivaCum(lPenMod.getIdPenaComplessivaCum());

				lSanSqlDao.start();
				if (lSanSqlDao.next()) {
					lSanMod = (SanzioneSostitutivaCumuloModel) lSanSqlDao.getModel();
				}
				lSanSqlDao.stop();

				lContSqlDao = new ContinuazioneCumuloSqlDAO(lConn);
				lContSqlDao.ricercaContinuazioneByIdPenaComplessivaCum(lPenMod.getIdPenaComplessivaCum());
				lListCircMod = new ArrayList(lContSqlDao.getModels());

				PenaComplessivaSanzioneSostitutivaCumuloModel lMod = new PenaComplessivaSanzioneSostitutivaCumuloModel(
						lPenMod, lSanMod);
				lDettPenCompl = new DettaglioPenaComplessivaCumuloModel(lMod, lListCircMod);
			}
		} catch (DAOException daoEx) {
			siesLogger.error("", daoEx);
			throw new F3BException(
					"PenaComplessivaCumuloController.ExRicercaPenaCompSanzioneSostContinuazioniCumByIdTitolo: "
							+ daoEx);
		} catch (Exception ex) {
			siesLogger.error("", ex);
			throw new F3BException(
					"PenaComplessivaCumuloController.ExRicercaPenaCompSanzioneSostContinuazioniCumByIdTitolo: "
							+ ex);
		} finally {
			cleanup(lPenSqlDao);
			cleanup(lSanSqlDao);
			cleanup(lContSqlDao);

			cleanup(lConn);
		}

		return lDettPenCompl;
	}

	public PenaComplessivaCumuloModel ExRicercaPenaComplessivaCumByIdTitolo(BigDecimal aIdTitolo)
			throws F3BException {

		Connection lConn = null;

		PenaComplessivaCumuloSqlDAO lPenSqlDao = null;
		PenaComplessivaCumuloModel lPenMod;

		try {
			lConn = getDBConnection();

			lPenSqlDao = new PenaComplessivaCumuloSqlDAO(lConn);
			lPenSqlDao.ricercaPenaComplessivaCumuloByIdTitolo(aIdTitolo);
			lPenMod = (PenaComplessivaCumuloModel) lPenSqlDao.getModelByKey();
		} catch (DAOException daoEx) {
			siesLogger.error("", daoEx);
			throw new F3BException(
					"PenaComplessivaCumuloController.ExRicercaPenaComplessivaCumByIdTitolo: Non posso leggere : "
							+ daoEx);
		} catch (Exception ex) {
			siesLogger.error("", ex);
			throw new F3BException(
					"PenaComplessivaCumuloController.ExRicercaPenaComplessivaCumByIdTitolo: Non posso leggere  : "
							+ ex);
		} finally {
			cleanup(lPenSqlDao);

			cleanup(lConn);
		}
		return lPenMod;
	}

	public DettaglioPenaComplessivaCumuloModel ExRicercaPenaCompSanzioneSostContinuazioniCumByKey(
			BigDecimal aIdPenaComplessiva) throws F3BException {

		Connection lConn = null;

		PenaComplessivaCumuloSqlDAO lPenCumDao = null;
		SanzioneSostitutivaCumuloSqlDAO lSanCumDao = null;
		ContinuazioneCumuloSqlDAO lContDao = null;

		DettaglioPenaComplessivaCumuloModel lDettPenCompl = null;

		PenaComplessivaCumuloModel lPenMod = null;
		SanzioneSostitutivaCumuloModel lSanMod = null;
		List lListCircMod = null;

		try {
			lConn = getDBConnection();

			lPenCumDao = new PenaComplessivaCumuloSqlDAO(lConn);
			lPenCumDao.ricercaPenaComplessivaCumuloByKey(aIdPenaComplessiva);

			lPenCumDao.start();
			if (lPenCumDao.next()) {
				lPenMod = (PenaComplessivaCumuloModel) lPenCumDao.getModel();
			}
			lPenCumDao.stop();

			if (lPenMod != null) {
				lSanCumDao = new SanzioneSostitutivaCumuloSqlDAO(lConn);
				lSanCumDao
						.ricercaSanzioneSostitutivaByIdPenaComplessivaCum(lPenMod.getIdPenaComplessivaCum());

				lSanCumDao.start();
				if (lSanCumDao.next()) {
					lSanMod = (SanzioneSostitutivaCumuloModel) lSanCumDao.getModel();
				}
				lSanCumDao.stop();

				lContDao = new ContinuazioneCumuloSqlDAO(lConn);
				lContDao.ricercaContinuazioneByIdPenaComplessivaCum(aIdPenaComplessiva);
				lListCircMod = new ArrayList(lContDao.getModels());

				PenaComplessivaSanzioneSostitutivaCumuloModel lMod = new PenaComplessivaSanzioneSostitutivaCumuloModel(
						lPenMod, lSanMod);
				lDettPenCompl = new DettaglioPenaComplessivaCumuloModel(lMod, lListCircMod);
			}
		} catch (DAOException daoEx) {
			siesLogger.error("DAOException: ", daoEx);
			throw new F3BException(
					"PenaComplessivaCumuloController.ExRicercaPenaCompSanzioneSostContinuazioniCumuloByKey: "
							+ daoEx);
		} catch (Exception sqe) {
			siesLogger.error("Exception: ", sqe);
			throw new F3BException(
					"PenaComplessivaCumuloController.ExRicercaPenaCompSanzioneSostContinuazioniCumuloByKey: "
							+ sqe);
		} finally {
			cleanup(lPenCumDao);
			cleanup(lSanCumDao);
			cleanup(lContDao);

			cleanup(lConn);
		}

		return lDettPenCompl;
	}

	/**
	 * Effettua la ricerca della PENA_COMPLESSIVA_CUMULO, SANZIONE_SOST_CUM
	 *
	 * Non Recupera le sentenze in continuazione!!!!!!!!!!!!Perchè
	 *
	 * @param aKeyPenCompCum
	 * @return PenaComplessivaSanzioneSostitutivaCumuloModel - Model Aggregato
	 *
	 */
	public PenaComplessivaSanzioneSostitutivaCumuloModel ExRicercaPenaComplessivaSanzioneSostitutivaCumByKey(
			BigDecimal aIdPenaComplessivaCum) throws F3BException {

		Connection lConn = null;

		PenaComplessivaCumuloSqlDAO lPenDao = null;
		SanzioneSostitutivaCumuloSqlDAO lSanDao = null;

		PenaComplessivaSanzioneSostitutivaCumuloModel lPenSanMod = null;
		PenaComplessivaCumuloModel lPenMod = null;
		SanzioneSostitutivaCumuloModel lSanMod = null;

		try {
			lConn = getDBConnection();

			lPenDao = new PenaComplessivaCumuloSqlDAO(lConn);
			lPenDao.ricercaPenaComplessivaCumuloByKey(aIdPenaComplessivaCum);

			lPenMod = (PenaComplessivaCumuloModel) lPenDao.getModelByKey();

			if (lPenMod != null) {
				lSanDao = new SanzioneSostitutivaCumuloSqlDAO(lConn);
				lSanDao.ricercaSanzioneSostitutivaByIdPenaComplessivaCum(lPenMod.getIdPenaComplessivaCum());

				lSanDao.start();
				if (lSanDao.next()) {
					lSanMod = (SanzioneSostitutivaCumuloModel) lSanDao.getModel();
				}
				lSanDao.stop();

				lPenSanMod = new PenaComplessivaSanzioneSostitutivaCumuloModel(lPenMod, lSanMod);
			}
		} catch (DAOException daoEx) {
			siesLogger.error("", daoEx);
			throw new F3BException(
					"PenaComplessivaCumuloController.ExRicercaPenaComplessivaSanzioneSostitutivaCumByKey: "
							+ daoEx);
		} catch (Exception ex) {
			siesLogger.error("", ex);
			throw new F3BException(
					"PenaComplessivaCumuloController.ExRicercaPenaComplessivaSanzioneSostitutivaCumByKey: "
							+ ex);
		} finally {
			cleanup(lPenDao);
			cleanup(lSanDao);

			cleanup(lConn);
		}

		return lPenSanMod;
	}

	/**
	 * @deprecated da verificare. Mai referenziata. Autogenerated. La cancellazione della PC non può essere
	 *             effettuate 'secca' ni qanto referenziata da SANZIONE_SOST_CUM, CONTINUAZIONE_CUMULO
	 */
	public void ExCancellaPenaComplessivaCumulo(PenaComplessivaCumuloModel aPenaComplessiva)
			throws F3BException {

		Connection lConn = null;
		PenaComplessivaCumuloDAO lPenDao = null;
		try {
			lConn = getDBConnection();
			lPenDao = new PenaComplessivaCumuloDAO(lConn);
			lPenDao.setCondizioneUpdate(aPenaComplessiva.getIdPenaComplessivaCum());
			lPenDao.delete();

			commit(lConn);
		} catch (DAOException daoEx) {
			siesLogger.error("DAOException : ", daoEx);
			throw new F3BException(
					"PenaComplessivaCumuloController.ExCancellaPenaComplessivaCumulo: " + daoEx);
		} catch (Exception ex) {
			siesLogger.error("Exception : ", ex);
			throw new F3BException("PenaComplessivaCumuloController.ExCancellaPenaComplessivaCumulo: " + ex);
		} finally {
			cleanup(lPenDao);
			cleanup(lConn);
		}
	}

	public void ExCancellaPenaComplessivaSanzioneSostitutivaContinuazioniCum(
			PenaComplessivaCumuloModel aPenaComplessiva) throws F3BException {

		Connection lConn = null;

		PenaComplessivaCumuloDAO lPenDao = null;
		SanzioneSostitutivaCumuloDAO lSanDao = null;
		ContinuazioneCumuloDAO lConDao = null;

		try {
			lConn = getDBTransaction();

			// ** Cancella prima i record associati
			// ** e poi cancella Pena Complessiva
			BigDecimal lIdPenaComplessiva = aPenaComplessiva.getIdPenaComplessivaCum();

			// * Cancella la Sanzione Sostitutiva associata
			lSanDao = new SanzioneSostitutivaCumuloDAO(lConn);
			lSanDao.setCondizioneByIdPenaComplessivaCum(lIdPenaComplessiva);
			lSanDao.delete();

			// * Cancella le Continuazioni associate
			lConDao = new ContinuazioneCumuloDAO(lConn);
			lConDao.setCondizioneByIdPenaComplessiva(lIdPenaComplessiva);
			lConDao.delete();

			// * Cancella la Pena Complessiva
			lPenDao = new PenaComplessivaCumuloDAO(lConn);
			lPenDao.setCondizioneUpdate(lIdPenaComplessiva);
			lPenDao.delete();

			commit(lConn);
		} catch (DAOException daoEx) {
			siesLogger.error("DAOException: ", daoEx);
			rollback(lConn);
			throw new F3BException(
					"PenaComplessivaCumuloController.ExCancellaPenaComplessivaSanzioneSostitutivaContinuazioniCum-> "
							+ daoEx);
		} catch (Exception ex) {
			siesLogger.error("Exception: ", ex);
			rollback(lConn);
			throw new F3BException(
					"PenaComplessivaCumuloController.ExCancellaPenaComplessivaSanzioneSostitutivaContinuazioniCum-> "
							+ ex);
		} finally {
			cleanup(lPenDao);
			cleanup(lSanDao);
			cleanup(lConDao);

			cleanup(lConn);
		}
	}

	/**
	 * Effettua l'aggiornamento di PENA_COMPLESSIVA_CUMULO, e SANZIONE_SOST_CUM Viene aggiornata
	 * PENA_COMPLESSIVA_CUMULO. Viene aggiornata/inserita/cancellata SANZIONE_SOST_CUM
	 */
	public PenaComplessivaSanzioneSostitutivaCumuloModel ExModificaPenaComplessivaSanzioneSostitutivaCum(
			PenaComplessivaCumuloModel aPenaComplessiva, SanzioneSostitutivaCumuloModel aSanzioneSostitutiva,
			boolean aflagSanzioneSostitutiva) throws F3BException {

		Connection lConn = null;

		PenaComplessivaCumuloDAO lPenDao = null;
		SanzioneSostitutivaCumuloDAO lSanzDao = null;

		PenaComplessivaSanzioneSostitutivaCumuloModel aPenaComplessivaSanzioneSostitutiva = null;

		try {
			lConn = getDBTransaction();

			lPenDao = new PenaComplessivaCumuloDAO(lConn);

			// Modifica Pena Complessiva
			lPenDao = new PenaComplessivaCumuloDAO(lConn);
			lPenDao.setDAOFromModelForUpdate(aPenaComplessiva);
			lPenDao.update();

			// Controlla la presenza della Sanzione Sostitutiva
			lSanzDao = new SanzioneSostitutivaCumuloDAO(lConn);
			if (aSanzioneSostitutiva != null) {
				if (aflagSanzioneSostitutiva) // Ho dati da inserire/Modificare
				{
					if (aSanzioneSostitutiva.getIdSanzioneSostitutivaCum() == null) {
						// Se non presente la inserisce
						lSanzDao.setDAOFromModel(aSanzioneSostitutiva);
						BigDecimal lKeySanzioneSostitutiva = null;
						lKeySanzioneSostitutiva = lSanzDao.insert();
						aSanzioneSostitutiva.setIdSanzioneSostitutivaCum(lKeySanzioneSostitutiva);
					} else {
						// Altrimenti la modifica
						lSanzDao.setDAOFromModelForUpdate(aSanzioneSostitutiva);
						lSanzDao.update();
					}
				} else { // Se la SS esiste devo cancellarla
					if (aSanzioneSostitutiva.getIdSanzioneSostitutivaCum() != null) {
						lSanzDao.setCondizioneUpdate(aSanzioneSostitutiva.getIdSanzioneSostitutivaCum());
						lSanzDao.delete();
					}
				}
			}

			commit(lConn);

			aPenaComplessivaSanzioneSostitutiva = new PenaComplessivaSanzioneSostitutivaCumuloModel(
					aPenaComplessiva, aSanzioneSostitutiva);
		} catch (DAOException ex) {
			siesLogger.error("DAOException: ", ex);
			rollback(lConn);
			throw new F3BException(
					"PenaComplessivaCumuloController.ExModificaPenaComplessivaSanzioneSostitutivaCum: Non posso inserire: "
							+ ex);
		} catch (Exception ex) {
			siesLogger.error("Exception: ", ex);
			rollback(lConn);
			throw new F3BException(
					"PenaComplessivaCumuloController.ExModificaPenaComplessivaSanzioneSostitutivaCum: " + ex);
		} finally {
			cleanup(lPenDao);
			cleanup(lSanzDao);

			cleanup(lConn);
		}

		return aPenaComplessivaSanzioneSostitutiva;
	}

	/**
	 * Effettua la insert di PenaComplessiva, SanzioneSostitutiva e Continuazione La Insert viene fatta in
	 * modalità 'NO SEQUENCE', senza utilizzare le sequnce. il valore della Primary_Key è già preimpostato;
	 * metodi usati nella funzione di presa in carico, per scaricare Tutti i dati del Fascicolo sulla nuova
	 * Base dati.
	 *
	 * @param aPenaComplessiva
	 * @param lConn
	 * @return
	 * @since MEV 42 Cumulo Step2
	 */
	public String ExInserisciPenaComplessivaSanzioneSostContinuazioniCumuloWithoutSequence(
			PenaComplessivaCumuloModel aPenaComplessiva, Connection lConn) throws F3BException {

		String EsitodiRitorno = "00000";
		String QualeOggetto = "";
		BigDecimal QualeId = null;

		PenaComplessivaCumuloDAO lPenCumDao = null;
		SanzioneSostitutivaCumuloDAO lSanzCumDao = null;
		ContinuazioneCumuloDAO lContCumDao = null;

		SanzioneSostitutivaCumuloModel lSanzCumMod = null;
		ContinuazioneCumuloModel lContCumMod = null;

		try {
			lPenCumDao = new PenaComplessivaCumuloDAO(lConn);
			lSanzCumDao = new SanzioneSostitutivaCumuloDAO(lConn);
			lContCumDao = new ContinuazioneCumuloDAO(lConn);

			// Pena_Complessiva_Cumulo
			if (aPenaComplessiva != null && aPenaComplessiva.getIdPenaComplessivaCum() != null) {
				QualeOggetto = "Pena_Complessiva_Cumulo";
				QualeId = aPenaComplessiva.getIdPenaComplessivaCum();

				try {
					lPenCumDao.setDAOFromModel(aPenaComplessiva);
					lPenCumDao.setWithoutSequence(true);
					lPenCumDao.insert();
					lPenCumDao.stop();
				} catch (DAOException daoEx) {
					if (daoEx.UNIQUE_CONSTRAINT_VIOLATED) {
						siesLogger.error(QualeOggetto + " gia' presente in Archivio...>" + QualeId + "<");
						EsitodiRitorno = "00001";
					} else {
						throw daoEx;
					}
				}
			}

			// Sanzione_Sostitutiva:Cumulo
			if (aPenaComplessiva != null && aPenaComplessiva.getSanzioneSostitutivaCumulo() != null) {
				lSanzCumMod = aPenaComplessiva.getSanzioneSostitutivaCumulo();

				if (lSanzCumMod != null && lSanzCumMod.getIdSanzioneSostitutivaCum() != null) {
					QualeOggetto = "Sanzione_Sostitutiva_Cumulo";
					QualeId = lSanzCumMod.getIdSanzioneSostitutivaCum();

					try {
						lSanzCumDao.setDAOFromModel(lSanzCumMod);
						lSanzCumDao.setWithoutSequence(true);
						lSanzCumDao.insert();
						lSanzCumDao.stop();
					} catch (DAOException daoEx) {
						if (daoEx.UNIQUE_CONSTRAINT_VIOLATED) {
							siesLogger.error(QualeOggetto + " gia' presente in Archivio...>" + QualeId + "<");
							EsitodiRitorno = "00001";
						} else {
							throw daoEx;
						}
					}
				}
			}

			// Continuazioni_Cumulo
			if (aPenaComplessiva != null && aPenaComplessiva.getContinuazioniCumulo() != null) {
				Vector<ContinuazioneCumuloModel> lVecCont = (Vector<ContinuazioneCumuloModel>) aPenaComplessiva
						.getContinuazioniCumulo();

				if (lVecCont != null && lVecCont.size() > 0) {
					QualeOggetto = "Continuazioni_Cumulo";

					Iterator ItxC = lVecCont.iterator();
					while (ItxC.hasNext()) {
						lContCumMod = (ContinuazioneCumuloModel) ItxC.next();

						if (lContCumMod != null && lContCumMod.getIdContinuazioneCum() != null) {
							QualeId = lContCumMod.getIdContinuazioneCum();

							try {
								lContCumDao.setDAOFromModel(lContCumMod);
								lContCumDao.setWithoutSequence(true);
								lContCumDao.insert();
								lContCumDao.stop();
							} catch (DAOException daoEx) {
								if (daoEx.UNIQUE_CONSTRAINT_VIOLATED) {
									siesLogger.error(
											QualeOggetto + " gia' presente in Archivio...>" + QualeId + "<");
									EsitodiRitorno = "00001";
								} else {
									throw daoEx;
								}
							}
						}
					}
				}
			}
		} catch (DAOException daoEx) {
			EsitodiRitorno = "01400";
			throw new F3BException(F3BException.USER_MESSAGE, "Impossibile inserire " + QualeOggetto + "!");
		} catch (Exception ex) {
			siesLogger.error("Exception: " + ex);
			throw new F3BException(
					"PenaComplessivaCumuloController.ExInserisciPenaComplessivaSanzioneSostContinuazioniCumuloWithoutSequence: "
							+ ex);
		} finally {
			cleanup(lSanzCumDao);
			cleanup(lPenCumDao);
			cleanup(lContCumDao);
		}

		return EsitodiRitorno;
	} // Chiude ExInserisciPenaComplessivaSanzioneSostContinuazioniCumuloWithoutSequence()

} // CHIUDE PenaComplessivaCumuloController()