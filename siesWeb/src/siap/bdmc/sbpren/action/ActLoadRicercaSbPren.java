package siap.bdmc.sbpren.action;

import siap.sico.web.ActionSiap;
import f3b.util.F3BException;

/**
 * <p>
 * Title: ActLoadRicercaSbPren
 * </p>
 * <p>
 * Description: Classe Action per la load ricerca di SbPren
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: Bull
 * </p>
 * 
 * @version 1.0
 */
public class ActLoadRicercaSbPren extends ActionSiap implements ICostantiSbPren {

	/*****************************************************************************
	 * Azione di caricamento della pagina di ricerca. Si occupa di precaricare tutti i dati da visualizzare i
	 * tale pagina (es: combo)
	 * 
	 * @return Nome della pagina JSP da visualizzare
	 * @throws F3BException
	 *****************************************************************************/
	public String processRequest() throws F3BException {

		// =============================================================
		// Aggiungere qui eventuali caricamento di combo o altri dati
		// da passare alla finestra di ricerca, e relative setRequest
		// =============================================================
		// es:
		// Option lOption = new Option(codice per caricare la option);
		// setRequestAttribute("nome_attributo", "" + lOption);

		// restituisce la jsp di visualizzazione della pagina di ricerca
		return PG_LOAD_RICERCASBPREN;
	}

}