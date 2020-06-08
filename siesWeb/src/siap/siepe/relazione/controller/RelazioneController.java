package siap.siepe.relazione.controller;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.dao.DAOException;
import f3b.log.LogF3B;
import f3b.util.F3BException;
import siap.controller.SiapController;
import siap.siepe.SIEPEException;
import siap.siepe.relazione.dao.RelazioneDAO;
import siap.siepe.relazione.model.RelazioneModel;

/**
 * <p>
 * Title: RelazioneController
 * </p>
 * <p>
 * Description: Classe Controller per Relazione
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
public class RelazioneController extends SiapController implements IRelazione {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	/**
	 * Esegue l'inserimento di una relazione.
	 * <p>
	 *
	 * @param aRelazione
	 *            RelazioneModel dati da inserire.
	 * @throws F3BException
	 *             propaga errore di eccezione.
	 * @return RelazioneModel Ritorna il model con i dati inseriti + l'id
	 */
	public RelazioneModel ExInserisciRelazione(RelazioneModel aRelazione) throws F3BException {

		Connection lConn = null;
		RelazioneDAO lRelDao = null;
		RelazioneModel lRelMod = null;

		try {
			// Esegue il controllo di univocità dei dati. Ossia, per una questione di sicurezza
			// e pulizia dei dati non è consentito inserire IDAttivita e IDRichiesta entrambi
			// valorizzati. Si esegue tale cpntrollo prima di prendere inutilmente una connessione
			// dal POOL.
			if (aRelazione.getAttIdAttivita() != null && aRelazione.getRicIdRichiesta() != null)
				throw new SIEPEException(
						"Non è consentito valorizzare entrambi gli attributi ( AttIdAttivita e RicIdRichiesta )");

			lConn = getDBConnection();

			lRelMod = new RelazioneModel(aRelazione);
			lRelDao = new RelazioneDAO(lConn);

			lRelDao.setDAOFromModel(aRelazione);
			BigDecimal lKey = null;
			lKey = lRelDao.insert();

			commit(lConn);
			lRelMod.setIdRelazione(lKey);
		} catch (Exception ex) {
			rollback(lConn);
			throw new SIEPEException("RelazioneController.ExInserisci: Non posso inserire: " + ex);
		} finally {
			cleanup(lRelDao);
			cleanup(lConn);
		}
		return lRelMod;
	}

	/**
	 * Esegue la ricerca di relazioni che corripondono ad uno degli ID di relazione contenuti nella tabella (
	 * ATT_ID_ATTIVITA o RIC_ID_RICHIESTA ).
	 * <p>
	 *
	 * @param aRelazione
	 *            RelazioneModel Model con i dati necessari per impostare le condizioni di filtro.
	 * @throws F3BException
	 *             Propaga errore di eccezione.
	 * @return Vector Elenco delle occorrenze.
	 */
	public Vector ExRicercaRelazione(RelazioneModel aRelazione) throws F3BException {

		Connection lConn = null;
		Vector lRelazioni = new Vector();
		RelazioneDAO lRelDao = null;

		try {
			lConn = getDBConnection(); // Preleva connessione dal dbase
			lRelDao = new RelazioneDAO(lConn);
			lRelDao.setCondizione(aRelazione); // Imposta le condizioni di filtro.
			lRelazioni = new Vector(lRelDao.getModels()); // Recupara le occorrenze
			// Se non vi sono occorrenze rilancia errore di eccezione.
			if (lRelazioni.size() == 0)
				throw new F3BException(F3BException.USER_MESSAGE, "Nessun Elemento trovato.");
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException("RelazioneController.ExRicercaRelazione: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lRelDao);
			cleanup(lConn);
		}
		return lRelazioni;
	}

	/**
	 * Metodo che esegue la ricerca di una relazione attraverso l'id
	 * <p>
	 *
	 * @param aKey
	 *            BigDecimal
	 * @throws F3BException
	 * @return RelazioneModel
	 */
	public RelazioneModel ExRicercaRelazioneByKey(BigDecimal aKey) throws F3BException {

		Connection lConn = null;
		RelazioneDAO lRelDao = null;
		RelazioneModel lRelMod = null;

		try {
			lConn = getDBConnection();
			lRelDao = new RelazioneDAO(lConn);

			lRelDao.setIdRelazione(aKey);
			lRelDao.selByKey();

			lRelDao.start();

			if (lRelDao.next())
				lRelMod = (RelazioneModel) lRelDao.getModelWithBlob();

			lRelDao.stop();
		} catch (Exception e) {
			throw new F3BException("RelazioneController.ExRicercaRelazioneByKey: " + e);
		} finally {
			cleanup(lRelDao);
			cleanup(lConn);
		}
		return lRelMod;
	}

	/**
	 * Metodo che segue la ricerca per l'ID di una determinata Attività
	 * <p>
	 *
	 * @param aKey
	 *            BigDecimal Chiave dell'ID attività
	 * @throws F3BException
	 *             propaga errore di eccezione
	 * @return Vector insieme di occorrenze
	 */
	public Vector ExRicercaRelazioniByAttivita(BigDecimal aKey) throws F3BException {

		Connection lConn = null;
		RelazioneDAO lRelDao = null;
		Vector lRelazioni = new Vector();

		try {
			lConn = getDBConnection();
			lRelDao = new RelazioneDAO(lConn);
			lRelDao.setCondizioneByIdAttivita(aKey);
			lRelazioni = new Vector(lRelDao.getModels());
		} catch (Exception e) {
			throw new F3BException("RelazioneController.ExRicercaRelazioniByAttivita: " + e);
		} finally {
			cleanup(lRelDao);
			cleanup(lConn);
		}
		return lRelazioni;
	}

	/**
	 * Metodo che segue la ricerca per l'ID di una determinata Richiesta.
	 * <p>
	 *
	 * @param aKey
	 *            BigDecimal Chiave dell'ID richiesta .
	 * @throws F3BException
	 *             propaga errore di eccezione.
	 * @return Vector insieme di occorrenze.
	 */
	public Vector ExRicercaRelazioniByRichiesta(BigDecimal aKey) throws F3BException {

		Connection lConn = null;
		RelazioneDAO lRelDao = null;
		Vector lRelazioni = new Vector();

		try {
			lConn = getDBConnection();
			lRelDao = new RelazioneDAO(lConn);
			lRelDao.setCondizioneByIdRichiesta(aKey);
			lRelazioni = new Vector(lRelDao.getModels());
		} catch (Exception e) {
			throw new F3BException("RelazioneController.ExRicercaRelazioniByRichiesta: " + e);
		} finally {
			cleanup(lRelDao);
			cleanup(lConn);
		}
		return lRelazioni;
	}

	/**
	 * Metodo che segue la ricerca per l'ID di una determinata Richiesta, delle realazioni.Inoltre, questo
	 * metodo si occupa di recuprare dal DB anche il contenuto del BLOB.
	 * <p>
	 *
	 * @param aKey
	 *            BigDecimal Chiave dell'ID richiesta .
	 * @throws F3BException
	 *             propaga errore di eccezione.
	 * @return Vector insieme di occorrenze.
	 */
	public Vector ExRicercaRelazioniWithBlobByRichiesta(BigDecimal aKey) throws F3BException {

		Connection lConn = null;
		RelazioneDAO lRelDao = null;
		Vector lRelazioni = new Vector();

		try {
			lConn = getDBConnection();
			lRelDao = new RelazioneDAO(lConn);
			lRelDao.setCondizioneByIdRichiesta(aKey);

			while (lRelDao.next())
				lRelazioni.add(lRelDao.getModelWithBlob());
		} catch (Exception e) {
			throw new F3BException("RelazioneController.ExRicercaRelazioniWithBlobByRichiesta: " + e);
		} finally {
			cleanup(lRelDao);
			cleanup(lConn);
		}
		return lRelazioni;
	}

	/**
	 * Metodo che segue la ricerca per l'ID di una determinata Attivita, delle realazioni.Inoltre, cosa
	 * importante, questo metodo si occupa di recuperare dal DB anche il contenuto del BLOB.
	 * <p>
	 *
	 * @param aKey
	 *            BigDecimal Chiave dell'ID attivita .
	 * @throws F3BException
	 *             propaga errore di eccezione.
	 * @return Vector insieme di occorrenze.
	 */
	public Vector ExRicercaRelazioniWithBlobByAttivita(BigDecimal aKey) throws F3BException {

		Connection lConn = null;
		RelazioneDAO lRelDao = null;
		Vector lRelazioni = new Vector();

		try {
			lConn = getDBConnection();
			lRelDao = new RelazioneDAO(lConn);
			lRelDao.setCondizioneByIdAttivita(aKey);

			while (lRelDao.next())
				lRelazioni.add(lRelDao.getModelWithBlob());
		} catch (Exception e) {
			throw new F3BException("RelazioneController.ExRicercaRelazioniWithBlobByAttivita: " + e);
		} finally {
			cleanup(lRelDao);
			cleanup(lConn);
		}
		return lRelazioni;
	}

	/**
	 * Seleziona un singolo documento rtf sul DB e lo restituisce come ByteArrayOutputStream
	 * <p>
	 *
	 * @param RelazioneModel
	 *            model contenente la chiave della Relazione.
	 * @return ByteArrayOutputStream con il Documento recuperato dal DB.
	 * @throws F3BException
	 *             propaga errore di eccezione.
	 */
	public ByteArrayOutputStream ExGetDocumento(RelazioneModel aRelazione) throws F3BException {

		Connection lConn = null;
		RelazioneDAO lRelDao = null;
		ByteArrayOutputStream lByteArrayOut = null;
		try {
			lConn = getDBConnection();
			lRelDao = new RelazioneDAO(lConn);

			lRelDao.setIdRelazione(aRelazione.getIdRelazione());
			lRelDao.selByKey();

			lRelDao.start(1);

			if (lRelDao.next())
				lByteArrayOut = lRelDao.getDocBlob();

			lRelDao.stop();

			if (lByteArrayOut == null)
				throw new SIEPEException(SIEPEException.USER_MESSAGE, "Nessun Documento Associato");

			if (lByteArrayOut.size() == 0)
				throw new SIEPEException(SIEPEException.USER_MESSAGE, "Nessun Documento Associato");

		} catch (F3BException eF3b) {
			throw eF3b;
		} catch (Exception e) {
			e.printStackTrace();
			lByteArrayOut = null;
		} finally {
			cleanup(lRelDao);
			cleanup(lConn);
		}
		return lByteArrayOut;
	}

	/**
	 * Meyodo che esegue la cancellazione di una Relazione, individuata attravero il proprio ID.
	 * <p>
	 *
	 * @param aKey
	 *            BigDecimal Id della Relazione da cancellare
	 * @throws F3BException
	 *             propaga errore di eccezione
	 */
	public void ExCancellaRelazione(BigDecimal aKey) throws F3BException {

		Connection lConn = null;
		RelazioneDAO lRelDao = null;

		try {
			lConn = getDBConnection();
			lRelDao = new RelazioneDAO(lConn);
			lRelDao.setCondizioneUpdate(aKey);
			lRelDao.delete();
			commit(lConn);
		} catch (Exception eEx) {
			rollback(lConn);
			throw new SIEPEException("RelazioneController.ExCancellaRelazione : " + eEx);
		} finally {
			cleanup(lRelDao);
			cleanup(lConn);
		}
	}

}