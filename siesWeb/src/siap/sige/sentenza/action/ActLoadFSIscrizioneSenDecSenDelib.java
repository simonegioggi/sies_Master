package siap.sige.sentenza.action;

import siap.sige.fascicolo.action.ActLoadRicercaFSigePuntuale;

public class ActLoadFSIscrizioneSenDecSenDelib extends ActLoadRicercaFSigePuntuale {

	public String processRequest() throws Exception {

		/*
		 * ISSUE MEV : recupero parametro isSentenza Numero MEV : 15_S4 Autore : sessa Data : 28/gen/2016
		 * Branch : MEV_15_S4
		 */
		String isSentenza = "";
		if (!isRequestParameterNullObj("isSentenza"))
			// 20191106 [SG]: apportata correzione per funzionalità "Assegnazione procedimento SIGE"
			// da "Iscrizione manuale » Ricerca Procedimento SIEP per Numero"
			// isSentenza = (String) super.getRequestAttribute("isSentenza");
			isSentenza = super.getRequestStringParameter("isSentenza");
		else
			isSentenza = (String) super.getSessionAttribute("isSentenza");
		// ***** FINE INTERVENTO MEV_15_S4 *****//

		setRequestAttribute("functionName",
				"Iscrizione Sentenza, Decreto Penale, Sentenza Straniera Delibata");
		setRequestAttribute("nextAction", "siap.sige.sentenza.action.ActRicercaFSPIscrizioneSenDecSenDelib");
		setRequestAttribute("isSentenza", isSentenza);
		// Si invoca il metodo della superclasse.
		String lPage = super.processRequest();
		return lPage;
	}

}