package siap.sius.documentoallegato.controller;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.util.xml.TreeModel;
import siap.controller.SiapController;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.stampa.controller.IStampa;
import siap.sico.utente.model.UtenteModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.util.report.ReportGenerator;
import siap.sius.SIUSException;
import siap.sius.avvocatura.action.ICostantiAvvisiAvvocato;
import siap.sius.avvocatura.dao.AvvisiAvvocatoDAO;
import siap.sius.avvocatura.model.AvvisiAvvocatoModel;
import siap.sius.depositodecreto.dao.DepositoDecretoDAO;
import siap.sius.depositoordinanzapc.dao.DepositoOrdinanzaPcDAO;
import siap.sius.documentoallegato.dao.DocumentoAllegatoDAO;
import siap.sius.documentoallegato.dao.DocumentoAllegatoSqlDAO;
import siap.sius.documentoallegato.model.DocumentoAllegatoModel;
import siap.sius.stampa.controller.IStampaSius;
import siap.sius.util.SIUSLookupRemote;

/**
 * Title: DocumentoAllegatoController
 * Description: Classe Controller per DocumentoAllegato
 *
 * @version 1.0
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class DocumentoAllegatoController extends SiapController implements IDocumentoAllegato {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	/**
	 * Inserisce DocumentoAllegato
	 *
	 * @param aDocumentoAllegato
	 * @return lDocAllRet
	 * @throws F3BException
	 */
	public DocumentoAllegatoModel ExInserisciDocumentoAllegato(DocumentoAllegatoModel aDocumentoAllegato)
			throws F3BException {

		Connection lConn = null;
		DocumentoAllegatoDAO lDocAllDao = null;
		DocumentoAllegatoSqlDAO lSqlDAO = null;
		DocumentoAllegatoModel lDocAllRet = new DocumentoAllegatoModel(aDocumentoAllegato);

		try {
			lConn = getDBTransaction();

			lDocAllDao = new DocumentoAllegatoDAO(lConn);
			// Setto l'anno e il progressivo...
			lSqlDAO = new DocumentoAllegatoSqlDAO(lConn);
			BigDecimal lProgr = lSqlDAO.getProgressivo(aDocumentoAllegato);
			aDocumentoAllegato.setNumeroProgressivo(new BigDecimal(lProgr.intValue() + 1));
			lDocAllDao.setDAOFromModel(aDocumentoAllegato);

			BigDecimal lKeyDocumentoAllegato = lDocAllDao.insert();
			lDocAllRet.setIdDocumentoAllegato(lKeyDocumentoAllegato);

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.info("Inserito documento allegato id = " + lKeyDocumentoAllegato);
			commit(lConn);
		} catch (Exception ex) {
			rollback(lConn);
			throw new F3BException("DocumentoAllegatoController.ExInserisciDocumentoAllegato: " + ex);
		} finally {
			cleanup(lDocAllDao);
			cleanup(lSqlDAO);
			cleanup(lConn);
		}
		return lDocAllRet;
	}

	/**
	 * Inserisce Foglio Complementare
	 *
	 * @param aDocumentoAllegato
	 * @return lDocAllRet
	 * @throws F3BException
	 */
	public DocumentoAllegatoModel ExInserisciFoglioComplementare(DocumentoAllegatoModel aDocumentoAllegato,
			String aCodTipo, BigDecimal aId) throws F3BException {

		Connection lConn = null;
		DocumentoAllegatoDAO lDocAllDao = null;
		DocumentoAllegatoSqlDAO lSqlDAO = null;
		DocumentoAllegatoModel lDocAllRet = new DocumentoAllegatoModel(aDocumentoAllegato);
		DepositoDecretoDAO lDecDao = null;
		DepositoOrdinanzaPcDAO lDepDao = null;

		try {
			lConn = getDBTransaction();

			lDocAllDao = new DocumentoAllegatoDAO(lConn);
			lSqlDAO = new DocumentoAllegatoSqlDAO(lConn);
			BigDecimal lProgr = lSqlDAO.getProgressivoByUff(aDocumentoAllegato.getCodUfficioInserimento(),
					aDocumentoAllegato.getAnnoFoglioComplementare());
			aDocumentoAllegato.setProgrFoglioComplementare(new BigDecimal(lProgr.intValue() + 1));
			lDocAllDao.setDAOFromModel(aDocumentoAllegato);
			BigDecimal lKeyDocumentoAllegato = lDocAllDao.insert();
			lDocAllRet.setIdDocumentoAllegato(lKeyDocumentoAllegato);
			lDocAllRet.setProgrFoglioComplementare(aDocumentoAllegato.getProgrFoglioComplementare());
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.info("Inserito foglio complementare id = " + lKeyDocumentoAllegato);

			// Prelevo Decreto
			if (aCodTipo.equals("02")) {
				// Setto il DAO dal Model di DepositoDecreto per l'Update
				lDecDao = new DepositoDecretoDAO(lConn);
				// Effettuo l'inserimento data deposito in DepositoDecretoModel; carico i dati da aggiornare.
				lDecDao.setCodUfficioAggiornamento(aDocumentoAllegato.getCodUfficioInserimento());
				lDecDao.setCodOperatoreAggiornamento(aDocumentoAllegato.getCodOperatoreInserimento());
				lDecDao.setDataAggiornamento(DateUtils.getSysDate());
				lDecDao.setDataCompFoglioComplementare(aDocumentoAllegato.getDataEmissione());
				lDecDao.setCondizioneUpdate(aId);
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.info("Fase di aggiornamento per DepositoDecreto (Data Deposito)");
				lDecDao.update();
			}

			// Prelevo Ordinanza
			if (aCodTipo.equals("03")) {
				// Setto il DAO dal Model di DepositoOrdinanza per l'Update
				lDepDao = new DepositoOrdinanzaPcDAO(lConn);
				// Effettuo l'inserimento data deposito in DepositoOrdinanzaModel; carico i dati da
				// aggiornare.
				lDepDao.setCodUfficioAggiornamento(aDocumentoAllegato.getCodUfficioInserimento());
				lDepDao.setCodOperatoreAggiornamento(aDocumentoAllegato.getCodOperatoreInserimento());
				lDepDao.setDataAggiornamento(DateUtils.getSysDate());
				lDepDao.setDataCompFoglioComplementare(aDocumentoAllegato.getDataEmissione());
				lDepDao.setCondizioneUpdate(aId);
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.info("Fase di aggiornamento per DepositoOrdinanza (Data Ordinanza)");
				lDepDao.update();
			}
			commit(lConn);
		} catch (Exception ex) {
			rollback(lConn);
			throw new F3BException("DocumentoAllegatoController.ExInserisciFoglioComplementare: " + ex);
		} finally {
			cleanup(lDocAllDao);
			cleanup(lSqlDAO);
			cleanup(lDecDao); // sca
			cleanup(lDepDao);// sca
			cleanup(lConn);
		}
		return lDocAllRet;
	}

	/**
	 * Metodo responsabile della modifica del foglio Complementare.
	 * <p>
	 *
	 * @param aDocumentoAllegato
	 *            dati del documento allaegato da modificare.
	 * @param aCodTipo
	 *            Codice tipo del provvedimento correlato.
	 * @param aId
	 *            id del provvedimento ( Decreto / Ordinanza ).
	 * @throws F3BException
	 *             propaga errore di eccezione.
	 */
	public void ExModificaFoglioComplementare(DocumentoAllegatoModel aDocumentoAllegato, String aCodTipo,
			BigDecimal aId) throws F3BException {

		Connection lConn = null;
		DocumentoAllegatoDAO lDocAllDao = null;
		DepositoDecretoDAO lDecDao = null;
		DepositoOrdinanzaPcDAO lDepDao = null;

		try {
			lConn = getDBTransaction();

			lDocAllDao = new DocumentoAllegatoDAO(lConn);
			lDocAllDao.setDataEmissione(aDocumentoAllegato.getDataEmissione());
			lDocAllDao.setDataTrasmissione(aDocumentoAllegato.getDataTrasmissione());
			lDocAllDao.setComuneSedeGiudiziaria(aDocumentoAllegato.getComuneSedeGiudiziaria());
			lDocAllDao.setDataAggiornamento(aDocumentoAllegato.getDataAggiornamento());
			lDocAllDao.setCodOperatoreAggiornamento(aDocumentoAllegato.getCodUfficioAggiornamento());
			lDocAllDao.setCodUfficioAggiornamento(aDocumentoAllegato.getCodUfficioAggiornamento());
			lDocAllDao.setCondizioneUpdate(aDocumentoAllegato.getIdDocumentoAllegato());
			lDocAllDao.setCodMotivazioneNonInvio(aDocumentoAllegato.getCodMotivazioneNonInvio());
			lDocAllDao.setDescrizioneNonInvio(aDocumentoAllegato.getDescrizioneNonInvio());
			lDocAllDao.setDataUltInvio(aDocumentoAllegato.getDataUltInvio());
			lDocAllDao.setDataInsMan(aDocumentoAllegato.getDataInsMan());

			lDocAllDao.update();

			// Aggiorna Decreto / Ordinanza
			if (aCodTipo.equals("02")) {
				// Imposta il DAO dal Model di DepositoDecreto per l'Update
				lDecDao = new DepositoDecretoDAO(lConn);
				// Effettua l'aggiornamento data deposito in DepositoDecretoModel; si caricano i dati da
				// aggiornare.
				lDecDao.setCodUfficioAggiornamento(aDocumentoAllegato.getCodUfficioAggiornamento());
				lDecDao.setCodOperatoreAggiornamento(aDocumentoAllegato.getCodOperatoreAggiornamento());
				lDecDao.setDataAggiornamento(aDocumentoAllegato.getDataAggiornamento());
				lDecDao.setDataCompFoglioComplementare(aDocumentoAllegato.getDataEmissione());
				lDecDao.setCondizioneUpdate(aId);

				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.info("Fase di aggiornamento per DepositoDecreto (Data Deposito)");

				lDecDao.update();
			} else if (aCodTipo.equals("03")) {
				// Imposta il DAO dal Model di DepositoOrdinanza per l'Update
				lDepDao = new DepositoOrdinanzaPcDAO(lConn);
				// Si Effettua l'aggiornamnto data deposito in DepositoOrdinanzaModel; si caricano i dati da
				// aggiornare.
				lDepDao.setCodUfficioAggiornamento(aDocumentoAllegato.getCodUfficioAggiornamento());
				lDepDao.setCodOperatoreAggiornamento(aDocumentoAllegato.getCodOperatoreAggiornamento());
				lDepDao.setDataAggiornamento(aDocumentoAllegato.getDataAggiornamento());
				lDepDao.setDataCompFoglioComplementare(aDocumentoAllegato.getDataEmissione());
				lDepDao.setCondizioneUpdate(aId);

				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.info("Fase di aggiornamento per DepositoOrdinanza (Data Ordinanza)");

				lDepDao.update();
			}

			commit(lConn);
		} catch (Exception ex) {
			rollback(lConn);
			throw new F3BException("DocumentoAllegatoController.ExModificaFoglioComplementare: " + ex);
		} finally {
			cleanup(lDocAllDao);
			cleanup(lDecDao);
			cleanup(lDepDao);
			cleanup(lConn);
		}
	}

	/**
	 * Ricerca DocumentoAllegato
	 *
	 * @param aDocumentoAllegato
	 * @return
	 * @throws F3BException
	 */
	public Vector ExRicercaDocumentoAllegato(DocumentoAllegatoModel aDocumentoAllegato) throws F3BException {

		Connection lConn = null;
		Vector lEventi = new Vector();
		DocumentoAllegatoSqlDAO llDocAllDao = null;

		try {
			lConn = getDBConnection();
			llDocAllDao = new DocumentoAllegatoSqlDAO(lConn);
			llDocAllDao.ricercaDocumentoAllegato(aDocumentoAllegato);
			lEventi = new Vector(llDocAllDao.getModels());
			if (lEventi.size() == 0)
				throw new F3BException(F3BException.USER_MESSAGE, "Nessun Elemento trovato");
		} catch (Exception sqe) {
			throw new F3BException(
					"DocumentoAllegatoController.ExRicercaDocumentoAllegato: Non posso leggere  : " + sqe);
		} finally {
			cleanup(llDocAllDao);
			cleanup(lConn);
		}
		return lEventi;
	}

	/**
	 * Ricerca DocumentoAllegato
	 *
	 * @param aIdEvento
	 * @return Vector
	 * @throws F3BException
	 */
	public Vector ExRicercaDocumentoAllegato(BigDecimal aIdEvento) throws F3BException {

		Connection lConn = null;
		Vector lEventi = null;
		DocumentoAllegatoSqlDAO llDocAllDao = null;

		try {
			lConn = getDBConnection();
			llDocAllDao = new DocumentoAllegatoSqlDAO(lConn);
			llDocAllDao.ricercaDocumentoAllegatoByIdEvento(aIdEvento);
			lEventi = new Vector(llDocAllDao.getModels());
			if (lEventi.size() == 0)
				throw new F3BException(F3BException.USER_MESSAGE, "Nessun Elemento trovato");
		} catch (Exception daoEx) {
			throw new F3BException(
					"DocumentoAllegatoController.ExRicercaDocumentoAllegato: Non posso leggere : " + daoEx);
		} finally {
			cleanup(llDocAllDao);
			cleanup(lConn);
		}
		return lEventi;
	}

	/**
	 * Seleziona un singolo documento dal BLOB nella tabella e lo restituisce come ByteArrayOutputStream.
	 *
	 * @param aKey
	 * @return ByteArray con il Documento recuperato dal DB
	 * @throws F3BException
	 */
	public ByteArrayOutputStream ExGetDocumentoByKey(BigDecimal aKey) throws F3BException {

		Connection lConn = null;
		DocumentoAllegatoDAO lDocAlDao = null;
		ByteArrayOutputStream lByteArrayOut = null;

		try {
			// Viene istanziata connessione e DAO
			lConn = getDBConnection();
			lDocAlDao = new DocumentoAllegatoDAO(lConn);

			// Settaggio della chiave di ricerca
			lDocAlDao.setIdDocumentoAllegato(aKey);
			lDocAlDao.selByKey();

			// Ricerca ed estrazione del BLOB
			lDocAlDao.start(1);

			if (lDocAlDao.next())
				lByteArrayOut = lDocAlDao.getDocBlob();

			lDocAlDao.stop();

			if (lByteArrayOut == null)
				throw new F3BException(F3BException.USER_MESSAGE, "Nessun Documento Associato");

			if (lByteArrayOut.size() == 0)
				throw new F3BException(F3BException.USER_MESSAGE, " Documento Vuoto");

		} catch (F3BException eF3b) {
			throw eF3b;
		} catch (Exception e) {
			e.printStackTrace();
			lByteArrayOut = null;
		} finally {
			cleanup(lDocAlDao);
			cleanup(lConn);
		}

		return lByteArrayOut;
	}

	/**
	 * Modifica del Documento Stampato...
	 *
	 * @param aDocumentoAllegato
	 * @throws F3BException
	 */
	public void ExModificaDocumentoAllegato(DocumentoAllegatoModel aDocumentoAllegato) throws F3BException {

		Connection lConn = null;
		DocumentoAllegatoDAO llDocAllDao = null;

		try {
			lConn = getDBConnection();
			llDocAllDao = new DocumentoAllegatoDAO(lConn);
			llDocAllDao.setDAOFromModelForUpdate(aDocumentoAllegato);
			llDocAllDao.update();
			commit(lConn);
		}

		catch (Exception sqe) {
			rollback(lConn);
			throw new F3BException(
					"DocumentoAllegatoController.ExModificaDocumentoAllegato: Non posso inserire il soggetti : "
							+ sqe);
		} finally {
			cleanup(llDocAllDao);
			cleanup(lConn);
		}
	}

	public void ExCancellaDocumentoAllegato(DocumentoAllegatoModel aDocumentoAllegato) throws F3BException {

		Connection lConn = null;
		DocumentoAllegatoDAO llDocAllDao = null;

		try {
			lConn = getDBConnection();
			llDocAllDao = new DocumentoAllegatoDAO(lConn);
			llDocAllDao.setCondizioneUpdate(aDocumentoAllegato.getIdDocumentoAllegato());
			llDocAllDao.delete();
			commit(lConn);
		} catch (Exception sqe) {
			rollback(lConn);
			throw new F3BException("DocumentoAllegatoController.ExCancellaDocumentoAllegato: " + sqe);
		} finally {
			cleanup(llDocAllDao);
			cleanup(lConn);
		}
	}

	/**
	 * Funzione di cancellazione logica del record.
	 *
	 * @param aModel
	 * @throws F3BException
	 */
	public void ExAnnullaDocumentoAllegato(DocumentoAllegatoModel aModel) throws F3BException {

		Connection lConn = null;
		DocumentoAllegatoDAO llDocAllDao = null;

		try {
			lConn = getDBConnection();
			llDocAllDao = new DocumentoAllegatoDAO(lConn);
			llDocAllDao.setCodUfficioAggiornamento(aModel.getCodUfficioAggiornamento());
			llDocAllDao.setCodOperatoreAggiornamento(aModel.getCodOperatoreAggiornamento());
			llDocAllDao.setDataAggiornamento(aModel.getDataAggiornamento());
			llDocAllDao.setDataAnnullamento(aModel.getDataAnnullamento());
			llDocAllDao.setMotivoAnnullamento(aModel.getMotivoAnnullamento());
			llDocAllDao.setCondizioneUpdate(aModel.getIdDocumentoAllegato());
			llDocAllDao.setFlagDocumentoRegistrato("A");
			llDocAllDao.update();
			commit(lConn);
		} catch (Exception daoEx) {
			rollback(lConn);
			throw new F3BException("DocumentoAllegatoController.ExAnnullaDocumentoAllegato: " + daoEx);
		} finally {
			cleanup(llDocAllDao);
			cleanup(lConn);
		}
	}

	/**
	 * Stampa un documento di Ordine d'esecuzione per condannato Libero
	 *
	 * @param aDocumentoAllegato
	 * @return lByteArrayOut
	 * @throws F3BException
	 */
	public ByteArrayOutputStream ExStampaDocumento(DocumentoAllegatoModel aDocumentoAllegato,
			UtenteModel aUtenteModel) throws F3BException {

		Connection lConn = null;
		DocumentoAllegatoDAO llDocAllDao = null;

		ByteArrayOutputStream lByteArrayOut = null;
		try {
			DocumentoAllegatoModel lDocAllMod = this
					.ExRicercaDocumentoAllegatoByKey(aDocumentoAllegato.getIdDocumentoAllegato());

			// Prelevamento di EventoNotificaModel Per Estrazione Dati Stampa.
			IEvento lCtrl = SICOLookupRemote.getEventoRemote();
			EventoNotificaModel lEveMod = lCtrl.ExRicercaEventoNotificaByKey(lDocAllMod.getEveIdEvento());

			lDocAllMod.setDescrUfficioInserimento(aDocumentoAllegato.getDescrUfficioInserimento());

			IStampa lStampa = SICOLookupRemote.getStampaRemote();

			TreeModel lTree = lStampa.prelevaDatiEventoSiep(lEveMod);

			// ReportGenerator lReport = new ReportGenerator();
			ReportGenerator lReport = new ReportGenerator(aUtenteModel.getUfficioUtente().getCodUfficio());
			String lNomeTemplate = "aaaa da valorizzare";
			// String lNomeTemplate =
			// TemplateManager.getInstance().getTemplateName(aDocumentoAllegato.getNomeTemplate());

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.info("NOME TEMPLATE >>>" + lNomeTemplate);
			lByteArrayOut = (ByteArrayOutputStream) lReport.generateDocument(lTree, lNomeTemplate);

			ByteArrayInputStream lByteArrayInput = new ByteArrayInputStream(lByteArrayOut.toByteArray());

			aDocumentoAllegato.setDocBlobIn(lByteArrayInput);
			lConn = getDBConnection();
			llDocAllDao = new DocumentoAllegatoDAO(lConn);
			llDocAllDao.setDAOFromModelForUpdateBlob(aDocumentoAllegato);

			llDocAllDao.setCondizioneUpdate(aDocumentoAllegato.getIdDocumentoAllegato());
			llDocAllDao.update();
			commit(lConn);
		} catch (Exception sqe) {
			rollback(lConn);
			throw new F3BException("DocumentoAllegatoController.ExStampaDocumento: " + sqe);
		} finally {
			cleanup(llDocAllDao);
			cleanup(lConn);
		}
		return lByteArrayOut;
	}

	/**
	 * Effettua l'operazione di update di un documento mandato tramite upload
	 *
	 * @param aDocumentoAllegato
	 * @throws F3BException
	 */
	public DocumentoAllegatoModel ExUpdateDocument(DocumentoAllegatoModel aDocumentoAllegato)
			throws F3BException {

		Connection lConn = null;
		DocumentoAllegatoDAO llDocAllDao = null;
		DocumentoAllegatoModel lDocAllMod = new DocumentoAllegatoModel(aDocumentoAllegato);

		try {
			lConn = getDBConnection();
			llDocAllDao = new DocumentoAllegatoDAO(lConn);
			llDocAllDao.setDAOFromModelForUpdateBlob(aDocumentoAllegato);
			llDocAllDao.setCondizioneUpdate(aDocumentoAllegato.getIdDocumentoAllegato());
			llDocAllDao.update();
			commit(lConn);
		} catch (Exception sqe) {
			rollback(lConn);
			throw new F3BException("DocumentoAllegatoController.ExUpdateDocument: " + sqe);
		} finally {
			cleanup(llDocAllDao);
			cleanup(lConn);
		}
		return lDocAllMod;
	}

	public DocumentoAllegatoModel ExRicercaDocumentoAllegatoByKey(BigDecimal aKey) throws F3BException {

		Connection lConn = null;
		DocumentoAllegatoSqlDAO llDocAllDao = null;
		DocumentoAllegatoModel lDocAllMod;

		try {
			lConn = getDBConnection();
			llDocAllDao = new DocumentoAllegatoSqlDAO(lConn);
			llDocAllDao.ricercaDocumentoAllegatoByKey(aKey);
			lDocAllMod = (DocumentoAllegatoModel) llDocAllDao.getModelByKey();
		} catch (Exception sqe) {
			throw new F3BException("DocumentoAllegatoController.ExRicercaDocumentoAllegatoByKey: " + sqe);
		} finally {
			cleanup(llDocAllDao);
			cleanup(lConn);
		}
		return lDocAllMod;
	}

	public Vector ExRicercaDocumentoAllegatoByIdEvento(BigDecimal aKey) throws F3BException {

		Connection lConn = null;
		DocumentoAllegatoSqlDAO llDocAllDao = null;
		Vector lDocs = new Vector();

		try {
			lConn = getDBConnection();
			llDocAllDao = new DocumentoAllegatoSqlDAO(lConn);
			llDocAllDao.ricercaDocumentoAllegatoByIdEvento(aKey);
			lDocs = new Vector(llDocAllDao.getModels());

			if (lDocs.size() == 0)
				throw new SIUSException(F3BException.USER_MESSAGE, "Nessun Elemento trovato");

		} catch (Exception sqe) {
			throw new SIUSException(
					"DocumentoAllegatoController.ExRicercaDocumentoAllegatoByIdEvento: " + sqe);
		} finally {
			cleanup(llDocAllDao);
			cleanup(lConn);
		}
		return lDocs;
	}

	public Vector ExRicercaSollecitoByIdEvento(BigDecimal aKey) throws F3BException {

		Connection lConn = null;
		DocumentoAllegatoSqlDAO llDocAllDao = null;
		Vector lDocs = new Vector();

		try {
			lConn = getDBConnection();
			llDocAllDao = new DocumentoAllegatoSqlDAO(lConn);
			llDocAllDao.ricercaSollecitoByIdEvento(aKey);
			lDocs = new Vector(llDocAllDao.getModels());

		} catch (Exception sqe) {
			throw new SIUSException("DocumentoAllegatoController.ExRicercaSollecitoByIdEvento: " + sqe);
		} finally {
			cleanup(llDocAllDao);
			cleanup(lConn);
		}
		return lDocs;
	}

	/**
	 * Controlla se il documento è validato.
	 *
	 * @param aIdEvento
	 *            : BigDecimal, id dell'Evento collegato; aCodTipo: String, codice tipo di documento.
	 * @return: boolean , true solo se FLAG_DOCUMENTO_REGISTRATO == "S".
	 * @throws F3BException
	 */

	public boolean IsValidato(BigDecimal aIdEvento, String aCodTipo) throws F3BException {

		Connection lConn = null;
		DocumentoAllegatoSqlDAO llDocAllDao = null;
		DocumentoAllegatoModel lDocAllMod = null;
		boolean aRet = false; // Valore di ritorno

		try {
			lConn = getDBConnection();
			llDocAllDao = new DocumentoAllegatoSqlDAO(lConn);
			lDocAllMod = new DocumentoAllegatoModel();

			// Si settano le condizioni di ricerca
			lDocAllMod.setEveIdEvento(aIdEvento);
			lDocAllMod.setCodTipoDocumento(aCodTipo);
			// Ricerca
			llDocAllDao.ricercaDocumentoAllegato(lDocAllMod);
			// STUB: si presuppone che il documento sia unico
			lDocAllMod = (DocumentoAllegatoModel) llDocAllDao.getModelByKey();

		} catch (Exception sqe) {
			throw new SIUSException("DocumentoAllegatoController.IsValidato: " + sqe);
		} finally {
			cleanup(llDocAllDao);
			cleanup(lConn);
			if (lDocAllMod != null && (lDocAllMod.getFlagDocumentoRegistrato().compareTo("S") == 0))
				aRet = true;
		}
		return aRet;
	}

	public DocumentoAllegatoModel ExRicercaDocumentoAllegatoByIdEventoCodTipo(BigDecimal aIdEvento,
			String aCodTipo) throws F3BException {

		Connection lConn = null;
		DocumentoAllegatoSqlDAO llDocAllDao = null;
		DocumentoAllegatoModel lDocAllMod = null;

		try {
			lConn = getDBConnection();
			llDocAllDao = new DocumentoAllegatoSqlDAO(lConn);
			lDocAllMod = new DocumentoAllegatoModel();

			// Si settano le condizioni di ricerca
			lDocAllMod.setEveIdEvento(aIdEvento);
			lDocAllMod.setCodTipoDocumento(aCodTipo);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.info("Ricerca allegato1: " + lDocAllMod);
			// Ricerca
			llDocAllDao.ricercaDocumentoAllegato(lDocAllMod);
			// STUB: si presuppone che il documento sia unico
			lDocAllMod = (DocumentoAllegatoModel) llDocAllDao.getModelByKey();
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.info("Ricerca allegato2: " + lDocAllMod);
		} catch (Exception sqe) {
			throw new SIUSException(
					"DocumentoAllegatoController.ExRicercaDocumentoAllegatoByIdEventoCodTipo: " + sqe);
		} finally {
			cleanup(llDocAllDao);
			cleanup(lConn);
		}
		return lDocAllMod;
	}

	public Vector ExRicercaDocAllAnnulatiByIdEventoCodTipo(BigDecimal aIdEvento, String aCodTipo)
			throws F3BException {

		Connection lConn = null;
		Vector LDocumenti = new Vector();
		DocumentoAllegatoSqlDAO llDocAllDao = null;
		DocumentoAllegatoModel lDocAllMod = null;

		try {
			lConn = getDBConnection();
			llDocAllDao = new DocumentoAllegatoSqlDAO(lConn);

			// Si settano le condizioni di ricerca
			lDocAllMod = new DocumentoAllegatoModel();
			lDocAllMod.setEveIdEvento(aIdEvento);
			lDocAllMod.setCodTipoDocumento(aCodTipo);
			// Ricerca
			llDocAllDao.ricercaDocumentoAllegatoAnnullato(lDocAllMod);

			LDocumenti = new Vector(llDocAllDao.getModels());
		} catch (Exception daoEx) {
			throw new F3BException(
					"DocumentoAllegatoController.ExRicercaDocAllAnnulatiByIdEventoCodTipo: " + daoEx);
		} finally {
			cleanup(llDocAllDao);
			cleanup(lConn);
		}
		return LDocumenti;
	}

	/**
	 * Effettua l'operazione di update del campo FLAG_DOCUMENTO_REGISTRATO. La chiave di selezione dell'UPDATE
	 * può essere in alternativa ID_DOCUMENTO_ALLEGATO O EVE_ID_EVENTO.
	 *
	 * @param aEvento
	 * @throws F3BException
	 */
	public void ExAggiornaValidazione(DocumentoAllegatoModel aDocumentoAllegato) throws F3BException {

		Connection lConn = null;
		DocumentoAllegatoDAO llDocAllDao = null;

		try {
			lConn = getDBConnection();
			llDocAllDao = new DocumentoAllegatoDAO(lConn);
			llDocAllDao.setDataAggiornamento(aDocumentoAllegato.getDataAggiornamento());
			llDocAllDao.setCodUfficioAggiornamento(aDocumentoAllegato.getCodUfficioAggiornamento());
			llDocAllDao.setCodOperatoreAggiornamento(aDocumentoAllegato.getCodOperatoreAggiornamento());
			llDocAllDao.setFlagDocumentoRegistrato(aDocumentoAllegato.getFlagDocumentoRegistrato());
			if (aDocumentoAllegato.getIdDocumentoAllegato() != null)
				llDocAllDao.setCondizioneUpdate(aDocumentoAllegato.getIdDocumentoAllegato());
			else if (aDocumentoAllegato.getEveIdEvento() != null)
				llDocAllDao.setCondizioneByEve(aDocumentoAllegato.getEveIdEvento());
			else
				throw new F3BException("Chiave di update non definita");
			llDocAllDao.update();
			commit(lConn);
		} catch (Exception e) {
			rollback(lConn);
			throw new F3BException("DocumentoAllegatoController.ExAggiornaValidazione: " + e);
		} finally {
			cleanup(llDocAllDao);
			cleanup(lConn);
		}
		return;
	}

	/**
	 * Estrae il BLOB dal DOCUMENTO_ALLEGATO e lo restituisce come Byte Array.
	 *
	 * @param aIdDocumentoAllegato
	 *            ,
	 * @return byte[].
	 * @throws F3BException
	 */
	public byte[] ExGetDocPerTrasferimento(BigDecimal aIdDocumentoAllegato) throws F3BException {

		byte[] lDocPerTrasferimento = null;

		Connection lConn = null;
		DocumentoAllegatoDAO lDocAllDao = null;
		ByteArrayOutputStream lByteArrayOut = null;
		try {
			lConn = getDBConnection();
			lDocAllDao = new DocumentoAllegatoDAO(lConn);

			lDocAllDao.setIdDocumentoAllegato(aIdDocumentoAllegato);
			lDocAllDao.selByKey();

			lDocAllDao.start(1);

			if (lDocAllDao.next())
				lByteArrayOut = lDocAllDao.getDocBlob();
			lDocAllDao.stop();

			if (lByteArrayOut != null && lByteArrayOut.size() > 0)
				lDocPerTrasferimento = lByteArrayOut.toByteArray();
		} catch (Exception e) {
			throw new F3BException(F3BException.EX_OPERATION_FAILED, e.toString());
		} finally {
			cleanup(lDocAllDao);
			cleanup(lConn);
		}
		return lDocPerTrasferimento;
	}

	/**
	 * Ricerca DocumentoAllegatoModel da ANNO, NUM, cod Ufficio, e CodTipoDocumento. Mentre i primi 3
	 * parametri vengono passati attraverso il model argomento, il CodTipoDocumento viene valorizzato dalla
	 * funzione stessa.
	 *
	 * @param DocumentoAllegatoModel
	 * @return
	 * @throws F3BException
	 */

	public DocumentoAllegatoModel ExRicercaFoglioComplementareByAnnoNumUfficio(
			DocumentoAllegatoModel aDocumentoAllegato) throws F3BException {

		Connection lConn = null;
		DocumentoAllegatoDAO lDao = null;
		DocumentoAllegatoModel lRisultato;
		// Il codice Tipo Documento viene imposto uguale
		// a quello che individua un Foglio Complementare
		// nella tabella DOCUMENTO_ALLEGATO.
		aDocumentoAllegato.setCodTipoDocumento("06");

		try {
			// Si effettua una ricerca nella Tabella DOCUMENTO_ALLEGATO
			lConn = getDBConnection();
			lDao = new DocumentoAllegatoDAO(lConn);
			lDao.setCondizione(aDocumentoAllegato);
			lRisultato = (DocumentoAllegatoModel) lDao.getModelByKey();
			if (lRisultato == null)
				throw new F3BException(F3BException.USER_MESSAGE, "Foglio Complementare non trovato");

		} catch (F3BException fe) {
			throw fe;
		} catch (Exception e) {
			throw new F3BException("Errore nella ricerca: " + e);
		} finally {
			cleanup(lDao);
			cleanup(lConn);
		}

		return lRisultato;
	}

	/**
	 * Esecuzione stampa Foglio Complementare
	 * <p>
	 *
	 * @param aModel
	 * @return ByteArrayOutputStream
	 * @throws F3BException
	 */
	public ByteArrayOutputStream ExStampaFoglioComp(BigDecimal aIdDocAllegato, String aCodUff,
			UtenteModel aUtenteModel) throws F3BException {

		ByteArrayOutputStream lByteArrayOut = null;
		ByteArrayInputStream lByteArrayInput = null;
		// Controller di stampa
		IStampaSius lCtrlSta = SIUSLookupRemote.getStampaRemote();

		Connection lConn = null;
		DocumentoAllegatoDAO lDADao = null;
		DocumentoAllegatoSqlDAO lDASqlDao = null;

		try {
			// Preleva connessione dal Db
			lConn = getDBConnection();

			// Lettura del Documento Allegato.
			DocumentoAllegatoModel lDAModel = ExRicercaDocumentoAllegatoByKey(aIdDocAllegato);

			if (lDAModel == null)
				throw new F3BException(F3BException.USER_MESSAGE, "Documento Allegato Mancante");

			lByteArrayOut = lCtrlSta.ExPreStampaFoglioComplementare(lDAModel, aCodUff, aUtenteModel);

			lByteArrayInput = new ByteArrayInputStream(lByteArrayOut.toByteArray());

			// Aggiornamento Documento Allegato.
			lDAModel.setDocBlobIn(lByteArrayInput);
			lDAModel.setDataAggiornamento(DateUtils.getSysDate());
			lDAModel.setCodUfficioAggiornamento(aCodUff);
			lDAModel.setCodOperatoreAggiornamento(aUtenteModel.getUserId());
			lDAModel.setFlagDocumentoRegistrato("N");

			lDADao = new DocumentoAllegatoDAO(lConn);
			lDADao.setDAOFromModelForUpdateBlob(lDAModel);

			// Selezione delle condizioni di Update.
			lDADao.setCondizioneUpdate(lDAModel.getIdDocumentoAllegato());
			lDADao.update();

			commit(lConn);
		} catch (Exception daoex) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Exception: " + daoex);
			throw new F3BException("DocumentoAllegatoController.ExStampaFoglioComp: " + daoex);
		} finally {
			cleanup(lDASqlDao);
			cleanup(lDADao);
			cleanup(lConn);
		}

		return lByteArrayOut;
	}

	public DocumentoAllegatoModel ExRicercaDocumentoAllegatoByKeyEvento(BigDecimal aIdEvento)
			throws F3BException {

		Connection lConn = null;
		DocumentoAllegatoSqlDAO llDocAllDao = null;
		DocumentoAllegatoModel lDocAllMod = null;

		try {
			lConn = getDBConnection();
			llDocAllDao = new DocumentoAllegatoSqlDAO(lConn);
			lDocAllMod = new DocumentoAllegatoModel();

			// Si settano le condizioni di ricerca
			lDocAllMod.setEveIdEvento(aIdEvento);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Ricerca allegato: " + lDocAllMod);
			// Ricerca
			llDocAllDao.ricercaDocumentoAllegato(lDocAllMod);
			// STUB: si presuppone che il documento sia unico
			lDocAllMod = (DocumentoAllegatoModel) llDocAllDao.getModelByKey();
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Ricerca allegato2: " + lDocAllMod);
		} catch (Exception sqe) {
			throw new SIUSException(
					"DocumentoAllegatoController.ExRicercaDocumentoAllegatoByKeyEvento: " + sqe);
		} finally {
			cleanup(llDocAllDao);
			cleanup(lConn);
		}
		return lDocAllMod;
	}

	/**
	 * Inserisce Foglio Complementare
	 *
	 * @param aDocumentoAllegato
	 * @return lDocAllRet
	 * @throws F3BException
	 */
	public DocumentoAllegatoModel ExInserisciFoglioComplementareNsc(DocumentoAllegatoModel aDocumentoAllegato,
			String aCodTipo) throws F3BException {

		Connection lConn = null;
		DocumentoAllegatoDAO lDocAllDao = null;
		DocumentoAllegatoSqlDAO lSqlDAO = null;
		DocumentoAllegatoModel lDocAllRet = new DocumentoAllegatoModel(aDocumentoAllegato);

		try {
			lConn = getDBTransaction();

			lDocAllDao = new DocumentoAllegatoDAO(lConn);
			lSqlDAO = new DocumentoAllegatoSqlDAO(lConn);
			BigDecimal lProgr = lSqlDAO.getProgressivoByUff(aDocumentoAllegato.getCodUfficioInserimento(),
					aDocumentoAllegato.getAnnoFoglioComplementare());
			aDocumentoAllegato.setProgrFoglioComplementare(new BigDecimal(lProgr.intValue() + 1));
			lDocAllDao.setDAOFromModel(aDocumentoAllegato);
			// Effettuo l'inserimento del Foglio Complementare NSC
			BigDecimal lKeyDocumentoAllegato = lDocAllDao.insert();
			lDocAllRet.setIdDocumentoAllegato(lKeyDocumentoAllegato);
			lDocAllRet.setProgrFoglioComplementare(aDocumentoAllegato.getProgrFoglioComplementare());
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.info("Inserito foglio complementare id = " + lKeyDocumentoAllegato);

			commit(lConn);
		} catch (Exception ex) {
			rollback(lConn);
			throw new F3BException("DocumentoAllegatoController.ExInserisciFoglioComplementare: " + ex);
		} finally {
			cleanup(lDocAllDao);
			cleanup(lSqlDAO);
			cleanup(lConn);
		}
		return lDocAllRet;
	}

	/**
	 * Metodo responsabile della modifica del foglio Complementare.
	 * <p>
	 *
	 * @param aDocumentoAllegato
	 *            dati del documento allaegato da modificare.
	 * @param aCodTipo
	 *            Codice tipo del provvedimento correlato.
	 * @throws F3BException
	 *             propaga errore di eccezione.
	 */
	public void ExModificaFoglioComplementareNsc(DocumentoAllegatoModel aDocumentoAllegato, String aCodTipo)
			throws F3BException {

		Connection lConn = null;
		DocumentoAllegatoDAO lDocAllDao = null;

		try {
			lConn = getDBTransaction();

			lDocAllDao = new DocumentoAllegatoDAO(lConn);
			lDocAllDao.setDataEmissione(aDocumentoAllegato.getDataEmissione());
			lDocAllDao.setDataTrasmissione(aDocumentoAllegato.getDataTrasmissione());
			lDocAllDao.setComuneSedeGiudiziaria(aDocumentoAllegato.getComuneSedeGiudiziaria());
			lDocAllDao.setDataAggiornamento(aDocumentoAllegato.getDataAggiornamento());
			// MERGE v10: modificato recupero valore ed aggiunte due impostazioni
			lDocAllDao.setCodOperatoreAggiornamento(aDocumentoAllegato.getCodOperatoreAggiornamento());
			lDocAllDao.setDataAnnullamento(aDocumentoAllegato.getDataAnnullamento());
			lDocAllDao.setMotivoAnnullamento(aDocumentoAllegato.getMotivoAnnullamento());
			lDocAllDao.setCodUfficioAggiornamento(aDocumentoAllegato.getCodUfficioAggiornamento());
			lDocAllDao.setCondizioneUpdate(aDocumentoAllegato.getIdDocumentoAllegato());
			lDocAllDao.setCodMotivazioneNonInvio(aDocumentoAllegato.getCodMotivazioneNonInvio());
			lDocAllDao.setDescrizioneNonInvio(aDocumentoAllegato.getDescrizioneNonInvio());
			lDocAllDao.setDataUltInvio(aDocumentoAllegato.getDataUltInvio());
			lDocAllDao.setDataInsMan(aDocumentoAllegato.getDataInsMan());

			lDocAllDao.update();

			commit(lConn);
		} catch (Exception ex) {
			rollback(lConn);
			throw new F3BException("DocumentoAllegatoController.ExModificaFoglioComplementare: " + ex);
		} finally {
			cleanup(lDocAllDao);
			cleanup(lConn);
		}
	}

	/**
	 * MERGE v10: aggiunto metodo di ricerca senza condizioni
	 */
	public DocumentoAllegatoModel ricDocAllByIdEvento(BigDecimal lIdEvento) throws F3BException {

		Connection lConn = null;
		DocumentoAllegatoSqlDAO llDocAllDao = null;
		DocumentoAllegatoModel lDocAllMod = null;

		try {
			lConn = getDBConnection();
			llDocAllDao = new DocumentoAllegatoSqlDAO(lConn);
			lDocAllMod = new DocumentoAllegatoModel();

			// Si settano le condizioni di ricerca
			lDocAllMod.setEveIdEvento(lIdEvento);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Ricerca allegato: " + lDocAllMod);
			// Ricerca
			llDocAllDao.ricDocAllByIdEvento(lDocAllMod);
			lDocAllMod = (DocumentoAllegatoModel) llDocAllDao.getModelByKey();
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Ricerca allegato2: " + lDocAllMod);
		} catch (Exception sqe) {
			throw new SIUSException("DocumentoAllegatoController.ricDocAllByIdEvento: " + sqe);
		} finally {
			cleanup(llDocAllDao);
			cleanup(lConn);
		}
		return lDocAllMod;
	}

	// MEV_AVVOCATURA: aggiungo un nuovo metodo ExUpdateDocument, con in input il lFlgAvvocatura
	// per inserire un avviso
	/**
	 * Effettua l'operazione di update di un documento mandato tramite upload ed inserisce un avviso quando
	 * trattasi di deposito ordinanza e deposito decreto
	 *
	 * @param aDocumentoAllegato
	 * @throws F3BException
	 */
	public DocumentoAllegatoModel ExUpdateDocument(DocumentoAllegatoModel aDocumentoAllegato,
			Vector<AvvisiAvvocatoModel> lAvvvisiAvvocato) throws F3BException {

		Connection lConn = null;
		DocumentoAllegatoDAO llDocAllDao = null;
		DocumentoAllegatoModel lDocAllMod = new DocumentoAllegatoModel(aDocumentoAllegato);
		AvvisiAvvocatoDAO lAvvisiAvvocatoDao = null;

		try {
			// Scheda Intervento n° 6 - Ottimizzazione SIUS Avvocati
			lConn = getDBTransaction();
			llDocAllDao = new DocumentoAllegatoDAO(lConn);
			lAvvisiAvvocatoDao = new AvvisiAvvocatoDAO(lConn);
			llDocAllDao.setDAOFromModelForUpdateBlob(aDocumentoAllegato);
			llDocAllDao.setCondizioneUpdate(aDocumentoAllegato.getIdDocumentoAllegato());
			llDocAllDao.update();

			// inserisco gli avvisi sulla tabella AVVISI_AVVOCATO
			if (lAvvvisiAvvocato != null && lAvvvisiAvvocato.size() > 0) {
				for (AvvisiAvvocatoModel avvisoAvvocato : lAvvvisiAvvocato) {
					// 20180111 [EC] : punto 2 nuovo PLO avvocatura (SIGI_PL_PO_2017 07 21-1.0-PLO MEV
					// SIUS_AVVOCATURA.DOC)
					if (!ICostantiAvvisiAvvocato.CONTENUTO_EMISSIONE_ORDINANZA
							.equals(avvisoAvvocato.getTestoAvviso())
							&& !ICostantiAvvisiAvvocato.CONTENUTO_EMISSIONE_DECRETO
									.equals(avvisoAvvocato.getTestoAvviso())
							// Ticket#20230201017 - Anomalia Sies: Ordinanza Rinvio Udienza va trattata come
							// Ordinanza classica '03'
							&& !ICostantiAvvisiAvvocato.CONTENUTO_ORDINANZA_RINVIO_UDIENZA
									.equals(avvisoAvvocato.getTestoAvviso())) {
						lAvvisiAvvocatoDao.setDAOFromModel(avvisoAvvocato);
						lAvvisiAvvocatoDao.insert();
						lAvvisiAvvocatoDao.stop();
					}
				}
			}

			commit(lConn);
		} catch (Exception sqe) {
			rollback(lConn);
			throw new F3BException("DocumentoAllegatoController.ExUpdateDocument: " + sqe);
		} finally {
			cleanup(llDocAllDao);
			// Scheda Intervento n° 6 - Ottimizzazione SIUS Avvocati
			cleanup(lAvvisiAvvocatoDao);
			cleanup(lConn);
		}
		return lDocAllMod;
	}

}