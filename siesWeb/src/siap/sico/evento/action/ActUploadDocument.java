package siap.sico.evento.action;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoModel;
import siap.sico.soggetto.controller.ISoggetto;
import siap.sico.soggetto.model.SoggettoModel;
import siap.sico.utente.model.UtenteModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.ordineesecuzione.controller.IOrdineEsecuzione;
import siap.siep.util.SIEPLookupRemote;
import siap.sige.impugnazione.action.ICostantiImpugnazioneSige;
import siap.sige.impugnazione.controller.IImpugnazioneSige;
import siap.sige.impugnazione.model.ImpugnazioneSigeModel;
import siap.sige.util.SIGELookupRemote;
import siap.sius.SIUSException;
import siap.sius.avvocato.controller.IAvvocato;
import siap.sius.avvocato.model.AvvocatoSiusModel;
import siap.sius.avvocatura.action.ICostantiAvvisiAvvocato;
import siap.sius.avvocatura.model.AvvisiAvvocatoModel;
import siap.sius.fascicolo.action.ICostantiFascicoloSius;
import siap.sius.fascicolo.controller.IFascicoloSius;
import siap.sius.fascicolo.model.FascicoloGPModel;
import siap.sius.fascicolo.model.FascicoloSiusModel;
import siap.sius.util.SIUSLookupRemote;

