package siap.siep.sanzionesostitutiva.action;

import java.math.BigDecimal;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;
import siap.jms.ICostantiJMS;
import siap.jms.SIAPSender;
import siap.jms.messaggio.action.ICostantiMessaggio;
import siap.jms.messaggio.model.MessaggioModel;
import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoModel;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.ufficio.action.ICostantiUfficio;
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.jms.controller.IRicercaJMS;
import siap.siep.notifica.model.NotificaModel;
import siap.siep.util.SIEPLookupRemote;

/**
 * Classe per la conferma della trasmissione atti esecuzione pena sostitutiva
 *
 * @author sgioggi
 * @since MEV_2023-33
 * @version 1.0
 */
public class ActConfermaTrasmissioneAttiEsecuzionePS extends ActionSiap implements ICostantiJMS {

	// info per il log
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public String processRequest() throws Exception {

		// info per il log
		siesLogger.debug(getClass().getName() + ".processRequest: inizio");

		BigDecimal idEvento = getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO);

		IEvento ie = SICOLookupRemote.getEventoRemote();
		EventoNotificaModel enm = ie.ExRicercaEventoNotificaByKey(idEvento);

		if (enm != null && enm.getEvento() != null
				&& !"S".equals(enm.getEvento().getFlagDocumentoRegistrato())) {
			EventoModel em = new EventoModel();
			em.setIdEvento(idEvento);
			em.setDataAggiornamento(DateUtils.getSysDate());
			em.setCodUfficioAggiornamento(getCodUfficioUtenteConnesso());
			em.setCodOperatoreAggiornamento(getCodUtenteConnesso());
			em.setFlagDocumentoRegistrato("S");
			ie.ExUpdateDocument(em);
		}

		// Preparo la trasmissione vera e propria dell'Istanza
		FascicoloSiepModel fsm = (FascicoloSiepModel) getSessionAttribute("fascicolo");

		String codUfficio = getRequestStringParameter(ICostantiUfficio.CAMPO_COD_UFFICIO);

		UfficioModel um = getUfficioByCodUfficio(codUfficio);
		UfficioModel umBDIDestinataria = getUfficioByCodUfficio(um.getCodDistretto());
		UfficioModel umBDIMittente = getUfficioByCodUfficio(getUfficioUtenteConnesso().getCodDistretto());

		// ******** Esegue tutta una serie di operazioni sul DB locale **********************
		EventoModel em = new EventoModel();
		em.setIdEvento(idEvento);
		em.setDataTrasmissioneAtti(DateUtils.getSysDate());
		em.setCodUfficioDestinatario(codUfficio);
		em.setCodLuogoDestinatario(um.getCodComune());

		NotificaModel nm = new NotificaModel(enm.getNotifiche()[0]);
		ie.ExConfermaTrasmissione(em, nm, "0369");

		IRicercaJMS irjms = SIEPLookupRemote.getRicercaJMS();
		MessaggioModel mm = irjms.ExRicercaFascicoloSiepPerTrasferimento(fsm);
		mm.setDescrBdiDestinataria(umBDIDestinataria.getDescrComune());
		mm.setCodBdiDestinataria(umBDIDestinataria.getCodUfficio());
		mm.setCodBdiMittente(umBDIMittente.getCodUfficio());
		mm.setDescrBdiMittente(umBDIMittente.getDescrComune());
		mm.setCodUfficioDestinatario(codUfficio);
		mm.setCodUfficioMittente(getCodUfficioUtenteConnesso());
		mm.setCodTipoMessaggio(RICHIESTA);
		mm.setCodTipoOperazione(TRASFERIMENTO_SANZIONE_SOSTITUTIVA);
		mm.setCodiceUtenteMittente(getCodUtenteConnesso());
		mm.setDataInvio(DateUtils.getSysDate());
		// SETTA RIFERIMENTI FASCICOLO SIEP
		mm.setChiaveAnnoSiep(fsm.getChiaveAnno());
		mm.setChiaveProgrSiep(fsm.getChiaveProgr());

		SIAPSender siaps = new SIAPSender();
		siaps.send(mm);

		// setta la risposta nella request
		setRequestAttribute(IWebConstants.MESSAGE_TEXT, "Trasmissione Provvedimento sottomessa al Sistema!");

		// Prepara la "pagina" di destinAction
		RedirectTo rt = new RedirectTo();
		rt.setPage(IWebConstants.PG_MAIN);
		rt.setAction("siap.siep.sanzionesostitutiva.action.ActLoadDettaglioTrasmissioneAttiEsecuzione");
		rt.setParameter(ICostantiMessaggio.CAMPO_ID_MESSAGGIO, mm.getIdMessaggio().toString());
		rt.setParameter(ICostantiEvento.CAMPO_ID_EVENTO, idEvento.toString());
		setRequestAttribute(IWebConstants.GOTO_PAGE, "" + rt);

		// info per il log
		siesLogger.debug(getClass().getName() + ".processRequest: fine");

		// valore di ritorno
		return IWebConstants.PG_MESSAGE;
	}

}