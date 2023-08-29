package siap.siep.sanzionesostitutiva.action;

import f3b.util.F3BException;
import siap.sico.web.ActionSiap;

/**
 * Title: ActGrigliaOrdineIngiunzioneAltriPagamenti 
 * Description: Classe Action per il caricamento della griglia per la gestione dell'Ordine di Ingiunzione
 * 				ed Altri Pagamenti
 *
 * @author sgioggi
 * @since MEV_2023-33
 * @version 1.0
 */
public class ActGrigliaOrdineIngiunzioneAltriPagamenti extends ActionSiap
		implements ICostantiSanzioneSostitutiva {

	public String processRequest() throws F3BException {

		if (isSessionAttributeNullObj("fascicolo"))
			setRequestAttribute("fascicoloNotInSession", "S");

		return PG_GRIGLIA_ORDINE_INGIUNZIONE_ALTRI_PAGAMENTI;
	}

}