package siap.sius.avvocato.action;

import siap.sico.web.ActionSiap;

// MEV_21: aggiunta classe per chiamata a WS per individuare lista avvocato in RegInde
public class ActLoadRicercaAvvocatoRegInde extends ActionSiap implements ICostantiAvvocato {

	public String processRequest() throws Exception {

		return PG_LOAD_RICERCA_AVVOCATO_REGINDE;
	}

}