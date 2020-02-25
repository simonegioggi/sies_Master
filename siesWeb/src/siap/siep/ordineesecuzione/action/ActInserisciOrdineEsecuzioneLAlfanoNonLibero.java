package siap.siep.ordineesecuzione.action;

import java.math.BigDecimal;
import java.util.Date;

import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.util.SICOLookupRemote;
import siap.siep.notifica.action.ICostantiNotifica;
import siap.siep.notifica.model.NotificaModel;
import siap.siep.ordineesecuzione.controller.IOrdineEsecuzione;
import siap.siep.penaresidua.action.ICostantiPenaResidua;
import siap.siep.penaresidua.model.PenaResiduaModel;
import siap.siep.posizione.action.ICostantiPosizioneGiuridica;
import siap.siep.util.SIEPLookupRemote;

/**
 *
 * <p>
 * Title: ActInserisciOrdineEsecuzioneLAlfanoNonLibero
 * </p>
 * <p>
 * Description: Inserimento degli Ordini di Esecuzione LAlfanoNonLibero
 * </p>
 * <p>
 * Copyright: Copyright (c) 2010
 * </p>
 * <p>
 * Company:
 * </p>
 */
public class ActInserisciOrdineEsecuzioneLAlfanoNonLibero extends ActOrdineEsecuzione
		implements ICostantiOrdineEsecuzione {

	/**
	 * Azione di Inserimento del Evento
	 * 
	 * @return Nome della pagina JSP da visualizzare al termine dell'elaborazione
	 * @throws F3BException
	 */
	public String processRequest() throws Exception {

		// FascicoloSiepModel lFascicoloModel = (FascicoloSiepModel) getSessionAttribute("fascicolo");
		Integer lPosInt = new Integer(
				getRequestStringParameter(ICostantiPosizioneGiuridica.CAMPO_COD_POSIZIONE_GIURIDICA));

		String lPage = "";

		switch (lPosInt.intValue()) {
		case 1:
		case 2:
		case 3:
			// MEV 10 S3
		case 70:
		case 71:
		case 72: {
			lPage = InserisciLAlfNonLibero(
					getRequestStringParameter(ICostantiPosizioneGiuridica.CAMPO_COD_POSIZIONE_GIURIDICA));
			break;
		}
		default: {
			// Metodo per posizioni diverse da quelle precedenti. Al momento nessuna contemplate: 9/12/2010
			lPage = InserisciLAlfAltrePosizioni();
			break;
		}
		}

		return lPage;
	}

	/**
	 * Inserisci LAlf Non Libero (pos. giuridiche 01, 02, 03)
	 * 
	 * @return JSP page
	 * @throws Exception
	 */
	private String InserisciLAlfNonLibero(String lCodPosGiu) throws Exception {
		String lCodiceOperatore = this.getCodUtenteConnesso();
		String lCodiceUfficio = this.getCodUfficioUtenteConnesso();
		Date lDataEmissione = getRequestDateParameter(ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE,
				ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE, ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE);

		Date lDataTrasmissione = getRequestDateParameter(ICostantiNotifica.CAMPO_ANNO_DATA_INVIO,
				ICostantiNotifica.CAMPO_MESE_DATA_INVIO, ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO);

		EventoNotificaModel lEve = new EventoNotificaModel();
		PenaResiduaModel lPenaRes = new PenaResiduaModel();
		lEve.getEvento().setDescrMotivo("LED-DET");
		if (lCodPosGiu.compareTo("02") == 0) {
			lEve.getEvento().setDescrMotivo("LED-ARR");
			// MEV 10 S3 ******************
		} else if (lCodPosGiu.compareTo("70") == 0) {
			lEve.getEvento().setDescrMotivo("LED-ARR-DOM");
		} else if (lCodPosGiu.compareTo("71") == 0) {
			lEve.getEvento().setDescrMotivo("LED-PERM");
		} else if (lCodPosGiu.compareTo("72") == 0) {
			lEve.getEvento().setDescrMotivo("LED-COLL");
		}
		// *****************************

		// Impostare il Tipo Provvedimento a 06 (Ordine di Esecuzione)
		lEve.getEvento().setCodTipoProvvedimento("06");

		lEve.setEvento(setEventoOrdineEsecuzione(lEve.getEvento()));
		lEve.getMagistrato()
				.setCodMagistrato(getRequestStringParameter(ICostantiEvento.CAMPO_COD_MAGISTRATO));
		// lEve.getMagistrato().getMagistratoCompetente().setMagCodMagistrato(getRequestStringParameter(ICostantiEvento.CAMPO_COD_MAGISTRATO));
		IEvento lCtrlEvento = SICOLookupRemote.getEventoRemote();
		IOrdineEsecuzione lCtrl = SIEPLookupRemote.getOrdineEsecuzioneRemote();

		EventoNotificaModel lRetModel = new EventoNotificaModel();

		// Inserisco l'array di Notifiche nell'Evento
		NotificaModel[] lNotifiche = this.setNotificheOrdineEsecuzione();

		// Controllo se il foglio complementare è ceccato
		String lFc = "0";
		if (!isRequestParameterNullObj(FOGLIO_COMPLEMENTARE))
			lFc = "1";

		// Pena residua
		BigDecimal lIdPenaRes = getRequestBigDecimalParameter(ICostantiPenaResidua.CAMPO_ID_PENA_RESIDUA);
		lPenaRes.setIdPenaResidua(lIdPenaRes);
		if (!isRequestParameterNullObj(ICostantiPenaResidua.CAMPO_ANNO_DATA_FINE))
			lPenaRes.setDataFine(getRequestDateParameter(ICostantiPenaResidua.CAMPO_ANNO_DATA_FINE,
					ICostantiPenaResidua.CAMPO_MESE_DATA_FINE, ICostantiPenaResidua.CAMPO_GIORNO_DATA_FINE));

		// Aggiungere Notifiche per Arresti Domiciliari
		if (lNotifiche[0] != null) {
			NotificaModel[] lNotArray = new NotificaModel[lNotifiche.length + 3];

			for (int i = 0; i < lNotifiche.length; i++)
				lNotArray[i] = lNotifiche[i];

			// Notifica Ufficio Sorveglianza
			NotificaModel lNotificheUfficioS = new NotificaModel();
			lNotificheUfficioS.setCodTipoNotifica("N");
			lNotificheUfficioS.setDataInvio(lDataEmissione); // era commentata !
			lNotificheUfficioS.setDataInvio(lDataTrasmissione);
			lNotificheUfficioS.setCodEsito("-");
			lNotificheUfficioS.setCodOperatoreInserimento(lCodiceOperatore);
			lNotificheUfficioS.setDataInserimento(DateUtils.getSysDate());
			lNotificheUfficioS.setCodUfficioInserimento(lCodiceUfficio);
			String lDescrComuneUDS = this
					.getRequestStringParameter(ICostantiOrdineEsecuzione.CAMPO_COD_SEDE_UDS_NOTIFICA);
			String lTipoUff = getRequestStringParameter(ICostantiOrdineEsecuzione.CAMPO_COD_SEDE_UDS_UDSM);
			lNotificheUfficioS.setUffCodUfficio(
					this.getCodUfficioByCodTipoUfficioDescrComune(lTipoUff, lDescrComuneUDS));

			lNotificheUfficioS
					.setNote(this.getRequestStringParameter(ICostantiOrdineEsecuzione.CAMPO_NOTE_UDS));

			// lNotArray[lNotifiche.length + 1] = lNotificheUfficioS;
			lNotArray[lNotifiche.length] = lNotificheUfficioS;

			lEve.setNotifiche(lNotArray);

			lRetModel = lCtrl.ExInserisciOModificaLAlfNotifica(lEve, lPenaRes);
		} else
			lRetModel.setEvento(lCtrlEvento.ExInserisciEvento(lEve.getEvento()));

		String lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
				+ "=siap.siep.ordineesecuzione.action.ActDettaglioLAlfanoNonLibero&"
				+ ICostantiEvento.CAMPO_ID_EVENTO + "=" + lRetModel.getEvento().getIdEvento()
				+ "&modalita=I&fc=" + lFc;

		return lPage;
	}

	private String InserisciLAlfAltrePosizioni() throws Exception {
		String lCodiceOperatore = this.getCodUtenteConnesso();
		String lCodiceUfficio = this.getCodUfficioUtenteConnesso();
		Date lDataEmissione = getRequestDateParameter(ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE,
				ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE, ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE);

		EventoNotificaModel lEve = new EventoNotificaModel();
		PenaResiduaModel lPenaRes = new PenaResiduaModel();

		String lIstPre = getRequestStringParameter("istanza");

		lEve.getEvento().setDescrMotivo("0000");

		lEve.setEvento(setEventoOrdineEsecuzione(lEve.getEvento()));
		lEve.getMagistrato()
				.setCodMagistrato(getRequestStringParameter(ICostantiEvento.CAMPO_COD_MAGISTRATO));
		// lEve.getMagistrato().getMagistratoCompetente().setMagCodMagistrato(getRequestStringParameter(ICostantiEvento.CAMPO_COD_MAGISTRATO));

		IEvento lCtrlEvento = SICOLookupRemote.getEventoRemote();
		IOrdineEsecuzione lCtrl = SIEPLookupRemote.getOrdineEsecuzioneRemote();
		EventoNotificaModel lRetModel = new EventoNotificaModel();

		// Inserisco l'array di Notifiche nell'Evento
		NotificaModel[] lNotifiche = this.setNotificheOrdineEsecuzione();

		// Controllo se il foglio complementare è ceccato
		String lFc = "0";
		if (!isRequestParameterNullObj(FOGLIO_COMPLEMENTARE))
			lFc = "1";

		// Pena residua
		BigDecimal lIdPenaRes = getRequestBigDecimalParameter(ICostantiPenaResidua.CAMPO_ID_PENA_RESIDUA);
		lPenaRes.setIdPenaResidua(lIdPenaRes);
		if (!isRequestParameterNullObj(ICostantiPenaResidua.CAMPO_ANNO_DATA_FINE))
			lPenaRes.setDataFine(getRequestDateParameter(ICostantiPenaResidua.CAMPO_ANNO_DATA_FINE,
					ICostantiPenaResidua.CAMPO_MESE_DATA_FINE, ICostantiPenaResidua.CAMPO_GIORNO_DATA_FINE));

		if (lNotifiche[0] != null) {
			if (lIstPre.equals("S")) { // Setto l'id_evento istanza sull'evento che sto inserendo
				if (!this.isRequestParameterNullObj("IdIstanza")) {
					BigDecimal lIdIstanza = this.getRequestBigDecimalParameter("IdIstanza");
					lEve.getEvento().setEveIdEvento(lIdIstanza);
				}

				NotificaModel[] lNotArray = new NotificaModel[lNotifiche.length + 1];
				for (int i = 0; i < lNotifiche.length; i++)
					lNotArray[i] = lNotifiche[i];
				// Notifica Ufficio Sorveglianza
				NotificaModel lNotificheUfficioS = new NotificaModel();
				lNotificheUfficioS.setCodTipoNotifica("N");
				lNotificheUfficioS.setDataInvio(lDataEmissione);
				lNotificheUfficioS.setCodEsito("-");
				lNotificheUfficioS.setCodOperatoreInserimento(lCodiceOperatore);
				lNotificheUfficioS.setDataInserimento(DateUtils.getSysDate());
				lNotificheUfficioS.setCodUfficioInserimento(lCodiceUfficio);
				String lDescrComuneTDS = this
						.getRequestStringParameter(ICostantiOrdineEsecuzione.CAMPO_COD_SEDE_TDS);
				String lTipoUff = getRequestStringParameter(
						ICostantiOrdineEsecuzione.CAMPO_COD_SEDE_TDS_TDSM);
				lNotificheUfficioS.setUffCodUfficio(
						this.getCodUfficioByCodTipoUfficioDescrComune(lTipoUff, lDescrComuneTDS));

				lNotificheUfficioS
						.setNote(this.getRequestStringParameter(ICostantiOrdineEsecuzione.CAMPO_NOTE_TDS));

				lNotArray[lNotifiche.length] = lNotificheUfficioS;

				lEve.setNotifiche(lNotArray);

				lRetModel = lCtrl.ExInserisciOModificaLSNotifica(lEve, lPenaRes);
			} else {
				lEve.setNotifiche(lNotifiche);
				lRetModel = lCtrl.ExInserisciOModificaLSNotifica(lEve, lPenaRes);
			}
		} else
			lRetModel.setEvento(lCtrlEvento.ExInserisciEvento(lEve.getEvento()));

		String lPage = null;

		lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
				+ "=siap.siep.ordineesecuzione.action.ActDettaglioLSAltrePosizioni&"
				+ ICostantiEvento.CAMPO_ID_EVENTO + "=" + lRetModel.getEvento().getIdEvento()
				+ "&modalita=I&fc=" + lFc;

		return lPage;
	}

}