package siap.siepe.attivita.controller;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Iterator;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.dao.DAOException;
import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import siap.controller.SiapController;
import siap.siepe.SIEPEException;
import siap.siepe.assistentesocialeattivita.controller.IAssistenteSocialeAttivita;
import siap.siepe.assistentesocialeattivita.dao.AssistenteSocialeAttivitaDAO;
import siap.siepe.assistentesocialeattivita.model.AssistenteSocialeAttivitaModel;
import siap.siepe.attivita.dao.AttivitaDAO;
import siap.siepe.attivita.dao.AttivitaSqlDAO;
import siap.siepe.attivita.model.AttivitaModel;
import siap.siepe.espertoattivita.dao.EspertoAttivitaDAO;
import siap.siepe.relazione.controller.IRelazione;
import siap.siepe.relazione.dao.RelazioneDAO;
import siap.siepe.relazione.model.RelazioneModel;
import siap.siepe.util.SIEPELookupRemote;

/**
 * <p>
 * Title: AttivitaController
 * </p>
 * <p>
 * Description: Classe Controller per Attivita
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
public class AttivitaController extends SiapController implements IAttivita {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	/**
	 * Esegue l'inserimento di un'attività.
	 * <p>
	 *
	 * @param aAttivita
	 *            AttivitaModel Model poipolato con i dati da inserire.
	 * @throws F3BException
	 *             propaga errore di eccezione.
	 * @return AttivitaModel ritorna il model con i dati appena inseriti.
	 */
	public AttivitaModel ExInserisciAttivita(AttivitaModel aAttivita) throws F3BException {

		Connection lConn = null;
		AttivitaDAO lAttDao = null;
		AttivitaModel lAttMod = null;

		try {
			lConn = getDBConnection();

			lAttMod = new AttivitaModel(aAttivita);

			lAttDao = new AttivitaDAO(lConn);
			lAttDao.setDAOFromModel(aAttivita);
			BigDecimal lKey = null;
			lKey = lAttDao.insert();
			lAttMod.setIdAttivita(lKey);

			// Aggiornamento della Relazione Assistente Sociale - Attività
			aggiornaAssistenteSociale(lAttMod, lConn);

			commit(lConn);
		} catch (DAOException daoex) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoex);
			throw new SIEPEException("AttivitaController.ExInserisciAttivita: Non posso inserire: " + daoex);
		} catch (Exception ex) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("Exception: " + ex);
			throw new SIEPEException(
					"AttivitaController.ExInserisciAttivita: Non posso inserire il soggetti : " + ex);
		} finally {
			cleanup(lAttDao);
			cleanup(lConn);
		}
		return lAttMod;
	}

	/**
	 * Inserimento multiplo di una lista di Attività.
	 * <p>
	 *
	 * @param aListaAttivita
	 * @return
	 * @throws F3BException
	 */
	public AttivitaModel[] ExInserisciAttivita(AttivitaModel[] aListaAttivita, Connection aConn)
			throws Exception {

		AttivitaDAO lAttDao = null;
		BigDecimal lKey = null;

		lAttDao = new AttivitaDAO(aConn);
		for (int i = 0; i < aListaAttivita.length; i++) {
			lAttDao.setDAOFromModel(aListaAttivita[i]);
			lKey = lAttDao.insert();
			aListaAttivita[i].setIdAttivita(lKey);
			lAttDao.stop();
		}
		cleanup(lAttDao);
		return aListaAttivita;
	}

	public Vector ExRicercaAttivita(AttivitaModel aAttivita) throws F3BException {

		Connection lConn = null;
		AttivitaSqlDAO lAttDao = null;
		Vector lAttiviti = new Vector();

		try {
			lConn = getDBConnection();
			lAttDao = new AttivitaSqlDAO(lConn);
			lAttDao.ricercaAttivita(aAttivita);
			lAttiviti = new Vector(lAttDao.getModels());
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new SIEPEException("AttivitaController.ExRicercaAttivita: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lAttDao);
			cleanup(lConn);
		}
		return lAttiviti;
	}

	/**
	 * Metodo di ricerca di un'attività attraverso il proprio id.
	 * <p>
	 *
	 * @param aKey
	 *            BigDecimal chiave di ricerca.
	 * @throws F3BException
	 *             propaga errorri di eccezione
	 * @return AttivitaModel Model di ritorno con i dati.
	 */
	public AttivitaModel ExRicercaAttivitaByKey(BigDecimal aKey) throws F3BException {

		Connection lConn = null;
		AttivitaSqlDAO lAttDao = null;
		AttivitaModel lAttMod;

		try {
			lConn = getDBConnection();
			lAttDao = new AttivitaSqlDAO(lConn);
			lAttDao.ricercaAttivitaByKey(aKey);
			lAttMod = (AttivitaModel) lAttDao.getModelByKey();

			// Ricerca Relazioni
			IRelazione lRelCtrl = SIEPELookupRemote.getRelazioneRemote();
			Vector lRelazioni = lRelCtrl.ExRicercaRelazioniByAttivita(aKey);
			if (lRelazioni.size() > 0) {
				RelazioneModel[] lRelArray = new RelazioneModel[lRelazioni.size()];
				Iterator lItRel = lRelazioni.iterator();
				int i = 0;
				while (lItRel.hasNext()) {
					RelazioneModel lRelazione = (RelazioneModel) lItRel.next();
					// Occorre rifare la ricerca puntuale per ricavare anche il BLOB
					RelazioneModel lRelazioneBlob = lRelCtrl
							.ExRicercaRelazioneByKey(lRelazione.getIdRelazione());
					if (lRelazioneBlob != null) {
						lRelazione.setDocPerTrasferimento(lRelazioneBlob.getDocBlobOut());
						lRelArray[i] = lRelazione;
						i++;
					}
				}
				lAttMod.setRelazioni(lRelArray);
			}
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new SIEPEException("AttivitaController.ExRicercaAttivita: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lAttDao);
			cleanup(lConn);
		}
		return lAttMod;
	}

	public AttivitaModel ExModificaAttivita(AttivitaModel aAttivita) throws F3BException {

		Connection lConn = null;
		AttivitaDAO lAttDao = null;
		AttivitaModel lAttMod = new AttivitaModel(aAttivita);

		try {
			lConn = getDBConnection();
			// Aggiornamento della Relazione Assistente Sociale - Attività
			aggiornaAssistenteSociale(lAttMod, lConn);

			lAttDao = new AttivitaDAO(lConn);
			lAttDao.setDAOFromModelForUpdate(lAttMod);
			lAttDao.update();
			commit(lConn);
		} catch (Exception ex) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("Exception: " + ex);
			throw new SIEPEException("AttivitaController.ExModifica: " + ex);
		} finally {
			cleanup(lAttDao);
			cleanup(lConn);
		}
		return lAttMod;
	}

	/**
	 * La funzione attua la "chiusura" della Attività. Tale operazione si realizza valorizzando la data di
	 * chiusura e l'esito dell'attività.
	 *
	 * @param aAttivita
	 * @return
	 * @throws F3BException
	 */
	public AttivitaModel ExChiusuraAttivita(AttivitaModel aAttivita) throws F3BException {

		Connection lConn = null;
		AttivitaDAO lAttDao = null;
		AttivitaModel lAttMod = new AttivitaModel(aAttivita);

		try {
			lConn = getDBConnection();
			lAttDao = new AttivitaDAO(lConn);
			// Set dei campi da aggiornare
			lAttDao.setDataChiusura(lAttMod.getDataChiusura());
			lAttDao.setCodEsitoAttivita(lAttMod.getCodEsitoAttivita());
			lAttDao.setCodOperatoreAggiornamento(lAttMod.getCodOperatoreAggiornamento());
			lAttDao.setDataAggiornamento(lAttMod.getDataAggiornamento());
			lAttDao.setCodUfficioAggiornamento(lAttMod.getCodUfficioAggiornamento());
			lAttDao.setNotaChiusura(lAttMod.getNotaChiusura());

			// Condizione di Update ed Update
			lAttDao.setCondizioneUpdate(lAttMod.getIdAttivita());
			lAttDao.update();
			commit(lConn);
		} catch (Exception ex) {
			rollback(lConn);
			throw new SIEPEException("AttivitaController.ExChiusuraAttivita: " + ex);
		} finally {
			cleanup(lAttDao);
			cleanup(lConn);
		}
		return lAttMod;
	}

	/**
	 * Esegue la cancellazione di un'attività.
	 * <p>
	 *
	 * @param IdAttivita
	 *            : chiave del record da cancellare.
	 * @throws F3BException
	 *             propaga errore di eccezione.
	 */
	public void ExCancellaAttivita(BigDecimal aIdAttivita) throws F3BException {

		Connection lConn = null;
		AttivitaDAO lAttDao = null;
		RelazioneDAO lRelDao = null;
		EspertoAttivitaDAO lEspAttDao = null;
		AssistenteSocialeAttivitaDAO lAssAttDao = null;

		try {
			lConn = getDBConnection();

			// Vengono cancellate prima eventuali relazioni
			lRelDao = new RelazioneDAO(lConn);
			lRelDao.setCondizioneByIdAttivita(aIdAttivita);
			lRelDao.delete();

			// Cancellazione eventuali relazioni con esperti
			lEspAttDao = new EspertoAttivitaDAO(lConn);
			lEspAttDao.setCondizioneByIdAttivita(aIdAttivita);
			lEspAttDao.delete();

			// Cancellazione di eventuali relazioni con Assistenti Sociali
			lAssAttDao = new AssistenteSocialeAttivitaDAO(lConn);
			lAssAttDao.setCondizioneByIdAttivita(aIdAttivita);
			lAssAttDao.delete();

			// Cancellazione Attività
			lAttDao = new AttivitaDAO(lConn);
			lAttDao.setCondizioneUpdate(aIdAttivita);
			lAttDao.delete();

			commit(lConn);
		} catch (Exception eEx) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("Exception: " + eEx);
			throw new SIEPEException("AttivitaController.ExCancellaAttivita: " + eEx);
		} finally {
			cleanup(lRelDao);
			cleanup(lEspAttDao);
			cleanup(lAttDao);
			// Scheda Intervento n° 6 - Ottimizzazione SIUS Avvocati
			cleanup(lAssAttDao);
			cleanup(lConn);
		}
	}

	/**
	 * Seleziona un singolo documento rtf sul DB e lo restituisce come ByteArrayOutputStream
	 * <p>
	 *
	 * @param AttivitaModel
	 *            model con i dati dell'attivita.
	 * @return Array con il Documento recuperato dal DB.
	 * @throws F3BException
	 *             propaga errore di eccezione.
	 */
	public ByteArrayOutputStream ExGetDocumento(AttivitaModel aAttivita) throws F3BException {

		Connection lConn = null;
		AttivitaDAO lAttDao = null;
		ByteArrayOutputStream lByteArrayOut = null;
		try {
			lConn = getDBConnection();
			lAttDao = new AttivitaDAO(lConn);

			lAttDao.setIdAttivita(aAttivita.getIdAttivita());
			lAttDao.selByKey();

			lAttDao.start(1);

			if (lAttDao.next())
				lByteArrayOut = lAttDao.getDocBlob();

			lAttDao.stop();

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
			cleanup(lAttDao);
			cleanup(lConn);
		}
		return lByteArrayOut;
	}

	public ByteArrayOutputStream ExGetDocBlob(AttivitaModel aAttivita) throws F3BException {

		Connection lConn = null;
		AttivitaDAO lAttDao = null;
		ByteArrayOutputStream lByteArrayOut = null;
		try {
			lConn = getDBConnection();
			lAttDao = new AttivitaDAO(lConn);

			lAttDao.setIdAttivita(aAttivita.getIdAttivita());
			lAttDao.selByKey();

			lAttDao.start(1);

			if (lAttDao.next())
				lByteArrayOut = lAttDao.getDocBlob();

			lAttDao.stop();

		} catch (F3BException eF3b) {
			throw eF3b;
		} catch (Exception e) {
			e.printStackTrace();
			lByteArrayOut = null;
		} finally {
			cleanup(lAttDao);
			cleanup(lConn);
		}
		return lByteArrayOut;
	}

	/**
	 * Effettua l'operazione di update di un documento mandato tramite upload
	 * <p>
	 *
	 * @param aAttivita
	 *            Model popolato dei dati dell'attivita.
	 * @throws F3BException
	 *             propaga errore di eccezione.
	 */
	public AttivitaModel ExUpdateDocument(AttivitaModel aAttivita) throws F3BException {

		Connection lConn = null;
		AttivitaDAO lAttDao = null;
		AttivitaModel lAttivita = new AttivitaModel(aAttivita);
		try {
			lConn = getDBConnection();
			lAttDao = new AttivitaDAO(lConn);
			lAttDao.setDAOFromModelForUpdateBlob(aAttivita);
			lAttDao.update();
			commit(lConn);
		} catch (Exception eEx) {
			rollback(lConn);
			throw new SIEPEException("AttivitaController.ExUpdateDocument: " + eEx);
		} finally {
			cleanup(lAttDao);
			cleanup(lConn);
		}
		return lAttivita;
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
	public void ExAggiornaValidazioneAttivita(AttivitaModel aAttivita) throws F3BException {

		Connection lConn = null;
		AttivitaDAO lAttDao = null;

		try {

			lConn = getDBConnection();
			lAttDao = new AttivitaDAO(lConn);
			lAttDao.setDataAggiornamento(aAttivita.getDataAggiornamento());
			lAttDao.setCodUfficioAggiornamento(aAttivita.getCodUfficioAggiornamento());
			lAttDao.setCodOperatoreAggiornamento(aAttivita.getCodOperatoreAggiornamento());
			lAttDao.setFlagDocumentoRegistrato(aAttivita.getFlagDocumentoRegistrato());
			lAttDao.setCondizioneUpdate(aAttivita.getIdAttivita());

			lAttDao.update();
			commit(lConn);
		} catch (DAOException daoEx) {
			rollback(lConn);
			throw new F3BException(
					"AttivitaController.ExAggiornaValidazioneRichiesta: Non posso aggiornare : " + daoEx);
		} catch (Exception e) {
			rollback(lConn);
			throw new F3BException(
					"AttivitaController.ExAggiornaValidazioneRichiesta: Non posso aggiornare  : " + e);
		} finally {
			cleanup(lAttDao);
			cleanup(lConn);
		}
	}

	/**
	 * La funzione richiama il Controller predisposto alle operazioni sulla entità ASSISTENTE_SOCIALE_ATTIVITA
	 * per registrare l'inserimento di un nuovo Assistente Sociale sulla Attività.
	 *
	 * @param aAttivita
	 * @param aConn
	 * @throws F3BException
	 */
	private void aggiornaAssistenteSociale(AttivitaModel aAttivita, Connection aConn) throws F3BException {

		if (aAttivita.getAssSocIdAssSociale() != null) {
			AssistenteSocialeAttivitaModel lAssSocAtt = new AssistenteSocialeAttivitaModel();

			lAssSocAtt.setAttIdAttivita(aAttivita.getIdAttivita());
			lAssSocAtt.setAssSocIdAssSociale(aAttivita.getAssSocIdAssSociale());
			lAssSocAtt.setDataInizio(DateUtils.getSysDate());
			lAssSocAtt.setDataInserimento(lAssSocAtt.getDataInizio());
			// Si risale al codice dell'operatore e dell'ufficio che compie l'operazione
			if (aAttivita.getCodOperatoreAggiornamento().trim().length() > 0) {
				lAssSocAtt.setCodOperatoreInserimento(aAttivita.getCodOperatoreAggiornamento());
				lAssSocAtt.setCodUfficioInserimento(aAttivita.getCodUfficioAggiornamento());
			} else {
				lAssSocAtt.setCodOperatoreInserimento(aAttivita.getCodOperatoreInserimento());
				lAssSocAtt.setCodUfficioInserimento(aAttivita.getCodUfficioInserimento());
			}

			IAssistenteSocialeAttivita lAssSocAttCtrl = SIEPELookupRemote
					.getAssistenteSocialeAttivitaRemote();
			lAssSocAttCtrl.ExInserisciAssistenteSocialeAttivita(lAssSocAtt, aConn);
		}
	}

}