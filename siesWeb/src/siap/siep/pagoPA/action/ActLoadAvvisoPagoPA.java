package siap.siep.pagoPA.action;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;

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

	public String processRequest() throws Exception {

		FascicoloSiepModel fsm = (FascicoloSiepModel) getSessionAttribute("fascicolo");
		BigDecimal idBollettinoPagopa = getRequestBigDecimalParameter("idBollettinoPagopa");

		IBollettinoPagopa ibp = SIEPLookupRemote.getBollettinoPagopaRemote();
		ByteArrayOutputStream baos = ibp.ExGetBollettino(idBollettinoPagopa);

		// Prepara la pagina di destinazione
		setRequestAttribute("report", baos);
		setRequestAttribute("idFascicolo", fsm.getIdFascicoloSiep());
		// Per VISUALIZZAZIONE file PDF
		return IWebConstants.PG_DOWNLOAD_PDF;
	}

}