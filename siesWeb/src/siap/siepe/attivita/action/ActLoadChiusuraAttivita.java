package siap.siepe.attivita.action;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.web.IWebConstants;
import f3b.web.html.Option;
import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.lock.controller.LockController;
import siap.sico.lock.model.LockModel;

/**
 * ActLoadChiusuraAttivita - Classe Action per il caricamento dela form per la chiusura dell'Attivita
 *
 * @version 1.0
 */
public class ActLoadChiusuraAttivita extends ActLoadDettaglioAttivita {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public String processRequest() throws Exception {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(
				this.getClass().getName() + "." + this.getClass().getName() + ".processRequest(): inizio");

		// Lock
		LockModel lck = LockController.lockIfNotLocked(getServletContext(), "ATTIVITA",
				getRequestStringParameter(CAMPO_ID_ATTIVITA), getCodUtenteConnesso(), getSession().getId());

		if (lck != null) {
			setRequestAttribute(IWebConstants.MESSAGE_TEXT,
					"L' " + lck.getEntity() + " è in gestione ad un altro utente!<BR>Riprovare più tardi !");
			return IWebConstants.PG_MESSAGE;
		}

		// Dati aggregati relativi al Fascicolo SIEPE
		// FascicoloSiepeEstesoModel lFascicoloEsteso = null;

		// Si richiama il Dettaglio
		String lpage = super.processRequest();

		// Preparazione della Combo Esito Attività
		Option lOption = new Option(DecodificheManager.getInstance().getEsitoAttivitaSiepe());
		setRequestAttribute("esiti", "" + lOption);

		// Imposta Modalità.
		setRequestAttribute("modalita", "C");

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(
				this.getClass().getName() + "." + this.getClass().getName() + ".processRequest(): fine");

		// restituisce la jsp di VIEW
		return lpage;
	}

}