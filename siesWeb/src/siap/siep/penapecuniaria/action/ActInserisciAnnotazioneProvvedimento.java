package siap.siep.penapecuniaria.action;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;

import org.apache.log4j.Logger;

import siap.sico.decodifiche.model.ComuneModel;
import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.model.EventoModel;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.utente.model.DatiOperazioneModel;
import siap.sico.web.ActionSiap;
import siap.siep.autoritaesterna.model.AutoritaEsternaModel;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.notifica.action.ICostantiNotifica;
import siap.siep.notifica.model.NotificaModel;
import siap.siep.penapecuniaria.controller.IRichiestaConversione;
import siap.siep.penapecuniaria.model.RichiestaConversioneModel;
import siap.siep.penaresidua.controller.IPenaResidua;
import siap.siep.penaresidua.model.PenaResiduaModel;
import siap.siep.scambiosanzione.action.ICostantiScambioSanzione;
import siap.siep.scambiosanzione.controller.IScambioSanzione;
import siap.siep.scambiosanzione.model.ScambioSanzioneModel;
import siap.siep.util.SIEPLookupRemote;
import siap.sius.depositodecreto.model.DepositoDecretoModel;
import siap.sius.depositoordinanzapc.model.DepositoOrdinanzaPcModel;
import siap.sius.tenore.model.TenoreModel;
import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;

/**
 * <p>
 * Title: ActInserisciAnnotazioneProvvedimento
 * </p>
 * <p>
 * Description: Classe Action per l'inserimento di una Annotazione Provvedimento Sanzione Sostitutiva viene
 * variato lo stato di esecuzione
 * </p>
 * <p>
 * Copyright: Copyright (c) 2008
 * </p>
 * <p>
 * Company: Eutelia
 * </p>
 * 
 * @version 3.0
 */
