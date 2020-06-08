package siap.siepe.richiesta.controller;

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
import siap.siepe.richiesta.dao.RichiestaDAO;
import siap.siepe.richiesta.dao.RichiestaSqlDAO;
import siap.siepe.richiesta.model.RichiestaModel;

/**
 * <p>
 * Title: RichiestaController
 * </p>
 * <p>
 * Description: Classe Controller per Richiesta
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
public class RichiestaController extends SiapController implements IRichiesta {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	/**
	 * Metodo per l'inserimento di una richiesta, i valori sono passati con il corrispondente model come
	 * argomento.
	 * <p>
	 *
	 * @param aRichiesta
	 *            RichiestaModel Model passato come argomento, popolato con i valori.
	 * @throws F3BException
	 *             propaga errore di eccezione
	 * @return RichiestaModel ritorna il model con l'id cdella richiesta appena inserita.
	 */
	public RichiestaModel ExInserisciRichiesta(RichiestaModel aRichiesta) throws F3BException {

		Connection lConn = null;
		RichiestaDAO lRicDao = null;
		RichiestaModel lRicMod = null;

		try {
			lConn = getDBConnection(); // preleva connessione al dbase
			lRicMod = new RichiestaModel(aRichiesta);
			lRicDao = new RichiestaDAO(lConn);
			lRicDao.setDAOFromModel(aRichiesta);

			BigDecimal lKey = null;
			lKey = lRicDao.insert();
			lRicMod.setIdRichiesta(lKey);
			commit(lConn);

		} catch (DAOException daoex) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoex);
			throw new SIEPEException(
					"RichiestaController.ExInserisciRichiesta: Non posso inserire la richiesta : " + daoex);
		} catch (Exception ex) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("Exception: " + ex);
			throw new SIEPEException(
					"RichiestaController.ExInserisciRichiesta: Non posso inserire la richiesta : " + ex);
		} finally {
			cleanup(lRicDao);
			cleanup(lConn);
		}
		return lRicMod;
	}

	/**
	 * Metodo che esegue la ricerca di richieste, i quli partamentri di ricerca sono valorizzati nel relativo
	 * model passato come argomento.
	 * <p>
	 *
	 * @param aRichiesta
	 *            RichiestaModel model.
	 * @throws F3BException
	 *             propaga errore di eccezione.
	 * @return Vector ritorna l'insieme di RichiesteModel occorse.
	 */
	public Vector ExRicercaRichiesta(RichiestaModel aRichiesta) throws F3BException {

		Connection lConn = null;
		Vector lRichieste = new Vector();
		RichiestaSqlDAO lRicSqlDao = null;

		try {
			lConn = getDBConnection();
			lRicSqlDao = new RichiestaSqlDAO(lConn);
			lRicSqlDao.ricercaRichiesta(aRichiesta);
			lRichieste = new Vector(lRicSqlDao.getModels());

			/*
			 * if (lRichieste.size() == 0) { throw new
			 * SIEPEException(SIEPEException.USER_MESSAGE,"Nessun Elemento trovato"); }
			 */
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new SIEPEException("RichiestaController.ExRicercaRichiesta: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lRicSqlDao);
			cleanup(lConn);
		}

		return lRichieste;
	}

	/**
	 * Metodo che esegue la ricerca puntuale di una richiesta attraverso il proprio id.
	 * <p>
	 *
	 * @param aKey
	 *            BigDecimal Chiave di ricerca.
	 * @throws F3BException
	 *             propaga errore di ecceszione.
	 * @return RichiestaModel ritorna istanza del model popolato di dati.
	 */
	public RichiestaModel ExRicercaRichiestaByKey(BigDecimal aKey) throws F3BException {

		Connection lConn = null;
		RichiestaSqlDAO lRicSqlDao = null;
		RichiestaModel lRicMod;

		try {
			lConn = getDBConnection();
			lRicSqlDao = new RichiestaSqlDAO(lConn);
			// STUB : 20060920 - sost .
			// lRicDao.ricercaRichiestaByKey(aKey);
			lRicSqlDao.ricercaRichiestaById(aKey); // Esegue la ricerca di una richiesta
			lRicMod = (RichiestaModel) lRicSqlDao.getModelByKey();
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx); // Log
			throw new SIEPEException(
					"RichiestaController.ExRicercaRichiestaByKey: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lRicSqlDao);
			cleanup(lConn);
		}
		return lRicMod;
	}

	/**
	 * Metodo che esgue la modifica di una determinata richiesta opportunamente, puntata previo la propria
	 * chiave id.
	 * <p>
	 *
	 * @param aRichiesta
	 *            RichiestaModel Passatpo come argomento e contenente i dati da modificati
	 * @throws F3BException
	 *             propaga errore di eccezione.
	 * @return RichiestaModel ritorna il model con i dati modificati.
	 */
	public RichiestaModel ExModificaRichiesta(RichiestaModel aRichiesta) throws F3BException {

		Connection lConn = null;
		RichiestaDAO lRicDao = null;
		RichiestaModel lRicMod = new RichiestaModel(aRichiesta);

		try {
			lConn = getDBConnection();
			lRicDao = new RichiestaDAO(lConn);
			lRicDao.setDAOFromModelForUpdate(aRichiesta);
			lRicDao.update();
			commit(lConn);
		} catch (DAOException daoex) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoex);
			throw new SIEPEException(
					"RichiestaController.ExModificaRichiesta: Non posso modificare i dati : " + daoex);
		} catch (Exception ex) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("Exception: " + ex);
			throw new SIEPEException(
					"RichiestaController.ExModificaRichiesta: Non posso modificare i dati : " + ex);
		} finally {
			cleanup(lRicDao);
			cleanup(lConn);
		}
		return lRicMod;
	}

	/**
	 * Metodo che esegue la cancellazione di una richiesta, opportunamente puntata attraverso la chiave id.
	 * <p>
	 *
	 * @param aKey
	 *            BigDecimal id della richiesta da cancellare
	 * @throws F3BException
	 *             propaga errore di eccezione.
	 */
	public void ExCancellaRichiesta(BigDecimal aKey) throws F3BException {

		Connection lConn = null;
		RichiestaDAO lRicDao = null;
		RelazioneDAO lRelDao = null;

		try {
			lConn = getDBConnection();

			// Esegue la rimozione di Relazioni afferenti al ID della richiesta
			// da rimuovere
			lRelDao = new RelazioneDAO(lConn);
			lRelDao.setCondizioneByIdRichiesta(aKey);
			lRelDao.delete();

			// Esegue la rimozione della richiesta.
			lRicDao = new RichiestaDAO(lConn);
			lRicDao.setCondizioneUpdate(aKey);
			lRicDao.delete();

			commit(lConn);
		} catch (DAOException daoex) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoex);
			throw new SIEPEException(
					"RichiestaController.ExCancellaRichiesta: Non posso cancellare : " + daoex);
		} catch (Exception ex) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("Exception: " + ex);
			throw new SIEPEException("RichiestaController.ExCancellaRichiesta: Non posso cancellare : " + ex);
		} finally {
			cleanup(lRelDao);
			cleanup(lRicDao);
			cleanup(lConn);
		}
	}

	/**
	 * Seleziona un singolo documento rtf sul DB e lo restituisce come ByteArrayOutputStream
	 * <p>
	 *
	 * @param RichiestaModel
	 *            model con i dati della Richiesta.
	 * @return Array con il Documento recuperato dal DB.
	 * @throws F3BException
	 *             propaga errore di eccezione.
	 */
	public ByteArrayOutputStream ExGetDocumento(RichiestaModel aRichiesta) throws F3BException {

		Connection lConn = null;
		RichiestaDAO lRichDao = null;
		ByteArrayOutputStream lByteArrayOut = null;

		try {
			lConn = getDBConnection();
			lRichDao = new RichiestaDAO(lConn);

			// Imposta nel DAO l'id di Richiesta
			lRichDao.setIdRichiesta(aRichiesta.getIdRichiesta());
			lRichDao.selByKey(); // Imposta il filtro di WHERE Condition sull'id

			lRichDao.start(1); // Esegue lo start di un record

			if (lRichDao.next())
				lByteArrayOut = lRichDao.getDocBlob();

			lRichDao.stop();

			if (lByteArrayOut == null)
				throw new SIEPEException(SIEPEException.USER_MESSAGE, "Nessun Documento Associato.");

			if (lByteArrayOut.size() == 0)
				throw new SIEPEException(SIEPEException.USER_MESSAGE, "Nessun Documento Associato.");
		} catch (F3BException f3bEx) {
			throw f3bEx; // Rilancia errore di eccezione
		} catch (Exception e) {
			e.printStackTrace();
			lByteArrayOut = null;
		} finally {
			cleanup(lRichDao);
			cleanup(lConn);
		}
		return lByteArrayOut;
	}

	/**
	 * Seleziona un singolo documento rtf sul DB e lo restituisce come ByteArrayOutputStream. La differenza
	 * con il metodo ExGetDocumento e che quest'ultimo non ha il controllo sui ByteArray, con relativo lancio
	 * di errore di eccezione.
	 * <p>
	 *
	 * @param RichiestaModel
	 *            model con i dati della Richiesta.
	 * @return Array con il Documento recuperato dal DB.
	 * @throws F3BException
	 *             propaga errore di eccezione.
	 */
	public ByteArrayOutputStream ExGetDocBlob(RichiestaModel aRichiesta) throws F3BException {

		Connection lConn = null;
		RichiestaDAO lRichDao = null;
		ByteArrayOutputStream lByteArrayOut = null;

		try {
			lConn = getDBConnection();
			lRichDao = new RichiestaDAO(lConn);

			// Imposta nel DAO l'id di Richiesta
			lRichDao.setIdRichiesta(aRichiesta.getIdRichiesta());
			lRichDao.selByKey(); // Imposta il filtro di WHERE Condition sull'id

			lRichDao.start(1); // Esegue lo start di un record

			if (lRichDao.next())
				lByteArrayOut = lRichDao.getDocBlob();

			lRichDao.stop();
		} catch (F3BException f3bEx) {
			throw f3bEx; // Rilancia errore di eccezione
		} catch (Exception e) {
			e.printStackTrace();
			lByteArrayOut = null;
		} finally {
			cleanup(lRichDao);
			cleanup(lConn);
		}
		return lByteArrayOut;
	}

	/**
	 * Effettua l'operazione di update di un documento inviato tramite l'upload
	 * <p>
	 *
	 * @param aRichiestaModel
	 *            popolato dei dati della richiesta.
	 * @throws F3BException
	 *             propaga errore di eccezione.
	 */
	public RichiestaModel ExUpdateDocument(RichiestaModel aRichiesta) throws F3BException {

		Connection lConn = null;
		RichiestaDAO lRichDao = null;
		RichiestaModel lRichiesta = new RichiestaModel(aRichiesta);

		try {
			lConn = getDBConnection();
			lRichDao = new RichiestaDAO(lConn);
			// Imposta i dati per l'update
			lRichDao.setDAOFromModelForUpdateBlob(aRichiesta);
			lRichDao.update(); // Esregue l'update
			commit(lConn);
		} catch (Exception eEx) {
			rollback(lConn);
			throw new SIEPEException("RichiestaController.ExUpdateDocument: " + eEx);
		} finally {
			cleanup(lRichDao);
			cleanup(lConn);
		}
		return lRichiesta;
	}

	/**
	 * Effettua l'operazione di update del campo FLAG_DOCUMENTO_REGISTRATO.
	 * <p>
	 *
	 * @param aRichiesta
	 *            model di richiesta con i dati utili all'aggiornamnto.
	 * @throws F3BException
	 *             propaga errore di eccezione.
	 */
	public void ExAggiornaValidazioneRichiesta(RichiestaModel aRichiesta) throws F3BException {

		Connection lConn = null;
		RichiestaDAO lRichDao = null;

		try {
			lConn = getDBConnection();
			lRichDao = new RichiestaDAO(lConn);
			lRichDao.setDataAggiornamento(aRichiesta.getDataAggiornamento());
			lRichDao.setCodUfficioAggiornamento(aRichiesta.getCodUfficioAggiornamento());
			lRichDao.setCodOperatoreAggiornamento(aRichiesta.getCodOperatoreAggiornamento());
			lRichDao.setFlagDocumentoRegistrato(aRichiesta.getFlagDocumentoRegistrato());
			lRichDao.setCondizioneUpdate(aRichiesta.getIdRichiesta());

			lRichDao.update();
			commit(lConn);
		} catch (DAOException daoEx) {
			rollback(lConn);
			throw new F3BException(
					"RichiestaController.ExAggiornaValidazioneRichiesta: Non posso aggiornare : " + daoEx);
		} catch (Exception e) {
			rollback(lConn);
			throw new F3BException(
					"RichiestaController.ExAggiornaValidazioneRichiesta: Non posso aggiornare  : " + e);
		} finally {
			cleanup(lRichDao);
			cleanup(lConn);
		}
		return;
	}

}