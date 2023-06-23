package siap.sico.webservice.action;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.xmlbeans.XmlOptions;

import f3b.log.LogF3B;
import f3b.util.StringUtils;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;
import it.mig.sies.type.ArrayChiaviReatiDocument;
import it.mig.sies.type.CHIAVIDocument;
import it.mig.sies.type.DATICHIAMATATRASFERIMENTODocument;
import it.mig.sies.type.DATIOPERAZIONEDocument;
import it.mig.sies.type.DATIUFFICIODocument;
import it.mig.sies.type.DATIUTENTEDocument;
import it.mig.sies.type.ENTITADocument;
import it.mig.sies.type.IscriviProvvedimentoProvvisorio_PortType;
import it.mig.sies.type.IscriviProvvedimentoProvvisorio_ServiceLocator;
import it.mig.sies.type.OPERAZIONEDocument;
import it.mig.sies.type.TRASFERIMENTODocument;
import siap.sico.codici_sies_nsc.model.CodiciSiesNscModel;
import siap.sico.security.action.ICostantiSecurity;
import siap.sico.soggetto.model.SoggettoModel;
import siap.sico.utente.model.UtenteModel;
import siap.sico.webservice.config.NscProperties;
import siap.siep.fascicolo.action.ICostantiFascicoloSiep;
import siap.siep.fascicolo.controller.IFascicoloSiep;
import siap.siep.fascicolo.model.DettaglioFascicoloModel;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.reato.controller.IReato;
import siap.siep.reato.model.ReatoCircostanzaModel;
import siap.siep.reato.model.ReatoModel;
import siap.siep.util.SIEPLookupRemote;
import siap.util.SIAPPathProperties;

@SuppressWarnings("rawtypes")
public class ActPrelevaDatiFascicoloCancellato extends ActWsBase {
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	CodiciSiesNscModel lCodiciSiesNscModel;
	String lCodCentralizzato = "";
	private NscProperties mProperties = NscProperties.getInstance();

	// NUOVA INFRASTRUTTURA: aggiunte variabili di classe
	private SIAPPathProperties mPathProperties = SIAPPathProperties.getInstance();
	private String mPath = null;

