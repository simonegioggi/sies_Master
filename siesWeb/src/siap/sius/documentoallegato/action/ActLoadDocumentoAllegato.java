package siap.sius.documentoallegato.action;

import java.io.ByteArrayOutputStream;

import f3b.web.IWebConstants;
import siap.sico.web.ActionSiap;
import siap.sius.documentoallegato.controller.IDocumentoAllegato;
import siap.sius.util.SIUSLookupRemote;

public class ActLoadDocumentoAllegato extends ActionSiap implements ICostantiDocumentoAllegato {

	public String processRequest() throws Exception {

		IDocumentoAllegato lCtrl = SIUSLookupRemote.getDocumentoAllegatoRemote();
		ByteArrayOutputStream lReport = lCtrl
				.ExGetDocumentoByKey(getRequestBigDecimalParameter(CAMPO_ID_DOCUMENTO_ALLEGATO));

		// Prepara la pagina di destinazione
		setRequestAttribute("report", lReport);

		return IWebConstants.PG_DOWNLOAD;
	}

}