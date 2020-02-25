package siap.sico.jms.action;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Vector;

import siap.jms.ICostantiJMS;
import siap.jms.JMSLookupRemote;
import siap.jms.SIAPReceiver;
import siap.jms.config.JMSProperties;
import siap.jms.messaggio.action.ICostantiMessaggio;
import siap.jms.messaggio.controller.IMessaggio;
import siap.sico.web.ActionSiap;
import siap.siep.jms.action.ICostantiSiepJMS;
import siap.siep.jms.controller.IEsitoRicercaJMS;
import siap.siep.util.SIEPLookupRemote;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;

/**
 * <p>
 * Title: classe action che recupera l'esito delle ricerche dei Procedimenti su altri distretti
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2005
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author not attributable
 * @version 1.0
 */
@SuppressWarnings("rawtypes")
public class ActListaEsitiRicercaFascAltreBDI extends ActionSiap implements ICostantiMessaggio,
		ICostantiSicoJMS {

	public String processRequest() throws Exception {

		this.setLinkRitorno();

		// Ricerca Richieste ed Esiti Ricerca
		try {
			// Test sui listener. n.b. questa operazione è asincrona. NON serve a ricevere
			// i messaggi da visualizzare ma solo a 'risvegliare' i listener se sono
			// 'caduti'
			if (JMSProperties.getInstance().getProperty("LISTENER_NUOVA_GESTIONE") != null
					&& JMSProperties.getInstance().getProperty("LISTENER_NUOVA_GESTIONE").trim()
							.equalsIgnoreCase("true")) {
				SIAPReceiver lSIAPReceiver = SIAPReceiver.getInstance();
				lSIAPReceiver.testInArrivo();
				lSIAPReceiver.testInPartenza();
				lSIAPReceiver.testStampa();
			} else {
				SIAPReceiver.getInstance();
			}
		} catch (Exception e) {
			// n.b. verificare se è il caso di rilanciare l'eccezione in caso di errore
			// JMS oppure se è più opportuno visualizzare un messaggio di warning
			// ma consentire comunque all'utente di visualizzare i messaggi già
			// ricevuti.
			// throw e;
		}

		String lPagina = "1";
		if (!isRequestParameterNullObj(IWebConstants.NUM_PAGE))
			lPagina = getRequestStringParameter(IWebConstants.NUM_PAGE);

		Vector lVect = null;

		// Recupero informazioni per i filtri di ricerca.
		String codUfficioMittente = this.getCodUfficioUtenteConnesso();
		String codUfficioDestinatario = "";
		String lAnnoSiep = getRequestStringParameter(ICostantiSicoJMS.CHIAVE_ANNO_SIEP);
		String lProgrSiep = getRequestStringParameter(ICostantiSicoJMS.CHIAVE_PROGR_SIEP);

		setRequestAttribute("annoSiep", lAnnoSiep);
		setRequestAttribute("progrSiep", lProgrSiep);

		// Si Verifica se l'ufficio emittente sia stato correttamente impostato.
		if (!getRequestStringParameter(ICostantiSicoJMS.CAMPO_TIPO_UFFICIO).equals("-")
				&& (!getRequestStringParameter(ICostantiSiepJMS.CAMPO_SEDE_UFFICIO).equals(""))) {
			codUfficioDestinatario = getCodUfficioByCodTipoUfficioDescrComune(
					getRequestStringParameter(ICostantiSicoJMS.CAMPO_TIPO_UFFICIO),
					getRequestStringParameter(ICostantiSiepJMS.CAMPO_SEDE_UFFICIO));
			setRequestAttribute("codUfficioDestinatario", codUfficioDestinatario);
			setRequestAttribute("descrUfficioDestinatario",
					getRequestStringParameter(ICostantiSicoJMS.CAMPO_TIPO_UFFICIO) + " "
							+ getRequestStringParameter(ICostantiSiepJMS.CAMPO_SEDE_UFFICIO));
		}

		Date dataRicercaInizio = (getRequestDateParameter(CAMPO_ANNO_DATA_TRASMISSIONE_INIZIO,
				CAMPO_MESE_DATA_TRASMISSIONE_INIZIO, CAMPO_GIORNO_DATA_TRASMISSIONE_INIZIO));
		Date dataRicercaFine = (getRequestDateParameter(CAMPO_ANNO_DATA_TRASMISSIONE_FINE,
				CAMPO_MESE_DATA_TRASMISSIONE_FINE, CAMPO_GIORNO_DATA_TRASMISSIONE_FINE));
		// String codTipoOperazione = ( getRequestStringParameter(CAMPO_COD_TIPO_OPERAZIONE) );
		String codTipoOperazione = ICostantiJMS.RICERCA_FASCICOLO_PER_TRASFERIMENTO; // "00040";

		String tipoEsito = (getRequestStringParameter(CAMPO_TIPO_ESITO));
		String descTipoEsito = "";
		String tipoUtente = (getRequestStringParameter(CAMPO_TIPO_UTENTE));
		String codUtente = null;
		String descTipoUtente = "Tutti";

		if (tipoUtente.equals("1")) {
			codUtente = this.getCodUtenteConnesso();
			descTipoUtente = "Utente Collegato";
		} else if (tipoUtente.equals("2")) {
			codUtente = (getRequestStringParameter(CAMPO_COD_UTENTE));
			descTipoUtente = "Utente con Codice";
		}
		if (tipoEsito.equals("0")) {
			tipoEsito = null;
			descTipoEsito = "Tutti";
		} else if (tipoEsito.equals("1")) {
			tipoEsito = "10000";
			descTipoEsito = "In attesa di risposta";
		} else if (tipoEsito.equals("2")) {
			tipoEsito = "00000";
			descTipoEsito = "Esito Positivo";
		}

		try {
			// IMessaggio lCrtlMessaggio = JMSLookupRemote.getMessaggioRemote();
			// lVect = lCrtlMessaggio.ExRicercaMessaggioEsitoConFiltri(codUfficio, codUtente, tipoEsito,
			// codTipoOperazione, dataRicercaInizio, dataRicercaFine );
			// lVect = lCrtlMessaggio.ExRicercaMessaggioEsitoRicercaFascConFiltri( lAnnoSiep, lProgrSiep,
			// codUfficioMittente, codUfficioDestinatario, codUtente, tipoEsito, codTipoOperazione,
			// dataRicercaInizio, dataRicercaFine );
			IEsitoRicercaJMS lEsiCntrl = SIEPLookupRemote.getEsitoRicercaJMS();

			BigDecimal CountRisultati = new BigDecimal(0);
			if (isRequestParameterNullObj("CountRisultati"))
				CountRisultati = lEsiCntrl.ExGetCountEsitoRicercaFascAltreBDI(lAnnoSiep, lProgrSiep,
						codUfficioMittente, codUfficioDestinatario, codUtente, tipoEsito, codTipoOperazione,
						dataRicercaInizio, dataRicercaFine);
			else
				CountRisultati = getRequestBigDecimalParameter("CountRisultati");

			IMessaggio lCrtlMessaggio = JMSLookupRemote.getMessaggioRemote();
			lVect = lCrtlMessaggio.ExRicercaMessaggioEsitoRicercaFascConFiltri(lAnnoSiep, lProgrSiep,
					codUfficioMittente, codUfficioDestinatario, codUtente, tipoEsito, codTipoOperazione,
					dataRicercaInizio, dataRicercaFine, Integer.parseInt(lPagina));

			setRequestAttribute("CountRisultati", CountRisultati);
			setRequestAttribute(IWebConstants.NUM_PAGE, lPagina);

			setRequestAttribute(IWebConstants.REQUEST_FOR_PAGING, getCompleteRequestURL());

			setRequestAttribute("Messaggi", lVect);
		} catch (F3BException ex) {
		}

		this.setRequestAttribute("Messaggi", lVect);

		setRequestAttribute("dataRicercaInizio", DateUtils.getDateToString(dataRicercaInizio, "dd/MM/yyyy"));
		setRequestAttribute("dataRicercaFine", DateUtils.getDateToString(dataRicercaFine, "dd/MM/yyyy"));
		setRequestAttribute("tipoEsito", tipoEsito);
		setRequestAttribute("descTipoEsito", descTipoEsito);
		setRequestAttribute("descTipoUtente", descTipoUtente);
		setRequestAttribute("codUtente", codUtente);

		return PG_LISTA_ESITI_RICERCA_FASC_ALTRE_BDI; // restituisce la jsp di VIEW
	}

}