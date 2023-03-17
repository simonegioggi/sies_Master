package siap.siep.pagoPA.controller;

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
 * Title: BollettinoPagopaController Description: Classe Controller per la gestione del Bollettino PagoPA
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
		Vector<BollettinoPagopaModel> coms = new Vector<>();

		BollettinoPagopaSqlDAO bpsdao = null;

		try {
			c = getDBConnection();
			bpsdao = new BollettinoPagopaSqlDAO(c);
			bpsdao.ricercaBollettinoPagopaByFasSieIdFascicoloSiep(fasSieIdFascicoloSiep);
			bpsdao.start();

			while (bpsdao.next()) {
				BollettinoPagopaModel com = (BollettinoPagopaModel) bpsdao.getModel();
				coms.add(com);
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

		return coms;
	}

	public BollettinoPagopaModel ExInserisciBollettinoPagopa(BollettinoPagopaModel com) throws F3BException {

		BollettinoPagopaModel comRet = null;
		BollettinoPagopaDAO bpdao = null;
		Connection c = null;

		try {
			c = getDBConnection();

			comRet = new BollettinoPagopaModel(com);
			bpdao = new BollettinoPagopaDAO(c);

			// Inserimento Bollettino Pagopa sulla tabella BOLLETTINO_PAGOPA
			bpdao.setDAOFromModel(comRet);

			BigDecimal bd = null;
			bd = bpdao.insert();
			comRet.setIdBollettinoPagopa(bd);

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

		return comRet;
	}

	public BollettinoPagopaModel ExRicercaBollettinoPagopaByKey(BigDecimal idBollettinoPagopa)
			throws F3BException {

		Connection c = null;

		BollettinoPagopaModel com = new BollettinoPagopaModel();
		BollettinoPagopaSqlDAO bpsdao = null;

		try {

			c = getDBConnection();

			bpsdao = new BollettinoPagopaSqlDAO(c);
			bpsdao.start();
			while (bpsdao.next()) {
				com = new BollettinoPagopaModel((BollettinoPagopaModel) bpsdao.getModel());
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

		return com;
	}

	public void ExModificaBollettinoPagopa(BollettinoPagopaModel com) throws F3BException {

		Connection c = null;

		BollettinoPagopaDAO bpdao = null;

		try {
			// Prende una connessione in transazione.
			c = getDBTransaction();

			// Modifica Bollettino Pagopa
			// Aggiornamento sulla tabella BOLLETTINO_PAGOPA
			bpdao = new BollettinoPagopaDAO(c);
			bpdao.setDAOFromModelForUpdate(com);
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

}