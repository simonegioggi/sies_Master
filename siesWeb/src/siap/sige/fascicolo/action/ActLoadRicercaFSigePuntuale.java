package siap.sige.fascicolo.action;

import java.util.Vector;

import siap.sico.ufficio.controller.IUfficio;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;

/**
 * <p>
 * Title: ActLoadRicercaFSgePuntuale
 * </p>
 * <p>
 * Description: Classe Action per la load di RicercaFSigePuntuale
 * </p>
 * <p>
 * Copyright: Copyright (c) 2008
 * </p>
 * <p>
 * Company: Eutelia
 * </p>
 *
 * @version 1.0
 */
public class ActLoadRicercaFSigePuntuale extends ActionSiap implements ICostantiFascicoloSige {

	@SuppressWarnings("rawtypes")
	public String processRequest() throws Exception {

		// esegue la query per recuperare l'elenco degli uffici accorpati dall'ufficio dell'utente loggato
		String codUfficioUtente = getCodUfficioUtenteConnesso();
		IUfficio lUACon = SICOLookupRemote.getUfficioRemote();
		Vector lUffAcc = lUACon.ListaUfficiAccorpati(null, codUfficioUtente);

		// imposta sulla request la lista degli uffici accorpati
		setRequestAttribute("elencoUfficiAccorpati", lUffAcc);

		return PG_LOAD_RICERCAFSIGEPUNTUALE; // restituisce la jsp di VIEW
	}

}