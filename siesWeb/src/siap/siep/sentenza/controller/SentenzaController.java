package siap.siep.sentenza.controller;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Iterator;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.dao.DAOException;
import f3b.log.LogF3B;
import f3b.util.F3BException;
import siap.controller.SiapController;
import siap.sico.soggetto.model.SoggettoModel;
import siap.siep.SIEPException;
import siap.siep.fascicolo.dao.FascicoloSiepDAO;
import siap.siep.fascicolo.dao.FascicoloSiepSoggettoSqlDAO;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.sentenza.dao.SentenzaDAO;
import siap.siep.sentenza.dao.SentenzaFascicoloSqlDAO;
import siap.siep.sentenza.dao.SentenzaSoggettoSqlDAO;
import siap.siep.sentenza.dao.SentenzaSqlDAO;
import siap.siep.sentenza.model.SentenzaFascicoliModel;
import siap.siep.sentenza.model.SentenzaModel;
import siap.sige.sentenza.dao.FasSigeSentenzaDAO;
import siap.sige.sentenza.model.SentenzaSigeModel;
import siap.sige.tenore.dao.TenoreSentenzaReatoDAO;

/**
 * <p>
 * Title: SentenzaController
 * </p>
 * <p>
 * Description: Controller dell'entità Sentenza
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
public class SentenzaController extends SiapController implements ISentenza {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	/**
	 * Inserisce in BancaDati la sentenza
	 *
	 * @param aSentenza
	 * @return SentenzaModel
	 * @throws F3BException
	 */

	public BigDecimal ExGetCountSentenze(SentenzaModel aSentenza) throws F3BException {

		BigDecimal lCount = new BigDecimal(0);
		Connection lConn = null;

		SentenzaSqlDAO lSenSqlDao = null;
		try {
			lConn = getDBConnection();
			lSenSqlDao = new SentenzaSqlDAO(lConn);
			lSenSqlDao.getCountSentenze(aSentenza);
			lSenSqlDao.start();
			lSenSqlDao.next();
			lCount = lSenSqlDao.getBigDecimal("HowManyRecords");
			lSenSqlDao.stop();
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error(daoEx.getLocalizedMessage());
			throw new SIEPException(SIEPException.USER_MESSAGE,
					"SentenzaController.ExGetCountSentenze:  " + daoEx);
		} finally {
			cleanup(lSenSqlDao);
			cleanup(lConn);
		}
		return lCount;
	}

	/**
	 * Inserisce Sentenza
	 *
	 * @param aSentenza
	 * @return
	 * @throws F3BException
	 */
	public SentenzaModel ExInserisciSentenza(SentenzaModel aSentenza) throws F3BException {

		Connection lConn = null;
		SentenzaModel lSen = null;
		try {
			lConn = getDBConnection();
			lSen = ExInserisciSentenza(aSentenza, lConn);

			commit(lConn);
		} catch (F3BException ex) {
			rollback(lConn);
			throw ex;
		} catch (Exception e) {
			rollback(lConn);
			throw new F3BException("SentenzaController.ExInserisciSentenza -> " + e);
		} finally {
			cleanup(lConn);
		}

		return lSen;
	}

	private SentenzaModel ExInserisciSentenza(SentenzaModel aSentenza, Connection aConn) throws Exception {

		SentenzaDAO lSenDao = null;
		SentenzaModel lSen = null;
		SentenzaSqlDAO lSenSQL = null;

		try {
			lSenSQL = new SentenzaSqlDAO(aConn);
			SentenzaModel lRis = null;
			// -- AMBROSINO 04/2011 - Non si accorgeva della differenza tra sentenza
			// e sentenza cassazone
			// lSenSQL.ricercaSentenzaDuplicata(aSentenza);

			lSenSQL.ricercaSentenzaDuplicataCassazione(aSentenza);
			// --
			Vector lRisRic = new Vector(lSenSQL.getModels());

			if (lRisRic.size() > 0) {
				lRis = (SentenzaModel) lRisRic.get(0);
				String lDescrizioneProvv = "";
				if (lRis.getCodTipoProvvedimento() != null && lRis.getCodTipoProvvedimento().equals("01"))
					lDescrizioneProvv = "La sentenza ";
				else
					lDescrizioneProvv = "Il decreto ";

				throw new SIEPException(SIEPException.SENTENZA_PRESENTE_NEL_SISTEMA,
						lDescrizioneProvv + lRis.getAnnoSentenza() + "/" + lRis.getNumeroSentenza() + " - "
								+ lRis.getDescrTipoAutoritaEmittente() + " <br>di "
								+ lRis.getDescrLuogoEmittente() + " è già presente in archivio.");
			}

			lSenDao = new SentenzaDAO(aConn);
			lSenDao.setDAOFromModel(aSentenza);
			BigDecimal lSequence = lSenDao.insert();
			lSen = new SentenzaModel(aSentenza);
			lSen.setIdSentenza(lSequence);
		} finally {
			// Scheda Intervento n° 6 - Ottimizzazione SIUS Avvocati
			cleanup(lSenDao);
			cleanup(lSenSQL);
		}

		return lSen;
	}

	/**
	 * ExRicercaSentenzaFascicoliPaged Ricerca la Sentenza in Banca Dati e fascicoli
	 *
	 * @param aSentenza
	 * @return
	 * @throws F3BException
	 */
	public Vector ExRicercaSentenzaFascicoliPaged(SentenzaModel aSentenza, int aPage) throws F3BException {

		Connection lConn = null;
		Vector lSentenzeFascicoli = new Vector();
		SentenzaSqlDAO lSenDao = null;
		FascicoloSiepSoggettoSqlDAO lFasDao = null;

		try {
			lConn = getDBConnection();
			lSenDao = new SentenzaSqlDAO(lConn);
			lFasDao = new FascicoloSiepSoggettoSqlDAO(lConn);

			Vector lSentenze = new Vector();
			lSenDao.ricercaSentenzaPaged(aSentenza, aPage);
			lSentenze = new Vector(lSenDao.getModels());
			lSenDao.stop();

			if (lSentenze.isEmpty())
				throw new SIEPException(SIEPException.USER_MESSAGE, "Nessun Elemento trovato");

			SentenzaFascicoliModel lSenFasMod = null;
			Vector lFascicoli = null;
			FascicoloSiepModel lFascicolo = new FascicoloSiepModel();

			Iterator itx = lSentenze.iterator();

			while (itx.hasNext()) {
				SentenzaModel lSenMod = (SentenzaModel) itx.next();
				lSenFasMod = new SentenzaFascicoliModel();
				lFascicoli = new Vector();

				lSenFasMod.setSentenza(lSenMod);

				lFasDao.ricercaFascicoloByIDSentenza(lSenMod.getIdSentenza());
				lFasDao.start();
				while (lFasDao.next()) {
					lFascicolo = (FascicoloSiepModel) lFasDao.getModelFascSogg();
					lFascicoli.add(lFascicolo);
				}

				lFasDao.stop();

				if (!lFascicoli.isEmpty())
					lSenFasMod.setFascicoli(
							(FascicoloSiepModel[]) lFascicoli.toArray(new FascicoloSiepModel[0]));

				lSentenzeFascicoli.add(lSenFasMod);
			}
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new SIEPException(
					"SentenzaController.ExRicercaSentenzaFascicoliPaged: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lSenDao);
			cleanup(lFasDao);
			cleanup(lConn);
		}

		return lSentenzeFascicoli;
	}

	/**
	 * Ricerca la Sentenza in Banca Dati
	 *
	 * @param aSentenza
	 * @return
	 * @throws F3BException
	 */
	public Vector ExRicercaSentenzaPaged(SentenzaModel aSentenza, int aPage) throws F3BException {

		Connection lConn = null;
		Vector lSentenzi = new Vector();
		SentenzaSqlDAO lSenDao = null;

		try {
			lConn = getDBConnection();
			lSenDao = new SentenzaSqlDAO(lConn);

			lSenDao.ricercaSentenzaPaged(aSentenza, aPage);
			lSentenzi = new Vector(lSenDao.getModels());

			if (lSentenzi.isEmpty())
				throw new SIEPException(SIEPException.USER_MESSAGE, "Nessun Elemento trovato");
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new SIEPException("SentenzaController.ExRicercaSentenza: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lSenDao);
			cleanup(lConn);
		}

		return lSentenzi;
	}

	/**
	 * Ricerca le Sentenze duplicate in Banca Dati
	 *
	 * @param aSentenza
	 * @return
	 * @throws F3BException
	 */
	public Vector ExRicercaSentenzaDuplicata(SentenzaModel aSentenza) throws F3BException {

		Connection lConn = null;
		Vector lSentenzi = new Vector();
		SentenzaSqlDAO lSenDao = null;

		try {
			lConn = getDBConnection();
			lSenDao = new SentenzaSqlDAO(lConn);

			lSenDao.ricercaSentenzaDuplicata(aSentenza);
			lSentenzi = new Vector(lSenDao.getModels());

			if (lSentenzi.isEmpty())
				throw new SIEPException(SIEPException.USER_MESSAGE, "Nessun Elemento trovato");
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new SIEPException(
					"SentenzaController.ExRicercaSentenzaDuplicata: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lSenDao);
			cleanup(lConn);
		}

		return lSentenzi;
	}

	/**
	 * @param aSentenza
	 * @return
	 * @throws F3BException
	 */
	public Vector ExRicercaSentenza(SentenzaModel aSentenza) throws F3BException {

		Connection lConn = null;
		Vector lSentenzi = new Vector();
		SentenzaSqlDAO lSenDao = null;

		try {
			lConn = getDBConnection();
			lSenDao = new SentenzaSqlDAO(lConn);
			lSenDao.ricercaSentenza(aSentenza);
			lSentenzi = new Vector(lSenDao.getModels());

			if (lSentenzi.isEmpty())
				throw new SIEPException(SIEPException.USER_MESSAGE, "Nessun Elemento trovato");
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new SIEPException("SentenzaController.ExRicercaSentenza: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lSenDao);
			cleanup(lConn);
		}

		return lSentenzi;
	}

	/**
	 * MEV 16: aggiunti parametri di passaggio per differenziare collegato al cumulo
	 */
	public Vector ExRicercaSentenzaWebServices(SentenzaModel aSentenza, boolean isForCumulo,
			String idFascicoloSiep, String idIstruttoriaCumulo) throws F3BException {

		Connection lConn = null;
		Vector lSentenzi = new Vector();
		SentenzaSqlDAO lSenSqlDao = null;

		try {
			lConn = getDBConnection();
			lSenSqlDao = new SentenzaSqlDAO(lConn);
			lSenSqlDao.ricercaSentenzaWebServices(aSentenza, isForCumulo, idFascicoloSiep,
					idIstruttoriaCumulo);
			lSentenzi = new Vector(lSenSqlDao.getModels());
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new SIEPException(
					"SentenzaController.ExRicercaSentenzaWebServices: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lSenSqlDao);
			cleanup(lConn);
		}

		return lSentenzi;
	}

	/**
	 * Ricerca la Sentenza in Banca Dati
	 *
	 * @param aSentenza
	 * @return
	 * @throws F3BException
	 */
	public SentenzaModel ExRicercaSentenzaByKey(BigDecimal aSentenzaKey) throws F3BException {

		Connection lConn = null;
		SentenzaModel lSen = new SentenzaModel();
		SentenzaSqlDAO lSenDao = null;
		SentenzaFascicoloSqlDAO lFasSenDao = null;

		try {
			lConn = getDBConnection();
			lSenDao = new SentenzaSqlDAO(lConn);
			lSenDao.ricercaSentenzaBykey(aSentenzaKey);
			lSen = new SentenzaModel((SentenzaModel) lSenDao.getModelByKey());

			lFasSenDao = new SentenzaFascicoloSqlDAO(lConn);
			lFasSenDao.getCountFascicoli(aSentenzaKey);
			lFasSenDao.start();
			lFasSenDao.next();
			int lCount = lFasSenDao.getInt("HowManyFascicoli");
			lFasSenDao.stop();
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.debug("FAscicoli trovati = " + lCount);
			if (lCount > 0)
				lSen.setEsistonoFascicoliAssociati(true);

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.debug("FAscicoli trovati = " + lSen.getEsistonoFascicoliAssociati());
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new SIEPException(
					"SentenzaController.ExRicercaSentenzaByKey: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lSenDao);
			cleanup(lFasSenDao);
			cleanup(lConn);
		}

		return lSen;
	}

	/**
	 * Modifica della Sentenza
	 *
	 * @param aSentenza
	 * @throws F3BException
	 */
	public SentenzaModel ExModificaSentenza(SentenzaModel aSentenza) throws F3BException {

		Connection lConn = null;
		SentenzaDAO lSenDao = null;
		SentenzaModel lSen = null;
		FascicoloSiepDAO lFasDao = null;

		try {
			lConn = getDBConnection();

			lSenDao = new SentenzaDAO(lConn);
			// Controllo se esiste un Fascicolo Siep Associato e validato
			lFasDao = new FascicoloSiepDAO(lConn);

			lFasDao.ricercaFascicoloBySentenza(aSentenza.getIdSentenza());

			if (lFasDao.getModelByKey() != null)
				throw new SIEPException(SIEPException.USER_MESSAGE,
						"Impossibile Modificare la Sentenza: Esiste un procedimento validato");

			aSentenza.toString();

			lSenDao.setDAOFromModelForUpdate(aSentenza);
			lSenDao.selCondizioneUpdate(aSentenza.getIdSentenza());
			lSenDao.update();

			commit(lConn);

			lSen = new SentenzaModel(aSentenza);
		} catch (DAOException ex) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + ex);
			throw new SIEPException("SentenzaController.ExModifica: " + ex);
		} finally {
			cleanup(lSenDao);
			cleanup(lFasDao);
			cleanup(lConn);
		}

		return lSen;
	}

	/**
	 * Modifica Sentenza X SIGE. La funzione realizza la modifica duplicando la Sentenza da modificare, per i
	 * Fascicoli SIGE selezionati, verranno poi aggiornati i riferimenti alla nuova Sentenza
	 *
	 * @param aSentenza
	 * @param aKeyFascicoli
	 * @return
	 * @throws F3BException
	 */
	public SentenzaModel ExModificaSentenzaSige(SentenzaModel aSentenza, Vector aKeyFascicoli)
			throws F3BException {

		Connection lConn = null;
		SentenzaModel lSen = null;
		BigDecimal lIdFascSige = null;
		SentenzaSigeModel lSenSige = null;
		FasSigeSentenzaDAO lFasSigeSenDao = null;
		TenoreSentenzaReatoDAO lTenSenReaDAO = null;

		try {
			lConn = getDBConnection();

			// Si duplica la Sentenza
			lSen = ExInserisciSentenza(aSentenza, lConn);

			lFasSigeSenDao = new FasSigeSentenzaDAO(lConn);
			lTenSenReaDAO = new TenoreSentenzaReatoDAO(lConn);

			// Aggiornamento dei riferimenti alla sentenza nei Fascicoli SIGE
			for (int i = 0; i < aKeyFascicoli.size(); i++) {
				lIdFascSige = (BigDecimal) aKeyFascicoli.get(i);

				// Update sulla tabella TENORE_SENTENZA_REATO
				lTenSenReaDAO.setDAOFromModelForUpdateSentenza(lSen);
				lTenSenReaDAO.selCondizioneSentenzaFascicolo(aSentenza.getIdSentenza(), lIdFascSige);
				lTenSenReaDAO.update();
				lTenSenReaDAO.stop();

				// Update sulla tabellaFAS_SIGE_SENTENZA
				lFasSigeSenDao.setDAOFromModelForUpdateIdSentenza(lSen);
				lSenSige = new SentenzaSigeModel(aSentenza);
				lSenSige.setFasIdFascicoloSige(lIdFascSige);
				lFasSigeSenDao.setCondizione(lSenSige);
				lFasSigeSenDao.update();
				lFasSigeSenDao.stop();
			}

			commit(lConn);

			commit(lConn);
		} catch (F3BException ex) {
			rollback(lConn);
			throw ex;
		} catch (Exception sqe) {
			rollback(lConn);
			throw new SIEPException("SentenzaController.ExModificaSentenzaSige: " + sqe);
		} finally {
			cleanup(lFasSigeSenDao);
			cleanup(lTenSenReaDAO);
			cleanup(lConn);
		}

		return lSen;
	}

	/**
	 * Cancella una Sentenza
	 *
	 * @param aSentenza
	 * @throws F3BException
	 */
	public void ExCancellaSentenza(SentenzaModel aSentenza) throws F3BException {

		Connection lConn = null;
		SentenzaDAO lSenDao = null;

		try {
			lConn = getDBConnection();
			lSenDao = new SentenzaDAO(lConn);
			lSenDao.selCondizioneUpdate(aSentenza.getIdSentenza());
			lSenDao.delete();
			commit(lConn);
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new SIEPException("SentenzaController.ExCancellaSentenza: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lSenDao);
			cleanup(lConn);
		}
	}

	/**
	 * Inserisci una sentenza da un'altra BDI
	 *
	 * @param lSent
	 * @param lConn
	 * @return
	 * @throws F3BException
	 */
	public String ExInserisciSentenzaWithoutSequence(SentenzaModel lSent, Connection lConn)
			throws F3BException {

		SentenzaDAO lSenDao = null;
		String lCodEsito = "00000";

		try {
			lSenDao = new SentenzaDAO(lConn);
			lSenDao.setDAOFromModel(lSent);
			lSenDao.setIdSentenza(lSent.getIdSentenza());
			lSenDao.setWithoutSequence(true);
			lSenDao.insert();
		} catch (DAOException ex) {
			if (ex.UNIQUE_CONSTRAINT_VIOLATED) {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
				siesLogger.debug("Sentenza gia' presente...");
				lCodEsito = "00001";
			} else {
				lCodEsito = "01400";
				throw new F3BException(F3BException.USER_MESSAGE, "Impossibile inserire la Sentenza! ");
			}
		} finally {
			cleanup(lSenDao);
		}
		return lCodEsito;
	}

	/**
	 * @param aSentenza
	 * @return
	 * @throws F3BException
	 */
	public Vector ExRicercaSentenzaFascicolo(SoggettoModel aSoggetto, String TipoBen) throws F3BException {

		Connection lConn = null;
		SentenzaFascicoloSqlDAO lSenFasDao = null;
		Vector lSentenzaFascicolo = new Vector();

		try {
			lConn = getDBConnection();
			lSenFasDao = new SentenzaFascicoloSqlDAO(lConn);
			lSenFasDao.ricercaSentenzaFascicolo(aSoggetto, TipoBen);
			lSentenzaFascicolo = new Vector(lSenFasDao.getModels());

			if (lSentenzaFascicolo.isEmpty())
				throw new SIEPException(SIEPException.USER_MESSAGE, "Nessun Elemento trovato");
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new SIEPException(
					"SentenzaController.ExRicercaSentenzaFascicolo: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lSenFasDao);
			cleanup(lConn);
		}

		return lSentenzaFascicolo;
	}

	public Vector ExRicercaSentenzeSoggettoPaged(SentenzaModel aSentenza, SoggettoModel aSoggetto, int aPage)
			throws F3BException {

		Connection lConn = null;
		Vector lSentenzi = new Vector();
		SentenzaSoggettoSqlDAO lSenDao = null;

		try {
			lConn = getDBConnection();
			lSenDao = new SentenzaSoggettoSqlDAO(lConn);

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.info("Cognome=" + aSoggetto.getCognome());
			lSenDao.ricercaSentenzeSoggetto(aSoggetto, aSentenza, aPage);
			lSentenzi = new Vector(lSenDao.getModels());

			if (lSentenzi.isEmpty())
				throw new SIEPException(SIEPException.USER_MESSAGE, "Nessun Elemento trovato");
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new SIEPException(
					"SentenzaController.ExRicercaSentenzeSoggettoPaged: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lSenDao);
			cleanup(lConn);
		}

		return lSentenzi;
	}

	public BigDecimal ExGetCountSentenzeSoggetto(SoggettoModel aSoggetto, SentenzaModel aSentenza)
			throws F3BException {

		BigDecimal lCount = new BigDecimal(0);
		Connection lConn = null;

		SentenzaSoggettoSqlDAO lSenSqlDao = null;
		try {
			lConn = getDBConnection();
			lSenSqlDao = new SentenzaSoggettoSqlDAO(lConn);
			lSenSqlDao.getCountSentenzeSoggetto(aSoggetto, aSentenza);
			lSenSqlDao.start();
			lSenSqlDao.next();
			lCount = lSenSqlDao.getBigDecimal("HowManySentenze");
			lSenSqlDao.stop();
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error(daoEx.getLocalizedMessage());
			throw new SIEPException(SIEPException.USER_MESSAGE,
					"SentenzaController.ExGetCountSentenze:  " + daoEx);
		} finally {
			cleanup(lSenSqlDao);
			cleanup(lConn);
		}
		return lCount;
	}

	public Vector<SentenzaFascicoliModel> ExRicercaElencoTitoliEsecutiviIscrittiaSIGEPaged(
			BigDecimal idFascicolo, int aPage) throws F3BException {

		Connection lConn = null;
		Vector<SentenzaFascicoliModel> lSentenzeFascicoli = new Vector<>();
		SentenzaSqlDAO lSenDao = null;
		FascicoloSiepSoggettoSqlDAO lFasDao = null;

		try {
			lConn = getDBConnection();
			lSenDao = new SentenzaSqlDAO(lConn);
			lFasDao = new FascicoloSiepSoggettoSqlDAO(lConn);

			Vector lSentenze = new Vector();

			lSenDao.ricercaElencoTitoliEsecutiviIscrittiaSIGEPaged(idFascicolo, aPage);
			lSentenze = new Vector(lSenDao.getModels());
			lSenDao.stop();

			if (lSentenze.isEmpty())
				throw new SIEPException(SIEPException.USER_MESSAGE, "Nessun Elemento trovato");

			SentenzaFascicoliModel lSenFasMod = null;
			Vector lFascicoli = null;
			FascicoloSiepModel lFascicolo = new FascicoloSiepModel();

			Iterator itx = lSentenze.iterator();

			while (itx.hasNext()) {
				SentenzaModel lSenMod = (SentenzaModel) itx.next();
				lSenFasMod = new SentenzaFascicoliModel();
				lFascicoli = new Vector();

				lSenFasMod.setSentenza(lSenMod);

				lFasDao.ricercaFascicoloByIDSentenza(lSenMod.getIdSentenza());
				lFasDao.start();
				while (lFasDao.next()) {
					lFascicolo = (FascicoloSiepModel) lFasDao.getModelFascSogg();
					lFascicoli.add(lFascicolo);
				}

				lFasDao.stop();

				if (!lFascicoli.isEmpty())
					lSenFasMod.setFascicoli(
							(FascicoloSiepModel[]) lFascicoli.toArray(new FascicoloSiepModel[0]));

				lSentenzeFascicoli.add(lSenFasMod);

			}
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new SIEPException(
					"SentenzaController.ExRicercaElencoTitoliEsecutiviIscrittiaSIGEPaged: Non posso leggere : "
							+ daoEx);
		} finally {
			cleanup(lSenDao);
			cleanup(lFasDao);
			cleanup(lConn);
		}

		return lSentenzeFascicoli;
	}

	@Override
	public BigDecimal ExGetCountaElencoTitoliEsecutiviIscrittiaSIGE(BigDecimal idFascicolo)
			throws F3BException {

		BigDecimal lCount = new BigDecimal(0);
		Connection lConn = null;

		SentenzaSqlDAO lSenSqlDao = null;
		try {
			lConn = getDBConnection();
			lSenSqlDao = new SentenzaSqlDAO(lConn);
			lSenSqlDao.countElencoTitoliEsecutiviIscrittiaSIGE(idFascicolo);
			lSenSqlDao.start();
			lSenSqlDao.next();
			lCount = lSenSqlDao.getBigDecimal("HowManyRecords");
			lSenSqlDao.stop();
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error(daoEx.getLocalizedMessage());
			throw new SIEPException(SIEPException.USER_MESSAGE,
					"SentenzaController.ExGetCountSentenze:  " + daoEx);
		} finally {
			cleanup(lSenSqlDao);
			cleanup(lConn);
		}
		return lCount;
	}

}