package siap.siep.misurasicurezza.action;

import java.math.BigDecimal;
import java.util.Date;

import siap.sico.camponota.model.CampoNotaModel;
import siap.sico.decodifiche.model.ComuneModel;
import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.web.ActionSiap;
import siap.siep.autoritaesterna.action.ICostantiAutoritaEsterna;
import siap.siep.autoritaesterna.model.AutoritaEsternaModel;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.notifica.action.ICostantiNotifica;
import siap.siep.notifica.model.NotificaModel;
import siap.siep.ordineesecuzione.action.ICostantiOrdineEsecuzione;
import siap.siep.ordineesecuzione.controller.IOrdineEsecuzione;
import siap.siep.penaresidua.action.ICostantiPenaResidua;
import siap.siep.penaresidua.model.PenaResiduaModel;
import siap.siep.util.SIEPLookupRemote;
import f3b.util.DateUtils;
import f3b.web.IWebConstants;

/**
 * MEV_39
 * <p>
 * Title: ActInserisciRestituzioneOrdineConsegna
 * </p>
 * <p>
 * Description: Inserimento della Restituzione Ordine di consegna
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
public class ActInserisciRestituzioneOrdineConsegna extends ActionSiap implements ICostantiMisuraSicurezza,
		ICostantiOrdineEsecuzione {

	public String processRequest() throws Exception {

		// recupero il fascicolo dalla sessione
		FascicoloSiepModel fsm = (FascicoloSiepModel) getSessionAttribute("fascicolo");

		String codiceOperatore = getCodUtenteConnesso();
		String codiceUfficio = getCodUfficioUtenteConnesso();
		Date dataEmissione = getRequestDateParameter(ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE,
				ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE, ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE);
		Date dataTrasmissione = getRequestDateParameter(ICostantiEvento.CAMPO_ANNO_DATA_TRASMISSIONE_ATTI,
				ICostantiEvento.CAMPO_MESE_DATA_TRASMISSIONE_ATTI,
				ICostantiEvento.CAMPO_GIORNO_DATA_TRASMISSIONE_ATTI);

		// Preparo l'Evento Notifica
		EventoNotificaModel enm = new EventoNotificaModel();
		enm.getEvento().setCodTipoEvento("01"); // provvedimento
		 // @emma intervento post collaudo 11.3 (deve essere sempre  'Richiesta' e NON 'Comunicazione')	
		enm.getEvento().setCodTipoProvvedimento("26"); 
		enm.getEvento().setCodMotivo("1149"); // Restituzione Ordine di Consegna per Esecuzione MS
		enm.getEvento().setFlagVideoSiep("S");
		enm.getEvento().setFlagStampaSiep("S");
		enm.getEvento().setFasSieIdFascicoloSiep(fsm.getIdFascicoloSiep());
		enm.getEvento().setDataEmissione(dataEmissione);
		enm.getEvento().setCodOperatoreInserimento(codiceOperatore);
		enm.getEvento().setDataInserimento(DateUtils.getSysDate());
		enm.getEvento().setCodUfficioInserimento(codiceUfficio);
		enm.getEvento().setCodLuogoEmittente(getCodComuneUtenteConnesso());
		enm.getEvento().setCodUfficioEmittente(codiceUfficio);
		enm.getEvento().setAnnoProtocollo(new BigDecimal(DateUtils.getSysDate("yyyy")));
		enm.getEvento().setCodEsito("-");
		enm.getEvento().setCodLuogoDestinatario("-");
		enm.getEvento().setCodTipoUfficioDestinatario("-");
		enm.getEvento().setEveIdEvento(getRequestBigDecimalParameter(ICostantiEvento.CAMPO_EVE_ID_EVENTO));
		enm.getMagistrato().setCodMagistrato(getRequestStringParameter(ICostantiEvento.CAMPO_COD_MAGISTRATO));
		enm.getEvento().setCodMagistrato(getRequestStringParameter(ICostantiEvento.CAMPO_COD_MAGISTRATO));

		EventoNotificaModel enmNew = new EventoNotificaModel();

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

		// Pena residua
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

		// Eventuale nota
		if (!this.isRequestParameterNullObj(ICostantiNotifica.CAMPO_NOTE)
				&& getRequestStringParameter(ICostantiNotifica.CAMPO_NOTE) != null
				&& !"".equals(getRequestStringParameter(ICostantiNotifica.CAMPO_NOTE))) {
			CampoNotaModel cnm = new  CampoNotaModel();
			cnm.setCodOperatoreInserimento(codiceOperatore);
			cnm.setCodUfficioInserimento(codiceUfficio);
			cnm.setDataInserimento(DateUtils.getSysDate());
			cnm.setDescr(getRequestStringParameter(ICostantiNotifica.CAMPO_NOTE));
			cnm.setFasSieIdFascicoloSiep(fsm.getIdFascicoloSiep());
			CampoNotaModel[] cnmArray = new CampoNotaModel[1];
			cnmArray[0] = cnm;
			enm.setCampoNote(cnmArray);
		}

		// INSERIMENTO RESTITUZIONE ORDINE di CONSEGNA
		IOrdineEsecuzione lCtrl = SIEPLookupRemote.getOrdineEsecuzioneRemote();
		enmNew = lCtrl.ExInserisciOENotifica(enm, prm);

		// valore di ritorno
		return IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
				+ "=siap.siep.misurasicurezza.action.ActLoadDettaglioRestituzioneOrdineConsegna&"
				+ ICostantiEvento.CAMPO_ID_EVENTO + "=" + enmNew.getEvento().getIdEvento();
	} // Chiude processRequest

} // Chiude Action