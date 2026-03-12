package siap.sico.webservice.action;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.apache.xmlbeans.XmlOptions;

import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.StringUtils;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;
import it.mig.sies.type.ANAGRAFICADocument;
import it.mig.sies.type.ArrayBeneficiDocument;
import it.mig.sies.type.ArrayCassazioneDocument;
import it.mig.sies.type.ArrayChiaviReatiDocument;
import it.mig.sies.type.ArrayCircostanzeDocument;
import it.mig.sies.type.ArrayCircostanzeSpecialiDocument;
import it.mig.sies.type.ArrayMisureSicurezzaDocument;
import it.mig.sies.type.ArrayPenaComplessivaDocument;
import it.mig.sies.type.ArrayPeneAccessorieDocument;
import it.mig.sies.type.ArrayPeneAggiunteDocument;
import it.mig.sies.type.ArrayPrimoGradoDocument;
import it.mig.sies.type.ArrayProgressiviReatiDocument;
import it.mig.sies.type.ArrayReatiDocument;
import it.mig.sies.type.ArrayRevocheDocument;
import it.mig.sies.type.ArraySecondoGradoDocument;
import it.mig.sies.type.BENEFICIODocument;
import it.mig.sies.type.CASSAZIONEDocument;
import it.mig.sies.type.CHIAVIDocument;
import it.mig.sies.type.CIRCOSTANZADocument;
import it.mig.sies.type.DATA;
import it.mig.sies.type.DATIANAGRAFICIDocument;
import it.mig.sies.type.DATICHIAMATATRASFERIMENTODocument;
import it.mig.sies.type.DATIFASCICOLODocument;
import it.mig.sies.type.DATIOPERAZIONEDocument;
import it.mig.sies.type.DATIPROVVEDIMENTODocument;
import it.mig.sies.type.DATIRISPOSTATRASFERIMENTODocument;
import it.mig.sies.type.DATIUFFICIODocument;
import it.mig.sies.type.DATIUTENTEDocument;
import it.mig.sies.type.DISPOSITIVODocument;
import it.mig.sies.type.DISPOSITIVOPENACOMPLESSIVADocument;
import it.mig.sies.type.DURATA;
import it.mig.sies.type.ENTITADocument;
import it.mig.sies.type.ESITODocument;
import it.mig.sies.type.IMPUGNAZIONEDocument;
import it.mig.sies.type.IscriviProvvedimentoProvvisorio_PortType;
import it.mig.sies.type.IscriviProvvedimentoProvvisorio_ServiceLocator;
import it.mig.sies.type.MISURADISICUREZZADocument;
import it.mig.sies.type.OPERAZIONEDocument;
import it.mig.sies.type.PENAACCESSORIADocument;
import it.mig.sies.type.PENAAGGIUNTADocument;
import it.mig.sies.type.PRIMOGRADODocument;
import it.mig.sies.type.PROCEDIMENTODocument;
import it.mig.sies.type.REATODocument;
import it.mig.sies.type.REVOCADocument;
import it.mig.sies.type.SECONDOGRADODocument;
import it.mig.sies.type.SOSTITUZIONEPENADocument;
import it.mig.sies.type.TITOLOESECUTIVODocument;
import it.mig.sies.type.TRASFERIMENTODocument;
import siap.sico.codici_sies_nsc.model.CodiciSiesNscModel;
import siap.sico.security.action.ICostantiSecurity;
import siap.sico.soggetto.model.SoggettoModel;
import siap.sico.utente.model.UtenteModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.webservice.config.NscProperties;
import siap.sico.webservice.controller.IWebServices;
import siap.siep.SIEPException;
import siap.siep.beneficio.model.BeneficioModel;
import siap.siep.circostanza.model.CircostanzaModel;
import siap.siep.continuazione.controller.IContinuazione;
import siap.siep.continuazione.model.ContinuazioneModel;
import siap.siep.fascicolo.action.ICostantiFascicoloSiep;
import siap.siep.fascicolo.controller.IFascicoloSiep;
import siap.siep.fascicolo.model.DettaglioFascicoloModel;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.misurasicurezza.model.MisuraSicurezzaModel;
import siap.siep.penaaccessoria.model.PenaAccessoriaModel;
import siap.siep.penacomplessiva.model.PenaComplessivaModel;
import siap.siep.reato.controller.IReato;
import siap.siep.reato.model.ReatoCircostanzaModel;
import siap.siep.reato.model.ReatoModel;
import siap.siep.sanzionesostitutiva.model.SanzioneSostitutivaModel;
import siap.siep.sentenza.model.SentenzaModel;
import siap.siep.util.SIEPLookupRemote;
import siap.util.SIAPPathProperties;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class ActPrelevaDatiFascicolo extends ActWsBase {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	CodiciSiesNscModel lCodiciSiesNscModel;

	String lCodCentralizzato = "";

	private NscProperties mProperties = NscProperties.getInstance();

	// NUOVA INFRASTRUTTURA: aggiunte variabili di classe
	private SIAPPathProperties mPathProperties = SIAPPathProperties.getInstance();
	private String mPath = null;

	public String processRequest() throws Exception {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.info("######## ActPrelevaDatiFascicolo ##########");

		BigDecimal aId;
		int lNumeroOmonimo = 0;
		long lProgAnagraficaOmonimo = 0;
		String lCognomeOmonimo = "", lNomeOmonimo = "", lFlagSessoOmonimo = "";
		String lDataNascitaOmonimo = "";
		String lFromOmonimi = "", lCodiPaeseCittad = "-";
		String lFlagSentenzaApplicazPena = "", lFlagGiudizioAbbreviato = "";

		// Verifica se c'è il parametro nella request se è presente la ActPrelevaDatiFascicolo è stata
		// chiamata dalla jsp ListaOmonimi.
		if (!isRequestParameterNullObj("FromOmonimi")) {
			lFromOmonimi = getRequestStringParameter("FromOmonimi");
			if (lFromOmonimi.equals("SI")) {
				// GESTIONE OMONIMI - Caso in cui è stato selezionato un soggetto presente nell'array inviato
				// da NSC
				lNumeroOmonimo = Integer.parseInt(getRequestStringParameter("Elemento"));
				lProgAnagraficaOmonimo = Long.parseLong(getRequestStringParameter("ProgAnagraficaOmonimo"));
				lCognomeOmonimo = getRequestStringParameter("CognomeOmonimo");
				lNomeOmonimo = getRequestStringParameter("NomeOmonimo");
				lFlagSessoOmonimo = getRequestStringParameter("FlagSessoOmonimo");
				lDataNascitaOmonimo = getRequestStringParameter("DataNascitaOmonimo");

				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.info("###############################################");
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.info("Numero Elemento Omonimi:" + lNumeroOmonimo);
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.info("PROG_ANAGRAFICA NSC:" + lProgAnagraficaOmonimo);
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.info("Data Nascita:" + lDataNascitaOmonimo);
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.info("###############################################");
			}
		}

		if (!this.isSessionAttributeNullObj("fascicolo")) {
			// Se provengo del Menù Scelta Rapida devo recuparere i dati dalla sessione
			// Cerco in sessione il fascicolo per recuperare l'id
			aId = ((FascicoloSiepModel) getSessionAttribute("fascicolo")).getIdFascicoloSiep();
		} else {
			// NON dovrebbe mai entrare
			// Se non ho l'id fascicolo ne sulla request ne in sessione restituisco la
			// pagina di ricerca fascicolo
			return ICostantiFascicoloSiep.REDIRECT_FASCICOLO_RICERCATO
					+ "siap.siep.fascicolo.action.ActLoadDettaglioFascicolo";
		}

		// Tabelle SIES:FASCICOLO_SIEP
		// I/F della classe fascicolo contiene tutti i metodi della classe
		IFascicoloSiep lCtrlFas = SIEPLookupRemote.getFascicoloSiepRemote();
		DettaglioFascicoloModel lDettaglio = lCtrlFas.ExDettaglioFascicoloSiep(aId);

		FascicoloSiepModel lFascicolo = lDettaglio.getFascicoloSiep();

		// Leggiamo l'Anagrafica del Soggetto impostando l'ID Soggetto della tabella Fascicolo
		// Tabelle NSC:CG_ANAG_PERS_FISICHE SIES:SOGGETTO
		SoggettoModel lSoggetto = lDettaglio.getFascicoloSiep().getSoggetto();

		// Leggiamo i dati della sentenza --> Tab: Sentenza
		// Tabelle NSC:CG_IMPUGNAZIONI - CG_PROVV_GIUDIZIARI
		// SIES:SENTENZA

		SentenzaModel lSentenza = lDettaglio.getFascicoloSiep().getSentenza();

		// Tabelle NSC:CG_REATI - CG_CIRCOSTANZE
		// SIES:REATO - CIRCOSTANZE

		// List lReatiCircostanze = lDettaglio.getReatiCircostanze();
		List lCircostanze = lDettaglio.getCircostanze();

		// ------> Inizio Blocco Solo se passa la girata sui REATI -----------------------------------------
		IReato lCtrlReato = SIEPLookupRemote.getReatoRemote();
		List lReatiCircostanze = lCtrlReato.ExRicercaReatoCircostanzaByFascicoloOnlyNsc(aId.longValue());
		// ----> Fine Blocco ------------------------------------------------------------------------------

		// Tabelle NSC:CG_DISPOSITIVI - CG_BENEFICI_SOSTITUZIONI_PENE
		// SIES:REATO - PENA_COMPLESSIVA - SANZIONE_SOSTITUTIVA
		PenaComplessivaModel lPenaComp = new PenaComplessivaModel();
		SanzioneSostitutivaModel lSanzSost = new SanzioneSostitutivaModel();

		if (lDettaglio.getPenaComplessivaSanzioneSostitutiva() != null) {
			lPenaComp = lDettaglio.getPenaComplessivaSanzioneSostitutiva().getPenaComplessiva();
			lSanzSost = lDettaglio.getPenaComplessivaSanzioneSostitutiva().getSanzioneSostitutiva();
		}

		// Tabelle NSC:CG_BENEFICI_SOSTITUZIONI_PENE SIES:BENEFICIO
		List lBenefici = lDettaglio.getBenefici();

		// Tabelle NSC:CG_MISURE_DI_SICUREZZA SIES: MISURA_SICUREZZA
		List lMisureSicurezza = lDettaglio.getMisureSicurezza();

		// Tabelle NSC:CG_PENE_ACCESSORIE SIES:PENA_ACCESSORIA
		List lPeneAccessorie = lDettaglio.getPeneAccessorie();

		// PENE_AGGIUNTIVE - SIES:CONTINUAZIONE
		IContinuazione lCtrl = SIEPLookupRemote.getContinuazioneRemote();
		List lContinuazioneModel = lCtrl
				.ExRicercaContinuazioneByIDPenaComplessiva(lPenaComp.getIdPenaComplessiva());

		UtenteModel lUteMod = new UtenteModel(
				(UtenteModel) getSessionAttribute(ICostantiSecurity.SESSION_UTENTE_CONNESSO));

		/*------------------------------------------------------------------------------*/
		/* Inizio Scrittura XML */
		/*------------------------------------------------------------------------------*/
		TRASFERIMENTODocument lTrasfDoc = TRASFERIMENTODocument.Factory.newInstance();
		// Root Element
		TRASFERIMENTODocument.TRASFERIMENTO ElementTrasf = lTrasfDoc.addNewTRASFERIMENTO();
		// Element DATI_CHIAMATA_TRASFERIMENTO
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
		DatiUtente.setUSERNAMEADN(lUteMod.getUserAdn());

		/*******************************************************************************/
		/* Element DATI_CHIAMATA_TRASFERIMENTO - DATI FASCICOLO */
		/*******************************************************************************/

		DATIFASCICOLODocument.DATIFASCICOLO DatiFascicolo = DatiChiamataTrasf.addNewDATIFASCICOLO();

		DatiFascicolo.setANNOFASCICOLO(lFascicolo.getChiaveAnno().intValue());
		DatiFascicolo.setNUMEROFASCICOLO(lFascicolo.getChiaveProgr().toString());

		/*******************************************************************************/
		/* Element DATI_CHIAMATA_TRASFERIMENTO - CHIAVI */
		/*******************************************************************************/
		CHIAVIDocument.CHIAVI Chiavi = DatiChiamataTrasf.addNewCHIAVI();
		if (lSoggetto.getKeySoggNsc() != null && !"".equals(lSoggetto.getKeySoggNsc().toString())) {
			Chiavi.setKANSC(lSoggetto.getKeySoggNsc().longValue()); // Caso di TIPO_OPERAZIONE UPDATE
		}

		Chiavi.setKASIES(lFascicolo.getSogIdSoggetto().longValue());

		if (lFascicolo.getKeyProvvNsc() != null && !"".equals(lFascicolo.getKeyProvvNsc().toString())) {
			Chiavi.setKPNSC(lFascicolo.getKeyProvvNsc().longValue()); // Caso di TIPO_OPERAZIONE UPDATE
		}
		Chiavi.setKPSIES(lFascicolo.getIdFascicoloSiep().longValue());

		// ArrayChiaviReatiDocument.ArrayChiaviReati ArrayChiaviReati = Chiavi.addNewArrayChiaviReati();
		ArrayChiaviReatiDocument.ArrayChiaviReati ArrayChiaviReati = null;

		/*******************************************************************************/
		/* Element DATI_CHIAMATA_TRASFERIMENTO - DATI_OPERAZIONE */
		/*******************************************************************************/
		DATIOPERAZIONEDocument.DATIOPERAZIONE DatiOperazione = DatiChiamataTrasf.addNewDATIOPERAZIONE();
		DatiOperazione.setENTITA(ENTITADocument.ENTITA.P); //

		// Caso di re-invio flusso a seguito di OMONIMIA
		if (!isRequestParameterNullObj("FromOmonimi")) {
			if (lFromOmonimi.equals("SI")) {
				DatiOperazione.setOPERAZIONE(OPERAZIONEDocument.OPERAZIONE.SKIP_INSERT_S); // Associa OMONIMO
			} else {
				DatiOperazione.setOPERAZIONE(OPERAZIONEDocument.OPERAZIONE.FORCE_INSERT_S); // OMONIMO NSC non
																							// trovato, NSC
																							// deve forzare il
																							// nostro Soggetto
			}
		} else {
			if (lFascicolo.getKeyProvvNsc() != null) {
				DatiOperazione.setOPERAZIONE(OPERAZIONEDocument.OPERAZIONE.UPDATE);
			} else {
				DatiOperazione.setOPERAZIONE(OPERAZIONEDocument.OPERAZIONE.INSERT);
			}
		}

		/*******************************************************************************/
		/* ELEMENT DATI_CHIAMATA_TRASFERIMENTO - ANAGRAFICA - DATI_ANAGRAFICI */
		/*******************************************************************************/
		ANAGRAFICADocument.ANAGRAFICA Anagrafica = DatiChiamataTrasf.addNewANAGRAFICA();
		DATIANAGRAFICIDocument.DATIANAGRAFICI DatiAnagrafici = Anagrafica.addNewDATIANAGRAFICI();

		// Gestione Campi Data
		DATA lData = DATA.Factory.newInstance();

		if (!isRequestParameterNullObj("FromOmonimi") && lFromOmonimi.equals("SI")) {
			// NSC ha risposto con degli OMONIMI, REINVIAMO il flusso valorizzando i DATI_ANAGRAFICI con il
			// Prog_ANAGRAFICA selezionato
			// nella gestione OMONIMI.

			DatiAnagrafici.setPROGANAGRAFICA(lProgAnagraficaOmonimo);
			DatiAnagrafici.setPERSCOGNOME(lCognomeOmonimo);
			DatiAnagrafici.setPERSNOME(lNomeOmonimo);
			DatiAnagrafici.setFLAGSESSOMF(lFlagSessoOmonimo);
			if (lDataNascitaOmonimo != null && !lDataNascitaOmonimo.equals("")) {
				lData.setGIORNO(lDataNascitaOmonimo.substring(6, 8));
				lData.setMESE(lDataNascitaOmonimo.substring(4, 6));
				lData.setANNO(lDataNascitaOmonimo.substring(0, 4));

				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.info("GiornoOmonimo:" + lDataNascitaOmonimo.substring(6, 8));
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.info("MeseOmonimo:" + lDataNascitaOmonimo.substring(4, 6));
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.info("AnnoOmonimo:" + lDataNascitaOmonimo.substring(0, 4));

				DatiAnagrafici.setDATANASCITA(lData);
			}
			DatiAnagrafici.setCODILUOGONASCITA("");
			DatiAnagrafici.setCODISTATOESTERONAS("");

			DatiAnagrafici.setFLAGDECEDUTOSN("");

		} else {
			DatiAnagrafici.setPROGANAGRAFICA(lSoggetto.getIdSoggetto().longValue());
			DatiAnagrafici.setPERSCOGNOME(lSoggetto.getCognome());
			DatiAnagrafici.setPERSNOME(lSoggetto.getNome());

			// ----> DECODIFICA COMUNE NASCITA
			if (lSoggetto.getCodComuneNascita() != null && !lSoggetto.getCodComuneNascita().equals("-")) {
				lCodiciSiesNscModel = new CodiciSiesNscModel();
				lCodiciSiesNscModel.setCoDomain("COMUNE");
				lCodiciSiesNscModel.setCoSies(lSoggetto.getCodComuneNascita());
				CodiciSiesNscModel aCodiciSIESNSCModel = DecodificaCodiceSies(lCodiciSiesNscModel);
				lCodCentralizzato = aCodiciSIESNSCModel.getCoCodcentr();
				DatiAnagrafici.setCODILUOGONASCITA(lCodCentralizzato);
			}

			// ----> DECODIFICA STATO NASCITA
			if (lSoggetto.getCodStatoNascita() != null && !lSoggetto.getCodStatoNascita().equals("-")) {
				lCodiciSiesNscModel = new CodiciSiesNscModel();
				lCodiciSiesNscModel.setCoDomain("NAZIONE");
				lCodiciSiesNscModel.setCoSies(lSoggetto.getCodStatoNascita());
				CodiciSiesNscModel aCodiciSIESNSCModel = DecodificaCodiceSies(lCodiciSiesNscModel);
				lCodCentralizzato = aCodiciSIESNSCModel.getCoCodcentr();
				DatiAnagrafici.setCODISTATOESTERONAS(lCodCentralizzato);
				// lCodiPaeseCittad=lCodCentralizzato; // Serve per valorizzare l'elemento CODI_PAESE_CITTAD
				// nel PROVVEDIMENTO
			}

			if (lSoggetto.getNazionalita() != null && !lSoggetto.getNazionalita().equals("-")) {
				// paolo 30/06/2010
				// abbiamo fatto la modifica della nazionalita che ora è di tre caratteri,
				// ma dobbiamo ancora allinearci con il casellario per ora faccio una manipolazione per
				// lasciare I per gli italiani e E per gli esteri
				if (lSoggetto.getNazionalita().equals("039")) {
					lSoggetto.setNazionalita("I");
				} else {
					lSoggetto.setNazionalita("E");
				}
				// fine paolo 30/06/2010

				lCodiPaeseCittad = lSoggetto.getNazionalita();
			}

			if (lSoggetto.getDescComuneNascitaEstero() != null
					&& !lSoggetto.getDescComuneNascitaEstero().equals("")) {
				DatiAnagrafici.setDESCCOMUNEESTERO(lSoggetto.getDescComuneNascitaEstero());
			}

			// Essendo opzionale nillable="true" minOccurs="0" preferisco scrivere l'elemento solo se diverso
			// da Null
			lData.setGIORNO("");
			lData.setMESE("");
			lData.setANNO("");

			if (lSoggetto.getDataNascita() != null) {
				lData.setGIORNO(DateUtils.getDateToString(lSoggetto.getDataNascita(), "dd"));
				lData.setMESE(DateUtils.getDateToString(lSoggetto.getDataNascita(), "MM"));
				lData.setANNO(DateUtils.getDateToString(lSoggetto.getDataNascita(), "yyyy"));
				DatiAnagrafici.setDATANASCITA(lData);
			} else {
				if (lSoggetto.getAnnoNascita() != null) {
					lData.setANNO(lSoggetto.getAnnoNascita().toString());
				}
				if (lSoggetto.getMeseNascita() != null) {
					lData.setMESE(lSoggetto.getMeseNascita().toString());
				}
				DatiAnagrafici.setDATANASCITA(lData);
			}

			DatiAnagrafici.setFLAGSESSOMF(lSoggetto.getSesso());

			if (lSoggetto.getCodFiscale() != null && !lSoggetto.getCodFiscale().equals("")) {
				DatiAnagrafici.setCODIFISCALE(lSoggetto.getCodFiscale());
			}

			if (lSoggetto.getCodAfis() != null && !lSoggetto.getCodAfis().equals("")) {
				DatiAnagrafici.setCODIIMPRONTADIGITALE(lSoggetto.getCodAfis());
			}

			if (lSoggetto.getPaternita() != null && !lSoggetto.getPaternita().equals("")) {
				DatiAnagrafici.setPERSPATERNITA(lSoggetto.getPaternita());
			}

			if (lSoggetto.getCognomeMadre() != null && !lSoggetto.getCognomeMadre().equals("")) {
				DatiAnagrafici.setPERSCOGNOMEMADRE(lSoggetto.getCognomeMadre());
			}

			if (lSoggetto.getNomeMadre() != null && !lSoggetto.getNomeMadre().equals("")) {
				DatiAnagrafici.setPERSNOMEMADRE(lSoggetto.getNomeMadre());
			}

			if (lSoggetto.getAttoNascita() != null && !lSoggetto.getAttoNascita().equals("")) {
				DatiAnagrafici.setNUMEATTONASCITA(lSoggetto.getAttoNascita().trim());
			}

			if (lSoggetto.getNote() != null && !lSoggetto.getNote().equals("")) {
				DatiAnagrafici.setDESCANNOTAZIONE(lSoggetto.getNote());
			}

			DatiAnagrafici.setFLAGDECEDUTOSN("N");
		}

		/*******************************************************************************/
		/* ELEMENT DATI_CHIAMATA_TRASFERIMENTO - ANAGRAFICA - PROCEDIMENTO */
		/*******************************************************************************/
		PROCEDIMENTODocument.PROCEDIMENTO Procedimento = Anagrafica.addNewPROCEDIMENTO();

		if (lFascicolo.getDataIrrevocabilita() != null) {
			lData.setGIORNO(DateUtils.getDateToString(lFascicolo.getDataIrrevocabilita(), "dd"));
			lData.setMESE(DateUtils.getDateToString(lFascicolo.getDataIrrevocabilita(), "MM"));
			lData.setANNO(DateUtils.getDateToString(lFascicolo.getDataIrrevocabilita(), "yyyy"));
		} else {
			lData.setGIORNO("");
			lData.setMESE("");
			lData.setANNO("");
		}
		Procedimento.setDATAPASSAGGIOGIUDICATO(lData);

		// NUMERO e ANNO NOTIZIA REATO valorizzare solo se diversi da null (nillable="true" minOccurs="0")
		if (lSentenza.getNumeroRegePm() != null && !lSentenza.getNumeroRegePm().equals("")) {
			Procedimento.setNUMENOTIZIAREATO(lSentenza.getNumeroRegePm());
		}

		if (lSentenza.getAnnoRegePm() != null) {
			Procedimento.setANNONOTIZIAREATO(lSentenza.getAnnoRegePm().intValue());
		}

		if (lSentenza.getCodTipoProvvedimento() == "05") // Sentenza Straniera
		{
			Procedimento.setFLAGSENTENZASTRANIERA("S");
		} else {
			Procedimento.setFLAGSENTENZASTRANIERA("N");
		}

		if (lFascicolo.getCodOperatoreInserimento().substring(0, 4).equals("res-")) {
			Procedimento.setFLAGMIGRATOSN("S");
		} else {
			Procedimento.setFLAGMIGRATOSN("N");
		}

		// Element DATI_CHIAMATA_TRASFERIMENTO - ANAGRAFICA - PROCEDIMENTO - SENTENZA_STRANIERA
		/********************************************************************/
		/* Non abbiamo campi relativi alla SENTENZA_STRANIERA e CERTIFICATO */
		/********************************************************************/

		/*****************************************************************************************************************/
		/*
		 * DATI_CHIAMATA_TRASFERIMENTO - ANAGRAFICA - PROCEDIMENTO - ARRAYPRIMOGRADO - PRIMO_GRADO -
		 * DATI_PROVVEDIMENTO
		 */
		/*****************************************************************************************************************/
		ArrayPrimoGradoDocument.ArrayPrimoGrado ArrayPrimoGrado = null;

		/********************************************************************************************************************/
		/*
		 * DATI_CHIAMATA_TRASFERIMENTO - ANAGRAFICA - PROCEDIMENTO - ARRAYSECONDOGRADO - SECONDO_GRADO -
		 * DATI_PROVVEDIMENTO
		 */
		/********************************************************************************************************************/
		ArraySecondoGradoDocument.ArraySecondoGrado ArraySecondoGrado = null;

		// Se l'autorità emittente del Titolo Esecutivo è di 2° allora scriviamo in Altro Grado di Giudizio il
		// 1°
		if (lSentenza.getCodTipoAutoritaEmittente().equals("CAP")
				|| lSentenza.getCodTipoAutoritaEmittente().equals("CAS")
				|| lSentenza.getCodTipoAutoritaEmittente().equals("CASAP")
				|| lSentenza.getCodTipoAutoritaEmittente().equals("CAPSM")
				|| lSentenza.getCodTipoAutoritaEmittente().equals("CAPMI")) {
			if (lSentenza.getDataProvvRif() != null) { // Controlliamo se è stata emessa una sentenza di 2°
				ArrayPrimoGrado = Procedimento.addNewArrayPrimoGrado();
				PRIMOGRADODocument.PRIMOGRADO PrimoGrado = ArrayPrimoGrado.addNewPRIMOGRADO();
				IMPUGNAZIONEDocument.IMPUGNAZIONE DatiImpugnazionePrimoGrado = PrimoGrado
						.addNewIMPUGNAZIONE();
				CaricaImpugnazione(DatiImpugnazionePrimoGrado, lSentenza, "PGCAP", "1G");
			}
		} else {
			// Se l'autorità emittente del Titolo Esecutivo è di 1° allora scriviamo in Altro Grado di
			// Giudizio il 2°
			if (lSentenza.getDataProvvRif() != null) { // Controlliamo se è stata emessa una sentenza di 2°
				ArraySecondoGrado = Procedimento.addNewArraySecondoGrado();
				SECONDOGRADODocument.SECONDOGRADO SecondoGrado = ArraySecondoGrado.addNewSECONDOGRADO();
				IMPUGNAZIONEDocument.IMPUGNAZIONE DatiImpugnazioneSecondoGrado = SecondoGrado
						.addNewIMPUGNAZIONE();
				CaricaImpugnazione(DatiImpugnazioneSecondoGrado, lSentenza, "PM", "2G");
			}
		}

		/**********************************************************************************************************/
		/*
		 * DATI_CHIAMATA_TRASFERIMENTO - ANAGRAFICA - PROCEDIMENTO - ARRAY_CASSAZIONE - Cassazione -
		 * Impugnazione
		 */
		/**********************************************************************************************************/
		ArrayCassazioneDocument.ArrayCassazione ArrayCassazione = null;

		// Verifico la presenza del 3 Grado di Giudizio (Cassazione)
		if (lSentenza.getCodTipoDecisioneCassazione() != null
				&& !lSentenza.getCodTipoDecisioneCassazione().equals("-")) {
			ArrayCassazione = Procedimento.addNewArrayCassazione();
			CASSAZIONEDocument.CASSAZIONE Cassazione = ArrayCassazione.addNewCASSAZIONE();
			IMPUGNAZIONEDocument.IMPUGNAZIONE Impugnazione = Cassazione.addNewIMPUGNAZIONE();

			Impugnazione.setIDENTIFICATIVO(lSentenza.getIdSentenza().longValue());

			// ----> DECODIFICA CODTIPO_DECISIONE_CASSAZIONE
			lCodiciSiesNscModel = new CodiciSiesNscModel();
			lCodiciSiesNscModel.setCoDomain("TIPO_DECISIONE_CASSAZIONE");
			lCodiciSiesNscModel.setCoSies(lSentenza.getCodTipoDecisioneCassazione());
			CodiciSiesNscModel aCodiciSIESNSCModel = DecodificaCodiceSies(lCodiciSiesNscModel);
			lCodCentralizzato = aCodiciSIESNSCModel.getCoCodcentr();
			Impugnazione.setCODITIPORIFERIMENTO(lCodCentralizzato);

			if (lFascicolo.getDataIrrevocabilita() != null) {
				lData.setGIORNO(DateUtils.getDateToString(lFascicolo.getDataIrrevocabilita(), "dd"));
				lData.setMESE(DateUtils.getDateToString(lFascicolo.getDataIrrevocabilita(), "MM"));
				lData.setANNO(DateUtils.getDateToString(lFascicolo.getDataIrrevocabilita(), "yyyy"));
			} else {
				lData.setGIORNO("");
				lData.setMESE("");
				lData.setANNO("");
			}
			Impugnazione.setDATAPROVVEDIMENTO(lData);

			Impugnazione.setCODISEDEAUTORITAPRINDIST("058091"); // Codice Centralizzato di Roma

			Impugnazione.setCODIAUTORITA("6"); // Codice Centralizzato CASSAZIONE

			if (lSentenza.getAnnoSentenzaCassazione() != null) {
				Impugnazione.setANNOSENTENZA(lSentenza.getAnnoSentenzaCassazione().intValue());
			}

			if (lSentenza.getNumeroSentenzaCassazione() != null
					&& !lSentenza.getNumeroSentenzaCassazione().equals("")) {
				Impugnazione.setNUMEROSENTENZA(lSentenza.getNumeroSentenzaCassazione());
			}

			if (lSentenza.getAnnoRegistroGenerale() != null) {
				Impugnazione.setANNOREGISTROGENERALE(lSentenza.getAnnoRegistroGenerale().intValue());
			}

			if (lSentenza.getNumeroRegistroGenerale() != null
					&& !lSentenza.getNumeroRegistroGenerale().equals("")) {
				Impugnazione.setNUMEROREGISTROGENERALE(lSentenza.getNumeroRegistroGenerale());
			}
		}

		/***************************************************************************************************/
		/* DATI_CHIAMATA_TRASFERIMENTO - ANAGRAFICA - PROCEDIMENTO - TITOLO_ESECUTIVO - DATI_PROVVEDIMENTO */
		/***************************************************************************************************/
		TITOLOESECUTIVODocument.TITOLOESECUTIVO TitoloEsecutivo = Procedimento.addNewTITOLOESECUTIVO();
		DATIPROVVEDIMENTODocument.DATIPROVVEDIMENTO DatiProvvedimento = TitoloEsecutivo
				.addNewDATIPROVVEDIMENTO();

		DatiProvvedimento.setFLAGSENTENZARIUNITA("N");

		/*
		 * Determiniamo il valore del Flag Sentenza Applicazione Pena e Flag Giudizio Abbreviato visto che
		 * quello buono viene scritto sui rispettivi campi della tabella CIRCOSTANZA
		 */
		if (lCircostanze.size() > 0) {
			CircostanzaModel lModelCircostanza1;
			lModelCircostanza1 = (CircostanzaModel) lCircostanze.get(0);
			// [MEV REL. 5.0] - Gestione Eliminazione dalla Sentenza del FlagSentenzaApplicazPena -
			// FlagGiudizioAbbreviato
			// lSentenza.setFlagSentenzaApplicazPena(lModelCircostanza1.getFlagSentenzaApplicazPena());
			// lSentenza.setFlagGiudizioAbbreviato(lModelCircostanza1.getFlagGiudizioAbbreviato());
			lFlagSentenzaApplicazPena = lModelCircostanza1.getFlagSentenzaApplicazPena();
			lFlagGiudizioAbbreviato = lModelCircostanza1.getFlagGiudizioAbbreviato();
		}
		// ----> Fine Blocco che determina il valore del Flag Sentenza Applicazione Pena e Flag Giudizio
		// Abbreviato

		CaricaProvvedimento(DatiProvvedimento, lSentenza, lCodiPaeseCittad, lFlagSentenzaApplicazPena,
				lFlagGiudizioAbbreviato);

		/****************************************************************/
		/* ARRAY presenti nel TITOLO ESECUTIVO */
		/****************************************************************/

		// ---> Element DATI_CHIAMATA_TRASFERIMENTO - ANAGRAFICA - PROCEDIMENTO - TITOLO_ESECUTIVO -
		// DATI_PROVVEDIMENTO - ArrayReati
		ArrayReatiDocument.ArrayReati ArrayReati = null;
		ReatoModel lReato;

		REATODocument.REATO Reato;
		ArrayCircostanzeDocument.ArrayCircostanze ReatoArrayCircostanze = null;
		CIRCOSTANZADocument.CIRCOSTANZA ReatoCircostanza = null;

		ArrayCircostanzeSpecialiDocument.ArrayCircostanzeSpeciali ReatoArrayCircostanzeSpeciali = null;

		/****************************************************************/
		/* REATO */
		/****************************************************************/

		Vector lArrayNumOrdineReato = new Vector();
		for (int i = 0; i <= lReatiCircostanze.size() - 1; i++) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.info("Reati");

			if (i == 0) {
				ArrayReati = DatiProvvedimento.addNewArrayReati();
				ArrayChiaviReati = Chiavi.addNewArrayChiaviReati();
			}

			Reato = ArrayReati.addNewREATO();

			lReato = ((ReatoCircostanzaModel) lReatiCircostanze.get(i)).getReato();
			ReatoModel[] lCircostanzaModel = ((ReatoCircostanzaModel) lReatiCircostanze.get(i))
					.getCircostanze();

			// Caso in cui esiste SOLO UNA circostanza speciale (110, 56 81)
			// 27-03-2009 - Non può esistere capo di imp. per solo 110 o 56 o 81, è un erroraccio
			// dell'operatore che ha inserito.
			if (lReato.getArticolo() != null && (lReato.getArticolo().equals("110")
					|| lReato.getArticolo().equals("56") || lReato.getArticolo().equals("81"))) {
				setRequestAttribute(IWebConstants.MESSAGE_TEXT,
						"Attenzione ! Prima di trasferire il procedimento completare l'inserimenti dei Reati. Non può esistere un capo d'imputazione solo per l'articolo "
								+ lReato.getArticolo());
				RedirectTo lRedirigi = new RedirectTo();
				lRedirigi.setPage(IWebConstants.PG_MAIN);
				lRedirigi.setAction("siap.siep.fascicolo.action.ActLoadDettaglioFascicolo");
				setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi);
				return IWebConstants.PG_MESSAGE;
			}
			// Caso in cui ci sono SOLO e PIU' circostanze (110, 56, 81) senza Reato.
			if (lReato.getIdReato() == null) {
				setRequestAttribute(IWebConstants.MESSAGE_TEXT,
						"Attenzione ! Prima di trasferire il procedimento completare l'inserimenti dei Reati.");
				RedirectTo lRedirigi = new RedirectTo();
				lRedirigi.setPage(IWebConstants.PG_MAIN);
				lRedirigi.setAction("siap.siep.fascicolo.action.ActLoadDettaglioFascicolo");
				setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi);
				return IWebConstants.PG_MESSAGE;
			}

			// Paolo Cherubini 21/04/2011 controllo presenza fonte e articolo
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.info("######## controllo presenza fonte e articolo");
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.info("######## id ----------> " + lReato.getIdReato());
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.info("######## progr -------> " + lReato.getProgrReato());
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.info("######## fonte -------> " + lReato.getCodFonte());
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.info("######## art ---------> " + lReato.getArticolo());

			if (lReato.getCodFonte() == null || lReato.getCodFonte().equals("-")
					|| lReato.getCodFonte().equals("") || lReato.getArticolo() == null
					|| lReato.getArticolo().equals("-") || lReato.getArticolo().equals("")) {
				setRequestAttribute(IWebConstants.MESSAGE_TEXT,
						"Attenzione! Prima di trasferire il procedimento completare l'inserimenti dei Reati. "
								+ "Fonte e Articolo devono essere entrambi presenti.");
				RedirectTo lRedirigi = new RedirectTo();
				lRedirigi.setPage(IWebConstants.PG_MAIN);
				lRedirigi.setAction("siap.siep.fascicolo.action.ActLoadDettaglioFascicolo");
				setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi);
				return IWebConstants.PG_MESSAGE;
			} // fine paolo

			Reato.setPROGREATO(lReato.getIdReato().longValue());

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.info("Reato1:" + lReato.getIdReato());
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.info("Reato2:" + lReato.getIdReato().longValue());

			// ----> Scriviamo L'IDENTIFICATIVO DEL REATO nell'ArrayChiaviReati
			ArrayChiaviReatiDocument.ArrayChiaviReati.CHIAVIREATO ChiaviReato = ArrayChiaviReati
					.addNewCHIAVIREATO();
			ChiaviReato.setKRSIES(lReato.getIdReato().longValue());
			// -----> Fine Blocco Scrittura ArrayChiaviReati

			Reato.setNUMEORDINEREATO(lReato.getProgrReato().intValue());

			// Valorizzo il vettore per scriverlo successivamente nell'array progressivi Reati del dispositivo
			// della PENA_COMPLESSIVA
			lArrayNumOrdineReato.add(lReato.getIdReato());

			// ----> DECODIFICA COD_TIPO_REATO
			if (lReato.getCodTipoReato() != null && !lReato.getCodTipoReato().equals("-")) {
				lCodiciSiesNscModel = new CodiciSiesNscModel();
				lCodiciSiesNscModel.setCoDomain("TIPO_REATO");
				lCodiciSiesNscModel.setCoSies(lReato.getCodTipoReato());
				CodiciSiesNscModel aCodiciSIESNSCModel = DecodificaCodiceSies(lCodiciSiesNscModel);
				lCodCentralizzato = aCodiciSIESNSCModel.getCoCodcentr();
				Reato.setCODITIPOREATO(lCodCentralizzato);
			} else {
				Reato.setCODITIPOREATO("");
			}

			if (lReato.getDescLuogo() != null && !lReato.getDescLuogo().equals("")) {
				if (lReato.getDescLuogo().length() > 150) {
					Reato.setDESCULTERIORELUOGOREATO(lReato.getDescLuogo().substring(0, 150));
				} else {
					Reato.setDESCULTERIORELUOGOREATO(lReato.getDescLuogo());
				}
			}

			// ----> DECODIFICA COD_PERIODO_CONSUMAZIONE
			if (lReato.getCodPeriodoConsumazione() != null
					&& !lReato.getCodPeriodoConsumazione().equals("-")) {
				if (lReato.getCodPeriodoConsumazione().equals("12")) {
					Reato.setCODIPERIODOCONSUMAZIONE("15");
				} else if (lReato.getCodPeriodoConsumazione().equals("05")) {
					Reato.setCODIPERIODOCONSUMAZIONE("19");
				} else if (lReato.getCodPeriodoConsumazione().equals("06")) {
					Reato.setCODIPERIODOCONSUMAZIONE("20");
				} else {
					lCodiciSiesNscModel = new CodiciSiesNscModel();
					lCodiciSiesNscModel.setCoDomain("PERIODO_CONSUMAZIONE");
					lCodiciSiesNscModel.setCoSies(lReato.getCodPeriodoConsumazione());
					CodiciSiesNscModel aCodiciSIESNSCModel = DecodificaCodiceSies(lCodiciSiesNscModel);
					lCodCentralizzato = aCodiciSIESNSCModel.getCoCodcentr();
					Reato.setCODIPERIODOCONSUMAZIONE(lCodCentralizzato);
				}
			}

			/*---> Data Reato non è mai valorizzata
			if (lReato.getDataReato() != null)
			{
			    lData.setGIORNO(DateUtils.getDateToString(lReato.getDataReato(), "dd"));
			    lData.setMESE(DateUtils.getDateToString(lReato.getDataReato(), "MM"));
			    lData.setANNO(DateUtils.getDateToString(lReato.getDataReato(), "yyyy"));
			    Reato.setDATAREATO(lData);
			}
			 */

			lData.setGIORNO("");
			lData.setMESE("");
			lData.setANNO("");

			if (lReato.getDataInizio() != null) {
				lData.setGIORNO(DateUtils.getDateToString(lReato.getDataInizio(), "dd"));
				lData.setMESE(DateUtils.getDateToString(lReato.getDataInizio(), "MM"));
				lData.setANNO(DateUtils.getDateToString(lReato.getDataInizio(), "yyyy"));
				// 01 = "Commesso in Data [data1]" - 02 = "Accertato in Data [data1]" Deciso con Eng il
				// 18/02/2009
				if (lReato.getCodPeriodoConsumazione() != null
						&& (lReato.getCodPeriodoConsumazione().equals("01")
								|| lReato.getCodPeriodoConsumazione().equals("02"))) {
					Reato.setDATAREATO(lData);
				} else {
					Reato.setDATAINIZIOREATO(lData);
				}
			} else {
				if (lReato.getAnnoInizio() != null) {
					lData.setANNO(lReato.getAnnoInizio().toString());
					if (lReato.getMeseInizio() != null) {
						lData.setMESE(lReato.getMeseInizio().toString());
					}
					if (lReato.getGiornoInizio() != null) {
						lData.setGIORNO(lReato.getGiornoInizio().toString());
					}

					if (lReato.getCodPeriodoConsumazione() != null
							&& (lReato.getCodPeriodoConsumazione().equals("01")
									|| lReato.getCodPeriodoConsumazione().equals("02"))) {
						Reato.setDATAREATO(lData);
					} else {
						Reato.setDATAINIZIOREATO(lData);
					}
				}
			}

			lData.setGIORNO("");
			lData.setMESE("");
			lData.setANNO("");

			if (lReato.getDataFine() != null) {
				lData.setGIORNO(DateUtils.getDateToString(lReato.getDataFine(), "dd"));
				lData.setMESE(DateUtils.getDateToString(lReato.getDataFine(), "MM"));
				lData.setANNO(DateUtils.getDateToString(lReato.getDataFine(), "yyyy"));
				Reato.setDATAFINEREATO(lData);
			} else {
				if (lReato.getAnnoFine() != null) {
					lData.setANNO(lReato.getAnnoFine().toString());
					if (lReato.getMeseFine() != null) {
						lData.setMESE(lReato.getMeseFine().toString());
					}
					if (lReato.getGiornoFine() != null) {
						lData.setGIORNO(lReato.getGiornoFine().toString());
					}
					Reato.setDATAFINEREATO(lData);
				}
			}

			// ----> DECODIFICA COD_FONTE
			if (lReato.getCodFonte() != null && !lReato.getCodFonte().equals("-")) {
				lCodiciSiesNscModel = new CodiciSiesNscModel();
				lCodiciSiesNscModel.setCoDomain("FONTE");
				lCodiciSiesNscModel.setCoSies(lReato.getCodFonte());
				CodiciSiesNscModel aCodiciSIESNSCModel = DecodificaCodiceSies(lCodiciSiesNscModel);
				lCodCentralizzato = aCodiciSIESNSCModel.getCoCodcentr();
				Reato.setCODITL(lCodCentralizzato);
			} else {
				Reato.setCODITL("");
			}

			// Solo se COD_FONTE e uno dei codice identificativi della legge speciale scriviamo ANNO e NUMERO
			// della Legge Speciale.
			if (Reato.getCODITL().equals("6") || Reato.getCODITL().equals("7")
					|| Reato.getCODITL().equals("8") || Reato.getCODITL().equals("9")
					|| Reato.getCODITL().equals("10") || Reato.getCODITL().equals("11")
					|| Reato.getCODITL().equals("12") || Reato.getCODITL().equals("13")
					|| Reato.getCODITL().equals("14") || Reato.getCODITL().equals("15")
					|| Reato.getCODITL().equals("16") || Reato.getCODITL().equals("17")
					|| Reato.getCODITL().equals("18") || Reato.getCODITL().equals("19")
					|| Reato.getCODITL().equals("20") || Reato.getCODITL().equals("21")
					|| Reato.getCODITL().equals("22")) {
				// ANNO LEGGE SPECIALE
				if (lReato.getAnnoFonte() != null) {
					Reato.setANNOLS(lReato.getAnnoFonte().intValue());
				}
				// NUMERO LEGGE SPECIALE
				if (lReato.getNumeroFonte() != null) {
					if (isNumeric(lReato.getNumeroFonte()) == true) {
						Reato.setNUMELS(Integer.parseInt(lReato.getNumeroFonte()));
					} else {
						Reato.setNUMELS(00); // Se non sono numeri caricare 00
					}
				}
			}

			if (lReato.getArticolo() != null && !lReato.getArticolo().equals("")) // Obbligatorio
			{
				if (isNumeric(lReato.getArticolo()) == true) {
					Reato.setARTINUME(Integer.parseInt(lReato.getArticolo()));
				} else {
					Reato.setARTINUME(00); // Se non sono numeri caricare 00
				}
			} else {
				Reato.setARTINUME(0);
			}

			// ----> DECODIFICA COD_SOTTONUMERAZIONE
			if (lReato.getCodSottonumerazione() != null && !lReato.getCodSottonumerazione().equals("-")) {
				lCodiciSiesNscModel = new CodiciSiesNscModel();
				lCodiciSiesNscModel.setCoDomain("SOTTONUMERAZIONE");
				lCodiciSiesNscModel.setCoSies(lReato.getCodSottonumerazione());
				CodiciSiesNscModel aCodiciSIESNSCModel = DecodificaCodiceSies(lCodiciSiesNscModel);
				lCodCentralizzato = aCodiciSIESNSCModel.getCoCodcentr();
				Reato.setARTIBTQ(lCodCentralizzato);
			}

			// Concordato con ENG in quanto in base dati potrebbe esserci scritto come comma "PRIMO" ecc.
			if (lReato.getComma() != null && !lReato.getComma().equals("")) {
				if (isNumeric(lReato.getComma()) == true && lReato.getComma().trim().length() < 4) {
					Reato.setARTICOMMA(lReato.getComma());
				}
			}

			// [MEV REL. 5.0] - Gestione Comma qualificante
			if (lReato.getCommaQualificante() != null && !lReato.getCommaQualificante().equals("-")) {
				lCodiciSiesNscModel = new CodiciSiesNscModel();
				lCodiciSiesNscModel.setCoDomain("SOTTONUMERAZIONE");
				lCodiciSiesNscModel.setCoSies(lReato.getCommaQualificante());
				CodiciSiesNscModel aCodiciSIESNSCModel = DecodificaCodiceSies(lCodiciSiesNscModel);
				lCodCentralizzato = aCodiciSIESNSCModel.getCoCodcentr();
				Reato.setARTICOMMABTQ(lCodCentralizzato);
			}

			if (lReato.getLettera() != null && !lReato.getLettera().equals("")) {
				Reato.setARTILETTERA(lReato.getLettera());
			}

			if (lReato.getNumero() != null && !lReato.getNumero().equals("")) {
				Reato.setARTINUMELETTERACOMMA(lReato.getNumero());
			}

			if (lReato.getNote() != null && !lReato.getNote().equals("")) {
				Reato.setDESCIPOTESIDIREATO(lReato.getNote());
			}

			/*--------------------------------------------------------------------------------------- */
			/* Scrivo i dati della pena del singolo REATO, nel DISPOSITIVO dell'elemento REATO */
			/*--------------------------------------------------------------------------------------- */
			if (lReato.getNumAnniIsolamentoDiurno() != null || lReato.getNumMesiIsolamentoDiurno() != null
					|| lReato.getNumGiorniIsolamentoDiurno() != null || lReato.getNumAnni() != null
					|| lReato.getNumMesi() != null || lReato.getNumGiorni() != null
					|| (lReato.getCodTipoSanzione() != null && !lReato.getCodTipoSanzione().equals("-"))
					|| (lReato.getCodTipoPenaDetentiva() != null
							&& !lReato.getCodTipoPenaDetentiva().equals("-"))) {
				DISPOSITIVODocument.DISPOSITIVO ReatoDispositivo = Reato.addNewDISPOSITIVO();

				// ArrayProgressiviReati del DISPOSITIVO del REATO
				ArrayProgressiviReatiDocument.ArrayProgressiviReati ReatoDispositivoArrayProgressivi = ReatoDispositivo
						.addNewArrayProgressiviReati();
				ReatoDispositivoArrayProgressivi.addNumero(lReato.getIdReato().longValue());

				ReatoDispositivo.setPROGDISPOSITIVO(lReato.getIdReato().longValue());

				DURATA lDurata = DURATA.Factory.newInstance();

				// ISOLAMENTO DIURNO
				// Inizializziamo ANNI - MESI - GIORNI - ORE
				lDurata.setANNIDURATA(0);
				lDurata.setMESIDURATA(0);
				lDurata.setGIORNIDURATA(0);
				lDurata.setOREDURATA(0);

				if (lReato.getNumAnniIsolamentoDiurno() != null) {
					lDurata.setANNIDURATA(lReato.getNumAnniIsolamentoDiurno().intValue());
				}

				if (lReato.getNumMesiIsolamentoDiurno() != null) {
					lDurata.setMESIDURATA(lReato.getNumMesiIsolamentoDiurno().intValue());
				}

				if (lReato.getNumGiorniIsolamentoDiurno() != null) {
					lDurata.setGIORNIDURATA(lReato.getNumGiorniIsolamentoDiurno().intValue());
				}

				if (lDurata.getANNIDURATA() != 0 || lDurata.getMESIDURATA() != 0
						|| lDurata.getGIORNIDURATA() != 0) {
					ReatoDispositivo.setISOLAMENTODIURNO(lDurata);
				}

				// Inizializziamo ANNI - MESI - GIORNI - ORE (RECLUSIONE e ARRESTO)
				lDurata.setANNIDURATA(0);
				lDurata.setMESIDURATA(0);
				lDurata.setGIORNIDURATA(0);
				lDurata.setOREDURATA(0);

				if (lReato.getNumAnni() != null) {
					lDurata.setANNIDURATA(lReato.getNumAnni().intValue());
				}
				if (lReato.getNumMesi() != null) {
					lDurata.setMESIDURATA(lReato.getNumMesi().intValue());
				}
				if (lReato.getNumGiorni() != null) {
					lDurata.setGIORNIDURATA(lReato.getNumGiorni().intValue());
				}

				if (lDurata.getANNIDURATA() != 0 || lDurata.getMESIDURATA() != 0
						|| lDurata.getGIORNIDURATA() != 0) {
					ReatoDispositivo.setRECLUSIONE(lDurata);
					ReatoDispositivo.setARRESTO(lDurata);
				}

				if (lReato.getCodTipoSanzione().equals("01")) {
					if (lReato.getSanzionePecuniaria().intValue() > 0) {
						ReatoDispositivo.setIMPOMULTA(lReato.getSanzionePecuniaria());
						ReatoDispositivo.setCODIVALUTA("EUR");
					}
				}

				if (lReato.getCodTipoSanzione().equals("02")) {
					if (lReato.getSanzionePecuniaria().intValue() > 0) {
						ReatoDispositivo.setIMPOAMMENDA(lReato.getSanzionePecuniaria());
						ReatoDispositivo.setCODIVALUTA("EUR");
					}
				}

				// ----> DECODIFICA COD_TIPO_PENA_DETENTIVA
				if (lReato.getCodTipoPenaDetentiva() != null
						&& !lReato.getCodTipoPenaDetentiva().equals("-")) {
					lCodiciSiesNscModel = new CodiciSiesNscModel();
					lCodiciSiesNscModel.setCoDomain("TIPO_PENA_DETENTIVA");
					lCodiciSiesNscModel.setCoSies(lReato.getCodTipoPenaDetentiva());
					CodiciSiesNscModel aCodiciSIESNSCModel = DecodificaCodiceSies(lCodiciSiesNscModel);
					lCodCentralizzato = aCodiciSIESNSCModel.getCoCodcentr();
					ReatoDispositivo.setCODTIPOPENADETENTIVA(lCodCentralizzato);
				}
			}

			/*-----------------------------------------------------------*/
			/* Ciclo sulle n° circostanze afffogate nella tabella REATI */
			/*-----------------------------------------------------------*/
			int ContaCircostanza = 0, ContaCircostanzaSpecale = 0;
			for (int j = 0; j <= lCircostanzaModel.length - 1; j++) {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.info("Circostanze Affogate nei Reati");

				if (lCircostanzaModel[j].getArticolo() != null
						&& (lCircostanzaModel[j].getArticolo().equals("110")
								|| lCircostanzaModel[j].getArticolo().equals("56")
								|| lCircostanzaModel[j].getArticolo().equals("81"))) {
					if (ContaCircostanzaSpecale == 0) {
						ReatoArrayCircostanzeSpeciali = Reato.addNewArrayCircostanzeSpeciali();
						ContaCircostanzaSpecale++;
					}
					ReatoCircostanza = ReatoArrayCircostanzeSpeciali.addNewCIRCOSTANZA();
				} else {
					if (ContaCircostanza == 0) {
						ReatoArrayCircostanze = Reato.addNewArrayCircostanze();
						ContaCircostanza++;
					}
					ReatoCircostanza = ReatoArrayCircostanze.addNewCIRCOSTANZA();
				}

				/*
				 * if (j == 0) { ReatoArrayCircostanze = Reato.addNewArrayCircostanze(); }
				 *
				 * CIRCOSTANZADocument.CIRCOSTANZA ReatoCircostanza =
				 * ReatoArrayCircostanze.addNewCIRCOSTANZA();
				 */

				ReatoCircostanza.setPROGCIRCOSTANZE(lCircostanzaModel[j].getIdReato().longValue());

				// ----> DECODIFICA COD_FONTE
				if (lCircostanzaModel[j].getCodFonte() != null
						&& !lCircostanzaModel[j].getCodFonte().equals("-")) {
					lCodiciSiesNscModel = new CodiciSiesNscModel();
					lCodiciSiesNscModel.setCoDomain("FONTE");
					lCodiciSiesNscModel.setCoSies(lCircostanzaModel[j].getCodFonte());
					CodiciSiesNscModel aCodiciSIESNSCModel = DecodificaCodiceSies(lCodiciSiesNscModel);
					lCodCentralizzato = aCodiciSIESNSCModel.getCoCodcentr();
					ReatoCircostanza.setCODITL(lCodCentralizzato);
				}

				// Solo se COD_FONTE e uno dei codice identificativi della legge speciale scriviamo ANNO e
				// NUMERO della Legge Speciale.
				if (ReatoCircostanza.getCODITL() != null && (ReatoCircostanza.getCODITL().equals("6")
						|| ReatoCircostanza.getCODITL().equals("7")
						|| ReatoCircostanza.getCODITL().equals("8")
						|| ReatoCircostanza.getCODITL().equals("9")
						|| ReatoCircostanza.getCODITL().equals("10")
						|| ReatoCircostanza.getCODITL().equals("11")
						|| ReatoCircostanza.getCODITL().equals("12")
						|| ReatoCircostanza.getCODITL().equals("13")
						|| ReatoCircostanza.getCODITL().equals("14")
						|| ReatoCircostanza.getCODITL().equals("15")
						|| ReatoCircostanza.getCODITL().equals("16")
						|| ReatoCircostanza.getCODITL().equals("17")
						|| ReatoCircostanza.getCODITL().equals("18")
						|| ReatoCircostanza.getCODITL().equals("19")
						|| ReatoCircostanza.getCODITL().equals("20")
						|| ReatoCircostanza.getCODITL().equals("21")
						|| ReatoCircostanza.getCODITL().equals("22"))) {
					// ANNO LEGGE SPECIALE
					if (lCircostanzaModel[j].getAnnoFonte() != null) {
						ReatoCircostanza.setANNOLS(lCircostanzaModel[j].getAnnoFonte().intValue());
					}
					// NUMERO LEGGE SPECIALE
					if (lCircostanzaModel[j].getNumeroFonte() != null
							&& !lCircostanzaModel[j].getNumeroFonte().equals("")) {
						ReatoCircostanza.setNUMELS(lCircostanzaModel[j].getNumeroFonte());
					}
				}

				if (lCircostanzaModel[j].getArticolo() != null
						&& !lCircostanzaModel[j].getArticolo().equals("")) {
					ReatoCircostanza.setARTILS(lCircostanzaModel[j].getArticolo());
				}

				if (lCircostanzaModel[j].getComma() != null && !lCircostanzaModel[j].getComma().equals("")) {
					if (isNumeric(lCircostanzaModel[j].getComma()) == true
							&& lCircostanzaModel[j].getComma().trim().length() < 4) {
						ReatoCircostanza.setARTICOMMA(lCircostanzaModel[j].getComma());
					}
				}

				// [MEV REL. 5.0] - Gestione Comma qualificante
				if (lCircostanzaModel[j].getCommaQualificante() != null
						&& !lCircostanzaModel[j].getCommaQualificante().equals("-")) {
					lCodiciSiesNscModel = new CodiciSiesNscModel();
					lCodiciSiesNscModel.setCoDomain("SOTTONUMERAZIONE");
					lCodiciSiesNscModel.setCoSies(lCircostanzaModel[j].getCommaQualificante());
					CodiciSiesNscModel aCodiciSIESNSCModel = DecodificaCodiceSies(lCodiciSiesNscModel);
					lCodCentralizzato = aCodiciSIESNSCModel.getCoCodcentr();
					ReatoCircostanza.setARTICOMMABTQ(lCodCentralizzato);
				}

				if (lCircostanzaModel[j].getLettera() != null
						&& !lCircostanzaModel[j].getLettera().equals("")) {
					ReatoCircostanza.setARTILETTERA(lCircostanzaModel[j].getLettera());
				}

				// ----> DECODIFICA COD_SOTTONUMERAZIONE
				if (lCircostanzaModel[j].getCodSottonumerazione() != null
						&& !lCircostanzaModel[j].getCodSottonumerazione().equals("-")) {
					lCodiciSiesNscModel = new CodiciSiesNscModel();
					lCodiciSiesNscModel.setCoDomain("SOTTONUMERAZIONE");
					lCodiciSiesNscModel.setCoSies(lCircostanzaModel[j].getCodSottonumerazione());
					CodiciSiesNscModel aCodiciSIESNSCModel = DecodificaCodiceSies(lCodiciSiesNscModel);
					lCodCentralizzato = aCodiciSIESNSCModel.getCoCodcentr();
					ReatoCircostanza.setARTIBTQ(lCodCentralizzato);
				}

				if (lCircostanzaModel[j].getNumero() != null
						&& !lCircostanzaModel[j].getNumero().equals("")) {
					ReatoCircostanza.setARTINUMEARTICOLO(lCircostanzaModel[j].getNumero());
				}

			} // ----> Fine Ciclo Circostanze affogate nei Reati

		} // ----> Fine ciclo Reati

		/************************************************************/
		/* REATI IN CONTINUAZIONE */
		/************************************************************/
		ArrayReatiDocument.ArrayReati.ArrayContinuazione lArrayContinuazione = null;
		ArrayReatiDocument.ArrayReati.ArrayContinuazione.Continuazione lDatiContinuazione = null;

		Vector lReatiContinuazione = CercaReatiInContinuazione(lFascicolo.getIdFascicoloSiep().longValue());

		long lIDCont = 0, lIDReato = 0;
		// int lContaContinuazione=0,lFlagTrovato=0;
		int lFlagTrovato = 0;
		for (int i = 0; i <= lReatiContinuazione.size() - 1; i++) {
			ReatoModel lReatoModelCont = (ReatoModel) lReatiContinuazione.elementAt(i);
			if (i == 0) {
				lArrayContinuazione = ArrayReati.addNewArrayContinuazione();
			}

			lFlagTrovato = 0;
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.info("ID REATO Continuazione:" + lReatoModelCont.getIdReato());
			for (int j = 0; j <= lReatiCircostanze.size() - 1; j++) {
				ReatoModel lReatoCaricato = ((ReatoCircostanzaModel) lReatiCircostanze.get(j)).getReato();
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.info("ID REATO Caricato:" + lReatoCaricato.getIdReato());
				if (lReatoCaricato.getIdReato().longValue() == lReatoModelCont.getIdReato().longValue()) {
					lIDReato = lReatoCaricato.getIdReato().longValue();
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger.info("TROVATO:" + lIDReato);
					lFlagTrovato = 1;
				}
			}

			if (lFlagTrovato == 1) {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.info("ID Continuazione:" + lIDCont);
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.info("ID REATO:" + lIDReato);
				if (lIDCont != lReatoModelCont.getIdContinuazioneReato().longValue()) {
					lIDCont = lReatoModelCont.getIdContinuazioneReato().longValue();

					lDatiContinuazione = lArrayContinuazione.addNewContinuazione();

					// lDatiContinuazione.addNumero(lReatoModelCont.getIdReato().longValue());
					lDatiContinuazione.addNumero(lIDReato);
					if (lReatoModelCont.getTipoContinuazioneReato().equals("C1")) {
						lDatiContinuazione.setTipoContinuazione(
								ArrayReatiDocument.ArrayReati.ArrayContinuazione.Continuazione.TipoContinuazione.C_1);
					} else {
						lDatiContinuazione.setTipoContinuazione(
								ArrayReatiDocument.ArrayReati.ArrayContinuazione.Continuazione.TipoContinuazione.C_2);
					}
				} else {
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger.info("ID CONTINUAZIONE uguale:" + lIDCont);
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger.info("Caso con  lIDCont uguale:" + lIDReato);
					// lDatiContinuazione.addNumero(lReatoModelCont.getIdReato().longValue());
					lDatiContinuazione.addNumero(lIDReato);
				}
			}
		}

		/************************************************************/
		/* PENA COMPLESSIVA - DISPOSITIVO */
		/************************************************************/

		// Element DATI_CHIAMATA_TRASFERIMENTO - ANAGRAFICA - PROCEDIMENTO - TITOLO_ESECUTIVO -
		// DATI_PROVVEDIMENTO - ArrayPenaComplessiva

		ArrayPenaComplessivaDocument.ArrayPenaComplessiva ArrayPenaComplessiva = DatiProvvedimento
				.addNewArrayPenaComplessiva();
		DISPOSITIVOPENACOMPLESSIVADocument.DISPOSITIVOPENACOMPLESSIVA DispositivoPenaComplessiva = ArrayPenaComplessiva
				.addNewDISPOSITIVOPENACOMPLESSIVA();
		DISPOSITIVODocument.DISPOSITIVO Dispositivo = DispositivoPenaComplessiva.addNewDISPOSITIVO();

		// ---------------------------------------------------------------------------------------------
		// Gestione ArrayProgressivoReati da scrivere sotto DISPOSITIVO DELLA PENA_COMPLESSIVA
		// ---------------------------------------------------------------------------------------------
		ArrayProgressiviReatiDocument.ArrayProgressiviReati lArrayProgReatiDisp = null;
		for (int j = 0; j <= lArrayNumOrdineReato.size() - 1; j++) {
			if (j == 0) {
				lArrayProgReatiDisp = Dispositivo.addNewArrayProgressiviReati();
			}
			lArrayProgReatiDisp.addNumero(((BigDecimal) lArrayNumOrdineReato.get(j)).longValue());
		}

		/********************************************************************************/
		/* DISPOSITIVO PENA COMPLESSIVA */
		/********************************************************************************/

		Dispositivo.setPROGDISPOSITIVO(lPenaComp.getIdPenaComplessiva().longValue());

		DURATA lDurata = DURATA.Factory.newInstance();

		// Inizializziamo ANNI - MESI - GIORNI - (ORE ISOLAMENTO DIURNO)
		lDurata.setANNIDURATA(0);
		lDurata.setMESIDURATA(0);
		lDurata.setGIORNIDURATA(0);
		lDurata.setOREDURATA(0);

		if (lPenaComp.getNumAnniIsolamentoDiurno() != null) {
			lDurata.setANNIDURATA(lPenaComp.getNumAnniIsolamentoDiurno().intValue());
		}

		if (lPenaComp.getNumMesiIsolamentoDiurno() != null) {
			lDurata.setMESIDURATA(lPenaComp.getNumMesiIsolamentoDiurno().intValue());
		}
		if (lPenaComp.getNumGiorniIsolamentoDiurno() != null) {
			lDurata.setGIORNIDURATA(lPenaComp.getNumGiorniIsolamentoDiurno().intValue());
		}

		if (lDurata.getANNIDURATA() != 0 || lDurata.getMESIDURATA() != 0 || lDurata.getGIORNIDURATA() != 0) {
			Dispositivo.setISOLAMENTODIURNO(lDurata);
		}
		// Inizializziamo ANNI - MESI - GIORNI - ORE (RECLUSIONE)
		lDurata.setANNIDURATA(0);
		lDurata.setMESIDURATA(0);
		lDurata.setGIORNIDURATA(0);
		lDurata.setOREDURATA(0);
		if (lPenaComp.getNumAnniReclusione() != null) {
			lDurata.setANNIDURATA(lPenaComp.getNumAnniReclusione().intValue());
		}

		if (lPenaComp.getNumMesiReclusione() != null) {
			lDurata.setMESIDURATA(lPenaComp.getNumMesiReclusione().intValue());
		}

		if (lPenaComp.getNumGiorniReclusione() != null) {
			lDurata.setGIORNIDURATA(lPenaComp.getNumGiorniReclusione().intValue());
		}

		if (lDurata.getANNIDURATA() != 0 || lDurata.getMESIDURATA() != 0 || lDurata.getGIORNIDURATA() != 0) {
			Dispositivo.setRECLUSIONE(lDurata);
		}

		// Inizializziamo ANNI - MESI - GIORNI - ORE (ARRESTO)
		lDurata.setANNIDURATA(0);
		lDurata.setMESIDURATA(0);
		lDurata.setGIORNIDURATA(0);
		lDurata.setOREDURATA(0);
		if (lPenaComp.getNumAnniArresto() != null) {
			lDurata.setANNIDURATA(lPenaComp.getNumAnniArresto().intValue());
		}

		if (lPenaComp.getNumMesiArresto() != null) {
			lDurata.setMESIDURATA(lPenaComp.getNumMesiArresto().intValue());
		}

		if (lPenaComp.getNumGiorniArresto() != null) {
			lDurata.setGIORNIDURATA(lPenaComp.getNumGiorniArresto().intValue());
		}

		if (lDurata.getANNIDURATA() != 0 || lDurata.getMESIDURATA() != 0 || lDurata.getGIORNIDURATA() != 0) {
			Dispositivo.setARRESTO(lDurata);
		}

		if (lPenaComp.getImportoMulta() != null && lPenaComp.getImportoMulta().intValue() > 0) {
			Dispositivo.setIMPOMULTA(lPenaComp.getImportoMulta());
			Dispositivo.setCODIVALUTA("EUR");
		}

		if (lPenaComp.getImportoAmmenda() != null && lPenaComp.getImportoAmmenda().intValue() > 0) {
			Dispositivo.setIMPOAMMENDA(lPenaComp.getImportoAmmenda());
			Dispositivo.setCODIVALUTA("EUR");
		}

		// ----> DECODIFICA COD_PENA_DETENTIVA
		// MEV 16 CUMULO: getCodTipoPenaDetentivaDB() sostituisce getCodTipoPenaDetentiva() che va a prendere
		// la pena residua e setta solo il codice tipo ma non l'isolamento se FlagErgastolo = D
		// il trasferimento rompeva il certificato (isolamento senza anni, mesi o giorni)
		if (lPenaComp.getCodTipoPenaDetentivaDB() != null
				&& !lPenaComp.getCodTipoPenaDetentivaDB().equals("-")) {
			lCodiciSiesNscModel = new CodiciSiesNscModel();
			lCodiciSiesNscModel.setCoDomain("TIPO_PENA_DETENTIVA");
			lCodiciSiesNscModel.setCoSies(lPenaComp.getCodTipoPenaDetentivaDB());
			CodiciSiesNscModel aCodiciSIESNSCModel = DecodificaCodiceSies(lCodiciSiesNscModel);
			lCodCentralizzato = aCodiciSIESNSCModel.getCoCodcentr();
			Dispositivo.setCODTIPOPENADETENTIVA(lCodCentralizzato);
		}

		if (lSentenza.getCodTipoProvvedimento() == "05" && lSentenza.getNote() != null) // Sentenza Straniera
		{
			Dispositivo.setANNOTSENTSTRAN(lSentenza.getNote());
		}

		/************************************************************/
		/* DISPOSITIVO->CIRCOSTANZE */
		/************************************************************/
		CircostanzaModel lModelCircostanza;
		ArrayCircostanzeDocument.ArrayCircostanze ArrayCircostanze = null;
		int ContaCircostanza = 0;
		for (int j = 0; j <= lCircostanze.size() - 1; j++) {
			lModelCircostanza = (CircostanzaModel) lCircostanze.get(j);

			// Tratto le Attenuanti e Aggravanti solo se sono diverse da Codice Fonte 25
			// "Codice procedura penale" e Articolo 442 e 444
			// Flag Sentenza Applicazione Pena e Flag giudizio Abbreviato
			if (lModelCircostanza.getArticolo() != null
					&& (lModelCircostanza.getArticolo().equals("442")
							|| lModelCircostanza.getArticolo().equals("444"))
					&& lModelCircostanza.getCodFonte() != null
					&& lModelCircostanza.getCodFonte().equals("25")) {
				// Tratto le Attenuanti e Aggravanti solo se sono diverse da Codice Fonte 25
				// "Codice Procedura Penale" e Articolo 442 e 444 (Flag giudizio Abbreviato - Flag Sentenza
				// Applicazione Pena)
			} else {
				if (ContaCircostanza == 0) {
					ArrayCircostanze = Dispositivo.addNewArrayCircostanze();
					ContaCircostanza++;
				}

				CIRCOSTANZADocument.CIRCOSTANZA Circostanza = ArrayCircostanze.addNewCIRCOSTANZA();

				Circostanza.setPROGCIRCOSTANZE(lModelCircostanza.getIdCircostanza().longValue());

				// Paolo Cherubini 21/04/2011 controllo presenza fonte e articolo
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.info("######## controllo presenza fonte e articolo");
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.info("######## id ----------> " + lModelCircostanza.getIdCircostanza());
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.info("######## fonte -------> " + lModelCircostanza.getCodFonte());
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.info("######## art ---------> " + lModelCircostanza.getArticolo());

				if (lModelCircostanza.getCodFonte() == null || lModelCircostanza.getCodFonte().equals("-")
						|| lModelCircostanza.getCodFonte().equals("")
						|| lModelCircostanza.getArticolo() == null
						|| lModelCircostanza.getArticolo().equals("-")
						|| lModelCircostanza.getArticolo().equals("")) {
					setRequestAttribute(IWebConstants.MESSAGE_TEXT,
							"Attenzione! Prima di trasferire il procedimento completare l'inserimento delle "
									+ "Aggravanti/Attenuanti. Fonte e Articolo devono essere entrambi presenti.");
					RedirectTo lRedirigi = new RedirectTo();
					lRedirigi.setPage(IWebConstants.PG_MAIN);
					lRedirigi.setAction("siap.siep.fascicolo.action.ActLoadDettaglioFascicolo");
					setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi);
					return IWebConstants.PG_MESSAGE;
				} // fine paolo

				// ----> DECODIFICA CODICE_FONTE (CODI_TL Codice del testo Legislativo)
				if (lModelCircostanza.getCodFonte() != null && !lModelCircostanza.getCodFonte().equals("-")) {
					lCodiciSiesNscModel = new CodiciSiesNscModel();
					lCodiciSiesNscModel.setCoDomain("FONTE");
					lCodiciSiesNscModel.setCoSies(lModelCircostanza.getCodFonte());
					CodiciSiesNscModel aCodiciSIESNSCModel = DecodificaCodiceSies(lCodiciSiesNscModel);
					lCodCentralizzato = aCodiciSIESNSCModel.getCoCodcentr();
					Circostanza.setCODITL(lCodCentralizzato);
				}

				// ----> DECODIFICA COD_TIPO_CIRCOSTANZA
				if (lModelCircostanza.getCodBilanciamentoCircostanze() != null
						&& !lModelCircostanza.getCodBilanciamentoCircostanze().equals("-")) {
					lCodiciSiesNscModel = new CodiciSiesNscModel();
					lCodiciSiesNscModel.setCoDomain("BILANCIAMENTO_CIRCOSTANZE");
					lCodiciSiesNscModel.setCoSies(lModelCircostanza.getCodBilanciamentoCircostanze());
					CodiciSiesNscModel aCodiciSIESNSCModel = DecodificaCodiceSies(lCodiciSiesNscModel);
					lCodCentralizzato = aCodiciSIESNSCModel.getCoCodcentr();
					Circostanza.setFLAGCIRCGENERICHE(lCodCentralizzato);
				}

				if (lModelCircostanza.getAnnoFonte() != null) {
					Circostanza.setANNOLS(lModelCircostanza.getAnnoFonte().intValue());
				}

				if (lModelCircostanza.getNumeroFonte() != null
						&& !lModelCircostanza.getNumeroFonte().equals("")) {
					Circostanza.setNUMELS(lModelCircostanza.getNumeroFonte());
				}

				if (lModelCircostanza.getArticolo() != null && !lModelCircostanza.getArticolo().equals("")) {
					Circostanza.setARTILS(lModelCircostanza.getArticolo());
				}

				// ----> DECODIFICA COD_SOTTONUMERAZIONE
				if (lModelCircostanza.getCodSottonumerazione() != null
						&& !lModelCircostanza.getCodSottonumerazione().equals("-")) {
					lCodiciSiesNscModel = new CodiciSiesNscModel();
					lCodiciSiesNscModel.setCoDomain("SOTTONUMERAZIONE");
					lCodiciSiesNscModel.setCoSies(lModelCircostanza.getCodSottonumerazione());
					CodiciSiesNscModel aCodiciSIESNSCModel = DecodificaCodiceSies(lCodiciSiesNscModel);
					lCodCentralizzato = aCodiciSIESNSCModel.getCoCodcentr();
					Circostanza.setARTIBTQ(lCodCentralizzato);
				}

				if (lModelCircostanza.getComma() != null && !lModelCircostanza.getComma().equals("")) {
					if (isNumeric(lModelCircostanza.getComma()) == true
							&& lModelCircostanza.getComma().trim().length() < 4) {
						Circostanza.setARTICOMMA(lModelCircostanza.getComma());
					}
				}

				// [MEV REL. 5.0] - Gestione Comma qualificante
				if (lModelCircostanza.getCommaQualificante() != null
						&& !lModelCircostanza.getCommaQualificante().equals("-")) {
					lCodiciSiesNscModel = new CodiciSiesNscModel();
					lCodiciSiesNscModel.setCoDomain("SOTTONUMERAZIONE");
					lCodiciSiesNscModel.setCoSies(lModelCircostanza.getCommaQualificante());
					CodiciSiesNscModel aCodiciSIESNSCModel = DecodificaCodiceSies(lCodiciSiesNscModel);
					lCodCentralizzato = aCodiciSIESNSCModel.getCoCodcentr();
					Circostanza.setARTICOMMABTQ(lCodCentralizzato);
				}

				if (lModelCircostanza.getLettera() != null && !lModelCircostanza.getLettera().equals("")) {
					Circostanza.setARTILETTERA(lModelCircostanza.getLettera());
				}

				if (lModelCircostanza.getNumero() != null && !lModelCircostanza.getNumero().equals("")) {
					Circostanza.setARTINUMEARTICOLO(lModelCircostanza.getNumero());
				}
			}
		}

		/************************************************************/
		/* DISPOSITIVO->MISURA SICUREZZA */
		/************************************************************/
		ArrayMisureSicurezzaDocument.ArrayMisureSicurezza ArrayMisureSicurezza = null;
		MisuraSicurezzaModel lModelMisuraSicurezza;
		int lContaMis = 0;
		for (int j = 0; j <= lMisureSicurezza.size() - 1; j++) {
			lModelMisuraSicurezza = (MisuraSicurezzaModel) lMisureSicurezza.get(j);
			// Il codice "07" - Espulsione Ex Art. 86 D.P.R. 309/90 non va passato a NSC perchè non lo
			// gestisce.
			if (lModelMisuraSicurezza.getCodTipo() != null
					&& !lModelMisuraSicurezza.getCodTipo().equals("07")) {
				lContaMis++;

				if (lContaMis == 1) {
					ArrayMisureSicurezza = Dispositivo.addNewArrayMisureSicurezza();
				}
				MISURADISICUREZZADocument.MISURADISICUREZZA MisuraSicurezza = ArrayMisureSicurezza
						.addNewMISURADISICUREZZA();

				MisuraSicurezza.setPROGMISSICUREZZA(lModelMisuraSicurezza.getIdMisuraSicurezza().longValue());

				// ----> DECODIFICA COD_TIPO
				if (lModelMisuraSicurezza.getCodTipo() != null
						&& !lModelMisuraSicurezza.getCodTipo().equals("-")) {
					lCodiciSiesNscModel = new CodiciSiesNscModel();
					lCodiciSiesNscModel.setCoDomain("TIPO_MISURA_SICUREZZA");
					lCodiciSiesNscModel.setCoSies(lModelMisuraSicurezza.getCodTipo());
					CodiciSiesNscModel aCodiciSIESNSCModel = DecodificaCodiceSies(lCodiciSiesNscModel);
					lCodCentralizzato = aCodiciSIESNSCModel.getCoCodcentr();
					MisuraSicurezza.setCODITIPOMS(lCodCentralizzato);
				} else {
					MisuraSicurezza.setCODITIPOMS("");
				}

				// Inizializziamo ANNI - MESI - GIORNI - ORE
				lDurata.setANNIDURATA(0);
				lDurata.setMESIDURATA(0);
				lDurata.setGIORNIDURATA(0);
				lDurata.setOREDURATA(0);

				if (lModelMisuraSicurezza.getNumAnni() != null) {
					lDurata.setANNIDURATA(lModelMisuraSicurezza.getNumAnni().intValue());
				}

				if (lModelMisuraSicurezza.getNumMesi() != null) {
					lDurata.setMESIDURATA(lModelMisuraSicurezza.getNumMesi().intValue());
				}

				if (lModelMisuraSicurezza.getNumGiorni() != null) {
					lDurata.setGIORNIDURATA(lModelMisuraSicurezza.getNumGiorni().intValue());
				}

				if (lDurata.getANNIDURATA() != 0 || lDurata.getMESIDURATA() != 0
						|| lDurata.getGIORNIDURATA() != 0) {
					MisuraSicurezza.setDURATAMISURA(lDurata);
					MisuraSicurezza.setFLAGDURATAMISURA("T"); // Accordo con Eng 19/03/2009
				}
			}
		}

		/************************************************************/
		/* DISPOSITIVO->PENE AGGIUNTIVE */
		/************************************************************/
		ArrayPeneAggiunteDocument.ArrayPeneAggiunte ArrayPeneAggiunte = null;
		ContinuazioneModel lContinuazione;

		for (int j = 0; j <= lContinuazioneModel.size() - 1; j++) {

			if (j == 0) {
				ArrayPeneAggiunte = Dispositivo.addNewArrayPeneAggiunte();
			}
			PENAAGGIUNTADocument.PENAAGGIUNTA PenaAggiunta = ArrayPeneAggiunte.addNewPENAAGGIUNTA();
			lContinuazione = (ContinuazioneModel) lContinuazioneModel.get(j);

			PenaAggiunta.setPROGPENAAGGIUNTA(lContinuazione.getIdContinuazione().longValue());

			// ----> DECODIFICA CODI_TIPO_PENA_AGGIUNTIVA
			if (lContinuazione.getCodTipoContinuazione() != null
					&& !lContinuazione.getCodTipoContinuazione().equals("-")) {
				lCodiciSiesNscModel = new CodiciSiesNscModel();
				lCodiciSiesNscModel.setCoDomain("TIPO_CONTINUAZIONE");
				lCodiciSiesNscModel.setCoSies(lContinuazione.getCodTipoContinuazione());
				CodiciSiesNscModel aCodiciSIESNSCModel = DecodificaCodiceSies(lCodiciSiesNscModel);
				lCodCentralizzato = aCodiciSIESNSCModel.getCoCodcentr();
				PenaAggiunta.setCODITIPOPENAAGGIUNTA(lCodCentralizzato);
			} else {
				PenaAggiunta.setCODITIPOPENAAGGIUNTA("");
			}

			if (lContinuazione.getDataSentenza() != null) {
				lData.setGIORNO(DateUtils.getDateToString(lContinuazione.getDataSentenza(), "dd"));
				lData.setMESE(DateUtils.getDateToString(lContinuazione.getDataSentenza(), "MM"));
				lData.setANNO(DateUtils.getDateToString(lContinuazione.getDataSentenza(), "yyyy"));
				PenaAggiunta.setDATAPROVVEDIMENTOPA(lData);
			}

			// ----> DECODIFICA COD_TIPO_AUTORITA
			if (lContinuazione.getCodTipoAutorita() != null
					&& !lContinuazione.getCodTipoAutorita().equals("-")) {
				lCodiciSiesNscModel = new CodiciSiesNscModel();
				lCodiciSiesNscModel.setCoDomain("TIPO_UFFICIO_EMITTENTE");
				lCodiciSiesNscModel.setCoSies(lContinuazione.getCodTipoAutorita());
				lCodiciSiesNscModel.setCoVal1("-");
				lCodiciSiesNscModel.setCoVal2("-");
				CodiciSiesNscModel aCodiciSIESNSCModel = DecodificaCodiceSies(lCodiciSiesNscModel);
				lCodCentralizzato = aCodiciSIESNSCModel.getCoCodcentr();
				PenaAggiunta.setAUTORITAGIUDIZIARIAPA(lCodCentralizzato);
			}

			// ----> DECODIFICA COD_LUOGO_AUTORITA
			if (lContinuazione.getCodLuogoAutorita() != null
					&& !lContinuazione.getCodLuogoAutorita().equals("-")) {
				lCodiciSiesNscModel = new CodiciSiesNscModel();
				lCodiciSiesNscModel.setCoDomain("COMUNE");
				lCodiciSiesNscModel.setCoSies(lContinuazione.getCodLuogoAutorita());
				CodiciSiesNscModel aCodiciSIESNSCModel = DecodificaCodiceSies(lCodiciSiesNscModel);
				lCodCentralizzato = aCodiciSIESNSCModel.getCoCodcentr();
				PenaAggiunta.setCODISEDEAUTORITAPRINDIST(lCodCentralizzato);
			}

			if (lContinuazione.getAnnoSentenza() != null) {
				PenaAggiunta.setANNOSENTENZA(lContinuazione.getAnnoSentenza().toString());
			}

			if (lContinuazione.getNumSentenza() != null) {
				PenaAggiunta.setNUMEROSENTENZA(Integer.valueOf(lContinuazione.getNumSentenza()).intValue());
			}

		}

		/************************************************************/
		/* DISPOSITIVO->PENE ACCESSORIE */
		/************************************************************/
		ArrayPeneAccessorieDocument.ArrayPeneAccessorie ArrayPeneAccessorie = null;
		PenaAccessoriaModel lPenaAccessoriaModel;
		int lContaPeneAcc = 0;

		for (int j = 0; j <= lPeneAccessorie.size() - 1; j++) {
			lPenaAccessoriaModel = (PenaAccessoriaModel) lPeneAccessorie.get(j);

			/*
			 * Non veicolare a NSC le seguenti Pene Accessorie "134" - Liquidazione spese per Pubblicazione
			 * sentenza "999" - Altre Pene Accessorie
			 */
			if (lPenaAccessoriaModel.getCodTipoPenaAccessoria() != null
					&& !lPenaAccessoriaModel.getCodTipoPenaAccessoria().equals("134")
					&& !lPenaAccessoriaModel.getCodTipoPenaAccessoria().equals("999")) {
				lContaPeneAcc++;

				if (lContaPeneAcc == 1) {
					ArrayPeneAccessorie = Dispositivo.addNewArrayPeneAccessorie();
				}
				PENAACCESSORIADocument.PENAACCESSORIA PenaAccessoria = ArrayPeneAccessorie
						.addNewPENAACCESSORIA();

				PenaAccessoria.setPROGPENAACCESSORIA(lPenaAccessoriaModel.getIdPenaAccessoria().longValue());

				// Inizializziamo ANNI - MESI - GIORNI - ORE (DURATA PENA ACCESSORIA)
				lDurata.setANNIDURATA(0);
				lDurata.setMESIDURATA(0);
				lDurata.setGIORNIDURATA(0);
				lDurata.setOREDURATA(0);
				String lFlagDurata = "-", lFlagRisDurata = "-";

				if (lPenaAccessoriaModel.getNumAnni() != null) {
					lDurata.setANNIDURATA(lPenaAccessoriaModel.getNumAnni().intValue());
				}

				if (lPenaAccessoriaModel.getNumMesi() != null) {
					lDurata.setMESIDURATA(lPenaAccessoriaModel.getNumMesi().intValue());
				}

				if (lPenaAccessoriaModel.getNumGiorni() != null) {
					lDurata.setGIORNIDURATA(lPenaAccessoriaModel.getNumGiorni().intValue());
				}

				if (lDurata.getANNIDURATA() != 0 || lDurata.getMESIDURATA() != 0
						|| lDurata.getGIORNIDURATA() != 0) {
					PenaAccessoria.setDURATAPENAACCESSORIA(lDurata);
					lFlagDurata = "D"; // Se c'è la Durata è "Temporanea"
				} else {
					if (lPenaAccessoriaModel.getDurata().equals("-")
							|| lPenaAccessoriaModel.getDurata().equals("D")) {
						lFlagDurata = "D"; // "Durante la Pena" quindi "Temporanea"
					} else {
						lFlagDurata = "P";
					}
				}

				// ----> DECODIFICA COD_TIPO_PENA_ACCESSORIA e DURATA
				if (lPenaAccessoriaModel.getCodTipoPenaAccessoria() != null
						&& !lPenaAccessoriaModel.getCodTipoPenaAccessoria().equals("-")) {
					lCodiciSiesNscModel = new CodiciSiesNscModel();
					lCodiciSiesNscModel.setCoDomain("TIPO_PENA_ACCESSORIA");
					lCodiciSiesNscModel.setCoSies(lPenaAccessoriaModel.getCodTipoPenaAccessoria());

					if (lPenaAccessoriaModel.getCodTipoPenaAccessoria().equals("001")
							|| lPenaAccessoriaModel.getCodTipoPenaAccessoria().equals("038")
							|| lPenaAccessoriaModel.getCodTipoPenaAccessoria().equals("039")
							|| lPenaAccessoriaModel.getCodTipoPenaAccessoria().equals("046")
							|| lPenaAccessoriaModel.getCodTipoPenaAccessoria().equals("047")
							|| lPenaAccessoriaModel.getCodTipoPenaAccessoria().equals("086")) {
						// if (lPenaAccessoriaModel.getDurata().equals("-"))
						// {
						lCodiciSiesNscModel.setCoVal1(lFlagDurata);
						// }
						// else
						// {
						// lCodiciSiesNscModel.setCoVal1(lPenaAccessoriaModel.getDurata());
						// }
					} else {
						lCodiciSiesNscModel.setCoVal1("-");
					}

					CodiciSiesNscModel aCodiciSIESNSCModel = DecodificaCodiceSies(lCodiciSiesNscModel);
					lCodCentralizzato = aCodiciSIESNSCModel.getCoCodcentr();

					if (lDurata.getANNIDURATA() != 0 || lDurata.getMESIDURATA() != 0
							|| lDurata.getGIORNIDURATA() != 0) {
						lFlagRisDurata = "T";
					} else {
						if (lPenaAccessoriaModel.getDurata().equals("-")
								|| lPenaAccessoriaModel.getDurata().equals("D")) {
							lFlagRisDurata = "T";
						} else {
							lFlagRisDurata = "P";

						}
					}
					/*
					 * Se Durata sulla Tabella codici_sies_nsc restituisce blank o "-" allora verifichiamo se
					 * l'informazione è presente nel campo specifico del record della Tab. PENA_ACCESSORIA
					 * altrimenti lo detrminiamo in base alla presenza o meno della durata.
					 */
					/*---------------------------------------------------------------------
					 if (aCodiciSIESNSCModel.getCoVal2().equals("-") || aCodiciSIESNSCModel.getCoVal2().equals(""))
					 {
					     if (lPenaAccessoriaModel.getDurata().equals("-"))
					     {
					         lFlagRisDurata = lFlagDurata;
					     }
					     else
					     {
					         lFlagRisDurata = lPenaAccessoriaModel.getDurata();
					     }
					 }
					 else
					 {
					     lFlagRisDurata = aCodiciSIESNSCModel.getCoVal2();
					 }
					}
					----------------------------------------------------------------------------------*/
				} else {
					lCodCentralizzato = "";
					lFlagRisDurata = "";
				}

				PenaAccessoria.setCODITIPOPENAACCESSORIA(lCodCentralizzato);
				PenaAccessoria.setFLAGDURATAPENAACCESSORIA(lFlagRisDurata);

				if (lPenaAccessoriaModel.getDataFineValidita() != null) {
					lData.setGIORNO(
							DateUtils.getDateToString(lPenaAccessoriaModel.getDataFineValidita(), "dd"));
					lData.setMESE(
							DateUtils.getDateToString(lPenaAccessoriaModel.getDataFineValidita(), "MM"));
					lData.setANNO(
							DateUtils.getDateToString(lPenaAccessoriaModel.getDataFineValidita(), "yyyy"));
					PenaAccessoria.setDATASCADENZA(lData);
				}
			}
		}

		/************************************************************/
		/* DISPOSITIVO->SOSTITUZIONE PENA */
		/************************************************************/
		if (lSanzSost != null) {
			SOSTITUZIONEPENADocument.SOSTITUZIONEPENA SostituzionePena = Dispositivo.addNewSOSTITUZIONEPENA();

			if (lSanzSost.getIdSanzioneSostitutiva().toBigInteger() != null) {
				SostituzionePena.setPROGBENEFICIO(lSanzSost.getIdSanzioneSostitutiva().longValue());
			} else {
				SostituzionePena.setPROGBENEFICIO(0);
			}

			// ----> DECODIFICA COD_TIPO_SANZIONE
			if (lSanzSost.getCodTipoSanzione() != null && !lSanzSost.getCodTipoSanzione().equals("-")) {
				lCodiciSiesNscModel = new CodiciSiesNscModel();
				lCodiciSiesNscModel.setCoDomain("TIPO_SANZIONE");
				lCodiciSiesNscModel.setCoSies(lSanzSost.getCodTipoSanzione());
				CodiciSiesNscModel aCodiciSIESNSCModel = DecodificaCodiceSies(lCodiciSiesNscModel);
				lCodCentralizzato = aCodiciSIESNSCModel.getCoCodcentr();
				SostituzionePena.setCODITIPOBENEFICIO(lCodCentralizzato);
			} else {
				SostituzionePena.setCODITIPOBENEFICIO("");
			}

			// Inizializziamo ANNI - MESI - GIORNI - ORE
			lDurata.setANNIDURATA(0);
			lDurata.setMESIDURATA(0);
			lDurata.setGIORNIDURATA(0);
			lDurata.setOREDURATA(0);

			if (lSanzSost.getNumAnni() != null) {
				lDurata.setANNIDURATA(lSanzSost.getNumAnni().intValue());
			}

			if (lSanzSost.getNumMesi() != null) {
				lDurata.setMESIDURATA(lSanzSost.getNumMesi().intValue());
			}

			if (lSanzSost.getNumGiorni() != null) {
				lDurata.setGIORNIDURATA(lSanzSost.getNumGiorni().intValue());
			}

			if (lSanzSost.getCodTipoSanzione().equals("S")) // SEMIDETENZIONE
			{
				SostituzionePena.setDURATASEMIDET(lDurata);
			} else if (lSanzSost.getCodTipoSanzione().equals("L")) // LIBERTA CONTROLLATA
			{
				SostituzionePena.setDURATALIBERTACONTROLLATA(lDurata);
			} else if (lSanzSost.getCodTipoSanzione().equals("E")) // ESPULSIONE
			{
				SostituzionePena.setDURATAESPULSTATO(lDurata);
			}

			// Inizializziamo ANNI - MESI - GIORNI - ORE (RECLUSIONE)
			lDurata.setANNIDURATA(0);
			lDurata.setMESIDURATA(0);
			lDurata.setGIORNIDURATA(0);
			lDurata.setOREDURATA(0);

			// Durata Reclusione e Arresto li prendiamo da Pena Complessiva
			if (lPenaComp.getNumAnniReclusione() != null) {
				lDurata.setANNIDURATA(lPenaComp.getNumAnniReclusione().intValue());
			}

			if (lPenaComp.getNumMesiReclusione() != null) {
				lDurata.setMESIDURATA(lPenaComp.getNumMesiReclusione().intValue());
			}

			if (lPenaComp.getNumGiorniReclusione() != null) {
				lDurata.setGIORNIDURATA(lPenaComp.getNumGiorniReclusione().intValue());
			}

			if (lDurata.getANNIDURATA() != 0 || lDurata.getMESIDURATA() != 0
					|| lDurata.getGIORNIDURATA() != 0) {
				SostituzionePena.setDURATARECLUSIONE(lDurata);
			}

			// Inizializziamo ANNI - MESI - GIORNI - ORE (ARRESTO)
			lDurata.setANNIDURATA(0);
			lDurata.setMESIDURATA(0);
			lDurata.setGIORNIDURATA(0);
			lDurata.setOREDURATA(0);

			if (lPenaComp.getNumAnniArresto() != null) {
				lDurata.setANNIDURATA(lPenaComp.getNumAnniArresto().intValue());
			}

			if (lPenaComp.getNumMesiArresto() != null) {
				lDurata.setMESIDURATA(lPenaComp.getNumMesiArresto().intValue());
			}

			if (lPenaComp.getNumGiorniArresto() != null) {
				lDurata.setGIORNIDURATA(lPenaComp.getNumGiorniArresto().intValue());
			}

			if (lDurata.getANNIDURATA() != 0 || lDurata.getMESIDURATA() != 0
					|| lDurata.getGIORNIDURATA() != 0) {
				SostituzionePena.setDURATAARRESTO(lDurata);
			}

			BigDecimal lSanzionePecuniariaMulta, lSanzionePecuniariaAmmenda;
			if (lSanzSost.getSanzionePecuniariaMulta() != null) {
				lSanzionePecuniariaMulta = lSanzSost.getSanzionePecuniariaMulta();
			} else {
				lSanzionePecuniariaMulta = new BigDecimal(0);
			}

			if (lSanzSost.getSanzionePecuniariaAmmenda() != null) {
				lSanzionePecuniariaAmmenda = lSanzSost.getSanzionePecuniariaAmmenda();
			} else {
				lSanzionePecuniariaAmmenda = new BigDecimal(0);
			}

			// Importo Multa e Ammenda li prendo da Pena Complessiva
			// Nota: Nel caso di situazione mista (Presenza di Multa e Ammenda) verrà valorizzato soltanto il
			// campo IMPOMULTASOST (IMPOAMMENDASOST=0)
			// Accordo preso con ENG 15/12/2008 - Accordo superato con l'aggiunta del campo
			// SANZIONE_PECUNIARIA_AMMENDA (REl 5.0)
			// [MEV REL. 5.0] - Sanzione Sostitutiva distinzione MULTA/AMMENDA SOSTITUTIVA
			if ((lPenaComp.getImportoAmmenda() != null && lPenaComp.getImportoAmmenda().intValue() > 0)
					|| SostituzionePena.getDURATAARRESTO() != null) {
				if (lPenaComp.getImportoAmmenda().intValue() > 0) {
					SostituzionePena.setIMPOAMMENDA(lPenaComp.getImportoAmmenda());
					SostituzionePena.setCODIVALUTA("EUR");
				}
				if (lSanzionePecuniariaAmmenda.intValue() > 0) {
					SostituzionePena.setIMPOAMMENDASOST(lSanzionePecuniariaAmmenda);
					SostituzionePena.setCODIVALUTA("EUR");
				}
			}

			if ((lPenaComp.getImportoMulta() != null && lPenaComp.getImportoMulta().intValue() > 0)
					|| SostituzionePena.getDURATARECLUSIONE() != null) {
				if (lPenaComp.getImportoMulta().intValue() > 0) {
					SostituzionePena.setIMPOMULTA(lPenaComp.getImportoMulta());
					SostituzionePena.setCODIVALUTA("EUR");
				}
				if (lSanzionePecuniariaMulta.intValue() > 0) {
					// SostituzionePena.setIMPOAMMENDASOST(new BigDecimal(0));
					SostituzionePena.setIMPOMULTASOST(lSanzionePecuniariaMulta);
					SostituzionePena.setCODIVALUTA("EUR");
				}
			}

		}

		/************************************************************/
		/* DISPOSITIVO->BENEFICI */
		/************************************************************/

		ArrayBeneficiDocument.ArrayBenefici ArrayBenefici = null;
		ArrayRevocheDocument.ArrayRevoche ArrayRevoche = null;
		int lContaBenefici = 0;
		int lContaRevoche = 0;

		BeneficioModel lBeneficioModel;
		for (int j = 0; j <= lBenefici.size() - 1; j++) {
			lBeneficioModel = (BeneficioModel) lBenefici.get(j);

			if (lBeneficioModel.getCodNaturaBeneficio().equals("C")) { // Concesso
				// ----> DECODIFICA COD_TIPO_BENEFICIO
				if (lBeneficioModel.getCodTipoBeneficio() != null
						&& !lBeneficioModel.getCodTipoBeneficio().equals("-")) {
					lCodiciSiesNscModel = new CodiciSiesNscModel();
					lCodiciSiesNscModel.setCoDomain("TIPO_BENEFICIO");
					lCodiciSiesNscModel.setCoSies(lBeneficioModel.getCodTipoBeneficio());
					// Se Tipo beneficio è "Sospensione Condizionale" o "Indulto" impostare nella ricerca
					// anche il codice SottoTipo
					if (lBeneficioModel.getCodTipoBeneficio().equals("01")
							|| lBeneficioModel.getCodTipoBeneficio().equals("03")) {
						if (lBeneficioModel.getCodSottotipoBeneficio() != null
								&& !lBeneficioModel.getCodSottotipoBeneficio().equals("")
								&& !lBeneficioModel.getCodSottotipoBeneficio().equals("-")) {
							lCodiciSiesNscModel.setCoVal1(lBeneficioModel.getCodSottotipoBeneficio());
						}
					}
					CodiciSiesNscModel aCodiciSIESNSCModel = DecodificaCodiceSies(lCodiciSiesNscModel);
					lCodCentralizzato = aCodiciSIESNSCModel.getCoCodcentr();
				} else {
					lCodCentralizzato = "";
				}

				/*
				 * Se il Tipo Beneficio è una "Sospensione Condizionale" NON scriviamo il TAG Benefico ma
				 * mettiamo il Codice Tipo Benedicio nell'elemento CODI_PENA_SOSPESA del DISPOSITIVO della
				 * PENA_COMPLESSIVA. Inoltre se la "Sospensione Condizionale" e "Subordinata" scriviamo il
				 * Tipo Sospensione subordinata nell'elemento CODI_TIPO_SOSP_SUB sempre del DISPOSITIVO della
				 * PENA_COMPLESSIVA
				 */
				if (lBeneficioModel.getCodTipoBeneficio() != null
						&& lBeneficioModel.getCodTipoBeneficio().equals("01")) { // Sospensione Condizionale
					Dispositivo.setCODIPENASOSPESA(lCodCentralizzato);
					// Solo se c'è una Sospensione Condizionale Subordinata preleviamo il codice Sospensione
					// Subordinata
					if (lBeneficioModel.getCodSottotipoBeneficio() != null
							&& lBeneficioModel.getCodSottotipoBeneficio().equals("03")) { // Subordinata
						lCodiciSiesNscModel = new CodiciSiesNscModel();
						lCodiciSiesNscModel.setCoDomain("TIPO_SOSP_SUBORDINATA");
						lCodiciSiesNscModel.setCoSies(lBeneficioModel.getCodTipoSospSubordinata());
						CodiciSiesNscModel aCodiciSIESNSCModel = DecodificaCodiceSies(lCodiciSiesNscModel);
						lCodCentralizzato = aCodiciSIESNSCModel.getCoCodcentr();
						Dispositivo.setCODITIPOSOSPSUB(lCodCentralizzato);
					}
				} else if (lBeneficioModel.getCodTipoBeneficio().equals("02")) { // NON MENZIONE
					/*
					 * Se il Tipo Beneficio è "NON MENZIONE" NON scriviamo il TAG Benefico ma scriviamo il
					 * Codice Non Menzione nell'elemento CODICE_NON_MENZIONE del DISPOSITIVO della
					 * PENA_COMPLESSIVA
					 */
					Dispositivo.setCODICENONMENZIONE(lCodCentralizzato);
				} else // BENEFICI
				{
					lContaBenefici++;
					if (lContaBenefici == 1) {
						ArrayBenefici = Dispositivo.addNewArrayBenefici();
					}

					BENEFICIODocument.BENEFICIO lBeneficio = ArrayBenefici.addNewBENEFICIO();

					lBeneficio.setPROGBENEFICIO(lBeneficioModel.getIdBeneficio().longValue());

					lBeneficio.setCODITIPOBENEFICIO(lCodCentralizzato);

					// ----> DECODIFICA COD_DPR - MANCA CAMPO SU XSD
					if (lBeneficioModel.getCodDpr() != null && !lBeneficioModel.getCodDpr().equals("-")) {
						lCodiciSiesNscModel = new CodiciSiesNscModel();
						lCodiciSiesNscModel.setCoDomain("DPR");
						lCodiciSiesNscModel.setCoSies(lBeneficioModel.getCodDpr());
						CodiciSiesNscModel aCodiciSIESNSCModel = DecodificaCodiceSies(lCodiciSiesNscModel);
						lCodCentralizzato = aCodiciSIESNSCModel.getCoCodcentr();
						lBeneficio.setCODICEDPR(lCodCentralizzato);
					}

					// Inizializziamo ANNI - MESI - GIORNI - ORE (RECLUSIONE)
					lDurata.setANNIDURATA(0);
					lDurata.setMESIDURATA(0);
					lDurata.setGIORNIDURATA(0);
					lDurata.setOREDURATA(0);

					// ---> Durata Reclusione
					if (lBeneficioModel.getNumAnniReclusione() != null) {
						lDurata.setANNIDURATA(lBeneficioModel.getNumAnniReclusione().intValue());
					}

					if (lBeneficioModel.getNumMesiReclusione() != null) {
						lDurata.setMESIDURATA(lBeneficioModel.getNumMesiReclusione().intValue());
					}

					if (lBeneficioModel.getNumGiorniReclusione() != null) {
						lDurata.setGIORNIDURATA(lBeneficioModel.getNumGiorniReclusione().intValue());
					}

					if (lDurata.getANNIDURATA() != 0 || lDurata.getMESIDURATA() != 0
							|| lDurata.getGIORNIDURATA() != 0) {
						lBeneficio.setDURATARECLUSIONE(lDurata);
					}

					// Inizializziamo ANNI - MESI - GIORNI - ORE (ARRESTO)
					lDurata.setANNIDURATA(0);
					lDurata.setMESIDURATA(0);
					lDurata.setGIORNIDURATA(0);
					lDurata.setOREDURATA(0);
					// ----> Durata Arresto
					if (lBeneficioModel.getNumAnniArresto() != null) {
						lDurata.setANNIDURATA(lBeneficioModel.getNumAnniArresto().intValue());
					}

					if (lBeneficioModel.getNumMesiArresto() != null) {
						lDurata.setMESIDURATA(lBeneficioModel.getNumMesiArresto().intValue());
					}

					if (lBeneficioModel.getNumGiorniArresto() != null) {
						lDurata.setGIORNIDURATA(lBeneficioModel.getNumGiorniArresto().intValue());
					}

					if (lDurata.getANNIDURATA() != 0 || lDurata.getMESIDURATA() != 0
							|| lDurata.getGIORNIDURATA() != 0) {
						lBeneficio.setDURATAARRESTO(lDurata);
					}

					if (lBeneficioModel.getImportoMulta() != null
							&& lBeneficioModel.getImportoMulta().intValue() > 0) {
						lBeneficio.setIMPOMULTA(lBeneficioModel.getImportoMulta());
						lBeneficio.setCODIVALUTA("EUR");
					}

					if (lBeneficioModel.getImportoAmmenda() != null
							&& lBeneficioModel.getImportoAmmenda().intValue() > 0) {
						lBeneficio.setIMPOAMMENDA(lBeneficioModel.getImportoAmmenda());
						lBeneficio.setCODIVALUTA("EUR");
					}
				}
			} else // REVOCATO
			{

				/************************************************************/
				/* REVOCHE */
				/************************************************************/
				lContaRevoche++;
				if (lContaRevoche == 1) {
					ArrayRevoche = DatiProvvedimento.addNewArrayRevoche();
				}
				// Element DATI_CHIAMATA_TRASFERIMENTO - ANAGRAFICA - PROCEDIMENTO - TITOLO_ESECUTIVO -
				// DATI_PROVVEDIMENTO - ArrayRevoche
				REVOCADocument.REVOCA Revoca = ArrayRevoche.addNewREVOCA();
				Revoca.setPROGPC(lBeneficioModel.getIdBeneficio().longValue());

				// ----> DECODIFICA COD_TIPO_BENEFICIO
				if (lBeneficioModel.getCodTipoBeneficio() != null
						&& !lBeneficioModel.getCodTipoBeneficio().equals("-")) {
					lCodiciSiesNscModel = new CodiciSiesNscModel();
					lCodiciSiesNscModel.setCoDomain("TIPO_REVOCA");
					lCodiciSiesNscModel.setCoSies(lBeneficioModel.getCodTipoBeneficio());
					CodiciSiesNscModel aCodiciSIESNSCModel = DecodificaCodiceSies(lCodiciSiesNscModel);
					lCodCentralizzato = aCodiciSIESNSCModel.getCoCodcentr();
					Revoca.setCODITIPOPC(lCodCentralizzato);
				} else {
					Revoca.setCODITIPOPC("");
				}

				// ----> DECODIFICA COD_DPR - VERIFICARE CAMPO SU XSD CODITL o ARTIPC
				if (lBeneficioModel.getCodDpr() != null && !lBeneficioModel.getCodDpr().equals("-")) {
					lCodiciSiesNscModel = new CodiciSiesNscModel();
					lCodiciSiesNscModel.setCoDomain("DPR");
					lCodiciSiesNscModel.setCoSies(lBeneficioModel.getCodDpr());
					CodiciSiesNscModel aCodiciSIESNSCModel = DecodificaCodiceSies(lCodiciSiesNscModel);
					lCodCentralizzato = aCodiciSIESNSCModel.getCoCodcentr();
					Revoca.setCODITL(lCodCentralizzato);
				}

				/*---------------------------------------------------------------------------------------------------------------------*/
				/*---> I dati che seguono sono i dati della Sentenza che ha generato il BENEFICIO che l'attuale Sentenza sta REVOCANDO */
				/*---------------------------------------------------------------------------------------------------------------------*/

				// Data Provvedimento
				if (lBeneficioModel.getRifDataProvvedimento() != null) {
					lData.setGIORNO(
							DateUtils.getDateToString(lBeneficioModel.getRifDataProvvedimento(), "dd"));
					lData.setMESE(DateUtils.getDateToString(lBeneficioModel.getRifDataProvvedimento(), "MM"));
					lData.setANNO(
							DateUtils.getDateToString(lBeneficioModel.getRifDataProvvedimento(), "yyyy"));
					Revoca.setDATAPROVVEDIMENTOREVOCA(lData);
				}

				// ----> DECODIFICA COD_TIPO_AUTORITA_EMITTENTE
				if (lBeneficioModel.getRifCodTipoAutoEmittente() != null
						&& !lBeneficioModel.getRifCodTipoAutoEmittente().equals("-")) {
					lCodiciSiesNscModel = new CodiciSiesNscModel();
					lCodiciSiesNscModel.setCoDomain("TIPO_UFFICIO_EMITTENTE");
					lCodiciSiesNscModel.setCoSies(lBeneficioModel.getRifCodTipoAutoEmittente());
					lCodiciSiesNscModel.setCoVal1("-");
					lCodiciSiesNscModel.setCoVal2("-");
					CodiciSiesNscModel aCodiciSIESNSCModel = DecodificaCodiceSies(lCodiciSiesNscModel);
					lCodCentralizzato = aCodiciSIESNSCModel.getCoCodcentr();
					Revoca.setAUTORITAGIUDIZIARIA(lCodCentralizzato);
				}

				// ----> DECODIFICA COD_LUOGO_EMITTENTE
				if (lBeneficioModel.getRifCodLuogoAutoEmittente() != null
						&& !lBeneficioModel.getRifCodLuogoAutoEmittente().equals("-")) {
					lCodiciSiesNscModel = new CodiciSiesNscModel();
					lCodiciSiesNscModel.setCoDomain("COMUNE");
					lCodiciSiesNscModel.setCoSies(lBeneficioModel.getRifCodLuogoAutoEmittente());
					CodiciSiesNscModel aCodiciSIESNSCModel = DecodificaCodiceSies(lCodiciSiesNscModel);
					lCodCentralizzato = aCodiciSIESNSCModel.getCoCodcentr();
					Revoca.setCODISEDEAUTORITAPRINDIST(lCodCentralizzato);
				}

				if (lBeneficioModel.getRifAnnoProvvedimento() != null) {
					Revoca.setANNOSENTENZA(lBeneficioModel.getRifAnnoProvvedimento().intValue());
				}

				if (lBeneficioModel.getRifNumeroProvvedimento() != null) {
					Revoca.setNUMEROSENTENZA(Integer.parseInt(lBeneficioModel.getRifNumeroProvvedimento()));
				}

			}
		}

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
			siesLogger.info("Struttura XML VALIDA!!!" + lTrasfDoc.xmlText());
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
						"ActPrelevaDatiFascicolo - Struttura XML non valida. Contattare il servizio di Help Desk");
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

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.info("---------------------------------------------------------");
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.info("Indirizzo WS NSC:#" + lNscWsAddress + "#");
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.info("---------------------------------------------------------");

		// IscriviProvvedimentoProvvisorio service = new IscriviProvvedimentoProvvisorioLocator();
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

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.info("====================================================================");
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.info("Risposta WS NSC: " + rispostaFlussoXmlNsc);
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.info("====================================================================");

		ActRispostaDatiFascicolo objRispostaNsc = new ActRispostaDatiFascicolo();
		Vector lListaOmonimi = objRispostaNsc.processRequest(rispostaFlussoXmlNsc, lFascicolo.getChiaveAnno(),
				lFascicolo.getChiaveProgr(), lUteMod);

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.info("-------------------------------------------------");
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.info("lunghezza Vettore Omonimi: " + lListaOmonimi.size());
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.info("-------------------------------------------------");

		if (lListaOmonimi.size() == 0) {
			// Non ci sono OMONIMI - TRASMISSIONE TERMINATA

			// Caricamento del flusso xml di Risposta per prelevare il Codice dell'esito
			TRASFERIMENTODocument lDocNsc = TRASFERIMENTODocument.Factory.parse(rispostaFlussoXmlNsc);
			TRASFERIMENTODocument.TRASFERIMENTO rootRisp = lDocNsc.getTRASFERIMENTO();
			DATIRISPOSTATRASFERIMENTODocument.DATIRISPOSTATRASFERIMENTO datiRispostaTrasf = rootRisp
					.getDATIRISPOSTATRASFERIMENTO();
			ESITODocument.ESITO datiEsito = datiRispostaTrasf.getESITO();

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.info("====================================================================");
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.info("datiEsito.getCODICE(): " + datiEsito.getCODICE().toString());
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.info("====================================================================");

			if (datiEsito.getCODICE().toString().equals("0")) {
				setRequestAttribute(IWebConstants.MESSAGE_TEXT,
						"Il Procedimento N:" + lDettaglio.getFascicoloSiep().getChiaveAnno() + "/"
								+ lDettaglio.getFascicoloSiep().getChiaveProgr()
								+ " trasferito con successo.");
			} else if (datiEsito.getCODICE().toString().equals("2")) {
				setRequestAttribute(IWebConstants.MESSAGE_TEXT,
						"Il Procedimento N:" + lDettaglio.getFascicoloSiep().getChiaveAnno() + "/"
								+ lDettaglio.getFascicoloSiep().getChiaveProgr()
								+ " Non è stato trasferito perchè già presente in NSC.");
			} else {
				setRequestAttribute(IWebConstants.MESSAGE_TEXT,
						"Il Procedimento N:" + lDettaglio.getFascicoloSiep().getChiaveAnno() + "/"
								+ lDettaglio.getFascicoloSiep().getChiaveProgr()
								+ " Non è stato trasferito.");
			}

			RedirectTo lRedirigi = new RedirectTo();
			lRedirigi.setPage(IWebConstants.PG_MAIN);
			lRedirigi.setAction("siap.siep.fascicolo.action.ActLoadDettaglioFascicolo");
			setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi);
			return IWebConstants.PG_MESSAGE;
		} else {
			// PRESENZA DI OMONIMI - Passo il Vector alla JSP ListaOmonimi
			setRequestAttribute("lListaOmonimi", lListaOmonimi);
			return f3b.web.IWebConstants.ROOT_DIR + "files/siap/sico/webservice/ListaOmonimi.jsp";
		}
	}

	private void CaricaProvvedimento(DATIPROVVEDIMENTODocument.DATIPROVVEDIMENTO aDatiProvvedimento,
			SentenzaModel aSentenza, String aCodiPaeseCittad, String aFlagSentenzaApplicazPena,
			String aFlagGiudizioAbbreviato) throws Exception {
		String lCodTipoProvv = "", lCodAutoritaEmittente = "", lCodLuogoEmittente = "";

		aDatiProvvedimento.setPROGPROVV(aSentenza.getIdSentenza().longValue());

		if (!aCodiPaeseCittad.equals("-")) {
			aDatiProvvedimento.setFLAGCITTADINANZAISD(aCodiPaeseCittad); // Flag Cittadinanza // Nazionalita
		}
		// aDatiProvvedimento.setCODIPAESECITTAD(aCodiPaeseCittad); // Stesso Valore
		// Centralizzato del CODICE STATO NASCITA

		// -------------------------------------- GESTIONE TIPO PROVVEDIMENTO
		// ----------------------------------------------------
		lCodTipoProvv = aSentenza.getCodTipoProvvedimento();

		if (lCodTipoProvv.equals("05")) // SENTENZA STRANIERA
		{
			// Se Sentenza Straniera forziamo il codice a "01" per prelevare il Codice
			// Centralizzato delle Sentenza
			lCodTipoProvv = "01";
		}
		// ----> DECODIFICA COD_TIPO_PROVVEDIMENTO
		if (!lCodTipoProvv.equals("-")) {
			lCodiciSiesNscModel = new CodiciSiesNscModel();
			lCodiciSiesNscModel.setCoDomain("TIPO_PROVVEDIMENTO");
			lCodiciSiesNscModel.setCoSies(lCodTipoProvv);
			if (aFlagSentenzaApplicazPena != null && !aFlagSentenzaApplicazPena.equals("")) {
				lCodiciSiesNscModel.setCoVal1(aFlagSentenzaApplicazPena);
			} else {
				lCodiciSiesNscModel.setCoVal1("N");
			}

			CodiciSiesNscModel aCodiciSIESNSCModel = DecodificaCodiceSies(lCodiciSiesNscModel);
			lCodCentralizzato = aCodiciSIESNSCModel.getCoCodcentr();
			aDatiProvvedimento.setCODITIPOPROVV(lCodCentralizzato);
		} else {
			aDatiProvvedimento.setCODITIPOPROVV("");
		}
		// -------------------------------------- FINE GESTIONE TIPO PROVVEDIMENTO
		// ----------------------------------------------------

		DATA lData = DATA.Factory.newInstance();

		lData.setGIORNO(DateUtils.getDateToString(aSentenza.getDataProvvedimento(), "dd"));
		lData.setMESE(DateUtils.getDateToString(aSentenza.getDataProvvedimento(), "MM"));
		lData.setANNO(DateUtils.getDateToString(aSentenza.getDataProvvedimento(), "yyyy"));
		aDatiProvvedimento.setDATAPROVVEDIMENTO(lData);

		lCodAutoritaEmittente = aSentenza.getCodTipoAutoritaEmittente();

		// ----> DECODIFICA COD_TIPO_AUTORITA_EMITTENTE
		if (!lCodAutoritaEmittente.equals("-")) {
			lCodiciSiesNscModel = new CodiciSiesNscModel();
			lCodiciSiesNscModel.setCoDomain("TIPO_UFFICIO_EMITTENTE");
			lCodiciSiesNscModel.setCoSies(lCodAutoritaEmittente);
			if (lCodAutoritaEmittente.equals("DIB") || lCodAutoritaEmittente.equals("TRIBSD")) {
				lCodiciSiesNscModel.setCoVal1(aSentenza.getCodTipoRito());
			} else {
				lCodiciSiesNscModel.setCoVal1("-");
			}
			lCodiciSiesNscModel.setCoVal2("-");

			CodiciSiesNscModel aCodiciSIESNSCModel = DecodificaCodiceSies(lCodiciSiesNscModel);
			lCodCentralizzato = aCodiciSIESNSCModel.getCoCodcentr();
			aDatiProvvedimento.setCODIAUTORITA(lCodCentralizzato);
		} else {
			aDatiProvvedimento.setCODIAUTORITA("");
		}
		lCodLuogoEmittente = aSentenza.getCodLuogoEmittente();

		// ----> DECODIFICA COD_LUOGO_EMITTENTE
		if (!lCodLuogoEmittente.equals("-")) {
			lCodiciSiesNscModel = new CodiciSiesNscModel();
			lCodiciSiesNscModel.setCoDomain("COMUNE");
			lCodiciSiesNscModel.setCoSies(lCodLuogoEmittente);
			CodiciSiesNscModel aCodiciSIESNSCModel = DecodificaCodiceSies(lCodiciSiesNscModel);
			lCodCentralizzato = aCodiciSIESNSCModel.getCoCodcentr();
			aDatiProvvedimento.setCODISEDEAUTORITAPRINDIST(lCodCentralizzato);
		} else {
			aDatiProvvedimento.setCODISEDEAUTORITAPRINDIST("");
		}

		/*
		 * Non valorizzare perchè l'informazione su SIES non significativa per NSC if
		 * (aSentenza.getNumSezioneAutoritaEmittente() != null) { aDatiProvvedimento
		 * .setCODISEDEAUTORITAPRIN(aSentenza.getNumSezioneAutoritaEmittente()); }
		 */

		// Se NumeroSentenza contiene "AC" scartare il numero sentenza
		if (aSentenza.getNumeroSentenza() != null) {
			int flagtrue;
			flagtrue = aSentenza.getNumeroSentenza().indexOf("NC");
			if (flagtrue == -1) {
				aDatiProvvedimento.setNUMEROSENTENZA(aSentenza.getNumeroSentenza());
			}
		}

		if (aSentenza.getAnnoSentenza() != null) {
			aDatiProvvedimento.setANNOSENTENZA(aSentenza.getAnnoSentenza().intValue());
		}

		// NUMRO e ANNO REGISTRO GENERALE
		if (aSentenza.getNumeroRegeGip() != null) {
			aDatiProvvedimento.setNUMEROREGISTROGENERALE(aSentenza.getNumeroRegeGip());
			if (aSentenza.getAnnoRegeGip() != null) {
				aDatiProvvedimento.setANNOREGISTROGENERALE(aSentenza.getAnnoRegeGip().intValue());
			}
		} else if (aSentenza.getNumeroRegeDib() != null) {
			aDatiProvvedimento.setNUMEROREGISTROGENERALE(aSentenza.getNumeroRegeDib());
			if (aSentenza.getAnnoRegeDib() != null) {
				aDatiProvvedimento.setANNOREGISTROGENERALE(aSentenza.getAnnoRegeDib().intValue());
			}
		} else if (aSentenza.getNumeroRegeCas() != null) {
			aDatiProvvedimento.setNUMEROREGISTROGENERALE(aSentenza.getNumeroRegeCas());
			if (aSentenza.getAnnoRegeCas() != null) {
				aDatiProvvedimento.setANNOREGISTROGENERALE(aSentenza.getAnnoRegeCas().intValue());
			}
		} else if (aSentenza.getNumeroRegeCap() != null) {
			aDatiProvvedimento.setNUMEROREGISTROGENERALE(aSentenza.getNumeroRegeCap());
			if (aSentenza.getAnnoRegeCap() != null) {
				aDatiProvvedimento.setANNOREGISTROGENERALE(aSentenza.getAnnoRegeCap().intValue());
			}
		} else if (aSentenza.getNumeroRegeCasap() != null) {
			aDatiProvvedimento.setNUMEROREGISTROGENERALE(aSentenza.getNumeroRegeCasap());
			if (aSentenza.getAnnoRegeCasap() != null) {
				aDatiProvvedimento.setANNOREGISTROGENERALE(aSentenza.getAnnoRegeCasap().intValue());
			}
		}
		// MEV_66: aggiunte quattro nuove proprietà
		else if (aSentenza.getNumeroRegeGup() != null) {
			aDatiProvvedimento.setNUMEROREGISTROGENERALE(aSentenza.getNumeroRegeGup());
			if (aSentenza.getAnnoRegeGup() != null) {
				aDatiProvvedimento.setANNOREGISTROGENERALE(aSentenza.getAnnoRegeGup().intValue());
			}
		} else if (aSentenza.getNumeroRegeCapsm() != null) {
			aDatiProvvedimento.setNUMEROREGISTROGENERALE(aSentenza.getNumeroRegeCapsm());
			if (aSentenza.getAnnoRegeCapsm() != null) {
				aDatiProvvedimento.setANNOREGISTROGENERALE(aSentenza.getAnnoRegeCapsm().intValue());
			}
		}

		// [MEV REL. 5.0] - Gestione Eliminazione dalla Sentenza del FlagSentenzaApplicazPena -
		// FlagGiudizioAbbreviato
		if (aFlagSentenzaApplicazPena != null && aFlagSentenzaApplicazPena.equals("S")) {
			aDatiProvvedimento.setCODIFORMAPROC("06");
		} else if (aFlagGiudizioAbbreviato != null && aFlagGiudizioAbbreviato.equals("S")) {
			aDatiProvvedimento.setCODIFORMAPROC("01");
		} else {
			aDatiProvvedimento.setCODIFORMAPROC("05");
		}

		if (aSentenza.getNote() != null) {
			aDatiProvvedimento.setDESCNOTE(aSentenza.getNote());
		}
	}

	private void CaricaImpugnazione(IMPUGNAZIONEDocument.IMPUGNAZIONE aDatiImpugnazione,
			SentenzaModel aSentenza, String aUtente, String aTipoSentenza) throws Exception {

		String lCodTipoRife = "", lCodAutoritaEmittente = "", lCodLuogoEmittente = "";

		aDatiImpugnazione.setIDENTIFICATIVO(aSentenza.getIdSentenza().longValue());

		lCodTipoRife = aSentenza.getCodTipoProvvRif();
		if (!lCodTipoRife.equals("-")) {
			if (lCodTipoRife.equals("01") || lCodTipoRife.equals("03"))
				aDatiImpugnazione.setCODITIPORIFERIMENTO("5"); // Era 01 "Conferma"
			else
				aDatiImpugnazione.setCODITIPORIFERIMENTO("6"); // Era 02 "In Riforma"
		} else {
			// Ticket#20190725016 — Anomalia fascicoli SIEP: 20190725 [SG]
			// Se si mette una impugnazione nella sentenza di tipo ordinanza di inammissibilita'
			// aSentenza.getCodTipoProvvRif() risulta "-"
			// (mentre aSentenza.getCodTipoProvvedimentoRif() = "53"
			// nell'xml il CODITIPORIFERIMENTO e' obbligatorio e quindi imposto "0"
			// aDatiImpugnazione.setCODITIPORIFERIMENTO("0");
			// FINE Ticket#20190725016
			// Ticket#20210825016 — foglio completare Iscrizione nel casellario giudiziale locale - ex art. 3
			// DPR 14 novembre 2002 n. 313
			// come concordato con VB, EC ed SDA aggiungiamo nuovo c.u. sul db di NSC in corrispondenza del
			// codice 022 nella tabella dc_tab7a_riferimenti
			aDatiImpugnazione.setCODITIPORIFERIMENTO("17");
			// FINE Ticket#20210825016
		}

		DATA lData = DATA.Factory.newInstance();
		lData.setGIORNO(DateUtils.getDateToString(aSentenza.getDataProvvRif(), "dd"));
		lData.setMESE(DateUtils.getDateToString(aSentenza.getDataProvvRif(), "MM"));
		lData.setANNO(DateUtils.getDateToString(aSentenza.getDataProvvRif(), "yyyy"));
		aDatiImpugnazione.setDATAPROVVEDIMENTO(lData);

		lCodAutoritaEmittente = aSentenza.getCodTipoAutoritaProvvRif();

		// ----> DECODIFICA COD_TIPO_AUTORITA_EMITTENTE
		if (!lCodAutoritaEmittente.equals("-")) {
			lCodiciSiesNscModel = new CodiciSiesNscModel();
			lCodiciSiesNscModel.setCoDomain("TIPO_UFFICIO_EMITTENTE");
			lCodiciSiesNscModel.setCoSies(lCodAutoritaEmittente);
			if (lCodAutoritaEmittente.equals("DIB") || lCodAutoritaEmittente.equals("TRIBSD")) {
				lCodiciSiesNscModel.setCoVal1(aSentenza.getCodTipoRito());
			} else {
				lCodiciSiesNscModel.setCoVal1("-");
			}
			lCodiciSiesNscModel.setCoVal2("-");
			CodiciSiesNscModel aCodiciSIESNSCModel = DecodificaCodiceSies(lCodiciSiesNscModel);
			lCodCentralizzato = aCodiciSIESNSCModel.getCoCodcentr();
			aDatiImpugnazione.setCODIAUTORITA(lCodCentralizzato);
		} else {
			aDatiImpugnazione.setCODIAUTORITA("");
		}

		lCodLuogoEmittente = aSentenza.getCodLuogoProvvRif();

		// ----> DECODIFICA COD_LUOGO_EMITTENTE
		if (!lCodLuogoEmittente.equals("-")) {
			lCodiciSiesNscModel = new CodiciSiesNscModel();
			lCodiciSiesNscModel.setCoDomain("COMUNE");
			lCodiciSiesNscModel.setCoSies(lCodLuogoEmittente);
			CodiciSiesNscModel aCodiciSIESNSCModel = DecodificaCodiceSies(lCodiciSiesNscModel);
			lCodCentralizzato = aCodiciSIESNSCModel.getCoCodcentr();
			aDatiImpugnazione.setCODISEDEAUTORITAPRINDIST(lCodCentralizzato);
		} else {
			aDatiImpugnazione.setCODISEDEAUTORITAPRINDIST("");
		}

		/*
		 * Non significativo per NSC if (aSentenza.getNumSezioneAutoritaProvvRif() != null)
		 * {aDatiImpugnazione.setCODISEDEAUTORITAPRIN(aSentenza. getNumSezioneAutoritaProvvRif()); }
		 */

		// Se NumeroSentenza contiene "AC" scartare il numero sentenza
		if (aSentenza.getNumeroProvvRif() != null) {
			int flagtrue;
			flagtrue = aSentenza.getNumeroProvvRif().indexOf("NC");
			if (flagtrue == -1) {
				aDatiImpugnazione.setNUMEROSENTENZA(aSentenza.getNumeroProvvRif());
			}
		}

		if (aSentenza.getAnnoProvvRif() != null) {
			aDatiImpugnazione.setANNOSENTENZA(aSentenza.getAnnoProvvRif().intValue());
		}

		// NUMRO e ANNO REGISTRO GENERALE
		if (aSentenza.getNumeroRegeGip() != null) {
			aDatiImpugnazione.setNUMEROREGISTROGENERALE(aSentenza.getNumeroRegeGip());
			if (aSentenza.getAnnoRegeGip() != null) {
				aDatiImpugnazione.setANNOREGISTROGENERALE(aSentenza.getAnnoRegeGip().intValue());
			}
		} else if (aSentenza.getNumeroRegeDib() != null) {
			aDatiImpugnazione.setNUMEROREGISTROGENERALE(aSentenza.getNumeroRegeDib());
			if (aSentenza.getAnnoRegeDib() != null) {
				aDatiImpugnazione.setANNOREGISTROGENERALE(aSentenza.getAnnoRegeDib().intValue());
			}
		} else if (aSentenza.getNumeroRegeCas() != null) {
			aDatiImpugnazione.setNUMEROREGISTROGENERALE(aSentenza.getNumeroRegeCas());
			if (aSentenza.getAnnoRegeCas() != null) {
				aDatiImpugnazione.setANNOREGISTROGENERALE(aSentenza.getAnnoRegeCas().intValue());
			}
		} else if (aSentenza.getNumeroRegeCap() != null) {
			aDatiImpugnazione.setNUMEROREGISTROGENERALE(aSentenza.getNumeroRegeCap());
			if (aSentenza.getAnnoRegeCap() != null) {
				aDatiImpugnazione.setANNOREGISTROGENERALE(aSentenza.getAnnoRegeCap().intValue());
			}
		} else if (aSentenza.getNumeroRegeCasap() != null) {
			aDatiImpugnazione.setNUMEROREGISTROGENERALE(aSentenza.getNumeroRegeCasap());
			if (aSentenza.getAnnoRegeCasap() != null) {
				aDatiImpugnazione.setANNOREGISTROGENERALE(aSentenza.getAnnoRegeCasap().intValue());
			}
		}
		// MEV_66: aggiunte quattro nuove proprietà
		else if (aSentenza.getNumeroRegeGup() != null) {
			aDatiImpugnazione.setNUMEROREGISTROGENERALE(aSentenza.getNumeroRegeGup());
			if (aSentenza.getAnnoRegeGup() != null) {
				aDatiImpugnazione.setANNOREGISTROGENERALE(aSentenza.getAnnoRegeGup().intValue());
			}
		} else if (aSentenza.getNumeroRegeCapsm() != null) {
			aDatiImpugnazione.setNUMEROREGISTROGENERALE(aSentenza.getNumeroRegeCapsm());
			if (aSentenza.getAnnoRegeCapsm() != null) {
				aDatiImpugnazione.setANNOREGISTROGENERALE(aSentenza.getAnnoRegeCapsm().intValue());
			}
		}
	}

	private static boolean isNumeric(String s) {

		boolean numerico = true;
		char[] sequenza = s.toCharArray();

		for (int i = 0; i < sequenza.length; i++) {
			try {
				Integer.parseInt(Character.toString(sequenza[i]));
			} catch (Exception e) {
				numerico = false;
			}
		}

		return numerico;
	}

	private Vector CercaReatiInContinuazione(long aFascicoloSIEP) throws Exception {

		IWebServices lCtrlReati = SICOLookupRemote.getWebServicesRemote();
		Vector lReatiVector = new Vector();
		try {
			lReatiVector = lCtrlReati.ExRicercaReatiInContinuazione(aFascicoloSIEP);
		} catch (SIEPException e) {
			throw e;
		}
		return lReatiVector;
	}

	// public static void main(String[] args) throws Exception {
	//
	// // Security.setProperty("jdk.tls.disabledAlgorithms", "DH keySize < 768");
	// // System.setProperty("javax.net.ssl.keyStoreType", "pkcs12");
	// System.setProperty("javax.net.ssl.trustStore",
	// "D:/DATA_RECOVERY/Progetti/SIES-NI/CONFIG/certs/sies.jks");
	// System.setProperty("javax.net.ssl.trustStorePassword", "testsies");
	// System.setProperty("javax.net.debug", "ssl");
	// // System.setProperty("https.protocols", "TLSv1");
	// //System.setProperty("https.protocols", "SSLv3");
	// // System.setProperty("https.cipherSuites", "SSL_RSA_WITH_RC4_128_SHA");
	//
	// // SSLContext ctx = SSLContext.getInstance("TLS");
	//
	// // URL url = new URL("https://10.5.207.119:10002/siesWS/iscriviProvvedimentoProvvisorio");
	// //SSLSocket sock = (SSLSocket) SSLSocketFactory.getDefault().createSocket(url.getHost(),
	// url.getDefaultPort());
	// // SSLSocket sock = (SSLSocket) SSLSocketFactory.getDefault().createSocket();
	// // String ena[] = sock.getEnabledCipherSuites();
	// // List<String> t = Arrays.asList(ena);
	// // List<String> l = new ArrayList<String>();
	// // l.addAll(t);
	// // l.add("SSL_RSA_WITH_RC4_128_SHA");
	// // l.add("SSL_RSA_WITH_RC4_128_MD5");
	// // ena = new String[l.size()];
	// // sock.setEnabledCipherSuites(l.toArray(ena));
	//
	// // Create all-trusting host name verifier
	// // HostnameVerifier allHostsValid = new HostnameVerifier() {
	// // public boolean verify(String hostname, SSLSession session) {
	// // return true;
	// // }
	// // };
	// // Install the all-trusting host verifier
	// // HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
	// // HttpsURLConnection conn = (HttpsURLConnection)url.openConnection();
	// // conn.setDoOutput(false);
	//
	// // Hashtable< ?, ?> hashtable = service.getEngine().getConfig().getGlobalOptions();
	// // Set<?> entrySet = hashtable.entrySet();
	// // Iterator it = entrySet.iterator();
	// //
	// //
	// // while(it.hasNext()){
	// // }
	// //
	// //
	// IscriviProvvedimentoProvvisorio_ServiceLocator service = new
	// IscriviProvvedimentoProvvisorio_ServiceLocator();
	// service.setiscriviProvvedimentoProvvisorioEndpointAddress("https://10.5.207.119:9000/siesWS/iscriviProvvedimentoProvvisorio");
	// IscriviProvvedimentoProvvisorio_PortType port = service.getiscriviProvvedimentoProvvisorio();
	// String rispostaFlussoXmlNsc = null;
	// try{
	// rispostaFlussoXmlNsc = port.iscriviProvvedimentoProvvisorio("");
	// }catch(Exception e){
	// throw e;
	// }
	// //
	// try {
	// SSLContext sc = getSSLContext();
	//
	// SSLSocketFactory sslsocketfactory = (SSLSocketFactory) sc.getSocketFactory();
	//
	// SSLSocket sslsocket = (SSLSocket) sslsocketfactory.createSocket(
	// "mev.casellario.giustizia.it", 443);
	//
	// final String[] enabledCipherSuites = { "SSL_RSA_WITH_RC4_128_SHA" };
	// sslsocket.setEnabledCipherSuites(enabledCipherSuites);
	//
	//
	// InputStream inputstream = System.in;
	// InputStreamReader inputstreamreader = new InputStreamReader(
	// inputstream);
	// BufferedReader bufferedreader = new BufferedReader(
	// inputstreamreader);
	//
	// OutputStream outputstream = sslsocket.getOutputStream();
	// OutputStreamWriter outputstreamwriter = new OutputStreamWriter(
	// outputstream);
	// BufferedWriter bufferedwriter = new BufferedWriter(
	// outputstreamwriter);
	//
	// String string = null;
	// while ((string = bufferedreader.readLine()) != null) {
	// bufferedwriter.write(string + '\n');
	// bufferedwriter.flush();
	// }
	// } catch (Exception exception) {
	// exception.printStackTrace();
	// }
	// }
	//
	// private static SSLContext getSSLContext() throws NoSuchAlgorithmException, KeyManagementException {
	// SSLContext ret = SSLContext.getInstance("TLSv1.2");
	// TrustManager[] tm = getTrustManagerArray(null, null);
	// ret.init(null, tm, null);
	// return ret;
	// }
	//
	// private static TrustManager[] getTrustManagerArray(String truststore,
	// String pwd) {
	// TrustManager[] ret = null;
	// String trustFile = "D:/DATA_RECOVERY/Progetti/SIES-NI/CONFIG/certs/sies.jks";
	// if (null != truststore) {
	// trustFile = truststore;
	// } else {
	// File t = new File(trustFile);
	// if (!t.exists()) {
	// trustFile = "../" + trustFile;
	// t = new File(trustFile);
	// if (!t.exists()) {
	// throw new RuntimeException("Could not find trust file");
	// }
	// }
	// pwd = "testsies";
	// }
	//
	// try {
	// TrustManagerFactory tmf = TrustManagerFactory
	// .getInstance("SunX509");
	// KeyStore ts = KeyStore.getInstance("JKS");
	// // initialize truststore object using truststore name
	// ts.load(new FileInputStream(trustFile), pwd.toCharArray());
	// tmf.init(ts);
	// ret = tmf.getTrustManagers();
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// return ret;
	// }

}