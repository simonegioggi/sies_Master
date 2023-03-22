package siap.siep.pagoPaBatch.controller;

import f3b.util.F3BException;
import siap.siep.pagoPaBatch.model.BatchPagopaModel;

public interface IBatchPagopa {
    BatchPagopaModel ExInserisciLancioBatchPagopa (BatchPagopaModel batchModel) throws F3BException;
    BatchPagopaModel ExAggiornaLancioBatchPagopa (BatchPagopaModel batchModel) throws F3BException;
}
