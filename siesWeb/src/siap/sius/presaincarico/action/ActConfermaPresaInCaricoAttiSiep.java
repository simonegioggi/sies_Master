package siap.sius.presaincarico.action;

import java.math.BigDecimal;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.F3BException;
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
import siap.jms.util.ParserMessage;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.soggetto.model.SoggettoModel;
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.web.ActionSiap;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.jms.controller.IPresaInCaricoJMS;
import siap.siep.sentenza.model.SentenzaModel;
import siap.siep.util.SIEPLookupRemote;
import siap.sius.SIUSException;

/**
 * Action di presa in carico atti ricevuti. Utilizzata sia sa SIEP che da SIUS sia per atti provenienti stessa
 * BDI che altra BDI.
 *
 * @author d.fiorletta
 * @version 1.0
 */
public class ActConfermaPresaInCaricoAttiSiep extends ActionSiap implements ICostantiJMS {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public String processRequest() throws Exception {

		// info per il log
		siesLogger.debug(getClass().getName() + ".processRequest: inizio");

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

		IMessaggio lCrtl = JMSLookupRemote.getMessaggioRemote();
		MessaggioModel lMess = lCrtl.ExRicercaMessaggioByKey(lIdMess);

		UfficioModel lBDI = getUfficioByCodUfficio(getCodUfficioUtenteConnesso());
		String lEsito = "00000"; // Setto l'esito positivo

		MessaggioModel lMessIns = new MessaggioModel(lMess);
		MessaggioModel lMessReturn = new MessaggioModel(lMess);

		try {
			IPresaInCaricoJMS lPres = SIEPLookupRemote.getPresaInCarico();
			// STUB 08/10/2004 Presa in carico Provvedimento LS va distinto dall'Istanza
			if (lMessIns.getCodTipoOperazione().compareTo(ICostantiJMS.TRASFERIMENTO_PROVVEDIMENTO) == 0)
				lMessReturn = lPres.ExInserisciProvvedimentoTrasmesso(lMessIns);
			// STUB 19/20/2008 Presa in carico Provvedimento di Sanzione Sostitutiva
			else if (lMessIns.getCodTipoOperazione()
					.compareTo(ICostantiJMS.TRASFERIMENTO_SANZIONE_SOSTITUTIVA) == 0
					// MEV_2023-33: aggiunto trasferimento pena sostitutiva
					|| lMessIns.getCodTipoOperazione()
							.compareTo(ICostantiJMS.TRASFERIMENTO_PENA_SOSTITUTIVA) == 0)
				lMessReturn = lPres.ExInserisciFascicoloSiep(lMessIns);
			// TRASFERIMENTO per COMPETENZA
			else if (lMessIns.getCodTipoOperazione().compareTo(ICostantiJMS.TRASFERIMENTO_COMPETENZA) == 0)
				lMessReturn = lPres.ExInserisciFascicoloSiep(lMessIns);
			// 16/03/2009 Presa in carico Provvedimento di Conversione Pene Pecuniarie
			else if (lMessIns.getCodTipoOperazione()
					.compareTo(ICostantiJMS.TRASFERIMENTO_ATTI_CONVERSIONE) == 0)
				lMessReturn = lPres.ExInserisciFascicoloSiep(lMessIns);
			else
				// lMessReturn = lPres.ExInserisciIstanzaTrasmessa(lMessIns); // 2010-04-30 sostituito
				lMessReturn = lPres.ExInserisciNuovaIstanzaTrasmessa(lMessIns);
		} catch (F3BException ex) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("Exception >>> " + ex, ex);
			throw new SIUSException(SIUSException.USER_MESSAGE,
					"Si è verificato un errore durante il caricamento dell'Atto pervenuto.<br> Riprovare in seguito.");
		}

		// ==================================================
		// Messaggio di risposta
		// ==================================================
		MessaggioModel lMessage = new MessaggioModel();

		lMessage.setDescrBdiDestinataria(lMess.getDescrBdiMittente());
		lMessage.setCodBdiDestinataria(lMess.getCodBdiMittente());
		lMessage.setCodUfficioDestinatario(lMess.getCodUfficioMittente());

		// >>>>>>>>> Destinazione fissa e non da maschera.......

