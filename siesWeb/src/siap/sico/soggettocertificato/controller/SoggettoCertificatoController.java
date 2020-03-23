package siap.sico.soggettocertificato.controller;

import java.io.ByteArrayOutputStream;
//import java.sql.SQLException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Vector;

import f3b.dao.DAOException;
import f3b.util.F3BException;
import siap.controller.SiapController;
import siap.sico.soggettocertificato.dao.SoggettoCertificatoDAO;
import siap.sico.soggettocertificato.dao.SoggettoCertificatoSqlDAO;
import siap.sico.soggettocertificato.model.SoggettoCertificatoModel;

/**
 * <p>
 * Title: SoggettoCertificatoController
 * </p>
 * <p>
 * Description: Classe Controller per SoggettoCertificato
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
public class SoggettoCertificatoController extends SiapController implements ISoggettoCertificato {

	/*****************************************************************************
	 * Effettua l'inserimento di un SoggettoCertificato a partire dai dati contenuti nel Model
	 *
	 * @param aSoggettoCertificato
	 *            Model con i dati da inserire
	 * @return il model con i dati inseriti e l'aggiunta dell'id del record inserito
	 * @throws F3BException
	 ****************************************************************************/
	public SoggettoCertificatoModel ExInserisciSoggettoCertificato(
			SoggettoCertificatoModel aSoggettoCertificato) throws F3BException {

		Connection lConn = null;
		SoggettoCertificatoDAO lSogDao = null;
		SoggettoCertificatoModel lSogMod = null;

		try {
			lConn = getDBConnection();
			lSogDao = new SoggettoCertificatoDAO(lConn);
			lSogDao.setDAOFromModel(aSoggettoCertificato);
			BigDecimal lSequence = lSogDao.insert();
			commit(lConn);
			lSogMod = new SoggettoCertificatoModel(aSoggettoCertificato);
			lSogMod.setMessage("Inserimento avvenuto correttamente!");
			lSogMod.setIdSoggettoCertificato(lSequence);
		} catch (DAOException ex) {
			rollback(lConn);
			throw new F3BException("SoggettoCertificatoController.ExInserisci: Non posso inserire: " + ex);
		} finally {
			cleanup(lSogDao);
			cleanup(lConn);
		}

		return lSogMod;
	}

	/*****************************************************************************
	 * Effettua la ricerca dei dati SoggettoCertificato
	 *
	 * @param aSoggettoCertificato
	 *            Model utilizzato per costruire le condizioni di ricerca Ogni valore attualizzato nel model
	 *            verrà utilizzato per imporre una condizione di ricerca
	 * @return un vettore di model con il risultato della ricerca
	 * @throws F3BException
	 ****************************************************************************/
	public Vector ExRicercaSoggettoCertificato(SoggettoCertificatoModel aSoggettoCertificato)
			throws F3BException {

		Connection lConn = null;
		Vector lSoggettoCertificati = new Vector();
		SoggettoCertificatoDAO lSogDao = null;

		try {
			lConn = getDBConnection();
			lSogDao = new SoggettoCertificatoDAO(lConn);
			lSogDao.setCondizioni(aSoggettoCertificato);
			lSogDao.setOrderBy();
			lSogDao.start();
			while (lSogDao.next()) {
				lSoggettoCertificati.add(lSogDao.getModel());
			}
			lSogDao.stop();
		} catch (DAOException daoEx) {
			throw new F3BException(
					"SoggettoCertificatoController.ExRicercaSoggettoCertificato: Non posso leggere : "
							+ daoEx);
		} finally {
			cleanup(lSogDao);
			cleanup(lConn);
		}

		return lSoggettoCertificati;
	}

	/*****************************************************************************
	 * Effettua la ricerca per chiave
	 *
	 * @param akey
	 *            valore della chiave del record da ricercare
	 * @return il model con i dati trovati
	 * @throws F3BException
	 ****************************************************************************/
	public SoggettoCertificatoModel ExRicercaSoggettoCertificatoById(BigDecimal aIdSoggettoCertificato)
			throws F3BException {

		Connection lConn = null;
		SoggettoCertificatoModel lSoggettoCertificatoMod = new SoggettoCertificatoModel();
		SoggettoCertificatoSqlDAO lSoggettoCertificatoSqlDao = null;

		try {
			lConn = getDBConnection();
			lSoggettoCertificatoSqlDao = new SoggettoCertificatoSqlDAO(lConn);
			lSoggettoCertificatoSqlDao.ricercaSoggettoCertificatoByKey(aIdSoggettoCertificato);
			lSoggettoCertificatoMod = (SoggettoCertificatoModel) lSoggettoCertificatoSqlDao.getModelByKey();
		} catch (DAOException daoEx) {
			throw new F3BException(
					"SoggettoCertificatoController.ExRicercaSoggettoCertificatoById: Non posso leggere : "
							+ daoEx);
		} finally {
			cleanup(lSoggettoCertificatoSqlDao);
			cleanup(lConn);
		}

		return lSoggettoCertificatoMod;
	}

	/*****************************************************************************
	 * Metodo che modifica i dati dell'SoggettoCertificato Viene fatto l'update di tutti i campi del record
	 * recuperando i valori dal Model Se mancano dati nel model i corrispondenti valori della tabella verranno
	 * impostati a null
	 *
	 * @param aSoggettoCertificato
	 *            Model con i nuovi valori
	 * @throws F3BException
	 ****************************************************************************/
	public void ExModificaSoggettoCertificato(SoggettoCertificatoModel aSoggettoCertificato)
			throws F3BException {

		Connection lConn = null;
		SoggettoCertificatoDAO lSogDao = null;

		try {
			lConn = getDBConnection();
			lSogDao = new SoggettoCertificatoDAO(lConn);
			lSogDao.setDAOFromModel(aSoggettoCertificato);
			lSogDao.selCondizioneUpdate(aSoggettoCertificato.getIdSoggettoCertificato());
			lSogDao.update();
			commit(lConn);
		} catch (DAOException ex) {
			rollback(lConn);
			throw new F3BException("SoggettoCertificatoController.ExModifica: Non posso inserire: " + ex);
		} finally {
			cleanup(lSogDao);
			cleanup(lConn);
		}
	}

	/*****************************************************************************
	 * Effettua la cancellazione del record
	 *
	 * @param aSoggettoCertificato
	 * @throws F3BException
	 ****************************************************************************/
	public void ExCancellaSoggettoCertificato(SoggettoCertificatoModel aSoggettoCertificato)
			throws F3BException {

		Connection lConn = null;
		SoggettoCertificatoDAO lSogDao = null;

		try {
			lConn = getDBConnection();
			lSogDao = new SoggettoCertificatoDAO(lConn);
			lSogDao.selCondizioneUpdate(aSoggettoCertificato.getIdSoggettoCertificato());
			lSogDao.delete();
			commit(lConn);
		} catch (DAOException daoEx) {
			rollback(lConn);
			throw new F3BException(
					"SoggettoCertificatoController.ExCancellaSoggettoCertificato: Non posso leggere : "
							+ daoEx);
		} finally {
			cleanup(lSogDao);
			cleanup(lConn);
		}
	}

	/*****************************************************************************
	 * Recupera il numero di record restituiti della ricerca. Utile in caso di ricerche paginate per ottenere
	 * il numero totale di record
	 *
	 * @param aSoggettoCertificato
	 * @return numero di record trovati dalla funzione dei ricerca
	 * @throws F3BException
	 ****************************************************************************/
	public BigDecimal ExGetCountSoggettoCertificato(SoggettoCertificatoModel aSoggettoCertificato)
			throws F3BException {

		Connection lConn = null;
		BigDecimal lCount = new BigDecimal(0);
		SoggettoCertificatoSqlDAO lSoggettoCertificatoSqlDao = null;

		try {
			lConn = getDBConnection();
			lSoggettoCertificatoSqlDao = new SoggettoCertificatoSqlDAO(lConn);
			lSoggettoCertificatoSqlDao.getCountSoggettoCertificato(aSoggettoCertificato);
			lSoggettoCertificatoSqlDao.start();
			lSoggettoCertificatoSqlDao.next();
			lCount = lSoggettoCertificatoSqlDao.getBigDecimal("HowManyRecords");
			lSoggettoCertificatoSqlDao.stop();
		} catch (DAOException daoEx) {
			throw new F3BException(
					"SoggettoCertificatoController.ExGetCountSoggettoCertificato: Non posso leggere : "
							+ daoEx);
		} finally {
			cleanup(lSoggettoCertificatoSqlDao);
			cleanup(lConn);
		}

		return lCount;
	}

	/*****************************************************************************
	 * Funzione di ricerca utilizzata per la paginazione che restituisce i risultati da visualizzare nella
	 * pagina specificata in input
	 *
	 * @param aSoggettoCertificato
	 *            model contenete i parametri della ricerca
	 * @param aPage
	 *            pagina per la quale si vogliono ottenere i risultati
	 * @return Vettore di model con i record da visualizzare nella pagina specificata in input
	 * @throws F3BException
	 ****************************************************************************/
	public Vector ExRicercaSoggettoCertificatoPaged(SoggettoCertificatoModel aSoggettoCertificato, int aPage)
			throws F3BException {

		Connection lConn = null;
		Vector lSoggettoCertificati = new Vector();
		SoggettoCertificatoSqlDAO lSoggettoCertificatoSqlDao = null;

		try {
			lConn = getDBConnection();
			lSoggettoCertificatoSqlDao = new SoggettoCertificatoSqlDAO(lConn);
			lSoggettoCertificatoSqlDao.ricercaSoggettoCertificatoPaged(aSoggettoCertificato, aPage);
			lSoggettoCertificati = new Vector(lSoggettoCertificatoSqlDao.getModels());
		} catch (DAOException daoEx) {
			throw new F3BException(
					"SoggettoCertificatoController.ExRicercaSoggettoCertificatoPaged: Non posso leggere : "
							+ daoEx);
		} finally {
			cleanup(lSoggettoCertificatoSqlDao);
			cleanup(lConn);
		}
		return lSoggettoCertificati;
	}

	public ByteArrayOutputStream ExGetDocumento(SoggettoCertificatoModel aCertificato) throws F3BException {

		Connection lConn = null;
		SoggettoCertificatoDAO lCerDao = null;
		ByteArrayOutputStream lByteArrayOut = null;

		try {
			lConn = getDBConnection();
			lCerDao = new SoggettoCertificatoDAO(lConn);

			lCerDao.setCondizioni(aCertificato);
			lCerDao.setOrderBy();

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