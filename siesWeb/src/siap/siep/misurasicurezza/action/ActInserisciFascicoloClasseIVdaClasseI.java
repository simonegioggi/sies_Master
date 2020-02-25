package siap.siep.misurasicurezza.action;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Vector;

import org.apache.log4j.Logger;

import siap.jms.ICostantiJMS;
import siap.jms.JMSLookupRemote;
import siap.jms.SIAPReceiver;
import siap.jms.SIAPSender;
import siap.jms.config.JMSProperties;
import siap.jms.messaggio.action.ICostantiMessaggio;
import siap.jms.messaggio.controller.IMessaggio;
import siap.jms.messaggio.model.MessaggioModel;
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.utente.model.UtenteModel;
import siap.sico.web.ActionSiap;
import siap.siep.cumulo.controller.ICumulo;
import siap.siep.cumulo.model.CumuloModel;
import siap.siep.fascicolo.action.ICostantiFascicoloSiep;
import siap.siep.fascicolo.controller.IFascicoloSiep;
import siap.siep.fascicolo.model.DettaglioFascicoloModel;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.misurasicurezza.controller.IMisuraSicurezza;
import siap.siep.util.SIEPLookupRemote;
import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.util.xml.TreeModel;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;

/**
 * Action per l'inserimento di un fascicolo di esecuzione delle misure di sicurezza (classe IV) ribaltando i
 * dati di un fascicolo di classe I o IV trasmesso per esempio per competenza o già in carico all'ufficio.
 * 
 * La action consente l'iscrizione ribaltando alcuni dati del fascicolo si cui sono iscritte le Misure di
 * Sicurezza.
 * 
 * @author d.fiorletta
 *
 */