		lMessage.setCodBdiMittente(lBDI.getCodDistretto());
		lMessage.setCodUfficioMittente(getCodUfficioUtenteConnesso());
		lMessage.setCodiceUtenteMittente(getCodUtenteConnesso());
		lMessage.setCodTipoMessaggio(ESITO);

		// STUB 08/10/2004 Presa in carico Provvedimento LS va distinto dall'Istanza
		if (lMessIns.getCodTipoOperazione().compareTo(ICostantiJMS.TRASFERIMENTO_PROVVEDIMENTO) == 0)
			lMessage.setCodTipoOperazione(ESITO_TRASFERIMENTO_PROVVEDIMENTO);
		// STUB 19/20/2008 Presa in carico Provvedimento di Sanzione Sostitutiva
		else if (lMessIns.getCodTipoOperazione()
				.compareTo(ICostantiJMS.TRASFERIMENTO_SANZIONE_SOSTITUTIVA) == 0)
			lMessage.setCodTipoOperazione(ESITO_TRASFERIMENTO_SANZIONE_SOSTITUTIVA);
		// MEV_2023-33: aggiunto trasferimento pena sostitutiva
		else if (lMessIns.getCodTipoOperazione().compareTo(ICostantiJMS.TRASFERIMENTO_PENA_SOSTITUTIVA) == 0)
			lMessage.setCodTipoOperazione(ESITO_TRASFERIMENTO_PENA_SOSTITUTIVA);
		// TRASFERIMENTO per COMPETENZA
		else if (lMessIns.getCodTipoOperazione().compareTo(ICostantiJMS.TRASFERIMENTO_COMPETENZA) == 0) {
			lMessage.setCodTipoOperazione(ESITO_TRASFERIMENTO_COMPETENZA);
			lMessage.setChiaveAnnoFasCumulante(lMessIns.getChiaveAnnoFasCumulante());
			lMessage.setChiaveProgrFasCumulante(lMessIns.getChiaveProgrFasCumulante());
		} else
			lMessage.setCodTipoOperazione(ESITO_TRASFERIMENTO_ISTANZA);

		// --- GDV---lMessage.setJmsCorrelationIdMessage(lMess.getJmsIdMessaggio());
		lMessage.setJmsCorrelationIdMessage(lMess.getJmsCorrelationIdMessage());
		lMessage.setDataInvio(DateUtils.getSysDate());
		if (lMessIns.getCodTipoOperazione().compareTo(ICostantiJMS.TRASFERIMENTO_COMPETENZA) == 0) {
			lMessage.setDataInvio(lMess.getDataInvio());
		}
		lMessage.setDataEsito(DateUtils.getSysDate());

		lMessage.setCodEsito(lEsito);
		lMessage.setTreeModel(lMess.getTreeModel());

		SIAPSender lSender = new SIAPSender();
		lSender.send(lMessage);

		// ==========================================
		// Marco il messaggio di richiesta evaso.
		// ==========================================
		lMess.setFlagVisto("S");
		lMess.setDataEsito(DateUtils.getSysDate());
		lCrtl.ExModificaMessaggio(lMess);

		// setta la risposta nella request
		// FIXME il messaggio di risposta dipende dal tipo di Atto Acquisito
		setRequestAttribute(IWebConstants.MESSAGE_TEXT,
				"Ricezione Atto Completata e Esito rispedito al Mittente. Iscrivere il procedimento Sius!");

		// Prepara la "pagina" di destinAction
		ParserMessage lPars;
		if (lMessReturn.getTreeModel() != null)
			lPars = new ParserMessage(lMessReturn.getTreeModel());
		else
			throw new SIUSException(SIUSException.USER_MESSAGE,
					"Impossibile porre il Fascicolo SIEP in sessione!");

		/// 26/03/2008 Prova recupero Evento
		/// EventoModel lEventoInviato = lPars.getEvento().getEvento();
		// 02/11/2009 Corretta l'impostazione dell'evento inviato:
		// Si punta all'ultimo elemento della lista (invece del primo) perchè è il più recente, ed è quindi
		/// quello relativo all'istanza.
		// EventoNotificaModel lEventoInviato =
		/// (EventoNotificaModel)lPars.getDettaglioFascicoloSiep().getEventi().get(0);
		EventoNotificaModel lEventoInviato = (EventoNotificaModel) lPars.getDettaglioFascicoloSiep()
				.getEventi().get(lPars.getDettaglioFascicoloSiep().getEventi().size() - 1);
		setSessionAttribute("IdEventoInviato", lEventoInviato.getEvento().getIdEvento());
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(">>>>>>>>> lEventoInviato = " + lEventoInviato);

