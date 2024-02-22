package siap.siep.pagoPA.action;

import java.math.BigDecimal;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;
import siap.sico.web.ActionSiap;

/**
 * Classe che permette di inoltrare una verifica di avvenuto pagamento
 *
 * @author sgioggi
 * @since MEV_2023-13
 * @version 1.0
 */
public class ActInoltraDownloadAvvisoPagoPA extends ActionSiap implements ICostantiPagoPA {

	// info per il log dedicato
	private static Logger siesLogger = Logger.getLogger(LogF3B.WS_PAGO_PA_LOG);

	public String processRequest() throws Exception {

		// info per il log
		siesLogger.debug(getClass().getName() + ".processRequest: inizio");

		String tipoFascicolo = getRequestStringParameter("tipoFascicolo");
		setRequestAttribute("tipoFascicolo", tipoFascicolo);
		BigDecimal idEvento = getRequestBigDecimalParameter("IdEvento");
		setRequestAttribute("idEvento", idEvento);
		String[] idBollettinoPagopa = null;
		if (!isRequestParameterNullObj("idBollettinoPagopa")) {
			idBollettinoPagopa = getRequestStringParameters("idBollettinoPagopa");
			String listaIdBollettini = "";
			for (int i = 0; i < idBollettinoPagopa.length; i++)
				listaIdBollettini += idBollettinoPagopa[i] + "#";
			setRequestAttribute("listaIdBollettini", listaIdBollettini);
		} else {
			// pagina di ritorno
			RedirectTo rt = new RedirectTo();
			rt.setPage(IWebConstants.PG_MAIN);
			setRequestAttribute(IWebConstants.MESSAGE_TEXT,
					"Attenzione! Nessun Bollettino selezionato. Selezionarne almeno uno.");
			rt.setAction("siap.siep.sanzionesostitutiva.action.ActVerificaStatoElencoBollettini&IdEvento="
					+ idEvento);
			setRequestAttribute(IWebConstants.GOTO_PAGE, "" + rt);
			// return rt.toString();
			return IWebConstants.PG_MESSAGE;
		}

		// info per il log
		siesLogger.debug(getClass().getName() + ".processRequest: fine");

		// pagina di ritorno
		return PG_INOLTRA_DOWNLOAD_AVVISO_PAGOPA;
	}

}