	public String processRequest() throws Exception {
		BigDecimal aId;

		if (!this.isSessionAttributeNullObj("fascicolo")) {
			// Se provengo del Menù Scelta Rapida devo recuparere i dati dalla sessione
			// Cerco in sessione il fascicolo per recuperare l'id
			aId = ((FascicoloSiepModel) getSessionAttribute("fascicolo")).getIdFascicoloSiep();
		} else {
			// Se non ho l'id fascicolo ne sulla request ne in sessione restituisco la
			// pagina di ricerca fascicolo
			return ICostantiFascicoloSiep.REDIRECT_FASCICOLO_RICERCATO + getClass().getName();
		}

		// Tabelle SIES:FASCICOLO_SIEP
		// I/F della classe fascicolo contiene tutti i metodi della classe
		IFascicoloSiep lCtrlFas = SIEPLookupRemote.getFascicoloSiepRemote();
		DettaglioFascicoloModel lDettaglio = lCtrlFas.ExDettaglioFascicoloSiep(aId);

		FascicoloSiepModel lFascicolo = lDettaglio.getFascicoloSiep();

		// Leggiamo l'Anagrafica del Soggetto impostando l'ID Soggetto della tabella Fascicolo
		// Tabelle NSC:CG_ANAG_PERS_FISICHE SIES:SOGGETTO
		SoggettoModel lSoggetto = lDettaglio.getFascicoloSiep().getSoggetto();

		// Tabelle NSC:CG_REATI - CG_CIRCOSTANZE
		// SIES:REATO - CIRCOSTANZE

		IReato lCtrlReato = SIEPLookupRemote.getReatoRemote();
		List lReatiCircostanze = lCtrlReato.ExRicercaReatoCircostanzaByFascicoloOnlyNsc(aId.longValue());

		UtenteModel lUteMod = new UtenteModel(
				(UtenteModel) getSessionAttribute(ICostantiSecurity.SESSION_UTENTE_CONNESSO));

		/*------------------------------------------------------------------------------*/
		/* Inizio Scrittura XML */
		/*------------------------------------------------------------------------------*/
		TRASFERIMENTODocument lTrasfDoc = TRASFERIMENTODocument.Factory.newInstance();
		TRASFERIMENTODocument.TRASFERIMENTO ElementTrasf = lTrasfDoc.addNewTRASFERIMENTO();
		DATICHIAMATATRASFERIMENTODocument.DATICHIAMATATRASFERIMENTO DatiChiamataTrasf = ElementTrasf
				.addNewDATICHIAMATATRASFERIMENTO();

		/*******************************************************************************/
		/* Element DATI_CHIAMATA_TRASFERIMENTO - DATI_UTENTE - DATI_UFFICIO */
		/*******************************************************************************/
		DATIUTENTEDocument.DATIUTENTE DatiUtente = DatiChiamataTrasf.addNewDATIUTENTE();
		DATIUFFICIODocument.DATIUFFICIO DatiUfficio = DatiUtente.addNewDATIUFFICIO();

		// DECODIFICA CODI_SEDE_UFFICIO
		if (lUteMod.getUfficioUtente().getCodComune() != null) {
			lCodiciSiesNscModel = new CodiciSiesNscModel();
			lCodiciSiesNscModel.setCoDomain("COMUNE");
			lCodiciSiesNscModel.setCoSies(lUteMod.getUfficioUtente().getCodComune());
			// lCodCentralizzato = DecodificaCodiceSies(lCodiciSiesNscModel);
			CodiciSiesNscModel aCodiciSIESNSCModel = DecodificaCodiceSies(lCodiciSiesNscModel);
			lCodCentralizzato = aCodiciSIESNSCModel.getCoCodcentr();
			DatiUfficio.setCODICESEDEUFFICIO(lCodCentralizzato);
		} else {
			DatiUfficio.setCODICESEDEUFFICIO("");
		}

		// DECODIFICA TIPO_UFFICIO
		if (lUteMod.getUfficioUtente().getCodTipoUfficio() != null) {
			lCodiciSiesNscModel = new CodiciSiesNscModel();
			lCodiciSiesNscModel.setCoDomain("TIPO_UFFICIO");
			lCodiciSiesNscModel.setCoSies(lUteMod.getUfficioUtente().getCodTipoUfficio());
			CodiciSiesNscModel aCodiciSIESNSCModel = DecodificaCodiceSies(lCodiciSiesNscModel);
			lCodCentralizzato = aCodiciSIESNSCModel.getCoCodcentr();
			DatiUfficio.setCODICETIPOUFFICIO(lCodCentralizzato);
		} else {
			DatiUfficio.setCODICETIPOUFFICIO("");
		}

		DatiUfficio.setCODISISTEMA(DATIUFFICIODocument.DATIUFFICIO.CODISISTEMA.SIEP);

		if (lUteMod.getUfficioUtente().getCodDistretto() != null
				&& !lUteMod.getUfficioUtente().getCodDistretto().equals("")) {
			DatiUfficio.setCODIDISTRETTO(lUteMod.getUfficioUtente().getCodDistretto().substring(0, 6));
		} else {
			DatiUfficio.setCODIDISTRETTO("");
		}

		DatiUtente.setUSERNAME(getCodUtenteConnesso());
		DatiUtente.setCOGNOMEUTENTE(lUteMod.getCognome());
		DatiUtente.setNOMEUTENTE(lUteMod.getNome());
		DatiUtente.setIPADDRESSSERVER(getRequest().getServerName() + ":" + getRequest().getServerPort());
		// MEV INTEGRAZIONE SIES ADN: aggiunta impostazione per variabile UserAdn
		// DatiUtente.setUSERNAMEADN(lUteMod.getUserAdn());

		/*******************************************************************************/
		/* Element DATI_CHIAMATA_TRASFERIMENTO - CHIAVI */
		/*******************************************************************************/
		CHIAVIDocument.CHIAVI Chiavi = DatiChiamataTrasf.addNewCHIAVI();
		if (lSoggetto.getKeySoggNsc() != null && !"".equals(lSoggetto.getKeySoggNsc().toString())) {
			Chiavi.setKANSC(lSoggetto.getKeySoggNsc().longValue());
		}
		Chiavi.setKASIES(lFascicolo.getSogIdSoggetto().longValue());

		if (lFascicolo.getKeyProvvNsc() != null && !"".equals(lFascicolo.getKeyProvvNsc().toString())) {
			Chiavi.setKPNSC(lFascicolo.getKeyProvvNsc().longValue());
		}

		Chiavi.setKPSIES(lFascicolo.getIdFascicoloSiep().longValue());

		// Chiavi REATI
		ArrayChiaviReatiDocument.ArrayChiaviReati ArrayChiaviReati = null;
		ReatoModel lReato;
		for (int i = 0; i <= lReatiCircostanze.size() - 1; i++) {
			if (i == 0) {
				ArrayChiaviReati = Chiavi.addNewArrayChiaviReati();
			}

			lReato = ((ReatoCircostanzaModel) lReatiCircostanze.get(i)).getReato();

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.info("Reato:" + lReato.getIdReato().longValue());

			ArrayChiaviReatiDocument.ArrayChiaviReati.CHIAVIREATO ChiaviReato = ArrayChiaviReati
					.addNewCHIAVIREATO();
			ChiaviReato.setKRSIES(lReato.getIdReato().longValue());
			if (lReato.getKeyReatoNsc() != null) {
				ChiaviReato.setKRNSC(lReato.getKeyReatoNsc().longValue());
			}
		}

		/*******************************************************************************/
		/* Element DATI_CHIAMATA_TRASFERIMENTO - DATI_OPERAZIONE */
		/*******************************************************************************/
		DATIOPERAZIONEDocument.DATIOPERAZIONE DatiOperazione = DatiChiamataTrasf.addNewDATIOPERAZIONE();
		DatiOperazione.setENTITA(ENTITADocument.ENTITA.P); //
		DatiOperazione.setOPERAZIONE(OPERAZIONEDocument.OPERAZIONE.DELETE);

		/*******************************************************************************/
		/* ELEMENT DATI_CHIAMATA_TRASFERIMENTO - ANAGRAFICA */
		/*******************************************************************************/
		DatiChiamataTrasf.setPROGANAGRAFICANSC(lSoggetto.getKeySoggNsc().longValue());

		String flussoDatiXML = mProperties.getProperty("FlussoDati");
		if (!StringUtils.checkValidValue(flussoDatiXML)) {
			// NUOVA INFRASTRUTTURA: cambiato il path per differenziare macchina windows da macchina UNIX
			mPath = mPathProperties.getProperty("FLUSSO");
			flussoDatiXML = mPath;
		}
		lTrasfDoc.save(new File(flussoDatiXML));

		/***********************************************************************************/
		/* Validazione XML generato prima dell'invio a NSC */
		/***********************************************************************************/

		ArrayList validationErrors = new ArrayList();
		XmlOptions m_validationOptions = new XmlOptions();
		m_validationOptions.setErrorListener(validationErrors);
		boolean isValid = lTrasfDoc.validate(m_validationOptions);

		if (isValid) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Struttura XML VALIDA!!!" + lTrasfDoc.xmlText());
		} else {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Struttura XML NON E' VALIDA!!!");

			Iterator iter = validationErrors.iterator();
			while (iter.hasNext()) {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.info("====================================================================");
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.info("Errori rilevati durante la convalida:");
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.info(">> ERRORE " + iter.next() + "\n");
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.info("====================================================================");

				// Se si vuole generare un Exception la riga sotto altrimenti gestire l'errore
				// throw new Exception("ActPrelevaDatiFascicolo - Struttura XML non valida");

				setRequestAttribute(IWebConstants.MESSAGE_TEXT,
						"ActPrelevaDatiFascicoloCancellato - Struttura XML non valida. Contattare il servizio di Help Desk");
				RedirectTo lRedirigi = new RedirectTo();
				lRedirigi.setPage(IWebConstants.PG_MAIN);
				lRedirigi.setAction("siap.siep.fascicolo.action.ActLoadDettaglioFascicolo");
				setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi);
				return IWebConstants.PG_MESSAGE;
			}
		}

		/***********************************************************************************/
		/* Chiamo il Servizio (Web Service) del Casellario passando il flusso XML generato */
		/***********************************************************************************/

		String lNscWsAddress = mProperties.getProperty("NscWsAddress");

		// IscriviProvvedimentoProvvisorio service = new
		// IscriviProvvedimentoProvvisorioLocator(lNscWsAddress);
		// IscriviProvvedimentoProvvisorioPort port = service.getIscriviProvvedimentoProvvisorioPort();
		// String rispostaFlussoXmlNsc = port.iscriviProvvedimentoProvvisorio(lTrasfDoc.xmlText());

		IscriviProvvedimentoProvvisorio_ServiceLocator service = new IscriviProvvedimentoProvvisorio_ServiceLocator();
		service.setiscriviProvvedimentoProvvisorioEndpointAddress(lNscWsAddress);
		IscriviProvvedimentoProvvisorio_PortType port = service.getiscriviProvvedimentoProvvisorio();
		String rispostaFlussoXmlNsc = port.iscriviProvvedimentoProvvisorio(lTrasfDoc.xmlText());

		/***********************************************************************************/
		/* Istanzio classe che gestice la Risposta di NSC */
		/***********************************************************************************/

		ActRispostaDatiFascicoloCancellato objRispostaNsc = new ActRispostaDatiFascicoloCancellato();
		String lEsitoTrasmissione = objRispostaNsc.processRequest(rispostaFlussoXmlNsc,
				lFascicolo.getChiaveAnno(), lFascicolo.getChiaveProgr(), lUteMod);

		if (lEsitoTrasmissione.equals("0")) {
			setRequestAttribute(IWebConstants.MESSAGE_TEXT,
					"L'operazione di cancellazione del Provvedimento N:"
							+ lDettaglio.getFascicoloSiep().getChiaveAnno() + "/"
							+ lDettaglio.getFascicoloSiep().getChiaveProgr()
							+ " è stata trasmessa ad NSC con successo");
		} else {
			setRequestAttribute(IWebConstants.MESSAGE_TEXT,
					"L'operazione di cancellazione del Provvedimento N:"
							+ lDettaglio.getFascicoloSiep().getChiaveAnno() + "/"
							+ lDettaglio.getFascicoloSiep().getChiaveProgr()
							+ " NON è stato trasmessa correttamente a NSC");
		}

		RedirectTo lRedirigi = new RedirectTo();
		lRedirigi.setPage(IWebConstants.PG_MAIN);
		lRedirigi.setAction("siap.siep.fascicolo.action.ActLoadDettaglioFascicolo");
		setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi);
		return IWebConstants.PG_MESSAGE;

	}

}