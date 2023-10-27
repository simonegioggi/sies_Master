package siap.siep.rateizzazionepp.action;

import java.math.BigDecimal;
import java.util.Date;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.web.IWebConstants;
import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.ufficio.action.ICostantiUfficio;
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.web.ActionSiap;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.notifica.action.ICostantiNotifica;
import siap.siep.notifica.model.NotificaModel;
import siap.siep.rateizzazionepp.controller.IRateizzazionePP;
import siap.siep.util.SIEPLookupRemote;

/**
 * Classe Action per l'inserimento della Trasmissione Atti Conversione
 *
 * @author sgioggi
 * @since MEV_2023-33
 * @version 1.0
 */
public class ActInserisciTrasmissioneAttiConversione extends ActionSiap implements ICostantiRateizzazionePP {

	private static Logger siesLogger = Logger.getLogger(LogF3B.WS_PAGO_PA_LOG);

	public String processRequest() throws Exception {

		// info per il log
		siesLogger.info(getClass().getName() + ".processRequest: inizio");

		EventoNotificaModel enm = new EventoNotificaModel();
		NotificaModel[] nmArray = new NotificaModel[1];

		// ricevo dalla maschera l'Ufficio di Sorveglianza
		if (!isRequestParameterNullObj(CAMPO_COD_SEDE_UDS)
				&& getRequestStringParameter(CAMPO_COD_SEDE_UDS) != null
				&& !"".equals(getRequestStringParameter(CAMPO_COD_SEDE_UDS))) {
			// preparo un record Notifica
			String lUDS = getCodUfficioByCodTipoUfficioDescrComune(
					getRequestStringParameter(ICostantiUfficio.CAMPO_TIPO_UFFICIO),
					getRequestStringParameter(CAMPO_COD_SEDE_UDS));
			NotificaModel nm = new NotificaModel();
			nm.setCodEsito("-");
			nm.setCodOperatoreInserimento(getCodUtenteConnesso());
			nm.setDataInserimento(DateUtils.getSysDate());
			nm.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
			nm.setCodTipoNotifica("E");
			nm.setDataInvio(getRequestDateParameter(ICostantiNotifica.CAMPO_ANNO_DATA_INVIO,
					ICostantiNotifica.CAMPO_MESE_DATA_INVIO, ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO));
			nm.setUffCodUfficio(lUDS);
			// carico la Notifica UDS nella lista delle Notifiche
			nmArray[0] = nm;
		}

		enm.setNotifiche(nmArray);

		// Tipo Evento = Richiesta
		enm.getEvento().setCodTipoEvento("02");
		enm.getEvento().setCodTipoProvvedimento("31"); // Tipo Provvedimento = Trasmissione Atti
		enm.getEvento().setCodMotivo("0934");

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

		IRateizzazionePP irpp = SIEPLookupRemote.getRateizzazionePPRemote();
		BigDecimal idEvento = irpp.exInserisciTrasmissioneAttiConversione(enm);

		// info per il log
		siesLogger.info(getClass().getName() + ".processRequest: fine");

		// valore di ritorno
		return IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
				+ "=siap.siep.rateizzazionepp.action.ActDettaglioTrasmissioneAttiConversione&"
				+ ICostantiEvento.CAMPO_ID_EVENTO + "=" + idEvento;
	}

}