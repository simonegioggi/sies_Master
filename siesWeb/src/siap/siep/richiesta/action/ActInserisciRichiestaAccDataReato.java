package siap.siep.richiesta.action;

/**
 * <p>Title: ActInserisciRichiestaAccDataReato</p>
 * <p>Description: Classe Action per l'inserimento di Richiesta di Accertamento
 *    Data commesso reato</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: Bull</p>
 * @version 1.0
 */
import java.math.BigDecimal;
import java.util.ArrayList;

import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.model.EventoModel;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.magistrato.action.ICostantiMagistrato;
import siap.sico.web.ActionSiap;
import siap.siep.annotazionemanuale.action.ICostantiAnnotazioneManuale;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.notifica.model.NotificaModel;
import siap.siep.richiesta.controller.IRichiesta;
import siap.siep.util.SIEPLookupRemote;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class ActInserisciRichiestaAccDataReato extends ActionSiap implements ICostantiRichiesta {

	public String processRequest() throws F3BException {

		FascicoloSiepModel lFascicoloModel = (FascicoloSiepModel) getSessionAttribute("fascicolo");

		String lCodiceOperatore = this.getCodUtenteConnesso();
		String lCodiceUfficio = this.getCodUfficioUtenteConnesso();

		EventoNotificaModel lRetModel = new EventoNotificaModel();

		String lPage = null;

		ArrayList lNotifiche = new ArrayList();

		BigDecimal lIdAnnotazioneManuale = this
				.getRequestBigDecimalParameter(ICostantiAnnotazioneManuale.CAMPO_ID_ANNOTAZIONE_MANUALE);

		EventoModel lEveModel = new EventoModel();

		// INSERISCO EVENTO E NOTIFICA
		EventoNotificaModel lEveMod = new EventoNotificaModel();

		lEveModel.setCodUfficioEmittente(lCodiceUfficio);
		lEveModel.setCodTipoEvento("01");
		lEveModel.setCodTipoProvvedimento("26");
		lEveModel.setCodMotivo("0297"); // 0297 - Accertamento data Commesso Reato
		lEveModel.setFasSieIdFascicoloSiep(lFascicoloModel.getIdFascicoloSiep());
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
		// lEve.setFlagDocumentoRegistrato("N");

		lEveMod.setEvento(lEveModel);

		// setto le notifiche
		String lPolizia = this.getRequestStringParameter(ICostantiRichiesta.CAMPO_COD_UFFICIO);
		NotificaModel lNotModPol = new NotificaModel();

		lNotModPol.setCodEsito("-");
		lNotModPol.setCodOperatoreInserimento(lCodiceOperatore);
		lNotModPol.setDataInserimento(DateUtils.getSysDate());
		lNotModPol.setCodUfficioInserimento(lCodiceUfficio);
		lNotModPol.setCodTipoNotifica("E");
		lNotModPol.setDataInvio(getRequestDateParameter(ICostantiEvento.CAMPO_ANNO_DATA_TRASMISSIONE_ATTI,
				ICostantiEvento.CAMPO_MESE_DATA_TRASMISSIONE_ATTI,
				ICostantiEvento.CAMPO_GIORNO_DATA_TRASMISSIONE_ATTI));
		String lUDS = this.getCodUfficioByCodTipoUfficioDescrComune(lPolizia,
				getRequestStringParameter(ICostantiRichiesta.CAMPO_SEDE_UFFICIO));

		lNotModPol.setUffCodUfficio(lUDS);
		lNotifiche.add(lNotModPol);

		lEveMod.setNotifiche((NotificaModel[]) lNotifiche.toArray(new NotificaModel[0]));

		IRichiesta lCtrlRich = SIEPLookupRemote.getRichiestaRemote();
		lRetModel = lCtrlRich.ExInserisciOModificaNotifica(lEveMod);

		lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
				+ "=siap.siep.richiesta.action.ActDettaglioRichAccDataReato&"
				+ ICostantiEvento.CAMPO_ID_EVENTO + "=" + lRetModel.getEvento().getIdEvento();

		return lPage;
	}

}