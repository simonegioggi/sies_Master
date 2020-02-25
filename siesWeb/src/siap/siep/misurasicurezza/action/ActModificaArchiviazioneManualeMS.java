package siap.siep.misurasicurezza.action;

import java.math.BigDecimal;
import java.util.Date;

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
 *
 * <p>
 * Title: ActModificaArchiviazioneManualeMS
 * </p>
 * <p>
 * Description: classe per Modifica dei Provvedimenti
 * </p>
 * <p>
 * di Archiviazione Manuale (Definizione MIS. SIC.)
 * </p>
 * <p>
 * ( tipo provv = Annotazione (25) )
 * </p>
 * <p>
 * Copyright: Copyright (c) 2015
 * </p>
 * <p>
 * Company: Intersistemi Italia S.P.A.
 * </p>
 * 
 * @author AMBROSINO
 * @version 1.0
 */
public class ActModificaArchiviazioneManualeMS extends ActNotificheMS implements ICostantiMisuraSicurezza {

	public String processRequest() throws Exception {

//		FascicoloSiepModel lFascicoloModel = (FascicoloSiepModel) getSessionAttribute("fascicolo");
		String lPage = "";

		String lCodiceOperatore = this.getCodUtenteConnesso();
		String lCodiceUfficio = this.getCodUfficioUtenteConnesso();

		// EventoNotifica
		BigDecimal lIdEve = getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO);
		EventoNotificaModel lEve = new EventoNotificaModel();
		IEvento lCtrlEve = SICOLookupRemote.getEventoRemote();
		lEve = lCtrlEve.ExRicercaEventoNotificaByKey(lIdEve);

		Date lDataDefinizione = getRequestDateParameter(ICostantiMisuraSicurezza.CAMPO_ANNO_DATA_DEFINIZIONE,
				ICostantiMisuraSicurezza.CAMPO_MESE_DATA_DEFINIZIONE, ICostantiMisuraSicurezza.CAMPO_GIORNO_DATA_DEFINIZIONE);

//		Date lDataTrasmissione = getRequestDateParameter(ICostantiEvento.CAMPO_ANNO_DATA_TRASMISSIONE_ATTI,
//				ICostantiEvento.CAMPO_MESE_DATA_TRASMISSIONE_ATTI,
//				ICostantiEvento.CAMPO_GIORNO_DATA_TRASMISSIONE_ATTI);

		// L'EVENTO VIENE MODIFICATO CON 'Update'
		String codRich = getRequestStringParameter(ICostantiMisuraSicurezza.CAMPO_COD_RICH_MISURA_SIC);

		lEve.getEvento().setDataEmissione(lDataDefinizione);
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

		// LE NOTIFICHE VENGONO CANCELLATE E RISCRITTE EX-NOVO ('Delete' e poi 'Insert')

		// NotificaModel[] lNotifiche = (NotificaModel[]) lNotificheArray.toArray(new NotificaModel[0]);
		NotificaModel[] lNotifiche = setNotificheMS(lDataDefinizione, lEve.getEvento().getCodMotivo());
		lEve.setNotifiche(lNotifiche);

		// = ARCHIVIAZIONE = = =
		// L'ARCHIVIAZIONE VIENE MODIFICATA CON 'Update'
		IArchiviazione lCtrlArc = SIEPLookupRemote.getArchiviazioneRemote();
		ArchiviazioneModel lArcMod = new ArchiviazioneModel();
		lArcMod = lCtrlArc.ExRicercaArchiviazioneCssaIstitutoByIdEvento(lIdEve);

		// lArcMod.setCodTipoProvvedimento("25"); // ANNTAZIOE old per provare
		// Ufficio Emittente: Locale; Archiviazione Manuale (COD = "-" ; RV_Domain = DEFI_ALTRO)

		lArcMod.setDataDefinizione(lDataDefinizione);
		lArcMod.setDataEmissione(lDataDefinizione);
		lArcMod.setCodOggettoDefinizione(codRich);

		lArcMod.setCodOperatoreAggiornamento(this.getCodUtenteConnesso());
		lArcMod.setCodUfficioAggiornamento(this.getCodUfficioUtenteConnesso());
		lArcMod.setDataAggiornamento(DateUtils.getSysDate());

		if (!isRequestParameterNullObj(ICostantiArchiviazione.CAMPO_NOTE)
				&& getRequestStringParameter(ICostantiArchiviazione.CAMPO_NOTE) != null) {
			lArcMod.setNote(getRequestStringParameter(ICostantiArchiviazione.CAMPO_NOTE));
		}

		// -> MODIFICA ARCHIVIAZIONE -------->
		IMisuraSicurezza lCtrl = SIEPLookupRemote.getMisuraSicurezzaRemote();
		EventoNotificaModel lRetModel = new EventoNotificaModel();
		lRetModel = lCtrl.ExAggiornaEventoNotificheXArchiviazioneMS(lEve, lArcMod);

		lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
				+ "=siap.siep.misurasicurezza.action.ActDettaglioArchiviazioneManuale&"
				+ ICostantiEvento.CAMPO_ID_EVENTO + "=" + lRetModel.getEvento().getIdEvento();

		return lPage;
	} // Chiude processRequest

} // Chiude Action