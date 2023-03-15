package siap.siep.sanzionesostitutiva.action;

import f3b.util.F3BException;
import siap.sico.web.ActionSiap;

/**
 * MEV_2023-13: aggiunta classe
 * Title: ActGrigliaOrdineIngiunzione
 * Description: Classe Action per il caricamento della griglia per la gestione dell'Ordine di Ingiunzione
 *
 * @author sgioggi
 * @version 1.0
 */
public class ActGrigliaOrdineIngiunzione extends ActionSiap implements ICostantiSanzioneSostitutiva {

	public String processRequest() throws F3BException {

		if (isSessionAttributeNullObj("fascicolo"))
			setRequestAttribute("fascicoloNotInSession", "S");

		return PG_LOAD_GRIGLIA_ORDINE_INGIUNZIONE;
	}

}