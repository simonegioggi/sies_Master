package siap.siep.scadenzario.action;

import siap.sico.web.ActionSiap;
import f3b.util.F3BException;

/**
 * MEV_39
 * <p>
 * Title: ActLoadRicercaScadenzarioDifferimentoMisureSicurezza
 * </p>
 * <p>
 * Description: Classe Action per la load ricerca di Scadenzario per
 * </p>
 * <p>
 * Differimento di procedimenti di Misure Sicurezza
 * </p>
 * <p>
 * Copyright: Copyright (c) 2017
 * </p>
 * <p>
 * Company: EII
 * </p>
 * 
 * @author SGIOGGI
 * @version 1.0
 */
public class ActLoadRicercaScadenzarioDifferimentoMisureSicurezza extends ActionSiap implements
		ICostantiScadenzario {

	public String processRequest() throws F3BException {

		if (!isRequestParameterNullObj("tipo")
				&& getRequestStringParameter("tipo") != null)
			setRequestAttribute("tipo", getRequestStringParameter("tipo"));
		if (!isRequestParameterNullObj("giorniScadenza")
				&& getRequestStringParameter("giorniScadenza") != null)
			setRequestAttribute("giorniScadenza", getRequestStringParameter("giorniScadenza"));
		if (!isRequestParameterNullObj("mesiScadenza")
				&& getRequestStringParameter("mesiScadenza") != null)
			setRequestAttribute("mesiScadenza", getRequestStringParameter("mesiScadenza"));
		if (!isRequestParameterNullObj("anniScadenza")
				&& getRequestStringParameter("anniScadenza") != null)
			setRequestAttribute("anniScadenza", getRequestStringParameter("anniScadenza"));
		// pagina di ritorno
		return PG_LOAD_RICERCASCADENZARIODIFFERIMENTO_MS;
	}

}