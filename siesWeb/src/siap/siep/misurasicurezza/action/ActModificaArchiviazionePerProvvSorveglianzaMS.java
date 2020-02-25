package siap.siep.misurasicurezza.action;

import java.math.BigDecimal;
import java.util.Date;

import siap.sico.decodifiche.model.ComuneModel;
import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.magistrato.action.ICostantiMagistrato;
import siap.sico.util.SICOLookupRemote;
import siap.siep.archiviazione.action.ICostantiArchiviazione;
import siap.siep.archiviazione.controller.IArchiviazione;
import siap.siep.archiviazione.model.ArchiviazioneModel;
import siap.siep.misurasicurezza.controller.IMisuraSicurezza;
import siap.siep.notifica.model.NotificaModel;
import siap.siep.util.SIEPLookupRemote;
import siap.sius.fascicolo.action.ICostantiFascicoloSius;
import f3b.util.DateUtils;
import f3b.web.IWebConstants;

/**
 *
 * <p>
 * Title: ActModificaArchiviazionePerProvvSorveglianzaMS
 * </p>
 * <p>
 * Description: classe per la Modifica dei Provvedimenti di Archiviazione
 * </p>
 * <p>
 * per decisione della Sorveglianza (Definizione MIS. SIC.)
 * </p>
 * <p>
 * ( tipo provv = Annotazione (25) )
 * </p>
 * <p>
 * La form prevede la possibilità di riscaricare il Provv. della Sorveglianza
 * </p>
 * <p>
 * Copyright: Copyright (c) 2015
 * </p>
 * <p>
 * Company: INTERSISTEMI ITALIA S.P.A.
 * </p>
 * 
 * @author AMBROSINO
 */
