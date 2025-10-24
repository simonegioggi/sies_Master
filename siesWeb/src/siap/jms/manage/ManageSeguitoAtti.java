package siap.jms.manage;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Iterator;
import java.util.Vector;

import javax.jms.ObjectMessage;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.util.F3BProperties;
import f3b.util.xml.TreeModel;
import siap.jms.ICostantiJMS;
import siap.jms.JMSLookupRemote;
import siap.jms.SIAPSender;
import siap.jms.jmscode.controller.JmsCodeController;
import siap.jms.jmscode.model.JmsCodeModel;
import siap.jms.messaggio.controller.IMessaggio;
import siap.jms.messaggio.model.MessaggioModel;
import siap.sico.evento.model.EventoModel;
import siap.sico.misuraalternativa.model.MisuraAlternativaAggregatoModel;
import siap.sico.ufficio.controller.IUfficio;
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.utente.model.DatiOperazioneModel;
import siap.sico.util.SICOLookupRemote;
import siap.siep.fascicolo.controller.IFascicoloSiep;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.jms.controller.IPresaInCaricoJMS;
import siap.siep.modulocumulo.controller.IStatoEsecTitoloCumulato;
import siap.siep.modulocumulo.controller.ITitoloCumulato;
import siap.siep.modulocumulo.model.StatoEsecTitoloCumulatoModel;
import siap.siep.modulocumulo.model.TitoloCumulatoModel;
import siap.siep.util.SIEPLookupRemote;

@SuppressWarnings("rawtypes")
public class ManageSeguitoAtti implements ICostantiJMS {

	// 28/05/2019 [EC] - Modifico la destinazione su log PER MEV PROBLEMA CODE INTRODOTTO IN SIES 11.3
	private static Logger siesLogger = Logger.getLogger(LogF3B.JMS_LOG);

	public ManageSeguitoAtti() {
	}

	/**
	 * Metodo preposto all'elaborazione automatica del messaggio di Seguito Atti. Tale elaborazione prevede
	 * l'aggiornamento dei dati del fascicolo ricevuto senza l'intervento dell'operatore.
	 *
	 *
	 * @param aJmsMessage
	 *            - Messaggio JMS
	 * @param aMessIns
	 *            - eventuale messaggio salvato in fase di scarico
	 * @throws Exception
	 * @deprecated 07/2018 non più utilizzato
	 */
	public void elaboraMessaggioSeguitoAtti(ObjectMessage aJmsMessage, MessaggioModel aMessIns)
			throws Exception {

		siesLogger.info("[JMS]: inizio elaborazione SEGUITO ATTI");

		if (!(aJmsMessage.getStringProperty(TIPO_OPERAZIONE).equals(SEGUITO_ATTI_TRASFERIMENTO_COMPETENZA))) {
			throw new F3BException(
					"TIPO OPERAZIONE diverso da SEGUITO_ATTI_TRASFERIMENTO_COMPETENZA! Impossibile elaborare il Messaggio");
		}

		MessaggioModel lMessIns = new MessaggioModel(aJmsMessage);

		BigDecimal lChiaveAnnoSiep = lMessIns.getChiaveAnnoSiep();
		BigDecimal lChiaveProgrSiep = lMessIns.getChiaveProgrSiep();
		String lChiaveUfficioSiep = lMessIns.getChiaveUfficioSiep();

		boolean lStessaBDI = false;

		IUfficio lUff = SICOLookupRemote.getUfficioRemote();
		UfficioModel lUfficioFascicoloRicevuto = lUff.getUfficioByKey(lChiaveUfficioSiep);

		// BDI corrente da parametro letto dal file f3b.properties
		// Bdi.DistrictCode=06304900603
		String lBDILocale = F3BProperties.getProperty("Bdi.DistrictCode");
		siesLogger.debug("lBDILocale = " + lBDILocale);

		if (lUfficioFascicoloRicevuto.getCodDistretto().equals(lBDILocale)) {
			siesLogger.debug("Fascicolo da Cumulare/Aggiornare della stessa BDI");
			lStessaBDI = true;
		} else {
			siesLogger.debug("Fascicolo da Cumulare/Aggiornare proveniente da fuori Distretto");
			lStessaBDI = false;
		}

		siesLogger.debug("lStessaBDI = " + lStessaBDI);

		// Procedo all'elaborazione, ovvero aggiornamento dati del fascicolo solo
		// se NON stessa BDI
		// MessaggioModel lMessReturn = null;
		if (!lStessaBDI) {
			siesLogger.debug("ALTRA BDI, aggiorno il fascicolo: " + lChiaveAnnoSiep + "/" + lChiaveProgrSiep
					+ " " + lUfficioFascicoloRicevuto.getCodTipoUfficio() + " "
					+ lUfficioFascicoloRicevuto.getDescrComune());

			// Il seguito atti dovrebbe aggiornare un fascicolo altra BDI già a sistema
			// ovvero già trasmesso e PRESO IN CARICO.
			// Se il fascicolo NON risulta preso in carico non andrebbe aggiornato

			IPresaInCaricoJMS lPres = SIEPLookupRemote.getPresaInCarico();
			/* lMessReturn = */lPres.ExInserisciFascicoloSiep(lMessIns);
		}

		siesLogger.debug("MESSAGGIO = " + lMessIns);

		// Recupero la Descrizione BDI MITTENTE, serve al SENDER
		JmsCodeController lCtrlJms = new JmsCodeController();
		JmsCodeModel lJmsModel = lCtrlJms.ExRicercaJmsCodeByKey("BDI", lMessIns.getCodBdiMittente());

		lMessIns.setDescrBdiMittente(lJmsModel.getDescrizione());

		siesLogger.debug("Invio Esito");
		// n.b. per aggiornare il FLAG_VISTO mi serve l'id del messaggio.
		// Se stasa BDI, il messaggio non è statao scodato e coincide con quello inviato
		// il cui id è JmsCorrelationIdMessage
		// Se altra BDI allora il messaggio è stato salvato
		if (aMessIns != null && aMessIns.getIdMessaggio() != null) {
			siesLogger.debug("aMessIns.getIdMessaggio() = " + aMessIns.getIdMessaggio());
			lMessIns.setIdMessaggio(aMessIns.getIdMessaggio());
		} else if (lMessIns.getJmsCorrelationIdMessage() != null
				&& !lMessIns.getJmsCorrelationIdMessage().equals("")) {
			siesLogger.debug(
					"lMessIns.getJmsCorrelationIdMessage() = " + lMessIns.getJmsCorrelationIdMessage());
			lMessIns.setIdMessaggio(new BigDecimal(lMessIns.getJmsCorrelationIdMessage()));
		}

		inviaEsito(lMessIns);

	}

