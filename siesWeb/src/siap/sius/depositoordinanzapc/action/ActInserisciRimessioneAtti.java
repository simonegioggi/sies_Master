package siap.sius.depositoordinanzapc.action;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.StringTokenizer;
import java.util.Vector;

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
import siap.sico.magistrato.controller.IMagistrato;
import siap.sico.magistrato.model.MagistratoModel;
import siap.sico.util.SICOLookupRemote;
import siap.siep.autoritaesterna.model.AutoritaEsternaModel;
import siap.siep.notifica.controller.INotifica;
import siap.siep.notifica.model.NotificaModel;
import siap.siep.util.SIEPLookupRemote;
import siap.sius.ActionSius;
import siap.sius.SIUSException;
import siap.sius.depositoordinanzapc.controller.IDepositoOrdinanzaPc;
import siap.sius.depositoordinanzapc.model.DepositoOrdinanzaPcModel;
import siap.sius.depositoordinanzapc.model.OrdinanzaEventoTenoriGProcModel;
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
 * Description: Classe Action per la load inserisci di Emissione Ordinanza Rimessione Atti
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: Bull
 * </p>
 *
 * @version 1.0
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class ActInserisciRimessioneAtti extends ActionSius implements ICostantiDepositoOrdinanzaPc {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
	public String mRetPage = IWebConstants.PG_MESSAGE; // pagina di view

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

		// Si richiama il lock per evitare inserimenti multipli per evitare inserimenti multipli
		// lockApplicativo("EmissioneProvvedimento");

		// Switch tipo decreto. Anticipato per effettuare il controllo
		String lCodTipoDec = RIMESSIONE_ATTI;

		// Preleva data di emissione
		Date lDataEmissione = getRequestDateParameter(ICostantiDepositoOrdinanzaPc.CAMPO_ANNO_DATA_EMISSIONE,
				ICostantiDepositoOrdinanzaPc.CAMPO_MESE_DATA_EMISSIONE,
				ICostantiDepositoOrdinanzaPc.CAMPO_GIORNO_DATA_EMISSIONE);

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("Data Emissione :" + lDataEmissione);

		// Ricerca del Magistrato Relatore
		IMagistratoRelatore lMagCtrl = SIUSLookupRemote.getMagistratoRelatoreRemote();
		MagistratoRelatoreModel lMagRel = lMagCtrl
				.ExRicercaEstesaMagRelByFascicolo(lFasGPMod.getFascicoloSiusModel().getIdFascicoloSius());

		// MEV_65: Punto 1.13 se magistrato è scaduto impossibile inserire decreto od ordinanza
		if (lMagRel != null && lMagRel.getMagistrato() != null) {
			IMagistrato im = SICOLookupRemote.getMagistratoRemote();
			MagistratoModel mm = new MagistratoModel();
			mm.setCodMagistrato(lMagRel.getMagistrato().getCodMagistrato());
			mm.setCodUfficioAppartenenza(getCodUfficioUtenteConnesso());
			mm.setCognome(lMagRel.getMagistrato().getCognome());
			mm.setNome(lMagRel.getMagistrato().getNome());
			Vector v = im.ExRicercaMagistrato(mm);
			if (!v.isEmpty()) {
				MagistratoModel mag = (MagistratoModel) v.get(0);
				if (mag.getDataFineValidita() != null
						&& (DateUtils.isLower(mag.getDataFineValidita(), DateUtils.getSysDate())
								|| DateUtils.isEquals(mag.getDataFineValidita(), DateUtils.getSysDate())))
					throw new SIUSException(SIUSException.USER_MESSAGE,
							"Attenzione! Impossibile emettere il provvedimento. Assegnatario del procedimento è un magistrato non più in servizio!");
			}
		}

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
			lTenModel.setCodEsitoTenore("0605");
			// 12/11/2003 Aggiunta la valorizzazione dell'eventuale Dettaglio Oggetto.
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

			// lEsiti[lIndex] = getEsito(lTenModel.getCodOggettoTenore());
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

		/*********** MICHELE 7/8/2009 ***************************************/
		try {
			OrdinanzaEventoTenoriGProcModel lOrdEveTenGP = new OrdinanzaEventoTenoriGProcModel();
			lOrdEveTenGP.setEvento(generaEvento(lDataEmissione));
			lOrdEveTenGP.setOrdinanza(generaOrdinanza(lDataEmissione));
			// lOrdEveTenGP.setTenori(generaTenori());
			lOrdEveTenGP.setTenori(lTenori);
			lOrdEveTenGP.setGeneraleProcedimento(generaProcedimento());
			// Lettura di ulteriori dati di rimessione atti
			lOrdEveTenGP = generaDati(lOrdEveTenGP);

			// Inserimento
			lOrdEveTenGP = inserimento(lOrdEveTenGP);

			// inserimento delle notifiche
			ArrayList listaNotifiche = null;

			listaNotifiche = generaNotifiche(lDataEmissione, lOrdEveTenGP.getEvento().getIdEvento());

			// Chiamata al Controller per inserimento NOTIFICA
			INotifica lCtrlNot = SIEPLookupRemote.getNotificaRemote();
			listaNotifiche = lCtrlNot.ExInserisciNotifiche(listaNotifiche);

			// aggiornamento dei dati in sessione
			IFascicoloSius lFasCtrl = SIUSLookupRemote.getFascicoloSiusRemote();
			lFasGPMod = lFasCtrl
					.ExRicercaFascicoloByKey(lFasGPMod.getFascicoloSiusModel().getIdFascicoloSius());
			setSessionAttribute("fascicoloSiusGP", lFasGPMod);

			// pagina dettaglio dell'ordinanza
			RedirectTo lRedirectTo = new RedirectTo();
			lRedirectTo.setPage(IWebConstants.PG_MAIN);
			lRedirectTo.setAction("siap.sius.depositoordinanzapc.action.ActLoadDettaglioOrdinanza");
			lRedirectTo.setParameter(ICostantiEvento.CAMPO_ID_EVENTO,
					lOrdEveTenGP.getEvento().getIdEvento().toString());
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
		/***************************************************/

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
		siesLogger.debug("N° Oggetti = " + lOggetti.size());
		// lettura tipo di ordinanza Sempre automatica 13-9-04
		String lCodTipoDec = null;
		// lCodTipoDec = DecodificheUtils.getCodAltebyCode(lOggetti, lCodContenuto);
		lCodTipoDec = RIMESSIONE_ATTI;
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("cod Tipo Ordinanza = " + lCodTipoDec);

		// per l'ordinanza di conversione serve trovare il fascicolo origine
		// anche detto precedimento collegato
		// if (getSessionAttribute("fascicoloSiusGP") != null) {
		// FascicoloGPModel lFasGPMod = new FascicoloGPModel(
		// (FascicoloGPModel) getSessionAttribute("fascicoloSiusGP"));
		// BigDecimal lIdFascicoloSiusOrigine = lFasGPMod.getFascicoloSiusModel()
		// .getIdFascicoloSiusOrigine();
		// }
		// if (lCodTipoDec == null)
		// throw new SIUSException(SIUSException.USER_MESSAGE, "Tipo ordinanza automatico non definito");

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("cod Tipo ordinanza decodificata" + lCodTipoDec);
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
	 * Prepara con i dati il Model per il Deposito Ordinanza.
	 * <p>
	 *
	 * @param aDataEmissione
	 *            Date data di emissione
	 * @throws F3BException
	 *             propaga errori di eccezione.
	 * @return DepositoOrdinanzaPcModel riotrna istanza del model opportunemnte popolato.
	 */
	private DepositoOrdinanzaPcModel generaOrdinanza(Date aDataEmissione) throws F3BException {
		// Prepara il model DepositoOrdinanza.
		DepositoOrdinanzaPcModel lDepOrdModel = new DepositoOrdinanzaPcModel();
		lDepOrdModel.setCodTipoOrdinanza(RIMESSIONE_ATTI);
		lDepOrdModel.setCodMagistrato(mCodMagistrato);
		lDepOrdModel.setGenPridGeneraleProcedimento(mIdGenProc);
		lDepOrdModel.setCodOperatoreInserimento(mCodiceOperatore);
		lDepOrdModel.setCodUfficioInserimento(mCodiceUfficio);
		lDepOrdModel.setDataInserimento(mOggi);
		lDepOrdModel.setDataCameraConsiglio(aDataEmissione);

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("DepositoOrdinanza = " + lDepOrdModel);
		return lDepOrdModel;
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
		lEvento.setCodTipoProvvedimento("03"); // 03 = Ordinanza.
		lEvento.setCodLuogoEmittente(mCodiceComune);
		lEvento.setCodUfficioEmittente(mCodiceUfficio);
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

		String lSediAvvocato[] = new String[0];
		if (!isRequestParameterNullObj(ICostantiRichiestaAtti.CAMPO_SEDE_AVVOCATO))
			lSediAvvocato = getRequestStringParameters(ICostantiRichiestaAtti.CAMPO_SEDE_AVVOCATO);

		String lDestinatariAvvocato[] = new String[0];
		if (!isRequestParameterNullObj(ICostantiRichiestaAtti.CAMPO_COD_AVVOCATO_DESTINATARIO))
			lDestinatariAvvocato = getRequestStringParameters(
					ICostantiRichiestaAtti.CAMPO_COD_AVVOCATO_DESTINATARIO);

		String lAvvocato[] = new String[0];
		if (!isRequestParameterNullObj(ICostantiUdienza.CAMPO_COD_AVVOCATO))
			lAvvocato = getRequestStringParameters(ICostantiUdienza.CAMPO_COD_AVVOCATO);

		String[] lIndirizziAvvocato = new String[0];
		if (!isRequestParameterNullObj(ICostantiRichiestaAtti.CAMPO_INDIRIZZO_AVVOCATO))
			lIndirizziAvvocato = getRequestStringParameters(ICostantiRichiestaAtti.CAMPO_INDIRIZZO_AVVOCATO);

		// String lViaFax[] = new String[0];
		// if (! isRequestParameterNullObj( ICostantiRichiestaAtti.CAMPO_NOTIFICHE_VIA_FAX))
		// lViaFax = getRequestStringParameter( ICostantiRichiestaAtti.CAMPO_NOTIFICHE_VIA_FAX).split(",");

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

					BigDecimal flagNotificaViaFax = null;
					// try {
					// flagNotificaViaFax = new BigDecimal (lViaFax[x]);
					// } catch (Exception e) {
					// e.printStackTrace();
					// flagNotificaViaFax=null;
					// }

					lNot.setFlagNotificaViaFax(flagNotificaViaFax);

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
	private OrdinanzaEventoTenoriGProcModel generaDati(OrdinanzaEventoTenoriGProcModel aModel)
			throws F3BException {
		// /////////////////////////////////////////////////
		// Aggiornamento dei dati in DepositoOrdinanza. //
		// ///////////////////////////////////////////////
		DepositoOrdinanzaPcModel lDepOrdModel = aModel.getOrdinanza();

		String filtroRimessione = "";
		String filtroRimessioneDescr = "";

		lDepOrdModel.setCodNaturaProvvedimento("-");
		lDepOrdModel.setIdCssaComp(new BigDecimal("9999"));

		if (!isRequestParameterNullObj(CAMPO_COD_NATURA_PROVVEDIMENTO))
			lDepOrdModel.setCodNaturaProvvedimento(getRequestStringParameter(CAMPO_COD_NATURA_PROVVEDIMENTO));

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

		lDepOrdModel.setOggettoProcedimento(filtroRimessioneDescr);

		// Aggiornamento Ordinanza effettuato
		aModel.setOrdinanza(lDepOrdModel);

		// /////////////////////////////////////////////////
		// Aggiornamento del template in Evento. //
		aModel.getEvento().setTemIdTemplate(TEMPLATE_MOD_ORDINANZA_RIMESSIONE_ATTI);

		// ///////////////////////////////////////////////
		if (!isRequestParameterNullObj(CAMPO_TIPO_ORDINANZA_DA_PRODURRE)) {
			EventoModel lEvento = aModel.getEvento();
			lEvento.setTemIdTemplate(TEMPLATE_MOD_ORDINANZA_RIMESSIONE_ATTI);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Evento  >>> " + lEvento);

			// Aggiornamento Evento effettuato
			aModel.setEvento(lEvento);
		}

		return aModel;
	}

	public OrdinanzaEventoTenoriGProcModel inserimento(OrdinanzaEventoTenoriGProcModel aOrdEveTenGP)
			throws F3BException {
		OrdinanzaEventoTenoriGProcModel lObjRet = null;
		IDepositoOrdinanzaPc IDepOrdCtrl = SIUSLookupRemote.getDepositoOrdinanzaPcRemote();

		// Variazione Fascicolo SIUS Origine
		if (!isRequestParameterNullObj(ICostantiFascicoloSius.CAMPO_ID_FASCICOLO_SIUS_ORIGINE)) {
			BigDecimal lIdFascOrigineNew = getRequestBigDecimalParameter(
					ICostantiFascicoloSius.CAMPO_ID_FASCICOLO_SIUS_ORIGINE);
			BigDecimal lIdFascOrigineOld = lFasGPMod.getFascicoloSiusModel().getIdFascicoloSiusOrigine();
			// Aggiornamento ID Fascicolo Origine solo se variato
			if (lIdFascOrigineOld == null || lIdFascOrigineNew.compareTo(lIdFascOrigineOld) != 0) {
				lObjRet = IDepOrdCtrl.ExInserisciOrdinanza(aOrdEveTenGP, lIdFascOrigineNew);
			} else {
				lObjRet = IDepOrdCtrl.ExInserisciOrdinanza(aOrdEveTenGP);
			}
		} else if (aOrdEveTenGP.getTenori() != null)
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug(
					"Test sui tenori aOrdEveTenGP.getTenori().length " + aOrdEveTenGP.getTenori().length);
		else
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Test sui tenori aOrdEveTenGP.getTenori() NULL");
		lObjRet = IDepOrdCtrl.ExInserisciOrdinanza(aOrdEveTenGP);

		return lObjRet;
	}

}