		RedirectTo lRedirigi = new RedirectTo();
		lRedirigi.setPage(IWebConstants.PG_MAIN);
		lRedirigi.setAction("siap.sius.iscrizioneprocedimento.action.ActLoadIscrProcedimentoDaSiep");
		setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi);

		/// FascicoloSiepModel lFasSiepInviato = lPars.getFascicolo();
		FascicoloSiepModel lFasSiepInviato = lPars.getDettaglioFascicoloSiep().getFascicoloSiep();
		/// SoggettoModel lSoggettoInviato = lPars.getFascicolo().getSoggetto();
		SoggettoModel lSoggettoInviato = lPars.getDettaglioFascicoloSiep().getFascicoloSiep().getSoggetto();
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(">>>>>>>>> lSoggettoInviato = " + lSoggettoInviato);

		/// SentenzaModel lSentenzaInviata = lPars.getSentenza();
		SentenzaModel lSentenzaInviata = lPars.getDettaglioFascicoloSiep().getFascicoloSiep().getSentenza();

		lFasSiepInviato.setSoggetto(lSoggettoInviato);
		lFasSiepInviato.setSentenza(lSentenzaInviata);
		setSessionAttribute("soggetto", lSoggettoInviato);
		setSessionAttribute("fascicolo", lFasSiepInviato);

		setRequestAttribute("Messaggio", lMessReturn);

		String strReturn = "";
		// STUB 08/10/2004 Presa in carico Provvedimento LS va distinto dall'Istanza
		if (lMessIns.getCodTipoOperazione().compareTo(ICostantiJMS.TRASFERIMENTO_PROVVEDIMENTO) == 0)
			strReturn = IWebConstants.ROOT_DIR
					+ "files/siap/siep/ordineesecuzione/RapportoTrasferimentoProvvedimento.jsp";
		// STUB 19/02/2008 Presa in carico Provvedimento di Sanzione Sostitutiva
		else if (lMessIns.getCodTipoOperazione()
				.compareTo(ICostantiJMS.TRASFERIMENTO_SANZIONE_SOSTITUTIVA) == 0
				// MEV_2023-33: aggiunto trasferimento pena sostitutiva
				|| lMessIns.getCodTipoOperazione()
						.compareTo(ICostantiJMS.TRASFERIMENTO_PENA_SOSTITUTIVA) == 0) {
			String tipoOperazione = (lMessIns.getCodTipoOperazione()
					.compareTo(ICostantiJMS.TRASFERIMENTO_SANZIONE_SOSTITUTIVA) == 0) ? "Sanzione" : "Pena";
			setRequestAttribute("tipoOperazione", tipoOperazione);
			strReturn = IWebConstants.ROOT_DIR
					+ "files/siap/siep/sanzionesostitutiva/RapportoTrasferimentoSanzioneSostitutiva.jsp";
		}
		// TRASFERIMENTO per COMPETENZA
		else if (lMessIns.getCodTipoOperazione().compareTo(ICostantiJMS.TRASFERIMENTO_COMPETENZA) == 0)
			strReturn = IWebConstants.ROOT_DIR + "files/siap/siep/istanza/RapportoTrasferimentoIstanza.jsp";
		// 16/03/2009 Presa in carico Provvedimento di Conversione Pene Pecuniarie
		else if (lMessIns.getCodTipoOperazione().compareTo(ICostantiJMS.TRASFERIMENTO_ATTI_CONVERSIONE) == 0)
			strReturn = IWebConstants.ROOT_DIR
					+ "files/siap/siep/penapecuniaria/RapportoTrasferimentoAttoConversionePP.jsp";
		else
			strReturn = IWebConstants.ROOT_DIR + "files/siap/siep/istanza/RapportoTrasferimentoIstanza.jsp";

		// info per il log
		siesLogger.debug(getClass().getName() + ".processRequest: fine");

		// valore di ritorno
		return strReturn;
	}

}