package siap.sius.stampa.action;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
import siap.sico.web.ActionSiap;
import siap.sius.cancassfascsius.action.ICostantiCancAssFascSius;
import siap.sius.fascicolo.action.ICostantiFascicoloSius;
import siap.sius.fascicolo.model.FascicoloSiusModel;
import siap.sius.stampa.controller.IStampaSius;
import siap.sius.util.SIUSLookupRemote;

/**
 * MEV_65: aggiunta action per gestire nuova funzionalita'
 * 
 * @author Gioggi
 * @version 1.0
 */
public class ActRicercaCopertineFascicoliSius extends ActionSiap implements ICostantiFascicoloSius {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	@SuppressWarnings("rawtypes")
	public String processRequest() throws Exception {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(getClass().getName() + " .processRequest: inizio");

		String lPage = IWebConstants.ROOT_DIR + "files/siap/sius/stampa/StampaCopertineFascicoliSius.jsp";

		FascicoloSiusModel fsm = new FascicoloSiusModel();

		if (!isRequestParameterNullObj(CAMPO_CHIAVE_ANNO_INIZIALE))
			fsm.setChiaveAnnoIniziale(getRequestBigDecimalParameter(CAMPO_CHIAVE_ANNO_INIZIALE));
		if (!isRequestParameterNullObj(CAMPO_CHIAVE_PROGR_INIZIALE))
			fsm.setChiaveProgrIniziale(getRequestBigDecimalParameter(CAMPO_CHIAVE_PROGR_INIZIALE));
		if (!isRequestParameterNullObj(CAMPO_CHIAVE_ANNO_FINALE))
			fsm.setChiaveAnnoFinale(getRequestBigDecimalParameter(CAMPO_CHIAVE_ANNO_FINALE));
		if (!isRequestParameterNullObj(CAMPO_CHIAVE_PROGR_FINALE))
			fsm.setChiaveProgrFinale(getRequestBigDecimalParameter(CAMPO_CHIAVE_PROGR_FINALE));
		setRequestAttribute(CAMPO_CHIAVE_ANNO_INIZIALE,
				getRequestStringParameter(CAMPO_CHIAVE_ANNO_INIZIALE));
		setRequestAttribute(CAMPO_CHIAVE_PROGR_INIZIALE,
				getRequestStringParameter(CAMPO_CHIAVE_PROGR_INIZIALE));
		setRequestAttribute(CAMPO_CHIAVE_ANNO_FINALE, getRequestStringParameter(CAMPO_CHIAVE_ANNO_FINALE));
		setRequestAttribute(CAMPO_CHIAVE_PROGR_FINALE, getRequestStringParameter(CAMPO_CHIAVE_PROGR_FINALE));

		// Lettura Competenza
		String codiceCompetenza = getRequestStringParameter(CAMPO_INCLUDE_UFFICIO);
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("Codice Competenza: " + codiceCompetenza);
		setRequestAttribute("competenza", codiceCompetenza);
		if ("0".equals(codiceCompetenza)) {
			// Visualizza solo i procedimenti dall'Ufficio
			String codUfficio = getCodUfficioUtenteConnesso();
			fsm.setCodUfficioInserimento(codUfficio);
			fsm.setChiaveUfficio(codUfficio);
		} else
			// Visualizza solo i procedimenti dall'Utente
			fsm.setCodOperatoreInserimento(getUtenteConnesso().getUserId());

		// Lettura Codice Cancelleria
		String codCancelleria = getRequestStringParameter(
				ICostantiCancAssFascSius.CAMPO_COD_CANCELLERIA_ASSEGNATARIA);
		String descCancelleria = getRequestStringParameter("descCancelleria");
		if (codCancelleria.trim().length() > 0) {
			fsm.setCodCancelleria(codCancelleria);
			setRequestAttribute("descCancelleria", descCancelleria);
			setRequestAttribute("codCancelleria", codCancelleria);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Cancelleria: " + codCancelleria + " # " + descCancelleria);
		}

		// Intervallo date
		Date dataIniziale = getRequestDateParameter(CAMPO_ANNO_ISCRIZIONE_INIZIALE,
				CAMPO_MESE_ISCRIZIONE_INIZIALE, CAMPO_GIORNO_ISCRIZIONE_INIZIALE);
		Date dataFinale = getRequestDateParameter(CAMPO_ANNO_ISCRIZIONE_FINALE, CAMPO_MESE_ISCRIZIONE_FINALE,
				CAMPO_GIORNO_ISCRIZIONE_FINALE);

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("data iniziale: " + DateUtils.getDateToString(dataIniziale, "dd/MM/yyyy"));
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("data finale: " + DateUtils.getDateToString(dataFinale, "dd/MM/yyyy"));

		fsm.setDataIscrizioneIniziale(dataIniziale);
		fsm.setDataIscrizioneFinale(dataFinale);
		setRequestAttribute("dataIniziale", DateUtils.getDateToString(dataIniziale, "dd/MM/yyyy"));
		setRequestAttribute("dataFinale", DateUtils.getDateToString(dataFinale, "dd/MM/yyyy"));

		// Inizializzazione numero di pagina
		String pagina = "1";
		if (!isRequestParameterNullObj(IWebConstants.NUM_PAGE))
			pagina = getRequestStringParameter(IWebConstants.NUM_PAGE);

		IStampaSius iss = SIUSLookupRemote.getStampaRemote();
		try {
			Vector v = iss.ExRicercaCopertineFascicoliSius(fsm, Integer.parseInt(pagina));
			if (v != null)
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("Risultati ricerca paginata: " + v.size() + " fascicoli!");
			setRequestAttribute("fascicoli", v);
		} catch (F3BException ex) {
			lPage = IWebConstants.PG_MESSAGE;
			setRequestAttribute(IWebConstants.MESSAGE_TEXT,
					"Nessun procedimento nell'intervallo selezionato!");
		}

		// Paginazione
		BigDecimal CountRisultati;
		if (isRequestParameterNullObj("CountRisultati"))
			CountRisultati = iss.ExContaCopertineFascicoliSius(fsm);
		else
			CountRisultati = getRequestBigDecimalParameter("CountRisultati");
		setRequestAttribute("CountRisultati", CountRisultati);
		setRequestAttribute(IWebConstants.NUM_PAGE, pagina);
		setRequestAttribute(IWebConstants.REQUEST_FOR_PAGING, getCompleteRequestURL());

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(getClass().getName() + " .processRequest: fine ");

		// pagina di ritorno
		return lPage;
	}

}