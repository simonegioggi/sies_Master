package siap.siep.tipoeventibdmc.action;

import siap.sico.web.ActionSiap;
import f3b.util.F3BException;

/**
 * <p>
 * Title: ActLoadInserisciTipoEventiBdmc
 * </p>
 * <p>
 * Description: Classe Action per la load inserisci di TipoEventiBdmc
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
public class ActLoadInserisciTipoEventiBdmc extends ActionSiap implements ICostantiTipoEventiBdmc {

	/*****************************************************************************
	 * Azione di caricamento della pagina di Inserimento dei dati. Si occupa anche di precaricare tutti i dati
	 * da visualizzare i tale pagina (es: combo)
	 * 
	 * @return Nome della pagina JSP da visualizzare
	 * @throws F3BException
	 *****************************************************************************/
	public String processRequest() throws F3BException {

		// Imposta la Modalità a Inserimento.
		setRequestAttribute("modalita", "I");

		// Restituisce la pagina di Inserimento dei Dati
		return PG_LOAD_INSERISCITIPOEVENTIBDMC;
	}

}