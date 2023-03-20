package siap.siep.pagoPA.action;

import java.math.BigDecimal;

import siap.sico.web.ActionSiap;

/**
 * Classe che permette di generare un avviso di pagamento per PagoPA
 *
 * @author sgioggi
 * @since MEV_2023-13
 * @version 1.0
 */
public class ActDownloadAvvisoPagoPA extends ActionSiap implements ICostantiPagoPA {

	public String processRequest() throws Exception {

		String tipoFascicolo = getRequestStringParameter("TipoFascicolo");
		setRequestAttribute("tipoFascicolo", tipoFascicolo);
		BigDecimal idEvento = getRequestBigDecimalParameter("IdEvento");
		setRequestAttribute("idEvento", idEvento);

		return PG_DOWNLOAD_AVVISO_PAGOPA;
	}

}