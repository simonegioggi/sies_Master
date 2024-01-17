package siap.sico.webservice.action;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.Utils;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;
import it.mig.sippi.service.client.SippiHelper;
import it.mig.sippi.service.richiestacertificato.type.ANAGRAFICA;
import it.mig.sippi.service.richiestacertificato.type.DATA;
import it.mig.sippi.service.richiestacertificato.type.DATIANAGRAFICI;
import it.mig.sippi.service.richiestacertificato.type.DATIAUTENTICAZIONE;
import it.mig.sippi.service.richiestacertificato.type.DATICERTIFICATO;
import it.mig.sippi.service.richiestacertificato.type.DATIRICHIESTACERTIFICATO;
import it.mig.sippi.service.richiestacertificato.type.DATIRISPOSTACERTIFICATO;
import it.mig.sippi.service.richiestacertificato.type.DATIUFFICIO;
import it.mig.sippi.service.richiestacertificato.type.DATIUTENTE;
import it.mig.sippi.service.richiestacertificato.type.ESITO;
import it.mig.sippi.service.richiestacertificato.type.SERVIZIOCERTIFICATIVO;
import it.mig.sippi.service.richiestacertificato.type.SESSO;
import it.mig.sippi.service.richiestacertificato.type.TIPOUFFICIO;
import siap.sico.codici_sies_nsc.model.CodiciSiesNscModel;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoModel;
import siap.sico.security.action.ICostantiSecurity;
import siap.sico.soggetto.model.SoggettoModel;
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.utente.controller.IUtente;
import siap.sico.utente.model.UtenteModel;
import siap.sico.util.SICOLookupRemote;
import siap.siep.fascicolo.model.FascicoloSiepCertBlobModel;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.sius.fascicolo.model.FascicoloGPModel;
import siap.sius.fascicolo.model.FascicoloSiusCertBlobModel;

/**
 * Classe che preleva i dati per la richiesta del Certificato
 * 
 * @author Engineering
 * @version	1.0
 */
@SuppressWarnings("rawtypes")
public class ActPrelevaDatiRichiestaCertificato extends ActWsBase implements ICostantiSecurity {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	CodiciSiesNscModel lCodiciSiesNscModel;
	String lCodCentralizzato = "";

