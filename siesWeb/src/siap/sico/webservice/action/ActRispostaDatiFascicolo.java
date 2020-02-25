package siap.sico.webservice.action;

import it.mig.sies.type.CHIAVIDocument;
import it.mig.sies.type.DATIOPERAZIONEDocument;
import it.mig.sies.type.DATIRISPOSTATRASFERIMENTODocument;
import it.mig.sies.type.ESITODocument;
import it.mig.sies.type.OMONIMODocument;
import it.mig.sies.type.TRASFERIMENTODocument;

import java.io.ByteArrayInputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.apache.xmlbeans.XmlOptions;

import siap.sico.certificato_omonimi_nsc.controller.ICertificatoOmonimiNsc;
import siap.sico.certificato_omonimi_nsc.model.CertificatoOmonimiNscModel;
import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.decodifiche.model.ComuneModel;
import siap.sico.trasmissione.controller.ITrasmissioni;
import siap.sico.trasmissione.model.TrasmissioniModel;
import siap.sico.utente.model.UtenteModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.webservice.controller.IWebServices;
import siap.sico.webservice.controller.WebServicesController;
import siap.sico.webservice.model.OmonimiModel;
import siap.siep.SIEPException;
import f3b.log.LogF3B;
import f3b.model.DecodeModel;
import f3b.util.DateUtils;
import f3b.util.F3BException;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class ActRispostaDatiFascicolo extends ActWsBase {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public Vector processRequest(String flussoXmlRisposta, BigDecimal lAnnoFascicolo,
			BigDecimal lNumeroFascicolo, UtenteModel lUteMod) throws Exception {

		// Caricamento del flusso xml di Risposta
		TRASFERIMENTODocument lDocNsc = TRASFERIMENTODocument.Factory.parse(flussoXmlRisposta);

		Vector lListaOmonimi = new Vector();

		/***********************************************************************************/
		/* Validazione XML di risposta proveniente da NSC */
		/***********************************************************************************/

		ArrayList validationErrors = new ArrayList();
		XmlOptions m_validationOptions = new XmlOptions();
		m_validationOptions.setErrorListener(validationErrors);
		boolean isValid = lDocNsc.validate(m_validationOptions);

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
						"ActRispostaDatiFascicolo - Struttura XML di Risposta NSC NON valida. Contattare il servizio di Help Desk");
			}
		}

		/***********************************************************************************/
		/* FINE Validazione XML di risposta proveniente da NSC */
		/***********************************************************************************/

		// rootTrasf è un'istanza del flusso xml
		TRASFERIMENTODocument.TRASFERIMENTO rootRisp = lDocNsc.getTRASFERIMENTO();
		DATIRISPOSTATRASFERIMENTODocument.DATIRISPOSTATRASFERIMENTO datiRispostaTrasf = rootRisp
				.getDATIRISPOSTATRASFERIMENTO();
		CHIAVIDocument.CHIAVI datiChiavi = datiRispostaTrasf.getCHIAVI();
		ESITODocument.ESITO datiEsito = datiRispostaTrasf.getESITO();
		DATIOPERAZIONEDocument.DATIOPERAZIONE datiOperazione = datiRispostaTrasf.getDATIOPERAZIONE();

		Collection lNazioni = DecodificheManager.getInstance().getNazioni();

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.info("--------------------------------------------------------------------------");
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.info("Esito XML NSC di Risposta: " + datiEsito.getCODICE());
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.info("--------------------------------------------------------------------------");

		if (datiEsito.getCODICE().toString().equals("0")) // OK
		{

			/*
			 * Scrittura delle chiavi NSC su DB SIES ScritturaChiaviNsc(datiChiavi); Registrazione Dati
			 * Trasmissione RegistrazioneTrasmissione(datiChiavi,datiOperazione,datiEsito);
			 */

			// Scrittura delle chiavi NSC su DB SIES e Registrazione Dati Trasmissione
			WebServicesController lWebServicesController = new WebServicesController();
			lWebServicesController.ExScritturaChiaviNsc(datiChiavi, datiOperazione, datiEsito, lAnnoFascicolo,
					lNumeroFascicolo, lUteMod);
		} else {
			if (datiEsito.getCODICE().toString().equals("1")) // PRESENZA OMONIMI
			{
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.info("--------------------------------------------------------------------------");
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.info("Lunghezza Array OMONIMI:"
						+ datiRispostaTrasf.getArrayOmonimi().getOMONIMOArray().length);
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.info("--------------------------------------------------------------------------");

				for (int i = 0; i <= datiRispostaTrasf.getArrayOmonimi().getOMONIMOArray().length - 1; i++) {
					// Descrizione COMUNE di NASCITA
					ComuneModel lComuneModel = CercaDescrizioneComuneNascita(datiRispostaTrasf
							.getArrayOmonimi().getOMONIMOArray(i).getDATIANAGRAFICI().getCODILUOGONASCITA());

					// Descrizione STATO ESTERO di NASCITA
					String lDescNazione = "";
					if (!datiRispostaTrasf.getArrayOmonimi().getOMONIMOArray(i).getDATIANAGRAFICI()
							.getCODISTATOESTERONAS().equals(""))
						;
					{
						Iterator itx = lNazioni.iterator();
						while (itx.hasNext()) {
							DecodeModel ldecodeModel;
							ldecodeModel = (DecodeModel) itx.next();
							if (ldecodeModel.getCode().equals(datiRispostaTrasf.getArrayOmonimi()
									.getOMONIMOArray(i).getDATIANAGRAFICI().getCODISTATOESTERONAS())) {
								lDescNazione = ldecodeModel.getDescription();
							}
						}
					}

					// Scrivo CERTIFICATO (BLOB) nella Tabella di Appoggio CERTIFICATO_OMONIMI_NSC per
					// consentire la Visualizzazione del certificato
					CertificatoOmonimiNscModel lCertificatoOmonimiNscModel = new CertificatoOmonimiNscModel();
					lCertificatoOmonimiNscModel = RegistraCertificatoOmonimi(
							datiRispostaTrasf.getArrayOmonimi().getOMONIMOArray(i));

					OmonimiModel mOmonimiModel = new OmonimiModel();
					mOmonimiModel.setOMONIMODocument(datiRispostaTrasf.getArrayOmonimi().getOMONIMOArray(i));
					mOmonimiModel.setDescLuogoNascita(lComuneModel.getDescrizione());
					mOmonimiModel.setProvNascita(lComuneModel.getCodProvincia());
					mOmonimiModel.setDescStatoEstero(lDescNazione);
					mOmonimiModel.setIDCertificatoOmonimiNsc(
							lCertificatoOmonimiNscModel.getIdCertificatoOmonimi());

					lListaOmonimi.add(mOmonimiModel);
				}
			} else {
				// Non ci sono casi di OMONIMIA e la RISPOSTA è KO - Registrazione Dati Trasmissione
				RegistrazioneTrasmissione(datiChiavi, datiOperazione, datiEsito, lAnnoFascicolo,
						lNumeroFascicolo, lUteMod);
			}
		}

		return lListaOmonimi;
	}

	private ComuneModel CercaDescrizioneComuneNascita(String aCodIstatComuneNascita) throws Exception {

		IWebServices lCtrlComune = SICOLookupRemote.getWebServicesRemote();
		ComuneModel lComuneModel = new ComuneModel();

		try {
			lComuneModel = lCtrlComune.ExRicercaProvinciaSedeGiudiziaria(aCodIstatComuneNascita);
		} catch (SIEPException e) {
			throw e;
		}
		return lComuneModel;
	}

	private void RegistrazioneTrasmissione(CHIAVIDocument.CHIAVI adatiChiavi,
			DATIOPERAZIONEDocument.DATIOPERAZIONE adatiOperazione, ESITODocument.ESITO adatiEsito,
			BigDecimal lAnnoFascicolo, BigDecimal lNumeroFascicolo, UtenteModel lUteMod) throws F3BException {

		TrasmissioniModel lTrasmissioniModel = new TrasmissioniModel();

		lTrasmissioniModel.setTipoTrasmissione("01"); // Provvedimenti Principali
		lTrasmissioniModel.setDataTrasmissione(DateUtils.getSysDate());
		lTrasmissioniModel.setEsitoTrasmissione(adatiEsito.getCODICE().toString());
		lTrasmissioniModel.setCodErrore("");
		lTrasmissioniModel.setTipoOperazione(adatiOperazione.getOPERAZIONE().toString());
		lTrasmissioniModel.setDestinazione("NSC");

		if (adatiEsito.getCODICE().toString().equals("0")) {
			lTrasmissioniModel.setChiaveNscProv(new BigDecimal(adatiChiavi.getKPNSC()));
			lTrasmissioniModel.setChiaveNscSogg(new BigDecimal(adatiChiavi.getKANSC()));
			lTrasmissioniModel.setChiaveSiesFasc(new BigDecimal(adatiChiavi.getKPSIES()));
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

	private CertificatoOmonimiNscModel RegistraCertificatoOmonimi(OMONIMODocument.OMONIMO aOmonimo)
			throws F3BException {

		ICertificatoOmonimiNsc lCtrlCertOmonimiNsc = SICOLookupRemote.getCertificatoOmonimiNscRemote();

		// Dato che CERTIIFCATO_OMONIMI_NSC è una tabella di appoggio cancello tutti i record antecedenti alla
		// data di sistema.
		lCtrlCertOmonimiNsc.ExCancellaCertificatoOmonimiNscByDate();

		CertificatoOmonimiNscModel lCertificatoOmonimiNscModel = new CertificatoOmonimiNscModel();
		lCertificatoOmonimiNscModel.setDataInserimento(DateUtils.getSysDate());

		// Scrivo CERTIFICATO_OMONIMI_NSC solo con la data per fare successivamente l'update del campo BLOB
		lCertificatoOmonimiNscModel = lCtrlCertOmonimiNsc
				.ExInserisciCertificatoOmonimiNsc(lCertificatoOmonimiNscModel);

		// Caricamento CERTIFICATO (Campo BLOB) nel Model
		if (aOmonimo.getCERTIFICATO() != null && !"".equals(aOmonimo.getCERTIFICATO().toString())) {
			ByteArrayInputStream lByteArrayInput = new ByteArrayInputStream(aOmonimo.getCERTIFICATO());
			lCertificatoOmonimiNscModel.setDocBlobCertificato(lByteArrayInput);
			// Effettuo UPDATE del campo CERTIFICATO (BLOB)
			lCtrlCertOmonimiNsc.ExModificaCertificatoOmonimiNsc(lCertificatoOmonimiNscModel);
		}

		return lCertificatoOmonimiNscModel;
	}

}