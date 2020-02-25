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
import siap.siep.jms.controller.IEsitoRicercaJMS;
import siap.siep.util.SIEPLookupRemote;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;

/**
 *
 * <p>
 * Title:ActListaEsitiRicercaSoggAltreBDI
 * </p>
 * <p>
 * Description: Ricerca gli Esiti per le ricerche Soggetto Effettuato
 * </p>
 * <p>
 * Copyright: Copyright (c) 2005
 * </p>
 */
public class ActListaEsitiRicercaSoggAltreBDI extends ActionSiap implements ICostantiSicoJMS,
		ICostantiMessaggio {

	@SuppressWarnings("rawtypes")
	public String processRequest() throws Exception {

		setLinkRitorno();

		// Ricerca Richieste ed Esiti Ricerca
		try {
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
		} catch (F3BException ex) {
			// Non si è collegato a OpenJMS ma per visualizzare gli esiti non ha importanza...
		}

		String lPagina = "1";
		if (!isRequestParameterNullObj(IWebConstants.NUM_PAGE))
			lPagina = getRequestStringParameter(IWebConstants.NUM_PAGE);

		Vector lVect = null;

		// Recupero informazioni per i filtri di ricerca.
		String codUfficioMittente = getCodUfficioUtenteConnesso();
		Date dataRicercaInizio = (getRequestDateParameter(CAMPO_ANNO_DATA_TRASMISSIONE_INIZIO,
				CAMPO_MESE_DATA_TRASMISSIONE_INIZIO, CAMPO_GIORNO_DATA_TRASMISSIONE_INIZIO));

		// Le ricerche scadono dopo 7 giorni!
		Date dataMinima = DateUtils.getSysDate();

		for (int j = 0; j < 7; j++)
			dataMinima = DateUtils.getDayBefore(dataMinima);

		if (dataRicercaInizio == null || dataRicercaInizio.before(dataMinima))
			dataRicercaInizio = dataMinima;

		Date dataRicercaFine = (getRequestDateParameter(CAMPO_ANNO_DATA_TRASMISSIONE_FINE,
				CAMPO_MESE_DATA_TRASMISSIONE_FINE, CAMPO_GIORNO_DATA_TRASMISSIONE_FINE));
		String codTipoOperazione = ICostantiJMS.RICERCA_SOGGETTO; // "00010";

		String tipoUtente = (getRequestStringParameter(CAMPO_TIPO_UTENTE));
		String codUtente = null;
		String descTipoUtente = "Tutti";

		if (tipoUtente.equals("1")) {
			codUtente = getCodUtenteConnesso();
			descTipoUtente = "Utente Collegato";
		} else if (tipoUtente.equals("2")) {
			codUtente = (getRequestStringParameter(CAMPO_COD_UTENTE));
			descTipoUtente = "Utente con Codice";
		}

		IMessaggio lCrtl = JMSLookupRemote.getMessaggioRemote();
		lVect = lCrtl.ExRicercaMessaggioEsitoRicercaSoggettoPerUfficioPaged(codUfficioMittente, codUtente,
				codTipoOperazione, dataRicercaInizio, dataRicercaFine, Integer.parseInt(lPagina));
		// lVect = lCrtl.ExRicercaMessaggioEsitoRicercaSoggettoPerUfficioPaged(getCodUfficioUtenteConnesso(),
		// getCodUtenteConnesso(), RICERCA_SOGGETTO,Integer.parseInt(lPagina));

		BigDecimal CountRisultati;
		if (isRequestParameterNullObj("CountRisultati")) {
			IEsitoRicercaJMS lEsiCntrl = SIEPLookupRemote.getEsitoRicercaJMS();
			CountRisultati = lEsiCntrl.ExGetCountEsitoRicercaSoggAltreBDI(codUfficioMittente, codUtente,
					codTipoOperazione, dataRicercaInizio, dataRicercaFine);
			// CountRisultati=lCrtl.ExGetCountMessaggioRichiestaRicercaSoggettoPerUfficioPaged(getCodUfficioUtenteConnesso(),
			// getCodUtenteConnesso(), RICERCA_SOGGETTO);
		} else
			CountRisultati = getRequestBigDecimalParameter("CountRisultati");

		setRequestAttribute("CountRisultati", CountRisultati);
		setRequestAttribute(IWebConstants.NUM_PAGE, lPagina);

		setRequestAttribute(IWebConstants.REQUEST_FOR_PAGING, getCompleteRequestURL());

		setRequestAttribute("Messaggi", lVect);

		setRequestAttribute("dataRicercaInizio", DateUtils.getDateToString(dataRicercaInizio, "dd/MM/yyyy"));
		setRequestAttribute("dataRicercaFine", DateUtils.getDateToString(dataRicercaFine, "dd/MM/yyyy"));
		setRequestAttribute("descTipoUtente", descTipoUtente);
		setRequestAttribute("codUtente", codUtente);

		return PG_LISTA_MESSAGGI_RICERCA_SOGGETTO; // restituisce la jsp di VIEW
	}

}