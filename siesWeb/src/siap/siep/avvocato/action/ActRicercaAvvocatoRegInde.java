package siap.siep.avvocato.action;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.F3BException;
import f3b.util.F3BProperties;
import it.giustizia.www.serviziTelematici.reginde.interrogazioniExt.Soggetto;
import it.giustizia.www.serviziTelematici.reginde.interrogazioniInt.SearchLimitException;
import it.giustizia.www.serviziTelematici.reginde.interrogazioniInt.WsServiziInterrogazioneInterni_PortType;
import it.giustizia.www.serviziTelematici.reginde.interrogazioniInt.WsServiziInterrogazioneInterni_ServiceLocator;
import siap.sico.web.ActionSiap;
import siap.siep.avvocato.model.AvvocatoModel;

// MEV_21: aggiunta classe per chiamata a WS per individuare lista avvocato in RegInde
@SuppressWarnings("rawtypes")
public class ActRicercaAvvocatoRegInde extends ActionSiap implements ICostantiAvvocato {

	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	@SuppressWarnings("unchecked")
	public String processRequest() throws F3BException {

		AvvocatoModel am = new AvvocatoModel();
		List v = null;
		am.setCognome(getRequestStringParameter(CAMPO_COGNOME));
		am.setNome(getRequestStringParameter(CAMPO_NOME));
		am.setForo(getRequestStringParameter(CAMPO_FORO));
		if (!isRequestParameterNullObj(CAMPO_CODICE_FISCALE))
			am.setCodiceFiscale(getRequestStringParameter(CAMPO_CODICE_FISCALE));
		am.setCodUffAppartenenza(getCodUfficioUtenteConnesso());

		try {
			siesLogger.debug("Ricerca su RegInde per:");
			siesLogger.debug("Cognome: " + am.getCognome());
			siesLogger.debug("Nome: " + am.getNome());
			siesLogger.debug("Foro: " + am.getForo());
			siesLogger.debug("Tutti i Fori: " + (isRequestChecked(CAMPO_FLAG_TUTTI_FORI) ? "SI" : "NO"));
			// inizio chiamata al servizio REGINDE
			WsServiziInterrogazioneInterni_ServiceLocator service = new WsServiziInterrogazioneInterni_ServiceLocator();
			service.setServiziInterrogazioneInterniBeanPortEndpointAddress(
					F3BProperties.getProperty("EndpointAddress"));
			System.setProperty("javax.net.debug", F3BProperties.getProperty("javax.net.debug"));
			System.setProperty("http.proxyHost", F3BProperties.getProperty("http.proxyHost"));
			System.setProperty("http.proxyPort", F3BProperties.getProperty("http.proxyPort"));
			System.setProperty("https.proxyHost", F3BProperties.getProperty("https.proxyHost"));
			System.setProperty("https.proxyPort", F3BProperties.getProperty("https.proxyPort"));
			WsServiziInterrogazioneInterni_PortType port = service.getServiziInterrogazioneInterniBeanPort();
			Soggetto[] listaSoggetti = null;
			siesLogger.debug(
					"Chiamo ricercaSoggettoComplete(cognome, nome, codiceFiscale, indirizzo, codiceEnte, orderBy, asc)");
			listaSoggetti = port.ricercaSoggettoComplete(am.getCognome() != null ? am.getCognome() : "",
					am.getNome() != null ? am.getNome() : "",
					am.getCodiceFiscale() != null ? am.getCodiceFiscale() : "", null, null, null, null);
			siesLogger.debug("Totale Soggetti (avvocati) trovati: " + listaSoggetti.length);
			if (listaSoggetti != null && listaSoggetti.length > 0) {
				v = new ArrayList(Arrays.asList(listaSoggetti));
				siesLogger.debug("Elementi trovati: " + v.size());
			} else
				siesLogger.debug("Nessun Elemento trovato");
		} catch (SearchLimitException sle) {
			siesLogger.error(sle.getMessage());
			setRequestAttribute("msg",
					"Attenzione: con i parametri inseriti la ricerca ritrova troppe occorrenze, restringere i criteri di ricerca");
		} catch (Exception e) {
			siesLogger.error(e.getMessage());
			setRequestAttribute("msg", "Errore nella ricerca Avvocato su RegInde: " + e.toString());
		}

		setRequestAttribute("formname", getRequestStringParameter("formname"));
		setRequestAttribute("avvocato", v);

		return PG_RICERCA_AVVOCATO_REGINDE;
	}

}