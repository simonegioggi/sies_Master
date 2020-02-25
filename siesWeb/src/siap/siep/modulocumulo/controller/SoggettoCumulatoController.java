package siap.siep.modulocumulo.controller;

import java.math.BigDecimal;
import java.sql.Connection;

import org.apache.log4j.Logger;

import siap.controller.SiapController;
import siap.sico.SICOException;
import siap.siep.modulocumulo.controller.ISoggettoCumulato;
import siap.siep.modulocumulo.dao.SoggettoCumulatoDAO;
import siap.siep.modulocumulo.dao.SoggettoCumulatoSqlDAO;
import siap.siep.modulocumulo.model.SoggettoCumulatoModel;
import f3b.dao.DAOException;
import f3b.log.LogF3B;
import f3b.util.F3BException;

/**
 * <p>
 * Title: SoggettoCumulatoController
 * </p>
 * <p>
 * Description: Classe Controller per il SoggettoCumulatoController
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
public class SoggettoCumulatoController extends SiapController implements ISoggettoCumulato {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	/*****************************************************************************
	 * Effettua l'inserimento di un SoggettoCumulatoController a partire dai dati contenuti nel Model
	 * 
	 * @param aSoggettoCumulatoModel
	 *            Model con i dati da inserire
	 * @return il model con i dati inseriti e l'aggiunta dell'id del record inserito
	 * @throws F3BException
	 ****************************************************************************/

	/**
	 * Inserimento di un SoggettoCumulato
	 * 
	 * @param aSoggetto
	 *            SoggettoCumulatoModel
	 * @return SoggettoCumulatoModel
	 * @throws F3BException
	 */
	public SoggettoCumulatoModel ExInserisciSoggetto_Cumulato(SoggettoCumulatoModel aSoggetto)
			throws F3BException {

		Connection lConn = null;
		SoggettoCumulatoDAO lSogDao = null;
		SoggettoCumulatoModel lSog = null;

		try {
			lConn = getDBConnection();
			lSogDao = new SoggettoCumulatoDAO(lConn);

			lSogDao.setDAOFromModel(aSoggetto);
			BigDecimal lSequence = lSogDao.insert();
			commit(lConn);

			lSog = new SoggettoCumulatoModel(aSoggetto);
			lSog.setMessage("Inserimento avvenuto correttamente!");

			lSog.setIdSoggettoCumulato(lSequence);

		} catch (Exception e) {
			siesLogger.error("Exception", e);
			rollback(lConn);
			throw new SICOException("SoggettoCumulatoController.ExInserisciSoggetto_Cumulo : " + e);
		} finally {
			cleanup(lSogDao);
			cleanup(lConn);
		}

		return lSog;

	}

	/**
	 * Ricerca di Soggetti (Cumulo)
	 * 
	 * @param aSoggetto
	 *            Soggetto Cumulato Model
	 * @return Vettore di Soggetti
	 * @throws F3BException
	 */
	public SoggettoCumulatoModel ExRicercaSoggettoCumulatoByKey(BigDecimal aKey) throws F3BException {
		Connection lConn = null;
		SoggettoCumulatoModel lSoggetto = null;
		SoggettoCumulatoSqlDAO lSogSqlDao = null;

		try {
			lConn = getDBConnection();
			lSogSqlDao = new SoggettoCumulatoSqlDAO(lConn);

			lSogSqlDao.ricercaSoggettoCumulatoByKey(aKey);

			lSoggetto = new SoggettoCumulatoModel();
			lSoggetto = (SoggettoCumulatoModel) lSogSqlDao.getModelByKey();

			if (lSoggetto == null)
				throw new SICOException(SICOException.USER_MESSAGE, "Nessun Elemento trovato");

		} catch (DAOException daoEx) {
			siesLogger.error("DAOException", daoEx);
			throw new SICOException(SICOException.USER_MESSAGE,
					"SoggettoCumulatoController.ExRicercaSoggettoCumulatoByKey : " + daoEx);
		} finally {
			cleanup(lSogSqlDao);
			cleanup(lConn);
		}

		return lSoggetto;
	}

	// ======================================

	/**
	 * Count dei Soggetti per Ricerca procedimento per soggetto
	 * 
	 * @param aSoggetto
	 * @param strCodUfficioUtenteConnesso
	 * @param strCodDistrettoUtenteConnesso
	 * @param strTipoRicerca
	 * @return
	 * @throws F3BException
	 */
	public BigDecimal ExGetCountSoggettiPerProcedimentiCumulo(SoggettoCumulatoModel aSoggetto,
			String strCodUfficioUtenteConnesso, String strCodDistrettoUtenteConnesso, String strTipoRicerca)
			throws F3BException {
		BigDecimal lCount = new BigDecimal(0);
		// Connection lConn = null;
		/*
		 * SoggettoFascicoloSqlDAO lSogSqlDao = null; try { lConn = getDBConnection(); lSogSqlDao = new
		 * SoggettoFascicoloSqlDAO(lConn); lSogSqlDao.getCountSoggettiPerProcedimenti(aSoggetto,
		 * strCodUfficioUtenteConnesso, strCodDistrettoUtenteConnesso, strTipoRicerca); lSogSqlDao.start();
		 * lSogSqlDao.next(); lCount = lSogSqlDao.getBigDecimal("HowManyRecords"); lSogSqlDao.stop(); } catch
		 * (DAOException daoEx) { // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger
		 * al posto di mLog siesLogger.info(daoEx.getLocalizedMessage()); throw new
		 * SICOException(SICOException.USER_MESSAGE,
		 * "SoggettoCumulatoController.ExGetCountSoggettiPerProcedimenti : " + daoEx); } catch (SQLException
		 * sqe) { // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		 * mLog siesLogger.info(sqe.getLocalizedMessage()); throw new
		 * SICOException(SICOException.USER_MESSAGE,
		 * "SoggettoCumulatoController.ExGetCountSoggettiPerProcedimenti : " + sqe); } finally {
		 * cleanup(lSogSqlDao); cleanup(lConn); }
		 */
		return lCount;
	}

	// ====================================

	/**
	 * Modifica Secca di un soggetto Cumulato
	 * 
	 * @param aSoggetto
	 *            SoggettoCumulatoModel
	 * @throws F3BException
	 */
	public SoggettoCumulatoModel ExModificaSoggettoCumulato(SoggettoCumulatoModel aSoggetto)
			throws F3BException {
		Connection conn = null;
		SoggettoCumulatoDAO lSogDao = null;
		SoggettoCumulatoModel lSog = null;

		try {
			conn = getDBConnection();

			lSogDao = new SoggettoCumulatoDAO(conn);

			lSogDao.setDAOFromModel(aSoggetto);
			lSogDao.selCondizioneUpdate(aSoggetto.getIdSoggettoCumulato());
			lSogDao.update();

			commit(conn);

			lSog = new SoggettoCumulatoModel();
			lSog.setMessage("Aggiornamento avvenuto correttamente!");
			lSog.setIdSoggettoCumulato(aSoggetto.getIdSoggettoCumulato());
		} catch (DAOException ex) {
			rollback(conn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.info("DAOException: " + ex);
			throw new SICOException("SoggettoCumulatoController.ExModificaSoggettoCumulato: " + ex);
		} finally {
			cleanup(lSogDao);
			cleanup(conn);
		}

		return lSog;
	}

	/**
	 * Cancellazione secca di un soggetto Cumulato
	 * 
	 * @param aSoggetto
	 *            SoggettoCumulatoModel
	 * @throws F3BException
	 */
	public void ExCancellaSoggettoCumulato(SoggettoCumulatoModel aSoggetto) throws F3BException {
		Connection conn = null;
		SoggettoCumulatoDAO lSogDao = null;

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
		siesLogger.info("SoggettoController.ExCancellaSoggetto: entrata nel metodo ");

		try {
			conn = getDBConnection();

			lSogDao = new SoggettoCumulatoDAO(conn);
			lSogDao.selCondizioneUpdate(aSoggetto.getIdSoggettoCumulato());
			lSogDao.delete();

			commit(conn);
		} catch (DAOException ex) {
			rollback(conn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.info("DAOException: " + ex);
			throw new SICOException("SoggettoCumulatoController.ExCancellaSoggettoCumulato: " + ex);
		} finally {
			cleanup(lSogDao);
			cleanup(conn);
		}
	}

	/**
	 * La Insert viene fatta in modalità 'NO SEQUENCE', senza utilizzare le sequnce. il valore della
	 * Primary_Key è già preimpostato; Questo metodo è usato nella funzione di presa in carico, per scaricare
	 * Tutti i dati del Fascicolo sulla nuova Base dati.
	 * 
	 * @param
	 * @param
	 * @return
	 * @since MEV 42 Cumulo step2
	 */
	public String ExInserisciSoggetto_CumulatoWithoutSequence(SoggettoCumulatoModel aSoggetto,
			Connection lConn) throws F3BException {
		String EsitodiRitorno = "00000";
		SoggettoCumulatoDAO lSogCumDao = null;

		try {
			lSogCumDao = new SoggettoCumulatoDAO(lConn);

			if (aSoggetto != null && aSoggetto.getIdSoggettoCumulato() != null) {
				lSogCumDao.setDAOFromModel(aSoggetto);
				lSogCumDao.setWithoutSequence(true);
				lSogCumDao.insert();
				lSogCumDao.stop();
			}

		} catch (DAOException daoEx) {
			if (daoEx.UNIQUE_CONSTRAINT_VIOLATED) {
				siesLogger.error("Soggetto Cumulato gia' presente...>" + aSoggetto.getIdSoggettoCumulato()
						+ "<");
				EsitodiRitorno = "00001";
			} else {
				EsitodiRitorno = "01400";
				throw new F3BException(F3BException.USER_MESSAGE, "Impossibile inserire Soggetto Cumulato! ");
			}
		} catch (Exception ex) {
			siesLogger.error("Exception: ", ex);
			throw new F3BException("SoggettoCumulatoController.ExInserisciSoggetto_CumulatoWithoutSequence: "
					+ ex);
		} finally {
			cleanup(lSogCumDao);
		}

		return EsitodiRitorno;

	} // Chiude ExInserisciSoggetto_CumulatoWithoutSequence

} // CHIUDE Controller()