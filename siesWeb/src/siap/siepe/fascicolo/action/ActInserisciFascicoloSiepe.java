package siap.siepe.fascicolo.action;

import java.math.BigDecimal;
import java.util.StringTokenizer;

import org.apache.log4j.Logger;

import siap.jms.ICostantiJMS;
import siap.jms.JMSLookupRemote;
import siap.jms.SIAPSender;
import siap.jms.messaggio.action.ICostantiMessaggio;
import siap.jms.messaggio.controller.IMessaggio;
import siap.jms.messaggio.model.MessaggioModel;
import siap.sico.lock.model.LockModel;
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.web.ActionSiap;
import siap.siepe.attivita.model.AttivitaModel;
import siap.siepe.fascicolo.controller.IFascicoloSiepe;
import siap.siepe.fascicolo.model.FascicoloSiepeModel;
import siap.siepe.util.SIEPELookupRemote;
import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;

/**
 * <p>
 * Title: ActInserisciFascicoloSiepe
 * </p>
 * <p>
 * Description: Classe Azione di inserimento del Fascicolo SIEPE L'azione elabora la Presa in Carico del
 * Messaggio da trattare e quindi la Creazione e la valorizzazione del Fascicolo SIEPE e delle Attività
 * corrispondenti.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: Bull
 * </p>
 */
public class ActInserisciFascicoloSiepe extends ActionSiap implements ICostantiFascicoloSiepe {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public String processRequest() throws Exception {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(getClass().getPackage().getName() + ".processRequest : inizio");

		if (isRequestParameterNullObj(ICostantiMessaggio.CAMPO_ID_MESSAGGIO))
			throw (new F3BException(F3BException.USER_MESSAGE, "Dati del Messaggio Assenti !"));

		// Lock sul messaggio per evitare che si attivino 2 prese in carico in contemporanea sullo stesso
		// messaggio.
		LockModel lck = lockIfNotLocked("MESSAGGIO",
				getRequestStringParameter(ICostantiMessaggio.CAMPO_ID_MESSAGGIO), getCodUtenteConnesso());
		if (lck != null) {
			setRequestAttribute(IWebConstants.MESSAGE_TEXT,
					"Il messaggio da elaborare è in gestione ad un altro utente! <BR>Riprovare più tardi!");
			return IWebConstants.PG_MESSAGE;
		}

		// Lettura ID Messaggio Ricevuto
		BigDecimal lIdMess = getRequestBigDecimalParameter(ICostantiMessaggio.CAMPO_ID_MESSAGGIO);
		// Lettura nel DB del massaggio
		IMessaggio lCrtl = JMSLookupRemote.getMessaggioRemote();
		MessaggioModel lMessaggioRicevuto = lCrtl.ExRicercaMessaggioByKey(lIdMess);

		// Creazione del Fascicolo SIEPE e sua valorizzazione
		FascicoloSiepeModel lFascicolo = new FascicoloSiepeModel();
		lFascicolo.setChiaveAnno(new BigDecimal(DateUtils.getSysDate("yyyy"))); // Anno corrente
		lFascicolo.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
		lFascicolo.setChiaveUfficio(lFascicolo.getCodUfficioInserimento());
		lFascicolo.setCodOperatoreInserimento(getCodUtenteConnesso());
		lFascicolo.setDataInserimento(DateUtils.getSysDate());
		lFascicolo.setDataIscrizione(lFascicolo.getDataInserimento());
		lFascicolo.setCodIncarico(getRequestStringParameter(CAMPO_COD_INCARICO));
		lFascicolo.setCodUfficioMittente(getRequestStringParameter(CAMPO_COD_UFFICIO_MITTENTE));
		lFascicolo.setAnnoUepe(getRequestBigDecimalParameter(CAMPO_ANNO_UEPE));
		lFascicolo.setNumUepe(getRequestBigDecimalParameter(CAMPO_NUM_UEPE));
		lFascicolo.setProgrUepe(getRequestBigDecimalParameter(CAMPO_PROGR_UEPE));
		lFascicolo.setNote(getRequestStringParameter(CAMPO_NOTE));
		lFascicolo.setCodStatoFascicolo("02");

		// Richiamo Controller per elaborazione dati
		IFascicoloSiepe lCtrl = SIEPELookupRemote.getFascicoloSiepeRemote();
		lFascicolo = lCtrl.ExInserisciFascicoloSiepe(lFascicolo, lMessaggioRicevuto, letturaAttivita());

