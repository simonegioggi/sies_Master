package siap.siep.pagoPA.controller;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.dao.DAOException;
import f3b.log.LogF3B;
import f3b.util.F3BException;
import siap.controller.SiapController;
import siap.siep.SIEPException;
import siap.siep.pagoPA.dao.BollettinoPagopaDAO;
import siap.siep.pagoPA.dao.BollettinoPagopaSqlDAO;
import siap.siep.pagoPA.model.BollettinoPagopaModel;

/**
 * Title: BollettinoPagopaController 
 * Description: Classe Controller per la gestione del Bollettino PagoPA
 *
 * @author sgioggi
 * @since MEV_2023-13
 * @version 1.0
 */
public class BollettinoPagopaController extends SiapController implements IBollettinoPagopa {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public Vector<BollettinoPagopaModel> ExRicercaBollettinoPagopaByFasSieIdFascicoloSiep(
			BigDecimal fasSieIdFascicoloSiep) throws F3BException {

		Connection c = null;
		Vector<BollettinoPagopaModel> bpms = new Vector<>();

		BollettinoPagopaSqlDAO bpsdao = null;

		try {
			c = getDBConnection();
			bpsdao = new BollettinoPagopaSqlDAO(c);
			bpsdao.ricercaBollettinoPagopaByFasSieIdFascicoloSiep(fasSieIdFascicoloSiep);
			bpsdao.start();

			while (bpsdao.next()) {
				BollettinoPagopaModel bpm = (BollettinoPagopaModel) bpsdao.getModel();
				bpms.add(bpm);
			}
			bpsdao.stop();
		} catch (DAOException daoEx) {
			throw new F3BException(
					"BollettinoPagopaController.ExricercaBollettinoPagopaByFasSieIdFascicoloSiep: Non posso leggere : "
							+ daoEx);
		} catch (Exception e) {
			throw new F3BException(
					"BollettinoPagopaController.ExricercaBollettinoPagopaByFasSieIdFascicoloSiep: " + e);
		} finally {
			cleanup(bpsdao);
			cleanup(c);
		}

		return bpms;
	}

	public BollettinoPagopaModel ExInserisciBollettinoPagopa(BollettinoPagopaModel bpm) throws F3BException {

		BollettinoPagopaModel bpmRet = null;
		BollettinoPagopaDAO bpdao = null;
		Connection c = null;

		try {
			c = getDBConnection();

			bpmRet = new BollettinoPagopaModel(bpm);
			bpdao = new BollettinoPagopaDAO(c);

			// Inserimento Bollettino Pagopa sulla tabella BOLLETTINO_PAGOPA
			bpdao.setDAOFromModel(bpmRet);

			BigDecimal idBollettinoPagopa = bpdao.insert();
			bpmRet.setIdBollettinoPagopa(idBollettinoPagopa);

			commit(c);
		} catch (F3BException fe) {
			rollback(c);
			throw fe;
		} catch (DAOException ex) {
			rollback(c);
			throw new F3BException("BollettinoPagopaController.ExInserisciBollettinoPagopa : " + ex);
		} catch (Exception e) {
			rollback(c);
			throw new F3BException("BollettinoPagopaController.ExInserisciBollettinoPagopa : " + e);
		} finally {
			cleanup(bpdao);
			cleanup(c);
		}

		return bpmRet;
	}

	public BollettinoPagopaModel ExRicercaBollettinoPagopaByKey(BigDecimal idBollettinoPagopa)
			throws F3BException {

		Connection c = null;

		BollettinoPagopaModel bpm = new BollettinoPagopaModel();
		BollettinoPagopaSqlDAO bpsdao = null;

		try {

			c = getDBConnection();

			bpsdao = new BollettinoPagopaSqlDAO(c);
			bpsdao.start();
			while (bpsdao.next()) {
				bpm = new BollettinoPagopaModel((BollettinoPagopaModel) bpsdao.getModel());
			}
			bpsdao.stop();
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new SIEPException(
					"BollettinoPagopaController.ExRicercaBollettinoPagopaByKey: Non posso leggere : "
							+ daoEx);
		} finally {
			cleanup(bpsdao);
			cleanup(c);
		}

		return bpm;
	}

