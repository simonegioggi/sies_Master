package siap.sico.webservice.action;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;

import org.apache.xmlbeans.impl.util.Base64;

import f3b.web.IWebConstants;
import siap.sico.soggetto.action.ICostantiSoggetto;
import siap.sico.soggettocertificato.controller.ISoggettoCertificato;
import siap.sico.soggettocertificato.model.SoggettoCertificatoModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;

public class ActLoadCertificatoSoggetto extends ActionSiap implements ICostantiSoggetto {

	public String processRequest() throws Exception {

		// 20261021 [SG]: modificato parametro proveniente dalla REQUEST
		// BigDecimal lId = getRequestBigDecimalParameter("IDSoggetto");
		BigDecimal idSoggetto = getRequestBigDecimalParameter("IdSoggetto");
		SoggettoCertificatoModel scm = new SoggettoCertificatoModel();
		scm.setSogIdSoggetto(idSoggetto);

		ISoggettoCertificato isc = SICOLookupRemote.getSoggettoCertificatoRemote();
		ByteArrayOutputStream baos = isc.ExGetDocumento(scm);

		// decodifica in Base 64
		byte[] certificato = Base64.decode(baos.toByteArray());
		// Trasformiamo in ByteArray
		ByteArrayOutputStream certificato_ByteArray = new ByteArrayOutputStream();
		certificato_ByteArray.write(certificato);

		// Prepara la pagina di destinazione
		setRequestAttribute("report", certificato_ByteArray);
		// Per VISUALIZZAZIONE file PDF
		return IWebConstants.PG_DOWNLOAD_PDF;
	}

}