public class ActInserisciFascicoloClasseIVdaClasseI extends ActionSiap implements ICostantiMisuraSicurezza {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	@SuppressWarnings("rawtypes")
	public String processRequest() throws Exception {

		UtenteModel lUtenteMod = getUtenteConnesso();

		BigDecimal lIdFascicoloSiepOrigine = null;
		if (!isRequestParameterNullObj(ICostantiFascicoloSiep.CAMPO_ID_FASCICOLO_SIEP))
			lIdFascicoloSiepOrigine = getRequestBigDecimalParameter(ICostantiFascicoloSiep.CAMPO_ID_FASCICOLO_SIEP);
		else
			throw new F3BException("IdFasciolo Origine assente nella request");

		// ==========================================================================
		// Recupero i dati del fascicoli di origine
		// ==========================================================================
		DettaglioFascicoloModel lDettaglioFascicoloOrig = null;
		IFascicoloSiep lCtrlFas = SIEPLookupRemote.getFascicoloSiepRemote();
		lDettaglioFascicoloOrig = lCtrlFas.ExDettaglioFascicoloSiep(lIdFascicoloSiepOrigine);

		Date lDataCumulo = null;
		if (lDettaglioFascicoloOrig.getFascicoloSiep() != null
				&& lDettaglioFascicoloOrig.getFascicoloSiep().getIdFascicoloSiep() != null
				&& lDettaglioFascicoloOrig.getFascicoloSiep().getFlagCumulante() != null) {
			if (lDettaglioFascicoloOrig.getFascicoloSiep().getFlagCumulante().compareTo("S") == 0) {
				ICumulo CtrlC = SIEPLookupRemote.getCumuloRemote();
				Vector lVec = CtrlC
						.ExRicercaFascicoliCumulobyIdFascicoloSiepValidatoDataCumuloNotNull(lDettaglioFascicoloOrig
								.getFascicoloSiep().getIdFascicoloSiep());
				if (lVec.size() > 0) {
					CumuloModel lCum = (CumuloModel) lVec.get(0);
					lDataCumulo = lCum.getDataInserimento();
					lDataCumulo = lCum.getDataCumulo();
				}
			}
		}

		// ==========================================================================
		// Inizializzo i dati del fascicolo di classe IV
		// ==========================================================================
		FascicoloSiepModel lFasMod = new FascicoloSiepModel();
		if (getRequestStringParameter("assegnazione_manuale").equals("S")) {
			lFasMod.setChiaveAnno(getRequestBigDecimalParameter(ICostantiFascicoloSiep.CAMPO_CHIAVE_ANNO));
			lFasMod.setChiaveProgr(getRequestBigDecimalParameter(ICostantiFascicoloSiep.CAMPO_CHIAVE_PROGR));
		} else {
			lFasMod.setChiaveAnno(new BigDecimal(DateUtils.getSysDate("yyyy"))); // Anno corrente
			lFasMod.setChiaveProgr(null);
			// Il progressivo del fascicolo (in base all'anno e all'ufficio) viene calcolato dal controller
		}

		lFasMod.setChiaveUfficio(getCodUfficioUtenteConnesso()); // Ufficio dell'operatore che inserisce

		lFasMod.setCodStatoFascicolo("02"); // Stato fascicolo settato ad Iscritto
		lFasMod.setCodMotivoArchiviazione("-"); // Motivo di archiviazione '-' per le join
		lFasMod.setCodTipoPosLibero("-"); // '-' per le join

		if (getRequestStringParameter("assegnazione_manuale").equals("S")) {
			lFasMod.setDataIscrizione(getRequestDateParameter(
					ICostantiFascicoloSiep.CAMPO_ANNO_ISCRIZIONE_ATTI,
					ICostantiFascicoloSiep.CAMPO_MESE_ISCRIZIONE_ATTI,
					ICostantiFascicoloSiep.CAMPO_GIORNO_ISCRIZIONE_ATTI));
		} else {
			// Data odierna senza HHmmss
			String lDataOdierna = DateUtils.getSysDate("dd/MM/yyyy");
			lFasMod.setDataIscrizione(DateUtils.getDate(lDataOdierna, "dd/MM/yyyy"));
		}

		lFasMod.setDataIrrevocabilita(getRequestDateParameter(
				ICostantiFascicoloSiep.CAMPO_ANNO_DATA_IRREVOCABILITA,
				ICostantiFascicoloSiep.CAMPO_MESE_DATA_IRREVOCABILITA,
				ICostantiFascicoloSiep.CAMPO_GIORNO_DATA_IRREVOCABILITA));

		lFasMod.setNote(getRequestStringParameter(ICostantiFascicoloSiep.CAMPO_NOTE));

		lFasMod.setFlagValidato("N"); // Il flag di validazione viene impostato a 'NO'
		lFasMod.setFlagAltraCausa("N"); // Il flag altra causa viene gestito nella gestione della posizione
										// giuridica

		lFasMod.setCodOperatoreInserimento(lUtenteMod.getUserId());
		lFasMod.setDataInserimento(DateUtils.getSysDate());
		lFasMod.setCodUfficioInserimento(getCodUfficioUtenteConnesso());

		// Soggetto: il controller procederà alla duplicazione del soggetto e quindi
		// a valorizzare sogIdSoggetto
		lFasMod.setSogIdSoggetto(null);
		// Sentenza: il controller procederà a verificare se la Sentenza va duplicata
		// e quindi a valorizzare SenIdSentenza
		lFasMod.setSenIdSentenza(null);

		lFasMod.setTipoProgressivo(getRequestIntParameter("tipo")); // sempre 4

		lFasMod.setDataArrivoAtto(getRequestDateParameter(ICostantiFascicoloSiep.CAMPO_ANNO_ARRIVO_ATTO,
				ICostantiFascicoloSiep.CAMPO_MESE_ARRIVO_ATTO,
				ICostantiFascicoloSiep.CAMPO_GIORNO_ARRIVO_ATTO));

		BigDecimal lIdMessaggio = null;
		if (!isRequestParameterNullObj(ICostantiMessaggio.CAMPO_ID_MESSAGGIO)
				&& getRequestStringParameter(ICostantiMessaggio.CAMPO_ID_MESSAGGIO).length() > 0) {
			lIdMessaggio = getRequestBigDecimalParameter(ICostantiMessaggio.CAMPO_ID_MESSAGGIO);
		}

		IMisuraSicurezza lCtrlMisCis = SIEPLookupRemote.getMisuraSicurezzaRemote();
		lCtrlMisCis.ExInserisciFascicoloClasseIV(lFasMod, lDettaglioFascicoloOrig, lIdMessaggio, lDataCumulo);

		// ==========================================================================
		// Prevedere eventuale messaggio di avvenuta iscrizione nel caso di
		// presa in carico da fuori BDI
		// ==========================================================================
		boolean lEsitoInvioRisposta = true;
		if (lIdMessaggio != null) {
			// L'iscrizione proviene dalla presa in carico quindi da un fascicolo di
			// altro uffiico. Se di altra BDI devo inviare risposta di avvenuta
			// iscrizione in modo che possa essere aggiornata la tabella
			// FASC_MS_TO_FASC_SIEP e sul dettaglio del fascicolo trasmesso compaia
			// il riferimento al fascicolo di classe IV
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger
					.debug("Iscrizione da presa in carico. Aggiorno lo stato del messaggio e invio Messaggio di avvenuta iscrizione.");

			// Invio il mesaggio di iscrizione e aggiorno lo stato locale
			lEsitoInvioRisposta = this.inviaInscrizioneProcedimento(lFasMod, lIdMessaggio);

		}

		// restituisce la jsp di VIEW
		// String lStringNomeAzione =
		// "&NomeAzione=siap.siep.misurasicurezza.action.ActInserisciFascicoloClasseIVdaClasseI";
		// return IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD +
		// "=siap.siep.fascicolo.action.ActLoadDettaglioFascicolo&"+
		// ICostantiFascicoloSiep.CAMPO_ID_FASCICOLO_SIEP+"="+lFasMod.getIdFascicoloSiep().toString()+
		// lStringNomeAzione;

		// setta la risposta nella request
		String messaggio = " Fascicolo correttamente Iscritto in classe IV.";
		if (lIdMessaggio != null) {
			if (lEsitoInvioRisposta)
				messaggio += " Inviata risposta di avvenuta iscrizione all'Ufficio Titolare degli atti presi in carico.";
			else
				messaggio += " Non è stato possibile inviare risposta all'Ufficio Titolare degli atti presi in carico.";
		}
		setRequestAttribute(IWebConstants.MESSAGE_TEXT, messaggio);

		// Prepara la "pagina" di destinAction
		RedirectTo lRedirigi = new RedirectTo();
		lRedirigi.setPage(IWebConstants.PG_MAIN);
		lRedirigi.setAction("siap.siep.fascicolo.action.ActLoadDettaglioFascicolo");
		lRedirigi.setParameter(ICostantiFascicoloSiep.CAMPO_ID_FASCICOLO_SIEP, ""
				+ lFasMod.getIdFascicoloSiep().toString());
		setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi);

