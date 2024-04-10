package siap.siep.rateizzazionepp.action;

import java.math.BigDecimal;
import java.util.Date;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.web.IWebConstants;
import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoModel;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.ufficio.action.ICostantiUfficio;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.notifica.action.ICostantiNotifica;
import siap.siep.notifica.model.NotificaModel;
import siap.siep.rateizzazionepp.controller.IRateizzazionePP;
import siap.siep.util.SIEPLookupRemote;

/**
 * Classe per modificare la Trasmissione Atti Conversione
 *
 * @author sgioggi
 * @since MEV_2023-33
 * @version 1.0
 */
public class ActModificaTrasmissioneAttiConversione extends ActionSiap implements ICostantiRateizzazionePP {

	private static Logger siesLogger = Logger.getLogger(LogF3B.WS_PAGO_PA_LOG);

	public String processRequest() throws Exception {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.info(getClass().getName() + ".processRequest: inizio");

		FascicoloSiepModel fsm = (FascicoloSiepModel) getSessionAttribute("fascicolo");
		siesLogger.debug("ID_FASCICOLO = " + fsm.getIdFascicoloSiep());
		BigDecimal idEvento = getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO);
		siesLogger.debug("ID_EVENTO = " + idEvento);

		EventoNotificaModel enm = new EventoNotificaModel();
		// Procedo all'Aggiornamento dell'evento
		IEvento ie = SICOLookupRemote.getEventoRemote();
		EventoModel em = ie.ExRicercaEventoByKey(idEvento);
		Date dataEmissione = getRequestDateParameter(ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE,
				ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE, ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE);
		em.setDataEmissione(dataEmissione);
		em.setCodLuogoEmittente(getCodComuneUtenteConnesso());
		em.setCodUfficioEmittente(getCodUfficioUtenteConnesso());
		em.setCodOperatoreAggiornamento(getCodUtenteConnesso());
		em.setDataAggiornamento(DateUtils.getSysDate());
		em.setCodUfficioAggiornamento(getCodUfficioUtenteConnesso());
		// Magistrato
		em.setCodMagistrato(calcolaMagistrato());

		// Procedo all'Aggiornamento delle notifiche
		NotificaModel[] nmArray = new NotificaModel[1];
		// ricevo dalla maschera l'Ufficio di Sorveglianza
		String codUfficioUDS = getCodUfficioByCodTipoUfficioDescrComune(
				getRequestStringParameter(ICostantiUfficio.CAMPO_TIPO_UFFICIO),
				getRequestStringParameter(CAMPO_COD_SEDE_UDS));
		NotificaModel nm = new NotificaModel();
		nm.setCodEsito("-");
		nm.setCodiceOperatoreAggiornamento(getCodUtenteConnesso());
		nm.setDataAggiornamento(DateUtils.getSysDate());
		nm.setCodUfficioAggiornamento(getCodUfficioUtenteConnesso());
		nm.setCodTipoNotifica("E");
		nm.setDataInvio(getRequestDateParameter(ICostantiNotifica.CAMPO_ANNO_DATA_INVIO,
				ICostantiNotifica.CAMPO_MESE_DATA_INVIO, ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO));
		nm.setUffCodUfficio(codUfficioUDS);
		// carico la Notifica UDS nella lista delle Notifiche
		nmArray[0] = nm;
		enm.setEvento(em);
		enm.setNotifiche(nmArray);

		IRateizzazionePP irpp = SIEPLookupRemote.getRateizzazionePPRemote();
		EventoNotificaModel enmRet = irpp.exModificaTrasmissioneAttiConversione(enm);

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.info(getClass().getName() + ".processRequest: fine");

		return IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
				+ "=siap.siep.rateizzazionepp.action.ActDettaglioTrasmissioneAttiConversione&"
				+ ICostantiEvento.CAMPO_ID_EVENTO + "=" + enmRet.getEvento().getIdEvento();
	}

}