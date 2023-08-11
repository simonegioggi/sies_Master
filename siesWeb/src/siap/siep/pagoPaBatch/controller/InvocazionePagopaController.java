package siap.siep.pagoPaBatch.controller;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.dao.DAOException;
import f3b.log.LogF3B;
import f3b.util.F3BException;
import siap.controller.SiapController;
import siap.siep.pagoPA.dao.BollettinoPagopaSqlDAO;
import siap.siep.pagoPA.model.BollettinoPagopaModel;
import siap.siep.pagoPaBatch.dao.BollettinoBatchPagopaDAO;
import siap.siep.pagoPaBatch.dao.InvocazionePagopaDAO;
import siap.siep.pagoPaBatch.dao.InvocazionePagopaSqlDAO;
import siap.siep.pagoPaBatch.model.BollettinoBatchPagopaModel;
import siap.siep.pagoPaBatch.model.InvocazionePagopaModel;

public class InvocazionePagopaController extends SiapController implements IInvocazionePagopa {

	private static Logger siesLogger = Logger.getLogger(LogF3B.PAGO_PA_LOG);

	public InvocazionePagopaModel ExInserisciInvocazionePagopa(InvocazionePagopaModel aInvocazioneModel)
			throws F3BException {

		InvocazionePagopaDAO lInvocazioneDao = null;
		Connection lConn = null;

		try {
			lConn = getDBConnection();

			lInvocazioneDao = new InvocazionePagopaDAO(lConn);

			// Inserimento Esito lancio
			lInvocazioneDao.setDAOFromModel(aInvocazioneModel);

			BigDecimal id = lInvocazioneDao.insert();
			aInvocazioneModel.setIdInvocazionePagopa(id);

			commit(lConn);
		} catch (DAOException ex) {
			siesLogger.error("DAOException", ex);
			rollback(lConn);
			throw new F3BException("InvocazionePagopaController.ExInserisciInvocazionePagopa : " + ex);
		} catch (Exception e) {
			siesLogger.error("Exception", e);
			rollback(lConn);
			throw new F3BException("InvocazionePagopaController.ExInserisciInvocazionePagopa : " + e);
		} finally {
			cleanup(lInvocazioneDao);
			cleanup(lConn);
		}

		return aInvocazioneModel;
	}

	public InvocazionePagopaModel ExAggiornaInvocazionePagopa(InvocazionePagopaModel aInvocazioneModel)
			throws F3BException {

		InvocazionePagopaDAO lInvocazioneDao = null;
		Connection lConn = null;

		try {
			lConn = getDBConnection();

			lInvocazioneDao = new InvocazionePagopaDAO(lConn);

			// Aggiornamento
			lInvocazioneDao.setDAOFromModelForUpdate(aInvocazioneModel);
			lInvocazioneDao.setCondizioneUpdate(aInvocazioneModel.getIdInvocazionePagopa());

			lInvocazioneDao.update();

			commit(lConn);
		} catch (DAOException ex) {
			siesLogger.error("DAOException", ex);
			rollback(lConn);
			throw new F3BException("InvocazionePagopaController.ExAggiornaInvocazionePagopa : " + ex);
		} catch (Exception e) {
			siesLogger.error("Exception", e);
			rollback(lConn);
			throw new F3BException("InvocazionePagopaController.ExAggiornaInvocazionePagopa : " + e);
		} finally {
			cleanup(lInvocazioneDao);
			cleanup(lConn);
		}

		return aInvocazioneModel;
	}

