package siap.siep.pagoPA.action;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.web.IWebConstants;
import siap.sico.web.ActionSiap;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.pagoPA.controller.IBollettinoPagopa;
import siap.siep.util.SIEPLookupRemote;

/**
 * MEV_2023-13: aggiunta classe di download avviso pagoPA
 *
 * @author sgioggi
 * @version 1.0
 */
public class ActLoadAvvisoPagoPA extends ActionSiap {

	// info per il log dedicato
	private static Logger siesLogger = Logger.getLogger(LogF3B.WS_PAGO_PA_LOG);

	public String processRequest() throws Exception {

		// info per il log
		siesLogger.debug(getClass().getName() + ".processRequest: inizio");

		FascicoloSiepModel fsm = (FascicoloSiepModel) getSessionAttribute("fascicolo");
		BigDecimal idBollettinoPagopa = getRequestBigDecimalParameter("idBollettinoPagopa");

		IBollettinoPagopa ibp = SIEPLookupRemote.getBollettinoPagopaRemote();
		ByteArrayOutputStream baos = ibp.ExGetBollettino(idBollettinoPagopa);

		// Prepara la pagina di destinazione
		setRequestAttribute("report", baos);
		setRequestAttribute("idFascicolo", fsm.getIdFascicoloSiep());

		// info per il log
		siesLogger.debug(getClass().getName() + ".processRequest: fine");

		// Per VISUALIZZAZIONE file PDF
		return IWebConstants.PG_DOWNLOAD_PDF;
	}

}