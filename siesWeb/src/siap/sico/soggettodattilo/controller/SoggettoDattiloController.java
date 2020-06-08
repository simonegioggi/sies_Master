package siap.sico.soggettodattilo.controller;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.dao.DAOException;
import f3b.log.LogF3B;
import f3b.util.F3BException;
import siap.controller.SiapController;
import siap.sico.soggettodattilo.dao.SoggettoDattiloDAO;
import siap.sico.soggettodattilo.dao.SoggettoDattiloSqlDAO;
import siap.sico.soggettodattilo.model.SoggettoDattiloModel;

/**
 * <p>
 * Title: SoggettoDattiloController
 * </p>
 * <p>
 * Description: Classe Controller per SoggettoDattilo
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
public class SoggettoDattiloController extends SiapController implements ISoggettoDattilo {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public SoggettoDattiloModel ExInserisciSoggettoDattilo(SoggettoDattiloModel aSoggettoDattilo)
			throws F3BException {

		Connection lConn = null;
		SoggettoDattiloDAO lSogDao = null;
		SoggettoDattiloModel lSogMod = null;

		try {
			lConn = getDBConnection();
			lSogMod = new SoggettoDattiloModel(aSoggettoDattilo);
			lSogDao = new SoggettoDattiloDAO(lConn);
			lSogDao.setDAOFromModel(aSoggettoDattilo);
			BigDecimal lKey = lSogDao.insert();
			commit(lConn);
			lSogMod.setIdDattilo(lKey);
		} catch (DAOException ex) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + ex);
			throw new F3BException(
					"SoggettoDattiloController.ExInserisciSoggettoDattilo: Non posso inserire: " + ex);
		} finally {
			cleanup(lSogDao);
			cleanup(lConn);
		}
		return lSogMod;
	}

	public Vector ExRicercaSoggettoDattilo(SoggettoDattiloModel aSoggettoDattilo) throws F3BException {

		Connection lConn = null;
		Vector lSoggettoDattili = new Vector();
		SoggettoDattiloSqlDAO lSogDao = null;

		try {
			lConn = getDBConnection();
			lSogDao = new SoggettoDattiloSqlDAO(lConn);
			lSogDao.ricercaSoggettoDattilo(aSoggettoDattilo);
			lSoggettoDattili = new Vector(lSogDao.getModels());
			if (lSoggettoDattili.size() == 0) {
				throw new F3BException(F3BException.USER_MESSAGE, "Nessun Elemento trovato");
			}
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException(
					"SoggettoDattiloController.ExRicercaSoggettoDattilo: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lSogDao);
			cleanup(lConn);
		}
		return lSoggettoDattili;
	}

	public SoggettoDattiloModel ExRicercaSoggettoDattiloByKey(BigDecimal aKey) throws F3BException {

		Connection lConn = null;
		SoggettoDattiloSqlDAO lSogDao = null;
		SoggettoDattiloModel lSogMod;

		try {
			lConn = getDBConnection();
			lSogDao = new SoggettoDattiloSqlDAO(lConn);
			lSogDao.ricercaSoggettoDattiloByKey(aKey);
			lSogMod = (SoggettoDattiloModel) lSogDao.getModelByKey();
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException(
					"SoggettoDattiloController.ExRicercaSoggettoDattilo: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lSogDao);
			cleanup(lConn);
		}
		return lSogMod;
	}

	public SoggettoDattiloModel ExModificaSoggettoDattilo(SoggettoDattiloModel aSoggettoDattilo)
			throws F3BException {

		Connection lConn = null;
		SoggettoDattiloDAO lSogDao = null;
		SoggettoDattiloModel lSogMod = new SoggettoDattiloModel(aSoggettoDattilo);

		try {
			lConn = getDBConnection();
			lSogDao = new SoggettoDattiloDAO(lConn);
			lSogDao.setDAOFromModelForUpdate(aSoggettoDattilo);
			lSogDao.update();
			commit(lConn);
		} catch (DAOException ex) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + ex);
			throw new F3BException(
					"SoggettoDattiloController.ExModificaSoggettoDattilo: Non posso inserire: " + ex);
		} finally {
			cleanup(lSogDao);
			cleanup(lConn);
		}
		return lSogMod;
	}

	public void ExCancellaSoggettoDattilo(SoggettoDattiloModel aSoggettoDattilo) throws F3BException {

		Connection lConn = null;
		SoggettoDattiloDAO lSogDao = null;

		try {
			lConn = getDBConnection();
			lSogDao = new SoggettoDattiloDAO(lConn);
			lSogDao.setCondizioneUpdate(aSoggettoDattilo.getIdDattilo());
			lSogDao.delete();
			commit(lConn);
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException(
					"SoggettoDattiloController.ExCancellaSoggettoDattilo: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lSogDao);
			cleanup(lConn);
		}
	}

	public SoggettoDattiloModel ExUpdateDocument(SoggettoDattiloModel aSoggettoDattilo) throws F3BException {

		Connection lConn = null;
		SoggettoDattiloDAO lSogDao = null;
		SoggettoDattiloDAO lSogDaoBlob = null;
		SoggettoDattiloModel lSogMod = new SoggettoDattiloModel(aSoggettoDattilo);

		try {
			lConn = getDBConnection();

			// effettua prima l'inserimento di un record
			lSogDao = new SoggettoDattiloDAO(lConn);
			lSogMod = new SoggettoDattiloModel(aSoggettoDattilo);
			lSogDao.setDAOFromModel(lSogMod);
			BigDecimal lKey = lSogDao.insert();

			// imposto l'id del record appena inserito
			aSoggettoDattilo.setIdDattilo(lKey);

			// effettua dopo l'aggiornamento del blob
			lSogDaoBlob = new SoggettoDattiloDAO(lConn);
			lSogDaoBlob.setDAOFromModelForUpdateBlob(aSoggettoDattilo);
			lSogDaoBlob.update();
			lSogDaoBlob.stop();

			commit(lConn);
		} catch (DAOException ex) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + ex);
			throw new F3BException("SoggettoDattiloController.ExUpdateDocument: Non posso inserire: " + ex);
		} finally {
			cleanup(lSogDaoBlob);
			cleanup(lSogDao);
			cleanup(lConn);
		}
		return lSogMod;
	}

	public ByteArrayOutputStream ExGetDocumento(SoggettoDattiloModel aSoggettoDattilo) throws F3BException {

		Connection lConn = null;
		SoggettoDattiloDAO lDao = null;
		ByteArrayOutputStream lByteArrayOut = null;

		try {
			lConn = getDBConnection();
			lDao = new SoggettoDattiloDAO(lConn);

			lDao.setIdDattilo(aSoggettoDattilo.getIdDattilo());
			lDao.selByKey();

			lDao.start(1);

			if (lDao.next())
				lByteArrayOut = lDao.getDocBlob();

			lDao.stop();

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
			cleanup(lDao);
			cleanup(lConn);
		}
		return lByteArrayOut;
	}

}