@SuppressWarnings({"rawtypes","unchecked"})
public class ActInserisciAnnotazioneProvvedimento extends ActionSiap implements ICostantiPenaPecuniaria,
		ICostantiNotifica, ICostantiScambioSanzione {
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public String processRequest() throws F3BException {

		FascicoloSiepModel lFascMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");

		// Prendo la Tab. Scambiosanzione che mi arriva dal SIUS, e a seconda di quello che c'è
		// scriverò un evento
		ScambioSanzioneModel lScSanzioneMod = null;
		RichiestaConversioneModel lRichConvModel = null;

		EventoModel lEventoSIUS = null;
		TenoreModel lTenoreModel = null;
		DepositoDecretoModel lDepDecMod = null;
		DepositoOrdinanzaPcModel lDepOrdMod = null;

		// ============================================================
		DatiOperazioneModel lDatiOpModel = new DatiOperazioneModel();

		lDatiOpModel.setCodOperatore(getCodUtenteConnesso());
		lDatiOpModel.setCodUfficio(getCodUfficioUtenteConnesso());
		lDatiOpModel.setData(DateUtils.getSysDate());

		BigDecimal idScSanzione = getRequestBigDecimalParameter(ICostantiScambioSanzione.CAMPO_ID_SCAMBIO_SANZIONE);
		if (idScSanzione != null) {
			// Elemento selezionato dalla lista, recupero i dati iscritti da SIUS
			IScambioSanzione lCtrl = SIEPLookupRemote.getScambioSanzionRemote();
			lScSanzioneMod = lCtrl.ExRicercaScambioSanzioneById(idScSanzione);
		} else {
			// Dati digitati dall'utente
			lScSanzioneMod = recuperaScambioSanzione(lDatiOpModel);
			lEventoSIUS = generaEventoSIUS(lDatiOpModel);

			// Tenore
			lTenoreModel = this.setTenore(lEventoSIUS, lDatiOpModel);

			// Deposito Decreto/Deposito OrdinanzaPC
			if ("02".equals(lEventoSIUS.getCodTipoProvvedimento())) {
				lDepDecMod = setDepositoDecreto(lEventoSIUS, lDatiOpModel);
			} else {
				lDepOrdMod = setDepositoOrdinanzaPc(lEventoSIUS, lDatiOpModel);
			}

			lRichConvModel = recuperaRichiestaConversione();
		}

		// evento Notifica
		EventoNotificaModel lEveMod = new EventoNotificaModel();

		lEveMod.getEvento().setFasSieIdFascicoloSiep(lFascMod.getIdFascicoloSiep());
		lEveMod.getEvento().setCodTipoEvento("01");
		lEveMod.getEvento().setCodTipoProvvedimento("12");
		lEveMod.getEvento().setCodMotivo(lScSanzioneMod.getCodTipoSanzione());
		lEveMod.getEvento().setCodEsito(lScSanzioneMod.getCodNaturaSanzione());
		lEveMod.getEvento().setEveIdEvento(lScSanzioneMod.getEveIdEvento());

		if (!this.isRequestParameterNullObj(ICostantiEvento.CAMPO_GIORNO_DATA_RICEZIONE_ATTI))
			lEveMod.getEvento().setDataRicezioneAtti(
					getRequestDateParameter(ICostantiEvento.CAMPO_ANNO_DATA_RICEZIONE_ATTI,
							ICostantiEvento.CAMPO_MESE_DATA_RICEZIONE_ATTI,
							ICostantiEvento.CAMPO_GIORNO_DATA_RICEZIONE_ATTI));

		if (!this.isRequestParameterNullObj(ICostantiScambioSanzione.CAMPO_ANNO_REGISTRO)) {
			lEveMod.getEvento().setAnnoProtocollo(
					getRequestBigDecimalParameter(ICostantiScambioSanzione.CAMPO_ANNO_REGISTRO));
			lEveMod.getEvento().setProgrProtocollo(
					getRequestBigDecimalParameter(ICostantiScambioSanzione.CAMPO_NUMERO_REGISTRO));
		}

		lEveMod.getEvento().setCodLuogoEmittente(getUfficioUtenteConnesso().getCodComune());
		lEveMod.getEvento().setCodUfficioEmittente(getUfficioUtenteConnesso().getCodUfficio());

		//
		// if (!this.isRequestParameterNullObj(ICostantiScambioSanzione.CAMPO_GIORNO_DATA_EMISSIONE))
		// lEveMod.getEvento().setDataEmissione(getRequestDateParameter(ICostantiScambioSanzione.CAMPO_ANNO_DATA_EMISSIONE
		// , ICostantiScambioSanzione.CAMPO_MESE_DATA_EMISSIONE ,
		// ICostantiScambioSanzione.CAMPO_GIORNO_DATA_EMISSIONE ));
		// FIXME CPP perchè come data emissione prende quella SIUS???
		// if (lScSanzioneMod.getDataEmissione()!=null)
		// lEveMod.getEvento().setDataEmissione(lScSanzioneMod.getDataEmissione());

		if (!this.isRequestParameterNullObj(ICostantiEvento.CAMPO_GIORNO_DATA_RICEZIONE_ATTI))
			lEveMod.getEvento().setDataEmissione(
					getRequestDateParameter(ICostantiEvento.CAMPO_ANNO_DATA_RICEZIONE_ATTI,
							ICostantiEvento.CAMPO_MESE_DATA_RICEZIONE_ATTI,
							ICostantiEvento.CAMPO_GIORNO_DATA_RICEZIONE_ATTI));

		lEveMod.getEvento().setCodLuogoDestinatario("-");
		lEveMod.getEvento().setCodUfficioDestinatario("-");
		lEveMod.getEvento().setCodTipoUfficioDestinatario("-");

		lEveMod.getEvento().setCodOperatoreInserimento(lDatiOpModel.getCodOperatore());
		lEveMod.getEvento().setDataInserimento(lDatiOpModel.getData());
		lEveMod.getEvento().setCodUfficioInserimento(lDatiOpModel.getCodUfficio());

		lEveMod.getEvento().setFlagStampaSiep("S");
		lEveMod.getEvento().setFlagVideoSiep("S");
		lEveMod.getEvento().setCodMagistrato("-");

		// preparo la parte Notifica
		ArrayList lNotifiche = new ArrayList();

		// Check Invio Comunicazione
		String Ca = (getRequestStringParameter(ICostantiEvento.CAMPO_FLAG_PIU_MENO));

		if (Ca.equals("0")) // SI
		{
			// Ufficio competente
			NotificaModel lNotModel = new NotificaModel();

			lNotModel.setCodOperatoreInserimento(this.getCodUtenteConnesso());
			lNotModel.setCodUfficioInserimento(this.getCodUfficioUtenteConnesso());
			lNotModel.setDataInserimento(DateUtils.getSysDate());
			lNotModel.setCodTipoNotifica("N");
			lNotModel.setDataInvio(DateUtils.getSysDate());
			lNotModel.setCodEsito("-");

			AutoritaEsternaModel lAut = new AutoritaEsternaModel();

			String lPolizia = this.getRequestStringParameter(ICostantiEvento.CAMPO_COD_UFFICIO_DESTINATARIO);
			String lSedePolizia = this
					.getRequestStringParameter(ICostantiEvento.CAMPO_COD_LUOGO_DESTINATARIO);
			ComuneModel lComModel = new ComuneModel(getCodComuneByDescrFlagVal(lSedePolizia));

			lAut.setCodTipoAutorita(lPolizia);
			lAut.setCodSede(lComModel.getCodComune());

			lAut.setCodOperatoreInserimento(this.getCodUtenteConnesso());
			lAut.setCodUfficioInserimento(this.getCodUfficioUtenteConnesso());
			lAut.setDataInserimento(DateUtils.getSysDate());

			lNotModel.setAutoritaEsterna(lAut);
			lNotifiche.add(lNotModel);

			// Altro destinatario
			// n.b. è il campo Altro destinatario
			String CmpNt = (getRequestStringParameter(ICostantiNotifica.CAMPO_NOTE));
			if (!CmpNt.equals("")) {
				NotificaModel lNotModelAD = new NotificaModel();

				lNotModelAD.setCodTipoNotifica("N");
				lNotModelAD.setNote(getRequestStringParameter(ICostantiNotifica.CAMPO_NOTE));
				lNotModelAD.setCodEsito("-");
				lNotModelAD.setDataInvio(DateUtils.getSysDate());

				lNotModelAD.setCodOperatoreInserimento(this.getCodUtenteConnesso());
				lNotModelAD.setDescrUfficioInserimento(this.getCodUfficioUtenteConnesso());
				lNotModelAD.setDataInserimento(DateUtils.getSysDate());

				lNotifiche.add(lNotModelAD);
			}
		}

		lEveMod.setNotifiche((NotificaModel[]) lNotifiche.toArray(new NotificaModel[0]));

		// Controllo Esistenza pena residua per quel fascicolo (?)
		PenaResiduaModel lPenaResMod = new PenaResiduaModel();
		IPenaResidua lPenResCtrl = SIEPLookupRemote.getPenaResiduaRemote();
		lPenaResMod = lPenResCtrl.ExRicercaPenaResiduaCorrenteByFascicoloSiep(lFascMod.getIdFascicoloSiep());

		// Inserisce l'evento e le notifiche
		// IEvento lCtrlEve = SICOLookupRemote.getEventoRemote();
		// lEveMod = lCtrlEve.ExInserisciEventoNotifica(lEveMod, lPenaResMod);

		// Inserisce l'evento e le notifiche
		IRichiestaConversione lCtrlRichConv = SIEPLookupRemote.getRichiestaConversioneRemote();
		lEveMod = lCtrlRichConv.ExInserisciDecisioneSorveglianza(lEveMod, lEventoSIUS, lRichConvModel,
				lScSanzioneMod, lTenoreModel, lDepDecMod, lDepOrdMod, lPenaResMod);

		String lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
				+ "=siap.siep.penapecuniaria.action.ActDettaglioAnnotazioneProvvedimento&"
				+ ICostantiEvento.CAMPO_ID_EVENTO + "=" + lEveMod.getEvento().getIdEvento();

		return lPage;
	}

	/**
	 * Recupera i dati scambio sanzione direttamente dalla form se digitati dall'utente
	 * 
	 * @return
	 */
	private ScambioSanzioneModel recuperaScambioSanzione(DatiOperazioneModel aDatiOpModel)
			throws F3BException {
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
		siesLogger.debug("recuperaScambioSanzione dalla form ");

		ScambioSanzioneModel lScambioSanzModel = new ScambioSanzioneModel();

		lScambioSanzModel
				.setCodTipoDecisione(getRequestStringParameter(ICostantiScambioSanzione.CAMPO_COD_TIPO_DECISIONE));
		lScambioSanzModel
				.setCodNaturaSanzione(getRequestStringParameter(ICostantiScambioSanzione.CAMPO_COD_NATURA_SANZIONE));
		lScambioSanzModel
				.setCodTipoSanzione(getRequestStringParameter(ICostantiScambioSanzione.CAMPO_COD_TIPO_SANZIONE));

		// lScambioSanzModel.setDataInizio(aValore);
		// lScambioSanzModel.setDataFine(aValore);

		// FIXME CPP sembra non valorizzato da SIUS
		lScambioSanzModel.setNote(getRequestStringParameter(ICostantiScambioSanzione.CAMPO_NOTE_ANN));

		lScambioSanzModel
				.setAnnoRegistro(getRequestBigDecimalParameter(ICostantiScambioSanzione.CAMPO_ANNO_REGISTRO));
		lScambioSanzModel
				.setNumeroRegistro(getRequestBigDecimalParameter(ICostantiScambioSanzione.CAMPO_NUMERO_REGISTRO));

		lScambioSanzModel
				.setChiaveAnnoFascicoloSius(getRequestBigDecimalParameter(ICostantiScambioSanzione.CAMPO_CHIAVE_ANNO_FASCICOLO_SIUS));
		lScambioSanzModel
				.setChiaveProgrFascicoloSius(getRequestBigDecimalParameter(ICostantiScambioSanzione.CAMPO_CHIAVE_PROGR_FASCICOLO_SIUS));

		// COD_UFFICIO_EMITTENTE
		String lTipoUffEmitt = (getRequestStringParameter(ICostantiScambioSanzione.CAMPO_COD_UFFICIO_EMITTENTE));
		String lSedeUffEmitt = (getRequestStringParameter(ICostantiScambioSanzione.CAMPO_COD_SEDE_AUTORITA_EMITTENTE));

		String lCodUffEmittente = this.getCodUfficioByCodTipoUfficioDescrComune(lTipoUffEmitt, lSedeUffEmitt);

		lScambioSanzModel.setCodUfficioEmittente(lCodUffEmittente);
		lScambioSanzModel.setCodUfficioSorveglianza(lCodUffEmittente);

		// DATA_EMISSIONE
		Date lDataEmissione = getRequestDateParameter(ICostantiScambioSanzione.CAMPO_ANNO_DATA_EMISSIONE,
				ICostantiScambioSanzione.CAMPO_MESE_DATA_EMISSIONE,
				ICostantiScambioSanzione.CAMPO_GIORNO_DATA_EMISSIONE);
		lScambioSanzModel.setDataEmissione(lDataEmissione);

		FascicoloSiepModel lFascMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");
		lScambioSanzModel.setFasSieIdFascicoloSiep(lFascMod.getIdFascicoloSiep());

		lScambioSanzModel.setCodOperatoreInserimento(aDatiOpModel.getCodOperatore());
		lScambioSanzModel.setCodUfficioInserimento(aDatiOpModel.getCodUfficio());
		lScambioSanzModel.setDataInserimento(aDatiOpModel.getData());

		// lScambioSanzModel.setEveIdEvento(aValore); // Da collegare in fase di inserimento all'evento
		// Ordinanza simulato

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
		siesLogger.debug("lScambioSanzModel = " + lScambioSanzModel);

		return lScambioSanzModel;
	}

	/**
	 * Simula l'evento SIUS di conversione nel caso di dati inseriti direttamente dall'utente SIEP. In questo
	 * modo l'ordinanza di conversione può uscire dall'elenco dei provvedimenti della sorveglianza.
	 * 
	 * @return
	 * @throws F3BException
	 */
	private EventoModel generaEventoSIUS(DatiOperazioneModel aDatiOpModel) throws F3BException {
		EventoModel lEveSIUS = new EventoModel();

		lEveSIUS.setCodTipoEvento("01");
		lEveSIUS.setCodTipoProvvedimento(getRequestStringParameter(ICostantiScambioSanzione.CAMPO_COD_TIPO_DECISIONE));
		lEveSIUS.setCodMotivo(getRequestStringParameter(ICostantiScambioSanzione.CAMPO_COD_TIPO_SANZIONE));

		lEveSIUS.setCodEsito(getRequestStringParameter(ICostantiScambioSanzione.CAMPO_COD_NATURA_SANZIONE));

		// COD_UFFICIO_EMITTENTE
		String lTipoUffEmitt = (getRequestStringParameter(ICostantiScambioSanzione.CAMPO_COD_UFFICIO_EMITTENTE));
		String lSedeUffEmitt = (getRequestStringParameter(ICostantiScambioSanzione.CAMPO_COD_SEDE_AUTORITA_EMITTENTE));

		String lCodUffEmittente = this.getCodUfficioByCodTipoUfficioDescrComune(lTipoUffEmitt, lSedeUffEmitt);
		UfficioModel lUffEmitt = getUfficioByCodUfficio(lCodUffEmittente);

		// DATA_EMISSIONE
		Date lDataEmissione = getRequestDateParameter(ICostantiScambioSanzione.CAMPO_ANNO_DATA_EMISSIONE,
				ICostantiScambioSanzione.CAMPO_MESE_DATA_EMISSIONE,
				ICostantiScambioSanzione.CAMPO_GIORNO_DATA_EMISSIONE);

		lEveSIUS.setCodUfficioEmittente(lCodUffEmittente);
		lEveSIUS.setCodLuogoEmittente(lUffEmitt.getCodComune());
		lEveSIUS.setDataEmissione(lDataEmissione);

		FascicoloSiepModel lFascMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");
		lEveSIUS.setFasSieIdFascicoloSiep(lFascMod.getIdFascicoloSiep());

		// 27/07/2015 lEveSIUS.setFlagDocumentoRegistrato("S"); // Va inserito validato altrimenti il
		// dettaglio non recupera i dati
		lEveSIUS.setFlagDocumentoRegistrato("N");
		lEveSIUS.setFlagStampaSiep("S");
		lEveSIUS.setFlagVideoSiep("S");

		lEveSIUS.setCodOperatoreInserimento(aDatiOpModel.getCodOperatore());
		lEveSIUS.setCodUfficioInserimento(aDatiOpModel.getCodUfficio());
		lEveSIUS.setDataInserimento(aDatiOpModel.getData());

		lEveSIUS.setCodMagistrato("-");
		lEveSIUS.setCodUfficioDestinatario("-");
		lEveSIUS.setCodTipoUfficioDestinatario("-");
		lEveSIUS.setCodLuogoDestinatario("-");

		return lEveSIUS;

	}

	/**
	 * Recupera il record richiesta coversione ed eventualmente valorizza i campi con i dati digitati in form.
	 * 
	 * @return
	 * @throws F3BException
	 */
	private RichiestaConversioneModel recuperaRichiestaConversione() throws F3BException {

		RichiestaConversioneModel lRicConvModel = null;

		IRichiestaConversione lCtrlRic = SIEPLookupRemote.getRichiestaConversioneRemote();

		FascicoloSiepModel lFascMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");
		lRicConvModel = lCtrlRic
				.ExRicercaRichiestaConversioneByIdFascicoloSiep(lFascMod.getIdFascicoloSiep());

		if (lRicConvModel != null && lRicConvModel.getIdRichiestaConversione() != null) {

			String lCodEsito = getRequestStringParameter(ICostantiScambioSanzione.CAMPO_COD_NATURA_SANZIONE);

			if ("0156".equals(lCodEsito)) {
				// Libertà controllata
				lRicConvModel
						.setDurataEsitoAnni(getRequestBigDecimalParameter(ICostantiPenaPecuniaria.CAMPO_DURATA_ESITO_ANNI_LIB));
				lRicConvModel
						.setDurataEsitoMesi(getRequestBigDecimalParameter(ICostantiPenaPecuniaria.CAMPO_DURATA_ESITO_MESI_LIB));
				lRicConvModel
						.setDurataEsitoGiorni(getRequestBigDecimalParameter(ICostantiPenaPecuniaria.CAMPO_DURATA_ESITO_GIORNI_LIB));
				lRicConvModel.setCodTipoSanzione("01");
			} else if ("0157".equals(lCodEsito)) {
				// Lavoro Sostitutivo
				lRicConvModel
						.setDurataEsitoAnni(getRequestBigDecimalParameter(ICostantiPenaPecuniaria.CAMPO_DURATA_ESITO_ANNI_LAV));
				lRicConvModel
						.setDurataEsitoMesi(getRequestBigDecimalParameter(ICostantiPenaPecuniaria.CAMPO_DURATA_ESITO_MESI_LAV));
				lRicConvModel
						.setDurataEsitoGiorni(getRequestBigDecimalParameter(ICostantiPenaPecuniaria.CAMPO_DURATA_ESITO_GIORNI_LAV));
				lRicConvModel.setCodTipoSanzione("02");
			} else if ("0158".equals(lCodEsito)) {
				// Differimento
				lRicConvModel
						.setDurataEsitoAnni(getRequestBigDecimalParameter(ICostantiPenaPecuniaria.CAMPO_DURATA_ESITO_ANNI_DIFF));
				lRicConvModel
						.setDurataEsitoMesi(getRequestBigDecimalParameter(ICostantiPenaPecuniaria.CAMPO_DURATA_ESITO_MESI_DIFF));
				lRicConvModel
						.setDurataEsitoGiorni(getRequestBigDecimalParameter(ICostantiPenaPecuniaria.CAMPO_DURATA_ESITO_GIORNI_DIFF));
			} else if ("0159".equals(lCodEsito)) {
				// Rateizzazione
				lRicConvModel
						.setNumeroRate(getRequestBigDecimalParameter(ICostantiPenaPecuniaria.CAMPO_NUMERO_RATE));

				// Valore Rata
				String lParteIntera = getRequestStringParameter(ICostantiPenaPecuniaria.CAMPO_VALORE_RATA_I);
				String lParteDecimale = getRequestStringParameter(ICostantiPenaPecuniaria.CAMPO_VALORE_RATA_D);
				BigDecimal lImportRata = null;

				if (!lParteIntera.equals("")) {
					if (!lParteDecimale.equals("")) {
						lImportRata = new BigDecimal(lParteIntera + "." + lParteDecimale);
					} else
						lImportRata = new BigDecimal(lParteIntera);
				} else if (!lParteDecimale.equals("")) {
					lImportRata = new BigDecimal("0." + lParteDecimale);
				}

				lRicConvModel.setValoreRata(lImportRata);

				// Valore Ultima Rata
				lParteIntera = getRequestStringParameter(ICostantiPenaPecuniaria.CAMPO_VALORE_ULTIMA_RATA_I);
				lParteDecimale = getRequestStringParameter(ICostantiPenaPecuniaria.CAMPO_VALORE_ULTIMA_RATA_D);

				
				//Ticket#202112240110 - Valorizzava l'importo ultima rata anche se non indicato in form
				//                      in quanto utilizzava la stessa variabile "lImportRata" delle "prime rate"
//				if (!lParteIntera.equals("")) {
//					if (!lParteDecimale.equals("")) {
//						lImportRata = new BigDecimal(lParteIntera + "." + lParteDecimale);
//					} else
//						lImportRata = new BigDecimal(lParteIntera);
//				} else if (!lParteDecimale.equals("")) {
//					lImportRata = new BigDecimal("0." + lParteDecimale);
//				}
//
//				lRicConvModel.setValoreUltimaRata(lImportRata);

				
				BigDecimal lImportUltimaRata = null;
				if (!lParteIntera.equals("")) {
					if (!lParteDecimale.equals("")) {
						lImportUltimaRata = new BigDecimal(lParteIntera + "." + lParteDecimale);
					} else
						lImportUltimaRata = new BigDecimal(lParteIntera);
				} else if (!lParteDecimale.equals("")) {
					lImportUltimaRata = new BigDecimal("0." + lParteDecimale);
				}
				lRicConvModel.setValoreUltimaRata(lImportUltimaRata);
				//Ticket#202112240110 - FINE
				
				// Data Pagamento Prima Rata
				// Inizio 01/02/2016
				if (getRequestStringParameter(ICostantiPenaPecuniaria.CAMPO_GIORNO_DATA_INIZIO_PAGA).length() > 0) {
					//Ticket#202112240110 - Caricava la data prima rata dal campo sbagliato ovvero dalla data emissione
//					lRicConvModel.setDataInizioPagamento(getRequestDateParameter(
//							ICostantiScambioSanzione.CAMPO_ANNO_DATA_EMISSIONE,
//							ICostantiScambioSanzione.CAMPO_MESE_DATA_EMISSIONE,
//							ICostantiScambioSanzione.CAMPO_GIORNO_DATA_EMISSIONE));
					lRicConvModel.setDataInizioPagamento(getRequestDateParameter(
							ICostantiPenaPecuniaria.CAMPO_ANNO_DATA_INIZIO_PAGA,
							ICostantiPenaPecuniaria.CAMPO_MESE_DATA_INIZIO_PAGA,
							ICostantiPenaPecuniaria.CAMPO_GIORNO_DATA_INIZIO_PAGA));					
					//Ticket#202112240110 - Caricava la data prima rata dal campo sbagliato
					
				}
				// fine 01/02/2016

				//
				lRicConvModel
						.setNumeroGiorniInizioPagamento(getRequestBigDecimalParameter(ICostantiPenaPecuniaria.CAMPO_GIORNI_INIZIO_PAGA));
			} else {
				// Devo comunque passare la Richiesta tal quale perchè il controller
				// deve agganciare Rich Conv al provvedimento SIUS
			}
		}

		return lRicConvModel;
	}

	/**
	 * Prepara l'oggetto TenoreModel
	 * 
	 * @param aEventoSIUS
	 * @param aDatiOpModel
	 * @return
	 * @throws F3BException
	 */
	private TenoreModel setTenore(EventoModel aEventoSIUS, DatiOperazioneModel aDatiOpModel)
			throws F3BException {
		TenoreModel lTenMod = new TenoreModel();

		lTenMod.setCodEsitoTenore(aEventoSIUS.getCodEsito());

		lTenMod.setData(aEventoSIUS.getDataEmissione());

		lTenMod.setCodOggettoTenore(aEventoSIUS.getCodMotivo());
		lTenMod.setProgrTenore(new BigDecimal(1));

		lTenMod.setCodUfficioInserimento(aDatiOpModel.getCodUfficio());
		lTenMod.setCodOperatoreInserimento(aDatiOpModel.getCodOperatore());
		lTenMod.setDataInserimento(aDatiOpModel.getData());

		return lTenMod;
	}

	/**
	 * Prepara l'oggetto DepositoDecretoModel
	 * 
	 * @param aEventoSIUS
	 * @param aDatiOpModel
	 * @return
	 * @throws F3BException
	 */
	private DepositoDecretoModel setDepositoDecreto(EventoModel aEventoSIUS, DatiOperazioneModel aDatiOpModel)
			throws F3BException {
		DepositoDecretoModel lDepDecMod = new DepositoDecretoModel();

		lDepDecMod.setAnnoS72(getRequestBigDecimalParameter(ICostantiScambioSanzione.CAMPO_ANNO_REGISTRO));
		lDepDecMod.setNumS72(getRequestBigDecimalParameter(ICostantiScambioSanzione.CAMPO_NUMERO_REGISTRO));

		lDepDecMod.setDataEmissione(aEventoSIUS.getDataEmissione());

		lDepDecMod.setCodUfficioCompetente(aEventoSIUS.getCodUfficioEmittente());

		lDepDecMod.setCodUfficioInserimento(aDatiOpModel.getCodUfficio());
		lDepDecMod.setCodOperatoreInserimento(aDatiOpModel.getCodOperatore());
		lDepDecMod.setDataInserimento(aDatiOpModel.getData());

		return lDepDecMod;
	}

	/**
	 * Prepara l'oggetto DepositoOrdinanzaPcModel
	 * 
	 * @param aEventoSIUS
	 * @param aDatiOpModel
	 * @return
	 * @throws F3BException
	 */
	private DepositoOrdinanzaPcModel setDepositoOrdinanzaPc(EventoModel aEventoSIUS,
			DatiOperazioneModel aDatiOpModel) throws F3BException {

		DepositoOrdinanzaPcModel lDepOrdMod = new DepositoOrdinanzaPcModel();

		lDepOrdMod.setAnnoS3(getRequestBigDecimalParameter(ICostantiScambioSanzione.CAMPO_ANNO_REGISTRO));
		lDepOrdMod.setNumS3(getRequestBigDecimalParameter(ICostantiScambioSanzione.CAMPO_NUMERO_REGISTRO));

		lDepOrdMod.setCodTipoOrdinanza("CP");

		lDepOrdMod.setDataUdienza(aEventoSIUS.getDataEmissione());

		lDepOrdMod.setCodUfficioMagistratoComp(aEventoSIUS.getCodUfficioEmittente());

		lDepOrdMod.setCodUfficioInserimento(aDatiOpModel.getCodUfficio());
		lDepOrdMod.setCodOperatoreInserimento(aDatiOpModel.getCodOperatore());
		lDepOrdMod.setDataInserimento(aDatiOpModel.getData());

		return lDepOrdMod;
	}

}