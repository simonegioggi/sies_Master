package siap.siep.misurasicurezza.action;

import java.math.BigDecimal;
import java.util.Date;

import siap.sico.camponota.model.CampoNotaModel;
import siap.sico.decodifiche.model.ComuneModel;
import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.magistrato.action.ICostantiMagistrato;
import siap.sico.util.SICOLookupRemote;
import siap.siep.autoritaesterna.action.ICostantiAutoritaEsterna;
import siap.siep.autoritaesterna.model.AutoritaEsternaModel;
import siap.siep.misurasicurezza.controller.IMisuraSicurezza;
import siap.siep.notifica.action.ICostantiNotifica;
import siap.siep.notifica.model.NotificaModel;
import siap.siep.util.SIEPLookupRemote;
import f3b.util.DateUtils;
import f3b.web.IWebConstants;

/**
 * MEV_39
 * <p>
 * Title: ActModificaRestituzioneOrdineConsegna
 * </p>
 * <p>
 * Description: Modifica della Restituzione Ordine di consegna
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
public class ActModificaRestituzioneOrdineConsegna extends ActNotificheMS implements ICostantiMisuraSicurezza {

	public String processRequest() throws Exception {

		BigDecimal idEvento = getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO);
		if (!isRequestParameterNullObj("modalita") && "D".equals(getRequestStringParameter("modalita"))) {
			// cancellazione
			return IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
					+ "=siap.siep.ordineesecuzione.action.ActCancellaProvvedimento&"
					+ ICostantiEvento.CAMPO_ID_EVENTO + "=" + idEvento;
		} else {
			// modifica
			String codiceOperatore = getCodUtenteConnesso();
			String codiceUfficio = getCodUfficioUtenteConnesso();
			// Preparo l'EventoNotifica
			Date dataEmissione = getRequestDateParameter(ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE,
					ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE, ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE);
			Date dataTrasmissione = getRequestDateParameter(
					ICostantiEvento.CAMPO_ANNO_DATA_TRASMISSIONE_ATTI,
					ICostantiEvento.CAMPO_MESE_DATA_TRASMISSIONE_ATTI,
					ICostantiEvento.CAMPO_GIORNO_DATA_TRASMISSIONE_ATTI);
			EventoNotificaModel enm = new EventoNotificaModel();
			IEvento iEvento = SICOLookupRemote.getEventoRemote();
			enm = iEvento.ExRicercaEventoNotificaByKey(idEvento);
			enm.getEvento().setDataEmissione(dataEmissione);
			enm.getEvento().setCodOperatoreAggiornamento(codiceOperatore);
			enm.getEvento().setDataAggiornamento(DateUtils.getSysDate());
			enm.getEvento().setCodUfficioAggiornamento(codiceUfficio);
			enm.getEvento().setEveIdEvento(getRequestBigDecimalParameter(ICostantiEvento.CAMPO_EVE_ID_EVENTO));
			if (!isRequestParameterNullObj(ICostantiMagistrato.CAMPO_COD_MAGISTRATO)
					&& getRequestStringParameter(ICostantiMagistrato.CAMPO_COD_MAGISTRATO) != null
					&& !getRequestStringParameter(ICostantiMagistrato.CAMPO_COD_MAGISTRATO).equals("")) {
				enm.getEvento().setCodMagistrato(
						getRequestStringParameter(ICostantiMagistrato.CAMPO_COD_MAGISTRATO));
				enm.getMagistrato().setCodMagistrato(
						getRequestStringParameter(ICostantiMagistrato.CAMPO_COD_MAGISTRATO));
			} else {
				enm.getEvento().setCodMagistrato(calcolaMagistrato());
				enm.getMagistrato().setCodMagistrato(calcolaMagistrato());
			}

			// Preparo la notifica
			NotificaModel nm = new NotificaModel();
			if (!isRequestParameterNullObj(ICostantiAutoritaEsterna.CAMPO_NOTE_E))
				nm.setNote(getRequestStringParameter(ICostantiAutoritaEsterna.CAMPO_NOTE_E));
			nm.setCodEsito("-");
			nm.setCodTipoNotifica("R");
			nm.setDataInvio(dataTrasmissione);
			nm.setCodOperatoreInserimento(codiceOperatore);
			nm.setDataInserimento(DateUtils.getSysDate());
			nm.setCodUfficioInserimento(codiceUfficio);
			if (!isRequestParameterNullObj("descrTipoAutorita")
					&& !getRequestStringParameter("descrTipoAutorita").contains("Istituto Detenzione")) {
				if (!isRequestParameterNullObj(ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA)
						&& getRequestStringParameter(ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA) != null
						&& !"".equals(getRequestStringParameter(ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA))
						&& !"-".equals(getRequestStringParameter(ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA))) {
					AutoritaEsternaModel aem = new AutoritaEsternaModel();
					String codTipoAutorita = getRequestStringParameter(ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA);
					String descSedeAutorita = getRequestStringParameter(ICostantiAutoritaEsterna.CAMPO_DESC_SEDE);
					aem.setCodTipoAutorita(codTipoAutorita);
					ComuneModel lComMod = new ComuneModel(getCodComuneByDescr(descSedeAutorita));
					aem.setCodSede(lComMod.getCodComune());
					aem.setCodOperatoreInserimento(codiceOperatore);
					aem.setCodUfficioInserimento(codiceUfficio);
					aem.setDataInserimento(DateUtils.getSysDate());
					nm.setAutoritaEsterna(aem);
				}
			} else {
				// Notifica Istituto di detenzione
				if (!isRequestParameterNullObj(ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA)
						&& getRequestStringParameter(ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA) != null
						&& !"".equals(getRequestStringParameter(ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA))
						&& !"-".equals(getRequestStringParameter(ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA)))
					nm.setIstDetIdIstitutoDetenzione(getRequestStringParameter(ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA));
			}
			NotificaModel[] nmArray = new NotificaModel[1];
			nmArray[0] = nm;
			enm.setNotifiche(nmArray);

			// Eventuale nota
			if (!this.isRequestParameterNullObj(ICostantiNotifica.CAMPO_NOTE)
					&& getRequestStringParameter(ICostantiNotifica.CAMPO_NOTE) != null
					&& !"".equals(getRequestStringParameter(ICostantiNotifica.CAMPO_NOTE))) {
				CampoNotaModel cnm = new  CampoNotaModel();
				cnm.setCodOperatoreInserimento(codiceOperatore);
				cnm.setCodUfficioInserimento(codiceUfficio);
				cnm.setDataInserimento(DateUtils.getSysDate());
				cnm.setDescr(getRequestStringParameter(ICostantiNotifica.CAMPO_NOTE));
				cnm.setEveIdEvento(idEvento);
				cnm.setFasSieIdFascicoloSiep(enm.getEvento().getFasSieIdFascicoloSiep());
				CampoNotaModel[] cnmArray = new CampoNotaModel[1];
				cnmArray[0] = cnm;
				enm.setCampoNote(cnmArray);
			} else
				enm.setCampoNote(null);

			// MODIFICA RESTITUZIONE ORDINE DI CONSEGNA
			IMisuraSicurezza ims = SIEPLookupRemote.getMisuraSicurezzaRemote();
			ims.ExAggiornaEventoNotificheXComunicazioneMS(enm);
		}

		return IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
				+ "=siap.siep.misurasicurezza.action.ActLoadDettaglioRestituzioneOrdineConsegna&"
				+ ICostantiEvento.CAMPO_ID_EVENTO + "=" + idEvento;
	} // Chiude processRequest

} // Chiude Action