package siap.sico.webservice.action;

import it.mig.sies.type.ArrayChiaviReatiDocument;
import it.mig.sies.type.CHIAVIDocument;
import it.mig.sies.type.DATIFASCICOLODocument;
import it.mig.sies.type.DATIOPERAZIONEDocument;
import it.mig.sies.type.DATIRISPOSTATRASFERIMENTODocument;
import it.mig.sies.type.ENTITADocument;
import it.mig.sies.type.ESITODocument;
import it.mig.sies.type.OPERAZIONEDocument;
import it.mig.sies.type.TRASFERIMENTODocument;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.apache.xmlbeans.XmlOptions;

import siap.sico.trasmissione.controller.ITrasmissioni;
import siap.sico.trasmissione.model.TrasmissioniModel;
import siap.sico.util.SICOLookupRemote;
//import siap.sico.web.ActionSiap;
import siap.sico.webservice.model.DatiNscToSiesModel;
import siap.siep.reato.model.ReatoModel;
import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.F3BException;

public class ActNscToSiesRisposta {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	@SuppressWarnings("rawtypes")
	/**
	 * MEV 16: aggiunto parametro di passaggio per gestire fascicolo coinvolto nel cumulo
	 * 
	 * @param aDatiNscToSiesModel
	 * @param aCodiceEsito
	 * @param lCodOperatoreIns
	 * @param lCodUfficioIns
	 * @param idFascicoloSiep
	 * @param progrFascSiep
	 * @param annoFascSiep
	 * @param idSoggettoSiep
	 * @return String
	 * @throws Exception
	 */
	public String processRequest(DatiNscToSiesModel aDatiNscToSiesModel, String aCodiceEsito,
			String lCodOperatoreIns, String lCodUfficioIns, String idFascicoloSiep, String progrFascSiep,
			String annoFascSiep, String idSoggettoSiep) throws Exception {

//		siesLogger.debug("ActNscToSiesRisposta: aDatiNscToSiesModel = "+aDatiNscToSiesModel);
//		siesLogger.debug("ActNscToSiesRisposta: aCodiceEsito = "+aCodiceEsito);
//		siesLogger.debug("ActNscToSiesRisposta: lCodOperatoreIns = "+lCodOperatoreIns);
//		siesLogger.debug("ActNscToSiesRisposta: lCodUfficioIns = "+lCodUfficioIns);
//		siesLogger.debug("ActNscToSiesRisposta: idFascicoloSiep = "+idFascicoloSiep);
//		siesLogger.debug("ActNscToSiesRisposta: progrFascSiep = "+progrFascSiep);
//		siesLogger.debug("ActNscToSiesRisposta: annoFascSiep = "+annoFascSiep);
//		siesLogger.debug("ActNscToSiesRisposta: idSoggettoSiep = "+idSoggettoSiep);
		
		
		TRASFERIMENTODocument lxmlRisp = TRASFERIMENTODocument.Factory.newInstance();
		try {
			BigDecimal lAnnoFascicolo = null;
			BigDecimal lNumeroFascicolo = null;

			// TRASFERIMENTO
			TRASFERIMENTODocument.TRASFERIMENTO ElementTrasf = lxmlRisp.addNewTRASFERIMENTO();

			// ELEMENTO DATI_RISPOSTA_TRASFERIMENTO
			DATIRISPOSTATRASFERIMENTODocument.DATIRISPOSTATRASFERIMENTO datiRisp = ElementTrasf
					.addNewDATIRISPOSTATRASFERIMENTO();

			// ELEMENTO CHIAVI
			CHIAVIDocument.CHIAVI tagChiavi = datiRisp.addNewCHIAVI();
			ArrayChiaviReatiDocument.ArrayChiaviReati ArrayChiaviReati = null;

			// ELEMENTO ESITO
			ESITODocument.ESITO tagEsito = datiRisp.addNewESITO();
			if (aCodiceEsito.equals("0")) {// OK PROCEDIENTO INSERITO
				tagEsito.setCODICE(ESITODocument.ESITO.CODICE.X_0);
				tagEsito.setDESCRIZIONE(ESITODocument.ESITO.DESCRIZIONE.OK);
			} else if (aCodiceEsito.equals("2")) {// PROCEDIMENTO PRESENTE
				tagEsito.setCODICE(ESITODocument.ESITO.CODICE.X_2);
				tagEsito.setDESCRIZIONE(ESITODocument.ESITO.DESCRIZIONE.TITOLO_PRESENTE);
			} else if (aCodiceEsito.equals("100")) {// ERRORE GENERICO
				tagEsito.setCODICE(ESITODocument.ESITO.CODICE.X_100);
				tagEsito.setDESCRIZIONE(ESITODocument.ESITO.DESCRIZIONE.ERRORE_INTERNO_GENERICO);
			} else {
				tagEsito.setCODICE(ESITODocument.ESITO.CODICE.X_200);
				tagEsito.setDESCRIZIONE(ESITODocument.ESITO.DESCRIZIONE.ERRORE_SISTEMA_GENERICO);
			}

			// DATI OPERAZIONE
			DATIOPERAZIONEDocument.DATIOPERAZIONE tagDatiOper = datiRisp.addNewDATIOPERAZIONE();
			tagDatiOper.setOPERAZIONE(OPERAZIONEDocument.OPERAZIONE.INSERT);
			tagDatiOper.setENTITA(ENTITADocument.ENTITA.P);

			// MEV 16: aggiunti controlli per gestire l'inserimento di fascicolo coinvolto in cumulo
			if (idFascicoloSiep != null && idFascicoloSiep.length() > 0) {
				lAnnoFascicolo = new BigDecimal(annoFascSiep);
				lNumeroFascicolo = new BigDecimal(progrFascSiep);
			} else {
				//siesLogger.debug("ActNscToSiesRisposta: getFascicoloSiepModel() = "+aDatiNscToSiesModel.getFascicoloSiepModel());
				lAnnoFascicolo = aDatiNscToSiesModel.getFascicoloSiepModel().getChiaveAnno();
				lNumeroFascicolo = aDatiNscToSiesModel.getFascicoloSiepModel().getChiaveProgr();
			}

			// ESITO OK
			if (aCodiceEsito.equals("0")) {
				// MEV 16: aggiunti controlli per gestire l'inserimento di fascicolo coinvolto in cumulo
				if (idSoggettoSiep != null && idSoggettoSiep.length() > 0)
					tagChiavi.setKASIES(new Long(idSoggettoSiep).longValue());
				else
					tagChiavi.setKASIES(aDatiNscToSiesModel.getSoggettoModel().getIdSoggetto().longValue());
				if (idFascicoloSiep != null && idFascicoloSiep.length() > 0)
					tagChiavi.setKPSIES(new Long(idFascicoloSiep).longValue());
				else
					tagChiavi.setKPSIES(aDatiNscToSiesModel.getFascicoloSiepModel().getIdFascicoloSiep()
							.longValue());
				tagChiavi.setKANSC(aDatiNscToSiesModel.getSoggettoModel().getKeySoggNsc().longValue());
				tagChiavi.setKPNSC(aDatiNscToSiesModel.getFascicoloSiepModel().getKeyProvvNsc().longValue());

				// Valorizzo gli elementi identificativi del Fascicolo inserito per il messaggio utente da
				// Vis. su NSC
				DATIFASCICOLODocument.DATIFASCICOLO DatiFascicolo = datiRisp.addNewDATIFASCICOLO();
				DatiFascicolo.setANNOFASCICOLO(lAnnoFascicolo.intValue());
				DatiFascicolo.setNUMEROFASCICOLO(lNumeroFascicolo.toString());

				Vector lListaReati = aDatiNscToSiesModel.getReatoModel();

				for (int i = 0; i <= lListaReati.size() - 1; i++) {
					if (i == 0) {
						ArrayChiaviReati = tagChiavi.addNewArrayChiaviReati();
					}

					ReatoModel lReatoModel = (ReatoModel) lListaReati.elementAt(i);
					// Identifico il REATO passato da NSC e probabilmente girato in SIES quindi diventato
					// CIRCOSTANZA
					// affogata nel REATO SIES (110 56 81)
					// if (lReatoModel.getProgrCircostanza().intValue() == 1)
					if (lReatoModel.getKeyReatoNsc() != null) {
						ArrayChiaviReatiDocument.ArrayChiaviReati.CHIAVIREATO ChiaviReato = ArrayChiaviReati
								.addNewCHIAVIREATO();
						// MEV 16: aggiunti controlli per gestire l'inserimento di fascicolo coinvolto in
						// cumulo
						if (lReatoModel.getIdReato() != null)
							ChiaviReato.setKRSIES(lReatoModel.getIdReato().longValue());
						ChiaviReato.setKRNSC(lReatoModel.getKeyReatoNsc().longValue());
					}
				}
			}

			// Registrazione Dati Trasmissione
			RegistrazioneTrasmissione(tagChiavi, tagDatiOper, tagEsito, lAnnoFascicolo, lNumeroFascicolo,
					lCodOperatoreIns, lCodUfficioIns);
		} catch (Exception e) {
			e.printStackTrace();
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("--------------------------------------------------");
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("ActNscToSiesRisposta - Errore nella Risposta a NSC",e);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error(e.getStackTrace());
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("--------------------------------------------------");
			// ----> Non genero l'exception quindi prosegue con l'invio del flusso di risposta, eventualmente
			// non verrà
			// validato.
			// throw new
			// Exception("ActNscToSiesRisposta - Procedimento Trasferito su SIES ma c'è un errore generico. Contattare il servizio di Help Desk");
		}

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.info("====================================================================");
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.info("Flusso XML SIES di Risposta:" + lxmlRisp.xmlText());
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.info("====================================================================");

		/**************************************************************************/
		/* Validazione XML di Risposta di SIES a NSC */
		/**************************************************************************/

		ArrayList validationErrors = new ArrayList();
		XmlOptions m_validationOptions = new XmlOptions();
		m_validationOptions.setErrorListener(validationErrors);
		boolean isValid = lxmlRisp.validate(m_validationOptions);

		if (isValid) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.info("Struttura XML di risposta SIES VALIDA!!!");
		} else {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.info("Struttura XML di risposta SIES NON VALIDA!!!");

			Iterator iter = validationErrors.iterator();
			while (iter.hasNext()) {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.error("====================================================================");
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.error("Errori rilevati durante la convalida del Flusso di Risposta SIES :");
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.error(">> ERRORE " + iter.next() + "\n");
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.error("====================================================================");

				// Se si vuole generare un Exception la riga sotto altrimenti gestire l'errore
				throw new Exception(
						"ActNscToSiesRisposta - Struttura XML di risposta SIES NON valida. Contattare il servizio di Help Desk");

			}
		}
		/**************************************************************************/
		/* Fine Validazione XML proveniente da NSC */
		/**************************************************************************/
		// valore di ritorno
		return lxmlRisp.toString();
	}

	private void RegistrazioneTrasmissione(CHIAVIDocument.CHIAVI adatiChiavi,
			DATIOPERAZIONEDocument.DATIOPERAZIONE adatiOperazione, ESITODocument.ESITO atagEsito,
			BigDecimal lAnnoFascicolo, BigDecimal lNumeroFascicolo, String lCodOperatoreInserimento,
			String lCodUfficioInserimento) throws F3BException {

		TrasmissioniModel lTrasmissioniModel = new TrasmissioniModel();

		lTrasmissioniModel.setTipoTrasmissione("01"); // Provvedimenti Principali
		lTrasmissioniModel.setDataTrasmissione(DateUtils.getSysDate());
		lTrasmissioniModel.setEsitoTrasmissione(atagEsito.getCODICE().toString());
		lTrasmissioniModel.setCodErrore("");

		lTrasmissioniModel.setTipoOperazione(adatiOperazione.getOPERAZIONE().toString());
		lTrasmissioniModel.setDestinazione("SIES");
		if (atagEsito.getCODICE().toString().equals("0")) {
			lTrasmissioniModel.setChiaveNscProv(new BigDecimal(adatiChiavi.getKPNSC()));
			lTrasmissioniModel.setChiaveNscSogg(new BigDecimal(adatiChiavi.getKANSC()));
			lTrasmissioniModel.setChiaveSiesFasc(new BigDecimal(adatiChiavi.getKPSIES()));
			lTrasmissioniModel.setChiaveSiesSogg(new BigDecimal(adatiChiavi.getKASIES()));
		}

		lTrasmissioniModel.setChiaveAnno(lAnnoFascicolo);
		lTrasmissioniModel.setChiaveProgr(lNumeroFascicolo);
		lTrasmissioniModel.setCodOperatoreInserimento("nsc-" + lCodOperatoreInserimento);
		lTrasmissioniModel.setDataInserimento(DateUtils.getSysDate());
		lTrasmissioniModel.setCodUfficioInserimento(lCodUfficioInserimento);

		// Scrivo TRASMISSIONE
		ITrasmissioni lCtrlTrasmissioni = SICOLookupRemote.getTrasmissioniRemote();
		lCtrlTrasmissioni.ExInserisciTrasmissioni(lTrasmissioniModel);
	}

}