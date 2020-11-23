package siap.sius.depositosentenza.action;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.StringTokenizer;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;
import siap.sico.SICOException;
import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.model.EventoModel;
import siap.siep.autoritaesterna.model.AutoritaEsternaModel;
import siap.siep.notifica.controller.INotifica;
import siap.siep.notifica.model.NotificaModel;
import siap.siep.util.SIEPLookupRemote;
import siap.sius.ActionSius;
import siap.sius.SIUSException;
import siap.sius.depositoordinanzapc.action.ICostantiDepositoOrdinanzaPc;
import siap.sius.depositosentenza.controller.IDepositoSentenza;
import siap.sius.depositosentenza.model.DepositoSentenzaModel;
import siap.sius.depositosentenza.model.SentenzaEventoTenoriGProcModel;
import siap.sius.fascicolo.action.ICostantiFascicoloSius;
import siap.sius.fascicolo.controller.IFascicoloSius;
import siap.sius.fascicolo.model.FascicoloGPModel;
import siap.sius.generaleprocedimento.model.GeneraleProcedimentoModel;
import siap.sius.magistratorelatore.controller.IMagistratoRelatore;
import siap.sius.magistratorelatore.model.MagistratoRelatoreModel;
import siap.sius.provvedimento.util.RicercaProvvedimentiUtil;
import siap.sius.richiestaatti.action.ICostantiRichiestaAtti;
import siap.sius.tenore.model.TenoreModel;
import siap.sius.udienza.action.ICostantiUdienza;
import siap.sius.util.SIUSLookupRemote;

