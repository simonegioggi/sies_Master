package siap.siep.misurasicurezza.action;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;

import siap.sico.decodifiche.model.ComuneModel;
import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.ufficio.action.ICostantiUfficio;
import siap.sico.web.ActionSiap;
import siap.siep.autoritaesterna.action.ICostantiAutoritaEsterna;
import siap.siep.autoritaesterna.model.AutoritaEsternaModel;
import siap.siep.avvocato.action.ICostantiAvvocato;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.luogodetenzione.action.ICostantiLuogoDetenzione;
import siap.siep.notifica.action.ICostantiNotifica;
import siap.siep.notifica.model.NotificaModel;
import siap.siep.ordineesecuzione.action.ICostantiOrdineEsecuzione;
import siap.siep.ordineesecuzione.controller.IOrdineEsecuzione;
import siap.siep.penaresidua.action.ICostantiPenaResidua;
import siap.siep.penaresidua.model.PenaResiduaModel;
import siap.siep.util.SIEPLookupRemote;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;

/**
 *
 * <p>
 * Title: ActInserisciOEInternamento
 * </p>
 * <p>
 * Description: Classe l' Emissione O.E. per Internamento
 * </p>
 * <p>
 * per esecuzione Misura di Sicurezza
 * </p>
 * <p>
 * Copyright: Copyright (c) 2014
 * </p>
 * <p>
 * Date: 12/02/2014
 * 
 * @author AMBROS
 * @version 1.0
 */
