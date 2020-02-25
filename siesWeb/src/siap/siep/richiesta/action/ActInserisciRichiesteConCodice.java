package siap.siep.richiesta.action;

/**
 * <p>Title: ActInserisciRichiesteConCodice</p>
 * <p>Description: Classe Action per l'inserimento delle richieste al GE di Indulto
 *    e Amnistia</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: Bull</p>
 * @version 1.0
 */
import java.math.BigDecimal;
import java.util.ArrayList;

import siap.sico.decodifiche.model.ComuneModel;
import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.model.EventoModel;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.magistrato.action.ICostantiMagistrato;
import siap.sico.web.ActionSiap;
import siap.siep.annotazionemanuale.action.ICostantiAnnotazioneManuale;
import siap.siep.autoritaesterna.model.AutoritaEsternaModel;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.notifica.model.NotificaModel;
import siap.siep.richiesta.controller.IRichiesta;
import siap.siep.util.SIEPLookupRemote;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class ActInserisciRichiesteConCodice extends ActionSiap implements ICostantiRichiesta {

	public String processRequest() throws F3BException {

		BigDecimal lIdFascicolo = ((FascicoloSiepModel) getSessionAttribute("fascicolo"))
				.getIdFascicoloSiep();

		// FascicoloSiepModel lFascicoloModel = (FascicoloSiepModel) getSessionAttribute("fascicolo");
		String lCodiceOperatore = this.getCodUtenteConnesso();
		String lCodiceUfficio = this.getCodUfficioUtenteConnesso();
		EventoNotificaModel lRetModel = new EventoNotificaModel();
		String lPage = null;
		ArrayList lNotifiche = new ArrayList();

		BigDecimal lIdAnnotazioneManuale = this
				.getRequestBigDecimalParameter(ICostantiAnnotazioneManuale.CAMPO_ID_ANNOTAZIONE_MANUALE);

		String codice = this.getRequestStringParameter("codice");
		// String testo = this.getRequestStringParameter("testo");

		// INSERISCO EVENTO E NOTIFICA
		EventoNotificaModel lEveMod = new EventoNotificaModel();

		EventoModel lEveModel = new EventoModel();

		lEveModel.setCodUfficioEmittente(lCodiceUfficio);

		lEveModel.setCodTipoEvento("01");
		lEveModel.setCodTipoProvvedimento("26"); // Richiesta
		lEveModel.setCodMotivo(codice);

		lEveModel.setFasSieIdFascicoloSiep(lIdFascicolo);
		lEveModel.setCodLuogoEmittente(this.getCodComuneUtenteConnesso());
		lEveModel.setCodUfficioDestinatario("-");
		lEveModel.setCodTipoUfficioDestinatario("-");

		lEveModel.setCodLuogoDestinatario("-");
		lEveModel.setCodEsito("-");
		lEveModel.setDataEmissione(getRequestDateParameter(ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE,
				ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE, ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE));

		lEveModel.setDataTrasmissioneAtti(
				getRequestDateParameter(ICostantiEvento.CAMPO_ANNO_DATA_TRASMISSIONE_ATTI,
						ICostantiEvento.CAMPO_MESE_DATA_TRASMISSIONE_ATTI,
						ICostantiEvento.CAMPO_GIORNO_DATA_TRASMISSIONE_ATTI));

		lEveModel.setCodMagistrato(getRequestStringParameter(ICostantiMagistrato.CAMPO_COD_MAGISTRATO));
		lEveModel.setAnnIdAnnotazioneManuale(lIdAnnotazioneManuale);

		lEveModel.setCodOperatoreInserimento(lCodiceOperatore);
		lEveModel.setDataInserimento(DateUtils.getSysDate());
		lEveModel.setCodUfficioInserimento(lCodiceUfficio);
		lEveModel.setFlagStampaSiep("S");
		lEveModel.setFlagVideoSiep("S");

		lEveMod.setEvento(lEveModel);

		// setto le notifiche

		NotificaModel lNotMod = new NotificaModel();
		AutoritaEsternaModel lAut = new AutoritaEsternaModel();
		String lUfficio = null;
		lNotMod.setCodEsito("-");

		if (codice.equals("0296")) // Restituzione Ordine di Esecuzione ex art.672 c.p.p.
		{
			lAut.setCodTipoAutorita(this.getRequestStringParameter(ICostantiRichiesta.CAMPO_COD_AUTORITA));

			ComuneModel lComMod = new ComuneModel(getCodComuneByDescr(
					this.getRequestStringParameter(ICostantiRichiesta.CAMPO_SEDE_AUTORITA)));
			lAut.setCodSede(lComMod.getCodComune());
			lAut.setCodOperatoreInserimento(lCodiceOperatore);
			lAut.setCodUfficioInserimento(lCodiceUfficio);
			lAut.setDataInserimento(DateUtils.getSysDate());
			lNotMod.setAutoritaEsterna(lAut);

			lNotMod.setNote(getRequestStringParameter(ICostantiRichiesta.CAMPO_NOTE_AUTORITA));

			lNotifiche.add(lNotMod);

		} else {
			lUfficio = this.getCodUfficioByCodTipoUfficioDescrComune(
					this.getRequestStringParameter(ICostantiRichiesta.CAMPO_COD_UFFICIO),
					getRequestStringParameter(ICostantiRichiesta.CAMPO_SEDE_UFFICIO));

			lNotMod.setUffCodUfficio(lUfficio);
			lNotMod.setNote(getRequestStringParameter(ICostantiRichiesta.CAMPO_NOTE_UFFICIO));
			lNotifiche.add(lNotMod);
		}

		lNotMod.setCodOperatoreInserimento(lCodiceOperatore);
		lNotMod.setDataInserimento(DateUtils.getSysDate());
		lNotMod.setCodUfficioInserimento(lCodiceUfficio);
		lNotMod.setCodTipoNotifica("E");
		lNotMod.setDataInvio(getRequestDateParameter(ICostantiEvento.CAMPO_ANNO_DATA_TRASMISSIONE_ATTI,
				ICostantiEvento.CAMPO_MESE_DATA_TRASMISSIONE_ATTI,
				ICostantiEvento.CAMPO_GIORNO_DATA_TRASMISSIONE_ATTI));

		// lNotMod.setUffCodUfficio(lCodiceUff);

		lEveMod.setNotifiche((NotificaModel[]) lNotifiche.toArray(new NotificaModel[0]));

		IRichiesta lCtrlRich = SIEPLookupRemote.getRichiestaRemote();
		lRetModel = lCtrlRich.ExInserisciOModificaNotifica(lEveMod);
		lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
				+ "=siap.siep.richiesta.action.ActDettaglioRichiesteConCodice&"
				+ ICostantiEvento.CAMPO_ID_EVENTO + "=" + lRetModel.getEvento().getIdEvento();

		return lPage;
	}

}