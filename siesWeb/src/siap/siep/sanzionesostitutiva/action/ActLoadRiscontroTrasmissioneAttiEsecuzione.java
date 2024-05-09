package siap.siep.sanzionesostitutiva.action;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.web.html.Option;
import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.web.ActionSiap;

/**
 * Classe Action per il caricamento della pagina con la lista Messaggi Inviati per la Gestione Riscontro
 * Trasmissione Atti per Esecuzione pena sostitutiva
 *
 * @author sgioggi
 * @since MEV_2023-33
 * @version 1.0
 */
public class ActLoadRiscontroTrasmissioneAttiEsecuzione extends ActionSiap
		implements ICostantiSanzioneSostitutiva {

	// info per il log
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public String processRequest() throws Exception {

		// info per il log
		siesLogger.debug(getClass().getName() + ".processRequest: inizio");

		// imposta il link per il ritorno
		setLinkRitorno();

		// Imposta Tipo Ufficio
		Option o = new Option(DecodificheManager.getInstance().getTipoUfficioSius());
		setRequestAttribute("tipoUfficio", "" + o);

		// info per il log
		siesLogger.debug(getClass().getName() + ".processRequest: fine");

		// pagina di ritorno
		return PG_LOAD_RICERCA_TRASMISSIONE_ATTI_ESECUZIONE_PS;
	}

}