package siap.siep.rateizzazionepp.action;

import java.math.BigDecimal;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;
import siap.jms.ICostantiJMS;
import siap.jms.SIAPSender;
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
import siap.siep.jms.controller.ITrasmissioneJMS;
import siap.siep.notifica.model.NotificaModel;
import siap.siep.util.SIEPLookupRemote;
import siap.sius.SIUSException;

/**
 * Classe per il trasferimento degli Atti per la Conversione della Pena Pecuniaria che impacchetta i dati da
 * inviare nel messaggio, poi attiva l'invio del messaggio stesso ai destinatari
 *
 * @author sgioggi
 * @since MEV_2023-33
 * @version 1.0
 */
public class ActTrasferisciAttiConversione extends ActionSiap implements ICostantiEvento, ICostantiJMS {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public String processRequest() throws Exception {

		BigDecimal idEvento = getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO);
		FascicoloSiepModel fsm = (FascicoloSiepModel) getSessionAttribute("fascicolo");

		IEvento ie = SICOLookupRemote.getEventoRemote();
		EventoNotificaModel enm = ie.ExRicercaEventoNotificaByKey(idEvento);

		// flag di controllo invio messaggio
		boolean inviato = false;

		String[] tipoUff = getRequestStringParameters(ICostantiUfficio.CAMPO_TIPO_UFFICIO);
		String[] sedeUff = getRequestStringParameters(ICostantiEvento.CAMPO_COD_LUOGO_DESTINATARIO);

		// Dati BDI mittente
		UfficioModel um = getUfficioByCodUfficio(getUfficioUtenteConnesso().getCodDistretto());
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("BDI MIttente = " + um);

		String codiceUfficio = new String();
		UfficioModel umLocal = new UfficioModel();
		UfficioModel umBDI = new UfficioModel();

		ITrasmissioneJMS itjms = SIEPLookupRemote.getTrasmissioneJMS();
		MessaggioModel mm = itjms.getMessageForProvvedimento(idEvento, fsm.getIdFascicoloSiep());

		if (tipoUff[0].trim().compareTo("-") != 0) {
			codiceUfficio = getCodUfficioByCodTipoUfficioDescrComune(tipoUff[0], sedeUff[0]);
			umLocal = getUfficioByCodUfficio(codiceUfficio);
			umBDI = getUfficioByCodUfficio(umLocal.getCodDistretto());

			mm.setDescrBdiDestinataria(umBDI.getDescrComune());
			mm.setCodBdiDestinataria(umBDI.getCodUfficio());
			mm.setCodBdiMittente(um.getCodUfficio());
			mm.setDescrBdiMittente(um.getDescrComune());
			mm.setCodUfficioDestinatario(codiceUfficio);
			mm.setCodUfficioMittente(getCodUfficioUtenteConnesso());
			mm.setCodTipoMessaggio(RICHIESTA);
			mm.setCodTipoOperazione(TRASFERIMENTO_ATTI_CONVERSIONE);
			mm.setCodiceUtenteMittente(getCodUtenteConnesso());
			mm.setDataInvio(DateUtils.getSysDate());

			// SETTA RIFERIMENTI FASCICOLO SIEP/SIUS
			mm.setChiaveAnnoSiep(fsm.getChiaveAnno());
			mm.setChiaveProgrSiep(fsm.getChiaveProgr());

			if (fsm.getSoggetto() != null) {
				if (fsm.getSoggetto().getNome() != null)
					mm.setNomeSoggetto(fsm.getSoggetto().getNome());
				if (fsm.getSoggetto().getCognome() != null)
					mm.setCognomeSoggetto(fsm.getSoggetto().getCognome());
				if (fsm.getSoggetto().getDataNascita() != null)
					mm.setDataNascita(fsm.getSoggetto().getDataNascita());
				if (fsm.getSoggetto().getCodComuneNascita() != null)
					mm.setCodComuneNascita(fsm.getSoggetto().getCodComuneNascita());
				if (fsm.getSoggetto().getCodStatoNascita() != null)
					mm.setCodStatoNascita(fsm.getSoggetto().getCodStatoNascita());
			}
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("MESSAGGIO DA SPEDIRE A " + umBDI.getDescrComune());

			SIAPSender siaps = new SIAPSender();
			siaps.send(mm);
			inviato = true;
		}

		if (!inviato)
			throw new SIUSException(SIUSException.USER_MESSAGE,
					"Attenzione: selezionare almeno un destinatario!");

		setRequestAttribute("IdEvento", idEvento.toString());

		// ******** Esegue tutta una serie di operazioni sul DB **********************
		EventoModel em = new EventoModel();
		em.setIdEvento(idEvento);
		em.setDataTrasmissioneAtti(DateUtils.getSysDate());
		em.setCodUfficioDestinatario(codiceUfficio);
		em.setCodLuogoDestinatario(umLocal.getCodComune());

		NotificaModel nm = new NotificaModel(enm.getNotifiche()[0]);
		ie.ExConfermaTrasmissione(em, nm, "0253");

		// setta la risposta nella request
		setRequestAttribute(IWebConstants.MESSAGE_TEXT, "Trasmissione Documento sottomessa al Sistema!");

		// Prepara la "pagina" di destinAction.
		RedirectTo rt = new RedirectTo();
		rt.setPage(IWebConstants.PG_MAIN);
		rt.setParameter("IdEvento", idEvento.toString());
		if (isRequestParameterNullObj("codTipoOperazione")) {
			// setta la risposta nella request
			rt.setAction("siap.siep.fascicolo.action.ActLoadDettaglioFascicolo");
			setRequestAttribute(IWebConstants.MESSAGE_TEXT, "Trasmissione Documento sottomessa al Sistema!");
		}
		setRequestAttribute(IWebConstants.GOTO_PAGE, "" + rt);

		return IWebConstants.PG_MESSAGE;
	}

}