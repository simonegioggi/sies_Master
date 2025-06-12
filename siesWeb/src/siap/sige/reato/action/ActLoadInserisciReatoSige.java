package siap.sige.reato.action;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.web.html.Option;
import siap.sico.decodifiche.controller.DecodificheManager;
import siap.siep.reato.action.ActLoadInserisciReato;

/**
 * ActLoadInserisciReatoSige - Classe Action visualizza la form Inserimento Reato; l'action specializza
 * siap.siep.reato.action.ActLoadInserisciReato per poter usare la stessa form ma cambiando in questa l'azione
 * successiva
 *
 * @version 1.0
 */
public class ActLoadInserisciReatoSige extends ActLoadInserisciReato {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public String processRequest() throws Exception {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(getClass().getName() + ".processRequest : inizio");

		Option lOption = new Option(DecodificheManager.getInstance().getTipoReato());
		setRequestAttribute("TipiReato", "" + lOption);
		lOption = new Option(DecodificheManager.getInstance().getTipoFonteReato());
		setRequestAttribute("TipiFontiReato", "" + lOption);
		lOption = new Option(DecodificheManager.getInstance().getSottonumerazione());
		setRequestAttribute("TipiSottonumerazione", "" + lOption);
		lOption = new Option(DecodificheManager.getInstance().getPeriodoConsumazione());
		setRequestAttribute("PeriodoConsumazione", "" + lOption);
		lOption = new Option(DecodificheManager.getInstance().getTipoPenaDetentiva());
		setRequestAttribute("TipiPeneDetentive", "" + lOption);

		setRequestAttribute("modalita", "SIGE");

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(getClass().getName() + ".processRequest : fine");

		// restituisce la jsp di VIEW
		return PG_LOAD_INSERISCIREATO;
	}

}