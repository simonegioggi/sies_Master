package siap.siep.sanzionesostitutiva.action;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;

import f3b.web.IWebConstants;
import siap.sico.web.ActionSiap;
import siap.siep.rinnovo.action.ICostantiRinnovo;
import siap.siep.rinnovo.controller.IRinnovo;
import siap.siep.rinnovo.model.RinnovoModel;
import siap.siep.util.SIEPLookupRemote;

public class ActLoadDocumentoRinnovoRicercheOIPP extends ActionSiap implements ICostantiRinnovo {

	public String processRequest() throws Exception {

		BigDecimal lId = getRequestBigDecimalParameter(ICostantiRinnovo.CAMPO_ID_RINNOVO);

		IRinnovo lCtrl = SIEPLookupRemote.getRinnovoRemote();
		RinnovoModel lRinModel = lCtrl.ExRicercaRinnovoByKey(lId);

		ByteArrayOutputStream lReport = lCtrl.ExGetDocumento(lRinModel);

		// Prepara la pagina di destinazione
		setRequestAttribute("report", lReport);

		// return IWebConstants.PG_DOWNLOAD; // 20100409 - sostituita pernuova implementazione lib TIKA
		return IWebConstants.PG_DOWNLOAD_DOCUMENT;
	}

}