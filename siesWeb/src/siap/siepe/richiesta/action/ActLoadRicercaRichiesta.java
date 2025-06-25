package siap.siepe.richiesta.action;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import siap.sico.web.ActionSiap;

/**
 * ActLoadRicercaRichiesta - Classe Action per la load ricerca di Richiesta
 *
 * @version 1.0
 */
public class ActLoadRicercaRichiesta extends ActionSiap implements ICostantiRichiesta {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public String processRequest() throws Exception {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(this.getClass().getName() + "processRequest(): inizio");
		// --
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(this.getClass().getName() + "processRequest(): fine");

		// restituisce la jsp di VIEW
		return PG_LOAD_RICERCARICHIESTA;
	}

}