package siap.siep.pagoPA.action;

import java.math.BigDecimal;

import f3b.util.F3BException;
import siap.sico.web.ActionSiap;
import siap.siep.pagoPaBatch.action.ICostantiBatchPagoPa;
import siap.siep.pagoPaBatch.controller.IBatchPagopa;
import siap.siep.pagoPaBatch.model.BatchPagopaModel;
import siap.siep.util.SIEPLookupRemote;

/**
 * <p>
 * Title: ActLoadAvvisoBatchPagoPA
 * </p>
 * <p>
 * Description: Classe Action per la load dell'avviso sul batche PagoPA
 * </p>
 *
 * @author sgioggi
 * @since MEV_2023-33
 * @version 1.0
 */
public class ActLoadAvvisoBatchPagoPA extends ActionSiap implements ICostantiPagoPA {

	public String processRequest() throws F3BException {

	  BigDecimal idBatch = getRequestBigDecimalParameter(ICostantiBatchPagoPa.CAMPO_ID_BATCH_PAGOPA);
	  IBatchPagopa lCtrlBatch = SIEPLookupRemote.getBatchPagopaPagopaRemote();
	  BatchPagopaModel lancioBatch = lCtrlBatch.ExRicercaBatchPagopaByKey(idBatch);
	  
	  setRequestAttribute("BatchPagoPa",lancioBatch);
	  
		return PG_AVVISO_BATCH_PAGOPA;
	}

}