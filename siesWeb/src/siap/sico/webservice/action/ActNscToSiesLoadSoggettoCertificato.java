package siap.sico.webservice.action;

import it.mig.sies.type.ANAGRAFICADocument;
import it.mig.sies.type.CHIAVIDocument;
import it.mig.sies.type.DATIUTENTEDocument;

import java.io.ByteArrayInputStream;

import org.apache.log4j.Logger;

import siap.sico.soggettocertificato.model.SoggettoCertificatoModel;
import f3b.log.LogF3B;
import f3b.util.DateUtils;

public class ActNscToSiesLoadSoggettoCertificato extends ActWsBase {
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
	private String mCodUfficio = "";

	public ActNscToSiesLoadSoggettoCertificato(String aCodUfficio) {
		mCodUfficio = aCodUfficio;
	}

	public SoggettoCertificatoModel processRequest(ANAGRAFICADocument.ANAGRAFICA adatiAnagrafica,
			DATIUTENTEDocument.DATIUTENTE adatiUtente, CHIAVIDocument.CHIAVI aChiavi) throws Exception {
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.info("---------------------------------------------");
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.info("ActNscToSiesLoadSoggettoCertificato");
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.info("---------------------------------------------");

		SoggettoCertificatoModel lSoggCertModel = new SoggettoCertificatoModel();

		lSoggCertModel.setCodOperatoreInserimento("nsc-" + adatiUtente.getUSERNAME());
		lSoggCertModel.setDataInserimento(DateUtils.getSysDate());
		lSoggCertModel.setCodUfficioInserimento(mCodUfficio);
		lSoggCertModel.setCodOperatoreAggiornamento("nsc-" + adatiUtente.getUSERNAME());
		lSoggCertModel.setDataAggiornamento(DateUtils.getSysDate());
		lSoggCertModel.setCodUfficioAggiornamento(mCodUfficio);

		// Caricamento CERTIFICATO (Campo BLOB) nel Model
		if (adatiAnagrafica.getCERTIFICATO() != null
				&& !"".equals(adatiAnagrafica.getCERTIFICATO().toString())) {
			ByteArrayInputStream lByteArrayInput = new ByteArrayInputStream(adatiAnagrafica.getCERTIFICATO());
			lSoggCertModel.setDocBlobCertificato(lByteArrayInput);
		}

		return lSoggCertModel;
	}

}