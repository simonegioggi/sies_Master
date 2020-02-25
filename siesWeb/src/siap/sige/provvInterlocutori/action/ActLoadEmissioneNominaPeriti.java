package siap.sige.provvInterlocutori.action;

import siap.sige.fascicolo.action.ActLoadRicercaFSigePuntuale;
import siap.sige.provvedimento.action.ICostantiProvvedimentoSige;

public class ActLoadEmissioneNominaPeriti extends ActLoadRicercaFSigePuntuale
implements ICostantiProvvedimentoSige
{
	public String processRequest() throws Exception
	{
		// Imposta alla JSP il nome della funzione e l'azione
		// da chiamare alla conferma.
		
		
	    
		
		setRequestAttribute("functionName", "Nomina Periti");
		setRequestAttribute("nextAction", "siap.sige.provvInterlocutori.action.ActLoadNominaPeriti");
		// Si invoca il metodo della superclasse.
		String lPage = super.processRequest();
		return lPage;
	}
}