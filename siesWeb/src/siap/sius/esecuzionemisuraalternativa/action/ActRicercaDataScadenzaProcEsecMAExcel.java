package siap.sius.esecuzionemisuraalternativa.action;

import java.io.ByteArrayOutputStream;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.Utils;
import f3b.web.IWebConstants;
import siap.sius.ActionSius;
import siap.sius.statistiche.model.RicercaProcedimentoModel;

/**
 * ActRicercaDataScadenzaProcEsecMAExcel - Classe Action per la ricerca Scadenzario monitoraggio misure
 * alternative espiate
 *
 * @since MEV_2025-48
 * @author sgioggi
 * @version 1.0
 */
public class ActRicercaDataScadenzaProcEsecMAExcel extends ActionSius {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public String processRequest() throws Exception {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(getClass().getName() + " .processRequest: inizio");

		RicercaProcedimentoModel rpm = null;
		ByteArrayOutputStream baos = null;

		rpm = (RicercaProcedimentoModel) getSessionAttribute("rpm");
		String anni = Utils.isPresent(getRequestStringParameter("Anni")) ? getRequestStringParameter("Anni")
				: "-";
		String mesi = Utils.isPresent(getRequestStringParameter("Mesi")) ? getRequestStringParameter("Mesi")
				: "-";
		String giorni = Utils.isPresent(getRequestStringParameter("Giorni"))
				? getRequestStringParameter("Giorni")
				: "-";
		if (Utils.isPresent(anni) || Utils.isPresent(mesi) || Utils.isPresent(giorni))
			rpm.setDescrCancelleria("in scadenza entro: Anni " + anni + " Mesi " + mesi + " Giorni " + giorni);

		RicercaDataScadenzaProcEsecMAExcel excel = new RicercaDataScadenzaProcEsecMAExcel();
		baos = excel.creaFoglioRicercaDataScadenzaProcEsecMA(rpm);

		setRequestAttribute("report", baos);
		setRequestAttribute(IWebConstants.DISPOSITION_FIELD, IWebConstants.ATTACHMENT_DISPOSITION_FILE);

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(getClass().getName() + " .processRequest: fine");

		// Pagina di download di ritorno
		return IWebConstants.PG_DOWNLOAD_DOCUMENT;
	}

}