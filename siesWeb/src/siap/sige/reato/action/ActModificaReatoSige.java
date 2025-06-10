package siap.sige.reato.action;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import siap.siep.reato.action.ActModificaReato;

/**
 * ActModificaReatoSige Classe Action per la modifica del Reato in SIGE; L'action specializza
 * siap.siep.reato.action.ActModificaReato poichè la form di input utilizzata è la stessa; questa action
 * utilizza la funzione letturaDati(...) ereditata che valorizza i reati da inserire. Viene poi richiamata la
 * funzione specifica per l'inserimento di reati
 *
 * @version 1.0
 */
public class ActModificaReatoSige extends ActModificaReato {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public String processRequest() throws Exception {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(getClass().getName() + ".processRequest : inizio");
		super.processRequest();

		// Si ritorna indietro al Dettaglio Reato Sige dove è stato posto il punto di ritorno
		return goToRitorno();

	}

}