package siap.sius.avvocato.action;

import f3b.web.html.Option;
import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.web.ActionSiap;

// MEV_21: aggiunta classe per chiamata a WS per individuare lista avvocato in RegInde
public class ActFiltraAvvRegInde extends ActionSiap implements ICostantiAvvocato {

	public String processRequest() throws Exception {

		UfficioModel um = getUfficioUtenteConnesso();
		String descrComune = um.getDescrComune();

		Option foro = new Option(DecodificheManager.getInstance().getForo(), descrComune.toUpperCase().trim(),
				Option.NO_BLANK_ITEM);
		setRequestAttribute("foro", "" + foro);

		return PG_FILTRA_AVV_REGINDE;
	}

}