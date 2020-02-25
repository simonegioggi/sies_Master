package siap.siep.fascicolo.action;

import java.util.Vector;

import siap.sico.ufficio.controller.IUfficio;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;

public class ActLoadSvalidaFascicolo extends ActionSiap implements ICostantiFascicoloSiep {

	@SuppressWarnings("rawtypes")
	public String processRequest() throws Exception {

		// model degli elementi del vettore
		// UfficioAccorpatoModel lUAMod = new UfficioAccorpatoModel();
		String codUfficioUtente = getCodUfficioUtenteConnesso();

		// esegue la query per recuperare l'elenco degli uffici accorpati di tipo PM ed appartenenti al
		// distretto dell'utente loggato
		IUfficio lUACon = SICOLookupRemote.getUfficioRemote();
		Vector lUffAcc = lUACon.ListaUfficiAccorpati("PM", codUfficioUtente);

		// imposta sulla request la lista degli uffici accorpati
		setRequestAttribute("elencoUfficiAccorpati", lUffAcc);

		return PG_LOAD_RICERCA_PER_SVALIDAZIONE; // restituisce la jsp di VIEW
	}

}