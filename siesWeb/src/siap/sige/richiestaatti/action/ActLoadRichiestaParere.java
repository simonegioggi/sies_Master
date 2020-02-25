package siap.sige.richiestaatti.action;

import siap.sige.fascicolo.action.ActLoadRicercaFSigePuntuale;

public class ActLoadRichiestaParere extends ActLoadRicercaFSigePuntuale implements ICostantiRichiestaAtti {

	public String processRequest() throws Exception {

		// Imposta alla JSP il nome della funzione e l'azione
      	// da chiamare alla conferma.
      	setRequestAttribute("functionName", "Richiesta parere ");
      	setRequestAttribute("nextAction", "siap.sige.richiestaatti.action.ActRichiestaParere");
      	// Si invoca il metodo della superclasse.
     	String lPage = super.processRequest();

     	// Bottone di ritorno
      	setLinkRitorno();
      	return lPage;
  	}

}