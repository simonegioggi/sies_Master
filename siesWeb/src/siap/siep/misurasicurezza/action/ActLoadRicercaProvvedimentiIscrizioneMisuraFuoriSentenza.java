package siap.siep.misurasicurezza.action;

import org.apache.log4j.Logger;

import siap.sico.web.ActionSiap;
import f3b.log.LogF3B;
import f3b.util.F3BException;

/**
 * <p>
 * Title: ActLoadRicercaProvvedimentiIscrizioneMisuraFuoriSentenza
 * </p>
 * <p>
 * Description: Carica la pagina della Ricerca dei Provvedimenti/Soggetti per
 * </p>
 * <p>
 * l'iscrizione del procedimento di Misura Sicurezza Fuori Sentenza
 * </p>
 * <p>
 * Copyright: Agile Copyright (c) 2014
 * </p>
 * <p>
 * Company: IntersistemiItalia s.p.a.
 * </p>
 * 
 * @version 8.2
 */
public class ActLoadRicercaProvvedimentiIscrizioneMisuraFuoriSentenza extends ActionSiap implements
		ICostantiMisuraSicurezza {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public String processRequest() throws F3BException {

		if (!isRequestParameterNullObj("NumerazioneManuale")
				&& "S".equals(getRequestStringParameter("NumerazioneManuale"))) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger
					.debug(" --XX-- ActLoadRicercaProvvedimentiIscrizioneMisuraFuoriSentenza - NumerazioneManuale = "
							+ getRequestStringParameter("NumerazioneManuale"));
			setRequestAttribute("NumerazioneManuale", "S");
		}

		return PG_LOAD_RICERCA_PROVV_MIS_SIC_FUORI_SENTENZA; // restituisce la jsp di VIEW
	}

}