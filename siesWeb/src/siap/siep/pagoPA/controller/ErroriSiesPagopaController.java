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
import siap.siep.pagoPA.dao.ErroriSiesPagopaDAO;
import siap.siep.pagoPA.dao.ErroriSiesPagopaSqlDAO;
import siap.siep.pagoPA.model.ErroriSiesPagopaModel;

@SuppressWarnings("unchecked")
public class ErroriSiesPagopaController extends SiapController implements IErroriSiesPagopa {

	private static Logger siesLogger = Logger.getLogger(LogF3B.WS_PAGO_PA_LOG);

	public ErroriSiesPagopaModel ExInserisciErroreSiesPagopa(ErroriSiesPagopaModel aErrModel)
			throws F3BException {

		ErroriSiesPagopaModel lErroreModelRet = null;
		ErroriSiesPagopaDAO lErroreDAO = null;
		Connection c = null;

		try {
			c = getDBConnection();

			lErroreModelRet = new ErroriSiesPagopaModel(aErrModel);
			lErroreDAO = new ErroriSiesPagopaDAO(c);

			lErroreDAO.setDAOFromModel(lErroreModelRet);

			BigDecimal idErrore = lErroreDAO.insert();
			lErroreModelRet.setIdErroriSiesPagopa(idErrore);

			commit(c);
		} catch (DAOException ex) {
			siesLogger.error("ErroriSiesPagopaController.ExInserisciErroreSiesPagopa ", ex);
			rollback(c);
			throw new F3BException("BollettinoPagopaController.ExInserisciBollettinoPagopa : " + ex);
		} catch (Exception e) {
			siesLogger.error("ErroriSiesPagopaController.ExInserisciErroreSiesPagopa ", e);
			rollback(c);
			throw new F3BException("BollettinoPagopaController.ExInserisciBollettinoPagopa : " + e);
		} finally {
			cleanup(lErroreDAO);
			cleanup(c);
		}

		return lErroreModelRet;
	}

	public ErroriSiesPagopaModel ExRicercaErroreSiesPagopaByKey(BigDecimal aIdErrore) throws F3BException {

		Connection c = null;

		ErroriSiesPagopaModel lErrModel = new ErroriSiesPagopaModel();
		ErroriSiesPagopaSqlDAO lErrSiesSqlDao = null;

		try {
			c = getDBConnection();
			lErrSiesSqlDao = new ErroriSiesPagopaSqlDAO(c);

			lErrSiesSqlDao.ricercaErroreaByKey(aIdErrore);
			lErrModel = (ErroriSiesPagopaModel) lErrSiesSqlDao.getModelByKey();

			lErrSiesSqlDao.stop();
		} catch (DAOException daoEx) {
			siesLogger.error("DAOException: ", daoEx);
			throw new SIEPException(
					"ErroriSiesPagopaController.ExRicercaErroreSiesPagopaByKey: Non posso leggere : "
							+ daoEx);
		} finally {
			cleanup(lErrSiesSqlDao);
			cleanup(c);
		}

		return lErrModel;
	}

	public void ExCancellaErroreSiesPagopa(BigDecimal aIdErrore) throws F3BException {

		Connection c = null;

		ErroriSiesPagopaDAO lErroreDAO = null;

		try {
			c = getDBTransaction();
			lErroreDAO = new ErroriSiesPagopaDAO(c);

			lErroreDAO.selCondizioneByKey(aIdErrore);
			lErroreDAO.delete();
			lErroreDAO.stop();

			commit(c);
		} catch (DAOException daoEx) {
			siesLogger.error("DAOException: ", daoEx);
			rollback(c);
			throw new F3BException("ErroriSiesPagopaController.ExCancellaErroreSiesPagopa: " + daoEx);
		} finally {
			cleanup(lErroreDAO);
			cleanup(c);
		}
	}

	public Vector<ErroriSiesPagopaModel> ExRicercaErroriSiesPagopaByCriteria(
			ErroriSiesPagopaModel aCriteriRicerca, int aPage) throws F3BException {

		Connection c = null;
		ErroriSiesPagopaSqlDAO lErrSiesSqlDao = null;

		Vector<ErroriSiesPagopaModel> lListaErrori = new Vector<>();

		try {
			c = getDBConnection();
			lErrSiesSqlDao = new ErroriSiesPagopaSqlDAO(c);

			lErrSiesSqlDao.ricercaErroreaByCriteria(aCriteriRicerca, aPage);

			lListaErrori = new Vector<ErroriSiesPagopaModel>(lErrSiesSqlDao.getModels());

			lErrSiesSqlDao.stop();
		} catch (DAOException daoEx) {
			siesLogger.error("DAOException: ", daoEx);
			throw new SIEPException(
					"ErroriSiesPagopaController.ExRicercaErroriSiesPagopaByCriteria: Non posso leggere : "
							+ daoEx);
		} finally {
			cleanup(lErrSiesSqlDao);
			cleanup(c);
		}

		return lListaErrori;
	}

