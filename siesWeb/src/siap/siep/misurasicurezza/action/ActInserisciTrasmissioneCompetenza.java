package siap.siep.misurasicurezza.action;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;

import siap.sico.SICOException;
import siap.sico.camponota.model.CampoNotaModel;
import siap.sico.decodifiche.model.ComuneModel;
import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoModel;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.magistrato.action.ICostantiMagistrato;
import siap.sico.ufficio.action.ICostantiUfficio;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siep.autoritaesterna.model.AutoritaEsternaModel;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.luogodetenzione.action.ICostantiLuogoDetenzione;
import siap.siep.notifica.action.ICostantiNotifica;
import siap.siep.notifica.model.NotificaModel;
import siap.siep.ordineesecuzione.controller.IOrdineEsecuzione;
import siap.siep.util.SIEPLookupRemote;
import siap.sius.SIUSException;
import f3b.util.DateUtils;
import f3b.web.IWebConstants;

/**
 * <p>
 * Title: ActInserisciTrasmissioneCompetenza
 * </p>
 * <p>
 * Description: Modulo che effettua l'inserimento del provvedimento di trasmissione atti per competenza ai
 * fini dell'esecuzione delle Misure di Sicurezza. I dati vengono salvati sulle tabelle: - EVENTO - NOTIFICA
 * (+AUTORITA_ESTERNA) - CAMPO_NOTA
 * 
 * - PENA_RESIDUA (non rideterminata ma duplicata e legata all'evento x dettaglio)
 * </p>
 * 
 * @version 1.0
 */
