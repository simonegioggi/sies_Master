package siap.siep.pagoPaBatch.controller;

import java.math.BigDecimal;
import java.util.Vector;

import f3b.util.F3BException;
import siap.siep.pagoPaBatch.model.BatchPagopaModel;

/**
 * Batch per PagoPA
 *
 * @author d.fiorletta
 * @since MEV_2023-13
 * @version 1.0
 */
public interface IBatchPagopa {

	BatchPagopaModel ExInserisciLancioBatchPagopa(BatchPagopaModel batchModel) throws F3BException;

	BatchPagopaModel ExAggiornaLancioBatchPagopa(BatchPagopaModel batchModel) throws F3BException;
	
	BatchPagopaModel ExRicercaBatchPagopaByKey(BigDecimal aIdBatch) throws F3BException;

	public Vector <BatchPagopaModel> ExRecuperaLancioBatchPagopa(BatchPagopaModel batchModel, int aPage) throws F3BException;
}
