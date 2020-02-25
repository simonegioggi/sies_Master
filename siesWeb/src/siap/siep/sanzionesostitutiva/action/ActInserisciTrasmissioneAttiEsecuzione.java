package siap.siep.sanzionesostitutiva.action;

import java.math.BigDecimal;
import java.util.Date;

import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.misuraalternativa.action.ICostantiMisuraAlternativa;
import siap.siep.notifica.action.ICostantiNotifica;
import siap.siep.notifica.model.NotificaModel;
import f3b.util.DateUtils;
import f3b.web.IWebConstants;

/**
 * <p>
 * Title: ActInserisciTrasmissioneAttiEsecuzione
 * </p>
 * <p>
 * Description: ActInserisciTrasmissioneAttiEsecuzione
 * </p>
 * <p>
 * Copyright: Copyright (c) 2007
 * </p>
 * <p>
 * Company: Eutelia
 * </p>
 * 
 * @version 1.0
 */
public class ActInserisciTrasmissioneAttiEsecuzione extends ActionSiap implements
		ICostantiSanzioneSostitutiva {

	public String processRequest() throws Exception {

		EventoNotificaModel lEve = new EventoNotificaModel();

		// lEve.getEvento().setCodTipoEvento("19"); //Tipo Evento = Atti
		lEve.getEvento().setCodTipoEvento("02"); // Tipo Evento = Richiesta (poiché in sostanza l'evento che
													// si trasmette corrisponde ad una Richiesta di
													// applicazione Sanzione Sostitutiva.
		lEve.getEvento().setCodTipoProvvedimento("31"); // Tipo Provvedimento = Trasmissione Atti

		if (this.isRequestChecked("ritrasmissione")) {
			lEve.getEvento().setCodMotivo("0941");
		} else {
			lEve.getEvento().setCodMotivo("0396");
		}

		FascicoloSiepModel lFascicoloModel = (FascicoloSiepModel) getSessionAttribute("fascicolo");
		lEve.getEvento().setFasSieIdFascicoloSiep(lFascicoloModel.getIdFascicoloSiep());

		Date lDataEmissione = getRequestDateParameter(ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE,
				ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE, ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE);
		lEve.getEvento().setDataEmissione(lDataEmissione);

		UfficioModel lUff = this.getUfficioUtenteConnesso();

		lEve.getEvento().setCodOperatoreInserimento(getCodUtenteConnesso());

		lEve.getEvento().setCodLuogoEmittente(lUff.getCodComune());
		lEve.getEvento().setCodUfficioEmittente(lUff.getCodUfficio());
		lEve.getEvento().setDataInserimento(DateUtils.getSysDate());
		lEve.getEvento().setCodUfficioInserimento(lUff.getCodUfficio());
		lEve.getEvento().setAnnoProtocollo(new BigDecimal(DateUtils.getSysDate("yyyy")));
		lEve.getEvento().setCodEsito("-");
		lEve.getEvento().setCodLuogoDestinatario("-");
		lEve.getEvento().setCodUfficioDestinatario("-");
		lEve.getEvento().setCodTipoUfficioDestinatario("-");
		lEve.getEvento().setFlagStampaSiep("S");
		lEve.getEvento().setFlagVideoSiep("S");
		lEve.getEvento().setCodMagistrato(calcolaMagistrato());

		NotificaModel lNotifiche[] = new NotificaModel[1];
		NotificaModel lNotModTDS = new NotificaModel();

		if (!isRequestParameterNullObj(ICostantiSanzioneSostitutiva.CAMPO_SEDE_UFFICIO)
				&& getRequestStringParameter(ICostantiSanzioneSostitutiva.CAMPO_SEDE_UFFICIO) != null
				&& !getRequestStringParameter(ICostantiSanzioneSostitutiva.CAMPO_SEDE_UFFICIO).equals("")) {
			// MERGE v10 COLLAUDO: sostituita scritta fissa ("UDS") con valore preso dalla pagina
			String lTipoUfficio = "UDS";
			if (!isRequestParameterNullObj(ICostantiMisuraAlternativa.CAMPO_COD_UDS_TIPO)
					&& getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_COD_UDS_TIPO) != null
					&& !"".equals(getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_COD_UDS_TIPO)))
				lTipoUfficio = getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_COD_UDS_TIPO);
			String lUDS = getCodUfficioByCodTipoUfficioDescrComune(lTipoUfficio,
					getRequestStringParameter(ICostantiSanzioneSostitutiva.CAMPO_SEDE_UFFICIO));

			lNotModTDS.setCodEsito("-");
			lNotModTDS.setCodOperatoreInserimento(this.getCodUtenteConnesso());
			lNotModTDS.setDataInserimento(DateUtils.getSysDate());
			lNotModTDS.setCodUfficioInserimento(this.getCodUfficioUtenteConnesso());
			lNotModTDS.setCodTipoNotifica("E");
			lNotModTDS.setDataInvio(getRequestDateParameter(ICostantiNotifica.CAMPO_ANNO_DATA_INVIO,
					ICostantiNotifica.CAMPO_MESE_DATA_INVIO, ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO));

			lNotModTDS.setUffCodUfficio(lUDS);
		}

		lNotifiche[0] = lNotModTDS;
		lEve.setNotifiche(lNotifiche);

		IEvento lCtrl = SICOLookupRemote.getEventoRemote();
		EventoNotificaModel lRetModel = lCtrl.ExInserisciEventoNotifica(lEve);

		String lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
				+ "=siap.siep.sanzionesostitutiva.action.ActDettaglioTrasmissioneAttiEsecuzione&"
				+ ICostantiEvento.CAMPO_ID_EVENTO + "=" + lRetModel.getEvento().getIdEvento() + "&modalita=I";

		return lPage;
	}

}