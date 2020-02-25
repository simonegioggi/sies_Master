package siap.siep.ordineesecuzione.action;

import java.math.BigDecimal;
import java.util.Date;

import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.util.SICOLookupRemote;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.notifica.model.NotificaModel;
import siap.siep.ordineesecuzione.controller.IOrdineEsecuzione;
import siap.siep.penaresidua.action.ICostantiPenaResidua;
import siap.siep.penaresidua.model.PenaResiduaModel;
import siap.siep.posizione.action.ICostantiPosizioneGiuridica;
import siap.siep.util.SIEPLookupRemote;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;

/**
 *
 * <p>
 * Title: ActInserisciComunicazioneL78del2013
 * </p>
 * <p>
 * Description: Inserimento Comunicazioni relative al Decreto Legge N. 78 del 2013
 * </p>
 * <p>
 * Copyright: Copyright (c) 2013
 * </p>
 * <p>
 * Company: intersistemi Italia spa
 * </p>
 */
public class ActInserisciComunicazioneL78del2013 extends ActOrdineEsecuzione implements
		ICostantiOrdineEsecuzione {

	/**
	 * Azione di Inserimento del Evento
	 * 
	 * @return Nome della pagina JSP da visualizzare al termine dell'elaborazione
	 * @throws F3BException
	 */
	public String processRequest() throws Exception {

		FascicoloSiepModel lFascicoloModel = (FascicoloSiepModel) getSessionAttribute("fascicolo");
//		Integer lPosInt = new Integer(
//				getRequestStringParameter(ICostantiPosizioneGiuridica.CAMPO_COD_POSIZIONE_GIURIDICA));

		String lCodPosGiu = (getRequestStringParameter(ICostantiPosizioneGiuridica.CAMPO_COD_POSIZIONE_GIURIDICA));
		String lCodiceOperatore = this.getCodUtenteConnesso();
		String lCodiceUfficio = this.getCodUfficioUtenteConnesso();
		Date lDataEmissione = getRequestDateParameter(ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE,
				ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE, ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE);

//		Date lDataTrasmissione = getRequestDateParameter(ICostantiNotifica.CAMPO_ANNO_DATA_INVIO,
//				ICostantiNotifica.CAMPO_MESE_DATA_INVIO, ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO);

		EventoNotificaModel lEve = new EventoNotificaModel();
		PenaResiduaModel lPenaRes = new PenaResiduaModel();

		// AltraCausaModel lAcModel = new AltraCausaModel();
		// IAltraCausa lAC = SIEPLookupRemote.getAltraCausa();
		// lAcModel = lAC.ExRicercaAltraCausaByFascicolo( lFascicoloModel.getIdFascicoloSiep());

		if (lCodPosGiu.compareTo("02") == 0) {
			lEve.getEvento().setCodMotivo("1023");
		} else if (lCodPosGiu.compareTo("70") == 0)
			lEve.getEvento().setCodMotivo("5524");
		else if (lCodPosGiu.compareTo("71") == 0)
			lEve.getEvento().setCodMotivo("5522");
		else if (lCodPosGiu.compareTo("72") == 0)
			lEve.getEvento().setCodMotivo("5523");
		else {
			lEve.getEvento().setCodMotivo("1022");
		}

		lEve.getEvento().setCodTipoEvento("01"); // Tipo Evento = PROVVEDIMENTO
		lEve.getEvento().setCodTipoProvvedimento("12"); // Tipo Provvedimento = ORDINE ESECUZIONE
		lEve.getEvento().setFlagStampaSiep("S");
		lEve.getEvento().setFlagVideoSiep("S");

		lEve.getEvento().setFasSieIdFascicoloSiep(lFascicoloModel.getIdFascicoloSiep());
		lEve.getEvento().setDataEmissione(lDataEmissione);

		lEve.getEvento().setCodOperatoreInserimento(lCodiceOperatore);
		lEve.getEvento().setDataInserimento(DateUtils.getSysDate());
		lEve.getEvento().setCodUfficioInserimento(lCodiceUfficio);

		lEve.getEvento().setCodLuogoEmittente(this.getCodComuneUtenteConnesso());
		lEve.getEvento().setCodUfficioEmittente(lCodiceUfficio);
		lEve.getEvento().setCodMagistrato(getRequestStringParameter(ICostantiEvento.CAMPO_COD_MAGISTRATO));
		lEve.getEvento().setAnnoProtocollo(new BigDecimal(DateUtils.getSysDate("yyyy")));

		/*
		 * lEve.getEvento().setCodOperatoreAggiornamento(lCodiceOperatore);
		 * lEve.getEvento().setCodUfficioAggiornamento(lCodiceUfficio);
		 * lEve.getEvento().setDataAggiornamento(DateUtils.getSysDate());
		 */
		lEve.getEvento().setCodEsito("-");
		lEve.getEvento().setCodLuogoDestinatario("-");
		lEve.getEvento().setCodTipoUfficioDestinatario("-");
		lEve.getEvento().setCodMagistrato(this.calcolaMagistrato());
		//
		lEve.getMagistrato()
				.setCodMagistrato(getRequestStringParameter(ICostantiEvento.CAMPO_COD_MAGISTRATO));

		IEvento lCtrlEvento = SICOLookupRemote.getEventoRemote();
		IOrdineEsecuzione lCtrl = SIEPLookupRemote.getOrdineEsecuzioneRemote();

		EventoNotificaModel lRetModel = new EventoNotificaModel();

		NotificaModel[] lNotifiche = this.setNotificheL78del2013();

		// Pena residua
		BigDecimal lIdPenaRes = getRequestBigDecimalParameter(ICostantiPenaResidua.CAMPO_ID_PENA_RESIDUA);
		lPenaRes.setIdPenaResidua(lIdPenaRes);
		if (!isRequestParameterNullObj(ICostantiPenaResidua.CAMPO_ANNO_DATA_FINE))
			lPenaRes.setDataFine(getRequestDateParameter(ICostantiPenaResidua.CAMPO_ANNO_DATA_FINE,
					ICostantiPenaResidua.CAMPO_MESE_DATA_FINE, ICostantiPenaResidua.CAMPO_GIORNO_DATA_FINE));

		// Aggiungere Notifiche per Arresti Domiciliari
		if (lNotifiche[0] != null) {
			lEve.setNotifiche(lNotifiche);
			lRetModel = lCtrl.ExInserisciOModificaLegge78del2013(lEve, lPenaRes);
		} else
			lRetModel.setEvento(lCtrlEvento.ExInserisciEvento(lEve.getEvento()));

		String lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
				+ "=siap.siep.ordineesecuzione.action.ActDettaglioComunicazioneL78del2013&"
				+ ICostantiEvento.CAMPO_ID_EVENTO + "=" + lRetModel.getEvento().getIdEvento() + "&modalita=I";

		return lPage;
	}

//	private String InserisciLAlfAltrePosizioni() throws Exception {
//
//		String lCodiceOperatore = this.getCodUtenteConnesso();
//		String lCodiceUfficio = this.getCodUfficioUtenteConnesso();
//		Date lDataEmissione = getRequestDateParameter(ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE,
//				ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE, ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE);
//
//		EventoNotificaModel lEve = new EventoNotificaModel();
//		PenaResiduaModel lPenaRes = new PenaResiduaModel();
//
//		String lIstPre = getRequestStringParameter("istanza");
//
//		lEve.getEvento().setDescrMotivo("0000");
//
//		lEve.setEvento(setEventoOrdineEsecuzione(lEve.getEvento()));
//		lEve.getMagistrato()
//				.setCodMagistrato(getRequestStringParameter(ICostantiEvento.CAMPO_COD_MAGISTRATO));
//		// lEve.getMagistrato().getMagistratoCompetente().setMagCodMagistrato(getRequestStringParameter(ICostantiEvento.CAMPO_COD_MAGISTRATO));
//
//		IEvento lCtrlEvento = SICOLookupRemote.getEventoRemote();
//		IOrdineEsecuzione lCtrl = SIEPLookupRemote.getOrdineEsecuzioneRemote();
//		EventoNotificaModel lRetModel = new EventoNotificaModel();
//
//		// Inserisco l'array di Notifiche nell'Evento
//		NotificaModel[] lNotifiche = this.setNotificheOrdineEsecuzione();
//
//		// Controllo se il foglio complementare è ceccato
//		String lFc = "0";
//		if (!isRequestParameterNullObj(FOGLIO_COMPLEMENTARE))
//			lFc = "1";
//
//		// Pena residua
//		BigDecimal lIdPenaRes = getRequestBigDecimalParameter(ICostantiPenaResidua.CAMPO_ID_PENA_RESIDUA);
//		lPenaRes.setIdPenaResidua(lIdPenaRes);
//		if (!isRequestParameterNullObj(ICostantiPenaResidua.CAMPO_ANNO_DATA_FINE))
//			lPenaRes.setDataFine(getRequestDateParameter(ICostantiPenaResidua.CAMPO_ANNO_DATA_FINE,
//					ICostantiPenaResidua.CAMPO_MESE_DATA_FINE, ICostantiPenaResidua.CAMPO_GIORNO_DATA_FINE));
//
//		if (lNotifiche[0] != null) {
//			if (lIstPre.equals("S")) { // Setto l'id_evento istanza sull'evento che sto inserendo
//				if (!this.isRequestParameterNullObj("IdIstanza")) {
//					BigDecimal lIdIstanza = this.getRequestBigDecimalParameter("IdIstanza");
//					lEve.getEvento().setEveIdEvento(lIdIstanza);
//				}
//
//				NotificaModel[] lNotArray = new NotificaModel[lNotifiche.length + 1];
//				for (int i = 0; i < lNotifiche.length; i++)
//					lNotArray[i] = lNotifiche[i];
//				// Notifica Ufficio Sorveglianza
//				NotificaModel lNotificheUfficioS = new NotificaModel();
//				lNotificheUfficioS.setCodTipoNotifica("N");
//				lNotificheUfficioS.setDataInvio(lDataEmissione);
//				lNotificheUfficioS.setCodEsito("-");
//				lNotificheUfficioS.setCodOperatoreInserimento(lCodiceOperatore);
//				lNotificheUfficioS.setDataInserimento(DateUtils.getSysDate());
//				lNotificheUfficioS.setCodUfficioInserimento(lCodiceUfficio);
//				String lDescrComuneTDS = this
//						.getRequestStringParameter(ICostantiOrdineEsecuzione.CAMPO_COD_SEDE_TDS);
//				lNotificheUfficioS.setUffCodUfficio(this.getCodUfficioByCodTipoUfficioDescrComune("TDS",
//						lDescrComuneTDS));
//
//				lNotificheUfficioS.setNote(this
//						.getRequestStringParameter(ICostantiOrdineEsecuzione.CAMPO_NOTE_TDS));
//
//				lNotArray[lNotifiche.length] = lNotificheUfficioS;
//
//				lEve.setNotifiche(lNotArray);
//
//				lRetModel = lCtrl.ExInserisciOModificaLSNotifica(lEve, lPenaRes);
//			} else {
//				lEve.setNotifiche(lNotifiche);
//				lRetModel = lCtrl.ExInserisciOModificaLSNotifica(lEve, lPenaRes);
//			}
//		} else
//			lRetModel.setEvento(lCtrlEvento.ExInserisciEvento(lEve.getEvento()));
//
//		String lPage = null;
//
//		lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
//				+ "=siap.siep.ordineesecuzione.action.ActDettaglioLSAltrePosizioni&"
//				+ ICostantiEvento.CAMPO_ID_EVENTO + "=" + lRetModel.getEvento().getIdEvento()
//				+ "&modalita=I&fc=" + lFc;
//
//		return lPage;
//	}

}