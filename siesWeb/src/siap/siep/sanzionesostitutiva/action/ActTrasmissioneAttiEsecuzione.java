package siap.siep.sanzionesostitutiva.action;

import java.math.BigDecimal;
import java.util.Date;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.web.IWebConstants;
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

/**
 * Classe Action per la trasmissione atti per l'esecuzione
 *
 * @author sgioggi
 * @since MEV_2023-33
 * @version 1.0
 */
public class ActTrasmissioneAttiEsecuzione extends ActionSiap implements ICostantiSanzioneSostitutiva {

	// info per il log
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public String processRequest() throws Exception {

		// info per il log
		siesLogger.debug(getClass().getName() + ".processRequest: inizio");

		EventoNotificaModel enm = new EventoNotificaModel();

		// Tipo Evento = Richiesta (poiché in sostanza l'evento che si trasmette corrisponde ad una Richiesta
		// di applicazione Sanzione Sostitutiva
		enm.getEvento().setCodTipoEvento("02");
		// Tipo Provvedimento = Trasmissione Atti
		enm.getEvento().setCodTipoProvvedimento("31");

		if (isRequestChecked("ritrasmissione"))
			enm.getEvento().setCodMotivo("0941");
		else
			enm.getEvento().setCodMotivo("0396");

		FascicoloSiepModel fsm = (FascicoloSiepModel) getSessionAttribute("fascicolo");
		enm.getEvento().setFasSieIdFascicoloSiep(fsm.getIdFascicoloSiep());

		Date dataEmissione = getRequestDateParameter(ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE,
				ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE, ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE);
		enm.getEvento().setDataEmissione(dataEmissione);

		UfficioModel um = getUfficioUtenteConnesso();
		enm.getEvento().setCodOperatoreInserimento(getCodUtenteConnesso());
		enm.getEvento().setCodLuogoEmittente(um.getCodComune());
		enm.getEvento().setCodUfficioEmittente(um.getCodUfficio());
		enm.getEvento().setDataInserimento(DateUtils.getSysDate());
		enm.getEvento().setCodUfficioInserimento(um.getCodUfficio());
		enm.getEvento().setAnnoProtocollo(new BigDecimal(DateUtils.getSysDate("yyyy")));
		enm.getEvento().setCodEsito("-");
		enm.getEvento().setCodLuogoDestinatario("-");
		enm.getEvento().setCodUfficioDestinatario("-");
		enm.getEvento().setCodTipoUfficioDestinatario("-");
		enm.getEvento().setFlagStampaSiep("S");
		enm.getEvento().setFlagVideoSiep("S");
		enm.getEvento().setCodMagistrato(calcolaMagistrato());

		NotificaModel notificheArray[] = new NotificaModel[1];
		NotificaModel nm = new NotificaModel();

		if (!isRequestParameterNullObj(ICostantiSanzioneSostitutiva.CAMPO_SEDE_UFFICIO)
				&& getRequestStringParameter(ICostantiSanzioneSostitutiva.CAMPO_SEDE_UFFICIO) != null
				&& !getRequestStringParameter(ICostantiSanzioneSostitutiva.CAMPO_SEDE_UFFICIO).equals("")) {
			String tipoUfficio = "UDS";
			if (!isRequestParameterNullObj(ICostantiMisuraAlternativa.CAMPO_COD_UDS_TIPO)
					&& getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_COD_UDS_TIPO) != null
					&& !"".equals(getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_COD_UDS_TIPO)))
				tipoUfficio = getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_COD_UDS_TIPO);
			String codUfficio = getCodUfficioByCodTipoUfficioDescrComune(tipoUfficio,
					getRequestStringParameter(ICostantiSanzioneSostitutiva.CAMPO_SEDE_UFFICIO));

			nm.setCodEsito("-");
			nm.setCodOperatoreInserimento(getCodUtenteConnesso());
			nm.setDataInserimento(DateUtils.getSysDate());
			nm.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
			nm.setCodTipoNotifica("E");
			nm.setDataInvio(getRequestDateParameter(ICostantiNotifica.CAMPO_ANNO_DATA_INVIO,
					ICostantiNotifica.CAMPO_MESE_DATA_INVIO, ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO));
			nm.setUffCodUfficio(codUfficio);
		}

		notificheArray[0] = nm;
		enm.setNotifiche(notificheArray);

		IEvento ie = SICOLookupRemote.getEventoRemote();
		EventoNotificaModel enmRet = ie.ExInserisciEventoNotifica(enm);

		String lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
				+ "=siap.siep.sanzionesostitutiva.action.ActLoadDettaglioTrasmissioneAttiEsecuzione&"
				+ ICostantiEvento.CAMPO_ID_EVENTO + "=" + enmRet.getEvento().getIdEvento() + "&modalita=I";


		// info per il log
		siesLogger.debug(getClass().getName() + ".processRequest: fine");

		// pagina di ritorno
		return lPage;
	}

}