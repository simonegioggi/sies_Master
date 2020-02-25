package siap.sico.profilo.action;

import java.util.Vector;

import siap.sico.profilo.controller.IProfilo;
//import per le combo
//import f3b.web.html.Option;
//import siap.sico.decodifiche.controller.DecodificheManager;import siap.sico.profilo.controller.IProfilo;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import f3b.util.F3BException;

/**
 * <p>
 * Title: ActLoadInserisciProfilo
 * </p>
 * <p>
 * Description: Classe Action per la load inserisci di Profilo
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
public class ActListaProfili extends ActionSiap implements ICostantiProfilo {

	@SuppressWarnings("rawtypes")
	public String processRequest() throws F3BException {

		// Inserire Eventuali ComboBOX
		// Option lOption = new Option( DecodificheManager.getInstance().get???());

		// setRequestAttribute("???", "" + lOption );
		// Imposta Modalità.
		IProfilo IPrf = SICOLookupRemote.getProfiloRemote();
		Vector ListaProfili = IPrf.ExRicercaListaProfili();
		setRequestAttribute("Profili", ListaProfili);
		return PG_LISTA_PROFILI; // restituisce la jsp di VIEW
	}

}