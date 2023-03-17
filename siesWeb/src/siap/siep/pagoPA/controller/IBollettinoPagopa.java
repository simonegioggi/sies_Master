package siap.siep.pagoPA.controller;

import java.math.BigDecimal;
import java.util.Vector;

import f3b.util.F3BException;
import siap.siep.pagoPA.model.BollettinoPagopaModel;

/**
 * Title: IBollettinoPagopa 
 * Description: Interfaccia per la gestione del Bollettino PagoPA
 *
 * @author sgioggi
 * @since MEV_2023-13
 * @version 1.0
 */
public interface IBollettinoPagopa {

	BollettinoPagopaModel ExInserisciBollettinoPagopa(BollettinoPagopaModel com) throws F3BException;

	public void ExCancellaBollettinoPagopa(BigDecimal idBollettinoPagopa) throws F3BException;

	public BollettinoPagopaModel ExRicercaBollettinoPagopaByKey(BigDecimal idBollettinoPagopa)
			throws F3BException;

	public void ExModificaBollettinoPagopa(BollettinoPagopaModel com) throws F3BException;

	public Vector<BollettinoPagopaModel> ExRicercaBollettinoPagopaByFasSieIdFascicoloSiep(
			BigDecimal fasSieIdFascicolSiep) throws F3BException;

}