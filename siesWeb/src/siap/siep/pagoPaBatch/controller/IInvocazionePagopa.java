package siap.siep.pagoPaBatch.controller;

import java.math.BigDecimal;
import java.util.Vector;

import f3b.util.F3BException;
import siap.siep.pagoPaBatch.model.BollettinoBatchPagopaModel;
import siap.siep.pagoPaBatch.model.InvocazionePagopaModel;

/**
 *
 * @author d.fiorletta
 * @since MEV_2023-13
 * @version 1.0
 */
public interface IInvocazionePagopa {
	InvocazionePagopaModel ExInserisciInvocazionePagopa(InvocazionePagopaModel aInvocazione) throws F3BException;
	
	InvocazionePagopaModel ExAggiornaInvocazionePagopa(InvocazionePagopaModel aInvocazione) throws F3BException;

	public InvocazionePagopaModel ExRicercaInvocazioneById (BigDecimal aIdInvocazione) throws F3BException;

	public Vector <InvocazionePagopaModel> ExRicercaInvocazioniByIdBatchPagopa(BigDecimal aIdBatchPagopa, int aPage) throws F3BException;
	
	public BollettinoBatchPagopaModel ExInserisciBollettinoBatchPagopa(BollettinoBatchPagopaModel aBollettinoBatchModel)  throws F3BException;
}