/**
 * <p>
 * Title: ActInserisciRimessioneAtti
 * </p>
 * <p>
 * Description: Classe Action per la load inserisci di Emissione Sentenza Rimessione Atti
 * </p>
 * <p>
 * Company: Engineering S.p.A.
 * </p>
 *
 * @version 1.0
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class ActInserisciRimessioneAtti extends ActionSius implements ICostantiDepositoOrdinanzaPc {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
	public String mRetPage = IWebConstants.PG_MESSAGE;

	// Variabili di sessione.
	protected FascicoloGPModel lFasGPMod = null;
	private String mCodiceOperatore = null;
	private String mCodiceUfficio = null;
	private String mCodiceComune = null;
	// Id Generale Procedimento
	private BigDecimal mIdGenProc = null;
	// Data odierna
	protected Date mOggi = null;
	// Cod. Magistrato Relatore
	private String mCodMagistrato = null;

	public String processRequest() throws Exception {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("ActInserisciRimessioneAtti: inizio");

		// setLinkRitorno(); Sostituzione perchè problemi con bottone di ritorno
		// this.gestioneRitorno();
		// Elimino bottone di ritorno
		removeSessionAttribute("StackDiRitorno");

		// Inizializzazione data
		mOggi = DateUtils.getSysDate();

		// Si prelevano dati di sessione.
		mCodiceOperatore = getCodUtenteConnesso();
		mCodiceUfficio = getCodUfficioUtenteConnesso();
		mCodiceComune = getCodComuneUtenteConnesso();

		setRequestAttribute(IWebConstants.MESSAGE_TEXT, "Rimessione non disponibile ...");

		// Si preleva dalla sessione il fascicolo GPModel.
		if (this.isSessionAttributeNullObj("fascicoloSiusGP"))
			throw new SIUSException(SIUSException.USER_MESSAGE, "fascicoloSiusGP non in sessione");
		lFasGPMod = new FascicoloGPModel((FascicoloGPModel) getSessionAttribute("fascicoloSiusGP"));

		// Preleva id generale procedimento.
		mIdGenProc = lFasGPMod.getGeneraleProcedimentoModel().getIdGeneraleProcedimento();
		if (mIdGenProc == null)
			throw new SIUSException(SIUSException.USER_MESSAGE, "ID Generale Procedimento non in sessione");

		RicercaProvvedimentiUtil lRicerca = new RicercaProvvedimentiUtil(mIdGenProc);
		boolean lEsistenzaDoc = lRicerca.verificaEsistenzaProv();
		if (lEsistenzaDoc)
			throw new SIUSException(SIUSException.USER_MESSAGE,
					"Per il procedimento indicato è già stato emesso un provvedimento. Non è consentito emettere un nuovo provvedimento");

		// Switch tipo decreto. Anticipato per effettuare il controllo
		String lCodTipoDec = RIMESSIONE_ATTI;

		// Preleva data di emissione
		Date lDataEmissione = getRequestDateParameter(CAMPO_ANNO_DATA_EMISSIONE, CAMPO_MESE_DATA_EMISSIONE,
				CAMPO_GIORNO_DATA_EMISSIONE);

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("Data Emissione :" + lDataEmissione);

		// Ricerca del Magistrato Relatore
		IMagistratoRelatore lMagCtrl = SIUSLookupRemote.getMagistratoRelatoreRemote();
		MagistratoRelatoreModel lMagRel = lMagCtrl
				.ExRicercaEstesaMagRelByFascicolo(lFasGPMod.getFascicoloSiusModel().getIdFascicoloSius());

		// Prelevare il codice Magistrato_Relatore
		if (lMagRel != null && lMagRel.getMagistrato() != null)
			mCodMagistrato = lMagRel.getMagistrato().getCodMagistrato();

		String lCodContenuto = getRequestStringParameter(ICostantiFascicoloSius.CAMPO_COD_CONTENUTO);

		// Lettura Tenori
		StringTokenizer lStCodice = new StringTokenizer(
				getRequestStringParameter(ICostantiFascicoloSius.CAMPO_COD_OGGETTO), "|");
		StringTokenizer lStDescr = new StringTokenizer(
				getRequestStringParameter(ICostantiFascicoloSius.CAMPO_DESCR_OGGETTO), "\n");
		String lStCodiceDet = new String(
				getRequestStringParameter(ICostantiFascicoloSius.CAMPO_COD_DETTAGLIO_OGGETTO));

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("### DEBUG DESCRIZIONE OGGETTI ###");
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("N.ro Cod oggetti ->" + lStCodice.countTokens());
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("N.ro Descr. oggetti ->" + lStDescr.countTokens());
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(
				"Descr. oggetti ->" + getRequestStringParameter(ICostantiFascicoloSius.CAMPO_DESCR_OGGETTO));

		int lSizeVector = lStCodice.countTokens();

		TenoreModel lTenori[] = new TenoreModel[lSizeVector];
		String[] lEsiti = new String[lSizeVector];

		int lIndex = 0;
		while (lStCodice.hasMoreTokens() && lStDescr.hasMoreTokens()) {
			TenoreModel lTenModel = new TenoreModel();

			lTenModel.setCodOggettoTenore(lStCodice.nextToken());
			lTenModel.setDescrOggettoTenore(lStDescr.nextToken());
			// TODO perchè viene settato questo codice ??????
			lTenModel.setCodEsitoTenore("0605");
			// Aggiunta la valorizzazione dell'eventuale Dettaglio Oggetto.
			if ((lStCodiceDet).indexOf(lTenModel.getCodOggettoTenore() + "0") < 0) {
				lTenModel.setCodDettaglioOggetto("-");
			} else {
				String lCodDettaglioCorrente = lStCodiceDet.substring(
						lStCodiceDet.indexOf(lTenModel.getCodOggettoTenore() + "0") + 4,
						lStCodiceDet.indexOf(lTenModel.getCodOggettoTenore() + "0") + 8);
				lTenModel.setCodDettaglioOggetto(lCodDettaglioCorrente);
			}

			lTenModel.setProgrTenore(new BigDecimal((double) (lIndex + 1)));
			lTenModel.setCodUfficioInserimento(mCodiceUfficio); // Codice dell'ufficio dell'operatore che
																// inserisce
			lTenModel.setCodOperatoreInserimento(mCodiceOperatore); // Codice dell'operatore che inserisce
			lTenModel.setDataInserimento(mOggi);
			lTenModel.setGenPridGeneraleProcedimento(mIdGenProc);
			lTenModel.setCodMagistrato(mCodMagistrato);

			// Setto l'Array
			lTenori[lIndex] = lTenModel;
			lEsiti[lIndex] = "0605";
			lIndex++;
		}

		// lettura contenuto e passaggio oltre
		setRequestAttribute("contenuto", lCodContenuto);
		// trasferimento tenori
		setRequestAttribute("tenori", lTenori);
		// trasferimento lista esiti
		setRequestAttribute("esiti", lEsiti);
		setRequestAttribute("tipo_decreto", lCodTipoDec);
		setRequestAttribute("data_emissione", lDataEmissione);

		try {
			SentenzaEventoTenoriGProcModel lSenEveTenGP = new SentenzaEventoTenoriGProcModel();
			lSenEveTenGP.setEvento(generaEvento(lDataEmissione));
			lSenEveTenGP.setSentenza(generaSentenza(lDataEmissione));
			// lOrdEveTenGP.setTenori(generaTenori());
			lSenEveTenGP.setTenori(lTenori);
			lSenEveTenGP.setGeneraleProcedimento(generaProcedimento());
			// Lettura di ulteriori dati di rimessione atti
			lSenEveTenGP = generaDati(lSenEveTenGP);

			// Inserimento
			lSenEveTenGP = inserimento(lSenEveTenGP);

			// inserimento delle notifiche
			ArrayList listaNotifiche = null;

			listaNotifiche = generaNotifiche(lDataEmissione, lSenEveTenGP.getEvento().getIdEvento());

			// Chiamata al Controller per inserimento NOTIFICA
			INotifica lCtrlNot = SIEPLookupRemote.getNotificaRemote();
			listaNotifiche = lCtrlNot.ExInserisciNotifiche(listaNotifiche);

			// aggiornamento dei dati in sessione
			IFascicoloSius lFasCtrl = SIUSLookupRemote.getFascicoloSiusRemote();
			lFasGPMod = lFasCtrl
					.ExRicercaFascicoloByKey(lFasGPMod.getFascicoloSiusModel().getIdFascicoloSius());
			setSessionAttribute("fascicoloSiusGP", lFasGPMod);

			// pagina dettaglio della sentenza
			RedirectTo lRedirectTo = new RedirectTo();
			lRedirectTo.setPage(IWebConstants.PG_MAIN);
			lRedirectTo.setAction("siap.sius.depositosentenza.action.ActLoadDettaglioSentenza");
			lRedirectTo.setParameter(ICostantiEvento.CAMPO_ID_EVENTO,
					lSenEveTenGP.getEvento().getIdEvento().toString());
			mRetPage = lRedirectTo.toString();
		} catch (SICOException daoex) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Exception: " + daoex);
			if (daoex.getErrorCode() == F3BException.USER_MESSAGE) {
				mRetPage = IWebConstants.PG_MESSAGE;
				setRequestAttribute(IWebConstants.MESSAGE_TEXT, daoex.getMessage());
			} else
				throw daoex;
		} catch (Exception e) {
			throw e;
		}

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("ActInserisciRimessioneAtti: -> page: " + mRetPage);
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("ActInserisciRimessioneAtti: fine");

		return mRetPage; // restituisce la jsp di VIEW
	}

	public String selezione() throws Exception {

		// Lettura contenuto
		String lCodContenuto = getRequestStringParameter(ICostantiFascicoloSius.CAMPO_COD_CONTENUTO);
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("cod Tipo lCodContenuto = " + lCodContenuto);

		// Generazione automatica in base al contenuto
		Collection lOggetti = DecodificheManager.getInstance().getOggettoProcedimento();
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("lOggetti = " + lOggetti.toString());
		// lettura tipo di sentenza Sempre automatica 13-9-04
		String lCodTipoDec = null;
		// lCodTipoDec = DecodificheUtils.getCodAltebyCode(lOggetti, lCodContenuto);
		lCodTipoDec = ICostantiDepositoSentenza.RIMESSIONE_ATTI;
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("cod Tipo Sentenza = " + lCodTipoDec);

		// per l'ordinanza di conversione serve trovare il fascicolo origine
		// anche detto precedimento collegato
		/*
		 * if(getSessionAttribute("fascicoloSiusGP") != null){ FascicoloGPModel lFasGPMod = new
		 * FascicoloGPModel((FascicoloGPModel)getSessionAttribute("fascicoloSiusGP")); BigDecimal
		 * lIdFascicoloSiusOrigine = lFasGPMod.getFascicoloSiusModel().getIdFascicoloSiusOrigine(); }
		 */

		// if (lCodTipoDec == null)
		// throw new SIUSException(SIUSException.USER_MESSAGE, "Tipo sentenza automatico non definito");

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("cod Tipo sentenza decodificata" + lCodTipoDec);
		return lCodTipoDec;
	}

	GeneraleProcedimentoModel generaProcedimento() throws F3BException {

		// Istanzia model generale procedimento.
		GeneraleProcedimentoModel lGenProcModel = new GeneraleProcedimentoModel();
		lGenProcModel.setIdGeneraleProcedimento(mIdGenProc);
		lGenProcModel.setCodOggettoProcedimento(
				getRequestStringParameter(ICostantiFascicoloSius.CAMPO_COD_CONTENUTO));
		lGenProcModel.setDataAggiornamento(mOggi);
		lGenProcModel.setCodUfficioAggiornamento(mCodiceUfficio);
		lGenProcModel.setCodOperatoreAggiornamento(mCodiceOperatore);

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("Generale Procedimento = " + lGenProcModel);

		return lGenProcModel;
	}

	/**
	 * Prepara con i dati il Model per il Deposito Sentenza.
	 * <p>
	 *
	 * @param aDataEmissione
	 *            Date data di emissione
	 * @throws F3BException
	 *             propaga errori di eccezione.
	 * @return DepositoOrdinanzaPcModel riotrna istanza del model opportunemnte popolato.
	 */
	private DepositoSentenzaModel generaSentenza(Date aDataEmissione) throws F3BException {

		// Prepara il model DepositoSentenza.
		DepositoSentenzaModel lDepSenModel = new DepositoSentenzaModel();
		lDepSenModel.setCodTipoSentenza(ICostantiDepositoSentenza.RIMESSIONE_ATTI);
		lDepSenModel.setCodMagistrato(mCodMagistrato);
		lDepSenModel.setGenPridGeneraleProcedimento(mIdGenProc);
		lDepSenModel.setCodOperatoreInserimento(mCodiceOperatore);
		lDepSenModel.setCodUfficioInserimento(mCodiceUfficio);
		lDepSenModel.setDataInserimento(mOggi);
		lDepSenModel.setDataEmissione(aDataEmissione);

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("DepositoSentenza = " + lDepSenModel);

		return lDepSenModel;
	}

	/**
	 * Prepara con i relativi dati, il Model per l'evento.
	 * <p>
	 *
	 * @param aDataEmissione
	 *            Date data di emissione.
	 * @throws F3BException
	 *             propaga errore di eccezione.
	 * @return EventoModel ritorna l'evento model.
	 */
	private EventoModel generaEvento(Date aDataEmissione) throws F3BException {

		// Prepara Model Evento.
		EventoModel lEvento = new EventoModel();
		lEvento.setCodTipoEvento("01"); // 01 = Provvedimento.
		lEvento.setCodTipoProvvedimento("01"); // 01 = Sentenza.
		lEvento.setCodLuogoEmittente(mCodiceComune);
		lEvento.setCodUfficioEmittente(mCodiceUfficio);
		// TODO perchè 0605 ??????
		lEvento.setCodEsito("0605");
		lEvento.setDataEmissione(aDataEmissione);
		lEvento.setFasSieIdFascicoloSiep(lFasGPMod.getFascicoloSiusModel().getFasSieIdFascicoloSiep());
		lEvento.setFasSiuIdFascicoloSius(lFasGPMod.getFascicoloSiusModel().getIdFascicoloSius());
		lEvento.setCodOperatoreInserimento(getCodUtenteConnesso());
		lEvento.setCodUfficioInserimento(mCodiceUfficio);
		lEvento.setDataInserimento(mOggi);
		lEvento.setCodLuogoDestinatario("-");
		lEvento.setCodTipoUfficioDestinatario("-");
		lEvento.setCodUfficioDestinatario("-");
		lEvento.setAnnoProtocollo(new BigDecimal(DateUtils.getSysDate("yyyy")));
		lEvento.setCodMagistrato(mCodMagistrato);

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("Evento = " + lEvento);
		return lEvento;
	}

	/**
	 * Prepara con i relativi dati, il vettore delle Notifiche.
	 * <p>
	 *
	 * @param aDataEmissione
	 *            Date data di emissione.
	 * @throws F3BException
	 *             propaga errore di eccezione.
	 * @return EventoModel ritorna l'evento model.
	 */
	protected ArrayList generaNotifiche(Date aDataEmissione, BigDecimal EveIdEvento) throws F3BException {

		// Vector per le notifiche.
		ArrayList lNotifiche = new ArrayList();

		// Popola Vettore
		if (!isRequestParameterNullObj(CAMPO_PRESIDENZA_CONSIGLIO)) {
			if (isRequestChecked(CAMPO_PRESIDENZA_CONSIGLIO)) {

				NotificaModel lNot = new NotificaModel();

				lNot.setEveIdEvento(EveIdEvento);
				lNot.setCodTipoNotifica(ICostantiUdienza.CODTIPONOTIFICA);
				lNot.setDataInvio(aDataEmissione);
				lNot.setCodEsito("-");
				lNot.setCodOperatoreInserimento(mCodiceOperatore);
				lNot.setDataInserimento(DateUtils.getSysDate());
				lNot.setCodUfficioInserimento(mCodiceUfficio);
				// lNot.setUffCodUfficio("A6");
				// Crea Model Autorità Esterna
				AutoritaEsternaModel lAutorita = new AutoritaEsternaModel();
				lAutorita.setCodTipoAutorita("A6");
				lAutorita.setCodSede("-");
				lAutorita.setCodOperatoreInserimento(mCodiceOperatore);
				lAutorita.setCodUfficioInserimento(mCodiceUfficio);
				lAutorita.setDataInserimento(DateUtils.getSysDate());
				// Aggiunge il model Autorità Esterna alla Notifica
				lNot.setAutoritaEsterna(lAutorita);

				lNotifiche.add(lNot);
			}
		}

		if (!isRequestParameterNullObj(CAMPO_CORTE_GIUSTIZIA_EUROPEA)) {
			if (isRequestChecked(CAMPO_CORTE_GIUSTIZIA_EUROPEA)) {

				NotificaModel lNot = new NotificaModel();

				lNot.setEveIdEvento(EveIdEvento);
				lNot.setCodTipoNotifica(ICostantiUdienza.CODTIPONOTIFICA);
				lNot.setDataInvio(aDataEmissione);
				lNot.setCodEsito("-");
				lNot.setCodOperatoreInserimento(mCodiceOperatore);
				lNot.setDataInserimento(DateUtils.getSysDate());
				lNot.setCodUfficioInserimento(mCodiceUfficio);
				// lNot.setUffCodUfficio("A4");
				AutoritaEsternaModel lAutorita = new AutoritaEsternaModel();
				lAutorita.setCodTipoAutorita("A4");
				lAutorita.setCodSede("-");
				lAutorita.setCodOperatoreInserimento(mCodiceOperatore);
				lAutorita.setCodUfficioInserimento(mCodiceUfficio);
				lAutorita.setDataInserimento(DateUtils.getSysDate());
				// Aggiunge il model Autorità Esterna alla Notifica
				lNot.setAutoritaEsterna(lAutorita);

				lNotifiche.add(lNot);
			}
		}

		if (!isRequestParameterNullObj(CAMPO_CORTE_COSTITUZIONALE)) {
			if (isRequestChecked(CAMPO_CORTE_COSTITUZIONALE)) {

				NotificaModel lNot = new NotificaModel();

				lNot.setEveIdEvento(EveIdEvento);
				lNot.setCodTipoNotifica(ICostantiUdienza.CODTIPONOTIFICA);
				lNot.setDataInvio(aDataEmissione);
				lNot.setCodEsito("-");
				lNot.setCodOperatoreInserimento(mCodiceOperatore);
				lNot.setDataInserimento(DateUtils.getSysDate());
				lNot.setCodUfficioInserimento(mCodiceUfficio);
				// lNot.setUffCodUfficio("A5");
				AutoritaEsternaModel lAutorita = new AutoritaEsternaModel();
				lAutorita.setCodTipoAutorita("A5");
				lAutorita.setCodSede("-");
				lAutorita.setCodOperatoreInserimento(mCodiceOperatore);
				lAutorita.setCodUfficioInserimento(mCodiceUfficio);
				lAutorita.setDataInserimento(DateUtils.getSysDate());
				// Aggiunge il model Autorità Esterna alla Notifica
				lNot.setAutoritaEsterna(lAutorita);

				lNotifiche.add(lNot);
			}
		}

		if (!isRequestParameterNullObj(CAMPO_PRESIDENZA_GIUNTA)) {
			String noteRegione = "";

			noteRegione = getRequestStringParameter(CAMPO_PRESIDENZA_GIUNTA);

			if (noteRegione.length() > 0) {

				NotificaModel lNot = new NotificaModel();

				lNot.setEveIdEvento(EveIdEvento);
				lNot.setCodTipoNotifica(ICostantiUdienza.CODTIPONOTIFICA);
				lNot.setDataInvio(aDataEmissione);
				lNot.setCodEsito("-");
				lNot.setCodOperatoreInserimento(mCodiceOperatore);
				lNot.setDataInserimento(DateUtils.getSysDate());
				lNot.setCodUfficioInserimento(mCodiceUfficio);
				// lNot.setUffCodUfficio("A9");
				AutoritaEsternaModel lAutorita = new AutoritaEsternaModel();
				lAutorita.setCodTipoAutorita("A9");
				lAutorita.setCodSede("-");
				lAutorita.setCodOperatoreInserimento(mCodiceOperatore);
				lAutorita.setCodUfficioInserimento(mCodiceUfficio);
				lAutorita.setDataInserimento(DateUtils.getSysDate());
				// Aggiunge il model Autorità Esterna alla Notifica
				lNot.setAutoritaEsterna(lAutorita);
				lNot.setNote(noteRegione);

				lNotifiche.add(lNot);
			}
		}

		if (!isRequestParameterNullObj(ICostantiRichiestaAtti.CAMPO_COD_DESTINATARIO)) {
			String autoritaDestinazionePerSoggetto = "";
			String sedeAutorita = "";

			autoritaDestinazionePerSoggetto = getRequestStringParameter(
					ICostantiRichiestaAtti.CAMPO_COD_DESTINATARIO);
			sedeAutorita = getRequestStringParameter(ICostantiRichiestaAtti.CAMPO_SEDE);

			if (!autoritaDestinazionePerSoggetto.equals("-") && !sedeAutorita.equals("")) {
				NotificaModel lNot = new NotificaModel();

				lNot.setEveIdEvento(EveIdEvento);
				lNot.setCodTipoNotifica(ICostantiUdienza.CODTIPONOTIFICA);
				lNot.setDataInvio(aDataEmissione);
				lNot.setCodEsito("-");
				lNot.setCodOperatoreInserimento(mCodiceOperatore);
				lNot.setDataInserimento(DateUtils.getSysDate());
				lNot.setCodUfficioInserimento(mCodiceUfficio);
				// lNot.setUffCodUfficio("-");
				if (lFasGPMod == null) {
					if (this.isSessionAttributeNullObj("fascicoloSiusGP"))
						throw new SIUSException(SIUSException.USER_MESSAGE,
								"fascicoloSiusGP non in sessione");
					lFasGPMod = new FascicoloGPModel(
							(FascicoloGPModel) getSessionAttribute("fascicoloSiusGP"));
				}
				lNot.setSogIdSoggetto(lFasGPMod.getFascicoloSiusModel().getSogIdSoggetto());
				// Crea Model Autorità Esterna
				AutoritaEsternaModel lAutorita = new AutoritaEsternaModel();
				String lCodComuneSede = getCodComuneByDescrFlagVal(sedeAutorita).getCodComune();
				lAutorita.setCodTipoAutorita(autoritaDestinazionePerSoggetto);
				lAutorita.setCodSede(lCodComuneSede);
				lAutorita.setCodOperatoreInserimento(mCodiceOperatore);
				lAutorita.setCodUfficioInserimento(mCodiceUfficio);
				lAutorita.setDataInserimento(DateUtils.getSysDate());
				// Aggiunge il model Autorità Esterna alla Notifica
				lNot.setAutoritaEsterna(lAutorita);

				lNotifiche.add(lNot);
			}
		}

		// Notifiche per Avvocato/i
		String lSediAvvocato[] = getRequestStringParameters(ICostantiRichiestaAtti.CAMPO_SEDE_AVVOCATO);
		String lDestinatariAvvocato[] = getRequestStringParameters(
				ICostantiRichiestaAtti.CAMPO_COD_AVVOCATO_DESTINATARIO);
		String lAvvocato[] = getRequestStringParameters(ICostantiUdienza.CAMPO_COD_AVVOCATO);
		String[] lIndirizziAvvocato = getRequestStringParameters(
				ICostantiRichiestaAtti.CAMPO_INDIRIZZO_AVVOCATO);

		// Avvocati
		int lSize = lDestinatariAvvocato.length;
		for (int x = 0; x < lSize; x++) {
			if (!lDestinatariAvvocato[x].equals("-") && !lSediAvvocato[x].equals("")) {
				String lCodComuneSede = getCodComuneByDescrFlagVal(lSediAvvocato[x]).getCodComune();

				NotificaModel lNot = new NotificaModel();
				lNot.setEveIdEvento(EveIdEvento);
				lNot.setCodTipoNotifica(ICostantiUdienza.CODTIPONOTIFICA);
				lNot.setDataInvio(aDataEmissione);
				lNot.setCodOperatoreInserimento(mCodiceOperatore);
				lNot.setDataInserimento(DateUtils.getSysDate());
				lNot.setCodUfficioInserimento(mCodiceUfficio);
				lNot.setCodEsito("-");
				lNot.setUffCodUfficio("-");
				if (lAvvocato[x] != null && lAvvocato[x].trim().length() > 0) {
					lNot.setCodTipoNotifica(ICostantiUdienza.CODTIPONOTIFICA);
					BigDecimal lAvvid = new BigDecimal(lAvvocato[x]);
					lNot.setAvvIdAvvocatoFascicoloSius(lAvvid);
				}
				lNot.setNote(lIndirizziAvvocato[x]); // Vincenzo 18/01/2007 Allineato il Campo Note rispetto
														// ai destinatari.
				// Crea Model Autorità Esterna
				AutoritaEsternaModel lAutorita = new AutoritaEsternaModel();
				lAutorita.setCodTipoAutorita(lDestinatariAvvocato[x]);
				lAutorita.setCodSede(lCodComuneSede);
				lAutorita.setCodOperatoreInserimento(mCodiceOperatore);
				lAutorita.setCodUfficioInserimento(mCodiceUfficio);
				lAutorita.setDataInserimento(DateUtils.getSysDate());

				// Aggiunge il model Autorità Esterna alla Notifica
				lNot.setAutoritaEsterna(lAutorita);
				// Aggiunge il model delle notifiche al vettore.
				lNotifiche.add(lNot);
			}
		}

		if (!isRequestParameterNullObj(CAMPO_PRESIDENTE_SENATO)) {
			if (isRequestChecked(CAMPO_PRESIDENTE_SENATO)) {

				NotificaModel lNot = new NotificaModel();

				lNot.setEveIdEvento(EveIdEvento);
				lNot.setCodTipoNotifica(ICostantiUdienza.CODTIPONOTIFICACOMUNICAZIONE);
				lNot.setDataInvio(aDataEmissione);
				lNot.setCodEsito("-");
				lNot.setCodOperatoreInserimento(mCodiceOperatore);
				lNot.setDataInserimento(DateUtils.getSysDate());
				lNot.setCodUfficioInserimento(mCodiceUfficio);
				// lNot.setUffCodUfficio("A7");
				AutoritaEsternaModel lAutorita = new AutoritaEsternaModel();
				lAutorita.setCodTipoAutorita("A7");
				lAutorita.setCodSede("-");
				lAutorita.setCodOperatoreInserimento(mCodiceOperatore);
				lAutorita.setCodUfficioInserimento(mCodiceUfficio);
				lAutorita.setDataInserimento(DateUtils.getSysDate());
				// Aggiunge il model Autorità Esterna alla Notifica
				lNot.setAutoritaEsterna(lAutorita);

				lNotifiche.add(lNot);
			}
		}
		if (!isRequestParameterNullObj(CAMPO_PRESIDENTE_CAMERA)) {
			if (isRequestChecked(CAMPO_PRESIDENTE_CAMERA)) {

				NotificaModel lNot = new NotificaModel();

				lNot.setEveIdEvento(EveIdEvento);
				lNot.setCodTipoNotifica(ICostantiUdienza.CODTIPONOTIFICACOMUNICAZIONE);
				lNot.setDataInvio(aDataEmissione);
				lNot.setCodEsito("-");
				lNot.setCodOperatoreInserimento(mCodiceOperatore);
				lNot.setDataInserimento(DateUtils.getSysDate());
				lNot.setCodUfficioInserimento(mCodiceUfficio);
				// lNot.setUffCodUfficio("A8");
				AutoritaEsternaModel lAutorita = new AutoritaEsternaModel();
				lAutorita.setCodTipoAutorita("A8");
				lAutorita.setCodSede("-");
				lAutorita.setCodOperatoreInserimento(mCodiceOperatore);
				lAutorita.setCodUfficioInserimento(mCodiceUfficio);
				lAutorita.setDataInserimento(DateUtils.getSysDate());
				// Aggiunge il model Autorità Esterna alla Notifica
				lNot.setAutoritaEsterna(lAutorita);

				lNotifiche.add(lNot);
			}
		}
		if (!isRequestParameterNullObj(CAMPO_PRESIDENTE_CONSIGLIO)) {
			if (isRequestChecked(CAMPO_PRESIDENTE_CONSIGLIO)) {

				NotificaModel lNot = new NotificaModel();

				lNot.setEveIdEvento(EveIdEvento);
				lNot.setCodTipoNotifica(ICostantiUdienza.CODTIPONOTIFICACOMUNICAZIONE);
				lNot.setDataInvio(aDataEmissione);
				lNot.setCodEsito("-");
				lNot.setCodOperatoreInserimento(mCodiceOperatore);
				lNot.setDataInserimento(DateUtils.getSysDate());
				lNot.setCodUfficioInserimento(mCodiceUfficio);
				// lNot.setUffCodUfficio("A6");
				AutoritaEsternaModel lAutorita = new AutoritaEsternaModel();
				lAutorita.setCodTipoAutorita("A6");
				lAutorita.setCodSede("-");
				lAutorita.setCodOperatoreInserimento(mCodiceOperatore);
				lAutorita.setCodUfficioInserimento(mCodiceUfficio);
				lAutorita.setDataInserimento(DateUtils.getSysDate());
				// Aggiunge il model Autorità Esterna alla Notifica
				lNot.setAutoritaEsterna(lAutorita);

				lNotifiche.add(lNot);
			}

		}

		return lNotifiche;
	}

	// Funzione di lettura dei dati opzionali
	private SentenzaEventoTenoriGProcModel generaDati(SentenzaEventoTenoriGProcModel aModel)
			throws F3BException {

		// /////////////////////////////////////////////////
		// Aggiornamento dei dati in DepositoSentenza. //
		// /////////////////////////////////////////////////
		DepositoSentenzaModel lDepSenModel = aModel.getSentenza();

		String filtroRimessione = "";
		String filtroRimessioneDescr = "";

		lDepSenModel.setCodNaturaProvvedimento("-");
		// lDepOrdModel.setIdCssaComp(new BigDecimal("9999"));

		if (!isRequestParameterNullObj(CAMPO_COD_NATURA_PROVVEDIMENTO))
			lDepSenModel.setCodNaturaProvvedimento(getRequestStringParameter(CAMPO_COD_NATURA_PROVVEDIMENTO));

		if (!isRequestParameterNullObj(ICostantiDepositoOrdinanzaPc.FILTRO_RIMESSIONE)) {
			filtroRimessione = getRequestStringParameter(ICostantiDepositoOrdinanzaPc.FILTRO_RIMESSIONE);
			if (filtroRimessione.compareToIgnoreCase("cortecostituzionale") == 0)
				filtroRimessioneDescr = "alla Corte Costituzionale per giudizio di legittimità costituzionale";
			if (filtroRimessione.compareToIgnoreCase("corteeuropea") == 0)
				filtroRimessioneDescr = "alla Corte Giustizia Europea per giudizio di legittimità in materia di interpretazione trattati internazionali";

			if ((filtroRimessione.compareToIgnoreCase("altro") == 0)
					&& !isRequestParameterNullObj(ICostantiDepositoOrdinanzaPc.CAMPO_ALTRO_RIMESSIONI)) {
				filtroRimessioneDescr = "a "
						+ getRequestStringParameter(ICostantiDepositoOrdinanzaPc.CAMPO_ALTRO_RIMESSIONI);
			}
		}

		lDepSenModel.setOggettoProcedimento(filtroRimessioneDescr);

		// Aggiornamento Sentenza effettuato
		aModel.setSentenza(lDepSenModel);

		// /////////////////////////////////////////////////
		// Aggiornamento del template in Evento. ///
		// /////////////////////////////////////////////////
		aModel.getEvento().setTemIdTemplate(ICostantiDepositoSentenza.TEMPLATE_MOD_SENTENZA_RIMESSIONE_ATTI);

		return aModel;
	}

	public SentenzaEventoTenoriGProcModel inserimento(SentenzaEventoTenoriGProcModel aSenEveTenGP)
			throws F3BException {

		SentenzaEventoTenoriGProcModel lObjRet = null;
		IDepositoSentenza IDepSenCtrl = SIUSLookupRemote.getDepositoSentenzaRemote();

		// Variazione Fascicolo SIUS Origine
		if (!isRequestParameterNullObj(ICostantiFascicoloSius.CAMPO_ID_FASCICOLO_SIUS_ORIGINE)) {
			BigDecimal lIdFascOrigineNew = getRequestBigDecimalParameter(
					ICostantiFascicoloSius.CAMPO_ID_FASCICOLO_SIUS_ORIGINE);
			BigDecimal lIdFascOrigineOld = lFasGPMod.getFascicoloSiusModel().getIdFascicoloSiusOrigine();
			// Aggiornamento ID Fascicolo Origine solo se variato
			if (lIdFascOrigineOld == null || lIdFascOrigineNew.compareTo(lIdFascOrigineOld) != 0) {
				lObjRet = IDepSenCtrl.ExInserisciSentenza(aSenEveTenGP, lIdFascOrigineNew);
			} else {
				lObjRet = IDepSenCtrl.ExInserisciSentenza(aSenEveTenGP);
			}
		} else if (aSenEveTenGP.getTenori() != null)
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug(
					"Test sui tenori aSenEveTenGP.getTenori().length " + aSenEveTenGP.getTenori().length);
		else
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Test sui tenori aSenEveTenGP.getTenori() NULL");

		lObjRet = IDepSenCtrl.ExInserisciSentenza(aSenEveTenGP);

		return lObjRet;
	}

}