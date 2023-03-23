package siap.siep.sanzionesostitutiva.action;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Iterator;
import java.util.Vector;

import org.apache.log4j.Logger;

import com.lowagie.text.DocumentException;

import f3b.log.LogF3B;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
import siap.sico.web.ActionSiap;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.pagoPA.controller.IBollettinoPagopa;
import siap.siep.pagoPA.model.BollettinoPagopaModel;
import siap.siep.util.SIEPLookupRemote;

public class ActStampaMassivaBollettini extends ActionSiap {

	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public String processRequest() throws F3BException, IOException, DocumentException {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.info(getClass().getName() + ".processRequest: inizio");

		// recupero il fascicolo dalla sessione
		FascicoloSiepModel fsm = (FascicoloSiepModel) getSessionAttribute("fascicolo");
		BigDecimal idFascicolo = fsm.getIdFascicoloSiep();

		IBollettinoPagopa ibp = SIEPLookupRemote.getBollettinoPagopaRemote();
		Vector<BollettinoPagopaModel> elencoStatoPagamenti = ibp
				.ExRicercaBollettinoPagopaByFasSieIdFascicoloSiep(idFascicolo);
		Iterator<BollettinoPagopaModel> iterBPM = elencoStatoPagamenti.iterator();
		ByteArrayOutputStream baosSingolo = null;
		ByteArrayOutputStream baosMassivo = new ByteArrayOutputStream();
		while (iterBPM.hasNext()) {
			BollettinoPagopaModel bpm = iterBPM.next();
			baosSingolo = ibp.ExGetBollettino(bpm.getIdBollettinoPagopa());
			baosMassivo.write(baosSingolo.toByteArray());
		}
		baosMassivo.close();

		// ==============================================
		// Setta il documento di stampa sulla response
		// ==============================================
		setRequestAttribute("report", baosMassivo);

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.info(getClass().getName() + ".processRequest: fine");

		// valore di ritorno
		return IWebConstants.PG_DOWNLOAD_PDF;
	}

}