public class ActInserisciTrasmissioneCompetenza extends ActionSiap implements ICostantiMisuraSicurezza {

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String processRequest() throws Exception {

		FascicoloSiepModel lFascicoloModel = (FascicoloSiepModel) getSessionAttribute("fascicolo");

		EventoNotificaModel lEve = new EventoNotificaModel();

		// setto il tipo provvedimento
		lEve.getEvento().setCodTipoEvento("01");
		lEve.getEvento().setCodTipoProvvedimento(
				getRequestStringParameter(ICostantiEvento.CAMPO_COD_TIPO_PROVVEDIMENTO));
		lEve.getEvento().setCodMotivo(getRequestStringParameter(ICostantiEvento.CAMPO_COD_MOTIVO));

		lEve.getEvento().setFasSieIdFascicoloSiep(lFascicoloModel.getIdFascicoloSiep());

		Date lDataEmissione = getRequestDateParameter(ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE,
				ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE, ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE);
		lEve.getEvento().setDataEmissione(lDataEmissione);

		Date lDataTrasmissione = getRequestDateParameter(ICostantiNotifica.CAMPO_ANNO_DATA_INVIO,
				ICostantiNotifica.CAMPO_MESE_DATA_INVIO, ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO);
		lEve.getEvento().setDataTrasmissioneAtti(lDataTrasmissione);

		// Gestisco l'inserimento del firmatario
		lEve.getEvento()
				.setCodMagistrato(getRequestStringParameter(ICostantiMagistrato.CAMPO_COD_MAGISTRATO));

		lEve.getEvento().setCodOperatoreInserimento(getCodUtenteConnesso());
		lEve.getEvento().setDataInserimento(DateUtils.getSysDate());
		lEve.getEvento().setCodUfficioInserimento(getCodUfficioUtenteConnesso());

		lEve.getEvento().setCodLuogoEmittente(getUfficioUtenteConnesso().getCodComune());
		lEve.getEvento().setCodUfficioEmittente(getUfficioUtenteConnesso().getCodUfficio());
		lEve.getEvento().setAnnoProtocollo(new BigDecimal(DateUtils.getSysDate("yyyy")));
		lEve.getEvento().setCodEsito("-");
		lEve.getEvento().setCodLuogoDestinatario("-");
		lEve.getEvento().setCodTipoUfficioDestinatario("-");

		lEve.getEvento().setFlagStampaSiep("S");
		lEve.getEvento().setFlagVideoSiep("S");
		// lEve.getEvento().setFlagDocumentoRegistrato("N"); // inizialmente a Null, 'N' in fase di
		// inserimento del blob

		// Recupero i dati dell'ufficio Competente
		// UFFICIO COMPETENZA
		String lCodiceUffComp = getCodUfficioByCodTipoUfficioDescrComune(
				getRequestStringParameter(ICostantiEvento.CAMPO_COD_TIPO_UFFICIO_DESTINATARIO),
				getRequestStringParameter(ICostantiEvento.CAMPO_COD_LUOGO_DESTINATARIO));

		// ComuneModel lComModAutComp = new
		// ComuneModel(getCodComuneByDescr(getRequestStringParameter(ICostantiEvento.CAMPO_COD_LUOGO_DESTINATARIO)));

		lEve.getEvento().setCodUfficioDestinatario(lCodiceUffComp);
		lEve.getEvento().setCodTipoUfficioDestinatario(
				getRequestStringParameter(ICostantiEvento.CAMPO_COD_TIPO_UFFICIO_DESTINATARIO));

		if (lCodiceUffComp.equals(getCodUfficioUtenteConnesso())) {
			// Errore l'ufficio non può trasmettere gli atti a se stesso
			throw new SIUSException(
					SICOException.USER_MESSAGE,
					"Non è possibile trasmettere gli atti a se stessi.<br> Verificare di aver selezionato correttamente l'ufficio destinatario.");
		}

		// =======================================================
		// imposto il campo contenuto nella tabella CampoNote
		// =======================================================
		if (!this.isRequestParameterNullObj("camponote") && getRequestStringParameter("camponote") != null
				&& !getRequestStringParameter("camponote").equals("")) {
			ArrayList lCampoNote = new ArrayList();

			CampoNotaModel lCampMod = new CampoNotaModel();

			lCampMod.setCodOperatoreInserimento(getCodUtenteConnesso());
			lCampMod.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
			lCampMod.setDataInserimento(DateUtils.getSysDate());
			lCampMod.setDescr(getRequestStringParameter("camponote"));

			lCampoNote.add(lCampMod);

			lEve.setCampoNote((CampoNotaModel[]) lCampoNote.toArray(new CampoNotaModel[0]));
		}

		// ==========================
		// Preparo le notifiche
		// ==========================
		ArrayList lNotifiche = new ArrayList();

		// ==========================================================================
		// All'ufficio per l'esecuzione
		// ==========================================================================
		NotificaModel lNotUffComp = new NotificaModel();

		lNotUffComp.setCodTipoNotifica("E"); // Per l'esecuzione
		lNotUffComp.setDataInvio(lDataTrasmissione);
		lNotUffComp.setCodEsito("-");
		lNotUffComp.setCodOperatoreInserimento(getCodUtenteConnesso());
		lNotUffComp.setDataInserimento(DateUtils.getSysDate());
		lNotUffComp.setCodUfficioInserimento(getCodUfficioUtenteConnesso());

		lNotUffComp.setUffCodUfficio(lCodiceUffComp); // Cod Uff Destinatario

		lNotifiche.add(lNotUffComp);

		// Eventuale Ufficio di Sorveglianza
		if (!isRequestParameterNullObj("tipoUDS") && !getRequestStringParameter("tipoUDS").equals("-")) {
			String lTipoUds = getRequestStringParameter("tipoUDS");
			String lComuneUds = getRequestStringParameter(ICostantiUfficio.CAMPO_SEDE_UFFICIO);
			String lCodUffUDS = getCodUfficioByCodTipoUfficioDescrComune(lTipoUds, lComuneUds);

			NotificaModel lNotUffUDS = new NotificaModel();

			lNotUffUDS.setCodTipoNotifica("C"); // Per l'esecuzione
			lNotUffUDS.setDataInvio(lDataTrasmissione);
			lNotUffUDS.setCodEsito("-");
			lNotUffUDS.setCodOperatoreInserimento(getCodUtenteConnesso());
			lNotUffUDS.setDataInserimento(DateUtils.getSysDate());
			lNotUffUDS.setCodUfficioInserimento(getCodUfficioUtenteConnesso());

			lNotUffUDS.setUffCodUfficio(lCodUffUDS);

			lNotifiche.add(lNotUffUDS);
		}

		// Istituto di detenzaione se presente e selezionato in maschera
		if (!isRequestParameterNullObj(ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE)
				&& getRequestStringParameter(ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE)
						.length() > 0) {
			String lIdIstituto = getRequestStringParameter(ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE);

			NotificaModel lNotIstituto = new NotificaModel();

			lNotIstituto.setCodTipoNotifica("C"); // Per l'esecuzione
			lNotIstituto.setDataInvio(lDataTrasmissione);
			lNotIstituto.setCodEsito("-");
			lNotIstituto.setCodOperatoreInserimento(getCodUtenteConnesso());
			lNotIstituto.setDataInserimento(DateUtils.getSysDate());
			lNotIstituto.setCodUfficioInserimento(getCodUfficioUtenteConnesso());

			lNotIstituto.setIstDetIdIstitutoDetenzione(lIdIstituto);

			lNotifiche.add(lNotIstituto);
		}

		// ALTRO DESTINATARIO
		if (!isRequestParameterNullObj("AltroDestinatario")
				&& !getRequestStringParameter("AltroDestinatario").equals("-")) {
			NotificaModel lNotAltroDestinatario = new NotificaModel();

			lNotAltroDestinatario.setCodTipoNotifica("C");
			lNotAltroDestinatario.setDataInvio(lDataTrasmissione);
			lNotAltroDestinatario.setCodEsito("-");
			lNotAltroDestinatario.setCodOperatoreInserimento(getCodUtenteConnesso());
			lNotAltroDestinatario.setDataInserimento(DateUtils.getSysDate());
			lNotAltroDestinatario.setCodUfficioInserimento(getCodUfficioUtenteConnesso());

			AutoritaEsternaModel lAut = new AutoritaEsternaModel();
			lAut.setCodTipoAutorita(getRequestStringParameter("AltroDestinatario"));

			ComuneModel lComMod = new ComuneModel(
					getCodComuneByDescrFlagVal(getRequestStringParameter("SedeAltroDestinatario")));

			lAut.setCodSede(lComMod.getCodComune());
			lAut.setCodOperatoreInserimento(getCodUtenteConnesso());
			lAut.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
			lAut.setDataInserimento(DateUtils.getSysDate());

			// Setto l'Autorita Esterna per la notifica corrente
			lNotAltroDestinatario.setAutoritaEsterna(lAut);

			lNotifiche.add(lNotAltroDestinatario);
		}

		// Inserisco l'array di Notifiche nell'Evento
		lEve.setNotifiche((NotificaModel[]) lNotifiche.toArray(new NotificaModel[0]));

		// ===============================================
		// Sono in modifica cancello l'evento e lo reinserisco
		// ===============================================
		if (!isRequestParameterNullObj("modalita")) {
			if ("M".equals(getRequestStringParameter("modalita"))) {
				// Cancella l'evento precedente
				BigDecimal lIdEvento = getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO);
				EventoModel lEveModel = new EventoModel();

				IEvento lCtrlEvento = SICOLookupRemote.getEventoRemote();
				lEveModel = lCtrlEvento.ExRicercaEventoByKey(lIdEvento);

				IOrdineEsecuzione lOECtrl = SIEPLookupRemote.getOrdineEsecuzioneRemote();
				lOECtrl.ExCancellaEventoConStorePocedure(lEveModel);
			}
		}

		IEvento lCtrl = SICOLookupRemote.getEventoRemote();
		EventoNotificaModel lRetModel = lCtrl.ExInserisciEventoNotifica(lEve);
		// FIXME 'MS' duplicare la pena residua
		// EventoNotificaModel lRetModel = lCtrl.ExInserisciEventoNotifica(lEve, lPenaResidua);
		// ==================

		String lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
				+ "=siap.siep.misurasicurezza.action.ActLoadDettaglioTrasmissioneCompetenza&"
				+ ICostantiEvento.CAMPO_ID_EVENTO + "=" + lRetModel.getEvento().getIdEvento();

		return lPage;
	}

}