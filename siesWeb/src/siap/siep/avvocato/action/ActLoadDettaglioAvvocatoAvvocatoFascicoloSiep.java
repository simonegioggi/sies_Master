package siap.siep.avvocato.action;

import java.math.BigDecimal;

import f3b.web.IWebConstants;
import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siep.avvocato.controller.IAvvocato;
import siap.siep.avvocato.model.AvvocatoFascicoloSiepModel;
import siap.siep.avvocato.model.AvvocatoSiepModel;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.istitutodetenzione.controller.IIstitutoDetenzione;
import siap.siep.istitutodetenzione.model.IstitutoDetenzioneModel;
import siap.siep.notifica.model.NotificaModel;
import siap.siep.util.SIEPLookupRemote;

public class ActLoadDettaglioAvvocatoAvvocatoFascicoloSiep extends ActionSiap implements ICostantiAvvocato {

	public String processRequest() throws Exception {

		// paramentro passato solo nel caso di iscrizione guidata
		if (!isRequestAttributeNullObj("lTipoFunzione"))
			setRequestAttribute("lTipoFunzione", getRequestAttribute("lTipoFunzione"));

		// paramentro passato solo nel caso di iscrizione guidata E VENGO DA DETTAGLIO SOGGETTO
		if (!isRequestParameterNullObj("lTipoFunzione"))
			setRequestAttribute("lTipoFunzione", getRequestStringParameter("lTipoFunzione"));

		BigDecimal lAvvFasId = null;
		BigDecimal lIdEvento = null;
		if (!isRequestParameterNullObj("idAvvFascicoloSiep")) {
			lAvvFasId = getRequestBigDecimalParameter("idAvvFascicoloSiep");

			// PAOLO 5/12/2009
			// provo ad inserire bottone di ritorno per elenco storico difensori
			// nessun bottone per elenco PM
			// attenzione a non ricoprire il ritorno per l'OE
			gestioneRitorno();

			if (!isRequestParameterNullObj("NomeAzione"))
				setRequestAttribute("NomeAzione", getRequestStringParameter("NomeAzione"));
		} else {
			lIdEvento = getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO);
			IEvento lCtrl = SICOLookupRemote.getEventoRemote();
			EventoNotificaModel lNotEveMod = lCtrl.ExRicercaEventoNotificaByKey(lIdEvento);
			if (lNotEveMod.getNotifiche() != null && lNotEveMod.getNotifiche().length > 0) {
				NotificaModel lNot = new NotificaModel(lNotEveMod.getNotifiche()[0]);
				if (lNot != null && lNot.getAvvIdAvvocatoFascicoloSiep() != null) {
					IAvvocato lAvvCtrl = SIEPLookupRemote.getAvvocatoRemote();
					AvvocatoSiepModel lAvvocato = lAvvCtrl
							.ExRicercaAvvocatoByKeyAvvocatoFasSiep(lNot.getAvvIdAvvocatoFascicoloSiep());
					lAvvFasId = lAvvocato.getAvvocatoFascicoloSiepModel().getIdAvvocatoFascicoloSiep();
					setRequestAttribute("lEve", lNotEveMod);
				}
			}
		}

		// riempie il model
		AvvocatoFascicoloSiepModel lAvvFascMod = new AvvocatoFascicoloSiepModel();
		lAvvFascMod.setFasSieIdFascicoloSiep(
				((FascicoloSiepModel) (getSessionAttribute("fascicolo"))).getIdFascicoloSiep());

		if (lAvvFasId != null) {
			IAvvocato lCtrl = SIEPLookupRemote.getAvvocatoRemote();
			AvvocatoSiepModel lAvv = lCtrl.ExRicercaAvvocatoByKeyAvvocatoFasSiep(lAvvFasId);

			IIstitutoDetenzione lCtrlIstituto = SIEPLookupRemote.getIstitutoDetenzioneRemote();
			IstitutoDetenzioneModel lIstMod = new IstitutoDetenzioneModel();

			// 20210610 MEV_21 Risoluzione errore dopo inserimento Avv. da RegInde
			if (lAvv.getAvvocatoFascicoloSiepModel() != null
					&& lAvv.getAvvocatoFascicoloSiepModel().getIstDetIdIstitutoDetenzione() != null)
				lIstMod = lCtrlIstituto.ExRicercaIstitutoDetenzioneByKey(
						lAvv.getAvvocatoFascicoloSiepModel().getIstDetIdIstitutoDetenzione());

			setRequestAttribute("avvocato", lAvv);
			setRequestAttribute("lIstMod", lIstMod);
			setRequestAttribute("modalita", "D");
			return PG_DETTAGLIO_AVVOCATO_AVVOCATO_FASCICOLO_SIEP;
		} else {
			String lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
					+ "=siap.sico.evento.action.ActDettaglioDocumento&" + ICostantiEvento.CAMPO_ID_EVENTO
					+ "=" + lIdEvento;
			return lPage; // restituisce la jsp di VIEW
		}
	}

}