package siap.sius.presaincarico.action;

import java.math.BigDecimal;

import f3b.util.DateUtils;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;
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

public class ActConfermaPresaInCaricoAttivitaSiepe extends ActionSiap implements ICostantiJMS {

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

		String lEsito = "00000"; // Setto l'esito positivo

		MessaggioModel lMessIns = new MessaggioModel(lMess);

		// MisuraAlternativaModel lMisAlt = new MisuraAlternativaModel();

		IPresaInCarico lPres = SICOLookupRemote.getPresaInCaricoRemote();
		/* MessaggioModel lMessReturn = */lPres.ExPresaInCaricoAttivita(lMessIns);

		MessaggioModel lMessage = new MessaggioModel();

		lMessage.setDescrBdiDestinataria(lMess.getDescrBdiMittente());
		lMessage.setCodBdiDestinataria(lMess.getCodBdiMittente());
		lMessage.setCodUfficioDestinatario(lMess.getCodUfficioMittente());
		lMessage.setCodBdiMittente(lBDI.getCodDistretto());
		lMessage.setCodUfficioMittente(this.getCodUfficioUtenteConnesso());
		lMessage.setCodiceUtenteMittente(this.getCodUtenteConnesso());
		lMessage.setCodTipoMessaggio(ESITO);
		lMessage.setCodTipoOperazione(ESITO_TRASFERIMENTO_ATTIVITA);
		lMessage.setCodiceUtenteMittente(this.getCodUtenteConnesso());
		lMessage.setJmsCorrelationIdMessage(lMess.getJmsCorrelationIdMessage());
		lMessage.setDataInvio(DateUtils.getSysDate());
		lMessage.setDataEsito(DateUtils.getSysDate());
		lMessage.setCodEsito(lEsito);
		lMessage.setTreeModel(lMess.getTreeModel());

		SIAPSender lSender = new SIAPSender();
		lSender.send(lMessage);

		// Esclusione del messaggio di richiesta evaso.
		lMess.setFlagVisto("S");
		lMess.setDataEsito(DateUtils.getSysDate());
		lCrtl.ExModificaMessaggio(lMess);

		// Prepara la "pagina" di destinAction
		RedirectTo lRedirigi = new RedirectTo();
		lRedirigi.setPage(IWebConstants.PG_MAIN);
		lRedirigi.setAction("siap.sius.presaincarico.action.ActDettaglioAttivitaRicevuta");
		lRedirigi.setParameter(ICostantiPresaincarico.CAMPO_ID_MESSAGGIO, lIdMess.toString());
		String lMessageOut = "";
		setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi);
		lMessageOut += " Ricezione Attività Completata e Esito rispedito al Mittente.";

		// setta la risposta nella request
		setRequestAttribute(IWebConstants.MESSAGE_TEXT, lMessageOut);
		return IWebConstants.PG_MESSAGE; // restituisce la jsp di VIEW
	}

}