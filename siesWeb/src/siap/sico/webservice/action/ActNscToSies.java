package siap.sico.webservice.action;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.Utils;
import it.mig.sies.type.ANAGRAFICADocument;
import it.mig.sies.type.CHIAVIDocument;
import it.mig.sies.type.DATIANAGRAFICIDocument.DATIANAGRAFICI;
import it.mig.sies.type.DATICHIAMATATRASFERIMENTODocument;
import it.mig.sies.type.DATIUTENTEDocument;
import it.mig.sies.type.TRASFERIMENTODocument;
import siap.sico.codici_sies_nsc.model.CodiciSiesNscModel;
import siap.sico.soggetto.controller.ISoggetto;
import siap.sico.soggetto.model.SoggettoModel;
import siap.sico.soggettocertificato.model.SoggettoCertificatoModel;
import siap.sico.util.SICOLookupRemote;
//import siap.sico.web.ActionSiap;
import siap.sico.webservice.controller.IWebServices;
import siap.sico.webservice.controller.WebServicesController;
import siap.sico.webservice.model.DatiNscToSiesModel;
import siap.siep.SIEPException;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.sentenza.controller.ISentenza;
import siap.siep.sentenza.model.SentenzaModel;
import siap.siep.statoprocedimento.model.StatoProcedimentoModel;
import siap.siep.util.SIEPLookupRemote;

@SuppressWarnings("rawtypes")
public class ActNscToSies extends ActWsBase {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	DatiNscToSiesModel mDatiNscToSiesModel;
	SentenzaModel lSentenzaModel;
	SoggettoModel lSoggettoModel;
	SoggettoCertificatoModel lSoggettoCertificatoModel;
	FascicoloSiepModel lFascicoloModel;
	Vector lReatoModel;
	Vector lPenaComplessivaModel;
	Vector lCircostanzaModel;
	Vector lMisuraSicurezzaModel;
	Vector lPeneAccessorieModel;
	Vector lSanzioneSostitutivaModel;
	Vector lBeneficioModel;
	Vector lRevocheModel;
	Vector lContinuazioneModel;
	StatoProcedimentoModel lStatoProcedimentoModel;
	WebServicesController lWebServicesController;
	String lCodiceEsito = "";
	int lCodErrXml = 0;
	String lCodOperatoreIns = "", lCodUfficioIns = "";
	// MEV 16: aggiunte variabili di classe
	boolean isForCumulo = false;
	String idSoggettoSiep = "", idFascicoloSiep = "", progrFascSiep = "", annoFascSiep = "";
	// MEV 16 CUMULO: aggiunta variabile di classe
	String idIstruttoriaCumulo = "";

