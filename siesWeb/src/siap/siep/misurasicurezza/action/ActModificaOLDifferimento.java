package siap.siep.misurasicurezza.action;

import java.math.BigDecimal;
import java.util.Date;

import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.magistrato.action.ICostantiMagistrato;
import siap.sico.misuraalternativa.controller.IMisuraAlternativa;
import siap.sico.misuraalternativa.model.MisuraAlternativaModel;
import siap.sico.util.SICOLookupRemote;
import siap.siep.notifica.model.NotificaModel;
import siap.siep.ordineesecuzione.controller.IOrdineEsecuzione;
import siap.siep.penaresidua.action.ICostantiPenaResidua;
import siap.siep.penaresidua.model.PenaResiduaModel;
import siap.siep.util.SIEPLookupRemote;
import siap.sius.depositoordinanzapc.action.ICostantiDepositoOrdinanzaPc;
import f3b.util.DateUtils;
import f3b.web.IWebConstants;

/**
 * MEV_39
 * <p>
 * Title: ActModificaOLDifferimento
 * </p>
 * <p>
 * Description: Classe Action per la Modifica dei provvedimenti di
 * </p>
 * <p>
 * Ordine di Esecuzione per Differimento, esecuzione MS
 * </p>
 * <p>
 * Copyright: Copyright (c) 2017
 * </p>
 * <p>
 * Company: EII
 * </p>
 * 
 * @author SGIOGGI
 * @version 1.0
 */
public class ActModificaOLDifferimento extends ActNotificheMS implements ICostantiMisuraSicurezza {