	public String processRequest() throws Exception {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.info("######## ActPrelevaDatiRichiestaCertificato ##########");

		// ObjectFactory objectFactory = new ObjectFactory();
		FascicoloSiepModel fascicoloSiep = null;
		FascicoloGPModel fascicoloSius = null;
		SoggettoModel lSoggetto = null;

		String tipoFascicolo = "";
		String tipoCertificato = "";
		String descTipoCertificato = "";
		BigDecimal annoProc = null;
		BigDecimal numeroProc = null;
		BigDecimal idFascicolo = null;

		BigDecimal idEvento = null;
		EventoModel evento = null;

		if (!isRequestParameterNullObj("idEvento")) {
			idEvento = getRequestBigDecimalParameter("idEvento");

			IEvento lCtrlEve = SICOLookupRemote.getEventoRemote();
			evento = lCtrlEve.ExRicercaEventoByKey(idEvento);
		}

		// parametro che indica se deve essere richiesto un certificato per
		// utente SIEP oppure SIUS
		if (!isRequestParameterNullObj("tipoFascicolo")) {
			tipoFascicolo = getRequestStringParameter("tipoFascicolo");

			if (tipoFascicolo != null && !tipoFascicolo.equals("")) {
				if (tipoFascicolo.equals("SIEP")) {
					if (!this.isSessionAttributeNullObj("fascicolo")) {
						fascicoloSiep = ((FascicoloSiepModel) getSessionAttribute("fascicolo"));
						// Certificato Pubblico Ministero (art.21 comma 1)
						tipoCertificato = "2PM";
						descTipoCertificato = "Pubblico Ministero (art.21 comma 1)";
					}
				} else {
					if (!this.isSessionAttributeNullObj("fascicoloSiusGP")) {
						fascicoloSius = ((FascicoloGPModel) getSessionAttribute("fascicoloSiusGP"));
						// Certificato Autorità Giudiziaria (art.21 comma 1)
						tipoCertificato = "AAG";
						descTipoCertificato = "Autorità Giudiziaria (art.21 comma 1)";
					}
				}
			}
		}

		// Questo variabile verrà valorizzata solo in caso di 'OMONIMIA'.
		// Per valore = -1 viene richiesto certificato nullo
		BigInteger progAnagraficaNSC = null;
		if (!isRequestParameterNullObj("progAnagraficaNSC")) {
			progAnagraficaNSC = getRequestBigDecimalParameter("progAnagraficaNSC").toBigInteger();
		}

		if (fascicoloSiep != null) {
			lSoggetto = fascicoloSiep.getSoggetto();
			annoProc = fascicoloSiep.getChiaveAnno();
			numeroProc = fascicoloSiep.getChiaveProgr();
			idFascicolo = fascicoloSiep.getIdFascicoloSiep();
		}

		if (fascicoloSius != null) {
			lSoggetto = fascicoloSius.getFascicoloSiusModel().getSoggetto();
			annoProc = fascicoloSius.getFascicoloSiusModel().getChiaveAnno();
			numeroProc = fascicoloSius.getFascicoloSiusModel().getChiaveProgr();
			idFascicolo = fascicoloSius.getFascicoloSiusModel().getIdFascicoloSius();
		}

		UtenteModel lUtenteMod = new UtenteModel(
				(UtenteModel) getSessionAttribute(ICostantiSecurity.SESSION_UTENTE_CONNESSO));

		UfficioModel lUfficioMod = lUtenteMod.getUfficioUtente();

		/*------------------------------------------------------------------------------*/
		/* Inizio Scrittura XML */
		/*------------------------------------------------------------------------------*/

		/*******************************************************************************/
		/* Element DATI_UFFICIO - TIPO_UFFICIO */
		/*******************************************************************************/
		DATIUFFICIO datiUfficio = new DATIUFFICIO();

		// DECODIFICA TIPO_UFFICIO
		if (lUfficioMod.getCodTipoUfficio() != null && !lUfficioMod.getCodTipoUfficio().equals("")) {
			if (lUfficioMod.getCodTipoUfficio().equals("PM")) {
				datiUfficio.setCODICETIPOUFFICIO(TIPOUFFICIO.PM);
			} else if (lUfficioMod.getCodTipoUfficio().equals("PGCAP")) {
				datiUfficio.setCODICETIPOUFFICIO(TIPOUFFICIO.PGCAP);
			} else if (lUfficioMod.getCodTipoUfficio().equals("PMM")) {
				datiUfficio.setCODICETIPOUFFICIO(TIPOUFFICIO.PMM);
			} else if (lUfficioMod.getCodTipoUfficio().equals("UDS")) {
				datiUfficio.setCODICETIPOUFFICIO(TIPOUFFICIO.UDS);
			} else if (lUfficioMod.getCodTipoUfficio().equals("TDS")) {
				datiUfficio.setCODICETIPOUFFICIO(TIPOUFFICIO.TDS);
			}
		}

		// DECODIFICA CODICE_SEDE_UFFICIO
		if (lUfficioMod.getCodComune() != null) {
			lCodiciSiesNscModel = new CodiciSiesNscModel();
			lCodiciSiesNscModel.setCoDomain("COMUNE");
			lCodiciSiesNscModel.setCoSies(lUfficioMod.getCodComune());

			CodiciSiesNscModel aCodiciSIESNSCModel = DecodificaCodiceSies(lCodiciSiesNscModel);
			lCodCentralizzato = aCodiciSIESNSCModel.getCoCodcentr();
			datiUfficio.setCODICESEDEUFFICIO(lCodCentralizzato);
		} else {
			datiUfficio.setCODICESEDEUFFICIO("");
		}

		/*******************************************************************************/
		/* Element DATI_UTENTE - DATI_UFFICIO */
		/*******************************************************************************/
		DATIUTENTE datiUtente = new DATIUTENTE();
		// DATI_UFFICIO
		datiUtente.setDATIUFFICIO(datiUfficio);
		// USERNAME_SIPPI
		datiUtente.setUSERNAMESIPPI(lUtenteMod.getUserId());
		// COGNOME_UTENTE
		datiUtente.setCOGNOMEUTENTE(lUtenteMod.getCognome());
		// NOME_UTENTE
		datiUtente.setNOMEUTENTE(lUtenteMod.getNome());

		/*******************************************************************************/
		/* Element DATI_ANAGRAFICI */
		/*******************************************************************************/
		DATIANAGRAFICI datiAnagrafici = new DATIANAGRAFICI();

		// se mi trovo nel caso in cui ho selezionato un omonimo oppure un sinonimo
		// i DATI_ANAGRAFICI non sono più quelli del soggetto recuperato dal fascicolo
		// ma li devo sovrascrivere con il soggetto selezionato dalla lista (omonimi/sinonimi)
		if (progAnagraficaNSC != null && !progAnagraficaNSC.equals(new BigInteger("-1"))) {

			// PERS_COGNOME
			String cognome = getRequestStringParameter("cognome_" + progAnagraficaNSC);
			if (!cognome.equals("")) {
				datiAnagrafici.setPERSCOGNOME(cognome);
			}

			// PERS_NOME
			String nome = getRequestStringParameter("nome_" + progAnagraficaNSC);
			if (!nome.equals("")) {
				datiAnagrafici.setPERSNOME(nome);
			}

			// DECODIFICA CODI_LUOGO_NASCITA
			String codLuogoNascita = getRequestStringParameter("codLuogoNascita_" + progAnagraficaNSC);
			if (codLuogoNascita != null && !codLuogoNascita.equals("null")) {
				datiAnagrafici.setCODILUOGONASCITA(codLuogoNascita);
			}

			// DECODIFICA CODI_STATO_ESTERO_NAS
			String codEsteroNascita = getRequestStringParameter("codEsteroNascita_" + progAnagraficaNSC);
			if (codEsteroNascita != null) {
				datiAnagrafici.setCODISTATOESTERONAS(codEsteroNascita);
			}

			// DESC_COMUNE_ESTERO
			String descEsteroNascita = getRequestStringParameter("descEsteroNascita_" + progAnagraficaNSC);
			if (descEsteroNascita != null) {
				datiAnagrafici.setDESCCOMUNEESTERO(descEsteroNascita);
			}

			// DATA_NASCITA
			DATA dataNascita = new DATA();
			dataNascita.setGIORNO(getRequestStringParameter("dataNascitaGG_" + progAnagraficaNSC));
			dataNascita.setMESE(getRequestStringParameter("dataNascitaMM_" + progAnagraficaNSC));
			dataNascita.setANNO(getRequestStringParameter("dataNascitaAAAA_" + progAnagraficaNSC));
			datiAnagrafici.setDATANASCITA(dataNascita);

			// SESSO
			String sesso = getRequestStringParameter("sesso_" + progAnagraficaNSC);
			if (sesso != null && !sesso.equals("")) {
				if (sesso.equals("M")) {
					datiAnagrafici.setSESSO(SESSO.M);
				} else {
					datiAnagrafici.setSESSO(SESSO.F);
				}
			}

			// CODI_FISCALE
			String codFiscale = getRequestStringParameter("codFiscale_" + progAnagraficaNSC);
			if (codFiscale != null) {
				datiAnagrafici.setCODIFISCALE(codFiscale);
			}

			// CODI_IMPRONTA_DIGITALE
			String codiImpronta = getRequestStringParameter("codiImpronta_" + progAnagraficaNSC);
			if (codiImpronta != null) {
				datiAnagrafici.setCODIIMPRONTADIGITALE(codiImpronta);
			}

			// PERS_PATERNITA
			String paternita = getRequestStringParameter("paternita_" + progAnagraficaNSC);
			if (paternita != null) {
				datiAnagrafici.setPERSPATERNITA(paternita);
			}

			// PERS-COGNOME_MADRE
			String cognomeMadre = getRequestStringParameter("cognomeMadre_" + progAnagraficaNSC);
			if (cognomeMadre != null && !cognomeMadre.equals("")) {
				datiAnagrafici.setPERSCOGNOMEMADRE(cognomeMadre);
			}

			// PERS_NOME_MADRE
			String nomeMadre = getRequestStringParameter("nomeMadre_" + progAnagraficaNSC);
			if (nomeMadre != null && !nomeMadre.equals("")) {
				datiAnagrafici.setPERSNOMEMADRE(lSoggetto.getNomeMadre());
			}

		} else {

			// PROG_ANAGRAFICA
			datiAnagrafici.setPROGANAGRAFICA(lSoggetto.getIdSoggetto().toBigInteger());

			// PERS_COGNOME
			datiAnagrafici.setPERSCOGNOME(lSoggetto.getCognome());

			// PERS_NOME
			datiAnagrafici.setPERSNOME(lSoggetto.getNome());

			// DECODIFICA CODI_LUOGO_NASCITA
			if (lSoggetto.getCodComuneNascita() != null && !lSoggetto.getCodComuneNascita().equals("-")) {
				lCodiciSiesNscModel = new CodiciSiesNscModel();
				lCodiciSiesNscModel.setCoDomain("COMUNE");
				lCodiciSiesNscModel.setCoSies(lSoggetto.getCodComuneNascita());
				CodiciSiesNscModel aCodiciSIESNSCModel = DecodificaCodiceSies(lCodiciSiesNscModel);
				lCodCentralizzato = aCodiciSIESNSCModel.getCoCodcentr();
				datiAnagrafici.setCODILUOGONASCITA(lCodCentralizzato);
			}

			// DECODIFICA CODI_STATO_ESTERO_NAS
			if (lSoggetto.getCodStatoNascita() != null && !lSoggetto.getCodStatoNascita().equals("-")) {
				lCodiciSiesNscModel = new CodiciSiesNscModel();
				lCodiciSiesNscModel.setCoDomain("NAZIONE");
				lCodiciSiesNscModel.setCoSies(lSoggetto.getCodStatoNascita());
				CodiciSiesNscModel aCodiciSIESNSCModel = DecodificaCodiceSies(lCodiciSiesNscModel);
				lCodCentralizzato = aCodiciSIESNSCModel.getCoCodcentr();
				// Nazione Italia
				if (lCodCentralizzato.equals("03900")) {
					lCodCentralizzato = "3900";
				}
				datiAnagrafici.setCODISTATOESTERONAS(lCodCentralizzato);
			}

			// DESC_COMUNE_ESTERO
			if (lSoggetto.getDescComuneNascitaEstero() != null
					&& !lSoggetto.getDescComuneNascitaEstero().equals("")) {
				datiAnagrafici.setDESCCOMUNEESTERO(lSoggetto.getDescComuneNascitaEstero());
			}

			// DATA_NASCITA
			DATA dataNascita = new DATA();
			if (lSoggetto.getDataNascita() != null) {
				dataNascita.setGIORNO(DateUtils.getDateToString(lSoggetto.getDataNascita(), "dd"));
				dataNascita.setMESE(DateUtils.getDateToString(lSoggetto.getDataNascita(), "MM"));
				dataNascita.setANNO(DateUtils.getDateToString(lSoggetto.getDataNascita(), "yyyy"));
				datiAnagrafici.setDATANASCITA(dataNascita);
			}

			// SESSO
			if (lSoggetto.getSesso() != null && !lSoggetto.getSesso().equals("")) {
				if (lSoggetto.getSesso().equals("M")) {
					datiAnagrafici.setSESSO(SESSO.M);
				} else {
					datiAnagrafici.setSESSO(SESSO.F);
				}
			}

			// CODI_FISCALE
			if (lSoggetto.getCodFiscale() != null && !lSoggetto.getCodFiscale().equals("")) {
				datiAnagrafici.setCODIFISCALE(lSoggetto.getCodFiscale());
			}

			// CODI_IMPRONTA_DIGITALE
			if (lSoggetto.getCodAfis() != null && !lSoggetto.getCodAfis().equals("")) {
				datiAnagrafici.setCODIIMPRONTADIGITALE(lSoggetto.getCodAfis());
			}

			// PERS_PATERNITA
			if (lSoggetto.getPaternita() != null && !lSoggetto.getPaternita().equals("")) {
				datiAnagrafici.setPERSPATERNITA(lSoggetto.getPaternita());
			}

			// PERS-COGNOME_MADRE
			if (lSoggetto.getCognomeMadre() != null && !lSoggetto.getCognomeMadre().equals("")) {
				datiAnagrafici.setPERSCOGNOMEMADRE(lSoggetto.getCognomeMadre());
			}

			// PERS_NOME_MADRE
			if (lSoggetto.getNomeMadre() != null && !lSoggetto.getNomeMadre().equals("")) {
				datiAnagrafici.setPERSNOMEMADRE(lSoggetto.getNomeMadre());
			}
		}

		/*******************************************************************************/
		/* Element DATI_CERTIFICATO */
		/*******************************************************************************/
		DATICERTIFICATO datiCertificato = new DATICERTIFICATO();
		// TIPO_CERTIFICATO
		datiCertificato.setTIPOCERTIFICATO(tipoCertificato);
		// ANNO_PROC
		datiCertificato.setANNOPROC(annoProc.toBigInteger());
		// NUMERO_PROC
		datiCertificato.setNUMEROPROC(numeroProc.toBigInteger());

		/*******************************************************************************/
		/* Element ANAGRAFICA - DATI_ANAGRAFICI - DATI_CERTIFICATO */
		/*******************************************************************************/
		ANAGRAFICA anagrafica = new ANAGRAFICA();
		anagrafica.setDATIANAGRAFICI(datiAnagrafici);

		anagrafica.setDATICERTIFICATO(datiCertificato);

		/*******************************************************************************/
		/* Element DATI_RICHIESTA_CERTIFICATO */
		/*******************************************************************************/
		DATIRICHIESTACERTIFICATO datiRichiesta = new DATIRICHIESTACERTIFICATO();
		datiRichiesta.setDATIUTENTE(datiUtente);

		datiRichiesta.setANAGRAFICA(anagrafica);

		// Richiesta Certificato in caso di Omonimia/Sinonimia
		if (progAnagraficaNSC != null)
			datiRichiesta.setPROGANAGRAFICANSC(progAnagraficaNSC);

		/*******************************************************************************/
		/* Element SERVIZIO_CERTIFICATIVO */
		/*******************************************************************************/
		SERVIZIOCERTIFICATIVO servizioCertificato = new SERVIZIOCERTIFICATIVO();
		servizioCertificato.setDATIRICHIESTACERTIFICATO(datiRichiesta);

		DATIAUTENTICAZIONE datiAutenticazione = new DATIAUTENTICAZIONE();
		datiAutenticazione.setPassword(Utils.pwdNSCDecode(lUtenteMod.getPwdNSC()));
		datiAutenticazione.setUsername(lUtenteMod.getUseridNSC());

		DATIRISPOSTACERTIFICATO risposta = SippiHelper.richiestaCertificato(datiRichiesta,
				datiAutenticazione);

		RedirectTo lRedirigi = new RedirectTo();
		lRedirigi.setPage(IWebConstants.PG_MAIN);

		if (Utils.isNullObj(risposta)) {
			String testo = "ATTENZIONE! Errore nella risposta del WebService esposto da NSC (Nuovo Sistema"
					+ " informativo del Casellario giudiziale): l'oggetto 'DATIRISPOSTACERTIFICATO'"
					+ " e' nullo! Contattare il servizio di Help Desk del Casellario!";
			siesLogger.error(testo);
			siesLogger.error(Utils.isNullObj(datiRichiesta.getPROGANAGRAFICANSC()) ? "PROGANAGRAFICANSC NULLO"
					: datiRichiesta.getPROGANAGRAFICANSC());
			siesLogger.error(datiRichiesta.getDATIUTENTE().getCOGNOMEUTENTE() + " "
					+ datiRichiesta.getDATIUTENTE().getNOMEUTENTE() + " "
					+ datiRichiesta.getDATIUTENTE().getUSERNAMESIPPI());
			siesLogger.error(datiAutenticazione.getUsername());
			setRequestAttribute(IWebConstants.MESSAGE_TEXT, testo);
			if (tipoFascicolo.equals("SIEP")) {
				lRedirigi.setAction("siap.siep.istruttoria.action.ActLoadInserisciCertificatoPenale");
			} else {
				lRedirigi.setAction("siap.sius.fascicolo.action.ActLoadRichiestaCertificatoPenale");
			}
			setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi);
			return IWebConstants.PG_MESSAGE;
		}

