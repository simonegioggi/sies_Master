package siap.sige.richiestaatti.action;

import siap.sige.fascicolo.action.ActLoadRicercaFSigePuntuale;

public class ActLoadRicercaStatoAtti extends ActLoadRicercaFSigePuntuale
implements ICostantiRichiestaAtti
{
  public String processRequest() throws Exception
  {
	// Imposta alla JSP il nome della funzione e l'azione
	    // da chiamare alla conferma.
	    setRequestAttribute("functionName", "Visualizza Stato Atti");
	    setRequestAttribute("nextAction", "siap.sige.richiestaatti.action.ActRicercaStatoAtti");
	    // Si invoca il metodo della superclasse.
	    String lPage = super.processRequest();
	    return lPage;
	  
	  
  }
}
