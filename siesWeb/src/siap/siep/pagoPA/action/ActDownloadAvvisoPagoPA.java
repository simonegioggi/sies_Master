package siap.siep.pagoPA.action;

import java.math.BigDecimal;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import siap.sico.web.ActionSiap;

/**
 * Classe che permette di generare un avviso di pagamento per PagoPA
 *
 * @author sgioggi
 * @since MEV_2023-13
 * @version 1.0
 */
public class ActDownloadAvvisoPagoPA extends ActionSiap implements ICostantiPagoPA {

	// info per il log dedicato
	private static Logger siesLogger = Logger.getLogger(LogF3B.WS_PAGO_PA_LOG);

	public String processRequest() throws Exception {

		// info per il log
		siesLogger.debug(getClass().getName() + ".processRequest: inizio");

		String tipoFascicolo = getRequestStringParameter("tipoFascicolo");
		setRequestAttribute("tipoFascicolo", tipoFascicolo);
		BigDecimal idEvento = getRequestBigDecimalParameter("IdEvento");
		setRequestAttribute("idEvento", idEvento);

		// info per il log
		siesLogger.debug(getClass().getName() + ".processRequest: fine");

		// pagina di ritorno
		return PG_DOWNLOAD_AVVISO_PAGOPA;
	}

}