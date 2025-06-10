package siap.siepe.attivita.action;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import siap.sico.ufficio.controller.IUfficio;
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.util.SICOLookupRemote;
import siap.siepe.fascicolo.model.FascicoloSiepeEstesoModel;

/**
 * ActLoadTrasferisciAttivita - Classe Action per il caricamento dela form per il trasferimento Attivita
 *
 * @version 1.0
 */
public class ActLoadTrasferisciAttivita extends ActLoadDettaglioAttivita {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public String processRequest() throws Exception {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(
				this.getClass().getName() + "." + this.getClass().getName() + ".processRequest(): inizio");
		// Dati aggregati relativi al Fascicolo SIEPE
		FascicoloSiepeEstesoModel lFascicoloEsteso = null;

		// Si richiama il Dettaglio
		String lpage = super.processRequest();
		// Si risale ai dati in Sessione
		lFascicoloEsteso = (FascicoloSiepeEstesoModel) getSessionAttribute("FascicoloSiepeEsteso");
		// L'ufficio destinatario è il mittente del Fascicolo SIEPE
		String lCodUfficioMittente = lFascicoloEsteso.getFascicoloSiepe().getCodUfficioMittente();
		IUfficio lUffCtrl = SICOLookupRemote.getUfficioRemote();
		UfficioModel lUffDestinatario = lUffCtrl.ExRicercaUfficioByCod(lCodUfficioMittente);
		this.setRequestAttribute("ufficioDestinatario", lUffDestinatario);

		// Imposta Modalità.
		setRequestAttribute("modalita", "T");

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(
				this.getClass().getName() + "." + this.getClass().getName() + ".processRequest(): fine");

		// restituisce la jsp di VIEW
		return lpage;
	}

}