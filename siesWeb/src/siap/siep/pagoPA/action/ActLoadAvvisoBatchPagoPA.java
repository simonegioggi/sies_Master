package siap.siep.pagoPA.action;

import f3b.util.F3BException;
import siap.sico.web.ActionSiap;

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

		return PG_AVVISO_BATCH_PAGOPA;
	}

}