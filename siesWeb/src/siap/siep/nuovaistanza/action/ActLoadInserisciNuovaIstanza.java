package siap.siep.nuovaistanza.action;

import f3b.util.F3BException;
import f3b.web.html.Option;
import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.web.ActionSiap;

/**
 * ActLoadInserisciNuovaIstanza - Classe Action per la load inserisci di NuovaIstanza
 *
 * @version 1.0
 */
public class ActLoadInserisciNuovaIstanza extends ActionSiap implements ICostantiNuovaIstanza {

	/*****************************************************************************
	 * Azione di caricamento della pagina di Inserimento dei dati. Si occupa anche di precaricare tutti i dati
	 * da visualizzare i tale pagina (es: combo)
	 *
	 * @return Nome della pagina JSP da visualizzare
	 * @throws F3BException
	 *****************************************************************************/
	public String processRequest() throws Exception {

		// Inserire Eventuali ComboBOX
		// Option lOption = new Option( DecodificheManager.getInstance().get???());
		// setRequestAttribute("???", "" + lOption );

		// 20210730 MEV_21 Nuova gestione Combo per Stato di Nascita
		Option lOption = new Option(DecodificheManager.getInstance().getNazioni(), "-");
		setRequestAttribute("nazione", "" + lOption);
		setRequestAttribute("nazioneP", "" + lOption);

		// 202108224 MEV_21 Nuova gestione Combo per Foro
		lOption = new Option(DecodificheManager.getInstance().getForo(), Option.BLANK_ITEM);
		setRequestAttribute("foro", "" + lOption);
		setRequestAttribute("foroP", "" + lOption);

		// 20210730 MEV_21 Nuova gestione Combo per Stato Difensore
		lOption = new Option(DecodificheManager.getInstance().getListaAttivitaAvvocato(), "-");
		setRequestAttribute("statoAvv", "" + lOption);
		setRequestAttribute("statoAvvP", "" + lOption);

		// Imposta la Modalità a Inserimento.
		setRequestAttribute("modalita", "I");

		// Restituisce la pagina di Inserimento dei Dati
		return PG_LOAD_INSERISCINUOVAISTANZA;
	}

}