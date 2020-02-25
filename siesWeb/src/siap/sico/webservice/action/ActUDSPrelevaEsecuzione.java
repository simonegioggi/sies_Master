package siap.sico.webservice.action;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.apache.xmlbeans.XmlOptions;

import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.util.StringUtils;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;
import it.mig.sies.type.esecuzione.ArrayChiaviProvvGiudiziariDocument;
import it.mig.sies.type.esecuzione.ArrayChiaviProvvGiudiziariDocument.ArrayChiaviProvvGiudiziari.CHIAVIPROVGIUDIZIARI;
import it.mig.sies.type.esecuzione.ArrayPeriodoLADocument;
import it.mig.sies.type.esecuzione.CHIAVIDocument;
import it.mig.sies.type.esecuzione.DATA;
import it.mig.sies.type.esecuzione.DATICHIAMATAESECUZIONEDocument;
import it.mig.sies.type.esecuzione.DATIOPERAZIONEDocument;
import it.mig.sies.type.esecuzione.DATIPROVVEDIMENTODocument;
import it.mig.sies.type.esecuzione.DATIUFFICIODocument;
import it.mig.sies.type.esecuzione.DATIUTENTEDocument;
import it.mig.sies.type.esecuzione.DURATA;
import it.mig.sies.type.esecuzione.ENTITADocument;
import it.mig.sies.type.esecuzione.ESECUZIONEDocument;
import it.mig.sies.type.esecuzione.OPERAZIONEDocument;
import it.mig.sies.type.esecuzione.PERIODOLADocument;
import it.mig.sies.type.esecuzione.PROVVUDSDocument;
import siap.sico.codici_sies_nsc.model.CodiciSiesNscModel;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoModel;
import siap.sico.libertaanticipata.controller.ILicenzaPeriodiLibAnticipata;
import siap.sico.libertaanticipata.model.LicenzaPeriodiLibAnticipataModel;
import siap.sico.provvedimentisiesnsc.model.ProvvSiesNscModel;
import siap.sico.security.action.ICostantiSecurity;
import siap.sico.soggetto.model.SoggettoModel;
import siap.sico.utente.model.UtenteModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.webservice.config.NscProperties;
import siap.siep.SIEPException;
import siap.siep.fascicolo.controller.IFascicoloSiep;
import siap.siep.fascicolo.model.DettaglioFascicoloModel;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.penapecuniaria.controller.IRichiestaConversione;
import siap.siep.penapecuniaria.model.RichiestaConversioneModel;
import siap.siep.util.SIEPLookupRemote;
import siap.sius.depositodecreto.controller.IDepositoDecreto;
import siap.sius.depositodecreto.model.DepositoDecretoModel;
import siap.sius.depositoordinanzapc.controller.IDepositoOrdinanzaPc;
import siap.sius.depositoordinanzapc.model.DepositoOrdinanzaPcModel;
import siap.sius.fascicolo.controller.FascicoloSiusController;
import siap.sius.fascicolo.model.FascicoloGPModel;
import siap.sius.util.SIUSLookupRemote;
import siap.util.SIAPPathProperties;

@SuppressWarnings("rawtypes")
public class ActUDSPrelevaEsecuzione extends ActWsBase {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	CodiciSiesNscModel lCodiciSiesNscModel;
	String lCodCentralizzato = "";
	BigDecimal lIdEvento;
	BigDecimal lIdDocAllegato;
	private NscProperties mProperties = NscProperties.getInstance();
	DATA lData = DATA.Factory.newInstance();
	DURATA lDurata = DURATA.Factory.newInstance();

	// NUOVA INFRASTRUTTURA: aggiunte variabili di classe
	private SIAPPathProperties mPathProperties = SIAPPathProperties.getInstance();
	private String mPath = null;

