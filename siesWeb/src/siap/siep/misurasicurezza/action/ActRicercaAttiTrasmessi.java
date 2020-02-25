package siap.siep.misurasicurezza.action;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;
import java.util.Vector;

import org.apache.log4j.Logger;

import siap.jms.ICostantiJMS;
import siap.jms.JMSLookupRemote;
import siap.jms.jmscode.controller.JmsCodeController;
import siap.jms.jmscode.model.JmsCodeModel;
import siap.jms.messaggio.controller.IMessaggio;
import siap.jms.messaggio.model.MessaggioModel;
import siap.sico.SICOException;
import siap.sico.jms.action.ICostantiSicoJMS;
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.web.ActionSiap;
import siap.siep.fascicolo.action.ICostantiFascicoloSiep;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.misurasicurezza.controller.IMisuraSicurezza;
import siap.siep.util.SIEPLookupRemote;
import siap.sius.SIUSException;
import siap.sius.presaincarico.action.ICostantiPresaincarico;
import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;

@SuppressWarnings("rawtypes")
public class ActRicercaAttiTrasmessi extends ActionSiap implements ICostantiMisuraSicurezza {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public String processRequest() throws Exception {

		// Registro nello stack i parametri di ricerca
		this.setLinkRitorno();
		setRequestAttribute(IWebConstants.LINK_RITORNO, "10");
		String lRitorno = getQueryRequestUrl();
		setRequestAttribute("linkRitorno", lRitorno);

		String lPagina = "1";
		if (!isRequestParameterNullObj(IWebConstants.NUM_PAGE))
			lPagina = getRequestStringParameter(IWebConstants.NUM_PAGE);

		// ==========================================================================
		// Recupero i Criteri di ricerca impostati in maschera
		// ==========================================================================
		// =========================
		// Ufficio Destinatario
		// =========================
		String lTipoUffDest = null;
		if (!isRequestParameterNullObj(ICostantiPresaincarico.CAMPO_COD_TIPO_UFFICIO)
				&& !"-".equals(getRequestStringParameter(ICostantiPresaincarico.CAMPO_COD_TIPO_UFFICIO))) {
			lTipoUffDest = getRequestStringParameter(ICostantiPresaincarico.CAMPO_COD_TIPO_UFFICIO);
		}

		String lDescComuneUffDest = null;
		if (!isRequestParameterNullObj(ICostantiPresaincarico.CAMPO_DESCR_COMUNE_UFFICIO))
			lDescComuneUffDest = getRequestStringParameter(ICostantiPresaincarico.CAMPO_DESCR_COMUNE_UFFICIO);

		String lCodiceUffDest = null;
		if (lTipoUffDest != null && lDescComuneUffDest != null)
			lCodiceUffDest = getCodUfficioByCodTipoUfficioDescrComune(lTipoUffDest, lDescComuneUffDest);

		// ==========
		// Esito
		// ==========
		Vector<String> lListaEsiti = null;
		String lCodEsito = null;
		if (!isRequestParameterNullObj(ICostantiSicoJMS.CAMPO_TIPO_ESITO))
			lCodEsito = getRequestStringParameter(ICostantiSicoJMS.CAMPO_TIPO_ESITO);

		if ("TUTTI".equals(lCodEsito))
			lCodEsito = null;

		if (lCodEsito != null) {
			lListaEsiti = new Vector<String>();
			lListaEsiti.add(lCodEsito);

			// anche i messaggi in stato 'Trasmesso x competenza' vanno considerati
			// 'in attesa di risposta'
			if ("-".equals(lCodEsito))
				lListaEsiti.add(ICostantiJMS.TRASFERITO);
		}

		// =========================
		// Data Trasmissione
		// =========================
		Date lDataTrasmissioneDal = null;
		lDataTrasmissioneDal = (getRequestDateParameter(ICostantiSicoJMS.CAMPO_ANNO_DATA_TRASMISSIONE_INIZIO,
				ICostantiSicoJMS.CAMPO_MESE_DATA_TRASMISSIONE_INIZIO,
				ICostantiSicoJMS.CAMPO_GIORNO_DATA_TRASMISSIONE_INIZIO));
		Date lDataTrasmissioneAl = null;
		lDataTrasmissioneAl = (getRequestDateParameter(ICostantiSicoJMS.CAMPO_ANNO_DATA_TRASMISSIONE_FINE,
				ICostantiSicoJMS.CAMPO_MESE_DATA_TRASMISSIONE_FINE,
				ICostantiSicoJMS.CAMPO_GIORNO_DATA_TRASMISSIONE_FINE));