public class ActModificaArchiviazionePerProvvSorveglianzaMS extends ActNotificheMS implements
		ICostantiMisuraSicurezza {

	public String processRequest() throws Exception {

//		FascicoloSiepModel lFascicoloModel = (FascicoloSiepModel) getSessionAttribute("fascicolo");
		String lPage = "";

		// dati operatore/ufficio Inserimento
		String lCodiceOperatore = this.getCodUtenteConnesso();
		String lCodiceUfficio = this.getCodUfficioUtenteConnesso();

		// EventoNotifica
		BigDecimal lIdEve = getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO);
		EventoNotificaModel lEve = new EventoNotificaModel();
		IEvento lCtrlEve = SICOLookupRemote.getEventoRemote();
		lEve = lCtrlEve.ExRicercaEventoNotificaByKey(lIdEve);

		Date lDataRicezione = getRequestDateParameter(ICostantiEvento.CAMPO_ANNO_DATA_RICEZIONE_ATTI,
				ICostantiEvento.CAMPO_MESE_DATA_RICEZIONE_ATTI,
				ICostantiEvento.CAMPO_GIORNO_DATA_RICEZIONE_ATTI);

		Date lDataEmissione = getRequestDateParameter(ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE,
				ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE, ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE);

		Date lDataArch = getRequestDateParameter(ICostantiMisuraSicurezza.CAMPO_ANNO_DATA_DEFINIZIONE,
				ICostantiMisuraSicurezza.CAMPO_MESE_DATA_DEFINIZIONE,
				ICostantiMisuraSicurezza.CAMPO_GIORNO_DATA_DEFINIZIONE);

		Date lDataTrasmissione = getRequestDateParameter(ICostantiEvento.CAMPO_ANNO_DATA_TRASMISSIONE_ATTI,
				ICostantiEvento.CAMPO_MESE_DATA_TRASMISSIONE_ATTI,
				ICostantiEvento.CAMPO_GIORNO_DATA_TRASMISSIONE_ATTI);

		// in codRich ci metto il Cod. OGGETTO_DEFINIZIONE
		String codRich = getRequestStringParameter(ICostantiMisuraSicurezza.CAMPO_COD_RICH_MISURA_SIC);

		// = = EVENTO = =
		// L'EVENTO VIENE MODIFICATO CON 'Update'
		// lEve.getEvento().setCodTipoEvento("01");
		// lEve.getEvento().setCodTipoProvvedimento("25");

		lEve.getEvento().setCodMotivo(codRich);
		lEve.getEvento().setDataEmissione(lDataArch);

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

		// Eventuale ANNO e NUM FASCICOLO SIUS
		if (!isRequestParameterNullObj(ICostantiFascicoloSius.CAMPO_CHIAVE_ANNO)
				&& getRequestStringParameter(ICostantiFascicoloSius.CAMPO_CHIAVE_ANNO) != null) {
			lEve.getEvento().setChiaveAnno(
					getRequestBigDecimalParameter(ICostantiFascicoloSius.CAMPO_CHIAVE_ANNO));
		}

		if (!isRequestParameterNullObj(ICostantiFascicoloSius.CAMPO_CHIAVE_PROGR)
				&& getRequestStringParameter(ICostantiFascicoloSius.CAMPO_CHIAVE_PROGR) != null) {
			lEve.getEvento().setChiaveProgr(
					getRequestBigDecimalParameter(ICostantiFascicoloSius.CAMPO_CHIAVE_PROGR));
		}

		// Eventuale ANNO e NUM PROVVEDIMENTO SIUS
		if (!isRequestParameterNullObj(ICostantiEvento.CAMPO_ANNO_PROTOCOLLO)
				&& getRequestStringParameter(ICostantiEvento.CAMPO_ANNO_PROTOCOLLO) != null) {
			lEve.getEvento().setAnnoProtocollo(
					getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ANNO_PROTOCOLLO));
		}

		if (!isRequestParameterNullObj(ICostantiEvento.CAMPO_PROGR_PROTOCOLLO)
				&& getRequestStringParameter(ICostantiEvento.CAMPO_PROGR_PROTOCOLLO) != null) {
			lEve.getEvento().setProgrProtocollo(
					getRequestBigDecimalParameter(ICostantiEvento.CAMPO_PROGR_PROTOCOLLO));
		}

		// Notifiche --------->
		// LE NOTIFICHE (Tutte) VENGONO CANCELLATE E RISCRITTE EX-NOVO ('Delete' e poi 'Insert')

		// NotificaModel[] lNotifiche = (NotificaModel[]) lNotificheArray.toArray(new NotificaModel[0]);
		NotificaModel[] lNotifiche = setNotificheMS(lDataTrasmissione, lEve.getEvento().getCodMotivo());

		lEve.setNotifiche(lNotifiche);

		// = = ARCHIVIAZIONE = =
		// L'ARCHIVIAZIONE VIENE MODIFICATA CON 'Update'
		IArchiviazione lCtrlArc = SIEPLookupRemote.getArchiviazioneRemote();
		ArchiviazioneModel lArcMod = new ArchiviazioneModel();
		lArcMod = lCtrlArc.ExRicercaArchiviazioneCssaIstitutoByIdEvento(lIdEve);

		// lArcMod.setCodTipoProvvedimento("25"); // ANNTAZIOE
		// Ufficio Emittente: Archiviazione provvedimento Giudice Sorveglianza (COD = 0003 RV_Domain =
		// DEFI_ALTRO)
		// lArcMod.setCodProvvedimento("0003");

		// Ordinanza/decreto
		lArcMod.setCodTipoProvvedimentoArc(getRequestStringParameter(ICostantiEvento.CAMPO_COD_TIPO_PROVVEDIMENTO));
		// Oggetto (Tipo di Archiviazione)
		lArcMod.setCodOggettoDefinizione(codRich);

		lArcMod.setDataDefinizione(lDataArch);
		lArcMod.setDataEmissione(lDataEmissione);
		lArcMod.setDataRicezione(lDataRicezione);

		lArcMod.setCodOperatoreAggiornamento(this.getCodUtenteConnesso());
		lArcMod.setCodUfficioAggiornamento(this.getCodUfficioUtenteConnesso());
		lArcMod.setDataAggiornamento(DateUtils.getSysDate());

		// Eventuale ANNO e NUM PROVVEDIMENTO SIUS
		if (!isRequestParameterNullObj(ICostantiEvento.CAMPO_ANNO_PROTOCOLLO)
				&& getRequestStringParameter(ICostantiEvento.CAMPO_ANNO_PROTOCOLLO) != null) {
			lArcMod.setAnnoProvvedimento(getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ANNO_PROTOCOLLO));
		}

		if (!isRequestParameterNullObj(ICostantiEvento.CAMPO_PROGR_PROTOCOLLO)
				&& getRequestStringParameter(ICostantiEvento.CAMPO_PROGR_PROTOCOLLO) != null) {
			lArcMod.setNumProvvedimento(getRequestStringParameter(ICostantiEvento.CAMPO_PROGR_PROTOCOLLO));
		}

		if (!isRequestParameterNullObj(ICostantiEvento.CAMPO_COD_UFFICIO_EMITTENTE)
				&& getRequestStringParameter(ICostantiEvento.CAMPO_COD_UFFICIO_EMITTENTE) != null
				&& !isRequestParameterNullObj(ICostantiEvento.CAMPO_COD_LUOGO_EMITTENTE)
				&& getRequestStringParameter(ICostantiEvento.CAMPO_COD_LUOGO_EMITTENTE) != null) {
			lArcMod.setCodTipoAutoritaEmittente(getRequestStringParameter(ICostantiEvento.CAMPO_COD_UFFICIO_EMITTENTE));
			ComuneModel lCom = new ComuneModel(
					getCodComuneByDescrFlagVal(getRequestStringParameter(ICostantiEvento.CAMPO_COD_LUOGO_EMITTENTE)));
			// MEV2 STEP2 - 01-03-16 - Metodo inserito per controllare l'esistenza dell'uffico nel comune
			// selezionato
			/*String lCodiceuf = */getCodUfficioByCodTipoUfficioDescrComune(
					getRequestStringParameter(ICostantiEvento.CAMPO_COD_UFFICIO_EMITTENTE),
					lCom.getDescrizione());

			lArcMod.setCodLuogoEmittente(lCom.getCodComune());
		}

		if (!isRequestParameterNullObj(ICostantiArchiviazione.CAMPO_NOTE)
				&& getRequestStringParameter(ICostantiArchiviazione.CAMPO_NOTE) != null) {
			lArcMod.setNote(getRequestStringParameter(ICostantiArchiviazione.CAMPO_NOTE));
		}

		// Eventuale ANNO e NUM FASCICOLO SIUS
		if (!isRequestParameterNullObj(ICostantiFascicoloSius.CAMPO_CHIAVE_ANNO)
				&& getRequestStringParameter(ICostantiFascicoloSius.CAMPO_CHIAVE_ANNO) != null) {
			lArcMod.setChiaveAnno(getRequestBigDecimalParameter(ICostantiFascicoloSius.CAMPO_CHIAVE_ANNO));
		}

		if (!isRequestParameterNullObj(ICostantiFascicoloSius.CAMPO_CHIAVE_PROGR)
				&& getRequestStringParameter(ICostantiFascicoloSius.CAMPO_CHIAVE_PROGR) != null) {
			lArcMod.setChiaveProgr(getRequestBigDecimalParameter(ICostantiFascicoloSius.CAMPO_CHIAVE_PROGR));
		}

		// -> MODIFICA ARCHIVIAZIONE -------->
		EventoNotificaModel lRetModel = new EventoNotificaModel();
		IMisuraSicurezza lCtrl = SIEPLookupRemote.getMisuraSicurezzaRemote();
		lRetModel = lCtrl.ExAggiornaEventoNotificheXArchiviazioneMS(lEve, lArcMod);

		lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
				+ "=siap.siep.misurasicurezza.action.ActDettaglioArchiviazionePerProvvSorveglianza&"
				+ ICostantiEvento.CAMPO_ID_EVENTO + "=" + lRetModel.getEvento().getIdEvento();

		return lPage;
	} // Chiude processRequest

} // Chiude Action