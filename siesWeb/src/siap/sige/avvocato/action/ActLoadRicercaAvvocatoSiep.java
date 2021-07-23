package siap.sige.avvocato.action;

import f3b.web.html.Option;

import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.web.ActionSiap;

public class ActLoadRicercaAvvocatoSiep extends ActionSiap implements ICostantiAvvocato {

	public String processRequest() throws Exception {

		UfficioModel lUffUte = getUfficioUtenteConnesso();
		String lDescrComune = lUffUte.getDescrComune();
		setRequestAttribute("comune", lDescrComune);

		// 19/03/2010 Nuova gestione Combo per Foro avvocato.
		Option lOption = new Option(DecodificheManager.getInstance().getForo(),
				lDescrComune.toUpperCase().trim(), Option.NO_BLANK_ITEM);
		setRequestAttribute("foro", "" + lOption);

		String lPage = PG_LOAD_RICERCAAVVOCATO_SIEP;

		return lPage; // restituisce la jsp di VIEW
	}

}