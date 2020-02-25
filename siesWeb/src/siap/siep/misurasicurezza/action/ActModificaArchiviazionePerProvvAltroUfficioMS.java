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
import f3b.util.DateUtils;
import f3b.web.IWebConstants;

/**
 * <p>
 * Title: ActModificaArchiviazionePerProvvAltroUfficioMS
 * </p>
 * <p>
 * Description: classe per la Modifica dei Provvedimenti
 * </p>
 * <p>
 * di Archiviazione su decisione di Altro Ufficio
 * </p>
 * <p>
 * (Definizione MIS. SIC.- tipo provv = Annotazione (25) )
 * </p>
 * <p>
 * Copyright: Copyright (c) 2015
 * </p>
 * <p>
 * Company: INTERSISTEMI ITALIA S.P.A.
 * </p>
 * 
 * @author AMBROSINO
 *
 */
public class ActModificaArchiviazionePerProvvAltroUfficioMS extends ActNotificheMS implements
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

		Date lDataRicezione = getRequestDateParameter(ICostantiArchiviazione.CAMPO_ANNO_DATA_RICEZIONE,
				ICostantiArchiviazione.CAMPO_MESE_DATA_RICEZIONE,
				ICostantiArchiviazione.CAMPO_GIORNO_DATA_RICEZIONE);

		Date lDataEmissione = getRequestDateParameter(ICostantiArchiviazione.CAMPO_ANNO_DATA_EMISSIONE,
				ICostantiArchiviazione.CAMPO_MESE_DATA_EMISSIONE,
				ICostantiArchiviazione.CAMPO_GIORNO_DATA_EMISSIONE);

		Date lDataArch = getRequestDateParameter(ICostantiArchiviazione.CAMPO_ANNO_DATA_DEFINIZIONE,
				ICostantiArchiviazione.CAMPO_MESE_DATA_DEFINIZIONE,
				ICostantiArchiviazione.CAMPO_GIORNO_DATA_DEFINIZIONE);

		Date lDataTrasmissione = getRequestDateParameter(ICostantiEvento.CAMPO_ANNO_DATA_TRASMISSIONE_ATTI,
				ICostantiEvento.CAMPO_MESE_DATA_TRASMISSIONE_ATTI,
				ICostantiEvento.CAMPO_GIORNO_DATA_TRASMISSIONE_ATTI);

		// in codRich ci metto il Cod. OGGETTO_DEFINIZIONE
		String codRich = getRequestStringParameter(ICostantiArchiviazione.CAMPO_COD_OGGETTO_DEFINIZIONE);

		// = = EVENTO = =
		// L'EVENTO VIENE MODIFICATO CON 'Update'
		// lEve.getEvento().setCodTipoEvento("01");
		// lEve.getEvento().setCodTipoProvvedimento("25");

		lEve.getEvento().setDataEmissione(lDataEmissione);
		lEve.getEvento().setCodMotivo(codRich);

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

		lEve.getEvento().setCodOperatoreAggiornamento(lCodiceOperatore);
		lEve.getEvento().setDataAggiornamento(DateUtils.getSysDate());
		lEve.getEvento().setCodUfficioAggiornamento(lCodiceUfficio);

		// Notifiche ---------> Altra Autorità
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

		// Ufficio Emittente: Archiviazione provvedimento Altro Ufficio (COD = 0002 , RV_Domain = DEFI_ALTRO)
		// lArcMod.setCodProvvedimento("0002");

		// Oggetto (Tipo di Archiviazione)
		lArcMod.setCodOggettoDefinizione(codRich);

		lArcMod.setDataDefinizione(lDataArch);
		lArcMod.setDataEmissione(lDataEmissione);
		lArcMod.setDataRicezione(lDataRicezione);

		lArcMod.setCodOperatoreAggiornamento(this.getCodUtenteConnesso());
		lArcMod.setCodUfficioAggiornamento(this.getCodUfficioUtenteConnesso());
		lArcMod.setDataAggiornamento(DateUtils.getSysDate());

		// Numero Nota o Protocollo
		lArcMod.setNumNota(getRequestStringParameter(ICostantiArchiviazione.CAMPO_NUM_NOTA));

		// autorità che ha emesso la nota
		lArcMod.setCodTipoAutoritaEmittente(getRequestStringParameter(ICostantiArchiviazione.CAMPO_COD_TIPO_AUTORITA_EMITTENTE));
		ComuneModel lCom = new ComuneModel(
				getCodComuneByDescrFlagVal(getRequestStringParameter(ICostantiArchiviazione.CAMPO_COD_LUOGO_EMITTENTE)));
		lArcMod.setCodLuogoEmittente(lCom.getCodComune());

		// eventuale altra autorità emittente (se manca l'autorità emittente)
		lArcMod.setAltraAutorita(getRequestStringParameter(ICostantiArchiviazione.CAMPO_ALTRA_AUTORITA));
		lArcMod.setIndirizzoEmittente(getRequestStringParameter(ICostantiArchiviazione.CAMPO_INDIRIZZO_EMITTENTE));
		lArcMod.setNote(getRequestStringParameter(ICostantiArchiviazione.CAMPO_NOTE));

		// -> MODIFICA ARCHIVIAZIONE -------->
		EventoNotificaModel lRetModel = new EventoNotificaModel();
		IMisuraSicurezza lCtrl = SIEPLookupRemote.getMisuraSicurezzaRemote();
		lRetModel = lCtrl.ExAggiornaEventoNotificheXArchiviazioneMS(lEve, lArcMod);

		lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
				+ "=siap.siep.misurasicurezza.action.ActDettaglioArchiviazionePerProvvAltroUfficio&"
				+ ICostantiEvento.CAMPO_ID_EVENTO + "=" + lRetModel.getEvento().getIdEvento();

		return lPage;

	} // Chiude processRequest

} // Chiude Action