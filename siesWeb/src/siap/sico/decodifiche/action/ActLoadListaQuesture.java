package siap.sico.decodifiche.action;

import java.util.Vector;

import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.web.ActionSiap;
import f3b.util.F3BException;

/**
 * Classe per il caricamento della PopUp con la lista delle Questure
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class ActLoadListaQuesture extends ActionSiap implements ICostantiComune {

	public String processRequest() throws F3BException {

		// ==========================================================================
		//
		// ==========================================================================
		Vector lListaQuesture = new Vector(DecodificheManager.getInstance().getQuesture());
		setRequestAttribute("ListaQuesture", lListaQuesture);

		return PG_LISTA_QUESTURE;
	}

}