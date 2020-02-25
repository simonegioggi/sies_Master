package siap.sico.soggettodattilo.action;

import java.io.ByteArrayOutputStream;

import org.apache.log4j.Logger;

import siap.sico.soggettodattilo.controller.ISoggettoDattilo;
import siap.sico.soggettodattilo.model.SoggettoDattiloModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import f3b.log.LogF3B;
import f3b.util.F3BException;
import f3b.web.IWebConstants;

/**
* <p>Title: ActDownloadSoggettoDattilo</p>
* <p>Description: Classe Action per l'inserimento di SoggettoDattilo</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/
public class ActDownloadSoggettoDattilo extends ActionSiap implements ICostantiSoggettoDattilo {
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	/**
	* Azione di Download Documento del SoggettoDattilo
	* @return Nome della pagina JSP da visualizzare
	* al termine dell'elaborazione
	* @throws F3BException
	*/
	public String processRequest() throws Exception {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
		siesLogger.info(" START ");

		// imposta il model
		SoggettoDattiloModel lSogMod = new SoggettoDattiloModel();
		if (!isRequestParameterNullObj(CAMPO_ID_DATTILO)) {
			lSogMod.setIdDattilo(getRequestBigDecimalParameter(CAMPO_ID_DATTILO));
		}

		// chiama il controller
		ISoggettoDattilo lCtrl = SICOLookupRemote.getSoggettoDattiloRemote();
		ByteArrayOutputStream lReport = lCtrl.ExGetDocumento(lSogMod);
		setRequestAttribute("report", lReport);

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
		siesLogger.info("Report Size : " + lReport.size());
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
		siesLogger.info(" END ");

		return IWebConstants.PG_DOWNLOAD_DOCUMENT;
	}

}