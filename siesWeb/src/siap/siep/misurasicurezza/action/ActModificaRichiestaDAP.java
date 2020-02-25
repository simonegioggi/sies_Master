package siap.siep.misurasicurezza.action;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;

import siap.sico.camponota.model.CampoNotaModel;
import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.magistrato.action.ICostantiMagistrato;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.misurasicurezza.controller.IMisuraSicurezza;
import siap.siep.notifica.action.ICostantiNotifica;
import siap.siep.util.SIEPLookupRemote;
import f3b.util.DateUtils;
import f3b.web.IWebConstants;

/**
 *
 * <p>
 * Title: ActModificaRichiestaDAP
 * </p>
 * <p>
 * Description: form per la modifica Richiesta al DAP
 * </p>
 * <p>
 * della designazione Istituto REMS per
 * </p>
 * <p>
 * la fase istruttoria Misura di Sicurezza
 * </p>
 * <p>
 * Copyright: Copyright (c) 2014
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author AMBROS
 */
public class ActModificaRichiestaDAP extends ActionSiap implements ICostantiEvento {

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String processRequest() throws Exception {

		BigDecimal lIdEve = getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO);

		// MEV_39: gestita la cancellazione
		if (!isRequestParameterNullObj("modalita") && "D".equals(getRequestStringParameter("modalita"))) {
			// valore di ritorno
			return IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
					+ "=siap.siep.ordineesecuzione.action.ActCancellaProvvedimento&"
					+ ICostantiEvento.CAMPO_ID_EVENTO + "=" + lIdEve;
		} else {
			FascicoloSiepModel lFascicoloModel = (FascicoloSiepModel) getSessionAttribute("fascicolo");

			String lPage = "";

			// EventoNotifica
			EventoNotificaModel lEve = new EventoNotificaModel();
			IEvento lCtrlEve = SICOLookupRemote.getEventoRemote();
			lEve = lCtrlEve.ExRicercaEventoNotificaByKey(lIdEve);

			String lCodiceOperatore = this.getCodUtenteConnesso();
			String lCodiceUfficio = this.getCodUfficioUtenteConnesso();
			Date lDataEmissione = getRequestDateParameter(ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE,
					ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE, ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE);

			// Evento: Data Emissione
			lEve.getEvento().setDataEmissione(lDataEmissione);

			lEve.getEvento().setCodOperatoreAggiornamento(lCodiceOperatore);
			lEve.getEvento().setDataAggiornamento(DateUtils.getSysDate());
			lEve.getEvento().setCodUfficioAggiornamento(lCodiceUfficio);
			//
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

			// CAMPONOTE
			String lDescr = "";
			if (!this.isRequestParameterNullObj(ICostantiNotifica.CAMPO_NOTE)
					&& getRequestStringParameter(ICostantiNotifica.CAMPO_NOTE) != null) {
				lDescr = getRequestStringParameter(ICostantiNotifica.CAMPO_NOTE);
			}

			if (lEve.getCampoNote() != null && lEve.getCampoNote().length > 0) {
				lEve.getCampoNote()[0].setDescr(getRequestStringParameter(ICostantiNotifica.CAMPO_NOTE));
				lEve.getCampoNote()[0].setCodOperatoreAggiornamento(getCodUtenteConnesso());
				lEve.getCampoNote()[0].setCodUfficioAggiornamento(lCodiceUfficio);
				lEve.getCampoNote()[0].setDataAggiornamento(DateUtils.getSysDate());
			} else {
				ArrayList lCampoNote = new ArrayList();
				CampoNotaModel lCampMod = new CampoNotaModel();
				lCampMod.setCodOperatoreInserimento(getCodUtenteConnesso());
				lCampMod.setCodUfficioInserimento(lCodiceUfficio);
				lCampMod.setDataInserimento(DateUtils.getSysDate());
				lCampMod.setCodOperatoreAggiornamento(getCodUtenteConnesso());
				lCampMod.setCodUfficioAggiornamento(lCodiceUfficio);
				lCampMod.setDataAggiornamento(DateUtils.getSysDate());

				lCampMod.setDescr(getRequestStringParameter(ICostantiNotifica.CAMPO_NOTE));
				lCampMod.setFasSieIdFascicoloSiep(lFascicoloModel.getIdFascicoloSiep());
				lCampoNote.add(lCampMod);

				lEve.setCampoNote((CampoNotaModel[]) lCampoNote.toArray(new CampoNotaModel[0]));
			}

			// Notifiche: è un valore fisso nella form e NON è MODIFICABILE

			// ---> INSERIMENTO RICHIESTA
			IMisuraSicurezza lCtrl = SIEPLookupRemote.getMisuraSicurezzaRemote();
			EventoNotificaModel lRetModel = new EventoNotificaModel();
			lRetModel = lCtrl.ExAggiornaEventoCampoNotaXRichiestaDapMS(lEve, lDescr);

			lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
					+ "=siap.siep.misurasicurezza.action.ActDettaglioRichiestaDAP&"
					+ ICostantiEvento.CAMPO_ID_EVENTO + "=" + lRetModel.getEvento().getIdEvento();

			return lPage;
		}
	} // Chiude processRequest

} // Chiude Action