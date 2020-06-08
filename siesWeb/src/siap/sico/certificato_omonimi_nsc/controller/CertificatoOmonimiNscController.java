package siap.sico.certificato_omonimi_nsc.controller;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Vector;

import f3b.dao.DAOException;
import f3b.util.F3BException;
import siap.controller.SiapController;
import siap.sico.certificato_omonimi_nsc.dao.CertificatoOmonimiNscDAO;
import siap.sico.certificato_omonimi_nsc.dao.CertificatoOmonimiNscSqlDAO;
import siap.sico.certificato_omonimi_nsc.model.CertificatoOmonimiNscModel;

/**
 * <p>
 * Title: CertificatoOmonimiNscController
 * </p>
 * <p>
 * Description: Classe Controller per CertificatoOmonimiNsc
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
public class CertificatoOmonimiNscController extends SiapController implements ICertificatoOmonimiNsc {

	/*****************************************************************************
	 * Effettua l'inserimento di un CertificatoOmonimiNsc a partire dai dati contenuti nel Model
	 *
	 * @param aCertificatoOmonimiNsc
	 *            Model con i dati da inserire
	 * @return il model con i dati inseriti e l'aggiunta dell'id del record inserito
	 * @throws F3BException
	 ****************************************************************************/
	public CertificatoOmonimiNscModel ExInserisciCertificatoOmonimiNsc(
			CertificatoOmonimiNscModel aCertificatoOmonimiNsc) throws F3BException {

		Connection lConn = null;
		CertificatoOmonimiNscDAO lCerDao = null;
		CertificatoOmonimiNscModel lCerMod = null;

		try {
			lConn = getDBConnection();
			lCerDao = new CertificatoOmonimiNscDAO(lConn);
			lCerDao.setDAOFromModel(aCertificatoOmonimiNsc);
			BigDecimal lSequence = lCerDao.insert();
			commit(lConn);
			lCerMod = new CertificatoOmonimiNscModel(aCertificatoOmonimiNsc);
			lCerMod.setMessage("Inserimento avvenuto correttamente!");
			lCerMod.setIdCertificatoOmonimi(lSequence);
		} catch (DAOException ex) {
			rollback(lConn);
			throw new F3BException("CertificatoOmonimiNscController.ExInserisci: Non posso inserire: " + ex);
		} finally {
			cleanup(lCerDao);
			cleanup(lConn);
		}

		return lCerMod;
	}

	/*****************************************************************************
	 * Effettua la ricerca dei dati CertificatoOmonimiNsc
	 *
	 * @param aCertificatoOmonimiNsc
	 *            Model utilizzato per costruire le condizioni di ricerca Ogni valore attualizzato nel model
	 *            verrà utilizzato per imporre una condizione di ricerca
	 * @return un vettore di model con il risultato della ricerca
	 * @throws F3BException
	 ****************************************************************************/
	public Vector ExRicercaCertificatoOmonimiNsc(CertificatoOmonimiNscModel aCertificatoOmonimiNsc)
			throws F3BException {

		Connection lConn = null;
		Vector lCertificatoOmonimiNsi = new Vector();
		CertificatoOmonimiNscDAO lCerDao = null;

		try {
			lConn = getDBConnection();
			lCerDao = new CertificatoOmonimiNscDAO(lConn);
			lCerDao.setCondizioni(aCertificatoOmonimiNsc);
			lCerDao.setOrderBy();
			lCerDao.start();
			while (lCerDao.next()) {
				lCertificatoOmonimiNsi.add(lCerDao.getModel());
			}
			lCerDao.stop();
		} catch (DAOException daoEx) {
			throw new F3BException(
					"CertificatoOmonimiNscController.ExRicercaCertificatoOmonimiNsc: Non posso leggere : "
							+ daoEx);
		} finally {
			cleanup(lCerDao);
			cleanup(lConn);
		}

		return lCertificatoOmonimiNsi;
	}

	/*****************************************************************************
	 * Effettua la ricerca per chiave
	 *
	 * @param akey
	 *            valore della chiave del record da ricercare
	 * @return il model con i dati trovati
	 * @throws F3BException
	 ****************************************************************************/
	public CertificatoOmonimiNscModel ExRicercaCertificatoOmonimiNscById(BigDecimal aIdCertificatoOmonimi)
			throws F3BException {

		Connection lConn = null;
		CertificatoOmonimiNscModel lCertificatoOmonimiNscMod = new CertificatoOmonimiNscModel();
		CertificatoOmonimiNscSqlDAO lCertificatoOmonimiNscSqlDao = null;

		try {
			lConn = getDBConnection();
			lCertificatoOmonimiNscSqlDao = new CertificatoOmonimiNscSqlDAO(lConn);
			lCertificatoOmonimiNscSqlDao.ricercaCertificatoOmonimiNscByKey(aIdCertificatoOmonimi);
			lCertificatoOmonimiNscMod = (CertificatoOmonimiNscModel) lCertificatoOmonimiNscSqlDao
					.getModelByKey();
		} catch (DAOException daoEx) {
			throw new F3BException(
					"CertificatoOmonimiNscController.ExRicercaCertificatoOmonimiNscById: Non posso leggere : "
							+ daoEx);
		} finally {
			cleanup(lCertificatoOmonimiNscSqlDao);
			cleanup(lConn);
		}

		return lCertificatoOmonimiNscMod;
	}

	/*****************************************************************************
	 * Metodo che modifica i dati dell'CertificatoOmonimiNsc Viene fatto l'update di tutti i campi del record
	 * recuperando i valori dal Model Se mancano dati nel model i corrispondenti valori della tabella verranno
	 * impostati a null
	 *
	 * @param aCertificatoOmonimiNsc
	 *            Model con i nuovi valori
	 * @throws F3BException
	 ****************************************************************************/
	public void ExModificaCertificatoOmonimiNsc(CertificatoOmonimiNscModel aCertificatoOmonimiNsc)
			throws F3BException {

		Connection lConn = null;
		CertificatoOmonimiNscDAO lCerDao = null;

		try {
			lConn = getDBConnection();
			lCerDao = new CertificatoOmonimiNscDAO(lConn);
			lCerDao.setDAOFromModelForUpdateBlob(aCertificatoOmonimiNsc);
			lCerDao.selCondizioneUpdate(aCertificatoOmonimiNsc.getIdCertificatoOmonimi());
			lCerDao.update();
			commit(lConn);
		} catch (DAOException ex) {
			rollback(lConn);
			throw new F3BException("CertificatoOmonimiNscController.ExModifica: Non posso inserire: " + ex);
		} finally {
			cleanup(lCerDao);
			cleanup(lConn);
		}
	}

	/*****************************************************************************
	 * Effettua la cancellazione del record
	 *
	 * @param aCertificatoOmonimiNsc
	 * @throws F3BException
	 ****************************************************************************/
	public void ExCancellaCertificatoOmonimiNsc(CertificatoOmonimiNscModel aCertificatoOmonimiNsc)
			throws F3BException {

		Connection lConn = null;
		CertificatoOmonimiNscDAO lCerDao = null;

		try {
			lConn = getDBConnection();
			lCerDao = new CertificatoOmonimiNscDAO(lConn);
			lCerDao.selCondizioneUpdate(aCertificatoOmonimiNsc.getIdCertificatoOmonimi());
			lCerDao.delete();
			commit(lConn);
		} catch (DAOException daoEx) {
			rollback(lConn);
			throw new F3BException(
					"CertificatoOmonimiNscController.ExCancellaCertificatoOmonimiNsc: Non posso leggere : "
							+ daoEx);
		} finally {
			cleanup(lCerDao);
			cleanup(lConn);
		}
	}

	/*****************************************************************************
	 * Effettua la cancellazione dei record precedenti la data di sistema
	 *
	 * @param aCertificatoOmonimiNsc
	 * @throws F3BException
	 ****************************************************************************/
	public void ExCancellaCertificatoOmonimiNscByDate() throws F3BException {

		Connection lConn = null;
		CertificatoOmonimiNscDAO lCerDao = null;

		try {
			lConn = getDBConnection();
			lCerDao = new CertificatoOmonimiNscDAO(lConn);
			lCerDao.selCondizioneDelete();
			lCerDao.delete();
			commit(lConn);
		} catch (DAOException daoEx) {
			rollback(lConn);
			throw new F3BException(
					"CertificatoOmonimiNscController.ExCancellaCertificatoOmonimiNsc: Non posso leggere : "
							+ daoEx);
		} finally {
			cleanup(lCerDao);
			cleanup(lConn);
		}
	}

	/*****************************************************************************
	 * Recupera il numero di record restituiti della ricerca. Utile in caso di ricerche paginate per ottenere
	 * il numero totale di record
	 *
	 * @param aCertificatoOmonimiNsc
	 * @return numero di record trovati dalla funzione dei ricerca
	 * @throws F3BException
	 ****************************************************************************/
	public BigDecimal ExGetCountCertificatoOmonimiNsc(CertificatoOmonimiNscModel aCertificatoOmonimiNsc)
			throws F3BException {

		Connection lConn = null;
		BigDecimal lCount = new BigDecimal(0);
		CertificatoOmonimiNscSqlDAO lCertificatoOmonimiNscSqlDao = null;

		try {
			lConn = getDBConnection();
			lCertificatoOmonimiNscSqlDao = new CertificatoOmonimiNscSqlDAO(lConn);
			lCertificatoOmonimiNscSqlDao.getCountCertificatoOmonimiNsc(aCertificatoOmonimiNsc);
			lCertificatoOmonimiNscSqlDao.start();
			lCertificatoOmonimiNscSqlDao.next();
			lCount = lCertificatoOmonimiNscSqlDao.getBigDecimal("HowManyRecords");
			lCertificatoOmonimiNscSqlDao.stop();
		} catch (DAOException daoEx) {
			throw new F3BException(
					"CertificatoOmonimiNscController.ExGetCountCertificatoOmonimiNsc: Non posso leggere : "
							+ daoEx);
		} finally {
			cleanup(lCertificatoOmonimiNscSqlDao);
			cleanup(lConn);
		}

		return lCount;
	}

	/*****************************************************************************
	 * Funzione di ricerca utilizzata per la paginazione che restituisce i risultati da visualizzare nella
	 * pagina specificata in input
	 *
	 * @param aCertificatoOmonimiNsc
	 *            model contenete i parametri della ricerca
	 * @param aPage
	 *            pagina per la quale si vogliono ottenere i risultati
	 * @return Vettore di model con i record da visualizzare nella pagina specificata in input
	 * @throws F3BException
	 ****************************************************************************/
	public Vector ExRicercaCertificatoOmonimiNscPaged(CertificatoOmonimiNscModel aCertificatoOmonimiNsc,
			int aPage) throws F3BException {

		Connection lConn = null;
		Vector lCertificatoOmonimiNsi = new Vector();
		CertificatoOmonimiNscSqlDAO lCertificatoOmonimiNscSqlDao = null;

		try {
			lConn = getDBConnection();
			lCertificatoOmonimiNscSqlDao = new CertificatoOmonimiNscSqlDAO(lConn);
			lCertificatoOmonimiNscSqlDao.ricercaCertificatoOmonimiNscPaged(aCertificatoOmonimiNsc, aPage);
			lCertificatoOmonimiNsi = new Vector(lCertificatoOmonimiNscSqlDao.getModels());
		} catch (DAOException daoEx) {
			throw new F3BException(
					"CertificatoOmonimiNscController.ExRicercaCertificatoOmonimiNscPaged: Non posso leggere : "
							+ daoEx);
		} finally {
			cleanup(lCertificatoOmonimiNscSqlDao);
			cleanup(lConn);
		}
		return lCertificatoOmonimiNsi;
	}

	/**
	 * Seleziona un singolo documento rtf sul DB e lo restituisce come ByteArrayOutputStream
	 *
	 * @param aProvvedimento
	 * @return Array con il Documento recuperato dal DB
	 * @throws F3BException
	 */
	public ByteArrayOutputStream ExGetDocumento(CertificatoOmonimiNscModel aCertificato) throws F3BException {

		Connection lConn = null;
		CertificatoOmonimiNscDAO lCerDao = null;
		ByteArrayOutputStream lByteArrayOut = null;
		// ByteArrayInputStream lByteArrayIn = null;
		try {
			lConn = getDBConnection();
			lCerDao = new CertificatoOmonimiNscDAO(lConn);

			lCerDao.setIdCertificatoOmonimi(aCertificato.getIdCertificatoOmonimi());
			lCerDao.selByKey();

			lCerDao.start(1);

			if (lCerDao.next())
				lByteArrayOut = lCerDao.getDocBlobCertificato();

			lCerDao.stop();

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
			cleanup(lCerDao);
			cleanup(lConn);
		}
		return lByteArrayOut;
	}

}