package siap.sius.presaincarico.action;

import java.math.BigDecimal;
import java.util.Date;

import siap.jms.ICostantiJMS;
import siap.jms.JMSLookupRemote;
import siap.jms.SIAPReceiver;
import siap.jms.SIAPSender;
import siap.jms.config.JMSProperties;
import siap.jms.messaggio.action.ICostantiMessaggio;
import siap.jms.messaggio.controller.IMessaggio;
import siap.jms.messaggio.model.MessaggioModel;
import siap.sico.jms.controller.IPresaInCarico;
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import f3b.util.DateUtils;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;

public class ActConfermaPresaInCaricoRichiestaSiepe extends ActionSiap implements ICostantiJMS {

	/**
	 * Azione di conferma della presa incarico della Richiesta SIEPE.
	 * <p>
	 * 
	 * @return Nome della pagina JSP da visualizzare al termine dell'elaborazione
	 * @throws Exception
	 *             propaga errore di eccezione.
	 */
	public String processRequest() throws Exception {

		if (JMSProperties.getInstance().getProperty("LISTENER_NUOVA_GESTIONE") != null && JMSProperties
				.getInstance().getProperty("LISTENER_NUOVA_GESTIONE").trim().equalsIgnoreCase("true")) {
			SIAPReceiver.getInstance().testInArrivo();
			SIAPReceiver.getInstance().testInPartenza();
			SIAPReceiver.getInstance().testStampa();
		} else {
			SIAPReceiver.getInstance();
		}

		BigDecimal lIdMess = getRequestBigDecimalParameter(ICostantiMessaggio.CAMPO_ID_MESSAGGIO);

		IMessaggio lCrtl = JMSLookupRemote.getMessaggioRemote();
		MessaggioModel lMess = lCrtl.ExRicercaMessaggioByKey(lIdMess);

		UfficioModel lBDI = this.getUfficioByCodUfficio(this.getCodUfficioUtenteConnesso());

		String lEsito = "00000"; // Imposta l'esito positivo.

		MessaggioModel lMessIns = new MessaggioModel(lMess);

		IPresaInCarico lPres = SICOLookupRemote.getPresaInCaricoRemote();

		// Sostituire con il nuovo metodo di presa in carico Richiesta
		/* MessaggioModel lMessReturn = */lPres.ExPresaInCaricoRichiestaSiepe(lMessIns);

		// Preleva l'orario di sistema.
		Date lSysDate = DateUtils.getSysDate();

		// Istnzia e imposta i valori nel MessaggioModel.
		MessaggioModel lMessage = new MessaggioModel();
		lMessage.setDescrBdiDestinataria(lMess.getDescrBdiMittente());
		lMessage.setCodBdiDestinataria(lMess.getCodBdiMittente());
		lMessage.setCodUfficioDestinatario(lMess.getCodUfficioMittente());
		lMessage.setCodBdiMittente(lBDI.getCodDistretto());
		lMessage.setCodUfficioMittente(this.getCodUfficioUtenteConnesso());
		lMessage.setCodiceUtenteMittente(this.getCodUtenteConnesso());
		lMessage.setCodTipoMessaggio(ESITO);
		lMessage.setCodTipoOperazione(ESITO_TRASFERIMENTO_RICHIESTA_UEPE);
		lMessage.setCodiceUtenteMittente(this.getCodUtenteConnesso());
		lMessage.setJmsCorrelationIdMessage(lMess.getJmsCorrelationIdMessage());
		lMessage.setDataInvio(lSysDate);
		lMessage.setDataEsito(lSysDate);
		lMessage.setCodEsito(lEsito);
		lMessage.setTreeModel(lMess.getTreeModel());

		SIAPSender lSender = new SIAPSender();
		lSender.send(lMessage);

		// Esclusione del messaggio di richiesta evaso.
		lMess.setFlagVisto("S");
		lMess.setDataEsito(lSysDate);
		lCrtl.ExModificaMessaggio(lMess); // Esegue la modifica del Messaggio appena processato.

		// Prepara la "pagina" di destinAction
		RedirectTo lRedirigi = new RedirectTo();
		lRedirigi.setPage(IWebConstants.PG_MAIN);
		lRedirigi.setAction("siap.sius.presaincarico.action.ActDettaglioRichiestaSiepeRicevuta");
		lRedirigi.setParameter(ICostantiPresaincarico.CAMPO_ID_MESSAGGIO, lIdMess.toString());
		String lMessageOut = "";
		setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi);
		lMessageOut += " Ricezione della Richiesta UEPE completata e l'esito è stato rispedito al Mittente.";

		// Imposta la risposta nella request.
		setRequestAttribute(IWebConstants.MESSAGE_TEXT, lMessageOut);
		return IWebConstants.PG_MESSAGE; // restituisce la jsp di VIEW
	}

}