		// Fascicolo corrente
		BigDecimal lChiaveAnnoSiep = null;
		BigDecimal lChiaveProgrSiep = null;
		String lChiaveUfficioSiep = null;
		if (isRequestChecked("checkFascicoloCorrente")) {
			if (isSessionAttributeNullObj("fascicolo")) {
				throw new SIUSException(SICOException.USER_MESSAGE, "Nessun fascicolo in sessione");
			} else {
				FascicoloSiepModel lFascicoloModel = (FascicoloSiepModel) getSessionAttribute("fascicolo");
				lChiaveAnnoSiep = lFascicoloModel.getChiaveAnno();
				lChiaveProgrSiep = lFascicoloModel.getChiaveProgr();
				lChiaveUfficioSiep = lFascicoloModel.getChiaveUfficio();
			}
		} else {
			if (!isRequestParameterNullObj(ICostantiFascicoloSiep.CAMPO_CHIAVE_ANNO)
					&& getRequestStringParameter(ICostantiFascicoloSiep.CAMPO_CHIAVE_ANNO).length() > 0)
				lChiaveAnnoSiep = getRequestBigDecimalParameter(ICostantiFascicoloSiep.CAMPO_CHIAVE_ANNO);
			if (!isRequestParameterNullObj(ICostantiFascicoloSiep.CAMPO_CHIAVE_PROGR)
					&& getRequestStringParameter(ICostantiFascicoloSiep.CAMPO_CHIAVE_PROGR).length() > 0)
				lChiaveProgrSiep = getRequestBigDecimalParameter(ICostantiFascicoloSiep.CAMPO_CHIAVE_PROGR);
		}

		// ==========================================================================
		// Lancio la ricerca dei messaggi inviati
		// vanno recuperati tutti i messaggio di richiesta 01 inviati dall'ufficio
		// corrente
		// Se è impostato il filtro sull'esito va verificato se presente il messaggio
		// di risposta ma soprattutto l'ultimo messaggio di risposta che determina
		// l'esito corrente.
		// ==========================================================================
		Vector<String> lListaTipoOperazione = new Vector<String>();
		lListaTipoOperazione.add(ICostantiJMS.TRASFERIMENTO_COMPETENZA_MS);
		lListaTipoOperazione.add(ICostantiJMS.TRASFERIMENTO_ESECUZIONE_MS);

		IMisuraSicurezza lCtrl = SIEPLookupRemote.getMisuraSicurezzaRemote();
		Vector<MessaggioModel> lVect = lCtrl.ExRicercaMessaggi(ICostantiJMS.DELIVERY_MODE_INVIATO,
				ICostantiJMS.RICHIESTA, lListaTipoOperazione
				// , lCodEsito // lCodEsito
				, lListaEsiti, null // Flag_visto.
				, lChiaveAnnoSiep // aChiaveAnnoSiep
				, lChiaveProgrSiep // aChiaveProgrSiep
				, lChiaveUfficioSiep // aChiaveUfficioSiep
				, getCodUfficioUtenteConnesso() // aCodUfficioMitt
				, lCodiceUffDest // ufficio dest
				, lDataTrasmissioneDal, lDataTrasmissioneAl, Integer.parseInt(lPagina));

		setRequestAttribute("Messaggi", lVect);

		// Recupero il numero totale di record
		BigDecimal lCountRisultati;
		if (isRequestParameterNullObj("CountRisultati")) {
			lCountRisultati = lCtrl.ExCountRicercaMessaggi(ICostantiJMS.DELIVERY_MODE_INVIATO,
					ICostantiJMS.RICHIESTA, lListaTipoOperazione
					// , lCodEsito // lCodEsito
					, lListaEsiti, null // Flag_visto. Non recupero quelli ancora da elaborare
					, lChiaveAnnoSiep // lChiaveAnno
					, lChiaveProgrSiep // lChiaveProgr
					, lChiaveUfficioSiep // aChiaveUfficioSiep: la ricerca viene fatta per Ufficio titolare
											// degli atti trasmessi
					, getCodUfficioUtenteConnesso() // aCodUfficioMitt
					, lCodiceUffDest // ufficio dest
					, lDataTrasmissioneDal, lDataTrasmissioneAl);
		} else {
			lCountRisultati = getRequestBigDecimalParameter("CountRisultati");
		}

