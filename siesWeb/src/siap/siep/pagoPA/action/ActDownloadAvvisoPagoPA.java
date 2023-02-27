package siap.siep.pagoPA.action;

import java.math.BigDecimal;

import f3b.web.IWebConstants;
import siap.sico.web.ActionSiap;

/**
 * MEV_2023-13: aggiunta classe di download avviso
 *
 * @author sgioggi
 * @version 1.0
 */
public class ActDownloadAvvisoPagoPA extends ActionSiap implements ICostantiPagoPA {

	public String processRequest() throws Exception {

		String tipoFascicolo = getRequestStringParameter("TipoFascicolo");
		setRequestAttribute("tipoFascicolo", tipoFascicolo);
		BigDecimal idEvento = getRequestBigDecimalParameter("IdEvento");
		setRequestAttribute("idEvento", idEvento);

		return IWebConstants.ROOT_DIR + PG_DOWNLOAD_AVVISO_PAGOPA;
	}

}