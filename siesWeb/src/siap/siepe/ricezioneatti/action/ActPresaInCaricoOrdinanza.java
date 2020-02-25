package siap.siepe.ricezioneatti.action;

import java.math.BigDecimal;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.web.IWebConstants;
import siap.jms.ICostantiJMS;
import siap.jms.JMSLookupRemote;
import siap.jms.SIAPReceiver;
import siap.jms.SIAPSender;
import siap.jms.config.JMSProperties;
import siap.jms.messaggio.action.ICostantiMessaggio;
import siap.jms.messaggio.controller.IMessaggio;
import siap.jms.messaggio.model.MessaggioModel;
import siap.sico.jms.controller.IPresaInCarico;
import siap.sico.lock.model.LockModel;
import siap.sico.misuraalternativa.model.MisuraAlternativaModel;
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;

/**
 *
 * <p>
 * Title: ActPresaInCaricoOrdinanza
 * </p>
 * <p>
 * Description: Prende in Carico l'ordinanza
 * </p>
 */

/*
 * Luigi 29-06 2006 Questo file risulta da una copia di
 * siap.sius.depositoordinanzapc.action.ActConfermaPresaInCarico .
 * 
 */

public class ActPresaInCaricoOrdinanza extends ActionSiap implements ICostantiJMS {
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public String processRequest() throws Exception {
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(this.getClass().getName() + ".ActPresaInCaricoOrdinanza: inizio");

		// Controllo che non si stia lavorando su una entità in modifica ad altri
		LockModel lck = lockIfNotLocked("caricoordinanza",
				getRequestStringParameter(ICostantiMessaggio.CAMPO_ID_MESSAGGIO), getCodUtenteConnesso());
		if (lck != null) {
			setRequestAttribute(IWebConstants.MESSAGE_TEXT,
					"La presa in carico di questa ordinanza è in gestione ad un altro utente! <BR>Riprovare più tardi!");
			return IWebConstants.PG_MESSAGE;
		}

		if (JMSProperties.getInstance().getProperty("LISTENER_NUOVA_GESTIONE") != null && JMSProperties
				.getInstance().getProperty("LISTENER_NUOVA_GESTIONE").trim().equalsIgnoreCase("true")) {
			SIAPReceiver.getInstance().testInArrivo();
			SIAPReceiver.getInstance().testInPartenza();
			SIAPReceiver.getInstance().testStampa();
		} else {
			SIAPReceiver.getInstance();
		}

		BigDecimal lIdMess = getRequestBigDecimalParameter(ICostantiMessaggio.CAMPO_ID_MESSAGGIO);

		// DI fatto questa classe dovrebbe solo andare a vedere i risultati sulla
		// tabella DI MESSAGGIO.

		IMessaggio lMessCtrl = JMSLookupRemote.getMessaggioRemote();
		MessaggioModel lMess = lMessCtrl.ExRicercaMessaggioByKey(lIdMess);

		// !!!RENDERE DINAMICO!!!!
		String lEsito = "00000"; // Setto l'esito positivo

		MessaggioModel lMessIns = new MessaggioModel(lMess);
		MisuraAlternativaModel lMisAlt = new MisuraAlternativaModel();

		// Luigi 30-06-2006
		// lMisAlt.setNote(getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_NOTE));
		lMisAlt.setCodOperatoreAggiornamento(getCodUtenteConnesso());
		lMisAlt.setCodUfficioAggiornamento(getCodUfficioUtenteConnesso());
		lMisAlt.setDataAggiornamento(DateUtils.getSysDate());

		// lUIGI 29-06-2006 Viene chiamata la nuova Presa In Carico.
		// 13/12/2007 Cambiato il riferimento da SIUS.PresaInCaricoJMSController a
		// SICO.PresaInCaricoController.
		// IPresaInCarico lPres = SIUSLookupRemote.getPresaInCaricoSIEPE();
		IPresaInCarico lPres = SICOLookupRemote.getPresaInCaricoRemote();
		/* MessaggioModel lMessReturn = */lPres.ExPresaInCaricoOrdinanza(lMessIns, lMisAlt);

		// Si ricava l'Ufficio dell'Utente Connesso
		UfficioModel lBDI = getUfficioByCodUfficio(this.getCodUfficioUtenteConnesso());

		// Preparazione del Messaggio da Rinviare al Mittente
		MessaggioModel lMessage = new MessaggioModel();

		lMessage.setDescrBdiDestinataria(lMess.getDescrBdiMittente());
		lMessage.setCodBdiDestinataria(lMess.getCodBdiMittente());
		lMessage.setCodUfficioDestinatario(lMess.getCodUfficioMittente());
		// >>>>>>>>> Destinazione fissa e non da maschera.......
		lMessage.setCodBdiMittente(lBDI.getCodDistretto());
		lMessage.setCodUfficioMittente(getCodUfficioUtenteConnesso());
		lMessage.setCodiceUtenteMittente(getCodUtenteConnesso());
		lMessage.setCodTipoMessaggio(ESITO);
		lMessage.setCodTipoOperazione(ESITO_TRASFERIMENTO_ORDINANZA);
		lMessage.setCodiceUtenteMittente(getCodUtenteConnesso());
		lMessage.setJmsCorrelationIdMessage(lMess.getJmsCorrelationIdMessage());
		lMessage.setDataInvio(DateUtils.getSysDate());
		lMessage.setDataEsito(DateUtils.getSysDate());
		lMessage.setCodEsito(lEsito);
		lMessage.setTreeModel(lMess.getTreeModel());

		// INVIO
		SIAPSender lSender = new SIAPSender();
		lSender.send(lMessage);

		// Elimino il messaggio di richiesta evaso.
		// --------- TEST ----- Per ORa non cancello -------- lMessCtrl.ExCancellaMessaggio(lIdMess);

		lMess.setFlagVisto("S");
		lMess.setDataEsito(DateUtils.getSysDate());
		lMessCtrl.ExModificaMessaggio(lMess);

		// setta la risposta nella request
		setRequestAttribute(IWebConstants.MESSAGE_TEXT,
				"Ricezione Ordinanza Completata e Esito rispedito al Mittente!");

		// setta il fascicolo interessato in sessione--27/12/04--viviana--dario
		// ------------------------------------------------------------------------/
		/*
		 * Non si mette il fascicolo in sessione ! Luigi 18-08-2006 ParserMessage lPars; FascicoloSiepModel
		 * lFascicoloRicevuto = new FascicoloSiepModel(); if (lMessReturn.getTreeModel()!= null) { lPars = new
		 * ParserMessage(lMessReturn.getTreeModel()); if(lPars.getFascicolo() != null &&
		 * lPars.getFascicolo()!= null && lPars.getSentenza() != null && lPars.getFascicoloGPSius() != null &&
		 * lPars.getFascicoloGPSius().getFascicoloSiusModel() != null &&
		 * lPars.getFascicoloGPSius().getFascicoloSiusModel().getSoggetto() != null)
		 * 
		 * { lFascicoloRicevuto = lPars.getFascicolo();
		 * lFascicoloRicevuto.setSoggetto(lPars.getFascicoloGPSius().getFascicoloSiusModel().getSoggetto());
		 * lFascicoloRicevuto.setSentenza(lPars.getSentenza());
		 * 
		 * this.setSessionAttribute("fascicolo", lFascicoloRicevuto); }else throw new
		 * SIUSException(SIUSException.USER_MESSAGE, "Impossibile porre il Fascicolo SIEP in sessione!"); }
		 * else throw new SIUSException(SIUSException.USER_MESSAGE,
		 * "Impossibile porre il Fascicolo SIEP in sessione!");
		 */
		// ----------------------------------------------------------------------------/

		String retPage = ritornoDopoCancellazione("Presa in carico effettuata", "");

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(this.getClass().getName() + ".ActPresaInCaricoOrdinanza: fine");

		// return IWebConstants.PG_MESSAGE; //restituisce la jsp di VIEW
		return retPage;
	}
}