		return IWebConstants.PG_MESSAGE;

	}

	/**
	 * Modulo che provvede all'invio del messaggio, all'ufficio titolare del fascicolo da cui sono stati
	 * ricevuti gli atti, di avvenuta iscrizione
	 * 
	 * @param aFascicoloModel
	 *            - Fascicolo di classe IV appena iscritto
	 * @param aIdMessaggio
	 *            - id del messaggio di richiesta a cui rispondere
	 * @return true se invio risposta avvenuto correttamente false se errore in fase di invio
	 * @throws Exception
	 */
	private boolean inviaInscrizioneProcedimento(FascicoloSiepModel aFascicoloModel, BigDecimal aIdMessaggio)
			throws Exception {

		boolean lEsitoOK = true;

		if (JMSProperties.getInstance().getProperty("LISTENER_NUOVA_GESTIONE") != null
				&& JMSProperties.getInstance().getProperty("LISTENER_NUOVA_GESTIONE").trim()
						.equalsIgnoreCase("true")) {
			SIAPReceiver.getInstance().testInArrivo();
			SIAPReceiver.getInstance().testInPartenza();
			SIAPReceiver.getInstance().testStampa();
		} else {
			SIAPReceiver.getInstance();
		}

		// =========================
		// Recupero il messaggio
		// =========================
		IMessaggio lCrtl = JMSLookupRemote.getMessaggioRemote();
		MessaggioModel lMessaggioRich = lCrtl.ExRicercaMessaggioByKey(aIdMessaggio);

		// ===========================
		// Creo il messaggio di risposta
		// ===========================
		// IRicercaJMS lCtrlMes = SIEPLookupRemote.getRicercaJMS();
		// MessaggioModel lMessageRisp = lCtrlMes.ExRicercaFascicoloSiepPerTrasferimento(aFascicoloModel);
		MessaggioModel lMessageRisp = new MessaggioModel();

		// ==========================================================================
		// BDI e Ufficio mittente
		// n.b. Recupero i dati dell'ufficio di Corte di Appello (BDI) per ottenere
		// la descrizione della BDI da inserire nelle setStringProperty del JmsMessaggio
		// ==========================================================================
		UfficioModel lBDIMittente = getUfficioByCodUfficio(getUfficioUtenteConnesso().getCodDistretto());
		lMessageRisp.setCodBdiMittente(lBDIMittente.getCodUfficio()); // COD BDI
		lMessageRisp.setDescrBdiMittente(lBDIMittente.getDescrComune()); // COMUNE BDI es Distretto di POTENZA
		lMessageRisp.setCodUfficioMittente(getCodUfficioUtenteConnesso());
		lMessageRisp.setCodiceUtenteMittente(this.getCodUtenteConnesso());

		// ==========================================================================
		// BDI e Ufficio destinatario
		// n.b. Recupero i dati dell'ufficio di Corte di Appello (BDI) per ottenere
		// la descrizione della BDI da inserire nelle setStringProperty del JmsMessaggio
		// ==========================================================================
		UfficioModel lUfficioReply = null;
		if (lMessaggioRich.getCodUfficioReplyTo() != null) {
			// Il messaggio mi è stato inoltrato. Rispondo all'ufficio indicato
			// nell'apposito campo (REPLY_TO). In caso di messaggio ricevuto come inoltro, l'id_messaggio
			// della richiesta originale è presente nel campo JMS_CORRELATION_REPLY_TO
			// mentre il campo JMS_CORRELATION_ID_MESSAGE contiene l'ID_MESSAGGIO
			// del messaggio di inoltro
			lUfficioReply = getUfficioByCodUfficio(lMessaggioRich.getCodUfficioReplyTo());

			// Indico nella risposta l'ID_MESSAGGIO della RICHIESTA presente sulla
			// BDI di Origine in modo che sia possibile collegare richiesta e risposta
			lMessageRisp.setJmsCorrelationIdMessage(lMessaggioRich.getJmsCorrelationReplyTo());
		} else {
			// Messaggio Diretto
			lUfficioReply = getUfficioByCodUfficio(lMessaggioRich.getCodUfficioMittente());

			// Indico nella risposta l'ID_MESSAGGIO della RICHIESTA presente sulla
			// BDI di Origine in modo che sia possibile collegare richiesta e risposta
			lMessageRisp.setJmsCorrelationIdMessage(lMessaggioRich.getJmsCorrelationIdMessage());
		}

		UfficioModel lUfficioPGCAPDest = getUfficioByCodUfficio(lUfficioReply.getCodDistretto());
		lMessageRisp.setDescrBdiDestinataria(lUfficioPGCAPDest.getDescrComune());
		lMessageRisp.setCodBdiDestinataria(lUfficioReply.getCodDistretto());
		lMessageRisp.setCodUfficioDestinatario(lUfficioReply.getCodUfficio());

		// Tipo Messaggio
		lMessageRisp.setCodTipoMessaggio(ICostantiJMS.ESITO);
		lMessageRisp.setCodTipoOperazione(ICostantiJMS.ESITO_TRASFERIMENTO_COMPETENZA_MS);
		lMessageRisp.setCodEsito(ICostantiJMS.ISCRITTO_CLASSE_IV);

		// n.b. le date non vengono prese in considerazione nella spedizione del
		// messaggio jms ma solo per l'aggiornamento locale. NON sono propertyJms
		// Sulla BDI di destinazione, in fase di ricezione, verrà utilizzata
		// come DATA_INVIO/DATA_ESITO quella dello scarico.
		lMessageRisp.setDataInvio(lMessaggioRich.getDataInvio());
		lMessageRisp.setDataEsito(DateUtils.getSysDate());

		lMessageRisp.setTreeModel(new TreeModel());
		// già valorizzato dal metodo ExRicercaFascicoloSiepPerTrasferimento con il blob del fascicolo di
		// classe IV

		// Nella risposta indico in chiaro i dati del procedimento di classe IV
		// appena iscritto
		// TODO 'MS' verificare se è meglio riportare i dati del procedimento trasmesso
		// atrimenti nel MESSAGGIO manca
		lMessageRisp.setChiaveAnnoSiep(aFascicoloModel.getChiaveAnno());
		lMessageRisp.setChiaveProgrSiep(aFascicoloModel.getChiaveProgr());
		lMessageRisp.setChiaveUfficioSiep(aFascicoloModel.getChiaveUfficio());
		// lMessageRisp.setChiaveAnnoSiep (lMessaggioRich.getChiaveAnnoSiep());
		// lMessageRisp.setChiaveProgrSiep (lMessaggioRich.getChiaveProgrSiep());
		// lMessageRisp.setChiaveUfficioSiep (lMessaggioRich.getChiaveUfficioSiep());

		// Dati del Soggetto
		lMessageRisp.setNomeSoggetto(lMessaggioRich.getNomeSoggetto());
		lMessageRisp.setCognomeSoggetto(lMessaggioRich.getCognomeSoggetto());
		lMessageRisp.setDataNascita(lMessaggioRich.getDataNascita());
		lMessageRisp.setCodComuneNascita(lMessaggioRich.getCodComuneNascita());
		lMessageRisp.setCodStatoNascita(lMessaggioRich.getCodStatoNascita());

		// =========================================
		// Invio il messaggio di risposta
		// =========================================
		try {
			SIAPSender lSender = new SIAPSender();
			lSender.send(lMessageRisp);
		} catch (Exception e) {
			lEsitoOK = false;
			// Non rilancio l'eccezione. Non c'è modo di gestirla. L'inserimento del
			// fascicolo è già avvenuta. Il mancato invio della risposta non può essere
			// bloccante, è un di più.
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("Errore in fase di invio dell'avvenuta iscrizione del fascicolo di classe IV: "
					+ aFascicoloModel.getChiaveAnno() + "/" + aFascicoloModel.getChiaveProgr() + " uff."
					+ aFascicoloModel.getChiaveUfficio());
		}

		// =============================================
		// Marco il messaggio di !!Richiesta!! come Iscritto.
		// =============================================
		lMessaggioRich.setFlagVisto("S");
		lMessaggioRich.setCodEsito(ICostantiJMS.ISCRITTO_CLASSE_IV);
		lMessaggioRich.setDataEsito(DateUtils.getSysDate());

		// Aggiorno il messaggio di richiesta
		lCrtl.ExModificaMessaggio(lMessaggioRich);

		return lEsitoOK;
	}

}