		// ==========================================================================
		// Per ogni messaggio inviato (di quelli da visualizzare nella maschera corrente)
		// recupero tutte le risposte ricevute. Ce ne
		// può essere più di una in caso di inoltro ma anche in caso di prese in carico
		// e successiva iscrizione
		// ==========================================================================
		if (lVect != null) {
			for (int i = 0; i < lVect.size(); i++) {
				MessaggioModel lRichiestaMsg = lVect.elementAt(i);

				IMessaggio lCrtlMess = JMSLookupRemote.getMessaggioRemote();

				Vector<MessaggioModel> lVectCorrelati = lCrtlMess.ExRicercaMessaggiCorrelati(lRichiestaMsg
						.getIdMessaggio().toString());
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("lVectCorrelati.size() prima = " + lVectCorrelati.size());
				// ========================================
				//
				// ========================================
				if (lVectCorrelati != null && lVectCorrelati.size() > 0
						&& ICostantiJMS.TRASFERITO.equals(lRichiestaMsg.getCodEsito())) { // Se la richiesta è
																							// ancora in stato
																							// (TRASFERITO)
																							// vuol dire
																							// che il
																							// messaggio è
																							// stato inoltrato
																							// e l'ufficio a
																							// cui è stato
																							// inoltrato non
																							// ha ancora
																							// risposto.
																							// Aggiungo un
																							// messaggio di
																							// esito
																							// fittizio utile
																							// solo ai fini
																							// della
																							// visualizzazione
																							// nella lista
																							// per avere
																							// l'ultimo record
																							// in stato (in
																							// attesa di
																							// risposta)
					MessaggioModel lUltimoMessaggio = (MessaggioModel) lVectCorrelati.lastElement();

					// Faccio una copia dell'ultimo messaggio
					MessaggioModel lMessFittizio = new MessaggioModel(lUltimoMessaggio);
					lMessFittizio.setCodEsito("-"); // In attesa di risposta
					lMessFittizio.setDataInvio(null);
					lMessFittizio.setCodUfficioMittente(lMessFittizio.getCodUfficioInoltro());
					lMessFittizio.setDescrUfficioMittente(lMessFittizio.getDescrUfficioInoltro());
					lMessFittizio.setDescrSedeUfficioMittente(lMessFittizio.getDescrSedeUfficioInoltro());

					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger.debug("Aggiungo risposta fittizia...");

					lVectCorrelati.add(lMessFittizio);
				}
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("lVectCorrelati.size() = " + lVectCorrelati.size());
				lRichiestaMsg.setMessaggiCorrelati(lVectCorrelati);

				// Verifico se presenti solleciti sul messaggio di richiesta
				IMisuraSicurezza lCtrlMisSic = SIEPLookupRemote.getMisuraSicurezzaRemote();
				{
					Vector<MessaggioModel> lVectSolleciti = lCtrlMisSic.ExRicercaSollecitiByIdRich(
							ICostantiJMS.DELIVERY_MODE_INVIATO, ICostantiJMS.RICHIESTA,
							ICostantiJMS.SOLLECITO_TRASFERIMENTO_COMPETENZA_MS,
							lRichiestaMsg.getIdMessaggio(),
							null // "N" // Flag_visto.
							/* uff destinatario del sollecito = dest del mess */,
							lRichiestaMsg.getCodUfficioDestinatario());

					if (lVectSolleciti != null && lVectSolleciti.size() > 0) {
						// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto
						// di LogF3B.getLogger()
						siesLogger.debug("Trovati solleciti a " + lRichiestaMsg.getDescrUfficioDestinatario()
								+ " di " + lRichiestaMsg.getDescrSedeUfficioDestinatario() + " per id Mess "
								+ lRichiestaMsg.getIdMessaggio());
						lRichiestaMsg.setMessaggiSollecito(lVectSolleciti);
					}
				}

				// TODO MS Aggiungo i solleciti
				for (int j = 0; j < lVectCorrelati.size(); j++) {
					MessaggioModel lMessMod = lVectCorrelati.elementAt(j);

					// verifica se presenti Solleciti all'ufficio che ha in carco gli atti
					Vector<MessaggioModel> lVectSolleciti = lCtrlMisSic.ExRicercaSollecitiByIdRich(
							ICostantiJMS.DELIVERY_MODE_INVIATO, ICostantiJMS.RICHIESTA,
							ICostantiJMS.SOLLECITO_TRASFERIMENTO_COMPETENZA_MS,
							lRichiestaMsg.getIdMessaggio(), null // "N" // Flag_visto.
							/* uff destinatario del sollecito */, lMessMod.getCodUfficioMittente());

					if (lVectSolleciti != null && lVectSolleciti.size() > 0) {
						// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto
						// di LogF3B.getLogger()
						siesLogger.debug("Trovati solleciti a " + lMessMod.getDescrUfficioMittente() + " di "
								+ lMessMod.getDescrSedeUfficioMittente() + " per id Mess "
								+ lRichiestaMsg.getIdMessaggio());
						lMessMod.setMessaggiSollecito(lVectSolleciti);
					}
				}
			}
		}

