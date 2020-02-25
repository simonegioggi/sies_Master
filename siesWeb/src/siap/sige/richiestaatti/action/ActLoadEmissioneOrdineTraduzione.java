package siap.sige.richiestaatti.action;

import siap.sige.fascicolo.action.ActLoadRicercaFSigePuntuale;
import siap.sige.provvedimento.action.ICostantiProvvedimentoSige;

public class ActLoadEmissioneOrdineTraduzione extends ActLoadRicercaFSigePuntuale
implements ICostantiProvvedimentoSige
{
	public String processRequest() throws Exception
	{
		// Imposta alla JSP il nome della funzione e l'azione
		// da chiamare alla conferma.
		
		
	    
		
		setRequestAttribute("functionName", "Ordine Traduzione");
		setRequestAttribute("nextAction", "siap.sige.richiestaatti.action.ActLoadOrdineTraduzione");
		// Si invoca il metodo della superclasse.
		String lPage = super.processRequest();
		return lPage;
	}
}