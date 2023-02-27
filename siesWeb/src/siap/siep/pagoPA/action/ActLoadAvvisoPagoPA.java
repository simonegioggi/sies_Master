package siap.siep.pagoPA.action;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;

import f3b.web.IWebConstants;
import siap.sico.web.ActionSiap;
import siap.siep.fascicolo.controller.IFascicoloSiep;
import siap.siep.util.SIEPLookupRemote;
import siap.sius.fascicolo.controller.IFascicoloSius;
import siap.sius.util.SIUSLookupRemote;

/**
 * MEV_2023-13: aggiunta classe di download avviso pagoPA
 * 
 * @author sgioggi
 * @version 1.0
 */
public class ActLoadAvvisoPagoPA extends ActionSiap {

	public String processRequest() throws Exception {

		BigDecimal idFascicolo = getRequestBigDecimalParameter("IDFascicolo");
		String tipoFascicolo = getRequestStringParameter("TipoFascicolo");
		ByteArrayOutputStream baos = null;

		if (tipoFascicolo != null && tipoFascicolo.equals("SIEP")) {
			IFascicoloSiep ifsp = SIEPLookupRemote.getFascicoloSiepRemote();
			baos = ifsp.ExGetCertificatoPenale(idFascicolo);
		} else {
			IFascicoloSius ifss = SIUSLookupRemote.getFascicoloSiusRemote();
			baos = ifss.ExGetCertificatoPenale(idFascicolo);
		}

		// Prepara la pagina di destinazione
		setRequestAttribute("report", baos);
		setRequestAttribute("idFascicolo", idFascicolo);
		// Per VISUALIZZAZIONE file PDF
		return IWebConstants.PG_DOWNLOAD_PDF;
	}

}