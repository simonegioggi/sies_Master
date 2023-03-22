package siap.siep.pagoPA.action;

import java.math.BigDecimal;

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

	public String processRequest() throws Exception {

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

		return PG_INOLTRA_DOWNLOAD_AVVISO_PAGOPA;
	}

}