		// =====================================================
		// Ripasso alla maschera i criteri di ricerca
		if (lCodiceUffDest != null) {
			UfficioModel lUfficioDestinatario = getUfficioByCodUfficio(lCodiceUffDest);
			setRequestAttribute("aUfficioDestinatario", lUfficioDestinatario);
		}

		String lDescEsito = null;
		if (lCodEsito != null) {
			JmsCodeController lCtrlJms = new JmsCodeController();
			JmsCodeModel lJmsModel = lCtrlJms.ExRicercaJmsCodeByKey("CODICE_ESITO", lCodEsito);
			lDescEsito = lJmsModel.getDescrizione();

			if ("-".equals(lDescEsito))
				lDescEsito = "In attesa di risposta";
		}

		setRequestAttribute("aEsito", lDescEsito);

		if (lDataTrasmissioneDal != null)
			setRequestAttribute("aDataTrasmissioneDal",
					DateUtils.getDateToString(lDataTrasmissioneDal, "dd/MM/yyyy"));
		if (lDataTrasmissioneAl != null)
			setRequestAttribute("aDataTrasmissioneAl",
					DateUtils.getDateToString(lDataTrasmissioneAl, "dd/MM/yyyy"));

		setRequestAttribute("ChiaveAnno", (lChiaveAnnoSiep == null) ? null : "" + lChiaveAnnoSiep);
		setRequestAttribute("ChiaveProgr", (lChiaveProgrSiep == null) ? null : "" + lChiaveProgrSiep);

		//
		// ==========================================

		// Passo i dati per gestire la paginazione
		setRequestAttribute("CountRisultati", lCountRisultati);
		setRequestAttribute(IWebConstants.NUM_PAGE, lPagina);
		setRequestAttribute(IWebConstants.REQUEST_FOR_PAGING, getCompleteRequestURL());

		return PG_LISTA_ATTI_TRASMESSI;
	}

	/**
	 * Recupera solo i parametri di ricerca da passare alla JSP per la costruzione del link "torna indietro"
	 * 
	 * @return
	 * @throws F3BException
	 */
	private String getQueryRequestUrl() throws F3BException {

		String lRequest = ""; // this.getRequest().getRequestURL() + "?";
		Set lKeys = getRequest().getParameterMap().keySet();

		Iterator itx = lKeys.iterator();
		while (itx.hasNext()) {
			String key = (String) itx.next();
			// Copia di tutti gli attributi tranne LINK_RITORNO e FLAG_RITORNO
			if (!key.equals(IWebConstants.LINK_RITORNO) && !key.equals(IWebConstants.FLAG_RITORNO)
					&& !key.equals(IWebConstants.ACTION_FIELD)) {
				lRequest += key + "=" + getRequestStringParameter(key) + "&";
			}
		}
		return lRequest.substring(0, lRequest.length() - 1);
	}

}