	public void ExModificaBollettinoPagopa(BollettinoPagopaModel bpm) throws F3BException {

		Connection c = null;

		BollettinoPagopaDAO bpdao = null;

		try {
			// Prende una connessione in transazione.
			c = getDBTransaction();

			// Modifica Bollettino Pagopa
			// Aggiornamento sulla tabella BOLLETTINO_PAGOPA
			bpdao = new BollettinoPagopaDAO(c);
			bpdao.setDAOFromModelForUpdate(bpm);
			bpdao.update();
			bpdao.stop();

			commit(c);
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("DAOException: " + daoEx);
			rollback(c);
			throw new SIEPException("BollettinoPagopaController.ExModificaBollettinoPagopa: " + daoEx);
		} catch (Exception e) {
			rollback(c);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Exception: " + e);
			throw new SIEPException("BollettinoPagopaController.ExModificaBollettinoPagopa: " + e);
		} finally {
			cleanup(bpdao);
			cleanup(c);
		}
	}

	public void ExCancellaBollettinoPagopa(BigDecimal idBollettinoPagopa) throws F3BException {

		Connection c = null;

		BollettinoPagopaDAO bpdao = null;

		try {
			c = getDBTransaction();

			// Cancellazione Bollettino Pagopa
			// tabella BOLLETTINO_PAGOPA
			bpdao = new BollettinoPagopaDAO(c);
			bpdao.selCondizioneDeleteByKey(idBollettinoPagopa);
			bpdao.delete();
			bpdao.stop();

			commit(c);
		} catch (DAOException daoEx) {
			throw new F3BException(
					"BollettinoPagopaController.ExCancellaBollettinoPagopa: Non posso leggere : " + daoEx);
		} finally {
			cleanup(bpdao);
			cleanup(c);
		}
	}

	@Override
	public ByteArrayOutputStream ExGetBollettino(BigDecimal idBollettinoPagopa) throws F3BException {

		Connection c = null;
		BollettinoPagopaSqlDAO bpsdao = null;
		ByteArrayOutputStream baos = null;
		try {
			c = getDBConnection();
			bpsdao = new BollettinoPagopaSqlDAO(c);
			bpsdao.getBollettinoByIdBollettinoPagopa(idBollettinoPagopa);
			bpsdao.start();
			if (bpsdao.next()) {
				baos = bpsdao.getBlob("DOC_BOLL_BLOB");
				bpsdao.stop();
			}
			if ((baos == null) || (baos.size() == 0))
				throw new F3BException(F3BException.USER_MESSAGE, "Nessun Bollettino Associato");
		} catch (F3BException eF3b) {
			throw eF3b;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			cleanup(bpsdao);
			cleanup(c);
		}
		return baos;
	}

	@Override
	public String[] ExRicercaCodiciUfficiProduzione(String codUfficio) throws F3BException {

		Connection c = null;
		String[] codici = new String[2];
		BollettinoPagopaSqlDAO bpsdao = null;

		try {
			c = getDBConnection();
			bpsdao = new BollettinoPagopaSqlDAO(c);
			bpsdao.ricercaCodiciUfficiProduzione(codUfficio);
			bpsdao.start();
			while (bpsdao.next()) {
				codici[0] = bpsdao.getString("codUfficio");
				codici[1] = bpsdao.getString("codGl");
			}
			bpsdao.stop();
		} catch (DAOException daoEx) {
			throw new F3BException(
					"BollettinoPagopaController.ExRicercaCodiciUfficiProduzione: Non posso leggere : "
							+ daoEx);
		} catch (Exception e) {
			throw new F3BException("BollettinoPagopaController.ExRicercaCodiciUfficiProduzione: " + e);
		} finally {
			cleanup(bpsdao);
			cleanup(c);
		}
		return codici;
	}

}