	public String processRequest() throws Exception {

		// EventoNotifica
		BigDecimal idEvento = getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO);
		if (!isRequestParameterNullObj("modalita") && "D".equals(getRequestStringParameter("modalita"))) {
			// cancellazione
			return IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
					+ "=siap.siep.ordineesecuzione.action.ActCancellaProvvedimento&"
					+ ICostantiEvento.CAMPO_ID_EVENTO + "=" + idEvento;
		} else {
			String codUtenteConnesso = this.getCodUtenteConnesso();
			String codUfficioUtenteConnesso = this.getCodUfficioUtenteConnesso();

			EventoNotificaModel enm = new EventoNotificaModel();
			IEvento iEvento = SICOLookupRemote.getEventoRemote();
			enm = iEvento.ExRicercaEventoNotificaByKey(idEvento);

			Date dataEmissione = getRequestDateParameter(ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE,
					ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE, ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE);
			Date dataTrasmissione = getRequestDateParameter(
					ICostantiEvento.CAMPO_ANNO_DATA_TRASMISSIONE_ATTI,
					ICostantiEvento.CAMPO_MESE_DATA_TRASMISSIONE_ATTI,
					ICostantiEvento.CAMPO_GIORNO_DATA_TRASMISSIONE_ATTI);
			enm.getEvento().setDataEmissione(dataEmissione);
			enm.getEvento().setCodOperatoreAggiornamento(codUtenteConnesso);
			enm.getEvento().setDataAggiornamento(DateUtils.getSysDate());
			enm.getEvento().setCodUfficioAggiornamento(codUfficioUtenteConnesso);

			if (!isRequestParameterNullObj(ICostantiMagistrato.CAMPO_COD_MAGISTRATO)
					&& getRequestStringParameter(ICostantiMagistrato.CAMPO_COD_MAGISTRATO) != null
					&& !"".equals(getRequestStringParameter(ICostantiMagistrato.CAMPO_COD_MAGISTRATO))) {
				enm.getEvento().setCodMagistrato(
						getRequestStringParameter(ICostantiMagistrato.CAMPO_COD_MAGISTRATO));
				enm.getMagistrato().setCodMagistrato(
						getRequestStringParameter(ICostantiMagistrato.CAMPO_COD_MAGISTRATO));
			} else {
				enm.getEvento().setCodMagistrato(this.calcolaMagistrato());
				enm.getMagistrato().setCodMagistrato(this.calcolaMagistrato());
			}

			NotificaModel[] nmArray = setNotificheMS(dataTrasmissione, enm.getEvento().getCodMotivo());
			enm.setNotifiche(nmArray);

			// Pena residua (va impostata = null se assente)
			PenaResiduaModel prm = new PenaResiduaModel();
			if (!isRequestParameterNullObj(ICostantiPenaResidua.CAMPO_ID_PENA_RESIDUA)) {
				BigDecimal idPenaResidua = getRequestBigDecimalParameter(ICostantiPenaResidua.CAMPO_ID_PENA_RESIDUA);
				prm.setIdPenaResidua(idPenaResidua);
				if (!isRequestParameterNullObj(ICostantiPenaResidua.CAMPO_ANNO_DATA_FINE))
					prm.setDataFine(getRequestDateParameter(ICostantiPenaResidua.CAMPO_ANNO_DATA_FINE,
							ICostantiPenaResidua.CAMPO_MESE_DATA_FINE,
							ICostantiPenaResidua.CAMPO_GIORNO_DATA_FINE));
			} else
				prm = null;

			// Data Differimento
			Date dataDifferimento = getRequestDateParameter(
					ICostantiDepositoOrdinanzaPc.CAMPO_ANNO_DATA_INIZIO_PERIODO,
					ICostantiDepositoOrdinanzaPc.CAMPO_MESE_DATA_INIZIO_PERIODO,
					ICostantiDepositoOrdinanzaPc.CAMPO_GIORNO_DATA_INIZIO_PERIODO);
			// Data Fine Rinvio
			Date dataFineRinvio = getRequestDateParameter(
					ICostantiDepositoOrdinanzaPc.CAMPO_ANNO_DATA_FINE_MISURA,
					ICostantiDepositoOrdinanzaPc.CAMPO_MESE_DATA_FINE_MISURA,
					ICostantiDepositoOrdinanzaPc.CAMPO_GIORNO_DATA_FINE_MISURA);

			Date dataScarcerazione = null;
			String codTipoUfficioScarcerazione = "-";
			if (!isRequestParameterNullObj("AnnoDataScarcerazione")) {
				dataScarcerazione = getRequestDateParameter("AnnoDataScarcerazione", "MeseDataScarcerazione",
						"GiornoDataScarcerazione");
				// scarcerato --> SORV; daScarcerare --> PROC
				codTipoUfficioScarcerazione = "PROC";
				if ("scarcerato".equals(getRequestStringParameter("isScarcerato"))) {
					codTipoUfficioScarcerazione = "SORV";
					// intervento 11.3 deve essere comunicazione = 12
					enm.getEvento().setCodTipoProvvedimento("12"); // Comunicazione
				}
			}

			IMisuraAlternativa ima = SICOLookupRemote.getMisuraAlternativaRemote();
			MisuraAlternativaModel mam = ima
					.ExRicercaMisuraAlternativaByKey(getRequestBigDecimalParameter("idMisuraAlternativa"));
			mam.setDataInizioMisura(dataDifferimento);
			mam.setDataFineMisura(dataFineRinvio);
			mam.setDataScarcerazione(dataScarcerazione);
			mam.setCodTipoUfficioScarcerazione(codTipoUfficioScarcerazione);
			if ("PROC".equals(codTipoUfficioScarcerazione))
				mam.setFlagUfficioInserimento("P");
			else
				mam.setFlagUfficioInserimento("S");
			mam.setCodOperatoreAggiornamento(codUtenteConnesso);
			mam.setDataAggiornamento(DateUtils.getSysDate());
			mam.setCodUfficioAggiornamento(codUfficioUtenteConnesso);

			// MODIFICA OE
			IOrdineEsecuzione ioe = SIEPLookupRemote.getOrdineEsecuzioneRemote();
			EventoNotificaModel enmMod = new EventoNotificaModel();
			enmMod = ioe.ExModificaOLDifferimentoConNotifiche(enm, prm, mam);

			// pagina di ritorno
			return IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
					+ "=siap.siep.misurasicurezza.action.ActDettaglioOLDifferimento&"
					+ ICostantiEvento.CAMPO_ID_EVENTO + "=" + enmMod.getEvento().getIdEvento();
		}
	} // Chiude processRequest

} // Chiude Action