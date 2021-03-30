package siap.siep.avvocato.action;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.GregorianCalendar;
import java.util.List;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.F3BException;
import f3b.util.F3BProperties;
import it.giustizia.www.serviziTelematici.reginde.interrogazioniExt.Indirizzo;
import it.giustizia.www.serviziTelematici.reginde.interrogazioniExt.Ruoloente;
import it.giustizia.www.serviziTelematici.reginde.interrogazioniExt.Soggetti;
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

		///////////////////////////////////////////////////////////////////////////////////////////
		Soggetto so = new Soggetto();
		Soggetti si = new Soggetti();
		si.setCodFisc("GGGSMN72D23H501K");
		si.setCognome("GIOGGI");
		si.setDataNascita(new GregorianCalendar(1972, 4-1, 23));
		si.setLuogoNascita("Roma");
		si.setNome("SIMONE");
		si.setPec("simone.gioggi@peclibero.it");
		si.setProvNascita("RM");
		so.setSoggetto(si);
		Indirizzo i1 = new Indirizzo();
		i1.setCap("12100");
		i1.setComune("CUNEO");
		i1.setEmail("xxx@yyy.it");
		i1.setFax("012345767890");
		i1.setIndirizzo("CORSO FRANCIA 121 D");
		i1.setProv("CN");
		i1.setTelefono("09876543210");
		i1.setTp_indirizzo("D");
		Indirizzo i2 = new Indirizzo();
		i2.setCap("12037");
		i2.setComune("TORINO");
		i2.setEmail("www@eee.it");
		i2.setFax("11111111111");
		i2.setIndirizzo("CORSO FRANCIA 121 E");
		i2.setProv("TO");
		i2.setTelefono("22222222222");
		i2.setTp_indirizzo("R");
		Indirizzo[] ii = new Indirizzo[2];
		ii[0] = i1;
		ii[1] = i2;
		//so.setIndirizzi(ii);
		Ruoloente re1 = new Ruoloente();
		re1.setCodice("99999");
		re1.setCodiceFiscale("XXXXXXXXXXXXXXXX");
		re1.setDescrizione("ENTE FITTIZIO");
		re1.setPartitaIVA("93007290302");
		re1.setPec("ccc@pec.it");
		re1.setPubblicaAmministrazione(true);
		re1.setRuolo("avvocato");
		re1.setStato("attivo");
		re1.setTipologia("Tipologia1");
		Ruoloente re2 = new Ruoloente();
		re2.setCodice("80409200583");
		re2.setCodiceFiscale("8040920058344444cc");
		re2.setDescrizione("CNF - CONSIGLIO NAZIONALE FORENSE");
		re2.setPartitaIVA("1234567890");
		re2.setPec("vvv@pec.it");
		re2.setPubblicaAmministrazione(false);
		re2.setRuolo("cassazionista");
		re2.setStato("attivo");
		re2.setTipologia("Tipologia2");
		Ruoloente[] ri = new Ruoloente[2];
		ri[0] = re1;
		ri[1] = re2;
		so.setRuoliente(ri);
		v = new ArrayList(Arrays.asList(so));
		///////////////////////////////////////////////////////////////////////////////////////////

		setRequestAttribute("formname", getRequestStringParameter("formname"));
		setRequestAttribute("avvocato", v);

		return PG_RICERCA_AVVOCATO_REGINDE;
	}

}