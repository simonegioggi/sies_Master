package siap.sico.webservice.action;

import it.mig.sies.type.esecuzione.CHIAVIDocument;
import it.mig.sies.type.esecuzione.DATIOPERAZIONEDocument;
import it.mig.sies.type.esecuzione.DATIRISPOSTAESECUZIONEDocument;
import it.mig.sies.type.esecuzione.ESECUZIONEDocument;
import it.mig.sies.type.esecuzione.ESITODocument;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;

import org.apache.log4j.Logger;
import org.apache.xmlbeans.XmlOptions;

import siap.sico.trasmissione.controller.ITrasmissioni;
import siap.sico.trasmissione.model.TrasmissioniModel;
import siap.sico.utente.model.UtenteModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.webservice.controller.WebServicesController;
import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.F3BException;

@SuppressWarnings("rawtypes")
public class ActUDSRispostaEsecuzione extends ActWsBase {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public String processRequest(String flussoXmlRisposta, BigDecimal lAnnoFascicolo,
			BigDecimal lNumeroFascicolo, UtenteModel lUteMod) throws Exception {

		String lEsito = "";

		// Caricamento del flusso xml di Risposta NSC Provvedimenti Esecuzione UDS
		ESECUZIONEDocument lEseNsc = ESECUZIONEDocument.Factory.parse(flussoXmlRisposta);

		/***********************************************************************************/
		/* Validazione XML di risposta proveniente da NSC */
		/***********************************************************************************/

		ArrayList validationErrors = new ArrayList();
		XmlOptions m_validationOptions = new XmlOptions();
		m_validationOptions.setErrorListener(validationErrors);
		boolean isValid = lEseNsc.validate(m_validationOptions);

		if (isValid) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.info("Struttura XML di risposta NSC VALIDA!!!");
		} else {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.info("Struttura XML di risposta NSC NON VALIDA!!!");
			// IN CASO DI STRUTTURA NON VALIDA COSTRUIRE UNA RISPOSTA CON ERRORE DA INVIARE A SIES
			Iterator iter = validationErrors.iterator();
			while (iter.hasNext()) {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.error("====================================================================");
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.error("Errori rilevati durante la convalida del Flusso di Risposta NSC :");
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.error(">> ERRORE " + iter.next() + "\n");
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.error("====================================================================");

				// Se si vuole generare un Exception la riga sotto altrimenti gestire l'errore
				throw new Exception(
						"ActUDSRispostaEsecuzione - Struttura XML di Risposta NSC NON valida. Contattare il servizio di Help Desk");
			}
		}

		ESECUZIONEDocument.ESECUZIONE RootEsecuzione = lEseNsc.getESECUZIONE();
		DATIRISPOSTAESECUZIONEDocument.DATIRISPOSTAESECUZIONE DatiRispostaEsecuzione = RootEsecuzione
				.getDATIRISPOSTAESECUZIONE();
		CHIAVIDocument.CHIAVI DatiChiavi = DatiRispostaEsecuzione.getCHIAVI();
		ESITODocument.ESITO DatiEsito = DatiRispostaEsecuzione.getESITO();
		DATIOPERAZIONEDocument.DATIOPERAZIONE DatiOperazione = DatiRispostaEsecuzione.getDATIOPERAZIONE();

		if ("0".equals(DatiEsito.getCODICE().toString())) { // OK
			WebServicesController lWebServicesController = new WebServicesController();
			lWebServicesController.ExScritturaChiaviNscUDS(DatiChiavi, DatiOperazione, DatiEsito,
					lAnnoFascicolo, lNumeroFascicolo, lUteMod, DatiRispostaEsecuzione);
		} else { // KO
			RegistrazioneTrasmissione(DatiChiavi, DatiOperazione, DatiEsito, lAnnoFascicolo, lNumeroFascicolo,
					lUteMod);
		}

		lEsito = DatiEsito.getCODICE().toString();
		return lEsito;

	}

	private void RegistrazioneTrasmissione(CHIAVIDocument.CHIAVI adatiChiavi,
			DATIOPERAZIONEDocument.DATIOPERAZIONE adatiOperazione, ESITODocument.ESITO adatiEsito,
			BigDecimal lAnnoFascicolo, BigDecimal lNumeroFascicolo, UtenteModel lUteMod) throws F3BException {

		TrasmissioniModel lTrasmissioniModel = new TrasmissioniModel();

		lTrasmissioniModel.setTipoTrasmissione("02"); // Provvedimenti Principali
		lTrasmissioniModel.setDataTrasmissione(DateUtils.getSysDate());
		lTrasmissioniModel.setEsitoTrasmissione(adatiEsito.getCODICE().toString());
		lTrasmissioniModel.setCodErrore("");
		lTrasmissioniModel.setTipoOperazione(adatiOperazione.getOPERAZIONE().toString());
		lTrasmissioniModel.setDestinazione("NSC");

		if (adatiEsito.getCODICE().toString().equals("0")) {
			lTrasmissioniModel.setChiaveNscProv(new BigDecimal(adatiChiavi.getKPENSC()));
			lTrasmissioniModel.setChiaveNscSogg(new BigDecimal(adatiChiavi.getKANSC()));
			lTrasmissioniModel.setChiaveSiesFasc(new BigDecimal(adatiChiavi.getKPESIES()));
			lTrasmissioniModel.setChiaveSiesSogg(new BigDecimal(adatiChiavi.getKASIES()));
		}

		lTrasmissioniModel.setChiaveAnno(lAnnoFascicolo);
		lTrasmissioniModel.setChiaveProgr(lNumeroFascicolo);

		lTrasmissioniModel.setCodOperatoreInserimento(lUteMod.getUserId());
		lTrasmissioniModel.setDataInserimento(DateUtils.getSysDate());
		lTrasmissioniModel.setCodUfficioInserimento(lUteMod.getUfficioUtente().getCodUfficio());

		// Scrivo TRASMISSIONE
		ITrasmissioni lCtrlTrasmissioni = SICOLookupRemote.getTrasmissioniRemote();
		lCtrlTrasmissioni.ExInserisciTrasmissioni(lTrasmissioniModel);
	}

}