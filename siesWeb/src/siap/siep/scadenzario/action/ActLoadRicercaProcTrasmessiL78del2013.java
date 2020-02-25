package siap.siep.scadenzario.action;

import java.util.Vector;

import f3b.util.F3BException;
import siap.sico.ufficio.controller.IUfficio;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;

/**
 * <p>
 * Title: ActLoadRicercaProcTrasmessiL78del2013
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Eutelia Copyright (c) 2002
 * </p>
 * <p>
 * Company: Eutelia
 * </p>
 */
public class ActLoadRicercaProcTrasmessiL78del2013 extends ActionSiap implements ICostantiScadenzario {

	/**
	 * Azione di caricamento della form di ricerca dei Fascicoli Siep non validati.
	 *
	 * @return Nome della pagina JSP su cui posizionarsi al termine dell'elaborazione
	 *         <p>
	 * @throws F3BException
	 */
	@SuppressWarnings("rawtypes")
	public String processRequest() throws F3BException {

		// esegue la query per recuperare l'elenco degli uffici accorpati di tipo PM ed appartenenti
		// all'ufficio dell'utente loggato
		IUfficio lUACon = SICOLookupRemote.getUfficioRemote();
		String codUfficioUtente = getCodUfficioUtenteConnesso();
		Vector lUffAcc = lUACon.ListaUfficiAccorpati("PM", codUfficioUtente);

		// imposta sulla request la lista degli uffici accorpati
		setRequestAttribute("elencoUfficiAccorpati", lUffAcc);

		return PG_LOAD_RICERCA_PROCEDIMENTI_TRASMESSI_L78_2013; // restituisce la jsp di VIEW
	}

}