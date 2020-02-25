package siap.siep.misurasicurezza.action;

import java.math.BigDecimal;
import java.util.Date;

import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.magistrato.action.ICostantiMagistrato;
import siap.sico.util.SICOLookupRemote;
import siap.siep.misurasicurezza.controller.IMisuraSicurezza;
import siap.siep.notifica.model.NotificaModel;
import siap.siep.util.SIEPLookupRemote;
import f3b.util.DateUtils;
import f3b.web.IWebConstants;

/**
 *
 * <p>
 * Title: ActModificaOEInternamentoOrdineLiberazioneMS
 * </p>
 * <p>
 * Description: Classe Action per la Modifica dei provvedimenti di
 * </p>
 * <p>
 * Ordine di Esecuzione per Internamento, esecuzione MS e
 * </p>
 * <p>
 * Ordine Scarcerazione per Liberazione, esecuzione MS
 * </p>
 * <p>
 * Copyright: Copyright (c) 2014
 * </p>
 * <p>
 * Company: Intersistemi Italia S.P.A.
 * </p>
 * 
 * @author AMBROSINO
 */
public class ActModificaOEInternamentoOrdineLiberazioneMS extends ActNotificheMS implements
		ICostantiMisuraSicurezza {

	public String processRequest() throws Exception {

		BigDecimal lIdEve = getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO);

		// MEV_39: gestita la cancellazione
		if (!isRequestParameterNullObj("modalita") && "D".equals(getRequestStringParameter("modalita"))) {
			// valore di ritorno
			return IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
					+ "=siap.siep.ordineesecuzione.action.ActCancellaProvvedimento&"
					+ ICostantiEvento.CAMPO_ID_EVENTO + "=" + lIdEve;
		} else {
			String lPage = "";

			String lCodiceOperatore = this.getCodUtenteConnesso();
			String lCodiceUfficio = this.getCodUfficioUtenteConnesso();

			// EventoNotifica
			EventoNotificaModel lEve = new EventoNotificaModel();
			IEvento lCtrlEve = SICOLookupRemote.getEventoRemote();
			lEve = lCtrlEve.ExRicercaEventoNotificaByKey(lIdEve);

			Date lDataEmissione = getRequestDateParameter(ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE,
					ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE, ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE);

			Date lDataTrasmissione = getRequestDateParameter(
					ICostantiEvento.CAMPO_ANNO_DATA_TRASMISSIONE_ATTI,
					ICostantiEvento.CAMPO_MESE_DATA_TRASMISSIONE_ATTI,
					ICostantiEvento.CAMPO_GIORNO_DATA_TRASMISSIONE_ATTI);

			// String codidMis =
			// getRequestStringParameter(ICostantiMisuraSicurezza.CAMPO_ID_MISURA_SICUREZZA);

			lEve.getEvento().setDataEmissione(lDataEmissione);

			lEve.getEvento().setCodOperatoreAggiornamento(lCodiceOperatore);
			lEve.getEvento().setDataAggiornamento(DateUtils.getSysDate());
			lEve.getEvento().setCodUfficioAggiornamento(lCodiceUfficio);

			if (!isRequestParameterNullObj(ICostantiMagistrato.CAMPO_COD_MAGISTRATO)
					&& getRequestStringParameter(ICostantiMagistrato.CAMPO_COD_MAGISTRATO) != null
					&& !getRequestStringParameter(ICostantiMagistrato.CAMPO_COD_MAGISTRATO).equals("")) {
				lEve.getEvento().setCodMagistrato(
						getRequestStringParameter(ICostantiMagistrato.CAMPO_COD_MAGISTRATO));
				lEve.getMagistrato().setCodMagistrato(
						getRequestStringParameter(ICostantiMagistrato.CAMPO_COD_MAGISTRATO));
			} else {
				lEve.getEvento().setCodMagistrato(this.calcolaMagistrato());
				lEve.getMagistrato().setCodMagistrato(this.calcolaMagistrato());
			}

			if (this.isRequestChecked(CAMPO_FLAG_ESECUZIONE_IMMEDIATA))
				lEve.getEvento().setFlagPiuMeno("D");
			else
				lEve.getEvento().setFlagPiuMeno(null);

			// NotificaModel[] lNotifiche = (NotificaModel[]) lNotificheArray.toArray(new NotificaModel[0]);
			NotificaModel[] lNotifiche = setNotificheMS(lDataTrasmissione, lEve.getEvento().getCodMotivo());

			// ---> MODIFICA COMUNICAZIONE
			IMisuraSicurezza lCtrl = SIEPLookupRemote.getMisuraSicurezzaRemote();
			lEve.setNotifiche(lNotifiche);

			EventoNotificaModel lRetModel = new EventoNotificaModel();
			lRetModel = lCtrl.ExAggiornaEventoNotificheXComunicazioneMS(lEve);

			if (lEve.getEvento().getCodMotivo().compareTo("1128") == 0) {
				lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
						+ "=siap.siep.misurasicurezza.action.ActDettaglioOEInternamento&"
						+ ICostantiEvento.CAMPO_ID_EVENTO + "=" + lRetModel.getEvento().getIdEvento();
			} else if (lEve.getEvento().getCodMotivo().compareTo("1129") == 0) {
				lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
						+ "=siap.siep.misurasicurezza.action.ActDettaglioOrdineLiberazione&"
						+ ICostantiEvento.CAMPO_ID_EVENTO + "=" + lRetModel.getEvento().getIdEvento();
			} else {
				lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
						+ "=siap.sico.evento.action.ActDettaglioDocumento&" + ICostantiEvento.CAMPO_ID_EVENTO
						+ "=" + lRetModel.getEvento().getIdEvento();
			}

			return lPage;
		}
	} // Chiude processRequest

} // Chiude Action