	/**
	 *
	 * @param lMess
	 *            * @deprecated 07/2018 non più utilizzato
	 */
	private void inviaEsito(MessaggioModel lMess) throws Exception {

		siesLogger.debug("INVIO ESITO");
		// ==================================================
		// Preparo e invio il Messaggio di risposta
		// ==================================================
		MessaggioModel lMessage = new MessaggioModel();

		lMessage.setDescrBdiDestinataria(lMess.getDescrBdiMittente());
		lMessage.setCodBdiDestinataria(lMess.getCodBdiMittente());
		lMessage.setCodUfficioDestinatario(lMess.getCodUfficioMittente());

		lMessage.setCodBdiMittente(F3BProperties.getProperty("Bdi.DistrictCode"));
		lMessage.setCodUfficioMittente(lMess.getCodUfficioDestinatario());
		lMessage.setCodiceUtenteMittente("OPENJMS");

		lMessage.setCodTipoMessaggio(ESITO);
		// lMessage.setCodTipoOperazione (ESITO_TRASFERIMENTO_COMPETENZA);
		lMessage.setCodTipoOperazione(ESITO_SEGUITO_ATTI);
		lMessage.setCodEsito(PRESAINCARICO);

		lMessage.setChiaveAnnoSiep(lMess.getChiaveAnnoSiep());
		lMessage.setChiaveProgrSiep(lMess.getChiaveProgrSiep());
		lMessage.setChiaveUfficioSiep(lMess.getChiaveUfficioSiep());

		// dati soggetto
		lMessage.setNomeSoggetto(lMess.getNomeSoggetto());
		lMessage.setCognomeSoggetto(lMess.getCognomeSoggetto());
		lMessage.setDataNascita(lMess.getDataNascita());
		lMessage.setCodComuneNascita(lMess.getCodComuneNascita());
		lMessage.setCodStatoNascita(lMess.getCodStatoNascita());

		lMessage.setChiaveAnnoFasCumulante(lMess.getChiaveAnnoFasCumulante());
		lMessage.setChiaveProgrFasCumulante(lMess.getChiaveProgrFasCumulante());
		lMessage.setChiaveUfficioFasCumulante(lMess.getChiaveUfficioFasCumulante());

		lMessage.setJmsCorrelationIdMessage(lMess.getJmsCorrelationIdMessage());

		lMessage.setDataInvio(lMess.getDataInvio());
		lMessage.setDataEsito(DateUtils.getSysDate());

		// lMessage.setTreeModel (lMess.getTreeModel()); //FIXME A che serve? Il mittente ha già i dati a
		// sistema
		// Non viene utilizzato da nessuno. E' inutile occupare spazio. Si manda un tree vuoto e non null
		// perchè altrimenti la MessaggioModel.VerifyMessage() rilancia errore
		lMessage.setTreeModel(new TreeModel());

		siesLogger.debug("Invio MSG di risposta = " + lMessage);

		SIAPSender lSender = new SIAPSender();
		lSender.send(lMessage);

		// ==========================================
		// Marco il messaggio di richiesta evaso.
		// ==========================================
		siesLogger.debug("Aggiornamento Messaggio Seguito ATti come elaborato FLAG_VISTO = S");
		lMess.setFlagVisto("S");
		lMess.setDataEsito(DateUtils.getSysDate());
		lMess.setCodEsito("01001");

		IMessaggio lCrtlMsg = JMSLookupRemote.getMessaggioRemote();
		lCrtlMsg.ExModificaMessaggio(lMess);
	}

