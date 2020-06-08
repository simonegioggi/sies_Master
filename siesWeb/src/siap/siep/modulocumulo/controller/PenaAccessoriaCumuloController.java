package siap.siep.modulocumulo.controller;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Iterator;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.dao.DAOException;
import f3b.log.LogF3B;
import f3b.util.F3BException;
import siap.controller.SiapController;
import siap.siep.modulocumulo.dao.PenaAccessoriaCumuloDAO;
import siap.siep.modulocumulo.dao.PenaAccessoriaCumuloSqlDAO;
import siap.siep.modulocumulo.dao.TitoloCumulatoSqlDAO;
import siap.siep.modulocumulo.model.PenaAccessoriaCumuloModel;
import siap.siep.modulocumulo.model.TitoloCumulatoModel;

/**
 * <p>
 * Title: PenaAccessoriaCumuloController
 * </p>
 * <p>
 * Description: Classe Controller per PenaAccessoriaCumulo
 * </p>
 *
 * @version 1.0
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class PenaAccessoriaCumuloController extends SiapController implements IPenaAccessoriaCumulo {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public PenaAccessoriaCumuloModel ExInserisciPenaAccessoriaCumulo(
			PenaAccessoriaCumuloModel aPenaAccessoria) throws F3BException {

		Connection lConn = null;
		PenaAccessoriaCumuloDAO lPenDao = null;

		try {
			lConn = getDBConnection();
			lPenDao = new PenaAccessoriaCumuloDAO(lConn);
			lPenDao.setDAOFromModel(aPenaAccessoria);
			aPenaAccessoria.setIdPenaAccessoriaCumulo(lPenDao.insert());

			commit(lConn);
			return aPenaAccessoria;
		} catch (DAOException ex) {
			rollback(lConn);
			siesLogger.error("DAOException: ", ex);
			throw new F3BException(
					"PenaAccessoriaCumuloController.ExInserisciPenaAccessoriaCumulo: Non posso inserire: "
							+ ex);
		} catch (Exception sqe) {
			rollback(lConn);
			siesLogger.error("Exception: " + sqe);
			throw new F3BException(
					"PenaAccessoriaCumuloController.ExInserisciPenaAccessoriaCumulo: Non posso inserire -> "
							+ sqe);
		} finally {
			cleanup(lPenDao);
			cleanup(lConn);
		}
	}

	/**
	 * Ricerca Tutte le PEne Accessorie relative ad un Tutolo Cumulato
	 *
	 * @param PenaAccessoriaCumuloModel
	 *            aPenaAccessoria
	 * @return
	 * @throws F3BException
	 */
	public Vector ExRicercaPenaAccessoriaCumulo(PenaAccessoriaCumuloModel aPenaAccessoria)
			throws F3BException {

		Connection lConn = null;
		Vector lPenaAccessorie = new Vector();
		PenaAccessoriaCumuloSqlDAO lPenSqlDao = null;

		try {
			lConn = getDBConnection();
			lPenSqlDao = new PenaAccessoriaCumuloSqlDAO(lConn);
			lPenSqlDao.ricercaPenaAccessoriaCumulo(aPenaAccessoria);
			lPenaAccessorie = new Vector(lPenSqlDao.getModels());
			if (lPenaAccessorie.size() == 0) {
				// throw new F3BException(F3BException.USER_MESSAGE, "Nessun Elemento trovato");
			}
		} catch (DAOException daoEx) {
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException(
					"PenaAccessoriaCumuloController.ExRicercaPenaAccessoriaCumulo: Non posso leggere : "
							+ daoEx);
		} finally {
			cleanup(lPenSqlDao);
			cleanup(lConn);
		}
		return lPenaAccessorie;
	}

	/**
	 * Ricerca Tutte le PEne Accessorie relative ad un Tutolo Cumulato con Flag_Stato Diverso da C
	 *
	 * @param PenaAccessoriaCumuloModel
	 *            aPenaAccessoria
	 * @return
	 * @throws F3BException
	 */
	public Vector ExRicercaPeneAccessorieCumulo_Valide(PenaAccessoriaCumuloModel aPenaAccessoria)
			throws F3BException {

		Connection lConn = null;
		Vector lPenaAccessorie = new Vector();
		PenaAccessoriaCumuloSqlDAO lPenSqlDao = null;

		try {
			lConn = getDBConnection();
			lPenSqlDao = new PenaAccessoriaCumuloSqlDAO(lConn);
			lPenSqlDao.ricercaPeneAccessorieCumulo_Valide(aPenaAccessoria);
			lPenaAccessorie = new Vector(lPenSqlDao.getModels());
			if (lPenaAccessorie.size() == 0) {
				// throw new F3BException(F3BException.USER_MESSAGE, "Nessun Elemento trovato");
			}
		} catch (DAOException daoEx) {
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException(
					"PenaAccessoriaCumuloController.ExRicercaPeneAccessorieCumulo_Valide: Non posso leggere : "
							+ daoEx);
		} finally {
			cleanup(lPenSqlDao);
			cleanup(lConn);
		}
		return lPenaAccessorie;
	}

	/**
	 * Ricerca la Pena Accessoria di Titolo Cumulato per la chiave
	 *
	 * @param PenaAccessoriaCumuloModel
	 *            aPenaAccessoria
	 * @return
	 * @throws F3BException
	 */
	public PenaAccessoriaCumuloModel ExRicercaPenaAccessoriaCumuloByKey(BigDecimal aIdPenaCum)
			throws F3BException {

		Connection lConn = null;
		PenaAccessoriaCumuloSqlDAO lPenDao = null;
		PenaAccessoriaCumuloModel lPenMod = null;

		try {
			lConn = getDBConnection();
			lPenDao = new PenaAccessoriaCumuloSqlDAO(lConn);
			lPenDao.ricercaPenaAccessoriaCumuloByKey(aIdPenaCum);
			lPenMod = (PenaAccessoriaCumuloModel) lPenDao.getModelByKey();
			lPenDao.stop();
			// Recupero Numero Eventi Correlati.
			// lPenDao = new PenaAccessoriaSqlDAO(lConn);
			// lPenMod.setNumeroEventiCorrelati(lPenDao.getNumeroEventiCorrelati(lPenMod.getIdPenaAccessoriaCumulo()));
		} catch (DAOException daoEx) {
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException(
					"PenaAccessoriaCumuloController.ExRicercaPenaAccessoriaCumulo: Non posso leggere : "
							+ daoEx);
		} finally {
			cleanup(lPenDao);
			cleanup(lConn);
		}
		return lPenMod;
	}

	/**
	 * MOdifica Pena Accessoria Titolo Cumulato
	 *
	 * @param PenaAccessoriaCumuloModel
	 *            aPenaAccessoria
	 * @return
	 * @throws F3BException
	 */
	public PenaAccessoriaCumuloModel ExModificaPenaAccessoriaCumulo(PenaAccessoriaCumuloModel aPenaAccessoria)
			throws F3BException {

		Connection lConn = null;
		PenaAccessoriaCumuloDAO lPenDao = null;

		try {
			lConn = getDBConnection();
			lPenDao = new PenaAccessoriaCumuloDAO(lConn);
			lPenDao.setDAOFromModelForUpdate(aPenaAccessoria);
			lPenDao.selCondizioneUpdate(aPenaAccessoria.getIdPenaAccessoriaCumulo());
			lPenDao.update();
			commit(lConn);
		} catch (DAOException ex) {
			rollback(lConn);
			siesLogger.error("DAOException: " + ex);
			throw new F3BException("PenaAccessoriaCumuloController.ExModificaPenaAccessoriaCumulo: " + ex);
		} finally {
			cleanup(lPenDao);
			cleanup(lConn);
		}
		return aPenaAccessoria;
	}

	/**
	 * Cancella Pena Accessoria di un Titolo Cumulato
	 *
	 * @param PenaAccessoriaCumuloModel
	 *            aPenaAccessoria
	 * @throws F3BException
	 */
	public void ExCancellaPenaAccessoriaCumulo(PenaAccessoriaCumuloModel aPenaAccessoria)
			throws F3BException {

		Connection lConn = null;
		PenaAccessoriaCumuloDAO lPenDao = null;

		try {
			lConn = getDBConnection();
			if (aPenaAccessoria.getFlagStato().equals("E") || aPenaAccessoria.getFlagStato().equals("M")) {
				// Effettuo la cancellazione logica del dato
				aPenaAccessoria.setFlagStato("C");

				lPenDao = new PenaAccessoriaCumuloDAO(lConn);
				lPenDao.setDAOFromModelForUpdate(aPenaAccessoria);
				lPenDao.selCondizioneUpdate(aPenaAccessoria.getIdPenaAccessoriaCumulo());
				lPenDao.update();
			} else {
				lPenDao = new PenaAccessoriaCumuloDAO(lConn);
				lPenDao.selCondizioneUpdate(aPenaAccessoria.getIdPenaAccessoriaCumulo());
				lPenDao.delete();
			}
			commit(lConn);
		} catch (DAOException daoEx) {
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException(
					"PenaAccessoriaCumuloController.ExCancellaPenaAccessoriaCumulo: Non posso leggere : "
							+ daoEx);
		} finally {
			cleanup(lPenDao);
			cleanup(lConn);
		}
	}

	/**
	 * Recupera tutte le PA iscritte sui Titoli iscritti nell'istruttoria Aggiunge ad ogni
	 * PenaAccessoriaCumuloModel anche i dati del titolo di riferimento
	 *
	 * @param aIdIstruttoria
	 * @param aFlagDatiFinali
	 *            = true se vanno recuperate le PA selezionate x Dati Finali
	 */
	public Vector<PenaAccessoriaCumuloModel> ExRicercaPenaAccessoriaCumuloByIdIstruttoria(
			BigDecimal aIdIstruttoria, boolean aFlagDatiFinali) throws F3BException {

		Connection lConn = null;

		Vector<PenaAccessoriaCumuloModel> lPeneAccessorieCumulo = new Vector<>();

		PenaAccessoriaCumuloSqlDAO lPenAccSqlDao = null;
		TitoloCumulatoSqlDAO lTitoloSqlDao = null;

		try {
			lConn = getDBConnection();

			lPenAccSqlDao = new PenaAccessoriaCumuloSqlDAO(lConn);

			lPenAccSqlDao.ricercaPenaAccessoriaCumuloByIdIstruttoria(aIdIstruttoria, aFlagDatiFinali);

			lPeneAccessorieCumulo = new Vector<PenaAccessoriaCumuloModel>(lPenAccSqlDao.getModels());

			Iterator<PenaAccessoriaCumuloModel> iter = lPeneAccessorieCumulo.iterator();

			// ==================================================
			// Per ogni Misura recupero i dati del Titolo
			// ==================================================
			lTitoloSqlDao = new TitoloCumulatoSqlDAO(lConn);
			while (iter.hasNext()) {
				PenaAccessoriaCumuloModel lPAModel = iter.next();

				lTitoloSqlDao.ricercaTitoloCumulatoByKey(lPAModel.getTitIdTitoloCumulato());

				TitoloCumulatoModel lTitoloModel = (TitoloCumulatoModel) lTitoloSqlDao.getModelByKey();

				lPAModel.setTitoloCumulato(lTitoloModel);
			}
		} catch (DAOException daoEx) {
			siesLogger.error("DAOException: ", daoEx);
			throw new F3BException(
					"PenaAccessoriaCumuloController.ExRicercaPenaAccessoriaCumuloByIdIstruttoria: Non posso leggere : "
							+ daoEx);
		} catch (Exception ex) {
			siesLogger.error("Exception: ", ex);
			throw new F3BException(
					"PenaAccessoriaCumuloController.ExRicercaPenaAccessoriaCumuloByIdIstruttoria: Non posso leggere : "
							+ ex);
		} finally {
			cleanup(lPenAccSqlDao);
			cleanup(lTitoloSqlDao);

			cleanup(lConn);
		}

		return lPeneAccessorieCumulo;
	}

	/**
	 * @param aListaPeneAccessorie
	 * @throws F3BException
	 */
	public void ExAggiornaPeneAccessorieDatiFinaliCumulo(
			Vector<PenaAccessoriaCumuloModel> aListaPeneAccessorie) throws F3BException {

		Connection lConn = null;

		PenaAccessoriaCumuloDAO lPenaAccDao = null;

		Iterator<PenaAccessoriaCumuloModel> iterMisure = aListaPeneAccessorie.iterator();

		try {
			lConn = getDBConnection();

			lPenaAccDao = new PenaAccessoriaCumuloDAO(lConn);

			while (iterMisure.hasNext()) {
				PenaAccessoriaCumuloModel lPenaAccModel = iterMisure.next();

				lPenaAccDao.setFlagDatiFinali(lPenaAccModel.getFlagDatiFinali());

				lPenaAccDao.selCondizioneUpdate(lPenaAccModel.getIdPenaAccessoriaCumulo());

				lPenaAccDao.update();
			}

			commit(lConn);
		} catch (DAOException daoEx) {
			rollback(lConn);
			siesLogger.error("DAOException: ", daoEx);
			throw new F3BException(
					"PenaAccessoriaCumuloController.ExAggiornaPeneAccessorieDatiFinaliCumulo: Non posso leggere : "
							+ daoEx);
		} catch (Exception ex) {
			rollback(lConn);
			siesLogger.error("Exception: ", ex);
			throw new F3BException(
					"PenaAccessoriaCumuloController.ExAggiornaPeneAccessorieDatiFinaliCumulo: Non posso leggere : "
							+ ex);
		} finally {
			cleanup(lPenaAccDao);
			cleanup(lConn);
		}

		return;
	}

	/**
	 * La Insert viene fatta in modalità 'NO SEQUENCE', senza utilizzare le sequnce. il valore della
	 * Primary_Key è già preimpostato; metodi usati nella funzione di presa in carico, per scaricare Tutti i
	 * dati del Fascicolo sulla nuova Base dati.
	 *
	 * @param
	 * @param
	 * @return
	 * @since MEV 42 Cumulo Step2
	 */
	public String ExInserisciPeneAccessorieCumuloWithoutSequence(
			Vector<PenaAccessoriaCumuloModel> aPeneAccessorie, Connection lConn) throws F3BException {

		String lCodEsito = "00000";
		PenaAccessoriaCumuloDAO lPenaCumDao = null;
		PenaAccessoriaCumuloModel lPenaCumMod = null;
		try {
			lPenaCumDao = new PenaAccessoriaCumuloDAO(lConn);

			if (aPeneAccessorie != null && aPeneAccessorie.size() > 0) {
				for (int i = 0; i < aPeneAccessorie.size(); i++) {
					lPenaCumMod = aPeneAccessorie.get(i);

					if (lPenaCumMod != null && lPenaCumMod.getIdPenaAccessoriaCumulo() != null) {
						try {
							lPenaCumDao.setDAOFromModel(lPenaCumMod);
							lPenaCumDao.setWithoutSequence(true);
							lPenaCumDao.insert();
							lPenaCumDao.stop();
						} catch (DAOException ex) {
							if (ex.UNIQUE_CONSTRAINT_VIOLATED) {
								siesLogger.error("Pena Accessoria Cumulo gia' presente..."
										+ lPenaCumMod.getIdPenaAccessoriaCumulo() + "<");
								lCodEsito = "00001";
							} else {
								throw ex;
							}
						}
					}
				}
			}
		} catch (DAOException ex) {
			lCodEsito = "01400";
			throw new F3BException(F3BException.USER_MESSAGE,
					"Impossibile inserire le Pene Accessorie Cumulo ! ");
		} finally {
			cleanup(lPenaCumDao);
		}

		return lCodEsito;
	} // Chiude ExInserisciPeneAccessorieCumuloWithoutSequence

	public Vector<PenaAccessoriaCumuloModel> ExRicercaPenaAccessoriaCumuloByIdTitoloCum(BigDecimal aIdTitolo)
			throws F3BException {

		Connection lConn = null;
		PenaAccessoriaCumuloSqlDAO lPenDao = null;
		Vector<PenaAccessoriaCumuloModel> VecPen = null;

		try {
			lConn = getDBConnection();
			lPenDao = new PenaAccessoriaCumuloSqlDAO(lConn);
			lPenDao.ricercaPenaAccessoriaCumuloByTitoloCum(aIdTitolo);
			VecPen = new Vector<PenaAccessoriaCumuloModel>(lPenDao.getModels());
			lPenDao.stop();
		} catch (DAOException daoEx) {
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException(
					"PenaAccessoriaCumuloController.ExRicercaPenaAccessoriaCumuloByIdTitoloCum: Non posso leggere : "
							+ daoEx);
		} finally {
			cleanup(lPenDao);
			cleanup(lConn);
		}
		return VecPen;
	} // Chiude ExRicercaPenaAccessoriaCumuloByIdTitoloCum

}