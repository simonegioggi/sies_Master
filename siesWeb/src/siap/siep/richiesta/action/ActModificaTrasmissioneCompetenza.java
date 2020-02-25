package siap.siep.richiesta.action;

import java.util.ArrayList;
import java.util.Date;

import siap.sico.camponota.model.CampoNotaModel;
import siap.sico.decodifiche.model.ComuneModel;
import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.magistrato.action.ICostantiMagistrato;
import siap.sico.ufficio.controller.IUfficio;
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siep.autoritaesterna.model.AutoritaEsternaModel;
import siap.siep.competenza.action.ICostantiCompetenza;
import siap.siep.competenza.controller.ICompetenza;
import siap.siep.competenza.model.CompetenzaModel;
import siap.siep.notifica.action.ICostantiNotifica;
import siap.siep.notifica.model.NotificaModel;
import siap.siep.util.SIEPLookupRemote;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;


/**
* <p>Title: ActModificaTrasmissioneCompetenza</p>
* <p>Description: Classe Action per la modifica di Trasmissione Competenza</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
* @deprecated 19/01/2016 Classe non utilizzata in esercizio. Da controllare
*/
@SuppressWarnings({ "rawtypes", "unchecked" })
public class ActModificaTrasmissioneCompetenza extends ActionSiap implements ICostantiRichiesta
{

	public String processRequest() throws F3BException {

		// Prendo l'evento e ne modifico i campi
		IEvento lEveCtrl = SICOLookupRemote.getEventoRemote();
		EventoNotificaModel mEve = lEveCtrl
				.ExRicercaEventoNotificaByKey(getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO));

		mEve.getEvento().setCodTipoProvvedimento(
				getRequestStringParameter(ICostantiEvento.CAMPO_COD_TIPO_PROVVEDIMENTO));
		mEve.getEvento().setCodMotivo(getRequestStringParameter(ICostantiEvento.CAMPO_COD_MOTIVO));

		// data emissione
		Date lDataEmissione = getRequestDateParameter(ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE,
				ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE, ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE);
		mEve.getEvento().setDataEmissione(lDataEmissione);

		// data trasmissione
		Date lDataTrasmissione = getRequestDateParameter(ICostantiNotifica.CAMPO_ANNO_DATA_INVIO,
				ICostantiNotifica.CAMPO_MESE_DATA_INVIO, ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO);
		mEve.getEvento().setDataTrasmissioneAtti(lDataTrasmissione);

		// firmatario
		mEve.getEvento()
				.setCodMagistrato(getRequestStringParameter(ICostantiMagistrato.CAMPO_COD_MAGISTRATO));

		// campi aggiornamento
		UfficioModel lUff = this.getUfficioUtenteConnesso();
		mEve.getEvento().setCodOperatoreAggiornamento(getCodUtenteConnesso());
		mEve.getEvento().setDataAggiornamento(DateUtils.getSysDate());
		mEve.getEvento().setCodUfficioAggiornamento(lUff.getCodUfficio());

		// imposto il campo contenuto nella tabella CampoNote
		if (!this.isRequestParameterNullObj("camponote") && getRequestStringParameter("camponote") != null
				&& !getRequestStringParameter("camponote").equals("")) {

			ArrayList lCampoNote = new ArrayList();

			CampoNotaModel lCampMod = mEve.getCampoNote()[0];
			lCampMod.setCodOperatoreAggiornamento(getCodUtenteConnesso());
			lCampMod.setCodUfficioAggiornamento(lUff.getCodUfficio());
			lCampMod.setDataAggiornamento(DateUtils.getSysDate());
			lCampMod.setDescr("");
			lCampMod.setDescr(getRequestStringParameter("camponote"));
			lCampoNote.add(lCampMod);

			mEve.setCampoNote((CampoNotaModel[]) lCampoNote.toArray(new CampoNotaModel[0]));
		}

		// Preparo le notifiche
		ArrayList lNotifiche = new ArrayList();

		// ALTRO DESTINATARIO
		NotificaModel lNotAltroDestinatario = mEve.getNotifiche()[0];
		if (!isRequestParameterNullObj("AltroDestinatario")
				&& getRequestStringParameter("AltroDestinatario") != null) {

			lNotAltroDestinatario.setCodiceOperatoreAggiornamento(getCodUtenteConnesso());
			lNotAltroDestinatario.setDataAggiornamento(DateUtils.getSysDate());
			lNotAltroDestinatario.setCodUfficioAggiornamento(lUff.getCodUfficio());
			lNotAltroDestinatario.setNote(getRequestStringParameter("AltroDestinatario"));

			// ===
			AutoritaEsternaModel lAut = new AutoritaEsternaModel();
			lAut.setCodTipoAutorita(getRequestStringParameter("AltroDestinatario"));

			ComuneModel lComMod = new ComuneModel(
					getCodComuneByDescr(getRequestStringParameter("SedeAltroDestinatario")));

			lAut.setCodSede(lComMod.getCodComune());
			lAut.setCodOperatoreInserimento(getCodUtenteConnesso());
			lAut.setCodUfficioInserimento(lUff.getCodUfficio());
			lAut.setDataInserimento(DateUtils.getSysDate());

			// Setto l'Autorita Esterna per la notifica corrente
			lNotAltroDestinatario.setAutoritaEsterna(lAut);
			// ===
			lNotifiche.add(lNotAltroDestinatario);
		}
		// Inserisco l'array di Notifiche nell'Evento
		mEve.setNotifiche((NotificaModel[]) lNotifiche.toArray(new NotificaModel[0]));

