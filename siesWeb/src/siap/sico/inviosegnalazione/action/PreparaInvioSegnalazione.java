package siap.sico.inviosegnalazione.action;

import org.apache.log4j.Logger;

import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.utente.model.UtenteModel;
import siap.sico.versione.util.VersionProperties;
import siap.sico.web.ActionSiap;
import f3b.log.LogF3B;
import f3b.util.F3BException;
import f3b.web.html.Option;

/**
 * MEV10-s3: Aggiunta classe per gestire l'invio di una segnalazione
 * 
 * @author sgioggi
 * @version 1.0
 *
 */
public class PreparaInvioSegnalazione extends ActionSiap implements ISCostanti {
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public String processRequest() throws F3BException {

		// info per il log
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
		siesLogger.debug(getClass().getName() + ".processRequest: inizio");
		// Combo titolo persona
	    Option titoloPersona = new Option(DecodificheManager.getInstance().getTitoloPersona());
	    setRequestAttribute("titoloPersona", "" + titoloPersona);
	    // Combo funzionalità
	    UtenteModel user = (UtenteModel) getSessionAttribute("UtenteConnesso");
	    Option funzionalita = new Option();
    	String codTipoUff = user.getUfficioUtente().getCodTipoUfficio();
		if (user.getUserProfile().getProfileId().intValue() != 99 &&
				user.getUserProfile().getProfileId().intValue() != 90) {
				if (codTipoUff.equals("UEPE") || codTipoUff.equals("UEPESS")) {
					// per SIEPE tale funzionalità non è concessa!!!
					funzionalita = new Option(DecodificheManager.getInstance().getFunzionalitaSIEPE());
				} else if (codTipoUff.startsWith("TDS") || codTipoUff.startsWith("UDS")) {
					funzionalita = new Option(DecodificheManager.getInstance().getFunzionalitaSIUS());
			} else if (codTipoUff.equals("PGCAP") || codTipoUff.equals("PM") || codTipoUff.equals("PMM")) {
				funzionalita = new Option(DecodificheManager.getInstance().getFunzionalitaSIEP());
				} else {
					funzionalita = new Option(DecodificheManager.getInstance().getFunzionalitaSIGE());
				}
		}
	    setRequestAttribute("funzionalita", "" + funzionalita);
	    // Combo azione
	    Option azione = new Option(DecodificheManager.getInstance().getAzione());
	    setRequestAttribute("azione", "" + azione);
	    // Combo tipologia segnalazione
	    Option tipoSegnalazione = new Option(DecodificheManager.getInstance().getTipoSegnalazione());
	    setRequestAttribute("tipoSegnalazione", "" + tipoSegnalazione);
	    // Combo gravità segnalazione
	    Option gravitaSegnalazione = new Option(DecodificheManager.getInstance().getGravitaSegnalazione());
	    setRequestAttribute("gravitaSegnalazione", "" + gravitaSegnalazione);
	    String versione = VersionProperties.getVersion();
	    setRequestAttribute("versione", "" + versione);

	    // info per il log
	    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
	    siesLogger.debug(getClass().getName() + ".processRequest: fine" );

	    // valore di ritorno
		return PG_LOAD_PAGINA_INVIO_SEGNALAZIONE;
	}

}