	public String processRequest() throws Exception {

		if (isRequestParameterNullObj("IdEvento") || isRequestParameterNullObj("IdDocumentoAllegato")) {
			throw new Exception(
					"ActPrelevaEseUDS - IdEvento non presente. Contattare il servizio di Help Desk");
		}

		lIdEvento = getRequestBigDecimalParameter("IdEvento");
		lIdDocAllegato = getRequestBigDecimalParameter("IdDocumentoAllegato");
		// Evento
		EventoModel lEvento = CercaEvento(lIdEvento);
		// Fascicolo SIUS
		FascicoloGPModel lFascicoloGPModel = CercaFascicoloSISU(lEvento.getFasSiuIdFascicoloSius());

		// Fascicolo SIEP
		IFascicoloSiep lCtrlFas = SIEPLookupRemote.getFascicoloSiepRemote();
		DettaglioFascicoloModel lDettaglio = lCtrlFas.ExDettaglioFascicoloSiep(
				lFascicoloGPModel.getFascicoloSiusModel().getFasSieIdFascicoloSiep());
		FascicoloSiepModel lFascicoloSIEP = lDettaglio.getFascicoloSiep();

		SoggettoModel lSoggetto = lDettaglio.getFascicoloSiep().getSoggetto();

		UtenteModel lUteMod = new UtenteModel(
				(UtenteModel) getSessionAttribute(ICostantiSecurity.SESSION_UTENTE_CONNESSO));

		/*------------------------------------------------------------------------------*/
		/* Inizio Scrittura XML */
		/*------------------------------------------------------------------------------*/
		ESECUZIONEDocument lEseDoc = ESECUZIONEDocument.Factory.newInstance();
		// Root Element
		ESECUZIONEDocument.ESECUZIONE ElementEsec = lEseDoc.addNewESECUZIONE();
		// Element DATI_CHIAMATA_ESECUZIONE
		DATICHIAMATAESECUZIONEDocument.DATICHIAMATAESECUZIONE DatiChiamataEsec = ElementEsec
				.addNewDATICHIAMATAESECUZIONE();

		ScriviDatiUtente_Ufficio(DatiChiamataEsec, lUteMod);

		ScriviChiavi(DatiChiamataEsec, lFascicoloGPModel, lFascicoloSIEP, lSoggetto, lEvento);

		ScriviDatiOperazione(DatiChiamataEsec, lEvento);

		ScriviDatiProvvedimento(DatiChiamataEsec, lEvento, lFascicoloGPModel);

		String flussoDatiXML = mProperties.getProperty("FlussoDatiEseUDS");
		if (!StringUtils.checkValidValue(flussoDatiXML)) {
			// NUOVA INFRASTRUTTURA: cambiato il path per differenziare macchina windows da macchina UNIX
			mPath = mPathProperties.getProperty("FLUSSOESEUDS");
			flussoDatiXML = mPath;
		}
		lEseDoc.save(new File(flussoDatiXML));

		/***********************************************************************************/
		/* Validazione XML generato prima dell'invio a NSC */
		/***********************************************************************************/

		ArrayList validationErrors = new ArrayList();
		XmlOptions m_validationOptions = new XmlOptions();
		m_validationOptions.setErrorListener(validationErrors);
		boolean isValid = lEseDoc.validate(m_validationOptions);

		if (isValid) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.info("Struttura XML VALIDA!!!" + lEseDoc.xmlText());
		} else {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.info("Struttura XML NON E' VALIDA!!!");

			Iterator iter = validationErrors.iterator();
			while (iter.hasNext()) {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.error("====================================================================");
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.error("Errori rilevati durante la convalida:");
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.error(">> ERRORE " + iter.next() + "\n");
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.error("====================================================================");

				// Se si vuole generare un Exception la riga sotto altrimenti gestire l'errore
				// throw new Exception("ActPrelevaDatiFascicolo - Struttura XML non valida");

				setRequestAttribute(IWebConstants.MESSAGE_TEXT,
						"ActUDSPrelevaEsecuzione - Struttura XML non valida. Contattare il servizio di Help Desk");
				RedirectTo lRedirigi = new RedirectTo();
				lRedirigi.setPage(IWebConstants.PG_MAIN);
				if (lEvento.getCodTipoProvvedimento() != null
						&& lEvento.getCodTipoProvvedimento().equals("02")) {
					// DECRETO
					lRedirigi.setAction(
							"siap.sius.depositodecreto.action.ActLoadDettaglioDataDepositoDecreto&IdDocumentoAllegato="
									+ lIdDocAllegato);
				} else { // ORDINANZA
					lRedirigi.setAction(
							"siap.sius.depositoordinanzapc.action.ActLoadDettaglioDataDepositoOrdinanza&IdDocumentoAllegato="
									+ lIdDocAllegato);
				}

				setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi);
				return IWebConstants.PG_MESSAGE;
			}
		}

		/***********************************************************************************/
		/* Chiamo il Servizio (Web Service) del Casellario passando il flusso XML generato */
		/***********************************************************************************/

		// ???????????? Generare client WS con WSDL generato da NSC ??????????????????????

		// String lNscWsAddress=mProperties.getProperty("NscWsAddress");

		// IscriviProvvedimentoProvvisorio service = new IscriviProvvedimentoProvvisorioLocator();
		// IscriviProvvedimentoProvvisorio service = new
		// IscriviProvvedimentoProvvisorioLocator(lNscWsAddress);
		// IscriviProvvedimentoProvvisorioPort port = service.getIscriviProvvedimentoProvvisorioPort();
		String rispostaFlussoXmlNsc = "";
		// rispostaFlussoXmlNsc = port.iscriviProvvedimentoProvvisorio(lTrasfDoc.xmlText());

		/***********************************************************************************/
		/* Istanzio classe che gestice la Risposta di NSC */
		/***********************************************************************************/

		ActUDSRispostaEsecuzione objRispostaNsc = new ActUDSRispostaEsecuzione();
		String lEsito = objRispostaNsc.processRequest(rispostaFlussoXmlNsc,
				lFascicoloGPModel.getFascicoloSiusModel().getChiaveAnno(),
				lFascicoloGPModel.getFascicoloSiusModel().getChiaveProgr(), lUteMod);

		if (lEsito.equals("0")) {
			setRequestAttribute(IWebConstants.MESSAGE_TEXT,
					"Il Provvedimento SIUS N:" + lFascicoloGPModel.getFascicoloSiusModel().getChiaveAnno()
							+ "/" + lFascicoloGPModel.getFascicoloSiusModel().getChiaveProgr()
							+ " trasferito con successo.");
		} else if (lEsito.equals("2")) {
			setRequestAttribute(IWebConstants.MESSAGE_TEXT,
					"Il Provvedimento SIUS N:" + lFascicoloGPModel.getFascicoloSiusModel().getChiaveAnno()
							+ "/" + lFascicoloGPModel.getFascicoloSiusModel().getChiaveProgr()
							+ " NON è stato trasferito perchè già presente in NSC.");
		} else {
			setRequestAttribute(IWebConstants.MESSAGE_TEXT,
					"Il Provvedimento SIUS N:" + lFascicoloGPModel.getFascicoloSiusModel().getChiaveAnno()
							+ "/" + lFascicoloGPModel.getFascicoloSiusModel().getChiaveProgr()
							+ " NON è stato trasferito.");
		}

		RedirectTo lRedirigi = new RedirectTo();
		lRedirigi.setPage(IWebConstants.PG_MAIN);
		if (lEvento.getCodTipoProvvedimento() != null && lEvento.getCodTipoProvvedimento().equals("02")) {
			// DECRETO
			lRedirigi.setAction(
					"siap.sius.depositodecreto.action.ActLoadDettaglioDataDepositoDecreto&IdDocumentoAllegato="
							+ lIdDocAllegato);
		} else { // ORDINANZA
			lRedirigi.setAction(
					"siap.sius.depositoordinanzapc.action.ActLoadDettaglioDataDepositoOrdinanza&IdDocumentoAllegato="
							+ lIdDocAllegato);
		}
		setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi);
		return IWebConstants.PG_MESSAGE;
	}

	/****************************************************************************************/
	/**************************** FINE METODO PRINCIPALE **********************************/
	/****************************************************************************************/

	private FascicoloGPModel CercaFascicoloSISU(BigDecimal lIdFascicoloSius) throws F3BException {

		FascicoloSiusController lFascContr = new FascicoloSiusController();
		FascicoloGPModel lFascicoloGPModel = new FascicoloGPModel();
		try {
			lFascicoloGPModel = lFascContr.ExRicercaFascicoloByKey(lIdFascicoloSius);
		} catch (SIEPException e) {
			throw new F3BException(
					"ActPrelevaEseUDS - Errore nella Ricerca del Fascicolo SIUS. Contattare il servizio di Help Desk");
		}

		return lFascicoloGPModel;

	}

	private void ScriviDatiUtente_Ufficio(
			DATICHIAMATAESECUZIONEDocument.DATICHIAMATAESECUZIONE aDatiChiamataEsec, UtenteModel aUteMod)
			throws Exception {

		DATIUTENTEDocument.DATIUTENTE DatiUtente = aDatiChiamataEsec.addNewDATIUTENTE();
		DATIUFFICIODocument.DATIUFFICIO DatiUfficio = DatiUtente.addNewDATIUFFICIO();

		// DECODIFICA CODI_SEDE_UFFICIO
		if (aUteMod.getUfficioUtente().getCodComune() != null) {
			lCodiciSiesNscModel = new CodiciSiesNscModel();
			lCodiciSiesNscModel.setCoDomain("COMUNE");
			lCodiciSiesNscModel.setCoSies(aUteMod.getUfficioUtente().getCodComune());

			CodiciSiesNscModel aCodiciSIESNSCModel = DecodificaCodiceSies(lCodiciSiesNscModel);
			lCodCentralizzato = aCodiciSIESNSCModel.getCoCodcentr();
			DatiUfficio.setCODICESEDEUFFICIO(lCodCentralizzato);
		} else {
			DatiUfficio.setCODICESEDEUFFICIO("");
		}

		// DECODIFICA TIPO_UFFICIO

		if (aUteMod.getUfficioUtente().getCodTipoUfficio() != null) {
			lCodiciSiesNscModel = new CodiciSiesNscModel();
			lCodiciSiesNscModel.setCoDomain("TIPO_UFFICIO");
			lCodiciSiesNscModel.setCoSies(aUteMod.getUfficioUtente().getCodTipoUfficio());
			CodiciSiesNscModel aCodiciSIESNSCModel = DecodificaCodiceSies(lCodiciSiesNscModel);
			lCodCentralizzato = aCodiciSIESNSCModel.getCoCodcentr();
			DatiUfficio.setCODICETIPOUFFICIO(lCodCentralizzato);
		} else {
			DatiUfficio.setCODICETIPOUFFICIO("");
		}
		DatiUfficio.setCODISISTEMA(DATIUFFICIODocument.DATIUFFICIO.CODISISTEMA.SIUS);

		if (aUteMod.getUfficioUtente().getCodDistretto() != null
				&& !aUteMod.getUfficioUtente().getCodDistretto().equals("")) {
			DatiUfficio.setCODIDISTRETTO(aUteMod.getUfficioUtente().getCodDistretto().substring(0, 6));
		} else {
			DatiUfficio.setCODIDISTRETTO("");
		}

		DatiUtente.setUSERNAME(getCodUtenteConnesso());
		DatiUtente.setCOGNOMEUTENTE(aUteMod.getCognome());
		DatiUtente.setNOMEUTENTE(aUteMod.getNome());
		DatiUtente.setIPADDRESSSERVER(getRequest().getServerName() + ":" + getRequest().getServerPort());

	}

	private void ScriviChiavi(DATICHIAMATAESECUZIONEDocument.DATICHIAMATAESECUZIONE aDatiChiamataEsec,
			FascicoloGPModel aFascicoloGPModel, FascicoloSiepModel aFascicoloSiep, SoggettoModel aSoggetto,
			EventoModel aEvento) throws Exception {

		CHIAVIDocument.CHIAVI DatiChiavi = aDatiChiamataEsec.addNewCHIAVI();

		if (aSoggetto.getIdSoggetto() != null && !aSoggetto.getIdSoggetto().toString().equals("")) {
			DatiChiavi.setKASIES(aSoggetto.getIdSoggetto().longValue());
		}

		if (aSoggetto.getKeySoggNsc() != null && !aSoggetto.getKeySoggNsc().toString().equals("")) {
			DatiChiavi.setKANSC(aSoggetto.getKeySoggNsc().longValue());
		}

		if (aEvento.getIdEvento() != null && !aEvento.getIdEvento().toString().equals("")) {
			DatiChiavi.setKPESIES(aEvento.getIdEvento().longValue());
		}

		if (aEvento.getKeyEsecNsc() != null && !aEvento.getKeyEsecNsc().toString().equals("")) {
			DatiChiavi.setKPENSC(aEvento.getKeyEsecNsc().longValue());
		}

		ArrayChiaviProvvGiudiziariDocument.ArrayChiaviProvvGiudiziari DatiChiaviProvvGiud = DatiChiavi
				.addNewArrayChiaviProvvGiudiziari();
		CHIAVIPROVGIUDIZIARI ChiaviProvvGiud = DatiChiaviProvvGiud.addNewCHIAVIPROVGIUDIZIARI();

		if (aFascicoloSiep.getKeyProvvNsc() != null
				&& !aFascicoloSiep.getKeyProvvNsc().toString().equals("")) {
			ChiaviProvvGiud.setKPNSC(aFascicoloSiep.getKeyProvvNsc().longValue());
		}

		if (aFascicoloSiep.getIdFascicoloSiep() != null
				&& !aFascicoloSiep.getIdFascicoloSiep().toString().equals("")) {
			ChiaviProvvGiud.setKPSIES(aFascicoloSiep.getIdFascicoloSiep().longValue());
		}

	}

	private void ScriviDatiOperazione(DATICHIAMATAESECUZIONEDocument.DATICHIAMATAESECUZIONE aDatiChiamataEsec,
			EventoModel aEvento) throws Exception {

		DATIOPERAZIONEDocument.DATIOPERAZIONE DatiOperazione = aDatiChiamataEsec.addNewDATIOPERAZIONE();
		DatiOperazione.setENTITA(ENTITADocument.ENTITA.P);

		if (aEvento.getKeyEsecNsc() != null) {
			DatiOperazione.setOPERAZIONE(OPERAZIONEDocument.OPERAZIONE.UPDATE);
		} else {
			DatiOperazione.setOPERAZIONE(OPERAZIONEDocument.OPERAZIONE.INSERT);
		}

	}

	private void ScriviDatiProvvedimento(
			DATICHIAMATAESECUZIONEDocument.DATICHIAMATAESECUZIONE aDatiChiamataEsec, EventoModel aEvento,
			FascicoloGPModel aFascicoloGPModel) throws Exception {

		DATIPROVVEDIMENTODocument.DATIPROVVEDIMENTO DatiProvvedimento = aDatiChiamataEsec
				.addNewDATIPROVVEDIMENTO();

		// DATA lData= DATA.Factory.newInstance();

		if (aEvento.getDataEmissione() != null) {
			lData.setGIORNO(DateUtils.getDateToString(aEvento.getDataEmissione(), "dd"));
			lData.setMESE(DateUtils.getDateToString(aEvento.getDataEmissione(), "MM"));
			lData.setANNO(DateUtils.getDateToString(aEvento.getDataEmissione(), "yyyy"));
		} else {
			lData.setGIORNO("");
			lData.setMESE("");
			lData.setANNO("");
		}
		DatiProvvedimento.setDATAPROVVEDIMENTO(lData);

		// CODI_AUTORITA
		lCodiciSiesNscModel = new CodiciSiesNscModel();
		lCodiciSiesNscModel.setCoDomain("TIPO_UFFICIO");
		lCodiciSiesNscModel.setCoSies("UDS");
		CodiciSiesNscModel aCodiciSIESNSCModel = DecodificaCodiceSies(lCodiciSiesNscModel);
		lCodCentralizzato = aCodiciSIESNSCModel.getCoCodcentr();
		DatiProvvedimento.setCODIAUTORITA(lCodCentralizzato);

		// CODI_SEDE_AUTORITA_PRIN_DIST
		if (aEvento.getCodLuogoEmittente() != null) {
			lCodiciSiesNscModel = new CodiciSiesNscModel();
			lCodiciSiesNscModel.setCoDomain("COMUNE");
			lCodiciSiesNscModel.setCoSies(aEvento.getCodLuogoEmittente());
			CodiciSiesNscModel lCodiciSIESNSCModel = DecodificaCodiceSies(lCodiciSiesNscModel);
			lCodCentralizzato = lCodiciSIESNSCModel.getCoCodcentr();
			DatiProvvedimento.setCODISEDEAUTORITAPRINDIST(lCodCentralizzato);
		} else {
			DatiProvvedimento.setCODISEDEAUTORITAPRINDIST("");
		}

		// CODI_SEDE_AUTORITA_PRIN
		// DatiProvvedimento.setCODISEDEAUTORITAPRIN("");

		// TIPO_ATTO - "02" Decreto - "03" Ordinanza
		if (aEvento.getCodTipoProvvedimento() != null) {
			if (aEvento.getCodTipoProvvedimento().equals("02")) {
				DatiProvvedimento.setTIPOATTO("D"); // DECRETO
			} else {
				DatiProvvedimento.setTIPOATTO("O"); // ORDINANZA
			}
		} else {
			DatiProvvedimento.setTIPOATTO("");
		}

		DatiProvvedimento.setFLAGORIGINEPROVVEDIMENTO("2");

		ScriviDatiProvvedimentoUDS(DatiProvvedimento, aEvento, aFascicoloGPModel);

	}

	private void ScriviDatiProvvedimentoUDS(DATIPROVVEDIMENTODocument.DATIPROVVEDIMENTO aDatiProvvedimento,
			EventoModel aEvento, FascicoloGPModel aFascicoloGPModel) throws Exception {

		String lCodCentralizzato;
		RichiestaConversioneModel lRichiestaConversioneModel = null;
		DepositoOrdinanzaPcModel lDepositoOrdinanzaPcModel = null;
		DepositoDecretoModel lDepositoDecretoModel = null;
		LicenzaPeriodiLibAnticipataModel lPeriodoLibAnticipataModel = null;

		PROVVUDSDocument.PROVVUDS DatiProvvUDS = aDatiProvvedimento.addNewPROVVUDS();
		ArrayPeriodoLADocument.ArrayPeriodoLA ArrayPeriodoLa = null;
		PERIODOLADocument.PERIODOLA PeriodoLA = null;

		// RICHIESTA_CONVERSIONE
		lRichiestaConversioneModel = CercaRichiestaConversione(aEvento.getIdEvento());

		if (aEvento.getCodTipoProvvedimento() != null) {
			if (aEvento.getCodTipoProvvedimento().equals("02")) {
				lDepositoDecretoModel = CercaDepositoDecreto(aEvento.getIdEvento());
			} else {
				lDepositoOrdinanzaPcModel = CercaDepositoOrdinanza(aEvento.getIdEvento());
			}
		}

		// ********************************************************************************************************
		// PROVVEDIMENTO: CONVERSIONE / RATEIZZAZIONE PENA PECUNIARIA
		// "0156" - Dispone conversione in libertà controllata "0157" - Dispone conversione in lavoro
		// sostitutivo
		// ********************************************************************************************************
		if (aEvento.getCodMotivo().equals("2470")
				&& (aEvento.getCodEsito().equals("0156") || aEvento.getCodEsito().equals("0157"))) {

			// ELEMENTO: IMPORTO_AMMENDA - IMPORTO MULTA
			if (lRichiestaConversioneModel != null
					&& lRichiestaConversioneModel.getImportoAmmenda() != null) {
				DatiProvvUDS.setIMPORTOAMMENDA(lRichiestaConversioneModel.getImportoAmmenda());
			}
			if (lRichiestaConversioneModel != null && lRichiestaConversioneModel.getImportoMulta() != null) {
				DatiProvvUDS.setIMPORTOMULTA(lRichiestaConversioneModel.getImportoMulta());
			}
			lDurata.setANNIDURATA(0);
			lDurata.setMESIDURATA(0);
			lDurata.setGIORNIDURATA(0);

			if (lRichiestaConversioneModel != null
					&& lRichiestaConversioneModel.getDurataEsitoAnni() != null) {
				lDurata.setANNIDURATA(lRichiestaConversioneModel.getDurataEsitoAnni().intValue());
			}

			if (lRichiestaConversioneModel != null
					&& lRichiestaConversioneModel.getDurataEsitoMesi() != null) {
				lDurata.setMESIDURATA(lRichiestaConversioneModel.getDurataEsitoMesi().intValue());
			}

			if (lRichiestaConversioneModel != null
					&& lRichiestaConversioneModel.getDurataEsitoGiorni() != null) {
				lDurata.setGIORNIDURATA(lRichiestaConversioneModel.getDurataEsitoGiorni().intValue());
			}

			if (lDurata.getANNIDURATA() != 0 || lDurata.getMESIDURATA() != 0
					|| lDurata.getGIORNIDURATA() != 0) {
				// ELEMENTO: DURATA LIBERTA CONTROLLATA
				if (aEvento.getCodEsito().equals("0156")) // Dispone conversione in libertà controllata
				{
					DatiProvvUDS.setDURATALIBERTACONTROLLATA(lDurata);
				} else {
					// ELEMENTO: DURATA LAVORO SOSTITUTIVO - "0157" = Dispone conversione in lavoro
					// sostitutivo
					DatiProvvUDS.setDURATALAVOROSOSTITUTIVO(lDurata);
				}
			}
		}

		// CODICE UNIVOCO PROVVEDIMENTO
		ProvvSiesNscModel lProvvSiesNscModel = new ProvvSiesNscModel();
		lProvvSiesNscModel.setProvvDomain("MDS");
		lProvvSiesNscModel.setProvvSiesMotivo(aEvento.getCodMotivo());
		lProvvSiesNscModel.setProvvSiesEsito(aEvento.getCodEsito());
		lProvvSiesNscModel.setProvvVal1("-");

		if (aEvento.getCodMotivo().equals("2470")
				&& (aEvento.getCodEsito().equals("0156") || aEvento.getCodEsito().equals("0157"))) {
			if (lRichiestaConversioneModel != null && lRichiestaConversioneModel.getImportoMulta() != null
					&& lRichiestaConversioneModel.getImportoMulta().intValue() != 0) {
				lProvvSiesNscModel.setProvvVal1("MULTA");
			} else if (lRichiestaConversioneModel != null
					&& lRichiestaConversioneModel.getImportoAmmenda() != null
					&& lRichiestaConversioneModel.getImportoAmmenda().intValue() != 0) {
				lProvvSiesNscModel.setProvvVal1("AMMENDA");
			}
		}

		ProvvSiesNscModel aProvvSIESNSCModel = DecodificaMotivoEsitoSies(lProvvSiesNscModel);
		lCodCentralizzato = aProvvSIESNSCModel.getProvvCodcentr();
		DatiProvvUDS.setCODICEUNIVOCOPROVVEDIMENTO(lCodCentralizzato);

		/******************************************************************************/
		// ----> PROVVEDIMENTO: RIDUZIONE PENA PER LIBERAZIONE ANTICIPATA
		/*********** GIORNI LIBERTA ANTICIPATA - PERIODI LIBERTA ANTICIPATA ***********/
		/******************************************************************************/

		if (aEvento.getCodMotivo().equals("2130") && aEvento.getCodEsito().equals("0020")) {

			// GIORNI_LIB_ANTICIPATA
			if (lDepositoOrdinanzaPcModel != null
					&& lDepositoOrdinanzaPcModel.getNumGiorniLibanticipata() != null) {
				DatiProvvUDS.setGIORNILIBANTICIPATA(
						lDepositoOrdinanzaPcModel.getNumGiorniLibanticipata().intValue());
			}

			Vector lPeriodiLibAnticipata = CercaPeriodiLibertaAnticipata(aEvento.getIdEvento());

			// ARRAY PERIODO LIBERTA ANTICIPATA
			for (int i = 0; i <= lPeriodiLibAnticipata.size() - 1; i++) {

				lPeriodoLibAnticipataModel = (LicenzaPeriodiLibAnticipataModel) lPeriodiLibAnticipata
						.elementAt(i);

				if (lPeriodoLibAnticipataModel.getPeriodi() != null) {
					for (int j = 0; j <= lPeriodoLibAnticipataModel.getPeriodi().length - 1; j++) {

						if (j == 0) {
							ArrayPeriodoLa = DatiProvvUDS.addNewArrayPeriodoLA();
							PeriodoLA = ArrayPeriodoLa.addNewPERIODOLA();
						}

						lData.setGIORNO("");
						lData.setMESE("");
						lData.setANNO("");

						if (lPeriodoLibAnticipataModel.getPeriodi()[j].getDataInizio() != null) {
							lData.setGIORNO(DateUtils.getDateToString(
									lPeriodoLibAnticipataModel.getPeriodi()[j].getDataInizio(), "dd"));
							lData.setMESE(DateUtils.getDateToString(
									lPeriodoLibAnticipataModel.getPeriodi()[j].getDataInizio(), "MM"));
							lData.setANNO(DateUtils.getDateToString(
									lPeriodoLibAnticipataModel.getPeriodi()[j].getDataInizio(), "yyyy"));
							PeriodoLA.setDATAINIZIO(lData);
						}

						lData.setGIORNO("");
						lData.setMESE("");
						lData.setANNO("");

						if (lPeriodoLibAnticipataModel.getPeriodi()[j].getDataFine() != null) {
							lData.setGIORNO(DateUtils.getDateToString(
									lPeriodoLibAnticipataModel.getPeriodi()[j].getDataFine(), "dd"));
							lData.setMESE(DateUtils.getDateToString(
									lPeriodoLibAnticipataModel.getPeriodi()[j].getDataFine(), "MM"));
							lData.setANNO(DateUtils.getDateToString(
									lPeriodoLibAnticipataModel.getPeriodi()[j].getDataFine(), "yyyy"));
							PeriodoLA.setDATAFINE(lData);
						}
					}
				}
			}
		}

		/******************************************************************************/

		// TESTO LIBERO
		if ((aEvento.getCodMotivo().equals("2470") && // PROVVEDIMENTO: CONVERSIONE / RATEIZZAZIONE PENA
														// PECUNIARIA
				(aEvento.getCodEsito().equals("0156") || aEvento.getCodEsito().equals("0157")))
				|| (aEvento.getCodMotivo().equals("2480") && aEvento.getCodEsito().equals("0001"))) // PROVVEDIMENTO:
																									// MISURE
																									// ALTERNATIVE
																									// ALLA
																									// DETENZIONE
																									// (SOSPENSIONE
																									// PROVVISORIA)
		{
			if (lDepositoOrdinanzaPcModel != null
					&& lDepositoOrdinanzaPcModel.getUlterioreDescrizione() != null) {
				DatiProvvUDS.setTESTOLIBERO(lDepositoOrdinanzaPcModel.getUlterioreDescrizione());
			}
		}

		// PROVVEDIMENTO: Conversione / rateizzazione pena pecuniaria - Rateizza pagamento
		if (aEvento.getCodMotivo().equals("2471") && aEvento.getCodEsito().equals("0159")) {
			if (lRichiestaConversioneModel != null && lRichiestaConversioneModel.getNumeroRate() != null) {
				DatiProvvUDS.setNUMERORATE(lRichiestaConversioneModel.getNumeroRate().intValue());
			}

			if (lRichiestaConversioneModel != null && lRichiestaConversioneModel.getValoreRata() != null) {
				DatiProvvUDS.setIMPORTORATA(lRichiestaConversioneModel.getValoreRata());
			}

			if (lRichiestaConversioneModel != null
					&& lRichiestaConversioneModel.getValoreUltimaRata() != null) {
				DatiProvvUDS.setIMPORTOULTIMARATA(lRichiestaConversioneModel.getValoreUltimaRata());
			}

			lData.setGIORNO("");
			lData.setMESE("");
			lData.setANNO("");

			if (lRichiestaConversioneModel != null
					&& lRichiestaConversioneModel.getDataInizioPagamento() != null) {
				lData.setGIORNO(
						DateUtils.getDateToString(lRichiestaConversioneModel.getDataInizioPagamento(), "dd"));
				lData.setMESE(
						DateUtils.getDateToString(lRichiestaConversioneModel.getDataInizioPagamento(), "MM"));
				lData.setANNO(DateUtils.getDateToString(lRichiestaConversioneModel.getDataInizioPagamento(),
						"yyyy"));
				DatiProvvUDS.setDATADECORRENZA1RATA(lData);
			}

			if (lRichiestaConversioneModel != null
					&& lRichiestaConversioneModel.getNumeroGiorniInizioPagamento() != null) {
				DatiProvvUDS.setNUMEROGGDANOTIFICA(
						lRichiestaConversioneModel.getNumeroGiorniInizioPagamento().intValue());
			}
		}

		lDurata.setANNIDURATA(0);
		lDurata.setMESIDURATA(0);
		lDurata.setGIORNIDURATA(0);
		// Conversione / rateizzazione pena pecuniaria - Differisce la conversione
		if (aEvento.getCodMotivo().equals("2470") && aEvento.getCodEsito().equals("0158")) {
			lDurata.setANNIDURATA(0);
			lDurata.setMESIDURATA(0);
			lDurata.setGIORNIDURATA(0);

			if (lRichiestaConversioneModel != null
					&& lRichiestaConversioneModel.getDurataEsitoAnni() != null) {
				lDurata.setANNIDURATA(lRichiestaConversioneModel.getDurataEsitoAnni().intValue());
			}

			if (lRichiestaConversioneModel != null
					&& lRichiestaConversioneModel.getDurataEsitoMesi() != null) {
				lDurata.setMESIDURATA(lRichiestaConversioneModel.getDurataEsitoMesi().intValue());
			}

			if (lRichiestaConversioneModel != null
					&& lRichiestaConversioneModel.getDurataEsitoGiorni() != null) {
				lDurata.setGIORNIDURATA(lRichiestaConversioneModel.getDurataEsitoGiorni().intValue());
			}

			if (lDurata.getANNIDURATA() != 0 || lDurata.getMESIDURATA() != 0
					|| lDurata.getGIORNIDURATA() != 0) {
				DatiProvvUDS.setDURATADIFFERIMENTO(lDurata);
			}
		}

		lData.setGIORNO("");
		lData.setMESE("");
		lData.setANNO("");

		lDurata.setANNIDURATA(0);
		lDurata.setMESIDURATA(0);
		lDurata.setGIORNIDURATA(0);

		// Sospensione esecuzione sanzioni sostitutive
		if ((aEvento.getCodMotivo().equals("2370") || aEvento.getCodMotivo().equals("2371")
				|| aEvento.getCodMotivo().equals("2372") || aEvento.getCodMotivo().equals("2373"))
				&& (aEvento.getCodEsito().equals("0136"))) {
			if (aEvento.getCodTipoProvvedimento() != null) {
				if (aEvento.getCodTipoProvvedimento().equals("02")) // DECRETO
				{

					// DATA_DECORRENZA_SOSPENSIONE
					if (lDepositoDecretoModel != null
							&& lDepositoDecretoModel.getDataSospensioneSS() != null) {
						lData.setGIORNO(DateUtils
								.getDateToString(lDepositoDecretoModel.getDataSospensioneSS(), "dd"));
						lData.setMESE(DateUtils.getDateToString(lDepositoDecretoModel.getDataSospensioneSS(),
								"MM"));
						lData.setANNO(DateUtils.getDateToString(lDepositoDecretoModel.getDataSospensioneSS(),
								"yyyy"));
						DatiProvvUDS.setDATADECORRENZASOSPENSIONE(lData);
					}

					// DATA_FINE_SOSPENSIONE
					lData.setGIORNO("");
					lData.setMESE("");
					lData.setANNO("");
					if (lDepositoDecretoModel != null
							&& lDepositoDecretoModel.getDataScadenzaSospensioneSS() != null) {
						lData.setGIORNO(DateUtils
								.getDateToString(lDepositoDecretoModel.getDataScadenzaSospensioneSS(), "dd"));
						lData.setMESE(DateUtils
								.getDateToString(lDepositoDecretoModel.getDataScadenzaSospensioneSS(), "MM"));
						lData.setANNO(DateUtils.getDateToString(
								lDepositoDecretoModel.getDataScadenzaSospensioneSS(), "yyyy"));
						// DatiProvvUDS.setDATAFINESOSPENSIONE(lData);
					}

					// DURATA_SOSPENSIONE
					if (lDepositoDecretoModel != null && lDepositoDecretoModel.getSospensioneAASS() != null) {
						lDurata.setANNIDURATA(lDepositoDecretoModel.getSospensioneAASS().intValue());
					}

					if (lDepositoDecretoModel != null && lDepositoDecretoModel.getSospensioneMMSS() != null) {
						lDurata.setMESIDURATA(lDepositoDecretoModel.getSospensioneMMSS().intValue());
					}
					if (lDepositoDecretoModel != null && lDepositoDecretoModel.getSospensioneGGSS() != null) {
						lDurata.setGIORNIDURATA(lDepositoDecretoModel.getSospensioneGGSS().intValue());
					}

					if (lDurata.getANNIDURATA() != 0 || lDurata.getMESIDURATA() != 0
							|| lDurata.getGIORNIDURATA() != 0) {
						DatiProvvUDS.setDURATASOSPENSIONE(lDurata);
					} else {
						DatiProvvUDS.setDATAFINESOSPENSIONE(lData);
					}

				} else // DEPOSITO ORDINANZA
				{
					// DATA_DECORRENZA_SOSPENSIONE
					if (lDepositoOrdinanzaPcModel != null
							&& lDepositoOrdinanzaPcModel.getDataSospensioneSS() != null) {
						lData.setGIORNO(DateUtils
								.getDateToString(lDepositoOrdinanzaPcModel.getDataSospensioneSS(), "dd"));
						lData.setMESE(DateUtils
								.getDateToString(lDepositoOrdinanzaPcModel.getDataSospensioneSS(), "MM"));
						lData.setANNO(DateUtils
								.getDateToString(lDepositoOrdinanzaPcModel.getDataSospensioneSS(), "yyyy"));
						DatiProvvUDS.setDATADECORRENZASOSPENSIONE(lData);
					}

					// DATA_FINE_SOSPENSIONE
					lData.setGIORNO("");
					lData.setMESE("");
					lData.setANNO("");

					if (lDepositoOrdinanzaPcModel != null
							&& lDepositoOrdinanzaPcModel.getDataScadenzaSospensioneSS() != null) {
						lData.setGIORNO(DateUtils.getDateToString(
								lDepositoOrdinanzaPcModel.getDataScadenzaSospensioneSS(), "dd"));
						lData.setMESE(DateUtils.getDateToString(
								lDepositoOrdinanzaPcModel.getDataScadenzaSospensioneSS(), "MM"));
						lData.setANNO(DateUtils.getDateToString(
								lDepositoOrdinanzaPcModel.getDataScadenzaSospensioneSS(), "yyyy"));
						// DatiProvvUDS.setDATAFINESOSPENSIONE(lData);
					}

					// DURATA_SOSPENSIONE
					if (lDepositoOrdinanzaPcModel != null
							&& lDepositoOrdinanzaPcModel.getSospensioneAASS() != null) {
						lDurata.setANNIDURATA(lDepositoOrdinanzaPcModel.getSospensioneAASS().intValue());
					}

					if (lDepositoOrdinanzaPcModel != null
							&& lDepositoOrdinanzaPcModel.getSospensioneMMSS() != null) {
						lDurata.setMESIDURATA(lDepositoOrdinanzaPcModel.getSospensioneMMSS().intValue());
					}
					if (lDepositoOrdinanzaPcModel != null
							&& lDepositoOrdinanzaPcModel.getSospensioneGGSS() != null) {
						lDurata.setGIORNIDURATA(lDepositoOrdinanzaPcModel.getSospensioneGGSS().intValue());
					}

					if (lDurata.getANNIDURATA() != 0 || lDurata.getMESIDURATA() != 0
							|| lDurata.getGIORNIDURATA() != 0) {
						DatiProvvUDS.setDURATASOSPENSIONE(lDurata);
					} else {
						DatiProvvUDS.setDATAFINESOSPENSIONE(lData);
					}
				}
			}
		}

		// PROVVEDIMENTO INDULTINO - SOLO DEPOSITO ORDINANZA
		if (aEvento.getCodMotivo().equals("2245") && aEvento.getCodEsito().equals("0001")) {
			lData.setGIORNO("");
			lData.setMESE("");
			lData.setANNO("");

			if (lDepositoOrdinanzaPcModel != null && lDepositoOrdinanzaPcModel.getDataFineMisura() != null) {
				lData.setGIORNO(
						DateUtils.getDateToString(lDepositoOrdinanzaPcModel.getDataFineMisura(), "dd"));
				lData.setMESE(DateUtils.getDateToString(lDepositoOrdinanzaPcModel.getDataFineMisura(), "MM"));
				lData.setANNO(
						DateUtils.getDateToString(lDepositoOrdinanzaPcModel.getDataFineMisura(), "yyyy"));
				DatiProvvUDS.setDATAFINESOSPENSIONE(lData);
			}
		}

		if ((aEvento.getCodMotivo().equals("2135") && aEvento.getCodEsito().equals("0023"))
				|| (aEvento.getCodMotivo().equals("2490") && aEvento.getCodEsito().equals("0007"))
				|| (aEvento.getCodMotivo().equals("2491") && aEvento.getCodEsito().equals("0007"))) {
			// Cerchiamo l'Evento Revocato tramite il Fascicolo SIUS di origine e il Codice Tipo Provvedimento
			// e Codice Tipo Evento = "01"
			Vector lAltroEvento = CercaEventoRevocato(
					aFascicoloGPModel.getFascicoloSiusModel().getIdFascicoloSiusOrigine(),
					aEvento.getCodTipoProvvedimento(), "01");
			if (lAltroEvento.size() > 0) {
				EventoModel lEventoRevocatoModel = (EventoModel) lAltroEvento.elementAt(0);
				DatiProvvUDS.setIDPROVVREVOCATO(lEventoRevocatoModel.getIdEvento().longValue());
			}
		}
	}

	private RichiestaConversioneModel CercaRichiestaConversione(BigDecimal aIDEvento) throws F3BException {

		IRichiestaConversione lCtrlRichConv = SIEPLookupRemote.getRichiestaConversioneRemote();
		RichiestaConversioneModel lRichiestaConversioneModel = new RichiestaConversioneModel();
		lRichiestaConversioneModel = lCtrlRichConv.ExRicercaRichiestaConversioneByIdEvento(aIDEvento);
		/*
		 * try { lRichiestaConversioneModel =
		 * lCtrlRichConv.ExRicercaRichiestaConversioneByIdEvento(aIDEvento); } catch (SIEPException e) { //
		 * [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		 * LogF3B.getLogger() siesLogger.error(
		 * "ActPrelevaEseUDS - Errore nella Ricerca della Richiesta Conversione Evento. Contattare il servizio di Help Desk"
		 * ,e); throw e; }
		 */
		return lRichiestaConversioneModel;
	}

	private DepositoOrdinanzaPcModel CercaDepositoOrdinanza(BigDecimal aIDEvento) throws F3BException {

		IDepositoOrdinanzaPc lCtrlDepOrd = SIUSLookupRemote.getDepositoOrdinanzaPcRemote();
		DepositoOrdinanzaPcModel lDepositoOrdinanzaPcModel = new DepositoOrdinanzaPcModel();
		lDepositoOrdinanzaPcModel = lCtrlDepOrd.ExRicercaDepositoOrdinanzaPcByEvento(aIDEvento);
		/*
		 * try { lDepositoOrdinanzaPcModel = lCtrlDepOrd.ExRicercaDepositoOrdinanzaPcByEvento(aIDEvento); } //
		 * [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		 * LogF3B.getLogger() catch (SIEPException e) { siesLogger.error(
		 * "ActPrelevaEseUDS - Errore nella Ricerca del DepositoOrdinanzaPc. Contattare il servizio di Help Desk"
		 * ,e); throw e; }
		 */
		return lDepositoOrdinanzaPcModel;
	}

	private DepositoDecretoModel CercaDepositoDecreto(BigDecimal aIDEvento) throws F3BException {

		IDepositoDecreto lCtrlDepDecr = SIUSLookupRemote.getDepositoDecretoRemote();
		DepositoDecretoModel lDepositoDecretoModel = new DepositoDecretoModel();
		lDepositoDecretoModel = lCtrlDepDecr.ExRicercaDepositoDecretoByIdEvento(aIDEvento);
		return lDepositoDecretoModel;
	}

	private Vector CercaPeriodiLibertaAnticipata(BigDecimal aIDEvento) throws F3BException {

		ILicenzaPeriodiLibAnticipata lCtrlLic = SICOLookupRemote.getLicenzaPeriodiLibAntRemote();
		Vector lPeriodiLibAnticipata = new Vector();
		lPeriodiLibAnticipata = lCtrlLic.ExRicercaLicenzeLibanticipataByEve(aIDEvento);
		return lPeriodiLibAnticipata;
	}

	private Vector CercaEventoRevocato(BigDecimal lIdFasSius, String lCodTipoProvv, String lCodTipoEbento)
			throws F3BException {

		IEvento lCtrlEvento = SICOLookupRemote.getEventoRemote();
		EventoModel lEventoModel = new EventoModel();
		Vector lEventoVector = new Vector();

		lEventoModel.setFasSiuIdFascicoloSius(lIdFasSius);
		lEventoModel.setCodTipoProvvedimento(lCodTipoProvv);
		lEventoModel.setCodTipoEvento(lCodTipoEbento);

		lEventoVector = lCtrlEvento.ExRicercaEvento(lEventoModel);

		return lEventoVector;
	}

}