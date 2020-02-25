package siap.siep.modulocumulo.action;

import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import siap.jms.ICostantiJMS;
import siap.jms.JMSLookupRemote;
import siap.jms.messaggio.action.ICostantiMessaggio;
import siap.jms.messaggio.controller.IMessaggio;
import siap.jms.messaggio.model.MessaggioModel;
import siap.siep.fascicolo.controller.IFascicoloSiep;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.istruttoriacumulo.model.IstruttoriaCumuloModel;
import siap.siep.modulocumulo.controller.IModuloCumulo;
import siap.siep.util.SIEPLookupRemote;

/**
 * Action per la ricerca delle Richieste Atti per Cumulo Trasmesse qualsiasi sia lo stato della trasmissione
 *
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class ActRicercaRichiesteAttiTrasmessiCumulo extends ActionModuloCumulo
		implements ICostantiModuloCumulo, ICostantiMessaggio {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public String processRequest() throws F3BException, Exception {

		// Controllo che il MSGQUEUE sia in ESECUZIONE: Commentato il 23-06-2016
		/*
		 * try { if ( JMSProperties.getInstance().getProperty("LISTENER_NUOVA_GESTIONE")!=null &&
		 * JMSProperties.getInstance().getProperty("LISTENER_NUOVA_GESTIONE").trim().equalsIgnoreCase("true")
		 * ) { SIAPReceiver.getInstance().testInArrivo(); SIAPReceiver.getInstance().testInPartenza();
		 * SIAPReceiver.getInstance().testStampa(); } else{ SIAPReceiver.getInstance(); } } catch (Exception
		 * e){ // do nothing. La mancanza di connessione con il provider non è boccante // in questa fase.
		 * L'utente potrà visualizzare solo i messaggi già scaricati // ma non eventuali messaggi fermi in
		 * coda } this.setLinkRitorno(); setRequestAttribute(IWebConstants.LINK_RITORNO, "10");
		 */
		//
		// -- 23-06-2016: Inserimento Jsp di Attesa.....
		IstruttoriaCumuloModel IstruModel = super.getDatiIstruttoria();

		String lpage = ICostantiModuloCumulo.PG_RICERCA_RICHIESTE_ATTI_TRASMESSE;

		if (isRequestParameterNullObj("vai")) {
			// Pagina di attesa (rotellina)
			lpage = PG_ATTESA_CUMULO;
			setRequestAttribute("titolo", " RISCONTRO RICHIESTE/SOLLECITI TRASMESSI ");
			setRequestAttribute("next_action", getClass().getName());
			siesLogger.debug("Attesa_Cumulo :" + getClass().getName());
		} else {
			FascicoloSiepModel lFasMod = null;
			IFascicoloSiep lCtrlF = SIEPLookupRemote.getFascicoloSiepRemote();
			lFasMod = lCtrlF.ExRicercaFascicoloByKey(IstruModel.getFasSieIdFascicoloSiep());

			Vector<String> lListaTipoOperazione = new Vector<>();

			lListaTipoOperazione.add(ICostantiJMS.RICHIESTA_TRASMISSIONE_ATTI_PER_COMP);
			lListaTipoOperazione.add(ICostantiJMS.COMUNICAZIONE_CUMULO_PROCURE_COMPETENTI);

			IModuloCumulo lCtrl = SIEPLookupRemote.getModuloCumuloRemote();

			Vector<MessaggioModel> lVect = lCtrl.ExRicercaMessaggi(ICostantiJMS.DELIVERY_MODE_INVIATO,
					ICostantiJMS.RICHIESTA, lListaTipoOperazione, null // lCodEsito
					, null // Flag_visto.
					, null // aChiaveAnnoSiep
					, null // aChiaveProgrSiep
					, null // aChiaveUfficioSiep
					, getCodUfficioUtenteConnesso() // aCodUfficioMitt
					, null // ufficio dest
					, null // lDataTrasmissioneDal
					, null // lDataTrasmissioneAl,
					, null // Cognome
					, null // Nome
					, lFasMod.getChiaveAnno() // ChiaveAnnoFasCumulante
					, lFasMod.getChiaveProgr() // ChiaveProgrFasCumulante
					, lFasMod.getChiaveUfficio() // ChiaveUfficioFasCumulante
					, 0);
			// ======================================================================================
			// Per ogni messaggio verifico se è arrivata la 'risposta', cioè è stato mandato
			// il Trasferimento di Competenza richiesto con il messaggio di Richiesta
			// =======================================================================================
			String lRisposta = "";
			IMessaggio lCtrlM = JMSLookupRemote.getMessaggioRemote();
			if (lVect != null && lVect.size() > 0) {
				for (int i = 0; i < lVect.size(); i++) {
					lRisposta = "";
					MessaggioModel lMessaggioRichiesta = lVect.elementAt(i);
					if (lMessaggioRichiesta != null && lMessaggioRichiesta.getIdMessaggio() != null) {
						// Provo a Cercare un messaggio di risposta di tipo 00066 : Richiesta Accettata
						MessaggioModel lMessaggioRisposta0066 = lCtrlM.ExRicercaMessaggioByIdRichiesta(
								ICostantiJMS.RICHIESTA, ICostantiJMS.TRASFERIMENTO_COMPETENZA,
								lMessaggioRichiesta.getCodUfficioDestinatario(),
								lMessaggioRichiesta.getIdMessaggio());

						if (lMessaggioRisposta0066 != null
								&& lMessaggioRisposta0066.getIdMessaggio() != null) {
							lRisposta += "Risposta inviata il ";

							if (lMessaggioRisposta0066.getCodEsito() != null
									&& lMessaggioRisposta0066.getCodEsito().compareTo("01007") == 0) {
								lRisposta += DateUtils.getDateToString(lMessaggioRisposta0066.getDataEsito(),
										"dd-MM-yyyy HH:mm");
								lMessaggioRichiesta.setRapportoEsito(lRisposta);
								lMessaggioRichiesta.setCodEsito("01007");
								lMessaggioRichiesta.setDescrEsito("ATTI RICEVUTI E RESTITUITI");
								lMessaggioRichiesta.setFlagVisto("S");
							} else if (lMessaggioRisposta0066.getCodEsito() != null
									&& lMessaggioRisposta0066.getCodEsito().compareTo("01003") == 0) {
								lRisposta += DateUtils.getDateToString(lMessaggioRisposta0066.getDataEsito(),
										"dd-MM-yyyy HH:mm");
								lMessaggioRichiesta.setRapportoEsito(lRisposta);
								lMessaggioRichiesta.setCodEsito("01003");
								lMessaggioRichiesta.setDescrEsito("ATTI RESTITUITI dopo PRESA in CARICO");
								lMessaggioRichiesta.setFlagVisto("S");
							} else if (lMessaggioRisposta0066.getCodEsito() != null
									&& lMessaggioRisposta0066.getCodEsito().compareTo("01001") == 0) {
								lRisposta += DateUtils.getDateToString(lMessaggioRisposta0066.getDataEsito(),
										"dd-MM-yyyy HH:mm");
								lMessaggioRichiesta.setRapportoEsito(lRisposta);
								lMessaggioRichiesta.setCodEsito("01001");
								lMessaggioRichiesta.setDescrEsito("ATTI RICEVUTI E PRESI in CARICO");
								lMessaggioRichiesta.setFlagVisto("S");
							} else {
								lRisposta += DateUtils.getDateToString(lMessaggioRisposta0066.getDataInvio(),
										"dd-MM-yyyy HH:mm");
								lMessaggioRichiesta.setRapportoEsito(lRisposta);
								lMessaggioRichiesta.setCodEsito("01006");
								lMessaggioRichiesta.setDescrEsito("ATTI RICEVUTI");
								lMessaggioRichiesta.setFlagVisto("S");
							}

							// Cerco Eventuali Solleciti prima della Richiesta Accettata
							Vector<MessaggioModel> lVectSolleciti0066 = new Vector();
							lVectSolleciti0066 = lCtrlM.ExRicercaSollecitiMessaggioRichiestaAtti(
									ICostantiJMS.RICHIESTA,
									ICostantiJMS.SOLLECITO_RICHIESTA_TRASMISSIONE_ATTI_PER_COMP,
									lMessaggioRichiesta.getIdMessaggio().toString());
							if (lVectSolleciti0066 != null && lVectSolleciti0066.size() > 0) {
								siesLogger.debug("Trovati solleciti prima di Risposta a "
										+ lMessaggioRichiesta.getDescrUfficioDestinatario() + " di "
										+ lMessaggioRichiesta.getDescrSedeUfficioDestinatario()
										+ " per id Mess " + lMessaggioRichiesta.getIdMessaggio());

								lMessaggioRichiesta.setMessaggiSollecito(lVectSolleciti0066);
							}

						} else {
							// Provo a Cercare un messaggio di risposta di tipo 00077 : Richiesta Rigettata
							MessaggioModel lMessaggioRisposta0077 = lCtrlM.ExRicercaMessaggioByIdRichiesta(
									ICostantiJMS.ESITO,
									ICostantiJMS.RIGETTO_RICHIESTA_TRASMISSIONE_ATTI_PER_COMP,
									lMessaggioRichiesta.getCodUfficioDestinatario(),
									lMessaggioRichiesta.getIdMessaggio());

							if (lMessaggioRisposta0077 != null
									&& lMessaggioRisposta0077.getIdMessaggio() != null) {
								lRisposta += "Risposta inviata il ";
								lRisposta += DateUtils.getDateToString(lMessaggioRisposta0077.getDataInvio(),
										"dd-MM-yyyy HH:mm");

								lMessaggioRichiesta.setRapportoEsito(lRisposta);
								lMessaggioRichiesta.setCodEsito("01007");
								lMessaggioRichiesta.setDescrEsito("RICHIESTA RIGETTATA");
								lMessaggioRichiesta.setFlagVisto("S");
							} else {
								// Se NON c'è una Risposta cerco Eventuali Solleciti
								Vector<MessaggioModel> lVectSolleciti = lCtrlM
										.ExRicercaSollecitiMessaggioRichiestaAtti(ICostantiJMS.RICHIESTA,
												ICostantiJMS.SOLLECITO_RICHIESTA_TRASMISSIONE_ATTI_PER_COMP,
												lMessaggioRichiesta.getIdMessaggio().toString());
								if (lVectSolleciti != null && lVectSolleciti.size() > 0) {
									siesLogger.debug("Trovati solleciti a "
											+ lMessaggioRichiesta.getDescrUfficioDestinatario() + " di "
											+ lMessaggioRichiesta.getDescrSedeUfficioDestinatario()
											+ " per id Mess " + lMessaggioRichiesta.getIdMessaggio());
									lMessaggioRichiesta.setMessaggiSollecito(lVectSolleciti);
									lMessaggioRichiesta.setRapportoEsito("Inviato Sollecito");
								} else {
									lMessaggioRichiesta.setRapportoEsito("In Attesa di Risposta...");
								}

							} // Chiude Else lMessaggioRisposta0077

						} // Chiude Else lMessaggioRisposta0066

					} // Chiude if(lMessaggioRichiesta NOT null)

				} // CHIUDE ciclo for

			} // chiude if Vec > 0

			setRequestAttribute("Messaggi", lVect);
		}

		return lpage;
	}

}