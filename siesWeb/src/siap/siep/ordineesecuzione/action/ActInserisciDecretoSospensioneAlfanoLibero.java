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
import siap.siep.altracausa.controller.IAltraCausa;
import siap.siep.altracausa.model.AltraCausaModel;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.notifica.action.ICostantiNotifica;
import siap.siep.notifica.model.NotificaModel;
import siap.siep.ordineesecuzione.controller.IOrdineEsecuzione;
import siap.siep.ordineesecuzione.controller.IOrdineEsecuzioneAlfano;
import siap.siep.penaresidua.action.ICostantiPenaResidua;
import siap.siep.penaresidua.model.PenaResiduaModel;
import siap.siep.posizione.action.ICostantiPosizioneGiuridica;
import siap.siep.util.SIEPLookupRemote;

/**
 *
 * <p>
 * Title: ActInserisciDecretoSospensioneAlfanoLibero
 * </p>
 * <p>
 * Description: Inserimento Decreto Sospensione Esecuzione presso il Domicilio AlfanoLibero
 * </p>
 * <p>
 * Copyright: Copyright (c) 2010
 * </p>
 * <p>
 * Company:
 * </p>
 */
public class ActInserisciDecretoSospensioneAlfanoLibero extends ActOrdineEsecuzione
		implements ICostantiOrdineEsecuzione {
	/**
	 * Azione di Inserimento del Evento
	 * 
	 * @return Nome della pagina JSP da visualizzare al termine dell'elaborazione
	 * @throws F3BException
	 */
	public String processRequest() throws Exception {

		FascicoloSiepModel lFascicoloModel = (FascicoloSiepModel) getSessionAttribute("fascicolo");
		Integer lPosInt = new Integer(
				getRequestStringParameter(ICostantiPosizioneGiuridica.CAMPO_COD_POSIZIONE_GIURIDICA));

		String lPage = "";

		// controllo su tabella altra causa
		IAltraCausa lCtrlAltraCausa = SIEPLookupRemote.getAltraCausa();
		AltraCausaModel lAltrMod = lCtrlAltraCausa
				.ExRicercaAltraCausaByFascicolo(lFascicoloModel.getIdFascicoloSiep());
		this.setRequestAttribute("altracausaposizionegiuridica", lAltrMod);
		if (lFascicoloModel.getFlagAltraCausa() != null && lFascicoloModel.getFlagAltraCausa().equals("S")) {
			lPage = InserisciLSAltraCausa();
		} else {
			switch (lPosInt.intValue()) {
			// case 1: { lPage = InserisciLSDetenutoQC();break;}
			case 2: {
				lPage = InserisciLSArrestiDomiciliari();
				break;
			}
			// case 3: { lPage = InserisciLSDetenutoQC();break;}
			case 4: {
				lPage = InserisciLSArrestiDomiciliari();
				break;
			}
			case 7:
			case 10:
			case 46: // Libero
			case 47: // Libero
			case 16: // Libero
			case 17: // Libero
			case 20: // Libero
			case 26: // Libero
			case 30: {
				lPage = InserisciDecretoSospAlfanoLibero();
				break;
			}
			// case 11:{ lPage = InserisciLSDetenutoQC();break;}
			default: {
				// Metodo per posizioni diverse da quelle precedenti
				lPage = InserisciLSAltrePosizioni();
				break;
			}
			}
		}
		return lPage;
	}

	/**
	 * Inserisci il Decreto di Sospensione Esecuzione Alfano per Libero
	 * 
	 * @return
	 * @throws Exception
	 */
	private String InserisciDecretoSospAlfanoLibero() throws Exception {
		String lCodiceOperatore = this.getCodUtenteConnesso();
		String lCodiceUfficio = this.getCodUfficioUtenteConnesso();
		Date lDataEmissione = getRequestDateParameter(ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE,
				ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE, ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE);

		EventoNotificaModel lEve = new EventoNotificaModel();
		PenaResiduaModel lPenaRes = new PenaResiduaModel();

		String lIstPre = null;
		lIstPre = getRequestStringParameter("istanza");

		if (lIstPre.equals("N"))
			lEve.getEvento().setDescrMotivo("LED-LIBDS");
		else
			lEve.getEvento().setDescrMotivo("LED-LIBDS-I");

		lEve.setEvento(setEventoDecretoSospensione(lEve.getEvento()));
		lEve.getMagistrato()
				.setCodMagistrato(getRequestStringParameter(ICostantiEvento.CAMPO_COD_MAGISTRATO));

		IEvento lCtrlEvento = SICOLookupRemote.getEventoRemote();
		// IOrdineEsecuzione lCtrl = SIEPLookupRemote.getOrdineEsecuzioneRemote();
		IOrdineEsecuzioneAlfano lCtrlOEA = SIEPLookupRemote.getOrdineEsecuzioneAlfanoRemote();
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
			if (lIstPre.equals("S")) {
				// Se l'istanza è presente setto l'eveIdEvento
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
				lNotificheUfficioS.setUffCodUfficio(
						this.getCodUfficioByCodTipoUfficioDescrComune("TDS", lDescrComuneTDS));
				lNotificheUfficioS
						.setNote(this.getRequestStringParameter(ICostantiOrdineEsecuzione.CAMPO_NOTE_TDS));
				lNotArray[lNotifiche.length] = lNotificheUfficioS;

				lEve.setNotifiche(lNotArray);

				lRetModel = lCtrlOEA.ExInserisciOModificaLAlfanoNotifica(lEve, lPenaRes);
			} else {
				lEve.setNotifiche(lNotifiche);
				lRetModel = lCtrlOEA.ExInserisciOModificaLAlfanoNotifica(lEve, lPenaRes);
			}
		} else
			lRetModel.setEvento(lCtrlEvento.ExInserisciEvento(lEve.getEvento()));

		String lPage = null;
		// if (lIstPre.equals("S"))
		// {
		// lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD +
		// "=siap.siep.ordineesecuzione.action.ActDettaglioLAlfanoLiberoIstanzaProdotta&" +
		// ICostantiEvento.CAMPO_ID_EVENTO + "=" +
		// lRetModel.getEvento().getIdEvento() + "&modalita=I&fc=" + lFc;
		// }
		// else
		// {
		lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
				+ "=siap.siep.ordineesecuzione.action.ActDettaglioDecretoSospLAlfanoLibero&"
				+ ICostantiEvento.CAMPO_ID_EVENTO + "=" + lRetModel.getEvento().getIdEvento()
				+ "&modalita=I&fc=" + lFc;
		// }

		return lPage;
	}

	/**
	 * Inserisci LSArresti Domiciliari
	 * 
	 * @return JSP page
	 * @throws Exception
	 */
	private String InserisciLSArrestiDomiciliari() throws Exception {

		String lCodiceOperatore = this.getCodUtenteConnesso();
		String lCodiceUfficio = this.getCodUfficioUtenteConnesso();
		// Date lDataEmissione = getRequestDateParameter(ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE,
		// ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE,
		// ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE);

		Date lDataTrasmissione = getRequestDateParameter(ICostantiNotifica.CAMPO_ANNO_DATA_INVIO,
				ICostantiNotifica.CAMPO_MESE_DATA_INVIO, ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO);

		EventoNotificaModel lEve = new EventoNotificaModel();
		PenaResiduaModel lPenaRes = new PenaResiduaModel();
		lEve.getEvento().setDescrMotivo("LS-ARR");

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

			// Notifica Servizi Sociali
			NotificaModel lNotificheServiziSociali = new NotificaModel();

			// AutoritaEsternaModel lAutEstMod = new AutoritaEsternaModel();
			// lAutEstMod.setCodTipoAutorita("SSPA");
			// lAutEstMod.setCodSede(getRequestStringParameter(ICostantiOrdineEsecuzione.CAMPO_COD_SEDE_SSPA));
			BigDecimal lIdSSPA = new BigDecimal(
					getRequestStringParameter(ICostantiOrdineEsecuzione.CAMPO_COD_SEDE_SSPA));
			// lNotificheServiziSociali.setUffCodUfficio(this.getCodUfficioByCodTipoUfficioDescrComune("SSPA",
			// lDescrComuneSSPA));
			lNotificheServiziSociali.setCssIdCssa(lIdSSPA);
			lNotificheServiziSociali.setCodTipoNotifica("N");
			// lNotificheServiziSociali.setDataInvio(lDataEmissione);
			lNotificheServiziSociali.setDataInvio(lDataTrasmissione);
			lNotificheServiziSociali.setCodEsito("-");
			lNotificheServiziSociali.setCodOperatoreInserimento(lCodiceOperatore);
			lNotificheServiziSociali.setDataInserimento(DateUtils.getSysDate());
			lNotificheServiziSociali.setCodUfficioInserimento(lCodiceUfficio);

			lNotificheServiziSociali
					.setNote(this.getRequestStringParameter(ICostantiOrdineEsecuzione.CAMPO_NOTE_SSPA));

			// lNotificheServiziSociali.setAutoritaEsterna(lAutEstMod);
			lNotArray[lNotifiche.length] = lNotificheServiziSociali;

			// Notifica Ufficio Sorveglianza
			NotificaModel lNotificheUfficioS = new NotificaModel();
			lNotificheUfficioS.setCodTipoNotifica("N");
			// lNotificheUfficioS.setDataInvio(lDataEmissione);
			lNotificheUfficioS.setDataInvio(lDataTrasmissione);
			lNotificheUfficioS.setCodEsito("-");
			lNotificheUfficioS.setCodOperatoreInserimento(lCodiceOperatore);
			lNotificheUfficioS.setDataInserimento(DateUtils.getSysDate());
			lNotificheUfficioS.setCodUfficioInserimento(lCodiceUfficio);
			String lDescrComuneUDS = this
					.getRequestStringParameter(ICostantiOrdineEsecuzione.CAMPO_COD_SEDE_UDS);
			lNotificheUfficioS
					.setUffCodUfficio(this.getCodUfficioByCodTipoUfficioDescrComune("UDS", lDescrComuneUDS));

			lNotificheUfficioS
					.setNote(this.getRequestStringParameter(ICostantiOrdineEsecuzione.CAMPO_NOTE_UDS));

			lNotArray[lNotifiche.length + 1] = lNotificheUfficioS;

			NotificaModel lNotMod = new NotificaModel();
			lNotMod.setCodTipoNotifica("N");
			// lNotMod.setDataInvio(lDataEmissione);
			lNotMod.setDataInvio(lDataTrasmissione);
			lNotMod.setCodEsito("-");
			lNotMod.setCodOperatoreInserimento(lCodiceOperatore);
			lNotMod.setDataInserimento(DateUtils.getSysDate());
			lNotMod.setCodUfficioInserimento(lCodiceUfficio);
			// String lTipoUfficio =
			// this.getRequestStringParameter(ICostantiOrdineEsecuzione.CAMPO_TIPO_UFFICIO_TDS);

			String lDescrComune = this
					.getRequestStringParameter(ICostantiOrdineEsecuzione.CAMPO_COD_SEDE_TDS);
			lNotMod.setUffCodUfficio(this.getCodUfficioByCodTipoUfficioDescrComune("TDS", lDescrComune));
			lNotMod.setNote(getRequestStringParameter(ICostantiOrdineEsecuzione.CAMPO_NOTE_TDS));
			lNotArray[lNotifiche.length + 2] = lNotMod;

			lEve.setNotifiche(lNotArray);

			lRetModel = lCtrl.ExInserisciOModificaLSNotifica(lEve, lPenaRes);
		} else
			lRetModel.setEvento(lCtrlEvento.ExInserisciEvento(lEve.getEvento()));

		String lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
				+ "=siap.siep.ordineesecuzione.action.ActDettaglioLSArrestiDomiciliari&"
				+ ICostantiEvento.CAMPO_ID_EVENTO + "=" + lRetModel.getEvento().getIdEvento()
				+ "&modalita=I&fc=" + lFc;

		return lPage;
	}

	/**
	 * Inserisci l'Ordine di Esecuzione Alfano per un Detenuto Altra Causa
	 * 
	 * @return
	 * @throws Exception
	 */
	private String InserisciLSAltraCausa() throws Exception {
		String lCodiceOperatore = this.getCodUtenteConnesso();
		String lCodiceUfficio = this.getCodUfficioUtenteConnesso();
		Date lDataEmissione = getRequestDateParameter(ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE,
				ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE, ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE);

		EventoNotificaModel lEve = new EventoNotificaModel();
		PenaResiduaModel lPenaRes = new PenaResiduaModel();

		String lIstPre = null;
		lIstPre = getRequestStringParameter("istanza");

		if (lIstPre.equals("N")) {
			lEve.getEvento().setDescrMotivo("LS-DAC");
		} else {
			lEve.getEvento().setDescrMotivo("LS-DET-I");
		}

		lEve.setEvento(setEventoOrdineEsecuzione(lEve.getEvento()));
		lEve.getMagistrato()
				.setCodMagistrato(getRequestStringParameter(ICostantiEvento.CAMPO_COD_MAGISTRATO));

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
			if (lIstPre.equals("S")) { // Se l'istanza è presenten setto l'eveIdEvento
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
				lNotificheUfficioS.setUffCodUfficio(
						this.getCodUfficioByCodTipoUfficioDescrComune("TDS", lDescrComuneTDS));

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
		if (lIstPre.equals("S")) {
			lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
					+ "=siap.siep.ordineesecuzione.action.ActDettaglioLSAltraCausaIstanzaProdotta&"
					+ ICostantiEvento.CAMPO_ID_EVENTO + "=" + lRetModel.getEvento().getIdEvento()
					+ "&modalita=I&fc=" + lFc;
		} else {
			lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
					+ "=siap.siep.ordineesecuzione.action.ActDettaglioLSAltraCausa&"
					+ ICostantiEvento.CAMPO_ID_EVENTO + "=" + lRetModel.getEvento().getIdEvento()
					+ "&modalita=I&fc=" + lFc;
		}

		return lPage;
	}

	/**
	 * Inserisci LS Altre Posizioni
	 * 
	 * @return
	 * @throws Exception
	 */
	private String InserisciLSAltrePosizioni() throws Exception {
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
				lNotificheUfficioS.setUffCodUfficio(
						this.getCodUfficioByCodTipoUfficioDescrComune("TDS", lDescrComuneTDS));

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