	@SuppressWarnings("unchecked")
	public Vector<InvocazionePagopaModel> ExRicercaInvocazioniByIdBatchPagopa(BigDecimal aIdBatchPagopa,
			int aPage) throws F3BException {
		Vector<InvocazionePagopaModel> listaInvocazioni = null;
		InvocazionePagopaSqlDAO lInvocazioneSqlDao = null;
		BollettinoPagopaSqlDAO lBollettinoSqlDao = null;

		Connection conn = null;

		try {
			conn = getDBConnection();

			lInvocazioneSqlDao = new InvocazionePagopaSqlDAO(conn);

			lInvocazioneSqlDao.ricercaInvocazioniByIdBatchPaged(aIdBatchPagopa, aPage);

			listaInvocazioni = new Vector<InvocazionePagopaModel>(lInvocazioneSqlDao.getModels());

			lBollettinoSqlDao = new BollettinoPagopaSqlDAO(conn);
			for (int i = 0; i < listaInvocazioni.size(); i++) {
				InvocazionePagopaModel invocazioneModel = listaInvocazioni.elementAt(i);

				lBollettinoSqlDao
						.ricercaBollettiniPagopaByIdInvocazione(invocazioneModel.getIdInvocazionePagopa());
				Vector<BollettinoPagopaModel> listaBollettini = new Vector<BollettinoPagopaModel>(
						lBollettinoSqlDao.getModels());

				invocazioneModel.setListaBollettini(listaBollettini);
				lBollettinoSqlDao.stop();
			}

		} catch (DAOException ex) {
			siesLogger.error("DAOException", ex);
			throw new F3BException("BatchPagopaController.ExRicercaInvocazioniByIdBatchPagopa : " + ex);
		} catch (Exception e) {
			siesLogger.error("Exception", e);
			throw new F3BException("BatchPagopaController.ExRicercaInvocazioniByIdBatchPagopa : " + e);
		} finally {
			cleanup(lInvocazioneSqlDao);
			cleanup(lBollettinoSqlDao);

			cleanup(conn);
		}

		return listaInvocazioni;
	}

	public BollettinoBatchPagopaModel ExInserisciBollettinoBatchPagopa(
			BollettinoBatchPagopaModel aBollettinoBatchModel) throws F3BException {

		BollettinoBatchPagopaDAO lBollettinoBatchDao = null;
		Connection lConn = null;

		try {
			lConn = getDBConnection();

			lBollettinoBatchDao = new BollettinoBatchPagopaDAO(lConn);

			// Inserimento Esito lancio
			lBollettinoBatchDao.setDAOFromModel(aBollettinoBatchModel);

			lBollettinoBatchDao.insert();

			commit(lConn);
		} catch (DAOException ex) {
			siesLogger.error("DAOException", ex);
			rollback(lConn);
			throw new F3BException("InvocazionePagopaController.ExInserisciInvocazionePagopa : " + ex);
		} catch (Exception e) {
			siesLogger.error("Exception", e);
			rollback(lConn);
			throw new F3BException("InvocazionePagopaController.ExInserisciInvocazionePagopa : " + e);
		} finally {
			cleanup(lBollettinoBatchDao);
			cleanup(lConn);
		}

		return aBollettinoBatchModel;
	}

	public InvocazionePagopaModel ExRicercaInvocazioneById(BigDecimal aIdInvocazione) throws F3BException {
		InvocazionePagopaModel lInvocazioneModel = null;
		InvocazionePagopaSqlDAO lInvocazioneSqlDao = null;

		Connection conn = null;

		try {
			conn = getDBConnection();

			lInvocazioneSqlDao = new InvocazionePagopaSqlDAO(conn);

			lInvocazioneSqlDao.ricercaInvocazioniById(aIdInvocazione);

			lInvocazioneModel = (InvocazionePagopaModel) lInvocazioneSqlDao.getModelByKey();

		} catch (DAOException ex) {
			siesLogger.error("DAOException", ex);
			throw new F3BException("BatchPagopaController.ExRicercaInvocazioneById : " + ex);
		} catch (Exception e) {
			siesLogger.error("Exception", e);
			throw new F3BException("BatchPagopaController.ExRicercaInvocazioneById : " + e);
		} finally {
			cleanup(lInvocazioneSqlDao);

			cleanup(conn);
		}

		return lInvocazioneModel;
	}

}