/**
 * Title: ActUploadDocument
 * Description: Azione demandata alla realizzazione delle funzioni di validazione ed upload sulla tabella
 * EVENTO.
 *
 * @version 1.0
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class ActUploadDocument extends ActionSiap implements ICostantiEvento {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	// Attributo di classe contenente l'azione da passare ad eventuale jsp Warning.
	public String mAzione = "siap.sico.evento.action.ActUploadDocument";
	// Input stream contenente il documento di upload.
	public ByteArrayInputStream mInStr = null;
	// Controller IEvento utilizzato per accedere alla tabella Evento.
	public IEvento mEveCtrl = null;
	public byte[] mBytes = null;

	public String processRequest() throws Exception {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(this.getClass().getPackage().getName() + ".processRequest(): inizio");
		String lPage = IWebConstants.PG_MESSAGE;

		// Lettura ID Evento
		BigDecimal lId = getRequestBigDecimalParameter(CAMPO_ID_EVENTO);

		// INIZIO @emma 12072018 intervento post COLLAUDO 11.2
		String lFlagValidazioneEsito = "";
		if (!isRequestParameterNullObj("validazioneEsito")) {
			lFlagValidazioneEsito = getRequestStringParameter("validazioneEsito");
		}
		setRequestAttribute("validazioneEsito", lFlagValidazioneEsito);
		// Lettura ID Impugnazione
		BigDecimal lIdImpu = null;
		if (!isRequestParameterNullObj(ICostantiImpugnazioneSige.CAMPO_ID_IMPUGNAZIONE)) {
			lIdImpu = getRequestBigDecimalParameter(ICostantiImpugnazioneSige.CAMPO_ID_IMPUGNAZIONE);
		}
		setRequestAttribute(ICostantiImpugnazioneSige.CAMPO_ID_IMPUGNAZIONE, lIdImpu);
		// FINE @emma 12072018 intervento post COLLAUDO 11.2

		// *********************************************
		// MEV_AVVOCATURA - INIZIO
		// **********************************************
		// Flag che segnala la necessità di inserire o meno un record nella tabella
		// AVVISI_AVVOCATO
		String lFlgAvvocatura = "";
		if (!isRequestParameterNullObj("FlagAvvocatura")) {
			lFlgAvvocatura = getRequestStringParameter("FlagAvvocatura");
		}
		// *********************************************
		// MEV_AVVOCATURA - FINE
		// **********************************************

		// Flag che segnala la necessità del controllo della presenza del documento nel BLOB
		boolean lControlloBlob = true;
		// Se si proviene dalla form di Warning non si effettua il controllo sul BLOB
		if (!isRequestParameterNullObj(CAMPO_CK_WARNING))
			lControlloBlob = false;
		if (lControlloBlob) {
			// Lettura del file di Upload
			InputStream lInput = null;
			lInput = getFile(ICostantiEvento.CAMPO_BLOB);
			if (lInput != null && lInput.available() > 0) {
				byte[] lBuffer = new byte[lInput.available()];
				lInput.read(lBuffer);
				mInStr = new ByteArrayInputStream(lBuffer);
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("BYTE ARRAY INPUT LENGTH >>> " + mInStr.available());
				lControlloBlob = false;
			} else
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.warn("file di Upload non disponibile !");
		}

		// 20171011: [SG] controllo preventivo se l'evento sia già validato
		IEvento iEvento = SICOLookupRemote.getEventoRemote();
		EventoModel em = iEvento.ExRicercaEventoByKey(lId);
		// if (em.getFlagDocumentoRegistrato() != null && "S".equals(em.getFlagDocumentoRegistrato()))
		boolean isRichiestaIstruttoria = false;
		List<String> l = Arrays.asList("0044", "0045", "0048", "0049", "0050", "0051", "0052", "0053", "0054",
				"0557", "0565", "0566", "0578", "0579", "0580", "1050");
		if (("05".equals(em.getCodTipoEvento())
				|| ("02".equals(em.getCodTipoEvento()) && "26".equals(em.getCodTipoProvvedimento())))
				&& l.contains(em.getCodMotivo())) {
			siesLogger.info("PER QUESTO CODICE MOTIVO: " + em.getCodMotivo()
					+ " NON VALIDO IL PROCEDIMENTO PER UN EVENTO DI RICHIESTA ISTRUTTORIA [TIPO EVENTO = 05]");
			isRichiestaIstruttoria = true;
		}

		String stato = "";
		UtenteModel um = getUtenteConnesso();
		String codTipoUff = new String(um.getUfficioUtente().getCodTipoUfficio());
		// ProfileModel pm = um.getUserProfile();
		// if (pm.isSiep())
		if (codTipoUff.equals("UEPE") || codTipoUff.equals("UEPESS")) {
			stato = "SIEPE";
		} else if (codTipoUff.startsWith("TDS") || codTipoUff.startsWith("UDS")) {
			stato = "SIUS";
		} else if (codTipoUff.equals("PGCAP") || codTipoUff.equals("PM") || codTipoUff.equals("PMM")) {
			stato = "SIEP";
		} else {
			stato = "SIGE";
		}
		if ("SIEP".equals(stato) && !isRichiestaIstruttoria) {
			FascicoloSiepModel lFascMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");
			if (lFascMod != null && lFascMod.getIdFascicoloSiep() != null) {
				EventoModel lModel = new EventoModel();
				lModel.setIdEvento(getRequestBigDecimalParameter(CAMPO_ID_EVENTO));
				InputStream lInput = getFile(ICostantiEvento.CAMPO_BLOB);
				if (lInput != null) {
					byte[] lBuffer = new byte[lInput.available()];
					lInput.read(lBuffer);
					ByteArrayInputStream lSt = new ByteArrayInputStream(lBuffer);
					lModel.setDocBlobIn(lSt);
				}
				lModel.setDataAggiornamento(DateUtils.getSysDate());
				lModel.setCodUfficioAggiornamento(getCodUfficioUtenteConnesso());
				lModel.setCodOperatoreAggiornamento(getCodUtenteConnesso());
				if (isRequestChecked(ICostantiEvento.CAMPO_VALIDA)) {
					lModel.setFlagDocumentoRegistrato("S");
					IOrdineEsecuzione lCtrl = SIEPLookupRemote.getOrdineEsecuzioneRemote();
					lCtrl.ExUpdateValidaOE(lModel, lFascMod);
				} else {
					lControlloBlob = false;
					lModel.setFlagDocumentoRegistrato("N");
					IEvento lCtrl = SICOLookupRemote.getEventoRemote();
					lCtrl.ExUpdateDocument(lModel);
				}
			} else {
				if (!isRequestChecked(ICostantiEvento.CAMPO_VALIDA))
					lControlloBlob = false;
			}
		} else if (!isRequestChecked(ICostantiEvento.CAMPO_VALIDA))
			lControlloBlob = false;

		boolean lisUpdate = true;

		if (lControlloBlob) {
			// Controllo esistenza documento di stampa per consentire la validazione
			try {
				leggiDocumento(lId);
			} catch (Exception e) {
				lisUpdate = false;
				lPage = PG_WARNING;
				setRequestAttribute(IWebConstants.MESSAGE_TEXT,
						"Attenzione: E' stata richiesta la validazione di un documento privo di stampa!");
				passaggioParametri();
			}
		}
		// Update
		if (lisUpdate) {
			// INIZIO @emma 12072018 intervento post COLLAUDO 11.2
			if ("S".equals(lFlagValidazioneEsito)) {
				// se è stata chiesta la validazione dell'esito, devo updatare
				// il nuovo campo flag_validazione_esito ='S' sulla tabella IMPUGNAZIONE_SIGE
				IImpugnazioneSige lCtrl = SIGELookupRemote.getImpugnazioneSigeRemote();
				ImpugnazioneSigeModel impugnazione = lCtrl.ExRicercaImpugnazioneByKey(lIdImpu);
				impugnazione.setCodOperatoreAggiornamento(getCodUtenteConnesso());
				impugnazione.setCodUfficioAggiornamento(getCodUfficioUtenteConnesso());
				impugnazione.setDataAggiornamento(DateUtils.getSysDate());
				impugnazione.setFlagValidazioneEsito(lFlagValidazioneEsito);
				/* ImpugnazioneSigeModel llImpModRet = */lCtrl.ExModificaImpugnazione(impugnazione);
			}
			// FINE @emma 12072018 intervento post COLLAUDO 11.2

			// ***********************************************************************
			// MEV_AVVOCATURA aggiunto parametro lFlgAvvocatura al metodo updateTabella
			// ***********************************************************************
			updateTabella(lId, lFlgAvvocatura);
			// Prepara la "pagina" di destinAction
			setRequestAttribute(IWebConstants.MESSAGE_TEXT,
					"Aggiornamento Documento Avvenuto Correttamente!");

			/*
			 * ISSUE MEV : aggiunto aggiornamento stato fascicolo per decreto di tipo DM 
			 * Numero MEV : 9 
			 * Autore : Gioggi 
			 * Data : 19 nov 2020 
			 * Branch : MEV_9
			 */
			if ("SIUS".equals(stato)
					&& ("0610".equals(em.getCodEsito()) || "0271".equals(em.getCodEsito()))) {
				IFascicoloSius ifs = SIUSLookupRemote.getFascicoloSiusRemote();
				FascicoloSiusModel fsm = new FascicoloSiusModel();
				fsm.setCodOperatoreAggiornamento(getCodUtenteConnesso());
				fsm.setCodUfficioAggiornamento(getCodUfficioUtenteConnesso());
				fsm.setDataAggiornamento(DateUtils.getSysDate());
				fsm.setCodStatoFascicolo(ICostantiFascicoloSius.COD_EMESSO_DECRETO_DESIGNAZIONE);
				fsm.setIdFascicoloSius(em.getFasSiuIdFascicoloSius());
				ifs.aggiornaStatoFascicoloSius(fsm);
			}
			// ***** FINE INTERVENTO MEV_9 *****//
		}

		// Se c'è lo stack di ritorno effettua un ritorno in cima
		String lRitorno = goToRitorno();
		if (lRitorno == null && !isRequestParameterNullObj(CAMPO_AZIONE_DETTAGLIO)) {
			RedirectTo lRedirigi = new RedirectTo();
			lRedirigi.setPage(IWebConstants.PG_MAIN);
			lRedirigi.setAction(getRequestStringParameter(CAMPO_AZIONE_DETTAGLIO) + "&" + CAMPO_ID_EVENTO
					+ "=" + getRequestStringParameter(ICostantiEvento.CAMPO_ID_EVENTO));
			if (!isSessionAttributeNullObj(IWebConstants.STACK_RITORNO))
				lRedirigi.setParameter(IWebConstants.LINK_RITORNO, "");
			setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi);
		}
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(this.getClass().getPackage().getName() + ".processRequest(): fine");
		return lPage;
	}

	// Nel richiamo della pagina di Warning occorre riciclare
	// i parametri nella request
	protected void passaggioParametri() throws Exception {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(this.getClass().getPackage().getName() + ".passaggioParametri(): inizio");

		setRequestAttribute(CAMPO_ID_EVENTO, getRequestStringParameter(CAMPO_ID_EVENTO));
		setRequestAttribute(CAMPO_AZIONE_DETTAGLIO, getRequestStringParameter(CAMPO_AZIONE_DETTAGLIO));
		if (isRequestChecked(CAMPO_VALIDA)) {
			setRequestAttribute(CAMPO_VALIDA, getRequestStringParameter(CAMPO_VALIDA));
		}

		// @emma 12072018 intervento post COLLAUDO 11.2
		if (!isRequestParameterNullObj("validazioneEsito")) {
			setRequestAttribute("validazioneEsito", getRequestStringParameter("validazioneEsito"));
		}

		if (!isRequestParameterNullObj(ICostantiImpugnazioneSige.CAMPO_ID_IMPUGNAZIONE)) {
			setRequestAttribute(ICostantiImpugnazioneSige.CAMPO_ID_IMPUGNAZIONE,
					getRequestBigDecimalParameter(ICostantiImpugnazioneSige.CAMPO_ID_IMPUGNAZIONE));
		}

		// *********************************************
		// MEV_AVVOCATURA - INIZIO
		// **********************************************
		if (!isRequestParameterNullObj("FlagAvvocatura")) {
			setRequestAttribute("FlagAvvocatura", getRequestStringParameter("FlagAvvocatura"));
		}
		// *********************************************
		// MEV_AVVOCATURA - FINE
		// **********************************************

		// Si passa ACTION_FIELD alla jsp
		setRequestAttribute(IWebConstants.ACTION_FIELD, mAzione);
		this.goToRitorno();
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(this.getClass().getPackage().getName() + ".passaggioParametri(): fine");
		return;
	}

	/**
	 * La funzione esegue l'Update della tabella EVENTO con o senza l'inserimento del documento di upload nel
	 * BLOB.
	 *
	 * @param aId
	 * @throws Exception
	 */
	public void updateTabella(BigDecimal aId) throws Exception {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(this.getClass().getPackage().getName() + ".updateTabella(): inizio");

		// Valorizzazione del Model
		EventoModel lModel = new EventoModel();
		lModel.setIdEvento(aId);
		if (mInStr != null) {
			lModel.setDocBlobIn(mInStr);
		}

		lModel.setDataAggiornamento(DateUtils.getSysDate());
		lModel.setCodUfficioAggiornamento(getCodUfficioUtenteConnesso());
		lModel.setCodOperatoreAggiornamento(getCodUtenteConnesso());
		if (isRequestChecked(ICostantiEvento.CAMPO_VALIDA))
			lModel.setFlagDocumentoRegistrato("S");
		else
			lModel.setFlagDocumentoRegistrato("N");
		// Aggiornamento del record attraverso la chiamata al Controller
		if (mEveCtrl == null)
			mEveCtrl = SICOLookupRemote.getEventoRemote();

		mEveCtrl.ExUpdateDocument(lModel);
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(this.getClass().getPackage().getName() + ".updateTabella(): fine");
		return;
	}

	/**
	 * Funzione di utilità. Viene richiamata per controllare l'esistenza del documento nel BLOB della tabella
	 * EVENTO. Se tale documento non esiste viene lanciata un'eccezione.
	 *
	 * @param aId
	 * @throws Exception
	 */
	public void leggiDocumento(BigDecimal aId) throws Exception {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(this.getClass().getPackage().getName() + ".leggiDocumento(): inizio");

		// Valorizzazione del Model con la chiave di ricerca
		EventoModel lModel = new EventoModel();
		lModel.setIdEvento(aId);
		// Attivazione della funzione attraverso il Controller
		if (mEveCtrl == null)
			mEveCtrl = SICOLookupRemote.getEventoRemote();
		mEveCtrl.ExGetDocumento(lModel);

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(this.getClass().getPackage().getName() + ".leggiDocumento(): fine");
		return;
	}

	// ***********************************************************************
	// MEV_AVVOCATURA aggiunto parametro lFlgAvvocatura al metodo updateTabella
	// ***********************************************************************
	public void updateTabella(BigDecimal aId, String lFlgAvvocatura) throws Exception {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(this.getClass().getPackage().getName() + ".updateTabella(): inizio");

		// Valorizzazione del Model
		EventoModel lModel = new EventoModel();
		lModel.setIdEvento(aId);
		if (mInStr != null) {
			lModel.setDocBlobIn(mInStr);
		}

		lModel.setDataAggiornamento(DateUtils.getSysDate());
		lModel.setCodUfficioAggiornamento(getCodUfficioUtenteConnesso());
		lModel.setCodOperatoreAggiornamento(getCodUtenteConnesso());
		if (isRequestChecked(ICostantiEvento.CAMPO_VALIDA))
			lModel.setFlagDocumentoRegistrato("S");
		else
			lModel.setFlagDocumentoRegistrato("N");
		// Aggiornamento del record attraverso la chiamata al Controller
		if (mEveCtrl == null)
			mEveCtrl = SICOLookupRemote.getEventoRemote();

		// *********************************************
		// MEV_AVVOCATURA - INIZIO
		// **********************************************
		if (!lFlgAvvocatura.equals("")) {
			String testoAvviso = "";
			if (lFlgAvvocatura.equals(ICostantiAvvisiAvvocato.FISSAZIONE_UDIENZA)) {
				testoAvviso = ICostantiAvvisiAvvocato.CONTENUTO_FISSAZIONE_UDIENZA;
			}
			if (lFlgAvvocatura.equals(ICostantiAvvisiAvvocato.RINVIO_UDIENZA)) {
				testoAvviso = ICostantiAvvisiAvvocato.CONTENUTO_RINVIO_UDIENZA;
			}
			if (lFlgAvvocatura.equals(ICostantiAvvisiAvvocato.ORDINANZA_RINVIO_UDIENZA)) {
				testoAvviso = ICostantiAvvisiAvvocato.CONTENUTO_ORDINANZA_RINVIO_UDIENZA;
			}
			if (lFlgAvvocatura.equals(ICostantiAvvisiAvvocato.EMISSIONE_ORDINANZA)) {
				testoAvviso = ICostantiAvvisiAvvocato.CONTENUTO_EMISSIONE_ORDINANZA;
			}
			if (lFlgAvvocatura.equals(ICostantiAvvisiAvvocato.INSERIMENTO_RICORSO)) {
				testoAvviso = ICostantiAvvisiAvvocato.CONTENUTO_INSERIMENTO_RICORSO;
			}
			if (lFlgAvvocatura.equals(ICostantiAvvisiAvvocato.INSERIMENTO_OPPOSIZIONE)) {
				testoAvviso = ICostantiAvvisiAvvocato.CONTENUTO_INSERIMENTO_OPPOSIZIONE;
			}
			if (lFlgAvvocatura.equals(ICostantiAvvisiAvvocato.EMISSIONE_DECRETO)) {
				testoAvviso = ICostantiAvvisiAvvocato.CONTENUTO_EMISSIONE_DECRETO;
			}
			if (lFlgAvvocatura.equals(ICostantiAvvisiAvvocato.DEPOSITO_DECRETO)) {
				testoAvviso = ICostantiAvvisiAvvocato.CONTENUTO_DEPOSITO_DECRETO;
			}
			if (lFlgAvvocatura.equals(ICostantiAvvisiAvvocato.DEPOSITO_ORDINANZA)) {
				testoAvviso = ICostantiAvvisiAvvocato.CONTENUTO_DEPOSITO_ORDINANZA;
			}

			UtenteModel user = (UtenteModel) getSessionAttribute("UtenteConnesso");
			// Ufficio Emittente
			String ufficio = user.getUfficioUtente().getDescrTipoUfficio() + " di "
					+ user.getUfficioUtente().getDescrComune();

			IEvento lCtrlEve = SICOLookupRemote.getEventoRemote();
			EventoModel eveMod = lCtrlEve.ExRicercaEventoByKey(aId);
			BigDecimal lIdEvento = eveMod.getIdEvento();

			// Recupero il Fascicolo Sius in sessione
			FascicoloGPModel lFasGPMod = null;
			if (isSessionAttributeNullObj("fascicoloSiusGP"))
				throw new SIUSException(SIUSException.USER_MESSAGE,
						"Fascicolo SIUS non presente in sessione!");

			lFasGPMod = (FascicoloGPModel) getSessionAttribute("fascicoloSiusGP");

			// Ricerca avvocati assegnati al fascicolo
			IAvvocato lAvvCtrl = SIUSLookupRemote.getAvvocatoRemote();
			Vector<AvvocatoSiusModel> avvocati = null;
			avvocati = lAvvCtrl.ExRicercaAvvocatiByFascicoloNoError(
					lFasGPMod.getFascicoloSiusModel().getIdFascicoloSius());

			// Recupero i dati del soggetto dalla sessione @emma 25/08/2016 - avvocatura
			String cognomeSoggetto = "";
			String nomeSoggetto = "";

			/*
			 * ISSUE MEV : segnalazione Maffucci oggetto mail: SIUS Avvocati Di pre-esercizio - SIES MO di
			 * Roma: Eliminato recupero dalla session del soggetto che viene inserito nella tabella degli
			 * avvisi_avvocato 
			 * Numero MEV : MEV_20 
			 * Autore : monica 
			 * Data : 13/mar/2020 
			 * Branch : MEV_20
			 */
			/*
			 * if (!isSessionAttributeNullObj("soggetto")) { SoggettoModel datiSoggetto = (SoggettoModel)
			 * getSessionAttribute("soggetto"); cognomeSoggetto = datiSoggetto.getCognome(); nomeSoggetto =
			 * datiSoggetto.getNome(); } else
			 */
			// ***** FINE INTERVENTO MEV_20 *****//
			if (lFasGPMod != null && lFasGPMod.getFascicoloSiusModel() != null) {
				// provo a verificare se è presente nell'oggetto FascicoloGPModel
				cognomeSoggetto = lFasGPMod.getFascicoloSiusModel().getSoggetto() != null
						? lFasGPMod.getFascicoloSiusModel().getSoggetto().getCognome()
						: "";
				nomeSoggetto = lFasGPMod.getFascicoloSiusModel().getSoggetto() != null
						? lFasGPMod.getFascicoloSiusModel().getSoggetto().getNome()
						: "";
			} else {
				// devo procedere con una ricerca del soggetto per chiave soggetto
				BigDecimal idSoggetto = lFasGPMod.getFascicoloSiusModel().getSogIdSoggetto();
				ISoggetto lSogCtrl = SICOLookupRemote.getSoggettoRemote();
				SoggettoModel s = lSogCtrl.ExRicercaSoggettoByKey(idSoggetto);
				cognomeSoggetto = s.getCognome();
				nomeSoggetto = s.getNome();
			}

			Vector<AvvisiAvvocatoModel> lAvvvisiAvvocato = new Vector<>();

			AvvisiAvvocatoModel lAvvisiAvvModel = null;

			Iterator itxAvv = avvocati.iterator();
			while (itxAvv.hasNext()) {
				lAvvisiAvvModel = new AvvisiAvvocatoModel();

				AvvocatoSiusModel lAvv = (AvvocatoSiusModel) itxAvv.next();

				lAvvisiAvvModel.setIdAvvocato(lAvv.getAvvocato().getIdAvvocato());
				lAvvisiAvvModel.setCognomeSoggeto(cognomeSoggetto);
				lAvvisiAvvModel.setNomeSoggetto(nomeSoggetto);
				// lAvvisiAvvModel.setIdProvvedimento(lIdProvvedimento);
				// setto idEvento (emma 22/08/2016)
				lAvvisiAvvModel.setIdEvento(lIdEvento);
				lAvvisiAvvModel.setDescProvvedimento(eveMod.getDescrTipoProvvedimento());
				lAvvisiAvvModel.setUfficioEmittente(ufficio);
				lAvvisiAvvModel.setTestoAvviso(testoAvviso);
				lAvvisiAvvModel.setFlagVisualizzazione("N");
				lAvvisiAvvModel.setCodOperatoreInserimento(user.getUserId());
				lAvvisiAvvModel.setCodUfficioInserimento(user.getUfficioUtente().getCodUfficio());
				lAvvisiAvvModel.setDataInserimento(DateUtils.getSysDate());

				lAvvvisiAvvocato.add(lAvvisiAvvModel);
			}

			mEveCtrl.ExUpdateDocument(lModel, lAvvvisiAvvocato);
		} else {
			mEveCtrl.ExUpdateDocument(lModel);
		}
		// *********************************************
		// MEV_AVVOCATURA - FINE
		// **********************************************

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(this.getClass().getPackage().getName() + ".updateTabella(): fine");
		return;
	}

}