	public String processRequest(String flussoXml) throws Exception {
		try {

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.info("Sto nel Servizio:" + flussoXml);

			// CARICAMENTO del flusso XML inviato da NSC
			TRASFERIMENTODocument lDocNsc = TRASFERIMENTODocument.Factory.parse(flussoXml);

			/**************************************************************************/
			/* Validazione XML proveniente da NSC */
			/**************************************************************************/
			/*
			 * Commentata il 07/04/2008 in quanto concordato con Eng che valida chi invia
			 *
			 * ArrayList validationErrors = new ArrayList(); XmlOptions m_validationOptions = new
			 * XmlOptions(); m_validationOptions.setErrorListener(validationErrors); boolean isValid =
			 * lDocNsc.validate(m_validationOptions);
			 *
			 * // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			 * LogF3B.getLogger() if (isValid) { siesLogger.info("Struttura XML proveniente da  NSC VALIDA!!!"
			 * ); } else { // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al
			 * posto di LogF3B.getLogger() siesLogger.info( "Struttura XML proveniente da NSC NON VALIDA!!!");
			 *
			 * // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			 * LogF3B.getLogger() Iterator iter = validationErrors.iterator(); while (iter.hasNext()) {
			 * siesLogger.error( "====================================================================" ); //
			 * [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			 * LogF3B.getLogger() "====================================================================" );
			 * siesLogger.error( "// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger
			 * al posto di LogF3B.getLogger() "Errori rilevati durante la convalida del Flusso XML NSC :");
			 * siesLogger.error(">> ERRORE " + // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di
			 * istanza siesLogger al posto di LogF3B.getLogger() iter.next() + "\n"); siesLogger.error(
			 * "====================================================================" );
			 *
			 * lCodiceEsito="200"; lCodErrXml=1; // Se si vuole generare un Exception la riga sotto altrimenti
			 * gestire l'errore throw new Exception(
			 * "ActNscToSies - Struttura XML proveniente da NSC NON valida. Contattare il servizio di Help Desk"
			 * );
			 *
			 * } } // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			 * LogF3B.getLogger() } } siesLogger.info("fine validazione");
			 */
			/**************************************************************************/
			/* Fine Validazione XML proveniente da NSC */
			/**************************************************************************/

			TRASFERIMENTODocument.TRASFERIMENTO rootTrasf = lDocNsc.getTRASFERIMENTO();
			DATICHIAMATATRASFERIMENTODocument.DATICHIAMATATRASFERIMENTO datiChiamataTrasf = rootTrasf
					.getDATICHIAMATATRASFERIMENTO();
			DATIUTENTEDocument.DATIUTENTE datiUtente = datiChiamataTrasf.getDATIUTENTE();
			CHIAVIDocument.CHIAVI datiChiavi = datiChiamataTrasf.getCHIAVI();
			ANAGRAFICADocument.ANAGRAFICA datiAnagrafica = datiChiamataTrasf.getANAGRAFICA();

			/**************************************************************************/
			/********** LOAD CODICE UFFICIO *********/
			/**************************************************************************/
			// Determiniamo il codice SIES Istat del COMUNE
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.info("Determiniamo il codice SIES Istat del COMUNE");

			String lCodIstatSiesSedeUff = "";
			if (datiUtente.getDATIUFFICIO().getCODICESEDEUFFICIO() != null) {
				CodiciSiesNscModel lCodiciSIESNSCModel = Decodifica("COMUNE",
						datiUtente.getDATIUFFICIO().getCODICESEDEUFFICIO());
				lCodIstatSiesSedeUff = lCodiciSIESNSCModel.getCoSies().trim();
			}
			// Determiniamo il codice SIES del TIPO_UFFICIO
			String lCodTipoUfficio = "";
			if (datiUtente.getDATIUFFICIO().getCODICETIPOUFFICIO() != null) {
				CodiciSiesNscModel lCodiciSIESNSCModel = Decodifica("TIPO_UFFICIO",
						datiUtente.getDATIUFFICIO().getCODICETIPOUFFICIO());
				lCodTipoUfficio = lCodiciSIESNSCModel.getCoSies().trim();
			}
			String lCodUfficio = CercaCodiceUfficio(lCodTipoUfficio, lCodIstatSiesSedeUff);

			lCodOperatoreIns = datiUtente.getUSERNAME().toString();
			lCodUfficioIns = lCodUfficio;

			/**************************************************************************/
			/********** LOAD TITOLO_ESECUTIVO.SENTENZA *********/
			/**************************************************************************/

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.info("LOAD  TITOLO_ESECUTIVO.SENTENZA ");

			ActNscToSiesLoadSentenza objLoadSentenza = new ActNscToSiesLoadSentenza(lCodUfficio);
			lSentenzaModel = objLoadSentenza.processRequest(datiAnagrafica, datiUtente);

			/**************************************************************************/
			/********** LOAD SOGGETTO *********/
			/**************************************************************************/

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.info("LOAD  SOGGETTO ");

			ActNscToSiesLoadSoggetto objLoadSoggetto = new ActNscToSiesLoadSoggetto(lCodUfficio);
			lSoggettoModel = objLoadSoggetto.processRequest(datiAnagrafica, datiUtente, datiChiavi);

			/**************************************************************************/
			/********** LOAD SOGGETTO CERTIFICATO *********/
			/**************************************************************************/

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.info("LOAD  CERTIFICATO ");

			ActNscToSiesLoadSoggettoCertificato objLoadSoggCert = new ActNscToSiesLoadSoggettoCertificato(
					lCodUfficio);
			lSoggettoCertificatoModel = objLoadSoggCert.processRequest(datiAnagrafica, datiUtente,
					datiChiavi);

			/**************************************************************************/
			/********** LOAD FASCICOLO *********/
			/**************************************************************************/

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.info("LOAD  FASCICOLO ");

			ActNscToSiesLoadFascicolo objLoadFascicolo = new ActNscToSiesLoadFascicolo(lCodUfficio);
			lFascicoloModel = objLoadFascicolo.processRequest(datiAnagrafica, datiUtente, datiChiavi);

			/**************************************************************************/
			/********** LOAD TITOLO_ESECUTIVO REATO - DISPOSITIVO del REATO *******/
			/**************************************************************************/

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.info("LOAD  REATO ");

			ActNscToSiesLoadReato objLoadReato = new ActNscToSiesLoadReato(lCodUfficio);
			lReatoModel = objLoadReato.processRequest(datiAnagrafica, datiUtente);

			/**************************************************************************/
			/********** LOAD TITOLO_ESECUTIVO PENA COMPLESSIVA *********/
			/**************************************************************************/

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.info("LOAD   TITOLO_ESECUTIVO PENA COMPLESSIVA ");

			ActNscToSiesLoadPenaComplessiva objLoadPenaCompl = new ActNscToSiesLoadPenaComplessiva(
					lCodUfficio);
			lPenaComplessivaModel = objLoadPenaCompl.processRequest(datiAnagrafica, datiUtente);

			/**************************************************************************/
			/********** LOAD TITOLO_ESECUTIVO Circostanze *********/
			/**************************************************************************/

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.info("LOAD   TITOLO_ESECUTIVO Circostanze ");

			ActNscToSiesLoadCircostanze objLoadCircostanze = new ActNscToSiesLoadCircostanze(lCodUfficio);
			lCircostanzaModel = objLoadCircostanze.processRequest(datiAnagrafica, datiUtente, lSentenzaModel);

			/**************************************************************************/
			/********** LOAD TITOLO_ESECUTIVO Misura Sicurezza *********/
			/**************************************************************************/

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.info("LOAD   TITOLO_ESECUTIVO Misura SicurezzaA ");

			ActNscToSiesLoadMisuraSicurezza objLoadMisuraSic = new ActNscToSiesLoadMisuraSicurezza(
					lCodUfficio);
			lMisuraSicurezzaModel = objLoadMisuraSic.processRequest(datiAnagrafica, datiUtente);

			/**************************************************************************/
			/********** LOAD TITOLO_ESECUTIVO Pena Accessoria *********/
			/**************************************************************************/

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.info("LOAD   TITOLO_ESECUTIVO Pena Accessoria ");

			ActNscToSiesLoadPeneAccessorie objLoadPeneAccess = new ActNscToSiesLoadPeneAccessorie(
					lCodUfficio);
			lPeneAccessorieModel = objLoadPeneAccess.processRequest(datiAnagrafica, datiUtente);

			/**************************************************************************/
			/********** LOAD TITOLO_ESECUTIVO Sanzione Sostitutiva *********/
			/**************************************************************************/

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.info("LOAD   TITOLO_ESECUTIVO Sanzione Sostitutiva ");

			ActNscToSiesLoadSostituzionePene objLoadSanzioneSost = new ActNscToSiesLoadSostituzionePene(
					lCodUfficio);
			lSanzioneSostitutivaModel = objLoadSanzioneSost.processRequest(datiAnagrafica, datiUtente);

			/**************************************************************************/
			/********** LOAD TITOLO_ESECUTIVO BENEFICI *********/
			/**************************************************************************/

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.info("LOAD   TITOLO_ESECUTIVO BENEFICI");

			ActNscToSiesLoadBenefici objLoadBenefici = new ActNscToSiesLoadBenefici(lCodUfficio);
			lBeneficioModel = objLoadBenefici.processRequest(datiAnagrafica, datiUtente);

			/**************************************************************************/
			/********** LOAD TITOLO_ESECUTIVO REVOCHE *********/
			/**************************************************************************/

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.info("LOAD   TITOLO_ESECUTIVO REVOCHE");

			ActNscToSiesLoadRevoche objLoadRevoche = new ActNscToSiesLoadRevoche(lCodUfficio);
			lRevocheModel = objLoadRevoche.processRequest(datiAnagrafica, datiUtente);

			/**************************************************************************/
			/********** LOAD TITOLO_ESECUTIVO PENE AGGIUNTIVE *********/
			/**************************************************************************/

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.info("LOAD   PENE AGGIUNTIVE");

			ActNscToSiesLoadContinuazione objLoadContinuazione = new ActNscToSiesLoadContinuazione(
					lCodUfficio);
			lContinuazioneModel = objLoadContinuazione.processRequest(datiAnagrafica, datiUtente);

			/**************************************************************************/
			/********** LOAD StatoProcedimentoValidato *********/
			/**************************************************************************/

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.info("LOAD   StatoProcedimentoValidato");

			ActNscToSiesLoadStatoProcedimento objLoadStatoProc = new ActNscToSiesLoadStatoProcedimento(
					lCodUfficio);
			lStatoProcedimentoModel = objLoadStatoProc.processRequest(datiAnagrafica, datiUtente);

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.info("LOAD   StatoProcedimentoValidato2");

			// Carichiamo i dati del flusso Xml nelle Model.
			mDatiNscToSiesModel = new DatiNscToSiesModel();

			mDatiNscToSiesModel.setSentenzaModel(lSentenzaModel);
			mDatiNscToSiesModel.setSoggettoModel(lSoggettoModel);
			mDatiNscToSiesModel.setFascicoloSiepModel(lFascicoloModel);
			mDatiNscToSiesModel.setReatoModel(lReatoModel);
			mDatiNscToSiesModel.setPenaComplessivaModel(lPenaComplessivaModel);
			mDatiNscToSiesModel.setCircostanzeModel(lCircostanzaModel);
			mDatiNscToSiesModel.setMisuraSicurezzaModel(lMisuraSicurezzaModel);
			mDatiNscToSiesModel.setPeneAccessorieModel(lPeneAccessorieModel);
			mDatiNscToSiesModel.setSanzioneSostitutivaModel(lSanzioneSostitutivaModel);
			mDatiNscToSiesModel.setBeneficioModel(lBeneficioModel);
			mDatiNscToSiesModel.setRevocaModel(lRevocheModel);
			mDatiNscToSiesModel.setContinuazioniModel(lContinuazioneModel);
			mDatiNscToSiesModel.setStatoProcedimentoModel(lStatoProcedimentoModel);
			mDatiNscToSiesModel.setSoggettoCertificatoModel(lSoggettoCertificatoModel);

			// MEV 16: aggiunti set di proprietà se presenti
			if (datiChiavi.getKPSIES() != 0) {
				idFascicoloSiep = "" + datiChiavi.getKPSIES();
				isForCumulo = true;
				// MEV 16 CUMULO: gestione dei titoli da associare al cumulo
				if (Utils.isPresent(datiChiamataTrasf.getPROGANAGRAFICANSC()))
					idIstruttoriaCumulo = "" + datiChiamataTrasf.getPROGANAGRAFICANSC();
			}

			if (Utils.isPresent(datiChiamataTrasf.getDATIFASCICOLO())) {
				if (datiChiamataTrasf.getDATIFASCICOLO().getANNOFASCICOLO() != 0)
					annoFascSiep = "" + datiChiamataTrasf.getDATIFASCICOLO().getANNOFASCICOLO();
				if (datiChiamataTrasf.getDATIFASCICOLO().getNUMEROFASCICOLO() != null)
					progrFascSiep = "" + datiChiamataTrasf.getDATIFASCICOLO().getNUMEROFASCICOLO();
			}

			if (datiChiavi.getKASIES() != 0) {
				idSoggettoSiep = "" + datiChiavi.getKASIES();
				DATIANAGRAFICI da = datiAnagrafica.getDATIANAGRAFICI();
				ISoggetto lSogCtrl = SICOLookupRemote.getSoggettoRemote();
				SoggettoModel lSoggetto = lSogCtrl.ExRicercaSoggettoByKey(new BigDecimal(idSoggettoSiep));
				boolean isItalianoNSC = ("03900".equals(da.getCODISTATOESTERONAS())) ? true : false;
				if (lSoggetto != null) {
					boolean isItalianoSIEP = ("039".equals(lSoggetto.getCodStatoNascita())) ? true : false;
					boolean areStessoSoggetto = false;
					if (isItalianoNSC && isItalianoSIEP) {
						if (da.getCODILUOGONASCITA().equals(lSoggetto.getCodComuneNascita()))
							areStessoSoggetto = true;
					} else if (!isItalianoNSC && !isItalianoSIEP) {
						if (da.getCODISTATOESTERONAS().equals(lSoggetto.getCodStatoNascita()))
							areStessoSoggetto = true;
					}
					String gg = (da.getDATANASCITA().getGIORNO() != null) ? da.getDATANASCITA().getGIORNO()
							: "01";
					String mm = (da.getDATANASCITA().getMESE() != null) ? da.getDATANASCITA().getMESE()
							: "01";
					String aa = (da.getDATANASCITA().getANNO() != null) ? da.getDATANASCITA().getANNO()
							: "1000";
					Date dn = DateUtils.getDate(aa, mm, gg);
					if (areStessoSoggetto && da.getPERSCOGNOME().equals(lSoggetto.getCognome())
							&& da.getPERSNOME().equals(lSoggetto.getNome())
							&& dn.compareTo(lSoggetto.getDataNascita()) == 0) {
						// confermo trattasi di stesso soggetto
						areStessoSoggetto = true;
					} else
						// confermo trattasi NON di stesso soggetto
						areStessoSoggetto = false;
					// TODO: IS_ATTRIBUITO_ALTRO_SOGGETTO
					// FIXME: inserire questa proprietà nel campo scelto
					;
				}
			}
			// info per il log
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.info(
					"idSoggettoSiep VALE: " + idSoggettoSiep + "idFascicoloSiep VALE: " + idFascicoloSiep
							+ "annoFascSiep VALE: " + annoFascSiep + "progrFascSiep VALE: " + progrFascSiep);

			lWebServicesController = new WebServicesController();

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.info("Cerco Fascicolo-Soggetto con CHIAVI NSC:");

			/**************************************************************************/
			/********** CERCO SENTENZA *********/
			/**************************************************************************/
			IWebServices lCtrlWs = SICOLookupRemote.getWebServicesRemote();
			BigDecimal countRisultati = new BigDecimal(0);
			// MEV 16 CUMULO: per il cumulo il trasferimento di un titolo già trasferito viene gestito lato
			// NSC (si può trasferire più volte lo stesso titolo)
			// countRisultati = lCtrlWs.ExGetCountCercaFascSogg(
			// mDatiNscToSiesModel.getFascicoloSiepModel().getKeyProvvNsc(),
			// mDatiNscToSiesModel.getSoggettoModel().getKeySoggNsc(), isForCumulo);
			countRisultati = lCtrlWs.ExGetCountCercaFascSogg(
					mDatiNscToSiesModel.getFascicoloSiepModel().getKeyProvvNsc(),
					mDatiNscToSiesModel.getSoggettoModel().getKeySoggNsc(), false);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.info("Num Fascicolo-Sogg con Key NSC:" + countRisultati);
			if (countRisultati.longValue() > 0) {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.info("Soggetto e Fascicolo già accoppiati - Titolo Presente");
				// Sentenza e Soggetto presenti su SIES - NON scriviamo il provvedimento NSC
				// NON scriviamo il provvedimento NSC - non mandiamo le KEY SIES nella risposta
				lCodiceEsito = "2"; // Procedimento e Soggetto Presenti con Chiavi NSC accoppiati
				// Inserito per far apparire Anno/Numero Procedimento
				// nell'elenco trasmissioni in caso di Titolo già presente
				FascicoloSiepModel lFascicoloSiepModel = lCtrlWs.ExPrelevaAnnoNumeroFas(
						mDatiNscToSiesModel.getFascicoloSiepModel().getKeyProvvNsc(),
						mDatiNscToSiesModel.getSoggettoModel().getKeySoggNsc());
				mDatiNscToSiesModel.setFascicoloSiepModel(lFascicoloSiepModel);
			} else {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.info("Cerco Sentenza:");
				Vector lVistaSentenza = CercaSentenza();
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.info("Num Elementi Vista sentenza:" + lVistaSentenza.size());
				if (lVistaSentenza.size() == 0) {
					// MEV 16: diversifico per cumulo
					if (!isForCumulo)
						// Sentenza non presente in SIES Scrivo Sentenza + Soggetto + Fascicolo
						// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto
						// di LogF3B.getLogger()
						siesLogger.info("Sentenza NON Presente: Scrivo Sentenza + Soggetto + Fascicolo");
					else
						// Sentenza non presente in SIES Scrivo Sentenza + Cumulo
						// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al
						// posto di LogF3B.getLogger()
						siesLogger.info("Sentenza NON Presente: Scrivo Sentenza + Soggetto + Cumulo");
					// MEV 16: aggiunti parametri di passaggio per inserire solo sentenza e cumulo e soggetto,
					// NO fasc per gestione fascicolo coinvolto nel cumulo
					// MEV 16 CUMULO: aggiunto parametro di passaggio per gestione dei titoli da associare al
					// cumulo
					lWebServicesController.ExInserisciFascicoloDaNsc(mDatiNscToSiesModel, "S", isForCumulo,
							idFascicoloSiep, lCodOperatoreIns, lCodUfficioIns, idIstruttoriaCumulo);
					lCodiceEsito = "0";
				} else {
					// Sentenza Presente - Cerco Soggetto
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger.info("Sentenza Presente - Cerco Soggetto");

					// Prendo l'ID Della Sentenza Trovata e lo scrivo nella
					// Struttuta del Model DatiNscToSies
					for (int i = 0; i <= lVistaSentenza.size() - 1; i++) {
						SentenzaModel tSentenzaModel = (SentenzaModel) lVistaSentenza.elementAt(i);
						mDatiNscToSiesModel.getSentenzaModel().setIdSentenza(tSentenzaModel.getIdSentenza());
					}

					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger.info(
							"ID Sentenza Trovata: " + mDatiNscToSiesModel.getSentenzaModel().getIdSentenza());

					Vector lVistaSoggetto = CercaSoggetto_della_Sentenza();
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger.info("SIZE Vettore Soggetto: " + lVistaSoggetto.size());
					if (lVistaSoggetto.size() < 2) {
						// Prendo l'ID Della Sentenza Trovata e lo scrivo nella
						// Struttuta del Model DatiNscToSies
						for (int i = 0; i <= lVistaSoggetto.size() - 1; i++) {
							DatiNscToSiesModel dntsm = (DatiNscToSiesModel) lVistaSoggetto.elementAt(i);
							mDatiNscToSiesModel.getSoggettoModel()
									.setIdSoggetto(dntsm.getSoggettoModel().getIdSoggetto());
						}
						// Soggetto non trovato inserisco Soggetto + Fascicolo
						// MEV 16: aggiunti parametri di passaggio per inserire solo il cumulo, NO sogg e fasc
						// per gestione fascicolo coinvolto nel cumulo
						// MEV 16 CUMULO: aggiunto parametro di passaggio per gestione dei titoli da associare
						// al cumulo
						lWebServicesController.ExInserisciFascicoloDaNsc(mDatiNscToSiesModel, "N",
								isForCumulo, idFascicoloSiep, lCodOperatoreIns, lCodUfficioIns,
								idIstruttoriaCumulo);
						lCodiceEsito = "0";
					} else {
						// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto
						// di LogF3B.getLogger()
						siesLogger.info("Titolo Presente");
						// Sentenza e Soggetto presenti su SIES - NON scriviamo
						// il provvedimento NSC - non mandiamo le KEY SIES nella risposta
						lCodiceEsito = "2"; // Procedimento e Soggetto Presenti
						// imposto la proprietà
						DatiNscToSiesModel lModel = (DatiNscToSiesModel) lVistaSoggetto.elementAt(0);
						mDatiNscToSiesModel.setFascicoloSiepModel(lModel.getFascicoloSiepModel());
					}
				}
			}
		} catch (Exception e) {
			/**********************************************/
			/* scrivi flusso XML di Risposta con ESITO KO */
			/**********************************************/
			e.printStackTrace();
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error(e.getMessage());

			if (lCodErrXml == 0) {
				lCodiceEsito = "100";
			}
			ActNscToSiesRisposta objflussoXMLRisp = new ActNscToSiesRisposta();
			// MEV 16: aggiunti parametri di passaggio per gestire fascicolo coinvolto nel cumulo
			String flussoXMLRisp = objflussoXMLRisp.processRequest(mDatiNscToSiesModel, lCodiceEsito,
					lCodOperatoreIns, lCodUfficioIns, idFascicoloSiep, progrFascSiep, annoFascSiep,
					idSoggettoSiep);

			return flussoXMLRisp;
		}

		/******************************************************************/
		/* Scrivi flusso XML di Risposta */
		/******************************************************************/

		ActNscToSiesRisposta objflussoXMLRisp = new ActNscToSiesRisposta();
		// MEV 16: aggiunti parametri di passaggio per gestire fascicolo coinvolto nel cumulo
		String flussoXMLRisp = objflussoXMLRisp.processRequest(mDatiNscToSiesModel, lCodiceEsito,
				lCodOperatoreIns, lCodUfficioIns, idFascicoloSiep, progrFascSiep, annoFascSiep,
				idSoggettoSiep);

		return flussoXMLRisp;

	}

	private Vector CercaSentenza() throws Exception {
		ISentenza lCtrlSentenza = SIEPLookupRemote.getSentenzaRemote();
		Vector lVistaSentenza = new Vector();
		try {
			// MEV 16: aggiunti parametri di passaggio per differenziare collegato al cumulo
			lVistaSentenza = lCtrlSentenza.ExRicercaSentenzaWebServices(lSentenzaModel, isForCumulo,
					idFascicoloSiep, idIstruttoriaCumulo);
		} catch (SIEPException e) {
			throw e;
		}
		return lVistaSentenza;
	}

	private Vector CercaSoggetto_della_Sentenza() throws Exception {
		IWebServices lCtrlSoggetto = SICOLookupRemote.getWebServicesRemote();
		Vector lVistaSoggetto = new Vector();
		try {
			// MEV 16: aggiunto parametri di passaggio per differenziare collegato al cumulo
			lVistaSoggetto = lCtrlSoggetto.ExRicercaSoggettoWebServices(mDatiNscToSiesModel, isForCumulo,
					idIstruttoriaCumulo);
		} catch (SIEPException e) {
			throw e;
		}
		return lVistaSoggetto;
	}

}