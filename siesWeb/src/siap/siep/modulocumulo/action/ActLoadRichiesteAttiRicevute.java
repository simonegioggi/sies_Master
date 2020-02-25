package siap.siep.modulocumulo.action;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
import f3b.web.html.Option;
import siap.jms.ICostantiJMS;
import siap.jms.JMSLookupRemote;
import siap.jms.messaggio.controller.IMessaggio;
import siap.jms.messaggio.model.MessaggioModel;
import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.jms.action.ICostantiSicoJMS;
import siap.sico.soggetto.action.ICostantiSoggetto;
import siap.sico.ufficio.action.ICostantiUfficio;
import siap.sico.ufficio.controller.IUfficio;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siep.fascicolo.action.ICostantiFascicoloSiep;
import siap.siep.modulocumulo.controller.IModuloCumulo;
import siap.siep.util.SIEPLookupRemote;

/**
 * Action che effettua la ricerca sulla TABELLA MESSAGGIO delle richieste atti Ricevute (04-00075)
 *
 * @author d.fiorletta
 */
public class ActLoadRichiesteAttiRicevute extends ActionSiap implements ICostantiModuloCumulo {
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	@SuppressWarnings("rawtypes")
	public String processRequest() throws F3BException, Exception {
		// ==========================================================
		// Verifica se i listener sono attivi e in caso li lancia
		// ==========================================================
		// Commentata. Non va testata qui la connessione con il provider. Questa è
		// una funzione di ricerca sulla tabella messaggio. Non serve avere la connessione
		// ed inoltre rallenta parecchio la ricerca
		// try {
		// if ( JMSProperties.getInstance().getProperty("LISTENER_NUOVA_GESTIONE")!=null
		// &&
		// JMSProperties.getInstance().getProperty("LISTENER_NUOVA_GESTIONE").trim().equalsIgnoreCase("true")
		// )
		// {
		// SIAPReceiver.getInstance().testInArrivo();
		// SIAPReceiver.getInstance().testInPartenza();
		// SIAPReceiver.getInstance().testStampa();
		// }
		// else{
		// SIAPReceiver.getInstance();
		// }
		// }
		// catch (Exception e){
		// // do nothing. La mancanza di connessione con il provider non è boccante
		// // in questa fase. L'utente potrà visualizzare solo i messaggi già scaricati
		// // ma non eventuali messaggi fermi in coda
		// }

		// ???????? serve???????
		this.setLinkRitorno();
		setRequestAttribute(IWebConstants.LINK_RITORNO, "10");
		// String lRitorno = getQueryRequestUrl();
		// setRequestAttribute("linkRitorno", lRitorno);

		String lPagina = "1";
		if (!isRequestParameterNullObj(IWebConstants.NUM_PAGE))
			lPagina = getRequestStringParameter(IWebConstants.NUM_PAGE);

		// ==========================================================================
		// Recupero eventuali filtri di ricerca:
		// - data richiesta (da a) ultimi 2 mesi
		// - cod Ufficio Mittente
		// - Flag visto (?)
		BigDecimal lChiaveAnnoSiep = null;
		BigDecimal lChiaveProgrSiep = null;
		BigDecimal lIncrementoProgrAccorpato = new BigDecimal(0);

		Date lDataTrasmissioneDal = null; // DateUtils.getDate("10/09/2015", "dd/MM/yyyy");
		Date lDataTrasmissioneAl = null; // DateUtils.getSysDate();
		String lCognome = null;
		String lNome = null;

		String lFlag_Visto = null;

		// Ufficio Mittente
		String lCodTipoUfficio = null;
		String lDescrComuneUfficio = null;
		String lCodUfficioMitt = null;
		if (!isRequestParameterNullObj(ICostantiUfficio.CAMPO_TIPO_UFFICIO)
				&& !getRequestStringParameter(ICostantiUfficio.CAMPO_TIPO_UFFICIO).equals("")
				&& !getRequestStringParameter(ICostantiUfficio.CAMPO_TIPO_UFFICIO).equals("-")) {
			lCodTipoUfficio = getRequestStringParameter(ICostantiUfficio.CAMPO_TIPO_UFFICIO);
			lDescrComuneUfficio = getRequestStringParameter(ICostantiUfficio.CAMPO_SEDE_UFFICIO);

			lCodUfficioMitt = getCodUfficioByCodTipoUfficioDescrComune(lCodTipoUfficio, lDescrComuneUfficio);
		}

		// Periodo trasmissione
		// Data Dal
		if (!isRequestParameterNullObj(ICostantiSicoJMS.CAMPO_GIORNO_DATA_TRASMISSIONE_INIZIO)
				&& !getRequestStringParameter(ICostantiSicoJMS.CAMPO_GIORNO_DATA_TRASMISSIONE_INIZIO)
						.equals("")) {
			lDataTrasmissioneDal = getRequestDateParameter(
					ICostantiSicoJMS.CAMPO_ANNO_DATA_TRASMISSIONE_INIZIO,
					ICostantiSicoJMS.CAMPO_MESE_DATA_TRASMISSIONE_INIZIO,
					ICostantiSicoJMS.CAMPO_GIORNO_DATA_TRASMISSIONE_INIZIO);
		}
		// Data al
		if (!isRequestParameterNullObj(ICostantiSicoJMS.CAMPO_GIORNO_DATA_TRASMISSIONE_FINE)
				&& !getRequestStringParameter(ICostantiSicoJMS.CAMPO_GIORNO_DATA_TRASMISSIONE_FINE)
						.equals("")) {
			lDataTrasmissioneAl = getRequestDateParameter(ICostantiSicoJMS.CAMPO_ANNO_DATA_TRASMISSIONE_FINE,
					ICostantiSicoJMS.CAMPO_MESE_DATA_TRASMISSIONE_FINE,
					ICostantiSicoJMS.CAMPO_GIORNO_DATA_TRASMISSIONE_FINE);
		}

		// flag_visto = N - Ricerca SOLO i messaggi da Trasmettere
		// flag_visto = null - Ricerca tutti i messaggi
		if (!isRequestParameterNullObj(ICostantiSicoJMS.CAMPO_FLAG_VISTO)
				&& !getRequestStringParameter(ICostantiSicoJMS.CAMPO_FLAG_VISTO).equals("")) {
			lFlag_Visto = getRequestStringParameter(ICostantiSicoJMS.CAMPO_FLAG_VISTO);
		}
		setRequestAttribute("Flag_Visto", lFlag_Visto);

		// Estremi procedimento
		if (!isRequestParameterNullObj(ICostantiJMS.CHIAVE_ANNO_SIEP)
				&& !getRequestStringParameter(ICostantiJMS.CHIAVE_ANNO_SIEP).equals("")) {
			lChiaveAnnoSiep = getRequestBigDecimalParameter(ICostantiJMS.CHIAVE_ANNO_SIEP);
		}

		if (!isRequestParameterNullObj(ICostantiJMS.CHIAVE_PROGR_SIEP)
				&& !getRequestStringParameter(ICostantiJMS.CHIAVE_PROGR_SIEP).equals("")) {
			lChiaveProgrSiep = getRequestBigDecimalParameter(ICostantiJMS.CHIAVE_PROGR_SIEP);
		}

		if (!isRequestParameterNullObj(ICostantiFascicoloSiep.CAMPO_CHIAVE_ACCORPATO)
				&& !getRequestStringParameter(ICostantiFascicoloSiep.CAMPO_CHIAVE_ACCORPATO).equals("0")) {
			lIncrementoProgrAccorpato = getRequestBigDecimalParameter(
					ICostantiFascicoloSiep.CAMPO_CHIAVE_ACCORPATO);

			lChiaveProgrSiep = lChiaveProgrSiep.add(lIncrementoProgrAccorpato);
		}

		// Estremi del soggetto
		if (!isRequestParameterNullObj(ICostantiSoggetto.CAMPO_COGNOME)
				&& !getRequestStringParameter(ICostantiSoggetto.CAMPO_COGNOME).equals("")) {
			lCognome = getRequestStringParameter(ICostantiSoggetto.CAMPO_COGNOME);
		}

		if (!isRequestParameterNullObj(ICostantiSoggetto.CAMPO_NOME)
				&& !getRequestStringParameter(ICostantiSoggetto.CAMPO_NOME).equals("")) {
			lNome = getRequestStringParameter(ICostantiSoggetto.CAMPO_NOME);
		}

		siesLogger.debug("lDataTrasmissioneDal = " + lDataTrasmissioneDal);
		siesLogger.debug("lDataTrasmissioneAl = " + lDataTrasmissioneAl);
		siesLogger.debug("lCodUfficioMitt = " + lCodUfficioMitt);
		siesLogger.debug("Anno/Progressivo = " + lChiaveAnnoSiep + "/" + lChiaveProgrSiep);
		siesLogger.debug("lFlag_visto = " + lFlag_Visto);

		Option lOption = new Option(DecodificheManager.getInstance().getTipoUfficioPerCodice());
		lOption.setFilter(new String[] { "-", "PM", "PGCAP", "PMM" });
		if (lCodTipoUfficio != null)
			lOption.setSelected(lCodTipoUfficio);
		else
			lOption.setSelected("-");
		setRequestAttribute("tipoUfficioRichiedente", "" + lOption);

		// Uffici accorpati per Ufficio corrente
		IUfficio lUffCtrl = SICOLookupRemote.getUfficioRemote();
		Vector lUffAcc = lUffCtrl.ListaUfficiAccorpati("PM", getCodUfficioUtenteConnesso());
		setRequestAttribute("elencoUfficiAccorpati", lUffAcc);

		// ==========================================================================
		Vector<String> lListaTipoOperazione = new Vector<>();
		lListaTipoOperazione.add(ICostantiJMS.RICHIESTA_TRASMISSIONE_ATTI_PER_COMP);
		// lListaTipoOperazione.add (ICostantiJMS.SOLLECITO_RICHIESTA_TRASMISSIONE_ATTI_PER_COMP);

		IModuloCumulo lCtrl = SIEPLookupRemote.getModuloCumuloRemote();
		Vector<MessaggioModel> lVect = lCtrl.ExRicercaMessaggi(null // ICostantiJMS.DELIVERY_MODE_INVIATO
				, ICostantiJMS.RICHIESTA, lListaTipoOperazione, null // lCodEsito
				, lFlag_Visto // Flag_visto: Se valorizzato ad 'N' entra nei parametri di Ricerca
				, lChiaveAnnoSiep // aChiaveAnnoSiep
				, lChiaveProgrSiep // aChiaveProgrSiep
				, null // aChiaveUfficioSiep
				, lCodUfficioMitt // aCodUfficioMitt
				, getCodUfficioUtenteConnesso() // ufficio dest
				, lDataTrasmissioneDal // lDataTrasmissioneDal
				, lDataTrasmissioneAl // lDataTrasmissioneAl
				, lCognome, lNome, null // aChiaveAnnoFasCumulante
				, null // aChiaveProgrFasCumulante
				, null // aChiaveUfficioFasCumulante
				, Integer.parseInt(lPagina));

		// Recupero il numero totale di record
		BigDecimal lCountRisultati;
		if (isRequestParameterNullObj("CountRisultati")) {
			lCountRisultati = lCtrl.ExCountRicercaMessaggi(null // ICostantiJMS.DELIVERY_MODE_INVIATO
					, ICostantiJMS.RICHIESTA, lListaTipoOperazione, null, lFlag_Visto // Flag_visto: Se
																						// valorizzato ad 'N'
																						// entra nei parametri
																						// di Ricerca
					, lChiaveAnnoSiep // aChiaveAnnoSiep
					, lChiaveProgrSiep // aChiaveProgrSiep
					, null // aChiaveUfficioSiep
					, lCodUfficioMitt // aCodUfficioMitt
					, getCodUfficioUtenteConnesso() // ufficio dest
					, lDataTrasmissioneDal // lDataTrasmissioneDal
					, lDataTrasmissioneAl // lDataTrasmissioneAl
					, lCognome, lNome, null // aChiaveAnnoFasCumulante
					, null // aChiaveProgrFasCumulante
					, null // aChiaveUfficioFasCumulante
			);
		} else {
			lCountRisultati = getRequestBigDecimalParameter("CountRisultati");
		}

		// ---- - - - - - - - - - - -- -
		// Ricerca Solleciti
		if (lVect != null && lVect.size() > 0) {
			for (int i = 0; i < lVect.size(); i++) {
				MessaggioModel lRichiesta = lVect.elementAt(i);

				if (lRichiesta != null && lRichiesta.getIdMessaggio() != null
						&& lRichiesta.getCodEsito() != null
						&& lRichiesta.getCodEsito().compareTo("01006") != 0) {
					IMessaggio lCtrlM = JMSLookupRemote.getMessaggioRemote();
					Vector<MessaggioModel> lVectSolleciti = lCtrlM.ExRicercaSollecitiMessaggioRichiestaAtti(
							ICostantiJMS.RICHIESTA,
							ICostantiJMS.SOLLECITO_RICHIESTA_TRASMISSIONE_ATTI_PER_COMP,
							lRichiesta.getJmsCorrelationIdMessage());
					if (lVectSolleciti != null && lVectSolleciti.size() > 0) {
						siesLogger.debug("Trovati n. " + lVectSolleciti.size() + " solleciti");
						lRichiesta.setMessaggiSollecito(lVectSolleciti);
					}
				}
			}
		}

		// Passo i dati per gestire la paginazione
		setRequestAttribute("CountRisultati", lCountRisultati);
		setRequestAttribute(IWebConstants.NUM_PAGE, lPagina);
		setRequestAttribute(IWebConstants.REQUEST_FOR_PAGING, getCompleteRequestURL());

		setRequestAttribute("Messaggi", lVect);

		return PG_LISTA_RICHIESTE_ATTI_RICEVUTE;
	}

}