		IEvento lCtrl = SICOLookupRemote.getEventoRemote();
		if (lNotAltroDestinatario == null) {
			lCtrl.ExModificaEvento(mEve.getEvento());
		} else {
			lCtrl.ExModificaEventoNotificheCampoNote(mEve.getEvento(), mEve.getNotifiche()[0],
					mEve.getCampoNote()[0]);
		}

		// modifiche in Competenza
		ICompetenza lCompCtrl = SIEPLookupRemote.getCompetenzaRemote();
		CompetenzaModel mComp = lCompCtrl.ExRicercaCompetenzaById(
				getRequestBigDecimalParameter(ICostantiCompetenza.CAMPO_ID_COMPETENZA));

		mComp.setCodOperatoreAggiornamento(mEve.getEvento().getCodOperatoreAggiornamento());
		mComp.setDataAggiornamento(mEve.getEvento().getDataAggiornamento());
		mComp.setCodUfficioAggiornamento(mEve.getEvento().getCodUfficioAggiornamento());

		if (mComp != null && mComp.getFasSieIdFascicoloSiep() == null) {

			mComp.setCodTipoProvvedimento(
					getRequestStringParameter(ICostantiCompetenza.CAMPO_COD_TIPO_PROVVEDIMENTO));
			mComp.setDataProvvedimento(
					getRequestDateParameter(ICostantiCompetenza.CAMPO_ANNO_DATA_INSERIMENTO,
							ICostantiCompetenza.CAMPO_MESE_DATA_INSERIMENTO,
							ICostantiCompetenza.CAMPO_GIORNO_DATA_INSERIMENTO));
			mComp.setCodTipoAutoritaEmittente(
					getRequestStringParameter(ICostantiCompetenza.CAMPO_COD_TIPO_AUTORITA_EMITTENTE));

			mComp.setNumSezioneAutoritaEmittente(
					getRequestStringParameter(ICostantiCompetenza.CAMPO_NUM_SEZIONE_AUTORITA_EMITTENTE));
			mComp.setDataIrrevocabilita(
					getRequestDateParameter(ICostantiCompetenza.CAMPO_ANNO_DATA_IRREVOCABILITA,
							ICostantiCompetenza.CAMPO_MESE_DATA_IRREVOCABILITA,
							ICostantiCompetenza.CAMPO_GIORNO_DATA_IRREVOCABILITA));

			mComp.setChiaveAnno(getRequestBigDecimalParameter(ICostantiCompetenza.CAMPO_CHIAVE_ANNO));
			mComp.setChiaveProgr(getRequestBigDecimalParameter(ICostantiCompetenza.CAMPO_CHIAVE_PROGR));

			// Ricerco la chiave dell'ufficio che ha emesso la sentenza
			IUfficio lCtrlUff = SICOLookupRemote.getUfficioRemote();
			UfficioModel mUfficio = lCtrlUff.getUfficioByCodTipoUffDescrComune(
					getRequestStringParameter(ICostantiCompetenza.CAMPO_COD_TIPO_AUTORITA_EMITTENTE),
					getRequestStringParameter(ICostantiCompetenza.CAMPO_LUOGO_UFFICIO_TROVATO_SENTENZA));

			mComp.setCodLuogoEmittente(mUfficio.getCodComune());

			// UFFICIO COMPETENZA
			UfficioModel mUfficioComp = lCtrlUff.getUfficioByCodTipoUffDescrComune(
					getRequestStringParameter(ICostantiCompetenza.CAMPO_COD_LUOGO_EMITTENTE),
					getRequestStringParameter(ICostantiCompetenza.CAMPO_LUOGO_UFFICIO_TROVATO));

			mComp.setCodTipoAutoritaComp(mUfficioComp.getCodTipoUfficio());
			mComp.setCodLuogoAutoritaComp(mUfficioComp.getCodComune());

		}

		// modifico la tab competenza
		lCompCtrl.ExModificaCompetenza(mComp);

		String lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
				+ "=siap.siep.richiesta.action.ActDettaglioTrasmissioneCompetenza&"
				+ ICostantiEvento.CAMPO_ID_EVENTO + "=" + mEve.getEvento().getIdEvento() + "&"
				+ ICostantiCompetenza.CAMPO_ID_COMPETENZA + "=" + mComp.getIdCompetenza() + "&modalita=I";

		return lPage;
	}

}