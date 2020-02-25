package siap.siep.misurasicurezza.action;

import java.math.BigDecimal;
import java.util.Date;

import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoModel;
import siap.sico.evento.model.EventoVerbaleModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siep.luogodetenzione.action.ICostantiLuogoDetenzione;
import siap.siep.misurasicurezza.controller.IMisuraSicurezza;
import siap.siep.util.SIEPLookupRemote;
import siap.siep.verbale.action.ICostantiVerbale;
import siap.siep.verbale.controller.IVerbale;
import siap.siep.verbale.model.VerbaleModel;
import f3b.util.DateUtils;
import f3b.web.IWebConstants;

/**
 *
 * <p>
 * Title: ActModificaDesignazioneIstituto
 * </p>
 * <p>
 * Description: Classe per la modifica Designazione Istituto REMS,
 * </p>
 * *
 * <p>
 * nella la fase di istruttoria Misure di Sicurezza
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
public class ActModificaDesignazioneIstituto extends ActionSiap implements ICostantiMisuraSicurezza {

	public String processRequest() throws Exception {

		// FascicoloSiepModel lFascicoloModel = (FascicoloSiepModel) getSessionAttribute("fascicolo");
		String lPage = "";
		BigDecimal lIdEve = getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO);

		// MEV_39: gestita la cancellazione
		if (!isRequestParameterNullObj("modalita") && "D".equals(getRequestStringParameter("modalita"))) {
			// valore di ritorno
			return IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
					+ "=siap.siep.ordineesecuzione.action.ActCancellaProvvedimento&"
					+ ICostantiEvento.CAMPO_ID_EVENTO + "=" + lIdEve;
		} else {
			// EventoVerbale
			EventoVerbaleModel lEveVerMod = new EventoVerbaleModel();
			IEvento lCtrlV = SICOLookupRemote.getEventoRemote();
			lEveVerMod = lCtrlV.ExRicercaEventoVerbaleByIdEve(lIdEve);

			String lCodiceOperatore = this.getCodUtenteConnesso();
			String lCodiceUfficio = this.getCodUfficioUtenteConnesso();

			Date lDataEmissione = getRequestDateParameter(ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE,
					ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE, ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE);

			Date lDataPervenimento = getRequestDateParameter(ICostantiVerbale.CAMPO_ANNO_DATA_PERVENIMENTO,
					ICostantiVerbale.CAMPO_MESE_DATA_PERVENIMENTO,
					ICostantiVerbale.CAMPO_GIORNO_DATA_PERVENIMENTO);
			Date lDataDesignazione = getRequestDateParameter(
					ICostantiMisuraSicurezza.CAMPO_ANNO_DATA_DEFINIZIONE,
					ICostantiMisuraSicurezza.CAMPO_MESE_DATA_DEFINIZIONE,
					ICostantiMisuraSicurezza.CAMPO_GIORNO_DATA_DEFINIZIONE);

			// Evento
			EventoModel lEveMod = lEveVerMod.getEvento();

			lEveMod.setFlagStampaSiep("S");
			lEveMod.setFlagVideoSiep("S");
			lEveMod.setCodEsito("-");
			lEveMod.setCodLuogoDestinatario("-");

			lEveMod.setDataEmissione(lDataEmissione);
			lEveMod.setDataRicezioneAtti(lDataPervenimento);

			lEveMod.setCodOperatoreAggiornamento(lCodiceOperatore);
			lEveMod.setDataAggiornamento(DateUtils.getSysDate());
			lEveMod.setCodUfficioAggiornamento(lCodiceUfficio);

			// Verbale
			// Devo Ricercare il Verbale perchè 'ExRicercaEventoVerbaleByIdEve' NON RIPORTA TUTTI I CAMPI di
			// VERBALE
			IVerbale lCtrlVer = SIEPLookupRemote.getVerbaleRemote();
			VerbaleModel lVerMod = null;
			lVerMod = (VerbaleModel) lCtrlVer.ExRicercaVerbaleByIdEvento(lIdEve);

			lVerMod.setDataPervenimento(lDataPervenimento);
			lVerMod.setDataEmissione(lDataDesignazione);
			lVerMod.setIstDetIdIstitutoDetenzione(this
					.getRequestStringParameter(ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE));

			lVerMod.setCodOperatoreAggiornamento(getCodUtenteConnesso());
			lVerMod.setCodUfficioAggiornamento(getCodUfficioUtenteConnesso());
			lVerMod.setDataAggiornamento(DateUtils.getSysDate());

			if (!this.isRequestParameterNullObj(ICostantiVerbale.CAMPO_NUMERO_PROTOCOLLO)
					&& getRequestStringParameter(ICostantiVerbale.CAMPO_NUMERO_PROTOCOLLO) != null) {
				lVerMod.setNumeroProtocollo(getRequestStringParameter(ICostantiVerbale.CAMPO_NUMERO_PROTOCOLLO));
			}

			// ---> INSERIMENTO EVENTO di tipo ANNOTAZIONE

			IMisuraSicurezza lCtrl = SIEPLookupRemote.getMisuraSicurezzaRemote();

			EventoModel lRetMod = lCtrl.ExAggiornaEventoVerbaleXDesignazioneIstitutoMS(lEveMod, lVerMod);

			lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
					+ "=siap.siep.misurasicurezza.action.ActLoadDettaglioDesignazioneIstituto&"
					+ ICostantiEvento.CAMPO_ID_EVENTO + "=" + lRetMod.getIdEvento();

			return lPage;
		}

	} // Chiude processRequest

} // Chiude Action