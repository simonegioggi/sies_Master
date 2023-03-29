package siap.siep.pagoPaBatch.controller;

import java.math.BigDecimal;
import java.sql.Connection;

import org.apache.log4j.Logger;

import f3b.dao.DAOException;
import f3b.log.LogF3B;
import f3b.util.F3BException;
import siap.controller.SiapController;
import siap.siep.pagoPaBatch.dao.BatchPagopaDAO;
import siap.siep.pagoPaBatch.model.BatchPagopaModel;

/**
 * Batch per PagoPA
 *
 * @author d.fiorletta
 * @since MEV_2023-13
 * @version 1.0
 */
public class BatchPagopaController extends SiapController  implements IBatchPagopa {

    private static Logger siesLogger = Logger.getLogger(LogF3B.PAGO_PA_LOG);

    public BatchPagopaModel ExInserisciLancioBatchPagopa(BatchPagopaModel batchModel) throws F3BException {

        BatchPagopaModel batchModelRet = null;
        BatchPagopaDAO batchDao = null;
        Connection c = null;

        try {
            c = getDBConnection();

            batchModelRet = new BatchPagopaModel(batchModel);
            
            batchDao = new BatchPagopaDAO(c);

            // Inserimento Esito lancio
            batchDao.setDAOFromModel(batchModelRet);

            BigDecimal id = batchDao.insert();
            batchModelRet.setIdBatchPagopa(id);

            commit(c);
        } catch (DAOException ex) {
            siesLogger.error("DAOException",ex);
            rollback(c);
            throw new F3BException("BatchPagopaController.ExInserisciLancioBatchPagopa : " + ex);
        } catch (Exception e) {
            siesLogger.error("Exception",e);
            rollback(c);
            throw new F3BException("BatchPagopaController.ExInserisciLancioBatchPagopa : " + e);
        } finally {
            cleanup(batchDao);
            cleanup(c);
        }

        return batchModelRet;
    }
    
    
    public BatchPagopaModel ExAggiornaLancioBatchPagopa (BatchPagopaModel batchModel) throws F3BException {

        BatchPagopaModel batchModelRet = null;
        BatchPagopaDAO batchDao = null;
        Connection c = null;

        try {
            c = getDBConnection();
            
            batchModelRet = new BatchPagopaModel(batchModel);
            
            batchDao = new BatchPagopaDAO(c);

            siesLogger.debug("batchModelRet = "+batchModelRet);
            if (batchModelRet.getErroreEsecuzione()!=null)
                siesLogger.debug("batchModelRet = "+batchModelRet.getErroreEsecuzione().length());
            if (batchModelRet.getErroreEsecuzione()!=null)
                siesLogger.debug("getEsitoEsecuzione = "+batchModelRet.getEsitoEsecuzione().length());
            // Aggiornamento esito lancio batch
            batchDao.setDAOFromModelForUpdate(batchModelRet);
            batchDao.setCondizioneUpdate (batchModel.getIdBatchPagopa());
            batchDao.update();
            
            commit(c);
        } catch (DAOException ex) {
            siesLogger.error("DAOException",ex);
            rollback(c);
            throw new F3BException("BatchPagopaController.ExAggiornaLancioBatchPagopa : " + ex);
        } catch (Exception e) {
            siesLogger.error("Exception",e);
            rollback(c);
            throw new F3BException("BatchPagopaController.ExAggiornaLancioBatchPagopa : " + e);
        } finally {
            cleanup(batchDao);
            cleanup(c);
        }

        return batchModelRet;
    }

}