		ActRispostaDatiRichiestaCertificato objRispostaNsc = new ActRispostaDatiRichiestaCertificato();

		FascicoloSiusCertBlobModel fascicoloSiusCertBlob = new FascicoloSiusCertBlobModel();
		FascicoloSiepCertBlobModel fascicoloSiepCertBlob = new FascicoloSiepCertBlobModel();
		if (fascicoloSiep != null) {
			fascicoloSiepCertBlob.setFascicoloSiep(fascicoloSiep);
		} else {
			fascicoloSiusCertBlob.setFascicoloSius(fascicoloSius.getFascicoloSiusModel());
		}

		Vector lListaOmonimi = null;
		if (fascicoloSiep != null) {
			lListaOmonimi = objRispostaNsc.processRequest(risposta, fascicoloSiepCertBlob, null, lUtenteMod);
		} else {
			lListaOmonimi = objRispostaNsc.processRequest(risposta, null, fascicoloSiusCertBlob, lUtenteMod);
		}

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.info("-------------------------------------------------");
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.info("lunghezza Vettore Omonimi: " + lListaOmonimi.size());
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.info("-------------------------------------------------");

		// Non ci sono OMONIMI/SINONIMI
		if (lListaOmonimi.size() == 0) {

			// Si preleva il codice dell'esito
			String codEsitoRichiesta = "";
			String descEsitoRichiesta = "";
			List<ESITO> listEsito = risposta.getArrayEsito().getESITO();
			for (ESITO esito : listEsito) {
				codEsitoRichiesta += esito.getCODICE();
				descEsitoRichiesta += " " + esito.getDESCRIZIONE();
				siesLogger.info(codEsitoRichiesta + " ### " + descEsitoRichiesta);
			}

			// Presenza del Certificato Penale o Certificato Nullo
			if (codEsitoRichiesta.contains("000")) {
				lRedirigi.setAction(
						"siap.sico.webservice.action.ActLoadCertificatoCasellarioGiudiziale&IDFascicolo="
								+ idFascicolo + "&TipoFascicolo=" + tipoFascicolo);
				setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi);
				setRequestAttribute("idFascicolo", idFascicolo.toString());
				setRequestAttribute("tipoFascicolo", tipoFascicolo);
				setRequestAttribute("evento", evento);
				return IWebConstants.PG_VISUALIZZA_CERTIFICATO_PENALE;
			} else {
				// Presenza errori o warning

				// Se l'esito della richiesta è il seguente: "ERRORE: PASSWORD NON VALIDA"
				// Bisogna cancellare i campi USERID_NSC e PWD_NSC presenti sulla tabella UTENTE,
				// così da consentire all'utente di inserire le credenziali corrette
				if (descEsitoRichiesta != null && !descEsitoRichiesta.equals("")
						&& descEsitoRichiesta.contains("ERRORE: PASSWORD NON VALIDA")) {
					IUtente lCtrl = SICOLookupRemote.getUtenteRemote();
					UtenteModel lRetModel = lCtrl.ExResetDatiAccessoNSC(lUtenteMod);
					setSessionAttribute(SESSION_UTENTE_CONNESSO, lRetModel);
				}

				setRequestAttribute(IWebConstants.MESSAGE_TEXT,
						"Si è verificato il seguente " + descEsitoRichiesta);
				if (tipoFascicolo.equals("SIEP")) {
					lRedirigi.setAction("siap.siep.istruttoria.action.ActLoadInserisciCertificatoPenale");
				} else {
					lRedirigi.setAction("siap.sius.fascicolo.action.ActLoadRichiestaCertificatoPenale");
				}
				setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi);
				return IWebConstants.PG_MESSAGE;
			}

		} else {
			// PRESENZA DI OMONIMI/SINONIMI - Passo il Vector alla JSP Risultato Richiesta Certificato
			setRequestAttribute("listaOmonimi", lListaOmonimi);
			// soggetto per il quale è stato richiesto il Certificato Penale
			setRequestAttribute("soggetto", lSoggetto);
			setRequestAttribute("tipologiaCertificato", descTipoCertificato);
			setRequestAttribute("tipoFascicolo", tipoFascicolo);
			setRequestAttribute("evento", evento);

			return f3b.web.IWebConstants.ROOT_DIR
					+ "files/siap/sico/webservice/RisultatoRichiestaCertificato.jsp";
		}
	}

}