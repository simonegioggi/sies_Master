package siap.siep.richiesta.action;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;

import siap.sico.camponota.model.CampoNotaModel;
import siap.sico.decodifiche.model.ComuneModel;
import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siep.autoritaesterna.action.ICostantiAutoritaEsterna;
import siap.siep.autoritaesterna.model.AutoritaEsternaModel;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.notifica.action.ICostantiNotifica;
import siap.siep.notifica.model.NotificaModel;
import f3b.util.DateUtils;
import f3b.web.IWebConstants;

/**
 * <p>
 * Title: ActInserisciRichiestaGenerica
 * </p>
 * <p>
 * Description: ActInserisciEsitoEspulsione
 * </p>
 * <p>
 * Copyright: Copyright (c) 2007
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @version 1.0
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class ActInserisciEsitoEspulsione extends ActionSiap implements ICostantiRichiesta {

	public String processRequest() throws Exception {

		EventoNotificaModel lEve = new EventoNotificaModel();

		lEve.getEvento().setCodTipoEvento("02"); // Tipo Evento = Richiesta
		lEve.getEvento().setCodTipoProvvedimento("26");
		lEve.getEvento().setCodMotivo("0319");

		FascicoloSiepModel lFascicoloModel = (FascicoloSiepModel) getSessionAttribute("fascicolo");
		lEve.getEvento().setFasSieIdFascicoloSiep(lFascicoloModel.getIdFascicoloSiep());

		Date lDataEmissione = getRequestDateParameter(ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE,
				ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE, ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE);
		lEve.getEvento().setDataEmissione(lDataEmissione);
		Date lDataTrasmissione = getRequestDateParameter(ICostantiNotifica.CAMPO_ANNO_DATA_INVIO,
				ICostantiNotifica.CAMPO_MESE_DATA_INVIO, ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO);
		lEve.getEvento().setDataTrasmissioneAtti(lDataTrasmissione);
		lEve.getEvento().setCodMagistrato(calcolaMagistrato());

		UfficioModel lUff = this.getUfficioUtenteConnesso();

		lEve.getEvento().setCodOperatoreInserimento(getCodUtenteConnesso());
		lEve.getEvento().setCodLuogoEmittente(lUff.getCodComune());
		lEve.getEvento().setCodUfficioEmittente(lUff.getCodUfficio());
		lEve.getEvento().setDataInserimento(DateUtils.getSysDate());
		lEve.getEvento().setCodUfficioInserimento(lUff.getCodUfficio());
		lEve.getEvento().setAnnoProtocollo(new BigDecimal(DateUtils.getSysDate("yyyy")));
		lEve.getEvento().setCodEsito("-");
		lEve.getEvento().setCodLuogoDestinatario("-");
		lEve.getEvento().setCodTipoUfficioDestinatario("-");
		lEve.getEvento().setFlagStampaSiep("N");
		lEve.getEvento().setFlagVideoSiep("S");

		// imposto il campo contenuto nella tabella CampoNote
		if (!this.isRequestParameterNullObj("camponote") && getRequestStringParameter("camponote") != null
				&& !getRequestStringParameter("camponote").equals("")) {
			ArrayList lCampoNote = new ArrayList();
			CampoNotaModel lCampMod = new CampoNotaModel();
			lCampMod.setCodOperatoreInserimento(getCodUtenteConnesso());
			lCampMod.setCodUfficioInserimento(lUff.getCodUfficio());
			lCampMod.setDataInserimento(DateUtils.getSysDate());
			lCampMod.setDescr(getRequestStringParameter("camponote"));
			lCampoNote.add(lCampMod);

			lEve.setCampoNote((CampoNotaModel[]) lCampoNote.toArray(new CampoNotaModel[0]));
		}

		// Preparo le notifiche
		ArrayList lNotifiche = new ArrayList();

		// autorità di polizia
		if (!isRequestParameterNullObj(ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA)
				&& getRequestStringParameter(ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA) != null
				&& !getRequestStringParameter(ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA).equals("-")) {
			String lPolizia = this
					.getRequestStringParameter(ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA);
			String lSedePolizia = this.getRequestStringParameter(ICostantiAutoritaEsterna.CAMPO_COD_SEDE);
			NotificaModel lNotModPol = new NotificaModel();

			// indirizzo
			if (!this.isRequestParameterNullObj(ICostantiNotifica.CAMPO_NOTE)) {
				String lNotePolizia = this.getRequestStringParameter(ICostantiNotifica.CAMPO_NOTE);
				lNotModPol.setNote(lNotePolizia);
			}

			lNotModPol.setCodEsito("-");
			lNotModPol.setCodOperatoreInserimento(getCodUtenteConnesso());
			lNotModPol.setDataInserimento(DateUtils.getSysDate());
			lNotModPol.setCodUfficioInserimento(lUff.getCodUfficio());
			lNotModPol.setCodTipoNotifica("N");
			lNotModPol.setDataInvio(lDataTrasmissione);

			AutoritaEsternaModel lAut = new AutoritaEsternaModel();
			lAut.setCodTipoAutorita(lPolizia);

			ComuneModel lComMod = new ComuneModel(getCodComuneByDescr(lSedePolizia));
			lAut.setCodSede(lComMod.getCodComune());
			lAut.setCodOperatoreInserimento(getCodUtenteConnesso());
			lAut.setCodUfficioInserimento(lUff.getCodUfficio());
			lAut.setDataInserimento(DateUtils.getSysDate());
			lNotModPol.setAutoritaEsterna(lAut);

			lNotifiche.add(lNotModPol);
		}

		// Inserisco l'array di Notifiche nell'Evento
		lEve.setNotifiche((NotificaModel[]) lNotifiche.toArray(new NotificaModel[0]));

		IEvento lCtrl = SICOLookupRemote.getEventoRemote();
		EventoNotificaModel lRetModel = lCtrl.ExInserisciEventoNotifica(lEve);

		String lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
				+ "=siap.siep.richiesta.action.ActDettaglioEsitoEspulsione&" + ICostantiEvento.CAMPO_ID_EVENTO
				+ "=" + lRetModel.getEvento().getIdEvento() + "&modalita=I";

		return lPage;
	}

}