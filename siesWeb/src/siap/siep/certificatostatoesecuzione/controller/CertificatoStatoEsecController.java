package siap.siep.certificatostatoesecuzione.controller;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
//import java.sql.SQLException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.controller.GenericController;
import f3b.dao.DAOException;
import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.util.report.ReportGenerator;
import f3b.util.xml.TreeModel;
import siap.sico.template.controller.TemplateManager;
import siap.sico.utente.model.UtenteModel;
import siap.siep.certificatostatoesecuzione.dao.CertificatoStatoEsecDAO;
import siap.siep.certificatostatoesecuzione.dao.CertificatoStatoEsecSqlDAO;
import siap.siep.certificatostatoesecuzione.model.CertificatoStatoEsecModel;
import siap.siep.fascicolo.controller.IFascicoloSiepStampa;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.util.SIEPLookupRemote;

/**
 * <p>
 * Title: CertificatoStatoEsecController
 * </p>
 * <p>
 * Description: Classe Controller per CertificatoStatoEsec
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
public class CertificatoStatoEsecController extends GenericController implements ICertificatoStatoEsec {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	/*****************************************************************************
	 * Effettua l'inserimento di un CertificatoStatoEsec a partire dai dati contenuti nel Model
	 *
	 * @param aCertificatoStatoEsec
	 *            Model con i dati da inserire
	 * @return il model con i dati inseriti e l'aggiunta dell'id del record inserito
	 * @throws F3BException
	 ****************************************************************************/
	public CertificatoStatoEsecModel ExInserisciCertificatoStatoEsec(
			CertificatoStatoEsecModel aCertificatoStatoEsec) throws F3BException {

		Connection lConn = null;
		CertificatoStatoEsecDAO lCerDao = null;
		CertificatoStatoEsecModel lCerMod = null;

		try {
			lConn = getDBConnection();
			lCerDao = new CertificatoStatoEsecDAO(lConn);
			lCerDao.setDAOFromModel(aCertificatoStatoEsec);
			BigDecimal lSequence = lCerDao.insert();
			commit(lConn);
			lCerMod = new CertificatoStatoEsecModel(aCertificatoStatoEsec);
			lCerMod.setMessage("Inserimento avvenuto correttamente!");
			lCerMod.setIdCertificatoStatoEsec(lSequence);
		} catch (DAOException ex) {
			rollback(lConn);
			throw new F3BException("CertificatoStatoEsecController.ExInserisci: Non posso inserire: " + ex);
		} finally {
			cleanup(lCerDao);
			cleanup(lConn);
		}

		return lCerMod;
	}

	/*****************************************************************************
	 * Effettua la ricerca dei dati CertificatoStatoEsec
	 *
	 * @param aCertificatoStatoEsec
	 *            Model utilizzato per costruire le condizioni di ricerca Ogni valore attualizzato nel model
	 *            verrà utilizzato per imporre una condizione di ricerca
	 * @return un vettore di model con il risultato della ricerca
	 * @throws F3BException
	 ****************************************************************************/
	public Vector ExRicercaCertificatoStatoEsec(CertificatoStatoEsecModel aCertificatoStatoEsec)
			throws F3BException {

		Connection lConn = null;
		Vector lCertificatoStatoEsei = new Vector();
		CertificatoStatoEsecDAO lCerDao = null;

		try {
			lConn = getDBConnection();
			lCerDao = new CertificatoStatoEsecDAO(lConn);
			lCerDao.setCondizioni(aCertificatoStatoEsec);
			lCerDao.setOrderBy();
			lCerDao.start();
			while (lCerDao.next()) {
				lCertificatoStatoEsei.add(lCerDao.getModel());
			}
			lCerDao.stop();
		} catch (DAOException daoEx) {
			throw new F3BException(
					"CertificatoStatoEsecController.ExRicercaCertificatoStatoEsec: Non posso leggere : "
							+ daoEx);
		} finally {
			cleanup(lCerDao);
			cleanup(lConn);
		}

		return lCertificatoStatoEsei;
	}

	/*****************************************************************************
	 * Effettua la ricerca per chiave
	 *
	 * @param akey
	 *            valore della chiave del record da ricercare
	 * @return il model con i dati trovati
	 * @throws F3BException
	 ****************************************************************************/
	public CertificatoStatoEsecModel ExRicercaCertificatoStatoEsecById(BigDecimal aIdCertificatoStatoEsec)
			throws F3BException {

		Connection lConn = null;
		CertificatoStatoEsecModel lCertificatoStatoEsecMod = new CertificatoStatoEsecModel();
		CertificatoStatoEsecSqlDAO lCertificatoStatoEsecSqlDao = null;

		try {
			lConn = getDBConnection();
			lCertificatoStatoEsecSqlDao = new CertificatoStatoEsecSqlDAO(lConn);
			lCertificatoStatoEsecSqlDao.ricercaCertificatoStatoEsecByKey(aIdCertificatoStatoEsec);
			lCertificatoStatoEsecMod = (CertificatoStatoEsecModel) lCertificatoStatoEsecSqlDao
					.getModelByKey();
		} catch (DAOException daoEx) {
			throw new F3BException(
					"CertificatoStatoEsecController.ExRicercaCertificatoStatoEsecById: Non posso leggere : "
							+ daoEx);
		} finally {
			cleanup(lCertificatoStatoEsecSqlDao);
			cleanup(lConn);
		}

		return lCertificatoStatoEsecMod;
	}

	/*****************************************************************************
	 * Effettua la cancellazione del record
	 *
	 * @param aCertificatoStatoEsec
	 * @throws F3BException
	 ****************************************************************************/
	public void ExCancellaCertificatoStatoEsec(CertificatoStatoEsecModel aCertificatoStatoEsec)
			throws F3BException {

		Connection lConn = null;
		CertificatoStatoEsecDAO lCerDao = null;

		try {
			lConn = getDBConnection();
			lCerDao = new CertificatoStatoEsecDAO(lConn);
			lCerDao.selCondizioneUpdate(aCertificatoStatoEsec.getIdCertificatoStatoEsec());
			lCerDao.delete();
			commit(lConn);
		} catch (DAOException daoEx) {
			rollback(lConn);
			throw new F3BException(
					"CertificatoStatoEsecController.ExCancellaCertificatoStatoEsec: Non posso leggere : "
							+ daoEx);
		} finally {
			cleanup(lCerDao);
			cleanup(lConn);
		}
	}

	/**
	 * ExRicercaCertificatoStatoEsecByIdFascicoloSiep
	 *
	 * @param BigDecimal
	 *            aIdFascicolo
	 * @return Vector - Stato Esecuzione trovato per il fascicolosiep
	 */
	public Vector ExRicercaCertificatoStatoEsecByIdFascicoloSiep(BigDecimal aIdFascicolo)
			throws F3BException {

		Connection lConn = null;
		Vector lCertEsec = new Vector();
		CertificatoStatoEsecSqlDAO lCertEsecDao = null;

		try {
			lConn = getDBConnection();
			lCertEsecDao = new CertificatoStatoEsecSqlDAO(lConn);
			lCertEsecDao.ricercaCertificatoStatoEsecByIdFascicolo(aIdFascicolo);
			lCertEsec = new Vector(lCertEsecDao.getModels());
			if (lCertEsec.size() == 0) {
				throw new F3BException(F3BException.USER_MESSAGE, "Nessun Elemento trovato");
			}
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException(
					"CertificatoStatoEsec.ExRicercaCertificatoStatoEsecByIdFascicoloSiep: Non posso leggere : "
							+ daoEx);
		} finally {
			cleanup(lCertEsecDao);
			cleanup(lConn);
		}
		return lCertEsec;
	}

	/**
	 * Seleziona un singolo documento rtf sul DB e lo restituisce come ByteArrayOutputStream
	 *
	 * @param aCertificatoStatoEsecModel
	 * @return Array con il Documento recuperato dal DB
	 * @throws F3BException
	 */
	public ByteArrayOutputStream ExGetDocumento(CertificatoStatoEsecModel aCertificatoStatoEsec)
			throws F3BException {

		Connection lConn = null;
		CertificatoStatoEsecDAO lCertSE = null;
		ByteArrayOutputStream lByteArrayOut = null;
		try {
			lConn = getDBConnection();
			lCertSE = new CertificatoStatoEsecDAO(lConn);

			lCertSE.setIdCertificatoStatoEsec(aCertificatoStatoEsec.getIdCertificatoStatoEsec());
			lCertSE.selByKey();

			lCertSE.start(1);

			if (lCertSE.next())
				lByteArrayOut = lCertSE.getDocBlob();

			lCertSE.stop();

			if (lByteArrayOut == null)
				throw new F3BException(F3BException.USER_MESSAGE, "Nessun Documento Associato");

			if (lByteArrayOut.size() == 0)
				throw new F3BException(F3BException.USER_MESSAGE, "Nessun Documento Associato");
		} catch (F3BException eF3b) {
			throw eF3b;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			cleanup(lCertSE);
			cleanup(lConn);
		}
		return lByteArrayOut;
	}

	/**
	 * stampa e salva il Certificato Stato Esecuzione
	 *
	 * @param aFasc
	 * @param lIdTemplate
	 * @param aUtente
	 * @return file .doc
	 * @throws F3BException
	 */
	public ByteArrayOutputStream ExStampaDocumento(FascicoloSiepModel aFasc, String lIdTemplate,
			UtenteModel aUtente) throws F3BException {

		ByteArrayOutputStream lByteArrayOut = null;
		Connection lConn = null;
		CertificatoStatoEsecDAO lCertSEDao = null;

		try {

			IFascicoloSiepStampa lCtrStam = SIEPLookupRemote.getFascicoloSiepStampaRemote();
			TreeModel lTree = lCtrStam.prelevaDatiStampaFascicolo(aFasc, aUtente);

			String lNomeTemplate = TemplateManager.getInstance().getTemplateName(lIdTemplate);

			ReportGenerator lReport = new ReportGenerator();

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.info("NOME TEMPLATE >>>" + lNomeTemplate);
			// prendo il certificato da inserire nella tabella
			lByteArrayOut = (ByteArrayOutputStream) lReport.generateDocument(lTree, lNomeTemplate);

			// attualizzo il Certificato
			ByteArrayInputStream lByteArrayInput = new ByteArrayInputStream(lByteArrayOut.toByteArray());
			CertificatoStatoEsecModel aCertSE = new CertificatoStatoEsecModel();
			aCertSE.setFlagUpload("0");
			aCertSE.setCodOperatoreInserimento(aUtente.getUserId());
			aCertSE.setCodUfficioInserimento(aUtente.getUfficioUtente().getCodUfficio());
			aCertSE.setFasSieIdFascicoloSiep(aFasc.getIdFascicoloSiep());
			aCertSE.setDataInserimento(DateUtils.getSysDate());
			aCertSE.setDocBlobIn(lByteArrayInput);

			// inserisco il record nella tabella
			lConn = getDBConnection();
			lCertSEDao = new CertificatoStatoEsecDAO(lConn);

			lCertSEDao.setDAOFromModel(aCertSE);
			/* BigDecimal lSequence = */lCertSEDao.insert();
			commit(lConn);
		} catch (DAOException ex) {
			rollback(lConn);
			throw new F3BException(
					"CertificatoStatoEsecController.ExStampaDocumento: Non posso inserire: " + ex);
		} finally {
			cleanup(lCertSEDao);
			cleanup(lConn);
		}
		return lByteArrayOut;
	}

	/**
	 * stampa e salva il Certificato Stato Esecuzione
	 *
	 * @param aFasc
	 * @param lIdTemplate
	 * @param aUtente
	 * @return file .doc
	 * @throws F3BException
	 */
	public ByteArrayOutputStream ExStampaDocumentoPDF(FascicoloSiepModel aFasc, String lIdTemplate,
			UtenteModel aUtente) throws F3BException {

		ByteArrayOutputStream lByteArrayOut = null;

		IFascicoloSiepStampa lCtrStam = SIEPLookupRemote.getFascicoloSiepStampaRemote();
		TreeModel lTree = lCtrStam.prelevaDatiStampaFascicolo(aFasc, aUtente);

		String lNomeTemplate = TemplateManager.getInstance().getTemplateName(lIdTemplate);

		ReportGenerator lReport = new ReportGenerator();
		// indico al Report Generator che deve essere prodotto un PDF
		lReport.setPdf();

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
		siesLogger.info("NOME TEMPLATE >>>" + lNomeTemplate);
		// prendo il certificato da inserire nella tabella
		lByteArrayOut = (ByteArrayOutputStream) lReport.generateDocument(lTree, lNomeTemplate);

		return lByteArrayOut;
	}

}