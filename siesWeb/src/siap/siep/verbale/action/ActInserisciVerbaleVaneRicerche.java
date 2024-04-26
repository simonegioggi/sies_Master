package siap.siep.verbale.action;

import java.math.BigDecimal;

import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
import siap.sico.decodifiche.model.ComuneModel;
import siap.sico.evento.controller.IEventoSimeone;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.notifica.model.NotificaModel;
import siap.siep.scadenzario.model.ScadenzarioModel;
import siap.siep.util.SIEPLookupRemote;
import siap.siep.verbale.controller.IVerbale;
import siap.siep.verbale.model.VerbaleModel;

/**
 * Title: ActInserisciVerbale Description: Classe Action per l'inserimento di Verbale
 *
 * @version 1.0
 */
public class ActInserisciVerbaleVaneRicerche extends ActionSiap implements ICostantiVerbale {

	/**
	 * Azione di Inserimento del Verbale
	 *
	 * @return Nome della pagina JSP da visualizzare al termine dell'elaborazione
	 * @throws F3BException
	 */
	public String processRequest() throws F3BException {

		FascicoloSiepModel lFascMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");

		VerbaleModel lVerMod = new VerbaleModel();

		lVerMod.setCodTipoVerbale("02");
		lVerMod.setDataPervenimento(getRequestDateParameter(ICostantiVerbale.CAMPO_ANNO_DATA_PERVENIMENTO,
				ICostantiVerbale.CAMPO_MESE_DATA_PERVENIMENTO,
				ICostantiVerbale.CAMPO_GIORNO_DATA_PERVENIMENTO));
		lVerMod.setDataEmissione(getRequestDateParameter(ICostantiVerbale.CAMPO_ANNO_DATA_EMISSIONE,
				ICostantiVerbale.CAMPO_MESE_DATA_EMISSIONE, ICostantiVerbale.CAMPO_GIORNO_DATA_EMISSIONE));
		lVerMod.setCodTipoUfficioFirmatario(
				getRequestStringParameter(ICostantiVerbale.CAMPO_COD_TIPO_UFFICIO_FIRMATARIO));

		ComuneModel lComMod = new ComuneModel(getCodComuneByDescrFlagVal(
				getRequestStringParameter(ICostantiVerbale.CAMPO_COD_LUOGO_UFFICIO_FIRMATARIO)));
		lVerMod.setCodLuogoUfficioFirmatario(lComMod.getCodComune());

		lVerMod.setCodOperatoreInserimento(getCodUtenteConnesso());
		lVerMod.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
		lVerMod.setNote(getRequestStringParameter(ICostantiVerbale.CAMPO_NOTE));

		if (!isRequestParameterNullObj(ICostantiVerbale.CAMPO_NUMERO_PROTOCOLLO))
			lVerMod.setNumeroProtocollo(getRequestStringParameter(ICostantiVerbale.CAMPO_NUMERO_PROTOCOLLO));

		lVerMod.setDataInserimento(DateUtils.getSysDate());

		BigDecimal lIdFascicolo = ((FascicoloSiepModel) getSessionAttribute("fascicolo"))
				.getIdFascicoloSiep();

		// Model Evento
		NotificaModel lNotMod = new NotificaModel();
		lNotMod.setCodEsito("02");
		lNotMod.setCodiceOperatoreAggiornamento(getCodUtenteConnesso());
		lNotMod.setCodUfficioAggiornamento(getCodUfficioUtenteConnesso());
		lNotMod.setDataAggiornamento(DateUtils.getSysDate());

		// Scadenzario Vane Ricerche
		ScadenzarioModel lScaMod = new ScadenzarioModel();

		// Imposto fisso 03=Vane Ricerche
		lScaMod.setFasSieIdFascicoloSiep(lIdFascicolo);
		lScaMod.setDataAggiornamento(DateUtils.getSysDate());
		lScaMod.setCodOperatoreAggiornamento(getCodUtenteConnesso());
		lScaMod.setCodUfficioAggiornamento(getCodUfficioUtenteConnesso());
		lScaMod.setCodTipoScadenzario("03");

		// se proviene dalla maschera di omesse notifiche
		EventoNotificaModel lEveNot = null;
		// 2023.12.20 FlagOmesse è sempre presente come campo hidden nella jsp per cui il test era
		// SEMPRE true
		// if (!isRequestParameterNullObj("FlagOmesse")) {
		if (!isRequestParameterNullEmptyObj("FlagOmesse")) {
			// 2023.12.20 - FINE
			setRequestAttribute("FlagOmesse", getRequestStringParameter("FlagOmesse"));
			lEveNot = new EventoNotificaModel();
			IEventoSimeone lCtrl = SICOLookupRemote.getEventoSimeoneRemote();
			lEveNot = lCtrl.ExRicercaEventoNotificaByIdFascicoloDescrMotivo(lFascMod.getIdFascicoloSiep(),
					"LS");
		}

		// NEL CONTROLLER GESTISCE LO STATO PROCEDIMENTO
		IVerbale lCtrl = SIEPLookupRemote.getVerbaleRemote();
		VerbaleModel llVerModRet = lCtrl.ExInserisciVerbaleVaneRicerche(lFascMod.getIdFascicoloSiep(),
				lVerMod, lNotMod, lScaMod, lEveNot);

		setRequestAttribute("verbale", llVerModRet);

		// se proviene dalla maschera di omesse notifiche
		if (!isRequestParameterNullObj("FlagOmesse"))
			setRequestAttribute("FlagOmesse", getRequestStringParameter("FlagOmesse"));

		// MEV_2023-33 se proviene dalla maschera di omesse notifiche
		if (!isRequestParameterNullObj("idEventoOIPP")) {
			setRequestAttribute("idEventoOIPP", getRequestStringParameter("idEventoOIPP"));
		}
		// MEV_2023-33 FINE

		// Prepara la pagina di destinazione
		String lPage = "";
		lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
				+ "=siap.siep.verbale.action.ActLoadDettaglioVerbaleVaneRicerche&" + CAMPO_ID_VERBALE + "="
				+ llVerModRet.getIdVerbale().toString();
		return lPage;
	}

}