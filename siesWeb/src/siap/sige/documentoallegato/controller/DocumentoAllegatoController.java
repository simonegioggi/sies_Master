package siap.sige.documentoallegato.controller;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Vector;

import org.apache.log4j.Logger;

import siap.controller.SiapController;
import siap.sige.documentoallegato.dao.DocumentoAllegatoDAO;
import siap.sige.documentoallegato.dao.DocumentoAllegatoSqlDAO;
import siap.sige.documentoallegato.model.DocumentoAllegatoModel;
import siap.sius.SIUSException;
import siap.sius.depositodecreto.dao.DepositoDecretoDAO;
import f3b.dao.DAOException;
import f3b.log.LogF3B;
import f3b.util.F3BException;

public class DocumentoAllegatoController extends SiapController implements IDocumentoAllegato {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	@Override
	public DocumentoAllegatoModel ExRicercaFCByKeyEvento(BigDecimal idEvento) throws F3BException {
		Connection lConn = null;

		DocumentoAllegatoSqlDAO llDocAllDao = null;

		try {
			lConn = getDBConnection();
			llDocAllDao = new DocumentoAllegatoSqlDAO(lConn);

			llDocAllDao.ricercaDocumentoAllegatoFCByIdEvento(idEvento);
			DocumentoAllegatoModel lDocAll = (DocumentoAllegatoModel) llDocAllDao.getModelByKey();
			return lDocAll;
		} catch (Exception sqe) {
			throw new F3BException(
					"DocumentoAllegatoController.ExRicercaDocumentoAllegato: Non posso leggere  : " + sqe);
		} finally {
			cleanup(llDocAllDao);
			cleanup(lConn);
		}
	}

	@Override
	public DocumentoAllegatoModel ExRicercaFCById(BigDecimal idDoc) throws F3BException {
		Connection lConn = null;
		DocumentoAllegatoSqlDAO llDocAllDao = null;

		try {
			lConn = getDBConnection();
			llDocAllDao = new DocumentoAllegatoSqlDAO(lConn);

			llDocAllDao.ricercaDocumentoAllegatoById(idDoc);
			DocumentoAllegatoModel lDocAll = (DocumentoAllegatoModel) llDocAllDao.getModelByKey();
			return lDocAll;
		} catch (Exception sqe) {
			throw new F3BException(
					"DocumentoAllegatoController.ExRicercaDocumentoAllegato: Non posso leggere  : " + sqe);
		} finally {
			cleanup(llDocAllDao);
			cleanup(lConn);
		}
	}

