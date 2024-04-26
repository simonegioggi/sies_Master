package siap.siep.pagoPaBatch.controller;

import java.math.BigDecimal;
import java.util.Vector;

import f3b.util.F3BException;
import siap.sico.utente.model.UtenteModel;
import siap.siep.pagoPaBatch.model.BatchPagopaModel;
import siap.siep.pagoPaBatch.model.CriteriRicercaBatchPagopaModel;

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

	public Vector <BatchPagopaModel> ExRecuperaLancioBatchPagopa(CriteriRicercaBatchPagopaModel criteriModel, int aPage) throws F3BException;
	
	public void ExLancioBatchPagopa (UtenteModel aUtente) throws F3BException;
	
	public BatchPagopaModel getLastEsecuzioneBatch () throws F3BException;
}