	/**
	 * EFfettua l'inserimento dell'erore se non esiste altrimento lo aggiorna
	 *
	 */
	public ErroriSiesPagopaModel ExInserisciAggiornaErroreSiesPagopa(ErroriSiesPagopaModel aErrModel)
			throws F3BException {

		ErroriSiesPagopaModel lErroreModelRet = null;
		ErroriSiesPagopaDAO lErroreDAO = null;
		ErroriSiesPagopaSqlDAO lErrSiesSqlDao = null;
		Connection c = null;

		ErroriSiesPagopaModel aCriteriRicerca = new ErroriSiesPagopaModel();
		aCriteriRicerca.setIdFascicoloSiep(aErrModel.getIdFascicoloSiep());
		aCriteriRicerca.setIdEvento(aErrModel.getIdEvento());
		aCriteriRicerca.setAzioneContestoJava(aErrModel.getAzioneContestoJava());

		lErroreModelRet = new ErroriSiesPagopaModel(aErrModel);

		try {
			c = getDBConnection();

			lErrSiesSqlDao = new ErroriSiesPagopaSqlDAO(c);
			lErroreDAO = new ErroriSiesPagopaDAO(c);

			//
			lErrSiesSqlDao.ricercaErroreaByCriteria(aCriteriRicerca, 0);
			Vector<ErroriSiesPagopaModel> lListaErrori = new Vector<ErroriSiesPagopaModel>(
					lErrSiesSqlDao.getModels());

			if (lListaErrori.size() == 0) {
				siesLogger.debug("Errore non trovato, lo inserisco...");
				// Verifico se esiste già un record per
				lErroreDAO.setDAOFromModel(lErroreModelRet);

				BigDecimal idErrore = lErroreDAO.insert();
				lErroreModelRet.setIdErroriSiesPagopa(idErrore);
			} else if (lListaErrori.size() == 1) {
				siesLogger.debug("Errore gia' presente, lo aggiorno...");
				ErroriSiesPagopaModel lErroreDaAggiornare = lListaErrori.elementAt(0);

				lErroreDAO.setDAOFromModelForUpdate(lErroreModelRet);
				lErroreDAO.selCondizioneByKey(lErroreDaAggiornare.getIdErroriSiesPagopa());
				lErroreDAO.update();
				lErroreModelRet.setIdErroriSiesPagopa(lErroreDaAggiornare.getIdErroriSiesPagopa());
			} else {
				siesLogger.warn(
						"Attenzione è presente più di un 'ERRORI_SIES_PAGOPA' che corrisponde a quello in ingresso...");
			}

			commit(c);
		} catch (DAOException ex) {
			siesLogger.error("ErroriSiesPagopaController.ExInserisciAggiornoErroreSiesPagopa ", ex);
			rollback(c);
			throw new F3BException("BollettinoPagopaController.ExInserisciAggiornoErroreSiesPagopa : " + ex);
		} catch (Exception e) {
			siesLogger.error("ErroriSiesPagopaController.ExInserisciAggiornoErroreSiesPagopa ", e);
			rollback(c);
			throw new F3BException("BollettinoPagopaController.ExInserisciAggiornoErroreSiesPagopa : " + e);
		} finally {
			cleanup(lErroreDAO);
			cleanup(lErrSiesSqlDao);
			cleanup(c);
		}

		return lErroreModelRet;
	}

	public void ExRimuoviErroreSiesPagopa(ErroriSiesPagopaModel aErrModel) throws F3BException {

		siesLogger.debug("ErroriSiesPagopaModel...");
		ErroriSiesPagopaDAO lErroreDAO = null;
		ErroriSiesPagopaSqlDAO lErrSiesSqlDao = null;
		Connection c = null;

		ErroriSiesPagopaModel aCriteriRicerca = new ErroriSiesPagopaModel();
		aCriteriRicerca.setIdFascicoloSiep(aErrModel.getIdFascicoloSiep());
		aCriteriRicerca.setIdEvento(aErrModel.getIdEvento());
		aCriteriRicerca.setAzioneContestoJava(aErrModel.getAzioneContestoJava());

		try {
			c = getDBConnection();

			lErrSiesSqlDao = new ErroriSiesPagopaSqlDAO(c);
			lErroreDAO = new ErroriSiesPagopaDAO(c);

			//
			lErrSiesSqlDao.ricercaErroreaByCriteria(aCriteriRicerca, 0);
			Vector<ErroriSiesPagopaModel> lListaErrori = new Vector<ErroriSiesPagopaModel>(
					lErrSiesSqlDao.getModels());

			if (lListaErrori.size() == 0) {
				siesLogger.debug("Errore non trovato...");
			} else if (lListaErrori.size() > 0) {
				// n.b. dovrebbe essere solo 1
				for (ErroriSiesPagopaModel lErrore : lListaErrori) {
					siesLogger.debug("Cancello..." + lErrore.getIdErroriSiesPagopa());
					lErroreDAO.selCondizioneByKey(lErrore.getIdErroriSiesPagopa());
					lErroreDAO.delete();
				}
			}

			commit(c);
		} catch (DAOException ex) {
			siesLogger.error("ErroriSiesPagopaController.ExRimuoviErroreSiesPagopa ", ex);
			rollback(c);
			throw new F3BException("BollettinoPagopaController.ExRimuoviErroreSiesPagopa : " + ex);
		} catch (Exception e) {
			siesLogger.error("ErroriSiesPagopaController.ExRimuoviErroreSiesPagopa ", e);
			rollback(c);
			throw new F3BException("BollettinoPagopaController.ExRimuoviErroreSiesPagopa : " + e);
		} finally {
			cleanup(lErroreDAO);
			cleanup(lErrSiesSqlDao);

			cleanup(c);
		}
	}

}