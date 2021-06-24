package siap.siep.avvocato.action;

import java.util.List;

import javax.xml.ws.BindingProvider;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.F3BException;
import f3b.util.F3BProperties;
import f3b.util.Utils;
import it.giustizia.serviziTelematici.reginde.interrogazioniExt.Soggetto;
import it.giustizia.serviziTelematici.reginde.interrogazioniInt.SearchLimitException_Exception;
import it.giustizia.serviziTelematici.reginde.interrogazioniInt.WsServiziInterrogazioneInterni;
import it.giustizia.serviziTelematici.reginde.interrogazioniInt.WsServiziInterrogazioneInterni_Service;
import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.decodifiche.util.DecodificheUtils;
import siap.sico.web.ActionSiap;
import siap.siep.avvocato.model.AvvocatoModel;

// MEV_21: aggiunta classe per chiamata a WS per individuare lista avvocato in RegInde
public class ActRicercaAvvocatoRegInde extends ActionSiap implements ICostantiAvvocato {

	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public String processRequest() throws F3BException {

		AvvocatoModel am = new AvvocatoModel();
		am.setCognome(getRequestStringParameter(CAMPO_COGNOME));
		am.setNome(getRequestStringParameter(CAMPO_NOME));
		am.setForo(getRequestStringParameter(CAMPO_FORO));
		if (!isRequestParameterNullObj(CAMPO_CODICE_FISCALE))
			am.setCodiceFiscale(getRequestStringParameter(CAMPO_CODICE_FISCALE));
		am.setCodUffAppartenenza(getCodUfficioUtenteConnesso());
		String foro = null;
		List<Soggetto> listaSoggetti = null;

		try {
			siesLogger.debug("Ricerca su RegInde per:");
			siesLogger.debug("Cognome: " + am.getCognome());
			siesLogger.debug("Nome: " + am.getNome());
			siesLogger.debug("Codice Fiscale: "
					+ (Utils.isPresent(am.getCodiceFiscale()) ? am.getCodiceFiscale() : ""));
			siesLogger.debug("Foro: " + am.getForo());
			siesLogger.debug("Tutti i Fori: " + (isRequestChecked(CAMPO_FLAG_TUTTI_FORI) ? "SI" : "NO"));
			if (!isRequestChecked(CAMPO_FLAG_TUTTI_FORI)) {
				foro = DecodificheUtils.getCodAlt2byCode(DecodificheManager.getInstance().getForoAll(),
						am.getForo());
				foro = "COA" + foro;
				siesLogger.debug("COA + Foro: " + foro);
			}
			// inizio chiamata al servizio REGINDE
			WsServiziInterrogazioneInterni_Service service = new WsServiziInterrogazioneInterni_Service();
			siesLogger.debug("service = " + service.toString());
			WsServiziInterrogazioneInterni ws = service.getServiziInterrogazioneInterniBeanPort();
			siesLogger.debug("ws = " + ws.toString());
			BindingProvider bindingProvider = (BindingProvider) ws;
			bindingProvider.getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY,
					F3BProperties.getProperty("EndpointAddress"));
			siesLogger.debug("EndpointAddress = "
					+ bindingProvider.getRequestContext().get(BindingProvider.ENDPOINT_ADDRESS_PROPERTY));
			// ricercaSoggettoComplete(cognome, nome, codiceFiscale, indirizzo, codiceEnte, orderBy, asc)
			// Gestione CF: il sistema ricercherà tutti gli avvocati con il codice fiscale indicato in tutti i
			// fori, non considerando il contenuto degli altri campi!!!
			if (Utils.isPresent(am.getCodiceFiscale()))
				listaSoggetti = ws.ricercaSoggettoComplete(null, null, am.getCodiceFiscale(), null, null,
						null, null);
			else
				listaSoggetti = ws.ricercaSoggettoComplete(am.getCognome() != null ? am.getCognome() : "",
						am.getNome() != null ? am.getNome() : "", null, null, foro, null, null);
			if (listaSoggetti != null && listaSoggetti.size() > 0) {
				siesLogger.debug("Totale Soggetti (avvocati) trovati: " + listaSoggetti.size());
				if (listaSoggetti.size() > 20) // max 200 avvocati
					throw new SearchLimitException_Exception();
			} else
				siesLogger.debug("Nessun Avvocato trovato!");
		} catch (SearchLimitException_Exception sle) {
			sle.printStackTrace();
			siesLogger.error("Errore in " + getClass().getName() + ": " + sle.getMessage());
			setRequestAttribute("msg",
					"Attenzione: con i parametri inseriti la ricerca ritrova troppe occorrenze, restringere i criteri di ricerca!");
		} catch (Exception e) {
			e.printStackTrace();
			siesLogger.error("Errore in " + getClass().getName() + ": " + e.toString());
			if (!Utils.isNullObj(e) && !Utils.isNullObj(e.getMessage())
					&& e.getMessage().contains("Unrecognized")) {
				siesLogger.error("Errore in " + getClass().getName() + ": " + e.getMessage());
				setRequestAttribute("msg",
						"Attenzione: collegamento con RegIndE assente!\\nE' possibile effettuare la ricerca del Difensore su SIES!");
			}
		}

		setRequestAttribute("formname", getRequestStringParameter("formname"));
		setRequestAttribute("avvocato", listaSoggetti);

		return PG_RICERCA_AVVOCATO_REGINDE;
	}

}