	@Override
	public Vector<DocumentoAllegatoModel> ExRicercaFogliComplementariByIdEvento(BigDecimal idEvento)
			throws F3BException {

		Connection lConn = null;
		Vector<DocumentoAllegatoModel> fogli = new Vector<DocumentoAllegatoModel>();
		DocumentoAllegatoSqlDAO llDocAllDao = null;

		try {
			lConn = getDBConnection();

			llDocAllDao = new DocumentoAllegatoSqlDAO(lConn);
			llDocAllDao.ricercaDocumentoAllegatoFCByIdEvento(idEvento);
			llDocAllDao.start();
			while (llDocAllDao.next()) {
				DocumentoAllegatoModel foglio = (DocumentoAllegatoModel) llDocAllDao.getModel();
				fogli.add(foglio);
			}
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("DAOException: " + daoEx);
			throw new F3BException(
					"ProvvedimentoSigeController.ExRicercaProvvedimentoDaDepositare: " + daoEx);
		} catch (Exception e) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("SQLException: " + e);
			throw new F3BException("ProvvedimentoSigeController.ExRicercaProvvedimentoDaDepositare: " + e);
		} finally {
			cleanup(llDocAllDao);
			cleanup(lConn);
		}
		return fogli;
	}

	@Override
	public DocumentoAllegatoModel ExInserisciFoglioComplementare(DocumentoAllegatoModel aDocumentoAllegato)
			throws F3BException {
		Connection lConn = null;
		DocumentoAllegatoDAO lDocAllDao = null;
		DocumentoAllegatoSqlDAO lSqlDAO = null;
		DocumentoAllegatoModel lDocAllRet = new DocumentoAllegatoModel(aDocumentoAllegato);
		DepositoDecretoDAO lDecDao = null;

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
			commit(lConn);
		} catch (Exception ex) {
			rollback(lConn);
			throw new F3BException("DocumentoAllegatoController.ExInserisciFoglioComplementare: " + ex);
		} finally {
			cleanup(lDocAllDao);
			cleanup(lSqlDAO);
			cleanup(lDecDao); // sca
			cleanup(lConn);
		}
		return lDocAllRet;
	}

	@Override
	public DocumentoAllegatoModel ExRicercaFoglioComplementareByFascicolo(BigDecimal idFascicolo)
			throws F3BException {
		Connection lConn = null;
		DocumentoAllegatoSqlDAO llDocAllDao = null;

		try {
			lConn = getDBConnection();
			llDocAllDao = new DocumentoAllegatoSqlDAO(lConn);
			llDocAllDao.ricercaDocumentoAllegatoFCByIdFascicolo(idFascicolo);
			DocumentoAllegatoModel lDocAll = (DocumentoAllegatoModel) llDocAllDao.getModelByKey();
			return lDocAll;
		} catch (Exception sqe) {
			throw new F3BException(
					"DocumentoAllegatoController.ExRicercaDocumentoAllegato: Non posso leggere  : " + sqe);
		} finally {
			cleanup(llDocAllDao);
			cleanup(lConn);
		}
	}

	@Override
	public void ExEliminaSollecito(BigDecimal idDocumento) throws F3BException {
		Connection lConn = null;
		DocumentoAllegatoDAO llDocAllDao = null;

		try {
			lConn = getDBConnection();
			llDocAllDao = new DocumentoAllegatoDAO(lConn);
			llDocAllDao.setCondizioneDeleteSollecito(idDocumento);
			llDocAllDao.delete();
			lConn.commit();

		} catch (Exception sqe) {
			throw new F3BException(
					"DocumentoAllegatoController.ExRicercaDocumentoAllegato: Non posso leggere  : " + sqe);
		} finally {
			cleanup(llDocAllDao);
			cleanup(lConn);
		}

	}

	@Override
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

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public Vector<DocumentoAllegatoModel> ExRicercaSollecitoByIdEvento(BigDecimal aKey) throws F3BException {
		Connection lConn = null;
		DocumentoAllegatoSqlDAO llDocAllDao = null;
		Vector<DocumentoAllegatoModel> lDocs = null;

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

	@Override
	public BigDecimal countDecretiDepositoFissazioneUdienzaNonValidati(BigDecimal idFascicolo)
			throws F3BException {
		Connection lConn = null;
		DocumentoAllegatoSqlDAO llDocAllDao = null;

		try {
			lConn = getDBConnection();
			llDocAllDao = new DocumentoAllegatoSqlDAO(lConn);
			BigDecimal nonValidati = llDocAllDao
					.countDecretiDepositoFissazioneUdienzaNonValidati(idFascicolo);
			return nonValidati;
		} catch (Exception sqe) {
			throw new F3BException(
					"DocumentoAllegatoController.countDecretiDepositoFissazioneUdienzaNonValidati: Non posso leggere  : "
							+ sqe);
		} finally {
			cleanup(llDocAllDao);
			cleanup(lConn);
		}
	}

	@Override
	public BigDecimal countDecretiDepositoFissazioneUdienzaValidati(BigDecimal idFascicolo)
			throws F3BException {
		Connection lConn = null;
		DocumentoAllegatoSqlDAO llDocAllDao = null;

		try {
			lConn = getDBConnection();
			llDocAllDao = new DocumentoAllegatoSqlDAO(lConn);
			BigDecimal nonValidati = llDocAllDao.countDecretiDepositoFissazioneUdienzaValidati(idFascicolo);
			return nonValidati;
		} catch (Exception sqe) {
			throw new F3BException(
					"DocumentoAllegatoController.countDecretiDepositoFissazioneUdienzaNonValidati: Non posso leggere  : "
							+ sqe);
		} finally {
			cleanup(llDocAllDao);
			cleanup(lConn);
		}
	}

}