	/**
	 * Effettua l'aggiornamento dello stato esecuzione del TitoloCumulato con i "nuovi" provvedimenti presenti
	 * nel seguito atti. Per capire quali sono i "NUOVI" provvedimenti viene testata la DATA_PRESA_IN_CARICO
	 * presente su TITOLO_CUMULATO (data ultima prese in carico) con EVENTO.DATA_INSERIMENTO Alla fine
	 * della'aggiornamento dello STATO esecuzione viene aggiornata la TITOLO_CUMULATO.DATA_PRESA_IN_CARICO Se
	 * DATA_PRESA_IN_CARICO = null (cumulo steop 1) il test viene fatto su TITOLO_CUMULATO.DATA_INSERIMENTO
	 *
	 * @param aIdFascicoloCumulato
	 */
	public void aggiornaStatoEsecuzioneTitolo(BigDecimal aIdFascicoloCumulato, BigDecimal aIdIstruttoria,
			DatiOperazioneModel aDatiOper) throws F3BException {
		// Recupero i dati del Titolo
		IFascicoloSiep lCtrlFasc = SIEPLookupRemote.getFascicoloSiepRemote();

		FascicoloSiepModel lFascicoloCumulato = lCtrlFasc.ExRicercaFascicoloByKey(aIdFascicoloCumulato);

		if (lFascicoloCumulato != null) {
			siesLogger.debug("Ricerco il titolo in istruttoria");
			ITitoloCumulato lCtrlTitolo = SIEPLookupRemote.getTitoloCumulatoRemote();
			TitoloCumulatoModel lTitoloModel = lCtrlTitolo.ExRicercaTitoloCumulatoByIstrIdOrig(aIdIstruttoria,
					lFascicoloCumulato.getSenIdSentenza());

			if (lTitoloModel != null) {
				// Recupero gli eventi che l'utente vedrebbe nella popup dello stato
				// esecuzione
				// ======================================================================
				// Recupero lo stato esecuzione del fascicolo del seguito atti
				// ======================================================================
				siesLogger.debug("Recupero lo stato esecuzione del Fascicolo");
				Vector<MisuraAlternativaAggregatoModel> lListaEventiStatoEsec = null;
				lListaEventiStatoEsec = lCtrlTitolo
						.ExRicercaEventiPerStatoEsecuzioneByFascicoloSiepPaged(aIdFascicoloCumulato, 0);

				siesLogger.debug("Elementi trovati SEF: " + lListaEventiStatoEsec.size());

				// ==========================================================================
				// Recupero lo stato esecuzione Caricato in istruttoria
				// ==========================================================================
				siesLogger.debug("Recupero lo stato esecuzione Caricato in istruttoria");
				IStatoEsecTitoloCumulato lCtrlSET = SIEPLookupRemote.getStatoEsecTitoloCumulatoRemote();
				Vector<StatoEsecTitoloCumulatoModel> lListaEventiSET = lCtrlSET
						.ExRicercaStatoEsecTitoloCumulatoByIdTitolo(lTitoloModel.getIdTitoloCumulato());

				siesLogger.debug("Elementi trovati SET: " + lListaEventiSET.size());

				// ======================================================================
				// Carico la lista degli eventi da inserire in lIdEventiDaInserire
				// Verifico quali eventi hanno EVENTO.DATA_INSERIMENTO > TITOLO_CUMULATO.DATA_PRESA_IN_CARICO
				// o EVENTO.DATA_INSERIMENTO > TITOLO_CUMULATO.DATA_INSERIMENTO
				// ======================================================================
				Date lDataUltimaPresaInCaricoTitolo = null;
				if (lTitoloModel.getDataPresaInCarico() != null)
					lDataUltimaPresaInCaricoTitolo = lTitoloModel.getDataPresaInCarico();
				else
					lDataUltimaPresaInCaricoTitolo = lTitoloModel.getDataInserimento();

				siesLogger.debug("lDataUltimaPresaInCaricoTitolo: " + lDataUltimaPresaInCaricoTitolo);

				Vector<BigDecimal> lIdEventiDaInserire = new Vector<>();

				Iterator itxFascicolo = lListaEventiStatoEsec.iterator();
				while (itxFascicolo.hasNext()) {
					MisuraAlternativaAggregatoModel lAggre = (MisuraAlternativaAggregatoModel) itxFascicolo
							.next();
					EventoModel lEvento = lAggre.getEventoNotifica().getEvento();

					if (lEvento.getDataInserimento().after(lDataUltimaPresaInCaricoTitolo)) {
						siesLogger.debug("Data Inserimento evento (" + lEvento.getDataInserimento()
								+ ") > Data Ultima presa in carico");

						// Per sicurezza verifico che l'evento non sia già presente:
						// STATO_ESEC_TITOLO_CUMULATO.ID_EVENTO_ORIGINE
						if (!isCaricato(lListaEventiSET, lEvento.getIdEvento())) {
							siesLogger.debug("Aggiungo l'evento alla lista di quelli da inserire. IDEvento: "
									+ lEvento.getIdEvento());
							lIdEventiDaInserire.add(lEvento.getIdEvento());
						} else {
							siesLogger.warn("Evento (" + lEvento.getIdEvento()
									+ ") con data inserimento > data ultima presa in carico ma già in SET, lo salto.");
						}
					}
				}

				siesLogger.debug("Nuovi eventi trovati e da inserire: " + lIdEventiDaInserire.size());
				for (BigDecimal lIdEventoNew : lIdEventiDaInserire)
					siesLogger.debug("lIdEventoNew: " + lIdEventoNew);

				// ======================================================================
				// Effettuo l'aggiornamento
				// ======================================================================
				siesLogger.debug("Procedo all'inserimento...: ");
				lCtrlSET.ExAggiornaStatoEsecTitoloCumulatoByIdTitolo(lIdEventiDaInserire, null,
						lTitoloModel.getIdTitoloCumulato(), lTitoloModel.getIstrIdIstruttoriaCumulo(), null,
						aDatiOper, true);

				// ======================================================================

			} else {
				siesLogger.warn("Attenzione Titolo non trovatro per idIstr = " + aIdIstruttoria
						+ ", idSentenza = " + lFascicoloCumulato.getSenIdSentenza());
			}

		} else {
			siesLogger.warn("Fascicolo SIEP non trovato");
		}

	}

	/**
	 * Verifica se nelo stato esecuzione del titolo esiste già un evento estratto con stesso aIdEvento
	 *
	 * @param aStatoEsecuzioneCaricato
	 *            - Lista evento dello stato esecuzione Titolo
	 * @param aIdEvento
	 *            - idEvento del fascicolo
	 * @return
	 */
	private boolean isCaricato(Vector<StatoEsecTitoloCumulatoModel> aStatoEsecuzioneCaricato,
			BigDecimal aIdEvento) {
		boolean lGiaCaricato = false;

		Iterator itxCaricato = aStatoEsecuzioneCaricato.iterator();
		while (itxCaricato.hasNext()) {
			StatoEsecTitoloCumulatoModel lSETModel = (StatoEsecTitoloCumulatoModel) itxCaricato.next();

			if (lSETModel.getIdEventoOrigine() != null
					&& aIdEvento.compareTo(lSETModel.getIdEventoOrigine()) == 0) {
				lGiaCaricato = true;
				break;
			}
		}

		return lGiaCaricato;
	}

}