		// Invio del messaggio di Ricevuta al mittente al termine dell'operazione di Presa in Carico
		if (lMessaggioRicevuto.getCodTipoOperazione().equalsIgnoreCase(ICostantiJMS.TRASFERIMENTO_ORDINANZA))
			InvioMessaggioRisposta(lMessaggioRicevuto, "00000", ICostantiJMS.ESITO,
					ICostantiJMS.ESITO_TRASFERIMENTO_ORDINANZA);

		// Prepara la "pagina" di destinAction
		RedirectTo lRedirigi = new RedirectTo();
		lRedirigi.setPage(IWebConstants.PG_MAIN);
		lRedirigi.setAction("siap.siepe.fascicolo.action.ActLoadDettaglioFascicoloSiepe");
		lRedirigi.setParameter(CAMPO_ID_FASCICOLO_SIEPE, lFascicolo.getIdFascicoloSiepe().toString());

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(getClass().getPackage().getName() + ".processRequest : fine");

		return lRedirigi.toString();
	}

	/**
	 * Lettura delle Attività assegnate al Fascicolo SIEPE. Vengono letti i codici attivita dalla form ed in
	 * base a questi valorizzato un array di AttivitaModel da archiviare.
	 * 
	 * @throws F3BException
	 */
	private AttivitaModel[] letturaAttivita() throws F3BException {

		// I codici attività sono elencati in un unico campo di input
		// e separati attraverso un carattere speciale.
		StringTokenizer lListaCodAttivita = new StringTokenizer(
				getRequestStringParameter(CAMPO_COD_ATTIVITA), "|");
		int numAttivita = lListaCodAttivita.countTokens();
		AttivitaModel lListaAttivita[] = new AttivitaModel[numAttivita];
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("N.ro Attività lette ->" + lListaAttivita.length);
		for (int i = 0; lListaCodAttivita.hasMoreTokens(); i++) {
			AttivitaModel lAttivita = new AttivitaModel();
			lAttivita.setCodTipoAttivita(lListaCodAttivita.nextToken());
			lAttivita.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
			lAttivita.setCodOperatoreInserimento(getCodUtenteConnesso());
			lAttivita.setDataInserimento(DateUtils.getSysDate());
			lAttivita.setDataInizio(DateUtils.getSysDate());
			lListaAttivita[i] = lAttivita;
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Attivita n." + i + " :" + lListaAttivita[i]);
		}
		return lListaAttivita;
	}

	/**
	 * Funzione di compilazione del messaggio da restituire al mittente per notificare l'avvenuta Presa in
	 * Carico.
	 * 
	 * @param aMessaggio
	 * @param aCodEsito
	 * @param aCodTipoMessaggio
	 * @param aCodTipoOperazione
	 * @return
	 * @throws Exception
	 */
	private MessaggioModel InvioMessaggioRisposta(MessaggioModel aMessaggio, String aCodEsito,
			String aCodTipoMessaggio, String aCodTipoOperazione) throws Exception {

		// Si ricava l'Ufficio dell'Utente Connesso
		UfficioModel lBDI = getUfficioByCodUfficio(getCodUfficioUtenteConnesso());

		// Preparazione del Messaggio da Rinviare al Mittente
		MessaggioModel lRetMess = new MessaggioModel();
		lRetMess.setDescrBdiDestinataria(aMessaggio.getDescrBdiMittente());
		lRetMess.setCodBdiDestinataria(aMessaggio.getCodBdiMittente());
		lRetMess.setCodUfficioDestinatario(aMessaggio.getCodUfficioMittente());
		lRetMess.setCodBdiMittente(lBDI.getCodDistretto());
		lRetMess.setCodUfficioMittente(getCodUfficioUtenteConnesso());
		lRetMess.setCodiceUtenteMittente(getCodUtenteConnesso());
		lRetMess.setCodTipoMessaggio(aCodTipoMessaggio);
		lRetMess.setCodTipoOperazione(aCodTipoOperazione);
		lRetMess.setCodiceUtenteMittente(getCodUtenteConnesso());
		lRetMess.setJmsCorrelationIdMessage(aMessaggio.getJmsCorrelationIdMessage());
		lRetMess.setDataInvio(DateUtils.getSysDate());
		lRetMess.setDataEsito(DateUtils.getSysDate());
		lRetMess.setCodEsito(aCodEsito);
		lRetMess.setTreeModel(aMessaggio.getTreeModel());

		// INVIO
		SIAPSender lSender = new SIAPSender();
		lSender.send(lRetMess);

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("Inviato messaggio di risposta al mittente");

		return lRetMess;
	}

}