public class ActInserisciOEInternamento extends ActionSiap implements ICostantiMisuraSicurezza,
		ICostantiOrdineEsecuzione {

	/**
	 * Azione di Inserimento del Evento
	 * 
	 * @return Nome della pagina JSP da visualizzare al termine dell'elaborazione
	 * @throws F3BException
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String processRequest() throws Exception {

		FascicoloSiepModel lFascicoloModel = (FascicoloSiepModel) getSessionAttribute("fascicolo");
//		String lCodPosGiu = getRequestStringParameter(ICostantiPosizioneGiuridica.CAMPO_COD_POSIZIONE_GIURIDICA);

		String lPage = "";

		String lCodiceOperatore = this.getCodUtenteConnesso();
		String lCodiceUfficio = this.getCodUfficioUtenteConnesso();
		Date lDataEmissione = getRequestDateParameter(ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE,
				ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE, ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE);

		Date lDataTrasmissione = getRequestDateParameter(ICostantiEvento.CAMPO_ANNO_DATA_TRASMISSIONE_ATTI,
				ICostantiEvento.CAMPO_MESE_DATA_TRASMISSIONE_ATTI,
				ICostantiEvento.CAMPO_GIORNO_DATA_TRASMISSIONE_ATTI);

//		String codidMis = getRequestStringParameter(ICostantiMisuraSicurezza.CAMPO_ID_MISURA_SICUREZZA);

		EventoNotificaModel lEve = new EventoNotificaModel();
		PenaResiduaModel lPenaRes = new PenaResiduaModel();

		lEve.getEvento().setCodTipoEvento("01"); // provvedimento
		lEve.getEvento().setCodTipoProvvedimento("06"); // Ordine Esecuzione
		lEve.getEvento().setCodMotivo("1128"); // per Internamento
		lEve.getEvento().setFlagStampaSiep("S");
		lEve.getEvento().setFlagVideoSiep("S");

		lEve.getEvento().setFasSieIdFascicoloSiep(lFascicoloModel.getIdFascicoloSiep());
		lEve.getEvento().setDataEmissione(lDataEmissione);

		lEve.getEvento().setCodOperatoreInserimento(lCodiceOperatore);
		lEve.getEvento().setDataInserimento(DateUtils.getSysDate());
		lEve.getEvento().setCodUfficioInserimento(lCodiceUfficio);

		lEve.getEvento().setCodLuogoEmittente(this.getCodComuneUtenteConnesso());
		lEve.getEvento().setCodUfficioEmittente(lCodiceUfficio);
		lEve.getEvento().setAnnoProtocollo(new BigDecimal(DateUtils.getSysDate("yyyy")));

		lEve.getEvento().setCodEsito("-");
		lEve.getEvento().setCodLuogoDestinatario("-");
		lEve.getEvento().setCodTipoUfficioDestinatario("-");

		if (!isRequestParameterNullObj(CAMPO_FLAG_ESECUZIONE_IMMEDIATA)
				&& getRequestStringParameter(CAMPO_FLAG_ESECUZIONE_IMMEDIATA) != null) {
			if (getRequestStringParameter(CAMPO_FLAG_ESECUZIONE_IMMEDIATA).endsWith("D")) {
				lEve.getEvento().setFlagPiuMeno("D");
			}
		}

		lEve.getMagistrato()
				.setCodMagistrato(getRequestStringParameter(ICostantiEvento.CAMPO_COD_MAGISTRATO));
		lEve.getEvento().setCodMagistrato(getRequestStringParameter(ICostantiEvento.CAMPO_COD_MAGISTRATO));

		// Notifiche ---------> PRIMA
		EventoNotificaModel lRetModel = new EventoNotificaModel();
		ArrayList lNotificheArray = new ArrayList();

		String lSedeDestinatario_E = null;
		String lDestinatario_E = null;
		String lNote_E = null;
		String lDestinatario_ist = null;

		if (!this.isRequestParameterNullObj(ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA_E)
				&& !this.getRequestStringParameter(ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA_E)
						.equals("-")) {
			lDestinatario_E = this
					.getRequestStringParameter(ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA_E);
			lSedeDestinatario_E = this.getRequestStringParameter(ICostantiAutoritaEsterna.CAMPO_COD_SEDE_E);
		}

		if (!this.isRequestParameterNullObj(ICostantiNotifica.CAMPO_NOTE_E))
			lNote_E = this.getRequestStringParameter(ICostantiNotifica.CAMPO_NOTE_E);

		if (!this.isRequestParameterNullObj(ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE))
			lDestinatario_ist = this
					.getRequestStringParameter(ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE);

		// notifiche al Destinatario per Esecuzione
		NotificaModel lNotMod = new NotificaModel();
		lNotMod.setCodTipoNotifica("E");
		lNotMod.setDataInvio(lDataTrasmissione);
		lNotMod.setCodEsito("-");
		lNotMod.setCodOperatoreInserimento(lCodiceOperatore);
		lNotMod.setDataInserimento(DateUtils.getSysDate());
		lNotMod.setCodUfficioInserimento(lCodiceUfficio);
		lNotMod.setNote(lNote_E);

		if (lDestinatario_E != null) {
			AutoritaEsternaModel lAutMod = new AutoritaEsternaModel();

			lAutMod.setCodTipoAutorita(lDestinatario_E);
			ComuneModel lComModel = new ComuneModel(getCodComuneByDescrFlagVal(lSedeDestinatario_E));
			lAutMod.setCodSede(lComModel.getCodComune());
			lAutMod.setCodOperatoreInserimento(lCodiceOperatore);
			lAutMod.setCodUfficioInserimento(lCodiceUfficio);
			lAutMod.setDataInserimento(DateUtils.getSysDate());
			lNotMod.setIstDetIdIstitutoDetenzione("");

			lNotMod.setAutoritaEsterna(lAutMod);
		}

		if (lDestinatario_E != null)
			lNotificheArray.add(lNotMod);

		// Istituto

		if (lDestinatario_ist != null && !lDestinatario_ist.equals("-") && !lDestinatario_ist.equals("")) {
			NotificaModel lNotistMod = new NotificaModel();
			lNotistMod.setCodTipoNotifica("E");
			lNotistMod.setDataInvio(lDataTrasmissione);
			lNotistMod.setCodEsito("-");
			lNotistMod.setCodOperatoreInserimento(lCodiceOperatore);
			lNotistMod.setDataInserimento(DateUtils.getSysDate());
			lNotistMod.setCodUfficioInserimento(lCodiceUfficio);

			lNotistMod.setIstDetIdIstitutoDetenzione(lDestinatario_ist);
			lNotificheArray.add(lNotistMod);
		}

		// SETTO Destinatari UDS (Revisione del 24/11/2014)
		if (!isRequestParameterNullObj("tipoUDS") && !getRequestStringParameter("tipoUDS").equals("-")) {
			String lTipoUds = getRequestStringParameter("tipoUDS");
			String lComuneUds = getRequestStringParameter(ICostantiUfficio.CAMPO_SEDE_UFFICIO);
			String lCodUffUDS = getCodUfficioByCodTipoUfficioDescrComune(lTipoUds, lComuneUds);

			NotificaModel lNotUffUDS = new NotificaModel();

			lNotUffUDS.setCodEsito("-");
			lNotUffUDS.setCodOperatoreInserimento(lCodiceOperatore);
			lNotUffUDS.setDataInserimento(DateUtils.getSysDate());
			lNotUffUDS.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
			lNotUffUDS.setCodTipoNotifica("MS");
			lNotUffUDS.setDataInvio(lDataTrasmissione);

			lNotUffUDS.setUffCodUfficio(lCodUffUDS);

			lNotificheArray.add(lNotUffUDS);
		}

		// =================================================
		// Notifiche agli Avvocati (Notifica Difensori)
		// Solo se selezionate in Maschera il check: Notifiche Atti (Difensore - Condannato)
		// =================================================
		if (this.isRequestChecked("Difesa")) {
			String[] lAvvocati = null;
			if (!this.isRequestParameterNullObj(ICostantiAvvocato.CAMPO_ID_AVVOCATO)) {
				lAvvocati = this.getRequestStringParameters(ICostantiAvvocato.CAMPO_ID_AVVOCATO);
			}

			String[] lSedeDestinatario_avv = null;
			String[] lDestinatario_avv = null;
			String[] lNote_avv = null;

			if (!this.isRequestParameterNullObj(ICostantiAutoritaEsterna.CAMPO_COD_SEDE)) {
				lSedeDestinatario_avv = this
						.getRequestStringParameters(ICostantiAutoritaEsterna.CAMPO_COD_SEDE);
			}

			if (!this.isRequestParameterNullObj(ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA)) {
				lDestinatario_avv = this
						.getRequestStringParameters(ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA);
			}

			if (!this.isRequestParameterNullObj(ICostantiNotifica.CAMPO_NOTE))
				lNote_avv = this.getRequestStringParameters(ICostantiNotifica.CAMPO_NOTE);

			int lIndNotifiche = 0;
			int lNumAvvNotifiche = 0;
			if (lAvvocati != null)
				lNumAvvNotifiche = lAvvocati.length;

			if (lSedeDestinatario_avv != null) {
				if (lAvvocati != null) {
					while (lIndNotifiche < lNumAvvNotifiche) {
						NotificaModel lNot = new NotificaModel();

						lNot.setCodTipoNotifica("ND");
						lNot.setAvvIdAvvocatoFascicoloSiep(new BigDecimal(lAvvocati[lIndNotifiche]));
						lNot.setNote(lNote_avv[lIndNotifiche]);
						lNot.setDataInvio(lDataTrasmissione);
						lNot.setCodEsito("-");
						lNot.setCodOperatoreInserimento(lCodiceOperatore);
						lNot.setDataInserimento(DateUtils.getSysDate());
						lNot.setCodUfficioInserimento(lCodiceUfficio);

						AutoritaEsternaModel lAut = new AutoritaEsternaModel();

						if (isRequestChecked("SiNoTe")) { // Sistema Notifiche Telematiche
															// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la
															// variabile di istanza siesLogger al posto di
															// LogF3B.getLogger()
															// siesLogger.debug("Sistema Notifiche Telematiche");
							lAut.setCodTipoAutorita("C0");
							lAut.setCodSede("-");
						} else {
							// Notifica tramite UNEP
							// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al
							// posto di LogF3B.getLogger()
							// siesLogger.debug("Notifiche all'UNEP");
							lAut.setCodTipoAutorita(lDestinatario_avv[lIndNotifiche]);
							//INIZIO: MEV_21 (avvocati) - si inibisce la selezione di comuni non validi (tipo NAPOLI NORD)
//							ComuneModel lComMod = new ComuneModel(
//									getCodComuneByDescr(lSedeDestinatario_avv[lIndNotifiche]));
							ComuneModel lComMod = new ComuneModel(
									getCodComuneByDescrFlagVal(lSedeDestinatario_avv[lIndNotifiche]));
							//FINE: MEV_21
							lAut.setCodSede(lComMod.getCodComune());
							lAut.setDescrizione(lComMod.getDescrizione());
						}
						lAut.setCodOperatoreInserimento(lCodiceOperatore);
						lAut.setCodUfficioInserimento(lCodiceUfficio);
						lAut.setDataInserimento(DateUtils.getSysDate());

						// Setto l'Autorita Esterna per la notifica corrente
						lNot.setAutoritaEsterna(lAut);
						lIndNotifiche++;
						lNotificheArray.add(lNot);
					}
				}
			}
		} // End notifiche al difensore

		//
		NotificaModel[] lNotifiche = (NotificaModel[]) lNotificheArray.toArray(new NotificaModel[0]);

		// Pena residua (04/11/2014 va impostata = null se assente)
		if (!isRequestParameterNullObj(ICostantiPenaResidua.CAMPO_ID_PENA_RESIDUA)) {
			BigDecimal lIdPenaRes = getRequestBigDecimalParameter(ICostantiPenaResidua.CAMPO_ID_PENA_RESIDUA);
			lPenaRes.setIdPenaResidua(lIdPenaRes);

			if (!isRequestParameterNullObj(ICostantiPenaResidua.CAMPO_ANNO_DATA_FINE))
				lPenaRes.setDataFine(getRequestDateParameter(ICostantiPenaResidua.CAMPO_ANNO_DATA_FINE,
						ICostantiPenaResidua.CAMPO_MESE_DATA_FINE,
						ICostantiPenaResidua.CAMPO_GIORNO_DATA_FINE));
		} else {
			lPenaRes = null;
		}
		// ---> INSERIMENTO COMUNICAZIONE
		IOrdineEsecuzione lCtrl = SIEPLookupRemote.getOrdineEsecuzioneRemote();
		lEve.setNotifiche(lNotifiche);
		lRetModel = lCtrl.ExInserisciOENotifica(lEve, lPenaRes);

		lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
				+ "=siap.siep.misurasicurezza.action.ActDettaglioOEInternamento&"
				+ ICostantiEvento.CAMPO_ID_EVENTO + "=" + lRetModel.getEvento().getIdEvento();

		return lPage;

	} // Chiude processRequest

} // Chiude Action